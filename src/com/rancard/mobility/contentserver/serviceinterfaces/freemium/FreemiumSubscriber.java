/*  1:   */ package com.rancard.mobility.contentserver.serviceinterfaces.freemium;
/*  2:   */ 
/*  3:   */ public class FreemiumSubscriber
/*  4:   */   implements IFreemiumSubscriber
/*  5:   */ {
/*  6:   */   public boolean subscribe(Freemium freemium, String msisdn)
/*  7:   */     throws Exception
/*  8:   */   {
/*  9:14 */     if (freemium.getType() == FreemiumType.ENDDATE) {
/* 10:15 */       return new ENDDATEFreemiumSubscriber().subscribe(freemium, msisdn);
/* 11:   */     }
/* 12:16 */     if (freemium.getType() == FreemiumType.CAPPEDATENDDATE) {
/* 13:17 */       return new CAPPEDATENDDATEFreemiumSubscriber().subscribe(freemium, msisdn);
/* 14:   */     }
/* 15:18 */     if (freemium.getType() == FreemiumType.DURATION) {
/* 16:19 */       return new DURATIONFreemiumSubscriber().subscribe(freemium, msisdn);
/* 17:   */     }
/* 18:21 */     return false;
/* 19:   */   }
/* 20:   */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.serviceinterfaces.freemium.FreemiumSubscriber
 * JD-Core Version:    0.7.0.1
 */