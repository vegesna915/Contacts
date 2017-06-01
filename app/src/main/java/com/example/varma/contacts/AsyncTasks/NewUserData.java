package com.example.varma.contacts.AsyncTasks;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.varma.contacts.Extra.WebServiceConnection;
import com.example.varma.contacts.R;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;


public class NewUserData extends AsyncTask<Void, Void, JSONObject> {

    private Activity activity;

    public NewUserData(Activity activity) {
        this.activity = activity;
    }

    @SuppressLint("ApplySharedPref")
    @Override
    protected JSONObject doInBackground(Void... voids) {


        SharedPreferences sharedPref = activity.getSharedPreferences(
                activity.getString(R.string.loginDetails), Context.MODE_PRIVATE);
        String googleId = sharedPref.getString(activity.getString(R.string.userGmailId), "");
        String name = sharedPref.getString(activity.getString(R.string.userName), "");
        String number = sharedPref.getString(activity.getString(R.string.userNumber), "");
        String email = sharedPref.getString(activity.getString(R.string.loginEmail), "");
        String imageUrl = sharedPref.getString(activity.getString(R.string.userImageUrl), "");

        String token = sharedPref.getString(activity.getString(R.string.userToken), "");
        if (token.equals("")) {
            token = FirebaseInstanceId.getInstance().getToken();
        }

        String urlString = "http://byvarma.esy.es/New/createNewUser.php";
        String parameters = "GOOGLE_ID=" + googleId +
                "&_NAME=" + name +
                "&_NUMBER=" + number +
                "&_EMAIL=" + email +
                "&IMAGE_URL=" + imageUrl +
                "&_TOKEN=" + token;

        JSONObject json = WebServiceConnection.getData(urlString, parameters);

        if (json == null) {
            SharedPreferences sharedPreferences = activity.getSharedPreferences(
                    activity.getString(R.string.loginDetails), Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(activity.getString(R.string.loginStatus), false);
            editor.commit();

            Toast.makeText(activity, "Login Failed", Toast.LENGTH_SHORT).show();
            return null;
        }


        try {

            SharedPreferences sharedPreferences = activity.getSharedPreferences(
                    activity.getString(R.string.loginDetails), Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(activity.getString(R.string.userDatabaseId), json.getString("_ID"));
            editor.commit();
            //getToken();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    @Override
    protected void onPostExecute(JSONObject json) {



    }

}
