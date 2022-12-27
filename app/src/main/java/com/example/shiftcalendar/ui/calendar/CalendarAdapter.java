package com.example.shiftcalendar.ui.calendar;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiftcalendar.R;
import com.example.shiftcalendar.Shift;
import com.example.shiftcalendar.ShiftDayList;

import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {

    private final ArrayList<String> daysOfMonth;
    private final OnItemListener onItemListener;

    private TextView monthYearTV;

    private Fragment rootFragment;

    private ShiftDayList shiftDayList;

    private ArrayList<Shift> shiftList;

    public CalendarAdapter(ArrayList<String> daysOfMonth, OnItemListener onItemListener, TextView monthYearTV, Fragment rootFragment, ArrayList<Shift> sL, ShiftDayList sDL) {
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
        this.monthYearTV = monthYearTV;
        this.rootFragment = rootFragment;
        this.shiftDayList = new ShiftDayList();
        this.shiftList = sL;
        this.shiftDayList = sDL;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getWidth() / 7);

        return new CalendarViewHolder(view, onItemListener, this.monthYearTV, this.rootFragment, this.shiftList, this.shiftDayList);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        holder.setText(String.valueOf(daysOfMonth.get(position)));
    }

    @Override
    public int getItemCount() { return daysOfMonth.size(); }

    public interface OnItemListener{
        void onItemClickListener(int position, String dayText);
    }
}
