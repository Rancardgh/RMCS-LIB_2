/*  1:   */ package com.rancard.security;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.net.InetAddress;
/*  5:   */ import javax.servlet.Filter;
/*  6:   */ import javax.servlet.FilterChain;
/*  7:   */ import javax.servlet.FilterConfig;
/*  8:   */ import javax.servlet.ServletContext;
/*  9:   */ import javax.servlet.ServletException;
/* 10:   */ import javax.servlet.ServletRequest;
/* 11:   */ import javax.servlet.ServletResponse;
/* 12:   */ import javax.servlet.http.HttpServlet;
/* 13:   */ import javax.servlet.http.HttpServletRequest;
/* 14:   */ import javax.servlet.http.HttpServletResponse;
/* 15:   */ import javax.servlet.http.HttpSession;
/* 16:   */ 
/* 17:   */ public class AuthenticationFilter
/* 18:   */   extends HttpServlet
/* 19:   */   implements Filter
/* 20:   */ {
/* 21:   */   private FilterConfig filterConfig;
/* 22:   */   
/* 23:   */   public void init(FilterConfig filterConfig)
/* 24:   */   {
/* 25:13 */     this.filterConfig = filterConfig;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
/* 29:   */   {
/* 30:20 */     ServletContext context = this.filterConfig.getServletContext();
/* 31:   */     try
/* 32:   */     {
/* 33:24 */       HttpServletRequest request = (HttpServletRequest)req;
/* 34:25 */       HttpServletResponse response = (HttpServletResponse)res;
/* 35:26 */       String s = request.getProtocol().toLowerCase();
/* 36:27 */       s = s.substring(0, s.indexOf("/")).toLowerCase();
/* 37:28 */       String base_url = s + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
/* 38:   */       
/* 39:30 */       InetAddress address = InetAddress.getByName(request.getServerName());
/* 40:31 */       String hostnameis = address.toString();
/* 41:32 */       String hostipis = hostnameis.substring(hostnameis.indexOf('/') + 1, hostnameis.length()).trim();
/* 42:   */       
/* 43:   */ 
/* 44:   */ 
/* 45:36 */       HttpSession session = request.getSession(true);
/* 46:37 */       String login = (String)session.getAttribute("login");
/* 47:   */       
/* 48:   */ 
/* 49:40 */       String requestUri = request.getRequestURI();
/* 50:41 */       String queryString = request.getQueryString();
/* 51:42 */       boolean accessAllowed = (login != null) && ("yes".equals(login));
/* 52:43 */       if (accessAllowed) {
/* 53:44 */         filterChain.doFilter(request, response);
/* 54:47 */       } else if (queryString != null) {
/* 55:48 */         response.sendRedirect(base_url + "default.jsp?login_err=1&redirect=" + requestUri + "?" + queryString);
/* 56:   */       } else {
/* 57:52 */         response.sendRedirect(base_url + "default.jsp?login_err=1&redirect=" + requestUri);
/* 58:   */       }
/* 59:   */     }
/* 60:   */     catch (ServletException sx)
/* 61:   */     {
/* 62:59 */       this.filterConfig.getServletContext().log(sx.getMessage());
/* 63:   */     }
/* 64:   */     catch (IOException iox)
/* 65:   */     {
/* 66:61 */       this.filterConfig.getServletContext().log(iox.getMessage());
/* 67:   */     }
/* 68:   */   }
/* 69:   */   
/* 70:   */   public void destroy() {}
/* 71:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.security.AuthenticationFilter
 * JD-Core Version:    0.7.0.1
 */