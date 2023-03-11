package com.example.shiftcalendar.ui;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.shiftcalendar.R;
import com.example.shiftcalendar.Shift;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ShiftDetailsBottomSheet extends BottomSheetDialogFragment {

    private View view;

    private Shift currShift;

    private EditText shiftNameEditText;
    private EditText incomePerHour;
    private EditText incomePerExtraHour;
    private TextView startShiftTime;
    private TextView endShiftTime;
    private TextView shiftTimeHours;
    private TextView shiftTimeMinutes;

    private int hour;
    private int minute;

    public ShiftDetailsBottomSheet(Shift s){
        this.currShift = s;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shift_details, container, false);

        this.shiftNameEditText = view.findViewById(R.id.shiftNameEditText);
        this.incomePerHour = view.findViewById(R.id.incomePerHour);
        this.incomePerExtraHour = view.findViewById(R.id.incomePerExtraHour);
        this.startShiftTime = view.findViewById(R.id.startShiftTime);
        this.endShiftTime = view.findViewById(R.id.endShiftTime);
        this.shiftTimeHours = view.findViewById(R.id.shiftTimeHours);
        this.shiftTimeMinutes = view.findViewById(R.id.shiftTimeMin);

        this.shiftNameEditText.setText(this.currShift.getName());
        this.incomePerHour.setText(String.valueOf(this.currShift.getIncomePerHour()));
        this.incomePerExtraHour.setText(String.valueOf(this.currShift.getIncomePerExtraHour()));
        this.setUpListeners();

        return view;
    }

    private void setUpListeners(){
        this.startShiftTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popTimePicker(view, startShiftTime);
            }
        });
        this.startShiftTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    updateTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        this.endShiftTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popTimePicker(view, endShiftTime);
            }
        });
        this.endShiftTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    updateTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void popTimePicker(View view, TextView textView){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                textView.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this.getContext(), onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    private void updateTime() throws ParseException {
        String time1 = startShiftTime.getText().toString();
        String time2 = endShiftTime.getText().toString();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date date1 = format.parse(time1);
        Date date2 = format.parse(time2);
        long difference = date2.getTime() - date1.getTime();

        int minutes = (int) ((difference / (1000*60)) % 60);
        int hours   = (int) ((difference / (1000*60*60)) % 24);
        if(hours < 0)
            hours = 24 + hours;
        if(minutes < 0){
            hours--;
            minutes = 60 + minutes;
        }

        shiftTimeHours.setText(String.valueOf(hours) + " h");
        shiftTimeMinutes.setText(String.valueOf(minutes) + " m");
    }

}
