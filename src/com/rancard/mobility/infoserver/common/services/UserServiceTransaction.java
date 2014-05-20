/*  1:   */ package com.rancard.mobility.infoserver.common.services;
/*  2:   */ 
/*  3:   */ import com.rancard.mobility.common.ServiceTransaction;
/*  4:   */ import java.sql.Timestamp;
/*  5:   */ 
/*  6:   */ public class UserServiceTransaction
/*  7:   */   extends ServiceTransaction
/*  8:   */ {
/*  9:   */   public UserServiceTransaction() {}
/* 10:   */   
/* 11:   */   public UserServiceTransaction(String transactionId, String keyword, String accountId, String msg, String msisdn, String callBackUrl, Timestamp date, String pricePoint, int isBilled, int isCompleted)
/* 12:   */   {
/* 13:25 */     super(transactionId, keyword, accountId, msg, msisdn, callBackUrl, date, pricePoint, isBilled, isCompleted);
/* 14:   */   }
/* 15:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.infoserver.common.services.UserServiceTransaction
 * JD-Core Version:    0.7.0.1
 */