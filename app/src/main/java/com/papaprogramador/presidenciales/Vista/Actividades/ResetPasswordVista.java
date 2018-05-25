package com.papaprogramador.presidenciales.Vista.Actividades;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.papaprogramador.presidenciales.InterfacesMVP.ResetPassword;
import com.papaprogramador.presidenciales.Presentador.ResetPasswordPresentador;
import com.papaprogramador.presidenciales.R;

public class ResetPasswordVista extends AppCompatActivity implements ResetPassword.Vista {

	private TextInputEditText ResetEmail;
	private Button btnResetPass;
	private ResetPassword.Presentador presentador;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_password);

		onStartVista();

		btnResetPass.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String emailUsuario = ResetEmail.getText().toString();
				presentador.procesarEmailUsuario(emailUsuario);
			}
		});
	}

	private void onStartVista() {
		ResetEmail = findViewById(R.id.EmailReset);
		btnResetPass = findViewById(R.id.btnResetpassword);
		presentador = new ResetPasswordPresentador(this);
	}

	@Override
	public void mostrarResultadoExitoso() {
		Toast.makeText(ResetPasswordVista.this, R.string.EmailResetSend,
				Toast.LENGTH_LONG).show();
		goLoginActivity();
	}

	@Override
	public void errorPorEmailSinCuentaAsociada() {
		ResetEmail.setError(getString(R.string.EmailSinCuenta));
	}

	@Override
	public void errorPorCampoVacio() {
		ResetEmail.setError(getString(R.string.EmailVacio));
	}

	@Override
	public void errorPorEmailInvalido() {
		ResetEmail.setError(getString(R.string.EmailInvalido));
	}

	private void goLoginActivity() {
		Intent login = new Intent(ResetPasswordVista.this, LoginActivity.class);
		startActivity(login);
	}
}
