package com.papaprogramador.presidenciales2019.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.papaprogramador.presidenciales2019.R;
import com.papaprogramador.presidenciales2019.io.Utils.Constantes;
import com.papaprogramador.presidenciales2019.io.Utils.Metodos;
import com.papaprogramador.presidenciales2019.io.Utils.ReferenciasFirebase;
import com.papaprogramador.presidenciales2019.model.Usuario;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

public class NewAccountActivity extends AppCompatActivity {
	private FirebaseAuth mAuth;
	private TextInputEditText EditTextEmail, EditTextPassword, EditTextUserName, confirmarEmail, confirmarPassword;
	private Button btnNewAccount;
	private ProgressBar mProgressBar;
	private String Username;
	private MaterialBetterSpinner spinnerDep;
	private String Departamento;
	private LinearLayout contenido;
	private String IDdispositivo;
	private String Useremail;
	private String firebaseUID;
	private DatabaseReference databaseReference;
	private boolean validacionDispositivo = true;

	boolean PasswordesValido = true;
	boolean EmailesValido = true;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_account);

		obtenerID();
		IniciarVista();

		mAuth = FirebaseAuth.getInstance();
		databaseReference = FirebaseDatabase.getInstance().getReference();

		//Implementación de un Spinner estilo material design
		final String[] departamento = {"Ahuachapán", "Cabañas", "Chalatenango", "Cuscatlán", "La Libertad", "La Paz",
				"La Unión", "Morazán", "San Miguel", "San Salvador", "San Vicente", "Santa Ana", "Sonsonate", "Usulután"};

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
				android.R.layout.simple_dropdown_item_1line, departamento);
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
				crearNuevaCuenta();
			}

		});

	}

	private void crearNuevaCuenta() {
		validarEmail(EditTextEmail, confirmarEmail);
		validarPassword(EditTextPassword, confirmarPassword);

		Username = EditTextUserName.getText().toString();
		String departamentoElegido = spinnerDep.getText().toString();

		boolean crearCuenta = true;

		if (Username.isEmpty()) {
			Toast.makeText(NewAccountActivity.this, R.string.EscribeNombreDeUsuario,
					Toast.LENGTH_LONG).show();
			crearCuenta = false;
		} else if (!EmailesValido) {
			Toast.makeText(NewAccountActivity.this, R.string.EmailDiferente,
					Toast.LENGTH_LONG).show();
			crearCuenta = false;
		} else if (!PasswordesValido) {
			Toast.makeText(NewAccountActivity.this, R.string.PasswordDiferente,
					Toast.LENGTH_LONG).show();
			crearCuenta = false;
		} else if (departamentoElegido.isEmpty()) {
			Toast.makeText(NewAccountActivity.this, R.string.EligeDepartamento,
					Toast.LENGTH_LONG).show();
			crearCuenta = false;
		}

		if (crearCuenta) {
			CreateNewAccount();
		}
	}

	private void IniciarVista() {

		EditTextEmail = findViewById(R.id.editTexEmailCreate);
		EditTextPassword = findViewById(R.id.editTextPasswordCreate);
		confirmarEmail = findViewById(R.id.editTexEmailConfirm);
		confirmarPassword = findViewById(R.id.editTextPasswordConfirm);
		btnNewAccount = findViewById(R.id.btnNewAccountCreate);
		mProgressBar = findViewById(R.id.ProgressBarNewAccount);
		EditTextUserName = findViewById(R.id.editTexUserName);
		spinnerDep = findViewById(R.id.spinnerDep);
		contenido = findViewById(R.id.contenido);
	}

	private void validarPassword(TextInputEditText editTextPassword, TextInputEditText confirmarPassword) {
		String pass = editTextPassword.getText().toString();
		String passconfirm = confirmarPassword.getText().toString();

		if (pass.isEmpty() || passconfirm.isEmpty()) {
			Toast.makeText(NewAccountActivity.this, R.string.PasswordVacio,
					Toast.LENGTH_LONG).show();
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
			Toast.makeText(NewAccountActivity.this, R.string.EmailVacio,
					Toast.LENGTH_LONG).show();
		} else if (!email.contains("@")) {
			Toast.makeText(NewAccountActivity.this, R.string.EmailInvalido,
					Toast.LENGTH_LONG).show();
		} else if (email.equals(emailconfirm)) {
			EmailesValido = true;
		} else {
			EmailesValido = false;
		}
	}

	//Método para crear la nueva cuenta en firebase
	private void CreateNewAccount() {
		String emailCreate = EditTextEmail.getText().toString();
		String passwordCreate = EditTextPassword.getText().toString();

		if (!emailCreate.isEmpty() && !passwordCreate.isEmpty()) {
			ProgressStatusVisible();
			mAuth.createUserWithEmailAndPassword(emailCreate, passwordCreate).addOnCompleteListener(this,
					new OnCompleteListener<AuthResult>() {
						@Override
						public void onComplete(@NonNull Task<AuthResult> task) {
							if (task.isSuccessful()) {
								//Implementar el método de registro de usuario desde aquí
								//Si se obtiene el resultado esperado con el método
								//Crear la cuenta normalmente y enviar el email de verificación de correo
								//Si no, crear el método necesario para eliminar la cuenta y notificar al usuario
								//En un futuro, inhabilitar las opciones para votar, permitir el resto de funciones de la app
								FirebaseUser user = mAuth.getCurrentUser();

								firebaseUID = user.getUid();
								Username = user.getDisplayName();
								Useremail = user.getEmail();

								validarDispositivo(IDdispositivo);

								if (validacionDispositivo) {
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
								}else {
									user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
										@Override
										public void onComplete(@NonNull Task<Void> task) {
											Toast.makeText(NewAccountActivity.this,R.string.DispositivoYaUtilizado,
													Toast.LENGTH_LONG).show();
										}
									});
								}
							} else {
								ProgressStatusGone();
								Toast.makeText(NewAccountActivity.this, R.string.CreateNewAccountERROR,
										Toast.LENGTH_LONG).show();
							}
						}
					});
		} else {
			Toast.makeText(NewAccountActivity.this, R.string.IntoEmailAndPasswordForNewAccount,
					Toast.LENGTH_LONG).show();
		}
	}

	private void validarDispositivo(final String iDdispositivo) {

		final DatabaseReference referenceIDdispositivo = FirebaseDatabase.getInstance().getReference().child(ReferenciasFirebase.NODO_ID_DISPOSITIVO);

		referenceIDdispositivo.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				Log.d("DatosIDDispositivos",dataSnapshot.getKey());//Eliminar despues de resolver el método
				for (DataSnapshot iddispositivos : dataSnapshot.getChildren()) {
					if (iddispositivos.getKey().equals(iDdispositivo)) {
						validacionDispositivo = false;
						break;
					} else {
						referenceIDdispositivo.setValue(IDdispositivo);
						validacionDispositivo = true;
					}

				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});
	}

	public void RegistrarUsuario(final String firebaseUID, String Username, String Useremail, String Departamento, final String IDdispositivo) {

		final Usuario usuario = new Usuario(Username, Useremail, Departamento, IDdispositivo, Constantes.VOTO_POR);

		final DatabaseReference databaseReference;
		databaseReference = FirebaseDatabase.getInstance().getReference();

		databaseReference.child(ReferenciasFirebase.NODO_UID).addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				for (DataSnapshot snapshot :
						dataSnapshot.getChildren()) {
					if (snapshot.getKey().equals(firebaseUID)) {
						break;
					} else {
						databaseReference.child(ReferenciasFirebase.NODO_USUARIO).child(firebaseUID).setValue(usuario);
						databaseReference.child(ReferenciasFirebase.NODO_UID).setValue(firebaseUID);
					}
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
			}
		});
	}

	//Método para ir al aviso para verificar el correo electrónico
	private void goEmailVerificationScreen() {
		String email = EditTextEmail.getText().toString();
		String password = EditTextPassword.getText().toString();

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
