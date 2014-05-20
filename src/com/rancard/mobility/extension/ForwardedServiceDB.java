/*   1:    */ package com.rancard.mobility.extension;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import java.sql.Connection;
/*   5:    */ import java.sql.PreparedStatement;
/*   6:    */ import java.sql.ResultSet;
/*   7:    */ import java.sql.Timestamp;
/*   8:    */ import java.util.ArrayList;
/*   9:    */ import java.util.Calendar;
/*  10:    */ import java.util.Date;
/*  11:    */ 
/*  12:    */ public abstract class ForwardedServiceDB
/*  13:    */ {
/*  14:    */   public static void createForwardedService(ForwardedService service)
/*  15:    */     throws Exception
/*  16:    */   {
/*  17: 30 */     ResultSet rs = null;
/*  18: 31 */     Connection con = null;
/*  19: 32 */     PreparedStatement prepstat = null;
/*  20: 33 */     boolean bError = false;
/*  21:    */     try
/*  22:    */     {
/*  23: 36 */       con = DConnect.getConnection();
/*  24: 37 */       con.setAutoCommit(false);
/*  25:    */       
/*  26: 39 */       String SQL = "select * from service_definition where keyword='" + service.getKeyword() + "' and account_id='" + service.getAccountId() + "'";
/*  27: 40 */       prepstat = con.prepareStatement(SQL);
/*  28: 41 */       rs = prepstat.executeQuery();
/*  29: 43 */       if (!rs.next())
/*  30:    */       {
/*  31: 45 */         SQL = "insert into service_definition(service_type,keyword,account_id,service_name,default_message,is_basic,last_updated) values(?,?,?,?,?,?,?)";
/*  32:    */         
/*  33: 47 */         prepstat = con.prepareStatement(SQL);
/*  34: 48 */         prepstat.setString(1, service.getServiceType());
/*  35: 49 */         prepstat.setString(2, service.getKeyword());
/*  36: 50 */         prepstat.setString(3, service.getAccountId());
/*  37: 51 */         prepstat.setString(4, service.getServiceName());
/*  38: 52 */         prepstat.setString(5, service.getDefaultMessage());
/*  39: 53 */         prepstat.setInt(6, 0);
/*  40: 54 */         prepstat.setTimestamp(7, service.now());
/*  41: 55 */         prepstat.execute();
/*  42:    */       }
/*  43: 58 */       SQL = "select * from service_forwarding where keyword='" + service.getKeyword() + "' and account_id='" + service.getAccountId() + "'";
/*  44: 59 */       prepstat = con.prepareStatement(SQL);
/*  45: 60 */       rs = prepstat.executeQuery();
/*  46: 61 */       if (!rs.next())
/*  47:    */       {
/*  48: 62 */         SQL = "insert into service_forwarding (keyword,account_id,url,timeout,listen_status) values(?,?,?,?,?)";
/*  49: 63 */         prepstat = con.prepareStatement(SQL);
/*  50: 64 */         prepstat.setString(1, service.getKeyword());
/*  51: 65 */         prepstat.setString(2, service.getAccountId());
/*  52: 66 */         prepstat.setString(3, service.getUrl());
/*  53: 67 */         prepstat.setInt(4, service.getTimeout());
/*  54: 68 */         prepstat.setInt(5, service.getListenStatus());
/*  55: 69 */         prepstat.execute();
/*  56:    */       }
/*  57:    */     }
/*  58:    */     catch (Exception ex)
/*  59:    */     {
/*  60: 73 */       bError = true;
/*  61:    */       try
/*  62:    */       {
/*  63: 75 */         deleteForwardedService(service.getKeyword(), service.getAccountId());
/*  64:    */       }
/*  65:    */       catch (Exception ee) {}
/*  66: 77 */       throw new Exception(ex.getMessage());
/*  67:    */     }
/*  68:    */     finally
/*  69:    */     {
/*  70: 80 */       if (bError) {
/*  71: 81 */         con.rollback();
/*  72:    */       } else {
/*  73: 83 */         con.commit();
/*  74:    */       }
/*  75: 86 */       if (con != null) {
/*  76: 87 */         con.close();
/*  77:    */       }
/*  78:    */     }
/*  79:    */   }
/*  80:    */   
/*  81:    */   public static void updateForwardedServiceName(String keyword, String account_id, String serviceName)
/*  82:    */     throws Exception
/*  83:    */   {
/*  84: 95 */     ResultSet rs = null;
/*  85: 96 */     Connection con = null;
/*  86: 97 */     PreparedStatement prepstat = null;
/*  87: 98 */     boolean bError = false;
/*  88:    */     try
/*  89:    */     {
/*  90:101 */       con = DConnect.getConnection();
/*  91:102 */       con.setAutoCommit(false);
/*  92:    */       
/*  93:104 */       String SQL = "update service_definition set service_name=?,last_updated=? where keyword=? and account_id=?";
/*  94:    */       
/*  95:106 */       prepstat = con.prepareStatement(SQL);
/*  96:    */       
/*  97:108 */       prepstat.setString(1, serviceName);
/*  98:109 */       prepstat.setTimestamp(2, new Timestamp(Calendar.getInstance().getTime().getTime()));
/*  99:110 */       prepstat.setString(3, keyword);
/* 100:111 */       prepstat.setString(4, account_id);
/* 101:    */       
/* 102:113 */       prepstat.execute();
/* 103:    */     }
/* 104:    */     catch (Exception ex)
/* 105:    */     {
/* 106:115 */       if (con != null) {
/* 107:116 */         con.close();
/* 108:    */       }
/* 109:118 */       bError = true;
/* 110:119 */       throw new Exception(ex.getMessage());
/* 111:    */     }
/* 112:    */     finally
/* 113:    */     {
/* 114:122 */       if (con != null) {
/* 115:123 */         con.close();
/* 116:    */       }
/* 117:    */     }
/* 118:    */   }
/* 119:    */   
/* 120:    */   public static void updateForwardedServiceParameters(String keyword, String account_id, String url, int timeout, int listenStatus)
/* 121:    */     throws Exception
/* 122:    */   {
/* 123:131 */     ResultSet rs = null;
/* 124:132 */     Connection con = null;
/* 125:133 */     PreparedStatement prepstat = null;
/* 126:134 */     boolean bError = false;
/* 127:    */     try
/* 128:    */     {
/* 129:137 */       con = DConnect.getConnection();
/* 130:138 */       con.setAutoCommit(false);
/* 131:    */       
/* 132:140 */       String SQL = "select * from service_definition where keyword='" + keyword + "' and account_id='" + account_id + "'";
/* 133:141 */       prepstat = con.prepareStatement(SQL);
/* 134:142 */       rs = prepstat.executeQuery();
/* 135:144 */       if (rs.next())
/* 136:    */       {
/* 137:145 */         SQL = "update service_forwarding f, service_definition d set f.url=?,f.timeout=?,f.listen_status=?,d.last_updated=? where d.keyword=? and d.account_id=? and d.keyword=f.keyword and d.account_id=f.account_id";
/* 138:    */         
/* 139:    */ 
/* 140:148 */         prepstat = con.prepareStatement(SQL);
/* 141:    */         
/* 142:    */ 
/* 143:151 */         prepstat.setString(1, url);
/* 144:152 */         prepstat.setInt(2, timeout);
/* 145:153 */         prepstat.setInt(3, listenStatus);
/* 146:154 */         prepstat.setTimestamp(4, new Timestamp(Calendar.getInstance().getTime().getTime()));
/* 147:155 */         prepstat.setString(5, keyword);
/* 148:156 */         prepstat.setString(6, account_id);
/* 149:    */         
/* 150:158 */         prepstat.execute();
/* 151:    */       }
/* 152:    */     }
/* 153:    */     catch (Exception ex)
/* 154:    */     {
/* 155:161 */       if (con != null) {
/* 156:162 */         con.close();
/* 157:    */       }
/* 158:164 */       bError = true;
/* 159:165 */       throw new Exception(ex.getMessage());
/* 160:    */     }
/* 161:    */     finally
/* 162:    */     {
/* 163:168 */       if (con != null) {
/* 164:169 */         con.close();
/* 165:    */       }
/* 166:    */     }
/* 167:    */   }
/* 168:    */   
/* 169:    */   public static void updateForwardedService(String keyword, String account_id, String name, String url, int timeout, int listenStatus)
/* 170:    */     throws Exception
/* 171:    */   {
/* 172:177 */     ResultSet rs = null;
/* 173:178 */     Connection con = null;
/* 174:179 */     PreparedStatement prepstat = null;
/* 175:180 */     boolean bError = false;
/* 176:    */     try
/* 177:    */     {
/* 178:183 */       con = DConnect.getConnection();
/* 179:184 */       con.setAutoCommit(false);
/* 180:    */       
/* 181:    */ 
/* 182:    */ 
/* 183:188 */       String SQL = "select * from service_definition where keyword='" + keyword + "' and account_id='" + account_id + "'";
/* 184:189 */       prepstat = con.prepareStatement(SQL);
/* 185:190 */       rs = prepstat.executeQuery();
/* 186:192 */       if (rs.next())
/* 187:    */       {
/* 188:193 */         SQL = "update service_forwarding f, service_definition d set f.url=?,f.timeout=?,f.listen_status=?,d.last_updated=?,d.service_name=? where d.keyword=? and d.account_id=? and d.keyword=f.keyword and d.account_id=f.account_id";
/* 189:    */         
/* 190:    */ 
/* 191:196 */         prepstat = con.prepareStatement(SQL);
/* 192:    */         
/* 193:    */ 
/* 194:199 */         prepstat.setString(1, url);
/* 195:200 */         prepstat.setInt(2, timeout);
/* 196:201 */         prepstat.setInt(3, listenStatus);
/* 197:202 */         prepstat.setTimestamp(4, new Timestamp(Calendar.getInstance().getTime().getTime()));
/* 198:203 */         prepstat.setString(5, name);
/* 199:204 */         prepstat.setString(6, keyword);
/* 200:205 */         prepstat.setString(7, account_id);
/* 201:    */         
/* 202:207 */         prepstat.execute();
/* 203:    */       }
/* 204:    */     }
/* 205:    */     catch (Exception ex)
/* 206:    */     {
/* 207:210 */       if (con != null) {
/* 208:211 */         con.close();
/* 209:    */       }
/* 210:213 */       bError = true;
/* 211:214 */       throw new Exception(ex.getMessage());
/* 212:    */     }
/* 213:    */     finally
/* 214:    */     {
/* 215:217 */       if (con != null) {
/* 216:218 */         con.close();
/* 217:    */       }
/* 218:    */     }
/* 219:    */   }
/* 220:    */   
/* 221:    */   public static ForwardedService viewForwardedService(String keyword, String accountId)
/* 222:    */     throws Exception
/* 223:    */   {
/* 224:225 */     ForwardedService service = new ForwardedService();
/* 225:    */     
/* 226:    */ 
/* 227:228 */     ResultSet rs = null;
/* 228:229 */     Connection con = null;
/* 229:230 */     PreparedStatement prepstat = null;
/* 230:    */     try
/* 231:    */     {
/* 232:232 */       con = DConnect.getConnection();
/* 233:    */       
/* 234:234 */       String SQL = "SELECT * FROM service_definition s inner join service_forwarding k on s.keyword=k.keyword and s.account_id=k.account_id where s.keyword='" + keyword + "' and s.account_id='" + accountId + "'";
/* 235:    */       
/* 236:    */ 
/* 237:237 */       prepstat = con.prepareStatement(SQL);
/* 238:238 */       rs = prepstat.executeQuery();
/* 239:240 */       while (rs.next())
/* 240:    */       {
/* 241:241 */         service.setAccountId(rs.getString("s.account_id"));
/* 242:242 */         service.setDefaultMessage(rs.getString("s.default_message"));
/* 243:243 */         service.setKeyword(rs.getString("s.keyword"));
/* 244:244 */         service.setLastUpdated(rs.getString("s.last_updated"));
/* 245:245 */         service.setServiceName(rs.getString("s.service_name"));
/* 246:246 */         service.setServiceType(rs.getString("s.service_type"));
/* 247:247 */         service.setListenStatus(rs.getInt("listen_status"));
/* 248:248 */         service.setTimeout(rs.getInt("timeout"));
/* 249:249 */         service.setUrl(rs.getString("url"));
/* 250:    */       }
/* 251:    */     }
/* 252:    */     catch (Exception ex)
/* 253:    */     {
/* 254:252 */       if (con != null) {
/* 255:253 */         con.close();
/* 256:    */       }
/* 257:255 */       throw new Exception(ex.getMessage());
/* 258:    */     }
/* 259:257 */     if (con != null) {
/* 260:258 */       con.close();
/* 261:    */     }
/* 262:261 */     return service;
/* 263:    */   }
/* 264:    */   
/* 265:    */   public static ArrayList viewAllForwardedServices(String accountId)
/* 266:    */     throws Exception
/* 267:    */   {
/* 268:266 */     ArrayList services = new ArrayList();
/* 269:    */     
/* 270:    */ 
/* 271:269 */     ResultSet rs = null;
/* 272:270 */     ResultSet rs2 = null;
/* 273:271 */     Connection con = null;
/* 274:272 */     PreparedStatement prepstat2 = null;
/* 275:    */     try
/* 276:    */     {
/* 277:275 */       con = DConnect.getConnection();
/* 278:    */       
/* 279:277 */       String SQL = "SELECT * FROM service_definition s inner join service_forwarding k on s.keyword=k.keyword and s.account_id=k.account_id where s.account_id='" + accountId + "'";
/* 280:    */       
/* 281:279 */       PreparedStatement prepstat = con.prepareStatement(SQL);
/* 282:280 */       rs = prepstat.executeQuery();
/* 283:282 */       while (rs.next())
/* 284:    */       {
/* 285:283 */         ForwardedService service = new ForwardedService();
/* 286:    */         
/* 287:285 */         service.setAccountId(rs.getString("s.account_id"));
/* 288:286 */         service.setDefaultMessage(rs.getString("s.default_message"));
/* 289:287 */         service.setKeyword(rs.getString("s.keyword"));
/* 290:288 */         service.setLastUpdated(rs.getString("s.last_updated"));
/* 291:289 */         service.setServiceName(rs.getString("s.service_name"));
/* 292:290 */         service.setServiceType(rs.getString("s.service_type"));
/* 293:291 */         service.setListenStatus(rs.getInt("listen_status"));
/* 294:292 */         service.setTimeout(rs.getInt("timeout"));
/* 295:293 */         service.setUrl(rs.getString("url"));
/* 296:    */         
/* 297:295 */         services.add(service);
/* 298:    */       }
/* 299:    */     }
/* 300:    */     catch (Exception ex)
/* 301:    */     {
/* 302:299 */       if (con != null) {
/* 303:300 */         con.close();
/* 304:    */       }
/* 305:302 */       throw new Exception(ex.getMessage());
/* 306:    */     }
/* 307:304 */     if (con != null) {
/* 308:305 */       con.close();
/* 309:    */     }
/* 310:308 */     return services;
/* 311:    */   }
/* 312:    */   
/* 313:    */   public static void deleteForwardedService(String keyword, String account_id)
/* 314:    */     throws Exception
/* 315:    */   {
/* 316:314 */     ResultSet rs = null;
/* 317:315 */     Connection con = null;
/* 318:316 */     PreparedStatement prepstat = null;
/* 319:    */     try
/* 320:    */     {
/* 321:319 */       con = DConnect.getConnection();
/* 322:    */       
/* 323:    */ 
/* 324:322 */       String SQL = "delete from service_definition d, service_forwarding f where d.keyword='" + keyword + "' and d.account_id='" + account_id + "' and d.account_id=f.account_id and d.keyword=f.keyword";
/* 325:    */       
/* 326:324 */       prepstat = con.prepareStatement(SQL);
/* 327:325 */       prepstat.execute();
/* 328:    */     }
/* 329:    */     catch (Exception ex)
/* 330:    */     {
/* 331:330 */       throw new Exception(ex.getMessage());
/* 332:    */     }
/* 333:    */     finally
/* 334:    */     {
/* 335:333 */       if (con != null) {
/* 336:334 */         con.close();
/* 337:    */       }
/* 338:    */     }
/* 339:    */   }
/* 340:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.extension.ForwardedServiceDB
 * JD-Core Version:    0.7.0.1
 */