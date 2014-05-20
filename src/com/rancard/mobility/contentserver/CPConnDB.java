/*   1:    */ package com.rancard.mobility.contentserver;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import java.io.PrintStream;
/*   5:    */ import java.sql.Connection;
/*   6:    */ import java.sql.ResultSet;
/*   7:    */ import java.sql.Statement;
/*   8:    */ import java.util.ArrayList;
/*   9:    */ import java.util.Arrays;
/*  10:    */ import java.util.Date;
/*  11:    */ import java.util.List;
/*  12:    */ 
/*  13:    */ public final class CPConnDB
/*  14:    */ {
/*  15:    */   public static CPConnections viewConnection(String providerId, String networkPrefix)
/*  16:    */     throws Exception
/*  17:    */   {
/*  18: 15 */     CPConnections cnxn = null;
/*  19: 16 */     ResultSet rs = null;
/*  20: 17 */     Connection conn = null;
/*  21:    */     try
/*  22:    */     {
/*  23: 20 */       conn = DConnect.getConnection();
/*  24: 21 */       String query = "SELECT * from cp_connections cpc left outer join country_timezone ctz on ctz.country_alias = cpc.time_zone WHERE cpc.list_id = '" + providerId + "'";
/*  25:    */       
/*  26: 23 */       System.out.println(new Date() + ": " + CPConnDB.class + ":DEBUG About to get connections: " + query);
/*  27: 24 */       rs = conn.createStatement().executeQuery(query);
/*  28: 26 */       while (rs.next()) {
/*  29: 27 */         if (rs.getString("allowed_networks").contains(networkPrefix)) {
/*  30: 28 */           cnxn = new CPConnections(rs.getString("password"), rs.getString("username"), rs.getString("conn_id"), rs.getString("gateway_url"), rs.getString("method"), rs.getString("type"), Arrays.asList(rs.getString("allowed_networks").split(",")), rs.getString("list_id"), rs.getString("billing_mech"), rs.getString("country_name"), rs.getString("time_zone"), rs.getInt("utc_offset"), rs.getString("language"));
/*  31:    */         }
/*  32:    */       }
/*  33: 34 */       return cnxn;
/*  34:    */     }
/*  35:    */     catch (Exception ex)
/*  36:    */     {
/*  37: 36 */       System.out.println(new Date() + ": " + CPConnDB.class + ":ERROR " + ex.getMessage());
/*  38: 37 */       throw new Exception(ex.getMessage());
/*  39:    */     }
/*  40:    */     finally
/*  41:    */     {
/*  42: 39 */       if (rs != null) {
/*  43: 40 */         rs.close();
/*  44:    */       }
/*  45: 42 */       if (conn != null) {
/*  46: 43 */         conn.close();
/*  47:    */       }
/*  48:    */     }
/*  49:    */   }
/*  50:    */   
/*  51:    */   public static List viewConnections(String providerId)
/*  52:    */     throws Exception
/*  53:    */   {
/*  54: 50 */     List connections = new ArrayList();
/*  55: 51 */     ResultSet rs = null;
/*  56: 52 */     Connection conn = null;
/*  57:    */     try
/*  58:    */     {
/*  59: 56 */       conn = DConnect.getConnection();
/*  60: 57 */       String query = "SELECT * from cp_connections cpc left outer join country_timezone ctz on ctz.country_alias=cpc.time_zone WHERE cpc.list_id= '" + providerId + "'";
/*  61:    */       
/*  62: 59 */       System.out.println(new Date() + ": " + CPConnDB.class + ":DEBUG About to get connections: " + query);
/*  63:    */       
/*  64: 61 */       rs = conn.createStatement().executeQuery(query);
/*  65: 63 */       while (rs.next()) {
/*  66: 64 */         connections.add(new CPConnections(rs.getString("password"), rs.getString("username"), rs.getString("conn_id"), rs.getString("gateway_url"), rs.getString("method"), rs.getString("type"), Arrays.asList(rs.getString("allowed_networks").split(",")), rs.getString("list_id"), rs.getString("billing_mech"), rs.getString("country_name"), rs.getString("time_zone"), rs.getInt("utc_offset"), rs.getString("language")));
/*  67:    */       }
/*  68:    */     }
/*  69:    */     catch (Exception ex)
/*  70:    */     {
/*  71: 71 */       System.out.println(new Date() + ": " + CPConnDB.class + ":ERROR " + ex.getMessage());
/*  72: 72 */       throw new Exception(ex.getMessage());
/*  73:    */     }
/*  74:    */     finally
/*  75:    */     {
/*  76: 74 */       if (rs != null) {
/*  77: 75 */         rs.close();
/*  78:    */       }
/*  79: 77 */       if (conn != null) {
/*  80: 78 */         conn.close();
/*  81:    */       }
/*  82:    */     }
/*  83: 81 */     return connections;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public static List viewConnections(String providerId, String driverType)
/*  87:    */     throws Exception
/*  88:    */   {
/*  89: 86 */     List connections = new ArrayList();
/*  90: 87 */     ResultSet rs = null;
/*  91: 88 */     Connection conn = null;
/*  92:    */     try
/*  93:    */     {
/*  94: 91 */       conn = DConnect.getConnection();
/*  95: 92 */       String query = "SELECT * from cp_connections cpc left outer join country_timezone ctz on ctz.country_alias=cpc.time_zone WHERE list_id= '" + providerId + "' and type= '" + driverType + "'";
/*  96:    */       
/*  97: 94 */       System.out.println(new Date() + ": " + CPConnDB.class + ":DEBUG About to get connections: " + query);
/*  98:    */       
/*  99: 96 */       rs = conn.createStatement().executeQuery(query);
/* 100: 98 */       while (rs.next()) {
/* 101: 99 */         connections.add(new CPConnections(rs.getString("password"), rs.getString("username"), rs.getString("conn_id"), rs.getString("gateway_url"), rs.getString("method"), rs.getString("type"), Arrays.asList(rs.getString("allowed_networks").split(",")), rs.getString("list_id"), rs.getString("billing_mech"), rs.getString("country_name"), rs.getString("time_zone"), rs.getInt("utc_offset"), rs.getString("language")));
/* 102:    */       }
/* 103:    */     }
/* 104:    */     catch (Exception ex)
/* 105:    */     {
/* 106:105 */       System.out.println(new Date() + ": " + CPConnDB.class + ":ERROR " + ex.getMessage());
/* 107:106 */       throw new Exception(ex.getMessage());
/* 108:    */     }
/* 109:    */     finally
/* 110:    */     {
/* 111:109 */       if (rs != null) {
/* 112:110 */         rs.close();
/* 113:    */       }
/* 114:112 */       if (conn != null) {
/* 115:113 */         conn.close();
/* 116:    */       }
/* 117:    */     }
/* 118:116 */     return connections;
/* 119:    */   }
/* 120:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.contentserver.CPConnDB
 * JD-Core Version:    0.7.0.1
 */