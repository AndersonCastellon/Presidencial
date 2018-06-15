package com.papaprogramador.presidenciales.Cases;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidarEmail {
	public interface EmailValidado {
		void emailEsValido(Boolean esValido);
	}

	private EmailValidado listener;

	public ValidarEmail(String emailUsuario, EmailValidado listener) {
		this.listener = listener;
		validarEmailUsuario(emailUsuario);
	}

	private void validarEmailUsuario(String emailUsuario) {

		String regex = "^[A-Za-z0-9+_.-]+@(.+)$";

		Pattern pattern = Pattern.compile(regex);

		Matcher mather = pattern.matcher(emailUsuario);

		if (mather.find()) {
			listener.emailEsValido(true);
		} else {
			listener.emailEsValido(false);
		}
	}
	}
