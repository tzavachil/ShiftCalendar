package com.example.shiftcalendar;

import com.example.shiftcalendar.ui.calendar.CalendarViewHolder;

import java.util.Calendar;

public class ShiftDay {

    private Calendar calendar;
    private int color;
    private CalendarViewHolder calendarViewHolder;
    private Shift shift;
    private String notes;

    public ShiftDay(Calendar c, CalendarViewHolder cvh){
        this.calendar = c;
        this.color = R.color.light_grey;
        this.calendarViewHolder = cvh;
        this.shift = null;
        this.notes = null;
    }

    public ShiftDay(Calendar calendar, Shift shift, String notes){
        this.calendar = calendar;
        this.color = color;
        this.shift = shift;
        this.notes = notes;
    }

    public Calendar getCalendar() {
        return calendar;
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

    public CalendarViewHolder getCalendarViewHolder() {
        return calendarViewHolder;
    }

    public void setCalendarViewHolder(CalendarViewHolder calendarViewHolder) { this.calendarViewHolder = calendarViewHolder; }

    public Shift getShift() { return shift; }

    public void setShift(Shift shift) { this.shift = shift; }

    public String getNotes() { return notes; }

    public void setNotes(String notes) { this.notes = notes; }
}
