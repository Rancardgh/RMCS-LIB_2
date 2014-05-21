/*  1:   */ package com.rancard.common;
/*  2:   */ 
/*  3:   */ public class Message
/*  4:   */ {
/*  5:   */   private boolean status;
/*  6:   */   private String message;
/*  7:   */   
/*  8:   */   public Message() {}
/*  9:   */   
/* 10:   */   public String toString()
/* 11:   */   {
/* 12:18 */     return getMessage();
/* 13:   */   }
/* 14:   */   
/* 15:   */   public Message(String message, boolean status)
/* 16:   */   {
/* 17:23 */     this.message = message;
/* 18:24 */     this.status = status;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public void setStatus(boolean status)
/* 22:   */   {
/* 23:27 */     this.status = status;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public void setMessage(String message)
/* 27:   */   {
/* 28:31 */     this.message = message;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public boolean isStatus()
/* 32:   */   {
/* 33:35 */     return this.status;
/* 34:   */   }
/* 35:   */   
/* 36:   */   public String getMessage()
/* 37:   */   {
/* 38:39 */     return this.message;
/* 39:   */   }
/* 40:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.common.Message
 * JD-Core Version:    0.7.0.1
 */