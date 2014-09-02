/*  1:   */ package com.rancard.util;
/*  2:   */ 
/*  3:   */ import javax.servlet.http.HttpServletRequest;
/*  4:   */ import javax.servlet.http.HttpSession;
/*  5:   */ import uk.ltd.getahead.dwr.WebContext;
/*  6:   */ import uk.ltd.getahead.dwr.WebContextFactory;
/*  7:   */ 
/*  8:   */ public class UploadMonitor
/*  9:   */ {
/* 10:   */   public UploadInfo getUploadInfo()
/* 11:   */   {
/* 12:26 */     HttpServletRequest req = WebContextFactory.get().getHttpServletRequest();
/* 13:28 */     if (req.getSession().getAttribute("uploadInfo") != null) {
/* 14:29 */       return (UploadInfo)req.getSession().getAttribute("uploadInfo");
/* 15:   */     }
/* 16:31 */     return new UploadInfo();
/* 17:   */   }
/* 18:   */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.util.UploadMonitor
 * JD-Core Version:    0.7.0.1
 */