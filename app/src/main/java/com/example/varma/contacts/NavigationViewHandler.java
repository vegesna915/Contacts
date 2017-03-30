package com.example.varma.contacts;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by Varma on 3/27/2017.
 */

public class NavigationViewHandler {


    static void onNavItemClicked(Context context, Activity activity, MenuItem item,String runningActivityName){

        ViewPager viewPager = (ViewPager) activity.findViewById(R.id.viewpager_home);

        switch(item.getItemId()){
            case R.id.navItemContacts:{
                if(runningActivityName != "HomeActivity"){
                    Intent toHomeActivity = new Intent(context,HomeActivity.class);
                    activity.startActivity(toHomeActivity);
                }else{

                }
                viewPager.setCurrentItem(1,true);
                break;
            }
            case R.id.navItemCallLog:{
                viewPager.setCurrentItem(2,true);
                break;
            }
            case R.id.navItemFriends:{
                viewPager.setCurrentItem(0,true);
                break;
            }
            case R.id.navItemRequestStatus:{
                break;
            }
            case R.id.navItemSearch:{
                break;
            }
            case R.id.navItemSettings:{
                break;
            }
            case R.id.nav_about:{
                break;
            }
            case R.id.nav_share:{
                break;
            }
            case R.id.nav_feedback:{
                break;
            }
            case R.id.nav_rate:{
                break;
            }



        }

    }

}
