package com.example.varma.contacts;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.varma.contacts.Adapters.RecyclerViewAdapterContactInfo;
import com.example.varma.contacts.Extra.Caller;
import com.example.varma.contacts.Extra.PermissionsClass;
import com.example.varma.contacts.Extra.Utils;
import com.example.varma.contacts.Objects.CallLogInfo;
import com.example.varma.contacts.Objects.Contact;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class ContactInfoActivity extends AppCompatActivity {

    Contact contact;
    RecyclerViewAdapterContactInfo adapter;
    ArrayList<CallLogInfo> callLogs = new ArrayList<>();
    boolean isFirstResume;
    AdView adView;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        isFirstResume = true;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_contactInfo);
        setSupportActionBar(toolbar);

        contact = new Contact();
        contact.setContactId(getIntent().getStringExtra("contactId"));
        contact.setContactNumber(getIntent().getStringExtra("contactNumber"));
        contact.setContactName(getIntent().getStringExtra("contactName"));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(contact.getContactName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        view = findViewById(R.id.layout_numberContactInfoHome);
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                openContextMenu(v);

                return true;
            }
        });
        registerForContextMenu(view);

        TextView contactNumberView = (TextView) findViewById(R.id.contact_phoneNumberHome);
        contactNumberView.setText(contact.getContactNumber());

        ImageButton options = (ImageButton) findViewById(R.id.contact_options);
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openContextMenu(view);
            }
        });
        //-----------------------------------------------------


        getCallLogs();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_ContactInfo);
        adapter = new RecyclerViewAdapterContactInfo(callLogs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);


        //-----------------------------------------------------

        FloatingActionButton fabCall = (FloatingActionButton) findViewById(R.id.fab_contactInfo_item);
        fabCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Caller.callNumber(ContactInfoActivity.this, contact.getContactNumber());
            }
        });

        //----------------------------------------------------------
        FloatingActionButton fabEdit = (FloatingActionButton) findViewById(R.id.fab_edit_contact_info);
        fabEdit.setVisibility(View.GONE);
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editContact();
            }
        });

        adView = (AdView) findViewById(R.id.adViewBanner_contactInfoActivity);
        if (Utils.internetConnectionStatus(this)) {

            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();

            adView.loadAd(adRequest);
        } else {
            adView.setVisibility(View.GONE);
        }

    }

    private void editContact() {
        Intent toEditContact = new Intent(Intent.ACTION_EDIT);
        Uri contentUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                Long.parseLong(contact.getContactId()));
        toEditContact.setData(contentUri);
        startActivity(toEditContact);
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 701) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    String number = contact.getContactNumber().trim();
                    number = "tel:" + number;

                    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
                    try {
                        startActivity(callIntent);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(this, "Grant Permission to Call", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    void getCallLogs() {


        callLogs.clear();
        if (!PermissionsClass.hasPermissionReadCallLog(this)) {
            return;
        }
        CallLogInfo callLog;

        ContentResolver contentResolver = getContentResolver();

        try {


            Uri uri = CallLog.Calls.CONTENT_URI;


            String[] columnsNumber = {
                    CallLog.Calls.CACHED_NAME,
                    CallLog.Calls.NUMBER,
                    CallLog.Calls.DATE,
                    CallLog.Calls.TYPE,
                    CallLog.Calls.DURATION
            };


            String where = CallLog.Calls.NUMBER + " GLOB ?";


            String[] selectionArgs = new String[]{"*" + contact.getContactNumber() + "*"};


            String sortingOrder = CallLog.Calls.DATE + " DESC ";

            Cursor cursorCallLog = contentResolver.query(uri, columnsNumber, where, selectionArgs, sortingOrder);


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
                    callLog.setCallDuration(
                            cursorCallLog.getString(cursorCallLog.getColumnIndex(CallLog.Calls.DURATION)));
                    callLogs.add(callLog);

                }
                cursorCallLog.close();
            }

        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirstResume) {
            getCallLogs();
            adapter.updateCallLog(callLogs);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isFirstResume = false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle("Select Option");

        menu.add(Menu.NONE, 0, Menu.NONE, "Call");
        menu.add(Menu.NONE, 1, Menu.NONE, "Message");
        menu.add(Menu.NONE, 2, Menu.NONE, "Copy");
        menu.add(Menu.NONE, 3, Menu.NONE, "Edit");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case 0: {
                Caller.callNumber(this, contact.getContactNumber());
                break;
            }

            case 1: {
                Caller.smsNumber(ContactInfoActivity.this, contact.getContactNumber());
                break;
            }

            case 2: {
                Utils.copyToClipBoard(ContactInfoActivity.this, contact.getContactNumber());
                break;
            }

            case 3: {
                editContact();
                break;
            }

            default: {

                break;
            }

        }


        return super.onContextItemSelected(item);

    }
}
