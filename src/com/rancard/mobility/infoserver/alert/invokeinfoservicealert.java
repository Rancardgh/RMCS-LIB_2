/*  1:   */ package com.rancard.mobility.infoserver.alert;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.io.PrintWriter;
/*  5:   */ import javax.servlet.ServletException;
/*  6:   */ import javax.servlet.http.HttpServlet;
/*  7:   */ import javax.servlet.http.HttpServletRequest;
/*  8:   */ import javax.servlet.http.HttpServletResponse;
/*  9:   */ 
/* 10:   */ public class invokeinfoservicealert
/* 11:   */   extends HttpServlet
/* 12:   */ {
/* 13:   */   protected void processRequest(HttpServletRequest request, HttpServletResponse response)
/* 14:   */     throws ServletException, IOException
/* 15:   */   {
/* 16:28 */     response.setContentType("text/html;charset=UTF-8");
/* 17:29 */     PrintWriter out = response.getWriter();
/* 18:   */     
/* 19:   */ 
/* 20:   */ 
/* 21:   */ 
/* 22:   */ 
/* 23:   */ 
/* 24:   */ 
/* 25:   */ 
/* 26:   */ 
/* 27:   */ 
/* 28:40 */     out.close();
/* 29:   */   }
/* 30:   */   
/* 31:   */   protected void doGet(HttpServletRequest request, HttpServletResponse response)
/* 32:   */     throws ServletException, IOException
/* 33:   */   {
/* 34:50 */     processRequest(request, response);
/* 35:   */   }
/* 36:   */   
/* 37:   */   protected void doPost(HttpServletRequest request, HttpServletResponse response)
/* 38:   */     throws ServletException, IOException
/* 39:   */   {
/* 40:59 */     processRequest(request, response);
/* 41:   */   }
/* 42:   */   
/* 43:   */   public String getServletInfo()
/* 44:   */   {
/* 45:65 */     return "Short description";
/* 46:   */   }
/* 47:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.infoserver.alert.invokeinfoservicealert
 * JD-Core Version:    0.7.0.1
 */