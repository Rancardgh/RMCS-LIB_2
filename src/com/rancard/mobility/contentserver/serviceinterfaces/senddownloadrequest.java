/*   1:    */ package com.rancard.mobility.contentserver.serviceinterfaces;
/*   2:    */ 
/*   3:    */ import com.rancard.common.Message;
/*   4:    */ import com.rancard.common.uidGen;
/*   5:    */ import com.rancard.mobility.common.Driver;
/*   6:    */ import com.rancard.mobility.common.FonCapabilityMtrx;
/*   7:    */ import com.rancard.mobility.common.PushDriver;
/*   8:    */ import com.rancard.mobility.contentserver.CPConnections;
/*   9:    */ import com.rancard.mobility.contentserver.ContentItem;
/*  10:    */ import com.rancard.mobility.contentserver.ContentType;
/*  11:    */ import com.rancard.mobility.contentserver.Format;
/*  12:    */ import com.rancard.mobility.contentserver.RepositoryManager;
/*  13:    */ import com.rancard.mobility.contentserver.Transaction;
/*  14:    */ import com.rancard.mobility.contentserver.uploadsBean;
/*  15:    */ import com.rancard.util.payment.ePin;
/*  16:    */ import java.io.IOException;
/*  17:    */ import java.io.PrintWriter;
/*  18:    */ import javax.servlet.ServletContext;
/*  19:    */ import javax.servlet.ServletException;
/*  20:    */ import javax.servlet.http.HttpServlet;
/*  21:    */ import javax.servlet.http.HttpServletRequest;
/*  22:    */ import javax.servlet.http.HttpServletResponse;
/*  23:    */ import net.sourceforge.wurfl.wurflapi.CapabilityMatrix;
/*  24:    */ import net.sourceforge.wurfl.wurflapi.WurflDevice;
/*  25:    */ 
/*  26:    */ public class senddownloadrequest
/*  27:    */   extends HttpServlet
/*  28:    */ {
/*  29:    */   private static final String CONTENT_TYPE = "text/html";
/*  30:    */   private static final String FROM = "RMCS";
/*  31: 24 */   String to = null;
/*  32: 26 */   String text = null;
/*  33: 28 */   String responsePath = null;
/*  34: 31 */   String queryString = null;
/*  35: 32 */   String url = null;
/*  36: 33 */   String listid = null;
/*  37: 34 */   String provName = "";
/*  38: 35 */   String pin = null;
/*  39: 37 */   String ticket = new String();
/*  40: 38 */   CPConnections cnxn = null;
/*  41:    */   
/*  42:    */   public void init()
/*  43:    */     throws ServletException
/*  44:    */   {}
/*  45:    */   
/*  46:    */   public void doGet(HttpServletRequest request, HttpServletResponse response)
/*  47:    */     throws ServletException, IOException
/*  48:    */   {
/*  49: 49 */     this.text = "Content Download:";
/*  50:    */     
/*  51:    */ 
/*  52: 52 */     String id = request.getParameter("id");
/*  53: 53 */     String listId = request.getParameter("listId");
/*  54: 54 */     String subscriber = request.getParameter("to");
/*  55: 55 */     String phone_id = request.getParameter("phoneId");
/*  56: 56 */     String pinNo = request.getParameter("pin");
/*  57: 57 */     String responsePath = request.getParameter("responsePath");
/*  58:    */     
/*  59: 59 */     String wurflpath = getServletContext().getInitParameter("wurfllocation");
/*  60:    */     
/*  61:    */ 
/*  62: 62 */     Message replyPg = new Message();
/*  63: 63 */     PrintWriter out = response.getWriter();
/*  64:    */     
/*  65: 65 */     String s = request.getProtocol().toLowerCase();
/*  66: 66 */     s = s.substring(0, s.indexOf("/")).toLowerCase();
/*  67:    */     
/*  68:    */ 
/*  69:    */ 
/*  70: 70 */     String baseUrl = getServletContext().getInitParameter("contentServerPublicURL");
/*  71: 72 */     if (id == null) {
/*  72: 73 */       id = "";
/*  73:    */     }
/*  74: 75 */     if (listId == null) {
/*  75: 76 */       listId = "";
/*  76:    */     }
/*  77: 78 */     if (subscriber == null) {
/*  78: 79 */       subscriber = "";
/*  79:    */     }
/*  80: 81 */     if (phone_id == null) {
/*  81: 82 */       phone_id = "";
/*  82:    */     }
/*  83: 84 */     if (pinNo == null) {
/*  84: 85 */       pinNo = "";
/*  85:    */     }
/*  86: 88 */     ContentItem item = null;
/*  87: 89 */     ContentType type = null;
/*  88: 90 */     Format format = null;
/*  89:    */     try
/*  90:    */     {
/*  91: 94 */       item = new ContentItem().viewContentItem(id, listId);
/*  92:    */       
/*  93:    */ 
/*  94:    */ 
/*  95:    */ 
/*  96:    */ 
/*  97:    */ 
/*  98:101 */       type = item.getContentTypeDetails();
/*  99:102 */       format = item.getFormat();
/* 100:104 */       if ((item.getid() == null) || ((item.getid().equals("")) && (item.getListId() != null)) || (item.getListId().equals(""))) {
/* 101:106 */         throw new Exception();
/* 102:    */       }
/* 103:108 */       this.text = (this.text + " " + item.gettitle());
/* 104:    */     }
/* 105:    */     catch (Exception e)
/* 106:    */     {
/* 107:110 */       replyPg.setMessage("1000");
/* 108:111 */       replyPg.setStatus(false);
/* 109:112 */       if (responsePath != null) {
/* 110:113 */         response.sendRedirect(responsePath + "?reply=" + replyPg.getMessage());
/* 111:    */       } else {
/* 112:116 */         out.println(replyPg.getMessage());
/* 113:    */       }
/* 114:118 */       return;
/* 115:    */     }
/* 116:    */     try
/* 117:    */     {
/* 118:122 */       if (!validateNumber(subscriber))
/* 119:    */       {
/* 120:125 */         replyPg.setMessage("2000");
/* 121:126 */         replyPg.setStatus(false);
/* 122:127 */         if (responsePath != null) {
/* 123:128 */           response.sendRedirect(responsePath + "?reply=" + replyPg.getMessage());
/* 124:    */         } else {
/* 125:131 */           out.println(replyPg.getMessage());
/* 126:    */         }
/* 127:133 */         return;
/* 128:    */       }
/* 129:    */       try
/* 130:    */       {
/* 131:137 */         this.cnxn = CPConnections.getConnection(item.getListId(), this.to);
/* 132:    */       }
/* 133:    */       catch (Exception e)
/* 134:    */       {
/* 135:140 */         replyPg.setMessage(e.getMessage());
/* 136:141 */         replyPg.setStatus(false);
/* 137:142 */         if (responsePath != null) {
/* 138:143 */           response.sendRedirect(responsePath + "?reply=" + replyPg.getMessage());
/* 139:    */         } else {
/* 140:146 */           out.println(replyPg.getMessage());
/* 141:    */         }
/* 142:148 */         return;
/* 143:    */       }
/* 144:152 */       if (!phone_id.equals(""))
/* 145:    */       {
/* 146:153 */         String capabilityValue = new String();
/* 147:    */         try
/* 148:    */         {
/* 149:155 */           FonCapabilityMtrx fcm = null;
/* 150:157 */           if (format.getPushBearer().equals("SMS"))
/* 151:    */           {
/* 152:158 */             capabilityValue = "true";
/* 153:    */           }
/* 154:    */           else
/* 155:    */           {
/* 156:    */             try
/* 157:    */             {
/* 158:163 */               fcm = new FonCapabilityMtrx();
/* 159:    */             }
/* 160:    */             catch (Exception e)
/* 161:    */             {
/* 162:165 */               throw new Exception("2003");
/* 163:    */             }
/* 164:169 */             WurflDevice phone = fcm.getActualDevice(phone_id);
/* 165:170 */             if (phone == null) {
/* 166:171 */               throw new Exception("1");
/* 167:    */             }
/* 168:175 */             if ((format.getPushBearer().equals("WAP")) && 
/* 169:176 */               (!fcm.getCapabilitiesManager().getCapabilityForDevice(phone_id, "wap_push_support").equals("true"))) {
/* 170:180 */               throw new Exception("2004");
/* 171:    */             }
/* 172:184 */             String capability = fcm.findSupportedCapability(format.getFileExt());
/* 173:    */             
/* 174:    */ 
/* 175:187 */             capabilityValue = fcm.getCapabilitiesManager().getCapabilityForDevice(phone_id, capability);
/* 176:    */           }
/* 177:193 */           if (!capabilityValue.equals("true")) {
/* 178:194 */             throw new Exception("2002");
/* 179:    */           }
/* 180:    */         }
/* 181:    */         catch (Exception e)
/* 182:    */         {
/* 183:199 */           replyPg.setMessage(e.getMessage());
/* 184:200 */           replyPg.setStatus(false);
/* 185:201 */           if (responsePath != null) {
/* 186:202 */             response.sendRedirect(responsePath + "?reply=" + replyPg.getMessage());
/* 187:    */           } else {
/* 188:205 */             out.println(replyPg.getMessage());
/* 189:    */           }
/* 190:207 */           return;
/* 191:    */         }
/* 192:    */       }
/* 193:212 */       Transaction download = new Transaction();
/* 194:    */       
/* 195:214 */       boolean logSuccessful = false;
/* 196:215 */       while (!logSuccessful) {
/* 197:216 */         logSuccessful = logging(10, download, "" + format.getId());
/* 198:    */       }
/* 199:222 */       this.url = (baseUrl + "downloadcontent?ticketId=" + download.getTicketID());
/* 200:    */       
/* 201:    */ 
/* 202:    */ 
/* 203:226 */       this.ticket = download.getTicketID();
/* 204:227 */       this.text = (this.text + " Download ID: " + this.ticket);
/* 205:228 */       this.text = this.text.replaceAll(" ", "%20");
/* 206:229 */       this.text = this.text.replaceAll("'", "%27");
/* 207:232 */       if (routeNotification(item))
/* 208:    */       {
/* 209:233 */         if ((format.getPushBearer().equals("SMS")) || (format.getPushBearer().equals("SMS_BIN"))) {
/* 210:235 */           download.setDownloadCompleted(true);
/* 211:    */         }
/* 212:238 */         replyPg.setStatus(true);
/* 213:    */       }
/* 214:    */       else
/* 215:    */       {
/* 216:240 */         Transaction.removeTransaction(download.getTicketID());
/* 217:241 */         replyPg.setMessage("8000");
/* 218:    */         
/* 219:243 */         replyPg.setStatus(false);
/* 220:244 */         if (responsePath != null) {
/* 221:245 */           response.sendRedirect(responsePath + "?reply=" + replyPg.getMessage());
/* 222:    */         } else {
/* 223:248 */           out.println(replyPg.getMessage());
/* 224:    */         }
/* 225:250 */         return;
/* 226:    */       }
/* 227:253 */       if (replyPg.isStatus() == true)
/* 228:    */       {
/* 229:254 */         ePin voucher = new ePin();
/* 230:    */         
/* 231:    */ 
/* 232:257 */         voucher.setPin(pinNo);
/* 233:258 */         voucher.isValid();
/* 234:259 */         voucher.setCurrentBalance(voucher.getCurrentBalance() - Double.parseDouble(item.getPrice()));
/* 235:    */         
/* 236:261 */         voucher.updateMyLog();
/* 237:    */         
/* 238:263 */         replyPg.setMessage("Ok: " + this.ticket);
/* 239:    */         
/* 240:265 */         replyPg.setStatus(true);
/* 241:266 */         if (responsePath != null) {
/* 242:267 */           response.sendRedirect(responsePath + "?reply=" + replyPg.getMessage());
/* 243:    */         } else {
/* 244:270 */           out.println(replyPg.getMessage());
/* 245:    */         }
/* 246:272 */         return;
/* 247:    */       }
/* 248:    */     }
/* 249:    */     catch (Exception e)
/* 250:    */     {
/* 251:    */       try
/* 252:    */       {
/* 253:276 */         Transaction.removeTransaction(this.ticket);
/* 254:    */       }
/* 255:    */       catch (Exception ex) {}
/* 256:279 */       replyPg.setMessage("6001: " + e.getMessage());
/* 257:    */       
/* 258:    */ 
/* 259:282 */       replyPg.setStatus(false);
/* 260:283 */       if (responsePath != null) {
/* 261:284 */         response.sendRedirect(responsePath + "?reply=" + replyPg.getMessage());
/* 262:    */       } else {
/* 263:287 */         out.println(replyPg.getMessage());
/* 264:    */       }
/* 265:289 */       return;
/* 266:    */     }
/* 267:    */   }
/* 268:    */   
/* 269:    */   public void doPost(HttpServletRequest request, HttpServletResponse response)
/* 270:    */     throws ServletException, IOException
/* 271:    */   {
/* 272:297 */     doGet(request, response);
/* 273:    */   }
/* 274:    */   
/* 275:    */   public void destroy() {}
/* 276:    */   
/* 277:    */   public boolean logging(int idLength, Transaction dlb, String formatID)
/* 278:    */   {
/* 279:307 */     boolean logged = true;
/* 280:308 */     dlb.setTicketID(uidGen.generateID(idLength) + "-" + formatID);
/* 281:    */     
/* 282:    */ 
/* 283:    */ 
/* 284:    */ 
/* 285:    */ 
/* 286:    */ 
/* 287:315 */     return logged;
/* 288:    */   }
/* 289:    */   
/* 290:    */   public boolean validateNumber(String number)
/* 291:    */     throws Exception
/* 292:    */   {
/* 293:320 */     boolean flag = false;
/* 294:322 */     if (number.indexOf("+") != -1)
/* 295:    */     {
/* 296:323 */       StringBuffer sb = new StringBuffer(number);
/* 297:324 */       sb.deleteCharAt(number.indexOf("+"));
/* 298:325 */       number = sb.toString();
/* 299:    */     }
/* 300:327 */     number = number.trim();
/* 301:    */     try
/* 302:    */     {
/* 303:330 */       Long.parseLong(number);
/* 304:    */       
/* 305:    */ 
/* 306:    */ 
/* 307:    */ 
/* 308:    */ 
/* 309:    */ 
/* 310:    */ 
/* 311:    */ 
/* 312:    */ 
/* 313:    */ 
/* 314:    */ 
/* 315:    */ 
/* 316:    */ 
/* 317:    */ 
/* 318:    */ 
/* 319:346 */       flag = true;
/* 320:347 */       this.to = ("+" + number);
/* 321:    */     }
/* 322:    */     catch (NumberFormatException e)
/* 323:    */     {
/* 324:349 */       flag = false;
/* 325:    */     }
/* 326:351 */     return flag;
/* 327:    */   }
/* 328:    */   
/* 329:    */   public boolean sendWapPushNotification(ContentItem item)
/* 330:    */     throws Exception
/* 331:    */   {
/* 332:356 */     String price_arg = item.getPrice() == null ? "0" : item.getPrice();
/* 333:357 */     String response = new String();
/* 334:358 */     boolean flag = false;
/* 335:    */     
/* 336:360 */     String number = this.to.substring(1, this.to.length());
/* 337:    */     
/* 338:    */ 
/* 339:    */ 
/* 340:    */ 
/* 341:    */ 
/* 342:    */ 
/* 343:    */ 
/* 344:368 */     response = Driver.getDriver(this.cnxn.getDriverType(), this.cnxn.getGatewayURL()).sendWAPPushMessage(number, "RMCS", this.text, this.url, this.cnxn.getUsername(), this.cnxn.getPassword(), this.cnxn.getConnection(), "", price_arg);
/* 345:376 */     if ((response.length() > 18) && (response.substring(14, 18).equals("true"))) {
/* 346:377 */       flag = true;
/* 347:    */     }
/* 348:379 */     return flag;
/* 349:    */   }
/* 350:    */   
/* 351:    */   public boolean sendSMSNotification(ContentItem item)
/* 352:    */     throws Exception
/* 353:    */   {
/* 354:385 */     boolean flag = false;
/* 355:386 */     String price_arg = item.getPrice() == null ? "0" : item.getPrice();
/* 356:387 */     String response = new String();
/* 357:388 */     String txt = new String();
/* 358:389 */     String number = this.to.substring(1, this.to.length());
/* 359:390 */     byte[] itemStream = null;
/* 360:392 */     if (!item.islocal())
/* 361:    */     {
/* 362:393 */       itemStream = RepositoryManager.getByteArray(item.getDownloadUrl());
/* 363:    */     }
/* 364:    */     else
/* 365:    */     {
/* 366:395 */       new RepositoryManager();uploadsBean upload = RepositoryManager.fetchFile(item.getListId(), item.getid());
/* 367:    */       
/* 368:397 */       itemStream = upload.getDataStream();
/* 369:    */     }
/* 370:399 */     txt = new String(itemStream);
/* 371:    */     
/* 372:    */ 
/* 373:    */ 
/* 374:    */ 
/* 375:    */ 
/* 376:    */ 
/* 377:    */ 
/* 378:    */ 
/* 379:    */ 
/* 380:    */ 
/* 381:    */ 
/* 382:    */ 
/* 383:    */ 
/* 384:    */ 
/* 385:414 */     response = Driver.getDriver(this.cnxn.getDriverType(), this.cnxn.getGatewayURL()).sendSMSTextMessage(number, "RMCS", txt, this.cnxn.getUsername(), this.cnxn.getPassword(), this.cnxn.getConnection(), "", price_arg);
/* 386:421 */     if ((response.length() > 18) && (response.substring(14, 18).equals("true"))) {
/* 387:422 */       flag = true;
/* 388:    */     }
/* 389:425 */     return flag;
/* 390:    */   }
/* 391:    */   
/* 392:    */   public boolean sendSMSBINNotification(ContentItem item)
/* 393:    */     throws Exception
/* 394:    */   {
/* 395:431 */     boolean flag = false;
/* 396:432 */     String format = item.getFormat().getFileExt();
/* 397:433 */     String price_arg = item.getPrice() == null ? "0" : item.getPrice();
/* 398:434 */     String response = new String();
/* 399:435 */     StringBuffer sb = null;
/* 400:436 */     String txt = new String();
/* 401:437 */     String number = this.to.substring(1, this.to.length());
/* 402:    */     
/* 403:439 */     byte[] itemStream = null;
/* 404:441 */     if (!item.islocal())
/* 405:    */     {
/* 406:442 */       itemStream = RepositoryManager.getByteArray(item.getDownloadUrl());
/* 407:    */     }
/* 408:    */     else
/* 409:    */     {
/* 410:444 */       new RepositoryManager();uploadsBean upload = RepositoryManager.fetchFile(item.getListId(), item.getid());
/* 411:    */       
/* 412:446 */       itemStream = upload.getDataStream();
/* 413:    */     }
/* 414:449 */     txt = RepositoryManager.getUD(format, item);
/* 415:    */     
/* 416:    */ 
/* 417:    */ 
/* 418:    */ 
/* 419:    */ 
/* 420:    */ 
/* 421:    */ 
/* 422:    */ 
/* 423:    */ 
/* 424:    */ 
/* 425:    */ 
/* 426:    */ 
/* 427:    */ 
/* 428:    */ 
/* 429:    */ 
/* 430:    */ 
/* 431:    */ 
/* 432:    */ 
/* 433:    */ 
/* 434:    */ 
/* 435:    */ 
/* 436:471 */     response = Driver.getDriver(this.cnxn.getDriverType(), this.cnxn.getGatewayURL()).sendSMSBinaryMessage(number, "RMCS", txt, "udh", "cs", format, this.cnxn.getUsername(), this.cnxn.getPassword(), this.cnxn.getConnection(), "", price_arg);
/* 437:479 */     if ((response.length() > 18) && (response.substring(14, 18).equals("true"))) {
/* 438:480 */       flag = true;
/* 439:    */     }
/* 440:483 */     return flag;
/* 441:    */   }
/* 442:    */   
/* 443:    */   public boolean routeNotification(ContentItem item)
/* 444:    */     throws Exception
/* 445:    */   {
/* 446:488 */     boolean flag = true;
/* 447:489 */     Format format = item.getFormat();
/* 448:490 */     if (format.getPushBearer().equals("WAP")) {
/* 449:491 */       flag = sendWapPushNotification(item);
/* 450:492 */     } else if (format.getPushBearer().equals("SMS")) {
/* 451:493 */       flag = sendSMSNotification(item);
/* 452:494 */     } else if (format.getPushBearer().equals("SMS_BIN")) {
/* 453:495 */       flag = sendSMSBINNotification(item);
/* 454:    */     }
/* 455:497 */     return flag;
/* 456:    */   }
/* 457:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.contentserver.serviceinterfaces.senddownloadrequest
 * JD-Core Version:    0.7.0.1
 */