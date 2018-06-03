package com.papaprogramador.presidenciales.Vistas.Actividades;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.papaprogramador.presidenciales.InterfacesMVP.Login;
import com.papaprogramador.presidenciales.Presentadores.LoginPresentador;
import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.Utilidades.Constantes;
import com.papaprogramador.presidenciales.Utilidades.ReferenciasFirebase;
import com.papaprogramador.presidenciales.Objetos.Usuario;


public class LoginVista extends MvpActivity<Login.Vista, Login.Presentador> implements Login.Vista {

	//Declaracion de variables globales de este activity
	private TextInputEditText emailUsuario;
	private TextInputEditText pass;
	private ProgressBar mProgressBar;
	private Button mBtnLogin, mBtnNewAccount, resetPass;
	private LinearLayout Logincontenido;
	private SignInButton mBtnLoginGoogle;

	private String IDdispositivo;
	private String Username;
	private String Useremail;
	private String firebaseUID;
	private DataSnapshot IDfirebase;
	private DataSnapshot UsuariosFirebase;
	public boolean validacionDispositivoConGoogle;
	public static final int SIGN_IN_CODE = 777;

	//Variable para la autenticacion con Firebase
	private FirebaseAuth mAuth;

	//Escuchador para conocer el estado de logueo
	private FirebaseAuth.AuthStateListener Listener;

	//VARIABLE PARA EL CLIENTE API DE GOOGLE
	private GoogleApiClient googleApiClient;

	private Context context = LoginVista.this;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		onStartVista();

		mBtnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String emailU = emailUsuario.getText().toString();
				String password = pass.getText().toString();
				getPresenter().iniciarSesionConEmail(context, emailU, password);
			}
		});

		mBtnNewAccount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getPresenter().obtenerIdDispositivo(LoginVista.this);
			}
		});

		resetPass.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent resetpass = new Intent(LoginVista.this, ResetPasswordVista.class);
				startActivity(resetpass);
			}
		});
		//FIN DE BOTONES DE INICIO DE SESION Y CREACION DE NUEVA CUENTA

		//INICIO AUTENTICACION CON GOOGLE
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getString(R.string.default_web_client_id))
				.requestProfile()
				.requestEmail() //TODO: INVESTIGAR COMO OBTENER EL EMAIL DE GOOGLE PARA VALIDARLO ANTES DE CREAR CUENTA EN FIREBASE
				.build();

		googleApiClient = new GoogleApiClient.Builder(this)
				.enableAutoManage(this, this)
				.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
				.build();

		mBtnLoginGoogle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
				startActivityForResult(intent, SIGN_IN_CODE);
			}
		});
		//FIN DE AUTENTICACION CON GOOGLE
	}

	private void onStartVista() {
		//Innicializacion de los elementos de la UI
		emailUsuario = findViewById(R.id.editTexEmail);
		pass = findViewById(R.id.editTextPassword);
		mBtnLogin = findViewById(R.id.btnLogin);
		mBtnNewAccount = findViewById(R.id.btnNewAccount);
		mBtnLoginGoogle = findViewById(R.id.btnLoginGoogle);
		mProgressBar = findViewById(R.id.mProgressBar);
		Logincontenido = findViewById(R.id.Logincontenido);
		resetPass = findViewById(R.id.recuperarPassword);

		mBtnLoginGoogle.setSize(SignInButton.SIZE_WIDE); //Tamaño del boton de Google
		mBtnLoginGoogle.setColorScheme(SignInButton.COLOR_DARK); //Estilo de color del boton de Google

	}

	@NonNull
	@Override
	public Login.Presentador createPresenter() {
		return new LoginPresentador();
	}

	@Override
	public void crearNuevaCuenta(String idDispositivo) {
		Intent intent = new Intent(LoginVista.this, NewAccountVista.class);
		intent.putExtra(Constantes.PUT_ID_DISPOSITIVO, idDispositivo);
		startActivity(intent);
	}

	@Override
	public void idYaUtilizado() {
		Snackbar.make(mBtnNewAccount, getResources().getString(R.string.DispositivoYautilizadoPorOtraCuenta),
				Snackbar.LENGTH_LONG).show();
	}

	@Override
	public void credencialesIncorrectas() {
		Snackbar.make(mBtnLogin, getResources().getString(R.string.credencialesEmailIncorrectas),
				Snackbar.LENGTH_LONG).show();
	}

	@Override
	public void emailNoVerificado() {
		Snackbar.make(mBtnLogin, getResources().getString(R.string.emailNoVerificado),
				Snackbar.LENGTH_LONG).show();
	}

	@Override
	public void irAVistaCandidatos() {
		Intent intent = new Intent(LoginVista.this, ListaCandidatosVista.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
				Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	//METODOS NECESARIOS PARA EL INICIO DE SESION CON GOOGLE
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == SIGN_IN_CODE) { //TODO switch para validar el requies code

			GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
			handleSingInResult(result);
		}
	}

	//Si el resultado de onActivityResult es exitoso se pasa el token a firebase aquí
	private void handleSingInResult(GoogleSignInResult result) {
		if (result.isSuccess()) {
			FirebaseAuthWithGoogle(result.getSignInAccount());
		} else {
			Toast.makeText(this, R.string.ErrorSignInGoogle, Toast.LENGTH_LONG).show();
		}
	}

	//Método para obtener las credenciales de Google y usarlas en FirebaseAuth
	private void FirebaseAuthWithGoogle(GoogleSignInAccount signInAccount) {
		onProgressbarVisible();
		AuthCredential credential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
		mAuth.signInWithCredential(credential).addOnCompleteListener(this,
				new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						onProgressbarGone();
						if (task.isSuccessful()) {
							FirebaseUser user = mAuth.getCurrentUser();

							if (user != null) {
								Username = user.getDisplayName();
								Useremail = user.getEmail();
								firebaseUID = user.getUid();

								validarIDfirebase(IDfirebase);

								if (validacionDispositivoConGoogle) {
									crearNuevoUsuarioFirebase(UsuariosFirebase);
								} else {
									EliminarUsuarioGoogle(user);
								}
							} else {
								Toast.makeText(LoginVista.this, R.string.Usuario_nulo,
										Toast.LENGTH_LONG).show();
							}
						} else {
							Toast.makeText(LoginVista.this, getString(R.string.ErrorAuthWithGoogle),
									Toast.LENGTH_LONG).show();
						}
					}
				});
	}

	private void EliminarUsuarioGoogle(FirebaseUser user) {
		user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task) {
				Toast.makeText(LoginVista.this, R.string.DispositivoYautilizadoPorOtraCuenta,
						Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


	}

	//metodos onStart y onStop sobreescritos para vigilar el estado de la sesion
	@Override
	protected void onStart() {
		super.onStart();
		mAuth.addAuthStateListener(Listener);

	}

	@Override
	protected void onStop() {
		super.onStop();
		if (Listener != null) {

			mAuth.removeAuthStateListener(Listener);
		}

	}


	public void onProgressbarVisible() {

		mProgressBar.setVisibility(View.VISIBLE);
		Logincontenido.setVisibility(View.GONE);


	}

	public void onProgressbarGone() {

		mProgressBar.setVisibility(View.GONE);
		Logincontenido.setVisibility(View.VISIBLE);

	}

	public void obtenerID() {

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			//Menores a Android 6.0
			IDdispositivo = getID();
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
		}
	}

	//Método que obtiene el IMEI
	private String getID() {

		String ID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
		return ID;

	}

	public void obtenerIDdeFirebase(String IDdispositivo) {

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

	private void validarIDfirebase(DataSnapshot dataSnapshot) {

		final DatabaseReference referenceIDdispositivo = FirebaseDatabase.getInstance().getReference();

		final DataSnapshot snapshotFirebase = dataSnapshot;
		String ID = IDdispositivo;
		String emailusuario = Useremail;

		if (snapshotFirebase.getValue() == null) {
			referenceIDdispositivo.child(ReferenciasFirebase.NODO_ID_DISPOSITIVO)
					.child(ID).setValue(emailusuario);
			validacionDispositivoConGoogle = true;
		} else
			validacionDispositivoConGoogle = snapshotFirebase.getValue().toString().equals(emailusuario);
	}

	private void obtenerUsuariosFirebase() {

		final DatabaseReference databaseReference;
		databaseReference = FirebaseDatabase.getInstance().getReference();

		databaseReference.child(ReferenciasFirebase.NODO_USUARIO).addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				almacenarUsuariosFirebase(dataSnapshot);
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
			}
		});
	}

	private void almacenarUsuariosFirebase(DataSnapshot dataSnapshot) {
		UsuariosFirebase = dataSnapshot;
	}

	private void crearNuevoUsuarioFirebase(DataSnapshot dataSnapshot) {

		final Usuario usuario = new Usuario(Username, Useremail, Constantes.VALOR_DEPARTAMENTO_DEFAULT, IDdispositivo, Constantes.VALOR_VOTO_DEFAULT);

		final DatabaseReference databaseReference;
		databaseReference = FirebaseDatabase.getInstance().getReference();

		if (dataSnapshot.getValue() == null) {
			databaseReference.child(ReferenciasFirebase.NODO_USUARIO)
					.child(firebaseUID).setValue(usuario);
		} else {
			for (DataSnapshot snapshot :
					dataSnapshot.getChildren()) {
				if (snapshot.getKey().equals(firebaseUID)) {
					databaseReference.child(ReferenciasFirebase.NODO_USUARIO).child(firebaseUID)
							.child(ReferenciasFirebase.NODO_NOMBRE_USUARIO).setValue(Username);
					break;
				} else {
					databaseReference.child(ReferenciasFirebase.NODO_USUARIO)
							.child(firebaseUID).setValue(usuario);
				}
			}
		}
	}

}
