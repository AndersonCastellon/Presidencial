package com.papaprogramador.presidenciales.Presentadores;


import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.InterfacesMVP.NewAccount;
import com.papaprogramador.presidenciales.Modelos.NewAccountModelo;

public class NewAccountPresentador extends MvpBasePresenter<NewAccount.Vista>
		implements NewAccount.Presentador {

	private NewAccount.Modelo modelo;
	private Context context;

	public NewAccountPresentador(Context context) {
		this.modelo = new NewAccountModelo(this);
		this.context = context;
	}

	@Override
	public void obtenerIdDispositivo(Context context) {

	}

	@Override
	public void almacenarIdDispositivo(String idDispositivo) {

	}

	@Override
	public void obtenerIdFirebase(String idDispositivo) {

	}

	@Override
	public void almacenarIdFirebase(DataSnapshot idFirebase) {

	}

	@Override
	public void validarCampos(String nombreUsuario, String emailUsuario, String emailUsuario2,
	                          String pass, String pass2, String departamento) {

	}

	@Override
	public void errorEnCampo(String error) {

	}

	@Override
	public void idYaUtilizado() {

	}

	@Override
	public void irAVerificarEmail(String emailUsuario, String pass) {

	}
}
