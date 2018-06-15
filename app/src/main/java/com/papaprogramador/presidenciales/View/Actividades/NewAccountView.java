package com.papaprogramador.presidenciales.View.Actividades;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.papaprogramador.presidenciales.InterfacesMVP.NewAccount;
import com.papaprogramador.presidenciales.Presenters.NewAccountPresenter;
import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.Utils.Constans;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

public class NewAccountView extends MvpActivity<NewAccount.View,
		NewAccount.Presenter> implements NewAccount.View {

	private TextInputEditText emailUsuario, pass, nombreUsuario, emailUsuario2, pass2;
	private Button btnNewAccount;
	private ProgressBar progressBar;
	private MaterialBetterSpinner spinnerDepartamento;
	private LinearLayout contenido;
	private String idDispositivo;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_account);

		onStartVista();

		spinnerDepartamento.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
			}
		});


		btnNewAccount.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(android.view.View v) {

				String nUsuario = nombreUsuario.getText().toString();
				String eUsuario = emailUsuario.getText().toString();
				String eUsuario2 = emailUsuario2.getText().toString();
				String password = pass.getText().toString();
				String password2 = pass2.getText().toString();
				String departamento = spinnerDepartamento.getText().toString();
				Context context = NewAccountView.this;

				getPresenter().validarCampos(context, idDispositivo, nUsuario, eUsuario, eUsuario2,
						password, password2, departamento);
			}
		});
	}

	private void onStartVista() {

		nombreUsuario = findViewById(R.id.nombreUsuario);
		emailUsuario = findViewById(R.id.emailUsuario);
		emailUsuario2 = findViewById(R.id.editTexEmailConfirm);
		pass = findViewById(R.id.editTextPasswordCreate);
		pass2 = findViewById(R.id.editTextPasswordConfirm);
		progressBar = findViewById(R.id.ProgressBarNewAccount);
		spinnerDepartamento = findViewById(R.id.spinnerDep);
		contenido = findViewById(R.id.contenido);
		btnNewAccount = findViewById(R.id.btnNewAccountCreate);

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
				android.R.layout.simple_dropdown_item_1line, Constans.departamento);
		spinnerDepartamento.setAdapter(arrayAdapter);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null){
			idDispositivo = bundle.getString(Constans.PUT_ID_DISPOSITIVO);
		}
	}

	@NonNull
	@Override
	public NewAccount.Presenter createPresenter() {
		return new NewAccountPresenter();
	}

	@Override
	public void cuentaYaExiste() {

		Snackbar.make(btnNewAccount,getResources().getString(R.string.emailConCuentaAsociada),
				Snackbar.LENGTH_LONG )
				.setActionTextColor(getResources().getColor(R.color.accent))
				.setAction(getResources().getText(R.string.RESET_PASS), new android.view.View.OnClickListener() {
					@Override
					public void onClick(android.view.View v) {
						Intent intent = new Intent(NewAccountView.this,
								ResetPasswordView.class);
						startActivity(intent);
						finish();
					}
				})
				.show();

	}

	@Override
	public void mostrarProgreso(Boolean bool) {

		if (bool){
			contenido.setVisibility(android.view.View.GONE);
			progressBar.setVisibility(android.view.View.VISIBLE);
		} else {
			contenido.setVisibility(android.view.View.VISIBLE);
			progressBar.setVisibility(android.view.View.GONE);
		}
	}

	@Override
	public void nombreUsuarioVacio() {
		nombreUsuario.setError(getResources().getString(R.string.nombreUsuarioVacio));
	}

	@Override
	public void emailUsuarioVacio() {
		emailUsuario.setError(getResources().getString(R.string.emailUsuarioVacio));
	}

	@Override
	public void emailUsuario2Vacio() {
		emailUsuario2.setError(getResources().getString(R.string.confirmaEmailUsuario));
	}

	@Override
	public void passwordVacio() {
		pass.setError(getResources().getString(R.string.passVacio));
	}

	@Override
	public void password2Vacio() {
		pass2.setError(getResources().getString(R.string.pass2Vacio));
	}

	@Override
	public void departamentoVacio() {
		spinnerDepartamento.setError(getResources().getString(R.string.eligeTuDepartamento));
	}

	@Override
	public void errorEmailNoCoincide() {
		emailUsuario.setError(getResources().getString(R.string.emailNoCoincide));
		emailUsuario2.setError(getResources().getString(R.string.emailNoCoincide));
	}

	@Override
	public void errorEmailInvalido() {
		emailUsuario.setError(getResources().getString(R.string.emailInvalido));
		emailUsuario2.setError(getResources().getString(R.string.emailInvalido));
	}

	@Override
	public void errorPassInvalido() {
		pass.setError(getResources().getString(R.string.passInvalido));
		pass2.setError(getResources().getString(R.string.passInvalido));

		pass.setText("");
		pass2.setText("");
	}

	@Override
	public void errorPassNoCoincide() {
		pass.setError(getResources().getString(R.string.passNoCoincide));
		pass2.setError(getResources().getString(R.string.passNoCoincide));

		pass.setText("");
		pass2.setText("");
	}

	@Override
	public void irAVerificarEmail(String emailUsuario, String pass) {

		Intent intent = new Intent(NewAccountView.this, EmailVerifyView.class);
		intent.putExtra(Constans.PUT_EMAIL_USUARIO, emailUsuario);
		intent.putExtra(Constans.PUT_PASSWORD, pass);

		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
				Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
}
