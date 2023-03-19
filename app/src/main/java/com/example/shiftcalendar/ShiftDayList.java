package com.example.shiftcalendar;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

public class ShiftDayList {

    private ArrayList<ShiftDay> shiftDaysList;

    public ShiftDayList(){
        this.shiftDaysList = new ArrayList<>();
    }

    public void addDay(ShiftDay day){
        if(!this.contains(day))
            this.shiftDaysList.add(day);
    }

    public boolean contains(ShiftDay day) {
        boolean flag = false;
        for(ShiftDay sd: this.shiftDaysList){
            if(!flag)
                flag = this.areEqual(sd, day);
        }
        return flag;
    }

    //public boolean contains(int day, int month, int year){
    public ShiftDay contains(int day, int month, int year){
        //boolean flag = false;
        ShiftDay tempShiftDay = null;
        year += 1900;
        for(ShiftDay sd: this.shiftDaysList){
            //if(!flag)
            if(tempShiftDay == null && this.areEqualWithInt(sd, day, month, year))
                //flag = this.areEqualWithInt(sd, day, month, year);
                tempShiftDay = sd;
        }
        //return flag;
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


}
