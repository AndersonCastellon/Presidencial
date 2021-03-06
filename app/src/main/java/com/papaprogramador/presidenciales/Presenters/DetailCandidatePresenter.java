package com.papaprogramador.presidenciales.Presenters;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.UseCases.GetDepartmentUser;
import com.papaprogramador.presidenciales.UseCases.GetTotalVotes;
import com.papaprogramador.presidenciales.UseCases.GetVoteCurrentUser;
import com.papaprogramador.presidenciales.InterfacesMVP.DetailCandidateContract;
import com.papaprogramador.presidenciales.Utils.StaticMethods.GetStringShareApp;
import com.papaprogramador.presidenciales.Utils.StaticMethods.IntoFirebaseDatabase;

public class DetailCandidatePresenter extends MvpBasePresenter<DetailCandidateContract.View>
		implements DetailCandidateContract.Presenter {

	private FirebaseAuth firebaseAuth;

	private FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
		@Override
		public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
			FirebaseUser user = firebaseAuth.getCurrentUser();
			if (user != null) {

				uidUser = user.getUid();

				ifViewAttached(new ViewAction<DetailCandidateContract.View>() {
					@Override
					public void run(@NonNull DetailCandidateContract.View view) {
						view.getToolbar(nameCandidate);
						view.getImgCandidate(urlImgCandidate);
						view.onWebViewSettings(urlHtmlCandidate);
					}
				});
			}
		}
	};

	private String idCandidate;
	private String nameCandidate;
	private String urlImgCandidate;
	private String urlHtmlCandidate;
	private String urlPoliticalFlag;

	private String uidUser;

	public DetailCandidatePresenter(String idCandidate, String nameCandidate,
	                                String urlImgCandidate, String urlHtmlCandidate, String urlPoliticalFlag) {
		this.idCandidate = idCandidate;
		this.nameCandidate = nameCandidate;
		this.urlImgCandidate = urlImgCandidate;
		this.urlHtmlCandidate = urlHtmlCandidate;
		this.urlPoliticalFlag = urlPoliticalFlag;
		firebaseAuth = FirebaseAuth.getInstance();
	}

	@Override
	public void fabShare(Context context) {

		final String shareCandidateString = GetStringShareApp.getStringShareCandidate(nameCandidate, context);

		ifViewAttached(new ViewAction<DetailCandidateContract.View>() {
			@Override
			public void run(@NonNull DetailCandidateContract.View view) {
				view.shareCandidate(shareCandidateString);
			}
		});
	}

	@Override
	public void goCurrentVote() {
		ifViewAttached(new ViewAction<DetailCandidateContract.View>() {
			@Override
			public void run(@NonNull final DetailCandidateContract.View view) {
				view.showProgressFab(true);
				new GetVoteCurrentUser(uidUser, new GetVoteCurrentUser.VoteCurrentUserListener() {
					@Override
					public void onResult(DataSnapshot vote) {
						if (vote.getValue() != null) {
							view.showProgressFab(false);
							view.existingVote();
						} else {
							getDepartment();
						}
					}
				});
			}
		});
	}

	@Override
	public void getDepartment() {
		ifViewAttached(new ViewAction<DetailCandidateContract.View>() {
			@Override
			public void run(@NonNull final DetailCandidateContract.View view) {
				new GetDepartmentUser(uidUser, new GetDepartmentUser.DepartmentUserListener() {
					@Override
					public void onResult(DataSnapshot department) {
						if (department.getValue() == null) {
							view.showProgressFab(false);
							view.goSelectDepartmentDialogFragment(uidUser);
						} else {
							applyVotes();
						}
					}

					@Override
					public void onError(String error) {

					}
				});
			}
		});
	}

	@Override
	public void applyVotes() {
		new GetTotalVotes(idCandidate, new GetTotalVotes.VotesListener() {
			@Override
			public void onResult(int votes) {

				IntoFirebaseDatabase.applyNewVoteCandidate(votes, idCandidate);
				IntoFirebaseDatabase.applyNewVoteUser(idCandidate, urlPoliticalFlag, uidUser);

				ifViewAttached(new ViewAction<DetailCandidateContract.View>() {
					@Override
					public void run(@NonNull DetailCandidateContract.View view) {
						view.showProgressFab(false);
						view.applyNewVoteIsSuccesful();
					}
				});
			}
		});
	}

	@Override
	public void setAuthListener() {
		firebaseAuth.addAuthStateListener(authStateListener);
	}

	@Override
	public void removeAuthListener() {
		firebaseAuth.removeAuthStateListener(authStateListener);
	}
}
