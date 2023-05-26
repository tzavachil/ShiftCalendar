package com.example.shiftcalendar.ui.summary.tabs;

import android.annotation.SuppressLint;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shiftcalendar.R;
import com.example.shiftcalendar.ShiftDay;
import com.example.shiftcalendar.ShiftDayList;
import com.example.shiftcalendar.ui.summary.ShiftDayRecyclerData;
import com.example.shiftcalendar.ui.summary.ShiftDayRecyclerViewAdapter;
import com.example.shiftcalendar.ui.summary.ShiftRecyclerViewAdapter;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class RangeSelectorFragment extends SelectorFragment {

    private TextView fromTextView;
    private TextView toTextView;
    private RecyclerView shiftRecyclerView;
    private RecyclerView shiftDayRecyclerView;
    private TextView totalHours;
    private TextView totalExtraIncome;


    private ShiftDayList shiftDayList;
    private ArrayList<ShiftDay> currShiftDayList;
    private ArrayList<ShiftDay> tempShiftDayList;

    private LocalDate now;

    public RangeSelectorFragment(ShiftDayList shiftDayList) {
        this.shiftDayList = shiftDayList;
        this.shiftDayList.sort();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_range_selector, container, false);

        this.now = LocalDate.now();

        this.fromTextView = view.findViewById(R.id.fromTextView);
        this.toTextView = view.findViewById(R.id.toTextView);
        this.shiftRecyclerView = view.findViewById(R.id.shiftRecyclerView);
        this.shiftDayRecyclerView = view.findViewById(R.id.shiftDayRecyclerView);

        this.totalHours = view.findViewById(R.id.totalHours);
        this.totalExtraIncome = view.findViewById(R.id.totalExtraIncome);

        this.fromTextView.setText(this.dateToString(now));
        this.toTextView.setText(this.dateToString(now));
        this.searchOnList(this.now, this.now);

        this.setUpListeners();

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String dateToString(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }

    private void setUpListeners(){
        this.fromTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerRange();
            }
        });
        this.toTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerRange();
            }
        });
    }

    private void openDatePickerRange(){
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.dateRangePicker().setTheme(R.style.ThemeMaterialCalendar);
        materialDateBuilder.setTitleText("SELECT A DATE");
        MaterialDatePicker materialDatePicker = materialDateBuilder.build();
        materialDatePicker.show(this.getParentFragmentManager(), "MATERIAL_DATE_PICKER");

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                Pair<Long, Long> datePair = (Pair) selection;
                fromTextView.setText(convertTimeToDate(datePair.first));
                toTextView.setText(convertTimeToDate(datePair.second));
                LocalDate from = Instant.ofEpochMilli(datePair.first).atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate to = Instant.ofEpochMilli(datePair.second).atZone(ZoneId.systemDefault()).toLocalDate();
                searchOnList(from, to);
            }
        });

    }

    private String convertTimeToDate(Long time){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(c.getTime());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void searchOnList(LocalDate from, LocalDate to){
        this.currShiftDayList = this.shiftDayList.searchByRange(from, to);
        this.tempShiftDayList = new ArrayList<>(this.currShiftDayList);
        this.displayList();
        this.displayOverview(this.currShiftDayList);
    }

    private void displayList(){
        try {
            ShiftRecyclerViewAdapter adapter = new ShiftRecyclerViewAdapter(this.getShiftsData(this.currShiftDayList), this.getContext(), this);
            GridLayoutManager layoutManager = new GridLayoutManager(this.getContext(), 1){
                @Override
                public boolean canScrollVertically(){
                    return false;
                }
                @Override
                public boolean canScrollHorizontally(){
                    return false;
                }
            };

            shiftRecyclerView.setLayoutManager(layoutManager);
            shiftRecyclerView.setAdapter(adapter);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void displayOverview(ArrayList<ShiftDay> overviewList){
        ShiftDayRecyclerViewAdapter adapter = new ShiftDayRecyclerViewAdapter(this.getShiftDayData(overviewList), this.getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());

        try {
            this.setTotalValues(overviewList);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        shiftDayRecyclerView.setLayoutManager(layoutManager);
        shiftDayRecyclerView.setAdapter(adapter);
    }

    @Override
    public void setTotalValues(ArrayList<ShiftDay> overviewList) throws ParseException {

        int hours = 0;
        int min = 0;
        double income = 0;

        for(ShiftDay shiftDay: overviewList){
            String time = ShiftDayRecyclerData.calculateHours(shiftDay);

            int tempHours = Integer.parseInt(time.split(" ")[0]);
            int tempMin = Integer.parseInt(time.split(" ")[2]);

            hours += tempHours;
            min += tempMin;
            if(min > 59){
                min -= 60;
                hours ++;
            }

            income += shiftDay.getExtraIncome();
        }

        String hoursStr = String.valueOf(hours);
        if(hours < 10) hoursStr = "0" + hoursStr;
        String minStr = String.valueOf(min);
        if(min < 10) minStr = "0" + minStr;

        this.totalHours.setText(hoursStr + " h " + minStr + " m");
        this.totalExtraIncome.setText(String.valueOf(income));
    }

    @Override
    public void displayOverviewWithoutShift(String shiftName){
        ArrayList<ShiftDay> removingElements = new ArrayList<>();
        for(ShiftDay shiftDay: this.tempShiftDayList){
            if(shiftDay.getShift().getName().equals(shiftName)){
                removingElements.add(shiftDay);
            }
        }
        this.tempShiftDayList.removeAll(removingElements);
        this.displayOverview(this.tempShiftDayList);
    }

    @Override
    public void displayOverviewWithShift(String shiftName){
        ArrayList<ShiftDay> addingElements = new ArrayList<>();
        for(ShiftDay shiftDay: this.currShiftDayList){
            if(shiftDay.getShift().getName().equals(shiftName)){
                addingElements.add(shiftDay);
            }
        }
        this.tempShiftDayList.addAll(addingElements);
        this.tempShiftDayList.sort(new Comparator<ShiftDay>() {
            @Override
            public int compare(ShiftDay shiftDay, ShiftDay t1) {
                return shiftDay.compareTo(t1);
            }
        });
        this.displayOverview(this.tempShiftDayList);
    }

}