package com.example.shiftcalendar.ui.calendar;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiftcalendar.R;
import com.example.shiftcalendar.Shift;
import com.example.shiftcalendar.ShiftDay;
import com.example.shiftcalendar.ShiftDayList;
import com.example.shiftcalendar.ui.DayDetailsBottomSheet;

import org.apache.commons.math3.distribution.LogisticDistribution;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Locale;

public class CalendarViewHolder extends RecyclerView.ViewHolder implements Serializable {

    private TextView dayOfMonth;
    private ConstraintLayout layout;

    private Fragment rootFragment;
    private FrameLayout frameLayout;

    private LocalDate now;
    private TextView monthYearTV;

    private ArrayList<Shift> shiftList;
    private ShiftDayList shiftDayList;
    private ShiftDay myShiftDay;

    public CalendarViewHolder(@NonNull View itemView, TextView monthYearTV, Fragment rootFragment, ArrayList<Shift> sL, ShiftDayList sDL){
        super(itemView);
        this.rootFragment = rootFragment;
        frameLayout = (FrameLayout) rootFragment.getView().findViewById(R.id.fragmentContainer);
        dayOfMonth = (TextView) itemView.findViewById(R.id.cellDayText);
        layout = (ConstraintLayout) itemView.findViewById(R.id.cellLayout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!dayOfMonth.getText().toString().equals("")) {
                    showBottomSheet();
                }
            }
        });

        now = LocalDate.now();
        this.monthYearTV = monthYearTV;
        this.shiftList = sL;
        this.shiftDayList = sDL;

        this.myShiftDay = this.createCurrentShiftDay();

    }

    private void showBottomSheet(){
        this.myShiftDay.setDay(this.dayOfMonth.getText().toString());
        DayDetailsBottomSheet bottomSheetDayDetails = new DayDetailsBottomSheet(this.getDateToString(), this.myShiftDay, this.shiftList, this);
        bottomSheetDayDetails.show(rootFragment.getActivity().getSupportFragmentManager(), "TAG");
    }

    public void displayShiftHolder(){
        String thisDay = this.dayOfMonth.getText().toString();
        if(!thisDay.equals("")) {
            String[] tempTable = monthYearTV.getText().toString().split(" ");
            String thisMonth = tempTable[0];
            String thisYear = tempTable[1];

            if (thisDay.length() == 1)
                thisDay = "0" + thisDay;
            DateTimeFormatter parser = DateTimeFormatter.ofPattern("MMMM").withLocale(Locale.ENGLISH);
            TemporalAccessor accessor = parser.parse(thisMonth);
            int dayInteger = Integer.parseInt(thisDay);
            int monthInteger = accessor.get(ChronoField.MONTH_OF_YEAR);
            int yearInteger = Integer.parseInt(thisYear);
            ShiftDay currShiftDay = this.shiftDayList.contains(dayInteger, monthInteger, yearInteger);
            if(currShiftDay != null){
                //Paint the holder with day's shift
                this.setBackgroundColor(currShiftDay.getShift().getBackgroundColor());
                if(!this.isToday(thisDay))
                    this.setTextColor();
                else {
                    this.setTextColor(Color.BLACK);
                    this.dayOfMonth.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                }
                this.myShiftDay = currShiftDay;
            }
        }
    }

    private String getDateToString(){
        String thisDay = this.dayOfMonth.getText().toString();
        String[] tempTable = monthYearTV.getText().toString().split(" ");
        String thisMonth = tempTable[0];
        String thisYear = tempTable[1];

        if(thisDay.length()==1)
            thisDay = "0" + thisDay;
        String tempDate = thisDay + "-" + thisMonth + "-" + thisYear;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy");
        LocalDate date = LocalDate.parse(tempDate, formatter);
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        return date.format(displayFormatter);

    }

    public void setText(String text){

        if(isToday(text)) {
            this.setTodayTextColor();
        }
        this.dayOfMonth.setText(text);
    }

    public void saveDayChanges(ShiftDay currShiftDay){
        this.shiftDayList.addDay(currShiftDay);
        this.shiftDayList.print();
        this.shiftDayList.sort();
    }

    public ShiftDay createCurrentShiftDay(){

        int length = this.monthYearTV.getText().toString().length();
        int currYear = Integer.parseInt(this.monthYearTV.getText().toString().substring(length-4, length));
        String monthInText = this.monthYearTV.getText().toString().substring(0, length-5);
        int currMonth = this.getNumberFromMonthName(monthInText) - 1;
        int currDay = Integer.parseInt(this.dayOfMonth.getText().toString());

        Calendar tempCal = Calendar.getInstance();
        tempCal.set(currYear, currMonth, currDay);

        return new ShiftDay(tempCal);
    }

    private int getNumberFromMonthName(String monthName){
        DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("MMMM").withLocale(Locale.ENGLISH);
        TemporalAccessor temporalAccessor = dtFormatter.parse(monthName);
        return temporalAccessor.get(ChronoField.MONTH_OF_YEAR);
    }

    private void setTodayTextColor(){this.dayOfMonth.setTextColor(Color.RED);}

    public void setTextColor(){
        if(!(((ColorDrawable) this.layout.getBackground()).getColor() == this.rootFragment.getView().getResources().getColor(R.color.white)))
            this.dayOfMonth.setTextColor(this.rootFragment.getView().getResources().getColor(R.color.black));
    }

    public void setTextColor(int color){
        if(!(((ColorDrawable) this.layout.getBackground()).getColor() == this.rootFragment.getView().getResources().getColor(R.color.white)))
            this.dayOfMonth.setTextColor(color);
    }

    public void setEmptyTextColor(){
        this.dayOfMonth.setTextColor(this.rootFragment.getView().getResources().getColor(R.color.light_grey2));
    }

    public void setBackgroundColor(int color){
        this.layout.setBackgroundColor(color);
    }

    private boolean isToday(String text){
        String nowDay = String.valueOf(now.getDayOfMonth());
        DateTimeFormatter formatterMonth = DateTimeFormatter.ofPattern("MMMM");
        DateTimeFormatter formatterYear = DateTimeFormatter.ofPattern("yyy");
        String nowMonth = now.format(formatterMonth);
        String nowYear = now.format(formatterYear);

        String thisDay = text;
        String[] tempTable = monthYearTV.getText().toString().split(" ");
        String thisMonth = tempTable[0];
        String thisYear = tempTable[1];

        return (nowDay.equals(thisDay)) && (nowMonth.equals(thisMonth) && (nowYear.equals(thisYear)));
    }

}