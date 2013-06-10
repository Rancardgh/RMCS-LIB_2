package com.rancard.util;

import java.text.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import com.rancard.mobility.contentserver.CPConnections;

public class Date {
    
    static int[] styles = {
        DateFormat.DEFAULT,
        DateFormat.SHORT,
        DateFormat.MEDIUM,
        DateFormat.LONG,
        DateFormat.FULL
    };
    
    public Date() {
    }
    
    java.util.Calendar cal = java.util.Calendar.getInstance();
    int day = cal.get(java.util.Calendar.DATE);
    int month = cal.get(java.util.Calendar.MONTH);
    int yr = cal.get(java.util.Calendar.YEAR);
    int status = 0;
    int i = 1;
    int j = 1;
    
    /**
     * validateDate
     *
     * @param dateString String
     * @param dateFormat String
     */
    public java.util.Date validateDate(String dateString, String dateFormat) {
        
        
        String months[] = {
            "", "January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"};
        SimpleDateFormat s_formatter = new SimpleDateFormat(dateFormat);
        java.util.Date validDate = null;
        try {
            validDate = s_formatter.parse(dateString);
            
        } catch (Exception ex) {
        }
        
        return validDate;
    }
    
    public static String formatDate(java.util.Date dateObj, String dateFormat) {
        
        String formatedDate = null;
        java.text.SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            formatedDate = sdf.format(dateObj);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return formatedDate;
    }
    
    public static String formatDate(long date, String dateFormat) {
        java.util.Date dateObj = new java.util.Date(date);
        String formatedDate = null;
        java.text.SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            formatedDate = sdf.format(dateObj);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return formatedDate;
    }
    
    public static String formatDate(java.sql.Date date, String dateFormat) {
        java.util.Date dateObj = new java.util.Date(date.getTime());
        String formatedDate = null;
        java.text.SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            formatedDate = sdf.format(dateObj);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return formatedDate;
    }
    
    /**
     * addDate
     */
    public java.util.Date addDate(java.util.Date dateObj,int noOfDays) {
        
        Calendar cali = new GregorianCalendar();
        cali.add(cali.DATE,noOfDays);
        return cali.getTime();
        
    }
    
    public java.util.Date subtractDate(java.util.Date dateObj,int noOfDays) {
        
        Calendar cali = new GregorianCalendar();
        cali.setTime(dateObj);
        cali.add(cali.DATE,-noOfDays);
        return cali.getTime();
        
    }
    
    public static boolean validateDate(String dateStr,
            boolean allowPast,
            String formatStr) {
        if (formatStr == null) return false; // or throw some kinda exception, possibly a InvalidArgumentException
        java.text.SimpleDateFormat df = new SimpleDateFormat(formatStr);
        java.util.Date testDate = null;
        try {
            testDate = df.parse(dateStr);
        } catch (ParseException e) {
            // invalid date format
            return false;
        }
        if (!allowPast) {
            // initialise the calendar to midnight to prevent
            // the current day from being rejected
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            if (cal.getTime().after(testDate)) return false;
        }
        // now test for legal values of parameters
        if (!df.format(testDate).equals(dateStr)) return false;
        return true;
    }
    
    public static String toLocalTime(java.util.Date time, CPConnections cnxn){
        
        //logging statements
        System.out.println(new java.util.Date()+":inside com.rancard.util:toLocalTime()...");
        System.out.println(new java.util.Date()+":converting UTC_time:"+time.toString() +",country:"+cnxn.getCountryAlias());
        
        String dateOut = "";
        Locale currentLocale = null;
        DateFormat dateFormatter =null;
        
        try{
            
            //convert to network's local time
            Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTimeInMillis(time.getTime());
            calendar.add(Calendar.HOUR_OF_DAY, cnxn.getUTCOffset());
            
            //format time to appropriate display
            currentLocale = new Locale(cnxn.getLanguage(), cnxn.getCountryAlias()); //set network's locale
            //logging..
            System.out.println(new java.util.Date()+":locale:"+currentLocale.toString());
            //do formatiing
            dateFormatter = DateFormat.getDateTimeInstance(styles[0], styles[0], currentLocale);
            dateOut = dateFormatter.format(calendar.getTime());
            
            System.out.println(new java.util.Date()+":output:"+dateOut);
        }catch(Exception ex){
            System.out.println(new java.util.Date()+":error converting UTC_time toLocalTime:");
            ex.printStackTrace();
        }
        
        return dateOut;
    }
    // create post date
    
  /*
     s_formatter = new SimpleDateFormat ("dd-MMMM-yyyy");
     article_post_date = s_formatter.format(dt_article_post_date);
   
   
    calend = new GregorianCalendar();
   
    calend.setTime(dt_article_post_date);
    postdate_day = calend.get(Calendar.DATE);
    postdate_month = calend.get(Calendar.MONTH) + 1;
     postdate_year = calend.get(Calendar.YEAR);
   
   */
    public int getDay() {
        return day;
    }
    
    public int getMonth() {
        return month;
    }
    
    public int getYr() {
        return yr;
    }
    
    public void setDay(int day) {
        this.day = day;
    }
    
    public void setMonth(int month) {
        this.month = month;
    }
    
    public void setYr(int yr) {
        this.yr = yr;
    }
}
