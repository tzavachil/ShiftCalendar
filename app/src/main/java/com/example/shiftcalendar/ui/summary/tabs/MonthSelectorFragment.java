package com.example.shiftcalendar.ui.summary.tabs;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shiftcalendar.R;
import com.example.shiftcalendar.Shift;
import com.example.shiftcalendar.ShiftDay;
import com.example.shiftcalendar.ShiftDayList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class MonthSelectorFragment extends Fragment {

    private ImageButton previousMonthButton;
    private ImageButton nextMonthButton;
    private TextView monthTextView;
    private GridLayout gridLayout;

    private ShiftDayList shiftDayList;
    private ArrayList<ShiftDay> currShiftDayList;

    private LocalDate now;

    public MonthSelectorFragment(ShiftDayList shiftDayList) {
        this.shiftDayList = shiftDayList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_month_selector, container, false);

        this.now = LocalDate.now();

        this.monthTextView = view.findViewById(R.id.monthTextView);
        this.gridLayout = view.findViewById(R.id.gridLayout);

        this.monthTextView.setText(this.monthYearFromDate(now));
        this.searchOnList();

        this.previousMonthButton = view.findViewById(R.id.previousMonthButton);
        this.nextMonthButton = view.findViewById(R.id.nextMonthButton);
        this.setUpListeners();

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String monthYearFromDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyy");
        return date.format(formatter);
    }

    private void setUpListeners(){
        this.previousMonthButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                now = now.minusMonths(1);
                monthTextView.setText(monthYearFromDate(now));
            }
        });
        this.nextMonthButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                now = now.plusMonths(1);
                monthTextView.setText(monthYearFromDate(now));
            }
        });
        this.monthTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchOnList();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void searchOnList(){
        String text = this.monthTextView.getText().toString();
        int month = now.getMonth().getValue();
        int year = now.getYear();
        this.currShiftDayList = this.shiftDayList.searchByMonth(month, year);
        this.displayList();
    }

    private void displayList(){
        int rows = this.countShiftsOnList();
        this.gridLayout.setRowCount(rows);

    }

    private int countShiftsOnList(){

        ArrayList<Shift> shifts = new ArrayList<>();

       for(ShiftDay shiftDay: this.currShiftDayList){
           Shift currShift = shiftDay.getShift();
           if(!shifts.contains(currShift)){
               shifts.add(currShift);
           }
       }

        return shifts.size();
    }

}