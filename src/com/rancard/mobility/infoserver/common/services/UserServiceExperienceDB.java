/*   1:    */ package com.rancard.mobility.infoserver.common.services;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import java.io.PrintStream;
/*   5:    */ import java.sql.Connection;
/*   6:    */ import java.sql.PreparedStatement;
/*   7:    */ import java.sql.ResultSet;
/*   8:    */ import java.sql.SQLException;
/*   9:    */ import java.util.ArrayList;
/*  10:    */ 
/*  11:    */ public class UserServiceExperienceDB
/*  12:    */ {
/*  13:    */   public static void createServiceExperience(UserServiceExperience serviceExperience)
/*  14:    */     throws Exception
/*  15:    */   {
/*  16: 10 */     ResultSet rs = null;
/*  17: 11 */     Connection con = null;
/*  18: 12 */     PreparedStatement prepstat = null;
/*  19:    */     try
/*  20:    */     {
/*  21: 15 */       con = DConnect.getConnection();
/*  22: 16 */       String SQL = "insert into service_experience_config(account_id, site_id, keyword, promo_id, welcome_msg, already_subscribed_msg, unsubscription_conf_msg, promo_msg_sender, welcome_msg_sender, already_subscribed_msg_sender, unsubscription_conf_msg_sender, push_msg_wait_time, subscription_interval, url, url_timeout, meta_data) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
/*  23:    */       
/*  24:    */ 
/*  25:    */ 
/*  26:    */ 
/*  27:    */ 
/*  28: 22 */       prepstat = con.prepareStatement(SQL);
/*  29:    */       
/*  30: 24 */       prepstat.setString(1, serviceExperience.getAccountId());
/*  31: 25 */       prepstat.setString(2, serviceExperience.getSiteId());
/*  32: 26 */       prepstat.setString(3, serviceExperience.getKeyword());
/*  33: 27 */       prepstat.setString(4, serviceExperience.getPromoId());
/*  34: 28 */       prepstat.setString(5, serviceExperience.getWelcomeMsg());
/*  35: 29 */       prepstat.setString(6, serviceExperience.getAlreadySubscribedMsg());
/*  36: 30 */       prepstat.setString(7, serviceExperience.getUnsubscriptionConfirmationMsg());
/*  37: 31 */       prepstat.setString(8, serviceExperience.getPromoMsgSender());
/*  38: 32 */       prepstat.setString(9, serviceExperience.getWelcomeMsgSender());
/*  39: 33 */       prepstat.setString(10, serviceExperience.getAlreadySubscribedMsgSender());
/*  40: 34 */       prepstat.setString(11, serviceExperience.getUnsubscriptionConfirmationMsgSender());
/*  41: 35 */       prepstat.setInt(12, serviceExperience.getPushMsgWaitTime());
/*  42: 36 */       prepstat.setInt(13, serviceExperience.getSubscriptionInterval());
/*  43: 37 */       prepstat.setString(14, serviceExperience.getUrl());
/*  44: 38 */       prepstat.setInt(15, serviceExperience.getUrlTimeout());
/*  45: 39 */       prepstat.setString(16, serviceExperience.getMetaData());
/*  46:    */       
/*  47: 41 */       prepstat.execute();
/*  48:    */     }
/*  49:    */     catch (Exception ex)
/*  50:    */     {
/*  51: 44 */       if (con != null)
/*  52:    */       {
/*  53:    */         try
/*  54:    */         {
/*  55: 46 */           con.close();
/*  56:    */         }
/*  57:    */         catch (SQLException ex1)
/*  58:    */         {
/*  59: 48 */           System.out.println(ex1.getMessage());
/*  60:    */         }
/*  61: 50 */         con = null;
/*  62:    */       }
/*  63:    */     }
/*  64:    */     finally
/*  65:    */     {
/*  66: 53 */       if (rs != null)
/*  67:    */       {
/*  68:    */         try
/*  69:    */         {
/*  70: 55 */           rs.close();
/*  71:    */         }
/*  72:    */         catch (SQLException e) {}
/*  73: 59 */         rs = null;
/*  74:    */       }
/*  75: 61 */       if (prepstat != null)
/*  76:    */       {
/*  77:    */         try
/*  78:    */         {
/*  79: 63 */           prepstat.close();
/*  80:    */         }
/*  81:    */         catch (SQLException e) {}
/*  82: 67 */         prepstat = null;
/*  83:    */       }
/*  84: 69 */       if (con != null)
/*  85:    */       {
/*  86:    */         try
/*  87:    */         {
/*  88: 71 */           con.close();
/*  89:    */         }
/*  90:    */         catch (SQLException e) {}
/*  91: 75 */         con = null;
/*  92:    */       }
/*  93:    */     }
/*  94:    */   }
/*  95:    */   
/*  96:    */   public static UserServiceExperience viewServiceExperience(String accountId, String siteId, String keyword)
/*  97:    */     throws Exception
/*  98:    */   {
/*  99: 84 */     ResultSet rs = null;
/* 100: 85 */     Connection con = null;
/* 101: 86 */     PreparedStatement prepstat = null;
/* 102: 87 */     UserServiceExperience serviceExperience = new UserServiceExperience();
/* 103:    */     try
/* 104:    */     {
/* 105: 90 */       con = DConnect.getConnection();
/* 106:    */       
/* 107: 92 */       String SQL = "select * from service_experience_config sec LEFT OUTER JOIN promotional_campaign pc ON sec.promo_id = pc.promo_id where sec.account_id = ? and sec.site_id = ? and sec.keyword = ? ";
/* 108:    */       
/* 109:    */ 
/* 110: 95 */       prepstat = con.prepareStatement(SQL);
/* 111:    */       
/* 112: 97 */       prepstat.setString(1, accountId);
/* 113: 98 */       prepstat.setString(2, siteId);
/* 114: 99 */       prepstat.setString(3, keyword);
/* 115:    */       
/* 116:101 */       rs = prepstat.executeQuery();
/* 117:103 */       while (rs.next())
/* 118:    */       {
/* 119:104 */         serviceExperience.setAccountId(rs.getString("sec.account_id"));
/* 120:105 */         serviceExperience.setSiteId(rs.getString("site_id"));
/* 121:106 */         serviceExperience.setKeyword(rs.getString("keyword"));
/* 122:107 */         serviceExperience.setPromoId(rs.getString("sec.promo_id"));
/* 123:108 */         serviceExperience.setPromoMsg(rs.getString("promo_msg"));
/* 124:109 */         serviceExperience.setPromoRespCode(rs.getString("promo_response_code"));
/* 125:110 */         serviceExperience.setWelcomeMsg(rs.getString("welcome_msg"));
/* 126:111 */         serviceExperience.setAlreadySubscribedMsg(rs.getString("already_subscribed_msg"));
/* 127:112 */         serviceExperience.setUnsubscriptionConfirmationMsg(rs.getString("unsubscription_conf_msg"));
/* 128:113 */         serviceExperience.setPromoMsgSender(rs.getString("promo_msg_sender"));
/* 129:114 */         serviceExperience.setWelcomeMsgSender(rs.getString("welcome_msg_sender"));
/* 130:115 */         serviceExperience.setAlreadySubscribedMsgSender(rs.getString("already_subscribed_msg_sender"));
/* 131:116 */         serviceExperience.setUnsubscriptionConfirmationMsgSender(rs.getString("unsubscription_conf_msg_sender"));
/* 132:117 */         serviceExperience.setPushMsgWaitTime(rs.getInt("push_msg_wait_time"));
/* 133:118 */         serviceExperience.setSubscriptionInterval(rs.getInt("subscription_interval"));
/* 134:119 */         serviceExperience.setUrl(rs.getString("url"));
/* 135:120 */         serviceExperience.setUrlTimeout(rs.getInt("url_timeout"));
/* 136:121 */         serviceExperience.setMetaData(rs.getString("meta_data"));
/* 137:    */       }
/* 138:    */     }
/* 139:    */     catch (Exception ex)
/* 140:    */     {
/* 141:125 */       if (con != null)
/* 142:    */       {
/* 143:    */         try
/* 144:    */         {
/* 145:127 */           con.close();
/* 146:    */         }
/* 147:    */         catch (SQLException ex1)
/* 148:    */         {
/* 149:129 */           System.out.println(ex1.getMessage());
/* 150:    */         }
/* 151:131 */         con = null;
/* 152:    */       }
/* 153:    */     }
/* 154:    */     finally
/* 155:    */     {
/* 156:135 */       if (rs != null)
/* 157:    */       {
/* 158:    */         try
/* 159:    */         {
/* 160:137 */           rs.close();
/* 161:    */         }
/* 162:    */         catch (SQLException e) {}
/* 163:141 */         rs = null;
/* 164:    */       }
/* 165:143 */       if (prepstat != null)
/* 166:    */       {
/* 167:    */         try
/* 168:    */         {
/* 169:145 */           prepstat.close();
/* 170:    */         }
/* 171:    */         catch (SQLException e) {}
/* 172:149 */         prepstat = null;
/* 173:    */       }
/* 174:151 */       if (con != null)
/* 175:    */       {
/* 176:    */         try
/* 177:    */         {
/* 178:153 */           con.close();
/* 179:    */         }
/* 180:    */         catch (SQLException e) {}
/* 181:157 */         con = null;
/* 182:    */       }
/* 183:    */     }
/* 184:161 */     return serviceExperience;
/* 185:    */   }
/* 186:    */   
/* 187:    */   public static void deleteServiceExperience(String accountId, String keyword)
/* 188:    */     throws Exception
/* 189:    */   {
/* 190:167 */     ResultSet rs = null;
/* 191:168 */     Connection con = null;
/* 192:169 */     PreparedStatement prepstat = null;
/* 193:    */     try
/* 194:    */     {
/* 195:173 */       con = DConnect.getConnection();
/* 196:    */       
/* 197:175 */       String SQL = "delete from service_experience_config where keyword = ? and account_id = ?";
/* 198:    */       
/* 199:177 */       prepstat = con.prepareStatement(SQL);
/* 200:    */       
/* 201:179 */       prepstat.setString(1, keyword);
/* 202:180 */       prepstat.setString(2, accountId);
/* 203:    */       
/* 204:182 */       prepstat.execute();
/* 205:    */     }
/* 206:    */     catch (Exception ex)
/* 207:    */     {
/* 208:185 */       if (con != null)
/* 209:    */       {
/* 210:    */         try
/* 211:    */         {
/* 212:187 */           con.close();
/* 213:    */         }
/* 214:    */         catch (SQLException ex1)
/* 215:    */         {
/* 216:189 */           System.out.println(ex1.getMessage());
/* 217:    */         }
/* 218:191 */         con = null;
/* 219:    */       }
/* 220:    */     }
/* 221:    */     finally
/* 222:    */     {
/* 223:195 */       if (rs != null)
/* 224:    */       {
/* 225:    */         try
/* 226:    */         {
/* 227:197 */           rs.close();
/* 228:    */         }
/* 229:    */         catch (SQLException e) {}
/* 230:201 */         rs = null;
/* 231:    */       }
/* 232:203 */       if (prepstat != null)
/* 233:    */       {
/* 234:    */         try
/* 235:    */         {
/* 236:205 */           prepstat.close();
/* 237:    */         }
/* 238:    */         catch (SQLException e) {}
/* 239:209 */         prepstat = null;
/* 240:    */       }
/* 241:211 */       if (con != null)
/* 242:    */       {
/* 243:    */         try
/* 244:    */         {
/* 245:213 */           con.close();
/* 246:    */         }
/* 247:    */         catch (SQLException e) {}
/* 248:217 */         con = null;
/* 249:    */       }
/* 250:    */     }
/* 251:    */   }
/* 252:    */   
/* 253:    */   public static void deleteServiceExperience(String accountId, ArrayList keywords)
/* 254:    */     throws Exception
/* 255:    */   {
/* 256:226 */     ResultSet rs = null;
/* 257:227 */     Connection con = null;
/* 258:228 */     PreparedStatement prepstat = null;
/* 259:    */     
/* 260:230 */     String keywordStr = "";
/* 261:231 */     for (int i = 0; i < keywords.size(); i++) {
/* 262:232 */       keywordStr = keywordStr + "'" + keywords.get(i).toString() + "',";
/* 263:    */     }
/* 264:234 */     keywordStr = keywordStr.substring(0, keywordStr.lastIndexOf(","));
/* 265:    */     try
/* 266:    */     {
/* 267:237 */       con = DConnect.getConnection();
/* 268:238 */       String SQL = "delete from service_experience_config where keyword in (" + keywordStr + ") and account_id='" + accountId + "'";
/* 269:239 */       prepstat = con.prepareStatement(SQL);
/* 270:240 */       prepstat.execute();
/* 271:    */     }
/* 272:    */     catch (Exception ex)
/* 273:    */     {
/* 274:242 */       if (con != null)
/* 275:    */       {
/* 276:    */         try
/* 277:    */         {
/* 278:244 */           con.close();
/* 279:    */         }
/* 280:    */         catch (SQLException ex1)
/* 281:    */         {
/* 282:246 */           System.out.println(ex1.getMessage());
/* 283:    */         }
/* 284:248 */         con = null;
/* 285:    */       }
/* 286:    */     }
/* 287:    */     finally
/* 288:    */     {
/* 289:251 */       if (rs != null)
/* 290:    */       {
/* 291:    */         try
/* 292:    */         {
/* 293:253 */           rs.close();
/* 294:    */         }
/* 295:    */         catch (SQLException e) {}
/* 296:257 */         rs = null;
/* 297:    */       }
/* 298:259 */       if (prepstat != null)
/* 299:    */       {
/* 300:    */         try
/* 301:    */         {
/* 302:261 */           prepstat.close();
/* 303:    */         }
/* 304:    */         catch (SQLException e) {}
/* 305:265 */         prepstat = null;
/* 306:    */       }
/* 307:267 */       if (con != null)
/* 308:    */       {
/* 309:    */         try
/* 310:    */         {
/* 311:269 */           con.close();
/* 312:    */         }
/* 313:    */         catch (SQLException e) {}
/* 314:273 */         con = null;
/* 315:    */       }
/* 316:    */     }
/* 317:    */   }
/* 318:    */   
/* 319:    */   public static void updateServiceExperience(String update_account_id, String update_keyword, UserServiceExperience serviceExperience)
/* 320:    */     throws Exception
/* 321:    */   {
/* 322:280 */     ResultSet rs = null;
/* 323:281 */     Connection con = null;
/* 324:282 */     PreparedStatement prepstat = null;
/* 325:    */     
/* 326:    */ 
/* 327:285 */     String account_id = (serviceExperience.getAccountId() != null) && (!serviceExperience.getAccountId().equals("")) ? "'" + serviceExperience.getAccountId() + "'" : "account_id";
/* 328:286 */     String site_id = (serviceExperience.getSiteId() != null) && (!serviceExperience.getSiteId().equals("")) ? "'" + serviceExperience.getSiteId() + "'" : "site_id";
/* 329:287 */     String keyword = (serviceExperience.getKeyword() != null) && (!serviceExperience.getKeyword().equals("")) ? "'" + serviceExperience.getKeyword() + "'" : "keyword";
/* 330:288 */     String promo_id = (serviceExperience.getPromoId() != null) && (!serviceExperience.getPromoId().equals("")) ? "'" + serviceExperience.getPromoId() + "'" : "promo_id";
/* 331:289 */     String welcome_msg = (serviceExperience.getWelcomeMsg() != null) && (!serviceExperience.getWelcomeMsg().equals("")) ? "'" + serviceExperience.getWelcomeMsg() + "'" : "welcome_msg";
/* 332:290 */     String already_subscribed_msg = (serviceExperience.getAlreadySubscribedMsg() != null) && (!serviceExperience.getAlreadySubscribedMsg().equals("")) ? "'" + serviceExperience.getAlreadySubscribedMsg() + "'" : "already_subscribed_msg";
/* 333:291 */     String unsubscription_conf_msg = (serviceExperience.getUnsubscriptionConfirmationMsg() != null) && (!serviceExperience.getUnsubscriptionConfirmationMsg().equals("")) ? "'" + serviceExperience.getUnsubscriptionConfirmationMsg() + "'" : "unsubscription_conf_msg";
/* 334:292 */     String promo_msg_sender = (serviceExperience.getPromoMsgSender() != null) && (!serviceExperience.getPromoMsgSender().equals("")) ? "'" + serviceExperience.getPromoMsgSender() + "'" : "promo_msg_sender";
/* 335:293 */     String welcome_msg_sender = (serviceExperience.getWelcomeMsgSender() != null) && (!serviceExperience.getWelcomeMsgSender().equals("")) ? "'" + serviceExperience.getWelcomeMsgSender() + "'" : "welcome_msg_sender";
/* 336:294 */     String already_subscribed_msg_sender = (serviceExperience.getAlreadySubscribedMsgSender() != null) && (!serviceExperience.getAlreadySubscribedMsgSender().equals("")) ? "'" + serviceExperience.getAlreadySubscribedMsgSender() + "'" : "already_subscribed_msg_sender";
/* 337:295 */     String unsubscription_conf_msg_sender = (serviceExperience.getUnsubscriptionConfirmationMsgSender() != null) && (!serviceExperience.getUnsubscriptionConfirmationMsgSender().equals("")) ? "'" + serviceExperience.getUnsubscriptionConfirmationMsgSender() + "'" : "unsubscription_conf_msg_sender";
/* 338:296 */     int push_msg_wait_time = serviceExperience.getPushMsgWaitTime();
/* 339:297 */     int subscription_interval = serviceExperience.getSubscriptionInterval();
/* 340:298 */     String url = (serviceExperience.getUrl() != null) && (!serviceExperience.getUrl().equals("")) ? "'" + serviceExperience.getUrl() + "'" : "url";
/* 341:299 */     int url_timeout = serviceExperience.getUrlTimeout();
/* 342:300 */     String meta_data = (serviceExperience.getMetaData() != null) && (!serviceExperience.getMetaData().equals("")) ? "'" + serviceExperience.getMetaData() + "'" : "meta_data";
/* 343:    */     try
/* 344:    */     {
/* 345:304 */       con = DConnect.getConnection();
/* 346:305 */       String SQL = "UPDATE service_experience_config SET account_id = " + account_id + ", site_id = " + site_id + ", keyword = " + keyword + "," + "promo_id = " + promo_id + ", welcome_msg = " + welcome_msg + ", already_subscribed_msg = " + already_subscribed_msg + ", unsubscription_conf_msg = " + unsubscription_conf_msg + ", " + "promo_msg_sender = " + promo_msg_sender + ", welcome_msg_sender = " + welcome_msg_sender + ", already_subscribed_msg_sender = " + already_subscribed_msg_sender + ", " + "unsubscription_conf_msg_sender = " + unsubscription_conf_msg_sender + ", push_msg_wait_time = " + push_msg_wait_time + ", subscription_interval = " + subscription_interval + ", " + "url = " + url + ", url_timeout = " + url_timeout + ", meta_data = " + meta_data + " WHERE account_id = ? and keyword = ? ";
/* 347:    */       
/* 348:    */ 
/* 349:    */ 
/* 350:    */ 
/* 351:    */ 
/* 352:311 */       prepstat = con.prepareStatement(SQL);
/* 353:    */       
/* 354:313 */       prepstat.setString(1, update_account_id);
/* 355:314 */       prepstat.setString(2, update_keyword);
/* 356:    */       
/* 357:316 */       prepstat.execute();
/* 358:    */     }
/* 359:    */     catch (Exception ex)
/* 360:    */     {
/* 361:319 */       if (con != null)
/* 362:    */       {
/* 363:    */         try
/* 364:    */         {
/* 365:321 */           con.close();
/* 366:    */         }
/* 367:    */         catch (SQLException ex1)
/* 368:    */         {
/* 369:323 */           System.out.println(ex1.getMessage());
/* 370:    */         }
/* 371:325 */         con = null;
/* 372:    */       }
/* 373:    */     }
/* 374:    */     finally
/* 375:    */     {
/* 376:328 */       if (rs != null)
/* 377:    */       {
/* 378:    */         try
/* 379:    */         {
/* 380:330 */           rs.close();
/* 381:    */         }
/* 382:    */         catch (SQLException e) {}
/* 383:334 */         rs = null;
/* 384:    */       }
/* 385:336 */       if (prepstat != null)
/* 386:    */       {
/* 387:    */         try
/* 388:    */         {
/* 389:338 */           prepstat.close();
/* 390:    */         }
/* 391:    */         catch (SQLException e) {}
/* 392:342 */         prepstat = null;
/* 393:    */       }
/* 394:344 */       if (con != null)
/* 395:    */       {
/* 396:    */         try
/* 397:    */         {
/* 398:346 */           con.close();
/* 399:    */         }
/* 400:    */         catch (SQLException e) {}
/* 401:350 */         con = null;
/* 402:    */       }
/* 403:    */     }
/* 404:    */   }
/* 405:    */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.common.services.UserServiceExperienceDB
 * JD-Core Version:    0.7.0.1
 */