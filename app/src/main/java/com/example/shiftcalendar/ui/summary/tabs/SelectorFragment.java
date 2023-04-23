package com.example.shiftcalendar.ui.summary.tabs;

import android.util.Log;

import androidx.fragment.app.Fragment;

import com.example.shiftcalendar.Shift;
import com.example.shiftcalendar.ShiftDay;
import com.example.shiftcalendar.ShiftDayList;
import com.example.shiftcalendar.ui.DayDetailsBottomSheet;
import com.example.shiftcalendar.ui.summary.ShiftDayRecyclerData;
import com.example.shiftcalendar.ui.summary.ShiftRecyclerData;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;

public class SelectorFragment extends Fragment {


    public SelectorFragment(){}

    public void displayOverviewWithoutShift(String shiftName){}

    public void displayOverviewWithShift(String shiftName){}

    public void setTotalValues(ArrayList<ShiftDay> overviewList) throws ParseException {}

    public ArrayList<ShiftDayRecyclerData> getShiftDayData(ArrayList<ShiftDay> shiftDaysList){

        ArrayList<ShiftDayRecyclerData> shiftDays = new ArrayList<>();
        ShiftDayRecyclerData tempShiftDay;

        for(ShiftDay shiftDay : shiftDaysList){
            tempShiftDay = new ShiftDayRecyclerData(shiftDay);
            shiftDays.add(tempShiftDay);
        }


        return shiftDays;
    }

    public ArrayList<ShiftRecyclerData> getShiftsData(ArrayList<ShiftDay> shiftDaysList) throws ParseException {

        ArrayList<ShiftRecyclerData> shifts = new ArrayList<>();
        ShiftRecyclerData tempShift;

        for(ShiftDay shiftDay : shiftDaysList){

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
            else{
                shiftMin -= earlyMin;
            }

            ShiftRecyclerData shiftRecyclerData = this.containsData(shifts, shiftDay.getShift());

            if(shiftRecyclerData == null){
                tempShift = new ShiftRecyclerData(shiftDay.getShift(), 1, shiftHours, shiftMin, extraHours, extraMin);
                shifts.add(tempShift);
            }
            else{
                shiftRecyclerData.increaseCount();
                shiftRecyclerData.increaseHours(shiftHours);
                shiftRecyclerData.increaseMin(shiftMin);
                shiftRecyclerData.increaseExtraHours(extraHours);
                shiftRecyclerData.increaseExtraMin(extraMin);
            }
        }

        Log.d("Debug", String.valueOf(shifts.size()));

        return shifts;
    }

    private ShiftRecyclerData containsData(ArrayList<ShiftRecyclerData> shifts, Shift shift){

        for(ShiftRecyclerData shiftRecyclerData: shifts){
            if(shiftRecyclerData.getShift() == shift) return shiftRecyclerData;
        }

        return null;
    }
}
