/*  1:   */ package com.rancard.util;
/*  2:   */ 
/*  3:   */ import java.io.PrintStream;
/*  4:   */ 
/*  5:   */ public class StringTest
/*  6:   */ {
/*  7: 7 */   String fileName = "some.file.txt.excel.document";
/*  8:   */   
/*  9:   */   public int getFileName()
/* 10:   */   {
/* 11:11 */     return this.fileName.lastIndexOf(".");
/* 12:   */   }
/* 13:   */   
/* 14:   */   public static void main(String[] args)
/* 15:   */   {
/* 16:21 */     StringTest thistest = new StringTest();
/* 17:22 */     System.out.println(thistest.getFileName());
/* 18:23 */     System.out.println(thistest.fileName.substring(thistest.getFileName()));
/* 19:   */   }
/* 20:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.util.StringTest
 * JD-Core Version:    0.7.0.1
 */