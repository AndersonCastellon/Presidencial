package com.papaprogramador.presidenciales.Presentadores;


import android.content.Context;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.InterfacesMVP.NewAccount;
import com.papaprogramador.presidenciales.Modelos.NewAccountModelo;
import com.papaprogramador.presidenciales.Tareas.CrearCuentaConEmail;
import com.papaprogramador.presidenciales.Tareas.ObtenerIdDispositivo;
import com.papaprogramador.presidenciales.Tareas.ObtenerIdFirebase;
import com.papaprogramador.presidenciales.Utilidades.Constantes;

public class NewAccountPresentador extends MvpBasePresenter<NewAccount.Vista>
		implements NewAccount.Presentador {

	private NewAccount.Modelo modelo;
	private Context context;

	public NewAccountPresentador(Context context) {
		this.context = context;
		this.modelo = new NewAccountModelo(this);
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
	public void obtenerIdFirebase(String idDispositivo) {
		new ObtenerIdFirebase(idDispositivo,
				new ObtenerIdFirebase.IdObtenido() {
					@Override
					public void idObtenido(boolean bool, final String idFirebase) {
						if (!bool) {
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
	public void validarCampos(Context context, String idDispositivo, String nombreUsuario,
	                          String emailUsuario, String emailUsuario2,
	                          String pass, String pass2, String departamento) {

		modelo.validarCampos(context, idDispositivo, nombreUsuario, emailUsuario, emailUsuario2,
				pass, pass2, departamento);

	}

	@Override
	public void campoVacio(String campoVacio) {

	}

	@Override
	public void errorEnCampo(String error) {

	}

	@Override
	public void crearCuenta(Context context, final String idDispositivo, final String emailUsuario,
	                        final String nombreUsuario, final String pass, final String departamento) {

		new CrearCuentaConEmail(context, emailUsuario, pass, new CrearCuentaConEmail.CuentaCreada() {
			@Override
			public void uidObtenido(boolean bool, String firebaseUID) {
				if (!bool) {
					ifViewAttached(new ViewAction<NewAccount.Vista>() {
						@Override
						public void run(@NonNull NewAccount.Vista view) {
							view.cuentaYaExiste();
						}
					});
				}

				modelo.registrarUsuarioEnFirebaseRealTimeDataBase(firebaseUID, nombreUsuario, emailUsuario,
						departamento, idDispositivo, Constantes.VALOR_VOTO_DEFAULT, pass);
			}
		});
	}

	@Override
	public void irAVerificarEmail(final String emailUsuario, final String pass) {
		ifViewAttached(new ViewAction<NewAccount.Vista>() {
			@Override
			public void run(@NonNull NewAccount.Vista view) {
				view.irAVerificarEmail(emailUsuario, pass);
			}
		});
	}
}
