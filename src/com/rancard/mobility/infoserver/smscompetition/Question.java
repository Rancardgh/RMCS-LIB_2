/*  1:   */ package com.rancard.mobility.infoserver.smscompetition;
/*  2:   */ 
/*  3:   */ public class Question
/*  4:   */ {
/*  5:   */   private String question;
/*  6:   */   private String questionId;
/*  7:   */   private String keyword;
/*  8:   */   private String accountId;
/*  9:   */   
/* 10:   */   public Question()
/* 11:   */   {
/* 12:11 */     this.question = "";
/* 13:12 */     this.questionId = "";
/* 14:13 */     this.keyword = "";
/* 15:14 */     this.accountId = "";
/* 16:   */   }
/* 17:   */   
/* 18:   */   public Question(String questionId, String question, String keyword, String accountId)
/* 19:   */   {
/* 20:18 */     this.question = question;
/* 21:19 */     this.questionId = questionId;
/* 22:20 */     this.keyword = keyword;
/* 23:21 */     this.accountId = accountId;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public String getQuestionId()
/* 27:   */   {
/* 28:25 */     return this.questionId;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public String getQuestion()
/* 32:   */   {
/* 33:29 */     return this.question;
/* 34:   */   }
/* 35:   */   
/* 36:   */   public String getKeyword()
/* 37:   */   {
/* 38:33 */     return this.keyword;
/* 39:   */   }
/* 40:   */   
/* 41:   */   public String getAccountId()
/* 42:   */   {
/* 43:37 */     return this.accountId;
/* 44:   */   }
/* 45:   */   
/* 46:   */   public void setQuestion(String question)
/* 47:   */   {
/* 48:41 */     this.question = question;
/* 49:   */   }
/* 50:   */   
/* 51:   */   public void setQuestionId(String questionId)
/* 52:   */   {
/* 53:45 */     this.questionId = questionId;
/* 54:   */   }
/* 55:   */   
/* 56:   */   public void setKeyword(String keyword)
/* 57:   */   {
/* 58:49 */     this.keyword = keyword;
/* 59:   */   }
/* 60:   */   
/* 61:   */   public void setAccountId(String accountId)
/* 62:   */   {
/* 63:53 */     this.accountId = accountId;
/* 64:   */   }
/* 65:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.infoserver.smscompetition.Question
 * JD-Core Version:    0.7.0.1
 */