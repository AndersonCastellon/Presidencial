package com.papaprogramador.presidenciales2019;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailVerificationActivity extends AppCompatActivity {
    private String emailIntent;
    private String passwordIntent;
    private TextView mTextView;
    private Button mButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener Listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        //Instancia de elementos de la UI
        mTextView = findViewById(R.id.TextViewConfirmationNotice);
        mButton = findViewById(R.id.EmailVerified);

        //Se rescatan los valores del Inten mediante el Bundle
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            emailIntent = bundle.getString("email");
            passwordIntent = bundle.getString("password");
        }
        //Instancia del usuario de firebase y el listener
        mAuth = FirebaseAuth.getInstance();
        Listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null){
                    if (user.isEmailVerified()){
                        goMainScreen();
                    }

                }else {
                    goLogInScreen();
                }
            }
        };

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInWitchEmail(emailIntent, passwordIntent);
            }
        });


    }

    private void SignInWitchEmail(final String email, final String password) {

        //Validar si los valores no estan vacios
        if (!email.isEmpty() && !password.isEmpty()){
            final FirebaseUser user = mAuth.getCurrentUser();
            user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (user.isEmailVerified()){
                        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
                        mAuth.signInWithCredential(credential).addOnCompleteListener(EmailVerificationActivity.this,
                                new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), R.string.EmailPasswordIncorrect,
                                                    Toast.LENGTH_LONG).show();
                                        }else {
                                            goMainScreen();
                                        }
                                    }
                                });
/*                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()){
                                            goMainScreen();
                                        }
                                        else {
                                            Toast.makeText(EmailVerificationActivity.this, R.string.EmailPasswordIncorrect,
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });*/
                    }else {
                        Toast.makeText(EmailVerificationActivity.this,R.string.EmailNoVerified, Toast.LENGTH_LONG).show();
                    }
                }
            });

        }else {
            Toast.makeText(EmailVerificationActivity.this, R.string.IntoCredentials, Toast.LENGTH_LONG).show();
        }
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
