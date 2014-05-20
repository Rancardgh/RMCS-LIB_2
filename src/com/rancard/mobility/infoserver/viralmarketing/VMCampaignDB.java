/*   1:    */ package com.rancard.mobility.infoserver.viralmarketing;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import com.rancard.util.DateUtil;
/*   5:    */ import java.io.PrintStream;
/*   6:    */ import java.sql.Connection;
/*   7:    */ import java.sql.ResultSet;
/*   8:    */ import java.sql.Statement;
/*   9:    */ import java.util.ArrayList;
/*  10:    */ import java.util.Date;
/*  11:    */ 
/*  12:    */ public class VMCampaignDB
/*  13:    */ {
/*  14:    */   public static void createCampaign(VMCampaign campaign)
/*  15:    */     throws Exception
/*  16:    */   {
/*  17: 22 */     Connection conn = null;
/*  18:    */     try
/*  19:    */     {
/*  20: 25 */       conn = DConnect.getConnection();
/*  21: 26 */       String sql = "insert into vm_campaigns(last_updated, campaign_id, account_id, keyword, message_sender, message) values('" + DateUtil.convertToMySQLTimeStamp(campaign.getLastUpdated()) + "' , '" + campaign.getCampaignId() + "', " + "'" + campaign.getAccountId() + "', '" + campaign.getKeyword() + "', '" + campaign.getMessageSender() + "', " + "'" + campaign.getMessage() + "')";
/*  22:    */       
/*  23:    */ 
/*  24:    */ 
/*  25: 30 */       System.out.println(new Date() + ": " + VMCampaignDB.class + ":DEBUG   Create campaign: " + sql);
/*  26:    */       
/*  27: 32 */       conn.createStatement().execute(sql);
/*  28:    */     }
/*  29:    */     catch (Exception ex)
/*  30:    */     {
/*  31: 35 */       System.out.println(new Date() + ": " + VMCampaignDB.class + "ERROR: Creating campaign: " + ex.getMessage());
/*  32: 36 */       throw new Exception(ex.getMessage());
/*  33:    */     }
/*  34:    */     finally
/*  35:    */     {
/*  36: 38 */       if (conn != null) {
/*  37: 39 */         conn.close();
/*  38:    */       }
/*  39:    */     }
/*  40:    */   }
/*  41:    */   
/*  42:    */   public static VMCampaign viewCampaign(String campaignId)
/*  43:    */     throws Exception
/*  44:    */   {
/*  45: 46 */     ResultSet rs = null;
/*  46: 47 */     Connection conn = null;
/*  47: 48 */     VMCampaign campaign = null;
/*  48:    */     try
/*  49:    */     {
/*  50: 51 */       conn = DConnect.getConnection();
/*  51:    */       
/*  52: 53 */       String sql = "select * from vm_campaigns where campaign_id = '" + campaignId + "'";
/*  53: 54 */       System.out.println(new Date() + ": " + VMCampaignDB.class + ":DEBUG   View campaign: " + sql);
/*  54:    */       
/*  55: 56 */       rs = conn.createStatement().executeQuery(sql);
/*  56: 58 */       if (rs.next()) {
/*  57: 59 */         campaign = new VMCampaign(rs.getString("campaign_id"), rs.getString("account_id"), rs.getString("keyword"), rs.getString("message_sender"), rs.getString("message"), DateUtil.convertFromMySQLTimeStamp(rs.getString("last_updated")));
/*  58:    */       }
/*  59: 63 */       return campaign;
/*  60:    */     }
/*  61:    */     catch (Exception ex)
/*  62:    */     {
/*  63: 65 */       System.out.println(new Date() + ": " + VMCampaignDB.class + "ERROR: Viewing campaign: " + ex.getMessage());
/*  64: 66 */       throw new Exception(ex.getMessage());
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
/*  77:    */   public static VMCampaign viewCampaign(String accountId, String keyword)
/*  78:    */     throws Exception
/*  79:    */   {
/*  80: 80 */     ResultSet rs = null;
/*  81: 81 */     Connection conn = null;
/*  82: 82 */     VMCampaign campaign = null;
/*  83:    */     try
/*  84:    */     {
/*  85: 85 */       conn = DConnect.getConnection();
/*  86: 86 */       String sql = "select * from vm_campaigns where account_id = '" + accountId + "' and keyword = '" + keyword + "'";
/*  87: 87 */       System.out.println(new Date() + ": " + VMCampaignDB.class + ":DEBUG   View campaign: " + sql);
/*  88:    */       
/*  89: 89 */       rs = conn.createStatement().executeQuery(sql);
/*  90: 91 */       if (rs.next()) {
/*  91: 92 */         campaign = new VMCampaign(rs.getString("campaign_id"), rs.getString("account_id"), rs.getString("keyword"), rs.getString("message_sender"), rs.getString("message"), DateUtil.convertFromMySQLTimeStamp(rs.getString("last_updated")));
/*  92:    */       }
/*  93: 98 */       return campaign;
/*  94:    */     }
/*  95:    */     catch (Exception ex)
/*  96:    */     {
/*  97:100 */       System.out.println(new Date() + ": " + VMCampaignDB.class + ":ERROR Viewing campaign: " + ex.getMessage());
/*  98:101 */       throw new Exception(ex.getMessage());
/*  99:    */     }
/* 100:    */     finally
/* 101:    */     {
/* 102:103 */       if (rs != null) {
/* 103:104 */         rs.close();
/* 104:    */       }
/* 105:106 */       if (conn != null) {
/* 106:107 */         conn.close();
/* 107:    */       }
/* 108:    */     }
/* 109:    */   }
/* 110:    */   
/* 111:    */   public static void updateCampaignMessage(String campaignId, String message)
/* 112:    */     throws Exception
/* 113:    */   {
/* 114:113 */     Connection conn = null;
/* 115:    */     try
/* 116:    */     {
/* 117:116 */       conn = DConnect.getConnection();
/* 118:117 */       String sql = "UPDATE vm_campaigns SET message = '" + message + "' WHERE campaign_id = '" + campaignId + "'";
/* 119:118 */       System.out.println(new Date() + ": " + VMCampaignDB.class + ":DEBUG   Updating campaign: " + sql);
/* 120:    */       
/* 121:120 */       conn.createStatement().executeUpdate(sql);
/* 122:    */     }
/* 123:    */     catch (Exception ex)
/* 124:    */     {
/* 125:123 */       System.out.println(new Date() + ": " + VMCampaignDB.class + ":ERROR   Updating campaign: " + ex.getMessage());
/* 126:124 */       throw new Exception(ex.getMessage());
/* 127:    */     }
/* 128:    */     finally
/* 129:    */     {
/* 130:126 */       if (conn != null) {
/* 131:127 */         conn.close();
/* 132:    */       }
/* 133:    */     }
/* 134:    */   }
/* 135:    */   
/* 136:    */   public static void updateCampaign(VMCampaign campaign, String update_account_id, String update_keyword)
/* 137:    */     throws Exception
/* 138:    */   {
/* 139:133 */     Connection conn = null;
/* 140:    */     try
/* 141:    */     {
/* 142:136 */       conn = DConnect.getConnection();
/* 143:    */       
/* 144:138 */       String sql = "UPDATE vm_campaigns SET campaign_id = '" + campaign.getCampaignId() + "', account_id = '" + campaign.getAccountId() + "', " + "keyword = '" + campaign.getKeyword() + "', message_sender = '" + campaign.getMessageSender() + "', " + "message = '" + campaign.getMessage() + "', last_updated = '" + DateUtil.convertToMySQLTimeStamp(campaign.getLastUpdated()) + "' " + "WHERE acount_id = '" + update_account_id + "' and keyword = '" + update_keyword + "'";
/* 145:    */       
/* 146:    */ 
/* 147:    */ 
/* 148:142 */       System.out.println(new Date() + ": " + VMCampaignDB.class + ":DEBUG   Updating campaign: " + sql);
/* 149:    */       
/* 150:144 */       conn.createStatement().executeUpdate(sql);
/* 151:    */     }
/* 152:    */     catch (Exception ex)
/* 153:    */     {
/* 154:147 */       System.out.println(new Date() + ": " + VMCampaignDB.class + ":ERROR   Updating campaign: " + ex.getMessage());
/* 155:148 */       throw new Exception(ex.getMessage());
/* 156:    */     }
/* 157:    */     finally
/* 158:    */     {
/* 159:150 */       if (conn != null) {
/* 160:151 */         conn.close();
/* 161:    */       }
/* 162:    */     }
/* 163:    */   }
/* 164:    */   
/* 165:    */   public static void deleteCampaign(ArrayList keywords, String accountId)
/* 166:    */     throws Exception
/* 167:    */   {
/* 168:158 */     Connection conn = null;
/* 169:    */     
/* 170:    */ 
/* 171:161 */     String keywordStr = "";
/* 172:162 */     for (int i = 0; i < keywords.size(); i++) {
/* 173:163 */       keywordStr = keywordStr + "'" + keywords.get(i).toString() + "',";
/* 174:    */     }
/* 175:165 */     keywordStr = keywordStr.substring(0, keywordStr.lastIndexOf(","));
/* 176:    */     try
/* 177:    */     {
/* 178:168 */       conn = DConnect.getConnection();
/* 179:169 */       String sql = "delete from vm_campaigns where keyword in (" + keywordStr + ") and account_id='" + accountId + "'";
/* 180:170 */       System.out.println(new Date() + ": " + VMCampaignDB.class + ":DEBUG   Delete campaigns: " + sql);
/* 181:    */       
/* 182:172 */       conn.createStatement().execute(sql);
/* 183:    */     }
/* 184:    */     catch (Exception ex)
/* 185:    */     {
/* 186:174 */       System.out.println(new Date() + ": " + VMCampaignDB.class + ":ERROR   Delete campaigns: " + ex.getMessage());
/* 187:175 */       throw new Exception(ex.getMessage());
/* 188:    */     }
/* 189:    */     finally
/* 190:    */     {
/* 191:177 */       if (conn != null) {
/* 192:178 */         conn.close();
/* 193:    */       }
/* 194:    */     }
/* 195:    */   }
/* 196:    */   
/* 197:    */   public static void deleteCampaign(String keyword, String accountId)
/* 198:    */     throws Exception
/* 199:    */   {
/* 200:184 */     Connection conn = null;
/* 201:    */     try
/* 202:    */     {
/* 203:187 */       conn = DConnect.getConnection();
/* 204:188 */       String sql = "delete from vm_campaigns where keyword = '" + keyword + "' and account_id= '" + accountId + "'";
/* 205:189 */       System.out.println(new Date() + ": " + VMCampaignDB.class + ":DEBUG   Delete campaign: " + sql);
/* 206:    */       
/* 207:191 */       conn.createStatement().execute(sql);
/* 208:    */     }
/* 209:    */     catch (Exception ex)
/* 210:    */     {
/* 211:194 */       System.out.println(new Date() + ": " + VMCampaignDB.class + ":ERROR   Delete campaign: " + ex.getMessage());
/* 212:195 */       throw new Exception(ex.getMessage());
/* 213:    */     }
/* 214:    */     finally
/* 215:    */     {
/* 216:197 */       if (conn != null) {
/* 217:198 */         conn.close();
/* 218:    */       }
/* 219:    */     }
/* 220:    */   }
/* 221:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.infoserver.viralmarketing.VMCampaignDB
 * JD-Core Version:    0.7.0.1
 */