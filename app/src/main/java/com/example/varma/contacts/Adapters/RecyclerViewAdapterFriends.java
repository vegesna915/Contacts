package com.example.varma.contacts.Adapters;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.varma.contacts.Database.FriendsDb;
import com.example.varma.contacts.Extra.Caller;
import com.example.varma.contacts.Extra.Utils;
import com.example.varma.contacts.Fragments.HomeFragment_3;
import com.example.varma.contacts.FriendProfileActivity;
import com.example.varma.contacts.HomeActivity;
import com.example.varma.contacts.Interface.AdapterInterface_HomeFragment3;
import com.example.varma.contacts.Objects.Friend;
import com.example.varma.contacts.R;
import com.example.varma.contacts.service.UnFriendJobService;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;


public class RecyclerViewAdapterFriends extends RecyclerView.Adapter<RecyclerViewAdapterFriends.MyViewHolder> {

    private ArrayList<Friend> friends = new ArrayList<>();
    private Context context;
    private int color, longClickPosition;
    private HomeActivity homeActivity;
    private HomeFragment_3 homeFragment_3;

    public RecyclerViewAdapterFriends(ArrayList<Friend> friends, HomeActivity homeActivity, HomeFragment_3 homeFragment_3) {
        this.friends.addAll(friends);
        this.homeActivity = homeActivity;
        this.homeFragment_3 = homeFragment_3;
        this.homeActivity.setAdapterInterface_homeFragment3(new AdapterInterface_HomeFragment3() {
            @Override
            public void passMenuItem(int menuItemId) {
                onClickContextMenu(menuItemId);
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
    public void onBindViewHolder(final MyViewHolder holder, int position1) {
        final int position = position1;
        holder.contactNameView.setText(friends.get(position).get_NAME());
        holder.contactNumberView.setText(friends.get(position).get_NUMBER());

        String firstLetter = friends.get(position).getFirstLetter();


        if (friends.get(position).getIMAGE_URL().equals("") && Utils.internetConnectionStatus(context)) {
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


        color = Utils.getContactColor(context, color);


        drawable.setColor(color);

        holder.viewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toFriendProfileActivity = new Intent(context, FriendProfileActivity.class);
                toFriendProfileActivity.putExtra("FRIEND_ID", friends.get(position).get_ID());

                context.startActivity(toFriendProfileActivity);
            }
        });

        holder.viewContact.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                longClickPosition = position;
                homeActivity.contextMenu(v, 2);

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public void updateFriendsList(ArrayList<Friend> friends) {
        this.friends.clear();
        this.friends.addAll(friends);
        notifyDataSetChanged();
    }

    private void onClickContextMenu(int menuItemId) {


        Friend friend = friends.get(longClickPosition);
        switch (menuItemId) {

            case 0: {//Call

                Caller.callNumber(homeActivity, friend.get_NUMBER().trim());

                break;
            }
            case 1: {//Message

                Caller.smsNumber(homeActivity, friend.get_NUMBER().trim());

                break;
            }

            case 2: {//Copy Number

                Utils.copyToClipBoard(context, friend.get_NUMBER());
                Toast.makeText(context, "Number copied", Toast.LENGTH_SHORT).show();

                break;
            }
            case 3: {//Unfriend

                unfriend(friend);

                break;
            }
            default: {

                break;
            }
        }
    }

    private void unfriend(final Friend friend) {


        AlertDialog.Builder builder = new AlertDialog.Builder(homeActivity);
        builder.setCancelable(true)
                .setTitle("Unfriend")
                .setMessage("Do you want to unfriend " + friend.get_NAME() + "?")
                .setPositiveButton("unfriend", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PersistableBundle bundle = new PersistableBundle();
                        bundle.putString(UnFriendJobService.INPUT_FRIEND_ID, friend.get_ID());

                        JobScheduler jobScheduler = (JobScheduler) homeActivity.getSystemService(Context.JOB_SCHEDULER_SERVICE);

                        int jobId = Integer.parseInt(friend.get_ID());
                        JobInfo jobInfo = new JobInfo.Builder(
                                jobId, new ComponentName(homeActivity, UnFriendJobService.class))
                                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                                .setExtras(bundle)
                                .build();

                        jobScheduler.schedule(jobInfo);
                        FriendsDb friendDb = new FriendsDb(homeActivity);
                        friendDb.unFriend(friend.get_ID());

                        homeFragment_3.refreshFromAdapter();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create().show();
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


}
