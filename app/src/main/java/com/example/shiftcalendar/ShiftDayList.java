package com.example.shiftcalendar;

import java.util.ArrayList;

public class ShiftDayList {

    private ArrayList<ShiftDay> shiftDaysList;

    public ShiftDayList(){
        this.shiftDaysList = new ArrayList<>();
    }

    public void addDay(ShiftDay day){
        this.shiftDaysList.add(day);
    }


}
