/*  1:   */ package com.rancard.mobility.contentserver.serviceinterfaces.freemium;
/*  2:   */ 
/*  3:   */ import com.rancard.mobility.infoserver.common.services.ServiceSubscriberDB;
/*  4:   */ import java.util.Date;
/*  5:   */ 
/*  6:   */ public class DURATIONFreemiumSubscriber
/*  7:   */   implements IFreemiumSubscriber
/*  8:   */ {
/*  9:   */   public boolean subscribe(Freemium freemium, String msisdn)
/* 10:   */     throws Exception
/* 11:   */   {
/* 12:17 */     if (freemium.isActive())
/* 13:   */     {
/* 14:18 */       ServiceSubscriberDB.addSubscription(new Date(), freemium.getLength(), msisdn, freemium.getAccountID(), freemium.getKeyword(), 1, 0);
/* 15:19 */       return true;
/* 16:   */     }
/* 17:21 */     return false;
/* 18:   */   }
/* 19:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.contentserver.serviceinterfaces.freemium.DURATIONFreemiumSubscriber
 * JD-Core Version:    0.7.0.1
 */