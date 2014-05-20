/*  1:   */ package com.rancard.mobility.contentserver.serviceinterfaces;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.io.PrintWriter;
/*  5:   */ import javax.servlet.ServletException;
/*  6:   */ import javax.servlet.http.HttpServlet;
/*  7:   */ import javax.servlet.http.HttpServletRequest;
/*  8:   */ import javax.servlet.http.HttpServletResponse;
/*  9:   */ 
/* 10:   */ public class contentlist
/* 11:   */   extends HttpServlet
/* 12:   */ {
/* 13:   */   private static final String CONTENT_TYPE = "application/xml";
/* 14:   */   
/* 15:   */   public void init()
/* 16:   */     throws ServletException
/* 17:   */   {}
/* 18:   */   
/* 19:   */   public void doGet(HttpServletRequest request, HttpServletResponse response)
/* 20:   */     throws ServletException, IOException
/* 21:   */   {
/* 22:18 */     String vaspId = request.getParameter("vasp_id");
/* 23:19 */     if (vaspId == null) {
/* 24:20 */       vaspId = "";
/* 25:   */     }
/* 26:22 */     String category = request.getParameter("category");
/* 27:23 */     if (category == null) {
/* 28:24 */       category = "";
/* 29:   */     }
/* 30:26 */     String type = request.getParameter("type");
/* 31:27 */     if (type == null) {
/* 32:28 */       type = "";
/* 33:   */     }
/* 34:30 */     String keyword = request.getParameter("keyword");
/* 35:31 */     if (keyword == null) {
/* 36:32 */       keyword = "";
/* 37:   */     }
/* 38:34 */     String count = request.getParameter("count");
/* 39:35 */     if (count == null) {
/* 40:36 */       count = "";
/* 41:   */     }
/* 42:38 */     String start = request.getParameter("start");
/* 43:39 */     if (start == null) {
/* 44:40 */       start = "";
/* 45:   */     }
/* 46:42 */     String pageno = request.getParameter("pageno");
/* 47:43 */     if (pageno == null) {
/* 48:44 */       pageno = "";
/* 49:   */     }
/* 50:46 */     response.setContentType("application/xml");
/* 51:47 */     PrintWriter out = response.getWriter();
/* 52:48 */     out.println("<html>");
/* 53:49 */     out.println("<head><title>contentlist</title></head>");
/* 54:50 */     out.println("<body bgcolor=\"#ffffff\">");
/* 55:51 */     out.println("<p>The servlet has received a " + request.getMethod() + ". This is the reply.</p>");
/* 56:   */     
/* 57:53 */     out.println("</body>");
/* 58:54 */     out.println("</html>");
/* 59:55 */     out.close();
/* 60:   */   }
/* 61:   */   
/* 62:   */   public void doPost(HttpServletRequest request, HttpServletResponse response)
/* 63:   */     throws ServletException, IOException
/* 64:   */   {
/* 65:61 */     doGet(request, response);
/* 66:   */   }
/* 67:   */   
/* 68:   */   public void destroy() {}
/* 69:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.contentserver.serviceinterfaces.contentlist
 * JD-Core Version:    0.7.0.1
 */