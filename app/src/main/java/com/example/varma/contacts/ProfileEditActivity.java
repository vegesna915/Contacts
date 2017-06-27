package com.example.varma.contacts;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.varma.contacts.AsyncTasks.NewUserData;
import com.example.varma.contacts.Extra.Utils;
import com.example.varma.contacts.Extra.WebServiceConnection;
import com.example.varma.contacts.service.SaveProfileEditDataJobService;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileEditActivity extends AppCompatActivity {

    boolean isNew, isUserIdValid;
    EditText nameUser, numberUser, userIdView;
    Button saveButton, checkUserIdButton;
    ImageView statusUserId;
    SharedPreferences sharedPref;
    String originalNameUser, originalNumberUser, originalUserId;
    ProgressBar progressBar;

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


        declarations();
        originalNameUser = sharedPref.getString(getString(R.string.userName), "");
        originalNumberUser = sharedPref.getString(getString(R.string.userNumber), "");
        originalUserId = sharedPref.getString(getString(R.string.userId), "");


        if (isNew) {
            checkUserIdButton.setVisibility(View.VISIBLE);
            userIdView.setEnabled(true);
            isUserIdValid = false;
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putBoolean(getString(R.string.loginStatus), false);


            editor.apply();
        } else {
            isUserIdValid = true;
            checkUserIdButton.setVisibility(View.GONE);
            userIdView.setEnabled(false);
        }


        nameUser.setText(originalNameUser);
        numberUser.setText(originalNumberUser);
        userIdView.setText(originalUserId);

        checkUserIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newUserId = userIdView.getText().toString().trim();

                if (!Utils.internetConnectionStatus(ProfileEditActivity.this)) {
                    Toast.makeText(ProfileEditActivity.this, "No Network Connection", Toast.LENGTH_SHORT).show();
                }

                if (newUserId.equals(originalUserId)) {
                    return;
                } else {
                    originalUserId = newUserId;
                }

                String errorMsg = Utils.isUserIdValid(newUserId);

                if (!errorMsg.equals("")) {
                    userIdView.setError(errorMsg);
                    return;
                }

                CheckUserId checkUserId = new CheckUserId();
                checkUserId.execute((Void) null);


            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("userid", originalUserId + "--" + userIdView.getText().toString().trim());
                if (!originalUserId.equals(userIdView.getText().toString().trim())) {
                    userIdView.setError("Check Availability");
                    return;
                }
                if (!isUserIdValid) {
                    userIdView.setError("UserId Invalid");
                    return;
                }
                Log.i("userid", "onClickSaveButton");
                Utils.closeKeyboard(ProfileEditActivity.this, view);
                onClickSaveButton();
            }
        });

        View view = findViewById(R.id.layout_profileEdit);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.closeKeyboard(ProfileEditActivity.this, v);
            }
        });


    }

    private void declarations() {
        nameUser = (EditText) findViewById(R.id.nameUser_editProfile);
        numberUser = (EditText) findViewById(R.id.numberUser_editProfile);
        userIdView = (EditText) findViewById(R.id.userId_editProfile);
        saveButton = (Button) findViewById(R.id.saveButton_editProfile);
        checkUserIdButton = (Button) findViewById(R.id.checkButton_userId_ProfileEdit);
        statusUserId = (ImageView) findViewById(R.id.checkStatus_imageView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_checkUserId);
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
            editor.putString(getString(R.string.userId), originalUserId);

            editor.commit();

            if (isNew) {
                NewUserData newUserData = new NewUserData(ProfileEditActivity.this);
                newUserData.execute((Void) null);
                SharedPreferences.Editor editor1 = sharedPref.edit();
                editor1.putBoolean(getString(R.string.loginStatus), true);
                editor1.commit();
                isNew = false;
            } else {

                JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
                JobInfo jobInfo = new JobInfo.Builder(10, new ComponentName(this, SaveProfileEditDataJobService.class))
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        .build();

                jobScheduler.schedule(jobInfo);

            }

            onBackPressed();
        }

    }

    @SuppressLint("ApplySharedPref")
    @Override
    public void onBackPressed() {
        if (isNew) {

            SharedPreferences sharedPreferences = getSharedPreferences(
                    getString(R.string.loginDetails), Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putBoolean(getString(R.string.loginStatus), false);

            editor.commit();

            Intent toLoginActivity = new Intent(this, LoginActivity.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

            // Adds the back stack
            stackBuilder.addParentStack(LoginActivity.class);
            // Adds the Intent to the top of the stack
            stackBuilder.addNextIntent(toLoginActivity);
            // Gets a PendingIntent containing the entire back stack
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            try {
                resultPendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        } else {
            super.onBackPressed();
        }

    }

    private class CheckUserId extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            checkUserIdButton.setClickable(false);
            statusUserId.setVisibility(View.GONE);

        }

        @Override
        protected Boolean doInBackground(Void... params) {

            boolean isUserIdUnique = false;

            try {

                String url = "http://byvarma.esy.es/New/checkUserId.php";
                String parameters = "USER_ID=" + originalUserId;

                JSONObject json = WebServiceConnection.getData(url, parameters);

                isUserIdUnique = json.getBoolean("USER_ID_UNIQUE");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return isUserIdUnique;

        }

        @Override
        protected void onPostExecute(Boolean isCheck) {

            progressBar.setVisibility(View.GONE);
            checkUserIdButton.setClickable(true);
            statusUserId.setVisibility(View.VISIBLE);

            if (isCheck) {
                statusUserId.setImageResource(R.drawable.ic_check_tick_icon);
                isUserIdValid = true;
            } else {
                statusUserId.setImageResource(R.drawable.ic_red_cross_tick);
                isUserIdValid = false;
            }


        }
    }
}
