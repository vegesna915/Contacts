package com.example.varma.contacts.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;
import android.os.PersistableBundle;

import com.example.varma.contacts.Database.FriendsDb;
import com.example.varma.contacts.Extra.WebServiceConnection;
import com.example.varma.contacts.Objects.Friend;

import org.json.JSONException;
import org.json.JSONObject;


public class RequestResponseJobService extends JobService {


    public static final String REQUEST_RESPONSE = "REQUEST_RESPONSE";
    public static final String REQUEST_RESPONSE_ACCEPTED = "REQUEST_RESPONSE_ACCEPTED";
    public static final String REQUEST_RESPONSE_REJECTED = "REQUEST_RESPONSE_REJECTED";
    public static final String REQUEST_ID = "REQUEST_ID";
    public static final String RECEIVER_ID = "RECEIVER_ID";
    public static final String SENDER_ID = "SENDER_ID";


    @Override
    public boolean onStartJob(JobParameters params) {

        String requestResponse = params.getExtras().getString(RequestResponseJobService.REQUEST_RESPONSE, "");

        if (requestResponse.equals(RequestResponseJobService.REQUEST_RESPONSE_ACCEPTED)) {
            AcceptRequestAsyncTask acceptRequestAsyncTask = new AcceptRequestAsyncTask(params);
            acceptRequestAsyncTask.execute((Void) null);
        }

        if (requestResponse.equals(RequestResponseJobService.REQUEST_RESPONSE_REJECTED)) {
            RequestRejectAsyncTask rejectAsyncTask = new RequestRejectAsyncTask(params);
            rejectAsyncTask.execute((Void) null);
        }


        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

    private class RequestRejectAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private JobParameters params;

        RequestRejectAsyncTask(JobParameters params) {
            this.params = params;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(Void... param) {

            boolean isJobDone = false;

            String REQUEST_ID, RECEIVER_ID, SENDER_ID;

            PersistableBundle bundle = params.getExtras();

            REQUEST_ID = bundle.getString(RequestResponseJobService.REQUEST_ID);
            RECEIVER_ID = bundle.getString(RequestResponseJobService.RECEIVER_ID);
            SENDER_ID = bundle.getString(RequestResponseJobService.SENDER_ID);

            String url = "http://byvarma.esy.es/New/requestRejected.php";
            String parameters = "REQUEST_ID=" + REQUEST_ID
                    + "&RECEIVER_ID=" + RECEIVER_ID
                    + "&SENDER_ID=" + SENDER_ID;

            JSONObject json = WebServiceConnection.getData(url, parameters);

            if (json != null) {

                try {
                    isJobDone = json.getString("JOB_DONE").equals("1");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            return !isJobDone;
        }

        @Override
        protected void onPostExecute(Boolean isJobFailed) {

            jobFinished(params, isJobFailed);

        }
    }


    private class AcceptRequestAsyncTask extends AsyncTask<Void, Void, Boolean> {

        JobParameters params;

        private AcceptRequestAsyncTask(JobParameters params) {
            this.params = params;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(Void... voids) {


            String REQUEST_ID, RECEIVER_ID, SENDER_ID;

            PersistableBundle bundle = params.getExtras();

            REQUEST_ID = bundle.getString(RequestResponseJobService.REQUEST_ID);
            RECEIVER_ID = bundle.getString(RequestResponseJobService.RECEIVER_ID);
            SENDER_ID = bundle.getString(RequestResponseJobService.SENDER_ID);


            String url = "http://byvarma.esy.es/New/requestAccepted.php";
            String params = "REQUEST_ID=" + REQUEST_ID +
                    "&SENDER_ID=" + SENDER_ID +
                    "&RECEIVER_ID=" + RECEIVER_ID;

            JSONObject json = WebServiceConnection.getData(url, params);

            if (json == null) {
                return false;
            }

            try {
                Friend friend = new Friend();
                friend.set_ID(json.getString("_ID"));
                friend.setUSER_ID(json.getString("USER_ID"));
                friend.set_NAME(json.getString("_NAME"));
                friend.set_NUMBER(json.getString("_NUMBER"));
                friend.set_EMAIL(json.getString("_EMAIL"));
                friend.setIMAGE_URL(json.getString("IMAGE_URL"));
                friend.set_NUMBER_OLD("");


                FriendsDb friendDb = new FriendsDb(RequestResponseJobService.this);
                friendDb.addFriend(friend);
                return true;

            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        }


        @Override
        protected void onPostExecute(Boolean jobDone) {

            jobFinished(params, !jobDone);

        }
    }



}
