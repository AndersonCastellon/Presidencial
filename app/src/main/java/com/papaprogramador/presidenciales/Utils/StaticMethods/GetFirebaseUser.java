package com.papaprogramador.presidenciales.Utils.StaticMethods;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GetFirebaseUser {

	private static boolean ON_COMPLETE = true;

	public static FirebaseUser getFirebaseUser() {
		return FirebaseAuth.getInstance().getCurrentUser();
	}

	public static boolean deleteFirebaseUser(FirebaseUser user) {

		user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task) {
				if (!task.isSuccessful()) {
					ON_COMPLETE = false;
				}
			}
		});

		return ON_COMPLETE;
	}
}
