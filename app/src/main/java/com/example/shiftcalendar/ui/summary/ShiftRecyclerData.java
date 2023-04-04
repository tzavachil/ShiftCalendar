package com.example.shiftcalendar.ui.summary;

import com.example.shiftcalendar.Shift;

public class ShiftRecyclerData {

    private Shift shift;
    private int count;
    private int hours;
    private int min;
    private int extraHours;
    private int extraMin;

    public ShiftRecyclerData(Shift shift, int count, int hours, int min, int extraHours, int extraMin) {
        this.shift = shift;
        this.count = count;
        this.hours = hours;
        this.min = min;
        this.extraHours = extraHours;
        this.extraMin = extraMin;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public int getCount() {
        return count;
    }

    public void increaseCount() {
        this.count ++;
    }

    public int getHours() {
        return hours;
    }

    public void increaseHours(int hours) {
        this.hours += hours;
    }

    public int getMin() {
        return min;
    }

    public void increaseMin(int min) {
        this.min += min;
    }

    public int getExtraHours() {
        return extraHours;
    }

    public void increaseExtraHours(int extraHours) {
        this.extraHours += extraHours;
    }

    public int getExtraMin() {
        return extraMin;
    }

    public void increaseExtraMin(int extraMin) {
        this.extraMin += extraMin;
    }
}
