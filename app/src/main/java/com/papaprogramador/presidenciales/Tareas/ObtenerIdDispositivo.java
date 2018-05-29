package com.papaprogramador.presidenciales.Tareas;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

import static android.support.v4.app.ActivityCompat.requestPermissions;
import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class ObtenerIdDispositivo {

	public interface OyenteTareaIdDispositivo {
		void idGenerado(String idDispositivo);
	}

	private OyenteTareaIdDispositivo listener;

	public ObtenerIdDispositivo(Context context, OyenteTareaIdDispositivo listener) {
		this.listener = listener;
		obtenerIdDispositivo(context);
	}

	private void obtenerIdDispositivo(Context context) {
		String idDispositivo;

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			//Menores a Android 6.0
			idDispositivo = getID(context);
			enviarIdDispositivo(idDispositivo);
		} else {
			// Mayores a Android 6.0
			if (checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
					!= PackageManager.PERMISSION_GRANTED) {
				requestPermissions((Activity) context,
						new String[]{Manifest.permission.READ_PHONE_STATE}, 225);
				idDispositivo = "";
			} else {
				idDispositivo = getID(context);
				enviarIdDispositivo(idDispositivo);
			}
		}
	}

	//Método que obtiene el IMEI
	private String getID(Context context) {
		String ID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
		return ID;

	}

	private void enviarIdDispositivo(String idDispositivo) {
		listener.idGenerado(idDispositivo);
	}
}
