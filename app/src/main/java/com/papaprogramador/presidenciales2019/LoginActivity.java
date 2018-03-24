package com.papaprogramador.presidenciales2019;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    //Declaracion de variables globales de este activity
    private EditText mTextEmail;
    private EditText mTextPassword;
    private ProgressBar mProgressBar;
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
        Button mBtnLogin = findViewById(R.id.btnLogin);
        Button mBtnNewAccount = findViewById(R.id.btnNewAccount);
        SignInButton mBtnLoginGoogle = findViewById(R.id.btnLoginGoogle);
        Button mBtnLoginFacebook = findViewById(R.id.btnLoginFacebook);
        mProgressBar = findViewById(R.id.mProgressBar);


        //Inicializacion del escuchador
        Listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null){
                    //ESTA LOGUEADO
                    Toast.makeText(getApplicationContext(),getString(R.string.IniciasteComo) + user.getEmail(),
                            Toast.LENGTH_LONG).show();
                    goMainScreen();
                }
            }
        };
        //BOTONES DE INICIO DE SESION Y CREACION DE NUEVA CUENTA
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //METODO PARA INGRESAR
                Ingresar();

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

        //DENTRO DE ONCREATE
    }


    //FUERA DEL ONCREATE



    //METODO PARA INICIAR SESION CON EMAIL
    private void Ingresar() {
        mProgressBar.setVisibility(View.VISIBLE);

        //Valores a las variables necesarias
        String email = mTextEmail.getText().toString();
        String password = mTextPassword.getText().toString();

        //Validar si los valores no estan vacios
        if (!email.isEmpty() && !password.isEmpty()){

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mProgressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()){
                        goMainScreen();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), R.string.EmailPasswordIncorrect,
                                Toast.LENGTH_LONG).show();
                    }
                    }
                });
        }
    }


    //METODOS NECESARIOS PARA EL INICIO DE SESION CON GOOGLE
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

        mProgressBar.setVisibility(View.VISIBLE);
        AuthCredential credential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mProgressBar.setVisibility(View.GONE);

                if (!task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,getString(R.string.ErrorAuthWithGoogle),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /*Este metodo goMainScreen puede ser utilizado para ir a la siguiente activity en caso de que el inicio de sesion
    sea exitoso, se puede utilizar para cualquier opcion de inicio de sesion, google, facebook y correo
    electronico*/
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
}
