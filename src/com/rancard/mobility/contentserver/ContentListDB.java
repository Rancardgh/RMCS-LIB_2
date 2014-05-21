/*    1:     */ package com.rancard.mobility.contentserver;
/*    2:     */ 
/*    3:     */ import com.rancard.common.DConnect;
/*    4:     */ import com.rancard.mobility.common.FonCapabilityMtrx;
/*    5:     */ import com.rancard.mobility.contentprovider.User;
/*    6:     */ import com.rancard.util.Page;
/*    7:     */ import java.io.BufferedInputStream;
/*    8:     */ import java.io.File;
/*    9:     */ import java.io.FileInputStream;
/*   10:     */ import java.io.FileNotFoundException;
/*   11:     */ import java.io.PrintStream;
/*   12:     */ import java.sql.BatchUpdateException;
/*   13:     */ import java.sql.Connection;
/*   14:     */ import java.sql.PreparedStatement;
/*   15:     */ import java.sql.ResultSet;
/*   16:     */ import java.sql.SQLException;
/*   17:     */ import java.sql.Statement;
/*   18:     */ import java.sql.Timestamp;
/*   19:     */ import java.util.ArrayList;
/*   20:     */ import java.util.Calendar;
/*   21:     */ import java.util.HashMap;
/*   22:     */ import java.util.Iterator;
/*   23:     */ import java.util.List;
/*   24:     */ import java.util.StringTokenizer;
/*   25:     */ import net.sourceforge.wurfl.wurflapi.CapabilityMatrix;
/*   26:     */ import net.sourceforge.wurfl.wurflapi.WurflDevice;
/*   27:     */ 
/*   28:     */ public class ContentListDB
/*   29:     */ {
/*   30:     */   public void loadPreviewFiles(ArrayList fileRefrences, String listId, String directoryPath)
/*   31:     */     throws Exception, FileNotFoundException
/*   32:     */   {
/*   33:  20 */     Connection con = null;
/*   34:  21 */     PreparedStatement prepstat = null;
/*   35:  22 */     boolean bError = false;
/*   36:     */     
/*   37:  24 */     File contentDir = new File(directoryPath);
/*   38:  26 */     if (!contentDir.exists()) {
/*   39:  27 */       throw new FileNotFoundException("Unable to find work directory . Please set work directory in properties file");
/*   40:     */     }
/*   41:  32 */     int[] aiupdateCounts = null;
/*   42:     */     try
/*   43:     */     {
/*   44:  34 */       con = DConnect.getConnection();
/*   45:  35 */       con.setAutoCommit(false);
/*   46:     */       
/*   47:  37 */       String SQL = "update  uploads  set previewfile = ?, preview_exists =1 where id=? and list_id = ?";
/*   48:     */       
/*   49:  39 */       prepstat = con.prepareStatement(SQL);
/*   50:  40 */       prepstat.clearBatch();
/*   51:  41 */       for (Iterator iter = fileRefrences.iterator(); iter.hasNext();)
/*   52:     */       {
/*   53:  42 */         String id = (String)iter.next();
/*   54:     */         
/*   55:  44 */         File blob = new File(contentDir.getAbsolutePath() + File.separator + id);
/*   56:     */         
/*   57:  46 */         BufferedInputStream bis = new BufferedInputStream(new FileInputStream(blob));
/*   58:     */         
/*   59:     */ 
/*   60:     */ 
/*   61:  50 */         byte[] buff = new byte[new Long(blob.length()).intValue()];
/*   62:  51 */         int bytesRead = 0;
/*   63:  54 */         while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {}
/*   64:  57 */         prepstat.setBytes(1, buff);
/*   65:  58 */         prepstat.setString(2, id);
/*   66:  59 */         prepstat.setString(3, listId);
/*   67:  60 */         prepstat.addBatch();
/*   68:     */         
/*   69:  62 */         blob.delete();
/*   70:     */       }
/*   71:  64 */       aiupdateCounts = prepstat.executeBatch();
/*   72:     */       
/*   73:  66 */       SQL = "update content_list set preview_exists=1 where id =? and list_id =? ";
/*   74:     */       
/*   75:     */ 
/*   76:  69 */       prepstat = con.prepareStatement(SQL);
/*   77:  70 */       for (Iterator iter = fileRefrences.iterator(); iter.hasNext();)
/*   78:     */       {
/*   79:  71 */         String id = (String)iter.next();
/*   80:  72 */         prepstat.setString(1, id);
/*   81:  73 */         prepstat.setString(2, listId);
/*   82:  74 */         prepstat.addBatch();
/*   83:     */       }
/*   84:  77 */       aiupdateCounts = prepstat.executeBatch();
/*   85:     */     }
/*   86:     */     catch (Exception ex)
/*   87:     */     {
/*   88:  79 */       if (con != null) {
/*   89:  80 */         con.close();
/*   90:     */       }
/*   91:  82 */       throw new Exception(ex.getMessage());
/*   92:     */     }
/*   93:     */     finally
/*   94:     */     {
/*   95:  88 */       for (int i = 0; i < aiupdateCounts.length; i++)
/*   96:     */       {
/*   97:  89 */         int iProcessed = aiupdateCounts[i];
/*   98:  90 */         if ((iProcessed > 0) || (iProcessed == -2))
/*   99:     */         {
/*  100:  94 */           System.out.println("Update Sucessful");
/*  101:     */         }
/*  102:     */         else
/*  103:     */         {
/*  104:  97 */           bError = true;
/*  105:  98 */           break;
/*  106:     */         }
/*  107:     */       }
/*  108: 102 */       if (bError) {
/*  109: 103 */         con.rollback();
/*  110:     */       } else {
/*  111: 105 */         con.commit();
/*  112:     */       }
/*  113: 108 */       if (con != null) {
/*  114: 109 */         con.close();
/*  115:     */       }
/*  116:     */     }
/*  117:     */   }
/*  118:     */   
/*  119:     */   public void createcontent_list(String id, String title, Integer type, String download_url, String preview_url, Long size, Integer formats, Long price, String list_id, Integer category, Timestamp date_added, String other_details, boolean isLocal, boolean canList, String author, boolean isFree, String supplierId, String shortItemRef)
/*  120:     */     throws Exception
/*  121:     */   {
/*  122: 122 */     ResultSet rs = null;
/*  123: 123 */     Connection con = null;
/*  124: 124 */     PreparedStatement prepstat = null;
/*  125:     */     try
/*  126:     */     {
/*  127: 126 */       con = DConnect.getConnection();
/*  128: 127 */       String SQL = "insert into content_list(id,title,content_type,download_url,preview_url,size,formats,price,list_id,category,date_added,other_details,author,isLocal,show,is_free,supplier_id,short_item_ref) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
/*  129:     */       
/*  130: 129 */       prepstat = con.prepareStatement(SQL);
/*  131:     */       
/*  132: 131 */       prepstat.setString(1, id);
/*  133: 132 */       prepstat.setString(2, title);
/*  134: 133 */       prepstat.setInt(3, type.intValue());
/*  135: 134 */       prepstat.setString(4, download_url);
/*  136: 135 */       prepstat.setString(5, preview_url);
/*  137: 136 */       prepstat.setInt(6, size.intValue());
/*  138: 137 */       prepstat.setInt(7, formats.intValue());
/*  139: 138 */       prepstat.setString(8, price.toString());
/*  140: 139 */       prepstat.setString(9, list_id);
/*  141: 140 */       prepstat.setInt(10, category.intValue());
/*  142: 141 */       prepstat.setTimestamp(11, date_added);
/*  143: 142 */       prepstat.setString(12, other_details);
/*  144: 143 */       prepstat.setString(13, author);
/*  145: 144 */       if (isLocal == true) {
/*  146: 145 */         prepstat.setInt(14, 1);
/*  147:     */       } else {
/*  148: 147 */         prepstat.setInt(14, 0);
/*  149:     */       }
/*  150: 149 */       if (canList == true) {
/*  151: 150 */         prepstat.setInt(15, 1);
/*  152:     */       } else {
/*  153: 152 */         prepstat.setInt(15, 0);
/*  154:     */       }
/*  155: 154 */       if (isFree == true) {
/*  156: 155 */         prepstat.setInt(16, 1);
/*  157:     */       } else {
/*  158: 157 */         prepstat.setInt(16, 0);
/*  159:     */       }
/*  160: 159 */       prepstat.setString(17, supplierId);
/*  161: 160 */       prepstat.setString(18, shortItemRef);
/*  162:     */       
/*  163: 162 */       prepstat.execute();
/*  164:     */     }
/*  165:     */     catch (Exception ex)
/*  166:     */     {
/*  167: 165 */       if (con != null) {
/*  168: 166 */         con.close();
/*  169:     */       }
/*  170: 168 */       throw new Exception(ex.getMessage());
/*  171:     */     }
/*  172: 170 */     if (con != null) {
/*  173: 171 */       con.close();
/*  174:     */     }
/*  175:     */   }
/*  176:     */   
/*  177:     */   public void updateContentList(String[] id, Integer category, String listId)
/*  178:     */     throws Exception
/*  179:     */   {
/*  180: 178 */     ResultSet rs = null;
/*  181: 179 */     Connection con = null;
/*  182: 180 */     PreparedStatement prepstat = null;
/*  183: 181 */     boolean bError = false;
/*  184:     */     
/*  185: 183 */     int[] aiupdateCounts = null;
/*  186:     */     try
/*  187:     */     {
/*  188: 185 */       con = DConnect.getConnection();
/*  189: 186 */       con.setAutoCommit(false);
/*  190:     */       
/*  191: 188 */       String SQL = "update content_list set category=?  where id=? and list_id=?";
/*  192:     */       
/*  193: 190 */       prepstat = con.prepareStatement(SQL);
/*  194: 191 */       prepstat.clearBatch();
/*  195: 192 */       for (int i = 0; i < id.length; i++)
/*  196:     */       {
/*  197: 194 */         prepstat.setInt(1, category.intValue());
/*  198: 195 */         prepstat.setString(2, id[i]);
/*  199: 196 */         prepstat.setString(3, listId);
/*  200: 197 */         prepstat.addBatch();
/*  201:     */       }
/*  202: 200 */       aiupdateCounts = prepstat.executeBatch();
/*  203:     */     }
/*  204:     */     catch (Exception ex)
/*  205:     */     {
/*  206: 202 */       if (con != null) {
/*  207: 203 */         con.close();
/*  208:     */       }
/*  209: 205 */       throw new Exception(ex.getMessage());
/*  210:     */     }
/*  211:     */     finally
/*  212:     */     {
/*  213: 211 */       for (int i = 0; i < aiupdateCounts.length; i++)
/*  214:     */       {
/*  215: 212 */         int iProcessed = aiupdateCounts[i];
/*  216: 213 */         if ((iProcessed > 0) || (iProcessed == -2))
/*  217:     */         {
/*  218: 217 */           System.out.println("Update Sucessful");
/*  219:     */         }
/*  220:     */         else
/*  221:     */         {
/*  222: 220 */           bError = true;
/*  223: 221 */           break;
/*  224:     */         }
/*  225:     */       }
/*  226: 225 */       if (bError) {
/*  227: 226 */         con.rollback();
/*  228:     */       } else {
/*  229: 228 */         con.commit();
/*  230:     */       }
/*  231:     */     }
/*  232: 232 */     if (con != null) {
/*  233: 233 */       con.close();
/*  234:     */     }
/*  235:     */   }
/*  236:     */   
/*  237:     */   public void updateContentList(String[] id, String price, String listId)
/*  238:     */     throws Exception
/*  239:     */   {
/*  240: 241 */     ResultSet rs = null;
/*  241: 242 */     Connection con = null;
/*  242: 243 */     PreparedStatement prepstat = null;
/*  243: 244 */     boolean bError = false;
/*  244:     */     
/*  245: 246 */     int[] aiupdateCounts = null;
/*  246:     */     try
/*  247:     */     {
/*  248: 248 */       con = DConnect.getConnection();
/*  249: 249 */       con.setAutoCommit(false);
/*  250:     */       
/*  251: 251 */       String SQL = "update content_list set price=?  where id=? and list_id=?";
/*  252: 252 */       prepstat = con.prepareStatement(SQL);
/*  253: 253 */       prepstat.clearBatch();
/*  254: 254 */       for (int i = 0; i < id.length; i++)
/*  255:     */       {
/*  256: 256 */         prepstat.setString(1, price);
/*  257: 257 */         prepstat.setString(2, id[i]);
/*  258: 258 */         prepstat.setString(3, listId);
/*  259: 259 */         prepstat.addBatch();
/*  260:     */       }
/*  261: 262 */       aiupdateCounts = prepstat.executeBatch();
/*  262:     */     }
/*  263:     */     catch (Exception ex)
/*  264:     */     {
/*  265: 264 */       if (con != null) {
/*  266: 265 */         con.close();
/*  267:     */       }
/*  268: 267 */       throw new Exception(ex.getMessage());
/*  269:     */     }
/*  270:     */     finally
/*  271:     */     {
/*  272: 273 */       for (int i = 0; i < aiupdateCounts.length; i++)
/*  273:     */       {
/*  274: 274 */         int iProcessed = aiupdateCounts[i];
/*  275: 275 */         if ((iProcessed > 0) || (iProcessed == -2))
/*  276:     */         {
/*  277: 279 */           System.out.println("Update Sucessful");
/*  278:     */         }
/*  279:     */         else
/*  280:     */         {
/*  281: 282 */           bError = true;
/*  282: 283 */           break;
/*  283:     */         }
/*  284:     */       }
/*  285: 287 */       if (bError) {
/*  286: 288 */         con.rollback();
/*  287:     */       } else {
/*  288: 290 */         con.commit();
/*  289:     */       }
/*  290:     */     }
/*  291: 294 */     if (con != null) {
/*  292: 295 */       con.close();
/*  293:     */     }
/*  294:     */   }
/*  295:     */   
/*  296:     */   public void updateContentList(String[] id, String[] price, String listId)
/*  297:     */     throws Exception
/*  298:     */   {
/*  299: 303 */     ResultSet rs = null;
/*  300: 304 */     Connection con = null;
/*  301: 305 */     PreparedStatement prepstat = null;
/*  302: 306 */     boolean bError = false;
/*  303:     */     
/*  304: 308 */     String string = "";
/*  305: 309 */     if ((price != null) || (price.length > 0))
/*  306:     */     {
/*  307: 310 */       string = price[0] + ",";
/*  308: 311 */       for (int i = 1; i < price.length; i++) {
/*  309: 312 */         string = string + price[i] + ",";
/*  310:     */       }
/*  311:     */     }
/*  312: 315 */     string = string.substring(0, string.lastIndexOf(","));
/*  313:     */     
/*  314: 317 */     int[] aiupdateCounts = null;
/*  315:     */     try
/*  316:     */     {
/*  317: 319 */       con = DConnect.getConnection();
/*  318: 320 */       con.setAutoCommit(false);
/*  319:     */       
/*  320: 322 */       String SQL = "update content_list set price=?  where id=? and list_id=?";
/*  321: 323 */       prepstat = con.prepareStatement(SQL);
/*  322: 324 */       prepstat.clearBatch();
/*  323: 325 */       for (int i = 0; i < id.length; i++)
/*  324:     */       {
/*  325: 327 */         prepstat.setString(1, string);
/*  326: 328 */         prepstat.setString(2, id[i]);
/*  327: 329 */         prepstat.setString(3, listId);
/*  328: 330 */         prepstat.addBatch();
/*  329:     */       }
/*  330: 333 */       aiupdateCounts = prepstat.executeBatch();
/*  331:     */     }
/*  332:     */     catch (Exception ex)
/*  333:     */     {
/*  334: 335 */       if (con != null) {
/*  335: 336 */         con.close();
/*  336:     */       }
/*  337: 338 */       throw new Exception(ex.getMessage());
/*  338:     */     }
/*  339:     */     finally
/*  340:     */     {
/*  341: 344 */       for (int i = 0; i < aiupdateCounts.length; i++)
/*  342:     */       {
/*  343: 345 */         int iProcessed = aiupdateCounts[i];
/*  344: 346 */         if ((iProcessed > 0) || (iProcessed == -2))
/*  345:     */         {
/*  346: 350 */           System.out.println("Update Sucessful");
/*  347:     */         }
/*  348:     */         else
/*  349:     */         {
/*  350: 353 */           bError = true;
/*  351: 354 */           break;
/*  352:     */         }
/*  353:     */       }
/*  354: 358 */       if (bError) {
/*  355: 359 */         con.rollback();
/*  356:     */       } else {
/*  357: 361 */         con.commit();
/*  358:     */       }
/*  359:     */     }
/*  360: 365 */     if (con != null) {
/*  361: 366 */       con.close();
/*  362:     */     }
/*  363:     */   }
/*  364:     */   
/*  365:     */   public void updateContentListAuthor(String[] id, String author, String listId)
/*  366:     */     throws Exception
/*  367:     */   {
/*  368: 374 */     ResultSet rs = null;
/*  369: 375 */     Connection con = null;
/*  370: 376 */     PreparedStatement prepstat = null;
/*  371: 377 */     boolean bError = false;
/*  372:     */     
/*  373: 379 */     int[] aiupdateCounts = null;
/*  374:     */     try
/*  375:     */     {
/*  376: 381 */       con = DConnect.getConnection();
/*  377: 382 */       con.setAutoCommit(false);
/*  378:     */       
/*  379:     */ 
/*  380:     */ 
/*  381: 386 */       String SQL = "update content_list set author=?  where id=? and list_id=?";
/*  382:     */       
/*  383: 388 */       prepstat = con.prepareStatement(SQL);
/*  384: 389 */       prepstat.clearBatch();
/*  385: 390 */       for (int i = 0; i < id.length; i++)
/*  386:     */       {
/*  387: 392 */         prepstat.setString(1, author);
/*  388: 393 */         prepstat.setString(2, id[i]);
/*  389: 394 */         prepstat.setString(3, listId);
/*  390: 395 */         prepstat.addBatch();
/*  391:     */       }
/*  392: 398 */       aiupdateCounts = prepstat.executeBatch();
/*  393:     */     }
/*  394:     */     catch (Exception ex)
/*  395:     */     {
/*  396: 400 */       if (con != null) {
/*  397: 401 */         con.close();
/*  398:     */       }
/*  399: 403 */       throw new Exception(ex.getMessage());
/*  400:     */     }
/*  401:     */     finally
/*  402:     */     {
/*  403: 409 */       for (int i = 0; i < aiupdateCounts.length; i++)
/*  404:     */       {
/*  405: 410 */         int iProcessed = aiupdateCounts[i];
/*  406: 411 */         if ((iProcessed > 0) || (iProcessed == -2))
/*  407:     */         {
/*  408: 415 */           System.out.println("Update Sucessful");
/*  409:     */         }
/*  410:     */         else
/*  411:     */         {
/*  412: 418 */           bError = true;
/*  413: 419 */           break;
/*  414:     */         }
/*  415:     */       }
/*  416: 423 */       if (bError) {
/*  417: 424 */         con.rollback();
/*  418:     */       } else {
/*  419: 426 */         con.commit();
/*  420:     */       }
/*  421:     */     }
/*  422: 430 */     if (con != null) {
/*  423: 431 */       con.close();
/*  424:     */     }
/*  425:     */   }
/*  426:     */   
/*  427:     */   public void updateContentListFreebie(String[] id, boolean isFree, String listId)
/*  428:     */     throws Exception
/*  429:     */   {
/*  430: 439 */     ResultSet rs = null;
/*  431: 440 */     Connection con = null;
/*  432: 441 */     PreparedStatement prepstat = null;
/*  433: 442 */     boolean bError = false;
/*  434:     */     
/*  435: 444 */     int[] aiupdateCounts = null;
/*  436:     */     try
/*  437:     */     {
/*  438: 446 */       con = DConnect.getConnection();
/*  439: 447 */       con.setAutoCommit(false);
/*  440:     */       
/*  441: 449 */       String SQL = "update content_list set is_free=?  where id=? and list_id=?";
/*  442: 450 */       prepstat = con.prepareStatement(SQL);
/*  443: 451 */       prepstat.clearBatch();
/*  444: 452 */       for (int i = 0; i < id.length; i++)
/*  445:     */       {
/*  446: 453 */         if (isFree == true) {
/*  447: 454 */           prepstat.setInt(1, 1);
/*  448:     */         } else {
/*  449: 456 */           prepstat.setInt(1, 0);
/*  450:     */         }
/*  451: 458 */         prepstat.setString(2, id[i]);
/*  452: 459 */         prepstat.setString(3, listId);
/*  453: 460 */         prepstat.addBatch();
/*  454:     */       }
/*  455: 463 */       aiupdateCounts = prepstat.executeBatch();
/*  456:     */     }
/*  457:     */     catch (Exception ex)
/*  458:     */     {
/*  459: 465 */       if (con != null) {
/*  460: 466 */         con.close();
/*  461:     */       }
/*  462: 468 */       throw new Exception(ex.getMessage());
/*  463:     */     }
/*  464:     */     finally
/*  465:     */     {
/*  466: 474 */       for (int i = 0; i < aiupdateCounts.length; i++)
/*  467:     */       {
/*  468: 475 */         int iProcessed = aiupdateCounts[i];
/*  469: 476 */         if ((iProcessed > 0) || (iProcessed == -2))
/*  470:     */         {
/*  471: 480 */           System.out.println("Update Sucessful");
/*  472:     */         }
/*  473:     */         else
/*  474:     */         {
/*  475: 483 */           bError = true;
/*  476: 484 */           break;
/*  477:     */         }
/*  478:     */       }
/*  479: 488 */       if (bError) {
/*  480: 489 */         con.rollback();
/*  481:     */       } else {
/*  482: 491 */         con.commit();
/*  483:     */       }
/*  484:     */     }
/*  485: 495 */     if (con != null) {
/*  486: 496 */       con.close();
/*  487:     */     }
/*  488:     */   }
/*  489:     */   
/*  490:     */   public void updateShortItemReference(String[] id, String listId, String shortItemRef)
/*  491:     */     throws Exception
/*  492:     */   {
/*  493: 504 */     ResultSet rs = null;
/*  494: 505 */     Connection con = null;
/*  495: 506 */     PreparedStatement prepstat = null;
/*  496: 507 */     boolean bError = false;
/*  497:     */     
/*  498: 509 */     int[] aiupdateCounts = null;
/*  499:     */     try
/*  500:     */     {
/*  501: 511 */       con = DConnect.getConnection();
/*  502: 512 */       con.setAutoCommit(false);
/*  503:     */       
/*  504: 514 */       String SQL = "update content_list set short_item_ref=? where id=? and list_id=?";
/*  505: 515 */       prepstat = con.prepareStatement(SQL);
/*  506: 516 */       prepstat.clearBatch();
/*  507: 517 */       for (int i = 0; i < id.length; i++)
/*  508:     */       {
/*  509: 518 */         prepstat.setString(1, shortItemRef);
/*  510: 519 */         prepstat.setString(2, id[i]);
/*  511: 520 */         prepstat.setString(3, listId);
/*  512: 521 */         prepstat.addBatch();
/*  513:     */       }
/*  514: 524 */       aiupdateCounts = prepstat.executeBatch();
/*  515:     */     }
/*  516:     */     catch (Exception ex)
/*  517:     */     {
/*  518: 526 */       if (con != null) {
/*  519: 527 */         con.close();
/*  520:     */       }
/*  521: 529 */       throw new Exception(ex.getMessage());
/*  522:     */     }
/*  523:     */     finally
/*  524:     */     {
/*  525: 535 */       for (int i = 0; i < aiupdateCounts.length; i++)
/*  526:     */       {
/*  527: 536 */         int iProcessed = aiupdateCounts[i];
/*  528: 537 */         if ((iProcessed > 0) || (iProcessed == -2))
/*  529:     */         {
/*  530: 541 */           System.out.println("Update Sucessful");
/*  531:     */         }
/*  532:     */         else
/*  533:     */         {
/*  534: 544 */           bError = true;
/*  535: 545 */           break;
/*  536:     */         }
/*  537:     */       }
/*  538: 549 */       if (bError) {
/*  539: 550 */         con.rollback();
/*  540:     */       } else {
/*  541: 552 */         con.commit();
/*  542:     */       }
/*  543:     */     }
/*  544: 556 */     if (con != null) {
/*  545: 557 */       con.close();
/*  546:     */     }
/*  547:     */   }
/*  548:     */   
/*  549:     */   public void updateContentList(String[] id, String price, Integer category, String author, String listId, boolean isFree)
/*  550:     */     throws Exception
/*  551:     */   {
/*  552: 566 */     ResultSet rs = null;
/*  553: 567 */     Connection con = null;
/*  554: 568 */     PreparedStatement prepstat = null;
/*  555: 569 */     boolean bError = false;
/*  556:     */     
/*  557: 571 */     int[] aiupdateCounts = null;
/*  558:     */     try
/*  559:     */     {
/*  560: 574 */       con = DConnect.getConnection();
/*  561: 575 */       con.setAutoCommit(false);
/*  562:     */       
/*  563: 577 */       String SQL = "update content_list set price=?,category=?,author=? where id=? and list_id=?";
/*  564:     */       
/*  565:     */ 
/*  566: 580 */       prepstat = con.prepareStatement(SQL);
/*  567: 581 */       prepstat.clearBatch();
/*  568: 582 */       for (int i = 0; i < id.length; i++)
/*  569:     */       {
/*  570: 584 */         prepstat.setString(1, price);
/*  571: 585 */         prepstat.setInt(2, category.intValue());
/*  572: 586 */         prepstat.setString(3, author);
/*  573: 587 */         prepstat.setString(4, id[i]);
/*  574: 588 */         prepstat.setString(5, listId);
/*  575: 589 */         prepstat.addBatch();
/*  576:     */       }
/*  577: 592 */       aiupdateCounts = prepstat.executeBatch();
/*  578:     */     }
/*  579:     */     catch (Exception ex)
/*  580:     */     {
/*  581: 594 */       if (con != null) {
/*  582: 595 */         con.close();
/*  583:     */       }
/*  584: 597 */       throw new Exception(ex.getMessage());
/*  585:     */     }
/*  586:     */     finally
/*  587:     */     {
/*  588: 603 */       for (int i = 0; i < aiupdateCounts.length; i++)
/*  589:     */       {
/*  590: 604 */         int iProcessed = aiupdateCounts[i];
/*  591: 605 */         if ((iProcessed > 0) || (iProcessed == -2))
/*  592:     */         {
/*  593: 609 */           System.out.println("Update Sucessful");
/*  594:     */         }
/*  595:     */         else
/*  596:     */         {
/*  597: 612 */           bError = true;
/*  598: 613 */           break;
/*  599:     */         }
/*  600:     */       }
/*  601: 617 */       if (bError) {
/*  602: 618 */         con.rollback();
/*  603:     */       } else {
/*  604: 620 */         con.commit();
/*  605:     */       }
/*  606:     */     }
/*  607: 624 */     if (con != null) {
/*  608: 625 */       con.close();
/*  609:     */     }
/*  610:     */   }
/*  611:     */   
/*  612:     */   public void updateContentList(String[] id, String price, Integer category, String author, String listId, boolean isFree, String shortItemRef)
/*  613:     */     throws Exception
/*  614:     */   {
/*  615: 634 */     ResultSet rs = null;
/*  616: 635 */     Connection con = null;
/*  617: 636 */     PreparedStatement prepstat = null;
/*  618: 637 */     boolean bError = false;
/*  619:     */     
/*  620: 639 */     int[] aiupdateCounts = null;
/*  621:     */     try
/*  622:     */     {
/*  623: 642 */       con = DConnect.getConnection();
/*  624: 643 */       con.setAutoCommit(false);
/*  625:     */       
/*  626: 645 */       String SQL = "update content_list set price=?,category=?,author=?,short_item_ref=? where id=? and list_id=?";
/*  627:     */       
/*  628:     */ 
/*  629: 648 */       prepstat = con.prepareStatement(SQL);
/*  630: 649 */       prepstat.clearBatch();
/*  631: 650 */       for (int i = 0; i < id.length; i++)
/*  632:     */       {
/*  633: 652 */         prepstat.setString(1, price);
/*  634: 653 */         prepstat.setInt(2, category.intValue());
/*  635: 654 */         prepstat.setString(3, author);
/*  636: 655 */         prepstat.setString(4, shortItemRef);
/*  637: 656 */         prepstat.setString(5, id[i]);
/*  638: 657 */         prepstat.setString(6, listId);
/*  639: 658 */         prepstat.addBatch();
/*  640:     */       }
/*  641: 661 */       aiupdateCounts = prepstat.executeBatch();
/*  642:     */     }
/*  643:     */     catch (Exception ex)
/*  644:     */     {
/*  645: 663 */       if (con != null) {
/*  646: 664 */         con.close();
/*  647:     */       }
/*  648: 666 */       throw new Exception(ex.getMessage());
/*  649:     */     }
/*  650:     */     finally
/*  651:     */     {
/*  652: 672 */       for (int i = 0; i < aiupdateCounts.length; i++)
/*  653:     */       {
/*  654: 673 */         int iProcessed = aiupdateCounts[i];
/*  655: 674 */         if ((iProcessed > 0) || (iProcessed == -2))
/*  656:     */         {
/*  657: 678 */           System.out.println("Update Sucessful");
/*  658:     */         }
/*  659:     */         else
/*  660:     */         {
/*  661: 681 */           bError = true;
/*  662: 682 */           break;
/*  663:     */         }
/*  664:     */       }
/*  665: 686 */       if (bError) {
/*  666: 687 */         con.rollback();
/*  667:     */       } else {
/*  668: 689 */         con.commit();
/*  669:     */       }
/*  670:     */     }
/*  671: 693 */     if (con != null) {
/*  672: 694 */       con.close();
/*  673:     */     }
/*  674:     */   }
/*  675:     */   
/*  676:     */   public void updateContentItem(String id, String price, String title, Integer category, String author, boolean isFree, String shortItemRef, String listId)
/*  677:     */     throws Exception
/*  678:     */   {
/*  679: 703 */     ResultSet rs = null;
/*  680: 704 */     Connection con = null;
/*  681: 705 */     PreparedStatement prepstat = null;
/*  682:     */     try
/*  683:     */     {
/*  684: 709 */       con = DConnect.getConnection();
/*  685:     */       
/*  686: 711 */       String SQL = "update content_list set price=?,category=?,author=?,title =?, is_free=?,short_item_ref=? where id=? and list_id=?";
/*  687: 712 */       prepstat = con.prepareStatement(SQL);
/*  688: 713 */       prepstat.setString(1, price);
/*  689: 714 */       prepstat.setInt(2, category.intValue());
/*  690: 715 */       prepstat.setString(3, author);
/*  691: 716 */       prepstat.setString(4, title);
/*  692: 717 */       if (isFree) {
/*  693: 718 */         prepstat.setInt(5, 1);
/*  694:     */       } else {
/*  695: 720 */         prepstat.setInt(5, 0);
/*  696:     */       }
/*  697: 722 */       prepstat.setString(6, shortItemRef);
/*  698: 723 */       prepstat.setString(7, id);
/*  699: 724 */       prepstat.setString(8, listId);
/*  700:     */       
/*  701: 726 */       prepstat.executeUpdate();
/*  702:     */     }
/*  703:     */     catch (Exception ex)
/*  704:     */     {
/*  705: 728 */       if (con != null) {
/*  706: 729 */         con.close();
/*  707:     */       }
/*  708: 731 */       throw new Exception(ex.getMessage());
/*  709:     */     }
/*  710:     */     finally
/*  711:     */     {
/*  712: 734 */       if (con != null) {
/*  713: 735 */         con.close();
/*  714:     */       }
/*  715:     */     }
/*  716:     */   }
/*  717:     */   
/*  718:     */   public void updateContentItem(String id, String price, Integer category, String author, String listId)
/*  719:     */     throws Exception
/*  720:     */   {
/*  721: 744 */     ResultSet rs = null;
/*  722: 745 */     Connection con = null;
/*  723: 746 */     PreparedStatement prepstat = null;
/*  724:     */     try
/*  725:     */     {
/*  726: 750 */       con = DConnect.getConnection();
/*  727:     */       
/*  728: 752 */       String SQL = "update content_list set price=?,category=?,author=?  where id=? and list_id=?";
/*  729:     */       
/*  730: 754 */       prepstat = con.prepareStatement(SQL);
/*  731: 755 */       prepstat.setString(1, price);
/*  732: 756 */       prepstat.setInt(2, category.intValue());
/*  733: 757 */       prepstat.setString(3, author);
/*  734: 758 */       prepstat.setString(4, id);
/*  735: 759 */       prepstat.setString(5, listId);
/*  736:     */       
/*  737: 761 */       prepstat.executeUpdate();
/*  738:     */     }
/*  739:     */     catch (Exception ex)
/*  740:     */     {
/*  741: 763 */       if (con != null) {
/*  742: 764 */         con.close();
/*  743:     */       }
/*  744: 766 */       throw new Exception(ex.getMessage());
/*  745:     */     }
/*  746:     */     finally
/*  747:     */     {
/*  748: 769 */       if (con != null) {
/*  749: 770 */         con.close();
/*  750:     */       }
/*  751:     */     }
/*  752:     */   }
/*  753:     */   
/*  754:     */   public void updatecontent_list(String id, String title, Integer type, String download_url, String preview_url, Integer size, Integer formats, Long price, Integer category, Timestamp date_added, String other_details, String author, boolean isLocal, boolean canList, String list_id)
/*  755:     */     throws Exception
/*  756:     */   {
/*  757: 781 */     ResultSet rs = null;
/*  758: 782 */     Connection con = null;
/*  759: 783 */     PreparedStatement prepstat = null;
/*  760:     */     try
/*  761:     */     {
/*  762: 785 */       con = DConnect.getConnection();
/*  763:     */       
/*  764: 787 */       String SQL = "update content_list set title=?,content_type=?,download_url=?,preview_url=?,size=?,formats=?,price=?,category=?,date_added=?,other_details=?,author=?,id=?,list_id=?,isLocal=? and show?";
/*  765: 788 */       prepstat = con.prepareStatement(SQL);
/*  766:     */       
/*  767: 790 */       prepstat.setString(1, title);
/*  768: 791 */       prepstat.setInt(2, type.intValue());
/*  769: 792 */       prepstat.setString(3, download_url);
/*  770: 793 */       prepstat.setString(4, preview_url);
/*  771: 794 */       prepstat.setInt(5, size.intValue());
/*  772: 795 */       prepstat.setInt(6, formats.intValue());
/*  773: 796 */       prepstat.setString(7, price.toString());
/*  774: 797 */       prepstat.setInt(8, category.intValue());
/*  775: 798 */       prepstat.setTimestamp(9, date_added);
/*  776: 799 */       prepstat.setString(10, other_details);
/*  777: 800 */       prepstat.setString(11, author);
/*  778: 801 */       prepstat.setString(12, id);
/*  779: 802 */       prepstat.setString(13, list_id);
/*  780: 803 */       if (isLocal == true) {
/*  781: 804 */         prepstat.setInt(14, 1);
/*  782:     */       } else {
/*  783: 806 */         prepstat.setInt(14, 0);
/*  784:     */       }
/*  785: 808 */       if (canList == true) {
/*  786: 809 */         prepstat.setInt(15, 1);
/*  787:     */       } else {
/*  788: 811 */         prepstat.setInt(15, 0);
/*  789:     */       }
/*  790: 814 */       prepstat.execute();
/*  791:     */     }
/*  792:     */     catch (Exception ex)
/*  793:     */     {
/*  794: 816 */       if (con != null) {
/*  795: 817 */         con.close();
/*  796:     */       }
/*  797: 819 */       throw new Exception(ex.getMessage());
/*  798:     */     }
/*  799: 821 */     if (con != null) {
/*  800: 822 */       con.close();
/*  801:     */     }
/*  802:     */   }
/*  803:     */   
/*  804:     */   public void deleteContentList(String list_id)
/*  805:     */     throws Exception
/*  806:     */   {
/*  807: 829 */     ResultSet rs = null;
/*  808: 830 */     Connection con = null;
/*  809: 831 */     PreparedStatement prepstat = null;
/*  810:     */     try
/*  811:     */     {
/*  812: 833 */       con = DConnect.getConnection();
/*  813: 834 */       String SQL = "delete  from content_list where list_id=?";
/*  814: 835 */       prepstat = con.prepareStatement(SQL);
/*  815:     */       
/*  816: 837 */       prepstat.setString(1, list_id);
/*  817: 838 */       prepstat.execute();
/*  818:     */     }
/*  819:     */     catch (Exception ex)
/*  820:     */     {
/*  821: 840 */       if (con != null) {
/*  822: 841 */         con.close();
/*  823:     */       }
/*  824: 843 */       throw new Exception(ex.getMessage());
/*  825:     */     }
/*  826: 845 */     if (con != null) {
/*  827: 846 */       con.close();
/*  828:     */     }
/*  829:     */   }
/*  830:     */   
/*  831:     */   public void deleteContentListItem(String Id, String listId)
/*  832:     */     throws Exception
/*  833:     */   {
/*  834: 853 */     Connection con = null;
/*  835: 854 */     Statement prepstat = null;
/*  836: 855 */     int[] aiupdateCounts = null;
/*  837: 856 */     boolean bError = false;
/*  838:     */     try
/*  839:     */     {
/*  840: 859 */       con = DConnect.getConnection();
/*  841: 860 */       con.setAutoCommit(false);
/*  842:     */       
/*  843: 862 */       String SQL1 = "delete  from content_list where id='" + Id + "' and list_id ='" + listId + "'";
/*  844:     */       
/*  845: 864 */       String SQL2 = "delete from uploads where id='" + Id + "' and list_id  ='" + listId + "'";
/*  846:     */       
/*  847:     */ 
/*  848: 867 */       con.createStatement();
/*  849: 868 */       prepstat = con.createStatement();
/*  850:     */       
/*  851: 870 */       bError = false;
/*  852: 871 */       prepstat.clearBatch();
/*  853:     */       
/*  854:     */ 
/*  855: 874 */       prepstat.addBatch(SQL1);
/*  856: 875 */       prepstat.addBatch(SQL2);
/*  857:     */       
/*  858:     */ 
/*  859: 878 */       aiupdateCounts = prepstat.executeBatch();
/*  860:     */     }
/*  861:     */     catch (BatchUpdateException bue)
/*  862:     */     {
/*  863: 883 */       bError = true;
/*  864: 884 */       aiupdateCounts = bue.getUpdateCounts();
/*  865:     */       
/*  866: 886 */       SQLException SQLe = bue;
/*  867: 887 */       while (SQLe != null) {
/*  868: 889 */         SQLe = SQLe.getNextException();
/*  869:     */       }
/*  870:     */     }
/*  871:     */     catch (SQLException SQLe)
/*  872:     */     {
/*  873: 893 */       System.out.println("there was an error during the update");
/*  874:     */     }
/*  875:     */     finally
/*  876:     */     {
/*  877: 897 */       for (int i = 0; i < aiupdateCounts.length; i++)
/*  878:     */       {
/*  879: 898 */         int iProcessed = aiupdateCounts[i];
/*  880: 899 */         if ((iProcessed > 0) || (iProcessed == -2))
/*  881:     */         {
/*  882: 903 */           System.out.println("Delete sucessful");
/*  883:     */         }
/*  884:     */         else
/*  885:     */         {
/*  886: 906 */           bError = true;
/*  887: 907 */           break;
/*  888:     */         }
/*  889:     */       }
/*  890: 911 */       if (bError) {
/*  891: 912 */         con.rollback();
/*  892:     */       } else {
/*  893: 914 */         con.commit();
/*  894:     */       }
/*  895: 916 */       if (con != null) {
/*  896: 917 */         con.close();
/*  897:     */       }
/*  898:     */     }
/*  899:     */   }
/*  900:     */   
/*  901:     */   public void deleteContentListItem(String[] Id, String listId)
/*  902:     */     throws Exception
/*  903:     */   {
/*  904: 925 */     Connection con = null;
/*  905: 926 */     Statement prepstat = null;
/*  906: 927 */     int[] aiupdateCounts = null;
/*  907: 928 */     boolean bError = false;
/*  908:     */     try
/*  909:     */     {
/*  910: 932 */       con = DConnect.getConnection();
/*  911: 933 */       con.setAutoCommit(false);
/*  912: 934 */       con.createStatement();
/*  913: 935 */       prepstat = con.createStatement();
/*  914: 937 */       for (int i = 0; i < Id.length; i++)
/*  915:     */       {
/*  916: 938 */         String SQL1 = "delete  from content_list where id='" + Id[i] + "' and list_id ='" + listId + "'";
/*  917:     */         
/*  918: 940 */         String SQL2 = "delete from uploads where id='" + Id[i] + "' and list_id  ='" + listId + "'";
/*  919:     */         
/*  920: 942 */         bError = false;
/*  921:     */         
/*  922:     */ 
/*  923:     */ 
/*  924: 946 */         prepstat.addBatch(SQL1);
/*  925: 947 */         prepstat.addBatch(SQL2);
/*  926:     */       }
/*  927: 951 */       aiupdateCounts = prepstat.executeBatch();
/*  928: 952 */       prepstat.clearBatch();
/*  929:     */     }
/*  930:     */     catch (BatchUpdateException bue)
/*  931:     */     {
/*  932: 957 */       bError = true;
/*  933: 958 */       aiupdateCounts = bue.getUpdateCounts();
/*  934:     */       
/*  935: 960 */       SQLException SQLe = bue;
/*  936: 961 */       while (SQLe != null) {
/*  937: 963 */         SQLe = SQLe.getNextException();
/*  938:     */       }
/*  939:     */     }
/*  940:     */     catch (SQLException SQLe)
/*  941:     */     {
/*  942: 967 */       System.out.println("there was an error during the update");
/*  943:     */     }
/*  944:     */     finally
/*  945:     */     {
/*  946: 971 */       for (int i = 0; i < aiupdateCounts.length; i++)
/*  947:     */       {
/*  948: 972 */         int iProcessed = aiupdateCounts[i];
/*  949: 973 */         if ((iProcessed > 0) || (iProcessed == -2))
/*  950:     */         {
/*  951: 977 */           System.out.println("Delete sucessful");
/*  952:     */         }
/*  953:     */         else
/*  954:     */         {
/*  955: 980 */           bError = true;
/*  956: 981 */           break;
/*  957:     */         }
/*  958:     */       }
/*  959: 985 */       if (bError) {
/*  960: 986 */         con.rollback();
/*  961:     */       } else {
/*  962: 988 */         con.commit();
/*  963:     */       }
/*  964: 990 */       if (con != null) {
/*  965: 991 */         con.close();
/*  966:     */       }
/*  967:     */     }
/*  968:     */   }
/*  969:     */   
/*  970:     */   public void deleteContentListTmp(String list_id)
/*  971:     */     throws Exception
/*  972:     */   {
/*  973:1001 */     ResultSet rs = null;
/*  974:1002 */     Connection con = null;
/*  975:1003 */     PreparedStatement prepstat = null;
/*  976:     */     try
/*  977:     */     {
/*  978:1005 */       con = DConnect.getConnection();
/*  979:1006 */       String SQL = "delete  from content_list_tmp where list_id=?";
/*  980:1007 */       prepstat = con.prepareStatement(SQL);
/*  981:     */       
/*  982:1009 */       prepstat.setString(1, list_id);
/*  983:1010 */       prepstat.execute();
/*  984:     */     }
/*  985:     */     catch (Exception ex)
/*  986:     */     {
/*  987:1012 */       if (con != null) {
/*  988:1013 */         con.close();
/*  989:     */       }
/*  990:1015 */       throw new Exception(ex.getMessage());
/*  991:     */     }
/*  992:1017 */     if (con != null) {
/*  993:1018 */       con.close();
/*  994:     */     }
/*  995:     */   }
/*  996:     */   
/*  997:     */   public ContentItem viewcontent_list(String id, String list_id)
/*  998:     */     throws Exception
/*  999:     */   {
/* 1000:1024 */     ContentItem item = new ContentItem();
/* 1001:1025 */     Format format = new Format();
/* 1002:1026 */     ContentType type = new ContentType();
/* 1003:1027 */     User cp = new User();
/* 1004:     */     
/* 1005:     */ 
/* 1006:     */ 
/* 1007:1031 */     ResultSet rs = null;
/* 1008:1032 */     Connection con = null;
/* 1009:1033 */     PreparedStatement prepstat = null;
/* 1010:     */     try
/* 1011:     */     {
/* 1012:1035 */       con = DConnect.getConnection();
/* 1013:     */       
/* 1014:     */ 
/* 1015:1038 */       String SQL = "SELECT * FROM content_list, service_route, cp_user, format_list where content_list.id=? and content_list.list_id=? and content_list.content_type=service_route.service_type and content_list.list_id=cp_user.id and content_list.formats=format_list.format_id";
/* 1016:     */       
/* 1017:     */ 
/* 1018:1041 */       prepstat = con.prepareStatement(SQL);
/* 1019:     */       
/* 1020:1043 */       prepstat.setString(1, id);
/* 1021:1044 */       prepstat.setString(2, list_id);
/* 1022:1045 */       rs = prepstat.executeQuery();
/* 1023:1046 */       while (rs.next())
/* 1024:     */       {
/* 1025:1048 */         item.setContentId(rs.getString("content_id"));
/* 1026:1049 */         item.setid(rs.getString("id"));
/* 1027:1050 */         item.settitle(rs.getString("title"));
/* 1028:1051 */         item.settype(new Integer(rs.getInt("content_type")));
/* 1029:1052 */         item.setdownload_url(rs.getString("download_url"));
/* 1030:1053 */         item.setPreviewUrl(rs.getString("preview_url"));
/* 1031:     */         
/* 1032:1055 */         item.setPrice(rs.getString("price"));
/* 1033:1056 */         item.setAuthor(rs.getString("author"));
/* 1034:1057 */         item.setCategory(new Integer(rs.getInt("category")));
/* 1035:1058 */         item.setSize(new Long(rs.getLong("size")));
/* 1036:1059 */         item.setListId(rs.getString("list_id"));
/* 1037:1060 */         item.setDate_Added(rs.getTimestamp("date_added"));
/* 1038:1061 */         item.setOther_Details(rs.getString("other_details"));
/* 1039:1062 */         if (rs.getInt("isLocal") == 1) {
/* 1040:1063 */           item.setIsLocal(true);
/* 1041:     */         } else {
/* 1042:1065 */           item.setIsLocal(false);
/* 1043:     */         }
/* 1044:1067 */         if (rs.getInt("show") == 1) {
/* 1045:1068 */           item.setCanList(true);
/* 1046:     */         } else {
/* 1047:1070 */           item.setCanList(false);
/* 1048:     */         }
/* 1049:1072 */         if (rs.getInt("is_free") == 1) {
/* 1050:1073 */           item.setFree(true);
/* 1051:     */         } else {
/* 1052:1075 */           item.setFree(false);
/* 1053:     */         }
/* 1054:1077 */         if (rs.getInt("preview_exists") == 1) {
/* 1055:1078 */           item.setPreviewExists(Boolean.valueOf(true));
/* 1056:     */         } else {
/* 1057:1080 */           item.setPreviewExists(Boolean.valueOf(false));
/* 1058:     */         }
/* 1059:1084 */         type.setParentServiceType(rs.getInt("service_route.parent_service_type"));
/* 1060:1085 */         type.setServiceName(rs.getString("service_route.service_name"));
/* 1061:1086 */         type.setServiceType(rs.getInt("service_route.service_type"));
/* 1062:     */         
/* 1063:     */ 
/* 1064:1089 */         cp.setId(rs.getString("cp_user.id"));
/* 1065:1090 */         cp.setName(rs.getString("cp_user.name"));
/* 1066:1091 */         cp.setUsername(rs.getString("cp_user.username"));
/* 1067:1092 */         cp.setPassword(rs.getString("cp_user.password"));
/* 1068:1093 */         cp.setDefaultSmsc(rs.getString("cp_user.default_smsc"));
/* 1069:1094 */         cp.setLogoUrl(rs.getString("cp_user.logo_url"));
/* 1070:1095 */         cp.setDefaultService(rs.getString("cp_user.default_service"));
/* 1071:     */         
/* 1072:     */ 
/* 1073:1098 */         format.setId(rs.getInt("format_list.format_id"));
/* 1074:1099 */         format.setFileExt(rs.getString("format_list.file_ext"));
/* 1075:1100 */         format.setMimeType(rs.getString("format_list.mime_type"));
/* 1076:1101 */         format.setPushBearer(rs.getString("format_list.push_bearer"));
/* 1077:     */       }
/* 1078:1103 */       item.setContentTypeDetails(type);
/* 1079:1104 */       item.setProviderDetails(cp);
/* 1080:1105 */       item.setFormat(format);
/* 1081:     */     }
/* 1082:     */     catch (Exception ex)
/* 1083:     */     {
/* 1084:1107 */       if (con != null) {
/* 1085:1108 */         con.close();
/* 1086:     */       }
/* 1087:1110 */       throw new Exception(ex.getMessage());
/* 1088:     */     }
/* 1089:1112 */     if (con != null) {
/* 1090:1113 */       con.close();
/* 1091:     */     }
/* 1092:1115 */     return item;
/* 1093:     */   }
/* 1094:     */   
/* 1095:     */   public ContentItem viewcontent_list(String contentId)
/* 1096:     */     throws Exception
/* 1097:     */   {
/* 1098:1120 */     ContentItem item = new ContentItem();
/* 1099:1121 */     ContentType type = new ContentType();
/* 1100:1122 */     Format format = new Format();
/* 1101:1123 */     User cp = new User();
/* 1102:     */     
/* 1103:     */ 
/* 1104:     */ 
/* 1105:1127 */     ResultSet rs = null;
/* 1106:1128 */     Connection con = null;
/* 1107:1129 */     PreparedStatement prepstat = null;
/* 1108:     */     try
/* 1109:     */     {
/* 1110:1131 */       con = DConnect.getConnection();
/* 1111:     */       
/* 1112:1133 */       String SQL = "SELECT * FROM content_list, service_route, cp_user, format_list where content_list.content_id=? and content_list.content_type=service_route.service_type and content_list.list_id=cp_user.id and content_list.formats=format_list.format_id";
/* 1113:     */       
/* 1114:     */ 
/* 1115:     */ 
/* 1116:1137 */       prepstat = con.prepareStatement(SQL);
/* 1117:     */       
/* 1118:1139 */       prepstat.setString(1, contentId);
/* 1119:1140 */       rs = prepstat.executeQuery();
/* 1120:1141 */       while (rs.next())
/* 1121:     */       {
/* 1122:1142 */         item.setContentId(rs.getString("content_id"));
/* 1123:1143 */         item.setid(rs.getString("id"));
/* 1124:1144 */         item.settitle(rs.getString("title"));
/* 1125:1145 */         item.settype(new Integer(rs.getInt("content_type")));
/* 1126:1146 */         item.setdownload_url(rs.getString("download_url"));
/* 1127:1147 */         item.setPreviewUrl(rs.getString("preview_url"));
/* 1128:     */         
/* 1129:1149 */         item.setPrice(rs.getString("price"));
/* 1130:1150 */         item.setAuthor(rs.getString("author"));
/* 1131:1151 */         item.setCategory(new Integer(rs.getInt("category")));
/* 1132:1152 */         item.setSize(new Long(rs.getLong("size")));
/* 1133:1153 */         item.setListId(rs.getString("list_id"));
/* 1134:1154 */         item.setDate_Added(rs.getTimestamp("date_added"));
/* 1135:1155 */         item.setOther_Details(rs.getString("other_details"));
/* 1136:1156 */         if (rs.getInt("isLocal") == 1) {
/* 1137:1157 */           item.setIsLocal(true);
/* 1138:     */         } else {
/* 1139:1159 */           item.setIsLocal(false);
/* 1140:     */         }
/* 1141:1161 */         if (rs.getInt("show") == 1) {
/* 1142:1162 */           item.setCanList(true);
/* 1143:     */         } else {
/* 1144:1164 */           item.setCanList(false);
/* 1145:     */         }
/* 1146:1166 */         if (rs.getInt("is_free") == 1) {
/* 1147:1167 */           item.setFree(true);
/* 1148:     */         } else {
/* 1149:1169 */           item.setFree(false);
/* 1150:     */         }
/* 1151:1173 */         type.setParentServiceType(rs.getInt("service_route.parent_service_type"));
/* 1152:1174 */         type.setServiceName(rs.getString("service_route.service_name"));
/* 1153:1175 */         type.setServiceType(rs.getInt("service_route.service_type"));
/* 1154:     */         
/* 1155:     */ 
/* 1156:1178 */         cp.setId(rs.getString("cp_user.id"));
/* 1157:1179 */         cp.setName(rs.getString("cp_user.name"));
/* 1158:1180 */         cp.setUsername(rs.getString("cp_user.username"));
/* 1159:1181 */         cp.setPassword(rs.getString("cp_user.password"));
/* 1160:1182 */         cp.setDefaultSmsc(rs.getString("cp_user.default_smsc"));
/* 1161:1183 */         cp.setLogoUrl(rs.getString("cp_user.logo_url"));
/* 1162:1184 */         cp.setDefaultService(rs.getString("cp_user.default_service"));
/* 1163:     */         
/* 1164:     */ 
/* 1165:1187 */         format.setId(rs.getInt("format_list.format_id"));
/* 1166:1188 */         format.setFileExt(rs.getString("format_list.file_ext"));
/* 1167:1189 */         format.setMimeType(rs.getString("format_list.mime_type"));
/* 1168:1190 */         format.setPushBearer(rs.getString("format_list.push_bearer"));
/* 1169:     */       }
/* 1170:1193 */       item.setContentTypeDetails(type);
/* 1171:1194 */       item.setProviderDetails(cp);
/* 1172:1195 */       item.setFormat(format);
/* 1173:     */     }
/* 1174:     */     catch (Exception ex)
/* 1175:     */     {
/* 1176:1197 */       if (con != null) {
/* 1177:1198 */         con.close();
/* 1178:     */       }
/* 1179:1200 */       throw new Exception(ex.getMessage());
/* 1180:     */     }
/* 1181:1202 */     if (con != null) {
/* 1182:1203 */       con.close();
/* 1183:     */     }
/* 1184:1205 */     return item;
/* 1185:     */   }
/* 1186:     */   
/* 1187:     */   public void ExportContentListToTempTable(String listId)
/* 1188:     */     throws Exception
/* 1189:     */   {
/* 1190:1215 */     ResultSet rs = null;
/* 1191:1216 */     Connection con = null;
/* 1192:1217 */     PreparedStatement prepstat = null;
/* 1193:     */     try
/* 1194:     */     {
/* 1195:1219 */       con = DConnect.getConnection();
/* 1196:1220 */       String SQL = "select * from content_list where list_id=? ";
/* 1197:1221 */       prepstat = con.prepareStatement(SQL);
/* 1198:     */       
/* 1199:     */ 
/* 1200:     */ 
/* 1201:1225 */       prepstat.setString(1, listId);
/* 1202:1226 */       rs = prepstat.executeQuery();
/* 1203:     */       
/* 1204:1228 */       SQL = "insert into content_list_tmp(id,content_id,title,content_type,download_url,preview_url,size,formats,price,list_id,category,date_added,other_details,author) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
/* 1205:1229 */       prepstat = con.prepareStatement(SQL);
/* 1206:1231 */       while (rs.next())
/* 1207:     */       {
/* 1208:1232 */         prepstat.setString(1, rs.getString("id"));
/* 1209:1233 */         prepstat.setString(2, rs.getString("content_id"));
/* 1210:1234 */         prepstat.setString(3, rs.getString("title"));
/* 1211:1235 */         prepstat.setInt(4, rs.getInt(4));
/* 1212:1236 */         prepstat.setString(5, rs.getString("download_url"));
/* 1213:1237 */         prepstat.setString(6, rs.getString(6));
/* 1214:1238 */         prepstat.setLong(7, rs.getLong(7));
/* 1215:1239 */         prepstat.setString(8, rs.getString(8));
/* 1216:1240 */         prepstat.setString(9, rs.getString(9));
/* 1217:1241 */         prepstat.setString(10, rs.getString(10));
/* 1218:1242 */         prepstat.setInt(11, rs.getInt(11));
/* 1219:1243 */         prepstat.setTimestamp(12, rs.getTimestamp(12));
/* 1220:1244 */         prepstat.setString(13, rs.getString(13));
/* 1221:1245 */         prepstat.setString(14, rs.getString(14));
/* 1222:     */         
/* 1223:1247 */         prepstat.addBatch();
/* 1224:     */       }
/* 1225:1249 */       prepstat.executeBatch();
/* 1226:     */     }
/* 1227:     */     catch (Exception ex)
/* 1228:     */     {
/* 1229:1252 */       if (con != null) {
/* 1230:1253 */         con.close();
/* 1231:     */       }
/* 1232:1255 */       throw new Exception(ex.getMessage());
/* 1233:     */     }
/* 1234:1257 */     if (con != null) {
/* 1235:1258 */       con.close();
/* 1236:     */     }
/* 1237:     */   }
/* 1238:     */   
/* 1239:     */   public void ImportContentListFromTempTable(String listId)
/* 1240:     */     throws Exception
/* 1241:     */   {
/* 1242:1269 */     ResultSet rs = null;
/* 1243:1270 */     Connection con = null;
/* 1244:1271 */     PreparedStatement prepstat = null;
/* 1245:     */     try
/* 1246:     */     {
/* 1247:1273 */       con = DConnect.getConnection();
/* 1248:1274 */       String SQL = "select * from content_list_tmp where list_id=? ";
/* 1249:1275 */       prepstat = con.prepareStatement(SQL);
/* 1250:     */       
/* 1251:     */ 
/* 1252:     */ 
/* 1253:1279 */       prepstat.setString(1, listId);
/* 1254:1280 */       rs = prepstat.executeQuery();
/* 1255:     */       
/* 1256:1282 */       SQL = "insert into content_list(id,content_id,title,content_type,download_url,preview_url,size,formats,price,list_id,category,date_added,other_details,author) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
/* 1257:1283 */       prepstat = con.prepareStatement(SQL);
/* 1258:1285 */       while (rs.next())
/* 1259:     */       {
/* 1260:1286 */         prepstat.setString(1, rs.getString("id"));
/* 1261:1287 */         prepstat.setString(2, rs.getString("content_id"));
/* 1262:1288 */         prepstat.setString(3, rs.getString("title"));
/* 1263:1289 */         prepstat.setInt(4, rs.getInt(4));
/* 1264:1290 */         prepstat.setString(5, rs.getString("download_url"));
/* 1265:1291 */         prepstat.setString(6, rs.getString(6));
/* 1266:1292 */         prepstat.setLong(7, rs.getLong(7));
/* 1267:1293 */         prepstat.setString(8, rs.getString(8));
/* 1268:1294 */         prepstat.setString(9, rs.getString(9));
/* 1269:1295 */         prepstat.setString(10, rs.getString(10));
/* 1270:1296 */         prepstat.setInt(11, rs.getInt(11));
/* 1271:1297 */         prepstat.setTimestamp(12, rs.getTimestamp(12));
/* 1272:1298 */         prepstat.setString(13, rs.getString(13));
/* 1273:1299 */         prepstat.setString(14, rs.getString(14));
/* 1274:     */         
/* 1275:1301 */         prepstat.addBatch();
/* 1276:     */       }
/* 1277:1303 */       prepstat.executeBatch();
/* 1278:     */     }
/* 1279:     */     catch (Exception ex)
/* 1280:     */     {
/* 1281:1306 */       if (con != null) {
/* 1282:1307 */         con.close();
/* 1283:     */       }
/* 1284:1309 */       throw new Exception(ex.getMessage());
/* 1285:     */     }
/* 1286:1311 */     if (con != null) {
/* 1287:1312 */       con.close();
/* 1288:     */     }
/* 1289:     */   }
/* 1290:     */   
/* 1291:     */   public ArrayList viewContentList(String[] id, String listId)
/* 1292:     */     throws Exception
/* 1293:     */   {
/* 1294:1318 */     String arrId = "";
/* 1295:1319 */     for (int i = 0; i < id.length - 1; i++) {
/* 1296:1320 */       arrId = arrId + id[i] + ",";
/* 1297:     */     }
/* 1298:1322 */     arrId = arrId + id[(id.length - 1)];
/* 1299:1323 */     ArrayList items = new ArrayList();
/* 1300:     */     
/* 1301:     */ 
/* 1302:1326 */     ResultSet rs = null;
/* 1303:1327 */     Connection con = null;
/* 1304:1328 */     PreparedStatement prepstat = null;
/* 1305:     */     try
/* 1306:     */     {
/* 1307:1330 */       con = DConnect.getConnection();
/* 1308:     */       
/* 1309:     */ 
/* 1310:1333 */       String SQL = "SELECT * FROM content_list, service_route, cp_user, format_list where content_list.id in (" + arrId + ") and content_list.list_id=? and " + "content_list.content_type=service_route.service_type and content_list.list_id=cp_user.id " + "and content_list.formats=format_list.format_id order by date_added desc";
/* 1311:     */       
/* 1312:     */ 
/* 1313:     */ 
/* 1314:     */ 
/* 1315:1338 */       prepstat = con.prepareStatement(SQL);
/* 1316:     */       
/* 1317:1340 */       prepstat.setString(1, listId);
/* 1318:1341 */       rs = prepstat.executeQuery();
/* 1319:1342 */       while (rs.next())
/* 1320:     */       {
/* 1321:1344 */         ContentItem item = new ContentItem();
/* 1322:1345 */         ContentType type = new ContentType();
/* 1323:1346 */         Format format = new Format();
/* 1324:1347 */         User cp = new User();
/* 1325:     */         
/* 1326:     */ 
/* 1327:     */ 
/* 1328:1351 */         item.setContentId(rs.getString("content_id"));
/* 1329:1352 */         item.setid(rs.getString("id"));
/* 1330:1353 */         item.settitle(rs.getString("title"));
/* 1331:1354 */         item.settype(new Integer(rs.getInt("content_type")));
/* 1332:1355 */         item.setdownload_url(rs.getString("download_url"));
/* 1333:1356 */         item.setPreviewUrl(rs.getString("preview_url"));
/* 1334:     */         
/* 1335:1358 */         item.setPrice(rs.getString("price"));
/* 1336:1359 */         item.setAuthor(rs.getString("author"));
/* 1337:1360 */         item.setCategory(new Integer(rs.getInt("category")));
/* 1338:1361 */         item.setSize(new Long(rs.getLong("size")));
/* 1339:1362 */         item.setListId(rs.getString("list_id"));
/* 1340:1363 */         item.setDate_Added(rs.getTimestamp("date_added"));
/* 1341:1364 */         item.setOther_Details(rs.getString("other_details"));
/* 1342:1365 */         if (rs.getInt("preview_exists") == 1) {
/* 1343:1366 */           item.setPreviewExists(new Boolean(true));
/* 1344:     */         } else {
/* 1345:1368 */           item.setPreviewExists(new Boolean(false));
/* 1346:     */         }
/* 1347:1370 */         if (rs.getInt("is_free") == 1) {
/* 1348:1371 */           item.setFree(true);
/* 1349:     */         } else {
/* 1350:1373 */           item.setFree(false);
/* 1351:     */         }
/* 1352:1375 */         item.setShortItemRef(rs.getString("short_item_ref"));
/* 1353:1376 */         item.setSupplierId(rs.getString("supplier_id"));
/* 1354:     */         
/* 1355:     */ 
/* 1356:1379 */         type.setParentServiceType(rs.getInt("service_route.parent_service_type"));
/* 1357:1380 */         type.setServiceName(rs.getString("service_route.service_name"));
/* 1358:1381 */         type.setServiceType(rs.getInt("service_route.service_type"));
/* 1359:     */         
/* 1360:     */ 
/* 1361:1384 */         cp.setId(rs.getString("id"));
/* 1362:1385 */         cp.setName(rs.getString("name"));
/* 1363:1386 */         cp.setUsername(rs.getString("username"));
/* 1364:1387 */         cp.setPassword(rs.getString("password"));
/* 1365:1388 */         cp.setDefaultSmsc(rs.getString("default_smsc"));
/* 1366:1389 */         cp.setLogoUrl(rs.getString("logo_url"));
/* 1367:1390 */         cp.setDefaultService(rs.getString("default_service"));
/* 1368:     */         
/* 1369:     */ 
/* 1370:     */ 
/* 1371:     */ 
/* 1372:     */ 
/* 1373:     */ 
/* 1374:     */ 
/* 1375:1398 */         format.setId(rs.getInt("format_list.format_id"));
/* 1376:1399 */         format.setFileExt(rs.getString("format_list.file_ext"));
/* 1377:1400 */         format.setMimeType(rs.getString("format_list.mime_type"));
/* 1378:1401 */         format.setPushBearer(rs.getString("format_list.push_bearer"));
/* 1379:     */         
/* 1380:1403 */         item.setContentTypeDetails(type);
/* 1381:1404 */         item.setProviderDetails(cp);
/* 1382:1405 */         item.setFormat(format);
/* 1383:1406 */         items.add(item);
/* 1384:     */       }
/* 1385:     */     }
/* 1386:     */     catch (Exception ex)
/* 1387:     */     {
/* 1388:1410 */       if (con != null) {
/* 1389:1411 */         con.close();
/* 1390:     */       }
/* 1391:1413 */       throw new Exception(ex.getMessage());
/* 1392:     */     }
/* 1393:1415 */     if (con != null) {
/* 1394:1416 */       con.close();
/* 1395:     */     }
/* 1396:1418 */     return items;
/* 1397:     */   }
/* 1398:     */   
/* 1399:     */   public ArrayList viewContentList(String[] id, String[] listId)
/* 1400:     */     throws Exception
/* 1401:     */   {
/* 1402:1423 */     ArrayList items = new ArrayList();
/* 1403:     */     
/* 1404:     */ 
/* 1405:1426 */     ResultSet rs = null;
/* 1406:1427 */     Connection con = null;
/* 1407:1428 */     PreparedStatement prepstat = null;
/* 1408:     */     try
/* 1409:     */     {
/* 1410:1430 */       con = DConnect.getConnection();
/* 1411:1432 */       for (int i = 0; i < id.length; i++)
/* 1412:     */       {
/* 1413:1434 */         String SQL = "SELECT * FROM content_list, service_route, cp_user, format_list where content_list.id=? and content_list.list_id=? and content_list.content_type=service_route.service_type and content_list.list_id=cp_user.id and content_list.formats=format_list.format_id  order by date_added desc";
/* 1414:     */         
/* 1415:     */ 
/* 1416:1437 */         prepstat = con.prepareStatement(SQL);
/* 1417:     */         
/* 1418:1439 */         prepstat.setString(1, id[i]);
/* 1419:1440 */         prepstat.setString(2, listId[i]);
/* 1420:1441 */         rs = prepstat.executeQuery();
/* 1421:1442 */         while (rs.next())
/* 1422:     */         {
/* 1423:1444 */           ContentItem item = new ContentItem();
/* 1424:1445 */           ContentType type = new ContentType();
/* 1425:1446 */           Format format = new Format();
/* 1426:1447 */           User cp = new User();
/* 1427:     */           
/* 1428:     */ 
/* 1429:     */ 
/* 1430:1451 */           item.setContentId(rs.getString("content_id"));
/* 1431:1452 */           item.setid(rs.getString("id"));
/* 1432:1453 */           item.settitle(rs.getString("title"));
/* 1433:1454 */           item.settype(new Integer(rs.getInt("content_type")));
/* 1434:1455 */           item.setdownload_url(rs.getString("download_url"));
/* 1435:1456 */           item.setPreviewUrl(rs.getString("preview_url"));
/* 1436:     */           
/* 1437:1458 */           item.setPrice(rs.getString("price"));
/* 1438:1459 */           item.setAuthor(rs.getString("author"));
/* 1439:1460 */           item.setCategory(new Integer(rs.getInt("category")));
/* 1440:1461 */           item.setSize(new Long(rs.getLong("size")));
/* 1441:1462 */           item.setListId(rs.getString("list_id"));
/* 1442:1463 */           item.setDate_Added(rs.getTimestamp("date_added"));
/* 1443:1464 */           item.setOther_Details(rs.getString("other_details"));
/* 1444:1465 */           if (rs.getInt("preview_exists") == 1) {
/* 1445:1466 */             item.setPreviewExists(new Boolean(true));
/* 1446:     */           } else {
/* 1447:1468 */             item.setPreviewExists(new Boolean(false));
/* 1448:     */           }
/* 1449:1470 */           if (rs.getInt("is_free") == 1) {
/* 1450:1471 */             item.setFree(true);
/* 1451:     */           } else {
/* 1452:1473 */             item.setFree(false);
/* 1453:     */           }
/* 1454:1475 */           item.setShortItemRef(rs.getString("short_item_ref"));
/* 1455:1476 */           item.setSupplierId(rs.getString("supplier_id"));
/* 1456:     */           
/* 1457:     */ 
/* 1458:1479 */           type.setParentServiceType(rs.getInt("service_route.parent_service_type"));
/* 1459:1480 */           type.setServiceName(rs.getString("service_route.service_name"));
/* 1460:1481 */           type.setServiceType(rs.getInt("service_route.service_type"));
/* 1461:     */           
/* 1462:     */ 
/* 1463:1484 */           cp.setId(rs.getString("id"));
/* 1464:1485 */           cp.setName(rs.getString("name"));
/* 1465:1486 */           cp.setUsername(rs.getString("username"));
/* 1466:1487 */           cp.setPassword(rs.getString("password"));
/* 1467:1488 */           cp.setDefaultSmsc(rs.getString("default_smsc"));
/* 1468:1489 */           cp.setLogoUrl(rs.getString("logo_url"));
/* 1469:1490 */           cp.setDefaultService(rs.getString("default_service"));
/* 1470:     */           
/* 1471:     */ 
/* 1472:     */ 
/* 1473:     */ 
/* 1474:     */ 
/* 1475:     */ 
/* 1476:     */ 
/* 1477:1498 */           format.setId(rs.getInt("format_list.format_id"));
/* 1478:1499 */           format.setFileExt(rs.getString("format_list.file_ext"));
/* 1479:1500 */           format.setMimeType(rs.getString("format_list.mime_type"));
/* 1480:1501 */           format.setPushBearer(rs.getString("format_list.push_bearer"));
/* 1481:     */           
/* 1482:1503 */           item.setContentTypeDetails(type);
/* 1483:1504 */           item.setProviderDetails(cp);
/* 1484:1505 */           item.setFormat(format);
/* 1485:1506 */           items.add(item);
/* 1486:     */         }
/* 1487:     */       }
/* 1488:     */     }
/* 1489:     */     catch (Exception ex)
/* 1490:     */     {
/* 1491:1510 */       if (con != null) {
/* 1492:1511 */         con.close();
/* 1493:     */       }
/* 1494:1513 */       throw new Exception(ex.getMessage());
/* 1495:     */     }
/* 1496:1515 */     if (con != null) {
/* 1497:1516 */       con.close();
/* 1498:     */     }
/* 1499:1518 */     return items;
/* 1500:     */   }
/* 1501:     */   
/* 1502:     */   public Page viewContentList(String listId, String title, String formatId, Integer type, int start, int count)
/* 1503:     */     throws Exception
/* 1504:     */   {
/* 1505:1527 */     boolean hasNext = false;
/* 1506:1528 */     List logList = new ArrayList();
/* 1507:1529 */     Page ret = null;
/* 1508:1530 */     int y = 0;
/* 1509:1531 */     int i = 0;
/* 1510:1532 */     int numResults = 0;
/* 1511:     */     
/* 1512:     */ 
/* 1513:1535 */     ResultSet rs = null;
/* 1514:1536 */     Connection con = null;
/* 1515:1537 */     PreparedStatement prepstat = null;
/* 1516:     */     
/* 1517:1539 */     String SQL = "select * from content_list, service_route, cp_user, format_list where list_id=? ";
/* 1518:1541 */     if (!"*".equals(title)) {
/* 1519:1542 */       SQL = SQL + " AND title = '" + title + "'";
/* 1520:     */     }
/* 1521:1544 */     if (!"*".equals(formatId)) {
/* 1522:1545 */       SQL = SQL + " AND format = '" + formatId + "'";
/* 1523:     */     }
/* 1524:1547 */     if (!"*".equals(type)) {
/* 1525:1548 */       SQL = SQL + " AND content_type = '" + type + "'";
/* 1526:     */     }
/* 1527:1550 */     SQL = SQL + " and content_list.content_type=service_route.service_type and " + "content_list.list_id=cp_user.id and content_list.formats=format_list.format_id " + "order by content_list.date_added desc";
/* 1528:     */     try
/* 1529:     */     {
/* 1530:1555 */       con = DConnect.getConnection();
/* 1531:     */       
/* 1532:1557 */       prepstat = con.prepareStatement(SQL);
/* 1533:     */       
/* 1534:     */ 
/* 1535:1560 */       prepstat.setString(1, listId);
/* 1536:1561 */       rs = prepstat.executeQuery();
/* 1537:     */       
/* 1538:1563 */       rs.last();
/* 1539:1564 */       numResults = rs.getRow();
/* 1540:1565 */       rs.beforeFirst();
/* 1541:1568 */       while ((i < start + count) && (rs.next()))
/* 1542:     */       {
/* 1543:1569 */         if (i == 0)
/* 1544:     */         {
/* 1545:1570 */           int x = numResults;
/* 1546:1571 */           y = x / count;
/* 1547:1572 */           if (x % count > 0) {
/* 1548:1573 */             y++;
/* 1549:     */           }
/* 1550:     */         }
/* 1551:1576 */         if (i >= start)
/* 1552:     */         {
/* 1553:1577 */           ContentItem content_list = new ContentItem();
/* 1554:1578 */           ContentType typeBean = new ContentType();
/* 1555:1579 */           Format format = new Format();
/* 1556:1580 */           User cp = new User();
/* 1557:     */           
/* 1558:     */ 
/* 1559:1583 */           content_list.setContentId(rs.getString("content_id"));
/* 1560:1584 */           content_list.setid(rs.getString("id"));
/* 1561:1585 */           content_list.settitle(rs.getString("title"));
/* 1562:1586 */           content_list.settype(new Integer(rs.getInt("content_type")));
/* 1563:1587 */           content_list.setdownload_url(rs.getString("download_url"));
/* 1564:1588 */           content_list.setPreviewUrl(rs.getString("preview_url"));
/* 1565:1589 */           content_list.setSize(new Long(rs.getLong("size")));
/* 1566:     */           
/* 1567:1591 */           content_list.setPrice(rs.getString("price"));
/* 1568:1592 */           content_list.setOther_Details(rs.getString("other_details"));
/* 1569:1593 */           content_list.setDate_Added(rs.getTimestamp("date_added"));
/* 1570:1594 */           content_list.setAuthor(rs.getString("author"));
/* 1571:1595 */           content_list.setCategory(new Integer(rs.getInt("category")));
/* 1572:1596 */           content_list.setListId(rs.getString("list_id"));
/* 1573:1597 */           if (rs.getInt("isLocal") == 1) {
/* 1574:1598 */             content_list.setIsLocal(true);
/* 1575:     */           } else {
/* 1576:1600 */             content_list.setIsLocal(false);
/* 1577:     */           }
/* 1578:1602 */           if (rs.getInt("show") == 1) {
/* 1579:1603 */             content_list.setCanList(true);
/* 1580:     */           } else {
/* 1581:1605 */             content_list.setCanList(false);
/* 1582:     */           }
/* 1583:1607 */           if (rs.getInt("is_free") == 1) {
/* 1584:1608 */             content_list.setFree(true);
/* 1585:     */           } else {
/* 1586:1610 */             content_list.setFree(false);
/* 1587:     */           }
/* 1588:1612 */           content_list.setShortItemRef(rs.getString("short_item_ref"));
/* 1589:1613 */           content_list.setSupplierId(rs.getString("supplier_id"));
/* 1590:     */           
/* 1591:     */ 
/* 1592:1616 */           typeBean.setParentServiceType(rs.getInt("service_route.parent_service_type"));
/* 1593:1617 */           typeBean.setServiceName(rs.getString("service_route.service_name"));
/* 1594:1618 */           typeBean.setServiceType(rs.getInt("service_route.service_type"));
/* 1595:     */           
/* 1596:     */ 
/* 1597:1621 */           cp.setId(rs.getString("cp_user.id"));
/* 1598:1622 */           cp.setName(rs.getString("cp_user.name"));
/* 1599:1623 */           cp.setUsername(rs.getString("cp_user.username"));
/* 1600:1624 */           cp.setPassword(rs.getString("cp_user.password"));
/* 1601:1625 */           cp.setDefaultSmsc(rs.getString("cp_user.default_smsc"));
/* 1602:1626 */           cp.setLogoUrl(rs.getString("cp_user.logo_url"));
/* 1603:1627 */           cp.setDefaultService(rs.getString("cp_user.default_service"));
/* 1604:     */           
/* 1605:     */ 
/* 1606:1630 */           format.setId(rs.getInt("format_list.format_id"));
/* 1607:1631 */           format.setFileExt(rs.getString("format_list.file_ext"));
/* 1608:1632 */           format.setMimeType(rs.getString("format_list.mime_type"));
/* 1609:1633 */           format.setPushBearer(rs.getString("format_list.push_bearer"));
/* 1610:     */           
/* 1611:1635 */           content_list.setContentTypeDetails(typeBean);
/* 1612:1636 */           content_list.setProviderDetails(cp);
/* 1613:1637 */           content_list.setFormat(format);
/* 1614:     */           
/* 1615:1639 */           logList.add(content_list);
/* 1616:     */         }
/* 1617:1641 */         i++;
/* 1618:     */       }
/* 1619:1644 */       hasNext = rs.next();
/* 1620:1645 */       ret = new Page(logList, start, hasNext, y, numResults);
/* 1621:1647 */       if (ret == null) {
/* 1622:1648 */         ret = Page.EMPTY_PAGE;
/* 1623:     */       }
/* 1624:     */     }
/* 1625:     */     catch (Exception ex)
/* 1626:     */     {
/* 1627:1652 */       if (con != null) {
/* 1628:1653 */         con.close();
/* 1629:     */       }
/* 1630:1655 */       throw new Exception(ex.getMessage());
/* 1631:     */     }
/* 1632:1657 */     if (con != null) {
/* 1633:1658 */       con.close();
/* 1634:     */     }
/* 1635:1660 */     return ret;
/* 1636:     */   }
/* 1637:     */   
/* 1638:     */   public Page searchContentList(String listId, String title, String formats, Integer type, String author, String price, Integer category, int isFree, int start, int count)
/* 1639:     */     throws Exception
/* 1640:     */   {
/* 1641:1666 */     boolean hasNext = false;
/* 1642:1667 */     List logList = new ArrayList();
/* 1643:1668 */     Page ret = null;
/* 1644:1669 */     int y = 0;
/* 1645:1670 */     int i = 0;
/* 1646:1671 */     int numResults = 0;
/* 1647:     */     
/* 1648:     */ 
/* 1649:1674 */     ResultSet rs = null;
/* 1650:1675 */     Connection con = null;
/* 1651:1676 */     PreparedStatement prepstat = null;
/* 1652:     */     
/* 1653:1678 */     String SQL = "select * from content_list cl inner join service_route sr on cl.content_type=sr.service_type inner join cp_user cu on cl.list_id=cu.id inner join format_list fl on cl.formats=fl.format_id where content_type =" + type.toString();
/* 1654:1681 */     if (!"*".equals(title)) {
/* 1655:1682 */       SQL = SQL + " AND cl.title like '%" + title + "%'";
/* 1656:     */     }
/* 1657:1684 */     if (!"*".equals(author)) {
/* 1658:1685 */       SQL = SQL + " AND cl.author like '%" + author + "%'";
/* 1659:     */     }
/* 1660:1687 */     if (category.intValue() != 0) {
/* 1661:1688 */       SQL = SQL + " AND cl.category =" + category;
/* 1662:     */     }
/* 1663:1690 */     if (!"*".equals(formats)) {
/* 1664:1691 */       SQL = SQL + " AND cl.formats like '%" + formats + "%'";
/* 1665:     */     }
/* 1666:1693 */     if (!"*".equals(listId)) {
/* 1667:1694 */       SQL = SQL + " AND cl.list_id  ='" + listId + "'";
/* 1668:     */     }
/* 1669:1696 */     if ((isFree == 0) || (isFree == 1)) {
/* 1670:1697 */       SQL = SQL + " AND cl.is_free = " + isFree;
/* 1671:     */     }
/* 1672:1699 */     SQL = SQL + " and cl.show=1 order by cl.date_added desc";
/* 1673:     */     try
/* 1674:     */     {
/* 1675:1702 */       con = DConnect.getConnection();
/* 1676:     */       
/* 1677:1704 */       prepstat = con.prepareStatement(SQL);
/* 1678:     */       
/* 1679:     */ 
/* 1680:     */ 
/* 1681:     */ 
/* 1682:1709 */       rs = prepstat.executeQuery();
/* 1683:     */       
/* 1684:1711 */       rs.last();
/* 1685:1712 */       numResults = rs.getRow();
/* 1686:1713 */       rs.beforeFirst();
/* 1687:1716 */       while ((i < start + count) && (rs.next()))
/* 1688:     */       {
/* 1689:1717 */         if (i == 0)
/* 1690:     */         {
/* 1691:1718 */           int x = numResults;
/* 1692:1719 */           y = x / count;
/* 1693:1720 */           if (x % count > 0) {
/* 1694:1721 */             y++;
/* 1695:     */           }
/* 1696:     */         }
/* 1697:1724 */         if (i >= start)
/* 1698:     */         {
/* 1699:1725 */           ContentItem content_list = new ContentItem();
/* 1700:1726 */           ContentType typeBean = new ContentType();
/* 1701:1727 */           Format format = new Format();
/* 1702:1728 */           User cp = new User();
/* 1703:     */           
/* 1704:     */ 
/* 1705:1731 */           content_list.setContentId(rs.getString("cl.content_id"));
/* 1706:1732 */           content_list.setid(rs.getString("cl.id"));
/* 1707:1733 */           content_list.settitle(rs.getString("cl.title"));
/* 1708:1734 */           content_list.settype(new Integer(rs.getInt("cl.content_type")));
/* 1709:1735 */           content_list.setdownload_url(rs.getString("cl.download_url"));
/* 1710:1736 */           content_list.setPreviewUrl(rs.getString("cl.preview_url"));
/* 1711:1737 */           content_list.setSize(new Long(rs.getLong("cl.size")));
/* 1712:     */           
/* 1713:1739 */           content_list.setPrice(rs.getString("cl.price"));
/* 1714:1740 */           content_list.setOther_Details(rs.getString("cl.other_details"));
/* 1715:     */           
/* 1716:1742 */           content_list.setDate_Added(rs.getTimestamp("cl.date_added"));
/* 1717:1743 */           content_list.setAuthor(rs.getString("cl.author"));
/* 1718:1744 */           content_list.setCategory(new Integer(rs.getInt("cl.category")));
/* 1719:1745 */           content_list.setListId(rs.getString("cl.list_id"));
/* 1720:1746 */           if (rs.getInt("cl.preview_exists") == 1) {
/* 1721:1747 */             content_list.setPreviewExists(new Boolean(true));
/* 1722:     */           } else {
/* 1723:1749 */             content_list.setPreviewExists(new Boolean(false));
/* 1724:     */           }
/* 1725:1752 */           if (rs.getInt("cl.isLocal") == 1) {
/* 1726:1753 */             content_list.setIsLocal(true);
/* 1727:     */           } else {
/* 1728:1755 */             content_list.setIsLocal(false);
/* 1729:     */           }
/* 1730:1757 */           if (rs.getInt("cl.show") == 1) {
/* 1731:1758 */             content_list.setCanList(true);
/* 1732:     */           } else {
/* 1733:1760 */             content_list.setCanList(false);
/* 1734:     */           }
/* 1735:1762 */           if (rs.getInt("cl.is_free") == 1) {
/* 1736:1763 */             content_list.setFree(true);
/* 1737:     */           } else {
/* 1738:1765 */             content_list.setFree(false);
/* 1739:     */           }
/* 1740:1767 */           content_list.setShortItemRef(rs.getString("short_item_ref"));
/* 1741:1768 */           content_list.setSupplierId(rs.getString("supplier_id"));
/* 1742:     */           
/* 1743:     */ 
/* 1744:1771 */           typeBean.setParentServiceType(rs.getInt("sr.parent_service_type"));
/* 1745:1772 */           typeBean.setServiceName(rs.getString("sr.service_name"));
/* 1746:1773 */           typeBean.setServiceType(rs.getInt("sr.service_type"));
/* 1747:     */           
/* 1748:     */ 
/* 1749:1776 */           cp.setId(rs.getString("cu.id"));
/* 1750:1777 */           cp.setName(rs.getString("cu.name"));
/* 1751:1778 */           cp.setUsername(rs.getString("cu.username"));
/* 1752:1779 */           cp.setPassword(rs.getString("cu.password"));
/* 1753:1780 */           cp.setDefaultSmsc(rs.getString("cu.default_smsc"));
/* 1754:1781 */           cp.setLogoUrl(rs.getString("cu.logo_url"));
/* 1755:1782 */           cp.setDefaultService(rs.getString("cu.default_service"));
/* 1756:     */           
/* 1757:     */ 
/* 1758:     */ 
/* 1759:1786 */           format.setId(rs.getInt("fl.format_id"));
/* 1760:1787 */           format.setFileExt(rs.getString("fl.file_ext"));
/* 1761:1788 */           format.setMimeType(rs.getString("fl.mime_type"));
/* 1762:1789 */           format.setPushBearer(rs.getString("fl.push_bearer"));
/* 1763:     */           
/* 1764:     */ 
/* 1765:1792 */           content_list.setContentTypeDetails(typeBean);
/* 1766:1793 */           content_list.setProviderDetails(cp);
/* 1767:1794 */           content_list.setFormat(format);
/* 1768:     */           
/* 1769:1796 */           logList.add(content_list);
/* 1770:     */         }
/* 1771:1798 */         i++;
/* 1772:     */       }
/* 1773:1801 */       hasNext = rs.next();
/* 1774:1802 */       ret = new Page(logList, start, hasNext, y, numResults);
/* 1775:1804 */       if (ret == null) {
/* 1776:1805 */         ret = Page.EMPTY_PAGE;
/* 1777:     */       }
/* 1778:     */     }
/* 1779:     */     catch (Exception ex)
/* 1780:     */     {
/* 1781:1809 */       if (con != null) {
/* 1782:1810 */         con.close();
/* 1783:     */       }
/* 1784:1812 */       throw new Exception(ex.getMessage());
/* 1785:     */     }
/* 1786:1814 */     if (con != null) {
/* 1787:1815 */       con.close();
/* 1788:     */     }
/* 1789:1817 */     return ret;
/* 1790:     */   }
/* 1791:     */   
/* 1792:     */   public Page searchContentListByPhone(String ownerId, String title, String formats, Integer type, String author, String price, Integer category, String phoneId, int isFree, String shortItemRef, String supplierId, int start, int count)
/* 1793:     */     throws Exception
/* 1794:     */   {
/* 1795:1823 */     FonCapabilityMtrx fcm = new FonCapabilityMtrx();
/* 1796:1824 */     ArrayList fonformats = Format.getAllFormats();
/* 1797:1825 */     String fmtquery = "(cl.formats=0 or ";
/* 1798:1826 */     String capability = new String();
/* 1799:1827 */     String capabilityValue = new String();
/* 1800:1828 */     WurflDevice device = fcm.getActualDevice(phoneId);
/* 1801:1829 */     Format fmt = null;
/* 1802:1831 */     if (device != null)
/* 1803:     */     {
/* 1804:1832 */       for (int j = 0; j < fonformats.size(); j++)
/* 1805:     */       {
/* 1806:1833 */         fmt = (Format)fonformats.get(j);
/* 1807:1834 */         capability = fcm.findSupportedCapability(fmt.getFileExt());
/* 1808:1835 */         if ((capability != null) && (!capability.equals("")))
/* 1809:     */         {
/* 1810:1836 */           capabilityValue = fcm.getCapabilitiesManager().getCapabilityForDevice(device.getId(), capability);
/* 1811:1837 */           if (capabilityValue.equals("true")) {
/* 1812:1838 */             fmtquery = fmtquery + "cl.formats=" + fmt.getId() + " or ";
/* 1813:     */           }
/* 1814:     */         }
/* 1815:     */       }
/* 1816:1842 */       fmtquery = fmtquery.substring(0, fmtquery.lastIndexOf(" or ") + 1);
/* 1817:1843 */       fmtquery = fmtquery + ") and ";
/* 1818:     */     }
/* 1819:     */     else
/* 1820:     */     {
/* 1821:1845 */       throw new Exception("1");
/* 1822:     */     }
/* 1823:1847 */     boolean hasNext = false;
/* 1824:1848 */     List logList = new ArrayList();
/* 1825:1849 */     Page ret = null;
/* 1826:1850 */     int y = 0;
/* 1827:1851 */     int i = 0;
/* 1828:1852 */     int numResults = 0;
/* 1829:     */     
/* 1830:     */ 
/* 1831:1855 */     ResultSet rs = null;
/* 1832:1856 */     Connection con = null;
/* 1833:1857 */     PreparedStatement prepstat = null;
/* 1834:     */     try
/* 1835:     */     {
/* 1836:1861 */       con = DConnect.getConnection();
/* 1837:     */       
/* 1838:     */ 
/* 1839:     */ 
/* 1840:     */ 
/* 1841:     */ 
/* 1842:     */ 
/* 1843:     */ 
/* 1844:     */ 
/* 1845:     */ 
/* 1846:     */ 
/* 1847:1872 */       String SQL = "select * from content_list cl inner join service_route sr on cl.content_type=sr.service_type inner join cp_user cu on cl.list_id=cu.id inner join format_list fl on cl.formats=fl.format_id where content_type =" + type.toString();
/* 1848:1875 */       if (!"*".equals(title)) {
/* 1849:1876 */         SQL = SQL + " AND cl.title like '%" + title + "%'";
/* 1850:     */       }
/* 1851:1878 */       if (!"*".equals(author)) {
/* 1852:1879 */         SQL = SQL + " AND cl.author like '%" + author + "%'";
/* 1853:     */       }
/* 1854:1881 */       if (category.intValue() != 0) {
/* 1855:1882 */         SQL = SQL + " AND cl.category =" + category;
/* 1856:     */       }
/* 1857:1884 */       if (!"*".equals(formats)) {
/* 1858:1885 */         SQL = SQL + " AND cl.formats like '%" + formats + "%'";
/* 1859:     */       }
/* 1860:1887 */       if (!"*".equals(ownerId)) {
/* 1861:1888 */         SQL = SQL + " AND cl.list_id  ='" + ownerId + "'";
/* 1862:     */       }
/* 1863:1890 */       if (!"*".equals(supplierId)) {
/* 1864:1891 */         SQL = SQL + " AND cl.supplier_id  ='" + supplierId + "'";
/* 1865:     */       }
/* 1866:1893 */       if ((isFree == 0) || (isFree == 1)) {
/* 1867:1894 */         SQL = SQL + " AND cl.is_free = " + isFree;
/* 1868:     */       }
/* 1869:1896 */       if (!"*".equals(shortItemRef)) {
/* 1870:1897 */         SQL = SQL + " AND cl.short_item_ref ='" + shortItemRef + "'";
/* 1871:     */       }
/* 1872:1899 */       SQL = SQL + " and " + fmtquery + " cl.show=1 order by cl.date_added desc";
/* 1873:     */       
/* 1874:1901 */       prepstat = con.prepareStatement(SQL);
/* 1875:     */       
/* 1876:1903 */       rs = prepstat.executeQuery();
/* 1877:     */       
/* 1878:1905 */       rs.last();
/* 1879:1906 */       numResults = rs.getRow();
/* 1880:1907 */       rs.beforeFirst();
/* 1881:1910 */       while ((i < start + count) && (rs.next()))
/* 1882:     */       {
/* 1883:1911 */         if (i == 0)
/* 1884:     */         {
/* 1885:1912 */           int x = numResults;
/* 1886:1913 */           y = x / count;
/* 1887:1914 */           if (x % count > 0) {
/* 1888:1915 */             y++;
/* 1889:     */           }
/* 1890:     */         }
/* 1891:1918 */         if (i >= start)
/* 1892:     */         {
/* 1893:1920 */           ContentItem content_list = new ContentItem();
/* 1894:1921 */           ContentType typeBean = new ContentType();
/* 1895:1922 */           Format format = new Format();
/* 1896:1923 */           User cp = new User();
/* 1897:     */           
/* 1898:     */ 
/* 1899:1926 */           content_list.setContentId(rs.getString("cl.content_id"));
/* 1900:1927 */           content_list.setid(rs.getString("cl.id"));
/* 1901:1928 */           content_list.settitle(rs.getString("cl.title"));
/* 1902:1929 */           content_list.settype(new Integer(rs.getInt("cl.content_type")));
/* 1903:1930 */           content_list.setdownload_url(rs.getString("cl.download_url"));
/* 1904:1931 */           content_list.setPreviewUrl(rs.getString("cl.preview_url"));
/* 1905:1932 */           content_list.setSize(new Long(rs.getLong("cl.size")));
/* 1906:     */           
/* 1907:1934 */           content_list.setPrice(rs.getString("cl.price"));
/* 1908:1935 */           content_list.setOther_Details(rs.getString("cl.other_details"));
/* 1909:     */           
/* 1910:1937 */           content_list.setDate_Added(rs.getTimestamp("cl.date_added"));
/* 1911:1938 */           content_list.setAuthor(rs.getString("cl.author"));
/* 1912:1939 */           content_list.setCategory(new Integer(rs.getInt("cl.category")));
/* 1913:1940 */           content_list.setListId(rs.getString("cl.list_id"));
/* 1914:1941 */           if (rs.getInt("cl.preview_exists") == 1) {
/* 1915:1942 */             content_list.setPreviewExists(new Boolean(true));
/* 1916:     */           } else {
/* 1917:1944 */             content_list.setPreviewExists(new Boolean(false));
/* 1918:     */           }
/* 1919:1947 */           if (rs.getInt("cl.isLocal") == 1) {
/* 1920:1948 */             content_list.setIsLocal(true);
/* 1921:     */           } else {
/* 1922:1950 */             content_list.setIsLocal(false);
/* 1923:     */           }
/* 1924:1952 */           if (rs.getInt("cl.show") == 1) {
/* 1925:1953 */             content_list.setCanList(true);
/* 1926:     */           } else {
/* 1927:1955 */             content_list.setCanList(false);
/* 1928:     */           }
/* 1929:1957 */           if (rs.getInt("cl.is_free") == 1) {
/* 1930:1958 */             content_list.setFree(true);
/* 1931:     */           } else {
/* 1932:1960 */             content_list.setFree(false);
/* 1933:     */           }
/* 1934:1962 */           content_list.setSupplierId(rs.getString("cl.supplier_id"));
/* 1935:1963 */           content_list.setShortItemRef(rs.getString("cl.short_item_ref"));
/* 1936:     */           
/* 1937:     */ 
/* 1938:1966 */           typeBean.setParentServiceType(rs.getInt("sr.parent_service_type"));
/* 1939:1967 */           typeBean.setServiceName(rs.getString("sr.service_name"));
/* 1940:1968 */           typeBean.setServiceType(rs.getInt("sr.service_type"));
/* 1941:     */           
/* 1942:     */ 
/* 1943:1971 */           cp.setId(rs.getString("cu.id"));
/* 1944:1972 */           cp.setName(rs.getString("cu.name"));
/* 1945:1973 */           cp.setUsername(rs.getString("cu.username"));
/* 1946:1974 */           cp.setPassword(rs.getString("cu.password"));
/* 1947:1975 */           cp.setDefaultSmsc(rs.getString("cu.default_smsc"));
/* 1948:1976 */           cp.setLogoUrl(rs.getString("cu.logo_url"));
/* 1949:1977 */           cp.setDefaultService(rs.getString("cu.default_service"));
/* 1950:     */           
/* 1951:     */ 
/* 1952:     */ 
/* 1953:1981 */           format.setId(rs.getInt("fl.format_id"));
/* 1954:1982 */           format.setFileExt(rs.getString("fl.file_ext"));
/* 1955:1983 */           format.setMimeType(rs.getString("fl.mime_type"));
/* 1956:1984 */           format.setPushBearer(rs.getString("fl.push_bearer"));
/* 1957:     */           
/* 1958:     */ 
/* 1959:1987 */           content_list.setContentTypeDetails(typeBean);
/* 1960:1988 */           content_list.setProviderDetails(cp);
/* 1961:1989 */           content_list.setFormat(format);
/* 1962:     */           
/* 1963:1991 */           logList.add(content_list);
/* 1964:     */         }
/* 1965:1994 */         i++;
/* 1966:     */       }
/* 1967:1996 */       hasNext = rs.next();
/* 1968:1997 */       ret = new Page(logList, start, hasNext, y, numResults);
/* 1969:1999 */       if (ret == null) {
/* 1970:2000 */         ret = Page.EMPTY_PAGE;
/* 1971:     */       }
/* 1972:     */     }
/* 1973:     */     catch (Exception ex)
/* 1974:     */     {
/* 1975:2004 */       if (con != null) {
/* 1976:2005 */         con.close();
/* 1977:     */       }
/* 1978:2007 */       throw new Exception(ex.getMessage());
/* 1979:     */     }
/* 1980:2009 */     if (con != null) {
/* 1981:2010 */       con.close();
/* 1982:     */     }
/* 1983:2012 */     return ret;
/* 1984:     */   }
/* 1985:     */   
/* 1986:     */   public Page searchContentListBySite(String siteId, String title, String formats, Integer type, String author, String price, Integer category, int isFree, String shortItemRef, String supplierId, int start, int count)
/* 1987:     */     throws Exception
/* 1988:     */   {
/* 1989:2019 */     boolean hasNext = false;
/* 1990:2020 */     List logList = new ArrayList();
/* 1991:2021 */     Page ret = null;
/* 1992:2022 */     int y = 0;
/* 1993:2023 */     int i = 0;
/* 1994:2024 */     int numResults = 0;
/* 1995:     */     
/* 1996:     */ 
/* 1997:2027 */     ResultSet rs = null;
/* 1998:2028 */     Connection con = null;
/* 1999:2029 */     PreparedStatement prepstat = null;
/* 2000:     */     try
/* 2001:     */     {
/* 2002:2033 */       con = DConnect.getConnection();
/* 2003:     */       
/* 2004:2035 */       String SQL = "select cp_id from cp_sites where site_id='" + siteId + "'";
/* 2005:2036 */       prepstat = con.prepareStatement(SQL);
/* 2006:2037 */       rs = prepstat.executeQuery();
/* 2007:2038 */       String acctId = new String();
/* 2008:2039 */       while (rs.next()) {
/* 2009:2040 */         acctId = rs.getString("cp_id");
/* 2010:     */       }
/* 2011:2052 */       SQL = "select * from content_list cl inner join service_route sr on cl.content_type=sr.service_type inner join cp_user cu on cl.list_id=cu.id inner join format_list fl on cl.formats=fl.format_id where content_type =" + type.toString();
/* 2012:2055 */       if (!"*".equals(title)) {
/* 2013:2056 */         SQL = SQL + " AND cl.title like '%" + title + "%'";
/* 2014:     */       }
/* 2015:2058 */       if (!"*".equals(author)) {
/* 2016:2059 */         SQL = SQL + " AND cl.author like '%" + author + "%'";
/* 2017:     */       }
/* 2018:2061 */       if (category.intValue() != 0) {
/* 2019:2062 */         SQL = SQL + " AND cl.category =" + category;
/* 2020:     */       }
/* 2021:2064 */       if (!"*".equals(formats)) {
/* 2022:2065 */         SQL = SQL + " AND cl.formats like '%" + formats + "%'";
/* 2023:     */       }
/* 2024:2067 */       if (!"*".equals(acctId)) {
/* 2025:2068 */         SQL = SQL + " AND cl.list_id  ='" + acctId + "'";
/* 2026:     */       }
/* 2027:2070 */       if (!"*".equals(supplierId)) {
/* 2028:2071 */         SQL = SQL + " AND cl.supplier_id  ='" + supplierId + "'";
/* 2029:     */       }
/* 2030:2073 */       if ((isFree == 0) || (isFree == 1)) {
/* 2031:2074 */         SQL = SQL + " AND cl.is_free = " + isFree;
/* 2032:     */       }
/* 2033:2076 */       if (!"*".equals(shortItemRef)) {
/* 2034:2077 */         SQL = SQL + " AND cl.short_item_ref ='" + shortItemRef + "'";
/* 2035:     */       }
/* 2036:2079 */       SQL = SQL + " and cl.show=1 order by cl.date_added desc";
/* 2037:     */       
/* 2038:2081 */       prepstat = con.prepareStatement(SQL);
/* 2039:     */       
/* 2040:2083 */       rs = prepstat.executeQuery();
/* 2041:     */       
/* 2042:2085 */       rs.last();
/* 2043:2086 */       numResults = rs.getRow();
/* 2044:2087 */       rs.beforeFirst();
/* 2045:2090 */       while ((i < start + count) && (rs.next()))
/* 2046:     */       {
/* 2047:2091 */         if (i == 0)
/* 2048:     */         {
/* 2049:2092 */           int x = numResults;
/* 2050:2093 */           y = x / count;
/* 2051:2094 */           if (x % count > 0) {
/* 2052:2095 */             y++;
/* 2053:     */           }
/* 2054:     */         }
/* 2055:2098 */         if (i >= start)
/* 2056:     */         {
/* 2057:2099 */           ContentItem content_list = new ContentItem();
/* 2058:2100 */           ContentType typeBean = new ContentType();
/* 2059:2101 */           Format format = new Format();
/* 2060:2102 */           User cp = new User();
/* 2061:     */           
/* 2062:     */ 
/* 2063:2105 */           content_list.setContentId(rs.getString("cl.content_id"));
/* 2064:2106 */           content_list.setid(rs.getString("cl.id"));
/* 2065:2107 */           content_list.settitle(rs.getString("cl.title"));
/* 2066:2108 */           content_list.settype(new Integer(rs.getInt("cl.content_type")));
/* 2067:2109 */           content_list.setdownload_url(rs.getString("cl.download_url"));
/* 2068:2110 */           content_list.setPreviewUrl(rs.getString("cl.preview_url"));
/* 2069:2111 */           content_list.setSize(new Long(rs.getLong("cl.size")));
/* 2070:     */           
/* 2071:2113 */           content_list.setPrice(rs.getString("cl.price"));
/* 2072:2114 */           content_list.setOther_Details(rs.getString("cl.other_details"));
/* 2073:     */           
/* 2074:2116 */           content_list.setDate_Added(rs.getTimestamp("cl.date_added"));
/* 2075:2117 */           content_list.setAuthor(rs.getString("cl.author"));
/* 2076:2118 */           content_list.setCategory(new Integer(rs.getInt("cl.category")));
/* 2077:2119 */           content_list.setListId(rs.getString("cl.list_id"));
/* 2078:2120 */           if (rs.getInt("cl.preview_exists") == 1) {
/* 2079:2121 */             content_list.setPreviewExists(new Boolean(true));
/* 2080:     */           } else {
/* 2081:2123 */             content_list.setPreviewExists(new Boolean(false));
/* 2082:     */           }
/* 2083:2125 */           content_list.setSupplierId(rs.getString("cl.supplier_id"));
/* 2084:2126 */           content_list.setShortItemRef(rs.getString("cl.short_item_ref"));
/* 2085:2128 */           if (rs.getInt("cl.isLocal") == 1) {
/* 2086:2129 */             content_list.setIsLocal(true);
/* 2087:     */           } else {
/* 2088:2131 */             content_list.setIsLocal(false);
/* 2089:     */           }
/* 2090:2133 */           if (rs.getInt("cl.show") == 1) {
/* 2091:2134 */             content_list.setCanList(true);
/* 2092:     */           } else {
/* 2093:2136 */             content_list.setCanList(false);
/* 2094:     */           }
/* 2095:2138 */           if (rs.getInt("cl.is_free") == 1) {
/* 2096:2139 */             content_list.setFree(true);
/* 2097:     */           } else {
/* 2098:2141 */             content_list.setFree(false);
/* 2099:     */           }
/* 2100:2145 */           typeBean.setParentServiceType(rs.getInt("sr.parent_service_type"));
/* 2101:2146 */           typeBean.setServiceName(rs.getString("sr.service_name"));
/* 2102:2147 */           typeBean.setServiceType(rs.getInt("sr.service_type"));
/* 2103:     */           
/* 2104:     */ 
/* 2105:2150 */           cp.setId(rs.getString("cu.id"));
/* 2106:2151 */           cp.setName(rs.getString("cu.name"));
/* 2107:2152 */           cp.setUsername(rs.getString("cu.username"));
/* 2108:2153 */           cp.setPassword(rs.getString("cu.password"));
/* 2109:2154 */           cp.setDefaultSmsc(rs.getString("cu.default_smsc"));
/* 2110:2155 */           cp.setLogoUrl(rs.getString("cu.logo_url"));
/* 2111:2156 */           cp.setDefaultService(rs.getString("cu.default_service"));
/* 2112:     */           
/* 2113:     */ 
/* 2114:     */ 
/* 2115:2160 */           format.setId(rs.getInt("fl.format_id"));
/* 2116:2161 */           format.setFileExt(rs.getString("fl.file_ext"));
/* 2117:2162 */           format.setMimeType(rs.getString("fl.mime_type"));
/* 2118:2163 */           format.setPushBearer(rs.getString("fl.push_bearer"));
/* 2119:     */           
/* 2120:     */ 
/* 2121:2166 */           content_list.setContentTypeDetails(typeBean);
/* 2122:2167 */           content_list.setProviderDetails(cp);
/* 2123:2168 */           content_list.setFormat(format);
/* 2124:     */           
/* 2125:2170 */           logList.add(content_list);
/* 2126:     */         }
/* 2127:2172 */         i++;
/* 2128:     */       }
/* 2129:2175 */       hasNext = rs.next();
/* 2130:2176 */       ret = new Page(logList, start, hasNext, y, numResults);
/* 2131:2178 */       if (ret == null) {
/* 2132:2179 */         ret = Page.EMPTY_PAGE;
/* 2133:     */       }
/* 2134:     */     }
/* 2135:     */     catch (Exception ex)
/* 2136:     */     {
/* 2137:2183 */       if (con != null) {
/* 2138:2184 */         con.close();
/* 2139:     */       }
/* 2140:2186 */       throw new Exception(ex.getMessage());
/* 2141:     */     }
/* 2142:2188 */     if (con != null) {
/* 2143:2189 */       con.close();
/* 2144:     */     }
/* 2145:2191 */     return ret;
/* 2146:     */   }
/* 2147:     */   
/* 2148:     */   public Page searchContentListByContentSubscriber(String csid, String listId, String title, String formats, Integer type, String author, String price, Integer category, int isFree, int start, int count)
/* 2149:     */     throws Exception
/* 2150:     */   {
/* 2151:2197 */     boolean hasNext = false;
/* 2152:2198 */     List logList = new ArrayList();
/* 2153:2199 */     Page ret = null;
/* 2154:2200 */     int y = 0;
/* 2155:2201 */     int i = 0;
/* 2156:2202 */     int numResults = 0;
/* 2157:     */     
/* 2158:     */ 
/* 2159:2205 */     ResultSet rs = null;
/* 2160:2206 */     Connection con = null;
/* 2161:2207 */     PreparedStatement prepstat = null;
/* 2162:2208 */     String listIDquery = new String(" and (cl.list_id='" + csid + "' or ");
/* 2163:     */     try
/* 2164:     */     {
/* 2165:2211 */       con = DConnect.getConnection();
/* 2166:     */       
/* 2167:2213 */       String SQL = "select list_id from cs_cp_relationship where cs_id='" + csid + "'";
/* 2168:2214 */       prepstat = con.prepareStatement(SQL);
/* 2169:2215 */       rs = prepstat.executeQuery();
/* 2170:2216 */       while (rs.next()) {
/* 2171:2217 */         listIDquery = listIDquery + " cl.list_id='" + rs.getString("list_id") + "' or ";
/* 2172:     */       }
/* 2173:2219 */       listIDquery = listIDquery.substring(0, listIDquery.lastIndexOf("' or ") + 1);
/* 2174:2220 */       listIDquery = listIDquery + ") and ";
/* 2175:     */       
/* 2176:2222 */       SQL = "select * from content_list cl inner join service_route sr on cl.content_type=sr.service_type inner join cp_user cu on cl.list_id=cu.id inner join format_list fl on cl.formats=fl.format_id where cl.content_type =" + type.toString();
/* 2177:2225 */       if (!"*".equals(title)) {
/* 2178:2226 */         SQL = SQL + " AND cl.title like '%" + title + "%'";
/* 2179:     */       }
/* 2180:2228 */       if (!"*".equals(author)) {
/* 2181:2229 */         SQL = SQL + " AND cl.author like '%" + author + "%'";
/* 2182:     */       }
/* 2183:2231 */       if (category.intValue() != 0) {
/* 2184:2232 */         SQL = SQL + " AND cl.category =" + category;
/* 2185:     */       }
/* 2186:2234 */       if (!"*".equals(formats)) {
/* 2187:2235 */         SQL = SQL + " AND cl.formats like '%" + formats + "%'";
/* 2188:     */       }
/* 2189:2237 */       if (!"*".equals(listId)) {
/* 2190:2238 */         SQL = SQL + " AND cl.list_id  ='" + listId + "'";
/* 2191:     */       }
/* 2192:2240 */       if ((isFree == 0) || (isFree == 1)) {
/* 2193:2241 */         SQL = SQL + " AND cl.is_free = " + isFree;
/* 2194:     */       }
/* 2195:2243 */       SQL = SQL + listIDquery + " cl.show=1 order by cl.date_added desc";
/* 2196:     */       
/* 2197:2245 */       prepstat = con.prepareStatement(SQL);
/* 2198:     */       
/* 2199:2247 */       rs = prepstat.executeQuery();
/* 2200:     */       
/* 2201:2249 */       rs.last();
/* 2202:2250 */       numResults = rs.getRow();
/* 2203:2251 */       rs.beforeFirst();
/* 2204:2254 */       while ((i < start + count) && (rs.next()))
/* 2205:     */       {
/* 2206:2255 */         if (i == 0)
/* 2207:     */         {
/* 2208:2256 */           int x = numResults;
/* 2209:2257 */           y = x / count;
/* 2210:2258 */           if (x % count > 0) {
/* 2211:2259 */             y++;
/* 2212:     */           }
/* 2213:     */         }
/* 2214:2262 */         if (i >= start)
/* 2215:     */         {
/* 2216:2263 */           ContentItem content_list = new ContentItem();
/* 2217:2264 */           ContentType typeBean = new ContentType();
/* 2218:2265 */           Format format = new Format();
/* 2219:2266 */           User cp = new User();
/* 2220:     */           
/* 2221:     */ 
/* 2222:2269 */           content_list.setContentId(rs.getString("cl.content_id"));
/* 2223:2270 */           content_list.setid(rs.getString("cl.id"));
/* 2224:2271 */           content_list.settitle(rs.getString("cl.title"));
/* 2225:2272 */           content_list.settype(new Integer(rs.getInt("cl.content_type")));
/* 2226:2273 */           content_list.setdownload_url(rs.getString("cl.download_url"));
/* 2227:2274 */           content_list.setPreviewUrl(rs.getString("cl.preview_url"));
/* 2228:2275 */           content_list.setSize(new Long(rs.getLong("cl.size")));
/* 2229:     */           
/* 2230:2277 */           content_list.setPrice(rs.getString("cl.price"));
/* 2231:2278 */           content_list.setOther_Details(rs.getString("cl.other_details"));
/* 2232:     */           
/* 2233:2280 */           content_list.setDate_Added(rs.getTimestamp("cl.date_added"));
/* 2234:2281 */           content_list.setAuthor(rs.getString("cl.author"));
/* 2235:2282 */           content_list.setCategory(new Integer(rs.getInt("cl.category")));
/* 2236:2283 */           content_list.setListId(rs.getString("cl.list_id"));
/* 2237:2284 */           if (rs.getInt("cl.preview_exists") == 1) {
/* 2238:2285 */             content_list.setPreviewExists(new Boolean(true));
/* 2239:     */           } else {
/* 2240:2287 */             content_list.setPreviewExists(new Boolean(false));
/* 2241:     */           }
/* 2242:2290 */           if (rs.getInt("cl.isLocal") == 1) {
/* 2243:2291 */             content_list.setIsLocal(true);
/* 2244:     */           } else {
/* 2245:2293 */             content_list.setIsLocal(false);
/* 2246:     */           }
/* 2247:2295 */           if (rs.getInt("cl.show") == 1) {
/* 2248:2296 */             content_list.setCanList(true);
/* 2249:     */           } else {
/* 2250:2298 */             content_list.setCanList(false);
/* 2251:     */           }
/* 2252:2300 */           if (rs.getInt("cl.is_free") == 1) {
/* 2253:2301 */             content_list.setFree(true);
/* 2254:     */           } else {
/* 2255:2303 */             content_list.setFree(false);
/* 2256:     */           }
/* 2257:2307 */           typeBean.setParentServiceType(rs.getInt("sr.parent_service_type"));
/* 2258:2308 */           typeBean.setServiceName(rs.getString("sr.service_name"));
/* 2259:2309 */           typeBean.setServiceType(rs.getInt("sr.service_type"));
/* 2260:     */           
/* 2261:     */ 
/* 2262:2312 */           cp.setId(rs.getString("cu.id"));
/* 2263:2313 */           cp.setName(rs.getString("cu.name"));
/* 2264:2314 */           cp.setUsername(rs.getString("cu.username"));
/* 2265:2315 */           cp.setPassword(rs.getString("cu.password"));
/* 2266:2316 */           cp.setDefaultSmsc(rs.getString("cu.default_smsc"));
/* 2267:2317 */           cp.setLogoUrl(rs.getString("cu.logo_url"));
/* 2268:2318 */           cp.setDefaultService(rs.getString("cu.default_service"));
/* 2269:     */           
/* 2270:     */ 
/* 2271:     */ 
/* 2272:2322 */           format.setId(rs.getInt("fl.format_id"));
/* 2273:2323 */           format.setFileExt(rs.getString("fl.file_ext"));
/* 2274:2324 */           format.setMimeType(rs.getString("fl.mime_type"));
/* 2275:2325 */           format.setPushBearer(rs.getString("fl.push_bearer"));
/* 2276:     */           
/* 2277:     */ 
/* 2278:2328 */           content_list.setContentTypeDetails(typeBean);
/* 2279:2329 */           content_list.setProviderDetails(cp);
/* 2280:2330 */           content_list.setFormat(format);
/* 2281:     */           
/* 2282:2332 */           logList.add(content_list);
/* 2283:     */         }
/* 2284:2334 */         i++;
/* 2285:     */       }
/* 2286:2337 */       hasNext = rs.next();
/* 2287:2338 */       ret = new Page(logList, start, hasNext, y, numResults);
/* 2288:2340 */       if (ret == null) {
/* 2289:2341 */         ret = Page.EMPTY_PAGE;
/* 2290:     */       }
/* 2291:     */     }
/* 2292:     */     catch (Exception ex)
/* 2293:     */     {
/* 2294:2345 */       if (con != null) {
/* 2295:2346 */         con.close();
/* 2296:     */       }
/* 2297:2348 */       throw new Exception(ex.getMessage());
/* 2298:     */     }
/* 2299:2350 */     if (con != null) {
/* 2300:2351 */       con.close();
/* 2301:     */     }
/* 2302:2353 */     return ret;
/* 2303:     */   }
/* 2304:     */   
/* 2305:     */   public Page searchContentList(String ownerId, String title, String formats, Integer type, String author, String price, Integer category, int isFree, String shortItemRef, String supplierId, int start, int count)
/* 2306:     */     throws Exception
/* 2307:     */   {
/* 2308:2359 */     boolean hasNext = false;
/* 2309:2360 */     List logList = new ArrayList();
/* 2310:2361 */     Page ret = null;
/* 2311:2362 */     int y = 0;
/* 2312:2363 */     int i = 0;
/* 2313:2364 */     int numResults = 0;
/* 2314:     */     
/* 2315:     */ 
/* 2316:2367 */     ResultSet rs = null;
/* 2317:2368 */     Connection con = null;
/* 2318:2369 */     PreparedStatement prepstat = null;
/* 2319:     */     try
/* 2320:     */     {
/* 2321:2373 */       con = DConnect.getConnection();
/* 2322:     */       
/* 2323:     */ 
/* 2324:     */ 
/* 2325:     */ 
/* 2326:     */ 
/* 2327:     */ 
/* 2328:     */ 
/* 2329:     */ 
/* 2330:     */ 
/* 2331:     */ 
/* 2332:2384 */       String SQL = "select * from content_list cl inner join service_route sr on cl.content_type=sr.service_type inner join cp_user cu on cl.list_id=cu.id inner join format_list fl on cl.formats=fl.format_id where cl.content_type =" + type.toString();
/* 2333:2387 */       if (!"*".equals(title)) {
/* 2334:2388 */         SQL = SQL + " AND cl.title like '%" + title + "%'";
/* 2335:     */       }
/* 2336:2390 */       if (!"*".equals(author)) {
/* 2337:2391 */         SQL = SQL + " AND cl.author like '%" + author + "%'";
/* 2338:     */       }
/* 2339:2393 */       if (category.intValue() != 0) {
/* 2340:2394 */         SQL = SQL + " AND cl.category =" + category;
/* 2341:     */       }
/* 2342:2396 */       if (!"*".equals(formats)) {
/* 2343:2397 */         SQL = SQL + " AND cl.formats like '%" + formats + "%'";
/* 2344:     */       }
/* 2345:2399 */       if (!"*".equals(ownerId)) {
/* 2346:2400 */         SQL = SQL + " AND cl.list_id  ='" + ownerId + "'";
/* 2347:     */       }
/* 2348:2402 */       if (!"*".equals(supplierId)) {
/* 2349:2403 */         SQL = SQL + " AND cl.supplier_id  ='" + supplierId + "'";
/* 2350:     */       }
/* 2351:2405 */       if ((isFree == 0) || (isFree == 1)) {
/* 2352:2406 */         SQL = SQL + " AND cl.is_free = " + isFree;
/* 2353:     */       }
/* 2354:2408 */       if (!"*".equals(shortItemRef)) {
/* 2355:2409 */         SQL = SQL + " AND cl.short_item_ref ='" + shortItemRef + "'";
/* 2356:     */       }
/* 2357:2411 */       SQL = SQL + " and cl.show=1 order by cl.date_added desc";
/* 2358:     */       
/* 2359:2413 */       prepstat = con.prepareStatement(SQL);
/* 2360:     */       
/* 2361:2415 */       rs = prepstat.executeQuery();
/* 2362:     */       
/* 2363:2417 */       rs.last();
/* 2364:2418 */       numResults = rs.getRow();
/* 2365:2419 */       rs.beforeFirst();
/* 2366:2422 */       while ((i < start + count) && (rs.next()))
/* 2367:     */       {
/* 2368:2423 */         if (i == 0)
/* 2369:     */         {
/* 2370:2424 */           int x = numResults;
/* 2371:2425 */           y = x / count;
/* 2372:2426 */           if (x % count > 0) {
/* 2373:2427 */             y++;
/* 2374:     */           }
/* 2375:     */         }
/* 2376:2430 */         if (i >= start)
/* 2377:     */         {
/* 2378:2431 */           ContentItem content_list = new ContentItem();
/* 2379:2432 */           ContentType typeBean = new ContentType();
/* 2380:2433 */           Format format = new Format();
/* 2381:2434 */           User cp = new User();
/* 2382:     */           
/* 2383:     */ 
/* 2384:2437 */           content_list.setContentId(rs.getString("cl.content_id"));
/* 2385:2438 */           content_list.setid(rs.getString("cl.id"));
/* 2386:2439 */           content_list.settitle(rs.getString("cl.title"));
/* 2387:2440 */           content_list.settype(new Integer(rs.getInt("cl.content_type")));
/* 2388:2441 */           content_list.setdownload_url(rs.getString("cl.download_url"));
/* 2389:2442 */           content_list.setPreviewUrl(rs.getString("cl.preview_url"));
/* 2390:2443 */           content_list.setSize(new Long(rs.getLong("cl.size")));
/* 2391:2444 */           content_list.setPrice(rs.getString("cl.price"));
/* 2392:2445 */           content_list.setOther_Details(rs.getString("cl.other_details"));
/* 2393:     */           
/* 2394:2447 */           content_list.setDate_Added(rs.getTimestamp("cl.date_added"));
/* 2395:2448 */           content_list.setAuthor(rs.getString("cl.author"));
/* 2396:2449 */           content_list.setCategory(new Integer(rs.getInt("cl.category")));
/* 2397:2450 */           content_list.setListId(rs.getString("cl.list_id"));
/* 2398:2451 */           if (rs.getInt("cl.preview_exists") == 1) {
/* 2399:2452 */             content_list.setPreviewExists(new Boolean(true));
/* 2400:     */           } else {
/* 2401:2454 */             content_list.setPreviewExists(new Boolean(false));
/* 2402:     */           }
/* 2403:2457 */           if (rs.getInt("cl.isLocal") == 1) {
/* 2404:2458 */             content_list.setIsLocal(true);
/* 2405:     */           } else {
/* 2406:2460 */             content_list.setIsLocal(false);
/* 2407:     */           }
/* 2408:2462 */           if (rs.getInt("cl.show") == 1) {
/* 2409:2463 */             content_list.setCanList(true);
/* 2410:     */           } else {
/* 2411:2465 */             content_list.setCanList(false);
/* 2412:     */           }
/* 2413:2467 */           if (rs.getInt("cl.is_free") == 1) {
/* 2414:2468 */             content_list.setFree(true);
/* 2415:     */           } else {
/* 2416:2470 */             content_list.setFree(false);
/* 2417:     */           }
/* 2418:2472 */           content_list.setSupplierId(rs.getString("cl.supplier_id"));
/* 2419:2473 */           content_list.setShortItemRef(rs.getString("cl.short_item_ref"));
/* 2420:     */           
/* 2421:     */ 
/* 2422:2476 */           typeBean.setParentServiceType(rs.getInt("sr.parent_service_type"));
/* 2423:2477 */           typeBean.setServiceName(rs.getString("sr.service_name"));
/* 2424:2478 */           typeBean.setServiceType(rs.getInt("sr.service_type"));
/* 2425:     */           
/* 2426:     */ 
/* 2427:2481 */           cp.setId(rs.getString("cu.id"));
/* 2428:2482 */           cp.setName(rs.getString("cu.name"));
/* 2429:2483 */           cp.setUsername(rs.getString("cu.username"));
/* 2430:2484 */           cp.setPassword(rs.getString("cu.password"));
/* 2431:2485 */           cp.setDefaultSmsc(rs.getString("cu.default_smsc"));
/* 2432:2486 */           cp.setLogoUrl(rs.getString("cu.logo_url"));
/* 2433:2487 */           cp.setDefaultService(rs.getString("cu.default_service"));
/* 2434:     */           
/* 2435:     */ 
/* 2436:     */ 
/* 2437:2491 */           format.setId(rs.getInt("fl.format_id"));
/* 2438:2492 */           format.setFileExt(rs.getString("fl.file_ext"));
/* 2439:2493 */           format.setMimeType(rs.getString("fl.mime_type"));
/* 2440:2494 */           format.setPushBearer(rs.getString("fl.push_bearer"));
/* 2441:     */           
/* 2442:     */ 
/* 2443:2497 */           content_list.setContentTypeDetails(typeBean);
/* 2444:2498 */           content_list.setProviderDetails(cp);
/* 2445:2499 */           content_list.setFormat(format);
/* 2446:     */           
/* 2447:2501 */           logList.add(content_list);
/* 2448:     */         }
/* 2449:2503 */         i++;
/* 2450:     */       }
/* 2451:2506 */       hasNext = rs.next();
/* 2452:2507 */       ret = new Page(logList, start, hasNext, y, numResults);
/* 2453:2509 */       if (ret == null) {
/* 2454:2510 */         ret = Page.EMPTY_PAGE;
/* 2455:     */       }
/* 2456:     */     }
/* 2457:     */     catch (Exception ex)
/* 2458:     */     {
/* 2459:2514 */       if (con != null) {
/* 2460:2515 */         con.close();
/* 2461:     */       }
/* 2462:2517 */       throw new Exception(ex.getMessage());
/* 2463:     */     }
/* 2464:2519 */     if (con != null) {
/* 2465:2520 */       con.close();
/* 2466:     */     }
/* 2467:2522 */     return ret;
/* 2468:     */   }
/* 2469:     */   
/* 2470:     */   public ArrayList getRecentlyAdded(int typeId, int count)
/* 2471:     */     throws Exception
/* 2472:     */   {
/* 2473:2526 */     ArrayList recentlyAdded = new ArrayList();
/* 2474:     */     
/* 2475:2528 */     ResultSet rs = null;
/* 2476:2529 */     Connection con = null;
/* 2477:2530 */     PreparedStatement prepstat = null;
/* 2478:2531 */     int counter = 0;
/* 2479:     */     try
/* 2480:     */     {
/* 2481:2535 */       con = DConnect.getConnection();
/* 2482:     */       
/* 2483:2537 */       String SQL = "SELECT * FROM content_list, service_route, cp_user, format_list where content_type=? and content_list.content_type=service_route.service_type and content_list.list_id=cp_user.id and content_list.formats=format_list.format_id order by date_added desc";
/* 2484:     */       
/* 2485:     */ 
/* 2486:2540 */       prepstat = con.prepareStatement(SQL);
/* 2487:     */       
/* 2488:2542 */       prepstat.setInt(1, typeId);
/* 2489:2543 */       rs = prepstat.executeQuery();
/* 2490:2544 */       while (rs.next())
/* 2491:     */       {
/* 2492:2545 */         ContentItem item = new ContentItem();
/* 2493:2546 */         ContentType type = new ContentType();
/* 2494:2547 */         Format format = new Format();
/* 2495:2548 */         User cp = new User();
/* 2496:     */         
/* 2497:     */ 
/* 2498:2551 */         item.setContentId(rs.getString("content_id"));
/* 2499:2552 */         item.setid(rs.getString("id"));
/* 2500:2553 */         item.settitle(rs.getString("title"));
/* 2501:2554 */         item.settype(new Integer(rs.getInt("content_type")));
/* 2502:2555 */         item.setdownload_url(rs.getString("download_url"));
/* 2503:2556 */         item.setPreviewUrl(rs.getString("preview_url"));
/* 2504:     */         
/* 2505:2558 */         item.setPrice(rs.getString("price"));
/* 2506:2559 */         item.setAuthor(rs.getString("author"));
/* 2507:2560 */         item.setCategory(new Integer(rs.getInt("category")));
/* 2508:2561 */         item.setSize(new Long(rs.getLong("size")));
/* 2509:2562 */         item.setListId(rs.getString("list_id"));
/* 2510:2563 */         item.setDate_Added(rs.getTimestamp("date_added"));
/* 2511:2564 */         item.setOther_Details(rs.getString("other_details"));
/* 2512:2565 */         if (rs.getInt("isLocal") == 1) {
/* 2513:2566 */           item.setIsLocal(true);
/* 2514:     */         } else {
/* 2515:2568 */           item.setIsLocal(false);
/* 2516:     */         }
/* 2517:2570 */         if (rs.getInt("show") == 1) {
/* 2518:2571 */           item.setCanList(true);
/* 2519:     */         } else {
/* 2520:2573 */           item.setCanList(false);
/* 2521:     */         }
/* 2522:2575 */         if (rs.getInt("is_free") == 1) {
/* 2523:2576 */           item.setFree(true);
/* 2524:     */         } else {
/* 2525:2578 */           item.setFree(false);
/* 2526:     */         }
/* 2527:2580 */         item.setShortItemRef(rs.getString("short_item_ref"));
/* 2528:2581 */         item.setSupplierId(rs.getString("supplier_id"));
/* 2529:     */         
/* 2530:     */ 
/* 2531:2584 */         type.setParentServiceType(rs.getInt("service_route.parent_service_type"));
/* 2532:2585 */         type.setServiceName(rs.getString("service_route.service_name"));
/* 2533:2586 */         type.setServiceType(rs.getInt("service_route.service_type"));
/* 2534:     */         
/* 2535:     */ 
/* 2536:2589 */         cp.setId(rs.getString("cp_user.id"));
/* 2537:2590 */         cp.setName(rs.getString("cp_user.name"));
/* 2538:2591 */         cp.setUsername(rs.getString("cp_user.username"));
/* 2539:2592 */         cp.setPassword(rs.getString("cp_user.password"));
/* 2540:2593 */         cp.setDefaultSmsc(rs.getString("cp_user.default_smsc"));
/* 2541:2594 */         cp.setLogoUrl(rs.getString("cp_user.logo_url"));
/* 2542:2595 */         cp.setDefaultService(rs.getString("cp_user.default_service"));
/* 2543:     */         
/* 2544:     */ 
/* 2545:2598 */         format.setId(rs.getInt("format_list.format_id"));
/* 2546:2599 */         format.setFileExt(rs.getString("format_list.file_ext"));
/* 2547:2600 */         format.setMimeType(rs.getString("format_list.mime_type"));
/* 2548:2601 */         format.setPushBearer(rs.getString("format_list.push_bearer"));
/* 2549:     */         
/* 2550:2603 */         item.setContentTypeDetails(type);
/* 2551:2604 */         item.setProviderDetails(cp);
/* 2552:2605 */         item.setFormat(format);
/* 2553:2607 */         if (counter >= count) {
/* 2554:     */           break;
/* 2555:     */         }
/* 2556:2608 */         recentlyAdded.add(item);
/* 2557:     */         
/* 2558:     */ 
/* 2559:     */ 
/* 2560:2612 */         counter++;
/* 2561:     */       }
/* 2562:     */     }
/* 2563:     */     catch (Exception ex)
/* 2564:     */     {
/* 2565:2615 */       if (con != null) {
/* 2566:2616 */         con.close();
/* 2567:     */       }
/* 2568:2618 */       throw new Exception(ex.getMessage());
/* 2569:     */     }
/* 2570:2620 */     if (con != null) {
/* 2571:2621 */       con.close();
/* 2572:     */     }
/* 2573:2624 */     return recentlyAdded;
/* 2574:     */   }
/* 2575:     */   
/* 2576:     */   public ArrayList getRecentlyAdded(String cpid, int typeId)
/* 2577:     */     throws Exception
/* 2578:     */   {
/* 2579:2628 */     ArrayList recentlyAdded = new ArrayList();
/* 2580:     */     
/* 2581:2630 */     ResultSet rs = null;
/* 2582:2631 */     Connection con = null;
/* 2583:2632 */     PreparedStatement prepstat = null;
/* 2584:2633 */     int counter = 0;
/* 2585:     */     try
/* 2586:     */     {
/* 2587:2638 */       con = DConnect.getConnection();
/* 2588:     */       
/* 2589:     */ 
/* 2590:     */ 
/* 2591:     */ 
/* 2592:     */ 
/* 2593:     */ 
/* 2594:     */ 
/* 2595:     */ 
/* 2596:     */ 
/* 2597:     */ 
/* 2598:2649 */       String SQL = "SELECT * FROM content_list, service_route, cp_user, format_list where content_type=? and content_list.content_type=service_route.service_type and content_list.list_id=cp_user.id and content_list.formats=format_list.format_id and content_list.list_id='" + cpid + "'" + " order by date_added desc limit 10";
/* 2599:     */       
/* 2600:     */ 
/* 2601:     */ 
/* 2602:2653 */       prepstat = con.prepareStatement(SQL);
/* 2603:     */       
/* 2604:2655 */       prepstat.setInt(1, typeId);
/* 2605:2656 */       rs = prepstat.executeQuery();
/* 2606:2657 */       while (rs.next())
/* 2607:     */       {
/* 2608:2658 */         ContentItem item = new ContentItem();
/* 2609:2659 */         ContentType type = new ContentType();
/* 2610:2660 */         Format format = new Format();
/* 2611:2661 */         User cp = new User();
/* 2612:     */         
/* 2613:     */ 
/* 2614:2664 */         item.setContentId(rs.getString("content_id"));
/* 2615:2665 */         item.setid(rs.getString("id"));
/* 2616:2666 */         item.settitle(rs.getString("title"));
/* 2617:2667 */         item.settype(new Integer(rs.getInt("content_type")));
/* 2618:2668 */         item.setdownload_url(rs.getString("download_url"));
/* 2619:2669 */         item.setPreviewUrl(rs.getString("preview_url"));
/* 2620:     */         
/* 2621:2671 */         item.setPrice(rs.getString("price"));
/* 2622:2672 */         item.setAuthor(rs.getString("author"));
/* 2623:2673 */         item.setCategory(new Integer(rs.getInt("category")));
/* 2624:2674 */         item.setSize(new Long(rs.getLong("size")));
/* 2625:2675 */         item.setListId(rs.getString("list_id"));
/* 2626:2676 */         item.setDate_Added(rs.getTimestamp("date_added"));
/* 2627:2677 */         item.setOther_Details(rs.getString("other_details"));
/* 2628:2678 */         if (rs.getInt("isLocal") == 1) {
/* 2629:2679 */           item.setIsLocal(true);
/* 2630:     */         } else {
/* 2631:2681 */           item.setIsLocal(false);
/* 2632:     */         }
/* 2633:2683 */         if (rs.getInt("show") == 1) {
/* 2634:2684 */           item.setCanList(true);
/* 2635:     */         } else {
/* 2636:2686 */           item.setCanList(false);
/* 2637:     */         }
/* 2638:2688 */         if (rs.getInt("is_free") == 1) {
/* 2639:2689 */           item.setFree(true);
/* 2640:     */         } else {
/* 2641:2691 */           item.setFree(false);
/* 2642:     */         }
/* 2643:2693 */         item.setShortItemRef(rs.getString("short_item_ref"));
/* 2644:2694 */         item.setSupplierId(rs.getString("supplier_id"));
/* 2645:     */         
/* 2646:     */ 
/* 2647:2697 */         type.setParentServiceType(rs.getInt("service_route.parent_service_type"));
/* 2648:2698 */         type.setServiceName(rs.getString("service_route.service_name"));
/* 2649:2699 */         type.setServiceType(rs.getInt("service_route.service_type"));
/* 2650:     */         
/* 2651:     */ 
/* 2652:2702 */         cp.setId(rs.getString("cp_user.id"));
/* 2653:2703 */         cp.setName(rs.getString("cp_user.name"));
/* 2654:2704 */         cp.setUsername(rs.getString("cp_user.username"));
/* 2655:2705 */         cp.setPassword(rs.getString("cp_user.password"));
/* 2656:2706 */         cp.setDefaultSmsc(rs.getString("cp_user.default_smsc"));
/* 2657:2707 */         cp.setLogoUrl(rs.getString("cp_user.logo_url"));
/* 2658:2708 */         cp.setDefaultService(rs.getString("cp_user.default_service"));
/* 2659:     */         
/* 2660:     */ 
/* 2661:2711 */         format.setId(rs.getInt("format_list.format_id"));
/* 2662:2712 */         format.setFileExt(rs.getString("format_list.file_ext"));
/* 2663:2713 */         format.setMimeType(rs.getString("format_list.mime_type"));
/* 2664:2714 */         format.setPushBearer(rs.getString("format_list.push_bearer"));
/* 2665:     */         
/* 2666:2716 */         item.setContentTypeDetails(type);
/* 2667:2717 */         item.setProviderDetails(cp);
/* 2668:2718 */         item.setFormat(format);
/* 2669:     */         
/* 2670:2720 */         recentlyAdded.add(item);
/* 2671:     */       }
/* 2672:     */     }
/* 2673:     */     catch (Exception ex)
/* 2674:     */     {
/* 2675:2723 */       if (con != null) {
/* 2676:2724 */         con.close();
/* 2677:     */       }
/* 2678:2726 */       throw new Exception(ex.getMessage());
/* 2679:     */     }
/* 2680:2728 */     if (con != null) {
/* 2681:2729 */       con.close();
/* 2682:     */     }
/* 2683:2732 */     return recentlyAdded;
/* 2684:     */   }
/* 2685:     */   
/* 2686:     */   public static ArrayList fetchRecentAdditions(String cpid, int typeId)
/* 2687:     */     throws Exception
/* 2688:     */   {
/* 2689:2738 */     ResultSet rs = null;
/* 2690:2739 */     ResultSet rs2 = null;
/* 2691:2740 */     Connection con = null;
/* 2692:2741 */     PreparedStatement prepstat = null;
/* 2693:2742 */     String custimlistid = null;
/* 2694:2743 */     ArrayList items = new ArrayList();
/* 2695:     */     try
/* 2696:     */     {
/* 2697:2746 */       con = DConnect.getConnection();
/* 2698:2747 */       String query = "select custom_list_id from custom_list_definition where cp_id=? and custom_list_name='recent additions'";
/* 2699:2748 */       prepstat = con.prepareStatement(query);
/* 2700:2749 */       prepstat.setString(1, cpid);
/* 2701:2750 */       rs = prepstat.executeQuery();
/* 2702:2752 */       if (rs.next())
/* 2703:     */       {
/* 2704:2753 */         custimlistid = rs.getString("custom_list_id");
/* 2705:     */         
/* 2706:2755 */         query = "select item_id, prov_id from custom_list where custom_list_id=?";
/* 2707:2756 */         prepstat = con.prepareStatement(query);
/* 2708:2757 */         prepstat.setString(1, custimlistid);
/* 2709:2758 */         rs = prepstat.executeQuery();
/* 2710:2760 */         while (rs.next())
/* 2711:     */         {
/* 2712:2761 */           ContentItem item = new ContentItem();
/* 2713:2762 */           Format format = new Format();
/* 2714:2763 */           ContentType type = new ContentType();
/* 2715:2764 */           User cp = new User();
/* 2716:     */           
/* 2717:2766 */           query = "SELECT * FROM content_list, service_route, cp_user, format_list where content_list.id=? and content_list.list_id=? and content_list.content_type=service_route.service_type and content_list.list_id=cp_user.id and content_list.formats=format_list.format_id and content_list.content_type=" + typeId;
/* 2718:     */           
/* 2719:     */ 
/* 2720:     */ 
/* 2721:2770 */           prepstat = con.prepareStatement(query);
/* 2722:2771 */           String id = rs.getString("item_id");
/* 2723:2772 */           String listid = rs.getString("prov_id");
/* 2724:     */           
/* 2725:2774 */           prepstat.setString(1, id);
/* 2726:2775 */           prepstat.setString(2, listid);
/* 2727:     */           
/* 2728:2777 */           rs2 = prepstat.executeQuery();
/* 2729:2778 */           while (rs2.next())
/* 2730:     */           {
/* 2731:2780 */             item.setContentId(rs2.getString("content_id"));
/* 2732:2781 */             item.setid(rs2.getString("id"));
/* 2733:2782 */             item.settitle(rs2.getString("title"));
/* 2734:2783 */             item.settype(new Integer(rs2.getInt("content_type")));
/* 2735:2784 */             item.setdownload_url(rs2.getString("download_url"));
/* 2736:2785 */             item.setPreviewUrl(rs2.getString("preview_url"));
/* 2737:2786 */             item.setPrice(rs2.getString("price"));
/* 2738:2787 */             item.setAuthor(rs2.getString("author"));
/* 2739:2788 */             item.setCategory(new Integer(rs2.getInt("category")));
/* 2740:2789 */             item.setSize(new Long(rs2.getLong("size")));
/* 2741:2790 */             item.setListId(rs2.getString("list_id"));
/* 2742:2791 */             item.setDate_Added(rs2.getTimestamp("date_added"));
/* 2743:2792 */             item.setOther_Details(rs2.getString("other_details"));
/* 2744:2793 */             if (rs2.getInt("isLocal") == 1) {
/* 2745:2794 */               item.setIsLocal(true);
/* 2746:     */             } else {
/* 2747:2796 */               item.setIsLocal(false);
/* 2748:     */             }
/* 2749:2798 */             if (rs2.getInt("show") == 1) {
/* 2750:2799 */               item.setCanList(true);
/* 2751:     */             } else {
/* 2752:2801 */               item.setCanList(false);
/* 2753:     */             }
/* 2754:2803 */             if (rs2.getInt("is_free") == 1) {
/* 2755:2804 */               item.setFree(true);
/* 2756:     */             } else {
/* 2757:2806 */               item.setFree(false);
/* 2758:     */             }
/* 2759:2808 */             item.setShortItemRef(rs2.getString("short_item_ref"));
/* 2760:2809 */             item.setSupplierId(rs2.getString("supplier_id"));
/* 2761:     */             
/* 2762:     */ 
/* 2763:2812 */             type.setParentServiceType(rs2.getInt("service_route.parent_service_type"));
/* 2764:2813 */             type.setServiceName(rs2.getString("service_route.service_name"));
/* 2765:2814 */             type.setServiceType(rs2.getInt("service_route.service_type"));
/* 2766:     */             
/* 2767:     */ 
/* 2768:2817 */             cp.setId(rs2.getString("cp_user.id"));
/* 2769:2818 */             cp.setName(rs2.getString("cp_user.name"));
/* 2770:2819 */             cp.setUsername(rs2.getString("cp_user.username"));
/* 2771:2820 */             cp.setPassword(rs2.getString("cp_user.password"));
/* 2772:2821 */             cp.setDefaultSmsc(rs2.getString("cp_user.default_smsc"));
/* 2773:2822 */             cp.setLogoUrl(rs2.getString("cp_user.logo_url"));
/* 2774:2823 */             cp.setDefaultService(rs2.getString("cp_user.default_service"));
/* 2775:     */             
/* 2776:     */ 
/* 2777:2826 */             format.setId(rs2.getInt("format_list.format_id"));
/* 2778:2827 */             format.setFileExt(rs2.getString("format_list.file_ext"));
/* 2779:2828 */             format.setMimeType(rs2.getString("format_list.mime_type"));
/* 2780:2829 */             format.setPushBearer(rs2.getString("format_list.push_bearer"));
/* 2781:     */           }
/* 2782:2831 */           item.setContentTypeDetails(type);
/* 2783:2832 */           item.setProviderDetails(cp);
/* 2784:2833 */           item.setFormat(format);
/* 2785:     */           
/* 2786:2835 */           items.add(item);
/* 2787:     */         }
/* 2788:     */       }
/* 2789:     */     }
/* 2790:     */     catch (Exception ex)
/* 2791:     */     {
/* 2792:2839 */       if (con != null) {
/* 2793:2840 */         con.close();
/* 2794:     */       }
/* 2795:2842 */       throw new Exception(ex.getMessage());
/* 2796:     */     }
/* 2797:2844 */     if (con != null) {
/* 2798:2845 */       con.close();
/* 2799:     */     }
/* 2800:2848 */     return items;
/* 2801:     */   }
/* 2802:     */   
/* 2803:     */   public static void exportRecentAdditionsToCustomList(String cpid, int limit)
/* 2804:     */     throws Exception
/* 2805:     */   {
/* 2806:2853 */     if (limit > 10) {
/* 2807:2853 */       limit = 10;
/* 2808:     */     }
/* 2809:2854 */     if (limit < 0) {
/* 2810:2854 */       limit = 0;
/* 2811:     */     }
/* 2812:2855 */     java.sql.Date today = new java.sql.Date(Calendar.getInstance().getTime().getTime());
/* 2813:2856 */     String dateString = today.toString();
/* 2814:2857 */     StringTokenizer st = new StringTokenizer(dateString, "-");
/* 2815:2858 */     st.nextToken();
/* 2816:2859 */     int month = Integer.parseInt(st.nextToken()) - 1;
/* 2817:2860 */     String monthStr = "";
/* 2818:2861 */     if (month < 10) {
/* 2819:2861 */       monthStr = "0" + month;
/* 2820:     */     }
/* 2821:2864 */     ResultSet rs = null;
/* 2822:2865 */     ResultSet rs2 = null;
/* 2823:2866 */     ResultSet rs3 = null;
/* 2824:2867 */     Connection con = null;
/* 2825:2868 */     PreparedStatement prepstat = null;
/* 2826:2869 */     Statement stat = null;
/* 2827:     */     try
/* 2828:     */     {
/* 2829:2872 */       con = DConnect.getConnection();
/* 2830:     */       
/* 2831:2874 */       String customListId = null;
/* 2832:     */       
/* 2833:2876 */       String query = "select custom_list_id from custom_list_definition where cp_id=? and custom_list_name='recent additions'";
/* 2834:2877 */       prepstat = con.prepareStatement(query);
/* 2835:2878 */       prepstat.setString(1, cpid);
/* 2836:2879 */       rs = prepstat.executeQuery();
/* 2837:2880 */       while (rs.next()) {
/* 2838:2881 */         customListId = rs.getString("custom_list_id");
/* 2839:     */       }
/* 2840:2884 */       if (customListId != null)
/* 2841:     */       {
/* 2842:2886 */         query = "select service_type from service_route where parent_service_type=1";
/* 2843:2887 */         prepstat = con.prepareStatement(query);
/* 2844:2888 */         rs = prepstat.executeQuery();
/* 2845:2889 */         while (rs.next())
/* 2846:     */         {
/* 2847:2891 */           String contentType = rs.getString("service_type");
/* 2848:     */           
/* 2849:     */ 
/* 2850:     */ 
/* 2851:     */ 
/* 2852:     */ 
/* 2853:     */ 
/* 2854:     */ 
/* 2855:     */ 
/* 2856:     */ 
/* 2857:2901 */           query = "SELECT content_list.id, content_list.list_id FROM content_list where content_type=" + contentType + " and content_list.list_id='" + cpid + "' order by date_added desc limit " + limit;
/* 2858:     */           
/* 2859:2903 */           prepstat = con.prepareStatement(query);
/* 2860:2904 */           rs2 = prepstat.executeQuery();
/* 2861:2906 */           if (rs2.next())
/* 2862:     */           {
/* 2863:2908 */             query = "select cu.item_id, cu.prov_id from custom_list cu inner join content_list co on co.id=cu.item_id and co.list_id=cu.prov_id where co.content_type=" + contentType + " and cu.custom_list_id='" + customListId + "'";
/* 2864:     */             
/* 2865:2910 */             prepstat = con.prepareStatement(query);
/* 2866:2911 */             rs3 = prepstat.executeQuery();
/* 2867:     */             
/* 2868:     */ 
/* 2869:2914 */             int hasMore = 0;
/* 2870:2915 */             con.setAutoCommit(false);
/* 2871:2916 */             stat = con.createStatement();
/* 2872:2917 */             while (rs3.next())
/* 2873:     */             {
/* 2874:2918 */               query = "delete from custom_list where custom_list_id='" + customListId + "' and item_id='" + rs3.getString("item_id") + "' and prov_id='" + rs3.getString("prov_id") + "'";
/* 2875:     */               
/* 2876:2920 */               stat.addBatch(query);
/* 2877:2921 */               hasMore++;
/* 2878:     */             }
/* 2879:2923 */             if (hasMore > 0)
/* 2880:     */             {
/* 2881:2924 */               stat.executeBatch();
/* 2882:2925 */               stat.clearBatch();
/* 2883:     */             }
/* 2884:2928 */             rs2.beforeFirst();
/* 2885:2929 */             con.setAutoCommit(false);
/* 2886:2930 */             con.createStatement();
/* 2887:2931 */             stat = con.createStatement();
/* 2888:2932 */             while (rs2.next())
/* 2889:     */             {
/* 2890:2933 */               query = "INSERT into custom_list (custom_list_id, item_id, prov_id) values('" + customListId + "','" + rs2.getString("id") + "','" + rs2.getString("list_id") + "')";
/* 2891:     */               
/* 2892:2935 */               stat.addBatch(query);
/* 2893:     */             }
/* 2894:2937 */             stat.executeBatch();
/* 2895:2938 */             stat.clearBatch();
/* 2896:     */           }
/* 2897:     */         }
/* 2898:     */       }
/* 2899:     */     }
/* 2900:     */     catch (Exception e)
/* 2901:     */     {
/* 2902:2943 */       throw new Exception();
/* 2903:     */     }
/* 2904:     */   }
/* 2905:     */   
/* 2906:     */   public List getDistinctListIDs(String criterion)
/* 2907:     */     throws Exception
/* 2908:     */   {
/* 2909:2951 */     List listIDs = new ArrayList();
/* 2910:     */     
/* 2911:2953 */     String queryEnding = "";
/* 2912:2955 */     if ((criterion != null) || (!criterion.equals(""))) {
/* 2913:2956 */       queryEnding = "where content_type=" + criterion;
/* 2914:     */     }
/* 2915:2960 */     ResultSet rs = null;
/* 2916:2961 */     Connection con = null;
/* 2917:2962 */     PreparedStatement prepstat = null;
/* 2918:     */     try
/* 2919:     */     {
/* 2920:2965 */       con = DConnect.getConnection();
/* 2921:2966 */       String query = "SELECT DISTINCT list_Id from content_list " + queryEnding;
/* 2922:2967 */       prepstat = con.prepareStatement(query);
/* 2923:2968 */       rs = prepstat.executeQuery();
/* 2924:2970 */       while (rs.next()) {
/* 2925:2972 */         listIDs.add(rs.getString("list_Id"));
/* 2926:     */       }
/* 2927:     */     }
/* 2928:     */     catch (Exception ex)
/* 2929:     */     {
/* 2930:2975 */       if (con != null) {
/* 2931:2976 */         con.close();
/* 2932:     */       }
/* 2933:2978 */       throw new Exception(ex.getMessage());
/* 2934:     */     }
/* 2935:2980 */     if (con != null) {
/* 2936:2982 */       con.close();
/* 2937:     */     }
/* 2938:2984 */     return listIDs;
/* 2939:     */   }
/* 2940:     */   
/* 2941:     */   public static String[][] getCPsForTypes()
/* 2942:     */     throws Exception
/* 2943:     */   {
/* 2944:2988 */     String[][] struct = (String[][])null;
/* 2945:2989 */     String query = "SELECT distinct content_list.content_type, content_list.list_id, cp_user.name, cp_user.logo_url FROM content_list, cp_user where cp_user.id=content_list.list_id order by content_type";
/* 2946:     */     
/* 2947:     */ 
/* 2948:     */ 
/* 2949:2993 */     String logo_url = new String();
/* 2950:     */     
/* 2951:2995 */     ResultSet rs = null;
/* 2952:2996 */     Connection con = null;
/* 2953:2997 */     PreparedStatement prepstat = null;
/* 2954:     */     try
/* 2955:     */     {
/* 2956:3000 */       con = DConnect.getConnection();
/* 2957:3001 */       prepstat = con.prepareStatement(query);
/* 2958:3002 */       rs = prepstat.executeQuery();
/* 2959:     */       
/* 2960:3004 */       int rowCount = 0;
/* 2961:3005 */       while (rs.next()) {
/* 2962:3006 */         rowCount++;
/* 2963:     */       }
/* 2964:3008 */       struct = new String[rowCount][4];
/* 2965:3009 */       rs.beforeFirst();
/* 2966:3010 */       rowCount = 0;
/* 2967:3012 */       while (rs.next())
/* 2968:     */       {
/* 2969:3013 */         String typeId = rs.getString("content_type");
/* 2970:3014 */         String listId = rs.getString("list_id");
/* 2971:3015 */         String name = rs.getString("name");
/* 2972:3016 */         logo_url = rs.getString("logo_url");
/* 2973:3017 */         struct[rowCount][0] = typeId;
/* 2974:3018 */         struct[rowCount][1] = listId;
/* 2975:3019 */         struct[rowCount][2] = name;
/* 2976:3020 */         struct[rowCount][3] = logo_url;
/* 2977:3021 */         rowCount++;
/* 2978:     */       }
/* 2979:     */     }
/* 2980:     */     catch (Exception ex)
/* 2981:     */     {
/* 2982:3024 */       if (con != null) {
/* 2983:3025 */         con.close();
/* 2984:     */       }
/* 2985:3027 */       throw new Exception(ex.getMessage());
/* 2986:     */     }
/* 2987:3029 */     if (con != null) {
/* 2988:3030 */       con.close();
/* 2989:     */     }
/* 2990:3033 */     return struct;
/* 2991:     */   }
/* 2992:     */   
/* 2993:     */   public static HashMap getMyProviders(String siteId)
/* 2994:     */     throws Exception
/* 2995:     */   {
/* 2996:3037 */     HashMap grpPrv = new HashMap();
/* 2997:     */     
/* 2998:     */ 
/* 2999:3040 */     ResultSet rs = null;
/* 3000:3041 */     Connection con = null;
/* 3001:3042 */     PreparedStatement prepstat = null;
/* 3002:     */     try
/* 3003:     */     {
/* 3004:3045 */       con = DConnect.getConnection();
/* 3005:3046 */       String query = "select cp_id from cp_sites where site_id='" + siteId + "'";
/* 3006:3047 */       prepstat = con.prepareStatement(query);
/* 3007:3048 */       rs = prepstat.executeQuery();
/* 3008:3049 */       String acctId = new String();
/* 3009:3050 */       while (rs.next()) {
/* 3010:3051 */         acctId = rs.getString("cp_id");
/* 3011:     */       }
/* 3012:3054 */       query = "select list_id from cs_cp_relationship where cs_id='" + acctId + "'";
/* 3013:3055 */       String listIDquery = new String("(content_list.list_id='" + acctId + "' or ");
/* 3014:3056 */       prepstat = con.prepareStatement(query);
/* 3015:3057 */       rs = prepstat.executeQuery();
/* 3016:3058 */       while (rs.next()) {
/* 3017:3059 */         listIDquery = listIDquery + " content_list.list_id='" + rs.getString("list_id") + "' or ";
/* 3018:     */       }
/* 3019:3061 */       listIDquery = listIDquery.substring(0, listIDquery.lastIndexOf("' or ") + 1);
/* 3020:3062 */       listIDquery = listIDquery + ") and ";
/* 3021:     */       
/* 3022:3064 */       query = "SELECT distinct content_list.list_id, cp_user.name, cp_user.logo_url, content_list.content_type FROM content_list, cp_user where " + listIDquery + "cp_user.id=content_list.list_id order by content_list.content_type, " + "cp_user.id";
/* 3023:     */       
/* 3024:     */ 
/* 3025:3067 */       prepstat = con.prepareStatement(query);
/* 3026:3068 */       rs = prepstat.executeQuery();
/* 3027:     */       
/* 3028:3070 */       int conTypeMem = 1;
/* 3029:3071 */       ArrayList provs = new ArrayList();
/* 3030:3072 */       while (rs.next())
/* 3031:     */       {
/* 3032:3073 */         String[] details = new String[3];
/* 3033:3074 */         details[0] = rs.getString("list_id");
/* 3034:3075 */         details[1] = rs.getString("name");
/* 3035:3076 */         details[2] = rs.getString("logo_url");
/* 3036:3077 */         int tempStore = rs.getInt("content_type");
/* 3037:3078 */         if (tempStore == conTypeMem)
/* 3038:     */         {
/* 3039:3079 */           provs.add(details);
/* 3040:     */         }
/* 3041:     */         else
/* 3042:     */         {
/* 3043:3081 */           grpPrv.put(new String("" + conTypeMem), provs);
/* 3044:3082 */           conTypeMem = tempStore;
/* 3045:3083 */           provs = new ArrayList();
/* 3046:3084 */           provs.add(details);
/* 3047:     */         }
/* 3048:     */       }
/* 3049:3087 */       grpPrv.put(new String("" + conTypeMem), provs);
/* 3050:     */     }
/* 3051:     */     catch (Exception ex)
/* 3052:     */     {
/* 3053:3089 */       if (con != null) {
/* 3054:3090 */         con.close();
/* 3055:     */       }
/* 3056:3092 */       throw new Exception(ex.getMessage());
/* 3057:     */     }
/* 3058:3094 */     if (con != null) {
/* 3059:3095 */       con.close();
/* 3060:     */     }
/* 3061:3098 */     return grpPrv;
/* 3062:     */   }
/* 3063:     */   
/* 3064:     */   public static HashMap itemCountByType(String listId)
/* 3065:     */     throws Exception
/* 3066:     */   {
/* 3067:3105 */     String query = null;
/* 3068:3106 */     ResultSet rs = null;
/* 3069:3107 */     Connection con = null;
/* 3070:3108 */     PreparedStatement prepstat = null;
/* 3071:3109 */     HashMap table = new HashMap();
/* 3072:     */     try
/* 3073:     */     {
/* 3074:3112 */       con = DConnect.getConnection();
/* 3075:3113 */       query = "select service_route.service_type, count(content_list.content_id) as number from content_list, service_route where service_route.service_type=content_list.content_type and content_list.list_id=? group by typeCode";
/* 3076:     */       
/* 3077:     */ 
/* 3078:     */ 
/* 3079:     */ 
/* 3080:3118 */       prepstat = con.prepareStatement(query);
/* 3081:3119 */       prepstat.setString(1, listId);
/* 3082:3120 */       rs = prepstat.executeQuery();
/* 3083:3122 */       while (rs.next())
/* 3084:     */       {
/* 3085:3123 */         String key = rs.getString("service_type");
/* 3086:3124 */         String entry = rs.getString("number");
/* 3087:3125 */         table.put(key, entry);
/* 3088:     */       }
/* 3089:     */     }
/* 3090:     */     catch (Exception ex)
/* 3091:     */     {
/* 3092:3128 */       if (con != null) {
/* 3093:3129 */         con.close();
/* 3094:     */       }
/* 3095:3131 */       throw new Exception(ex.getMessage());
/* 3096:     */     }
/* 3097:3133 */     if (con != null) {
/* 3098:3134 */       con.close();
/* 3099:     */     }
/* 3100:3137 */     return table;
/* 3101:     */   }
/* 3102:     */   
/* 3103:     */   public static int itemCountByType(String listId, int[] typeIDs)
/* 3104:     */     throws Exception
/* 3105:     */   {
/* 3106:3143 */     int number = 0;
/* 3107:3144 */     String query = null;
/* 3108:3145 */     ResultSet rs = null;
/* 3109:3146 */     Connection con = null;
/* 3110:3147 */     PreparedStatement prepstat = null;
/* 3111:     */     try
/* 3112:     */     {
/* 3113:3150 */       con = DConnect.getConnection();
/* 3114:3151 */       query = "select count(content_id) as number from content_list where content_type=" + typeIDs[0];
/* 3115:3154 */       if (typeIDs.length > 1) {
/* 3116:3155 */         for (int i = 1; i < typeIDs.length; i++) {
/* 3117:3156 */           query = query + " or content_type=" + typeIDs[i];
/* 3118:     */         }
/* 3119:     */       }
/* 3120:3160 */       query = query + " and content_list.list_id=?";
/* 3121:3161 */       prepstat = con.prepareStatement(query);
/* 3122:3162 */       prepstat.setString(1, listId);
/* 3123:3163 */       rs = prepstat.executeQuery();
/* 3124:3165 */       while (rs.next()) {
/* 3125:3166 */         number = rs.getInt("number");
/* 3126:     */       }
/* 3127:     */     }
/* 3128:     */     catch (Exception ex)
/* 3129:     */     {
/* 3130:3169 */       if (con != null) {
/* 3131:3170 */         con.close();
/* 3132:     */       }
/* 3133:3172 */       throw new Exception(ex.getMessage());
/* 3134:     */     }
/* 3135:3174 */     if (con != null) {
/* 3136:3175 */       con.close();
/* 3137:     */     }
/* 3138:3178 */     return number;
/* 3139:     */   }
/* 3140:     */   
/* 3141:     */   public static String validateSupplier(String ownerId, String supplierKey)
/* 3142:     */     throws Exception
/* 3143:     */   {
/* 3144:3182 */     String id = null;
/* 3145:3183 */     String query = null;
/* 3146:3184 */     ResultSet rs = null;
/* 3147:3185 */     Connection con = null;
/* 3148:3186 */     PreparedStatement prepstat = null;
/* 3149:     */     try
/* 3150:     */     {
/* 3151:3189 */       con = DConnect.getConnection();
/* 3152:3190 */       query = "select 3rd_party_id from 3rd_party_info where account_id='" + ownerId + "' and 3rd_party_key='" + supplierKey + "'";
/* 3153:3191 */       prepstat = con.prepareStatement(query);
/* 3154:3192 */       rs = prepstat.executeQuery();
/* 3155:3194 */       if (rs.next()) {
/* 3156:3195 */         id = rs.getString("3rd_party_id");
/* 3157:     */       }
/* 3158:     */     }
/* 3159:     */     catch (Exception ex)
/* 3160:     */     {
/* 3161:3198 */       if (con != null) {
/* 3162:3199 */         con.close();
/* 3163:     */       }
/* 3164:3201 */       throw new Exception(ex.getMessage());
/* 3165:     */     }
/* 3166:3203 */     if (con != null) {
/* 3167:3204 */       con.close();
/* 3168:     */     }
/* 3169:3207 */     return id;
/* 3170:     */   }
/* 3171:     */   
/* 3172:     */   public static String resolveShortItemReference(String shortItemRef, String accountId, String keyword)
/* 3173:     */     throws Exception
/* 3174:     */   {
/* 3175:3211 */     String id = null;
/* 3176:3212 */     String query = null;
/* 3177:3213 */     ResultSet rs = null;
/* 3178:3214 */     Connection con = null;
/* 3179:3215 */     PreparedStatement prepstat = null;
/* 3180:     */     try
/* 3181:     */     {
/* 3182:3218 */       con = DConnect.getConnection();
/* 3183:3219 */       query = "select c.id from content_list c inner join service_definition s on s.service_type=c.content_type and s.account_id=c.list_id where c.list_id='" + accountId + "' and c.short_item_ref='" + shortItemRef + "' and s.keyword='" + keyword + "'";
/* 3184:     */       
/* 3185:3221 */       prepstat = con.prepareStatement(query);
/* 3186:3222 */       rs = prepstat.executeQuery();
/* 3187:3224 */       if (rs.next()) {
/* 3188:3225 */         id = rs.getString("c.id");
/* 3189:     */       }
/* 3190:     */     }
/* 3191:     */     catch (Exception ex)
/* 3192:     */     {
/* 3193:3228 */       if (con != null) {
/* 3194:3229 */         con.close();
/* 3195:     */       }
/* 3196:3231 */       throw new Exception(ex.getMessage());
/* 3197:     */     }
/* 3198:3233 */     if (con != null) {
/* 3199:3234 */       con.close();
/* 3200:     */     }
/* 3201:3237 */     return id;
/* 3202:     */   }
/* 3203:     */   
/* 3204:     */   public static void main(String[] args)
/* 3205:     */     throws Exception
/* 3206:     */   {
/* 3207:3388 */     int[] ids = { 5 };
/* 3208:     */     try
/* 3209:     */     {
/* 3210:3390 */       System.out.println(itemCountByType("kf007tg", ids));
/* 3211:     */     }
/* 3212:     */     catch (Exception e) {}
/* 3213:     */   }
/* 3214:     */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.ContentListDB
 * JD-Core Version:    0.7.0.1
 */