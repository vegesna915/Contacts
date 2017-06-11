package com.example.varma.contacts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.varma.contacts.Extra.NavigationViewHandler;
import com.example.varma.contacts.Extra.Utils;
import com.mikhaellopez.circularimageview.CircularImageView;

public class UserProfileActivity extends AppCompatActivity {


    SharedPreferences sharedPref;
    DrawerLayout drawerLayout;
    boolean isLogin, isGoogleLogin, isFirstResume;
    TextView userNumberHomeView, userEmailHomeView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_activty);
        isFirstResume = true;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_userProfile);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutUserProfile);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //--------------------------------------------------------------
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_userProfile);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toEditProfile = new Intent(UserProfileActivity.this, ProfileEditActivity.class);
                startActivity(toEditProfile);
            }
        });

        //--------------------------------------------------------------
        userEmailHomeView = (TextView) findViewById(R.id.user_emailHome);
        userNumberHomeView = (TextView) findViewById(R.id.user_phoneNumberHome);

        updateData();


        //---------------------------------------------------------------
        navigationView();

        //---------------------------------------------------------------


        //------------------------------------------------------------------
    }

    void updateData() {
        sharedPref = getSharedPreferences(getString(R.string.loginDetails), Context.MODE_PRIVATE);
        isLogin = sharedPref.getBoolean(getString(R.string.loginStatus), false);
        isGoogleLogin = sharedPref.getBoolean(getString(R.string.loginIsGoogle), false);


        if (isLogin) {

            userEmailHomeView.setText(sharedPref.getString(getString(R.string.loginEmail), "EmailAddress"));
            userNumberHomeView.setText(sharedPref.getString(getString(R.string.userNumber), "9999999999"));

            CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbarLayout_userProfile);
            toolbarLayout.setTitle(sharedPref.getString(getString(R.string.userName), "Profile"));

        }

    }

    @SuppressLint("SetTextI18n")
    void navigationView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_userProfile);
        navigationView.setNavigationItemSelectedListener(
                new NavigationViewHandler(this, this, R.string.UserProfileActivity));

        Menu menu = navigationView.getMenu();
        MenuItem logOutItem = menu.findItem(R.id.navItemLogOut);
        logOutItem.setVisible(isLogin);

        View navHeaderView = navigationView.getHeaderView(0);
        FrameLayout navHeaderLayout = (FrameLayout) navHeaderView.findViewById(R.id.nav_header_home);
        TextView navHeaderTextView = (TextView) navHeaderLayout.findViewById(R.id.textview_navheader_home);
        CircularImageView navHeaderProfilePic = (CircularImageView) navHeaderLayout.findViewById(R.id.imageview_navheader_home);
        navHeaderProfilePic.setImageResource(R.drawable.ic_account_circle);
        if (isLogin) {
            navHeaderTextView.setText("Click here to see Profile");
            if (isGoogleLogin && Utils.internetConnectionStatus(this)) {

                String imgUrl = sharedPref.getString(getString(R.string.userImageUrl), "");

                if (!imgUrl.equals("")) {

                    Glide.with(this).load(imgUrl).dontAnimate().into(navHeaderProfilePic);

                }


            }


        } else {
            navHeaderTextView.setText("Click here to Login");
        }
        navHeaderLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!isLogin) {
                    Intent toLoginActivity = new Intent(UserProfileActivity.this, LoginActivity.class);
                    startActivity(toLoginActivity);
                }


                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayoutUserProfile);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirstResume) {
            updateData();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isFirstResume = false;
    }
}
