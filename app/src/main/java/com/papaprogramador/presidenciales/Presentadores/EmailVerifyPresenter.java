package com.papaprogramador.presidenciales.Presentadores;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.InterfacesMVP.EmailVerify;
import com.papaprogramador.presidenciales.Modelos.IniciarSesionConCredenciales;
import com.papaprogramador.presidenciales.Utilidades.Constantes;

public class EmailVerifyPresenter extends MvpBasePresenter<EmailVerify.Vista>
		implements EmailVerify.Presentador {

	private Context context;

	public EmailVerifyPresenter(Context context) {
		this.context = context;
	}

	@Override
	public void startIsEmailIsVerify(final String emailUser, final String pass) {
		ifViewAttached(new ViewAction<EmailVerify.Vista>() {
			@Override
			public void run(@NonNull final EmailVerify.Vista view) {

				view.showProgressBar(true);

				new IniciarSesionConCredenciales(context, emailUser, pass,
						new IniciarSesionConCredenciales.IniciarSesion() {
							@Override
							public void resultadoInicio(final String resultado, FirebaseUser user) {
								switch (resultado) {
									case Constantes.RESULT_NO_SUCCESSFUL:
										view.showProgressBar(false);
										view.errorSession();
										break;
									case Constantes.RESULT_EMAIL_NO_VERIFY:
										view.showProgressBar(false);
										view.emailNoVerify();
										break;
									case Constantes.RESULT_IS_SUCCESSFUL:
										view.showProgressBar(false);
										view.goMainActivity();
										break;
								}
							}
						});
			}
		});
	}
}
