/*   1:    */ package com.rancard.mobility.contentserver;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import com.rancard.mobility.contentprovider.User;
/*   5:    */ import java.io.PrintStream;
/*   6:    */ import java.sql.BatchUpdateException;
/*   7:    */ import java.sql.Connection;
/*   8:    */ import java.sql.PreparedStatement;
/*   9:    */ import java.sql.ResultSet;
/*  10:    */ import java.sql.SQLException;
/*  11:    */ import java.sql.Statement;
/*  12:    */ import java.util.ArrayList;
/*  13:    */ 
/*  14:    */ public abstract class RelationshipDB
/*  15:    */ {
/*  16:    */   public static void insertRelationship(String cs_id, String listId)
/*  17:    */     throws Exception
/*  18:    */   {
/*  19: 18 */     ResultSet rs = null;
/*  20: 19 */     Connection con = null;
/*  21: 20 */     PreparedStatement prepstat = null;
/*  22:    */     try
/*  23:    */     {
/*  24: 23 */       con = DConnect.getConnection();
/*  25: 24 */       String query = "INSERT into cs_cp_relationship(cs_id,list_id) values(?,?)";
/*  26:    */       
/*  27:    */ 
/*  28: 27 */       prepstat = con.prepareStatement(query);
/*  29: 28 */       prepstat.setString(1, cs_id);
/*  30: 29 */       prepstat.setString(2, listId);
/*  31:    */       
/*  32: 31 */       prepstat.execute();
/*  33:    */     }
/*  34:    */     catch (Exception ex)
/*  35:    */     {
/*  36: 33 */       if (con != null) {
/*  37: 34 */         con.close();
/*  38:    */       }
/*  39: 36 */       throw new Exception(ex.getMessage());
/*  40:    */     }
/*  41: 38 */     if (con != null) {
/*  42: 39 */       con.close();
/*  43:    */     }
/*  44:    */   }
/*  45:    */   
/*  46:    */   public static void deleteRelationshipForProvider(String id)
/*  47:    */     throws Exception
/*  48:    */   {
/*  49: 48 */     ResultSet rs = null;
/*  50: 49 */     Connection con = null;
/*  51: 50 */     PreparedStatement prepstat = null;
/*  52:    */     try
/*  53:    */     {
/*  54: 53 */       con = DConnect.getConnection();
/*  55: 54 */       String query = "DELETE from cs_cp_relationship WHERE list_id=?";
/*  56: 55 */       prepstat = con.prepareStatement(query);
/*  57: 56 */       prepstat.setString(1, id);
/*  58: 57 */       prepstat.execute();
/*  59:    */     }
/*  60:    */     catch (Exception ex)
/*  61:    */     {
/*  62: 59 */       if (con != null) {
/*  63: 60 */         con.close();
/*  64:    */       }
/*  65: 62 */       throw new Exception(ex.getMessage());
/*  66:    */     }
/*  67: 64 */     if (con != null) {
/*  68: 65 */       con.close();
/*  69:    */     }
/*  70:    */   }
/*  71:    */   
/*  72:    */   public static void deleteRelationshipForContentSubscriber(String id)
/*  73:    */     throws Exception
/*  74:    */   {
/*  75: 71 */     ResultSet rs = null;
/*  76: 72 */     Connection con = null;
/*  77: 73 */     PreparedStatement prepstat = null;
/*  78:    */     try
/*  79:    */     {
/*  80: 76 */       con = DConnect.getConnection();
/*  81: 77 */       String query = "DELETE from cs_cp_relationship WHERE cs_id=?";
/*  82: 78 */       prepstat = con.prepareStatement(query);
/*  83: 79 */       prepstat.setString(1, id);
/*  84: 80 */       prepstat.execute();
/*  85:    */     }
/*  86:    */     catch (Exception ex)
/*  87:    */     {
/*  88: 82 */       if (con != null) {
/*  89: 83 */         con.close();
/*  90:    */       }
/*  91: 85 */       throw new Exception(ex.getMessage());
/*  92:    */     }
/*  93: 87 */     if (con != null) {
/*  94: 88 */       con.close();
/*  95:    */     }
/*  96:    */   }
/*  97:    */   
/*  98:    */   public static void deleteRelationship(String csid, String cpid)
/*  99:    */     throws Exception
/* 100:    */   {
/* 101: 94 */     ResultSet rs = null;
/* 102: 95 */     Connection con = null;
/* 103: 96 */     PreparedStatement prepstat = null;
/* 104:    */     try
/* 105:    */     {
/* 106: 99 */       con = DConnect.getConnection();
/* 107:100 */       String query = "DELETE from cs_cp_relationship WHERE cs_id=? and list_id=?";
/* 108:101 */       prepstat = con.prepareStatement(query);
/* 109:102 */       prepstat.setString(1, csid);
/* 110:103 */       prepstat.setString(2, cpid);
/* 111:104 */       prepstat.execute();
/* 112:    */     }
/* 113:    */     catch (Exception ex)
/* 114:    */     {
/* 115:106 */       if (con != null) {
/* 116:107 */         con.close();
/* 117:    */       }
/* 118:109 */       throw new Exception(ex.getMessage());
/* 119:    */     }
/* 120:111 */     if (con != null) {
/* 121:112 */       con.close();
/* 122:    */     }
/* 123:    */   }
/* 124:    */   
/* 125:    */   public static void deleteRelationship(String[] csid, String cpid)
/* 126:    */     throws Exception
/* 127:    */   {
/* 128:119 */     Connection con = null;
/* 129:120 */     Statement prepstat = null;
/* 130:121 */     int[] aiupdateCounts = null;
/* 131:122 */     boolean bError = false;
/* 132:    */     try
/* 133:    */     {
/* 134:126 */       con = DConnect.getConnection();
/* 135:127 */       con.setAutoCommit(false);
/* 136:128 */       con.createStatement();
/* 137:129 */       prepstat = con.createStatement();
/* 138:131 */       for (int i = 0; i < csid.length; i++)
/* 139:    */       {
/* 140:133 */         String SQL1 = "DELETE from cs_cp_relationship WHERE cs_id='" + csid[i] + "' and list_id ='" + cpid + "'";
/* 141:    */         
/* 142:135 */         bError = false;
/* 143:    */         
/* 144:    */ 
/* 145:    */ 
/* 146:139 */         prepstat.addBatch(SQL1);
/* 147:    */       }
/* 148:144 */       aiupdateCounts = prepstat.executeBatch();
/* 149:145 */       prepstat.clearBatch();
/* 150:    */     }
/* 151:    */     catch (BatchUpdateException bue)
/* 152:    */     {
/* 153:150 */       bError = true;
/* 154:151 */       aiupdateCounts = bue.getUpdateCounts();
/* 155:    */       
/* 156:153 */       SQLException SQLe = bue;
/* 157:154 */       while (SQLe != null) {
/* 158:156 */         SQLe = SQLe.getNextException();
/* 159:    */       }
/* 160:    */     }
/* 161:    */     catch (SQLException SQLe)
/* 162:    */     {
/* 163:160 */       System.out.println("there was an error during the update");
/* 164:    */     }
/* 165:    */     finally
/* 166:    */     {
/* 167:164 */       for (int i = 0; i < aiupdateCounts.length; i++)
/* 168:    */       {
/* 169:165 */         int iProcessed = aiupdateCounts[i];
/* 170:166 */         if ((iProcessed > 0) || (iProcessed == -2))
/* 171:    */         {
/* 172:170 */           System.out.println("Delete sucessful");
/* 173:    */         }
/* 174:    */         else
/* 175:    */         {
/* 176:173 */           bError = true;
/* 177:174 */           break;
/* 178:    */         }
/* 179:    */       }
/* 180:178 */       if (bError) {
/* 181:179 */         con.rollback();
/* 182:    */       } else {
/* 183:181 */         con.commit();
/* 184:    */       }
/* 185:183 */       if (con != null) {
/* 186:184 */         con.close();
/* 187:    */       }
/* 188:    */     }
/* 189:    */   }
/* 190:    */   
/* 191:    */   public static void deleteProviderRelationship(String[] cpid, String csid)
/* 192:    */     throws Exception
/* 193:    */   {
/* 194:193 */     Connection con = null;
/* 195:194 */     Statement prepstat = null;
/* 196:195 */     int[] aiupdateCounts = null;
/* 197:196 */     boolean bError = false;
/* 198:    */     try
/* 199:    */     {
/* 200:200 */       con = DConnect.getConnection();
/* 201:201 */       con.setAutoCommit(false);
/* 202:202 */       con.createStatement();
/* 203:203 */       prepstat = con.createStatement();
/* 204:205 */       for (int i = 0; i < cpid.length; i++)
/* 205:    */       {
/* 206:207 */         String SQL1 = "DELETE from cs_cp_relationship WHERE list_id='" + cpid[i] + "' and cs_id ='" + csid + "'";
/* 207:    */         
/* 208:209 */         bError = false;
/* 209:    */         
/* 210:    */ 
/* 211:    */ 
/* 212:213 */         prepstat.addBatch(SQL1);
/* 213:    */       }
/* 214:218 */       aiupdateCounts = prepstat.executeBatch();
/* 215:219 */       prepstat.clearBatch();
/* 216:    */     }
/* 217:    */     catch (BatchUpdateException bue)
/* 218:    */     {
/* 219:224 */       bError = true;
/* 220:225 */       aiupdateCounts = bue.getUpdateCounts();
/* 221:    */       
/* 222:227 */       SQLException SQLe = bue;
/* 223:228 */       while (SQLe != null) {
/* 224:230 */         SQLe = SQLe.getNextException();
/* 225:    */       }
/* 226:    */     }
/* 227:    */     catch (SQLException SQLe)
/* 228:    */     {
/* 229:234 */       System.out.println("there was an error during the update");
/* 230:    */     }
/* 231:    */     finally
/* 232:    */     {
/* 233:238 */       for (int i = 0; i < aiupdateCounts.length; i++)
/* 234:    */       {
/* 235:239 */         int iProcessed = aiupdateCounts[i];
/* 236:240 */         if ((iProcessed > 0) || (iProcessed == -2))
/* 237:    */         {
/* 238:244 */           System.out.println("Delete sucessful");
/* 239:    */         }
/* 240:    */         else
/* 241:    */         {
/* 242:247 */           bError = true;
/* 243:248 */           break;
/* 244:    */         }
/* 245:    */       }
/* 246:252 */       if (bError) {
/* 247:253 */         con.rollback();
/* 248:    */       } else {
/* 249:255 */         con.commit();
/* 250:    */       }
/* 251:257 */       if (con != null) {
/* 252:258 */         con.close();
/* 253:    */       }
/* 254:    */     }
/* 255:    */   }
/* 256:    */   
/* 257:    */   public static ArrayList viewProvidersForContentSubscriber(String id)
/* 258:    */     throws Exception
/* 259:    */   {
/* 260:271 */     ArrayList providers = new ArrayList();
/* 261:    */     
/* 262:273 */     ResultSet rs = null;
/* 263:274 */     Connection con = null;
/* 264:275 */     PreparedStatement prepstat = null;
/* 265:    */     try
/* 266:    */     {
/* 267:278 */       con = DConnect.getConnection();
/* 268:279 */       String query = "SELECT * from cs_cp_relationship   WHERE cs_id=?";
/* 269:280 */       prepstat = con.prepareStatement(query);
/* 270:281 */       prepstat.setString(1, id);
/* 271:282 */       rs = prepstat.executeQuery();
/* 272:284 */       while (rs.next()) {
/* 273:285 */         providers.add(rs.getString("list_id"));
/* 274:    */       }
/* 275:    */     }
/* 276:    */     catch (Exception ex)
/* 277:    */     {
/* 278:288 */       if (con != null) {
/* 279:289 */         con.close();
/* 280:    */       }
/* 281:291 */       throw new Exception(ex.getMessage());
/* 282:    */     }
/* 283:293 */     if (con != null) {
/* 284:294 */       con.close();
/* 285:    */     }
/* 286:296 */     return providers;
/* 287:    */   }
/* 288:    */   
/* 289:    */   public static ArrayList viewProvidersDetailsForContentSubscriber(String id)
/* 290:    */     throws Exception
/* 291:    */   {
/* 292:301 */     ArrayList providers = new ArrayList();
/* 293:    */     
/* 294:303 */     ResultSet rs = null;
/* 295:304 */     Connection con = null;
/* 296:305 */     PreparedStatement prepstat = null;
/* 297:    */     try
/* 298:    */     {
/* 299:308 */       con = DConnect.getConnection();
/* 300:309 */       String query = "SELECT * from cs_cp_relationship  csr inner join cp_user cpu on csr.list_id = cpu.id  WHERE cs_id=?";
/* 301:310 */       prepstat = con.prepareStatement(query);
/* 302:311 */       prepstat.setString(1, id);
/* 303:312 */       rs = prepstat.executeQuery();
/* 304:314 */       while (rs.next())
/* 305:    */       {
/* 306:315 */         User cp = new User();
/* 307:    */         
/* 308:317 */         cp.setId(rs.getString("id"));
/* 309:318 */         cp.setName(rs.getString("name"));
/* 310:319 */         cp.setLogoUrl(rs.getString("logo_url"));
/* 311:320 */         cp.setUsername(rs.getString("username"));
/* 312:321 */         providers.add(cp);
/* 313:    */       }
/* 314:    */     }
/* 315:    */     catch (Exception ex)
/* 316:    */     {
/* 317:324 */       if (con != null) {
/* 318:325 */         con.close();
/* 319:    */       }
/* 320:327 */       throw new Exception(ex.getMessage());
/* 321:    */     }
/* 322:329 */     if (con != null) {
/* 323:330 */       con.close();
/* 324:    */     }
/* 325:332 */     return providers;
/* 326:    */   }
/* 327:    */   
/* 328:    */   public static ArrayList viewContentSubscribersForProvider(String id)
/* 329:    */     throws Exception
/* 330:    */   {
/* 331:336 */     ArrayList providers = new ArrayList();
/* 332:    */     
/* 333:338 */     ResultSet rs = null;
/* 334:339 */     Connection con = null;
/* 335:340 */     PreparedStatement prepstat = null;
/* 336:    */     try
/* 337:    */     {
/* 338:343 */       con = DConnect.getConnection();
/* 339:344 */       String query = "SELECT * from cs_cp_relationship WHERE list_id=?";
/* 340:345 */       prepstat = con.prepareStatement(query);
/* 341:346 */       prepstat.setString(1, id);
/* 342:347 */       rs = prepstat.executeQuery();
/* 343:349 */       while (rs.next()) {
/* 344:350 */         providers.add(rs.getString("cs_id"));
/* 345:    */       }
/* 346:    */     }
/* 347:    */     catch (Exception ex)
/* 348:    */     {
/* 349:353 */       if (con != null) {
/* 350:354 */         con.close();
/* 351:    */       }
/* 352:356 */       throw new Exception(ex.getMessage());
/* 353:    */     }
/* 354:358 */     if (con != null) {
/* 355:359 */       con.close();
/* 356:    */     }
/* 357:361 */     return providers;
/* 358:    */   }
/* 359:    */   
/* 360:    */   public static ArrayList viewContentSubscriberDetailsForProvider(String id)
/* 361:    */     throws Exception
/* 362:    */   {
/* 363:367 */     ArrayList providers = new ArrayList();
/* 364:    */     
/* 365:369 */     ResultSet rs = null;
/* 366:370 */     Connection con = null;
/* 367:371 */     PreparedStatement prepstat = null;
/* 368:    */     try
/* 369:    */     {
/* 370:374 */       con = DConnect.getConnection();
/* 371:375 */       String query = "SELECT * from cs_cp_relationship  csr inner join cp_user cpu on csr.cs_id = cpu.id  WHERE list_id=?";
/* 372:376 */       prepstat = con.prepareStatement(query);
/* 373:377 */       prepstat.setString(1, id);
/* 374:378 */       rs = prepstat.executeQuery();
/* 375:380 */       while (rs.next())
/* 376:    */       {
/* 377:381 */         User cp = new User();
/* 378:    */         
/* 379:383 */         cp.setId(rs.getString("id"));
/* 380:384 */         cp.setName(rs.getString("name"));
/* 381:385 */         cp.setLogoUrl(rs.getString("logo_url"));
/* 382:386 */         cp.setUsername(rs.getString("username"));
/* 383:387 */         providers.add(cp);
/* 384:    */       }
/* 385:    */     }
/* 386:    */     catch (Exception ex)
/* 387:    */     {
/* 388:390 */       if (con != null) {
/* 389:391 */         con.close();
/* 390:    */       }
/* 391:393 */       throw new Exception(ex.getMessage());
/* 392:    */     }
/* 393:395 */     if (con != null) {
/* 394:396 */       con.close();
/* 395:    */     }
/* 396:398 */     return providers;
/* 397:    */   }
/* 398:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.RelationshipDB
 * JD-Core Version:    0.7.0.1
 */