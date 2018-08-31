package com.papaprogramador.presidenciales.common;

public class RxFirebaseDataErrorException extends Throwable {
	private int codeException;

	public RxFirebaseDataErrorException(int codeException) {
		this.codeException = codeException;
	}

	public int getCodeException() {
		return codeException;
	}

	@Override
	public String toString() {
		return "RxFirebaseDataErrorException{" +
				"codeException=" + codeException +
				'}';
	}
}
