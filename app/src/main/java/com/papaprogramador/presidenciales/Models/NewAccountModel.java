package com.papaprogramador.presidenciales.Models;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.papaprogramador.presidenciales.InterfacesMVP.NewAccount;
import com.papaprogramador.presidenciales.Objetos.Usuario;
import com.papaprogramador.presidenciales.Modelos.ValidarEmail;
import com.papaprogramador.presidenciales.Utils.Constantes;
import com.papaprogramador.presidenciales.Utils.ReferenciasFirebase;

public class NewAccountModel implements NewAccount.Model {

	private NewAccount.Presenter presenter;
	private boolean crearCuenta = true;

	public NewAccountModel(NewAccount.Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void validarCampos(final Context context, final String idDispositivo, final String nombreUsuario,
	                          final String emailUsuario, String emailUsuario2,
	                          final String pass, String pass2, final String departamento) {

		if (nombreUsuario.isEmpty()) {
			presenter.campoVacio(Constantes.NOMBRE_USUARIO_VACIO);
			crearCuenta = false;
		}

		if (emailUsuario.isEmpty()) {
			presenter.campoVacio(Constantes.EMAIL_USUARIO_VACIO);
			crearCuenta = false;
		} else {
			new ValidarEmail(emailUsuario, new ValidarEmail.EmailValidado() {
				@Override
				public void emailEsValido(Boolean esValido) {
					if (!esValido) {
						presenter.errorEnCampo(Constantes.EMAIL_INVALIDO);
						crearCuenta = false;
					}
				}
			});
		}

		if (emailUsuario2.isEmpty()) {
			presenter.campoVacio(Constantes.EMAIL_USUARIO2_VACIO);
			crearCuenta = false;
		} else {
			new ValidarEmail(emailUsuario2, new ValidarEmail.EmailValidado() {
				@Override
				public void emailEsValido(Boolean esValido) {
					if (!esValido) {
						presenter.errorEnCampo(Constantes.EMAIL_INVALIDO);
						crearCuenta = false;
					}
				}
			});
		}

		if (emailNoCoincide(emailUsuario, emailUsuario2)) {
			presenter.errorEnCampo(Constantes.EMAIL_NO_COINCIDE);
			crearCuenta = false;
		}

		if (pass.isEmpty()) {
			presenter.campoVacio(Constantes.PASS_VACIO);
			crearCuenta = false;
		}

		if (pass2.isEmpty()) {
			presenter.campoVacio(Constantes.PASS2_VACIO);
			crearCuenta = false;
		}

		if (!passwordCorto(pass)) {
			presenter.errorEnCampo(Constantes.PASS_INVALIDO);
			crearCuenta = false;
		}

		if (!passwordNoCoincide(pass, pass2)) {
			presenter.errorEnCampo(Constantes.PASS_NO_COINCIDE);
			crearCuenta = false;
		}

		if (departamento == null) {
			presenter.campoVacio(Constantes.DEPARTAMENTO_VACIO);
			crearCuenta = false;
		} else if (departamento.isEmpty()) {
			presenter.campoVacio(Constantes.DEPARTAMENTO_VACIO);
			crearCuenta = false;
		}

		if (crearCuenta) {
			presenter.crearCuenta(context, idDispositivo, emailUsuario, nombreUsuario, pass, departamento);
		}
	}

	@Override
	public void registrarUsuarioEnFirebaseRealTimeDataBase(String uidFirebase, String nombreUsuario,
	                                                       String emailUsuario, String departamento,
	                                                       String idDispositivo, String voto, String pass) {

		Usuario usuario = new Usuario(nombreUsuario, emailUsuario, departamento, idDispositivo, voto);

		DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

		databaseReference.child(ReferenciasFirebase.NODO_USUARIO)
				.child(uidFirebase).setValue(usuario);

		databaseReference.child(ReferenciasFirebase.NODO_ID_DISPOSITIVO)
				.child(idDispositivo).setValue(emailUsuario);

		presenter.irAVerificarEmail(emailUsuario, pass);
	}

	private boolean emailNoCoincide(String emailUsuario, String emailUsuario2) {
		return !emailUsuario.equals(emailUsuario2);
	}

	private boolean passwordCorto(String pass) {
		return pass.length() >= 8 && pass.length() <= 30;
	}

	private boolean passwordNoCoincide(String pass, String pass2) {
		return pass.equals(pass2);
	}
}
