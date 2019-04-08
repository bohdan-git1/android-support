package com.rapidzz.yourmusicmap.other.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

public class Permission {

    public static boolean isPermissionGranted(Activity context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }


    public static void requestPermission(Fragment fragment, String... permissions) {
        fragment.requestPermissions(permissions, 50);
    }

    public static void requestPermission(Activity fragment, String... permissions) {
        ActivityCompat.requestPermissions(fragment, permissions, 50);
    }
}
