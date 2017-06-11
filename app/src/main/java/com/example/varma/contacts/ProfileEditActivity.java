package com.example.varma.contacts;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.varma.contacts.AsyncTasks.NewUserData;
import com.example.varma.contacts.Extra.Utils;
import com.example.varma.contacts.service.SaveProfileEditDataJobService;
import com.example.varma.contacts.service.UpdateProfileDataService;

public class ProfileEditActivity extends AppCompatActivity {

    boolean isNew;
    EditText nameUser, numberUser;
    Button saveButton;
    SharedPreferences sharedPref;
    boolean isLogin, isGoogleLogin;
    String originalNameUser, originalNumberUser;

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
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            originalNameUser = sharedPref.getString(getString(R.string.userName), "").trim();
            originalNumberUser = sharedPref.getString(getString(R.string.userNumber), "").trim();
            nameUser.setText(originalNameUser);
            numberUser.setText(originalNumberUser);

        } else {
            originalNameUser = "";
            originalNumberUser = "";
        }



        saveButton = (Button) findViewById(R.id.saveButton_editProfile);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Utils.closeKeyboard(ProfileEditActivity.this, view);
                onClickSaveButton();
            }
        });
    }

    @SuppressLint("ApplySharedPref")
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
        if (name.length() < 3) {
            nameUser.setError("Minimum length 3");
            return;
        }

        if (!Utils.isUserNameValid(name)) {
            nameUser.setError("No Special Characters Allowed");
            return;
        }

        if (name.equals(originalNameUser) && number.equals(originalNumberUser)) {
            onBackPressed();
        } else {
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putString(getString(R.string.userName), name);
            editor.putString(getString(R.string.userNumber), number);

            editor.commit();

            if (isNew) {
                NewUserData newUserData = new NewUserData(ProfileEditActivity.this);
                newUserData.execute((Void) null);
            } else {
                if (Utils.internetConnectionStatus(this)) {
                    Intent startUpdateProfileDataService = new Intent(this, UpdateProfileDataService.class);
                    startService(startUpdateProfileDataService);
                } else {
                    JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
                    JobInfo jobInfo = new JobInfo.Builder(10, new ComponentName(this, SaveProfileEditDataJobService.class))
                            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                            .build();

                    jobScheduler.schedule(jobInfo);

                }
            }

            onBackPressed();
        }

    }

}
