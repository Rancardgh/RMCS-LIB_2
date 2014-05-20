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
/*  52: 70 */       throw new Exception(ex.getMessage());
/*  53:    */     }
/*  54:    */     finally
/*  55:    */     {
/*  56: 72 */       if (con != null) {
/*  57: 73 */         con.close();
/*  58:    */       }
/*  59:    */     }
/*  60: 76 */     return created;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public static String viewUpdateMessage(String updateId, String language)
/*  64:    */     throws Exception
/*  65:    */   {
/*  66: 80 */     String message = "No updates yet";
/*  67:    */     
/*  68: 82 */     ResultSet rs = null;
/*  69: 83 */     Connection con = null;
/*  70: 84 */     PreparedStatement prepstat = null;
/*  71: 85 */     boolean created = false;
/*  72:    */     try
/*  73:    */     {
/*  74: 88 */       con = DConnect.getConnection();
/*  75:    */       
/*  76:    */ 
/*  77: 91 */       String SQL = "select * from livescore_updates lu inner join livescore_fixtures lf on lu.game_id=lf.game_id where lu.update_id='" + updateId + "'";
/*  78: 92 */       prepstat = con.prepareStatement(SQL);
/*  79: 93 */       rs = prepstat.executeQuery();
/*  80: 94 */       if (rs.next())
/*  81:    */       {
/*  82: 95 */         String columnName = "lu.message_" + language;
/*  83: 96 */         byte[] msgBytes = rs.getBytes(columnName);
/*  84:    */         
/*  85: 98 */         message = new String(msgBytes);
/*  86:    */       }
/*  87:    */     }
/*  88:    */     catch (Exception ex)
/*  89:    */     {
/*  90:103 */       throw new Exception(ex.getMessage());
/*  91:    */     }
/*  92:    */     finally
/*  93:    */     {
/*  94:105 */       if (con != null) {
/*  95:106 */         con.close();
/*  96:    */       }
/*  97:    */     }
/*  98:109 */     return message;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public static LiveScoreUpdate viewUpdate(String updateId)
/* 102:    */     throws Exception
/* 103:    */   {
/* 104:113 */     LiveScoreUpdate update = new LiveScoreUpdate();
/* 105:    */     
/* 106:115 */     ResultSet rs = null;
/* 107:116 */     Connection con = null;
/* 108:117 */     PreparedStatement prepstat = null;
/* 109:118 */     boolean created = false;
/* 110:    */     try
/* 111:    */     {
/* 112:121 */       con = DConnect.getConnection();
/* 113:    */       
/* 114:    */ 
/* 115:124 */       String SQL = "select * from livescore_updates lu inner join livescore_fixtures lf on lu.game_id=lf.game_id where lu.update_id='" + updateId + "'";
/* 116:125 */       prepstat = con.prepareStatement(SQL);
/* 117:126 */       rs = prepstat.executeQuery();
/* 118:127 */       if (rs.next())
/* 119:    */       {
/* 120:128 */         update.setCountryName(rs.getString("lf.country_name"));
/* 121:129 */         update.setEventDate(rs.getString("lf.date"));
/* 122:130 */         update.setEventId(rs.getString("lf.game_id"));
/* 123:131 */         update.setEventName(rs.getString("lf.home_team") + " vs " + rs.getString("lf.away_team"));
/* 124:132 */         update.setEventStatus(rs.getString("lf.status"));
/* 125:133 */         update.setLeagueId(rs.getString("lf.league_id"));
/* 126:134 */         update.setLeagueName(rs.getString("lf.league_name"));
/* 127:    */         
/* 128:136 */         byte[] msgBytes = rs.getBytes("lu.message_en");
/* 129:137 */         String message = new String(msgBytes);
/* 130:138 */         update.setEnglishMessage(message);
/* 131:    */         
/* 132:140 */         msgBytes = rs.getBytes("lu.message_fr");
/* 133:141 */         message = new String(msgBytes);
/* 134:142 */         update.setFrenchMessage(message);
/* 135:    */         
/* 136:144 */         update.setTrigger(rs.getString("lu.event_trigger"));
/* 137:145 */         update.setUpdateId(rs.getString("lu.update_id"));
/* 138:146 */         ArrayList participants = new ArrayList();
/* 139:147 */         participants.add(rs.getString("lf.home_team"));
/* 140:148 */         participants.add(rs.getString("lf.away_team"));
/* 141:149 */         ArrayList scores = new ArrayList();
/* 142:150 */         participants.add(rs.getString("lf.home_score"));
/* 143:151 */         participants.add(rs.getString("lf.away_score"));
/* 144:152 */         update.setParticipants(participants);
/* 145:153 */         update.setScores(scores);
/* 146:154 */         update.setPublishDate(rs.getString("lu.publish_date"));
/* 147:    */       }
/* 148:    */     }
/* 149:    */     catch (Exception ex)
/* 150:    */     {
/* 151:159 */       throw new Exception(ex.getMessage());
/* 152:    */     }
/* 153:    */     finally
/* 154:    */     {
/* 155:161 */       if (con != null) {
/* 156:162 */         con.close();
/* 157:    */       }
/* 158:    */     }
/* 159:165 */     return update;
/* 160:    */   }
/* 161:    */   
/* 162:    */   public static LiveScoreUpdate viewUpdate(String updateId, int gameStatus)
/* 163:    */     throws Exception
/* 164:    */   {
/* 165:169 */     LiveScoreUpdate update = new LiveScoreUpdate();
/* 166:    */     
/* 167:171 */     ResultSet rs = null;
/* 168:172 */     Connection con = null;
/* 169:173 */     PreparedStatement prepstat = null;
/* 170:174 */     boolean created = false;
/* 171:    */     try
/* 172:    */     {
/* 173:177 */       con = DConnect.getConnection();
/* 174:    */       
/* 175:    */ 
/* 176:180 */       String SQL = "select * from livescore_updates lu inner join livescore_fixtures lf on lu.game_id=lf.game_id where lu.update_id='" + updateId + "' and lf.status ='" + gameStatus + "'";
/* 177:181 */       prepstat = con.prepareStatement(SQL);
/* 178:182 */       rs = prepstat.executeQuery();
/* 179:183 */       if (rs.next())
/* 180:    */       {
/* 181:184 */         update.setCountryName(rs.getString("lf.country_name"));
/* 182:185 */         update.setEventDate(rs.getString("lf.date"));
/* 183:186 */         update.setEventId(rs.getString("lf.game_id"));
/* 184:187 */         update.setEventName(rs.getString("lf.home_team") + " vs " + rs.getString("lf.away_team"));
/* 185:188 */         update.setEventStatus(rs.getString("lf.status"));
/* 186:189 */         update.setLeagueId(rs.getString("lf.league_id"));
/* 187:190 */         update.setLeagueName(rs.getString("lf.league_name"));
/* 188:    */         
/* 189:192 */         byte[] msgBytes = rs.getBytes("lu.message_en");
/* 190:193 */         String message = new String(msgBytes);
/* 191:194 */         update.setEnglishMessage(message);
/* 192:    */         
/* 193:196 */         msgBytes = rs.getBytes("lu.message_fr");
/* 194:197 */         message = new String(msgBytes);
/* 195:198 */         update.setFrenchMessage(message);
/* 196:    */         
/* 197:200 */         update.setTrigger(rs.getString("lu.event_trigger"));
/* 198:201 */         update.setUpdateId(rs.getString("lu.update_id"));
/* 199:202 */         ArrayList participants = new ArrayList();
/* 200:203 */         participants.add(rs.getString("lf.home_team"));
/* 201:204 */         participants.add(rs.getString("lf.away_team"));
/* 202:205 */         ArrayList scores = new ArrayList();
/* 203:206 */         participants.add(rs.getString("lf.home_score"));
/* 204:207 */         participants.add(rs.getString("lf.away_score"));
/* 205:208 */         update.setParticipants(participants);
/* 206:209 */         update.setScores(scores);
/* 207:210 */         update.setPublishDate(rs.getString("lu.publish_date"));
/* 208:    */       }
/* 209:    */     }
/* 210:    */     catch (Exception ex)
/* 211:    */     {
/* 212:215 */       throw new Exception(ex.getMessage());
/* 213:    */     }
/* 214:    */     finally
/* 215:    */     {
/* 216:217 */       if (con != null) {
/* 217:218 */         con.close();
/* 218:    */       }
/* 219:    */     }
/* 220:221 */     return update;
/* 221:    */   }
/* 222:    */   
/* 223:    */   public static ArrayList viewAllUpdates(String gameId)
/* 224:    */     throws Exception
/* 225:    */   {
/* 226:226 */     ArrayList updates = new ArrayList();
/* 227:    */     
/* 228:228 */     ResultSet rs = null;
/* 229:229 */     Connection con = null;
/* 230:230 */     PreparedStatement prepstat = null;
/* 231:231 */     boolean created = false;
/* 232:    */     try
/* 233:    */     {
/* 234:234 */       con = DConnect.getConnection();
/* 235:    */       
/* 236:    */ 
/* 237:237 */       String SQL = "select * from livescore_updates lu inner join livescore_fixtures lf on lu.game_id=lf.game_id where lf.game_id='" + gameId + "' order by publish_date desc";
/* 238:238 */       prepstat = con.prepareStatement(SQL);
/* 239:239 */       rs = prepstat.executeQuery();
/* 240:240 */       while (rs.next())
/* 241:    */       {
/* 242:241 */         LiveScoreUpdate update = new LiveScoreUpdate();
/* 243:242 */         update.setCountryName(rs.getString("lf.country_name"));
/* 244:243 */         update.setEventDate(rs.getString("lf.date"));
/* 245:244 */         update.setEventId(rs.getString("lf.game_id"));
/* 246:245 */         update.setEventName(rs.getString("lf.home_team") + " vs " + rs.getString("lf.away_team"));
/* 247:246 */         update.setEventStatus(rs.getString("lf.status"));
/* 248:247 */         update.setLeagueId(rs.getString("lf.league_id"));
/* 249:248 */         update.setLeagueName(rs.getString("lf.league_name"));
/* 250:    */         
/* 251:250 */         byte[] msgBytes = rs.getBytes("lu.message_en");
/* 252:251 */         String message = new String(msgBytes);
/* 253:252 */         update.setEnglishMessage(message);
/* 254:    */         
/* 255:254 */         msgBytes = rs.getBytes("lu.message_fr");
/* 256:255 */         message = new String(msgBytes);
/* 257:256 */         update.setFrenchMessage(message);
/* 258:    */         
/* 259:258 */         update.setTrigger(rs.getString("lu.event_trigger"));
/* 260:259 */         update.setUpdateId(rs.getString("lu.update_id"));
/* 261:260 */         ArrayList participants = new ArrayList();
/* 262:261 */         participants.add(rs.getString("lf.home_team"));
/* 263:262 */         participants.add(rs.getString("lf.away_team"));
/* 264:263 */         ArrayList scores = new ArrayList();
/* 265:264 */         participants.add(rs.getString("lf.home_score"));
/* 266:265 */         participants.add(rs.getString("lf.away_score"));
/* 267:266 */         update.setParticipants(participants);
/* 268:267 */         update.setScores(scores);
/* 269:268 */         update.setPublishDate(rs.getString("lu.publish_date"));
/* 270:    */         
/* 271:270 */         updates.add(update);
/* 272:    */       }
/* 273:    */     }
/* 274:    */     catch (Exception ex)
/* 275:    */     {
/* 276:275 */       throw new Exception(ex.getMessage());
/* 277:    */     }
/* 278:    */     finally
/* 279:    */     {
/* 280:277 */       if (con != null) {
/* 281:278 */         con.close();
/* 282:    */       }
/* 283:    */     }
/* 284:281 */     return updates;
/* 285:    */   }
/* 286:    */   
/* 287:    */   public static ArrayList viewAllUpdates(String gameId, String date)
/* 288:    */     throws Exception
/* 289:    */   {
/* 290:285 */     ArrayList updates = new ArrayList();
/* 291:    */     
/* 292:287 */     ResultSet rs = null;
/* 293:288 */     Connection con = null;
/* 294:289 */     PreparedStatement prepstat = null;
/* 295:290 */     boolean created = false;
/* 296:    */     try
/* 297:    */     {
/* 298:293 */       con = DConnect.getConnection();
/* 299:    */       
/* 300:295 */       SimpleDateFormat s_formatter = new SimpleDateFormat("MM-dd-yyyy");
/* 301:296 */       java.util.Date validDate = null;
/* 302:    */       try
/* 303:    */       {
/* 304:298 */         validDate = s_formatter.parse(date);
/* 305:    */       }
/* 306:    */       catch (Exception ex)
/* 307:    */       {
/* 308:300 */         validDate = Calendar.getInstance().getTime();
/* 309:    */       }
/* 310:305 */       String SQL = "select * from livescore_updates lu inner join livescore_fixtures lf on lu.game_id=lf.game_id where lf.game_id=? and date(lf.date)=? order by publish_date desc";
/* 311:306 */       prepstat = con.prepareStatement(SQL);
/* 312:307 */       prepstat.setString(1, gameId);
/* 313:308 */       prepstat.setDate(2, new java.sql.Date(validDate.getTime()));
/* 314:    */       
/* 315:310 */       rs = prepstat.executeQuery();
/* 316:311 */       while (rs.next())
/* 317:    */       {
/* 318:312 */         LiveScoreUpdate update = new LiveScoreUpdate();
/* 319:313 */         update.setCountryName(rs.getString("lf.country_name"));
/* 320:314 */         update.setEventDate(rs.getString("lf.date"));
/* 321:315 */         update.setEventId(rs.getString("lf.game_id"));
/* 322:316 */         update.setEventName(rs.getString("lf.home_team") + " vs " + rs.getString("lf.away_team"));
/* 323:317 */         update.setEventStatus(rs.getString("lf.status"));
/* 324:318 */         update.setLeagueId(rs.getString("lf.league_id"));
/* 325:319 */         update.setLeagueName(rs.getString("lf.league_name"));
/* 326:    */         
/* 327:321 */         byte[] msgBytes = rs.getBytes("lu.message_en");
/* 328:322 */         String message = new String(msgBytes);
/* 329:323 */         update.setEnglishMessage(message);
/* 330:    */         
/* 331:325 */         msgBytes = rs.getBytes("lu.message_fr");
/* 332:326 */         message = new String(msgBytes);
/* 333:327 */         update.setFrenchMessage(message);
/* 334:    */         
/* 335:329 */         update.setTrigger(rs.getString("lu.event_trigger"));
/* 336:330 */         update.setUpdateId(rs.getString("lu.update_id"));
/* 337:331 */         ArrayList participants = new ArrayList();
/* 338:332 */         participants.add(rs.getString("lf.home_team"));
/* 339:333 */         participants.add(rs.getString("lf.away_team"));
/* 340:334 */         ArrayList scores = new ArrayList();
/* 341:335 */         participants.add(rs.getString("lf.home_score"));
/* 342:336 */         participants.add(rs.getString("lf.away_score"));
/* 343:337 */         update.setParticipants(participants);
/* 344:338 */         update.setScores(scores);
/* 345:339 */         update.setPublishDate(rs.getString("lu.publish_date"));
/* 346:    */         
/* 347:341 */         updates.add(update);
/* 348:    */       }
/* 349:    */     }
/* 350:    */     catch (Exception ex)
/* 351:    */     {
/* 352:346 */       throw new Exception(ex.getMessage());
/* 353:    */     }
/* 354:    */     finally
/* 355:    */     {
/* 356:348 */       if (con != null) {
/* 357:349 */         con.close();
/* 358:    */       }
/* 359:    */     }
/* 360:352 */     return updates;
/* 361:    */   }
/* 362:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.infoserver.livescore.LiveScoreUpdateDB
 * JD-Core Version:    0.7.0.1
 */