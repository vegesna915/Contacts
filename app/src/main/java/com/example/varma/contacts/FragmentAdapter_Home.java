package com.example.varma.contacts;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;



class FragmentAdapter_Home extends FragmentStatePagerAdapter {

    private List<String> titles = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();

    FragmentAdapter_Home(FragmentManager fm, List<String> titles, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;

    }


    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
