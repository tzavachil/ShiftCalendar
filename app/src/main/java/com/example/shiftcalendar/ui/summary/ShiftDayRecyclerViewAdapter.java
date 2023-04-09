package com.example.shiftcalendar.ui.summary;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiftcalendar.R;

import java.util.ArrayList;

public class ShiftDayRecyclerViewAdapter extends RecyclerView.Adapter<ShiftDayRecyclerViewAdapter.ShiftDayRecyclerViewHolder> {

    private ArrayList<ShiftDayRecyclerData> shiftDayDataArrayList;
    private Context context;

    public ShiftDayRecyclerViewAdapter(ArrayList<ShiftDayRecyclerData> recyclerDataArrayList, Context context) {
        this.shiftDayDataArrayList = recyclerDataArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ShiftDayRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shift_day_on_recycler_view, parent, false);
        return new ShiftDayRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShiftDayRecyclerViewHolder holder, int position) {
        // Set the data to textview and imageview.
        ShiftDayRecyclerData recyclerData = shiftDayDataArrayList.get(position);
        holder.dayTextView.setText(recyclerData.getDay());
        holder.dateTextView.setText(recyclerData.getCalendarToString());
        GradientDrawable circleViewBackground = (GradientDrawable) holder.shiftCircle.getBackground();
        circleViewBackground.setColor(recyclerData.getColor());
        holder.shiftTimeTextView.setText(recyclerData.getTime());
        holder.shiftHoursTextView.setText(recyclerData.getHours());
        holder.shiftIncomeTextView.setText(String.valueOf(recyclerData.getIncome()));
    }

    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return shiftDayDataArrayList.size();
    }

    // View Holder Class to handle Recycler View.
    public class ShiftDayRecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView dayTextView;
        private TextView dateTextView;
        private View shiftCircle;
        private TextView shiftTimeTextView;
        private TextView shiftHoursTextView;
        private TextView shiftIncomeTextView;

        public ShiftDayRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            this.dayTextView = itemView.findViewById(R.id.dayTextView);
            this.dateTextView = itemView.findViewById(R.id.dateTextView);
            this.shiftCircle = itemView.findViewById(R.id.shiftCircle);
            this.shiftTimeTextView = itemView.findViewById(R.id.shiftTimeTextView);
            this.shiftHoursTextView = itemView.findViewById(R.id.shiftHoursTextView);
            this.shiftIncomeTextView = itemView.findViewById(R.id.shiftIncomeTextView);
        }
    }
}
