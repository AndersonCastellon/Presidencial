package com.papaprogramador.presidenciales.Utils.StaticMethods;

import android.content.Context;

import com.papaprogramador.presidenciales.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeStamp {

	public static String timeStamp() {
		return new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss", Locale.ROOT)
				.format(new Date());
	}
}
