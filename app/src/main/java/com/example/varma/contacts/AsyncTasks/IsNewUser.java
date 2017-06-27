package com.example.varma.contacts.AsyncTasks;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.example.varma.contacts.Extra.WebServiceConnection;
import com.example.varma.contacts.LoginActivity;
import com.example.varma.contacts.ProfileEditActivity;
import com.example.varma.contacts.R;
import com.example.varma.contacts.UserProfileActivity;
import com.example.varma.contacts.service.SyncDataJobService;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;


public class IsNewUser extends AsyncTask<Void, Void, String> {

    private LoginActivity loginActivity;
    private String googleId;

    public IsNewUser(LoginActivity loginActivity, String googleId) {

        this.loginActivity = loginActivity;
        this.googleId = googleId;

    }

    @Override
    protected void onPreExecute() {

        /*ScrollView scrollView = (ScrollView) activity.findViewById(R.id.scrollView_login);
        scrollView.setAlpha(0.3f);
        scrollView.setClickable(false);

        SignInButton googleSignIn = (SignInButton) activity.findViewById(R.id.googleSignIn_login);
        googleSignIn.setAlpha(0.3f);
        googleSignIn.setClickable(false);


        ProgressBar progressBar = (ProgressBar) activity.findViewById(R.id.progressBar_login);
        progressBar.setVisibility(View.VISIBLE);*/

        loginActivity.progressBar(true);

    }

    @SuppressLint("ApplySharedPref")
    @Override
    protected String doInBackground(Void... voids) {

        SharedPreferences sharedPref = loginActivity.getSharedPreferences(
                loginActivity.getString(R.string.loginDetails), Context.MODE_PRIVATE);
        String imageUrl = sharedPref.getString(loginActivity.getString(R.string.userImageUrl), "");

        String token = sharedPref.getString(loginActivity.getString(R.string.userToken), "");
        if (token.equals("")) {
            token = FirebaseInstanceId.getInstance().getToken();
        }

        String urlString = "http://byvarma.esy.es/New/isUserNewGoogle.php";
        String parameters = "GOOGLE_ID=" + googleId +
                "&IMAGE_URL=" + imageUrl +
                "&_TOKEN=" + token;
        Log.i("signin", "1");
        JSONObject json = WebServiceConnection.getData(urlString, parameters);

        if (json == null) {
            return "2";
        }

        String isNew = "0";

        try {
            SharedPreferences sharedPreferences = loginActivity.getSharedPreferences(
                    loginActivity.getString(R.string.loginDetails), Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(loginActivity.getString(R.string.userName), json.getString("_NAME"));
            editor.putString(loginActivity.getString(R.string.userNumber), json.getString("_NUMBER"));
            editor.putString(loginActivity.getString(R.string.userDatabaseId), json.getString("_ID"));
            editor.putString(loginActivity.getString(R.string.userId), json.getString("USER_ID"));
            editor.commit();
            isNew = json.getString("IS_NEW");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //getToken();

        return isNew;
    }

    @Override
    protected void onPostExecute(String isNew) {

        if (isNew.equals("2")) {
            SharedPreferences sharedPreferences = loginActivity.getSharedPreferences(
                    loginActivity.getString(R.string.loginDetails), Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(loginActivity.getString(R.string.loginStatus), false);
            editor.apply();
            Toast.makeText(loginActivity, "Login Failed", Toast.LENGTH_SHORT).show();
            /*
            ProgressBar progressBar = (ProgressBar) loginActivity.findViewById(R.id.progressBar_login);
            progressBar.setVisibility(View.GONE);

            ScrollView scrollView = (ScrollView) loginActivity.findViewById(R.id.scrollView_login);
            scrollView.setAlpha(1f);
            scrollView.setClickable(true);

            SignInButton googleSignIn = (SignInButton) loginActivity.findViewById(R.id.googleSignIn_login);
            googleSignIn.setAlpha(1f);
            googleSignIn.setClickable(true);*/
            loginActivity.progressBar(false);
            return;
        }



        switch (isNew) {
            case "1":
                isNew();
                break;
            case "0":
                isNotNew();
                break;
            default:
                Toast.makeText(loginActivity, "not working: \n", Toast.LENGTH_LONG).show();
                break;
        }


    }

    private void isNew() {
        Intent toEditProfile = new Intent(loginActivity, ProfileEditActivity.class);
        toEditProfile.putExtra("isNew", true);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(loginActivity);

        // Adds the back stack
        stackBuilder.addParentStack(UserProfileActivity.class);
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

    private void isNotNew() {

        callSyncDataService();

        Intent toUserProfile = new Intent(loginActivity, UserProfileActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(loginActivity);

        // Adds the back stack
        stackBuilder.addParentStack(UserProfileActivity.class);
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

    private void callSyncDataService() {

        JobScheduler jobScheduler = (JobScheduler) loginActivity.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo jobInfo = new JobInfo.Builder(300, new ComponentName(loginActivity, SyncDataJobService.class))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build();
        jobScheduler.schedule(jobInfo);


    }



}
