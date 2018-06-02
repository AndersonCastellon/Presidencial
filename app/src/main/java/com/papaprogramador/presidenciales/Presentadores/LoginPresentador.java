package com.papaprogramador.presidenciales.Presentadores;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.InterfacesMVP.Login;
import com.papaprogramador.presidenciales.Modelos.LoginModelo;
import com.papaprogramador.presidenciales.Tareas.ObtenerIdDispositivo;
import com.papaprogramador.presidenciales.Tareas.ObtenerIdFirebase;

public class LoginPresentador extends MvpBasePresenter<Login.Vista> implements Login.Presentador {

	private Login.Modelo modelo;

	public LoginPresentador() {
		this.modelo = new LoginModelo(this);
	}

	@Override
	public void obtenerIdDispositivo(Context context) {
		new ObtenerIdDispositivo(context,
				new ObtenerIdDispositivo.OyenteTareaIdDispositivo() {
					@Override
					public void idGenerado(String idDispositivo) {
						obtenerIdFirebase(idDispositivo);
					}
				});
	}

	@Override
	public void obtenerIdFirebase(final String idDispositivo) {
		new ObtenerIdFirebase(idDispositivo,
				new ObtenerIdFirebase.IdObtenido() {
					@Override
					public void idObtenido(final boolean bool, final String idFirebase) {
						if (!bool) {
							ifViewAttached(new ViewAction<Login.Vista>() {
								@Override
								public void run(@NonNull Login.Vista view) {
									view.idYaUtilizado();
								}
							});
						} else {
							ifViewAttached(new ViewAction<Login.Vista>() {
								@Override
								public void run(@NonNull Login.Vista view) {
									view.crearNuevaCuenta(idDispositivo);
								}
							});
						}
					}
				});
	}
}
