package com.example.varma.contacts;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    FloatingActionButton fab;
    SharedPreferences sharedPref;
    boolean isLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        //setting up toolbar

        sharedPref = getSharedPreferences(getString(R.string.loginDetails), Context.MODE_PRIVATE);
        isLogin = sharedPref.getBoolean(getString(R.string.loginStatus), false);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        //for opening and closing navigation bar
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutHome);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        navigationView();
        //handling tabView and viewPager


        tabLayout = (TabLayout) findViewById(R.id.tabbar_home);
        viewPager = (ViewPager) findViewById(R.id.viewpager_home);


        //setting tab titles

        final List<String> tabTitles = new ArrayList<>();
        tabTitles.add("CallLog");
        tabTitles.add("Contacts");
        tabTitles.add("Friends");


        tabLayout.addTab(tabLayout.newTab().setText(tabTitles.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(tabTitles.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(tabTitles.get(2)));

        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton_home);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabClick();
            }
        });


        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment_1());
        fragments.add(new HomeFragment_2());

        if (isLogin) {
            //fragments.add(new HomeFragment_3());
            fragments.add(new HomeFragment_3());
        } else {
            fragments.add(new HomeFragment_3_notLogedIn());
        }


        viewPager.setOffscreenPageLimit(2);
        FragmentAdapter_Home fragmentAdapter = new FragmentAdapter_Home(getSupportFragmentManager(), tabTitles, fragments);
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(fragmentAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0: {
                        fab.setImageResource(R.drawable.ic_dialpad);
                        break;
                    }
                    case 1: {
                        fab.setImageResource(R.drawable.ic_fab_add);
                        break;
                    }
                    case 2: {

                        fab.setImageResource(R.drawable.ic_fab_add);

                    }
                }

                if(getSupportActionBar() != null){
                    getSupportActionBar().setTitle(tabTitles.get(position));
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        NavigationViewHandler.onNavItemClicked(HomeActivity.this, this, item);
        Toast.makeText(this, item.getTitle() + " - is Selected", Toast.LENGTH_SHORT).show();
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    void fabClick() {
        switch (viewPager.getCurrentItem()) {

            case 0: {
                break;
            }
            case 1: {
                Intent toAddContact = new Intent(ContactsContract.Intents.Insert.ACTION);
                toAddContact.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                startActivityForResult(toAddContact, 11);
                break;
            }
            case 2: {


                break;
            }
            default: {
                break;
            }

        }
    }

    void navigationView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navViewHome);
        navigationView.setNavigationItemSelectedListener(this);


        Menu menu = navigationView.getMenu();
        MenuItem logOutItem = menu.findItem(R.id.navItemLogOut);
        logOutItem.setVisible(isLogin);


        //Handling  click on NavigationViewHeader
        View navHeaderView = navigationView.getHeaderView(0);
        LinearLayout navHeaderLayout = (LinearLayout) navHeaderView.findViewById(R.id.nav_header_home);
        TextView navHeaderTextView = (TextView) navHeaderLayout.findViewById(R.id.textview_navheader_home);
        ImageView navHeaderProfilePic = (ImageView) navHeaderLayout.findViewById(R.id.imageview_navheader_home);
        navHeaderProfilePic.setImageResource(R.drawable.ic_account_circle);
        if(isLogin){
            navHeaderTextView.setText("Click here to see User Profile");
            if(sharedPref.getBoolean(getString(R.string.loginIsGoogle),false)&&Utilis.internetConnectionStatus(this)){

                String imgUrl = sharedPref.getString(getString(R.string.userImageUrl),"");

                if(!imgUrl.equals("")){

                    Glide.with(this).load(imgUrl).dontAnimate().into(navHeaderProfilePic);

                }


            }


        }else{
            navHeaderTextView.setText("Click here to Login");
        }
        navHeaderLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (isLogin) {
                    Intent toUserProfile = new Intent(HomeActivity.this, UserProfileActivty.class);
                    startActivity(toUserProfile);
                } else {
                    Intent toLoginActivity = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(toLoginActivity);
                }


                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayoutHome);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
    }
}
