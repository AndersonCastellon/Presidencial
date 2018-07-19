package com.papaprogramador.presidenciales.Utils.StaticMethods;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GetFirebaseUser {

	public static FirebaseUser getFirebaseUser(){
		return FirebaseAuth.getInstance().getCurrentUser();
	}
}
