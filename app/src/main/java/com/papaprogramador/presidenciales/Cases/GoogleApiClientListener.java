package com.papaprogramador.presidenciales.Cases;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class GoogleApiClientListener implements com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener {

	public interface GoogleApi{
		void apiClient(GoogleApiClient  googleApiClient);
	}

	private GoogleApi listener;

	public GoogleApiClientListener(Context context, String string, GoogleApi listener) {
		this.listener = listener;
		obtenerApiClient(context, string);
	}

	private void obtenerApiClient(Context context, String string) {

		GoogleApiClient googleApiClient;

		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(string)
				.requestProfile()
				.requestEmail()
				.build();

		googleApiClient = new GoogleApiClient.Builder(context)
				.enableAutoManage((FragmentActivity) context, this)
				.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
				.build();

		listener.apiClient(googleApiClient);
	}


	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

	}
}
