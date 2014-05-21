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
/* 18:   */ public class IPSecurityFilter
/* 19:   */   extends HttpServlet
/* 20:   */   implements Filter
/* 21:   */ {
/* 22:   */   private FilterConfig filterConfig;
/* 23:   */   String baseUrl;
/* 24:   */   
/* 25:   */   public void init(FilterConfig filterConfig)
/* 26:   */     throws ServletException
/* 27:   */   {
/* 28:13 */     this.filterConfig = filterConfig;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
/* 32:   */   {
/* 33:20 */     ServletContext context = this.filterConfig.getServletContext();
/* 34:   */     
/* 35:22 */     HttpServletRequest request = (HttpServletRequest)req;
/* 36:23 */     HttpServletResponse response = (HttpServletResponse)res;
/* 37:24 */     String s = request.getProtocol().toLowerCase();
/* 38:25 */     s = s.substring(0, s.indexOf("/")).toLowerCase();
/* 39:26 */     this.baseUrl = (s + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/");
/* 40:   */     
/* 41:   */ 
/* 42:29 */     HttpSession session = request.getSession(true);
/* 43:   */     
/* 44:   */ 
/* 45:32 */     Message reply = new Message();
/* 46:33 */     ArrayList responses = new ArrayList();
/* 47:34 */     boolean canSend = false;
/* 48:   */     
/* 49:36 */     boolean isValidIP = false;
/* 50:   */     
/* 51:   */ 
/* 52:   */ 
/* 53:40 */     ArrayList allowedIps = new ArrayList();
/* 54:41 */     ArrayList deniedIps = new ArrayList();
/* 55:42 */     String remotehostIP = request.getRemoteHost();
/* 56:43 */     String responsePath = this.filterConfig.getInitParameter("error_page");
/* 57:   */     try
/* 58:   */     {
/* 59:45 */       allowedIps = AccessControlListFactory.viewWhiteList();
/* 60:46 */       deniedIps = AccessControlListFactory.viewBlackList();
/* 61:   */     }
/* 62:   */     catch (Exception ex) {}
/* 63:   */     try
/* 64:   */     {
/* 65:55 */       if ((!deniedIps.contains(remotehostIP)) && (allowedIps.contains(remotehostIP))) {
/* 66:57 */         isValidIP = true;
/* 67:   */       } else {
/* 68:60 */         responses.add("Your IP address is not authorized");
/* 69:   */       }
/* 70:68 */       String requestUri = request.getRequestURI();
/* 71:69 */       if (isValidIP) {
/* 72:70 */         canSend = true;
/* 73:   */       }
/* 74:72 */       if (canSend)
/* 75:   */       {
/* 76:74 */         filterChain.doFilter(request, response);
/* 77:   */       }
/* 78:   */       else
/* 79:   */       {
/* 80:76 */         String messageString = "";
/* 81:77 */         for (int i = 0; i < responses.size(); i++) {
/* 82:78 */           messageString = messageString + responses.get(i).toString() + "<BR>";
/* 83:   */         }
/* 84:82 */         reply.setStatus(false);
/* 85:83 */         reply.setMessage(messageString);
/* 86:85 */         if (responsePath != null) {
/* 87:86 */           response.sendRedirect(responsePath + "?reply=" + reply.getMessage());
/* 88:   */         } else {
/* 89:89 */           response.sendRedirect(this.baseUrl + "error.jsp?reply=" + reply.getMessage());
/* 90:   */         }
/* 91:   */       }
/* 92:   */     }
/* 93:   */     catch (ServletException sx)
/* 94:   */     {
/* 95:96 */       this.filterConfig.getServletContext().log(sx.getMessage());
/* 96:   */     }
/* 97:   */     catch (IOException iox)
/* 98:   */     {
/* 99:98 */       this.filterConfig.getServletContext().log(iox.getMessage());
/* :0:   */     }
/* :1:   */   }
/* :2:   */   
/* :3:   */   public void destroy() {}
/* :4:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.security.IPSecurityFilter
 * JD-Core Version:    0.7.0.1
 */