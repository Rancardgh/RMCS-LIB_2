/*  1:   */ package com.rancard.util.payment;
/*  2:   */ 
/*  3:   */ public class PricePoint
/*  4:   */ {
/*  5:   */   private String pricePointId;
/*  6:   */   private String networkPrefix;
/*  7:   */   private String value;
/*  8:   */   private String currency;
/*  9:   */   private String billingMech;
/* 10:   */   private String billingUrl;
/* 11:   */   
/* 12:   */   public PricePoint()
/* 13:   */   {
/* 14:27 */     this.pricePointId = "";
/* 15:28 */     this.networkPrefix = "";
/* 16:29 */     this.value = "";
/* 17:30 */     this.currency = "";
/* 18:31 */     this.billingMech = "1";
/* 19:32 */     this.billingUrl = "";
/* 20:   */   }
/* 21:   */   
/* 22:   */   public PricePoint(String pricePointId, String networkPrefix, String value, String currenc, String billingMech, String billingUrl)
/* 23:   */   {
/* 24:37 */     this.pricePointId = pricePointId;
/* 25:38 */     this.networkPrefix = networkPrefix;
/* 26:39 */     this.value = value;
/* 27:40 */     this.currency = this.currency;
/* 28:41 */     this.billingMech = billingMech;
/* 29:42 */     this.billingUrl = billingUrl;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public String getPricePointId()
/* 33:   */   {
/* 34:46 */     return this.pricePointId;
/* 35:   */   }
/* 36:   */   
/* 37:   */   public void setPricePointId(String pricePointId)
/* 38:   */   {
/* 39:50 */     this.pricePointId = pricePointId;
/* 40:   */   }
/* 41:   */   
/* 42:   */   public String getNetworkPrefix()
/* 43:   */   {
/* 44:54 */     return this.networkPrefix;
/* 45:   */   }
/* 46:   */   
/* 47:   */   public void setNetworkPrefix(String networkPrefix)
/* 48:   */   {
/* 49:58 */     this.networkPrefix = networkPrefix;
/* 50:   */   }
/* 51:   */   
/* 52:   */   public String getValue()
/* 53:   */   {
/* 54:62 */     return this.value;
/* 55:   */   }
/* 56:   */   
/* 57:   */   public void setValue(String value)
/* 58:   */   {
/* 59:66 */     this.value = value;
/* 60:   */   }
/* 61:   */   
/* 62:   */   public String getCurrency()
/* 63:   */   {
/* 64:70 */     return this.currency;
/* 65:   */   }
/* 66:   */   
/* 67:   */   public void setCurrency(String currency)
/* 68:   */   {
/* 69:74 */     this.currency = currency;
/* 70:   */   }
/* 71:   */   
/* 72:   */   public String getBillingUrl()
/* 73:   */   {
/* 74:78 */     return this.billingUrl;
/* 75:   */   }
/* 76:   */   
/* 77:   */   public void setBillingUrl(String billingUrl)
/* 78:   */   {
/* 79:82 */     this.billingUrl = billingUrl;
/* 80:   */   }
/* 81:   */   
/* 82:   */   public String getBillingMech()
/* 83:   */   {
/* 84:86 */     return this.billingMech;
/* 85:   */   }
/* 86:   */   
/* 87:   */   public void setBillingMech(String billingMech)
/* 88:   */   {
/* 89:90 */     this.billingMech = billingMech;
/* 90:   */   }
/* 91:   */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.util.payment.PricePoint
 * JD-Core Version:    0.7.0.1
 */