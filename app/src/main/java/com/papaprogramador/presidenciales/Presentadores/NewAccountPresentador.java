package com.papaprogramador.presidenciales.Presentadores;


import android.content.Context;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.InterfacesMVP.NewAccount;
import com.papaprogramador.presidenciales.Modelos.NewAccountModelo;
import com.papaprogramador.presidenciales.Tareas.ObtenerIdDispositivo;
import com.papaprogramador.presidenciales.Tareas.ObtenerIdFirebase;

public class NewAccountPresentador extends MvpBasePresenter<NewAccount.Vista>
		implements NewAccount.Presentador {

	private NewAccount.Modelo modelo;

	public NewAccountPresentador() {
		this.modelo = new NewAccountModelo(this);
	}

	@Override
	public void obtenerIdDispositivo(Context context) {
		ObtenerIdDispositivo obtenerIdDispositivo = new ObtenerIdDispositivo(context,
				new ObtenerIdDispositivo.OyenteTareaIdDispositivo() {
					@Override
					public void idGenerado(String idDispositivo) {
						obtenerIdFirebase(idDispositivo);
					}
				});
	}

	@Override
	public void obtenerIdFirebase(String idDispositivo) {

		ObtenerIdFirebase obtenerIdFirebase = new ObtenerIdFirebase(idDispositivo,
				new ObtenerIdFirebase.IdObtenido() {
					@Override
					public void idObtenido(boolean bool, final String idFirebase) {
						if (bool) {
							ifViewAttached(new ViewAction<NewAccount.Vista>() {
								@Override
								public void run(@NonNull NewAccount.Vista view) {
									view.almacenarID(idFirebase);
								}
							});
						} else {
							ifViewAttached(new ViewAction<NewAccount.Vista>() {
								@Override
								public void run(@NonNull NewAccount.Vista view) {
									view.idYaUtilizado();
								}
							});
						}
					}
				});
	}

	@Override
	public void validarCampos(String idDispositivo, String nombreUsuario,
	                          String emailUsuario, String emailUsuario2,
	                          String pass, String pass2, String departamento) {

		modelo.validarCampos(idDispositivo, nombreUsuario, emailUsuario, emailUsuario2,
				pass, pass2, departamento);

	}

	@Override
	public void errorEnCampo(String error) {

	}

	@Override
	public void irAVerificarEmail(String emailUsuario, String pass) {

	}
}
