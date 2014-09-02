/*  1:   */ package com.rancard.mobility.infoserver.smscompetition;
/*  2:   */ 
/*  3:   */ public class Answer
/*  4:   */ {
/*  5:   */   private String questionId;
/*  6:   */   private String optionId;
/*  7:   */   private String keyword;
/*  8:   */   private String accountId;
/*  9:   */   private String description;
/* 10:   */   
/* 11:   */   public Answer()
/* 12:   */   {
/* 13:12 */     this.questionId = "";
/* 14:13 */     this.optionId = "";
/* 15:14 */     this.keyword = "";
/* 16:15 */     this.accountId = "";
/* 17:16 */     this.description = "";
/* 18:   */   }
/* 19:   */   
/* 20:   */   public Answer(String questionid, String optionid, String desc, String keyword, String accountId)
/* 21:   */   {
/* 22:20 */     this.questionId = questionid;
/* 23:21 */     this.optionId = optionid;
/* 24:22 */     this.keyword = keyword;
/* 25:23 */     this.accountId = accountId;
/* 26:24 */     this.description = desc;
/* 27:   */   }
/* 28:   */   
/* 29:   */   public void setQuestionId(String questionId)
/* 30:   */   {
/* 31:28 */     this.questionId = questionId;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public void setOptionId(String optionId)
/* 35:   */   {
/* 36:32 */     this.optionId = optionId;
/* 37:   */   }
/* 38:   */   
/* 39:   */   public void setKeyword(String keyword)
/* 40:   */   {
/* 41:36 */     this.keyword = keyword;
/* 42:   */   }
/* 43:   */   
/* 44:   */   public void setAccountId(String accountId)
/* 45:   */   {
/* 46:40 */     this.accountId = accountId;
/* 47:   */   }
/* 48:   */   
/* 49:   */   public void setDescription(String description)
/* 50:   */   {
/* 51:44 */     this.description = description;
/* 52:   */   }
/* 53:   */   
/* 54:   */   public String getQuestionId()
/* 55:   */   {
/* 56:48 */     return this.questionId;
/* 57:   */   }
/* 58:   */   
/* 59:   */   public String getOptionId()
/* 60:   */   {
/* 61:52 */     return this.optionId;
/* 62:   */   }
/* 63:   */   
/* 64:   */   public String getKeyword()
/* 65:   */   {
/* 66:56 */     return this.keyword;
/* 67:   */   }
/* 68:   */   
/* 69:   */   public String getAccountId()
/* 70:   */   {
/* 71:60 */     return this.accountId;
/* 72:   */   }
/* 73:   */   
/* 74:   */   public String getDescription()
/* 75:   */   {
/* 76:64 */     return this.description;
/* 77:   */   }
/* 78:   */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.smscompetition.Answer
 * JD-Core Version:    0.7.0.1
 */