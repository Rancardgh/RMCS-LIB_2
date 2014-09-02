/*  1:   */ package com.rancard.security;
/*  2:   */ 
/*  3:   */ import com.rancard.mobility.contentprovider.User;
/*  4:   */ import com.rancard.mobility.contentserver.ContentType;
/*  5:   */ import javax.servlet.http.HttpServletRequest;
/*  6:   */ import javax.servlet.http.HttpServletRequestWrapper;
/*  7:   */ import javax.servlet.http.HttpSession;
/*  8:   */ 
/*  9:   */ public class RMCSProfileRequestWrapper
/* 10:   */   extends HttpServletRequestWrapper
/* 11:   */ {
/* 12:   */   public RMCSProfileRequestWrapper(HttpServletRequest servletRequest)
/* 13:   */   {
/* 14:27 */     super(servletRequest);
/* 15:   */   }
/* 16:   */   
/* 17:   */   public String getParameter(String paramName)
/* 18:   */   {
/* 19:31 */     String paramVal = "";
/* 20:33 */     if (!"type".equals(paramName))
/* 21:   */     {
/* 22:34 */       paramVal = super.getParameter(paramName);
/* 23:   */     }
/* 24:   */     else
/* 25:   */     {
/* 26:36 */       paramVal = super.getParameter("type");
/* 27:37 */       int paramIntVal = Integer.parseInt(paramVal);
/* 28:   */       
/* 29:39 */       boolean serviceExist = checkService(paramIntVal);
/* 30:40 */       if (!serviceExist) {
/* 31:41 */         return "0";
/* 32:   */       }
/* 33:   */     }
/* 34:45 */     return paramVal;
/* 35:   */   }
/* 36:   */   
/* 37:   */   private boolean checkService(int serviceType)
/* 38:   */   {
/* 39:49 */     HttpSession session = super.getSession(true);
/* 40:50 */     User profile = (User)session.getAttribute("user");
/* 41:   */     
/* 42:52 */     boolean serviceExist = false;
/* 43:   */     try
/* 44:   */     {
/* 45:55 */       serviceExist = ContentType.isRegisteredWithProfile(serviceType, profile.getId());
/* 46:   */     }
/* 47:   */     catch (Exception ex) {}
/* 48:59 */     return serviceExist;
/* 49:   */   }
/* 50:   */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.security.RMCSProfileRequestWrapper
 * JD-Core Version:    0.7.0.1
 */