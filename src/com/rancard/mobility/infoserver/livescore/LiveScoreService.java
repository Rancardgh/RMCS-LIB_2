/*  1:   */ package com.rancard.mobility.infoserver.livescore;
/*  2:   */ 
/*  3:   */ import com.rancard.mobility.infoserver.common.services.UserService;
/*  4:   */ import java.sql.Timestamp;
/*  5:   */ import java.util.Calendar;
/*  6:   */ import java.util.Date;
/*  7:   */ 
/*  8:   */ public class LiveScoreService
/*  9:   */   extends UserService
/* 10:   */ {
/* 11:   */   private String[] liveScoreServiceIds;
/* 12:   */   
/* 13:   */   public LiveScoreService()
/* 14:   */   {
/* 15:27 */     this.liveScoreServiceIds = null;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public LiveScoreService(String serviceType, String keyword, String accountId, String serviceName, String defaultMessage, String[] liveScoreServiceIds)
/* 19:   */   {
/* 20:31 */     super(serviceType, keyword, accountId, serviceName, defaultMessage);
/* 21:32 */     this.liveScoreServiceIds = liveScoreServiceIds;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public LiveScoreService(String serviceType, String keyword, String accountId, String serviceid, String defaultMessage, String command, String allowedShortcodes, String allowedSiteTypes, String pricing, boolean isBasic, String[] liveScoreServiceIds)
/* 25:   */   {
/* 26:37 */     super(serviceType, keyword, accountId, serviceid, defaultMessage, command, allowedShortcodes, allowedSiteTypes, pricing, isBasic);
/* 27:38 */     this.liveScoreServiceIds = liveScoreServiceIds;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public Timestamp now()
/* 31:   */   {
/* 32:42 */     return new Timestamp(Calendar.getInstance().getTime().getTime());
/* 33:   */   }
/* 34:   */   
/* 35:   */   public String[] getLiveScoreServiceIds()
/* 36:   */   {
/* 37:46 */     return this.liveScoreServiceIds;
/* 38:   */   }
/* 39:   */   
/* 40:   */   public void setLiveScoreServiceIds(String[] liveScoreServiceIds)
/* 41:   */   {
/* 42:50 */     this.liveScoreServiceIds = liveScoreServiceIds;
/* 43:   */   }
/* 44:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.infoserver.livescore.LiveScoreService
 * JD-Core Version:    0.7.0.1
 */