package com.example.varma.contacts.Objects;

import java.util.ArrayList;


public class Data {

    public ArrayList<Contact> dataContacts;
    public ArrayList<CallLogInfo> dataCallLogs;
    public ArrayList<Friend> dataFriends;

    public Data() {
        dataContacts = new ArrayList<>();
        dataCallLogs = new ArrayList<>();
        dataFriends = new ArrayList<>();
    }


}
