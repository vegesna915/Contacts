package com.example.varma.contacts.Database;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.varma.contacts.Objects.Friend;
import java.util.ArrayList;



public class FriendsDb {


    private static final String TABLE_FRIENDS = "FRIENDS_TABLE";
    private static final String FRIENDS_ID = "_ID";
    private static final String FRIENDS_NAME = "_NAME";
    private static final String FRIENDS_EMAIL = "_EMAIL";
    private static final String FRIENDS_NUMBER = "_NUMBER";
    private Activity activity;

    FriendsDb(Activity activity) {
        this.activity = activity;

    }


    public void addFriend(Friend friend) {
        DatabaseHelper databaseHelper = new DatabaseHelper(activity);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FRIENDS_ID, Integer.parseInt(friend.get_ID()));
        values.put(FRIENDS_NAME, friend.get_NAME()); // Friend Name
        values.put(FRIENDS_NUMBER, friend.get_NUMBER()); // Friend Phone Number
        values.put(FRIENDS_EMAIL, friend.get_EMAIL()); // Friend Email
        // Inserting Row
        db.insert(TABLE_FRIENDS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single friend
    public Friend getFriend(String email) {
        DatabaseHelper databaseHelper = new DatabaseHelper(activity);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Friend friend = null;
        String[] columns = {
                FRIENDS_ID,
                FRIENDS_NAME,
                FRIENDS_NUMBER,
                FRIENDS_EMAIL,
        };

        String where = FRIENDS_EMAIL + "=?";

        String[] selectionArg = {email};

        Cursor cursor = db.query(TABLE_FRIENDS, columns, where, selectionArg, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                friend = new Friend();
                friend.set_ID(cursor.getString(cursor.getColumnIndex(FRIENDS_ID)));
                friend.set_NAME(cursor.getString(cursor.getColumnIndex(FRIENDS_NAME)));
                friend.set_NUMBER(cursor.getString(cursor.getColumnIndex(FRIENDS_NUMBER)));
                friend.set_EMAIL(cursor.getString(cursor.getColumnIndex(FRIENDS_EMAIL)));
                // return contact

            }
            cursor.close();
        }


        db.close();
        return friend;
    }

    // Getting All friend
    public ArrayList<Friend> getAllFriend() {

        ArrayList<Friend> friends = new ArrayList<>();
        Friend friend;

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FRIENDS;

        DatabaseHelper databaseHelper = new DatabaseHelper(activity);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor != null) {
            while (cursor.moveToNext()) {
                friend = new Friend();
                friend.set_NAME(cursor.getString(cursor.getColumnIndex(FRIENDS_NAME)));
                friend.set_NUMBER(cursor.getString(cursor.getColumnIndex(FRIENDS_NUMBER)));
                friend.set_EMAIL(cursor.getString(cursor.getColumnIndex(FRIENDS_EMAIL)));
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
        DatabaseHelper databaseHelper = new DatabaseHelper(activity);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        db.close();

        // return count
        return cursor.getCount();
    }

    // Updating single contact
    public int updateFriend(Friend friend) {
        DatabaseHelper databaseHelper = new DatabaseHelper(activity);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FRIENDS_NAME, friend.get_NAME());
        values.put(FRIENDS_NUMBER, friend.get_NUMBER());
        values.put(FRIENDS_EMAIL, friend.get_EMAIL());

        // updating row

        String where = FRIENDS_ID + " = ?";
        String[] selectionArgs = {String.valueOf(friend.get_ID())};
        int update = db.update(TABLE_FRIENDS, values, where, selectionArgs);
        db.close();
        return update;
    }

    // Deleting single contact
    public void deleteFriend(String _ID) {
        DatabaseHelper databaseHelper = new DatabaseHelper(activity);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(TABLE_FRIENDS, FRIENDS_ID + " = ?",
                new String[]{_ID});
        db.close();
    }

}
