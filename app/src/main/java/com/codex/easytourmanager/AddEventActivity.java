package com.codex.easytourmanager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codex.easytourmanager.event_class.EventInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddEventActivity extends AppCompatActivity {
    EditText destinationEt, budgetEt;
    TextView fromDate, toDate;
    String fDate, tDate;
    Double budget;
    Button createEventBtn;
    EventInfo updateInfo;

    Calendar calendar = Calendar.getInstance();
    DatePickerDialog datePickerDialog;

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference rootref;


    List<EventInfo> infoList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        destinationEt = findViewById(R.id.destination_edit);
        budgetEt = findViewById(R.id.budget_edit);
        fromDate = findViewById(R.id.from_date_edit);
        toDate = findViewById(R.id.to_date_edit);
        createEventBtn = findViewById(R.id.createEventBtn);
        rootref = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();



        //DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        //final DatabaseReference eventRef = reference.child("user").child(user.getUid()).child("event");

        Intent intent = getIntent();
        if (intent.getExtras()!=null){

            updateInfo = (EventInfo) intent.getSerializableExtra("updateEvent");

            //Toast.makeText(this, updateInfo.getEventDestination(), Toast.LENGTH_SHORT).show();
            createEventBtn.setText("Update Event");
            destinationEt.setText(updateInfo.getEventDestination());
            budgetEt.setText(updateInfo.getEventBudget());
            fromDate.setText(updateInfo.getFromDate());
            toDate.setText(updateInfo.getToDate());
            createEventBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String destination = destinationEt.getText().toString();
                    String budget = budgetEt.getText().toString();
                    String datefrom = fromDate.getText().toString();
                    String dateto = toDate.getText().toString();
                    String key = updateInfo.getEventKey();
                    if (!TextUtils.isEmpty(destination)&&!TextUtils.isEmpty(budget)&&!TextUtils.isEmpty(datefrom)&&!TextUtils.isEmpty(dateto)){
                        EventInfo eventInfo = new EventInfo(datefrom,dateto,budget,destination,key);
                        rootref.child("user").child(user.getUid()).child("event").child(key).
                                setValue(eventInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                finish();
                                Toast.makeText(AddEventActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(AddEventActivity.this, "Failed, Try Again", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            });

        }else {
            createEventBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String destination = destinationEt.getText().toString();
                    String budgett = budgetEt.getText().toString();

                    if (!TextUtils.isEmpty(destination) && !TextUtils.isEmpty(budgett) && !TextUtils.isEmpty(fDate) && !TextUtils.isEmpty(tDate) && !TextUtils.isEmpty(budgett)) {
                        //budget = Double.valueOf(budgett);
                        String key = rootref.child("user").child(user.getUid()).push().getKey();
                        EventInfo eventInfo = new EventInfo(fDate, tDate, budgett, destination, key);
                        rootref.child("user").child(user.getUid()).child("event").child(key).setValue(eventInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                finish();

                                Toast.makeText(AddEventActivity.this, "Event Created", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddEventActivity.this, "Failed, Try Again", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }


                }
            });
        }

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFDate();
            }
        });
        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTDate();
            }
        });

    }

    public void getFDate() {
        datePickerDialog = new DatePickerDialog(AddEventActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                fDate = dayOfMonth + "." + month + "." + year;
                fromDate.setText(fDate);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void getTDate() {
        datePickerDialog = new DatePickerDialog(AddEventActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tDate = dayOfMonth + "." + month + "." + year;
                toDate.setText(tDate);

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


}
