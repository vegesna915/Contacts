package com.example.varma.contacts.service;


import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.example.varma.contacts.Extra.WebServiceConnection;
import com.example.varma.contacts.R;

import org.json.JSONException;
import org.json.JSONObject;

public class UnFriendJobService extends JobService {


    public static final String INPUT_FRIEND_ID = "FriendIdInput";


    @Override
    public boolean onStartJob(JobParameters params) {


        UnFriendAsyncTask unFriendAsyncTask = new UnFriendAsyncTask(params);
        unFriendAsyncTask.execute((Void) null);


        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }


    private class UnFriendAsyncTask extends AsyncTask<Void, Void, Boolean> {

        JobParameters params;

        UnFriendAsyncTask(JobParameters params) {
            this.params = params;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(Void... vales) {

            boolean jobDone = false;

            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.loginDetails), Context.MODE_PRIVATE);

            String _ID = sharedPref.getString(getString(R.string.userDatabaseId), "");
            String friendId = params.getExtras().getString(INPUT_FRIEND_ID, "");

            String url = "http://byvarma.esy.es/New/unFriend.php";
            String parameters = "_ID=" + _ID +
                    "&FRIEND_ID=" + friendId;

            JSONObject json = WebServiceConnection.getData(url, parameters);

            try {
                jobDone = json.getString("JOB_DONE").equals("1");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return jobDone;
        }

        @Override
        protected void onPostExecute(Boolean jobDone) {

            jobFinished(params, !jobDone);

        }
    }

}
