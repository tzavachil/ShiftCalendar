package com.example.shiftcalendar.ui;

import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.shiftcalendar.MainActivity;
import com.example.shiftcalendar.R;
import com.example.shiftcalendar.Shift;
import com.example.shiftcalendar.ui.shifts.ShiftAdapter;
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
    private Button saveChangesBtn;

    private LinearLayout shiftTimeLayout;
    private LinearLayout incomeLayout;

    private int hour;
    private int minute;

    private int color;
    private View convertView;

    private Context context;

    private ShiftAdapter currShiftAdapter;

    public ShiftDetailsBottomSheet(Shift s, View convertView){
        this.currShift = s;
        this.color = currShift.getBackgroundColor();
        this.convertView = convertView;
    }

    public ShiftDetailsBottomSheet(Context context, ShiftAdapter shiftAdapter){
        this.currShift = new Shift(context);
        this.context = context;
        this.currShift.setName("New Shift");
        this.color = currShift.getBackgroundColor();
        this.convertView = null;
        this.currShiftAdapter = shiftAdapter;
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
        this.saveChangesBtn = view.findViewById(R.id.saveChangesBtn);

        this.shiftNameEditText.setText(this.currShift.getName());
        this.incomePerHour.setText(String.valueOf(this.currShift.getIncomePerHour()));
        this.incomePerExtraHour.setText(String.valueOf(this.currShift.getIncomePerExtraHour()));
        this.setColor(this.backgroundColorView, currShift.getBackgroundColor());
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
                            Log.d("Debug", "New Color: " + String.format("#%06X", (0xFFFFFF & newColor)));
                            Log.d("Debug", "New Color: " + String.format("#%06X", (0xFFFFFF & currShift.getBackgroundColor())));
                            setColor(backgroundColorView, newColor);
                            backgroundColorId.setTextColor(Color.BLACK);
                            color = newColor;
                            setUpColors(color);
                        }
                    } catch (NumberFormatException e){
                        backgroundColorId.setTextColor(Color.RED);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        this.saveChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges();
            }
        });
    }

    private void saveChanges(){

        boolean save = false;

        //Save color
        //ColorDrawable viewBG = (ColorDrawable) this.backgroundColorView.getBackground();
        //Can't save any color bcz in 'if' i check the shift's background which is not changed until it saved
        if(this.currShift.getBackgroundColor() != Color.WHITE) {
            this.currShift.setBackgroundColor(this.color);
            this.backgroundColorId.setTextColor(Color.BLACK);
            save = true;
            dismiss();
        }
        else
            this.backgroundColorId.setTextColor(Color.RED);

        //Save name
        this.currShift.setName(this.shiftNameEditText.getText().toString());

        //Save start shift time
        String time = startShiftTime.getText().toString();
        int hour = Integer.parseInt(time.split(":")[0]);
        int min = Integer.parseInt(time.split(":")[1]);
        currShift.setStartTime(new Time(hour, min, 0));

        //Save end shift time
        time = endShiftTime.getText().toString();
        hour = Integer.parseInt(time.split(":")[0]);
        min = Integer.parseInt(time.split(":")[1]);
        currShift.setEndTime(new Time(hour, min, 0));

        //Save income per hour
        String incomeStr = incomePerHour.getText().toString();
        if(incomeStr.length() != 0){
            currShift.setIncomePerHour(Double.parseDouble(incomeStr));
        }

        //Save income per extra hour
        String incomeExtraStr = incomePerExtraHour.getText().toString();
        if(incomeStr.length() != 0){
            currShift.setIncomePerExtraHour(Double.parseDouble(incomeExtraStr));
        }

        if(this.convertView != null) {
            GradientDrawable layoutBackground = (GradientDrawable) this.convertView.findViewById(R.id.shiftItemLayout).getBackground();
            layoutBackground.setStroke(3, currShift.getBackgroundColor());
            TextView tempTV = this.convertView.findViewById(R.id.shiftItemName);
            tempTV.setText(this.shiftNameEditText.getText().toString());
        } else if(save){
            MainActivity.shiftList.add(this.currShift);
            this.currShiftAdapter.getShifts().add(this.currShift);
            this.currShiftAdapter.notifyDataSetChanged();
        }
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
        if(this.context != null && shiftColor == context.getResources().getColor(R.color.white, context.getTheme())){
            shiftColor = this.currShift.getLineColor();
        }

        this.setStroke(this.shiftTimeLayout, shiftColor);
        this.setStroke(this.incomeLayout, shiftColor);
        this.setColor(this.slideLine, shiftColor);
        this.setColor(this.shiftTimeLine, shiftColor);
        this.setColor(this.incomeLine, shiftColor);
        this.setStroke(this.incomePerHour, shiftColor);
        this.setStroke(this.incomePerExtraHour, shiftColor);
        this.setStroke(this.backgroundColorView, this.currShift.getLineColor());
        this.setColor(this.backgroundColorView, currShift.getBackgroundColor());
    }

    private void setUpColors(int color){
        this.setStroke(this.shiftTimeLayout, color);
        this.setStroke(this.incomeLayout, color);
        this.setColor(this.slideLine, color);
        this.setColor(this.shiftTimeLine, color);
        this.setColor(this.incomeLine, color);
        this.setStroke(this.incomePerHour, color);
        this.setStroke(this.incomePerExtraHour, color);
        this.setColor(this.backgroundColorView, color);
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
            this.backgroundColorId.setText(String.format("%06X", (0xFFFFFF & newColor)));
            setUpColors(newColor);
            return true;
        }
        return false;
    }
}
