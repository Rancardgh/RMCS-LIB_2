/*  1:   */ package com.rancard.mobility.contentserver.serviceinterfaces.freemium;
/*  2:   */ 
/*  3:   */ import com.rancard.common.DConnect;
/*  4:   */ import java.sql.Connection;
/*  5:   */ import java.sql.ResultSet;
/*  6:   */ import java.sql.Statement;
/*  7:   */ import java.util.ArrayList;
/*  8:   */ import java.util.List;
/*  9:   */ 
/* 10:   */ public class SQLFreemiumDataSourceProvider
/* 11:   */   extends FreemiumDataSourceProviderImpl
/* 12:   */ {
/* 13:   */   private String column;
/* 14:   */   
/* 15:   */   public SQLFreemiumDataSourceProvider(String source, FreemiumDataSourceType type, String column)
/* 16:   */   {
/* 17:22 */     super(source, type);
/* 18:   */     
/* 19:24 */     this.column = column;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public List<String> getList()
/* 23:   */     throws Exception
/* 24:   */   {
/* 25:28 */     Connection conn = null;
/* 26:29 */     ResultSet rs = null;
/* 27:30 */     List<String> items = new ArrayList();
/* 28:31 */     boolean isColumnIndex = isNumber(this.column);
/* 29:   */     try
/* 30:   */     {
/* 31:34 */       conn = DConnect.getConnection();
/* 32:35 */       rs = conn.createStatement().executeQuery(getProviderSource());
/* 33:38 */       while (rs.next()) {
/* 34:39 */         if (isColumnIndex) {
/* 35:40 */           items.add(rs.getString(Integer.parseInt(this.column)));
/* 36:   */         } else {
/* 37:42 */           items.add(rs.getString(this.column));
/* 38:   */         }
/* 39:   */       }
/* 40:46 */       return items;
/* 41:   */     }
/* 42:   */     catch (Exception e)
/* 43:   */     {
/* 44:49 */       throw new Exception(e.getMessage());
/* 45:   */     }
/* 46:   */     finally
/* 47:   */     {
/* 48:51 */       if (conn != null) {
/* 49:52 */         conn.close();
/* 50:   */       }
/* 51:54 */       if (rs != null) {
/* 52:55 */         rs.close();
/* 53:   */       }
/* 54:   */     }
/* 55:   */   }
/* 56:   */   
/* 57:   */   private boolean isNumber(String str)
/* 58:   */   {
/* 59:61 */     return str.matches("-?\\d+(\\.\\d+)?");
/* 60:   */   }
/* 61:   */   
/* 62:   */   public boolean inProvider(String value)
/* 63:   */     throws Exception
/* 64:   */   {
/* 65:65 */     return getList().contains(value);
/* 66:   */   }
/* 67:   */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.serviceinterfaces.freemium.SQLFreemiumDataSourceProvider
 * JD-Core Version:    0.7.0.1
 */