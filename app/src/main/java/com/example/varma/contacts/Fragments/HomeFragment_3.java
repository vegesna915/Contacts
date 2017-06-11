package com.example.varma.contacts.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.varma.contacts.Adapters.RecyclerViewAdapterFriends;
import com.example.varma.contacts.Database.FriendsDb;
import com.example.varma.contacts.Objects.Friend;
import com.example.varma.contacts.R;

import java.util.ArrayList;


public class HomeFragment_3 extends Fragment {

    boolean isLogin, isFirstResume;
    RecyclerViewAdapterFriends adapter;
    private Context context;
    private ArrayList<Friend> friends = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        isFirstResume = true;
        context = inflater.getContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                getString(R.string.loginDetails), Context.MODE_PRIVATE);
        isLogin = sharedPreferences.getBoolean(getString(R.string.loginStatus), false);
        View view = inflater.inflate(R.layout.fragment_home_3, container, false);
        getFriends();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_fragment3_home);
        textView = (TextView) view.findViewById(R.id.textView_fragment3_noFriends);
        Log.i("lifecycle", "RecyclerView and TextView Initiated");


        Log.i("lifecycle", "Friends  OnCreateView ");



        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRecyclerView();
        Log.i("lifecycle", "Friends  OnActivityCreated ");
    }


    private void checkFriendsListSize() {
        Log.i("lifecycle", "checkSize Friends");
        if (textView != null && recyclerView != null) {

            Log.i("lifecycle", "checkSize Friends recyclerView !=null");
            if (friends.size() > 0) {
                Log.i("lifecycle", "checkSize Friends > 0 ");
                textView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {

                Log.i("lifecycle", "checkSize Friends < 0 ");
                textView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        } else {
            Log.i("lifecycle", "checkSize Friends recyclerView == null");
        }
    }

    private void setRecyclerView() {

        adapter = new RecyclerViewAdapterFriends(friends);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

    }


    private void adapterUpdate() {
        if (adapter != null) {
            Log.i("lifecycle", "updateFriends adapter != null ");
            adapter.updateFriendsList(this.friends);
        } else {
            Log.i("lifecycle", "updateFriends adapter != null ");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirstResume) {
            getFriends();
            adapterUpdate();
        }

        Log.i("lifecycle", "Friends  OnResume ");
        checkFriendsListSize();
    }

    void getFriends() {
        if (isLogin) {
            FriendsDb friendsDb = new FriendsDb(context);
            this.friends.clear();
            this.friends.addAll(friendsDb.getAllFriends());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isFirstResume = false;
    }
}
