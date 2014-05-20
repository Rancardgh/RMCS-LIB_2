/*   1:    */ package com.rancard.mobility.contentserver;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.HashMap;
/*   5:    */ 
/*   6:    */ public class ServicePricingBean
/*   7:    */ {
/*   8:    */   private String accountId;
/*   9:    */   private String keyword;
/*  10:    */   private String networkPrefix;
/*  11:    */   private String price;
/*  12:    */   private String serviceType;
/*  13:    */   private String shortCode;
/*  14:    */   private String url;
/*  15:    */   
/*  16:    */   public String getAccountId()
/*  17:    */   {
/*  18: 36 */     return this.accountId;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public void setAccountId(String accountId)
/*  22:    */   {
/*  23: 40 */     this.accountId = accountId;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public String getKeyword()
/*  27:    */   {
/*  28: 44 */     return this.keyword;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void setKeyword(String keyword)
/*  32:    */   {
/*  33: 48 */     this.keyword = keyword;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public String getNetworkPrefix()
/*  37:    */   {
/*  38: 52 */     return this.networkPrefix;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void setNetworkPrefix(String networkPrefix)
/*  42:    */   {
/*  43: 56 */     this.networkPrefix = networkPrefix;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public String getPrice()
/*  47:    */   {
/*  48: 60 */     return this.price;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void setPrice(String price)
/*  52:    */   {
/*  53: 64 */     this.price = price;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public String getServiceType()
/*  57:    */   {
/*  58: 68 */     return this.serviceType;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void setServiceType(String serviceType)
/*  62:    */   {
/*  63: 72 */     this.serviceType = serviceType;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public String getShortCode()
/*  67:    */   {
/*  68: 76 */     return this.shortCode;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void setShortCode(String shortCode)
/*  72:    */   {
/*  73: 80 */     this.shortCode = shortCode;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public static void create(String cpId, String keyword, String networkPrefix, String price, String serviceType, String shortCode)
/*  77:    */     throws Exception
/*  78:    */   {
/*  79: 85 */     ServicePricingDB.create(cpId, keyword, networkPrefix, price, serviceType, shortCode);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public static void updatePrice(String price, String cpId, String serviceType, String networkCode)
/*  83:    */     throws Exception
/*  84:    */   {
/*  85: 89 */     ServicePricingDB.updatePrice(price, cpId, serviceType, networkCode);
/*  86:    */   }
/*  87:    */   
/*  88:    */   public static void delete(String cpId, String serviceType, String networkCode)
/*  89:    */     throws Exception
/*  90:    */   {
/*  91:101 */     ServicePricingDB.delete(cpId, serviceType, networkCode);
/*  92:    */   }
/*  93:    */   
/*  94:    */   public static void deleteAllForCP(String cpId)
/*  95:    */     throws Exception
/*  96:    */   {
/*  97:105 */     ServicePricingDB.deleteAllForCP(cpId);
/*  98:    */   }
/*  99:    */   
/* 100:    */   public static ServicePricingBean viewPrice(String accountId, String serviceType, String networkPrefix)
/* 101:    */     throws Exception
/* 102:    */   {
/* 103:113 */     return ServicePricingDB.viewPrice(accountId, serviceType, networkPrefix);
/* 104:    */   }
/* 105:    */   
/* 106:    */   public static ServicePricingBean viewPriceWithUrl(String accountId, String serviceType, String networkPrefix)
/* 107:    */     throws Exception
/* 108:    */   {
/* 109:117 */     return ServicePricingDB.viewPriceWithUrl(accountId, serviceType, networkPrefix);
/* 110:    */   }
/* 111:    */   
/* 112:    */   public static ArrayList viewServicePricesForNetwork(String accountId, String serviceType)
/* 113:    */     throws Exception
/* 114:    */   {
/* 115:121 */     return ServicePricingDB.viewServicePricesForNetwork(accountId, serviceType);
/* 116:    */   }
/* 117:    */   
/* 118:    */   public static ArrayList viewAllForCP(String accountId)
/* 119:    */     throws Exception
/* 120:    */   {
/* 121:125 */     return ServicePricingDB.viewAllForCP(accountId);
/* 122:    */   }
/* 123:    */   
/* 124:    */   public static HashMap viewPricesForCP(String accountId)
/* 125:    */     throws Exception
/* 126:    */   {
/* 127:129 */     return ServicePricingDB.viewPricesForCP(accountId);
/* 128:    */   }
/* 129:    */   
/* 130:    */   public static HashMap viewAllPrices()
/* 131:    */     throws Exception
/* 132:    */   {
/* 133:133 */     return ServicePricingDB.viewAllPrices();
/* 134:    */   }
/* 135:    */   
/* 136:    */   public String getUrl()
/* 137:    */   {
/* 138:137 */     return this.url;
/* 139:    */   }
/* 140:    */   
/* 141:    */   public void setUrl(String url)
/* 142:    */   {
/* 143:141 */     this.url = url;
/* 144:    */   }
/* 145:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.ServicePricingBean
 * JD-Core Version:    0.7.0.1
 */