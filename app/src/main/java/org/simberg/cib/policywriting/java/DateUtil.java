package org.simberg.cib.policywriting.java;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by javadbadirkhanly on 11/15/17.
 */

public class DateUtil {

    public static String CurrentDate(){

        Date date = Calendar.getInstance().getTime();
        String format = "dd.MM.yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);
        return simpleDateFormat.format(date);
    }

    public static String CurrentDateTime(){
        Date date = Calendar.getInstance().getTime();
        String format = "dd.MM.yyyy HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);
        return simpleDateFormat.format(date);
    }

    public static String DateFormatter(String dateTime) {
        String dateTimeFormat = "yyyy-MM-dd hh:mm:ss";
        String format = "dd.MM.yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateTimeFormat, Locale.US);

        try {
            Date date = simpleDateFormat.parse(dateTime);
            simpleDateFormat = new SimpleDateFormat(format, Locale.US);
            return simpleDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateTime;
    }

    public static String DateTimeFormatter(String dateTime){
        String dateTimeFormat = "yyyy-MM-dd hh:mm:ss";
        String format = "dd.MM.yyyy hh:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateTimeFormat, Locale.US);

        try {
            Date date = simpleDateFormat.parse(dateTime);
            simpleDateFormat = new SimpleDateFormat(format, Locale.US);
            return simpleDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateTime;
    }

    public static String ApiDateFormatter(String dateTime){
        String dateTimeFormat = "dd.MM.yyyy";
        String format = "yyyy-MM-dd hh:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateTimeFormat, Locale.US);

        try {
            Date date = simpleDateFormat.parse(dateTime);
            simpleDateFormat = new SimpleDateFormat(format, Locale.US);
            return simpleDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateTime;
    }

    public static String ApiDateTimeFormatter(String dateTime){
        String dateTimeFormat = "dd.MM.yyyy hh:mm:ss";
        String format = "yyyy-MM-dd hh:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateTimeFormat, Locale.US);

        try {
            Date date = simpleDateFormat.parse(dateTime);
            simpleDateFormat = new SimpleDateFormat(format, Locale.US);
            return simpleDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateTime;
    }

    public static String ConvertDate(int year, int month, int day){
        return day + "." + month + "." + year;
    }
}
