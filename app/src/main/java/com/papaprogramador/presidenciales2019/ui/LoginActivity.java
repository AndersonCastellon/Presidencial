package com.papaprogramador.presidenciales2019.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.papaprogramador.presidenciales2019.R;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    //Declaracion de variables globales de este activity
    private EditText mTextEmail;
    private EditText mTextPassword;
    private ProgressBar mProgressBar;
    private Button mBtnLogin;
    private Button mBtnNewAccount;
    private SignInButton mBtnLoginGoogle;
    private LoginButton mBtnLoginFacebook;
    private TextView mtextView;
    private CallbackManager callbackManager;
    public  static final int SIGN_IN_CODE = 777;

    //Variable para la autenticacion con Firebase
    private FirebaseAuth mAuth;

    //Escuchador para conocer el estado de logueo
    private FirebaseAuth.AuthStateListener Listener;

    //VARIABLE PARA EL CLIENTE API DE GOOGLE
    private GoogleApiClient googleApiClient;

    //Inicio Metodo onCreate
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Inicializacion de la instancia de Firebase
        mAuth = FirebaseAuth.getInstance();

        //Innicializacion de los elementos de la UI
        mTextEmail = findViewById(R.id.editTexEmail);
        mTextPassword = findViewById(R.id.editTextPassword);
        mBtnLogin = findViewById(R.id.btnLogin);
        mBtnNewAccount = findViewById(R.id.btnNewAccount);
        mBtnLoginGoogle = findViewById(R.id.btnLoginGoogle);
       // mBtnLoginFacebook = findViewById(R.id.btnLoginFacebook);
        mProgressBar = findViewById(R.id.mProgressBar);
        mtextView = findViewById(R.id.tvText);



        //Inicializacion del escuchador
        Listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null){
                    if (user.isEmailVerified()){
                        goMainScreen();
                    }else {
                        Toast.makeText(LoginActivity.this, R.string.EmailNotVerified,Toast.LENGTH_LONG).show();
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

/*        //INICIO AUTENTICACION CON FACEBOOK

        callbackManager = CallbackManager.Factory.create();
        //Linea para leer el correo del usuario
        mBtnLoginFacebook.setReadPermissions("email");
        mBtnLoginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, getString(R.string.CancelLoginFacebook),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, R.string.ErrorLoginFacebook,
                        Toast.LENGTH_LONG).show();

            }
        });


        //FIN AUTENTICACION CON FACEBOOK*/

        //DENTRO DE ONCREATE
    }
    //FUERA DEL ONCREATE

    //Método para enviar el token de facebook a firebase y realizar la autenticacion
  /*  private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(LoginActivity.this,
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                ProgressStatusVisible();
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.facebookErrorLogin,
                            Toast.LENGTH_LONG).show();
                }else {
                    goMainScreen();
                }
                ProgressStatusGone();
            }
        });
    }*/

    //METODO PARA INICIAR SESION CON EMAIL
    private void SignInWitchEmail(final String email, final String password) {

        //Validar si los valores no estan vacios
        if (!email.isEmpty() && !password.isEmpty()){
            final FirebaseUser user = mAuth.getCurrentUser();
            user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (user.isEmailVerified()){
                        ProgressStatusVisible();
                        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
                        mAuth.signInWithCredential(credential).addOnCompleteListener(LoginActivity.this,
                                new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), R.string.EmailPasswordIncorrect,
                                                    Toast.LENGTH_LONG).show();
                                        }else {
                                            goMainScreen();
                                        }
                                        ProgressStatusGone();
                                    }
                                });

                    }else {
                        Toast.makeText(LoginActivity.this,R.string.EmailNoVerified, Toast.LENGTH_LONG).show();
                    }
                }
            });

        }else {
            Toast.makeText(LoginActivity.this, R.string.IntoCredentials, Toast.LENGTH_LONG).show();
        }
    }


    //METODOS NECESARIOS PARA EL INICIO DE SESION CON GOOGLE
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       // callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_CODE){

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSingInResult(result);
        }
    }

    //Si el resultado de onActivityResult es exitoso se pasa el token a firebase aquí
    @SuppressLint("ResourceType")
    private void handleSingInResult(GoogleSignInResult result) {
        if (result.isSuccess()){
            FirebaseAuthWithGoogle(result.getSignInAccount());
        }else {
            Toast.makeText(this,R.string.ErrorSignInGoogle, Toast.LENGTH_LONG).show();
        }
    }

    //Método para obtener las credenciales de Google y usarlas en FirebaseAuth
    private void FirebaseAuthWithGoogle(GoogleSignInAccount signInAccount) {

        ProgressStatusVisible();
        AuthCredential credential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                ProgressStatusGone();
                if (!task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,getString(R.string.ErrorAuthWithGoogle),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
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
        if (Listener != null){

            mAuth.removeAuthStateListener(Listener);
        }

    }

    private void ProgressStatusVisible(){

        mTextEmail.setVisibility(View.GONE);
        mTextPassword.setVisibility(View.GONE);
        mBtnLogin.setVisibility(View.GONE);
        mBtnNewAccount.setVisibility(View.GONE);
        mBtnLoginGoogle.setVisibility(View.GONE);
        mBtnLoginFacebook.setVisibility(View.GONE);
       mtextView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);

    }
    private void ProgressStatusGone(){

        mTextEmail.setVisibility(View.VISIBLE);
        mTextPassword.setVisibility(View.VISIBLE);
        mBtnLogin.setVisibility(View.VISIBLE);
        mBtnNewAccount.setVisibility(View.VISIBLE);
        mBtnLoginGoogle.setVisibility(View.VISIBLE);
        mBtnLoginFacebook.setVisibility(View.VISIBLE);
        mtextView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);

    }
}
