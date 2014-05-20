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
/*  18: 35 */     StringBuilder id = new StringBuilder();
/*  19: 36 */     char[] characters = { 'Q', 'U', 'o', 'a', 'G', 'y', '2', 'M', '4', '0', 'v', '9', 'T', 'c', 'k', 'l', 'N', 'm', 'g', 'q', '5', 'r', '8', 's', 'w', 'z', 'Z', 'x', 'A', 'f', 'i', 'u', 'B', '1', 'E', 'H', 'I', 'p', 'J', 'X', 'j', 'L', '7', 'e', 'W', 'C', 'O', '3', 'n', 'P', 'd', 'R', 'F', '6', 'S', 't', 'D', 'V', 'b', 'K', 'Y', 'h' };
/*  20: 42 */     for (int i = 0; i < stringLength; i++)
/*  21:    */     {
/*  22: 43 */       double index = Math.random() * characters.length;
/*  23: 44 */       id.append(characters[new java.lang.Double(index).intValue()]);
/*  24:    */     }
/*  25: 47 */     String id_string = id.toString();
/*  26:    */     
/*  27:    */ 
/*  28: 50 */     return id_string;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public static synchronized String generateNumberID(int stringLength)
/*  32:    */   {
/*  33: 54 */     StringBuilder id = new StringBuilder();
/*  34: 55 */     char[] characters = { '1', '0', '2', '4', '3', '5', '8', '6', '9', '0', '7', '9', '3', '1', '9', '7', '6', '8', '1', '4', '2', '3', '9', '3', '1', '8', '5', '5', '3', '8', '6', '0', '2', '1', '6', '8', '8' };
/*  35: 59 */     for (int i = 0; i < stringLength; i++)
/*  36:    */     {
/*  37: 60 */       double index = Math.random() * characters.length;
/*  38: 61 */       id.append(characters[new java.lang.Double(index).intValue()]);
/*  39:    */     }
/*  40: 64 */     String id_string = id.toString();
/*  41:    */     
/*  42:    */ 
/*  43:    */ 
/*  44: 68 */     return id_string;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public static synchronized String getUId()
/*  48:    */   {
/*  49: 72 */     Calendar cal = new GregorianCalendar();
/*  50: 73 */     Date currentDate = cal.getTime();
/*  51: 74 */     DateFormat df = new SimpleDateFormat("yyMMddhhmmssss");
/*  52: 75 */     String header = new Integer(100 + (int)(Math.random() * 100.0D + Math.random() * 100.0D - Math.random() * 20.0D)).toString();
/*  53: 76 */     if (header.length() < 3) {
/*  54: 77 */       header = header + generateNumberID(3 - header.length());
/*  55:    */     }
/*  56: 79 */     String tail = new Integer(100 + (int)(Math.random() * 100.0D + Math.random() * 100.0D - Math.random() * 20.0D)).toString();
/*  57: 80 */     if (tail.length() < 3) {
/*  58: 81 */       tail = tail + generateNumberID(3 - tail.length());
/*  59:    */     }
/*  60: 83 */     return header + df.format(currentDate) + tail;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public static synchronized String genUID(String prefix, int numRandomDigits)
/*  64:    */   {
/*  65: 87 */     SecureRandom wheel = new SecureRandom();
/*  66: 88 */     int base = (int)Math.pow(10.0D, numRandomDigits);
/*  67: 89 */     int myInt = wheel.nextInt(1000000) + base;
/*  68: 90 */     String str = Integer.toString(myInt);
/*  69:    */     
/*  70: 92 */     Calendar cal = new GregorianCalendar();
/*  71: 93 */     Date currentDate = cal.getTime();
/*  72: 94 */     DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
/*  73: 95 */     return prefix + df.format(currentDate) + "" + str.substring(0, numRandomDigits);
/*  74:    */   }
/*  75:    */   
/*  76:    */   public static synchronized String generateSecureUID()
/*  77:    */   {
/*  78: 99 */     UUID id = UUID.randomUUID();
/*  79:100 */     return id.toString();
/*  80:    */   }
/*  81:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.common.uidGen
 * JD-Core Version:    0.7.0.1
 */