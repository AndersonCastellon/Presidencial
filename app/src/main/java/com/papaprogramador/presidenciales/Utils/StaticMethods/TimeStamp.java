package com.papaprogramador.presidenciales.Utils.StaticMethods;

import android.content.Context;

import com.papaprogramador.presidenciales.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeStamp {

	public static String timeStamp(Context context) {
		String string = context.getResources().getString(R.string.string_time_stamp);
		return new SimpleDateFormat("dd-MM-yyyy" + " " + string + " " + "HH:mm:ss", Locale.ROOT)
				.format(new Date());
	}
}
