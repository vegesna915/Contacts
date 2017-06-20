package com.example.varma.contacts.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.example.varma.contacts.Database.FriendsDb;
import com.example.varma.contacts.Database.RequestsDb;
import com.example.varma.contacts.FriendProfileActivity;
import com.example.varma.contacts.HomeActivity;
import com.example.varma.contacts.Objects.Friend;
import com.example.varma.contacts.Objects.Request;
import com.example.varma.contacts.R;
import com.example.varma.contacts.RequestsActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;


public class MyFirebaseMessagingService extends FirebaseMessagingService {


    private final static String messageType_request = "newRequest";
    private final static String messageType_test = "Test";
    private final static String messageType_requestAccepted = "requestAccepted";
    private final static String messageType_updateFriendData = "updateFriendsData";
    private final static String messageType_requestRejected = "requestRejected";
    private final static String messageType_unFriend = "unFriend";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.loginDetails), Context.MODE_PRIVATE);
        boolean isLogin = sharedPref.getBoolean(getString(R.string.loginStatus), false);


        if (remoteMessage.getData().size() <= 0) {
            return;
        }


        try {

            JSONObject json = new JSONObject(remoteMessage.getData());

            switch (json.getString("messageType")) {


                case messageType_request: {

                    if (isLogin) {
                        newRequest(json);
                    }

                    break;
                }

                case messageType_test: {

                    showNotification(json.getString("message"));

                    break;
                }

                case messageType_requestAccepted: {

                    if (isLogin) {
                        requestAccepted(json);

                    }


                    break;
                }

                case messageType_updateFriendData: {
                    if (isLogin) {
                        updateFriendData(json);
                    }

                    break;
                }

                case messageType_requestRejected: {
                    if (isLogin) {
                        requestRejected(json);
                    }
                    break;
                }

                case messageType_unFriend: {
                    if (isLogin) {
                        unFriend(json);
                    }
                    break;
                }

                default: {

                    defaultNotification();
                    break;
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void unFriend(JSONObject json) throws JSONException {


        Intent toHomeActivity = new Intent(this, HomeActivity.class);
        toHomeActivity.putExtra(getString(R.string.putExtraPage_HomeActivity), "2");
        toHomeActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());

        stackBuilder.addNextIntent(toHomeActivity);
        // Gets a PendingIntent containing the entire back stack
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        FriendsDb friendDb = new FriendsDb(this);

        Friend friend;
        String friendId = json.getString("FRIEND_ID");
        friend = friendDb.getFriendById(friendId);
        friendDb.unFriend(friendId);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("In Contact")
                .setContentText(friend.get_NAME() + " has unfriended you")
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_notification_small_icon)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(Integer.parseInt(friendId), builder.build());
    }

    private void defaultNotification() {
        Intent toRequestActivity = new Intent(this, RequestsActivity.class);
        toRequestActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());

        // Adds the back stack
        stackBuilder.addParentStack(RequestsActivity.class);
        // Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(toRequestActivity);
        // Gets a PendingIntent containing the entire back stack
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("In Contact")
                .setContentText("Default")
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_notification_small_icon)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }

    private void requestAccepted(JSONObject json) throws JSONException {

        Friend friend = new Friend();

        friend.set_ID(json.getString("_ID"));
        friend.setUSER_ID(json.getString("USER_ID"));
        friend.set_NAME(json.getString("_NAME"));
        friend.set_NUMBER(json.getString("_NUMBER"));
        friend.set_EMAIL(json.getString("_EMAIL"));
        friend.setIMAGE_URL(json.getString("IMAGE_URL"));
        friend.set_NUMBER_OLD("");

        FriendsDb friendDb = new FriendsDb(getApplicationContext());
        friendDb.addFriend(friend);

        RequestsDb requestDb = new RequestsDb(getApplicationContext());
        requestDb.requestAccepted(json.getString("REQUEST_ID"));

        Intent toRequestActivity = new Intent(this, RequestsActivity.class);
        toRequestActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());

        // Adds the back stack
        stackBuilder.addParentStack(RequestsActivity.class);
        // Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(toRequestActivity);
        // Gets a PendingIntent containing the entire back stack
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("In Contact")
                .setContentText("Friend Request Accepted By " + friend.get_NAME())
                .setSound(notificationSound)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_notification_small_icon)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(Integer.parseInt(friend.get_ID()), builder.build());

    }

    private void requestRejected(JSONObject json) throws JSONException {

        RequestsDb requestsDb = new RequestsDb(this);
        requestsDb.requestRejected(json.getString("REQUEST_ID"));


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

        Intent toRequestActivity = new Intent(this, RequestsActivity.class);
        toRequestActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());

        // Adds the back stack
        stackBuilder.addParentStack(RequestsActivity.class);
        // Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(toRequestActivity);
        // Gets a PendingIntent containing the entire back stack
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("In Contact")
                .setContentText("New Contact Request From " + request.get_Name())
                .setSmallIcon(R.drawable.ic_notification_small_icon)
                .setSound(notificationSound)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(Integer.parseInt(request.getREQUEST_ID()), builder.build());

    }

    private void showNotification(String message) {


        Intent i = new Intent(this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("FCM Test")
                .setContentText(message)
                .setSound(notificationSound)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_notification_small_icon)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    private void updateFriendData(JSONObject json) throws JSONException {

        Friend friend = new Friend();
        friend.set_ID(json.getString("FRIEND_ID"));
        friend.set_NAME(json.getString("FRIEND_NAME"));
        friend.set_NUMBER(json.getString("FRIEND_NUMBER"));

        FriendsDb friendsDb = new FriendsDb(this);
        friendsDb.updateFriend(friend);

        Intent toRequestActivity = new Intent(this, FriendProfileActivity.class);
        toRequestActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());

        // Adds the back stack
        stackBuilder.addParentStack(FriendProfileActivity.class);

        // Gets a PendingIntent containing the entire back stack
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("In Contact")
                .setContentText(friend.get_NAME() + " has updated his profile ")
                .setSmallIcon(R.drawable.ic_notification_small_icon)
                .setSound(notificationSound)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(Integer.parseInt(friend.get_ID()), builder.build());

    }

}
