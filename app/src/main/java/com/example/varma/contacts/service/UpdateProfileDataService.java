package com.example.varma.contacts.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.example.varma.contacts.Extra.WebServiceConnection;
import com.example.varma.contacts.R;

import org.json.JSONException;
import org.json.JSONObject;


public class UpdateProfileDataService extends IntentService {

    boolean jobDone;

    public UpdateProfileDataService() {
        super("UpdateProfileDataService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent == null) {
            return;
        }

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.loginDetails), Context.MODE_PRIVATE);

        String _ID = sharedPref.getString(getString(R.string.userDatabaseId), "");
        String GOOGLE_ID = sharedPref.getString(getString(R.string.userGmailId), "");
        String NAME = sharedPref.getString(getString(R.string.userName), "");
        String NUMBER = sharedPref.getString(getString(R.string.userNumber), "");


        String urlString = "http://byvarma.esy.es/New/updateUserData.php";
        String parameters = "_ID=" + _ID +
                "&GOOGLE_ID=" + GOOGLE_ID +
                "&_NAME=" + NAME +
                "&_NUMBER=" + NUMBER;

        JSONObject json = WebServiceConnection.getData(urlString, parameters);

        try {
            String result = json.getString("updateComplete");
            jobDone = result.equals("1");

        } catch (JSONException e) {
            jobDone = false;
        }


    }
}
