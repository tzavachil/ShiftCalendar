package com.example.shiftcalendar.ui.calendar;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiftcalendar.R;
import com.example.shiftcalendar.ShiftDayList;
import com.example.shiftcalendar.databinding.FragmentCalendarBinding;
import com.example.shiftcalendar.Shift;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

    public class CalendarFragment extends Fragment implements CalendarAdapter.OnItemListener{

    private FragmentCalendarBinding binding;

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;

    private Button previousMonthBtn;
    private Button nextMonthBtn;

    private ArrayList<Shift> shiftList;
    private ShiftDayList shiftDayList;

    public CalendarFragment(ArrayList<Shift> sL, ShiftDayList sDL) {
        this.shiftList = sL;
        this.shiftDayList = sDL;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        previousMonthBtn = (Button) root.findViewById(R.id.previousMonthBtn);
        nextMonthBtn = (Button) root.findViewById(R.id.nextMonthBtn);

        this.setUpMonthButtonListeners();

        calendarRecyclerView = (RecyclerView) root.findViewById(R.id.calendarRecyclerView);
        monthYearText = (TextView) root.findViewById(R.id.monthYearTV);

        selectedDate = LocalDate.now();
        this.setMonthView();

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView(){
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this, monthYearText, this, this.shiftList, this.shiftDayList);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<String> daysInMonthArray(LocalDate date){
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i=0; i<=42; i++){
            if(i<(dayOfWeek-1) ){
                daysInMonthArray.add("");
            }
            else if(i>daysInMonth+dayOfWeek-2){
                break;
            }
            else{
                daysInMonthArray.add(String.valueOf(i - dayOfWeek + 2));
            }
        }
        return daysInMonthArray;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String monthYearFromDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyy");
        return date.format(formatter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClickListener(int position, String dayText) {
        if(dayText.equals("")){
            String message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDate);
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
        }
    }

    private void setUpMonthButtonListeners(){
        this.previousMonthBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                selectedDate = selectedDate.minusMonths(1);
                setMonthView();
            }
        });

        this.nextMonthBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                selectedDate = selectedDate.plusMonths(1);
                setMonthView();
            }
        });
    }
}