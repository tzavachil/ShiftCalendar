package com.example.shiftcalendar.ui.summary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.shiftcalendar.R;
import com.example.shiftcalendar.ShiftDay;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class ExportOptionsBottomSheet extends BottomSheetDialogFragment {

    private View view;
    private ArrayList<ShiftDay> currShiftDayList;
    private String fileName;
    private String fileDestination;

    private ImageButton pdfExportButton;
    private TextView pdfDestinationTextView;
    private TextView pdfNameTextView;
    private ImageButton excelExportButton;
    private TextView excelDestinationTextView;
    private TextView excelNameTextView;

    public ExportOptionsBottomSheet(ArrayList<ShiftDay> shiftDayArrayList, String fileName){
        this.currShiftDayList = shiftDayArrayList;
        this.fileName = fileName.replaceAll(" ", "-");
        this.fileDestination = this.getContext().getFilesDir().toString();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.export_options, container, false);

        this.pdfDestinationTextView = view.findViewById(R.id.pdfDestinationTextView);
        this.pdfDestinationTextView.setText(this.fileDestination);
        this.pdfNameTextView = view.findViewById(R.id.pdfNameTextView);
        this.pdfNameTextView.setText("SC-" + this.fileName + ".pdf");
        this.excelDestinationTextView = view.findViewById(R.id.excelDestinationTextView);
        this.excelDestinationTextView.setText(this.fileDestination);
        this.excelNameTextView = view.findViewById(R.id.excelNameTextView);
        this.excelNameTextView.setText("SC-" + this.fileName + ".xls");

        this.pdfExportButton = view.findViewById(R.id.pdfExportButton);
        this.excelExportButton = view.findViewById(R.id.excelExportButton);
        this.setUpListeners();

        return view;

    }

    private void setUpListeners(){
        this.pdfExportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        this.excelExportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
