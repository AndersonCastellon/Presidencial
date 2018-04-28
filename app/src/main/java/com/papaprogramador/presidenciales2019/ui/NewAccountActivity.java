package com.papaprogramador.presidenciales2019.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.papaprogramador.presidenciales2019.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

public class NewAccountActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextInputEditText EditTextEmail, EditTextPassword, EditTextUserName;
    private Button btnNewAccount;
    private ProgressBar mProgressBar;
    private String UserName;
    private MaterialBetterSpinner spinnerDep;
    private String Departamento;
    private LinearLayout contenido;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        EditTextEmail = findViewById(R.id.editTexEmailCreate);
        EditTextPassword = findViewById(R.id.editTextPasswordCreate);
        btnNewAccount = findViewById(R.id.btnNewAccountCreate);
        mProgressBar = findViewById(R.id.ProgressBarNewAccount);
        EditTextUserName = findViewById(R.id.editTexUserName);
		spinnerDep = findViewById(R.id.spinnerDep);
		contenido = findViewById(R.id.contenido);

        mAuth = FirebaseAuth.getInstance();

		//Implementación de un Spinner estilo material design
	    String[] departamento = {"Ahuachapán","Cabañas","Chalatenango","Cuscatlán","La Libertad","La Paz",
	    "La Unión","Morazán","San Miguel","San Salvador","San Vicente","Santa Ana","Sonsonate","Usulután"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, departamento);
        spinnerDep.setAdapter(arrayAdapter);

        spinnerDep.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Departamento = spinnerDep.getText().toString();
	        }
        });


	    btnNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserName = EditTextUserName.getText().toString();
                if (!UserName.isEmpty()){
                    CreateNewAccount();
                }else{
                    Toast.makeText(NewAccountActivity.this, R.string.CreateUserName, Toast.LENGTH_LONG).show();
                }
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
        intent.putExtra("username", UserName);
        intent.putExtra("departamento", Departamento);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void ProgressStatusVisible(){

        mProgressBar.setVisibility(View.VISIBLE);
		contenido.setVisibility(View.GONE);

    }
    private void ProgressStatusGone(){

	    mProgressBar.setVisibility(View.GONE);
	    contenido.setVisibility(View.VISIBLE);
    }
}
