package com.example.varma.contacts.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;
import android.os.PersistableBundle;

import com.example.varma.contacts.Database.RequestsDb;
import com.example.varma.contacts.Extra.WebServiceConnection;

import org.json.JSONException;
import org.json.JSONObject;


public class RequestResponseJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {

        String requestResponse = params.getExtras().getString("REQUEST_RESPONSE", "");

        if (requestResponse.equals("REQUEST_ACCEPTED")) {

        }

        if (requestResponse.equals("REQUEST_REJECTED")) {
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

            REQUEST_ID = bundle.getString("REQUEST_ID");
            RECEIVER_ID = bundle.getString("RECEIVER_ID");
            SENDER_ID = bundle.getString("SENDER_ID");

            String url = "http://byvarma.esy.es/New/requestRejected.php";
            String parameters = "REQUEST_ID=" + REQUEST_ID
                    + "&RECEIVER_ID=" + RECEIVER_ID
                    + "&SENDER_ID=" + SENDER_ID;

            JSONObject json = WebServiceConnection.getData(url, parameters);

            if (json != null) {

                try {
                    isJobDone = json.getString("JOB_DONE").equals("1");

                    RequestsDb requestsDb = new RequestsDb(RequestResponseJobService.this);
                    requestsDb.requestRejected(REQUEST_ID);

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


}
