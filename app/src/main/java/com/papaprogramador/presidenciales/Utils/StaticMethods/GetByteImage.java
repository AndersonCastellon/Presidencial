package com.papaprogramador.presidenciales.Utils.StaticMethods;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class GetByteImage {

	public static byte[] getImageBytes(Bitmap bitmap) throws IOException {

		BitmapFactory.Options bmoptions = new BitmapFactory.Options();
		bmoptions.inJustDecodeBounds = true;
		bmoptions.inSampleSize = 1;
		bmoptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
		bmoptions.inJustDecodeBounds = false;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
		baos.close();

		return baos.toByteArray();
	}
}
