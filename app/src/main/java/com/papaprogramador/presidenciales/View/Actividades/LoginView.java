package com.papaprogramador.presidenciales.View.Actividades;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.papaprogramador.presidenciales.InterfacesMVP.Login;
import com.papaprogramador.presidenciales.Presenters.LoginPresenter;
import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.Utils.Constans;


public class LoginView extends MvpActivity<Login.View, Login.Presenter>
		implements Login.View, android.view.View.OnClickListener {

	private TextInputEditText emailUsuario;
	private TextInputEditText pass;
	private ProgressBar progressBar;
	private Button mbtnLoginEmail,
			mbtnNewAccount, mbtnResetPass;
	private LinearLayout contenido;
	private SignInButton mBtnLoginGoogle;

	private Context context = LoginView.this;

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
	public void onClick(android.view.View v) {

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
			case R.id.btnLoginGoogle:
				getPresenter().obtenerGoogleApliClient(context,
						getString(R.string.default_web_client_id));
				break;
		}
	}

	@NonNull
	@Override
	public Login.Presenter createPresenter() {
		return new LoginPresenter(context);
	}

	@Override
	public void goNewAccountView(String idDispositivo) {
		Intent intent = new Intent(LoginView.this, NewAccountView.class);
		intent.putExtra(Constans.PUT_ID_DISPOSITIVO, idDispositivo);
		startActivity(intent);
	}

	@Override
	public void idYaUtilizado() {
		Snackbar.make(mbtnNewAccount, getResources().getString(R.string.DispositivoYautilizadoPorOtraCuenta),
				Snackbar.LENGTH_LONG).show();
	}

	@Override
	public void emailUserEmpty() {
		emailUsuario.setError(getResources().getString(R.string.emailUsuarioVacio));
	}

	@Override
	public void passEmpty() {
		pass.setError(getResources().getString(R.string.passVacio));
	}

	@Override
	public void noValidCredencials() {
		Snackbar.make(mbtnLoginEmail, getResources().getString(R.string.credencialesEmailIncorrectas),
				Snackbar.LENGTH_LONG).show();
	}

	@Override
	public void emailUserNoVerify() {
		Snackbar.make(mbtnLoginEmail, getResources().getString(R.string.emailNoVerificado),
				Snackbar.LENGTH_LONG).show();
	}

	@Override
	public void intentGoogle(GoogleApiClient googleApiClient) {
		Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
		startActivityForResult(intent, Constans.SIGN_IN_CODE);
	}

	@Override
	public void errorSigInGoogle() {
		Snackbar.make(mBtnLoginGoogle, getResources().getString(R.string.ErrorSignInGoogle),
				Snackbar.LENGTH_LONG).show();
	}

	@Override
	public void goListaCandidatosView() {
		Intent intent = new Intent(LoginView.this, MainView.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
				Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	@Override
	public void goResetPasswordView() {
		Intent intent = new Intent(LoginView.this, ResetPasswordView.class);
		startActivity(intent);
	}

	@Override
	public void showProgressBar(Boolean show) {
		if (show) {
			contenido.setVisibility(android.view.View.GONE);
			progressBar.setVisibility(android.view.View.VISIBLE);
		} else {
			contenido.setVisibility(android.view.View.VISIBLE);
			progressBar.setVisibility(android.view.View.GONE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case Constans.SIGN_IN_CODE:
				getPresenter().googleSingInFromResult(data);
				break;
		}
	}
}
