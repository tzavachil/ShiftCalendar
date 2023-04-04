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

public class ShiftRecyclerViewAdapter extends RecyclerView.Adapter<ShiftRecyclerViewAdapter.ShiftRecyclerViewHolder> {

    private ArrayList<ShiftRecyclerData> shiftDataArrayList;
    private Context context;

    public ShiftRecyclerViewAdapter(ArrayList<ShiftRecyclerData> recyclerDataArrayList, Context context) {
        this.shiftDataArrayList = recyclerDataArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ShiftRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shift_recycler_view, parent, false);
        return new ShiftRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShiftRecyclerViewHolder holder, int position) {
        // Set the data to textview and imageview.
        ShiftRecyclerData recyclerData = shiftDataArrayList.get(position);
        holder.shiftNameTextView.setText(recyclerData.getShift().getName());
        GradientDrawable colorViewBackground = (GradientDrawable) holder.shiftColorView.getBackground();
        colorViewBackground.setColor(recyclerData.getShift().getBackgroundColor());
        holder.countTextView.setText(String.valueOf(recyclerData.getCount()));
        holder.timeTextView.setText(recyclerData.getHours() + "h " + recyclerData.getMin() + "m");
        holder.extraTimeTextView.setText(recyclerData.getExtraHours() + "h " + recyclerData.getExtraMin() + "m");
    }

    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return shiftDataArrayList.size();
    }

    // View Holder Class to handle Recycler View.
    public class ShiftRecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView shiftNameTextView;
        private View shiftColorView;
        private TextView countTextView;
        private TextView timeTextView;
        private TextView extraTimeTextView;

        public ShiftRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            this.shiftNameTextView = itemView.findViewById(R.id.shiftNameTextView);
            this.shiftColorView = itemView.findViewById(R.id.shiftColorView);
            this.countTextView = itemView.findViewById(R.id.countTextView);
            this.timeTextView = itemView.findViewById(R.id.timeTextView);
            this.extraTimeTextView = itemView.findViewById(R.id.extraTimeTextView);
        }
    }
}