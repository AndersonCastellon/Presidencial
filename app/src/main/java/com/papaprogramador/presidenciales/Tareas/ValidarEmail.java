package com.papaprogramador.presidenciales.Tareas;

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

		Pattern pattern = Pattern
				.compile("([a-z0-9]+(\\\\.?[a-z0-9])*)+@(([a-z]+)\\\\.([a-z]+))+");

		Matcher mather = pattern.matcher(emailUsuario);

		if (mather.find()) {
			listener.emailEsValido(true);
		} else {
			listener.emailEsValido(false);
		}
	}
	}
