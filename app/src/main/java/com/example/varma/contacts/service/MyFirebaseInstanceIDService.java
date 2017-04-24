package com.example.varma.contacts.service;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.varma.contacts.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();

        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.loginDetails), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(getString(R.string.userToken), token);

        editor.commit();
    }


}
