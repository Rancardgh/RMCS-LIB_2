/*  1:   */ package com.rancard.security;
/*  2:   */ 
/*  3:   */ public class AccessListItem
/*  4:   */ {
/*  5:   */   private String ipAddress;
/*  6:   */   private String owner;
/*  7:   */   
/*  8:   */   public void setIpAddress(String ipAddress)
/*  9:   */   {
/* 10:10 */     this.ipAddress = ipAddress;
/* 11:   */   }
/* 12:   */   
/* 13:   */   public void setOwner(String owner)
/* 14:   */   {
/* 15:14 */     this.owner = owner;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public String getIpAddress()
/* 19:   */   {
/* 20:18 */     return this.ipAddress;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public String getOwner()
/* 24:   */   {
/* 25:22 */     return this.owner;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public String getOwner(String ipAddress)
/* 29:   */   {
/* 30:26 */     return this.owner;
/* 31:   */   }
/* 32:   */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.security.AccessListItem
 * JD-Core Version:    0.7.0.1
 */