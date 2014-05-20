/*  1:   */ package com.rancard.mobility.infoserver.common.services;
/*  2:   */ 
/*  3:   */ import java.util.Date;
/*  4:   */ 
/*  5:   */ public class ServiceSubscriber
/*  6:   */ {
/*  7:   */   private String msisdn;
/*  8:   */   private String accountID;
/*  9:   */   private String keyword;
/* 10:   */   private Date subscriptionDate;
/* 11:   */   private Date nextSubscriptionDate;
/* 12:   */   private int status;
/* 13:   */   private int billingType;
/* 14:   */   public static final int BILLING_TYPE_NON = 0;
/* 15:   */   public static final int ON_DEMAND_BILLING = 1;
/* 16:   */   public static final int MONTHLY_BILLING = 2;
/* 17:   */   public static final int STATUS_ACTIVE = 1;
/* 18:   */   public static final int STATUS_INACTIVE = 0;
/* 19:   */   
/* 20:   */   public ServiceSubscriber(String msisdn, String accountID, String keyword, Date subscriptionDate, Date nextSubscriptionDate, int status, int billingType)
/* 21:   */   {
/* 22:36 */     this.msisdn = msisdn;
/* 23:37 */     this.accountID = accountID;
/* 24:38 */     this.keyword = keyword;
/* 25:39 */     this.subscriptionDate = subscriptionDate;
/* 26:40 */     this.nextSubscriptionDate = nextSubscriptionDate;
/* 27:41 */     this.status = status;
/* 28:42 */     this.billingType = billingType;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public String getMsisdn()
/* 32:   */   {
/* 33:46 */     return this.msisdn;
/* 34:   */   }
/* 35:   */   
/* 36:   */   public String getAccountID()
/* 37:   */   {
/* 38:50 */     return this.accountID;
/* 39:   */   }
/* 40:   */   
/* 41:   */   public void setKeyword(String keyword)
/* 42:   */   {
/* 43:54 */     this.keyword = keyword;
/* 44:   */   }
/* 45:   */   
/* 46:   */   public String getKeyword()
/* 47:   */   {
/* 48:58 */     return this.keyword;
/* 49:   */   }
/* 50:   */   
/* 51:   */   public Date getSubscriptionDate()
/* 52:   */   {
/* 53:62 */     return this.subscriptionDate;
/* 54:   */   }
/* 55:   */   
/* 56:   */   public Date getNextSubscriptionDate()
/* 57:   */   {
/* 58:66 */     return this.nextSubscriptionDate;
/* 59:   */   }
/* 60:   */   
/* 61:   */   public int getStatus()
/* 62:   */   {
/* 63:70 */     return this.status;
/* 64:   */   }
/* 65:   */   
/* 66:   */   public int getBillingType()
/* 67:   */   {
/* 68:74 */     return this.billingType;
/* 69:   */   }
/* 70:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.infoserver.common.services.ServiceSubscriber
 * JD-Core Version:    0.7.0.1
 */