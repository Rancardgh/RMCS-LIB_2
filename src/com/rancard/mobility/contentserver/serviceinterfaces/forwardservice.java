/*   1:    */ package com.rancard.mobility.contentserver.serviceinterfaces;
/*   2:    */ 
/*   3:    */ import com.rancard.common.Feedback;
/*   4:    */ import com.rancard.mobility.contentprovider.User;
/*   5:    */ import com.rancard.mobility.contentserver.CPConnections;
/*   6:    */ import com.rancard.mobility.extension.ForwardedService;
/*   7:    */ import com.rancard.mobility.extension.ForwardedServiceManager;
/*   8:    */ import com.rancard.util.URLUTF8Encoder;
/*   9:    */ import java.io.BufferedReader;
/*  10:    */ import java.io.IOException;
/*  11:    */ import java.io.InputStream;
/*  12:    */ import java.io.InputStreamReader;
/*  13:    */ import java.io.PrintStream;
/*  14:    */ import java.io.PrintWriter;
/*  15:    */ import java.util.Date;
/*  16:    */ import javax.servlet.RequestDispatcher;
/*  17:    */ import javax.servlet.ServletContext;
/*  18:    */ import javax.servlet.ServletException;
/*  19:    */ import javax.servlet.ServletRequest;
/*  20:    */ import javax.servlet.ServletResponse;
/*  21:    */ import javax.servlet.http.HttpServlet;
/*  22:    */ import javax.servlet.http.HttpServletRequest;
/*  23:    */ import javax.servlet.http.HttpServletResponse;
/*  24:    */ import org.apache.commons.httpclient.HttpClient;
/*  25:    */ import org.apache.commons.httpclient.HttpException;
/*  26:    */ import org.apache.commons.httpclient.methods.GetMethod;
/*  27:    */ import org.apache.commons.httpclient.params.HttpMethodParams;
/*  28:    */ import org.apache.commons.lang.StringUtils;
/*  29:    */ 
/*  30:    */ public class forwardservice
/*  31:    */   extends HttpServlet
/*  32:    */   implements RequestDispatcher
/*  33:    */ {
/*  34:    */   public void init()
/*  35:    */     throws ServletException
/*  36:    */   {}
/*  37:    */   
/*  38:    */   public void doGet(HttpServletRequest request, HttpServletResponse response)
/*  39:    */     throws ServletException, IOException
/*  40:    */   {
/*  41: 45 */     System.out.println(new Date() + ":@com.rancard.mobility.contentserver.serviceinterfaces.forwardservice...");
/*  42:    */     
/*  43:    */ 
/*  44: 48 */     String message = "";
/*  45:    */     
/*  46: 50 */     PrintWriter out = response.getWriter();
/*  47: 51 */     CPConnections cnxn = new CPConnections();
/*  48: 52 */     String accountId = (String)request.getAttribute("acctId");
/*  49: 53 */     if ((accountId == null) || (accountId.equals(""))) {
/*  50: 54 */       accountId = request.getParameter("acctId");
/*  51:    */     }
/*  52: 56 */     String kw = request.getParameter("keyword");
/*  53: 57 */     String msgBody = request.getParameter("msg");
/*  54: 58 */     String dest = request.getParameter("dest");
/*  55: 59 */     String msisdn = request.getParameter("msisdn");
/*  56: 60 */     String date = request.getParameter("date");
/*  57: 61 */     String phoneId = request.getParameter("phoneId");
/*  58: 62 */     String ua = request.getParameter("ua");
/*  59: 63 */     String siteId = request.getParameter("siteId");
/*  60: 64 */     String regId = request.getParameter("regId");
/*  61: 65 */     String ack = (String)request.getAttribute("ack");
/*  62: 66 */     String time = request.getParameter("time");
/*  63: 67 */     String smsc = request.getParameter("smsc");
/*  64: 68 */     String flag = request.getParameter("routeBy");
/*  65: 69 */     String debitInbox = request.getParameter("debitInbox");
/*  66:    */     
/*  67:    */ 
/*  68: 72 */     System.out.println(new Date() + ":Request params: ack=" + ack);
/*  69: 76 */     if (flag == null) {
/*  70: 76 */       flag = "";
/*  71:    */     }
/*  72: 78 */     if (flag.equals("1")) {
/*  73: 79 */       kw = dest.substring(dest.indexOf("+") + 1);
/*  74: 80 */     } else if (flag.equals("2")) {
/*  75: 81 */       kw = accountId;
/*  76:    */     } else {
/*  77: 83 */       kw = kw;
/*  78:    */     }
/*  79: 86 */     String siteType = (String)request.getAttribute("site_type");
/*  80: 87 */     String lang = (String)request.getAttribute("default_lang");
/*  81: 88 */     if ((lang == null) || (lang.equals(""))) {
/*  82: 89 */       lang = "en";
/*  83:    */     }
/*  84: 91 */     Feedback feedback = (Feedback)getServletContext().getAttribute("feedback_" + lang);
/*  85: 92 */     if (feedback == null) {
/*  86:    */       try
/*  87:    */       {
/*  88: 94 */         feedback = new Feedback();
/*  89:    */       }
/*  90:    */       catch (Exception e) {}
/*  91:    */     }
/*  92: 99 */     if (ack == null) {
/*  93: 99 */       ack = "";
/*  94:    */     }
/*  95:109 */     if ((accountId == null) || (accountId.equals("")))
/*  96:    */     {
/*  97:    */       try
/*  98:    */       {
/*  99:111 */         if (siteType.equals("2")) {
/* 100:112 */           message = feedback.getUserFriendlyDescription("10004");
/* 101:    */         } else {
/* 102:114 */           message = feedback.formDefaultMessage("10004");
/* 103:    */         }
/* 104:    */       }
/* 105:    */       catch (Exception ex)
/* 106:    */       {
/* 107:117 */         message = ex.getMessage();
/* 108:    */       }
/* 109:120 */       boolean isAsciiPrintable = StringUtils.isAsciiPrintable(message);
/* 110:121 */       if (!isAsciiPrintable)
/* 111:    */       {
/* 112:122 */         System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
/* 113:123 */         request.setAttribute("X-Kannel-Coding", "2");
/* 114:124 */         if (request.getAttribute("X-Kannel-Coding") != null) {
/* 115:125 */           System.out.println("Request contains X-Kannel-Coding attribute");
/* 116:    */         }
/* 117:    */       }
/* 118:127 */       out.println(message);
/* 119:128 */       return;
/* 120:    */     }
/* 121:130 */     if ((kw == null) || (kw.equals("")))
/* 122:    */     {
/* 123:    */       try
/* 124:    */       {
/* 125:132 */         if (siteType.equals("2")) {
/* 126:133 */           message = feedback.getUserFriendlyDescription("10001");
/* 127:    */         } else {
/* 128:135 */           message = feedback.formDefaultMessage("10001");
/* 129:    */         }
/* 130:    */       }
/* 131:    */       catch (Exception ex)
/* 132:    */       {
/* 133:138 */         message = ex.getMessage();
/* 134:    */       }
/* 135:141 */       boolean isAsciiPrintable = StringUtils.isAsciiPrintable(message);
/* 136:142 */       if (!isAsciiPrintable)
/* 137:    */       {
/* 138:143 */         System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
/* 139:144 */         request.setAttribute("X-Kannel-Coding", "2");
/* 140:145 */         if (request.getAttribute("X-Kannel-Coding") != null) {
/* 141:146 */           System.out.println("Request contains X-Kannel-Coding attribute");
/* 142:    */         }
/* 143:    */       }
/* 144:148 */       out.println(message);
/* 145:149 */       return;
/* 146:    */     }
/* 147:153 */     if ("1".equals(debitInbox))
/* 148:    */     {
/* 149:154 */       User user = new User();
/* 150:    */       try
/* 151:    */       {
/* 152:156 */         user = user.viewDealer(accountId);
/* 153:158 */         if (user != null)
/* 154:    */         {
/* 155:159 */           double inboxLimit = user.getInboxBalance();
/* 156:160 */           if (inboxLimit - 1.0D < 0.0D)
/* 157:    */           {
/* 158:161 */             System.out.println(new Date() + ":User does NOT have enough space in inbox");
/* 159:162 */             throw new Exception("4005");
/* 160:    */           }
/* 161:164 */           System.out.println(new Date() + ":User has enough space in inbox");
/* 162:165 */           double result = inboxLimit - 1.0D;
/* 163:166 */           user.updateDealer(user.getId(), user.getBandwidthBalance(), result, user.getOutboxBalance());
/* 164:    */         }
/* 165:    */         else
/* 166:    */         {
/* 167:169 */           throw new Exception("10004");
/* 168:    */         }
/* 169:    */       }
/* 170:    */       catch (Exception e)
/* 171:    */       {
/* 172:    */         try
/* 173:    */         {
/* 174:173 */           if (siteType.equals("2")) {
/* 175:174 */             message = feedback.getUserFriendlyDescription(e.getMessage());
/* 176:    */           } else {
/* 177:176 */             message = feedback.formDefaultMessage(e.getMessage());
/* 178:    */           }
/* 179:178 */           if (message == null) {
/* 180:179 */             message = e.getMessage();
/* 181:    */           }
/* 182:    */         }
/* 183:    */         catch (Exception ex) {}
/* 184:184 */         boolean isAsciiPrintable = StringUtils.isAsciiPrintable(message);
/* 185:185 */         if (!isAsciiPrintable)
/* 186:    */         {
/* 187:186 */           System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
/* 188:187 */           request.setAttribute("X-Kannel-Coding", "2");
/* 189:188 */           if (request.getAttribute("X-Kannel-Coding") != null) {
/* 190:189 */             System.out.println("Request contains X-Kannel-Coding attribute");
/* 191:    */           }
/* 192:    */         }
/* 193:191 */         out.println(message);
/* 194:192 */         return;
/* 195:    */       }
/* 196:    */     }
/* 197:197 */     System.out.println(new Date() + ":Looking for forwarded service for keyword: " + kw + " on account with ID: " + accountId);
/* 198:    */     
/* 199:    */ 
/* 200:    */ 
/* 201:201 */     ForwardedService fs = new ForwardedService();
/* 202:    */     try
/* 203:    */     {
/* 204:204 */       fs = ForwardedServiceManager.viewForwardedService(kw, accountId);
/* 205:205 */       if ((fs == null) || (fs.getAccountId().equals("")) || (fs.getKeyword().equals(""))) {
/* 206:206 */         throw new Exception();
/* 207:    */       }
/* 208:    */     }
/* 209:    */     catch (Exception e)
/* 210:    */     {
/* 211:    */       try
/* 212:    */       {
/* 213:210 */         if (siteType.equals("2")) {
/* 214:211 */           message = feedback.getUserFriendlyDescription("10001");
/* 215:    */         } else {
/* 216:213 */           message = feedback.formDefaultMessage("10001");
/* 217:    */         }
/* 218:    */       }
/* 219:    */       catch (Exception ex)
/* 220:    */       {
/* 221:216 */         message = ex.getMessage();
/* 222:    */       }
/* 223:219 */       boolean isAsciiPrintable = StringUtils.isAsciiPrintable(message);
/* 224:220 */       if (!isAsciiPrintable)
/* 225:    */       {
/* 226:221 */         System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
/* 227:222 */         request.setAttribute("X-Kannel-Coding", "2");
/* 228:223 */         if (request.getAttribute("X-Kannel-Coding") != null) {
/* 229:224 */           System.out.println("Request contains X-Kannel-Coding attribute");
/* 230:    */         }
/* 231:    */       }
/* 232:226 */       out.println(message);
/* 233:227 */       return;
/* 234:    */     }
/* 235:231 */     String query_string = request.getQueryString();
/* 236:232 */     String dest_url = fs.getUrl();
/* 237:233 */     String resultingUrl = "";
/* 238:    */     
/* 239:    */ 
/* 240:236 */     System.out.println(new Date() + ":Forwarding_URL_raw:" + dest_url);
/* 241:    */     try
/* 242:    */     {
/* 243:239 */       resultingUrl = URLUTF8Encoder.doURLEscaping(query_string, dest_url);
/* 244:    */       
/* 245:241 */       System.out.println(new Date() + ":Forwarding_URL_postURLEscaping: " + resultingUrl);
/* 246:    */     }
/* 247:    */     catch (Exception e)
/* 248:    */     {
/* 249:243 */       System.out.println(new Date() + ":Error in the forward URL:" + dest_url);
/* 250:    */       try
/* 251:    */       {
/* 252:245 */         if (siteType.equals("2")) {
/* 253:246 */           message = feedback.getUserFriendlyDescription("10002");
/* 254:    */         } else {
/* 255:248 */           message = feedback.formDefaultMessage("10002");
/* 256:    */         }
/* 257:    */       }
/* 258:    */       catch (Exception ex)
/* 259:    */       {
/* 260:251 */         message = ex.getMessage();
/* 261:    */       }
/* 262:254 */       boolean isAsciiPrintable = StringUtils.isAsciiPrintable(message);
/* 263:255 */       if (!isAsciiPrintable)
/* 264:    */       {
/* 265:256 */         System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
/* 266:257 */         request.setAttribute("X-Kannel-Coding", "2");
/* 267:258 */         if (request.getAttribute("X-Kannel-Coding") != null) {
/* 268:259 */           System.out.println("Request contains X-Kannel-Coding attribute");
/* 269:    */         }
/* 270:    */       }
/* 271:261 */       out.println(message);
/* 272:262 */       return;
/* 273:    */     }
/* 274:265 */     HttpClient client = new HttpClient();
/* 275:266 */     GetMethod httpGETFORM = new GetMethod(resultingUrl);
/* 276:267 */     httpGETFORM.getParams().setParameter("http.socket.timeout", new Integer(fs.getTimeout()));
/* 277:268 */     String resp = "";
/* 278:    */     try
/* 279:    */     {
/* 280:271 */       client.executeMethod(httpGETFORM);
/* 281:272 */       resp = getResponse(httpGETFORM.getResponseBodyAsStream());
/* 282:    */     }
/* 283:    */     catch (HttpException e)
/* 284:    */     {
/* 285:277 */       resp = "5001: " + e.getMessage();
/* 286:    */       
/* 287:279 */       System.out.println(new Date() + ":error response: " + resp);
/* 288:    */     }
/* 289:    */     catch (IOException e)
/* 290:    */     {
/* 291:282 */       resp = "5002: " + e.getMessage();
/* 292:    */       
/* 293:284 */       System.out.println(new Date() + ":error response: " + resp);
/* 294:    */     }
/* 295:    */     catch (Exception e)
/* 296:    */     {
/* 297:288 */       System.out.println(new Date() + ":error response: " + e.getMessage());
/* 298:    */     }
/* 299:    */     finally
/* 300:    */     {
/* 301:292 */       httpGETFORM.releaseConnection();
/* 302:293 */       client = null;
/* 303:294 */       httpGETFORM = null;
/* 304:    */     }
/* 305:297 */     if (fs.getListenStatus() == 1)
/* 306:    */     {
/* 307:299 */       System.out.println(new Date() + ":ListenStatus= LISTEN_FOR_REPLY");
/* 308:    */       
/* 309:301 */       System.out.println(new Date() + ":response: " + resp);
/* 310:    */       
/* 311:303 */       boolean isAsciiPrintable = StringUtils.isAsciiPrintable(resp);
/* 312:304 */       if (!isAsciiPrintable)
/* 313:    */       {
/* 314:305 */         System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
/* 315:306 */         request.setAttribute("X-Kannel-Coding", "2");
/* 316:307 */         if (request.getAttribute("X-Kannel-Coding") != null) {
/* 317:308 */           System.out.println("Request contains X-Kannel-Coding attribute");
/* 318:    */         }
/* 319:    */       }
/* 320:310 */       out.println(resp);
/* 321:    */     }
/* 322:311 */     else if (fs.getListenStatus() == 2)
/* 323:    */     {
/* 324:313 */       System.out.println(new Date() + ":ListenStatus= REPLY_WITH_DEFAULT");
/* 325:    */       
/* 326:315 */       String insertions = "ack=" + ack + "&keyword=" + kw + "&msg=" + msgBody + "&msisdn=" + msisdn + "&dest=" + dest + "&date=" + date + "&phoneId=" + phoneId + "&ua=" + ua + "&regId=" + regId + "&resp=" + resp;
/* 327:    */       
/* 328:    */ 
/* 329:318 */       System.out.println(new Date() + ":insertions= " + insertions);
/* 330:    */       try
/* 331:    */       {
/* 332:322 */         ack = URLUTF8Encoder.doMessageEscaping(insertions, ack);
/* 333:    */       }
/* 334:    */       catch (Exception e)
/* 335:    */       {
/* 336:325 */         System.out.println(new Date() + ":Error doing MessageEscaping:");
/* 337:326 */         e.printStackTrace();
/* 338:    */         
/* 339:328 */         ack = "";
/* 340:    */       }
/* 341:330 */       System.out.println(new Date() + ":response: " + ack);
/* 342:    */       
/* 343:332 */       boolean isAsciiPrintable = StringUtils.isAsciiPrintable(ack);
/* 344:333 */       if (!isAsciiPrintable)
/* 345:    */       {
/* 346:334 */         System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
/* 347:335 */         request.setAttribute("X-Kannel-Coding", "2");
/* 348:336 */         if (request.getAttribute("X-Kannel-Coding") != null) {
/* 349:337 */           System.out.println("Request contains X-Kannel-Coding attribute");
/* 350:    */         }
/* 351:    */       }
/* 352:339 */       out.println(ack);
/* 353:    */     }
/* 354:    */     else
/* 355:    */     {
/* 356:343 */       System.out.println(new Date() + ":ListenStatus= NEVER_LISTEN_FOR_REPLY");
/* 357:    */       
/* 358:345 */       System.out.println(new Date() + ":response: " + resp);
/* 359:    */     }
/* 360:    */   }
/* 361:    */   
/* 362:    */   public void doPost(HttpServletRequest request, HttpServletResponse response)
/* 363:    */     throws ServletException, IOException
/* 364:    */   {
/* 365:353 */     doGet(request, response);
/* 366:    */   }
/* 367:    */   
/* 368:    */   public void destroy() {}
/* 369:    */   
/* 370:    */   public void forward(ServletRequest request, ServletResponse response)
/* 371:    */     throws ServletException, IOException
/* 372:    */   {
/* 373:362 */     HttpServletRequest req = (HttpServletRequest)request;
/* 374:363 */     HttpServletResponse resp = (HttpServletResponse)response;
/* 375:364 */     doGet(req, resp);
/* 376:    */   }
/* 377:    */   
/* 378:    */   public void include(ServletRequest request, ServletResponse response)
/* 379:    */     throws ServletException, IOException
/* 380:    */   {
/* 381:369 */     HttpServletRequest req = (HttpServletRequest)request;
/* 382:370 */     HttpServletResponse resp = (HttpServletResponse)response;
/* 383:371 */     doGet(req, resp);
/* 384:    */   }
/* 385:    */   
/* 386:    */   private String getResponse(InputStream in)
/* 387:    */     throws Exception
/* 388:    */   {
/* 389:375 */     String status = "error";
/* 390:376 */     String reply = "";
/* 391:377 */     String error = "";
/* 392:378 */     String responseString = "";
/* 393:379 */     BufferedReader br = null;
/* 394:    */     try
/* 395:    */     {
/* 396:381 */       InputStream responseBody = in;
/* 397:382 */       br = new BufferedReader(new InputStreamReader(responseBody));
/* 398:    */       
/* 399:384 */       String line = br.readLine();
/* 400:385 */       while (line != null)
/* 401:    */       {
/* 402:386 */         responseString = responseString + line;
/* 403:387 */         line = br.readLine();
/* 404:    */       }
/* 405:    */     }
/* 406:    */     catch (IOException e)
/* 407:    */     {
/* 408:390 */       throw new Exception("5002: " + e.getMessage());
/* 409:    */     }
/* 410:    */     finally
/* 411:    */     {
/* 412:392 */       br.close();
/* 413:393 */       in.close();
/* 414:    */       
/* 415:395 */       br = null;
/* 416:396 */       in = null;
/* 417:    */     }
/* 418:399 */     return responseString;
/* 419:    */   }
/* 420:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.serviceinterfaces.forwardservice
 * JD-Core Version:    0.7.0.1
 */