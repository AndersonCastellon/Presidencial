package com.papaprogramador.presidenciales2019.InterfacesMVP;

public interface ResetPassword {

	interface Vista{
		void mostrarResultadoExitoso();
		void errorPorEmailSinCuentaAsociada();
		void errorPorCampoVacio();
		void errorPorEmailInvalido();
	}

	interface Presentador{
		void enviarResultadoExitoso();
		void enviarEmailSinCuentaAsociada();
		void enviarErrorPorCampoVacio();
		void enviarErrorPorEmailInvalido();
		void procesarEmailUsuario(String emailUsuario);
	}

	interface Modelo{
		void resetEmailUsuario(String emailUsuario);
	}
}
