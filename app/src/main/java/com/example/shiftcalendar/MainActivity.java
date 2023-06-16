package com.example.shiftcalendar;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.shiftcalendar.databinding.ActivityMainBinding;
import com.example.shiftcalendar.ui.calendar.CalendarFragment;
import com.example.shiftcalendar.ui.shifts.ShiftsFragment;
import com.example.shiftcalendar.ui.summary.SummaryFragment;

import java.sql.Time;
import java.util.ArrayList;
import android.icu.util.Calendar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public static ArrayList<Shift> shiftList;
    private ShiftDayList shiftDayList;
    public static int lightGray;

    public Content content;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setMessage("Allow access to manage all files?");
                builder.setTitle("All files access");
                builder.setCancelable(false);

                builder.setPositiveButton("ALLOW", (DialogInterface.OnClickListener) (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                });

                builder.setNegativeButton("DENY", (DialogInterface.OnClickListener) (dialog, which) -> {
                    Toast.makeText(this, "You can't extract your calendar data", Toast.LENGTH_LONG).show();
                    dialog.cancel();
                });


                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE
            }, PackageManager.PERMISSION_GRANTED);
        }

        lightGray = getResources().getColor(R.color.light_grey);

        /*shiftList = new ArrayList<>();
        this.shiftDayList = new ShiftDayList();
        this.initializingShifts();
        this.initializingDays();*/

        Shift emptyShift = new Shift(this);
        this.content = new Content(emptyShift);
        shiftList = content.getShiftList();
        this.shiftDayList = content.getShiftDayList();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new CalendarFragment(this.shiftList, this.shiftDayList));

        binding.navView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.navigation_calendar:
                    replaceFragment(new CalendarFragment(this.shiftList, this.shiftDayList));
                    break;
                case R.id.navigation_summary:
                    replaceFragment(new SummaryFragment(this.shiftDayList));
                    break;
                case R.id.navigation_shifts:
                    replaceFragment(new ShiftsFragment(this.shiftList));
                    break;
            }

            return true;
        });

    }

    @Override
    protected void onPause() {
        this.content.printContent();
        this.content.extractAll();
        super.onPause();
    }

    @Override
    protected void onStop() {
        this.content.printContent();
        this.content.extractAll();
        super.onStop();
    }

    //Temp
    private void initializingDays(){
        Calendar tempCal;
        int shiftNum = 1;
        for(int i = 4; i<= 18; i++){
            tempCal = Calendar.getInstance();
            tempCal.set(2023, 3, i);
            this.shiftDayList.addDay(new ShiftDay(tempCal, shiftList.get(shiftNum), null));
            if(shiftNum == 4)
                shiftNum = 1;
            else
                shiftNum ++;
        }
        shiftNum = 1;
        for(int i = 1; i<= 10; i++){
            tempCal = Calendar.getInstance();
            tempCal.set(2023, 2, i);
            this.shiftDayList.addDay(new ShiftDay(tempCal, shiftList.get(shiftNum), null));
            if(shiftNum == 2)
                shiftNum = 1;
            else
                shiftNum ++;
        }
        Log.d("Debug", "ShiftDayList.size() = " + this.shiftDayList.size());
    }

    //Temp
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initializingShifts(){
        //Shift emptyShift = new Shift(this);
        Shift shift1 = new Shift("Shift 1", getResources().getColor(R.color.blue), new Time(7,0,0), new Time(15, 0, 0), 4.0, 4.4);
        Shift shift2 = new Shift("Shift 2", getResources().getColor(R.color.red), new Time(6,0,0), new Time(14, 0, 0), 0, 0);
        Shift shift3 = new Shift("Shift 3", getResources().getColor(R.color.green), new Time(15,0,0), new Time(23, 0, 0), 4.0, 4.4);
        Shift shift4 = new Shift("Shift 4", getResources().getColor(R.color.orange), new Time(14,0,0), new Time(22, 0, 0), 1, 2);

        //this.shiftList.add(emptyShift);
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