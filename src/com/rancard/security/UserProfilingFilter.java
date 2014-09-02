/*  1:   */ package com.rancard.security;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import javax.servlet.Filter;
/*  5:   */ import javax.servlet.FilterChain;
/*  6:   */ import javax.servlet.FilterConfig;
/*  7:   */ import javax.servlet.ServletContext;
/*  8:   */ import javax.servlet.ServletException;
/*  9:   */ import javax.servlet.ServletRequest;
/* 10:   */ import javax.servlet.ServletResponse;
/* 11:   */ import javax.servlet.http.HttpServlet;
/* 12:   */ import javax.servlet.http.HttpServletRequest;
/* 13:   */ 
/* 14:   */ public class UserProfilingFilter
/* 15:   */   extends HttpServlet
/* 16:   */   implements Filter
/* 17:   */ {
/* 18:   */   private FilterConfig filterConfig;
/* 19:   */   
/* 20:   */   public void init(FilterConfig filterConfig)
/* 21:   */   {
/* 22:35 */     this.filterConfig = filterConfig;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
/* 26:   */   {
/* 27:   */     try
/* 28:   */     {
/* 29:42 */       HttpServletRequest request = (HttpServletRequest)req;
/* 30:44 */       if (request.getParameter("type") != null) {
/* 31:46 */         filterChain.doFilter(new RMCSProfileRequestWrapper((HttpServletRequest)req), res);
/* 32:   */       } else {
/* 33:49 */         filterChain.doFilter(req, res);
/* 34:   */       }
/* 35:   */     }
/* 36:   */     catch (ServletException sx)
/* 37:   */     {
/* 38:53 */       this.filterConfig.getServletContext().log(sx.getMessage());
/* 39:   */     }
/* 40:   */     catch (IOException iox)
/* 41:   */     {
/* 42:55 */       this.filterConfig.getServletContext().log(iox.getMessage());
/* 43:   */     }
/* 44:   */   }
/* 45:   */   
/* 46:   */   public void destroy()
/* 47:   */   {
/* 48:61 */     this.filterConfig = null;
/* 49:   */   }
/* 50:   */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.security.UserProfilingFilter
 * JD-Core Version:    0.7.0.1
 */