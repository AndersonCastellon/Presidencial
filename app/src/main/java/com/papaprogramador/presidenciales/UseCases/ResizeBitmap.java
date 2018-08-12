package com.papaprogramador.presidenciales.UseCases;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ResizeBitmap {

	private static float WIDTH = 600;
	private static float HEIGHT = 800;

	public static Bitmap resizeBitmap(Bitmap bitmap) {

		int currentWidth = bitmap.getWidth();
		int currentHeight = bitmap.getHeight();

		if (currentWidth > WIDTH || currentHeight > HEIGHT) {

			if (currentWidth > currentHeight) {
				WIDTH = 800;
				HEIGHT = 600;

				return createNewBitmap(bitmap, currentWidth, currentHeight);

			} else if (currentWidth == currentHeight) {
				WIDTH = 800;
				HEIGHT = 800;

				return createNewBitmap(bitmap, currentWidth, currentHeight);
			} else {

				return createNewBitmap(bitmap, currentWidth, currentHeight);
			}
		}

		return bitmap;
	}

	private static Bitmap createNewBitmap(Bitmap bitmap, int currentWidth, int currentHeight) {

		float scaleWidth = WIDTH / currentWidth;
		float scaleHeight = HEIGHT / currentHeight;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);

		return Bitmap.createBitmap(bitmap, 0, 0, currentWidth,
				currentHeight, matrix, false);
	}
}
