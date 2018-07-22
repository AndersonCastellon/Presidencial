package com.papaprogramador.presidenciales.Utils.StaticMethods;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

import static android.support.v4.app.ActivityCompat.requestPermissions;
import static android.support.v4.content.ContextCompat.checkSelfPermission;

//TODO: Id dispositivo llega nulo cuando se quiere obtener idFirebase, validar la lógica actual

public class GetIdDevice {

	@SuppressLint("HardwareIds")
	public static String getIdDevice(Context context) {

		String idDevice;

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

			idDevice = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

		} else {

			if (checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
					!= PackageManager.PERMISSION_GRANTED) {

				requestPermissions((Activity) context,
						new String[]{Manifest.permission.READ_PHONE_STATE}, 225);
				idDevice = "";

			} else {

				idDevice = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

			}
		}

		return idDevice;
	}
}
