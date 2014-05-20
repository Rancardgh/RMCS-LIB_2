/*   1:    */ package com.rancard.mobility.infoserver.common.inbox;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import com.rancard.common.uidGen;
/*   5:    */ import java.sql.Connection;
/*   6:    */ import java.sql.PreparedStatement;
/*   7:    */ import java.sql.ResultSet;
/*   8:    */ import java.sql.Timestamp;
/*   9:    */ import java.util.Date;
/*  10:    */ 
/*  11:    */ public class InboxEntry
/*  12:    */ {
/*  13:    */   private String keyword;
/*  14:    */   private String msisdn;
/*  15:    */   private Timestamp dateReceived;
/*  16:    */   private String message;
/*  17:    */   private String messageId;
/*  18:    */   private String shortCode;
/*  19:    */   private String accountId;
/*  20:    */   private int viewed;
/*  21:    */   
/*  22:    */   public InboxEntry()
/*  23:    */   {
/*  24: 20 */     this.keyword = "";
/*  25: 21 */     this.msisdn = "";
/*  26: 22 */     this.dateReceived = new Timestamp(new Date().getTime());
/*  27:    */     
/*  28: 24 */     this.message = "";
/*  29: 25 */     new uidGen();this.messageId = uidGen.generateNumberID(10);
/*  30:    */     
/*  31: 27 */     this.shortCode = "";
/*  32: 28 */     this.accountId = "";
/*  33: 29 */     this.viewed = 0;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public static InboxEntry getLastInboxEntryFor(String keyword, String msisdn)
/*  37:    */     throws Exception
/*  38:    */   {
/*  39: 33 */     InboxEntry ie = new InboxEntry();
/*  40:    */     
/*  41:    */ 
/*  42:    */ 
/*  43: 37 */     Connection con = null;
/*  44:    */     try
/*  45:    */     {
/*  46: 40 */       con = DConnect.getConnection();
/*  47: 41 */       String SQL = "select ib.* from inbox ib where ib.keyword = '" + keyword + "' and mobileno  = '" + msisdn + "' order by date_voted desc;";
/*  48: 42 */       PreparedStatement prepstat = con.prepareStatement(SQL);
/*  49:    */       
/*  50: 44 */       ResultSet rs = prepstat.executeQuery();
/*  51: 46 */       if (rs.next())
/*  52:    */       {
/*  53: 47 */         InboxEntry entry = new InboxEntry();
/*  54: 48 */         entry.setDateReceived(rs.getTimestamp("date_voted"));
/*  55: 49 */         entry.setMessage(rs.getString("response"));
/*  56: 50 */         entry.setMessageId(rs.getString("voteid"));
/*  57: 51 */         entry.setMsisdn(rs.getString("mobileno"));
/*  58: 52 */         entry.setKeyword(rs.getString("keyword"));
/*  59: 53 */         entry.setShortCode(rs.getString("short_code"));
/*  60: 54 */         entry.setAccountId(rs.getString("account_id"));
/*  61: 55 */         entry.setViewed(rs.getInt("is_read"));
/*  62: 56 */         ie = entry;
/*  63:    */       }
/*  64:    */     }
/*  65:    */     catch (Exception ex)
/*  66:    */     {
/*  67: 59 */       if (con != null) {
/*  68: 60 */         con.close();
/*  69:    */       }
/*  70: 62 */       throw new Exception(ex.getMessage());
/*  71:    */     }
/*  72: 64 */     if (con != null) {
/*  73: 65 */       con.close();
/*  74:    */     }
/*  75: 67 */     return ie;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public InboxEntry(String keyword, String msisdn, Timestamp dateReceived, String message, String messageId, String shortCode, String provId)
/*  79:    */   {
/*  80: 73 */     this.keyword = keyword;
/*  81: 74 */     this.msisdn = msisdn;
/*  82: 75 */     this.dateReceived = dateReceived;
/*  83: 76 */     this.message = message;
/*  84: 77 */     this.messageId = messageId;
/*  85:    */     
/*  86: 79 */     this.shortCode = shortCode;
/*  87: 80 */     this.accountId = provId;
/*  88: 81 */     this.viewed = 0;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public Timestamp getDateReceived()
/*  92:    */   {
/*  93: 85 */     return this.dateReceived;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public String getMessage()
/*  97:    */   {
/*  98: 89 */     return this.message;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public String getMessageId()
/* 102:    */   {
/* 103: 93 */     return this.messageId;
/* 104:    */   }
/* 105:    */   
/* 106:    */   public String getMsisdn()
/* 107:    */   {
/* 108: 97 */     return this.msisdn;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public String getKeyword()
/* 112:    */   {
/* 113:105 */     return this.keyword;
/* 114:    */   }
/* 115:    */   
/* 116:    */   public String getShortCode()
/* 117:    */   {
/* 118:109 */     return this.shortCode;
/* 119:    */   }
/* 120:    */   
/* 121:    */   public String getAccountId()
/* 122:    */   {
/* 123:113 */     return this.accountId;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public void setDateReceived(Timestamp dateReceived)
/* 127:    */   {
/* 128:117 */     this.dateReceived = dateReceived;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public void setMessage(String message)
/* 132:    */   {
/* 133:121 */     this.message = message;
/* 134:    */   }
/* 135:    */   
/* 136:    */   public void setMessageId(String messageId)
/* 137:    */   {
/* 138:125 */     this.messageId = messageId;
/* 139:    */   }
/* 140:    */   
/* 141:    */   public void setMsisdn(String msisdn)
/* 142:    */   {
/* 143:129 */     this.msisdn = msisdn;
/* 144:    */   }
/* 145:    */   
/* 146:    */   public void setKeyword(String keyword)
/* 147:    */   {
/* 148:137 */     this.keyword = keyword;
/* 149:    */   }
/* 150:    */   
/* 151:    */   public void setShortCode(String shortCode)
/* 152:    */   {
/* 153:141 */     this.shortCode = shortCode;
/* 154:    */   }
/* 155:    */   
/* 156:    */   public void setAccountId(String providerId)
/* 157:    */   {
/* 158:145 */     this.accountId = providerId;
/* 159:    */   }
/* 160:    */   
/* 161:    */   public int getViewed()
/* 162:    */   {
/* 163:149 */     return this.viewed;
/* 164:    */   }
/* 165:    */   
/* 166:    */   public void setViewed(int viewed)
/* 167:    */   {
/* 168:153 */     this.viewed = viewed;
/* 169:    */   }
/* 170:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.common.inbox.InboxEntry
 * JD-Core Version:    0.7.0.1
 */