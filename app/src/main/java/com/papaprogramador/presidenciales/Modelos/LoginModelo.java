package com.papaprogramador.presidenciales.Modelos;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.papaprogramador.presidenciales.InterfacesMVP.Login;

public class LoginModelo implements Login.Modelo {

	private Login.Presentador presentador;

	public LoginModelo(Login.Presentador presentador) {
		this.presentador = presentador;
	}



}
