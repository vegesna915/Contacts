package com.example.varma.contacts;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        userIdView = (AutoCompleteTextView) findViewById(R.id.userName_login);

        passwordView = (EditText) findViewById(R.id.password_login);
        passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == R.id.password_login_by_enter || actionId == EditorInfo.IME_NULL)
                onLoginButtonClicked((v));
                return true;
            }
        });

    }



    void onLoginButtonClicked(View view){

        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
        attemptLogin();

    }

    void onForgotPasswordButtonClicked(View view){
        Snackbar.make(view, "Sorry about that, we can do nothing about that", Snackbar.LENGTH_LONG)
                .setAction("^_^", null).show();

    }

    void onNewUserClicked(View view){
        Intent toRegisterActivity = new Intent(this,RegisterActivity.class);
        startActivity(toRegisterActivity);
    }

    void attemptLogin(){


        userIdView.setError(null);
        passwordView.setError(null);
        String userId = userIdView.getText().toString();
        String password = passwordView.getText().toString();

        if(userId.length()==0){
            userIdView.setError("Enter Email Address");
            userIdView.requestFocus();
            return;
        }else if(!Utilis.isEmailValid(userId)){
            userIdView.setError("Enter Valid Email Address");
            userIdView.requestFocus();
            return;
        }else if(password.length()==0){
            passwordView.setError("Enter Password");
            passwordView.requestFocus();
            return;

        }else if(!Utilis.isPasswordValid(password)){
            passwordView.setError("Enter Valid Password");
            passwordView.requestFocus();
            return;
        }


        //using another thread

        LogingIn logingIn = new LogingIn(userId,password);
        logingIn.execute((Void) null);

    }

    class LogingIn extends AsyncTask<Void,Void,Boolean>{

        String userId,password;
        LogingIn(String userId,String password){
            this.userId = userId;
            this.password = password;
        }
        @Override
        protected Boolean doInBackground(Void... params) {

            try{
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(userId.equals("vegesna95@gmail.com") ){
                return (password.equals("1234512345"));
            }
            return false;
        }

        @Override
        protected void onPreExecute() {
            ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView_login);
            scrollView.setAlpha(0.3f);


            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar_login);
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(Boolean loginStatus) {

            ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView_login);
            scrollView.setAlpha(1f);

            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar_login);
            progressBar.setVisibility(View.GONE);

            if(loginStatus){
                Toast.makeText(LoginActivity.this, " Login Successful ", Toast.LENGTH_SHORT).show();
                Intent toUserProfile = new Intent(LoginActivity.this,UserProfileActivty.class);
                toUserProfile.putExtra("from","LoginActivity");
                startActivity(toUserProfile);
            }else{
                userIdView.setError("Invalid User Email or Password");
                userIdView.requestFocus();
            }
        }
    }


}
