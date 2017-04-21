package com.example.varma.contacts.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Contacts.db";
    private static final String TABLE_FRIENDS = "friends_table";
    private static final String TABLE_REQUESTS = "requests_table";
    private static final String FRIENDS_ID = "_ID";
    private static final String FRIENDS_NAME = "_NAME";
    private static final String FRIENDS_EMAIL = "_EMAIL";
    private static final String FRIENDS_NUMBER = "_NUMBER";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_FRIENDS
                + "("
                + FRIENDS_ID + " INTEGER PRIMARY KEY,"
                + FRIENDS_NAME + " TEXT,"
                + FRIENDS_NUMBER + " TEXT,"
                + FRIENDS_EMAIL + " TEXT"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIENDS);

        // Create tables again
        onCreate(db);
    }


}
