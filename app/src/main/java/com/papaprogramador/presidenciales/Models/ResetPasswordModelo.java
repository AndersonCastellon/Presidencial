package com.papaprogramador.presidenciales.Models;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.papaprogramador.presidenciales.InterfacesMVP.ResetPassword;
import com.papaprogramador.presidenciales.Presentadores.ResetPasswordPresentador;

public class ResetPasswordModelo implements ResetPassword.Modelo {

	private ResetPasswordPresentador presentador;

	public ResetPasswordModelo(ResetPasswordPresentador presentador) {
		this.presentador = presentador;
	}

	@Override
	public void resetEmailUsuario(String emailUsuario) {
		if (emailVacio(emailUsuario)) {
			presentador.campoEmailVacio();
		} else if (emailInvalido(emailUsuario)) {
			presentador.emailInvalido();
		} else {
			FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

			firebaseAuth.sendPasswordResetEmail(emailUsuario)
					.addOnCompleteListener(new OnCompleteListener<Void>() {
						@Override
						public void onComplete(@NonNull Task<Void> task) {
							if (task.isSuccessful()){
								presentador.emailEnviado();
							}else {
								presentador.emailSinCuentaAsociada();
							}
						}
					});
		}
	}

	private boolean emailInvalido(String emailUsuario) {
		return !emailUsuario.contains("@");
	}

	private boolean emailVacio(String emailUsuario) {
		return emailUsuario.isEmpty();
	}
}
