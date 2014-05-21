/*   1:    */ package com.rancard.mobility.infoserver.livescore;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import java.sql.Connection;
/*   5:    */ import java.sql.PreparedStatement;
/*   6:    */ import java.sql.ResultSet;
/*   7:    */ import java.text.SimpleDateFormat;
/*   8:    */ import java.util.ArrayList;
/*   9:    */ import java.util.Calendar;
/*  10:    */ 
/*  11:    */ public abstract class LiveScoreUpdateDB
/*  12:    */ {
/*  13:    */   public static boolean createUpdate(LiveScoreUpdate update)
/*  14:    */     throws Exception
/*  15:    */   {
/*  16: 33 */     ResultSet rs = null;
/*  17: 34 */     Connection con = null;
/*  18: 35 */     PreparedStatement prepstat = null;
/*  19: 36 */     boolean created = false;
/*  20: 37 */     String updateMessage = update.getEnglishMessage();
/*  21:    */     try
/*  22:    */     {
/*  23: 41 */       con = DConnect.getConnection();
/*  24:    */       
/*  25:    */ 
/*  26: 44 */       String SQL = "select * from livescore_fixtures where game_id='" + update.getEventId() + "'";
/*  27: 45 */       prepstat = con.prepareStatement(SQL);
/*  28: 46 */       rs = prepstat.executeQuery();
/*  29: 47 */       if (rs.next())
/*  30:    */       {
/*  31: 49 */         SQL = "select * from livescore_updates where update_id='" + update.getUpdateId() + "'";
/*  32: 50 */         prepstat = con.prepareStatement(SQL);
/*  33: 51 */         rs = prepstat.executeQuery();
/*  34: 52 */         if (!rs.next())
/*  35:    */         {
/*  36: 56 */           SQL = "insert into livescore_updates (update_id,game_id,event_trigger,message_en,publish_date,message_fr) values ('" + update.getUpdateId() + "','" + update.getEventId() + "','" + update.getTrigger() + "',?,'" + update.getPublishDate() + "',?)";
/*  37:    */           
/*  38: 58 */           prepstat = con.prepareStatement(SQL);
/*  39: 59 */           prepstat.setBytes(1, update.getEnglishMessage().getBytes());
/*  40: 60 */           prepstat.setBytes(2, update.getFrenchMessage().getBytes());
/*  41: 61 */           prepstat.execute();
/*  42: 62 */           created = true;
/*  43:    */         }
/*  44:    */         else
/*  45:    */         {
/*  46: 64 */           created = false;
/*  47:    */         }
/*  48:    */       }
/*  49:    */     }
/*  50:    */     catch (Exception ex)
/*  51:    */     {
/*  52: 68 */       if (con != null) {
/*  53: 69 */         con.close();
/*  54:    */       }
/*  55: 70 */       throw new Exception(ex.getMessage());
/*  56:    */     }
/*  57:    */     finally
/*  58:    */     {
/*  59: 72 */       if (con != null) {
/*  60: 73 */         con.close();
/*  61:    */       }
/*  62:    */     }
/*  63: 76 */     return created;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public static String viewUpdateMessage(String updateId, String language)
/*  67:    */     throws Exception
/*  68:    */   {
/*  69: 80 */     String message = "No updates yet";
/*  70:    */     
/*  71: 82 */     ResultSet rs = null;
/*  72: 83 */     Connection con = null;
/*  73: 84 */     PreparedStatement prepstat = null;
/*  74: 85 */     boolean created = false;
/*  75:    */     try
/*  76:    */     {
/*  77: 88 */       con = DConnect.getConnection();
/*  78:    */       
/*  79:    */ 
/*  80: 91 */       String SQL = "select * from livescore_updates lu inner join livescore_fixtures lf on lu.game_id=lf.game_id where lu.update_id='" + updateId + "'";
/*  81: 92 */       prepstat = con.prepareStatement(SQL);
/*  82: 93 */       rs = prepstat.executeQuery();
/*  83: 94 */       if (rs.next())
/*  84:    */       {
/*  85: 95 */         String columnName = "lu.message_" + language;
/*  86: 96 */         byte[] msgBytes = rs.getBytes(columnName);
/*  87:    */         
/*  88: 98 */         message = new String(msgBytes);
/*  89:    */       }
/*  90:    */     }
/*  91:    */     catch (Exception ex)
/*  92:    */     {
/*  93:101 */       if (con != null) {
/*  94:102 */         con.close();
/*  95:    */       }
/*  96:103 */       throw new Exception(ex.getMessage());
/*  97:    */     }
/*  98:    */     finally
/*  99:    */     {
/* 100:105 */       if (con != null) {
/* 101:106 */         con.close();
/* 102:    */       }
/* 103:    */     }
/* 104:109 */     return message;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public static LiveScoreUpdate viewUpdate(String updateId)
/* 108:    */     throws Exception
/* 109:    */   {
/* 110:113 */     LiveScoreUpdate update = new LiveScoreUpdate();
/* 111:    */     
/* 112:115 */     ResultSet rs = null;
/* 113:116 */     Connection con = null;
/* 114:117 */     PreparedStatement prepstat = null;
/* 115:118 */     boolean created = false;
/* 116:    */     try
/* 117:    */     {
/* 118:121 */       con = DConnect.getConnection();
/* 119:    */       
/* 120:    */ 
/* 121:124 */       String SQL = "select * from livescore_updates lu inner join livescore_fixtures lf on lu.game_id=lf.game_id where lu.update_id='" + updateId + "'";
/* 122:125 */       prepstat = con.prepareStatement(SQL);
/* 123:126 */       rs = prepstat.executeQuery();
/* 124:127 */       if (rs.next())
/* 125:    */       {
/* 126:128 */         update.setCountryName(rs.getString("lf.country_name"));
/* 127:129 */         update.setEventDate(rs.getString("lf.date"));
/* 128:130 */         update.setEventId(rs.getString("lf.game_id"));
/* 129:131 */         update.setEventName(rs.getString("lf.home_team") + " vs " + rs.getString("lf.away_team"));
/* 130:132 */         update.setEventStatus(rs.getString("lf.status"));
/* 131:133 */         update.setLeagueId(rs.getString("lf.league_id"));
/* 132:134 */         update.setLeagueName(rs.getString("lf.league_name"));
/* 133:    */         
/* 134:136 */         byte[] msgBytes = rs.getBytes("lu.message_en");
/* 135:137 */         String message = new String(msgBytes);
/* 136:138 */         update.setEnglishMessage(message);
/* 137:    */         
/* 138:140 */         msgBytes = rs.getBytes("lu.message_fr");
/* 139:141 */         message = new String(msgBytes);
/* 140:142 */         update.setFrenchMessage(message);
/* 141:    */         
/* 142:144 */         update.setTrigger(rs.getString("lu.event_trigger"));
/* 143:145 */         update.setUpdateId(rs.getString("lu.update_id"));
/* 144:146 */         ArrayList participants = new ArrayList();
/* 145:147 */         participants.add(rs.getString("lf.home_team"));
/* 146:148 */         participants.add(rs.getString("lf.away_team"));
/* 147:149 */         ArrayList scores = new ArrayList();
/* 148:150 */         participants.add(rs.getString("lf.home_score"));
/* 149:151 */         participants.add(rs.getString("lf.away_score"));
/* 150:152 */         update.setParticipants(participants);
/* 151:153 */         update.setScores(scores);
/* 152:154 */         update.setPublishDate(rs.getString("lu.publish_date"));
/* 153:    */       }
/* 154:    */     }
/* 155:    */     catch (Exception ex)
/* 156:    */     {
/* 157:157 */       if (con != null) {
/* 158:158 */         con.close();
/* 159:    */       }
/* 160:159 */       throw new Exception(ex.getMessage());
/* 161:    */     }
/* 162:    */     finally
/* 163:    */     {
/* 164:161 */       if (con != null) {
/* 165:162 */         con.close();
/* 166:    */       }
/* 167:    */     }
/* 168:165 */     return update;
/* 169:    */   }
/* 170:    */   
/* 171:    */   public static LiveScoreUpdate viewUpdate(String updateId, int gameStatus)
/* 172:    */     throws Exception
/* 173:    */   {
/* 174:169 */     LiveScoreUpdate update = new LiveScoreUpdate();
/* 175:    */     
/* 176:171 */     ResultSet rs = null;
/* 177:172 */     Connection con = null;
/* 178:173 */     PreparedStatement prepstat = null;
/* 179:174 */     boolean created = false;
/* 180:    */     try
/* 181:    */     {
/* 182:177 */       con = DConnect.getConnection();
/* 183:    */       
/* 184:    */ 
/* 185:180 */       String SQL = "select * from livescore_updates lu inner join livescore_fixtures lf on lu.game_id=lf.game_id where lu.update_id='" + updateId + "' and lf.status ='" + gameStatus + "'";
/* 186:181 */       prepstat = con.prepareStatement(SQL);
/* 187:182 */       rs = prepstat.executeQuery();
/* 188:183 */       if (rs.next())
/* 189:    */       {
/* 190:184 */         update.setCountryName(rs.getString("lf.country_name"));
/* 191:185 */         update.setEventDate(rs.getString("lf.date"));
/* 192:186 */         update.setEventId(rs.getString("lf.game_id"));
/* 193:187 */         update.setEventName(rs.getString("lf.home_team") + " vs " + rs.getString("lf.away_team"));
/* 194:188 */         update.setEventStatus(rs.getString("lf.status"));
/* 195:189 */         update.setLeagueId(rs.getString("lf.league_id"));
/* 196:190 */         update.setLeagueName(rs.getString("lf.league_name"));
/* 197:    */         
/* 198:192 */         byte[] msgBytes = rs.getBytes("lu.message_en");
/* 199:193 */         String message = new String(msgBytes);
/* 200:194 */         update.setEnglishMessage(message);
/* 201:    */         
/* 202:196 */         msgBytes = rs.getBytes("lu.message_fr");
/* 203:197 */         message = new String(msgBytes);
/* 204:198 */         update.setFrenchMessage(message);
/* 205:    */         
/* 206:200 */         update.setTrigger(rs.getString("lu.event_trigger"));
/* 207:201 */         update.setUpdateId(rs.getString("lu.update_id"));
/* 208:202 */         ArrayList participants = new ArrayList();
/* 209:203 */         participants.add(rs.getString("lf.home_team"));
/* 210:204 */         participants.add(rs.getString("lf.away_team"));
/* 211:205 */         ArrayList scores = new ArrayList();
/* 212:206 */         participants.add(rs.getString("lf.home_score"));
/* 213:207 */         participants.add(rs.getString("lf.away_score"));
/* 214:208 */         update.setParticipants(participants);
/* 215:209 */         update.setScores(scores);
/* 216:210 */         update.setPublishDate(rs.getString("lu.publish_date"));
/* 217:    */       }
/* 218:    */     }
/* 219:    */     catch (Exception ex)
/* 220:    */     {
/* 221:213 */       if (con != null) {
/* 222:214 */         con.close();
/* 223:    */       }
/* 224:215 */       throw new Exception(ex.getMessage());
/* 225:    */     }
/* 226:    */     finally
/* 227:    */     {
/* 228:217 */       if (con != null) {
/* 229:218 */         con.close();
/* 230:    */       }
/* 231:    */     }
/* 232:221 */     return update;
/* 233:    */   }
/* 234:    */   
/* 235:    */   public static ArrayList viewAllUpdates(String gameId)
/* 236:    */     throws Exception
/* 237:    */   {
/* 238:226 */     ArrayList updates = new ArrayList();
/* 239:    */     
/* 240:228 */     ResultSet rs = null;
/* 241:229 */     Connection con = null;
/* 242:230 */     PreparedStatement prepstat = null;
/* 243:231 */     boolean created = false;
/* 244:    */     try
/* 245:    */     {
/* 246:234 */       con = DConnect.getConnection();
/* 247:    */       
/* 248:    */ 
/* 249:237 */       String SQL = "select * from livescore_updates lu inner join livescore_fixtures lf on lu.game_id=lf.game_id where lf.game_id='" + gameId + "' order by publish_date desc";
/* 250:238 */       prepstat = con.prepareStatement(SQL);
/* 251:239 */       rs = prepstat.executeQuery();
/* 252:240 */       while (rs.next())
/* 253:    */       {
/* 254:241 */         LiveScoreUpdate update = new LiveScoreUpdate();
/* 255:242 */         update.setCountryName(rs.getString("lf.country_name"));
/* 256:243 */         update.setEventDate(rs.getString("lf.date"));
/* 257:244 */         update.setEventId(rs.getString("lf.game_id"));
/* 258:245 */         update.setEventName(rs.getString("lf.home_team") + " vs " + rs.getString("lf.away_team"));
/* 259:246 */         update.setEventStatus(rs.getString("lf.status"));
/* 260:247 */         update.setLeagueId(rs.getString("lf.league_id"));
/* 261:248 */         update.setLeagueName(rs.getString("lf.league_name"));
/* 262:    */         
/* 263:250 */         byte[] msgBytes = rs.getBytes("lu.message_en");
/* 264:251 */         String message = new String(msgBytes);
/* 265:252 */         update.setEnglishMessage(message);
/* 266:    */         
/* 267:254 */         msgBytes = rs.getBytes("lu.message_fr");
/* 268:255 */         message = new String(msgBytes);
/* 269:256 */         update.setFrenchMessage(message);
/* 270:    */         
/* 271:258 */         update.setTrigger(rs.getString("lu.event_trigger"));
/* 272:259 */         update.setUpdateId(rs.getString("lu.update_id"));
/* 273:260 */         ArrayList participants = new ArrayList();
/* 274:261 */         participants.add(rs.getString("lf.home_team"));
/* 275:262 */         participants.add(rs.getString("lf.away_team"));
/* 276:263 */         ArrayList scores = new ArrayList();
/* 277:264 */         participants.add(rs.getString("lf.home_score"));
/* 278:265 */         participants.add(rs.getString("lf.away_score"));
/* 279:266 */         update.setParticipants(participants);
/* 280:267 */         update.setScores(scores);
/* 281:268 */         update.setPublishDate(rs.getString("lu.publish_date"));
/* 282:    */         
/* 283:270 */         updates.add(update);
/* 284:    */       }
/* 285:    */     }
/* 286:    */     catch (Exception ex)
/* 287:    */     {
/* 288:273 */       if (con != null) {
/* 289:274 */         con.close();
/* 290:    */       }
/* 291:275 */       throw new Exception(ex.getMessage());
/* 292:    */     }
/* 293:    */     finally
/* 294:    */     {
/* 295:277 */       if (con != null) {
/* 296:278 */         con.close();
/* 297:    */       }
/* 298:    */     }
/* 299:281 */     return updates;
/* 300:    */   }
/* 301:    */   
/* 302:    */   public static ArrayList viewAllUpdates(String gameId, String date)
/* 303:    */     throws Exception
/* 304:    */   {
/* 305:285 */     ArrayList updates = new ArrayList();
/* 306:    */     
/* 307:287 */     ResultSet rs = null;
/* 308:288 */     Connection con = null;
/* 309:289 */     PreparedStatement prepstat = null;
/* 310:290 */     boolean created = false;
/* 311:    */     try
/* 312:    */     {
/* 313:293 */       con = DConnect.getConnection();
/* 314:    */       
/* 315:295 */       SimpleDateFormat s_formatter = new SimpleDateFormat("MM-dd-yyyy");
/* 316:296 */       java.util.Date validDate = null;
/* 317:    */       try
/* 318:    */       {
/* 319:298 */         validDate = s_formatter.parse(date);
/* 320:    */       }
/* 321:    */       catch (Exception ex)
/* 322:    */       {
/* 323:300 */         validDate = Calendar.getInstance().getTime();
/* 324:    */       }
/* 325:305 */       String SQL = "select * from livescore_updates lu inner join livescore_fixtures lf on lu.game_id=lf.game_id where lf.game_id=? and date(lf.date)=? order by publish_date desc";
/* 326:306 */       prepstat = con.prepareStatement(SQL);
/* 327:307 */       prepstat.setString(1, gameId);
/* 328:308 */       prepstat.setDate(2, new java.sql.Date(validDate.getTime()));
/* 329:    */       
/* 330:310 */       rs = prepstat.executeQuery();
/* 331:311 */       while (rs.next())
/* 332:    */       {
/* 333:312 */         LiveScoreUpdate update = new LiveScoreUpdate();
/* 334:313 */         update.setCountryName(rs.getString("lf.country_name"));
/* 335:314 */         update.setEventDate(rs.getString("lf.date"));
/* 336:315 */         update.setEventId(rs.getString("lf.game_id"));
/* 337:316 */         update.setEventName(rs.getString("lf.home_team") + " vs " + rs.getString("lf.away_team"));
/* 338:317 */         update.setEventStatus(rs.getString("lf.status"));
/* 339:318 */         update.setLeagueId(rs.getString("lf.league_id"));
/* 340:319 */         update.setLeagueName(rs.getString("lf.league_name"));
/* 341:    */         
/* 342:321 */         byte[] msgBytes = rs.getBytes("lu.message_en");
/* 343:322 */         String message = new String(msgBytes);
/* 344:323 */         update.setEnglishMessage(message);
/* 345:    */         
/* 346:325 */         msgBytes = rs.getBytes("lu.message_fr");
/* 347:326 */         message = new String(msgBytes);
/* 348:327 */         update.setFrenchMessage(message);
/* 349:    */         
/* 350:329 */         update.setTrigger(rs.getString("lu.event_trigger"));
/* 351:330 */         update.setUpdateId(rs.getString("lu.update_id"));
/* 352:331 */         ArrayList participants = new ArrayList();
/* 353:332 */         participants.add(rs.getString("lf.home_team"));
/* 354:333 */         participants.add(rs.getString("lf.away_team"));
/* 355:334 */         ArrayList scores = new ArrayList();
/* 356:335 */         participants.add(rs.getString("lf.home_score"));
/* 357:336 */         participants.add(rs.getString("lf.away_score"));
/* 358:337 */         update.setParticipants(participants);
/* 359:338 */         update.setScores(scores);
/* 360:339 */         update.setPublishDate(rs.getString("lu.publish_date"));
/* 361:    */         
/* 362:341 */         updates.add(update);
/* 363:    */       }
/* 364:    */     }
/* 365:    */     catch (Exception ex)
/* 366:    */     {
/* 367:344 */       if (con != null) {
/* 368:345 */         con.close();
/* 369:    */       }
/* 370:346 */       throw new Exception(ex.getMessage());
/* 371:    */     }
/* 372:    */     finally
/* 373:    */     {
/* 374:348 */       if (con != null) {
/* 375:349 */         con.close();
/* 376:    */       }
/* 377:    */     }
/* 378:352 */     return updates;
/* 379:    */   }
/* 380:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.livescore.LiveScoreUpdateDB
 * JD-Core Version:    0.7.0.1
 */