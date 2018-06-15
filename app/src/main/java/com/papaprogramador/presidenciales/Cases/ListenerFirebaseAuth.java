package com.papaprogramador.presidenciales.Cases;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ListenerFirebaseAuth implements FirebaseAuth.AuthStateListener {

	public interface CurrentUser {
		void user(FirebaseUser user);
	}

	private CurrentUser listener;

	public ListenerFirebaseAuth(CurrentUser listener) {
		this.listener = listener;
	}

	@Override
	public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
		FirebaseUser user = firebaseAuth.getCurrentUser();
		listener.user(user);
	}
}
