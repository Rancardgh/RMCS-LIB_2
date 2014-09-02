/*   1:    */ package com.rancard.mobility.infoserver.common.inbox;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import com.rancard.mobility.infoserver.smscompetition.Option;
/*   5:    */ import com.rancard.util.Page;
/*   6:    */ import java.sql.Connection;
/*   7:    */ import java.sql.PreparedStatement;
/*   8:    */ import java.sql.ResultSet;
/*   9:    */ import java.sql.Timestamp;
/*  10:    */ import java.util.ArrayList;
/*  11:    */ 
/*  12:    */ public abstract class InboxDB
/*  13:    */ {
/*  14:    */   public static Page viewInbox(String keyword, String accountId, Timestamp startdate, Timestamp enddate, int start, int count)
/*  15:    */     throws Exception
/*  16:    */   {
/*  17: 15 */     ArrayList responses = new ArrayList();
/*  18: 16 */     Page page = null;
/*  19:    */     
/*  20: 18 */     int y = 0;
/*  21: 19 */     int i = 0;
/*  22: 20 */     int numResults = 0;
/*  23: 21 */     boolean hasNext = false;
/*  24:    */     
/*  25: 23 */     String query = "Select * from inbox";
/*  26: 24 */     ResultSet rs = null;
/*  27: 25 */     Connection con = null;
/*  28: 26 */     PreparedStatement prepstat = null;
/*  29:    */     try
/*  30:    */     {
/*  31: 29 */       con = DConnect.getConnection();
/*  32: 30 */       if ((keyword != null) && (!keyword.equals(""))) {
/*  33: 31 */         if (query.indexOf("where") == -1) {
/*  34: 32 */           query = query + " where keyword='" + keyword + "'";
/*  35:    */         } else {
/*  36: 34 */           query = query + " and keyword='" + keyword + "'";
/*  37:    */         }
/*  38:    */       }
/*  39: 37 */       if ((accountId != null) && (!accountId.equals(""))) {
/*  40: 38 */         if (query.indexOf("where") == -1) {
/*  41: 39 */           query = query + " where account_id='" + accountId + "'";
/*  42:    */         } else {
/*  43: 41 */           query = query + " and account_id='" + accountId + "'";
/*  44:    */         }
/*  45:    */       }
/*  46: 44 */       if (startdate != null) {
/*  47: 45 */         if (query.indexOf("where") == -1) {
/*  48: 46 */           query = query + " where date_voted >='" + startdate + "'";
/*  49:    */         } else {
/*  50: 49 */           query = query + " and date_voted >='" + startdate + "'";
/*  51:    */         }
/*  52:    */       }
/*  53: 53 */       if (enddate != null) {
/*  54: 54 */         if (query.indexOf("where") == -1) {
/*  55: 55 */           query = query + " where date_voted <='" + enddate + "'";
/*  56:    */         } else {
/*  57: 58 */           query = query + " and date_voted <='" + enddate + "'";
/*  58:    */         }
/*  59:    */       }
/*  60: 61 */       query = query + " order by date_voted desc";
/*  61:    */       
/*  62: 63 */       prepstat = con.prepareStatement(query);
/*  63: 64 */       rs = prepstat.executeQuery();
/*  64:    */       
/*  65:    */ 
/*  66: 67 */       rs.last();
/*  67: 68 */       numResults = rs.getRow();
/*  68: 69 */       rs.beforeFirst();
/*  69: 71 */       while ((i < start + count) && (rs.next()))
/*  70:    */       {
/*  71: 72 */         if (i == 0)
/*  72:    */         {
/*  73: 73 */           int x = numResults;
/*  74: 74 */           y = x / count;
/*  75: 75 */           if (x % count > 0) {
/*  76: 76 */             y++;
/*  77:    */           }
/*  78:    */         }
/*  79: 79 */         if (i >= start)
/*  80:    */         {
/*  81: 81 */           InboxEntry newBean = new InboxEntry();
/*  82: 82 */           newBean.setAccountId(rs.getString("account_id"));
/*  83: 83 */           newBean.setDateReceived(rs.getTimestamp("date_voted"));
/*  84: 84 */           newBean.setMessage(rs.getString("response"));
/*  85: 85 */           newBean.setMessageId(rs.getString("voteid"));
/*  86: 86 */           newBean.setMsisdn(rs.getString("mobileno"));
/*  87: 87 */           newBean.setKeyword(rs.getString("keyword"));
/*  88: 88 */           newBean.setShortCode(rs.getString("short_code"));
/*  89: 89 */           newBean.setViewed(rs.getInt("is_read"));
/*  90:    */           
/*  91: 91 */           responses.add(newBean);
/*  92:    */         }
/*  93: 93 */         i++;
/*  94:    */       }
/*  95: 96 */       hasNext = rs.next();
/*  96: 97 */       page = new Page(responses, start, hasNext, y, numResults);
/*  97: 99 */       if (page == null) {
/*  98:100 */         page = Page.EMPTY_PAGE;
/*  99:    */       }
/* 100:    */     }
/* 101:    */     catch (Exception ex)
/* 102:    */     {
/* 103:104 */       if (con != null) {
/* 104:105 */         con.close();
/* 105:    */       }
/* 106:107 */       throw new Exception(ex.getMessage());
/* 107:    */     }
/* 108:109 */     if (con != null) {
/* 109:110 */       con.close();
/* 110:    */     }
/* 111:112 */     return page;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public static int getParticipationLevel(String keyword, String accountId)
/* 115:    */     throws Exception
/* 116:    */   {
/* 117:118 */     int level = 0;
/* 118:119 */     ResultSet rs = null;
/* 119:120 */     Connection con = null;
/* 120:121 */     PreparedStatement prepstat = null;
/* 121:    */     try
/* 122:    */     {
/* 123:123 */       con = DConnect.getConnection();
/* 124:    */       
/* 125:125 */       String SQL = "select count(distinct mobileno) as level from  inbox where keyword=? and account_id=?";
/* 126:    */       
/* 127:127 */       prepstat = con.prepareStatement(SQL);
/* 128:    */       
/* 129:129 */       prepstat.setString(1, keyword);
/* 130:130 */       prepstat.setString(2, accountId);
/* 131:    */       
/* 132:132 */       rs = prepstat.executeQuery();
/* 133:134 */       while (rs.next()) {
/* 134:135 */         level = rs.getInt("level");
/* 135:    */       }
/* 136:    */     }
/* 137:    */     catch (Exception ex)
/* 138:    */     {
/* 139:138 */       if (con != null) {
/* 140:139 */         con.close();
/* 141:    */       }
/* 142:141 */       throw new Exception(ex.getMessage());
/* 143:    */     }
/* 144:143 */     if (con != null) {
/* 145:144 */       con.close();
/* 146:    */     }
/* 147:147 */     return level;
/* 148:    */   }
/* 149:    */   
/* 150:    */   public static String write(InboxEntry entry)
/* 151:    */     throws Exception
/* 152:    */   {
/* 153:153 */     String voteid = entry.getMessageId();
/* 154:154 */     String keyword = entry.getKeyword();
/* 155:155 */     Timestamp dateVoted = entry.getDateReceived();
/* 156:156 */     String response = entry.getMessage();
/* 157:    */     
/* 158:158 */     String mobileno = entry.getMsisdn();
/* 159:159 */     String shortCode = entry.getShortCode();
/* 160:160 */     String provId = entry.getAccountId();
/* 161:    */     
/* 162:    */ 
/* 163:163 */     int level = 0;
/* 164:164 */     String compId = "";
/* 165:165 */     ResultSet rs = null;
/* 166:166 */     Connection con = null;
/* 167:167 */     PreparedStatement prepstat = null;
/* 168:    */     try
/* 169:    */     {
/* 170:169 */       con = DConnect.getConnection();
/* 171:    */       
/* 172:    */ 
/* 173:172 */       String SQL = "insert into inbox(voteid,keyword,date_voted,response,mobileno,short_code,account_id) values(?,?,?,?,?,?,?)";
/* 174:    */       
/* 175:174 */       prepstat = con.prepareStatement(SQL);
/* 176:    */       
/* 177:176 */       prepstat.setString(1, voteid);
/* 178:177 */       prepstat.setString(2, keyword);
/* 179:178 */       prepstat.setTimestamp(3, dateVoted);
/* 180:179 */       prepstat.setString(4, response);
/* 181:180 */       prepstat.setString(5, mobileno);
/* 182:181 */       prepstat.setString(6, shortCode);
/* 183:182 */       prepstat.setString(7, provId);
/* 184:    */       
/* 185:184 */       prepstat.execute();
/* 186:    */     }
/* 187:    */     catch (Exception ex)
/* 188:    */     {
/* 189:223 */       if (con != null) {
/* 190:224 */         con.close();
/* 191:    */       }
/* 192:226 */       throw new Exception(ex.getMessage());
/* 193:    */     }
/* 194:228 */     if (con != null) {
/* 195:229 */       con.close();
/* 196:    */     }
/* 197:231 */     return voteid;
/* 198:    */   }
/* 199:    */   
/* 200:    */   public static void updateVote(InboxEntry entry)
/* 201:    */     throws Exception
/* 202:    */   {
/* 203:237 */     String voteid = entry.getMessageId();
/* 204:238 */     String keyword = entry.getKeyword();
/* 205:239 */     Timestamp dateVoted = entry.getDateReceived();
/* 206:240 */     String response = entry.getMessage();
/* 207:    */     
/* 208:242 */     String mobileno = entry.getMsisdn();
/* 209:243 */     String shortCode = entry.getShortCode();
/* 210:244 */     String provId = entry.getAccountId();
/* 211:    */     
/* 212:    */ 
/* 213:247 */     ResultSet rs = null;
/* 214:248 */     Connection con = null;
/* 215:249 */     PreparedStatement prepstat = null;
/* 216:    */     try
/* 217:    */     {
/* 218:251 */       con = DConnect.getConnection();
/* 219:    */       
/* 220:253 */       String SQL = "update inbox set response=?,date_voted=? where mobileno=? and keyword=? and account_id=?";
/* 221:    */       
/* 222:255 */       prepstat = con.prepareStatement(SQL);
/* 223:    */       
/* 224:257 */       prepstat.setString(1, response);
/* 225:258 */       prepstat.setTimestamp(2, dateVoted);
/* 226:259 */       prepstat.setString(3, mobileno);
/* 227:    */       
/* 228:261 */       prepstat.setString(4, keyword);
/* 229:262 */       prepstat.setString(5, provId);
/* 230:    */       
/* 231:264 */       prepstat.execute();
/* 232:    */     }
/* 233:    */     catch (Exception ex)
/* 234:    */     {
/* 235:267 */       if (con != null) {
/* 236:268 */         con.close();
/* 237:    */       }
/* 238:270 */       throw new Exception(ex.getMessage());
/* 239:    */     }
/* 240:272 */     if (con != null) {
/* 241:273 */       con.close();
/* 242:    */     }
/* 243:    */   }
/* 244:    */   
/* 245:    */   public static void deleteVote(String keyword, String msisdn, String accountId)
/* 246:    */     throws Exception
/* 247:    */   {
/* 248:280 */     ResultSet rs = null;
/* 249:281 */     Connection con = null;
/* 250:282 */     PreparedStatement prepstat = null;
/* 251:    */     try
/* 252:    */     {
/* 253:284 */       con = DConnect.getConnection();
/* 254:    */       
/* 255:286 */       String SQL = "delete from inbox where keyword=? and mobileno=? and account_id=?";
/* 256:    */       
/* 257:    */ 
/* 258:289 */       prepstat = con.prepareStatement(SQL);
/* 259:    */       
/* 260:291 */       prepstat.setString(1, keyword);
/* 261:    */       
/* 262:293 */       prepstat.setString(2, msisdn);
/* 263:294 */       prepstat.setString(3, accountId);
/* 264:    */       
/* 265:296 */       prepstat.execute();
/* 266:    */     }
/* 267:    */     catch (Exception ex)
/* 268:    */     {
/* 269:299 */       if (con != null) {
/* 270:300 */         con.close();
/* 271:    */       }
/* 272:302 */       throw new Exception(ex.getMessage());
/* 273:    */     }
/* 274:304 */     if (con != null) {
/* 275:305 */       con.close();
/* 276:    */     }
/* 277:    */   }
/* 278:    */   
/* 279:    */   public static void deleteVotes(String keyword, String accountId)
/* 280:    */     throws Exception
/* 281:    */   {
/* 282:312 */     ResultSet rs = null;
/* 283:313 */     Connection con = null;
/* 284:314 */     PreparedStatement prepstat = null;
/* 285:    */     try
/* 286:    */     {
/* 287:316 */       con = DConnect.getConnection();
/* 288:    */       
/* 289:318 */       String SQL = "delete from inbox where keyword=? and account_id=?";
/* 290:    */       
/* 291:    */ 
/* 292:321 */       prepstat = con.prepareStatement(SQL);
/* 293:    */       
/* 294:323 */       prepstat.setString(1, keyword);
/* 295:    */       
/* 296:325 */       prepstat.setString(2, accountId);
/* 297:    */       
/* 298:327 */       prepstat.execute();
/* 299:    */     }
/* 300:    */     catch (Exception ex)
/* 301:    */     {
/* 302:330 */       if (con != null) {
/* 303:331 */         con.close();
/* 304:    */       }
/* 305:333 */       throw new Exception(ex.getMessage());
/* 306:    */     }
/* 307:335 */     if (con != null) {
/* 308:336 */       con.close();
/* 309:    */     }
/* 310:    */   }
/* 311:    */   
/* 312:    */   public static void deleteVote(String entryId)
/* 313:    */     throws Exception
/* 314:    */   {
/* 315:343 */     ResultSet rs = null;
/* 316:344 */     Connection con = null;
/* 317:345 */     PreparedStatement prepstat = null;
/* 318:    */     try
/* 319:    */     {
/* 320:347 */       con = DConnect.getConnection();
/* 321:    */       
/* 322:349 */       String SQL = "delete from inbox where voteid=?";
/* 323:    */       
/* 324:351 */       prepstat = con.prepareStatement(SQL);
/* 325:352 */       prepstat.setString(1, entryId);
/* 326:353 */       prepstat.execute();
/* 327:    */     }
/* 328:    */     catch (Exception ex)
/* 329:    */     {
/* 330:356 */       if (con != null) {
/* 331:357 */         con.close();
/* 332:    */       }
/* 333:359 */       throw new Exception(ex.getMessage());
/* 334:    */     }
/* 335:361 */     if (con != null) {
/* 336:362 */       con.close();
/* 337:    */     }
/* 338:    */   }
/* 339:    */   
/* 340:    */   public static InboxEntry viewEntry(String keyword, String msisdn, String accountId)
/* 341:    */     throws Exception
/* 342:    */   {
/* 343:368 */     InboxEntry entry = new InboxEntry();
/* 344:    */     
/* 345:370 */     ResultSet rs = null;
/* 346:371 */     Connection con = null;
/* 347:372 */     PreparedStatement prepstat = null;
/* 348:    */     try
/* 349:    */     {
/* 350:374 */       con = DConnect.getConnection();
/* 351:    */       
/* 352:376 */       String SQL = "select * from  inbox where keyword=? and mobileno=? and account_id=?";
/* 353:    */       
/* 354:378 */       prepstat = con.prepareStatement(SQL);
/* 355:    */       
/* 356:380 */       prepstat.setString(1, keyword);
/* 357:    */       
/* 358:382 */       prepstat.setString(2, msisdn);
/* 359:383 */       prepstat.setString(3, accountId);
/* 360:    */       
/* 361:385 */       rs = prepstat.executeQuery();
/* 362:387 */       while (rs.next())
/* 363:    */       {
/* 364:388 */         entry.setDateReceived(rs.getTimestamp("date_voted"));
/* 365:389 */         entry.setMessage(rs.getString("response"));
/* 366:390 */         entry.setMessageId(rs.getString("voteid"));
/* 367:391 */         entry.setMsisdn(rs.getString("mobileno"));
/* 368:    */         
/* 369:393 */         entry.setKeyword(rs.getString("keyword"));
/* 370:394 */         entry.setShortCode(rs.getString("short_code"));
/* 371:395 */         entry.setAccountId(rs.getString("account_id"));
/* 372:396 */         entry.setViewed(rs.getInt("is_read"));
/* 373:    */       }
/* 374:    */     }
/* 375:    */     catch (Exception ex)
/* 376:    */     {
/* 377:399 */       if (con != null) {
/* 378:400 */         con.close();
/* 379:    */       }
/* 380:402 */       throw new Exception(ex.getMessage());
/* 381:    */     }
/* 382:404 */     if (con != null) {
/* 383:405 */       con.close();
/* 384:    */     }
/* 385:408 */     return entry;
/* 386:    */   }
/* 387:    */   
/* 388:    */   public static InboxEntry viewEntry(String entryId)
/* 389:    */     throws Exception
/* 390:    */   {
/* 391:413 */     InboxEntry entry = new InboxEntry();
/* 392:    */     
/* 393:415 */     ResultSet rs = null;
/* 394:416 */     Connection con = null;
/* 395:417 */     PreparedStatement prepstat = null;
/* 396:    */     try
/* 397:    */     {
/* 398:419 */       con = DConnect.getConnection();
/* 399:    */       
/* 400:421 */       String SQL = "select * from  inbox where voteid=?";
/* 401:422 */       prepstat = con.prepareStatement(SQL);
/* 402:423 */       prepstat.setString(1, entryId);
/* 403:424 */       rs = prepstat.executeQuery();
/* 404:426 */       while (rs.next())
/* 405:    */       {
/* 406:427 */         entry.setDateReceived(rs.getTimestamp("date_voted"));
/* 407:428 */         entry.setMessage(rs.getString("response"));
/* 408:429 */         entry.setMessageId(rs.getString("voteid"));
/* 409:430 */         entry.setMsisdn(rs.getString("mobileno"));
/* 410:431 */         entry.setKeyword(rs.getString("keyword"));
/* 411:432 */         entry.setShortCode(rs.getString("short_code"));
/* 412:433 */         entry.setAccountId(rs.getString("account_id"));
/* 413:434 */         entry.setViewed(rs.getInt("is_read"));
/* 414:    */       }
/* 415:    */     }
/* 416:    */     catch (Exception ex)
/* 417:    */     {
/* 418:437 */       if (con != null) {
/* 419:438 */         con.close();
/* 420:    */       }
/* 421:440 */       throw new Exception(ex.getMessage());
/* 422:    */     }
/* 423:442 */     if (con != null) {
/* 424:443 */       con.close();
/* 425:    */     }
/* 426:446 */     return entry;
/* 427:    */   }
/* 428:    */   
/* 429:    */   public static ArrayList viewEntries(String keyword, String accountId)
/* 430:    */     throws Exception
/* 431:    */   {
/* 432:451 */     ArrayList entries = new ArrayList();
/* 433:    */     
/* 434:453 */     ResultSet rs = null;
/* 435:454 */     Connection con = null;
/* 436:455 */     PreparedStatement prepstat = null;
/* 437:    */     try
/* 438:    */     {
/* 439:457 */       con = DConnect.getConnection();
/* 440:    */       
/* 441:459 */       String SQL = "select * from  inbox where keyword=? and account_id=?";
/* 442:    */       
/* 443:461 */       prepstat = con.prepareStatement(SQL);
/* 444:    */       
/* 445:463 */       prepstat.setString(1, keyword);
/* 446:    */       
/* 447:465 */       prepstat.setString(2, accountId);
/* 448:    */       
/* 449:467 */       rs = prepstat.executeQuery();
/* 450:469 */       while (rs.next())
/* 451:    */       {
/* 452:470 */         InboxEntry entry = new InboxEntry();
/* 453:471 */         entry.setDateReceived(rs.getTimestamp("date_voted"));
/* 454:472 */         entry.setMessage(rs.getString("response"));
/* 455:473 */         entry.setMessageId(rs.getString("voteid"));
/* 456:474 */         entry.setMsisdn(rs.getString("mobileno"));
/* 457:    */         
/* 458:476 */         entry.setKeyword(rs.getString("keyword"));
/* 459:477 */         entry.setShortCode(rs.getString("short_code"));
/* 460:478 */         entry.setAccountId(rs.getString("account_id"));
/* 461:479 */         entry.setViewed(rs.getInt("is_read"));
/* 462:480 */         entries.add(entry);
/* 463:    */       }
/* 464:    */     }
/* 465:    */     catch (Exception ex)
/* 466:    */     {
/* 467:483 */       if (con != null) {
/* 468:484 */         con.close();
/* 469:    */       }
/* 470:486 */       throw new Exception(ex.getMessage());
/* 471:    */     }
/* 472:488 */     if (con != null) {
/* 473:489 */       con.close();
/* 474:    */     }
/* 475:492 */     return entries;
/* 476:    */   }
/* 477:    */   
/* 478:    */   public static ArrayList viewEntries(String competitionId)
/* 479:    */     throws Exception
/* 480:    */   {
/* 481:497 */     ArrayList entries = new ArrayList();
/* 482:    */     
/* 483:499 */     ResultSet rs = null;
/* 484:500 */     Connection con = null;
/* 485:501 */     PreparedStatement prepstat = null;
/* 486:    */     try
/* 487:    */     {
/* 488:503 */       con = DConnect.getConnection();
/* 489:    */       
/* 490:505 */       String SQL = "select ib.* from inbox ib inner join competitions cmp on ib.keyword =cmp.keyword and ib.account_id = cmp.account_id where cmp.competition_id=?  and ib.date_voted BETWEEN cmp.startdate and cmp.enddate";
/* 491:    */       
/* 492:    */ 
/* 493:508 */       prepstat = con.prepareStatement(SQL);
/* 494:509 */       prepstat.setString(1, competitionId);
/* 495:510 */       rs = prepstat.executeQuery();
/* 496:512 */       while (rs.next())
/* 497:    */       {
/* 498:513 */         InboxEntry entry = new InboxEntry();
/* 499:514 */         entry.setDateReceived(rs.getTimestamp("date_voted"));
/* 500:515 */         entry.setMessage(rs.getString("response"));
/* 501:516 */         entry.setMessageId(rs.getString("voteid"));
/* 502:517 */         entry.setMsisdn(rs.getString("mobileno"));
/* 503:518 */         entry.setKeyword(rs.getString("keyword"));
/* 504:519 */         entry.setShortCode(rs.getString("short_code"));
/* 505:520 */         entry.setAccountId(rs.getString("account_id"));
/* 506:521 */         entry.setViewed(rs.getInt("is_read"));
/* 507:522 */         entries.add(entry);
/* 508:    */       }
/* 509:    */     }
/* 510:    */     catch (Exception ex)
/* 511:    */     {
/* 512:525 */       if (con != null) {
/* 513:526 */         con.close();
/* 514:    */       }
/* 515:528 */       throw new Exception(ex.getMessage());
/* 516:    */     }
/* 517:530 */     if (con != null) {
/* 518:531 */       con.close();
/* 519:    */     }
/* 520:534 */     return entries;
/* 521:    */   }
/* 522:    */   
/* 523:    */   public static ArrayList viewEntriesOnDate(String competitionId, String date)
/* 524:    */     throws Exception
/* 525:    */   {
/* 526:539 */     ArrayList entries = new ArrayList();
/* 527:    */     
/* 528:541 */     ResultSet rs = null;
/* 529:542 */     Connection con = null;
/* 530:543 */     PreparedStatement prepstat = null;
/* 531:    */     try
/* 532:    */     {
/* 533:545 */       con = DConnect.getConnection();
/* 534:    */       
/* 535:547 */       String SQL = "select ib.* from inbox ib inner join competitions cmp on ib.keyword =cmp.keyword and ib.account_id = cmp.account_id where cmp.competition_id=?  and ib.date_voted BETWEEN cmp.startdate and cmp.enddate and ib.date_voted = ?";
/* 536:    */       
/* 537:    */ 
/* 538:    */ 
/* 539:551 */       prepstat = con.prepareStatement(SQL);
/* 540:552 */       prepstat.setString(1, competitionId);
/* 541:553 */       prepstat.setString(2, date);
/* 542:554 */       rs = prepstat.executeQuery();
/* 543:556 */       while (rs.next())
/* 544:    */       {
/* 545:557 */         InboxEntry entry = new InboxEntry();
/* 546:558 */         entry.setDateReceived(rs.getTimestamp("date_voted"));
/* 547:559 */         entry.setMessage(rs.getString("response"));
/* 548:560 */         entry.setMessageId(rs.getString("voteid"));
/* 549:561 */         entry.setMsisdn(rs.getString("mobileno"));
/* 550:562 */         entry.setKeyword(rs.getString("keyword"));
/* 551:563 */         entry.setShortCode(rs.getString("short_code"));
/* 552:564 */         entry.setAccountId(rs.getString("account_id"));
/* 553:565 */         entry.setViewed(rs.getInt("is_read"));
/* 554:566 */         entries.add(entry);
/* 555:    */       }
/* 556:    */     }
/* 557:    */     catch (Exception ex)
/* 558:    */     {
/* 559:569 */       if (con != null) {
/* 560:570 */         con.close();
/* 561:    */       }
/* 562:572 */       throw new Exception(ex.getMessage());
/* 563:    */     }
/* 564:574 */     if (con != null) {
/* 565:575 */       con.close();
/* 566:    */     }
/* 567:578 */     return entries;
/* 568:    */   }
/* 569:    */   
/* 570:    */   public static ArrayList viewCorrectEntries(String competitionId)
/* 571:    */     throws Exception
/* 572:    */   {
/* 573:583 */     ArrayList entries = new ArrayList();
/* 574:    */     
/* 575:585 */     ResultSet rs = null;
/* 576:586 */     Connection con = null;
/* 577:587 */     PreparedStatement prepstat = null;
/* 578:    */     try
/* 579:    */     {
/* 580:589 */       con = DConnect.getConnection();
/* 581:    */       
/* 582:591 */       String SQL = "select answer from competitions where competition_id=?";
/* 583:592 */       prepstat.setString(1, competitionId);
/* 584:593 */       rs = prepstat.executeQuery();
/* 585:594 */       String ans = new String();
/* 586:595 */       while (rs.next()) {
/* 587:596 */         ans = rs.getString("answer");
/* 588:    */       }
/* 589:599 */       SQL = "select ib.* from inbox ib inner join competitions cmp on ib.keyword =cmp.keyword and ib.account_id = cmp.account_id where cmp.competition_id=?  and ib.date_voted BETWEEN cmp.startdate and cmp.enddate and ib.response like '%" + ans + "%'";
/* 590:    */       
/* 591:    */ 
/* 592:602 */       prepstat = con.prepareStatement(SQL);
/* 593:603 */       prepstat.setString(1, competitionId);
/* 594:604 */       rs = prepstat.executeQuery();
/* 595:606 */       while (rs.next())
/* 596:    */       {
/* 597:607 */         InboxEntry entry = new InboxEntry();
/* 598:608 */         entry.setDateReceived(rs.getTimestamp("date_voted"));
/* 599:609 */         entry.setMessage(rs.getString("response"));
/* 600:610 */         entry.setMessageId(rs.getString("voteid"));
/* 601:611 */         entry.setMsisdn(rs.getString("mobileno"));
/* 602:612 */         entry.setKeyword(rs.getString("keyword"));
/* 603:613 */         entry.setShortCode(rs.getString("short_code"));
/* 604:614 */         entry.setAccountId(rs.getString("account_id"));
/* 605:615 */         entry.setViewed(rs.getInt("is_read"));
/* 606:616 */         entries.add(entry);
/* 607:    */       }
/* 608:    */     }
/* 609:    */     catch (Exception ex)
/* 610:    */     {
/* 611:619 */       if (con != null) {
/* 612:620 */         con.close();
/* 613:    */       }
/* 614:622 */       throw new Exception(ex.getMessage());
/* 615:    */     }
/* 616:624 */     if (con != null) {
/* 617:625 */       con.close();
/* 618:    */     }
/* 619:628 */     return entries;
/* 620:    */   }
/* 621:    */   
/* 622:    */   public static ArrayList viewWinners_predefined(String keyword, String accountId, int filter)
/* 623:    */     throws Exception
/* 624:    */   {
/* 625:633 */     String SQL = new String();
/* 626:634 */     ResultSet rs = null;
/* 627:635 */     Connection con = null;
/* 628:636 */     PreparedStatement prepstat = null;
/* 629:637 */     ArrayList winners = new ArrayList();
/* 630:    */     try
/* 631:    */     {
/* 632:640 */       con = DConnect.getConnection();
/* 633:642 */       if (filter == 1) {
/* 634:643 */         SQL = "select distinct q.mobileno from inbox q inner join competitions c on q.response=c.answer  where q.keyword=? and q.account_id=? and q.date_voted BETWEEN c.startdate and c.enddate";
/* 635:645 */       } else if (filter == 0) {
/* 636:646 */         SQL = "select distinct q.mobileno from inbox q, competitions a, quickquestions_options o where q.keyword=? and q.account_id=? and o.description=q.response or q.response=a.answer and q.date_voted BETWEEN a.startdate and a.enddate";
/* 637:    */       }
/* 638:649 */       prepstat = con.prepareStatement(SQL);
/* 639:    */       
/* 640:651 */       prepstat.setString(1, keyword);
/* 641:652 */       prepstat.setString(2, accountId);
/* 642:    */       
/* 643:654 */       rs = prepstat.executeQuery();
/* 644:656 */       while (rs.next()) {
/* 645:657 */         winners.add(rs.getString("mobileno"));
/* 646:    */       }
/* 647:    */     }
/* 648:    */     catch (Exception ex)
/* 649:    */     {
/* 650:660 */       if (con != null) {
/* 651:661 */         con.close();
/* 652:    */       }
/* 653:663 */       throw new Exception(ex.getMessage());
/* 654:    */     }
/* 655:665 */     if (con != null) {
/* 656:666 */       con.close();
/* 657:    */     }
/* 658:668 */     return winners;
/* 659:    */   }
/* 660:    */   
/* 661:    */   public static ArrayList viewWinners_userdefined(String keyword, String accountId, String[] answers, int filter)
/* 662:    */     throws Exception
/* 663:    */   {
/* 664:674 */     String SQL = new String();
/* 665:675 */     ResultSet rs = null;
/* 666:676 */     Connection con = null;
/* 667:677 */     PreparedStatement prepstat = null;
/* 668:678 */     ArrayList winners = new ArrayList();
/* 669:679 */     String answerStr = new String();
/* 670:681 */     if (answers.length > 0) {
/* 671:682 */       if (filter == 1)
/* 672:    */       {
/* 673:685 */         String answer = "or q.response='" + answers[0] + "'";
/* 674:686 */         if (answers.length > 1) {
/* 675:687 */           for (int i = 1; i < answers.length; i++) {
/* 676:688 */             answer = answer + " or q.response='" + answers[i] + "'";
/* 677:    */           }
/* 678:    */         }
/* 679:691 */         answerStr = answer;
/* 680:    */       }
/* 681:692 */       else if (filter == 0)
/* 682:    */       {
/* 683:696 */         String answer = "or q.response like '%" + answers[0] + "%'";
/* 684:697 */         if (answers.length > 1) {
/* 685:698 */           for (int i = 1; i < answers.length; i++) {
/* 686:699 */             answer = answer + " or q.response like '%" + answers[i] + "%'";
/* 687:    */           }
/* 688:    */         }
/* 689:702 */         answerStr = answer;
/* 690:    */       }
/* 691:    */     }
/* 692:    */     try
/* 693:    */     {
/* 694:707 */       con = DConnect.getConnection();
/* 695:    */       
/* 696:709 */       SQL = "select distinct q.mobileno from inbox q inner join competitions a on q.response=a.answer " + answerStr + " where q.keyword='" + keyword + "' and  q.account_id='" + accountId + "' and q.date_voted BETWEEN a.startdate and a.enddate";
/* 697:    */       
/* 698:    */ 
/* 699:712 */       prepstat = con.prepareStatement(SQL);
/* 700:    */       
/* 701:714 */       rs = prepstat.executeQuery();
/* 702:716 */       while (rs.next()) {
/* 703:717 */         winners.add(rs.getString("mobileno"));
/* 704:    */       }
/* 705:    */     }
/* 706:    */     catch (Exception ex)
/* 707:    */     {
/* 708:720 */       if (con != null) {
/* 709:721 */         con.close();
/* 710:    */       }
/* 711:723 */       throw new Exception(ex.getMessage());
/* 712:    */     }
/* 713:725 */     if (con != null) {
/* 714:726 */       con.close();
/* 715:    */     }
/* 716:728 */     return winners;
/* 717:    */   }
/* 718:    */   
/* 719:    */   public static ArrayList viewWinners_predefined(String competitionId, int filter)
/* 720:    */     throws Exception
/* 721:    */   {
/* 722:733 */     String SQL = new String();
/* 723:734 */     ResultSet rs = null;
/* 724:735 */     Connection con = null;
/* 725:736 */     PreparedStatement prepstat = null;
/* 726:737 */     ArrayList winners = new ArrayList();
/* 727:    */     try
/* 728:    */     {
/* 729:740 */       con = DConnect.getConnection();
/* 730:742 */       if (filter == 1) {
/* 731:743 */         SQL = "select distinct q.mobileno from inbox q inner join competitions c on q.response=c.answer  where q.keyword=c.keyword and q.account_id=c.account_id and q.date_voted BETWEEN c.startdate and c.enddate and c.competition_id='" + competitionId + "'";
/* 732:746 */       } else if (filter == 0) {
/* 733:747 */         SQL = "select distinct q.mobileno from inbox q, competitions a, quickquestions_options o where q.keyword=a.keyword and q.account_id=a.account_id and o.description=q.response or q.response=a.answer and q.date_voted BETWEEN a.startdate and a.enddate and a.competition_id='" + competitionId + "'";
/* 734:    */       }
/* 735:751 */       prepstat = con.prepareStatement(SQL);
/* 736:    */       
/* 737:753 */       rs = prepstat.executeQuery();
/* 738:755 */       while (rs.next()) {
/* 739:756 */         winners.add(rs.getString("mobileno"));
/* 740:    */       }
/* 741:    */     }
/* 742:    */     catch (Exception ex)
/* 743:    */     {
/* 744:759 */       if (con != null) {
/* 745:760 */         con.close();
/* 746:    */       }
/* 747:762 */       throw new Exception(ex.getMessage());
/* 748:    */     }
/* 749:764 */     if (con != null) {
/* 750:765 */       con.close();
/* 751:    */     }
/* 752:767 */     return winners;
/* 753:    */   }
/* 754:    */   
/* 755:    */   public static ArrayList viewWinners_userdefined(String competitionId, String[] answers, int filter)
/* 756:    */     throws Exception
/* 757:    */   {
/* 758:773 */     String SQL = new String();
/* 759:774 */     ResultSet rs = null;
/* 760:775 */     Connection con = null;
/* 761:776 */     PreparedStatement prepstat = null;
/* 762:777 */     ArrayList winners = new ArrayList();
/* 763:778 */     String answerStr = new String();
/* 764:780 */     if (answers.length > 0) {
/* 765:781 */       if (filter == 1)
/* 766:    */       {
/* 767:784 */         String answer = "or q.response='" + answers[0] + "'";
/* 768:785 */         if (answers.length > 1) {
/* 769:786 */           for (int i = 1; i < answers.length; i++) {
/* 770:787 */             answer = answer + " or q.response='" + answers[i] + "'";
/* 771:    */           }
/* 772:    */         }
/* 773:790 */         answerStr = answer;
/* 774:    */       }
/* 775:791 */       else if (filter == 0)
/* 776:    */       {
/* 777:795 */         String answer = "or q.response like '%" + answers[0] + "%'";
/* 778:796 */         if (answers.length > 1) {
/* 779:797 */           for (int i = 1; i < answers.length; i++) {
/* 780:798 */             answer = answer + " or q.response like '%" + answers[i] + "%'";
/* 781:    */           }
/* 782:    */         }
/* 783:801 */         answerStr = answer;
/* 784:    */       }
/* 785:    */     }
/* 786:    */     try
/* 787:    */     {
/* 788:806 */       con = DConnect.getConnection();
/* 789:    */       
/* 790:808 */       SQL = "select distinct q.mobileno from inbox q inner join competitions a on (q.response=a.answer " + answerStr + ") and q.keyword=a.keyword and  q.account_id=a.account_id" + " and q.date_voted BETWEEN a.startdate and a.enddate and a.competition_id='" + competitionId + "'";
/* 791:    */       
/* 792:    */ 
/* 793:811 */       prepstat = con.prepareStatement(SQL);
/* 794:    */       
/* 795:813 */       rs = prepstat.executeQuery();
/* 796:815 */       while (rs.next()) {
/* 797:816 */         winners.add(rs.getString("mobileno"));
/* 798:    */       }
/* 799:    */     }
/* 800:    */     catch (Exception ex)
/* 801:    */     {
/* 802:819 */       if (con != null) {
/* 803:820 */         con.close();
/* 804:    */       }
/* 805:822 */       throw new Exception(ex.getMessage());
/* 806:    */     }
/* 807:824 */     if (con != null) {
/* 808:825 */       con.close();
/* 809:    */     }
/* 810:827 */     return winners;
/* 811:    */   }
/* 812:    */   
/* 813:    */   public static ArrayList calculateResults(String competitionId)
/* 814:    */     throws Exception
/* 815:    */   {
/* 816:831 */     boolean voted = false;
/* 817:    */     
/* 818:833 */     ResultSet rs = null;
/* 819:834 */     Connection con = null;
/* 820:835 */     PreparedStatement prepstat = null;
/* 821:836 */     int total_votes = 0;
/* 822:837 */     ArrayList votePercentages = new ArrayList();
/* 823:    */     try
/* 824:    */     {
/* 825:840 */       con = DConnect.getConnection();
/* 826:    */       
/* 827:    */ 
/* 828:843 */       String SQL = "select * from quickquestions_options where competition_id=?";
/* 829:844 */       prepstat.setString(1, competitionId);
/* 830:845 */       rs = prepstat.executeQuery();
/* 831:846 */       String ans = new String();
/* 832:847 */       while (rs.next())
/* 833:    */       {
/* 834:848 */         Option option = new Option();
/* 835:849 */         option.setQuestionId(competitionId);
/* 836:850 */         option.setAccountId(rs.getString("account_id"));
/* 837:851 */         option.setDescription(rs.getString("description"));
/* 838:852 */         option.setKeyword(rs.getString("keyword"));
/* 839:853 */         option.setOptionId(rs.getString("optionid"));
/* 840:    */         
/* 841:855 */         votePercentages.add(option);
/* 842:    */       }
/* 843:859 */       SQL = "select COUNT(*) from inbox ib inner join competitions cmp on ib.keyword =cmp.keyword and ib.account_id = cmp.account_id where cmp.competition_id=?  and ib.date_voted BETWEEN cmp.startdate and cmp.enddate";
/* 844:    */       
/* 845:861 */       prepstat = con.prepareStatement(SQL);
/* 846:    */       
/* 847:863 */       prepstat.setString(1, competitionId);
/* 848:864 */       rs = prepstat.executeQuery();
/* 849:865 */       while (rs.next()) {
/* 850:866 */         total_votes = rs.getInt("count");
/* 851:    */       }
/* 852:870 */       SQL = "select Count(*)as option_total from quickquestions_votes where optionid=?";
/* 853:871 */       prepstat = con.prepareStatement(SQL);
/* 854:872 */       int option_total = 0;
/* 855:873 */       double perc = 0.0D;
/* 856:874 */       for (int k = 0; k < votePercentages.size(); k++)
/* 857:    */       {
/* 858:875 */         prepstat.setString(1, ((Option)votePercentages.get(k)).getOptionId());
/* 859:876 */         rs = prepstat.executeQuery();
/* 860:877 */         while (rs.next()) {
/* 861:878 */           option_total = rs.getInt("option_total");
/* 862:    */         }
/* 863:881 */         perc = option_total / total_votes;
/* 864:882 */         perc *= 100.0D;
/* 865:883 */         perc = Math.round(perc);
/* 866:884 */         ((Option)votePercentages.get(k)).setPercVoted(new Double(perc).toString() + "%");
/* 867:    */       }
/* 868:    */     }
/* 869:    */     catch (Exception ex)
/* 870:    */     {
/* 871:888 */       if (con != null) {
/* 872:889 */         con.close();
/* 873:    */       }
/* 874:891 */       throw new Exception(ex.getMessage());
/* 875:    */     }
/* 876:893 */     if (con != null) {
/* 877:894 */       con.close();
/* 878:    */     }
/* 879:896 */     return votePercentages;
/* 880:    */   }
/* 881:    */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.common.inbox.InboxDB
 * JD-Core Version:    0.7.0.1
 */