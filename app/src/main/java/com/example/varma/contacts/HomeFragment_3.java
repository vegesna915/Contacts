package com.example.varma.contacts;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Varma on 3/27/2017.
 */

public class HomeFragment_3 extends Fragment {

    boolean isLogin;
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        context = inflater.getContext();
        View view = inflater.inflate(R.layout.fragment_home_3, container, false);

        TextView emailAdress = (TextView) view.findViewById(R.id.textView);

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                getString(R.string.loginDetails), Context.MODE_PRIVATE);
        isLogin = sharedPreferences.getBoolean(getString(R.string.loginStatus), false);
        if (isLogin) {
            String email = sharedPreferences.getString(getString(R.string.loginEmailId), "No Email");
            emailAdress.setText(email);
        }


        return view;
    }
}
