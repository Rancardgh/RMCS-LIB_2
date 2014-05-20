/*  1:   */ package com.rancard.mobility.contentserver.serviceinterfaces.freemium;
/*  2:   */ 
/*  3:   */ import com.rancard.mobility.infoserver.common.services.ServiceSubscriberDB;
/*  4:   */ import com.rancard.util.DateUtil;
/*  5:   */ import java.util.Date;
/*  6:   */ 
/*  7:   */ public class CAPPEDATENDDATEFreemiumSubscriber
/*  8:   */   implements IFreemiumSubscriber
/*  9:   */ {
/* 10:   */   public boolean subscribe(Freemium freemium, String msisdn)
/* 11:   */     throws Exception
/* 12:   */   {
/* 13:18 */     if ((freemium.getEndDate() == null) || (DateUtil.addDaysToDate(freemium.getEndDate(), 1).before(new Date())) || (!freemium.isActive())) {
/* 14:19 */       return false;
/* 15:   */     }
/* 16:21 */     if (DateUtil.daysBtnDates(freemium.getStartDate(), freemium.getEndDate()) < freemium.getLength())
/* 17:   */     {
/* 18:22 */       ServiceSubscriberDB.addSubscription(new Date(), freemium.getEndDate(), msisdn, freemium.getAccountID(), freemium.getKeyword(), 1, 0);
/* 19:   */       
/* 20:24 */       return true;
/* 21:   */     }
/* 22:26 */     ServiceSubscriberDB.addSubscription(new Date(), freemium.getLength(), msisdn, freemium.getAccountID(), freemium.getKeyword(), 1, 0);
/* 23:27 */     return true;
/* 24:   */   }
/* 25:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.contentserver.serviceinterfaces.freemium.CAPPEDATENDDATEFreemiumSubscriber
 * JD-Core Version:    0.7.0.1
 */