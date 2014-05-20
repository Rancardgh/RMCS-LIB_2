/*    1:     */ package com.rancard.mobility.infoserver;
/*    2:     */ 
/*    3:     */ import com.rancard.common.DConnect;
/*    4:     */ import com.rancard.mobility.infoserver.common.services.ServiceManager;
/*    5:     */ import com.rancard.mobility.infoserver.common.services.UserService;
/*    6:     */ import com.rancard.mobility.infoserver.common.services.UserServiceDB;
/*    7:     */ import com.rancard.util.URLUTF8Encoder;
/*    8:     */ import java.io.BufferedReader;
/*    9:     */ import java.io.PrintStream;
/*   10:     */ import java.sql.Clob;
/*   11:     */ import java.sql.Connection;
/*   12:     */ import java.sql.PreparedStatement;
/*   13:     */ import java.sql.ResultSet;
/*   14:     */ import java.sql.SQLException;
/*   15:     */ import java.sql.Timestamp;
/*   16:     */ import java.text.SimpleDateFormat;
/*   17:     */ import java.util.ArrayList;
/*   18:     */ import java.util.HashMap;
/*   19:     */ import java.util.Random;
/*   20:     */ 
/*   21:     */ public class InfoServiceDB
/*   22:     */   extends UserServiceDB
/*   23:     */ {
/*   24:     */   public void createInfoServiceEntry(java.util.Date date, String keyword, String message, String accountId, String ownerId, String imageURL, String articleTitle)
/*   25:     */     throws Exception
/*   26:     */   {
/*   27:  24 */     UserService serv = viewService(keyword, accountId);
/*   28:  25 */     int test = 0;
/*   29:  26 */     if ((serv.getAccountId() != null) && (serv.getAccountId().equals(accountId)) && (serv.getKeyword() != null) && (serv.getKeyword().equals(keyword))) {
/*   30:  27 */       test = 1;
/*   31:     */     } else {
/*   32:  29 */       test = 0;
/*   33:     */     }
/*   34:  32 */     if (test == 1)
/*   35:     */     {
/*   36:  35 */       ResultSet rs = null;
/*   37:  36 */       ResultSet rs_label = null;
/*   38:  37 */       Connection con = null;
/*   39:  38 */       PreparedStatement prepstat = null;
/*   40:  39 */       PreparedStatement prepstat_label = null;
/*   41:     */       try
/*   42:     */       {
/*   43:  42 */         con = DConnect.getConnection();
/*   44:  43 */         java.sql.Date sqlDate_date = new java.sql.Date(date.getTime());
/*   45:     */         
/*   46:  45 */         int test_exists = 0;
/*   47:     */         
/*   48:     */ 
/*   49:  48 */         String SQL = "select * from system_sms_queue where publish_date = ? and keyword = ?  and account_id= ? order by msg_id desc";
/*   50:     */         
/*   51:  50 */         prepstat = con.prepareStatement(SQL);
/*   52:  51 */         prepstat.setDate(1, sqlDate_date);
/*   53:  52 */         prepstat.setString(2, keyword);
/*   54:  53 */         prepstat.setString(3, accountId);
/*   55:     */         
/*   56:  55 */         rs = prepstat.executeQuery();
/*   57:  56 */         int lastMsgId = 0;
/*   58:  57 */         if (rs.next())
/*   59:     */         {
/*   60:  58 */           test_exists = 1;
/*   61:  59 */           lastMsgId = rs.getInt("msg_id");
/*   62:     */         }
/*   63:  64 */         String SQL_label = "select * from service_labels where account_id = ? and keyword = ?";
/*   64:  65 */         prepstat_label = con.prepareStatement(SQL_label);
/*   65:  66 */         prepstat_label.setString(1, accountId);
/*   66:  67 */         prepstat_label.setString(2, keyword);
/*   67:     */         
/*   68:  69 */         rs_label = prepstat_label.executeQuery();
/*   69:  70 */         String header = "";
/*   70:  71 */         String footer = "";
/*   71:  72 */         while (rs_label.next())
/*   72:     */         {
/*   73:  73 */           header = rs_label.getString("header");
/*   74:  74 */           footer = rs_label.getString("footer");
/*   75:     */         }
/*   76:  78 */         String final_message = header + message + (footer.equals("") ? "" : new StringBuilder().append("\n").append(footer).toString());
/*   77:     */         
/*   78:  80 */         java.util.Date currentDate = new java.util.Date();
/*   79:  81 */         if (test_exists == 1)
/*   80:     */         {
/*   81:  83 */           addInfoService(final_message, keyword, accountId, lastMsgId + 1, sqlDate_date, ownerId, imageURL, articleTitle);
/*   82:  88 */           if (!date.after(currentDate)) {
/*   83:  89 */             updateService(final_message, keyword, accountId);
/*   84:     */           }
/*   85:     */         }
/*   86:  92 */         else if (test_exists == 0)
/*   87:     */         {
/*   88:  94 */           addInfoService(final_message, keyword, accountId, lastMsgId + 1, sqlDate_date, ownerId, imageURL, articleTitle);
/*   89:  97 */           if (!date.after(currentDate)) {
/*   90:  98 */             updateService(final_message, keyword, accountId);
/*   91:     */           }
/*   92:     */         }
/*   93:     */         else
/*   94:     */         {
/*   95: 101 */           throw new Exception("unable to determine insert basis for service item: " + keyword);
/*   96:     */         }
/*   97:     */       }
/*   98:     */       catch (Exception ex)
/*   99:     */       {
/*  100: 105 */         if (con != null) {
/*  101: 106 */           con.close();
/*  102:     */         }
/*  103: 108 */         throw new Exception(ex.getMessage());
/*  104:     */       }
/*  105:     */       finally
/*  106:     */       {
/*  107: 110 */         if (prepstat != null) {
/*  108: 111 */           prepstat.close();
/*  109:     */         }
/*  110: 113 */         if (prepstat_label != null) {
/*  111: 114 */           prepstat_label.close();
/*  112:     */         }
/*  113: 116 */         if (rs != null) {
/*  114: 117 */           rs.close();
/*  115:     */         }
/*  116: 119 */         if (rs_label != null) {
/*  117: 120 */           rs_label.close();
/*  118:     */         }
/*  119: 122 */         if (con != null) {
/*  120: 123 */           con.close();
/*  121:     */         }
/*  122:     */       }
/*  123:     */     }
/*  124:     */     else
/*  125:     */     {
/*  126: 127 */       System.out.println("Cannot create news item. Service does not exist");
/*  127:     */     }
/*  128:     */   }
/*  129:     */   
/*  130:     */   public int createInfoServiceEntry(java.util.Date date, String keyword, String message, String accountId, String ownerId, String imageURL, String articleTitle, int maxContentLength)
/*  131:     */     throws Exception
/*  132:     */   {
/*  133: 146 */     UserService serv = viewService(keyword, accountId);
/*  134: 147 */     int test = 0;
/*  135: 148 */     if ((serv.getAccountId() != null) && (serv.getAccountId().equals(accountId)) && (serv.getKeyword() != null) && (serv.getKeyword().equals(keyword))) {
/*  136: 149 */       test = 1;
/*  137:     */     } else {
/*  138: 151 */       test = 0;
/*  139:     */     }
/*  140: 154 */     if (test == 1)
/*  141:     */     {
/*  142: 157 */       ResultSet rs = null;
/*  143: 158 */       ResultSet rs_label = null;
/*  144: 159 */       ResultSet rs_duplicateTest = null;
/*  145: 160 */       Connection con = null;
/*  146: 161 */       PreparedStatement prepstat = null;
/*  147: 162 */       PreparedStatement prepstat_label = null;
/*  148: 163 */       PreparedStatement prepstat_duplicateTest = null;
/*  149:     */       try
/*  150:     */       {
/*  151: 166 */         con = DConnect.getConnection();
/*  152: 167 */         java.sql.Date sqlDate_date = new java.sql.Date(date.getTime());
/*  153:     */         
/*  154: 169 */         int test_exists = 0;
/*  155:     */         
/*  156:     */ 
/*  157: 172 */         String SQL = "select * from system_sms_queue where publish_date = ? and keyword = ?  and account_id= ? order by msg_id desc";
/*  158:     */         
/*  159: 174 */         prepstat = con.prepareStatement(SQL);
/*  160: 175 */         prepstat.setDate(1, sqlDate_date);
/*  161: 176 */         prepstat.setString(2, keyword);
/*  162: 177 */         prepstat.setString(3, accountId);
/*  163:     */         
/*  164: 179 */         rs = prepstat.executeQuery();
/*  165: 180 */         int lastMsgId = 0;
/*  166: 181 */         if (rs.next())
/*  167:     */         {
/*  168: 182 */           test_exists = 1;
/*  169: 183 */           lastMsgId = rs.getInt("msg_id");
/*  170:     */         }
/*  171: 188 */         String SQL_label = "select * from service_labels where account_id = ? and keyword = ?";
/*  172: 189 */         prepstat_label = con.prepareStatement(SQL_label);
/*  173: 190 */         prepstat_label.setString(1, accountId);
/*  174: 191 */         prepstat_label.setString(2, keyword);
/*  175:     */         
/*  176: 193 */         rs_label = prepstat_label.executeQuery();
/*  177: 194 */         String header = "";
/*  178: 195 */         String footer = "";
/*  179: 196 */         while (rs_label.next())
/*  180:     */         {
/*  181: 197 */           header = rs_label.getString("header");
/*  182: 198 */           footer = rs_label.getString("footer");
/*  183:     */         }
/*  184: 202 */         String final_message = header + message;
/*  185: 203 */         if (new String(final_message + (footer.equals("") ? "" : new StringBuilder().append("\n").append(footer).toString())).length() <= maxContentLength) {
/*  186: 204 */           final_message = final_message + (footer.equals("") ? "" : new StringBuilder().append("\n").append(footer).toString());
/*  187:     */         } else {
/*  188: 206 */           System.out.println("Ignoring footer: \"" + footer + "\" from actual message. Stored message: " + final_message);
/*  189:     */         }
/*  190: 210 */         if (final_message.length() > maxContentLength) {
/*  191: 211 */           return 1;
/*  192:     */         }
/*  193: 215 */         String SQL_duplicateTest = "select * from system_sms_queue where publish_date = ? and keyword = ?  and account_id= ? and msg_hash = ?";
/*  194:     */         
/*  195: 217 */         prepstat_duplicateTest = con.prepareStatement(SQL_duplicateTest);
/*  196: 218 */         prepstat_duplicateTest.setDate(1, sqlDate_date);
/*  197: 219 */         prepstat_duplicateTest.setString(2, keyword);
/*  198: 220 */         prepstat_duplicateTest.setString(3, accountId);
/*  199: 221 */         prepstat_duplicateTest.setInt(4, final_message.hashCode());
/*  200:     */         
/*  201: 223 */         rs_duplicateTest = prepstat_duplicateTest.executeQuery();
/*  202: 224 */         int duplicate_exists = 0;
/*  203: 226 */         if (rs_duplicateTest.next()) {
/*  204: 227 */           duplicate_exists = 1;
/*  205:     */         }
/*  206: 231 */         if (duplicate_exists == 1) {
/*  207: 232 */           return 2;
/*  208:     */         }
/*  209: 235 */         java.util.Date currentDate = new java.util.Date();
/*  210: 236 */         if (test_exists == 1)
/*  211:     */         {
/*  212: 238 */           addInfoService(final_message, keyword, accountId, lastMsgId + 1, sqlDate_date, ownerId, imageURL, articleTitle);
/*  213: 243 */           if (!date.after(currentDate)) {
/*  214: 244 */             updateService(final_message, keyword, accountId);
/*  215:     */           }
/*  216:     */         }
/*  217: 247 */         else if (test_exists == 0)
/*  218:     */         {
/*  219: 249 */           addInfoService(final_message, keyword, accountId, lastMsgId + 1, sqlDate_date, ownerId, imageURL, articleTitle);
/*  220: 252 */           if (!date.after(currentDate)) {
/*  221: 253 */             updateService(final_message, keyword, accountId);
/*  222:     */           }
/*  223:     */         }
/*  224:     */         else
/*  225:     */         {
/*  226: 256 */           throw new Exception("unable to determine insert basis for service item: " + keyword);
/*  227:     */         }
/*  228:     */       }
/*  229:     */       catch (Exception ex)
/*  230:     */       {
/*  231: 260 */         if (con != null) {
/*  232: 261 */           con.close();
/*  233:     */         }
/*  234: 263 */         throw new Exception(ex.getMessage());
/*  235:     */       }
/*  236:     */       finally
/*  237:     */       {
/*  238: 265 */         if (prepstat != null) {
/*  239: 266 */           prepstat.close();
/*  240:     */         }
/*  241: 268 */         if (prepstat_label != null) {
/*  242: 269 */           prepstat_label.close();
/*  243:     */         }
/*  244: 271 */         if (rs != null) {
/*  245: 272 */           rs.close();
/*  246:     */         }
/*  247: 274 */         if (rs_label != null) {
/*  248: 275 */           rs_label.close();
/*  249:     */         }
/*  250: 277 */         if (con != null) {
/*  251: 278 */           con.close();
/*  252:     */         }
/*  253:     */       }
/*  254:     */     }
/*  255:     */     else
/*  256:     */     {
/*  257: 282 */       System.out.println("Cannot create news item. Service does not exist");
/*  258:     */     }
/*  259: 285 */     return 0;
/*  260:     */   }
/*  261:     */   
/*  262:     */   public int createInfoServiceEntryWithTags(java.util.Date date, String keyword, String message, String accountId, String ownerId, String imageURL, String articleTitle, String contentURL, String author, String tags, String messageRef, int maxContentLength)
/*  263:     */     throws Exception
/*  264:     */   {
/*  265: 291 */     UserService serv = viewService(keyword, accountId);
/*  266: 292 */     int test = 0;
/*  267: 293 */     if ((serv.getAccountId() != null) && (serv.getAccountId().equals(accountId)) && (serv.getKeyword() != null) && (serv.getKeyword().equals(keyword))) {
/*  268: 294 */       test = 1;
/*  269:     */     } else {
/*  270: 296 */       test = 0;
/*  271:     */     }
/*  272: 299 */     if (test == 1)
/*  273:     */     {
/*  274: 302 */       ResultSet rs = null;
/*  275: 303 */       ResultSet rs_label = null;
/*  276: 304 */       ResultSet rs_duplicateTest = null;
/*  277: 305 */       Connection con = null;
/*  278: 306 */       PreparedStatement prepstat = null;
/*  279: 307 */       PreparedStatement prepstat_label = null;
/*  280: 308 */       PreparedStatement prepstat_duplicateTest = null;
/*  281:     */       try
/*  282:     */       {
/*  283: 311 */         con = DConnect.getConnection();
/*  284: 312 */         java.sql.Date sqlDate_date = new java.sql.Date(date.getTime());
/*  285:     */         
/*  286: 314 */         int test_exists = 0;
/*  287:     */         
/*  288:     */ 
/*  289: 317 */         String SQL = "select * from system_sms_queue where publish_date = ? and keyword = ?  and account_id= ? order by msg_id desc";
/*  290:     */         
/*  291: 319 */         prepstat = con.prepareStatement(SQL);
/*  292: 320 */         prepstat.setDate(1, sqlDate_date);
/*  293: 321 */         prepstat.setString(2, keyword);
/*  294: 322 */         prepstat.setString(3, accountId);
/*  295:     */         
/*  296: 324 */         rs = prepstat.executeQuery();
/*  297: 325 */         int lastMsgId = 0;
/*  298: 326 */         if (rs.next())
/*  299:     */         {
/*  300: 327 */           test_exists = 1;
/*  301: 328 */           lastMsgId = rs.getInt("msg_id");
/*  302:     */         }
/*  303: 333 */         String SQL_label = "select * from service_labels where account_id = ? and keyword = ?";
/*  304: 334 */         prepstat_label = con.prepareStatement(SQL_label);
/*  305: 335 */         prepstat_label.setString(1, accountId);
/*  306: 336 */         prepstat_label.setString(2, keyword);
/*  307:     */         
/*  308: 338 */         rs_label = prepstat_label.executeQuery();
/*  309: 339 */         String header = "";
/*  310: 340 */         String footer = "";
/*  311: 341 */         while (rs_label.next())
/*  312:     */         {
/*  313: 342 */           header = rs_label.getString("header");
/*  314: 343 */           footer = rs_label.getString("footer");
/*  315:     */         }
/*  316: 347 */         String final_message = header + message;
/*  317: 348 */         if (new String(final_message + (footer.equals("") ? "" : new StringBuilder().append("\n").append(footer).toString())).length() <= maxContentLength) {
/*  318: 349 */           final_message = final_message + (footer.equals("") ? "" : new StringBuilder().append("\n").append(footer).toString());
/*  319:     */         } else {
/*  320: 351 */           System.out.println("Ignoring footer: \"" + footer + "\" from actual message. Stored message: " + final_message);
/*  321:     */         }
/*  322: 355 */         if (final_message.length() > maxContentLength) {
/*  323: 356 */           return 1;
/*  324:     */         }
/*  325: 360 */         String SQL_duplicateTest = "select * from system_sms_queue where publish_date = ? and keyword = ?  and account_id= ? and msg_hash = ?";
/*  326:     */         
/*  327: 362 */         prepstat_duplicateTest = con.prepareStatement(SQL_duplicateTest);
/*  328: 363 */         prepstat_duplicateTest.setDate(1, sqlDate_date);
/*  329: 364 */         prepstat_duplicateTest.setString(2, keyword);
/*  330: 365 */         prepstat_duplicateTest.setString(3, accountId);
/*  331: 366 */         prepstat_duplicateTest.setInt(4, final_message.hashCode());
/*  332:     */         
/*  333: 368 */         rs_duplicateTest = prepstat_duplicateTest.executeQuery();
/*  334: 369 */         int duplicate_exists = 0;
/*  335: 371 */         if (rs_duplicateTest.next()) {
/*  336: 372 */           duplicate_exists = 1;
/*  337:     */         }
/*  338: 376 */         if (duplicate_exists == 1) {
/*  339: 377 */           return 2;
/*  340:     */         }
/*  341: 380 */         java.util.Date currentDate = new java.util.Date();
/*  342: 382 */         if (test_exists == 1)
/*  343:     */         {
/*  344: 385 */           addInfoServiceWithTags(final_message, keyword, accountId, lastMsgId + 1, sqlDate_date, ownerId, imageURL, articleTitle, contentURL, author, tags, messageRef);
/*  345: 390 */           if (!date.after(currentDate)) {
/*  346: 391 */             updateService(final_message, keyword, accountId);
/*  347:     */           }
/*  348:     */         }
/*  349: 394 */         else if (test_exists == 0)
/*  350:     */         {
/*  351: 396 */           addInfoServiceWithTags(final_message, keyword, accountId, lastMsgId + 1, sqlDate_date, ownerId, imageURL, articleTitle, contentURL, author, tags, messageRef);
/*  352: 399 */           if (!date.after(currentDate)) {
/*  353: 400 */             updateService(final_message, keyword, accountId);
/*  354:     */           }
/*  355:     */         }
/*  356:     */         else
/*  357:     */         {
/*  358: 403 */           throw new Exception("unable to determine insert basis for service item: " + keyword);
/*  359:     */         }
/*  360:     */       }
/*  361:     */       catch (Exception ex)
/*  362:     */       {
/*  363: 407 */         if (con != null) {
/*  364: 408 */           con.close();
/*  365:     */         }
/*  366: 410 */         throw new Exception(ex.getMessage());
/*  367:     */       }
/*  368:     */       finally
/*  369:     */       {
/*  370: 412 */         if (prepstat != null) {
/*  371: 413 */           prepstat.close();
/*  372:     */         }
/*  373: 415 */         if (prepstat_label != null) {
/*  374: 416 */           prepstat_label.close();
/*  375:     */         }
/*  376: 418 */         if (rs != null) {
/*  377: 419 */           rs.close();
/*  378:     */         }
/*  379: 421 */         if (rs_label != null) {
/*  380: 422 */           rs_label.close();
/*  381:     */         }
/*  382: 424 */         if (con != null) {
/*  383: 425 */           con.close();
/*  384:     */         }
/*  385:     */       }
/*  386:     */     }
/*  387:     */     else
/*  388:     */     {
/*  389: 429 */       System.out.println("Cannot create news item. Service does not exist");
/*  390:     */     }
/*  391: 432 */     return 0;
/*  392:     */   }
/*  393:     */   
/*  394:     */   public void addInfoService(String message, String keyword, String accountId, int msg_id, java.sql.Date sqlDate, String ownerId, String imageURL, String articleTitle)
/*  395:     */     throws Exception
/*  396:     */   {
/*  397: 511 */     String SQL = "insert into system_sms_queue (publish_date,Publish_time ,keyword,message,account_id,msg_id,msg_hash, owner_id, image_url, article_title) values";
/*  398: 512 */     SQL = SQL + "(?,?, ?, ?, ?, ?,?, ?, ?, ?)";
/*  399: 513 */     Connection con = null;
/*  400: 514 */     PreparedStatement prepstat = null;
/*  401:     */     try
/*  402:     */     {
/*  403: 516 */       con = DConnect.getConnection();
/*  404: 517 */       prepstat = prepstat = con.prepareStatement(SQL);
/*  405:     */       
/*  406: 519 */       prepstat.setDate(1, sqlDate);
/*  407: 520 */       prepstat.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));
/*  408: 521 */       prepstat.setString(3, keyword);
/*  409: 522 */       prepstat.setString(4, message);
/*  410: 523 */       prepstat.setString(5, accountId);
/*  411: 524 */       prepstat.setInt(6, msg_id);
/*  412: 525 */       prepstat.setInt(7, message.hashCode());
/*  413: 526 */       prepstat.setString(8, ownerId);
/*  414: 527 */       prepstat.setString(9, imageURL);
/*  415: 528 */       prepstat.setString(10, articleTitle);
/*  416: 529 */       prepstat.execute();
/*  417:     */     }
/*  418:     */     catch (Exception ex)
/*  419:     */     {
/*  420: 531 */       if (con != null) {
/*  421: 532 */         con.close();
/*  422:     */       }
/*  423: 534 */       throw new Exception(ex.getMessage());
/*  424:     */     }
/*  425: 538 */     if (prepstat != null) {
/*  426: 539 */       prepstat.close();
/*  427:     */     }
/*  428: 541 */     if (con != null) {
/*  429: 542 */       con.close();
/*  430:     */     }
/*  431:     */   }
/*  432:     */   
/*  433:     */   public void addInfoServiceWithTags(String message, String keyword, String accountId, int msg_id, java.sql.Date sqlDate, String ownerId, String imageURL, String articleTitle, String contentURL, String author, String tags, String messageRef)
/*  434:     */     throws Exception
/*  435:     */   {
/*  436: 554 */     String SQL = "insert into system_sms_queue (publish_date,Publish_time ,keyword,message,account_id,msg_id,msg_hash, owner_id, image_url, article_title,tags,msg_ref) values (?,?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?)";
/*  437:     */     
/*  438:     */ 
/*  439: 557 */     Connection con = null;
/*  440: 558 */     PreparedStatement prepstat = null;
/*  441:     */     try
/*  442:     */     {
/*  443: 560 */       con = DConnect.getConnection();
/*  444: 561 */       prepstat = prepstat = con.prepareStatement(SQL);
/*  445:     */       
/*  446: 563 */       prepstat.setDate(1, sqlDate);
/*  447: 564 */       prepstat.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));
/*  448: 565 */       prepstat.setString(3, keyword);
/*  449: 566 */       prepstat.setString(4, message);
/*  450: 567 */       prepstat.setString(5, accountId);
/*  451: 568 */       prepstat.setInt(6, msg_id);
/*  452: 569 */       prepstat.setInt(7, message.hashCode());
/*  453: 570 */       prepstat.setString(8, ownerId);
/*  454: 571 */       prepstat.setString(9, imageURL);
/*  455: 572 */       prepstat.setString(10, articleTitle);
/*  456: 573 */       prepstat.setString(11, tags);
/*  457: 574 */       prepstat.setString(12, messageRef);
/*  458:     */       
/*  459:     */ 
/*  460: 577 */       prepstat.execute();
/*  461:     */     }
/*  462:     */     catch (Exception ex)
/*  463:     */     {
/*  464: 579 */       if (con != null) {
/*  465: 580 */         con.close();
/*  466:     */       }
/*  467: 582 */       throw new Exception(ex.getMessage());
/*  468:     */     }
/*  469: 586 */     if (prepstat != null) {
/*  470: 587 */       prepstat.close();
/*  471:     */     }
/*  472: 589 */     if (con != null) {
/*  473: 590 */       con.close();
/*  474:     */     }
/*  475:     */   }
/*  476:     */   
/*  477:     */   public void updateInfoService(java.util.Date date, String keyword, String message, String accountId, int msg_id, String imageURL, String articleTitle)
/*  478:     */     throws Exception
/*  479:     */   {
/*  480: 599 */     ResultSet rs = null;
/*  481: 600 */     Connection con = null;
/*  482: 601 */     PreparedStatement prepstat = null;
/*  483:     */     try
/*  484:     */     {
/*  485: 603 */       con = DConnect.getConnection();
/*  486: 604 */       java.sql.Date sqlDate_date = new java.sql.Date(date.getTime());
/*  487:     */       
/*  488: 606 */       String SQL = "update system_sms_queue set Publish_time= ?, message =?, msg_hash=?, image_url=?, article_title=? where keyword =? and publish_date=? and  account_id= ? and msg_id = ?";
/*  489:     */       
/*  490: 608 */       prepstat = con.prepareStatement(SQL);
/*  491: 609 */       prepstat.setTimestamp(1, new Timestamp(new java.util.Date().getTime()));
/*  492: 610 */       prepstat.setString(2, message);
/*  493: 611 */       prepstat.setInt(3, message.hashCode());
/*  494: 612 */       prepstat.setString(4, imageURL);
/*  495: 613 */       prepstat.setString(5, articleTitle);
/*  496: 614 */       prepstat.setString(6, keyword);
/*  497: 615 */       prepstat.setDate(7, sqlDate_date);
/*  498: 616 */       prepstat.setString(8, accountId);
/*  499: 617 */       prepstat.setInt(9, msg_id);
/*  500: 618 */       prepstat.execute();
/*  501:     */     }
/*  502:     */     catch (Exception ex)
/*  503:     */     {
/*  504: 620 */       if (con != null) {
/*  505: 621 */         con.close();
/*  506:     */       }
/*  507: 623 */       throw new Exception(ex.getMessage());
/*  508:     */     }
/*  509: 625 */     if (prepstat != null) {
/*  510: 626 */       prepstat.close();
/*  511:     */     }
/*  512: 628 */     if (rs != null) {
/*  513: 629 */       rs.close();
/*  514:     */     }
/*  515: 631 */     if (con != null) {
/*  516: 632 */       con.close();
/*  517:     */     }
/*  518:     */   }
/*  519:     */   
/*  520:     */   public void updateInfoService(java.util.Date date, String keyword, String message, String accountId, int msg_id, String imageURL, String articleTitle, String author, String contentURL)
/*  521:     */     throws Exception
/*  522:     */   {
/*  523: 640 */     ResultSet rs = null;
/*  524: 641 */     Connection con = null;
/*  525: 642 */     PreparedStatement prepstat = null;
/*  526:     */     try
/*  527:     */     {
/*  528: 644 */       con = DConnect.getConnection();
/*  529: 645 */       java.sql.Date sqlDate_date = new java.sql.Date(date.getTime());
/*  530:     */       
/*  531:     */ 
/*  532:     */ 
/*  533:     */ 
/*  534:     */ 
/*  535:     */ 
/*  536: 652 */       String SQL = "update system_sms_queue set Publish_time= ?, message =?, msg_hash=?, image_url=?, article_title=? where keyword =? and publish_date=? and  account_id= ? and msg_id = ?";
/*  537:     */       
/*  538:     */ 
/*  539: 655 */       prepstat = con.prepareStatement(SQL);
/*  540: 656 */       prepstat.setTimestamp(1, new Timestamp(new java.util.Date().getTime()));
/*  541: 657 */       prepstat.setString(2, message);
/*  542: 658 */       prepstat.setInt(3, message.hashCode());
/*  543: 659 */       prepstat.setString(4, imageURL);
/*  544: 660 */       prepstat.setString(5, articleTitle);
/*  545:     */       
/*  546:     */ 
/*  547:     */ 
/*  548:     */ 
/*  549:     */ 
/*  550:     */ 
/*  551:     */ 
/*  552:     */ 
/*  553:     */ 
/*  554:     */ 
/*  555:     */ 
/*  556: 672 */       prepstat.setString(6, keyword);
/*  557: 673 */       prepstat.setDate(7, sqlDate_date);
/*  558: 674 */       prepstat.setString(8, accountId);
/*  559: 675 */       prepstat.setInt(9, msg_id);
/*  560:     */       
/*  561: 677 */       prepstat.execute();
/*  562:     */     }
/*  563:     */     catch (Exception ex)
/*  564:     */     {
/*  565: 679 */       if (con != null) {
/*  566: 680 */         con.close();
/*  567:     */       }
/*  568: 682 */       throw new Exception(ex.getMessage());
/*  569:     */     }
/*  570: 684 */     if (prepstat != null) {
/*  571: 685 */       prepstat.close();
/*  572:     */     }
/*  573: 687 */     if (rs != null) {
/*  574: 688 */       rs.close();
/*  575:     */     }
/*  576: 690 */     if (con != null) {
/*  577: 691 */       con.close();
/*  578:     */     }
/*  579:     */   }
/*  580:     */   
/*  581:     */   public void deleteInfoService(Integer id)
/*  582:     */     throws Exception
/*  583:     */   {
/*  584: 698 */     Connection con = null;
/*  585: 699 */     PreparedStatement prepstat = null;
/*  586:     */     try
/*  587:     */     {
/*  588: 701 */       con = DConnect.getConnection();
/*  589: 702 */       String SQL = "delete from system_sms_queue where id=?";
/*  590: 703 */       prepstat = con.prepareStatement(SQL);
/*  591:     */       
/*  592: 705 */       prepstat.setInt(1, id.intValue());
/*  593: 706 */       prepstat.execute();
/*  594:     */     }
/*  595:     */     catch (Exception ex)
/*  596:     */     {
/*  597: 708 */       if (con != null) {
/*  598: 709 */         con.close();
/*  599:     */       }
/*  600: 711 */       throw new Exception(ex.getMessage());
/*  601:     */     }
/*  602: 713 */     if (prepstat != null) {
/*  603: 714 */       prepstat.close();
/*  604:     */     }
/*  605: 716 */     if (con != null) {
/*  606: 717 */       con.close();
/*  607:     */     }
/*  608:     */   }
/*  609:     */   
/*  610:     */   public void deleteInfoService(String keyword, String accountId)
/*  611:     */     throws Exception
/*  612:     */   {
/*  613: 724 */     Connection con = null;
/*  614: 725 */     PreparedStatement prepstat = null;
/*  615:     */     try
/*  616:     */     {
/*  617: 727 */       con = DConnect.getConnection();
/*  618: 728 */       String SQL = "delete from system_sms_queue where keyword=? and account_id=?";
/*  619: 729 */       prepstat = con.prepareStatement(SQL);
/*  620:     */       
/*  621: 731 */       prepstat.setString(1, keyword);
/*  622: 732 */       prepstat.setString(2, accountId);
/*  623: 733 */       prepstat.execute();
/*  624:     */     }
/*  625:     */     catch (Exception ex)
/*  626:     */     {
/*  627: 735 */       if (con != null) {
/*  628: 736 */         con.close();
/*  629:     */       }
/*  630: 738 */       throw new Exception(ex.getMessage());
/*  631:     */     }
/*  632: 740 */     if (prepstat != null) {
/*  633: 741 */       prepstat.close();
/*  634:     */     }
/*  635: 743 */     if (con != null) {
/*  636: 744 */       con.close();
/*  637:     */     }
/*  638:     */   }
/*  639:     */   
/*  640:     */   public void deleteInfoService(java.util.Date date, String keyword, String accountId, int msg_id)
/*  641:     */     throws Exception
/*  642:     */   {
/*  643: 753 */     Connection con = null;
/*  644: 754 */     PreparedStatement prepstat = null;
/*  645:     */     try
/*  646:     */     {
/*  647: 756 */       con = DConnect.getConnection();
/*  648: 757 */       java.sql.Date sqlDate_date = new java.sql.Date(date.getTime());
/*  649:     */       
/*  650: 759 */       String SQL = "delete from system_sms_queue where  keyword =? and publish_date=? and  account_id= ? and msg_id = ?";
/*  651: 760 */       prepstat = con.prepareStatement(SQL);
/*  652: 761 */       prepstat.setString(1, keyword);
/*  653: 762 */       prepstat.setDate(2, sqlDate_date);
/*  654: 763 */       prepstat.setString(3, accountId);
/*  655: 764 */       prepstat.setInt(4, msg_id);
/*  656: 765 */       prepstat.execute();
/*  657:     */     }
/*  658:     */     catch (Exception ex)
/*  659:     */     {
/*  660: 768 */       if (con != null) {
/*  661: 769 */         con.close();
/*  662:     */       }
/*  663: 771 */       throw new Exception(ex.getMessage());
/*  664:     */     }
/*  665: 773 */     if (prepstat != null) {
/*  666: 774 */       prepstat.close();
/*  667:     */     }
/*  668: 776 */     if (con != null) {
/*  669: 777 */       con.close();
/*  670:     */     }
/*  671:     */   }
/*  672:     */   
/*  673:     */   public static InfoService retrieveOutboundMessage(UserService service, java.util.Date date, String messageRef)
/*  674:     */     throws Exception
/*  675:     */   {
/*  676: 786 */     InfoService outboundMessage = new InfoService();
/*  677:     */     
/*  678:     */ 
/*  679: 789 */     ResultSet rs = null;
/*  680: 790 */     Connection con = null;
/*  681: 791 */     PreparedStatement prepstat = null;
/*  682: 792 */     java.sql.Date sqlDate = new java.sql.Date(date.getTime());
/*  683:     */     
/*  684: 794 */     System.out.println(new java.util.Date() + ":@com.rancard.mobility.infoserver.InfoServiceDB..");
/*  685: 795 */     System.out.println(new java.util.Date() + ": retrieving outbound message from message queue...");
/*  686:     */     try
/*  687:     */     {
/*  688: 800 */       if ((messageRef != null) && (!messageRef.equals("")))
/*  689:     */       {
/*  690: 802 */         String SQL = "select ssq.*, sl.header, sl.footer from system_sms_queue ssq left outer join service_labels sl on ssq.account_id = sl.account_id and ssq.keyword = sl.keyword where ssq.keyword=? and ssq.account_id = ? and publish_date =? and msg_ref=? order by publish_time desc, msg_id desc limit 10";
/*  691:     */         
/*  692:     */ 
/*  693:     */ 
/*  694:     */ 
/*  695: 807 */         con = DConnect.getConnection();
/*  696: 808 */         prepstat = con.prepareStatement(SQL);
/*  697: 809 */         prepstat.setString(1, service.getKeyword());
/*  698: 810 */         prepstat.setString(2, service.getAccountId());
/*  699: 811 */         prepstat.setDate(3, sqlDate);
/*  700: 812 */         prepstat.setString(4, messageRef);
/*  701:     */       }
/*  702:     */       else
/*  703:     */       {
/*  704: 815 */         String SQL = "select ssq.*, sl.header, sl.footer from system_sms_queue ssq left outer join service_labels sl on ssq.account_id = sl.account_id and ssq.keyword = sl.keyword where ssq.keyword=? and ssq.account_id = ? and publish_date =? order by publish_time desc, msg_id desc limit 10";
/*  705:     */         
/*  706:     */ 
/*  707:     */ 
/*  708:     */ 
/*  709: 820 */         con = DConnect.getConnection();
/*  710: 821 */         prepstat = con.prepareStatement(SQL);
/*  711: 822 */         prepstat.setString(1, service.getKeyword());
/*  712: 823 */         prepstat.setString(2, service.getAccountId());
/*  713: 824 */         prepstat.setDate(3, sqlDate);
/*  714:     */       }
/*  715: 827 */       rs = prepstat.executeQuery();
/*  716: 829 */       if (rs.next())
/*  717:     */       {
/*  718: 830 */         SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH.mm.ss");
/*  719: 831 */         String publishTime = df.format(new java.util.Date(rs.getTimestamp("Publish_time").getTime()));
/*  720:     */         
/*  721: 833 */         outboundMessage.setPublishTime(publishTime);
/*  722: 834 */         outboundMessage.setKeyword(rs.getString("keyword"));
/*  723:     */         
/*  724: 836 */         Clob clob = rs.getClob("message");
/*  725: 837 */         String tmpMsg = "";
/*  726: 838 */         String temp = "";
/*  727:     */         
/*  728: 840 */         BufferedReader br = new BufferedReader(clob.getCharacterStream());
/*  729: 841 */         while ((temp = br.readLine()) != null) {
/*  730: 842 */           tmpMsg = tmpMsg + temp;
/*  731:     */         }
/*  732: 845 */         tmpMsg = URLUTF8Encoder.removeMalformedChars(tmpMsg);
/*  733: 846 */         System.out.println("Text item from SYSTEM_SMS_QUEUE (WORKING LOCALLY): " + tmpMsg);
/*  734: 847 */         outboundMessage.setMessage(tmpMsg);
/*  735:     */         
/*  736: 849 */         outboundMessage.setAccountId(rs.getString("account_id"));
/*  737: 850 */         outboundMessage.setMsgId(rs.getInt("msg_id"));
/*  738: 851 */         outboundMessage.setPublishDate(new SimpleDateFormat("dd-MMM-yyyy").format(rs.getDate("publish_date")));
/*  739: 852 */         outboundMessage.setOwnerId(rs.getString("owner_id"));
/*  740: 853 */         outboundMessage.setImageURL(rs.getString("image_url"));
/*  741: 854 */         outboundMessage.setArticleTitle(rs.getString("article_title"));
/*  742: 855 */         outboundMessage.setTags(rs.getString("tags"));
/*  743: 856 */         outboundMessage.setMessageRef(rs.getString("msg_ref"));
/*  744:     */         
/*  745: 858 */         String header = rs.getString("header");
/*  746: 859 */         String footer = rs.getString("footer");
/*  747: 860 */         outboundMessage.setHeader(header == null ? "" : header);
/*  748: 861 */         outboundMessage.setFooter(footer == null ? "" : footer);
/*  749:     */         
/*  750: 863 */         outboundMessage.setDefaultMessage(service.getDefaultMessage());
/*  751: 864 */         outboundMessage.setServiceName(service.getServiceName());
/*  752: 865 */         outboundMessage.setServiceType(service.getServiceType());
/*  753: 866 */         outboundMessage.setPricing(service.getPricing());
/*  754: 867 */         outboundMessage.setLastUpdated(service.getLastUpdated());
/*  755: 868 */         outboundMessage.setAllowedShortcodes(service.getAllowedShortcodes());
/*  756: 869 */         outboundMessage.setAllowedSiteTypes(service.getAllowedSiteTypes());
/*  757:     */       }
/*  758:     */     }
/*  759:     */     catch (Exception ex)
/*  760:     */     {
/*  761: 872 */       if (con != null) {
/*  762: 873 */         con.close();
/*  763:     */       }
/*  764: 876 */       System.out.println(new java.util.Date() + ": error retrieving info for " + service.getKeyword() + "/" + date.toString() + "/" + service.getAccountId() + " combination: " + ex.getMessage());
/*  765:     */       
/*  766:     */ 
/*  767: 879 */       throw new Exception(ex.getMessage());
/*  768:     */     }
/*  769:     */     finally
/*  770:     */     {
/*  771: 881 */       if (prepstat != null) {
/*  772: 882 */         prepstat.close();
/*  773:     */       }
/*  774: 884 */       if (rs != null) {
/*  775: 885 */         rs.close();
/*  776:     */       }
/*  777: 887 */       if (con != null) {
/*  778: 888 */         con.close();
/*  779:     */       }
/*  780:     */     }
/*  781: 891 */     return outboundMessage;
/*  782:     */   }
/*  783:     */   
/*  784:     */   public InfoService viewInfoService(String keyword, String accountId, java.util.Date date)
/*  785:     */     throws Exception
/*  786:     */   {
/*  787: 895 */     InfoService system_sms_queue = new InfoService();
/*  788: 896 */     ArrayList<InfoService> recordList = new ArrayList();
/*  789:     */     
/*  790:     */ 
/*  791: 899 */     ResultSet rs = null;
/*  792: 900 */     Connection con = null;
/*  793: 901 */     PreparedStatement prepstat = null;
/*  794: 902 */     java.sql.Date sqlDate = new java.sql.Date(date.getTime());
/*  795:     */     
/*  796: 904 */     System.out.println(new java.util.Date() + ":@com.rancard.mobility.infoserver.InfoServiceDB..");
/*  797: 905 */     System.out.println(new java.util.Date() + ":viewing info service...");
/*  798:     */     try
/*  799:     */     {
/*  800: 909 */       UserService service = ServiceManager.viewService(keyword, accountId);
/*  801: 910 */       system_sms_queue.setAccountId(service.getAccountId());
/*  802: 911 */       system_sms_queue.setKeyword(service.getKeyword());
/*  803: 912 */       system_sms_queue.setDefaultMessage(service.getDefaultMessage());
/*  804: 913 */       system_sms_queue.setServiceName(service.getServiceName());
/*  805: 914 */       system_sms_queue.setServiceType(service.getServiceType());
/*  806: 915 */       system_sms_queue.setPricing(service.getPricing());
/*  807: 916 */       system_sms_queue.setLastUpdated(service.getLastUpdated());
/*  808: 917 */       system_sms_queue.setAllowedShortcodes(service.getAllowedShortcodes());
/*  809: 918 */       system_sms_queue.setAllowedSiteTypes(service.getAllowedSiteTypes());
/*  810:     */       
/*  811: 920 */       String SQL = "select ssq.*, sl.header, sl.footer from system_sms_queue ssq left outer join service_labels sl on ssq.account_id = sl.account_id and ssq.keyword = sl.keyword where ssq.keyword=? and ssq.account_id = ? and publish_date =? order by publish_time desc, msg_id limit 10";
/*  812:     */       
/*  813:     */ 
/*  814:     */ 
/*  815:     */ 
/*  816: 925 */       con = DConnect.getConnection();
/*  817: 926 */       prepstat = con.prepareStatement(SQL);
/*  818: 927 */       prepstat.setString(1, keyword);
/*  819: 928 */       prepstat.setString(2, accountId);
/*  820: 929 */       prepstat.setDate(3, sqlDate);
/*  821: 930 */       rs = prepstat.executeQuery();
/*  822: 932 */       while (rs.next())
/*  823:     */       {
/*  824: 933 */         SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH.mm.ss");
/*  825: 934 */         String publishTime = df.format(new java.util.Date(rs.getTimestamp("Publish_time").getTime()));
/*  826: 935 */         system_sms_queue.setPublishTime(publishTime);
/*  827: 936 */         system_sms_queue.setKeyword(rs.getString("keyword"));
/*  828:     */         
/*  829: 938 */         Clob clob = rs.getClob("message");
/*  830: 939 */         String tmpMsg = "";
/*  831: 940 */         String temp = "";
/*  832:     */         
/*  833: 942 */         BufferedReader br = new BufferedReader(clob.getCharacterStream());
/*  834: 943 */         while ((temp = br.readLine()) != null) {
/*  835: 944 */           tmpMsg = tmpMsg + temp;
/*  836:     */         }
/*  837: 947 */         tmpMsg = URLUTF8Encoder.removeMalformedChars(tmpMsg);
/*  838:     */         
/*  839: 949 */         System.out.println("Text item from SYSTEM_SMS_QUEUE (WORKING LOCALLY): " + tmpMsg);
/*  840: 950 */         system_sms_queue.setMessage(tmpMsg);
/*  841:     */         
/*  842: 952 */         system_sms_queue.setAccountId(rs.getString("account_id"));
/*  843: 953 */         system_sms_queue.setMsgId(rs.getInt("msg_id"));
/*  844: 954 */         system_sms_queue.setPublishDate(new SimpleDateFormat("dd-MMM-yyyy").format(rs.getDate("publish_date")));
/*  845: 955 */         system_sms_queue.setOwnerId(rs.getString("owner_id"));
/*  846: 956 */         system_sms_queue.setImageURL(rs.getString("image_url"));
/*  847: 957 */         system_sms_queue.setArticleTitle(rs.getString("article_title"));
/*  848:     */         
/*  849: 959 */         String header = rs.getString("header");
/*  850: 960 */         String footer = rs.getString("footer");
/*  851: 961 */         system_sms_queue.setHeader(header == null ? "" : header);
/*  852: 962 */         system_sms_queue.setFooter(footer == null ? "" : footer);
/*  853:     */         
/*  854: 964 */         system_sms_queue.setDefaultMessage(service.getDefaultMessage());
/*  855: 965 */         system_sms_queue.setServiceName(service.getServiceName());
/*  856: 966 */         system_sms_queue.setServiceType(service.getServiceType());
/*  857: 967 */         system_sms_queue.setPricing(service.getPricing());
/*  858: 968 */         system_sms_queue.setLastUpdated(service.getLastUpdated());
/*  859: 969 */         system_sms_queue.setAllowedShortcodes(service.getAllowedShortcodes());
/*  860: 970 */         system_sms_queue.setAllowedSiteTypes(service.getAllowedSiteTypes());
/*  861:     */         
/*  862: 972 */         recordList.add(system_sms_queue);
/*  863: 973 */         system_sms_queue = new InfoService();
/*  864:     */       }
/*  865: 977 */       int recordCount = recordList.size();
/*  866: 978 */       System.out.println(new java.util.Date() + ":" + recordCount + " records retrieved from db for " + keyword + "/" + date.toString() + "/" + accountId + " combination");
/*  867: 979 */       int msg_id = 0;
/*  868: 980 */       int msg_base = 0;
/*  869: 981 */       if (recordCount > 0)
/*  870:     */       {
/*  871: 982 */         msg_base = ((InfoService)recordList.get(0)).getMsgId();
/*  872: 983 */         msg_id = selectMessage(recordCount, msg_base);
/*  873: 984 */         System.out.println(new java.util.Date() + ":" + msg_id + " selected as random msg_id");
/*  874:     */       }
/*  875: 987 */       if (!recordList.isEmpty()) {
/*  876: 988 */         system_sms_queue = (InfoService)recordList.get(msg_id - msg_base);
/*  877:     */       }
/*  878:     */     }
/*  879:     */     catch (Exception ex)
/*  880:     */     {
/*  881: 991 */       if (con != null) {
/*  882: 992 */         con.close();
/*  883:     */       }
/*  884: 995 */       System.out.println(new java.util.Date() + ": error retrieving info for " + keyword + "/" + date.toString() + "/" + accountId + " combination: " + ex.getMessage());
/*  885:     */       
/*  886: 997 */       throw new Exception(ex.getMessage());
/*  887:     */     }
/*  888:     */     finally
/*  889:     */     {
/*  890: 999 */       if (prepstat != null) {
/*  891:1000 */         prepstat.close();
/*  892:     */       }
/*  893:1002 */       if (rs != null) {
/*  894:1003 */         rs.close();
/*  895:     */       }
/*  896:1005 */       if (con != null) {
/*  897:1006 */         con.close();
/*  898:     */       }
/*  899:     */     }
/*  900:1009 */     return system_sms_queue;
/*  901:     */   }
/*  902:     */   
/*  903:     */   public static InfoService viewInfoService(String keyword, String accountId, java.util.Date date, int msg_id)
/*  904:     */     throws Exception
/*  905:     */   {
/*  906:1013 */     InfoService system_sms_queue = new InfoService();
/*  907:     */     
/*  908:     */ 
/*  909:1016 */     ResultSet rs = null;
/*  910:1017 */     Connection con = null;
/*  911:1018 */     PreparedStatement prepstat = null;
/*  912:1019 */     java.sql.Date sqlDate = new java.sql.Date(date.getTime());
/*  913:     */     try
/*  914:     */     {
/*  915:1022 */       UserService service = ServiceManager.viewService(keyword, accountId);
/*  916:     */       
/*  917:1024 */       system_sms_queue.setAccountId(service.getAccountId());
/*  918:1025 */       system_sms_queue.setKeyword(service.getKeyword());
/*  919:1026 */       system_sms_queue.setDefaultMessage(service.getDefaultMessage());
/*  920:1027 */       system_sms_queue.setServiceName(service.getServiceName());
/*  921:1028 */       system_sms_queue.setServiceType(service.getServiceType());
/*  922:1029 */       system_sms_queue.setPricing(service.getPricing());
/*  923:1030 */       system_sms_queue.setLastUpdated(service.getLastUpdated());
/*  924:1031 */       system_sms_queue.setAllowedShortcodes(service.getAllowedShortcodes());
/*  925:1032 */       system_sms_queue.setAllowedSiteTypes(service.getAllowedSiteTypes());
/*  926:     */       
/*  927:1034 */       con = DConnect.getConnection();
/*  928:     */       
/*  929:1036 */       String SQL = "select ssq.*, sl.header, sl.footer from system_sms_queue ssq left outer join service_labels sl on ssq.account_id = sl.account_id and ssq.keyword = sl.keyword where ssq.keyword=? and ssq.account_id = ? and publish_date =? and msg_id = ?";
/*  930:     */       
/*  931:     */ 
/*  932:     */ 
/*  933:1040 */       prepstat = con.prepareStatement(SQL);
/*  934:     */       
/*  935:1042 */       prepstat.setString(1, keyword);
/*  936:1043 */       prepstat.setString(2, accountId);
/*  937:1044 */       prepstat.setDate(3, sqlDate);
/*  938:1045 */       prepstat.setInt(4, msg_id);
/*  939:1046 */       rs = prepstat.executeQuery();
/*  940:1048 */       while (rs.next())
/*  941:     */       {
/*  942:1049 */         SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH.mm.ss");
/*  943:1050 */         String publishTime = df.format(new java.util.Date(rs.getTimestamp("Publish_time").getTime()));
/*  944:1051 */         system_sms_queue.setPublishTime(publishTime);
/*  945:1052 */         system_sms_queue.setKeyword(rs.getString("keyword"));
/*  946:     */         
/*  947:1054 */         Clob clob = rs.getClob("message");
/*  948:1055 */         String tmpMsg = "";
/*  949:1056 */         String temp = "";
/*  950:     */         
/*  951:1058 */         BufferedReader br = new BufferedReader(clob.getCharacterStream());
/*  952:1059 */         while ((temp = br.readLine()) != null) {
/*  953:1060 */           tmpMsg = tmpMsg + temp;
/*  954:     */         }
/*  955:1063 */         tmpMsg = URLUTF8Encoder.removeMalformedChars(tmpMsg);
/*  956:1064 */         System.out.println("Text item from SYSTEM_SMS_QUEUE (WORKING LOCALLY): " + tmpMsg);
/*  957:1065 */         system_sms_queue.setMessage(tmpMsg);
/*  958:     */         
/*  959:1067 */         system_sms_queue.setAccountId(rs.getString("account_id"));
/*  960:1068 */         system_sms_queue.setMsgId(rs.getInt("msg_id"));
/*  961:1069 */         system_sms_queue.setPublishDate(new SimpleDateFormat("dd-MMM-yyyy").format(rs.getDate("publish_date")));
/*  962:1070 */         system_sms_queue.setOwnerId(rs.getString("owner_id"));
/*  963:1071 */         system_sms_queue.setImageURL(rs.getString("image_url"));
/*  964:1072 */         system_sms_queue.setArticleTitle(rs.getString("article_title"));
/*  965:     */         
/*  966:1074 */         String header = rs.getString("header");
/*  967:1075 */         String footer = rs.getString("footer");
/*  968:1076 */         system_sms_queue.setHeader(header == null ? "" : header);
/*  969:1077 */         system_sms_queue.setFooter(footer == null ? "" : footer);
/*  970:     */       }
/*  971:     */     }
/*  972:     */     catch (Exception ex)
/*  973:     */     {
/*  974:1081 */       if (con != null) {
/*  975:1082 */         con.close();
/*  976:     */       }
/*  977:1084 */       throw new Exception(ex.getMessage());
/*  978:     */     }
/*  979:1086 */     if (prepstat != null) {
/*  980:1087 */       prepstat.close();
/*  981:     */     }
/*  982:1089 */     if (rs != null) {
/*  983:1090 */       rs.close();
/*  984:     */     }
/*  985:1092 */     if (con != null) {
/*  986:1093 */       con.close();
/*  987:     */     }
/*  988:1096 */     return system_sms_queue;
/*  989:     */   }
/*  990:     */   
/*  991:     */   private int selectMessage(int n, int startRange)
/*  992:     */   {
/*  993:1120 */     Random r = new Random();
/*  994:1121 */     r.setSeed(new java.util.Date().getTime());
/*  995:1122 */     int ret = r.nextInt(n > 0 ? n : 1) + startRange;
/*  996:1123 */     return ret;
/*  997:     */   }
/*  998:     */   
/*  999:     */   public ArrayList<InfoService> viewInfoServices(String keyword, String accountId, java.util.Date date)
/* 1000:     */     throws Exception
/* 1001:     */   {
/* 1002:1151 */     InfoService system_sms_queue = null;
/* 1003:     */     
/* 1004:1153 */     ArrayList<InfoService> list = new ArrayList();
/* 1005:     */     
/* 1006:1155 */     String counterSQL = "select ssq.*, sl.header, sl.footer from system_sms_queue ssq left outer join service_labels sl on ssq.account_id = sl.account_id and ssq.keyword = sl.keyword where ssq.keyword=? and ssq.account_id = ? and publish_date =? order by msg_id desc";
/* 1007:     */     
/* 1008:     */ 
/* 1009:     */ 
/* 1010:1159 */     Connection con = DConnect.getConnection();
/* 1011:1160 */     PreparedStatement counterStat = con.prepareStatement(counterSQL);
/* 1012:1161 */     counterStat.setString(1, keyword);
/* 1013:1162 */     counterStat.setString(2, accountId);
/* 1014:1163 */     counterStat.setDate(3, new java.sql.Date(date.getTime()));
/* 1015:1164 */     ResultSet rs = counterStat.executeQuery();
/* 1016:1166 */     while (rs.next())
/* 1017:     */     {
/* 1018:1167 */       system_sms_queue = new InfoService();
/* 1019:1168 */       SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH.mm.ss");
/* 1020:1169 */       String publishTime = df.format(new java.util.Date(rs.getTimestamp("Publish_time").getTime()));
/* 1021:1170 */       system_sms_queue.setPublishTime(publishTime);
/* 1022:1171 */       system_sms_queue.setKeyword(rs.getString("keyword"));
/* 1023:     */       
/* 1024:1173 */       Clob clob = rs.getClob("message");
/* 1025:1174 */       String tmpMsg = "";
/* 1026:1175 */       String temp = "";
/* 1027:     */       
/* 1028:1177 */       BufferedReader br = new BufferedReader(clob.getCharacterStream());
/* 1029:1178 */       while ((temp = br.readLine()) != null) {
/* 1030:1179 */         tmpMsg = tmpMsg + temp;
/* 1031:     */       }
/* 1032:1182 */       tmpMsg = URLUTF8Encoder.removeMalformedChars(tmpMsg);
/* 1033:1183 */       System.out.println("Text item from SYSTEM_SMS_QUEUE (WORKING LOCALLY): " + tmpMsg);
/* 1034:1184 */       system_sms_queue.setMessage(tmpMsg);
/* 1035:     */       
/* 1036:1186 */       system_sms_queue.setAccountId(rs.getString("account_id"));
/* 1037:1187 */       system_sms_queue.setMsgId(rs.getInt("msg_id"));
/* 1038:1188 */       system_sms_queue.setOwnerId(rs.getString("owner_id"));
/* 1039:1189 */       system_sms_queue.setPublishDate(new SimpleDateFormat("dd-MMM-yyyy").format(rs.getDate("publish_date")));
/* 1040:1190 */       system_sms_queue.setImageURL(rs.getString("image_url"));
/* 1041:1191 */       system_sms_queue.setArticleTitle(rs.getString("article_title"));
/* 1042:     */       
/* 1043:1193 */       String header = rs.getString("header");
/* 1044:1194 */       String footer = rs.getString("footer");
/* 1045:1195 */       system_sms_queue.setHeader(header == null ? "" : header);
/* 1046:1196 */       system_sms_queue.setFooter(footer == null ? "" : footer);
/* 1047:     */       
/* 1048:     */ 
/* 1049:1199 */       list.add(system_sms_queue);
/* 1050:     */     }
/* 1051:1202 */     return list;
/* 1052:     */   }
/* 1053:     */   
/* 1054:     */   public HashMap<String, String> viewHeaderFooter(String keyword, String accountId)
/* 1055:     */     throws Exception
/* 1056:     */   {
/* 1057:1206 */     HashMap<String, String> hf_map = new HashMap();
/* 1058:     */     
/* 1059:1208 */     ResultSet rs = null;
/* 1060:1209 */     Connection con = null;
/* 1061:1210 */     PreparedStatement prepstat = null;
/* 1062:     */     
/* 1063:1212 */     String header = "";
/* 1064:1213 */     String footer = "";
/* 1065:     */     try
/* 1066:     */     {
/* 1067:1216 */       con = DConnect.getConnection();
/* 1068:1217 */       String SQL = "select * from service_labels  where keyword = ?  and account_id= ?";
/* 1069:     */       
/* 1070:1219 */       prepstat = con.prepareStatement(SQL);
/* 1071:1220 */       prepstat.setString(1, keyword);
/* 1072:1221 */       prepstat.setString(2, accountId);
/* 1073:     */       
/* 1074:1223 */       rs = prepstat.executeQuery();
/* 1075:1225 */       while (rs.next())
/* 1076:     */       {
/* 1077:1226 */         header = rs.getString("header");
/* 1078:1227 */         footer = rs.getString("footer");
/* 1079:     */       }
/* 1080:     */     }
/* 1081:     */     catch (Exception ex)
/* 1082:     */     {
/* 1083:1231 */       if (con != null) {
/* 1084:1232 */         con.close();
/* 1085:     */       }
/* 1086:1234 */       throw new Exception(ex.getMessage());
/* 1087:     */     }
/* 1088:     */     finally
/* 1089:     */     {
/* 1090:1236 */       if (prepstat != null) {
/* 1091:1237 */         prepstat.close();
/* 1092:     */       }
/* 1093:1239 */       if (rs != null) {
/* 1094:1240 */         rs.close();
/* 1095:     */       }
/* 1096:1242 */       if (con != null) {
/* 1097:1243 */         con.close();
/* 1098:     */       }
/* 1099:     */     }
/* 1100:1247 */     hf_map.put("header", header == null ? "" : header);
/* 1101:1248 */     hf_map.put("footer", footer == null ? "" : footer);
/* 1102:     */     
/* 1103:1250 */     return hf_map;
/* 1104:     */   }
/* 1105:     */   
/* 1106:     */   public ArrayList<InfoService> viewInfoServices(String keyword, String accountId, java.util.Date date, String ownerId)
/* 1107:     */     throws Exception
/* 1108:     */   {
/* 1109:1254 */     InfoService system_sms_queue = null;
/* 1110:     */     
/* 1111:1256 */     ArrayList<InfoService> list = new ArrayList();
/* 1112:     */     
/* 1113:1258 */     String counterSQL = "select ssq.*, sl.header, sl.footer from system_sms_queue ssq left outer join service_labels sl on ssq.account_id = sl.account_id and ssq.keyword = sl.keyword where ssq.keyword=? and ssq.account_id = ? and publish_date =? and owner_id =? order by msg_id desc";
/* 1114:     */     
/* 1115:     */ 
/* 1116:     */ 
/* 1117:1262 */     Connection con = DConnect.getConnection();
/* 1118:1263 */     PreparedStatement counterStat = con.prepareStatement(counterSQL);
/* 1119:1264 */     counterStat.setString(1, keyword);
/* 1120:1265 */     counterStat.setString(2, accountId);
/* 1121:1266 */     counterStat.setDate(3, new java.sql.Date(date.getTime()));
/* 1122:1267 */     counterStat.setString(4, ownerId);
/* 1123:     */     
/* 1124:1269 */     ResultSet rs = counterStat.executeQuery();
/* 1125:1271 */     while (rs.next())
/* 1126:     */     {
/* 1127:1272 */       system_sms_queue = new InfoService();
/* 1128:1273 */       SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH.mm.ss");
/* 1129:1274 */       String publishTime = df.format(new java.util.Date(rs.getTimestamp("Publish_time").getTime()));
/* 1130:1275 */       system_sms_queue.setPublishTime(publishTime);
/* 1131:1276 */       system_sms_queue.setKeyword(rs.getString("keyword"));
/* 1132:     */       
/* 1133:1278 */       Clob clob = rs.getClob("message");
/* 1134:1279 */       String tmpMsg = "";
/* 1135:1280 */       String temp = "";
/* 1136:     */       
/* 1137:1282 */       BufferedReader br = new BufferedReader(clob.getCharacterStream());
/* 1138:1283 */       while ((temp = br.readLine()) != null) {
/* 1139:1284 */         tmpMsg = tmpMsg + temp;
/* 1140:     */       }
/* 1141:1287 */       tmpMsg = URLUTF8Encoder.removeMalformedChars(tmpMsg);
/* 1142:1288 */       System.out.println("Text item from SYSTEM_SMS_QUEUE (WORKING LOCALLY): " + tmpMsg);
/* 1143:1289 */       system_sms_queue.setMessage(tmpMsg);
/* 1144:     */       
/* 1145:1291 */       system_sms_queue.setAccountId(rs.getString("account_id"));
/* 1146:1292 */       system_sms_queue.setMsgId(rs.getInt("msg_id"));
/* 1147:1293 */       system_sms_queue.setOwnerId(rs.getString("owner_id"));
/* 1148:1294 */       system_sms_queue.setPublishDate(new SimpleDateFormat("dd-MMM-yyyy").format(rs.getDate("publish_date")));
/* 1149:1295 */       system_sms_queue.setImageURL(rs.getString("image_url"));
/* 1150:1296 */       system_sms_queue.setArticleTitle(rs.getString("article_title"));
/* 1151:     */       
/* 1152:1298 */       String header = rs.getString("header");
/* 1153:1299 */       String footer = rs.getString("footer");
/* 1154:1300 */       system_sms_queue.setHeader(header == null ? "" : header);
/* 1155:1301 */       system_sms_queue.setFooter(footer == null ? "" : footer);
/* 1156:     */       
/* 1157:     */ 
/* 1158:1304 */       list.add(system_sms_queue);
/* 1159:     */     }
/* 1160:1307 */     return list;
/* 1161:     */   }
/* 1162:     */   
/* 1163:     */   public void updatebackupsystem_sms_queue(java.util.Date date, String service, String message)
/* 1164:     */     throws Exception
/* 1165:     */   {
/* 1166:1313 */     ResultSet rs = null;
/* 1167:1314 */     Connection con = null;
/* 1168:1315 */     PreparedStatement prepstat = null;
/* 1169:     */     try
/* 1170:     */     {
/* 1171:1319 */       java.sql.Date sqlDate_date = new java.sql.Date(date.getTime());
/* 1172:     */       
/* 1173:1321 */       con = DConnect.getConnection();
/* 1174:     */       
/* 1175:1323 */       String SQL = "Update  backup_system_sms_queue set message= ? , last_update=? where keyword=? ";
/* 1176:     */       
/* 1177:1325 */       prepstat = con.prepareStatement(SQL);
/* 1178:     */       
/* 1179:     */ 
/* 1180:1328 */       prepstat.setString(1, message);
/* 1181:1329 */       prepstat.setDate(2, sqlDate_date);
/* 1182:1330 */       prepstat.setString(3, service);
/* 1183:     */       
/* 1184:1332 */       prepstat.execute();
/* 1185:     */     }
/* 1186:     */     catch (Exception ex)
/* 1187:     */     {
/* 1188:1334 */       if (con != null) {
/* 1189:1335 */         con.close();
/* 1190:     */       }
/* 1191:1337 */       throw new Exception(ex.getMessage());
/* 1192:     */     }
/* 1193:1339 */     if (con != null) {
/* 1194:1340 */       con.close();
/* 1195:     */     }
/* 1196:     */   }
/* 1197:     */   
/* 1198:     */   public InfoService viewbackupsystem_sms_queue(String keyword)
/* 1199:     */     throws Exception
/* 1200:     */   {
/* 1201:1346 */     InfoService system_sms_queue = new InfoService();
/* 1202:     */     
/* 1203:     */ 
/* 1204:1349 */     ResultSet rs = null;
/* 1205:1350 */     Connection con = null;
/* 1206:1351 */     PreparedStatement prepstat = null;
/* 1207:     */     try
/* 1208:     */     {
/* 1209:1354 */       con = DConnect.getConnection();
/* 1210:1355 */       java.sql.Date tempdate = null;
/* 1211:     */       
/* 1212:1357 */       String SQL = "select * from  backup_system_sms_queue where keyword=?";
/* 1213:     */       
/* 1214:1359 */       prepstat = con.prepareStatement(SQL);
/* 1215:     */       
/* 1216:1361 */       prepstat.setString(1, keyword);
/* 1217:1362 */       rs = prepstat.executeQuery();
/* 1218:1364 */       while (rs.next()) {
/* 1219:1367 */         system_sms_queue.setMessage(rs.getString("message"));
/* 1220:     */       }
/* 1221:1371 */       prepstat.close();
/* 1222:1372 */       prepstat = null;
/* 1223:1373 */       con.close();
/* 1224:1374 */       con = null;
/* 1225:     */     }
/* 1226:     */     catch (Exception ex)
/* 1227:     */     {
/* 1228:1376 */       ex.printStackTrace();
/* 1229:1377 */       if (con != null)
/* 1230:     */       {
/* 1231:1378 */         con.close();
/* 1232:1379 */         con = null;
/* 1233:     */       }
/* 1234:     */     }
/* 1235:     */     finally
/* 1236:     */     {
/* 1237:1382 */       if (prepstat != null)
/* 1238:     */       {
/* 1239:     */         try
/* 1240:     */         {
/* 1241:1384 */           prepstat.close();
/* 1242:     */         }
/* 1243:     */         catch (SQLException e) {}
/* 1244:1388 */         prepstat = null;
/* 1245:     */       }
/* 1246:1390 */       if (con != null)
/* 1247:     */       {
/* 1248:     */         try
/* 1249:     */         {
/* 1250:1392 */           con.close();
/* 1251:     */         }
/* 1252:     */         catch (SQLException e) {}
/* 1253:1396 */         con = null;
/* 1254:     */       }
/* 1255:     */     }
/* 1256:1407 */     return system_sms_queue;
/* 1257:     */   }
/* 1258:     */   
/* 1259:     */   public void update_currency(String alt_value, String service, java.util.Date date)
/* 1260:     */     throws Exception
/* 1261:     */   {
/* 1262:1412 */     ResultSet rs = null;
/* 1263:1413 */     Connection con = null;
/* 1264:1414 */     PreparedStatement prepstat = null;
/* 1265:     */     try
/* 1266:     */     {
/* 1267:1417 */       java.sql.Date sqlDate_date = new java.sql.Date(date.getTime());
/* 1268:     */       
/* 1269:1419 */       con = DConnect.getConnection();
/* 1270:     */       
/* 1271:1421 */       String SQL = "Update system_sms_queue set alt_value = ? where service=? and date=?";
/* 1272:     */       
/* 1273:1423 */       prepstat = con.prepareStatement(SQL);
/* 1274:     */       
/* 1275:1425 */       prepstat.setString(1, alt_value);
/* 1276:1426 */       prepstat.setString(2, service);
/* 1277:1427 */       prepstat.setDate(3, sqlDate_date);
/* 1278:     */       
/* 1279:1429 */       prepstat.execute();
/* 1280:     */     }
/* 1281:     */     catch (Exception ex)
/* 1282:     */     {
/* 1283:1431 */       if (con != null) {
/* 1284:1432 */         con.close();
/* 1285:     */       }
/* 1286:1434 */       throw new Exception(ex.getMessage());
/* 1287:     */     }
/* 1288:1436 */     if (con != null) {
/* 1289:1437 */       con.close();
/* 1290:     */     }
/* 1291:     */   }
/* 1292:     */   
/* 1293:     */   public static void logInfoRequest(String ownerId, String msg, String accountId, String keyword, String msisdn, String shortcode)
/* 1294:     */     throws Exception
/* 1295:     */   {
/* 1296:1444 */     Connection con = null;
/* 1297:1445 */     PreparedStatement prepstat = null;
/* 1298:     */     
/* 1299:     */ 
/* 1300:1448 */     System.out.println(new java.util.Date() + ":logging Info request with ownerId-accountId:" + ownerId + "-" + accountId + "...");
/* 1301:     */     try
/* 1302:     */     {
/* 1303:1451 */       con = DConnect.getConnection();
/* 1304:     */       
/* 1305:1453 */       String SQL = "insert into  system_sms_usage_log(owner_id, msg, account_id, keyword, msisdn, shortcode) values(?,?,?,?,?,?)";
/* 1306:     */       
/* 1307:     */ 
/* 1308:1456 */       prepstat = con.prepareStatement(SQL);
/* 1309:1457 */       prepstat.setString(1, ownerId);
/* 1310:1458 */       prepstat.setString(2, msg);
/* 1311:1459 */       prepstat.setString(3, accountId);
/* 1312:1460 */       prepstat.setString(4, keyword);
/* 1313:1461 */       prepstat.setString(5, msisdn);
/* 1314:1462 */       prepstat.setString(6, shortcode);
/* 1315:1463 */       prepstat.execute();
/* 1316:     */       
/* 1317:1465 */       System.out.println(new java.util.Date() + ":Infor Request successfully logged!");
/* 1318:     */     }
/* 1319:     */     catch (Exception ex)
/* 1320:     */     {
/* 1321:1467 */       if (con != null) {
/* 1322:1468 */         con.close();
/* 1323:     */       }
/* 1324:1470 */       System.out.println(new java.util.Date() + ":Error logging InfoRequest:" + ex.getMessage());
/* 1325:     */       
/* 1326:1472 */       throw new Exception(ex.getMessage());
/* 1327:     */     }
/* 1328:1474 */     if (prepstat != null) {
/* 1329:1475 */       prepstat.close();
/* 1330:     */     }
/* 1331:1477 */     if (con != null) {
/* 1332:1478 */       con.close();
/* 1333:     */     }
/* 1334:     */   }
/* 1335:     */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.infoserver.InfoServiceDB
 * JD-Core Version:    0.7.0.1
 */