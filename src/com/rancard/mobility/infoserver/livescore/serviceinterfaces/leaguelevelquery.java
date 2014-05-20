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
/*  14:    */ import javax.servlet.RequestDispatcher;
/*  15:    */ import javax.servlet.ServletException;
/*  16:    */ import javax.servlet.ServletRequest;
/*  17:    */ import javax.servlet.ServletResponse;
/*  18:    */ import javax.servlet.http.HttpServlet;
/*  19:    */ import javax.servlet.http.HttpServletRequest;
/*  20:    */ import javax.servlet.http.HttpServletResponse;
/*  21:    */ 
/*  22:    */ public class leaguelevelquery
/*  23:    */   extends HttpServlet
/*  24:    */   implements RequestDispatcher
/*  25:    */ {
/*  26:    */   public void init()
/*  27:    */     throws ServletException
/*  28:    */   {}
/*  29:    */   
/*  30:    */   public void doGet(HttpServletRequest request, HttpServletResponse response)
/*  31:    */     throws ServletException, IOException
/*  32:    */   {
/*  33: 43 */     PrintWriter out = response.getWriter();
/*  34:    */     
/*  35: 45 */     String subscriber = request.getParameter("msisdn");
/*  36: 46 */     String team = request.getParameter("msg");
/*  37: 47 */     String accountId = (String)request.getAttribute("acctId");
/*  38: 48 */     String keyword = request.getParameter("keyword");
/*  39: 49 */     String ack = (String)request.getAttribute("ack");
/*  40: 50 */     if (ack == null) {
/*  41: 50 */       ack = "Your request is being processed";
/*  42:    */     }
/*  43: 53 */     Calendar calendar = Calendar.getInstance();
/*  44: 54 */     java.sql.Date today = new java.sql.Date(calendar.getTime().getTime());
/*  45: 55 */     String dateString = today.toString();
/*  46: 56 */     ArrayList fixtures = null;
/*  47:    */     
/*  48: 58 */     CPConnections cnxn = new CPConnections();
/*  49: 59 */     System.out.println("Event notification");
/*  50:    */     try
/*  51:    */     {
/*  52: 63 */       if ((subscriber == null) || (subscriber.equals(""))) {
/*  53: 64 */         throw new Exception("2000");
/*  54:    */       }
/*  55: 66 */       if ((accountId == null) || (accountId.equals(""))) {
/*  56: 67 */         throw new Exception("10004");
/*  57:    */       }
/*  58: 69 */       if ((keyword == null) || (keyword.equals(""))) {
/*  59: 70 */         throw new Exception("10001");
/*  60:    */       }
/*  61: 74 */       if ((team == null) || (team.equals(""))) {
/*  62: 75 */         fixtures = LiveScoreServiceManager.viewAllActiveFixturesInLeague(dateString, keyword, accountId);
/*  63:    */       } else {
/*  64: 77 */         fixtures = LiveScoreServiceManager.viewAllActiveFixturesInLeague(dateString, keyword, accountId, team);
/*  65:    */       }
/*  66:    */       try
/*  67:    */       {
/*  68: 81 */         cnxn = CPConnections.getConnection(accountId, subscriber);
/*  69:    */       }
/*  70:    */       catch (Exception e)
/*  71:    */       {
/*  72: 83 */         throw new Exception("8002");
/*  73:    */       }
/*  74: 86 */       String message = "";
/*  75: 87 */       if ((fixtures != null) && (fixtures.size() != 0))
/*  76:    */       {
/*  77: 88 */         out.println(ack);
/*  78: 89 */         for (int k = 0; k < fixtures.size(); k++)
/*  79:    */         {
/*  80: 90 */           LiveScoreFixture game = (LiveScoreFixture)fixtures.get(k);
/*  81:    */           
/*  82: 92 */           message = "Send OK " + game.getGameId() + " to 406 for live updates for " + game.getHomeTeam() + " vs " + game.getAwayTeam() + " on " + new java.util.Date(Timestamp.valueOf(game.getDate()).getTime()).toString();
/*  83:    */           try
/*  84:    */           {
/*  85: 96 */             Driver.getDriver(cnxn.getDriverType(), cnxn.getGatewayURL()).sendSMSTextMessage(subscriber, "LiveScore", message, cnxn.getUsername(), cnxn.getPassword(), cnxn.getConnection(), "", "0");
/*  86:    */           }
/*  87:    */           catch (Exception e)
/*  88:    */           {
/*  89: 99 */             System.out.println("5002");
/*  90:100 */             throw new Exception("5002");
/*  91:    */           }
/*  92:    */         }
/*  93:    */       }
/*  94:    */       else
/*  95:    */       {
/*  96:104 */         out.println("There are no active games today.");
/*  97:    */       }
/*  98:    */     }
/*  99:    */     catch (Exception e)
/* 100:    */     {
/* 101:107 */       out.println(e.getMessage().substring(e.getMessage().indexOf(":") + 1));
/* 102:    */     }
/* 103:    */     finally
/* 104:    */     {
/* 105:109 */       today = null;
/* 106:110 */       fixtures = null;
/* 107:111 */       cnxn = null;
/* 108:    */     }
/* 109:    */   }
/* 110:    */   
/* 111:    */   public void doPost(HttpServletRequest request, HttpServletResponse response)
/* 112:    */     throws ServletException, IOException
/* 113:    */   {
/* 114:119 */     doGet(request, response);
/* 115:    */   }
/* 116:    */   
/* 117:    */   public void destroy() {}
/* 118:    */   
/* 119:    */   public void forward(ServletRequest request, ServletResponse response)
/* 120:    */     throws ServletException, IOException
/* 121:    */   {
/* 122:128 */     HttpServletRequest req = (HttpServletRequest)request;
/* 123:129 */     HttpServletResponse resp = (HttpServletResponse)response;
/* 124:130 */     doGet(req, resp);
/* 125:    */   }
/* 126:    */   
/* 127:    */   public void include(ServletRequest request, ServletResponse response)
/* 128:    */     throws ServletException, IOException
/* 129:    */   {
/* 130:135 */     HttpServletRequest req = (HttpServletRequest)request;
/* 131:136 */     HttpServletResponse resp = (HttpServletResponse)response;
/* 132:137 */     doGet(req, resp);
/* 133:    */   }
/* 134:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.livescore.serviceinterfaces.leaguelevelquery
 * JD-Core Version:    0.7.0.1
 */