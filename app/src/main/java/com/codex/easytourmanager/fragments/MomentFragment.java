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

import com.codex.easytourmanager.AddMomentActivity;
import com.codex.easytourmanager.R;
import com.codex.easytourmanager.adapters.MomentViewAdapter;
import com.codex.easytourmanager.event_class.EventInfo;
import com.codex.easytourmanager.event_class.Moments;
import com.codex.easytourmanager.event_class.SharedEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MomentFragment extends Fragment {
    private RecyclerView momentView;
    private FloatingActionButton floatingActionButton;
    private FirebaseUser user;
    private DatabaseReference rootReference, momentReference;
    List<Moments> momentsList = new ArrayList<>();

    EventInfo eventInfo = new EventInfo();
    ProgressDialog progressDialog;


    public MomentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_moment, container, false);
        momentView = view.findViewById(R.id.moment_recycler_view);
        floatingActionButton = view.findViewById(R.id.addMomentFloatingButton);
        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage("Loading Moments");
        eventInfo = (EventInfo) getActivity().getIntent().getSerializableExtra("expensefromevent");

        user = FirebaseAuth.getInstance().getCurrentUser();
        rootReference = FirebaseDatabase.getInstance().getReference();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        momentView.setLayoutManager(layoutManager);
        momentView.hasFixedSize();

        progressDialog.show();
        momentReference = rootReference.child("user").child(user.getUid()).child("moment");

        if (eventInfo instanceof SharedEvent){
            SharedEvent sharedEvent = (SharedEvent) eventInfo;
            momentReference = rootReference.child("user").child(sharedEvent.getUserKey()).child("moment");

        }

        Query query = momentReference.orderByChild("eventKey").equalTo(eventInfo.getEventKey());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                momentsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Moments moment = snapshot.getValue(Moments.class);
                    momentsList.add(moment);
                }

                MomentViewAdapter adapter = new MomentViewAdapter(momentsList, getActivity());
                momentView.setAdapter(adapter);
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddMomentActivity.class);
                intent.putExtra("frommomentfrag",eventInfo);
                startActivity(intent);
            }
        });


        return view;
    }

}
