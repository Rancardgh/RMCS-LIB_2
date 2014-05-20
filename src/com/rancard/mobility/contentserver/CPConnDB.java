/*   1:    */ package com.rancard.mobility.contentserver;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import java.sql.Connection;
/*   5:    */ import java.sql.PreparedStatement;
/*   6:    */ import java.sql.ResultSet;
/*   7:    */ import java.util.ArrayList;
/*   8:    */ 
/*   9:    */ public abstract class CPConnDB
/*  10:    */ {
/*  11:    */   public static CPConnections viewConnection(String providerId, String networkPrefix)
/*  12:    */     throws Exception
/*  13:    */   {
/*  14: 13 */     CPConnections cnxn = new CPConnections();
/*  15:    */     
/*  16: 15 */     ResultSet rs = null;
/*  17: 16 */     Connection con = null;
/*  18: 17 */     PreparedStatement prepstat = null;
/*  19:    */     try
/*  20:    */     {
/*  21: 20 */       con = DConnect.getConnection();
/*  22: 21 */       String query = "SELECT * from cp_connections WHERE list_id=?";
/*  23: 22 */       prepstat = con.prepareStatement(query);
/*  24: 23 */       prepstat.setString(1, providerId);
/*  25: 24 */       rs = prepstat.executeQuery();
/*  26: 26 */       while (rs.next())
/*  27:    */       {
/*  28: 27 */         cnxn.setAllowedNetworks(rs.getString("allowed_networks"));
/*  29: 28 */         if (cnxn.getAllowedNetworks().contains(networkPrefix))
/*  30:    */         {
/*  31: 29 */           cnxn.setConnection(rs.getString("conn_id"));
/*  32: 30 */           cnxn.setDriverType(rs.getString("type"));
/*  33: 31 */           cnxn.setGatewayURL(rs.getString("gateway_url"));
/*  34: 32 */           cnxn.setHttpMethod(rs.getString("method"));
/*  35: 33 */           cnxn.setPassword(rs.getString("password"));
/*  36: 34 */           cnxn.setProviderId(rs.getString("list_id"));
/*  37: 35 */           cnxn.setUsername(rs.getString("username"));
/*  38: 36 */           cnxn.setBillingMech(rs.getString("billing_mech"));
/*  39:    */         }
/*  40:    */       }
/*  41:    */     }
/*  42:    */     catch (Exception ex)
/*  43:    */     {
/*  44: 40 */       if (con != null) {
/*  45: 41 */         con.close();
/*  46:    */       }
/*  47: 43 */       throw new Exception(ex.getMessage());
/*  48:    */     }
/*  49: 45 */     if (con != null) {
/*  50: 46 */       con.close();
/*  51:    */     }
/*  52: 48 */     return cnxn;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public static ArrayList viewConnections(String providerId)
/*  56:    */     throws Exception
/*  57:    */   {
/*  58: 53 */     ArrayList connections = new ArrayList();
/*  59:    */     
/*  60: 55 */     ResultSet rs = null;
/*  61: 56 */     Connection con = null;
/*  62: 57 */     PreparedStatement prepstat = null;
/*  63:    */     try
/*  64:    */     {
/*  65: 60 */       con = DConnect.getConnection();
/*  66: 61 */       String query = "SELECT * from cp_connections cpc left outer join country_timezone ctz on ctz.country_alias=cpc.time_zone WHERE list_id=?";
/*  67:    */       
/*  68:    */ 
/*  69: 64 */       prepstat = con.prepareStatement(query);
/*  70: 65 */       prepstat.setString(1, providerId);
/*  71: 66 */       rs = prepstat.executeQuery();
/*  72: 68 */       while (rs.next())
/*  73:    */       {
/*  74: 69 */         CPConnections cnxn = new CPConnections();
/*  75: 70 */         cnxn.setAllowedNetworks(rs.getString("allowed_networks"));
/*  76: 71 */         cnxn.setConnection(rs.getString("conn_id"));
/*  77: 72 */         cnxn.setDriverType(rs.getString("type"));
/*  78: 73 */         cnxn.setGatewayURL(rs.getString("gateway_url"));
/*  79: 74 */         cnxn.setHttpMethod(rs.getString("method"));
/*  80: 75 */         cnxn.setPassword(rs.getString("password"));
/*  81: 76 */         cnxn.setProviderId(rs.getString("list_id"));
/*  82: 77 */         cnxn.setUsername(rs.getString("username"));
/*  83: 78 */         cnxn.setBillingMech(rs.getString("billing_mech"));
/*  84: 79 */         cnxn.setCountryAlias(rs.getString("time_zone"));
/*  85: 80 */         cnxn.setCountryName(rs.getString("country_name"));
/*  86: 81 */         cnxn.setUTCOffset(rs.getInt("utc_offset"));
/*  87: 82 */         cnxn.setLanguage(rs.getString("language"));
/*  88:    */         
/*  89: 84 */         connections.add(cnxn);
/*  90:    */       }
/*  91:    */     }
/*  92:    */     catch (Exception ex)
/*  93:    */     {
/*  94: 87 */       if (con != null) {
/*  95: 88 */         con.close();
/*  96:    */       }
/*  97: 90 */       throw new Exception(ex.getMessage());
/*  98:    */     }
/*  99: 92 */     if (con != null) {
/* 100: 93 */       con.close();
/* 101:    */     }
/* 102: 95 */     return connections;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public static ArrayList viewConnections(String providerId, String driverType)
/* 106:    */     throws Exception
/* 107:    */   {
/* 108:101 */     ArrayList connections = new ArrayList();
/* 109:    */     
/* 110:103 */     ResultSet rs = null;
/* 111:104 */     Connection con = null;
/* 112:105 */     PreparedStatement prepstat = null;
/* 113:    */     try
/* 114:    */     {
/* 115:108 */       con = DConnect.getConnection();
/* 116:109 */       String query = "SELECT * from cp_connections cpc left outer join country_timezone ctz on ctz.country_alias=cpc.time_zone WHERE list_id=? and type=?";
/* 117:    */       
/* 118:    */ 
/* 119:112 */       prepstat = con.prepareStatement(query);
/* 120:113 */       prepstat.setString(1, providerId);
/* 121:114 */       prepstat.setString(2, driverType);
/* 122:115 */       rs = prepstat.executeQuery();
/* 123:117 */       while (rs.next())
/* 124:    */       {
/* 125:118 */         CPConnections cnxn = new CPConnections();
/* 126:119 */         cnxn.setAllowedNetworks(rs.getString("allowed_networks"));
/* 127:120 */         cnxn.setConnection(rs.getString("conn_id"));
/* 128:121 */         cnxn.setDriverType(rs.getString("type"));
/* 129:122 */         cnxn.setGatewayURL(rs.getString("gateway_url"));
/* 130:123 */         cnxn.setHttpMethod(rs.getString("method"));
/* 131:124 */         cnxn.setPassword(rs.getString("password"));
/* 132:125 */         cnxn.setProviderId(rs.getString("list_id"));
/* 133:126 */         cnxn.setUsername(rs.getString("username"));
/* 134:127 */         cnxn.setBillingMech(rs.getString("billing_mech"));
/* 135:128 */         cnxn.setCountryAlias(rs.getString("time_zone"));
/* 136:129 */         cnxn.setCountryName(rs.getString("country_name"));
/* 137:130 */         cnxn.setUTCOffset(rs.getInt("utc_offset"));
/* 138:131 */         cnxn.setLanguage(rs.getString("language"));
/* 139:    */         
/* 140:133 */         connections.add(cnxn);
/* 141:    */       }
/* 142:    */     }
/* 143:    */     catch (Exception ex)
/* 144:    */     {
/* 145:136 */       if (con != null) {
/* 146:137 */         con.close();
/* 147:    */       }
/* 148:139 */       throw new Exception(ex.getMessage());
/* 149:    */     }
/* 150:141 */     if (con != null) {
/* 151:142 */       con.close();
/* 152:    */     }
/* 153:144 */     return connections;
/* 154:    */   }
/* 155:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.CPConnDB
 * JD-Core Version:    0.7.0.1
 */