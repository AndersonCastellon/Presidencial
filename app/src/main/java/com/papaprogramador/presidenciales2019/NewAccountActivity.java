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

public class NewAccountActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener Listener;

    private EditText EditTextEmail, EditTextPassword;
    private Button btnLoginGoogle;
    private Button btnLoginFacebook;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        mAuth = FirebaseAuth.getInstance();
        EditTextEmail = findViewById(R.id.editTexEmailCreate);
        EditTextPassword = findViewById(R.id.editTextPasswordCreate);
        Button btnNewAccount = findViewById(R.id.btnNewAccountCreate);
        ProgressBar mProgressbar = findViewById(R.id.LoginProgressBarCreate);
        mProgressbar.setVisibility(View.INVISIBLE);

        //Inicializacion del escuchador
        Listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null){
                    //ESTA LOGUEADO
                    Toast.makeText(getApplicationContext(),"Te registrastes como: " + user.getEmail(),
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
/*                    Toast.makeText(NewAccountActivity.this, "Cuenta creada correctamente",
                            Toast.LENGTH_LONG).show();*/
                    startActivity(new Intent(NewAccountActivity.this, MainActivity.class));
                }else{

                    Toast.makeText(NewAccountActivity.this, "Ha ocurrido un error",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        }else {
            Toast.makeText(NewAccountActivity.this, "Introduce Email y Contraseña", Toast.LENGTH_LONG).show();
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
