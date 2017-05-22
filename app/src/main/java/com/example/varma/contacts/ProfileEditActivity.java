package com.example.varma.contacts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.varma.contacts.AsyncTasks.NewUserData;
import com.example.varma.contacts.Extra.Utilis;

public class ProfileEditActivity extends AppCompatActivity {

    boolean isNew;
    EditText nameUser, numberUser;
    Button saveButton;
    SharedPreferences sharedPref;
    boolean isLogin, isGoogleLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        isNew = getIntent().getBooleanExtra("isNew", false);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_editProfile);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Edit Profile");
        }

        sharedPref = getSharedPreferences(getString(R.string.loginDetails), Context.MODE_PRIVATE);
        isLogin = sharedPref.getBoolean(getString(R.string.loginStatus), false);
        isGoogleLogin = sharedPref.getBoolean(getString(R.string.loginIsGoogle), false);


        nameUser = (EditText) findViewById(R.id.nameUser_editProfile);
        numberUser = (EditText) findViewById(R.id.numberUser_editProfile);

        if (isLogin) {

            nameUser.setText(sharedPref.getString(getString(R.string.userName), ""));
            numberUser.setText(sharedPref.getString(getString(R.string.userNumber), ""));


        }


        saveButton = (Button) findViewById(R.id.saveButton_editProfile);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Utilis.closeKeyboard(ProfileEditActivity.this, view);
                onClickSaveButton();
            }
        });
    }

    void onClickSaveButton() {

        String name, number;

        name = nameUser.getText().toString().trim();
        number = numberUser.getText().toString().trim();

        if (name.equals("")) {
            nameUser.setError("Enter your Name");
            return;
        }
        if (number.equals("")) {
            numberUser.setError("Enter your number");
            return;
        }

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(getString(R.string.userName), name);
        editor.putString(getString(R.string.userNumber), number);

        editor.commit();

        if (isNew) {
            NewUserData newUserData = new NewUserData(ProfileEditActivity.this);
            newUserData.execute((Void) null);

        }
        onBackPressed();


    }
}
