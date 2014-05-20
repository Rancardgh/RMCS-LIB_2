/*   1:    */ package com.rancard.security;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import java.sql.Connection;
/*   5:    */ import java.sql.Date;
/*   6:    */ import java.sql.PreparedStatement;
/*   7:    */ import java.sql.ResultSet;
/*   8:    */ import java.sql.SQLException;
/*   9:    */ import java.util.HashMap;
/*  10:    */ 
/*  11:    */ public class rolesDB
/*  12:    */ {
/*  13:    */   public String getroleName(String id)
/*  14:    */     throws Exception
/*  15:    */   {
/*  16: 22 */     String roleName = "";
/*  17:    */     
/*  18:    */ 
/*  19: 25 */     ResultSet rs = null;
/*  20: 26 */     Connection con = null;
/*  21: 27 */     PreparedStatement prepstat = null;
/*  22:    */     try
/*  23:    */     {
/*  24: 30 */       con = DConnect.getConnection();
/*  25: 31 */       Date tempdate = null;
/*  26:    */       
/*  27: 33 */       String SQL = "Select r.role from  roles r where  r.role_ID=?";
/*  28:    */       
/*  29:    */ 
/*  30:    */ 
/*  31:    */ 
/*  32: 38 */       prepstat = con.prepareStatement(SQL);
/*  33: 39 */       prepstat.setString(1, id);
/*  34: 40 */       rs = prepstat.executeQuery();
/*  35: 41 */       while (rs.next()) {
/*  36: 42 */         roleName = rs.getString("role");
/*  37:    */       }
/*  38: 46 */       rs.close();
/*  39: 47 */       rs = null;
/*  40: 48 */       prepstat.close();
/*  41: 49 */       prepstat = null;
/*  42: 50 */       con.close();
/*  43: 51 */       con = null;
/*  44:    */     }
/*  45:    */     catch (Exception ex)
/*  46:    */     {
/*  47: 54 */       if (con != null)
/*  48:    */       {
/*  49: 55 */         con.close();
/*  50: 56 */         con = null;
/*  51:    */       }
/*  52: 58 */       throw new Exception(ex.getMessage());
/*  53:    */     }
/*  54:    */     finally
/*  55:    */     {
/*  56: 61 */       if (rs != null)
/*  57:    */       {
/*  58:    */         try
/*  59:    */         {
/*  60: 63 */           rs.close();
/*  61:    */         }
/*  62:    */         catch (SQLException e) {}
/*  63: 68 */         rs = null;
/*  64:    */       }
/*  65: 70 */       if (prepstat != null)
/*  66:    */       {
/*  67:    */         try
/*  68:    */         {
/*  69: 72 */           prepstat.close();
/*  70:    */         }
/*  71:    */         catch (SQLException e) {}
/*  72: 77 */         prepstat = null;
/*  73:    */       }
/*  74: 79 */       if (con != null)
/*  75:    */       {
/*  76:    */         try
/*  77:    */         {
/*  78: 81 */           con.close();
/*  79:    */         }
/*  80:    */         catch (SQLException e) {}
/*  81: 86 */         con = null;
/*  82:    */       }
/*  83:    */     }
/*  84: 90 */     return roleName;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public roles viewRoles(String role_id)
/*  88:    */     throws Exception
/*  89:    */   {
/*  90: 94 */     roles roleType = new roles();
/*  91:    */     
/*  92:    */ 
/*  93: 97 */     ResultSet rs = null;
/*  94: 98 */     Connection con = null;
/*  95: 99 */     PreparedStatement prepstat = null;
/*  96:    */     try
/*  97:    */     {
/*  98:102 */       con = DConnect.getConnection();
/*  99:103 */       Date tempdate = null;
/* 100:    */       
/* 101:105 */       String SQL = "Select r.* from  roles r where  r.role_ID=?";
/* 102:    */       
/* 103:    */ 
/* 104:    */ 
/* 105:    */ 
/* 106:110 */       prepstat = con.prepareStatement(SQL);
/* 107:111 */       prepstat.setString(1, role_id);
/* 108:112 */       rs = prepstat.executeQuery();
/* 109:113 */       while (rs.next())
/* 110:    */       {
/* 111:114 */         roleType.setRole_id(rs.getString("role_ID"));
/* 112:115 */         roleType.setRole_name(rs.getString("role"));
/* 113:116 */         roleType.setManageUser(rs.getString("canManageUsers"));
/* 114:117 */         roleType.setMenu1(rs.getString("canViewMenu1"));
/* 115:118 */         roleType.setMenu2(rs.getString("canViewMenu2"));
/* 116:119 */         roleType.setCreateFolder(rs.getString("canCreateFolder"));
/* 117:120 */         roleType.setDeleteFolder(rs.getString("canDeleteFolder"));
/* 118:121 */         roleType.setManageTemplate(rs.getString("canManageTemplate"));
/* 119:122 */         roleType.setMenu3(rs.getString("canViewMenu3"));
/* 120:123 */         roleType.setMenu4(rs.getString("canViewMenu4"));
/* 121:124 */         roleType.setMenu5(rs.getString("canViewMenu5"));
/* 122:125 */         roleType.setMenu6(rs.getString("canViewMenu6"));
/* 123:    */       }
/* 124:128 */       rs.close();
/* 125:129 */       rs = null;
/* 126:130 */       prepstat.close();
/* 127:131 */       prepstat = null;
/* 128:132 */       con.close();
/* 129:133 */       con = null;
/* 130:    */     }
/* 131:    */     catch (Exception ex)
/* 132:    */     {
/* 133:136 */       if (con != null)
/* 134:    */       {
/* 135:137 */         con.close();
/* 136:138 */         con = null;
/* 137:    */       }
/* 138:140 */       throw new Exception(ex.getMessage());
/* 139:    */     }
/* 140:    */     finally
/* 141:    */     {
/* 142:143 */       if (rs != null)
/* 143:    */       {
/* 144:    */         try
/* 145:    */         {
/* 146:145 */           rs.close();
/* 147:    */         }
/* 148:    */         catch (SQLException e) {}
/* 149:150 */         rs = null;
/* 150:    */       }
/* 151:152 */       if (prepstat != null)
/* 152:    */       {
/* 153:    */         try
/* 154:    */         {
/* 155:154 */           prepstat.close();
/* 156:    */         }
/* 157:    */         catch (SQLException e) {}
/* 158:159 */         prepstat = null;
/* 159:    */       }
/* 160:161 */       if (con != null)
/* 161:    */       {
/* 162:    */         try
/* 163:    */         {
/* 164:163 */           con.close();
/* 165:    */         }
/* 166:    */         catch (SQLException e) {}
/* 167:168 */         con = null;
/* 168:    */       }
/* 169:    */     }
/* 170:172 */     return roleType;
/* 171:    */   }
/* 172:    */   
/* 173:    */   public HashMap getAllRoles()
/* 174:    */     throws Exception
/* 175:    */   {
/* 176:179 */     HashMap results = new HashMap();
/* 177:    */     
/* 178:181 */     ResultSet rs = null;
/* 179:182 */     Connection con = null;
/* 180:183 */     PreparedStatement prepstat = null;
/* 181:    */     try
/* 182:    */     {
/* 183:185 */       con = DConnect.getConnection();
/* 184:186 */       String SQL = "select * from roles ";
/* 185:187 */       prepstat = con.prepareStatement(SQL);
/* 186:    */       
/* 187:    */ 
/* 188:    */ 
/* 189:191 */       rs = prepstat.executeQuery();
/* 190:192 */       while (rs.next())
/* 191:    */       {
/* 192:193 */         roles roleType = new roles();
/* 193:    */         
/* 194:195 */         roleType.setRole_id(rs.getString("role_ID"));
/* 195:    */         
/* 196:197 */         results.put(roleType.getRole_id(), roleType);
/* 197:    */       }
/* 198:    */     }
/* 199:    */     catch (Exception ex)
/* 200:    */     {
/* 201:201 */       if (con != null) {
/* 202:202 */         con.close();
/* 203:    */       }
/* 204:204 */       throw new Exception(ex.getMessage());
/* 205:    */     }
/* 206:206 */     if (con != null) {
/* 207:207 */       con.close();
/* 208:    */     }
/* 209:209 */     return results;
/* 210:    */   }
/* 211:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.security.rolesDB
 * JD-Core Version:    0.7.0.1
 */