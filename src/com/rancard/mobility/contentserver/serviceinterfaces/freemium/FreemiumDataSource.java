/*  1:   */ package com.rancard.mobility.contentserver.serviceinterfaces.freemium;
/*  2:   */ 
/*  3:   */ public class FreemiumDataSource
/*  4:   */ {
/*  5:   */   private Freemium freemium;
/*  6:   */   private FreemiumDataSourceType sourceType;
/*  7:   */   private String source;
/*  8:   */   private String column;
/*  9:   */   private String delimiter;
/* 10:   */   private boolean notIn;
/* 11:   */   
/* 12:   */   public FreemiumDataSource(Freemium freemium, FreemiumDataSourceType sourceType, String source, String column, String delimiter, boolean notIn)
/* 13:   */   {
/* 14:20 */     this.freemium = freemium;
/* 15:21 */     this.column = column;
/* 16:22 */     this.delimiter = delimiter;
/* 17:23 */     this.sourceType = sourceType;
/* 18:24 */     this.source = source;
/* 19:25 */     this.notIn = notIn;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public Freemium getFreemium()
/* 23:   */   {
/* 24:29 */     return this.freemium;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public FreemiumDataSourceType getSourceType()
/* 28:   */   {
/* 29:33 */     return this.sourceType;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public String getSource()
/* 33:   */   {
/* 34:37 */     return this.source;
/* 35:   */   }
/* 36:   */   
/* 37:   */   public String getColumn()
/* 38:   */   {
/* 39:41 */     return this.column;
/* 40:   */   }
/* 41:   */   
/* 42:   */   public String getDelimiter()
/* 43:   */   {
/* 44:45 */     return this.delimiter;
/* 45:   */   }
/* 46:   */   
/* 47:   */   public boolean isNotIn()
/* 48:   */   {
/* 49:50 */     return this.notIn;
/* 50:   */   }
/* 51:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.contentserver.serviceinterfaces.freemium.FreemiumDataSource
 * JD-Core Version:    0.7.0.1
 */