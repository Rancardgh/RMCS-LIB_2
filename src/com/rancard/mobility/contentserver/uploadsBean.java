/*   1:    */ package com.rancard.mobility.contentserver;
/*   2:    */ 
/*   3:    */ import com.rancard.util.DataImportListener;
/*   4:    */ import java.util.List;
/*   5:    */ 
/*   6:    */ public class uploadsBean
/*   7:    */ {
/*   8:    */   private String id;
/*   9:    */   private String filename;
/*  10:    */   private String listId;
/*  11:    */   private byte[] dataStream;
/*  12:    */   private byte[] previewStream;
/*  13:    */   private String format;
/*  14:    */   private String mimeType;
/*  15:    */   
/*  16:    */   public void setid(String id)
/*  17:    */   {
/*  18: 22 */     this.id = id;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public String getid()
/*  22:    */   {
/*  23: 26 */     return this.id;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void setfilename(String filename)
/*  27:    */   {
/*  28: 30 */     this.filename = filename;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public String getfilename()
/*  32:    */   {
/*  33: 34 */     return this.filename;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setlist_id(String list_id)
/*  37:    */   {
/*  38: 39 */     this.listId = list_id;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void setDataStream(byte[] in)
/*  42:    */   {
/*  43: 43 */     this.dataStream = in;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void setPreviewStream(byte[] in)
/*  47:    */   {
/*  48: 47 */     this.previewStream = in;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void setFormat(String format)
/*  52:    */   {
/*  53: 51 */     this.format = format;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void setMimeType(String mimeType)
/*  57:    */   {
/*  58: 55 */     this.mimeType = mimeType;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public String getlist_id()
/*  62:    */   {
/*  63: 59 */     return this.listId;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public byte[] getDataStream()
/*  67:    */   {
/*  68: 63 */     return this.dataStream;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public byte[] getPreviewStream()
/*  72:    */   {
/*  73: 67 */     return this.previewStream;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public String getFormat()
/*  77:    */   {
/*  78: 71 */     return this.format;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public String getMimeType()
/*  82:    */   {
/*  83: 75 */     return this.mimeType;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void createuploads()
/*  87:    */     throws Exception
/*  88:    */   {
/*  89: 79 */     uploadsDB uploads = new uploadsDB();
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void updateuploads()
/*  93:    */     throws Exception
/*  94:    */   {
/*  95: 85 */     uploadsDB uploads = new uploadsDB();
/*  96:    */   }
/*  97:    */   
/*  98:    */   public void deleteuploads()
/*  99:    */     throws Exception
/* 100:    */   {
/* 101: 91 */     uploadsDB uploads = new uploadsDB();
/* 102:    */     
/* 103: 93 */     uploads.deleteuploads(this.listId, this.id);
/* 104:    */   }
/* 105:    */   
/* 106:    */   public void viewuploads()
/* 107:    */     throws Exception
/* 108:    */   {
/* 109: 97 */     uploadsBean bean = new uploadsBean();
/* 110: 98 */     uploadsDB uploads = new uploadsDB();
/* 111:    */     
/* 112:100 */     bean = uploads.viewuploads(this.listId, this.id);
/* 113:    */     
/* 114:102 */     this.id = bean.getid();
/* 115:103 */     this.filename = bean.getfilename();
/* 116:104 */     this.dataStream = bean.getDataStream();
/* 117:105 */     this.previewStream = bean.getPreviewStream();
/* 118:106 */     this.listId = bean.getlist_id();
/* 119:107 */     this.format = bean.getFormat();
/* 120:108 */     this.mimeType = bean.getMimeType();
/* 121:    */   }
/* 122:    */   
/* 123:    */   public static uploadsBean viewuploads(String list_id, String id)
/* 124:    */     throws Exception
/* 125:    */   {
/* 126:114 */     return new uploadsDB().viewuploads(list_id, id);
/* 127:    */   }
/* 128:    */   
/* 129:    */   public static long getStorageSpaceUsed(String listId)
/* 130:    */     throws Exception
/* 131:    */   {
/* 132:120 */     return uploadsDB.totalDiskSpaceUsed(listId);
/* 133:    */   }
/* 134:    */   
/* 135:    */   public static void uploadPreview(DataImportListener listener, List fileItems, String listId)
/* 136:    */     throws Exception
/* 137:    */   {
/* 138:124 */     uploadsDB.uploadPreview(listener, fileItems, listId);
/* 139:    */   }
/* 140:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.contentserver.uploadsBean
 * JD-Core Version:    0.7.0.1
 */