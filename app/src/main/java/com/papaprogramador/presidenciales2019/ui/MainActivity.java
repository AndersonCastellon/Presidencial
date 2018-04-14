package com.papaprogramador.presidenciales2019.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.papaprogramador.presidenciales2019.R;
import com.papaprogramador.presidenciales2019.io.Utils.ReferenciasFirebase;
import com.papaprogramador.presidenciales2019.model.Candidato;
import com.papaprogramador.presidenciales2019.ui.adapter.CandidatoAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient googleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener Listener;
    RecyclerView recyclerView;
    List<Candidato> candidatos;
    CandidatoAdapter candidatoAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();


        //Implementación del Recyclerview
        recyclerView = findViewById(R.id.rv);
	    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);//Creacion del layoutmanager
	    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//Orientacion del layoutmanager
	    recyclerView.setLayoutManager(linearLayoutManager);//asignacion de layoutmanager al recyclerview

	    candidatos = new ArrayList<>();
	    candidatoAdapter = new CandidatoAdapter(candidatos);
	    recyclerView.setAdapter(candidatoAdapter);

	    firebaseDatabase.getReference().child(ReferenciasFirebase.NODO_CANDIDATOS).addValueEventListener(new ValueEventListener() {
		    @Override
		    public void onDataChange(DataSnapshot dataSnapshot) {
		    	candidatos.remove(candidatos);
			    for (DataSnapshot snapshot :
					    dataSnapshot.getChildren()) {
			    	Candidato candidato = snapshot.getValue(Candidato.class);
			    	candidatos.add(candidato);
			    	candidatoAdapter.notifyDataSetChanged();
				    
			    }
		    }

		    @Override
		    public void onCancelled(DatabaseError databaseError) {

		    }
	    });

        Button mBtnLogout = findViewById(R.id.BtnLogout);
        //Coneccion con la api de google para la autenticación
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();
        //Escuchador para validar la session iniciada
        Listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null){
                    goLogInScreen();
                }else {
                    if (!user.isEmailVerified()){
                        goLogInScreen();
                        Toast.makeText(MainActivity.this,R.string.EmailNoVerified, Toast.LENGTH_LONG).show();

                    }
                }
            }
        };

        //Metodo del boton
        mBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloseFullSession();
            }
        });
    }
    //Metodo para cerrar la session completa en firebase, Facebook y Google
    private void CloseFullSession() {
        mAuth.signOut();
        logOut();
        LoginManager.getInstance().logOut();
        Toast.makeText(MainActivity.this, R.string.CloseFullSession, Toast.LENGTH_LONG).show();
        //revoke();
    }
    //Metodo que cierra la session en google
    public void logOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    goLogInScreen();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.not_close_session, Toast.LENGTH_SHORT).show();
                }
            }
        });
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
    //Método para ir a la ventana de login
    protected void goLogInScreen() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //Sobreescritura de este metodo al implementar GoogleApiClient.OnConnectionFailedListener
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
