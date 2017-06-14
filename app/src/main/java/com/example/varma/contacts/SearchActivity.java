package com.example.varma.contacts;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.varma.contacts.AsyncTasks.SendRequest;
import com.example.varma.contacts.Extra.Utils;
import com.example.varma.contacts.Extra.WebServiceConnection;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

public class SearchActivity extends AppCompatActivity {
    String email;
    EditText emailFriendView;
    Activity activity;
    CardView cardViewSearch, cardViewDetails;
    ProgressBar progressBar;
    View layout;
    CircularImageView circleImageView;
    TextView userNameText;
    Button sendRequestButton;
    String userId, userName, userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        activity = this;

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.loginDetails), Context.MODE_PRIVATE);
        email = sharedPref.getString(getString(R.string.loginEmail), "");

        progressBar = (ProgressBar) findViewById(R.id.progressBar_search);
        emailFriendView = (EditText) findViewById(R.id.emailFriend_search);
        cardViewSearch = (CardView) findViewById(R.id.cardView_search);
        cardViewDetails = (CardView) findViewById(R.id.cardView_userDetails_search);
        Button searchButton = (Button) findViewById(R.id.searchButton_search);
        layout = findViewById(R.id.Layout_search);
        circleImageView = (CircularImageView) findViewById(R.id.userImage_search);
        userNameText = (TextView) findViewById(R.id.userName_search);
        sendRequestButton = (Button) findViewById(R.id.sendRequest);


        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        cardViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.closeKeyboard(activity, view);
            }
        });

        cardViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.closeKeyboard(activity, view);

                String enteredEmail = emailFriendView.getText().toString();

                if (enteredEmail.equals("")) {
                    emailFriendView.setError("Enter Email Address");
                    return;
                }
                if (email.equals(enteredEmail)) {
                    emailFriendView.setError("That's your Email Address");
                    return;
                }

                if (Utils.internetConnectionStatus(SearchActivity.this)) {
                    CheckConnection checkConnection = new CheckConnection();
                    checkConnection.execute(enteredEmail);

                } else {
                    Toast.makeText(SearchActivity.this, " Unable to Access Network ", Toast.LENGTH_SHORT).show();
                }

            }
        });

        sendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendRequest sendRequest = new SendRequest(activity, userId, userName, userImage);
                sendRequest.execute((Void) null);
            }
        });
    }


    private class CheckConnection extends AsyncTask<String, Void, JSONObject> {


        @Override
        protected void onPreExecute() {
            layout.setClickable(false);
            cardViewSearch.setAlpha(0.7f);
            cardViewSearch.setClickable(false);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONObject doInBackground(String... strings) {

            String urlString = "http://byvarma.esy.es/New/getUserDetails.php";
            String parameters = "_EMAIL=" + strings[0];

            return WebServiceConnection.getData(urlString, parameters);

        }

        @Override
        protected void onPostExecute(JSONObject json) {

            if (json == null) {
                Toast.makeText(activity, " Server  Connection Error ", Toast.LENGTH_SHORT).show();
                layout.setClickable(true);
                cardViewSearch.setAlpha(1f);
                cardViewSearch.setClickable(true);
                progressBar.setVisibility(View.GONE);
                return;
            }
            String _EXISTS = "0";
            String _ID = "";
            String _NAME = "";
            String IMAGE_URL = "";
            try {
                _EXISTS = json.getString("_EXISTS");
                if (_EXISTS != null && _EXISTS.equals("1")) {
                    _ID = json.getString("_ID");
                    _NAME = json.getString("_NAME");
                    IMAGE_URL = json.getString("IMAGE_URL");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (_EXISTS != null && _EXISTS.equals("1")) {

                    cardViewSearch.setVisibility(View.GONE);
                    cardViewDetails.setVisibility(View.VISIBLE);

                    userNameText.setText(_NAME);
                    if (!IMAGE_URL.equals("")) {
                        Glide.with(activity).load(IMAGE_URL).dontAnimate().into(circleImageView);
                    }
                    userId = _ID;
                    userName = _NAME;
                    userImage = IMAGE_URL;


                } else {
                    Toast.makeText(activity, " Entered Email is Not Registered ", Toast.LENGTH_SHORT).show();
                }

                layout.setClickable(true);
                cardViewSearch.setAlpha(1f);
                cardViewSearch.setClickable(true);
                progressBar.setVisibility(View.GONE);
            }


        }
    }


}
