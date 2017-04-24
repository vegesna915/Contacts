package com.example.varma.contacts.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Contacts.db";
    private static final String TABLE_FRIENDS = "FRIENDS_TABLE";
    private static final String TABLE_REQUESTS = "REQUESTS_TABLE";

    private static final String FRIENDS_ID = "_ID";
    private static final String FRIENDS_NAME = "_NAME";
    private static final String FRIENDS_EMAIL = "_EMAIL";
    private static final String FRIENDS_NUMBER = "_NUMBER";

    private static final String REQUEST_ID = "_ID";
    private static final String REQUEST_SENDER_ID = "SENDER_ID";
    private static final String REQUEST_RECEIVER_ID = "RECEIVER_ID";
    private static final String REQUEST_IS_PENDING = "IS_PENDING";
    private static final String REQUEST_IS_ACCEPTED = "IS_ACCEPTED";
    private static final String REQUEST_IS_SEND = "IS_SEND";
    private static final String REQUEST_NAME = "_NAME";
    private static final String REQUEST_IMAGE = "IMAGE_URL";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_FRIENDS_TABLE = "CREATE TABLE " + TABLE_FRIENDS
                + "("
                + FRIENDS_ID + " TEXT PRIMARY KEY,"
                + FRIENDS_NAME + " TEXT,"
                + FRIENDS_NUMBER + " TEXT,"
                + FRIENDS_EMAIL + " TEXT"
                + ")";
        db.execSQL(CREATE_FRIENDS_TABLE);

        String CREATE_REQUESTS_TABLE = "CREATE TABLE " + TABLE_REQUESTS
                + "("
                + REQUEST_ID + " TEXT PRIMARY KEY,"
                + REQUEST_SENDER_ID + " TEXT,"
                + REQUEST_RECEIVER_ID + " TEXT,"
                + REQUEST_IS_PENDING + " TEXT,"
                + REQUEST_IS_ACCEPTED + " TEXT,"
                + REQUEST_NAME + " TEXT,"
                + REQUEST_IMAGE + " TEXT,"
                + REQUEST_IS_SEND + " TEXT"
                + ")";
        db.execSQL(CREATE_REQUESTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIENDS);

        // Create tables again
        onCreate(db);
    }


}
