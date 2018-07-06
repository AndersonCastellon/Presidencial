package com.papaprogramador.presidenciales.InterfacesMVP;

import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseUser;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface MainViewContrat {
	interface View extends MvpView {
		void setNewFragment(Fragment fragment, CharSequence itemTitle);
		void goHomeApp(MenuItem item);
		void hideMainView(boolean hide);
		void starNavView();
		void setToolbar();
		void setTabs();
		void getDataUser(FirebaseUser user);
		void shareApp();
		void goLoginView();
		void errorCloseSesion();
	}

	interface Presenter extends MvpPresenter<View> {
		void setNewFragment(Fragment fragment, MenuItem item);
		void goHomeApp(MenuItem item);
		void getShareApp();
		void setAuthListener();
		void removeAuthListener();
		void signOff();
		void updatePassword();
		void getGoogleApiClient();
		void getConnectionCallbacks();
		void closeSesionGoogle();
	}
}
