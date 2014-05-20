/*   1:    */ package com.rancard.mobility.contentserver.serviceinterfaces.freemium;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import java.sql.Connection;
/*   5:    */ import java.sql.ResultSet;
/*   6:    */ import java.sql.SQLException;
/*   7:    */ import java.sql.Statement;
/*   8:    */ import java.util.ArrayList;
/*   9:    */ import java.util.List;
/*  10:    */ 
/*  11:    */ class FreemiumDB
/*  12:    */ {
/*  13:    */   private static Freemium createFreemiumFromRS(ResultSet rs)
/*  14:    */     throws SQLException
/*  15:    */   {
/*  16: 21 */     return new Freemium(rs.getString("id"), rs.getString("accountID"), rs.getString("keyword"), rs.getInt("length"), rs.getDate("startDate"), rs.getDate("endDate"), rs.getBoolean("isActive"), FreemiumType.valueOf(rs.getString("type").trim().toUpperCase()), FreemiumRolloverOption.valueOf(rs.getString("rolloverOption").trim()));
/*  17:    */   }
/*  18:    */   
/*  19:    */   public static Freemium getFreemuim(String id)
/*  20:    */     throws SQLException, Exception
/*  21:    */   {
/*  22: 28 */     Connection conn = null;
/*  23: 29 */     ResultSet rs = null;
/*  24: 30 */     Freemium freemium = null;
/*  25: 31 */     String sql = "SELECT * FROM freemium where id = '" + id + "' LIMIT 1";
/*  26:    */     try
/*  27:    */     {
/*  28: 33 */       conn = DConnect.getConnection();
/*  29: 34 */       rs = conn.createStatement().executeQuery(sql);
/*  30: 36 */       if (rs.next()) {
/*  31: 37 */         freemium = createFreemiumFromRS(rs);
/*  32:    */       }
/*  33: 39 */       return freemium;
/*  34:    */     }
/*  35:    */     catch (SQLException se)
/*  36:    */     {
/*  37: 42 */       throw new SQLException(se.getMessage(), se.getSQLState());
/*  38:    */     }
/*  39:    */     catch (Exception e)
/*  40:    */     {
/*  41: 44 */       throw new Exception(e.getMessage());
/*  42:    */     }
/*  43:    */     finally
/*  44:    */     {
/*  45: 46 */       if (rs != null) {
/*  46: 47 */         rs.close();
/*  47:    */       }
/*  48: 49 */       if (conn != null) {
/*  49: 50 */         conn.close();
/*  50:    */       }
/*  51:    */     }
/*  52:    */   }
/*  53:    */   
/*  54:    */   public static Freemium getFreemuim(String id, Connection conn)
/*  55:    */     throws SQLException
/*  56:    */   {
/*  57: 57 */     ResultSet rs = null;
/*  58: 58 */     Freemium freemium = null;
/*  59: 59 */     String sql = "SELECT * FROM freemium where id = '" + id + "' LIMIT 1";
/*  60:    */     try
/*  61:    */     {
/*  62: 62 */       rs = conn.createStatement().executeQuery(sql);
/*  63: 64 */       if (rs.next()) {
/*  64: 65 */         freemium = createFreemiumFromRS(rs);
/*  65:    */       }
/*  66: 68 */       return freemium;
/*  67:    */     }
/*  68:    */     catch (SQLException se)
/*  69:    */     {
/*  70: 71 */       throw new SQLException(se.getMessage(), se.getSQLState());
/*  71:    */     }
/*  72:    */     finally
/*  73:    */     {
/*  74: 73 */       if (rs != null) {
/*  75: 74 */         rs.close();
/*  76:    */       }
/*  77:    */     }
/*  78:    */   }
/*  79:    */   
/*  80:    */   public static List<Freemium> getFreemuim(String accountID, String keyword)
/*  81:    */     throws SQLException, Exception
/*  82:    */   {
/*  83: 80 */     Connection conn = null;
/*  84: 81 */     ResultSet rs = null;
/*  85: 82 */     List<Freemium> freemiums = new ArrayList();
/*  86: 83 */     String sql = "SELECT * FROM freemium where accountID = '" + accountID + "' and keyword = '" + keyword + "'";
/*  87:    */     try
/*  88:    */     {
/*  89: 85 */       conn = DConnect.getConnection();
/*  90: 86 */       rs = conn.createStatement().executeQuery(sql);
/*  91: 88 */       while (rs.next()) {
/*  92: 89 */         freemiums.add(createFreemiumFromRS(rs));
/*  93:    */       }
/*  94: 91 */       return freemiums;
/*  95:    */     }
/*  96:    */     catch (SQLException se)
/*  97:    */     {
/*  98: 94 */       throw new SQLException(se.getMessage(), se.getSQLState());
/*  99:    */     }
/* 100:    */     catch (Exception e)
/* 101:    */     {
/* 102: 96 */       throw new Exception(e.getMessage());
/* 103:    */     }
/* 104:    */     finally
/* 105:    */     {
/* 106: 98 */       if (rs != null) {
/* 107: 99 */         rs.close();
/* 108:    */       }
/* 109:101 */       if (conn != null) {
/* 110:102 */         conn.close();
/* 111:    */       }
/* 112:    */     }
/* 113:    */   }
/* 114:    */   
/* 115:    */   public static List<Freemium> getFreemuim(String accountID, String keyword, boolean active)
/* 116:    */     throws SQLException, Exception
/* 117:    */   {
/* 118:108 */     Connection conn = null;
/* 119:109 */     ResultSet rs = null;
/* 120:110 */     List<Freemium> freemiums = new ArrayList();
/* 121:111 */     String sql = "SELECT * FROM freemium where accountID = '" + accountID + "' and keyword = '" + keyword + "'";
/* 122:    */     try
/* 123:    */     {
/* 124:113 */       conn = DConnect.getConnection();
/* 125:114 */       rs = conn.createStatement().executeQuery(sql);
/* 126:116 */       while (rs.next()) {
/* 127:117 */         if (rs.getBoolean("isActive") == active) {
/* 128:118 */           freemiums.add(createFreemiumFromRS(rs));
/* 129:    */         }
/* 130:    */       }
/* 131:122 */       return freemiums;
/* 132:    */     }
/* 133:    */     catch (SQLException se)
/* 134:    */     {
/* 135:125 */       throw new SQLException(se.getMessage(), se.getSQLState());
/* 136:    */     }
/* 137:    */     catch (Exception e)
/* 138:    */     {
/* 139:127 */       throw new Exception(e.getMessage());
/* 140:    */     }
/* 141:    */     finally
/* 142:    */     {
/* 143:129 */       if (rs != null) {
/* 144:130 */         rs.close();
/* 145:    */       }
/* 146:132 */       if (conn != null) {
/* 147:133 */         conn.close();
/* 148:    */       }
/* 149:    */     }
/* 150:    */   }
/* 151:    */   
/* 152:    */   public static List<Freemium> getFreemuim(String accountID, String keyword, Connection conn)
/* 153:    */     throws SQLException
/* 154:    */   {
/* 155:139 */     ResultSet rs = null;
/* 156:140 */     List<Freemium> freemiums = new ArrayList();
/* 157:141 */     String sql = "SELECT * FROM freemium where accountID = '" + accountID + "' and keyword = '" + keyword + "' LIMIT 1";
/* 158:    */     try
/* 159:    */     {
/* 160:144 */       rs = conn.createStatement().executeQuery(sql);
/* 161:146 */       while (rs.next()) {
/* 162:147 */         freemiums.add(createFreemiumFromRS(rs));
/* 163:    */       }
/* 164:150 */       return freemiums;
/* 165:    */     }
/* 166:    */     catch (SQLException se)
/* 167:    */     {
/* 168:153 */       throw new SQLException(se.getMessage(), se.getSQLState());
/* 169:    */     }
/* 170:    */     finally
/* 171:    */     {
/* 172:155 */       if (rs != null) {
/* 173:156 */         rs.close();
/* 174:    */       }
/* 175:    */     }
/* 176:    */   }
/* 177:    */   
/* 178:    */   public static List<FreemiumDataSource> getFreemuimDataSource(Freemium freemium)
/* 179:    */     throws SQLException, Exception
/* 180:    */   {
/* 181:162 */     ResultSet rs = null;
/* 182:163 */     List<FreemiumDataSource> dataSources = new ArrayList();
/* 183:164 */     Connection conn = null;
/* 184:165 */     String sql = "SELECT * FROM freemium_datasource where freemuimID = '" + freemium.getID() + "'";
/* 185:    */     try
/* 186:    */     {
/* 187:167 */       conn = DConnect.getConnection();
/* 188:168 */       rs = conn.createStatement().executeQuery(sql);
/* 189:170 */       while (rs.next()) {
/* 190:171 */         dataSources.add(new FreemiumDataSource(freemium, FreemiumDataSourceType.valueOf(rs.getString("sourceType").trim().toUpperCase()), rs.getString("source"), rs.getString("column"), rs.getString("delimeter"), rs.getBoolean("notin")));
/* 191:    */       }
/* 192:175 */       return dataSources;
/* 193:    */     }
/* 194:    */     catch (SQLException se)
/* 195:    */     {
/* 196:178 */       throw new SQLException(se.getMessage(), se.getSQLState());
/* 197:    */     }
/* 198:    */     catch (Exception e)
/* 199:    */     {
/* 200:180 */       throw new Exception(e.getMessage());
/* 201:    */     }
/* 202:    */     finally
/* 203:    */     {
/* 204:182 */       if (rs != null) {
/* 205:183 */         rs.close();
/* 206:    */       }
/* 207:185 */       if (conn != null) {
/* 208:186 */         conn.close();
/* 209:    */       }
/* 210:    */     }
/* 211:    */   }
/* 212:    */   
/* 213:    */   public static List<FreemiumDataSource> getFreemuimDataSource(Freemium freemium, Connection conn)
/* 214:    */     throws SQLException
/* 215:    */   {
/* 216:192 */     ResultSet rs = null;
/* 217:193 */     List<FreemiumDataSource> dataSources = new ArrayList();
/* 218:194 */     String sql = "SELECT * FROM freemium_datasource where freemuimID = '" + freemium.getID() + "'";
/* 219:    */     try
/* 220:    */     {
/* 221:197 */       rs = conn.createStatement().executeQuery(sql);
/* 222:199 */       while (rs.next()) {
/* 223:200 */         dataSources.add(new FreemiumDataSource(freemium, FreemiumDataSourceType.valueOf(rs.getString("sourceType").trim().toUpperCase()), rs.getString("source"), rs.getString("column"), rs.getString("delimiter"), rs.getBoolean("notin")));
/* 224:    */       }
/* 225:204 */       return dataSources;
/* 226:    */     }
/* 227:    */     catch (SQLException se)
/* 228:    */     {
/* 229:207 */       throw new SQLException(se.getMessage(), se.getSQLState());
/* 230:    */     }
/* 231:    */     finally
/* 232:    */     {
/* 233:209 */       if (rs != null) {
/* 234:210 */         rs.close();
/* 235:    */       }
/* 236:    */     }
/* 237:    */   }
/* 238:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.contentserver.serviceinterfaces.freemium.FreemiumDB
 * JD-Core Version:    0.7.0.1
 */