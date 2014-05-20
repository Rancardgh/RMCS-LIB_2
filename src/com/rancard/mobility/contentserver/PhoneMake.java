/*  1:   */ package com.rancard.mobility.contentserver;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.List;
/*  5:   */ 
/*  6:   */ public class PhoneMake
/*  7:   */ {
/*  8:   */   private int id;
/*  9:   */   private String make;
/* 10:   */   
/* 11:   */   public PhoneMake()
/* 12:   */   {
/* 13:22 */     this.id = 0;
/* 14:23 */     this.make = "";
/* 15:   */   }
/* 16:   */   
/* 17:   */   public PhoneMake(int id, String make)
/* 18:   */   {
/* 19:27 */     this.id = id;
/* 20:28 */     this.make = make;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public void setId(int id)
/* 24:   */   {
/* 25:33 */     this.id = id;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public void setMake(String make)
/* 29:   */   {
/* 30:37 */     this.make = make;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public int getId()
/* 34:   */   {
/* 35:42 */     return this.id;
/* 36:   */   }
/* 37:   */   
/* 38:   */   public String getMake()
/* 39:   */   {
/* 40:46 */     return this.make;
/* 41:   */   }
/* 42:   */   
/* 43:   */   public static PhoneMake viewPhoneMake(int referenceCode)
/* 44:   */     throws Exception
/* 45:   */   {
/* 46:51 */     return PhoneMakeDB.viewPhoneMake(referenceCode);
/* 47:   */   }
/* 48:   */   
/* 49:   */   public void updatePhoneMake()
/* 50:   */     throws Exception
/* 51:   */   {
/* 52:55 */     PhoneMakeDB.updatePhoneMake(this.id, this.make);
/* 53:   */   }
/* 54:   */   
/* 55:   */   public void removePhoneMake()
/* 56:   */     throws Exception
/* 57:   */   {
/* 58:59 */     PhoneMakeDB.deletePhoneMake(this.id);
/* 59:   */   }
/* 60:   */   
/* 61:   */   public void insertPhoneMake()
/* 62:   */     throws Exception
/* 63:   */   {
/* 64:63 */     PhoneMakeDB.insertPhoneMake(this.make);
/* 65:   */   }
/* 66:   */   
/* 67:   */   public static List getPhoneMakes()
/* 68:   */     throws Exception
/* 69:   */   {
/* 70:67 */     List makeIDs = new ArrayList();
/* 71:68 */     makeIDs = PhoneMakeDB.getPhoneMakes();
/* 72:69 */     return makeIDs;
/* 73:   */   }
/* 74:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.contentserver.PhoneMake
 * JD-Core Version:    0.7.0.1
 */