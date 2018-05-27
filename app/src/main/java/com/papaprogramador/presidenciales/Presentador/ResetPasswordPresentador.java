package com.papaprogramador.presidenciales.Presentador;


import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.Modelo.ResetPasswordModelo;
import com.papaprogramador.presidenciales.InterfacesMVP.ResetPassword;

public class ResetPasswordPresentador extends MvpBasePresenter<ResetPassword.Vista>
		implements ResetPassword.Presentador {

	private ResetPassword.Modelo modelo;


	public ResetPasswordPresentador(){
		modelo = new ResetPasswordModelo(this);
	}

	@Override
	public void emailEnviado() {
		ifViewAttached(new ViewAction<ResetPassword.Vista>() {
			@Override
			public void run(@NonNull ResetPassword.Vista view) {
				view.mostrarResultadoExitoso();
			}
		});
	}

	@Override
	public void emailSinCuentaAsociada() {
		ifViewAttached(new ViewAction<ResetPassword.Vista>() {
			@Override
			public void run(@NonNull ResetPassword.Vista view) {
				view.errorPorEmailSinCuentaAsociada();
			}
		});
	}

	@Override
	public void campoEmailVacio() {
		ifViewAttached(new ViewAction<ResetPassword.Vista>() {
			@Override
			public void run(@NonNull ResetPassword.Vista view) {
				view.errorPorCampoVacio();
			}
		});
	}

	@Override
	public void emailInvalido() {
		ifViewAttached(new ViewAction<ResetPassword.Vista>() {
			@Override
			public void run(@NonNull ResetPassword.Vista view) {
				view.errorPorEmailInvalido();
			}
		});
	}

	@Override
	public void procesarEmailUsuario(String emailUsuario) {
		modelo.resetEmailUsuario(emailUsuario);
	}
}
