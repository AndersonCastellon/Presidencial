package com.papaprogramador.presidenciales.Presentador;

import android.content.Context;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.Modelo.ResetPasswordModelo;
import com.papaprogramador.presidenciales.InterfacesMVP.ResetPassword;

public class ResetPasswordPresentador extends MvpBasePresenter<ResetPassword.Vista>
		implements ResetPassword.Presentador {

	private ResetPassword.Modelo modelo;

	public ResetPasswordPresentador(Context context){
		modelo = new ResetPasswordModelo(this);
	}

	@Override
	public void enviarResultadoExitoso() {

	}

	@Override
	public void enviarEmailSinCuentaAsociada() {

	}

	@Override
	public void enviarErrorPorCampoVacio() {

	}

	@Override
	public void enviarErrorPorEmailInvalido() {

	}

	@Override
	public void procesarEmailUsuario(String emailUsuario) {

	}
}
