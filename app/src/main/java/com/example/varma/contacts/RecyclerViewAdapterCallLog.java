package com.example.varma.contacts;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.provider.CallLog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


class RecyclerViewAdapterCallLog extends RecyclerView.Adapter<RecyclerViewAdapterCallLog.MyViewHolderCallLog> {


    private ArrayList<CallLogInfo> callLogs;
    private Context context;

    RecyclerViewAdapterCallLog(ArrayList<CallLogInfo> callLogs) {
        this.callLogs = callLogs;

    }

    @Override
    public MyViewHolderCallLog onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        Log.i("calllogs", "1");
        View view = LayoutInflater.from(context).inflate(R.layout.call_log, parent, false);
        return new MyViewHolderCallLog(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolderCallLog holder, int position) {

        String callerName = callLogs.get(position).getCallerName();


        Log.i("calllogs", "1");
        if (callerName == null) {

            Log.i("calllogs", "2");
            holder.linearLayout.setVisibility(View.GONE);
            holder.callerNumber_Only.setVisibility(View.VISIBLE);


            holder.callerNumber_Only.setText(callLogs.get(position).getCallernumber());


        } else {

            Log.i("calllogs", "3");
            holder.callerNumber_Only.setVisibility(View.GONE);
            holder.linearLayout.setVisibility(View.VISIBLE);

            holder.callerNameView.setText(callerName);
            holder.callerNumberView.setText(callLogs.get(position).getCallernumber());
        }


        setIconCallLog(callLogs.get(position), holder.callerIcon);


    }

    @Override
    public int getItemCount() {
        return callLogs.size();
    }

    class MyViewHolderCallLog extends RecyclerView.ViewHolder {

        TextView callerNameView, callerNumberView, callerNumber_Only;
        View view;
        ImageView callerIcon;
        LinearLayout linearLayout;


        MyViewHolderCallLog(View itemView) {
            super(itemView);

            view = itemView;
            linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout_callLog_fragment1);
            callerNumber_Only = (TextView) view.findViewById(R.id.phoneNumber_only);
            callerNameView = (TextView) view.findViewById(R.id.callerName_callLog);
            callerNumberView = (TextView) view.findViewById(R.id.callerNumber_callLog);
            callerIcon = (ImageView) view.findViewById(R.id.profileIcon_callLog_fragment1);
        }
    }

    private void setIconCallLog(CallLogInfo callLog, ImageView imageView) {

        GradientDrawable drawable = (GradientDrawable) imageView.getBackground();

        switch (Integer.parseInt(callLog.getCallType())) {


            case CallLog.Calls.OUTGOING_TYPE: {

                imageView.setImageResource(R.drawable.ic_call_made);
                drawable.setColor(context.getResources().getColor(R.color.lightBlueA400));

                break;
            }
            case CallLog.Calls.INCOMING_TYPE: {
                imageView.setImageResource(R.drawable.ic_call_received);
                drawable.setColor(context.getResources().getColor(R.color.greenA400));
                break;

            }
            case CallLog.Calls.MISSED_TYPE: {

                imageView.setImageResource(R.drawable.ic_call_missed);
                drawable.setColor(context.getResources().getColor(R.color.orangeA400));
                break;
            }
            default: {
                break;
            }
        }
    }


}
