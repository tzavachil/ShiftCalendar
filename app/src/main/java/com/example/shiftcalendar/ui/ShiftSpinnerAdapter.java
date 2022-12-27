package com.example.shiftcalendar.ui;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.shiftcalendar.R;
import com.example.shiftcalendar.Shift;

import java.util.ArrayList;

public class ShiftSpinnerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Shift> shiftList;

    public ShiftSpinnerAdapter(Context context, ArrayList<Shift> shiftList) {
        this.context = context;
        this.shiftList = shiftList;
    }

    @Override
    public int getCount() {
        return shiftList != null ? shiftList.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View rootView = LayoutInflater.from(context).inflate(R.layout.item_shift, viewGroup, false);

        TextView shiftName = rootView.findViewById(R.id.shiftName);
        View colorRectangle = rootView.findViewById(R.id.colorRectangle);

        if(!shiftList.get(i).getName().equals(""))
            shiftName.setText(shiftList.get(i).getName());
        else
            shiftName.setText("No Shift");
        GradientDrawable rectangle = (GradientDrawable) colorRectangle.getBackground();
        rectangle.setColor(shiftList.get(i).getLineColor());

        return rootView;
    }
}
