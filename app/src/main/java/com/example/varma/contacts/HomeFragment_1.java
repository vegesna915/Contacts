package com.example.varma.contacts;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Varma on 3/27/2017.
 */

public class HomeFragment_1 extends Fragment {

    private ArrayList<CallLogInfo> callLogs = new ArrayList<>();
    private boolean hasPermission;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        hasPermission = PermissionsClass.hasPermissionReadCallLog(getContext());

        LinearLayout linearLayout;

        if (!hasPermission) {
            linearLayout = (LinearLayout) inflater.inflate(R.layout.no_permission, container, false);
            TextView noPermission = (TextView) linearLayout.findViewById(R.id.textView_noPermission);
            noPermission.setText("Permission not Granted to get CallLog");
            return linearLayout;
        }

        linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_home_1, container, false);
        recyclerView = (RecyclerView) linearLayout.findViewById(R.id.recyclerView_callLog_home);

        return linearLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (!hasPermission) {
            return;
        }
        getCallLog();


        RecyclerViewAdapterCallLog adapter = new RecyclerViewAdapterCallLog(callLogs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);


    }

    void getCallLog() {

        CallLogInfo callLog;

        ContentResolver contentResolver = getContext().getContentResolver();

        try {


            Uri uri = CallLog.Calls.CONTENT_URI;


            String[] columnsNumber = {
                    CallLog.Calls.CACHED_NAME,
                    CallLog.Calls.NUMBER,
                    CallLog.Calls.DATE,
                    CallLog.Calls.DURATION,
                    CallLog.Calls.TYPE
            };


            String where = null;


            String[] selectionArgs = null;


            String sortingOrder = CallLog.Calls.DATE + " DESC ";

            Cursor cursorCallLog = contentResolver.query(uri, columnsNumber, where, selectionArgs, sortingOrder);


            while (cursorCallLog.moveToNext()) {
                callLog = new CallLogInfo();

                callLog.setCallerName(cursorCallLog.getString(
                        cursorCallLog.getColumnIndex(CallLog.Calls.CACHED_NAME)));
                callLog.setCallernumber(
                        cursorCallLog.getString(cursorCallLog.getColumnIndex(CallLog.Calls.NUMBER)));
                callLog.setCalldate(
                        cursorCallLog.getString(cursorCallLog.getColumnIndex(CallLog.Calls.DATE)));
                callLog.setCallDuration(
                        cursorCallLog.getString(cursorCallLog.getColumnIndex(CallLog.Calls.DURATION)));
                callLog.setCallType(
                        cursorCallLog.getString(cursorCallLog.getColumnIndex(CallLog.Calls.TYPE))
                );

                callLogs.add(callLog);

            }

            cursorCallLog.close();

        } catch (SecurityException e) {
            e.printStackTrace();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }


}
