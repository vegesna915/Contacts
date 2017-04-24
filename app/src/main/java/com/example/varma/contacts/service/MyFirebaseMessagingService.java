package com.example.varma.contacts.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.varma.contacts.Database.RequestsDb;
import com.example.varma.contacts.HomeActivity;
import com.example.varma.contacts.Objects.Request;
import com.example.varma.contacts.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;


public class MyFirebaseMessagingService extends FirebaseMessagingService {


    private final static String messageType_Request = "newRequest";
    private final static String messageType_Test = "Test";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        if (remoteMessage.getData().size() <= 0) {
            return;
        }


        try {

            JSONObject json = new JSONObject(remoteMessage.getData());

            switch (json.getString("messageType")) {


                case messageType_Request: {
                    newRequest(json);

                    break;
                }

                case messageType_Test: {

                    showNotification(json.getString("message"));

                    break;
                }


                default: {

                    break;
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



    private void showNotification(String message) {


        Intent i = new Intent(this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("FCM Test")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notification_small_icon)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    private void newRequest(JSONObject json) throws JSONException {

        Request request = new Request();

        request.setIS_SEND("0");
        request.setIS_PENDING("1");
        request.setIS_ACCEPTED("0");
        request.setREQUEST_ID(json.getString("REQUEST_ID"));
        request.setSENDER_ID(json.getString("SENDER_ID"));
        request.setRECEIVER_ID(json.getString("RECEIVER_ID"));
        request.set_Name(json.getString("SENDER_NAME"));
        request.setIMAGE_URL(json.getString("SENDER_IMAGE"));

        RequestsDb requestsDb = new RequestsDb(getApplicationContext());
        requestsDb.addRequest(request);

        Intent i = new Intent(this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("New Contact Request")
                .setContentText("From " + request.get_Name())
                .setSmallIcon(R.drawable.ic_notification_small_icon)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(Integer.parseInt(request.getREQUEST_ID()), builder.build());

    }
}
