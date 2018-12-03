package com.codex.easytourmanager;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.codex.easytourmanager.fragments.CoxBazarFragment;
import com.codex.easytourmanager.fragments.KhoiyachoraFragment;
import com.codex.easytourmanager.fragments.KomoldohoFragment;
import com.codex.easytourmanager.fragments.MohamayaFragment;

public class PlaceDetailsActivity extends AppCompatActivity implements
        CoxBazarFragment.coxBazarInterface, KomoldohoFragment.KomoldohoInterface, MohamayaFragment.MohamayaInterface,
        KhoiyachoraFragment.khoiyachoraInterface {





    Spinner spinner;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;


    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        fragmentManager =getSupportFragmentManager();
        spinner = findViewById(R.id.spinnerId);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 1:
                        goToCoxBazarFragment();
                        break;
                    case 2:
                        goToKomoldohoFragment();
                        break;
                    case 3:
                        goToMohamayaFragment();
                        break;
                    case 4:
                        goToKhoiyachoraFragment();
                        break;
                    case 5:
                        Toast.makeText(PlaceDetailsActivity.this, "will be add soon", Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                        Toast.makeText(PlaceDetailsActivity.this, "will be add soon", Toast.LENGTH_SHORT).show();
                        break;
                    case 7:
                        Toast.makeText(PlaceDetailsActivity.this, "will be add soon", Toast.LENGTH_SHORT).show();
                        break;
                    case 8:
                        Toast.makeText(PlaceDetailsActivity.this, "will be add soon", Toast.LENGTH_SHORT).show();
                        break;
                    case 9:
                        Toast.makeText(PlaceDetailsActivity.this, "will be add soon", Toast.LENGTH_SHORT).show();
                        break;



                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public void goToCoxBazarFragment() {
        fragmentTransaction =getSupportFragmentManager().beginTransaction();
        CoxBazarFragment coxBazarFragment = new CoxBazarFragment();
        fragmentTransaction.replace(R.id.fragmentContainer,coxBazarFragment);
        fragmentTransaction.commit();
    }


    @Override
    public void goToKomoldohoFragment() {
        fragmentTransaction =getSupportFragmentManager().beginTransaction();
        KomoldohoFragment komoldohoFragment = new KomoldohoFragment();
        fragmentTransaction.replace(R.id.fragmentContainer,komoldohoFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void goToMohamayaFragment() {
        fragmentTransaction =getSupportFragmentManager().beginTransaction();
        KomoldohoFragment komoldohoFragment = new KomoldohoFragment();
        fragmentTransaction.replace(R.id.fragmentContainer,komoldohoFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void goToKhoiyachoraFragment() {
        fragmentTransaction =getSupportFragmentManager().beginTransaction();
        KhoiyachoraFragment khoiyachoraFragment= new KhoiyachoraFragment();
        fragmentTransaction.replace(R.id.fragmentContainer,khoiyachoraFragment);
        fragmentTransaction.commit();
    }
}
