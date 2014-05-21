/*   1:    */ package com.rancard.util;
/*   2:    */ 
/*   3:    */ import com.rancard.mobility.contentserver.CPConnections;
/*   4:    */ import java.io.PrintStream;
/*   5:    */ import java.text.DateFormat;
/*   6:    */ import java.text.ParseException;
/*   7:    */ import java.text.SimpleDateFormat;
/*   8:    */ import java.util.Calendar;
/*   9:    */ import java.util.GregorianCalendar;
/*  10:    */ import java.util.Locale;
/*  11:    */ 
/*  12:    */ public class Date
/*  13:    */ {
/*  14: 11 */   static int[] styles = { 2, 3, 2, 1, 0 };
/*  15: 22 */   Calendar cal = Calendar.getInstance();
/*  16: 23 */   int day = this.cal.get(5);
/*  17: 24 */   int month = this.cal.get(2);
/*  18: 25 */   int yr = this.cal.get(1);
/*  19: 26 */   int status = 0;
/*  20: 27 */   int i = 1;
/*  21: 28 */   int j = 1;
/*  22:    */   
/*  23:    */   public java.util.Date validateDate(String dateString, String dateFormat)
/*  24:    */   {
/*  25: 39 */     String[] months = { "", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
/*  26:    */     
/*  27:    */ 
/*  28: 42 */     SimpleDateFormat s_formatter = new SimpleDateFormat(dateFormat);
/*  29: 43 */     java.util.Date validDate = null;
/*  30:    */     try
/*  31:    */     {
/*  32: 45 */       validDate = s_formatter.parse(dateString);
/*  33:    */     }
/*  34:    */     catch (Exception ex) {}
/*  35: 50 */     return validDate;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public static String formatDate(java.util.Date dateObj, String dateFormat)
/*  39:    */   {
/*  40: 55 */     String formatedDate = null;
/*  41: 56 */     SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
/*  42:    */     try
/*  43:    */     {
/*  44: 58 */       formatedDate = sdf.format(dateObj);
/*  45:    */     }
/*  46:    */     catch (Exception ex)
/*  47:    */     {
/*  48: 60 */       ex.printStackTrace();
/*  49:    */     }
/*  50: 62 */     return formatedDate;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public static String formatDate(long date, String dateFormat)
/*  54:    */   {
/*  55: 66 */     java.util.Date dateObj = new java.util.Date(date);
/*  56: 67 */     String formatedDate = null;
/*  57: 68 */     SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
/*  58:    */     try
/*  59:    */     {
/*  60: 70 */       formatedDate = sdf.format(dateObj);
/*  61:    */     }
/*  62:    */     catch (Exception ex)
/*  63:    */     {
/*  64: 72 */       ex.printStackTrace();
/*  65:    */     }
/*  66: 74 */     return formatedDate;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public static String formatDate(java.sql.Date date, String dateFormat)
/*  70:    */   {
/*  71: 78 */     java.util.Date dateObj = new java.util.Date(date.getTime());
/*  72: 79 */     String formatedDate = null;
/*  73: 80 */     SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
/*  74:    */     try
/*  75:    */     {
/*  76: 82 */       formatedDate = sdf.format(dateObj);
/*  77:    */     }
/*  78:    */     catch (Exception ex)
/*  79:    */     {
/*  80: 84 */       ex.printStackTrace();
/*  81:    */     }
/*  82: 86 */     return formatedDate;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public java.util.Date addDate(java.util.Date dateObj, int noOfDays)
/*  86:    */   {
/*  87: 94 */     Calendar cali = new GregorianCalendar();
/*  88: 95 */     cali.add(5, noOfDays);
/*  89: 96 */     return cali.getTime();
/*  90:    */   }
/*  91:    */   
/*  92:    */   public java.util.Date subtractDate(java.util.Date dateObj, int noOfDays)
/*  93:    */   {
/*  94:102 */     Calendar cali = new GregorianCalendar();
/*  95:103 */     cali.setTime(dateObj);
/*  96:104 */     cali.add(5, -noOfDays);
/*  97:105 */     return cali.getTime();
/*  98:    */   }
/*  99:    */   
/* 100:    */   public static boolean validateDate(String dateStr, boolean allowPast, String formatStr)
/* 101:    */   {
/* 102:112 */     if (formatStr == null) {
/* 103:112 */       return false;
/* 104:    */     }
/* 105:113 */     SimpleDateFormat df = new SimpleDateFormat(formatStr);
/* 106:114 */     java.util.Date testDate = null;
/* 107:    */     try
/* 108:    */     {
/* 109:116 */       testDate = df.parse(dateStr);
/* 110:    */     }
/* 111:    */     catch (ParseException e)
/* 112:    */     {
/* 113:119 */       return false;
/* 114:    */     }
/* 115:121 */     if (!allowPast)
/* 116:    */     {
/* 117:124 */       Calendar cal = Calendar.getInstance();
/* 118:125 */       cal.set(11, 0);
/* 119:126 */       cal.set(12, 0);
/* 120:127 */       cal.set(13, 0);
/* 121:128 */       cal.set(14, 0);
/* 122:129 */       if (cal.getTime().after(testDate)) {
/* 123:129 */         return false;
/* 124:    */       }
/* 125:    */     }
/* 126:132 */     if (!df.format(testDate).equals(dateStr)) {
/* 127:132 */       return false;
/* 128:    */     }
/* 129:133 */     return true;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public static String toLocalTime(java.util.Date time, CPConnections cnxn)
/* 133:    */   {
/* 134:139 */     System.out.println(new java.util.Date() + ":inside com.rancard.util:toLocalTime()...");
/* 135:140 */     System.out.println(new java.util.Date() + ":converting UTC_time:" + time.toString() + ",country:" + cnxn.getCountryAlias());
/* 136:    */     
/* 137:142 */     String dateOut = "";
/* 138:143 */     Locale currentLocale = null;
/* 139:144 */     DateFormat dateFormatter = null;
/* 140:    */     try
/* 141:    */     {
/* 142:149 */       Calendar calendar = Calendar.getInstance();
/* 143:150 */       calendar.setTimeInMillis(time.getTime());
/* 144:151 */       calendar.add(11, cnxn.getUTCOffset());
/* 145:    */       
/* 146:    */ 
/* 147:154 */       currentLocale = new Locale(cnxn.getLanguage(), cnxn.getCountryAlias());
/* 148:    */       
/* 149:156 */       System.out.println(new java.util.Date() + ":locale:" + currentLocale.toString());
/* 150:    */       
/* 151:158 */       dateFormatter = DateFormat.getDateTimeInstance(styles[0], styles[0], currentLocale);
/* 152:159 */       dateOut = dateFormatter.format(calendar.getTime());
/* 153:    */       
/* 154:161 */       System.out.println(new java.util.Date() + ":output:" + dateOut);
/* 155:    */     }
/* 156:    */     catch (Exception ex)
/* 157:    */     {
/* 158:163 */       System.out.println(new java.util.Date() + ":error converting UTC_time toLocalTime:");
/* 159:164 */       ex.printStackTrace();
/* 160:    */     }
/* 161:167 */     return dateOut;
/* 162:    */   }
/* 163:    */   
/* 164:    */   public int getDay()
/* 165:    */   {
/* 166:185 */     return this.day;
/* 167:    */   }
/* 168:    */   
/* 169:    */   public int getMonth()
/* 170:    */   {
/* 171:189 */     return this.month;
/* 172:    */   }
/* 173:    */   
/* 174:    */   public int getYr()
/* 175:    */   {
/* 176:193 */     return this.yr;
/* 177:    */   }
/* 178:    */   
/* 179:    */   public void setDay(int day)
/* 180:    */   {
/* 181:197 */     this.day = day;
/* 182:    */   }
/* 183:    */   
/* 184:    */   public void setMonth(int month)
/* 185:    */   {
/* 186:201 */     this.month = month;
/* 187:    */   }
/* 188:    */   
/* 189:    */   public void setYr(int yr)
/* 190:    */   {
/* 191:205 */     this.yr = yr;
/* 192:    */   }
/* 193:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.util.Date
 * JD-Core Version:    0.7.0.1
 */