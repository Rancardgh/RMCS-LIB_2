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
/*  15:    */   public static void updatePromoViewDate(PromoImpression impression)
/*  16:    */     throws Exception
/*  17:    */   {
/*  18: 21 */     String sql = "UPDATE promo_impression_tracker set view_date = ? where hash_code = " + impression.getHashCode();
/*  19: 22 */     ResultSet rs = null;
/*  20: 23 */     Connection con = null;
/*  21: 24 */     PreparedStatement prepstat = null;
/*  22:    */     try
/*  23:    */     {
/*  24: 27 */       con = DConnect.getConnection();
/*  25: 28 */       prepstat = con.prepareStatement(sql);
/*  26: 29 */       prepstat.setTimestamp(1, new Timestamp(impression.getViewDate().getTime()));
/*  27: 30 */       prepstat.setString(2, impression.getAccountId());
/*  28: 31 */       prepstat.setString(3, impression.getKeyword());
/*  29: 32 */       prepstat.setString(4, impression.getMsisdn());
/*  30:    */       
/*  31: 34 */       prepstat.executeUpdate();
/*  32:    */     }
/*  33:    */     catch (Exception ex)
/*  34:    */     {
/*  35: 37 */       System.out.println(new Date() + ": error creating promo_campaign_hash entry(" + impression.getHashCode() + "): " + ex.getMessage());
/*  36: 38 */       throw new Exception();
/*  37:    */     }
/*  38:    */     finally
/*  39:    */     {
/*  40: 40 */       if (rs != null) {
/*  41: 41 */         rs.close();
/*  42:    */       }
/*  43: 43 */       if (prepstat != null) {
/*  44: 44 */         prepstat.close();
/*  45:    */       }
/*  46: 46 */       if (con != null) {
/*  47: 47 */         con.close();
/*  48:    */       }
/*  49:    */     }
/*  50:    */   }
/*  51:    */   
/*  52:    */   public static void createEntry(PromoImpression impression)
/*  53:    */     throws Exception
/*  54:    */   {
/*  55: 55 */     ResultSet rs = null;
/*  56: 56 */     Connection con = null;
/*  57: 57 */     PreparedStatement prepstat = null;
/*  58:    */     try
/*  59:    */     {
/*  60: 60 */       con = DConnect.getConnection();
/*  61: 61 */       String SQL = "insert into promo_impression_tracker (hash_code, msisdn, account_id, promo_response_code, view_date, inventory_keyword) values(?, ?, ?, ?, ?, ?)";
/*  62:    */       
/*  63:    */ 
/*  64: 64 */       prepstat = con.prepareStatement(SQL);
/*  65:    */       
/*  66: 66 */       prepstat.setLong(1, impression.getHashCode());
/*  67: 67 */       prepstat.setString(2, impression.getMsisdn());
/*  68: 68 */       prepstat.setString(3, impression.getAccountId());
/*  69: 69 */       prepstat.setString(4, impression.getKeyword());
/*  70: 70 */       prepstat.setTimestamp(5, new Timestamp(impression.getViewDate().getTime()));
/*  71: 71 */       prepstat.setString(6, impression.getInventory_keyword());
/*  72:    */       
/*  73: 73 */       prepstat.execute();
/*  74:    */     }
/*  75:    */     catch (Exception ex)
/*  76:    */     {
/*  77: 76 */       if (con != null)
/*  78:    */       {
/*  79:    */         try
/*  80:    */         {
/*  81: 78 */           con.close();
/*  82:    */         }
/*  83:    */         catch (SQLException ex1)
/*  84:    */         {
/*  85: 80 */           System.out.println(ex1.getMessage());
/*  86:    */         }
/*  87: 82 */         con = null;
/*  88:    */       }
/*  89: 84 */       System.out.println(new Date() + ": error creating promo_campaign_hash entry(" + impression.getHashCode() + "): " + ex.getMessage());
/*  90:    */     }
/*  91:    */     finally
/*  92:    */     {
/*  93: 86 */       if (rs != null)
/*  94:    */       {
/*  95:    */         try
/*  96:    */         {
/*  97: 88 */           rs.close();
/*  98:    */         }
/*  99:    */         catch (SQLException e) {}
/* 100: 92 */         rs = null;
/* 101:    */       }
/* 102: 94 */       if (prepstat != null)
/* 103:    */       {
/* 104:    */         try
/* 105:    */         {
/* 106: 96 */           prepstat.close();
/* 107:    */         }
/* 108:    */         catch (SQLException e) {}
/* 109:100 */         prepstat = null;
/* 110:    */       }
/* 111:102 */       if (con != null)
/* 112:    */       {
/* 113:    */         try
/* 114:    */         {
/* 115:104 */           con.close();
/* 116:    */         }
/* 117:    */         catch (SQLException e) {}
/* 118:108 */         con = null;
/* 119:    */       }
/* 120:    */     }
/* 121:    */   }
/* 122:    */   
/* 123:    */   public static PromoImpression viewPromoImpression(String msisdn, String keyword, String accountID)
/* 124:    */     throws Exception
/* 125:    */   {
/* 126:115 */     System.out.println(new Date() + " :@PromoImpressionDB");
/* 127:    */     
/* 128:117 */     ResultSet rs = null;
/* 129:118 */     Connection con = null;
/* 130:119 */     PreparedStatement prepstat = null;
/* 131:120 */     PromoImpression impression = new PromoImpression();
/* 132:    */     try
/* 133:    */     {
/* 134:123 */       con = DConnect.getConnection();
/* 135:    */       
/* 136:125 */       String SQL = "select * from promo_impression_tracker where msisdn = '" + msisdn + "' and inventory_keyword = '" + keyword + "' and account_id = '" + accountID + "'";
/* 137:126 */       System.out.println(new Date() + ": Looking for recent promo SQL: " + SQL);
/* 138:127 */       prepstat = con.prepareStatement(SQL);
/* 139:    */       
/* 140:129 */       rs = prepstat.executeQuery();
/* 141:131 */       while (rs.next())
/* 142:    */       {
/* 143:132 */         impression.setHashCode(rs.getLong("hash_code"));
/* 144:133 */         impression.setAccountId(rs.getString("account_id"));
/* 145:134 */         impression.setMsisdn(rs.getString("msisdn"));
/* 146:135 */         impression.setKeyword(rs.getString("promo_response_code"));
/* 147:136 */         impression.setViewDate(new Date(rs.getTimestamp("view_date").getTime()));
/* 148:137 */         impression.setInventory_keyword(rs.getString("inventory_keyword"));
/* 149:    */       }
/* 150:140 */       System.out.println(new Date() + ": Result found: MSISDN=" + impression.getMsisdn() + " KEYWORD=" + impression.getInventory_keyword() + " ACCOUNTID=" + impression.getAccountId());
/* 151:    */     }
/* 152:    */     catch (Exception ex)
/* 153:    */     {
/* 154:143 */       System.out.println(new Date() + ": error viewing promo_campaign_hash (" + impression.getHashCode() + "): " + ex.getMessage());
/* 155:    */     }
/* 156:    */     finally
/* 157:    */     {
/* 158:146 */       if (rs != null) {
/* 159:147 */         rs.close();
/* 160:    */       }
/* 161:149 */       if (prepstat != null) {
/* 162:150 */         prepstat.close();
/* 163:    */       }
/* 164:152 */       if (con != null) {
/* 165:153 */         con.close();
/* 166:    */       }
/* 167:    */     }
/* 168:157 */     return impression;
/* 169:    */   }
/* 170:    */   
/* 171:    */   public static PromoImpression viewPromoImpression(long hashCode)
/* 172:    */     throws Exception
/* 173:    */   {
/* 174:163 */     ResultSet rs = null;
/* 175:164 */     Connection con = null;
/* 176:165 */     PreparedStatement prepstat = null;
/* 177:166 */     PromoImpression impression = new PromoImpression();
/* 178:    */     try
/* 179:    */     {
/* 180:169 */       con = DConnect.getConnection();
/* 181:    */       
/* 182:171 */       String SQL = "select * from promo_impression_tracker where hash_code = ?";
/* 183:    */       
/* 184:173 */       prepstat = con.prepareStatement(SQL);
/* 185:    */       
/* 186:175 */       prepstat.setLong(1, hashCode);
/* 187:    */       
/* 188:177 */       rs = prepstat.executeQuery();
/* 189:179 */       while (rs.next())
/* 190:    */       {
/* 191:180 */         impression.setHashCode(rs.getLong("hash_code"));
/* 192:181 */         impression.setAccountId(rs.getString("account_id"));
/* 193:182 */         impression.setMsisdn(rs.getString("msisdn"));
/* 194:183 */         impression.setKeyword(rs.getString("promo_response_code"));
/* 195:184 */         impression.setViewDate(new Date(rs.getTimestamp("view_date").getTime()));
/* 196:185 */         impression.setInventory_keyword(rs.getString("inventory_keyword"));
/* 197:    */       }
/* 198:    */     }
/* 199:    */     catch (Exception ex)
/* 200:    */     {
/* 201:189 */       if (con != null)
/* 202:    */       {
/* 203:    */         try
/* 204:    */         {
/* 205:191 */           con.close();
/* 206:    */         }
/* 207:    */         catch (SQLException ex1)
/* 208:    */         {
/* 209:193 */           System.out.println(ex1.getMessage());
/* 210:    */         }
/* 211:195 */         con = null;
/* 212:    */       }
/* 213:199 */       System.out.println(new Date() + ": error viewing promo_campaign_hash (" + hashCode + "): " + ex.getMessage());
/* 214:    */     }
/* 215:    */     finally
/* 216:    */     {
/* 217:202 */       if (rs != null)
/* 218:    */       {
/* 219:    */         try
/* 220:    */         {
/* 221:204 */           rs.close();
/* 222:    */         }
/* 223:    */         catch (SQLException e) {}
/* 224:208 */         rs = null;
/* 225:    */       }
/* 226:210 */       if (prepstat != null)
/* 227:    */       {
/* 228:    */         try
/* 229:    */         {
/* 230:212 */           prepstat.close();
/* 231:    */         }
/* 232:    */         catch (SQLException e) {}
/* 233:216 */         prepstat = null;
/* 234:    */       }
/* 235:218 */       if (con != null)
/* 236:    */       {
/* 237:    */         try
/* 238:    */         {
/* 239:220 */           con.close();
/* 240:    */         }
/* 241:    */         catch (SQLException e) {}
/* 242:224 */         con = null;
/* 243:    */       }
/* 244:    */     }
/* 245:228 */     return impression;
/* 246:    */   }
/* 247:    */   
/* 248:    */   public static synchronized void updateAdResponseSummary(PromoImpression impression)
/* 249:    */     throws Exception
/* 250:    */   {
/* 251:233 */     ResultSet rs = null;
/* 252:234 */     Connection con = null;
/* 253:235 */     PreparedStatement prepstat = null;
/* 254:    */     
/* 255:237 */     Date dt = new Date();
/* 256:238 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/* 257:239 */     String currDate = sdf.format(dt);
/* 258:    */     try
/* 259:    */     {
/* 260:242 */       con = DConnect.getConnection();
/* 261:243 */       String SQL = "select promo_hits from ad_response_summary where account_id ='" + impression.getAccountId() + "' and keyword='" + impression.getInventory_keyword() + "'" + " and promo_keyword = '" + impression.getKeyword() + "' and log_date = '" + currDate + "'";
/* 262:    */       
/* 263:245 */       prepstat = con.prepareStatement(SQL);
/* 264:246 */       rs = prepstat.executeQuery();
/* 265:248 */       if (!rs.next())
/* 266:    */       {
/* 267:250 */         SQL = "Insert into ad_response_summary (account_id,keyword,promo_hits,log_date,promo_keyword) values(?,?,?,?,?)";
/* 268:251 */         prepstat = con.prepareStatement(SQL);
/* 269:252 */         prepstat.setString(1, impression.getAccountId());
/* 270:253 */         prepstat.setString(2, impression.getInventory_keyword());
/* 271:254 */         prepstat.setInt(3, 1);
/* 272:255 */         prepstat.setString(4, currDate);
/* 273:256 */         prepstat.setString(5, impression.getKeyword());
/* 274:257 */         prepstat.execute();
/* 275:    */       }
/* 276:    */       else
/* 277:    */       {
/* 278:259 */         int total_promo_hits = rs.getInt("promo_hits") + 1;
/* 279:    */         
/* 280:261 */         SQL = "UPDATE ad_response_summary  SET promo_hits = " + total_promo_hits + " where keyword = ? and account_id = ? and promo_keyword = ? and log_date = ?";
/* 281:262 */         prepstat = con.prepareStatement(SQL);
/* 282:263 */         prepstat.setString(1, impression.getInventory_keyword());
/* 283:264 */         prepstat.setString(2, impression.getAccountId());
/* 284:265 */         prepstat.setString(3, impression.getKeyword());
/* 285:266 */         prepstat.setString(4, currDate);
/* 286:267 */         prepstat.execute();
/* 287:    */       }
/* 288:    */     }
/* 289:    */     catch (Exception ex)
/* 290:    */     {
/* 291:271 */       if (con != null)
/* 292:    */       {
/* 293:    */         try
/* 294:    */         {
/* 295:273 */           con.close();
/* 296:    */         }
/* 297:    */         catch (SQLException ex1)
/* 298:    */         {
/* 299:275 */           System.out.println(ex1.getMessage());
/* 300:    */         }
/* 301:277 */         con = null;
/* 302:    */       }
/* 303:279 */       System.out.println(new Date() + ": error updating ad_response_summary: " + ex.getMessage());
/* 304:    */     }
/* 305:    */     finally
/* 306:    */     {
/* 307:281 */       if (rs != null)
/* 308:    */       {
/* 309:    */         try
/* 310:    */         {
/* 311:283 */           rs.close();
/* 312:    */         }
/* 313:    */         catch (SQLException e) {}
/* 314:287 */         rs = null;
/* 315:    */       }
/* 316:289 */       if (prepstat != null)
/* 317:    */       {
/* 318:    */         try
/* 319:    */         {
/* 320:291 */           prepstat.close();
/* 321:    */         }
/* 322:    */         catch (SQLException e) {}
/* 323:295 */         prepstat = null;
/* 324:    */       }
/* 325:297 */       if (con != null)
/* 326:    */       {
/* 327:    */         try
/* 328:    */         {
/* 329:299 */           con.close();
/* 330:    */         }
/* 331:    */         catch (SQLException e) {}
/* 332:303 */         con = null;
/* 333:    */       }
/* 334:    */     }
/* 335:    */   }
/* 336:    */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.viralmarketing.PromoImpressionDB
 * JD-Core Version:    0.7.0.1
 */