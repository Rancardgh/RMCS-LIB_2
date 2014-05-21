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
/*  15:    */ import java.sql.Timestamp;
/*  16:    */ import java.util.Calendar;
/*  17:    */ import javax.servlet.RequestDispatcher;
/*  18:    */ import javax.servlet.ServletContext;
/*  19:    */ import javax.servlet.ServletException;
/*  20:    */ import javax.servlet.ServletRequest;
/*  21:    */ import javax.servlet.ServletResponse;
/*  22:    */ import javax.servlet.http.HttpServlet;
/*  23:    */ import javax.servlet.http.HttpServletRequest;
/*  24:    */ import javax.servlet.http.HttpServletResponse;
/*  25:    */ import org.apache.commons.lang.StringUtils;
/*  26:    */ 
/*  27:    */ public class sendinfo
/*  28:    */   extends HttpServlet
/*  29:    */   implements RequestDispatcher
/*  30:    */ {
/*  31:    */   public void init()
/*  32:    */     throws ServletException
/*  33:    */   {}
/*  34:    */   
/*  35:    */   public void doGet(HttpServletRequest request, HttpServletResponse response)
/*  36:    */     throws ServletException, IOException
/*  37:    */   {
/*  38:    */     try
/*  39:    */     {
/*  40: 42 */       request.setCharacterEncoding("UTF-8");
/*  41:    */     }
/*  42:    */     catch (Exception e) {}
/*  43: 45 */     String fullContextPath = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
/*  44: 46 */     boolean skipMessagingFromCallBack = false;
/*  45:    */     
/*  46:    */ 
/*  47: 49 */     PrintWriter out = response.getWriter();
/*  48: 50 */     CPConnections cnxn = new CPConnections();
/*  49:    */     
/*  50: 52 */     String provId = (String)request.getAttribute("acctId");
/*  51: 53 */     provId = provId == null ? request.getParameter("acctId") : provId;
/*  52:    */     
/*  53:    */ 
/*  54: 56 */     String siteType = (String)request.getAttribute("site_type");
/*  55: 57 */     siteType = siteType == null ? request.getParameter("site_type") : siteType;
/*  56:    */     
/*  57: 59 */     String lang = (String)request.getAttribute("default_lang");
/*  58: 60 */     String override_msg = (String)request.getAttribute("override_msg");
/*  59: 61 */     String override_keyword = (String)request.getAttribute("override_keyword");
/*  60: 62 */     lang = lang == null ? request.getParameter("default_lang") : lang;
/*  61: 63 */     String shortcode = request.getParameter("dest") == null ? "" : request.getParameter("dest");
/*  62: 64 */     String siteId = request.getParameter("siteId") == null ? "" : request.getParameter("siteId");
/*  63:    */     
/*  64: 66 */     String msisdn = request.getParameter("msisdn");
/*  65:    */     
/*  66:    */ 
/*  67: 69 */     String kw = request.getParameter("keyword");
/*  68: 70 */     String alreadyBilled = request.getParameter("pre_billed");
/*  69:    */     
/*  70:    */ 
/*  71: 73 */     System.out.println(new java.util.Date() + ":@com.rancard.mobility.contentserver.serviceinterfaces.sendinfo..");
/*  72: 75 */     if (kw == null) {
/*  73: 76 */       kw = (String)request.getAttribute("keyword");
/*  74:    */     }
/*  75: 78 */     if (alreadyBilled == null) {
/*  76: 79 */       alreadyBilled = (String)request.getAttribute("pre_billed");
/*  77:    */     }
/*  78: 83 */     if ((override_keyword != null) && (!override_keyword.equals(""))) {
/*  79: 86 */       kw = override_keyword;
/*  80:    */     }
/*  81: 90 */     if ((lang == null) || (lang.equals(""))) {
/*  82: 91 */       lang = "en";
/*  83:    */     }
/*  84: 93 */     Feedback feedback = (Feedback)getServletContext().getAttribute("feedback_" + lang);
/*  85: 94 */     if (feedback == null) {
/*  86:    */       try
/*  87:    */       {
/*  88: 96 */         feedback = new Feedback();
/*  89:    */       }
/*  90:    */       catch (Exception e) {}
/*  91:    */     }
/*  92:105 */     String message = "";
/*  93:106 */     if ((provId == null) || (provId.equals("")))
/*  94:    */     {
/*  95:    */       try
/*  96:    */       {
/*  97:108 */         if (siteType.equals("2")) {
/*  98:109 */           message = feedback.getUserFriendlyDescription("10004");
/*  99:    */         } else {
/* 100:111 */           message = feedback.formDefaultMessage("10004");
/* 101:    */         }
/* 102:114 */         System.out.println(new java.util.Date() + ":Feedback.MISSING_INVALID_PROV_ID: " + message);
/* 103:    */       }
/* 104:    */       catch (Exception ex)
/* 105:    */       {
/* 106:117 */         message = ex.getMessage();
/* 107:    */       }
/* 108:119 */       boolean isAsciiPrintable = StringUtils.isAsciiPrintable(message);
/* 109:120 */       if (!isAsciiPrintable)
/* 110:    */       {
/* 111:121 */         System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
/* 112:122 */         request.setAttribute("X-Kannel-Coding", "2");
/* 113:123 */         if (request.getAttribute("X-Kannel-Coding") != null) {
/* 114:124 */           System.out.println("Request contains X-Kannel-Coding attribute");
/* 115:    */         }
/* 116:    */       }
/* 117:126 */       out.println(message);
/* 118:127 */       return;
/* 119:    */     }
/* 120:131 */     if ((kw == null) || (kw.equals("")))
/* 121:    */     {
/* 122:    */       try
/* 123:    */       {
/* 124:133 */         if (siteType.equals("2")) {
/* 125:134 */           message = feedback.getUserFriendlyDescription("10001");
/* 126:    */         } else {
/* 127:136 */           message = feedback.formDefaultMessage("10001");
/* 128:    */         }
/* 129:139 */         System.out.println(new java.util.Date() + ":NO_SUCH_SERVICE (keyword empty): " + message);
/* 130:    */       }
/* 131:    */       catch (Exception ex)
/* 132:    */       {
/* 133:142 */         message = ex.getMessage();
/* 134:    */         
/* 135:144 */         System.out.println(new java.util.Date() + ":error: " + message);
/* 136:    */       }
/* 137:147 */       boolean isAsciiPrintable = StringUtils.isAsciiPrintable(message);
/* 138:148 */       if (!isAsciiPrintable)
/* 139:    */       {
/* 140:149 */         System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
/* 141:150 */         request.setAttribute("X-Kannel-Coding", "2");
/* 142:151 */         if (request.getAttribute("X-Kannel-Coding") != null) {
/* 143:152 */           System.out.println("Request contains X-Kannel-Coding attribute");
/* 144:    */         }
/* 145:    */       }
/* 146:154 */       out.println(message);
/* 147:155 */       return;
/* 148:    */     }
/* 149:159 */     System.out.println(new java.util.Date() + ":Info to be sent for keyword " + kw.toUpperCase() + " on account with ID " + provId);
/* 150:    */     
/* 151:    */ 
/* 152:    */ 
/* 153:163 */     InfoService is = new InfoService();
/* 154:    */     
/* 155:    */ 
/* 156:166 */     Calendar calendar = Calendar.getInstance();
/* 157:167 */     java.util.Date today = new java.util.Date(calendar.getTime().getTime());
/* 158:168 */     String dateString = com.rancard.util.Date.formatDate(today, "dd-MM-yyyy");
/* 159:169 */     is.setAccountId(provId);
/* 160:170 */     is.setKeyword(kw);
/* 161:171 */     is.setPublishDate(dateString);
/* 162:    */     
/* 163:173 */     calendar = null;
/* 164:174 */     today = null;
/* 165:    */     try
/* 166:    */     {
/* 167:178 */       is.viewInfoService();
/* 168:    */     }
/* 169:    */     catch (Exception e)
/* 170:    */     {
/* 171:182 */       System.out.println(new java.util.Date() + ":error: " + e.getMessage());
/* 172:    */       try
/* 173:    */       {
/* 174:185 */         if (siteType.equals("2")) {
/* 175:186 */           message = feedback.getUserFriendlyDescription("10001");
/* 176:    */         } else {
/* 177:188 */           message = feedback.formDefaultMessage("10001");
/* 178:    */         }
/* 179:191 */         System.out.println(new java.util.Date() + ":NO_SUCH_SERVICE: " + message);
/* 180:    */       }
/* 181:    */       catch (Exception ex)
/* 182:    */       {
/* 183:193 */         message = ex.getMessage();
/* 184:    */         
/* 185:195 */         System.out.println(new java.util.Date() + ":error: " + message);
/* 186:    */       }
/* 187:198 */       boolean isAsciiPrintable = StringUtils.isAsciiPrintable(message);
/* 188:199 */       if (!isAsciiPrintable)
/* 189:    */       {
/* 190:200 */         System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
/* 191:201 */         request.setAttribute("X-Kannel-Coding", "2");
/* 192:202 */         if (request.getAttribute("X-Kannel-Coding") != null) {
/* 193:203 */           System.out.println("Request contains X-Kannel-Coding attribute");
/* 194:    */         }
/* 195:    */       }
/* 196:205 */       out.println(message);
/* 197:206 */       return;
/* 198:    */     }
/* 199:210 */     String info = "";
/* 200:211 */     String compactInfo = "";
/* 201:212 */     if ((is.getMessage() == null) || (is.getMessage().equals(""))) {
/* 202:213 */       info = is.getDefaultMessage();
/* 203:    */     } else {
/* 204:215 */       info = is.getMessage();
/* 205:    */     }
/* 206:218 */     if ((msisdn != null) && (!msisdn.equals("")))
/* 207:    */     {
/* 208:220 */       String number = msisdn;
/* 209:221 */       if (number.indexOf("+") != -1)
/* 210:    */       {
/* 211:222 */         StringBuffer sb = new StringBuffer(number);
/* 212:223 */         sb.deleteCharAt(number.indexOf("+"));
/* 213:224 */         number = sb.toString();
/* 214:    */         
/* 215:226 */         sb = null;
/* 216:    */       }
/* 217:228 */       number = number.trim();
/* 218:    */       try
/* 219:    */       {
/* 220:232 */         Long.parseLong(number);
/* 221:233 */         msisdn = "+" + number;
/* 222:    */       }
/* 223:    */       catch (NumberFormatException e)
/* 224:    */       {
/* 225:236 */         System.out.println(new java.util.Date() + ":error parsing msisdn: " + e.getMessage());
/* 226:    */         try
/* 227:    */         {
/* 228:238 */           if (siteType.equals("2")) {
/* 229:239 */             message = feedback.getUserFriendlyDescription("2000");
/* 230:    */           } else {
/* 231:241 */             message = feedback.formDefaultMessage("2000");
/* 232:    */           }
/* 233:244 */           System.out.println(new java.util.Date() + ":MISSING_INVALID_MSISDN: " + message);
/* 234:    */         }
/* 235:    */         catch (Exception ex)
/* 236:    */         {
/* 237:246 */           message = ex.getMessage();
/* 238:    */           
/* 239:248 */           System.out.println(new java.util.Date() + ":error: " + message);
/* 240:    */         }
/* 241:251 */         boolean isAsciiPrintable = StringUtils.isAsciiPrintable(message);
/* 242:252 */         if (!isAsciiPrintable)
/* 243:    */         {
/* 244:253 */           System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
/* 245:254 */           request.setAttribute("X-Kannel-Coding", "2");
/* 246:255 */           if (request.getAttribute("X-Kannel-Coding") != null) {
/* 247:256 */             System.out.println("Request contains X-Kannel-Coding attribute");
/* 248:    */           }
/* 249:    */         }
/* 250:258 */         out.println(message);
/* 251:259 */         return;
/* 252:    */       }
/* 253:    */       try
/* 254:    */       {
/* 255:264 */         cnxn = CPConnections.getConnection(provId, msisdn);
/* 256:    */       }
/* 257:    */       catch (Exception e)
/* 258:    */       {
/* 259:267 */         System.out.println(new java.util.Date() + ":error: " + e.getMessage());
/* 260:    */         try
/* 261:    */         {
/* 262:270 */           if (siteType.equals("2")) {
/* 263:271 */             message = feedback.getUserFriendlyDescription("8002");
/* 264:    */           } else {
/* 265:273 */             message = feedback.formDefaultMessage("8002");
/* 266:    */           }
/* 267:276 */           System.out.println(new java.util.Date() + ":UNSUPPORTED_NETWORK:" + message);
/* 268:    */         }
/* 269:    */         catch (Exception ex)
/* 270:    */         {
/* 271:278 */           message = ex.getMessage();
/* 272:    */           
/* 273:280 */           System.out.println(new java.util.Date() + ":" + message);
/* 274:    */         }
/* 275:284 */         boolean isAsciiPrintable = StringUtils.isAsciiPrintable(message);
/* 276:285 */         if (!isAsciiPrintable)
/* 277:    */         {
/* 278:286 */           System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
/* 279:287 */           request.setAttribute("X-Kannel-Coding", "2");
/* 280:288 */           if (request.getAttribute("X-Kannel-Coding") != null) {
/* 281:289 */             System.out.println("Request contains X-Kannel-Coding attribute");
/* 282:    */           }
/* 283:    */         }
/* 284:291 */         out.println(message);
/* 285:292 */         return;
/* 286:    */       }
/* 287:295 */       System.out.println(new java.util.Date() + ":" + msisdn + ":Checking if billing required for content...");
/* 288:    */       
/* 289:297 */       boolean billed = false;
/* 290:298 */       String pricePointString = is.getPricing();
/* 291:299 */       if ((pricePointString == null) || ("".equals(pricePointString)))
/* 292:    */       {
/* 293:300 */         pricePointString = "";
/* 294:301 */         billed = true;
/* 295:    */         
/* 296:303 */         System.out.println(new java.util.Date() + ":" + msisdn + ":No pricePoint found on content. Billing not required!");
/* 297:    */       }
/* 298:307 */       if ((info == null) || ("".equals(info)))
/* 299:    */       {
/* 300:308 */         billed = true;
/* 301:    */         
/* 302:310 */         info = "No info is currently available for " + is.getServiceName() + ". Please try again later.";
/* 303:    */         
/* 304:312 */         System.out.println(new java.util.Date() + ": " + msisdn + ":No info currently available for:" + is.getServiceName() + "(" + is.getKeyword() + ")");
/* 305:313 */         if ((pricePointString != null) && (!"".equals(pricePointString)))
/* 306:    */         {
/* 307:314 */           pricePointString = "";
/* 308:315 */           System.out.println(new java.util.Date() + ": " + msisdn + ":Do not bill subscriber:" + msisdn);
/* 309:    */         }
/* 310:    */       }
/* 311:320 */       if ((alreadyBilled != null) && ("1".equals(alreadyBilled)))
/* 312:    */       {
/* 313:321 */         billed = true;
/* 314:322 */         pricePointString = "";
/* 315:323 */         System.out.println(new java.util.Date() + ": " + msisdn + ":Pre-billed request: Content already paid for. deliver content!");
/* 316:    */       }
/* 317:328 */       if (!pricePointString.equals("")) {
/* 318:    */         try
/* 319:    */         {
/* 320:330 */           System.out.println(new java.util.Date() + ": " + msisdn + ":About to bill...");
/* 321:    */           
/* 322:332 */           String[] itemPricePoints = pricePointString.split(",");
/* 323:334 */           if ((itemPricePoints == null) || (itemPricePoints[0] == null) || (itemPricePoints[0].equals("")))
/* 324:    */           {
/* 325:335 */             System.out.println(new java.util.Date() + ": " + msisdn + ":ERROR:BILLING_MECH_FAILURE:PricePoint Missing!");
/* 326:336 */             throw new Exception("11000");
/* 327:    */           }
/* 328:339 */           PricePoint pricePoint = PaymentManager.viewPricePointFor(itemPricePoints, msisdn);
/* 329:340 */           String pricePointId = pricePoint.getPricePointId();
/* 330:341 */           System.out.println(new java.util.Date() + ":pricePoint ID:" + pricePointId);
/* 331:344 */           if ((pricePointId == null) || ("".equals(pricePointId)))
/* 332:    */           {
/* 333:345 */             System.out.println(new java.util.Date() + ": " + msisdn + ":ERROR: Invalid pricePointID(s):" + pricePointString);
/* 334:346 */             billed = true;
/* 335:    */             
/* 336:348 */             throw new Exception("11000");
/* 337:    */           }
/* 338:358 */           System.out.println(new java.util.Date() + ": " + kw + ": " + msisdn + ": Entering initiatePayment from sendinfo");
/* 339:    */           
/* 340:360 */           String transactionId = "";
/* 341:361 */           if (pricePoint.getBillingMech().equals("4")) {
/* 342:362 */             transactionId = uidGen.genUID("", 5);
/* 343:364 */           } else if ((siteType.equals("1")) || (siteType.equals("0"))) {
/* 344:365 */             transactionId = uidGen.genUID("MP-", 5);
/* 345:    */           } else {
/* 346:367 */             transactionId = uidGen.genUID("OD-", 5);
/* 347:    */           }
/* 348:373 */           String completeTransnxnUrl = fullContextPath + "/sendinfo_push.jsp?msisdn=" + URLUTF8Encoder.encode(msisdn) + "&keyword=" + kw.toUpperCase() + "&alert_count=" + is.getMsgId() + "&dest=" + URLUTF8Encoder.encode(shortcode) + "&siteId=" + siteId + "&transId=" + transactionId;
/* 349:377 */           if (pricePoint.getBillingMech().equals("3")) {
/* 350:378 */             completeTransnxnUrl = completeTransnxnUrl + "&sender=KEYWORD";
/* 351:    */           }
/* 352:381 */           UserServiceTransaction trans = new UserServiceTransaction();
/* 353:382 */           trans.setAccountId(provId);
/* 354:    */           
/* 355:384 */           trans.setDate(new Timestamp(new java.util.Date().getTime()));
/* 356:385 */           trans.setKeyword(kw.toUpperCase());
/* 357:386 */           trans.setMsg("on-demand");
/* 358:387 */           trans.setMsisdn(msisdn);
/* 359:388 */           trans.setPricePoint(pricePointId);
/* 360:389 */           trans.setTransactionId(transactionId);
/* 361:390 */           trans.setIsBilled(0);
/* 362:391 */           trans.setIsCompleted(0);
/* 363:392 */           trans.setCallBackUrl(completeTransnxnUrl);
/* 364:    */           
/* 365:394 */           boolean transactionCreated = false;
/* 366:395 */           int isCompleted = 0;
/* 367:    */           try
/* 368:    */           {
/* 369:398 */             ServiceManager.createTransaction(trans);
/* 370:399 */             System.out.println(new java.util.Date() + ":sendinfo: Transaction " + trans.getTransactionId() + " created!");
/* 371:400 */             transactionCreated = true;
/* 372:    */           }
/* 373:    */           catch (Exception e)
/* 374:    */           {
/* 375:402 */             transactionCreated = false;
/* 376:403 */             System.out.println(new java.util.Date() + ":sendinfo: Transaction " + trans.getTransactionId() + " NOT created! Error message: " + e.getMessage());
/* 377:    */           }
/* 378:407 */           if (transactionCreated)
/* 379:    */           {
/* 380:    */             try
/* 381:    */             {
/* 382:409 */               billed = PaymentManager.initiatePayment(pricePoint, cnxn, msisdn, transactionId, "", completeTransnxnUrl);
/* 383:    */             }
/* 384:    */             catch (Exception e)
/* 385:    */             {
/* 386:411 */               if (e.getMessage().equals("READ_TIMEOUT"))
/* 387:    */               {
/* 388:412 */                 message = "We've received your request for a " + is.getServiceName() + " item. Please be patient while we process it.";
/* 389:413 */                 skipMessagingFromCallBack = false;
/* 390:414 */                 isCompleted = 0;
/* 391:    */               }
/* 392:415 */               else if (e.getMessage().equals("INSUFFICIENT_CREDIT"))
/* 393:    */               {
/* 394:416 */                 message = "We couldn't complete purchase of the " + is.getServiceName() + " item you requested. Please top up and then send " + is.getKeyword().toUpperCase() + " to " + shortcode;
/* 395:    */                 
/* 396:418 */                 skipMessagingFromCallBack = true;
/* 397:419 */                 isCompleted = 1;
/* 398:    */               }
/* 399:420 */               else if (e.getMessage().equals("ERROR"))
/* 400:    */               {
/* 401:421 */                 message = "We couldn't complete purchase of the " + is.getServiceName() + " item you requested. Please try again. Send " + is.getKeyword().toUpperCase() + " to " + shortcode + ". You've not been billed.";
/* 402:    */                 
/* 403:423 */                 isCompleted = 1;
/* 404:424 */                 skipMessagingFromCallBack = true;
/* 405:    */               }
/* 406:    */             }
/* 407:434 */             request.setAttribute("x-kannel-header-binfo", transactionId);
/* 408:435 */             System.out.println(new java.util.Date() + ": " + kw + ": " + msisdn + ": Completed initiatePayment from sendinfo with result: " + billed);
/* 409:437 */             if (billed)
/* 410:    */             {
/* 411:438 */               trans.setIsBilled(1);
/* 412:439 */               trans.setIsCompleted(1);
/* 413:    */             }
/* 414:    */             else
/* 415:    */             {
/* 416:442 */               trans.setIsBilled(0);
/* 417:443 */               trans.setIsCompleted(isCompleted);
/* 418:    */             }
/* 419:    */             try
/* 420:    */             {
/* 421:448 */               ServiceManager.updateTransaction(trans.getTransactionId(), trans.getIsCompleted(), trans.getIsBilled());
/* 422:449 */               System.out.println(new java.util.Date() + ":sendinfo: Transaction " + trans.getTransactionId() + " updated!");
/* 423:    */             }
/* 424:    */             catch (Exception e)
/* 425:    */             {
/* 426:451 */               System.out.println(new java.util.Date() + ":sendinfo: Transaction " + trans.getTransactionId() + " NOT updated! Error message: " + e.getMessage());
/* 427:    */             }
/* 428:    */           }
/* 429:    */         }
/* 430:    */         catch (Exception e)
/* 431:    */         {
/* 432:    */           try
/* 433:    */           {
/* 434:457 */             if (siteType.equals("2")) {
/* 435:458 */               message = feedback.getUserFriendlyDescription(e.getMessage());
/* 436:    */             } else {
/* 437:460 */               message = feedback.formDefaultMessage(e.getMessage());
/* 438:    */             }
/* 439:    */           }
/* 440:    */           catch (Exception ex)
/* 441:    */           {
/* 442:463 */             message = ex.getMessage();
/* 443:    */           }
/* 444:466 */           boolean isAsciiPrintable = StringUtils.isAsciiPrintable(message);
/* 445:467 */           if (!isAsciiPrintable)
/* 446:    */           {
/* 447:468 */             System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
/* 448:469 */             request.setAttribute("X-Kannel-Coding", "2");
/* 449:470 */             if (request.getAttribute("X-Kannel-Coding") != null) {
/* 450:471 */               System.out.println("Request contains X-Kannel-Coding attribute");
/* 451:    */             }
/* 452:    */           }
/* 453:473 */           out.println(message);
/* 454:474 */           return;
/* 455:    */         }
/* 456:    */       }
/* 457:480 */       compactInfo = info.replaceAll("\r\n", ".");
/* 458:481 */       System.out.println(new java.util.Date() + ":Destination number: " + msisdn);
/* 459:482 */       System.out.println(new java.util.Date() + ":Info: " + compactInfo);
/* 460:    */       
/* 461:    */ 
/* 462:    */ 
/* 463:486 */       System.out.println(new java.util.Date() + ": " + kw + ": " + msisdn + ": About to send content. billed: " + billed);
/* 464:487 */       if (billed)
/* 465:    */       {
/* 466:488 */         if ((override_msg == null) || (override_msg.equals("")))
/* 467:    */         {
/* 468:489 */           boolean isAsciiPrintable = StringUtils.isAsciiPrintable(info);
/* 469:490 */           if (!isAsciiPrintable)
/* 470:    */           {
/* 471:491 */             System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
/* 472:492 */             request.setAttribute("X-Kannel-Coding", "2");
/* 473:493 */             if (request.getAttribute("X-Kannel-Coding") != null) {
/* 474:494 */               System.out.println("Request contains X-Kannel-Coding attribute");
/* 475:    */             }
/* 476:    */           }
/* 477:496 */           out.println(info);
/* 478:    */         }
/* 479:    */         else
/* 480:    */         {
/* 481:499 */           boolean isAsciiPrintable = StringUtils.isAsciiPrintable(override_msg);
/* 482:500 */           if (!isAsciiPrintable)
/* 483:    */           {
/* 484:501 */             System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
/* 485:502 */             request.setAttribute("X-Kannel-Coding", "2");
/* 486:503 */             if (request.getAttribute("X-Kannel-Coding") != null) {
/* 487:504 */               System.out.println("Request contains X-Kannel-Coding attribute");
/* 488:    */             }
/* 489:    */           }
/* 490:506 */           out.println(override_msg);
/* 491:    */         }
/* 492:    */       }
/* 493:    */       else
/* 494:    */       {
/* 495:511 */         boolean isAsciiPrintable = StringUtils.isAsciiPrintable(message);
/* 496:512 */         if (!isAsciiPrintable)
/* 497:    */         {
/* 498:513 */           System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
/* 499:514 */           request.setAttribute("X-Kannel-Coding", "2");
/* 500:515 */           if (request.getAttribute("X-Kannel-Coding") != null) {
/* 501:516 */             System.out.println("Request contains X-Kannel-Coding attribute");
/* 502:    */           }
/* 503:    */         }
/* 504:518 */         out.println(message);
/* 505:    */       }
/* 506:534 */       request.setAttribute("log_thirdPartyCPId", is.getOwnerId());
/* 507:    */       
/* 508:    */ 
/* 509:537 */       is = null;
/* 510:    */       
/* 511:539 */       return;
/* 512:    */     }
/* 513:    */     try
/* 514:    */     {
/* 515:542 */       if (siteType.equals("2")) {
/* 516:543 */         message = feedback.getUserFriendlyDescription("2000");
/* 517:    */       } else {
/* 518:545 */         message = feedback.formDefaultMessage("2000");
/* 519:    */       }
/* 520:548 */       System.out.println(new java.util.Date() + ":MISSING_INVALID_MSISDN: " + message);
/* 521:    */     }
/* 522:    */     catch (Exception ex)
/* 523:    */     {
/* 524:551 */       message = ex.getMessage();
/* 525:    */       
/* 526:553 */       System.out.println(new java.util.Date() + ":error: " + message);
/* 527:    */     }
/* 528:556 */     boolean isAsciiPrintable = StringUtils.isAsciiPrintable(message);
/* 529:557 */     if (!isAsciiPrintable)
/* 530:    */     {
/* 531:558 */       System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
/* 532:559 */       request.setAttribute("X-Kannel-Coding", "2");
/* 533:560 */       if (request.getAttribute("X-Kannel-Coding") != null) {
/* 534:561 */         System.out.println("Request contains X-Kannel-Coding attribute");
/* 535:    */       }
/* 536:    */     }
/* 537:563 */     out.println(message);
/* 538:    */   }
/* 539:    */   
/* 540:    */   public void doPost(HttpServletRequest request, HttpServletResponse response)
/* 541:    */     throws ServletException, IOException
/* 542:    */   {
/* 543:603 */     doGet(request, response);
/* 544:    */   }
/* 545:    */   
/* 546:    */   public void destroy() {}
/* 547:    */   
/* 548:    */   public void forward(ServletRequest request, ServletResponse response)
/* 549:    */     throws ServletException, IOException
/* 550:    */   {
/* 551:612 */     HttpServletRequest req = (HttpServletRequest)request;
/* 552:613 */     HttpServletResponse resp = (HttpServletResponse)response;
/* 553:614 */     doGet(req, resp);
/* 554:    */   }
/* 555:    */   
/* 556:    */   public void include(ServletRequest request, ServletResponse response)
/* 557:    */     throws ServletException, IOException
/* 558:    */   {
/* 559:619 */     HttpServletRequest req = (HttpServletRequest)request;
/* 560:620 */     HttpServletResponse resp = (HttpServletResponse)response;
/* 561:621 */     doGet(req, resp);
/* 562:    */   }
/* 563:    */   
/* 564:    */   public void updateOTTransaction(String kw, String provId, String alertCount, String msisdn, String pricePointId, int isBilled, int isCompleted, String transId)
/* 565:    */   {
/* 566:625 */     String svrAddr = "";
/* 567:626 */     String transactionId = transId;
/* 568:627 */     UserServiceTransaction trans = null;
/* 569:    */     
/* 570:    */ 
/* 571:    */ 
/* 572:631 */     trans = new UserServiceTransaction();
/* 573:632 */     svrAddr = "http://msg.rancardmobility.com:8080/ot.rms/sendsms?to=%2b2000&text=" + kw + "&conn=OT:5511&username=otsms&password=o1t1s1m1s1&serviceId=&price=0&from=" + URLUTF8Encoder.encode(msisdn);
/* 574:    */     
/* 575:634 */     trans.setAccountId(provId);
/* 576:635 */     trans.setKeyword(kw);
/* 577:636 */     trans.setMsg(alertCount);
/* 578:637 */     trans.setMsisdn(msisdn);
/* 579:638 */     trans.setTransactionId(transactionId);
/* 580:639 */     trans.setCallBackUrl(svrAddr);
/* 581:640 */     trans.setPricePoint(pricePointId);
/* 582:641 */     trans.setIsBilled(isBilled);
/* 583:642 */     trans.setIsCompleted(isCompleted);
/* 584:    */     
/* 585:644 */     System.out.println(new java.util.Date() + ":updating transaction for OT:transId=" + transactionId + ":msisdn=" + msisdn + ":keyword=" + kw + ":billed=" + isBilled + ":completed=" + isCompleted);
/* 586:    */     try
/* 587:    */     {
/* 588:646 */       ServiceManager.createTransaction(trans);
/* 589:    */     }
/* 590:    */     catch (Exception e)
/* 591:    */     {
/* 592:648 */       System.out.println(new java.util.Date() + ":sendinfo:error updating OTTransaction:" + e.getMessage());
/* 593:    */     }
/* 594:    */   }
/* 595:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.serviceinterfaces.sendinfo
 * JD-Core Version:    0.7.0.1
 */