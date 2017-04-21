package com.example.varma.contacts.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.varma.contacts.Objects.Contact;
import com.example.varma.contacts.ContactInfoActivity;
import com.example.varma.contacts.R;
import com.example.varma.contacts.Extra.Utilis;

import java.util.ArrayList;

public class RecyclerViewAdapterContact extends RecyclerView.Adapter<RecyclerViewAdapterContact.MyViewHolder> {

    private ArrayList<Contact> copyContacts = new ArrayList<>();
    private ArrayList<Contact> contacts = new ArrayList<>();
    private Context context;
    private int color;

    public RecyclerViewAdapterContact(ArrayList<Contact> contacts) {
        this.copyContacts.addAll(contacts);
        this.contacts.addAll(contacts);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        View contactView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_holder, parent, false);

        return new MyViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.contactNameView.setText(contacts.get(position).getContactName());
        holder.contactNumberView.setText(contacts.get(position).getContactNumber());

        String contactFirstLetter = contacts.get(position).getContactFirstLetter();
        holder.contactProfileIconView.setText(contactFirstLetter);


        if (position == 0) {
            holder.textDivider.setText(contactFirstLetter);
            holder.textDivider.setVisibility(View.VISIBLE);
            holder.dividerHorizontal.setVisibility(View.GONE);
        } else if (contactFirstLetter.equals(contacts.get(position - 1).getContactFirstLetter())) {
            holder.textDivider.setText(contactFirstLetter);
            holder.textDivider.setVisibility(View.INVISIBLE);
            holder.dividerHorizontal.setVisibility(View.GONE);
        } else {
            holder.textDivider.setText(contactFirstLetter);
            holder.dividerHorizontal.setVisibility(View.VISIBLE);
            holder.textDivider.setVisibility(View.VISIBLE);
        }


        GradientDrawable drawable = (GradientDrawable) holder.contactProfileIconView.getBackground();


        if (contacts.get(position).getContactColor() == 0) {
            color = Utilis.getContactColor(context, color);
            contacts.get(position).setContactColor(color);
        } else {
            color = contacts.get(position).getContactColor();
        }


        drawable.setColor(color);

        holder.viewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toContactInfoActivity = new Intent(context, ContactInfoActivity.class);

                toContactInfoActivity.putExtra("contactId", contacts.get(position).getContactId());
                toContactInfoActivity.putExtra("contactNumber", contacts.get(position).getContactNumber());
                toContactInfoActivity.putExtra("contactName", contacts.get(position).getContactName());


                context.startActivity(toContactInfoActivity);
            }
        });


    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void filterContacts(String query) {


        contacts.clear();

        if (query.isEmpty()) {
            contacts.addAll(copyContacts);
        } else {

            String name, number;
            query = query.trim().toLowerCase();

            for (Contact contact : copyContacts) {

                name = contact.getContactName().toLowerCase();
                number = contact.getContactNumber().toLowerCase();

                if (name.contains(query) || number.contains(query)) {
                    contacts.add(contact);
                }


            }
        }
        notifyDataSetChanged();

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView contactNameView, contactNumberView, contactProfileIconView, textDivider;
        View viewContact, dividerHorizontal;


        MyViewHolder(View itemView) {
            super(itemView);
            dividerHorizontal = itemView.findViewById(R.id.contactDivider_fragment2_home);
            contactNameView = (TextView) itemView.findViewById(R.id.contactName_contact);
            contactNumberView = (TextView) itemView.findViewById(R.id.contactNumber_contact);
            contactProfileIconView = (TextView) itemView.findViewById(R.id.profileIcon_contact_fragment2);
            textDivider = (TextView) itemView.findViewById(R.id.textDivider_contact);
            viewContact = itemView.findViewById(R.id.contact);

        }
    }


}
