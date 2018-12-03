package com.codex.easytourmanager.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codex.easytourmanager.fragments.ExpenseFragment;
import com.codex.easytourmanager.fragments.MomentFragment;

public class PagerViewAdapter extends FragmentPagerAdapter {
    public PagerViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                ExpenseFragment expenseFragment = new ExpenseFragment();
                return expenseFragment;

            case  1:
                MomentFragment momentFragment = new MomentFragment();
                return momentFragment;

        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
