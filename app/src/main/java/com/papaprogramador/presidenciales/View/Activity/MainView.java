package com.papaprogramador.presidenciales.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseUser;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.papaprogramador.presidenciales.Adapters.ViewPageAdapter;
import com.papaprogramador.presidenciales.InterfacesMVP.MainViewContrat;
import com.papaprogramador.presidenciales.Presenters.MainViewPresenter;
import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.View.Fragments.DeleteAccountFragment;
import com.papaprogramador.presidenciales.View.Fragments.SuggestionsAndErrorsFragment;
import com.papaprogramador.presidenciales.View.Fragments.UpdatePasswordFragment;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainView extends MvpActivity<MainViewContrat.View, MainViewContrat.Presenter>
		implements TabLayout.OnTabSelectedListener, MainViewContrat.View {

	@BindView(R.id.LayoutMain)
	DrawerLayout drawerLayout;
	@BindView(R.id.navview)
	NavigationView navview;
	@BindView(R.id.layout_view_pager)
	LinearLayout layoutViewPager;
	@BindView(R.id.options_menu)
	FrameLayout optionsMenu;

	Unbinder unbinder;

	private TextView userName;
	private TextView userEmail;
	private ImageView userImg;
	private ViewPager viewPage;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		unbinder = ButterKnife.bind(this);

		navview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem item) {

				boolean fragmentTransaction = false;
				Fragment fragment = null;

				boolean goHomeApp = false;

				switch (item.getItemId()) {
					case R.id.home_app:
						goHomeApp = true;
						break;
					case R.id.sign_off:
						getPresenter().signOff();
						break;
					case R.id.update_pass:
						fragment = new UpdatePasswordFragment();
						fragmentTransaction = true;
						break;
					case R.id.delete_account:
						fragment = new DeleteAccountFragment();
						fragmentTransaction = true;
						break;
					case R.id.suggestions:
						fragment = new SuggestionsAndErrorsFragment();
						fragmentTransaction = true;
						break;
					case R.id.share_app:
						getPresenter().getShareApp();
						break;
				}

				if (fragmentTransaction) {
					getPresenter().setNewFragment(fragment, item);
					drawerLayout.closeDrawers();
				}

				if (goHomeApp) {
					getPresenter().goHomeApp(item);
					drawerLayout.closeDrawers();
				}
				return true;
			}
		});
	}

	@Override
	public void onTabSelected(TabLayout.Tab tab) {
		int position = tab.getPosition();
		viewPage.setCurrentItem(position);
	}

	@Override
	public void onTabUnselected(TabLayout.Tab tab) {

	}

	@Override
	public void onTabReselected(TabLayout.Tab tab) {

	}

	@Override
	public void setNewFragment(Fragment fragment, CharSequence itemTitle) {

		if (layoutViewPager.getVisibility() == View.VISIBLE) {
			layoutViewPager.setVisibility(View.GONE);
		}

		if (optionsMenu.getVisibility() == View.GONE) {
			optionsMenu.setVisibility(View.VISIBLE);
		}

		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.options_menu, fragment)
				.commit();

		getSupportActionBar().setTitle(itemTitle);
	}

	@Override
	public void goHomeApp(MenuItem item) {

		FragmentManager fragmentManager = getSupportFragmentManager();
		Fragment currentFragment = fragmentManager.findFragmentById(R.id.options_menu);

		if (currentFragment != null) {
			fragmentManager.beginTransaction().remove(currentFragment).commit();
		}

		if (optionsMenu.getVisibility() == View.VISIBLE) {
			optionsMenu.setVisibility(View.GONE);
		}

		if (layoutViewPager.getVisibility() == View.GONE) {
			layoutViewPager.setVisibility(View.VISIBLE);
		}

		item.setChecked(true);

		getSupportActionBar().setTitle(getResources().getString(R.string.app_name));

	}

	@Override
	public void onStartView() {

		//Referencia al encabezado del menú lateral para pasar los datos de user
		View header = ((NavigationView) findViewById(R.id.navview)).getHeaderView(0);
		userName = header.findViewById(R.id.username);
		userEmail = header.findViewById(R.id.email);
		userImg = header.findViewById(R.id.profile_image);
	}

	@Override
	public void setTabs() {
		//Agregando tabs a la ventana principal
		TabLayout tabs = findViewById(R.id.tabs);

		tabs.removeAllTabs();

		tabs.addTab(tabs.newTab().setText(R.string.tabtextCandidatos));
		tabs.addTab(tabs.newTab().setText(R.string.tabtextOpiniones));
		tabs.addTab(tabs.newTab().setText(R.string.tabtextResultados));

		tabs.setTabGravity(TabLayout.GRAVITY_FILL);

		tabs.addOnTabSelectedListener(this);

		viewPage = findViewById(R.id.viewPageMain);
		ViewPageAdapter viewpageAdapter = new ViewPageAdapter(getSupportFragmentManager(),
				tabs.getTabCount());

		viewPage.setAdapter(viewpageAdapter);
		viewPage.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
	}

	@Override
	public void getDataUser(FirebaseUser user) {

		userName.setText(user.getDisplayName());
		userEmail.setText(user.getEmail());

		Glide.with(this)
				.load(user.getPhotoUrl())
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.dontAnimate()
				.into(userImg);
	}

	@Override
	public void shareApp() {

	}

	@Override
	public void goLoginView() {

		Intent intent = new Intent(MainView.this, LoginView.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
				Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);

	}

	@Override
	public void errorCloseSesion() {
		Snackbar.make(drawerLayout, getResources().getString(R.string.errorThis),
				Snackbar.LENGTH_LONG).show();
	}

	@NonNull
	@Override
	public MainViewContrat.Presenter createPresenter() {
		return new MainViewPresenter(this, getResources()
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbinder.unbind();
	}
}
