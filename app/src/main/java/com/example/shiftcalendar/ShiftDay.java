package com.example.shiftcalendar;

import com.example.shiftcalendar.ui.calendar.CalendarViewHolder;

import java.util.Calendar;

public class ShiftDay implements Comparable<ShiftDay>{

    private Calendar calendar;
    private int color;
    private CalendarViewHolder calendarViewHolder;
    private Shift shift;
    private String notes;
    private int earlyExitHours;
    private int earlyExitMin;
    private int extraTimeHours;
    private int extraTimeMin;
    private double incomePerHour;
    private double incomePerExtraHour;
    private double extraIncome;

    public ShiftDay(Calendar c, CalendarViewHolder cvh){
        this.calendar = c;
        this.color = R.color.light_grey;
        this.calendarViewHolder = cvh;
        this.shift = null;
        this.notes = null;
    }

    public ShiftDay(Calendar calendar, Shift shift, String notes){
        this.calendar = calendar;
        this.shift = shift;
        this.color = shift.getBackgroundColor();
        this.notes = notes;
        this.earlyExitHours = 0;
        this.earlyExitMin = 0;
        this.extraTimeHours = 0;
        this.extraTimeMin = 0;
        this.incomePerHour = shift.getIncomePerHour();
        this.incomePerExtraHour = shift.getIncomePerExtraHour();
        this.extraIncome = 0;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public void setDay(String day){
        int currYear = this.calendar.get(Calendar.YEAR);
        int currMonth = this.calendar.get(Calendar.MONTH);
        int currDay = Integer.parseInt(day);

        Calendar tempCal = Calendar.getInstance();
        tempCal.set(currYear, currMonth, currDay);

        setCalendar(tempCal);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public CalendarViewHolder getCalendarViewHolder() {
        return calendarViewHolder;
    }

    public void setCalendarViewHolder(CalendarViewHolder calendarViewHolder) { this.calendarViewHolder = calendarViewHolder; }

    public Shift getShift() { return shift; }

    public void setShift(Shift shift) { this.shift = shift; }

    public String getNotes() { return notes; }

    public void setNotes(String notes) { this.notes = notes; }

    public int getEarlyExitHours() {
        return earlyExitHours;
    }

    public void setEarlyExitHours(int earlyExitHours) {
        this.earlyExitHours = earlyExitHours;
    }

    public int getEarlyExitMin() {
        return earlyExitMin;
    }

    public void setEarlyExitMin(int earlyExitMin) {
        this.earlyExitMin = earlyExitMin;
    }

    public int getExtraTimeHours() {
        return extraTimeHours;
    }

    public void setExtraTimeHours(int extraTimeHours) {
        this.extraTimeHours = extraTimeHours;
    }

    public int getExtraTimeMin() {
        return extraTimeMin;
    }

    public void setExtraTimeMin(int extraTimeMin) {
        this.extraTimeMin = extraTimeMin;
    }

    public double getIncomePerHour() {
        return incomePerHour;
    }

    public void setIncomePerHour(double incomePerHour) {
        this.incomePerHour = incomePerHour;
    }

    public double getIncomePerExtraHour() {
        return incomePerExtraHour;
    }

    public void setIncomePerExtraHour(double incomePerExtraHour) {
        this.incomePerExtraHour = incomePerExtraHour;
    }

    public double getExtraIncome() {
        return extraIncome;
    }

    public void setExtraIncome(double extraIncome) {
        this.extraIncome = extraIncome;
    }

    public String toString(){
        String text;

        text = "Date: " + this.calendar.get(Calendar.DAY_OF_MONTH) + "/";
        text += this.calendar.get(Calendar.MONTH) + "/";
        text += String.valueOf(this.calendar.get(Calendar.YEAR));
        if(this.shift != null)
            text += "\n" + this.shift.getName() + "|" + String.format("#%06X", (0xFFFFFF & this.shift.getBackgroundColor()));

        return text;
    }

    @Override
    public int compareTo(ShiftDay o) {
        return this.calendar.compareTo(o.getCalendar());
    }
}
