package com.codex.easytourmanager.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codex.easytourmanager.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CoxBazarFragment extends Fragment {

    public interface coxBazarInterface {

        void goToCoxBazarFragment();
    }


    public CoxBazarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cox_bazar, container, false);
    }

}
