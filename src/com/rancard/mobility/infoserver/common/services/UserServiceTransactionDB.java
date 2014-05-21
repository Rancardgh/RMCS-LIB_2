/*   1:    */ package com.rancard.mobility.infoserver.common.services;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import java.sql.Connection;
/*   5:    */ import java.sql.PreparedStatement;
/*   6:    */ import java.sql.ResultSet;
/*   7:    */ 
/*   8:    */ public abstract class UserServiceTransactionDB
/*   9:    */ {
/*  10:    */   public static void createTransaction(UserServiceTransaction trans)
/*  11:    */     throws Exception
/*  12:    */   {
/*  13: 26 */     ResultSet rs = null;
/*  14: 27 */     Connection con = null;
/*  15: 28 */     PreparedStatement prepstat = null;
/*  16:    */     try
/*  17:    */     {
/*  18: 31 */       con = DConnect.getConnection();
/*  19: 32 */       String query = "INSERT into transactions (trans_id,keyword,account_id,msisdn,callback_url,date,msg,is_billed,is_completed,price_point_id) values(?,?,?,?,?,?,?,?,?,?)";
/*  20:    */       
/*  21: 34 */       prepstat = con.prepareStatement(query);
/*  22: 35 */       prepstat.setString(1, trans.getTransactionId());
/*  23: 36 */       prepstat.setString(2, trans.getKeyword());
/*  24: 37 */       prepstat.setString(3, trans.getAccountId());
/*  25: 38 */       prepstat.setString(4, trans.getMsisdn());
/*  26: 39 */       prepstat.setString(5, trans.getCallBackUrl());
/*  27: 40 */       prepstat.setTimestamp(6, trans.getDate());
/*  28: 41 */       prepstat.setString(7, trans.getMsg());
/*  29: 42 */       prepstat.setInt(8, trans.getIsBilled());
/*  30: 43 */       prepstat.setInt(9, trans.getIsCompleted());
/*  31: 44 */       prepstat.setString(10, trans.getPricePoint());
/*  32:    */       
/*  33: 46 */       prepstat.execute();
/*  34:    */     }
/*  35:    */     catch (Exception ex)
/*  36:    */     {
/*  37: 48 */       if (con != null) {
/*  38: 49 */         con.close();
/*  39:    */       }
/*  40: 51 */       throw new Exception(ex.getMessage());
/*  41:    */     }
/*  42: 53 */     if (con != null) {
/*  43: 54 */       con.close();
/*  44:    */     }
/*  45:    */   }
/*  46:    */   
/*  47:    */   public static void updateTransaction(String transId, int isCompleted, int isBilled)
/*  48:    */     throws Exception
/*  49:    */   {
/*  50: 60 */     ResultSet rs = null;
/*  51: 61 */     Connection con = null;
/*  52: 62 */     PreparedStatement prepstat = null;
/*  53:    */     try
/*  54:    */     {
/*  55: 65 */       con = DConnect.getConnection();
/*  56: 66 */       String query = "UPDATE transactions SET is_billed=" + isBilled + ", is_completed=" + isCompleted + " WHERE trans_id='" + transId + "'";
/*  57: 67 */       prepstat = con.prepareStatement(query);
/*  58: 68 */       prepstat.execute();
/*  59:    */     }
/*  60:    */     catch (Exception ex)
/*  61:    */     {
/*  62: 70 */       if (con != null) {
/*  63: 71 */         con.close();
/*  64:    */       }
/*  65: 73 */       throw new Exception(ex.getMessage());
/*  66:    */     }
/*  67: 75 */     if (con != null) {
/*  68: 76 */       con.close();
/*  69:    */     }
/*  70:    */   }
/*  71:    */   
/*  72:    */   public static void deleteTransaction(String transId)
/*  73:    */     throws Exception
/*  74:    */   {
/*  75: 82 */     ResultSet rs = null;
/*  76: 83 */     Connection con = null;
/*  77: 84 */     PreparedStatement prepstat = null;
/*  78:    */     try
/*  79:    */     {
/*  80: 87 */       con = DConnect.getConnection();
/*  81: 88 */       String query = "DELETE from transactions WHERE trans_id=?";
/*  82: 89 */       prepstat = con.prepareStatement(query);
/*  83: 90 */       prepstat.setString(1, transId);
/*  84: 91 */       prepstat.execute();
/*  85:    */     }
/*  86:    */     catch (Exception ex)
/*  87:    */     {
/*  88: 93 */       if (con != null) {
/*  89: 94 */         con.close();
/*  90:    */       }
/*  91: 96 */       throw new Exception(ex.getMessage());
/*  92:    */     }
/*  93: 98 */     if (con != null) {
/*  94: 99 */       con.close();
/*  95:    */     }
/*  96:    */   }
/*  97:    */   
/*  98:    */   public static UserServiceTransaction viewTransaction(String transId)
/*  99:    */     throws Exception
/* 100:    */   {
/* 101:104 */     UserServiceTransaction newBean = new UserServiceTransaction();
/* 102:    */     
/* 103:    */ 
/* 104:107 */     ResultSet rs = null;
/* 105:108 */     Connection con = null;
/* 106:109 */     PreparedStatement prepstat = null;
/* 107:    */     try
/* 108:    */     {
/* 109:112 */       con = DConnect.getConnection();
/* 110:113 */       String query = "select * from transactions WHERE trans_id='" + transId + "'";
/* 111:114 */       prepstat = con.prepareStatement(query);
/* 112:115 */       rs = prepstat.executeQuery();
/* 113:117 */       while (rs.next())
/* 114:    */       {
/* 115:118 */         newBean.setAccountId(rs.getString("account_id"));
/* 116:119 */         newBean.setDate(rs.getTimestamp("date"));
/* 117:120 */         newBean.setIsBilled(rs.getInt("is_billed"));
/* 118:121 */         newBean.setIsCompleted(rs.getInt("is_completed"));
/* 119:122 */         newBean.setKeyword(rs.getString("keyword"));
/* 120:123 */         newBean.setCallBackUrl(rs.getString("callback_url"));
/* 121:124 */         newBean.setMsg(rs.getString("msg"));
/* 122:125 */         newBean.setMsisdn(rs.getString("msisdn"));
/* 123:126 */         newBean.setTransactionId(rs.getString("trans_id"));
/* 124:127 */         newBean.setPricePoint(rs.getString("price_point_id"));
/* 125:    */       }
/* 126:    */     }
/* 127:    */     catch (Exception ex)
/* 128:    */     {
/* 129:130 */       if (con != null) {
/* 130:131 */         con.close();
/* 131:    */       }
/* 132:133 */       throw new Exception(ex.getMessage());
/* 133:    */     }
/* 134:135 */     if (con != null) {
/* 135:136 */       con.close();
/* 136:    */     }
/* 137:138 */     return newBean;
/* 138:    */   }
/* 139:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.common.services.UserServiceTransactionDB
 * JD-Core Version:    0.7.0.1
 */