package com.example.shiftcalendar;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DayDetailsFragment extends Fragment {

    private TextView dateTextView;
    private Spinner shiftSpinner;
    private String dateTextViewContext = "";

    private int color;

    public DayDetailsFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day_details, container, false);

        dateTextView = view.findViewById(R.id.dateTextView);
        dateTextView.setText(dateTextViewContext);

        shiftSpinner = view.findViewById(R.id.shiftSpinner);
        GradientDrawable spinnerBackground = (GradientDrawable) shiftSpinner.getBackground();
        spinnerBackground.mutate();
        //spinnerBackground.setStroke(8, color);


        return view;
    }

    public void setDateTextViewContextText(String text){ dateTextViewContext= text; }
}