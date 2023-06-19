package com.example.shiftcalendar;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Content implements Serializable {

    private ShiftDayList shiftDayList;
    private ArrayList<Shift> shiftList;

    private String rootPath = Environment.getExternalStorageDirectory() + "/Shift Calendar";

    public Content(Shift emptyShift){
        File file = new File(this.rootPath + "/data");
        if(!file.exists()) {
            file.mkdirs();
        }
        this.loadLists(emptyShift);
    }

    public void extractAll(){
        //this.extractShiftList();
        //this.extractShiftDayList();
        this.extractData(this, "personalCalendarData.dat");
    }

    public void extractShiftDayList(){
        this.extractData(this.shiftDayList, "shiftDayList.dat");
    }

    public void extractShiftList(){
        this.extractData(this.shiftList, "shifts.dat");
        Log.d("Content", this.shiftList.size() + " extracting size");
    }

    public void loadLists(Shift emptyShift) {
        Content currContent = (Content) this.loadData("personalCalendarData.dat");
        if(currContent == null){
            this.shiftDayList = new ShiftDayList();
            this.shiftList = new ArrayList<>();
        }
        else {
            this.shiftDayList = currContent.getShiftDayList();
            this.shiftList = currContent.getShiftList();
        }
        /*this.shiftDayList = (ShiftDayList) this.loadData("shiftDayList.dat");
        if(this.shiftDayList == null)
            this.shiftDayList = new ShiftDayList();
        this.shiftList = (ArrayList<Shift>) this.loadData("shifts.dat");
        if(this.shiftList == null)
            this.shiftList = new ArrayList<>();*/
        if(this.shiftList.size() == 0)
            this.shiftList.add(emptyShift);

    }

    private void extractData(Object obj, String fileName) {
        File file = new File(this.rootPath + "/data/" + fileName);
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(obj);
            oos.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object loadData(String fileName) {
        File file = new File(this.rootPath + "/data/" + fileName);
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return ois.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    public ShiftDayList getShiftDayList() {
        return shiftDayList;
    }

    public ArrayList<Shift> getShiftList() {
        return shiftList;
    }

    public void printContent(){
        Log.d("Content", "Shifts: " + this.shiftList.size());
        for(Shift s : this.shiftList)
            Log.d("Content", s.getName());

        Log.d("Content", "Shift Days: " + this.shiftDayList.size());
        this.shiftDayList.print();
    }

}
