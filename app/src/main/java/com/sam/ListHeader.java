package com.sam;

import java.util.ArrayList;

/**
 * Created by MOHD IMTIAZ on 20-Mar-17.
 */

public class ListHeader {
    String monthName;
    String totalPresentDays;
    String presentPercent;
    ArrayList<ListChild> dayAttendanceData = new ArrayList<>();

    public ListHeader(String monthName, String totalPresentDays, String presentPercent) {
        this.monthName = monthName;
        this.presentPercent = presentPercent;
        this.totalPresentDays = totalPresentDays;
    }

    public String getMonthName() {
        return monthName;
    }

    public String getPresentPercent() {
        return presentPercent;
    }

    public String getTotalPresentDays() {
        return totalPresentDays;
    }

    public ArrayList<ListChild> getDayAttendanceData() {
        return dayAttendanceData;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public void setPresentPercent(String presentPercent) {
        this.presentPercent = presentPercent;
    }

    public void setTotalPresentDays(String totalPresentDays) {
        this.totalPresentDays = totalPresentDays;
    }

    public void setDayAttendanceData(ArrayList<ListChild> dayAttendanceData) {
        this.dayAttendanceData = dayAttendanceData;
    }
}
