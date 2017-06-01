package com.example.varma.contacts.Fragments;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.varma.contacts.Adapters.RecyclerViewAdapterCallLog;
import com.example.varma.contacts.Extra.PermissionsClass;
import com.example.varma.contacts.Extra.Utilis;
import com.example.varma.contacts.Objects.CallLogInfo;
import com.example.varma.contacts.R;

import java.util.ArrayList;


public class HomeFragment_1 extends Fragment {

    public RecyclerViewAdapterCallLog adapter;
    private ArrayList<CallLogInfo> callLogs = new ArrayList<>();
    private boolean hasPermission;
    private RecyclerView recyclerView;

    @SuppressLint("SetTextI18n")
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


        adapter = new RecyclerViewAdapterCallLog(callLogs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);


    }

    void getCallLog() {

        CallLogInfo callLog;
        callLogs.clear();

        ContentResolver contentResolver = getContext().getContentResolver();

        try {


            Uri uri = CallLog.Calls.CONTENT_URI;


            String[] columnsNumber = {
                    CallLog.Calls.CACHED_NAME,
                    CallLog.Calls.NUMBER,
                    CallLog.Calls.DATE,
                    CallLog.Calls.TYPE,
                    CallLog.Calls.NEW
            };


//            String where = null;


//            String[] selectionArgs = null;


            String sortingOrder = CallLog.Calls.DATE + " DESC ";

            Cursor cursorCallLog = contentResolver.query(uri, columnsNumber, null, null, sortingOrder);


            if (cursorCallLog != null) {
                while (cursorCallLog.moveToNext()) {
                    callLog = new CallLogInfo();

                    callLog.setCallerName(cursorCallLog.getString(
                            cursorCallLog.getColumnIndex(CallLog.Calls.CACHED_NAME)));
                    callLog.setCallernumber(
                            cursorCallLog.getString(cursorCallLog.getColumnIndex(CallLog.Calls.NUMBER)));
                    callLog.setCalldate(
                            cursorCallLog.getString(cursorCallLog.getColumnIndex(CallLog.Calls.DATE)));
                    callLog.setCallType(
                            cursorCallLog.getString(cursorCallLog.getColumnIndex(CallLog.Calls.TYPE)));

                    /*String isNew = cursorCallLog.getString(cursorCallLog.getColumnIndex(CallLog.Calls.NEW));

                    if (isNew.equals("1")) {
                        Uri uriNumber = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;


                        String[] columnsNumbers = {
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                                ContactsContract.CommonDataKinds.Phone.NUMBER
                        };


                        String whereNumber =" REPLACE("+ContactsContract.CommonDataKinds.Phone.NUMBER+",' ','') IN (?, ?, ?)";


                        String[] selectionArgsNumber = {callLog.getCallernumber(),
                                "0"+callLog.getCallernumber(),
                                "+91"+callLog.getCallernumber()};


                        String sortingOrderNumber = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;


                        Cursor cursorContacts = contentResolver.query(uriNumber, columnsNumbers, whereNumber,
                                selectionArgsNumber, sortingOrderNumber);


                        if (cursorContacts != null) {
                            while (cursorContacts.moveToNext()) {

                                callLog.setCallerName(
                                        cursorContacts.getString(cursorContacts.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));

                            }
                            cursorContacts.close();
                        }



                    }*/

                    callLogs.add(callLog);

                }
                cursorCallLog.close();
            }


        } catch (SecurityException e) {

            e.printStackTrace();

        }
        callLogs = Utilis.callLogMaintainer(callLogs);
    }

    public void notifyCallLogChanged() {
        getCallLog();
        adapter.updateCallLog(callLogs);


    }


}
