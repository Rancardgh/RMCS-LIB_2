/*   1:    */ package com.rancard.mobility.contentserver;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import java.sql.Connection;
/*   5:    */ import java.sql.PreparedStatement;
/*   6:    */ import java.sql.ResultSet;
/*   7:    */ 
/*   8:    */ public class NetworkconnectionsDB
/*   9:    */ {
/*  10:    */   public void createcp_connections(String username, String password, String conn_id, String gateway_url, String method, String type, String network_prefix)
/*  11:    */     throws Exception
/*  12:    */   {
/*  13: 23 */     ResultSet rs = null;
/*  14: 24 */     Connection con = null;
/*  15: 25 */     PreparedStatement prepstat = null;
/*  16:    */     try
/*  17:    */     {
/*  18: 27 */       con = DConnect.getConnection();
/*  19:    */       
/*  20: 29 */       String SQL = "insert into cp_connections(username,password,conn_id,gateway_url,method,type,network_prefix) values(?,?,?,?,?,?,?)";
/*  21: 30 */       prepstat = con.prepareStatement(SQL);
/*  22:    */       
/*  23: 32 */       prepstat.setString(1, username);
/*  24:    */       
/*  25: 34 */       prepstat.setString(2, password);
/*  26:    */       
/*  27: 36 */       prepstat.setString(3, conn_id);
/*  28:    */       
/*  29: 38 */       prepstat.setString(4, gateway_url);
/*  30:    */       
/*  31: 40 */       prepstat.setString(5, method);
/*  32:    */       
/*  33: 42 */       prepstat.setString(6, type);
/*  34:    */       
/*  35: 44 */       prepstat.setString(7, network_prefix);
/*  36: 45 */       prepstat.execute();
/*  37:    */     }
/*  38:    */     catch (Exception ex)
/*  39:    */     {
/*  40: 47 */       if (con != null) {
/*  41: 48 */         con.close();
/*  42:    */       }
/*  43: 50 */       throw new Exception(ex.getMessage());
/*  44:    */     }
/*  45: 52 */     if (con != null) {
/*  46: 53 */       con.close();
/*  47:    */     }
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void updatecp_connections(String username, String password, String conn_id, String gateway_url, String method, String type, String network_prefix)
/*  51:    */     throws Exception
/*  52:    */   {
/*  53: 67 */     ResultSet rs = null;
/*  54: 68 */     Connection con = null;
/*  55: 69 */     PreparedStatement prepstat = null;
/*  56:    */     try
/*  57:    */     {
/*  58: 71 */       con = DConnect.getConnection();
/*  59:    */       
/*  60: 73 */       String SQL = "update cp_connections set username=?,password=?,gateway_url=?,method=?,type=?,network_prefix=? where conn_id=?";
/*  61: 74 */       prepstat = con.prepareStatement(SQL);
/*  62:    */       
/*  63: 76 */       prepstat.setString(1, username);
/*  64:    */       
/*  65: 78 */       prepstat.setString(2, password);
/*  66:    */       
/*  67: 80 */       prepstat.setString(3, gateway_url);
/*  68:    */       
/*  69: 82 */       prepstat.setString(4, method);
/*  70:    */       
/*  71: 84 */       prepstat.setString(5, type);
/*  72:    */       
/*  73: 86 */       prepstat.setString(6, network_prefix);
/*  74:    */       
/*  75: 88 */       prepstat.setString(7, conn_id);
/*  76: 89 */       prepstat.execute();
/*  77:    */     }
/*  78:    */     catch (Exception ex)
/*  79:    */     {
/*  80: 91 */       if (con != null) {
/*  81: 92 */         con.close();
/*  82:    */       }
/*  83: 94 */       throw new Exception(ex.getMessage());
/*  84:    */     }
/*  85: 96 */     if (con != null) {
/*  86: 97 */       con.close();
/*  87:    */     }
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void deletecp_connections(String conn_id)
/*  91:    */     throws Exception
/*  92:    */   {
/*  93:104 */     ResultSet rs = null;
/*  94:105 */     Connection con = null;
/*  95:106 */     PreparedStatement prepstat = null;
/*  96:    */     try
/*  97:    */     {
/*  98:108 */       con = DConnect.getConnection();
/*  99:109 */       String SQL = "delete from cp_connections where conn_id=?";
/* 100:110 */       prepstat = con.prepareStatement(SQL);
/* 101:    */       
/* 102:112 */       prepstat.setString(1, conn_id);
/* 103:113 */       prepstat.execute();
/* 104:    */     }
/* 105:    */     catch (Exception ex)
/* 106:    */     {
/* 107:115 */       if (con != null) {
/* 108:116 */         con.close();
/* 109:    */       }
/* 110:118 */       throw new Exception(ex.getMessage());
/* 111:    */     }
/* 112:120 */     if (con != null) {
/* 113:121 */       con.close();
/* 114:    */     }
/* 115:    */   }
/* 116:    */   
/* 117:    */   public NetworkConnectionsBean viewcp_connections(String conn_id)
/* 118:    */     throws Exception
/* 119:    */   {
/* 120:127 */     NetworkConnectionsBean cp_connections = new NetworkConnectionsBean();
/* 121:    */     
/* 122:    */ 
/* 123:130 */     ResultSet rs = null;
/* 124:131 */     Connection con = null;
/* 125:132 */     PreparedStatement prepstat = null;
/* 126:    */     try
/* 127:    */     {
/* 128:134 */       con = DConnect.getConnection();
/* 129:    */       
/* 130:136 */       String SQL = "select * from  cp_connections where conn_id=?";
/* 131:137 */       prepstat = con.prepareStatement(SQL);
/* 132:    */       
/* 133:139 */       prepstat.setString(1, conn_id);
/* 134:140 */       rs = prepstat.executeQuery();
/* 135:141 */       while (rs.next())
/* 136:    */       {
/* 137:143 */         cp_connections.setusername(rs.getString("username"));
/* 138:144 */         cp_connections.setpassword(rs.getString("password"));
/* 139:145 */         cp_connections.setconn_id(rs.getString("conn_id"));
/* 140:146 */         cp_connections.setgateway_url(rs.getString("gateway_url"));
/* 141:147 */         cp_connections.setmethod(rs.getString("method"));
/* 142:148 */         cp_connections.settype(rs.getString("type"));
/* 143:149 */         cp_connections.setnetwork_prefix(rs.getString("network_prefix"));
/* 144:    */       }
/* 145:    */     }
/* 146:    */     catch (Exception ex)
/* 147:    */     {
/* 148:152 */       if (con != null) {
/* 149:153 */         con.close();
/* 150:    */       }
/* 151:155 */       throw new Exception(ex.getMessage());
/* 152:    */     }
/* 153:157 */     if (con != null) {
/* 154:158 */       con.close();
/* 155:    */     }
/* 156:160 */     return cp_connections;
/* 157:    */   }
/* 158:    */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.NetworkconnectionsDB
 * JD-Core Version:    0.7.0.1
 */