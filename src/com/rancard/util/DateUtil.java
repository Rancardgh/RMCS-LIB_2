/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author Ahmed Mustapha
 * Utility class for converting Date to String formats
 */
public class DateUtil {
    public static final DateFormat MYSQLTIMESTAMPFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public static String convertToMySQLTimeStamp(java.util.Date date){               
        return MYSQLTIMESTAMPFORMAT.format(date);
    }
    
    public static java.util.Date convertFromMySQLTimeStamp(String timeStamp) throws ParseException{
        return MYSQLTIMESTAMPFORMAT.parse(timeStamp);
    }
    
    public static java.util.Date addDaysToDate(java.util.Date date, int noOfDays) {        
        Calendar calender = new GregorianCalendar();
        calender.add(calender.DATE, noOfDays);
        return calender.getTime();        
    }
    
    public static int daysBtnDates(java.util.Date startDate, java.util.Date endDate){
        return (int) ((endDate.getTime() - startDate.getTime())/(1000*60*60*24));
    }
}
