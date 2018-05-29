package com.papaprogramador.presidenciales.InterfacesMVP;

import android.content.Context;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

// Logica
// 1-Obtener en hilo secundario el ID del dispositivo y almacenarlo en una variable en el presenter
// 2-Obtener en hilo secundario el ID desde Firebase y almacenarlo en una variable en el presenter
// 3-Verificar los campos en el modelo y notificar al presenter quien dirá a la vista si no hay error
// 4-Si no hay errores, enviar los ID´s y el email a validar al modelo
// 5-Si es true, ir al método para crear la cuenta con los valores que necesite
// 6-Se registra el usuario en Firebase con los datos obtenidos y se lanza la activity para verificar el email

public interface NewAccount {
	interface Vista extends MvpView {
		void almacenarID(String idDispositivo);
		void idYaUtilizado();
		void cuentaYaExiste();
		void mostrarProgreso();
		void nombreUsuarioVacio();
		void emailUsuarioVacio();
		void emailUsuario2Vacio();
		void passwordVacio();
		void password2Vacio();
		void departamentoVacio();
		void errorEmailNoCoincide();
		void errorPassInvalido();
		void errorPassNoCoincide();
		void irAVerificarEmail(String emailUsuario, String pass);
	}

	interface Presentador extends MvpPresenter<NewAccount.Vista> {
		void obtenerIdDispositivo(Context context);
		void obtenerIdFirebase(String idDispositivo);
		void validarCampos(Context context, String idDispositivo, String nombreUsuario, String emailUsuario, String emailUsuario2, String pass,
		                   String pass2, String departamento);
		void campoVacio(String campoVacio);
		void errorEnCampo(String error);
		void crearCuenta(Context context, String idDispositivo, String emailUsuario, String nombreUsuario,
		                 String pass, String departamento);
		void irAVerificarEmail(String emailUsuario, String pass);

	}

	interface Modelo {
		void validarCampos(Context context, String idDispositivo, String nombreUsuario,
		                   String emailUsuario, String emailUsuario2, String pass,
		                   String pass2, String departamento);
		void registrarUsuarioEnFirebaseRealTimeDataBase(String uidFirebase, String nombreUsuario,
		                                                String emailUsuario, String departamento,
		                                                String idDispositivo, String voto,
		                                                String pass);
	}
}
