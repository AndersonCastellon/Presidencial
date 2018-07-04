package com.papaprogramador.presidenciales.UseCases;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;

public class ConnectionCallbackGoogleApiClient {

	public interface Callback {
		void callbackGoogleApliClientConnection(GoogleApiClient.ConnectionCallbacks connectionCallbacks);
	}

	private Callback listener;

	public ConnectionCallbackGoogleApiClient(Callback listener) {
		this.listener = listener;
		connectionCallback();
	}

	private void connectionCallback() {
		GoogleApiClient.ConnectionCallbacks connectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {
			@Override
			public void onConnected(@Nullable Bundle bundle) {

			}

			@Override
			public void onConnectionSuspended(int i) {

			}
		};

		listener.callbackGoogleApliClientConnection(connectionCallbacks);
	}
}
