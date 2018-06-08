package com.papaprogramador.presidenciales.Vistas.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.Adaptadores.ViewpagerAdapter;

public class ListaCandidatosView extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

	private GoogleApiClient googleApiClient;
	private FirebaseAuth mAuth;
	private FirebaseAuth.AuthStateListener Listener;
	private DrawerLayout drawerLayout;

	TextView Username;
	TextView Useremail;
	ImageView Userimagen;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		drawerLayout = findViewById(R.id.LayoutMain);

		//Referencia al encabezado del menú lateral para pasar los datos de usuario
		View header = ((NavigationView) findViewById(R.id.navview)).getHeaderView(0);
		Username = header.findViewById(R.id.username);
		Useremail = header.findViewById(R.id.email);
		Userimagen = header.findViewById(R.id.profile_image);

		// Adding Toolbar to Main screen
		setToolbar();

		//Agregando tabs a la ventana principal
		TabLayout tabs = findViewById(R.id.tabs);
		tabs.addTab(tabs.newTab().setText(R.string.tabtextCandidatos));
		tabs.addTab(tabs.newTab().setText(R.string.tabtextOpiniones));
		tabs.addTab(tabs.newTab().setText(R.string.tabtextResultados));
		tabs.setTabGravity(tabs.GRAVITY_FILL);

		//Implementación de la vista de páginas
		final ViewPager viewPager = findViewById(R.id.viewpagerMain);
		ViewpagerAdapter viewpagerAdapter = new ViewpagerAdapter(getSupportFragmentManager(), tabs.getTabCount());

		viewPager.setAdapter(viewpagerAdapter);
		viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

		//Listener para el cambio entre TABS
		tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				int position = tab.getPosition();
				viewPager.setCurrentItem(position);
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {

			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {

			}
		});

		Button mBtnLogout = findViewById(R.id.BtnLogout);
		//Coneccion con la api de google para la autenticación
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestProfile()
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
				if (user == null) {
					goLogInScreen();
				} else {
					if (!user.isEmailVerified()) {
						goLogInScreen();
						Toast.makeText(ListaCandidatosView.this, R.string.emailNoVerificado, Toast.LENGTH_LONG).show();

					}else {
						datosUsuario(user);
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

	private void datosUsuario(FirebaseUser user) {
		Username.setText(user.getDisplayName());
		Useremail.setText(user.getEmail());
		//Carga de la imagen del perfil del usuario actual
		Glide.with(this)
				.load(user.getPhotoUrl())
				.placeholder(R.drawable.im_userimgdefault)
				.error(R.drawable.im_userimgdefault)
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.dontAnimate()
				.into(Userimagen);
	}

	//Metodo para cerrar la session completa en firebase, Facebook y Google
	private void CloseFullSession() {
		mAuth.signOut();
		logOut();
		Toast.makeText(ListaCandidatosView.this, R.string.CloseFullSession, Toast.LENGTH_LONG).show();
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
		if (Listener != null) {
			mAuth.removeAuthStateListener(Listener);
		}
	}

	//Método para ir a la ventana de login
	protected void goLogInScreen() {
		Intent intent = new Intent(ListaCandidatosView.this, LoginView.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
				Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	//Sobreescritura de este metodo al implementar GoogleApiClientListener.OnConnectionFailedListener
	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

	}

	private void setToolbar() {//Método que inicializa el toolbar y agrega el menú de hamburguesa
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home);//Asignación del icono de hamburguesa
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Estado activado del menú
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {//Método que permite habrir el menú lateral con el icono de hamburguesa
		switch (item.getItemId()) {
			case android.R.id.home:
				drawerLayout.openDrawer(GravityCompat.START);
		}
		return super.onOptionsItemSelected(item);
	}
}
