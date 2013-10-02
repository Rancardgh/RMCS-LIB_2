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
    public static final DateFormat MYSQLSHORTFORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    public static String convertToMySQLTimeStamp(java.util.Date date){    
        if(date == null){
            return null;
        }
        
        return MYSQLTIMESTAMPFORMAT.format(date);
    }
    
    public static java.util.Date convertFromMySQLTimeStamp(String timeStamp) throws ParseException{
        if(timeStamp == null || timeStamp.equals("")){
            return null;
        }
        
        return MYSQLTIMESTAMPFORMAT.parse(timeStamp);
    }
    
    public static String convertToMySQLShort(java.util.Date date){  
        if(date == null){
            return null;
        }
        
        return MYSQLSHORTFORMAT.format(date);
    }
    
    public static java.util.Date convertFromMySQLShort(String shortDate) throws ParseException{
        if(shortDate == null || shortDate.equals("")){
            return null;
        }
        
        return MYSQLSHORTFORMAT.parse(shortDate);
    }
    
    public static java.util.Date addDaysToDate(java.util.Date date, int noOfDays) {        
        Calendar calender = new GregorianCalendar();
        calender.add(Calendar.DATE, noOfDays);
        return calender.getTime();        
    }
    
    public static int daysBtnDates(java.util.Date startDate, java.util.Date endDate){
        return (int) ((endDate.getTime() - startDate.getTime())/(1000*60*60*24));
    }
}
