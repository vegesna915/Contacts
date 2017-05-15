package com.example.varma.contacts.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.varma.contacts.Extra.Utilis;
import com.example.varma.contacts.FriendProfileActivity;
import com.example.varma.contacts.Objects.Friend;
import com.example.varma.contacts.R;

import java.util.ArrayList;

import com.mikhaellopez.circularimageview.CircularImageView;


public class RecyclerViewAdapterFriends extends RecyclerView.Adapter<RecyclerViewAdapterFriends.MyViewHolder> {

    private ArrayList<Friend> friends = new ArrayList<>();
    private Context context;
    private int color;

    public RecyclerViewAdapterFriends(ArrayList<Friend> friends) {
        this.friends.addAll(friends);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        View contactView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_holder, parent, false);

        return new MyViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position1) {
        final int position = position1;
        holder.contactNameView.setText(friends.get(position).get_NAME());
        holder.contactNumberView.setText(friends.get(position).get_NUMBER());

        String firstLetter = friends.get(position).getFirstLetter();

        Log.i("friendimage", friends.get(position).getIMAGE_URL());

        if (friends.get(position).getIMAGE_URL().equals("") && Utilis.internetConnectionStatus(context)) {
            holder.contactProfileIconView.setText(firstLetter);
            holder.contactProfileIconView.setVisibility(View.VISIBLE);
            holder.friendProfileIcon.setVisibility(View.GONE);
        } else {
            Glide.with(context)
                    .load(friends.get(position).getIMAGE_URL())
                    .dontAnimate()
                    .into(holder.friendProfileIcon);
            holder.contactProfileIconView.setVisibility(View.GONE);
            holder.friendProfileIcon.setVisibility(View.VISIBLE);
        }

        holder.contactProfileIconView.setText(firstLetter);

        holder.textDivider.setText(firstLetter);
        if (position == 0) {

            holder.textDivider.setVisibility(View.VISIBLE);
            holder.dividerHorizontal.setVisibility(View.GONE);

        } else if (firstLetter.equals(friends.get(position - 1).getFirstLetter())) {

            holder.textDivider.setVisibility(View.INVISIBLE);
            holder.dividerHorizontal.setVisibility(View.GONE);

        } else {

            holder.dividerHorizontal.setVisibility(View.VISIBLE);
            holder.textDivider.setVisibility(View.VISIBLE);
        }


        GradientDrawable drawable = (GradientDrawable) holder.contactProfileIconView.getBackground();


        color = Utilis.getContactColor(context, color);


        drawable.setColor(color);

        holder.viewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toFriendProfileActivity = new Intent(context, FriendProfileActivity.class);
                toFriendProfileActivity.putExtra("_ID", friends.get(position).get_ID());
                toFriendProfileActivity.putExtra("_NAME", friends.get(position).get_NAME());
                toFriendProfileActivity.putExtra("_EMAIL", friends.get(position).get_EMAIL());
                toFriendProfileActivity.putExtra("_NUMBER", friends.get(position).get_NUMBER());
                toFriendProfileActivity.putExtra("IMAGE_URL", friends.get(position).getIMAGE_URL());
                toFriendProfileActivity.putExtra("NUMBER_OLD", friends.get(position).get_NUMBER_OLD());
                toFriendProfileActivity.putExtra("FIRST_LETTER", friends.get(position).getFirstLetter());
                context.startActivity(toFriendProfileActivity);
            }
        });

    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView contactNameView, contactNumberView, contactProfileIconView, textDivider;
        View viewContact, dividerHorizontal;
        CircularImageView friendProfileIcon;


        MyViewHolder(View itemView) {
            super(itemView);
            dividerHorizontal = itemView.findViewById(R.id.contactDivider_fragment2_home);
            contactNameView = (TextView) itemView.findViewById(R.id.contactName_contact);
            contactNumberView = (TextView) itemView.findViewById(R.id.contactNumber_contact);
            contactProfileIconView = (TextView) itemView.findViewById(R.id.profileIcon_contact_fragment2);
            textDivider = (TextView) itemView.findViewById(R.id.textDivider_contact);
            viewContact = itemView.findViewById(R.id.contact);
            friendProfileIcon = (CircularImageView) itemView.findViewById(R.id.profileIcon_friend_fragment2);

        }
    }

    public void updateFriendsList(ArrayList<Friend> friends) {
        this.friends.clear();
        this.friends.addAll(friends);
        Log.i("onPause", "size : " + String.valueOf(friends.size()));
        notifyDataSetChanged();
    }

}
