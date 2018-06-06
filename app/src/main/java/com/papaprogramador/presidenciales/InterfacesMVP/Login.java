package com.papaprogramador.presidenciales.InterfacesMVP;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseUser;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface Login {
	interface Vista extends MvpView {
		void activityCrearNuevaCuenta(String idDispositivo);
		void idYaUtilizado();
		void credencialesIncorrectas();
		void emailNoVerificado();
		void intentGoogle(GoogleApiClient googleApiClient);
		void errorSigInGoogle();
		void activityListaCandidatos();
		void activityResetPassword();
		void mostrarProgreso(Boolean bool);

	}

	interface Presentador extends MvpPresenter<Login.Vista> {
		void obtenerIdDispositivo(Context context);
		void obtenerIdFirebase();
		void iniciarSesionConEmail(Context context, String emailUsuario, String pass);
		void obtenerGoogleApliClient(Context context, String string);
		void activityResetPassword();
		void googleSingInFromResult(Intent data);
		void resultGoogle(GoogleSignInResult result);
		void validarDispositivoConCuentaGoogle(GoogleSignInAccount signInAccount);
		void iniciarSesionConGoogle(Context context, GoogleSignInAccount signInAccount);
		void registrarUsuarioEnFirebase(FirebaseUser user);

	}
}
