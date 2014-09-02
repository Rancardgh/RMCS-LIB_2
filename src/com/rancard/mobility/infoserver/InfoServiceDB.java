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
/*   27:  19 */     UserService serv = viewService(keyword, accountId);
/*   28:  20 */     int test = 0;
/*   29:  21 */     if ((serv.getAccountId() != null) && (serv.getAccountId().equals(accountId)) && (serv.getKeyword() != null) && (serv.getKeyword().equals(keyword))) {
/*   30:  22 */       test = 1;
/*   31:     */     } else {
/*   32:  24 */       test = 0;
/*   33:     */     }
/*   34:  27 */     if (test == 1)
/*   35:     */     {
/*   36:  30 */       ResultSet rs = null;
/*   37:  31 */       ResultSet rs_label = null;
/*   38:  32 */       Connection con = null;
/*   39:  33 */       PreparedStatement prepstat = null;
/*   40:  34 */       PreparedStatement prepstat_label = null;
/*   41:     */       try
/*   42:     */       {
/*   43:  37 */         con = DConnect.getConnection();
/*   44:  38 */         java.sql.Date sqlDate_date = new java.sql.Date(date.getTime());
/*   45:     */         
/*   46:  40 */         int test_exists = 0;
/*   47:     */         
/*   48:     */ 
/*   49:  43 */         String SQL = "select * from system_sms_queue where publish_date = ? and keyword = ?  and account_id= ? order by msg_id desc";
/*   50:     */         
/*   51:  45 */         prepstat = con.prepareStatement(SQL);
/*   52:  46 */         prepstat.setDate(1, sqlDate_date);
/*   53:  47 */         prepstat.setString(2, keyword);
/*   54:  48 */         prepstat.setString(3, accountId);
/*   55:     */         
/*   56:  50 */         rs = prepstat.executeQuery();
/*   57:  51 */         int lastMsgId = 0;
/*   58:  52 */         if (rs.next())
/*   59:     */         {
/*   60:  53 */           test_exists = 1;
/*   61:  54 */           lastMsgId = rs.getInt("msg_id");
/*   62:     */         }
/*   63:  59 */         String SQL_label = "select * from service_labels where account_id = ? and keyword = ?";
/*   64:  60 */         prepstat_label = con.prepareStatement(SQL_label);
/*   65:  61 */         prepstat_label.setString(1, accountId);
/*   66:  62 */         prepstat_label.setString(2, keyword);
/*   67:     */         
/*   68:  64 */         rs_label = prepstat_label.executeQuery();
/*   69:  65 */         String header = "";
/*   70:  66 */         String footer = "";
/*   71:  67 */         while (rs_label.next())
/*   72:     */         {
/*   73:  68 */           header = rs_label.getString("header");
/*   74:  69 */           footer = rs_label.getString("footer");
/*   75:     */         }
/*   76:  73 */         String final_message = header + message + (footer.equals("") ? "" : new StringBuilder().append("\n").append(footer).toString());
/*   77:     */         
/*   78:  75 */         java.util.Date currentDate = new java.util.Date();
/*   79:  76 */         if (test_exists == 1)
/*   80:     */         {
/*   81:  78 */           addInfoService(final_message, keyword, accountId, lastMsgId + 1, sqlDate_date, ownerId, imageURL, articleTitle);
/*   82:  83 */           if (!date.after(currentDate)) {
/*   83:  84 */             updateService(final_message, keyword, accountId);
/*   84:     */           }
/*   85:     */         }
/*   86:  87 */         else if (test_exists == 0)
/*   87:     */         {
/*   88:  89 */           addInfoService(final_message, keyword, accountId, lastMsgId + 1, sqlDate_date, ownerId, imageURL, articleTitle);
/*   89:  92 */           if (!date.after(currentDate)) {
/*   90:  93 */             updateService(final_message, keyword, accountId);
/*   91:     */           }
/*   92:     */         }
/*   93:     */         else
/*   94:     */         {
/*   95:  96 */           throw new Exception("unable to determine insert basis for service item: " + keyword);
/*   96:     */         }
/*   97:     */       }
/*   98:     */       catch (Exception ex)
/*   99:     */       {
/*  100: 100 */         if (con != null) {
/*  101: 101 */           con.close();
/*  102:     */         }
/*  103: 103 */         throw new Exception(ex.getMessage());
/*  104:     */       }
/*  105:     */       finally
/*  106:     */       {
/*  107: 105 */         if (prepstat != null) {
/*  108: 106 */           prepstat.close();
/*  109:     */         }
/*  110: 108 */         if (prepstat_label != null) {
/*  111: 109 */           prepstat_label.close();
/*  112:     */         }
/*  113: 111 */         if (rs != null) {
/*  114: 112 */           rs.close();
/*  115:     */         }
/*  116: 114 */         if (rs_label != null) {
/*  117: 115 */           rs_label.close();
/*  118:     */         }
/*  119: 117 */         if (con != null) {
/*  120: 118 */           con.close();
/*  121:     */         }
/*  122:     */       }
/*  123:     */     }
/*  124:     */     else
/*  125:     */     {
/*  126: 122 */       System.out.println("Cannot create news item. Service does not exist");
/*  127:     */     }
/*  128:     */   }
/*  129:     */   
/*  130:     */   public int createInfoServiceEntry(java.util.Date date, String keyword, String message, String accountId, String ownerId, String imageURL, String articleTitle, int maxContentLength)
/*  131:     */     throws Exception
/*  132:     */   {
/*  133: 141 */     UserService serv = viewService(keyword, accountId);
/*  134: 142 */     int test = 0;
/*  135: 143 */     if ((serv.getAccountId() != null) && (serv.getAccountId().equals(accountId)) && (serv.getKeyword() != null) && (serv.getKeyword().equals(keyword))) {
/*  136: 144 */       test = 1;
/*  137:     */     } else {
/*  138: 146 */       test = 0;
/*  139:     */     }
/*  140: 149 */     if (test == 1)
/*  141:     */     {
/*  142: 152 */       ResultSet rs = null;
/*  143: 153 */       ResultSet rs_label = null;
/*  144: 154 */       ResultSet rs_duplicateTest = null;
/*  145: 155 */       Connection con = null;
/*  146: 156 */       PreparedStatement prepstat = null;
/*  147: 157 */       PreparedStatement prepstat_label = null;
/*  148: 158 */       PreparedStatement prepstat_duplicateTest = null;
/*  149:     */       try
/*  150:     */       {
/*  151: 161 */         con = DConnect.getConnection();
/*  152: 162 */         java.sql.Date sqlDate_date = new java.sql.Date(date.getTime());
/*  153:     */         
/*  154: 164 */         int test_exists = 0;
/*  155:     */         
/*  156:     */ 
/*  157: 167 */         String SQL = "select * from system_sms_queue where publish_date = ? and keyword = ?  and account_id= ? order by msg_id desc";
/*  158:     */         
/*  159: 169 */         prepstat = con.prepareStatement(SQL);
/*  160: 170 */         prepstat.setDate(1, sqlDate_date);
/*  161: 171 */         prepstat.setString(2, keyword);
/*  162: 172 */         prepstat.setString(3, accountId);
/*  163:     */         
/*  164: 174 */         rs = prepstat.executeQuery();
/*  165: 175 */         int lastMsgId = 0;
/*  166: 176 */         if (rs.next())
/*  167:     */         {
/*  168: 177 */           test_exists = 1;
/*  169: 178 */           lastMsgId = rs.getInt("msg_id");
/*  170:     */         }
/*  171: 183 */         String SQL_label = "select * from service_labels where account_id = ? and keyword = ?";
/*  172: 184 */         prepstat_label = con.prepareStatement(SQL_label);
/*  173: 185 */         prepstat_label.setString(1, accountId);
/*  174: 186 */         prepstat_label.setString(2, keyword);
/*  175:     */         
/*  176: 188 */         rs_label = prepstat_label.executeQuery();
/*  177: 189 */         String header = "";
/*  178: 190 */         String footer = "";
/*  179: 191 */         while (rs_label.next())
/*  180:     */         {
/*  181: 192 */           header = rs_label.getString("header");
/*  182: 193 */           footer = rs_label.getString("footer");
/*  183:     */         }
/*  184: 197 */         String final_message = header + message;
/*  185: 198 */         if (new String(final_message + (footer.equals("") ? "" : new StringBuilder().append("\n").append(footer).toString())).length() <= maxContentLength) {
/*  186: 199 */           final_message = final_message + (footer.equals("") ? "" : new StringBuilder().append("\n").append(footer).toString());
/*  187:     */         } else {
/*  188: 201 */           System.out.println("Ignoring footer: \"" + footer + "\" from actual message. Stored message: " + final_message);
/*  189:     */         }
/*  190: 205 */         if (final_message.length() > maxContentLength) {
/*  191: 206 */           return 1;
/*  192:     */         }
/*  193: 210 */         String SQL_duplicateTest = "select * from system_sms_queue where publish_date = ? and keyword = ?  and account_id= ? and msg_hash = ?";
/*  194:     */         
/*  195: 212 */         prepstat_duplicateTest = con.prepareStatement(SQL_duplicateTest);
/*  196: 213 */         prepstat_duplicateTest.setDate(1, sqlDate_date);
/*  197: 214 */         prepstat_duplicateTest.setString(2, keyword);
/*  198: 215 */         prepstat_duplicateTest.setString(3, accountId);
/*  199: 216 */         prepstat_duplicateTest.setInt(4, final_message.hashCode());
/*  200:     */         
/*  201: 218 */         rs_duplicateTest = prepstat_duplicateTest.executeQuery();
/*  202: 219 */         int duplicate_exists = 0;
/*  203: 221 */         if (rs_duplicateTest.next()) {
/*  204: 222 */           duplicate_exists = 1;
/*  205:     */         }
/*  206: 226 */         if (duplicate_exists == 1) {
/*  207: 227 */           return 2;
/*  208:     */         }
/*  209: 230 */         java.util.Date currentDate = new java.util.Date();
/*  210: 231 */         if (test_exists == 1)
/*  211:     */         {
/*  212: 233 */           addInfoService(final_message, keyword, accountId, lastMsgId + 1, sqlDate_date, ownerId, imageURL, articleTitle);
/*  213: 238 */           if (!date.after(currentDate)) {
/*  214: 239 */             updateService(final_message, keyword, accountId);
/*  215:     */           }
/*  216:     */         }
/*  217: 242 */         else if (test_exists == 0)
/*  218:     */         {
/*  219: 244 */           addInfoService(final_message, keyword, accountId, lastMsgId + 1, sqlDate_date, ownerId, imageURL, articleTitle);
/*  220: 247 */           if (!date.after(currentDate)) {
/*  221: 248 */             updateService(final_message, keyword, accountId);
/*  222:     */           }
/*  223:     */         }
/*  224:     */         else
/*  225:     */         {
/*  226: 251 */           throw new Exception("unable to determine insert basis for service item: " + keyword);
/*  227:     */         }
/*  228:     */       }
/*  229:     */       catch (Exception ex)
/*  230:     */       {
/*  231: 255 */         if (con != null) {
/*  232: 256 */           con.close();
/*  233:     */         }
/*  234: 258 */         throw new Exception(ex.getMessage());
/*  235:     */       }
/*  236:     */       finally
/*  237:     */       {
/*  238: 260 */         if (prepstat != null) {
/*  239: 261 */           prepstat.close();
/*  240:     */         }
/*  241: 263 */         if (prepstat_label != null) {
/*  242: 264 */           prepstat_label.close();
/*  243:     */         }
/*  244: 266 */         if (rs != null) {
/*  245: 267 */           rs.close();
/*  246:     */         }
/*  247: 269 */         if (rs_label != null) {
/*  248: 270 */           rs_label.close();
/*  249:     */         }
/*  250: 272 */         if (con != null) {
/*  251: 273 */           con.close();
/*  252:     */         }
/*  253:     */       }
/*  254:     */     }
/*  255:     */     else
/*  256:     */     {
/*  257: 277 */       System.out.println("Cannot create news item. Service does not exist");
/*  258:     */     }
/*  259: 280 */     return 0;
/*  260:     */   }
/*  261:     */   
/*  262:     */   public int createInfoServiceEntryWithTags(java.util.Date date, String keyword, String message, String accountId, String ownerId, String imageURL, String articleTitle, String contentURL, String author, String tags, String messageRef, int maxContentLength)
/*  263:     */     throws Exception
/*  264:     */   {
/*  265: 286 */     UserService serv = viewService(keyword, accountId);
/*  266: 287 */     int test = 0;
/*  267: 288 */     if ((serv.getAccountId() != null) && (serv.getAccountId().equals(accountId)) && (serv.getKeyword() != null) && (serv.getKeyword().equals(keyword))) {
/*  268: 289 */       test = 1;
/*  269:     */     } else {
/*  270: 291 */       test = 0;
/*  271:     */     }
/*  272: 294 */     if (test == 1)
/*  273:     */     {
/*  274: 297 */       ResultSet rs = null;
/*  275: 298 */       ResultSet rs_label = null;
/*  276: 299 */       ResultSet rs_duplicateTest = null;
/*  277: 300 */       Connection con = null;
/*  278: 301 */       PreparedStatement prepstat = null;
/*  279: 302 */       PreparedStatement prepstat_label = null;
/*  280: 303 */       PreparedStatement prepstat_duplicateTest = null;
/*  281:     */       try
/*  282:     */       {
/*  283: 306 */         con = DConnect.getConnection();
/*  284: 307 */         java.sql.Date sqlDate_date = new java.sql.Date(date.getTime());
/*  285:     */         
/*  286: 309 */         int test_exists = 0;
/*  287:     */         
/*  288:     */ 
/*  289: 312 */         String SQL = "select * from system_sms_queue where publish_date = ? and keyword = ?  and account_id= ? order by msg_id desc";
/*  290:     */         
/*  291: 314 */         prepstat = con.prepareStatement(SQL);
/*  292: 315 */         prepstat.setDate(1, sqlDate_date);
/*  293: 316 */         prepstat.setString(2, keyword);
/*  294: 317 */         prepstat.setString(3, accountId);
/*  295:     */         
/*  296: 319 */         rs = prepstat.executeQuery();
/*  297: 320 */         int lastMsgId = 0;
/*  298: 321 */         if (rs.next())
/*  299:     */         {
/*  300: 322 */           test_exists = 1;
/*  301: 323 */           lastMsgId = rs.getInt("msg_id");
/*  302:     */         }
/*  303: 328 */         String SQL_label = "select * from service_labels where account_id = ? and keyword = ?";
/*  304: 329 */         prepstat_label = con.prepareStatement(SQL_label);
/*  305: 330 */         prepstat_label.setString(1, accountId);
/*  306: 331 */         prepstat_label.setString(2, keyword);
/*  307:     */         
/*  308: 333 */         rs_label = prepstat_label.executeQuery();
/*  309: 334 */         String header = "";
/*  310: 335 */         String footer = "";
/*  311: 336 */         while (rs_label.next())
/*  312:     */         {
/*  313: 337 */           header = rs_label.getString("header");
/*  314: 338 */           footer = rs_label.getString("footer");
/*  315:     */         }
/*  316: 342 */         String final_message = header + message;
/*  317: 343 */         if (new String(final_message + (footer.equals("") ? "" : new StringBuilder().append("\n").append(footer).toString())).length() <= maxContentLength) {
/*  318: 344 */           final_message = final_message + (footer.equals("") ? "" : new StringBuilder().append("\n").append(footer).toString());
/*  319:     */         } else {
/*  320: 346 */           System.out.println("Ignoring footer: \"" + footer + "\" from actual message. Stored message: " + final_message);
/*  321:     */         }
/*  322: 350 */         if (final_message.length() > maxContentLength) {
/*  323: 351 */           return 1;
/*  324:     */         }
/*  325: 355 */         String SQL_duplicateTest = "select * from system_sms_queue where publish_date = ? and keyword = ?  and account_id= ? and msg_hash = ?";
/*  326:     */         
/*  327: 357 */         prepstat_duplicateTest = con.prepareStatement(SQL_duplicateTest);
/*  328: 358 */         prepstat_duplicateTest.setDate(1, sqlDate_date);
/*  329: 359 */         prepstat_duplicateTest.setString(2, keyword);
/*  330: 360 */         prepstat_duplicateTest.setString(3, accountId);
/*  331: 361 */         prepstat_duplicateTest.setInt(4, final_message.hashCode());
/*  332:     */         
/*  333: 363 */         rs_duplicateTest = prepstat_duplicateTest.executeQuery();
/*  334: 364 */         int duplicate_exists = 0;
/*  335: 366 */         if (rs_duplicateTest.next()) {
/*  336: 367 */           duplicate_exists = 1;
/*  337:     */         }
/*  338: 371 */         if (duplicate_exists == 1) {
/*  339: 372 */           return 2;
/*  340:     */         }
/*  341: 375 */         java.util.Date currentDate = new java.util.Date();
/*  342: 377 */         if (test_exists == 1)
/*  343:     */         {
/*  344: 380 */           addInfoServiceWithTags(final_message, keyword, accountId, lastMsgId + 1, sqlDate_date, ownerId, imageURL, articleTitle, contentURL, author, tags, messageRef);
/*  345: 385 */           if (!date.after(currentDate)) {
/*  346: 386 */             updateService(final_message, keyword, accountId);
/*  347:     */           }
/*  348:     */         }
/*  349: 389 */         else if (test_exists == 0)
/*  350:     */         {
/*  351: 391 */           addInfoServiceWithTags(final_message, keyword, accountId, lastMsgId + 1, sqlDate_date, ownerId, imageURL, articleTitle, contentURL, author, tags, messageRef);
/*  352: 394 */           if (!date.after(currentDate)) {
/*  353: 395 */             updateService(final_message, keyword, accountId);
/*  354:     */           }
/*  355:     */         }
/*  356:     */         else
/*  357:     */         {
/*  358: 398 */           throw new Exception("unable to determine insert basis for service item: " + keyword);
/*  359:     */         }
/*  360:     */       }
/*  361:     */       catch (Exception ex)
/*  362:     */       {
/*  363: 402 */         if (con != null) {
/*  364: 403 */           con.close();
/*  365:     */         }
/*  366: 405 */         throw new Exception(ex.getMessage());
/*  367:     */       }
/*  368:     */       finally
/*  369:     */       {
/*  370: 407 */         if (prepstat != null) {
/*  371: 408 */           prepstat.close();
/*  372:     */         }
/*  373: 410 */         if (prepstat_label != null) {
/*  374: 411 */           prepstat_label.close();
/*  375:     */         }
/*  376: 413 */         if (rs != null) {
/*  377: 414 */           rs.close();
/*  378:     */         }
/*  379: 416 */         if (rs_label != null) {
/*  380: 417 */           rs_label.close();
/*  381:     */         }
/*  382: 419 */         if (con != null) {
/*  383: 420 */           con.close();
/*  384:     */         }
/*  385:     */       }
/*  386:     */     }
/*  387:     */     else
/*  388:     */     {
/*  389: 424 */       System.out.println("Cannot create news item. Service does not exist");
/*  390:     */     }
/*  391: 427 */     return 0;
/*  392:     */   }
/*  393:     */   
/*  394:     */   public void addInfoService(String message, String keyword, String accountId, int msg_id, java.sql.Date sqlDate, String ownerId, String imageURL, String articleTitle)
/*  395:     */     throws Exception
/*  396:     */   {
/*  397: 506 */     String SQL = "insert into system_sms_queue (publish_date,Publish_time ,keyword,message,account_id,msg_id,msg_hash, owner_id, image_url, article_title) values";
/*  398: 507 */     SQL = SQL + "(?,?, ?, ?, ?, ?,?, ?, ?, ?)";
/*  399: 508 */     Connection con = null;
/*  400: 509 */     PreparedStatement prepstat = null;
/*  401:     */     try
/*  402:     */     {
/*  403: 511 */       con = DConnect.getConnection();
/*  404: 512 */       prepstat = prepstat = con.prepareStatement(SQL);
/*  405:     */       
/*  406: 514 */       prepstat.setDate(1, sqlDate);
/*  407: 515 */       prepstat.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));
/*  408: 516 */       prepstat.setString(3, keyword);
/*  409: 517 */       prepstat.setString(4, message);
/*  410: 518 */       prepstat.setString(5, accountId);
/*  411: 519 */       prepstat.setInt(6, msg_id);
/*  412: 520 */       prepstat.setInt(7, message.hashCode());
/*  413: 521 */       prepstat.setString(8, ownerId);
/*  414: 522 */       prepstat.setString(9, imageURL);
/*  415: 523 */       prepstat.setString(10, articleTitle);
/*  416: 524 */       prepstat.execute();
/*  417:     */     }
/*  418:     */     catch (Exception ex)
/*  419:     */     {
/*  420: 526 */       if (con != null) {
/*  421: 527 */         con.close();
/*  422:     */       }
/*  423: 529 */       throw new Exception(ex.getMessage());
/*  424:     */     }
/*  425: 533 */     if (prepstat != null) {
/*  426: 534 */       prepstat.close();
/*  427:     */     }
/*  428: 536 */     if (con != null) {
/*  429: 537 */       con.close();
/*  430:     */     }
/*  431:     */   }
/*  432:     */   
/*  433:     */   public void addInfoServiceWithTags(String message, String keyword, String accountId, int msg_id, java.sql.Date sqlDate, String ownerId, String imageURL, String articleTitle, String contentURL, String author, String tags, String messageRef)
/*  434:     */     throws Exception
/*  435:     */   {
/*  436: 549 */     String SQL = "insert into system_sms_queue (publish_date,Publish_time ,keyword,message,account_id,msg_id,msg_hash, owner_id, image_url, article_title,tags,msg_ref) values (?,?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?)";
/*  437:     */     
/*  438:     */ 
/*  439: 552 */     Connection con = null;
/*  440: 553 */     PreparedStatement prepstat = null;
/*  441:     */     try
/*  442:     */     {
/*  443: 555 */       con = DConnect.getConnection();
/*  444: 556 */       prepstat = prepstat = con.prepareStatement(SQL);
/*  445:     */       
/*  446: 558 */       prepstat.setDate(1, sqlDate);
/*  447: 559 */       prepstat.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));
/*  448: 560 */       prepstat.setString(3, keyword);
/*  449: 561 */       prepstat.setString(4, message);
/*  450: 562 */       prepstat.setString(5, accountId);
/*  451: 563 */       prepstat.setInt(6, msg_id);
/*  452: 564 */       prepstat.setInt(7, message.hashCode());
/*  453: 565 */       prepstat.setString(8, ownerId);
/*  454: 566 */       prepstat.setString(9, imageURL);
/*  455: 567 */       prepstat.setString(10, articleTitle);
/*  456: 568 */       prepstat.setString(11, tags);
/*  457: 569 */       prepstat.setString(12, messageRef);
/*  458:     */       
/*  459:     */ 
/*  460: 572 */       prepstat.execute();
/*  461:     */     }
/*  462:     */     catch (Exception ex)
/*  463:     */     {
/*  464: 574 */       if (con != null) {
/*  465: 575 */         con.close();
/*  466:     */       }
/*  467: 577 */       throw new Exception(ex.getMessage());
/*  468:     */     }
/*  469: 581 */     if (prepstat != null) {
/*  470: 582 */       prepstat.close();
/*  471:     */     }
/*  472: 584 */     if (con != null) {
/*  473: 585 */       con.close();
/*  474:     */     }
/*  475:     */   }
/*  476:     */   
/*  477:     */   public void updateInfoService(java.util.Date date, String keyword, String message, String accountId, int msg_id, String imageURL, String articleTitle)
/*  478:     */     throws Exception
/*  479:     */   {
/*  480: 594 */     ResultSet rs = null;
/*  481: 595 */     Connection con = null;
/*  482: 596 */     PreparedStatement prepstat = null;
/*  483:     */     try
/*  484:     */     {
/*  485: 598 */       con = DConnect.getConnection();
/*  486: 599 */       java.sql.Date sqlDate_date = new java.sql.Date(date.getTime());
/*  487:     */       
/*  488: 601 */       String SQL = "update system_sms_queue set Publish_time= ?, message =?, msg_hash=?, image_url=?, article_title=? where keyword =? and publish_date=? and  account_id= ? and msg_id = ?";
/*  489:     */       
/*  490: 603 */       prepstat = con.prepareStatement(SQL);
/*  491: 604 */       prepstat.setTimestamp(1, new Timestamp(new java.util.Date().getTime()));
/*  492: 605 */       prepstat.setString(2, message);
/*  493: 606 */       prepstat.setInt(3, message.hashCode());
/*  494: 607 */       prepstat.setString(4, imageURL);
/*  495: 608 */       prepstat.setString(5, articleTitle);
/*  496: 609 */       prepstat.setString(6, keyword);
/*  497: 610 */       prepstat.setDate(7, sqlDate_date);
/*  498: 611 */       prepstat.setString(8, accountId);
/*  499: 612 */       prepstat.setInt(9, msg_id);
/*  500: 613 */       prepstat.execute();
/*  501:     */     }
/*  502:     */     catch (Exception ex)
/*  503:     */     {
/*  504: 615 */       if (con != null) {
/*  505: 616 */         con.close();
/*  506:     */       }
/*  507: 618 */       throw new Exception(ex.getMessage());
/*  508:     */     }
/*  509: 620 */     if (prepstat != null) {
/*  510: 621 */       prepstat.close();
/*  511:     */     }
/*  512: 623 */     if (rs != null) {
/*  513: 624 */       rs.close();
/*  514:     */     }
/*  515: 626 */     if (con != null) {
/*  516: 627 */       con.close();
/*  517:     */     }
/*  518:     */   }
/*  519:     */   
/*  520:     */   public void updateInfoService(java.util.Date date, String keyword, String message, String accountId, int msg_id, String imageURL, String articleTitle, String author, String contentURL)
/*  521:     */     throws Exception
/*  522:     */   {
/*  523: 635 */     ResultSet rs = null;
/*  524: 636 */     Connection con = null;
/*  525: 637 */     PreparedStatement prepstat = null;
/*  526:     */     try
/*  527:     */     {
/*  528: 639 */       con = DConnect.getConnection();
/*  529: 640 */       java.sql.Date sqlDate_date = new java.sql.Date(date.getTime());
/*  530:     */       
/*  531:     */ 
/*  532:     */ 
/*  533:     */ 
/*  534:     */ 
/*  535:     */ 
/*  536: 647 */       String SQL = "update system_sms_queue set Publish_time= ?, message =?, msg_hash=?, image_url=?, article_title=? where keyword =? and publish_date=? and  account_id= ? and msg_id = ?";
/*  537:     */       
/*  538:     */ 
/*  539: 650 */       prepstat = con.prepareStatement(SQL);
/*  540: 651 */       prepstat.setTimestamp(1, new Timestamp(new java.util.Date().getTime()));
/*  541: 652 */       prepstat.setString(2, message);
/*  542: 653 */       prepstat.setInt(3, message.hashCode());
/*  543: 654 */       prepstat.setString(4, imageURL);
/*  544: 655 */       prepstat.setString(5, articleTitle);
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
/*  556: 667 */       prepstat.setString(6, keyword);
/*  557: 668 */       prepstat.setDate(7, sqlDate_date);
/*  558: 669 */       prepstat.setString(8, accountId);
/*  559: 670 */       prepstat.setInt(9, msg_id);
/*  560:     */       
/*  561: 672 */       prepstat.execute();
/*  562:     */     }
/*  563:     */     catch (Exception ex)
/*  564:     */     {
/*  565: 674 */       if (con != null) {
/*  566: 675 */         con.close();
/*  567:     */       }
/*  568: 677 */       throw new Exception(ex.getMessage());
/*  569:     */     }
/*  570: 679 */     if (prepstat != null) {
/*  571: 680 */       prepstat.close();
/*  572:     */     }
/*  573: 682 */     if (rs != null) {
/*  574: 683 */       rs.close();
/*  575:     */     }
/*  576: 685 */     if (con != null) {
/*  577: 686 */       con.close();
/*  578:     */     }
/*  579:     */   }
/*  580:     */   
/*  581:     */   public void deleteInfoService(Integer id)
/*  582:     */     throws Exception
/*  583:     */   {
/*  584: 693 */     Connection con = null;
/*  585: 694 */     PreparedStatement prepstat = null;
/*  586:     */     try
/*  587:     */     {
/*  588: 696 */       con = DConnect.getConnection();
/*  589: 697 */       String SQL = "delete from system_sms_queue where id=?";
/*  590: 698 */       prepstat = con.prepareStatement(SQL);
/*  591:     */       
/*  592: 700 */       prepstat.setInt(1, id.intValue());
/*  593: 701 */       prepstat.execute();
/*  594:     */     }
/*  595:     */     catch (Exception ex)
/*  596:     */     {
/*  597: 703 */       if (con != null) {
/*  598: 704 */         con.close();
/*  599:     */       }
/*  600: 706 */       throw new Exception(ex.getMessage());
/*  601:     */     }
/*  602: 708 */     if (prepstat != null) {
/*  603: 709 */       prepstat.close();
/*  604:     */     }
/*  605: 711 */     if (con != null) {
/*  606: 712 */       con.close();
/*  607:     */     }
/*  608:     */   }
/*  609:     */   
/*  610:     */   public void deleteInfoService(String keyword, String accountId)
/*  611:     */     throws Exception
/*  612:     */   {
/*  613: 719 */     Connection con = null;
/*  614: 720 */     PreparedStatement prepstat = null;
/*  615:     */     try
/*  616:     */     {
/*  617: 722 */       con = DConnect.getConnection();
/*  618: 723 */       String SQL = "delete from system_sms_queue where keyword=? and account_id=?";
/*  619: 724 */       prepstat = con.prepareStatement(SQL);
/*  620:     */       
/*  621: 726 */       prepstat.setString(1, keyword);
/*  622: 727 */       prepstat.setString(2, accountId);
/*  623: 728 */       prepstat.execute();
/*  624:     */     }
/*  625:     */     catch (Exception ex)
/*  626:     */     {
/*  627: 730 */       if (con != null) {
/*  628: 731 */         con.close();
/*  629:     */       }
/*  630: 733 */       throw new Exception(ex.getMessage());
/*  631:     */     }
/*  632: 735 */     if (prepstat != null) {
/*  633: 736 */       prepstat.close();
/*  634:     */     }
/*  635: 738 */     if (con != null) {
/*  636: 739 */       con.close();
/*  637:     */     }
/*  638:     */   }
/*  639:     */   
/*  640:     */   public void deleteInfoService(java.util.Date date, String keyword, String accountId, int msg_id)
/*  641:     */     throws Exception
/*  642:     */   {
/*  643: 748 */     Connection con = null;
/*  644: 749 */     PreparedStatement prepstat = null;
/*  645:     */     try
/*  646:     */     {
/*  647: 751 */       con = DConnect.getConnection();
/*  648: 752 */       java.sql.Date sqlDate_date = new java.sql.Date(date.getTime());
/*  649:     */       
/*  650: 754 */       String SQL = "delete from system_sms_queue where  keyword =? and publish_date=? and  account_id= ? and msg_id = ?";
/*  651: 755 */       prepstat = con.prepareStatement(SQL);
/*  652: 756 */       prepstat.setString(1, keyword);
/*  653: 757 */       prepstat.setDate(2, sqlDate_date);
/*  654: 758 */       prepstat.setString(3, accountId);
/*  655: 759 */       prepstat.setInt(4, msg_id);
/*  656: 760 */       prepstat.execute();
/*  657:     */     }
/*  658:     */     catch (Exception ex)
/*  659:     */     {
/*  660: 763 */       if (con != null) {
/*  661: 764 */         con.close();
/*  662:     */       }
/*  663: 766 */       throw new Exception(ex.getMessage());
/*  664:     */     }
/*  665: 768 */     if (prepstat != null) {
/*  666: 769 */       prepstat.close();
/*  667:     */     }
/*  668: 771 */     if (con != null) {
/*  669: 772 */       con.close();
/*  670:     */     }
/*  671:     */   }
/*  672:     */   
/*  673:     */   public static InfoService retrieveOutboundMessage(UserService service, java.util.Date date, String messageRef)
/*  674:     */     throws Exception
/*  675:     */   {
/*  676: 781 */     InfoService outboundMessage = new InfoService();
/*  677:     */     
/*  678:     */ 
/*  679: 784 */     ResultSet rs = null;
/*  680: 785 */     Connection con = null;
/*  681: 786 */     PreparedStatement prepstat = null;
/*  682: 787 */     java.sql.Date sqlDate = new java.sql.Date(date.getTime());
/*  683:     */     
/*  684: 789 */     System.out.println(new java.util.Date() + ":@com.rancard.mobility.infoserver.InfoServiceDB..");
/*  685: 790 */     System.out.println(new java.util.Date() + ": retrieving outbound message from message queue...");
/*  686:     */     try
/*  687:     */     {
/*  688: 795 */       if ((messageRef != null) && (!messageRef.equals("")))
/*  689:     */       {
/*  690: 797 */         String SQL = "select ssq.*, sl.header, sl.footer from system_sms_queue ssq left outer join service_labels sl on ssq.account_id = sl.account_id and ssq.keyword = sl.keyword where ssq.keyword=? and ssq.account_id = ? and publish_date =? and msg_ref=? order by publish_time desc, msg_id desc limit 10";
/*  691:     */         
/*  692:     */ 
/*  693:     */ 
/*  694:     */ 
/*  695: 802 */         con = DConnect.getConnection();
/*  696: 803 */         prepstat = con.prepareStatement(SQL);
/*  697: 804 */         prepstat.setString(1, service.getKeyword());
/*  698: 805 */         prepstat.setString(2, service.getAccountId());
/*  699: 806 */         prepstat.setDate(3, sqlDate);
/*  700: 807 */         prepstat.setString(4, messageRef);
/*  701:     */       }
/*  702:     */       else
/*  703:     */       {
/*  704: 810 */         String SQL = "select ssq.*, sl.header, sl.footer from system_sms_queue ssq left outer join service_labels sl on ssq.account_id = sl.account_id and ssq.keyword = sl.keyword where ssq.keyword=? and ssq.account_id = ? and publish_date =? order by publish_time desc, msg_id desc limit 10";
/*  705:     */         
/*  706:     */ 
/*  707:     */ 
/*  708:     */ 
/*  709: 815 */         con = DConnect.getConnection();
/*  710: 816 */         prepstat = con.prepareStatement(SQL);
/*  711: 817 */         prepstat.setString(1, service.getKeyword());
/*  712: 818 */         prepstat.setString(2, service.getAccountId());
/*  713: 819 */         prepstat.setDate(3, sqlDate);
/*  714:     */       }
/*  715: 822 */       rs = prepstat.executeQuery();
/*  716: 824 */       if (rs.next())
/*  717:     */       {
/*  718: 825 */         SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH.mm.ss");
/*  719: 826 */         String publishTime = df.format(new java.util.Date(rs.getTimestamp("Publish_time").getTime()));
/*  720:     */         
/*  721: 828 */         outboundMessage.setPublishTime(publishTime);
/*  722: 829 */         outboundMessage.setKeyword(rs.getString("keyword"));
/*  723:     */         
/*  724: 831 */         Clob clob = rs.getClob("message");
/*  725: 832 */         String tmpMsg = "";
/*  726: 833 */         String temp = "";
/*  727:     */         
/*  728: 835 */         BufferedReader br = new BufferedReader(clob.getCharacterStream());
/*  729: 836 */         while ((temp = br.readLine()) != null) {
/*  730: 837 */           tmpMsg = tmpMsg + temp;
/*  731:     */         }
/*  732: 840 */         tmpMsg = URLUTF8Encoder.removeMalformedChars(tmpMsg);
/*  733: 841 */         System.out.println("Text item from SYSTEM_SMS_QUEUE (WORKING LOCALLY): " + tmpMsg);
/*  734: 842 */         outboundMessage.setMessage(tmpMsg);
/*  735:     */         
/*  736: 844 */         outboundMessage.setAccountId(rs.getString("account_id"));
/*  737: 845 */         outboundMessage.setMsgId(rs.getInt("msg_id"));
/*  738: 846 */         outboundMessage.setPublishDate(new SimpleDateFormat("dd-MMM-yyyy").format(rs.getDate("publish_date")));
/*  739: 847 */         outboundMessage.setOwnerId(rs.getString("owner_id"));
/*  740: 848 */         outboundMessage.setImageURL(rs.getString("image_url"));
/*  741: 849 */         outboundMessage.setArticleTitle(rs.getString("article_title"));
/*  742: 850 */         outboundMessage.setTags(rs.getString("tags"));
/*  743: 851 */         outboundMessage.setMessageRef(rs.getString("msg_ref"));
/*  744:     */         
/*  745: 853 */         String header = rs.getString("header");
/*  746: 854 */         String footer = rs.getString("footer");
/*  747: 855 */         outboundMessage.setHeader(header == null ? "" : header);
/*  748: 856 */         outboundMessage.setFooter(footer == null ? "" : footer);
/*  749:     */         
/*  750: 858 */         outboundMessage.setDefaultMessage(service.getDefaultMessage());
/*  751: 859 */         outboundMessage.setServiceName(service.getServiceName());
/*  752: 860 */         outboundMessage.setServiceType(service.getServiceType());
/*  753: 861 */         outboundMessage.setPricing(service.getPricing());
/*  754: 862 */         outboundMessage.setLastUpdated(service.getLastUpdated());
/*  755: 863 */         outboundMessage.setAllowedShortcodes(service.getAllowedShortcodes());
/*  756: 864 */         outboundMessage.setAllowedSiteTypes(service.getAllowedSiteTypes());
/*  757:     */       }
/*  758:     */     }
/*  759:     */     catch (Exception ex)
/*  760:     */     {
/*  761: 867 */       if (con != null) {
/*  762: 868 */         con.close();
/*  763:     */       }
/*  764: 871 */       System.out.println(new java.util.Date() + ": error retrieving info for " + service.getKeyword() + "/" + date.toString() + "/" + service.getAccountId() + " combination: " + ex.getMessage());
/*  765:     */       
/*  766:     */ 
/*  767: 874 */       throw new Exception(ex.getMessage());
/*  768:     */     }
/*  769:     */     finally
/*  770:     */     {
/*  771: 876 */       if (prepstat != null) {
/*  772: 877 */         prepstat.close();
/*  773:     */       }
/*  774: 879 */       if (rs != null) {
/*  775: 880 */         rs.close();
/*  776:     */       }
/*  777: 882 */       if (con != null) {
/*  778: 883 */         con.close();
/*  779:     */       }
/*  780:     */     }
/*  781: 886 */     return outboundMessage;
/*  782:     */   }
/*  783:     */   
/*  784:     */   public InfoService viewInfoService(String keyword, String accountId, java.util.Date date)
/*  785:     */     throws Exception
/*  786:     */   {
/*  787: 890 */     InfoService system_sms_queue = new InfoService();
/*  788: 891 */     ArrayList<InfoService> recordList = new ArrayList();
/*  789:     */     
/*  790:     */ 
/*  791: 894 */     ResultSet rs = null;
/*  792: 895 */     Connection con = null;
/*  793: 896 */     PreparedStatement prepstat = null;
/*  794: 897 */     java.sql.Date sqlDate = new java.sql.Date(date.getTime());
/*  795:     */     
/*  796: 899 */     System.out.println(new java.util.Date() + ":@com.rancard.mobility.infoserver.InfoServiceDB..");
/*  797: 900 */     System.out.println(new java.util.Date() + ":viewing info service...");
/*  798:     */     try
/*  799:     */     {
/*  800: 904 */       UserService service = ServiceManager.viewService(keyword, accountId);
/*  801: 905 */       system_sms_queue.setAccountId(service.getAccountId());
/*  802: 906 */       system_sms_queue.setKeyword(service.getKeyword());
/*  803: 907 */       system_sms_queue.setDefaultMessage(service.getDefaultMessage());
/*  804: 908 */       system_sms_queue.setServiceName(service.getServiceName());
/*  805: 909 */       system_sms_queue.setServiceType(service.getServiceType());
/*  806: 910 */       system_sms_queue.setPricing(service.getPricing());
/*  807: 911 */       system_sms_queue.setLastUpdated(service.getLastUpdated());
/*  808: 912 */       system_sms_queue.setAllowedShortcodes(service.getAllowedShortcodes());
/*  809: 913 */       system_sms_queue.setAllowedSiteTypes(service.getAllowedSiteTypes());
/*  810:     */       
/*  811: 915 */       String SQL = "select ssq.*, sl.header, sl.footer from system_sms_queue ssq left outer join service_labels sl on ssq.account_id = sl.account_id and ssq.keyword = sl.keyword where ssq.keyword=? and ssq.account_id = ? and publish_date =? order by publish_time desc, msg_id limit 10";
/*  812:     */       
/*  813:     */ 
/*  814:     */ 
/*  815:     */ 
/*  816: 920 */       con = DConnect.getConnection();
/*  817: 921 */       prepstat = con.prepareStatement(SQL);
/*  818: 922 */       prepstat.setString(1, keyword);
/*  819: 923 */       prepstat.setString(2, accountId);
/*  820: 924 */       prepstat.setDate(3, sqlDate);
/*  821: 925 */       rs = prepstat.executeQuery();
/*  822: 927 */       while (rs.next())
/*  823:     */       {
/*  824: 928 */         SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH.mm.ss");
/*  825: 929 */         String publishTime = df.format(new java.util.Date(rs.getTimestamp("Publish_time").getTime()));
/*  826: 930 */         system_sms_queue.setPublishTime(publishTime);
/*  827: 931 */         system_sms_queue.setKeyword(rs.getString("keyword"));
/*  828:     */         
/*  829: 933 */         Clob clob = rs.getClob("message");
/*  830: 934 */         String tmpMsg = "";
/*  831: 935 */         String temp = "";
/*  832:     */         
/*  833: 937 */         BufferedReader br = new BufferedReader(clob.getCharacterStream());
/*  834: 938 */         while ((temp = br.readLine()) != null) {
/*  835: 939 */           tmpMsg = tmpMsg + temp;
/*  836:     */         }
/*  837: 942 */         tmpMsg = URLUTF8Encoder.removeMalformedChars(tmpMsg);
/*  838:     */         
/*  839: 944 */         System.out.println("Text item from SYSTEM_SMS_QUEUE (WORKING LOCALLY): " + tmpMsg);
/*  840: 945 */         system_sms_queue.setMessage(tmpMsg);
/*  841:     */         
/*  842: 947 */         system_sms_queue.setAccountId(rs.getString("account_id"));
/*  843: 948 */         system_sms_queue.setMsgId(rs.getInt("msg_id"));
/*  844: 949 */         system_sms_queue.setPublishDate(new SimpleDateFormat("dd-MMM-yyyy").format(rs.getDate("publish_date")));
/*  845: 950 */         system_sms_queue.setOwnerId(rs.getString("owner_id"));
/*  846: 951 */         system_sms_queue.setImageURL(rs.getString("image_url"));
/*  847: 952 */         system_sms_queue.setArticleTitle(rs.getString("article_title"));
/*  848:     */         
/*  849: 954 */         String header = rs.getString("header");
/*  850: 955 */         String footer = rs.getString("footer");
/*  851: 956 */         system_sms_queue.setHeader(header == null ? "" : header);
/*  852: 957 */         system_sms_queue.setFooter(footer == null ? "" : footer);
/*  853:     */         
/*  854: 959 */         system_sms_queue.setDefaultMessage(service.getDefaultMessage());
/*  855: 960 */         system_sms_queue.setServiceName(service.getServiceName());
/*  856: 961 */         system_sms_queue.setServiceType(service.getServiceType());
/*  857: 962 */         system_sms_queue.setPricing(service.getPricing());
/*  858: 963 */         system_sms_queue.setLastUpdated(service.getLastUpdated());
/*  859: 964 */         system_sms_queue.setAllowedShortcodes(service.getAllowedShortcodes());
/*  860: 965 */         system_sms_queue.setAllowedSiteTypes(service.getAllowedSiteTypes());
/*  861:     */         
/*  862: 967 */         recordList.add(system_sms_queue);
/*  863: 968 */         system_sms_queue = new InfoService();
/*  864:     */       }
/*  865: 972 */       int recordCount = recordList.size();
/*  866: 973 */       System.out.println(new java.util.Date() + ":" + recordCount + " records retrieved from db for " + keyword + "/" + date.toString() + "/" + accountId + " combination");
/*  867: 974 */       int msg_id = 0;
/*  868: 975 */       int msg_base = 0;
/*  869: 976 */       if (recordCount > 0)
/*  870:     */       {
/*  871: 977 */         msg_base = ((InfoService)recordList.get(0)).getMsgId();
/*  872: 978 */         msg_id = selectMessage(recordCount, msg_base);
/*  873: 979 */         System.out.println(new java.util.Date() + ":" + msg_id + " selected as random msg_id");
/*  874:     */       }
/*  875: 982 */       if (!recordList.isEmpty()) {
/*  876: 983 */         system_sms_queue = (InfoService)recordList.get(msg_id - msg_base);
/*  877:     */       }
/*  878:     */     }
/*  879:     */     catch (Exception ex)
/*  880:     */     {
/*  881: 986 */       if (con != null) {
/*  882: 987 */         con.close();
/*  883:     */       }
/*  884: 990 */       System.out.println(new java.util.Date() + ": error retrieving info for " + keyword + "/" + date.toString() + "/" + accountId + " combination: " + ex.getMessage());
/*  885:     */       
/*  886: 992 */       throw new Exception(ex.getMessage());
/*  887:     */     }
/*  888:     */     finally
/*  889:     */     {
/*  890: 994 */       if (prepstat != null) {
/*  891: 995 */         prepstat.close();
/*  892:     */       }
/*  893: 997 */       if (rs != null) {
/*  894: 998 */         rs.close();
/*  895:     */       }
/*  896:1000 */       if (con != null) {
/*  897:1001 */         con.close();
/*  898:     */       }
/*  899:     */     }
/*  900:1004 */     return system_sms_queue;
/*  901:     */   }
/*  902:     */   
/*  903:     */   public static InfoService viewInfoService(String keyword, String accountId, java.util.Date date, int msg_id)
/*  904:     */     throws Exception
/*  905:     */   {
/*  906:1008 */     InfoService system_sms_queue = new InfoService();
/*  907:     */     
/*  908:     */ 
/*  909:1011 */     ResultSet rs = null;
/*  910:1012 */     Connection con = null;
/*  911:1013 */     PreparedStatement prepstat = null;
/*  912:1014 */     java.sql.Date sqlDate = new java.sql.Date(date.getTime());
/*  913:     */     try
/*  914:     */     {
/*  915:1017 */       UserService service = ServiceManager.viewService(keyword, accountId);
/*  916:     */       
/*  917:1019 */       system_sms_queue.setAccountId(service.getAccountId());
/*  918:1020 */       system_sms_queue.setKeyword(service.getKeyword());
/*  919:1021 */       system_sms_queue.setDefaultMessage(service.getDefaultMessage());
/*  920:1022 */       system_sms_queue.setServiceName(service.getServiceName());
/*  921:1023 */       system_sms_queue.setServiceType(service.getServiceType());
/*  922:1024 */       system_sms_queue.setPricing(service.getPricing());
/*  923:1025 */       system_sms_queue.setLastUpdated(service.getLastUpdated());
/*  924:1026 */       system_sms_queue.setAllowedShortcodes(service.getAllowedShortcodes());
/*  925:1027 */       system_sms_queue.setAllowedSiteTypes(service.getAllowedSiteTypes());
/*  926:     */       
/*  927:1029 */       con = DConnect.getConnection();
/*  928:     */       
/*  929:1031 */       String SQL = "select ssq.*, sl.header, sl.footer from system_sms_queue ssq left outer join service_labels sl on ssq.account_id = sl.account_id and ssq.keyword = sl.keyword where ssq.keyword=? and ssq.account_id = ? and publish_date =? and msg_id = ?";
/*  930:     */       
/*  931:     */ 
/*  932:     */ 
/*  933:1035 */       prepstat = con.prepareStatement(SQL);
/*  934:     */       
/*  935:1037 */       prepstat.setString(1, keyword);
/*  936:1038 */       prepstat.setString(2, accountId);
/*  937:1039 */       prepstat.setDate(3, sqlDate);
/*  938:1040 */       prepstat.setInt(4, msg_id);
/*  939:1041 */       rs = prepstat.executeQuery();
/*  940:1043 */       while (rs.next())
/*  941:     */       {
/*  942:1044 */         SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH.mm.ss");
/*  943:1045 */         String publishTime = df.format(new java.util.Date(rs.getTimestamp("Publish_time").getTime()));
/*  944:1046 */         system_sms_queue.setPublishTime(publishTime);
/*  945:1047 */         system_sms_queue.setKeyword(rs.getString("keyword"));
/*  946:     */         
/*  947:1049 */         Clob clob = rs.getClob("message");
/*  948:1050 */         String tmpMsg = "";
/*  949:1051 */         String temp = "";
/*  950:     */         
/*  951:1053 */         BufferedReader br = new BufferedReader(clob.getCharacterStream());
/*  952:1054 */         while ((temp = br.readLine()) != null) {
/*  953:1055 */           tmpMsg = tmpMsg + temp;
/*  954:     */         }
/*  955:1058 */         tmpMsg = URLUTF8Encoder.removeMalformedChars(tmpMsg);
/*  956:1059 */         System.out.println("Text item from SYSTEM_SMS_QUEUE (WORKING LOCALLY): " + tmpMsg);
/*  957:1060 */         system_sms_queue.setMessage(tmpMsg);
/*  958:     */         
/*  959:1062 */         system_sms_queue.setAccountId(rs.getString("account_id"));
/*  960:1063 */         system_sms_queue.setMsgId(rs.getInt("msg_id"));
/*  961:1064 */         system_sms_queue.setPublishDate(new SimpleDateFormat("dd-MMM-yyyy").format(rs.getDate("publish_date")));
/*  962:1065 */         system_sms_queue.setOwnerId(rs.getString("owner_id"));
/*  963:1066 */         system_sms_queue.setImageURL(rs.getString("image_url"));
/*  964:1067 */         system_sms_queue.setArticleTitle(rs.getString("article_title"));
/*  965:     */         
/*  966:1069 */         String header = rs.getString("header");
/*  967:1070 */         String footer = rs.getString("footer");
/*  968:1071 */         system_sms_queue.setHeader(header == null ? "" : header);
/*  969:1072 */         system_sms_queue.setFooter(footer == null ? "" : footer);
/*  970:     */       }
/*  971:     */     }
/*  972:     */     catch (Exception ex)
/*  973:     */     {
/*  974:1076 */       if (con != null) {
/*  975:1077 */         con.close();
/*  976:     */       }
/*  977:1079 */       throw new Exception(ex.getMessage());
/*  978:     */     }
/*  979:1081 */     if (prepstat != null) {
/*  980:1082 */       prepstat.close();
/*  981:     */     }
/*  982:1084 */     if (rs != null) {
/*  983:1085 */       rs.close();
/*  984:     */     }
/*  985:1087 */     if (con != null) {
/*  986:1088 */       con.close();
/*  987:     */     }
/*  988:1091 */     return system_sms_queue;
/*  989:     */   }
/*  990:     */   
/*  991:     */   private int selectMessage(int n, int startRange)
/*  992:     */   {
/*  993:1115 */     Random r = new Random();
/*  994:1116 */     r.setSeed(new java.util.Date().getTime());
/*  995:1117 */     int ret = r.nextInt(n > 0 ? n : 1) + startRange;
/*  996:1118 */     return ret;
/*  997:     */   }
/*  998:     */   
/*  999:     */   public ArrayList<InfoService> viewInfoServices(String keyword, String accountId, java.util.Date date)
/* 1000:     */     throws Exception
/* 1001:     */   {
/* 1002:1146 */     InfoService system_sms_queue = null;
/* 1003:     */     
/* 1004:1148 */     ArrayList<InfoService> list = new ArrayList();
/* 1005:     */     
/* 1006:1150 */     String counterSQL = "select ssq.*, sl.header, sl.footer from system_sms_queue ssq left outer join service_labels sl on ssq.account_id = sl.account_id and ssq.keyword = sl.keyword where ssq.keyword=? and ssq.account_id = ? and publish_date =? order by msg_id desc";
/* 1007:     */     
/* 1008:     */ 
/* 1009:     */ 
/* 1010:1154 */     Connection con = DConnect.getConnection();
/* 1011:1155 */     PreparedStatement counterStat = con.prepareStatement(counterSQL);
/* 1012:1156 */     counterStat.setString(1, keyword);
/* 1013:1157 */     counterStat.setString(2, accountId);
/* 1014:1158 */     counterStat.setDate(3, new java.sql.Date(date.getTime()));
/* 1015:1159 */     ResultSet rs = counterStat.executeQuery();
/* 1016:1161 */     while (rs.next())
/* 1017:     */     {
/* 1018:1162 */       system_sms_queue = new InfoService();
/* 1019:1163 */       SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH.mm.ss");
/* 1020:1164 */       String publishTime = df.format(new java.util.Date(rs.getTimestamp("Publish_time").getTime()));
/* 1021:1165 */       system_sms_queue.setPublishTime(publishTime);
/* 1022:1166 */       system_sms_queue.setKeyword(rs.getString("keyword"));
/* 1023:     */       
/* 1024:1168 */       Clob clob = rs.getClob("message");
/* 1025:1169 */       String tmpMsg = "";
/* 1026:1170 */       String temp = "";
/* 1027:     */       
/* 1028:1172 */       BufferedReader br = new BufferedReader(clob.getCharacterStream());
/* 1029:1173 */       while ((temp = br.readLine()) != null) {
/* 1030:1174 */         tmpMsg = tmpMsg + temp;
/* 1031:     */       }
/* 1032:1177 */       tmpMsg = URLUTF8Encoder.removeMalformedChars(tmpMsg);
/* 1033:1178 */       System.out.println("Text item from SYSTEM_SMS_QUEUE (WORKING LOCALLY): " + tmpMsg);
/* 1034:1179 */       system_sms_queue.setMessage(tmpMsg);
/* 1035:     */       
/* 1036:1181 */       system_sms_queue.setAccountId(rs.getString("account_id"));
/* 1037:1182 */       system_sms_queue.setMsgId(rs.getInt("msg_id"));
/* 1038:1183 */       system_sms_queue.setOwnerId(rs.getString("owner_id"));
/* 1039:1184 */       system_sms_queue.setPublishDate(new SimpleDateFormat("dd-MMM-yyyy").format(rs.getDate("publish_date")));
/* 1040:1185 */       system_sms_queue.setImageURL(rs.getString("image_url"));
/* 1041:1186 */       system_sms_queue.setArticleTitle(rs.getString("article_title"));
/* 1042:     */       
/* 1043:1188 */       String header = rs.getString("header");
/* 1044:1189 */       String footer = rs.getString("footer");
/* 1045:1190 */       system_sms_queue.setHeader(header == null ? "" : header);
/* 1046:1191 */       system_sms_queue.setFooter(footer == null ? "" : footer);
/* 1047:     */       
/* 1048:     */ 
/* 1049:1194 */       list.add(system_sms_queue);
/* 1050:     */     }
/* 1051:1197 */     return list;
/* 1052:     */   }
/* 1053:     */   
/* 1054:     */   public HashMap<String, String> viewHeaderFooter(String keyword, String accountId)
/* 1055:     */     throws Exception
/* 1056:     */   {
/* 1057:1201 */     HashMap<String, String> hf_map = new HashMap();
/* 1058:     */     
/* 1059:1203 */     ResultSet rs = null;
/* 1060:1204 */     Connection con = null;
/* 1061:1205 */     PreparedStatement prepstat = null;
/* 1062:     */     
/* 1063:1207 */     String header = "";
/* 1064:1208 */     String footer = "";
/* 1065:     */     try
/* 1066:     */     {
/* 1067:1211 */       con = DConnect.getConnection();
/* 1068:1212 */       String SQL = "select * from service_labels  where keyword = ?  and account_id= ?";
/* 1069:     */       
/* 1070:1214 */       prepstat = con.prepareStatement(SQL);
/* 1071:1215 */       prepstat.setString(1, keyword);
/* 1072:1216 */       prepstat.setString(2, accountId);
/* 1073:     */       
/* 1074:1218 */       rs = prepstat.executeQuery();
/* 1075:1220 */       while (rs.next())
/* 1076:     */       {
/* 1077:1221 */         header = rs.getString("header");
/* 1078:1222 */         footer = rs.getString("footer");
/* 1079:     */       }
/* 1080:     */     }
/* 1081:     */     catch (Exception ex)
/* 1082:     */     {
/* 1083:1226 */       if (con != null) {
/* 1084:1227 */         con.close();
/* 1085:     */       }
/* 1086:1229 */       throw new Exception(ex.getMessage());
/* 1087:     */     }
/* 1088:     */     finally
/* 1089:     */     {
/* 1090:1231 */       if (prepstat != null) {
/* 1091:1232 */         prepstat.close();
/* 1092:     */       }
/* 1093:1234 */       if (rs != null) {
/* 1094:1235 */         rs.close();
/* 1095:     */       }
/* 1096:1237 */       if (con != null) {
/* 1097:1238 */         con.close();
/* 1098:     */       }
/* 1099:     */     }
/* 1100:1242 */     hf_map.put("header", header == null ? "" : header);
/* 1101:1243 */     hf_map.put("footer", footer == null ? "" : footer);
/* 1102:     */     
/* 1103:1245 */     return hf_map;
/* 1104:     */   }
/* 1105:     */   
/* 1106:     */   public ArrayList<InfoService> viewInfoServices(String keyword, String accountId, java.util.Date date, String ownerId)
/* 1107:     */     throws Exception
/* 1108:     */   {
/* 1109:1249 */     InfoService system_sms_queue = null;
/* 1110:     */     
/* 1111:1251 */     ArrayList<InfoService> list = new ArrayList();
/* 1112:     */     
/* 1113:1253 */     String counterSQL = "select ssq.*, sl.header, sl.footer from system_sms_queue ssq left outer join service_labels sl on ssq.account_id = sl.account_id and ssq.keyword = sl.keyword where ssq.keyword=? and ssq.account_id = ? and publish_date =? and owner_id =? order by msg_id desc";
/* 1114:     */     
/* 1115:     */ 
/* 1116:     */ 
/* 1117:1257 */     Connection con = DConnect.getConnection();
/* 1118:1258 */     PreparedStatement counterStat = con.prepareStatement(counterSQL);
/* 1119:1259 */     counterStat.setString(1, keyword);
/* 1120:1260 */     counterStat.setString(2, accountId);
/* 1121:1261 */     counterStat.setDate(3, new java.sql.Date(date.getTime()));
/* 1122:1262 */     counterStat.setString(4, ownerId);
/* 1123:     */     
/* 1124:1264 */     ResultSet rs = counterStat.executeQuery();
/* 1125:1266 */     while (rs.next())
/* 1126:     */     {
/* 1127:1267 */       system_sms_queue = new InfoService();
/* 1128:1268 */       SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH.mm.ss");
/* 1129:1269 */       String publishTime = df.format(new java.util.Date(rs.getTimestamp("Publish_time").getTime()));
/* 1130:1270 */       system_sms_queue.setPublishTime(publishTime);
/* 1131:1271 */       system_sms_queue.setKeyword(rs.getString("keyword"));
/* 1132:     */       
/* 1133:1273 */       Clob clob = rs.getClob("message");
/* 1134:1274 */       String tmpMsg = "";
/* 1135:1275 */       String temp = "";
/* 1136:     */       
/* 1137:1277 */       BufferedReader br = new BufferedReader(clob.getCharacterStream());
/* 1138:1278 */       while ((temp = br.readLine()) != null) {
/* 1139:1279 */         tmpMsg = tmpMsg + temp;
/* 1140:     */       }
/* 1141:1282 */       tmpMsg = URLUTF8Encoder.removeMalformedChars(tmpMsg);
/* 1142:1283 */       System.out.println("Text item from SYSTEM_SMS_QUEUE (WORKING LOCALLY): " + tmpMsg);
/* 1143:1284 */       system_sms_queue.setMessage(tmpMsg);
/* 1144:     */       
/* 1145:1286 */       system_sms_queue.setAccountId(rs.getString("account_id"));
/* 1146:1287 */       system_sms_queue.setMsgId(rs.getInt("msg_id"));
/* 1147:1288 */       system_sms_queue.setOwnerId(rs.getString("owner_id"));
/* 1148:1289 */       system_sms_queue.setPublishDate(new SimpleDateFormat("dd-MMM-yyyy").format(rs.getDate("publish_date")));
/* 1149:1290 */       system_sms_queue.setImageURL(rs.getString("image_url"));
/* 1150:1291 */       system_sms_queue.setArticleTitle(rs.getString("article_title"));
/* 1151:     */       
/* 1152:1293 */       String header = rs.getString("header");
/* 1153:1294 */       String footer = rs.getString("footer");
/* 1154:1295 */       system_sms_queue.setHeader(header == null ? "" : header);
/* 1155:1296 */       system_sms_queue.setFooter(footer == null ? "" : footer);
/* 1156:     */       
/* 1157:     */ 
/* 1158:1299 */       list.add(system_sms_queue);
/* 1159:     */     }
/* 1160:1302 */     return list;
/* 1161:     */   }
/* 1162:     */   
/* 1163:     */   public void updatebackupsystem_sms_queue(java.util.Date date, String service, String message)
/* 1164:     */     throws Exception
/* 1165:     */   {
/* 1166:1308 */     ResultSet rs = null;
/* 1167:1309 */     Connection con = null;
/* 1168:1310 */     PreparedStatement prepstat = null;
/* 1169:     */     try
/* 1170:     */     {
/* 1171:1314 */       java.sql.Date sqlDate_date = new java.sql.Date(date.getTime());
/* 1172:     */       
/* 1173:1316 */       con = DConnect.getConnection();
/* 1174:     */       
/* 1175:1318 */       String SQL = "Update  backup_system_sms_queue set message= ? , last_update=? where keyword=? ";
/* 1176:     */       
/* 1177:1320 */       prepstat = con.prepareStatement(SQL);
/* 1178:     */       
/* 1179:     */ 
/* 1180:1323 */       prepstat.setString(1, message);
/* 1181:1324 */       prepstat.setDate(2, sqlDate_date);
/* 1182:1325 */       prepstat.setString(3, service);
/* 1183:     */       
/* 1184:1327 */       prepstat.execute();
/* 1185:     */     }
/* 1186:     */     catch (Exception ex)
/* 1187:     */     {
/* 1188:1329 */       if (con != null) {
/* 1189:1330 */         con.close();
/* 1190:     */       }
/* 1191:1332 */       throw new Exception(ex.getMessage());
/* 1192:     */     }
/* 1193:1334 */     if (con != null) {
/* 1194:1335 */       con.close();
/* 1195:     */     }
/* 1196:     */   }
/* 1197:     */   
/* 1198:     */   public InfoService viewbackupsystem_sms_queue(String keyword)
/* 1199:     */     throws Exception
/* 1200:     */   {
/* 1201:1341 */     InfoService system_sms_queue = new InfoService();
/* 1202:     */     
/* 1203:     */ 
/* 1204:1344 */     ResultSet rs = null;
/* 1205:1345 */     Connection con = null;
/* 1206:1346 */     PreparedStatement prepstat = null;
/* 1207:     */     try
/* 1208:     */     {
/* 1209:1349 */       con = DConnect.getConnection();
/* 1210:1350 */       java.sql.Date tempdate = null;
/* 1211:     */       
/* 1212:1352 */       String SQL = "select * from  backup_system_sms_queue where keyword=?";
/* 1213:     */       
/* 1214:1354 */       prepstat = con.prepareStatement(SQL);
/* 1215:     */       
/* 1216:1356 */       prepstat.setString(1, keyword);
/* 1217:1357 */       rs = prepstat.executeQuery();
/* 1218:1359 */       while (rs.next()) {
/* 1219:1362 */         system_sms_queue.setMessage(rs.getString("message"));
/* 1220:     */       }
/* 1221:1366 */       prepstat.close();
/* 1222:1367 */       prepstat = null;
/* 1223:1368 */       con.close();
/* 1224:1369 */       con = null;
/* 1225:     */     }
/* 1226:     */     catch (Exception ex)
/* 1227:     */     {
/* 1228:1371 */       ex.printStackTrace();
/* 1229:1372 */       if (con != null)
/* 1230:     */       {
/* 1231:1373 */         con.close();
/* 1232:1374 */         con = null;
/* 1233:     */       }
/* 1234:     */     }
/* 1235:     */     finally
/* 1236:     */     {
/* 1237:1377 */       if (prepstat != null)
/* 1238:     */       {
/* 1239:     */         try
/* 1240:     */         {
/* 1241:1379 */           prepstat.close();
/* 1242:     */         }
/* 1243:     */         catch (SQLException e) {}
/* 1244:1383 */         prepstat = null;
/* 1245:     */       }
/* 1246:1385 */       if (con != null)
/* 1247:     */       {
/* 1248:     */         try
/* 1249:     */         {
/* 1250:1387 */           con.close();
/* 1251:     */         }
/* 1252:     */         catch (SQLException e) {}
/* 1253:1391 */         con = null;
/* 1254:     */       }
/* 1255:     */     }
/* 1256:1402 */     return system_sms_queue;
/* 1257:     */   }
/* 1258:     */   
/* 1259:     */   public void update_currency(String alt_value, String service, java.util.Date date)
/* 1260:     */     throws Exception
/* 1261:     */   {
/* 1262:1407 */     ResultSet rs = null;
/* 1263:1408 */     Connection con = null;
/* 1264:1409 */     PreparedStatement prepstat = null;
/* 1265:     */     try
/* 1266:     */     {
/* 1267:1412 */       java.sql.Date sqlDate_date = new java.sql.Date(date.getTime());
/* 1268:     */       
/* 1269:1414 */       con = DConnect.getConnection();
/* 1270:     */       
/* 1271:1416 */       String SQL = "Update system_sms_queue set alt_value = ? where service=? and date=?";
/* 1272:     */       
/* 1273:1418 */       prepstat = con.prepareStatement(SQL);
/* 1274:     */       
/* 1275:1420 */       prepstat.setString(1, alt_value);
/* 1276:1421 */       prepstat.setString(2, service);
/* 1277:1422 */       prepstat.setDate(3, sqlDate_date);
/* 1278:     */       
/* 1279:1424 */       prepstat.execute();
/* 1280:     */     }
/* 1281:     */     catch (Exception ex)
/* 1282:     */     {
/* 1283:1426 */       if (con != null) {
/* 1284:1427 */         con.close();
/* 1285:     */       }
/* 1286:1429 */       throw new Exception(ex.getMessage());
/* 1287:     */     }
/* 1288:1431 */     if (con != null) {
/* 1289:1432 */       con.close();
/* 1290:     */     }
/* 1291:     */   }
/* 1292:     */   
/* 1293:     */   public static void logInfoRequest(String ownerId, String msg, String accountId, String keyword, String msisdn, String shortcode)
/* 1294:     */     throws Exception
/* 1295:     */   {
/* 1296:1439 */     Connection con = null;
/* 1297:1440 */     PreparedStatement prepstat = null;
/* 1298:     */     
/* 1299:     */ 
/* 1300:1443 */     System.out.println(new java.util.Date() + ":logging Info request with ownerId-accountId:" + ownerId + "-" + accountId + "...");
/* 1301:     */     try
/* 1302:     */     {
/* 1303:1446 */       con = DConnect.getConnection();
/* 1304:     */       
/* 1305:1448 */       String SQL = "insert into  system_sms_usage_log(owner_id, msg, account_id, keyword, msisdn, shortcode) values(?,?,?,?,?,?)";
/* 1306:     */       
/* 1307:     */ 
/* 1308:1451 */       prepstat = con.prepareStatement(SQL);
/* 1309:1452 */       prepstat.setString(1, ownerId);
/* 1310:1453 */       prepstat.setString(2, msg);
/* 1311:1454 */       prepstat.setString(3, accountId);
/* 1312:1455 */       prepstat.setString(4, keyword);
/* 1313:1456 */       prepstat.setString(5, msisdn);
/* 1314:1457 */       prepstat.setString(6, shortcode);
/* 1315:1458 */       prepstat.execute();
/* 1316:     */       
/* 1317:1460 */       System.out.println(new java.util.Date() + ":Infor Request successfully logged!");
/* 1318:     */     }
/* 1319:     */     catch (Exception ex)
/* 1320:     */     {
/* 1321:1462 */       if (con != null) {
/* 1322:1463 */         con.close();
/* 1323:     */       }
/* 1324:1465 */       System.out.println(new java.util.Date() + ":Error logging InfoRequest:" + ex.getMessage());
/* 1325:     */       
/* 1326:1467 */       throw new Exception(ex.getMessage());
/* 1327:     */     }
/* 1328:1469 */     if (prepstat != null) {
/* 1329:1470 */       prepstat.close();
/* 1330:     */     }
/* 1331:1472 */     if (con != null) {
/* 1332:1473 */       con.close();
/* 1333:     */     }
/* 1334:     */   }
/* 1335:     */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.InfoServiceDB
 * JD-Core Version:    0.7.0.1
 */