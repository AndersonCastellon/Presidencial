package com.papaprogramador.presidenciales.Presentador;

import com.papaprogramador.presidenciales.Modelo.ResetPasswordModelo;
import com.papaprogramador.presidenciales.InterfacesMVP.ResetPassword;

public class ResetPasswordPresentador implements ResetPassword.Presentador {

	private ResetPassword.Vista vista;
	private ResetPassword.Modelo modelo;

	public ResetPasswordPresentador(ResetPassword.Vista vista){
		this.vista = vista;
		modelo = new ResetPasswordModelo(this);
	}

	@Override
	public void enviarResultadoExitoso() {
		if (vista != null){
			vista.mostrarResultadoExitoso();
		}

	}

	@Override
	public void enviarEmailSinCuentaAsociada() {
		if (vista != null){
			vista.errorPorEmailSinCuentaAsociada();
		}
	}

	@Override
	public void enviarErrorPorCampoVacio() {
		if (vista != null){
			vista.errorPorCampoVacio();
		}
	}

	@Override
	public void enviarErrorPorEmailInvalido() {
		if (vista != null){
			vista.errorPorEmailInvalido();
		}
	}

	@Override
	public void procesarEmailUsuario(String emailUsuario) {
		if (vista != null){
			modelo.resetEmailUsuario(emailUsuario);
		}
	}
}
