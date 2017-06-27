package com.example.varma.contacts.Adapters;


import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.PersistableBundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.varma.contacts.Database.RequestsDb;
import com.example.varma.contacts.Fragments.ReceiveRequestFragment;
import com.example.varma.contacts.Objects.Request;
import com.example.varma.contacts.R;
import com.example.varma.contacts.service.RequestResponseJobService;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;


public class RecyclerViewAdapterReceiveRequest extends RecyclerView.Adapter<RecyclerViewAdapterReceiveRequest.MyViewHolder> {

    private Context context;
    private ArrayList<Request> requests = new ArrayList<>();
    private ReceiveRequestFragment receiveRequestFragment;

    public RecyclerViewAdapterReceiveRequest(ArrayList<Request> requests, ReceiveRequestFragment receiveRequestFragment) {
        this.requests = requests;
        this.receiveRequestFragment = receiveRequestFragment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.request_receive, parent, false);

        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        final Request request = requests.get(position);
        holder.nameView.setText(request.get_Name());

        if (!request.getIMAGE_URL().equals("")) {
            Glide.with(context).load(request.getIMAGE_URL()).dontAnimate().into(holder.circleImageView);
        } else {
            holder.circleImageView.setImageResource(R.drawable.ic_account_circle);
        }

        if (request.getIS_PENDING().equals("1")) {
            holder.responseLayout.setVisibility(View.VISIBLE);
            holder.responseText.setVisibility(View.GONE);
        } else {
            holder.responseLayout.setVisibility(View.GONE);
            holder.responseText.setVisibility(View.VISIBLE);
            if (request.getIS_ACCEPTED().equals("1")) {
                holder.responseText.setText("Request Accepted");
            } else {
                holder.responseText.setText("Request Rejected");
            }
        }

        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PersistableBundle bundle = new PersistableBundle();

                bundle.putString(RequestResponseJobService.REQUEST_RESPONSE, RequestResponseJobService.REQUEST_RESPONSE_ACCEPTED);
                bundle.putString(RequestResponseJobService.REQUEST_ID, request.getREQUEST_ID());
                bundle.putString(RequestResponseJobService.RECEIVER_ID, request.getRECEIVER_ID());
                bundle.putString(RequestResponseJobService.SENDER_ID, request.getSENDER_ID());

                int jobId = Integer.parseInt(request.getREQUEST_ID()) * 12;
                JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
                JobInfo jobInfo = new JobInfo.Builder(jobId, new ComponentName(context, RequestResponseJobService.class))
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        .setExtras(bundle)
                        .build();
                jobScheduler.schedule(jobInfo);

                RequestsDb requestsDb = new RequestsDb(context);
                requestsDb.requestAccepted(request.getREQUEST_ID());

                receiveRequestFragment.refreshRequests();

            }
        });

        holder.rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                PersistableBundle bundle = new PersistableBundle();
                bundle.putString(RequestResponseJobService.REQUEST_ID, RequestResponseJobService.REQUEST_RESPONSE_REJECTED);
                bundle.putString(RequestResponseJobService.REQUEST_ID, request.getREQUEST_ID());
                bundle.putString(RequestResponseJobService.RECEIVER_ID, request.getRECEIVER_ID());
                bundle.putString(RequestResponseJobService.SENDER_ID, request.getSENDER_ID());

                int jobId = Integer.parseInt(request.getREQUEST_ID()) * 110;
                JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
                JobInfo jobInfo = new JobInfo.Builder(jobId, new ComponentName(context, RequestResponseJobService.class))
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        .setExtras(bundle)
                        .build();
                jobScheduler.schedule(jobInfo);

                RequestsDb requestsDb = new RequestsDb(context);
                requestsDb.requestRejected(request.getREQUEST_ID());

                receiveRequestFragment.refreshRequests();
            }
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        CircularImageView circleImageView;
        TextView nameView, responseText;
        Button acceptButton, rejectButton;
        View responseLayout;

        private MyViewHolder(View view) {
            super(view);

            circleImageView = (CircularImageView) view.findViewById(R.id.image_received_request);
            nameView = (TextView) view.findViewById(R.id.name_received_request);
            acceptButton = (Button) view.findViewById(R.id.accept_received_request);
            rejectButton = (Button) view.findViewById(R.id.reject_received_request);
            responseLayout = view.findViewById(R.id.responseLayout);
            responseText = (TextView) view.findViewById(R.id.text_response_receive_request);
        }
    }
}
