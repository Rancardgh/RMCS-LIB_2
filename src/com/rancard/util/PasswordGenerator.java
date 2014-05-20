/*  1:   */ package com.rancard.util;
/*  2:   */ 
/*  3:   */ public class PasswordGenerator
/*  4:   */ {
/*  5:   */   public static String generatePassword(int n)
/*  6:   */   {
/*  7: 8 */     char[] pw = new char[n];
/*  8: 9 */     int c = 65;
/*  9:10 */     int r1 = 0;
/* 10:11 */     for (int i = 0; i < n; i++)
/* 11:   */     {
/* 12:13 */       r1 = (int)(Math.random() * 3.0D);
/* 13:14 */       switch (r1)
/* 14:   */       {
/* 15:   */       case 0: 
/* 16:15 */         c = 48 + (int)(Math.random() * 10.0D); break;
/* 17:   */       case 1: 
/* 18:16 */         c = 97 + (int)(Math.random() * 26.0D); break;
/* 19:   */       case 2: 
/* 20:17 */         c = 65 + (int)(Math.random() * 26.0D);
/* 21:   */       }
/* 22:19 */       pw[i] = ((char)c);
/* 23:   */     }
/* 24:21 */     return new String(pw);
/* 25:   */   }
/* 26:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.util.PasswordGenerator
 * JD-Core Version:    0.7.0.1
 */