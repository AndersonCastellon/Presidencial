package com.papaprogramador.presidenciales.InterfacesMVP;

import com.google.firebase.auth.FirebaseUser;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface MainViewContrat {
	interface View extends MvpView {
		void onStartView();
		void setToolbar();
		void setTabs();
		void getDataUser(FirebaseUser user);
		void shareApp();
		void goLoginView();
		void errorCloseSesion();
	}

	interface Presenter extends MvpPresenter<View> {
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
