/*  1:   */ package com.rancard.mobility.common;
/*  2:   */ 
/*  3:   */ import com.rancard.mobility.contentserver.CPConnections;
/*  4:   */ import java.io.PrintStream;
/*  5:   */ import java.util.Date;
/*  6:   */ 
/*  7:   */ public class ThreadedMessageSender
/*  8:   */   implements Runnable
/*  9:   */ {
/* 10:   */   CPConnections cnxn;
/* 11:   */   String msisdn;
/* 12:   */   String short_code;
/* 13:   */   String message;
/* 14:   */   String meta_data;
/* 15:   */   int delay;
/* 16:   */   
/* 17:   */   public ThreadedMessageSender(CPConnections cnxn, String msisdn, String short_code, String message, int delay)
/* 18:   */   {
/* 19:23 */     this.cnxn = cnxn;
/* 20:24 */     this.msisdn = msisdn;
/* 21:25 */     this.short_code = short_code;
/* 22:26 */     this.message = message;
/* 23:27 */     this.delay = delay;
/* 24:28 */     this.meta_data = "";
/* 25:   */   }
/* 26:   */   
/* 27:   */   public ThreadedMessageSender(CPConnections cnxn, String msisdn, String short_code, String message, String meta_data, int delay)
/* 28:   */   {
/* 29:32 */     this.cnxn = cnxn;
/* 30:33 */     this.msisdn = msisdn;
/* 31:34 */     this.short_code = short_code;
/* 32:35 */     this.message = message;
/* 33:36 */     this.delay = delay;
/* 34:37 */     this.meta_data = meta_data;
/* 35:   */   }
/* 36:   */   
/* 37:   */   public void run()
/* 38:   */   {
/* 39:   */     try
/* 40:   */     {
/* 41:43 */       Thread.sleep(this.delay);
/* 42:44 */       Driver.getDriver(this.cnxn.getDriverType(), this.cnxn.getGatewayURL()).sendSMSTextMessage(this.msisdn, this.short_code, this.message, this.cnxn.getUsername(), this.cnxn.getPassword(), this.cnxn.getConnection(), this.meta_data);
/* 43:   */     }
/* 44:   */     catch (InterruptedException e)
/* 45:   */     {
/* 46:47 */       System.out.println(new Date() + ": " + this.short_code + ": " + this.msisdn + ": thread error sending notification @ ThreadedMessageSender: " + e.getMessage());
/* 47:   */     }
/* 48:   */     catch (Exception e)
/* 49:   */     {
/* 50:49 */       System.out.println(new Date() + ": " + this.short_code + ": " + this.msisdn + ": error sending notification @ ThreadedMessageSender: " + e.getMessage());
/* 51:   */     }
/* 52:   */     finally
/* 53:   */     {
/* 54:51 */       this.cnxn = null;
/* 55:   */     }
/* 56:   */   }
/* 57:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.common.ThreadedMessageSender
 * JD-Core Version:    0.7.0.1
 */