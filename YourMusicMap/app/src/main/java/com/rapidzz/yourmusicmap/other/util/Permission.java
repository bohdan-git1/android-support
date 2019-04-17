package com.rapidzz.yourmusicmap.other.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

public class Permission {

    public static boolean isPermissionGranted(Activity context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }


    public static void requestPermission(Fragment fragment, String... permissions) {
        fragment.requestPermissions(permissions, 50);
    }

    public static void requestPermission(Fragment fragment, int requestCode, String... permissions) {
        fragment.requestPermissions(permissions, requestCode);
    }

    public static void requestPermission(Activity activity, String... permissions) {
        ActivityCompat.requestPermissions(activity, permissions, 50);
    }
}
