package com.papaprogramador.presidenciales.Vistas.Actividades;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.papaprogramador.presidenciales.InterfacesMVP.Login;
import com.papaprogramador.presidenciales.Presentadores.LoginPresentador;
import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.Utilidades.Constantes;


public class LoginVista extends MvpActivity<Login.Vista, Login.Presentador>
		implements Login.Vista, View.OnClickListener {

	private TextInputEditText emailUsuario;
	private TextInputEditText pass;
	private ProgressBar progressBar;
	private Button mbtnLoginEmail,
			mbtnNewAccount, mbtnResetPass;
	private LinearLayout contenido;
	private SignInButton mBtnLoginGoogle;

	private Context context = LoginVista.this;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		onStartVista();
		getPresenter().obtenerIdDispositivo(context);
	}

	private void onStartVista() {
		emailUsuario = findViewById(R.id.editTexEmail);
		pass = findViewById(R.id.editTextPassword);
		mbtnLoginEmail = findViewById(R.id.btnLogin);
		mbtnNewAccount = findViewById(R.id.btnNewAccount);
		mBtnLoginGoogle = findViewById(R.id.btnLoginGoogle);
		progressBar = findViewById(R.id.mProgressBar);
		contenido = findViewById(R.id.Logincontenido);
		mbtnResetPass = findViewById(R.id.recuperarPassword);

		mbtnLoginEmail.setOnClickListener(this);
		mbtnNewAccount.setOnClickListener(this);
		mbtnResetPass.setOnClickListener(this);
		mBtnLoginGoogle.setOnClickListener(this);

		mBtnLoginGoogle.setSize(SignInButton.SIZE_WIDE); //Tamaño del boton de Google
		mBtnLoginGoogle.setColorScheme(SignInButton.COLOR_DARK); //Estilo de color del boton de Google
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()){
			case R.id.btnLogin:
				String emailU = emailUsuario.getText().toString();
				String password = pass.getText().toString();
				getPresenter().iniciarSesionConEmail(context, emailU, password);
				break;
			case R.id.btnNewAccount:
				getPresenter().obtenerIdFirebase();
				break;
			case R.id.recuperarPassword:
				getPresenter().activityResetPassword();
				break;
			case R.id.btnLoginGoogle: //TODO: Encontrar error: Iniciar sesion con Google despues
				//de haber cancelado un inicio de sesion con Google
				getPresenter().obtenerGoogleApliClient(context,
						getString(R.string.default_web_client_id));
				break;
		}
	}

	@NonNull
	@Override
	public Login.Presentador createPresenter() {
		return new LoginPresentador(context);
	}

	@Override
	public void activityCrearNuevaCuenta(String idDispositivo) {
		Intent intent = new Intent(LoginVista.this, NewAccountVista.class);
		intent.putExtra(Constantes.PUT_ID_DISPOSITIVO, idDispositivo);
		startActivity(intent);
	}

	@Override
	public void idYaUtilizado() {
		Snackbar.make(mbtnNewAccount, getResources().getString(R.string.DispositivoYautilizadoPorOtraCuenta),
				Snackbar.LENGTH_LONG).show();
	}

	@Override
	public void credencialesIncorrectas() {
		Snackbar.make(mbtnLoginEmail, getResources().getString(R.string.credencialesEmailIncorrectas),
				Snackbar.LENGTH_LONG).show();
	}

	@Override
	public void emailNoVerificado() {
		Snackbar.make(mbtnLoginEmail, getResources().getString(R.string.emailNoVerificado),
				Snackbar.LENGTH_LONG).show();
	}

	@Override
	public void intentGoogle(GoogleApiClient googleApiClient) {
		Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
		startActivityForResult(intent, Constantes.SIGN_IN_CODE);
	}

	@Override
	public void errorSigInGoogle() {
		Snackbar.make(mBtnLoginGoogle, getResources().getString(R.string.ErrorSignInGoogle),
				Snackbar.LENGTH_LONG).show();
	}

	@Override
	public void activityListaCandidatos() {
		Intent intent = new Intent(LoginVista.this, ListaCandidatosVista.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
				Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	@Override
	public void activityResetPassword() {
		Intent intent = new Intent(LoginVista.this, ResetPasswordVista.class);
		startActivity(intent);
	}

	@Override
	public void mostrarProgreso(Boolean bool) {
		if (bool) {
			contenido.setVisibility(View.GONE);
			progressBar.setVisibility(View.VISIBLE);
		} else {
			contenido.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case Constantes.SIGN_IN_CODE:
				getPresenter().googleSingInFromResult(data);
				break;
		}
	}
}
