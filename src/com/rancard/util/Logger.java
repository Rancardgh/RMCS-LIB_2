/*   1:    */ package com.rancard.util;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import java.io.PrintStream;
/*   5:    */ import java.sql.Connection;
/*   6:    */ import java.sql.PreparedStatement;
/*   7:    */ import java.sql.ResultSet;
/*   8:    */ import java.sql.Timestamp;
/*   9:    */ import java.util.Date;
/*  10:    */ 
/*  11:    */ public class Logger
/*  12:    */ {
/*  13:    */   public static void logServiceRequest(String keyword, String accountId, String origin, String dest, String timeSent, String msg, String siteId, String smscId)
/*  14:    */     throws Exception
/*  15:    */   {
/*  16: 19 */     ResultSet rs = null;
/*  17: 20 */     Connection con = null;
/*  18: 21 */     PreparedStatement prepstat = null;
/*  19:    */     
/*  20:    */ 
/*  21: 24 */     System.out.println(new Date() + ": updating service_usage_log with request details..");
/*  22: 25 */     System.out.println(new Date() + ": service_usage_log details: " + "keyword=" + keyword + ", " + "account_id=" + accountId + ", " + "origin=" + origin + ", " + "dest=" + dest + ", " + "timeSent=" + ("".equals(timeSent) ? new Date() : timeSent) + ", " + "msg=" + msg + ", " + "siteId=" + siteId + ", " + "smsc=" + smscId);
/*  23:    */     try
/*  24:    */     {
/*  25: 36 */       con = DConnect.getConnection();
/*  26:    */       
/*  27: 38 */       String query = "insert into service_usage_log (keyword, account_id, to_number, time_received, from_number, msg, site_id, smscId, time_sent) values (?,?,?,?,?,?,?,?,?)";
/*  28:    */       
/*  29: 40 */       prepstat = con.prepareStatement(query);
/*  30:    */       
/*  31: 42 */       prepstat.setString(1, keyword);
/*  32: 43 */       prepstat.setString(2, accountId);
/*  33: 44 */       prepstat.setString(3, dest);
/*  34: 45 */       prepstat.setTimestamp(4, new Timestamp(new Date().getTime()));
/*  35: 46 */       prepstat.setString(5, origin);
/*  36: 47 */       prepstat.setString(6, msg);
/*  37: 48 */       prepstat.setString(7, siteId);
/*  38: 49 */       prepstat.setString(8, smscId);
/*  39:    */       try
/*  40:    */       {
/*  41: 51 */         prepstat.setTimestamp(9, Timestamp.valueOf(timeSent));
/*  42:    */       }
/*  43:    */       catch (Exception e)
/*  44:    */       {
/*  45: 53 */         prepstat.setTimestamp(9, new Timestamp(new Date().getTime()));
/*  46:    */       }
/*  47: 57 */       prepstat.execute();
/*  48:    */       
/*  49:    */ 
/*  50: 60 */       System.out.println(new Date() + ": service_usage_log updated successfully!");
/*  51:    */     }
/*  52:    */     catch (Exception ex)
/*  53:    */     {
/*  54: 64 */       if (con != null)
/*  55:    */       {
/*  56:    */         try
/*  57:    */         {
/*  58: 66 */           con.close();
/*  59:    */         }
/*  60:    */         catch (Exception ex1)
/*  61:    */         {
/*  62: 68 */           System.out.println(ex.getMessage());
/*  63:    */         }
/*  64: 70 */         con = null;
/*  65:    */       }
/*  66: 73 */       System.out.println(new Date() + ": error updating service_usage_log: " + ex.getMessage());
/*  67:    */     }
/*  68:    */     finally
/*  69:    */     {
/*  70: 76 */       if (rs != null)
/*  71:    */       {
/*  72:    */         try
/*  73:    */         {
/*  74: 78 */           rs.close();
/*  75:    */         }
/*  76:    */         catch (Exception e)
/*  77:    */         {
/*  78: 80 */           System.out.println(e.getMessage());
/*  79:    */         }
/*  80: 82 */         rs = null;
/*  81:    */       }
/*  82: 84 */       if (prepstat != null)
/*  83:    */       {
/*  84:    */         try
/*  85:    */         {
/*  86: 86 */           prepstat.close();
/*  87:    */         }
/*  88:    */         catch (Exception e)
/*  89:    */         {
/*  90: 88 */           System.out.println(e.getMessage());
/*  91:    */         }
/*  92: 90 */         prepstat = null;
/*  93:    */       }
/*  94: 92 */       if (con != null)
/*  95:    */       {
/*  96:    */         try
/*  97:    */         {
/*  98: 94 */           con.close();
/*  99:    */         }
/* 100:    */         catch (Exception e)
/* 101:    */         {
/* 102: 96 */           System.out.println(e.getMessage());
/* 103:    */         }
/* 104: 98 */         con = null;
/* 105:    */       }
/* 106:    */     }
/* 107:    */   }
/* 108:    */   
/* 109:    */   public static void logServiceRequest(String keyword, String accountId, String origin, String dest, String timeSent, String msg, String siteId, String smscId, String promoId)
/* 110:    */     throws Exception
/* 111:    */   {
/* 112:107 */     ResultSet rs = null;
/* 113:108 */     Connection con = null;
/* 114:109 */     PreparedStatement prepstat = null;
/* 115:    */     
/* 116:    */ 
/* 117:112 */     System.out.println(new Date() + ": updating service_usage_log with request details..");
/* 118:113 */     System.out.println(new Date() + ": service_usage_log details: " + "keyword=" + keyword + ", " + "account_id=" + accountId + ", " + "origin=" + origin + ", " + "dest=" + dest + ", " + "timeSent=" + ("".equals(timeSent) ? new Date() : timeSent) + ", " + "msg=" + msg + ", " + "promoId=" + promoId + ", " + "siteId=" + siteId + ", " + "smsc=" + smscId);
/* 119:    */     try
/* 120:    */     {
/* 121:125 */       con = DConnect.getConnection();
/* 122:    */       
/* 123:127 */       String query = "insert into service_usage_log (keyword, account_id, to_number, time_received, from_number, msg, site_id, smscId, time_sent,promo_id) values (?,?,?,?,?,?,?,?,?,?)";
/* 124:    */       
/* 125:129 */       prepstat = con.prepareStatement(query);
/* 126:    */       
/* 127:131 */       prepstat.setString(1, keyword);
/* 128:132 */       prepstat.setString(2, accountId);
/* 129:133 */       prepstat.setString(3, dest);
/* 130:134 */       prepstat.setTimestamp(4, new Timestamp(new Date().getTime()));
/* 131:135 */       prepstat.setString(5, origin);
/* 132:136 */       prepstat.setString(6, msg);
/* 133:137 */       prepstat.setString(7, siteId);
/* 134:138 */       prepstat.setString(8, smscId);
/* 135:    */       try
/* 136:    */       {
/* 137:140 */         prepstat.setTimestamp(9, Timestamp.valueOf(timeSent));
/* 138:    */       }
/* 139:    */       catch (Exception e)
/* 140:    */       {
/* 141:142 */         prepstat.setTimestamp(9, new Timestamp(new Date().getTime()));
/* 142:    */       }
/* 143:144 */       prepstat.setString(10, promoId);
/* 144:    */       
/* 145:146 */       prepstat.execute();
/* 146:    */       
/* 147:    */ 
/* 148:149 */       System.out.println(new Date() + ": service_usage_log updated successfully!");
/* 149:    */     }
/* 150:    */     catch (Exception ex)
/* 151:    */     {
/* 152:153 */       if (con != null)
/* 153:    */       {
/* 154:    */         try
/* 155:    */         {
/* 156:155 */           con.close();
/* 157:    */         }
/* 158:    */         catch (Exception ex1)
/* 159:    */         {
/* 160:157 */           System.out.println(ex.getMessage());
/* 161:    */         }
/* 162:159 */         con = null;
/* 163:    */       }
/* 164:162 */       System.out.println(new Date() + ": error updating service_usage_log: " + ex.getMessage());
/* 165:    */     }
/* 166:    */     finally
/* 167:    */     {
/* 168:165 */       if (rs != null)
/* 169:    */       {
/* 170:    */         try
/* 171:    */         {
/* 172:167 */           rs.close();
/* 173:    */         }
/* 174:    */         catch (Exception e)
/* 175:    */         {
/* 176:169 */           System.out.println(e.getMessage());
/* 177:    */         }
/* 178:171 */         rs = null;
/* 179:    */       }
/* 180:173 */       if (prepstat != null)
/* 181:    */       {
/* 182:    */         try
/* 183:    */         {
/* 184:175 */           prepstat.close();
/* 185:    */         }
/* 186:    */         catch (Exception e)
/* 187:    */         {
/* 188:177 */           System.out.println(e.getMessage());
/* 189:    */         }
/* 190:179 */         prepstat = null;
/* 191:    */       }
/* 192:181 */       if (con != null)
/* 193:    */       {
/* 194:    */         try
/* 195:    */         {
/* 196:183 */           con.close();
/* 197:    */         }
/* 198:    */         catch (Exception e)
/* 199:    */         {
/* 200:185 */           System.out.println(e.getMessage());
/* 201:    */         }
/* 202:187 */         con = null;
/* 203:    */       }
/* 204:    */     }
/* 205:    */   }
/* 206:    */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.util.Logger
 * JD-Core Version:    0.7.0.1
 */