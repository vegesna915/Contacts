package com.example.varma.contacts.Extra;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;


public class PermissionsClass {


    public final static String readContacts = Manifest.permission.READ_CONTACTS;
    public final static String readCallLog = Manifest.permission.READ_CALL_LOG;
    public final static String CallPhone = Manifest.permission.CALL_PHONE;


    public static boolean hasPermissionReadContacts(Context context) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {

            return (context.checkSelfPermission(readContacts) == PackageManager.PERMISSION_GRANTED);
        }

        return true;
    }

    public static boolean hasPermissionReadCallLog(Context context) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {

            return (context.checkSelfPermission(readCallLog) == PackageManager.PERMISSION_GRANTED);
        }

        return true;
    }

    public static boolean hasPermissionCallPhone(Context context) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {

            return (context.checkSelfPermission(CallPhone) == PackageManager.PERMISSION_GRANTED);
        }

        return true;
    }


    public static void requestPermission(Activity activity, String[] permission, int requestId) {


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            activity.requestPermissions(permission, requestId);
        }

    }

}
