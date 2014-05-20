/*   1:    */ package com.rancard.security;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import java.sql.Connection;
/*   5:    */ import java.sql.Date;
/*   6:    */ import java.sql.PreparedStatement;
/*   7:    */ import java.sql.ResultSet;
/*   8:    */ import java.sql.SQLException;
/*   9:    */ import java.util.ArrayList;
/*  10:    */ 
/*  11:    */ public class AccessControlListFactory
/*  12:    */ {
/*  13:    */   public static ArrayList viewWhiteList()
/*  14:    */     throws Exception
/*  15:    */   {
/*  16: 16 */     ResultSet rs = null;
/*  17: 17 */     Connection con = null;
/*  18: 18 */     PreparedStatement prepstat = null;
/*  19: 19 */     ArrayList whiteList = new ArrayList();
/*  20:    */     try
/*  21:    */     {
/*  22: 21 */       con = DConnect.getConnection();
/*  23: 22 */       Date tempdate = null;
/*  24:    */       
/*  25: 24 */       String SQL = "select * from whitelist";
/*  26: 25 */       prepstat = con.prepareStatement(SQL);
/*  27: 26 */       rs = prepstat.executeQuery();
/*  28: 27 */       while (rs.next())
/*  29:    */       {
/*  30: 28 */         AccessListItem userIP = new AccessListItem();
/*  31:    */         
/*  32: 30 */         whiteList.add(rs.getString("allowed_IP"));
/*  33:    */       }
/*  34: 33 */       rs.close();
/*  35: 34 */       rs = null;
/*  36: 35 */       prepstat.close();
/*  37: 36 */       prepstat = null;
/*  38: 37 */       con.close();
/*  39: 38 */       con = null;
/*  40:    */     }
/*  41:    */     catch (Exception ex)
/*  42:    */     {
/*  43: 40 */       if (con != null)
/*  44:    */       {
/*  45: 41 */         con.close();
/*  46: 42 */         con = null;
/*  47:    */       }
/*  48: 44 */       throw new Exception(ex.getMessage());
/*  49:    */     }
/*  50:    */     finally
/*  51:    */     {
/*  52: 46 */       if (rs != null)
/*  53:    */       {
/*  54:    */         try
/*  55:    */         {
/*  56: 48 */           rs.close();
/*  57:    */         }
/*  58:    */         catch (SQLException e) {}
/*  59: 52 */         rs = null;
/*  60:    */       }
/*  61: 54 */       if (prepstat != null)
/*  62:    */       {
/*  63:    */         try
/*  64:    */         {
/*  65: 56 */           prepstat.close();
/*  66:    */         }
/*  67:    */         catch (SQLException e) {}
/*  68: 60 */         prepstat = null;
/*  69:    */       }
/*  70: 62 */       if (con != null)
/*  71:    */       {
/*  72:    */         try
/*  73:    */         {
/*  74: 64 */           con.close();
/*  75:    */         }
/*  76:    */         catch (SQLException e) {}
/*  77: 68 */         con = null;
/*  78:    */       }
/*  79:    */     }
/*  80: 72 */     return whiteList;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public static ArrayList viewBlackList()
/*  84:    */     throws Exception
/*  85:    */   {
/*  86: 79 */     ResultSet rs = null;
/*  87: 80 */     Connection con = null;
/*  88: 81 */     PreparedStatement prepstat = null;
/*  89: 82 */     ArrayList whiteList = new ArrayList();
/*  90:    */     try
/*  91:    */     {
/*  92: 84 */       con = DConnect.getConnection();
/*  93: 85 */       Date tempdate = null;
/*  94:    */       
/*  95: 87 */       String SQL = "select * from blacklist";
/*  96: 88 */       prepstat = con.prepareStatement(SQL);
/*  97: 89 */       rs = prepstat.executeQuery();
/*  98: 90 */       while (rs.next()) {
/*  99: 92 */         whiteList.add(rs.getString("denied_IP"));
/* 100:    */       }
/* 101: 95 */       rs.close();
/* 102: 96 */       rs = null;
/* 103: 97 */       prepstat.close();
/* 104: 98 */       prepstat = null;
/* 105: 99 */       con.close();
/* 106:100 */       con = null;
/* 107:    */     }
/* 108:    */     catch (Exception ex)
/* 109:    */     {
/* 110:102 */       if (con != null)
/* 111:    */       {
/* 112:103 */         con.close();
/* 113:104 */         con = null;
/* 114:    */       }
/* 115:106 */       throw new Exception(ex.getMessage());
/* 116:    */     }
/* 117:    */     finally
/* 118:    */     {
/* 119:108 */       if (rs != null)
/* 120:    */       {
/* 121:    */         try
/* 122:    */         {
/* 123:110 */           rs.close();
/* 124:    */         }
/* 125:    */         catch (SQLException e) {}
/* 126:114 */         rs = null;
/* 127:    */       }
/* 128:116 */       if (prepstat != null)
/* 129:    */       {
/* 130:    */         try
/* 131:    */         {
/* 132:118 */           prepstat.close();
/* 133:    */         }
/* 134:    */         catch (SQLException e) {}
/* 135:122 */         prepstat = null;
/* 136:    */       }
/* 137:124 */       if (con != null)
/* 138:    */       {
/* 139:    */         try
/* 140:    */         {
/* 141:126 */           con.close();
/* 142:    */         }
/* 143:    */         catch (SQLException e) {}
/* 144:130 */         con = null;
/* 145:    */       }
/* 146:    */     }
/* 147:134 */     return whiteList;
/* 148:    */   }
/* 149:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.security.AccessControlListFactory
 * JD-Core Version:    0.7.0.1
 */