/*   1:    */ package com.rancard.security;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import java.sql.Connection;
/*   5:    */ import java.sql.Date;
/*   6:    */ import java.sql.PreparedStatement;
/*   7:    */ import java.sql.ResultSet;
/*   8:    */ import java.sql.SQLException;
/*   9:    */ import java.util.ArrayList;
/*  10:    */ 
/*  11:    */ public class AccessListItemDB
/*  12:    */ {
/*  13:    */   public void SaveToWhiteList(String ipAddress, String owner_id)
/*  14:    */     throws Exception
/*  15:    */   {
/*  16: 17 */     ResultSet rs = null;
/*  17: 18 */     Connection con = null;
/*  18: 19 */     PreparedStatement prepstat = null;
/*  19: 20 */     AccessListItem userIP = new AccessListItem();
/*  20:    */     try
/*  21:    */     {
/*  22: 23 */       con = DConnect.getConnection();
/*  23: 24 */       Date tempdate = null;
/*  24:    */       
/*  25: 26 */       String SQL = "insert into whitelist  (allowed_IP, owner_id) values (?,?)";
/*  26: 27 */       prepstat = con.prepareStatement(SQL);
/*  27: 28 */       prepstat.setString(1, ipAddress);
/*  28: 29 */       prepstat.setString(2, owner_id);
/*  29: 30 */       prepstat.execute();
/*  30: 31 */       prepstat.close();
/*  31: 32 */       prepstat = null;
/*  32: 33 */       con.close();
/*  33: 34 */       con = null;
/*  34:    */     }
/*  35:    */     catch (Exception ex)
/*  36:    */     {
/*  37: 36 */       if (con != null)
/*  38:    */       {
/*  39: 37 */         con.close();
/*  40: 38 */         con = null;
/*  41:    */       }
/*  42: 40 */       throw new Exception(ex.getMessage());
/*  43:    */     }
/*  44:    */     finally
/*  45:    */     {
/*  46: 42 */       if (rs != null)
/*  47:    */       {
/*  48:    */         try
/*  49:    */         {
/*  50: 44 */           rs.close();
/*  51:    */         }
/*  52:    */         catch (SQLException e) {}
/*  53: 48 */         rs = null;
/*  54:    */       }
/*  55: 50 */       if (prepstat != null)
/*  56:    */       {
/*  57:    */         try
/*  58:    */         {
/*  59: 52 */           prepstat.close();
/*  60:    */         }
/*  61:    */         catch (SQLException e) {}
/*  62: 56 */         prepstat = null;
/*  63:    */       }
/*  64: 58 */       if (con != null)
/*  65:    */       {
/*  66:    */         try
/*  67:    */         {
/*  68: 60 */           con.close();
/*  69:    */         }
/*  70:    */         catch (SQLException e) {}
/*  71: 64 */         con = null;
/*  72:    */       }
/*  73:    */     }
/*  74:    */   }
/*  75:    */   
/*  76:    */   public void SaveToBlackList(String ipAddress, String owner_id)
/*  77:    */     throws Exception
/*  78:    */   {
/*  79: 74 */     ResultSet rs = null;
/*  80: 75 */     Connection con = null;
/*  81: 76 */     PreparedStatement prepstat = null;
/*  82: 77 */     AccessListItem userIP = new AccessListItem();
/*  83:    */     try
/*  84:    */     {
/*  85: 80 */       con = DConnect.getConnection();
/*  86: 81 */       Date tempdate = null;
/*  87:    */       
/*  88: 83 */       String SQL = "insert into blacklist  (denied_IP, owner_id) values (?,?)";
/*  89: 84 */       prepstat = con.prepareStatement(SQL);
/*  90: 85 */       prepstat.setString(1, ipAddress);
/*  91: 86 */       prepstat.setString(2, owner_id);
/*  92: 87 */       prepstat.execute();
/*  93:    */       
/*  94:    */ 
/*  95: 90 */       prepstat.close();
/*  96: 91 */       prepstat = null;
/*  97: 92 */       con.close();
/*  98: 93 */       con = null;
/*  99:    */     }
/* 100:    */     catch (Exception ex)
/* 101:    */     {
/* 102: 95 */       if (con != null)
/* 103:    */       {
/* 104: 96 */         con.close();
/* 105: 97 */         con = null;
/* 106:    */       }
/* 107: 99 */       throw new Exception(ex.getMessage());
/* 108:    */     }
/* 109:    */     finally
/* 110:    */     {
/* 111:101 */       if (rs != null)
/* 112:    */       {
/* 113:    */         try
/* 114:    */         {
/* 115:103 */           rs.close();
/* 116:    */         }
/* 117:    */         catch (SQLException e) {}
/* 118:107 */         rs = null;
/* 119:    */       }
/* 120:109 */       if (prepstat != null)
/* 121:    */       {
/* 122:    */         try
/* 123:    */         {
/* 124:111 */           prepstat.close();
/* 125:    */         }
/* 126:    */         catch (SQLException e) {}
/* 127:115 */         prepstat = null;
/* 128:    */       }
/* 129:117 */       if (con != null)
/* 130:    */       {
/* 131:    */         try
/* 132:    */         {
/* 133:119 */           con.close();
/* 134:    */         }
/* 135:    */         catch (SQLException e) {}
/* 136:123 */         con = null;
/* 137:    */       }
/* 138:    */     }
/* 139:    */   }
/* 140:    */   
/* 141:    */   public void RemoveFromBlackList(String ipAddress)
/* 142:    */     throws Exception
/* 143:    */   {
/* 144:133 */     ResultSet rs = null;
/* 145:134 */     Connection con = null;
/* 146:135 */     PreparedStatement prepstat = null;
/* 147:136 */     AccessListItem userIP = new AccessListItem();
/* 148:    */     try
/* 149:    */     {
/* 150:139 */       con = DConnect.getConnection();
/* 151:140 */       Date tempdate = null;
/* 152:    */       
/* 153:142 */       String SQL = "delete from blacklist  where denied_IP =?";
/* 154:143 */       prepstat = con.prepareStatement(SQL);
/* 155:144 */       prepstat.setString(1, ipAddress);
/* 156:145 */       prepstat.execute();
/* 157:    */       
/* 158:    */ 
/* 159:148 */       prepstat.close();
/* 160:149 */       prepstat = null;
/* 161:150 */       con.close();
/* 162:151 */       con = null;
/* 163:    */     }
/* 164:    */     catch (Exception ex)
/* 165:    */     {
/* 166:153 */       if (con != null)
/* 167:    */       {
/* 168:154 */         con.close();
/* 169:155 */         con = null;
/* 170:    */       }
/* 171:157 */       throw new Exception(ex.getMessage());
/* 172:    */     }
/* 173:    */     finally
/* 174:    */     {
/* 175:159 */       if (rs != null)
/* 176:    */       {
/* 177:    */         try
/* 178:    */         {
/* 179:161 */           rs.close();
/* 180:    */         }
/* 181:    */         catch (SQLException e) {}
/* 182:165 */         rs = null;
/* 183:    */       }
/* 184:167 */       if (prepstat != null)
/* 185:    */       {
/* 186:    */         try
/* 187:    */         {
/* 188:169 */           prepstat.close();
/* 189:    */         }
/* 190:    */         catch (SQLException e) {}
/* 191:173 */         prepstat = null;
/* 192:    */       }
/* 193:175 */       if (con != null)
/* 194:    */       {
/* 195:    */         try
/* 196:    */         {
/* 197:177 */           con.close();
/* 198:    */         }
/* 199:    */         catch (SQLException e) {}
/* 200:181 */         con = null;
/* 201:    */       }
/* 202:    */     }
/* 203:    */   }
/* 204:    */   
/* 205:    */   public void RemoveFromWhiteList(String ipAddress)
/* 206:    */     throws Exception
/* 207:    */   {
/* 208:190 */     ResultSet rs = null;
/* 209:191 */     Connection con = null;
/* 210:192 */     PreparedStatement prepstat = null;
/* 211:193 */     AccessListItem userIP = new AccessListItem();
/* 212:    */     try
/* 213:    */     {
/* 214:196 */       con = DConnect.getConnection();
/* 215:197 */       Date tempdate = null;
/* 216:    */       
/* 217:199 */       String SQL = "delete from whitelist  where allowed_IP =?";
/* 218:200 */       prepstat = con.prepareStatement(SQL);
/* 219:201 */       prepstat.setString(1, ipAddress);
/* 220:202 */       prepstat.execute();
/* 221:    */       
/* 222:    */ 
/* 223:205 */       prepstat.close();
/* 224:206 */       prepstat = null;
/* 225:207 */       con.close();
/* 226:208 */       con = null;
/* 227:    */     }
/* 228:    */     catch (Exception ex)
/* 229:    */     {
/* 230:210 */       if (con != null)
/* 231:    */       {
/* 232:211 */         con.close();
/* 233:212 */         con = null;
/* 234:    */       }
/* 235:214 */       throw new Exception(ex.getMessage());
/* 236:    */     }
/* 237:    */     finally
/* 238:    */     {
/* 239:216 */       if (rs != null)
/* 240:    */       {
/* 241:    */         try
/* 242:    */         {
/* 243:218 */           rs.close();
/* 244:    */         }
/* 245:    */         catch (SQLException e) {}
/* 246:222 */         rs = null;
/* 247:    */       }
/* 248:224 */       if (prepstat != null)
/* 249:    */       {
/* 250:    */         try
/* 251:    */         {
/* 252:226 */           prepstat.close();
/* 253:    */         }
/* 254:    */         catch (SQLException e) {}
/* 255:230 */         prepstat = null;
/* 256:    */       }
/* 257:232 */       if (con != null)
/* 258:    */       {
/* 259:    */         try
/* 260:    */         {
/* 261:234 */           con.close();
/* 262:    */         }
/* 263:    */         catch (SQLException e) {}
/* 264:238 */         con = null;
/* 265:    */       }
/* 266:    */     }
/* 267:    */   }
/* 268:    */   
/* 269:    */   public AccessListItem viewWhiteListItem(String ipAddress)
/* 270:    */     throws Exception
/* 271:    */   {
/* 272:247 */     ResultSet rs = null;
/* 273:248 */     Connection con = null;
/* 274:249 */     PreparedStatement prepstat = null;
/* 275:    */     
/* 276:251 */     AccessListItem userIP = new AccessListItem();
/* 277:    */     try
/* 278:    */     {
/* 279:254 */       con = DConnect.getConnection();
/* 280:255 */       Date tempdate = null;
/* 281:    */       
/* 282:257 */       String SQL = "select * from whitelist where allowed_IP = ?";
/* 283:258 */       prepstat = con.prepareStatement(SQL);
/* 284:259 */       prepstat.setString(1, ipAddress);
/* 285:260 */       rs = prepstat.executeQuery();
/* 286:261 */       while (rs.next())
/* 287:    */       {
/* 288:263 */         userIP.setIpAddress(rs.getString("allowed_IP"));
/* 289:264 */         userIP.setOwner(rs.getString("owner_id"));
/* 290:    */       }
/* 291:268 */       rs.close();
/* 292:269 */       rs = null;
/* 293:270 */       prepstat.close();
/* 294:271 */       prepstat = null;
/* 295:272 */       con.close();
/* 296:273 */       con = null;
/* 297:    */     }
/* 298:    */     catch (Exception ex)
/* 299:    */     {
/* 300:275 */       if (con != null)
/* 301:    */       {
/* 302:276 */         con.close();
/* 303:277 */         con = null;
/* 304:    */       }
/* 305:279 */       throw new Exception(ex.getMessage());
/* 306:    */     }
/* 307:    */     finally
/* 308:    */     {
/* 309:281 */       if (rs != null)
/* 310:    */       {
/* 311:    */         try
/* 312:    */         {
/* 313:283 */           rs.close();
/* 314:    */         }
/* 315:    */         catch (SQLException e) {}
/* 316:287 */         rs = null;
/* 317:    */       }
/* 318:289 */       if (prepstat != null)
/* 319:    */       {
/* 320:    */         try
/* 321:    */         {
/* 322:291 */           prepstat.close();
/* 323:    */         }
/* 324:    */         catch (SQLException e) {}
/* 325:295 */         prepstat = null;
/* 326:    */       }
/* 327:297 */       if (con != null)
/* 328:    */       {
/* 329:    */         try
/* 330:    */         {
/* 331:299 */           con.close();
/* 332:    */         }
/* 333:    */         catch (SQLException e) {}
/* 334:303 */         con = null;
/* 335:    */       }
/* 336:    */     }
/* 337:307 */     return userIP;
/* 338:    */   }
/* 339:    */   
/* 340:    */   public AccessListItem viewBlackListItem(String ipAddress)
/* 341:    */     throws Exception
/* 342:    */   {
/* 343:312 */     ResultSet rs = null;
/* 344:313 */     Connection con = null;
/* 345:314 */     PreparedStatement prepstat = null;
/* 346:    */     
/* 347:316 */     AccessListItem userIP = new AccessListItem();
/* 348:    */     try
/* 349:    */     {
/* 350:319 */       con = DConnect.getConnection();
/* 351:320 */       Date tempdate = null;
/* 352:    */       
/* 353:322 */       String SQL = "select * from blacklist where denied_IP = ?";
/* 354:323 */       prepstat = con.prepareStatement(SQL);
/* 355:324 */       prepstat.setString(1, ipAddress);
/* 356:325 */       rs = prepstat.executeQuery();
/* 357:326 */       while (rs.next())
/* 358:    */       {
/* 359:328 */         userIP.setIpAddress(rs.getString("denied_IP"));
/* 360:329 */         userIP.setOwner(rs.getString("owner_id"));
/* 361:    */       }
/* 362:333 */       rs.close();
/* 363:334 */       rs = null;
/* 364:335 */       prepstat.close();
/* 365:336 */       prepstat = null;
/* 366:337 */       con.close();
/* 367:338 */       con = null;
/* 368:    */     }
/* 369:    */     catch (Exception ex)
/* 370:    */     {
/* 371:340 */       if (con != null)
/* 372:    */       {
/* 373:341 */         con.close();
/* 374:342 */         con = null;
/* 375:    */       }
/* 376:344 */       throw new Exception(ex.getMessage());
/* 377:    */     }
/* 378:    */     finally
/* 379:    */     {
/* 380:346 */       if (rs != null)
/* 381:    */       {
/* 382:    */         try
/* 383:    */         {
/* 384:348 */           rs.close();
/* 385:    */         }
/* 386:    */         catch (SQLException e) {}
/* 387:352 */         rs = null;
/* 388:    */       }
/* 389:354 */       if (prepstat != null)
/* 390:    */       {
/* 391:    */         try
/* 392:    */         {
/* 393:356 */           prepstat.close();
/* 394:    */         }
/* 395:    */         catch (SQLException e) {}
/* 396:360 */         prepstat = null;
/* 397:    */       }
/* 398:362 */       if (con != null)
/* 399:    */       {
/* 400:    */         try
/* 401:    */         {
/* 402:364 */           con.close();
/* 403:    */         }
/* 404:    */         catch (SQLException e) {}
/* 405:368 */         con = null;
/* 406:    */       }
/* 407:    */     }
/* 408:372 */     return userIP;
/* 409:    */   }
/* 410:    */   
/* 411:    */   public ArrayList viewWhiteList()
/* 412:    */     throws Exception
/* 413:    */   {
/* 414:379 */     ResultSet rs = null;
/* 415:380 */     Connection con = null;
/* 416:381 */     PreparedStatement prepstat = null;
/* 417:382 */     ArrayList whiteList = new ArrayList();
/* 418:    */     try
/* 419:    */     {
/* 420:384 */       con = DConnect.getConnection();
/* 421:385 */       Date tempdate = null;
/* 422:    */       
/* 423:387 */       String SQL = "select * from whitelist";
/* 424:388 */       prepstat = con.prepareStatement(SQL);
/* 425:389 */       rs = prepstat.executeQuery();
/* 426:390 */       while (rs.next())
/* 427:    */       {
/* 428:391 */         AccessListItem userIP = new AccessListItem();
/* 429:392 */         userIP.setIpAddress(rs.getString("allowed_IP"));
/* 430:393 */         userIP.setOwner(rs.getString("owner_id"));
/* 431:394 */         whiteList.add(userIP);
/* 432:    */       }
/* 433:397 */       rs.close();
/* 434:398 */       rs = null;
/* 435:399 */       prepstat.close();
/* 436:400 */       prepstat = null;
/* 437:401 */       con.close();
/* 438:402 */       con = null;
/* 439:    */     }
/* 440:    */     catch (Exception ex)
/* 441:    */     {
/* 442:404 */       if (con != null)
/* 443:    */       {
/* 444:405 */         con.close();
/* 445:406 */         con = null;
/* 446:    */       }
/* 447:408 */       throw new Exception(ex.getMessage());
/* 448:    */     }
/* 449:    */     finally
/* 450:    */     {
/* 451:410 */       if (rs != null)
/* 452:    */       {
/* 453:    */         try
/* 454:    */         {
/* 455:412 */           rs.close();
/* 456:    */         }
/* 457:    */         catch (SQLException e) {}
/* 458:416 */         rs = null;
/* 459:    */       }
/* 460:418 */       if (prepstat != null)
/* 461:    */       {
/* 462:    */         try
/* 463:    */         {
/* 464:420 */           prepstat.close();
/* 465:    */         }
/* 466:    */         catch (SQLException e) {}
/* 467:424 */         prepstat = null;
/* 468:    */       }
/* 469:426 */       if (con != null)
/* 470:    */       {
/* 471:    */         try
/* 472:    */         {
/* 473:428 */           con.close();
/* 474:    */         }
/* 475:    */         catch (SQLException e) {}
/* 476:432 */         con = null;
/* 477:    */       }
/* 478:    */     }
/* 479:436 */     return whiteList;
/* 480:    */   }
/* 481:    */   
/* 482:    */   public ArrayList viewWhiteList(String ownerId)
/* 483:    */     throws Exception
/* 484:    */   {
/* 485:443 */     ResultSet rs = null;
/* 486:444 */     Connection con = null;
/* 487:445 */     PreparedStatement prepstat = null;
/* 488:446 */     ArrayList whiteList = new ArrayList();
/* 489:    */     try
/* 490:    */     {
/* 491:448 */       con = DConnect.getConnection();
/* 492:449 */       Date tempdate = null;
/* 493:    */       
/* 494:451 */       String SQL = "select * from whitelist where owner_id = ?";
/* 495:452 */       prepstat = con.prepareStatement(SQL);
/* 496:453 */       prepstat.setString(1, ownerId);
/* 497:454 */       rs = prepstat.executeQuery();
/* 498:455 */       while (rs.next())
/* 499:    */       {
/* 500:456 */         AccessListItem userIP = new AccessListItem();
/* 501:457 */         userIP.setIpAddress(rs.getString("allowed_IP"));
/* 502:458 */         userIP.setOwner(rs.getString("owner_id"));
/* 503:459 */         whiteList.add(userIP);
/* 504:    */       }
/* 505:462 */       rs.close();
/* 506:463 */       rs = null;
/* 507:464 */       prepstat.close();
/* 508:465 */       prepstat = null;
/* 509:466 */       con.close();
/* 510:467 */       con = null;
/* 511:    */     }
/* 512:    */     catch (Exception ex)
/* 513:    */     {
/* 514:469 */       if (con != null)
/* 515:    */       {
/* 516:470 */         con.close();
/* 517:471 */         con = null;
/* 518:    */       }
/* 519:473 */       throw new Exception(ex.getMessage());
/* 520:    */     }
/* 521:    */     finally
/* 522:    */     {
/* 523:475 */       if (rs != null)
/* 524:    */       {
/* 525:    */         try
/* 526:    */         {
/* 527:477 */           rs.close();
/* 528:    */         }
/* 529:    */         catch (SQLException e) {}
/* 530:481 */         rs = null;
/* 531:    */       }
/* 532:483 */       if (prepstat != null)
/* 533:    */       {
/* 534:    */         try
/* 535:    */         {
/* 536:485 */           prepstat.close();
/* 537:    */         }
/* 538:    */         catch (SQLException e) {}
/* 539:489 */         prepstat = null;
/* 540:    */       }
/* 541:491 */       if (con != null)
/* 542:    */       {
/* 543:    */         try
/* 544:    */         {
/* 545:493 */           con.close();
/* 546:    */         }
/* 547:    */         catch (SQLException e) {}
/* 548:497 */         con = null;
/* 549:    */       }
/* 550:    */     }
/* 551:501 */     return whiteList;
/* 552:    */   }
/* 553:    */   
/* 554:    */   public ArrayList viewBlackList()
/* 555:    */     throws Exception
/* 556:    */   {
/* 557:510 */     ResultSet rs = null;
/* 558:511 */     Connection con = null;
/* 559:512 */     PreparedStatement prepstat = null;
/* 560:513 */     ArrayList whiteList = new ArrayList();
/* 561:    */     try
/* 562:    */     {
/* 563:515 */       con = DConnect.getConnection();
/* 564:516 */       Date tempdate = null;
/* 565:    */       
/* 566:518 */       String SQL = "select * from blacklist";
/* 567:519 */       prepstat = con.prepareStatement(SQL);
/* 568:520 */       rs = prepstat.executeQuery();
/* 569:521 */       while (rs.next())
/* 570:    */       {
/* 571:522 */         AccessListItem userIP = new AccessListItem();
/* 572:523 */         userIP.setIpAddress(rs.getString("denied_IP"));
/* 573:524 */         userIP.setOwner(rs.getString("owner_id"));
/* 574:525 */         whiteList.add(userIP);
/* 575:    */       }
/* 576:528 */       rs.close();
/* 577:529 */       rs = null;
/* 578:530 */       prepstat.close();
/* 579:531 */       prepstat = null;
/* 580:532 */       con.close();
/* 581:533 */       con = null;
/* 582:    */     }
/* 583:    */     catch (Exception ex)
/* 584:    */     {
/* 585:535 */       if (con != null)
/* 586:    */       {
/* 587:536 */         con.close();
/* 588:537 */         con = null;
/* 589:    */       }
/* 590:539 */       throw new Exception(ex.getMessage());
/* 591:    */     }
/* 592:    */     finally
/* 593:    */     {
/* 594:541 */       if (rs != null)
/* 595:    */       {
/* 596:    */         try
/* 597:    */         {
/* 598:543 */           rs.close();
/* 599:    */         }
/* 600:    */         catch (SQLException e) {}
/* 601:547 */         rs = null;
/* 602:    */       }
/* 603:549 */       if (prepstat != null)
/* 604:    */       {
/* 605:    */         try
/* 606:    */         {
/* 607:551 */           prepstat.close();
/* 608:    */         }
/* 609:    */         catch (SQLException e) {}
/* 610:555 */         prepstat = null;
/* 611:    */       }
/* 612:557 */       if (con != null)
/* 613:    */       {
/* 614:    */         try
/* 615:    */         {
/* 616:559 */           con.close();
/* 617:    */         }
/* 618:    */         catch (SQLException e) {}
/* 619:563 */         con = null;
/* 620:    */       }
/* 621:    */     }
/* 622:567 */     return whiteList;
/* 623:    */   }
/* 624:    */   
/* 625:    */   public ArrayList viewBlackList(String ownerId)
/* 626:    */     throws Exception
/* 627:    */   {
/* 628:576 */     ResultSet rs = null;
/* 629:577 */     Connection con = null;
/* 630:578 */     PreparedStatement prepstat = null;
/* 631:579 */     ArrayList whiteList = new ArrayList();
/* 632:    */     try
/* 633:    */     {
/* 634:581 */       con = DConnect.getConnection();
/* 635:582 */       Date tempdate = null;
/* 636:    */       
/* 637:584 */       String SQL = "select * from blacklist where owner_id =?";
/* 638:585 */       prepstat = con.prepareStatement(SQL);
/* 639:586 */       prepstat.setString(1, ownerId);
/* 640:587 */       rs = prepstat.executeQuery();
/* 641:588 */       while (rs.next())
/* 642:    */       {
/* 643:589 */         AccessListItem userIP = new AccessListItem();
/* 644:590 */         userIP.setIpAddress(rs.getString("denied_IP"));
/* 645:591 */         userIP.setOwner(rs.getString("owner_id"));
/* 646:592 */         whiteList.add(userIP);
/* 647:    */       }
/* 648:595 */       rs.close();
/* 649:596 */       rs = null;
/* 650:597 */       prepstat.close();
/* 651:598 */       prepstat = null;
/* 652:599 */       con.close();
/* 653:600 */       con = null;
/* 654:    */     }
/* 655:    */     catch (Exception ex)
/* 656:    */     {
/* 657:602 */       if (con != null)
/* 658:    */       {
/* 659:603 */         con.close();
/* 660:604 */         con = null;
/* 661:    */       }
/* 662:606 */       throw new Exception(ex.getMessage());
/* 663:    */     }
/* 664:    */     finally
/* 665:    */     {
/* 666:608 */       if (rs != null)
/* 667:    */       {
/* 668:    */         try
/* 669:    */         {
/* 670:610 */           rs.close();
/* 671:    */         }
/* 672:    */         catch (SQLException e) {}
/* 673:614 */         rs = null;
/* 674:    */       }
/* 675:616 */       if (prepstat != null)
/* 676:    */       {
/* 677:    */         try
/* 678:    */         {
/* 679:618 */           prepstat.close();
/* 680:    */         }
/* 681:    */         catch (SQLException e) {}
/* 682:622 */         prepstat = null;
/* 683:    */       }
/* 684:624 */       if (con != null)
/* 685:    */       {
/* 686:    */         try
/* 687:    */         {
/* 688:626 */           con.close();
/* 689:    */         }
/* 690:    */         catch (SQLException e) {}
/* 691:630 */         con = null;
/* 692:    */       }
/* 693:    */     }
/* 694:634 */     return whiteList;
/* 695:    */   }
/* 696:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.security.AccessListItemDB
 * JD-Core Version:    0.7.0.1
 */