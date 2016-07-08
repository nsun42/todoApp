package com.nsundareswaran.todoapp;

import java.util.Calendar;

/**
 * Created by nsundareswaran on 7/7/16.
 */
public class Task {

    public String name;
    public String date;
    private Calendar calendar = Calendar.getInstance();

    public Task(String tname) {
        name = tname;
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        date = String.format("%04d", year)+"-"+String.format("%02d", month)+ "-"+String.format("%02d", day);
    }
    public Task(String tname, int year, int month, int day) {
        name = tname;
        date = String.format("%04d", year)+"-"+String.format("%02d", month)+ "-"+String.format("%02d", day);
    }

    public Task(String tname, String tdate) {
        name = tname;
        date = tdate;
    }

    int getYear() {
        String timeUnits[] = date.split("-");
        return Integer.parseInt(timeUnits[0]);
    }

    int getMonth() {
        String timeUnits[] = date.split("-");
        return Integer.parseInt(timeUnits[1]);
    }

    int getDay() {
        String timeUnits[] = date.split("-");
        return Integer.parseInt(timeUnits[2]);
    }

    void updateDate(int year, int month, int day) {
        date = String.format("%04d", year)+"-"+String.format("%02d", month)+ "-"+String.format("%02d", day);
    }

    static String normalizeDate(String javaDataFormat) {
        String timeUnits[] = javaDataFormat.split("-");
        int year = Integer.parseInt(timeUnits[0]);
        int month = Integer.parseInt(timeUnits[1]);
        int day = Integer.parseInt(timeUnits[2]);
        month = month+1;

        String date = String.format("%04d", year)+"-"+String.format("%02d", month)+ "-"+String.format("%02d", day);
        return date;
    }
}
