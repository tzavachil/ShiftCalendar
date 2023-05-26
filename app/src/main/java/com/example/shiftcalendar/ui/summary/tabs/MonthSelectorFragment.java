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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shiftcalendar.R;
import com.example.shiftcalendar.Shift;
import com.example.shiftcalendar.ShiftDay;
import com.example.shiftcalendar.ShiftDayList;
import com.example.shiftcalendar.ui.DayDetailsBottomSheet;
import com.example.shiftcalendar.ui.summary.ExportOptionsBottomSheet;
import com.example.shiftcalendar.ui.summary.ShiftDayRecyclerData;
import com.example.shiftcalendar.ui.summary.ShiftRecyclerData;
import com.example.shiftcalendar.ui.summary.ShiftDayRecyclerViewAdapter;
import com.example.shiftcalendar.ui.summary.ShiftRecyclerViewAdapter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

public class MonthSelectorFragment extends SelectorFragment{

    private ImageButton previousMonthButton;
    private ImageButton nextMonthButton;
    private TextView monthTextView;
    private RecyclerView shiftRecyclerView;
    private RecyclerView shiftDayRecyclerView;
    private TextView totalHours;
    private TextView totalExtraIncome;
    private Button exportButton;

    private ShiftDayList shiftDayList;
    private ArrayList<ShiftDay> currShiftDayList;
    private ArrayList<ShiftDay> tempShiftDayList;

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

        this.totalHours = view.findViewById(R.id.totalHours);
        this.totalExtraIncome = view.findViewById(R.id.totalExtraIncome);

        this.monthTextView.setText(this.monthYearFromDate(now));
        this.searchOnList();

        this.previousMonthButton = view.findViewById(R.id.previousMonthButton);
        this.nextMonthButton = view.findViewById(R.id.nextMonthButton);
        this.exportButton = view.findViewById(R.id.exportButton);
        this.setUpListeners();
        this.paintLines(view);

        return view;
    }

    private void paintLines(View view){
        LinearLayout titleLayout = view.findViewById(R.id.monthYearLayout);
        this.changeLineColor(titleLayout);
        LinearLayout overviewLayout = view.findViewById(R.id.monthOverviewLayout);
        this.changeLineColor(overviewLayout);
        LinearLayout totalValuesLayout = view.findViewById(R.id.monthTotalValuesLayout);
        this.changeLineColor(totalValuesLayout);
    }

    @SuppressLint("ResourceAsColor")
    private void changeLineColor(LinearLayout layout){
        GradientDrawable lineBackground = (GradientDrawable) layout.getBackground();
        lineBackground.setStroke(3, ResourcesCompat.getColor(getResources(), R.color.dark_grey, null));
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
        this.exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExportOptions();
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

    private void showExportOptions(){
        ExportOptionsBottomSheet exportOptionsBottomSheet = new ExportOptionsBottomSheet(this.tempShiftDayList, this.monthTextView.getText().toString());
        exportOptionsBottomSheet.show(this.getActivity().getSupportFragmentManager(), "TAG");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void searchOnList(){
        int month = now.getMonth().getValue();
        int year = now.getYear();
        this.currShiftDayList = this.shiftDayList.searchByMonth(month, year);
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