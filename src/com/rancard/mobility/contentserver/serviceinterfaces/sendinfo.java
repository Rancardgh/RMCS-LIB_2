/*   1:    */ package com.rancard.mobility.contentserver.serviceinterfaces;
/*   2:    */ 
/*   3:    */ import com.rancard.common.Feedback;
/*   4:    */ import com.rancard.common.uidGen;
/*   5:    */ import com.rancard.mobility.contentserver.CPConnections;
/*   6:    */ import com.rancard.mobility.infoserver.InfoService;
/*   7:    */ import com.rancard.mobility.infoserver.common.services.ServiceManager;
/*   8:    */ import com.rancard.mobility.infoserver.common.services.UserServiceTransaction;
/*   9:    */ import com.rancard.util.URLUTF8Encoder;
/*  10:    */ import com.rancard.util.payment.PaymentManager;
/*  11:    */ import com.rancard.util.payment.PricePoint;
/*  12:    */ import java.io.IOException;
/*  13:    */ import java.io.PrintStream;
/*  14:    */ import java.io.PrintWriter;
/*  15:    */ import java.net.URLEncoder;
/*  16:    */ import java.sql.Timestamp;
/*  17:    */ import java.util.Calendar;
/*  18:    */ import javax.servlet.RequestDispatcher;
/*  19:    */ import javax.servlet.ServletContext;
/*  20:    */ import javax.servlet.ServletException;
/*  21:    */ import javax.servlet.ServletRequest;
/*  22:    */ import javax.servlet.ServletResponse;
/*  23:    */ import javax.servlet.http.HttpServlet;
/*  24:    */ import javax.servlet.http.HttpServletRequest;
/*  25:    */ import javax.servlet.http.HttpServletResponse;
/*  26:    */ import org.apache.commons.lang.StringUtils;
/*  27:    */ 
/*  28:    */ public class sendinfo
/*  29:    */   extends HttpServlet
/*  30:    */   implements RequestDispatcher
/*  31:    */ {
/*  32:    */   public void init()
/*  33:    */     throws ServletException
/*  34:    */   {}
/*  35:    */   
/*  36:    */   public void doGet(HttpServletRequest request, HttpServletResponse response)
/*  37:    */     throws ServletException, IOException
/*  38:    */   {
/*  39:    */     try
/*  40:    */     {
/*  41: 45 */       request.setCharacterEncoding("UTF-8");
/*  42:    */     }
/*  43:    */     catch (Exception e) {}
/*  44: 48 */     String fullContextPath = "http://192.168.1.243:81" + request.getContextPath();
/*  45: 49 */     boolean skipMessagingFromCallBack = false;
/*  46:    */     
/*  47:    */ 
/*  48: 52 */     PrintWriter out = response.getWriter();
/*  49: 53 */     CPConnections cnxn = new CPConnections();
/*  50:    */     
/*  51: 55 */     String provId = (String)request.getAttribute("acctId");
/*  52: 56 */     provId = provId == null ? request.getParameter("acctId") : provId;
/*  53:    */     
/*  54:    */ 
/*  55: 59 */     String siteType = (String)request.getAttribute("site_type");
/*  56: 60 */     siteType = siteType == null ? request.getParameter("site_type") : siteType;
/*  57:    */     
/*  58: 62 */     String lang = (String)request.getAttribute("default_lang");
/*  59: 63 */     String override_msg = (String)request.getAttribute("override_msg");
/*  60: 64 */     String override_keyword = (String)request.getAttribute("override_keyword");
/*  61: 65 */     lang = lang == null ? request.getParameter("default_lang") : lang;
/*  62: 66 */     String shortcode = request.getParameter("dest") == null ? "" : request.getParameter("dest");
/*  63: 67 */     String siteId = request.getParameter("siteId") == null ? "" : request.getParameter("siteId");
/*  64:    */     
/*  65: 69 */     String msisdn = request.getParameter("msisdn");
/*  66:    */     
/*  67:    */ 
/*  68: 72 */     String kw = request.getParameter("keyword");
/*  69: 73 */     String alreadyBilled = request.getParameter("pre_billed");
/*  70:    */     
/*  71:    */ 
/*  72: 76 */     System.out.println(new java.util.Date() + ":@com.rancard.mobility.contentserver.serviceinterfaces.sendinfo..");
/*  73: 78 */     if (kw == null) {
/*  74: 79 */       kw = (String)request.getAttribute("keyword");
/*  75:    */     }
/*  76: 81 */     if (alreadyBilled == null) {
/*  77: 82 */       alreadyBilled = (String)request.getAttribute("pre_billed");
/*  78:    */     }
/*  79: 86 */     if ((override_keyword != null) && (!override_keyword.equals(""))) {
/*  80: 89 */       kw = override_keyword;
/*  81:    */     }
/*  82: 93 */     if ((lang == null) || (lang.equals(""))) {
/*  83: 94 */       lang = "en";
/*  84:    */     }
/*  85: 96 */     Feedback feedback = (Feedback)getServletContext().getAttribute("feedback_" + lang);
/*  86: 97 */     if (feedback == null) {
/*  87:    */       try
/*  88:    */       {
/*  89: 99 */         feedback = new Feedback();
/*  90:    */       }
/*  91:    */       catch (Exception e) {}
/*  92:    */     }
/*  93:108 */     String message = "";
/*  94:109 */     if ((provId == null) || (provId.equals("")))
/*  95:    */     {
/*  96:    */       try
/*  97:    */       {
/*  98:111 */         if (siteType.equals("2")) {
/*  99:112 */           message = feedback.getUserFriendlyDescription("10004");
/* 100:    */         } else {
/* 101:114 */           message = feedback.formDefaultMessage("10004");
/* 102:    */         }
/* 103:117 */         System.out.println(new java.util.Date() + ":Feedback.MISSING_INVALID_PROV_ID: " + message);
/* 104:    */       }
/* 105:    */       catch (Exception ex)
/* 106:    */       {
/* 107:120 */         message = ex.getMessage();
/* 108:    */       }
/* 109:122 */       boolean isAsciiPrintable = StringUtils.isAsciiPrintable(message);
/* 110:123 */       if (!isAsciiPrintable)
/* 111:    */       {
/* 112:124 */         System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
/* 113:125 */         request.setAttribute("X-Kannel-Coding", "2");
/* 114:126 */         if (request.getAttribute("X-Kannel-Coding") != null) {
/* 115:127 */           System.out.println("Request contains X-Kannel-Coding attribute");
/* 116:    */         }
/* 117:    */       }
/* 118:130 */       out.println(message);
/* 119:131 */       return;
/* 120:    */     }
/* 121:135 */     if ((kw == null) || (kw.equals("")))
/* 122:    */     {
/* 123:    */       try
/* 124:    */       {
/* 125:137 */         if (siteType.equals("2")) {
/* 126:138 */           message = feedback.getUserFriendlyDescription("10001");
/* 127:    */         } else {
/* 128:140 */           message = feedback.formDefaultMessage("10001");
/* 129:    */         }
/* 130:143 */         System.out.println(new java.util.Date() + ":NO_SUCH_SERVICE (keyword empty): " + message);
/* 131:    */       }
/* 132:    */       catch (Exception ex)
/* 133:    */       {
/* 134:146 */         message = ex.getMessage();
/* 135:    */         
/* 136:148 */         System.out.println(new java.util.Date() + ":error: " + message);
/* 137:    */       }
/* 138:151 */       boolean isAsciiPrintable = StringUtils.isAsciiPrintable(message);
/* 139:152 */       if (!isAsciiPrintable)
/* 140:    */       {
/* 141:153 */         System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
/* 142:154 */         request.setAttribute("X-Kannel-Coding", "2");
/* 143:155 */         if (request.getAttribute("X-Kannel-Coding") != null) {
/* 144:156 */           System.out.println("Request contains X-Kannel-Coding attribute");
/* 145:    */         }
/* 146:    */       }
/* 147:158 */       out.println(message);
/* 148:159 */       return;
/* 149:    */     }
/* 150:163 */     System.out.println(new java.util.Date() + ":Info to be sent for keyword " + kw.toUpperCase() + " on account with ID " + provId);
/* 151:    */     
/* 152:    */ 
/* 153:    */ 
/* 154:167 */     InfoService is = new InfoService();
/* 155:    */     
/* 156:    */ 
/* 157:170 */     Calendar calendar = Calendar.getInstance();
/* 158:171 */     java.util.Date today = new java.util.Date(calendar.getTime().getTime());
/* 159:172 */     String dateString = com.rancard.util.Date.formatDate(today, "dd-MM-yyyy");
/* 160:173 */     is.setAccountId(provId);
/* 161:174 */     is.setKeyword(kw);
/* 162:175 */     is.setPublishDate(dateString);
/* 163:    */     
/* 164:177 */     calendar = null;
/* 165:178 */     today = null;
/* 166:    */     try
/* 167:    */     {
/* 168:182 */       is.viewInfoService();
/* 169:    */     }
/* 170:    */     catch (Exception e)
/* 171:    */     {
/* 172:186 */       System.out.println(new java.util.Date() + ":error: " + e.getMessage());
/* 173:    */       try
/* 174:    */       {
/* 175:189 */         if (siteType.equals("2")) {
/* 176:190 */           message = feedback.getUserFriendlyDescription("10001");
/* 177:    */         } else {
/* 178:192 */           message = feedback.formDefaultMessage("10001");
/* 179:    */         }
/* 180:195 */         System.out.println(new java.util.Date() + ":NO_SUCH_SERVICE: " + message);
/* 181:    */       }
/* 182:    */       catch (Exception ex)
/* 183:    */       {
/* 184:197 */         message = ex.getMessage();
/* 185:    */         
/* 186:199 */         System.out.println(new java.util.Date() + ":error: " + message);
/* 187:    */       }
/* 188:202 */       boolean isAsciiPrintable = StringUtils.isAsciiPrintable(message);
/* 189:203 */       if (!isAsciiPrintable)
/* 190:    */       {
/* 191:204 */         System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
/* 192:205 */         request.setAttribute("X-Kannel-Coding", "2");
/* 193:206 */         if (request.getAttribute("X-Kannel-Coding") != null) {
/* 194:207 */           System.out.println("Request contains X-Kannel-Coding attribute");
/* 195:    */         }
/* 196:    */       }
/* 197:209 */       out.println(message);
/* 198:210 */       return;
/* 199:    */     }
/* 200:214 */     String info = "";
/* 201:215 */     String compactInfo = "";
/* 202:216 */     if ((is.getMessage() == null) || (is.getMessage().equals(""))) {
/* 203:217 */       info = is.getDefaultMessage();
/* 204:    */     } else {
/* 205:219 */       info = is.getMessage();
/* 206:    */     }
/* 207:222 */     if ((msisdn != null) && (!msisdn.equals("")))
/* 208:    */     {
/* 209:224 */       String number = msisdn;
/* 210:225 */       if (number.indexOf("+") != -1)
/* 211:    */       {
/* 212:226 */         StringBuffer sb = new StringBuffer(number);
/* 213:227 */         sb.deleteCharAt(number.indexOf("+"));
/* 214:228 */         number = sb.toString();
/* 215:    */         
/* 216:230 */         sb = null;
/* 217:    */       }
/* 218:232 */       number = number.trim();
/* 219:    */       try
/* 220:    */       {
/* 221:236 */         Long.parseLong(number);
/* 222:237 */         msisdn = "+" + number;
/* 223:    */       }
/* 224:    */       catch (NumberFormatException e)
/* 225:    */       {
/* 226:240 */         System.out.println(new java.util.Date() + ":error parsing msisdn: " + e.getMessage());
/* 227:    */         try
/* 228:    */         {
/* 229:242 */           if (siteType.equals("2")) {
/* 230:243 */             message = feedback.getUserFriendlyDescription("2000");
/* 231:    */           } else {
/* 232:245 */             message = feedback.formDefaultMessage("2000");
/* 233:    */           }
/* 234:248 */           System.out.println(new java.util.Date() + ":MISSING_INVALID_MSISDN: " + message);
/* 235:    */         }
/* 236:    */         catch (Exception ex)
/* 237:    */         {
/* 238:250 */           message = ex.getMessage();
/* 239:    */           
/* 240:252 */           System.out.println(new java.util.Date() + ":error: " + message);
/* 241:    */         }
/* 242:255 */         boolean isAsciiPrintable = StringUtils.isAsciiPrintable(message);
/* 243:256 */         if (!isAsciiPrintable)
/* 244:    */         {
/* 245:257 */           System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
/* 246:258 */           request.setAttribute("X-Kannel-Coding", "2");
/* 247:259 */           if (request.getAttribute("X-Kannel-Coding") != null) {
/* 248:260 */             System.out.println("Request contains X-Kannel-Coding attribute");
/* 249:    */           }
/* 250:    */         }
/* 251:263 */         out.println(message);
/* 252:264 */         return;
/* 253:    */       }
/* 254:    */       try
/* 255:    */       {
/* 256:269 */         cnxn = CPConnections.getConnection(provId, msisdn);
/* 257:    */       }
/* 258:    */       catch (Exception e)
/* 259:    */       {
/* 260:272 */         System.out.println(new java.util.Date() + ":error: " + e.getMessage());
/* 261:    */         try
/* 262:    */         {
/* 263:275 */           if (siteType.equals("2")) {
/* 264:276 */             message = feedback.getUserFriendlyDescription("8002");
/* 265:    */           } else {
/* 266:278 */             message = feedback.formDefaultMessage("8002");
/* 267:    */           }
/* 268:281 */           System.out.println(new java.util.Date() + ":UNSUPPORTED_NETWORK:" + message);
/* 269:    */         }
/* 270:    */         catch (Exception ex)
/* 271:    */         {
/* 272:283 */           message = ex.getMessage();
/* 273:    */           
/* 274:285 */           System.out.println(new java.util.Date() + ":" + message);
/* 275:    */         }
/* 276:289 */         boolean isAsciiPrintable = StringUtils.isAsciiPrintable(message);
/* 277:290 */         if (!isAsciiPrintable)
/* 278:    */         {
/* 279:291 */           System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
/* 280:292 */           request.setAttribute("X-Kannel-Coding", "2");
/* 281:293 */           if (request.getAttribute("X-Kannel-Coding") != null) {
/* 282:294 */             System.out.println("Request contains X-Kannel-Coding attribute");
/* 283:    */           }
/* 284:    */         }
/* 285:296 */         out.println(message);
/* 286:297 */         return;
/* 287:    */       }
/* 288:300 */       System.out.println(new java.util.Date() + ":" + msisdn + ":Checking if billing required for content...");
/* 289:    */       
/* 290:302 */       boolean billed = false;
/* 291:303 */       String pricePointString = is.getPricing();
/* 292:304 */       if ((pricePointString == null) || ("".equals(pricePointString)))
/* 293:    */       {
/* 294:305 */         pricePointString = "";
/* 295:306 */         billed = true;
/* 296:    */         
/* 297:308 */         System.out.println(new java.util.Date() + ":" + msisdn + ":No pricePoint found on content. Billing not required!");
/* 298:    */       }
/* 299:312 */       if ((info == null) || ("".equals(info)))
/* 300:    */       {
/* 301:313 */         billed = true;
/* 302:    */         
/* 303:315 */         info = "No info is currently available for " + is.getServiceName() + ". Please try again later.";
/* 304:    */         
/* 305:317 */         System.out.println(new java.util.Date() + ": " + msisdn + ":No info currently available for:" + is.getServiceName() + "(" + is.getKeyword() + ")");
/* 306:318 */         if ((pricePointString != null) && (!"".equals(pricePointString)))
/* 307:    */         {
/* 308:319 */           pricePointString = "";
/* 309:320 */           System.out.println(new java.util.Date() + ": " + msisdn + ":Do not bill subscriber:" + msisdn);
/* 310:    */         }
/* 311:    */       }
/* 312:325 */       if ((alreadyBilled != null) && ("1".equals(alreadyBilled)))
/* 313:    */       {
/* 314:326 */         billed = true;
/* 315:327 */         pricePointString = "";
/* 316:328 */         System.out.println(new java.util.Date() + ": " + msisdn + ":Pre-billed request: Content already paid for. deliver content!");
/* 317:    */       }
/* 318:333 */       if (!pricePointString.equals("")) {
/* 319:    */         try
/* 320:    */         {
/* 321:335 */           System.out.println(new java.util.Date() + ": " + msisdn + ":About to bill...");
/* 322:    */           
/* 323:337 */           String[] itemPricePoints = pricePointString.split(",");
/* 324:339 */           if ((itemPricePoints == null) || (itemPricePoints[0] == null) || (itemPricePoints[0].equals("")))
/* 325:    */           {
/* 326:340 */             System.out.println(new java.util.Date() + ": " + msisdn + ":ERROR:BILLING_MECH_FAILURE:PricePoint Missing!");
/* 327:341 */             throw new Exception("11000");
/* 328:    */           }
/* 329:344 */           PricePoint pricePoint = PaymentManager.viewPricePointFor(itemPricePoints, msisdn);
/* 330:345 */           String pricePointId = pricePoint.getPricePointId();
/* 331:346 */           System.out.println(new java.util.Date() + ":pricePoint ID:" + pricePointId);
/* 332:349 */           if ((pricePointId == null) || ("".equals(pricePointId)))
/* 333:    */           {
/* 334:350 */             System.out.println(new java.util.Date() + ": " + msisdn + ":ERROR: Invalid pricePointID(s):" + pricePointString);
/* 335:351 */             billed = true;
/* 336:    */             
/* 337:353 */             throw new Exception("11000");
/* 338:    */           }
/* 339:363 */           System.out.println(new java.util.Date() + ": " + kw + ": " + msisdn + ": Entering initiatePayment from sendinfo");
/* 340:    */           
/* 341:365 */           String transactionId = "";
/* 342:366 */           if (pricePoint.getBillingMech().equals("4")) {
/* 343:367 */             transactionId = uidGen.genUID("", 5);
/* 344:369 */           } else if ((siteType.equals("1")) || (siteType.equals("0"))) {
/* 345:370 */             transactionId = uidGen.genUID("MP-", 5);
/* 346:    */           } else {
/* 347:372 */             transactionId = uidGen.genUID("OD-", 5);
/* 348:    */           }
/* 349:378 */           String completeTransnxnUrl = fullContextPath + "/sendinfo_push.jsp?msisdn=" + URLEncoder.encode(msisdn, "UTF-8") + "&keyword=" + URLEncoder.encode(kw.toUpperCase(), "UTF-8") + "&alert_count=" + is.getMsgId() + "&dest=" + URLEncoder.encode(shortcode, "UTF-8") + "&siteId=" + URLEncoder.encode(siteId, "UTF-8") + "&transId=" + URLEncoder.encode(transactionId, "UTF-8");
/* 350:382 */           if (pricePoint.getBillingMech().equals("3")) {
/* 351:383 */             completeTransnxnUrl = completeTransnxnUrl + "&sender=KEYWORD";
/* 352:    */           }
/* 353:386 */           UserServiceTransaction trans = new UserServiceTransaction();
/* 354:387 */           trans.setAccountId(provId);
/* 355:    */           
/* 356:389 */           trans.setDate(new Timestamp(new java.util.Date().getTime()));
/* 357:390 */           trans.setKeyword(kw.toUpperCase());
/* 358:391 */           trans.setMsg("on-demand");
/* 359:392 */           trans.setMsisdn(msisdn);
/* 360:393 */           trans.setPricePoint(pricePointId);
/* 361:394 */           trans.setTransactionId(transactionId);
/* 362:395 */           trans.setIsBilled(0);
/* 363:396 */           trans.setIsCompleted(0);
/* 364:397 */           trans.setCallBackUrl(completeTransnxnUrl);
/* 365:    */           
/* 366:399 */           boolean transactionCreated = false;
/* 367:400 */           int isCompleted = 0;
/* 368:    */           try
/* 369:    */           {
/* 370:403 */             ServiceManager.createTransaction(trans);
/* 371:404 */             System.out.println(new java.util.Date() + ":sendinfo: Transaction " + trans.getTransactionId() + " created!");
/* 372:405 */             transactionCreated = true;
/* 373:    */           }
/* 374:    */           catch (Exception e)
/* 375:    */           {
/* 376:407 */             transactionCreated = false;
/* 377:408 */             System.out.println(new java.util.Date() + ":sendinfo: Transaction " + trans.getTransactionId() + " NOT created! Error message: " + e.getMessage());
/* 378:    */           }
/* 379:412 */           if (transactionCreated)
/* 380:    */           {
/* 381:    */             try
/* 382:    */             {
/* 383:414 */               billed = PaymentManager.initiatePayment(pricePoint, cnxn, msisdn, transactionId, "", completeTransnxnUrl, shortcode, trans.getKeyword());
/* 384:    */             }
/* 385:    */             catch (Exception e)
/* 386:    */             {
/* 387:416 */               if (e.getMessage().equals("READ_TIMEOUT"))
/* 388:    */               {
/* 389:417 */                 message = "We've received your request for a " + is.getServiceName() + " item. Please be patient while we process it.";
/* 390:418 */                 skipMessagingFromCallBack = false;
/* 391:419 */                 isCompleted = 0;
/* 392:    */               }
/* 393:420 */               else if (e.getMessage().equals("INSUFFICIENT_CREDIT"))
/* 394:    */               {
/* 395:421 */                 message = "We couldn't complete purchase of the " + is.getServiceName() + " item you requested. Please top up and then send " + is.getKeyword().toUpperCase() + " to " + shortcode;
/* 396:    */                 
/* 397:423 */                 skipMessagingFromCallBack = true;
/* 398:424 */                 isCompleted = 1;
/* 399:    */               }
/* 400:425 */               else if (e.getMessage().equals("ERROR"))
/* 401:    */               {
/* 402:426 */                 message = "We couldn't complete purchase of the " + is.getServiceName() + " item you requested. Please try again. Send " + is.getKeyword().toUpperCase() + " to " + shortcode + ". You've not been billed.";
/* 403:    */                 
/* 404:428 */                 isCompleted = 1;
/* 405:429 */                 skipMessagingFromCallBack = true;
/* 406:    */               }
/* 407:    */             }
/* 408:439 */             request.setAttribute("x-kannel-header-binfo", transactionId);
/* 409:440 */             System.out.println(new java.util.Date() + ": " + kw + ": " + msisdn + ": Completed initiatePayment from sendinfo with result: " + billed);
/* 410:442 */             if (billed)
/* 411:    */             {
/* 412:443 */               trans.setIsBilled(1);
/* 413:444 */               trans.setIsCompleted(1);
/* 414:    */             }
/* 415:    */             else
/* 416:    */             {
/* 417:447 */               trans.setIsBilled(0);
/* 418:448 */               trans.setIsCompleted(isCompleted);
/* 419:    */             }
/* 420:    */             try
/* 421:    */             {
/* 422:453 */               ServiceManager.updateTransaction(trans.getTransactionId(), trans.getIsCompleted(), trans.getIsBilled());
/* 423:454 */               System.out.println(new java.util.Date() + ":sendinfo: Transaction " + trans.getTransactionId() + " updated!");
/* 424:    */             }
/* 425:    */             catch (Exception e)
/* 426:    */             {
/* 427:456 */               System.out.println(new java.util.Date() + ":sendinfo: Transaction " + trans.getTransactionId() + " NOT updated! Error message: " + e.getMessage());
/* 428:    */             }
/* 429:    */           }
/* 430:    */         }
/* 431:    */         catch (Exception e)
/* 432:    */         {
/* 433:    */           try
/* 434:    */           {
/* 435:462 */             if (siteType.equals("2")) {
/* 436:463 */               message = feedback.getUserFriendlyDescription(e.getMessage());
/* 437:    */             } else {
/* 438:465 */               message = feedback.formDefaultMessage(e.getMessage());
/* 439:    */             }
/* 440:    */           }
/* 441:    */           catch (Exception ex)
/* 442:    */           {
/* 443:468 */             message = ex.getMessage();
/* 444:    */           }
/* 445:471 */           boolean isAsciiPrintable = StringUtils.isAsciiPrintable(message);
/* 446:472 */           if (!isAsciiPrintable)
/* 447:    */           {
/* 448:473 */             System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
/* 449:474 */             request.setAttribute("X-Kannel-Coding", "2");
/* 450:475 */             if (request.getAttribute("X-Kannel-Coding") != null) {
/* 451:476 */               System.out.println("Request contains X-Kannel-Coding attribute");
/* 452:    */             }
/* 453:    */           }
/* 454:478 */           out.println(message);
/* 455:479 */           return;
/* 456:    */         }
/* 457:    */       }
/* 458:485 */       compactInfo = info.replaceAll("\r\n", ".");
/* 459:486 */       System.out.println(new java.util.Date() + ":Destination number: " + msisdn);
/* 460:487 */       System.out.println(new java.util.Date() + ":Info: " + compactInfo);
/* 461:    */       
/* 462:    */ 
/* 463:    */ 
/* 464:491 */       System.out.println(new java.util.Date() + ": " + kw + ": " + msisdn + ": About to send content. billed: " + billed);
/* 465:492 */       if (billed)
/* 466:    */       {
/* 467:493 */         if ((override_msg == null) || (override_msg.equals("")))
/* 468:    */         {
/* 469:494 */           boolean isAsciiPrintable = StringUtils.isAsciiPrintable(compactInfo);
/* 470:495 */           if (!isAsciiPrintable)
/* 471:    */           {
/* 472:496 */             System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
/* 473:497 */             request.setAttribute("X-Kannel-Coding", "2");
/* 474:498 */             if (request.getAttribute("X-Kannel-Coding") != null) {
/* 475:499 */               System.out.println("Request contains X-Kannel-Coding attribute");
/* 476:    */             }
/* 477:    */           }
/* 478:502 */           out.println(compactInfo);
/* 479:    */         }
/* 480:    */         else
/* 481:    */         {
/* 482:505 */           boolean isAsciiPrintable = StringUtils.isAsciiPrintable(override_msg);
/* 483:506 */           if (!isAsciiPrintable)
/* 484:    */           {
/* 485:507 */             System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
/* 486:508 */             request.setAttribute("X-Kannel-Coding", "2");
/* 487:509 */             if (request.getAttribute("X-Kannel-Coding") != null) {
/* 488:510 */               System.out.println("Request contains X-Kannel-Coding attribute");
/* 489:    */             }
/* 490:    */           }
/* 491:513 */           out.println(override_msg);
/* 492:    */         }
/* 493:    */       }
/* 494:    */       else
/* 495:    */       {
/* 496:518 */         boolean isAsciiPrintable = StringUtils.isAsciiPrintable(message);
/* 497:519 */         if (!isAsciiPrintable)
/* 498:    */         {
/* 499:520 */           System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
/* 500:521 */           request.setAttribute("X-Kannel-Coding", "2");
/* 501:522 */           if (request.getAttribute("X-Kannel-Coding") != null) {
/* 502:523 */             System.out.println("Request contains X-Kannel-Coding attribute");
/* 503:    */           }
/* 504:    */         }
/* 505:526 */         out.println(message);
/* 506:    */       }
/* 507:542 */       request.setAttribute("log_thirdPartyCPId", is.getOwnerId());
/* 508:    */       
/* 509:    */ 
/* 510:545 */       is = null;
/* 511:    */       
/* 512:547 */       return;
/* 513:    */     }
/* 514:    */     try
/* 515:    */     {
/* 516:550 */       if (siteType.equals("2")) {
/* 517:551 */         message = feedback.getUserFriendlyDescription("2000");
/* 518:    */       } else {
/* 519:553 */         message = feedback.formDefaultMessage("2000");
/* 520:    */       }
/* 521:556 */       System.out.println(new java.util.Date() + ":MISSING_INVALID_MSISDN: " + message);
/* 522:    */     }
/* 523:    */     catch (Exception ex)
/* 524:    */     {
/* 525:559 */       message = ex.getMessage();
/* 526:    */       
/* 527:561 */       System.out.println(new java.util.Date() + ":error: " + message);
/* 528:    */     }
/* 529:564 */     boolean isAsciiPrintable = StringUtils.isAsciiPrintable(message);
/* 530:565 */     if (!isAsciiPrintable)
/* 531:    */     {
/* 532:566 */       System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
/* 533:567 */       request.setAttribute("X-Kannel-Coding", "2");
/* 534:568 */       if (request.getAttribute("X-Kannel-Coding") != null) {
/* 535:569 */         System.out.println("Request contains X-Kannel-Coding attribute");
/* 536:    */       }
/* 537:    */     }
/* 538:571 */     out.println(message);
/* 539:    */   }
/* 540:    */   
/* 541:    */   public void doPost(HttpServletRequest request, HttpServletResponse response)
/* 542:    */     throws ServletException, IOException
/* 543:    */   {
/* 544:611 */     doGet(request, response);
/* 545:    */   }
/* 546:    */   
/* 547:    */   public void destroy() {}
/* 548:    */   
/* 549:    */   public void forward(ServletRequest request, ServletResponse response)
/* 550:    */     throws ServletException, IOException
/* 551:    */   {
/* 552:620 */     HttpServletRequest req = (HttpServletRequest)request;
/* 553:621 */     HttpServletResponse resp = (HttpServletResponse)response;
/* 554:622 */     doGet(req, resp);
/* 555:    */   }
/* 556:    */   
/* 557:    */   public void include(ServletRequest request, ServletResponse response)
/* 558:    */     throws ServletException, IOException
/* 559:    */   {
/* 560:627 */     HttpServletRequest req = (HttpServletRequest)request;
/* 561:628 */     HttpServletResponse resp = (HttpServletResponse)response;
/* 562:629 */     doGet(req, resp);
/* 563:    */   }
/* 564:    */   
/* 565:    */   public void updateOTTransaction(String kw, String provId, String alertCount, String msisdn, String pricePointId, int isBilled, int isCompleted, String transId)
/* 566:    */   {
/* 567:633 */     String svrAddr = "";
/* 568:634 */     String transactionId = transId;
/* 569:635 */     UserServiceTransaction trans = null;
/* 570:    */     
/* 571:    */ 
/* 572:    */ 
/* 573:639 */     trans = new UserServiceTransaction();
/* 574:640 */     svrAddr = "http://msg.rancardmobility.com:8080/ot.rms/sendsms?to=%2b2000&text=" + kw + "&conn=OT:5511&username=otsms&password=o1t1s1m1s1&serviceId=&price=0&from=" + URLUTF8Encoder.encode(msisdn);
/* 575:    */     
/* 576:642 */     trans.setAccountId(provId);
/* 577:643 */     trans.setKeyword(kw);
/* 578:644 */     trans.setMsg(alertCount);
/* 579:645 */     trans.setMsisdn(msisdn);
/* 580:646 */     trans.setTransactionId(transactionId);
/* 581:647 */     trans.setCallBackUrl(svrAddr);
/* 582:648 */     trans.setPricePoint(pricePointId);
/* 583:649 */     trans.setIsBilled(isBilled);
/* 584:650 */     trans.setIsCompleted(isCompleted);
/* 585:    */     
/* 586:652 */     System.out.println(new java.util.Date() + ":updating transaction for OT:transId=" + transactionId + ":msisdn=" + msisdn + ":keyword=" + kw + ":billed=" + isBilled + ":completed=" + isCompleted);
/* 587:    */     try
/* 588:    */     {
/* 589:654 */       ServiceManager.createTransaction(trans);
/* 590:    */     }
/* 591:    */     catch (Exception e)
/* 592:    */     {
/* 593:656 */       System.out.println(new java.util.Date() + ":sendinfo:error updating OTTransaction:" + e.getMessage());
/* 594:    */     }
/* 595:    */   }
/* 596:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.serviceinterfaces.sendinfo
 * JD-Core Version:    0.7.0.1
 */