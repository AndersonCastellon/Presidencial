package com.papaprogramador.presidenciales2019.Vista.Actividades;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.papaprogramador.presidenciales2019.R;
import com.papaprogramador.presidenciales2019.io.Utils.Constantes;
import com.papaprogramador.presidenciales2019.io.Utils.ReferenciasFirebase;
import com.papaprogramador.presidenciales2019.Objetos.Usuario;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

public class NewAccountActivity extends AppCompatActivity {
	private FirebaseAuth mAuth;
	private TextInputEditText textoEmail, textoPassword, textoUserName, confirmarEmail, confirmarPassword;
	private Button btnNewAccount;
	private ProgressBar mProgressBar;
	private String Username;
	private MaterialBetterSpinner spinnerDep;
	private String Departamento;
	private ScrollView contenido;
	private String IDdispositivo;
	private String Useremail;
	private String firebaseUID;
	private DataSnapshot IDfirebase;
	private boolean validacionDispositivo;

	private TextInputLayout campoUserName, campoEmail, campoConfirmEmail, campoPassword, campoConfirmPassword;

	boolean PasswordesValido = true;
	boolean EmailesValido = true;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_account);

		mAuth = FirebaseAuth.getInstance();

		obtenerID();
		obtenerIDfirebase(IDdispositivo);
		IniciarVista();

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
				android.R.layout.simple_dropdown_item_1line, Constantes.departamento);
		spinnerDep.setAdapter(arrayAdapter);

		spinnerDep.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Departamento = spinnerDep.getText().toString();
			}
		});


		btnNewAccount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (validarCampos()) {
					crearNuevaCuenta();
				}
			}

		});

	}

	private boolean validarCampos() {
		validarEmail(textoEmail, confirmarEmail);
		validarPassword(textoPassword, confirmarPassword);

		String departamento = spinnerDep.getText().toString();
		String username = textoUserName.getText().toString();

		campoUserName.setError(null);
		campoEmail.setError(null);
		campoConfirmEmail.setError(null);
		campoPassword.setError(null);
		confirmarPassword.setError(null);

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
			textoPassword.setText("");
			confirmarPassword.setText("");
			foco = campoPassword;
			crearCuenta = false;
		} else if (departamento.isEmpty()) {
			spinnerDep.setError(getString(R.string.EligeDepartamento));
			foco = spinnerDep;
			crearCuenta = false;
		}
		if (!crearCuenta) {
			foco.requestFocus();
			return false;
		} else {
			return crearCuenta;
		}

	}

	private void IniciarVista() {

		textoEmail = findViewById(R.id.editTexEmailCreate);
		textoPassword = findViewById(R.id.editTextPasswordCreate);
		confirmarEmail = findViewById(R.id.editTexEmailConfirm);
		confirmarPassword = findViewById(R.id.editTextPasswordConfirm);
		btnNewAccount = findViewById(R.id.btnNewAccountCreate);
		mProgressBar = findViewById(R.id.ProgressBarNewAccount);
		textoUserName = findViewById(R.id.editTexUserName);
		spinnerDep = findViewById(R.id.spinnerDep);
		contenido = findViewById(R.id.contenido);

		campoUserName = findViewById(R.id.campo_user_name);
		campoEmail = findViewById(R.id.campo_email);
		campoConfirmEmail = findViewById(R.id.campo_confirm_email);
		campoPassword = findViewById(R.id.campo_password);
		campoConfirmPassword = findViewById(R.id.campo_confirm_password);
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
		String email = textoEmail.getText().toString();
		String password = textoPassword.getText().toString();

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
									Username = textoUserName.getText().toString();

									RegistrarUsuario(firebaseUID, Username, Useremail, Departamento, IDdispositivo);

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
									Toast.makeText(NewAccountActivity.this, R.string.CreateNewAccountERROR,
											Toast.LENGTH_LONG).show();
								}
							}
						});
			}else {
				Toast.makeText(NewAccountActivity.this,
						R.string.DispositivoYautilizadoPorOtraCuenta, Toast.LENGTH_LONG).show();
			}
			ProgressStatusGone();
		} else {
			Toast.makeText(NewAccountActivity.this, R.string.IntoEmailAndPasswordForNewAccount,
					Toast.LENGTH_LONG).show();
		}
	}

	private void obtenerIDfirebase(String IDdispositivo) {

		final DatabaseReference referenceIDdispositivo = FirebaseDatabase.getInstance().getReference();

		referenceIDdispositivo.child(ReferenciasFirebase.NODO_ID_DISPOSITIVO)
				.child(IDdispositivo).addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				almacenarIDfirebase(dataSnapshot);
				referenceIDdispositivo.removeEventListener(this);
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
			}
		});
	}

	private void almacenarIDfirebase(DataSnapshot dataSnapshot) {
		IDfirebase = dataSnapshot;
	}

	private void validarIDfirebase(DataSnapshot dataSnapshot, String iddispositivo, String emailusuario) {

		DatabaseReference referenceIDdispositivo = FirebaseDatabase.getInstance().getReference();
		final DataSnapshot dataIDfirebase = dataSnapshot;

		if (dataIDfirebase.getValue() == null){
			referenceIDdispositivo.child(ReferenciasFirebase.NODO_ID_DISPOSITIVO)
					.child(iddispositivo).setValue(emailusuario);
			validacionDispositivo = true;
		}else {
			validacionDispositivo = dataIDfirebase.getValue().toString().equals(emailusuario);
		}
	}

	private void RegistrarUsuario(final String firebaseUID, String Username, String Useremail, String Departamento, final String IDdispositivo) {

		Usuario usuario = new Usuario(Username, Useremail, Departamento, IDdispositivo, Constantes.VALOR_VOTO_DEFAULT);

		DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

		databaseReference.child(ReferenciasFirebase.NODO_USUARIO).child(firebaseUID).setValue(usuario);

	}

	//Método para ir al aviso para verificar el correo electrónico
	private void goEmailVerificationScreen() {
		String email = textoEmail.getText().toString();
		String password = textoPassword.getText().toString();

		Intent intent = new Intent(NewAccountActivity.this, EmailVerificationActivity.class);
		intent.putExtra("email", email);
		intent.putExtra("password", password);
		intent.putExtra("username", Username);
		intent.putExtra("departamento", Departamento);

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

	public String obtenerID() {

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			//Menores a Android 6.0
			IDdispositivo = getID();
			return IDdispositivo;
		} else {
			// Mayores a Android 6.0
			IDdispositivo = "";
			if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
					!= PackageManager.PERMISSION_GRANTED) {
				requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},
						225);
				IDdispositivo = "";
			} else {
				IDdispositivo = getID();
			}

			return IDdispositivo;

		}
	}

	//Método que obtiene el IMEI
	private String getID() {

		String ID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
		return ID;

	}
}
