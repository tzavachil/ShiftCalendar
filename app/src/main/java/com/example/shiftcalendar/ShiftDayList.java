package com.example.shiftcalendar;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import android.icu.util.Calendar;
import java.util.Collections;

public class ShiftDayList implements Serializable {

    private ArrayList<ShiftDay> shiftDaysList;

    public ShiftDayList(){
        this.shiftDaysList = new ArrayList<>();
    }

    public void addDay(ShiftDay day){
        Log.d("Debug", "Before add: " + day.toString());
        ShiftDay tempShiftDay = this.contains(day);
        if(tempShiftDay != null)
            this.shiftDaysList.remove(tempShiftDay);
        this.shiftDaysList.add(day);
    }

    public void removeDay(ShiftDay day){
        this.shiftDaysList.remove(this.contains(day));
    }

    public ShiftDay contains(ShiftDay day) {
        ShiftDay tempShiftDay = null;
        for(ShiftDay sd: this.shiftDaysList){
            if(tempShiftDay == null && this.areEqual(sd, day))
                tempShiftDay = sd;
        }
        return tempShiftDay;
    }

    public boolean contains(Shift s){
        for(ShiftDay shiftDay: this.shiftDaysList){
            if(shiftDay.getShift().equals(s)) return true;
        }
        return false;
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

    public ArrayList<ShiftDay> searchByYear(int year) {
        ArrayList<ShiftDay> currShiftDayList = new ArrayList<>();

        for(ShiftDay shiftDay: this.shiftDaysList){
            if(shiftDay.getCalendar().get(Calendar.YEAR) == year)
                currShiftDayList.add(shiftDay);
        }

        return currShiftDayList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<ShiftDay> searchByRange(LocalDate dateFrom, LocalDate dateTo){
        ArrayList<ShiftDay> currShiftDayList = new ArrayList<>();

        for(ShiftDay shiftDay: this.shiftDaysList){
            if(calendarToLocalDate(shiftDay.getCalendar()).compareTo(dateFrom) >= 0 && calendarToLocalDate(shiftDay.getCalendar()).compareTo(dateTo) <= 0){
                currShiftDayList.add(shiftDay);
            }
        }

        return currShiftDayList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private LocalDate calendarToLocalDate(Calendar calendar){
        return LocalDateTime.ofInstant(calendar.getTime().toInstant(), ZoneId.systemDefault()).toLocalDate();
    }

    private String calendarToString(Calendar calendar){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        if (calendar != null) {
            return sdf.format(calendar.getTime());
        }

        return null;
    }

    public void sort(){
        Collections.sort(this.shiftDaysList);
    }

    public void print(){
        for(ShiftDay shiftDay: this.shiftDaysList){
            Log.d("Debug", calendarToString(shiftDay.getCalendar()) + " with Shift Name: " + shiftDay.getShift().getName());
        }
    }

    public ShiftDay get(int i){
        return this.shiftDaysList.get(i);
    }
}
