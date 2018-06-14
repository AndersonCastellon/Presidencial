package com.papaprogramador.presidenciales.Modelos;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.papaprogramador.presidenciales.Utils.Constantes;

public class IniciarSesionConCredenciales {

	public interface IniciarSesion {
		void resultadoInicio(String resultado, FirebaseUser user);
	}

	private IniciarSesion listener;
	private Context context;
	private String emailUsuario;
	private String pass;
	private GoogleSignInAccount signInAccount;

	public IniciarSesionConCredenciales(Context context, String emailUsuario, String pass,
	                                    IniciarSesion listener) {
		this.listener = listener;
		this.emailUsuario = emailUsuario;
		this.pass = pass;
		this.context = context;
		iniciarSesionConEmail();
	}

	public IniciarSesionConCredenciales(Context context, GoogleSignInAccount signInAccount,
	                                    IniciarSesion listener) {
		this.listener = listener;
		this.signInAccount = signInAccount;
		this.context = context;
		iniciarSesionConEmail();
	}

	private void iniciarSesionConEmail() {

		FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
		AuthCredential credential;

		if (signInAccount == null) {
			credential = EmailAuthProvider.getCredential(emailUsuario, pass);
		} else {
			credential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
		}


		firebaseAuth.signInWithCredential(credential).addOnCompleteListener((Activity) context,
				new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
						if (signInAccount == null) {
							if (user != null) {
								validarPorCredencialesEmail(user, task);
							}
						} else {
							if (user != null) {
								validarPorCredencialesGoogle(user, task);
							}
						}

					}

					private void validarPorCredencialesGoogle(FirebaseUser user, Task<AuthResult> task) {


						if (!task.isSuccessful()) {
							listener.resultadoInicio(Constantes.RESULT_NO_SUCCESSFUL, user);
						} else {
							listener.resultadoInicio(Constantes.RESULT_IS_SUCCESSFUL, user);
						}
					}

					private void validarPorCredencialesEmail(FirebaseUser user, Task<AuthResult> task) {


						if (!task.isSuccessful()) {
							listener.resultadoInicio(Constantes.RESULT_NO_SUCCESSFUL, user);
						} else if (!user.isEmailVerified()) {
							listener.resultadoInicio(Constantes.RESULT_EMAIL_NO_VERIFY, user);
						} else {
							listener.resultadoInicio(Constantes.RESULT_IS_SUCCESSFUL, user);
						}
					}
				});
	}
}
