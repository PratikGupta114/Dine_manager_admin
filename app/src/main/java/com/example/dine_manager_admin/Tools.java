package com.example.dine_manager_admin;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Tools {

    public static boolean deviceIsOnline(Activity activity) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        if(activeNetwork != null && activeNetwork.isConnected())
            return true;

        return false;
    }
    public static String getCurrentTime() {
        String timeNow;
        Date date = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30")).getTime();

        SimpleDateFormat timeFomat = new SimpleDateFormat("hh:mm:ss a");
        timeNow = timeFomat.format(date);
        return timeNow;
    }
    public static String getCurrentDate() {
        String dateToday ;
        Date date  = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30")).getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateToday = dateFormat.format(date);

        return dateToday;
    }

    public static int getCurrentDayOfMonth() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        return dayOfMonth;
    }
    public static int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
        int month = calendar.get(Calendar.MONTH);
        return month+1;
    }
    public static int getCurrentYear() {
        Calendar calendar1 = Calendar.getInstance();
        int year = calendar1.get(Calendar.YEAR);

        return year;
    }

    public static String formatDatetoDDMMYYYY(String date) {
        String[] blocks = date.trim().split("-");
        String newDate = blocks[2]+"-"+blocks[1]+"-"+blocks[0];
        return newDate;
    }

    public static String getCurrentFormattedYear() {
        return getCurrentDate().trim().split("-")[0];
    }
    public static String getCurrentFormattedMonth() {
        return getCurrentDate().trim().split("-")[1];
    }
    public static String getFormattedDateFromNow(int field, int amount) {
        Calendar otherDate = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
        otherDate.add(field, amount);
        Date newDate = otherDate.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return dateFormat.format(newDate);
    }

    public static String[] getStartAndEndDateOfWeek() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DAY_OF_MONTH, -(day-1));

        String[] startAndEndDates = new String[2];
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        startAndEndDates[0] = dateFormat.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 6);
        startAndEndDates[1] = dateFormat.format(calendar.getTime());

        return startAndEndDates;
    }
    public static String[] getCurrentAndLastDateOfMonth() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
        String[] currentAndEndDates = new String[2];
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        currentAndEndDates[0] = dateFormat.format(calendar.getTime());
        String[] blocks = currentAndEndDates[0].trim().split("-");
        int lastDayOfMonth = calendar.getActualMaximum(Calendar.DATE);
        currentAndEndDates[1] = blocks[0]+"-"+blocks[1]+"-"+lastDayOfMonth;
        return currentAndEndDates;
    }
}
