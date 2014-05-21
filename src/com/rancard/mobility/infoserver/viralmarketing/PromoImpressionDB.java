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
/*  13:    */ public class PromoImpressionDB
/*  14:    */ {
/*  15:    */   public static void createEntry(PromoImpression impression)
/*  16:    */     throws Exception
/*  17:    */   {
/*  18: 22 */     ResultSet rs = null;
/*  19: 23 */     Connection con = null;
/*  20: 24 */     PreparedStatement prepstat = null;
/*  21:    */     try
/*  22:    */     {
/*  23: 27 */       con = DConnect.getConnection();
/*  24: 28 */       String SQL = "insert into promo_impression_tracker (hash_code, msisdn, account_id, promo_response_code, view_date, inventory_keyword) values(?, ?, ?, ?, ?, ?)";
/*  25:    */       
/*  26:    */ 
/*  27: 31 */       prepstat = con.prepareStatement(SQL);
/*  28:    */       
/*  29: 33 */       prepstat.setLong(1, impression.getHashCode());
/*  30: 34 */       prepstat.setString(2, impression.getMsisdn());
/*  31: 35 */       prepstat.setString(3, impression.getAccountId());
/*  32: 36 */       prepstat.setString(4, impression.getKeyword());
/*  33: 37 */       prepstat.setTimestamp(5, new Timestamp(impression.getViewDate().getTime()));
/*  34: 38 */       prepstat.setString(6, impression.getInventory_keyword());
/*  35:    */       
/*  36: 40 */       prepstat.execute();
/*  37:    */     }
/*  38:    */     catch (Exception ex)
/*  39:    */     {
/*  40: 43 */       if (con != null)
/*  41:    */       {
/*  42:    */         try
/*  43:    */         {
/*  44: 45 */           con.close();
/*  45:    */         }
/*  46:    */         catch (SQLException ex1)
/*  47:    */         {
/*  48: 47 */           System.out.println(ex1.getMessage());
/*  49:    */         }
/*  50: 49 */         con = null;
/*  51:    */       }
/*  52: 51 */       System.out.println(new Date() + ": error creating promo_campaign_hash entry(" + impression.getHashCode() + "): " + ex.getMessage());
/*  53:    */     }
/*  54:    */     finally
/*  55:    */     {
/*  56: 53 */       if (rs != null)
/*  57:    */       {
/*  58:    */         try
/*  59:    */         {
/*  60: 55 */           rs.close();
/*  61:    */         }
/*  62:    */         catch (SQLException e) {}
/*  63: 59 */         rs = null;
/*  64:    */       }
/*  65: 61 */       if (prepstat != null)
/*  66:    */       {
/*  67:    */         try
/*  68:    */         {
/*  69: 63 */           prepstat.close();
/*  70:    */         }
/*  71:    */         catch (SQLException e) {}
/*  72: 67 */         prepstat = null;
/*  73:    */       }
/*  74: 69 */       if (con != null)
/*  75:    */       {
/*  76:    */         try
/*  77:    */         {
/*  78: 71 */           con.close();
/*  79:    */         }
/*  80:    */         catch (SQLException e) {}
/*  81: 75 */         con = null;
/*  82:    */       }
/*  83:    */     }
/*  84:    */   }
/*  85:    */   
/*  86:    */   public static PromoImpression viewPromoImpression(long hashCode)
/*  87:    */     throws Exception
/*  88:    */   {
/*  89: 84 */     ResultSet rs = null;
/*  90: 85 */     Connection con = null;
/*  91: 86 */     PreparedStatement prepstat = null;
/*  92: 87 */     PromoImpression impression = new PromoImpression();
/*  93:    */     try
/*  94:    */     {
/*  95: 90 */       con = DConnect.getConnection();
/*  96:    */       
/*  97: 92 */       String SQL = "select * from promo_impression_tracker where hash_code = ?";
/*  98:    */       
/*  99: 94 */       prepstat = con.prepareStatement(SQL);
/* 100:    */       
/* 101: 96 */       prepstat.setLong(1, hashCode);
/* 102:    */       
/* 103: 98 */       rs = prepstat.executeQuery();
/* 104:100 */       while (rs.next())
/* 105:    */       {
/* 106:101 */         impression.setHashCode(rs.getLong("hash_code"));
/* 107:102 */         impression.setAccountId(rs.getString("account_id"));
/* 108:103 */         impression.setMsisdn(rs.getString("msisdn"));
/* 109:104 */         impression.setKeyword(rs.getString("promo_response_code"));
/* 110:105 */         impression.setViewDate(new Date(rs.getTimestamp("view_date").getTime()));
/* 111:106 */         impression.setInventory_keyword(rs.getString("inventory_keyword"));
/* 112:    */       }
/* 113:    */     }
/* 114:    */     catch (Exception ex)
/* 115:    */     {
/* 116:110 */       if (con != null)
/* 117:    */       {
/* 118:    */         try
/* 119:    */         {
/* 120:112 */           con.close();
/* 121:    */         }
/* 122:    */         catch (SQLException ex1)
/* 123:    */         {
/* 124:114 */           System.out.println(ex1.getMessage());
/* 125:    */         }
/* 126:116 */         con = null;
/* 127:    */       }
/* 128:120 */       System.out.println(new Date() + ": error viewing promo_campaign_hash (" + hashCode + "): " + ex.getMessage());
/* 129:    */     }
/* 130:    */     finally
/* 131:    */     {
/* 132:123 */       if (rs != null)
/* 133:    */       {
/* 134:    */         try
/* 135:    */         {
/* 136:125 */           rs.close();
/* 137:    */         }
/* 138:    */         catch (SQLException e) {}
/* 139:129 */         rs = null;
/* 140:    */       }
/* 141:131 */       if (prepstat != null)
/* 142:    */       {
/* 143:    */         try
/* 144:    */         {
/* 145:133 */           prepstat.close();
/* 146:    */         }
/* 147:    */         catch (SQLException e) {}
/* 148:137 */         prepstat = null;
/* 149:    */       }
/* 150:139 */       if (con != null)
/* 151:    */       {
/* 152:    */         try
/* 153:    */         {
/* 154:141 */           con.close();
/* 155:    */         }
/* 156:    */         catch (SQLException e) {}
/* 157:145 */         con = null;
/* 158:    */       }
/* 159:    */     }
/* 160:149 */     return impression;
/* 161:    */   }
/* 162:    */   
/* 163:    */   public static synchronized void updateAdResponseSummary(PromoImpression impression)
/* 164:    */     throws Exception
/* 165:    */   {
/* 166:154 */     ResultSet rs = null;
/* 167:155 */     Connection con = null;
/* 168:156 */     PreparedStatement prepstat = null;
/* 169:    */     
/* 170:158 */     Date dt = new Date();
/* 171:159 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/* 172:160 */     String currDate = sdf.format(dt);
/* 173:    */     try
/* 174:    */     {
/* 175:163 */       con = DConnect.getConnection();
/* 176:164 */       String SQL = "select promo_hits from ad_response_summary where account_id ='" + impression.getAccountId() + "' and keyword='" + impression.getInventory_keyword() + "'" + " and promo_keyword = '" + impression.getKeyword() + "' and log_date = '" + currDate + "'";
/* 177:    */       
/* 178:166 */       prepstat = con.prepareStatement(SQL);
/* 179:167 */       rs = prepstat.executeQuery();
/* 180:169 */       if (!rs.next())
/* 181:    */       {
/* 182:171 */         SQL = "Insert into ad_response_summary (account_id,keyword,promo_hits,log_date,promo_keyword) values(?,?,?,?,?)";
/* 183:172 */         prepstat = con.prepareStatement(SQL);
/* 184:173 */         prepstat.setString(1, impression.getAccountId());
/* 185:174 */         prepstat.setString(2, impression.getInventory_keyword());
/* 186:175 */         prepstat.setInt(3, 1);
/* 187:176 */         prepstat.setString(4, currDate);
/* 188:177 */         prepstat.setString(5, impression.getKeyword());
/* 189:178 */         prepstat.execute();
/* 190:    */       }
/* 191:    */       else
/* 192:    */       {
/* 193:180 */         int total_promo_hits = rs.getInt("promo_hits") + 1;
/* 194:    */         
/* 195:182 */         SQL = "UPDATE ad_response_summary  SET promo_hits = " + total_promo_hits + " where keyword = ? and account_id = ? and promo_keyword = ? and log_date = ?";
/* 196:183 */         prepstat = con.prepareStatement(SQL);
/* 197:184 */         prepstat.setString(1, impression.getInventory_keyword());
/* 198:185 */         prepstat.setString(2, impression.getAccountId());
/* 199:186 */         prepstat.setString(3, impression.getKeyword());
/* 200:187 */         prepstat.setString(4, currDate);
/* 201:188 */         prepstat.execute();
/* 202:    */       }
/* 203:    */     }
/* 204:    */     catch (Exception ex)
/* 205:    */     {
/* 206:192 */       if (con != null)
/* 207:    */       {
/* 208:    */         try
/* 209:    */         {
/* 210:194 */           con.close();
/* 211:    */         }
/* 212:    */         catch (SQLException ex1)
/* 213:    */         {
/* 214:196 */           System.out.println(ex1.getMessage());
/* 215:    */         }
/* 216:198 */         con = null;
/* 217:    */       }
/* 218:200 */       System.out.println(new Date() + ": error updating ad_response_summary: " + ex.getMessage());
/* 219:    */     }
/* 220:    */     finally
/* 221:    */     {
/* 222:202 */       if (rs != null)
/* 223:    */       {
/* 224:    */         try
/* 225:    */         {
/* 226:204 */           rs.close();
/* 227:    */         }
/* 228:    */         catch (SQLException e) {}
/* 229:208 */         rs = null;
/* 230:    */       }
/* 231:210 */       if (prepstat != null)
/* 232:    */       {
/* 233:    */         try
/* 234:    */         {
/* 235:212 */           prepstat.close();
/* 236:    */         }
/* 237:    */         catch (SQLException e) {}
/* 238:216 */         prepstat = null;
/* 239:    */       }
/* 240:218 */       if (con != null)
/* 241:    */       {
/* 242:    */         try
/* 243:    */         {
/* 244:220 */           con.close();
/* 245:    */         }
/* 246:    */         catch (SQLException e) {}
/* 247:224 */         con = null;
/* 248:    */       }
/* 249:    */     }
/* 250:    */   }
/* 251:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.viralmarketing.PromoImpressionDB
 * JD-Core Version:    0.7.0.1
 */