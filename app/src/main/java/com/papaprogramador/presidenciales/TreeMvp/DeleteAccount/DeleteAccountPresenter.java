package com.papaprogramador.presidenciales.TreeMvp.DeleteAccount;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.Utils.StaticMethods.GetFirebaseUser;

public class DeleteAccountPresenter extends MvpBasePresenter<DeleteAccountContract.View>
		implements DeleteAccountContract.Presenter {

	private FirebaseUser firebaseUser;
	private String emailCurrentUser;

	DeleteAccountPresenter() {
		firebaseUser = GetFirebaseUser.getFirebaseUser();
		emailCurrentUser = firebaseUser.getEmail();
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
}
