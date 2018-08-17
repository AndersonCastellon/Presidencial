package com.papaprogramador.presidenciales.Utils.StaticMethods;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class GetByteImage {

	public static byte[] getImageBytes(Bitmap bitmap) {

		BitmapFactory.Options bmoptions = new BitmapFactory.Options();
		bmoptions.inJustDecodeBounds = true;
		bmoptions.inSampleSize = 1;
		bmoptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
		bmoptions.inJustDecodeBounds = false;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);

		return baos.toByteArray();
	}
}
