package com.papaprogramador.presidenciales.Presenters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseUser;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.common.pojo.User;
import com.papaprogramador.presidenciales.UseCases.ValidarEmail;
import com.papaprogramador.presidenciales.InterfacesMVP.LoginContract;
import com.papaprogramador.presidenciales.UseCases.GoogleApiClientListener;
import com.papaprogramador.presidenciales.UseCases.SignInWithCredentials;
import com.papaprogramador.presidenciales.UseCases.GetIdDevice;
import com.papaprogramador.presidenciales.UseCases.ObtenerIdFirebase;
import com.papaprogramador.presidenciales.UseCases.RegistrarUsuarioRTDB;
import com.papaprogramador.presidenciales.Utils.Constans;
import com.papaprogramador.presidenciales.Utils.StaticMethods.GetPermissions;
import com.papaprogramador.presidenciales.Utils.StaticMethods.SharedPreferencesMethods;

import java.util.Objects;

public class LoginPresenter extends MvpBasePresenter<LoginContract.View> implements LoginContract.Presenter {

	private String idDevice;
	private GoogleApiClient apiClient;
	private Context context;

	private boolean bool = true;

	public LoginPresenter(Context context) {
		this.context = context;
	}

	@Override
	public void getIdDevice(String permission, int requestPermission) {
		if (GetPermissions.checkPermissionToApp(context, permission, requestPermission)) {
			idDevice = GetIdDevice.getIdDevice(context);
		}
	}

	@Override
	public void getIdFirebase() {
		new ObtenerIdFirebase(idDevice,
				new ObtenerIdFirebase.IdObtenido() {
					@Override
					public void idObtenido(final boolean bool, final String idFirebase) {
						ifViewAttached(new ViewAction<LoginContract.View>() {
							@Override
							public void run(@NonNull LoginContract.View view) {
								if (bool) {
									view.idYaUtilizado();
								} else {
									view.goNewAccountView(idDevice);
								}
							}
						});

					}
				});
	}


	public void logInWithEmailAndPassword(final Context context, final String emailUsuario, final String pass) {

		ifViewAttached(new ViewAction<LoginContract.View>() {
			@Override
			public void run(@NonNull final LoginContract.View view) {

				if (emailUsuario.isEmpty()) {
					view.emailUserEmpty();
					bool = false;
				} else {
					new ValidarEmail(emailUsuario, new ValidarEmail.EmailValidado() {
						@Override
						public void emailEsValido(Boolean esValido) {
							if (!esValido) {
								bool = false;
								view.emailNoValid();
							}
						}
					});
				}

				if (pass.isEmpty()) {
					view.passEmpty();
					bool = false;
				}

				if (bool) {
					view.showProgressBar(true);
					new SignInWithCredentials(context, emailUsuario, pass,
							new SignInWithCredentials.SignIn() {
								@Override
								public void onResult(final String resultado, FirebaseUser user) {
									switch (resultado) {
										case Constans.RESULT_IS_SUCCESSFUL:
											SharedPreferencesMethods.saveEmailAndPassword(context,
													emailUsuario, pass);
											view.goListaCandidatosView();
											break;
										case Constans.RESULT_EMAIL_NO_VERIFY:
											view.showProgressBar(false);
											view.emailUserNoVerify();
											break;
										case Constans.RESULT_NO_SUCCESSFUL:
											view.showProgressBar(false);
											view.noValidCredencials();
											break;
									}
								}
							});
				}
			}
		});
	}

	@Override
	public void obtenerGoogleApliClient(final Context context, final String string) {
		ifViewAttached(new ViewAction<LoginContract.View>() {
			@Override
			public void run(@NonNull final LoginContract.View view) {
				new GoogleApiClientListener(context, string, new GoogleApiClientListener.GoogleApi() {
					@Override
					public void apiClient(GoogleApiClient googleApiClient) {
						apiClient = googleApiClient;
						view.intentGoogle(apiClient);
					}
				});
			}
		});
	}

	@Override
	public void activityResetPassword() {
		ifViewAttached(new ViewAction<LoginContract.View>() {
			@Override
			public void run(@NonNull LoginContract.View view) {
				view.goResetPasswordView();
			}
		});
	}

	@Override
	public void googleSingInFromResult(final Intent data) {
		ifViewAttached(new ViewAction<LoginContract.View>() {
			@Override
			public void run(@NonNull LoginContract.View view) {
				view.showProgressBar(true);
				GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
				resultGoogle(result);
			}
		});
	}

	@Override
	public void resultGoogle(final GoogleSignInResult result) {
		ifViewAttached(new ViewAction<LoginContract.View>() {
			@Override
			public void run(@NonNull final LoginContract.View view) {
				if (result.isSuccess()) {
					validarDispositivoConCuentaGoogle(result.getSignInAccount());
				} else {
					apiClient.stopAutoManage((FragmentActivity) context);
					apiClient.disconnect();
					view.showProgressBar(false);
					view.errorSigInGoogle();
				}
			}
		});

	}

	@Override
	public void validarDispositivoConCuentaGoogle(final GoogleSignInAccount signInAccount) {

		ifViewAttached(new ViewAction<LoginContract.View>() {
			@Override
			public void run(@NonNull final LoginContract.View view) {

				String emailUsuario = signInAccount.getEmail();

				new ObtenerIdFirebase(idDevice, emailUsuario, new ObtenerIdFirebase.IdObtenido() {
					@Override
					public void idObtenido(boolean bool, String idFirebase) {
						if (bool) {
							iniciarSesionConGoogle(context, signInAccount);
						} else {
							view.showProgressBar(false);
							view.idYaUtilizado();
						}
					}
				});
			}
		});
	}

	@Override
	public void iniciarSesionConGoogle(final Context context, final GoogleSignInAccount signInAccount) {

		ifViewAttached(new ViewAction<LoginContract.View>() {
			@Override
			public void run(@NonNull final LoginContract.View view) {
				new SignInWithCredentials(context, signInAccount,
						new SignInWithCredentials.SignIn() {
							@Override
							public void onResult(final String resultado,
							                     final FirebaseUser user) {

								switch (resultado) {
									case Constans.RESULT_IS_SUCCESSFUL:
										SharedPreferencesMethods.saveEmailAndPassword(context,
												signInAccount.getEmail(), null);
										registrarUsuarioEnFirebase(user);
										break;
									case Constans.RESULT_NO_SUCCESSFUL:
										view.showProgressBar(false);
										view.errorSigInGoogle();
										break;
								}
							}
						});
			}
		});
	}

	@Override
	public void registrarUsuarioEnFirebase(final FirebaseUser user) {

		ifViewAttached(new ViewAction<LoginContract.View>() {
			@Override
			public void run(@NonNull final LoginContract.View view) {
				String nombreUsuario = user.getDisplayName();
				String emailUsuario = user.getEmail();
				String idDispositivo = idDevice;
				String userUid = user.getUid();
				String uriPhotoProfile = Objects.requireNonNull(user.getPhotoUrl()).toString();

				User user = new User(nombreUsuario, emailUsuario, null, null,
						uriPhotoProfile);

				new RegistrarUsuarioRTDB(userUid, idDispositivo, user, new RegistrarUsuarioRTDB.RegistrarUsuarioFirebase() {
					@Override
					public void registroExitoso(boolean bool) {
						if (bool) {
							view.goListaCandidatosView();
						}
					}
				});
			}
		});
	}
}
