package com.example.varma.contacts;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        //setting up toolbar


        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        //for opening and closing navigation bar
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutHome);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //Handling Click on NavigationViewItems
        NavigationView navigationView = (NavigationView) findViewById(R.id.navViewHome);
        navigationView.setNavigationItemSelectedListener(this);

        //Handling  click on NavigationViewHeader
        View navHeaderView = navigationView.getHeaderView(0);
        LinearLayout navHeaderLayout = (LinearLayout) navHeaderView.findViewById(R.id.nav_header_home);
        navHeaderLayout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent toLoginActivity = new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(toLoginActivity);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayoutHome);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

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

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment_1());
        fragments.add(new HomeFragment_2());
        fragments.add(new HomeFragment_3());

        viewPager.setOffscreenPageLimit(2);
        FragmentAdapter_Home fragmentAdapter = new FragmentAdapter_Home(getSupportFragmentManager(),tabTitles,fragments);
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(fragmentAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                getSupportActionBar().setTitle(tabTitles.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {



        NavigationViewHandler.onNavItemClicked(HomeActivity.this,this,item,"HomeActivity");
        Toast.makeText(this, item.getTitle() + " - is Selected", Toast.LENGTH_SHORT).show();
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
