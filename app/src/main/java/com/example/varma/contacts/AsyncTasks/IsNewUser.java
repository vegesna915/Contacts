package com.example.varma.contacts.AsyncTasks;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.varma.contacts.Extra.WebServiceConnection;
import com.example.varma.contacts.ProfileEditActivity;
import com.example.varma.contacts.R;
import com.example.varma.contacts.UserProfileActivty;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;


public class IsNewUser extends AsyncTask<Void, Void, JSONObject> {

    private Context context;
    private Activity activity;
    private String googleId;

    public IsNewUser(Context context, String googleId) {
        this.context = context;
        this.activity = (Activity) context;
        this.googleId = googleId;
    }

    @Override
    protected void onPreExecute() {
        ScrollView scrollView = (ScrollView) activity.findViewById(R.id.scrollView_login);
        scrollView.setAlpha(0.3f);
        scrollView.setClickable(false);

        SignInButton googleSignIn = (SignInButton) activity.findViewById(R.id.googleSignIn_login);
        googleSignIn.setAlpha(0.3f);
        googleSignIn.setClickable(false);


        ProgressBar progressBar = (ProgressBar) activity.findViewById(R.id.progressBar_login);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected JSONObject doInBackground(Void... voids) {

        String urlString = "http://byvarma.esy.es/New/isUserNewGoogle.php";
        String parameters = "GOOGLE_ID=" + googleId;

        JSONObject json = WebServiceConnection.getData(urlString, parameters);

        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(
                    context.getString(R.string.loginDetails), Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(context.getString(R.string.userName), json.getString("_NAME"));
            editor.putString(context.getString(R.string.userNumber), json.getString("_NUMBER"));
            editor.putString(context.getString(R.string.userDatabaseId), json.getString("_ID"));
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        getToken();

        return json;
    }

    @Override
    protected void onPostExecute(JSONObject json) {

        if (json == null) {
            SharedPreferences sharedPreferences = activity.getSharedPreferences(
                    activity.getString(R.string.loginDetails), Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(activity.getString(R.string.loginStatus), false);
            editor.commit();
            Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show();
            return;
        }


        String isNew = "0";
        try {
            isNew = json.getString("IS_NEW");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        switch (isNew) {
            case "1":
                isNew();
                break;
            case "0":
                isNotNew(json);
                break;
            default:
                Toast.makeText(context, "not working: \n", Toast.LENGTH_LONG).show();
                break;
        }
        ProgressBar progressBar = (ProgressBar) activity.findViewById(R.id.progressBar_login);
        progressBar.setVisibility(View.GONE);

        ScrollView scrollView = (ScrollView) activity.findViewById(R.id.scrollView_login);
        scrollView.setAlpha(1f);
        scrollView.setClickable(true);

        SignInButton googleSignIn = (SignInButton) activity.findViewById(R.id.googleSignIn_login);
        googleSignIn.setAlpha(1f);
        googleSignIn.setClickable(true);

    }

    private void isNew() {
        Intent toEditProfile = new Intent(context, ProfileEditActivity.class);
        toEditProfile.putExtra("isNew", true);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(activity);

        // Adds the back stack
        stackBuilder.addParentStack(UserProfileActivty.class);
        stackBuilder.addParentStack(ProfileEditActivity.class);
        // Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(toEditProfile);
        // Gets a PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        try {
            resultPendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    private void isNotNew(JSONObject json) {


        Intent toUserProfile = new Intent(activity, UserProfileActivty.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(activity);

        // Adds the back stack
        stackBuilder.addParentStack(UserProfileActivty.class);
        // Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(toUserProfile);
        // Gets a PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        try {
            resultPendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    private void getToken() {
        String token = FirebaseInstanceId.getInstance().getToken();


        if (token == null || token.equals("")) {
            Log.i("token", "Token not received");
            return;
        }

        Log.i("token", token);

        SharedPreferences sharedPref = activity.getSharedPreferences(
                activity.getString(R.string.loginDetails), Context.MODE_PRIVATE);
        boolean isLogin = sharedPref.getBoolean(activity.getString(R.string.loginStatus), false);

        if (!isLogin) {
            return;
        }


        String _ID = sharedPref.getString(activity.getString(R.string.userDatabaseId), "0");
        String url = "http://byvarma.esy.es/New/saveToken.php";
        String parameters = "_ID=" + _ID + "&_TOKEN=" + token;

        Log.i("token", "sending token");
        JSONObject json = WebServiceConnection.getData(url, parameters);
        if (json == null) {

            Log.i("token", "saveToken.php error");
        }

        try {
            Log.i("token", "Inserted : " + json.getString("INSERTED"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}