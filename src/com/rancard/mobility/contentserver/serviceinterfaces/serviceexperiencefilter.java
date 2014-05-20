/*   1:    */ package com.rancard.mobility.contentserver.serviceinterfaces;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import com.rancard.common.Feedback;
/*   5:    */ import com.rancard.common.uidGen;
/*   6:    */ import com.rancard.mobility.common.ThreadedMessageSender;
/*   7:    */ import com.rancard.mobility.contentserver.CPConnections;
/*   8:    */ import com.rancard.mobility.contentserver.CPSite;
/*   9:    */ import com.rancard.mobility.infoserver.InfoService;
/*  10:    */ import com.rancard.mobility.infoserver.common.services.ServiceManager;
/*  11:    */ import com.rancard.mobility.infoserver.common.services.UserService;
/*  12:    */ import com.rancard.mobility.infoserver.common.services.UserServiceExperience;
/*  13:    */ import com.rancard.mobility.infoserver.common.services.UserServiceTransaction;
/*  14:    */ import com.rancard.mobility.infoserver.viralmarketing.PromoImpression;
/*  15:    */ import com.rancard.mobility.infoserver.viralmarketing.VMServiceManager;
/*  16:    */ import com.rancard.mobility.infoserver.viralmarketing.VMTransaction;
/*  17:    */ import com.rancard.mobility.infoserver.viralmarketing.VMUser;
/*  18:    */ import com.rancard.util.payment.PaymentManager;
/*  19:    */ import com.rancard.util.payment.PricePoint;
/*  20:    */ import java.io.IOException;
/*  21:    */ import java.io.PrintStream;
/*  22:    */ import java.net.URLEncoder;
/*  23:    */ import java.sql.Connection;
/*  24:    */ import java.sql.PreparedStatement;
/*  25:    */ import java.sql.ResultSet;
/*  26:    */ import java.sql.Timestamp;
/*  27:    */ import java.util.ArrayList;
/*  28:    */ import java.util.Calendar;
/*  29:    */ import java.util.GregorianCalendar;
/*  30:    */ import java.util.HashMap;
/*  31:    */ import java.util.Map;
/*  32:    */ import javax.servlet.Filter;
/*  33:    */ import javax.servlet.FilterChain;
/*  34:    */ import javax.servlet.FilterConfig;
/*  35:    */ import javax.servlet.ServletContext;
/*  36:    */ import javax.servlet.ServletException;
/*  37:    */ import javax.servlet.ServletRequest;
/*  38:    */ import javax.servlet.ServletResponse;
/*  39:    */ import javax.servlet.http.HttpServlet;
/*  40:    */ import javax.servlet.http.HttpServletRequest;
/*  41:    */ import javax.servlet.http.HttpServletResponse;
/*  42:    */ 
/*  43:    */ public class serviceexperiencefilter
/*  44:    */   extends HttpServlet
/*  45:    */   implements Filter
/*  46:    */ {
/*  47: 45 */   String push_sender = "";
/*  48: 46 */   String inv_instruction = "";
/*  49:    */   
/*  50:    */   public void init(FilterConfig filterConfig) {}
/*  51:    */   
/*  52:    */   public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
/*  53:    */     throws IOException, ServletException
/*  54:    */   {
/*  55: 55 */     HttpServletRequest request = (HttpServletRequest)req;
/*  56: 56 */     HttpServletResponse response = (HttpServletResponse)res;
/*  57:    */     
/*  58: 58 */     String accountId = (String)req.getAttribute("acctId");
/*  59: 59 */     String siteId = request.getParameter("siteId");
/*  60: 60 */     String keyword = req.getParameter("keyword");
/*  61: 61 */     String msisdn = req.getParameter("msisdn");
/*  62: 62 */     String dest = req.getParameter("dest");
/*  63: 63 */     String shortCode = dest.contains("+") ? dest.substring(1) : dest;
/*  64: 64 */     String smsc = req.getParameter("smsc") == null ? "" : req.getParameter("smsc");
/*  65:    */     
/*  66: 66 */     String pushMsg = "";
/*  67: 67 */     String pushMsgSender = "";
/*  68:    */     
/*  69: 69 */     String promoId = "";
/*  70: 70 */     boolean skipMessagePush = false;
/*  71:    */     
/*  72: 72 */     logState(accountId, siteId, keyword, msisdn, "Entering serviceexperiencefilter...");
/*  73:    */     try
/*  74:    */     {
/*  75: 74 */       UserService srvc = ServiceManager.viewService(keyword, accountId);
/*  76:    */       
/*  77: 76 */       logState(accountId, siteId, keyword, msisdn, "Start service experience processing...");
/*  78:    */       
/*  79: 78 */       String service_keyword = srvc.getKeyword();
/*  80: 79 */       UserServiceExperience srvcExpr = ServiceManager.viewServiceExperience(accountId, siteId, service_keyword);
/*  81: 81 */       if (srvcExpr != null)
/*  82:    */       {
/*  83: 82 */         promoId = srvcExpr.getPromoId();
/*  84: 83 */         String serviceRespCode = srvcExpr.getPromoRespCode();
/*  85:    */         
/*  86: 85 */         Map metaDataMap = srvcExpr.getMetaDataMap();
/*  87: 86 */         String altKeyword = (String)metaDataMap.get("override_keyword".toUpperCase());
/*  88: 87 */         if ((altKeyword != null) && (!altKeyword.equals(""))) {
/*  89: 88 */           request.setAttribute("override_keyword", altKeyword);
/*  90:    */         }
/*  91: 91 */         if (srvc.isSubscription())
/*  92:    */         {
/*  93: 93 */           boolean alreadySubscribed = false;
/*  94: 94 */           HashMap thisSubscription = ServiceManager.getSubscription(msisdn, accountId, keyword, altKeyword);
/*  95: 95 */           if ((thisSubscription != null) && (!thisSubscription.isEmpty())) {
/*  96: 96 */             alreadySubscribed = true;
/*  97:    */           }
/*  98: 99 */           if (alreadySubscribed)
/*  99:    */           {
/* 100:100 */             Timestamp curTimestamp = new Timestamp(new java.util.Date().getTime());
/* 101:    */             
/* 102:102 */             java.util.Date nextSubscriptionDate = thisSubscription.get("next_subscription_date") == null ? null : (Timestamp)thisSubscription.get("next_subscription_date");
/* 103:103 */             ArrayList keywordList = new ArrayList();
/* 104:104 */             keywordList.add(service_keyword);
/* 105:106 */             if ((nextSubscriptionDate == null) || (nextSubscriptionDate.after(curTimestamp)))
/* 106:    */             {
/* 107:107 */               logState(accountId, siteId, keyword, msisdn, "Subscription already exists in DB");
/* 108:108 */               if (pushMsg.isEmpty()) {
/* 109:109 */                 pushMsg = srvcExpr.getAlreadySubscribedMsg();
/* 110:    */               }
/* 111:111 */               pushMsgSender = srvcExpr.getAlreadySubscribedMsgSender().equals("") ? shortCode : srvcExpr.getAlreadySubscribedMsgSender();
/* 112:113 */               if ((srvcExpr.getMetaDataMap().get("FREEMIUM") != null) && (((String)srvcExpr.getMetaDataMap().get("FREEMIUM")).equals("YES")) && (Integer.parseInt(thisSubscription.get("billing_type").toString()) == 0))
/* 113:    */               {
/* 114:114 */                 pushMsgSender = srvcExpr.getWelcomeMsgSender();
/* 115:115 */                 logState(accountId, siteId, keyword, msisdn, "Setting Kannel FROM attribute to " + pushMsgSender);
/* 116:116 */                 ServiceManager.subscribeToService(msisdn, keywordList, accountId, srvcExpr.getSubscriptionInterval(), 1, 1);
/* 117:117 */                 request.setAttribute("x-kannel-header-from", pushMsgSender);
/* 118:    */                 
/* 119:119 */                 skipMessagePush = false;
/* 120:    */               }
/* 121:120 */               else if ((metaDataMap.get("OVERRIDE_CONTENT_ON_RETURNING_ACTIVE") != null) && (((String)metaDataMap.get("OVERRIDE_CONTENT_ON_RETURNING_ACTIVE")).trim().equalsIgnoreCase("TRUE")))
/* 122:    */               {
/* 123:121 */                 request.setAttribute("override_msg", pushMsg);
/* 124:122 */                 logState(accountId, siteId, keyword, msisdn, "Setting Kannel FROM attribute to " + pushMsgSender);
/* 125:123 */                 request.setAttribute("x-kannel-header-from", pushMsgSender);
/* 126:    */                 
/* 127:125 */                 skipMessagePush = true;
/* 128:    */               }
/* 129:    */               else
/* 130:    */               {
/* 131:127 */                 logState(accountId, siteId, keyword, msisdn, "Setting Kannel FROM attribute to " + srvc.getServiceResponseSender());
/* 132:128 */                 request.setAttribute("x-kannel-header-from", srvc.getServiceResponseSender());
/* 133:    */                 
/* 134:130 */                 promoId = "";
/* 135:131 */                 serviceRespCode = "";
/* 136:    */                 try
/* 137:    */                 {
/* 138:133 */                   skipMessagePush = Boolean.parseBoolean((String)metaDataMap.get("skip_msg_push".toUpperCase()));
/* 139:    */                 }
/* 140:    */                 catch (Exception e)
/* 141:    */                 {
/* 142:135 */                   skipMessagePush = false;
/* 143:    */                 }
/* 144:138 */                 if (skipMessagePush)
/* 145:    */                 {
/* 146:139 */                   int accessCount = 0;
/* 147:    */                   try
/* 148:    */                   {
/* 149:141 */                     accessCount = ServiceManager.getLastAccessCount(msisdn, accountId, keyword);
/* 150:    */                   }
/* 151:    */                   catch (Exception e) {}
/* 152:144 */                   if (accessCount > 2) {
/* 153:145 */                     skipMessagePush = true;
/* 154:    */                   } else {
/* 155:147 */                     skipMessagePush = false;
/* 156:    */                   }
/* 157:    */                 }
/* 158:    */               }
/* 159:    */             }
/* 160:    */             else
/* 161:    */             {
/* 162:152 */               String renewalMessage = "";
/* 163:153 */               boolean isInActive = isInActiveUser(accountId, keyword, msisdn);
/* 164:155 */               if (isInActive)
/* 165:    */               {
/* 166:156 */                 updateSubscription(msisdn, accountId, keyword, srvcExpr.getSubscriptionInterval(), false);
/* 167:157 */                 renewalMessage = "Dear subscriber, your subscription to " + srvc.getKeyword() + " has been renewed. This service is free for " + srvcExpr.getSubscriptionInterval() + " days.";
/* 168:    */               }
/* 169:    */               else
/* 170:    */               {
/* 171:159 */                 srvcExpr.getAlreadySubscribedMsg();
/* 172:    */               }
/* 173:162 */               if ((metaDataMap.get("OVERRIDE_CONTENT_ON_RETURNING_INACTIVE") != null) && (((String)metaDataMap.get("OVERRIDE_CONTENT_ON_RETURNING_INACTIVE")).trim().equalsIgnoreCase("TRUE")))
/* 174:    */               {
/* 175:163 */                 request.setAttribute("override_msg", renewalMessage);
/* 176:164 */                 logState(accountId, siteId, keyword, msisdn, "Setting Kannel FROM attribute to " + srvc.getServiceResponseSender());
/* 177:165 */                 request.setAttribute("x-kannel-header-from", srvcExpr.getWelcomeMsgSender());
/* 178:    */                 
/* 179:167 */                 skipMessagePush = true;
/* 180:    */               }
/* 181:    */               else
/* 182:    */               {
/* 183:169 */                 pushMsg = renewalMessage;
/* 184:170 */                 pushMsgSender = srvcExpr.getWelcomeMsgSender();
/* 185:    */                 
/* 186:172 */                 request.setAttribute("x-kannel-header-from", srvc.getServiceResponseSender());
/* 187:    */               }
/* 188:    */             }
/* 189:    */           }
/* 190:    */           else
/* 191:    */           {
/* 192:176 */             if ((altKeyword != null) && (!altKeyword.equals("")))
/* 193:    */             {
/* 194:177 */               service_keyword = altKeyword;
/* 195:178 */               logState(accountId, siteId, keyword, msisdn, "Service keyword changed for subscription: " + service_keyword);
/* 196:    */             }
/* 197:181 */             if ((metaDataMap.get("SEND_CONTENT_IF_UNSUB") != null) && (metaDataMap.get("SEND_CONTENT_IF_UNSUB").toString().trim().equalsIgnoreCase("FALSE"))) {
/* 198:182 */               alreadySubscribed = ServiceManager.hasRecentUnsubscription(msisdn, service_keyword, accountId);
/* 199:    */             } else {
/* 200:184 */               alreadySubscribed = false;
/* 201:    */             }
/* 202:187 */             if (alreadySubscribed)
/* 203:    */             {
/* 204:188 */               ArrayList keywordList = new ArrayList();
/* 205:189 */               keywordList.add(service_keyword);
/* 206:191 */               if (srvcExpr.getSubscriptionInterval() == -1)
/* 207:    */               {
/* 208:192 */                 pushMsg = srvcExpr.getPromoMsg();
/* 209:193 */                 pushMsgSender = srvcExpr.getPromoMsgSender();
/* 210:    */               }
/* 211:194 */               else if (srvcExpr.getSubscriptionInterval() == -2)
/* 212:    */               {
/* 213:195 */                 request.setAttribute("override_msg", srvcExpr.getPromoMsg());
/* 214:196 */                 if ((request.getAttribute("x-kannel-header-from") != null) && (!((String)request.getAttribute("x-kannel-header-from")).equals("")))
/* 215:    */                 {
/* 216:197 */                   response.addHeader("X-Kannel-From", srvcExpr.getPromoMsgSender());
/* 217:    */                 }
/* 218:198 */                 else if ((srvc.getServiceResponseSender() != null) && (!srvc.getServiceResponseSender().equals("")))
/* 219:    */                 {
/* 220:199 */                   System.out.println(new java.util.Date() + ": Setting X-Kannel-From header (" + srvc.getServiceResponseSender() + ")");
/* 221:200 */                   response.addHeader("X-Kannel-From", srvcExpr.getPromoMsgSender());
/* 222:    */                 }
/* 223:    */               }
/* 224:    */               else
/* 225:    */               {
/* 226:203 */                 if ((srvcExpr.getMetaDataMap().get("FREEMIUM") == null) || (((String)srvcExpr.getMetaDataMap().get("FREEMIUM")).equals("NO"))) {
/* 227:204 */                   ServiceManager.subscribeToService(msisdn, keywordList, accountId, srvcExpr.getSubscriptionInterval(), 1, 1);
/* 228:    */                 } else {
/* 229:206 */                   ServiceManager.subscribeToService(msisdn, keywordList, accountId, srvcExpr.getSubscriptionInterval(), 1, 0);
/* 230:    */                 }
/* 231:209 */                 String subscriptionResumedMessage = srvcExpr.getWelcomeMsg();
/* 232:210 */                 request.setAttribute("override_msg", subscriptionResumedMessage);
/* 233:211 */                 logState(accountId, siteId, keyword, msisdn, "Setting Kannel FROM attribute to " + srvcExpr.getWelcomeMsgSender());
/* 234:212 */                 request.setAttribute("x-kannel-header-from", srvcExpr.getWelcomeMsgSender());
/* 235:    */                 
/* 236:214 */                 skipMessagePush = true;
/* 237:    */               }
/* 238:217 */               vmAcceptance(accountId, service_keyword, msisdn, srvc.getServiceName(), srvcExpr.getWelcomeMsgSender().equals("") ? shortCode : srvcExpr.getWelcomeMsgSender(), smsc);
/* 239:    */             }
/* 240:219 */             else if (srvcExpr.getSubscriptionInterval() == -1)
/* 241:    */             {
/* 242:220 */               pushMsg = srvcExpr.getPromoMsg();
/* 243:221 */               pushMsgSender = srvcExpr.getPromoMsgSender();
/* 244:    */             }
/* 245:222 */             else if (srvcExpr.getSubscriptionInterval() == -2)
/* 246:    */             {
/* 247:223 */               request.setAttribute("override_msg", srvcExpr.getPromoMsg());
/* 248:224 */               if ((request.getAttribute("x-kannel-header-from") != null) && (!((String)request.getAttribute("x-kannel-header-from")).equals("")))
/* 249:    */               {
/* 250:225 */                 response.addHeader("X-Kannel-From", srvcExpr.getPromoMsgSender());
/* 251:    */               }
/* 252:226 */               else if ((srvc.getServiceResponseSender() != null) && (!srvc.getServiceResponseSender().equals("")))
/* 253:    */               {
/* 254:227 */                 System.out.println(new java.util.Date() + ": Setting X-Kannel-From header (" + srvc.getServiceResponseSender() + ")");
/* 255:228 */                 response.addHeader("X-Kannel-From", srvcExpr.getPromoMsgSender());
/* 256:    */               }
/* 257:    */             }
/* 258:    */             else
/* 259:    */             {
/* 260:231 */               logState(accountId, siteId, keyword, msisdn, "Subscription doesn't exist. Creating new subscription record...");
/* 261:    */               
/* 262:233 */               ArrayList keywordList = new ArrayList();
/* 263:234 */               keywordList.add(service_keyword);
/* 264:236 */               if (srvcExpr.getSubscriptionInterval() == 0) {
/* 265:237 */                 ServiceManager.subscribeToService(msisdn, keywordList, accountId);
/* 266:238 */               } else if ((srvcExpr.getMetaDataMap().get("FREEMIUM") == null) || (((String)srvcExpr.getMetaDataMap().get("FREEMIUM")).equals("NO"))) {
/* 267:239 */                 ServiceManager.subscribeToService(msisdn, keywordList, accountId, srvcExpr.getSubscriptionInterval(), 1, 1);
/* 268:    */               } else {
/* 269:241 */                 ServiceManager.subscribeToService(msisdn, keywordList, accountId, srvcExpr.getSubscriptionInterval(), 1, 0);
/* 270:    */               }
/* 271:244 */               vmAcceptance(accountId, service_keyword, msisdn, srvc.getServiceName(), srvcExpr.getWelcomeMsgSender().equals("") ? shortCode : srvcExpr.getWelcomeMsgSender(), smsc);
/* 272:    */               
/* 273:    */ 
/* 274:247 */               pushMsg = srvcExpr.getWelcomeMsg();
/* 275:248 */               pushMsgSender = srvcExpr.getWelcomeMsgSender().equals("") ? shortCode : srvcExpr.getWelcomeMsgSender();
/* 276:    */               
/* 277:250 */               promoId = "";
/* 278:251 */               serviceRespCode = "";
/* 279:    */             }
/* 280:    */           }
/* 281:    */         }
/* 282:    */         else
/* 283:    */         {
/* 284:255 */           pushMsg = srvcExpr.getPromoMsg();
/* 285:256 */           pushMsgSender = srvcExpr.getPromoMsgSender();
/* 286:    */         }
/* 287:259 */         if (!skipMessagePush)
/* 288:    */         {
/* 289:260 */           int pushMsgWaitTime = srvcExpr.getPushMsgWaitTime();
/* 290:    */           
/* 291:    */ 
/* 292:263 */           String flashPush = (String)metaDataMap.get("push_msg_flash".toUpperCase());
/* 293:265 */           if ((flashPush != null) && (flashPush.equals("1")))
/* 294:    */           {
/* 295:266 */             CPConnections cnxn = CPConnections.getConnection(accountId, msisdn, "kannel");
/* 296:267 */             new Thread(new ThreadedMessageSender(cnxn, msisdn, pushMsgSender, pushMsg, "&mclass=0", pushMsgWaitTime)).start();
/* 297:    */           }
/* 298:    */           else
/* 299:    */           {
/* 300:269 */             CPConnections cnxn = CPConnections.getConnection(accountId, msisdn);
/* 301:270 */             new Thread(new ThreadedMessageSender(cnxn, msisdn, pushMsgSender, pushMsg, pushMsgWaitTime)).start();
/* 302:    */           }
/* 303:    */         }
/* 304:273 */         request.setAttribute("promoId", promoId);
/* 305:    */         
/* 306:    */ 
/* 307:276 */         promoImpressionsCheck(serviceRespCode, keyword, accountId, msisdn, request);
/* 308:    */       }
/* 309:    */       else
/* 310:    */       {
/* 311:278 */         logState(accountId, siteId, keyword, msisdn, "No experience configuration defined for service");
/* 312:    */       }
/* 313:    */     }
/* 314:    */     catch (Exception e)
/* 315:    */     {
/* 316:281 */       logState(accountId, siteId, keyword, msisdn, "Error @ serviceexperiencefilter: " + e.getMessage());
/* 317:    */     }
/* 318:284 */     logState(accountId, siteId, keyword, msisdn, "Exiting serviceexperiencefilter.");
/* 319:    */     
/* 320:286 */     filterChain.doFilter(req, res);
/* 321:    */   }
/* 322:    */   
/* 323:    */   private void vmAcceptance(String accountId, String keyword, String msisdn, String serviceName, String shortCode, String smsc)
/* 324:    */   {
/* 325:290 */     initializeServiceCustomization(accountId, keyword, shortCode);
/* 326:    */     try
/* 327:    */     {
/* 328:292 */       CPConnections cnxn = CPConnections.getConnection(accountId, msisdn);
/* 329:    */       
/* 330:294 */       VMTransaction trans = VMServiceManager.viewTransaction(accountId, keyword, msisdn);
/* 331:295 */       if ((trans != null) && (trans.getStatus().equals("inv_sent")))
/* 332:    */       {
/* 333:296 */         VMServiceManager.updateTransactionStatus(trans.getCampaignId(), msisdn, "inv_accepted", 10);
/* 334:    */         
/* 335:298 */         String recruiter = trans.getRecruiterMsisdn();
/* 336:299 */         VMUser user = VMServiceManager.viewUser(keyword, accountId, recruiter);
/* 337:300 */         String displayable_number = "0" + trans.getRecipientMsisdn().substring(4);
/* 338:301 */         String confirmation_msg = "You recently invited " + displayable_number + " to use your favourite service. " + "We're excited to inform you that your invitation was just accepted! You now have " + user.getPoints() + " points.";
/* 339:    */         
/* 340:303 */         new Thread(new ThreadedMessageSender(cnxn, recruiter, this.push_sender, confirmation_msg, 5000)).start();
/* 341:    */       }
/* 342:306 */       if ((!smsc.toUpperCase().contains("myBuzz".toUpperCase())) && (!smsc.toUpperCase().contains("AIRTEL_NG".toUpperCase()))) {
/* 343:307 */         new Thread(new ThreadedMessageSender(cnxn, msisdn, this.push_sender, this.inv_instruction, 30000)).start();
/* 344:    */       }
/* 345:    */     }
/* 346:    */     catch (Exception exc)
/* 347:    */     {
/* 348:310 */       System.out.println(new java.util.Date() + " " + serviceexperiencefilter.class + ":ERROR Exception caught processing viral marketing step 2: acceptance " + exc.getMessage());
/* 349:    */     }
/* 350:    */   }
/* 351:    */   
/* 352:    */   private void initializeServiceCustomization(String accountId, String keyword, String shortCode)
/* 353:    */   {
/* 354:315 */     if (accountId.equals("000"))
/* 355:    */     {
/* 356:316 */       this.push_sender = keyword;
/* 357:317 */       this.inv_instruction = ("Send INVITE followed by your friend's number to " + shortCode + " to share this service with them. Add FROM followed by your name to the message to personalize it.");
/* 358:    */     }
/* 359:    */     else
/* 360:    */     {
/* 361:319 */       this.push_sender = shortCode;
/* 362:320 */       this.inv_instruction = ("Send INVITE followed by your friend's number to " + this.push_sender + " to share this service with them. Add FROM followed by your name to the message to personalize it.");
/* 363:    */     }
/* 364:    */   }
/* 365:    */   
/* 366:    */   private void promoImpressionsCheck(String srvcRespCode, String keyword, String accountId, String msisdn, HttpServletRequest request)
/* 367:    */     throws Exception
/* 368:    */   {
/* 369:    */     try
/* 370:    */     {
/* 371:326 */       System.out.println(new java.util.Date() + ": In promoImpression Checker");
/* 372:327 */       String hashString = msisdn + accountId + keyword + srvcRespCode.toUpperCase();
/* 373:    */       
/* 374:329 */       PromoImpression pImpression = VMServiceManager.viewPromoImpression(hashString.hashCode());
/* 375:331 */       if (pImpression.exists())
/* 376:    */       {
/* 377:332 */         request.setAttribute("promoRespCode", keyword);
/* 378:333 */         VMServiceManager.updateAdRespSummary(pImpression);
/* 379:334 */         pImpression.setViewDate(new java.util.Date());
/* 380:335 */         VMServiceManager.updatePromoViewDate(pImpression);
/* 381:336 */         return;
/* 382:    */       }
/* 383:339 */       if ((srvcRespCode != null) && (!srvcRespCode.equals("")))
/* 384:    */       {
/* 385:341 */         pImpression.setHashCode(hashString.hashCode());
/* 386:342 */         pImpression.setAccountId(accountId);
/* 387:343 */         pImpression.setMsisdn(msisdn);
/* 388:344 */         pImpression.setKeyword(srvcRespCode);
/* 389:345 */         pImpression.setInventory_keyword(keyword);
/* 390:    */         
/* 391:347 */         VMServiceManager.createPromoImpression(pImpression);
/* 392:    */       }
/* 393:    */     }
/* 394:    */     catch (Exception exc)
/* 395:    */     {
/* 396:350 */       System.out.println("Exception caught processing promotional campaign: " + exc.getMessage());
/* 397:351 */       throw new Exception(exc.getMessage());
/* 398:    */     }
/* 399:    */   }
/* 400:    */   
/* 401:    */   private void logState(String accountId, String siteId, String keyword, String msisdn, String state)
/* 402:    */   {
/* 403:356 */     System.out.println(new java.util.Date() + ": " + accountId + ": " + siteId + ": " + keyword + ": " + msisdn + ": " + state);
/* 404:    */   }
/* 405:    */   
/* 406:    */   private boolean bill(String msisdn, String accountID, String keyword, String site_id, HttpServletRequest request, String price, String shortcode)
/* 407:    */   {
/* 408:360 */     boolean billed = false;
/* 409:361 */     String message = "";
/* 410:362 */     String siteType = "";
/* 411:363 */     String fullContextPath = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
/* 412:    */     
/* 413:365 */     String lang = (String)request.getAttribute("default_lang");
/* 414:366 */     if ((lang == null) || (lang.equals(""))) {
/* 415:367 */       lang = "en";
/* 416:    */     }
/* 417:370 */     Feedback feedback = (Feedback)getServletContext().getAttribute("feedback_" + lang);
/* 418:    */     try
/* 419:    */     {
/* 420:372 */       siteType = CPSite.viewSite(site_id).getSiteType();
/* 421:373 */       InfoService is = new InfoService();
/* 422:374 */       is.setAccountId(accountID);
/* 423:375 */       is.setKeyword(keyword);
/* 424:    */       
/* 425:377 */       Calendar calendar = Calendar.getInstance();
/* 426:378 */       java.util.Date today = new java.util.Date(calendar.getTime().getTime());
/* 427:379 */       String dateString = com.rancard.util.Date.formatDate(today, "dd-MM-yyyy");
/* 428:380 */       is.setPublishDate(dateString);
/* 429:    */       
/* 430:382 */       is.viewInfoService();
/* 431:    */       
/* 432:384 */       System.out.println(new java.util.Date() + ": " + msisdn + ":About to bill...");
/* 433:386 */       if ((price == null) || (price.equals("")))
/* 434:    */       {
/* 435:387 */         System.out.println(new java.util.Date() + ": " + msisdn + ":ERROR:BILLING_MECH_FAILURE:PricePoint Missing!");
/* 436:388 */         throw new Exception("11000");
/* 437:    */       }
/* 438:391 */       PricePoint pricePoint = PaymentManager.viewPricePointFor(new String[] { price }, msisdn);
/* 439:392 */       String pricePointId = pricePoint.getPricePointId();
/* 440:393 */       System.out.println(new java.util.Date() + ":pricePoint ID:" + pricePointId);
/* 441:395 */       if ((pricePointId == null) || ("".equals(pricePointId)))
/* 442:    */       {
/* 443:396 */         System.out.println(new java.util.Date() + ": " + msisdn + ":ERROR: Invalid pricePointID(s): " + price);
/* 444:    */         
/* 445:398 */         throw new Exception("11000");
/* 446:    */       }
/* 447:401 */       System.out.println(new java.util.Date() + ": " + keyword + ": " + msisdn + ": Entering initiatePayment from sendinfo");
/* 448:    */       
/* 449:403 */       String transactionId = "";
/* 450:404 */       if (pricePoint.getBillingMech().equals("4")) {
/* 451:405 */         transactionId = uidGen.genUID("", 5);
/* 452:406 */       } else if ((siteType.equals("1")) || (siteType.equals("0"))) {
/* 453:407 */         transactionId = uidGen.genUID("MP-", 5);
/* 454:    */       } else {
/* 455:409 */         transactionId = uidGen.genUID("OD-", 5);
/* 456:    */       }
/* 457:412 */       String completeTransnxnUrl = fullContextPath + "/sendinfo_push.jsp?msisdn=" + URLEncoder.encode(msisdn, "UTF-8") + "&keyword=" + URLEncoder.encode(keyword.toUpperCase(), "UTF-8") + "&alert_count=" + is.getMsgId() + "&dest=" + URLEncoder.encode(shortcode, "UTF-8") + "&siteId=" + URLEncoder.encode(site_id, "UTF-8") + "&transId=" + URLEncoder.encode(transactionId, "UTF-8");
/* 458:415 */       if (pricePoint.getBillingMech().equals("3")) {
/* 459:416 */         completeTransnxnUrl = completeTransnxnUrl + "&sender=KEYWORD";
/* 460:    */       }
/* 461:419 */       UserServiceTransaction trans = new UserServiceTransaction();
/* 462:420 */       trans.setAccountId(accountID);
/* 463:    */       
/* 464:422 */       trans.setDate(new Timestamp(new java.util.Date().getTime()));
/* 465:423 */       trans.setKeyword(keyword.toUpperCase());
/* 466:424 */       trans.setMsg("on-demand");
/* 467:425 */       trans.setMsisdn(msisdn);
/* 468:426 */       trans.setPricePoint(pricePointId);
/* 469:427 */       trans.setTransactionId(transactionId);
/* 470:428 */       trans.setIsBilled(0);
/* 471:429 */       trans.setIsCompleted(0);
/* 472:430 */       trans.setCallBackUrl(completeTransnxnUrl);
/* 473:    */       
/* 474:    */ 
/* 475:433 */       int isCompleted = 0;
/* 476:    */       boolean transactionCreated;
/* 477:    */       try
/* 478:    */       {
/* 479:435 */         ServiceManager.createTransaction(trans);
/* 480:436 */         System.out.println(new java.util.Date() + ":sendinfo: Transaction " + trans.getTransactionId() + " created!");
/* 481:437 */         transactionCreated = true;
/* 482:    */       }
/* 483:    */       catch (Exception e)
/* 484:    */       {
/* 485:439 */         transactionCreated = false;
/* 486:440 */         System.out.println(new java.util.Date() + ":sendinfo: Transaction " + trans.getTransactionId() + " NOT created! Error message: " + e.getMessage());
/* 487:    */       }
/* 488:443 */       if (transactionCreated)
/* 489:    */       {
/* 490:    */         try
/* 491:    */         {
/* 492:445 */           CPConnections cnxn = CPConnections.getConnection(accountID, msisdn);
/* 493:446 */           billed = PaymentManager.initiatePayment(pricePoint, cnxn, msisdn, transactionId, "", completeTransnxnUrl);
/* 494:    */         }
/* 495:    */         catch (Exception e)
/* 496:    */         {
/* 497:448 */           if (e.getMessage().equals("READ_TIMEOUT"))
/* 498:    */           {
/* 499:449 */             message = "We've received your request for a " + is.getServiceName() + " item. Please be patient while we process it.";
/* 500:450 */             isCompleted = 0;
/* 501:    */           }
/* 502:451 */           else if (e.getMessage().equals("INSUFFICIENT_CREDIT"))
/* 503:    */           {
/* 504:452 */             message = "We couldn't complete purchase of the " + is.getServiceName() + " item you requested. Please top up and then send " + is.getKeyword().toUpperCase() + " to " + shortcode;
/* 505:    */             
/* 506:454 */             isCompleted = 1;
/* 507:    */           }
/* 508:455 */           else if (e.getMessage().equals("ERROR"))
/* 509:    */           {
/* 510:456 */             message = "We couldn't complete purchase of the " + is.getServiceName() + " item you requested. Please try again. Send " + is.getKeyword().toUpperCase() + " to " + shortcode + ". You've not been billed.";
/* 511:    */             
/* 512:458 */             isCompleted = 1;
/* 513:    */           }
/* 514:    */         }
/* 515:463 */         request.setAttribute("x-kannel-header-binfo", transactionId);
/* 516:464 */         System.out.println(new java.util.Date() + ": " + keyword + ": " + msisdn + ": Completed initiatePayment from sendinfo with result: " + billed);
/* 517:466 */         if (billed)
/* 518:    */         {
/* 519:467 */           trans.setIsBilled(1);
/* 520:468 */           trans.setIsCompleted(1);
/* 521:    */         }
/* 522:    */         else
/* 523:    */         {
/* 524:470 */           trans.setIsBilled(0);
/* 525:471 */           trans.setIsCompleted(isCompleted);
/* 526:    */         }
/* 527:    */         try
/* 528:    */         {
/* 529:475 */           ServiceManager.updateTransaction(trans.getTransactionId(), trans.getIsCompleted(), trans.getIsBilled());
/* 530:476 */           System.out.println(new java.util.Date() + ":sendinfo: Transaction " + trans.getTransactionId() + " updated!");
/* 531:    */         }
/* 532:    */         catch (Exception e)
/* 533:    */         {
/* 534:478 */           System.out.println(new java.util.Date() + ":sendinfo: Transaction " + trans.getTransactionId() + " NOT updated! Error message: " + e.getMessage());
/* 535:    */         }
/* 536:    */       }
/* 537:    */     }
/* 538:    */     catch (Exception e)
/* 539:    */     {
/* 540:    */       try
/* 541:    */       {
/* 542:483 */         if (siteType.equals("2")) {
/* 543:484 */           message = feedback.getUserFriendlyDescription(e.getMessage());
/* 544:    */         } else {
/* 545:486 */           message = feedback.formDefaultMessage(e.getMessage());
/* 546:    */         }
/* 547:    */       }
/* 548:    */       catch (Exception ex)
/* 549:    */       {
/* 550:489 */         message = ex.getMessage();
/* 551:    */       }
/* 552:    */     }
/* 553:493 */     if (!message.equals("")) {
/* 554:494 */       new Thread(new ThreadedMessageSender(new CPConnections(), msisdn, shortcode, message, 0)).start();
/* 555:    */     }
/* 556:497 */     return billed;
/* 557:    */   }
/* 558:    */   
/* 559:    */   private int updateSubscription(String msisdn, String accountId, String keyword, int subscriptionInterval, boolean activeSub)
/* 560:    */     throws Exception
/* 561:    */   {
/* 562:501 */     int numOfDaysTillEndOfSubscription = 0;
/* 563:502 */     ResultSet rs = null;
/* 564:503 */     Connection con = null;
/* 565:504 */     PreparedStatement prepstat = null;
/* 566:    */     try
/* 567:    */     {
/* 568:506 */       con = DConnect.getConnection();
/* 569:507 */       String SQL = "update service_subscription set subscription_date=subscription_date, status=1, next_subscription_date=DATE_ADD(" + (activeSub ? "next_subscription_date" : "CURRENT_TIMESTAMP") + ", INTERVAL " + subscriptionInterval + " DAY) where msisdn='" + msisdn + "' and keyword='" + keyword + "' and account_id='" + accountId + "'";
/* 570:    */       
/* 571:509 */       prepstat = con.prepareStatement(SQL);
/* 572:510 */       prepstat.execute();
/* 573:    */       
/* 574:512 */       SQL = "select next_subscription_date from service_subscription where msisdn='" + msisdn + "' and keyword='" + keyword + "' and account_id='" + accountId + "'";
/* 575:513 */       prepstat = con.prepareStatement(SQL);
/* 576:514 */       rs = prepstat.executeQuery();
/* 577:516 */       while (rs.next())
/* 578:    */       {
/* 579:517 */         Timestamp nextSubxnDate = rs.getTimestamp("next_subscription_date");
/* 580:518 */         numOfDaysTillEndOfSubscription = rest_of_days(nextSubxnDate);
/* 581:    */       }
/* 582:    */     }
/* 583:    */     catch (Exception ex)
/* 584:    */     {
/* 585:521 */       System.out.println(new java.util.Date() + ": [bbcNewsMtnNg:ERROR]: Exception caught :" + ex.getMessage());
/* 586:522 */       throw new Exception(ex.getMessage());
/* 587:    */     }
/* 588:    */     finally
/* 589:    */     {
/* 590:524 */       if (rs != null) {
/* 591:525 */         rs.close();
/* 592:    */       }
/* 593:527 */       if (prepstat != null) {
/* 594:528 */         prepstat.close();
/* 595:    */       }
/* 596:530 */       if (con != null) {
/* 597:531 */         con.close();
/* 598:    */       }
/* 599:    */     }
/* 600:535 */     return numOfDaysTillEndOfSubscription;
/* 601:    */   }
/* 602:    */   
/* 603:    */   private static int rest_of_days(Timestamp nextSubxnDate)
/* 604:    */   {
/* 605:539 */     GregorianCalendar tmpDate1 = new GregorianCalendar();
/* 606:540 */     GregorianCalendar tmpDate2 = new GregorianCalendar();
/* 607:    */     
/* 608:542 */     tmpDate1.setTime(new java.util.Date());
/* 609:543 */     tmpDate2.setTimeInMillis(nextSubxnDate.getTime());
/* 610:    */     
/* 611:545 */     int days_elapsed = calculate(tmpDate2, tmpDate1);
/* 612:547 */     if (days_elapsed < 1) {
/* 613:548 */       days_elapsed = 0;
/* 614:    */     }
/* 615:551 */     return days_elapsed;
/* 616:    */   }
/* 617:    */   
/* 618:    */   private static int calculate(GregorianCalendar t1, GregorianCalendar t2)
/* 619:    */   {
/* 620:555 */     int ndays = 0;
/* 621:557 */     if (t1.get(1) < t2.get(1))
/* 622:    */     {
/* 623:558 */       ndays += 366 - t1.get(6);
/* 624:560 */       for (int n = t2.get(1) + 1; n <= t2.get(1) - 1; n++) {
/* 625:561 */         ndays += 365;
/* 626:    */       }
/* 627:    */     }
/* 628:565 */     ndays += t2.get(6);
/* 629:566 */     if (t2.get(1) == t1.get(1)) {
/* 630:567 */       ndays = t1.get(6) - t2.get(6);
/* 631:    */     }
/* 632:570 */     return ndays;
/* 633:    */   }
/* 634:    */   
/* 635:    */   private boolean isInActiveUser(String accountId, String keyword, String msisdn)
/* 636:    */     throws Exception
/* 637:    */   {
/* 638:574 */     boolean isActive = false;
/* 639:575 */     int status = 0;
/* 640:576 */     ResultSet rs = null;
/* 641:577 */     Connection dbConnection = null;
/* 642:578 */     PreparedStatement prepstat = null;
/* 643:    */     try
/* 644:    */     {
/* 645:580 */       dbConnection = DConnect.getConnection();
/* 646:581 */       prepstat = dbConnection.prepareStatement("SELECT status from service_subscription WHERE account_id=? AND keyword = ? AND msisdn LIKE ? limit 1;");
/* 647:    */       
/* 648:583 */       prepstat.setString(1, accountId);
/* 649:584 */       prepstat.setString(2, keyword);
/* 650:585 */       prepstat.setString(3, msisdn);
/* 651:    */       
/* 652:587 */       rs = prepstat.executeQuery();
/* 653:588 */       while (rs.next()) {
/* 654:589 */         status = rs.getInt(1);
/* 655:    */       }
/* 656:591 */       isActive = status == -1;
/* 657:    */     }
/* 658:    */     catch (Exception ex)
/* 659:    */     {
/* 660:593 */       System.out.println(new java.util.Date() + ": [isActiveUser Method:ERROR]: Exception caught :" + ex.getMessage());
/* 661:594 */       throw new Exception(ex.toString());
/* 662:    */     }
/* 663:    */     finally
/* 664:    */     {
/* 665:596 */       if (dbConnection != null) {
/* 666:597 */         dbConnection.close();
/* 667:    */       }
/* 668:600 */       if (prepstat != null) {
/* 669:601 */         prepstat.close();
/* 670:    */       }
/* 671:604 */       if (rs != null) {
/* 672:605 */         rs.close();
/* 673:    */       }
/* 674:    */     }
/* 675:608 */     return isActive;
/* 676:    */   }
/* 677:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.contentserver.serviceinterfaces.serviceexperiencefilter
 * JD-Core Version:    0.7.0.1
 */