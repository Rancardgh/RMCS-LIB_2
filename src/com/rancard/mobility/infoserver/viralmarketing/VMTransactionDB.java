/*   1:    */ package com.rancard.mobility.infoserver.viralmarketing;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import com.rancard.util.DateUtil;
/*   5:    */ import java.io.PrintStream;
/*   6:    */ import java.sql.Connection;
/*   7:    */ import java.sql.ResultSet;
/*   8:    */ import java.sql.Statement;
/*   9:    */ import java.sql.Timestamp;
/*  10:    */ import java.util.Date;
/*  11:    */ 
/*  12:    */ public final class VMTransactionDB
/*  13:    */ {
/*  14:    */   public static void createTransaction(VMTransaction transaction)
/*  15:    */     throws Exception
/*  16:    */   {
/*  17: 20 */     Connection conn = null;
/*  18:    */     try
/*  19:    */     {
/*  20: 23 */       conn = DConnect.getConnection();
/*  21: 24 */       String sql = "insert into vm_transactions(trans_date, campaign_id, recruiter_msisdn, recipient_msisdn, status) values('" + DateUtil.convertToMySQLTimeStamp(transaction.getTransactionDate()) + "', '" + transaction.getCampaignId() + "', " + "'" + transaction.getRecruiterMsisdn() + "', '" + transaction.getRecipientMsisdn() + "', " + "'" + transaction.getStatus() + "')";
/*  22:    */       
/*  23:    */ 
/*  24:    */ 
/*  25: 28 */       System.out.println(new Date() + ": " + VMTransactionDB.class + ":DEBUG Insert Transaction: " + sql);
/*  26:    */       
/*  27: 30 */       conn.createStatement().execute(sql);
/*  28:    */     }
/*  29:    */     catch (Exception ex)
/*  30:    */     {
/*  31: 34 */       System.out.println(new Date() + ": " + VMTransactionDB.class + "ERROR: Creating transaction: " + ex.getMessage());
/*  32: 35 */       throw new Exception(ex.getMessage());
/*  33:    */     }
/*  34:    */     finally
/*  35:    */     {
/*  36: 37 */       if (conn != null) {
/*  37: 38 */         conn.close();
/*  38:    */       }
/*  39:    */     }
/*  40:    */   }
/*  41:    */   
/*  42:    */   public static VMTransaction viewTransaction(String campaignId, String recipientMsisdn)
/*  43:    */     throws Exception
/*  44:    */   {
/*  45: 45 */     ResultSet rs = null;
/*  46: 46 */     Connection conn = null;
/*  47: 47 */     VMTransaction trans = null;
/*  48:    */     try
/*  49:    */     {
/*  50: 50 */       conn = DConnect.getConnection();
/*  51:    */       
/*  52: 52 */       String sql = "select * from vm_transactions where campaign_id = '" + campaignId + "' and recipient_msisdn = '" + recipientMsisdn + "'";
/*  53: 53 */       System.out.println(new Date() + ": " + VMTransactionDB.class + ":DEBUG Getting Transaction: " + sql);
/*  54:    */       
/*  55: 55 */       rs = conn.createStatement().executeQuery(sql);
/*  56: 57 */       if (rs.next()) {
/*  57: 58 */         trans = new VMTransaction(rs.getString("campaign_id"), rs.getString("recruiter_msisdn"), rs.getString("recipient_msisdn"), rs.getString("status"), new Date(rs.getTimestamp("trans_date").getTime()));
/*  58:    */       }
/*  59: 63 */       return trans;
/*  60:    */     }
/*  61:    */     catch (Exception ex)
/*  62:    */     {
/*  63: 66 */       System.out.println(new Date() + ": " + VMTransactionDB.class + "ERROR: Getting transaction: " + ex.getMessage());
/*  64: 67 */       throw new Exception(ex.getMessage());
/*  65:    */     }
/*  66:    */     finally
/*  67:    */     {
/*  68: 69 */       if (rs != null) {
/*  69: 70 */         rs.close();
/*  70:    */       }
/*  71: 72 */       if (conn != null) {
/*  72: 73 */         conn.close();
/*  73:    */       }
/*  74:    */     }
/*  75:    */   }
/*  76:    */   
/*  77:    */   public static VMTransaction viewTransaction(String accountId, String keyword, String recipientMsisdn)
/*  78:    */     throws Exception
/*  79:    */   {
/*  80: 80 */     ResultSet rs = null;
/*  81: 81 */     Connection conn = null;
/*  82: 82 */     VMTransaction trans = null;
/*  83:    */     try
/*  84:    */     {
/*  85: 85 */       conn = DConnect.getConnection();
/*  86:    */       
/*  87: 87 */       String sql = "select * from vm_campaigns INNER JOIN vm_transactions ON vm_campaigns.campaign_id = vm_transactions.campaign_id  where account_id = '" + accountId + "' and keyword = '" + keyword + "' and recipient_msisdn = '" + recipientMsisdn + "'";
/*  88:    */       
/*  89:    */ 
/*  90: 90 */       System.out.println(new Date() + ": " + VMTransactionDB.class + ":DEBUG Getting Transaction: " + sql);
/*  91:    */       
/*  92: 92 */       rs = conn.createStatement().executeQuery(sql);
/*  93: 94 */       if (rs.next()) {
/*  94: 95 */         trans = new VMTransaction(rs.getString("campaign_id"), rs.getString("recruiter_msisdn"), rs.getString("recipient_msisdn"), rs.getString("status"), new Date(rs.getTimestamp("trans_date").getTime()));
/*  95:    */       }
/*  96:100 */       return trans;
/*  97:    */     }
/*  98:    */     catch (Exception ex)
/*  99:    */     {
/* 100:102 */       System.out.println(new Date() + ": " + VMTransactionDB.class + "ERROR: Getting transaction: " + ex.getMessage());
/* 101:103 */       throw new Exception(ex.getMessage());
/* 102:    */     }
/* 103:    */     finally
/* 104:    */     {
/* 105:105 */       if (rs != null) {
/* 106:106 */         rs.close();
/* 107:    */       }
/* 108:108 */       if (conn != null) {
/* 109:109 */         conn.close();
/* 110:    */       }
/* 111:    */     }
/* 112:    */   }
/* 113:    */   
/* 114:    */   public static void updateTransactionStatus(String campaignId, String recipientId, String status)
/* 115:    */     throws Exception
/* 116:    */   {
/* 117:115 */     Connection conn = null;
/* 118:    */     try
/* 119:    */     {
/* 120:118 */       conn = DConnect.getConnection();
/* 121:119 */       String sql = "UPDATE vm_transactions SET status = '" + status + "' WHERE campaign_id = '" + campaignId + "'  and recipient_msisdn = '" + recipientId + "'";
/* 122:120 */       System.out.println(new Date() + ": " + VMTransactionDB.class + ":DEBUG:  Updating Transaction: " + sql);
/* 123:    */       
/* 124:122 */       conn.createStatement().executeUpdate(sql);
/* 125:    */     }
/* 126:    */     catch (Exception ex)
/* 127:    */     {
/* 128:125 */       System.out.println(new Date() + ": " + VMTransactionDB.class + "ERROR:  Updating transaction: " + ex.getMessage());
/* 129:126 */       throw new Exception(ex.getMessage());
/* 130:    */     }
/* 131:    */     finally
/* 132:    */     {
/* 133:128 */       if (conn != null) {
/* 134:129 */         conn.close();
/* 135:    */       }
/* 136:    */     }
/* 137:    */   }
/* 138:    */   
/* 139:    */   public static void updateTransactionStatus(String campaignId, String recipientId, String status, int points)
/* 140:    */     throws Exception
/* 141:    */   {
/* 142:136 */     Connection conn = null;
/* 143:    */     try
/* 144:    */     {
/* 145:140 */       conn = DConnect.getConnection();
/* 146:141 */       String sql = "UPDATE vm_transactions SET status = '" + status + "' WHERE campaign_id = '" + campaignId + "'  and recipient_msisdn = '" + recipientId + "'";
/* 147:142 */       System.out.println(new Date() + ": " + VMTransactionDB.class + ":DEBUG:  Updating Transaction: " + sql);
/* 148:    */       
/* 149:144 */       conn.createStatement().executeUpdate(sql);
/* 150:    */       
/* 151:    */ 
/* 152:147 */       String sqlPoints = "UPDATE vm_users vmu INNER JOIN vm_campaigns vmc ON vmu.account_id = vmc.account_id AND vmu.keyword = vmc.keyword INNER JOIN vm_transactions vmt ON vmt.campaign_id = vmc.campaign_id SET vmu.points = vmu.points + " + points + " " + "WHERE vmt.recipient_msisdn = '" + recipientId + "' AND vmt.campaign_id = '" + campaignId + "' AND vmt.status = '" + status + "' AND vmu.msisdn = vmt.recruiter_msisdn";
/* 153:    */       
/* 154:    */ 
/* 155:150 */       System.out.println(new Date() + ": " + VMTransactionDB.class + ":DEBUG:  Updating Points: " + sqlPoints);
/* 156:    */       
/* 157:152 */       conn.createStatement().executeUpdate(sqlPoints);
/* 158:    */     }
/* 159:    */     catch (Exception ex)
/* 160:    */     {
/* 161:155 */       System.out.println(new Date() + ": " + VMTransactionDB.class + "ERROR:  Updating transaction: " + ex.getMessage());
/* 162:156 */       throw new Exception(ex.getMessage());
/* 163:    */     }
/* 164:    */     finally
/* 165:    */     {
/* 166:158 */       if (conn != null) {
/* 167:159 */         conn.close();
/* 168:    */       }
/* 169:    */     }
/* 170:    */   }
/* 171:    */   
/* 172:    */   public static VMTransaction viewTransaction(String campaignId, String recipientMsisdn, String recruiterMsisdn, boolean sendReminderIfExists)
/* 173:    */     throws Exception
/* 174:    */   {
/* 175:166 */     ResultSet rs = null;
/* 176:167 */     Connection conn = null;
/* 177:168 */     VMTransaction trans = null;
/* 178:    */     try
/* 179:    */     {
/* 180:171 */       conn = DConnect.getConnection();
/* 181:    */       
/* 182:173 */       String sql = "select * from vm_transactions where campaign_id = '" + campaignId + "' and recipient_msisdn = '" + recipientMsisdn + "' and recruiter_msisdn = '" + recruiterMsisdn + "'";
/* 183:174 */       System.out.println(new Date() + ": " + VMTransactionDB.class + ":DEBUG:  Getting Transaction: " + sql);
/* 184:    */       
/* 185:    */ 
/* 186:177 */       rs = conn.createStatement().executeQuery(sql);
/* 187:179 */       if (rs.next()) {
/* 188:180 */         trans = new VMTransaction(rs.getString("campaign_id"), rs.getString("recruiter_msisdn"), rs.getString("recipient_msisdn"), rs.getString("status"), new Date(rs.getTimestamp("trans_date").getTime()));
/* 189:    */       }
/* 190:184 */       return trans;
/* 191:    */     }
/* 192:    */     catch (Exception ex)
/* 193:    */     {
/* 194:186 */       System.out.println(new Date() + ": " + VMTransactionDB.class + "ERROR:  Selecting transaction: " + ex.getMessage());
/* 195:187 */       throw new Exception(ex.getMessage());
/* 196:    */     }
/* 197:    */     finally
/* 198:    */     {
/* 199:189 */       if (rs != null) {
/* 200:190 */         rs.close();
/* 201:    */       }
/* 202:192 */       if (conn != null) {
/* 203:193 */         conn.close();
/* 204:    */       }
/* 205:    */     }
/* 206:    */   }
/* 207:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.infoserver.viralmarketing.VMTransactionDB
 * JD-Core Version:    0.7.0.1
 */