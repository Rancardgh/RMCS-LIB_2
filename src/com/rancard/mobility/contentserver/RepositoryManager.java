/*   1:    */ package com.rancard.mobility.contentserver;
/*   2:    */ 
/*   3:    */ import com.rancard.common.Config;
/*   4:    */ import com.rancard.mobility.contentserver.util.ArchiveManager;
/*   5:    */ import com.rancard.util.DataImportListener;
/*   6:    */ import com.rancard.util.Page;
/*   7:    */ import java.io.BufferedInputStream;
/*   8:    */ import java.io.BufferedOutputStream;
/*   9:    */ import java.io.File;
/*  10:    */ import java.io.IOException;
/*  11:    */ import java.io.InputStream;
/*  12:    */ import java.io.PrintStream;
/*  13:    */ import java.sql.Timestamp;
/*  14:    */ import java.util.ArrayList;
/*  15:    */ import java.util.HashMap;
/*  16:    */ import java.util.List;
/*  17:    */ import javax.naming.Context;
/*  18:    */ import javax.naming.InitialContext;
/*  19:    */ import javax.naming.NamingException;
/*  20:    */ import org.apache.commons.fileupload.FileItem;
/*  21:    */ import org.apache.commons.httpclient.HttpClient;
/*  22:    */ import org.apache.commons.httpclient.HttpException;
/*  23:    */ import org.apache.commons.httpclient.methods.GetMethod;
/*  24:    */ 
/*  25:    */ public class RepositoryManager
/*  26:    */ {
/*  27:    */   private String tmpFolder;
/*  28:    */   private String contentId;
/*  29:    */   private String id;
/*  30:    */   private String listId;
/*  31:    */   private String title;
/*  32:    */   private Integer type;
/*  33:    */   private String formats;
/*  34:    */   private String price;
/*  35:    */   private Integer category;
/*  36:    */   private String author;
/*  37:    */   private Timestamp date_added;
/*  38:    */   private int start;
/*  39: 52 */   private int count = 10;
/*  40:    */   
/*  41:    */   public RepositoryManager(String tempFolder)
/*  42:    */   {
/*  43: 56 */     this.tmpFolder = tempFolder;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public RepositoryManager()
/*  47:    */     throws Exception
/*  48:    */   {
/*  49: 60 */     if (this.tmpFolder == null) {
/*  50:    */       try
/*  51:    */       {
/*  52: 62 */         this.tmpFolder = getTempDirectory();
/*  53:    */       }
/*  54:    */       catch (Exception ex)
/*  55:    */       {
/*  56: 64 */         throw new Exception("Temporary directory not set");
/*  57:    */       }
/*  58:    */     }
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void addItem(ContentItem item)
/*  62:    */   {
/*  63: 71 */     ContentListDB db = new ContentListDB();
/*  64:    */   }
/*  65:    */   
/*  66:    */   public void updateItem(String listId, String id, String author, String title, byte[] previewFile)
/*  67:    */   {
/*  68: 78 */     ContentListDB db = new ContentListDB();
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void updateItem(ContentItem item)
/*  72:    */   {
/*  73: 84 */     ContentListDB db = new ContentListDB();
/*  74:    */   }
/*  75:    */   
/*  76:    */   public void viewItem() {}
/*  77:    */   
/*  78:    */   public void deleteItem(String id, String listId)
/*  79:    */     throws Exception
/*  80:    */   {
/*  81: 92 */     ContentListDB cldb = new ContentListDB();
/*  82:    */     try
/*  83:    */     {
/*  84: 95 */       cldb.deleteContentListItem(id, listId);
/*  85:    */     }
/*  86:    */     catch (Exception ex)
/*  87:    */     {
/*  88: 97 */       System.out.println(ex.getMessage());
/*  89:    */     }
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void deleteItem(String[] id, String listId)
/*  93:    */     throws Exception
/*  94:    */   {
/*  95:106 */     ContentListDB cldb = new ContentListDB();
/*  96:    */     try
/*  97:    */     {
/*  98:109 */       cldb.deleteContentListItem(id, listId);
/*  99:    */     }
/* 100:    */     catch (Exception ex)
/* 101:    */     {
/* 102:111 */       System.out.println(ex.getMessage());
/* 103:    */     }
/* 104:    */   }
/* 105:    */   
/* 106:    */   public void FindItem() {}
/* 107:    */   
/* 108:    */   public ArrayList createPreviews(String[] contentIds, String listId)
/* 109:    */     throws Exception
/* 110:    */   {
/* 111:122 */     ContentListDB cdb = new ContentListDB();
/* 112:    */     
/* 113:    */ 
/* 114:125 */     ContentAdapter ca = new ContentAdapter();
/* 115:    */     
/* 116:127 */     ArrayList validFiles = new ArrayList();
/* 117:    */     
/* 118:129 */     String path = getTmpFolder();
/* 119:    */     
/* 120:131 */     File workDirectory = new File(path);
/* 121:132 */     if ((!workDirectory.exists()) || (!workDirectory.isDirectory())) {
/* 122:134 */       if (!workDirectory.mkdir()) {
/* 123:135 */         throw new Exception("Unable to find work directory. Please check work directory settings in properties file");
/* 124:    */       }
/* 125:    */     }
/* 126:140 */     File userWorkDirectory = new File(workDirectory.getAbsolutePath() + File.separator + listId);
/* 127:142 */     if (((!userWorkDirectory.exists()) || (!userWorkDirectory.isDirectory())) && 
/* 128:143 */       (!userWorkDirectory.mkdirs())) {
/* 129:144 */       throw new Exception("Unable to create work directory for content provider with listId" + listId + " . Check security permissions");
/* 130:    */     }
/* 131:150 */     for (int i = 0; i < contentIds.length; i++)
/* 132:    */     {
/* 133:151 */       File tmp = null;
/* 134:152 */       uploadsBean contentItem = new uploadsBean();
/* 135:    */       
/* 136:    */ 
/* 137:155 */       contentItem = fetchFile(listId, contentIds[i]);
/* 138:    */       
/* 139:157 */       contentItem = ca.generatePreview(contentItem);
/* 140:    */       
/* 141:159 */       tmp = ContentAdapter.writeBytesToFile(contentItem.getDataStream(), userWorkDirectory.getAbsolutePath() + File.separator + contentIds[i]);
/* 142:163 */       if (tmp.exists())
/* 143:    */       {
/* 144:164 */         tmp.getAbsolutePath();
/* 145:    */         
/* 146:166 */         validFiles.add(contentIds[i]);
/* 147:    */       }
/* 148:    */     }
/* 149:171 */     cdb.loadPreviewFiles(validFiles, listId, userWorkDirectory.getAbsolutePath());
/* 150:    */     
/* 151:    */ 
/* 152:174 */     return validFiles;
/* 153:    */   }
/* 154:    */   
/* 155:    */   public void ImportContent(FileItem fi, String contentProviderId, Integer type)
/* 156:    */     throws Exception
/* 157:    */   {
/* 158:178 */     if (!fi.isFormField())
/* 159:    */     {
/* 160:179 */       String filename = fi.getName();
/* 161:183 */       if ((filename != null) && (filename.endsWith(".zip")))
/* 162:    */       {
/* 163:188 */         ArchiveManager zipmanager = new ArchiveManager(getTmpFolder());
/* 164:    */         
/* 165:190 */         zipmanager.unZipToContentRepository(fi.getInputStream(), fi.getName(), contentProviderId, type);
/* 166:    */       }
/* 167:    */       else
/* 168:    */       {
/* 169:195 */         throw new Exception("Invalid file Archive. File must be a zip file");
/* 170:    */       }
/* 171:    */     }
/* 172:    */   }
/* 173:    */   
/* 174:    */   public void ImportContent(FileItem fi, String contentProviderId, Integer type, String supplierId)
/* 175:    */     throws Exception
/* 176:    */   {
/* 177:205 */     if (!fi.isFormField())
/* 178:    */     {
/* 179:206 */       String filename = fi.getName();
/* 180:210 */       if ((filename != null) && (filename.endsWith(".zip")))
/* 181:    */       {
/* 182:215 */         ArchiveManager zipmanager = new ArchiveManager(getTmpFolder());
/* 183:    */         
/* 184:217 */         zipmanager.unZipToContentRepository(fi.getInputStream(), fi.getName(), contentProviderId, type, supplierId);
/* 185:    */       }
/* 186:    */       else
/* 187:    */       {
/* 188:222 */         throw new Exception("Invalid file Archive. File must be a zip file");
/* 189:    */       }
/* 190:    */     }
/* 191:    */   }
/* 192:    */   
/* 193:    */   public void ImportContent(FileItem fi, String contentProviderId)
/* 194:    */     throws Exception
/* 195:    */   {
/* 196:232 */     if (!fi.isFormField())
/* 197:    */     {
/* 198:233 */       String filename = fi.getName();
/* 199:237 */       if ((filename != null) && (filename.endsWith(".zip")))
/* 200:    */       {
/* 201:242 */         ArchiveManager zipmanager = new ArchiveManager(getTmpFolder());
/* 202:    */         
/* 203:244 */         zipmanager.unZipToContentRepository(fi.getInputStream(), fi.getName(), contentProviderId);
/* 204:    */       }
/* 205:    */       else
/* 206:    */       {
/* 207:249 */         throw new Exception("Invalid file Archive. File must be a zip file");
/* 208:    */       }
/* 209:    */     }
/* 210:    */   }
/* 211:    */   
/* 212:    */   public Page ViewContentList()
/* 213:    */     throws Exception
/* 214:    */   {
/* 215:263 */     ContentListDB content_list = new ContentListDB();
/* 216:    */     
/* 217:265 */     Page pg = Page.EMPTY_PAGE;
/* 218:266 */     return content_list.viewContentList(this.listId, this.title, this.formats, this.type, this.start, this.count);
/* 219:    */   }
/* 220:    */   
/* 221:    */   public Page ViewContentList(String listId, String title, String formats, Integer type, int start, int count)
/* 222:    */     throws Exception
/* 223:    */   {
/* 224:275 */     ContentListDB content_list = new ContentListDB();
/* 225:    */     
/* 226:277 */     Page pg = Page.EMPTY_PAGE;
/* 227:278 */     return content_list.viewContentList(listId, title, formats, type, start, count);
/* 228:    */   }
/* 229:    */   
/* 230:    */   public Page SearchContentList(String listId, String title, String format, Integer type, String author, String price, Integer category, int isFree, int start, int count)
/* 231:    */     throws Exception
/* 232:    */   {
/* 233:285 */     ContentListDB content_list = new ContentListDB();
/* 234:    */     
/* 235:287 */     Page pg = Page.EMPTY_PAGE;
/* 236:288 */     return content_list.searchContentList(listId, title, format, type, author, price, category, isFree, start, count);
/* 237:    */   }
/* 238:    */   
/* 239:    */   public static Page searchContentListByPhone(String ownerId, String title, String formats, Integer type, String author, String price, Integer category, String phoneId, int isFree, String shortItemRef, String supplierId, int start, int count)
/* 240:    */     throws Exception
/* 241:    */   {
/* 242:293 */     return new ContentListDB().searchContentListByPhone(ownerId, title, formats, type, author, price, category, phoneId, isFree, shortItemRef, supplierId, start, count);
/* 243:    */   }
/* 244:    */   
/* 245:    */   public static Page searchContentListBySite(String ownerId, String title, String formats, Integer type, String author, String price, Integer category, int isFree, String shortItemRef, String supplierId, int start, int count)
/* 246:    */     throws Exception
/* 247:    */   {
/* 248:298 */     return new ContentListDB().searchContentListBySite(ownerId, title, formats, type, author, price, category, isFree, shortItemRef, supplierId, start, count);
/* 249:    */   }
/* 250:    */   
/* 251:    */   public static Page searchContentListByContentSubscriber(String csid, String listId, String title, String formats, Integer type, String author, String price, Integer category, int isFree, int start, int count)
/* 252:    */     throws Exception
/* 253:    */   {
/* 254:303 */     return new ContentListDB().searchContentListByContentSubscriber(csid, listId, title, formats, type, author, price, category, isFree, start, count);
/* 255:    */   }
/* 256:    */   
/* 257:    */   public static Page searchContentList(String ownerId, String title, String formats, Integer type, String author, String price, Integer category, int isFree, String shortItemRef, String supplierId, int start, int count)
/* 258:    */     throws Exception
/* 259:    */   {
/* 260:308 */     return new ContentListDB().searchContentList(ownerId, title, formats, type, author, price, category, isFree, shortItemRef, supplierId, start, count);
/* 261:    */   }
/* 262:    */   
/* 263:    */   public List getDistinctListIDs(String queryEnding)
/* 264:    */   {
/* 265:312 */     List listIDs = new ArrayList();
/* 266:    */     try
/* 267:    */     {
/* 268:314 */       listIDs = new ContentListDB().getDistinctListIDs(queryEnding);
/* 269:    */     }
/* 270:    */     catch (Exception e)
/* 271:    */     {
/* 272:317 */       System.out.println("Exception at getDistinctListIDs(): " + e.getMessage());
/* 273:    */     }
/* 274:320 */     return listIDs;
/* 275:    */   }
/* 276:    */   
/* 277:    */   public ContentItem viewContentItem(String id, String listId)
/* 278:    */     throws Exception
/* 279:    */   {
/* 280:324 */     return new ContentListDB().viewcontent_list(id, listId);
/* 281:    */   }
/* 282:    */   
/* 283:    */   public ArrayList viewContentItem(String[] id, String listId)
/* 284:    */     throws Exception
/* 285:    */   {
/* 286:328 */     return new ContentListDB().viewContentList(id, listId);
/* 287:    */   }
/* 288:    */   
/* 289:    */   public ArrayList viewContentList(String[] id, String[] listId)
/* 290:    */     throws Exception
/* 291:    */   {
/* 292:332 */     return new ContentListDB().viewContentList(id, listId);
/* 293:    */   }
/* 294:    */   
/* 295:    */   public ContentItem viewContentItem(String contentId)
/* 296:    */     throws Exception
/* 297:    */   {
/* 298:336 */     return new ContentListDB().viewcontent_list(contentId);
/* 299:    */   }
/* 300:    */   
/* 301:    */   public String viewContentList()
/* 302:    */   {
/* 303:340 */     return "";
/* 304:    */   }
/* 305:    */   
/* 306:    */   public String[][] getCPsForTypes()
/* 307:    */     throws Exception
/* 308:    */   {
/* 309:344 */     return ContentListDB.getCPsForTypes();
/* 310:    */   }
/* 311:    */   
/* 312:    */   public static HashMap getMyProviders(String siteId)
/* 313:    */     throws Exception
/* 314:    */   {
/* 315:348 */     return ContentListDB.getMyProviders(siteId);
/* 316:    */   }
/* 317:    */   
/* 318:    */   public ArrayList getRecentlyAdded(int typeId, int count)
/* 319:    */     throws Exception
/* 320:    */   {
/* 321:352 */     return new ContentListDB().getRecentlyAdded(typeId, count);
/* 322:    */   }
/* 323:    */   
/* 324:    */   public void updateContentItem(String id, String price, Integer category, String author, String listId)
/* 325:    */     throws Exception
/* 326:    */   {
/* 327:358 */     ContentListDB db = new ContentListDB();
/* 328:359 */     db.updateContentItem(id, price, category, author, listId);
/* 329:    */   }
/* 330:    */   
/* 331:    */   public void updateContentItem(String id, String price, String title, Integer category, String author, boolean isFree, String shortItemRef, String listId)
/* 332:    */     throws Exception
/* 333:    */   {
/* 334:365 */     ContentListDB db = new ContentListDB();
/* 335:366 */     db.updateContentItem(id, price, title, category, author, isFree, shortItemRef, listId);
/* 336:    */   }
/* 337:    */   
/* 338:    */   public void updateContentItemsPrice(String[] ids, String newprice, String listId)
/* 339:    */     throws Exception
/* 340:    */   {
/* 341:371 */     ContentListDB db = new ContentListDB();
/* 342:372 */     db.updateContentList(ids, newprice, listId);
/* 343:    */   }
/* 344:    */   
/* 345:    */   public void updateContentItemsCategory(String[] ids, Integer newcategory, String listId)
/* 346:    */     throws Exception
/* 347:    */   {
/* 348:378 */     ContentListDB db = new ContentListDB();
/* 349:379 */     db.updateContentList(ids, newcategory, listId);
/* 350:    */   }
/* 351:    */   
/* 352:    */   public void updateContentItemsAuthor(String[] ids, String newauthor, String listId)
/* 353:    */     throws Exception
/* 354:    */   {
/* 355:383 */     ContentListDB db = new ContentListDB();
/* 356:384 */     db.updateContentListAuthor(ids, newauthor, listId);
/* 357:    */   }
/* 358:    */   
/* 359:    */   public void updateContentListFreebie(String[] id, boolean isFree, String listId)
/* 360:    */     throws Exception
/* 361:    */   {
/* 362:388 */     ContentListDB db = new ContentListDB();
/* 363:389 */     db.updateContentListFreebie(id, isFree, listId);
/* 364:    */   }
/* 365:    */   
/* 366:    */   public String getTmpFolder()
/* 367:    */   {
/* 368:393 */     return this.tmpFolder;
/* 369:    */   }
/* 370:    */   
/* 371:    */   private String getTempDirectory()
/* 372:    */     throws Exception
/* 373:    */   {
/* 374:398 */     String tmpDirectory = "WEB-INF/work";
/* 375:    */     try
/* 376:    */     {
/* 377:400 */       Context ctx = new InitialContext();
/* 378:401 */       Config appConfig = (Config)ctx.lookup("java:comp/env/bean/rmcsConfig");
/* 379:403 */       if ((appConfig != null) && (appConfig.valueOf("TMPDIR") != null)) {
/* 380:404 */         tmpDirectory = appConfig.valueOf("TMPDIR");
/* 381:    */       }
/* 382:    */     }
/* 383:    */     catch (NamingException e)
/* 384:    */     {
/* 385:410 */       throw new Exception(e.getMessage());
/* 386:    */     }
/* 387:413 */     return tmpDirectory;
/* 388:    */   }
/* 389:    */   
/* 390:    */   public static uploadsBean fetchFile(String list_id, String id)
/* 391:    */     throws Exception
/* 392:    */   {
/* 393:418 */     return new uploadsDB().viewuploads(list_id, id);
/* 394:    */   }
/* 395:    */   
/* 396:    */   public static byte[] getByteArray(String urlstr)
/* 397:    */     throws Exception
/* 398:    */   {
/* 399:424 */     InputStream in = null;
/* 400:425 */     BufferedInputStream bis = null;
/* 401:426 */     BufferedOutputStream bos = null;
/* 402:427 */     HttpClient httpclient = new HttpClient();
/* 403:428 */     GetMethod httpget = new GetMethod(urlstr);
/* 404:    */     
/* 405:430 */     byte[] buff = null;
/* 406:    */     try
/* 407:    */     {
/* 408:432 */       int statusCode = httpclient.executeMethod(httpget);
/* 409:433 */       if (statusCode != 200) {
/* 410:434 */         throw new HttpException(new Integer(statusCode).toString());
/* 411:    */       }
/* 412:437 */       long resplength = httpget.getResponseContentLength();
/* 413:438 */       Long size = new Long(resplength);
/* 414:439 */       int length = size.intValue();
/* 415:    */       
/* 416:    */ 
/* 417:442 */       in = httpget.getResponseBodyAsStream();
/* 418:443 */       bis = new BufferedInputStream(in);
/* 419:    */       
/* 420:445 */       buff = new byte[length];
/* 421:    */       int bytesRead;
/* 422:449 */       while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {}
/* 423:    */     }
/* 424:    */     catch (Exception e)
/* 425:    */     {
/* 426:453 */       throw new Exception();
/* 427:    */     }
/* 428:    */     finally
/* 429:    */     {
/* 430:455 */       httpget.releaseConnection();
/* 431:    */       try
/* 432:    */       {
/* 433:457 */         if (bis != null) {
/* 434:458 */           bis.close();
/* 435:    */         }
/* 436:460 */         if (bos != null) {
/* 437:461 */           bos.close();
/* 438:    */         }
/* 439:    */       }
/* 440:    */       catch (IOException e) {}
/* 441:465 */       if (in != null) {
/* 442:    */         try
/* 443:    */         {
/* 444:467 */           in.close();
/* 445:    */         }
/* 446:    */         catch (IOException e) {}
/* 447:    */       }
/* 448:    */     }
/* 449:472 */     return buff;
/* 450:    */   }
/* 451:    */   
/* 452:    */   public static String getHexString(byte[] item)
/* 453:    */     throws Exception
/* 454:    */   {
/* 455:476 */     String ErrorStr = null;
/* 456:477 */     String msg = new String();
/* 457:    */     
/* 458:479 */     byte[] b = item;
/* 459:    */     
/* 460:481 */     String hex = bytesToHex(b);
/* 461:482 */     System.out.println(hex);
/* 462:    */     
/* 463:    */ 
/* 464:485 */     return hex;
/* 465:    */   }
/* 466:    */   
/* 467:    */   private static String bytesToHex(byte[] data)
/* 468:    */   {
/* 469:489 */     StringBuffer buf = new StringBuffer();
/* 470:490 */     for (int i = 0; i < data.length; i++) {
/* 471:491 */       buf.append(byteToHex(data[i]));
/* 472:    */     }
/* 473:493 */     return buf.toString();
/* 474:    */   }
/* 475:    */   
/* 476:    */   private static String byteToHex(byte data)
/* 477:    */   {
/* 478:498 */     StringBuffer buf = new StringBuffer();
/* 479:499 */     buf.append(toHexChar(data >>> 4 & 0xF));
/* 480:500 */     buf.append(toHexChar(data & 0xF));
/* 481:501 */     return buf.toString();
/* 482:    */   }
/* 483:    */   
/* 484:    */   private static char toHexChar(int i)
/* 485:    */   {
/* 486:506 */     if ((0 <= i) && (i <= 9)) {
/* 487:507 */       return (char)(48 + i);
/* 488:    */     }
/* 489:509 */     return (char)(65 + (i - 10));
/* 490:    */   }
/* 491:    */   
/* 492:    */   public static String getUD(String format, ContentItem item)
/* 493:    */     throws Exception
/* 494:    */   {
/* 495:515 */     String ud = null;
/* 496:516 */     byte[] byteStream = null;
/* 497:518 */     if (!item.islocal())
/* 498:    */     {
/* 499:519 */       byteStream = getByteArray(item.getDownloadUrl());
/* 500:    */     }
/* 501:    */     else
/* 502:    */     {
/* 503:521 */       new RepositoryManager();uploadsBean upload = fetchFile(item.getListId(), item.getid());
/* 504:522 */       byteStream = upload.getDataStream();
/* 505:    */     }
/* 506:525 */     if ((format.equalsIgnoreCase("noktxt")) || (format.equalsIgnoreCase("ems"))) {
/* 507:526 */       ud = new String(byteStream);
/* 508:527 */     } else if ((format.equalsIgnoreCase("imy")) || (format.equalsIgnoreCase("ott")) || (format.equalsIgnoreCase("10.imy")) || (format.equalsIgnoreCase("12.imy"))) {
/* 509:529 */       ud = getHexString(byteStream);
/* 510:530 */     } else if (format.equalsIgnoreCase("bmp")) {
/* 511:    */       try
/* 512:    */       {
/* 513:532 */         ud = getHexString(new ContentAdapter().generateOTB(byteStream, new String(item.getid() + "-" + item.getListId())));
/* 514:    */       }
/* 515:    */       catch (Exception e)
/* 516:    */       {
/* 517:534 */         ud = "";
/* 518:    */       }
/* 519:    */       finally
/* 520:    */       {
/* 521:536 */         byteStream = null;
/* 522:    */       }
/* 523:    */     }
/* 524:539 */     if (ud.indexOf("%") != -1) {
/* 525:540 */       ud = ud.substring(0, ud.indexOf("%"));
/* 526:    */     }
/* 527:542 */     if (ud.indexOf("\r") != -1) {
/* 528:543 */       ud = ud.substring(0, ud.indexOf("\r"));
/* 529:    */     }
/* 530:545 */     if (ud.indexOf("\n") != -1) {
/* 531:546 */       ud = ud.substring(0, ud.indexOf("\n"));
/* 532:    */     }
/* 533:549 */     return ud;
/* 534:    */   }
/* 535:    */   
/* 536:    */   public static long totalDiskSpaceUsed(String list_id)
/* 537:    */     throws Exception
/* 538:    */   {
/* 539:555 */     return uploadsDB.totalDiskSpaceUsed(list_id);
/* 540:    */   }
/* 541:    */   
/* 542:    */   public static HashMap itemCountByType(String listId)
/* 543:    */     throws Exception
/* 544:    */   {
/* 545:562 */     return ContentListDB.itemCountByType(listId);
/* 546:    */   }
/* 547:    */   
/* 548:    */   public static int itemCountByType(String listId, int[] typeIDs)
/* 549:    */     throws Exception
/* 550:    */   {
/* 551:568 */     return ContentListDB.itemCountByType(listId, typeIDs);
/* 552:    */   }
/* 553:    */   
/* 554:    */   public static void uploadPreview(DataImportListener listener, List fileItems, String listId)
/* 555:    */     throws Exception
/* 556:    */   {
/* 557:572 */     uploadsDB.uploadPreview(listener, fileItems, listId);
/* 558:    */   }
/* 559:    */   
/* 560:    */   public static void createCustomList(String cpId, String customListId, String customListName)
/* 561:    */     throws Exception
/* 562:    */   {
/* 563:576 */     CustomListDB.createCustomList(cpId, customListId, customListName);
/* 564:    */   }
/* 565:    */   
/* 566:    */   public static void updateCustomList(String cpId, String customListId, String customListName)
/* 567:    */     throws Exception
/* 568:    */   {
/* 569:580 */     CustomListDB.updateCustomList(cpId, customListId, customListName);
/* 570:    */   }
/* 571:    */   
/* 572:    */   public static void deleteCustomList(String customListId)
/* 573:    */     throws Exception
/* 574:    */   {
/* 575:584 */     CustomListDB.deleteCustomList(customListId);
/* 576:    */   }
/* 577:    */   
/* 578:    */   public static void deleteCustomLists(String[] customListId)
/* 579:    */     throws Exception
/* 580:    */   {
/* 581:588 */     CustomListDB.deleteCustomLists(customListId);
/* 582:    */   }
/* 583:    */   
/* 584:    */   public static void deleteCustomLists(String cpId)
/* 585:    */     throws Exception
/* 586:    */   {
/* 587:592 */     CustomListDB.deleteCustomLists(cpId);
/* 588:    */   }
/* 589:    */   
/* 590:    */   public static CustomList viewCustomList(String cpId, String customListId)
/* 591:    */     throws Exception
/* 592:    */   {
/* 593:596 */     return CustomListDB.viewCustomList(cpId, customListId);
/* 594:    */   }
/* 595:    */   
/* 596:    */   public static ArrayList viewCustomLists(String cpId, String[] customListIds)
/* 597:    */     throws Exception
/* 598:    */   {
/* 599:600 */     return CustomListDB.viewCustomLists(cpId, customListIds);
/* 600:    */   }
/* 601:    */   
/* 602:    */   public static ArrayList viewCustomLists(String cpId)
/* 603:    */     throws Exception
/* 604:    */   {
/* 605:604 */     return CustomListDB.viewCustomLists(cpId);
/* 606:    */   }
/* 607:    */   
/* 608:    */   public static ArrayList viewCustomListDefinitions(String cpId)
/* 609:    */     throws Exception
/* 610:    */   {
/* 611:608 */     return CustomListDB.viewCustomListDefinitions(cpId);
/* 612:    */   }
/* 613:    */   
/* 614:    */   public static void addItemsToList(String customListId, String[] ids, String[] listIds)
/* 615:    */     throws Exception
/* 616:    */   {
/* 617:612 */     CustomListDB.addItemsToList(customListId, ids, listIds);
/* 618:    */   }
/* 619:    */   
/* 620:    */   public static void removeItemsFromList(String customListId, String[] ids, String[] listIds)
/* 621:    */     throws Exception
/* 622:    */   {
/* 623:616 */     CustomListDB.removeItemsFromList(customListId, ids, listIds);
/* 624:    */   }
/* 625:    */   
/* 626:    */   public static void exportRecentAdditionsToCustomList(String cpid, int limit)
/* 627:    */     throws Exception
/* 628:    */   {
/* 629:620 */     ContentListDB.exportRecentAdditionsToCustomList(cpid, limit);
/* 630:    */   }
/* 631:    */   
/* 632:    */   public static ArrayList fetchRecentAdditions(String cpid, int typeId)
/* 633:    */     throws Exception
/* 634:    */   {
/* 635:624 */     return ContentListDB.fetchRecentAdditions(cpid, typeId);
/* 636:    */   }
/* 637:    */   
/* 638:    */   public static String getCpIdForSite(String siteId)
/* 639:    */     throws Exception
/* 640:    */   {
/* 641:628 */     return CPSiteDB.getCpIdForSite(siteId);
/* 642:    */   }
/* 643:    */   
/* 644:    */   public static String validateSupplier(String ownerId, String supplierKey)
/* 645:    */     throws Exception
/* 646:    */   {
/* 647:632 */     return ContentListDB.validateSupplier(ownerId, supplierKey);
/* 648:    */   }
/* 649:    */   
/* 650:    */   public static String resolveShortItemReference(String shortItemRef, String accountId, String keyword)
/* 651:    */     throws Exception
/* 652:    */   {
/* 653:636 */     return ContentListDB.resolveShortItemReference(shortItemRef, accountId, keyword);
/* 654:    */   }
/* 655:    */   
/* 656:    */   public static ArrayList getDistinctTypes(int parentServiceType, String accountId)
/* 657:    */     throws Exception
/* 658:    */   {
/* 659:640 */     return ContentTypeDB.getDistinctTypes(parentServiceType, accountId);
/* 660:    */   }
/* 661:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.RepositoryManager
 * JD-Core Version:    0.7.0.1
 */