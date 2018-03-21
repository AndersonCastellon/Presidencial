package com.papaprogramador.presidenciales2019;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {

    //Declaracion de variables globales de este activity
    private EditText mTextEmail;
    private EditText mTextPassword;
    private ProgressBar mProgressBar;

    //Variable para la autenticacion con Firebase
    private FirebaseAuth mAuth;

    //Escuchador para conocer el estado de logueo
    private FirebaseAuth.AuthStateListener Listener;

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
        Button mBtnLoginGoogle = findViewById(R.id.btnLoginGoogle);
        Button mBtnLoginFacebook = findViewById(R.id.btnLoginFacebook);
        mProgressBar = findViewById(R.id.LoginProgressBar);

        //Barra de progreso INVISIBLE por defecto
        mProgressBar.setVisibility(View.INVISIBLE);

        //Inicializacion del escuchador
        Listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user == null){
                    //NO ESTA LOGUEADO
                }
                else{
                    //ESTA LOGUEADO
                    Toast.makeText(getApplicationContext(),"Iniciastes como: " + user.getEmail(),
                            Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
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


    }
    //METODO PARA INICIAR SESION CON EMAL
    private void Ingresar() {
        //Valores a las variables necesarias
        String email = mTextEmail.getText().toString();
        String password = mTextPassword.getText().toString();

        //PROGRESSBAR VISIBLE
        mProgressBar.setVisibility(View.VISIBLE);

        //Validar si los valores no estan vacios
        if (!email.isEmpty() && !password.isEmpty()){

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        //Toast.makeText(getApplicationContext(),"CORRECTO",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Email o contraseña incorrectos", Toast.LENGTH_LONG).show();
                    }


                    }


                });

        }

        //PROGRESSBAR VISIBLE
        mProgressBar.setVisibility(View.INVISIBLE);

    }

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
