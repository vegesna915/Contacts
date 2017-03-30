package com.example.varma.contacts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * Created by Varma on 3/29/2017.
 */

public class PermissionsClass {


    static String readContacts =  Manifest.permission.READ_CONTACTS;
    static  String readCallLog = Manifest.permission.READ_CALL_LOG;


    static boolean hasPermissionReadContacts(Context context) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {

            return (context.checkSelfPermission(readContacts) == PackageManager.PERMISSION_GRANTED);
        }

        return true;
    }

    static boolean hasPermissionReadCallLog(Context context) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {

            return (context.checkSelfPermission(readCallLog) == PackageManager.PERMISSION_GRANTED);
        }

        return true;
    }



    static void requestPermission(Activity activity, String[] permission, int requestId) {


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            activity.requestPermissions(permission,requestId);
        }

    }

}
