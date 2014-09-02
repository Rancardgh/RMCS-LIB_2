/*   1:    */ package com.rancard.mobility.contentserver.serviceinterfaces;
/*   2:    */ 
/*   3:    */ import com.rancard.common.Feedback;
/*   4:    */ import com.rancard.mobility.contentserver.CPConnections;
/*   5:    */ import com.rancard.mobility.infoserver.common.services.ServiceManager;
/*   6:    */ import java.io.IOException;
/*   7:    */ import java.io.PrintStream;
/*   8:    */ import java.io.PrintWriter;
/*   9:    */ import java.util.Calendar;
/*  10:    */ import java.util.Date;
/*  11:    */ import javax.servlet.RequestDispatcher;
/*  12:    */ import javax.servlet.ServletContext;
/*  13:    */ import javax.servlet.ServletException;
/*  14:    */ import javax.servlet.ServletRequest;
/*  15:    */ import javax.servlet.ServletResponse;
/*  16:    */ import javax.servlet.http.HttpServlet;
/*  17:    */ import javax.servlet.http.HttpServletRequest;
/*  18:    */ import javax.servlet.http.HttpServletResponse;
/*  19:    */ 
/*  20:    */ public class processsubscriptionoptions
/*  21:    */   extends HttpServlet
/*  22:    */   implements RequestDispatcher
/*  23:    */ {
/*  24:    */   public static final String ACTIVATE_SUBSCRIPTION = "act";
/*  25:    */   public static final String DEACTIVATE_SUBSCRIPTION = "deact";
/*  26:    */   
/*  27:    */   public void init()
/*  28:    */     throws ServletException
/*  29:    */   {}
/*  30:    */   
/*  31:    */   public void doGet(HttpServletRequest request, HttpServletResponse response)
/*  32:    */     throws ServletException, IOException
/*  33:    */   {
/*  34: 42 */     PrintWriter out = response.getWriter();
/*  35: 43 */     CPConnections cnxn = new CPConnections();
/*  36:    */     
/*  37: 45 */     String ACK = (String)request.getAttribute("ack");
/*  38: 46 */     String kw = request.getParameter("keyword");
/*  39: 47 */     String msg = request.getParameter("msg");
/*  40: 48 */     String msisdn = request.getParameter("msisdn");
/*  41: 49 */     String provId = (String)request.getAttribute("acctId");
/*  42:    */     
/*  43: 51 */     String siteType = (String)request.getAttribute("site_type");
/*  44: 52 */     String lang = (String)request.getAttribute("default_lang");
/*  45: 53 */     if ((lang == null) || (lang.equals(""))) {
/*  46: 54 */       lang = "en";
/*  47:    */     }
/*  48: 56 */     Feedback feedback = (Feedback)getServletContext().getAttribute("feedback_" + lang);
/*  49: 57 */     if (feedback == null) {
/*  50:    */       try
/*  51:    */       {
/*  52: 59 */         feedback = new Feedback();
/*  53:    */       }
/*  54:    */       catch (Exception e) {}
/*  55:    */     }
/*  56: 64 */     String provName = "";
/*  57: 65 */     String message = "";
/*  58: 67 */     if ((provId == null) || (provId.equals("")))
/*  59:    */     {
/*  60:    */       try
/*  61:    */       {
/*  62: 69 */         if (siteType.equals("2")) {
/*  63: 70 */           message = feedback.getUserFriendlyDescription("10004");
/*  64:    */         } else {
/*  65: 72 */           message = feedback.formDefaultMessage("10004");
/*  66:    */         }
/*  67:    */       }
/*  68:    */       catch (Exception ex)
/*  69:    */       {
/*  70: 75 */         message = ex.getMessage();
/*  71:    */       }
/*  72: 77 */       out.println(message);
/*  73: 78 */       return;
/*  74:    */     }
/*  75: 81 */     if ((msisdn == null) || (msisdn.equals("")))
/*  76:    */     {
/*  77:    */       try
/*  78:    */       {
/*  79: 83 */         if (siteType.equals("2")) {
/*  80: 84 */           message = feedback.getUserFriendlyDescription("2000");
/*  81:    */         } else {
/*  82: 86 */           message = feedback.formDefaultMessage("2000");
/*  83:    */         }
/*  84:    */       }
/*  85:    */       catch (Exception ex)
/*  86:    */       {
/*  87: 89 */         message = ex.getMessage();
/*  88:    */       }
/*  89: 91 */       out.println(message);
/*  90: 92 */       return;
/*  91:    */     }
/*  92: 95 */     if ((msg == null) || (msg.equals(""))) {
/*  93: 96 */       msg = "";
/*  94:    */     }
/*  95: 99 */     if ((ACK == null) || (ACK.equals(""))) {
/*  96:100 */       ACK = "";
/*  97:    */     }
/*  98:104 */     System.out.println("Received request to update subscription");
/*  99:105 */     System.out.println("Date received: " + Calendar.getInstance().getTime().toString());
/* 100:106 */     System.out.println("Keyword: " + kw);
/* 101:107 */     System.out.println("Message: " + msg);
/* 102:108 */     System.out.println("Subscriber's number: " + msisdn);
/* 103:109 */     System.out.println("Service privoder's ID: " + provId);
/* 104:    */     
/* 105:    */ 
/* 106:    */ 
/* 107:113 */     String number = msisdn;
/* 108:114 */     if (number.indexOf("+") != -1)
/* 109:    */     {
/* 110:115 */       StringBuffer sb = new StringBuffer(number);
/* 111:116 */       sb.deleteCharAt(number.indexOf("+"));
/* 112:117 */       number = sb.toString();
/* 113:    */       
/* 114:119 */       sb = null;
/* 115:    */     }
/* 116:121 */     number = number.trim();
/* 117:    */     try
/* 118:    */     {
/* 119:125 */       Long.parseLong(number);
/* 120:126 */       msisdn = "+" + number;
/* 121:    */     }
/* 122:    */     catch (NumberFormatException e)
/* 123:    */     {
/* 124:    */       try
/* 125:    */       {
/* 126:129 */         if (siteType.equals("2")) {
/* 127:130 */           message = feedback.getUserFriendlyDescription("2000");
/* 128:    */         } else {
/* 129:132 */           message = feedback.formDefaultMessage("2000");
/* 130:    */         }
/* 131:    */       }
/* 132:    */       catch (Exception ex)
/* 133:    */       {
/* 134:135 */         message = ex.getMessage();
/* 135:    */       }
/* 136:137 */       out.println(message);
/* 137:138 */       return;
/* 138:    */     }
/* 139:    */     try
/* 140:    */     {
/* 141:144 */       cnxn = CPConnections.getConnection(provId, msisdn);
/* 142:    */     }
/* 143:    */     catch (Exception e)
/* 144:    */     {
/* 145:    */       try
/* 146:    */       {
/* 147:147 */         if (siteType.equals("2")) {
/* 148:148 */           message = feedback.getUserFriendlyDescription("8002");
/* 149:    */         } else {
/* 150:150 */           message = feedback.formDefaultMessage("8002");
/* 151:    */         }
/* 152:    */       }
/* 153:    */       catch (Exception ex)
/* 154:    */       {
/* 155:153 */         message = ex.getMessage();
/* 156:    */       }
/* 157:155 */       out.println(message);
/* 158:156 */       return;
/* 159:    */     }
/* 160:159 */     String resp = ACK;
/* 161:161 */     if ((msg != null) && (!msg.equals("")))
/* 162:    */     {
/* 163:162 */       String subkw = msg.split(" ")[0];
/* 164:163 */       String opt = msg.split(" ")[1];
/* 165:164 */       int action = 2;
/* 166:165 */       if (opt.equalsIgnoreCase("act")) {
/* 167:166 */         action = 1;
/* 168:167 */       } else if (opt.equalsIgnoreCase("deact")) {
/* 169:168 */         action = 0;
/* 170:    */       }
/* 171:171 */       if (action != 2)
/* 172:    */       {
/* 173:    */         try
/* 174:    */         {
/* 175:173 */           ServiceManager.updateSubscriptionStatus(msisdn, subkw, provId, action);
/* 176:174 */           System.out.println("Update completed");
/* 177:    */         }
/* 178:    */         catch (Exception e)
/* 179:    */         {
/* 180:176 */           System.out.println("Update NOT completed");
/* 181:    */           try
/* 182:    */           {
/* 183:178 */             if (siteType.equals("2")) {
/* 184:179 */               message = feedback.getUserFriendlyDescription(e.getMessage());
/* 185:    */             } else {
/* 186:181 */               message = feedback.formDefaultMessage(e.getMessage());
/* 187:    */             }
/* 188:    */           }
/* 189:    */           catch (Exception ex)
/* 190:    */           {
/* 191:184 */             message = ex.getMessage();
/* 192:    */           }
/* 193:186 */           out.println(message);
/* 194:187 */           return;
/* 195:    */         }
/* 196:    */       }
/* 197:    */       else
/* 198:    */       {
/* 199:190 */         System.out.println("Update NOT completed");
/* 200:    */         try
/* 201:    */         {
/* 202:192 */           if (siteType.equals("2")) {
/* 203:193 */             message = feedback.getUserFriendlyDescription("6001");
/* 204:    */           } else {
/* 205:195 */             message = feedback.formDefaultMessage("6001");
/* 206:    */           }
/* 207:    */         }
/* 208:    */         catch (Exception ex)
/* 209:    */         {
/* 210:198 */           message = ex.getMessage();
/* 211:    */         }
/* 212:200 */         resp = message;
/* 213:    */       }
/* 214:    */     }
/* 215:    */     else
/* 216:    */     {
/* 217:203 */       System.out.println("Update NOT completed");
/* 218:    */       try
/* 219:    */       {
/* 220:205 */         if (siteType.equals("2")) {
/* 221:206 */           message = feedback.getUserFriendlyDescription("6001");
/* 222:    */         } else {
/* 223:208 */           message = feedback.formDefaultMessage("6001");
/* 224:    */         }
/* 225:    */       }
/* 226:    */       catch (Exception ex)
/* 227:    */       {
/* 228:211 */         message = ex.getMessage();
/* 229:    */       }
/* 230:213 */       resp = message;
/* 231:    */     }
/* 232:216 */     out.println(resp);
/* 233:217 */     cnxn = null;
/* 234:    */   }
/* 235:    */   
/* 236:    */   public void doPost(HttpServletRequest request, HttpServletResponse response)
/* 237:    */     throws ServletException, IOException
/* 238:    */   {
/* 239:224 */     doGet(request, response);
/* 240:    */   }
/* 241:    */   
/* 242:    */   public void destroy() {}
/* 243:    */   
/* 244:    */   public void forward(ServletRequest request, ServletResponse response)
/* 245:    */     throws ServletException, IOException
/* 246:    */   {
/* 247:233 */     HttpServletRequest req = (HttpServletRequest)request;
/* 248:234 */     HttpServletResponse resp = (HttpServletResponse)response;
/* 249:235 */     doGet(req, resp);
/* 250:    */   }
/* 251:    */   
/* 252:    */   public void include(ServletRequest request, ServletResponse response)
/* 253:    */     throws ServletException, IOException
/* 254:    */   {
/* 255:240 */     HttpServletRequest req = (HttpServletRequest)request;
/* 256:241 */     HttpServletResponse resp = (HttpServletResponse)response;
/* 257:242 */     doGet(req, resp);
/* 258:    */   }
/* 259:    */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.serviceinterfaces.processsubscriptionoptions
 * JD-Core Version:    0.7.0.1
 */