/*   1:    */ package com.rancard.mobility.infoserver.viralmarketing;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import com.rancard.util.DateUtil;
/*   5:    */ import java.io.PrintStream;
/*   6:    */ import java.sql.Connection;
/*   7:    */ import java.sql.PreparedStatement;
/*   8:    */ import java.sql.ResultSet;
/*   9:    */ import java.sql.Statement;
/*  10:    */ import java.sql.Timestamp;
/*  11:    */ import java.text.SimpleDateFormat;
/*  12:    */ import java.util.Date;
/*  13:    */ 
/*  14:    */ public class PromoImpressionDB
/*  15:    */ {
/*  16:    */   public static void updatePromoViewDate(PromoImpression impression)
/*  17:    */     throws Exception
/*  18:    */   {
/*  19: 21 */     String sql = "UPDATE promo_impression_tracker set view_date = '" + DateUtil.convertToMySQLTimeStamp(impression.getViewDate()) + "' " + "where hash_code = " + impression.getHashCode();
/*  20:    */     
/*  21:    */ 
/*  22:    */ 
/*  23: 25 */     Connection conn = null;
/*  24:    */     
/*  25: 27 */     System.out.println(new Date() + " " + PromoImpressionDB.class + ": Will update promo view date");
/*  26:    */     try
/*  27:    */     {
/*  28: 30 */       conn = DConnect.getConnection();
/*  29: 31 */       conn.createStatement().executeUpdate(sql);
/*  30:    */     }
/*  31:    */     catch (Exception ex)
/*  32:    */     {
/*  33: 34 */       System.out.println(new Date() + ": error creating promo_campaign_hash entry(" + impression.getHashCode() + "): " + ex.getMessage());
/*  34: 35 */       throw new Exception(ex.getMessage());
/*  35:    */     }
/*  36:    */     finally
/*  37:    */     {
/*  38: 38 */       if (conn != null) {
/*  39: 39 */         conn.close();
/*  40:    */       }
/*  41:    */     }
/*  42:    */   }
/*  43:    */   
/*  44:    */   public static void createEntry(PromoImpression impression)
/*  45:    */     throws Exception
/*  46:    */   {
/*  47: 47 */     ResultSet rs = null;
/*  48: 48 */     Connection con = null;
/*  49: 49 */     PreparedStatement prepstat = null;
/*  50:    */     try
/*  51:    */     {
/*  52: 52 */       con = DConnect.getConnection();
/*  53: 53 */       String SQL = "insert into promo_impression_tracker (hash_code, msisdn, account_id, promo_response_code, view_date, inventory_keyword) values(?, ?, ?, ?, ?, ?)";
/*  54:    */       
/*  55:    */ 
/*  56: 56 */       prepstat = con.prepareStatement(SQL);
/*  57:    */       
/*  58: 58 */       prepstat.setLong(1, impression.getHashCode());
/*  59: 59 */       prepstat.setString(2, impression.getMsisdn());
/*  60: 60 */       prepstat.setString(3, impression.getAccountId());
/*  61: 61 */       prepstat.setString(4, impression.getKeyword());
/*  62: 62 */       prepstat.setTimestamp(5, new Timestamp(impression.getViewDate().getTime()));
/*  63: 63 */       prepstat.setString(6, impression.getInventory_keyword());
/*  64:    */       
/*  65: 65 */       prepstat.execute();
/*  66:    */     }
/*  67:    */     catch (Exception ex)
/*  68:    */     {
/*  69: 68 */       System.out.println(new Date() + ": error creating promo_campaign_hash entry(" + impression.getHashCode() + "): " + ex.getMessage());
/*  70: 69 */       throw new Exception(ex.getMessage());
/*  71:    */     }
/*  72:    */     finally
/*  73:    */     {
/*  74: 71 */       if (rs != null) {
/*  75: 72 */         rs.close();
/*  76:    */       }
/*  77: 74 */       if (prepstat != null) {
/*  78: 75 */         prepstat.close();
/*  79:    */       }
/*  80: 77 */       if (con != null) {
/*  81: 78 */         con.close();
/*  82:    */       }
/*  83:    */     }
/*  84:    */   }
/*  85:    */   
/*  86:    */   public static PromoImpression viewPromoImpression(String msisdn, String keyword, String accountID)
/*  87:    */     throws Exception
/*  88:    */   {
/*  89: 85 */     System.out.println(new Date() + " :@PromoImpressionDB");
/*  90:    */     
/*  91: 87 */     ResultSet rs = null;
/*  92: 88 */     Connection con = null;
/*  93: 89 */     PreparedStatement prepstat = null;
/*  94: 90 */     PromoImpression impression = new PromoImpression();
/*  95:    */     try
/*  96:    */     {
/*  97: 93 */       con = DConnect.getConnection();
/*  98:    */       
/*  99: 95 */       String SQL = "select * from promo_impression_tracker where msisdn = '" + msisdn + "' and inventory_keyword = '" + keyword + "' and account_id = '" + accountID + "'";
/* 100: 96 */       System.out.println(new Date() + ": Looking for recent promo SQL: " + SQL);
/* 101: 97 */       prepstat = con.prepareStatement(SQL);
/* 102:    */       
/* 103: 99 */       rs = prepstat.executeQuery();
/* 104:101 */       while (rs.next())
/* 105:    */       {
/* 106:102 */         impression.setHashCode(rs.getLong("hash_code"));
/* 107:103 */         impression.setAccountId(rs.getString("account_id"));
/* 108:104 */         impression.setMsisdn(rs.getString("msisdn"));
/* 109:105 */         impression.setKeyword(rs.getString("promo_response_code"));
/* 110:106 */         impression.setViewDate(new Date(rs.getTimestamp("view_date").getTime()));
/* 111:107 */         impression.setInventory_keyword(rs.getString("inventory_keyword"));
/* 112:    */       }
/* 113:110 */       System.out.println(new Date() + ": Result found: MSISDN=" + impression.getMsisdn() + " KEYWORD=" + impression.getInventory_keyword() + " ACCOUNTID=" + impression.getAccountId());
/* 114:    */     }
/* 115:    */     catch (Exception ex)
/* 116:    */     {
/* 117:113 */       System.out.println(new Date() + ": error viewing promo_campaign_hash (" + impression.getHashCode() + "): " + ex.getMessage());
/* 118:    */     }
/* 119:    */     finally
/* 120:    */     {
/* 121:116 */       if (rs != null) {
/* 122:117 */         rs.close();
/* 123:    */       }
/* 124:119 */       if (prepstat != null) {
/* 125:120 */         prepstat.close();
/* 126:    */       }
/* 127:122 */       if (con != null) {
/* 128:123 */         con.close();
/* 129:    */       }
/* 130:    */     }
/* 131:127 */     return impression;
/* 132:    */   }
/* 133:    */   
/* 134:    */   public static PromoImpression viewPromoImpression(long hashCode)
/* 135:    */     throws Exception
/* 136:    */   {
/* 137:131 */     ResultSet rs = null;
/* 138:132 */     Connection con = null;
/* 139:133 */     PromoImpression impression = new PromoImpression();
/* 140:    */     try
/* 141:    */     {
/* 142:136 */       con = DConnect.getConnection();
/* 143:    */       
/* 144:138 */       String SQL = "select * from promo_impression_tracker where hash_code = " + hashCode;
/* 145:139 */       System.out.println(new Date() + " " + PromoImpressionDB.class + ": Will check if promo exists: " + SQL);
/* 146:    */       
/* 147:    */ 
/* 148:142 */       rs = con.createStatement().executeQuery(SQL);
/* 149:144 */       if (rs.next())
/* 150:    */       {
/* 151:145 */         impression.setHashCode(rs.getLong("hash_code"));
/* 152:146 */         impression.setAccountId(rs.getString("account_id"));
/* 153:147 */         impression.setMsisdn(rs.getString("msisdn"));
/* 154:148 */         impression.setKeyword(rs.getString("promo_response_code"));
/* 155:149 */         impression.setViewDate(new Date(rs.getTimestamp("view_date").getTime()));
/* 156:150 */         impression.setInventory_keyword(rs.getString("inventory_keyword"));
/* 157:151 */         System.out.println(new Date() + " " + PromoImpressionDB.class + ": Promo exists");
/* 158:    */       }
/* 159:    */     }
/* 160:    */     catch (Exception ex)
/* 161:    */     {
/* 162:157 */       System.out.println(new Date() + ": error viewing promo_campaign_hash (" + hashCode + "): " + ex.getMessage());
/* 163:158 */       throw new Exception(ex.getMessage());
/* 164:    */     }
/* 165:    */     finally
/* 166:    */     {
/* 167:160 */       if (rs != null) {
/* 168:161 */         rs.close();
/* 169:    */       }
/* 170:164 */       if (con != null) {
/* 171:165 */         con.close();
/* 172:    */       }
/* 173:    */     }
/* 174:169 */     return impression;
/* 175:    */   }
/* 176:    */   
/* 177:    */   public static synchronized void updateAdResponseSummary(PromoImpression impression)
/* 178:    */     throws Exception
/* 179:    */   {
/* 180:173 */     ResultSet rs = null;
/* 181:174 */     Connection con = null;
/* 182:    */     
/* 183:    */ 
/* 184:177 */     Date dt = new Date();
/* 185:178 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/* 186:179 */     String currDate = sdf.format(dt);
/* 187:    */     
/* 188:181 */     System.out.println(new Date() + " " + PromoImpressionDB.class + ": Will update ad_response_summary");
/* 189:    */     try
/* 190:    */     {
/* 191:183 */       con = DConnect.getConnection();
/* 192:    */       
/* 193:185 */       String selectQuery = "select promo_hits from ad_response_summary where account_id ='" + impression.getAccountId() + "' and keyword='" + impression.getInventory_keyword() + "'" + " and promo_keyword = '" + impression.getKeyword() + "' and log_date = '" + currDate + "'";
/* 194:    */       
/* 195:187 */       System.out.println(new Date() + " " + PromoImpressionDB.class + ": Will firsh check if ad_response_summary exists: " + selectQuery);
/* 196:188 */       rs = con.createStatement().executeQuery(selectQuery);
/* 197:190 */       if (!rs.next())
/* 198:    */       {
/* 199:192 */         String insertStmt = "Insert into ad_response_summary (account_id,keyword,promo_hits,log_date,promo_keyword) values('" + impression.getAccountId() + "', '" + impression.getInventory_keyword() + "', 1, '" + currDate + "', '" + impression.getKeyword() + "')";
/* 200:    */         
/* 201:    */ 
/* 202:195 */         System.out.println(new Date() + " " + PromoImpressionDB.class + ": ad_response_summary does not exists. Will insert new one: " + insertStmt);
/* 203:196 */         con.createStatement().execute(insertStmt);
/* 204:    */       }
/* 205:    */       else
/* 206:    */       {
/* 207:199 */         int total_promo_hits = rs.getInt("promo_hits") + 1;
/* 208:    */         
/* 209:201 */         String updateStmt = "UPDATE ad_response_summary SET promo_hits = " + total_promo_hits + " where " + "keyword = '" + impression.getInventory_keyword() + "' and account_id = '" + impression.getAccountId() + "' " + "and promo_keyword = '" + impression.getKeyword() + "' and log_date = '" + currDate + "'";
/* 210:    */         
/* 211:    */ 
/* 212:204 */         System.out.println(new Date() + " " + PromoImpressionDB.class + ": ad_response_summary exists. Will update hits: " + updateStmt);
/* 213:    */         
/* 214:206 */         con.createStatement().executeUpdate(updateStmt);
/* 215:    */       }
/* 216:    */     }
/* 217:    */     catch (Exception ex)
/* 218:    */     {
/* 219:210 */       System.out.println(new Date() + " " + PromoImpressionDB.class + ": error updating ad_response_summary: " + ex.getMessage());
/* 220:211 */       throw new Exception(ex.getMessage());
/* 221:    */     }
/* 222:    */     finally
/* 223:    */     {
/* 224:213 */       if (rs != null) {
/* 225:214 */         rs.close();
/* 226:    */       }
/* 227:216 */       if (con != null) {
/* 228:217 */         con.close();
/* 229:    */       }
/* 230:    */     }
/* 231:    */   }
/* 232:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.viralmarketing.PromoImpressionDB
 * JD-Core Version:    0.7.0.1
 */