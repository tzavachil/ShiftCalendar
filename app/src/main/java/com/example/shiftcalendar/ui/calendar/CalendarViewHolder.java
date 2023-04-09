package com.example.shiftcalendar.ui.calendar;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiftcalendar.R;
import com.example.shiftcalendar.Shift;
import com.example.shiftcalendar.ShiftDay;
import com.example.shiftcalendar.ShiftDayList;
import com.example.shiftcalendar.ui.DayDetailsBottomSheet;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import eltos.simpledialogfragment.form.DateTime;

public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private final TextView dayOfMonth;
    private final ConstraintLayout layout;
    private final CalendarAdapter.OnItemListener onItemListener;

    private LocalDate now;
    private TextView monthYearTV;

    private Fragment rootFragment;
    private FrameLayout frameLayout;

    private ArrayList<Shift> shiftList;
    private ShiftDayList shiftDayList;
    private ShiftDay myShiftDay;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public CalendarViewHolder(@NonNull View itemView, CalendarAdapter.OnItemListener onItemListener, TextView monthYearTV, Fragment rootFragment, ArrayList<Shift> sL, ShiftDayList sDL) {
        super(itemView);
        this.rootFragment = rootFragment;
        frameLayout = (FrameLayout) rootFragment.getView().findViewById(R.id.fragmentContainer);
        dayOfMonth = (TextView) itemView.findViewById(R.id.cellDayText);
        layout = (ConstraintLayout) itemView.findViewById(R.id.cellLayout);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);

        now = LocalDate.now();
        this.monthYearTV = monthYearTV;
        this.shiftList = sL;
        this.shiftDayList = sDL;

        this.myShiftDay = this.createCurrentShiftDay();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void displayShiftHolder(){
        String thisDay = this.dayOfMonth.getText().toString();
        if(!thisDay.equals("")) {
            String[] tempTable = monthYearTV.getText().toString().split(" ");
            String thisMonth = tempTable[0];
            String thisYear = tempTable[1];

            if (thisDay.length() == 1)
                thisDay = "0" + thisDay;
            DateTimeFormatter parser = DateTimeFormatter.ofPattern("MMMM").withLocale(Locale.ENGLISH);
            TemporalAccessor accessor = parser.parse(thisMonth);
            int dayInteger = Integer.parseInt(thisDay);
            int monthInteger = accessor.get(ChronoField.MONTH_OF_YEAR);
            int yearInteger = Integer.parseInt(thisYear);
            ShiftDay currShiftDay = this.shiftDayList.contains(dayInteger, monthInteger, yearInteger);
            if(currShiftDay != null){
                //Paint the holder with day's shift
                this.setBackgroundColor(currShiftDay.getColor());
                if(!this.isToday(thisDay))
                    this.setTextColor();
                else {
                    this.setTextColor(Color.BLACK);
                    this.dayOfMonth.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                }
                this.myShiftDay = currShiftDay;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        if(!this.dayOfMonth.getText().toString().equals("")) {
            this.showBottomSheet();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showBottomSheet(){
        this.myShiftDay.setDay(this.dayOfMonth.getText().toString());
        DayDetailsBottomSheet bottomSheetDayDetails = new DayDetailsBottomSheet(this.getDateToString(), this.myShiftDay, this.shiftList, this);
        bottomSheetDayDetails.show(rootFragment.getActivity().getSupportFragmentManager(), "TAG");
    }

    public void saveDayChanges(ShiftDay currShiftDay){
        Log.d("Debug", this.myShiftDay.toString());
        this.shiftDayList.addDay(currShiftDay);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ShiftDay createCurrentShiftDay(){

        int length = this.monthYearTV.getText().toString().length();
        int currYear = Integer.parseInt(this.monthYearTV.getText().toString().substring(length-4, length));
        String monthInText = this.monthYearTV.getText().toString().substring(0, length-5);
        int currMonth = this.getNumberFromMonthName(monthInText);
        int currDay = Integer.parseInt(this.dayOfMonth.getText().toString());

        Calendar tempCal = Calendar.getInstance();
        tempCal.set(currYear, currMonth, currDay);

        return new ShiftDay(tempCal, this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private int getNumberFromMonthName(String monthName){
        DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("MMMM").withLocale(Locale.ENGLISH);
        TemporalAccessor temporalAccessor = dtFormatter.parse(monthName);
        return temporalAccessor.get(ChronoField.MONTH_OF_YEAR);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getDateToString(){
        String thisDay = this.dayOfMonth.getText().toString();
        String[] tempTable = monthYearTV.getText().toString().split(" ");
        String thisMonth = tempTable[0];
        String thisYear = tempTable[1];

        if(thisDay.length()==1)
            thisDay = "0" + thisDay;
        String tempDate = thisDay + "-" + thisMonth + "-" + thisYear;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy");
        LocalDate date = LocalDate.parse(tempDate, formatter);
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        return date.format(displayFormatter);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setText(String text){

        if(isToday(text)) {
            this.setTodayTextColor();
        }
        this.dayOfMonth.setText(text);
    }

    private void setTodayTextColor(){this.dayOfMonth.setTextColor(Color.RED);}

    public void setTextColor(){
        if(!(((ColorDrawable) this.layout.getBackground()).getColor() == this.rootFragment.getView().getResources().getColor(R.color.white)))
            this.dayOfMonth.setTextColor(this.rootFragment.getView().getResources().getColor(R.color.black));
    }

    public void setTextColor(int color){
        if(!(((ColorDrawable) this.layout.getBackground()).getColor() == this.rootFragment.getView().getResources().getColor(R.color.white)))
            this.dayOfMonth.setTextColor(color);
    }

    public void setEmptyTextColor(){
            this.dayOfMonth.setTextColor(this.rootFragment.getView().getResources().getColor(R.color.light_grey2));
    }

    public void setBackgroundColor(int color){
        this.layout.setBackgroundColor(color);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isToday(String text){
        String nowDay = String.valueOf(now.getDayOfMonth());
        DateTimeFormatter formatterMonth = DateTimeFormatter.ofPattern("MMMM");
        DateTimeFormatter formatterYear = DateTimeFormatter.ofPattern("yyy");
        String nowMonth = now.format(formatterMonth);
        String nowYear = now.format(formatterYear);

        String thisDay = text;
        String[] tempTable = monthYearTV.getText().toString().split(" ");
        String thisMonth = tempTable[0];
        String thisYear = tempTable[1];

        return (nowDay.equals(thisDay)) && (nowMonth.equals(thisMonth) && (nowYear.equals(thisYear)));
    }
}
