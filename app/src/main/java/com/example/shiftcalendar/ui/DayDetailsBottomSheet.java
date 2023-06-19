package com.example.shiftcalendar.ui;

import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.shiftcalendar.R;
import com.example.shiftcalendar.Shift;
import com.example.shiftcalendar.ShiftDay;
import com.example.shiftcalendar.ui.calendar.CalendarViewHolder;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.sql.Time;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DayDetailsBottomSheet extends BottomSheetDialogFragment {

    private static final DecimalFormat df = new DecimalFormat("0.00");

    private ArrayList<Shift> shiftList;
    private Shift myShift;
    private View view;
    private CalendarViewHolder calendarViewHolder;
    private ShiftDay myShiftDay;

    private String date;
    private String notes;
    private int lineColor;
    private int backgroundColor;
    private Time startShift;
    private Time endShift;
    private int dayEarlyExitHours;
    private int dayEarlyExitMin;
    private int dayExtraTimeHours;
    private int dayExtraTimeMin;
    private double incomePerHour;
    private double incomePerExtraHour;
    private double extraIncomeValue;

    private ShiftSpinnerAdapter shiftSpinnerAdapter;

    private TextView dateTextView;
    private EditText notesPlainText;
    private Spinner shiftSpinner;
    private View slide_line;
    private ImageButton notesAddButton;
    private ImageButton notesRemoveButton;

    //Layouts
    private LinearLayout shiftTimeLayout;
    private LinearLayout earlyExitLayout;
    private LinearLayout extraTimeLayout;
    private ConstraintLayout totalTimeLayout;
    private LinearLayout incomeLayout;

    //Lines
    private View shiftTimeLine;
    private View earlyExitLine;
    private View extraTimeLine;
    private View incomeLine;
    private View horizontalLine;

    //Edit Text
    private EditText earlyExitHours;
    private EditText earlyExitMin;
    private EditText extraTimeHours;
    private EditText extraTimeMin;
    private EditText incomePerHourEditText;
    private EditText incomePerExtraHourEditText;
    private EditText extraIncome;

    //Text View
    private TextView startShiftTime;
    private TextView endShiftTime;
    private TextView shiftTimeHours;
    private TextView shiftTimeMin;
    private TextView earlyExitTimeTextHour;
    private TextView earlyExitTimeTextMin;
    private TextView extraTimeTextHour;
    private TextView extraTimeTextMin;
    private TextView totalTimeTextHour;
    private TextView totalTimeTextMin;
    private TextView totalIncome;
    private TextView incomePerHourTV;
    private TextView incomePerExtraHourTV;

    public DayDetailsBottomSheet(String date, ShiftDay myShiftDay, ArrayList<Shift> sL, CalendarViewHolder cvh){

        this.shiftList = sL;
        this.calendarViewHolder = cvh;

        //Day Data Initialization
        this.myShiftDay = myShiftDay;
        this.date = date;
        this.notes = myShiftDay.getNotes();
        this.dayEarlyExitHours = this.myShiftDay.getEarlyExitHours();
        this.dayEarlyExitMin = this.myShiftDay.getEarlyExitMin();
        this.dayExtraTimeHours = this.myShiftDay.getExtraTimeHours();
        this.dayExtraTimeMin = this.myShiftDay.getExtraTimeMin();
        this.incomePerHour = this.myShiftDay.getIncomePerHour();
        this.incomePerExtraHour = this.myShiftDay.getIncomePerExtraHour();
        this.extraIncomeValue = this.myShiftDay.getExtraIncome();

        //Shift Data Initialization
        if(myShiftDay.getShift() == null)
            this.myShift = sL.get(0);
        else
            this.myShift = myShiftDay.getShift();
        this.lineColor = this.myShift.getLineColor();
        this.backgroundColor = this.myShift.getBackgroundColor();
        this.startShift = this.myShift.getStartTime();
        this.endShift = this.myShift.getEndTime();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_day_details, container, false);

        this.dateTextView = (TextView) view.findViewById(R.id.dateTextView);
        this.notesPlainText = (EditText) view.findViewById(R.id.notesPlainText);
        this.shiftSpinner = (Spinner) view.findViewById(R.id.shiftSpinner);
        this.setUpSpinner();
        this.slide_line = (View) view.findViewById(R.id.slide_line);

        //Button
        this.notesAddButton = (ImageButton) view.findViewById(R.id.notesAddButton);
        this.notesRemoveButton = (ImageButton) view.findViewById(R.id.notesRemoveButton);
        if(this.notes == null)
            this.notesRemoveButton.setVisibility(View.GONE);
        else
            this.notesAddButton.setVisibility(View.GONE);
        this.setUpButtonListener();

        //Layouts
        this.shiftTimeLayout = (LinearLayout) view.findViewById(R.id.shiftTimeLayout);
        this.earlyExitLayout = (LinearLayout) view.findViewById(R.id.earlyExitLayout);
        this.extraTimeLayout = (LinearLayout) view.findViewById(R.id.extraTimeLayout);
        this.totalTimeLayout = (ConstraintLayout) view.findViewById(R.id.totalTimeLayout);
        this.incomeLayout = (LinearLayout) view.findViewById(R.id.incomeLayout);

        //Lines
        this.shiftTimeLine = (View) view.findViewById(R.id.shiftTimeLine);
        this.earlyExitLine = (View) view.findViewById(R.id.earlyExitLine);
        this.extraTimeLine = (View) view.findViewById(R.id.extraTimeLine);
        this.incomeLine = (View) view.findViewById(R.id.incomeLine);
        this.horizontalLine = (View) view.findViewById(R.id.horizontalLine);

        //Edit Text
        this.earlyExitHours = (EditText) view.findViewById(R.id.earlyExitHours);
        this.earlyExitHours.setText(String.valueOf(this.dayEarlyExitHours));
        this.earlyExitMin = (EditText) view.findViewById(R.id.earlyExitMin);
        this.earlyExitMin.setText(String.valueOf(this.dayEarlyExitMin));
        this.extraTimeHours = (EditText) view.findViewById(R.id.extraTimeHours);
        this.extraTimeHours.setText(String.valueOf(dayExtraTimeHours));
        this.extraTimeMin = (EditText) view.findViewById(R.id.extraTimeMin);
        this.extraTimeMin.setText(String.valueOf(dayExtraTimeMin));
        this.incomePerHourEditText = (EditText) view.findViewById(R.id.incomePerHour);
        this.incomePerExtraHourEditText = (EditText) view.findViewById(R.id.incomePerExtraHour);
        this.extraIncome = (EditText) view.findViewById(R.id.extraIncome);

        //Text View
        this.startShiftTime = (TextView) view.findViewById(R.id.startShiftTime);
        this.endShiftTime = (TextView) view.findViewById(R.id.endShiftTime);
        this.shiftTimeHours = (TextView) view.findViewById(R.id.shiftTimeHours);
        this.shiftTimeMin = (TextView) view.findViewById(R.id.shiftTimeMin);
        this.earlyExitTimeTextHour = (TextView) view.findViewById(R.id.earlyExitTimeTextHour);
        this.earlyExitTimeTextMin = (TextView) view.findViewById(R.id.earlyExitTimeTextMin);
        this.extraTimeTextHour = (TextView) view.findViewById(R.id.extraTimeTextHour);
        this.extraTimeTextMin = (TextView) view.findViewById(R.id.extraTimeTextMin);
        this.setUpEditTextListeners();
        this.totalTimeTextHour = (TextView) view.findViewById(R.id.totalTimeTVHour);
        this.totalTimeTextMin = (TextView) view.findViewById(R.id.totalTimeTVMin);
        this.totalIncome = (TextView) view.findViewById(R.id.totalIncome);
        this.incomePerHourTV = (TextView) view.findViewById(R.id.incomePerHourTV);
        this.incomePerExtraHourTV = (TextView) view.findViewById(R.id.incomePerExtraHourTV);

        try {
            this.setUpTextViews();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.setUpIncome();

        this.dateTextView.setText(this.date);
        if(notes!=null){
            this.notesPlainText.setText(this.notes);
        }
        else{
            this.notesPlainText.setVisibility(View.GONE);
        }
        this.changeColor();
        if(this.myShiftDay.getShift() == null) this.displayDetails(0);

        try {
            this.sumTime();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return view;
    }

    private void displayDetails(int i){
        switch (i){
            case 0:
                this.shiftTimeLayout.setVisibility(View.GONE);
                this.earlyExitLayout.setVisibility(View.GONE);
                this.extraTimeLayout.setVisibility(View.GONE);
                this.totalTimeLayout.setVisibility(View.GONE);
                this.incomeLayout.setVisibility(View.GONE);
                this.notesAddButton.setVisibility(View.GONE);
                break;
            case 1:
                this.shiftTimeLayout.setVisibility(View.VISIBLE);
                this.earlyExitLayout.setVisibility(View.VISIBLE);
                this.extraTimeLayout.setVisibility(View.VISIBLE);
                this.totalTimeLayout.setVisibility(View.VISIBLE);
                this.incomeLayout.setVisibility(View.VISIBLE);
                this.notesAddButton.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setUpSpinner(){
        shiftSpinnerAdapter = new ShiftSpinnerAdapter(this.getActivity(), this.shiftList);
        this.shiftSpinner.setAdapter(shiftSpinnerAdapter);
        this.shiftSpinner.setSelection(this.myShiftPosition());
        this.shiftSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Shift selectedShift = shiftList.get((Integer) shiftSpinner.getSelectedItem());
                lineColor = selectedShift.getLineColor();
                backgroundColor = selectedShift.getBackgroundColor();
                changeColor();
                startShift = selectedShift.getStartTime();
                endShift = selectedShift.getEndTime();
                if(myShiftDay.getShift() == null) {
                    myShiftDay.setIncomePerHour(selectedShift.getIncomePerHour());
                    myShiftDay.setIncomePerExtraHour(selectedShift.getIncomePerExtraHour());
                }
                incomePerHour = myShiftDay.getIncomePerHour();
                incomePerExtraHour = myShiftDay.getIncomePerExtraHour();
                try {
                    setUpTextViews();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                myShiftDay.setShift(selectedShift);
                myShiftDay.setColor(selectedShift.getBackgroundColor());
                setUpIncome();
                displayDetails(1);
                if(selectedShift.getName().equals("")) {
                    calendarViewHolder.setEmptyTextColor();
                    myShiftDay.setShift(null);
                    displayDetails(0);
                } else {
                    calendarViewHolder.saveDayChanges(myShiftDay);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private int myShiftPosition(){

        for(int i=0; i<this.shiftList.size(); i++)
            if (this.shiftList.get(i) == this.myShift) return i;

        return 0;
    }

    private void changeColor(){
        //Spinner
        GradientDrawable spinnerBackground = (GradientDrawable) this.shiftSpinner.getBackground();
        if(String.format("#%06X", (0xFFFFFF & this.lineColor)).equals("#6A6A6A"))
            spinnerBackground.setStroke(8, this.lineColor);
        else
            spinnerBackground.setStroke(8, this.backgroundColor);

        //Slide Line
        GradientDrawable slideLineBackground = (GradientDrawable) this.slide_line.getBackground();
        slideLineBackground.setColor(this.backgroundColor);

        //Notes Plain Text
        this.notesPlainText.setBackgroundTintList(ColorStateList.valueOf(this.backgroundColor));

        //Layouts
        this.changeLayoutColor(this.shiftTimeLayout,3,this.backgroundColor);
        this.changeLayoutColor(this.earlyExitLayout,3,this.backgroundColor);
        this.changeLayoutColor(this.extraTimeLayout,3,this.backgroundColor);
        this.changeLayoutColor(this.totalTimeLayout,3,this.backgroundColor);
        this.changeLayoutColor(this.incomeLayout,3,this.backgroundColor);

        //Lines
        this.changeLineColor(this.shiftTimeLine,this.backgroundColor);
        this.changeLineColor(this.earlyExitLine,this.backgroundColor);
        this.changeLineColor(this.extraTimeLine,this.backgroundColor);
        this.changeLineColor(this.incomeLine,this.backgroundColor);
        this.changeLineColor(this.horizontalLine,this.backgroundColor);

        //Edit Text
        this.changeEditTextColor(this.earlyExitHours,3,this.backgroundColor);
        this.changeEditTextColor(this.earlyExitMin,3,this.backgroundColor);
        this.changeEditTextColor(this.extraTimeHours,3,this.backgroundColor);
        this.changeEditTextColor(this.extraTimeMin,3,this.backgroundColor);
        this.changeEditTextColor(this.incomePerHourEditText,3,this.backgroundColor);
        this.changeEditTextColor(this.incomePerExtraHourEditText,3,this.backgroundColor);
        this.changeEditTextColor(this.extraIncome,3,this.backgroundColor);

        this.calendarViewHolder.setBackgroundColor(this.backgroundColor);
        this.calendarViewHolder.setTextColor();
    }

    private void changeLayoutColor(ViewGroup l, int strokeWidth, int color){
        GradientDrawable layoutBackground = (GradientDrawable) l.getBackground();
        layoutBackground.setStroke(strokeWidth, color);
    }

    public void changeLineColor(View line, int color){
        GradientDrawable horizontalLineBackground = (GradientDrawable) line.getBackground();
        horizontalLineBackground.setColor(color);
    }

    public void changeEditTextColor(EditText eT, int strokeWidth, int color){
        GradientDrawable layoutBackground = (GradientDrawable) eT.getBackground();
        layoutBackground.setStroke(strokeWidth, color);
    }

    private void setUpButtonListener(){
        this.notesAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notesPlainText.setVisibility(View.VISIBLE);
                notesAddButton.setVisibility(View.GONE);
                notesRemoveButton.setVisibility(View.VISIBLE);
            }
        });
        this.notesRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notesPlainText.setVisibility(View.GONE);
                notesAddButton.setVisibility(View.VISIBLE);
                notesRemoveButton.setVisibility(View.GONE);
                myShiftDay.setNotes(null);
            }
        });
    }

    private void setUpEditTextListeners(){
        this.earlyExitHours.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String hours = earlyExitHours.getText().toString();
                if (!hours.equals("")) {
                    earlyExitTimeTextHour.setText(hours + " h");
                    myShiftDay.setEarlyExitHours(Integer.parseInt(hours));
                }
                else {
                    earlyExitTimeTextHour.setText("0 h");
                    myShiftDay.setEarlyExitHours(0);
                }
                try {
                    sumTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        this.earlyExitMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String min = earlyExitMin.getText().toString();
                if(!min.equals("")) {
                    earlyExitTimeTextMin.setText(min + " m");
                    myShiftDay.setEarlyExitMin(Integer.parseInt(min));
                }
                else {
                    earlyExitTimeTextMin.setText("0 m");
                    myShiftDay.setEarlyExitMin(0);
                }
                try {
                    sumTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        this.extraTimeHours.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String hours = extraTimeHours.getText().toString();
                if(!hours.equals("")) {
                    extraTimeTextHour.setText(hours + " h");
                    myShiftDay.setExtraTimeHours(Integer.parseInt(hours));
                }
                else {
                    extraTimeTextHour.setText("0 h");
                    myShiftDay.setExtraTimeHours(0);
                }
                try {
                    sumTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        this.extraTimeMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String min = extraTimeMin.getText().toString();
                if(!min.equals("")) {
                    extraTimeTextMin.setText(min + " m");
                    myShiftDay.setExtraTimeMin(Integer.parseInt(min));
                }
                else {
                    extraTimeTextMin.setText("0 m");
                    myShiftDay.setExtraTimeMin(0);
                }
                try {
                    sumTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        this.notesPlainText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                myShiftDay.setNotes(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void sumTime() throws ParseException {
        int shiftHours = Integer.parseInt(this.shiftTimeHours.getText().toString().replaceAll(" h", ""));
        int shiftMin = Integer.parseInt(this.shiftTimeMin.getText().toString().replaceAll(" m", ""));
        int earlyHours = Integer.parseInt(this.earlyExitTimeTextHour.getText().toString().replaceAll(" h", ""));
        int earlyMin = Integer.parseInt(this.earlyExitTimeTextMin.getText().toString().replaceAll(" m", ""));
        int extraHours = Integer.parseInt(this.extraTimeTextHour.getText().toString().replaceAll(" h", ""));
        int extraMin = Integer.parseInt(this.extraTimeTextMin.getText().toString().replaceAll(" m", ""));

        int hours = shiftHours;
        int min;

        if(earlyMin > shiftMin){
            min = 60 - earlyMin + shiftMin;
            hours --;
        }
        else
            min = shiftMin - earlyMin;

        hours -= earlyHours;

        if((extraMin + min) >= 60){
            hours += ((min + extraMin) / 60);
            min = (min + extraMin) % 60;
        }
        else
            min += extraMin;

        hours += extraHours;

        Log.d("Debug", hours+"");

        this.totalTimeTextMin.setText(min + " m");
        this.totalTimeTextHour.setText(hours + " h");

    }

    private void setUpTextViews() throws ParseException {
        this.startShiftTime.setText(this.startShift.toString().substring(0,this.startShift.toString().length()-3));
        this.endShiftTime.setText(this.endShift.toString().substring(0,this.endShift.toString().length()-3));

        String timeDif = this.timeDifferenceToString(this.startShift.toString(), this.endShift.toString());

        this.shiftTimeHours.setText(timeDif.split("/")[0]);
        this.shiftTimeMin.setText(timeDif.split("/")[1]);
        String hours = this.earlyExitHours.getText().toString();
        String min = this.earlyExitMin.getText().toString();
        this.earlyExitTimeTextHour.setText(hours + " h");
        this.earlyExitTimeTextMin.setText(min + " m");
        hours = this.extraTimeHours.getText().toString();
        min = this.extraTimeMin.getText().toString();
        this.extraTimeTextHour.setText(hours + " h");
        this.extraTimeTextMin.setText(min + " m");

    }

    private void setUpIncome(){
        this.incomePerHourEditText.setText(Double.toString(this.incomePerHour));
        this.incomePerExtraHourEditText.setText(Double.toString(this.incomePerExtraHour));
        this.extraIncome.setText(Double.toString(extraIncomeValue));

        int totalHours = Integer.parseInt(this.totalTimeTextHour.getText().toString().replaceAll(" h", ""));
        int totalMin = Integer.parseInt(this.totalTimeTextMin.getText().toString().replaceAll(" m", ""));
        double i1 = incomePerHour * (totalHours + ((double) totalMin / 60));
        this.incomePerHourTV.setText(Double.toString(i1));

        int extraHours = Integer.parseInt(this.extraTimeTextHour.getText().toString().replaceAll(" h", ""));
        int extraMin = Integer.parseInt(this.extraTimeTextMin.getText().toString().replaceAll(" m", ""));
        double i2 = incomePerExtraHour * (extraHours + ((double) extraMin / 60));
        this.incomePerExtraHourTV.setText(df.format(i2));


        double i3 = Double.parseDouble(this.extraIncome.getText().toString());
        this.totalIncome.setText(Double.toString(i1 + i2 + i3));

        this.setUpIncomeFieldsListeners();
    }

    private void setUpIncomeFieldsListeners(){
        this.incomePerHourEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String incomePerHourEditTextString = incomePerHourEditText.getText().toString();
                double tempIncomePerHour;
                if(incomePerHourEditTextString.equals(""))
                    tempIncomePerHour = 0;
                else
                    tempIncomePerHour = Double.parseDouble(incomePerHourEditText.getText().toString());
                myShiftDay.setIncomePerHour(tempIncomePerHour);
                int totalHours = Integer.parseInt(totalTimeTextHour.getText().toString().replaceAll(" h", ""));
                int totalMin = Integer.parseInt(totalTimeTextMin.getText().toString().replaceAll(" m", ""));
                int extraHours = Integer.parseInt(extraTimeTextHour.getText().toString().replaceAll(" h", ""));
                int extraMin = Integer.parseInt(extraTimeTextMin.getText().toString().replaceAll(" m", ""));
                double income1 = tempIncomePerHour * ((totalHours - extraHours) + ((double) (totalMin - extraMin) / 60));
                incomePerHourTV.setText(Double.toString(income1));

                double income2 = Double.parseDouble(incomePerExtraHourTV.getText().toString());
                double income3 = Double.parseDouble(extraIncome.getText().toString());

                totalIncome.setText(Double.toString(income1 + income2 + income3));
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        this.incomePerExtraHourEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String incomePerExtraHourEditTextString = incomePerExtraHourEditText.getText().toString();
                double tempIncomePerExtraHour;
                if(incomePerExtraHourEditTextString.equals(""))
                    tempIncomePerExtraHour = 0;
                else
                    tempIncomePerExtraHour = Double.parseDouble(incomePerExtraHourEditText.getText().toString());
                myShiftDay.setIncomePerExtraHour(tempIncomePerExtraHour);
                int extraHours = Integer.parseInt(extraTimeTextHour.getText().toString().replaceAll(" h", ""));
                int extraMin = Integer.parseInt(extraTimeTextMin.getText().toString().replaceAll(" m", ""));
                double income1 = tempIncomePerExtraHour * (extraHours + ((double) extraMin / 60));
                incomePerExtraHourTV.setText(df.format(income1));

                double income2 = Double.parseDouble(incomePerHourTV.getText().toString());
                double income3 = Double.parseDouble(extraIncome.getText().toString());

                totalIncome.setText(Double.toString(income1 + income2 + income3));
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        this.extraIncome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String extraIncomeString = extraIncome.getText().toString();
                double tempExtraIncome;
                if(extraIncomeString.equals(""))
                    tempExtraIncome = 0;
                else
                    tempExtraIncome = Double.parseDouble(extraIncome.getText().toString());
                myShiftDay.setExtraIncome(tempExtraIncome);

                double income1 = tempExtraIncome;

                double income2 = Double.parseDouble(incomePerHourTV.getText().toString());
                double income3 = Double.parseDouble(incomePerExtraHourTV.getText().toString());

                totalIncome.setText(Double.toString(income1 + income2 + income3));
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        this.totalTimeTextHour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onTotalTimeChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        this.totalTimeTextMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onTotalTimeChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void onTotalTimeChanged(){
        int totalHours = Integer.parseInt(totalTimeTextHour.getText().toString().replaceAll(" h", ""));
        int totalMin = Integer.parseInt(totalTimeTextMin.getText().toString().replaceAll(" m", ""));
        int extraHours = Integer.parseInt(extraTimeTextHour.getText().toString().replaceAll(" h", ""));
        int extraMin = Integer.parseInt(extraTimeTextMin.getText().toString().replaceAll(" m", ""));

        double tempIncomePerHour = Double.parseDouble(incomePerHourEditText.getText().toString());
        double income1 = tempIncomePerHour * ((totalHours - extraHours) + ((double) (totalMin - extraMin) / 60));
        incomePerHourTV.setText(String.valueOf(income1));
        double tempIncomePerExtraHour = Double.parseDouble(incomePerExtraHourEditText.getText().toString());
        double income2 = tempIncomePerExtraHour * (extraHours + ((double) extraMin / 60));
        incomePerExtraHourTV.setText(df.format(income2));
        double income3 = Double.parseDouble(extraIncome.getText().toString());

        totalIncome.setText(Double.toString(income1 + income2 + income3));
    }

    public static String timeDifferenceToString(String time1, String time2) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date date1 = format.parse(time1);
        Date date2 = format.parse(time2);
        long difference = date2.getTime() - date1.getTime();

        int minutes = (int) ((difference / (1000*60)) % 60);
        int hours   = (int) ((difference / (1000*60*60)) % 24);

        return hours + " h/" + minutes + " m";
    }

}
