package com.codex.easytourmanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.codex.easytourmanager.adapters.EventRecyclerViewAdapter;
import com.codex.easytourmanager.event_class.EventInfo;
import com.codex.easytourmanager.event_class.SharedEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import static com.codex.easytourmanager.event_class.Utility.sharedEvent;

public class SharedEventActivity extends AppCompatActivity {

    RecyclerView sharedEventView;
    FloatingActionButton addSharedEventButton;
    DatabaseReference reference, sharedReference;
    java.util.List<EventInfo> infoList = new ArrayList<>();
    FirebaseUser user;
    String sharedUserKey,sharedEventKey;
  // public static SharedEvent sharedEvent;
    EventInfo eventInfo;

    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_event);
        sharedEventView = findViewById(R.id.shared_recycler_view);
        addSharedEventButton = findViewById(R.id.addSharedEventFloatButton);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading..");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        sharedEventView.setLayoutManager(layoutManager);
        sharedEventView.hasFixedSize();


        addSharedEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(SharedEventActivity.this);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                intentIntegrator.setPrompt("Scan");
                intentIntegrator.setCameraId(0);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setBarcodeImageEnabled(false);
                intentIntegrator.initiateScan();
            }
        });

        sharedReference = reference.child("user").child(user.getUid()).child("shared");
        sharedReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                infoList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    SharedEvent info = snapshot.getValue(SharedEvent.class);
                    EventInfo event = (EventInfo)info;
                    infoList.add(event);
                }

                EventRecyclerViewAdapter adapter = new EventRecyclerViewAdapter(SharedEventActivity.this, infoList);
                sharedEventView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        dialog.show();
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (intentResult!=null){
            if (intentResult.getContents()==null){
                dialog.dismiss();
                Toast.makeText(this, "Try Again please", Toast.LENGTH_SHORT).show();

            }else {
                //dialog.dismiss();
                Log.i("m111111","Into the database");
                getSharedData(intentResult.getContents());
                Toast.makeText(SharedEventActivity.this,sharedEventKey+" "+sharedUserKey , Toast.LENGTH_SHORT).show();
                //dialog.show();
                DatabaseReference shareReference = reference.child("user").child(sharedUserKey).child("event");
                shareReference.orderByChild("eventKey").equalTo(sharedEventKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                            eventInfo = snapshot.getValue(EventInfo.class);
                            Log.i("m22222","Databse read successful");
                            sharedEvent = new SharedEvent(eventInfo.getFromDate(),eventInfo.getToDate(),eventInfo.getEventBudget(),eventInfo.getEventDestination(),eventInfo.getEventKey(),sharedUserKey);

                        }
                        //Toast.makeText(SharedEventActivity.this, eventInfo.getEventDestination(), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(SharedEventActivity.this, sharedEvent.getUserKey(), Toast.LENGTH_SHORT).show();

                        String key = sharedReference.push().getKey();
                        sharedReference.child(sharedEvent.getEventKey()).setValue(sharedEvent);

                        sharedReference.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                infoList.clear();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                    Log.i("m3333333","Databse read successful");
                                    SharedEvent info = snapshot.getValue(SharedEvent.class);
                                    EventInfo event = (EventInfo)info;
                                    infoList.add(event);
                                }

                                EventRecyclerViewAdapter adapter = new EventRecyclerViewAdapter(SharedEventActivity.this, infoList);
                                sharedEventView.setAdapter(adapter);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                dialog.dismiss();
            }
        }
        super.onActivityResult(requestCode,resultCode,data);
    }

    public void getSharedData(String string){
        String[] item = string.split(" ");
        sharedUserKey = item[0].trim();
        sharedEventKey = item[1].trim();

    }
}
