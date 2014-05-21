/*   1:    */ package com.rancard.mobility.contentserver;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import com.rancard.mobility.contentprovider.User;
/*   5:    */ import java.io.PrintStream;
/*   6:    */ import java.sql.BatchUpdateException;
/*   7:    */ import java.sql.Connection;
/*   8:    */ import java.sql.PreparedStatement;
/*   9:    */ import java.sql.ResultSet;
/*  10:    */ import java.sql.SQLException;
/*  11:    */ import java.sql.Statement;
/*  12:    */ import java.util.ArrayList;
/*  13:    */ 
/*  14:    */ public abstract class CustomListDB
/*  15:    */ {
/*  16:    */   public static void createCustomList(String cpId, String customListId, String customListName)
/*  17:    */     throws Exception
/*  18:    */   {
/*  19: 30 */     ResultSet rs = null;
/*  20: 31 */     Connection con = null;
/*  21: 32 */     PreparedStatement prepstat = null;
/*  22:    */     try
/*  23:    */     {
/*  24: 35 */       con = DConnect.getConnection();
/*  25: 36 */       String query = "INSERT into custom_list_definition (cp_id, custom_list_id, custom_list_name) values(?,?,?)";
/*  26:    */       
/*  27: 38 */       prepstat = con.prepareStatement(query);
/*  28: 39 */       prepstat.setString(1, cpId);
/*  29: 40 */       prepstat.setString(2, customListId);
/*  30: 41 */       prepstat.setString(3, customListName);
/*  31:    */       
/*  32: 43 */       prepstat.execute();
/*  33:    */     }
/*  34:    */     catch (Exception ex)
/*  35:    */     {
/*  36: 45 */       if (con != null) {
/*  37: 46 */         con.close();
/*  38:    */       }
/*  39: 48 */       throw new Exception(ex.getMessage());
/*  40:    */     }
/*  41: 50 */     if (con != null) {
/*  42: 51 */       con.close();
/*  43:    */     }
/*  44:    */   }
/*  45:    */   
/*  46:    */   public static void updateCustomList(String cpId, String customListId, String customListName)
/*  47:    */     throws Exception
/*  48:    */   {
/*  49: 58 */     ResultSet rs = null;
/*  50: 59 */     Connection con = null;
/*  51: 60 */     PreparedStatement prepstat = null;
/*  52:    */     try
/*  53:    */     {
/*  54: 63 */       con = DConnect.getConnection();
/*  55: 64 */       String query = "UPDATE custom_list_definition SET custom_list_name=? WHERE cp_id=? and custom_list_id=?";
/*  56:    */       
/*  57: 66 */       prepstat = con.prepareStatement(query);
/*  58: 67 */       prepstat.setString(1, customListName);
/*  59: 68 */       prepstat.setString(2, cpId);
/*  60: 69 */       prepstat.setString(3, customListId);
/*  61: 70 */       prepstat.execute();
/*  62:    */     }
/*  63:    */     catch (Exception ex)
/*  64:    */     {
/*  65: 72 */       if (con != null) {
/*  66: 73 */         con.close();
/*  67:    */       }
/*  68: 75 */       throw new Exception(ex.getMessage());
/*  69:    */     }
/*  70: 77 */     if (con != null) {
/*  71: 78 */       con.close();
/*  72:    */     }
/*  73:    */   }
/*  74:    */   
/*  75:    */   public static void deleteCustomList(String customListId)
/*  76:    */     throws Exception
/*  77:    */   {
/*  78: 86 */     ResultSet rs = null;
/*  79: 87 */     Connection con = null;
/*  80: 88 */     PreparedStatement prepstat = null;
/*  81:    */     try
/*  82:    */     {
/*  83: 91 */       con = DConnect.getConnection();
/*  84: 92 */       String query = "DELETE from custom_list_definition WHERE custom_list_id=?";
/*  85: 93 */       prepstat = con.prepareStatement(query);
/*  86: 94 */       prepstat.setString(1, customListId);
/*  87: 95 */       prepstat.execute();
/*  88:    */       
/*  89: 97 */       query = "DELETE from custom_list WHERE custom_list_id=?";
/*  90: 98 */       prepstat = con.prepareStatement(query);
/*  91: 99 */       prepstat.setString(1, customListId);
/*  92:100 */       prepstat.execute();
/*  93:    */     }
/*  94:    */     catch (Exception ex)
/*  95:    */     {
/*  96:102 */       if (con != null) {
/*  97:103 */         con.close();
/*  98:    */       }
/*  99:105 */       throw new Exception(ex.getMessage());
/* 100:    */     }
/* 101:107 */     if (con != null) {
/* 102:108 */       con.close();
/* 103:    */     }
/* 104:    */   }
/* 105:    */   
/* 106:    */   public static void deleteCustomLists(String[] customListId)
/* 107:    */     throws Exception
/* 108:    */   {
/* 109:115 */     Connection con = null;
/* 110:116 */     Statement prepstat = null;
/* 111:117 */     int[] aiupdateCounts = null;
/* 112:118 */     boolean bError = false;
/* 113:    */     try
/* 114:    */     {
/* 115:122 */       con = DConnect.getConnection();
/* 116:123 */       con.setAutoCommit(false);
/* 117:124 */       con.createStatement();
/* 118:125 */       prepstat = con.createStatement();
/* 119:127 */       for (int i = 0; i < customListId.length; i++)
/* 120:    */       {
/* 121:128 */         String SQL1 = "delete from custom_list_definition where custom_list_id='" + customListId[i] + "'";
/* 122:129 */         String SQL2 = "delete from custom_list where custom_list_id='" + customListId[i] + "'";
/* 123:130 */         bError = false;
/* 124:    */         
/* 125:    */ 
/* 126:133 */         prepstat.addBatch(SQL1);
/* 127:134 */         prepstat.addBatch(SQL2);
/* 128:    */       }
/* 129:137 */       aiupdateCounts = prepstat.executeBatch();
/* 130:138 */       prepstat.clearBatch();
/* 131:    */     }
/* 132:    */     catch (BatchUpdateException bue)
/* 133:    */     {
/* 134:140 */       bError = true;
/* 135:141 */       aiupdateCounts = bue.getUpdateCounts();
/* 136:    */       
/* 137:143 */       SQLException SQLe = bue;
/* 138:144 */       while (SQLe != null) {
/* 139:145 */         SQLe = SQLe.getNextException();
/* 140:    */       }
/* 141:    */     }
/* 142:    */     catch (SQLException SQLe) {}finally
/* 143:    */     {
/* 144:151 */       for (int i = 0; i < aiupdateCounts.length; i++)
/* 145:    */       {
/* 146:152 */         int iProcessed = aiupdateCounts[i];
/* 147:153 */         if ((iProcessed > 0) || (iProcessed == -2))
/* 148:    */         {
/* 149:157 */           System.out.println("Delete sucessful");
/* 150:    */         }
/* 151:    */         else
/* 152:    */         {
/* 153:160 */           bError = true;
/* 154:161 */           break;
/* 155:    */         }
/* 156:    */       }
/* 157:165 */       if (bError) {
/* 158:166 */         con.rollback();
/* 159:    */       } else {
/* 160:168 */         con.commit();
/* 161:    */       }
/* 162:170 */       if (con != null) {
/* 163:171 */         con.close();
/* 164:    */       }
/* 165:    */     }
/* 166:    */   }
/* 167:    */   
/* 168:    */   public static void deleteCustomLists(String cpId)
/* 169:    */     throws Exception
/* 170:    */   {
/* 171:180 */     ResultSet rs = null;
/* 172:181 */     Connection con = null;
/* 173:182 */     PreparedStatement prepstat = null;
/* 174:183 */     Statement stat = null;
/* 175:184 */     ArrayList ids = new ArrayList();
/* 176:    */     try
/* 177:    */     {
/* 178:187 */       con = DConnect.getConnection();
/* 179:    */       
/* 180:    */ 
/* 181:190 */       String query = "select custom_list_id from custom_list_definition where cp_id=?";
/* 182:191 */       prepstat = con.prepareStatement(query);
/* 183:192 */       prepstat.setString(1, cpId);
/* 184:193 */       rs = prepstat.executeQuery();
/* 185:195 */       while (rs.next()) {
/* 186:196 */         ids.add(rs.getString("custom_list_id"));
/* 187:    */       }
/* 188:200 */       query = "DELETE from custom_list_definition WHERE cp_id=?";
/* 189:201 */       prepstat = con.prepareStatement(query);
/* 190:202 */       prepstat.setString(1, cpId);
/* 191:203 */       prepstat.execute();
/* 192:    */       
/* 193:205 */       con.setAutoCommit(false);
/* 194:206 */       con.createStatement();
/* 195:207 */       stat = con.createStatement();
/* 196:210 */       for (int i = 0; i < ids.size(); i++)
/* 197:    */       {
/* 198:211 */         query = "delete from custom_list where custom_list_id='" + ids.get(i) + "'";
/* 199:212 */         stat.addBatch(query);
/* 200:    */       }
/* 201:214 */       stat.executeBatch();
/* 202:215 */       stat.clearBatch();
/* 203:    */     }
/* 204:    */     catch (Exception ex)
/* 205:    */     {
/* 206:217 */       if (con != null) {
/* 207:218 */         con.close();
/* 208:    */       }
/* 209:220 */       throw new Exception(ex.getMessage());
/* 210:    */     }
/* 211:222 */     if (con != null) {
/* 212:223 */       con.close();
/* 213:    */     }
/* 214:    */   }
/* 215:    */   
/* 216:    */   public static CustomList viewCustomList(String cpId, String customListId)
/* 217:    */     throws Exception
/* 218:    */   {
/* 219:229 */     CustomList thisList = new CustomList();
/* 220:    */     
/* 221:    */ 
/* 222:232 */     ResultSet rs = null;
/* 223:233 */     ResultSet rs2 = null;
/* 224:234 */     Connection con = null;
/* 225:235 */     PreparedStatement prepstat = null;
/* 226:    */     try
/* 227:    */     {
/* 228:238 */       con = DConnect.getConnection();
/* 229:239 */       String query = "select * from custom_list_definition where cp_id=? and custom_list_id=?";
/* 230:240 */       prepstat = con.prepareStatement(query);
/* 231:241 */       prepstat.setString(1, cpId);
/* 232:242 */       prepstat.setString(2, customListId);
/* 233:243 */       rs = prepstat.executeQuery();
/* 234:245 */       if (rs.next())
/* 235:    */       {
/* 236:246 */         thisList.setCpId(rs.getString("cp_id"));
/* 237:247 */         thisList.setCustomListId(rs.getString("custom_list_id"));
/* 238:248 */         thisList.setCustomListName(rs.getString("custom_list_name"));
/* 239:    */         
/* 240:250 */         query = "select item_id, prov_id from custom_list where custom_list_id=?";
/* 241:251 */         prepstat = con.prepareStatement(query);
/* 242:252 */         prepstat.setString(1, customListId);
/* 243:253 */         rs = prepstat.executeQuery();
/* 244:    */         
/* 245:255 */         ArrayList items = new ArrayList();
/* 246:256 */         while (rs.next())
/* 247:    */         {
/* 248:257 */           ContentItem item = new ContentItem();
/* 249:258 */           Format format = new Format();
/* 250:259 */           ContentType type = new ContentType();
/* 251:260 */           User cp = new User();
/* 252:    */           
/* 253:262 */           query = "SELECT * FROM content_list, service_route, cp_user, format_list where content_list.id=? and content_list.list_id=? and content_list.content_type=service_route.service_type and content_list.list_id=cp_user.id and content_list.formats=format_list.format_id";
/* 254:    */           
/* 255:    */ 
/* 256:265 */           prepstat = con.prepareStatement(query);
/* 257:266 */           String id = rs.getString("item_id");
/* 258:267 */           String listid = rs.getString("prov_id");
/* 259:    */           
/* 260:269 */           prepstat.setString(1, id);
/* 261:270 */           prepstat.setString(2, listid);
/* 262:    */           
/* 263:272 */           rs2 = prepstat.executeQuery();
/* 264:273 */           while (rs2.next())
/* 265:    */           {
/* 266:275 */             item.setContentId(rs2.getString("content_id"));
/* 267:276 */             item.setid(rs2.getString("id"));
/* 268:277 */             item.settitle(rs2.getString("title"));
/* 269:278 */             item.settype(new Integer(rs2.getInt("content_type")));
/* 270:279 */             item.setdownload_url(rs2.getString("download_url"));
/* 271:280 */             item.setPreviewUrl(rs2.getString("preview_url"));
/* 272:281 */             item.setPrice(rs2.getString("price"));
/* 273:282 */             item.setAuthor(rs2.getString("author"));
/* 274:283 */             item.setCategory(new Integer(rs2.getInt("category")));
/* 275:284 */             item.setSize(new Long(rs2.getLong("size")));
/* 276:285 */             item.setListId(rs2.getString("list_id"));
/* 277:286 */             item.setDate_Added(rs2.getTimestamp("date_added"));
/* 278:287 */             item.setOther_Details(rs2.getString("other_details"));
/* 279:288 */             if (rs2.getInt("isLocal") == 1) {
/* 280:289 */               item.setIsLocal(true);
/* 281:    */             } else {
/* 282:291 */               item.setIsLocal(false);
/* 283:    */             }
/* 284:293 */             if (rs2.getInt("show") == 1) {
/* 285:294 */               item.setCanList(true);
/* 286:    */             } else {
/* 287:296 */               item.setCanList(false);
/* 288:    */             }
/* 289:298 */             if (rs2.getInt("is_free") == 1) {
/* 290:299 */               item.setFree(true);
/* 291:    */             } else {
/* 292:301 */               item.setFree(false);
/* 293:    */             }
/* 294:305 */             type.setParentServiceType(rs2.getInt("service_route.parent_service_type"));
/* 295:306 */             type.setServiceName(rs2.getString("service_route.service_name"));
/* 296:307 */             type.setServiceType(rs2.getInt("service_route.service_type"));
/* 297:    */             
/* 298:    */ 
/* 299:310 */             cp.setId(rs2.getString("cp_user.id"));
/* 300:311 */             cp.setName(rs2.getString("cp_user.name"));
/* 301:312 */             cp.setUsername(rs2.getString("cp_user.username"));
/* 302:313 */             cp.setPassword(rs2.getString("cp_user.password"));
/* 303:314 */             cp.setDefaultSmsc(rs2.getString("cp_user.default_smsc"));
/* 304:315 */             cp.setLogoUrl(rs2.getString("cp_user.logo_url"));
/* 305:316 */             cp.setDefaultService(rs2.getString("cp_user.default_service"));
/* 306:    */             
/* 307:    */ 
/* 308:319 */             format.setId(rs2.getInt("format_list.format_id"));
/* 309:320 */             format.setFileExt(rs2.getString("format_list.file_ext"));
/* 310:321 */             format.setMimeType(rs2.getString("format_list.mime_type"));
/* 311:322 */             format.setPushBearer(rs2.getString("format_list.push_bearer"));
/* 312:    */           }
/* 313:324 */           item.setContentTypeDetails(type);
/* 314:325 */           item.setProviderDetails(cp);
/* 315:326 */           item.setFormat(format);
/* 316:    */           
/* 317:328 */           items.add(item);
/* 318:    */         }
/* 319:330 */         thisList.setItems(items);
/* 320:    */       }
/* 321:    */     }
/* 322:    */     catch (Exception ex)
/* 323:    */     {
/* 324:333 */       if (con != null) {
/* 325:334 */         con.close();
/* 326:    */       }
/* 327:336 */       throw new Exception(ex.getMessage());
/* 328:    */     }
/* 329:338 */     if (con != null) {
/* 330:339 */       con.close();
/* 331:    */     }
/* 332:342 */     return thisList;
/* 333:    */   }
/* 334:    */   
/* 335:    */   public static ArrayList viewCustomLists(String cpId, String[] customListIds)
/* 336:    */     throws Exception
/* 337:    */   {
/* 338:347 */     ArrayList allLists = new ArrayList();
/* 339:    */     
/* 340:    */ 
/* 341:350 */     ResultSet rs = null;
/* 342:351 */     ResultSet rs2 = null;
/* 343:352 */     Connection con = null;
/* 344:353 */     PreparedStatement prepstat = null;
/* 345:    */     try
/* 346:    */     {
/* 347:356 */       con = DConnect.getConnection();
/* 348:357 */       for (int i = 0; i < customListIds.length; i++)
/* 349:    */       {
/* 350:358 */         String query = "select * from custom_list_definition where cp_id='" + cpId + "' and custom_list_id='" + customListIds[i] + "'";
/* 351:359 */         prepstat = con.prepareStatement(query);
/* 352:360 */         rs = prepstat.executeQuery();
/* 353:362 */         while (rs.next())
/* 354:    */         {
/* 355:363 */           CustomList thisList = new CustomList();
/* 356:364 */           thisList.setCpId(rs.getString("cp_id"));
/* 357:365 */           thisList.setCustomListId(rs.getString("custom_list_id"));
/* 358:366 */           thisList.setCustomListName(rs.getString("custom_list_name"));
/* 359:    */           
/* 360:368 */           query = "select item_id, prov_id from custom_list where custom_list_id='" + customListIds[i] + "'";
/* 361:369 */           prepstat = con.prepareStatement(query);
/* 362:370 */           rs = prepstat.executeQuery();
/* 363:    */           
/* 364:372 */           ArrayList items = new ArrayList();
/* 365:373 */           while (rs.next())
/* 366:    */           {
/* 367:374 */             ContentItem item = new ContentItem();
/* 368:375 */             Format format = new Format();
/* 369:376 */             ContentType type = new ContentType();
/* 370:377 */             User cp = new User();
/* 371:    */             
/* 372:379 */             String id = rs.getString("item_id");
/* 373:380 */             String listid = rs.getString("prov_id");
/* 374:    */             
/* 375:382 */             query = "SELECT * FROM content_list, service_route, cp_user, format_list where content_list.id='" + id + "' and content_list.list_id='" + listid + "' and " + "content_list.content_type=service_route.service_type and content_list.list_id=cp_user.id and content_list.formats=format_list.format_id";
/* 376:    */             
/* 377:    */ 
/* 378:385 */             prepstat = con.prepareStatement(query);
/* 379:    */             
/* 380:387 */             rs2 = prepstat.executeQuery();
/* 381:388 */             while (rs2.next())
/* 382:    */             {
/* 383:390 */               item.setContentId(rs2.getString("content_id"));
/* 384:391 */               item.setid(rs2.getString("id"));
/* 385:392 */               item.settitle(rs2.getString("title"));
/* 386:393 */               item.settype(new Integer(rs2.getInt("content_type")));
/* 387:394 */               item.setdownload_url(rs2.getString("download_url"));
/* 388:395 */               item.setPreviewUrl(rs2.getString("preview_url"));
/* 389:396 */               item.setPrice(rs2.getString("price"));
/* 390:397 */               item.setAuthor(rs2.getString("author"));
/* 391:398 */               item.setCategory(new Integer(rs2.getInt("category")));
/* 392:399 */               item.setSize(new Long(rs2.getLong("size")));
/* 393:400 */               item.setListId(rs2.getString("list_id"));
/* 394:401 */               item.setDate_Added(rs2.getTimestamp("date_added"));
/* 395:402 */               item.setOther_Details(rs2.getString("other_details"));
/* 396:403 */               if (rs2.getInt("isLocal") == 1) {
/* 397:404 */                 item.setIsLocal(true);
/* 398:    */               } else {
/* 399:406 */                 item.setIsLocal(false);
/* 400:    */               }
/* 401:408 */               if (rs2.getInt("show") == 1) {
/* 402:409 */                 item.setCanList(true);
/* 403:    */               } else {
/* 404:411 */                 item.setCanList(false);
/* 405:    */               }
/* 406:413 */               if (rs2.getInt("is_free") == 1) {
/* 407:414 */                 item.setFree(true);
/* 408:    */               } else {
/* 409:416 */                 item.setFree(false);
/* 410:    */               }
/* 411:418 */               item.setShortItemRef(rs2.getString("short_item_ref"));
/* 412:419 */               item.setSupplierId(rs2.getString("supplier_id"));
/* 413:    */               
/* 414:    */ 
/* 415:422 */               type.setParentServiceType(rs2.getInt("service_route.parent_service_type"));
/* 416:423 */               type.setServiceName(rs2.getString("service_route.service_name"));
/* 417:424 */               type.setServiceType(rs2.getInt("service_route.service_type"));
/* 418:    */               
/* 419:    */ 
/* 420:427 */               cp.setId(rs2.getString("cp_user.id"));
/* 421:428 */               cp.setName(rs2.getString("cp_user.name"));
/* 422:429 */               cp.setUsername(rs2.getString("cp_user.username"));
/* 423:430 */               cp.setPassword(rs2.getString("cp_user.password"));
/* 424:431 */               cp.setDefaultSmsc(rs2.getString("cp_user.default_smsc"));
/* 425:432 */               cp.setLogoUrl(rs2.getString("cp_user.logo_url"));
/* 426:433 */               cp.setDefaultService(rs2.getString("cp_user.default_service"));
/* 427:    */               
/* 428:    */ 
/* 429:436 */               format.setId(rs2.getInt("format_list.format_id"));
/* 430:437 */               format.setFileExt(rs2.getString("format_list.file_ext"));
/* 431:438 */               format.setMimeType(rs2.getString("format_list.mime_type"));
/* 432:439 */               format.setPushBearer(rs2.getString("format_list.push_bearer"));
/* 433:    */             }
/* 434:441 */             item.setContentTypeDetails(type);
/* 435:442 */             item.setProviderDetails(cp);
/* 436:443 */             item.setFormat(format);
/* 437:    */             
/* 438:445 */             items.add(item);
/* 439:    */           }
/* 440:447 */           thisList.setItems(items);
/* 441:448 */           allLists.add(thisList);
/* 442:    */         }
/* 443:    */       }
/* 444:    */     }
/* 445:    */     catch (Exception ex)
/* 446:    */     {
/* 447:452 */       if (con != null) {
/* 448:453 */         con.close();
/* 449:    */       }
/* 450:455 */       throw new Exception(ex.getMessage());
/* 451:    */     }
/* 452:457 */     if (con != null) {
/* 453:458 */       con.close();
/* 454:    */     }
/* 455:461 */     return allLists;
/* 456:    */   }
/* 457:    */   
/* 458:    */   public static ArrayList viewCustomLists(String cpId)
/* 459:    */     throws Exception
/* 460:    */   {
/* 461:466 */     ArrayList lists = new ArrayList();
/* 462:    */     
/* 463:    */ 
/* 464:469 */     ResultSet rs = null;
/* 465:470 */     ResultSet rs2 = null;
/* 466:471 */     ResultSet rs3 = null;
/* 467:472 */     Connection con = null;
/* 468:473 */     PreparedStatement prepstat = null;
/* 469:    */     try
/* 470:    */     {
/* 471:476 */       con = DConnect.getConnection();
/* 472:477 */       String query = "select * from custom_list_definition where cp_id=?";
/* 473:478 */       prepstat = con.prepareStatement(query);
/* 474:479 */       prepstat.setString(1, cpId);
/* 475:480 */       rs = prepstat.executeQuery();
/* 476:482 */       while (rs.next())
/* 477:    */       {
/* 478:483 */         ArrayList items = new ArrayList();
/* 479:484 */         CustomList thisList = new CustomList();
/* 480:485 */         thisList.setCpId(rs.getString("cp_id"));
/* 481:486 */         thisList.setCustomListId(rs.getString("custom_list_id"));
/* 482:487 */         thisList.setCustomListName(rs.getString("custom_list_name"));
/* 483:    */         
/* 484:489 */         query = "select * from content_list ctl inner join custom_list cl on ctl.id=cl.item_id and ctl.list_id=cl.prov_id inner join service_route sr on ctl.content_type=sr.service_type inner join cp_user cp on ctl.list_id=cp.id inner join format_list fl on ctl.formats=fl.format_id where cl.custom_list_id='" + thisList.getCustomListId() + "'";
/* 485:    */         
/* 486:491 */         prepstat = con.prepareStatement(query);
/* 487:492 */         rs3 = prepstat.executeQuery();
/* 488:494 */         while (rs3.next())
/* 489:    */         {
/* 490:495 */           ContentItem item = new ContentItem();
/* 491:496 */           Format format = new Format();
/* 492:497 */           ContentType type = new ContentType();
/* 493:498 */           User cp = new User();
/* 494:    */           
/* 495:    */ 
/* 496:501 */           item.setContentId(rs3.getString("ctl.content_id"));
/* 497:502 */           item.setid(rs3.getString("ctl.id"));
/* 498:503 */           item.settitle(rs3.getString("ctl.title"));
/* 499:504 */           item.settype(new Integer(rs3.getInt("ctl.content_type")));
/* 500:505 */           item.setdownload_url(rs3.getString("ctl.download_url"));
/* 501:506 */           item.setPreviewUrl(rs3.getString("ctl.preview_url"));
/* 502:507 */           item.setPrice(rs3.getString("ctl.price"));
/* 503:508 */           item.setAuthor(rs3.getString("ctl.author"));
/* 504:509 */           item.setCategory(new Integer(rs3.getInt("ctl.category")));
/* 505:510 */           item.setSize(new Long(rs3.getLong("ctl.size")));
/* 506:511 */           item.setListId(rs3.getString("ctl.list_id"));
/* 507:512 */           item.setDate_Added(rs3.getTimestamp("ctl.date_added"));
/* 508:513 */           item.setOther_Details(rs3.getString("ctl.other_details"));
/* 509:514 */           if (rs3.getInt("ctl.isLocal") == 1) {
/* 510:515 */             item.setIsLocal(true);
/* 511:    */           } else {
/* 512:517 */             item.setIsLocal(false);
/* 513:    */           }
/* 514:519 */           if (rs3.getInt("ctl.show") == 1) {
/* 515:520 */             item.setCanList(true);
/* 516:    */           } else {
/* 517:522 */             item.setCanList(false);
/* 518:    */           }
/* 519:524 */           if (rs3.getInt("ctl.is_free") == 1) {
/* 520:525 */             item.setFree(true);
/* 521:    */           } else {
/* 522:527 */             item.setFree(false);
/* 523:    */           }
/* 524:529 */           item.setShortItemRef(rs3.getString("short_item_ref"));
/* 525:530 */           item.setSupplierId(rs3.getString("supplier_id"));
/* 526:    */           
/* 527:    */ 
/* 528:533 */           type.setParentServiceType(rs3.getInt("sr.parent_service_type"));
/* 529:534 */           type.setServiceName(rs3.getString("sr.service_name"));
/* 530:535 */           type.setServiceType(rs3.getInt("sr.service_type"));
/* 531:    */           
/* 532:    */ 
/* 533:538 */           cp.setId(rs3.getString("cp.id"));
/* 534:539 */           cp.setName(rs3.getString("cp.name"));
/* 535:540 */           cp.setUsername(rs3.getString("cp.username"));
/* 536:541 */           cp.setPassword(rs3.getString("cp.password"));
/* 537:542 */           cp.setDefaultSmsc(rs3.getString("cp.default_smsc"));
/* 538:543 */           cp.setLogoUrl(rs3.getString("cp.logo_url"));
/* 539:544 */           cp.setDefaultService(rs3.getString("cp.default_service"));
/* 540:    */           
/* 541:    */ 
/* 542:547 */           format.setId(rs3.getInt("fl.format_id"));
/* 543:548 */           format.setFileExt(rs3.getString("fl.file_ext"));
/* 544:549 */           format.setMimeType(rs3.getString("fl.mime_type"));
/* 545:550 */           format.setPushBearer(rs3.getString("fl.push_bearer"));
/* 546:    */           
/* 547:552 */           item.setContentTypeDetails(type);
/* 548:553 */           item.setProviderDetails(cp);
/* 549:554 */           item.setFormat(format);
/* 550:    */           
/* 551:556 */           items.add(item);
/* 552:    */         }
/* 553:558 */         thisList.setItems(items);
/* 554:559 */         lists.add(thisList);
/* 555:    */       }
/* 556:    */     }
/* 557:    */     catch (Exception ex)
/* 558:    */     {
/* 559:562 */       if (con != null) {
/* 560:563 */         con.close();
/* 561:    */       }
/* 562:565 */       throw new Exception(ex.getMessage());
/* 563:    */     }
/* 564:567 */     if (con != null) {
/* 565:568 */       con.close();
/* 566:    */     }
/* 567:570 */     return lists;
/* 568:    */   }
/* 569:    */   
/* 570:    */   public static ArrayList viewCustomListDefinitions(String cpId)
/* 571:    */     throws Exception
/* 572:    */   {
/* 573:575 */     ArrayList lists = new ArrayList();
/* 574:    */     
/* 575:    */ 
/* 576:578 */     ResultSet rs = null;
/* 577:579 */     ResultSet rs2 = null;
/* 578:580 */     Connection con = null;
/* 579:581 */     PreparedStatement prepstat = null;
/* 580:    */     try
/* 581:    */     {
/* 582:584 */       con = DConnect.getConnection();
/* 583:585 */       String query = "select * from custom_list_definition where cp_id=?";
/* 584:586 */       prepstat = con.prepareStatement(query);
/* 585:587 */       prepstat.setString(1, cpId);
/* 586:588 */       rs = prepstat.executeQuery();
/* 587:590 */       while (rs.next())
/* 588:    */       {
/* 589:591 */         CustomList thisList = new CustomList();
/* 590:592 */         thisList.setCpId(rs.getString("cp_id"));
/* 591:593 */         thisList.setCustomListId(rs.getString("custom_list_id"));
/* 592:594 */         thisList.setCustomListName(rs.getString("custom_list_name"));
/* 593:    */         
/* 594:596 */         lists.add(thisList);
/* 595:    */       }
/* 596:    */     }
/* 597:    */     catch (Exception ex)
/* 598:    */     {
/* 599:599 */       if (con != null) {
/* 600:600 */         con.close();
/* 601:    */       }
/* 602:602 */       throw new Exception(ex.getMessage());
/* 603:    */     }
/* 604:604 */     if (con != null) {
/* 605:605 */       con.close();
/* 606:    */     }
/* 607:607 */     return lists;
/* 608:    */   }
/* 609:    */   
/* 610:    */   public static void addItemsToList(String customListId, String[] ids, String[] listIds)
/* 611:    */     throws Exception
/* 612:    */   {
/* 613:612 */     Connection con = null;
/* 614:613 */     PreparedStatement prepstat = null;
/* 615:614 */     Statement stat = null;
/* 616:615 */     int[] aiupdateCounts = null;
/* 617:616 */     boolean bError = false;
/* 618:    */     
/* 619:618 */     ResultSet rs = null;
/* 620:620 */     if (ids.length == listIds.length) {
/* 621:    */       try
/* 622:    */       {
/* 623:622 */         con = DConnect.getConnection();
/* 624:    */         
/* 625:624 */         String query = "select * from custom_list_definition where custom_list_id=?";
/* 626:625 */         prepstat = con.prepareStatement(query);
/* 627:626 */         prepstat.setString(1, customListId);
/* 628:627 */         rs = prepstat.executeQuery();
/* 629:628 */         if (rs.next())
/* 630:    */         {
/* 631:629 */           con.setAutoCommit(false);
/* 632:630 */           stat = con.createStatement();
/* 633:632 */           for (int i = 0; i < ids.length; i++)
/* 634:    */           {
/* 635:633 */             query = "INSERT into custom_list (custom_list_id, item_id, prov_id) values('" + customListId + "','" + ids[i] + "','" + listIds[i] + "')";
/* 636:634 */             bError = false;
/* 637:635 */             stat.addBatch(query);
/* 638:    */           }
/* 639:637 */           aiupdateCounts = stat.executeBatch();
/* 640:638 */           stat.clearBatch();
/* 641:    */         }
/* 642:    */       }
/* 643:    */       catch (BatchUpdateException bue)
/* 644:    */       {
/* 645:641 */         bError = true;
/* 646:642 */         aiupdateCounts = bue.getUpdateCounts();
/* 647:    */         
/* 648:644 */         SQLException SQLe = bue;
/* 649:645 */         while (SQLe != null) {
/* 650:646 */           SQLe = SQLe.getNextException();
/* 651:    */         }
/* 652:    */       }
/* 653:    */       catch (SQLException SQLe) {}finally
/* 654:    */       {
/* 655:652 */         for (int i = 0; i < aiupdateCounts.length; i++)
/* 656:    */         {
/* 657:653 */           int iProcessed = aiupdateCounts[i];
/* 658:654 */           if ((iProcessed > 0) || (iProcessed == -2))
/* 659:    */           {
/* 660:658 */             System.out.println("Insert sucessful");
/* 661:    */           }
/* 662:    */           else
/* 663:    */           {
/* 664:661 */             bError = true;
/* 665:662 */             break;
/* 666:    */           }
/* 667:    */         }
/* 668:666 */         if (bError) {
/* 669:667 */           con.rollback();
/* 670:    */         } else {
/* 671:669 */           con.commit();
/* 672:    */         }
/* 673:671 */         if (con != null) {
/* 674:672 */           con.close();
/* 675:    */         }
/* 676:    */       }
/* 677:    */     } else {
/* 678:676 */       throw new Exception("unpaired references found");
/* 679:    */     }
/* 680:    */   }
/* 681:    */   
/* 682:    */   public static void removeItemsFromList(String customListId, String[] ids, String[] listIds)
/* 683:    */     throws Exception
/* 684:    */   {
/* 685:682 */     Connection con = null;
/* 686:683 */     PreparedStatement prepstat = null;
/* 687:684 */     Statement stat = null;
/* 688:685 */     int[] aiupdateCounts = null;
/* 689:686 */     boolean bError = false;
/* 690:    */     
/* 691:688 */     ResultSet rs = null;
/* 692:690 */     if (ids.length == listIds.length) {
/* 693:    */       try
/* 694:    */       {
/* 695:692 */         con = DConnect.getConnection();
/* 696:    */         
/* 697:694 */         con.setAutoCommit(false);
/* 698:695 */         stat = con.createStatement();
/* 699:697 */         for (int i = 0; i < ids.length; i++)
/* 700:    */         {
/* 701:698 */           String query = "delete from custom_list where custom_list_id='" + customListId + "' and item_id='" + ids[i] + "' and prov_id='" + listIds[i] + "'";
/* 702:    */           
/* 703:700 */           bError = false;
/* 704:701 */           stat.addBatch(query);
/* 705:    */         }
/* 706:703 */         aiupdateCounts = stat.executeBatch();
/* 707:704 */         stat.clearBatch();
/* 708:    */       }
/* 709:    */       catch (BatchUpdateException bue)
/* 710:    */       {
/* 711:706 */         bError = true;
/* 712:707 */         aiupdateCounts = bue.getUpdateCounts();
/* 713:    */         
/* 714:709 */         SQLException SQLe = bue;
/* 715:710 */         while (SQLe != null) {
/* 716:711 */           SQLe = SQLe.getNextException();
/* 717:    */         }
/* 718:    */       }
/* 719:    */       catch (SQLException SQLe) {}finally
/* 720:    */       {
/* 721:717 */         for (int i = 0; i < aiupdateCounts.length; i++)
/* 722:    */         {
/* 723:718 */           int iProcessed = aiupdateCounts[i];
/* 724:719 */           if ((iProcessed > 0) || (iProcessed == -2))
/* 725:    */           {
/* 726:723 */             System.out.println("Delete sucessful");
/* 727:    */           }
/* 728:    */           else
/* 729:    */           {
/* 730:726 */             bError = true;
/* 731:727 */             break;
/* 732:    */           }
/* 733:    */         }
/* 734:731 */         if (bError) {
/* 735:732 */           con.rollback();
/* 736:    */         } else {
/* 737:734 */           con.commit();
/* 738:    */         }
/* 739:736 */         if (con != null) {
/* 740:737 */           con.close();
/* 741:    */         }
/* 742:    */       }
/* 743:    */     } else {
/* 744:741 */       throw new Exception("unpaired references found");
/* 745:    */     }
/* 746:    */   }
/* 747:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.CustomListDB
 * JD-Core Version:    0.7.0.1
 */