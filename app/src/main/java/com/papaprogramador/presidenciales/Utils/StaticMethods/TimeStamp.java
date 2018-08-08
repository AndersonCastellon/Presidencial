package com.papaprogramador.presidenciales.Utils.StaticMethods;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeStamp {

	public static String timeStamp(String pattern) {
		return new SimpleDateFormat(pattern, Locale.ROOT)
				.format(new Date());
	}
}
