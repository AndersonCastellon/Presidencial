package com.papaprogramador.presidenciales.Vistas.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseUser;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.papaprogramador.presidenciales.InterfacesMVP.ListCandidatos;
import com.papaprogramador.presidenciales.Presentadores.ListCandidatosPresenter;
import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.Adaptadores.ViewpagerAdapter;

import java.util.Objects;

public class ListCandidatosView extends MvpActivity<ListCandidatos.Vista, ListCandidatos.Presentador>
		implements TabLayout.OnTabSelectedListener, ListCandidatos.Vista {

	private DrawerLayout drawerLayout;

	private TextView userName;
	private TextView userEmail;
	private ImageView userImg;
	private ViewPager viewPager;

	private Button mBtnLogout;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		onStartView();

		setToolbar();

		setTabs();

		mBtnLogout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getPresenter().closeSesion();
			}
		});
	}

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
	@Override
	public void onStartView() {
		drawerLayout = findViewById(R.id.LayoutMain);

		//Referencia al encabezado del menú lateral para pasar los datos de user
		View header = ((NavigationView) findViewById(R.id.navview)).getHeaderView(0);
		userName = header.findViewById(R.id.username);
		userEmail = header.findViewById(R.id.email);
		userImg = header.findViewById(R.id.profile_image);

		mBtnLogout = findViewById(R.id.BtnLogout);
	}

	@Override
	public void setTabs() {
		//Agregando tabs a la ventana principal
		TabLayout tabs = findViewById(R.id.tabs);
		tabs.addTab(tabs.newTab().setText(R.string.tabtextCandidatos));
		tabs.addTab(tabs.newTab().setText(R.string.tabtextOpiniones));
		tabs.addTab(tabs.newTab().setText(R.string.tabtextResultados));
		tabs.setTabGravity(TabLayout.GRAVITY_FILL);

		tabs.addOnTabSelectedListener(this);

		//Implementación de la vista de páginas
		viewPager = findViewById(R.id.viewpagerMain);
		ViewpagerAdapter viewpagerAdapter = new ViewpagerAdapter(getSupportFragmentManager(),
				tabs.getTabCount());

		viewPager.setAdapter(viewpagerAdapter);
		viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
	}

	@Override
	public void getDataUser(FirebaseUser user) {

		userName.setText(user.getDisplayName());
		userEmail.setText(user.getEmail());
		//Carga de la imagen del perfil del user actual
		Glide.with(this)
				.load(user.getPhotoUrl())
				.placeholder(R.drawable.im_userimgdefault)
				.error(R.drawable.im_userimgdefault)
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.dontAnimate()
				.into(userImg);
	}

	@Override
	public void goLoginView() {
		Toast.makeText(ListCandidatosView.this,
				R.string.CloseFullSession, Toast.LENGTH_LONG).show();

		Intent intent = new Intent(ListCandidatosView.this, LoginView.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
				Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);

	}

	@Override
	public void errorCloseSesion() {
		Snackbar.make(mBtnLogout, getResources().getString(R.string.errorThis),
				Snackbar.LENGTH_LONG).show();
	}

	@NonNull
	@Override
	public ListCandidatos.Presentador createPresenter() {
		return new ListCandidatosPresenter(this,getResources()
				.getString(R.string.default_web_client_id));
	}

	@Override
	public void setToolbar() {
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		Objects.requireNonNull(getSupportActionBar())
				.setHomeAsUpIndicator(R.drawable.ic_home);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				drawerLayout.openDrawer(GravityCompat.START);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onStart() {
		super.onStart();
		getPresenter().setAuthListener();
	}

	@Override
	protected void onStop() {
		super.onStop();
		getPresenter().removeAuthListener();
	}
}
