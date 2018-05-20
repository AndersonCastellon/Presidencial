package com.papaprogramador.presidenciales2019.RessetPassword;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
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

public class ResetPasswordVista extends AppCompatActivity {

	FirebaseAuth firebaseAuth;
	//Objetos de la interfaz
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
				String emailReset = ResetEmail.getText().toString();

				if (validarEmail(emailReset, v)) {
					enviarCorreoDeRecuperacion(emailReset, v);
				}
			}
		});
	}

	private void onStartVista() {
		ResetEmail = findViewById(R.id.EmailReset);
		btnResetPass = findViewById(R.id.btnResetpassword);
	}

	private void enviarCorreoDeRecuperacion(String emailReset, final View v) {
		firebaseAuth = FirebaseAuth.getInstance();

		firebaseAuth.sendPasswordResetEmail(emailReset)
				.addOnCompleteListener(new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task) {
				if (task.isSuccessful()){
					Toast.makeText(ResetPasswordVista.this,R.string.EmailResetSend,
							Toast.LENGTH_LONG).show();
					goLoginActivity();
				}else {
					Snackbar.make(v, getString(R.string.EmailSinCuenta), Snackbar.LENGTH_LONG).show();
				}
			}
		});
	}

	private void goLoginActivity() {
		Intent login = new Intent(ResetPasswordVista.this, LoginActivity.class);
		startActivity(login);
	}

	private boolean validarEmail(String emailReset, View v) {
		if (!emailReset.isEmpty() && emailReset.contains("@")) {
			return true;
		} else {
			Snackbar.make(v, getString(R.string.EmailInvalido), Snackbar.LENGTH_LONG).show();
			return false;
		}
	}
}
