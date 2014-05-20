/*  1:   */ package com.rancard.mobility.infoserver.feeds;
/*  2:   */ 
/*  3:   */ public class CPUserFeeds
/*  4:   */ {
/*  5:17 */   private String feedId = "";
/*  6:18 */   private String URL = "";
/*  7:19 */   private String keyword = "";
/*  8:20 */   private String cpUserId = "";
/*  9:21 */   private int allowedAge = 0;
/* 10:22 */   private String regexReject = "";
/* 11:   */   private int msgDlrPrority;
/* 12:   */   
/* 13:   */   public String getFeedId()
/* 14:   */   {
/* 15:31 */     return this.feedId;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public void setFeedId(String feedId)
/* 19:   */   {
/* 20:35 */     this.feedId = feedId;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public String getURL()
/* 24:   */   {
/* 25:39 */     return this.URL;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public void setURL(String URL)
/* 29:   */   {
/* 30:43 */     this.URL = URL;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public void setAllowedAge(int allowedAge)
/* 34:   */   {
/* 35:47 */     this.allowedAge = allowedAge;
/* 36:   */   }
/* 37:   */   
/* 38:   */   public int getAllowedAge()
/* 39:   */   {
/* 40:51 */     return this.allowedAge;
/* 41:   */   }
/* 42:   */   
/* 43:   */   public String getKeyword()
/* 44:   */   {
/* 45:55 */     return this.keyword;
/* 46:   */   }
/* 47:   */   
/* 48:   */   public void setKeyword(String keyword)
/* 49:   */   {
/* 50:59 */     this.keyword = keyword;
/* 51:   */   }
/* 52:   */   
/* 53:   */   public String getCpUserId()
/* 54:   */   {
/* 55:63 */     return this.cpUserId;
/* 56:   */   }
/* 57:   */   
/* 58:   */   public void setCpUserId(String cpUserId)
/* 59:   */   {
/* 60:67 */     this.cpUserId = cpUserId;
/* 61:   */   }
/* 62:   */   
/* 63:   */   public String getRegexReject()
/* 64:   */   {
/* 65:71 */     return this.regexReject;
/* 66:   */   }
/* 67:   */   
/* 68:   */   public void setRegexReject(String regexReject)
/* 69:   */   {
/* 70:75 */     this.regexReject = regexReject;
/* 71:   */   }
/* 72:   */   
/* 73:   */   public int getMsgDlrPrority()
/* 74:   */   {
/* 75:82 */     return this.msgDlrPrority;
/* 76:   */   }
/* 77:   */   
/* 78:   */   public void setMsgDlrPrority(int msgDlrPrority)
/* 79:   */   {
/* 80:89 */     this.msgDlrPrority = msgDlrPrority;
/* 81:   */   }
/* 82:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.infoserver.feeds.CPUserFeeds
 * JD-Core Version:    0.7.0.1
 */