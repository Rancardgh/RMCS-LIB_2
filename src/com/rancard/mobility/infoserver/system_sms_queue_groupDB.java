/*   1:    */ package com.rancard.mobility.infoserver;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import java.sql.Connection;
/*   5:    */ import java.sql.PreparedStatement;
/*   6:    */ import java.sql.ResultSet;
/*   7:    */ import java.util.Vector;
/*   8:    */ 
/*   9:    */ public class system_sms_queue_groupDB
/*  10:    */ {
/*  11:    */   public void createsystem_sms_queue_group(Integer category_id, String category_name)
/*  12:    */     throws Exception
/*  13:    */   {
/*  14: 14 */     ResultSet rs = null;
/*  15: 15 */     Connection con = null;
/*  16: 16 */     PreparedStatement prepstat = null;
/*  17:    */     try
/*  18:    */     {
/*  19: 18 */       con = DConnect.getConnection();
/*  20:    */       
/*  21: 20 */       String SQL = "insert into system_sms_queue_group(category_id,category_name) values(?,?)";
/*  22: 21 */       prepstat = con.prepareStatement(SQL);
/*  23:    */       
/*  24: 23 */       prepstat.setInt(1, category_id.intValue());
/*  25:    */       
/*  26: 25 */       prepstat.setString(2, category_name);
/*  27: 26 */       prepstat.execute();
/*  28:    */     }
/*  29:    */     catch (Exception ex)
/*  30:    */     {
/*  31: 28 */       if (con != null) {
/*  32: 29 */         con.close();
/*  33:    */       }
/*  34: 30 */       throw new Exception(ex.getMessage());
/*  35:    */     }
/*  36: 32 */     if (con != null) {
/*  37: 33 */       con.close();
/*  38:    */     }
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void updatesystem_sms_queue_group(Integer category_id, String category_name)
/*  42:    */     throws Exception
/*  43:    */   {
/*  44: 38 */     ResultSet rs = null;
/*  45: 39 */     Connection con = null;
/*  46: 40 */     PreparedStatement prepstat = null;
/*  47:    */     try
/*  48:    */     {
/*  49: 42 */       con = DConnect.getConnection();
/*  50:    */       
/*  51:    */ 
/*  52: 45 */       String SQL = "update system_sms_queue_group set category_name=? where category_id=?";
/*  53: 46 */       prepstat = con.prepareStatement(SQL);
/*  54:    */       
/*  55: 48 */       prepstat.setString(1, category_name);
/*  56:    */       
/*  57:    */ 
/*  58: 51 */       prepstat.setInt(2, category_id.intValue());
/*  59: 52 */       prepstat.execute();
/*  60:    */     }
/*  61:    */     catch (Exception ex)
/*  62:    */     {
/*  63: 54 */       if (con != null) {
/*  64: 55 */         con.close();
/*  65:    */       }
/*  66: 56 */       throw new Exception(ex.getMessage());
/*  67:    */     }
/*  68: 58 */     if (con != null) {
/*  69: 59 */       con.close();
/*  70:    */     }
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void deletesystem_sms_queue_group(Integer category_id)
/*  74:    */     throws Exception
/*  75:    */   {
/*  76: 64 */     ResultSet rs = null;
/*  77: 65 */     Connection con = null;
/*  78: 66 */     PreparedStatement prepstat = null;
/*  79:    */     try
/*  80:    */     {
/*  81: 68 */       con = DConnect.getConnection();
/*  82: 69 */       String SQL = "delete from system_sms_queue_group where category_id=?";
/*  83: 70 */       prepstat = con.prepareStatement(SQL);
/*  84:    */       
/*  85: 72 */       prepstat.setInt(1, category_id.intValue());
/*  86: 73 */       prepstat.execute();
/*  87:    */     }
/*  88:    */     catch (Exception ex)
/*  89:    */     {
/*  90: 75 */       if (con != null) {
/*  91: 76 */         con.close();
/*  92:    */       }
/*  93: 77 */       throw new Exception(ex.getMessage());
/*  94:    */     }
/*  95: 79 */     if (con != null) {
/*  96: 80 */       con.close();
/*  97:    */     }
/*  98:    */   }
/*  99:    */   
/* 100:    */   public system_sms_queue_groupBean viewsystem_sms_queue_group(Integer category_id)
/* 101:    */     throws Exception
/* 102:    */   {
/* 103: 83 */     system_sms_queue_groupBean system_sms_queue_group = new system_sms_queue_groupBean();
/* 104:    */     
/* 105:    */ 
/* 106: 86 */     ResultSet rs = null;
/* 107: 87 */     Connection con = null;
/* 108: 88 */     PreparedStatement prepstat = null;
/* 109:    */     try
/* 110:    */     {
/* 111: 90 */       con = DConnect.getConnection();
/* 112:    */       
/* 113: 92 */       String SQL = "select * from  system_sms_queue_group where category_id=?";
/* 114: 93 */       prepstat = con.prepareStatement(SQL);
/* 115:    */       
/* 116: 95 */       prepstat.setInt(1, category_id.intValue());
/* 117: 96 */       rs = prepstat.executeQuery();
/* 118: 97 */       while (rs.next())
/* 119:    */       {
/* 120: 99 */         system_sms_queue_group.setcategory_id(new Integer(rs.getInt("category_id")));
/* 121:100 */         system_sms_queue_group.setcategory_name(rs.getString("category_name"));
/* 122:    */       }
/* 123:    */     }
/* 124:    */     catch (Exception ex)
/* 125:    */     {
/* 126:103 */       if (con != null) {
/* 127:104 */         con.close();
/* 128:    */       }
/* 129:105 */       throw new Exception(ex.getMessage());
/* 130:    */     }
/* 131:107 */     if (con != null) {
/* 132:108 */       con.close();
/* 133:    */     }
/* 134:109 */     return system_sms_queue_group;
/* 135:    */   }
/* 136:    */   
/* 137:    */   public Vector Query1system_sms_queue_group()
/* 138:    */     throws Exception
/* 139:    */   {
/* 140:115 */     Vector results = new Vector();
/* 141:    */     
/* 142:117 */     ResultSet rs = null;
/* 143:118 */     Connection con = null;
/* 144:119 */     PreparedStatement prepstat = null;
/* 145:    */     try
/* 146:    */     {
/* 147:121 */       con = DConnect.getConnection();
/* 148:122 */       String SQL = "select * from system_sms_queue_group order by category_id";
/* 149:123 */       prepstat = con.prepareStatement(SQL);
/* 150:    */       
/* 151:    */ 
/* 152:    */ 
/* 153:127 */       rs = prepstat.executeQuery();
/* 154:128 */       while (rs.next())
/* 155:    */       {
/* 156:129 */         system_sms_queue_groupBean system_sms_queue_group = new system_sms_queue_groupBean();
/* 157:    */         
/* 158:131 */         system_sms_queue_group.setcategory_id(new Integer(rs.getInt("category_id")));
/* 159:132 */         system_sms_queue_group.setcategory_name(rs.getString("category_name"));
/* 160:133 */         results.addElement(system_sms_queue_group);
/* 161:    */       }
/* 162:    */     }
/* 163:    */     catch (Exception ex)
/* 164:    */     {
/* 165:136 */       if (con != null) {
/* 166:137 */         con.close();
/* 167:    */       }
/* 168:138 */       throw new Exception(ex.getMessage());
/* 169:    */     }
/* 170:140 */     if (con != null) {
/* 171:141 */       con.close();
/* 172:    */     }
/* 173:142 */     return results;
/* 174:    */   }
/* 175:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.infoserver.system_sms_queue_groupDB
 * JD-Core Version:    0.7.0.1
 */