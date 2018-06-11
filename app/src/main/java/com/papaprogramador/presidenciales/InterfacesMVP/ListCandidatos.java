package com.papaprogramador.presidenciales.InterfacesMVP;

import com.google.firebase.auth.FirebaseUser;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface ListCandidatos {
	interface View extends MvpView {
		void onStartView();
		void setToolbar();
		void setTabs();
		void getDataUser(FirebaseUser user);
		void goLoginView();
		void errorCloseSesion();
	}

	interface Presenter extends MvpPresenter<View> {
		void setAuthListener();
		void removeAuthListener();
		void closeSesion();
		void getGoogleApiClient();
		void getConnectionCallbacks();
		void closeSesionGoogle();
	}
}
