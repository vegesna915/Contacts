package com.example.varma.contacts.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.varma.contacts.Objects.Request;
import com.example.varma.contacts.R;
import java.util.ArrayList;


public class RequestsDb {

    private static final String TABLE_REQUESTS = "REQUESTS_TABLE";
    private static final String REQUEST_ID = "_ID";
    private static final String REQUEST_SENDER_ID = "SENDER_ID";
    private static final String REQUEST_RECEIVER_ID = "RECEIVER_ID";
    private static final String REQUEST_IS_PENDING = "IS_PENDING";
    private static final String REQUEST_IS_ACCEPTED = "IS_ACCEPTED";
    private static final String REQUEST_IS_SEND = "IS_SEND";
    private static final String REQUEST_NAME = "_NAME";
    private static final String REQUEST_IMAGE = "IMAGE_URL";
    private Context context;

    public RequestsDb(Context context) {
        this.context = context;
    }


    public void addRequest(Request request) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(REQUEST_ID, request.getREQUEST_ID());
        values.put(REQUEST_SENDER_ID, request.getSENDER_ID());
        values.put(REQUEST_RECEIVER_ID, request.getRECEIVER_ID());
        values.put(REQUEST_IS_PENDING, request.getIS_PENDING());
        values.put(REQUEST_IS_ACCEPTED, request.getIS_ACCEPTED());
        values.put(REQUEST_IS_SEND, request.getIS_SEND());
        values.put(REQUEST_NAME, request.get_Name());
        values.put(REQUEST_IMAGE, request.getIMAGE_URL());
        // Inserting Row
        db.insert(TABLE_REQUESTS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single friend
    public Request getRequest(String id) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Request request = null;
        String[] columns = null;

        String where = REQUEST_ID + "=?";

        String[] selectionArg = {id};

        Cursor cursor = db.query(TABLE_REQUESTS, columns, where, selectionArg, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                request = new Request();
                request.setREQUEST_ID(cursor.getString(cursor.getColumnIndex(REQUEST_ID)));
                request.setSENDER_ID(cursor.getString(cursor.getColumnIndex(REQUEST_SENDER_ID)));
                request.setRECEIVER_ID(cursor.getString(cursor.getColumnIndex(REQUEST_RECEIVER_ID)));
                request.setIS_PENDING(cursor.getString(cursor.getColumnIndex(REQUEST_IS_PENDING)));
                request.setIS_ACCEPTED(cursor.getString(cursor.getColumnIndex(REQUEST_IS_ACCEPTED)));
                request.setIS_SEND(cursor.getString(cursor.getColumnIndex(REQUEST_IS_SEND)));
                request.set_Name(cursor.getString(cursor.getColumnIndex(REQUEST_NAME)));
                request.setIMAGE_URL(cursor.getString(cursor.getColumnIndex(REQUEST_IMAGE)));
                // return contact

            }
            cursor.close();
        }

        db.close();
        return request;
    }

    // Getting All friend
    public ArrayList<Request> getAllRequests() {

        ArrayList<Request> requests = new ArrayList<>();
        Request request;

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_REQUESTS;

        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor != null) {
            while (cursor.moveToNext()) {
                request = new Request();
                request.setREQUEST_ID(cursor.getString(cursor.getColumnIndex(REQUEST_ID)));
                request.setSENDER_ID(cursor.getString(cursor.getColumnIndex(REQUEST_SENDER_ID)));
                request.setRECEIVER_ID(cursor.getString(cursor.getColumnIndex(REQUEST_RECEIVER_ID)));
                request.setIS_PENDING(cursor.getString(cursor.getColumnIndex(REQUEST_IS_PENDING)));
                request.setIS_ACCEPTED(cursor.getString(cursor.getColumnIndex(REQUEST_IS_ACCEPTED)));
                request.setIS_SEND(cursor.getString(cursor.getColumnIndex(REQUEST_IS_SEND)));
                request.set_Name(cursor.getString(cursor.getColumnIndex(REQUEST_NAME)));
                request.setIMAGE_URL(cursor.getString(cursor.getColumnIndex(REQUEST_IMAGE)));
                // Adding contact to list
                requests.add(request);
            }
            cursor.close();
        }
        db.close();
        // return contact list
        return requests;

    }

    public ArrayList<Request> getSendRequests() {
        ArrayList<Request> requests = new ArrayList<>();
        Request request;

        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.loginDetails), Context.MODE_PRIVATE);

        String _ID = sharedPref.getString(context.getString(R.string.userDatabaseId), "");

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_REQUESTS + " WHERE " +
                REQUEST_SENDER_ID + " = ? AND " +
                REQUEST_IS_PENDING + " = ?;";
        String[] selectArgs = {_ID, "1"};

        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, selectArgs);

        // looping through all rows and adding to list
        if (cursor != null) {
            while (cursor.moveToNext()) {
                request = new Request();
                request.setREQUEST_ID(cursor.getString(cursor.getColumnIndex(REQUEST_ID)));
                request.setSENDER_ID(cursor.getString(cursor.getColumnIndex(REQUEST_SENDER_ID)));
                request.setRECEIVER_ID(cursor.getString(cursor.getColumnIndex(REQUEST_RECEIVER_ID)));
                request.setIS_PENDING(cursor.getString(cursor.getColumnIndex(REQUEST_IS_PENDING)));
                request.setIS_ACCEPTED(cursor.getString(cursor.getColumnIndex(REQUEST_IS_ACCEPTED)));
                request.setIS_SEND(cursor.getString(cursor.getColumnIndex(REQUEST_IS_SEND)));
                request.set_Name(cursor.getString(cursor.getColumnIndex(REQUEST_NAME)));
                request.setIMAGE_URL(cursor.getString(cursor.getColumnIndex(REQUEST_IMAGE)));
                // Adding contact to list
                requests.add(request);
            }
            cursor.close();
        }
        db.close();
        // return contact list
        return requests;
    }

    public ArrayList<Request> getReceivedRequests() {
        ArrayList<Request> requests = new ArrayList<>();
        Request request;

        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.loginDetails), Context.MODE_PRIVATE);

        String _ID = sharedPref.getString(context.getString(R.string.userDatabaseId), "");

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_REQUESTS + " WHERE " +
                REQUEST_RECEIVER_ID + " = ? AND " +
                REQUEST_IS_PENDING + " = ?;";

        String[] selectArgs = {_ID, "1"};

        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, selectArgs);

        // looping through all rows and adding to list
        if (cursor != null) {
            while (cursor.moveToNext()) {
                request = new Request();
                request.setREQUEST_ID(cursor.getString(cursor.getColumnIndex(REQUEST_ID)));
                request.setSENDER_ID(cursor.getString(cursor.getColumnIndex(REQUEST_SENDER_ID)));
                request.setRECEIVER_ID(cursor.getString(cursor.getColumnIndex(REQUEST_RECEIVER_ID)));
                request.setIS_PENDING(cursor.getString(cursor.getColumnIndex(REQUEST_IS_PENDING)));
                request.setIS_ACCEPTED(cursor.getString(cursor.getColumnIndex(REQUEST_IS_ACCEPTED)));
                request.setIS_SEND(cursor.getString(cursor.getColumnIndex(REQUEST_IS_SEND)));
                request.set_Name(cursor.getString(cursor.getColumnIndex(REQUEST_NAME)));
                request.setIMAGE_URL(cursor.getString(cursor.getColumnIndex(REQUEST_IMAGE)));
                // Adding contact to list
                requests.add(request);
            }
            cursor.close();
        }
        db.close();
        // return contact list
        return requests;
    }

    // Getting friend Count
    public int getRequestsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_REQUESTS;
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
    public int updateRequest(Request request) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(REQUEST_IS_PENDING, request.getIS_PENDING());
        values.put(REQUEST_IS_ACCEPTED, request.getIS_ACCEPTED());

        // updating row

        String where = REQUEST_ID + " = ?";
        String[] selectionArgs = {String.valueOf(request.getREQUEST_ID())};
        int update = db.update(TABLE_REQUESTS, values, where, selectionArgs);
        db.close();
        return update;
    }

    // Deleting single contact
    public void deleteRequest(String _ID) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(TABLE_REQUESTS, REQUEST_ID + " = ?",
                new String[]{_ID});
        db.close();
    }

    public void deleteAllRequests() {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        db.delete(TABLE_REQUESTS, null, null);
    }

    public void requestAccepted(String _ID) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(REQUEST_IS_PENDING, "0");
        values.put(REQUEST_IS_ACCEPTED, "1");

        String where = REQUEST_ID + " = ?";
        String[] selectionArgs = {_ID};

        db.update(TABLE_REQUESTS, values, where, selectionArgs);
        db.close();
    }

}
