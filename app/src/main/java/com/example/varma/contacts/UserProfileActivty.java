package com.example.varma.contacts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class UserProfileActivty extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_activty);

    }

    @Override
    public void onBackPressed() {
        if(((String) (getIntent().getStringExtra("from"))).equals("LoginActivity") ){
            Intent toHomeActivity = new Intent(this,HomeActivity.class);
            startActivity(toHomeActivity);
        }else{
            super.onBackPressed();
        }


    }
}
