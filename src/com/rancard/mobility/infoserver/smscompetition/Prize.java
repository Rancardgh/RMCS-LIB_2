/*  1:   */ package com.rancard.mobility.infoserver.smscompetition;
/*  2:   */ 
/*  3:   */ public class Prize
/*  4:   */ {
/*  5:   */   private String prizeId;
/*  6:   */   private String description;
/*  7:   */   private String winnerMSISDN;
/*  8:   */   private String thumbnailURL;
/*  9:   */   private String keyword;
/* 10:   */   private String accountId;
/* 11:   */   private String questionId;
/* 12:   */   
/* 13:   */   public Prize()
/* 14:   */   {
/* 15:10 */     this.prizeId = "";
/* 16:11 */     this.description = "";
/* 17:12 */     this.winnerMSISDN = "";
/* 18:13 */     this.thumbnailURL = "";
/* 19:14 */     this.keyword = "";
/* 20:15 */     this.accountId = "";
/* 21:16 */     this.questionId = "";
/* 22:   */   }
/* 23:   */   
/* 24:   */   public Prize(String id, String desc, String winnerMSISDN, String thumbnailURL, String keyword, String accountId, String questionid)
/* 25:   */   {
/* 26:22 */     this.prizeId = id;
/* 27:23 */     this.description = desc;
/* 28:24 */     this.winnerMSISDN = winnerMSISDN;
/* 29:25 */     this.thumbnailURL = thumbnailURL;
/* 30:26 */     this.keyword = keyword;
/* 31:27 */     this.accountId = accountId;
/* 32:28 */     this.questionId = questionid;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public void setPrizeId(String id)
/* 36:   */   {
/* 37:33 */     this.prizeId = id;
/* 38:   */   }
/* 39:   */   
/* 40:   */   public void setDescription(String description)
/* 41:   */   {
/* 42:37 */     this.description = description;
/* 43:   */   }
/* 44:   */   
/* 45:   */   public void setWinnerMSISDN(String mobNumber)
/* 46:   */   {
/* 47:41 */     this.winnerMSISDN = mobNumber;
/* 48:   */   }
/* 49:   */   
/* 50:   */   public void setThumbnailURL(String url)
/* 51:   */   {
/* 52:45 */     this.thumbnailURL = url;
/* 53:   */   }
/* 54:   */   
/* 55:   */   public void setKeyword(String keyword)
/* 56:   */   {
/* 57:49 */     this.keyword = keyword;
/* 58:   */   }
/* 59:   */   
/* 60:   */   public void setAccountId(String id)
/* 61:   */   {
/* 62:53 */     this.accountId = id;
/* 63:   */   }
/* 64:   */   
/* 65:   */   public void setQuestionId(String questionId)
/* 66:   */   {
/* 67:57 */     this.questionId = questionId;
/* 68:   */   }
/* 69:   */   
/* 70:   */   public String getPrizeId()
/* 71:   */   {
/* 72:63 */     return this.prizeId;
/* 73:   */   }
/* 74:   */   
/* 75:   */   public String getDescription()
/* 76:   */   {
/* 77:67 */     return this.description;
/* 78:   */   }
/* 79:   */   
/* 80:   */   public String getWinnerMSISDN()
/* 81:   */   {
/* 82:71 */     return this.winnerMSISDN;
/* 83:   */   }
/* 84:   */   
/* 85:   */   public String getThumbnailURL()
/* 86:   */   {
/* 87:75 */     return this.thumbnailURL;
/* 88:   */   }
/* 89:   */   
/* 90:   */   public String getKeyword()
/* 91:   */   {
/* 92:79 */     return this.keyword;
/* 93:   */   }
/* 94:   */   
/* 95:   */   public String getAccountId()
/* 96:   */   {
/* 97:83 */     return this.accountId;
/* 98:   */   }
/* 99:   */   
/* :0:   */   public String getQuestionId()
/* :1:   */   {
/* :2:87 */     return this.questionId;
/* :3:   */   }
/* :4:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.infoserver.smscompetition.Prize
 * JD-Core Version:    0.7.0.1
 */