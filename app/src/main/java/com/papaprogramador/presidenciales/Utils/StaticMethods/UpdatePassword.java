package com.papaprogramador.presidenciales.Utils.StaticMethods;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdatePassword {

	private static boolean updateIsSuccessful = true;

	public static boolean validatePasswordLength(String password) {
		return password.length() >= 8 && password.length() <= 30;
	}

	public static boolean updatePasswordUser(String newPassword) {

		FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

		user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task) {
				if (!task.isSuccessful()) {
					updateIsSuccessful = false;
				}
			}
		});

		return updateIsSuccessful;
	}
}
