package com.example.varma.contacts.Extra;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class Caller {


    static final String feedBackEmail = "developers.incontact@gmail.com";


    public static void callNumber(Activity activity, String number) {
        if (PermissionsClass.hasPermissionCallPhone(activity)) {

            number = "tel:" + number;

            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
            try {
                activity.startActivity(callIntent);
            } catch (SecurityException e) {
                e.printStackTrace();
            }

        } else {
            PermissionsClass.requestPermission(activity,
                    new String[]{PermissionsClass.CallPhone}, 701);
        }
    }


    public static void smsNumber(Activity activity, String number) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + number));
        activity.startActivity(intent);
    }

}
