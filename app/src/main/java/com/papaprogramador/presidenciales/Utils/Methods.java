package com.papaprogramador.presidenciales.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Methods {

	//Método para verificar la conexion a internet
	public static boolean checkConnection(Context context) {

		boolean connected = false;

		ConnectivityManager connectivityManager = (ConnectivityManager)
				context.getSystemService(Context.CONNECTIVITY_SERVICE);

		// Recupera todas las redes (tanto móviles como wifi)
		NetworkInfo[] redes = new NetworkInfo[0];

		if (connectivityManager != null) {
			redes = connectivityManager.getAllNetworkInfo();
		}

		for (NetworkInfo networkInfo : redes) {
			// Si alguna red tiene conexión, se devuelve true
			if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
				connected = true;
			}
		}
		return connected;
	}
}
