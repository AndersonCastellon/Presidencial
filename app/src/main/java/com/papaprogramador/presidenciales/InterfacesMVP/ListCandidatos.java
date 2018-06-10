package com.papaprogramador.presidenciales.InterfacesMVP;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseUser;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface ListCandidatos {
	interface Vista extends MvpView {
		void onStartView();
		void setToolbar();
		void setTabs();
		void getDataUser(FirebaseUser user);
		void goLoginView();
		void errorCloseSesion();
	}

	interface Presentador extends MvpPresenter<ListCandidatos.Vista> {
		void setAuthListener();
		void removeAuthListener();
		void closeSesion();
		void closeSesionFirebase();
		void getGoogleApiClient();
		void verifyGoogleApliClient(GoogleApiClient googleApiClient);
		void closeSesionGoogle(GoogleApiClient.ConnectionCallbacks connectionCallbacks,
		                       GoogleApiClient googleApiClient);
	}
}
