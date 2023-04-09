package com.example.shiftcalendar.ui.summary.tabs;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
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
import android.widget.TextView;

import com.example.shiftcalendar.R;
import com.example.shiftcalendar.Shift;
import com.example.shiftcalendar.ShiftDay;
import com.example.shiftcalendar.ShiftDayList;
import com.example.shiftcalendar.ui.DayDetailsBottomSheet;
import com.example.shiftcalendar.ui.summary.ShiftDayRecyclerData;
import com.example.shiftcalendar.ui.summary.ShiftRecyclerData;
import com.example.shiftcalendar.ui.summary.ShiftDayRecyclerViewAdapter;
import com.example.shiftcalendar.ui.summary.ShiftRecyclerViewAdapter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MonthSelectorFragment extends Fragment {

    private ImageButton previousMonthButton;
    private ImageButton nextMonthButton;
    private TextView monthTextView;
    private RecyclerView shiftRecyclerView;
    private RecyclerView shiftDayRecyclerView;

    private ShiftDayList shiftDayList;
    private ArrayList<ShiftDay> currShiftDayList;

    private LocalDate now;

    public MonthSelectorFragment(ShiftDayList shiftDayList) {
        this.shiftDayList = shiftDayList;
        this.shiftDayList.sort();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_month_selector, container, false);

        this.now = LocalDate.now();

        this.monthTextView = view.findViewById(R.id.monthTextView);
        this.shiftRecyclerView = view.findViewById(R.id.shiftRecyclerView);
        this.shiftDayRecyclerView = view.findViewById(R.id.shiftDayRecyclerView);

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
        int month = now.getMonth().getValue();
        int year = now.getYear();
        this.currShiftDayList = this.shiftDayList.searchByMonth(month, year);
        this.displayList();
        this.displayOverview();
    }

    private void displayList(){
        try {
            ShiftRecyclerViewAdapter adapter = new ShiftRecyclerViewAdapter(this.getShiftsData(this.currShiftDayList), this.getContext());
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

    private void displayOverview(){
        ShiftDayRecyclerViewAdapter adapter = new ShiftDayRecyclerViewAdapter(this.getShiftDayData(this.currShiftDayList), this.getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());

        shiftDayRecyclerView.setLayoutManager(layoutManager);
        shiftDayRecyclerView.setAdapter(adapter);
    }

    private ArrayList<ShiftDayRecyclerData> getShiftDayData(ArrayList<ShiftDay> shiftDaysList){

        ArrayList<ShiftDayRecyclerData> shiftDays = new ArrayList<>();
        ShiftDayRecyclerData tempShiftDay;

        for(ShiftDay shiftDay : shiftDaysList){
            tempShiftDay = new ShiftDayRecyclerData(shiftDay);
            shiftDays.add(tempShiftDay);
        }


        return shiftDays;
    }

    private ArrayList<ShiftRecyclerData> getShiftsData(ArrayList<ShiftDay> shiftDaysList) throws ParseException {

        ArrayList<ShiftRecyclerData> shifts = new ArrayList<>();
        ShiftRecyclerData tempShift;

        for(ShiftDay shiftDay : shiftDaysList){

            String timeDifference = DayDetailsBottomSheet.timeDifferenceToString(shiftDay.getShift().getStartTime().toString(), shiftDay.getShift().getEndTime().toString());
            String shiftHoursText = timeDifference.split("/")[0];
            int shiftHours = Integer.parseInt(shiftHoursText.substring(0, shiftHoursText.length()-2));
            String shiftMinText = timeDifference.split("/")[1];
            int shiftMin = Integer.parseInt(shiftMinText.substring(0, shiftMinText.length()-2));
            int extraHours = shiftDay.getExtraTimeHours();
            int extraMin = shiftDay.getExtraTimeMin();
            int earlyHours = shiftDay.getEarlyExitHours();
            int earlyMin = shiftDay.getEarlyExitMin();
            shiftHours -= earlyHours;
            if(earlyMin > shiftMin){
                shiftMin = 60 - earlyMin + shiftMin;
                shiftHours--;
            }
            else{
                shiftMin -= earlyMin;
            }

            ShiftRecyclerData shiftRecyclerData = this.containsData(shifts, shiftDay.getShift());

            if(shiftRecyclerData == null){
                tempShift = new ShiftRecyclerData(shiftDay.getShift(), 1, shiftHours, shiftMin, extraHours, extraMin);
                shifts.add(tempShift);
            }
            else{
                shiftRecyclerData.increaseCount();
                shiftRecyclerData.increaseHours(shiftHours);
                shiftRecyclerData.increaseMin(shiftMin);
                shiftRecyclerData.increaseExtraHours(extraHours);
                shiftRecyclerData.increaseExtraMin(extraMin);
            }
        }

        Log.d("Debug", String.valueOf(shifts.size()));

        return shifts;
    }

    private ShiftRecyclerData containsData(ArrayList<ShiftRecyclerData> shifts, Shift shift){

        for(ShiftRecyclerData shiftRecyclerData: shifts){
            if(shiftRecyclerData.getShift() == shift) return shiftRecyclerData;
        }

        return null;
    }

}