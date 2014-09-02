/*   1:    */ package com.rancard.mobility.infoserver.viralmarketing;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import java.io.PrintStream;
/*   5:    */ import java.sql.Connection;
/*   6:    */ import java.sql.PreparedStatement;
/*   7:    */ import java.sql.ResultSet;
/*   8:    */ import java.sql.SQLException;
/*   9:    */ import java.sql.Timestamp;
/*  10:    */ import java.text.SimpleDateFormat;
/*  11:    */ import java.util.Date;
/*  12:    */ 
/*  13:    */ public class VMUserDB
/*  14:    */ {
/*  15:    */   public static void createUser(VMUser user)
/*  16:    */     throws Exception
/*  17:    */   {
/*  18: 21 */     ResultSet rs = null;
/*  19: 22 */     Connection con = null;
/*  20: 23 */     PreparedStatement prepstat = null;
/*  21:    */     try
/*  22:    */     {
/*  23: 26 */       con = DConnect.getConnection();
/*  24: 27 */       String SQL = "insert into vm_users(reg_date, account_id, keyword, msisdn, username, points) values(?, ?, ?, ?, ?, ?)";
/*  25:    */       
/*  26:    */ 
/*  27: 30 */       prepstat = con.prepareStatement(SQL);
/*  28:    */       
/*  29: 32 */       prepstat.setTimestamp(1, new Timestamp(new Date().getTime()));
/*  30: 33 */       prepstat.setString(2, user.getAccountId());
/*  31: 34 */       prepstat.setString(3, user.getKeyword());
/*  32: 35 */       prepstat.setString(4, user.getMsisdn());
/*  33: 36 */       prepstat.setString(5, user.getUsername());
/*  34: 37 */       prepstat.setInt(6, user.getPoints());
/*  35:    */       
/*  36: 39 */       prepstat.execute();
/*  37:    */     }
/*  38:    */     catch (Exception ex)
/*  39:    */     {
/*  40: 42 */       if (con != null)
/*  41:    */       {
/*  42:    */         try
/*  43:    */         {
/*  44: 44 */           con.close();
/*  45:    */         }
/*  46:    */         catch (SQLException ex1)
/*  47:    */         {
/*  48: 46 */           System.out.println(ex1.getMessage());
/*  49:    */         }
/*  50: 48 */         con = null;
/*  51:    */       }
/*  52:    */     }
/*  53:    */     finally
/*  54:    */     {
/*  55: 51 */       if (rs != null)
/*  56:    */       {
/*  57:    */         try
/*  58:    */         {
/*  59: 53 */           rs.close();
/*  60:    */         }
/*  61:    */         catch (SQLException e) {}
/*  62: 57 */         rs = null;
/*  63:    */       }
/*  64: 59 */       if (prepstat != null)
/*  65:    */       {
/*  66:    */         try
/*  67:    */         {
/*  68: 61 */           prepstat.close();
/*  69:    */         }
/*  70:    */         catch (SQLException e) {}
/*  71: 65 */         prepstat = null;
/*  72:    */       }
/*  73: 67 */       if (con != null)
/*  74:    */       {
/*  75:    */         try
/*  76:    */         {
/*  77: 69 */           con.close();
/*  78:    */         }
/*  79:    */         catch (SQLException e) {}
/*  80: 73 */         con = null;
/*  81:    */       }
/*  82:    */     }
/*  83:    */   }
/*  84:    */   
/*  85:    */   public static VMUser viewUser(String keyword, String accountId, String msisdn)
/*  86:    */     throws Exception
/*  87:    */   {
/*  88: 82 */     ResultSet rs = null;
/*  89: 83 */     Connection con = null;
/*  90: 84 */     PreparedStatement prepstat = null;
/*  91: 85 */     VMUser user = new VMUser();
/*  92:    */     try
/*  93:    */     {
/*  94: 88 */       con = DConnect.getConnection();
/*  95:    */       
/*  96: 90 */       String SQL = "select * from vm_users where keyword = ? and account_id = ? and msisdn = ?";
/*  97:    */       
/*  98: 92 */       prepstat = con.prepareStatement(SQL);
/*  99:    */       
/* 100: 94 */       prepstat.setString(1, keyword);
/* 101: 95 */       prepstat.setString(2, accountId);
/* 102: 96 */       prepstat.setString(3, msisdn);
/* 103:    */       
/* 104: 98 */       rs = prepstat.executeQuery();
/* 105:100 */       while (rs.next())
/* 106:    */       {
/* 107:101 */         user.setKeyword(rs.getString("keyword"));
/* 108:102 */         user.setAccountId(rs.getString("account_id"));
/* 109:103 */         user.setMsisdn(rs.getString("msisdn"));
/* 110:104 */         user.setUsername(rs.getString("username"));
/* 111:105 */         user.setPoints(rs.getInt("points"));
/* 112:106 */         SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/* 113:107 */         String regDate = df.format(new Date(rs.getTimestamp("reg_date").getTime()));
/* 114:108 */         user.setRegDate(regDate);
/* 115:    */       }
/* 116:    */     }
/* 117:    */     catch (Exception ex)
/* 118:    */     {
/* 119:112 */       if (con != null)
/* 120:    */       {
/* 121:    */         try
/* 122:    */         {
/* 123:114 */           con.close();
/* 124:    */         }
/* 125:    */         catch (SQLException ex1)
/* 126:    */         {
/* 127:116 */           System.out.println(ex1.getMessage());
/* 128:    */         }
/* 129:118 */         con = null;
/* 130:    */       }
/* 131:122 */       System.out.println(new Date() + ": error viewing vm_user (" + keyword + ", " + accountId + ", " + msisdn + "): " + ex.getMessage());
/* 132:    */     }
/* 133:    */     finally
/* 134:    */     {
/* 135:125 */       if (rs != null)
/* 136:    */       {
/* 137:    */         try
/* 138:    */         {
/* 139:127 */           rs.close();
/* 140:    */         }
/* 141:    */         catch (SQLException e) {}
/* 142:131 */         rs = null;
/* 143:    */       }
/* 144:133 */       if (prepstat != null)
/* 145:    */       {
/* 146:    */         try
/* 147:    */         {
/* 148:135 */           prepstat.close();
/* 149:    */         }
/* 150:    */         catch (SQLException e) {}
/* 151:139 */         prepstat = null;
/* 152:    */       }
/* 153:141 */       if (con != null)
/* 154:    */       {
/* 155:    */         try
/* 156:    */         {
/* 157:143 */           con.close();
/* 158:    */         }
/* 159:    */         catch (SQLException e) {}
/* 160:147 */         con = null;
/* 161:    */       }
/* 162:    */     }
/* 163:151 */     return user;
/* 164:    */   }
/* 165:    */   
/* 166:    */   public static void addPoints(String keyword, String accountId, String msisdn, int points)
/* 167:    */     throws Exception
/* 168:    */   {
/* 169:157 */     ResultSet rs = null;
/* 170:158 */     Connection con = null;
/* 171:159 */     PreparedStatement prepstat = null;
/* 172:    */     try
/* 173:    */     {
/* 174:162 */       con = DConnect.getConnection();
/* 175:163 */       String SQL = "UPDATE vm_users SET points = points + ? WHERE keyword = ?  and account_id =? and msisdn = ?";
/* 176:    */       
/* 177:    */ 
/* 178:    */ 
/* 179:167 */       prepstat = con.prepareStatement(SQL);
/* 180:    */       
/* 181:169 */       prepstat.setInt(1, points);
/* 182:170 */       prepstat.setString(2, keyword);
/* 183:171 */       prepstat.setString(3, accountId);
/* 184:172 */       prepstat.setString(4, msisdn);
/* 185:173 */       prepstat.execute();
/* 186:    */     }
/* 187:    */     catch (Exception ex)
/* 188:    */     {
/* 189:176 */       if (con != null)
/* 190:    */       {
/* 191:    */         try
/* 192:    */         {
/* 193:178 */           con.close();
/* 194:    */         }
/* 195:    */         catch (SQLException ex1)
/* 196:    */         {
/* 197:180 */           System.out.println(ex1.getMessage());
/* 198:    */         }
/* 199:182 */         con = null;
/* 200:    */       }
/* 201:    */     }
/* 202:    */     finally
/* 203:    */     {
/* 204:185 */       if (rs != null)
/* 205:    */       {
/* 206:    */         try
/* 207:    */         {
/* 208:187 */           rs.close();
/* 209:    */         }
/* 210:    */         catch (SQLException e) {}
/* 211:191 */         rs = null;
/* 212:    */       }
/* 213:193 */       if (prepstat != null)
/* 214:    */       {
/* 215:    */         try
/* 216:    */         {
/* 217:195 */           prepstat.close();
/* 218:    */         }
/* 219:    */         catch (SQLException e) {}
/* 220:199 */         prepstat = null;
/* 221:    */       }
/* 222:201 */       if (con != null)
/* 223:    */       {
/* 224:    */         try
/* 225:    */         {
/* 226:203 */           con.close();
/* 227:    */         }
/* 228:    */         catch (SQLException e) {}
/* 229:207 */         con = null;
/* 230:    */       }
/* 231:    */     }
/* 232:    */   }
/* 233:    */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.viralmarketing.VMUserDB
 * JD-Core Version:    0.7.0.1
 */