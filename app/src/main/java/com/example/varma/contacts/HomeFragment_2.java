package com.example.varma.contacts;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
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

public class HomeFragment_2 extends Fragment {


    Context context;
    private ArrayList<Contact> contacts = new ArrayList<>();
    private boolean hasPermission;
    private RecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        context = inflater.getContext();
        hasPermission = PermissionsClass.hasPermissionReadContacts(getContext());
        LinearLayout linearLayout;
        if(!hasPermission){

            linearLayout = (LinearLayout) inflater.inflate(R.layout.no_permission,container,false);
            TextView textView = (TextView) linearLayout.findViewById(R.id.textView_noPermission);
            textView.setText("Permission not Granted to get Contacts");

        }else{

            linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_home_2,container,false);

            recyclerView = (RecyclerView)linearLayout.findViewById(R.id.recyclerView_contacts_home);

        }

        return linearLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(!hasPermission){
            return;
        }

        getContacts();
        RecyclerViewAdapterContact adapter = new RecyclerViewAdapterContact(contacts);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

    }

    private void getContacts(){

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



            String[] selectionArgsNumber = null;



            String sortingOrderNumber = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;


            Cursor cursorContacts = contentResolver.query(uriNumber, columnsNumber, whereNumber,
                    selectionArgsNumber, sortingOrderNumber);


            while(cursorContacts.moveToNext()){
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

        }catch (Exception e){

            e.printStackTrace();

        }




    }



}
