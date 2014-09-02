/*   1:    */ package com.rancard.mobility.infoserver.viralmarketing;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import java.io.PrintStream;
/*   5:    */ import java.sql.Connection;
/*   6:    */ import java.sql.PreparedStatement;
/*   7:    */ import java.sql.ResultSet;
/*   8:    */ import java.sql.SQLException;
/*   9:    */ import java.sql.Timestamp;
/*  10:    */ import java.text.SimpleDateFormat;
/*  11:    */ import java.util.ArrayList;
/*  12:    */ import java.util.Date;
/*  13:    */ 
/*  14:    */ public class VMCampaignDB
/*  15:    */ {
/*  16:    */   public static void createCampaign(VMCampaign campaign)
/*  17:    */     throws Exception
/*  18:    */   {
/*  19: 22 */     ResultSet rs = null;
/*  20: 23 */     Connection con = null;
/*  21: 24 */     PreparedStatement prepstat = null;
/*  22:    */     try
/*  23:    */     {
/*  24: 27 */       con = DConnect.getConnection();
/*  25: 28 */       String SQL = "insert into vm_campaigns(last_updated, campaign_id, account_id, keyword, message_sender, message) values(?, ?, ?, ?, ?, ?)";
/*  26:    */       
/*  27:    */ 
/*  28: 31 */       prepstat = con.prepareStatement(SQL);
/*  29:    */       
/*  30: 33 */       prepstat.setTimestamp(1, new Timestamp(new Date().getTime()));
/*  31: 34 */       prepstat.setString(2, campaign.getCampaignId());
/*  32: 35 */       prepstat.setString(3, campaign.getAccountId());
/*  33: 36 */       prepstat.setString(4, campaign.getKeyword());
/*  34: 37 */       prepstat.setString(5, campaign.getMessageSender());
/*  35: 38 */       prepstat.setString(6, campaign.getMessage());
/*  36:    */       
/*  37: 40 */       prepstat.execute();
/*  38:    */     }
/*  39:    */     catch (Exception ex)
/*  40:    */     {
/*  41: 43 */       if (con != null)
/*  42:    */       {
/*  43:    */         try
/*  44:    */         {
/*  45: 45 */           con.close();
/*  46:    */         }
/*  47:    */         catch (SQLException ex1)
/*  48:    */         {
/*  49: 47 */           System.out.println(ex1.getMessage());
/*  50:    */         }
/*  51: 49 */         con = null;
/*  52:    */       }
/*  53:    */     }
/*  54:    */     finally
/*  55:    */     {
/*  56: 52 */       if (rs != null)
/*  57:    */       {
/*  58:    */         try
/*  59:    */         {
/*  60: 54 */           rs.close();
/*  61:    */         }
/*  62:    */         catch (SQLException e) {}
/*  63: 58 */         rs = null;
/*  64:    */       }
/*  65: 60 */       if (prepstat != null)
/*  66:    */       {
/*  67:    */         try
/*  68:    */         {
/*  69: 62 */           prepstat.close();
/*  70:    */         }
/*  71:    */         catch (SQLException e) {}
/*  72: 66 */         prepstat = null;
/*  73:    */       }
/*  74: 68 */       if (con != null)
/*  75:    */       {
/*  76:    */         try
/*  77:    */         {
/*  78: 70 */           con.close();
/*  79:    */         }
/*  80:    */         catch (SQLException e) {}
/*  81: 74 */         con = null;
/*  82:    */       }
/*  83:    */     }
/*  84:    */   }
/*  85:    */   
/*  86:    */   public static VMCampaign viewCampaign(String campaignId)
/*  87:    */     throws Exception
/*  88:    */   {
/*  89: 83 */     ResultSet rs = null;
/*  90: 84 */     Connection con = null;
/*  91: 85 */     PreparedStatement prepstat = null;
/*  92: 86 */     VMCampaign campaign = new VMCampaign();
/*  93:    */     try
/*  94:    */     {
/*  95: 89 */       con = DConnect.getConnection();
/*  96:    */       
/*  97: 91 */       String SQL = "select * from vm_campaigns where campaign_id = ?";
/*  98:    */       
/*  99: 93 */       prepstat = con.prepareStatement(SQL);
/* 100:    */       
/* 101: 95 */       prepstat.setString(1, campaignId);
/* 102:    */       
/* 103: 97 */       rs = prepstat.executeQuery();
/* 104: 99 */       while (rs.next())
/* 105:    */       {
/* 106:100 */         campaign.setCampaignId(rs.getString("campaign_id"));
/* 107:101 */         campaign.setAccountId(rs.getString("account_id"));
/* 108:102 */         campaign.setKeyword(rs.getString("keyword"));
/* 109:103 */         campaign.setMessageSender(rs.getString("message_sender"));
/* 110:104 */         campaign.setMessage(rs.getString("message"));
/* 111:105 */         SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/* 112:106 */         String lastUpdated = df.format(new Date(rs.getTimestamp("last_updated").getTime()));
/* 113:107 */         campaign.setLastUpdated(lastUpdated);
/* 114:    */       }
/* 115:    */     }
/* 116:    */     catch (Exception ex)
/* 117:    */     {
/* 118:111 */       if (con != null)
/* 119:    */       {
/* 120:    */         try
/* 121:    */         {
/* 122:113 */           con.close();
/* 123:    */         }
/* 124:    */         catch (SQLException ex1)
/* 125:    */         {
/* 126:115 */           System.out.println(ex1.getMessage());
/* 127:    */         }
/* 128:117 */         con = null;
/* 129:    */       }
/* 130:121 */       System.out.println(new Date() + ": error viewing vm_campaign (" + campaignId + "): " + ex.getMessage());
/* 131:    */     }
/* 132:    */     finally
/* 133:    */     {
/* 134:124 */       if (rs != null)
/* 135:    */       {
/* 136:    */         try
/* 137:    */         {
/* 138:126 */           rs.close();
/* 139:    */         }
/* 140:    */         catch (SQLException e) {}
/* 141:130 */         rs = null;
/* 142:    */       }
/* 143:132 */       if (prepstat != null)
/* 144:    */       {
/* 145:    */         try
/* 146:    */         {
/* 147:134 */           prepstat.close();
/* 148:    */         }
/* 149:    */         catch (SQLException e) {}
/* 150:138 */         prepstat = null;
/* 151:    */       }
/* 152:140 */       if (con != null)
/* 153:    */       {
/* 154:    */         try
/* 155:    */         {
/* 156:142 */           con.close();
/* 157:    */         }
/* 158:    */         catch (SQLException e) {}
/* 159:146 */         con = null;
/* 160:    */       }
/* 161:    */     }
/* 162:150 */     return campaign;
/* 163:    */   }
/* 164:    */   
/* 165:    */   public static VMCampaign viewCampaign(String accountId, String keyword)
/* 166:    */     throws Exception
/* 167:    */   {
/* 168:156 */     ResultSet rs = null;
/* 169:157 */     Connection con = null;
/* 170:158 */     PreparedStatement prepstat = null;
/* 171:159 */     VMCampaign campaign = new VMCampaign();
/* 172:    */     try
/* 173:    */     {
/* 174:162 */       con = DConnect.getConnection();
/* 175:    */       
/* 176:164 */       String SQL = "select * from vm_campaigns where account_id = ? and keyword = ?";
/* 177:    */       
/* 178:166 */       prepstat = con.prepareStatement(SQL);
/* 179:    */       
/* 180:168 */       prepstat.setString(1, accountId);
/* 181:169 */       prepstat.setString(2, keyword);
/* 182:    */       
/* 183:171 */       rs = prepstat.executeQuery();
/* 184:173 */       while (rs.next())
/* 185:    */       {
/* 186:174 */         campaign.setCampaignId(rs.getString("campaign_id"));
/* 187:175 */         campaign.setAccountId(rs.getString("account_id"));
/* 188:176 */         campaign.setKeyword(rs.getString("keyword"));
/* 189:177 */         campaign.setMessageSender(rs.getString("message_sender"));
/* 190:178 */         campaign.setMessage(rs.getString("message"));
/* 191:179 */         SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/* 192:180 */         String lastUpdated = df.format(new Date(rs.getTimestamp("last_updated").getTime()));
/* 193:181 */         campaign.setLastUpdated(lastUpdated);
/* 194:    */       }
/* 195:    */     }
/* 196:    */     catch (Exception ex)
/* 197:    */     {
/* 198:185 */       if (con != null)
/* 199:    */       {
/* 200:    */         try
/* 201:    */         {
/* 202:187 */           con.close();
/* 203:    */         }
/* 204:    */         catch (SQLException ex1)
/* 205:    */         {
/* 206:189 */           System.out.println(ex1.getMessage());
/* 207:    */         }
/* 208:191 */         con = null;
/* 209:    */       }
/* 210:195 */       System.out.println(new Date() + ": error viewing vm_campaign (" + accountId + " - " + keyword + "): " + ex.getMessage());
/* 211:    */     }
/* 212:    */     finally
/* 213:    */     {
/* 214:198 */       if (rs != null)
/* 215:    */       {
/* 216:    */         try
/* 217:    */         {
/* 218:200 */           rs.close();
/* 219:    */         }
/* 220:    */         catch (SQLException e) {}
/* 221:204 */         rs = null;
/* 222:    */       }
/* 223:206 */       if (prepstat != null)
/* 224:    */       {
/* 225:    */         try
/* 226:    */         {
/* 227:208 */           prepstat.close();
/* 228:    */         }
/* 229:    */         catch (SQLException e) {}
/* 230:212 */         prepstat = null;
/* 231:    */       }
/* 232:214 */       if (con != null)
/* 233:    */       {
/* 234:    */         try
/* 235:    */         {
/* 236:216 */           con.close();
/* 237:    */         }
/* 238:    */         catch (SQLException e) {}
/* 239:220 */         con = null;
/* 240:    */       }
/* 241:    */     }
/* 242:224 */     return campaign;
/* 243:    */   }
/* 244:    */   
/* 245:    */   public static void updateCampaignMessage(String campaignId, String message)
/* 246:    */     throws Exception
/* 247:    */   {
/* 248:230 */     ResultSet rs = null;
/* 249:231 */     Connection con = null;
/* 250:232 */     PreparedStatement prepstat = null;
/* 251:    */     try
/* 252:    */     {
/* 253:235 */       con = DConnect.getConnection();
/* 254:236 */       String SQL = "UPDATE vm_campaigns SET message = ? WHERE campaign_id = ?";
/* 255:    */       
/* 256:    */ 
/* 257:    */ 
/* 258:240 */       prepstat = con.prepareStatement(SQL);
/* 259:    */       
/* 260:242 */       prepstat.setString(1, message);
/* 261:243 */       prepstat.setString(2, campaignId);
/* 262:    */       
/* 263:245 */       prepstat.execute();
/* 264:    */     }
/* 265:    */     catch (Exception ex)
/* 266:    */     {
/* 267:248 */       if (con != null)
/* 268:    */       {
/* 269:    */         try
/* 270:    */         {
/* 271:250 */           con.close();
/* 272:    */         }
/* 273:    */         catch (SQLException ex1)
/* 274:    */         {
/* 275:252 */           System.out.println(ex1.getMessage());
/* 276:    */         }
/* 277:254 */         con = null;
/* 278:    */       }
/* 279:    */     }
/* 280:    */     finally
/* 281:    */     {
/* 282:257 */       if (rs != null)
/* 283:    */       {
/* 284:    */         try
/* 285:    */         {
/* 286:259 */           rs.close();
/* 287:    */         }
/* 288:    */         catch (SQLException e) {}
/* 289:263 */         rs = null;
/* 290:    */       }
/* 291:265 */       if (prepstat != null)
/* 292:    */       {
/* 293:    */         try
/* 294:    */         {
/* 295:267 */           prepstat.close();
/* 296:    */         }
/* 297:    */         catch (SQLException e) {}
/* 298:271 */         prepstat = null;
/* 299:    */       }
/* 300:273 */       if (con != null)
/* 301:    */       {
/* 302:    */         try
/* 303:    */         {
/* 304:275 */           con.close();
/* 305:    */         }
/* 306:    */         catch (SQLException e) {}
/* 307:279 */         con = null;
/* 308:    */       }
/* 309:    */     }
/* 310:    */   }
/* 311:    */   
/* 312:    */   public static void updateCampaign(VMCampaign campaign, String update_account_id, String update_keyword)
/* 313:    */     throws Exception
/* 314:    */   {
/* 315:288 */     ResultSet rs = null;
/* 316:289 */     Connection con = null;
/* 317:290 */     PreparedStatement prepstat = null;
/* 318:    */     
/* 319:    */ 
/* 320:293 */     String account_id = (campaign.getAccountId() != null) && (!campaign.getAccountId().equals("")) ? "'" + campaign.getAccountId() + "'" : "account_id";
/* 321:294 */     String campaign_id = (campaign.getCampaignId() != null) && (!campaign.getCampaignId().equals("")) ? "'" + campaign.getCampaignId() + "'" : "campaign_id";
/* 322:295 */     String keyword = (campaign.getKeyword() != null) && (!campaign.getKeyword().equals("")) ? "'" + campaign.getKeyword() + "'" : "keyword";
/* 323:296 */     String message_sender = (campaign.getMessageSender() != null) && (!campaign.getMessageSender().equals("")) ? "'" + campaign.getMessageSender() + "'" : "message_sender";
/* 324:297 */     String message = (campaign.getMessage() != null) && (!campaign.getMessage().equals("")) ? "'" + campaign.getMessage() + "'" : "message";
/* 325:298 */     String last_updated = (campaign.getLastUpdated() != null) && (!campaign.getLastUpdated().equals("")) ? "'" + campaign.getLastUpdated() + "'" : "last_updated";
/* 326:    */     try
/* 327:    */     {
/* 328:301 */       con = DConnect.getConnection();
/* 329:    */       
/* 330:303 */       String SQL = "UPDATE vm_campaigns SET campaign_id = " + campaign_id + ", account_id = " + account_id + ", keyword = " + keyword + ", message_sender = " + message_sender + ", " + "message = " + message + ", last_updated = " + last_updated + " WHERE acount_id = ? and keyword = ? ";
/* 331:    */       
/* 332:    */ 
/* 333:306 */       prepstat = con.prepareStatement(SQL);
/* 334:    */       
/* 335:308 */       prepstat.setString(1, update_account_id);
/* 336:309 */       prepstat.setString(2, update_keyword);
/* 337:    */       
/* 338:311 */       prepstat.execute();
/* 339:    */     }
/* 340:    */     catch (Exception ex)
/* 341:    */     {
/* 342:314 */       if (con != null)
/* 343:    */       {
/* 344:    */         try
/* 345:    */         {
/* 346:316 */           con.close();
/* 347:    */         }
/* 348:    */         catch (SQLException ex1)
/* 349:    */         {
/* 350:318 */           System.out.println(ex1.getMessage());
/* 351:    */         }
/* 352:320 */         con = null;
/* 353:    */       }
/* 354:    */     }
/* 355:    */     finally
/* 356:    */     {
/* 357:323 */       if (rs != null)
/* 358:    */       {
/* 359:    */         try
/* 360:    */         {
/* 361:325 */           rs.close();
/* 362:    */         }
/* 363:    */         catch (SQLException e) {}
/* 364:329 */         rs = null;
/* 365:    */       }
/* 366:331 */       if (prepstat != null)
/* 367:    */       {
/* 368:    */         try
/* 369:    */         {
/* 370:333 */           prepstat.close();
/* 371:    */         }
/* 372:    */         catch (SQLException e) {}
/* 373:337 */         prepstat = null;
/* 374:    */       }
/* 375:339 */       if (con != null)
/* 376:    */       {
/* 377:    */         try
/* 378:    */         {
/* 379:341 */           con.close();
/* 380:    */         }
/* 381:    */         catch (SQLException e) {}
/* 382:345 */         con = null;
/* 383:    */       }
/* 384:    */     }
/* 385:    */   }
/* 386:    */   
/* 387:    */   public static void deleteCampaign(ArrayList keywords, String accountId)
/* 388:    */     throws Exception
/* 389:    */   {
/* 390:354 */     ResultSet rs = null;
/* 391:355 */     Connection con = null;
/* 392:356 */     PreparedStatement prepstat = null;
/* 393:    */     
/* 394:358 */     String keywordStr = "";
/* 395:359 */     for (int i = 0; i < keywords.size(); i++) {
/* 396:360 */       keywordStr = keywordStr + "'" + keywords.get(i).toString() + "',";
/* 397:    */     }
/* 398:362 */     keywordStr = keywordStr.substring(0, keywordStr.lastIndexOf(","));
/* 399:    */     try
/* 400:    */     {
/* 401:365 */       con = DConnect.getConnection();
/* 402:366 */       String SQL = "delete from vm_campaigns where keyword in (" + keywordStr + ") and account_id='" + accountId + "'";
/* 403:367 */       prepstat = con.prepareStatement(SQL);
/* 404:368 */       prepstat.execute();
/* 405:    */     }
/* 406:    */     catch (Exception ex)
/* 407:    */     {
/* 408:370 */       if (con != null)
/* 409:    */       {
/* 410:    */         try
/* 411:    */         {
/* 412:372 */           con.close();
/* 413:    */         }
/* 414:    */         catch (SQLException ex1)
/* 415:    */         {
/* 416:374 */           System.out.println(ex1.getMessage());
/* 417:    */         }
/* 418:376 */         con = null;
/* 419:    */       }
/* 420:    */     }
/* 421:    */     finally
/* 422:    */     {
/* 423:379 */       if (rs != null)
/* 424:    */       {
/* 425:    */         try
/* 426:    */         {
/* 427:381 */           rs.close();
/* 428:    */         }
/* 429:    */         catch (SQLException e) {}
/* 430:385 */         rs = null;
/* 431:    */       }
/* 432:387 */       if (prepstat != null)
/* 433:    */       {
/* 434:    */         try
/* 435:    */         {
/* 436:389 */           prepstat.close();
/* 437:    */         }
/* 438:    */         catch (SQLException e) {}
/* 439:393 */         prepstat = null;
/* 440:    */       }
/* 441:395 */       if (con != null)
/* 442:    */       {
/* 443:    */         try
/* 444:    */         {
/* 445:397 */           con.close();
/* 446:    */         }
/* 447:    */         catch (SQLException e) {}
/* 448:401 */         con = null;
/* 449:    */       }
/* 450:    */     }
/* 451:    */   }
/* 452:    */   
/* 453:    */   public static void deleteCampaign(String keyword, String accountId)
/* 454:    */     throws Exception
/* 455:    */   {
/* 456:409 */     ResultSet rs = null;
/* 457:410 */     Connection con = null;
/* 458:411 */     PreparedStatement prepstat = null;
/* 459:    */     try
/* 460:    */     {
/* 461:414 */       con = DConnect.getConnection();
/* 462:415 */       String SQL = "delete from vm_campaigns where keyword = ? and account_id=? ";
/* 463:    */       
/* 464:    */ 
/* 465:418 */       prepstat = con.prepareStatement(SQL);
/* 466:    */       
/* 467:420 */       prepstat.setString(1, keyword);
/* 468:421 */       prepstat.setString(2, accountId);
/* 469:    */       
/* 470:423 */       prepstat.execute();
/* 471:    */     }
/* 472:    */     catch (Exception ex)
/* 473:    */     {
/* 474:426 */       if (con != null)
/* 475:    */       {
/* 476:    */         try
/* 477:    */         {
/* 478:428 */           con.close();
/* 479:    */         }
/* 480:    */         catch (SQLException ex1)
/* 481:    */         {
/* 482:430 */           System.out.println(ex1.getMessage());
/* 483:    */         }
/* 484:432 */         con = null;
/* 485:    */       }
/* 486:    */     }
/* 487:    */     finally
/* 488:    */     {
/* 489:435 */       if (rs != null)
/* 490:    */       {
/* 491:    */         try
/* 492:    */         {
/* 493:437 */           rs.close();
/* 494:    */         }
/* 495:    */         catch (SQLException e) {}
/* 496:441 */         rs = null;
/* 497:    */       }
/* 498:443 */       if (prepstat != null)
/* 499:    */       {
/* 500:    */         try
/* 501:    */         {
/* 502:445 */           prepstat.close();
/* 503:    */         }
/* 504:    */         catch (SQLException e) {}
/* 505:449 */         prepstat = null;
/* 506:    */       }
/* 507:451 */       if (con != null)
/* 508:    */       {
/* 509:    */         try
/* 510:    */         {
/* 511:453 */           con.close();
/* 512:    */         }
/* 513:    */         catch (SQLException e) {}
/* 514:457 */         con = null;
/* 515:    */       }
/* 516:    */     }
/* 517:    */   }
/* 518:    */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.viralmarketing.VMCampaignDB
 * JD-Core Version:    0.7.0.1
 */