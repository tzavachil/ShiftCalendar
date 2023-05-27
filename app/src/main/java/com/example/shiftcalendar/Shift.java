package com.example.shiftcalendar;

import android.content.Context;

import java.sql.Time;
import java.util.Collection;

public class Shift implements Comparable<Shift> {

    private String name;
    private int lineColor;
    private int backgroundColor;
    private Time startTime;
    private Time endTime;
    private double incomePerHour;
    private double incomePerExtraHour;

    public Shift(String n, int c, Time sT, Time eT, double iPH, double iPEH){
        this.name = n;
        this.lineColor = c;
        this.backgroundColor = c;
        this.startTime = sT;
        this.endTime = eT;
        this.incomePerHour = iPH;
        this.incomePerExtraHour = iPEH;
    }

    public Shift(Context context){
        this.name = "";
        this.lineColor = context.getColor(R.color.light_grey);
        this.backgroundColor = context.getColor(R.color.colorPrimary );
        this.startTime = new Time(6,0,0);
        this.endTime = new Time(14, 0, 0);
        this.incomePerHour = 0;
        this.incomePerExtraHour = 0;
    }

    public Shift(Shift s){
        this.name = s.getName();
        this.lineColor = s.getLineColor();
        this.backgroundColor = s.getBackgroundColor();
        this.startTime = s.getStartTime();
        this.endTime = s.getEndTime();
        this.incomePerHour = s.incomePerHour;
        this.incomePerExtraHour = s.getIncomePerExtraHour();
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Shift s = (Shift) o;
        return this.name.equals(s.getName());
    }

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public int getLineColor() { return lineColor; }

    public void setLineColor(int lineColor) { this.lineColor = lineColor; }

    public int getBackgroundColor() { return backgroundColor; }

    public void setBackgroundColor(int backgroundColor) { this.backgroundColor = backgroundColor; }

    public Time getStartTime() {return startTime;}

    public void setStartTime(Time startTime) {this.startTime = startTime;}

    public Time getEndTime() {return endTime;}

    public void setEndTime(Time endTime) {this.endTime = endTime;}

    public double getIncomePerHour() {return incomePerHour;}

    public void setIncomePerHour(double incomePerHour) {this.incomePerHour = incomePerHour;}

    public double getIncomePerExtraHour() {return incomePerExtraHour;}

    public void setIncomePerExtraHour(double incomePerExtraHour) {this.incomePerExtraHour = incomePerExtraHour;}

    @Override
    public int compareTo(Shift o) {


        return 0;
    }
}
