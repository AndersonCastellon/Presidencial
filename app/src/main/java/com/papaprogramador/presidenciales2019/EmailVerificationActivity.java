package com.papaprogramador.presidenciales2019;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailVerificationActivity extends AppCompatActivity {
    private String emailIntent;
    private String passowordIntent;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener Listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);



        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            emailIntent = bundle.getString("email");
            passowordIntent = bundle.getString("password");
        }

        mAuth = FirebaseAuth.getInstance();
        Listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null){
                    if (user.isEmailVerified()){
                        SignInWitchEmail(emailIntent, passowordIntent);
                    }
                }else {
                    goLogInScreen();
                }
            }
        };


    }
    //METODO PARA INICIAR SESION CON EMAIL
    private void SignInWitchEmail(String email, String password) {
        ProgressStatusVisible();
        //Validar si los valores no estan vacios
        if (!email.isEmpty() && !password.isEmpty()){

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            ProgressStatusGone();
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

    private void ProgressStatusGone() {
    }

    private void ProgressStatusVisible() {
    }

    private void goMainScreen() {
        Intent intent = new Intent(EmailVerificationActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    protected void goLogInScreen() {
        Intent intent = new Intent(EmailVerificationActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
