/*  1:   */ package com.rancard.mobility.extension;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ 
/*  5:   */ public abstract class ForwardedServiceManager
/*  6:   */ {
/*  7:   */   public static void createForwardedService(ForwardedService service)
/*  8:   */     throws Exception
/*  9:   */   {
/* 10:21 */     ForwardedServiceDB.createForwardedService(service);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public static void updateForwardedServiceName(String keyword, String account_id, String serviceName)
/* 14:   */     throws Exception
/* 15:   */   {
/* 16:25 */     ForwardedServiceDB.updateForwardedServiceName(keyword, account_id, serviceName);
/* 17:   */   }
/* 18:   */   
/* 19:   */   public static void updateForwardedServiceParameters(String keyword, String account_id, String url, int timeout, int listenStatus)
/* 20:   */     throws Exception
/* 21:   */   {
/* 22:29 */     ForwardedServiceDB.updateForwardedServiceParameters(keyword, account_id, url, timeout, listenStatus);
/* 23:   */   }
/* 24:   */   
/* 25:   */   public static void updateForwardedService(String keyword, String account_id, String name, String url, int timeout, int listenStatus)
/* 26:   */     throws Exception
/* 27:   */   {
/* 28:33 */     ForwardedServiceDB.updateForwardedService(keyword, account_id, name, url, timeout, listenStatus);
/* 29:   */   }
/* 30:   */   
/* 31:   */   public static ForwardedService viewForwardedService(String keyword, String accountId)
/* 32:   */     throws Exception
/* 33:   */   {
/* 34:37 */     return ForwardedServiceDB.viewForwardedService(keyword, accountId);
/* 35:   */   }
/* 36:   */   
/* 37:   */   public static ArrayList viewAllForwardedServices(String accountId)
/* 38:   */     throws Exception
/* 39:   */   {
/* 40:41 */     return ForwardedServiceDB.viewAllForwardedServices(accountId);
/* 41:   */   }
/* 42:   */   
/* 43:   */   public static void deleteForwardedService(String keyword, String account_id)
/* 44:   */     throws Exception
/* 45:   */   {
/* 46:45 */     ForwardedServiceDB.deleteForwardedService(keyword, account_id);
/* 47:   */   }
/* 48:   */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.extension.ForwardedServiceManager
 * JD-Core Version:    0.7.0.1
 */