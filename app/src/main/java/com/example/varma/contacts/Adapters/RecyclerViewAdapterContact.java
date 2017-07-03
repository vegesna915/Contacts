package com.example.varma.contacts.Adapters;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.varma.contacts.ContactInfoActivity;
import com.example.varma.contacts.Extra.Caller;
import com.example.varma.contacts.Extra.Utils;
import com.example.varma.contacts.HomeActivity;
import com.example.varma.contacts.Interface.AdapterInterface_HomeFragment2;
import com.example.varma.contacts.Objects.Contact;
import com.example.varma.contacts.R;

import java.util.ArrayList;

public class RecyclerViewAdapterContact extends RecyclerView.Adapter<RecyclerViewAdapterContact.MyViewHolder> {

    private ArrayList<Contact> copyContacts = new ArrayList<>();
    private ArrayList<Contact> contacts = new ArrayList<>();
    private Context context;
    private int color;
    private HomeActivity homeActivity;
    private int longClickPosition;

    public RecyclerViewAdapterContact(ArrayList<Contact> contacts, HomeActivity homeActivity) {
        this.copyContacts.addAll(contacts);
        this.contacts.addAll(contacts);
        this.homeActivity = homeActivity;

        homeActivity.setAdapterInterface_homeFragment2(new AdapterInterface_HomeFragment2() {
            @Override
            public void passMenuItem(int menuItemId) {

                onClickContextMenu(menuItemId);
            }

            @Override
            public void callFilterContacts(String query) {
                filterContacts(query);
            }
        });

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        View contactView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_holder, parent, false);

        return new MyViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {


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
            color = Utils.getContactColor(context, color);
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


        holder.viewContact.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                longClickPosition = position;

                homeActivity.contextMenu(v, 1);

                return true;
            }
        });


    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    private void filterContacts(String query) {


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

    public void refreshContacts(ArrayList<Contact> contacts) {
        this.contacts.clear();
        this.contacts.addAll(contacts);
        this.copyContacts.addAll(contacts);
        notifyDataSetChanged();
    }

    private void onClickContextMenu(int menuItemId) {


        Contact contact = contacts.get(longClickPosition);
        switch (menuItemId) {

            case 0: {
                //Call
                Caller.callNumber(homeActivity, contact.getContactNumber().trim());
                break;
            }
            case 1: {
                //Message
                Caller.smsNumber(homeActivity, contact.getContactNumber().trim());
                break;
            }

            case 2: {
                //Copy Number
                Utils.copyToClipBoard(context, contact.getContactNumber());
                Toast.makeText(context, "Number copied", Toast.LENGTH_SHORT).show();
                break;
            }
            case 3: {
                //Edit Contact
                Intent toEditContact = new Intent(Intent.ACTION_EDIT);
                Uri contentUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                        Long.parseLong(contact.getContactId()));
                toEditContact.setData(contentUri);
                homeActivity.startActivity(toEditContact);
                break;
            }
            default: {

                break;
            }
        }
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
