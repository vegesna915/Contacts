package com.example.varma.contacts;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;


class NavigationViewHandler {


    static void onNavItemClicked(Context context, Activity activity, MenuItem item) {

        ViewPager viewPager = (ViewPager) activity.findViewById(R.id.viewpager_home);

        switch (item.getItemId()) {
            case R.id.navItemContacts: {

                viewPager.setCurrentItem(1, true);
                break;
            }
            case R.id.navItemCallLog: {
                viewPager.setCurrentItem(0, true);
                break;
            }
            case R.id.navItemFriends: {
                viewPager.setCurrentItem(2, true);
                break;
            }
            case R.id.navItemRequestStatus: {
                break;
            }
            case R.id.navItemSearch: {
                break;
            }
            case R.id.navItemSettings: {
                break;
            }
            case R.id.nav_about: {
                break;
            }
            case R.id.nav_share: {
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
                editor.putString(context.getString(R.string.loginEmailId), "");
                editor.commit();
                Intent toHomeActivity = new Intent(context, HomeActivity.class);
                context.startActivity(toHomeActivity);
                break;
            }


        }

    }

}
