/*   1:    */ package com.rancard.mobility.contentserver;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ 
/*   6:    */ public class ContentType
/*   7:    */ {
/*   8:    */   private String serviceName;
/*   9:    */   private int serviceType;
/*  10:    */   private int parentServiceType;
/*  11:    */   
/*  12:    */   public ContentType()
/*  13:    */   {
/*  14: 23 */     this.serviceName = "";
/*  15: 24 */     this.serviceType = 0;
/*  16: 25 */     this.parentServiceType = 0;
/*  17:    */   }
/*  18:    */   
/*  19:    */   public ContentType(String serviceName, int serviceType, int parentServiceType)
/*  20:    */   {
/*  21: 30 */     this.serviceName = serviceName;
/*  22: 31 */     this.serviceType = serviceType;
/*  23: 32 */     this.parentServiceType = parentServiceType;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void setServiceName(String serviceName)
/*  27:    */   {
/*  28: 37 */     this.serviceName = serviceName;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void setServiceType(int serviceType)
/*  32:    */   {
/*  33: 41 */     this.serviceType = serviceType;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setParentServiceType(int parentServiceType)
/*  37:    */   {
/*  38: 45 */     this.parentServiceType = parentServiceType;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public String getServiceName()
/*  42:    */   {
/*  43: 50 */     return this.serviceName;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public int getServiceType()
/*  47:    */   {
/*  48: 54 */     return this.serviceType;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public int getTypeCode(String fileExtension)
/*  52:    */   {
/*  53: 58 */     if ("mid".equals(fileExtension)) {
/*  54: 59 */       this.serviceType = 2;
/*  55:    */     }
/*  56: 62 */     if ("jpg".equals(fileExtension)) {
/*  57: 63 */       this.serviceType = 4;
/*  58:    */     }
/*  59: 66 */     return this.serviceType;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public int getParentServiceType()
/*  63:    */   {
/*  64: 70 */     return this.parentServiceType;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public static void insertServiceType(String serviceName, String serviceUrl, int serviceType, int parentServiceType)
/*  68:    */     throws Exception
/*  69:    */   {
/*  70: 75 */     ContentTypeDB.insertServiceType(serviceName, serviceUrl, serviceType, parentServiceType);
/*  71:    */   }
/*  72:    */   
/*  73:    */   public static void updateServiceType(int oldServiceType, String serviceName, int parentServiceType, String serviceUrl)
/*  74:    */     throws Exception
/*  75:    */   {
/*  76: 79 */     ContentTypeDB.updateServiceType(oldServiceType, serviceName, parentServiceType, serviceUrl);
/*  77:    */   }
/*  78:    */   
/*  79:    */   public static void deleteServicType(int serviceType)
/*  80:    */     throws Exception
/*  81:    */   {
/*  82: 83 */     ContentTypeDB.deleteServicType(serviceType);
/*  83:    */   }
/*  84:    */   
/*  85:    */   public static ContentType viewServiceType(int serviceType)
/*  86:    */     throws Exception
/*  87:    */   {
/*  88: 87 */     return ContentTypeDB.viewServiceType(serviceType);
/*  89:    */   }
/*  90:    */   
/*  91:    */   public static List getDistinctTypes()
/*  92:    */     throws Exception
/*  93:    */   {
/*  94: 91 */     List types = new ArrayList();
/*  95: 92 */     types = ContentTypeDB.getDistinctTypes();
/*  96: 93 */     return types;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public static List getDistinctTypes(int parentServiceType)
/* 100:    */     throws Exception
/* 101:    */   {
/* 102: 97 */     List types = new ArrayList();
/* 103: 98 */     types = ContentTypeDB.getDistinctTypes(parentServiceType);
/* 104: 99 */     return types;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public static ArrayList getDistinctTypes(int parentServiceType, String accountId)
/* 108:    */     throws Exception
/* 109:    */   {
/* 110:103 */     return ContentTypeDB.getDistinctTypes(parentServiceType, accountId);
/* 111:    */   }
/* 112:    */   
/* 113:    */   public static boolean isRegisteredWithProfile(int serviceType, String accountId)
/* 114:    */     throws Exception
/* 115:    */   {
/* 116:107 */     return ContentTypeDB.isRegisteredWithProfile(serviceType, accountId);
/* 117:    */   }
/* 118:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.contentserver.ContentType
 * JD-Core Version:    0.7.0.1
 */