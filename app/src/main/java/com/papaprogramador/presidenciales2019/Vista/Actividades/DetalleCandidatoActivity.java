package com.papaprogramador.presidenciales2019.Vista.Actividades;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.papaprogramador.presidenciales2019.R;
import com.papaprogramador.presidenciales2019.io.Utils.Constantes;

public class DetalleCandidatoActivity extends AppCompatActivity {

	private String idCandidato;
	private String nombreCandidato;
	private String urlImagen;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle_candidato);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);


		Bundle idCandidato = getIntent().getExtras();
		if (idCandidato != null) {
			this.idCandidato = idCandidato.getString(Constantes.ID_CANDIDATO);
			nombreCandidato = idCandidato.getString(Constantes.NOMBRE_CANDIDATO);
			urlImagen = idCandidato.getString(Constantes.URL_IMAGEN_CANDIDATO);
		}

		getToolbar();
		recuperarImagenCandidato(urlImagen);


	}

	private void recuperarImagenCandidato(String urlImagen) {

		FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
		StorageReference storageReference = firebaseStorage.getReferenceFromUrl(urlImagen);

		ImageView ImagenCandidato = findViewById(R.id.imagen_candidato);

		//Obteniendo la imagen con Glide, mucho más optimo
		//TODO: Implementar placeholder para Glide
		Glide.with(this)
				.using(new FirebaseImageLoader())
				.load(storageReference)
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.into(ImagenCandidato);
	}

	private void getToolbar() {//Método que inicializa el toolbar y agrega el menú de hamburguesa
		Toolbar toolbar = findViewById(R.id.toolbar);

		setSupportActionBar(toolbar);

		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);// Activar flecha atras
		getSupportActionBar().setTitle(nombreCandidato);
	}
}
