package com.papaprogramador.presidenciales.Utils.StaticMethods;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

public class GetPermissions {

	public static boolean checkPermissionToApp(Context context, String permission, int requestPermission) {

		boolean permissionChek = false;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED){
				ActivityCompat.requestPermissions((Activity) context, new String[] {permission}, requestPermission);
			} else {
				permissionChek = true;
			}
		}
		return permissionChek;
	}
}
