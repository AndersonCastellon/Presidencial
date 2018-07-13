package com.papaprogramador.presidenciales.Utils.StaticMethods;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdatePassword {

	public static void updatePasswordUser(String newPassword){

		FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

		user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task) {

			}
		});
	}
}
