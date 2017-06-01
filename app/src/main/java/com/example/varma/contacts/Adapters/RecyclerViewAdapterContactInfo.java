package com.example.varma.contacts.Adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.provider.CallLog;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.varma.contacts.Objects.CallLogInfo;
import com.example.varma.contacts.R;

import java.util.ArrayList;



public class RecyclerViewAdapterContactInfo extends RecyclerView.Adapter<RecyclerViewAdapterContactInfo.myViewHolder> {

    private ArrayList<CallLogInfo> callLogs = new ArrayList<>();
    private Context context;

    public RecyclerViewAdapterContactInfo(ArrayList<CallLogInfo> callLogs) {
        this.callLogs.addAll(callLogs);
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.contact_info_recyclerview_item, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {

        holder.numberView.setText(callLogs.get(position).getOriginalNumber());
        holder.timeView.setText(callLogs.get(position).getCallTime());
        holder.durationView.setText(callLogs.get(position).getCallDuration());

        setIconCallLog(callLogs.get(position), holder.callIcon);

        String date = callLogs.get(position).getCalldate();
        if (position == 0) {
            holder.dateView.setText(date);
            holder.dateView.setVisibility(View.VISIBLE);
        } else if (date.equals(callLogs.get(position - 1).getCalldate())) {
            holder.dateView.setVisibility(View.GONE);
        } else {
            holder.dateView.setText(date);
            holder.dateView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return callLogs.size();
    }

    private void setIconCallLog(CallLogInfo callLog, ImageView imageView) {

        GradientDrawable drawable = (GradientDrawable) imageView.getBackground();

        switch (Integer.parseInt(callLog.getCallType())) {


            case CallLog.Calls.OUTGOING_TYPE: {

                imageView.setImageResource(R.drawable.ic_call_made);
                //drawable.setColor(context.getResources().getColor(R.color.lightBlueA400));
                drawable.setColor(ContextCompat.getColor(context, R.color.lightBlueA400));

                break;
            }
            case CallLog.Calls.INCOMING_TYPE: {
                imageView.setImageResource(R.drawable.ic_call_received);
                drawable.setColor(ContextCompat.getColor(context, R.color.greenA400));
                break;

            }
            case CallLog.Calls.MISSED_TYPE: {

                imageView.setImageResource(R.drawable.ic_call_missed);
                drawable.setColor(ContextCompat.getColor(context, R.color.orangeA400));
                break;
            }
            default: {
                break;
            }
        }
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        TextView dateView, numberView, timeView, durationView;
        ImageView callIcon;

        myViewHolder(View itemView) {
            super(itemView);

            dateView = (TextView) itemView.findViewById(R.id.date_contactInfo_item);
            numberView = (TextView) itemView.findViewById(R.id.callerNumber_contactInfo_item);
            durationView = (TextView) itemView.findViewById(R.id.callDuration_contactInfo_item);
            timeView = (TextView) itemView.findViewById(R.id.time_contactInfo_item);
            callIcon = (ImageView) itemView.findViewById(R.id.profileIcon_contactInfo_item);
        }
    }

}
