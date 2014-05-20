/*   1:    */ package com.rancard.common;
/*   2:    */ 
/*   3:    */ import java.io.PrintStream;
/*   4:    */ import java.sql.Connection;
/*   5:    */ import java.sql.ResultSet;
/*   6:    */ import java.sql.SQLException;
/*   7:    */ import java.sql.Statement;
/*   8:    */ import java.util.Date;
/*   9:    */ 
/*  10:    */ public class AddressBookDB
/*  11:    */ {
/*  12:    */   public static AddressBook getAddress(String accountID, String msisdn)
/*  13:    */     throws SQLException, Exception
/*  14:    */   {
/*  15: 19 */     Connection conn = null;
/*  16: 20 */     ResultSet rs = null;
/*  17: 21 */     AddressBook address = null;
/*  18:    */     try
/*  19:    */     {
/*  20: 24 */       conn = DConnect.getConnection();
/*  21:    */       
/*  22: 26 */       String sql = "SELECT * FROM address_book where account_id = '" + accountID + "' and msisdn = '" + msisdn + "'";
/*  23: 27 */       System.out.println(new Date() + ": " + AddressBookDB.class + ":DEBUG Get address: " + sql);
/*  24:    */       
/*  25: 29 */       rs = conn.createStatement().executeQuery(sql);
/*  26: 31 */       if (rs.next()) {
/*  27: 32 */         address = new AddressBook(rs.getString("account_id"), rs.getString("msisdn"), rs.getString("registration_id"));
/*  28:    */       }
/*  29: 34 */       return address;
/*  30:    */     }
/*  31:    */     catch (SQLException se)
/*  32:    */     {
/*  33: 36 */       System.out.println(new Date() + ": " + AddressBookDB.class + "ERROR: Checking if address exists: " + se.getMessage());
/*  34: 37 */       throw new SQLException(se.getMessage(), se.getSQLState());
/*  35:    */     }
/*  36:    */     catch (Exception e)
/*  37:    */     {
/*  38: 39 */       System.out.println(new Date() + ": " + AddressBookDB.class + "ERROR: Checking if address exists: " + e.getMessage());
/*  39: 40 */       throw new Exception(e.getMessage());
/*  40:    */     }
/*  41:    */     finally
/*  42:    */     {
/*  43: 42 */       if (conn != null) {
/*  44: 43 */         conn.close();
/*  45:    */       }
/*  46: 45 */       if (rs != null) {
/*  47: 46 */         rs.close();
/*  48:    */       }
/*  49:    */     }
/*  50:    */   }
/*  51:    */   
/*  52:    */   public static void save(AddressBook addressBook)
/*  53:    */     throws SQLException, Exception
/*  54:    */   {
/*  55: 52 */     Connection conn = null;
/*  56:    */     try
/*  57:    */     {
/*  58: 55 */       conn = DConnect.getConnection();
/*  59:    */       
/*  60: 57 */       String sql = "INSERT INTO address_book (account_id, msisdn, registration_id) VALUES ('" + addressBook.getAccountID() + "', '" + addressBook.getMsisdn() + "', '" + addressBook.getRegistrationID() + "')";
/*  61:    */       
/*  62:    */ 
/*  63: 60 */       System.out.println(new Date() + ": " + AddressBookDB.class + ":DEBUG Inserting address : " + sql);
/*  64: 61 */       conn.createStatement().execute(sql);
/*  65:    */     }
/*  66:    */     catch (SQLException se)
/*  67:    */     {
/*  68: 64 */       System.out.println(new Date() + ": " + AddressBookDB.class + "ERROR: Inserting address: " + se.getMessage());
/*  69: 65 */       throw new SQLException(se.getMessage(), se.getSQLState());
/*  70:    */     }
/*  71:    */     catch (Exception e)
/*  72:    */     {
/*  73: 67 */       System.out.println(new Date() + ": " + AddressBookDB.class + "ERROR: Inserting address: " + e.getMessage());
/*  74: 68 */       throw new Exception(e.getMessage());
/*  75:    */     }
/*  76:    */     finally
/*  77:    */     {
/*  78: 70 */       if (conn != null) {
/*  79: 71 */         conn.close();
/*  80:    */       }
/*  81:    */     }
/*  82:    */   }
/*  83:    */   
/*  84:    */   public static void delete(AddressBook addressBook)
/*  85:    */     throws SQLException, Exception
/*  86:    */   {
/*  87: 78 */     Connection conn = null;
/*  88:    */     try
/*  89:    */     {
/*  90: 81 */       String sql = "DELETE FROM address_book where account_id = '" + addressBook.getAccountID() + "' and msisdn = '" + addressBook.getMsisdn() + "'";
/*  91: 82 */       System.out.println(new Date() + ": " + AddressBookDB.class + ":DEBUG Deleting address : " + sql);
/*  92:    */       
/*  93: 84 */       conn = DConnect.getConnection();
/*  94: 85 */       conn.createStatement().execute(sql);
/*  95:    */     }
/*  96:    */     catch (SQLException se)
/*  97:    */     {
/*  98: 88 */       System.out.println(new Date() + ": " + AddressBookDB.class + "ERROR: Deleting address: " + se.getMessage());
/*  99: 89 */       throw new SQLException(se.getMessage(), se.getSQLState());
/* 100:    */     }
/* 101:    */     catch (Exception e)
/* 102:    */     {
/* 103: 91 */       System.out.println(new Date() + ": " + AddressBookDB.class + "ERROR: Deleting address: " + e.getMessage());
/* 104: 92 */       throw new Exception(e.getMessage());
/* 105:    */     }
/* 106:    */     finally
/* 107:    */     {
/* 108: 94 */       if (conn != null) {
/* 109: 95 */         conn.close();
/* 110:    */       }
/* 111:    */     }
/* 112:    */   }
/* 113:    */   
/* 114:    */   public static void update(AddressBook original, AddressBook update)
/* 115:    */     throws SQLException, Exception
/* 116:    */   {
/* 117:102 */     Connection conn = null;
/* 118:103 */     String sql = "UPDATE address_book SET account_id = '" + update.getAccountID() + "', msisdn = '" + update.getMsisdn() + "', registration_id = '" + update.getRegistrationID() + "' " + "where account_id = '" + original.getAccountID() + "' and msisdn = '" + original.getMsisdn() + "'";
/* 119:    */     
/* 120:105 */     System.out.println(new Date() + ": " + AddressBookDB.class + ":DEBUG Updating address : " + sql);
/* 121:    */     try
/* 122:    */     {
/* 123:107 */       conn = DConnect.getConnection();
/* 124:108 */       conn.createStatement().executeUpdate(sql);
/* 125:    */     }
/* 126:    */     catch (SQLException se)
/* 127:    */     {
/* 128:110 */       System.out.println(new Date() + ": " + AddressBookDB.class + "ERROR: Updating address: " + se.getMessage());
/* 129:111 */       throw new SQLException(se.getMessage(), se.getSQLState());
/* 130:    */     }
/* 131:    */     catch (Exception e)
/* 132:    */     {
/* 133:113 */       System.out.println(new Date() + ": " + AddressBookDB.class + "ERROR: Updating address: " + e.getMessage());
/* 134:114 */       throw new Exception(e.getMessage());
/* 135:    */     }
/* 136:    */     finally
/* 137:    */     {
/* 138:116 */       if (conn != null) {
/* 139:117 */         conn.close();
/* 140:    */       }
/* 141:    */     }
/* 142:    */   }
/* 143:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.common.AddressBookDB
 * JD-Core Version:    0.7.0.1
 */