package com.papaprogramador.presidenciales.common.dataAccess;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseUserAPI {

	private FirebaseUser mUser;
	private static FirebaseUserAPI INSTANCE = null;

	private FirebaseUserAPI() {
		mUser = FirebaseAuth.getInstance().getCurrentUser();
	}

	public static FirebaseUserAPI getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new FirebaseUserAPI();
		}
		return INSTANCE;
	}

	public boolean getSession(){
		return mUser != null;
	}

	public String getUserId() {
		return mUser.getUid();
	}
}
