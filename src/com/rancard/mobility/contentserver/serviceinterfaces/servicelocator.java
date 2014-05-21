/*   1:    */ package com.rancard.mobility.contentserver.serviceinterfaces;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import com.rancard.common.Feedback;
/*   5:    */ import com.rancard.mobility.contentprovider.User;
/*   6:    */ import com.rancard.mobility.contentserver.CPSite;
/*   7:    */ import com.rancard.mobility.infoserver.common.services.ServiceManager;
/*   8:    */ import com.rancard.mobility.infoserver.common.services.UserService;
/*   9:    */ import com.rancard.util.DefaultService;
/*  10:    */ import com.rancard.util.URLUTF8Encoder;
/*  11:    */ import java.io.IOException;
/*  12:    */ import java.io.PrintStream;
/*  13:    */ import java.io.PrintWriter;
/*  14:    */ import java.sql.Connection;
/*  15:    */ import java.sql.PreparedStatement;
/*  16:    */ import java.sql.ResultSet;
/*  17:    */ import java.sql.Timestamp;
/*  18:    */ import java.util.Arrays;
/*  19:    */ import java.util.Date;
/*  20:    */ import java.util.HashMap;
/*  21:    */ import java.util.List;
/*  22:    */ import javax.servlet.Filter;
/*  23:    */ import javax.servlet.FilterChain;
/*  24:    */ import javax.servlet.FilterConfig;
/*  25:    */ import javax.servlet.RequestDispatcher;
/*  26:    */ import javax.servlet.ServletContext;
/*  27:    */ import javax.servlet.ServletException;
/*  28:    */ import javax.servlet.ServletRequest;
/*  29:    */ import javax.servlet.ServletResponse;
/*  30:    */ import javax.servlet.http.HttpServlet;
/*  31:    */ import javax.servlet.http.HttpServletRequest;
/*  32:    */ import javax.servlet.http.HttpServletResponse;
/*  33:    */ import javax.servlet.http.HttpSession;
/*  34:    */ 
/*  35:    */ public class servicelocator
/*  36:    */   extends HttpServlet
/*  37:    */   implements Filter
/*  38:    */ {
/*  39:    */   private FilterConfig filterConfig;
/*  40: 19 */   private HashMap routingTable = new HashMap();
/*  41:    */   private static final String FROM = "RMCS";
/*  42:    */   public static final String BY_SHORTCODE = "1";
/*  43:    */   public static final String BY_PROVIDER = "2";
/*  44:    */   public static final String EX_NO_SERVICE = "1";
/*  45:    */   public static final String EX_HELP_SERVICE = "2";
/*  46:    */   String baseUrl;
/*  47:    */   
/*  48:    */   public void init(FilterConfig filterConfig)
/*  49:    */   {
/*  50: 29 */     this.filterConfig = filterConfig;
/*  51:    */     try
/*  52:    */     {
/*  53: 37 */       this.routingTable = ServiceManager.populateRoutingTable();
/*  54:    */     }
/*  55:    */     catch (Exception e) {}
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
/*  59:    */   {
/*  60: 47 */     ServletContext context = this.filterConfig.getServletContext();
/*  61:    */     
/*  62: 49 */     HttpServletRequest request = (HttpServletRequest)req;
/*  63: 50 */     HttpServletResponse response = (HttpServletResponse)res;
/*  64: 51 */     String s = request.getProtocol().toLowerCase();
/*  65: 52 */     s = s.substring(0, s.indexOf("/")).toLowerCase();
/*  66: 53 */     String baseUrl = s + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
/*  67:    */     try
/*  68:    */     {
/*  69: 56 */       req.setCharacterEncoding("UTF-8");
/*  70: 57 */       request.setCharacterEncoding("UTF-8");
/*  71:    */     }
/*  72:    */     catch (Exception e) {}
/*  73: 61 */     HttpSession session = request.getSession(true);
/*  74: 62 */     PrintWriter out = null;
/*  75:    */     try
/*  76:    */     {
/*  77: 64 */       out = response.getWriter();
/*  78:    */     }
/*  79:    */     catch (IOException ex1) {}
/*  80: 69 */     Feedback feedback = (Feedback)context.getAttribute("feedback_en");
/*  81: 70 */     if (feedback == null) {
/*  82:    */       try
/*  83:    */       {
/*  84: 72 */         feedback = new Feedback();
/*  85:    */       }
/*  86:    */       catch (Exception e) {}
/*  87:    */     }
/*  88: 77 */     String accountId = new String();
/*  89: 78 */     CPSite site = null;
/*  90: 79 */     User sp = null;
/*  91:    */     
/*  92: 81 */     String entireText = "";
/*  93: 82 */     String searchParam = "";
/*  94: 83 */     String defaultLang = "";
/*  95:    */     
/*  96:    */ 
/*  97: 86 */     String kw = request.getParameter("keyword");
/*  98: 87 */     String msgBody = request.getParameter("msg");
/*  99: 88 */     String dest = request.getParameter("dest");
/* 100: 89 */     String msisdn = request.getParameter("msisdn");
/* 101: 90 */     String date = request.getParameter("date");
/* 102: 91 */     String phoneId = request.getParameter("phoneId");
/* 103: 92 */     String ua = request.getParameter("ua");
/* 104: 93 */     String siteId = request.getParameter("siteId");
/* 105: 94 */     String regId = request.getParameter("regId");
/* 106: 95 */     String smscId = req.getParameter("smsc");
/* 107: 96 */     String timeSent = req.getParameter("time");
/* 108: 97 */     String flag = req.getParameter("routeBy");
/* 109: 98 */     String promoId = "";
/* 110: 99 */     String promoRespCode = "";
/* 111:100 */     String defaultSrvc = "";
/* 112:    */     
/* 113:    */ 
/* 114:103 */     msisdn = msisdn.replaceAll(" ", "");
/* 115:104 */     boolean msisdn_ok = checkMsisdn(msisdn);
/* 116:    */     
/* 117:106 */     System.out.println(new Date() + ":@com.rancard.mobility.contentserver.serviceinterfaces.servicelocator..");
/* 118:107 */     System.out.println(new Date() + ": Service reqest params: " + "keyword=" + kw + ", " + "msg=" + msgBody + ", " + "dest=" + dest + ", " + "msisdn=" + msisdn + ", " + "date=" + date + ", " + "phoneId=" + phoneId + ", " + "ua=" + ua + ", " + "siteId=" + siteId + ", " + "regId=" + regId + ", " + "smsc=" + smscId + ", " + "time=" + timeSent + ", " + "routeBy=" + flag + ", ");
/* 119:122 */     if (msgBody == null) {
/* 120:123 */       msgBody = "";
/* 121:    */     }
/* 122:125 */     if (dest == null) {
/* 123:126 */       dest = "";
/* 124:    */     }
/* 125:128 */     if (msisdn == null) {
/* 126:129 */       msisdn = "";
/* 127:    */     }
/* 128:131 */     if (date == null) {
/* 129:132 */       date = "";
/* 130:    */     }
/* 131:134 */     if (phoneId == null) {
/* 132:135 */       phoneId = "";
/* 133:    */     }
/* 134:137 */     if (ua == null) {
/* 135:138 */       ua = "";
/* 136:    */     }
/* 137:140 */     if (regId == null) {
/* 138:141 */       regId = "";
/* 139:    */     }
/* 140:143 */     if (smscId == null) {
/* 141:144 */       smscId = "";
/* 142:    */     }
/* 143:146 */     if (timeSent == null) {
/* 144:147 */       timeSent = "";
/* 145:    */     }
/* 146:149 */     if (siteId == null) {
/* 147:150 */       siteId = "";
/* 148:    */     }
/* 149:152 */     if (flag == null) {
/* 150:153 */       flag = "";
/* 151:    */     }
/* 152:155 */     if (kw == null) {
/* 153:156 */       kw = "";
/* 154:    */     }
/* 155:    */     try
/* 156:    */     {
/* 157:    */       try
/* 158:    */       {
/* 159:163 */         site = CPSite.viewSite(siteId);
/* 160:164 */         if ((site.getCpSiteId() == null) || (site.getCpSiteId().equals(""))) {
/* 161:165 */           throw new Exception("4001");
/* 162:    */         }
/* 163:167 */         if (!msisdn_ok) {
/* 164:168 */           throw new Exception("2000");
/* 165:    */         }
/* 166:170 */         accountId = site.getCpId();
/* 167:171 */         request.setAttribute("site_type", site.getSiteType());
/* 168:    */       }
/* 169:    */       catch (Exception e)
/* 170:    */       {
/* 171:174 */         if (!msisdn_ok) {
/* 172:175 */           throw new Exception("2000");
/* 173:    */         }
/* 174:177 */         throw new Exception("4001");
/* 175:    */       }
/* 176:    */       try
/* 177:    */       {
/* 178:181 */         sp = new User().viewDealer(accountId);
/* 179:182 */         defaultSrvc = sp.getDefaultService();
/* 180:    */         
/* 181:184 */         String sp_lang = "";
/* 182:185 */         if ((sp.getDefaultLanguage() == null) || (sp.getDefaultLanguage().equals(""))) {
/* 183:186 */           sp_lang = "en";
/* 184:    */         } else {
/* 185:188 */           sp_lang = sp.getDefaultLanguage();
/* 186:    */         }
/* 187:190 */         feedback = (Feedback)context.getAttribute("feedback_" + sp_lang);
/* 188:191 */         if (feedback == null) {
/* 189:192 */           feedback = new Feedback();
/* 190:    */         }
/* 191:194 */         request.setAttribute("default_lang", feedback.getLanguage());
/* 192:    */       }
/* 193:    */       catch (Exception e)
/* 194:    */       {
/* 195:196 */         throw new Exception("4001");
/* 196:    */       }
/* 197:199 */       if (flag.equals("1")) {
/* 198:201 */         searchParam = dest.substring(dest.indexOf("+") + 1);
/* 199:202 */       } else if (flag.equals("2")) {
/* 200:204 */         searchParam = accountId;
/* 201:    */       } else {
/* 202:206 */         searchParam = kw;
/* 203:    */       }
/* 204:209 */       if (searchParam == null) {
/* 205:210 */         throw new Exception("10001");
/* 206:    */       }
/* 207:214 */       if ((siteId == null) || (siteId.equals(""))) {
/* 208:215 */         throw new Exception("4001");
/* 209:    */       }
/* 210:219 */       UserService srvc = null;
/* 211:220 */       String serviceExeptionFlag = "1";
/* 212:    */       try
/* 213:    */       {
/* 214:222 */         srvc = ServiceManager.viewService(searchParam, accountId);
/* 215:223 */         if ((srvc.getKeyword() == null) || (srvc.getKeyword().equals("")))
/* 216:    */         {
/* 217:226 */           if (sp.getDefaultService().startsWith("HELP"))
/* 218:    */           {
/* 219:227 */             serviceExeptionFlag = "2";
/* 220:228 */             throw new Exception(DefaultService.getHelp(accountId, msisdn, dest, searchParam));
/* 221:    */           }
/* 222:231 */           serviceExeptionFlag = "1";
/* 223:232 */           throw new Exception("10001");
/* 224:    */         }
/* 225:235 */         request.setAttribute("thisService", srvc);
/* 226:    */       }
/* 227:    */       catch (Exception e)
/* 228:    */       {
/* 229:237 */         if (serviceExeptionFlag.equals("2")) {
/* 230:238 */           throw new Exception(e.getMessage());
/* 231:    */         }
/* 232:240 */         throw new Exception("10001");
/* 233:    */       }
/* 234:245 */       if ((srvc.getAllowedSiteTypes() != null) && (!srvc.getAllowedSiteTypes().equals("")))
/* 235:    */       {
/* 236:246 */         List allowedSites = Arrays.asList(srvc.getAllowedSiteTypes().split(","));
/* 237:248 */         if (!allowedSites.contains("" + site.getSiteType()))
/* 238:    */         {
/* 239:250 */           System.out.println(new Date() + ": requesting site (" + site.getCpSiteId() + ") not in allowed list.");
/* 240:251 */           throw new Exception("4001");
/* 241:    */         }
/* 242:255 */         if (site.getSiteType().equals("2"))
/* 243:    */         {
/* 244:256 */           String allowedShortcodes = srvc.getAllowedShortcodes();
/* 245:257 */           String tempDest = dest.substring(dest.indexOf("+") + 1);
/* 246:259 */           if ((allowedShortcodes != null) && (!allowedShortcodes.equals("")))
/* 247:    */           {
/* 248:260 */             List as = Arrays.asList(allowedShortcodes.split(","));
/* 249:262 */             if (!as.contains(tempDest))
/* 250:    */             {
/* 251:264 */               System.out.println(new Date() + ": requested shortcode (" + tempDest + ") not in allowed list.");
/* 252:    */               
/* 253:266 */               throw new Exception("10003");
/* 254:    */             }
/* 255:    */           }
/* 256:    */         }
/* 257:    */       }
/* 258:272 */       String acknowledgement = srvc.getDefaultMessage();
/* 259:    */       
/* 260:274 */       request.setAttribute("acctId", accountId);
/* 261:275 */       request.setAttribute("ack", acknowledgement);
/* 262:276 */       request.setAttribute("cmd", srvc.getCommand());
/* 263:277 */       request.setAttribute("attr_keyword", searchParam);
/* 264:    */       
/* 265:    */ 
/* 266:280 */       String srvcUrl = (String)this.routingTable.get(srvc.getServiceType());
/* 267:281 */       if ((srvcUrl == null) || (srvcUrl.equals("")))
/* 268:    */       {
/* 269:283 */         System.out.println(new Date() + ": no URL (routing) found for requested service (" + searchParam + ", " + accountId + ")");
/* 270:    */         
/* 271:285 */         throw new Exception("10002");
/* 272:    */       }
/* 273:289 */       RequestDispatcher dispatch = null;
/* 274:    */       try
/* 275:    */       {
/* 276:291 */         dispatch = request.getRequestDispatcher(srvcUrl);
/* 277:    */       }
/* 278:    */       catch (Exception e)
/* 279:    */       {
/* 280:293 */         throw new Exception("8000");
/* 281:    */       }
/* 282:296 */       dispatch.include(request, response);
/* 283:    */       
/* 284:298 */       request.setAttribute("dfltMsg", "");
/* 285:    */       
/* 286:    */ 
/* 287:301 */       promoId = (String)request.getAttribute("promoId");
/* 288:302 */       promoRespCode = (String)request.getAttribute("promoRespCode");
/* 289:305 */       if ((request.getAttribute("x-kannel-header-from") != null) && (!((String)request.getAttribute("x-kannel-header-from")).equals("")))
/* 290:    */       {
/* 291:306 */         response.addHeader("X-Kannel-From", (String)request.getAttribute("x-kannel-header-from"));
/* 292:    */       }
/* 293:307 */       else if ((srvc.getServiceResponseSender() != null) && (!srvc.getServiceResponseSender().equals("")))
/* 294:    */       {
/* 295:308 */         System.out.println(new Date() + ": Setting X-Kannel-From header (" + srvc.getServiceResponseSender() + ")");
/* 296:309 */         response.addHeader("X-Kannel-From", srvc.getServiceResponseSender());
/* 297:    */       }
/* 298:313 */       if ((request.getAttribute("x-kannel-header-binfo") != null) && (!((String)request.getAttribute("x-kannel-header-binfo")).equals(""))) {
/* 299:314 */         response.addHeader("X-Kannel-BInfo", (String)request.getAttribute("x-kannel-header-binfo"));
/* 300:    */       }
/* 301:317 */       if ((request.getAttribute("X-Kannel-Coding") != null) && (!((String)request.getAttribute("X-Kannel-Coding")).equals("")))
/* 302:    */       {
/* 303:318 */         response.addHeader("X-Kannel-Coding", (String)request.getAttribute("X-Kannel-Coding"));
/* 304:319 */         response.addHeader("Content-Type", "text/html;charset=UTF-8");
/* 305:    */       }
/* 306:322 */       filterChain.doFilter(request, response);
/* 307:    */     }
/* 308:    */     catch (Exception e)
/* 309:    */     {
/* 310:325 */       String message = "";
/* 311:    */       try
/* 312:    */       {
/* 313:327 */         if (site.getSiteType().equals("2"))
/* 314:    */         {
/* 315:328 */           message = feedback.getUserFriendlyDescription(e.getMessage());
/* 316:330 */           if ((message == null) || (message.equals(""))) {
/* 317:331 */             message = feedback.getValue(e.getMessage());
/* 318:    */           }
/* 319:    */         }
/* 320:    */         else
/* 321:    */         {
/* 322:334 */           message = feedback.formDefaultMessage(e.getMessage());
/* 323:    */         }
/* 324:336 */         if ((message == null) || (message.equals(""))) {
/* 325:337 */           message = e.getMessage();
/* 326:    */         }
/* 327:341 */         String insertions = "shortcode=" + dest;
/* 328:342 */         message = URLUTF8Encoder.doMessageEscaping(insertions, message);
/* 329:    */         
/* 330:    */ 
/* 331:    */ 
/* 332:346 */         System.out.println(new Date() + ": error: " + message + ":" + e.getMessage());
/* 333:    */         
/* 334:    */ 
/* 335:349 */         request.setAttribute("dfltMsg", message);
/* 336:350 */         filterChain.doFilter(request, response);
/* 337:    */       }
/* 338:    */       catch (ServletException sx)
/* 339:    */       {
/* 340:352 */         this.filterConfig.getServletContext().log(sx.getMessage());
/* 341:    */       }
/* 342:    */       catch (IOException iox)
/* 343:    */       {
/* 344:354 */         this.filterConfig.getServletContext().log(iox.getMessage());
/* 345:    */       }
/* 346:    */       catch (Exception ex)
/* 347:    */       {
/* 348:356 */         this.filterConfig.getServletContext().log(ex.getMessage());
/* 349:    */       }
/* 350:    */     }
/* 351:    */     finally
/* 352:    */     {
/* 353:362 */       String fwdReqKw = (String)request.getAttribute("log_fwdReq_kw");
/* 354:    */       try
/* 355:    */       {
/* 356:367 */         if (kw.equals("")) {
/* 357:368 */           entireText = msgBody;
/* 358:    */         } else {
/* 359:370 */           entireText = kw + " " + msgBody;
/* 360:    */         }
/* 361:374 */         logServiceRequest(searchParam, accountId, msisdn, dest, timeSent, entireText, siteId, smscId, fwdReqKw, promoId, promoRespCode);
/* 362:    */       }
/* 363:    */       catch (Exception ex)
/* 364:    */       {
/* 365:376 */         log(ex.getMessage());
/* 366:    */       }
/* 367:    */     }
/* 368:    */   }
/* 369:    */   
/* 370:    */   public void logServiceRequest(String keyword, String accountId, String origin, String dest, String timeSent, String msg, String siteId, String smscId, String fwd_req_kw)
/* 371:    */     throws Exception
/* 372:    */   {
/* 373:385 */     ResultSet rs = null;
/* 374:386 */     Connection con = null;
/* 375:387 */     PreparedStatement prepstat = null;
/* 376:    */     
/* 377:    */ 
/* 378:390 */     System.out.println(new Date() + ": updating service_usage_log with request details..");
/* 379:391 */     System.out.println(new Date() + ": service_usage_log details: " + "keyword=" + keyword + ", " + "account_id=" + accountId + ", " + "origin=" + origin + ", " + "dest=" + dest + ", " + "timeSent=" + ("".equals(timeSent) ? new Date() : timeSent) + ", " + "msg=" + msg + ", " + "siteId=" + siteId + ", " + "smsc=" + smscId + ", " + "fwd_req_kw=" + fwd_req_kw);
/* 380:402 */     if ((fwd_req_kw != null) && (!"".equals(fwd_req_kw))) {
/* 381:403 */       keyword = fwd_req_kw;
/* 382:    */     }
/* 383:    */     try
/* 384:    */     {
/* 385:407 */       con = DConnect.getConnection();
/* 386:    */       
/* 387:409 */       String query = "insert into service_usage_log (keyword, account_id, to_number, time_received, from_number, msg, site_id, smscId, time_sent) values (?,?,?,?,?,?,?,?,?)";
/* 388:    */       
/* 389:411 */       prepstat = con.prepareStatement(query);
/* 390:    */       
/* 391:413 */       prepstat.setString(1, keyword);
/* 392:414 */       prepstat.setString(2, accountId);
/* 393:415 */       prepstat.setString(3, dest);
/* 394:416 */       prepstat.setTimestamp(4, new Timestamp(new Date().getTime()));
/* 395:417 */       prepstat.setString(5, origin);
/* 396:418 */       prepstat.setString(6, msg);
/* 397:419 */       prepstat.setString(7, siteId);
/* 398:420 */       prepstat.setString(8, smscId);
/* 399:    */       try
/* 400:    */       {
/* 401:422 */         prepstat.setTimestamp(9, Timestamp.valueOf(timeSent));
/* 402:    */       }
/* 403:    */       catch (Exception e)
/* 404:    */       {
/* 405:424 */         prepstat.setTimestamp(9, new Timestamp(new Date().getTime()));
/* 406:    */       }
/* 407:428 */       prepstat.execute();
/* 408:    */       
/* 409:    */ 
/* 410:431 */       System.out.println(new Date() + ": service_usage_log updated successfully!");
/* 411:    */     }
/* 412:    */     catch (Exception ex)
/* 413:    */     {
/* 414:435 */       if (con != null)
/* 415:    */       {
/* 416:    */         try
/* 417:    */         {
/* 418:437 */           con.close();
/* 419:    */         }
/* 420:    */         catch (Exception ex1)
/* 421:    */         {
/* 422:439 */           log(ex.getMessage());
/* 423:    */         }
/* 424:441 */         con = null;
/* 425:    */       }
/* 426:444 */       System.out.println(new Date() + ": error updating service_usage_log: " + ex.getMessage());
/* 427:    */     }
/* 428:    */     finally
/* 429:    */     {
/* 430:447 */       if (rs != null)
/* 431:    */       {
/* 432:    */         try
/* 433:    */         {
/* 434:449 */           rs.close();
/* 435:    */         }
/* 436:    */         catch (Exception e)
/* 437:    */         {
/* 438:451 */           log(e.getMessage());
/* 439:    */         }
/* 440:453 */         rs = null;
/* 441:    */       }
/* 442:455 */       if (prepstat != null)
/* 443:    */       {
/* 444:    */         try
/* 445:    */         {
/* 446:457 */           prepstat.close();
/* 447:    */         }
/* 448:    */         catch (Exception e)
/* 449:    */         {
/* 450:459 */           log(e.getMessage());
/* 451:    */         }
/* 452:462 */         prepstat = null;
/* 453:    */       }
/* 454:464 */       if (con != null)
/* 455:    */       {
/* 456:    */         try
/* 457:    */         {
/* 458:466 */           con.close();
/* 459:    */         }
/* 460:    */         catch (Exception e)
/* 461:    */         {
/* 462:468 */           log(e.getMessage());
/* 463:    */         }
/* 464:471 */         con = null;
/* 465:    */       }
/* 466:    */     }
/* 467:    */   }
/* 468:    */   
/* 469:    */   public void logServiceRequest(String keyword, String accountId, String origin, String dest, String timeSent, String msg, String siteId, String smscId, String fwd_req_kw, String promoId, String promoRespCode)
/* 470:    */     throws Exception
/* 471:    */   {
/* 472:479 */     ResultSet rs = null;
/* 473:480 */     Connection con = null;
/* 474:481 */     PreparedStatement prepstat = null;
/* 475:    */     
/* 476:    */ 
/* 477:484 */     System.out.println(new Date() + ": updating service_usage_log with request details..");
/* 478:485 */     System.out.println(new Date() + ": service_usage_log details: " + "keyword=" + keyword + ", " + "account_id=" + accountId + ", " + "origin=" + origin + ", " + "dest=" + dest + ", " + "timeSent=" + ("".equals(timeSent) ? new Date() : timeSent) + ", " + "msg=" + msg + ", " + "promoId=" + promoId + ", " + "promoRespCode=" + promoRespCode + ", " + "siteId=" + siteId + ", " + "smsc=" + smscId + ", " + "fwd_req_kw=" + fwd_req_kw);
/* 479:498 */     if ((fwd_req_kw != null) && (!"".equals(fwd_req_kw))) {
/* 480:499 */       keyword = fwd_req_kw;
/* 481:    */     }
/* 482:    */     try
/* 483:    */     {
/* 484:503 */       con = DConnect.getConnection();
/* 485:    */       
/* 486:505 */       String query = "insert into service_usage_log (keyword, account_id, to_number, time_received, from_number, msg, site_id, smscId, time_sent,promo_id,promo_response_code) values (?,?,?,?,?,?,?,?,?,?,?)";
/* 487:    */       
/* 488:507 */       prepstat = con.prepareStatement(query);
/* 489:    */       
/* 490:509 */       prepstat.setString(1, keyword);
/* 491:510 */       prepstat.setString(2, accountId);
/* 492:511 */       prepstat.setString(3, dest);
/* 493:512 */       prepstat.setTimestamp(4, new Timestamp(new Date().getTime()));
/* 494:513 */       prepstat.setString(5, origin);
/* 495:514 */       prepstat.setString(6, msg);
/* 496:515 */       prepstat.setString(7, siteId);
/* 497:516 */       prepstat.setString(8, smscId);
/* 498:    */       try
/* 499:    */       {
/* 500:518 */         prepstat.setTimestamp(9, Timestamp.valueOf(timeSent));
/* 501:    */       }
/* 502:    */       catch (Exception e)
/* 503:    */       {
/* 504:520 */         prepstat.setTimestamp(9, new Timestamp(new Date().getTime()));
/* 505:    */       }
/* 506:522 */       prepstat.setString(10, promoId);
/* 507:523 */       prepstat.setString(11, promoRespCode);
/* 508:    */       
/* 509:525 */       prepstat.execute();
/* 510:    */       
/* 511:    */ 
/* 512:528 */       System.out.println(new Date() + ": service_usage_log updated successfully!");
/* 513:    */     }
/* 514:    */     catch (Exception ex)
/* 515:    */     {
/* 516:532 */       if (con != null)
/* 517:    */       {
/* 518:    */         try
/* 519:    */         {
/* 520:534 */           con.close();
/* 521:    */         }
/* 522:    */         catch (Exception ex1)
/* 523:    */         {
/* 524:536 */           log(ex.getMessage());
/* 525:    */         }
/* 526:538 */         con = null;
/* 527:    */       }
/* 528:541 */       System.out.println(new Date() + ": error updating service_usage_log: " + ex.getMessage());
/* 529:    */     }
/* 530:    */     finally
/* 531:    */     {
/* 532:544 */       if (rs != null)
/* 533:    */       {
/* 534:    */         try
/* 535:    */         {
/* 536:546 */           rs.close();
/* 537:    */         }
/* 538:    */         catch (Exception e)
/* 539:    */         {
/* 540:548 */           log(e.getMessage());
/* 541:    */         }
/* 542:550 */         rs = null;
/* 543:    */       }
/* 544:552 */       if (prepstat != null)
/* 545:    */       {
/* 546:    */         try
/* 547:    */         {
/* 548:554 */           prepstat.close();
/* 549:    */         }
/* 550:    */         catch (Exception e)
/* 551:    */         {
/* 552:556 */           log(e.getMessage());
/* 553:    */         }
/* 554:559 */         prepstat = null;
/* 555:    */       }
/* 556:561 */       if (con != null)
/* 557:    */       {
/* 558:    */         try
/* 559:    */         {
/* 560:563 */           con.close();
/* 561:    */         }
/* 562:    */         catch (Exception e)
/* 563:    */         {
/* 564:565 */           log(e.getMessage());
/* 565:    */         }
/* 566:568 */         con = null;
/* 567:    */       }
/* 568:    */     }
/* 569:    */   }
/* 570:    */   
/* 571:    */   public void log(String msg)
/* 572:    */   {
/* 573:574 */     this.filterConfig.getServletContext().log(msg);
/* 574:    */   }
/* 575:    */   
/* 576:    */   public boolean checkMsisdn(String msisdn)
/* 577:    */   {
/* 578:578 */     boolean msisdn_ok = true;
/* 579:581 */     if (msisdn.startsWith("+")) {
/* 580:582 */       msisdn = msisdn.replace("+", "");
/* 581:    */     }
/* 582:    */     try
/* 583:    */     {
/* 584:585 */       Long.parseLong(msisdn);
/* 585:    */     }
/* 586:    */     catch (Exception e)
/* 587:    */     {
/* 588:587 */       msisdn_ok = false;
/* 589:588 */       System.out.println(new Date() + ": error in msisdn : " + msisdn + " .Request would be terminated.");
/* 590:    */     }
/* 591:591 */     return msisdn_ok;
/* 592:    */   }
/* 593:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.serviceinterfaces.servicelocator
 * JD-Core Version:    0.7.0.1
 */