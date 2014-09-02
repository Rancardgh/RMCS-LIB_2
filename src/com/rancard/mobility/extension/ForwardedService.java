/*  1:   */ package com.rancard.mobility.extension;
/*  2:   */ 
/*  3:   */ import com.rancard.mobility.infoserver.common.services.UserService;
/*  4:   */ import java.sql.Timestamp;
/*  5:   */ import java.util.Calendar;
/*  6:   */ import java.util.Date;
/*  7:   */ 
/*  8:   */ public class ForwardedService
/*  9:   */   extends UserService
/* 10:   */ {
/* 11:   */   public static final int REPLY_WITH_DEFAULT = 2;
/* 12:   */   public static final int LISTEN_FOR_REPLY = 1;
/* 13:   */   public static final int NEVER_LISTEN_FOR_REPLY = 0;
/* 14:   */   private String url;
/* 15:   */   private int timeout;
/* 16:   */   private int listenStatus;
/* 17:   */   
/* 18:   */   public ForwardedService()
/* 19:   */   {
/* 20:33 */     this.url = "";
/* 21:34 */     this.timeout = 0;
/* 22:35 */     this.listenStatus = 0;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public ForwardedService(String serviceType, String keyword, String accountId, String serviceName, String defaultMessage, String url, int timeout, int listenStatus)
/* 26:   */   {
/* 27:39 */     super(serviceType, keyword, accountId, serviceName, defaultMessage);
/* 28:40 */     this.url = url;
/* 29:41 */     this.timeout = timeout;
/* 30:42 */     this.listenStatus = listenStatus;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public Timestamp now()
/* 34:   */   {
/* 35:46 */     return new Timestamp(Calendar.getInstance().getTime().getTime());
/* 36:   */   }
/* 37:   */   
/* 38:   */   public String getUrl()
/* 39:   */   {
/* 40:50 */     return this.url;
/* 41:   */   }
/* 42:   */   
/* 43:   */   public void setUrl(String url)
/* 44:   */   {
/* 45:54 */     this.url = url;
/* 46:   */   }
/* 47:   */   
/* 48:   */   public int getTimeout()
/* 49:   */   {
/* 50:58 */     return this.timeout;
/* 51:   */   }
/* 52:   */   
/* 53:   */   public void setTimeout(int timeout)
/* 54:   */   {
/* 55:62 */     this.timeout = timeout;
/* 56:   */   }
/* 57:   */   
/* 58:   */   public int getListenStatus()
/* 59:   */   {
/* 60:66 */     return this.listenStatus;
/* 61:   */   }
/* 62:   */   
/* 63:   */   public void setListenStatus(int listenStatus)
/* 64:   */   {
/* 65:70 */     this.listenStatus = listenStatus;
/* 66:   */   }
/* 67:   */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.extension.ForwardedService
 * JD-Core Version:    0.7.0.1
 */