package com.papaprogramador.presidenciales.Presentadores;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
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
	private Login.Vista vista;

	public LoginPresentador(Context context) {
		this.context = context;
		viewAction();
	}

	private void viewAction() {
		ifViewAttached(new ViewAction<Login.Vista>() {
			@Override
			public void run(@NonNull Login.Vista view) {
				vista = view;
			}
		});
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
						if (!bool) {
							vista.idYaUtilizado();
						} else {
							vista.activityCrearNuevaCuenta(ID);
						}
					}
				});
	}

	@Override
	public void iniciarSesionConEmail(Context context, String emailUsuario, String pass) {
		vista.mostrarProgreso(true);
		new IniciarSesionConCredenciales(context, emailUsuario, pass, new IniciarSesionConCredenciales.IniciarSesion() {
			@Override
			public void resultadoInicio(final String resultado, FirebaseUser user) {
				switch (resultado) {
					case Constantes.RESULT_IS_SUCCESSFUL:
						vista.activityListaCandidatos();
						break;
					case Constantes.RESULT_EMAIL_NO_VERIFY:
						vista.mostrarProgreso(false);
						vista.emailNoVerificado();
						break;
					case Constantes.RESULT_NO_SUCCESSFUL:
						vista.mostrarProgreso(false);
						vista.credencialesIncorrectas();
						break;
				}
			}
		});
	}

	@Override
	public void iniciarSesionConGoogle(Context context, String string) {
		new GoogleApiClientListener(context, string, new GoogleApiClientListener.GoogleApi() {
			@Override
			public void apiClient(final com.google.android.gms.common.api.GoogleApiClient googleApiClient) {
				vista.intentGoogle(googleApiClient);
			}
		});
	}

	@Override
	public void activityResetPassword() {
		vista.activityResetPassword();
	}

	@Override
	public void googleSingInFromResult(Intent data) {
		vista.mostrarProgreso(true);
		GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
		resultGoogle(result);
	}

	@Override
	public void resultGoogle(GoogleSignInResult result) {
		if (result.isSuccess()) {
			validarDispositivoConCuentaGoogle(result.getSignInAccount());
		} else {
			vista.mostrarProgreso(false);
			vista.errorSigInGoogle();
		}
	}

	@Override
	public void validarDispositivoConCuentaGoogle(final GoogleSignInAccount signInAccount) {
		String emailUsuario = signInAccount.getEmail();

		new ObtenerIdFirebase(ID, emailUsuario, new ObtenerIdFirebase.IdObtenido() {
			@Override
			public void idObtenido(boolean bool, String idFirebase) {
				if (bool) {
					new IniciarSesionConCredenciales(context, signInAccount,
							new IniciarSesionConCredenciales.IniciarSesion() {
								@Override
								public void resultadoInicio(final String resultado, final FirebaseUser user) {
									switch (resultado) {
										case Constantes.RESULT_IS_SUCCESSFUL:
											registrarUsuarioEnFirebase(user);
											break;
										case Constantes.RESULT_NO_SUCCESSFUL:
											vista.mostrarProgreso(false);
											vista.errorSigInGoogle();
											break;
									}
								}
							});
				} else {
					vista.mostrarProgreso(false);
					vista.idYaUtilizado();
				}
			}
		});
	}

	@Override
	public void registrarUsuarioEnFirebase(FirebaseUser user) {
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
				if (bool){
					vista.activityListaCandidatos();
				}
			}
		});
	}
}
