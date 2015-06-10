/*   1:    */ package com.rancard.mobility.contentserver.serviceinterfaces;
/*   2:    */ 
/*   3:    */ import com.rancard.common.Feedback;
/*   4:    */ import com.rancard.mobility.common.Driver;
/*   5:    */ import com.rancard.mobility.common.PushDriver;
/*   6:    */ import com.rancard.mobility.contentprovider.User;
/*   7:    */ import com.rancard.mobility.contentserver.CPConnections;
/*   8:    */ import com.rancard.mobility.infoserver.common.services.ServiceManager;
/*   9:    */ import com.rancard.mobility.infoserver.common.services.UserService;
import com.rancard.rndvu.events.UserEvents;
/*  10:    */ import com.rancard.util.URLUTF8Encoder;
/*  11:    */ import java.io.IOException;
/*  12:    */ import java.io.PrintStream;
/*  13:    */ import java.io.PrintWriter;
/*  14:    */ import java.util.ArrayList;
/*  15:    */ import java.util.Date;
import java.util.HashMap;
/*  16:    */ import javax.servlet.RequestDispatcher;
/*  17:    */ import javax.servlet.ServletContext;
/*  18:    */ import javax.servlet.ServletException;
/*  19:    */ import javax.servlet.ServletRequest;
/*  20:    */ import javax.servlet.ServletResponse;
/*  21:    */ import javax.servlet.http.HttpServlet;
/*  22:    */ import javax.servlet.http.HttpServletRequest;
/*  23:    */ import javax.servlet.http.HttpServletResponse;
/*  24:    */ 
/*  25:    */ public class processsubscriberequest
/*  26:    */   extends HttpServlet
/*  27:    */   implements RequestDispatcher
/*  28:    */ {
/*  29:    */   public void init()
/*  30:    */     throws ServletException
/*  31:    */   {}
/*  32:    */   
/*  33:    */   public void doGet(HttpServletRequest request, HttpServletResponse response)
/*  34:    */     throws ServletException, IOException
/*  35:    */   {
/*  36: 39 */     PrintWriter out = response.getWriter();
/*  37: 40 */     CPConnections cnxn = new CPConnections();
/*  38: 41 */     String[] regId = new String[2];
/*  39:    */     
/*  40: 43 */     String ACK = (String)request.getAttribute("ack");
/*  41: 44 */     String kw = request.getParameter("keyword");
/*  42: 45 */     String msg = request.getParameter("msg");
/*  43: 46 */     String msisdn = request.getParameter("msisdn");
/*  44: 47 */     String provId = (String)request.getAttribute("acctId");
/*  45: 48 */     String subsPeriodStr = request.getParameter("subs_period");
/*  46: 51 */     if ((provId == null) || (provId.equals(""))) {
/*  47: 52 */       provId = request.getParameter("acctId");
/*  48:    */     }
/*  49: 54 */     String cmd = (String)request.getAttribute("cmd");
/*  50: 55 */     if ((cmd == null) || (cmd.equals(""))) {
/*  51: 56 */       cmd = "0";
/*  52:    */     }
/*  53: 59 */     String siteType = (String)request.getAttribute("site_type");
/*  54: 60 */     String lang = (String)request.getAttribute("default_lang");
/*  55: 61 */     if ((lang == null) || (lang.equals(""))) {
/*  56: 62 */       lang = "en";
/*  57:    */     }
/*  58: 64 */     Feedback feedback = (Feedback)getServletContext().getAttribute("feedback_" + lang);
/*  59: 65 */     if (feedback == null) {
/*  60:    */       try
/*  61:    */       {
/*  62: 67 */         feedback = new Feedback();
/*  63:    */       }
/*  64:    */       catch (Exception e) {}
/*  65:    */     }
/*  66: 72 */     String message = "";
/*  67: 73 */     String provName = "";
/*  68: 75 */     if ((provId == null) || (provId.equals("")))
/*  69:    */     {
/*  70:    */       try
/*  71:    */       {
/*  72: 77 */         if (siteType.equals("2")) {
/*  73: 78 */           message = feedback.getUserFriendlyDescription("10004");
/*  74:    */         } else {
/*  75: 80 */           message = feedback.formDefaultMessage("10004");
/*  76:    */         }
/*  77:    */       }
/*  78:    */       catch (Exception ex)
/*  79:    */       {
/*  80: 83 */         message = ex.getMessage();
/*  81:    */       }
/*  82: 85 */       out.println(message);
/*  83: 86 */       return;
/*  84:    */     }
/*  85: 89 */     if ((msisdn == null) || (msisdn.equals("")))
/*  86:    */     {
/*  87:    */       try
/*  88:    */       {
/*  89: 91 */         if (siteType.equals("2")) {
/*  90: 92 */           message = feedback.getUserFriendlyDescription("2000");
/*  91:    */         } else {
/*  92: 94 */           message = feedback.formDefaultMessage("2000");
/*  93:    */         }
/*  94:    */       }
/*  95:    */       catch (Exception ex)
/*  96:    */       {
/*  97: 97 */         message = ex.getMessage();
/*  98:    */       }
/*  99: 99 */       out.println(message);
/* 100:100 */       return;
/* 101:    */     }
/* 102:103 */     if ((msg == null) || (msg.equals(""))) {
/* 103:104 */       msg = "";
/* 104:    */     }
/* 105:107 */     if ((ACK == null) || (ACK.equals(""))) {
/* 106:108 */       ACK = "";
/* 107:    */     }
/* 108:112 */     System.out.println(new Date() + ":Received request to subscribe to a service");
/* 109:113 */     System.out.println(new Date() + ":Keyword: " + kw);
/* 110:114 */     System.out.println(new Date() + ":Message: " + msg);
/* 111:115 */     System.out.println(new Date() + ":Subscriber's number: " + msisdn);
/* 112:116 */     System.out.println(new Date() + ":Service privoder's ID: " + provId);
/* 113:    */     
/* 114:    */ 
/* 115:    */ 
/* 116:120 */     String number = msisdn;
/* 117:121 */     if (number.indexOf("+") != -1)
/* 118:    */     {
/* 119:122 */       StringBuffer sb = new StringBuffer(number);
/* 120:123 */       sb.deleteCharAt(number.indexOf("+"));
/* 121:124 */       number = sb.toString();
/* 122:    */       
/* 123:126 */       sb = null;
/* 124:    */     }
/* 125:128 */     number = number.trim();
/* 126:    */     try
/* 127:    */     {
/* 128:132 */       Long.parseLong(number);
/* 129:133 */       msisdn = "+" + number;
/* 130:    */     }
/* 131:    */     catch (NumberFormatException e)
/* 132:    */     {
/* 133:    */       try
/* 134:    */       {
/* 135:136 */         if (siteType.equals("2"))
/* 136:    */         {
/* 137:137 */           System.out.println(new Date() + ":MISSING or INVALID_MSISDN: " + number);
/* 138:138 */           message = feedback.getUserFriendlyDescription("2000");
/* 139:    */         }
/* 140:    */         else
/* 141:    */         {
/* 142:140 */           System.out.println(new Date() + ":MISSING or INVALID_MSISDN: " + number);
/* 143:141 */           message = feedback.formDefaultMessage("2000");
/* 144:    */         }
/* 145:    */       }
/* 146:    */       catch (Exception ex)
/* 147:    */       {
/* 148:144 */         System.out.println(new Date() + ":ERROR: " + ex.getMessage());
/* 149:145 */         message = ex.getMessage();
/* 150:    */       }
/* 151:147 */       out.println(message);
/* 152:148 */       return;
/* 153:    */     }
/* 154:    */     try
/* 155:    */     {
/* 156:    */       try
/* 157:    */       {
/* 158:155 */         cnxn = CPConnections.getConnection(provId, msisdn);
/* 159:    */       }
/* 160:    */       catch (Exception e)
/* 161:    */       {
/* 162:157 */         throw new Exception("8002");
/* 163:    */       }
/* 164:160 */       ArrayList keywords = new ArrayList();
/* 165:161 */       String resp = new String();
                                  String resolvedMsg = msg;
/* 166:    */       
/* 167:163 */       String serviceNames = "";
/* 168:164 */       if ((msg != null) && (!msg.equals("")))
/* 169:    */       {
/* 170:166 */         if (!keywords.contains(msg)) {
                                        // For vodafone services, check if message sent matches any configured alias for any service, then use that service's keyword instead
                                        UserService serviceAlias = null;
                                        if (provId.equals("000")){
                                            try{
                                                serviceAlias = ServiceManager.viewServiceByAlias(msg, provId);
                                                String resolvedKeyword = serviceAlias.getKeyword() == null || serviceAlias.getKeyword().equals("") ? msg : serviceAlias.getKeyword();
                                                keywords.add(resolvedKeyword);
                                                resolvedMsg = resolvedKeyword;
                                            } catch (Exception ex){
                                                System.out.println(new Date() + "\t[processsubscriberequest]\tMessage ("+msg+") does not match any configured alias");
                                            }
                                        }
                                        
                                        if (serviceAlias == null){
                                            keywords.add(msg);
                                        }
/* 172:    */         }
/* 173:    */       }
/* 174:    */       else {
/* 175:170 */         keywords = ServiceManager.getKeywordsOfBasicServices(provId);
/* 176:    */       }
/* 177:    */       try
/* 178:    */       {
/* 179:174 */         serviceNames = ServiceManager.viewService(keywords.get(0).toString(), provId).getServiceName();
/* 180:175 */         for (int i = 1; i < keywords.size(); i++) {
/* 181:176 */           serviceNames = serviceNames + ", " + ServiceManager.viewService(keywords.get(i).toString(), provId).getServiceName();
/* 182:    */         }
/* 183:178 */         StringBuffer sb = new StringBuffer(serviceNames);
/* 184:179 */         if (serviceNames.lastIndexOf(",") != -1) {
/* 185:180 */           sb.replace(serviceNames.lastIndexOf(","), serviceNames.lastIndexOf(",") + 1, " and");
/* 186:    */         }
/* 187:182 */         serviceNames = sb.toString();
/* 188:183 */         sb = null;
/* 189:    */       }
/* 190:    */       catch (Exception ex)
/* 191:    */       {
                                    final String rndvuMsisdn = msisdn;
                                    final String clientId = "74nc4r6rn6vu";
                                    final String searchString = keywords.get(0) != null ? keywords.get(0).toString() : null;
                                    if (searchString == null){
                                        throw new Exception("10001");
                                    } else {
                                        // Log User HELP/SEARCH action
                                        new Thread(new Runnable() {

                                            @Override
                                            public void run() {
                                                try{
                                                    UserEvents.help(rndvuMsisdn, clientId, searchString);
                                                } catch (Exception ex){
                                                    System.out.println(new java.util.Date()+"\tERROR\t[processsubscriberequest]\t"+rndvuMsisdn+"\tError while writing User action [SEARCH] to RNDVU Graph: "+ex.getMessage());
                                                }
                                            }
                                        }).start();
    /* 192:185 */         throw new Exception("10001");
                                    }
/* 193:    */       }
/* 194:191 */       int numOfDays = 0;
                            HashMap thisSubscription = ServiceManager.getSubscription(msisdn, provId, resolvedMsg, null);
                            if ((thisSubscription != null) && (!thisSubscription.isEmpty())) {
                                // Log Rendezvous ALREADY SUBSCRIBED (MORE) here
                                System.out.println(new java.util.Date()+"\tINFO\t[processsubscriberequest]\tSubscription cannot be completed. "+msisdn+" has an existing "+resolvedMsg.toUpperCase()+" subscription");
                            } else {
                                if ((subsPeriodStr != null) && (!"".equals(subsPeriodStr)))
    /* 196:    */       {
    /* 197:193 */         numOfDays = Integer.parseInt(subsPeriodStr);
    /* 198:    */         
    /* 199:195 */         ServiceManager.subscribeToService(msisdn, keywords, provId, numOfDays);
    /* 200:    */       }
    /* 201:    */       else
    /* 202:    */       {
    /* 203:198 */         ServiceManager.subscribeToService(msisdn, keywords, provId, numOfDays);
    /* 204:    */       }
    /* 205:204 */       System.out.println("Subscription completed.");
                            }
/* 206:    */       
/* 207:    */ 
/* 208:207 */       provName = new User().viewDealer(provId).getName();
/* 209:208 */       String insertions = "ack=" + ACK + "&keyword=" + kw + "&msg=" + resolvedMsg + "&msisdn=" + msisdn + "&provName=" + provName + "&pin=" + regId[0] + "&srvcName=" + serviceNames;
/* 210:209 */       ACK = URLUTF8Encoder.doMessageEscaping(insertions, ACK);
/* 211:211 */       if (regId[1] == null) {
/* 212:212 */         message = ACK;
/* 213:    */       } else {
/* 214:214 */         message = regId[1] + " " + ACK + " ";
/* 215:    */       }
/* 216:226 */       if (cmd.equals("0")) {
/* 217:227 */         out.println(message);
/* 218:228 */       } else if (cmd.equals("1")) {
/* 219:    */         try
/* 220:    */         {
/* 221:230 */           Driver.getDriver(cnxn.getDriverType(), cnxn.getGatewayURL()).sendSMSTextMessage(msisdn, provName, message, cnxn.getUsername(), cnxn.getPassword(), cnxn.getConnection(), "", "0");
/* 222:    */         }
/* 223:    */         catch (Exception e)
/* 224:    */         {
/* 225:233 */           System.out.println("5002");
/* 226:234 */           throw new Exception("5002");
/* 227:    */         }
/* 228:    */       }
/* 229:238 */       keywords = null;
/* 230:239 */       cnxn = null;
/* 231:    */     }
/* 232:    */     catch (Exception e)
/* 233:    */     {
/* 234:    */       try
/* 235:    */       {
/* 236:243 */         if (siteType.equals("2")) {
/* 237:244 */           message = feedback.getUserFriendlyDescription(e.getMessage());
/* 238:    */         } else {
/* 239:246 */           message = feedback.formDefaultMessage(e.getMessage());
/* 240:    */         }
/* 241:248 */         if (message == null) {
/* 242:249 */           message = e.getMessage();
/* 243:    */         }
/* 244:    */       }
/* 245:    */       catch (Exception ex)
/* 246:    */       {
/* 247:252 */         message = ex.getMessage();
/* 248:    */       }
/* 249:255 */       if (cmd.equals("0")) {
/* 250:256 */         out.println(message);
/* 251:257 */       } else if (cmd.equals("1")) {
/* 252:    */         try
/* 253:    */         {
/* 254:259 */           Driver.getDriver(cnxn.getDriverType(), cnxn.getGatewayURL()).sendSMSTextMessage(msisdn, provName, message, cnxn.getUsername(), cnxn.getPassword(), cnxn.getConnection(), "", "0");
/* 255:    */         }
/* 256:    */         catch (Exception ex)
/* 257:    */         {
/* 258:262 */           System.out.println("5002");
/* 259:    */         }
/* 260:    */       }
/* 261:265 */       return;
/* 262:    */     }
/* 263:    */   }
/* 264:    */   
/* 265:    */   public void doPost(HttpServletRequest request, HttpServletResponse response)
/* 266:    */     throws ServletException, IOException
/* 267:    */   {
/* 268:273 */     doGet(request, response);
/* 269:    */   }
/* 270:    */   
/* 271:    */   public void destroy() {}
/* 272:    */   
/* 273:    */   public void forward(ServletRequest request, ServletResponse response)
/* 274:    */     throws ServletException, IOException
/* 275:    */   {
/* 276:282 */     HttpServletRequest req = (HttpServletRequest)request;
/* 277:283 */     HttpServletResponse resp = (HttpServletResponse)response;
/* 278:284 */     doGet(req, resp);
/* 279:    */   }
/* 280:    */   
/* 281:    */   public void include(ServletRequest request, ServletResponse response)
/* 282:    */     throws ServletException, IOException
/* 283:    */   {
/* 284:289 */     HttpServletRequest req = (HttpServletRequest)request;
/* 285:290 */     HttpServletResponse resp = (HttpServletResponse)response;
/* 286:291 */     doGet(req, resp);
/* 287:    */   }
/* 288:    */ }



/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar

 * Qualified Name:     com.rancard.mobility.contentserver.serviceinterfaces.processsubscriberequest

 * JD-Core Version:    0.7.0.1

 */