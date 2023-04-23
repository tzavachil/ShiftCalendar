package com.example.shiftcalendar.ui.summary.tabs;

import android.util.Range;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.shiftcalendar.ShiftDayList;

public class MyViewPageAdapter extends FragmentStateAdapter {

    private ShiftDayList shiftDayList;

    public MyViewPageAdapter(@NonNull FragmentActivity fragmentActivity, ShiftDayList shiftDayList) {
        super(fragmentActivity);
        this.shiftDayList = shiftDayList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new MonthSelectorFragment(this.shiftDayList);
            case 1:
                return new YearSelectorFragment(this.shiftDayList);
            case 2:
                return new RangeSelectorFragment();
            default:
                return new MonthSelectorFragment(this.shiftDayList);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
