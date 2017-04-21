package com.example.varma.contacts;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.varma.contacts.Extra.PermissionsClass;
import com.google.firebase.messaging.FirebaseMessaging;


public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        permissionCheck();
        setContentView(R.layout.activity_start);





    }

    void permissionCheck() {

        boolean readContact = PermissionsClass.hasPermissionReadContacts(this);
        boolean readCallLog = PermissionsClass.hasPermissionReadCallLog(this);

        if (readCallLog && readContact) {
            Intent toHomeActivity = new Intent(this, HomeActivity.class);
            toHomeActivity.putExtra(getString(R.string.putExtraPage_HomeActivity), 1);
            toHomeActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(toHomeActivity);
        } else if (readCallLog) {

            PermissionsClass.requestPermission(this, new String[]{PermissionsClass.readContacts}, 100);
        } else if (readContact) {
            PermissionsClass.requestPermission(this, new String[]{PermissionsClass.readCallLog}, 101);
        } else {

            PermissionsClass.requestPermission(this, new String[]{PermissionsClass.readContacts, PermissionsClass.readCallLog}, 102);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Intent toHomeActivity = new Intent(this, HomeActivity.class);
        toHomeActivity.putExtra(getString(R.string.putExtraPage_HomeActivity), 1);
        toHomeActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(toHomeActivity);

    }


}

