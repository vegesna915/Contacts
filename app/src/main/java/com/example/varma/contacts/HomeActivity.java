package com.example.varma.contacts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.varma.contacts.Adapters.FragmentAdapter_Home;
import com.example.varma.contacts.Extra.NavigationViewHandler;
import com.example.varma.contacts.Extra.PermissionsClass;
import com.example.varma.contacts.Extra.Utils;
import com.example.varma.contacts.Fragments.HomeFragment_1;
import com.example.varma.contacts.Fragments.HomeFragment_2;
import com.example.varma.contacts.Fragments.HomeFragment_3;
import com.example.varma.contacts.Fragments.HomeFragment_3_notLogedIn;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    FloatingActionButton fab;
    SharedPreferences sharedPref;
    List<Fragment> fragments;
    HomeFragment_1 homeFragment1;
    HomeFragment_2 homeFragment2;
    HomeFragment_3 homeFragment3;
    HomeFragment_3_notLogedIn homeFragment_3_notLogedIn;
    MenuItem menuItem;
    SearchView searchView;
    int page;
    boolean isFirstResume, isLogin, readCallLog, readContacts;
    FragmentAdapter_Home fragmentAdapter;
    List<String> tabTitles;
    NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //setting up toolbar
        isFirstResume = true;
        page = getIntent().getIntExtra(getString(R.string.putExtraPage_HomeActivity), 1);
        sharedPref = getSharedPreferences(getString(R.string.loginDetails), Context.MODE_PRIVATE);
        isLogin = sharedPref.getBoolean(getString(R.string.loginStatus), false);
        readCallLog = PermissionsClass.hasPermissionReadCallLog(this);
        readContacts = PermissionsClass.hasPermissionReadContacts(this);

        declareViews();

        toolbar();

        navigationView();
        navigationView.setNavigationItemSelectedListener(
                new NavigationViewHandler(this, this, R.string.HomeActivity));

        //handling tabView and viewPager
        tabLayout();

        viewPager();


        setFabClickListener();


    }

    void declareViews() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutHome);
        tabLayout = (TabLayout) findViewById(R.id.tabbar_home);
        tabTitles = new ArrayList<>();
        viewPager = (ViewPager) findViewById(R.id.viewpager_home);
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton_home);
        homeFragment1 = new HomeFragment_1();
        homeFragment2 = new HomeFragment_2();
        homeFragment3 = new HomeFragment_3();
        homeFragment_3_notLogedIn = new HomeFragment_3_notLogedIn();
        fragments = new ArrayList<>();
        navigationView = (NavigationView) findViewById(R.id.navViewHome);

    }

    void toolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        //for opening and closing navigation bar

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    void tabLayout() {

        //setting tab titles

        tabTitles.add("CallLog");
        tabTitles.add("Contacts");
        tabTitles.add("Friends");


        tabLayout.addTab(tabLayout.newTab().setText(tabTitles.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(tabTitles.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(tabTitles.get(2)));

    }

    void viewPager() {
        fragments.add(homeFragment1);
        fragments.add(homeFragment2);

        if (isLogin) {
            fragments.add(homeFragment3);
        } else {
            fragments.add(homeFragment_3_notLogedIn);
        }


        viewPager.setOffscreenPageLimit(2);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentAdapter = new FragmentAdapter_Home(fragmentManager, tabTitles, fragments);
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(page);

    }

    void setViewPagerListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (!searchView.isIconified()) {
                    homeFragment2.adapter.filterContacts("");
                    searchView.setIconified(true);
                }

                switch (position) {
                    case 0: {
                        fab.setImageResource(R.drawable.ic_dialpad);
                        searchView.setVisibility(View.GONE);
                        break;
                    }
                    case 1: {
                        searchView.setVisibility(View.VISIBLE);
                        fab.setImageResource(R.drawable.ic_person_add);
                        break;
                    }
                    case 2: {
                        searchView.setVisibility(View.GONE);
                        fab.setImageResource(R.drawable.ic_person_add);

                    }
                }

                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(tabTitles.get(position));
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    void setFabClickListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (viewPager.getCurrentItem()) {

                    case 0: {
                        Intent toDialerPadActivity = new Intent(HomeActivity.this, DialerPadActivity.class);
                        startActivity(toDialerPadActivity);


                        break;
                    }
                    case 1: {
                        Intent toAddContact = new Intent(ContactsContract.Intents.Insert.ACTION);
                        toAddContact.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                        startActivityForResult(toAddContact, 11);
                        break;
                    }
                    case 2: {
                        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.loginDetails), Context.MODE_PRIVATE);
                        if (sharedPref.getBoolean(getString(R.string.loginStatus), false)) {
                            Intent toSearchActivity = new Intent(HomeActivity.this, SearchActivity.class);
                            startActivity(toSearchActivity);
                        } else {
                            Toast.makeText(HomeActivity.this, "Login to add Friends", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                    default: {
                        break;
                    }

                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    void navigationView() {


        Menu menu = navigationView.getMenu();
        MenuItem logOutItem = menu.findItem(R.id.navItemLogOut);
        logOutItem.setVisible(isLogin);


        //Handling  click on NavigationViewHeader
        View navHeaderView = navigationView.getHeaderView(0);
        FrameLayout navHeaderLayout = (FrameLayout) navHeaderView.findViewById(R.id.nav_header_home);
        TextView navHeaderTextView = (TextView) navHeaderLayout.findViewById(R.id.textview_navheader_home);
        CircularImageView navHeaderProfilePic = (CircularImageView) navHeaderLayout.findViewById(R.id.imageview_navheader_home);
        navHeaderProfilePic.setImageResource(R.drawable.ic_account_circle);
        if (isLogin) {
            navHeaderTextView.setText("Click here to see User Profile");
            if (sharedPref.getBoolean(getString(R.string.loginIsGoogle), false) && Utils.internetConnectionStatus(this)) {

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

                if (isLogin) {
                    Intent toUserProfile = new Intent(HomeActivity.this, UserProfileActivity.class);
                    startActivity(toUserProfile);

                } else {
                    Intent toLoginActivity = new Intent(HomeActivity.this, LoginActivity.class);
                    toLoginActivity.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    startActivity(toLoginActivity);
                }


                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayoutHome);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search, menu);
        menuItem = menu.findItem(R.id.actionSearch_home);

        MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                tabLayout.setVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                tabLayout.setVisibility(View.VISIBLE);
                return true;
            }
        });


        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);

        setViewPagerListener();
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {


        switch (viewPager.getCurrentItem()) {

            case 0: {

                break;
            }
            case 1: {
                homeFragment2.adapter.filterContacts(newText);

                break;
            }
            case 2: {


                break;
            }
            default: {
                break;
            }

        }


        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (!searchView.isIconified()) {
            searchView.setIconified(true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isFirstResume) {

            if (isLogin != sharedPref.getBoolean(getString(R.string.loginStatus), false)) {
                isLogin = sharedPref.getBoolean(getString(R.string.loginStatus), false);

                if (isLogin) {
                    fragments.remove(2);
                    fragments.add(2, homeFragment3);
                } else {
                    fragmentAdapter.changeFragment3(new HomeFragment_3_notLogedIn());
                }
                navigationView();
            }

        }
    }

    @Override
    protected void onPause() {

        super.onPause();
        isFirstResume = false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 701: {
                if (grantResults.length > 0) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Grant Permission to Call", Toast.LENGTH_SHORT).show();

                    }
                }
                break;
            }

            case 100: {

            }

            default: {

                break;
            }
        }

    }


}