package com.papaprogramador.presidenciales2019.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

public class NewAccountActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
//    private FirebaseAuth.AuthStateListener Listener;

    private EditText EditTextEmail, EditTextPassword, EditTextUserName;
    private Button btnNewAccount;
    private ProgressBar mProgressBar;
    private String UserName;
    private MaterialBetterSpinner spinnerDep;
    private String Departamento;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        EditTextEmail = findViewById(R.id.editTexEmailCreate);
        EditTextPassword = findViewById(R.id.editTextPasswordCreate);
        btnNewAccount = findViewById(R.id.btnNewAccountCreate);
        mProgressBar = findViewById(R.id.ProgressBarNewAccount);
        EditTextUserName = findViewById(R.id.editTexUserName);
		spinnerDep = findViewById(R.id.spinnerDep);

        mAuth = FirebaseAuth.getInstance();

		//Implementación de un Spinner estilo material design
	    String[] departamento = {"Ahuachapán","Cabañas","Chalatenango","Cuscatlán","La Libertad","La Paz",
	    "La Unión","Morazán","San Miguel","San Salvador","San Vicente","Santa Ana","Sonsonate","Usulután"};
//	    spinnerDep.setAdapter(new ArrayAdapter<>(NewAccountActivity.this
//			    , android.R.layout.simple_spinner_item, departamento));
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

        EditTextEmail.setVisibility(View.GONE);
        EditTextPassword.setVisibility(View.GONE);
        btnNewAccount.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
	    EditTextUserName.setVisibility(View.GONE);
	    spinnerDep.setVisibility(View.GONE);
	    TextInputLayout textInputLayout1 = findViewById(R.id.textinput1);
	    TextInputLayout textInputLayout2 = findViewById(R.id.textinput2);
	    TextInputLayout textInputLayout3 = findViewById(R.id.textinput3);
	    textInputLayout1.setVisibility(View.GONE);
	    textInputLayout2.setVisibility(View.GONE);
	    textInputLayout3.setVisibility(View.GONE);


    }
    private void ProgressStatusGone(){

        EditTextEmail.setVisibility(View.VISIBLE);
        EditTextPassword.setVisibility(View.VISIBLE);
        btnNewAccount.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        EditTextUserName.setVisibility(View.VISIBLE);
	    spinnerDep.setVisibility(View.VISIBLE);
	    TextInputLayout textInputLayout1 = findViewById(R.id.textinput1);
	    TextInputLayout textInputLayout2 = findViewById(R.id.textinput2);
	    TextInputLayout textInputLayout3 = findViewById(R.id.textinput3);
	    textInputLayout1.setVisibility(View.VISIBLE);
	    textInputLayout2.setVisibility(View.VISIBLE);
	    textInputLayout3.setVisibility(View.VISIBLE);
    }
}
