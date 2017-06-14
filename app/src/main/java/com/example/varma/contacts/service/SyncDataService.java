package com.example.varma.contacts.service;


import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.example.varma.contacts.Database.FriendsDb;
import com.example.varma.contacts.Database.RequestsDb;
import com.example.varma.contacts.Extra.WebServiceConnection;
import com.example.varma.contacts.Objects.Friend;
import com.example.varma.contacts.Objects.Request;
import com.example.varma.contacts.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SyncDataService extends IntentService {


    public SyncDataService() {
        super("SyncDataService");

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent == null) {
            return;
        }


        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.loginDetails), Context.MODE_PRIVATE);
        String _ID = sharedPref.getString(getString(R.string.userDatabaseId), "");


        String GOOGLE_ID = sharedPref.getString(getString(R.string.userGmailId), "");

        String urlString = "http://byvarma.esy.es/New/getAllRequests.php";
        String parameters = "_ID=" + _ID +
                "&GOOGLE_ID==" + GOOGLE_ID;


        JSONObject json = WebServiceConnection.getData(urlString, parameters);
        ArrayList<Request> requests = new ArrayList<>();

        if (json != null) {
            try {
                JSONArray requestsJsonArray = json.getJSONArray("requests");
                int j = 0;
                @SuppressLint("UseSparseArrays") HashMap<Integer, String> userDetails = new HashMap<>();
                for (int i = 0; i < requestsJsonArray.length(); i++) {

                    JSONObject requestJson = requestsJsonArray.getJSONObject(i);

                    Request request = new Request();

                    request.setRECEIVER_ID(requestJson.getString("RECEIVER_ID"));
                    request.setSENDER_ID(requestJson.getString("SENDER_ID"));
                    request.setREQUEST_ID(requestJson.getString("REQUEST_ID"));
                    request.setIS_ACCEPTED(requestJson.getString("IS_ACCEPTED"));
                    request.setIS_PENDING(requestJson.getString("IS_PENDING"));

                    if (requestJson.getString("SENDER_ID").equals(_ID)) {
                        request.setIS_SEND("1");
                        if (!userDetails.containsValue(request.getRECEIVER_ID())) {
                            userDetails.put(j, request.getRECEIVER_ID());
                            j++;
                        }
                    } else {
                        request.setIS_SEND("0");
                        if (!userDetails.containsValue(request.getSENDER_ID())) {
                            userDetails.put(j, request.getSENDER_ID());
                            j++;
                        }
                    }
                    requests.add(request);
                }

                String idsParameters2 = "";
                if (userDetails.size() > 0) {
                    idsParameters2 = userDetails.get(0);
                }


                for (int i = 1; i < userDetails.size() - 1; i++) {

                    idsParameters2 = idsParameters2 + "," + userDetails.get(i);

                }

                String urlString2 = "http://byvarma.esy.es/New/getSomeUsersData.php";
                String parameters2 = "STRING_IDS=" + idsParameters2;

                JSONObject json2 = WebServiceConnection.getData(urlString2, parameters2);

                JSONObject tempJson;

                Request request;
                for (int i = 0; i < requests.size(); i++) {
                    request = requests.get(i);
                    if (request.getIS_SEND().equals("1")) {
                        tempJson = json2.getJSONObject(request.getRECEIVER_ID());
                    } else {
                        tempJson = json2.getJSONObject(request.getSENDER_ID());
                    }
                    request.set_Name(tempJson.getString("_NAME"));
                    request.setIMAGE_URL(tempJson.getString("IMAGE_URL"));
                }

                RequestsDb requestDb = new RequestsDb(this);
                for (int i = 0; i < requests.size(); i++) {
                    requestDb.addRequest(requests.get(i));
                }


                // Getting all Friends Data

                String urlString3 = "http://byvarma.esy.es/New/getAllFriends.php";
                String parameters3 = "_ID=" + _ID +
                        "&GOOGLE_ID==" + GOOGLE_ID;

                JSONObject json3 = WebServiceConnection.getData(urlString3, parameters3);

                JSONArray friendsJsonArray = json3.getJSONArray("friends");

                JSONObject friendJson;
                Friend friend;
                FriendsDb friendsDb = new FriendsDb(this);
                for (int i = 0; i < friendsJsonArray.length(); i++) {

                    friendJson = friendsJsonArray.getJSONObject(i);
                    friend = new Friend();


                    friend.set_ID(friendJson.getString("_ID"));
                    friend.setUSER_ID(friendJson.getString("USER_ID"));
                    friend.set_NAME(friendJson.getString("_NAME"));
                    friend.set_NUMBER(friendJson.getString("_NUMBER"));
                    friend.setIMAGE_URL(friendJson.getString("IMAGE_URL"));
                    friend.set_EMAIL(friendJson.getString("_EMAIL"));
                    friend.set_NUMBER_OLD("");

                    friendsDb.addFriend(friend);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
