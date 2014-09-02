/*  1:   */ package com.rancard.mobility.contentserver.serviceinterfaces.freemium;
/*  2:   */ 
/*  3:   */ import java.sql.Connection;
/*  4:   */ import java.sql.SQLException;
/*  5:   */ import java.util.Date;
/*  6:   */ import java.util.List;
/*  7:   */ 
/*  8:   */ public class Freemium
/*  9:   */ {
/* 10:   */   private String id;
/* 11:   */   private String accountID;
/* 12:   */   private String keyword;
/* 13:   */   private Date startDate;
/* 14:   */   private Date endDate;
/* 15:   */   private boolean active;
/* 16:   */   private FreemiumType type;
/* 17:   */   private int length;
/* 18:   */   private FreemiumRolloverOption rolloverOption;
/* 19:   */   
/* 20:   */   public Freemium(String id, String accountID, String keyword, int length, Date startDate, Date endDate, boolean active, FreemiumType type, FreemiumRolloverOption rolloverOption)
/* 21:   */   {
/* 22:28 */     this.id = id;
/* 23:29 */     this.accountID = accountID;
/* 24:30 */     this.keyword = keyword;
/* 25:31 */     this.startDate = startDate;
/* 26:32 */     this.endDate = endDate;
/* 27:33 */     this.active = active;
/* 28:34 */     this.type = type;
/* 29:35 */     this.length = length;
/* 30:36 */     this.rolloverOption = rolloverOption;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public String getID()
/* 34:   */   {
/* 35:40 */     return this.id;
/* 36:   */   }
/* 37:   */   
/* 38:   */   public String getAccountID()
/* 39:   */   {
/* 40:44 */     return this.accountID;
/* 41:   */   }
/* 42:   */   
/* 43:   */   public String getKeyword()
/* 44:   */   {
/* 45:49 */     return this.keyword;
/* 46:   */   }
/* 47:   */   
/* 48:   */   public int getLength()
/* 49:   */   {
/* 50:53 */     return this.length;
/* 51:   */   }
/* 52:   */   
/* 53:   */   public Date getStartDate()
/* 54:   */   {
/* 55:57 */     return this.startDate;
/* 56:   */   }
/* 57:   */   
/* 58:   */   public Date getEndDate()
/* 59:   */   {
/* 60:61 */     return this.endDate;
/* 61:   */   }
/* 62:   */   
/* 63:   */   public boolean isActive()
/* 64:   */   {
/* 65:65 */     return this.active;
/* 66:   */   }
/* 67:   */   
/* 68:   */   public FreemiumType getType()
/* 69:   */   {
/* 70:69 */     return this.type;
/* 71:   */   }
/* 72:   */   
/* 73:   */   public List<FreemiumDataSource> getDataSource()
/* 74:   */     throws SQLException, Exception
/* 75:   */   {
/* 76:73 */     return FreemiumDB.getFreemuimDataSource(this);
/* 77:   */   }
/* 78:   */   
/* 79:   */   public List<FreemiumDataSource> getDataSource(Connection conn)
/* 80:   */     throws SQLException
/* 81:   */   {
/* 82:77 */     return FreemiumDB.getFreemuimDataSource(this, conn);
/* 83:   */   }
/* 84:   */   
/* 85:   */   public FreemiumRolloverOption getRolloverOption()
/* 86:   */   {
/* 87:84 */     return this.rolloverOption;
/* 88:   */   }
/* 89:   */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.serviceinterfaces.freemium.Freemium
 * JD-Core Version:    0.7.0.1
 */