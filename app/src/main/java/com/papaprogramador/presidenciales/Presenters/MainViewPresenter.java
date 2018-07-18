package com.papaprogramador.presidenciales.Presenters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.InterfacesMVP.MainViewContrat;
import com.papaprogramador.presidenciales.UseCases.ConnectionCallbackGoogleApiClient;
import com.papaprogramador.presidenciales.UseCases.GoogleApiClientListener;
import com.papaprogramador.presidenciales.Utils.StaticMethods.SharedPreferencesMethods;

public class MainViewPresenter extends MvpBasePresenter<MainViewContrat.View>
		implements MainViewContrat.Presenter {

	private Context context;
	private String string;
	private FirebaseAuth firebaseAuth;
	private GoogleApiClient googleApiClientThis;
	private GoogleApiClient.ConnectionCallbacks connectionCallbacksThis;

	private FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
		@Override
		public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
			ifViewAttached(new ViewAction<MainViewContrat.View>() {
				@Override
				public void run(@NonNull MainViewContrat.View view) {
					FirebaseUser user = firebaseAuth.getCurrentUser();
					if (user != null) {
						view.starNavView();
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

	public MainViewPresenter(Context context, String string) {
		this.context = context;
		this.string = string;
		firebaseAuth = FirebaseAuth.getInstance();
	}

	@Override
	public void setNewFragment(final Fragment fragment, MenuItem item) {
		final CharSequence itemTitle = item.getTitle();

		ifViewAttached(new ViewAction<MainViewContrat.View>() {
			@Override
			public void run(@NonNull MainViewContrat.View view) {
				view.setNewFragment(fragment, itemTitle);
				view.hideMainView(true);
			}
		});
	}

	@Override
	public void goHomeApp(final MenuItem item) {
		ifViewAttached(new ViewAction<MainViewContrat.View>() {
			@Override
			public void run(@NonNull MainViewContrat.View view) {
				view.goHomeApp(item);
				view.removeCurrentFragment();
				view.setTitleActionBar();
				view.hideMainView(false);
			}
		});
	}

	@Override
	public void getShareApp() {

	}

	@Override
	public void goResetPasswordView() {
		final String emailUser = SharedPreferencesMethods.getEmail(context);

		ifViewAttached(new ViewAction<MainViewContrat.View>() {
			@Override
			public void run(@NonNull MainViewContrat.View view) {
				view.goResetPasswordView(emailUser);
			}
		});
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
	public void signOff() {
		FirebaseAuth.getInstance().signOut();

//		getGoogleApiClient(); //TODO: implementacion pendiente, será completamente modificada
	}

	@Override
	public void updatePassword() {

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
				signOff();
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

				ifViewAttached(new ViewAction<MainViewContrat.View>() {
					@Override
					public void run(@NonNull MainViewContrat.View view) {

						if (!status.isSuccess()) {

							view.errorCloseSesion();

						}
					}
				});
			}
		});
	}
}
