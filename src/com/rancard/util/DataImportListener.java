/*  1:   */ package com.rancard.util;
/*  2:   */ 
/*  3:   */ import javax.servlet.http.HttpServletRequest;
/*  4:   */ import javax.servlet.http.HttpSession;
/*  5:   */ 
/*  6:   */ public class DataImportListener
/*  7:   */   implements ProcessListener
/*  8:   */ {
/*  9:   */   private HttpServletRequest request;
/* 10:23 */   private long delay = 0L;
/* 11:24 */   private long startTime = 0L;
/* 12:25 */   private int totalProcessSteps = 0;
/* 13:26 */   private int totalProcessStepsCompleted = 0;
/* 14:27 */   private int totalFiles = -1;
/* 15:   */   
/* 16:   */   public DataImportListener(HttpServletRequest request, long debugDelay, int numberOfSteps)
/* 17:   */   {
/* 18:31 */     this.request = request;
/* 19:32 */     this.delay = debugDelay;
/* 20:   */     
/* 21:   */ 
/* 22:35 */     this.totalProcessSteps = numberOfSteps;
/* 23:36 */     this.startTime = System.currentTimeMillis();
/* 24:   */   }
/* 25:   */   
/* 26:   */   public void start()
/* 27:   */   {
/* 28:41 */     this.totalFiles += 1;
/* 29:42 */     updateUploadInfo("start");
/* 30:   */   }
/* 31:   */   
/* 32:   */   public void processRead(int stepsRead)
/* 33:   */   {
/* 34:47 */     this.totalProcessStepsCompleted += stepsRead;
/* 35:48 */     updateUploadInfo("progress");
/* 36:   */     try
/* 37:   */     {
/* 38:52 */       Thread.sleep(this.delay);
/* 39:   */     }
/* 40:   */     catch (InterruptedException e)
/* 41:   */     {
/* 42:56 */       e.printStackTrace();
/* 43:   */     }
/* 44:   */   }
/* 45:   */   
/* 46:   */   public void error(String message)
/* 47:   */   {
/* 48:62 */     updateUploadInfo("error");
/* 49:   */   }
/* 50:   */   
/* 51:   */   public void done()
/* 52:   */   {
/* 53:67 */     updateUploadInfo("done");
/* 54:   */   }
/* 55:   */   
/* 56:   */   private long getDelta()
/* 57:   */   {
/* 58:72 */     return (System.currentTimeMillis() - this.startTime) / 1000L;
/* 59:   */   }
/* 60:   */   
/* 61:   */   private void updateUploadInfo(String status)
/* 62:   */   {
/* 63:77 */     long delta = (System.currentTimeMillis() - this.startTime) / 1000L;
/* 64:78 */     this.request.getSession().setAttribute("uploadInfo", new UploadInfo(this.totalFiles, this.totalProcessSteps, this.totalProcessStepsCompleted, delta, status));
/* 65:   */   }
/* 66:   */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.util.DataImportListener
 * JD-Core Version:    0.7.0.1
 */