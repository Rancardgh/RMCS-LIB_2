/*  1:   */ package com.rancard.mobility.infoserver.viralmarketing;
/*  2:   */ 
/*  3:   */ public class VMUser
/*  4:   */ {
/*  5:   */   private String msisdn;
/*  6:   */   private String accountId;
/*  7:   */   private String keyword;
/*  8:   */   private String username;
/*  9:   */   private String regDate;
/* 10:   */   private int points;
/* 11:   */   
/* 12:   */   public VMUser()
/* 13:   */   {
/* 14:22 */     this.accountId = "";
/* 15:23 */     this.msisdn = "";
/* 16:24 */     this.keyword = "";
/* 17:25 */     this.username = "";
/* 18:26 */     this.regDate = "";
/* 19:27 */     this.points = 0;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public VMUser(String msisdn, String accountId, String keyword, String username, int points)
/* 23:   */   {
/* 24:31 */     this.accountId = accountId;
/* 25:32 */     this.msisdn = msisdn;
/* 26:33 */     this.keyword = keyword;
/* 27:34 */     this.username = username;
/* 28:35 */     this.points = points;
/* 29:36 */     this.regDate = "";
/* 30:   */   }
/* 31:   */   
/* 32:   */   public String getAccountId()
/* 33:   */   {
/* 34:40 */     return this.accountId;
/* 35:   */   }
/* 36:   */   
/* 37:   */   public String getMsisdn()
/* 38:   */   {
/* 39:44 */     return this.msisdn;
/* 40:   */   }
/* 41:   */   
/* 42:   */   public String getKeyword()
/* 43:   */   {
/* 44:48 */     return this.keyword;
/* 45:   */   }
/* 46:   */   
/* 47:   */   public String getUsername()
/* 48:   */   {
/* 49:52 */     return this.username;
/* 50:   */   }
/* 51:   */   
/* 52:   */   public String getRegDate()
/* 53:   */   {
/* 54:56 */     return this.regDate;
/* 55:   */   }
/* 56:   */   
/* 57:   */   public int getPoints()
/* 58:   */   {
/* 59:60 */     return this.points;
/* 60:   */   }
/* 61:   */   
/* 62:   */   public void setAccountId(String accountId)
/* 63:   */   {
/* 64:64 */     this.accountId = accountId;
/* 65:   */   }
/* 66:   */   
/* 67:   */   public void setMsisdn(String msisdn)
/* 68:   */   {
/* 69:68 */     this.msisdn = msisdn;
/* 70:   */   }
/* 71:   */   
/* 72:   */   public void setKeyword(String keyword)
/* 73:   */   {
/* 74:72 */     this.keyword = keyword;
/* 75:   */   }
/* 76:   */   
/* 77:   */   public void setUsername(String username)
/* 78:   */   {
/* 79:76 */     this.username = username;
/* 80:   */   }
/* 81:   */   
/* 82:   */   public void setRegDate(String regDate)
/* 83:   */   {
/* 84:80 */     this.regDate = regDate;
/* 85:   */   }
/* 86:   */   
/* 87:   */   public void setPoints(int points)
/* 88:   */   {
/* 89:84 */     this.points = points;
/* 90:   */   }
/* 91:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.viralmarketing.VMUser
 * JD-Core Version:    0.7.0.1
 */