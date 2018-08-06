package com.papaprogramador.presidenciales.UseCases;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;

public class GetIdDevice {
	@SuppressLint("HardwareIds")
	public static String getIdDevice(Context context) {
		return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
	}
}
