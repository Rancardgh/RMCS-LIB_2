/*  1:   */ package com.rancard.mobility.infoserver.smscompetition;
/*  2:   */ 
/*  3:   */ public class Option
/*  4:   */ {
/*  5:   */   private String keyword;
/*  6:   */   private String accountId;
/*  7:   */   private String description;
/*  8:   */   private String optionId;
/*  9:   */   private String questionId;
/* 10:   */   private String percVoted;
/* 11:   */   
/* 12:   */   public Option()
/* 13:   */   {
/* 14:13 */     this.keyword = "";
/* 15:14 */     this.accountId = "";
/* 16:15 */     this.description = "";
/* 17:16 */     this.optionId = "";
/* 18:17 */     this.questionId = "";
/* 19:18 */     this.percVoted = "";
/* 20:   */   }
/* 21:   */   
/* 22:   */   public Option(String keyword, String accountId, String desc, String optId, String qusId, String percVote)
/* 23:   */   {
/* 24:22 */     keyword = keyword;
/* 25:23 */     accountId = accountId;
/* 26:24 */     this.description = desc;
/* 27:25 */     this.optionId = optId;
/* 28:26 */     this.questionId = qusId;
/* 29:27 */     this.percVoted = percVote;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public void setKeyword(String keyword)
/* 33:   */   {
/* 34:32 */     this.keyword = keyword;
/* 35:   */   }
/* 36:   */   
/* 37:   */   public void setAccountId(String accountId)
/* 38:   */   {
/* 39:36 */     this.accountId = accountId;
/* 40:   */   }
/* 41:   */   
/* 42:   */   public void setDescription(String description)
/* 43:   */   {
/* 44:40 */     this.description = description;
/* 45:   */   }
/* 46:   */   
/* 47:   */   public void setOptionId(String optionId)
/* 48:   */   {
/* 49:44 */     this.optionId = optionId;
/* 50:   */   }
/* 51:   */   
/* 52:   */   public void setQuestionId(String questionId)
/* 53:   */   {
/* 54:48 */     this.questionId = questionId;
/* 55:   */   }
/* 56:   */   
/* 57:   */   public void setPercVoted(String percVoted)
/* 58:   */   {
/* 59:52 */     this.percVoted = percVoted;
/* 60:   */   }
/* 61:   */   
/* 62:   */   public String getKeyword()
/* 63:   */   {
/* 64:56 */     return this.keyword;
/* 65:   */   }
/* 66:   */   
/* 67:   */   public String getAccountId()
/* 68:   */   {
/* 69:60 */     return this.accountId;
/* 70:   */   }
/* 71:   */   
/* 72:   */   public String getDescription()
/* 73:   */   {
/* 74:64 */     return this.description;
/* 75:   */   }
/* 76:   */   
/* 77:   */   public String getOptionId()
/* 78:   */   {
/* 79:68 */     return this.optionId;
/* 80:   */   }
/* 81:   */   
/* 82:   */   public String getQuestionId()
/* 83:   */   {
/* 84:72 */     return this.questionId;
/* 85:   */   }
/* 86:   */   
/* 87:   */   public String getPercVoted()
/* 88:   */   {
/* 89:76 */     return this.percVoted;
/* 90:   */   }
/* 91:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.smscompetition.Option
 * JD-Core Version:    0.7.0.1
 */