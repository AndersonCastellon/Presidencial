package com.papaprogramador.presidenciales.Modelos;

import com.google.firebase.database.DataSnapshot;
import com.papaprogramador.presidenciales.InterfacesMVP.NewAccount;

public class NewAccountModelo implements NewAccount.Modelo {

	private NewAccount.Presentador presentador;

	public NewAccountModelo(NewAccount.Presentador presentador) {
		this.presentador = presentador;
	}

	@Override
	public void almacenarIdDispositivo(String idDispositivo) {

	}

	@Override
	public void almacenarIdFirebase(DataSnapshot idFirebase) {

	}

	@Override
	public void validarCampos(String nombreUsuario, String emailUsuario, String emailUsuario2, String pass, String pass2, String departamento) {

	}

	@Override
	public void validarIdEnFirebase(DataSnapshot idFirebase, String idDispositivo, String emailUsuario, String nombreUsuario, String pass, String departamento) {

	}

	@Override
	public void crearCuenta(String emailUsuario, String nombreUsuario, String pass, String departamento) {

	}

	@Override
	public void registrarUsuarioEnFirebase(String nombreUsuario, String emailUsuario, String departamento, String idDispositivo, String voto) {

	}
}
