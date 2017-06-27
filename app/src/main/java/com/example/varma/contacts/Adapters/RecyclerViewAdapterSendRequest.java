package com.example.varma.contacts.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.varma.contacts.Objects.Request;
import com.example.varma.contacts.R;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class RecyclerViewAdapterSendRequest extends RecyclerView.Adapter<RecyclerViewAdapterSendRequest.MyViewHolder> {

    private Context context;
    private ArrayList<Request> requests;

    public RecyclerViewAdapterSendRequest(ArrayList<Request> requests) {
        this.requests = requests;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.request_send, parent, false);

        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Request request = requests.get(position);
        holder.nameView.setText(request.get_Name());

        if (!request.getIMAGE_URL().equals("")) {
            Glide.with(context).load(request.getIMAGE_URL()).dontAnimate().into(holder.circleImageView);
        } else {
            holder.circleImageView.setImageResource(R.drawable.ic_account_circle);
        }

        if (request.getIS_PENDING().equals("1")) {
            holder.textView.setText("waiting for response");
        } else {
            if (request.getIS_ACCEPTED().equals("1")) {
                holder.textView.setText("Request Accepted");
            } else {
                holder.textView.setText("Request Rejected");
            }
        }


    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        CircularImageView circleImageView;
        TextView nameView, textView;

        private MyViewHolder(View view) {
            super(view);

            circleImageView = (CircularImageView) view.findViewById(R.id.image_send_request);
            nameView = (TextView) view.findViewById(R.id.name_send_request);
            textView = (TextView) view.findViewById(R.id.text_send_request);

        }
    }


}
