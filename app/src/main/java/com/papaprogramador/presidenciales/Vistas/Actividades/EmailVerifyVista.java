package com.papaprogramador.presidenciales.Vistas.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.papaprogramador.presidenciales.InterfacesMVP.EmailVerify;
import com.papaprogramador.presidenciales.Presentadores.EmailVerifyPresenter;
import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.Utilidades.Constantes;

public class EmailVerifyVista extends MvpActivity<EmailVerify.Vista, EmailVerify.Presentador>
		implements EmailVerify.Vista {

	private String emailUser;
	private String pass;
	private Button mButton;
	private ConstraintLayout contenido;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_email_verification);

		onStartView();

		mButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getPresenter().startIsEmailIsVerify(emailUser, pass);
			}
		});

	}

	private void onStartView() {

		mButton = findViewById(R.id.EmailVerified);

		contenido = findViewById(R.id.contenido);
		progressBar = findViewById(R.id.progressBar);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			emailUser = bundle.getString(Constantes.PUT_EMAIL_USUARIO);
			pass = bundle.getString(Constantes.PUT_PASSWORD);
		}
	}

	@NonNull
	@Override
	public EmailVerify.Presentador createPresenter() {
		return new EmailVerifyPresenter(this);
	}

	@Override
	public void emailNoVerify() {
		Snackbar.make(mButton, getResources().getString(R.string.emailNoVerificado),
				Snackbar.LENGTH_LONG).show();
	}

	@Override
	public void errorSession() {
		Snackbar.make(mButton, getResources().getString(R.string.errorThis),
				Snackbar.LENGTH_LONG).show();
	}

	@Override
	public void goMainActivity() {
		Intent intent = new Intent(EmailVerifyVista.this, ListCandidatosView.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
				Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	@Override
	public void showProgressBar(boolean show) {
		if (show) {
			contenido.setVisibility(View.GONE);
			progressBar.setVisibility(View.VISIBLE);
		} else {
			contenido.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
		}
	}
}
