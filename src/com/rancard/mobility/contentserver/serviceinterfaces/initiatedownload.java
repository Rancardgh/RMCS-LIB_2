/*   1:    */ package com.rancard.mobility.contentserver.serviceinterfaces;
/*   2:    */ 
/*   3:    */ import com.rancard.mobility.common.Driver;
/*   4:    */ import com.rancard.mobility.common.PushDriver;
/*   5:    */ import com.rancard.mobility.contentserver.CPConnections;
/*   6:    */ import com.rancard.mobility.contentserver.CPSite;
/*   7:    */ import com.rancard.mobility.contentserver.ContentItem;
/*   8:    */ import com.rancard.mobility.contentserver.Format;
/*   9:    */ import com.rancard.mobility.contentserver.RepositoryManager;
/*  10:    */ import com.rancard.mobility.contentserver.Transaction;
/*  11:    */ import com.rancard.mobility.contentserver.uploadsBean;
/*  12:    */ import com.rancard.mobility.infoserver.common.services.ServiceManager;
/*  13:    */ import java.io.IOException;
/*  14:    */ import java.io.PrintStream;
/*  15:    */ import java.io.PrintWriter;
/*  16:    */ import java.util.Calendar;
/*  17:    */ import java.util.Date;
/*  18:    */ import javax.servlet.RequestDispatcher;
/*  19:    */ import javax.servlet.ServletContext;
/*  20:    */ import javax.servlet.ServletException;
/*  21:    */ import javax.servlet.ServletRequest;
/*  22:    */ import javax.servlet.ServletResponse;
/*  23:    */ import javax.servlet.http.HttpServlet;
/*  24:    */ import javax.servlet.http.HttpServletRequest;
/*  25:    */ import javax.servlet.http.HttpServletResponse;
/*  26:    */ 
/*  27:    */ public class initiatedownload
/*  28:    */   extends HttpServlet
/*  29:    */   implements RequestDispatcher
/*  30:    */ {
/*  31:    */   private static final String FROM = "RMCS";
/*  32:    */   
/*  33:    */   public void init()
/*  34:    */     throws ServletException
/*  35:    */   {}
/*  36:    */   
/*  37:    */   public void doGet(HttpServletRequest request, HttpServletResponse response)
/*  38:    */     throws ServletException, IOException
/*  39:    */   {
/*  40: 26 */     PrintWriter out = response.getWriter();
/*  41: 27 */     int isBilled = 1;
/*  42: 28 */     int isDelivered = 0;
/*  43:    */     
/*  44:    */ 
/*  45: 31 */     String siteType = (String)request.getAttribute("siteType");
/*  46:    */     
/*  47:    */ 
/*  48:    */ 
/*  49:    */ 
/*  50:    */ 
/*  51:    */ 
/*  52: 38 */     String transactionId = (String)request.getAttribute("ticketId");
/*  53: 39 */     if (transactionId == null) {
/*  54: 40 */       transactionId = request.getParameter("ticketId");
/*  55:    */     }
/*  56: 42 */     Transaction transaction = new Transaction();
/*  57:    */     
/*  58:    */ 
/*  59: 45 */     System.out.println("Received request to initiate download with transaction ID: " + transactionId);
/*  60: 46 */     System.out.println("Date received: " + Calendar.getInstance().getTime().toString());
/*  61: 49 */     if ((transactionId == null) || (transactionId.equals("")))
/*  62:    */     {
/*  63: 50 */       System.out.println("3000");
/*  64: 51 */       return;
/*  65:    */     }
/*  66:    */     try
/*  67:    */     {
/*  68: 54 */       System.out.println("Looking up transaction details for transaction ID: " + transactionId + "...");
/*  69: 55 */       transaction = Transaction.viewTransaction(transactionId);
/*  70:    */     }
/*  71:    */     catch (Exception e)
/*  72:    */     {
/*  73: 60 */       System.out.println("Could not find transaction details for transaction ID: " + transactionId);
/*  74: 61 */       System.out.println("Exception thrown carries message: " + e.getMessage());
/*  75:    */       
/*  76: 63 */       System.out.println("3000");
/*  77: 64 */       return;
/*  78:    */     }
/*  79: 70 */     String s = request.getProtocol().toLowerCase();
/*  80: 71 */     s = s.substring(0, s.indexOf("/")).toLowerCase();
/*  81:    */     
/*  82:    */ 
/*  83: 74 */     String baseUrl = getServletContext().getInitParameter("contentServerPublicURL");
/*  84:    */     
/*  85: 76 */     ContentItem item = transaction.getContentItem();
/*  86: 77 */     Format format = item.getFormat();
/*  87: 78 */     CPConnections cnxn = null;
/*  88:    */     
/*  89: 80 */     System.out.println(new Date() + ":ContentItem: title=" + item.gettitle() + ", format=" + item.getFormat().getFileExt() + ", downloadURL=" + item.getDownloadUrl());
/*  90:    */     
/*  91: 82 */     String msisdn = transaction.getSubscriberMSISDN();
/*  92:    */     
/*  93: 84 */     System.out.println("User's mobile number is: " + msisdn);
/*  94:    */     
/*  95:    */ 
/*  96:    */ 
/*  97: 88 */     CPSite site = transaction.getSite();
/*  98:    */     try
/*  99:    */     {
/* 100: 92 */       System.out.println("Looking up available connections for content provider with ID: " + site.getCpId() + " ......");
/* 101:    */       
/* 102: 94 */       cnxn = CPConnections.getConnection(site.getCpId(), msisdn);
/* 103:    */     }
/* 104:    */     catch (Exception e)
/* 105:    */     {
/* 106: 96 */       System.out.println("8002");
/* 107: 97 */       return;
/* 108:    */     }
/* 109:    */     try
/* 110:    */     {
/* 111:104 */       if ("WAP".equals(siteType))
/* 112:    */       {
/* 113:106 */         RequestDispatcher dispatch = null;
/* 114:107 */         request.setAttribute("ticketId", transactionId);
/* 115:    */         try
/* 116:    */         {
/* 117:109 */           dispatch = request.getRequestDispatcher("downloadcontent");
/* 118:    */         }
/* 119:    */         catch (Exception e)
/* 120:    */         {
/* 121:111 */           throw new Exception("8000");
/* 122:    */         }
/* 123:114 */         dispatch.include(request, response);
/* 124:    */       }
/* 125:116 */       else if (routeNotification(item, msisdn, cnxn, transactionId, baseUrl, transaction.getKeyword()))
/* 126:    */       {
/* 127:118 */         System.out.println("Notification sent!!");
/* 128:120 */         if ((format.getPushBearer().equals("SMS")) || (format.getPushBearer().equals("SMS_BIN")))
/* 129:    */         {
/* 130:121 */           transaction.setDownloadCompleted(true);
/* 131:    */           
/* 132:123 */           System.out.println("Download Complete.");
/* 133:    */           
/* 134:125 */           Transaction.updateDownloadStatus(transactionId, true);
/* 135:126 */           isDelivered = 1;
/* 136:    */         }
/* 137:127 */         else if (format.getPushBearer().equals("WAP"))
/* 138:    */         {
/* 139:129 */           isDelivered = 1;
/* 140:    */         }
/* 141:    */       }
/* 142:    */       else
/* 143:    */       {
/* 144:133 */         System.out.println("Routing notification FAILED.");
/* 145:    */         
/* 146:    */ 
/* 147:136 */         System.out.println("8000");
/* 148:    */       }
/* 149:    */     }
/* 150:    */     catch (Exception e)
/* 151:    */     {
/* 152:144 */       System.out.println(e.getMessage());
/* 153:145 */       return;
/* 154:    */     }
/* 155:    */     finally
/* 156:    */     {
/* 157:147 */       cnxn = null;
/* 158:148 */       format = null;
/* 159:149 */       item = null;
/* 160:150 */       out = null;
/* 161:151 */       site = null;
/* 162:152 */       transaction = null;
/* 163:    */       try
/* 164:    */       {
/* 165:154 */         ServiceManager.updateTransaction(transactionId, isDelivered, isBilled);
/* 166:    */       }
/* 167:    */       catch (Exception e) {}
/* 168:    */     }
/* 169:    */   }
/* 170:    */   
/* 171:    */   public void doPost(HttpServletRequest request, HttpServletResponse response)
/* 172:    */     throws ServletException, IOException
/* 173:    */   {
/* 174:162 */     doGet(request, response);
/* 175:    */   }
/* 176:    */   
/* 177:    */   public void destroy() {}
/* 178:    */   
/* 179:    */   public boolean sendWapPushNotification(ContentItem item, String number, CPConnections cnxn, String transactionId, String baseURL, String service)
/* 180:    */     throws Exception
/* 181:    */   {
/* 182:172 */     String response = new String();
/* 183:173 */     boolean flag = false;
/* 184:    */     
/* 185:175 */     String url = baseURL + "downloadcontent?ticketId=" + transactionId;
/* 186:176 */     String text = "Downloading '" + item.gettitle() + "'...";
/* 187:    */     try
/* 188:    */     {
/* 189:179 */       response = Driver.getDriver(cnxn.getDriverType(), cnxn.getGatewayURL()).sendWAPPushMessage(number, "RMCS", text, url, cnxn.getUsername(), cnxn.getPassword(), cnxn.getConnection(), service, "0");
/* 190:    */     }
/* 191:    */     catch (Exception e)
/* 192:    */     {
/* 193:183 */       throw new Exception("5002");
/* 194:    */     }
/* 195:186 */     if ((response.length() > 18) && (response.substring(14, 18).equals("true"))) {
/* 196:187 */       flag = true;
/* 197:    */     }
/* 198:189 */     return flag;
/* 199:    */   }
/* 200:    */   
/* 201:    */   public boolean sendSMSNotification(ContentItem item, String number, CPConnections cnxn, String transactionId, String baseURL, String service)
/* 202:    */     throws Exception
/* 203:    */   {
/* 204:195 */     boolean flag = false;
/* 205:196 */     String response = new String();
/* 206:197 */     String txt = new String();
/* 207:198 */     byte[] itemStream = null;
/* 208:200 */     if (!item.islocal())
/* 209:    */     {
/* 210:201 */       itemStream = RepositoryManager.getByteArray(item.getDownloadUrl());
/* 211:    */     }
/* 212:    */     else
/* 213:    */     {
/* 214:203 */       new RepositoryManager();uploadsBean upload = RepositoryManager.fetchFile(item.getListId(), item.getid());
/* 215:204 */       itemStream = upload.getDataStream();
/* 216:    */     }
/* 217:206 */     txt = new String(itemStream);
/* 218:    */     try
/* 219:    */     {
/* 220:209 */       response = Driver.getDriver(cnxn.getDriverType(), cnxn.getGatewayURL()).sendSMSTextMessage(number, "RMCS", txt, cnxn.getUsername(), cnxn.getPassword(), cnxn.getConnection(), service, "0");
/* 221:    */     }
/* 222:    */     catch (Exception e)
/* 223:    */     {
/* 224:213 */       throw new Exception("5002");
/* 225:    */     }
/* 226:    */     finally
/* 227:    */     {
/* 228:215 */       itemStream = null;
/* 229:    */     }
/* 230:218 */     if ((response.length() > 18) && (response.substring(14, 18).equals("true"))) {
/* 231:219 */       flag = true;
/* 232:    */     }
/* 233:222 */     return flag;
/* 234:    */   }
/* 235:    */   
/* 236:    */   public boolean sendSMSBINNotification(ContentItem item, String number, CPConnections cnxn, String transactionId, String baseURL, String service)
/* 237:    */     throws Exception
/* 238:    */   {
/* 239:228 */     boolean flag = false;
/* 240:229 */     String format = item.getFormat().getFileExt();
/* 241:230 */     String response = new String();
/* 242:231 */     String txt = new String();
/* 243:232 */     String udh = new String();
/* 244:233 */     String cs = new String();
/* 245:    */     
/* 246:235 */     txt = RepositoryManager.getUD(format, item);
/* 247:    */     try
/* 248:    */     {
/* 249:238 */       response = Driver.getDriver(cnxn.getDriverType(), cnxn.getGatewayURL()).sendSMSBinaryMessage(number, "RMCS", txt, udh, cs, format, cnxn.getUsername(), cnxn.getPassword(), cnxn.getConnection(), service, "0");
/* 250:    */     }
/* 251:    */     catch (Exception e)
/* 252:    */     {
/* 253:242 */       throw new Exception("5002");
/* 254:    */     }
/* 255:245 */     if ((response.length() > 18) && (response.substring(14, 18).equals("true"))) {
/* 256:246 */       flag = true;
/* 257:    */     }
/* 258:249 */     return flag;
/* 259:    */   }
/* 260:    */   
/* 261:    */   public boolean routeNotification(ContentItem item, String number, CPConnections cnxn, String transactionId, String baseURL, String service)
/* 262:    */     throws Exception
/* 263:    */   {
/* 264:255 */     boolean flag = true;
/* 265:256 */     Format format = item.getFormat();
/* 266:257 */     if (format.getPushBearer().equals("WAP"))
/* 267:    */     {
/* 268:259 */       System.out.println("Sending WAP push.....");
/* 269:    */       
/* 270:261 */       flag = sendWapPushNotification(item, number, cnxn, transactionId, baseURL, service);
/* 271:    */     }
/* 272:262 */     else if (format.getPushBearer().equals("SMS"))
/* 273:    */     {
/* 274:264 */       System.out.println("Sending SMS.....");
/* 275:    */       
/* 276:266 */       flag = sendSMSNotification(item, number, cnxn, transactionId, baseURL, service);
/* 277:    */     }
/* 278:267 */     else if (format.getPushBearer().equals("SMS_BIN"))
/* 279:    */     {
/* 280:269 */       System.out.println("Sending SMS with binary content.....");
/* 281:    */       
/* 282:271 */       flag = sendSMSBINNotification(item, number, cnxn, transactionId, baseURL, service);
/* 283:    */     }
/* 284:273 */     return flag;
/* 285:    */   }
/* 286:    */   
/* 287:    */   public void forward(ServletRequest request, ServletResponse response)
/* 288:    */     throws ServletException, IOException
/* 289:    */   {
/* 290:278 */     HttpServletRequest req = (HttpServletRequest)request;
/* 291:279 */     HttpServletResponse resp = (HttpServletResponse)response;
/* 292:280 */     doGet(req, resp);
/* 293:    */   }
/* 294:    */   
/* 295:    */   public void include(ServletRequest request, ServletResponse response)
/* 296:    */     throws ServletException, IOException
/* 297:    */   {
/* 298:285 */     HttpServletRequest req = (HttpServletRequest)request;
/* 299:286 */     HttpServletResponse resp = (HttpServletResponse)response;
/* 300:287 */     doGet(req, resp);
/* 301:    */   }
/* 302:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.serviceinterfaces.initiatedownload
 * JD-Core Version:    0.7.0.1
 */