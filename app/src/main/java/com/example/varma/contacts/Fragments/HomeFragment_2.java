package com.example.varma.contacts.Fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.varma.contacts.Adapters.RecyclerViewAdapterContact;
import com.example.varma.contacts.Extra.PermissionsClass;
import com.example.varma.contacts.Objects.Contact;
import com.example.varma.contacts.R;

import java.util.ArrayList;


public class HomeFragment_2 extends Fragment {


    public RecyclerView recyclerView;

    public RecyclerViewAdapterContact adapter;
    private Context context;
    private ArrayList<Contact> contacts = new ArrayList<>();
    private TextView noPermissionTextView;
    private boolean isFirstResume;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        context = inflater.getContext();
        isFirstResume = true;
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_home_2, container, false);

        recyclerView = (RecyclerView) linearLayout.findViewById(R.id.recyclerView_contacts_home);
        noPermissionTextView = (TextView) linearLayout.findViewById(R.id.textView_fragment2_noPermission);


        getContacts();
        return linearLayout;
    }

    private void checkPermission() {
        if (recyclerView == null || noPermissionTextView == null) {
            Log.i("lifecycle", "checkPermission Contacts recyclerView == null");
            return;
        }

        boolean hasPermission = PermissionsClass.hasPermissionReadContacts(getContext());
        Log.i("lifecycle", "checkPermission Contacts");
        if (hasPermission) {
            Log.i("lifecycle", "checkPermission Contacts has Permission");
            recyclerView.setVisibility(View.VISIBLE);
            noPermissionTextView.setVisibility(View.GONE);
        } else {
            Log.i("lifecycle", "checkPermission Contacts no Permission");
            recyclerView.setVisibility(View.GONE);
            noPermissionTextView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        //getContacts();
        adapter = new RecyclerViewAdapterContact(contacts);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(recyclerView.getWindowToken(), 0);
                return false;
            }
        });


    }

    private void getContacts() {

        contacts.clear();


        if (PermissionsClass.hasPermissionReadContacts(context)) {
            Contact contact;

            //getting ContactNames

            ContentResolver contentResolver = getContext().getContentResolver();

            try {


                Uri uriNumber = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;


                String[] columnsNumber = {
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                };


                String whereNumber = ContactsContract.CommonDataKinds.Phone.IN_VISIBLE_GROUP + " ='1'" +
                        " AND " +
                        ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER + " = '1'";


                //String[] selectionArgsNumber = null;


                String sortingOrderNumber = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;


                Cursor cursorContacts = contentResolver.query(uriNumber, columnsNumber, whereNumber,
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

                        contacts.add(contact);

                    }
                    cursorContacts.close();
                }


            } catch (Exception e) {

                e.printStackTrace();

            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!isFirstResume) {
            getContacts();
            adapter.refreshContacts(contacts);
        }
        checkPermission();
    }

    @Override
    public void onPause() {
        super.onPause();
        isFirstResume = false;
    }


}
