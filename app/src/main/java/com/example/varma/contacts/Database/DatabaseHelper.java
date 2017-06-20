package com.example.varma.contacts.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Contacts.db";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_FRIENDS_TABLE = "CREATE TABLE " + FriendsDb.TABLE_FRIENDS
                + "("
                + FriendsDb.FRIENDS_ID + " TEXT PRIMARY KEY,"
                + FriendsDb.FRIENDS_USER_ID + " TEXT,"
                + FriendsDb.FRIENDS_NAME + " TEXT,"
                + FriendsDb.FRIENDS_NUMBER + " TEXT,"
                + FriendsDb.FRIENDS_EMAIL + " TEXT,"
                + FriendsDb.FRIENDS_IMAGE_URL + " TEXT,"
                + FriendsDb.FRIENDS_OLD_NUMBER + " TEXT"
                + ")";
        db.execSQL(CREATE_FRIENDS_TABLE);

        String CREATE_REQUESTS_TABLE = "CREATE TABLE " + RequestsDb.TABLE_REQUESTS
                + "("
                + RequestsDb.REQUEST_ID + " TEXT PRIMARY KEY,"
                + RequestsDb.REQUEST_SENDER_ID + " TEXT,"
                + RequestsDb.REQUEST_RECEIVER_ID + " TEXT,"
                + RequestsDb.REQUEST_IS_PENDING + " TEXT,"
                + RequestsDb.REQUEST_IS_ACCEPTED + " TEXT,"
                + RequestsDb.REQUEST_NAME + " TEXT,"
                + RequestsDb.REQUEST_IMAGE + " TEXT,"
                + RequestsDb.REQUEST_IS_SEND + " TEXT"
                + ")";
        db.execSQL(CREATE_REQUESTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + FriendsDb.TABLE_FRIENDS);
        db.execSQL("DROP TABLE IF EXISTS " + RequestsDb.TABLE_REQUESTS);

        // Create tables again
        onCreate(db);
    }


}
