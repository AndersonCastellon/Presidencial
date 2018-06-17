package com.papaprogramador.presidenciales.InterfacesMVP;

import android.content.Context;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface NewAccount {
	interface View extends MvpView {
		void accountAlreadyExists();
		void showProgressBar(Boolean show);
		void nameUserIsEmpty();
		void emailUserIsEmpty();
		void emailUser2IsEmpty();
		void passUserIsEmpty();
		void passUser2IsEmpty();
		void departmentIsEmpty();
		void emailUserIsDifferent();
		void emailUserIsInvalid();
		void passUserIsInvalid();
		void passUserIsDifferent();
		void goEmailVerifyView(String emailUser, String passUser);
	}

	interface Presenter extends MvpPresenter<View> {
		void validarCampos(Context context, String idDispositivo, String nombreUsuario, String emailUsuario, String emailUsuario2, String pass,
		                   String pass2, String departamento);
		void campoVacio(String campoVacio);
		void errorEnCampo(String error);
		void crearCuenta(Context context, String idDispositivo, String emailUsuario, String nombreUsuario,
		                 String pass, String departamento);
		void irAVerificarEmail(String emailUsuario, String pass);

	}

	interface Model {
		void validarCampos(Context context, String idDispositivo, String nombreUsuario,
		                   String emailUsuario, String emailUsuario2, String pass,
		                   String pass2, String departamento);
		void registrarUsuarioEnFirebaseRealTimeDataBase(String uidFirebase, String nombreUsuario,
		                                                String emailUsuario, String departamento,
		                                                String idDispositivo, String voto,
		                                                String pass);
	}
}
