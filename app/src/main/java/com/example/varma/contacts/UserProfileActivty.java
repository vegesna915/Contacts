package com.example.varma.contacts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivty extends AppCompatActivity {


    SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_activty);

        sharedPref = getSharedPreferences(getString(R.string.loginDetails), Context.MODE_PRIVATE);


        CircleImageView profileIconView = (CircleImageView) findViewById(R.id.imageIcon_UserProfile);
        TextView userNameView = (TextView) findViewById(R.id.userName_profile);
        TextView userEmailView = (TextView) findViewById(R.id.userEmail_profile);

        if (sharedPref.getBoolean(getString(R.string.loginStatus), false)) {

            userEmailView.setText(sharedPref.getString(getString(R.string.loginEmailId), "User Email Address"));
            userNameView.setText(sharedPref.getString(getString(R.string.userName), "User Name"));

            if (sharedPref.getBoolean(getString(R.string.loginIsGoogle), false) && Utilis.internetConnectionStatus(this)) {

                String imgUrl = sharedPref.getString(getString(R.string.userImageUrl), "");

                if (!imgUrl.equals("")) {

                    Glide.with(this).load(imgUrl).dontAnimate().into(profileIconView);

                }
            }


        }
    }

    @Override
    public void onBackPressed() {

        Intent toHomeActivity = new Intent(this, HomeActivity.class);
        startActivity(toHomeActivity);


    }
}
