/*   1:    */ package com.rancard.mobility.infoserver.common.inbox;
/*   2:    */ 
/*   3:    */ import com.rancard.common.uidGen;
/*   4:    */ import java.sql.Timestamp;
/*   5:    */ import java.util.Date;
/*   6:    */ 
/*   7:    */ public class InboxEntry
/*   8:    */ {
/*   9:    */   private String keyword;
/*  10:    */   private String msisdn;
/*  11:    */   private Timestamp dateReceived;
/*  12:    */   private String message;
/*  13:    */   private String messageId;
/*  14:    */   private String shortCode;
/*  15:    */   private String accountId;
/*  16:    */   private int viewed;
/*  17:    */   
/*  18:    */   public InboxEntry()
/*  19:    */   {
/*  20: 18 */     this.keyword = "";
/*  21: 19 */     this.msisdn = "";
/*  22: 20 */     this.dateReceived = new Timestamp(new Date().getTime());
/*  23:    */     
/*  24: 22 */     this.message = "";
/*  25: 23 */     new uidGen();this.messageId = uidGen.generateNumberID(10);
/*  26:    */     
/*  27: 25 */     this.shortCode = "";
/*  28: 26 */     this.accountId = "";
/*  29: 27 */     this.viewed = 0;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public InboxEntry(String keyword, String msisdn, Timestamp dateReceived, String message, String messageId, String shortCode, String provId)
/*  33:    */   {
/*  34: 33 */     this.keyword = keyword;
/*  35: 34 */     this.msisdn = msisdn;
/*  36: 35 */     this.dateReceived = dateReceived;
/*  37: 36 */     this.message = message;
/*  38: 37 */     this.messageId = messageId;
/*  39:    */     
/*  40: 39 */     this.shortCode = shortCode;
/*  41: 40 */     this.accountId = provId;
/*  42: 41 */     this.viewed = 0;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public Timestamp getDateReceived()
/*  46:    */   {
/*  47: 45 */     return this.dateReceived;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public String getMessage()
/*  51:    */   {
/*  52: 49 */     return this.message;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public String getMessageId()
/*  56:    */   {
/*  57: 53 */     return this.messageId;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public String getMsisdn()
/*  61:    */   {
/*  62: 57 */     return this.msisdn;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public String getKeyword()
/*  66:    */   {
/*  67: 65 */     return this.keyword;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public String getShortCode()
/*  71:    */   {
/*  72: 69 */     return this.shortCode;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public String getAccountId()
/*  76:    */   {
/*  77: 73 */     return this.accountId;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void setDateReceived(Timestamp dateReceived)
/*  81:    */   {
/*  82: 77 */     this.dateReceived = dateReceived;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void setMessage(String message)
/*  86:    */   {
/*  87: 81 */     this.message = message;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void setMessageId(String messageId)
/*  91:    */   {
/*  92: 85 */     this.messageId = messageId;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void setMsisdn(String msisdn)
/*  96:    */   {
/*  97: 89 */     this.msisdn = msisdn;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public void setKeyword(String keyword)
/* 101:    */   {
/* 102: 97 */     this.keyword = keyword;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public void setShortCode(String shortCode)
/* 106:    */   {
/* 107:101 */     this.shortCode = shortCode;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public void setAccountId(String providerId)
/* 111:    */   {
/* 112:105 */     this.accountId = providerId;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public int getViewed()
/* 116:    */   {
/* 117:109 */     return this.viewed;
/* 118:    */   }
/* 119:    */   
/* 120:    */   public void setViewed(int viewed)
/* 121:    */   {
/* 122:113 */     this.viewed = viewed;
/* 123:    */   }
/* 124:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.common.inbox.InboxEntry
 * JD-Core Version:    0.7.0.1
 */