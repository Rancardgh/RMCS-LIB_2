/*   1:    */ package com.rancard.common;
/*   2:    */ 
/*   3:    */ import java.security.SecureRandom;
/*   4:    */ import java.text.DateFormat;
/*   5:    */ import java.text.SimpleDateFormat;
/*   6:    */ import java.util.Calendar;
/*   7:    */ import java.util.Date;
/*   8:    */ import java.util.GregorianCalendar;
/*   9:    */ import java.util.UUID;
/*  10:    */ 
/*  11:    */ public class uidGen
/*  12:    */ {
/*  13: 17 */   private String id = null;
/*  14: 19 */   private Calendar cal = new GregorianCalendar();
/*  15:    */   
/*  16:    */   public static synchronized String generateID(int stringLength)
/*  17:    */   {
/*  18: 35 */     StringBuffer id = new StringBuffer();
/*  19: 36 */     char[] characters = { 'Q', 'U', 'o', 'a', 'G', 'y', '2', 'M', '4', '0', 'v', '9', 'T', 'c', 'k', 'l', 'N', 'm', 'g', 'q', '5', 'r', '8', 's', 'w', 'z', 'Z', 'x', 'A', 'f', 'i', 'u', 'B', '1', 'E', 'H', 'I', 'p', 'J', 'X', 'j', 'L', '7', 'e', 'W', 'C', 'O', '3', 'n', 'P', 'd', 'R', 'F', '6', 'S', 't', 'D', 'V', 'b', 'K', 'Y', 'h' };
/*  20: 42 */     for (int i = 0; i < stringLength; i++)
/*  21:    */     {
/*  22: 43 */       double index = Math.random() * characters.length;
/*  23: 44 */       id.append(characters[new java.lang.Double(index).intValue()]);
/*  24:    */     }
/*  25: 47 */     String id_string = id.toString();
/*  26:    */     
/*  27:    */ 
/*  28: 50 */     id = null;
/*  29: 51 */     characters = null;
/*  30:    */     
/*  31: 53 */     return id_string;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public static synchronized String generateNumberID(int stringLength)
/*  35:    */   {
/*  36: 57 */     StringBuffer id = new StringBuffer();
/*  37: 58 */     char[] characters = { '1', '0', '2', '4', '3', '5', '8', '6', '9', '0', '7', '9', '3', '1', '9', '7', '6', '8', '1', '4', '2', '3', '9', '3', '1', '8', '5', '5', '3', '8', '6', '0', '2', '1', '6', '8', '8' };
/*  38: 62 */     for (int i = 0; i < stringLength; i++)
/*  39:    */     {
/*  40: 63 */       double index = Math.random() * characters.length;
/*  41: 64 */       id.append(characters[new java.lang.Double(index).intValue()]);
/*  42:    */     }
/*  43: 67 */     String id_string = id.toString();
/*  44:    */     
/*  45:    */ 
/*  46: 70 */     id = null;
/*  47: 71 */     characters = null;
/*  48:    */     
/*  49: 73 */     return id_string;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public static synchronized String getUId()
/*  53:    */   {
/*  54: 77 */     Calendar cal = new GregorianCalendar();
/*  55: 78 */     Date currentDate = cal.getTime();
/*  56: 79 */     DateFormat df = new SimpleDateFormat("yyMMddhhmmssss");
/*  57: 80 */     String header = new Integer(100 + (int)(Math.random() * 100.0D + Math.random() * 100.0D - Math.random() * 20.0D)).toString();
/*  58: 81 */     if (header.length() < 3) {
/*  59: 82 */       header = header + generateNumberID(3 - header.length());
/*  60:    */     }
/*  61: 84 */     String tail = new Integer(100 + (int)(Math.random() * 100.0D + Math.random() * 100.0D - Math.random() * 20.0D)).toString();
/*  62: 85 */     if (tail.length() < 3) {
/*  63: 86 */       tail = tail + generateNumberID(3 - tail.length());
/*  64:    */     }
/*  65: 88 */     return header + df.format(currentDate) + tail;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public static synchronized String genUID(String prefix, int numRandomDigits)
/*  69:    */   {
/*  70: 92 */     SecureRandom wheel = new SecureRandom();
/*  71: 93 */     int base = (int)Math.pow(10.0D, numRandomDigits);
/*  72: 94 */     int myInt = wheel.nextInt(1000000) + base;
/*  73: 95 */     String str = Integer.toString(myInt);
/*  74:    */     
/*  75: 97 */     Calendar cal = new GregorianCalendar();
/*  76: 98 */     Date currentDate = cal.getTime();
/*  77: 99 */     DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
/*  78:100 */     return prefix + df.format(currentDate) + "" + str.substring(0, numRandomDigits);
/*  79:    */   }
/*  80:    */   
/*  81:    */   public static synchronized String generateSecureUID()
/*  82:    */   {
/*  83:104 */     UUID id = UUID.randomUUID();
/*  84:105 */     return id.toString();
/*  85:    */   }
/*  86:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.common.uidGen
 * JD-Core Version:    0.7.0.1
 */