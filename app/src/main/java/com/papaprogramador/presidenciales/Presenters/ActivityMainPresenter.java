package com.papaprogramador.presidenciales.Presenters;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.InterfacesMVP.MainView;
import com.papaprogramador.presidenciales.Modelos.ConnectionCallbackGoogleApiClient;
import com.papaprogramador.presidenciales.Modelos.GoogleApiClientListener;

public class ActivityMainPresenter extends MvpBasePresenter<MainView.View>
		implements MainView.Presenter {

	private Context context;
	private String string;
	private FirebaseAuth firebaseAuth;
	private GoogleApiClient googleApiClientThis;
	private GoogleApiClient.ConnectionCallbacks connectionCallbacksThis;

	private FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
		@Override
		public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
			ifViewAttached(new ViewAction<MainView.View>() {
				@Override
				public void run(@NonNull MainView.View view) {
					FirebaseUser user = firebaseAuth.getCurrentUser();
					if (user != null) {
						view.onStartView();
						view.setToolbar();
						view.setTabs();
						view.getDataUser(user);
					} else {
						view.goLoginView();
					}
				}
			});
		}
	};

	public ActivityMainPresenter(Context context, String string) {
		this.context = context;
		this.string = string;
		firebaseAuth = FirebaseAuth.getInstance();
	}

	@Override
	public void setAuthListener() {
		firebaseAuth.addAuthStateListener(authStateListener);
	}

	@Override
	public void removeAuthListener() {
		firebaseAuth.removeAuthStateListener(authStateListener);
	}

	@Override
	public void closeSesion() {
		FirebaseAuth.getInstance().signOut();

//		getGoogleApiClient(); //TODO: implementacion pendiente, será completamente modificada
	}

	@Override
	public void getGoogleApiClient() {
		new GoogleApiClientListener(context, string, new GoogleApiClientListener.GoogleApi() {
			@Override
			public void apiClient(GoogleApiClient googleApiClient) {
				googleApiClientThis = googleApiClient;
				getConnectionCallbacks();
			}
		});
	}

	@Override
	public void getConnectionCallbacks() {
		new ConnectionCallbackGoogleApiClient(new ConnectionCallbackGoogleApiClient.Callback() {
			@Override
			public void callbackGoogleApliClientConnection(GoogleApiClient.ConnectionCallbacks connectionCallbacks) {
				connectionCallbacksThis = connectionCallbacks;
				closeSesion();
			}
		});
	}

	@Override
	public void closeSesionGoogle() {

		if (!googleApiClientThis.isConnected()) {

			googleApiClientThis.connect();
			googleApiClientThis.registerConnectionCallbacks(connectionCallbacksThis);

		}

		if (connectionCallbacksThis != null) {

			googleApiClientThis.unregisterConnectionCallbacks(connectionCallbacksThis);

		}

		Auth.GoogleSignInApi.signOut(googleApiClientThis).setResultCallback(new ResultCallback<Status>() {
			@Override
			public void onResult(@NonNull final Status status) {

				ifViewAttached(new ViewAction<MainView.View>() {
					@Override
					public void run(@NonNull MainView.View view) {

						if (!status.isSuccess()) {

							view.errorCloseSesion();

						}
					}
				});
			}
		});
	}
}
