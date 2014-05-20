/*  1:   */ package com.rancard.mobility.contentserver.serviceinterfaces.freemium;
/*  2:   */ 
/*  3:   */ public abstract class FreemiumDataSourceProviderImpl
/*  4:   */   implements FreemiumDataSourceProvider
/*  5:   */ {
/*  6:   */   private String source;
/*  7:   */   private FreemiumDataSourceType type;
/*  8:   */   
/*  9:   */   public FreemiumDataSourceProviderImpl(String source, FreemiumDataSourceType type)
/* 10:   */   {
/* 11:16 */     this.source = source;
/* 12:17 */     this.type = type;
/* 13:   */   }
/* 14:   */   
/* 15:   */   public FreemiumDataSourceType getProviderType()
/* 16:   */   {
/* 17:21 */     return this.type;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public String getProviderSource()
/* 21:   */   {
/* 22:25 */     return this.source;
/* 23:   */   }
/* 24:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.contentserver.serviceinterfaces.freemium.FreemiumDataSourceProviderImpl
 * JD-Core Version:    0.7.0.1
 */