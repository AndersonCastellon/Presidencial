package com.papaprogramador.presidenciales.Presenters;


import android.content.Context;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.InterfacesMVP.NewAccount;
import com.papaprogramador.presidenciales.Models.NewAccountModel;
import com.papaprogramador.presidenciales.Modelos.CrearCuentaConEmail;
import com.papaprogramador.presidenciales.Utils.Constantes;

public class NewAccountPresenter extends MvpBasePresenter<NewAccount.View>
		implements NewAccount.Presenter {

	private NewAccount.Model model;

	public NewAccountPresenter() {
		this.model = new NewAccountModel(this);
	}

	@Override
	public void validarCampos(Context context, String idDispositivo, String nombreUsuario,
	                          String emailUsuario, String emailUsuario2,
	                          String pass, String pass2, String departamento) {

		model.validarCampos(context, idDispositivo, nombreUsuario, emailUsuario, emailUsuario2,
				pass, pass2, departamento);

	}

	@Override
	public void campoVacio(final String campoVacio) {
		ifViewAttached(new ViewAction<NewAccount.View>() {
			@Override
			public void run(@NonNull NewAccount.View view) {
				switch (campoVacio) {
					case Constantes.NOMBRE_USUARIO_VACIO:
						view.nombreUsuarioVacio();
						break;
					case Constantes.EMAIL_USUARIO_VACIO:
						view.emailUsuarioVacio();
						break;
					case Constantes.EMAIL_USUARIO2_VACIO:
						view.emailUsuario2Vacio();
						break;
					case Constantes.PASS_VACIO:
						view.passwordVacio();
						break;
					case Constantes.PASS2_VACIO:
						view.password2Vacio();
						break;
					case Constantes.DEPARTAMENTO_VACIO:
						view.departamentoVacio();
						break;
				}
			}
		});
	}

	@Override
	public void errorEnCampo(final String error) {
		ifViewAttached(new ViewAction<NewAccount.View>() {
			@Override
			public void run(@NonNull NewAccount.View view) {
				switch (error) {
					case Constantes.EMAIL_INVALIDO:
						view.errorEmailInvalido();
						break;
					case Constantes.EMAIL_NO_COINCIDE:
						view.errorEmailNoCoincide();
						break;
					case Constantes.PASS_INVALIDO:
						view.errorPassInvalido();
						break;
					case Constantes.PASS_NO_COINCIDE:
						view.errorPassNoCoincide();
				}
			}
		});
	}

	@Override
	public void crearCuenta(final Context context, final String idDispositivo, final String emailUsuario,
	                        final String nombreUsuario, final String pass, final String departamento) {

		ifViewAttached(new ViewAction<NewAccount.View>() {
			@Override
			public void run(@NonNull NewAccount.View view) {
				view.mostrarProgreso(true);
			}
		});
		new CrearCuentaConEmail(context, emailUsuario, pass, new CrearCuentaConEmail.CuentaCreada() {
			@Override
			public void uidObtenido(final boolean bool, String firebaseUID) {
				if (!bool) {
					ifViewAttached(new ViewAction<NewAccount.View>() {
						@Override
						public void run(@NonNull NewAccount.View view) {
							view.mostrarProgreso(false);
							view.cuentaYaExiste();
						}
					});
				}

				model.registrarUsuarioEnFirebaseRealTimeDataBase(firebaseUID, nombreUsuario, emailUsuario,
						departamento, idDispositivo, Constantes.VALOR_VOTO_DEFAULT, pass);
			}
		});
	}

	@Override
	public void irAVerificarEmail(final String emailUsuario, final String pass) {
		ifViewAttached(new ViewAction<NewAccount.View>() {
			@Override
			public void run(@NonNull NewAccount.View view) {
				view.mostrarProgreso(false);
				view.irAVerificarEmail(emailUsuario, pass);
			}
		});
	}
}
