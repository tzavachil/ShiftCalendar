package com.example.shiftcalendar;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.shiftcalendar.databinding.ActivityMainBinding;
import com.example.shiftcalendar.ui.calendar.CalendarFragment;
import com.example.shiftcalendar.ui.shifts.ShiftsFragment;
import com.example.shiftcalendar.ui.summary.SummaryFragment;

import java.sql.Time;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public static ArrayList<Shift> shiftList;
    private ShiftDayList shiftDayList;
    public static int lightGray;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lightGray = getResources().getColor(R.color.light_grey);

        shiftList = new ArrayList<>();
        this.initializingShifts();
        this.shiftDayList = new ShiftDayList();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new CalendarFragment(this.shiftList, this.shiftDayList));

        binding.navView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.navigation_calendar:
                    replaceFragment(new CalendarFragment(this.shiftList, this.shiftDayList));
                    break;
                case R.id.navigation_summary:
                    replaceFragment(new SummaryFragment());
                    break;
                case R.id.navigation_shifts:
                    replaceFragment(new ShiftsFragment(this.shiftList));
                    break;
            }

            return true;
        });

    }

    //Temp
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initializingShifts(){
        Shift emptyShift = new Shift(this);
        Shift shift1 = new Shift("Shift 1", getResources().getColor(R.color.blue), new Time(7,0,0), new Time(15, 0, 0), 4.0, 4.4);
        Shift shift2 = new Shift("Shift 2", getResources().getColor(R.color.red), new Time(6,0,0), new Time(14, 0, 0), 0, 0);
        Shift shift3 = new Shift("Shift 3", getResources().getColor(R.color.green), new Time(15,0,0), new Time(23, 0, 0), 4.0, 4.4);
        Shift shift4 = new Shift("Shift 4", getResources().getColor(R.color.orange), new Time(14,0,0), new Time(22, 0, 0), 1, 2);

        this.shiftList.add(emptyShift);
        this.shiftList.add(shift1);
        this.shiftList.add(shift2);
        this.shiftList.add(shift3);
        this.shiftList.add(shift4);
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

}