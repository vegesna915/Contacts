package com.example.varma.contacts;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.varma.contacts.Adapters.FragmentAdapter_Requests;
import com.example.varma.contacts.Database.RequestsDb;
import com.example.varma.contacts.Fragments.ReceiveRequestFragment;
import com.example.varma.contacts.Fragments.SendRequestFragment;

import java.util.ArrayList;
import java.util.List;

public class RequestsActivity extends AppCompatActivity {

    TabLayout tabLayout;
    List<String> tabTitles = new ArrayList<>();
    List<Fragment> fragments = new ArrayList<>();
    ViewPager viewPager;
    SendRequestFragment sendRequestFragment;
    ReceiveRequestFragment receiveRequestFragment;
    FragmentAdapter_Requests fragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_requests);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        tabLayout = (TabLayout) findViewById(R.id.tabbar_requests);
        viewPager = (ViewPager) findViewById(R.id.viewpager_requests);

        sendRequestFragment = new SendRequestFragment();
        receiveRequestFragment = new ReceiveRequestFragment();

        tabTitles.add("Received");
        tabTitles.add("Sent");

        tabLayout.addTab(tabLayout.newTab().setText(tabTitles.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(tabTitles.get(1)));

        fragments.add(receiveRequestFragment);
        fragments.add(sendRequestFragment);


        viewPager.setOffscreenPageLimit(1);
        fragmentAdapter = new FragmentAdapter_Requests(getSupportFragmentManager(), tabTitles, fragments);
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.requests_activity_main_menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.clearRequests_menuItem_requestsActivity: {

                RequestsDb requestsDb = new RequestsDb(this);

                if (viewPager.getCurrentItem() == 0) {
                    requestsDb.deleteNonPendingReceivedRequests();
                    receiveRequestFragment.refreshRequests();
                } else {
                    requestsDb.deleteNonPendingSendRequests();
                    sendRequestFragment.refreshRequests();
                }


                return true;
            }


            default: {

                return super.onOptionsItemSelected(item);
            }

        }

    }
}
