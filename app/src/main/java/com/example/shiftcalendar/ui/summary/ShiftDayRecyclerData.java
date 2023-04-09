package com.example.shiftcalendar.ui.summary;

import android.util.Log;

import com.example.shiftcalendar.ShiftDay;
import com.example.shiftcalendar.ui.DayDetailsBottomSheet;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ShiftDayRecyclerData {

    private String day;
    private Calendar calendar;
    private int color;
    private String time;
    private String hours;
    private double income;

    public ShiftDayRecyclerData(ShiftDay shiftDay){
        this.calendar = shiftDay.getCalendar();
        this.day = this.dayFormatting();
        this.color = shiftDay.getShift().getBackgroundColor();
        this.time = this.calculateTime(shiftDay);
        try {
            this.hours = this.calculateHours(shiftDay);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        this.income = shiftDay.getExtraIncome();
    }

    private String dayFormatting(){

        String weekDay;
        SimpleDateFormat dayFormat = new SimpleDateFormat("E", Locale.getDefault());

        weekDay = dayFormat.format(calendar.getTime());

        return weekDay;
    }

    private String calculateTime(ShiftDay shiftDay) {

        String startTime = shiftDay.getShift().getStartTime().toString();
        String endTime = shiftDay.getShift().getEndTime().toString();
        int minusPlusHours = shiftDay.getExtraTimeHours() - shiftDay.getEarlyExitHours();
        int minusPlusMinutes = shiftDay.getExtraTimeMin() - shiftDay.getEarlyExitMin();
        int shiftDayHours = Integer.parseInt(endTime.split(":")[0]) + minusPlusHours;
        int shiftDayMinutes = Integer.parseInt(endTime.split(":")[1]) + minusPlusMinutes;
        if(shiftDayMinutes > 59){
            shiftDayMinutes -= 60;
            shiftDayHours++;
        } else if (shiftDayMinutes < 0) {
            shiftDayMinutes = 60 + shiftDayMinutes;
            shiftDayHours--;
        }
        String hoursToText;
        if(shiftDayHours > 23) shiftDayHours = shiftDayHours % 24;
        if(shiftDayHours < 10)
            hoursToText = "0" + shiftDayHours;
        else
            hoursToText = String.valueOf(shiftDayHours);
        String minutesToText;
        if(shiftDayMinutes < 10)
            minutesToText = "0" + shiftDayMinutes;
        else
            minutesToText = String.valueOf(shiftDayMinutes);

        return startTime.split(":")[0] + ":" + startTime.split(":")[1] + " - " + hoursToText + ":" + minutesToText;
    }

    private String calculateHours(ShiftDay shiftDay) throws ParseException {

        String timeDifference = DayDetailsBottomSheet.timeDifferenceToString(shiftDay.getShift().getStartTime().toString(), shiftDay.getShift().getEndTime().toString());
        String shiftHoursText = timeDifference.split("/")[0];
        int shiftHours = Integer.parseInt(shiftHoursText.substring(0, shiftHoursText.length()-2));
        String shiftMinText = timeDifference.split("/")[1];
        int shiftMin = Integer.parseInt(shiftMinText.substring(0, shiftMinText.length()-2));
        int extraHours = shiftDay.getExtraTimeHours();
        int extraMin = shiftDay.getExtraTimeMin();
        int earlyHours = shiftDay.getEarlyExitHours();
        int earlyMin = shiftDay.getEarlyExitMin();
        shiftHours -= earlyHours;
        if(earlyMin > shiftMin){
            shiftMin = 60 - earlyMin + shiftMin;
            shiftHours--;
        }
        else {
            shiftMin -= earlyMin;
        }
        shiftHours += extraHours;
        shiftMin += extraMin;
        if(shiftMin > 59){
            shiftMin -= 60;
            shiftHours ++;
        }
        shiftHoursText = String.valueOf(shiftHours);
        if(shiftHoursText.length() == 1)
            shiftHoursText = "0" + shiftHoursText;
        shiftMinText = String.valueOf(shiftMin);
        if(shiftMinText.length() == 1)
            shiftMinText = "0" + shiftMinText;

        String returnableHours = shiftHoursText + " h " + shiftMinText + " m";

        return returnableHours;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getCalendarToString() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        return simpleDateFormat.format(this.calendar.getTime());
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
