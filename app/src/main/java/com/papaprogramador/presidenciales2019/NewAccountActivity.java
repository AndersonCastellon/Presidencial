package com.papaprogramador.presidenciales2019;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NewAccountActivity extends LoginActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener Listener;

    private EditText EditTextEmail, EditTextPassword;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        mAuth = FirebaseAuth.getInstance();
        EditTextEmail = findViewById(R.id.editTexEmailCreate);
        EditTextPassword = findViewById(R.id.editTextPasswordCreate);
        Button btnNewAccount = findViewById(R.id.btnNewAccountCreate);

        //Inicializacion del escuchador
        Listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null){
                    //ESTA LOGUEADO
                    Toast.makeText(getApplicationContext(),R.string.IniciasteComo + user.getEmail(),
                            Toast.LENGTH_LONG).show();
                    startActivity(new Intent(NewAccountActivity.this, MainActivity.class));
                }
            }
        };


        btnNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
            }
        });

    }

    private void CreateNewAccount() {
        String emailCreate = EditTextEmail.getText().toString();
        String passwordCreate = EditTextPassword.getText().toString();

        if (!emailCreate.isEmpty() && !passwordCreate.isEmpty()){
            mAuth.createUserWithEmailAndPassword(emailCreate, passwordCreate).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    goMainScreen();
                }else{

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
