package com.papaprogramador.presidenciales.View.Actividades;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.Utils.Constans;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DetailCandidatoView extends AppCompatActivity {

	@BindView(R.id.CandidateImg)
	ImageView imgCandidate;
	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@BindView(R.id.webViewDetailCandidate)
	WebView webViewDetailCandidate;

	Unbinder unbinder;

	private String idCandidate;
	private String nameCandidate;
	private String urlImgCandidate;
	private String urlHtmlCandidate;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle_candidato);

		unbinder = ButterKnife.bind(this);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			idCandidate = bundle.getString(Constans.PUT_ID_CANDIDATE);
			nameCandidate = bundle.getString(Constans.PUT_NOMBRE_CANDIDATE);
			urlImgCandidate = bundle.getString(Constans.PUT_URL_IMAGEN_CANDIDATE);
			urlHtmlCandidate = bundle.getString(Constans.PUT_URL_HTML_CANDIDATE);
		}

		webViewDetailCandidate.getSettings().setJavaScriptEnabled(true);
		webViewDetailCandidate.getSettings().setDomStorageEnabled(true);
		webViewDetailCandidate.getSettings().setBuiltInZoomControls(true);
		webViewDetailCandidate.getSettings().setLoadWithOverviewMode(true);
		webViewDetailCandidate.getSettings().setLoadsImagesAutomatically(true);
		webViewDetailCandidate.loadUrl(urlHtmlCandidate);

		getToolbar();
		recuperarImagenCandidato(urlImgCandidate);


	}

	private void recuperarImagenCandidato(String urlImagen) {

		//TODO: Implementar placeholder para Glide
		Glide.with(this)
				.load(urlImagen)
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.into(imgCandidate);
	}

	private void getToolbar() {

		setSupportActionBar(toolbar);

		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(nameCandidate);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbinder.unbind();
	}
}
