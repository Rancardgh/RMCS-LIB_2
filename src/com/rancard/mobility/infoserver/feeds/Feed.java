/*  1:   */ package com.rancard.mobility.infoserver.feeds;
/*  2:   */ 
/*  3:   */ import java.net.URL;
/*  4:   */ 
/*  5:   */ public class Feed
/*  6:   */ {
/*  7:   */   private String feedId;
/*  8:   */   private URL feedURL;
/*  9:   */   private String feedName;
/* 10:   */   private boolean isActive;
/* 11:   */   private String username;
/* 12:   */   private String password;
/* 13:   */   
/* 14:   */   public String getFeedId()
/* 15:   */   {
/* 16:27 */     return this.feedId;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public void setFeedId(String feedId)
/* 20:   */   {
/* 21:31 */     this.feedId = feedId;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public URL getFeedURL()
/* 25:   */   {
/* 26:35 */     return this.feedURL;
/* 27:   */   }
/* 28:   */   
/* 29:   */   public void setFeedURL(URL feedURL)
/* 30:   */   {
/* 31:39 */     this.feedURL = feedURL;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public String getFeedName()
/* 35:   */   {
/* 36:43 */     return this.feedName;
/* 37:   */   }
/* 38:   */   
/* 39:   */   public void setFeedName(String feedName)
/* 40:   */   {
/* 41:47 */     this.feedName = feedName;
/* 42:   */   }
/* 43:   */   
/* 44:   */   public String getUsername()
/* 45:   */   {
/* 46:51 */     return this.username;
/* 47:   */   }
/* 48:   */   
/* 49:   */   public void setUsername(String username)
/* 50:   */   {
/* 51:55 */     this.username = username;
/* 52:   */   }
/* 53:   */   
/* 54:   */   public String getPassword()
/* 55:   */   {
/* 56:59 */     return this.password;
/* 57:   */   }
/* 58:   */   
/* 59:   */   public void setPassword(String password)
/* 60:   */   {
/* 61:63 */     this.password = password;
/* 62:   */   }
/* 63:   */   
/* 64:   */   public boolean isActive()
/* 65:   */   {
/* 66:67 */     return this.isActive;
/* 67:   */   }
/* 68:   */   
/* 69:   */   public void setActive(boolean isActive)
/* 70:   */   {
/* 71:71 */     this.isActive = isActive;
/* 72:   */   }
/* 73:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.infoserver.feeds.Feed
 * JD-Core Version:    0.7.0.1
 */