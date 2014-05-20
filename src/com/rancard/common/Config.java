/*  1:   */ package com.rancard.common;
/*  2:   */ 
/*  3:   */ import java.io.FileInputStream;
/*  4:   */ import java.io.FileOutputStream;
/*  5:   */ import java.io.IOException;
/*  6:   */ import java.util.Date;
/*  7:   */ import java.util.Properties;
/*  8:   */ 
/*  9:   */ public class Config
/* 10:   */ {
/* 11:10 */   private String location = "WEB-INF/conf/rmcs.properties";
/* 12:11 */   private Properties defaultProps = new Properties();
/* 13:   */   
/* 14:   */   public void setLocation(String location)
/* 15:   */     throws Exception
/* 16:   */   {
/* 17:20 */     this.location = location;
/* 18:   */     try
/* 19:   */     {
/* 20:23 */       load();
/* 21:   */     }
/* 22:   */     catch (IOException ex)
/* 23:   */     {
/* 24:25 */       ex.printStackTrace();
/* 25:   */     }
/* 26:   */   }
/* 27:   */   
/* 28:   */   public void init()
/* 29:   */     throws Exception
/* 30:   */   {
/* 31:30 */     if (this.location == null) {
/* 32:31 */       throw new Exception("'location'  of config file must be set");
/* 33:   */     }
/* 34:33 */     load();
/* 35:   */   }
/* 36:   */   
/* 37:   */   public Properties load()
/* 38:   */     throws IOException
/* 39:   */   {
/* 40:41 */     FileInputStream in = new FileInputStream(this.location);
/* 41:42 */     this.defaultProps.load(in);
/* 42:43 */     in.close();
/* 43:   */     
/* 44:45 */     return this.defaultProps;
/* 45:   */   }
/* 46:   */   
/* 47:   */   public void Store()
/* 48:   */     throws IOException
/* 49:   */   {
/* 50:51 */     FileOutputStream out = new FileOutputStream(this.location);
/* 51:   */     
/* 52:53 */     this.defaultProps.store(out, "Properties modified on" + new Date().toString());
/* 53:   */     
/* 54:   */ 
/* 55:56 */     out.close();
/* 56:   */   }
/* 57:   */   
/* 58:   */   public String valueOf(String key)
/* 59:   */   {
/* 60:61 */     return this.defaultProps.getProperty(key);
/* 61:   */   }
/* 62:   */   
/* 63:   */   public void changeValueOf(String key, String newValue)
/* 64:   */   {
/* 65:65 */     this.defaultProps.put(key, newValue);
/* 66:   */   }
/* 67:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.common.Config
 * JD-Core Version:    0.7.0.1
 */