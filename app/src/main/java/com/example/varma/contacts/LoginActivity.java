package com.example.varma.contacts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.varma.contacts.AsyncTasks.IsNewUser;
import com.example.varma.contacts.AsyncTasks.LogingIn;
import com.example.varma.contacts.Extra.Utils;
import com.example.varma.contacts.Extra.WebServiceConnection;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    static final int Req_code = 9001;
    EditText passwordView;
    AutoCompleteTextView userIdView;
    Context context;
    GoogleApiClient googleApiClient;

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

        Button loginButton = (Button) findViewById(R.id.loginButton_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Utils.closeKeyboard(context, v);
                attemptLogin();

            }
        });

        Button forgotButton = (Button) findViewById(R.id.forgotPassword_login);
        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Sorry about that, we can do nothing about that", Snackbar.LENGTH_LONG)
                        .setAction("^_^", null).show();
            }
        });

        Button newUserButton = (Button) findViewById(R.id.register_login);
        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toRegisterActivity = new Intent(context, RegisterActivity.class);
                startActivity(toRegisterActivity);
            }
        });


        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();


        SignInButton signInButton = (SignInButton) findViewById(R.id.googleSignIn_login);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar(true);

                if (Utils.internetConnectionStatus(LoginActivity.this)) {
                    CheckConnection checkConnection = new CheckConnection();
                    checkConnection.execute((Void) null);
                } else {
                    progressBar(false);
                    Toast.makeText(context, " Unable to Access Network ", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }


    void googleLogIn() {


        Intent toGoogle = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(toGoogle, Req_code);
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
        } else if (!Utils.isEmailValid(userId)) {
            userIdView.setError("Enter Valid Email Address");
            userIdView.requestFocus();
            return;
        } else if (password.length() == 0) {
            passwordView.setError("Enter Password");
            passwordView.requestFocus();
            return;

        } else if (!Utils.isPasswordValid(password)) {
            passwordView.setError("Enter Valid Password");
            passwordView.requestFocus();
            return;
        }


        //using another thread

        LogingIn logingIn = new LogingIn(userId, password, this);
        logingIn.execute((Void) null);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        progressBar(false);
        Toast.makeText(context, " Connection Failed ", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("ApplySharedPref")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Req_code) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                if (account == null) {
                    Toast.makeText(this, "Google SignIn Failed ", Toast.LENGTH_SHORT).show();
                    progressBar(false);
                    return;
                }
                String name = account.getDisplayName();
                String email = account.getEmail();
                Uri uri = account.getPhotoUrl();
                String Id = account.getId();


                SharedPreferences sharedPreferences = getSharedPreferences(
                        getString(R.string.loginDetails), Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putBoolean(getString(R.string.loginStatus), true);
                editor.putBoolean(getString(R.string.loginIsGoogle), true);
                editor.putString(getString(R.string.loginEmail), email);
                editor.putString(getString(R.string.userName), name);
                editor.putString(getString(R.string.userGmailId), Id);

                if (uri != null) {
                    String ImgUrl = uri.toString();
                    editor.putString(getString(R.string.userImageUrl), ImgUrl);
                } else {
                    editor.putString(getString(R.string.userImageUrl), "");
                }


                editor.commit();

                signOut();

                IsNewUser isNewUser = new IsNewUser(LoginActivity.this, Id);
                isNewUser.execute((Void) null);


            } else {
                Toast.makeText(this, "Google SignIn Failed", Toast.LENGTH_SHORT).show();
            }

        }
        progressBar(false);

    }

    void progressBar(boolean visible) {

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar_login);
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView_login);
        SignInButton googleSignIn = (SignInButton) findViewById(R.id.googleSignIn_login);

        if (visible) {
            progressBar.setVisibility(View.VISIBLE);

            scrollView.setAlpha(0.3f);
            scrollView.setClickable(false);

            googleSignIn.setAlpha(0.3f);
            googleSignIn.setClickable(false);
        } else {
            progressBar.setVisibility(View.GONE);

            scrollView.setAlpha(1f);
            scrollView.setClickable(true);

            googleSignIn.setAlpha(1f);
            googleSignIn.setClickable(true);
        }
    }

    void signOut() {


        if (googleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    userIdView.setText("");
                }
            });
        }

    }


    private class CheckConnection extends AsyncTask<Void, Void, JSONObject> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected JSONObject doInBackground(Void... voids) {

            String urlString = "http://byvarma.esy.es/New/checkConnection.php";
            String parameters = "";

            return WebServiceConnection.getData(urlString, parameters);

        }

        @Override
        protected void onPostExecute(JSONObject json) {
            if (json == null) {
                Toast.makeText(context, " Server Connection Error ", Toast.LENGTH_SHORT).show();
                progressBar(false);
                return;
            }
            String check = "0";
            try {
                check = json.getString("_CHECK");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (check.equals("1")) {
                googleLogIn();
            } else {
                Toast.makeText(context, " Unable to connect to server " + check, Toast.LENGTH_SHORT).show();
                progressBar(false);
            }


        }
    }


}
