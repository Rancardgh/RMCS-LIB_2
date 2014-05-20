/*  1:   */ package com.rancard.util;
/*  2:   */ 
/*  3:   */ import com.rancard.common.Config;
/*  4:   */ import java.io.FileInputStream;
/*  5:   */ import java.io.FileOutputStream;
/*  6:   */ import java.io.PrintStream;
/*  7:   */ import java.util.Enumeration;
/*  8:   */ import java.util.Properties;
/*  9:   */ import javax.naming.Context;
/* 10:   */ import javax.naming.InitialContext;
/* 11:   */ import javax.naming.NamingException;
/* 12:   */ 
/* 13:   */ public class PropertyHolder
/* 14:   */ {
/* 15:   */   private static Properties props;
/* 16:   */   private static String filePath;
/* 17:   */   
/* 18:   */   public static void setProperties(Properties value)
/* 19:   */   {
/* 20:29 */     props = value;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public static boolean initialize(String propertyFile)
/* 24:   */   {
/* 25:33 */     filePath = propertyFile;
/* 26:34 */     return initialize();
/* 27:   */   }
/* 28:   */   
/* 29:   */   public static boolean initialize()
/* 30:   */   {
/* 31:38 */     props = new Properties();
/* 32:   */     try
/* 33:   */     {
/* 34:40 */       props.load(new FileInputStream(filePath));
/* 35:   */     }
/* 36:   */     catch (Exception e)
/* 37:   */     {
/* 38:43 */       return false;
/* 39:   */     }
/* 40:45 */     return true;
/* 41:   */   }
/* 42:   */   
/* 43:   */   public static boolean save()
/* 44:   */   {
/* 45:   */     try
/* 46:   */     {
/* 47:50 */       FileOutputStream fos = new FileOutputStream(filePath);
/* 48:51 */       props.store(fos, "CMS 3.0 PROPERTIES FILE");
/* 49:52 */       fos.close();
/* 50:   */     }
/* 51:   */     catch (Exception e)
/* 52:   */     {
/* 53:55 */       e.printStackTrace();
/* 54:56 */       return false;
/* 55:   */     }
/* 56:58 */     return true;
/* 57:   */   }
/* 58:   */   
/* 59:   */   public static String get(String key)
/* 60:   */   {
/* 61:62 */     return props.getProperty(key);
/* 62:   */   }
/* 63:   */   
/* 64:   */   public static void set(String key, String value)
/* 65:   */   {
/* 66:66 */     props.setProperty(key, value);
/* 67:   */   }
/* 68:   */   
/* 69:   */   public static String getPropsValue(String key)
/* 70:   */     throws Exception
/* 71:   */   {
/* 72:70 */     String value = null;
/* 73:   */     try
/* 74:   */     {
/* 75:72 */       Context ctx = new InitialContext();
/* 76:73 */       Context envCtx = (Context)ctx.lookup("java:comp/env");
/* 77:74 */       Config appConfig = (Config)envCtx.lookup("bean/rmcsConfig");
/* 78:   */       
/* 79:76 */       value = appConfig.valueOf(key);
/* 80:   */     }
/* 81:   */     catch (NamingException e)
/* 82:   */     {
/* 83:79 */       value = "";
/* 84:   */     }
/* 85:82 */     return value;
/* 86:   */   }
/* 87:   */   
/* 88:   */   public static void main(String[] s)
/* 89:   */   {
/* 90:86 */     filePath = "C:\\dev\\project\\rancardCms3.0\\CMS3.0\\nia\\WEB-INF\\cms.properties";
/* 91:   */     
/* 92:88 */     initialize();
/* 93:89 */     while (props.elements().hasMoreElements()) {
/* 94:90 */       System.out.println((String)props.elements().nextElement());
/* 95:   */     }
/* 96:94 */     System.out.println(save());
/* 97:   */   }
/* 98:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.util.PropertyHolder
 * JD-Core Version:    0.7.0.1
 */