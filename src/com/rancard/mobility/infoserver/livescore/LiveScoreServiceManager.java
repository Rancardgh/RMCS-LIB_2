/*   1:    */ package com.rancard.mobility.infoserver.livescore;
/*   2:    */ 
/*   3:    */ import com.rancard.mobility.infoserver.common.services.UserService;
/*   4:    */ import com.rancard.util.PaginatedList;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.PrintStream;
/*   7:    */ import java.sql.Timestamp;
/*   8:    */ import java.util.ArrayList;
/*   9:    */ import java.util.Date;
/*  10:    */ import java.util.HashMap;
/*  11:    */ import org.apache.commons.httpclient.HttpClient;
/*  12:    */ import org.apache.commons.httpclient.HttpException;
/*  13:    */ import org.apache.commons.httpclient.methods.GetMethod;
/*  14:    */ 
/*  15:    */ public class LiveScoreServiceManager
/*  16:    */ {
/*  17:    */   public static void createLiveScoreService(LiveScoreService service)
/*  18:    */     throws Exception
/*  19:    */   {
/*  20: 31 */     LiveScoreServiceDB.createLiveScoreService(service);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public static void updateLiveScoreService(String keyword, String account_id, String serviceName)
/*  24:    */     throws Exception
/*  25:    */   {
/*  26: 35 */     LiveScoreServiceDB.updateLiveScoreServiceName(keyword, account_id, serviceName);
/*  27:    */   }
/*  28:    */   
/*  29:    */   public static void updateSubscriptionStatus(String gameId, int subStatus, int gameStatus)
/*  30:    */     throws Exception
/*  31:    */   {
/*  32: 39 */     LiveScoreFixtureDB.updateSubscriptionStatus(gameId, subStatus, gameStatus);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public static LiveScoreService viewLiveScoreService(String keyword, String accountId)
/*  36:    */     throws Exception
/*  37:    */   {
/*  38: 43 */     return LiveScoreServiceDB.viewLiveScoreService(keyword, accountId);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public static ArrayList viewAllLiveScoreLeagues(String accountId)
/*  42:    */     throws Exception
/*  43:    */   {
/*  44: 47 */     return LiveScoreServiceDB.viewAllLiveScoreLeagues(accountId);
/*  45:    */   }
/*  46:    */   
/*  47:    */   public static void deleteLiveScoreService(String keyword, String account_id)
/*  48:    */     throws Exception
/*  49:    */   {
/*  50: 51 */     LiveScoreServiceDB.deleteLiveScoreService(keyword, account_id);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public static ArrayList getAccount_KeywordPairForService(String liveScoreServiceID)
/*  54:    */     throws Exception
/*  55:    */   {
/*  56: 55 */     return LiveScoreServiceDB.getAccount_KeywordPairForService(liveScoreServiceID);
/*  57:    */   }
/*  58:    */   
/*  59:    */   public static String getKeywordForService(String liveScoreServiceID, String accountId)
/*  60:    */     throws Exception
/*  61:    */   {
/*  62: 59 */     return LiveScoreServiceDB.getKeywordForService(liveScoreServiceID, accountId);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public static String[][] getAvailableLiveScoreServices()
/*  66:    */     throws Exception
/*  67:    */   {
/*  68: 63 */     return LiveScoreServiceDB.getAvailableLiveScoreServices();
/*  69:    */   }
/*  70:    */   
/*  71:    */   public static String[][] getAvailableLiveScoreServices(String accountId)
/*  72:    */     throws Exception
/*  73:    */   {
/*  74: 67 */     return LiveScoreServiceDB.getAvailableLiveScoreServices(accountId);
/*  75:    */   }
/*  76:    */   
/*  77:    */   public static String[] subscribeToLiveScoreServices(String accountId, String msisdn, int numOfDays, int billingType)
/*  78:    */     throws Exception
/*  79:    */   {
/*  80: 71 */     return LiveScoreServiceDB.subscribeToLiveScoreServices(accountId, msisdn, numOfDays, billingType);
/*  81:    */   }
/*  82:    */   
/*  83:    */   public static boolean isRegisteredToLiveScoreServices(String accountId, String msisdn)
/*  84:    */     throws Exception
/*  85:    */   {
/*  86: 75 */     return LiveScoreServiceDB.isRegisteredToLiveScoreServices(accountId, msisdn);
/*  87:    */   }
/*  88:    */   
/*  89:    */   public static boolean isRegisteredToLiveScoreServices(String accountId, String msisdn, int status)
/*  90:    */     throws Exception
/*  91:    */   {
/*  92: 79 */     return LiveScoreServiceDB.isRegisteredToLiveScoreServices(accountId, msisdn, status);
/*  93:    */   }
/*  94:    */   
/*  95:    */   public static boolean isMultiShortCodeLivesScore(String accountId)
/*  96:    */     throws Exception
/*  97:    */   {
/*  98: 83 */     return LiveScoreServiceDB.isMultiShortCodeLivesScore(accountId);
/*  99:    */   }
/* 100:    */   
/* 101:    */   public static String manageNextLivescoreSubscription(Date today)
/* 102:    */     throws Exception
/* 103:    */   {
/* 104: 87 */     return LiveScoreServiceDB.manageNextLivescoreSubscription(today);
/* 105:    */   }
/* 106:    */   
/* 107:    */   public static int manageNextLivescoreSubscription(Date today, String accountId)
/* 108:    */     throws Exception
/* 109:    */   {
/* 110: 90 */     return LiveScoreServiceDB.manageNextLivescoreSubscription(today, accountId);
/* 111:    */   }
/* 112:    */   
/* 113:    */   public static UserService viewLiveScoreSubscriptionService(String accountId)
/* 114:    */     throws Exception
/* 115:    */   {
/* 116: 93 */     return LiveScoreServiceDB.viewLiveScoreSubscriptionService(accountId);
/* 117:    */   }
/* 118:    */   
/* 119:    */   public static UserService viewHeadLiveScoreService(String accountId)
/* 120:    */     throws Exception
/* 121:    */   {
/* 122: 97 */     return LiveScoreServiceDB.viewHeadLiveScoreService(accountId);
/* 123:    */   }
/* 124:    */   
/* 125:    */   public static void createFixture(LiveScoreFixture game)
/* 126:    */     throws Exception
/* 127:    */   {
/* 128:102 */     LiveScoreFixtureDB.createFixture(game);
/* 129:    */   }
/* 130:    */   
/* 131:    */   public static HashMap viewAllFixturesForDate(String date, int status)
/* 132:    */     throws Exception
/* 133:    */   {
/* 134:106 */     return LiveScoreFixtureDB.viewAllFixturesForDate(date, status);
/* 135:    */   }
/* 136:    */   
/* 137:    */   public static HashMap viewAllFixturesForDateGroupByCP(String date, int status, int notifStatus)
/* 138:    */     throws Exception
/* 139:    */   {
/* 140:109 */     return LiveScoreFixtureDB.viewAllFixturesForDateGroupByCP(date, status, notifStatus);
/* 141:    */   }
/* 142:    */   
/* 143:    */   public static ArrayList viewDistinctGameTimesForDate(Date date, int status)
/* 144:    */     throws Exception
/* 145:    */   {
/* 146:113 */     return LiveScoreFixtureDB.viewDistinctGameTimesForDate(date, status);
/* 147:    */   }
/* 148:    */   
/* 149:    */   public static HashMap viewAllActiveFixturesForDate(String date)
/* 150:    */     throws Exception
/* 151:    */   {
/* 152:117 */     return LiveScoreFixtureDB.viewAllActiveFixturesForDate(date);
/* 153:    */   }
/* 154:    */   
/* 155:    */   public static HashMap viewAllActiveFixturesForDate(String accountId, String date)
/* 156:    */     throws Exception
/* 157:    */   {
/* 158:121 */     return LiveScoreFixtureDB.viewAllActiveFixturesForDate(accountId, date);
/* 159:    */   }
/* 160:    */   
/* 161:    */   public static ArrayList viewAllActiveFixturesInLeague(String date, String keyword, String accountId)
/* 162:    */     throws Exception
/* 163:    */   {
/* 164:125 */     return LiveScoreFixtureDB.viewAllActiveFixturesInLeague(date, keyword, accountId);
/* 165:    */   }
/* 166:    */   
/* 167:    */   public static ArrayList viewAllActiveFixturesForCPInLeague(String date, String keyword, String accountId)
/* 168:    */     throws Exception
/* 169:    */   {
/* 170:128 */     return LiveScoreFixtureDB.viewAllActiveFixturesForCPInLeague(date, keyword, accountId);
/* 171:    */   }
/* 172:    */   
/* 173:    */   public static ArrayList viewAllActiveFixturesInAllLeagues(String date)
/* 174:    */     throws Exception
/* 175:    */   {
/* 176:132 */     return LiveScoreFixtureDB.viewAllActiveFixturesInAllLeagues(date);
/* 177:    */   }
/* 178:    */   
/* 179:    */   public static ArrayList viewFixtures(String keyword, String accountId, String date)
/* 180:    */     throws Exception
/* 181:    */   {
/* 182:136 */     return LiveScoreFixtureDB.viewFixtures(keyword, accountId, date);
/* 183:    */   }
/* 184:    */   
/* 185:    */   public static ArrayList viewAllActiveFixturesInLeague(String date, String keyword, String accountId, String team)
/* 186:    */     throws Exception
/* 187:    */   {
/* 188:141 */     return LiveScoreFixtureDB.viewAllActiveFixturesInLeague(date, keyword, accountId, team);
/* 189:    */   }
/* 190:    */   
/* 191:    */   public static ArrayList viewAllActiveFixturesForCPInLeague(String date, String keyword, String accountId, String team)
/* 192:    */     throws Exception
/* 193:    */   {
/* 194:144 */     return LiveScoreFixtureDB.viewAllActiveFixturesForCPInLeague(date, keyword, accountId, team);
/* 195:    */   }
/* 196:    */   
/* 197:    */   public static LiveScoreFixture viewFixture(String gameId)
/* 198:    */     throws Exception
/* 199:    */   {
/* 200:148 */     return LiveScoreFixtureDB.viewFixture(gameId);
/* 201:    */   }
/* 202:    */   
/* 203:    */   public static LiveScoreFixture viewFixtureForCP(String gameId, String accountId)
/* 204:    */     throws Exception
/* 205:    */   {
/* 206:151 */     return LiveScoreFixtureDB.viewFixtureForCP(gameId, accountId);
/* 207:    */   }
/* 208:    */   
/* 209:    */   public static LiveScoreFixture viewFixture(String gameId, String date)
/* 210:    */     throws Exception
/* 211:    */   {
/* 212:154 */     return LiveScoreFixtureDB.viewFixture(gameId, date);
/* 213:    */   }
/* 214:    */   
/* 215:    */   public static LiveScoreFixture viewFixtureByAlias(String alias)
/* 216:    */     throws Exception
/* 217:    */   {
/* 218:158 */     return LiveScoreFixtureDB.viewFixtureByAlias(alias);
/* 219:    */   }
/* 220:    */   
/* 221:    */   public static LiveScoreFixture viewFixtureForCPByAlias(String alias, String accountId)
/* 222:    */     throws Exception
/* 223:    */   {
/* 224:161 */     return LiveScoreFixtureDB.viewFixtureForCPByAlias(alias, accountId);
/* 225:    */   }
/* 226:    */   
/* 227:    */   public static void deleteFixture(String keyword)
/* 228:    */     throws Exception
/* 229:    */   {
/* 230:164 */     LiveScoreFixtureDB.deleteFixture(keyword);
/* 231:    */   }
/* 232:    */   
/* 233:    */   public static void updateFixture(LiveScoreFixture update)
/* 234:    */     throws Exception
/* 235:    */   {
/* 236:168 */     LiveScoreFixtureDB.updateFixture(update);
/* 237:    */   }
/* 238:    */   
/* 239:    */   public static void updateFixture(String match_id, int status, String date)
/* 240:    */     throws Exception
/* 241:    */   {
/* 242:172 */     LiveScoreFixtureDB.updateFixture(match_id, status, date);
/* 243:    */   }
/* 244:    */   
/* 245:    */   public static void updateFixture(String match_id, String homeScore, String awayScore, String date)
/* 246:    */     throws Exception
/* 247:    */   {
/* 248:176 */     LiveScoreFixtureDB.updateFixture(match_id, homeScore, awayScore, date);
/* 249:    */   }
/* 250:    */   
/* 251:    */   public static void updateFixture(String match_id, String homeScore, String awayScore, int status, String date)
/* 252:    */     throws Exception
/* 253:    */   {
/* 254:180 */     LiveScoreFixtureDB.updateFixture(match_id, homeScore, awayScore, status, date);
/* 255:    */   }
/* 256:    */   
/* 257:    */   public static synchronized void subscribeForGame(String accountId, String gameId, String msisdn)
/* 258:    */     throws Exception
/* 259:    */   {
/* 260:184 */     LiveScoreFixtureDB.subscribeForGame(accountId, gameId, msisdn);
/* 261:    */   }
/* 262:    */   
/* 263:    */   public static synchronized void unsubscribeFromGame(String msisdn, String gameId, String accountId)
/* 264:    */     throws Exception
/* 265:    */   {
/* 266:188 */     LiveScoreFixtureDB.unsubscribeFromGame(msisdn, gameId, accountId);
/* 267:    */   }
/* 268:    */   
/* 269:    */   public static String viewGameIdForAlias(String alias)
/* 270:    */     throws Exception
/* 271:    */   {
/* 272:192 */     return LiveScoreFixtureDB.viewGameIdForAlias(alias);
/* 273:    */   }
/* 274:    */   
/* 275:    */   public static String viewAliasForGameId(String gameId)
/* 276:    */     throws Exception
/* 277:    */   {
/* 278:196 */     return LiveScoreFixtureDB.viewAliasForGameId(gameId);
/* 279:    */   }
/* 280:    */   
/* 281:    */   public static boolean canAccommodateSubscriber(String accountId, String msisdn, LiveScoreFixture game)
/* 282:    */     throws Exception
/* 283:    */   {
/* 284:200 */     return LiveScoreServiceDB.canAccommodateSubscriber(accountId, msisdn, game);
/* 285:    */   }
/* 286:    */   
/* 287:    */   public static boolean createUpdate(LiveScoreUpdate update)
/* 288:    */     throws Exception
/* 289:    */   {
/* 290:205 */     return LiveScoreUpdateDB.createUpdate(update);
/* 291:    */   }
/* 292:    */   
/* 293:    */   public static String viewUpdateMessage(String updateId, String language)
/* 294:    */     throws Exception
/* 295:    */   {
/* 296:209 */     return LiveScoreUpdateDB.viewUpdateMessage(updateId, language);
/* 297:    */   }
/* 298:    */   
/* 299:    */   public static LiveScoreUpdate viewUpdate(String updateId)
/* 300:    */     throws Exception
/* 301:    */   {
/* 302:213 */     return LiveScoreUpdateDB.viewUpdate(updateId);
/* 303:    */   }
/* 304:    */   
/* 305:    */   public static LiveScoreUpdate viewUpdate(String updateId, int gameStatus)
/* 306:    */     throws Exception
/* 307:    */   {
/* 308:216 */     return LiveScoreUpdateDB.viewUpdate(updateId, gameStatus);
/* 309:    */   }
/* 310:    */   
/* 311:    */   public static ArrayList viewAllUpdates(String gameId)
/* 312:    */     throws Exception
/* 313:    */   {
/* 314:220 */     return LiveScoreUpdateDB.viewAllUpdates(gameId);
/* 315:    */   }
/* 316:    */   
/* 317:    */   public static ArrayList viewAllUpdates(String gameId, String date)
/* 318:    */     throws Exception
/* 319:    */   {
/* 320:224 */     return LiveScoreUpdateDB.viewAllUpdates(gameId, date);
/* 321:    */   }
/* 322:    */   
/* 323:    */   public static ArrayList viewSubscribersForGame(String accountId, String game_id, String alias)
/* 324:    */     throws Exception
/* 325:    */   {
/* 326:228 */     return LiveScoreServiceDB.viewSubscribersForGame(accountId, game_id, alias);
/* 327:    */   }
/* 328:    */   
/* 329:    */   public static PaginatedList viewSubscribersForGame(String accountId, String game_id, String alias, int[] pagingParams, String[] sortParams)
/* 330:    */     throws Exception
/* 331:    */   {
/* 332:232 */     return LiveScoreServiceDB.viewSubscribersForGame(accountId, game_id, alias, pagingParams, sortParams);
/* 333:    */   }
/* 334:    */   
/* 335:    */   public static int viewNoSubscribersForGame(String accountId, String game_id)
/* 336:    */     throws Exception
/* 337:    */   {
/* 338:236 */     return LiveScoreServiceDB.viewNoSubscribersForGame(accountId, game_id);
/* 339:    */   }
/* 340:    */   
/* 341:    */   public static int getBillingTypeForSubscriber(String msisdn, String accountId)
/* 342:    */     throws Exception
/* 343:    */   {
/* 344:240 */     return LiveScoreServiceDB.getBillingTypeForSubscriber(msisdn, accountId);
/* 345:    */   }
/* 346:    */   
/* 347:    */   public static void sendUpdate(String serverUrl, String accountId, String updateId)
/* 348:    */     throws HttpException, IOException, Exception
/* 349:    */   {
/* 350:247 */     String url = serverUrl + "sendlivescoreupdate?acctId=" + accountId + "&updateId=" + updateId;
/* 351:    */     
/* 352:249 */     HttpClient client = new HttpClient();
/* 353:250 */     GetMethod httpGETFORM = new GetMethod(url);
/* 354:251 */     String resp = "";
/* 355:    */     try
/* 356:    */     {
/* 357:254 */       client.executeMethod(httpGETFORM);
/* 358:    */     }
/* 359:    */     catch (HttpException e)
/* 360:    */     {
/* 361:256 */       resp = "5001: " + e.getMessage();
/* 362:    */       
/* 363:258 */       System.out.println("error response: " + resp);
/* 364:    */     }
/* 365:    */     catch (IOException e)
/* 366:    */     {
/* 367:261 */       resp = "5002: " + e.getMessage();
/* 368:    */       
/* 369:263 */       System.out.println("error response: " + resp);
/* 370:    */     }
/* 371:    */     catch (Exception e)
/* 372:    */     {
/* 373:267 */       System.out.println("error response: " + e.getMessage());
/* 374:    */     }
/* 375:    */     finally
/* 376:    */     {
/* 377:271 */       httpGETFORM.releaseConnection();
/* 378:272 */       client = null;
/* 379:273 */       httpGETFORM = null;
/* 380:    */     }
/* 381:    */   }
/* 382:    */   
/* 383:    */   public static boolean createTriggerSchedule(Timestamp triggerTime)
/* 384:    */     throws Exception
/* 385:    */   {
/* 386:279 */     return LiveScoreTriggerScheduleDB.createTriggerSchedule(triggerTime);
/* 387:    */   }
/* 388:    */   
/* 389:    */   public static void updateTriggerSchedule(Timestamp triggerTime)
/* 390:    */     throws Exception
/* 391:    */   {
/* 392:283 */     LiveScoreTriggerScheduleDB.updateTriggerSchedule(triggerTime);
/* 393:    */   }
/* 394:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.infoserver.livescore.LiveScoreServiceManager
 * JD-Core Version:    0.7.0.1
 */