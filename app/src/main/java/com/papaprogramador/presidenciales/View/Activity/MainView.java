package com.papaprogramador.presidenciales.View.Activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
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
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseUser;
import com.hannesdorfmann.mosby3.mvp.viewstate.MvpViewStateActivity;
import com.papaprogramador.presidenciales.Adapters.ViewPageAdapter;
import com.papaprogramador.presidenciales.InterfacesMVP.MainViewContrat;
import com.papaprogramador.presidenciales.Presenters.MainViewPresenter;
import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.Views.DeleteAccount.DeleteAccountView;
import com.papaprogramador.presidenciales.Views.SuggestionsAndErrors.SuggestionsAndErrorsView;
import com.papaprogramador.presidenciales.Views.UpdatePassword.UpdatePasswordView;
import com.papaprogramador.presidenciales.Utils.Constans;
import com.papaprogramador.presidenciales.Utils.ViewStateActivity.CustomViewStateActivity;
import com.papaprogramador.presidenciales.View.Fragments.DialogFragment.DialogOk;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainView extends MvpViewStateActivity<MainViewContrat.View, MainViewContrat.Presenter, CustomViewStateActivity>
		implements TabLayout.OnTabSelectedListener, MainViewContrat.View, DialogOk.DialogOkListener {

	@BindView(R.id.LayoutMain)
	DrawerLayout drawerLayout;
	@BindView(R.id.navview)
	NavigationView navigationView;
	@BindView(R.id.layout_view_pager)
	LinearLayout layoutViewPager;
	@BindView(R.id.options_menu)
	FrameLayout optionsMenu;

	Unbinder unbinder;
	@BindView(R.id.tabs)
	TabLayout tabs;
	@BindView(R.id.viewPageMain)
	ViewPager viewPage;

	private TextView userName;
	private TextView userEmail;
	private ImageView userImg;
	private int position;
	private ViewPageAdapter viewpageAdapter;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		unbinder = ButterKnife.bind(this);

		viewpageAdapter = new ViewPageAdapter(getSupportFragmentManager(), 2);
		viewPage.setAdapter(viewpageAdapter);

		tabs.setupWithViewPager(viewPage);

		tabs.removeAllTabs();

		tabs.addTab(tabs.newTab().setText(R.string.tabtextCandidatos));
		tabs.addTab(tabs.newTab().setText(R.string.tabtextOpiniones));

		tabs.setTabGravity(TabLayout.GRAVITY_FILL);

		tabs.addOnTabSelectedListener(this);

		Objects.requireNonNull(tabs.getTabAt(getPresenter().getTabPosition())).select();
		viewPage.setCurrentItem(getPresenter().getTabPosition());

		viewPage.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
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
						fragment = new UpdatePasswordView();
						fragmentTransaction = true;
						break;
					case R.id.delete_account:
						fragment = new DeleteAccountView();
						fragmentTransaction = true;
						break;
					case R.id.suggestions:
						fragment = new SuggestionsAndErrorsView();
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
		position = tab.getPosition();
		getPresenter().setTabPosition(position);
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

		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.options_menu, fragment)
				.commit();

		Objects.requireNonNull(getSupportActionBar()).setTitle(itemTitle);
	}

	@Override
	public void goHomeApp(MenuItem item) {

		if (item != null) {
			item.setChecked(true);
		}
	}

	@Override
	public void setTitleActionBar() {
		try {
			Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.app_name));
		} catch (Resources.NotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void hideMainView(boolean hide) {

		CustomViewStateActivity customViewStateActivity = viewState;

		if (hide) {
			customViewStateActivity.setHideCandidateView(true);
			layoutViewPager.setVisibility(View.GONE);
			optionsMenu.setVisibility(View.VISIBLE);
		} else {
			customViewStateActivity.setHideCandidateView(false);
			layoutViewPager.setVisibility(View.VISIBLE);
			optionsMenu.setVisibility(View.GONE);
		}
	}

	@Override
	public void starNavView() {

		//Referencia al encabezado del menú lateral para pasar los datos de user
		View header = ((NavigationView) findViewById(R.id.navview)).getHeaderView(0);
		userName = header.findViewById(R.id.username);
		userEmail = header.findViewById(R.id.email);
		userImg = header.findViewById(R.id.profile_image);
	}

	@Override
	public void getDataUser(FirebaseUser user) {

		userName.setText(user.getDisplayName());
		userEmail.setText(user.getEmail());

		RequestOptions options = new RequestOptions()
				.centerCrop()
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.centerCrop()
				.dontAnimate();

		Glide.with(this)
				.load(user.getPhotoUrl())
				.apply(options)
				.into(userImg);
	}

	@Override
	public void shareApp() {
		String message = getResources().getString(R.string.share_app_text);
		String URL = getResources().getString(R.string.url_download_app);

		Intent emailIntent = new Intent(Intent.ACTION_SEND);

		emailIntent.setType("text/plain");
		emailIntent.putExtra(Intent.EXTRA_TEXT, message + " " + URL);

		String shooserTitle = getResources().getString(R.string.share_app);

		try {
			startActivity(Intent.createChooser(emailIntent, shooserTitle));
		} catch (ActivityNotFoundException e) {
			Snackbar.make(drawerLayout, getResources().getString(R.string.error), Snackbar.LENGTH_LONG)
					.show();
		}
	}

	@Override
	public void goLoginView() {

		Intent intent = new Intent(MainView.this, LoginView.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
				Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);

	}

	@Override
	public void goResetPasswordView(String emailUser) {

		Intent intent = new Intent(this, ResetPasswordView.class);
		intent.putExtra(Constans.PUT_EMAIL_USUARIO, emailUser);
		startActivityForResult(intent, Constans.REQUEST_CODE_RESET_PASSWORD_VIEW);
	}

	@Override
	public void errorCloseSesion() {
		Snackbar.make(drawerLayout, getResources().getString(R.string.error),
				Snackbar.LENGTH_LONG).show();
	}

	@Override
	public void removeCurrentFragment() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		Fragment currentFragment = fragmentManager.findFragmentById(R.id.options_menu);

		if (currentFragment != null) {
			fragmentManager.beginTransaction().remove(currentFragment).commit();
		}
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

	@NonNull
	@Override
	public CustomViewStateActivity createViewState() {
		return new CustomViewStateActivity();
	}

	@Override
	public void onResultDialogOk(int requestCode) {

		switch (requestCode) {
			case Constans.DIALOG_OK_SUCCESSFUL_CODE:
				removeCurrentFragment();
				setTitleActionBar();
				hideMainView(false);
				break;
			case Constans.CURRENT_PASSWORD_IS_NULL:
				getPresenter().goResetPasswordView();
				break;
		}
	}

	@Override
	public void onNewViewStateInstance() {
		hideMainView(false);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case Constans.REQUEST_CODE_RESET_PASSWORD_VIEW:
					onResultDialogOk(Constans.DIALOG_OK_SUCCESSFUL_CODE);
					break;
			}
		}
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
