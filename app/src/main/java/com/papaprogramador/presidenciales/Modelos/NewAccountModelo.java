package com.papaprogramador.presidenciales.Modelos;

import com.papaprogramador.presidenciales.InterfacesMVP.NewAccount;

public class NewAccountModelo implements NewAccount.Modelo {

	private NewAccount.Presentador presentador;

	public NewAccountModelo(NewAccount.Presentador presentador) {
		this.presentador = presentador;
	}

	@Override
	public void validarCampos(String idDispositivo, String nombreUsuario,
	                          String emailUsuario, String emailUsuario2,
	                          String pass, String pass2, String departamento) {

	}

	@Override
	public void crearCuenta(String emailUsuario, String nombreUsuario, String pass, String departamento) {

	}

	@Override
	public void registrarUsuarioEnFirebase(String nombreUsuario, String
			emailUsuario, String departamento, String idDispositivo, String voto) {

	}
}
