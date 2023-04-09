package com.example.shiftcalendar.ui.summary;

import com.example.shiftcalendar.ShiftDay;

import java.util.Calendar;

public class ShiftDayRecyclerData {

    private String day;
    private Calendar calendar;
    private int color;
    private String time;
    private String hours;
    private double income;

    public ShiftDayRecyclerData(ShiftDay shiftDay){
        this.day = this.dayFormatting(shiftDay);
        this.calendar = shiftDay.getCalendar();
        this.color = shiftDay.getShift().getBackgroundColor();
        this.time = this.calculateTime(shiftDay);
        this.hours = this.calculateHours(shiftDay);
        this.income = shiftDay.getExtraIncome();
    }

    private String dayFormatting(ShiftDay shiftDay){
        return "Mo";
    }

    private String calculateTime(ShiftDay shiftDay){
        return "7:00 - 15:00";
    }

    private String calculateHours(ShiftDay shiftDay){
        return "8 h 00 m";
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getCalendarToString() {
        return "calendar";
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }
}
