package com.example.shiftcalendar.ui.summary;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.shiftcalendar.ShiftDay;
import com.example.shiftcalendar.ui.summary.tabs.SelectorFragment;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
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
    private ArrayList<ShiftRecyclerData> shifts;

    //Cell Styles
    private XSSFCellStyle headerGreenCellStyle;
    private XSSFCellStyle headerYellowCellStyle;
    private XSSFCellStyle headerDarkGreyCellStyle;
    private XSSFCellStyle lightGrayCellStyle;
    private XSSFCellStyle centerStyle;

    //Column Max Width
    private int maxNameSize;
    private int maxDateSize;
    private int maxHoursRangeSize;
    private int maxHoursSize;

    //Sum variables
    int totalHours;
    int totalMin;

    private String possibleStartDate;
    private String possibleEndDate;

    public ExcelCreator(Activity parentActivity, String month, String year, ArrayList<ShiftDay> data){

        this.data = data;
        this.totalHours = 0;
        this.totalMin = 0;
        this.calendarWorkbook = new XSSFWorkbook();
        if(month.contains("/") && year.contains("/")){
            this.possibleStartDate = month;
            month = this.possibleStartDate.replaceAll("/", "");
            this.possibleEndDate = year;
            year = this.possibleEndDate.replaceAll("/", "");
        }
        this.createExcel(month + " " + year);
        if(this.storeExcelInStorage("SC-" + month + "-" + year)){
            Toast.makeText(parentActivity, "Save Complete!", Toast.LENGTH_SHORT).show();
        }
    }

    public ExcelCreator(Activity parentActivity, String year, ArrayList<ShiftDay> data){

        this.data = data;
        this.totalHours = 0;
        this.totalMin = 0;
        this.calendarWorkbook = new XSSFWorkbook();
        this.createExcel(year);
        if(this.storeExcelInStorage("SC-" + year)){
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
        CellRangeAddress range = new CellRangeAddress(row,row,0,3);
        this.addBorderToRange(range, sheet);
        sheet.addMergedRegion(range);
        if(this.possibleStartDate != null && this.possibleEndDate != null)
            dataDateRangeHeader.setCellValue(this.possibleStartDate + " - " + this.possibleEndDate);
        else
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
        range = new CellRangeAddress(row,row,2,3);
        this.addBorderToRange(range, sheet);
        sheet.addMergedRegion(range);
        hoursHeader.setCellValue("Hours");
        hoursHeader.setCellStyle(this.headerYellowCellStyle);
        row++;

        this.maxNameSize = 0;
        this.maxDateSize = 0;
        this.maxHoursRangeSize = 0;
        this.maxHoursSize = 0;

        //Data Row
        this.createDataRows(sheet, row);
        row += this.data.size();

        //Row Total Header
        Row totalHeaderRow = sheet.createRow(row);
        Cell totalHeader = totalHeaderRow.createCell(0);
        range = new CellRangeAddress(row,row,0,3);
        this.addBorderToRange(range, sheet);
        sheet.addMergedRegion(range);
        totalHeader.setCellValue("Total");
        totalHeader.setCellStyle(this.headerYellowCellStyle);
        row++;

        //Row Total Shift and Hours Headers
        Row shiftHoursHeaders = sheet.createRow(row);

        Cell shiftsHeader = shiftHoursHeaders.createCell(0);
        range = new CellRangeAddress(row, row, 0, 1);
        this.addBorderToRange(range, sheet);
        sheet.addMergedRegion(range);
        shiftsHeader.setCellValue("Shifts");
        shiftsHeader.setCellStyle(this.headerYellowCellStyle);

        Cell totalHoursHeader = shiftHoursHeaders.createCell(2);
        range = new CellRangeAddress(row, row, 2, 3);
        this.addBorderToRange(range, sheet);
        sheet.addMergedRegion(range);
        totalHoursHeader.setCellValue("Hours");
        totalHoursHeader.setCellStyle(this.headerYellowCellStyle);
        row++;

        this.createTotalRows(sheet, row);
    }

    private void createTotalRows(Sheet sheet, int startingRow){
        int row = startingRow;

        SelectorFragment tempSelectorFragment = new SelectorFragment();
        this.shifts = new ArrayList<>();
        try {
            this.shifts = tempSelectorFragment.getShiftsData(this.data);
            for(ShiftRecyclerData srd : this.shifts){
                Row currRow = sheet.createRow(row);

                //Shift Name
                Cell shiftNameCell = currRow.createCell(0);
                shiftNameCell.setCellValue(srd.getShift().getName());
                if(srd.getShift().getName().length() > maxNameSize) maxNameSize = srd.getShift().getName().length();
                shiftNameCell.setCellStyle(this.headerDarkGreyCellStyle);

                //Shift Counter
                Cell shiftCounterCell = currRow.createCell(1);
                shiftCounterCell.setCellValue(srd.getCount());
                shiftCounterCell.setCellStyle(this.lightGrayCellStyle);

                //Shift Hours
                Cell shiftHoursCell = currRow.createCell(2);
                CellRangeAddress range = new CellRangeAddress(row, row, 2, 3);
                this.addBorderToRange(range, sheet);
                sheet.addMergedRegion(range);
                shiftHoursCell.setCellValue(this.calculateTotalTime(srd));
                shiftHoursCell.setCellStyle(this.lightGrayCellStyle);

                row++;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Row totalRow = sheet.createRow(row);

        //Total Header
        Cell totalHeader = totalRow.createCell(0);
        totalHeader.setCellValue("Total");
        totalHeader.setCellStyle(this.headerDarkGreyCellStyle);

        //Total Shifts Count
        Cell totalShiftsCount = totalRow.createCell(1);
        totalShiftsCount.setCellFormula("SUM($B$" + (row - this.shifts.size() + 1) + ":$B$" +   row + ")");
        totalShiftsCount.setCellStyle(this.headerDarkGreyCellStyle);

        //Total Hours
        Cell totalShiftHours = totalRow.createCell(2);
        CellRangeAddress range = new CellRangeAddress(row, row, 2, 3);
        this.addBorderToRange(range, sheet);
        sheet.addMergedRegion(range);
        String totalHoursStr = String.valueOf(this.totalHours);
        if(totalHoursStr.length() == 1) totalHoursStr = "0" + totalHoursStr;
        String totalMinStr = String.valueOf(this.totalMin);
        if(totalMinStr.length() == 1) totalMinStr = "0" + totalMinStr;
        totalShiftHours.setCellValue(totalHoursStr + ":" + totalMinStr);
        totalShiftHours.setCellStyle(this.headerDarkGreyCellStyle);

    }

    private String calculateTotalTime(ShiftRecyclerData srd){

        int hours = srd.getHours() + srd.getExtraHours();
        int min = srd.getMin() + srd.getExtraMin();
        if(min > 60){
            hours += min / 60;
            min += min % 60;
        }
        String hoursStr = String.valueOf(hours);
        if(hoursStr.length() == 1) hoursStr = "0" + hoursStr;
        String minStr = String.valueOf(min);
        if(minStr.length() == 1) minStr = "0" + minStr;

        this.totalHours += hours;
        this.totalMin += min;

        return hoursStr + ":" + minStr;
    }

    private void createDataRows(Sheet sheet, int startingRow){
        int row = startingRow;

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
        sheet.setColumnWidth(0, this.calculateWidthSize(this.maxNameSize));
        sheet.setColumnWidth(1, this.calculateWidthSize(this.maxDateSize));
        sheet.setColumnWidth(2, this.calculateWidthSize(this.maxHoursRangeSize));
        sheet.setColumnWidth(3, this.calculateWidthSize(this.maxHoursSize));
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

        XSSFFont defaultFont= (XSSFFont) this.calendarWorkbook.createFont();
        defaultFont.setFontName("Calibri");
        defaultFont.setColor(IndexedColors.BLACK.getIndex());
        defaultFont.setBold(false);
        defaultFont.setItalic(false);

        this.headerGreenCellStyle = (XSSFCellStyle) this.calendarWorkbook.createCellStyle();
        XSSFColor lightGreen = new XSSFColor(this.colorToRgbTable(169,208,142));
        this.headerGreenCellStyle.setFillForegroundColor(lightGreen);
        this.headerGreenCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        this.headerGreenCellStyle.setAlignment(HorizontalAlignment.CENTER);
        this.addBoldAndItalic(this.headerGreenCellStyle, true, false);
        this.addBorderToStyle(this.headerGreenCellStyle);

        this.headerYellowCellStyle = (XSSFCellStyle) this.calendarWorkbook.createCellStyle();
        XSSFColor lightYellow = new XSSFColor(this.colorToRgbTable(255,217,102));
        this.headerYellowCellStyle.setFillForegroundColor(lightYellow);
        this.headerYellowCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        this.headerYellowCellStyle.setAlignment(HorizontalAlignment.CENTER);
        this.addBoldAndItalic(this.headerYellowCellStyle, true, true);
        this.addBorderToStyle(this.headerYellowCellStyle);

        this.headerDarkGreyCellStyle = (XSSFCellStyle) this.calendarWorkbook.createCellStyle();
        XSSFColor darkGrey = new XSSFColor(this.colorToRgbTable(128,128,128));
        this.headerDarkGreyCellStyle.setFillForegroundColor(darkGrey);
        this.headerDarkGreyCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        this.headerDarkGreyCellStyle.setAlignment(HorizontalAlignment.CENTER);
        this.addBoldAndItalic(this.headerDarkGreyCellStyle, true, false);
        this.addBorderToStyle(this.headerDarkGreyCellStyle);

        this.lightGrayCellStyle = (XSSFCellStyle) this.calendarWorkbook.createCellStyle();
        XSSFColor lightGrey = new XSSFColor(this.colorToRgbTable(166,166,166));
        this.lightGrayCellStyle.setFillForegroundColor(lightGrey);
        this.lightGrayCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        this.lightGrayCellStyle.setAlignment(HorizontalAlignment.CENTER);
        this.addBorderToStyle(this.lightGrayCellStyle);

        this.centerStyle = (XSSFCellStyle) this.calendarWorkbook.createCellStyle();
        this.centerStyle.setAlignment(HorizontalAlignment.CENTER);
        this.addBoldAndItalic(this.centerStyle, false, false);
        this.addBorderToStyle(this.centerStyle);
    }

    private void addBoldAndItalic(XSSFCellStyle style, boolean bold, boolean italic){
        XSSFFont font = (XSSFFont) this.calendarWorkbook.createFont();
        font.setFontName("Calibri");
        font.setBold(bold);
        font.setItalic(italic);
        style.setFont(font);
    }

    private void addBorderToStyle(XSSFCellStyle style){
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
    }

    private void addBorderToRange(CellRangeAddress range, Sheet sheet){
        RegionUtil.setBorderTop(BorderStyle.MEDIUM, range, sheet);
        RegionUtil.setBorderBottom(BorderStyle.MEDIUM, range, sheet);
        RegionUtil.setBorderLeft(BorderStyle.MEDIUM, range, sheet);
        RegionUtil.setBorderRight(BorderStyle.MEDIUM, range, sheet);
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
