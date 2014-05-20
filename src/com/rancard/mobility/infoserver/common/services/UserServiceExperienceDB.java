/*   1:    */ package com.rancard.mobility.infoserver.common.services;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import java.io.PrintStream;
/*   5:    */ import java.sql.Connection;
/*   6:    */ import java.sql.PreparedStatement;
/*   7:    */ import java.sql.ResultSet;
/*   8:    */ import java.sql.SQLException;
/*   9:    */ import java.sql.Statement;
/*  10:    */ import java.util.ArrayList;
/*  11:    */ import java.util.Date;
/*  12:    */ 
/*  13:    */ public class UserServiceExperienceDB
/*  14:    */ {
/*  15:    */   public static void createServiceExperience(UserServiceExperience serviceExperience)
/*  16:    */     throws Exception
/*  17:    */   {
/*  18: 13 */     Connection conn = null;
/*  19:    */     try
/*  20:    */     {
/*  21: 16 */       conn = DConnect.getConnection();
/*  22: 17 */       String sql = "insert into service_experience_config(account_id, site_id, keyword, promo_id, welcome_msg, already_subscribed_msg, unsubscription_conf_msg, promo_msg_sender, welcome_msg_sender, already_subscribed_msg_sender, unsubscription_conf_msg_sender, push_msg_wait_time, subscription_interval, url, url_timeout, meta_data) values('" + serviceExperience.getAccountId() + "', '" + serviceExperience.getSiteId() + "', " + "'" + serviceExperience.getKeyword() + "', '" + serviceExperience.getPromoId() + "', '" + serviceExperience.getWelcomeMsg() + "', " + "'" + serviceExperience.getAlreadySubscribedMsg() + "', '" + serviceExperience.getUnsubscriptionConfirmationMsg() + "', " + "'" + serviceExperience.getPromoMsgSender() + "', '" + serviceExperience.getWelcomeMsgSender() + "', " + "'" + serviceExperience.getAlreadySubscribedMsgSender() + "', '" + serviceExperience.getUnsubscriptionConfirmationMsgSender() + "', " + serviceExperience.getPushMsgWaitTime() + ", " + serviceExperience.getSubscriptionInterval() + ", '" + serviceExperience.getUrl() + "', " + serviceExperience.getUrlTimeout() + ", '" + serviceExperience.getMetaData() + "')";
/*  23:    */       
/*  24:    */ 
/*  25:    */ 
/*  26:    */ 
/*  27:    */ 
/*  28:    */ 
/*  29:    */ 
/*  30:    */ 
/*  31:    */ 
/*  32:    */ 
/*  33: 28 */       System.out.println(new Date() + ": " + UserServiceExperienceDB.class + " : Inserting new service_experience_config: " + sql);
/*  34:    */       
/*  35: 30 */       conn.createStatement().executeQuery(sql);
/*  36:    */       
/*  37: 32 */       System.out.println(new Date() + ": " + UserServiceExperienceDB.class + " : Inserted service_experience_config");
/*  38:    */     }
/*  39:    */     catch (SQLException ex)
/*  40:    */     {
/*  41: 36 */       throw new SQLException(ex.getMessage(), ex.getSQLState());
/*  42:    */     }
/*  43:    */     finally
/*  44:    */     {
/*  45: 38 */       if (conn != null) {
/*  46: 39 */         conn.close();
/*  47:    */       }
/*  48:    */     }
/*  49:    */   }
/*  50:    */   
/*  51:    */   public static UserServiceExperience viewServiceExperience(String accountId, String siteId, String keyword)
/*  52:    */     throws Exception
/*  53:    */   {
/*  54: 46 */     ResultSet rs = null;
/*  55: 47 */     Connection conn = null;
/*  56:    */     try
/*  57:    */     {
/*  58: 50 */       conn = DConnect.getConnection();
/*  59: 51 */       String sql = "select * from service_experience_config sec LEFT OUTER JOIN promotional_campaign pc ON sec.promo_id = pc.promo_id where sec.account_id = '" + accountId + "' and " + "sec.site_id = '" + siteId + "' and sec.keyword = '" + keyword + "'";
/*  60:    */       
/*  61:    */ 
/*  62:    */ 
/*  63: 55 */       System.out.println(new Date() + ": " + UserServiceExperienceDB.class + " : Selecting service_experience_config: " + sql);
/*  64:    */       
/*  65: 57 */       rs = conn.createStatement().executeQuery(sql);
/*  66: 59 */       if (rs.next())
/*  67:    */       {
/*  68: 60 */         System.out.println(new Date() + ": " + UserServiceExperienceDB.class + " : Found service_experience_config: " + sql);
/*  69: 61 */         return new UserServiceExperience(rs.getString("sec.account_id"), rs.getString("site_id"), rs.getString("keyword"), rs.getString("promo_msg"), rs.getString("sec.promo_id"), rs.getString("promo_response_code"), rs.getString("welcome_msg"), rs.getString("already_subscribed_msg"), rs.getString("unsubscription_conf_msg"), rs.getString("promo_msg_sender"), rs.getString("welcome_msg_sender"), rs.getString("already_subscribed_msg_sender"), rs.getString("unsubscription_conf_msg_sender"), rs.getInt("push_msg_wait_time"), rs.getInt("subscription_interval"), rs.getString("url"), rs.getInt("url_timeout"), rs.getString("meta_data"));
/*  70:    */       }
/*  71:    */     }
/*  72:    */     catch (SQLException ex)
/*  73:    */     {
/*  74: 71 */       throw new SQLException(ex.getMessage(), ex.getSQLState());
/*  75:    */     }
/*  76:    */     finally
/*  77:    */     {
/*  78: 73 */       if (rs != null) {
/*  79: 74 */         rs.close();
/*  80:    */       }
/*  81: 76 */       if (conn != null) {
/*  82: 77 */         conn.close();
/*  83:    */       }
/*  84:    */     }
/*  85: 81 */     return null;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public static void deleteServiceExperience(String accountId, String keyword)
/*  89:    */     throws Exception
/*  90:    */   {
/*  91: 85 */     Connection conn = null;
/*  92:    */     try
/*  93:    */     {
/*  94: 88 */       conn = DConnect.getConnection();
/*  95:    */       
/*  96: 90 */       String sql = "delete from service_experience_config where keyword = '" + keyword + "' and account_id = '" + accountId + "'";
/*  97: 91 */       System.out.println(new Date() + ": " + UserServiceExperienceDB.class + " : Delete service_experience_config: " + sql);
/*  98:    */       
/*  99: 93 */       conn.createStatement().execute(sql);
/* 100:    */       
/* 101: 95 */       System.out.println(new Date() + ": " + UserServiceExperienceDB.class + " : Deleted service_experience_config: " + accountId + "-" + keyword);
/* 102:    */     }
/* 103:    */     catch (Exception ex)
/* 104:    */     {
/* 105: 98 */       throw new Exception(ex.getMessage());
/* 106:    */     }
/* 107:    */     finally
/* 108:    */     {
/* 109:101 */       if (conn != null) {
/* 110:102 */         conn.close();
/* 111:    */       }
/* 112:    */     }
/* 113:    */   }
/* 114:    */   
/* 115:    */   public static void deleteServiceExperience(String accountId, ArrayList keywords)
/* 116:    */     throws Exception
/* 117:    */   {
/* 118:109 */     Connection conn = null;
/* 119:110 */     StringBuilder keywordStr = new StringBuilder();
/* 120:112 */     for (int i = 0; i < keywords.size(); i++) {
/* 121:113 */       keywordStr.append("'").append(keywords.get(i).toString()).append("',");
/* 122:    */     }
/* 123:116 */     keywordStr.deleteCharAt(keywordStr.toString().lastIndexOf(","));
/* 124:    */     try
/* 125:    */     {
/* 126:119 */       conn = DConnect.getConnection();
/* 127:120 */       String sql = "delete from service_experience_config where keyword in (" + keywordStr.toString() + ") and account_id='" + accountId + "'";
/* 128:121 */       System.out.println(new Date() + ": " + UserServiceExperienceDB.class + " : Delete service_experience_config: " + sql);
/* 129:    */       
/* 130:123 */       conn.createStatement().executeQuery(sql);
/* 131:124 */       System.out.println(new Date() + ": " + UserServiceExperienceDB.class + " : Deleted service_experience_config: " + accountId + "-" + keywordStr.toString());
/* 132:    */     }
/* 133:    */     catch (Exception ex)
/* 134:    */     {
/* 135:126 */       throw new Exception(ex.getMessage());
/* 136:    */     }
/* 137:    */     finally
/* 138:    */     {
/* 139:129 */       if (conn != null) {
/* 140:130 */         conn.close();
/* 141:    */       }
/* 142:    */     }
/* 143:    */   }
/* 144:    */   
/* 145:    */   public static void updateServiceExperience(String update_account_id, String update_keyword, UserServiceExperience serviceExperience)
/* 146:    */     throws Exception
/* 147:    */   {
/* 148:137 */     ResultSet rs = null;
/* 149:138 */     Connection con = null;
/* 150:139 */     PreparedStatement prepstat = null;
/* 151:    */     
/* 152:    */ 
/* 153:142 */     String account_id = (serviceExperience.getAccountId() != null) && (!serviceExperience.getAccountId().equals("")) ? "'" + serviceExperience.getAccountId() + "'" : "account_id";
/* 154:143 */     String site_id = (serviceExperience.getSiteId() != null) && (!serviceExperience.getSiteId().equals("")) ? "'" + serviceExperience.getSiteId() + "'" : "site_id";
/* 155:144 */     String keyword = (serviceExperience.getKeyword() != null) && (!serviceExperience.getKeyword().equals("")) ? "'" + serviceExperience.getKeyword() + "'" : "keyword";
/* 156:145 */     String promo_id = (serviceExperience.getPromoId() != null) && (!serviceExperience.getPromoId().equals("")) ? "'" + serviceExperience.getPromoId() + "'" : "promo_id";
/* 157:146 */     String welcome_msg = (serviceExperience.getWelcomeMsg() != null) && (!serviceExperience.getWelcomeMsg().equals("")) ? "'" + serviceExperience.getWelcomeMsg() + "'" : "welcome_msg";
/* 158:147 */     String already_subscribed_msg = (serviceExperience.getAlreadySubscribedMsg() != null) && (!serviceExperience.getAlreadySubscribedMsg().equals("")) ? "'" + serviceExperience.getAlreadySubscribedMsg() + "'" : "already_subscribed_msg";
/* 159:148 */     String unsubscription_conf_msg = (serviceExperience.getUnsubscriptionConfirmationMsg() != null) && (!serviceExperience.getUnsubscriptionConfirmationMsg().equals("")) ? "'" + serviceExperience.getUnsubscriptionConfirmationMsg() + "'" : "unsubscription_conf_msg";
/* 160:149 */     String promo_msg_sender = (serviceExperience.getPromoMsgSender() != null) && (!serviceExperience.getPromoMsgSender().equals("")) ? "'" + serviceExperience.getPromoMsgSender() + "'" : "promo_msg_sender";
/* 161:150 */     String welcome_msg_sender = (serviceExperience.getWelcomeMsgSender() != null) && (!serviceExperience.getWelcomeMsgSender().equals("")) ? "'" + serviceExperience.getWelcomeMsgSender() + "'" : "welcome_msg_sender";
/* 162:151 */     String already_subscribed_msg_sender = (serviceExperience.getAlreadySubscribedMsgSender() != null) && (!serviceExperience.getAlreadySubscribedMsgSender().equals("")) ? "'" + serviceExperience.getAlreadySubscribedMsgSender() + "'" : "already_subscribed_msg_sender";
/* 163:152 */     String unsubscription_conf_msg_sender = (serviceExperience.getUnsubscriptionConfirmationMsgSender() != null) && (!serviceExperience.getUnsubscriptionConfirmationMsgSender().equals("")) ? "'" + serviceExperience.getUnsubscriptionConfirmationMsgSender() + "'" : "unsubscription_conf_msg_sender";
/* 164:153 */     int push_msg_wait_time = serviceExperience.getPushMsgWaitTime();
/* 165:154 */     int subscription_interval = serviceExperience.getSubscriptionInterval();
/* 166:155 */     String url = (serviceExperience.getUrl() != null) && (!serviceExperience.getUrl().equals("")) ? "'" + serviceExperience.getUrl() + "'" : "url";
/* 167:156 */     int url_timeout = serviceExperience.getUrlTimeout();
/* 168:157 */     String meta_data = (serviceExperience.getMetaData() != null) && (!serviceExperience.getMetaData().equals("")) ? "'" + serviceExperience.getMetaData() + "'" : "meta_data";
/* 169:    */     try
/* 170:    */     {
/* 171:161 */       con = DConnect.getConnection();
/* 172:162 */       String SQL = "UPDATE service_experience_config SET account_id = " + account_id + ", site_id = " + site_id + ", keyword = " + keyword + "," + "promo_id = " + promo_id + ", welcome_msg = " + welcome_msg + ", already_subscribed_msg = " + already_subscribed_msg + ", unsubscription_conf_msg = " + unsubscription_conf_msg + ", " + "promo_msg_sender = " + promo_msg_sender + ", welcome_msg_sender = " + welcome_msg_sender + ", already_subscribed_msg_sender = " + already_subscribed_msg_sender + ", " + "unsubscription_conf_msg_sender = " + unsubscription_conf_msg_sender + ", push_msg_wait_time = " + push_msg_wait_time + ", subscription_interval = " + subscription_interval + ", " + "url = " + url + ", url_timeout = " + url_timeout + ", meta_data = " + meta_data + " WHERE account_id = ? and keyword = ? ";
/* 173:    */       
/* 174:    */ 
/* 175:    */ 
/* 176:    */ 
/* 177:    */ 
/* 178:168 */       prepstat = con.prepareStatement(SQL);
/* 179:    */       
/* 180:170 */       prepstat.setString(1, update_account_id);
/* 181:171 */       prepstat.setString(2, update_keyword);
/* 182:    */       
/* 183:173 */       prepstat.execute();
/* 184:    */     }
/* 185:    */     catch (Exception ex)
/* 186:    */     {
/* 187:176 */       if (con != null)
/* 188:    */       {
/* 189:    */         try
/* 190:    */         {
/* 191:178 */           con.close();
/* 192:    */         }
/* 193:    */         catch (SQLException ex1)
/* 194:    */         {
/* 195:180 */           System.out.println(ex1.getMessage());
/* 196:    */         }
/* 197:182 */         con = null;
/* 198:    */       }
/* 199:    */     }
/* 200:    */     finally
/* 201:    */     {
/* 202:185 */       if (rs != null)
/* 203:    */       {
/* 204:    */         try
/* 205:    */         {
/* 206:187 */           rs.close();
/* 207:    */         }
/* 208:    */         catch (SQLException e) {}
/* 209:191 */         rs = null;
/* 210:    */       }
/* 211:193 */       if (prepstat != null)
/* 212:    */       {
/* 213:    */         try
/* 214:    */         {
/* 215:195 */           prepstat.close();
/* 216:    */         }
/* 217:    */         catch (SQLException e) {}
/* 218:199 */         prepstat = null;
/* 219:    */       }
/* 220:201 */       if (con != null)
/* 221:    */       {
/* 222:    */         try
/* 223:    */         {
/* 224:203 */           con.close();
/* 225:    */         }
/* 226:    */         catch (SQLException e) {}
/* 227:207 */         con = null;
/* 228:    */       }
/* 229:    */     }
/* 230:    */   }
/* 231:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.infoserver.common.services.UserServiceExperienceDB
 * JD-Core Version:    0.7.0.1
 */