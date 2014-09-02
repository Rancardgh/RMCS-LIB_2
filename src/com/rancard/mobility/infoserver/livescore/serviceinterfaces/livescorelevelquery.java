/*   1:    */ package com.rancard.mobility.infoserver.livescore.serviceinterfaces;
/*   2:    */ 
/*   3:    */ import com.rancard.mobility.common.Driver;
/*   4:    */ import com.rancard.mobility.common.PushDriver;
/*   5:    */ import com.rancard.mobility.contentserver.CPConnections;
/*   6:    */ import com.rancard.mobility.infoserver.livescore.LiveScoreFixture;
/*   7:    */ import com.rancard.mobility.infoserver.livescore.LiveScoreServiceManager;
/*   8:    */ import java.io.IOException;
/*   9:    */ import java.io.PrintStream;
/*  10:    */ import java.io.PrintWriter;
/*  11:    */ import java.sql.Timestamp;
/*  12:    */ import java.util.ArrayList;
/*  13:    */ import java.util.Calendar;
/*  14:    */ import java.util.HashMap;
/*  15:    */ import java.util.Set;
/*  16:    */ import javax.servlet.RequestDispatcher;
/*  17:    */ import javax.servlet.ServletException;
/*  18:    */ import javax.servlet.ServletRequest;
/*  19:    */ import javax.servlet.ServletResponse;
/*  20:    */ import javax.servlet.http.HttpServlet;
/*  21:    */ import javax.servlet.http.HttpServletRequest;
/*  22:    */ import javax.servlet.http.HttpServletResponse;
/*  23:    */ 
/*  24:    */ public class livescorelevelquery
/*  25:    */   extends HttpServlet
/*  26:    */   implements RequestDispatcher
/*  27:    */ {
/*  28:    */   public void init()
/*  29:    */     throws ServletException
/*  30:    */   {}
/*  31:    */   
/*  32:    */   public void doGet(HttpServletRequest request, HttpServletResponse response)
/*  33:    */     throws ServletException, IOException
/*  34:    */   {
/*  35: 43 */     PrintWriter out = response.getWriter();
/*  36:    */     
/*  37:    */ 
/*  38: 46 */     String subscriber = request.getParameter("msisdn");
/*  39: 47 */     String command = request.getParameter("msg");
/*  40: 48 */     String accountId = (String)request.getAttribute("acctId");
/*  41: 49 */     String keyword = request.getParameter("keyword");
/*  42: 50 */     String ack = (String)request.getAttribute("ack");
/*  43: 51 */     if (ack == null) {
/*  44: 51 */       ack = "Your request is being processed";
/*  45:    */     }
/*  46: 53 */     CPConnections cnxn = new CPConnections();
/*  47: 54 */     String from = "406";
/*  48: 55 */     System.out.println("Livescore-level query");
/*  49: 56 */     System.out.println("command: " + command);
/*  50:    */     try
/*  51:    */     {
/*  52: 60 */       if ((subscriber == null) || (subscriber.equals(""))) {
/*  53: 61 */         throw new Exception("2000");
/*  54:    */       }
/*  55: 63 */       if ((accountId == null) || (accountId.equals(""))) {
/*  56: 64 */         throw new Exception("10004");
/*  57:    */       }
/*  58: 66 */       if ((keyword == null) || (keyword.equals(""))) {
/*  59: 67 */         throw new Exception("10001");
/*  60:    */       }
/*  61:    */       try
/*  62:    */       {
/*  63: 71 */         cnxn = CPConnections.getConnection(accountId, subscriber);
/*  64:    */       }
/*  65:    */       catch (Exception e)
/*  66:    */       {
/*  67: 73 */         throw new Exception("8002");
/*  68:    */       }
/*  69: 76 */       String message = "";
/*  70: 79 */       if ((command != null) && (command.equalsIgnoreCase("l")))
/*  71:    */       {
/*  72: 80 */         out.println(ack);
/*  73:    */         
/*  74: 82 */         String[][] leagues = LiveScoreServiceManager.getAvailableLiveScoreServices(accountId);
/*  75: 83 */         for (int i = 0; i < leagues.length; i++)
/*  76:    */         {
/*  77: 84 */           message = "Send START " + leagues[i][0].toUpperCase() + " to subscribe to updates for " + leagues[i][1] + ".";
/*  78:    */           try
/*  79:    */           {
/*  80: 86 */             Driver.getDriver(cnxn.getDriverType(), cnxn.getGatewayURL()).sendSMSTextMessage(subscriber, from, message, cnxn.getUsername(), cnxn.getPassword(), cnxn.getConnection(), "", "0");
/*  81:    */           }
/*  82:    */           catch (Exception e)
/*  83:    */           {
/*  84: 89 */             System.out.println("5002");
/*  85: 90 */             throw new Exception("5002");
/*  86:    */           }
/*  87:    */         }
/*  88:    */       }
/*  89: 93 */       else if ((command != null) && (command.equalsIgnoreCase("g")))
/*  90:    */       {
/*  91: 94 */         out.println(ack);
/*  92:    */         
/*  93: 96 */         Calendar calendar = Calendar.getInstance();
/*  94: 97 */         java.sql.Date today = new java.sql.Date(calendar.getTime().getTime());
/*  95: 98 */         String dateString = today.toString();
/*  96:    */         
/*  97:100 */         HashMap fixtures = LiveScoreServiceManager.viewAllActiveFixturesForDate(dateString);
/*  98:101 */         Object[] keys = fixtures.keySet().toArray();
/*  99:102 */         for (int i = 0; i < keys.length; i++)
/* 100:    */         {
/* 101:103 */           String key = (String)keys[i];
/* 102:104 */           ArrayList fixturesList = (ArrayList)fixtures.get(key);
/* 103:105 */           if ((fixturesList != null) && (fixturesList.size() != 0)) {
/* 104:106 */             for (int k = 0; k < fixturesList.size(); k++)
/* 105:    */             {
/* 106:107 */               LiveScoreFixture game = (LiveScoreFixture)fixturesList.get(k);
/* 107:108 */               message = "Send OK " + game.getGameId() + " to 406 for live updates for " + game.getHomeTeam() + " vs " + game.getAwayTeam() + " on " + new java.util.Date(Timestamp.valueOf(game.getDate()).getTime()).toString();
/* 108:    */               try
/* 109:    */               {
/* 110:112 */                 Driver.getDriver(cnxn.getDriverType(), cnxn.getGatewayURL()).sendSMSTextMessage(subscriber, from, message, cnxn.getUsername(), cnxn.getPassword(), cnxn.getConnection(), "", "0");
/* 111:    */               }
/* 112:    */               catch (Exception e)
/* 113:    */               {
/* 114:115 */                 System.out.println("5002");
/* 115:116 */                 throw new Exception("5002");
/* 116:    */               }
/* 117:    */             }
/* 118:    */           }
/* 119:    */         }
/* 120:121 */         today = null;
/* 121:122 */         fixtures = null;
/* 122:    */       }
/* 123:    */       else
/* 124:    */       {
/* 125:125 */         out.println("Send LS L for available leagues.\r\nSend LS G for all games playing today.");
/* 126:    */       }
/* 127:    */     }
/* 128:    */     catch (Exception e)
/* 129:    */     {
/* 130:128 */       out.println(e.getMessage().substring(e.getMessage().indexOf(":") + 1));
/* 131:    */     }
/* 132:    */     finally
/* 133:    */     {
/* 134:130 */       cnxn = null;
/* 135:    */     }
/* 136:    */   }
/* 137:    */   
/* 138:    */   public void doPost(HttpServletRequest request, HttpServletResponse response)
/* 139:    */     throws ServletException, IOException
/* 140:    */   {
/* 141:138 */     doGet(request, response);
/* 142:    */   }
/* 143:    */   
/* 144:    */   public void destroy() {}
/* 145:    */   
/* 146:    */   public void forward(ServletRequest request, ServletResponse response)
/* 147:    */     throws ServletException, IOException
/* 148:    */   {
/* 149:147 */     HttpServletRequest req = (HttpServletRequest)request;
/* 150:148 */     HttpServletResponse resp = (HttpServletResponse)response;
/* 151:149 */     doGet(req, resp);
/* 152:    */   }
/* 153:    */   
/* 154:    */   public void include(ServletRequest request, ServletResponse response)
/* 155:    */     throws ServletException, IOException
/* 156:    */   {
/* 157:154 */     HttpServletRequest req = (HttpServletRequest)request;
/* 158:155 */     HttpServletResponse resp = (HttpServletResponse)response;
/* 159:156 */     doGet(req, resp);
/* 160:    */   }
/* 161:    */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.livescore.serviceinterfaces.livescorelevelquery
 * JD-Core Version:    0.7.0.1
 */