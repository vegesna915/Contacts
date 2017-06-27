package com.example.varma.contacts.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.varma.contacts.ContactInfoActivity;
import com.example.varma.contacts.Objects.DialerInfo;
import com.example.varma.contacts.R;

import java.util.ArrayList;


public class RecyclerViewAdapterDialPad extends RecyclerView.Adapter<RecyclerViewAdapterDialPad.MyViewHolder> {

    private ArrayList<DialerInfo> infos;
    private Context context;

    public RecyclerViewAdapterDialPad(ArrayList<DialerInfo> infos) {
        this.infos = infos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.dial_pad_recycler_view_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position1) {

        final int position = position1;

        if (infos.get(position).getName() == null) {
            holder.nameView.setText(infos.get(position).getNumber());
        } else {
            holder.nameView.setText(infos.get(position).getName());
        }


        holder.numberView.setText(infos.get(position).getNumber());
        holder.infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toContactInfoActivity = new Intent(context, ContactInfoActivity.class);
                toContactInfoActivity.putExtra("contactNumber", infos.get(position).getNumber());
                if (infos.get(position).getName() == null) {
                    toContactInfoActivity.putExtra("contactName", infos.get(position).getNumber());
                } else {
                    toContactInfoActivity.putExtra("contactName", infos.get(position).getName());
                }
                context.startActivity(toContactInfoActivity);
            }
        });

        holder.callLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = infos.get(position).getNumber().trim();
                number = number.replace("#", Uri.encode("#"));
                number = "tel:" + number;

                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
                try {
                    context.startActivity(callIntent);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return infos.size();
    }

    public void updateInfos(ArrayList<DialerInfo> infos) {
        this.infos = infos;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameView, numberView;
        ImageButton infoButton;
        View callLayout;

        MyViewHolder(View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.callerName_dialPadSearch);
            numberView = (TextView) itemView.findViewById(R.id.callerNumber_dialPadSearch);
            infoButton = (ImageButton) itemView.findViewById(R.id.callerInfo_button);
            callLayout = itemView.findViewById(R.id.linearLayout_dialPadSearch);
        }
    }

}
