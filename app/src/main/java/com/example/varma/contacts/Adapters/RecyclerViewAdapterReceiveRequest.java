package com.example.varma.contacts.Adapters;


import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.varma.contacts.Database.FriendsDb;
import com.example.varma.contacts.Database.RequestsDb;
import com.example.varma.contacts.Extra.Utils;
import com.example.varma.contacts.Extra.WebServiceConnection;
import com.example.varma.contacts.Objects.Friend;
import com.example.varma.contacts.Objects.Request;
import com.example.varma.contacts.R;
import com.example.varma.contacts.service.RequestResponseJobService;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class RecyclerViewAdapterReceiveRequest extends RecyclerView.Adapter<RecyclerViewAdapterReceiveRequest.MyViewHolder> {

    private Context context;
    private ArrayList<Request> requests = new ArrayList<>();

    public RecyclerViewAdapterReceiveRequest(ArrayList<Request> requests) {
        this.requests.addAll(requests);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.request_receive, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final int position2 = position;

        final Request request = requests.get(position);
        holder.nameView.setText(request.get_Name());

        if (!request.getIMAGE_URL().equals("")) {
            Glide.with(context).load(request.getIMAGE_URL()).dontAnimate().into(holder.circleImageView);
        } else {
            holder.circleImageView.setImageResource(R.drawable.ic_account_circle);
        }

        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!Utils.internetConnectionStatus(context)) {
                    Toast.makeText(context, "Check Network Connection", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {

                    AcceptRequest acceptRequest = new AcceptRequest(view, position2);
                    acceptRequest.execute((Void) null);

                } catch (Exception e) {
                    Toast.makeText(context, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    view.setClickable(true);

                }

            }
        });

        holder.rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                PersistableBundle bundle = new PersistableBundle();
                bundle.putString("REQUEST_RESPONSE", "REQUEST_REJECTED");
                bundle.putString("REQUEST_ID", request.getREQUEST_ID());
                bundle.putString("RECEIVER_ID", request.getRECEIVER_ID());
                bundle.putString("SENDER_ID", request.getSENDER_ID());

                int jobId = Integer.parseInt(request.getREQUEST_ID());
                JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
                JobInfo jobInfo = new JobInfo.Builder(jobId, new ComponentName(context, RequestResponseJobService.class))
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        .setExtras(bundle)
                        .build();
                jobScheduler.schedule(jobInfo);

                requests.remove(position2);
                notifyItemRemoved(position2);
                notifyItemRangeChanged(position2, requests.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        CircularImageView circleImageView;
        TextView nameView;
        Button acceptButton, rejectButton;

        private MyViewHolder(View view) {
            super(view);

            circleImageView = (CircularImageView) view.findViewById(R.id.image_received_request);
            nameView = (TextView) view.findViewById(R.id.name_received_request);
            acceptButton = (Button) view.findViewById(R.id.accept_received_request);
            rejectButton = (Button) view.findViewById(R.id.reject_received_request);

        }
    }

    private class AcceptRequest extends AsyncTask<Void, Void, JSONObject> {

        Request request;
        View view;
        int position;

        private AcceptRequest(View view, int position) {
            this.request = requests.get(position);
            this.view = view;
            this.position = position;
        }

        @Override
        protected void onPreExecute() {

            view.setClickable(false);

        }

        @Override
        protected JSONObject doInBackground(Void... voids) {

            String url = "http://byvarma.esy.es/New/requestAccepted.php";
            String params = "REQUEST_ID=" + request.getREQUEST_ID() +
                    "&SENDER_ID=" + request.getSENDER_ID() +
                    "&RECEIVER_ID=" + request.getRECEIVER_ID();

            return WebServiceConnection.getData(url, params);

        }


        @Override
        protected void onPostExecute(JSONObject json) {
            if (json == null) {
                Toast.makeText(context, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                view.setClickable(true);
                return;
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

                FriendsDb friendDb = new FriendsDb(context);
                friendDb.addFriend(friend);

                RequestsDb requestDb = new RequestsDb(context);
                requestDb.requestAccepted(json.getString("REQUEST_ID"));


                requests.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, requests.size());
            } catch (JSONException e) {
                Toast.makeText(context, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                view.setClickable(true);
            }

        }
    }


}
