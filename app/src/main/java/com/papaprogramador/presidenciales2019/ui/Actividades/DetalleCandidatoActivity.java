package com.papaprogramador.presidenciales2019.ui.Actividades;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.TextView;

import com.papaprogramador.presidenciales2019.R;

public class DetalleCandidatoActivity extends AppCompatActivity {

	private String IdCandidato;
	private String NombreCandidato;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle_candidato);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);


		Bundle idcandidato = getIntent().getExtras();
		if (idcandidato != null) {
			IdCandidato = idcandidato.getString("idcandidato");
			NombreCandidato = idcandidato.getString("nombrecandidato");
		}

		setToolbar();


	}

	private void setToolbar() {//Método que inicializa el toolbar y agrega el menú de hamburguesa
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);// Activar flecha atras
		getSupportActionBar().setTitle(NombreCandidato);
	}
}
