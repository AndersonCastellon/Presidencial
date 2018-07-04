package com.papaprogramador.presidenciales.UseCases;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CrearCuentaConEmail {

	public interface CuentaCreada {
		void uidObtenido(final boolean bool, String firebaseUID);
	}

	private CuentaCreada listener;

	public CrearCuentaConEmail(Context context, String emailUsuario, String pass, CuentaCreada listener) {
		this.listener = listener;
		crearCuentaConCredencialesEmailYPassword(context, emailUsuario, pass);
	}

	private void crearCuentaConCredencialesEmailYPassword(Context context, String emailUsuario, String pass) {

		final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

		firebaseAuth.createUserWithEmailAndPassword(emailUsuario, pass).addOnCompleteListener((Activity) context,
				new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {

							FirebaseUser user = firebaseAuth.getCurrentUser();
							final String uidFirebase = user.getUid();

							user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
								@Override
								public void onComplete(@NonNull Task<Void> task) {
									listener.uidObtenido(true, uidFirebase);
								}
							});
						} else {
							listener.uidObtenido(false, "");
						}
					}
				});
	}
}
