package com.example.varma.contacts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText passwordView;
    AutoCompleteTextView userIdView;
    Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        userIdView = (AutoCompleteTextView) findViewById(R.id.userName_login);

        passwordView = (EditText) findViewById(R.id.password_login);

        Button loginbutton = (Button) findViewById(R.id.loginButton_login);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);

                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }


                attemptLogin();

            }
        });

        Button forgotbutton = (Button) findViewById(R.id.forgotPassword_login);
        forgotbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Sorry about that, we can do nothing about that", Snackbar.LENGTH_LONG)
                        .setAction("^_^", null).show();
            }
        });

        Button newuserbutton = (Button) findViewById(R.id.register_login);
        newuserbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toRegisterActivity = new Intent(context, RegisterActivity.class);
                startActivity(toRegisterActivity);
            }
        });


    }


    void attemptLogin() {


        userIdView.setError(null);
        passwordView.setError(null);
        String userId = userIdView.getText().toString();
        String password = passwordView.getText().toString();

        if (userId.length() == 0) {
            userIdView.setError("Enter Email Address");
            userIdView.requestFocus();
            return;
        } else if (!Utilis.isEmailValid(userId)) {
            userIdView.setError("Enter Valid Email Address");
            userIdView.requestFocus();
            return;
        } else if (password.length() == 0) {
            passwordView.setError("Enter Password");
            passwordView.requestFocus();
            return;

        } else if (!Utilis.isPasswordValid(password)) {
            passwordView.setError("Enter Valid Password");
            passwordView.requestFocus();
            return;
        }


        //using another thread

        LogingIn logingIn = new LogingIn(userId, password);
        logingIn.execute((Void) null);

    }


    private class LogingIn extends AsyncTask<Void, Void, Boolean> {

        String[] userIds={"vegesna95@gmail.com","sunny.bodepudi@gmail.com"};
        String[] passwords = {"1234512345","1234512345"};
        String userId, password;

        LogingIn(String userId, String password) {
            this.userId = userId;
            this.password = password;
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
            ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView_login);
            scrollView.setAlpha(0.3f);
            scrollView.setClickable(false);


            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar_login);
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(Boolean loginStatus) {

            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar_login);
            progressBar.setVisibility(View.GONE);

            ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView_login);
            scrollView.setAlpha(1f);
            scrollView.setClickable(true);


            if (loginStatus) {
                Toast.makeText(LoginActivity.this, " Login Successful ", Toast.LENGTH_SHORT).show();


                SharedPreferences sharedPreferences = getSharedPreferences(
                        getString(R.string.loginDetails), Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(getString(R.string.loginStatus), true);
                editor.putString(getString(R.string.loginEmailId), userId);
                editor.commit();

                Intent toUserProfile = new Intent(LoginActivity.this, UserProfileActivty.class);
                startActivity(toUserProfile);

            } else {
                userIdView.setError("Invalid User Email or Password");
                userIdView.requestFocus();
            }
        }
    }


}
