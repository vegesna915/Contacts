package com.example.varma.contacts.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class FragmentAdapter_Home extends FragmentStatePagerAdapter {

    private List<String> titles = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();

    public FragmentAdapter_Home(FragmentManager fm, List<String> titles, List<Fragment> fragments) {
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

    public void changeFragments(List<Fragment> fragments) {
        this.fragments = fragments;
        notifyDataSetChanged();
    }

    public void changeFragment3(Fragment fragment3) {
        fragments.remove(2);
        fragments.add(2, fragment3);
        notifyDataSetChanged();
    }

    public void changeFragment2(Fragment fragment2) {
        fragments.remove(1);
        fragments.add(1, fragment2);
        notifyDataSetChanged();
    }

    public void changeFragment1(Fragment fragment1) {
        fragments.remove(0);
        fragments.add(0, fragment1);
        notifyDataSetChanged();
    }
}
