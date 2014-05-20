/*  1:   */ package com.rancard.mobility.contentserver.serviceinterfaces.freemium;
/*  2:   */ 
/*  3:   */ import org.apache.commons.lang.NotImplementedException;
/*  4:   */ 
/*  5:   */ public class FreemiumDataSourceProviderFactory
/*  6:   */ {
/*  7:   */   public static FreemiumDataSourceProvider getProvider(FreemiumDataSource dataSource)
/*  8:   */     throws Exception
/*  9:   */   {
/* 10:16 */     if (dataSource.getSourceType() == FreemiumDataSourceType.SQL) {
/* 11:17 */       return new SQLFreemiumDataSourceProvider(dataSource.getSource(), dataSource.getSourceType(), dataSource.getColumn());
/* 12:   */     }
/* 13:19 */     throw new NotImplementedException();
/* 14:   */   }
/* 15:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.serviceinterfaces.freemium.FreemiumDataSourceProviderFactory
 * JD-Core Version:    0.7.0.1
 */