/*  1:   */ package com.rancard.mobility.infoserver;
/*  2:   */ 
/*  3:   */ import com.rancard.mobility.infoserver.common.services.ServiceManager;
/*  4:   */ 
/*  5:   */ public class TestMain
/*  6:   */ {
/*  7:   */   public static void main(String[] args)
/*  8:   */   {
/*  9:30 */     InfoService is = new InfoService();
/* 10:31 */     is.setPublishDate("12/12/2006");
/* 11:32 */     is.setMessage("Hello world");
/* 12:33 */     is.setAccountId("test");
/* 13:34 */     is.setKeyword("In");
/* 14:35 */     is.setServiceName("International news");
/* 15:36 */     is.setServiceType("11");
/* 16:   */     try
/* 17:   */     {
/* 18:39 */       ServiceManager.createService(is);
/* 19:   */     }
/* 20:   */     catch (Exception ex)
/* 21:   */     {
/* 22:41 */       ex.printStackTrace();
/* 23:   */     }
/* 24:   */     try
/* 25:   */     {
/* 26:45 */       is.createInfoServiceEntry();
/* 27:   */     }
/* 28:   */     catch (Exception ex)
/* 29:   */     {
/* 30:47 */       ex.printStackTrace();
/* 31:   */     }
/* 32:   */   }
/* 33:   */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.TestMain
 * JD-Core Version:    0.7.0.1
 */