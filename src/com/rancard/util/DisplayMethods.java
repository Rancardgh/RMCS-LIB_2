/*  1:   */ package com.rancard.util;
/*  2:   */ 
/*  3:   */ import java.text.DecimalFormat;
/*  4:   */ import java.text.NumberFormat;
/*  5:   */ 
/*  6:   */ public class DisplayMethods
/*  7:   */ {
/*  8:   */   public String clearNull(String strValue)
/*  9:   */     throws Exception
/* 10:   */   {
/* 11:20 */     if ((strValue == null) || (strValue.equals("null"))) {
/* 12:21 */       strValue = "";
/* 13:   */     }
/* 14:24 */     return strValue;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public String clearNullRHS(String strValue)
/* 18:   */     throws Exception
/* 19:   */   {
/* 20:29 */     if ((strValue == null) || (strValue.equals("null"))) {
/* 21:30 */       strValue = "&nbsp;";
/* 22:   */     }
/* 23:33 */     return strValue;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public double clearNull_dbl(Double dblValue)
/* 27:   */     throws Exception
/* 28:   */   {
/* 29:37 */     if (dblValue == null) {
/* 30:38 */       dblValue = new Double(0.0D);
/* 31:   */     }
/* 32:41 */     return dblValue.doubleValue();
/* 33:   */   }
/* 34:   */   
/* 35:   */   public int clearNull_int(Integer intValue)
/* 36:   */     throws Exception
/* 37:   */   {
/* 38:45 */     if (intValue == null) {
/* 39:46 */       intValue = new Integer(0);
/* 40:   */     }
/* 41:49 */     return intValue.intValue();
/* 42:   */   }
/* 43:   */   
/* 44:   */   public String dispMoney(Double dblValue)
/* 45:   */     throws Exception
/* 46:   */   {
/* 47:53 */     NumberFormat numberFormatter = NumberFormat.getNumberInstance();
/* 48:54 */     NumberFormat nFormat = new DecimalFormat("#,##0.00");
/* 49:   */     
/* 50:56 */     String strValue = "";
/* 51:58 */     if (dblValue != null) {
/* 52:59 */       strValue = nFormat.format(dblValue);
/* 53:   */     } else {
/* 54:62 */       strValue = "__________";
/* 55:   */     }
/* 56:65 */     return strValue;
/* 57:   */   }
/* 58:   */   
/* 59:   */   public String reduceString(String description, Integer display)
/* 60:   */   {
/* 61:69 */     int i_display = display.intValue();
/* 62:70 */     int string_len = description.length();
/* 63:72 */     if (i_display > description.length()) {
/* 64:73 */       i_display = description.length();
/* 65:   */     }
/* 66:76 */     char[] description_20 = new char[i_display];
/* 67:77 */     description.getChars(0, i_display, description_20, 0);
/* 68:   */     
/* 69:79 */     description = "";
/* 70:81 */     for (int i = 0; i < i_display; i++) {
/* 71:82 */       description = description + description_20[i];
/* 72:   */     }
/* 73:85 */     if (display.intValue() < string_len) {
/* 74:86 */       description = description + "...";
/* 75:   */     }
/* 76:89 */     return description;
/* 77:   */   }
/* 78:   */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.util.DisplayMethods
 * JD-Core Version:    0.7.0.1
 */