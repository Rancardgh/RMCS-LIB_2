/*   1:    */ package com.rancard.mobility.infoserver.common.services;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import com.rancard.util.DateUtil;
/*   5:    */ import java.sql.Connection;
/*   6:    */ import java.sql.SQLException;
/*   7:    */ import java.sql.Statement;
/*   8:    */ import java.util.Date;
/*   9:    */ 
/*  10:    */ public class ServiceSubscriberDB
/*  11:    */ {
/*  12:    */   public static void addServiceSubscription(ServiceSubscriber serviceSubscriber)
/*  13:    */     throws SQLException, Exception
/*  14:    */   {
/*  15: 20 */     Connection conn = null;
/*  16: 21 */     String sql = "INSERT INTO service_subscription (subscription_date, next_subscription_date, msisdn, account_id, keyword, status, billing_type) VALUES ('" + DateUtil.convertToMySQLTimeStamp(serviceSubscriber.getSubscriptionDate()) + "', '" + DateUtil.convertToMySQLTimeStamp(serviceSubscriber.getNextSubscriptionDate()) + "', " + "'" + serviceSubscriber.getMsisdn() + "', '" + serviceSubscriber.getAccountID() + "', '" + serviceSubscriber.getKeyword() + "', " + serviceSubscriber.getStatus() + ", " + serviceSubscriber.getBillingType() + ")";
/*  17:    */     try
/*  18:    */     {
/*  19: 25 */       conn = DConnect.getConnection();
/*  20: 26 */       conn.createStatement().execute(sql);
/*  21:    */     }
/*  22:    */     catch (SQLException se)
/*  23:    */     {
/*  24: 29 */       throw new SQLException(se.getMessage(), se.getSQLState());
/*  25:    */     }
/*  26:    */     finally
/*  27:    */     {
/*  28: 31 */       if (conn != null) {
/*  29: 32 */         conn.close();
/*  30:    */       }
/*  31:    */     }
/*  32:    */   }
/*  33:    */   
/*  34:    */   public static void addServiceSubscription(ServiceSubscriber serviceSubscriber, Connection conn)
/*  35:    */     throws SQLException
/*  36:    */   {
/*  37: 38 */     String sql = "INSERT INTO service_subscription (subscription_date, next_subscription_date, msisdn, account_id, keyword, status, billing_type) VALUES ('" + DateUtil.convertToMySQLTimeStamp(serviceSubscriber.getSubscriptionDate()) + "', '" + DateUtil.convertToMySQLTimeStamp(serviceSubscriber.getNextSubscriptionDate()) + "', " + "'" + serviceSubscriber.getMsisdn() + "', '" + serviceSubscriber.getAccountID() + "', '" + serviceSubscriber.getKeyword() + "', " + serviceSubscriber.getStatus() + ", " + serviceSubscriber.getBillingType() + ")";
/*  38:    */     try
/*  39:    */     {
/*  40: 43 */       conn.createStatement().execute(sql);
/*  41:    */     }
/*  42:    */     catch (SQLException se)
/*  43:    */     {
/*  44: 46 */       throw new SQLException(se.getMessage(), se.getSQLState());
/*  45:    */     }
/*  46:    */   }
/*  47:    */   
/*  48:    */   public static void addSubscription(Date subscriptionDate, Date nextSubscriptionDate, String msisdn, String accountID, String keyword, int status, int billingType)
/*  49:    */     throws SQLException, Exception
/*  50:    */   {
/*  51: 52 */     Connection conn = null;
/*  52: 53 */     String sql = "INSERT INTO service_subscription (subscription_date, next_subscription_date, msisdn, account_id, keyword, status, billing_type) VALUES ('" + DateUtil.convertToMySQLTimeStamp(subscriptionDate) + "', '" + DateUtil.convertToMySQLTimeStamp(nextSubscriptionDate) + "', " + "'" + msisdn + "', '" + accountID + "', '" + keyword + "', " + status + ", " + billingType + ")";
/*  53:    */     try
/*  54:    */     {
/*  55: 57 */       conn = DConnect.getConnection();
/*  56: 58 */       conn.createStatement().execute(sql);
/*  57:    */     }
/*  58:    */     catch (SQLException se)
/*  59:    */     {
/*  60: 61 */       throw new SQLException(se.getMessage(), se.getSQLState());
/*  61:    */     }
/*  62:    */     finally
/*  63:    */     {
/*  64: 63 */       if (conn != null) {
/*  65: 64 */         conn.close();
/*  66:    */       }
/*  67:    */     }
/*  68:    */   }
/*  69:    */   
/*  70:    */   public static void addSubscription(Date subscriptionDate, Date nextSubscriptionDate, String msisdn, String accountID, String keyword, int status, int billingType, Connection conn)
/*  71:    */     throws SQLException
/*  72:    */   {
/*  73: 72 */     String sql = "INSERT INTO service_subscription (subscription_date, next_subscription_date, msisdn, account_id, keyword, status, billing_type) VALUES ('" + DateUtil.convertToMySQLTimeStamp(subscriptionDate) + "', '" + DateUtil.convertToMySQLTimeStamp(nextSubscriptionDate) + "', " + "'" + msisdn + "', '" + accountID + "', '" + keyword + "', " + status + ", " + billingType + ")";
/*  74:    */     try
/*  75:    */     {
/*  76: 76 */       conn.createStatement().execute(sql);
/*  77:    */     }
/*  78:    */     catch (SQLException se)
/*  79:    */     {
/*  80: 78 */       throw new SQLException(se.getMessage(), se.getSQLState());
/*  81:    */     }
/*  82:    */   }
/*  83:    */   
/*  84:    */   public static void addSubscription(Date subscriptionDate, int subscriptionInterval, String msisdn, String accountID, String keyword, int status, int billingType)
/*  85:    */     throws SQLException, Exception
/*  86:    */   {
/*  87: 84 */     Connection conn = null;
/*  88: 85 */     String sql = "INSERT INTO service_subscription (subscription_date, next_subscription_date, msisdn, account_id, keyword, status, billing_type) VALUES ('" + DateUtil.convertToMySQLTimeStamp(subscriptionDate) + "', '" + DateUtil.convertToMySQLTimeStamp(DateUtil.addDaysToDate(subscriptionDate, subscriptionInterval)) + "', " + "'" + msisdn + "', '" + accountID + "', '" + keyword + "', " + status + ", " + billingType + ")";
/*  89:    */     try
/*  90:    */     {
/*  91: 89 */       conn = DConnect.getConnection();
/*  92: 90 */       conn.createStatement().execute(sql);
/*  93:    */     }
/*  94:    */     catch (SQLException se)
/*  95:    */     {
/*  96: 93 */       throw new SQLException(se.getMessage(), se.getSQLState());
/*  97:    */     }
/*  98:    */     finally
/*  99:    */     {
/* 100: 95 */       if (conn != null) {
/* 101: 96 */         conn.close();
/* 102:    */       }
/* 103:    */     }
/* 104:    */   }
/* 105:    */   
/* 106:    */   public static void addSubscription(Date subscriptionDate, int subscriptionInterval, String msisdn, String accountID, String keyword, int status, int billingType, Connection conn)
/* 107:    */     throws SQLException, Exception
/* 108:    */   {
/* 109:104 */     String sql = "INSERT INTO service_subscription (subscription_date, next_subscription_date, msisdn, account_id, keyword, status, billing_type) VALUES ('" + DateUtil.convertToMySQLTimeStamp(subscriptionDate) + "', '" + DateUtil.convertToMySQLTimeStamp(DateUtil.addDaysToDate(subscriptionDate, subscriptionInterval)) + "', " + "'" + msisdn + "', '" + accountID + "', '" + keyword + "', " + status + ", " + billingType + ")";
/* 110:    */     try
/* 111:    */     {
/* 112:108 */       conn.createStatement().execute(sql);
/* 113:    */     }
/* 114:    */     catch (SQLException se)
/* 115:    */     {
/* 116:110 */       throw new SQLException(se.getMessage(), se.getSQLState());
/* 117:    */     }
/* 118:    */   }
/* 119:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.common.services.ServiceSubscriberDB
 * JD-Core Version:    0.7.0.1
 */