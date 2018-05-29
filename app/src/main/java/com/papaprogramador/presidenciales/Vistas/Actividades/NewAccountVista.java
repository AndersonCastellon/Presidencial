package com.papaprogramador.presidenciales.Vistas.Actividades;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.papaprogramador.presidenciales.InterfacesMVP.NewAccount;
import com.papaprogramador.presidenciales.Presentadores.NewAccountPresentador;
import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.Utilidades.Constantes;
import com.papaprogramador.presidenciales.Utilidades.ReferenciasFirebase;
import com.papaprogramador.presidenciales.Objetos.Usuario;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

public class NewAccountVista extends MvpActivity<NewAccount.Vista,
		NewAccount.Presentador> implements NewAccount.Vista {
	private FirebaseAuth mAuth;
	private TextInputEditText emailUsuario, pass, nombreUsuario, emailUsuario2, pass2;
	private Button btnNewAccount;
	private ProgressBar mProgressBar;
	private String Username;
	private MaterialBetterSpinner spinnerDepartamento;
	private String departamento;
	private LinearLayout contenido;
	private String idDispositivo;
	private String Useremail;
	private String firebaseUID;
	private DataSnapshot IDfirebase;

	boolean PasswordesValido = true;
	boolean EmailesValido = true;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_account);

		onStartVista();

		spinnerDepartamento.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				departamento = spinnerDepartamento.getText().toString();
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

				getPresenter().validarCampos(idDispositivo, nUsuario, eUsuario, eUsuario2,
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
		mProgressBar = findViewById(R.id.ProgressBarNewAccount);
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
		return new NewAccountPresentador();
	}


	@Override
	public void almacenarID(String idDispositivo) {
		this.idDispositivo = idDispositivo;
	}

	@Override
	public void verificarEmail(String emailUsuario, String password) {

	}

	@Override
	public void errorEnCampo(String error) {

	}

	@Override
	public void idYaUtilizado() {
		Toast.makeText(NewAccountVista.this,
				R.string.DispositivoYautilizadoPorOtraCuenta, Toast.LENGTH_LONG).show();

		Intent intent = new Intent(NewAccountVista.this, LoginVista.class);
		startActivity(intent);
	}

	@Override
	public void mostrarProgreso() {

	}

	private boolean validarCampos() {
		validarEmail(emailUsuario, emailUsuario2);
		validarPassword(pass, pass2);

		String departamento = spinnerDepartamento.getText().toString();
		String username = nombreUsuario.getText().toString();

		campoUserName.setError(null);
		campoEmail.setError(null);
		campoConfirmEmail.setError(null);
		campoPassword.setError(null);
		pass2.setError(null);

		boolean crearCuenta = true;
		View foco = null;

		if (username.isEmpty()) {
			campoUserName.setError(getString(R.string.EscribeNombreDeUsuario));
			foco = campoUserName;
			crearCuenta = false;
		} else if (!EmailesValido) {
			campoEmail.setError(getString(R.string.EmailInvalido));
			foco = campoEmail;
			crearCuenta = false;
		} else if (!PasswordesValido) {
			campoPassword.setError(getString(R.string.PasswordDiferente));
			campoConfirmPassword.setError(getString(R.string.PasswordDiferente));
			pass.setText("");
			pass2.setText("");
			foco = campoPassword;
			crearCuenta = false;
		} else if (departamento.isEmpty()) {
			spinnerDepartamento.setError(getString(R.string.EligeDepartamento));
			foco = spinnerDepartamento;
			crearCuenta = false;
		}
		if (!crearCuenta) {
			foco.requestFocus();
			return false;
		} else {
			return crearCuenta;
		}

	}


	private void validarPassword(TextInputEditText editTextPassword, TextInputEditText confirmarPassword) {

		String pass = editTextPassword.getText().toString();
		String passconfirm = confirmarPassword.getText().toString();

		if (pass.isEmpty() || passconfirm.isEmpty()) {
			PasswordesValido = false;
		} else if (pass.equals(passconfirm)) {
			PasswordesValido = true;
		} else {
			PasswordesValido = false;
		}
	}

	private void validarEmail(TextInputEditText editTextEmail, TextInputEditText confirmarEmail) {

		String email = editTextEmail.getText().toString();
		String emailconfirm = confirmarEmail.getText().toString();

		if (email.isEmpty() || emailconfirm.isEmpty()) {
			EmailesValido = false;
		} else if (!email.contains("@")) {
			EmailesValido = false;
		} else if (email.equals(emailconfirm)) {
			EmailesValido = true;
		} else {
			EmailesValido = false;
		}
	}

	//Método para crear la nueva cuenta en firebase
	private void crearNuevaCuenta() {
		String email = emailUsuario.getText().toString();
		String password = pass.getText().toString();

		validarIDfirebase(IDfirebase, IDdispositivo, email);

		if (!email.isEmpty() && !password.isEmpty()) {
			ProgressStatusVisible();
			if (validacionDispositivo) {
				mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,
						new OnCompleteListener<AuthResult>() {
							@Override
							public void onComplete(@NonNull Task<AuthResult> task) {
								if (task.isSuccessful()) {
									FirebaseUser user = mAuth.getCurrentUser();

									firebaseUID = user.getUid();
									Useremail = user.getEmail();
									Username = nombreUsuario.getText().toString();

									RegistrarUsuario(firebaseUID, Username, Useremail, departamento, IDdispositivo);

									user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
										@Override
										public void onComplete(@NonNull Task<Void> task) {
											ProgressStatusGone();
											if (task.isSuccessful()) {
												goEmailVerificationScreen();
											}
										}
									});
								} else {
									ProgressStatusGone();
									Toast.makeText(NewAccountVista.this, R.string.CreateNewAccountERROR,
											Toast.LENGTH_LONG).show();
								}
							}
						});
			} else {
				Toast.makeText(NewAccountVista.this,
						R.string.DispositivoYautilizadoPorOtraCuenta, Toast.LENGTH_LONG).show();
			}
			ProgressStatusGone();
		} else {
			Toast.makeText(NewAccountVista.this, R.string.IntoEmailAndPasswordForNewAccount,
					Toast.LENGTH_LONG).show();
		}
	}

	private void obtenerIDfirebase(String IDdispositivo) {

	}

	private void almacenarIDfirebase(DataSnapshot dataSnapshot) {
		IDfirebase = dataSnapshot;
	}

	private void validarIDfirebase(DataSnapshot dataSnapshot, String iddispositivo, String emailusuario) {

		DatabaseReference referenceIDdispositivo = FirebaseDatabase.getInstance().getReference();
		final DataSnapshot dataIDfirebase = dataSnapshot;


	}

	private void RegistrarUsuario(final String firebaseUID, String Username, String Useremail, String Departamento, final String IDdispositivo) {

		Usuario usuario = new Usuario(Username, Useremail, Departamento, IDdispositivo, Constantes.VALOR_VOTO_DEFAULT);

		DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

		databaseReference.child(ReferenciasFirebase.NODO_USUARIO).child(firebaseUID).setValue(usuario);

	}

	//Método para ir al aviso para verificar el correo electrónico
	private void goEmailVerificationScreen() {
		String email = emailUsuario.getText().toString();
		String password = pass.getText().toString();

		Intent intent = new Intent(NewAccountVista.this, EmailVerifyVista.class);
		intent.putExtra("email", email);
		intent.putExtra("password", password);
		intent.putExtra("username", Username);
		intent.putExtra("departamento", departamento);

		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
				Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	private void ProgressStatusVisible() {

		mProgressBar.setVisibility(View.VISIBLE);
		contenido.setVisibility(View.GONE);

	}

	private void ProgressStatusGone() {

		mProgressBar.setVisibility(View.GONE);
		contenido.setVisibility(View.VISIBLE);
	}
}
