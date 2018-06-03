package com.papaprogramador.presidenciales.InterfacesMVP;

import android.content.Context;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface Login {
	interface Vista extends MvpView {
		void crearNuevaCuenta(String idDispositivo);
		void idYaUtilizado();
		void credencialesIncorrectas();
		void emailNoVerificado();
		void irAVistaCandidatos();

	}

	interface Presentador extends MvpPresenter<Login.Vista> {
		void obtenerIdDispositivo(Context context);
		void obtenerIdFirebase(String idDispositivo);
		void iniciarSesionConEmail(Context context, String emailUsuario, String pass);
		void iniciarSesionConGoogle(Context context);

	}

	interface Modelo {

	}
}
