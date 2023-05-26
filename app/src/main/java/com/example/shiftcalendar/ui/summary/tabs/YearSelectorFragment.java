package com.example.shiftcalendar.ui.summary.tabs;

import android.annotation.SuppressLint;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shiftcalendar.R;
import com.example.shiftcalendar.Shift;
import com.example.shiftcalendar.ShiftDay;
import com.example.shiftcalendar.ShiftDayList;
import com.example.shiftcalendar.ui.summary.ShiftDayRecyclerData;
import com.example.shiftcalendar.ui.summary.ShiftDayRecyclerViewAdapter;
import com.example.shiftcalendar.ui.summary.ShiftRecyclerData;
import com.example.shiftcalendar.ui.summary.ShiftRecyclerViewAdapter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class YearSelectorFragment extends SelectorFragment {

    private TextView yearTextView;
    private ImageButton previousYearButton;
    private ImageButton nextYearButton;
    private RecyclerView shiftRecyclerView;
    private RecyclerView shiftDayRecyclerView;
    private TextView totalHours;
    private TextView totalExtraIncome;

    private ShiftDayList shiftDayList;
    private ArrayList<ShiftDay> currShiftDayList;
    private ArrayList<ShiftDay> tempShiftDayList;

    private LocalDate now;

    public YearSelectorFragment(ShiftDayList shiftDayList) {
        this.shiftDayList = shiftDayList;
        this.shiftDayList.sort();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_year_selector, container, false);

        this.now = LocalDate.now();

        this.yearTextView = view.findViewById(R.id.yearTextView);
        this.shiftRecyclerView = view.findViewById(R.id.shiftRecyclerView);
        this.shiftDayRecyclerView = view.findViewById(R.id.shiftDayRecyclerView);

        this.totalHours = view.findViewById(R.id.totalHours);
        this.totalExtraIncome = view.findViewById(R.id.totalExtraIncome);

        this.yearTextView.setText(this.yearFromDate(now));
        this.searchOnList();

        this.previousYearButton = view.findViewById(R.id.previousYearButton);
        this.nextYearButton = view.findViewById(R.id.nextYearButton);
        this.setUpListeners();

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String yearFromDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy");
        return date.format(formatter);
    }

    private void setUpListeners(){
        this.previousYearButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                now = now.minusYears(1);
                yearTextView.setText(yearFromDate(now));
            }
        });
        this.nextYearButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                now = now.plusYears(1);
                yearTextView.setText(yearFromDate(now));
            }
        });
        this.yearTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchOnList();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void searchOnList(){
        int year = now.getYear();
        this.currShiftDayList = this.shiftDayList.searchByYear(year);
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