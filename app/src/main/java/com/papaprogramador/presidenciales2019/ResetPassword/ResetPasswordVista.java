package com.papaprogramador.presidenciales2019.ResetPassword;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.papaprogramador.presidenciales2019.R;
import com.papaprogramador.presidenciales2019.ui.Actividades.LoginActivity;

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
		Toast.makeText(ResetPasswordVista.this, R.string.EmailSinCuenta,
				Toast.LENGTH_LONG).show();
	}

	@Override
	public void errorPorCampoVacio() {
//		TextInputLayout textInputLayout = findViewById(R.id.inputResetEmail);
//		textInputLayout.setError(getString(R.string.EmailVacio));
		ResetEmail.setError(getString(R.string.EmailVacio));
	}

	@Override
	public void errorPorEmailInvalido() {
		Toast.makeText(ResetPasswordVista.this, R.string.EmailInvalido,
				Toast.LENGTH_LONG).show();
	}

	private void goLoginActivity() {
		Intent login = new Intent(ResetPasswordVista.this, LoginActivity.class);
		startActivity(login);
	}
}
