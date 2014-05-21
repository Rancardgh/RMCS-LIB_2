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
/* 19:17 */       con = getConnection("rmcs");
/* 20:   */     }
/* 21:   */     catch (Exception e)
/* 22:   */     {
/* 23:24 */       throw new Exception("Exception: Couldn't connect to the database " + e.getMessage());
/* 24:   */     }
/* 25:26 */     return con;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public static Connection getConnection(String datasource)
/* 29:   */     throws Exception
/* 30:   */   {
/* 31:31 */     Context ctx = null;
/* 32:32 */     DataSource ds = null;
/* 33:   */     try
/* 34:   */     {
/* 35:35 */       ctx = new InitialContext();
/* 36:36 */       ds = (DataSource)ctx.lookup("java:comp/env/jdbc/" + datasource);
/* 37:37 */       return ds.getConnection();
/* 38:   */     }
/* 39:   */     catch (NamingException e)
/* 40:   */     {
/* 41:40 */       throw new Exception(e.getMessage());
/* 42:   */     }
/* 43:   */     catch (SQLException e)
/* 44:   */     {
/* 45:42 */       throw new Exception(e.getMessage());
/* 46:   */     }
/* 47:   */     finally
/* 48:   */     {
/* 49:44 */       ctx = null;
/* 50:45 */       ds = null;
/* 51:   */     }
/* 52:   */   }
/* 53:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.common.DConnect
 * JD-Core Version:    0.7.0.1
 */