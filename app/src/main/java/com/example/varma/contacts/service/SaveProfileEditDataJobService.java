package com.example.varma.contacts.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.example.varma.contacts.Extra.WebServiceConnection;
import com.example.varma.contacts.R;

import org.json.JSONObject;


public class SaveProfileEditDataJobService extends JobService {

    SaveProfileEditDataAsync saveProfileEditDataAsync;

    @Override
    public boolean onStartJob(JobParameters params) {

        saveProfileEditDataAsync = new SaveProfileEditDataAsync(this, params);
        saveProfileEditDataAsync.execute((Void) null);


        return true;

    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }


    private class SaveProfileEditDataAsync extends AsyncTask<Void, Void, Boolean> {

        private Context context;
        private JobParameters params;

        private SaveProfileEditDataAsync(Context context, JobParameters params) {
            this.context = context;
            this.params = params;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {

            boolean jobReschedule;
            try {

                SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.loginDetails), Context.MODE_PRIVATE);

                String _ID = sharedPref.getString(context.getString(R.string.userDatabaseId), "");
                String GOOGLE_ID = sharedPref.getString(context.getString(R.string.userGmailId), "");
                String NAME = sharedPref.getString(context.getString(R.string.userName), "");
                String NUMBER = sharedPref.getString(context.getString(R.string.userNumber), "");


                String urlString = "http://byvarma.esy.es/New/updateUserData.php";
                String parameters = "_ID=" + _ID +
                        "&GOOGLE_ID=" + GOOGLE_ID +
                        "&_NAME=" + NAME +
                        "&_NUMBER=" + NUMBER;

                JSONObject json = WebServiceConnection.getData(urlString, parameters);


                String result = json.getString("updateComplete");
                jobReschedule = !(result.equals("1"));

            } catch (Exception e) {
                jobReschedule = true;
            }


            return jobReschedule;
        }

        @Override
        protected void onPostExecute(Boolean jobReschedule) {

            jobFinished(params, jobReschedule);

        }
    }

}
