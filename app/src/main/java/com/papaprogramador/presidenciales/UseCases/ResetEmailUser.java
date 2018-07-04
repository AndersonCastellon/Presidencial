package com.papaprogramador.presidenciales.UseCases;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetEmailUser {

	public interface IsReset {
		void resultReset(boolean isReset);
	}

	private IsReset listener;

	public ResetEmailUser(String emailUsuario, IsReset listener) {
		this.listener = listener;
		resetEmailUser(emailUsuario);
	}

	private void resetEmailUser(String emailUsuario) {

		FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

		firebaseAuth.sendPasswordResetEmail(emailUsuario)
				.addOnCompleteListener(new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						if (task.isSuccessful()) {
							listener.resultReset(true);
						} else {
							listener.resultReset(false);
						}
					}
				});
	}
}
