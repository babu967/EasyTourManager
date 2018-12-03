package com.codex.easytourmanager.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codex.easytourmanager.AddEventActivity;
import com.codex.easytourmanager.R;
import com.codex.easytourmanager.adapters.EventRecyclerViewAdapter;
import com.codex.easytourmanager.event_class.EventInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment {
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;

    private List<EventInfo> eventInfoList = new ArrayList<EventInfo>();
    private EventRecyclerViewAdapter adapter;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference rootReference, eventReference;
    private ProgressDialog progressDialog;



    public EventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView=inflater.inflate(R.layout.fragment_event, container, false);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        rootReference = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Events");

        floatingActionButton = convertView.findViewById(R.id.addEventFloatButton);
        recyclerView = convertView.findViewById(R.id.event_recycler_view);
        recyclerView.hasFixedSize();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        getAllEvents();



        progressDialog.show();
        DatabaseReference eventRef = rootReference.child("user").child(user.getUid()).child("event");
        eventRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventInfoList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    EventInfo eventInfo = snapshot.getValue(EventInfo.class);
                    eventInfoList.add(eventInfo);

                }
                adapter = new EventRecyclerViewAdapter(getContext(), eventInfoList);
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                progressDialog.dismiss();
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        //Toast.makeText(getActivity(), eventInfoList.get(0).getEventDestination(), Toast.LENGTH_SHORT).show();


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddEventActivity.class);
                startActivity(intent);
            }
        });


        return convertView;
    }

    public void getAllEvents() {

    }

}
