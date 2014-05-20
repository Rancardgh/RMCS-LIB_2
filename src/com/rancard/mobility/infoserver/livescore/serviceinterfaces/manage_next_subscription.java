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
/*  13:    */ import java.util.ArrayList;
/*  14:    */ import java.util.Date;
/*  15:    */ import java.util.HashMap;
/*  16:    */ import java.util.Iterator;
/*  17:    */ import java.util.Set;
/*  18:    */ import javax.servlet.RequestDispatcher;
/*  19:    */ import javax.servlet.ServletException;
/*  20:    */ import javax.servlet.ServletRequest;
/*  21:    */ import javax.servlet.ServletResponse;
/*  22:    */ import javax.servlet.http.HttpServlet;
/*  23:    */ import javax.servlet.http.HttpServletRequest;
/*  24:    */ import javax.servlet.http.HttpServletResponse;
/*  25:    */ 
/*  26:    */ public class manage_next_subscription
/*  27:    */   extends HttpServlet
/*  28:    */   implements RequestDispatcher
/*  29:    */ {
/*  30:    */   public void init()
/*  31:    */     throws ServletException
/*  32:    */   {}
/*  33:    */   
/*  34:    */   public void doGet(HttpServletRequest request, HttpServletResponse response)
/*  35:    */     throws ServletException, IOException
/*  36:    */   {
/*  37: 44 */     System.out.println(new Date() + ":@com.rancard.mobility.infoserver.livescore.serviceinterfaces.manage_next_subscription");
/*  38:    */     
/*  39:    */ 
/*  40: 47 */     int MILLISECS_BETWEEN_TRANSMITS = 0;
/*  41:    */     try
/*  42:    */     {
/*  43: 49 */       String millis = PropertyHolder.getPropsValue("MILLIS_BETWEEN_LS_RE-REG_ATMPT");
/*  44: 50 */       if ((millis != null) && (!millis.equals(""))) {
/*  45: 51 */         MILLISECS_BETWEEN_TRANSMITS = Integer.parseInt(millis);
/*  46:    */       } else {
/*  47: 53 */         MILLISECS_BETWEEN_TRANSMITS = 500;
/*  48:    */       }
/*  49:    */     }
/*  50:    */     catch (Exception e)
/*  51:    */     {
/*  52: 56 */       MILLISECS_BETWEEN_TRANSMITS = 500;
/*  53:    */     }
/*  54: 59 */     Date today = new Date();
/*  55: 60 */     String processId = "";
/*  56:    */     
/*  57: 62 */     System.out.println(new Date() + ":renewing subscription for subscribers on MONTHLY_BILLING...");
/*  58:    */     try
/*  59:    */     {
/*  60: 66 */       CPConnections cnxn = new CPConnections();
/*  61:    */       
/*  62:    */ 
/*  63: 69 */       HashMap accountsReSubKeywordMatrix = ServiceManager.getCPIDsForServiceType("15", "9");
/*  64:    */       
/*  65: 71 */       Iterator iter = accountsReSubKeywordMatrix.keySet().iterator();
/*  66: 72 */       String accountId = "";
/*  67: 73 */       String kw = "";
/*  68: 76 */       while (iter.hasNext())
/*  69:    */       {
/*  70: 78 */         accountId = (String)iter.next();
/*  71: 79 */         kw = (String)accountsReSubKeywordMatrix.get(accountId);
/*  72:    */         
/*  73:    */ 
/*  74:    */ 
/*  75: 83 */         int deactivateStatus = LiveScoreServiceManager.manageNextLivescoreSubscription(today, accountId);
/*  76: 86 */         if (deactivateStatus > 0)
/*  77:    */         {
/*  78: 90 */           String networkPrefix = "";
/*  79:    */           
/*  80:    */ 
/*  81: 93 */           HashMap groupedSubscribers = ServiceManager.viewTempSubscribersGroupByNetworkPrefix(accountId, 0, today);
/*  82: 94 */           Iterator grpSubsItr = groupedSubscribers.keySet().iterator();
/*  83: 96 */           while (grpSubsItr.hasNext())
/*  84:    */           {
/*  85: 98 */             networkPrefix = (String)grpSubsItr.next();
/*  86: 99 */             String[] subscribers = (String[])groupedSubscribers.get(networkPrefix);
/*  87:    */             
/*  88:101 */             System.out.println(new Date() + ":getting connection for network:" + networkPrefix + "...");
/*  89:105 */             if ((subscribers != null) && (subscribers.length > 0))
/*  90:    */             {
/*  91:    */               try
/*  92:    */               {
/*  93:108 */                 cnxn = CPConnections.getConnection(accountId, subscribers[0].toString());
/*  94:    */               }
/*  95:    */               catch (Exception e)
/*  96:    */               {
/*  97:111 */                 throw new Exception("8002");
/*  98:    */               }
/*  99:116 */               String message = "";
/* 100:    */               
/* 101:    */ 
/* 102:119 */               UserService us = new UserService();
/* 103:120 */               String messageTemplate = "";
/* 104:    */               try
/* 105:    */               {
/* 106:122 */                 us = (UserService)ServiceManager.viewAllServices(accountId, "15", "11").get(0);
/* 107:    */               }
/* 108:    */               catch (Exception e) {}
/* 109:125 */               messageTemplate = us.getDefaultMessage();
/* 110:    */               
/* 111:    */ 
/* 112:    */ 
/* 113:    */ 
/* 114:130 */               String from = "";
/* 115:131 */               UserService lsHeadSrvc = new UserService();
/* 116:    */               try
/* 117:    */               {
/* 118:133 */                 lsHeadSrvc = LiveScoreServiceManager.viewHeadLiveScoreService(accountId);
/* 119:134 */                 from = lsHeadSrvc.getKeyword();
/* 120:    */               }
/* 121:    */               catch (Exception e)
/* 122:    */               {
/* 123:136 */                 from = "406";
/* 124:    */               }
/* 125:139 */               System.out.println(new Date() + ":composing resubscription notification message...");
/* 126:    */               
/* 127:141 */               String insertions = "lsHeadSrvcName=" + lsHeadSrvc.getServiceName() + "&subscriptionKw=" + kw + "&shortcode=" + lsHeadSrvc.getKeyword() + "&date=" + today;
/* 128:    */               
/* 129:    */ 
/* 130:144 */               System.out.println(new Date() + ":Message Template:" + messageTemplate);
/* 131:145 */               message = message + URLUTF8Encoder.doMessageEscaping(insertions, messageTemplate) + ". ";
/* 132:    */               
/* 133:147 */               System.out.println(new Date() + ":final message:" + message);
/* 134:    */               try
/* 135:    */               {
/* 136:154 */                 Driver.getDriver(cnxn.getDriverType(), cnxn.getGatewayURL()).sendSMSTextMessage(subscribers, from, message, cnxn.getUsername(), cnxn.getPassword(), cnxn.getConnection(), "", "0");
/* 137:    */               }
/* 138:    */               catch (Exception e)
/* 139:    */               {
/* 140:157 */                 System.out.println(new Date() + ":Feedback.TRANSPORT_ERROR:" + e.getMessage());
/* 141:    */               }
/* 142:161 */               us = null;
/* 143:162 */               lsHeadSrvc = null;
/* 144:    */             }
/* 145:    */             else
/* 146:    */             {
/* 147:165 */               System.out.println(new Date() + ":No subscribers found for " + networkPrefix);
/* 148:    */             }
/* 149:    */           }
/* 150:    */         }
/* 151:    */       }
/* 152:    */     }
/* 153:    */     catch (Exception e)
/* 154:    */     {
/* 155:194 */       System.out.println(new Date() + ":error in Livescore auto monthly re-subscription process:" + e.getMessage());
/* 156:    */     }
/* 157:    */   }
/* 158:    */   
/* 159:    */   public void doPost(HttpServletRequest request, HttpServletResponse response)
/* 160:    */     throws ServletException, IOException
/* 161:    */   {
/* 162:202 */     doGet(request, response);
/* 163:    */   }
/* 164:    */   
/* 165:    */   public void destroy() {}
/* 166:    */   
/* 167:    */   public void forward(ServletRequest request, ServletResponse response)
/* 168:    */     throws ServletException, IOException
/* 169:    */   {
/* 170:211 */     HttpServletRequest req = (HttpServletRequest)request;
/* 171:212 */     HttpServletResponse resp = (HttpServletResponse)response;
/* 172:213 */     doGet(req, resp);
/* 173:    */   }
/* 174:    */   
/* 175:    */   public void include(ServletRequest request, ServletResponse response)
/* 176:    */     throws ServletException, IOException
/* 177:    */   {
/* 178:218 */     HttpServletRequest req = (HttpServletRequest)request;
/* 179:219 */     HttpServletResponse resp = (HttpServletResponse)response;
/* 180:220 */     doGet(req, resp);
/* 181:    */   }
/* 182:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.livescore.serviceinterfaces.manage_next_subscription
 * JD-Core Version:    0.7.0.1
 */