package com.example.varma.contacts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;

/**
 * Created by Varma on 4/1/2017.
 */
class LogingIn extends AsyncTask<Void, Void, Boolean> {

    String[] userIds={"vegesna95@gmail.com","sunny.bodepudi@gmail.com"};
    String[] passwords = {"1234512345","1234512345"};
    String userId, password;
    Activity activity;

    LogingIn(String userId, String password,Activity activity) {
        this.userId = userId;
        this.password = password;
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(Void... params) {


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int i = 0;
        boolean login = false;
        while ((i < userIds.length) && !login) {
            if (userId.equals(userIds[i])) {
                login = (password.equals(passwords[i]));
            }
            i++;
        }
        return login;
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
    protected void onPostExecute(Boolean loginStatus) {

        ProgressBar progressBar = (ProgressBar) activity.findViewById(R.id.progressBar_login);
        progressBar.setVisibility(View.GONE);

        ScrollView scrollView = (ScrollView) activity.findViewById(R.id.scrollView_login);
        scrollView.setAlpha(1f);
        scrollView.setClickable(true);

        SignInButton googleSignIn = (SignInButton) activity.findViewById(R.id.googleSignIn_login);
        googleSignIn.setAlpha(1f);
        googleSignIn.setClickable(true);


        if (loginStatus) {
            Toast.makeText(activity, " Login Successful ", Toast.LENGTH_SHORT).show();


            SharedPreferences sharedPreferences = activity.getSharedPreferences(
                    activity.getString(R.string.loginDetails), Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(activity.getString(R.string.loginStatus), true);
            editor.putBoolean(activity.getString(R.string.loginIsGoogle),false);
            editor.putString(activity.getString(R.string.loginEmailId), userId);
            editor.commit();

            Intent toUserProfile = new Intent(activity, UserProfileActivty.class);
            activity.startActivity(toUserProfile);

        } else {
            AutoCompleteTextView userIdView = (AutoCompleteTextView) activity.findViewById(R.id.userName_login);
            userIdView.setError("Invalid User Email or Password");
            userIdView.requestFocus();
        }
    }
}