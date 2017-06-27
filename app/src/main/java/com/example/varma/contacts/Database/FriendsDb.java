package com.example.varma.contacts.Database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.varma.contacts.Objects.DialerInfo;
import com.example.varma.contacts.Objects.Friend;

import java.util.ArrayList;



public class FriendsDb {


    static final String TABLE_FRIENDS = "FRIENDS_TABLE";
    static final String FRIENDS_ID = "_ID";
    static final String FRIENDS_USER_ID = "USER_ID";
    static final String FRIENDS_NAME = "_NAME";
    static final String FRIENDS_EMAIL = "_EMAIL";
    static final String FRIENDS_NUMBER = "_NUMBER";
    static final String FRIENDS_IMAGE_URL = "IMAGE_URL";
    static final String FRIENDS_OLD_NUMBER = "OLD_NUMBER";

    private Context context;

    public FriendsDb(Context context) {
        this.context = context;

    }


    public void addFriend(Friend friend) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FRIENDS_ID, friend.get_ID());
        values.put(FRIENDS_USER_ID, friend.getUSER_ID());
        values.put(FRIENDS_NAME, friend.get_NAME()); // Friend Name
        values.put(FRIENDS_NUMBER, friend.get_NUMBER()); // Friend Phone Number
        values.put(FRIENDS_EMAIL, friend.get_EMAIL()); // Friend Email
        values.put(FRIENDS_IMAGE_URL, friend.getIMAGE_URL()); // Friend IMAGE
        values.put(FRIENDS_OLD_NUMBER, friend.get_NUMBER_OLD()); // Friend OLD NUMBER

        // Inserting Row
        db.insert(TABLE_FRIENDS, null, values);
        db.close(); // Closing database connection
    }



    public Friend getFriendById(String _ID) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Friend friend = null;
        String[] columns = {
                FRIENDS_ID,
                FRIENDS_USER_ID,
                FRIENDS_NAME,
                FRIENDS_NUMBER,
                FRIENDS_EMAIL,
                FRIENDS_IMAGE_URL,
                FRIENDS_OLD_NUMBER
        };

        String where = FRIENDS_ID + "=?";

        String[] selectionArg = {_ID};

        Cursor cursor = db.query(TABLE_FRIENDS, columns, where, selectionArg, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                friend = new Friend();
                friend.set_ID(cursor.getString(cursor.getColumnIndex(FRIENDS_ID)));
                friend.setUSER_ID(cursor.getString(cursor.getColumnIndex(FRIENDS_USER_ID)));
                friend.set_NAME(cursor.getString(cursor.getColumnIndex(FRIENDS_NAME)));
                friend.set_NUMBER(cursor.getString(cursor.getColumnIndex(FRIENDS_NUMBER)));
                friend.set_EMAIL(cursor.getString(cursor.getColumnIndex(FRIENDS_EMAIL)));
                friend.setIMAGE_URL(cursor.getString(cursor.getColumnIndex(FRIENDS_IMAGE_URL)));
                friend.set_NUMBER_OLD(cursor.getString(cursor.getColumnIndex(FRIENDS_OLD_NUMBER)));
                // return contact

            }
            cursor.close();
        }


        db.close();
        return friend;
    }

    public ArrayList<DialerInfo> getDialerInfoByNumber(String _NUMBER) {

        ArrayList<DialerInfo> infos = new ArrayList<>();
        DialerInfo info;

        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String[] columns = {
                FRIENDS_NAME,
                FRIENDS_NUMBER
        };

        String where = FRIENDS_NUMBER + " GLOB ? ";

        String[] selectionArg = {"*" + _NUMBER + "*"};

        Cursor cursor = db.query(TABLE_FRIENDS, columns, where, selectionArg, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                info = new DialerInfo();
                info.setName(cursor.getString(cursor.getColumnIndex(FRIENDS_NAME)));
                info.setNumber(cursor.getString(cursor.getColumnIndex(FRIENDS_NUMBER)));
                infos.add(info);
            }
            cursor.close();
        }


        db.close();
        return infos;
    }

    // Getting All friend
    public ArrayList<Friend> getAllFriends() {

        ArrayList<Friend> friends = new ArrayList<>();
        Friend friend;

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FRIENDS;

        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor != null) {
            while (cursor.moveToNext()) {
                friend = new Friend();

                friend.set_ID(cursor.getString(cursor.getColumnIndex(FRIENDS_ID)));
                friend.setUSER_ID(cursor.getString(cursor.getColumnIndex(FRIENDS_USER_ID)));
                friend.set_NAME(cursor.getString(cursor.getColumnIndex(FRIENDS_NAME)));
                friend.set_NUMBER(cursor.getString(cursor.getColumnIndex(FRIENDS_NUMBER)));
                friend.set_EMAIL(cursor.getString(cursor.getColumnIndex(FRIENDS_EMAIL)));
                friend.setIMAGE_URL(cursor.getString(cursor.getColumnIndex(FRIENDS_IMAGE_URL)));
                friend.set_NUMBER_OLD(cursor.getString(cursor.getColumnIndex(FRIENDS_OLD_NUMBER)));
                // Adding contact to list
                friends.add(friend);
            }
            cursor.close();
        }
        db.close();
        // return contact list
        return friends;

    }

    // Getting friend Count
    public int getFriendsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_FRIENDS;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        db.close();

        // return count
        return count;
    }

    // Updating single contact
    public int updateFriend(Friend friend) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FRIENDS_NAME, friend.get_NAME()); // Friend Name
        values.put(FRIENDS_NUMBER, friend.get_NUMBER()); // Friend Phone Number

        // updating row

        String where = FRIENDS_ID + " = ?";
        String[] selectionArgs = {String.valueOf(friend.get_ID())};
        int update = db.update(TABLE_FRIENDS, values, where, selectionArgs);
        db.close();
        return update;
    }

    // Deleting single contact
    public void unFriend(String _ID) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(TABLE_FRIENDS, FRIENDS_ID + " = ?",
                new String[]{_ID});
        db.close();
    }

    public void deleteAllFriends() {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(TABLE_FRIENDS, null, null);
        db.close();
    }

    public boolean isFriend(String _ID) {

        String countQuery = "SELECT  * FROM " + TABLE_FRIENDS
                + " WHERE " + FRIENDS_ID + " = " + _ID;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        db.close();

        // return count
        return count != 0;

    }

}
