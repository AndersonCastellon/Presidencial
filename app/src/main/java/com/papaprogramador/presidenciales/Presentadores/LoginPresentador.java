package com.papaprogramador.presidenciales.Presentadores;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseUser;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.InterfacesMVP.Login;
import com.papaprogramador.presidenciales.Tareas.GoogleApiClientListener;
import com.papaprogramador.presidenciales.Tareas.IniciarSesionConCredenciales;
import com.papaprogramador.presidenciales.Tareas.ObtenerIdDispositivo;
import com.papaprogramador.presidenciales.Tareas.ObtenerIdFirebase;
import com.papaprogramador.presidenciales.Tareas.RegistrarUsuarioRTDB;
import com.papaprogramador.presidenciales.Utilidades.Constantes;

public class LoginPresentador extends MvpBasePresenter<Login.Vista> implements Login.Presentador {

	private String ID;
	private Context context;

	public LoginPresentador(Context context) {
		this.context = context;
	}


	@Override
	public void obtenerIdDispositivo(Context context) {
		new ObtenerIdDispositivo(context,
				new ObtenerIdDispositivo.OyenteTareaIdDispositivo() {
					@Override
					public void idGenerado(final String idDispositivo) {
						ID = idDispositivo;
					}
				});
	}

	@Override
	public void obtenerIdFirebase() {
		new ObtenerIdFirebase(ID,
				new ObtenerIdFirebase.IdObtenido() {
					@Override
					public void idObtenido(final boolean bool, final String idFirebase) {
						ifViewAttached(new ViewAction<Login.Vista>() {
							@Override
							public void run(@NonNull Login.Vista view) {
								if (!bool) {
									view.idYaUtilizado();
								} else {
									view.activityCrearNuevaCuenta(ID);
								}
							}
						});

					}
				});
	}

	@Override
	public void iniciarSesionConEmail(final Context context, final String emailUsuario, final String pass) {
		ifViewAttached(new ViewAction<Login.Vista>() {
			@Override
			public void run(@NonNull final Login.Vista view) {
				view.mostrarProgreso(true);
				new IniciarSesionConCredenciales(context, emailUsuario, pass,
						new IniciarSesionConCredenciales.IniciarSesion() {
							@Override
							public void resultadoInicio(final String resultado, FirebaseUser user) {
								switch (resultado) {
									case Constantes.RESULT_IS_SUCCESSFUL:
										view.activityListaCandidatos();
										break;
									case Constantes.RESULT_EMAIL_NO_VERIFY:
										view.mostrarProgreso(false);
										view.emailNoVerificado();
										break;
									case Constantes.RESULT_NO_SUCCESSFUL:
										view.mostrarProgreso(false);
										view.credencialesIncorrectas();
										break;
								}
							}
						});
			}
		});
	}

	@Override
	public void obtenerGoogleApliClient(final Context context, final String string) {
		ifViewAttached(new ViewAction<Login.Vista>() {
			@Override
			public void run(@NonNull final Login.Vista view) {
				new GoogleApiClientListener(context, string, new GoogleApiClientListener.GoogleApi() {
					@Override
					public void apiClient(final GoogleApiClient googleApiClient) {
						view.intentGoogle(googleApiClient);
					}
				});
			}
		});
	}

	@Override
	public void activityResetPassword() {
		ifViewAttached(new ViewAction<Login.Vista>() {
			@Override
			public void run(@NonNull Login.Vista view) {
				view.activityResetPassword();
			}
		});
	}

	@Override
	public void googleSingInFromResult(final Intent data) {
		ifViewAttached(new ViewAction<Login.Vista>() {
			@Override
			public void run(@NonNull Login.Vista view) {
				view.mostrarProgreso(true);
				GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
				resultGoogle(result);
			}
		});
	}

	@Override
	public void resultGoogle(final GoogleSignInResult result) {
		ifViewAttached(new ViewAction<Login.Vista>() {
			@Override
			public void run(@NonNull Login.Vista view) {
				if (result.isSuccess()) {
					validarDispositivoConCuentaGoogle(result.getSignInAccount());
				} else {
					view.mostrarProgreso(false);
					view.errorSigInGoogle();
				}
			}
		});

	}

	@Override
	public void validarDispositivoConCuentaGoogle(final GoogleSignInAccount signInAccount) {

		ifViewAttached(new ViewAction<Login.Vista>() {
			@Override
			public void run(@NonNull final Login.Vista view) {

				String emailUsuario = signInAccount.getEmail();

				new ObtenerIdFirebase(ID, emailUsuario, new ObtenerIdFirebase.IdObtenido() {
					@Override
					public void idObtenido(boolean bool, String idFirebase) {
						if (bool) {
							iniciarSesionConGoogle(context, signInAccount);
						} else {
							view.mostrarProgreso(false);
							view.idYaUtilizado();
						}
					}
				});
			}
		});
	}

	@Override
	public void iniciarSesionConGoogle(final Context context, final GoogleSignInAccount signInAccount) {

		ifViewAttached(new ViewAction<Login.Vista>() {
			@Override
			public void run(@NonNull final Login.Vista view) {
				new IniciarSesionConCredenciales(context, signInAccount,
						new IniciarSesionConCredenciales.IniciarSesion() {
							@Override
							public void resultadoInicio(final String resultado,
							                            final FirebaseUser user) {

								switch (resultado) {
									case Constantes.RESULT_IS_SUCCESSFUL:
										registrarUsuarioEnFirebase(user);
										break;
									case Constantes.RESULT_NO_SUCCESSFUL:
										view.mostrarProgreso(false);
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

		ifViewAttached(new ViewAction<Login.Vista>() {
			@Override
			public void run(@NonNull final Login.Vista view) {
				String nombreUsuario = user.getDisplayName();
				String emailUsuario = user.getEmail();
				String departamento = Constantes.VALOR_DEPARTAMENTO_DEFAULT;
				String idDispositivo = ID;
				String uidFirebase = user.getUid();
				String voto = Constantes.VALOR_VOTO_DEFAULT;

				new RegistrarUsuarioRTDB(uidFirebase, nombreUsuario, emailUsuario, departamento, idDispositivo,
						voto, new RegistrarUsuarioRTDB.RegistrarUsuarioFirebase() {
					@Override
					public void registroExitoso(boolean bool) {
						if (bool) {
							view.activityListaCandidatos();
						}
					}
				});
			}
		});
	}
}
