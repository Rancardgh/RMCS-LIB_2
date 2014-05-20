/*   1:    */ package com.rancard.mobility.infoserver.viralmarketing;
/*   2:    */ 
/*   3:    */ import java.util.Date;
/*   4:    */ 
/*   5:    */ public class PromoImpression
/*   6:    */ {
/*   7:    */   private long hashCode;
/*   8:    */   private String msisdn;
/*   9:    */   private String promoResponseCode;
/*  10:    */   private String accountId;
/*  11:    */   private Date viewDate;
/*  12:    */   private String inventory_keyword;
/*  13:    */   
/*  14:    */   public PromoImpression()
/*  15:    */   {
/*  16: 21 */     this.hashCode = 0L;
/*  17: 22 */     this.msisdn = "";
/*  18: 23 */     this.promoResponseCode = "";
/*  19: 24 */     this.accountId = "";
/*  20: 25 */     this.inventory_keyword = "";
/*  21: 26 */     this.viewDate = new Date();
/*  22:    */   }
/*  23:    */   
/*  24:    */   public boolean exists()
/*  25:    */   {
/*  26: 31 */     if (getHashCode() == 0L) {
/*  27: 32 */       return false;
/*  28:    */     }
/*  29: 34 */     return true;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public long getHashCode()
/*  33:    */   {
/*  34: 41 */     return this.hashCode;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void setHashCode(long hashCode)
/*  38:    */   {
/*  39: 48 */     this.hashCode = hashCode;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public String getMsisdn()
/*  43:    */   {
/*  44: 55 */     return this.msisdn;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void setMsisdn(String msisdn)
/*  48:    */   {
/*  49: 62 */     this.msisdn = msisdn;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public String getKeyword()
/*  53:    */   {
/*  54: 69 */     return this.promoResponseCode;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void setKeyword(String promoResponseCode)
/*  58:    */   {
/*  59: 76 */     this.promoResponseCode = promoResponseCode;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public String getAccountId()
/*  63:    */   {
/*  64: 83 */     return this.accountId;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void setAccountId(String accountId)
/*  68:    */   {
/*  69: 90 */     this.accountId = accountId;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public Date getViewDate()
/*  73:    */   {
/*  74: 97 */     return this.viewDate;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public void setViewDate(Date viewDate)
/*  78:    */   {
/*  79:104 */     this.viewDate = viewDate;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public String getInventory_keyword()
/*  83:    */   {
/*  84:111 */     return this.inventory_keyword;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public void setInventory_keyword(String inventory_keyword)
/*  88:    */   {
/*  89:118 */     this.inventory_keyword = inventory_keyword;
/*  90:    */   }
/*  91:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.infoserver.viralmarketing.PromoImpression
 * JD-Core Version:    0.7.0.1
 */