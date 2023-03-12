package com.example.shiftcalendar.ui;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.shiftcalendar.R;
import com.example.shiftcalendar.Shift;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import eltos.simpledialogfragment.SimpleDialog;
import eltos.simpledialogfragment.color.SimpleColorDialog;

public class ShiftDetailsBottomSheet extends BottomSheetDialogFragment implements SimpleDialog.OnDialogResultListener {

    private View view;

    private Shift currShift;

    private EditText shiftNameEditText;
    private EditText incomePerHour;
    private EditText incomePerExtraHour;
    private TextView startShiftTime;
    private TextView endShiftTime;
    private TextView shiftTimeHours;
    private TextView shiftTimeMinutes;
    private TextView incomePerHourTV;
    private View backgroundColorView;
    private EditText backgroundColorId;
    private View slideLine;
    private View shiftTimeLine;
    private View incomeLine;

    private LinearLayout shiftTimeLayout;
    private LinearLayout incomeLayout;

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
        this.incomePerHourTV = view.findViewById(R.id.incomePerHourTV);
        this.shiftTimeLayout = view.findViewById(R.id.shiftTimeLayout);
        this.incomeLayout = view.findViewById(R.id.incomeLayout);
        this.backgroundColorView = view.findViewById(R.id.backgroundColor);
        this.backgroundColorId = view.findViewById(R.id.backgroundColorId);
        this.slideLine = view.findViewById(R.id.slide_line);
        this.shiftTimeLine = view.findViewById(R.id.shiftTimeLine);
        this.incomeLine = view.findViewById(R.id.incomeLine);

        this.shiftNameEditText.setText(this.currShift.getName());
        this.incomePerHour.setText(String.valueOf(this.currShift.getIncomePerHour()));
        this.incomePerExtraHour.setText(String.valueOf(this.currShift.getIncomePerExtraHour()));
        this.backgroundColorView.getBackground().setColorFilter(currShift.getBackgroundColor(), PorterDuff.Mode.SRC_IN);
        this.backgroundColorId.setText(String.format("%06X", (0xFFFFFF & currShift.getBackgroundColor())));

        try {
            this.updateTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.calcIncome();
        this.setUpListeners();
        this.setUpColors();

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
                    String time = startShiftTime.getText().toString();
                    int hour = Integer.parseInt(time.split(":")[0]);
                    int min = Integer.parseInt(time.split(":")[1]);
                    currShift.setStartTime(new Time(hour, min, 0));
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
                    String time = endShiftTime.getText().toString();
                    int hour = Integer.parseInt(time.split(":")[0]);
                    int min = Integer.parseInt(time.split(":")[1]);
                    currShift.setEndTime(new Time(hour, min, 0));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        this.shiftTimeHours.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { calcIncome(); }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        this.shiftTimeMinutes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { calcIncome(); }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        this.incomePerHour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                calcIncome();
                String incomeStr = incomePerHour.getText().toString();
                if(incomeStr.length() != 0){
                    currShift.setIncomePerHour(Double.parseDouble(incomeStr));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        this.incomePerExtraHour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String incomeStr = incomePerExtraHour.getText().toString();
                if(incomeStr.length() != 0){
                    currShift.setIncomePerExtraHour(Double.parseDouble(incomeStr));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        this.backgroundColorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createColorPicker();
            }
        });
        this.backgroundColorId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = backgroundColorId.getText().toString();
                if(text.matches("\\w{6}")) {
                    int newColor;
                    try {
                        newColor = Color.parseColor("#" + text);
                        if(newColor != currShift.getBackgroundColor()) {
                            backgroundColorView.getBackground().setColorFilter(newColor, PorterDuff.Mode.SRC_IN);
                            backgroundColorId.setTextColor(Color.BLACK);
                            currShift.setBackgroundColor(newColor);
                            setUpColors();
                        }
                    } catch (NumberFormatException e){
                        backgroundColorId.setTextColor(Color.RED);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void createColorPicker(){
        SimpleColorDialog.build().title("Pick a color").allowCustom(true).colorPreset(currShift.getBackgroundColor()).show(this,"ColorPickerTag");
    }

    private void calcIncome(){
        double incomePerHour;
        if(this.incomePerHour.getText().toString().equals("")) incomePerHour = 0;
        else incomePerHour = Double.parseDouble(this.incomePerHour.getText().toString());
        int hours = Integer.parseInt(this.shiftTimeHours.getText().toString().replaceAll(" h", ""));
        int min = Integer.parseInt(this.shiftTimeMinutes.getText().toString().replaceAll(" m", ""));

        double income = incomePerHour * (hours + ((double) min / 60));

        this.incomePerHourTV.setText(String.valueOf(income) + " €");
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

        int initialHour = Integer.parseInt(textView.getText().toString().split(":")[0]);
        int initialMin = Integer.parseInt(textView.getText().toString().split(":")[1]);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this.getContext(), onTimeSetListener, initialHour, initialMin, true);

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

    private void setUpColors(){

        int shiftColor = this.currShift.getBackgroundColor();

        this.setStroke(this.shiftTimeLayout, shiftColor);
        this.setStroke(this.incomeLayout, shiftColor);
        this.setColor(this.slideLine, shiftColor);
        this.setColor(this.shiftTimeLine, shiftColor);
        this.setColor(this.incomeLine, shiftColor);
        this.setStroke(this.incomePerHour, shiftColor);
        this.setStroke(this.incomePerExtraHour, shiftColor);
        this.backgroundColorView.getBackground().setColorFilter(shiftColor, PorterDuff.Mode.SRC_IN);
    }

    private void setStroke(View view, int color){
        GradientDrawable viewBG = (GradientDrawable) view.getBackground();
        viewBG.setStroke(3, color);
    }

    private void setColor(View view, int color){
        GradientDrawable viewBG = (GradientDrawable) view.getBackground();
        viewBG.setColor(color);
    }

    @Override
    public boolean onResult(@NonNull String dialogTag, int which, @NonNull Bundle extras) {
        if ("ColorPickerTag".equals(dialogTag) && which == BUTTON_POSITIVE){
            int newColor = extras.getInt(SimpleColorDialog.COLOR);
            Log.d("Debug", newColor + " ");
            this.backgroundColorId.setText(String.format("%06X", (0xFFFFFF & newColor)));
            this.currShift.setBackgroundColor(newColor);
            setUpColors();
            return true;
        }
        return false;
    }
}
