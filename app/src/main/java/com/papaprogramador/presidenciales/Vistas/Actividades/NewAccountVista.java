package com.papaprogramador.presidenciales.Vistas.Actividades;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.papaprogramador.presidenciales.InterfacesMVP.NewAccount;
import com.papaprogramador.presidenciales.Presentadores.NewAccountPresentador;
import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.Utilidades.Constantes;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

public class NewAccountVista extends MvpActivity<NewAccount.Vista,
		NewAccount.Presentador> implements NewAccount.Vista {

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
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			}
		});


		btnNewAccount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				String nUsuario = nombreUsuario.getText().toString();
				String eUsuario = emailUsuario.getText().toString();
				String eUsuario2 = emailUsuario2.getText().toString();
				String password = pass.getText().toString();
				String password2 = pass2.getText().toString();
				String departamento = spinnerDepartamento.getText().toString();
				Context context = getApplicationContext();

				getPresenter().validarCampos(context, idDispositivo, nUsuario, eUsuario, eUsuario2,
						password, password2, departamento);
			}
		});
	}

	private void onStartVista() {

		getPresenter().obtenerIdDispositivo(NewAccountVista.this);

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
				android.R.layout.simple_dropdown_item_1line, Constantes.departamento);
		spinnerDepartamento.setAdapter(arrayAdapter);
	}

	@NonNull
	@Override
	public NewAccount.Presentador createPresenter() {
		return new NewAccountPresentador(NewAccountVista.this);
	}


	@Override
	public void almacenarID(String idDispositivo) {
		this.idDispositivo = idDispositivo;
	}

	@Override
	public void idYaUtilizado() {
		Toast.makeText(NewAccountVista.this,
				R.string.DispositivoYautilizadoPorOtraCuenta, Toast.LENGTH_LONG).show();

		Intent intent = new Intent(NewAccountVista.this, LoginVista.class);
		startActivity(intent);
	}

	@Override
	public void cuentaYaExiste() {

	}

	@Override
	public void mostrarProgreso() {

		if (contenido.getVisibility() == View.VISIBLE &&
				progressBar.getVisibility() == View.GONE){
			contenido.setVisibility(View.GONE);
			progressBar.setVisibility(View.VISIBLE);
		} else {
			contenido.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
		}
	}

	@Override
	public void nombreUsuarioVacio() {

	}

	@Override
	public void emailUsuarioVacio() {

	}

	@Override
	public void emailUsuario2Vacio() {

	}

	@Override
	public void passwordVacio() {

	}

	@Override
	public void password2Vacio() {

	}

	@Override
	public void departamentoVacio() {

	}

	@Override
	public void errorEmailNoCoincide() {

	}

	@Override
	public void errorPassInvalido() {

	}

	@Override
	public void errorPassNoCoincide() {

	}

	@Override
	public void irAVerificarEmail(String emailUsuario, String pass) {

		Intent intent = new Intent(NewAccountVista.this, EmailVerifyVista.class);
		intent.putExtra(Constantes.PUT_EMAIL_USUARIO, emailUsuario);
		intent.putExtra(Constantes.PUT_PASSWORD, pass);

		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
				Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
}
