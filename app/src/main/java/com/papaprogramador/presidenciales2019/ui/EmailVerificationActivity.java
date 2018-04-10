package com.papaprogramador.presidenciales2019.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.papaprogramador.presidenciales2019.R;
import com.papaprogramador.presidenciales2019.io.Utils.ReferenciasFirebase;
import com.papaprogramador.presidenciales2019.model.Usuario;

public class EmailVerificationActivity extends AppCompatActivity {
    private String emailIntent;
    private String passwordIntent;
    private String usernameIntent;
    private String departamentoIntent;
    private TextView mTextView;
    private Button mButton;
    private String myIMEI;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener Listener;
    private DatabaseReference databaseReference;
    private String firebaseUID;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        //Instancia de elementos de la UI
        mTextView = findViewById(R.id.TextViewConfirmationNotice);
        mButton = findViewById(R.id.EmailVerified);
        progressBar = findViewById(R.id.ProgressBarVerified);


	    obtenerImei();


        //Se rescatan los valores del Inten mediante el Bundle
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            emailIntent = bundle.getString("email");
            passwordIntent = bundle.getString("password");
            usernameIntent = bundle.getString("username");
            departamentoIntent = bundle.getString("departamento");
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
                                    	ProgressBarVisible();
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), R.string.EmailPasswordIncorrect,
                                                    Toast.LENGTH_LONG).show();
                                        }else {
                                        	firebaseUID =  user.getUid();
											RegistrarUsuario(firebaseUID, email, usernameIntent, departamentoIntent, myIMEI);
                                            goMainScreen();
                                        }
                                        ProgressBarGone();
                                    }
                                });
                    }else {
                        Toast.makeText(EmailVerificationActivity.this,R.string.EmailNoVerified, Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }

	private void RegistrarUsuario(String firebaseUID, String email, String usernameIntent, String departamentoIntent, String myIMEI) {
		Usuario usuario = new Usuario(usernameIntent, email, departamentoIntent, myIMEI);

		databaseReference.child(ReferenciasFirebase.NODO_USUARIO).child(firebaseUID).setValue(usuario);
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

    private void ProgressBarVisible(){
	    mTextView.setVisibility(View.GONE);
	    mButton.setVisibility(View.GONE);
	    progressBar.setVisibility(View.VISIBLE);

    }

	private void ProgressBarGone(){
		mTextView.setVisibility(View.VISIBLE);
		mButton.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.GONE);

	}
	//Método para solicitar el permiso de lectura de IMEI en cualquier version android
	public String obtenerImei(){

		if(Build.VERSION.SDK_INT  < Build.VERSION_CODES.M){
			//Menores a Android 6.0
			myIMEI = getIMEI();
			return myIMEI;
		} else {
			// Mayores a Android 6.0
			myIMEI ="";
			if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
					!= PackageManager.PERMISSION_GRANTED) {
				requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},
						225);
				myIMEI ="";
			} else {
				myIMEI = getIMEI();
			}

			return myIMEI;

		}
	}
	//Método que obtiene el IMEI
	private String getIMEI() {

		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		myIMEI = tm.getDeviceId();
		return myIMEI;

	}
}
