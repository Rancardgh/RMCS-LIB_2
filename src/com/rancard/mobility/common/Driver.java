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
/* 11:11 */     PushDriver driver = null;
/* 12:12 */     if (driverName.equalsIgnoreCase("mks")) {
/* 13:13 */       driver = new MksDriver("http://mks.alcatel.pt:8004/ghana/sms/xml");
/* 14:14 */     } else if (driverName.equalsIgnoreCase("rms")) {
/* 15:15 */       driver = new RMSDriver("http://212.96.11.19/rms12");
/* 16:16 */     } else if (driverName.equalsIgnoreCase("kannel")) {
/* 17:17 */       driver = new KannelDriver("http://192.168.1.249:13013/cgi-bin");
/* 18:   */     }
/* 19:19 */     return driver;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public static PushDriver getDriver(String driverType, String gatewayUrl)
/* 23:   */   {
/* 24:24 */     PushDriver driver = null;
/* 25:25 */     if (driverType.equalsIgnoreCase("mks")) {
/* 26:26 */       driver = new MksDriver(gatewayUrl);
/* 27:27 */     } else if (driverType.equalsIgnoreCase("rms")) {
/* 28:28 */       driver = new RMSDriver(gatewayUrl);
/* 29:29 */     } else if (driverType.equalsIgnoreCase("kannel")) {
/* 30:30 */       driver = new KannelDriver(gatewayUrl);
/* 31:   */     }
/* 32:32 */     return driver;
/* 33:   */   }
/* 34:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.common.Driver
 * JD-Core Version:    0.7.0.1
 */