package com.papaprogramador.presidenciales.Modelos;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.papaprogramador.presidenciales.InterfacesMVP.NewAccount;
import com.papaprogramador.presidenciales.Objetos.Usuario;
import com.papaprogramador.presidenciales.Utilidades.Constantes;
import com.papaprogramador.presidenciales.Utilidades.ReferenciasFirebase;

public class NewAccountModelo implements NewAccount.Modelo {

	private NewAccount.Presentador presentador;

	public NewAccountModelo(NewAccount.Presentador presentador) {
		this.presentador = presentador;
	}

	@Override
	public void validarCampos(Context context, String idDispositivo, String nombreUsuario,
	                          String emailUsuario, String emailUsuario2,
	                          String pass, String pass2, String departamento) {
		// Errores a controlar:
		// Campo vacio, valor no es igual, contraseña muy corta,

		if (nombreUsuario.isEmpty()) {
			presentador.campoVacio(Constantes.NOMBRE_USUARIO_VACIO);
		} else if (emailUsuario.isEmpty()) {
			presentador.campoVacio(Constantes.EMAIL_USUARIO_VACIO);
		} else if (emailUsuario2.isEmpty()) {
			presentador.campoVacio(Constantes.EMAIL_USUARIO2_VACIO);
		} else if (pass.isEmpty()) {
			presentador.campoVacio(Constantes.PASS_VACIO);
		} else if (pass2.isEmpty()) {
			presentador.campoVacio(Constantes.PASS2_VACIO);
		} else if (departamento.isEmpty()) {
			presentador.campoVacio(Constantes.DEPARTAMENTO_VACIO);
		} else if (!emailNoCoincide(emailUsuario, emailUsuario2)) {
			presentador.errorEnCampo(Constantes.EMAIL_NO_COINCIDE);
		} else if (!passwordCorto(pass)) {
			presentador.errorEnCampo(Constantes.PASS_INVALIDO);
		} else if (!passwordNoCoincide(pass, pass2)) {
			presentador.errorEnCampo(Constantes.PASS_NO_COINCIDE);
		} else {
			presentador.crearCuenta(context, idDispositivo, emailUsuario, nombreUsuario, pass, departamento);
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

		presentador.irAVerificarEmail(emailUsuario, pass);
	}

	private boolean emailNoCoincide(String emailUsuario, String emailUsuario2) {
		return emailUsuario.equals(emailUsuario2);
	}

	private boolean passwordCorto(String pass) {
		return pass.length() >= 8 && pass.length() <= 30;
	}

	private boolean passwordNoCoincide(String pass, String pass2) {
		return pass.equals(pass2);
	}
}
