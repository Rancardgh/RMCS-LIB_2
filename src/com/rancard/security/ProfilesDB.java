/*   1:    */ package com.rancard.security;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import com.rancard.util.Page;
/*   5:    */ import java.io.PrintStream;
/*   6:    */ import java.sql.Connection;
/*   7:    */ import java.sql.PreparedStatement;
/*   8:    */ import java.sql.ResultSet;
/*   9:    */ import java.sql.SQLException;
/*  10:    */ import java.sql.Statement;
/*  11:    */ import java.sql.Timestamp;
/*  12:    */ import java.util.ArrayList;
/*  13:    */ import java.util.List;
/*  14:    */ 
/*  15:    */ public class ProfilesDB
/*  16:    */ {
/*  17:  9 */   boolean val = false;
/*  18:    */   
/*  19:    */   public void createProfiles(String username, String password, String role_ID, String firstname, String lastname, String email, String middlename)
/*  20:    */     throws Exception
/*  21:    */   {
/*  22: 17 */     ResultSet rs = null;
/*  23: 18 */     Connection con = null;
/*  24: 19 */     PreparedStatement prepstat = null;
/*  25:    */     try
/*  26:    */     {
/*  27: 23 */       con = DConnect.getConnection();
/*  28:    */       
/*  29:    */ 
/*  30: 26 */       String SQL = "Insert into profiles(username,password,role_ID,firstname,lastname,email,middlename) values(?,?,?,?,?,?,?)";
/*  31:    */       
/*  32: 28 */       prepstat = con.prepareStatement(SQL);
/*  33: 29 */       prepstat.setString(1, username);
/*  34: 30 */       prepstat.setString(2, password);
/*  35: 31 */       prepstat.setString(3, role_ID);
/*  36: 32 */       prepstat.setString(4, firstname);
/*  37: 33 */       prepstat.setString(5, lastname);
/*  38: 34 */       prepstat.setString(6, email);
/*  39: 35 */       prepstat.setString(7, middlename);
/*  40:    */       
/*  41: 37 */       prepstat.execute();
/*  42:    */       
/*  43:    */ 
/*  44: 40 */       prepstat.close();
/*  45: 41 */       prepstat = null;
/*  46: 42 */       con.close();
/*  47: 43 */       con = null;
/*  48:    */     }
/*  49:    */     catch (Exception ex)
/*  50:    */     {
/*  51: 46 */       if (con != null)
/*  52:    */       {
/*  53: 48 */         con.close();
/*  54: 49 */         con = null;
/*  55:    */       }
/*  56:    */     }
/*  57:    */     finally
/*  58:    */     {
/*  59: 55 */       if (prepstat != null)
/*  60:    */       {
/*  61:    */         try
/*  62:    */         {
/*  63: 56 */           prepstat.close();
/*  64:    */         }
/*  65:    */         catch (SQLException e) {}
/*  66: 57 */         prepstat = null;
/*  67:    */       }
/*  68: 59 */       if (con != null)
/*  69:    */       {
/*  70:    */         try
/*  71:    */         {
/*  72: 60 */           con.close();
/*  73:    */         }
/*  74:    */         catch (SQLException e) {}
/*  75: 61 */         con = null;
/*  76:    */       }
/*  77:    */     }
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void createProfiles(String username, String password, String role_ID, String emp_ID, String firstname, String lastname, String email, String middlename, String mobilePhone)
/*  81:    */     throws Exception
/*  82:    */   {
/*  83: 70 */     ResultSet rs = null;
/*  84: 71 */     Connection con = null;
/*  85: 72 */     PreparedStatement prepstat = null;
/*  86:    */     try
/*  87:    */     {
/*  88: 76 */       con = DConnect.getConnection();
/*  89:    */       
/*  90:    */ 
/*  91: 79 */       String SQL = "Insert into profiles(username,password,role_ID,emp_ID,firstname,lastname,email,middlename,mobilePhone) values(?,?,?,?,?,?,?,?,?)";
/*  92:    */       
/*  93: 81 */       prepstat = con.prepareStatement(SQL);
/*  94: 82 */       prepstat.setString(1, username);
/*  95: 83 */       prepstat.setString(2, password);
/*  96: 84 */       prepstat.setString(3, role_ID);
/*  97: 85 */       prepstat.setString(4, emp_ID);
/*  98:    */       
/*  99: 87 */       prepstat.setString(5, firstname);
/* 100: 88 */       prepstat.setString(6, lastname);
/* 101: 89 */       prepstat.setString(7, email);
/* 102: 90 */       prepstat.setString(8, middlename);
/* 103: 91 */       prepstat.setString(9, mobilePhone);
/* 104:    */       
/* 105: 93 */       prepstat.execute();
/* 106:    */       
/* 107:    */ 
/* 108: 96 */       prepstat.close();
/* 109: 97 */       prepstat = null;
/* 110: 98 */       con.close();
/* 111: 99 */       con = null;
/* 112:    */     }
/* 113:    */     catch (Exception ex)
/* 114:    */     {
/* 115:102 */       if (con != null)
/* 116:    */       {
/* 117:104 */         con.close();
/* 118:105 */         con = null;
/* 119:    */       }
/* 120:    */     }
/* 121:    */     finally
/* 122:    */     {
/* 123:111 */       if (prepstat != null)
/* 124:    */       {
/* 125:    */         try
/* 126:    */         {
/* 127:112 */           prepstat.close();
/* 128:    */         }
/* 129:    */         catch (SQLException e) {}
/* 130:113 */         prepstat = null;
/* 131:    */       }
/* 132:115 */       if (con != null)
/* 133:    */       {
/* 134:    */         try
/* 135:    */         {
/* 136:116 */           con.close();
/* 137:    */         }
/* 138:    */         catch (SQLException e) {}
/* 139:117 */         con = null;
/* 140:    */       }
/* 141:    */     }
/* 142:    */   }
/* 143:    */   
/* 144:    */   public void createProfiles(String username, String password, String role_ID, String emp_ID)
/* 145:    */     throws Exception
/* 146:    */   {
/* 147:128 */     ResultSet rs = null;
/* 148:129 */     Connection con = null;
/* 149:130 */     PreparedStatement prepstat = null;
/* 150:    */     try
/* 151:    */     {
/* 152:134 */       con = DConnect.getConnection();
/* 153:    */       
/* 154:136 */       String SQL = "Insert into Profiles(username,password,role_ID,emp_ID) values(?,?,?,?)";
/* 155:    */       
/* 156:138 */       prepstat = con.prepareStatement(SQL);
/* 157:139 */       prepstat.setString(1, username);
/* 158:140 */       prepstat.setString(2, password);
/* 159:141 */       prepstat.setString(3, role_ID);
/* 160:142 */       prepstat.setString(4, emp_ID);
/* 161:    */       
/* 162:    */ 
/* 163:145 */       prepstat.execute();
/* 164:    */     }
/* 165:    */     catch (Exception ex)
/* 166:    */     {
/* 167:148 */       if (con != null) {
/* 168:149 */         con.close();
/* 169:    */       }
/* 170:150 */       throw new Exception(ex.getMessage());
/* 171:    */     }
/* 172:152 */     if (con != null) {
/* 173:153 */       con.close();
/* 174:    */     }
/* 175:    */   }
/* 176:    */   
/* 177:    */   public void updateProfiles(String username, String password, String role_ID, String emp_ID, java.util.Date last_login)
/* 178:    */     throws Exception
/* 179:    */   {
/* 180:162 */     ResultSet rs = null;
/* 181:163 */     Connection con = null;
/* 182:164 */     PreparedStatement prepstat = null;
/* 183:    */     try
/* 184:    */     {
/* 185:168 */       con = DConnect.getConnection();
/* 186:169 */       java.sql.Date sqlDate_last_login = new java.sql.Date(last_login.getTime());
/* 187:    */       
/* 188:171 */       String SQL = "Update Profiles set username=?,password=?,role_ID=?,last_login=? where emp_ID=?";
/* 189:    */       
/* 190:173 */       prepstat = con.prepareStatement(SQL);
/* 191:174 */       prepstat.setString(1, username);
/* 192:175 */       prepstat.setString(2, password);
/* 193:176 */       prepstat.setString(3, role_ID);
/* 194:177 */       prepstat.setDate(4, sqlDate_last_login);
/* 195:178 */       prepstat.setString(5, emp_ID);
/* 196:    */       
/* 197:180 */       prepstat.execute();
/* 198:    */     }
/* 199:    */     catch (Exception ex)
/* 200:    */     {
/* 201:183 */       if (con != null) {
/* 202:184 */         con.close();
/* 203:    */       }
/* 204:185 */       throw new Exception(ex.getMessage());
/* 205:    */     }
/* 206:187 */     if (con != null) {
/* 207:188 */       con.close();
/* 208:    */     }
/* 209:    */   }
/* 210:    */   
/* 211:    */   public void updateProfiles(String username, String password, String role_ID, String emp_ID)
/* 212:    */     throws Exception
/* 213:    */   {
/* 214:196 */     ResultSet rs = null;
/* 215:197 */     Connection con = null;
/* 216:198 */     PreparedStatement prepstat = null;
/* 217:    */     try
/* 218:    */     {
/* 219:202 */       con = DConnect.getConnection();
/* 220:    */       
/* 221:204 */       String SQL = "Update Profiles set username=?,password=?,role_ID=? where emp_ID=?";
/* 222:    */       
/* 223:206 */       prepstat = con.prepareStatement(SQL);
/* 224:207 */       prepstat.setString(1, username);
/* 225:208 */       prepstat.setString(2, password);
/* 226:209 */       prepstat.setString(3, role_ID);
/* 227:210 */       prepstat.setString(4, emp_ID);
/* 228:    */       
/* 229:212 */       prepstat.execute();
/* 230:    */     }
/* 231:    */     catch (Exception ex)
/* 232:    */     {
/* 233:215 */       if (con != null) {
/* 234:216 */         con.close();
/* 235:    */       }
/* 236:217 */       throw new Exception(ex.getMessage());
/* 237:    */     }
/* 238:219 */     if (con != null) {
/* 239:220 */       con.close();
/* 240:    */     }
/* 241:    */   }
/* 242:    */   
/* 243:    */   public void updateAllProfiles(String username, String password, String role_ID, String emp_ID, String fname, String lname, String email, String mname, String mphone)
/* 244:    */     throws Exception
/* 245:    */   {
/* 246:232 */     ResultSet rs = null;
/* 247:233 */     Connection con = null;
/* 248:234 */     PreparedStatement prepstat = null;
/* 249:    */     try
/* 250:    */     {
/* 251:238 */       con = DConnect.getConnection();
/* 252:    */       
/* 253:    */ 
/* 254:241 */       String SQL = "Update Profiles set username=?,password=?,role_ID=?,emp_ID=?, firstname=?,lastname=?,email=?,middlename=?,mobilePhone=? where emp_ID=?";
/* 255:    */       
/* 256:243 */       prepstat = con.prepareStatement(SQL);
/* 257:244 */       prepstat.setString(1, username);
/* 258:245 */       prepstat.setString(2, password);
/* 259:246 */       prepstat.setString(3, role_ID);
/* 260:247 */       prepstat.setString(4, emp_ID);
/* 261:    */       
/* 262:249 */       prepstat.setString(5, fname);
/* 263:250 */       prepstat.setString(6, lname);
/* 264:251 */       prepstat.setString(7, email);
/* 265:252 */       prepstat.setString(8, mname);
/* 266:253 */       prepstat.setString(9, mphone);
/* 267:254 */       prepstat.setString(10, emp_ID);
/* 268:255 */       prepstat.execute();
/* 269:    */     }
/* 270:    */     catch (Exception ex)
/* 271:    */     {
/* 272:258 */       if (con != null) {
/* 273:259 */         con.close();
/* 274:    */       }
/* 275:260 */       throw new Exception(ex.getMessage());
/* 276:    */     }
/* 277:262 */     if (con != null) {
/* 278:263 */       con.close();
/* 279:    */     }
/* 280:    */   }
/* 281:    */   
/* 282:    */   public void updateUserProfiles(String username, String password, String role_ID)
/* 283:    */     throws Exception
/* 284:    */   {
/* 285:278 */     ResultSet rs = null;
/* 286:279 */     Connection con = null;
/* 287:280 */     PreparedStatement prepstat = null;
/* 288:    */     try
/* 289:    */     {
/* 290:284 */       con = DConnect.getConnection();
/* 291:285 */       String SQL = "Update Profiles set password=?,role_ID=? where username=?";
/* 292:286 */       prepstat = con.prepareStatement(SQL);
/* 293:287 */       prepstat.setString(1, password);
/* 294:288 */       prepstat.setString(2, role_ID);
/* 295:289 */       prepstat.setString(3, username);
/* 296:290 */       prepstat.execute();
/* 297:    */     }
/* 298:    */     catch (Exception ex)
/* 299:    */     {
/* 300:293 */       if (con != null) {
/* 301:294 */         con.close();
/* 302:    */       }
/* 303:295 */       throw new Exception(ex.getMessage());
/* 304:    */     }
/* 305:297 */     if (con != null) {
/* 306:298 */       con.close();
/* 307:    */     }
/* 308:    */   }
/* 309:    */   
/* 310:    */   public void updateLastLogin(String emp_ID, java.util.Date last_login)
/* 311:    */     throws Exception
/* 312:    */   {
/* 313:303 */     ResultSet rs = null;
/* 314:304 */     Connection con = null;
/* 315:305 */     PreparedStatement prepstat = null;
/* 316:    */     try
/* 317:    */     {
/* 318:309 */       con = DConnect.getConnection();
/* 319:310 */       java.sql.Date sqlDate_last_login = new java.sql.Date(last_login.getTime());
/* 320:    */       
/* 321:312 */       String SQL = "Update Profiles set last_login=? where emp_ID=?";
/* 322:    */       
/* 323:314 */       prepstat = con.prepareStatement(SQL);
/* 324:315 */       prepstat.setDate(1, sqlDate_last_login);
/* 325:316 */       prepstat.setString(2, emp_ID);
/* 326:    */       
/* 327:318 */       prepstat.execute();
/* 328:    */     }
/* 329:    */     catch (Exception ex)
/* 330:    */     {
/* 331:321 */       if (con != null) {
/* 332:322 */         con.close();
/* 333:    */       }
/* 334:323 */       throw new Exception(ex.getMessage());
/* 335:    */     }
/* 336:325 */     if (con != null) {
/* 337:326 */       con.close();
/* 338:    */     }
/* 339:    */   }
/* 340:    */   
/* 341:    */   public void deleteProfiles()
/* 342:    */     throws Exception
/* 343:    */   {
/* 344:333 */     ResultSet rs = null;
/* 345:334 */     Connection con = null;
/* 346:335 */     PreparedStatement prepstat = null;
/* 347:    */     try
/* 348:    */     {
/* 349:337 */       con = DConnect.getConnection();
/* 350:338 */       String SQL = "delete from Profiles";
/* 351:339 */       prepstat = con.prepareStatement(SQL);
/* 352:340 */       prepstat.execute();
/* 353:    */     }
/* 354:    */     catch (Exception ex)
/* 355:    */     {
/* 356:342 */       if (con != null) {
/* 357:343 */         con.close();
/* 358:    */       }
/* 359:344 */       throw new Exception(ex.getMessage());
/* 360:    */     }
/* 361:346 */     if (con != null) {
/* 362:347 */       con.close();
/* 363:    */     }
/* 364:    */   }
/* 365:    */   
/* 366:    */   public void deleteUser(String username)
/* 367:    */     throws Exception
/* 368:    */   {
/* 369:362 */     Connection con = null;
/* 370:    */     try
/* 371:    */     {
/* 372:365 */       con = DConnect.getConnection();
/* 373:    */       
/* 374:367 */       PreparedStatement st = con.prepareStatement("Delete from profiles  where username =?");
/* 375:368 */       st.setString(1, username);
/* 376:    */       
/* 377:370 */       st.executeUpdate();
/* 378:    */     }
/* 379:    */     catch (Exception ex)
/* 380:    */     {
/* 381:373 */       ex.printStackTrace();
/* 382:    */     }
/* 383:    */   }
/* 384:    */   
/* 385:    */   public ProfilesBean viewProfiles(String username)
/* 386:    */     throws Exception
/* 387:    */   {
/* 388:384 */     ProfilesBean Profiles = new ProfilesBean();
/* 389:    */     
/* 390:    */ 
/* 391:387 */     ResultSet rs = null;
/* 392:388 */     Connection con = null;
/* 393:389 */     PreparedStatement prepstat = null;
/* 394:    */     try
/* 395:    */     {
/* 396:393 */       con = DConnect.getConnection();
/* 397:394 */       java.sql.Date tempdate = null;
/* 398:    */       
/* 399:396 */       String SQL = "Select p.* from  profiles p where  p.username=?";
/* 400:    */       
/* 401:    */ 
/* 402:    */ 
/* 403:    */ 
/* 404:401 */       prepstat = con.prepareStatement(SQL);
/* 405:402 */       prepstat.setString(1, username);
/* 406:403 */       rs = prepstat.executeQuery();
/* 407:404 */       while (rs.next())
/* 408:    */       {
/* 409:406 */         Profiles.setusername(rs.getString("username"));
/* 410:407 */         Profiles.setpassword(rs.getString("password"));
/* 411:408 */         Profiles.setrole_id(rs.getString("role_ID"));
/* 412:409 */         Profiles.setemp_id(rs.getString("emp_ID"));
/* 413:410 */         Profiles.setfirstname(rs.getString("firstname"));
/* 414:411 */         Profiles.setlastname(rs.getString("lastname"));
/* 415:412 */         Profiles.setMiddlename(rs.getString("middlename"));
/* 416:413 */         Profiles.setEmail(rs.getString("email"));
/* 417:414 */         Profiles.setMobile_phone(rs.getString("phone_no"));
/* 418:415 */         tempdate = new java.sql.Date(rs.getTimestamp("last_login").getTime());
/* 419:    */         
/* 420:417 */         Profiles.setlast_login(tempdate.toString());
/* 421:    */       }
/* 422:421 */       rs.close();
/* 423:422 */       rs = null;
/* 424:423 */       prepstat.close();
/* 425:424 */       prepstat = null;
/* 426:425 */       con.close();
/* 427:426 */       con = null;
/* 428:    */     }
/* 429:    */     catch (Exception ex)
/* 430:    */     {
/* 431:429 */       if (con != null)
/* 432:    */       {
/* 433:431 */         con.close();
/* 434:432 */         con = null;
/* 435:    */       }
/* 436:434 */       throw new Exception(ex.getMessage());
/* 437:    */     }
/* 438:    */     finally
/* 439:    */     {
/* 440:437 */       if (rs != null)
/* 441:    */       {
/* 442:    */         try
/* 443:    */         {
/* 444:438 */           rs.close();
/* 445:    */         }
/* 446:    */         catch (SQLException e) {}
/* 447:439 */         rs = null;
/* 448:    */       }
/* 449:441 */       if (prepstat != null)
/* 450:    */       {
/* 451:    */         try
/* 452:    */         {
/* 453:442 */           prepstat.close();
/* 454:    */         }
/* 455:    */         catch (SQLException e) {}
/* 456:443 */         prepstat = null;
/* 457:    */       }
/* 458:445 */       if (con != null)
/* 459:    */       {
/* 460:    */         try
/* 461:    */         {
/* 462:446 */           con.close();
/* 463:    */         }
/* 464:    */         catch (SQLException e) {}
/* 465:447 */         con = null;
/* 466:    */       }
/* 467:    */     }
/* 468:451 */     return Profiles;
/* 469:    */   }
/* 470:    */   
/* 471:    */   public Page viewAllUserProfiles(int start, int count, String sort_type)
/* 472:    */     throws Exception
/* 473:    */   {
/* 474:535 */     ResultSet rs1 = null;
/* 475:536 */     ResultSet rs = null;
/* 476:537 */     Connection con = null;
/* 477:538 */     PreparedStatement prepstat = null;
/* 478:539 */     boolean hasNext = false;
/* 479:540 */     ProfilesBean Profiles = null;
/* 480:541 */     List profiles_List = new ArrayList();
/* 481:542 */     Page ret = null;
/* 482:543 */     int y = 0;
/* 483:    */     try
/* 484:    */     {
/* 485:548 */       con = DConnect.getConnection();
/* 486:549 */       java.sql.Date tempdate = null;
/* 487:550 */       String SQL1 = "SELECT     COUNT(*) AS Total FROM         profiles ";
/* 488:    */       
/* 489:552 */       String SQL = "Select  p.*   from  profiles p order by " + sort_type;
/* 490:    */       
/* 491:    */ 
/* 492:    */ 
/* 493:    */ 
/* 494:    */ 
/* 495:558 */       prepstat = con.prepareStatement(SQL1);
/* 496:    */       
/* 497:560 */       rs1 = prepstat.executeQuery();
/* 498:561 */       int i = 0;
/* 499:562 */       int numResults = 0;
/* 500:565 */       while (rs1.next()) {
/* 501:566 */         numResults = rs1.getInt(1);
/* 502:    */       }
/* 503:570 */       rs1 = null;
/* 504:571 */       prepstat = con.prepareStatement(SQL);
/* 505:    */       
/* 506:573 */       rs = prepstat.executeQuery();
/* 507:574 */       while ((i < start + count) && (rs.next()))
/* 508:    */       {
/* 509:576 */         if (i == 0)
/* 510:    */         {
/* 511:578 */           int x = numResults;
/* 512:579 */           y = x / count;
/* 513:580 */           if (x % count > 0) {
/* 514:582 */             y++;
/* 515:    */           }
/* 516:    */         }
/* 517:585 */         if (i >= start)
/* 518:    */         {
/* 519:587 */           Profiles = new ProfilesBean();
/* 520:588 */           Profiles.setusername(rs.getString("username"));
/* 521:589 */           Profiles.setpassword(rs.getString("password"));
/* 522:590 */           Profiles.setrole_id(rs.getString("role_ID"));
/* 523:    */           
/* 524:592 */           Profiles.setfirstname(rs.getString("firstname"));
/* 525:593 */           Profiles.setlastname(rs.getString("lastname"));
/* 526:594 */           Profiles.setMiddlename(rs.getString("middlename"));
/* 527:595 */           Profiles.setEmail(rs.getString("email"));
/* 528:    */           
/* 529:    */ 
/* 530:    */ 
/* 531:    */ 
/* 532:    */ 
/* 533:    */ 
/* 534:    */ 
/* 535:    */ 
/* 536:604 */           profiles_List.add(Profiles);
/* 537:    */         }
/* 538:606 */         i++;
/* 539:    */       }
/* 540:609 */       hasNext = rs.next();
/* 541:610 */       System.out.println(profiles_List.size());
/* 542:611 */       System.out.println(start);
/* 543:612 */       ret = new Page(profiles_List, start, hasNext, y, numResults);
/* 544:614 */       if (ret == null) {
/* 545:615 */         ret = Page.EMPTY_PAGE;
/* 546:    */       }
/* 547:    */     }
/* 548:    */     catch (Exception ex)
/* 549:    */     {
/* 550:619 */       if (con != null)
/* 551:    */       {
/* 552:621 */         con.close();
/* 553:622 */         con = null;
/* 554:    */       }
/* 555:624 */       throw new Exception(ex.getMessage());
/* 556:    */     }
/* 557:    */     finally
/* 558:    */     {
/* 559:627 */       if (rs != null)
/* 560:    */       {
/* 561:    */         try
/* 562:    */         {
/* 563:628 */           rs.close();
/* 564:    */         }
/* 565:    */         catch (SQLException e) {}
/* 566:629 */         rs = null;
/* 567:    */       }
/* 568:631 */       if (prepstat != null)
/* 569:    */       {
/* 570:    */         try
/* 571:    */         {
/* 572:632 */           prepstat.close();
/* 573:    */         }
/* 574:    */         catch (SQLException e) {}
/* 575:633 */         prepstat = null;
/* 576:    */       }
/* 577:635 */       if (con != null)
/* 578:    */       {
/* 579:    */         try
/* 580:    */         {
/* 581:636 */           con.close();
/* 582:    */         }
/* 583:    */         catch (SQLException e) {}
/* 584:637 */         con = null;
/* 585:    */       }
/* 586:    */     }
/* 587:641 */     return ret;
/* 588:    */   }
/* 589:    */   
/* 590:    */   public Page viewAllUserProfiles(String keyword, int start, int count)
/* 591:    */     throws Exception
/* 592:    */   {
/* 593:649 */     ResultSet rs1 = null;
/* 594:650 */     ResultSet rs = null;
/* 595:651 */     Connection con = null;
/* 596:652 */     PreparedStatement prepstat = null;
/* 597:653 */     boolean hasNext = false;
/* 598:654 */     ProfilesBean Profiles = null;
/* 599:655 */     List profiles_List = new ArrayList();
/* 600:656 */     Page ret = null;
/* 601:657 */     int y = 0;
/* 602:    */     try
/* 603:    */     {
/* 604:662 */       con = DConnect.getConnection();
/* 605:663 */       java.sql.Date tempdate = null;
/* 606:664 */       String SQL1 = "SELECT     COUNT(*) AS Total FROM      Profiles P, Employees E where P.emp_ID = E.emp_ID AND ( lastname LIKE '%" + keyword + "%' or firstname Like '%" + keyword + "%')";
/* 607:    */       
/* 608:666 */       String SQL = "Select  p.*, e.lastname, e.firstname, e.middlename,e.email  , e.HomeTelephone from  Profiles P, Employees E where P.emp_ID = E.emp_ID AND ( lastname LIKE '%" + keyword + "%' or firstname Like '%" + keyword + "%')";
/* 609:    */       
/* 610:    */ 
/* 611:    */ 
/* 612:    */ 
/* 613:671 */       prepstat = con.prepareStatement(SQL1);
/* 614:    */       
/* 615:673 */       rs1 = prepstat.executeQuery();
/* 616:674 */       int i = 0;
/* 617:675 */       int numResults = 0;
/* 618:678 */       while (rs1.next()) {
/* 619:679 */         numResults = rs1.getInt(1);
/* 620:    */       }
/* 621:683 */       rs1 = null;
/* 622:684 */       prepstat = con.prepareStatement(SQL);
/* 623:    */       
/* 624:686 */       rs = prepstat.executeQuery();
/* 625:687 */       while ((i < start + count) && (rs.next()))
/* 626:    */       {
/* 627:688 */         if (i == 0)
/* 628:    */         {
/* 629:689 */           int x = numResults;
/* 630:690 */           y = x / count;
/* 631:691 */           if (x % count > 0) {
/* 632:692 */             y++;
/* 633:    */           }
/* 634:    */         }
/* 635:694 */         if (i >= start)
/* 636:    */         {
/* 637:695 */           Profiles = new ProfilesBean();
/* 638:696 */           Profiles.setusername(rs.getString("username"));
/* 639:697 */           Profiles.setpassword(rs.getString("password"));
/* 640:698 */           Profiles.setrole_id(rs.getString("role_ID"));
/* 641:699 */           Profiles.setemp_id(rs.getString("emp_ID"));
/* 642:700 */           Profiles.setfirstname(rs.getString("firstname"));
/* 643:701 */           Profiles.setlastname(rs.getString("lastname"));
/* 644:702 */           Profiles.setMiddlename(rs.getString("middlename"));
/* 645:703 */           Profiles.setEmail(rs.getString("email"));
/* 646:704 */           Profiles.setMobile_phone(rs.getString("HomeTelephone"));
/* 647:705 */           tempdate = rs.getDate("last_login");
/* 648:706 */           if (tempdate == null) {
/* 649:707 */             Profiles.setlast_login(null);
/* 650:    */           } else {
/* 651:709 */             Profiles.setlast_login(tempdate.toString());
/* 652:    */           }
/* 653:711 */           profiles_List.add(Profiles);
/* 654:    */         }
/* 655:713 */         i++;
/* 656:    */       }
/* 657:716 */       hasNext = rs.next();
/* 658:717 */       ret = new Page(profiles_List, start, hasNext, y);
/* 659:719 */       if (ret == null) {
/* 660:720 */         ret = Page.EMPTY_PAGE;
/* 661:    */       }
/* 662:    */     }
/* 663:    */     catch (Exception ex)
/* 664:    */     {
/* 665:724 */       if (con != null)
/* 666:    */       {
/* 667:726 */         con.close();
/* 668:727 */         con = null;
/* 669:    */       }
/* 670:729 */       throw new Exception(ex.getMessage());
/* 671:    */     }
/* 672:    */     finally
/* 673:    */     {
/* 674:732 */       if (rs != null)
/* 675:    */       {
/* 676:    */         try
/* 677:    */         {
/* 678:733 */           rs.close();
/* 679:    */         }
/* 680:    */         catch (SQLException e) {}
/* 681:734 */         rs = null;
/* 682:    */       }
/* 683:736 */       if (prepstat != null)
/* 684:    */       {
/* 685:    */         try
/* 686:    */         {
/* 687:737 */           prepstat.close();
/* 688:    */         }
/* 689:    */         catch (SQLException e) {}
/* 690:738 */         prepstat = null;
/* 691:    */       }
/* 692:740 */       if (con != null)
/* 693:    */       {
/* 694:    */         try
/* 695:    */         {
/* 696:741 */           con.close();
/* 697:    */         }
/* 698:    */         catch (SQLException e) {}
/* 699:742 */         con = null;
/* 700:    */       }
/* 701:    */     }
/* 702:746 */     return ret;
/* 703:    */   }
/* 704:    */   
/* 705:    */   public ProfilesBean searchProfiles(String keyword)
/* 706:    */     throws Exception
/* 707:    */   {
/* 708:750 */     ProfilesBean Profiles = new ProfilesBean();
/* 709:    */     
/* 710:    */ 
/* 711:753 */     ResultSet rs = null;
/* 712:754 */     Connection con = null;
/* 713:755 */     PreparedStatement prepstat = null;
/* 714:    */     try
/* 715:    */     {
/* 716:759 */       con = DConnect.getConnection();
/* 717:    */       
/* 718:    */ 
/* 719:762 */       String SQL = "Select * from  profiles  where   firstname LIKE '%" + keyword + "%' or lastname Like '%" + keyword + "%' or username Like '%" + keyword + "%' or emp_ID Like '%" + keyword + "%'";
/* 720:    */       
/* 721:    */ 
/* 722:    */ 
/* 723:766 */       prepstat = con.prepareStatement(SQL);
/* 724:    */       
/* 725:768 */       rs = prepstat.executeQuery();
/* 726:769 */       while (rs.next())
/* 727:    */       {
/* 728:771 */         Profiles.setusername(rs.getString("username"));
/* 729:772 */         Profiles.setpassword(rs.getString("password"));
/* 730:773 */         Profiles.setrole_id(rs.getString("role_ID"));
/* 731:774 */         Profiles.setemp_id(rs.getString("emp_ID"));
/* 732:775 */         Profiles.setfirstname(rs.getString("firstname"));
/* 733:776 */         Profiles.setlastname(rs.getString("lastname"));
/* 734:777 */         Profiles.setEmail(rs.getString("email"));
/* 735:778 */         Profiles.setMobile_phone(rs.getString("mobilePhone"));
/* 736:    */       }
/* 737:788 */       rs.close();
/* 738:789 */       rs = null;
/* 739:790 */       prepstat.close();
/* 740:791 */       prepstat = null;
/* 741:792 */       con.close();
/* 742:793 */       con = null;
/* 743:    */     }
/* 744:    */     catch (Exception ex)
/* 745:    */     {
/* 746:796 */       if (con != null)
/* 747:    */       {
/* 748:798 */         con.close();
/* 749:799 */         con = null;
/* 750:    */       }
/* 751:801 */       throw new Exception(ex.getMessage());
/* 752:    */     }
/* 753:    */     finally
/* 754:    */     {
/* 755:804 */       if (rs != null)
/* 756:    */       {
/* 757:    */         try
/* 758:    */         {
/* 759:805 */           rs.close();
/* 760:    */         }
/* 761:    */         catch (SQLException e) {}
/* 762:806 */         rs = null;
/* 763:    */       }
/* 764:808 */       if (prepstat != null)
/* 765:    */       {
/* 766:    */         try
/* 767:    */         {
/* 768:809 */           prepstat.close();
/* 769:    */         }
/* 770:    */         catch (SQLException e) {}
/* 771:810 */         prepstat = null;
/* 772:    */       }
/* 773:812 */       if (con != null)
/* 774:    */       {
/* 775:    */         try
/* 776:    */         {
/* 777:813 */           con.close();
/* 778:    */         }
/* 779:    */         catch (SQLException e) {}
/* 780:814 */         con = null;
/* 781:    */       }
/* 782:    */     }
/* 783:818 */     return Profiles;
/* 784:    */   }
/* 785:    */   
/* 786:    */   public ProfilesBean verifyUser(String username)
/* 787:    */     throws Exception
/* 788:    */   {
/* 789:822 */     ProfilesBean Profiles = new ProfilesBean();
/* 790:    */     
/* 791:824 */     ResultSet rs = null;
/* 792:825 */     Connection con = null;
/* 793:    */     try
/* 794:    */     {
/* 795:830 */       con = DConnect.getConnection();
/* 796:831 */       Statement stmt = con.createStatement();
/* 797:832 */       String SQL = "SELECT username, password, role_ID FROM profiles WHERE username='" + username + "'";
/* 798:    */       
/* 799:    */ 
/* 800:    */ 
/* 801:    */ 
/* 802:    */ 
/* 803:838 */       rs = stmt.executeQuery(SQL);
/* 804:839 */       while (rs.next())
/* 805:    */       {
/* 806:841 */         Profiles.setusername(rs.getString("username"));
/* 807:842 */         Profiles.setpassword(rs.getString("password"));
/* 808:843 */         Profiles.setrole_id(rs.getString("role_ID"));
/* 809:    */       }
/* 810:847 */       rs.close();
/* 811:848 */       rs = null;
/* 812:    */       
/* 813:    */ 
/* 814:851 */       con.close();
/* 815:852 */       con = null;
/* 816:    */     }
/* 817:    */     catch (Exception ex)
/* 818:    */     {
/* 819:856 */       ex.printStackTrace();
/* 820:    */     }
/* 821:858 */     return Profiles;
/* 822:    */   }
/* 823:    */   
/* 824:    */   public ProfilesBean verifyUser(String username, String password)
/* 825:    */     throws Exception
/* 826:    */   {
/* 827:864 */     ProfilesBean Profiles = new ProfilesBean();
/* 828:    */     
/* 829:866 */     ResultSet rs = null;
/* 830:867 */     Connection con = null;
/* 831:    */     try
/* 832:    */     {
/* 833:872 */       con = DConnect.getConnection();
/* 834:    */       
/* 835:874 */       String SQL = "SELECT username, password, role_ID FROM profiles WHERE username=? and passowrd=?";
/* 836:    */       
/* 837:876 */       PreparedStatement stmt = con.prepareStatement(SQL);
/* 838:877 */       stmt.setString(1, username);
/* 839:878 */       stmt.setString(2, password);
/* 840:879 */       rs = stmt.executeQuery();
/* 841:880 */       while (rs.next())
/* 842:    */       {
/* 843:882 */         Profiles.setusername(rs.getString("username"));
/* 844:883 */         Profiles.setpassword(rs.getString("password"));
/* 845:884 */         Profiles.setrole_id(rs.getString("role_ID"));
/* 846:    */       }
/* 847:888 */       rs.close();
/* 848:889 */       rs = null;
/* 849:    */       
/* 850:    */ 
/* 851:892 */       con.close();
/* 852:893 */       con = null;
/* 853:    */     }
/* 854:    */     catch (Exception ex)
/* 855:    */     {
/* 856:897 */       throw new Exception(ex.getMessage());
/* 857:    */     }
/* 858:899 */     return Profiles;
/* 859:    */   }
/* 860:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.security.ProfilesDB
 * JD-Core Version:    0.7.0.1
 */