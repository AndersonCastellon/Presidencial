package com.papaprogramador.presidenciales.Tareas;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CrearCuentaConEmail {

	public interface CuentaCreada {
		void uidObtenido(boolean bool, String firebaseUID);
	}

	private CuentaCreada listener;

	public CrearCuentaConEmail(Context context, String emailUsuario, String pass, CuentaCreada listener) {
		this.listener = listener;
		crearCuentaConCredencialesEmailYPassword(context, emailUsuario, pass);
	}

	private void crearCuentaConCredencialesEmailYPassword(Context context, String emailUsuario, String pass) {

		AuthCredential authCredential = EmailAuthProvider.getCredential(emailUsuario, pass);
		FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

		firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener((Activity) context,
				new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {

							FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
							String uidFirebase = user.getUid();

							listener.uidObtenido(true, uidFirebase);
						} else {
							listener.uidObtenido(false, "");
						}
					}
				});
	}
}
