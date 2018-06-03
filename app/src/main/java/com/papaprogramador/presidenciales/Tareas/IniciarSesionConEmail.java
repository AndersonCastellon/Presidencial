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
import com.papaprogramador.presidenciales.Utilidades.Constantes;

public class IniciarSesionConEmail {

	public interface IniciarSesion {
		void resultadoInicio(String resultado);
	}

	private IniciarSesion listener;

	public IniciarSesionConEmail(Context context, String emailUsuario, String pass,
	                             IniciarSesion listener) {
		this.listener = listener;
		iniciarSesionConEmail(context, emailUsuario, pass);
	}

	private void iniciarSesionConEmail(Context context, String emailUsuario, String pass) {

		AuthCredential credential = EmailAuthProvider.getCredential(emailUsuario, pass);
		FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

		firebaseAuth.signInWithCredential(credential).addOnCompleteListener((Activity) context,
				new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
						if (!task.isSuccessful()) {
							listener.resultadoInicio(Constantes.RESULT_NO_SUCCESSFUL);
						} else if (!user.isEmailVerified()) {
							listener.resultadoInicio(Constantes.RESULT_EMAIL_NO_VERIFY);
						} else {
							listener.resultadoInicio(Constantes.RESULT_IS_SUCCESSFUL);
						}
					}
				});
	}
}
