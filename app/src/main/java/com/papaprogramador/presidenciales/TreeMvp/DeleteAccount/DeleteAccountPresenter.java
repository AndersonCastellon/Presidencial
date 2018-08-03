package com.papaprogramador.presidenciales.TreeMvp.DeleteAccount;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.UseCases.GetTotalVotes;
import com.papaprogramador.presidenciales.UseCases.GetVoteCurrentUser;
import com.papaprogramador.presidenciales.Utils.StaticMethods.GetFirebaseUser;
import com.papaprogramador.presidenciales.UseCases.GetIdDevice;
import com.papaprogramador.presidenciales.Utils.StaticMethods.IntoFirebaseDatabase;
import com.papaprogramador.presidenciales.Utils.StaticMethods.ReauthenticateUserFirebase;

import java.util.Objects;


public class DeleteAccountPresenter extends MvpBasePresenter<DeleteAccountContract.View>
		implements DeleteAccountContract.Presenter {

	private FirebaseUser firebaseUser;
	private String emailCurrentUser;
	private Context context;

	DeleteAccountPresenter(Context context) {
		firebaseUser = GetFirebaseUser.getFirebaseUser();
		emailCurrentUser = firebaseUser.getEmail();

		this.context = context;
	}

	@Override
	public void getEmailCurrentUser() {

		ifViewAttached(new ViewAction<DeleteAccountContract.View>() {
			@Override
			public void run(@NonNull DeleteAccountContract.View view) {
				view.setEmailCurrentUser(emailCurrentUser);
			}
		});
	}

	@Override
	public void validateCredentials(String emailUser, String passwordUser) {
		ifViewAttached(new ViewAction<DeleteAccountContract.View>() {
			@Override
			public void run(@NonNull DeleteAccountContract.View view) {
				view.showProgress(true);
			}
		});

		if (!ReauthenticateUserFirebase.reauthenticateUser(emailUser, passwordUser)) {
			ifViewAttached(new ViewAction<DeleteAccountContract.View>() {
				@Override
				public void run(@NonNull DeleteAccountContract.View view) {
					view.showProgress(false);
					view.credentialsUserError();
				}
			});
		} else {
			goDeleteAccount();
		}
	}

	@Override
	public void goDeleteAccount() {

		if (!GetFirebaseUser.deleteFirebaseUser(firebaseUser)) {
			ifViewAttached(new ViewAction<DeleteAccountContract.View>() {
				@Override
				public void run(@NonNull DeleteAccountContract.View view) {
					view.showProgress(false);
					view.deleteUserError();
				}
			});
		} else {
			getVoteByCandidate();
		}
	}

	@Override
	public void getVoteByCandidate() {
		new GetVoteCurrentUser(firebaseUser.getUid(), new GetVoteCurrentUser.VoteCurrentUserListener() {
			@Override
			public void onResult(DataSnapshot vote) {
				if (vote != null) {
					deleteVoteIntoCandidate(Objects.requireNonNull(vote.getValue()).toString());
				} else {
					deleteUserInfo();
				}
			}
		});
	}

	@Override
	public void deleteVoteIntoCandidate(final String idCandidate) {
		new GetTotalVotes(idCandidate, new GetTotalVotes.VotesListener() {
			@Override
			public void onResult(int votes) {
				IntoFirebaseDatabase.deleteVoteCurrentUser(votes, idCandidate);
				deleteUserInfo();
			}
		});
	}

	@Override
	public void deleteUserInfo() {

		String idDevice = GetIdDevice.getIdDevice(context);
		IntoFirebaseDatabase.deleteUserInfo(firebaseUser.getUid(), idDevice);

		if (GetFirebaseUser.deleteFirebaseUser(firebaseUser)){
			ifViewAttached(new ViewAction<DeleteAccountContract.View>() {
				@Override
				public void run(@NonNull DeleteAccountContract.View view) {
					view.showProgress(false);
					view.deleteUserIsSuccessful();
				}
			});
		} else {
			ifViewAttached(new ViewAction<DeleteAccountContract.View>() {
				@Override
				public void run(@NonNull DeleteAccountContract.View view) {
					view.deleteUserError();
				}
			});
		}
	}
}
