package com.papaprogramador.presidenciales.Utils.StaticMethods;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ReauthenticateUserFirebase {

	private static boolean reauthenticateIsTrue = true;

	public static boolean reauthenticateUser(String emailUser, String currentPassword) {

		FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

		AuthCredential credential = EmailAuthProvider
				.getCredential(emailUser, currentPassword);

		user.reauthenticate(credential)
				.addOnCompleteListener(new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						if (!task.isSuccessful()){
							reauthenticateIsTrue = false;
						}
					}
				});
		return reauthenticateIsTrue;
	}
}
