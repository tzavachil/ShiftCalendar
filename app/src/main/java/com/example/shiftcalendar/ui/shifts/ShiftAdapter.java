package com.example.shiftcalendar.ui.shifts;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.shiftcalendar.MainActivity;
import com.example.shiftcalendar.R;
import com.example.shiftcalendar.Shift;
import com.example.shiftcalendar.ui.ShiftDetailsBottomSheet;

import java.util.ArrayList;

public class ShiftAdapter extends ArrayAdapter<Shift> {

    private Fragment rootFragment;

    private ArrayList<Shift> shifts;

    private ConstraintLayout shiftItemLayout;
    private ImageButton deleteShiftButton;
    private ImageButton duplicateShiftButton;

    public ShiftAdapter(Fragment rootFragment, ArrayList<Shift> shifts){
        super(rootFragment.getContext(), 0, shifts);
        this.rootFragment = rootFragment;
        this.shifts = shifts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        Shift shift = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.shift_list_layout, parent, false);
        }
        this.shiftItemLayout = convertView.findViewById(R.id.shiftItemLayout);
        GradientDrawable layoutBackground = (GradientDrawable) this.shiftItemLayout.getBackground();
        layoutBackground.setStroke(3, shift.getBackgroundColor());
        TextView shiftNameTextView = (TextView) convertView.findViewById(R.id.shiftItemName);
        this.deleteShiftButton = (ImageButton) convertView.findViewById(R.id.deleteShiftButton);
        this.duplicateShiftButton = (ImageButton) convertView.findViewById(R.id.duplicateShiftButton);
        this.setUpListeners(position, shift, convertView);
        shiftNameTextView.setText(shift.getName());

        return convertView;
    }

    private void setUpListeners(int position, Shift currShift, View convertView){
        this.shiftItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShiftDetailsBottomSheet shiftDetailsBottomSheet = new ShiftDetailsBottomSheet(currShift, convertView);
                shiftDetailsBottomSheet.show(rootFragment.getActivity().getSupportFragmentManager(), "TAG");
            }
        });
        this.deleteShiftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.shiftList.remove(currShift);
                shifts.remove(currShift);
                notifyDataSetChanged();
            }
        });

        this.duplicateShiftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Shift newShift = new Shift(currShift);
                newShift.setName(currShift.getName() + " - Duplicate");
                if(!MainActivity.shiftList.contains(newShift)) {
                    MainActivity.shiftList.add(position + 1, newShift);
                    shifts.add(position + 1, newShift);
                    notifyDataSetChanged();
                }
            }
        });
    }

    public ArrayList<Shift> getShifts() { return shifts; }

    public void setShifts(ArrayList<Shift> shifts) { this.shifts = shifts; }

}
