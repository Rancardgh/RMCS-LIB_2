/*   1:    */ package com.rancard.mobility.contentserver;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import java.io.PrintStream;
/*   5:    */ import java.sql.Connection;
/*   6:    */ import java.sql.PreparedStatement;
/*   7:    */ import java.sql.ResultSet;
/*   8:    */ import java.util.ArrayList;
/*   9:    */ import java.util.Date;
/*  10:    */ 
/*  11:    */ public abstract class CPSiteDB
/*  12:    */ {
/*  13:    */   public static void createSite(String cpId, String siteName, String siteId, boolean check, String type)
/*  14:    */     throws Exception
/*  15:    */   {
/*  16: 26 */     ResultSet rs = null;
/*  17: 27 */     Connection con = null;
/*  18: 28 */     PreparedStatement prepstat = null;
/*  19:    */     try
/*  20:    */     {
/*  21: 31 */       con = DConnect.getConnection();
/*  22: 32 */       String query = "INSERT into cp_sites(cp_id,site_name,check_user,site_id, site_type) values(?,?,?,?,?)";
/*  23:    */       
/*  24: 34 */       prepstat = con.prepareStatement(query);
/*  25:    */       
/*  26: 36 */       prepstat.setString(1, cpId);
/*  27: 37 */       prepstat.setString(2, siteName);
/*  28: 38 */       if (check == true) {
/*  29: 39 */         prepstat.setInt(3, 1);
/*  30:    */       } else {
/*  31: 41 */         prepstat.setInt(3, 0);
/*  32:    */       }
/*  33: 43 */       prepstat.setString(4, siteId);
/*  34: 44 */       prepstat.setString(5, type);
/*  35:    */       
/*  36: 46 */       prepstat.execute();
/*  37:    */     }
/*  38:    */     catch (Exception ex)
/*  39:    */     {
/*  40: 49 */       if (con != null) {
/*  41: 50 */         con.close();
/*  42:    */       }
/*  43: 52 */       throw new Exception(ex.getMessage());
/*  44:    */     }
/*  45: 54 */     if (con != null) {
/*  46: 55 */       con.close();
/*  47:    */     }
/*  48:    */   }
/*  49:    */   
/*  50:    */   public static void updateSiteName(String siteName, String siteId)
/*  51:    */     throws Exception
/*  52:    */   {
/*  53: 61 */     ResultSet rs = null;
/*  54: 62 */     Connection con = null;
/*  55: 63 */     PreparedStatement prepstat = null;
/*  56:    */     try
/*  57:    */     {
/*  58: 66 */       con = DConnect.getConnection();
/*  59: 67 */       String query = "update cp_sites set site_name=? where site_id=?";
/*  60:    */       
/*  61: 69 */       prepstat = con.prepareStatement(query);
/*  62:    */       
/*  63: 71 */       prepstat.setString(1, siteName);
/*  64: 72 */       prepstat.setString(2, siteId);
/*  65:    */       
/*  66: 74 */       prepstat.execute();
/*  67:    */     }
/*  68:    */     catch (Exception ex)
/*  69:    */     {
/*  70: 77 */       if (con != null) {
/*  71: 78 */         con.close();
/*  72:    */       }
/*  73: 80 */       throw new Exception(ex.getMessage());
/*  74:    */     }
/*  75: 82 */     if (con != null) {
/*  76: 83 */       con.close();
/*  77:    */     }
/*  78:    */   }
/*  79:    */   
/*  80:    */   public static void updateSiteAuthentication(boolean check, String siteId)
/*  81:    */     throws Exception
/*  82:    */   {
/*  83: 89 */     ResultSet rs = null;
/*  84: 90 */     Connection con = null;
/*  85: 91 */     PreparedStatement prepstat = null;
/*  86:    */     try
/*  87:    */     {
/*  88: 94 */       con = DConnect.getConnection();
/*  89: 95 */       String query = "update cp_sites set check_user=? where site_id=?";
/*  90:    */       
/*  91: 97 */       prepstat = con.prepareStatement(query);
/*  92: 99 */       if (check == true) {
/*  93:100 */         prepstat.setInt(1, 1);
/*  94:    */       } else {
/*  95:102 */         prepstat.setInt(1, 0);
/*  96:    */       }
/*  97:104 */       prepstat.setString(2, siteId);
/*  98:    */       
/*  99:106 */       prepstat.execute();
/* 100:    */     }
/* 101:    */     catch (Exception ex)
/* 102:    */     {
/* 103:109 */       if (con != null) {
/* 104:110 */         con.close();
/* 105:    */       }
/* 106:112 */       throw new Exception(ex.getMessage());
/* 107:    */     }
/* 108:114 */     if (con != null) {
/* 109:115 */       con.close();
/* 110:    */     }
/* 111:    */   }
/* 112:    */   
/* 113:    */   public static void deleteSite(String siteId)
/* 114:    */     throws Exception
/* 115:    */   {
/* 116:121 */     ResultSet rs = null;
/* 117:122 */     Connection con = null;
/* 118:123 */     PreparedStatement prepstat = null;
/* 119:    */     try
/* 120:    */     {
/* 121:126 */       con = DConnect.getConnection();
/* 122:127 */       String query = "DELETE from cp_sites WHERE site_id=?";
/* 123:128 */       prepstat = con.prepareStatement(query);
/* 124:    */       
/* 125:130 */       prepstat.setString(1, siteId);
/* 126:    */       
/* 127:132 */       prepstat.execute();
/* 128:    */     }
/* 129:    */     catch (Exception ex)
/* 130:    */     {
/* 131:134 */       if (con != null) {
/* 132:135 */         con.close();
/* 133:    */       }
/* 134:137 */       throw new Exception(ex.getMessage());
/* 135:    */     }
/* 136:139 */     if (con != null) {
/* 137:140 */       con.close();
/* 138:    */     }
/* 139:    */   }
/* 140:    */   
/* 141:    */   public static void deleteAllSitesForCP(String cpId)
/* 142:    */     throws Exception
/* 143:    */   {
/* 144:146 */     ResultSet rs = null;
/* 145:147 */     Connection con = null;
/* 146:148 */     PreparedStatement prepstat = null;
/* 147:    */     try
/* 148:    */     {
/* 149:151 */       con = DConnect.getConnection();
/* 150:152 */       String query = "DELETE from cp_sites WHERE cp_id=?";
/* 151:153 */       prepstat = con.prepareStatement(query);
/* 152:    */       
/* 153:155 */       prepstat.setString(1, cpId);
/* 154:    */       
/* 155:157 */       prepstat.execute();
/* 156:    */     }
/* 157:    */     catch (Exception ex)
/* 158:    */     {
/* 159:159 */       if (con != null) {
/* 160:160 */         con.close();
/* 161:    */       }
/* 162:162 */       throw new Exception(ex.getMessage());
/* 163:    */     }
/* 164:164 */     if (con != null) {
/* 165:165 */       con.close();
/* 166:    */     }
/* 167:    */   }
/* 168:    */   
/* 169:    */   public static CPSite viewSite(String siteId)
/* 170:    */     throws Exception
/* 171:    */   {
/* 172:170 */     CPSite site = new CPSite();
/* 173:    */     
/* 174:    */ 
/* 175:173 */     ResultSet rs = null;
/* 176:174 */     Connection con = null;
/* 177:175 */     PreparedStatement prepstat = null;
/* 178:    */     
/* 179:177 */     System.out.println(new Date() + ":@com.rancard.mobility.contentserver.CPSiteDB...");
/* 180:178 */     System.out.println(new Date() + ": viewing cp_site (" + siteId + ")...");
/* 181:    */     try
/* 182:    */     {
/* 183:181 */       con = DConnect.getConnection();
/* 184:182 */       String query = "select * from cp_sites where site_id=?";
/* 185:183 */       prepstat = con.prepareStatement(query);
/* 186:    */       
/* 187:185 */       prepstat.setString(1, siteId);
/* 188:    */       
/* 189:187 */       rs = prepstat.executeQuery();
/* 190:189 */       while (rs.next())
/* 191:    */       {
/* 192:190 */         site.setCpId(rs.getString("cp_id"));
/* 193:191 */         site.setCpSiteId(rs.getString("site_id"));
/* 194:192 */         site.setCpSiteName(rs.getString("site_name"));
/* 195:193 */         if (rs.getInt("check_user") == 1) {
/* 196:194 */           site.setCheckUser(true);
/* 197:    */         } else {
/* 198:196 */           site.setCheckUser(false);
/* 199:    */         }
/* 200:198 */         site.setSiteType(rs.getString("site_type"));
/* 201:    */         
/* 202:    */ 
/* 203:201 */         System.out.println(new Date() + ": cp_site (" + siteId + ") found!");
/* 204:    */       }
/* 205:204 */       if ((site.getCpSiteId() == null) || ("".equals(site.getCpSiteId()))) {
/* 206:205 */         System.out.println(new Date() + ": cp_site (" + siteId + ") not found!");
/* 207:    */       }
/* 208:    */     }
/* 209:    */     catch (Exception ex)
/* 210:    */     {
/* 211:210 */       System.out.println(new Date() + ": error viewing cp_site (" + siteId + "): " + ex.getMessage());
/* 212:212 */       if (con != null) {
/* 213:213 */         con.close();
/* 214:    */       }
/* 215:215 */       throw new Exception(ex.getMessage());
/* 216:    */     }
/* 217:217 */     if (con != null) {
/* 218:218 */       con.close();
/* 219:    */     }
/* 220:221 */     return site;
/* 221:    */   }
/* 222:    */   
/* 223:    */   public static String getCpIdForSite(String siteId)
/* 224:    */     throws Exception
/* 225:    */   {
/* 226:226 */     String id = "";
/* 227:227 */     ResultSet rs = null;
/* 228:228 */     Connection con = null;
/* 229:229 */     PreparedStatement prepstat = null;
/* 230:    */     try
/* 231:    */     {
/* 232:232 */       con = DConnect.getConnection();
/* 233:233 */       String query = "select cp_id from cp_sites where site_id=?";
/* 234:234 */       prepstat = con.prepareStatement(query);
/* 235:    */       
/* 236:236 */       prepstat.setString(1, siteId);
/* 237:    */       
/* 238:238 */       rs = prepstat.executeQuery();
/* 239:240 */       while (rs.next()) {
/* 240:241 */         id = rs.getString("cp_id");
/* 241:    */       }
/* 242:    */     }
/* 243:    */     catch (Exception ex)
/* 244:    */     {
/* 245:244 */       if (con != null) {
/* 246:245 */         con.close();
/* 247:    */       }
/* 248:247 */       throw new Exception(ex.getMessage());
/* 249:    */     }
/* 250:249 */     if (con != null) {
/* 251:250 */       con.close();
/* 252:    */     }
/* 253:253 */     return id;
/* 254:    */   }
/* 255:    */   
/* 256:    */   public static ArrayList viewAllSitesForCP(String cpId)
/* 257:    */     throws Exception
/* 258:    */   {
/* 259:257 */     ArrayList sites = new ArrayList();
/* 260:    */     
/* 261:259 */     ResultSet rs = null;
/* 262:260 */     Connection con = null;
/* 263:261 */     PreparedStatement prepstat = null;
/* 264:    */     try
/* 265:    */     {
/* 266:264 */       con = DConnect.getConnection();
/* 267:265 */       String query = "select * from cp_sites where cp_id=?";
/* 268:266 */       prepstat = con.prepareStatement(query);
/* 269:    */       
/* 270:268 */       prepstat.setString(1, cpId);
/* 271:    */       
/* 272:270 */       rs = prepstat.executeQuery();
/* 273:272 */       while (rs.next())
/* 274:    */       {
/* 275:273 */         CPSite site = new CPSite();
/* 276:    */         
/* 277:275 */         site.setCpId(rs.getString("cp_id"));
/* 278:276 */         site.setCpSiteId(rs.getString("site_id"));
/* 279:277 */         site.setCpSiteName(rs.getString("site_name"));
/* 280:278 */         if (rs.getInt("check_user") == 1) {
/* 281:279 */           site.setCheckUser(true);
/* 282:    */         } else {
/* 283:281 */           site.setCheckUser(false);
/* 284:    */         }
/* 285:283 */         site.setSiteType(rs.getString("site_type"));
/* 286:    */         
/* 287:285 */         sites.add(site);
/* 288:    */       }
/* 289:    */     }
/* 290:    */     catch (Exception ex)
/* 291:    */     {
/* 292:288 */       if (con != null) {
/* 293:289 */         con.close();
/* 294:    */       }
/* 295:291 */       throw new Exception(ex.getMessage());
/* 296:    */     }
/* 297:293 */     if (con != null) {
/* 298:294 */       con.close();
/* 299:    */     }
/* 300:297 */     return sites;
/* 301:    */   }
/* 302:    */   
/* 303:    */   public static ArrayList viewAffiliatedSitesForCP(String cpId)
/* 304:    */     throws Exception
/* 305:    */   {
/* 306:301 */     ArrayList sites = new ArrayList();
/* 307:    */     
/* 308:303 */     ResultSet rs = null;
/* 309:304 */     Connection con = null;
/* 310:305 */     PreparedStatement prepstat = null;
/* 311:    */     try
/* 312:    */     {
/* 313:308 */       con = DConnect.getConnection();
/* 314:309 */       String query = "select * from cp_sites where cp_id='" + cpId + "' or cp_id in (select cs_id from cs_cp_relationship where list_id='" + cpId + "')";
/* 315:310 */       prepstat = con.prepareStatement(query);
/* 316:311 */       rs = prepstat.executeQuery();
/* 317:313 */       while (rs.next())
/* 318:    */       {
/* 319:314 */         CPSite site = new CPSite();
/* 320:315 */         site.setCpId(rs.getString("cp_id"));
/* 321:316 */         site.setCpSiteId(rs.getString("site_id"));
/* 322:317 */         site.setCpSiteName(rs.getString("site_name"));
/* 323:318 */         if (rs.getInt("check_user") == 1) {
/* 324:319 */           site.setCheckUser(true);
/* 325:    */         } else {
/* 326:321 */           site.setCheckUser(false);
/* 327:    */         }
/* 328:323 */         site.setSiteType(rs.getString("site_type"));
/* 329:    */         
/* 330:325 */         sites.add(site);
/* 331:    */       }
/* 332:    */     }
/* 333:    */     catch (Exception ex)
/* 334:    */     {
/* 335:328 */       if (con != null) {
/* 336:329 */         con.close();
/* 337:    */       }
/* 338:331 */       throw new Exception(ex.getMessage());
/* 339:    */     }
/* 340:333 */     if (con != null) {
/* 341:334 */       con.close();
/* 342:    */     }
/* 343:337 */     return sites;
/* 344:    */   }
/* 345:    */   
/* 346:    */   public static ArrayList viewCPsWithSites()
/* 347:    */     throws Exception
/* 348:    */   {
/* 349:341 */     ArrayList cps = new ArrayList();
/* 350:    */     
/* 351:343 */     ResultSet rs = null;
/* 352:344 */     Connection con = null;
/* 353:345 */     PreparedStatement prepstat = null;
/* 354:    */     try
/* 355:    */     {
/* 356:348 */       con = DConnect.getConnection();
/* 357:349 */       String query = "select distinct cp_id from cp_sites";
/* 358:350 */       prepstat = con.prepareStatement(query);
/* 359:351 */       rs = prepstat.executeQuery();
/* 360:353 */       while (rs.next()) {
/* 361:354 */         cps.add(rs.getString("cp_id"));
/* 362:    */       }
/* 363:    */     }
/* 364:    */     catch (Exception ex)
/* 365:    */     {
/* 366:357 */       if (con != null) {
/* 367:358 */         con.close();
/* 368:    */       }
/* 369:360 */       throw new Exception(ex.getMessage());
/* 370:    */     }
/* 371:362 */     if (con != null) {
/* 372:363 */       con.close();
/* 373:    */     }
/* 374:366 */     return cps;
/* 375:    */   }
/* 376:    */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.CPSiteDB
 * JD-Core Version:    0.7.0.1
 */