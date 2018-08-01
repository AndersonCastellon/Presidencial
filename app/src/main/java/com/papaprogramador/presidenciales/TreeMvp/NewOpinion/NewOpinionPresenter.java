package com.papaprogramador.presidenciales.TreeMvp.NewOpinion;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.Obj.User;
import com.papaprogramador.presidenciales.UseCases.GetUserProfile;

public class NewOpinionPresenter extends MvpBasePresenter<NewOpinionContract.View>
		implements NewOpinionContract.Presenter {

	private FirebaseAuth firebaseAuth;
	private String userId;

	private FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
		@Override
		public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
			FirebaseUser user = firebaseAuth.getCurrentUser();
			if (user != null) {
				new GetUserProfile(new GetUserProfile.UserProfileListener() {
					@Override
					public void onResult(User user) {

						userId = user.getUserId();

						final String userName = user.getUsername();
						final String urlPhotoProfile = user.getUriPhotoProfile();
						final String urlPoliticalFlag = user.getPoliticalFlag();

						ifViewAttached(new ViewAction<NewOpinionContract.View>() {
							@Override
							public void run(@NonNull NewOpinionContract.View view) {
								view.setUserProfile(userName, urlPhotoProfile, urlPoliticalFlag);
							}
						});
					}
				});
			}
		}
	};

	NewOpinionPresenter() {
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
