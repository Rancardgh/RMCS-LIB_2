/*  1:   */ package com.rancard.security;
/*  2:   */ 
/*  3:   */ import com.rancard.common.Message;
/*  4:   */ import java.io.IOException;
/*  5:   */ import java.util.ArrayList;
/*  6:   */ import javax.servlet.Filter;
/*  7:   */ import javax.servlet.FilterChain;
/*  8:   */ import javax.servlet.FilterConfig;
/*  9:   */ import javax.servlet.ServletContext;
/* 10:   */ import javax.servlet.ServletException;
/* 11:   */ import javax.servlet.ServletRequest;
/* 12:   */ import javax.servlet.ServletResponse;
/* 13:   */ import javax.servlet.http.HttpServlet;
/* 14:   */ import javax.servlet.http.HttpServletRequest;
/* 15:   */ import javax.servlet.http.HttpServletResponse;
/* 16:   */ import javax.servlet.http.HttpSession;
/* 17:   */ 
/* 18:   */ public class SessionlessSecurityManager
/* 19:   */   extends HttpServlet
/* 20:   */   implements Filter
/* 21:   */ {
/* 22:   */   private FilterConfig filterConfig;
/* 23:   */   String baseUrl;
/* 24:   */   
/* 25:   */   public void init(FilterConfig filterConfig)
/* 26:   */   {
/* 27:22 */     this.filterConfig = filterConfig;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
/* 31:   */   {
/* 32:30 */     ServletContext context = this.filterConfig.getServletContext();
/* 33:   */     
/* 34:32 */     HttpServletRequest request = (HttpServletRequest)req;
/* 35:33 */     HttpServletResponse response = (HttpServletResponse)res;
/* 36:34 */     String s = request.getProtocol().toLowerCase();
/* 37:35 */     s = s.substring(0, s.indexOf("/")).toLowerCase();
/* 38:36 */     String baseUrl = s + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
/* 39:   */     
/* 40:   */ 
/* 41:39 */     HttpSession session = request.getSession(true);
/* 42:   */     
/* 43:41 */     String username = request.getParameter("username");
/* 44:42 */     String responsePath = request.getParameter("responsePath");
/* 45:43 */     String password = request.getParameter("password");
/* 46:44 */     Message reply = new Message();
/* 47:45 */     ArrayList responses = new ArrayList();
/* 48:46 */     boolean canSend = false;
/* 49:47 */     boolean isValidUser = false;
/* 50:52 */     if ((username != null) && (password != null)) {
/* 51:56 */       isValidUser = true;
/* 52:   */     } else {
/* 53:59 */       responses.add("Invalid user name or password");
/* 54:   */     }
/* 55:   */     try
/* 56:   */     {
/* 57:69 */       String requestUri = request.getRequestURI();
/* 58:70 */       if (isValidUser) {
/* 59:71 */         canSend = true;
/* 60:   */       }
/* 61:73 */       if (canSend)
/* 62:   */       {
/* 63:75 */         filterChain.doFilter(request, response);
/* 64:   */       }
/* 65:   */       else
/* 66:   */       {
/* 67:77 */         String messageString = "";
/* 68:78 */         for (int i = 0; i < responses.size(); i++) {
/* 69:79 */           messageString = messageString + responses.get(i).toString() + "<BR>";
/* 70:   */         }
/* 71:83 */         reply.setStatus(false);
/* 72:84 */         reply.setMessage(messageString);
/* 73:86 */         if (responsePath != null) {
/* 74:87 */           response.sendRedirect(responsePath + "?reply=" + reply.getMessage());
/* 75:   */         } else {
/* 76:90 */           response.sendRedirect(baseUrl + "default.jsp?reply=" + reply.getMessage());
/* 77:   */         }
/* 78:   */       }
/* 79:   */     }
/* 80:   */     catch (ServletException sx)
/* 81:   */     {
/* 82:97 */       this.filterConfig.getServletContext().log(sx.getMessage());
/* 83:   */     }
/* 84:   */     catch (IOException iox)
/* 85:   */     {
/* 86:99 */       this.filterConfig.getServletContext().log(iox.getMessage());
/* 87:   */     }
/* 88:   */   }
/* 89:   */   
/* 90:   */   public void destroy() {}
/* 91:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.security.SessionlessSecurityManager
 * JD-Core Version:    0.7.0.1
 */