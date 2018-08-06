package com.papaprogramador.presidenciales.View.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.papaprogramador.presidenciales.InterfacesMVP.LoginContract;
import com.papaprogramador.presidenciales.Presenters.LoginPresenter;
import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.Utils.Constans;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class LoginView extends MvpActivity<LoginContract.View, LoginContract.Presenter>
		implements LoginContract.View {


	@BindView(R.id.emailUserLoginView)
	TextInputEditText emailUserLoginView;
	@BindView(R.id.passUserLoginView)
	TextInputEditText passUserLoginView;
	@BindView(R.id.contentLoginView)
	LinearLayout contentLoginView;
	@BindView(R.id.mProgressBar)
	ProgressBar mProgressBar;

	Unbinder unbinder;

	private Context context = LoginView.this;
	private static final int RP_PHONE_STATE = 222;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		SignInButton btnLoginGoogle = findViewById(R.id.btnLoginGoogle);

		btnLoginGoogle.setSize(SignInButton.SIZE_WIDE);
		btnLoginGoogle.setColorScheme(SignInButton.COLOR_DARK);

		getPresenter().getIdDevice(Manifest.permission.READ_PHONE_STATE, RP_PHONE_STATE);

		unbinder = ButterKnife.bind(this);
	}

	@OnClick({R.id.mBtnLoginEmail, R.id.btnLoginGoogle, R.id.mBtnResetPass, R.id.mBtnNewAccount})
	public void loginButtonsClicked(View view) {
		switch (view.getId()) {
			case R.id.mBtnLoginEmail:
				String emailU = emailUserLoginView.getText().toString();
				String password = passUserLoginView.getText().toString();
				getPresenter().logInWithEmailAndPassword(context, emailU, password);
				break;
			case R.id.btnLoginGoogle:
				getPresenter().obtenerGoogleApliClient(context,
						getString(R.string.default_web_client_id));
				break;
			case R.id.mBtnResetPass:
				getPresenter().activityResetPassword();
				break;
			case R.id.mBtnNewAccount:
				getPresenter().getIdFirebase();
				break;
		}
	}

	@NonNull
	@Override
	public LoginContract.Presenter createPresenter() {
		return new LoginPresenter(context);
	}

	@Override
	public void goNewAccountView(String idDispositivo) {
		Intent intent = new Intent(LoginView.this, NewAccountView.class);
		intent.putExtra(Constans.PUT_ID_DEVICE, idDispositivo);
		startActivity(intent);
	}

	@Override
	public void idYaUtilizado() {
		Snackbar.make(emailUserLoginView, getResources().getString(R.string.DispositivoYautilizadoPorOtraCuenta),
				Snackbar.LENGTH_LONG).show();
	}

	@Override
	public void emailUserEmpty() {
		emailUserLoginView.setError(getResources().getString(R.string.emailUsuarioVacio));
	}

	@Override
	public void passEmpty() {
		passUserLoginView.setError(getResources().getString(R.string.passVacio));
	}

	@Override
	public void noValidCredencials() {
		Snackbar.make(emailUserLoginView, getResources().getString(R.string.credencialesEmailIncorrectas),
				Snackbar.LENGTH_LONG).show();
	}

	@Override
	public void emailUserNoVerify() {
		Snackbar.make(emailUserLoginView, getResources().getString(R.string.emailNoVerificado),
				Snackbar.LENGTH_LONG).show();
	}

	@Override
	public void emailNoValid() {
		Snackbar.make(emailUserLoginView, getResources().getString(R.string.emailInvalido),
				Snackbar.LENGTH_LONG).show();
	}

	@Override
	public void intentGoogle(GoogleApiClient googleApiClient) {
		Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
		startActivityForResult(intent, Constans.SIGN_IN_CODE);
	}

	@Override
	public void errorSigInGoogle() {
		Snackbar.make(emailUserLoginView, getResources().getString(R.string.ErrorSignInGoogle),
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
			contentLoginView.setVisibility(View.GONE);
			mProgressBar.setVisibility(View.VISIBLE);
		} else {
			contentLoginView.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.GONE);
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

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
			switch (requestCode){
				case RP_PHONE_STATE:
					getPresenter().getIdDevice(Manifest.permission.READ_PHONE_STATE, RP_PHONE_STATE);
					break;
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbinder.unbind();
	}
}
