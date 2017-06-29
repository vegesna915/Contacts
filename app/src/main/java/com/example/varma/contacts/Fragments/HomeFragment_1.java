package com.example.varma.contacts.Fragments;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.varma.contacts.Adapters.RecyclerViewAdapterCallLog;
import com.example.varma.contacts.Database.FriendsDb;
import com.example.varma.contacts.Extra.PermissionsClass;
import com.example.varma.contacts.Extra.Utils;
import com.example.varma.contacts.HomeActivity;
import com.example.varma.contacts.Objects.CallLogInfo;
import com.example.varma.contacts.Objects.Contact;
import com.example.varma.contacts.Objects.Friend;
import com.example.varma.contacts.R;

import java.util.ArrayList;
import java.util.HashMap;


public class HomeFragment_1 extends Fragment {

    public RecyclerViewAdapterCallLog adapter;
    private ArrayList<CallLogInfo> callLogs = new ArrayList<>();
    private boolean hasPermission;
    private TextView noPermissionTextView;
    private RecyclerView recyclerView;
    private boolean isFirstResume;
    private Context context;
    private boolean isLogin;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.loginDetails), Context.MODE_PRIVATE);
        isLogin = sharedPref.getBoolean(getString(R.string.loginStatus), false);


        isFirstResume = true;
        hasPermission = PermissionsClass.hasPermissionReadCallLog(getContext());
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_home_1, container, false);
        recyclerView = (RecyclerView) linearLayout.findViewById(R.id.recyclerView_callLog_home);
        noPermissionTextView = (TextView) linearLayout.findViewById(R.id.textView_fragment1_noPermission);

        return linearLayout;
    }

    private void checkPermission() {
        if (recyclerView == null || noPermissionTextView == null) {
            Log.i("lifecycle", "checkPermission CallLog recyclerView == null");
            return;
        }

        Log.i("lifecycle", "checkPermission CallLog");
        hasPermission = PermissionsClass.hasPermissionReadCallLog(getContext());

        if (hasPermission) {
            Log.i("lifecycle", "checkPermission CallLog has Permission");
            recyclerView.setVisibility(View.VISIBLE);
            noPermissionTextView.setVisibility(View.GONE);
        } else {
            Log.i("lifecycle", "checkPermission CallLog no permission");
            recyclerView.setVisibility(View.GONE);
            noPermissionTextView.setVisibility(View.VISIBLE);
        }

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //getCallLog();


        adapter = new RecyclerViewAdapterCallLog(callLogs, (HomeActivity) getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        DataAsyncTask dataAsyncTask = new DataAsyncTask();
        dataAsyncTask.execute((Void) null);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirstResume) {
            DataAsyncTask dataAsyncTask = new DataAsyncTask();
            dataAsyncTask.execute((Void) null);
        } else {
            checkPermission();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        isFirstResume = false;

    }

    private class DataAsyncTask extends AsyncTask<Void, Void, ArrayList<CallLogInfo>> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected ArrayList<CallLogInfo> doInBackground(Void... params) {

            HashMap<String, String> contactHashMap = new HashMap<>();
            ContentResolver contentResolver = getActivity().getContentResolver();
            ArrayList<CallLogInfo> callLogs2 = new ArrayList<>();
            try {


                if (isLogin) {
                    FriendsDb friendsDb = new FriendsDb(context);
                    ArrayList<Friend> friends;

                    friends = friendsDb.getAllFriends();
                    if (friends.size() > 0) {
                        for (int i = 0; i < friends.size(); i++) {
                            contactHashMap.put(friends.get(i).get_NUMBER(), friends.get(i).get_NAME());
                        }
                    }

                }

                if (PermissionsClass.hasPermissionReadContacts(context)) {
                    Contact contact;


                    //getting ContactNames

                    Uri uriNumber = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;


                    String[] columnsNumberContact = {
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER,
                    };


                    String whereNumber = ContactsContract.CommonDataKinds.Phone.IN_VISIBLE_GROUP + " ='1'" +
                            " AND " +
                            ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER + " = '1'";


                    //String[] selectionArgsNumber = null;


                    String sortingOrderNumber = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;


                    Cursor cursorContacts = contentResolver.query(uriNumber, columnsNumberContact, whereNumber,
                            null, sortingOrderNumber);

                    if (cursorContacts != null) {
                        while (cursorContacts.moveToNext()) {
                            contact = new Contact();

                            contact.setContactId(cursorContacts.getString(
                                    cursorContacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)));
                            contact.setContactName(
                                    cursorContacts.getString(cursorContacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                            contact.setContactNumber(
                                    cursorContacts.getString(cursorContacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                            contactHashMap.put(contact.getContactNumber(), contact.getContactName());

                        }
                        cursorContacts.close();
                    }

                }


                if (PermissionsClass.hasPermissionReadCallLog(context)) {

                    CallLogInfo callLog;


                    try {


                        Uri uri = CallLog.Calls.CONTENT_URI;


                        String[] columnsNumberCallLog = {
                                CallLog.Calls.CACHED_NAME,
                                CallLog.Calls.NUMBER,
                                CallLog.Calls.DATE,
                                CallLog.Calls.TYPE,
                                CallLog.Calls.NEW
                        };

                        //String where = null;

                        //String[] selectionArgs = null;


                        String sortingOrder = CallLog.Calls.DATE + " DESC ";

                        Cursor cursorCallLog = contentResolver.query(uri, columnsNumberCallLog, null, null, sortingOrder);


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


                                if (callLog.getCallerName().equals("")) {
                                    String name;
                                    if ((name = contactHashMap.get(callLog.getCallernumber())) == null) {
                                        name = contactHashMap.get("0" + callLog.getCallernumber());
                                    }
                                    callLog.setCallerName(name);
                                }

                                callLogs2.add(callLog);
                            }
                            cursorCallLog.close();
                        }


                    } catch (SecurityException e) {

                        e.printStackTrace();

                    }
                    callLogs2 = Utils.callLogMaintainer(callLogs2);
                }


            } catch (Exception e) {

                e.printStackTrace();

            }

            return callLogs2;
        }

        @Override
        protected void onPostExecute(ArrayList<CallLogInfo> callLogInfos) {

            callLogs.clear();
            callLogs.addAll(callLogInfos);
            checkPermission();
            adapter.updateCallLog(callLogs);

        }

    }

}
