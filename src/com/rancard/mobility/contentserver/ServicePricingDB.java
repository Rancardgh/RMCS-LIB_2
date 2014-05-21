/*   1:    */ package com.rancard.mobility.contentserver;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import java.sql.Connection;
/*   5:    */ import java.sql.PreparedStatement;
/*   6:    */ import java.sql.ResultSet;
/*   7:    */ import java.util.ArrayList;
/*   8:    */ import java.util.HashMap;
/*   9:    */ 
/*  10:    */ public abstract class ServicePricingDB
/*  11:    */ {
/*  12:    */   public static void create(String cpId, String keyword, String networkPrefix, String price, String serviceType, String shortCode)
/*  13:    */     throws Exception
/*  14:    */   {
/*  15: 20 */     ResultSet rs = null;
/*  16: 21 */     Connection con = null;
/*  17: 22 */     PreparedStatement prepstat = null;
/*  18:    */     try
/*  19:    */     {
/*  20: 25 */       con = DConnect.getConnection();
/*  21:    */       
/*  22: 27 */       String query = "select * from service_definition where keyword=? and account_id=?";
/*  23: 28 */       prepstat = con.prepareStatement(query);
/*  24: 29 */       prepstat.setString(1, keyword);
/*  25: 30 */       prepstat.setString(2, cpId);
/*  26: 31 */       rs = prepstat.executeQuery();
/*  27: 32 */       if (rs.next())
/*  28:    */       {
/*  29: 33 */         query = "INSERT into service_pricing(account_id,,keyword,network_prefix,price,service_type,short_code) values(?,?,?,?,?,?)";
/*  30:    */         
/*  31: 35 */         prepstat = con.prepareStatement(query);
/*  32:    */         
/*  33: 37 */         prepstat.setString(1, cpId);
/*  34: 38 */         prepstat.setString(2, keyword);
/*  35: 39 */         prepstat.setString(3, networkPrefix);
/*  36: 40 */         prepstat.setString(4, price);
/*  37: 41 */         prepstat.setString(5, serviceType);
/*  38: 42 */         prepstat.setString(6, shortCode);
/*  39:    */         
/*  40: 44 */         prepstat.execute();
/*  41:    */       }
/*  42:    */     }
/*  43:    */     catch (Exception ex)
/*  44:    */     {
/*  45: 48 */       if (con != null) {
/*  46: 49 */         con.close();
/*  47:    */       }
/*  48: 51 */       throw new Exception(ex.getMessage());
/*  49:    */     }
/*  50: 53 */     if (con != null) {
/*  51: 54 */       con.close();
/*  52:    */     }
/*  53:    */   }
/*  54:    */   
/*  55:    */   public static void updatePrice(String price, String cpId, String serviceType, String networkCode)
/*  56:    */     throws Exception
/*  57:    */   {
/*  58: 60 */     ResultSet rs = null;
/*  59: 61 */     Connection con = null;
/*  60: 62 */     PreparedStatement prepstat = null;
/*  61: 63 */     String keyword = "";
/*  62:    */     try
/*  63:    */     {
/*  64: 66 */       con = DConnect.getConnection();
/*  65: 67 */       String query = "select keyword from service_pricing where account_id=? and service_type=? and network_prefix=?";
/*  66: 68 */       prepstat = con.prepareStatement(query);
/*  67: 69 */       prepstat.setString(1, cpId);
/*  68: 70 */       prepstat.setString(2, serviceType);
/*  69: 71 */       prepstat.setString(3, networkCode);
/*  70: 72 */       rs = prepstat.executeQuery();
/*  71: 73 */       while (rs.next()) {
/*  72: 74 */         keyword = rs.getString("keyword");
/*  73:    */       }
/*  74: 77 */       query = "select * from service_definition where keyword=? and account_id=?";
/*  75: 78 */       prepstat = con.prepareStatement(query);
/*  76: 79 */       prepstat.setString(1, keyword);
/*  77: 80 */       prepstat.setString(2, cpId);
/*  78: 81 */       rs = prepstat.executeQuery();
/*  79: 82 */       if (rs.next())
/*  80:    */       {
/*  81: 83 */         query = "update service_pricing set price=? where account_id=? and service_type=? and network_prefix=?";
/*  82:    */         
/*  83: 85 */         prepstat = con.prepareStatement(query);
/*  84:    */         
/*  85: 87 */         prepstat.setString(1, price);
/*  86: 88 */         prepstat.setString(2, cpId);
/*  87: 89 */         prepstat.setString(3, serviceType);
/*  88: 90 */         prepstat.setString(4, networkCode);
/*  89: 91 */         prepstat.execute();
/*  90:    */       }
/*  91:    */     }
/*  92:    */     catch (Exception ex)
/*  93:    */     {
/*  94: 94 */       if (con != null) {
/*  95: 95 */         con.close();
/*  96:    */       }
/*  97: 97 */       throw new Exception(ex.getMessage());
/*  98:    */     }
/*  99: 99 */     if (con != null) {
/* 100:100 */       con.close();
/* 101:    */     }
/* 102:    */   }
/* 103:    */   
/* 104:    */   public static void delete(String cpId, String serviceType, String networkCode)
/* 105:    */     throws Exception
/* 106:    */   {
/* 107:106 */     ResultSet rs = null;
/* 108:107 */     Connection con = null;
/* 109:108 */     PreparedStatement prepstat = null;
/* 110:    */     try
/* 111:    */     {
/* 112:111 */       con = DConnect.getConnection();
/* 113:112 */       String query = "DELETE from service_pricing WHERE account_id=? and service_type=? and network_prefix=?";
/* 114:113 */       prepstat = con.prepareStatement(query);
/* 115:    */       
/* 116:115 */       prepstat.setString(1, cpId);
/* 117:116 */       prepstat.setString(2, serviceType);
/* 118:117 */       prepstat.setString(3, networkCode);
/* 119:    */       
/* 120:119 */       prepstat.execute();
/* 121:    */     }
/* 122:    */     catch (Exception ex)
/* 123:    */     {
/* 124:121 */       if (con != null) {
/* 125:122 */         con.close();
/* 126:    */       }
/* 127:124 */       throw new Exception(ex.getMessage());
/* 128:    */     }
/* 129:126 */     if (con != null) {
/* 130:127 */       con.close();
/* 131:    */     }
/* 132:    */   }
/* 133:    */   
/* 134:    */   public static void deleteAllForCP(String cpId)
/* 135:    */     throws Exception
/* 136:    */   {
/* 137:133 */     ResultSet rs = null;
/* 138:134 */     Connection con = null;
/* 139:135 */     PreparedStatement prepstat = null;
/* 140:    */     try
/* 141:    */     {
/* 142:138 */       con = DConnect.getConnection();
/* 143:139 */       String query = "DELETE from service_pricing WHERE account_id=?";
/* 144:140 */       prepstat = con.prepareStatement(query);
/* 145:    */       
/* 146:142 */       prepstat.setString(1, cpId);
/* 147:    */       
/* 148:144 */       prepstat.execute();
/* 149:    */     }
/* 150:    */     catch (Exception ex)
/* 151:    */     {
/* 152:146 */       if (con != null) {
/* 153:147 */         con.close();
/* 154:    */       }
/* 155:149 */       throw new Exception(ex.getMessage());
/* 156:    */     }
/* 157:151 */     if (con != null) {
/* 158:152 */       con.close();
/* 159:    */     }
/* 160:    */   }
/* 161:    */   
/* 162:    */   public static void deleteAllForCPonNetwork(String cpId, String networkCode)
/* 163:    */     throws Exception
/* 164:    */   {
/* 165:158 */     ResultSet rs = null;
/* 166:159 */     Connection con = null;
/* 167:160 */     PreparedStatement prepstat = null;
/* 168:    */     try
/* 169:    */     {
/* 170:163 */       con = DConnect.getConnection();
/* 171:164 */       String query = "DELETE from service_pricing WHERE account_id=? and network_prefix=?";
/* 172:165 */       prepstat = con.prepareStatement(query);
/* 173:    */       
/* 174:167 */       prepstat.setString(1, cpId);
/* 175:168 */       prepstat.setString(2, networkCode);
/* 176:    */       
/* 177:170 */       prepstat.execute();
/* 178:    */     }
/* 179:    */     catch (Exception ex)
/* 180:    */     {
/* 181:172 */       if (con != null) {
/* 182:173 */         con.close();
/* 183:    */       }
/* 184:175 */       throw new Exception(ex.getMessage());
/* 185:    */     }
/* 186:177 */     if (con != null) {
/* 187:178 */       con.close();
/* 188:    */     }
/* 189:    */   }
/* 190:    */   
/* 191:    */   public static ServicePricingBean viewPrice(String accountId, String serviceType, String networkPrefix)
/* 192:    */     throws Exception
/* 193:    */   {
/* 194:183 */     ServicePricingBean bean = new ServicePricingBean();
/* 195:    */     
/* 196:    */ 
/* 197:186 */     ResultSet rs = null;
/* 198:187 */     Connection con = null;
/* 199:188 */     PreparedStatement prepstat = null;
/* 200:189 */     String keyword = "";
/* 201:    */     try
/* 202:    */     {
/* 203:192 */       con = DConnect.getConnection();
/* 204:    */       
/* 205:194 */       String query = "select keyword from service_pricing where account_id=? and service_type=? and network_prefix=?";
/* 206:195 */       prepstat = con.prepareStatement(query);
/* 207:196 */       prepstat.setString(1, accountId);
/* 208:197 */       prepstat.setString(2, serviceType);
/* 209:198 */       prepstat.setString(3, networkPrefix);
/* 210:199 */       rs = prepstat.executeQuery();
/* 211:200 */       while (rs.next()) {
/* 212:201 */         keyword = rs.getString("keyword");
/* 213:    */       }
/* 214:204 */       query = "select * from service_definition where keyword=? and account_id=?";
/* 215:205 */       prepstat = con.prepareStatement(query);
/* 216:206 */       prepstat.setString(1, keyword);
/* 217:207 */       prepstat.setString(2, accountId);
/* 218:208 */       rs = prepstat.executeQuery();
/* 219:209 */       if (rs.next())
/* 220:    */       {
/* 221:210 */         query = "select * from service_pricing where account_id=? and service_type=? and network_prefix=?";
/* 222:211 */         prepstat = con.prepareStatement(query);
/* 223:    */         
/* 224:213 */         prepstat.setString(1, accountId);
/* 225:214 */         prepstat.setString(2, serviceType);
/* 226:215 */         prepstat.setString(3, networkPrefix);
/* 227:    */         
/* 228:217 */         rs = prepstat.executeQuery();
/* 229:219 */         while (rs.next())
/* 230:    */         {
/* 231:220 */           bean.setAccountId(rs.getString("account_id"));
/* 232:221 */           bean.setKeyword(rs.getString("keyword"));
/* 233:222 */           bean.setNetworkPrefix(rs.getString("network_prefix"));
/* 234:223 */           bean.setPrice(rs.getString("price"));
/* 235:224 */           bean.setServiceType(rs.getString("service_type"));
/* 236:225 */           bean.setShortCode(rs.getString("short_code"));
/* 237:    */         }
/* 238:    */       }
/* 239:    */     }
/* 240:    */     catch (Exception ex)
/* 241:    */     {
/* 242:229 */       if (con != null) {
/* 243:230 */         con.close();
/* 244:    */       }
/* 245:232 */       throw new Exception(ex.getMessage());
/* 246:    */     }
/* 247:234 */     if (con != null) {
/* 248:235 */       con.close();
/* 249:    */     }
/* 250:238 */     return bean;
/* 251:    */   }
/* 252:    */   
/* 253:    */   public static ServicePricingBean viewPriceWithUrl(String accountId, String serviceType, String networkPrefix)
/* 254:    */     throws Exception
/* 255:    */   {
/* 256:242 */     ServicePricingBean bean = new ServicePricingBean();
/* 257:    */     
/* 258:    */ 
/* 259:245 */     ResultSet rs = null;
/* 260:246 */     Connection con = null;
/* 261:247 */     PreparedStatement prepstat = null;
/* 262:248 */     String keyword = "";
/* 263:    */     try
/* 264:    */     {
/* 265:251 */       con = DConnect.getConnection();
/* 266:    */       
/* 267:253 */       String query = "select keyword from service_pricing where account_id=? and service_type=? and network_prefix=?";
/* 268:254 */       prepstat = con.prepareStatement(query);
/* 269:255 */       prepstat.setString(1, accountId);
/* 270:256 */       prepstat.setString(2, serviceType);
/* 271:257 */       prepstat.setString(3, networkPrefix);
/* 272:258 */       rs = prepstat.executeQuery();
/* 273:259 */       while (rs.next()) {
/* 274:260 */         keyword = rs.getString("keyword");
/* 275:    */       }
/* 276:263 */       query = "select * from service_definition where keyword=? and account_id=?";
/* 277:264 */       prepstat = con.prepareStatement(query);
/* 278:265 */       prepstat.setString(1, keyword);
/* 279:266 */       prepstat.setString(2, accountId);
/* 280:267 */       rs = prepstat.executeQuery();
/* 281:268 */       if (rs.next())
/* 282:    */       {
/* 283:269 */         query = "select * from service_pricing where account_id=? and service_type=? and network_prefix=?";
/* 284:270 */         prepstat = con.prepareStatement(query);
/* 285:    */         
/* 286:272 */         prepstat.setString(1, accountId);
/* 287:273 */         prepstat.setString(2, serviceType);
/* 288:274 */         prepstat.setString(3, networkPrefix);
/* 289:    */         
/* 290:276 */         rs = prepstat.executeQuery();
/* 291:278 */         while (rs.next())
/* 292:    */         {
/* 293:279 */           bean.setAccountId(rs.getString("account_id"));
/* 294:280 */           bean.setKeyword(rs.getString("keyword"));
/* 295:281 */           bean.setNetworkPrefix(rs.getString("network_prefix"));
/* 296:282 */           bean.setPrice(rs.getString("price"));
/* 297:283 */           bean.setServiceType(rs.getString("service_type"));
/* 298:284 */           bean.setShortCode(rs.getString("short_code"));
/* 299:285 */           bean.setUrl(rs.getString("billing_url"));
/* 300:    */         }
/* 301:    */       }
/* 302:    */     }
/* 303:    */     catch (Exception ex)
/* 304:    */     {
/* 305:289 */       if (con != null) {
/* 306:290 */         con.close();
/* 307:    */       }
/* 308:292 */       throw new Exception(ex.getMessage());
/* 309:    */     }
/* 310:294 */     if (con != null) {
/* 311:295 */       con.close();
/* 312:    */     }
/* 313:298 */     return bean;
/* 314:    */   }
/* 315:    */   
/* 316:    */   public static ArrayList viewServicePricesForNetwork(String accountId, String serviceType)
/* 317:    */     throws Exception
/* 318:    */   {
/* 319:302 */     ArrayList sites = new ArrayList();
/* 320:    */     
/* 321:304 */     ResultSet rs = null;
/* 322:305 */     Connection con = null;
/* 323:306 */     PreparedStatement prepstat = null;
/* 324:307 */     ArrayList keywords = new ArrayList();
/* 325:    */     try
/* 326:    */     {
/* 327:310 */       con = DConnect.getConnection();
/* 328:    */       
/* 329:312 */       String query = "select keyword from service_definition where account_id=? and service_type=?";
/* 330:313 */       prepstat = con.prepareStatement(query);
/* 331:314 */       prepstat.setString(1, accountId);
/* 332:315 */       prepstat.setString(2, serviceType);
/* 333:316 */       rs = prepstat.executeQuery();
/* 334:317 */       while (rs.next()) {
/* 335:318 */         keywords.add(rs.getString("keyword"));
/* 336:    */       }
/* 337:320 */       if (keywords.size() > 0)
/* 338:    */       {
/* 339:321 */         query = "select * from service_pricing where account_id=? and service_type=? and (";
/* 340:322 */         for (int i = 0; i < keywords.size(); i++)
/* 341:    */         {
/* 342:323 */           query = query + "keyword='" + keywords.get(i).toString() + "'";
/* 343:324 */           if (i == keywords.size() - 1) {
/* 344:325 */             query = query + ")";
/* 345:    */           } else {
/* 346:327 */             query = query + " or ";
/* 347:    */           }
/* 348:    */         }
/* 349:330 */         prepstat = con.prepareStatement(query);
/* 350:331 */         prepstat.setString(1, accountId);
/* 351:332 */         prepstat.setString(2, serviceType);
/* 352:333 */         rs = prepstat.executeQuery();
/* 353:335 */         while (rs.next())
/* 354:    */         {
/* 355:336 */           ServicePricingBean bean = new ServicePricingBean();
/* 356:    */           
/* 357:338 */           bean.setAccountId(rs.getString("account_id"));
/* 358:339 */           bean.setKeyword(rs.getString("keyword"));
/* 359:340 */           bean.setNetworkPrefix(rs.getString("network_prefix"));
/* 360:341 */           bean.setPrice(rs.getString("price"));
/* 361:342 */           bean.setServiceType(rs.getString("service_type"));
/* 362:343 */           bean.setShortCode(rs.getString("short_code"));
/* 363:    */           
/* 364:345 */           sites.add(bean);
/* 365:    */         }
/* 366:    */       }
/* 367:    */     }
/* 368:    */     catch (Exception ex)
/* 369:    */     {
/* 370:349 */       if (con != null) {
/* 371:350 */         con.close();
/* 372:    */       }
/* 373:352 */       throw new Exception(ex.getMessage());
/* 374:    */     }
/* 375:354 */     if (con != null) {
/* 376:355 */       con.close();
/* 377:    */     }
/* 378:358 */     return sites;
/* 379:    */   }
/* 380:    */   
/* 381:    */   public static ArrayList viewAllForCP(String accountId)
/* 382:    */     throws Exception
/* 383:    */   {
/* 384:362 */     ArrayList sites = new ArrayList();
/* 385:    */     
/* 386:364 */     ResultSet rs = null;
/* 387:365 */     Connection con = null;
/* 388:366 */     PreparedStatement prepstat = null;
/* 389:367 */     ArrayList keywords = new ArrayList();
/* 390:    */     try
/* 391:    */     {
/* 392:370 */       con = DConnect.getConnection();
/* 393:    */       
/* 394:372 */       String query = "select keyword from service_definition where account_id=?";
/* 395:373 */       prepstat = con.prepareStatement(query);
/* 396:374 */       prepstat.setString(1, accountId);
/* 397:375 */       rs = prepstat.executeQuery();
/* 398:376 */       while (rs.next()) {
/* 399:377 */         keywords.add(rs.getString("keyword"));
/* 400:    */       }
/* 401:379 */       if (keywords.size() > 0)
/* 402:    */       {
/* 403:380 */         query = "select * from service_pricing where account_id=? and (";
/* 404:381 */         for (int i = 0; i < keywords.size(); i++)
/* 405:    */         {
/* 406:382 */           query = query + "keyword='" + keywords.get(i).toString() + "'";
/* 407:383 */           if (i == keywords.size() - 1) {
/* 408:384 */             query = query + ")";
/* 409:    */           } else {
/* 410:386 */             query = query + " or ";
/* 411:    */           }
/* 412:    */         }
/* 413:389 */         prepstat = con.prepareStatement(query);
/* 414:390 */         prepstat.setString(1, accountId);
/* 415:391 */         rs = prepstat.executeQuery();
/* 416:393 */         while (rs.next())
/* 417:    */         {
/* 418:394 */           ServicePricingBean bean = new ServicePricingBean();
/* 419:    */           
/* 420:396 */           bean.setAccountId(rs.getString("account_id"));
/* 421:397 */           bean.setKeyword(rs.getString("keyword"));
/* 422:398 */           bean.setNetworkPrefix(rs.getString("network_prefix"));
/* 423:399 */           bean.setPrice(rs.getString("price"));
/* 424:400 */           bean.setServiceType(rs.getString("service_type"));
/* 425:401 */           bean.setShortCode(rs.getString("short_code"));
/* 426:    */           
/* 427:403 */           sites.add(bean);
/* 428:    */         }
/* 429:    */       }
/* 430:    */     }
/* 431:    */     catch (Exception ex)
/* 432:    */     {
/* 433:407 */       if (con != null) {
/* 434:408 */         con.close();
/* 435:    */       }
/* 436:410 */       throw new Exception(ex.getMessage());
/* 437:    */     }
/* 438:412 */     if (con != null) {
/* 439:413 */       con.close();
/* 440:    */     }
/* 441:416 */     return sites;
/* 442:    */   }
/* 443:    */   
/* 444:    */   public static HashMap viewPricesForCP(String accountId)
/* 445:    */     throws Exception
/* 446:    */   {
/* 447:420 */     HashMap grpPrices = new HashMap();
/* 448:421 */     ArrayList prices = new ArrayList();
/* 449:    */     
/* 450:423 */     ResultSet rs = null;
/* 451:424 */     Connection con = null;
/* 452:425 */     PreparedStatement prepstat = null;
/* 453:426 */     ArrayList keywords = new ArrayList();
/* 454:    */     try
/* 455:    */     {
/* 456:429 */       con = DConnect.getConnection();
/* 457:    */       
/* 458:431 */       String query = "select keyword from service_definition where account_id=?";
/* 459:432 */       prepstat = con.prepareStatement(query);
/* 460:433 */       prepstat.setString(1, accountId);
/* 461:434 */       rs = prepstat.executeQuery();
/* 462:435 */       while (rs.next()) {
/* 463:436 */         keywords.add(rs.getString("keyword"));
/* 464:    */       }
/* 465:438 */       if (keywords.size() > 0)
/* 466:    */       {
/* 467:439 */         query = "select * from service_pricing where account_id=? and (";
/* 468:440 */         for (int i = 0; i < keywords.size(); i++)
/* 469:    */         {
/* 470:441 */           query = query + "keyword='" + keywords.get(i).toString() + "'";
/* 471:442 */           if (i == keywords.size() - 1) {
/* 472:443 */             query = query + ")";
/* 473:    */           } else {
/* 474:445 */             query = query + " or ";
/* 475:    */           }
/* 476:    */         }
/* 477:448 */         prepstat = con.prepareStatement(query);
/* 478:449 */         prepstat.setString(1, accountId);
/* 479:450 */         rs = prepstat.executeQuery();
/* 480:    */         
/* 481:452 */         String conTypeMem = "1";
/* 482:453 */         while (rs.next())
/* 483:    */         {
/* 484:454 */           ServicePricingBean bean = new ServicePricingBean();
/* 485:455 */           bean.setAccountId(rs.getString("account_id"));
/* 486:456 */           bean.setKeyword(rs.getString("keyword"));
/* 487:457 */           bean.setNetworkPrefix(rs.getString("network_prefix"));
/* 488:458 */           bean.setPrice(rs.getString("price"));
/* 489:459 */           bean.setServiceType(rs.getString("service_type"));
/* 490:460 */           bean.setShortCode(rs.getString("short_code"));
/* 491:462 */           if (bean.getServiceType().equals(conTypeMem))
/* 492:    */           {
/* 493:463 */             prices.add(bean);
/* 494:    */           }
/* 495:    */           else
/* 496:    */           {
/* 497:465 */             grpPrices.put(new String(conTypeMem), prices);
/* 498:466 */             conTypeMem = bean.getServiceType();
/* 499:467 */             prices = new ArrayList();
/* 500:468 */             prices.add(bean);
/* 501:    */           }
/* 502:    */         }
/* 503:471 */         grpPrices.put(conTypeMem, prices);
/* 504:    */       }
/* 505:    */     }
/* 506:    */     catch (Exception ex)
/* 507:    */     {
/* 508:474 */       if (con != null) {
/* 509:475 */         con.close();
/* 510:    */       }
/* 511:477 */       throw new Exception(ex.getMessage());
/* 512:    */     }
/* 513:479 */     if (con != null) {
/* 514:480 */       con.close();
/* 515:    */     }
/* 516:483 */     return grpPrices;
/* 517:    */   }
/* 518:    */   
/* 519:    */   public static HashMap viewAllPrices()
/* 520:    */     throws Exception
/* 521:    */   {
/* 522:487 */     HashMap prices = new HashMap();
/* 523:    */     
/* 524:    */ 
/* 525:490 */     ResultSet rs = null;
/* 526:491 */     ResultSet types = null;
/* 527:492 */     ResultSet cps = null;
/* 528:493 */     Connection con = null;
/* 529:494 */     PreparedStatement prepstat = null;
/* 530:495 */     ArrayList keywords = new ArrayList();
/* 531:    */     try
/* 532:    */     {
/* 533:498 */       con = DConnect.getConnection();
/* 534:    */       
/* 535:500 */       String query = "select service_type from service_route where parent_service_type=1";
/* 536:501 */       prepstat = con.prepareStatement(query);
/* 537:502 */       types = prepstat.executeQuery();
/* 538:504 */       while (types.next())
/* 539:    */       {
/* 540:505 */         String srvType = types.getString("service_type");
/* 541:506 */         query = "select account_id from service_definition where service_type='" + srvType + "'";
/* 542:507 */         prepstat = con.prepareStatement(query);
/* 543:508 */         cps = prepstat.executeQuery();
/* 544:    */         
/* 545:510 */         HashMap cpPrices = new HashMap();
/* 546:511 */         while (cps.next())
/* 547:    */         {
/* 548:512 */           String accountId = cps.getString("account_id");
/* 549:513 */           query = "select keyword from service_definition where account_id='" + accountId + "' and service_type='" + srvType + "'";
/* 550:514 */           prepstat = con.prepareStatement(query);
/* 551:515 */           rs = prepstat.executeQuery();
/* 552:516 */           while (rs.next()) {
/* 553:517 */             keywords.add(rs.getString("keyword"));
/* 554:    */           }
/* 555:519 */           ArrayList indivPrices = new ArrayList();
/* 556:520 */           if (keywords.size() > 0)
/* 557:    */           {
/* 558:521 */             query = "select * from service_pricing where service_type='" + srvType + "' and account_id='" + accountId + "' and (";
/* 559:522 */             for (int i = 0; i < keywords.size(); i++)
/* 560:    */             {
/* 561:523 */               query = query + "keyword='" + keywords.get(i).toString() + "'";
/* 562:524 */               if (i == keywords.size() - 1) {
/* 563:525 */                 query = query + ")";
/* 564:    */               } else {
/* 565:527 */                 query = query + " or ";
/* 566:    */               }
/* 567:    */             }
/* 568:530 */             prepstat = con.prepareStatement(query);
/* 569:531 */             rs = prepstat.executeQuery();
/* 570:533 */             while (rs.next())
/* 571:    */             {
/* 572:534 */               ServicePricingBean bean = new ServicePricingBean();
/* 573:535 */               bean.setAccountId(rs.getString("account_id"));
/* 574:536 */               bean.setKeyword(rs.getString("keyword"));
/* 575:537 */               bean.setNetworkPrefix(rs.getString("network_prefix"));
/* 576:538 */               bean.setPrice(rs.getString("price"));
/* 577:539 */               bean.setServiceType(rs.getString("service_type"));
/* 578:540 */               bean.setShortCode(rs.getString("short_code"));
/* 579:    */               
/* 580:542 */               indivPrices.add(bean);
/* 581:    */             }
/* 582:    */           }
/* 583:545 */           cpPrices.put(accountId, indivPrices);
/* 584:    */         }
/* 585:547 */         prices.put(srvType, cpPrices);
/* 586:    */       }
/* 587:    */     }
/* 588:    */     catch (Exception ex)
/* 589:    */     {
/* 590:550 */       if (con != null) {
/* 591:551 */         con.close();
/* 592:    */       }
/* 593:553 */       throw new Exception(ex.getMessage());
/* 594:    */     }
/* 595:555 */     if (con != null) {
/* 596:556 */       con.close();
/* 597:    */     }
/* 598:559 */     return prices;
/* 599:    */   }
/* 600:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.ServicePricingDB
 * JD-Core Version:    0.7.0.1
 */