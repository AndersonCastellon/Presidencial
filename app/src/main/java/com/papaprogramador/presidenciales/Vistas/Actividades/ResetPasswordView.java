package com.papaprogramador.presidenciales.Vistas.Actividades;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.papaprogramador.presidenciales.InterfacesMVP.ResetPassword;
import com.papaprogramador.presidenciales.Presentadores.ResetPasswordPresentador;
import com.papaprogramador.presidenciales.R;

public class ResetPasswordView extends MvpActivity<ResetPassword.Vista, ResetPassword.Presentador>
		implements ResetPassword.Vista {

	private TextInputEditText ResetEmail;
	private Button btnResetPass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_password);

		onStartVista();

		btnResetPass.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String emailUsuario = ResetEmail.getText().toString();
				getPresenter().emailUserProcess(emailUsuario);
			}
		});
	}

	@NonNull
	@Override
	public ResetPassword.Presentador createPresenter() {
		return new ResetPasswordPresentador();
	}

	private void onStartVista() {
		ResetEmail = findViewById(R.id.EmailReset);
		btnResetPass = findViewById(R.id.btnResetpassword);
	}

	@Override
	public void resetIsSuccesful() {
		Toast.makeText(ResetPasswordView.this,
				R.string.EmailResetSend,
				Toast.LENGTH_LONG).show();
		goLoginActivity();
	}

	@Override
	public void emailNoExist() {
		ResetEmail.setError(getString(R.string.EmailSinCuenta));
	}

	@Override
	public void emailIsEmpty() {
		ResetEmail.setError(getString(R.string.emailUsuarioVacio));
	}

	@Override
	public void emailIsInvalid() {
		ResetEmail.setError(getString(R.string.emailInvalido));
	}

	private void goLoginActivity() {
		Intent login = new Intent(ResetPasswordView.this, LoginView.class);
		startActivity(login);
	}
}