package com.example.varma.contacts.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.provider.CallLog;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.varma.contacts.ContactInfoActivity;
import com.example.varma.contacts.Extra.PermissionsClass;
import com.example.varma.contacts.Objects.CallLogInfo;
import com.example.varma.contacts.R;

import java.util.ArrayList;


public class RecyclerViewAdapterCallLog extends RecyclerView.Adapter<RecyclerViewAdapterCallLog.MyViewHolderCallLog> {


    private ArrayList<CallLogInfo> callLogs = new ArrayList<>();
    private ArrayList<CallLogInfo> copyCallLogs = new ArrayList<>();
    private Context context;
    private Activity activity;

    public RecyclerViewAdapterCallLog(ArrayList<CallLogInfo> callLogs) {
        this.callLogs.addAll(callLogs);
        this.copyCallLogs.addAll(callLogs);
    }

    @Override
    public MyViewHolderCallLog onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        activity = (Activity) context;
        View view = LayoutInflater.from(context).inflate(R.layout.call_log_holder, parent, false);
        return new MyViewHolderCallLog(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolderCallLog holder, @SuppressLint("RecyclerView") final int position) {

        String callerName = callLogs.get(position).getCallerName();

        if (callerName == null) {

            holder.callerNameView.setText(callLogs.get(position).getCallernumber());
            holder.callerNumberView.setText("");

        } else {


            holder.callerNameView.setText(callerName);
            holder.callerNumberView.setText(callLogs.get(position).getCallernumber());

        }
        //----------------------------------------
        holder.copyCalls.setText(callLogs.get(position).getCopyCalls());
        setIconCallLog(callLogs.get(position), holder.callerIcon);


        String callDate = callLogs.get(position).getCalldate();
        holder.callTime.setText(callLogs.get(position).getCallTime());
        if (position == 0) {
            holder.callDate.setText(callDate);
            holder.callDate.setVisibility(View.VISIBLE);
            holder.horizontalDivider.setVisibility(View.GONE);
        } else if (callDate.equals(callLogs.get(position - 1).getCalldate())) {
            holder.callDate.setVisibility(View.GONE);
            holder.horizontalDivider.setVisibility(View.GONE);
        } else {
            holder.callDate.setText(callDate);
            holder.callDate.setVisibility(View.VISIBLE);
            holder.horizontalDivider.setVisibility(View.VISIBLE);
        }

        //---------------------------------------

        holder.callDetailsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermissionsClass.hasPermissionCallPhone(context)) {

                    String number = callLogs.get(position).getCallernumber().trim();
                    number = "tel:" + number;

                    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
                    try {
                        context.startActivity(callIntent);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }


                } else {
                    PermissionsClass.requestPermission(activity,
                            new String[]{PermissionsClass.CallPhone}, 701);
                }
            }

        });

        holder.infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toContactInfoActivity = new Intent(context, ContactInfoActivity.class);

                toContactInfoActivity.putExtra("contactId", "");
                toContactInfoActivity.putExtra("contactNumber", callLogs.get(position).getCallernumber());
                if (callLogs.get(position).getCallerName() == null) {
                    toContactInfoActivity.putExtra("contactName", callLogs.get(position).getCallernumber());
                } else {
                    toContactInfoActivity.putExtra("contactName", callLogs.get(position).getCallerName());
                }


                context.startActivity(toContactInfoActivity);
            }
        });

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

    public void filterCallLog(String query) {
        callLogs.clear();

        if (query.isEmpty()) {
            callLogs.addAll(copyCallLogs);
        } else {

            String name, number;
            query = query.trim().toLowerCase();

            for (CallLogInfo callLog : copyCallLogs) {

                name = callLog.getCallerName().toLowerCase();
                number = callLog.getCallernumber().toLowerCase();

                if (name.contains(query) || number.contains(query)) {
                    callLogs.add(callLog);
                }


            }
        }
        notifyDataSetChanged();
    }

    public void updateCallLog(ArrayList<CallLogInfo> callLogs) {
        this.callLogs = callLogs;
        notifyDataSetChanged();
    }

    class MyViewHolderCallLog extends RecyclerView.ViewHolder {

        TextView callerNameView, callerNumberView, callDate, callTime, copyCalls;
        View view, horizontalDivider;
        ImageView callerIcon;
        View callDetailsLayout, infoButton;


        MyViewHolderCallLog(View itemView) {
            super(itemView);

            view = itemView;
            horizontalDivider = view.findViewById(R.id.divider_callLog);
            callDate = (TextView) view.findViewById(R.id.date_callLog);
            callTime = (TextView) view.findViewById(R.id.time_callLog);
            callerNameView = (TextView) view.findViewById(R.id.callerName_callLog);
            callerNumberView = (TextView) view.findViewById(R.id.callerNumber_callLog);
            callerIcon = (ImageView) view.findViewById(R.id.profileIcon_callLog_fragment1);
            callDetailsLayout = view.findViewById(R.id.linearLayout_callLog_fragment1);
            infoButton = view.findViewById(R.id.callerInfo_button);
            copyCalls = (TextView) view.findViewById(R.id.copyCalls_callLog);
        }
    }


}
