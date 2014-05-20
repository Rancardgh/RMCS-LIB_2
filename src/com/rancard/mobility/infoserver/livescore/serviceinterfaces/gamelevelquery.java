/*   1:    */ package com.rancard.mobility.infoserver.livescore.serviceinterfaces;
/*   2:    */ 
/*   3:    */ import com.rancard.mobility.contentserver.CPConnections;
/*   4:    */ import com.rancard.mobility.infoserver.livescore.LiveScoreFixture;
/*   5:    */ import com.rancard.mobility.infoserver.livescore.LiveScoreServiceManager;
/*   6:    */ import java.io.IOException;
/*   7:    */ import java.io.PrintWriter;
/*   8:    */ import javax.servlet.RequestDispatcher;
/*   9:    */ import javax.servlet.ServletException;
/*  10:    */ import javax.servlet.ServletRequest;
/*  11:    */ import javax.servlet.ServletResponse;
/*  12:    */ import javax.servlet.http.HttpServlet;
/*  13:    */ import javax.servlet.http.HttpServletRequest;
/*  14:    */ import javax.servlet.http.HttpServletResponse;
/*  15:    */ 
/*  16:    */ public class gamelevelquery
/*  17:    */   extends HttpServlet
/*  18:    */   implements RequestDispatcher
/*  19:    */ {
/*  20:    */   public void init()
/*  21:    */     throws ServletException
/*  22:    */   {}
/*  23:    */   
/*  24:    */   public void doGet(HttpServletRequest request, HttpServletResponse response)
/*  25:    */     throws ServletException, IOException
/*  26:    */   {
/*  27: 43 */     PrintWriter out = response.getWriter();
/*  28:    */     
/*  29:    */ 
/*  30: 46 */     String subscriber = request.getParameter("msisdn");
/*  31: 47 */     String gameId = request.getParameter("msg");
/*  32: 48 */     String accountId = (String)request.getAttribute("acctId");
/*  33: 49 */     String keyword = request.getParameter("keyword");
/*  34: 50 */     String ack = (String)request.getAttribute("ack");
/*  35: 51 */     if (ack == null) {
/*  36: 51 */       ack = "Your request is being processed";
/*  37:    */     }
/*  38: 53 */     CPConnections cnxn = new CPConnections();
/*  39:    */     try
/*  40:    */     {
/*  41: 57 */       if ((subscriber == null) || (subscriber.equals(""))) {
/*  42: 58 */         throw new Exception("2000");
/*  43:    */       }
/*  44: 60 */       if ((accountId == null) || (accountId.equals(""))) {
/*  45: 61 */         throw new Exception("10004");
/*  46:    */       }
/*  47: 63 */       if ((keyword == null) || (keyword.equals(""))) {
/*  48: 64 */         throw new Exception("10001");
/*  49:    */       }
/*  50:    */       try
/*  51:    */       {
/*  52: 68 */         cnxn = CPConnections.getConnection(accountId, subscriber);
/*  53:    */       }
/*  54:    */       catch (Exception e)
/*  55:    */       {
/*  56: 70 */         throw new Exception("8002");
/*  57:    */       }
/*  58: 73 */       LiveScoreFixture game = null;
/*  59:    */       try
/*  60:    */       {
/*  61: 75 */         game = LiveScoreServiceManager.viewFixture(gameId);
/*  62: 76 */         if ((game.getGameId() == null) || (game.getGameId().equals(""))) {
/*  63: 77 */           throw new Exception("Specified game does not exist.");
/*  64:    */         }
/*  65:    */       }
/*  66:    */       catch (Exception e)
/*  67:    */       {
/*  68: 80 */         throw new Exception(ack);
/*  69:    */       }
/*  70: 83 */       String message = "Current score:\r\n " + game.getHomeTeam() + ": " + game.getHomeScore() + "\r\n" + game.getAwayTeam() + ": " + game.getAwayScore();
/*  71:    */       
/*  72:    */ 
/*  73:    */ 
/*  74:    */ 
/*  75:    */ 
/*  76:    */ 
/*  77:    */ 
/*  78: 91 */       out.println(message);
/*  79: 92 */       game = null;
/*  80:    */     }
/*  81:    */     catch (Exception e)
/*  82:    */     {
/*  83: 94 */       out.println(e.getMessage().substring(e.getMessage().indexOf(":") + 1));
/*  84:    */     }
/*  85:    */     finally
/*  86:    */     {
/*  87: 97 */       cnxn = null;
/*  88:    */     }
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void doPost(HttpServletRequest request, HttpServletResponse response)
/*  92:    */     throws ServletException, IOException
/*  93:    */   {
/*  94:105 */     doGet(request, response);
/*  95:    */   }
/*  96:    */   
/*  97:    */   public void destroy() {}
/*  98:    */   
/*  99:    */   public void forward(ServletRequest request, ServletResponse response)
/* 100:    */     throws ServletException, IOException
/* 101:    */   {
/* 102:114 */     HttpServletRequest req = (HttpServletRequest)request;
/* 103:115 */     HttpServletResponse resp = (HttpServletResponse)response;
/* 104:116 */     doGet(req, resp);
/* 105:    */   }
/* 106:    */   
/* 107:    */   public void include(ServletRequest request, ServletResponse response)
/* 108:    */     throws ServletException, IOException
/* 109:    */   {
/* 110:121 */     HttpServletRequest req = (HttpServletRequest)request;
/* 111:122 */     HttpServletResponse resp = (HttpServletResponse)response;
/* 112:123 */     doGet(req, resp);
/* 113:    */   }
/* 114:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.infoserver.livescore.serviceinterfaces.gamelevelquery
 * JD-Core Version:    0.7.0.1
 */