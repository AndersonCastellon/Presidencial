package com.papaprogramador.presidenciales.Utils.StaticMethods;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

public class CreatePhotoFile {
	public static File getPhotoFile(Context context, String timeStamp) {

		final String imageFileName = timeStamp.trim();
		File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

		File image = null;

		try {
			image = File.createTempFile(imageFileName, ".jpg", storageDir);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return image;
	}
}
