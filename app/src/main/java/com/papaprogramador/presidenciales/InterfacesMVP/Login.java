package com.papaprogramador.presidenciales.InterfacesMVP;

import android.content.Context;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface Login {
	interface Vista extends MvpView {
		void crearNuevaCuenta(String idDispositivo);
		void idYaUtilizado();

	}

	interface Presentador extends MvpPresenter<Login.Vista> {
		void obtenerIdDispositivo(Context context);
		void obtenerIdFirebase(String idDispositivo);

	}

	interface Modelo {

	}
}
