package com.papaprogramador.presidenciales.Presenters;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.InterfacesMVP.DetailCandidateContract;

public class DetailCandidatePresenter extends MvpBasePresenter<DetailCandidateContract.View>
		implements DetailCandidateContract.Presenter {

	private FirebaseAuth firebaseAuth;

	private FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
		@Override
		public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
			FirebaseUser user = firebaseAuth.getCurrentUser();
			if (user != null){
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

	public DetailCandidatePresenter(String idCandidate, String nameCandidate,
	                                String urlImgCandidate, String urlHtmlCandidate) {
		this.idCandidate = idCandidate;
		this.nameCandidate = nameCandidate;
		this.urlImgCandidate = urlImgCandidate;
		this.urlHtmlCandidate = urlHtmlCandidate;
		firebaseAuth = FirebaseAuth.getInstance();
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
