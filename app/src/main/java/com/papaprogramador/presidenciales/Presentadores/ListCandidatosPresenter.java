package com.papaprogramador.presidenciales.Presentadores;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.InterfacesMVP.ListCandidatos;
import com.papaprogramador.presidenciales.Modelos.ConnectionCallbackGoogleApiClient;
import com.papaprogramador.presidenciales.Modelos.GoogleApiClientListener;

public class ListCandidatosPresenter extends MvpBasePresenter<ListCandidatos.Vista>
		implements ListCandidatos.Presentador {
//TODO: REPLANTEAR LA ESTRUCTURA MVP DE LISTCANDIDATOSPRESENTER- LISTENER FUNCIONANDO
	private Context context;
	private String string;
	private FirebaseAuth firebaseAuth;

	private FirebaseAuth.AuthStateListener listener = new FirebaseAuth.AuthStateListener() {
		@Override
		public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
			ifViewAttached(new ViewAction<ListCandidatos.Vista>() {
				@Override
				public void run(@NonNull ListCandidatos.Vista view) {
					FirebaseUser user = firebaseAuth.getCurrentUser();
					if (user != null){
						view.getDataUser(user);
					} else {
						view.goLoginView();
					}
				}
			});
		}
	};

	public ListCandidatosPresenter(Context context, String string) {
		this.context = context;
		this.string = string;
		firebaseAuth = FirebaseAuth.getInstance();
	}

	@Override
	public void setAuthListener() {
		firebaseAuth.addAuthStateListener(listener);
	}

	@Override
	public void removeAuthListener() {
		firebaseAuth.removeAuthStateListener(listener);
	}

	@Override
	public void closeSesion() {
		closeSesionFirebase();
//		getGoogleApiClient();
	}

	@Override
	public void closeSesionFirebase() {
		FirebaseAuth.getInstance().signOut();
	}

	@Override
	public void getGoogleApiClient() {
		new GoogleApiClientListener(context, string, new GoogleApiClientListener.GoogleApi() {
			@Override
			public void apiClient(GoogleApiClient googleApiClient) {
				verifyGoogleApliClient(googleApiClient);
			}
		});
	}

	@Override
	public void verifyGoogleApliClient(final GoogleApiClient googleApiClient) {
		new ConnectionCallbackGoogleApiClient(new ConnectionCallbackGoogleApiClient.Callback() {
			@Override
			public void callbackGoogleApliClientConnection(GoogleApiClient.ConnectionCallbacks connectionCallbacks) {
				if (!googleApiClient.isConnected()){

					googleApiClient.connect();
					googleApiClient.registerConnectionCallbacks(connectionCallbacks);

					closeSesionGoogle(connectionCallbacks, googleApiClient);
				} else {
					closeSesionGoogle(connectionCallbacks, googleApiClient);
				}
			}
		});
	}

	@Override
	public void closeSesionGoogle(GoogleApiClient.ConnectionCallbacks connectionCallbacks,
	                              GoogleApiClient googleApiClient) {

		if (connectionCallbacks != null){
			googleApiClient.unregisterConnectionCallbacks(connectionCallbacks);
		}

		Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
			@Override
			public void onResult(@NonNull final Status status) {

				ifViewAttached(new ViewAction<ListCandidatos.Vista>() {
					@Override
					public void run(@NonNull ListCandidatos.Vista view) {
						if (status.isSuccess()){
							view.goLoginView();
						} else {
							view.errorCloseSesion();
						}
					}
				});
			}
		});
	}
}
