package com.papaprogramador.presidenciales2019.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
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
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.papaprogramador.presidenciales2019.R;
import com.papaprogramador.presidenciales2019.io.Utils.Constantes;
import com.papaprogramador.presidenciales2019.io.Utils.ReferenciasFirebase;
import com.papaprogramador.presidenciales2019.model.Usuario;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

	//Declaracion de variables globales de este activity
	private TextInputEditText  mTextEmail;
	private TextInputEditText mTextPassword;
	private ProgressBar mProgressBar;
	private Button mBtnLogin;
	private Button mBtnNewAccount;
	private LinearLayout Logincontenido;
	private SignInButton mBtnLoginGoogle;
	private String IDdispositivo;
	private String Username;
	private String Useremail;
	private String firebaseUID;
	private DatabaseReference databaseReference;
	public static final int SIGN_IN_CODE = 777;

	//Variable para la autenticacion con Firebase
	private FirebaseAuth mAuth;

	//Escuchador para conocer el estado de logueo
	private FirebaseAuth.AuthStateListener Listener;

	//VARIABLE PARA EL CLIENTE API DE GOOGLE
	private GoogleApiClient googleApiClient;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		IniciarVista();
		obtenerID();

		//Inicializacion de la instancia de Firebase
		mAuth = FirebaseAuth.getInstance();
		//Referencia a la base de datos firebase
		databaseReference = FirebaseDatabase.getInstance().getReference();

		//Inicializacion del escuchador
		Listener = new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
				FirebaseUser user = mAuth.getCurrentUser();
				if (user != null) {
					if (user.isEmailVerified()) {
						goMainScreen();
					} else {
						Toast.makeText(LoginActivity.this, R.string.EmailNotVerified, Toast.LENGTH_LONG).show();
					}

				}
			}
		};
		//BOTONES DE INICIO DE SESION Y CREACION DE NUEVA CUENTA
		mBtnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				SignInWitchEmail(mTextEmail.getText().toString(), mTextPassword.getText().toString());

			}
		});

		mBtnNewAccount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//INTENT PARA IR A LA VISTA PARA CREAR UNA NUEVA CUENTA
				startActivity(new Intent(LoginActivity.this, NewAccountActivity.class));

			}
		});
		//FIN DE BOTONES DE INICIO DE SESION Y CREACION DE NUEVA CUENTA

		//INICIO AUTENTICACION CON GOOGLE
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getString(R.string.default_web_client_id))
				.requestProfile()
				.requestEmail()
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

	private void IniciarVista() {
		//Innicializacion de los elementos de la UI
		mTextEmail = findViewById(R.id.editTexEmail);
		mTextPassword = findViewById(R.id.editTextPassword);
		mBtnLogin = findViewById(R.id.btnLogin);
		mBtnNewAccount = findViewById(R.id.btnNewAccount);
		mBtnLoginGoogle = findViewById(R.id.btnLoginGoogle);
		// mBtnLoginFacebook = findViewById(R.id.btnLoginFacebook);
		mProgressBar = findViewById(R.id.mProgressBar);
		Logincontenido = findViewById(R.id.Logincontenido);

		mBtnLoginGoogle.setSize(SignInButton.SIZE_WIDE); //Tamaño del boton de Google
		mBtnLoginGoogle.setColorScheme(SignInButton.COLOR_DARK); //Estilo de color del boton de Google

	}

	//METODO PARA INICIAR SESION CON EMAIL
	private void SignInWitchEmail(final String email, final String password) {

		//Validar si los valores no estan vacios
		if (!email.isEmpty() && !password.isEmpty()) {
			ProgressStatusVisible();
			AuthCredential credential = EmailAuthProvider.getCredential(email, password);
			mAuth.signInWithCredential(credential).addOnCompleteListener(LoginActivity.this,
					new OnCompleteListener<AuthResult>() {
						@Override
						public void onComplete(@NonNull Task<AuthResult> task) {
							if (!task.isSuccessful()) {
								Toast.makeText(getApplicationContext(), R.string.EmailPasswordIncorrect,
										Toast.LENGTH_LONG).show();
							} else {
								FirebaseUser user = mAuth.getCurrentUser();
								if (!user.isEmailVerified()) {
									Toast.makeText(LoginActivity.this, R.string.EmailNoVerified, Toast.LENGTH_LONG).show();
								} else {
									goMainScreen();
								}
							}
							ProgressStatusGone();
						}
					});
		}
	}

	//METODOS NECESARIOS PARA EL INICIO DE SESION CON GOOGLE
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// callbackManager.onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == SIGN_IN_CODE) {

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
	private void FirebaseAuthWithGoogle(GoogleSignInAccount signInAccount) { //TODO: Revisar a fondo este método para corregir el error de la base de datos

		ProgressStatusVisible();
		AuthCredential credential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
		mAuth.signInWithCredential(credential).addOnCompleteListener(this,
				new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						ProgressStatusGone();
						if (task.isSuccessful()) {
							FirebaseUser user = mAuth.getCurrentUser();
							if (user != null){
								Username = user.getDisplayName();
								Useremail = user.getEmail();
								firebaseUID = user.getUid();
								RegistrarUsuario(firebaseUID, Username, Useremail,IDdispositivo);
							}else {
								Toast.makeText(LoginActivity.this,"El usuario es nulo",
										Toast.LENGTH_LONG).show();
							}
						}else {
							Toast.makeText(LoginActivity.this, getString(R.string.ErrorAuthWithGoogle),
									Toast.LENGTH_LONG).show();
						}
					}
				});
	}
	private void RegistrarUsuario(String firebaseUID, String Username, String Useremail, String IDdispositivo) {
		Usuario usuario = new Usuario(Username, Useremail, Constantes.DEPARTAMENTO, IDdispositivo, Constantes.VOTO_POR);

		databaseReference.child(ReferenciasFirebase.NODO_USUARIO).child(firebaseUID).setValue(usuario);
	}

	//Metodo para ir a el activity principal en caso de session exitosa
	protected void goMainScreen() {
		Intent intent = new Intent(LoginActivity.this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
				Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
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

	private void ProgressStatusVisible() {

		mProgressBar.setVisibility(View.VISIBLE);
		Logincontenido.setVisibility(View.GONE);


	}

	private void ProgressStatusGone() {

		mProgressBar.setVisibility(View.GONE);
		Logincontenido.setVisibility(View.VISIBLE);

	}

	public String obtenerID(){

		if(Build.VERSION.SDK_INT  < Build.VERSION_CODES.M){
			//Menores a Android 6.0
			IDdispositivo = getID();
			return IDdispositivo;
		} else {
			// Mayores a Android 6.0
			IDdispositivo ="";
			if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
					!= PackageManager.PERMISSION_GRANTED) {
				requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},
						225);
				IDdispositivo ="";
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
