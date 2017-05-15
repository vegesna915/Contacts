package com.example.varma.contacts.Extra;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.varma.contacts.Database.FriendsDb;
import com.example.varma.contacts.Database.RequestsDb;
import com.example.varma.contacts.HomeActivity;
import com.example.varma.contacts.LoginActivity;
import com.example.varma.contacts.R;
import com.example.varma.contacts.RequestsActivity;
import com.example.varma.contacts.SearchActivity;
import com.example.varma.contacts.TestActivty;


public class NavigationViewHandler implements NavigationView.OnNavigationItemSelectedListener {

    private Context context;
    private Activity activity;
    private int activityName;


    public NavigationViewHandler(Context context, Activity activity, int activityName) {
        this.context = context;
        this.activity = activity;
        this.activityName = activityName;

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()) {

            case R.id.navItemHome: {
                switch (activityName) {
                    case R.string.HomeActivity: {

                        break;
                    }
                    case R.string.UserProfileActivity: {
                        Intent toHomeActivity = new Intent(context, HomeActivity.class);
                        toHomeActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        toHomeActivity.putExtra(context.getString(R.string.putExtraPage_HomeActivity), 0);
                        context.startActivity(toHomeActivity);
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
            }


            case R.id.navItemRequests: {

                SharedPreferences sharedPref = activity.getSharedPreferences(
                        activity.getString(R.string.loginDetails), Context.MODE_PRIVATE);
                if (sharedPref.getBoolean(activity.getString(R.string.loginStatus), false)) {
                    Intent toRequestsActivity = new Intent(activity, RequestsActivity.class);
                    activity.startActivity(toRequestsActivity);
                } else {
                    Toast.makeText(context, "Login to see Requests", Toast.LENGTH_SHORT).show();
                }


                break;
            }
            case R.id.navItemSearch: {
                SharedPreferences sharedPref = activity.getSharedPreferences(
                        activity.getString(R.string.loginDetails), Context.MODE_PRIVATE);
                if (sharedPref.getBoolean(activity.getString(R.string.loginStatus), false)) {
                    Intent toSearchActivity = new Intent(context, SearchActivity.class);
                    context.startActivity(toSearchActivity);
                } else {
                    Toast.makeText(context, "Login to add Friends", Toast.LENGTH_SHORT).show();
                }

                break;
            }
            case R.id.navItemSettings: {
                break;
            }
            case R.id.nav_about: {
                break;
            }
            case R.id.nav_share: {

                Intent toTestActivity = new Intent(context, TestActivty.class);
                context.startActivity(toTestActivity);
                break;
            }
            case R.id.nav_feedback: {
                break;
            }
            case R.id.nav_rate: {
                break;
            }
            case R.id.navItemLogOut: {

                SharedPreferences sharedPreferences = context.getSharedPreferences(
                        context.getString(R.string.loginDetails), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putBoolean(context.getString(R.string.loginStatus), false);
                editor.putBoolean(context.getString(R.string.loginIsGoogle), false);
                editor.putString(context.getString(R.string.loginEmail), "");
                editor.putString(context.getString(R.string.userName), "");
                editor.putString(context.getString(R.string.userGmailId), "");
                editor.putString(context.getString(R.string.userNumber), "");

                FriendsDb friendsDb = new FriendsDb(activity);
                friendsDb.deleteAllFriends();

                RequestsDb requestsDb = new RequestsDb(activity);
                requestsDb.deleteAllRequests();

                editor.apply();

                switch (activityName) {
                    case R.string.HomeActivity: {

                        Intent toHomeActivity = new Intent(context, HomeActivity.class);
                        toHomeActivity.putExtra(activity.getString(R.string.putExtraPage_HomeActivity), 1);
                        toHomeActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(toHomeActivity);
                        break;
                    }
                    case R.string.UserProfileActivity: {
                        Intent toLoginActivity = new Intent(context, LoginActivity.class);
                        context.startActivity(toLoginActivity);
                        break;
                    }
                    default: {
                        break;
                    }
                }


                break;
            }


        }
        if (activityName == R.string.HomeActivity) {
            DrawerLayout drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawerLayoutHome);
            drawerLayout.closeDrawer(GravityCompat.START);

        } else if (activityName == R.string.UserProfileActivity) {
            DrawerLayout drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawerLayoutUserProfile);
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        Toast.makeText(context, item.getTitle() + " - is Selected", Toast.LENGTH_SHORT).show();


        return true;
    }
}
