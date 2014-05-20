/*  1:   */ package com.rancard.mobility.infoserver.viralmarketing;
/*  2:   */ 
/*  3:   */ public class VMTransaction
/*  4:   */ {
/*  5:   */   private String transactionDate;
/*  6:   */   private String campaignId;
/*  7:   */   private String recruiterMsisdn;
/*  8:   */   private String recipientMsisdn;
/*  9:   */   private String status;
/* 10:   */   
/* 11:   */   public VMTransaction()
/* 12:   */   {
/* 13:21 */     this.transactionDate = "";
/* 14:22 */     this.campaignId = "";
/* 15:23 */     this.recruiterMsisdn = "";
/* 16:24 */     this.recipientMsisdn = "";
/* 17:25 */     this.status = "";
/* 18:   */   }
/* 19:   */   
/* 20:   */   public VMTransaction(String campaignId, String recruiterMsisdn, String recipientMsisdn, String status)
/* 21:   */   {
/* 22:29 */     this.campaignId = campaignId;
/* 23:30 */     this.recruiterMsisdn = recruiterMsisdn;
/* 24:31 */     this.recipientMsisdn = recipientMsisdn;
/* 25:32 */     this.status = status;
/* 26:33 */     this.transactionDate = "";
/* 27:   */   }
/* 28:   */   
/* 29:   */   public String getTransactionDate()
/* 30:   */   {
/* 31:37 */     return this.transactionDate;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public String getCampaignId()
/* 35:   */   {
/* 36:41 */     return this.campaignId;
/* 37:   */   }
/* 38:   */   
/* 39:   */   public String getRecruiterMsisdn()
/* 40:   */   {
/* 41:45 */     return this.recruiterMsisdn;
/* 42:   */   }
/* 43:   */   
/* 44:   */   public String getRecipientMsisdn()
/* 45:   */   {
/* 46:49 */     return this.recipientMsisdn;
/* 47:   */   }
/* 48:   */   
/* 49:   */   public String getStatus()
/* 50:   */   {
/* 51:53 */     return this.status;
/* 52:   */   }
/* 53:   */   
/* 54:   */   public void setTransactionDate(String transactionDate)
/* 55:   */   {
/* 56:57 */     this.transactionDate = transactionDate;
/* 57:   */   }
/* 58:   */   
/* 59:   */   public void setCampaignId(String campaignId)
/* 60:   */   {
/* 61:61 */     this.campaignId = campaignId;
/* 62:   */   }
/* 63:   */   
/* 64:   */   public void setRecruiterMsisdn(String recruiterMsisdn)
/* 65:   */   {
/* 66:65 */     this.recruiterMsisdn = recruiterMsisdn;
/* 67:   */   }
/* 68:   */   
/* 69:   */   public void setRecipientMsisdn(String recipientMsisdn)
/* 70:   */   {
/* 71:69 */     this.recipientMsisdn = recipientMsisdn;
/* 72:   */   }
/* 73:   */   
/* 74:   */   public void setStatus(String status)
/* 75:   */   {
/* 76:73 */     this.status = status;
/* 77:   */   }
/* 78:   */   
/* 79:   */   public boolean isEmptyTransaction()
/* 80:   */   {
/* 81:77 */     if ((this.transactionDate.equals("")) && (this.campaignId.equals("")) && (this.recruiterMsisdn.equals("")) && (this.recipientMsisdn.equals("")) && (this.status.equals(""))) {
/* 82:82 */       return true;
/* 83:   */     }
/* 84:84 */     return false;
/* 85:   */   }
/* 86:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.viralmarketing.VMTransaction
 * JD-Core Version:    0.7.0.1
 */