/*  1:   */ package com.rancard.mobility.contentserver.serviceinterfaces.freemium;
/*  2:   */ 
/*  3:   */ import com.rancard.mobility.infoserver.common.services.ServiceSubscriberDB;
/*  4:   */ import com.rancard.util.DateUtil;
/*  5:   */ import java.util.Date;
/*  6:   */ 
/*  7:   */ class ENDDATEFreemiumSubscriber
/*  8:   */   implements IFreemiumSubscriber
/*  9:   */ {
/* 10:   */   public boolean subscribe(Freemium freemium, String msisdn)
/* 11:   */     throws Exception
/* 12:   */   {
/* 13:19 */     if ((freemium.getEndDate() == null) || (DateUtil.addDaysToDate(freemium.getEndDate(), 1).before(new Date())) || (!freemium.isActive())) {
/* 14:20 */       return false;
/* 15:   */     }
/* 16:   */     try
/* 17:   */     {
/* 18:24 */       ServiceSubscriberDB.addSubscription(new Date(), freemium.getLength(), msisdn, freemium.getAccountID(), freemium.getKeyword(), 1, 0);
/* 19:25 */       return true;
/* 20:   */     }
/* 21:   */     catch (Exception e)
/* 22:   */     {
/* 23:27 */       throw new Exception(e.getMessage());
/* 24:   */     }
/* 25:   */   }
/* 26:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.serviceinterfaces.freemium.ENDDATEFreemiumSubscriber
 * JD-Core Version:    0.7.0.1
 */