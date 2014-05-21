/*   1:    */ package com.rancard.mobility.common;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import com.rancard.common.uidGen;
/*   5:    */ import com.rancard.mobility.contentserver.CPConnections;
/*   6:    */ import com.rancard.mobility.infoserver.InfoService;
/*   7:    */ import com.rancard.mobility.infoserver.common.services.UserServiceTransaction;
/*   8:    */ import com.rancard.util.URLUTF8Encoder;
/*   9:    */ import com.rancard.util.payment.PaymentManager;
/*  10:    */ import com.rancard.util.payment.PricePoint;
/*  11:    */ import java.io.BufferedReader;
/*  12:    */ import java.io.InputStream;
/*  13:    */ import java.io.InputStreamReader;
/*  14:    */ import java.io.PrintStream;
/*  15:    */ import java.sql.BatchUpdateException;
/*  16:    */ import java.sql.Connection;
/*  17:    */ import java.sql.PreparedStatement;
/*  18:    */ import java.sql.ResultSet;
/*  19:    */ import java.sql.SQLException;
/*  20:    */ import java.sql.Statement;
/*  21:    */ import java.util.ArrayList;
/*  22:    */ import java.util.Calendar;
/*  23:    */ import java.util.Date;
/*  24:    */ import java.util.List;
/*  25:    */ import org.apache.commons.httpclient.HttpClient;
/*  26:    */ import org.apache.commons.httpclient.methods.GetMethod;
/*  27:    */ import org.apache.commons.httpclient.params.HttpMethodParams;
/*  28:    */ 
/*  29:    */ public class TempBatchTransmission
/*  30:    */   implements Runnable
/*  31:    */ {
/*  32: 40 */   final long MILLISECONDS_BETWEEN_RETRY = 0L;
/*  33: 41 */   final long MILLISECONDS_BETWEEN_TRANSMIT = 0L;
/*  34: 42 */   final long MILLISECONDS_BETWEEN_BATCHES = 10000L;
/*  35: 43 */   final int BATCH_SIZE = 100;
/*  36: 44 */   final int URL_CALL_TIMEOUT = 9000;
/*  37: 45 */   final int NUM_OF_RETRIES = 3;
/*  38:    */   private List subscribers;
/*  39:    */   private InfoService service;
/*  40:    */   
/*  41:    */   public TempBatchTransmission()
/*  42:    */   {
/*  43: 50 */     this.subscribers = new ArrayList();
/*  44: 51 */     this.service = new InfoService();
/*  45:    */   }
/*  46:    */   
/*  47:    */   public TempBatchTransmission(List subscribers, InfoService service)
/*  48:    */   {
/*  49: 55 */     this.subscribers = subscribers;
/*  50: 56 */     this.service = service;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void run()
/*  54:    */   {
/*  55:    */     try
/*  56:    */     {
/*  57: 62 */       ArrayList<UserServiceTransaction> transactionList = new ArrayList();
/*  58:    */       
/*  59: 64 */       long startTime = 0L;
/*  60: 65 */       long stopTime = 0L;
/*  61: 66 */       long totalTime = 0L;
/*  62: 67 */       int delivery_count = 0;
/*  63:    */       
/*  64: 69 */       CPConnections cnxn = null;
/*  65: 70 */       Connection con = null;
/*  66: 71 */       Statement stat = null;
/*  67: 72 */       con = DConnect.getConnection();
/*  68: 73 */       con.setAutoCommit(false);
/*  69: 74 */       con.createStatement();
/*  70: 75 */       stat = con.createStatement();
/*  71: 76 */       stat.clearBatch();
/*  72: 78 */       for (int i = 0; i < this.subscribers.size(); i++)
/*  73:    */       {
/*  74: 79 */         if (cnxn == null) {
/*  75: 80 */           cnxn = CPConnections.getConnection(this.service.getAccountId(), this.subscribers.get(0).toString(), "rms");
/*  76:    */         }
/*  77: 83 */         UserServiceTransaction tranxn = new UserServiceTransaction();
/*  78:    */         
/*  79: 85 */         String msisdn = this.subscribers.get(i).toString();
/*  80: 86 */         String transactionId = uidGen.generateSecureUID();
/*  81:    */         
/*  82: 88 */         String smsUrl = "";
/*  83: 90 */         if (msisdn.matches("(233|\\+233|00233|0)20\\d{7}")) {
/*  84: 91 */           smsUrl = cnxn.getGatewayURL() + "/sendsms?to=" + URLUTF8Encoder.encode(msisdn) + "&account=" + URLUTF8Encoder.encode(cnxn.getUsername()) + "&smsc=OTBulk&username=OTBulk&password=" + URLUTF8Encoder.encode("03F7*NV!") + "&from=" + URLUTF8Encoder.encode(this.service.getServiceName()) + "&text=" + URLUTF8Encoder.encode(this.service.getMessage()) + "&binfo=" + transactionId;
/*  85:    */         } else {
/*  86: 96 */           smsUrl = cnxn.getGatewayURL() + "/sendsms?to=" + URLUTF8Encoder.encode(msisdn) + "&text=" + URLUTF8Encoder.encode(this.service.getMessage()) + "&conn=" + URLUTF8Encoder.encode(cnxn.getConnection()) + "&username=" + URLUTF8Encoder.encode(cnxn.getUsername()) + "&password=" + URLUTF8Encoder.encode(cnxn.getPassword()) + "&from=" + URLUTF8Encoder.encode(this.service.getServiceName());
/*  87:    */         }
/*  88:101 */         smsUrl = smsUrl.replaceAll("'", "\\\\'");
/*  89:    */         
/*  90:103 */         tranxn.setAccountId(this.service.getAccountId());
/*  91:104 */         tranxn.setKeyword(this.service.getKeyword());
/*  92:105 */         tranxn.setMsg("");
/*  93:106 */         tranxn.setMsisdn(msisdn);
/*  94:107 */         tranxn.setTransactionId(transactionId);
/*  95:108 */         tranxn.setCallBackUrl(smsUrl);
/*  96:109 */         tranxn.setPricePoint(this.service.getPricing());
/*  97:110 */         tranxn.setIsBilled(0);
/*  98:111 */         tranxn.setIsCompleted(0);
/*  99:    */         
/* 100:    */ 
/* 101:114 */         String query = "INSERT into transactions (trans_id,keyword,account_id,msisdn,callback_url,date,msg,is_billed,is_completed,price_point_id) values('" + tranxn.getTransactionId() + "','" + tranxn.getKeyword() + "','" + tranxn.getAccountId() + "','" + tranxn.getMsisdn() + "','" + tranxn.getCallBackUrl() + "',now(),'" + tranxn.getMsg() + "','" + tranxn.getIsBilled() + "','" + tranxn.getIsCompleted() + "','" + tranxn.getPricePoint() + "')";
/* 102:    */         
/* 103:    */ 
/* 104:    */ 
/* 105:    */ 
/* 106:119 */         System.out.println(query);
/* 107:    */         
/* 108:121 */         transactionList.add(tranxn);
/* 109:122 */         stat.addBatch(query);
/* 110:    */       }
/* 111:125 */       if ((transactionList != null) && (transactionList.size() > 0))
/* 112:    */       {
/* 113:128 */         boolean transOpened = false;
/* 114:    */         
/* 115:130 */         transOpened = openTransactions(con, stat);
/* 116:133 */         if (transOpened)
/* 117:    */         {
/* 118:134 */           int totalBilled = 0;
/* 119:135 */           if (this.subscribers.get(0).toString().matches("(233|\\+233|00233|0)20\\d{7}")) {
/* 120:137 */             totalBilled = executeTransactionOnVodafone(transactionList);
/* 121:    */           } else {
/* 122:139 */             totalBilled = executeTransaction(transactionList);
/* 123:    */           }
/* 124:    */         }
/* 125:    */         else
/* 126:    */         {
/* 127:142 */           System.out.println(new Date() + "could not open transactions with size:" + transactionList.size());
/* 128:    */         }
/* 129:    */       }
/* 130:    */     }
/* 131:    */     catch (Exception e)
/* 132:    */     {
/* 133:147 */       System.out.println("@ TempBatchTransmission: Error: " + e.getMessage());
/* 134:    */     }
/* 135:    */   }
/* 136:    */   
/* 137:    */   private boolean openTransactions(Connection con, Statement stat)
/* 138:    */     throws SQLException
/* 139:    */   {
/* 140:153 */     String queryString = "";
/* 141:154 */     PreparedStatement prepstat = null;
/* 142:155 */     ResultSet rs = null;
/* 143:    */     
/* 144:157 */     int[] aiupdateCounts = null;
/* 145:158 */     boolean bError = false;
/* 146:159 */     boolean status = false;
/* 147:    */     try
/* 148:    */     {
/* 149:163 */       aiupdateCounts = stat.executeBatch();
/* 150:    */     }
/* 151:    */     catch (BatchUpdateException bue)
/* 152:    */     {
/* 153:165 */       bError = true;
/* 154:166 */       aiupdateCounts = bue.getUpdateCounts();
/* 155:    */       
/* 156:168 */       SQLException SQLe = bue;
/* 157:169 */       while (SQLe != null) {
/* 158:171 */         SQLe = SQLe.getNextException();
/* 159:    */       }
/* 160:173 */       System.out.println(new Date() + ":BATCH SQLerror:" + bue.getMessage());
/* 161:    */     }
/* 162:    */     catch (SQLException SQLe)
/* 163:    */     {
/* 164:175 */       System.out.println(new Date() + ":there was an error during the update:");
/* 165:176 */       System.out.println(new Date() + ":BATCH SQLerror:" + SQLe.getMessage());
/* 166:    */     }
/* 167:    */     finally
/* 168:    */     {
/* 169:180 */       for (int i = 0; i < aiupdateCounts.length; i++)
/* 170:    */       {
/* 171:181 */         int iProcessed = aiupdateCounts[i];
/* 172:182 */         if ((iProcessed > 0) || (iProcessed == -2))
/* 173:    */         {
/* 174:184 */           System.out.println(new Date() + ":" + i + ":BATCH SQL Update sucessful");
/* 175:    */         }
/* 176:    */         else
/* 177:    */         {
/* 178:187 */           bError = true;
/* 179:188 */           break;
/* 180:    */         }
/* 181:    */       }
/* 182:192 */       if (bError)
/* 183:    */       {
/* 184:193 */         System.out.println(new Date() + ":CODE_GEN: BATCH SQL Update fail. RollBack...");
/* 185:194 */         con.rollback();
/* 186:195 */         status = false;
/* 187:    */       }
/* 188:    */       else
/* 189:    */       {
/* 190:197 */         con.commit();
/* 191:198 */         status = true;
/* 192:    */       }
/* 193:200 */       if (con != null) {
/* 194:201 */         con.close();
/* 195:    */       }
/* 196:    */     }
/* 197:205 */     return status;
/* 198:    */   }
/* 199:    */   
/* 200:    */   private int executeTransactionOnVodafone(ArrayList<UserServiceTransaction> transList)
/* 201:    */   {
/* 202:210 */     boolean billed = false;
/* 203:211 */     String msisdn = "";
/* 204:212 */     String kw = "";
/* 205:213 */     UserServiceTransaction trans = null;
/* 206:    */     
/* 207:215 */     int billCount = 0;
/* 208:    */     
/* 209:217 */     int IN_period = 10;
/* 210:    */     
/* 211:219 */     int IN_check_counter = 0;
/* 212:220 */     Connection conn = null;
/* 213:221 */     PreparedStatement prepstat = null;
/* 214:222 */     ResultSet rs = null;
/* 215:    */     
/* 216:224 */     String in_directive = "";String in_wait_period = "";String in_check_period = "";String in_status = "";String transmission_halt_time = "";
/* 217:225 */     for (int i = 0; i < transList.size(); i++)
/* 218:    */     {
/* 219:226 */       in_directive = "";
/* 220:227 */       in_wait_period = "";
/* 221:228 */       in_check_period = "";
/* 222:229 */       in_status = "";
/* 223:230 */       transmission_halt_time = "";
/* 224:    */       
/* 225:232 */       Calendar cal = Calendar.getInstance();
/* 226:233 */       int minute = cal.get(12);
/* 227:235 */       if (minute % IN_period != 0) {
/* 228:236 */         IN_check_counter = 0;
/* 229:    */       }
/* 230:239 */       if ((i == 0) || (minute % IN_period == 0))
/* 231:    */       {
/* 232:240 */         IN_check_counter++;
/* 233:241 */         if (IN_check_counter == 1)
/* 234:    */         {
/* 235:    */           try
/* 236:    */           {
/* 237:243 */             System.out.println("=======================checking directive from IN=====================");
/* 238:244 */             conn = DConnect.getConnection();
/* 239:245 */             String query = "select * from ot_transmission_settings where settings_key='Status'";
/* 240:246 */             prepstat = conn.prepareStatement(query);
/* 241:247 */             rs = prepstat.executeQuery();
/* 242:248 */             while (rs.next()) {
/* 243:249 */               in_directive = rs.getString("settings_value");
/* 244:    */             }
/* 245:251 */             query = "select * from ot_transmission_settings where settings_key='wait_period'";
/* 246:252 */             prepstat = conn.prepareStatement(query);
/* 247:253 */             rs = prepstat.executeQuery();
/* 248:254 */             while (rs.next()) {
/* 249:255 */               in_wait_period = rs.getString("settings_value");
/* 250:    */             }
/* 251:257 */             query = "select * from ot_transmission_settings where settings_key='trans_halt_time'";
/* 252:258 */             prepstat = conn.prepareStatement(query);
/* 253:259 */             rs = prepstat.executeQuery();
/* 254:260 */             while (rs.next()) {
/* 255:261 */               transmission_halt_time = rs.getString("settings_value");
/* 256:    */             }
/* 257:263 */             query = "select * from ot_transmission_settings where settings_key='in_check_period'";
/* 258:264 */             prepstat = conn.prepareStatement(query);
/* 259:265 */             rs = prepstat.executeQuery();
/* 260:266 */             while (rs.next()) {
/* 261:267 */               in_check_period = rs.getString("settings_value");
/* 262:    */             }
/* 263:269 */             conn.close();
/* 264:    */           }
/* 265:    */           catch (Exception ex1)
/* 266:    */           {
/* 267:271 */             ex1.printStackTrace();
/* 268:    */           }
/* 269:275 */           if ((in_check_period != null) && (!in_check_period.equals(""))) {
/* 270:    */             try
/* 271:    */             {
/* 272:277 */               IN_period = new Integer(in_check_period).intValue();
/* 273:    */             }
/* 274:    */             catch (Exception ex)
/* 275:    */             {
/* 276:279 */               IN_period = 10;
/* 277:    */             }
/* 278:    */           } else {
/* 279:282 */             IN_period = 10;
/* 280:    */           }
/* 281:285 */           if ((in_directive != null) && (in_directive.equals("pause")))
/* 282:    */           {
/* 283:286 */             int in_wait = 0;
/* 284:288 */             if ((in_wait_period == null) || (in_wait_period.equals(""))) {
/* 285:289 */               in_wait_period = "5";
/* 286:    */             } else {
/* 287:    */               try
/* 288:    */               {
/* 289:292 */                 in_wait = new Integer(in_wait_period).intValue();
/* 290:    */               }
/* 291:    */               catch (Exception ndf)
/* 292:    */               {
/* 293:294 */                 in_wait_period = "5";
/* 294:    */               }
/* 295:    */             }
/* 296:297 */             in_wait *= 60000;
/* 297:    */             try
/* 298:    */             {
/* 299:299 */               System.out.println("waiting....");
/* 300:300 */               Thread.sleep(in_wait);
/* 301:301 */               System.out.println("resuming....");
/* 302:302 */               if (in_directive == null) {
/* 303:303 */                 in_directive = "";
/* 304:    */               }
/* 305:306 */               while (in_directive.equals("pause"))
/* 306:    */               {
/* 307:307 */                 conn = DConnect.getConnection();
/* 308:308 */                 String query = "select * from ot_transmission_settings where settings_key='Status'";
/* 309:309 */                 prepstat = conn.prepareStatement(query);
/* 310:310 */                 rs = prepstat.executeQuery();
/* 311:311 */                 while (rs.next()) {
/* 312:312 */                   in_directive = rs.getString("settings_value");
/* 313:    */                 }
/* 314:314 */                 if ((in_directive != null) && (in_directive.equals("pause"))) {
/* 315:315 */                   Thread.sleep(in_wait);
/* 316:    */                 }
/* 317:    */               }
/* 318:    */             }
/* 319:    */             catch (Exception ex1)
/* 320:    */             {
/* 321:319 */               ex1.printStackTrace();
/* 322:    */             }
/* 323:    */           }
/* 324:    */         }
/* 325:    */       }
/* 326:324 */       trans = (UserServiceTransaction)transList.get(i);
/* 327:325 */       kw = trans.getKeyword();
/* 328:326 */       msisdn = trans.getMsisdn();
/* 329:327 */       System.out.println(new Date() + ":" + (i + 1) + ":next_transaction:" + trans.getTransactionId() + ":" + kw + ": " + msisdn + "...");
/* 330:    */       try
/* 331:    */       {
/* 332:329 */         billed = bill(trans);
/* 333:331 */         if (billed)
/* 334:    */         {
/* 335:332 */           pushAlert(trans);
/* 336:333 */           billCount++;
/* 337:    */         }
/* 338:    */       }
/* 339:    */       catch (Exception e)
/* 340:    */       {
/* 341:337 */         System.out.println(new Date() + ":Info alert: error executing transaction for: " + trans.getTransactionId() + ":" + kw + ":" + msisdn + ": " + e.getMessage());
/* 342:    */       }
/* 343:    */     }
/* 344:340 */     trans = null;
/* 345:341 */     return billCount;
/* 346:    */   }
/* 347:    */   
/* 348:    */   private int executeTransaction(ArrayList<UserServiceTransaction> transList)
/* 349:    */   {
/* 350:346 */     boolean billed = false;
/* 351:347 */     String msisdn = "";
/* 352:348 */     String kw = "";
/* 353:349 */     UserServiceTransaction trans = null;
/* 354:    */     
/* 355:351 */     int billCount = 0;
/* 356:353 */     for (int i = 0; i < transList.size(); i++)
/* 357:    */     {
/* 358:355 */       trans = (UserServiceTransaction)transList.get(i);
/* 359:356 */       kw = trans.getKeyword();
/* 360:357 */       msisdn = trans.getMsisdn();
/* 361:358 */       System.out.println(new Date() + ":" + (i + 1) + ":next_transaction:" + trans.getTransactionId() + ":" + kw + ": " + msisdn + "...");
/* 362:    */       try
/* 363:    */       {
/* 364:360 */         billed = bill(trans);
/* 365:362 */         if (billed)
/* 366:    */         {
/* 367:363 */           pushAlert(trans);
/* 368:364 */           billCount++;
/* 369:    */         }
/* 370:    */       }
/* 371:    */       catch (Exception e)
/* 372:    */       {
/* 373:368 */         System.out.println(new Date() + ":Info alert: error executing transaction for: " + trans.getTransactionId() + ":" + kw + ":" + msisdn + ": " + e.getMessage());
/* 374:    */       }
/* 375:    */     }
/* 376:371 */     trans = null;
/* 377:372 */     return billCount;
/* 378:    */   }
/* 379:    */   
/* 380:    */   private void pushAlert(UserServiceTransaction trans)
/* 381:    */   {
/* 382:377 */     String resp = "";
/* 383:378 */     int count = 1;
/* 384:379 */     String kw = trans.getKeyword();
/* 385:380 */     String msisdn = trans.getMsisdn();
/* 386:381 */     String callbackUrl = trans.getCallBackUrl();
/* 387:    */     
/* 388:    */ 
/* 389:384 */     HttpClient client = new HttpClient();
/* 390:385 */     GetMethod httpGETFORM = new GetMethod(callbackUrl);
/* 391:    */     
/* 392:387 */     System.out.println(new Date() + ": " + kw + ": " + msisdn + ": HttpClient created. Now calling sendsmsURL...");
/* 393:388 */     System.out.println(new Date() + ":AlertURL:" + callbackUrl);
/* 394:    */     try
/* 395:    */     {
/* 396:390 */       while ((!resp.contains("Accepted")) && (!resp.contains("Queued")) && (count <= 3))
/* 397:    */       {
/* 398:392 */         client.executeMethod(httpGETFORM);
/* 399:393 */         resp = getResponse(httpGETFORM.getResponseBodyAsStream());
/* 400:394 */         System.out.println(new Date() + " :: " + kw + " :: " + msisdn + " :: Messaging Gateway response=" + resp + " :: tranxn ID=" + trans.getTransactionId() + " :: Attempt=" + count + " :: callback URL=" + trans.getCallBackUrl());
/* 401:    */         
/* 402:396 */         count++;
/* 403:    */       }
/* 404:398 */       System.out.println(new Date() + ":===============================================================");
/* 405:    */     }
/* 406:    */     catch (Exception e)
/* 407:    */     {
/* 408:400 */       System.out.println(new Date() + ":" + kw + ": " + msisdn + ":exception forwarding alert: " + e.getMessage());
/* 409:    */     }
/* 410:    */     finally
/* 411:    */     {
/* 412:402 */       httpGETFORM.releaseConnection();
/* 413:403 */       client = null;
/* 414:404 */       httpGETFORM = null;
/* 415:    */     }
/* 416:    */   }
/* 417:    */   
/* 418:    */   private String getResponse(InputStream in)
/* 419:    */     throws Exception
/* 420:    */   {
/* 421:410 */     String status = "error";
/* 422:411 */     String reply = "";
/* 423:412 */     String error = "";
/* 424:413 */     String responseString = "";
/* 425:414 */     BufferedReader br = null;
/* 426:    */     try
/* 427:    */     {
/* 428:417 */       InputStream responseBody = in;
/* 429:418 */       br = new BufferedReader(new InputStreamReader(responseBody));
/* 430:    */       
/* 431:420 */       String line = br.readLine();
/* 432:421 */       while (line != null)
/* 433:    */       {
/* 434:422 */         responseString = responseString + line;
/* 435:423 */         line = br.readLine();
/* 436:    */       }
/* 437:    */     }
/* 438:    */     catch (Exception e) {}finally
/* 439:    */     {
/* 440:427 */       br.close();
/* 441:428 */       in.close();
/* 442:    */       
/* 443:430 */       br = null;
/* 444:431 */       in = null;
/* 445:    */     }
/* 446:433 */     return responseString;
/* 447:    */   }
/* 448:    */   
/* 449:    */   private boolean bill(UserServiceTransaction trans)
/* 450:    */     throws Exception
/* 451:    */   {
/* 452:437 */     boolean isBilled = false;
/* 453:438 */     String ACCOUNT_NOT_FOUND = "201";
/* 454:439 */     String ACCOUNT_NOT_IN_DB = "5";
/* 455:440 */     String STATUS_BILLED = "0";
/* 456:441 */     String resp = "";
/* 457:442 */     String queryString = "";
/* 458:    */     
/* 459:444 */     PricePoint pp = PaymentManager.viewPricePoint(this.service.getPricing());
/* 460:445 */     String billingURL = pp.getBillingUrl();
/* 461:446 */     String kw = trans.getKeyword();
/* 462:447 */     String msisdn = trans.getMsisdn();
/* 463:449 */     if ((billingURL == null) || (billingURL.equals("")))
/* 464:    */     {
/* 465:450 */       System.out.println(new Date() + kw + ": " + msisdn + ":billingURL:" + billingURL);
/* 466:451 */       return true;
/* 467:    */     }
/* 468:454 */     if (msisdn.matches("(233|\\+233|00233|0)20\\d{7}")) {
/* 469:455 */       queryString = "msisdn=" + msisdn.substring(4);
/* 470:    */     } else {
/* 471:457 */       queryString = "msisdn=" + msisdn;
/* 472:    */     }
/* 473:459 */     queryString = queryString + "&transactionId=" + trans.getTransactionId() + "&keyword=" + trans.getKeyword();
/* 474:    */     
/* 475:    */ 
/* 476:462 */     billingURL = URLUTF8Encoder.doURLEscaping(queryString, billingURL);
/* 477:    */     
/* 478:464 */     HttpClient client = new HttpClient();
/* 479:465 */     GetMethod httpGETFORM = new GetMethod(billingURL);
/* 480:466 */     httpGETFORM.getParams().setParameter("http.socket.timeout", new Integer(9000));
/* 481:    */     try
/* 482:    */     {
/* 483:471 */       client.executeMethod(httpGETFORM);
/* 484:472 */       resp = getResponse(httpGETFORM.getResponseBodyAsStream());
/* 485:473 */       System.out.println(new Date() + " :: " + kw + " :: " + msisdn + " :: Response from billing gateway=" + resp + " :: tranxn ID=" + trans.getTransactionId() + " :: billingURL=" + billingURL + " :: callback URL=" + trans.getCallBackUrl());
/* 486:    */     }
/* 487:    */     catch (Exception e)
/* 488:    */     {
/* 489:477 */       System.out.println(new Date() + ":" + kw + ": " + msisdn + ":exception billing subscriber: " + e.getMessage());
/* 490:478 */       if (!e.getMessage().equals("Read timed out")) {}
/* 491:    */     }
/* 492:    */     finally
/* 493:    */     {
/* 494:481 */       httpGETFORM.releaseConnection();
/* 495:482 */       client = null;
/* 496:483 */       httpGETFORM = null;
/* 497:    */     }
/* 498:488 */     if ((resp != null) && (resp.equals("0"))) {
/* 499:489 */       isBilled = true;
/* 500:    */     } else {
/* 501:491 */       isBilled = false;
/* 502:    */     }
/* 503:493 */     return isBilled;
/* 504:    */   }
/* 505:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.common.TempBatchTransmission
 * JD-Core Version:    0.7.0.1
 */