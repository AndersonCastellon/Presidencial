package com.papaprogramador.presidenciales.Modelos;

import com.papaprogramador.presidenciales.InterfacesMVP.Login;

public class LoginModelo implements Login.Modelo {

	private Login.Presentador presentador;

	public LoginModelo(Login.Presentador presentador) {
		this.presentador = presentador;
	}


}
