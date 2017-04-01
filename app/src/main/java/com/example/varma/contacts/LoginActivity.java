package com.example.varma.contacts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    EditText passwordView;
    AutoCompleteTextView userIdView;
    Context context;

    GoogleApiClient googleApiClient;
    static final int Req_code = 9001;




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


        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();



        SignInButton signInButton = (SignInButton) findViewById(R.id.googleSignIn_login);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                googleLogIn();
            }
        });






    }



    void googleLogIn(){


        Intent toGoogle = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(toGoogle,Req_code);
        googleApiClient.connect();
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

        LogingIn logingIn = new LogingIn(userId, password,this);
        logingIn.execute((Void) null);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(context, " Connection Failed " , Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Req_code){

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                String name = account.getDisplayName();
                String email = account.getEmail();
                Uri uri = account.getPhotoUrl();
                String Id = account.getId();

                SharedPreferences sharedPreferences = getSharedPreferences(
                        getString(R.string.loginDetails), Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putBoolean(getString(R.string.loginStatus),true);
                editor.putBoolean(getString(R.string.loginIsGoogle),true);
                editor.putString(getString(R.string.loginEmailId),email);
                editor.putString(getString(R.string.userName),name);
                editor.putString(getString(R.string.userGmailId),Id);

                if(uri != null){
                    String ImgUrl = uri.toString();
                    editor.putString(getString(R.string.userImageUrl),ImgUrl);
                }else{
                    editor.putString(getString(R.string.userImageUrl),"");
                }


                editor.commit();
                userIdView.setText(name);

                signOut();
                Intent toUserProfile = new Intent(this, UserProfileActivty.class);
                startActivity(toUserProfile);


            }else{
                Toast.makeText(this, "Google SignIn Failed", Toast.LENGTH_SHORT).show();
            }

        }


    }

    void signOut(){


        if(googleApiClient.isConnected()){
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    userIdView.setText("");
                }
            });
        }

    }
}
