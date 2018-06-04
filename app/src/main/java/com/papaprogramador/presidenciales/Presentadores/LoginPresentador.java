package com.papaprogramador.presidenciales.Presentadores;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.InterfacesMVP.Login;
import com.papaprogramador.presidenciales.Modelos.LoginModelo;
import com.papaprogramador.presidenciales.Tareas.IniciarSesionConCredenciales;
import com.papaprogramador.presidenciales.Tareas.LoginGoogle;
import com.papaprogramador.presidenciales.Tareas.ObtenerIdDispositivo;
import com.papaprogramador.presidenciales.Tareas.ObtenerIdFirebase;
import com.papaprogramador.presidenciales.Utilidades.Constantes;

public class LoginPresentador extends MvpBasePresenter<Login.Vista> implements Login.Presentador {

	private Login.Modelo modelo;
	private String ID;
	private Context context;

	public LoginPresentador(Context context) {
		this.context = context;
		this.modelo = new LoginModelo(this);
	}

	@Override
	public void obtenerIdDispositivo(Context context) {
		new ObtenerIdDispositivo(context,
				new ObtenerIdDispositivo.OyenteTareaIdDispositivo() {
					@Override
					public void idGenerado(String idDispositivo) {
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
						if (!bool) {
							ifViewAttached(new ViewAction<Login.Vista>() {
								@Override
								public void run(@NonNull Login.Vista view) {
									view.idYaUtilizado();
								}
							});
						} else {
							ifViewAttached(new ViewAction<Login.Vista>() {
								@Override
								public void run(@NonNull Login.Vista view) {
									view.crearNuevaCuenta(ID);
								}
							});
						}
					}
				});
	}

	@Override
	public void iniciarSesionConEmail(Context context, String emailUsuario, String pass) {
		new IniciarSesionConCredenciales(context, emailUsuario, pass, new IniciarSesionConCredenciales.IniciarSesion() {
			@Override
			public void resultadoInicio(final String resultado, String uidFirebase) {
				ifViewAttached(new ViewAction<Login.Vista>() {
					@Override
					public void run(@NonNull Login.Vista view) {
						switch (resultado) {
							case Constantes.RESULT_IS_SUCCESSFUL:
								view.irAVistaCandidatos();
								break;
							case Constantes.RESULT_EMAIL_NO_VERIFY:
								view.emailNoVerificado();
								break;
							case Constantes.RESULT_NO_SUCCESSFUL:
								view.credencialesIncorrectas();
								break;
						}
					}
				});
			}
		});
	}

	@Override
	public void iniciarSesionConGoogle(Context context, String string) {
		new LoginGoogle(context, string, new LoginGoogle.GoogleApi() {
			@Override
			public void apiClient(final GoogleApiClient googleApiClient) {
				ifViewAttached(new ViewAction<Login.Vista>() {
					@Override
					public void run(@NonNull Login.Vista view) {
						view.intentGoogle(googleApiClient);
					}
				});
			}
		});
	}

	@Override
	public void irAResetPassword() {
		ifViewAttached(new ViewAction<Login.Vista>() {
			@Override
			public void run(@NonNull Login.Vista view) {
				view.irAResetPassword();
			}
		});
	}

	@Override
	public void googleSingInFromResult(Intent data) {
		GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
		resultGoogle(result);
	}

	@Override
	public void resultGoogle(GoogleSignInResult result) {
		if (result.isSuccess()) {
			validarDispositivoConCuentaGoogle(result.getSignInAccount());
		} else {
			ifViewAttached(new ViewAction<Login.Vista>() {
				@Override
				public void run(@NonNull Login.Vista view) {
					view.errorSigInGoogle();
				}
			});
		}
	}

	@Override
	public void validarDispositivoConCuentaGoogle(final GoogleSignInAccount signInAccount) {

		new ObtenerIdFirebase(ID, signInAccount.getEmail(), new ObtenerIdFirebase.IdObtenido() {
			@Override
			public void idObtenido(boolean bool, String idFirebase) {
				if (bool) {
					new IniciarSesionConCredenciales(context, signInAccount,
							new IniciarSesionConCredenciales.IniciarSesion() {
								@Override
								public void resultadoInicio(final String resultado, final String uidFirebase) {
									ifViewAttached(new ViewAction<Login.Vista>() {
										@Override
										public void run(@NonNull Login.Vista view) {
											switch (resultado) {
												case Constantes.RESULT_IS_SUCCESSFUL:
													registrarUsuarioEnFirebase(signInAccount, uidFirebase);
													break;
												case Constantes.RESULT_NO_SUCCESSFUL:
													view.errorSigInGoogle();
													break;
											}
										}
									});
								}
							});
				} else {
					ifViewAttached(new ViewAction<Login.Vista>() {
						@Override
						public void run(@NonNull Login.Vista view) {
							view.idYaUtilizado();
						}
					});
				}
			}
		});
	}

	@Override
	public void registrarUsuarioEnFirebase(GoogleSignInAccount signInAccount, String uidFirebase) {
		String nombreUsuario = signInAccount.getDisplayName();
		String emailUsuario = signInAccount.getEmail();
		String departamento = Constantes.VALOR_DEPARTAMENTO_DEFAULT;
		String idDispositivo = ID;
		String voto = Constantes.VALOR_VOTO_DEFAULT;



	}
}
