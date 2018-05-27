package com.papaprogramador.presidenciales.InterfacesMVP;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface ResetPassword {

	interface Vista extends MvpView {
		void mostrarResultadoExitoso();

		void errorPorEmailSinCuentaAsociada();

		void errorPorCampoVacio();

		void errorPorEmailInvalido();
	}

	interface Presentador extends MvpPresenter<ResetPassword.Vista>{
		void emailEnviado();

		void emailSinCuentaAsociada();

		void campoEmailVacio();

		void emailInvalido();

		void procesarEmailUsuario(String emailUsuario);
	}

	interface Modelo {
		void resetEmailUsuario(String emailUsuario);
	}
}
