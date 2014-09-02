/*    1:     */ package com.rancard.mobility.contentprovider;
/*    2:     */ 
/*    3:     */ import com.rancard.common.DConnect;
/*    4:     */ import com.rancard.mobility.contentserver.ContentType;
/*    5:     */ import java.io.PrintStream;
/*    6:     */ import java.sql.Connection;
/*    7:     */ import java.sql.PreparedStatement;
/*    8:     */ import java.sql.ResultSet;
/*    9:     */ import java.sql.SQLException;
/*   10:     */ import java.util.ArrayList;
/*   11:     */ import java.util.Date;
/*   12:     */ 
/*   13:     */ public class UserDB
/*   14:     */ {
/*   15:     */   public void createDealer(String name, String id, double accountBalance, String username, String password, double bandwidthBalance, double inboxBalance, double outboxBalance)
/*   16:     */     throws Exception
/*   17:     */   {
/*   18:  15 */     ResultSet rs = null;
/*   19:  16 */     Connection con = null;
/*   20:  17 */     PreparedStatement prepstat = null;
/*   21:     */     try
/*   22:     */     {
/*   23:  20 */       con = DConnect.getConnection("rmcs");
/*   24:  21 */       String SQL = "select * from cp_user where username=?";
/*   25:  22 */       prepstat = con.prepareStatement(SQL);
/*   26:  23 */       prepstat.setString(1, username);
/*   27:  24 */       rs = prepstat.executeQuery();
/*   28:  25 */       if (rs.next()) {
/*   29:  27 */         throw new Exception("This username already exists .please use a different username");
/*   30:     */       }
/*   31:  32 */       SQL = "Insert into cp_user(accountBalance,id,name,username,password,bandwidth_balance,inbox_balance,outbox_balance) values(?,?,?,?,?,?,?,?)";
/*   32:     */       
/*   33:     */ 
/*   34:  35 */       prepstat = con.prepareStatement(SQL);
/*   35:  36 */       prepstat.setDouble(1, accountBalance);
/*   36:  37 */       prepstat.setString(2, id);
/*   37:  38 */       prepstat.setString(3, name);
/*   38:  39 */       prepstat.setString(4, username);
/*   39:  40 */       prepstat.setString(5, password);
/*   40:  41 */       prepstat.setDouble(6, bandwidthBalance);
/*   41:  42 */       prepstat.setDouble(7, inboxBalance);
/*   42:  43 */       prepstat.setDouble(8, outboxBalance);
/*   43:     */       
/*   44:  45 */       prepstat.execute();
/*   45:     */     }
/*   46:     */     catch (Exception ex)
/*   47:     */     {
/*   48:  49 */       if (con != null)
/*   49:     */       {
/*   50:     */         try
/*   51:     */         {
/*   52:  51 */           con.close();
/*   53:     */         }
/*   54:     */         catch (SQLException ex1) {}
/*   55:  54 */         con = null;
/*   56:     */       }
/*   57:     */     }
/*   58:     */     finally
/*   59:     */     {
/*   60:  58 */       if (rs != null)
/*   61:     */       {
/*   62:     */         try
/*   63:     */         {
/*   64:  60 */           rs.close();
/*   65:     */         }
/*   66:     */         catch (SQLException e)
/*   67:     */         {
/*   68:  62 */           System.out.println(e.getMessage());
/*   69:     */         }
/*   70:  64 */         rs = null;
/*   71:     */       }
/*   72:  66 */       if (prepstat != null)
/*   73:     */       {
/*   74:     */         try
/*   75:     */         {
/*   76:  68 */           prepstat.close();
/*   77:     */         }
/*   78:     */         catch (SQLException e)
/*   79:     */         {
/*   80:  70 */           System.out.println(e.getMessage());
/*   81:     */         }
/*   82:  72 */         prepstat = null;
/*   83:     */       }
/*   84:  74 */       if (con != null)
/*   85:     */       {
/*   86:     */         try
/*   87:     */         {
/*   88:  76 */           con.close();
/*   89:     */         }
/*   90:     */         catch (SQLException e)
/*   91:     */         {
/*   92:  78 */           System.out.println(e.getMessage());
/*   93:     */         }
/*   94:  80 */         con = null;
/*   95:     */       }
/*   96:     */     }
/*   97:     */   }
/*   98:     */   
/*   99:     */   public void updateDealer(String name, String id, double accountBalance, String username, String password)
/*  100:     */     throws Exception
/*  101:     */   {
/*  102:  89 */     ResultSet rs = null;
/*  103:  90 */     Connection con = null;
/*  104:  91 */     PreparedStatement prepstat = null;
/*  105:     */     try
/*  106:     */     {
/*  107:  94 */       con = DConnect.getConnection("rmcs");
/*  108:  95 */       String SQL = "Update cp_user set accountBalance=?,id=?,name=?,username=?,password=? where username='" + username + "'";
/*  109:     */       
/*  110:     */ 
/*  111:  98 */       prepstat = con.prepareStatement(SQL);
/*  112:  99 */       prepstat.setDouble(1, accountBalance);
/*  113: 100 */       prepstat.setString(2, id);
/*  114: 101 */       prepstat.setString(3, name);
/*  115: 102 */       prepstat.setString(4, username);
/*  116: 103 */       prepstat.setString(5, password);
/*  117:     */       
/*  118: 105 */       prepstat.execute();
/*  119:     */     }
/*  120:     */     catch (Exception ex)
/*  121:     */     {
/*  122: 107 */       if (con != null)
/*  123:     */       {
/*  124:     */         try
/*  125:     */         {
/*  126: 109 */           con.close();
/*  127:     */         }
/*  128:     */         catch (SQLException ex1) {}
/*  129: 112 */         con = null;
/*  130:     */       }
/*  131:     */     }
/*  132:     */     finally
/*  133:     */     {
/*  134: 116 */       if (rs != null)
/*  135:     */       {
/*  136:     */         try
/*  137:     */         {
/*  138: 118 */           rs.close();
/*  139:     */         }
/*  140:     */         catch (SQLException e)
/*  141:     */         {
/*  142: 120 */           System.out.println(e.getMessage());
/*  143:     */         }
/*  144: 122 */         rs = null;
/*  145:     */       }
/*  146: 124 */       if (prepstat != null)
/*  147:     */       {
/*  148:     */         try
/*  149:     */         {
/*  150: 126 */           prepstat.close();
/*  151:     */         }
/*  152:     */         catch (SQLException e)
/*  153:     */         {
/*  154: 128 */           System.out.println(e.getMessage());
/*  155:     */         }
/*  156: 130 */         prepstat = null;
/*  157:     */       }
/*  158: 132 */       if (con != null)
/*  159:     */       {
/*  160:     */         try
/*  161:     */         {
/*  162: 134 */           con.close();
/*  163:     */         }
/*  164:     */         catch (SQLException e)
/*  165:     */         {
/*  166: 136 */           System.out.println(e.getMessage());
/*  167:     */         }
/*  168: 138 */         con = null;
/*  169:     */       }
/*  170:     */     }
/*  171:     */   }
/*  172:     */   
/*  173:     */   public void updateDealer(String username, String name, String password, String defaultSmsc, String defaultService)
/*  174:     */     throws Exception
/*  175:     */   {
/*  176: 148 */     ResultSet rs = null;
/*  177: 149 */     Connection con = null;
/*  178: 150 */     PreparedStatement prepstat = null;
/*  179:     */     try
/*  180:     */     {
/*  181: 153 */       con = DConnect.getConnection("rmcs");
/*  182: 154 */       String SQL = "Update cp_user set name=?,password=?, default_service=?,default_smsc=? where username=?";
/*  183:     */       
/*  184: 156 */       prepstat = con.prepareStatement(SQL);
/*  185: 157 */       prepstat.setString(1, name);
/*  186: 158 */       prepstat.setString(2, password);
/*  187: 159 */       prepstat.setString(3, defaultService);
/*  188: 160 */       prepstat.setString(4, defaultSmsc);
/*  189: 161 */       prepstat.setString(5, username);
/*  190:     */       
/*  191: 163 */       prepstat.execute();
/*  192:     */     }
/*  193:     */     catch (Exception ex)
/*  194:     */     {
/*  195: 165 */       if (con != null)
/*  196:     */       {
/*  197:     */         try
/*  198:     */         {
/*  199: 167 */           con.close();
/*  200:     */         }
/*  201:     */         catch (SQLException ex1) {}
/*  202: 170 */         con = null;
/*  203:     */       }
/*  204:     */     }
/*  205:     */     finally
/*  206:     */     {
/*  207: 174 */       if (rs != null)
/*  208:     */       {
/*  209:     */         try
/*  210:     */         {
/*  211: 176 */           rs.close();
/*  212:     */         }
/*  213:     */         catch (SQLException e)
/*  214:     */         {
/*  215: 178 */           System.out.println(e.getMessage());
/*  216:     */         }
/*  217: 180 */         rs = null;
/*  218:     */       }
/*  219: 182 */       if (prepstat != null)
/*  220:     */       {
/*  221:     */         try
/*  222:     */         {
/*  223: 184 */           prepstat.close();
/*  224:     */         }
/*  225:     */         catch (SQLException e)
/*  226:     */         {
/*  227: 186 */           System.out.println(e.getMessage());
/*  228:     */         }
/*  229: 188 */         prepstat = null;
/*  230:     */       }
/*  231: 190 */       if (con != null)
/*  232:     */       {
/*  233:     */         try
/*  234:     */         {
/*  235: 192 */           con.close();
/*  236:     */         }
/*  237:     */         catch (SQLException e)
/*  238:     */         {
/*  239: 194 */           System.out.println(e.getMessage());
/*  240:     */         }
/*  241: 196 */         con = null;
/*  242:     */       }
/*  243:     */     }
/*  244:     */   }
/*  245:     */   
/*  246:     */   public void updateDealer(String username, String name, String defaultSmsc, String defaultService)
/*  247:     */     throws Exception
/*  248:     */   {
/*  249: 205 */     ResultSet rs = null;
/*  250: 206 */     Connection con = null;
/*  251: 207 */     PreparedStatement prepstat = null;
/*  252:     */     try
/*  253:     */     {
/*  254: 210 */       con = DConnect.getConnection("rmcs");
/*  255: 211 */       String SQL = "Update cp_user set name=?, default_service=?,default_smsc=? where username=?";
/*  256:     */       
/*  257:     */ 
/*  258: 214 */       prepstat = con.prepareStatement(SQL);
/*  259: 215 */       prepstat.setString(1, name);
/*  260: 216 */       prepstat.setString(2, defaultService);
/*  261: 217 */       prepstat.setString(3, defaultSmsc);
/*  262: 218 */       prepstat.setString(4, username);
/*  263:     */       
/*  264: 220 */       prepstat.execute();
/*  265:     */     }
/*  266:     */     catch (Exception ex)
/*  267:     */     {
/*  268: 222 */       if (con != null)
/*  269:     */       {
/*  270:     */         try
/*  271:     */         {
/*  272: 224 */           con.close();
/*  273:     */         }
/*  274:     */         catch (SQLException ex1) {}
/*  275: 227 */         con = null;
/*  276:     */       }
/*  277:     */     }
/*  278:     */     finally
/*  279:     */     {
/*  280: 231 */       if (rs != null)
/*  281:     */       {
/*  282:     */         try
/*  283:     */         {
/*  284: 233 */           rs.close();
/*  285:     */         }
/*  286:     */         catch (SQLException e)
/*  287:     */         {
/*  288: 235 */           System.out.println(e.getMessage());
/*  289:     */         }
/*  290: 237 */         rs = null;
/*  291:     */       }
/*  292: 239 */       if (prepstat != null)
/*  293:     */       {
/*  294:     */         try
/*  295:     */         {
/*  296: 241 */           prepstat.close();
/*  297:     */         }
/*  298:     */         catch (SQLException e)
/*  299:     */         {
/*  300: 243 */           System.out.println(e.getMessage());
/*  301:     */         }
/*  302: 245 */         prepstat = null;
/*  303:     */       }
/*  304: 247 */       if (con != null)
/*  305:     */       {
/*  306:     */         try
/*  307:     */         {
/*  308: 249 */           con.close();
/*  309:     */         }
/*  310:     */         catch (SQLException e)
/*  311:     */         {
/*  312: 251 */           System.out.println(e.getMessage());
/*  313:     */         }
/*  314: 253 */         con = null;
/*  315:     */       }
/*  316:     */     }
/*  317:     */   }
/*  318:     */   
/*  319:     */   public void updateDealerInfo(String username, String name, String password, String defaultSmsc, String defaultService, String logoUrl)
/*  320:     */     throws Exception
/*  321:     */   {
/*  322: 263 */     ResultSet rs = null;
/*  323: 264 */     Connection con = null;
/*  324: 265 */     PreparedStatement prepstat = null;
/*  325:     */     try
/*  326:     */     {
/*  327: 268 */       con = DConnect.getConnection("rmcs");
/*  328: 269 */       String SQL = "Update cp_user set name=?,password=?, default_service=?,default_smsc=?,logo_url =? where username=?";
/*  329:     */       
/*  330: 271 */       prepstat = con.prepareStatement(SQL);
/*  331: 272 */       prepstat.setString(1, name);
/*  332: 273 */       prepstat.setString(2, password);
/*  333: 274 */       prepstat.setString(3, defaultService);
/*  334: 275 */       prepstat.setString(4, defaultSmsc);
/*  335: 276 */       prepstat.setString(5, logoUrl);
/*  336: 277 */       prepstat.setString(6, username);
/*  337: 278 */       prepstat.execute();
/*  338:     */     }
/*  339:     */     catch (Exception ex)
/*  340:     */     {
/*  341: 280 */       if (con != null)
/*  342:     */       {
/*  343:     */         try
/*  344:     */         {
/*  345: 282 */           con.close();
/*  346:     */         }
/*  347:     */         catch (SQLException ex1) {}
/*  348: 285 */         con = null;
/*  349:     */       }
/*  350:     */     }
/*  351:     */     finally
/*  352:     */     {
/*  353: 289 */       if (rs != null)
/*  354:     */       {
/*  355:     */         try
/*  356:     */         {
/*  357: 291 */           rs.close();
/*  358:     */         }
/*  359:     */         catch (SQLException e)
/*  360:     */         {
/*  361: 293 */           System.out.println(e.getMessage());
/*  362:     */         }
/*  363: 295 */         rs = null;
/*  364:     */       }
/*  365: 297 */       if (prepstat != null)
/*  366:     */       {
/*  367:     */         try
/*  368:     */         {
/*  369: 299 */           prepstat.close();
/*  370:     */         }
/*  371:     */         catch (SQLException e)
/*  372:     */         {
/*  373: 301 */           System.out.println(e.getMessage());
/*  374:     */         }
/*  375: 303 */         prepstat = null;
/*  376:     */       }
/*  377: 305 */       if (con != null)
/*  378:     */       {
/*  379:     */         try
/*  380:     */         {
/*  381: 307 */           con.close();
/*  382:     */         }
/*  383:     */         catch (SQLException e)
/*  384:     */         {
/*  385: 309 */           System.out.println(e.getMessage());
/*  386:     */         }
/*  387: 311 */         con = null;
/*  388:     */       }
/*  389:     */     }
/*  390:     */   }
/*  391:     */   
/*  392:     */   public void updateDealerInfo(String username, String name, String defaultSmsc, String defaultService, String logoUrl)
/*  393:     */     throws Exception
/*  394:     */   {
/*  395: 321 */     ResultSet rs = null;
/*  396: 322 */     Connection con = null;
/*  397: 323 */     PreparedStatement prepstat = null;
/*  398:     */     try
/*  399:     */     {
/*  400: 326 */       con = DConnect.getConnection("rmcs");
/*  401: 327 */       String SQL = "Update cp_user set name=?, default_service=?,default_smsc=?,logo_url =? where username=?";
/*  402:     */       
/*  403:     */ 
/*  404: 330 */       prepstat = con.prepareStatement(SQL);
/*  405: 331 */       prepstat.setString(1, name);
/*  406: 332 */       prepstat.setString(2, defaultService);
/*  407: 333 */       prepstat.setString(3, defaultSmsc);
/*  408: 334 */       prepstat.setString(4, logoUrl);
/*  409: 335 */       prepstat.setString(5, username);
/*  410: 336 */       prepstat.execute();
/*  411:     */     }
/*  412:     */     catch (Exception ex)
/*  413:     */     {
/*  414: 338 */       if (con != null)
/*  415:     */       {
/*  416:     */         try
/*  417:     */         {
/*  418: 340 */           con.close();
/*  419:     */         }
/*  420:     */         catch (SQLException ex1) {}
/*  421: 343 */         con = null;
/*  422:     */       }
/*  423:     */     }
/*  424:     */     finally
/*  425:     */     {
/*  426: 347 */       if (rs != null)
/*  427:     */       {
/*  428:     */         try
/*  429:     */         {
/*  430: 349 */           rs.close();
/*  431:     */         }
/*  432:     */         catch (SQLException e)
/*  433:     */         {
/*  434: 351 */           System.out.println(e.getMessage());
/*  435:     */         }
/*  436: 353 */         rs = null;
/*  437:     */       }
/*  438: 355 */       if (prepstat != null)
/*  439:     */       {
/*  440:     */         try
/*  441:     */         {
/*  442: 357 */           prepstat.close();
/*  443:     */         }
/*  444:     */         catch (SQLException e)
/*  445:     */         {
/*  446: 359 */           System.out.println(e.getMessage());
/*  447:     */         }
/*  448: 361 */         prepstat = null;
/*  449:     */       }
/*  450: 363 */       if (con != null)
/*  451:     */       {
/*  452:     */         try
/*  453:     */         {
/*  454: 365 */           con.close();
/*  455:     */         }
/*  456:     */         catch (SQLException e)
/*  457:     */         {
/*  458: 367 */           System.out.println(e.getMessage());
/*  459:     */         }
/*  460: 369 */         con = null;
/*  461:     */       }
/*  462:     */     }
/*  463:     */   }
/*  464:     */   
/*  465:     */   public void updateDealer(String username, String language)
/*  466:     */     throws Exception
/*  467:     */   {
/*  468: 377 */     ResultSet rs = null;
/*  469: 378 */     Connection con = null;
/*  470: 379 */     PreparedStatement prepstat = null;
/*  471:     */     try
/*  472:     */     {
/*  473: 382 */       con = DConnect.getConnection("rmcs");
/*  474: 383 */       String SQL = "Update cp_user set default_language=? where username=?";
/*  475:     */       
/*  476:     */ 
/*  477: 386 */       prepstat = con.prepareStatement(SQL);
/*  478: 387 */       prepstat.setString(1, language);
/*  479: 388 */       prepstat.setString(2, username);
/*  480:     */       
/*  481: 390 */       prepstat.execute();
/*  482:     */     }
/*  483:     */     catch (Exception ex)
/*  484:     */     {
/*  485: 392 */       if (con != null)
/*  486:     */       {
/*  487:     */         try
/*  488:     */         {
/*  489: 394 */           con.close();
/*  490:     */         }
/*  491:     */         catch (SQLException ex1) {}
/*  492: 397 */         con = null;
/*  493:     */       }
/*  494:     */     }
/*  495:     */     finally
/*  496:     */     {
/*  497: 401 */       if (rs != null)
/*  498:     */       {
/*  499:     */         try
/*  500:     */         {
/*  501: 403 */           rs.close();
/*  502:     */         }
/*  503:     */         catch (SQLException e)
/*  504:     */         {
/*  505: 405 */           System.out.println(e.getMessage());
/*  506:     */         }
/*  507: 407 */         rs = null;
/*  508:     */       }
/*  509: 409 */       if (prepstat != null)
/*  510:     */       {
/*  511:     */         try
/*  512:     */         {
/*  513: 411 */           prepstat.close();
/*  514:     */         }
/*  515:     */         catch (SQLException e)
/*  516:     */         {
/*  517: 413 */           System.out.println(e.getMessage());
/*  518:     */         }
/*  519: 415 */         prepstat = null;
/*  520:     */       }
/*  521: 417 */       if (con != null)
/*  522:     */       {
/*  523:     */         try
/*  524:     */         {
/*  525: 419 */           con.close();
/*  526:     */         }
/*  527:     */         catch (SQLException e)
/*  528:     */         {
/*  529: 421 */           System.out.println(e.getMessage());
/*  530:     */         }
/*  531: 423 */         con = null;
/*  532:     */       }
/*  533:     */     }
/*  534:     */   }
/*  535:     */   
/*  536:     */   public void updateDealer(String id, double bandwidth, double inbox, double outbox)
/*  537:     */     throws Exception
/*  538:     */   {
/*  539: 431 */     ResultSet rs = null;
/*  540: 432 */     Connection con = null;
/*  541: 433 */     PreparedStatement prepstat = null;
/*  542:     */     try
/*  543:     */     {
/*  544: 436 */       con = DConnect.getConnection("rmcs");
/*  545: 437 */       String SQL = "Update cp_user set bandwidth_balance=?, inbox_balance=?, outbox_balance=? where id=?";
/*  546:     */       
/*  547:     */ 
/*  548: 440 */       prepstat = con.prepareStatement(SQL);
/*  549: 441 */       prepstat.setDouble(1, bandwidth);
/*  550: 442 */       prepstat.setDouble(2, inbox);
/*  551: 443 */       prepstat.setDouble(3, outbox);
/*  552: 444 */       prepstat.setString(4, id);
/*  553:     */       
/*  554: 446 */       prepstat.execute();
/*  555:     */     }
/*  556:     */     catch (Exception ex)
/*  557:     */     {
/*  558: 448 */       if (con != null)
/*  559:     */       {
/*  560:     */         try
/*  561:     */         {
/*  562: 450 */           con.close();
/*  563:     */         }
/*  564:     */         catch (SQLException ex1) {}
/*  565: 453 */         con = null;
/*  566:     */       }
/*  567:     */     }
/*  568:     */     finally
/*  569:     */     {
/*  570: 457 */       if (rs != null)
/*  571:     */       {
/*  572:     */         try
/*  573:     */         {
/*  574: 459 */           rs.close();
/*  575:     */         }
/*  576:     */         catch (SQLException e)
/*  577:     */         {
/*  578: 461 */           System.out.println(e.getMessage());
/*  579:     */         }
/*  580: 463 */         rs = null;
/*  581:     */       }
/*  582: 465 */       if (prepstat != null)
/*  583:     */       {
/*  584:     */         try
/*  585:     */         {
/*  586: 467 */           prepstat.close();
/*  587:     */         }
/*  588:     */         catch (SQLException e)
/*  589:     */         {
/*  590: 469 */           System.out.println(e.getMessage());
/*  591:     */         }
/*  592: 471 */         prepstat = null;
/*  593:     */       }
/*  594: 473 */       if (con != null)
/*  595:     */       {
/*  596:     */         try
/*  597:     */         {
/*  598: 475 */           con.close();
/*  599:     */         }
/*  600:     */         catch (SQLException e)
/*  601:     */         {
/*  602: 477 */           System.out.println(e.getMessage());
/*  603:     */         }
/*  604: 479 */         con = null;
/*  605:     */       }
/*  606:     */     }
/*  607:     */   }
/*  608:     */   
/*  609:     */   public void changePassword(String id, String password)
/*  610:     */     throws Exception
/*  611:     */   {
/*  612: 487 */     ResultSet rs = null;
/*  613: 488 */     Connection con = null;
/*  614: 489 */     PreparedStatement prepstat = null;
/*  615:     */     try
/*  616:     */     {
/*  617: 492 */       con = DConnect.getConnection("rmcs");
/*  618: 493 */       String SQL = "Update dealer set password=? where id=?";
/*  619:     */       
/*  620: 495 */       prepstat = con.prepareStatement(SQL);
/*  621:     */       
/*  622: 497 */       prepstat.setString(1, password);
/*  623: 498 */       prepstat.setString(2, id);
/*  624:     */       
/*  625: 500 */       prepstat.execute();
/*  626:     */     }
/*  627:     */     catch (Exception ex)
/*  628:     */     {
/*  629: 502 */       if (con != null)
/*  630:     */       {
/*  631:     */         try
/*  632:     */         {
/*  633: 504 */           con.close();
/*  634:     */         }
/*  635:     */         catch (SQLException ex1) {}
/*  636: 507 */         con = null;
/*  637:     */       }
/*  638:     */     }
/*  639:     */     finally
/*  640:     */     {
/*  641: 511 */       if (rs != null)
/*  642:     */       {
/*  643:     */         try
/*  644:     */         {
/*  645: 513 */           rs.close();
/*  646:     */         }
/*  647:     */         catch (SQLException e)
/*  648:     */         {
/*  649: 515 */           System.out.println(e.getMessage());
/*  650:     */         }
/*  651: 517 */         rs = null;
/*  652:     */       }
/*  653: 519 */       if (prepstat != null)
/*  654:     */       {
/*  655:     */         try
/*  656:     */         {
/*  657: 521 */           prepstat.close();
/*  658:     */         }
/*  659:     */         catch (SQLException e)
/*  660:     */         {
/*  661: 523 */           System.out.println(e.getMessage());
/*  662:     */         }
/*  663: 525 */         prepstat = null;
/*  664:     */       }
/*  665: 527 */       if (con != null)
/*  666:     */       {
/*  667:     */         try
/*  668:     */         {
/*  669: 529 */           con.close();
/*  670:     */         }
/*  671:     */         catch (SQLException e)
/*  672:     */         {
/*  673: 531 */           System.out.println(e.getMessage());
/*  674:     */         }
/*  675: 533 */         con = null;
/*  676:     */       }
/*  677:     */     }
/*  678:     */   }
/*  679:     */   
/*  680:     */   public void deleteDealer(String id)
/*  681:     */     throws Exception
/*  682:     */   {
/*  683: 540 */     ResultSet rs = null;
/*  684: 541 */     Connection con = null;
/*  685: 542 */     PreparedStatement prepstat = null;
/*  686:     */     try
/*  687:     */     {
/*  688: 545 */       con = DConnect.getConnection("rmcs");
/*  689: 546 */       String SQL = "delete from cp_user where id ='" + id + "'";
/*  690: 547 */       prepstat = con.prepareStatement(SQL);
/*  691: 548 */       prepstat.execute();
/*  692:     */     }
/*  693:     */     catch (Exception ex)
/*  694:     */     {
/*  695: 550 */       if (con != null)
/*  696:     */       {
/*  697:     */         try
/*  698:     */         {
/*  699: 552 */           con.close();
/*  700:     */         }
/*  701:     */         catch (SQLException ex1) {}
/*  702: 556 */         con = null;
/*  703:     */       }
/*  704:     */     }
/*  705:     */     finally
/*  706:     */     {
/*  707: 560 */       if (rs != null)
/*  708:     */       {
/*  709:     */         try
/*  710:     */         {
/*  711: 562 */           rs.close();
/*  712:     */         }
/*  713:     */         catch (SQLException e)
/*  714:     */         {
/*  715: 564 */           System.out.println(e.getMessage());
/*  716:     */         }
/*  717: 566 */         rs = null;
/*  718:     */       }
/*  719: 568 */       if (prepstat != null)
/*  720:     */       {
/*  721:     */         try
/*  722:     */         {
/*  723: 570 */           prepstat.close();
/*  724:     */         }
/*  725:     */         catch (SQLException e)
/*  726:     */         {
/*  727: 572 */           System.out.println(e.getMessage());
/*  728:     */         }
/*  729: 574 */         prepstat = null;
/*  730:     */       }
/*  731: 576 */       if (con != null)
/*  732:     */       {
/*  733:     */         try
/*  734:     */         {
/*  735: 578 */           con.close();
/*  736:     */         }
/*  737:     */         catch (SQLException e)
/*  738:     */         {
/*  739: 580 */           System.out.println(e.getMessage());
/*  740:     */         }
/*  741: 582 */         con = null;
/*  742:     */       }
/*  743:     */     }
/*  744:     */   }
/*  745:     */   
/*  746:     */   public void deleteDealer(String[] ids)
/*  747:     */     throws Exception
/*  748:     */   {
/*  749: 590 */     ResultSet rs = null;
/*  750: 591 */     Connection con = null;
/*  751: 592 */     PreparedStatement prepstat = null;
/*  752:     */     
/*  753: 594 */     String deleteList = "";
/*  754: 595 */     for (int i = 0; i < ids.length; i++) {
/*  755: 596 */       deleteList = deleteList + "'" + ids[i] + "'" + (ids.length - 1 == i ? "" : ",");
/*  756:     */     }
/*  757: 600 */     deleteList = "(" + deleteList + ")";
/*  758:     */     try
/*  759:     */     {
/*  760: 603 */       con = DConnect.getConnection("rmcs");
/*  761: 604 */       String SQL = "delete from cp_user where id  IN " + deleteList;
/*  762: 605 */       prepstat = con.prepareStatement(SQL);
/*  763: 606 */       prepstat.execute();
/*  764:     */     }
/*  765:     */     catch (Exception ex)
/*  766:     */     {
/*  767: 608 */       if (con != null)
/*  768:     */       {
/*  769:     */         try
/*  770:     */         {
/*  771: 610 */           con.close();
/*  772:     */         }
/*  773:     */         catch (SQLException ex1) {}
/*  774: 614 */         con = null;
/*  775:     */       }
/*  776:     */     }
/*  777:     */     finally
/*  778:     */     {
/*  779: 618 */       if (rs != null)
/*  780:     */       {
/*  781:     */         try
/*  782:     */         {
/*  783: 620 */           rs.close();
/*  784:     */         }
/*  785:     */         catch (SQLException e)
/*  786:     */         {
/*  787: 622 */           System.out.println(e.getMessage());
/*  788:     */         }
/*  789: 624 */         rs = null;
/*  790:     */       }
/*  791: 626 */       if (prepstat != null)
/*  792:     */       {
/*  793:     */         try
/*  794:     */         {
/*  795: 628 */           prepstat.close();
/*  796:     */         }
/*  797:     */         catch (SQLException e)
/*  798:     */         {
/*  799: 630 */           System.out.println(e.getMessage());
/*  800:     */         }
/*  801: 632 */         prepstat = null;
/*  802:     */       }
/*  803: 634 */       if (con != null)
/*  804:     */       {
/*  805:     */         try
/*  806:     */         {
/*  807: 636 */           con.close();
/*  808:     */         }
/*  809:     */         catch (SQLException e)
/*  810:     */         {
/*  811: 638 */           System.out.println(e.getMessage());
/*  812:     */         }
/*  813: 640 */         con = null;
/*  814:     */       }
/*  815:     */     }
/*  816:     */   }
/*  817:     */   
/*  818:     */   public User viewDealer(String id)
/*  819:     */     throws Exception
/*  820:     */   {
/*  821: 647 */     User dealer = new User();
/*  822:     */     
/*  823:     */ 
/*  824: 650 */     ResultSet rs = null;
/*  825: 651 */     Connection con = null;
/*  826: 652 */     PreparedStatement prepstat = null;
/*  827:     */     
/*  828:     */ 
/*  829: 655 */     System.out.println(new Date() + ":@com.rancard.mobility.contentprovider.UserDB...");
/*  830: 656 */     System.out.println(new Date() + ": viewing cp_user/dealer (" + id + ")...");
/*  831:     */     try
/*  832:     */     {
/*  833: 659 */       con = DConnect.getConnection("rmcs");
/*  834:     */       
/*  835: 661 */       String SQL = "SELECT * FROM cp_user  where cp_user.id='" + id + "'";
/*  836:     */       
/*  837: 663 */       prepstat = con.prepareStatement(SQL);
/*  838:     */       
/*  839: 665 */       rs = prepstat.executeQuery();
/*  840: 666 */       ArrayList services = new ArrayList();
/*  841: 667 */       while (rs.next())
/*  842:     */       {
/*  843: 668 */         dealer.setId(rs.getString("id"));
/*  844: 669 */         dealer.setName(rs.getString("name"));
/*  845: 670 */         dealer.setPassword(rs.getString("password"));
/*  846: 671 */         dealer.setUsername(rs.getString("username"));
/*  847: 672 */         dealer.setDefaultSmsc(rs.getString("default_smsc"));
/*  848: 673 */         dealer.setDefaultService(rs.getString("default_service"));
/*  849: 674 */         dealer.setLogoUrl(rs.getString("logo_url"));
/*  850: 675 */         dealer.setDefaultLanguage(rs.getString("default_language"));
/*  851: 676 */         dealer.setBandwidthBalance(rs.getDouble("bandwidth_balance"));
/*  852: 677 */         dealer.setInboxBalance(rs.getDouble("inbox_balance"));
/*  853: 678 */         dealer.setOutboxBalance(rs.getDouble("outbox_balance"));
/*  854:     */         
/*  855: 680 */         System.out.println(new Date() + ": cp_user (" + id + ") found!");
/*  856:     */       }
/*  857: 683 */       if ((dealer.getId() == null) || ("".equals(dealer.getId()))) {
/*  858: 684 */         System.out.println(new Date() + ": cp_user (" + id + ") not found!");
/*  859:     */       }
/*  860: 687 */       if ((dealer.getId() != null) && (!dealer.getId().equals("")))
/*  861:     */       {
/*  862: 688 */         SQL = "SELECT * FROM cp_user_profile up inner join service_route sr on up.service_type_id=sr.service_type where up.account_id='" + id + "'";
/*  863: 689 */         prepstat = con.prepareStatement(SQL);
/*  864:     */         
/*  865: 691 */         rs = prepstat.executeQuery();
/*  866: 693 */         while (rs.next())
/*  867:     */         {
/*  868: 694 */           ContentType service = new ContentType();
/*  869: 695 */           service.setParentServiceType(rs.getInt("sr.parent_service_type"));
/*  870: 696 */           service.setServiceName(rs.getString("sr.service_name"));
/*  871: 697 */           service.setServiceType(rs.getInt("sr.service_type"));
/*  872:     */           
/*  873: 699 */           services.add(service);
/*  874:     */         }
/*  875: 701 */         dealer.setServices(services);
/*  876:     */       }
/*  877: 703 */       rs.close();
/*  878: 704 */       rs = null;
/*  879: 705 */       prepstat.close();
/*  880: 706 */       prepstat = null;
/*  881: 707 */       con.close();
/*  882: 708 */       con = null;
/*  883:     */     }
/*  884:     */     catch (Exception ex)
/*  885:     */     {
/*  886: 710 */       if (con != null)
/*  887:     */       {
/*  888: 711 */         con.close();
/*  889: 712 */         con = null;
/*  890:     */       }
/*  891: 715 */       System.out.println(new Date() + ": error viewing cp_user (" + id + "): " + ex.getMessage());
/*  892:     */       
/*  893: 717 */       throw new Exception(ex.getMessage());
/*  894:     */     }
/*  895:     */     finally
/*  896:     */     {
/*  897: 719 */       if (rs != null)
/*  898:     */       {
/*  899:     */         try
/*  900:     */         {
/*  901: 721 */           rs.close();
/*  902:     */         }
/*  903:     */         catch (SQLException e)
/*  904:     */         {
/*  905: 723 */           System.out.println(e.getMessage());
/*  906:     */         }
/*  907: 725 */         rs = null;
/*  908:     */       }
/*  909: 727 */       if (prepstat != null)
/*  910:     */       {
/*  911:     */         try
/*  912:     */         {
/*  913: 729 */           prepstat.close();
/*  914:     */         }
/*  915:     */         catch (SQLException e)
/*  916:     */         {
/*  917: 731 */           System.out.println(e.getMessage());
/*  918:     */         }
/*  919: 733 */         prepstat = null;
/*  920:     */       }
/*  921: 735 */       if (con != null)
/*  922:     */       {
/*  923:     */         try
/*  924:     */         {
/*  925: 737 */           con.close();
/*  926:     */         }
/*  927:     */         catch (SQLException e)
/*  928:     */         {
/*  929: 739 */           System.out.println(e.getMessage());
/*  930:     */         }
/*  931: 741 */         con = null;
/*  932:     */       }
/*  933:     */     }
/*  934: 745 */     return dealer;
/*  935:     */   }
/*  936:     */   
/*  937:     */   public User view(String username, String password)
/*  938:     */     throws Exception
/*  939:     */   {
/*  940: 749 */     User dealer = new User();
/*  941:     */     
/*  942:     */ 
/*  943: 752 */     ResultSet rs = null;
/*  944: 753 */     Connection con = null;
/*  945: 754 */     PreparedStatement prepstat = null;
/*  946:     */     try
/*  947:     */     {
/*  948: 757 */       con = DConnect.getConnection("rmcs");
/*  949:     */       
/*  950: 759 */       String SQL = "SELECT * FROM cp_user where cp_user.username='" + username + "' and cp_user.password = '" + password + "'";
/*  951:     */       
/*  952: 761 */       prepstat = con.prepareStatement(SQL);
/*  953:     */       
/*  954: 763 */       rs = prepstat.executeQuery();
/*  955: 764 */       ArrayList services = new ArrayList();
/*  956: 765 */       while (rs.next())
/*  957:     */       {
/*  958: 769 */         dealer.setId(rs.getString("id"));
/*  959: 770 */         dealer.setName(rs.getString("name"));
/*  960: 771 */         dealer.setPassword(rs.getString("password"));
/*  961: 772 */         dealer.setUsername(rs.getString("username"));
/*  962: 773 */         dealer.setDefaultSmsc(rs.getString("default_smsc"));
/*  963: 774 */         dealer.setDefaultService(rs.getString("default_service"));
/*  964: 775 */         dealer.setLogoUrl(rs.getString("logo_url"));
/*  965: 776 */         dealer.setDefaultLanguage(rs.getString("default_language"));
/*  966: 777 */         dealer.setBandwidthBalance(rs.getDouble("bandwidth_balance"));
/*  967: 778 */         dealer.setInboxBalance(rs.getDouble("inbox_balance"));
/*  968: 779 */         dealer.setOutboxBalance(rs.getDouble("outbox_balance"));
/*  969:     */       }
/*  970: 791 */       if ((dealer.getId() != null) && (!dealer.getId().equals("")))
/*  971:     */       {
/*  972: 792 */         SQL = "SELECT * FROM cp_user_profile up inner join service_route sr on up.service_type_id=sr.service_type where up.account_id='" + dealer.getId() + "'";
/*  973: 793 */         prepstat = con.prepareStatement(SQL);
/*  974:     */         
/*  975: 795 */         rs = prepstat.executeQuery();
/*  976: 797 */         while (rs.next())
/*  977:     */         {
/*  978: 798 */           ContentType service = new ContentType();
/*  979: 799 */           service.setParentServiceType(rs.getInt("sr.parent_service_type"));
/*  980: 800 */           service.setServiceName(rs.getString("sr.service_name"));
/*  981: 801 */           service.setServiceType(rs.getInt("sr.service_type"));
/*  982:     */           
/*  983: 803 */           services.add(service);
/*  984:     */         }
/*  985: 805 */         dealer.setServices(services);
/*  986:     */       }
/*  987: 808 */       rs.close();
/*  988: 809 */       rs = null;
/*  989: 810 */       prepstat.close();
/*  990: 811 */       prepstat = null;
/*  991: 812 */       con.close();
/*  992: 813 */       con = null;
/*  993:     */     }
/*  994:     */     catch (Exception ex)
/*  995:     */     {
/*  996: 815 */       if (con != null)
/*  997:     */       {
/*  998: 816 */         con.close();
/*  999: 817 */         con = null;
/* 1000:     */       }
/* 1001: 819 */       throw new Exception(ex.getMessage());
/* 1002:     */     }
/* 1003:     */     finally
/* 1004:     */     {
/* 1005: 821 */       if (rs != null)
/* 1006:     */       {
/* 1007:     */         try
/* 1008:     */         {
/* 1009: 823 */           rs.close();
/* 1010:     */         }
/* 1011:     */         catch (SQLException e)
/* 1012:     */         {
/* 1013: 825 */           System.out.println(e.getMessage());
/* 1014:     */         }
/* 1015: 827 */         rs = null;
/* 1016:     */       }
/* 1017: 829 */       if (prepstat != null)
/* 1018:     */       {
/* 1019:     */         try
/* 1020:     */         {
/* 1021: 831 */           prepstat.close();
/* 1022:     */         }
/* 1023:     */         catch (SQLException e)
/* 1024:     */         {
/* 1025: 833 */           System.out.println(e.getMessage());
/* 1026:     */         }
/* 1027: 835 */         prepstat = null;
/* 1028:     */       }
/* 1029: 837 */       if (con != null)
/* 1030:     */       {
/* 1031:     */         try
/* 1032:     */         {
/* 1033: 839 */           con.close();
/* 1034:     */         }
/* 1035:     */         catch (SQLException e)
/* 1036:     */         {
/* 1037: 841 */           System.out.println(e.getMessage());
/* 1038:     */         }
/* 1039: 843 */         con = null;
/* 1040:     */       }
/* 1041:     */     }
/* 1042: 847 */     return dealer;
/* 1043:     */   }
/* 1044:     */   
/* 1045:     */   public ArrayList getDealers()
/* 1046:     */     throws Exception
/* 1047:     */   {
/* 1048: 852 */     ArrayList dealers = new ArrayList();
/* 1049:     */     
/* 1050: 854 */     ResultSet rs = null;
/* 1051: 855 */     Connection con = null;
/* 1052: 856 */     PreparedStatement prepstat = null;
/* 1053:     */     try
/* 1054:     */     {
/* 1055: 859 */       con = DConnect.getConnection("rmcs");
/* 1056:     */       
/* 1057: 861 */       String SQL = "Select * from cp_user";
/* 1058: 862 */       prepstat = con.prepareStatement(SQL);
/* 1059:     */       
/* 1060: 864 */       rs = prepstat.executeQuery();
/* 1061: 866 */       while (rs.next())
/* 1062:     */       {
/* 1063: 867 */         User dealer = new User();
/* 1064:     */         
/* 1065:     */ 
/* 1066:     */ 
/* 1067: 871 */         dealer.setId(rs.getString("id"));
/* 1068: 872 */         dealer.setName(rs.getString("name"));
/* 1069: 873 */         dealer.setPassword(rs.getString("password"));
/* 1070: 874 */         dealer.setUsername(rs.getString("username"));
/* 1071:     */         
/* 1072: 876 */         dealers.add(dealer);
/* 1073:     */       }
/* 1074: 878 */       rs.close();
/* 1075: 879 */       rs = null;
/* 1076: 880 */       prepstat.close();
/* 1077: 881 */       prepstat = null;
/* 1078: 882 */       con.close();
/* 1079: 883 */       con = null;
/* 1080:     */     }
/* 1081:     */     catch (Exception ex)
/* 1082:     */     {
/* 1083: 885 */       if (con != null)
/* 1084:     */       {
/* 1085: 886 */         con.close();
/* 1086: 887 */         con = null;
/* 1087:     */       }
/* 1088: 889 */       throw new Exception(ex.getMessage());
/* 1089:     */     }
/* 1090:     */     finally
/* 1091:     */     {
/* 1092: 891 */       if (rs != null)
/* 1093:     */       {
/* 1094:     */         try
/* 1095:     */         {
/* 1096: 893 */           rs.close();
/* 1097:     */         }
/* 1098:     */         catch (SQLException e)
/* 1099:     */         {
/* 1100: 895 */           System.out.println(e.getMessage());
/* 1101:     */         }
/* 1102: 897 */         rs = null;
/* 1103:     */       }
/* 1104: 899 */       if (prepstat != null)
/* 1105:     */       {
/* 1106:     */         try
/* 1107:     */         {
/* 1108: 901 */           prepstat.close();
/* 1109:     */         }
/* 1110:     */         catch (SQLException e)
/* 1111:     */         {
/* 1112: 903 */           System.out.println(e.getMessage());
/* 1113:     */         }
/* 1114: 905 */         prepstat = null;
/* 1115:     */       }
/* 1116: 907 */       if (con != null)
/* 1117:     */       {
/* 1118:     */         try
/* 1119:     */         {
/* 1120: 909 */           con.close();
/* 1121:     */         }
/* 1122:     */         catch (SQLException e)
/* 1123:     */         {
/* 1124: 911 */           System.out.println(e.getMessage());
/* 1125:     */         }
/* 1126: 913 */         con = null;
/* 1127:     */       }
/* 1128:     */     }
/* 1129: 916 */     return dealers;
/* 1130:     */   }
/* 1131:     */   
/* 1132:     */   public static void main(String[] args)
/* 1133:     */     throws Exception
/* 1134:     */   {
/* 1135:1117 */     UserDB d = new UserDB();
/* 1136:     */   }
/* 1137:     */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentprovider.UserDB
 * JD-Core Version:    0.7.0.1
 */