package com.example.varma.contacts.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.varma.contacts.Adapters.RecyclerViewAdapterSendRequest;
import com.example.varma.contacts.Database.RequestsDb;
import com.example.varma.contacts.Objects.Request;
import com.example.varma.contacts.R;

import java.util.ArrayList;

public class SendRequestFragment extends Fragment {

    ArrayList<Request> requests = new ArrayList<>();
    Context context;
    RecyclerView recyclerViewSend;
    RecyclerViewAdapterSendRequest adapterSendRequest;
    TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        context = inflater.getContext();
        View view = inflater.inflate(R.layout.fragment_send_request, container, false);
        recyclerViewSend = (RecyclerView) view.findViewById(R.id.recyclerView_send_requests);
        textView = (TextView) view.findViewById(R.id.textView_sendRequest_fragment);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapterSendRequest = new RecyclerViewAdapterSendRequest(requests);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);

        recyclerViewSend.setLayoutManager(linearLayoutManager);
        recyclerViewSend.setAdapter(adapterSendRequest);
    }

    public void refreshRequests() {
        RequestsDb requestsDb = new RequestsDb(context);
        requests.clear();
        requests.addAll(requestsDb.getSendRequests());

        adapterSendRequest.notifyDataSetChanged();

        checkRequests();
    }

    void checkRequests() {

        if (requests.size() > 0) {
            recyclerViewSend.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        } else {
            recyclerViewSend.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        refreshRequests();
    }
}
