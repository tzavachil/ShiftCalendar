package com.example.shiftcalendar.ui.summary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.shiftcalendar.R;
import com.example.shiftcalendar.ShiftDayList;
import com.example.shiftcalendar.databinding.FragmentSummaryBinding;
import com.example.shiftcalendar.ui.summary.tabs.MyViewPageAdapter;
import com.google.android.material.tabs.TabLayout;

public class SummaryFragment extends Fragment {

    private FragmentSummaryBinding binding;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private MyViewPageAdapter myViewPageAdapter;
    private ShiftDayList shiftDayList;

    public SummaryFragment(ShiftDayList shiftDayList){
        this.shiftDayList = shiftDayList;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSummaryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        this.tabLayout = root.findViewById(R.id.tabLayout);
        this.viewPager2 = root.findViewById(R.id.viewPager);
        myViewPageAdapter = new MyViewPageAdapter(this.getActivity(), this.shiftDayList);
        viewPager2.setAdapter(myViewPageAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}