package com.example.shiftcalendar.ui.shifts;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.shiftcalendar.R;
import com.example.shiftcalendar.Shift;

import java.util.ArrayList;

public class ShiftAdapter extends ArrayAdapter<Shift> {

    private ConstraintLayout shiftItemLayout;

    public ShiftAdapter(Context context, ArrayList<Shift> shifts){
        super(context, 0, shifts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        Shift shift = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.shift_list_layout, parent, false);
        }
        this.shiftItemLayout = convertView.findViewById(R.id.shiftItemLayout);
        GradientDrawable layoutBackground = (GradientDrawable) this.shiftItemLayout.getBackground();
        layoutBackground.setStroke(3, shift.getLineColor());
        TextView shiftNameTextView = (TextView) convertView.findViewById(R.id.shiftItemName);
        shiftNameTextView.setText(shift.getName());

        return convertView;
    }

}
