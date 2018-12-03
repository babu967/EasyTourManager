package com.codex.easytourmanager;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codex.easytourmanager.event_class.EventInfo;
import com.codex.easytourmanager.event_class.Expense;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddExpenseActivity extends AppCompatActivity {
    private EditText expenseDetailsEt, expenseAmountEt;
    private Button addExpenseButton;
    private String currentDateTime;
    private Calendar calendar;
    private FirebaseUser user;
    private DatabaseReference rootReference;
    private EventInfo event = new EventInfo();
    private Expense expense = new Expense();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        expenseDetailsEt = findViewById(R.id.expense_details_edit);
        expenseAmountEt = findViewById(R.id.expense_amount_edit);
        addExpenseButton = findViewById(R.id.addExpense_button);


        calendar = Calendar.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        rootReference = FirebaseDatabase.getInstance().getReference();

        event = (EventInfo) getIntent().getSerializableExtra("fromeventexpense");

        expense = (Expense) getIntent().getSerializableExtra("expense");

        // Toast.makeText(this, event.getEventDestination(), Toast.LENGTH_SHORT).show();
        if (expense != null) {

            addExpenseButton.setText("Update Expense");

            expenseDetailsEt.setText(expense.getExpenseDetails());
            expenseAmountEt.setText(expense.getExpenseCost());

            addExpenseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String details = expenseDetailsEt.getText().toString();
                    String cost = expenseAmountEt.getText().toString();
                    if (!TextUtils.isEmpty(details) && !TextUtils.isEmpty(cost)){

                        String key = expense.getExpenseKey();
                        Expense expense1 = new Expense(details,currentDateTime,cost,key,expense.getEventKey());
                        rootReference.child("user").child(user.getUid()).child("expense").child(key).setValue(expense1).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                finish();

                                Toast.makeText(AddExpenseActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddExpenseActivity.this, "Update Failed Try Again", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                }
            });


        }else {

            addExpenseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //String key = reference.child("user").child(user.getUid()).child("expense").push().getKey();
                    String details = expenseDetailsEt.getText().toString();
                    String amount = expenseAmountEt.getText().toString();

                    if (!TextUtils.isEmpty(details) && !TextUtils.isEmpty(amount)) {
                        currentDateTime = getDateTime();
                        String expenseKey = rootReference.child("user").child(user.getUid()).child("expense").push().getKey();
                        Expense expense = new Expense(details, currentDateTime, amount, expenseKey, event.getEventKey());

                        rootReference.child("user").child(user.getUid()).child("expense").child(expenseKey).setValue(expense).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddExpenseActivity.this, "Failed, Try again", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }

                }
            });

        }



    }

    public String getDateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy   hh:mm aa");
        return simpleDateFormat.format(calendar.getTime());

    }


}



