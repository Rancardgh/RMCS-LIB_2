/*  1:   */ package com.rancard.mobility.contentserver;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ 
/*  5:   */ public class Format
/*  6:   */ {
/*  7:   */   private int id;
/*  8:   */   private String fileExt;
/*  9:   */   private String pushBearer;
/* 10:   */   private String mimeType;
/* 11:   */   
/* 12:   */   public Format()
/* 13:   */   {
/* 14:25 */     this.id = 0;
/* 15:26 */     this.fileExt = "";
/* 16:27 */     this.pushBearer = "";
/* 17:28 */     this.mimeType = "";
/* 18:   */   }
/* 19:   */   
/* 20:   */   public Format(int id, String fileExt, String push, String mimeType)
/* 21:   */   {
/* 22:33 */     this.id = id;
/* 23:34 */     this.fileExt = fileExt;
/* 24:35 */     this.pushBearer = push;
/* 25:36 */     this.mimeType = mimeType;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public void setId(int id)
/* 29:   */   {
/* 30:41 */     this.id = id;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public void setFileExt(String fileExt)
/* 34:   */   {
/* 35:45 */     this.fileExt = fileExt;
/* 36:   */   }
/* 37:   */   
/* 38:   */   public void setPushBearer(String push)
/* 39:   */   {
/* 40:49 */     this.pushBearer = push;
/* 41:   */   }
/* 42:   */   
/* 43:   */   public void setMimeType(String mimeType)
/* 44:   */   {
/* 45:53 */     this.mimeType = mimeType;
/* 46:   */   }
/* 47:   */   
/* 48:   */   public int getId()
/* 49:   */   {
/* 50:58 */     return this.id;
/* 51:   */   }
/* 52:   */   
/* 53:   */   public String getFileExt()
/* 54:   */   {
/* 55:62 */     return this.fileExt;
/* 56:   */   }
/* 57:   */   
/* 58:   */   public String getPushBearer()
/* 59:   */   {
/* 60:66 */     return this.pushBearer;
/* 61:   */   }
/* 62:   */   
/* 63:   */   public String getMimeType()
/* 64:   */   {
/* 65:70 */     return this.mimeType;
/* 66:   */   }
/* 67:   */   
/* 68:   */   public static Format viewFormat(int referenceCode)
/* 69:   */     throws Exception
/* 70:   */   {
/* 71:75 */     return FormatDB.viewFormat(referenceCode);
/* 72:   */   }
/* 73:   */   
/* 74:   */   public void updateFormat()
/* 75:   */     throws Exception
/* 76:   */   {
/* 77:79 */     FormatDB.updateFormat(this.id, this.fileExt, this.pushBearer, this.mimeType);
/* 78:   */   }
/* 79:   */   
/* 80:   */   public void removeFormat()
/* 81:   */     throws Exception
/* 82:   */   {
/* 83:83 */     FormatDB.deleteFormat(this.id);
/* 84:   */   }
/* 85:   */   
/* 86:   */   public void insertFormat()
/* 87:   */     throws Exception
/* 88:   */   {
/* 89:87 */     FormatDB.insertFormat(this.fileExt, this.pushBearer, this.mimeType);
/* 90:   */   }
/* 91:   */   
/* 92:   */   public static ArrayList getAllFormats()
/* 93:   */     throws Exception
/* 94:   */   {
/* 95:91 */     return FormatDB.viewFormats();
/* 96:   */   }
/* 97:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.contentserver.Format
 * JD-Core Version:    0.7.0.1
 */