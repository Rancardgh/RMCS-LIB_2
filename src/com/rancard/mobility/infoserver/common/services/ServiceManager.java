/*   1:    */ package com.rancard.mobility.infoserver.common.services;
/*   2:    */ 
/*   3:    */ import com.rancard.mobility.infoserver.feeds.CPUserFeeds;
/*   4:    */ import com.rancard.mobility.infoserver.feeds.Feed;
/*   5:    */ import com.rancard.util.Page;
/*   6:    */ import java.sql.Timestamp;
/*   7:    */ import java.util.ArrayList;
/*   8:    */ import java.util.Date;
/*   9:    */ import java.util.HashMap;
/*  10:    */ 
/*  11:    */ public abstract class ServiceManager
/*  12:    */ {
/*  13:    */   public static void createService(UserService service)
/*  14:    */     throws Exception
/*  15:    */   {
/*  16: 11 */     UserServiceDB.createService(service);
/*  17:    */   }
/*  18:    */   
/*  19:    */   public static void createServiceExperience(UserServiceExperience serviceExperience)
/*  20:    */     throws Exception
/*  21:    */   {
/*  22: 15 */     UserServiceExperienceDB.createServiceExperience(serviceExperience);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public static void updateService(String serviceType, String defaultMessage, String serviceName, String keyword, String accountId)
/*  26:    */     throws Exception
/*  27:    */   {
/*  28: 22 */     UserServiceDB.updateService(serviceType, defaultMessage, serviceName, keyword, accountId);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public static void deleteService(String keyword, String accountId)
/*  32:    */     throws Exception
/*  33:    */   {
/*  34: 28 */     UserServiceDB.deleteService(keyword, accountId);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public static void deleteService(ArrayList keywords, String accountId)
/*  38:    */     throws Exception
/*  39:    */   {
/*  40: 33 */     UserServiceDB.deleteService(keywords, accountId);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public static UserService viewService(String keyword, String accountId)
/*  44:    */     throws Exception
/*  45:    */   {
/*  46: 38 */     return UserServiceDB.viewService(keyword, accountId);
/*  47:    */   }
/*  48:    */   
/*  49:    */   public static UserServiceExperience viewServiceExperience(String accountId, String siteId, String keyword)
/*  50:    */     throws Exception
/*  51:    */   {
/*  52: 43 */     return UserServiceExperienceDB.viewServiceExperience(accountId, siteId, keyword);
/*  53:    */   }
/*  54:    */   
/*  55:    */   public static void deleteServiceExperience(String accountId, String keyword)
/*  56:    */     throws Exception
/*  57:    */   {
/*  58: 48 */     UserServiceExperienceDB.deleteServiceExperience(accountId, keyword);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public static void deleteServiceExperience(String accountId, ArrayList keywords)
/*  62:    */     throws Exception
/*  63:    */   {
/*  64: 53 */     UserServiceExperienceDB.deleteServiceExperience(accountId, keywords);
/*  65:    */   }
/*  66:    */   
/*  67:    */   public static void updateServiceExperience(String accountId, String keyword, UserServiceExperience serviceExperience)
/*  68:    */     throws Exception
/*  69:    */   {
/*  70: 58 */     UserServiceExperienceDB.updateServiceExperience(accountId, keyword, serviceExperience);
/*  71:    */   }
/*  72:    */   
/*  73:    */   public static void createFeed(Feed feed)
/*  74:    */     throws Exception
/*  75:    */   {
/*  76: 62 */     UserServiceDB.createFeed(feed);
/*  77:    */   }
/*  78:    */   
/*  79:    */   public static Feed viewFeed(String feedId)
/*  80:    */     throws Exception
/*  81:    */   {
/*  82: 66 */     return UserServiceDB.viewFeed(feedId);
/*  83:    */   }
/*  84:    */   
/*  85:    */   public static void deleteFeed(String feedId)
/*  86:    */     throws Exception
/*  87:    */   {
/*  88: 70 */     UserServiceDB.deleteFeed(feedId);
/*  89:    */   }
/*  90:    */   
/*  91:    */   public static void deleteFeed(ArrayList feedIds)
/*  92:    */     throws Exception
/*  93:    */   {
/*  94: 74 */     UserServiceDB.deleteFeed(feedIds);
/*  95:    */   }
/*  96:    */   
/*  97:    */   public static void updateFeed(String update_feed_id, Feed feedItem)
/*  98:    */     throws Exception
/*  99:    */   {
/* 100: 78 */     UserServiceDB.updateFeed(update_feed_id, feedItem);
/* 101:    */   }
/* 102:    */   
/* 103:    */   public static void createCPUserFeedAssociation(CPUserFeeds cpUserFeed)
/* 104:    */     throws Exception
/* 105:    */   {
/* 106: 83 */     UserServiceDB.createCPUserFeedAssociation(cpUserFeed);
/* 107:    */   }
/* 108:    */   
/* 109:    */   public static CPUserFeeds viewCPUserFeed(String account_id, String keyword)
/* 110:    */     throws Exception
/* 111:    */   {
/* 112: 87 */     return UserServiceDB.viewCPUserFeed(account_id, keyword);
/* 113:    */   }
/* 114:    */   
/* 115:    */   public static void deleteCPUserFeed(String account_id, String keyword)
/* 116:    */     throws Exception
/* 117:    */   {
/* 118: 91 */     UserServiceDB.deleteCPUserFeed(account_id, keyword);
/* 119:    */   }
/* 120:    */   
/* 121:    */   public static void deleteCPUserFeed(String account_id, ArrayList keywords)
/* 122:    */     throws Exception
/* 123:    */   {
/* 124: 95 */     UserServiceDB.deleteCPUserFeed(account_id, keywords);
/* 125:    */   }
/* 126:    */   
/* 127:    */   public static void updateCPUserFeed(String update_account_id, String update_keyword, CPUserFeeds cpUserFeed)
/* 128:    */     throws Exception
/* 129:    */   {
/* 130: 99 */     UserServiceDB.updateCPUserFeed(update_account_id, update_keyword, cpUserFeed);
/* 131:    */   }
/* 132:    */   
/* 133:    */   public static void createServiceLabel(String account_id, String keyword, String header, String footer)
/* 134:    */     throws Exception
/* 135:    */   {
/* 136:104 */     UserServiceDB.createServiceLabel(account_id, keyword, header, footer);
/* 137:    */   }
/* 138:    */   
/* 139:    */   public static HashMap viewServiceLabel(String account_id, String keyword)
/* 140:    */     throws Exception
/* 141:    */   {
/* 142:108 */     return UserServiceDB.viewServiceLabel(account_id, keyword);
/* 143:    */   }
/* 144:    */   
/* 145:    */   public static void deleteServiceLabel(String account_id, String keyword)
/* 146:    */     throws Exception
/* 147:    */   {
/* 148:112 */     UserServiceDB.deleteServiceLabel(account_id, keyword);
/* 149:    */   }
/* 150:    */   
/* 151:    */   public static void deleteServiceLabel(String account_id, ArrayList keywords)
/* 152:    */     throws Exception
/* 153:    */   {
/* 154:116 */     UserServiceDB.deleteServiceLabel(account_id, keywords);
/* 155:    */   }
/* 156:    */   
/* 157:    */   public static void updateServiceLabel(String update_account_id, String update_keyword, String new_account_id, String new_keyword, String new_header, String new_footer)
/* 158:    */     throws Exception
/* 159:    */   {
/* 160:121 */     UserServiceDB.updateServiceLabel(update_account_id, update_keyword, new_account_id, new_keyword, new_header, new_footer);
/* 161:    */   }
/* 162:    */   
/* 163:    */   public static void createServiceForwarding(String account_id, String keyword, String url, long timeout, int listen_status)
/* 164:    */     throws Exception
/* 165:    */   {
/* 166:126 */     UserServiceDB.createServiceForwarding(account_id, keyword, url, timeout, listen_status);
/* 167:    */   }
/* 168:    */   
/* 169:    */   public static HashMap viewServiceForwarding(String account_id, String keyword)
/* 170:    */     throws Exception
/* 171:    */   {
/* 172:130 */     return UserServiceDB.viewServiceForwarding(account_id, keyword);
/* 173:    */   }
/* 174:    */   
/* 175:    */   public static void deleteServiceForwarding(String account_id, String keyword)
/* 176:    */     throws Exception
/* 177:    */   {
/* 178:134 */     UserServiceDB.deleteServiceForwarding(account_id, keyword);
/* 179:    */   }
/* 180:    */   
/* 181:    */   public static void deleteServiceForwarding(String account_id, ArrayList keywords)
/* 182:    */     throws Exception
/* 183:    */   {
/* 184:138 */     UserServiceDB.deleteServiceForwarding(account_id, keywords);
/* 185:    */   }
/* 186:    */   
/* 187:    */   public static void updateServiceForwarding(String update_account_id, String update_keyword, String new_account_id, String new_keyword, String new_url, String new_timeout, String new_listen_status)
/* 188:    */     throws Exception
/* 189:    */   {
/* 190:143 */     UserServiceDB.updateServiceForwarding(update_account_id, update_keyword, new_account_id, new_keyword, new_url, new_timeout, new_listen_status);
/* 191:    */   }
/* 192:    */   
/* 193:    */   public static UserService viewServiceByAlias(String alias, String accountId)
/* 194:    */     throws Exception
/* 195:    */   {
/* 196:149 */     return UserServiceDB.viewServiceByAlias(alias, accountId);
/* 197:    */   }
/* 198:    */   
/* 199:    */   public static String viewLastRequestedKeyword(String msisdn, String accountId, String siteId)
/* 200:    */     throws Exception
/* 201:    */   {
/* 202:154 */     return UserServiceDB.viewLastRequestedKeyword(msisdn, accountId, siteId);
/* 203:    */   }
/* 204:    */   
/* 205:    */   public static int getLastAccessCount(String msisdn, String accountId, String keyword)
/* 206:    */     throws Exception
/* 207:    */   {
/* 208:158 */     return UserServiceDB.getLastAccessCount(msisdn, accountId, keyword);
/* 209:    */   }
/* 210:    */   
/* 211:    */   public static String viewLastSubscribedKeyword(String msisdn, String accountId)
/* 212:    */     throws Exception
/* 213:    */   {
/* 214:163 */     return UserServiceDB.viewLastSubscribedKeyword(msisdn, accountId);
/* 215:    */   }
/* 216:    */   
/* 217:    */   public static ArrayList viewAllServices(String accountId)
/* 218:    */     throws Exception
/* 219:    */   {
/* 220:167 */     return UserServiceDB.viewAllServices(accountId);
/* 221:    */   }
/* 222:    */   
/* 223:    */   public static ArrayList viewAllServices(String accountId, String serviceType)
/* 224:    */     throws Exception
/* 225:    */   {
/* 226:172 */     return UserServiceDB.viewAllServices(accountId, serviceType);
/* 227:    */   }
/* 228:    */   
/* 229:    */   public static ArrayList<UserService> viewAllServices(String accountId, String serviceType, String command)
/* 230:    */     throws Exception
/* 231:    */   {
/* 232:177 */     return UserServiceDB.viewAllServices(accountId, serviceType, command);
/* 233:    */   }
/* 234:    */   
/* 235:    */   public static ArrayList viewAllServicesOfParentType(String accountId, String parentType)
/* 236:    */     throws Exception
/* 237:    */   {
/* 238:182 */     return UserServiceDB.viewAllServicesOfParentType(accountId, parentType);
/* 239:    */   }
/* 240:    */   
/* 241:    */   public static ArrayList viewAllServicesForType(String serviceType)
/* 242:    */     throws Exception
/* 243:    */   {
/* 244:187 */     return UserServiceDB.viewAllServicesForType(serviceType);
/* 245:    */   }
/* 246:    */   
/* 247:    */   public static HashMap populateRoutingTable()
/* 248:    */     throws Exception
/* 249:    */   {
/* 250:191 */     return UserServiceDB.populateRoutingTable();
/* 251:    */   }
/* 252:    */   
/* 253:    */   public static HashMap getServiceTable()
/* 254:    */     throws Exception
/* 255:    */   {
/* 256:195 */     return UserServiceDB.getServiceTable();
/* 257:    */   }
/* 258:    */   
/* 259:    */   public static String[][] getCPsForTypes()
/* 260:    */     throws Exception
/* 261:    */   {
/* 262:199 */     return UserServiceDB.getCPsForTypes();
/* 263:    */   }
/* 264:    */   
/* 265:    */   public static String[] getCPIDsForServiceType(String serviceType)
/* 266:    */     throws Exception
/* 267:    */   {
/* 268:203 */     return UserServiceDB.getCPIDsForServiceType(serviceType);
/* 269:    */   }
/* 270:    */   
/* 271:    */   public static HashMap<String, String> getCPIDsForServiceType(String serviceType, String command)
/* 272:    */     throws Exception
/* 273:    */   {
/* 274:207 */     return UserServiceDB.getCPIDsForServiceType(serviceType, command);
/* 275:    */   }
/* 276:    */   
/* 277:    */   public static void updateSubscriptionStatus(String msisdn, String keyword, String accountId, int status)
/* 278:    */     throws Exception
/* 279:    */   {
/* 280:211 */     UserServiceDB.updateSubscriptionStatus(msisdn, keyword, accountId, status);
/* 281:    */   }
/* 282:    */   
/* 283:    */   public static String subscribeToService(String msisdn, String keyword, String accountId)
/* 284:    */     throws Exception
/* 285:    */   {
/* 286:215 */     return UserServiceDB.subscribeToService(msisdn, keyword, accountId);
/* 287:    */   }
/* 288:    */   
/* 289:    */   public static String[] subscribeToService(String msisdn, ArrayList keywords, String accountId)
/* 290:    */     throws Exception
/* 291:    */   {
/* 292:219 */     return UserServiceDB.subscribeToService(msisdn, keywords, accountId);
/* 293:    */   }
/* 294:    */   
/* 295:    */   public static String[] subscribeToService(String msisdn, ArrayList keywords, String accountId, int numOfDays)
/* 296:    */     throws Exception
/* 297:    */   {
/* 298:223 */     return UserServiceDB.subscribeToService(msisdn, keywords, accountId, numOfDays);
/* 299:    */   }
/* 300:    */   
/* 301:    */   public static String[] subscribeToService(String msisdn, ArrayList keywords, String accountId, int numOfDays, int status, int billingType)
/* 302:    */     throws Exception
/* 303:    */   {
/* 304:227 */     return UserServiceDB.subscribeToService(msisdn, keywords, accountId, numOfDays, status, billingType);
/* 305:    */   }
/* 306:    */   
/* 307:    */   public static void resumeSubscription(String msisdn, ArrayList keywords, String accountId, int status, Date subscriptionDate, Date nextSubscriptionDate)
/* 308:    */     throws Exception
/* 309:    */   {
/* 310:231 */     UserServiceDB.resumeSubscription(msisdn, keywords, accountId, status, subscriptionDate, nextSubscriptionDate);
/* 311:    */   }
/* 312:    */   
/* 313:    */   public static void unsubscribeToService(String msisdn, String keyword, String accountId)
/* 314:    */     throws Exception
/* 315:    */   {
/* 316:235 */     UserServiceDB.unsubscribeToService(msisdn, keyword, accountId);
/* 317:    */   }
/* 318:    */   
/* 319:    */   public static void forceUnsubscribe(String keyword, String accountId)
/* 320:    */     throws Exception
/* 321:    */   {
/* 322:239 */     UserServiceDB.forceUnsubscribe(keyword, accountId);
/* 323:    */   }
/* 324:    */   
/* 325:    */   public static void forceUnsubscribe(String msisdn, ArrayList keywords, String accountId)
/* 326:    */     throws Exception
/* 327:    */   {
/* 328:243 */     UserServiceDB.forceUnsubscribe(msisdn, keywords, accountId);
/* 329:    */   }
/* 330:    */   
/* 331:    */   public static boolean verifyUser(String msisdn, String regId, String acctId, String keyword)
/* 332:    */     throws Exception
/* 333:    */   {
/* 334:247 */     return UserServiceDB.verifyUser(msisdn, regId, acctId, keyword);
/* 335:    */   }
/* 336:    */   
/* 337:    */   public static boolean isRegistered(String msisdn, String accountId)
/* 338:    */     throws Exception
/* 339:    */   {
/* 340:251 */     return UserServiceDB.isRegistered(msisdn, accountId);
/* 341:    */   }
/* 342:    */   
/* 343:    */   public static boolean isRegistered(String msisdn, String accountId, String keyword)
/* 344:    */     throws Exception
/* 345:    */   {
/* 346:255 */     return UserServiceDB.isRegistered(msisdn, accountId, keyword);
/* 347:    */   }
/* 348:    */   
/* 349:    */   public static boolean isSubscribed(String msisdn, String accountId, String keyword)
/* 350:    */     throws Exception
/* 351:    */   {
/* 352:259 */     return UserServiceDB.isSubscribed(msisdn, accountId, keyword);
/* 353:    */   }
/* 354:    */   
/* 355:    */   public static HashMap getSubscription(String msisdn, String accountId, String keyword, String alternativeKeyword)
/* 356:    */     throws Exception
/* 357:    */   {
/* 358:263 */     return UserServiceDB.getSubscription(msisdn, accountId, keyword, alternativeKeyword);
/* 359:    */   }
/* 360:    */   
/* 361:    */   public static HashMap getRecentUnsubscription(String msisdn, String keyword, String accountID, Date now)
/* 362:    */     throws Exception
/* 363:    */   {
/* 364:267 */     return UserServiceDB.getRecentUnsubscription(msisdn, keyword, accountID, now);
/* 365:    */   }
/* 366:    */   
/* 367:    */   public static boolean hasRecentUnsubscription(String msisdn, String keyword, String accountID)
/* 368:    */     throws Exception
/* 369:    */   {
/* 370:271 */     return UserServiceDB.hasRecentUnsubscription(msisdn, keyword, accountID);
/* 371:    */   }
/* 372:    */   
/* 373:    */   public static boolean isMonthlySubscriber(String msisdn, String accountId)
/* 374:    */     throws Exception
/* 375:    */   {
/* 376:275 */     return UserServiceDB.isMonthlySubscriber(msisdn, accountId);
/* 377:    */   }
/* 378:    */   
/* 379:    */   public static int[] updateServicesRSSFeeds(String accountId, String keyword, ArrayList feedIds)
/* 380:    */     throws Exception
/* 381:    */   {
/* 382:279 */     return UserServiceDB.updateServiceRSSFeeds(accountId, keyword, feedIds);
/* 383:    */   }
/* 384:    */   
/* 385:    */   public static ArrayList viewServiceRSSFeeds(String accountId, String keyword)
/* 386:    */     throws Exception
/* 387:    */   {
/* 388:283 */     return UserServiceDB.viewServiceRSSFeeds(accountId, keyword);
/* 389:    */   }
/* 390:    */   
/* 391:    */   public static ArrayList getKeywordsOfBasicServices(String accountId)
/* 392:    */     throws Exception
/* 393:    */   {
/* 394:287 */     return UserServiceDB.getKeywordsOfBasicServices(accountId);
/* 395:    */   }
/* 396:    */   
/* 397:    */   public static ArrayList getKeywordsUserSubscribedTo(String msisdn, String accountId)
/* 398:    */     throws Exception
/* 399:    */   {
/* 400:291 */     return UserServiceDB.getKeywordsUserSubscribedTo(msisdn, accountId);
/* 401:    */   }
/* 402:    */   
/* 403:    */   public static ArrayList getKeywordsUserSubscribedTo(String msisdn, String accountId, ArrayList<String> keywordList)
/* 404:    */     throws Exception
/* 405:    */   {
/* 406:295 */     return UserServiceDB.getKeywordsUserSubscribedTo(msisdn, accountId, keywordList);
/* 407:    */   }
/* 408:    */   
/* 409:    */   public static Page viewSubscribers(String csid, String[] keywords, Timestamp startdate, Timestamp enddate, int start, int count)
/* 410:    */     throws Exception
/* 411:    */   {
/* 412:300 */     return UserServiceDB.viewSubscribers(csid, keywords, startdate, enddate, start, count);
/* 413:    */   }
/* 414:    */   
/* 415:    */   public static String[] viewAllSubscribers(String csid, String keyword)
/* 416:    */     throws Exception
/* 417:    */   {
/* 418:304 */     return UserServiceDB.viewAllSubscribers(csid, keyword);
/* 419:    */   }
/* 420:    */   
/* 421:    */   public static String[] viewAllSubscribersWithStatus(String csid, String keyword, int status)
/* 422:    */     throws Exception
/* 423:    */   {
/* 424:308 */     return UserServiceDB.viewAllSubscribersWithStatus(csid, keyword, status);
/* 425:    */   }
/* 426:    */   
/* 427:    */   public static HashMap viewSubscribersGroupByNetworkPrefix(String accountid, String keyword, int status)
/* 428:    */     throws Exception
/* 429:    */   {
/* 430:312 */     return UserServiceDB.viewSubscribersGroupByNetworkPrefix(accountid, keyword, status);
/* 431:    */   }
/* 432:    */   
/* 433:    */   public static HashMap viewSubscribersGroupByNetworkPrefix(String accountid, String keyword, String alias, int status)
/* 434:    */     throws Exception
/* 435:    */   {
/* 436:316 */     return UserServiceDB.viewSubscribersGroupByNetworkPrefix(accountid, keyword, alias, status);
/* 437:    */   }
/* 438:    */   
/* 439:    */   public static HashMap viewSubscribersGroupByNetworkPrefix(String accountid, String keyword, int start, int count)
/* 440:    */     throws Exception
/* 441:    */   {
/* 442:320 */     return UserServiceDB.viewSubscribersGroupByNetworkPrefix(accountid, keyword, start, count);
/* 443:    */   }
/* 444:    */   
/* 445:    */   public static ArrayList viewTempSubscribersGroupByNetworkPrefix(String accountid, String keyword, String processId)
/* 446:    */     throws Exception
/* 447:    */   {
/* 448:324 */     return UserServiceDB.viewTempSubscribersGroupByNetworkPrefix(accountid, keyword, processId);
/* 449:    */   }
/* 450:    */   
/* 451:    */   public static ArrayList viewTempSubscribersGroupByNetworkPrefix(String accountid, String processId)
/* 452:    */     throws Exception
/* 453:    */   {
/* 454:328 */     return UserServiceDB.viewTempSubscribersGroupByNetworkPrefix(accountid, processId);
/* 455:    */   }
/* 456:    */   
/* 457:    */   public static HashMap<String, String[]> viewTempSubscribersGroupByNetworkPrefix(String accountId, int status, Date today)
/* 458:    */     throws Exception
/* 459:    */   {
/* 460:333 */     return UserServiceDB.viewTempSubscribersGroupByNetworkPrefix(accountId, status, today);
/* 461:    */   }
/* 462:    */   
/* 463:    */   public static String findKeywordForMapping(String mapping, String accountId)
/* 464:    */     throws Exception
/* 465:    */   {
/* 466:337 */     return UserServiceDB.findKeywordForMapping(mapping, accountId);
/* 467:    */   }
/* 468:    */   
/* 469:    */   public static boolean stoppedB4SubscriptionEnded(String msisdn, String keyword, String accountID, Date now)
/* 470:    */     throws Exception
/* 471:    */   {
/* 472:341 */     return UserServiceDB.stoppedB4SubscriptionEnded(msisdn, keyword, accountID, now);
/* 473:    */   }
/* 474:    */   
/* 475:    */   public static void createTransaction(UserServiceTransaction trans)
/* 476:    */     throws Exception
/* 477:    */   {
/* 478:346 */     UserServiceTransactionDB.createTransaction(trans);
/* 479:    */   }
/* 480:    */   
/* 481:    */   public static void updateTransaction(String transId, int isCompleted, int isBilled)
/* 482:    */     throws Exception
/* 483:    */   {
/* 484:350 */     UserServiceTransactionDB.updateTransaction(transId, isCompleted, isBilled);
/* 485:    */   }
/* 486:    */   
/* 487:    */   public static void deleteTransaction(String transId)
/* 488:    */     throws Exception
/* 489:    */   {
/* 490:354 */     UserServiceTransactionDB.deleteTransaction(transId);
/* 491:    */   }
/* 492:    */   
/* 493:    */   public static UserServiceTransaction viewTransaction(String transId)
/* 494:    */     throws Exception
/* 495:    */   {
/* 496:358 */     return UserServiceTransactionDB.viewTransaction(transId);
/* 497:    */   }
/* 498:    */   
/* 499:    */   public static void deleteTempSubscriptionRecord(String msisdn, String accountId, String processId)
/* 500:    */     throws Exception
/* 501:    */   {
/* 502:362 */     UserServiceDB.deleteTempSubscriptionRecord(msisdn, accountId, processId);
/* 503:    */   }
/* 504:    */   
/* 505:    */   public static String viewNextSubscriptionDate(String msisdn, String accountId, String keyword)
/* 506:    */     throws Exception
/* 507:    */   {
/* 508:366 */     return UserServiceDB.viewNextSubscriptionDate(msisdn, accountId, keyword);
/* 509:    */   }
/* 510:    */ }



/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar

 * Qualified Name:     com.rancard.mobility.infoserver.common.services.ServiceManager

 * JD-Core Version:    0.7.0.1

 */