/*  1:   */ package com.rancard.util;
/*  2:   */ 
/*  3:   */ import java.text.DateFormat;
/*  4:   */ import java.text.ParseException;
/*  5:   */ import java.text.SimpleDateFormat;
/*  6:   */ import java.util.Calendar;
/*  7:   */ import java.util.Date;
/*  8:   */ import java.util.GregorianCalendar;
/*  9:   */ 
/* 10:   */ public class DateUtil
/* 11:   */ {
/* 12:19 */   public static final DateFormat MYSQLTIMESTAMPFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/* 13:20 */   public static final DateFormat MYSQLSHORTFORMAT = new SimpleDateFormat("yyyy-MM-dd");
/* 14:   */   
/* 15:   */   public static String convertToMySQLTimeStamp(Date date)
/* 16:   */   {
/* 17:23 */     return MYSQLTIMESTAMPFORMAT.format(date);
/* 18:   */   }
/* 19:   */   
/* 20:   */   public static Date convertFromMySQLTimeStamp(String timeStamp)
/* 21:   */     throws ParseException
/* 22:   */   {
/* 23:27 */     return MYSQLTIMESTAMPFORMAT.parse(timeStamp);
/* 24:   */   }
/* 25:   */   
/* 26:   */   public static String convertToMySQLShort(Date date)
/* 27:   */   {
/* 28:31 */     return MYSQLSHORTFORMAT.format(date);
/* 29:   */   }
/* 30:   */   
/* 31:   */   public static Date convertFromMySQLShort(String shortDate)
/* 32:   */     throws ParseException
/* 33:   */   {
/* 34:35 */     return MYSQLSHORTFORMAT.parse(shortDate);
/* 35:   */   }
/* 36:   */   
/* 37:   */   public static Date addDaysToDate(Date date, int noOfDays)
/* 38:   */   {
/* 39:39 */     Calendar calender = new GregorianCalendar();
/* 40:40 */     calender.add(5, noOfDays);
/* 41:41 */     return calender.getTime();
/* 42:   */   }
/* 43:   */   
/* 44:   */   public static int daysBtnDates(Date startDate, Date endDate)
/* 45:   */   {
/* 46:45 */     return (int)((endDate.getTime() - startDate.getTime()) / 86400000L);
/* 47:   */   }
/* 48:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.util.DateUtil
 * JD-Core Version:    0.7.0.1
 */