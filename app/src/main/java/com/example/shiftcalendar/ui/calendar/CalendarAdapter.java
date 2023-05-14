package com.example.shiftcalendar.ui.calendar;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiftcalendar.R;
import com.example.shiftcalendar.Shift;
import com.example.shiftcalendar.ShiftDayList;

import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {

    private final ArrayList<String> daysOfMonth;
    private Context context;

    private TextView monthYearTV;
    private Fragment rootFragment;
    private ShiftDayList shiftDayList;
    private ArrayList<Shift> shiftList;

    public CalendarAdapter(ArrayList<String> daysOfMonth, Context context, TextView monthYearTV, Fragment rootFragment, ArrayList<Shift> sL, ShiftDayList sDL){
        this.daysOfMonth = daysOfMonth;
        this.context = context;

        this.monthYearTV = monthYearTV;
        this.rootFragment = rootFragment;
        this.shiftDayList = sDL;
        this.shiftList = sL;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getWidth() / 7);

        return new CalendarViewHolder(view, this.monthYearTV, this.rootFragment, this.shiftList ,this.shiftDayList);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        String day = this.daysOfMonth.get(position);
        holder.setText(day);
        holder.displayShiftHolder();
    }

    @Override
    public int getItemCount() {
        return this.daysOfMonth.size();
    }
}
