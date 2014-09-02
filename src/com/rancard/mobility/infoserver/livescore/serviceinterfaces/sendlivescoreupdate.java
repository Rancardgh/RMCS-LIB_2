/*   1:    */ package com.rancard.mobility.infoserver.livescore.serviceinterfaces;
/*   2:    */ 
/*   3:    */ import com.rancard.mobility.common.Driver;
/*   4:    */ import com.rancard.mobility.common.PushDriver;
/*   5:    */ import com.rancard.mobility.contentprovider.User;
/*   6:    */ import com.rancard.mobility.contentserver.CPConnections;
/*   7:    */ import com.rancard.mobility.infoserver.common.services.ServiceManager;
/*   8:    */ import com.rancard.mobility.infoserver.common.services.UserService;
/*   9:    */ import com.rancard.mobility.infoserver.livescore.LiveScoreServiceManager;
/*  10:    */ import com.rancard.mobility.infoserver.livescore.LiveScoreUpdate;
/*  11:    */ import java.io.IOException;
/*  12:    */ import java.io.PrintStream;
/*  13:    */ import java.io.PrintWriter;
/*  14:    */ import java.util.ArrayList;
/*  15:    */ import java.util.Calendar;
/*  16:    */ import java.util.Date;
/*  17:    */ import java.util.HashMap;
/*  18:    */ import java.util.Set;
/*  19:    */ import javax.servlet.RequestDispatcher;
/*  20:    */ import javax.servlet.ServletException;
/*  21:    */ import javax.servlet.ServletRequest;
/*  22:    */ import javax.servlet.ServletResponse;
/*  23:    */ import javax.servlet.http.HttpServlet;
/*  24:    */ import javax.servlet.http.HttpServletRequest;
/*  25:    */ import javax.servlet.http.HttpServletResponse;
/*  26:    */ 
/*  27:    */ public class sendlivescoreupdate
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
/*  38: 43 */     PrintWriter out = response.getWriter();
/*  39: 44 */     CPConnections cnxn = new CPConnections();
/*  40:    */     
/*  41: 46 */     String ui = request.getParameter("updateId");
/*  42: 47 */     String accountId = request.getParameter("acctId");
/*  43:    */     
/*  44: 49 */     String from = "";
/*  45: 50 */     String kw = "";
/*  46: 51 */     String alias = "";
/*  47:    */     
/*  48: 53 */     LiveScoreUpdate update = null;
/*  49:    */     try
/*  50:    */     {
/*  51: 55 */       update = LiveScoreServiceManager.viewUpdate(ui);
/*  52:    */       
/*  53:    */ 
/*  54: 58 */       System.out.println("Received Update with ID " + update.getUpdateId());
/*  55:    */     }
/*  56:    */     catch (Exception e)
/*  57:    */     {
/*  58: 60 */       out.println("10001".substring("10001".indexOf(":") + 1));
/*  59: 61 */       return;
/*  60:    */     }
/*  61: 64 */     kw = update.getEventId();
/*  62:    */     try
/*  63:    */     {
/*  64: 66 */       alias = LiveScoreServiceManager.viewAliasForGameId(kw);
/*  65:    */     }
/*  66:    */     catch (Exception e) {}
/*  67: 70 */     if ((kw == null) || (kw.equals("")))
/*  68:    */     {
/*  69: 71 */       out.println("10001".substring("10001".indexOf(":") + 1));
/*  70: 72 */       return;
/*  71:    */     }
/*  72: 75 */     User prov = null;
/*  73: 76 */     String lang = "";
/*  74:    */     try
/*  75:    */     {
/*  76: 78 */       prov = new User().viewDealer(accountId);
/*  77: 79 */       lang = prov.getDefaultLanguage();
/*  78:    */     }
/*  79:    */     catch (Exception e)
/*  80:    */     {
/*  81: 81 */       lang = "en";
/*  82:    */     }
/*  83: 84 */     UserService lsHeadSrvc = new UserService();
/*  84:    */     try
/*  85:    */     {
/*  86: 86 */       lsHeadSrvc = LiveScoreServiceManager.viewHeadLiveScoreService(accountId);
/*  87: 87 */       from = lsHeadSrvc.getServiceName();
/*  88:    */     }
/*  89:    */     catch (Exception e)
/*  90:    */     {
/*  91: 89 */       from = "LiveScore";
/*  92:    */     }
/*  93: 93 */     System.out.println("Info to be sent for keyword: " + kw + " on account with ID: " + accountId);
/*  94: 94 */     System.out.println("Date received: " + Calendar.getInstance().getTime().toString());
/*  95:    */     
/*  96:    */ 
/*  97: 97 */     String gameUpdate = "";
/*  98: 98 */     if (lang.equalsIgnoreCase("fr")) {
/*  99: 99 */       gameUpdate = update.getFrenchMessage();
/* 100:    */     } else {
/* 101:101 */       gameUpdate = update.getEnglishMessage();
/* 102:    */     }
/* 103:    */     try
/* 104:    */     {
/* 105:105 */       System.out.println("Info: " + gameUpdate);
/* 106:106 */       HashMap groupedSubscribers = ServiceManager.viewSubscribersGroupByNetworkPrefix(accountId, kw, alias, 1);
/* 107:107 */       Object[] keys = groupedSubscribers.keySet().toArray();
/* 108:109 */       for (int j = 0; j < keys.length; j++)
/* 109:    */       {
/* 110:110 */         String network = keys[j].toString();
/* 111:111 */         String[] subscribers = (String[])groupedSubscribers.get(network);
/* 112:112 */         if ((subscribers != null) && (subscribers.length > 0))
/* 113:    */         {
/* 114:    */           try
/* 115:    */           {
/* 116:115 */             cnxn = CPConnections.getConnection(accountId, subscribers[0]);
/* 117:116 */             System.out.println("Number of subscribers from network " + cnxn.getAllowedNetworks().get(0).toString() + " : " + subscribers.length);
/* 118:    */           }
/* 119:    */           catch (Exception e)
/* 120:    */           {
/* 121:118 */             System.out.println("Error getting connection: " + e.getMessage());
/* 122:119 */             throw new Exception("8002");
/* 123:    */           }
/* 124:    */           try
/* 125:    */           {
/* 126:123 */             System.out.println("sending...");
/* 127:124 */             Driver.getDriver(cnxn.getDriverType(), cnxn.getGatewayURL()).sendSMSTextMessage(subscribers, from, gameUpdate, cnxn.getUsername(), cnxn.getPassword(), cnxn.getConnection(), "", "0");
/* 128:    */           }
/* 129:    */           catch (Exception e)
/* 130:    */           {
/* 131:127 */             System.out.println("5002 Response from gateway: " + e.getMessage());
/* 132:128 */             throw new Exception("5002");
/* 133:    */           }
/* 134:    */         }
/* 135:    */         else
/* 136:    */         {
/* 137:131 */           System.out.println("No subscribers found");
/* 138:    */         }
/* 139:    */       }
/* 140:    */     }
/* 141:    */     catch (Exception e)
/* 142:    */     {
/* 143:135 */       System.out.println("ERROR!! SENDING UPDATES FAILED: " + e.getMessage());
/* 144:136 */       out.println(e.getMessage().substring(e.getMessage().indexOf(":") + 1));
/* 145:137 */       return;
/* 146:    */     }
/* 147:    */   }
/* 148:    */   
/* 149:    */   public void doPost(HttpServletRequest request, HttpServletResponse response)
/* 150:    */     throws ServletException, IOException
/* 151:    */   {
/* 152:145 */     doGet(request, response);
/* 153:    */   }
/* 154:    */   
/* 155:    */   public void destroy() {}
/* 156:    */   
/* 157:    */   public void forward(ServletRequest request, ServletResponse response)
/* 158:    */     throws ServletException, IOException
/* 159:    */   {
/* 160:154 */     HttpServletRequest req = (HttpServletRequest)request;
/* 161:155 */     HttpServletResponse resp = (HttpServletResponse)response;
/* 162:156 */     doGet(req, resp);
/* 163:    */   }
/* 164:    */   
/* 165:    */   public void include(ServletRequest request, ServletResponse response)
/* 166:    */     throws ServletException, IOException
/* 167:    */   {
/* 168:161 */     HttpServletRequest req = (HttpServletRequest)request;
/* 169:162 */     HttpServletResponse resp = (HttpServletResponse)response;
/* 170:163 */     doGet(req, resp);
/* 171:    */   }
/* 172:    */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.livescore.serviceinterfaces.sendlivescoreupdate
 * JD-Core Version:    0.7.0.1
 */