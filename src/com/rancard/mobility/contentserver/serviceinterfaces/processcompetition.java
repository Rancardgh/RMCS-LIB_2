/*   1:    */ package com.rancard.mobility.contentserver.serviceinterfaces;
/*   2:    */ 
/*   3:    */ import com.rancard.common.Feedback;
/*   4:    */ import com.rancard.mobility.contentprovider.User;
/*   5:    */ import com.rancard.mobility.contentserver.CPConnections;
/*   6:    */ import com.rancard.mobility.infoserver.common.inbox.InboxEntry;
/*   7:    */ import com.rancard.mobility.infoserver.smscompetition.Competition;
/*   8:    */ import com.rancard.mobility.infoserver.smscompetition.CompetitionManager;
/*   9:    */ import com.rancard.mobility.infoserver.smscompetition.EndlessOddsCompetition;
/*  10:    */ import com.rancard.mobility.infoserver.smscompetition.EndlessOddsWithQuestion;
/*  11:    */ import com.rancard.mobility.infoserver.smscompetition.FixedOddsCompetition;
/*  12:    */ import com.rancard.mobility.infoserver.smscompetition.Option;
/*  13:    */ import com.rancard.util.URLUTF8Encoder;
/*  14:    */ import java.io.IOException;
/*  15:    */ import java.io.PrintStream;
/*  16:    */ import java.io.PrintWriter;
/*  17:    */ import java.sql.Timestamp;
/*  18:    */ import java.util.ArrayList;
/*  19:    */ import java.util.Calendar;
/*  20:    */ import java.util.Date;
/*  21:    */ import java.util.regex.Matcher;
/*  22:    */ import java.util.regex.Pattern;
/*  23:    */ import javax.servlet.RequestDispatcher;
/*  24:    */ import javax.servlet.ServletContext;
/*  25:    */ import javax.servlet.ServletException;
/*  26:    */ import javax.servlet.ServletRequest;
/*  27:    */ import javax.servlet.ServletResponse;
/*  28:    */ import javax.servlet.http.HttpServlet;
/*  29:    */ import javax.servlet.http.HttpServletRequest;
/*  30:    */ import javax.servlet.http.HttpServletResponse;
/*  31:    */ 
/*  32:    */ public class processcompetition
/*  33:    */   extends HttpServlet
/*  34:    */   implements RequestDispatcher
/*  35:    */ {
/*  36:    */   public void init()
/*  37:    */     throws ServletException
/*  38:    */   {}
/*  39:    */   
/*  40:    */   public void doGet(HttpServletRequest request, HttpServletResponse response)
/*  41:    */     throws ServletException, IOException
/*  42:    */   {
/*  43: 27 */     String message = "";
/*  44:    */     
/*  45: 29 */     PrintWriter out = response.getWriter();
/*  46:    */     
/*  47:    */ 
/*  48: 32 */     String ACK = (String)request.getAttribute("ack");
/*  49: 33 */     String kw = request.getParameter("keyword");
/*  50: 34 */     if ((request.getParameter("routeBy") != null) && ((request.getParameter("routeBy").equals("1")) || (request.getParameter("routeBy").equals("2")))) {
/*  51: 36 */       kw = (String)request.getAttribute("attr_keyword");
/*  52:    */     }
/*  53: 38 */     String msg = request.getParameter("msg");
/*  54: 39 */     String dest = request.getParameter("dest");
/*  55: 40 */     String msisdn = request.getParameter("msisdn");
/*  56: 41 */     String provId = (String)request.getAttribute("acctId");
/*  57: 42 */     String date = request.getParameter("date");
/*  58:    */     
/*  59: 44 */     String siteType = (String)request.getAttribute("site_type");
/*  60: 45 */     String lang = (String)request.getAttribute("default_lang");
/*  61: 46 */     if ((lang == null) || (lang.equals(""))) {
/*  62: 47 */       lang = "en";
/*  63:    */     }
/*  64: 49 */     Feedback feedback = (Feedback)getServletContext().getAttribute("feedback_" + lang);
/*  65: 50 */     if (feedback == null) {
/*  66:    */       try
/*  67:    */       {
/*  68: 52 */         feedback = new Feedback();
/*  69:    */       }
/*  70:    */       catch (Exception e) {}
/*  71:    */     }
/*  72: 57 */     CPConnections cnxn = new CPConnections();
/*  73:    */     
/*  74: 59 */     Competition comp = new Competition();
/*  75:    */     
/*  76:    */ 
/*  77: 62 */     String REG_REPLY = "Vote ID: ";
/*  78: 63 */     String FROM = "RMCS";
/*  79: 64 */     String QSTN_REPLY = "Question: ";
/*  80: 65 */     String provName = "";
/*  81:    */     
/*  82: 67 */     String svrresp = new String();
/*  83: 68 */     String number = new String();
/*  84: 69 */     Date realDate = new Date();
/*  85: 72 */     if ((provId == null) || (provId.equals("")))
/*  86:    */     {
/*  87:    */       try
/*  88:    */       {
/*  89: 74 */         if (siteType.equals("2")) {
/*  90: 75 */           message = feedback.getUserFriendlyDescription("10004");
/*  91:    */         } else {
/*  92: 77 */           message = feedback.formDefaultMessage("10004");
/*  93:    */         }
/*  94:    */       }
/*  95:    */       catch (Exception ex) {}
/*  96: 81 */       out.println(message);
/*  97: 82 */       return;
/*  98:    */     }
/*  99: 85 */     if ((msisdn == null) || (msisdn.equals("")))
/* 100:    */     {
/* 101:    */       try
/* 102:    */       {
/* 103: 87 */         if (siteType.equals("2")) {
/* 104: 88 */           message = feedback.getUserFriendlyDescription("2000");
/* 105:    */         } else {
/* 106: 90 */           message = feedback.formDefaultMessage("2000");
/* 107:    */         }
/* 108:    */       }
/* 109:    */       catch (Exception ex) {}
/* 110: 94 */       out.println(message);
/* 111: 95 */       return;
/* 112:    */     }
/* 113: 98 */     if ((kw == null) || (kw.equals("")))
/* 114:    */     {
/* 115:    */       try
/* 116:    */       {
/* 117:100 */         if (siteType.equals("2")) {
/* 118:101 */           message = feedback.getUserFriendlyDescription("9000");
/* 119:    */         } else {
/* 120:103 */           message = feedback.formDefaultMessage("9000");
/* 121:    */         }
/* 122:    */       }
/* 123:    */       catch (Exception ex) {}
/* 124:107 */       out.println(message);
/* 125:108 */       return;
/* 126:    */     }
/* 127:111 */     if ((dest == null) || (dest.equals(""))) {
/* 128:112 */       dest = "";
/* 129:    */     }
/* 130:115 */     if ((msg == null) || (msg.equals(""))) {
/* 131:116 */       msg = "";
/* 132:    */     }
/* 133:119 */     if ((ACK == null) || (ACK.equals(""))) {
/* 134:120 */       ACK = "";
/* 135:    */     }
/* 136:123 */     if ((date == null) || (date.equals(""))) {
/* 137:124 */       realDate = Calendar.getInstance().getTime();
/* 138:    */     } else {
/* 139:    */       try
/* 140:    */       {
/* 141:127 */         Timestamp t = Timestamp.valueOf(date);
/* 142:128 */         realDate = new Date(t.getTime());
/* 143:129 */         t = null;
/* 144:    */       }
/* 145:    */       catch (Exception e)
/* 146:    */       {
/* 147:131 */         realDate = Calendar.getInstance().getTime();
/* 148:    */       }
/* 149:    */     }
/* 150:136 */     number = msisdn;
/* 151:137 */     if (number.indexOf("+") != -1)
/* 152:    */     {
/* 153:138 */       StringBuffer sb = new StringBuffer(number);
/* 154:139 */       sb.deleteCharAt(number.indexOf("+"));
/* 155:140 */       number = sb.toString();
/* 156:    */       
/* 157:142 */       sb = null;
/* 158:    */     }
/* 159:144 */     number = number.trim();
/* 160:    */     try
/* 161:    */     {
/* 162:148 */       Long.parseLong(number);
/* 163:149 */       msisdn = "+" + number;
/* 164:    */     }
/* 165:    */     catch (NumberFormatException e)
/* 166:    */     {
/* 167:    */       try
/* 168:    */       {
/* 169:152 */         if (siteType.equals("2")) {
/* 170:153 */           message = feedback.getUserFriendlyDescription("2000");
/* 171:    */         } else {
/* 172:155 */           message = feedback.formDefaultMessage("2000");
/* 173:    */         }
/* 174:    */       }
/* 175:    */       catch (Exception ex) {}
/* 176:159 */       out.println(message);
/* 177:160 */       return;
/* 178:    */     }
/* 179:    */     try
/* 180:    */     {
/* 181:165 */       cnxn = CPConnections.getConnection(provId, msisdn);
/* 182:    */     }
/* 183:    */     catch (Exception e)
/* 184:    */     {
/* 185:    */       try
/* 186:    */       {
/* 187:168 */         if (siteType.equals("2")) {
/* 188:169 */           message = feedback.getUserFriendlyDescription("8002");
/* 189:    */         } else {
/* 190:171 */           message = feedback.formDefaultMessage("8002");
/* 191:    */         }
/* 192:    */       }
/* 193:    */       catch (Exception ex) {}
/* 194:175 */       out.println(message);
/* 195:176 */       return;
/* 196:    */     }
/* 197:180 */     System.out.println("Received request to participate in competition");
/* 198:181 */     System.out.println("Date received: " + Calendar.getInstance().getTime().toString());
/* 199:182 */     System.out.println("Keyword: " + kw);
/* 200:183 */     System.out.println("Message: " + msg);
/* 201:184 */     System.out.println("Destination number: " + dest);
/* 202:185 */     System.out.println("Participant's number: " + msisdn);
/* 203:186 */     System.out.println("Service privoder's ID: " + provId);
/* 204:187 */     System.out.println("Date (from RMS): " + date);
/* 205:    */     
/* 206:    */ 
/* 207:190 */     User user = new User();
/* 208:    */     try
/* 209:    */     {
/* 210:192 */       user = user.viewDealer(provId);
/* 211:194 */       if (user != null)
/* 212:    */       {
/* 213:195 */         double inboxLimit = user.getInboxBalance();
/* 214:196 */         if (inboxLimit - 1.0D < 0.0D)
/* 215:    */         {
/* 216:197 */           System.out.println("User does NOT have enough space in inbox");
/* 217:198 */           throw new Exception("4005");
/* 218:    */         }
/* 219:200 */         System.out.println("User has enough space in inbox");
/* 220:201 */         double result = inboxLimit - 1.0D;
/* 221:202 */         user.updateDealer(user.getId(), user.getBandwidthBalance(), result, user.getOutboxBalance());
/* 222:    */       }
/* 223:    */       else
/* 224:    */       {
/* 225:205 */         throw new Exception("10004");
/* 226:    */       }
/* 227:    */     }
/* 228:    */     catch (Exception e)
/* 229:    */     {
/* 230:    */       try
/* 231:    */       {
/* 232:209 */         if (siteType.equals("2")) {
/* 233:210 */           message = feedback.getUserFriendlyDescription(e.getMessage());
/* 234:    */         } else {
/* 235:212 */           message = feedback.formDefaultMessage(e.getMessage());
/* 236:    */         }
/* 237:214 */         if (message == null) {
/* 238:215 */           message = e.getMessage();
/* 239:    */         }
/* 240:    */       }
/* 241:    */       catch (Exception ex) {}
/* 242:219 */       out.println(message);
/* 243:220 */       return;
/* 244:    */     }
/* 245:    */     try
/* 246:    */     {
/* 247:    */       try
/* 248:    */       {
/* 249:227 */         System.out.println("Looking up competition details ......");
/* 250:    */         
/* 251:229 */         comp = CompetitionManager.viewCompetition(kw, provId, realDate);
/* 252:230 */         provName = user.getName();
/* 253:    */         
/* 254:232 */         System.out.println("Found details for competition with ID: " + comp.getCompetitionId());
/* 255:233 */         System.out.println("competition name: " + comp.getServiceName());
/* 256:234 */         System.out.println("competition question: " + comp.getQuestion());
/* 257:235 */         System.out.println("Keyword: " + comp.getKeyword());
/* 258:    */       }
/* 259:    */       catch (Exception e)
/* 260:    */       {
/* 261:239 */         System.out.println("Could not find details for competition");
/* 262:    */         
/* 263:241 */         throw new Exception("9000");
/* 264:    */       }
/* 265:245 */       if (!comp.isEnded())
/* 266:    */       {
/* 267:247 */         System.out.println("Competition not ended");
/* 268:250 */         if (comp.getType() == 1)
/* 269:    */         {
/* 270:252 */           System.out.println("Competition is a fixed odds competition");
/* 271:    */           
/* 272:254 */           FixedOddsCompetition fo = (FixedOddsCompetition)comp;
/* 273:256 */           if (!fo.alreadyRegistered(msisdn))
/* 274:    */           {
/* 275:258 */             System.out.println("participant hzs not already registered");
/* 276:260 */             if (fo.canAddParticipant())
/* 277:    */             {
/* 278:262 */               System.out.println("participant can be registered");
/* 279:    */               
/* 280:    */ 
/* 281:265 */               InboxEntry entry = new InboxEntry();
/* 282:266 */               entry.setMsisdn(msisdn);
/* 283:267 */               entry.setKeyword(fo.getKeyword());
/* 284:268 */               entry.setShortCode(dest);
/* 285:269 */               entry.setAccountId(provId);
/* 286:270 */               String registrationId = fo.register(entry);
/* 287:    */               
/* 288:272 */               System.out.println("participant has been registered");
/* 289:    */               
/* 290:    */ 
/* 291:    */ 
/* 292:    */ 
/* 293:    */ 
/* 294:278 */               String options = "";
/* 295:279 */               ArrayList opt = fo.getOptions();
/* 296:280 */               for (int i = 0; i < opt.size(); i++) {
/* 297:281 */                 options = options + ((Option)opt.get(i)).getOptionId() + ". " + ((Option)opt.get(i)).getDescription() + ", ";
/* 298:    */               }
/* 299:284 */               String question = fo.getQuestion() + " " + options;
/* 300:    */               
/* 301:286 */               System.out.println("sending question");
/* 302:    */               
/* 303:    */ 
/* 304:    */ 
/* 305:290 */               fo = null;
/* 306:291 */               entry = null;
/* 307:292 */               opt = null;
/* 308:    */               
/* 309:    */ 
/* 310:295 */               out.println(new String(question));
/* 311:296 */               return;
/* 312:    */             }
/* 313:299 */             System.out.println("participant can NOT be registered. participation limit exceeded");
/* 314:    */             
/* 315:301 */             throw new Exception("9006");
/* 316:    */           }
/* 317:303 */           if ((fo.alreadyRegistered(msisdn)) && (!fo.alreadyVoted(msisdn)) && ((msg == null) || (msg.equals(""))))
/* 318:    */           {
/* 319:306 */             System.out.println("participant has already been registered. cannot register");
/* 320:    */             
/* 321:308 */             throw new Exception("10007");
/* 322:    */           }
/* 323:309 */           if (fo.alreadyVoted(msisdn))
/* 324:    */           {
/* 325:311 */             System.out.println("participant has already voted. cannot vote");
/* 326:    */             
/* 327:313 */             throw new Exception("9008");
/* 328:    */           }
/* 329:314 */           if ((!fo.alreadyVoted(msisdn)) && (msg != null) && (!msg.equals("")))
/* 330:    */           {
/* 331:317 */             System.out.println("participant can vote");
/* 332:    */             
/* 333:319 */             InboxEntry entry = new InboxEntry();
/* 334:320 */             entry.setMsisdn(msisdn);
/* 335:321 */             entry.setKeyword(fo.getKeyword());
/* 336:322 */             entry.setMessage(msg);
/* 337:323 */             entry.setShortCode(dest);
/* 338:324 */             entry.setAccountId(provId);
/* 339:325 */             fo.vote(entry);
/* 340:    */             
/* 341:327 */             System.out.println("participant voted successfully");
/* 342:    */             
/* 343:    */ 
/* 344:    */ 
/* 345:331 */             fo = null;
/* 346:    */             
/* 347:    */ 
/* 348:334 */             String insertions = "ack=" + ACK + "&keyword=" + kw + "&msg=" + msg + "&msisdn=" + msisdn + "&acctId=" + provId + "&srvcName=" + fo.getServiceName() + "&provName=" + provName + "&pin=" + entry.getMessageId();
/* 349:    */             
/* 350:336 */             ACK = URLUTF8Encoder.doMessageEscaping(insertions, ACK);
/* 351:337 */             out.println(ACK);
/* 352:    */             
/* 353:    */ 
/* 354:340 */             entry = null;
/* 355:    */             
/* 356:    */ 
/* 357:343 */             return;
/* 358:    */           }
/* 359:    */         }
/* 360:    */         else
/* 361:    */         {
/* 362:346 */           if (comp.getType() == 0)
/* 363:    */           {
/* 364:348 */             System.out.println("Competition is a endless odds competition");
/* 365:    */             
/* 366:350 */             EndlessOddsCompetition eo = (EndlessOddsCompetition)comp;
/* 367:    */             
/* 368:    */ 
/* 369:353 */             System.out.println("participant can vote");
/* 370:    */             
/* 371:355 */             InboxEntry entry = new InboxEntry();
/* 372:356 */             entry.setMsisdn(msisdn);
/* 373:357 */             entry.setKeyword(eo.getKeyword());
/* 374:358 */             entry.setMessage(msg);
/* 375:359 */             entry.setShortCode(dest);
/* 376:360 */             entry.setAccountId(provId);
/* 377:361 */             String voteid = eo.vote(entry);
/* 378:    */             
/* 379:363 */             System.out.println("participant voted successfully");
/* 380:    */             
/* 381:    */ 
/* 382:    */ 
/* 383:367 */             String insertions = "ack=" + ACK + "&keyword=" + kw + "&msg=" + msg + "&msisdn=" + msisdn + "&acctId=" + provId + "&srvcName=" + eo.getServiceName() + "&provName=" + provName + "&pin=" + entry.getMessageId();
/* 384:    */             
/* 385:369 */             ACK = URLUTF8Encoder.doMessageEscaping(insertions, ACK);
/* 386:370 */             out.println(ACK);
/* 387:    */             
/* 388:    */ 
/* 389:373 */             eo = null;
/* 390:374 */             entry = null;
/* 391:    */             
/* 392:    */ 
/* 393:377 */             return;
/* 394:    */           }
/* 395:378 */           if (comp.getType() == 2)
/* 396:    */           {
/* 397:380 */             System.out.println("Competition is a endless odds competition with question");
/* 398:    */             
/* 399:382 */             EndlessOddsWithQuestion eo = (EndlessOddsWithQuestion)comp;
/* 400:383 */             if ((msg == null) || (msg.equals("")))
/* 401:    */             {
/* 402:385 */               System.out.println("keyword only - requesting question");
/* 403:    */               
/* 404:387 */               String options = "";
/* 405:388 */               ArrayList opt = eo.getOptions();
/* 406:389 */               for (int i = 0; i < opt.size(); i++) {
/* 407:390 */                 options = options + ((Option)opt.get(i)).getOptionId() + ". " + ((Option)opt.get(i)).getDescription() + ", ";
/* 408:    */               }
/* 409:393 */               String question = eo.getQuestion() + " " + options;
/* 410:    */               
/* 411:395 */               System.out.println("sending question");
/* 412:    */               
/* 413:    */ 
/* 414:398 */               eo = null;
/* 415:399 */               opt = null;
/* 416:    */               
/* 417:401 */               out.println(new String(question));
/* 418:402 */               return;
/* 419:    */             }
/* 420:406 */             System.out.println("participant can vote");
/* 421:    */             
/* 422:408 */             InboxEntry entry = new InboxEntry();
/* 423:409 */             entry.setMsisdn(msisdn);
/* 424:410 */             entry.setKeyword(eo.getKeyword());
/* 425:411 */             entry.setMessage(msg);
/* 426:412 */             entry.setShortCode(dest);
/* 427:413 */             entry.setAccountId(provId);
/* 428:414 */             String voteid = eo.vote(entry);
/* 429:    */             
/* 430:416 */             System.out.println("participant voted successfully");
/* 431:    */             
/* 432:    */ 
/* 433:    */ 
/* 434:420 */             String insertions = "ack=" + ACK + "&keyword=" + kw + "&msg=" + msg + "&msisdn=" + msisdn + "&acctId=" + provId + "&srvcName=" + eo.getServiceName() + "&provName=" + provName + "&pin=" + entry.getMessageId();
/* 435:    */             
/* 436:422 */             ACK = URLUTF8Encoder.doMessageEscaping(insertions, ACK);
/* 437:423 */             out.println(ACK);
/* 438:    */             
/* 439:    */ 
/* 440:426 */             entry = null;
/* 441:    */           }
/* 442:    */         }
/* 443:    */       }
/* 444:    */       else
/* 445:    */       {
/* 446:434 */         System.out.println("Competition ended");
/* 447:    */         
/* 448:436 */         throw new Exception("9002");
/* 449:    */       }
/* 450:    */     }
/* 451:    */     catch (Exception e)
/* 452:    */     {
/* 453:    */       try
/* 454:    */       {
/* 455:440 */         if (siteType.equals("2")) {
/* 456:441 */           message = feedback.getUserFriendlyDescription(e.getMessage());
/* 457:    */         } else {
/* 458:443 */           message = feedback.formDefaultMessage(e.getMessage());
/* 459:    */         }
/* 460:445 */         if (message == null) {
/* 461:446 */           message = e.getMessage();
/* 462:    */         }
/* 463:    */       }
/* 464:    */       catch (Exception ex)
/* 465:    */       {
/* 466:449 */         message = e.getMessage();
/* 467:    */       }
/* 468:451 */       out.println(message);
/* 469:452 */       return;
/* 470:    */     }
/* 471:    */     finally
/* 472:    */     {
/* 473:454 */       realDate = null;
/* 474:455 */       cnxn = null;
/* 475:456 */       comp = null;
/* 476:    */     }
/* 477:    */   }
/* 478:    */   
/* 479:    */   public void doPost(HttpServletRequest request, HttpServletResponse response)
/* 480:    */     throws ServletException, IOException
/* 481:    */   {
/* 482:464 */     doGet(request, response);
/* 483:    */   }
/* 484:    */   
/* 485:    */   public void destroy() {}
/* 486:    */   
/* 487:    */   public void forward(ServletRequest request, ServletResponse response)
/* 488:    */     throws ServletException, IOException
/* 489:    */   {
/* 490:473 */     HttpServletRequest req = (HttpServletRequest)request;
/* 491:474 */     HttpServletResponse resp = (HttpServletResponse)response;
/* 492:475 */     doGet(req, resp);
/* 493:    */   }
/* 494:    */   
/* 495:    */   public void include(ServletRequest request, ServletResponse response)
/* 496:    */     throws ServletException, IOException
/* 497:    */   {
/* 498:480 */     HttpServletRequest req = (HttpServletRequest)request;
/* 499:481 */     HttpServletResponse resp = (HttpServletResponse)response;
/* 500:482 */     doGet(req, resp);
/* 501:    */   }
/* 502:    */   
/* 503:    */   private String doURLEscaping(String insertions, String stored_ack)
/* 504:    */     throws Exception
/* 505:    */   {
/* 506:486 */     String url_tmp = stored_ack;
/* 507:487 */     String result = "";
/* 508:488 */     String tmp_1_val = "";
/* 509:    */     
/* 510:490 */     String[] query = insertions.split("&");
/* 511:492 */     for (int i = 0; i < query.length; i++)
/* 512:    */     {
/* 513:493 */       String[] tmp = query[i].split("=");
/* 514:494 */       Pattern pattern = Pattern.compile("@@" + tmp[0] + "@@");
/* 515:495 */       Matcher matcher = pattern.matcher(url_tmp);
/* 516:    */       try
/* 517:    */       {
/* 518:497 */         tmp_1_val = tmp[1];
/* 519:    */       }
/* 520:    */       catch (ArrayIndexOutOfBoundsException a)
/* 521:    */       {
/* 522:499 */         tmp_1_val = "";
/* 523:    */       }
/* 524:501 */       url_tmp = matcher.replaceAll(tmp_1_val);
/* 525:    */     }
/* 526:503 */     result = url_tmp;
/* 527:    */     
/* 528:    */ 
/* 529:506 */     return result;
/* 530:    */   }
/* 531:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.serviceinterfaces.processcompetition
 * JD-Core Version:    0.7.0.1
 */