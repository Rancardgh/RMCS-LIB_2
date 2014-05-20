/*   1:    */ package com.rancard.mobility.infoserver.livescore.serviceinterfaces;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.PrintWriter;
/*   5:    */ import javax.servlet.RequestDispatcher;
/*   6:    */ import javax.servlet.ServletException;
/*   7:    */ import javax.servlet.ServletRequest;
/*   8:    */ import javax.servlet.ServletResponse;
/*   9:    */ import javax.servlet.http.HttpServlet;
/*  10:    */ import javax.servlet.http.HttpServletRequest;
/*  11:    */ import javax.servlet.http.HttpServletResponse;
/*  12:    */ 
/*  13:    */ public class livescore
/*  14:    */   extends HttpServlet
/*  15:    */   implements RequestDispatcher
/*  16:    */ {
/*  17:    */   public void init()
/*  18:    */     throws ServletException
/*  19:    */   {}
/*  20:    */   
/*  21:    */   public void doGet(HttpServletRequest request, HttpServletResponse response)
/*  22:    */     throws ServletException, IOException
/*  23:    */   {
/*  24: 34 */     PrintWriter out = response.getWriter();
/*  25: 35 */     String command = (String)request.getAttribute("cmd");
/*  26:    */     
/*  27:    */ 
/*  28:    */ 
/*  29: 39 */     RequestDispatcher dispatch = null;
/*  30: 41 */     if ((command != null) && (command.equals("1")))
/*  31:    */     {
/*  32:    */       try
/*  33:    */       {
/*  34: 43 */         dispatch = request.getRequestDispatcher("/livescore/query_leagues.jsp");
/*  35:    */       }
/*  36:    */       catch (Exception e)
/*  37:    */       {
/*  38: 45 */         out.println("8000".substring("8000".indexOf(":") + 1));
/*  39: 46 */         return;
/*  40:    */       }
/*  41: 49 */       dispatch.include(request, response);
/*  42:    */     }
/*  43: 50 */     else if ((command != null) && (command.equals("2")))
/*  44:    */     {
/*  45:    */       try
/*  46:    */       {
/*  47: 52 */         dispatch = request.getRequestDispatcher("leaguelevelquery");
/*  48:    */       }
/*  49:    */       catch (Exception e)
/*  50:    */       {
/*  51: 54 */         out.println("8000".substring("8000".indexOf(":") + 1));
/*  52: 55 */         return;
/*  53:    */       }
/*  54: 58 */       dispatch.include(request, response);
/*  55:    */     }
/*  56: 59 */     else if ((command != null) && (command.equals("3")))
/*  57:    */     {
/*  58:    */       try
/*  59:    */       {
/*  60: 61 */         dispatch = request.getRequestDispatcher("gamesubscription");
/*  61:    */       }
/*  62:    */       catch (Exception e)
/*  63:    */       {
/*  64: 63 */         out.println("8000".substring("8000".indexOf(":") + 1));
/*  65: 64 */         return;
/*  66:    */       }
/*  67: 67 */       dispatch.include(request, response);
/*  68:    */     }
/*  69: 68 */     else if ((command != null) && (command.equals("4")))
/*  70:    */     {
/*  71:    */       try
/*  72:    */       {
/*  73: 70 */         dispatch = request.getRequestDispatcher("gamelevelquery");
/*  74:    */       }
/*  75:    */       catch (Exception e)
/*  76:    */       {
/*  77: 72 */         out.println("8000".substring("8000".indexOf(":") + 1));
/*  78: 73 */         return;
/*  79:    */       }
/*  80: 76 */       dispatch.include(request, response);
/*  81:    */     }
/*  82:    */     else
/*  83:    */     {
/*  84: 78 */       out.println("Command not found");
/*  85:    */     }
/*  86:    */   }
/*  87:    */   
/*  88:    */   public void doPost(HttpServletRequest request, HttpServletResponse response)
/*  89:    */     throws ServletException, IOException
/*  90:    */   {
/*  91: 86 */     doGet(request, response);
/*  92:    */   }
/*  93:    */   
/*  94:    */   public void destroy() {}
/*  95:    */   
/*  96:    */   public void forward(ServletRequest request, ServletResponse response)
/*  97:    */     throws ServletException, IOException
/*  98:    */   {
/*  99: 95 */     HttpServletRequest req = (HttpServletRequest)request;
/* 100: 96 */     HttpServletResponse resp = (HttpServletResponse)response;
/* 101: 97 */     doGet(req, resp);
/* 102:    */   }
/* 103:    */   
/* 104:    */   public void include(ServletRequest request, ServletResponse response)
/* 105:    */     throws ServletException, IOException
/* 106:    */   {
/* 107:102 */     HttpServletRequest req = (HttpServletRequest)request;
/* 108:103 */     HttpServletResponse resp = (HttpServletResponse)response;
/* 109:104 */     doGet(req, resp);
/* 110:    */   }
/* 111:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.infoserver.livescore.serviceinterfaces.livescore
 * JD-Core Version:    0.7.0.1
 */