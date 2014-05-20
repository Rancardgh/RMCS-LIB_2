/*  1:   */ package com.rancard.mobility.infoserver.viralmarketing;
/*  2:   */ 
/*  3:   */ public class VMCampaign
/*  4:   */ {
/*  5:   */   private String campaignId;
/*  6:   */   private String accountId;
/*  7:   */   private String keyword;
/*  8:   */   private String messageSender;
/*  9:   */   private String message;
/* 10:   */   private String lastUpdated;
/* 11:   */   
/* 12:   */   public VMCampaign()
/* 13:   */   {
/* 14:22 */     this.campaignId = "";
/* 15:23 */     this.accountId = "";
/* 16:24 */     this.keyword = "";
/* 17:25 */     this.messageSender = "";
/* 18:26 */     this.message = "";
/* 19:27 */     this.lastUpdated = "";
/* 20:   */   }
/* 21:   */   
/* 22:   */   public VMCampaign(String campaignId, String accountId, String keyword, String messageSender, String message)
/* 23:   */   {
/* 24:31 */     this.campaignId = campaignId;
/* 25:32 */     this.accountId = accountId;
/* 26:33 */     this.keyword = keyword;
/* 27:34 */     this.messageSender = messageSender;
/* 28:35 */     this.message = message;
/* 29:36 */     this.lastUpdated = "";
/* 30:   */   }
/* 31:   */   
/* 32:   */   public String getLastUpdated()
/* 33:   */   {
/* 34:40 */     return this.lastUpdated;
/* 35:   */   }
/* 36:   */   
/* 37:   */   public String getCampaignId()
/* 38:   */   {
/* 39:44 */     return this.campaignId;
/* 40:   */   }
/* 41:   */   
/* 42:   */   public String getAccountId()
/* 43:   */   {
/* 44:48 */     return this.accountId;
/* 45:   */   }
/* 46:   */   
/* 47:   */   public String getKeyword()
/* 48:   */   {
/* 49:52 */     return this.keyword;
/* 50:   */   }
/* 51:   */   
/* 52:   */   public String getMessageSender()
/* 53:   */   {
/* 54:56 */     return this.messageSender;
/* 55:   */   }
/* 56:   */   
/* 57:   */   public String getMessage()
/* 58:   */   {
/* 59:60 */     return this.message;
/* 60:   */   }
/* 61:   */   
/* 62:   */   public void setLastUpdated(String lastUpdated)
/* 63:   */   {
/* 64:64 */     this.lastUpdated = lastUpdated;
/* 65:   */   }
/* 66:   */   
/* 67:   */   public void setCampaignId(String campaignId)
/* 68:   */   {
/* 69:68 */     this.campaignId = campaignId;
/* 70:   */   }
/* 71:   */   
/* 72:   */   public void setAccountId(String accountId)
/* 73:   */   {
/* 74:72 */     this.accountId = accountId;
/* 75:   */   }
/* 76:   */   
/* 77:   */   public void setKeyword(String keyword)
/* 78:   */   {
/* 79:76 */     this.keyword = keyword;
/* 80:   */   }
/* 81:   */   
/* 82:   */   public void setMessageSender(String messageSender)
/* 83:   */   {
/* 84:80 */     this.messageSender = messageSender;
/* 85:   */   }
/* 86:   */   
/* 87:   */   public void setMessage(String message)
/* 88:   */   {
/* 89:84 */     this.message = message;
/* 90:   */   }
/* 91:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.viralmarketing.VMCampaign
 * JD-Core Version:    0.7.0.1
 */