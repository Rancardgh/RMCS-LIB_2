package com.rancard.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Mustee on 2/9/14.
 */
public class DateUtil {
    public static final DateFormat TIMESTAMPFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final DateFormat SHORTFORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static String formatToTimeStamp(Date date){
        if(date == null){
            throw new IllegalArgumentException("Date is null.");
        }

        return TIMESTAMPFORMAT.format(date);
    }

    public static String formatToShort(Date date){
        if(date == null){
            throw new IllegalArgumentException("Date is null.");
        }

        return SHORTFORMAT.format(date);
    }

    public static Date convertFromTimeStampFormat(String timeStamp) throws ParseException {
        if(timeStamp == null || timeStamp.equals("")){
            return null;
        }

        return TIMESTAMPFORMAT.parse(timeStamp);
    }

    public static Date convertFromShortFormat(String shortDate) throws ParseException{
        if(shortDate == null || shortDate.equals("")){
            return null;
        }

        return SHORTFORMAT.parse(shortDate);
    }

    public static Date addDaysToDate(Date date, int noOfDays) {
        Calendar calender = new GregorianCalendar();
        calender.setTime(date);
        calender.add(Calendar.DATE, noOfDays);
        return calender.getTime();
    }
}
