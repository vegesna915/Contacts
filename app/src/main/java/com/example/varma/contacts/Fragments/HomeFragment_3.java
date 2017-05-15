package com.example.varma.contacts.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

    boolean isLogin;
    Context context;
    ArrayList<Friend> friends = new ArrayList<>();
    RecyclerViewAdapterFriends adapter;
    RecyclerView recyclerView;
    TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        context = inflater.getContext();
        View view = inflater.inflate(R.layout.fragment_home_3, container, false);


        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_fragment3_home);
        textView = (TextView) view.findViewById(R.id.textView_fragment3_noFriends);

        getFriends();
        setRecyclerView();
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                getString(R.string.loginDetails), Context.MODE_PRIVATE);
        isLogin = sharedPreferences.getBoolean(getString(R.string.loginStatus), false);


        return view;
    }

    void checkFriendsListSize() {
        if (friends.size() <= 0) {
            textView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    void setRecyclerView() {

        adapter = new RecyclerViewAdapterFriends(friends);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

    }

    void getFriends() {
        FriendsDb friendsDb = new FriendsDb(context);
        friends.addAll(friendsDb.getAllFriends());
    }


    public void refreshFriends() {
        friends.clear();
        getFriends();
        checkFriendsListSize();
        adapter.updateFriendsList(friends);

    }

}
