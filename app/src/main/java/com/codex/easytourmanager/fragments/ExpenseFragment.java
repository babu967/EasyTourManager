package com.codex.easytourmanager.fragments;


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

import com.codex.easytourmanager.AddExpenseActivity;
import com.codex.easytourmanager.R;
import com.codex.easytourmanager.adapters.ExpenseViewAdapter;
import com.codex.easytourmanager.event_class.EventInfo;
import com.codex.easytourmanager.event_class.Expense;
import com.codex.easytourmanager.event_class.SharedEvent;
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
public class ExpenseFragment extends Fragment {
    private RecyclerView expenseView;
    private FloatingActionButton floatingActionButton;

    private FirebaseUser user;
    private DatabaseReference reference, expenseReference;
    private List<Expense> expenseList = new ArrayList<>();
    private ExpenseViewAdapter adapter;


    private EventInfo eventInfo = new EventInfo();



    public ExpenseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_expense, container, false);
        expenseView = view.findViewById(R.id.expense_recycler_view);
        floatingActionButton = view.findViewById(R.id.addExpenseFloatingButton);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        expenseView.setLayoutManager(layoutManager);
        expenseView.hasFixedSize();

        //Bundle  bundle = getArguments();

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        expenseReference = reference.child("user").child(user.getUid()).child("expense");

        eventInfo = (EventInfo)getActivity().getIntent().getSerializableExtra("expensefromevent");

        if (eventInfo instanceof SharedEvent){
            SharedEvent sharedEvent = (SharedEvent) eventInfo;
           // Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
            expenseReference = reference.child("user").child(sharedEvent.getUserKey()).child("expense");
        }



        expenseReference.orderByChild("eventKey").equalTo(eventInfo.getEventKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                expenseList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Expense expense = snapshot.getValue(Expense.class);
                    expenseList.add(expense);
                }
                adapter = new ExpenseViewAdapter(expenseList, getContext());
                expenseView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddExpenseActivity.class);
                intent.putExtra("fromeventexpense", eventInfo);
                startActivity(intent);
            }
        });


        return view;
    }

}
