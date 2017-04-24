package com.example.varma.contacts.AsyncTasks;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.varma.contacts.Database.RequestsDb;
import com.example.varma.contacts.Extra.WebServiceConnection;
import com.example.varma.contacts.Objects.Request;
import com.example.varma.contacts.R;

import org.json.JSONException;
import org.json.JSONObject;


public class SendRequest extends AsyncTask<Void, Void, JSONObject> {

    private Request request;
    private Activity activity;

    public SendRequest(Activity activity, String RECEIVER_ID, String name, String image) {
        this.request = new Request();
        this.request.setRECEIVER_ID(RECEIVER_ID);
        this.request.set_Name(name);
        this.request.setIMAGE_URL(image);
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected JSONObject doInBackground(Void... voids) {

        SharedPreferences sharedPref = activity.getSharedPreferences(
                activity.getString(R.string.loginDetails), Context.MODE_PRIVATE);
        request.setSENDER_ID(sharedPref.getString(activity.getString(R.string.userDatabaseId), ""));
        String SENDER_NAME = sharedPref.getString(activity.getString(R.string.userName), "");
        String SENDER_IMAGE = sharedPref.getString(activity.getString(R.string.userImageUrl), "");

        String url = "http://byvarma.esy.es/New/sendRequest.php";
        String params = "RECEIVER_ID=" + request.getRECEIVER_ID() +
                "&SENDER_ID=" + request.getSENDER_ID() +
                "&SENDER_NAME=" + SENDER_NAME +
                "&SENDER_IMAGE=" + SENDER_IMAGE;


        JSONObject json = WebServiceConnection.getData(url, params);

        if (json != null) {
            try {
                request.setREQUEST_ID(json.getString("REQUEST_ID"));
                request.setIS_ACCEPTED("0");
                request.setIS_PENDING("1");
                request.setIS_SEND("1");

                RequestsDb requestsDb = new RequestsDb(activity);
                requestsDb.addRequest(request);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        return json;
    }

    @Override
    protected void onPostExecute(JSONObject json) {
        if (json == null) {
            Toast.makeText(activity, "Failed to send request", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(activity, "Request Has been Send", Toast.LENGTH_SHORT).show();
        activity.finish();
    }
}
