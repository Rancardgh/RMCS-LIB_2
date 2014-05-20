/*   1:    */ package com.rancard.mobility.infoserver.livescore.serviceinterfaces;
/*   2:    */ 
/*   3:    */ import com.rancard.common.Feedback;
/*   4:    */ import com.rancard.mobility.infoserver.livescore.LiveScoreFixture;
/*   5:    */ import com.rancard.mobility.infoserver.livescore.LiveScoreServiceManager;
/*   6:    */ import com.rancard.mobility.infoserver.livescore.LiveScoreUpdate;
/*   7:    */ import java.io.BufferedReader;
/*   8:    */ import java.io.IOException;
/*   9:    */ import java.io.InputStreamReader;
/*  10:    */ import java.io.PrintStream;
/*  11:    */ import java.io.PrintWriter;
/*  12:    */ import java.net.URLDecoder;
/*  13:    */ import java.sql.Timestamp;
/*  14:    */ import java.util.ArrayList;
/*  15:    */ import java.util.Calendar;
/*  16:    */ import java.util.Date;
/*  17:    */ import java.util.Iterator;
/*  18:    */ import javax.servlet.ServletContext;
/*  19:    */ import javax.servlet.ServletException;
/*  20:    */ import javax.servlet.http.HttpServlet;
/*  21:    */ import javax.servlet.http.HttpServletRequest;
/*  22:    */ import javax.servlet.http.HttpServletResponse;
/*  23:    */ import org.apache.commons.httpclient.HttpClient;
/*  24:    */ import org.apache.commons.httpclient.HttpException;
/*  25:    */ import org.apache.commons.httpclient.methods.GetMethod;
/*  26:    */ import org.dom4j.Attribute;
/*  27:    */ import org.dom4j.Document;
/*  28:    */ import org.dom4j.DocumentHelper;
/*  29:    */ import org.dom4j.Element;
/*  30:    */ 
/*  31:    */ public class receivefeed
/*  32:    */   extends HttpServlet
/*  33:    */ {
/*  34:    */   private static final String CONTENT_TYPE = "text/html";
/*  35: 17 */   private String mode = "";
/*  36:    */   
/*  37:    */   public void init()
/*  38:    */     throws ServletException
/*  39:    */   {}
/*  40:    */   
/*  41:    */   public void doGet(HttpServletRequest request, HttpServletResponse response)
/*  42:    */     throws ServletException, IOException
/*  43:    */   {
/*  44: 32 */     PrintWriter pw = null;
/*  45: 33 */     InputStreamReader isr = null;
/*  46: 34 */     BufferedReader br = null;
/*  47:    */     
/*  48: 36 */     Feedback feedback_en = (Feedback)getServletContext().getAttribute("feedback_en");
/*  49: 37 */     if (feedback_en == null) {
/*  50:    */       try
/*  51:    */       {
/*  52: 39 */         feedback_en = new Feedback();
/*  53:    */       }
/*  54:    */       catch (Exception e) {}
/*  55:    */     }
/*  56: 43 */     Feedback feedback_fr = (Feedback)getServletContext().getAttribute("feedback_fr");
/*  57: 44 */     if (feedback_fr == null) {
/*  58:    */       try
/*  59:    */       {
/*  60: 46 */         feedback_fr = new Feedback();
/*  61:    */       }
/*  62:    */       catch (Exception e) {}
/*  63:    */     }
/*  64:    */     try
/*  65:    */     {
/*  66: 52 */       String svrAddr = getServletContext().getInitParameter("contentServerUrl");
/*  67: 53 */       pw = response.getWriter();
/*  68:    */       
/*  69: 55 */       isr = new InputStreamReader(request.getInputStream());
/*  70:    */       
/*  71: 57 */       br = new BufferedReader(isr);
/*  72: 58 */       String line = new String();
/*  73: 59 */       String resp = new String();
/*  74: 60 */       while ((line = br.readLine()) != null) {
/*  75: 61 */         resp = resp + line + "\n";
/*  76:    */       }
/*  77: 64 */       String rawXML = resp.substring(resp.indexOf("%"));
/*  78: 65 */       System.out.println(new Date() + ":raw Score24 XML: " + rawXML);
/*  79: 66 */       String processedXML = URLDecoder.decode(rawXML, "UTF-8");
/*  80:    */       
/*  81:    */ 
/*  82:    */ 
/*  83: 70 */       LiveScoreUpdate update = processResponse(processedXML, feedback_fr, feedback_en);
/*  84: 71 */       if (!resp.equals(""))
/*  85:    */       {
/*  86: 72 */         System.out.println(new Date() + ":Received XML from Score24 for " + update.getLeagueName());
/*  87: 73 */         System.out.println(new Date() + ":Event ID " + update.getEventId());
/*  88: 74 */         System.out.println(new Date() + ":Event Update ID " + update.getUpdateId());
/*  89: 75 */         System.out.println(new Date() + ":Event Name " + update.getEventName());
/*  90: 76 */         System.out.println(new Date() + ":Event Status " + update.getEventStatus());
/*  91: 77 */         System.out.println(new Date() + ":Request Mode " + this.mode);
/*  92:    */         
/*  93: 79 */         response.setStatus(200);
/*  94: 80 */         pw.println("OK");
/*  95:    */       }
/*  96:    */       else
/*  97:    */       {
/*  98: 82 */         System.out.println(new Date() + ":DID NOT receive XML from Score24");
/*  99:    */       }
/* 100: 85 */       if (update != null)
/* 101:    */       {
/* 102: 86 */         LiveScoreFixture game = new LiveScoreFixture();
/* 103: 87 */         LiveScoreFixture temp_game = LiveScoreServiceManager.viewFixture(update.getEventId());
/* 104:    */         
/* 105: 89 */         game.setCountryName(update.getCountryName());
/* 106:    */         
/* 107: 91 */         String eventdate = update.getEventDate().replaceAll("T", " ");
/* 108: 92 */         eventdate = eventdate.substring(0, eventdate.indexOf("+"));
/* 109: 93 */         game.setDate(eventdate);
/* 110: 94 */         game.setGameId(update.getEventId());
/* 111: 95 */         game.setLeagueName(update.getLeagueName());
/* 112: 96 */         game.setLeagueId(update.getLeagueId());
/* 113:    */         
/* 114: 98 */         game.setHomeTeam((String)update.getParticipants().get(0));
/* 115:    */         
/* 116:100 */         String homescore = (String)update.getScores().get(0);
/* 117:101 */         if ((homescore == null) || (homescore.equals(""))) {
/* 118:101 */           homescore = "NA";
/* 119:    */         }
/* 120:102 */         game.setHomeScore(homescore);
/* 121:    */         
/* 122:104 */         game.setAwayTeam((String)update.getParticipants().get(1));
/* 123:    */         
/* 124:106 */         String awayscore = (String)update.getScores().get(1);
/* 125:107 */         if ((awayscore == null) || (awayscore.equals(""))) {
/* 126:107 */           awayscore = "NA";
/* 127:    */         }
/* 128:108 */         game.setAwayScore(awayscore);
/* 129:110 */         if (update.getEventStatus().equals("1000")) {
/* 130:111 */           game.setStatus(0);
/* 131:112 */         } else if ((update.getEventStatus().equals("1500")) || (update.getEventStatus().equals("1420")) || (update.getEventStatus().equals("1400"))) {
/* 132:113 */           game.setStatus(1);
/* 133:114 */         } else if (update.getEventStatus().equals("1100")) {
/* 134:115 */           game.setStatus(2);
/* 135:116 */         } else if (update.getEventStatus().equals("1200")) {
/* 136:117 */           game.setStatus(4);
/* 137:118 */         } else if (update.getEventStatus().equals("1300")) {
/* 138:119 */           game.setStatus(5);
/* 139:120 */         } else if ((update.getEventStatus().equals("1600")) || (update.getEventStatus().equals("1700"))) {
/* 140:121 */           game.setStatus(6);
/* 141:122 */         } else if (update.getEventStatus().equals("1800")) {
/* 142:123 */           game.setStatus(7);
/* 143:    */         } else {
/* 144:125 */           game.setStatus(3);
/* 145:    */         }
/* 146:128 */         game.setEventNotifSent(temp_game.getEventNotifSent());
/* 147:129 */         temp_game = null;
/* 148:    */         
/* 149:131 */         Calendar now = Calendar.getInstance();
/* 150:132 */         Calendar gameTime = Calendar.getInstance();
/* 151:133 */         gameTime.setTime(new Date(Timestamp.valueOf(game.getDate()).getTime()));
/* 152:134 */         gameTime.add(12, -10);
/* 153:136 */         if ((this.mode.equalsIgnoreCase("Manual request")) || (now.before(gameTime)))
/* 154:    */         {
/* 155:138 */           System.out.println(new Date() + ":Creating fixture for " + game.getHomeTeam() + " vs " + game.getAwayTeam());
/* 156:139 */           LiveScoreServiceManager.createFixture(game);
/* 157:140 */           game = null;
/* 158:    */         }
/* 159:    */         else
/* 160:    */         {
/* 161:142 */           ArrayList pairing = LiveScoreServiceManager.getAccount_KeywordPairForService(game.getLeagueId());
/* 162:143 */           if ((!update.getEventStatus().equals("1000")) || (!update.getTrigger().equals(""))) {
/* 163:145 */             if (update.getEventStatus().equals("1500"))
/* 164:    */             {
/* 165:147 */               LiveScoreServiceManager.updateFixture(game);
/* 166:    */             }
/* 167:148 */             else if ((update.getEventStatus().equals("1420")) || (update.getEventStatus().equals("1400")))
/* 168:    */             {
/* 169:149 */               LiveScoreServiceManager.updateFixture(game);
/* 170:    */             }
/* 171:    */             else
/* 172:    */             {
/* 173:153 */               String englishMessage = createMessage(update, feedback_en);
/* 174:154 */               String frenchMessage = createMessage(update, feedback_fr);
/* 175:    */               
/* 176:156 */               update.setEnglishMessage(englishMessage);
/* 177:157 */               update.setFrenchMessage(frenchMessage);
/* 178:    */               
/* 179:    */ 
/* 180:160 */               LiveScoreServiceManager.updateFixture(game);
/* 181:163 */               if (LiveScoreServiceManager.createUpdate(update) == true)
/* 182:    */               {
/* 183:164 */                 System.out.println(new Date() + ":created update record for " + update.getUpdateId());
/* 184:165 */                 request.setAttribute("updateId", update.getUpdateId());
/* 185:167 */                 for (int j = 0; j < pairing.size(); j++)
/* 186:    */                 {
/* 187:168 */                   String pair = (String)pairing.get(j);
/* 188:169 */                   String accountId = pair.split("-")[0];
/* 189:    */                   
/* 190:171 */                   LiveScoreServiceManager.sendUpdate(svrAddr, accountId, update.getUpdateId());
/* 191:172 */                   System.out.println(new Date() + ":>>>>>>>>>>>>>>>>>>>accountId: " + accountId + " >>>>>>>>>>>>>>>>>>>updateId: " + update.getUpdateId());
/* 192:    */                 }
/* 193:    */               }
/* 194:    */               else
/* 195:    */               {
/* 196:175 */                 System.out.println(new Date() + ":could NOT create update record for " + update.getUpdateId());
/* 197:    */               }
/* 198:    */             }
/* 199:    */           }
/* 200:    */         }
/* 201:    */       }
/* 202:    */       else
/* 203:    */       {
/* 204:180 */         System.out.println(new Date() + ":XML_PARSER_ERROR:");
/* 205:181 */         throw new Exception("7000");
/* 206:    */       }
/* 207:    */       return;
/* 208:    */     }
/* 209:    */     catch (Exception e)
/* 210:    */     {
/* 211:184 */       System.out.println(new Date() + ":ERROR:" + e.getMessage());
/* 212:185 */       pw.println(e.getMessage());
/* 213:    */     }
/* 214:    */     finally
/* 215:    */     {
/* 216:187 */       if (pw != null) {
/* 217:188 */         pw.close();
/* 218:    */       }
/* 219:    */       try
/* 220:    */       {
/* 221:191 */         br.close();
/* 222:    */       }
/* 223:    */       catch (Exception e) {}
/* 224:    */       try
/* 225:    */       {
/* 226:195 */         isr.close();
/* 227:    */       }
/* 228:    */       catch (Exception e) {}
/* 229:    */     }
/* 230:    */   }
/* 231:    */   
/* 232:    */   public void doPost(HttpServletRequest request, HttpServletResponse response)
/* 233:    */     throws ServletException, IOException
/* 234:    */   {
/* 235:203 */     doGet(request, response);
/* 236:    */   }
/* 237:    */   
/* 238:    */   public void destroy() {}
/* 239:    */   
/* 240:    */   private LiveScoreUpdate processResponse(String xmlDoc, Feedback french, Feedback english)
/* 241:    */   {
/* 242:211 */     LiveScoreUpdate update = new LiveScoreUpdate();
/* 243:212 */     String description = "";
/* 244:213 */     String score = "";
/* 245:    */     try
/* 246:    */     {
/* 247:216 */       Document xml = DocumentHelper.parseText(xmlDoc);
/* 248:217 */       Element rootNode = xml.getRootElement();
/* 249:218 */       Element element = null;
/* 250:219 */       Attribute attribute = null;
/* 251:    */       
/* 252:    */ 
/* 253:222 */       Iterator i = rootNode.elementIterator();
/* 254:    */       
/* 255:    */ 
/* 256:225 */       element = rootNode.element("message");
/* 257:226 */       attribute = element.attribute("id");
/* 258:227 */       update.setUpdateId(attribute.getValue());
/* 259:    */       
/* 260:    */ 
/* 261:230 */       description = element.element("description").getText();
/* 262:231 */       this.mode = description;
/* 263:232 */       element = element.element("event");
/* 264:    */       
/* 265:    */ 
/* 266:    */ 
/* 267:236 */       attribute = element.attribute("id");
/* 268:237 */       update.setEventId(attribute.getValue());
/* 269:238 */       attribute = element.attribute("primarycountry");
/* 270:239 */       update.setCountryName(attribute.getValue());
/* 271:240 */       attribute = element.attribute("primarydesc");
/* 272:241 */       update.setLeagueName(attribute.getValue());
/* 273:242 */       attribute = element.attribute("primaryid");
/* 274:243 */       update.setLeagueId(attribute.getValue());
/* 275:244 */       attribute = element.attribute("status");
/* 276:245 */       update.setEventStatus(attribute.getValue());
/* 277:    */       
/* 278:    */ 
/* 279:248 */       Element innerelement = null;
/* 280:249 */       innerelement = element.element("name");
/* 281:250 */       update.setEventName(innerelement.getText());
/* 282:    */       
/* 283:    */ 
/* 284:253 */       innerelement = element.element("date");
/* 285:254 */       update.setEventDate(innerelement.getText());
/* 286:    */       
/* 287:    */ 
/* 288:257 */       i = element.elementIterator();
/* 289:258 */       ArrayList participants = new ArrayList();
/* 290:259 */       ArrayList scores = new ArrayList();
/* 291:260 */       scores.add("NA");
/* 292:261 */       scores.add("NA");
/* 293:262 */       int participantiterations = 0;
/* 294:263 */       while (i.hasNext())
/* 295:    */       {
/* 296:264 */         innerelement = (Element)i.next();
/* 297:265 */         if (innerelement.getName().equals("participant"))
/* 298:    */         {
/* 299:266 */           participants.add(innerelement.element("name").getText());
/* 300:267 */           Iterator e = innerelement.elementIterator();
/* 301:269 */           while (e.hasNext())
/* 302:    */           {
/* 303:270 */             Element subelement = (Element)e.next();
/* 304:271 */             if (subelement.getName().equals("result"))
/* 305:    */             {
/* 306:273 */               Attribute type = subelement.attribute("type");
/* 307:274 */               Attribute prd = subelement.attribute("period");
/* 308:275 */               if ((type != null) && (type.getValue().equals("200")) && (prd != null) && (prd.getValue().equals("90"))) {
/* 309:277 */                 scores.set(participantiterations, subelement.getText());
/* 310:    */               }
/* 311:280 */               attribute = subelement.attribute("trigger");
/* 312:281 */               if ((attribute != null) && (attribute.getName().equals("trigger")) && (attribute.getValue().equals("yes")))
/* 313:    */               {
/* 314:283 */                 attribute = subelement.attribute("period");
/* 315:    */                 
/* 316:285 */                 String period_en = english.getLiveScorePeriodCode(attribute.getValue());
/* 317:286 */                 String period_fr = french.getLiveScorePeriodCode(attribute.getValue());
/* 318:    */                 
/* 319:288 */                 attribute = subelement.attribute("type");
/* 320:    */                 
/* 321:290 */                 String resultType_en = english.getLiveScoreResultType(attribute.getValue());
/* 322:291 */                 String resultType_fr = french.getLiveScoreResultType(attribute.getValue());
/* 323:292 */                 update.setTrigger("LS_RT_" + attribute.getValue());
/* 324:    */                 
/* 325:294 */                 Element text = subelement.element("text");
/* 326:295 */                 String resultDescription_en = "";
/* 327:296 */                 String resultDescription_fr = "";
/* 328:297 */                 if (text != null)
/* 329:    */                 {
/* 330:299 */                   if ((attribute.getValue().equals("410")) || (attribute.getValue().equals("420")))
/* 331:    */                   {
/* 332:300 */                     String value = text.getText();
/* 333:301 */                     resultDescription_en = value.substring(0, value.lastIndexOf(".") + 1);
/* 334:302 */                     resultDescription_fr = value.substring(0, value.lastIndexOf(".") + 1);
/* 335:    */                   }
/* 336:    */                   else
/* 337:    */                   {
/* 338:304 */                     resultDescription_en = text.getText();
/* 339:305 */                     resultDescription_fr = text.getText();
/* 340:    */                   }
/* 341:307 */                   text = null;
/* 342:    */                 }
/* 343:309 */                 else if (description.equalsIgnoreCase("Changed status"))
/* 344:    */                 {
/* 345:311 */                   resultDescription_en = english.getLiveScoreStatusCode(subelement.getText());
/* 346:312 */                   resultDescription_fr = french.getLiveScoreStatusCode(subelement.getText());
/* 347:    */                 }
/* 348:    */                 else
/* 349:    */                 {
/* 350:315 */                   resultDescription_en = subelement.getText();
/* 351:316 */                   resultDescription_fr = subelement.getText();
/* 352:    */                 }
/* 353:320 */                 update.setEnglishMessage(period_en + ", " + resultType_en + ": " + resultDescription_en);
/* 354:321 */                 update.setFrenchMessage(period_fr + ", " + resultType_fr + ": " + resultDescription_fr);
/* 355:    */               }
/* 356:324 */               type = null;
/* 357:325 */               prd = null;
/* 358:    */             }
/* 359:326 */             else if (subelement.getName().equals("participant"))
/* 360:    */             {
/* 361:328 */               Iterator sub_e = subelement.elementIterator();
/* 362:330 */               while (sub_e.hasNext())
/* 363:    */               {
/* 364:332 */                 subelement = (Element)sub_e.next();
/* 365:333 */                 if (subelement != null)
/* 366:    */                 {
/* 367:335 */                   attribute = subelement.attribute("trigger");
/* 368:336 */                   if ((attribute != null) && (attribute.getName().equals("trigger")) && (attribute.getValue().equals("yes")))
/* 369:    */                   {
/* 370:338 */                     attribute = subelement.attribute("period");
/* 371:    */                     
/* 372:340 */                     String period_en = english.getLiveScorePeriodCode(attribute.getValue());
/* 373:341 */                     String period_fr = french.getLiveScorePeriodCode(attribute.getValue());
/* 374:    */                     
/* 375:343 */                     attribute = subelement.attribute("type");
/* 376:    */                     
/* 377:345 */                     String resultType_en = english.getLiveScoreResultType(attribute.getValue());
/* 378:346 */                     String resultType_fr = french.getLiveScoreResultType(attribute.getValue());
/* 379:347 */                     update.setTrigger("LS_RT_" + attribute.getValue());
/* 380:    */                     
/* 381:349 */                     Element text = subelement.element("text");
/* 382:350 */                     String resultDescription_en = "";
/* 383:351 */                     String resultDescription_fr = "";
/* 384:352 */                     if (text != null)
/* 385:    */                     {
/* 386:354 */                       if ((attribute.getValue().equals("410")) || (attribute.getValue().equals("420")))
/* 387:    */                       {
/* 388:355 */                         String value = text.getText();
/* 389:356 */                         resultDescription_en = value.substring(0, value.lastIndexOf(".") + 1);
/* 390:357 */                         resultDescription_fr = value.substring(0, value.lastIndexOf(".") + 1);
/* 391:    */                       }
/* 392:    */                       else
/* 393:    */                       {
/* 394:359 */                         resultDescription_en = text.getText();
/* 395:360 */                         resultDescription_fr = text.getText();
/* 396:    */                       }
/* 397:362 */                       text = null;
/* 398:    */                     }
/* 399:364 */                     else if (description.equalsIgnoreCase("Changed status"))
/* 400:    */                     {
/* 401:366 */                       resultDescription_en = english.getLiveScoreStatusCode(subelement.getText());
/* 402:367 */                       resultDescription_fr = french.getLiveScoreStatusCode(subelement.getText());
/* 403:    */                     }
/* 404:    */                     else
/* 405:    */                     {
/* 406:370 */                       resultDescription_en = subelement.getText();
/* 407:371 */                       resultDescription_fr = subelement.getText();
/* 408:    */                     }
/* 409:375 */                     update.setEnglishMessage(period_en + ", " + resultType_en + ": " + resultDescription_en);
/* 410:376 */                     update.setFrenchMessage(period_fr + ", " + resultType_fr + ": " + resultDescription_fr);
/* 411:    */                   }
/* 412:378 */                   subelement = null;
/* 413:    */                 }
/* 414:    */               }
/* 415:    */             }
/* 416:    */           }
/* 417:383 */           e = null;
/* 418:384 */           participantiterations++;
/* 419:    */         }
/* 420:386 */         innerelement = null;
/* 421:387 */         attribute = null;
/* 422:    */       }
/* 423:389 */       update.setScores(scores);
/* 424:390 */       update.setParticipants(participants);
/* 425:    */     }
/* 426:    */     catch (Exception e)
/* 427:    */     {
/* 428:392 */       update = null;
/* 429:    */     }
/* 430:394 */     return update;
/* 431:    */   }
/* 432:    */   
/* 433:    */   private String createMessage(LiveScoreUpdate obj, Feedback language)
/* 434:    */   {
/* 435:398 */     String msg = "";
/* 436:    */     
/* 437:    */ 
/* 438:    */ 
/* 439:    */ 
/* 440:    */ 
/* 441:    */ 
/* 442:    */ 
/* 443:    */ 
/* 444:    */ 
/* 445:    */ 
/* 446:    */ 
/* 447:    */ 
/* 448:    */ 
/* 449:    */ 
/* 450:    */ 
/* 451:414 */     String country = obj.getCountryName();
/* 452:415 */     String leagueName = obj.getLeagueName();
/* 453:416 */     String eventName = obj.getEventName();
/* 454:417 */     String currentScore = language.getValue("CURRENT_SCORE") + ": " + (String)obj.getParticipants().get(0) + ": " + (String)obj.getScores().get(0) + " " + (String)obj.getParticipants().get(1) + ": " + (String)obj.getScores().get(1);
/* 455:    */     
/* 456:    */ 
/* 457:420 */     String message = "";
/* 458:421 */     if (language.getLanguage().equals("fr")) {
/* 459:422 */       message = obj.getFrenchMessage();
/* 460:    */     } else {
/* 461:424 */       message = obj.getEnglishMessage();
/* 462:    */     }
/* 463:427 */     if ((eventName != null) && (!eventName.equals(""))) {
/* 464:427 */       msg = msg + eventName + ":\r\n";
/* 465:    */     }
/* 466:428 */     if ((message != null) && (!message.equals(""))) {
/* 467:428 */       msg = msg + message + ":\r\n";
/* 468:    */     }
/* 469:429 */     if ((currentScore != null) && (!currentScore.equals(""))) {
/* 470:429 */       msg = msg + currentScore;
/* 471:    */     }
/* 472:432 */     if ((leagueName != null) && (!leagueName.equals("")) && (new String(msg + leagueName).length() <= 160)) {
/* 473:432 */       msg = leagueName + ":\r\n" + msg;
/* 474:    */     }
/* 475:433 */     if ((country != null) && (!country.equals("")) && (new String(msg + country).length() <= 160)) {
/* 476:433 */       msg = country + ":\r\n" + msg;
/* 477:    */     }
/* 478:435 */     return msg;
/* 479:    */   }
/* 480:    */   
/* 481:    */   private String filterMessage(String message)
/* 482:    */   {
/* 483:439 */     message = message.replaceAll("'", "'");
/* 484:440 */     message = message.replaceAll("`", "'");
/* 485:441 */     message = message.replaceAll("'", "'");
/* 486:442 */     return message;
/* 487:    */   }
/* 488:    */   
/* 489:    */   private void sendUpdate(String serverUrl, String accountId, String updateId)
/* 490:    */     throws HttpException, IOException, Exception
/* 491:    */   {
/* 492:447 */     String url = serverUrl + "sendlivescoreupdate?acctId=" + accountId + "&updateId=" + updateId;
/* 493:    */     
/* 494:449 */     HttpClient client = new HttpClient();
/* 495:450 */     GetMethod httpGETFORM = new GetMethod(url);
/* 496:451 */     String resp = "";
/* 497:    */     try
/* 498:    */     {
/* 499:454 */       client.executeMethod(httpGETFORM);
/* 500:    */     }
/* 501:    */     catch (HttpException e)
/* 502:    */     {
/* 503:456 */       resp = "5001: " + e.getMessage();
/* 504:    */       
/* 505:458 */       System.out.println("error response: " + resp);
/* 506:    */     }
/* 507:    */     catch (IOException e)
/* 508:    */     {
/* 509:461 */       resp = "5002: " + e.getMessage();
/* 510:    */       
/* 511:463 */       System.out.println("error response: " + resp);
/* 512:    */     }
/* 513:    */     catch (Exception e)
/* 514:    */     {
/* 515:467 */       System.out.println("error response: " + e.getMessage());
/* 516:    */     }
/* 517:    */     finally
/* 518:    */     {
/* 519:471 */       httpGETFORM.releaseConnection();
/* 520:472 */       client = null;
/* 521:473 */       httpGETFORM = null;
/* 522:    */     }
/* 523:    */   }
/* 524:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.infoserver.livescore.serviceinterfaces.receivefeed
 * JD-Core Version:    0.7.0.1
 */