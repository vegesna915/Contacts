package com.example.varma.contacts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    TextView appNameView, appVersionNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_aboutActivity);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        appNameView = (TextView) findViewById(R.id.appName_aboutActivity);
        appVersionNameView = (TextView) findViewById(R.id.versionName_aboutActivity);


        appVersionNameView.setText(BuildConfig.VERSION_NAME);


    }
}
