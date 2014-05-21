/*  1:   */ package com.rancard.mobility.common;
/*  2:   */ 
/*  3:   */ public abstract class Driver
/*  4:   */ {
/*  5:   */   public static final String MKS_SUPPORT = "mks";
/*  6:   */   public static final String RMS_SUPPORT = "rms";
/*  7:   */   public static final String KANNEL_SUPPORT = "kannel";
/*  8:   */   
/*  9:   */   public static PushDriver getPushDriver(String driverName)
/* 10:   */   {
/* 11:10 */     PushDriver driver = null;
/* 12:11 */     if (driverName.equals("mks")) {
/* 13:12 */       driver = new MksDriver("http://mks.alcatel.pt:8004/ghana/sms/xml");
/* 14:13 */     } else if (driverName.equals("rms")) {
/* 15:14 */       driver = new RMSDriver("http://212.96.11.19/rms12");
/* 16:15 */     } else if (driverName.equals("kannel")) {
/* 17:16 */       driver = new KannelDriver("http://192.168.1.249:13013/cgi-bin");
/* 18:   */     }
/* 19:17 */     return driver;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public static PushDriver getDriver(String driverType, String gatewayUrl)
/* 23:   */   {
/* 24:22 */     PushDriver driver = null;
/* 25:23 */     if (driverType.equals("mks")) {
/* 26:24 */       driver = new MksDriver(gatewayUrl);
/* 27:25 */     } else if (driverType.equals("rms")) {
/* 28:26 */       driver = new RMSDriver(gatewayUrl);
/* 29:27 */     } else if (driverType.equals("kannel")) {
/* 30:28 */       driver = new KannelDriver(gatewayUrl);
/* 31:   */     }
/* 32:29 */     return driver;
/* 33:   */   }
/* 34:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.common.Driver
 * JD-Core Version:    0.7.0.1
 */