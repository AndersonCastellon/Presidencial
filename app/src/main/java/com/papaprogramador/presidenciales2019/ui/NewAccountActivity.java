package com.papaprogramador.presidenciales2019.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.papaprogramador.presidenciales2019.R;

public class NewAccountActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
//    private FirebaseAuth.AuthStateListener Listener;

    private EditText EditTextEmail, EditTextPassword;
    private Button btnNewAccount;
    private ProgressBar mProgressBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        EditTextEmail = findViewById(R.id.editTexEmailCreate);
        EditTextPassword = findViewById(R.id.editTextPasswordCreate);
        btnNewAccount = findViewById(R.id.btnNewAccountCreate);
        mProgressBar = findViewById(R.id.ProgressBarNewAccount);

        mAuth = FirebaseAuth.getInstance();
        //Inicializacion del escuchador
/*        Listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null){
                    startActivity(new Intent(NewAccountActivity.this, MainActivity.class));
                }
            }
        };*/


        btnNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
            }
        });

    }
    //Método para crear la nueva cuenta en firebase
    private void CreateNewAccount() {
        String emailCreate = EditTextEmail.getText().toString();
        String passwordCreate = EditTextPassword.getText().toString();

        if (!emailCreate.isEmpty() && !passwordCreate.isEmpty()){
            ProgressStatusVisible();
            mAuth.createUserWithEmailAndPassword(emailCreate, passwordCreate).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            ProgressStatusGone();
                           if (task.isSuccessful()){
                               goEmailVerificationScreen();
                           }
                        }
                    });
                }else{
                    ProgressStatusGone();
                    Toast.makeText(NewAccountActivity.this, R.string.CreateNewAccountERROR,
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        }else {
            Toast.makeText(NewAccountActivity.this, R.string.IntoEmailAndPasswordForNewAccount,
                    Toast.LENGTH_LONG).show();
        }

    }

    //Método para ir al aviso para verificar el correo electrónico
    private void goEmailVerificationScreen() {
        String email = EditTextEmail.getText().toString();
        String password = EditTextPassword.getText().toString();

        Intent intent = new Intent(NewAccountActivity.this, EmailVerificationActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("password", password);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

/*    @Override
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

    }*/

    private void ProgressStatusVisible(){

        EditTextEmail.setVisibility(View.GONE);
        EditTextPassword.setVisibility(View.GONE);
        btnNewAccount.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);

    }
    private void ProgressStatusGone(){

        EditTextEmail.setVisibility(View.VISIBLE);
        EditTextPassword.setVisibility(View.VISIBLE);
        btnNewAccount.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }
}
