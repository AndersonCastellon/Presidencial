package com.papaprogramador.presidenciales.Presentadores;


import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.Modelos.ResetEmailUser;
import com.papaprogramador.presidenciales.Modelos.ValidarEmail;
import com.papaprogramador.presidenciales.InterfacesMVP.ResetPassword;

public class ResetPasswordPresentador extends MvpBasePresenter<ResetPassword.Vista>
		implements ResetPassword.Presentador {

	private boolean bool = true;


	public ResetPasswordPresentador() {
	}

	@Override
	public void emailUserProcess(final String emailUsuario) {
		ifViewAttached(new ViewAction<ResetPassword.Vista>() {
			@Override
			public void run(@NonNull final ResetPassword.Vista view) {
				if (emailUsuario.isEmpty()) {
					bool = false;
					view.emailIsEmpty();
				} else {
					new ValidarEmail(emailUsuario, new ValidarEmail.EmailValidado() {
						@Override
						public void emailEsValido(Boolean esValido) {
							if (!esValido) {
								bool = false;
								view.emailIsInvalid();
							}
						}
					});
				}

				if (bool) {
					new ResetEmailUser(emailUsuario, new ResetEmailUser.IsReset() {
						@Override
						public void resultReset(boolean isReset) {
							if (isReset) {
								view.resetIsSuccesful();
							} else {
								view.emailNoExist();
							}
						}
					});
				}
			}
		});

	}
}
