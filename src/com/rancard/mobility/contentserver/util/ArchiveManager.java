/*   1:    */ package com.rancard.mobility.contentserver.util;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import com.rancard.common.uidGen;
/*   5:    */ import com.rancard.mobility.contentserver.ContentType;
/*   6:    */ import com.rancard.util.ExtManager;
/*   7:    */ import java.io.ByteArrayOutputStream;
/*   8:    */ import java.io.File;
/*   9:    */ import java.io.IOException;
/*  10:    */ import java.io.InputStream;
/*  11:    */ import java.io.PrintStream;
/*  12:    */ import java.sql.Connection;
/*  13:    */ import java.sql.PreparedStatement;
/*  14:    */ import java.sql.SQLException;
/*  15:    */ import java.sql.Statement;
/*  16:    */ import java.sql.Timestamp;
/*  17:    */ import java.util.SortedSet;
/*  18:    */ import java.util.TreeSet;
/*  19:    */ import java.util.zip.ZipEntry;
/*  20:    */ import java.util.zip.ZipFile;
/*  21:    */ import java.util.zip.ZipInputStream;
/*  22:    */ 
/*  23:    */ public class ArchiveManager
/*  24:    */ {
/*  25:    */   public static final int LIST = 0;
/*  26:    */   public static final int EXTRACT = 1;
/*  27: 93 */   protected int mode = 0;
/*  28:    */   protected ZipFile zippy;
/*  29:    */   protected String tmpFolder;
/*  30:    */   protected ZipInputStream zipStream;
/*  31:    */   protected byte[] b;
/*  32:105 */   protected boolean warnedMkDir = false;
/*  33:    */   protected SortedSet dirsMade;
/*  34:    */   
/*  35:    */   public void unZipToContentRepository(InputStream in, String fileName, String contentProviderId)
/*  36:    */   {
/*  37:109 */     ArchiveManager u = new ArchiveManager();
/*  38:110 */     u.setMode(1);
/*  39:111 */     String candidate = fileName;
/*  40:    */     try
/*  41:    */     {
/*  42:116 */       Connection conn = DConnect.getConnection();
/*  43:118 */       if ((candidate.endsWith(".zip")) || (candidate.endsWith(".jar"))) {
/*  44:119 */         u.unZip(in, conn, contentProviderId);
/*  45:    */       } else {
/*  46:121 */         System.err.println("Not a zip file? " + candidate);
/*  47:    */       }
/*  48:123 */       System.err.println("All done!");
/*  49:    */     }
/*  50:    */     catch (Exception ex)
/*  51:    */     {
/*  52:125 */       System.err.println(ex.getMessage());
/*  53:    */     }
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void unZipToContentRepository(InputStream in, String fileName, String contentProviderId, Integer type)
/*  57:    */   {
/*  58:131 */     ArchiveManager u = new ArchiveManager();
/*  59:132 */     u.setMode(1);
/*  60:133 */     String candidate = fileName;
/*  61:    */     
/*  62:    */ 
/*  63:136 */     Connection conn = null;
/*  64:    */     try
/*  65:    */     {
/*  66:138 */       conn = DConnect.getConnection();
/*  67:139 */       if ((candidate.endsWith(".zip")) || (candidate.endsWith(".jar"))) {
/*  68:140 */         u.unZip(in, conn, contentProviderId, type);
/*  69:    */       } else {
/*  70:142 */         System.err.println("Not a zip file? " + candidate);
/*  71:    */       }
/*  72:144 */       System.err.println("All done!"); return;
/*  73:    */     }
/*  74:    */     catch (Exception ex)
/*  75:    */     {
/*  76:146 */       System.err.println(ex.getMessage());
/*  77:147 */       if (conn != null) {
/*  78:    */         try
/*  79:    */         {
/*  80:149 */           conn.close();
/*  81:    */         }
/*  82:    */         catch (Exception e)
/*  83:    */         {
/*  84:151 */           conn = null;
/*  85:    */         }
/*  86:    */       }
/*  87:    */     }
/*  88:    */     finally
/*  89:    */     {
/*  90:156 */       if (conn != null) {
/*  91:    */         try
/*  92:    */         {
/*  93:158 */           conn.close();
/*  94:    */         }
/*  95:    */         catch (Exception e)
/*  96:    */         {
/*  97:160 */           conn = null;
/*  98:    */         }
/*  99:    */       }
/* 100:    */     }
/* 101:    */   }
/* 102:    */   
/* 103:    */   public void unZipToContentRepository(InputStream in, String fileName, String contentProviderId, Integer type, String supplierId)
/* 104:    */   {
/* 105:168 */     ArchiveManager u = new ArchiveManager();
/* 106:169 */     u.setMode(1);
/* 107:170 */     String candidate = fileName;
/* 108:    */     
/* 109:    */ 
/* 110:173 */     Connection conn = null;
/* 111:    */     try
/* 112:    */     {
/* 113:175 */       conn = DConnect.getConnection();
/* 114:176 */       if ((candidate.endsWith(".zip")) || (candidate.endsWith(".jar"))) {
/* 115:177 */         u.unZip(in, conn, contentProviderId, type, supplierId);
/* 116:    */       } else {
/* 117:179 */         System.err.println("Not a zip file? " + candidate);
/* 118:    */       }
/* 119:181 */       System.err.println("All done!"); return;
/* 120:    */     }
/* 121:    */     catch (Exception ex)
/* 122:    */     {
/* 123:183 */       System.err.println(ex.getMessage());
/* 124:184 */       if (conn != null) {
/* 125:    */         try
/* 126:    */         {
/* 127:186 */           conn.close();
/* 128:    */         }
/* 129:    */         catch (Exception e)
/* 130:    */         {
/* 131:188 */           conn = null;
/* 132:    */         }
/* 133:    */       }
/* 134:    */     }
/* 135:    */     finally
/* 136:    */     {
/* 137:193 */       if (conn != null) {
/* 138:    */         try
/* 139:    */         {
/* 140:195 */           conn.close();
/* 141:    */         }
/* 142:    */         catch (Exception e)
/* 143:    */         {
/* 144:197 */           conn = null;
/* 145:    */         }
/* 146:    */       }
/* 147:    */     }
/* 148:    */   }
/* 149:    */   
/* 150:    */   public ArchiveManager()
/* 151:    */   {
/* 152:206 */     this.b = new byte[8092];
/* 153:    */   }
/* 154:    */   
/* 155:    */   public ArchiveManager(String tmpFolder)
/* 156:    */   {
/* 157:211 */     this.b = new byte[8092];
/* 158:212 */     this.tmpFolder = tmpFolder;
/* 159:    */   }
/* 160:    */   
/* 161:    */   protected void setMode(int m)
/* 162:    */   {
/* 163:217 */     if ((m == 0) || (m == 1)) {
/* 164:218 */       this.mode = m;
/* 165:    */     }
/* 166:    */   }
/* 167:    */   
/* 168:    */   public void unZip(InputStream in)
/* 169:    */   {
/* 170:227 */     this.dirsMade = new TreeSet();
/* 171:    */     try
/* 172:    */     {
/* 173:230 */       this.zipStream = new ZipInputStream(in);
/* 174:237 */       while (0 != this.zipStream.available())
/* 175:    */       {
/* 176:238 */         getFile(this.zipStream.getNextEntry());
/* 177:239 */         this.zipStream.closeEntry();
/* 178:    */       }
/* 179:242 */       this.zipStream.close();
/* 180:    */     }
/* 181:    */     catch (IOException err)
/* 182:    */     {
/* 183:245 */       System.err.println("IO Error: " + err);
/* 184:246 */       return;
/* 185:    */     }
/* 186:    */     catch (SQLException ex) {}
/* 187:    */   }
/* 188:    */   
/* 189:    */   public String createTempTable(Connection conn)
/* 190:    */     throws Exception
/* 191:    */   {
/* 192:253 */     new uidGen();String tablename = uidGen.getUId();
/* 193:    */     
/* 194:255 */     String SQL = "CREATE TEMPORARY TABLE UPLOADS (UPLOADID INT NOT NULL, FILENAME VARCHAR(255), BINARYFILE LONGBLOB, PRIMARY KEY (UPLOADID) ); ";
/* 195:    */     try
/* 196:    */     {
/* 197:262 */       Statement stmt = conn.createStatement();
/* 198:263 */       stmt.execute(SQL);
/* 199:    */     }
/* 200:    */     catch (Exception ex)
/* 201:    */     {
/* 202:265 */       if (conn != null) {
/* 203:266 */         conn.close();
/* 204:    */       }
/* 205:269 */       tablename = null;
/* 206:    */     }
/* 207:272 */     if (conn != null) {
/* 208:273 */       conn.close();
/* 209:    */     }
/* 210:276 */     return tablename;
/* 211:    */   }
/* 212:    */   
/* 213:    */   protected void unZip(InputStream in, Connection conn, String contenProviderId)
/* 214:    */     throws Exception
/* 215:    */   {
/* 216:282 */     this.dirsMade = new TreeSet();
/* 217:    */     try
/* 218:    */     {
/* 219:284 */       this.zipStream = new ZipInputStream(in);
/* 220:    */       
/* 221:286 */       String tablename = contenProviderId;
/* 222:    */       ZipEntry z;
/* 223:294 */       while ((z = this.zipStream.getNextEntry()) != null)
/* 224:    */       {
/* 225:295 */         byte[] buf = new byte[1024];
/* 226:    */         
/* 227:297 */         ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 228:    */         int len;
/* 229:299 */         while ((len = this.zipStream.read(buf)) > 0) {
/* 230:300 */           bos.write(buf, 0, len);
/* 231:    */         }
/* 232:302 */         byte[] unzippedFile = bos.toByteArray();
/* 233:303 */         bos.close();
/* 234:    */         
/* 235:305 */         write(z, unzippedFile, conn, tablename);
/* 236:    */       }
/* 237:311 */       this.zipStream.close();
/* 238:    */     }
/* 239:    */     catch (IOException err)
/* 240:    */     {
/* 241:314 */       System.err.println("IO Error: " + err);
/* 242:315 */       return;
/* 243:    */     }
/* 244:    */     catch (SQLException ex) {}
/* 245:    */   }
/* 246:    */   
/* 247:    */   protected void unZip(InputStream in, Connection conn, String contenProviderId, Integer type)
/* 248:    */     throws Exception
/* 249:    */   {
/* 250:325 */     this.dirsMade = new TreeSet();
/* 251:    */     try
/* 252:    */     {
/* 253:327 */       this.zipStream = new ZipInputStream(in);
/* 254:    */       
/* 255:329 */       String tablename = contenProviderId;
/* 256:    */       ZipEntry z;
/* 257:337 */       while ((z = this.zipStream.getNextEntry()) != null)
/* 258:    */       {
/* 259:338 */         byte[] buf = new byte[1024];
/* 260:    */         
/* 261:340 */         ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 262:    */         int len;
/* 263:342 */         while ((len = this.zipStream.read(buf)) > 0) {
/* 264:343 */           bos.write(buf, 0, len);
/* 265:    */         }
/* 266:345 */         byte[] unzippedFile = bos.toByteArray();
/* 267:346 */         bos.close();
/* 268:    */         
/* 269:    */ 
/* 270:349 */         write(z, unzippedFile, conn, tablename, type.intValue());
/* 271:    */       }
/* 272:355 */       this.zipStream.close();
/* 273:    */     }
/* 274:    */     catch (IOException err)
/* 275:    */     {
/* 276:358 */       System.err.println("IO Error: " + err);
/* 277:359 */       return;
/* 278:    */     }
/* 279:    */     catch (SQLException ex) {}
/* 280:    */   }
/* 281:    */   
/* 282:    */   protected void unZip(InputStream in, Connection conn, String contenProviderId, Integer type, String supplierId)
/* 283:    */     throws Exception
/* 284:    */   {
/* 285:368 */     this.dirsMade = new TreeSet();
/* 286:    */     try
/* 287:    */     {
/* 288:370 */       this.zipStream = new ZipInputStream(in);
/* 289:    */       
/* 290:372 */       String tablename = contenProviderId;
/* 291:    */       ZipEntry z;
/* 292:380 */       while ((z = this.zipStream.getNextEntry()) != null)
/* 293:    */       {
/* 294:381 */         byte[] buf = new byte[1024];
/* 295:    */         
/* 296:383 */         ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 297:    */         int len;
/* 298:385 */         while ((len = this.zipStream.read(buf)) > 0) {
/* 299:386 */           bos.write(buf, 0, len);
/* 300:    */         }
/* 301:388 */         byte[] unzippedFile = bos.toByteArray();
/* 302:389 */         bos.close();
/* 303:    */         
/* 304:    */ 
/* 305:392 */         write(z, unzippedFile, conn, tablename, type.intValue(), supplierId);
/* 306:    */       }
/* 307:398 */       this.zipStream.close();
/* 308:    */     }
/* 309:    */     catch (IOException err)
/* 310:    */     {
/* 311:401 */       System.err.println("IO Error: " + err);
/* 312:402 */       return;
/* 313:    */     }
/* 314:    */     catch (SQLException ex) {}
/* 315:    */   }
/* 316:    */   
/* 317:    */   protected void getFile(ZipEntry e)
/* 318:    */     throws IOException, SQLException
/* 319:    */   {
/* 320:415 */     String zipName = e.getName();
/* 321:416 */     switch (this.mode)
/* 322:    */     {
/* 323:    */     case 1: 
/* 324:418 */       if (zipName.startsWith("/"))
/* 325:    */       {
/* 326:419 */         if (!this.warnedMkDir) {
/* 327:420 */           System.out.println("Ignoring absolute paths");
/* 328:    */         }
/* 329:422 */         this.warnedMkDir = true;
/* 330:423 */         zipName = zipName.substring(1);
/* 331:    */       }
/* 332:429 */       if (zipName.endsWith("/")) {
/* 333:430 */         return;
/* 334:    */       }
/* 335:435 */       int ix = zipName.lastIndexOf('/');
/* 336:436 */       if (ix > 0)
/* 337:    */       {
/* 338:437 */         String dirName = zipName.substring(0, ix);
/* 339:438 */         if (!this.dirsMade.contains(dirName))
/* 340:    */         {
/* 341:439 */           File d = new File(dirName);
/* 342:441 */           if ((!d.exists()) || (!d.isDirectory()))
/* 343:    */           {
/* 344:443 */             System.out.println("Creating Directory: " + dirName);
/* 345:444 */             if (!d.mkdirs()) {
/* 346:445 */               System.err.println("Warning: unable to mkdir " + dirName);
/* 347:    */             }
/* 348:448 */             this.dirsMade.add(dirName);
/* 349:    */           }
/* 350:    */         }
/* 351:    */       }
/* 352:452 */       System.err.println("Creating " + zipName);
/* 353:    */       
/* 354:    */ 
/* 355:455 */       String filename = e.getName();
/* 356:    */       
/* 357:    */ 
/* 358:458 */       long size = e.getSize();
/* 359:    */       
/* 360:    */ 
/* 361:461 */       String extension = null;
/* 362:462 */       int x = filename.lastIndexOf(".");
/* 363:463 */       if (x != 0) {
/* 364:464 */         extension = filename.substring(x + 1, filename.length());
/* 365:    */       }
/* 366:468 */       int type = new ContentType().getTypeCode(extension);
/* 367:    */       
/* 368:    */ 
/* 369:471 */       InputStream is = this.zippy.getInputStream(e);
/* 370:472 */       ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 371:    */       
/* 372:    */ 
/* 373:475 */       byte[] buff = new byte[32768];
/* 374:    */       int bytesRead;
/* 375:480 */       while (-1 != (bytesRead = is.read(buff, 0, 32768))) {
/* 376:482 */         bos.write(buff, 0, bytesRead);
/* 377:    */       }
/* 378:485 */       byte[] zipedfile = bos.toByteArray();
/* 379:    */       
/* 380:487 */       is.close();
/* 381:    */       try
/* 382:    */       {
/* 383:491 */         Connection conn = DConnect.getConnection();
/* 384:    */         
/* 385:    */ 
/* 386:494 */         new uidGen();String ID = uidGen.getUId();
/* 387:495 */         String listId = "001";
/* 388:    */         
/* 389:497 */         PreparedStatement pstmt = conn.prepareStatement("insert into uploads\t(id, filename,binaryfile,list_id) \tvalues \t(?, ?,?, ? ,?)");
/* 390:    */         
/* 391:499 */         pstmt.setString(1, ID);
/* 392:500 */         int executeStatus = -3;
/* 393:    */         
/* 394:    */ 
/* 395:503 */         pstmt.setString(2, zipName);
/* 396:    */         
/* 397:505 */         pstmt.setBytes(3, zipedfile);
/* 398:    */         
/* 399:507 */         pstmt.setString(4, listId);
/* 400:508 */         executeStatus = pstmt.executeUpdate();
/* 401:510 */         if (executeStatus != -3)
/* 402:    */         {
/* 403:513 */           String qry = "insert into content_list (id, content_id, title,size,  price, list_id, date_added  ) values (?, ?, ?, ?, ?,?, ?)";
/* 404:    */           
/* 405:    */ 
/* 406:516 */           pstmt = conn.prepareStatement(qry);
/* 407:517 */           pstmt.setString(1, ID);
/* 408:518 */           new uidGen();pstmt.setString(2, uidGen.getUId());
/* 409:519 */           pstmt.setString(3, zipName);
/* 410:520 */           pstmt.setInt(4, 3000);
/* 411:521 */           pstmt.setString(5, "1500");
/* 412:522 */           pstmt.setString(6, listId);
/* 413:523 */           pstmt.setDate(7, new java.sql.Date(new java.util.Date().getTime()));
/* 414:    */           
/* 415:    */ 
/* 416:526 */           pstmt.execute();
/* 417:    */         }
/* 418:    */       }
/* 419:    */       catch (Exception ex) {}
/* 420:533 */       is.close();
/* 421:    */       
/* 422:    */ 
/* 423:536 */       break;
/* 424:    */     case 0: 
/* 425:540 */       if (e.isDirectory()) {
/* 426:541 */         System.out.println("Directory " + zipName);
/* 427:    */       } else {
/* 428:543 */         System.out.println("File " + zipName);
/* 429:    */       }
/* 430:545 */       break;
/* 431:    */     default: 
/* 432:547 */       throw new IllegalStateException("mode value (" + this.mode + ") bad");
/* 433:    */     }
/* 434:    */   }
/* 435:    */   
/* 436:    */   protected void write(ZipEntry e, byte[] zipedfile, Connection conn, String tableName)
/* 437:    */     throws IOException, SQLException, Exception
/* 438:    */   {
/* 439:558 */     String zipName = e.getName();
/* 440:560 */     if (zipName.startsWith("/"))
/* 441:    */     {
/* 442:561 */       if (!this.warnedMkDir) {
/* 443:562 */         System.out.println("Ignoring absolute paths");
/* 444:    */       }
/* 445:564 */       this.warnedMkDir = true;
/* 446:565 */       zipName = zipName.substring(1);
/* 447:    */     }
/* 448:571 */     if (zipName.endsWith("/")) {
/* 449:572 */       return;
/* 450:    */     }
/* 451:575 */     System.out.println("Creating " + zipName);
/* 452:    */     
/* 453:577 */     String filename = e.getName();
/* 454:578 */     filename = zipName.substring(zipName.lastIndexOf("/") + 1, zipName.length());
/* 455:    */     
/* 456:    */ 
/* 457:    */ 
/* 458:582 */     long size = e.getSize();
/* 459:    */     
/* 460:584 */     String extension = null;
/* 461:585 */     int x = filename.lastIndexOf(".");
/* 462:586 */     if (x != 0) {
/* 463:587 */       extension = filename.substring(x + 1, filename.length());
/* 464:    */     }
/* 465:590 */     int type = ExtManager.getTypeForFormat(extension);
/* 466:591 */     String formatid = ExtManager.getFormatFor(extension);
/* 467:    */     
/* 468:593 */     long time = e.getTime();
/* 469:596 */     if (type != 0) {
/* 470:    */       try
/* 471:    */       {
/* 472:600 */         new uidGen();String ID = uidGen.getUId();
/* 473:601 */         String listId = tableName;
/* 474:    */         
/* 475:603 */         PreparedStatement pstmt = conn.prepareStatement("insert into uploads  (id, filename,binaryfile,list_id) \tvalues \t(?, ?, ? ,?)");
/* 476:    */         
/* 477:605 */         pstmt.setString(1, ID);
/* 478:606 */         int executeStatus = -3;
/* 479:607 */         pstmt.setString(2, filename);
/* 480:608 */         pstmt.setBytes(3, zipedfile);
/* 481:609 */         pstmt.setString(4, listId);
/* 482:610 */         executeStatus = pstmt.executeUpdate();
/* 483:612 */         if (executeStatus != -3)
/* 484:    */         {
/* 485:614 */           String qry = "insert into content_list (id, content_id, title,size,  price, list_id, date_added ,content_type,formats,isLocal ) values (?, ?, ?, ?, ?,?, ?,?,?,?)";
/* 486:    */           
/* 487:    */ 
/* 488:617 */           pstmt = conn.prepareStatement(qry);
/* 489:618 */           pstmt.setString(1, ID);
/* 490:619 */           new uidGen();pstmt.setString(2, uidGen.getUId());
/* 491:620 */           pstmt.setString(3, zipName);
/* 492:621 */           pstmt.setLong(4, size);
/* 493:622 */           pstmt.setString(5, "0");
/* 494:623 */           pstmt.setString(6, listId);
/* 495:624 */           pstmt.setTimestamp(7, new Timestamp(time));
/* 496:625 */           pstmt.setInt(8, type);
/* 497:626 */           pstmt.setString(9, formatid);
/* 498:627 */           pstmt.setInt(10, 1);
/* 499:628 */           pstmt.execute();
/* 500:    */         }
/* 501:    */       }
/* 502:    */       catch (Exception ex)
/* 503:    */       {
/* 504:632 */         System.out.println(ex.getMessage());
/* 505:633 */         System.out.println("Error importing ringtone into database");
/* 506:    */       }
/* 507:    */     }
/* 508:    */   }
/* 509:    */   
/* 510:    */   protected void write(ZipEntry e, byte[] zipedfile, Connection conn, String tableName, int type)
/* 511:    */     throws IOException, SQLException, Exception
/* 512:    */   {
/* 513:646 */     String zipName = e.getName();
/* 514:648 */     if (zipName.startsWith("/"))
/* 515:    */     {
/* 516:649 */       if (!this.warnedMkDir) {
/* 517:650 */         System.out.println("Ignoring absolute paths");
/* 518:    */       }
/* 519:652 */       this.warnedMkDir = true;
/* 520:653 */       zipName = zipName.substring(1);
/* 521:    */     }
/* 522:659 */     if (zipName.endsWith("/")) {
/* 523:660 */       return;
/* 524:    */     }
/* 525:663 */     System.out.println("Creating " + zipName);
/* 526:    */     
/* 527:665 */     String filename = e.getName();
/* 528:666 */     filename = zipName.substring(zipName.lastIndexOf("/") + 1, zipName.length());
/* 529:    */     
/* 530:    */ 
/* 531:    */ 
/* 532:670 */     long size = e.getSize();
/* 533:    */     
/* 534:672 */     String extension = null;
/* 535:673 */     int x = filename.lastIndexOf(".");
/* 536:674 */     if (x != 0) {
/* 537:675 */       extension = filename.substring(x + 1, filename.length());
/* 538:    */     }
/* 539:677 */     String typeId = "" + type;
/* 540:678 */     String formatid = ExtManager.getFormatFor(extension);
/* 541:679 */     if (!ExtManager.match(typeId, formatid)) {
/* 542:680 */       type = ExtManager.getTypeForFormat(extension);
/* 543:    */     }
/* 544:683 */     long time = e.getTime();
/* 545:686 */     if (type != 0) {
/* 546:    */       try
/* 547:    */       {
/* 548:690 */         new uidGen();String ID = uidGen.generateNumberID(10);
/* 549:691 */         String listId = tableName;
/* 550:    */         
/* 551:693 */         PreparedStatement pstmt = conn.prepareStatement("insert into uploads  (id, filename,binaryfile,list_id) \tvalues \t(?, ?, ? ,?)");
/* 552:    */         
/* 553:695 */         pstmt.setString(1, ID);
/* 554:696 */         int executeStatus = -3;
/* 555:697 */         pstmt.setString(2, filename);
/* 556:698 */         pstmt.setBytes(3, zipedfile);
/* 557:699 */         pstmt.setString(4, listId);
/* 558:700 */         executeStatus = pstmt.executeUpdate();
/* 559:702 */         if (executeStatus != -3)
/* 560:    */         {
/* 561:704 */           String qry = "insert into content_list (id, content_id, title,size,  price, list_id, date_added ,content_type,formats,isLocal ) values (?, ?, ?, ?, ?,?, ?,?,?,?)";
/* 562:    */           
/* 563:706 */           pstmt = conn.prepareStatement(qry);
/* 564:707 */           pstmt.setString(1, ID);
/* 565:708 */           new uidGen();pstmt.setString(2, uidGen.getUId());
/* 566:709 */           pstmt.setString(3, zipName);
/* 567:710 */           pstmt.setLong(4, size);
/* 568:711 */           pstmt.setString(5, "0");
/* 569:712 */           pstmt.setString(6, listId);
/* 570:713 */           pstmt.setTimestamp(7, new Timestamp(time));
/* 571:714 */           pstmt.setInt(8, type);
/* 572:715 */           pstmt.setString(9, formatid);
/* 573:716 */           pstmt.setInt(10, 1);
/* 574:717 */           pstmt.execute();
/* 575:    */         }
/* 576:    */       }
/* 577:    */       catch (Exception ex)
/* 578:    */       {
/* 579:721 */         System.out.println(ex.getMessage());
/* 580:722 */         System.out.println("Error importing ringtone into database");
/* 581:    */       }
/* 582:    */     }
/* 583:    */   }
/* 584:    */   
/* 585:    */   protected void write(ZipEntry e, byte[] zipedfile, Connection conn, String tableName, int type, String supplierId)
/* 586:    */     throws IOException, SQLException, Exception
/* 587:    */   {
/* 588:731 */     String zipName = e.getName();
/* 589:733 */     if (zipName.startsWith("/"))
/* 590:    */     {
/* 591:734 */       if (!this.warnedMkDir) {
/* 592:735 */         System.out.println("Ignoring absolute paths");
/* 593:    */       }
/* 594:737 */       this.warnedMkDir = true;
/* 595:738 */       zipName = zipName.substring(1);
/* 596:    */     }
/* 597:744 */     if (zipName.endsWith("/")) {
/* 598:745 */       return;
/* 599:    */     }
/* 600:748 */     System.out.println("Creating " + zipName);
/* 601:    */     
/* 602:750 */     String filename = e.getName();
/* 603:751 */     filename = zipName.substring(zipName.lastIndexOf("/") + 1, zipName.length());
/* 604:    */     
/* 605:    */ 
/* 606:    */ 
/* 607:755 */     long size = e.getSize();
/* 608:    */     
/* 609:757 */     String extension = null;
/* 610:758 */     int x = filename.lastIndexOf(".");
/* 611:759 */     if (x != 0) {
/* 612:760 */       extension = filename.substring(x + 1, filename.length());
/* 613:    */     }
/* 614:762 */     String typeId = "" + type;
/* 615:763 */     String formatid = ExtManager.getFormatFor(extension);
/* 616:764 */     if (!ExtManager.match(typeId, formatid)) {
/* 617:765 */       type = ExtManager.getTypeForFormat(extension);
/* 618:    */     }
/* 619:768 */     long time = e.getTime();
/* 620:771 */     if (type != 0) {
/* 621:    */       try
/* 622:    */       {
/* 623:775 */         new uidGen();String ID = uidGen.generateNumberID(10);
/* 624:776 */         String listId = tableName;
/* 625:    */         
/* 626:778 */         PreparedStatement pstmt = conn.prepareStatement("insert into uploads  (id, filename,binaryfile,list_id) \tvalues \t(?, ?, ? ,?)");
/* 627:    */         
/* 628:780 */         pstmt.setString(1, ID);
/* 629:781 */         int executeStatus = -3;
/* 630:782 */         pstmt.setString(2, filename);
/* 631:783 */         pstmt.setBytes(3, zipedfile);
/* 632:784 */         pstmt.setString(4, listId);
/* 633:785 */         executeStatus = pstmt.executeUpdate();
/* 634:787 */         if (executeStatus != -3)
/* 635:    */         {
/* 636:789 */           String qry = "insert into content_list (id, content_id, title,size,  price, list_id, date_added ,content_type,formats,isLocal,supplier_id ) values (?, ?, ?, ?, ?,?, ?,?,?,?,?)";
/* 637:    */           
/* 638:791 */           pstmt = conn.prepareStatement(qry);
/* 639:792 */           pstmt.setString(1, ID);
/* 640:793 */           new uidGen();pstmt.setString(2, uidGen.getUId());
/* 641:794 */           pstmt.setString(3, zipName);
/* 642:795 */           pstmt.setLong(4, size);
/* 643:796 */           pstmt.setString(5, "0");
/* 644:797 */           pstmt.setString(6, listId);
/* 645:798 */           pstmt.setTimestamp(7, new Timestamp(time));
/* 646:799 */           pstmt.setInt(8, type);
/* 647:800 */           pstmt.setString(9, formatid);
/* 648:801 */           pstmt.setInt(10, 1);
/* 649:802 */           pstmt.setString(11, supplierId);
/* 650:803 */           pstmt.execute();
/* 651:    */         }
/* 652:    */       }
/* 653:    */       catch (Exception ex)
/* 654:    */       {
/* 655:807 */         System.out.println(ex.getMessage());
/* 656:808 */         System.out.println("Error importing ringtone into database");
/* 657:    */       }
/* 658:    */     }
/* 659:    */   }
/* 660:    */   
/* 661:    */   private String getTempDirectory()
/* 662:    */     throws Exception
/* 663:    */   {
/* 664:824 */     return "S:\\developer\\work\\";
/* 665:    */   }
/* 666:    */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.util.ArchiveManager
 * JD-Core Version:    0.7.0.1
 */