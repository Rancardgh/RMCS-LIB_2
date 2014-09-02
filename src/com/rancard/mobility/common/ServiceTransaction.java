/*   1:    */ package com.rancard.mobility.common;
/*   2:    */ 
/*   3:    */ import java.sql.Timestamp;
/*   4:    */ import java.util.Date;
/*   5:    */ 
/*   6:    */ public abstract class ServiceTransaction
/*   7:    */ {
/*   8:    */   private String transactionId;
/*   9:    */   private String keyword;
/*  10:    */   private String accountId;
/*  11:    */   private String msg;
/*  12:    */   private String msisdn;
/*  13:    */   private String callBackUrl;
/*  14:    */   private Timestamp date;
/*  15:    */   private String pricePoint;
/*  16:    */   private int isBilled;
/*  17:    */   private int isCompleted;
/*  18:    */   
/*  19:    */   public ServiceTransaction()
/*  20:    */   {
/*  21: 31 */     this.transactionId = "";
/*  22: 32 */     this.keyword = "";
/*  23: 33 */     this.accountId = "";
/*  24: 34 */     this.msg = "";
/*  25: 35 */     this.msisdn = "";
/*  26: 36 */     this.date = new Timestamp(new Date().getTime());
/*  27: 37 */     this.pricePoint = "";
/*  28: 38 */     this.isBilled = 0;
/*  29: 39 */     this.isCompleted = 0;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public ServiceTransaction(String transactionId, String keyword, String accountId, String msg, String msisdn, String callBackUrl, Timestamp date, String pricePoint, int isBilled, int isCompleted)
/*  33:    */   {
/*  34: 45 */     this.transactionId = transactionId;
/*  35: 46 */     this.keyword = keyword;
/*  36: 47 */     this.accountId = accountId;
/*  37: 48 */     this.msg = msg;
/*  38: 49 */     this.msisdn = msisdn;
/*  39: 50 */     this.callBackUrl = callBackUrl;
/*  40: 51 */     this.date = date;
/*  41: 52 */     this.pricePoint = pricePoint;
/*  42: 53 */     this.isBilled = isBilled;
/*  43: 54 */     this.isCompleted = isCompleted;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public String getTransactionId()
/*  47:    */   {
/*  48: 58 */     return this.transactionId;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void setTransactionId(String transactionId)
/*  52:    */   {
/*  53: 62 */     this.transactionId = transactionId;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public String getKeyword()
/*  57:    */   {
/*  58: 66 */     return this.keyword;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void setKeyword(String keyword)
/*  62:    */   {
/*  63: 70 */     this.keyword = keyword;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public String getAccountId()
/*  67:    */   {
/*  68: 74 */     return this.accountId;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void setAccountId(String accountId)
/*  72:    */   {
/*  73: 78 */     this.accountId = accountId;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public String getMsg()
/*  77:    */   {
/*  78: 82 */     return this.msg;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public void setMsg(String msg)
/*  82:    */   {
/*  83: 86 */     this.msg = msg;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public String getMsisdn()
/*  87:    */   {
/*  88: 90 */     return this.msisdn;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void setMsisdn(String msisdn)
/*  92:    */   {
/*  93: 94 */     this.msisdn = msisdn;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public Timestamp getDate()
/*  97:    */   {
/*  98: 98 */     return this.date;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public void setDate(Timestamp date)
/* 102:    */   {
/* 103:102 */     this.date = date;
/* 104:    */   }
/* 105:    */   
/* 106:    */   public int getIsBilled()
/* 107:    */   {
/* 108:106 */     return this.isBilled;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public void setIsBilled(int isBilled)
/* 112:    */   {
/* 113:110 */     this.isBilled = isBilled;
/* 114:    */   }
/* 115:    */   
/* 116:    */   public int getIsCompleted()
/* 117:    */   {
/* 118:114 */     return this.isCompleted;
/* 119:    */   }
/* 120:    */   
/* 121:    */   public void setIsCompleted(int isCompleted)
/* 122:    */   {
/* 123:118 */     this.isCompleted = isCompleted;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public String getCallBackUrl()
/* 127:    */   {
/* 128:122 */     return this.callBackUrl;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public void setCallBackUrl(String callBackUrl)
/* 132:    */   {
/* 133:126 */     this.callBackUrl = callBackUrl;
/* 134:    */   }
/* 135:    */   
/* 136:    */   public String getPricePoint()
/* 137:    */   {
/* 138:130 */     return this.pricePoint;
/* 139:    */   }
/* 140:    */   
/* 141:    */   public void setPricePoint(String pricePoint)
/* 142:    */   {
/* 143:134 */     this.pricePoint = pricePoint;
/* 144:    */   }
/* 145:    */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.common.ServiceTransaction
 * JD-Core Version:    0.7.0.1
 */