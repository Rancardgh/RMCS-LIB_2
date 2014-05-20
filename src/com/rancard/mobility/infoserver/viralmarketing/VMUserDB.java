/*  1:   */ package com.rancard.mobility.infoserver.viralmarketing;
/*  2:   */ 
/*  3:   */ import com.rancard.common.DConnect;
/*  4:   */ import com.rancard.util.DateUtil;
/*  5:   */ import java.io.PrintStream;
/*  6:   */ import java.sql.Connection;
/*  7:   */ import java.sql.ResultSet;
/*  8:   */ import java.sql.Statement;
/*  9:   */ import java.util.Date;
/* 10:   */ 
/* 11:   */ public class VMUserDB
/* 12:   */ {
/* 13:   */   public static void createUser(VMUser user)
/* 14:   */     throws Exception
/* 15:   */   {
/* 16:21 */     Connection conn = null;
/* 17:   */     try
/* 18:   */     {
/* 19:24 */       conn = DConnect.getConnection();
/* 20:25 */       String sql = "insert into vm_users(reg_date, account_id, keyword, msisdn, username, points) values('" + DateUtil.convertToMySQLTimeStamp(user.getRegDate()) + "', '" + user.getAccountId() + "', " + "'" + user.getKeyword() + "', '" + user.getMsisdn() + "', '" + user.getUsername() + "', " + user.getPoints() + ")";
/* 21:   */       
/* 22:   */ 
/* 23:28 */       System.out.println(new Date() + ": " + VMUser.class + ":DEBUG Creating vm user: " + sql);
/* 24:   */       
/* 25:30 */       conn.createStatement().execute(sql);
/* 26:   */     }
/* 27:   */     catch (Exception ex)
/* 28:   */     {
/* 29:33 */       System.out.println(new Date() + ": " + VMUser.class + ":ERROR Creating vm user: " + ex.getMessage());
/* 30:34 */       throw new Exception(ex.getMessage());
/* 31:   */     }
/* 32:   */     finally
/* 33:   */     {
/* 34:36 */       if (conn != null) {
/* 35:37 */         conn.close();
/* 36:   */       }
/* 37:   */     }
/* 38:   */   }
/* 39:   */   
/* 40:   */   public static VMUser viewUser(String keyword, String accountId, String msisdn)
/* 41:   */     throws Exception
/* 42:   */   {
/* 43:44 */     ResultSet rs = null;
/* 44:45 */     Connection conn = null;
/* 45:46 */     VMUser user = null;
/* 46:   */     try
/* 47:   */     {
/* 48:49 */       conn = DConnect.getConnection();
/* 49:   */       
/* 50:51 */       String sql = "select * from vm_users where keyword = '" + keyword + "' and account_id = '" + accountId + "' and msisdn = '" + msisdn + "'";
/* 51:52 */       System.out.println(new Date() + ": " + VMUser.class + ":DEBUG Selecting vm user: " + sql);
/* 52:   */       
/* 53:54 */       rs = conn.createStatement().executeQuery(sql);
/* 54:56 */       if (rs.next()) {
/* 55:58 */         user = new VMUser(rs.getString("msisdn"), rs.getString("account_id"), rs.getString("keyword"), rs.getString("username"), DateUtil.convertFromMySQLTimeStamp(rs.getString("reg_date")), rs.getInt("points"));
/* 56:   */       }
/* 57:63 */       return user;
/* 58:   */     }
/* 59:   */     catch (Exception ex)
/* 60:   */     {
/* 61:65 */       System.out.println(new Date() + ": " + VMUser.class + ":ERROR Creating vm user: " + ex.getMessage());
/* 62:66 */       throw new Exception(ex.getMessage());
/* 63:   */     }
/* 64:   */     finally
/* 65:   */     {
/* 66:68 */       if (rs != null) {
/* 67:69 */         rs.close();
/* 68:   */       }
/* 69:71 */       if (conn != null) {
/* 70:72 */         conn.close();
/* 71:   */       }
/* 72:   */     }
/* 73:   */   }
/* 74:   */   
/* 75:   */   public static void addPoints(String keyword, String accountId, String msisdn, int points)
/* 76:   */     throws Exception
/* 77:   */   {
/* 78:79 */     Connection conn = null;
/* 79:   */     try
/* 80:   */     {
/* 81:82 */       conn = DConnect.getConnection();
/* 82:83 */       String sql = "UPDATE vm_users SET points = points + " + points + " WHERE keyword = '" + keyword + "' and account_id = '" + accountId + "' and msisdn = '" + msisdn + "'";
/* 83:84 */       System.out.println(new Date() + ": " + VMUser.class + ":DEBUG Add poits to user: " + sql);
/* 84:   */       
/* 85:86 */       conn.createStatement().executeUpdate(sql);
/* 86:   */     }
/* 87:   */     catch (Exception ex)
/* 88:   */     {
/* 89:89 */       System.out.println(new Date() + ": " + VMUser.class + ":ERROR Creating vm user: " + ex.getMessage());
/* 90:90 */       throw new Exception(ex.getMessage());
/* 91:   */     }
/* 92:   */     finally
/* 93:   */     {
/* 94:92 */       if (conn != null) {
/* 95:93 */         conn.close();
/* 96:   */       }
/* 97:   */     }
/* 98:   */   }
/* 99:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.infoserver.viralmarketing.VMUserDB
 * JD-Core Version:    0.7.0.1
 */