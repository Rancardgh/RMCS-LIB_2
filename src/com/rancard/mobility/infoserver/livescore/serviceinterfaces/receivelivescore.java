/*    1:     */ package com.rancard.mobility.infoserver.livescore.serviceinterfaces;
/*    2:     */ 
/*    3:     */ import com.rancard.common.Feedback;
/*    4:     */ import com.rancard.common.uidGen;
/*    5:     */ import com.rancard.mobility.infoserver.livescore.LiveScoreFixture;
/*    6:     */ import com.rancard.mobility.infoserver.livescore.LiveScoreServiceManager;
/*    7:     */ import com.rancard.mobility.infoserver.livescore.LiveScoreUpdate;
/*    8:     */ import java.io.BufferedReader;
/*    9:     */ import java.io.IOException;
/*   10:     */ import java.io.InputStream;
/*   11:     */ import java.io.InputStreamReader;
/*   12:     */ import java.io.PrintStream;
/*   13:     */ import java.io.PrintWriter;
/*   14:     */ import java.net.URLDecoder;
/*   15:     */ import java.sql.Timestamp;
/*   16:     */ import java.util.ArrayList;
/*   17:     */ import java.util.Arrays;
/*   18:     */ import java.util.Calendar;
/*   19:     */ import java.util.Date;
/*   20:     */ import java.util.HashMap;
/*   21:     */ import java.util.Iterator;
/*   22:     */ import java.util.List;
/*   23:     */ import java.util.Set;
/*   24:     */ import javax.servlet.ServletContext;
/*   25:     */ import javax.servlet.ServletException;
/*   26:     */ import javax.servlet.http.HttpServlet;
/*   27:     */ import javax.servlet.http.HttpServletRequest;
/*   28:     */ import javax.servlet.http.HttpServletResponse;
/*   29:     */ import org.apache.commons.httpclient.HttpClient;
/*   30:     */ import org.apache.commons.httpclient.HttpException;
/*   31:     */ import org.apache.commons.httpclient.methods.GetMethod;
/*   32:     */ import org.dom4j.Attribute;
/*   33:     */ import org.dom4j.Document;
/*   34:     */ import org.dom4j.DocumentHelper;
/*   35:     */ import org.dom4j.Element;
/*   36:     */ 
/*   37:     */ public class receivelivescore
/*   38:     */   extends HttpServlet
/*   39:     */ {
/*   40:     */   private static final String CONTENT_TYPE = "text/html";
/*   41:  21 */   private String mode = "";
/*   42:  22 */   private Feedback feedback_en = null;
/*   43:  23 */   private Feedback feedback_fr = null;
/*   44:  24 */   private String feedType = "";
/*   45:  30 */   private HashMap<String, ArrayList> updateManager = null;
/*   46:  31 */   private HashMap<String, Date> expiredGamesManager = null;
/*   47:     */   
/*   48:     */   public void init()
/*   49:     */     throws ServletException
/*   50:     */   {
/*   51:  42 */     this.feedback_en = ((Feedback)getServletContext().getAttribute("feedback_en"));
/*   52:  43 */     if (this.feedback_en == null) {
/*   53:     */       try
/*   54:     */       {
/*   55:  45 */         this.feedback_en = new Feedback();
/*   56:     */       }
/*   57:     */       catch (Exception e) {}
/*   58:     */     }
/*   59:  49 */     this.feedback_fr = ((Feedback)getServletContext().getAttribute("feedback_fr"));
/*   60:  50 */     if (this.feedback_fr == null) {
/*   61:     */       try
/*   62:     */       {
/*   63:  52 */         this.feedback_fr = new Feedback();
/*   64:     */       }
/*   65:     */       catch (Exception e) {}
/*   66:     */     }
/*   67:  58 */     this.updateManager = new HashMap();
/*   68:  59 */     this.expiredGamesManager = new HashMap();
/*   69:     */   }
/*   70:     */   
/*   71:     */   public void doGet(HttpServletRequest request, HttpServletResponse response)
/*   72:     */     throws ServletException, IOException
/*   73:     */   {
/*   74:  69 */     PrintWriter pw = null;
/*   75:  70 */     BufferedReader br = null;
/*   76:     */     
/*   77:  72 */     HttpClient client = null;
/*   78:  73 */     GetMethod httpGETFORM = null;
/*   79:     */     
/*   80:  75 */     String svrAddr = getServletContext().getInitParameter("contentServerUrl");
/*   81:  76 */     boolean dopost = true;
/*   82:  77 */     String resp = "";
/*   83:  78 */     ArrayList<LiveScoreUpdate> updateList = null;
/*   84:     */     
/*   85:  80 */     String feedURL = "";
/*   86:  81 */     String param_feedType = "0";
/*   87:  82 */     String type = request.getParameter("type");
/*   88:  83 */     String isLocalTest = request.getParameter("localTest");
/*   89:     */     
/*   90:  85 */     feedURL = "http://194.19.15.164/livescore/getXml2.php?username=Rancard&key=264dbf25ba132dbf183e3c30e5eee822&type=";
/*   91:  87 */     if ((type != null) && (!"".equals(type)))
/*   92:     */     {
/*   93:  88 */       param_feedType = type;
/*   94:  89 */       dopost = false;
/*   95:  92 */       if ((isLocalTest != null) && ("true".equals(isLocalTest)))
/*   96:     */       {
/*   97:  93 */         param_feedType = "";
/*   98:  94 */         if ("1".equals(type)) {
/*   99:  95 */           feedURL = svrAddr + "livescoretest/livescore_lang_delta.xml";
/*  100:     */         } else {
/*  101:  97 */           feedURL = svrAddr + "livescoretest/livescore_lang.xml";
/*  102:     */         }
/*  103:     */       }
/*  104:     */     }
/*  105:     */     try
/*  106:     */     {
/*  107: 109 */       String status = "error";
/*  108: 110 */       String reply = "";
/*  109: 111 */       String error = "";
/*  110: 112 */       String responseString = "";
/*  111: 113 */       pw = response.getWriter();
/*  112:     */       
/*  113: 115 */       InputStream responseBody = null;
/*  114: 117 */       if (dopost)
/*  115:     */       {
/*  116: 119 */         System.out.println(new Date() + ":Livescore Feed: HTTP Post request...");
/*  117:     */         
/*  118:     */ 
/*  119: 122 */         resp = request.getParameter("data");
/*  120:     */         
/*  121: 124 */         resp = resp == null ? "" : resp;
/*  122:     */       }
/*  123:     */       else
/*  124:     */       {
/*  125:     */         try
/*  126:     */         {
/*  127: 129 */           feedURL = feedURL + param_feedType;
/*  128:     */           
/*  129: 131 */           System.out.println(new Date() + ":Livescore Feed: HTTP Get request...\nScoreradarFeedURL:" + feedURL);
/*  130:     */           
/*  131: 133 */           client = new HttpClient();
/*  132: 134 */           httpGETFORM = new GetMethod(feedURL);
/*  133:     */           
/*  134:     */ 
/*  135: 137 */           client.executeMethod(httpGETFORM);
/*  136: 138 */           responseBody = httpGETFORM.getResponseBodyAsStream();
/*  137:     */           
/*  138: 140 */           br = new BufferedReader(new InputStreamReader(responseBody));
/*  139:     */           
/*  140: 142 */           String line = "";
/*  141: 148 */           while ((line = br.readLine()) != null) {
/*  142: 149 */             resp = resp + line + "\n";
/*  143:     */           }
/*  144:     */         }
/*  145:     */         catch (IOException e)
/*  146:     */         {
/*  147: 155 */           throw new Exception(e.getMessage());
/*  148:     */         }
/*  149:     */         catch (Exception e)
/*  150:     */         {
/*  151: 157 */           throw new Exception(e.getMessage());
/*  152:     */         }
/*  153:     */         finally
/*  154:     */         {
/*  155: 159 */           br.close();
/*  156:     */           
/*  157: 161 */           br = null;
/*  158:     */         }
/*  159:     */       }
/*  160: 166 */       String rawXML = resp;
/*  161: 167 */       if (resp.indexOf("%") != -1) {
/*  162: 168 */         rawXML = resp.substring(resp.indexOf("%"));
/*  163:     */       }
/*  164: 170 */       String processedXML = URLDecoder.decode(rawXML, "UTF-8");
/*  165: 174 */       if (!"".equals(resp))
/*  166:     */       {
/*  167: 175 */         pw.println("OK");
/*  168: 176 */         if (dopost) {
/*  169: 177 */           response.setStatus(200);
/*  170:     */         }
/*  171: 179 */         updateList = processResponse(processedXML);
/*  172:     */       }
/*  173:     */       else
/*  174:     */       {
/*  175: 181 */         pw.println("NO FEED RECEIVED");
/*  176: 182 */         if (dopost) {
/*  177: 183 */           response.setStatus(400);
/*  178:     */         }
/*  179: 185 */         System.out.println(new Date() + ":NO FEED RECEIVED from Scoreradar");
/*  180:     */       }
/*  181:     */     }
/*  182:     */     catch (HttpException e)
/*  183:     */     {
/*  184: 192 */       System.out.println(new Date() + ":HTTPException: " + e.getMessage());
/*  185:     */     }
/*  186:     */     catch (IOException e)
/*  187:     */     {
/*  188: 197 */       System.out.println(new Date() + ":IOException : " + e.getMessage());
/*  189:     */     }
/*  190:     */     catch (Exception e)
/*  191:     */     {
/*  192: 201 */       System.out.println(new Date() + ":ERROR: " + e.getMessage());
/*  193:     */     }
/*  194:     */     finally
/*  195:     */     {
/*  196: 204 */       if (!dopost)
/*  197:     */       {
/*  198: 207 */         httpGETFORM.releaseConnection();
/*  199: 208 */         client = null;
/*  200: 209 */         httpGETFORM = null;
/*  201:     */       }
/*  202:     */     }
/*  203: 217 */     LiveScoreUpdate update = null;
/*  204: 219 */     if (updateList != null) {
/*  205: 220 */       for (int i = 0; i < updateList.size(); i++)
/*  206:     */       {
/*  207: 222 */         update = (LiveScoreUpdate)updateList.get(i);
/*  208:     */         
/*  209:     */ 
/*  210: 225 */         System.out.println(new Date() + ":Received XML from Scoreradar for " + update.getLeagueName());
/*  211: 226 */         System.out.println(new Date() + ":Event ID " + update.getEventId());
/*  212: 227 */         System.out.println(new Date() + ":Event Update ID " + update.getUpdateId());
/*  213: 228 */         System.out.println(new Date() + ":Event Name " + update.getEventName());
/*  214: 229 */         System.out.println(new Date() + ":Event Status " + update.getEventStatus());
/*  215: 230 */         System.out.println(new Date() + ":Feed Type: " + update.getMode());
/*  216:     */         try
/*  217:     */         {
/*  218: 234 */           if (update != null)
/*  219:     */           {
/*  220: 237 */             LiveScoreFixture game = new LiveScoreFixture();
/*  221:     */             
/*  222: 239 */             LiveScoreFixture temp_game = LiveScoreServiceManager.viewFixture(update.getEventId());
/*  223:     */             
/*  224:     */ 
/*  225: 242 */             game.setCountryName(update.getCountryName());
/*  226:     */             
/*  227: 244 */             String eventdate = update.getEventDate().replaceAll("T", " ");
/*  228:     */             
/*  229: 246 */             game.setDate(eventdate);
/*  230: 247 */             game.setGameId(update.getEventId());
/*  231: 248 */             game.setLeagueName(update.getLeagueName());
/*  232: 249 */             game.setLeagueId(update.getLeagueId());
/*  233:     */             
/*  234: 251 */             game.setHomeTeam((String)update.getParticipants().get(0));
/*  235:     */             
/*  236: 253 */             String homescore = (String)update.getScores().get(0);
/*  237: 254 */             if ((homescore == null) || (homescore.equals(""))) {
/*  238: 254 */               homescore = "NA";
/*  239:     */             }
/*  240: 255 */             game.setHomeScore(homescore);
/*  241:     */             
/*  242: 257 */             game.setAwayTeam((String)update.getParticipants().get(1));
/*  243:     */             
/*  244: 259 */             String awayscore = (String)update.getScores().get(1);
/*  245: 260 */             if ((awayscore == null) || (awayscore.equals(""))) {
/*  246: 260 */               awayscore = "NA";
/*  247:     */             }
/*  248: 261 */             game.setAwayScore(awayscore);
/*  249: 263 */             if (update.getEventStatus().equals("0")) {
/*  250: 264 */               game.setStatus(0);
/*  251: 265 */             } else if ((update.getEventStatus().equals("100")) || (update.getEventStatus().equals("110")) || (update.getEventStatus().equals("120"))) {
/*  252: 266 */               game.setStatus(1);
/*  253: 267 */             } else if (isActive(update.getEventStatus())) {
/*  254: 268 */               game.setStatus(2);
/*  255: 269 */             } else if (update.getEventStatus().equals("80")) {
/*  256: 270 */               game.setStatus(4);
/*  257: 271 */             } else if (update.getEventStatus().equals("70")) {
/*  258: 272 */               game.setStatus(5);
/*  259: 273 */             } else if (update.getEventStatus().equals("60")) {
/*  260: 274 */               game.setStatus(6);
/*  261:     */             } else {
/*  262: 276 */               game.setStatus(3);
/*  263:     */             }
/*  264: 280 */             game.setEventNotifSent(temp_game.getEventNotifSent());
/*  265: 281 */             temp_game = null;
/*  266:     */             
/*  267:     */ 
/*  268: 284 */             Calendar now = Calendar.getInstance();
/*  269: 285 */             Calendar gameTime = Calendar.getInstance();
/*  270: 286 */             gameTime.setTime(new Date(Timestamp.valueOf(game.getDate()).getTime()));
/*  271: 287 */             gameTime.add(12, -10);
/*  272: 289 */             if ((now.before(gameTime)) || ("full".equalsIgnoreCase(update.getMode().trim())) || ("future".equalsIgnoreCase(update.getMode().trim())))
/*  273:     */             {
/*  274: 291 */               System.out.println(new Date() + ":Creating fixture for " + game.getHomeTeam() + " vs " + game.getAwayTeam());
/*  275: 292 */               LiveScoreServiceManager.createFixture(game);
/*  276: 293 */               game = null;
/*  277:     */             }
/*  278:     */             else
/*  279:     */             {
/*  280: 296 */               ArrayList pairing = LiveScoreServiceManager.getAccount_KeywordPairForService(game.getLeagueId());
/*  281: 297 */               if ((!update.getEventStatus().equals("0")) || (!update.getTrigger().equals(""))) {
/*  282: 299 */                 if (!isAlertStatus(update.getEventStatus()))
/*  283:     */                 {
/*  284: 301 */                   LiveScoreServiceManager.updateFixture(game);
/*  285:     */                 }
/*  286:     */                 else
/*  287:     */                 {
/*  288: 306 */                   String englishMessage = createMessage(update, this.feedback_en);
/*  289: 307 */                   String frenchMessage = createMessage(update, this.feedback_fr);
/*  290:     */                   
/*  291:     */ 
/*  292: 310 */                   boolean sendupdate = true;
/*  293: 312 */                   if (this.expiredGamesManager.containsKey(game.getGameId()))
/*  294:     */                   {
/*  295: 313 */                     System.out.println(new Date() + ":Closing update for " + game.getGameId() + " sent already. donnot send!");
/*  296: 314 */                     sendupdate = false;
/*  297:     */                   }
/*  298:     */                   else
/*  299:     */                   {
/*  300: 318 */                     ArrayList<Integer> updateHashList = null;
/*  301: 319 */                     updateHashList = (ArrayList)this.updateManager.get(game.getGameId());
/*  302: 320 */                     int updateMsgKey = englishMessage.hashCode();
/*  303: 323 */                     if (updateHashList == null)
/*  304:     */                     {
/*  305: 325 */                       System.out.println(new Date() + ":fixture (" + game.getGameId() + ") not in update updateManager. creating entry...");
/*  306:     */                       
/*  307:     */ 
/*  308: 328 */                       updateHashList = new ArrayList();
/*  309: 329 */                       updateHashList.add(Integer.valueOf(updateMsgKey));
/*  310: 330 */                       this.updateManager.put(game.getGameId(), updateHashList);
/*  311:     */                     }
/*  312: 334 */                     else if (updateHashList.contains(Integer.valueOf(updateMsgKey)))
/*  313:     */                     {
/*  314: 335 */                       sendupdate = false;
/*  315:     */                       
/*  316: 337 */                       System.out.println(new Date() + ":update with hashKey " + updateMsgKey + "already exists for gameId:" + game.getGameId() + ":  Donot transmit alert!");
/*  317: 338 */                       System.out.println(new Date() + ":current size of updateHashList for gameId:" + game.getGameId() + ": " + updateHashList.size());
/*  318:     */                     }
/*  319:     */                     else
/*  320:     */                     {
/*  321: 342 */                       System.out.println(new Date() + ":adding new update: hashKey=" + updateMsgKey + ": gameId=" + game.getGameId() + ": updateHashList size:" + updateHashList.size() + ". Transmit alert!");
/*  322: 343 */                       updateHashList.add(Integer.valueOf(updateMsgKey));
/*  323: 344 */                       this.updateManager.put(game.getGameId(), updateHashList);
/*  324:     */                       
/*  325: 346 */                       System.out.println(new Date() + ":size of updateHashList for gameId:" + game.getGameId() + " after adding new update: " + updateHashList.size());
/*  326:     */                     }
/*  327:     */                   }
/*  328: 355 */                   if (sendupdate)
/*  329:     */                   {
/*  330: 357 */                     update.setEnglishMessage(englishMessage);
/*  331: 358 */                     update.setFrenchMessage(frenchMessage);
/*  332:     */                     
/*  333:     */ 
/*  334: 361 */                     LiveScoreServiceManager.updateFixture(game);
/*  335: 364 */                     if (LiveScoreServiceManager.createUpdate(update) == true)
/*  336:     */                     {
/*  337: 365 */                       System.out.println(new Date() + ":created update record for " + update.getUpdateId());
/*  338: 366 */                       request.setAttribute("updateId", update.getUpdateId());
/*  339: 368 */                       for (int j = 0; j < pairing.size(); j++)
/*  340:     */                       {
/*  341: 369 */                         String pair = (String)pairing.get(j);
/*  342: 370 */                         String accountId = pair.split("-")[0];
/*  343:     */                         
/*  344: 372 */                         LiveScoreServiceManager.sendUpdate(svrAddr, accountId, update.getUpdateId());
/*  345: 373 */                         System.out.println(new Date() + ":>>>>>>>>>>>>>>>>>>>accountId: " + accountId + " >>>>>>>>>>>>>>>>>>>updateId: " + update.getUpdateId());
/*  346:     */                       }
/*  347:     */                     }
/*  348:     */                     else
/*  349:     */                     {
/*  350: 376 */                       System.out.println(new Date() + ":could NOT create update record for " + update.getUpdateId());
/*  351:     */                     }
/*  352:     */                   }
/*  353: 381 */                   if (game.getStatus() == 1)
/*  354:     */                   {
/*  355: 384 */                     System.out.println(new Date() + ":Event " + update.getEventName() + " with gameId:" + game.getGameId() + " has ended. removing entry from updateManager...");
/*  356: 385 */                     System.out.println(new Date() + ":No. of entries in updateManager before:" + this.updateManager.size());
/*  357:     */                     
/*  358: 387 */                     this.updateManager.remove(game.getGameId());
/*  359:     */                     
/*  360: 389 */                     System.out.println(new Date() + ":No. of entries in updateManager after:" + this.updateManager.size());
/*  361:     */                     
/*  362:     */ 
/*  363: 392 */                     this.expiredGamesManager.put(game.getGameId(), new Date());
/*  364: 393 */                     System.out.println(new Date() + ":No. of entries in expiredGamesManager:" + this.expiredGamesManager.size());
/*  365:     */                     
/*  366:     */ 
/*  367: 396 */                     LiveScoreServiceManager.updateSubscriptionStatus(game.getGameId(), 0, 1);
/*  368:     */                   }
/*  369:     */                 }
/*  370:     */               }
/*  371:     */             }
/*  372: 407 */             game = null;
/*  373:     */           }
/*  374:     */           else
/*  375:     */           {
/*  376: 411 */             System.out.println(new Date() + ":XML_PARSER_ERROR:");
/*  377: 412 */             throw new Exception("7000");
/*  378:     */           }
/*  379: 415 */           System.out.println("------------------------------------------------------------------------");
/*  380:     */         }
/*  381:     */         catch (Exception e)
/*  382:     */         {
/*  383: 417 */           System.out.println(new Date() + ":ERROR:" + e.getMessage());
/*  384: 418 */           pw.println(e.getMessage());
/*  385:     */         }
/*  386:     */         finally
/*  387:     */         {
/*  388: 420 */           if (pw != null) {
/*  389: 421 */             pw.close();
/*  390:     */           }
/*  391:     */           try
/*  392:     */           {
/*  393: 424 */             br.close();
/*  394:     */           }
/*  395:     */           catch (Exception e) {}
/*  396: 432 */           update = null;
/*  397:     */         }
/*  398:     */       }
/*  399:     */     }
/*  400: 440 */     updateList = null;
/*  401:     */     
/*  402:     */ 
/*  403: 443 */     manageExpiredGames();
/*  404:     */   }
/*  405:     */   
/*  406:     */   public void doPost(HttpServletRequest request, HttpServletResponse response)
/*  407:     */     throws ServletException, IOException
/*  408:     */   {
/*  409: 450 */     doGet(request, response);
/*  410:     */   }
/*  411:     */   
/*  412:     */   public void destroy() {}
/*  413:     */   
/*  414:     */   private void manageExpiredGames()
/*  415:     */   {
/*  416: 481 */     System.out.println(new Date() + ":managing expired games...");
/*  417: 482 */     System.out.println(new Date() + ":size of expiredGamesManager before:" + this.expiredGamesManager.size());
/*  418:     */     
/*  419: 484 */     Object[] keys = this.expiredGamesManager.keySet().toArray();
/*  420: 486 */     for (int i = 0; i < keys.length; i++)
/*  421:     */     {
/*  422: 487 */       String gameId = (String)keys[i];
/*  423:     */       
/*  424:     */ 
/*  425: 490 */       Date gameEndtime = (Date)this.expiredGamesManager.get(gameId);
/*  426:     */       
/*  427: 492 */       Date currentTime = new Date();
/*  428:     */       
/*  429: 494 */       long MILL_SEC_IN_1HOUR = 3600000L;
/*  430: 495 */       long EXPIRE_HOURS = 24L;
/*  431: 496 */       long numOfHours = 0L;
/*  432: 497 */       long timeDiffMillisc = 0L;
/*  433:     */       
/*  434:     */ 
/*  435:     */ 
/*  436: 501 */       timeDiffMillisc = currentTime.getTime() - gameEndtime.getTime();
/*  437: 502 */       numOfHours = timeDiffMillisc / 3600000L;
/*  438:     */       
/*  439:     */ 
/*  440: 505 */       System.out.println(new Date() + ":expired game management: gameId: " + gameId);
/*  441: 506 */       System.out.println(new Date() + ":expired game management: timeDiff in millisc: " + timeDiffMillisc);
/*  442: 507 */       System.out.println(new Date() + ":expired game management: timeDiff in hours: " + numOfHours);
/*  443: 509 */       if (numOfHours >= 24L)
/*  444:     */       {
/*  445: 511 */         System.out.println(new Date() + ":game " + gameId + " expired " + numOfHours + " hrs ago. flush...");
/*  446: 512 */         this.expiredGamesManager.remove(gameId);
/*  447:     */       }
/*  448: 515 */       currentTime = null;
/*  449:     */     }
/*  450: 517 */     System.out.println(new Date() + ":size of expiredGamesManager after:" + this.expiredGamesManager.size());
/*  451: 518 */     System.out.println(new Date() + ":expired game management done!");
/*  452:     */   }
/*  453:     */   
/*  454:     */   private ArrayList<LiveScoreUpdate> processResponse(String xmlDoc)
/*  455:     */   {
/*  456: 525 */     String description = "";
/*  457: 526 */     String score = "";
/*  458:     */     
/*  459:     */ 
/*  460: 529 */     String RMCS_CUSTOM_STATUS_SUBSTITUTION = "150";
/*  461: 530 */     String RMCS_CUSTOM_STATUS_BOOKING = "151";
/*  462: 531 */     String RMCS_CUSTOM_STATUS_GOAL = "152";
/*  463:     */     
/*  464: 533 */     String DEFAULT_RESULT_CODE = "200";
/*  465:     */     
/*  466:     */ 
/*  467: 536 */     String sportName = "";
/*  468: 537 */     String countryName = "";
/*  469: 538 */     String leagueName = "";
/*  470: 539 */     String leagueId = "";
/*  471: 540 */     String gameId = "";
/*  472: 541 */     String homeTeam = "";
/*  473: 542 */     String awayTeam = "";
/*  474: 543 */     String gameTime = "";
/*  475: 544 */     String status = "";
/*  476: 545 */     ArrayList<String> participants = null;
/*  477: 546 */     ArrayList<String> scores = null;
/*  478: 547 */     String lsTrigger = "";
/*  479: 548 */     ArrayList<LiveScoreUpdate> updateList = new ArrayList();
/*  480: 549 */     LiveScoreUpdate update = null;
/*  481: 550 */     String mode = "";
/*  482:     */     try
/*  483:     */     {
/*  484: 555 */       Document xml = DocumentHelper.parseText(xmlDoc);
/*  485: 556 */       Element rootNode = xml.getRootElement();
/*  486: 557 */       Element element = null;
/*  487:     */       
/*  488: 559 */       Attribute attribute = null;
/*  489: 560 */       int count = 0;
/*  490: 561 */       Iterator tempItr = null;
/*  491: 562 */       Element tempEl = null;
/*  492: 563 */       String language = "";
/*  493:     */       
/*  494:     */ 
/*  495:     */ 
/*  496:     */ 
/*  497:     */ 
/*  498: 569 */       System.out.println(new Date() + ":About to consume feed...");
/*  499: 574 */       if ((rootNode != null) && (rootNode.getName().equals("BetradarLivescoreData"))) {
/*  500: 575 */         mode = rootNode.attribute("type").getValue();
/*  501:     */       }
/*  502: 578 */       for (Iterator i = rootNode.elementIterator(); i.hasNext();)
/*  503:     */       {
/*  504: 579 */         System.out.println(new Date() + ":Processing starts...");
/*  505:     */         
/*  506: 581 */         element = (Element)i.next();
/*  507:     */         
/*  508: 583 */         sportName = element.element("Name").getText();
/*  509:     */         
/*  510:     */ 
/*  511:     */ 
/*  512: 587 */         Iterator j = element.elementIterator("Category");
/*  513: 589 */         while (j.hasNext())
/*  514:     */         {
/*  515: 590 */           Element subelement = (Element)j.next();
/*  516:     */           
/*  517:     */ 
/*  518: 593 */           tempItr = subelement.elementIterator("Name");
/*  519: 594 */           while (tempItr.hasNext())
/*  520:     */           {
/*  521: 595 */             tempEl = (Element)tempItr.next();
/*  522: 596 */             language = tempEl.attribute("language").getValue();
/*  523: 597 */             if ("en".equals(language)) {
/*  524: 598 */               countryName = tempEl.getText();
/*  525:     */             }
/*  526:     */           }
/*  527: 606 */           Iterator k = subelement.elementIterator("Tournament");
/*  528: 607 */           while (k.hasNext())
/*  529:     */           {
/*  530: 609 */             Element subelement2 = (Element)k.next();
/*  531:     */             
/*  532: 611 */             leagueId = subelement2.attribute("BetradarTournamentId").getValue();
/*  533:     */             
/*  534:     */ 
/*  535:     */ 
/*  536: 615 */             tempItr = subelement2.elementIterator("Name");
/*  537: 616 */             while (tempItr.hasNext())
/*  538:     */             {
/*  539: 617 */               tempEl = (Element)tempItr.next();
/*  540: 618 */               language = tempEl.attribute("language").getValue();
/*  541: 619 */               if ("en".equals(language)) {
/*  542: 620 */                 leagueName = tempEl.getText();
/*  543:     */               }
/*  544:     */             }
/*  545: 624 */             Iterator L = subelement2.elementIterator("Match");
/*  546: 626 */             while (L.hasNext())
/*  547:     */             {
/*  548: 627 */               count++;
/*  549: 628 */               Element subelement3 = (Element)L.next();
/*  550:     */               
/*  551:     */ 
/*  552: 631 */               int latestResultTime = 0;
/*  553: 632 */               String period_en = "";
/*  554: 633 */               String period_fr = "";
/*  555: 634 */               String resultType_en = "";
/*  556: 635 */               String resultType_fr = "";
/*  557: 636 */               String resultDescription_en = "";
/*  558: 637 */               String resultDescription_fr = "";
/*  559: 638 */               String resultCode = "200";
/*  560: 639 */               String score24_status = "";
/*  561:     */               
/*  562:     */ 
/*  563: 642 */               gameId = subelement3.attribute("Id").getValue();
/*  564: 643 */               gameTime = subelement3.element("MatchDate").getText();
/*  565:     */               
/*  566:     */ 
/*  567:     */ 
/*  568: 647 */               tempItr = subelement3.element("Team1").elementIterator();
/*  569: 649 */               while (tempItr.hasNext())
/*  570:     */               {
/*  571: 650 */                 tempEl = (Element)tempItr.next();
/*  572: 651 */                 language = tempEl.attribute("language").getValue();
/*  573: 652 */                 if ("en".equals(language)) {
/*  574: 653 */                   homeTeam = tempEl.getText();
/*  575:     */                 }
/*  576:     */               }
/*  577: 656 */               tempItr = subelement3.element("Team2").elementIterator();
/*  578: 657 */               while (tempItr.hasNext())
/*  579:     */               {
/*  580: 658 */                 tempEl = (Element)tempItr.next();
/*  581: 659 */                 language = tempEl.attribute("language").getValue();
/*  582: 660 */                 if ("en".equals(language)) {
/*  583: 661 */                   awayTeam = tempEl.getText();
/*  584:     */                 }
/*  585:     */               }
/*  586: 665 */               participants = new ArrayList();
/*  587: 666 */               participants.add(homeTeam);
/*  588: 667 */               participants.add(awayTeam);
/*  589:     */               
/*  590:     */ 
/*  591: 670 */               status = subelement3.element("Status").attribute("Code").getValue();
/*  592:     */               
/*  593:     */ 
/*  594:     */ 
/*  595: 674 */               Iterator m = subelement3.element("Scores").elementIterator();
/*  596: 675 */               scores = new ArrayList();
/*  597: 676 */               scores.add("NA");
/*  598: 677 */               scores.add("NA");
/*  599: 678 */               String homeScore = "";
/*  600: 679 */               String awayScore = "";
/*  601:     */               
/*  602: 681 */               Element innerElement = null;
/*  603: 682 */               while ((m != null) && (m.hasNext()))
/*  604:     */               {
/*  605: 683 */                 innerElement = (Element)m.next();
/*  606: 685 */                 if ("Current".equals(innerElement.attribute("type").getValue()))
/*  607:     */                 {
/*  608: 686 */                   homeScore = innerElement.element("Team1").getText();
/*  609: 687 */                   awayScore = innerElement.element("Team2").getText();
/*  610:     */                   
/*  611:     */ 
/*  612: 690 */                   scores.set(0, homeScore);
/*  613: 691 */                   scores.set(1, awayScore);
/*  614:     */                 }
/*  615:     */               }
/*  616: 701 */               m = subelement3.element("Goals").elementIterator();
/*  617:     */               
/*  618: 703 */               int currTime = 0;
/*  619: 704 */               while ((m != null) && (m.hasNext()))
/*  620:     */               {
/*  621: 705 */                 innerElement = (Element)m.next();
/*  622:     */                 
/*  623: 707 */                 currTime = Integer.parseInt(innerElement.element("Time").getText());
/*  624: 708 */                 if (currTime > latestResultTime)
/*  625:     */                 {
/*  626: 709 */                   latestResultTime = currTime;
/*  627:     */                   
/*  628:     */ 
/*  629: 712 */                   String player = innerElement.element("Player").getText();
/*  630: 713 */                   resultDescription_en = player + ", " + currTime + " min. " + homeScore + "-" + awayScore;
/*  631: 714 */                   resultDescription_fr = resultDescription_en;
/*  632:     */                   
/*  633:     */ 
/*  634: 717 */                   resultCode = "400";
/*  635: 720 */                   if (!isHighPriorityStatus(status)) {
/*  636: 721 */                     status = "152";
/*  637:     */                   }
/*  638:     */                 }
/*  639:     */               }
/*  640: 729 */               m = subelement3.element("Cards").elementIterator();
/*  641: 730 */               while ((m != null) && (m.hasNext()))
/*  642:     */               {
/*  643: 731 */                 innerElement = (Element)m.next();
/*  644:     */                 
/*  645: 733 */                 currTime = Integer.parseInt(innerElement.element("Time").getText());
/*  646: 734 */                 if (currTime > latestResultTime)
/*  647:     */                 {
/*  648: 735 */                   latestResultTime = currTime;
/*  649:     */                   
/*  650:     */ 
/*  651: 738 */                   String player = innerElement.element("Player").getText();
/*  652: 739 */                   resultDescription_en = player + ", " + currTime + " min.";
/*  653: 740 */                   resultDescription_fr = resultDescription_en;
/*  654:     */                   
/*  655:     */ 
/*  656: 743 */                   String cardType = innerElement.attribute("type").getValue();
/*  657: 744 */                   resultCode = cardType.equalsIgnoreCase("Red") ? "420" : "410";
/*  658: 747 */                   if (!isHighPriorityStatus(status)) {
/*  659: 748 */                     status = "151";
/*  660:     */                   }
/*  661:     */                 }
/*  662:     */               }
/*  663: 756 */               m = subelement3.element("Substitutions").elementIterator();
/*  664: 757 */               while ((m != null) && (m.hasNext()))
/*  665:     */               {
/*  666: 758 */                 innerElement = (Element)m.next();
/*  667:     */                 
/*  668: 760 */                 currTime = Integer.parseInt(innerElement.element("Time").getText());
/*  669: 761 */                 if (currTime > latestResultTime)
/*  670:     */                 {
/*  671: 762 */                   latestResultTime = currTime;
/*  672:     */                   
/*  673:     */ 
/*  674: 765 */                   String playerOut = innerElement.element("PlayerOut").getText();
/*  675: 766 */                   String playerIn = innerElement.element("PlayerIn").getText();
/*  676: 767 */                   int teamNo = Integer.parseInt(innerElement.element("PlayerTeam").getText());
/*  677: 768 */                   String playerTeam = teamNo == 1 ? homeTeam : awayTeam;
/*  678:     */                   
/*  679: 770 */                   resultDescription_en = "by " + playerTeam + ": " + playerIn + " in for " + playerOut + "," + currTime + " min.";
/*  680: 771 */                   resultDescription_fr = resultDescription_en;
/*  681:     */                   
/*  682:     */ 
/*  683: 774 */                   resultCode = "800";
/*  684: 777 */                   if (!isHighPriorityStatus(status)) {
/*  685: 778 */                     status = "150";
/*  686:     */                   }
/*  687:     */                 }
/*  688:     */               }
/*  689: 791 */               if ("20".equals(status))
/*  690:     */               {
/*  691: 792 */                 resultCode = "500";
/*  692: 793 */                 score24_status = "1100";
/*  693: 794 */                 resultDescription_en = this.feedback_en.getLiveScoreStatusCode(score24_status);
/*  694: 795 */                 resultDescription_fr = this.feedback_fr.getLiveScoreStatusCode(score24_status);
/*  695:     */               }
/*  696: 798 */               if ("31".equals(status))
/*  697:     */               {
/*  698: 799 */                 resultCode = "500";
/*  699: 800 */                 score24_status = "1910";
/*  700: 801 */                 resultDescription_en = this.feedback_en.getLiveScoreStatusCode(score24_status);
/*  701: 802 */                 resultDescription_fr = this.feedback_fr.getLiveScoreStatusCode(score24_status);
/*  702:     */               }
/*  703: 807 */               if (("100".equals(status)) || ("110".equals(status)) || ("120".equals(status)))
/*  704:     */               {
/*  705: 808 */                 resultCode = "500";
/*  706: 809 */                 score24_status = "1500";
/*  707: 810 */                 resultDescription_en = this.feedback_en.getLiveScoreStatusCode(score24_status);
/*  708: 811 */                 resultDescription_fr = this.feedback_fr.getLiveScoreStatusCode(score24_status);
/*  709:     */               }
/*  710: 814 */               resultType_en = this.feedback_en.getLiveScoreResultType(resultCode);
/*  711: 815 */               resultType_fr = this.feedback_fr.getLiveScoreResultType(resultCode);
/*  712:     */               
/*  713: 817 */               lsTrigger = "LS_RT_" + resultCode;
/*  714:     */               
/*  715: 819 */               String msg_en = "";
/*  716: 820 */               String msg_fr = "";
/*  717: 823 */               if (!"200".equals(resultCode))
/*  718:     */               {
/*  719: 825 */                 msg_en = resultType_en + ": " + resultDescription_en;
/*  720: 826 */                 msg_fr = resultType_fr + ": " + resultDescription_fr;
/*  721:     */               }
/*  722: 829 */               update = new LiveScoreUpdate();
/*  723:     */               
/*  724: 831 */               update.setCountryName(countryName);
/*  725: 832 */               update.setEventId(gameId);
/*  726: 833 */               update.setEventDate(gameTime);
/*  727: 834 */               update.setLeagueName(leagueName);
/*  728: 835 */               update.setLeagueId(leagueId);
/*  729: 836 */               update.setEventName(homeTeam + " - " + awayTeam);
/*  730: 837 */               update.setEventStatus(status);
/*  731:     */               
/*  732:     */ 
/*  733: 840 */               String uid = uidGen.getUId();
/*  734: 841 */               uid = uid + "-" + count;
/*  735: 842 */               update.setUpdateId(uid);
/*  736: 843 */               update.setTrigger(lsTrigger);
/*  737: 844 */               update.setScores(scores);
/*  738: 845 */               update.setParticipants(participants);
/*  739: 846 */               update.setEnglishMessage(msg_en);
/*  740: 847 */               update.setFrenchMessage(msg_fr);
/*  741: 848 */               update.setMode(mode);
/*  742:     */               
/*  743:     */ 
/*  744:     */ 
/*  745:     */ 
/*  746:     */ 
/*  747:     */ 
/*  748:     */ 
/*  749:     */ 
/*  750:     */ 
/*  751:     */ 
/*  752:     */ 
/*  753:     */ 
/*  754:     */ 
/*  755: 862 */               updateList.add(update);
/*  756:     */               
/*  757:     */ 
/*  758: 865 */               update = null;
/*  759:     */             }
/*  760:     */           }
/*  761:     */         }
/*  762:     */       }
/*  763: 878 */       System.out.println(new Date() + ":Finished Extracting Data from feed!");
/*  764:     */     }
/*  765:     */     catch (Exception e)
/*  766:     */     {
/*  767: 884 */       System.out.println(new Date() + ":Error Consuming Feed:" + e.getMessage());
/*  768: 885 */       update = null;
/*  769:     */     }
/*  770: 888 */     return updateList;
/*  771:     */   }
/*  772:     */   
/*  773:     */   private String getPeriod(String statusCode)
/*  774:     */   {
/*  775: 892 */     String periodCode = "";
/*  776:     */     
/*  777: 894 */     return periodCode;
/*  778:     */   }
/*  779:     */   
/*  780:     */   private void setFeedType(String feedType)
/*  781:     */   {
/*  782: 898 */     this.feedType = feedType;
/*  783:     */   }
/*  784:     */   
/*  785:     */   private String getFeedType()
/*  786:     */   {
/*  787: 901 */     return this.feedType;
/*  788:     */   }
/*  789:     */   
/*  790:     */   private String createMessage(LiveScoreUpdate obj, Feedback language)
/*  791:     */   {
/*  792: 906 */     String msg = "";
/*  793:     */     
/*  794:     */ 
/*  795:     */ 
/*  796:     */ 
/*  797:     */ 
/*  798:     */ 
/*  799:     */ 
/*  800:     */ 
/*  801:     */ 
/*  802:     */ 
/*  803:     */ 
/*  804:     */ 
/*  805:     */ 
/*  806:     */ 
/*  807:     */ 
/*  808: 922 */     String country = obj.getCountryName();
/*  809: 923 */     String leagueName = obj.getLeagueName();
/*  810: 924 */     String eventName = obj.getEventName();
/*  811: 925 */     String currentScore = language.getValue("CURRENT_SCORE") + ": " + (String)obj.getParticipants().get(0) + ": " + (String)obj.getScores().get(0) + " " + (String)obj.getParticipants().get(1) + ": " + (String)obj.getScores().get(1);
/*  812:     */     
/*  813:     */ 
/*  814: 928 */     String message = "";
/*  815: 929 */     if (language.getLanguage().equals("fr")) {
/*  816: 930 */       message = obj.getFrenchMessage();
/*  817:     */     } else {
/*  818: 932 */       message = obj.getEnglishMessage();
/*  819:     */     }
/*  820: 935 */     if ((eventName != null) && (!eventName.equals(""))) {
/*  821: 935 */       msg = msg + eventName + ":\r\n";
/*  822:     */     }
/*  823: 936 */     if ((message != null) && (!message.equals(""))) {
/*  824: 936 */       msg = msg + message + ":\r\n";
/*  825:     */     }
/*  826: 937 */     if ((currentScore != null) && (!currentScore.equals(""))) {
/*  827: 937 */       msg = msg + currentScore;
/*  828:     */     }
/*  829: 940 */     if ((leagueName != null) && (!leagueName.equals("")) && (new String(msg + leagueName).length() <= 160)) {
/*  830: 940 */       msg = leagueName + ":\r\n" + msg;
/*  831:     */     }
/*  832: 941 */     if ((country != null) && (!country.equals("")) && (new String(msg + country).length() <= 160)) {
/*  833: 941 */       msg = country + ":\r\n" + msg;
/*  834:     */     }
/*  835: 943 */     return msg;
/*  836:     */   }
/*  837:     */   
/*  838:     */   private String filterMessage(String message)
/*  839:     */   {
/*  840: 947 */     message = message.replaceAll("'", "'");
/*  841: 948 */     message = message.replaceAll("`", "'");
/*  842: 949 */     message = message.replaceAll("'", "'");
/*  843: 950 */     return message;
/*  844:     */   }
/*  845:     */   
/*  846:     */   private boolean isActive(String statusCode)
/*  847:     */   {
/*  848: 954 */     boolean status = false;
/*  849:     */     
/*  850: 956 */     String POSTPONED = "60";
/*  851: 957 */     String START_DELAYED = "61";
/*  852: 958 */     String CANCELlED = "70";
/*  853: 959 */     String INTERUPTED = "80";
/*  854: 960 */     String ABANDONED = "90";
/*  855: 961 */     String WALKOVER = "91";
/*  856: 962 */     String RETIRED = "92";
/*  857: 963 */     String ENDED = "100";
/*  858: 964 */     String AET = "110";
/*  859: 965 */     String AP = "120";
/*  860: 966 */     String NOT_STARTED = "0";
/*  861:     */     
/*  862: 968 */     String[] xl = { POSTPONED, START_DELAYED, CANCELlED, INTERUPTED, ABANDONED, WALKOVER, RETIRED, RETIRED, ENDED, AET, AP, NOT_STARTED };
/*  863:     */     
/*  864:     */ 
/*  865:     */ 
/*  866: 972 */     List inactiveList = Arrays.asList(xl);
/*  867: 975 */     if (!inactiveList.contains(statusCode)) {
/*  868: 976 */       status = true;
/*  869:     */     }
/*  870: 980 */     return status;
/*  871:     */   }
/*  872:     */   
/*  873:     */   private boolean isAlertStatus(String statusCode)
/*  874:     */   {
/*  875: 986 */     boolean status = true;
/*  876:     */     
/*  877:     */ 
/*  878: 989 */     String POSTPONED = "60";
/*  879: 990 */     String START_DELAYED = "61";
/*  880: 991 */     String CANCELlED = "70";
/*  881: 992 */     String INTERUPTED = "80";
/*  882: 993 */     String ABANDONED = "90";
/*  883: 994 */     String WALKOVER = "91";
/*  884: 995 */     String RETIRED = "92";
/*  885:     */     
/*  886:     */ 
/*  887:     */ 
/*  888: 999 */     String NOT_STARTED = "0";
/*  889:1000 */     String RMCS_CUSTOM_STATUS_SUBSTITUTION = "150";
/*  890:1001 */     String RMCS_CUSTOM_STATUS_BOOKING = "151";
/*  891:     */     
/*  892:1003 */     String[] xl = { POSTPONED, START_DELAYED, CANCELlED, INTERUPTED, ABANDONED, WALKOVER, RETIRED, RETIRED, NOT_STARTED, RMCS_CUSTOM_STATUS_BOOKING, RMCS_CUSTOM_STATUS_SUBSTITUTION };
/*  893:     */     
/*  894:     */ 
/*  895:     */ 
/*  896:1007 */     List nonAlertList = Arrays.asList(xl);
/*  897:1010 */     if (nonAlertList.contains(statusCode)) {
/*  898:1011 */       status = false;
/*  899:     */     }
/*  900:1015 */     return status;
/*  901:     */   }
/*  902:     */   
/*  903:     */   private boolean isHighPriorityStatus(String statusCode)
/*  904:     */   {
/*  905:1021 */     boolean status = false;
/*  906:     */     
/*  907:     */ 
/*  908:1024 */     String STARTED = "20";
/*  909:1025 */     String HALFTIME = "31";
/*  910:1026 */     String GAME_END1 = "100";
/*  911:1027 */     String GAME_END2 = "110";
/*  912:1028 */     String GAME_END3 = "120";
/*  913:     */     
/*  914:1030 */     String[] xl = { STARTED, HALFTIME, GAME_END1, GAME_END2, GAME_END3 };
/*  915:     */     
/*  916:     */ 
/*  917:1033 */     List allowList = Arrays.asList(xl);
/*  918:1036 */     if (allowList.contains(statusCode)) {
/*  919:1037 */       status = true;
/*  920:     */     }
/*  921:1042 */     return status;
/*  922:     */   }
/*  923:     */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.infoserver.livescore.serviceinterfaces.receivelivescore
 * JD-Core Version:    0.7.0.1
 */