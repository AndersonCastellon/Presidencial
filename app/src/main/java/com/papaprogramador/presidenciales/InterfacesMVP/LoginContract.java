package com.papaprogramador.presidenciales.InterfacesMVP;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseUser;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface LoginContract {
	interface View extends MvpView {
		void goNewAccountView(String idDispositivo);
		void idYaUtilizado();
		void emailUserEmpty();
		void passEmpty();
		void noValidCredencials();
		void emailUserNoVerify();
		void emailNoValid();
		void intentGoogle(GoogleApiClient googleApiClient);
		void errorSigInGoogle();
		void goListaCandidatosView();
		void goResetPasswordView();
		void showProgressBar(Boolean show);

	}

	interface Presenter extends MvpPresenter<View> {
		void getIdFirebase();
		void logInWithEmailAndPassword(Context context, String emailUsuario, String pass);
		void obtenerGoogleApliClient(Context context, String string);
		void activityResetPassword();
		void googleSingInFromResult(Intent data);
		void resultGoogle(GoogleSignInResult result);
		void validarDispositivoConCuentaGoogle(GoogleSignInAccount signInAccount);
		void iniciarSesionConGoogle(Context context, GoogleSignInAccount signInAccount);
		void registrarUsuarioEnFirebase(FirebaseUser user);

	}
}
