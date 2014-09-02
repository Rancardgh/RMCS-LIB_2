/*  1:   */ package com.rancard.common;
/*  2:   */ 
/*  3:   */ import java.sql.Connection;
/*  4:   */ import java.sql.SQLException;
/*  5:   */ import javax.naming.Context;
/*  6:   */ import javax.naming.InitialContext;
/*  7:   */ import javax.naming.NamingException;
/*  8:   */ import javax.sql.DataSource;
/*  9:   */ 
/* 10:   */ public class DConnect
/* 11:   */ {
/* 12: 9 */   private static Connection con = null;
/* 13:   */   
/* 14:   */   public static Connection getConnection()
/* 15:   */     throws Exception
/* 16:   */   {
/* 17:   */     try
/* 18:   */     {
/* 19:14 */       Class.forName("com.mysql.jdbc.Driver").newInstance();
/* 20:   */       
/* 21:   */ 
/* 22:17 */       con = getConnection("rmcs");
/* 23:   */     }
/* 24:   */     catch (Exception e)
/* 25:   */     {
/* 26:24 */       throw new Exception("Exception: Couldn't connect to the database " + e.getMessage());
/* 27:   */     }
/* 28:26 */     return con;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public static Connection getConnection(String datasource)
/* 32:   */     throws Exception
/* 33:   */   {
/* 34:31 */     Context ctx = null;
/* 35:32 */     DataSource ds = null;
/* 36:   */     try
/* 37:   */     {
/* 38:35 */       ctx = new InitialContext();
/* 39:36 */       ds = (DataSource)ctx.lookup("java:comp/env/jdbc/" + datasource);
/* 40:37 */       return ds.getConnection();
/* 41:   */     }
/* 42:   */     catch (NamingException e)
/* 43:   */     {
/* 44:40 */       throw new Exception(e.getMessage());
/* 45:   */     }
/* 46:   */     catch (SQLException e)
/* 47:   */     {
/* 48:42 */       throw new Exception(e.getMessage());
/* 49:   */     }
/* 50:   */     finally
/* 51:   */     {
/* 52:44 */       ctx = null;
/* 53:45 */       ds = null;
/* 54:   */     }
/* 55:   */   }
/* 56:   */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.common.DConnect
 * JD-Core Version:    0.7.0.1
 */