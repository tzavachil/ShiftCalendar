package com.example.shiftcalendar;

import android.util.Log;

import com.example.shiftcalendar.ui.summary.ShiftRecyclerData;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class ShiftDayList {

    private ArrayList<ShiftDay> shiftDaysList;

    public ShiftDayList(){
        this.shiftDaysList = new ArrayList<>();
    }

    public void addDay(ShiftDay day){
        ShiftDay tempShiftDay = this.contains(day);
        if(tempShiftDay != null)
            this.shiftDaysList.remove(tempShiftDay);
        this.shiftDaysList.add(day);
    }

    public ShiftDay contains(ShiftDay day) {
        ShiftDay tempShiftDay = null;
        for(ShiftDay sd: this.shiftDaysList){
            if(tempShiftDay == null && this.areEqual(sd, day))
                tempShiftDay = sd;
        }
        return tempShiftDay;
    }

    public ShiftDay contains(int day, int month, int year){
        ShiftDay tempShiftDay = null;
        month--;
        for(ShiftDay sd: this.shiftDaysList){
            if(tempShiftDay == null && this.areEqualWithInt(sd, day, month, year))
                tempShiftDay = sd;
        }
        return tempShiftDay;
    }

    private boolean areEqual(ShiftDay sd1, ShiftDay sd2){
        if(sd1.getCalendar().get(Calendar.YEAR) != sd2.getCalendar().get(Calendar.YEAR)) return false;
        if(sd1.getCalendar().get(Calendar.MONTH) != sd2.getCalendar().get(Calendar.MONTH)) return false;
        if(sd1.getCalendar().get(Calendar.DAY_OF_MONTH) == sd2.getCalendar().get(Calendar.DAY_OF_MONTH)) return true;
        return false;
    }

    private boolean areEqualWithInt(ShiftDay sd, int day, int month, int year){
        if(sd.getCalendar().get(Calendar.YEAR) != year) return false;
        if(sd.getCalendar().get(Calendar.MONTH) != month) return false;
        if(sd.getCalendar().get(Calendar.DAY_OF_MONTH) == day) return true;
        return false;
    }

    public int size() { return this.shiftDaysList.size(); }


    public ArrayList<ShiftDay> searchByMonth(int month, int year) {
        ArrayList<ShiftDay> currShiftDayList = new ArrayList<>();

        for(ShiftDay shiftDay: this.shiftDaysList){
            if(shiftDay.getCalendar().get(Calendar.MONTH) == month-1 && shiftDay.getCalendar().get(Calendar.YEAR) == year)
                currShiftDayList.add(shiftDay);
        }

        return currShiftDayList;
    }

    public void sort(){
        Collections.sort(this.shiftDaysList);
    }
}
