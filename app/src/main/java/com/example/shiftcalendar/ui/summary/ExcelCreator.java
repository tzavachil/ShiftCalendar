package com.example.shiftcalendar.ui.summary;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.shiftcalendar.ShiftDay;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ExcelCreator {

    private Workbook calendarWorkbook;
    private ArrayList<ShiftDay> data;

    //Cell Styles
    private XSSFCellStyle headerGreenCellStyle;
    private XSSFCellStyle headerYellowCellStyle;
    private XSSFCellStyle headerDarkGreyCellStyle;
    private XSSFCellStyle lightGrayCellStyle;
    private XSSFCellStyle centerStyle;

    public ExcelCreator(Activity parentActivity, String month, String year, ArrayList<ShiftDay> data){

        this.data = data;
        Log.d("Debug", "Data Size: " + String.valueOf(data.size()));
        this.calendarWorkbook = new XSSFWorkbook();
        this.createExcel(month + " " + year);
        if(this.storeExcelInStorage("SC-" + month + "-" + year)){
            Toast.makeText(parentActivity, "Save Complete!", Toast.LENGTH_SHORT).show();
        }
    }

    private void createExcel(String sheetName){

        Sheet sheet = calendarWorkbook.createSheet(sheetName);
        this.createCellStyles();

        int row = 0;
        //Row 0 = Data Date Range Header
        Row firstRow = sheet.createRow(row);
        Cell dataDateRangeHeader = firstRow.createCell(0);
        sheet.addMergedRegion(new CellRangeAddress(row,row,0,3));
        dataDateRangeHeader.setCellValue(sheetName);
        dataDateRangeHeader.setCellStyle(this.headerGreenCellStyle);
        row++;

        //Row 1 = Columns Header
        Row columnHeaderRow = sheet.createRow(row);
        Cell shiftHeader = columnHeaderRow.createCell(0);
        shiftHeader.setCellValue("Shift");
        shiftHeader.setCellStyle(this.headerYellowCellStyle);

        Cell dateHeader = columnHeaderRow.createCell(1);
        dateHeader.setCellValue("Date");
        dateHeader.setCellStyle(this.headerYellowCellStyle);

        Cell hoursHeader = columnHeaderRow.createCell(2);
        sheet.addMergedRegion(new CellRangeAddress(row,row,2,3));
        hoursHeader.setCellValue("Hours");
        hoursHeader.setCellStyle(this.headerYellowCellStyle);
        row++;

        //Data Row
        this.createDataRows(sheet, row);
        row += this.data.size();

        //Row Total Header
        Row totalHeaderRow = sheet.createRow(row);
        Cell totalHeader = totalHeaderRow.createCell(0);
        sheet.addMergedRegion(new CellRangeAddress(row,row,0,3));
        totalHeader.setCellValue("Total");
        totalHeader.setCellStyle(this.headerYellowCellStyle);
        row++;

        //Row Total Shift and Hours Headers
        Row shiftHoursHeaders = sheet.createRow(row);

        Cell shiftsHeader = shiftHoursHeaders.createCell(0);
        sheet.addMergedRegion(new CellRangeAddress(row, row, 0, 1));
        shiftsHeader.setCellValue("Shifts");
        shiftsHeader.setCellStyle(this.headerYellowCellStyle);

        Cell totalHoursHeader = shiftHoursHeaders.createCell(2);
        sheet.addMergedRegion(new CellRangeAddress(row, row, 2, 3));
        totalHoursHeader.setCellValue("Hours");
        totalHoursHeader.setCellStyle(this.headerYellowCellStyle);
        row++;
    }

    private void createDataRows(Sheet sheet, int startingRow){
        int row = startingRow;

        int maxNameSize = 0;
        int maxDateSize = 0;
        int maxHoursRangeSize = 0;
        int maxHoursSize = 0;

        for(ShiftDay sd : this.data){
            Row currRow = sheet.createRow(row);

            //Shift Name
            Cell shiftNameCell = currRow.createCell(0);
            shiftNameCell.setCellValue(sd.getShift().getName());
            if(sd.getShift().getName().length() > maxNameSize) maxNameSize = sd.getShift().getName().length();
            shiftNameCell.setCellStyle(this.centerStyle);

            //Shift Date
            Cell shiftDate = currRow.createCell(1);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            shiftDate.setCellValue(simpleDateFormat.format(sd.getCalendar().getTime()));
            if(simpleDateFormat.format(sd.getCalendar().getTime()).length() > maxDateSize) maxDateSize = simpleDateFormat.format(sd.getCalendar().getTime()).length();
            shiftDate.setCellStyle(this.centerStyle);

            //Shift Hours Range
            Cell shiftHoursRange = currRow.createCell(2);
            shiftHoursRange.setCellValue(ShiftDayRecyclerData.calculateTime(sd));
            if(ShiftDayRecyclerData.calculateTime(sd).length() > maxHoursRangeSize) maxHoursRangeSize = ShiftDayRecyclerData.calculateTime(sd).length();
            shiftHoursRange.setCellStyle(this.centerStyle);

            //Shift Hours
            Cell shiftHours = currRow.createCell(3);
            shiftHours.setCellValue(this.calculateHours(sd));
            if(String.valueOf(this.calculateHours(sd)).length() > maxHoursSize) maxHoursSize = String.valueOf(this.calculateHours(sd)).length();
            shiftHours.setCellStyle(this.centerStyle);

            row++;
        }

        //Auto Resize Columns
        sheet.setColumnWidth(0, this.calculateWidthSize(maxNameSize));
        sheet.setColumnWidth(1, this.calculateWidthSize(maxDateSize));
        sheet.setColumnWidth(2, this.calculateWidthSize(maxHoursRangeSize));
        sheet.setColumnWidth(3, this.calculateWidthSize(maxHoursSize));
    }

    private int calculateWidthSize(int maxSize){
        return maxSize * 256 + 256;
    }

    private String calculateHours(ShiftDay sd){

        String hoursStr = "";

        try {
            hoursStr = ShiftDayRecyclerData.calculateHours(sd); //Current Format XX h XX m
            hoursStr = hoursStr.replaceAll(" |m", ""); //Current Format XXhXX
            hoursStr = hoursStr.replace("h", ":"); //Current Format XX:XX
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return hoursStr;
    }

    private void createCellStyles(){
        this.headerGreenCellStyle = (XSSFCellStyle) this.calendarWorkbook.createCellStyle();
        XSSFColor lightGreen = new XSSFColor(this.colorToRgbTable(169,208,142));
        this.headerGreenCellStyle.setFillForegroundColor(lightGreen);
        this.headerGreenCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        this.headerGreenCellStyle.setAlignment(HorizontalAlignment.CENTER);

        this.headerYellowCellStyle = (XSSFCellStyle) this.calendarWorkbook.createCellStyle();
        XSSFColor lightYellow = new XSSFColor(this.colorToRgbTable(255,217,102));
        this.headerYellowCellStyle.setFillForegroundColor(lightYellow);
        this.headerYellowCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        this.headerYellowCellStyle.setAlignment(HorizontalAlignment.CENTER);

        this.headerDarkGreyCellStyle = (XSSFCellStyle) this.calendarWorkbook.createCellStyle();
        XSSFColor darkGrey = new XSSFColor(this.colorToRgbTable(128,128,128));
        this.headerDarkGreyCellStyle.setFillForegroundColor(darkGrey);
        this.headerDarkGreyCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        this.headerDarkGreyCellStyle.setAlignment(HorizontalAlignment.CENTER);

        this.lightGrayCellStyle = (XSSFCellStyle) this.calendarWorkbook.createCellStyle();
        XSSFColor lightGrey = new XSSFColor(this.colorToRgbTable(166,166,166));
        this.lightGrayCellStyle.setFillForegroundColor(lightGrey);
        this.lightGrayCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        this.lightGrayCellStyle.setAlignment(HorizontalAlignment.CENTER);

        this.centerStyle = (XSSFCellStyle) this.calendarWorkbook.createCellStyle();
        this.centerStyle.setAlignment(HorizontalAlignment.CENTER);
    }

    private byte[] colorToRgbTable(int r, int g, int b){
        byte[] rgb = new byte[3];
        rgb[0] = (byte) r;
        rgb[1] = (byte) g;
        rgb[2] = (byte) b;

        return rgb;
    }

    private boolean storeExcelInStorage(String fileName){
        boolean isSuccess;
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/Shift Calendar/Exports/", fileName + ".xlsx");
        FileOutputStream fileOutputStream = null;

        try{
            fileOutputStream = new FileOutputStream(file);
            this.calendarWorkbook.write(fileOutputStream);
            isSuccess = true;
        } catch (FileNotFoundException e) {
            isSuccess = false;
        } catch (IOException e) {
            isSuccess = false;
        } finally {
            try {
                if(null != fileOutputStream) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return isSuccess;
    }

}
