/*  1:   */ package com.rancard.mobility.infoserver.common.services;
/*  2:   */ 
/*  3:   */ import java.util.Date;
/*  4:   */ 
/*  5:   */ public class ServiceSubscriber
/*  6:   */ {
/*  7:   */   private String msisdn;
/*  8:   */   private String accountId;
/*  9:   */   private String keyword;
/* 10:   */   private String subscriptionDate;
/* 11:   */   private String nextSubscriptionDate;
/* 12:   */   private int status;
/* 13:   */   private int billingType;
/* 14:   */   public static final int BILLING_TYPE_NON = 0;
/* 15:   */   public static final int ON_DEMAND_BILLING = 1;
/* 16:   */   public static final int MONTHLY_BILLING = 2;
/* 17:   */   public static final int STATUS_ACTIVE = 1;
/* 18:   */   public static final int STATUS_INACTIVE = 0;
/* 19:   */   
/* 20:   */   public ServiceSubscriber()
/* 21:   */   {
/* 22:35 */     this.msisdn = "";
/* 23:36 */     this.accountId = "";
/* 24:37 */     this.keyword = "";
/* 25:38 */     this.subscriptionDate = new Date().toString();
/* 26:39 */     this.nextSubscriptionDate = "";
/* 27:40 */     this.status = 0;
/* 28:41 */     this.billingType = 0;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public void setMsisdn(String msisdn)
/* 32:   */   {
/* 33:48 */     this.msisdn = msisdn;
/* 34:   */   }
/* 35:   */   
/* 36:   */   public String getMsisdn()
/* 37:   */   {
/* 38:51 */     return this.msisdn;
/* 39:   */   }
/* 40:   */   
/* 41:   */   public void setAccountId(String accountId)
/* 42:   */   {
/* 43:54 */     this.accountId = accountId;
/* 44:   */   }
/* 45:   */   
/* 46:   */   public String getAccountId()
/* 47:   */   {
/* 48:57 */     return this.accountId;
/* 49:   */   }
/* 50:   */   
/* 51:   */   public void setKeyword(String keyword)
/* 52:   */   {
/* 53:60 */     this.keyword = keyword;
/* 54:   */   }
/* 55:   */   
/* 56:   */   public String getKeyword()
/* 57:   */   {
/* 58:63 */     return this.keyword;
/* 59:   */   }
/* 60:   */   
/* 61:   */   public void setSubscriptionDate(String subscriptionDate)
/* 62:   */   {
/* 63:66 */     this.subscriptionDate = subscriptionDate;
/* 64:   */   }
/* 65:   */   
/* 66:   */   public String getSubscriptionDate()
/* 67:   */   {
/* 68:69 */     return this.subscriptionDate;
/* 69:   */   }
/* 70:   */   
/* 71:   */   public void setNextSubscriptionDate(String nextSubscriptionDate)
/* 72:   */   {
/* 73:72 */     this.nextSubscriptionDate = nextSubscriptionDate;
/* 74:   */   }
/* 75:   */   
/* 76:   */   public String getNextSubscriptionDate()
/* 77:   */   {
/* 78:75 */     return this.nextSubscriptionDate;
/* 79:   */   }
/* 80:   */   
/* 81:   */   public void setStatus(int status)
/* 82:   */   {
/* 83:78 */     this.status = status;
/* 84:   */   }
/* 85:   */   
/* 86:   */   public int getStatus()
/* 87:   */   {
/* 88:81 */     return this.status;
/* 89:   */   }
/* 90:   */   
/* 91:   */   public void setBillingType(int billingType)
/* 92:   */   {
/* 93:84 */     this.billingType = billingType;
/* 94:   */   }
/* 95:   */   
/* 96:   */   public int getBillingType()
/* 97:   */   {
/* 98:87 */     return this.billingType;
/* 99:   */   }
/* :0:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.common.services.ServiceSubscriber
 * JD-Core Version:    0.7.0.1
 */