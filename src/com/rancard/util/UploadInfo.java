/*  1:   */ package com.rancard.util;
/*  2:   */ 
/*  3:   */ public class UploadInfo
/*  4:   */ {
/*  5:20 */   private long totalSize = 0L;
/*  6:21 */   private long bytesRead = 0L;
/*  7:22 */   private long elapsedTime = 0L;
/*  8:23 */   private String status = "done";
/*  9:24 */   private int fileIndex = 0;
/* 10:   */   
/* 11:   */   public UploadInfo() {}
/* 12:   */   
/* 13:   */   public UploadInfo(int fileIndex, long totalSize, long bytesRead, long elapsedTime, String status)
/* 14:   */   {
/* 15:32 */     this.fileIndex = fileIndex;
/* 16:33 */     this.totalSize = totalSize;
/* 17:34 */     this.bytesRead = bytesRead;
/* 18:35 */     this.elapsedTime = elapsedTime;
/* 19:36 */     this.status = status;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public String getStatus()
/* 23:   */   {
/* 24:41 */     return this.status;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public void setStatus(String status)
/* 28:   */   {
/* 29:46 */     this.status = status;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public long getTotalSize()
/* 33:   */   {
/* 34:51 */     return this.totalSize;
/* 35:   */   }
/* 36:   */   
/* 37:   */   public void setTotalSize(long totalSize)
/* 38:   */   {
/* 39:56 */     this.totalSize = totalSize;
/* 40:   */   }
/* 41:   */   
/* 42:   */   public long getBytesRead()
/* 43:   */   {
/* 44:61 */     return this.bytesRead;
/* 45:   */   }
/* 46:   */   
/* 47:   */   public void setBytesRead(long bytesRead)
/* 48:   */   {
/* 49:66 */     this.bytesRead = bytesRead;
/* 50:   */   }
/* 51:   */   
/* 52:   */   public long getElapsedTime()
/* 53:   */   {
/* 54:71 */     return this.elapsedTime;
/* 55:   */   }
/* 56:   */   
/* 57:   */   public void setElapsedTime(long elapsedTime)
/* 58:   */   {
/* 59:76 */     this.elapsedTime = elapsedTime;
/* 60:   */   }
/* 61:   */   
/* 62:   */   public boolean isInProgress()
/* 63:   */   {
/* 64:81 */     return ("progress".equals(this.status)) || ("start".equals(this.status));
/* 65:   */   }
/* 66:   */   
/* 67:   */   public int getFileIndex()
/* 68:   */   {
/* 69:86 */     return this.fileIndex;
/* 70:   */   }
/* 71:   */   
/* 72:   */   public void setFileIndex(int fileIndex)
/* 73:   */   {
/* 74:91 */     this.fileIndex = fileIndex;
/* 75:   */   }
/* 76:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.util.UploadInfo
 * JD-Core Version:    0.7.0.1
 */