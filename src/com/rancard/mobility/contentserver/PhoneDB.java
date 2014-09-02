/*   1:    */ package com.rancard.mobility.contentserver;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import java.io.PrintStream;
/*   5:    */ import java.sql.Connection;
/*   6:    */ import java.sql.PreparedStatement;
/*   7:    */ import java.sql.ResultSet;
/*   8:    */ import java.util.ArrayList;
/*   9:    */ import java.util.List;
/*  10:    */ 
/*  11:    */ public abstract class PhoneDB
/*  12:    */ {
/*  13:    */   public static void insertPhone(String phoneModel, int phoneMake, String supportedFormats)
/*  14:    */     throws Exception
/*  15:    */   {
/*  16: 30 */     ResultSet rs = null;
/*  17: 31 */     Connection con = null;
/*  18: 32 */     PreparedStatement prepstat = null;
/*  19:    */     try
/*  20:    */     {
/*  21: 35 */       con = DConnect.getConnection();
/*  22: 36 */       String query = "INSERT into phone_list(phone_model,phone_make_id,supported_formats_id) values(?,?,?)";
/*  23:    */       
/*  24: 38 */       prepstat = con.prepareStatement(query);
/*  25: 39 */       prepstat.setString(1, phoneModel);
/*  26: 40 */       prepstat.setInt(2, phoneMake);
/*  27: 41 */       prepstat.setString(3, supportedFormats);
/*  28:    */       
/*  29: 43 */       prepstat.execute();
/*  30:    */     }
/*  31:    */     catch (Exception ex)
/*  32:    */     {
/*  33: 45 */       if (con != null) {
/*  34: 46 */         con.close();
/*  35:    */       }
/*  36: 48 */       System.out.println(ex.getMessage() + "\r\n\r\n" + ex.getCause());
/*  37: 49 */       throw new Exception(ex.getMessage());
/*  38:    */     }
/*  39: 51 */     if (con != null) {
/*  40: 52 */       con.close();
/*  41:    */     }
/*  42:    */   }
/*  43:    */   
/*  44:    */   public static void updatePhone(int id, String phoneModel, int phoneMake, String supportedFormats)
/*  45:    */     throws Exception
/*  46:    */   {
/*  47: 62 */     ResultSet rs = null;
/*  48: 63 */     Connection con = null;
/*  49: 64 */     PreparedStatement prepstat = null;
/*  50:    */     try
/*  51:    */     {
/*  52: 67 */       con = DConnect.getConnection();
/*  53: 68 */       String query = "UPDATE phone_list SET phone_model=?,phone_make_id=?,supported_formats_id=? WHERE phone_id=" + id;
/*  54:    */       
/*  55: 70 */       prepstat = con.prepareStatement(query);
/*  56: 71 */       prepstat.setString(1, phoneModel);
/*  57: 72 */       prepstat.setInt(2, phoneMake);
/*  58: 73 */       prepstat.setString(3, supportedFormats);
/*  59: 74 */       prepstat.execute();
/*  60:    */     }
/*  61:    */     catch (Exception ex)
/*  62:    */     {
/*  63: 76 */       if (con != null) {
/*  64: 77 */         con.close();
/*  65:    */       }
/*  66: 79 */       throw new Exception(ex.getMessage());
/*  67:    */     }
/*  68: 81 */     if (con != null) {
/*  69: 82 */       con.close();
/*  70:    */     }
/*  71:    */   }
/*  72:    */   
/*  73:    */   public static void deletePhone(int id)
/*  74:    */     throws Exception
/*  75:    */   {
/*  76: 91 */     ResultSet rs = null;
/*  77: 92 */     Connection con = null;
/*  78: 93 */     PreparedStatement prepstat = null;
/*  79:    */     try
/*  80:    */     {
/*  81: 96 */       con = DConnect.getConnection();
/*  82: 97 */       String query = "DELETE from phone_list WHERE phone_id=?";
/*  83: 98 */       prepstat = con.prepareStatement(query);
/*  84: 99 */       prepstat.setInt(1, id);
/*  85:100 */       prepstat.execute();
/*  86:    */     }
/*  87:    */     catch (Exception ex)
/*  88:    */     {
/*  89:102 */       if (con != null) {
/*  90:103 */         con.close();
/*  91:    */       }
/*  92:105 */       throw new Exception(ex.getMessage());
/*  93:    */     }
/*  94:107 */     if (con != null) {
/*  95:108 */       con.close();
/*  96:    */     }
/*  97:    */   }
/*  98:    */   
/*  99:    */   public static Phone viewPhone(int phoneId)
/* 100:    */     throws Exception
/* 101:    */   {
/* 102:116 */     Phone newBean = new Phone();
/* 103:    */     
/* 104:118 */     ResultSet rs = null;
/* 105:119 */     Connection con = null;
/* 106:120 */     PreparedStatement prepstat = null;
/* 107:    */     try
/* 108:    */     {
/* 109:123 */       con = DConnect.getConnection();
/* 110:124 */       String query = "SELECT * from phone_list WHERE phone_id=?";
/* 111:125 */       prepstat = con.prepareStatement(query);
/* 112:126 */       prepstat.setInt(1, phoneId);
/* 113:127 */       rs = prepstat.executeQuery();
/* 114:129 */       while (rs.next())
/* 115:    */       {
/* 116:130 */         newBean.setId(rs.getInt("phone_id"));
/* 117:131 */         newBean.setModel(rs.getString("phone_model"));
/* 118:132 */         newBean.setMake(rs.getInt("phone_make_id"));
/* 119:133 */         newBean.setSupportedFormats(rs.getString("supported_formats_id"));
/* 120:    */       }
/* 121:    */     }
/* 122:    */     catch (Exception ex)
/* 123:    */     {
/* 124:136 */       if (con != null) {
/* 125:137 */         con.close();
/* 126:    */       }
/* 127:139 */       throw new Exception(ex.getMessage());
/* 128:    */     }
/* 129:141 */     if (con != null) {
/* 130:142 */       con.close();
/* 131:    */     }
/* 132:144 */     return newBean;
/* 133:    */   }
/* 134:    */   
/* 135:    */   public static List getDistinctMakes(String criterion)
/* 136:    */     throws Exception
/* 137:    */   {
/* 138:149 */     List items = new ArrayList();
/* 139:    */     
/* 140:    */ 
/* 141:152 */     ResultSet rs = null;
/* 142:153 */     Connection con = null;
/* 143:154 */     PreparedStatement prepstat = null;
/* 144:    */     
/* 145:156 */     String queryEnding = "";
/* 146:158 */     if ((criterion != null) || (!criterion.equals(""))) {
/* 147:159 */       queryEnding = "where phone_make_id=" + criterion;
/* 148:    */     }
/* 149:    */     try
/* 150:    */     {
/* 151:163 */       con = DConnect.getConnection();
/* 152:164 */       String query = "SELECT * from phone_list " + queryEnding;
/* 153:165 */       prepstat = con.prepareStatement(query);
/* 154:166 */       rs = prepstat.executeQuery();
/* 155:168 */       while (rs.next()) {
/* 156:170 */         items.add(new Phone(rs.getInt("phone_id"), rs.getString("phone_model"), rs.getInt("phone_make_id"), rs.getString("supported_formats_id")));
/* 157:    */       }
/* 158:    */     }
/* 159:    */     catch (Exception ex)
/* 160:    */     {
/* 161:176 */       if (con != null) {
/* 162:177 */         con.close();
/* 163:    */       }
/* 164:179 */       throw new Exception(ex.getMessage());
/* 165:    */     }
/* 166:181 */     if (con != null) {
/* 167:182 */       con.close();
/* 168:    */     }
/* 169:185 */     return items;
/* 170:    */   }
/* 171:    */   
/* 172:    */   public static void main(String[] args)
/* 173:    */     throws Exception
/* 174:    */   {}
/* 175:    */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.PhoneDB
 * JD-Core Version:    0.7.0.1
 */