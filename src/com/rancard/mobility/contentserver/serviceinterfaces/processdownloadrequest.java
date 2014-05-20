/*   1:    */ package com.rancard.mobility.contentserver.serviceinterfaces;
/*   2:    */ 
/*   3:    */ import com.rancard.common.Feedback;
/*   4:    */ import com.rancard.common.uidGen;
/*   5:    */ import com.rancard.mobility.common.FonCapabilityMtrx;
/*   6:    */ import com.rancard.mobility.contentprovider.User;
/*   7:    */ import com.rancard.mobility.contentserver.CPConnections;
/*   8:    */ import com.rancard.mobility.contentserver.CPSite;
/*   9:    */ import com.rancard.mobility.contentserver.ContentItem;
/*  10:    */ import com.rancard.mobility.contentserver.Format;
/*  11:    */ import com.rancard.mobility.contentserver.RepositoryManager;
/*  12:    */ import com.rancard.mobility.contentserver.Transaction;
/*  13:    */ import com.rancard.mobility.infoserver.common.services.ServiceManager;
/*  14:    */ import com.rancard.mobility.infoserver.common.services.UserService;
/*  15:    */ import com.rancard.mobility.infoserver.common.services.UserServiceTransaction;
/*  16:    */ import com.rancard.util.URLUTF8Encoder;
/*  17:    */ import com.rancard.util.payment.PaymentManager;
/*  18:    */ import com.rancard.util.payment.PricePoint;
/*  19:    */ import java.io.BufferedReader;
/*  20:    */ import java.io.IOException;
/*  21:    */ import java.io.InputStreamReader;
/*  22:    */ import java.io.PrintStream;
/*  23:    */ import java.io.PrintWriter;
/*  24:    */ import java.net.URL;
/*  25:    */ import java.util.Calendar;
/*  26:    */ import java.util.Date;
/*  27:    */ import java.util.HashMap;
/*  28:    */ import javax.servlet.RequestDispatcher;
/*  29:    */ import javax.servlet.ServletContext;
/*  30:    */ import javax.servlet.ServletException;
/*  31:    */ import javax.servlet.ServletRequest;
/*  32:    */ import javax.servlet.ServletResponse;
/*  33:    */ import javax.servlet.http.HttpServlet;
/*  34:    */ import javax.servlet.http.HttpServletRequest;
/*  35:    */ import javax.servlet.http.HttpServletResponse;
/*  36:    */ import net.sourceforge.wurfl.wurflapi.CapabilityMatrix;
/*  37:    */ import net.sourceforge.wurfl.wurflapi.UAManager;
/*  38:    */ 
/*  39:    */ public class processdownloadrequest
/*  40:    */   extends HttpServlet
/*  41:    */   implements RequestDispatcher
/*  42:    */ {
/*  43:    */   private static final String PIN_OPTION = "pinredemption";
/*  44:    */   private static final String SHORTCODE_OPTION = "shortcode";
/*  45:    */   
/*  46:    */   public void init()
/*  47:    */     throws ServletException
/*  48:    */   {}
/*  49:    */   
/*  50:    */   public void doGet(HttpServletRequest request, HttpServletResponse response)
/*  51:    */     throws ServletException, IOException
/*  52:    */   {
/*  53: 28 */     String itemName = "";
/*  54: 29 */     String message = "";
/*  55:    */     
/*  56:    */ 
/*  57: 32 */     String svrAddr = getServletContext().getInitParameter("contentServerUrl");
/*  58:    */     
/*  59:    */ 
/*  60: 35 */     PrintWriter out = response.getWriter();
/*  61: 36 */     PricePoint pricePoint = new PricePoint();
/*  62:    */     
/*  63: 38 */     String transaction = request.getParameter("msg");
/*  64: 39 */     String keyword = request.getParameter("keyword");
/*  65: 40 */     String msisdn = request.getParameter("msisdn");
/*  66: 41 */     String phoneId = request.getParameter("phoneId");
/*  67: 42 */     String ua = request.getParameter("ua");
/*  68: 43 */     String siteId = request.getParameter("siteId");
/*  69: 44 */     String ack = (String)request.getAttribute("ack");
/*  70: 45 */     String regId = request.getParameter("regId");
/*  71: 46 */     String pricePointId = request.getParameter("pricePoint");
/*  72: 47 */     String siteType = (String)request.getAttribute("site_type");
/*  73: 48 */     String lang = (String)request.getAttribute("default_lang");
/*  74: 49 */     if ((lang == null) || (lang.equals(""))) {
/*  75: 50 */       lang = "en";
/*  76:    */     }
/*  77: 52 */     Feedback feedback = (Feedback)getServletContext().getAttribute("feedback_" + lang);
/*  78: 53 */     if (feedback == null) {
/*  79:    */       try
/*  80:    */       {
/*  81: 55 */         feedback = new Feedback();
/*  82:    */       }
/*  83:    */       catch (Exception e) {}
/*  84:    */     }
/*  85: 60 */     String id = new String();
/*  86: 61 */     String listId = new String();
/*  87: 62 */     String pin = new String();
/*  88: 63 */     String responsePath = new String();
/*  89: 64 */     if (ack == null) {
/*  90: 65 */       ack = "";
/*  91:    */     }
/*  92: 67 */     if (regId == null) {
/*  93: 68 */       regId = "";
/*  94:    */     }
/*  95: 70 */     if (phoneId == null) {
/*  96: 71 */       phoneId = "";
/*  97:    */     }
/*  98: 73 */     if (pricePointId == null) {
/*  99: 74 */       pricePointId = "";
/* 100:    */     }
/* 101: 78 */     if ((transaction == null) || (transaction.equals("")))
/* 102:    */     {
/* 103:    */       try
/* 104:    */       {
/* 105: 80 */         if (siteType.equals("2")) {
/* 106: 81 */           message = feedback.getUserFriendlyDescription("1000");
/* 107:    */         } else {
/* 108: 83 */           message = feedback.formDefaultMessage("1000");
/* 109:    */         }
/* 110:    */       }
/* 111:    */       catch (Exception ex)
/* 112:    */       {
/* 113: 86 */         message = ex.getMessage();
/* 114:    */       }
/* 115: 88 */       out.println(message);
/* 116: 89 */       return;
/* 117:    */     }
/* 118: 91 */     String[] rqst = transaction.split("-");
/* 119: 92 */     if (rqst.length == 1)
/* 120:    */     {
/* 121: 93 */       listId = (String)request.getAttribute("acctId");
/* 122:    */       try
/* 123:    */       {
/* 124: 95 */         id = RepositoryManager.resolveShortItemReference(rqst[0], listId, keyword);
/* 125:    */       }
/* 126:    */       catch (Exception ex) {}
/* 127:    */     }
/* 128: 98 */     else if (rqst.length == 2)
/* 129:    */     {
/* 130: 99 */       id = rqst[0];
/* 131:100 */       listId = rqst[1];
/* 132:    */     }
/* 133:134 */     if ((msisdn == null) || (msisdn.equals("")))
/* 134:    */     {
/* 135:    */       try
/* 136:    */       {
/* 137:136 */         if (siteType.equals("2")) {
/* 138:137 */           message = feedback.getUserFriendlyDescription("2000");
/* 139:    */         } else {
/* 140:139 */           message = feedback.formDefaultMessage("2000");
/* 141:    */         }
/* 142:    */       }
/* 143:    */       catch (Exception ex)
/* 144:    */       {
/* 145:142 */         message = ex.getMessage();
/* 146:    */       }
/* 147:144 */       out.println(message);
/* 148:145 */       return;
/* 149:    */     }
/* 150:149 */     System.out.println("Received request to download content");
/* 151:150 */     System.out.println("Date received: " + Calendar.getInstance().getTime().toString());
/* 152:151 */     System.out.println("Request: " + keyword + " " + transaction);
/* 153:152 */     System.out.println("User's number: " + msisdn);
/* 154:    */     
/* 155:    */ 
/* 156:    */ 
/* 157:156 */     String s = request.getProtocol().toLowerCase();
/* 158:157 */     s = s.substring(0, s.indexOf("/")).toLowerCase();
/* 159:158 */     String baseUrl = s + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
/* 160:    */     
/* 161:160 */     ContentItem item = null;
/* 162:161 */     Format format = null;
/* 163:162 */     CPConnections cnxn = null;
/* 164:163 */     CPSite initSite = null;
/* 165:    */     try
/* 166:    */     {
/* 167:167 */       initSite = CPSite.viewSite(siteId);
/* 168:168 */       System.out.println("Requesting site: " + initSite.getCpSiteName());
/* 169:169 */       if ((initSite.getCpId() == null) || (initSite.getCpId().equals(""))) {
/* 170:170 */         throw new Exception();
/* 171:    */       }
/* 172:    */     }
/* 173:    */     catch (Exception ex)
/* 174:    */     {
/* 175:    */       try
/* 176:    */       {
/* 177:174 */         if (siteType.equals("2")) {
/* 178:175 */           message = feedback.getUserFriendlyDescription("4001");
/* 179:    */         } else {
/* 180:177 */           message = feedback.formDefaultMessage("4001");
/* 181:    */         }
/* 182:    */       }
/* 183:    */       catch (Exception e)
/* 184:    */       {
/* 185:180 */         message = ex.getMessage();
/* 186:    */       }
/* 187:182 */       if ((responsePath != null) && (!responsePath.equals("")) && (!responsePath.equalsIgnoreCase("null"))) {
/* 188:183 */         response.sendRedirect(responsePath + "?reply=" + message);
/* 189:    */       } else {
/* 190:185 */         out.println(message);
/* 191:    */       }
/* 192:187 */       return;
/* 193:    */     }
/* 194:    */     try
/* 195:    */     {
/* 196:192 */       item = new ContentItem().viewContentItem(id, listId);
/* 197:193 */       System.out.println("Content item's title is: " + item.gettitle());
/* 198:194 */       System.out.println("Content MIME type is: " + item.getFormat().getMimeType());
/* 199:    */       
/* 200:196 */       format = item.getFormat();
/* 201:198 */       if ((item.getid() == null) || ((item.getid().equals("")) && (item.getListId() != null)) || (item.getListId().equals(""))) {
/* 202:200 */         throw new Exception();
/* 203:    */       }
/* 204:202 */       itemName = item.gettitle();
/* 205:    */     }
/* 206:    */     catch (Exception e)
/* 207:    */     {
/* 208:    */       try
/* 209:    */       {
/* 210:205 */         if (siteType.equals("2")) {
/* 211:206 */           message = feedback.getUserFriendlyDescription("1000");
/* 212:    */         } else {
/* 213:208 */           message = feedback.formDefaultMessage("1000");
/* 214:    */         }
/* 215:    */       }
/* 216:    */       catch (Exception ex)
/* 217:    */       {
/* 218:211 */         message = ex.getMessage();
/* 219:    */       }
/* 220:213 */       if ((responsePath != null) && (!responsePath.equals("")) && (!responsePath.equalsIgnoreCase("null"))) {
/* 221:214 */         response.sendRedirect(responsePath + "?reply=" + message);
/* 222:    */       } else {
/* 223:216 */         out.println(message);
/* 224:    */       }
/* 225:218 */       return;
/* 226:    */     }
/* 227:222 */     String number = msisdn;
/* 228:223 */     if (number.indexOf("+") != -1)
/* 229:    */     {
/* 230:224 */       StringBuffer sb = new StringBuffer(number);
/* 231:225 */       sb.deleteCharAt(number.indexOf("+"));
/* 232:226 */       number = sb.toString();
/* 233:    */       
/* 234:228 */       sb = null;
/* 235:    */     }
/* 236:230 */     number = number.trim();
/* 237:    */     try
/* 238:    */     {
/* 239:234 */       Long.parseLong(number);
/* 240:235 */       msisdn = "+" + number;
/* 241:    */     }
/* 242:    */     catch (NumberFormatException e)
/* 243:    */     {
/* 244:    */       try
/* 245:    */       {
/* 246:238 */         if (siteType.equals("2")) {
/* 247:239 */           message = feedback.getUserFriendlyDescription("2000");
/* 248:    */         } else {
/* 249:241 */           message = feedback.formDefaultMessage("2000");
/* 250:    */         }
/* 251:    */       }
/* 252:    */       catch (Exception ex)
/* 253:    */       {
/* 254:244 */         message = ex.getMessage();
/* 255:    */       }
/* 256:246 */       if ((responsePath != null) && (!responsePath.equals("")) && (!responsePath.equalsIgnoreCase("null"))) {
/* 257:247 */         response.sendRedirect(responsePath + "?reply=" + message);
/* 258:    */       } else {
/* 259:249 */         out.println(message);
/* 260:    */       }
/* 261:251 */       return;
/* 262:    */     }
/* 263:254 */     if (initSite.getCheckUser() == true)
/* 264:    */     {
/* 265:255 */       boolean check = false;
/* 266:256 */       if ((regId != null) && (!regId.equals("")))
/* 267:    */       {
/* 268:    */         try
/* 269:    */         {
/* 270:258 */           check = ServiceManager.verifyUser(msisdn, regId, initSite.getCpId(), keyword);
/* 271:    */         }
/* 272:    */         catch (Exception e)
/* 273:    */         {
/* 274:260 */           check = false;
/* 275:    */         }
/* 276:262 */         if (!check)
/* 277:    */         {
/* 278:    */           try
/* 279:    */           {
/* 280:264 */             if (siteType.equals("2")) {
/* 281:265 */               message = feedback.getUserFriendlyDescription("10000");
/* 282:    */             } else {
/* 283:267 */               message = feedback.formDefaultMessage("10000");
/* 284:    */             }
/* 285:    */           }
/* 286:    */           catch (Exception ex)
/* 287:    */           {
/* 288:270 */             message = ex.getMessage();
/* 289:    */           }
/* 290:272 */           if ((responsePath != null) && (!responsePath.equals("")) && (!responsePath.equalsIgnoreCase("null"))) {
/* 291:273 */             response.sendRedirect(responsePath + "?reply=" + message);
/* 292:    */           } else {
/* 293:275 */             out.println(message);
/* 294:    */           }
/* 295:    */         }
/* 296:    */       }
/* 297:    */       else
/* 298:    */       {
/* 299:    */         try
/* 300:    */         {
/* 301:281 */           if (siteType.equals("2")) {
/* 302:282 */             message = feedback.getUserFriendlyDescription("4002");
/* 303:    */           } else {
/* 304:284 */             message = feedback.formDefaultMessage("4002");
/* 305:    */           }
/* 306:    */         }
/* 307:    */         catch (Exception ex)
/* 308:    */         {
/* 309:287 */           message = ex.getMessage();
/* 310:    */         }
/* 311:289 */         if ((responsePath != null) && (!responsePath.equals("")) && (!responsePath.equalsIgnoreCase("null"))) {
/* 312:290 */           response.sendRedirect(responsePath + "?reply=" + message);
/* 313:    */         } else {
/* 314:292 */           out.println(message);
/* 315:    */         }
/* 316:294 */         return;
/* 317:    */       }
/* 318:    */     }
/* 319:    */     try
/* 320:    */     {
/* 321:300 */       if (pricePointId.equals(""))
/* 322:    */       {
/* 323:302 */         String[] itemPricePoints = item.getPrice().split(",");
/* 324:303 */         if ((itemPricePoints == null) || (itemPricePoints[0] == null) || (itemPricePoints[0].equals("")))
/* 325:    */         {
/* 326:304 */           UserService thisService = (UserService)request.getAttribute("thisService");
/* 327:305 */           if (thisService != null) {
/* 328:306 */             itemPricePoints = thisService.getPricing().split(",");
/* 329:    */           }
/* 330:308 */           thisService = null;
/* 331:    */         }
/* 332:311 */         if (((itemPricePoints == null) || (itemPricePoints[0] == null) || (itemPricePoints[0].equals(""))) && (item.isFree() != true)) {
/* 333:312 */           throw new Exception("11000");
/* 334:    */         }
/* 335:315 */         pricePoint = PaymentManager.viewPricePointFor(itemPricePoints, msisdn);
/* 336:316 */         pricePointId = pricePoint.getPricePointId();
/* 337:    */       }
/* 338:    */       else
/* 339:    */       {
/* 340:318 */         String[] itemPricePoints = new String[1];
/* 341:319 */         itemPricePoints[0] = pricePointId;
/* 342:320 */         pricePoint = PaymentManager.viewPricePointFor(itemPricePoints, msisdn);
/* 343:    */       }
/* 344:    */     }
/* 345:    */     catch (Exception e)
/* 346:    */     {
/* 347:    */       try
/* 348:    */       {
/* 349:324 */         if (siteType.equals("2")) {
/* 350:325 */           message = feedback.getUserFriendlyDescription(e.getMessage());
/* 351:    */         } else {
/* 352:327 */           message = feedback.formDefaultMessage(e.getMessage());
/* 353:    */         }
/* 354:    */       }
/* 355:    */       catch (Exception ex)
/* 356:    */       {
/* 357:330 */         message = ex.getMessage();
/* 358:    */       }
/* 359:332 */       out.println(message);
/* 360:333 */       return;
/* 361:    */     }
/* 362:    */     try
/* 363:    */     {
/* 364:338 */       cnxn = CPConnections.getConnection(initSite.getCpId(), msisdn);
/* 365:339 */       System.out.println("Connection for Messaging: " + cnxn.getConnection());
/* 366:    */     }
/* 367:    */     catch (Exception e)
/* 368:    */     {
/* 369:    */       try
/* 370:    */       {
/* 371:342 */         if (siteType.equals("2")) {
/* 372:343 */           message = feedback.getUserFriendlyDescription("8002");
/* 373:    */         } else {
/* 374:345 */           message = feedback.formDefaultMessage("8002");
/* 375:    */         }
/* 376:    */       }
/* 377:    */       catch (Exception ex)
/* 378:    */       {
/* 379:348 */         message = ex.getMessage();
/* 380:    */       }
/* 381:350 */       out.println(message);
/* 382:351 */       return;
/* 383:    */     }
/* 384:355 */     FonCapabilityMtrx fcm = null;
/* 385:356 */     if (((ua != null) && (!ua.equals(""))) || ((phoneId != null) && (!phoneId.equals("")))) {
/* 386:    */       try
/* 387:    */       {
/* 388:358 */         System.out.println("Setting up compatibility matrix");
/* 389:359 */         fcm = (FonCapabilityMtrx)getServletContext().getAttribute("capabilitiesMtrx");
/* 390:360 */         if (fcm == null) {
/* 391:361 */           throw new Exception();
/* 392:    */         }
/* 393:    */       }
/* 394:    */       catch (Exception e)
/* 395:    */       {
/* 396:365 */         System.out.println("Setup of compatibility matrix FAILED");
/* 397:    */         try
/* 398:    */         {
/* 399:369 */           if (siteType.equals("2")) {
/* 400:370 */             message = feedback.getUserFriendlyDescription("2003");
/* 401:    */           } else {
/* 402:372 */             message = feedback.formDefaultMessage("2003");
/* 403:    */           }
/* 404:    */         }
/* 405:    */         catch (Exception ex)
/* 406:    */         {
/* 407:375 */           message = ex.getMessage();
/* 408:    */         }
/* 409:377 */         out.println(message);
/* 410:378 */         return;
/* 411:    */       }
/* 412:    */     }
/* 413:382 */     if ((ua != null) && (!ua.equals(""))) {
/* 414:383 */       phoneId = fcm.getUAManager().getDeviceIDFromUA(ua);
/* 415:    */     }
/* 416:385 */     if ((!phoneId.equals("")) && (!phoneId.equalsIgnoreCase("null")))
/* 417:    */     {
/* 418:387 */       System.out.println("Phone ID was supplied. Checking compatibility");
/* 419:    */       
/* 420:389 */       String capabilityValue = "false";
/* 421:    */       try
/* 422:    */       {
/* 423:392 */         if (format.getPushBearer().equals("SMS"))
/* 424:    */         {
/* 425:393 */           capabilityValue = "true";
/* 426:    */         }
/* 427:    */         else
/* 428:    */         {
/* 429:396 */           String capability = fcm.findSupportedCapability(format.getFileExt());
/* 430:397 */           capabilityValue = fcm.getCapabilitiesManager().getCapabilityForDevice(phoneId, capability);
/* 431:398 */           System.out.println("Capability value for " + capability + " is: " + capabilityValue);
/* 432:401 */           if (!capabilityValue.equals("true")) {
/* 433:402 */             throw new Exception("2002");
/* 434:    */           }
/* 435:404 */           System.out.println("Phone supports requested format");
/* 436:408 */           if (format.getPushBearer().equals("WAP"))
/* 437:    */           {
/* 438:409 */             System.out.println("Checking if phone can receive a WAP push...");
/* 439:410 */             if (!fcm.getCapabilitiesManager().getCapabilityForDevice(phoneId, "wap_push_support").equals("true"))
/* 440:    */             {
/* 441:411 */               System.out.println("Phone cannot support WAP push");
/* 442:412 */               throw new Exception("2004");
/* 443:    */             }
/* 444:414 */             System.out.println("Phone supports WAP push");
/* 445:    */           }
/* 446:    */         }
/* 447:    */       }
/* 448:    */       catch (NullPointerException npe)
/* 449:    */       {
/* 450:    */         try
/* 451:    */         {
/* 452:420 */           if (siteType.equals("2")) {
/* 453:421 */             message = feedback.getUserFriendlyDescription("1");
/* 454:    */           } else {
/* 455:423 */             message = feedback.formDefaultMessage("1");
/* 456:    */           }
/* 457:    */         }
/* 458:    */         catch (Exception ex)
/* 459:    */         {
/* 460:426 */           message = ex.getMessage();
/* 461:    */         }
/* 462:428 */         if ((responsePath != null) && (!responsePath.equals("")) && (!responsePath.equalsIgnoreCase("null"))) {
/* 463:429 */           response.sendRedirect(responsePath + "?reply=" + message);
/* 464:    */         } else {
/* 465:431 */           out.println(message);
/* 466:    */         }
/* 467:433 */         return;
/* 468:    */       }
/* 469:    */       catch (Exception e)
/* 470:    */       {
/* 471:    */         try
/* 472:    */         {
/* 473:436 */           if (siteType.equals("2")) {
/* 474:437 */             message = feedback.getUserFriendlyDescription(e.getMessage());
/* 475:    */           } else {
/* 476:439 */             message = feedback.formDefaultMessage(e.getMessage());
/* 477:    */           }
/* 478:441 */           if (message == null) {
/* 479:442 */             message = e.getMessage();
/* 480:    */           }
/* 481:    */         }
/* 482:    */         catch (Exception ex)
/* 483:    */         {
/* 484:445 */           message = ex.getMessage();
/* 485:    */         }
/* 486:447 */         if ((responsePath != null) && (!responsePath.equals("")) && (!responsePath.equalsIgnoreCase("null"))) {
/* 487:448 */           response.sendRedirect(responsePath + "?reply=" + message);
/* 488:    */         } else {
/* 489:450 */           out.println(message);
/* 490:    */         }
/* 491:452 */         return;
/* 492:    */       }
/* 493:    */     }
/* 494:    */     else
/* 495:    */     {
/* 496:455 */       phoneId = "";
/* 497:    */     }
/* 498:459 */     User user = new User();
/* 499:460 */     System.out.println("Checking bandwidth availability for " + initSite.getCpSiteName());
/* 500:    */     try
/* 501:    */     {
/* 502:462 */       user = user.viewDealer(initSite.getCpId());
/* 503:464 */       if (user != null)
/* 504:    */       {
/* 505:465 */         double bandwidth = user.getBandwidthBalance();
/* 506:466 */         if (bandwidth - item.getSize().longValue() < 0.0D) {
/* 507:467 */           throw new Exception("4004");
/* 508:    */         }
/* 509:469 */         System.out.println("Site has enough bandwidth");
/* 510:    */       }
/* 511:    */       else
/* 512:    */       {
/* 513:474 */         throw new Exception("10004");
/* 514:    */       }
/* 515:    */     }
/* 516:    */     catch (Exception e)
/* 517:    */     {
/* 518:    */       try
/* 519:    */       {
/* 520:478 */         if (siteType.equals("2")) {
/* 521:479 */           message = feedback.getUserFriendlyDescription(e.getMessage());
/* 522:    */         } else {
/* 523:481 */           message = feedback.formDefaultMessage(e.getMessage());
/* 524:    */         }
/* 525:483 */         if (message == null) {
/* 526:484 */           message = e.getMessage();
/* 527:    */         }
/* 528:    */       }
/* 529:    */       catch (Exception ex)
/* 530:    */       {
/* 531:487 */         message = ex.getMessage();
/* 532:    */       }
/* 533:489 */       if ((responsePath != null) && (!responsePath.equals("")) && (!responsePath.equalsIgnoreCase("null"))) {
/* 534:490 */         response.sendRedirect(responsePath + "?reply=" + message);
/* 535:    */       } else {
/* 536:492 */         out.println(message);
/* 537:    */       }
/* 538:494 */       return;
/* 539:    */     }
/* 540:498 */     if (pin.equalsIgnoreCase("null")) {
/* 541:499 */       pin = "";
/* 542:    */     }
/* 543:501 */     Transaction download = new Transaction("", id, listId, msisdn, phoneId, false, pin, siteId, false, keyword, false, pricePointId);
/* 544:    */     
/* 545:    */ 
/* 546:504 */     System.out.println("Logging transaction...");
/* 547:    */     
/* 548:506 */     boolean isLogged = false;
/* 549:507 */     String ticket = null;
/* 550:508 */     while (!isLogged)
/* 551:    */     {
/* 552:509 */       ticket = log(10, download, "" + format.getId(), keyword);
/* 553:510 */       if (ticket == null)
/* 554:    */       {
/* 555:512 */         System.out.println("Logging transaction FAILED...");
/* 556:    */         
/* 557:514 */         isLogged = false;
/* 558:    */       }
/* 559:    */       else
/* 560:    */       {
/* 561:517 */         System.out.println("Logging transaction COMPLETED...");
/* 562:    */         
/* 563:519 */         isLogged = true;
/* 564:    */       }
/* 565:    */     }
/* 566:    */     try
/* 567:    */     {
/* 568:526 */       System.out.println("Billing...");
/* 569:528 */       if (isLogged)
/* 570:    */       {
/* 571:529 */         svrAddr = svrAddr + "initiatedownload?ticketId=" + download.getTicketID();
/* 572:530 */         UserServiceTransaction trans = new UserServiceTransaction();
/* 573:531 */         trans.setAccountId(cnxn.getProviderId());
/* 574:532 */         trans.setKeyword(keyword);
/* 575:533 */         trans.setMsg(transaction);
/* 576:534 */         trans.setMsisdn(msisdn);
/* 577:535 */         trans.setTransactionId(download.getTicketID());
/* 578:536 */         trans.setCallBackUrl(svrAddr);
/* 579:537 */         trans.setPricePoint(pricePointId);
/* 580:    */         
/* 581:    */ 
/* 582:540 */         boolean billed = false;
/* 583:543 */         if (!item.isFree())
/* 584:    */         {
/* 585:544 */           System.out.println("Item is not free...");
/* 586:    */           
/* 587:    */ 
/* 588:547 */           HashMap cdrHash = new HashMap();
/* 589:548 */           cdrHash.put("transaction_type", "download_" + item.gettitle());
/* 590:549 */           cdrHash.put("cost", pricePoint.getCurrency() + " " + pricePoint.getValue());
/* 591:550 */           cdrHash.put("service_code", "");
/* 592:551 */           cdrHash.put("aparty", download.getSubscriberMSISDN());
/* 593:552 */           cdrHash.put("bparty", "");
/* 594:553 */           cdrHash.put("provider_id", item.getProviderDetails().getName());
/* 595:    */           try
/* 596:    */           {
/* 597:556 */             billed = PaymentManager.initiatePayment(pricePoint, cnxn, msisdn, ticket, pin, cdrHash);
/* 598:    */           }
/* 599:    */           catch (Exception e)
/* 600:    */           {
/* 601:559 */             if (e.getMessage().equals("READ_TIMEOUT"))
/* 602:    */             {
/* 603:560 */               message = "We've received your request. Please be patient while we process it.";
/* 604:561 */               trans.setIsBilled(0);
/* 605:    */             }
/* 606:562 */             else if (e.getMessage().equals("INSUFFICIENT_CREDIT"))
/* 607:    */             {
/* 608:563 */               message = "We couldn't complete your purchase. Please top up and then try again";
/* 609:564 */               trans.setIsBilled(0);
/* 610:    */             }
/* 611:565 */             else if (e.getMessage().equals("ERROR"))
/* 612:    */             {
/* 613:566 */               message = "We couldn't complete your purchase. Please try again. You've not been billed.";
/* 614:567 */               trans.setIsBilled(0);
/* 615:    */             }
/* 616:    */           }
/* 617:571 */           if ((billed == true) && ((pricePoint.getBillingMech().equals("3")) || (pricePoint.getBillingMech().equals("2"))))
/* 618:    */           {
/* 619:573 */             trans.setIsBilled(1);
/* 620:574 */             ServiceManager.createTransaction(trans);
/* 621:    */             
/* 622:576 */             System.out.println("billing successful. Initiating download....");
/* 623:    */             
/* 624:578 */             RequestDispatcher dispatch = null;
/* 625:579 */             request.setAttribute("ticketId", ticket);
/* 626:    */             try
/* 627:    */             {
/* 628:581 */               dispatch = request.getRequestDispatcher("initiatedownload");
/* 629:    */             }
/* 630:    */             catch (Exception e)
/* 631:    */             {
/* 632:583 */               throw new Exception("8000");
/* 633:    */             }
/* 634:586 */             dispatch.include(request, response);
/* 635:    */           }
/* 636:    */           else
/* 637:    */           {
/* 638:588 */             ServiceManager.createTransaction(trans);
/* 639:    */           }
/* 640:    */         }
/* 641:    */         else
/* 642:    */         {
/* 643:639 */           System.out.println("Item is free...");
/* 644:640 */           billed = true;
/* 645:    */           
/* 646:    */ 
/* 647:    */ 
/* 648:644 */           trans.setIsBilled(1);
/* 649:645 */           ServiceManager.createTransaction(trans);
/* 650:    */           
/* 651:647 */           RequestDispatcher dispatch = null;
/* 652:    */           try
/* 653:    */           {
/* 654:650 */             if (siteType.equals("1")) {
/* 655:651 */               request.setAttribute("siteType", "WAP");
/* 656:    */             }
/* 657:    */           }
/* 658:    */           catch (Exception ex)
/* 659:    */           {
/* 660:654 */             System.out.println(new Date() + ":WARNING unable to determine site type will default to using WAP push to deliver message ");
/* 661:    */           }
/* 662:657 */           request.setAttribute("ticketId", ticket);
/* 663:    */           try
/* 664:    */           {
/* 665:660 */             dispatch = request.getRequestDispatcher("initiatedownload");
/* 666:    */           }
/* 667:    */           catch (Exception e)
/* 668:    */           {
/* 669:662 */             throw new Exception("8000");
/* 670:    */           }
/* 671:665 */           dispatch.include(request, response);
/* 672:    */         }
/* 673:669 */         if (!billed)
/* 674:    */         {
/* 675:670 */           System.out.println(new Date() + ": billing was NOT successful");
/* 676:671 */           throw new Exception(message);
/* 677:    */         }
/* 678:    */       }
/* 679:    */       else
/* 680:    */       {
/* 681:674 */         throw new Exception("6001");
/* 682:    */       }
/* 683:677 */       String insertions = "ack=" + ack + "&keyword=" + keyword + "&msg=" + transaction + "&msisdn=" + msisdn + "&itemName=" + itemName + "&transId=" + download.getTicketID();
/* 684:678 */       ack = URLUTF8Encoder.doMessageEscaping(insertions, ack);
/* 685:    */       try
/* 686:    */       {
/* 687:681 */         if (siteType.equals("2")) {
/* 688:682 */           message = feedback.getUserFriendlyDescription("3002") + " " + ack;
/* 689:    */         } else {
/* 690:684 */           message = feedback.getType("3002") + ":" + download.getTicketID();
/* 691:    */         }
/* 692:    */       }
/* 693:    */       catch (Exception ex)
/* 694:    */       {
/* 695:687 */         message = ack;
/* 696:    */       }
/* 697:689 */       if ((responsePath != null) && (!responsePath.equals("")) && (!responsePath.equalsIgnoreCase("null"))) {
/* 698:690 */         response.sendRedirect(responsePath + "?reply=" + message);
/* 699:    */       } else {
/* 700:692 */         out.println(message);
/* 701:    */       }
/* 702:    */     }
/* 703:    */     catch (Exception e)
/* 704:    */     {
/* 705:    */       try
/* 706:    */       {
/* 707:704 */         if (siteType.equals("2")) {
/* 708:705 */           message = feedback.getUserFriendlyDescription(e.getMessage());
/* 709:    */         } else {
/* 710:707 */           message = message;
/* 711:    */         }
/* 712:709 */         if (message == null) {
/* 713:710 */           message = e.getMessage();
/* 714:    */         }
/* 715:    */       }
/* 716:    */       catch (Exception ex)
/* 717:    */       {
/* 718:713 */         message = ex.getMessage();
/* 719:    */       }
/* 720:715 */       if ((responsePath != null) && (!responsePath.equals("")) && (!responsePath.equalsIgnoreCase("null"))) {
/* 721:716 */         response.sendRedirect(responsePath + "?reply=" + message);
/* 722:    */       } else {
/* 723:718 */         out.println(message);
/* 724:    */       }
/* 725:    */     }
/* 726:    */     finally
/* 727:    */     {
/* 728:723 */       cnxn = null;
/* 729:724 */       download = null;
/* 730:725 */       format = null;
/* 731:726 */       initSite = null;
/* 732:727 */       item = null;
/* 733:728 */       out = null;
/* 734:729 */       user = null;
/* 735:    */     }
/* 736:    */   }
/* 737:    */   
/* 738:    */   public void doPost(HttpServletRequest request, HttpServletResponse response)
/* 739:    */     throws ServletException, IOException
/* 740:    */   {
/* 741:738 */     doGet(request, response);
/* 742:    */   }
/* 743:    */   
/* 744:    */   public void destroy() {}
/* 745:    */   
/* 746:    */   private void initiateDownload(String baseUrl, String ticket)
/* 747:    */     throws Exception
/* 748:    */   {
/* 749:746 */     BufferedReader br = null;
/* 750:747 */     URL url = null;
/* 751:    */     try
/* 752:    */     {
/* 753:750 */       url = new URL(baseUrl + "initiatedownload?ticketId=" + ticket);
/* 754:    */       
/* 755:752 */       System.out.println(" Initiate download URL: " + url);
/* 756:    */       
/* 757:754 */       br = new BufferedReader(new InputStreamReader(url.openStream()));
/* 758:    */       
/* 759:756 */       String temp = br.readLine();
/* 760:757 */       String resp = new String();
/* 761:759 */       while (temp != null)
/* 762:    */       {
/* 763:760 */         resp = resp + temp;
/* 764:761 */         temp = br.readLine();
/* 765:    */       }
/* 766:764 */       br.close();
/* 767:    */     }
/* 768:    */     catch (IOException ex2)
/* 769:    */     {
/* 770:766 */       System.out.println("Error: " + ex2.getMessage());
/* 771:767 */       throw new Exception("8000");
/* 772:    */     }
/* 773:    */     finally
/* 774:    */     {
/* 775:769 */       url = null;
/* 776:770 */       br = null;
/* 777:    */     }
/* 778:    */   }
/* 779:    */   
/* 780:    */   public String log(int idLength, Transaction dlb, String formatID, String keyword)
/* 781:    */   {
/* 782:776 */     String ticket = null;
/* 783:777 */     dlb.setTicketID(uidGen.getUId() + "-" + formatID);
/* 784:    */     try
/* 785:    */     {
/* 786:780 */       Transaction.logTransaction(dlb.getTicketID(), dlb.getID(), dlb.getListID(), dlb.getSubscriberMSISDN(), dlb.getPhoneId(), false, dlb.getPin(), dlb.getSiteId(), keyword);
/* 787:781 */       ticket = dlb.getTicketID();
/* 788:    */     }
/* 789:    */     catch (Exception e)
/* 790:    */     {
/* 791:783 */       e.printStackTrace();
/* 792:    */     }
/* 793:785 */     return ticket;
/* 794:    */   }
/* 795:    */   
/* 796:    */   public void forward(ServletRequest request, ServletResponse response)
/* 797:    */     throws ServletException, IOException
/* 798:    */   {
/* 799:790 */     HttpServletRequest req = (HttpServletRequest)request;
/* 800:791 */     HttpServletResponse resp = (HttpServletResponse)response;
/* 801:792 */     doGet(req, resp);
/* 802:    */   }
/* 803:    */   
/* 804:    */   public void include(ServletRequest request, ServletResponse response)
/* 805:    */     throws ServletException, IOException
/* 806:    */   {
/* 807:797 */     HttpServletRequest req = (HttpServletRequest)request;
/* 808:798 */     HttpServletResponse resp = (HttpServletResponse)response;
/* 809:799 */     doGet(req, resp);
/* 810:    */   }
/* 811:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.serviceinterfaces.processdownloadrequest
 * JD-Core Version:    0.7.0.1
 */