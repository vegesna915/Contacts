package com.example.varma.contacts;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.varma.contacts.Extra.WebServiceConnection;
import com.example.varma.contacts.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONObject;

/**
 * Created by Varma on 4/18/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();

        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.loginDetails), Context.MODE_PRIVATE);
        boolean isLogin = sharedPref.getBoolean(getString(R.string.loginStatus), false);

        if (!isLogin) {
            return;
        }


        String _ID = sharedPref.getString(getString(R.string.userDatabaseId), "0");
        String url = "http://byvarma.esy.es/New/saveToken.php";
        String parameters = "_ID=" + _ID + "&_TOKEN=" + token;

        JSONObject json = WebServiceConnection.getData(url, parameters);
        if (json == null) {


        }
    }


}
