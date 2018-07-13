package com.papaprogramador.presidenciales.Utils.StaticMethods;

import android.content.Context;
import android.content.SharedPreferences;

import com.papaprogramador.presidenciales.Utils.Constans;


public class SharedPreferencesMethods {

	private static SharedPreferences sharedPreferences;

	public static void saveEmailAndPassword(Context context, String emailUser, String password) {

		sharedPreferences = context.getSharedPreferences(Constans.SP_BLOCK_CREDENTIALS_USERS, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();

		editor.putString(Constans.SP_PUTSTRING_EMAILUSER, emailUser);
		editor.putString(Constans.SP_PUTSTRING_PASSWORDUSER, password);

		editor.apply();
	}

	public static void setPassword(Context context, String newPassword) {

		sharedPreferences = context.getSharedPreferences(Constans.SP_BLOCK_CREDENTIALS_USERS, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();

		editor.putString(Constans.SP_PUTSTRING_PASSWORDUSER, newPassword);
		editor.apply();
	}

	public static String getEmail(Context context) {
		sharedPreferences = context.getSharedPreferences(Constans.SP_BLOCK_CREDENTIALS_USERS, Context.MODE_PRIVATE);
		return sharedPreferences.getString(Constans.SP_PUTSTRING_EMAILUSER, null);
	}

	public static String getPassword(Context context) {
		sharedPreferences = context.getSharedPreferences(Constans.SP_BLOCK_CREDENTIALS_USERS, Context.MODE_PRIVATE);
		return sharedPreferences.getString(Constans.SP_PUTSTRING_PASSWORDUSER, null);
	}
}
