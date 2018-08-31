package com.papaprogramador.presidenciales.common;

public interface BasicErrorEventCallback {
	void onSuccess();
	void onError(int typeEvent, int resMsg);
}
