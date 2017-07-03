package com.example.varma.contacts.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.provider.CallLog;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.varma.contacts.ContactInfoActivity;
import com.example.varma.contacts.DialerPadActivity;
import com.example.varma.contacts.Extra.Caller;
import com.example.varma.contacts.Extra.Utils;
import com.example.varma.contacts.HomeActivity;
import com.example.varma.contacts.Interface.AdapterInterface_HomeFragment1;
import com.example.varma.contacts.Objects.CallLogInfo;
import com.example.varma.contacts.R;

import java.util.ArrayList;


public class RecyclerViewAdapterCallLog extends RecyclerView.Adapter<RecyclerViewAdapterCallLog.MyViewHolderCallLog> {


    private ArrayList<CallLogInfo> callLogs = new ArrayList<>();
    private Context context;
    private HomeActivity homeActivity;
    private int longClickPosition;

    public RecyclerViewAdapterCallLog(ArrayList<CallLogInfo> callLogs, HomeActivity homeActivity) {
        this.callLogs.addAll(callLogs);
        this.homeActivity = homeActivity;

        homeActivity.setAdapterInterface_homeFragment1(new AdapterInterface_HomeFragment1() {
            @Override
            public void passMenuItem(int menuItemId) {
                onClickContextMenu(menuItemId);
            }
        });

    }

    @Override
    public MyViewHolderCallLog onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.call_log_holder, parent, false);
        return new MyViewHolderCallLog(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolderCallLog holder, @SuppressLint("RecyclerView") final int position) {

        final CallLogInfo callLog = callLogs.get(position);
        String callerName = callLog.getCallerName();

        if (callerName == null || callerName.equals("")) {

            holder.callerNameView.setText(callLog.getCallernumber());
            holder.callerNumberView.setText("");

        } else {


            holder.callerNameView.setText(callerName);
            holder.callerNumberView.setText(callLog.getCallernumber());

        }
        //----------------------------------------
        holder.copyCalls.setText(callLog.getCopyCalls());
        setIconCallLog(callLog, holder.callerIcon);


        String callDate = callLog.getCalldate();
        holder.callTime.setText(callLog.getCallTime());
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
                Caller.callNumber(homeActivity, callLog.getCallernumber().trim());
            }

        });

        holder.infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toContactInfoActivity = new Intent(context, ContactInfoActivity.class);

                toContactInfoActivity.putExtra("contactId", "");
                toContactInfoActivity.putExtra("contactNumber", callLog.getCallernumber());
                if (callLog.getCallerName() == null) {
                    toContactInfoActivity.putExtra("contactName", callLog.getCallernumber());
                } else {
                    toContactInfoActivity.putExtra("contactName", callLog.getCallerName());
                }


                context.startActivity(toContactInfoActivity);
            }
        });

        holder.callDetailsLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                longClickPosition = position;
                homeActivity.contextMenu(v, 0);

                return true;
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

    public void updateCallLog(ArrayList<CallLogInfo> callLogs) {
        this.callLogs.clear();
        this.callLogs.addAll(callLogs);
        notifyDataSetChanged();
    }

    private void onClickContextMenu(int menuItemId) {


        CallLogInfo info = callLogs.get(longClickPosition);
        switch (menuItemId) {

            case 0: {
                //Call
                Caller.callNumber(homeActivity, info.getCallernumber().trim());
                break;
            }
            case 1: {
                //Message
                Caller.smsNumber(homeActivity, info.getCallernumber().trim());
                break;
            }

            case 2: {
                //Copy Number
                Utils.copyToClipBoard(context, info.getCallernumber());
                Toast.makeText(context, "Number copied", Toast.LENGTH_SHORT).show();
                break;
            }
            case 3: {
                //Edit Before Call

                Intent toDialerPadActivity = new Intent(homeActivity, DialerPadActivity.class);
                toDialerPadActivity.putExtra(DialerPadActivity.numberIntentExtra, info.getCallernumber().trim());
                homeActivity.startActivity(toDialerPadActivity);

                break;
            }
            default: {

                break;
            }
        }
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
/*public void filterCallLog(String query) {
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
    }*/