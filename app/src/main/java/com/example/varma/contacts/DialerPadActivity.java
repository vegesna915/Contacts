package com.example.varma.contacts;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.varma.contacts.Adapters.RecyclerViewAdapterDialPad;
import com.example.varma.contacts.Extra.PermissionsClass;
import com.example.varma.contacts.Extra.Utilis;
import com.example.varma.contacts.Objects.DialerInfo;

import java.util.ArrayList;

public class DialerPadActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    String dialPadInput = "";
    EditText editText;
    boolean isDialPadUp = true;
    GridView dialerPad;
    ImageButton dialPadControl;
    FloatingActionButton fab;
    RecyclerViewAdapterDialPad adapter;
    ArrayList<DialerInfo> infos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialer_pad);

        final View divider = findViewById(R.id.divider_dialPad);
        editText = (EditText) findViewById(R.id.editText_dialPad);
        editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        editText.setTextIsSelectable(true);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isDialPadUp) {
                    dialerPad.setVisibility(View.VISIBLE);
                    dialPadControl.setImageResource(R.drawable.ic_down_purpile);
                    fab.setVisibility(View.VISIBLE);
                    divider.setVisibility(View.VISIBLE);
                    isDialPadUp = true;
                    cursorVisible();
                }
            }
        });


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_dialerPad);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (isDialPadUp) {
                    dialerPad.setVisibility(View.GONE);
                    dialPadControl.setImageResource(R.drawable.ic_up_purpile);
                    fab.setVisibility(View.GONE);
                    divider.setVisibility(View.GONE);
                    editText.setCursorVisible(false);
                    isDialPadUp = false;
                }
                return false;
            }
        });

        adapter = new RecyclerViewAdapterDialPad(infos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        fab = (FloatingActionButton) findViewById(R.id.fab_dial_pad);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (PermissionsClass.hasPermissionCallPhone(DialerPadActivity.this)) {

                    String number = editText.getText().toString().trim();
                    number = "tel:" + number;

                    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
                    try {
                        startActivity(callIntent);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }


                } else {
                    PermissionsClass.requestPermission(DialerPadActivity.this,
                            new String[]{PermissionsClass.CallPhone}, 501);
                }

            }
        });

        dialerPad = (GridView) findViewById(R.id.gridView_dialPad);


        ArrayAdapter<String> dialPadAdapter = new ArrayAdapter<>(this, R.layout.dial_pad_number_item,
                getResources().getStringArray(R.array.dialer_numbers));

        dialerPad.setOnItemClickListener(this);
        dialerPad.setAdapter(dialPadAdapter);


        dialPadControl = (ImageButton) findViewById(R.id.dial_pad_control);
        dialPadControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isDialPadUp) {
                    dialerPad.setVisibility(View.GONE);
                    dialPadControl.setImageResource(R.drawable.ic_up_purpile);
                    fab.setVisibility(View.GONE);
                    divider.setVisibility(View.GONE);
                    editText.setCursorVisible(false);
                    isDialPadUp = false;
                } else {
                    dialerPad.setVisibility(View.VISIBLE);
                    dialPadControl.setImageResource(R.drawable.ic_down_purpile);
                    fab.setVisibility(View.VISIBLE);
                    divider.setVisibility(View.VISIBLE);
                    isDialPadUp = true;
                    cursorVisible();
                }
            }
        });


        ImageButton backButton = (ImageButton) findViewById(R.id.back_dial_pad);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int startPosition = editText.getSelectionStart();
                int endPosition = editText.getSelectionEnd();

                editText.setText(Utilis.removeCharFromString(editText.getText().toString(), "",
                        startPosition, endPosition));
                if (startPosition != 0) {
                    editText.setSelection(startPosition - 1);
                }
                cursorVisible();
                updateInfo();

            }
        });
        backButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                editText.setText("");
                cursorVisible();
                return true;

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView textView = (TextView) view;

        dialPadInput = editText.getText().toString();
        int startPosition = editText.getSelectionStart();
        int endPosition = editText.getSelectionEnd();
        String newEntry;

        switch (textView.getText().toString()) {

            case "1": {
                newEntry = "1";
                break;
            }
            case "2": {
                newEntry = "2";
                break;
            }
            case "3": {
                newEntry = "3";
                break;
            }
            case "4": {
                newEntry = "4";
                break;
            }
            case "5": {
                newEntry = "5";
                break;
            }
            case "6": {
                newEntry = "6";
                break;
            }
            case "7": {
                newEntry = "7";
                break;
            }
            case "8": {
                newEntry = "8";
                break;
            }
            case "9": {
                newEntry = "9";
                break;
            }
            case "0": {
                newEntry = "0";
                break;
            }
            case "*": {
                newEntry = "*";
                break;
            }
            case "#": {
                newEntry = "#";
                break;
            }
            default: {
                newEntry = "";
                break;
            }


        }

        editText.setText(Utilis.addCharToString(dialPadInput, newEntry, startPosition, endPosition));
        editText.setSelection(startPosition + 1);
        updateInfo();
        cursorVisible();

    }

    void cursorVisible() {
        if (editText.getText().toString().equals("")) {
            editText.setCursorVisible(false);
        } else {
            editText.setCursorVisible(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 501) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    String number = editText.getText().toString().trim();
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

    public void getInfo(String query) {
        infos.clear();
        DialerInfo info;

        ContentResolver contentResolver = getContentResolver();

        try {


            Uri uriNumber = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;


            String[] columnsNumbers = {
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
            };


            String whereNumber = ContactsContract.CommonDataKinds.Phone.IN_VISIBLE_GROUP + " ='1'" +
                    " AND " +
                    ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER + " = '1'" + " AND " +
                    ContactsContract.CommonDataKinds.Phone.NUMBER + " GLOB ?";


            String[] selectionArgsNumber = {"*" + query + "*"};


            String sortingOrderNumber = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;


            Cursor cursorContacts = contentResolver.query(uriNumber, columnsNumbers, whereNumber,
                    selectionArgsNumber, sortingOrderNumber);


            if (cursorContacts != null) {
                while (cursorContacts.moveToNext()) {
                    info = new DialerInfo();

                    info.setName(
                            cursorContacts.getString(cursorContacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                    info.setNumber(
                            cursorContacts.getString(cursorContacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));

                    infos.add(info);


                }
                cursorContacts.close();

            }


            Uri uri = CallLog.Calls.CONTENT_URI;

            String[] columnsNumber = {
                    CallLog.Calls.NUMBER
            };

            String where = CallLog.Calls.NUMBER + " GLOB ? " + " AND " +
                    CallLog.Calls.CACHED_NAME + " IS NULL";


            String[] selectionArgs = {"*" + query + "*"};


            String sortingOrder = CallLog.Calls.DATE + " DESC ";

            Cursor cursorCallLog = contentResolver.query(uri, columnsNumber, where, selectionArgs, sortingOrder);


            if (cursorCallLog != null) {
                while (cursorCallLog.moveToNext()) {
                    info = new DialerInfo();

                    info.setNumber(
                            cursorCallLog.getString(cursorCallLog.getColumnIndex(CallLog.Calls.NUMBER)));

                    infos.add(info);
                }
                cursorCallLog.close();
            }


        } catch (SecurityException e) {
            e.printStackTrace();

        } catch (Exception e) {

            e.printStackTrace();

        }


    }

    public void updateInfo() {
        if (editText.getText().toString().equals("")) {
            infos.clear();
        } else {
            getInfo(editText.getText().toString());
        }
        adapter.updateInfos(infos);
        adapter.notifyDataSetChanged();

    }


}
