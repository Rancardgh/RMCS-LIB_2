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
/* 13:   */   
/* 14:   */   public static String convertToMySQLTimeStamp(Date date)
/* 15:   */   {
/* 16:22 */     return MYSQLTIMESTAMPFORMAT.format(date);
/* 17:   */   }
/* 18:   */   
/* 19:   */   public static Date convertFromMySQLTimeStamp(String timeStamp)
/* 20:   */     throws ParseException
/* 21:   */   {
/* 22:26 */     return MYSQLTIMESTAMPFORMAT.parse(timeStamp);
/* 23:   */   }
/* 24:   */   
/* 25:   */   public static Date addDaysToDate(Date date, int noOfDays)
/* 26:   */   {
/* 27:30 */     Calendar calender = new GregorianCalendar();
/* 28:31 */     calender.add(5, noOfDays);
/* 29:32 */     return calender.getTime();
/* 30:   */   }
/* 31:   */   
/* 32:   */   public static int daysBtnDates(Date startDate, Date endDate)
/* 33:   */   {
/* 34:36 */     return (int)((endDate.getTime() - startDate.getTime()) / 86400000L);
/* 35:   */   }
/* 36:   */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.util.DateUtil
 * JD-Core Version:    0.7.0.1
 */