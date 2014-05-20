/*   1:    */ package com.rancard.mobility.contentserver.serviceinterfaces;
/*   2:    */ 
/*   3:    */ import com.rancard.common.Feedback;
/*   4:    */ import com.rancard.mobility.contentprovider.User;
/*   5:    */ import com.rancard.mobility.contentserver.CPConnections;
/*   6:    */ import com.rancard.mobility.infoserver.common.services.ServiceManager;
/*   7:    */ import com.rancard.mobility.infoserver.common.services.UserService;
/*   8:    */ import com.rancard.util.URLUTF8Encoder;
/*   9:    */ import java.io.BufferedReader;
/*  10:    */ import java.io.IOException;
/*  11:    */ import java.io.InputStream;
/*  12:    */ import java.io.InputStreamReader;
/*  13:    */ import java.io.PrintStream;
/*  14:    */ import java.io.PrintWriter;
/*  15:    */ import java.util.ArrayList;
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
/*  27:    */ public class processunsubscriberequest
/*  28:    */   extends HttpServlet
/*  29:    */   implements RequestDispatcher
/*  30:    */ {
/*  31:    */   public void init()
/*  32:    */     throws ServletException
/*  33:    */   {}
/*  34:    */   
/*  35:    */   public void doGet(HttpServletRequest request, HttpServletResponse response)
/*  36:    */     throws ServletException, IOException
/*  37:    */   {
/*  38: 39 */     PrintWriter out = response.getWriter();
/*  39: 40 */     CPConnections cnxn = new CPConnections();
/*  40: 41 */     String ACTION_UNSUBSCRIBE_KW = "1";
/*  41: 42 */     String ACTION_UNSUBSCRIBE_ALL = "2";
/*  42: 43 */     String action = "1";
/*  43:    */     
/*  44: 45 */     String ACK = (String)request.getAttribute("ack");
/*  45: 46 */     String kw = request.getParameter("keyword");
/*  46: 47 */     String msg = request.getParameter("msg");
/*  47: 48 */     String msisdn = request.getParameter("msisdn");
/*  48: 49 */     String provId = (String)request.getAttribute("acctId");
/*  49:    */     
/*  50: 51 */     String siteType = (String)request.getAttribute("site_type");
/*  51: 52 */     String lang = (String)request.getAttribute("default_lang");
/*  52: 53 */     if ((lang == null) || (lang.equals(""))) {
/*  53: 54 */       lang = "en";
/*  54:    */     }
/*  55: 56 */     Feedback feedback = (Feedback)getServletContext().getAttribute("feedback_" + lang);
/*  56: 57 */     if (feedback == null) {
/*  57:    */       try
/*  58:    */       {
/*  59: 59 */         feedback = new Feedback();
/*  60:    */       }
/*  61:    */       catch (Exception e) {}
/*  62:    */     }
/*  63: 64 */     String provName = "";
/*  64: 65 */     String message = "";
/*  65: 67 */     if ((provId == null) || (provId.equals("")))
/*  66:    */     {
/*  67:    */       try
/*  68:    */       {
/*  69: 69 */         if (siteType.equals("2")) {
/*  70: 70 */           message = feedback.getUserFriendlyDescription("10004");
/*  71:    */         } else {
/*  72: 72 */           message = feedback.formDefaultMessage("10004");
/*  73:    */         }
/*  74:    */       }
/*  75:    */       catch (Exception ex)
/*  76:    */       {
/*  77: 75 */         message = ex.getMessage();
/*  78:    */       }
/*  79: 77 */       out.println(message);
/*  80: 78 */       return;
/*  81:    */     }
/*  82: 81 */     if ((msisdn == null) || (msisdn.equals("")))
/*  83:    */     {
/*  84:    */       try
/*  85:    */       {
/*  86: 83 */         if (siteType.equals("2")) {
/*  87: 84 */           message = feedback.getUserFriendlyDescription("2000");
/*  88:    */         } else {
/*  89: 86 */           message = feedback.formDefaultMessage("2000");
/*  90:    */         }
/*  91:    */       }
/*  92:    */       catch (Exception ex)
/*  93:    */       {
/*  94: 89 */         message = ex.getMessage();
/*  95:    */       }
/*  96: 91 */       out.println(message);
/*  97: 92 */       return;
/*  98:    */     }
/*  99: 95 */     if ((msg == null) || (msg.equals(""))) {
/* 100: 96 */       msg = "";
/* 101:    */     }
/* 102: 99 */     if ((ACK == null) || (ACK.equals(""))) {
/* 103:100 */       ACK = "";
/* 104:    */     }
/* 105:104 */     System.out.println("Cancelling subscription to a service");
/* 106:105 */     System.out.println("Date received: " + Calendar.getInstance().getTime().toString());
/* 107:106 */     System.out.println("Keyword: " + kw);
/* 108:107 */     System.out.println("Message: " + msg);
/* 109:108 */     System.out.println("Subscriber's number: " + msisdn);
/* 110:109 */     System.out.println("Service privoder's ID: " + provId);
/* 111:    */     
/* 112:    */ 
/* 113:    */ 
/* 114:113 */     String number = msisdn;
/* 115:114 */     if (number.indexOf("+") != -1)
/* 116:    */     {
/* 117:115 */       StringBuffer sb = new StringBuffer(number);
/* 118:116 */       sb.deleteCharAt(number.indexOf("+"));
/* 119:117 */       number = sb.toString();
/* 120:    */       
/* 121:119 */       sb = null;
/* 122:    */     }
/* 123:121 */     number = number.trim();
/* 124:    */     try
/* 125:    */     {
/* 126:125 */       Long.parseLong(number);
/* 127:126 */       msisdn = "+" + number;
/* 128:    */     }
/* 129:    */     catch (NumberFormatException e)
/* 130:    */     {
/* 131:    */       try
/* 132:    */       {
/* 133:129 */         if (siteType.equals("2")) {
/* 134:130 */           message = feedback.getUserFriendlyDescription("2000");
/* 135:    */         } else {
/* 136:132 */           message = feedback.formDefaultMessage("2000");
/* 137:    */         }
/* 138:    */       }
/* 139:    */       catch (Exception ex)
/* 140:    */       {
/* 141:135 */         message = ex.getMessage();
/* 142:    */       }
/* 143:137 */       out.println(message);
/* 144:138 */       return;
/* 145:    */     }
/* 146:    */     try
/* 147:    */     {
/* 148:144 */       provName = new User().viewDealer(provId).getName();
/* 149:    */       
/* 150:146 */       String serviceNames = "";
/* 151:147 */       if ((msg != null) && (!msg.equals("")))
/* 152:    */       {
/* 153:149 */         String[] keywords = msg.split(",");
/* 154:150 */         ArrayList<String> keywordList = new ArrayList();
/* 155:151 */         if (keywords.length > 1)
/* 156:    */         {
/* 157:153 */           for (int i = 0; i < keywords.length; i++)
/* 158:    */           {
/* 159:155 */             keywordList.add(keywords[i].trim());
/* 160:156 */             System.out.print("Keyword #" + i + " = " + (String)keywordList.get(i));
/* 161:    */           }
/* 162:158 */           ServiceManager.forceUnsubscribe(msisdn, keywordList, provId);
/* 163:159 */           ACK = "You have successfully cancelled your subscriptions. Thank you for using our services.";
/* 164:160 */           action = "0";
/* 165:    */         }
/* 166:    */         else
/* 167:    */         {
/* 168:    */           try
/* 169:    */           {
/* 170:163 */             UserService service = ServiceManager.viewService(msg, provId);
/* 171:164 */             if ((service == null) || (service.getAccountId() == null) || (service.getKeyword() == null) || (service.getAccountId().equals("")) || (service.getKeyword().equals("")))
/* 172:    */             {
/* 173:166 */               service = ServiceManager.viewServiceByAlias(msg, provId);
/* 174:167 */               if ((service == null) || (service.getAccountId() == null) || (service.getKeyword() == null) || (service.getAccountId().equals("")) || (service.getKeyword().equals(""))) {
/* 175:168 */                 action = "2";
/* 176:    */               } else {
/* 177:171 */                 msg = service.getKeyword();
/* 178:    */               }
/* 179:    */             }
/* 180:175 */             serviceNames = service.getServiceName();
/* 181:176 */             service = null;
/* 182:    */           }
/* 183:    */           catch (Exception ex)
/* 184:    */           {
/* 185:178 */             throw new Exception("10001");
/* 186:    */           }
/* 187:    */         }
/* 188:    */       }
/* 189:    */       else
/* 190:    */       {
/* 191:183 */         action = "2";
/* 192:    */       }
/* 193:187 */       if (action.equals("2"))
/* 194:    */       {
/* 195:188 */         ServiceManager.unsubscribeToService(msisdn, "", provId);
/* 196:    */         
/* 197:190 */         ACK = feedback.getValue("CANCEL_ALL_SUBSCRIPTIONS");
/* 198:    */       }
/* 199:191 */       else if (action.equals("1"))
/* 200:    */       {
/* 201:199 */         ArrayList<String> keywords = new ArrayList();
/* 202:200 */         keywords.add(msg);
/* 203:201 */         keywords.add(msg + "2");
/* 204:202 */         ServiceManager.forceUnsubscribe(msisdn, keywords, provId);
/* 205:    */       }
/* 206:211 */       String insertions = "ack=" + ACK + "&keyword=" + kw + "&msg=" + msg + "&msisdn=" + msisdn + "&acctId=" + provId + "&srvcName=" + serviceNames + "&provName=" + provName;
/* 207:212 */       ACK = URLUTF8Encoder.doMessageEscaping(insertions, ACK);
/* 208:    */       
/* 209:214 */       out.println(ACK);
/* 210:    */       
/* 211:    */ 
/* 212:    */ 
/* 213:    */ 
/* 214:    */ 
/* 215:    */ 
/* 216:    */ 
/* 217:    */ 
/* 218:    */ 
/* 219:    */ 
/* 220:    */ 
/* 221:    */ 
/* 222:    */ 
/* 223:    */ 
/* 224:    */ 
/* 225:    */ 
/* 226:    */ 
/* 227:232 */       cnxn = null;
/* 228:    */     }
/* 229:    */     catch (Exception e)
/* 230:    */     {
/* 231:    */       try
/* 232:    */       {
/* 233:236 */         if (siteType.equals("2")) {
/* 234:237 */           message = feedback.getUserFriendlyDescription(e.getMessage());
/* 235:    */         } else {
/* 236:239 */           message = feedback.formDefaultMessage(e.getMessage());
/* 237:    */         }
/* 238:241 */         if (message == null) {
/* 239:242 */           message = e.getMessage();
/* 240:    */         }
/* 241:    */       }
/* 242:    */       catch (Exception ex)
/* 243:    */       {
/* 244:245 */         message = ex.getMessage();
/* 245:    */       }
/* 246:247 */       out.println(message);
/* 247:248 */       return;
/* 248:    */     }
/* 249:    */   }
/* 250:    */   
/* 251:    */   private String getResponse(InputStream in)
/* 252:    */     throws Exception
/* 253:    */   {
/* 254:253 */     String status = "error";
/* 255:254 */     String reply = "";
/* 256:255 */     String error = "";
/* 257:256 */     String responseString = "";
/* 258:257 */     BufferedReader br = null;
/* 259:    */     try
/* 260:    */     {
/* 261:259 */       InputStream responseBody = in;
/* 262:260 */       br = new BufferedReader(new InputStreamReader(responseBody));
/* 263:    */       
/* 264:262 */       String line = br.readLine();
/* 265:263 */       while (line != null)
/* 266:    */       {
/* 267:264 */         responseString = responseString + line;
/* 268:265 */         line = br.readLine();
/* 269:    */       }
/* 270:    */     }
/* 271:    */     catch (IOException e)
/* 272:    */     {
/* 273:268 */       throw new Exception("5002: " + e.getMessage());
/* 274:    */     }
/* 275:    */     finally
/* 276:    */     {
/* 277:270 */       br.close();
/* 278:271 */       in.close();
/* 279:    */       
/* 280:273 */       br = null;
/* 281:274 */       in = null;
/* 282:    */     }
/* 283:277 */     return responseString;
/* 284:    */   }
/* 285:    */   
/* 286:    */   public void doPost(HttpServletRequest request, HttpServletResponse response)
/* 287:    */     throws ServletException, IOException
/* 288:    */   {
/* 289:283 */     doGet(request, response);
/* 290:    */   }
/* 291:    */   
/* 292:    */   public void destroy() {}
/* 293:    */   
/* 294:    */   public void forward(ServletRequest request, ServletResponse response)
/* 295:    */     throws ServletException, IOException
/* 296:    */   {
/* 297:291 */     HttpServletRequest req = (HttpServletRequest)request;
/* 298:292 */     HttpServletResponse resp = (HttpServletResponse)response;
/* 299:293 */     doGet(req, resp);
/* 300:    */   }
/* 301:    */   
/* 302:    */   public void include(ServletRequest request, ServletResponse response)
/* 303:    */     throws ServletException, IOException
/* 304:    */   {
/* 305:298 */     HttpServletRequest req = (HttpServletRequest)request;
/* 306:299 */     HttpServletResponse resp = (HttpServletResponse)response;
/* 307:300 */     doGet(req, resp);
/* 308:    */   }
/* 309:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.serviceinterfaces.processunsubscriberequest
 * JD-Core Version:    0.7.0.1
 */