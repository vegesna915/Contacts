package com.example.varma.contacts;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.varma.contacts.Database.DatabaseHelper;

public class TestActivty extends AppCompatActivity {

    TextView textView;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_activty);


        textView = (TextView) findViewById(R.id.textView_Test);
        button = (Button) findViewById(R.id.button_test);


        final DatabaseHelper databaseHelper = new DatabaseHelper(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPref = getSharedPreferences(
                        getString(R.string.loginDetails), Context.MODE_PRIVATE);
                String googleId = sharedPref.getString(getString(R.string.userGmailId), "");
                String name = sharedPref.getString(getString(R.string.userName), "");
                String number = sharedPref.getString(getString(R.string.userNumber), "");
                String email = sharedPref.getString(getString(R.string.loginEmail), "");
                String _id = sharedPref.getString(getString(R.string.userDatabaseId), "");

                textView.setText(_id + " " + name + " " + email + " " + number + " \n" + googleId);


            }
        });


    }

}
