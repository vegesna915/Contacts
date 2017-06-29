package com.example.varma.contacts;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.varma.contacts.Adapters.RecyclerViewAdapterContactInfo;
import com.example.varma.contacts.Database.FriendsDb;
import com.example.varma.contacts.Extra.PermissionsClass;
import com.example.varma.contacts.Extra.Utils;
import com.example.varma.contacts.Objects.CallLogInfo;
import com.example.varma.contacts.Objects.Friend;
import com.example.varma.contacts.service.UnFriendJobService;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class FriendProfileActivity extends AppCompatActivity {

    Friend friend;
    TextView nameView, numberHomeView, emailHomeView, userIdView;
    CircularImageView circularImageView;
    Context context;
    View numberLayout, emailLayout;
    ArrayList<CallLogInfo> callLogs;
    RecyclerView recyclerView;
    RecyclerViewAdapterContactInfo adapter;
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);

        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_friendProfile);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        FriendsDb friendsDb = new FriendsDb(this);
        friend = friendsDb.getFriendById(getIntent().getStringExtra("FRIEND_ID"));

        if (friend == null || friend.get_NAME() == null) {
            Toast.makeText(context, "sorry, you have been unfriended", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        declarations();
        assignValues();
        listeners();

        getCallLogs();

        adapter = new RecyclerViewAdapterContactInfo(callLogs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        getAd();

    }

    private void getAd() {

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);
    }

    void declarations() {
        nameView = (TextView) findViewById(R.id.nameView_friend);
        circularImageView = (CircularImageView) findViewById(R.id.profileIcon_friend);
        numberHomeView = (TextView) findViewById(R.id.user_phoneNumberHome);
        emailHomeView = (TextView) findViewById(R.id.user_emailHome);
        numberLayout = findViewById(R.id.layout_numberHome);
        emailLayout = findViewById(R.id.layout_emailHome);
        recyclerView = (RecyclerView) findViewById(R.id.friendCallLog);
        userIdView = (TextView) findViewById(R.id.userIdView_friend);
        adView = (AdView) findViewById(R.id.adViewBanner_friendProfile);
    }

    void assignValues() {

        nameView.setText(friend.get_NAME());
        numberHomeView.setText(friend.get_NUMBER());
        emailHomeView.setText(friend.get_EMAIL());
        String userId = "#" + friend.getUSER_ID();
        userIdView.setText(userId);
        if (!friend.getIMAGE_URL().equals("") && Utils.internetConnectionStatus(this)) {
            Glide.with(this).load(friend.getIMAGE_URL()).dontAnimate().into(circularImageView);
        }

    }

    void listeners() {
        numberLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (PermissionsClass.hasPermissionCallPhone(context)) {

                    String number = friend.get_NUMBER();
                    number = "tel:" + number;

                    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
                    try {
                        context.startActivity(callIntent);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }


                } else {
                    PermissionsClass.requestPermission((Activity) context,
                            new String[]{PermissionsClass.CallPhone}, 1001);
                }
            }
        });

        emailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{friend.get_EMAIL()});

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    finish();

                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(FriendProfileActivity.this, "There is no Email Sending App Installed.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1001: {
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        String number = friend.get_NUMBER();
                        number = "tel:" + number;

                        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
                        try {
                            context.startActivity(callIntent);
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(this, "Grant Permission to Call", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }


            default: {

                break;
            }
        }

    }

    void getCallLogs() {
        callLogs = new ArrayList<>();
        CallLogInfo callLog;

        ContentResolver contentResolver = getContentResolver();

        try {


            Uri uri = CallLog.Calls.CONTENT_URI;


            String[] columnsNumber = {
                    CallLog.Calls.CACHED_NAME,
                    CallLog.Calls.NUMBER,
                    CallLog.Calls.DATE,
                    CallLog.Calls.TYPE,
                    CallLog.Calls.DURATION
            };


            String where = " REPLACE(REPLACE(" + CallLog.Calls.NUMBER + ",' ',''),'+91','') IN (?,?)";


            String[] selectionArgs = new String[]{friend.get_NUMBER(), "0" + friend.get_NUMBER()};


            String sortingOrder = CallLog.Calls.DATE + " DESC ";

            Cursor cursorCallLog = contentResolver.query(uri, columnsNumber, where, selectionArgs, sortingOrder);


            if (cursorCallLog != null) {
                while (cursorCallLog.moveToNext()) {
                    callLog = new CallLogInfo();

                    callLog.setCallerName(cursorCallLog.getString(
                            cursorCallLog.getColumnIndex(CallLog.Calls.CACHED_NAME)));
                    callLog.setCallernumber(
                            cursorCallLog.getString(cursorCallLog.getColumnIndex(CallLog.Calls.NUMBER)));
                    callLog.setCalldate(
                            cursorCallLog.getString(cursorCallLog.getColumnIndex(CallLog.Calls.DATE)));
                    callLog.setCallType(
                            cursorCallLog.getString(cursorCallLog.getColumnIndex(CallLog.Calls.TYPE)));
                    callLog.setCallDuration(
                            cursorCallLog.getString(cursorCallLog.getColumnIndex(CallLog.Calls.DURATION)));
                    callLogs.add(callLog);

                }
                cursorCallLog.close();
            }

        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.friend_profile_main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.unFriendMenuItem: {
                AlertDialog.Builder builder = new AlertDialog.Builder(FriendProfileActivity.this);
                builder.setCancelable(true)
                        .setTitle("Unfriend")
                        .setMessage("Are you sure you want to unfriend " + friend.get_NAME() + "?")
                        .setPositiveButton("unfriend", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PersistableBundle bundle = new PersistableBundle();
                                bundle.putString(UnFriendJobService.INPUT_FRIEND_ID, friend.get_ID());

                                JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

                                int jobId = Integer.parseInt(friend.get_ID());
                                JobInfo jobInfo = new JobInfo.Builder(jobId, new ComponentName(FriendProfileActivity.this, UnFriendJobService.class))
                                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                                        .setExtras(bundle)
                                        .build();

                                jobScheduler.schedule(jobInfo);
                                FriendsDb friendDb = new FriendsDb(FriendProfileActivity.this);
                                friendDb.unFriend(friend.get_ID());
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create().show();

                return true;
            }

            default: {
                return super.onOptionsItemSelected(menuItem);
            }
        }
    }
}
