package com.codex.easytourmanager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.codex.easytourmanager.adapters.PagerViewAdapter;
import com.codex.easytourmanager.event_class.EventInfo;
import com.codex.easytourmanager.fragments.ExpenseFragment;
import com.codex.easytourmanager.fragments.MomentFragment;

public class EventActivity extends AppCompatActivity {
    Button momentButton, expenseButton;
    private EventInfo eventInfo;
    View moment,expense;
    private ViewPager viewPager;
    private PagerViewAdapter pagerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        momentButton = findViewById(R.id.momentFragment_button);
        expenseButton = findViewById(R.id.expense_frag_button);
        moment = findViewById(R.id.moment);
        expense = findViewById(R.id.expense);

        viewPager= findViewById(R.id.viewPager);
        pagerViewAdapter =new PagerViewAdapter(getSupportFragmentManager());

        viewPager.setAdapter(pagerViewAdapter);


        moment.setVisibility(View.INVISIBLE);
        eventInfo = (EventInfo)getIntent().getSerializableExtra("event");




        ExpenseFragment expenseFragment = new ExpenseFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        //Bundle bundle = new Bundle();
        //bundle.putSerializable("expensefromevent",eventInfo);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.exp_moment_container_layout, expenseFragment);
        transaction.commit();
        //Toast.makeText(this, eventInfo.getEventDestination(), Toast.LENGTH_SHORT).show();
        getIntent().putExtra("expensefromevent",eventInfo);
        //getIntent().putExtra("fromeventactivity",eventInfo)



        momentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMoments();
            }
        });

        expenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadExpenseList();
            }
        });

    }


    public void loadMoments() {

        expense.setVisibility(View.INVISIBLE);
        moment.setVisibility(View.VISIBLE);
        FragmentManager fm = getSupportFragmentManager();
        Fragment momentFragment = new MomentFragment();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.exp_moment_container_layout, momentFragment);
        ft.commit();


    }

    public void loadExpenseList() {

        moment.setVisibility(View.INVISIBLE);
        expense.setVisibility(View.VISIBLE);
        ExpenseFragment expenseFragment = new ExpenseFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.exp_moment_container_layout, expenseFragment);
        transaction.commit();
    }
}

