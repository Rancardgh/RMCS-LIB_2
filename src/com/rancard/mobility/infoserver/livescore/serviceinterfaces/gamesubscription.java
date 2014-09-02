/*   1:    */ package com.rancard.mobility.infoserver.livescore.serviceinterfaces;
/*   2:    */ 
/*   3:    */ import com.rancard.mobility.contentprovider.User;
/*   4:    */ import com.rancard.mobility.contentserver.BillingManager;
/*   5:    */ import com.rancard.mobility.contentserver.CPConnections;
/*   6:    */ import com.rancard.mobility.infoserver.common.services.ServiceManager;
/*   7:    */ import com.rancard.mobility.infoserver.common.services.UserService;
/*   8:    */ import com.rancard.mobility.infoserver.livescore.LiveScoreFixture;
/*   9:    */ import com.rancard.mobility.infoserver.livescore.LiveScoreServiceManager;
/*  10:    */ import com.rancard.util.URLUTF8Encoder;
/*  11:    */ import java.io.IOException;
/*  12:    */ import java.io.PrintStream;
/*  13:    */ import java.io.PrintWriter;
/*  14:    */ import java.util.ArrayList;
/*  15:    */ import java.util.Calendar;
/*  16:    */ import java.util.Date;
/*  17:    */ import javax.servlet.RequestDispatcher;
/*  18:    */ import javax.servlet.ServletException;
/*  19:    */ import javax.servlet.ServletRequest;
/*  20:    */ import javax.servlet.ServletResponse;
/*  21:    */ import javax.servlet.http.HttpServlet;
/*  22:    */ import javax.servlet.http.HttpServletRequest;
/*  23:    */ import javax.servlet.http.HttpServletResponse;
/*  24:    */ 
/*  25:    */ public class gamesubscription
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
/*  36: 30 */     PrintWriter out = response.getWriter();
/*  37:    */     
/*  38: 32 */     CPConnections cnxn = new CPConnections();
/*  39:    */     
/*  40:    */ 
/*  41: 35 */     String ACK = (String)request.getAttribute("ack");
/*  42: 36 */     String kw = request.getParameter("keyword");
/*  43: 37 */     String msg = request.getParameter("msg");
/*  44: 38 */     String msisdn = request.getParameter("msisdn");
/*  45:    */     
/*  46: 40 */     String provId = (String)request.getAttribute("acctId");
/*  47:    */     
/*  48: 42 */     Date realDate = new Date();
/*  49: 45 */     if ((provId == null) || (provId.equals("")))
/*  50:    */     {
/*  51: 46 */       out.println("10004".substring("10004".indexOf(":") + 1));
/*  52: 47 */       return;
/*  53:    */     }
/*  54: 50 */     if ((msisdn == null) || (msisdn.equals("")))
/*  55:    */     {
/*  56: 51 */       out.println("2000".substring("2000".indexOf(":") + 1));
/*  57: 52 */       return;
/*  58:    */     }
/*  59: 55 */     if ((ACK == null) || (ACK.equals(""))) {
/*  60: 56 */       ACK = "";
/*  61:    */     }
/*  62: 60 */     System.out.println("Received request to subscribe to a service");
/*  63: 61 */     System.out.println("Date received: " + Calendar.getInstance().getTime().toString());
/*  64: 62 */     System.out.println("Keyword: " + kw);
/*  65: 63 */     System.out.println("Message: " + msg);
/*  66: 64 */     System.out.println("Subscriber's number: " + msisdn);
/*  67: 65 */     System.out.println("Service privoder's ID: " + provId);
/*  68:    */     try
/*  69:    */     {
/*  70: 70 */       if (msisdn.indexOf("+") != -1)
/*  71:    */       {
/*  72: 71 */         StringBuffer sb = new StringBuffer(msisdn);
/*  73: 72 */         sb.deleteCharAt(msisdn.indexOf("+"));
/*  74: 73 */         msisdn = sb.toString();
/*  75: 74 */         sb = null;
/*  76:    */       }
/*  77: 76 */       msisdn = msisdn.trim();
/*  78:    */       try
/*  79:    */       {
/*  80: 78 */         Long.parseLong(msisdn);
/*  81: 79 */         msisdn = "+" + msisdn;
/*  82:    */       }
/*  83:    */       catch (NumberFormatException e)
/*  84:    */       {
/*  85: 81 */         throw new Exception("2000".substring("2000".indexOf(":") + 1));
/*  86:    */       }
/*  87:    */     }
/*  88:    */     catch (Exception e)
/*  89:    */     {
/*  90: 84 */       out.println(e.getMessage());
/*  91: 85 */       return;
/*  92:    */     }
/*  93:    */     try
/*  94:    */     {
/*  95: 90 */       String serviceName = "";
/*  96:    */       try
/*  97:    */       {
/*  98: 93 */         cnxn = CPConnections.getConnection(provId, msisdn);
/*  99:    */       }
/* 100:    */       catch (Exception e)
/* 101:    */       {
/* 102: 95 */         throw new Exception("8002");
/* 103:    */       }
/* 104: 98 */       String keyword = "";
/* 105: 99 */       LiveScoreFixture lsf = null;
/* 106:    */       try
/* 107:    */       {
/* 108:101 */         lsf = LiveScoreServiceManager.viewFixture(msg);
/* 109:102 */         keyword = LiveScoreServiceManager.getKeywordForService(lsf.getLeagueId(), provId);
/* 110:103 */         if ((keyword == null) || (keyword.equals(""))) {
/* 111:104 */           throw new Exception("10001");
/* 112:    */         }
/* 113:    */       }
/* 114:    */       catch (Exception e)
/* 115:    */       {
/* 116:107 */         throw new Exception("10001");
/* 117:    */       }
/* 118:110 */       UserService us = new UserService();
/* 119:111 */       if ((msg != null) && (!msg.equals(""))) {
/* 120:    */         try
/* 121:    */         {
/* 122:113 */           us = ServiceManager.viewService(keyword, provId);
/* 123:114 */           serviceName = us.getServiceName();
/* 124:    */         }
/* 125:    */         catch (Exception ex)
/* 126:    */         {
/* 127:116 */           throw new Exception("10001");
/* 128:    */         }
/* 129:    */       }
/* 130:120 */       boolean billed = false;
/* 131:121 */       if (cnxn.getBillingMech().equals("shortcode"))
/* 132:    */       {
/* 133:124 */         System.out.println("do billing using SMSC...");
/* 134:    */         
/* 135:126 */         String messageString = us.getAccountId() + " " + msg;
/* 136:127 */         billed = BillingManager.doShortCodeBilling(us.getServiceType(), cnxn, cnxn.getAllowedNetworks().get(0).toString(), msisdn, messageString, keyword, us.getAccountId());
/* 137:    */       }
/* 138:129 */       else if (!cnxn.getBillingMech().equals("pinredemption")) {}
/* 139:148 */       if (!billed)
/* 140:    */       {
/* 141:150 */         ACK = "Billing process ws not successful. Subscription could not be completed.";
/* 142:151 */         System.out.println("billing ws NOT successful");
/* 143:    */         
/* 144:153 */         throw new Exception("11000");
/* 145:    */       }
/* 146:156 */       String provName = new User().viewDealer(provId).getName();
/* 147:157 */       String insertions = "ack=" + ACK + "&keyword=" + kw + "&msg=" + msg + "&msisdn=" + msisdn + "&provName=" + provName + "&srvcName=" + serviceName;
/* 148:158 */       ACK = URLUTF8Encoder.doMessageEscaping(insertions, ACK);
/* 149:    */       
/* 150:160 */       out.println(ACK);
/* 151:    */       
/* 152:162 */       cnxn = null;
/* 153:163 */       realDate = null;
/* 154:    */     }
/* 155:    */     catch (Exception e)
/* 156:    */     {
/* 157:166 */       out.println(e.getMessage().substring(e.getMessage().indexOf(":") + 1));
/* 158:167 */       return;
/* 159:    */     }
/* 160:    */   }
/* 161:    */   
/* 162:    */   public void doPost(HttpServletRequest request, HttpServletResponse response)
/* 163:    */     throws ServletException, IOException
/* 164:    */   {
/* 165:175 */     doGet(request, response);
/* 166:    */   }
/* 167:    */   
/* 168:    */   public void destroy() {}
/* 169:    */   
/* 170:    */   public void forward(ServletRequest request, ServletResponse response)
/* 171:    */     throws ServletException, IOException
/* 172:    */   {
/* 173:184 */     HttpServletRequest req = (HttpServletRequest)request;
/* 174:185 */     HttpServletResponse resp = (HttpServletResponse)response;
/* 175:186 */     doGet(req, resp);
/* 176:    */   }
/* 177:    */   
/* 178:    */   public void include(ServletRequest request, ServletResponse response)
/* 179:    */     throws ServletException, IOException
/* 180:    */   {
/* 181:191 */     HttpServletRequest req = (HttpServletRequest)request;
/* 182:192 */     HttpServletResponse resp = (HttpServletResponse)response;
/* 183:193 */     doGet(req, resp);
/* 184:    */   }
/* 185:    */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.livescore.serviceinterfaces.gamesubscription
 * JD-Core Version:    0.7.0.1
 */