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
/*  13:    */ public class VMTransactionDB
/*  14:    */ {
/*  15:    */   public static void createTransaction(VMTransaction transaction)
/*  16:    */     throws Exception
/*  17:    */   {
/*  18: 22 */     ResultSet rs = null;
/*  19: 23 */     Connection con = null;
/*  20: 24 */     PreparedStatement prepstat = null;
/*  21:    */     try
/*  22:    */     {
/*  23: 27 */       con = DConnect.getConnection();
/*  24: 28 */       String SQL = "insert into vm_transactions(trans_date, campaign_id, recruiter_msisdn, recipient_msisdn, status) values(?, ?, ?, ?, ?)";
/*  25:    */       
/*  26:    */ 
/*  27: 31 */       prepstat = con.prepareStatement(SQL);
/*  28:    */       
/*  29: 33 */       prepstat.setTimestamp(1, new Timestamp(new Date().getTime()));
/*  30: 34 */       prepstat.setString(2, transaction.getCampaignId());
/*  31: 35 */       prepstat.setString(3, transaction.getRecruiterMsisdn());
/*  32: 36 */       prepstat.setString(4, transaction.getRecipientMsisdn());
/*  33: 37 */       prepstat.setString(5, transaction.getStatus());
/*  34:    */       
/*  35: 39 */       prepstat.execute();
/*  36:    */     }
/*  37:    */     catch (Exception ex)
/*  38:    */     {
/*  39: 42 */       if (con != null)
/*  40:    */       {
/*  41:    */         try
/*  42:    */         {
/*  43: 44 */           con.close();
/*  44:    */         }
/*  45:    */         catch (SQLException ex1)
/*  46:    */         {
/*  47: 46 */           System.out.println(ex1.getMessage());
/*  48:    */         }
/*  49: 48 */         con = null;
/*  50:    */       }
/*  51:    */     }
/*  52:    */     finally
/*  53:    */     {
/*  54: 51 */       if (rs != null)
/*  55:    */       {
/*  56:    */         try
/*  57:    */         {
/*  58: 53 */           rs.close();
/*  59:    */         }
/*  60:    */         catch (SQLException e) {}
/*  61: 57 */         rs = null;
/*  62:    */       }
/*  63: 59 */       if (prepstat != null)
/*  64:    */       {
/*  65:    */         try
/*  66:    */         {
/*  67: 61 */           prepstat.close();
/*  68:    */         }
/*  69:    */         catch (SQLException e) {}
/*  70: 65 */         prepstat = null;
/*  71:    */       }
/*  72: 67 */       if (con != null)
/*  73:    */       {
/*  74:    */         try
/*  75:    */         {
/*  76: 69 */           con.close();
/*  77:    */         }
/*  78:    */         catch (SQLException e) {}
/*  79: 73 */         con = null;
/*  80:    */       }
/*  81:    */     }
/*  82:    */   }
/*  83:    */   
/*  84:    */   public static VMTransaction viewTransaction(String campaignId, String recipientMsisdn)
/*  85:    */     throws Exception
/*  86:    */   {
/*  87: 82 */     ResultSet rs = null;
/*  88: 83 */     Connection con = null;
/*  89: 84 */     PreparedStatement prepstat = null;
/*  90: 85 */     VMTransaction transaction = new VMTransaction();
/*  91:    */     try
/*  92:    */     {
/*  93: 88 */       con = DConnect.getConnection();
/*  94:    */       
/*  95: 90 */       String SQL = "select * from vm_transactions where campaign_id = ? and recipient_msisdn = ?";
/*  96:    */       
/*  97: 92 */       prepstat = con.prepareStatement(SQL);
/*  98:    */       
/*  99: 94 */       prepstat.setString(1, campaignId);
/* 100: 95 */       prepstat.setString(2, recipientMsisdn);
/* 101:    */       
/* 102: 97 */       rs = prepstat.executeQuery();
/* 103: 99 */       while (rs.next())
/* 104:    */       {
/* 105:100 */         transaction.setCampaignId(rs.getString("campaign_id"));
/* 106:101 */         transaction.setRecruiterMsisdn(rs.getString("recruiter_msisdn"));
/* 107:102 */         transaction.setRecipientMsisdn(rs.getString("recipient_msisdn"));
/* 108:103 */         transaction.setStatus(rs.getString("status"));
/* 109:104 */         SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/* 110:105 */         String transactionDate = df.format(new Date(rs.getTimestamp("trans_date").getTime()));
/* 111:106 */         transaction.setTransactionDate(transactionDate);
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
/* 128:120 */       System.out.println(new Date() + ": error viewing vm_transaction (" + campaignId + ", " + recipientMsisdn + "): " + ex.getMessage());
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
/* 160:149 */     return transaction;
/* 161:    */   }
/* 162:    */   
/* 163:    */   public static VMTransaction viewTransaction(String accountId, String keyword, String recipientMsisdn)
/* 164:    */     throws Exception
/* 165:    */   {
/* 166:155 */     ResultSet rs = null;
/* 167:156 */     Connection con = null;
/* 168:157 */     PreparedStatement prepstat = null;
/* 169:158 */     VMTransaction transaction = new VMTransaction();
/* 170:    */     try
/* 171:    */     {
/* 172:161 */       con = DConnect.getConnection();
/* 173:    */       
/* 174:163 */       String SQL = "select * from vm_campaigns INNER JOIN vm_transactions ON vm_campaigns.campaign_id = vm_transactions.campaign_id  where account_id = ? and keyword = ? and recipient_msisdn = ?";
/* 175:    */       
/* 176:    */ 
/* 177:166 */       prepstat = con.prepareStatement(SQL);
/* 178:    */       
/* 179:168 */       prepstat.setString(1, accountId);
/* 180:169 */       prepstat.setString(2, keyword);
/* 181:170 */       prepstat.setString(3, recipientMsisdn);
/* 182:    */       
/* 183:172 */       rs = prepstat.executeQuery();
/* 184:174 */       while (rs.next())
/* 185:    */       {
/* 186:175 */         transaction.setCampaignId(rs.getString("campaign_id"));
/* 187:176 */         transaction.setRecruiterMsisdn(rs.getString("recruiter_msisdn"));
/* 188:177 */         transaction.setRecipientMsisdn(rs.getString("recipient_msisdn"));
/* 189:178 */         transaction.setStatus(rs.getString("status"));
/* 190:179 */         SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/* 191:180 */         String transactionDate = df.format(new Date(rs.getTimestamp("trans_date").getTime()));
/* 192:181 */         transaction.setTransactionDate(transactionDate);
/* 193:    */       }
/* 194:    */     }
/* 195:    */     catch (Exception ex)
/* 196:    */     {
/* 197:185 */       if (con != null)
/* 198:    */       {
/* 199:    */         try
/* 200:    */         {
/* 201:187 */           con.close();
/* 202:    */         }
/* 203:    */         catch (SQLException ex1)
/* 204:    */         {
/* 205:189 */           System.out.println(ex1.getMessage());
/* 206:    */         }
/* 207:191 */         con = null;
/* 208:    */       }
/* 209:195 */       System.out.println(new Date() + ": error viewing vm_transaction (" + accountId + "," + keyword + "," + recipientMsisdn + "): " + ex.getMessage());
/* 210:    */     }
/* 211:    */     finally
/* 212:    */     {
/* 213:198 */       if (rs != null)
/* 214:    */       {
/* 215:    */         try
/* 216:    */         {
/* 217:200 */           rs.close();
/* 218:    */         }
/* 219:    */         catch (SQLException e) {}
/* 220:204 */         rs = null;
/* 221:    */       }
/* 222:206 */       if (prepstat != null)
/* 223:    */       {
/* 224:    */         try
/* 225:    */         {
/* 226:208 */           prepstat.close();
/* 227:    */         }
/* 228:    */         catch (SQLException e) {}
/* 229:212 */         prepstat = null;
/* 230:    */       }
/* 231:214 */       if (con != null)
/* 232:    */       {
/* 233:    */         try
/* 234:    */         {
/* 235:216 */           con.close();
/* 236:    */         }
/* 237:    */         catch (SQLException e) {}
/* 238:220 */         con = null;
/* 239:    */       }
/* 240:    */     }
/* 241:224 */     return transaction;
/* 242:    */   }
/* 243:    */   
/* 244:    */   public static void updateTransactionStatus(String campaignId, String recipientId, String status)
/* 245:    */     throws Exception
/* 246:    */   {
/* 247:230 */     ResultSet rs = null;
/* 248:231 */     Connection con = null;
/* 249:232 */     PreparedStatement prepstat = null;
/* 250:    */     try
/* 251:    */     {
/* 252:235 */       con = DConnect.getConnection();
/* 253:236 */       String SQL = "UPDATE vm_transactions SET status = ? WHERE campaign_id = ?  and recipient_msisdn = ?";
/* 254:    */       
/* 255:    */ 
/* 256:    */ 
/* 257:240 */       prepstat = con.prepareStatement(SQL);
/* 258:    */       
/* 259:242 */       prepstat.setString(1, status);
/* 260:243 */       prepstat.setString(2, campaignId);
/* 261:244 */       prepstat.setString(3, recipientId);
/* 262:    */       
/* 263:246 */       prepstat.execute();
/* 264:    */     }
/* 265:    */     catch (Exception ex)
/* 266:    */     {
/* 267:249 */       if (con != null)
/* 268:    */       {
/* 269:    */         try
/* 270:    */         {
/* 271:251 */           con.close();
/* 272:    */         }
/* 273:    */         catch (SQLException ex1)
/* 274:    */         {
/* 275:253 */           System.out.println(ex1.getMessage());
/* 276:    */         }
/* 277:255 */         con = null;
/* 278:    */       }
/* 279:    */     }
/* 280:    */     finally
/* 281:    */     {
/* 282:258 */       if (rs != null)
/* 283:    */       {
/* 284:    */         try
/* 285:    */         {
/* 286:260 */           rs.close();
/* 287:    */         }
/* 288:    */         catch (SQLException e) {}
/* 289:264 */         rs = null;
/* 290:    */       }
/* 291:266 */       if (prepstat != null)
/* 292:    */       {
/* 293:    */         try
/* 294:    */         {
/* 295:268 */           prepstat.close();
/* 296:    */         }
/* 297:    */         catch (SQLException e) {}
/* 298:272 */         prepstat = null;
/* 299:    */       }
/* 300:274 */       if (con != null)
/* 301:    */       {
/* 302:    */         try
/* 303:    */         {
/* 304:276 */           con.close();
/* 305:    */         }
/* 306:    */         catch (SQLException e) {}
/* 307:280 */         con = null;
/* 308:    */       }
/* 309:    */     }
/* 310:    */   }
/* 311:    */   
/* 312:    */   public static void updateTransactionStatus(String campaignId, String recipientId, String status, int points)
/* 313:    */     throws Exception
/* 314:    */   {
/* 315:290 */     ResultSet rs = null;
/* 316:291 */     Connection con = null;
/* 317:292 */     PreparedStatement prepstat = null;
/* 318:293 */     PreparedStatement prepstat_points = null;
/* 319:    */     try
/* 320:    */     {
/* 321:296 */       con = DConnect.getConnection();
/* 322:297 */       String SQL = "UPDATE vm_transactions SET status = ? WHERE campaign_id = ?  and recipient_msisdn = ?";
/* 323:    */       
/* 324:    */ 
/* 325:    */ 
/* 326:301 */       prepstat = con.prepareStatement(SQL);
/* 327:    */       
/* 328:303 */       prepstat.setString(1, status);
/* 329:304 */       prepstat.setString(2, campaignId);
/* 330:305 */       prepstat.setString(3, recipientId);
/* 331:    */       
/* 332:307 */       prepstat.execute();
/* 333:    */       
/* 334:    */ 
/* 335:    */ 
/* 336:    */ 
/* 337:    */ 
/* 338:    */ 
/* 339:    */ 
/* 340:    */ 
/* 341:    */ 
/* 342:317 */       String SQL_points = "UPDATE vm_users vmu INNER JOIN vm_campaigns vmc ON vmu.account_id = vmc.account_id AND vmu.keyword = vmc.keyword INNER JOIN vm_transactions vmt ON vmt.campaign_id = vmc.campaign_id SET vmu.points = vmu.points + ? WHERE vmt.recipient_msisdn = ? AND vmt.campaign_id = ? AND vmt.status = ? AND vmu.msisdn = vmt.recruiter_msisdn";
/* 343:    */       
/* 344:    */ 
/* 345:    */ 
/* 346:    */ 
/* 347:    */ 
/* 348:    */ 
/* 349:    */ 
/* 350:325 */       prepstat_points = con.prepareStatement(SQL_points);
/* 351:    */       
/* 352:327 */       prepstat_points.setInt(1, points);
/* 353:328 */       prepstat_points.setString(2, recipientId);
/* 354:329 */       prepstat_points.setString(3, campaignId);
/* 355:330 */       prepstat_points.setString(4, status);
/* 356:    */       
/* 357:332 */       prepstat_points.execute();
/* 358:    */     }
/* 359:    */     catch (Exception ex)
/* 360:    */     {
/* 361:335 */       if (con != null)
/* 362:    */       {
/* 363:    */         try
/* 364:    */         {
/* 365:337 */           con.close();
/* 366:    */         }
/* 367:    */         catch (SQLException ex1)
/* 368:    */         {
/* 369:339 */           System.out.println(ex1.getMessage());
/* 370:    */         }
/* 371:341 */         con = null;
/* 372:    */       }
/* 373:    */     }
/* 374:    */     finally
/* 375:    */     {
/* 376:344 */       if (rs != null)
/* 377:    */       {
/* 378:    */         try
/* 379:    */         {
/* 380:346 */           rs.close();
/* 381:    */         }
/* 382:    */         catch (SQLException e) {}
/* 383:350 */         rs = null;
/* 384:    */       }
/* 385:352 */       if (prepstat != null)
/* 386:    */       {
/* 387:    */         try
/* 388:    */         {
/* 389:354 */           prepstat.close();
/* 390:355 */           prepstat_points.close();
/* 391:    */         }
/* 392:    */         catch (SQLException e) {}
/* 393:359 */         prepstat = null;
/* 394:360 */         prepstat_points = null;
/* 395:    */       }
/* 396:362 */       if (con != null)
/* 397:    */       {
/* 398:    */         try
/* 399:    */         {
/* 400:364 */           con.close();
/* 401:    */         }
/* 402:    */         catch (SQLException e) {}
/* 403:368 */         con = null;
/* 404:    */       }
/* 405:    */     }
/* 406:    */   }
/* 407:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.viralmarketing.VMTransactionDB
 * JD-Core Version:    0.7.0.1
 */