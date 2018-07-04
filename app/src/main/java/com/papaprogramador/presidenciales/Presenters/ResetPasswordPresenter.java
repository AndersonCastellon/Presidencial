package com.papaprogramador.presidenciales.Presenters;


import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.UseCases.ResetEmailUser;
import com.papaprogramador.presidenciales.UseCases.ValidarEmail;
import com.papaprogramador.presidenciales.InterfacesMVP.ResetPassword;

public class ResetPasswordPresenter extends MvpBasePresenter<ResetPassword.View>
		implements ResetPassword.Presenter {

	private boolean bool = true;


	public ResetPasswordPresenter() {
	}

	@Override
	public void emailUserProcess(final String emailUsuario) {
		ifViewAttached(new ViewAction<ResetPassword.View>() {
			@Override
			public void run(@NonNull final ResetPassword.View view) {
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
								view.goLoginView();
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
