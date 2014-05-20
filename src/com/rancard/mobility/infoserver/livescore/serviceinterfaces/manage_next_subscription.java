/*   1:    */ package com.rancard.mobility.infoserver.livescore.serviceinterfaces;
/*   2:    */ 
/*   3:    */ import com.rancard.mobility.common.Driver;
/*   4:    */ import com.rancard.mobility.common.PushDriver;
/*   5:    */ import com.rancard.mobility.contentserver.CPConnections;
/*   6:    */ import com.rancard.mobility.infoserver.common.services.ServiceManager;
/*   7:    */ import com.rancard.mobility.infoserver.common.services.UserService;
/*   8:    */ import com.rancard.mobility.infoserver.livescore.LiveScoreServiceManager;
/*   9:    */ import com.rancard.util.PropertyHolder;
/*  10:    */ import com.rancard.util.URLUTF8Encoder;
/*  11:    */ import java.io.IOException;
/*  12:    */ import java.io.PrintStream;
/*  13:    */ import java.util.Date;
/*  14:    */ import java.util.HashMap;
/*  15:    */ import java.util.Iterator;
/*  16:    */ import java.util.List;
/*  17:    */ import java.util.Map;
/*  18:    */ import java.util.Set;
/*  19:    */ import javax.servlet.RequestDispatcher;
/*  20:    */ import javax.servlet.ServletException;
/*  21:    */ import javax.servlet.ServletRequest;
/*  22:    */ import javax.servlet.ServletResponse;
/*  23:    */ import javax.servlet.http.HttpServlet;
/*  24:    */ import javax.servlet.http.HttpServletRequest;
/*  25:    */ import javax.servlet.http.HttpServletResponse;
/*  26:    */ 
/*  27:    */ public class manage_next_subscription
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
/*  38: 44 */     System.out.println(new Date() + ":@com.rancard.mobility.infoserver.livescore.serviceinterfaces.manage_next_subscription");
/*  39:    */     
/*  40:    */ 
/*  41: 47 */     int MILLISECS_BETWEEN_TRANSMITS = 0;
/*  42:    */     try
/*  43:    */     {
/*  44: 49 */       String millis = PropertyHolder.getPropsValue("MILLIS_BETWEEN_LS_RE-REG_ATMPT");
/*  45: 50 */       if ((millis != null) && (!millis.equals(""))) {
/*  46: 51 */         MILLISECS_BETWEEN_TRANSMITS = Integer.parseInt(millis);
/*  47:    */       } else {
/*  48: 53 */         MILLISECS_BETWEEN_TRANSMITS = 500;
/*  49:    */       }
/*  50:    */     }
/*  51:    */     catch (Exception e)
/*  52:    */     {
/*  53: 56 */       MILLISECS_BETWEEN_TRANSMITS = 500;
/*  54:    */     }
/*  55: 59 */     Date today = new Date();
/*  56: 60 */     String processId = "";
/*  57:    */     
/*  58: 62 */     System.out.println(new Date() + ":renewing subscription for subscribers on MONTHLY_BILLING...");
/*  59:    */     try
/*  60:    */     {
/*  61: 66 */       CPConnections cnxn = new CPConnections();
/*  62:    */       
/*  63:    */ 
/*  64: 69 */       Map<String, String> accountsReSubKeywordMatrix = ServiceManager.getCPIDsForServiceType("15", "9");
/*  65:    */       
/*  66: 71 */       Iterator iter = accountsReSubKeywordMatrix.keySet().iterator();
/*  67: 72 */       String accountId = "";
/*  68: 73 */       String kw = "";
/*  69: 76 */       while (iter.hasNext())
/*  70:    */       {
/*  71: 78 */         accountId = (String)iter.next();
/*  72: 79 */         kw = (String)accountsReSubKeywordMatrix.get(accountId);
/*  73:    */         
/*  74:    */ 
/*  75:    */ 
/*  76: 83 */         int deactivateStatus = LiveScoreServiceManager.manageNextLivescoreSubscription(today, accountId);
/*  77: 86 */         if (deactivateStatus > 0)
/*  78:    */         {
/*  79: 90 */           String networkPrefix = "";
/*  80:    */           
/*  81:    */ 
/*  82: 93 */           HashMap groupedSubscribers = ServiceManager.viewTempSubscribersGroupByNetworkPrefix(accountId, 0, today);
/*  83: 94 */           Iterator grpSubsItr = groupedSubscribers.keySet().iterator();
/*  84: 96 */           while (grpSubsItr.hasNext())
/*  85:    */           {
/*  86: 98 */             networkPrefix = (String)grpSubsItr.next();
/*  87: 99 */             String[] subscribers = (String[])groupedSubscribers.get(networkPrefix);
/*  88:    */             
/*  89:101 */             System.out.println(new Date() + ":getting connection for network:" + networkPrefix + "...");
/*  90:105 */             if ((subscribers != null) && (subscribers.length > 0))
/*  91:    */             {
/*  92:    */               try
/*  93:    */               {
/*  94:108 */                 cnxn = CPConnections.getConnection(accountId, subscribers[0].toString());
/*  95:    */               }
/*  96:    */               catch (Exception e)
/*  97:    */               {
/*  98:111 */                 throw new Exception("8002");
/*  99:    */               }
/* 100:116 */               String message = "";
/* 101:    */               
/* 102:    */ 
/* 103:119 */               UserService us = new UserService();
/* 104:120 */               String messageTemplate = "";
/* 105:    */               try
/* 106:    */               {
/* 107:122 */                 us = (UserService)ServiceManager.viewAllServices(accountId, "15", "11").get(0);
/* 108:    */               }
/* 109:    */               catch (Exception e) {}
/* 110:125 */               messageTemplate = us.getDefaultMessage();
/* 111:    */               
/* 112:    */ 
/* 113:    */ 
/* 114:    */ 
/* 115:130 */               String from = "";
/* 116:131 */               UserService lsHeadSrvc = new UserService();
/* 117:    */               try
/* 118:    */               {
/* 119:133 */                 lsHeadSrvc = LiveScoreServiceManager.viewHeadLiveScoreService(accountId);
/* 120:134 */                 from = lsHeadSrvc.getKeyword();
/* 121:    */               }
/* 122:    */               catch (Exception e)
/* 123:    */               {
/* 124:136 */                 from = "406";
/* 125:    */               }
/* 126:139 */               System.out.println(new Date() + ":composing resubscription notification message...");
/* 127:    */               
/* 128:141 */               String insertions = "lsHeadSrvcName=" + lsHeadSrvc.getServiceName() + "&subscriptionKw=" + kw + "&shortcode=" + lsHeadSrvc.getKeyword() + "&date=" + today;
/* 129:    */               
/* 130:    */ 
/* 131:144 */               System.out.println(new Date() + ":Message Template:" + messageTemplate);
/* 132:145 */               message = message + URLUTF8Encoder.doMessageEscaping(insertions, messageTemplate) + ". ";
/* 133:    */               
/* 134:147 */               System.out.println(new Date() + ":final message:" + message);
/* 135:    */               try
/* 136:    */               {
/* 137:154 */                 Driver.getDriver(cnxn.getDriverType(), cnxn.getGatewayURL()).sendSMSTextMessage(subscribers, from, message, cnxn.getUsername(), cnxn.getPassword(), cnxn.getConnection(), "", "0");
/* 138:    */               }
/* 139:    */               catch (Exception e)
/* 140:    */               {
/* 141:157 */                 System.out.println(new Date() + ":Feedback.TRANSPORT_ERROR:" + e.getMessage());
/* 142:    */               }
/* 143:161 */               us = null;
/* 144:162 */               lsHeadSrvc = null;
/* 145:    */             }
/* 146:    */             else
/* 147:    */             {
/* 148:165 */               System.out.println(new Date() + ":No subscribers found for " + networkPrefix);
/* 149:    */             }
/* 150:    */           }
/* 151:    */         }
/* 152:    */       }
/* 153:    */     }
/* 154:    */     catch (Exception e)
/* 155:    */     {
/* 156:194 */       System.out.println(new Date() + ":error in Livescore auto monthly re-subscription process:" + e.getMessage());
/* 157:    */     }
/* 158:    */   }
/* 159:    */   
/* 160:    */   public void doPost(HttpServletRequest request, HttpServletResponse response)
/* 161:    */     throws ServletException, IOException
/* 162:    */   {
/* 163:202 */     doGet(request, response);
/* 164:    */   }
/* 165:    */   
/* 166:    */   public void destroy() {}
/* 167:    */   
/* 168:    */   public void forward(ServletRequest request, ServletResponse response)
/* 169:    */     throws ServletException, IOException
/* 170:    */   {
/* 171:211 */     HttpServletRequest req = (HttpServletRequest)request;
/* 172:212 */     HttpServletResponse resp = (HttpServletResponse)response;
/* 173:213 */     doGet(req, resp);
/* 174:    */   }
/* 175:    */   
/* 176:    */   public void include(ServletRequest request, ServletResponse response)
/* 177:    */     throws ServletException, IOException
/* 178:    */   {
/* 179:218 */     HttpServletRequest req = (HttpServletRequest)request;
/* 180:219 */     HttpServletResponse resp = (HttpServletResponse)response;
/* 181:220 */     doGet(req, resp);
/* 182:    */   }
/* 183:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.infoserver.livescore.serviceinterfaces.manage_next_subscription
 * JD-Core Version:    0.7.0.1
 */