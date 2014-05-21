/*   1:    */ package com.rancard.mobility.contentserver;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import java.sql.Connection;
/*   5:    */ import java.sql.PreparedStatement;
/*   6:    */ import java.sql.ResultSet;
/*   7:    */ import java.util.ArrayList;
/*   8:    */ import java.util.List;
/*   9:    */ 
/*  10:    */ public abstract class PhoneMakeDB
/*  11:    */ {
/*  12:    */   public static void insertPhoneMake(String phoneMake)
/*  13:    */     throws Exception
/*  14:    */   {
/*  15: 28 */     ResultSet rs = null;
/*  16: 29 */     Connection con = null;
/*  17: 30 */     PreparedStatement prepstat = null;
/*  18:    */     try
/*  19:    */     {
/*  20: 33 */       con = DConnect.getConnection();
/*  21: 34 */       String query = "INSERT into phone_make_list(phone_manufacturer) values(?)";
/*  22:    */       
/*  23: 36 */       prepstat = con.prepareStatement(query);
/*  24: 37 */       prepstat.setString(1, phoneMake);
/*  25:    */       
/*  26: 39 */       prepstat.execute();
/*  27:    */     }
/*  28:    */     catch (Exception ex)
/*  29:    */     {
/*  30: 41 */       if (con != null) {
/*  31: 42 */         con.close();
/*  32:    */       }
/*  33: 44 */       throw new Exception(ex.getMessage());
/*  34:    */     }
/*  35: 46 */     if (con != null) {
/*  36: 47 */       con.close();
/*  37:    */     }
/*  38:    */   }
/*  39:    */   
/*  40:    */   public static void updatePhoneMake(int id, String phoneMake)
/*  41:    */     throws Exception
/*  42:    */   {
/*  43: 57 */     ResultSet rs = null;
/*  44: 58 */     Connection con = null;
/*  45: 59 */     PreparedStatement prepstat = null;
/*  46:    */     try
/*  47:    */     {
/*  48: 62 */       con = DConnect.getConnection();
/*  49: 63 */       String query = "UPDATE phone_make_list SET phone_manufacturer=? WHERE id=" + id;
/*  50:    */       
/*  51: 65 */       prepstat = con.prepareStatement(query);
/*  52: 66 */       prepstat.setString(1, phoneMake);
/*  53: 67 */       prepstat.execute();
/*  54:    */     }
/*  55:    */     catch (Exception ex)
/*  56:    */     {
/*  57: 69 */       if (con != null) {
/*  58: 70 */         con.close();
/*  59:    */       }
/*  60: 72 */       throw new Exception(ex.getMessage());
/*  61:    */     }
/*  62: 74 */     if (con != null) {
/*  63: 75 */       con.close();
/*  64:    */     }
/*  65:    */   }
/*  66:    */   
/*  67:    */   public static void deletePhoneMake(int id)
/*  68:    */     throws Exception
/*  69:    */   {
/*  70: 84 */     ResultSet rs = null;
/*  71: 85 */     Connection con = null;
/*  72: 86 */     PreparedStatement prepstat = null;
/*  73:    */     try
/*  74:    */     {
/*  75: 89 */       con = DConnect.getConnection();
/*  76: 90 */       String query = "DELETE from phone_make_list WHERE id=?";
/*  77: 91 */       prepstat = con.prepareStatement(query);
/*  78: 92 */       prepstat.setInt(1, id);
/*  79: 93 */       prepstat.execute();
/*  80:    */     }
/*  81:    */     catch (Exception ex)
/*  82:    */     {
/*  83: 95 */       if (con != null) {
/*  84: 96 */         con.close();
/*  85:    */       }
/*  86: 98 */       throw new Exception(ex.getMessage());
/*  87:    */     }
/*  88:100 */     if (con != null) {
/*  89:101 */       con.close();
/*  90:    */     }
/*  91:    */   }
/*  92:    */   
/*  93:    */   public static PhoneMake viewPhoneMake(int id)
/*  94:    */     throws Exception
/*  95:    */   {
/*  96:109 */     PhoneMake newBean = new PhoneMake();
/*  97:    */     
/*  98:111 */     ResultSet rs = null;
/*  99:112 */     Connection con = null;
/* 100:113 */     PreparedStatement prepstat = null;
/* 101:    */     try
/* 102:    */     {
/* 103:116 */       con = DConnect.getConnection();
/* 104:117 */       String query = "SELECT * from phone_make_list WHERE id=?";
/* 105:118 */       prepstat = con.prepareStatement(query);
/* 106:119 */       prepstat.setInt(1, id);
/* 107:120 */       rs = prepstat.executeQuery();
/* 108:122 */       while (rs.next())
/* 109:    */       {
/* 110:123 */         newBean.setId(rs.getInt("id"));
/* 111:124 */         newBean.setMake(rs.getString("phone_manufacturer"));
/* 112:    */       }
/* 113:    */     }
/* 114:    */     catch (Exception ex)
/* 115:    */     {
/* 116:127 */       if (con != null) {
/* 117:128 */         con.close();
/* 118:    */       }
/* 119:130 */       throw new Exception(ex.getMessage());
/* 120:    */     }
/* 121:132 */     if (con != null) {
/* 122:133 */       con.close();
/* 123:    */     }
/* 124:135 */     return newBean;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public static List getPhoneMakes()
/* 128:    */     throws Exception
/* 129:    */   {
/* 130:139 */     List makes = new ArrayList();
/* 131:    */     
/* 132:    */ 
/* 133:142 */     ResultSet rs = null;
/* 134:143 */     Connection con = null;
/* 135:144 */     PreparedStatement prepstat = null;
/* 136:    */     try
/* 137:    */     {
/* 138:147 */       con = DConnect.getConnection();
/* 139:148 */       String query = "SELECT * from phone_make_list";
/* 140:149 */       prepstat = con.prepareStatement(query);
/* 141:150 */       rs = prepstat.executeQuery();
/* 142:152 */       while (rs.next()) {
/* 143:153 */         makes.add(new PhoneMake(rs.getInt("id"), rs.getString("phone_manufacturer")));
/* 144:    */       }
/* 145:    */     }
/* 146:    */     catch (Exception ex)
/* 147:    */     {
/* 148:157 */       if (con != null) {
/* 149:158 */         con.close();
/* 150:    */       }
/* 151:160 */       throw new Exception(ex.getMessage());
/* 152:    */     }
/* 153:162 */     if (con != null) {
/* 154:163 */       con.close();
/* 155:    */     }
/* 156:166 */     return makes;
/* 157:    */   }
/* 158:    */   
/* 159:    */   public static void main(String[] args)
/* 160:    */     throws Exception
/* 161:    */   {}
/* 162:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.PhoneMakeDB
 * JD-Core Version:    0.7.0.1
 */