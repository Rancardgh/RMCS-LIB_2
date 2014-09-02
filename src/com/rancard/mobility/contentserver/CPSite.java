/*   1:    */ package com.rancard.mobility.contentserver;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ 
/*   5:    */ public class CPSite
/*   6:    */ {
/*   7:    */   public static final String WEB = "0";
/*   8:    */   public static final String WAP = "1";
/*   9:    */   public static final String SMS = "2";
/*  10:    */   public static final String IVR = "3";
/*  11:    */   private String cpId;
/*  12:    */   private String cpSiteName;
/*  13:    */   private String cpSiteId;
/*  14:    */   private String siteType;
/*  15:    */   private boolean checkUser;
/*  16:    */   
/*  17:    */   public CPSite()
/*  18:    */   {
/*  19: 35 */     this.checkUser = false;
/*  20: 36 */     this.cpId = "";
/*  21: 37 */     this.cpSiteId = "";
/*  22: 38 */     this.siteType = "0";
/*  23: 39 */     this.cpSiteName = "";
/*  24:    */   }
/*  25:    */   
/*  26:    */   public String getCpId()
/*  27:    */   {
/*  28: 43 */     return this.cpId;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void setCpId(String cpId)
/*  32:    */   {
/*  33: 47 */     this.cpId = cpId;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public String getCpSiteName()
/*  37:    */   {
/*  38: 51 */     return this.cpSiteName;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public boolean getCheckUser()
/*  42:    */   {
/*  43: 55 */     return this.checkUser;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void setCpSiteName(String cpSiteName)
/*  47:    */   {
/*  48: 59 */     this.cpSiteName = cpSiteName;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public String getCpSiteId()
/*  52:    */   {
/*  53: 63 */     return this.cpSiteId;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void setCpSiteId(String cpSiteId)
/*  57:    */   {
/*  58: 67 */     this.cpSiteId = cpSiteId;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void setCheckUser(boolean check)
/*  62:    */   {
/*  63: 71 */     this.checkUser = check;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public static void createSite(String cpId, String siteName, String siteId, boolean check, String type)
/*  67:    */     throws Exception
/*  68:    */   {
/*  69: 76 */     CPSiteDB.createSite(cpId, siteName, siteId, check, type);
/*  70:    */   }
/*  71:    */   
/*  72:    */   public static void updateSiteName(String siteName, String siteId)
/*  73:    */     throws Exception
/*  74:    */   {
/*  75: 80 */     CPSiteDB.updateSiteName(siteName, siteId);
/*  76:    */   }
/*  77:    */   
/*  78:    */   public static void updateSiteAuthentication(boolean check, String siteId)
/*  79:    */     throws Exception
/*  80:    */   {
/*  81: 84 */     CPSiteDB.updateSiteAuthentication(check, siteId);
/*  82:    */   }
/*  83:    */   
/*  84:    */   public static void deleteSite(String siteId)
/*  85:    */     throws Exception
/*  86:    */   {
/*  87: 88 */     CPSiteDB.deleteSite(siteId);
/*  88:    */   }
/*  89:    */   
/*  90:    */   public static void deleteAllSitesForCP(String cpId)
/*  91:    */     throws Exception
/*  92:    */   {
/*  93: 92 */     CPSiteDB.deleteAllSitesForCP(cpId);
/*  94:    */   }
/*  95:    */   
/*  96:    */   public static CPSite viewSite(String siteId)
/*  97:    */     throws Exception
/*  98:    */   {
/*  99: 96 */     return CPSiteDB.viewSite(siteId);
/* 100:    */   }
/* 101:    */   
/* 102:    */   public static ArrayList viewAllSitesForCP(String cpId)
/* 103:    */     throws Exception
/* 104:    */   {
/* 105:100 */     return CPSiteDB.viewAllSitesForCP(cpId);
/* 106:    */   }
/* 107:    */   
/* 108:    */   public static ArrayList viewAffiliatedSitesForCP(String cpId)
/* 109:    */     throws Exception
/* 110:    */   {
/* 111:104 */     return CPSiteDB.viewAffiliatedSitesForCP(cpId);
/* 112:    */   }
/* 113:    */   
/* 114:    */   public static ArrayList viewCPsWithSites()
/* 115:    */     throws Exception
/* 116:    */   {
/* 117:108 */     return CPSiteDB.viewCPsWithSites();
/* 118:    */   }
/* 119:    */   
/* 120:    */   public static String getCpIdForSite(String siteId)
/* 121:    */     throws Exception
/* 122:    */   {
/* 123:112 */     return CPSiteDB.getCpIdForSite(siteId);
/* 124:    */   }
/* 125:    */   
/* 126:    */   public String getSiteType()
/* 127:    */   {
/* 128:116 */     return this.siteType;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public void setSiteType(String siteType)
/* 132:    */   {
/* 133:120 */     this.siteType = siteType;
/* 134:    */   }
/* 135:    */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.CPSite
 * JD-Core Version:    0.7.0.1
 */