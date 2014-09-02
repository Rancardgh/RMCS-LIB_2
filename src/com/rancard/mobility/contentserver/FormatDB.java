/*   1:    */ package com.rancard.mobility.contentserver;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import java.sql.Connection;
/*   5:    */ import java.sql.PreparedStatement;
/*   6:    */ import java.sql.ResultSet;
/*   7:    */ import java.util.ArrayList;
/*   8:    */ 
/*   9:    */ public abstract class FormatDB
/*  10:    */ {
/*  11:    */   public static void insertFormat(String fileExt, String push, String mimeType)
/*  12:    */     throws Exception
/*  13:    */   {
/*  14: 30 */     ResultSet rs = null;
/*  15: 31 */     Connection con = null;
/*  16: 32 */     PreparedStatement prepstat = null;
/*  17:    */     try
/*  18:    */     {
/*  19: 35 */       con = DConnect.getConnection();
/*  20: 36 */       String query = "INSERT into format_list(file_ext,push_bearer,mime_type) values(?,?,?)";
/*  21:    */       
/*  22:    */ 
/*  23: 39 */       prepstat = con.prepareStatement(query);
/*  24: 40 */       prepstat.setString(1, fileExt);
/*  25: 41 */       prepstat.setString(2, push);
/*  26: 42 */       prepstat.setString(3, mimeType);
/*  27:    */       
/*  28: 44 */       prepstat.execute();
/*  29:    */     }
/*  30:    */     catch (Exception ex)
/*  31:    */     {
/*  32: 46 */       if (con != null) {
/*  33: 47 */         con.close();
/*  34:    */       }
/*  35: 49 */       throw new Exception(ex.getMessage());
/*  36:    */     }
/*  37: 51 */     if (con != null) {
/*  38: 52 */       con.close();
/*  39:    */     }
/*  40:    */   }
/*  41:    */   
/*  42:    */   public static void updateFormat(int id, String fileExt, String push, String mimeType)
/*  43:    */     throws Exception
/*  44:    */   {
/*  45: 62 */     ResultSet rs = null;
/*  46: 63 */     Connection con = null;
/*  47: 64 */     PreparedStatement prepstat = null;
/*  48:    */     try
/*  49:    */     {
/*  50: 67 */       con = DConnect.getConnection();
/*  51: 68 */       String query = "UPDATE format_list SET file_ext=?,push_bearer=?,mime_type=? WHERE format_id=" + id;
/*  52:    */       
/*  53:    */ 
/*  54: 71 */       prepstat = con.prepareStatement(query);
/*  55: 72 */       prepstat.setString(1, fileExt);
/*  56: 73 */       prepstat.setString(2, push);
/*  57: 74 */       prepstat.setString(3, mimeType);
/*  58: 75 */       prepstat.execute();
/*  59:    */     }
/*  60:    */     catch (Exception ex)
/*  61:    */     {
/*  62: 77 */       if (con != null) {
/*  63: 78 */         con.close();
/*  64:    */       }
/*  65: 80 */       throw new Exception(ex.getMessage());
/*  66:    */     }
/*  67: 82 */     if (con != null) {
/*  68: 83 */       con.close();
/*  69:    */     }
/*  70:    */   }
/*  71:    */   
/*  72:    */   public static void deleteFormat(int id)
/*  73:    */     throws Exception
/*  74:    */   {
/*  75: 92 */     ResultSet rs = null;
/*  76: 93 */     Connection con = null;
/*  77: 94 */     PreparedStatement prepstat = null;
/*  78:    */     try
/*  79:    */     {
/*  80: 97 */       con = DConnect.getConnection();
/*  81: 98 */       String query = "DELETE from format_list WHERE format_id=?";
/*  82: 99 */       prepstat = con.prepareStatement(query);
/*  83:100 */       prepstat.setInt(1, id);
/*  84:101 */       prepstat.execute();
/*  85:    */     }
/*  86:    */     catch (Exception ex)
/*  87:    */     {
/*  88:103 */       if (con != null) {
/*  89:104 */         con.close();
/*  90:    */       }
/*  91:106 */       throw new Exception(ex.getMessage());
/*  92:    */     }
/*  93:108 */     if (con != null) {
/*  94:109 */       con.close();
/*  95:    */     }
/*  96:    */   }
/*  97:    */   
/*  98:    */   public static Format viewFormat(int formatId)
/*  99:    */     throws Exception
/* 100:    */   {
/* 101:117 */     Format newBean = new Format();
/* 102:    */     
/* 103:119 */     ResultSet rs = null;
/* 104:120 */     Connection con = null;
/* 105:121 */     PreparedStatement prepstat = null;
/* 106:    */     try
/* 107:    */     {
/* 108:124 */       con = DConnect.getConnection();
/* 109:125 */       String query = "SELECT * from format_list WHERE format_id=?";
/* 110:126 */       prepstat = con.prepareStatement(query);
/* 111:127 */       prepstat.setInt(1, formatId);
/* 112:128 */       rs = prepstat.executeQuery();
/* 113:130 */       while (rs.next())
/* 114:    */       {
/* 115:131 */         newBean.setId(rs.getInt("format_id"));
/* 116:132 */         newBean.setFileExt(rs.getString("file_ext"));
/* 117:133 */         newBean.setPushBearer(rs.getString("push_bearer"));
/* 118:134 */         newBean.setMimeType(rs.getString("mime_type"));
/* 119:    */       }
/* 120:    */     }
/* 121:    */     catch (Exception ex)
/* 122:    */     {
/* 123:137 */       if (con != null) {
/* 124:138 */         con.close();
/* 125:    */       }
/* 126:140 */       throw new Exception(ex.getMessage());
/* 127:    */     }
/* 128:142 */     if (con != null) {
/* 129:143 */       con.close();
/* 130:    */     }
/* 131:145 */     return newBean;
/* 132:    */   }
/* 133:    */   
/* 134:    */   public static ArrayList viewFormats()
/* 135:    */     throws Exception
/* 136:    */   {
/* 137:150 */     ArrayList formats = new ArrayList();
/* 138:    */     
/* 139:152 */     ResultSet rs = null;
/* 140:153 */     Connection con = null;
/* 141:154 */     PreparedStatement prepstat = null;
/* 142:    */     try
/* 143:    */     {
/* 144:157 */       con = DConnect.getConnection();
/* 145:158 */       String query = "SELECT * from format_list";
/* 146:159 */       prepstat = con.prepareStatement(query);
/* 147:160 */       rs = prepstat.executeQuery();
/* 148:162 */       while (rs.next())
/* 149:    */       {
/* 150:163 */         Format newBean = new Format();
/* 151:164 */         newBean.setId(rs.getInt("format_id"));
/* 152:165 */         newBean.setFileExt(rs.getString("file_ext"));
/* 153:166 */         newBean.setPushBearer(rs.getString("push_bearer"));
/* 154:167 */         newBean.setMimeType(rs.getString("mime_type"));
/* 155:168 */         formats.add(newBean);
/* 156:    */       }
/* 157:    */     }
/* 158:    */     catch (Exception ex)
/* 159:    */     {
/* 160:171 */       if (con != null) {
/* 161:172 */         con.close();
/* 162:    */       }
/* 163:174 */       throw new Exception(ex.getMessage());
/* 164:    */     }
/* 165:176 */     if (con != null) {
/* 166:177 */       con.close();
/* 167:    */     }
/* 168:179 */     return formats;
/* 169:    */   }
/* 170:    */   
/* 171:    */   public static void main(String[] args)
/* 172:    */     throws Exception
/* 173:    */   {}
/* 174:    */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.FormatDB
 * JD-Core Version:    0.7.0.1
 */