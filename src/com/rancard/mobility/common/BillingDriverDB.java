/*  1:   */ package com.rancard.mobility.common;
/*  2:   */ 
/*  3:   */ import com.rancard.common.DConnect;
/*  4:   */ import java.sql.Connection;
/*  5:   */ import java.sql.PreparedStatement;
/*  6:   */ import java.sql.ResultSet;
/*  7:   */ 
/*  8:   */ public abstract class BillingDriverDB
/*  9:   */ {
/* 10:   */   public static String viewBillingUrl(String networkPrefix, String command)
/* 11:   */     throws Exception
/* 12:   */   {
/* 13:26 */     String url = "";
/* 14:   */     
/* 15:28 */     ResultSet rs = null;
/* 16:29 */     Connection con = null;
/* 17:30 */     PreparedStatement prepstat = null;
/* 18:   */     try
/* 19:   */     {
/* 20:33 */       con = DConnect.getConnection();
/* 21:34 */       String query = "SELECT billing_url from network_billing WHERE network_prefix='" + networkPrefix + "' and command='" + command + "'";
/* 22:35 */       prepstat = con.prepareStatement(query);
/* 23:36 */       rs = prepstat.executeQuery();
/* 24:38 */       while (rs.next()) {
/* 25:39 */         url = rs.getString("biling_url");
/* 26:   */       }
/* 27:41 */       if ((url == null) || (url.equals(""))) {
/* 28:42 */         throw new Exception("11001");
/* 29:   */       }
/* 30:   */     }
/* 31:   */     catch (Exception ex)
/* 32:   */     {
/* 33:45 */       if (con != null) {
/* 34:46 */         con.close();
/* 35:   */       }
/* 36:48 */       throw new Exception(ex.getMessage());
/* 37:   */     }
/* 38:50 */     if (con != null) {
/* 39:51 */       con.close();
/* 40:   */     }
/* 41:53 */     return url;
/* 42:   */   }
/* 43:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.common.BillingDriverDB
 * JD-Core Version:    0.7.0.1
 */