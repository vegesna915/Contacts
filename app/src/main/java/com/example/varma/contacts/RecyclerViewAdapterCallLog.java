package com.example.varma.contacts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Varma on 3/30/2017.
 */

public class RecyclerViewAdapterCallLog extends RecyclerView.Adapter<RecyclerViewAdapterCallLog.MyViewHolderCallLog> {


    ArrayList<CallLogInfo> callLogs;
    Context context;

    RecyclerViewAdapterCallLog(ArrayList<CallLogInfo> callLogs) {
        this.callLogs = callLogs;

    }

    @Override
    public MyViewHolderCallLog onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.call_log, parent, false);
        return new MyViewHolderCallLog(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolderCallLog holder, int position) {

        holder.callerNameView.setText(callLogs.get(position).getCallerName());
        holder.callerNumberView.setText(callLogs.get(position).getCallernumber());


    }

    @Override
    public int getItemCount() {
        return callLogs.size();
    }

    class MyViewHolderCallLog extends RecyclerView.ViewHolder {

        TextView callerNameView, callerNumberView;
        View view;
        ImageView callerIcon;

        public MyViewHolderCallLog(View itemView) {
            super(itemView);

            view = itemView;
            callerNameView = (TextView) view.findViewById(R.id.callerName_callLog);
            callerNumberView = (TextView) view.findViewById(R.id.callerNumber_callLog);
            callerIcon = (ImageView) view.findViewById(R.id.profileIcon_callLog_fragment1);
        }
    }
}
