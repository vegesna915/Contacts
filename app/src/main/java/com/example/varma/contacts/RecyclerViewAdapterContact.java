package com.example.varma.contacts;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

class RecyclerViewAdapterContact extends RecyclerView.Adapter<RecyclerViewAdapterContact.MyViewHolder> {

    private ArrayList<Contact> contacts;
    private Context context;
    private int color;

    RecyclerViewAdapterContact(ArrayList<Contact> contacts) {
        this.contacts = contacts;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        View contactView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact, parent, false);

        return new MyViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.contactNameView.setText(contacts.get(position).getContactName());
        holder.contactNumberView.setText(contacts.get(position).getContactNumber());

        //GradientDrawable gradientDrawable =(GradientDrawable) holder.contactProfileIconView.getBackground();

        String c = Utilis.getFirstLetter(contacts.get(position).getContactName());
        holder.contactProfileIconView.setText(c);

        GradientDrawable drawable = (GradientDrawable) (holder.contactProfileIconView.getBackground());

        color = Utilis.getContactColor(context, color);
        drawable.setColor(color);

        holder.viewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toContactInfoActivity = new Intent(context, ContactInfoActivity.class);

                toContactInfoActivity.putExtra("contactId", contacts.get(position).getContactId());

                context.startActivity(toContactInfoActivity);
            }
        });


    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView contactNameView, contactNumberView, contactProfileIconView;
        View viewContact;

        MyViewHolder(View itemView) {
            super(itemView);
            contactNameView = (TextView) itemView.findViewById(R.id.contactName_contact);
            contactNumberView = (TextView) itemView.findViewById(R.id.contactNumber_contact);
            contactProfileIconView = (TextView) itemView.findViewById(R.id.profileIcon_contact_fragment2);
            viewContact = itemView;

        }
    }

}
