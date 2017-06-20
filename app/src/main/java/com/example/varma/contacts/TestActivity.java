package com.example.varma.contacts;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.varma.contacts.Database.FriendsDb;


public class TestActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        Button button = (Button) findViewById(R.id.button_test);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView textView = (TextView) findViewById(R.id.text_test);
                FriendsDb friendsDb = new FriendsDb(TestActivity.this);
                textView.setText("Friend Count" + friendsDb.getFriendsCount());
            }
        });


    }
}
