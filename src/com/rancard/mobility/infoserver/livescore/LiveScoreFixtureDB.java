/*    1:     */ package com.rancard.mobility.infoserver.livescore;
/*    2:     */ 
/*    3:     */ import com.rancard.common.DConnect;
/*    4:     */ import com.rancard.common.uidGen;
/*    5:     */ import com.rancard.util.PropertyHolder;
/*    6:     */ import java.io.PrintStream;
/*    7:     */ import java.sql.Connection;
/*    8:     */ import java.sql.PreparedStatement;
/*    9:     */ import java.sql.ResultSet;
/*   10:     */ import java.sql.SQLException;
/*   11:     */ import java.sql.Timestamp;
/*   12:     */ import java.text.SimpleDateFormat;
/*   13:     */ import java.util.ArrayList;
/*   14:     */ import java.util.Calendar;
/*   15:     */ import java.util.HashMap;
/*   16:     */ 
/*   17:     */ public abstract class LiveScoreFixtureDB
/*   18:     */ {
/*   19:     */   public static void createFixture(LiveScoreFixture game)
/*   20:     */     throws Exception
/*   21:     */   {
/*   22:  34 */     ResultSet rs = null;
/*   23:  35 */     Connection con = null;
/*   24:  36 */     PreparedStatement prepstat = null;
/*   25:     */     try
/*   26:     */     {
/*   27:  39 */       con = DConnect.getConnection();
/*   28:     */       
/*   29:  41 */       String SQL = "Select * from livescore_fixtures where game_id='" + game.getGameId() + "'";
/*   30:  42 */       prepstat = con.prepareStatement(SQL);
/*   31:  43 */       rs = prepstat.executeQuery();
/*   32:  45 */       if (!rs.next())
/*   33:     */       {
/*   34:  46 */         SQL = "insert into livescore_fixtures (game_id,country_name,league_id,league_name,home_team,home_score,away_team,away_score,date,status) values(?,?,?,?,?,?,?,?,?,?)";
/*   35:  47 */         prepstat = con.prepareStatement(SQL);
/*   36:  48 */         prepstat.setString(1, game.getGameId());
/*   37:  49 */         prepstat.setString(2, game.getCountryName());
/*   38:  50 */         prepstat.setString(3, game.getLeagueId());
/*   39:  51 */         prepstat.setString(4, game.getLeagueName());
/*   40:  52 */         prepstat.setString(5, game.getHomeTeam());
/*   41:  53 */         prepstat.setString(6, game.getHomeScore());
/*   42:  54 */         prepstat.setString(7, game.getAwayTeam());
/*   43:  55 */         prepstat.setString(8, game.getAwayScore());
/*   44:  56 */         prepstat.setString(9, game.getDate());
/*   45:  57 */         prepstat.setInt(10, game.getStatus());
/*   46:  58 */         prepstat.execute();
/*   47:     */         
/*   48:     */ 
/*   49:  61 */         SQL = "select ";
/*   50:     */       }
/*   51:     */       else
/*   52:     */       {
/*   53:  63 */         SQL = "update livescore_fixtures set country_name=?, league_id=?, league_name=?, home_team=?, home_score=?, away_team=?, away_score=?, livescore_fixtures.status=?, date=? where livescore_fixtures.game_id=?";
/*   54:     */         
/*   55:  65 */         prepstat = con.prepareStatement(SQL);
/*   56:  66 */         prepstat.setString(1, game.getCountryName());
/*   57:  67 */         prepstat.setString(2, game.getLeagueId());
/*   58:  68 */         prepstat.setString(3, game.getLeagueName());
/*   59:  69 */         prepstat.setString(4, game.getHomeTeam());
/*   60:  70 */         prepstat.setString(5, game.getHomeScore());
/*   61:  71 */         prepstat.setString(6, game.getAwayTeam());
/*   62:  72 */         prepstat.setString(7, game.getAwayScore());
/*   63:  73 */         prepstat.setInt(8, game.getStatus());
/*   64:  74 */         prepstat.setString(9, game.getDate());
/*   65:  75 */         prepstat.setString(10, game.getGameId());
/*   66:  76 */         prepstat.execute();
/*   67:     */       }
/*   68:     */     }
/*   69:     */     catch (Exception ex)
/*   70:     */     {
/*   71:  79 */       if (con != null) {
/*   72:  80 */         con.close();
/*   73:     */       }
/*   74:  82 */       System.out.println("Could not create/update entry for " + game.getGameId() + ": " + game.getHomeTeam() + " vs " + game.getAwayTeam() + ". Error message: " + ex.getMessage());
/*   75:     */     }
/*   76:     */     finally
/*   77:     */     {
/*   78:  85 */       if (con != null) {
/*   79:  86 */         con.close();
/*   80:     */       }
/*   81:     */     }
/*   82:     */   }
/*   83:     */   
/*   84:     */   public static void updateFixture(LiveScoreFixture update)
/*   85:     */     throws Exception
/*   86:     */   {
/*   87:  94 */     ResultSet rs = null;
/*   88:  95 */     Connection con = null;
/*   89:  96 */     PreparedStatement prepstat = null;
/*   90:     */     try
/*   91:     */     {
/*   92:  99 */       con = DConnect.getConnection();
/*   93:     */       
/*   94: 101 */       String SQL = "update livescore_fixtures set country_name=?, league_id=?, league_name=?, home_team=?, home_score=?, away_team=?, away_score=?, livescore_fixtures.status=?, date=?, event_notif_sent=? where livescore_fixtures.game_id=?";
/*   95:     */       
/*   96: 103 */       prepstat = con.prepareStatement(SQL);
/*   97: 104 */       prepstat.setString(1, update.getCountryName());
/*   98: 105 */       prepstat.setString(2, update.getLeagueId());
/*   99: 106 */       prepstat.setString(3, update.getLeagueName());
/*  100: 107 */       prepstat.setString(4, update.getHomeTeam());
/*  101: 108 */       prepstat.setString(5, update.getHomeScore());
/*  102: 109 */       prepstat.setString(6, update.getAwayTeam());
/*  103: 110 */       prepstat.setString(7, update.getAwayScore());
/*  104: 111 */       prepstat.setInt(8, update.getStatus());
/*  105: 112 */       prepstat.setString(9, update.getDate());
/*  106: 113 */       prepstat.setInt(10, update.getEventNotifSent());
/*  107: 114 */       prepstat.setString(11, update.getGameId());
/*  108: 115 */       prepstat.execute();
/*  109:     */     }
/*  110:     */     catch (Exception ex)
/*  111:     */     {
/*  112: 120 */       throw new Exception(ex.getMessage());
/*  113:     */     }
/*  114:     */     finally
/*  115:     */     {
/*  116: 123 */       if (con != null) {
/*  117: 124 */         con.close();
/*  118:     */       }
/*  119:     */     }
/*  120:     */   }
/*  121:     */   
/*  122:     */   public static void updateFixture(String match_id, int status, String date)
/*  123:     */     throws Exception
/*  124:     */   {
/*  125: 132 */     ResultSet rs = null;
/*  126: 133 */     Connection con = null;
/*  127: 134 */     PreparedStatement prepstat = null;
/*  128:     */     try
/*  129:     */     {
/*  130: 137 */       con = DConnect.getConnection();
/*  131:     */       
/*  132:     */ 
/*  133: 140 */       String SQL = "update livescore_fixtures set livescore_fixtures.status=?, date=? where livescore_fixtures.game_id=?";
/*  134: 141 */       prepstat = con.prepareStatement(SQL);
/*  135: 142 */       prepstat.setInt(1, status);
/*  136: 143 */       prepstat.setString(2, date);
/*  137: 144 */       prepstat.setString(3, match_id);
/*  138: 145 */       prepstat.execute();
/*  139:     */     }
/*  140:     */     catch (Exception ex)
/*  141:     */     {
/*  142: 150 */       throw new Exception(ex.getMessage());
/*  143:     */     }
/*  144:     */     finally
/*  145:     */     {
/*  146: 153 */       if (con != null) {
/*  147: 154 */         con.close();
/*  148:     */       }
/*  149:     */     }
/*  150:     */   }
/*  151:     */   
/*  152:     */   public static void updateFixture(String match_id, String homeScore, String awayScore, String date)
/*  153:     */     throws Exception
/*  154:     */   {
/*  155: 162 */     ResultSet rs = null;
/*  156: 163 */     Connection con = null;
/*  157: 164 */     PreparedStatement prepstat = null;
/*  158:     */     try
/*  159:     */     {
/*  160: 167 */       con = DConnect.getConnection();
/*  161:     */       
/*  162:     */ 
/*  163: 170 */       String SQL = "update livescore_fixtures set home_score=?, away_score=?, date=? where livescore_fixtures.game_id=?";
/*  164: 171 */       prepstat = con.prepareStatement(SQL);
/*  165: 172 */       prepstat.setString(1, homeScore);
/*  166: 173 */       prepstat.setString(2, awayScore);
/*  167: 174 */       prepstat.setString(3, date);
/*  168: 175 */       prepstat.setString(4, match_id);
/*  169: 176 */       prepstat.execute();
/*  170:     */     }
/*  171:     */     catch (Exception ex)
/*  172:     */     {
/*  173: 181 */       throw new Exception(ex.getMessage());
/*  174:     */     }
/*  175:     */     finally
/*  176:     */     {
/*  177: 184 */       if (con != null) {
/*  178: 185 */         con.close();
/*  179:     */       }
/*  180:     */     }
/*  181:     */   }
/*  182:     */   
/*  183:     */   public static void updateFixture(String match_id, String homeScore, String awayScore, int status, String date)
/*  184:     */     throws Exception
/*  185:     */   {
/*  186: 193 */     ResultSet rs = null;
/*  187: 194 */     Connection con = null;
/*  188: 195 */     PreparedStatement prepstat = null;
/*  189:     */     try
/*  190:     */     {
/*  191: 198 */       con = DConnect.getConnection();
/*  192:     */       
/*  193:     */ 
/*  194: 201 */       String SQL = "update livescore_fixtures set home_score=?, away_score=?, livescore_fixtures.status=?, date=? where livescore_fixtures.game_id=?";
/*  195: 202 */       prepstat = con.prepareStatement(SQL);
/*  196: 203 */       prepstat.setString(1, homeScore);
/*  197: 204 */       prepstat.setString(2, awayScore);
/*  198: 205 */       prepstat.setInt(3, status);
/*  199: 206 */       prepstat.setString(4, date);
/*  200: 207 */       prepstat.setString(5, match_id);
/*  201: 208 */       prepstat.execute();
/*  202:     */     }
/*  203:     */     catch (Exception ex)
/*  204:     */     {
/*  205: 213 */       throw new Exception(ex.getMessage());
/*  206:     */     }
/*  207:     */     finally
/*  208:     */     {
/*  209: 216 */       if (con != null) {
/*  210: 217 */         con.close();
/*  211:     */       }
/*  212:     */     }
/*  213:     */   }
/*  214:     */   
/*  215:     */   public static LiveScoreFixture viewFixture(String gameId)
/*  216:     */     throws Exception
/*  217:     */   {
/*  218: 224 */     LiveScoreFixture fixture = new LiveScoreFixture();
/*  219:     */     
/*  220:     */ 
/*  221: 227 */     ResultSet rs = null;
/*  222: 228 */     Connection con = null;
/*  223: 229 */     PreparedStatement prepstat = null;
/*  224:     */     try
/*  225:     */     {
/*  226: 231 */       con = DConnect.getConnection();
/*  227:     */       
/*  228: 233 */       String SQL = "SELECT * FROM livescore_fixtures where game_id='" + gameId + "'";
/*  229:     */       
/*  230:     */ 
/*  231: 236 */       prepstat = con.prepareStatement(SQL);
/*  232: 237 */       rs = prepstat.executeQuery();
/*  233: 239 */       while (rs.next())
/*  234:     */       {
/*  235: 240 */         fixture.setCountryName(rs.getString("country_name"));
/*  236: 241 */         fixture.setDate(rs.getTimestamp("date").toString());
/*  237: 242 */         fixture.setGameId(rs.getString("game_id"));
/*  238: 243 */         fixture.setLeagueId(rs.getString("league_id"));
/*  239: 244 */         fixture.setLeagueName(rs.getString("league_name"));
/*  240: 245 */         fixture.setHomeTeam(rs.getString("home_team"));
/*  241: 246 */         fixture.setHomeScore(rs.getString("home_score"));
/*  242: 247 */         fixture.setAwayTeam(rs.getString("away_team"));
/*  243: 248 */         fixture.setAwayScore(rs.getString("away_score"));
/*  244: 249 */         fixture.setStatus(rs.getInt("status"));
/*  245: 250 */         fixture.setEventNotifSent(rs.getInt("event_notif_sent"));
/*  246:     */       }
/*  247: 253 */       SQL = "SELECT * FROM livescore_game_alias where game_id='" + gameId + "'";
/*  248: 254 */       prepstat = con.prepareStatement(SQL);
/*  249: 255 */       rs = prepstat.executeQuery();
/*  250: 256 */       while (rs.next()) {
/*  251: 257 */         fixture.setAlias(rs.getString("alias"));
/*  252:     */       }
/*  253:     */     }
/*  254:     */     catch (Exception ex)
/*  255:     */     {
/*  256: 260 */       if (con != null) {
/*  257: 261 */         con.close();
/*  258:     */       }
/*  259: 263 */       throw new Exception(ex.getMessage());
/*  260:     */     }
/*  261: 265 */     if (con != null) {
/*  262: 266 */       con.close();
/*  263:     */     }
/*  264: 269 */     return fixture;
/*  265:     */   }
/*  266:     */   
/*  267:     */   public static LiveScoreFixture viewFixtureForCP(String gameId, String accountId)
/*  268:     */     throws Exception
/*  269:     */   {
/*  270: 273 */     LiveScoreFixture fixture = new LiveScoreFixture();
/*  271:     */     
/*  272:     */ 
/*  273: 276 */     ResultSet rs = null;
/*  274: 277 */     Connection con = null;
/*  275: 278 */     PreparedStatement prepstat = null;
/*  276:     */     try
/*  277:     */     {
/*  278: 280 */       con = DConnect.getConnection();
/*  279:     */       
/*  280: 282 */       String SQL = "SELECT * FROM livescore_fixture_mgt lfm inner join livescore_fixtures lf on lf.game_id=lfm.game_id where lfm.game_id='" + gameId + "' and lfm.account_id='" + accountId + "'";
/*  281:     */       
/*  282:     */ 
/*  283:     */ 
/*  284: 286 */       prepstat = con.prepareStatement(SQL);
/*  285: 287 */       rs = prepstat.executeQuery();
/*  286: 289 */       while (rs.next())
/*  287:     */       {
/*  288: 290 */         fixture.setCountryName(rs.getString("country_name"));
/*  289: 291 */         fixture.setDate(rs.getTimestamp("date").toString());
/*  290: 292 */         fixture.setGameId(rs.getString("game_id"));
/*  291: 293 */         fixture.setLeagueId(rs.getString("league_id"));
/*  292: 294 */         fixture.setLeagueName(rs.getString("league_name"));
/*  293: 295 */         fixture.setHomeTeam(rs.getString("home_team"));
/*  294: 296 */         fixture.setHomeScore(rs.getString("home_score"));
/*  295: 297 */         fixture.setAwayTeam(rs.getString("away_team"));
/*  296: 298 */         fixture.setAwayScore(rs.getString("away_score"));
/*  297: 299 */         fixture.setStatus(rs.getInt("status"));
/*  298: 300 */         fixture.setEventNotifSent(rs.getInt("event_notif_sent"));
/*  299:     */       }
/*  300: 303 */       SQL = "SELECT * FROM livescore_game_alias where game_id='" + gameId + "'";
/*  301: 304 */       prepstat = con.prepareStatement(SQL);
/*  302: 305 */       rs = prepstat.executeQuery();
/*  303: 306 */       while (rs.next()) {
/*  304: 307 */         fixture.setAlias(rs.getString("alias"));
/*  305:     */       }
/*  306:     */     }
/*  307:     */     catch (Exception ex)
/*  308:     */     {
/*  309: 310 */       if (con != null) {
/*  310: 311 */         con.close();
/*  311:     */       }
/*  312: 313 */       throw new Exception(ex.getMessage());
/*  313:     */     }
/*  314: 315 */     if (con != null) {
/*  315: 316 */       con.close();
/*  316:     */     }
/*  317: 319 */     return fixture;
/*  318:     */   }
/*  319:     */   
/*  320:     */   public static LiveScoreFixture viewFixture(String gameId, String date)
/*  321:     */     throws Exception
/*  322:     */   {
/*  323: 323 */     LiveScoreFixture fixture = new LiveScoreFixture();
/*  324:     */     
/*  325:     */ 
/*  326: 326 */     ResultSet rs = null;
/*  327: 327 */     Connection con = null;
/*  328: 328 */     PreparedStatement prepstat = null;
/*  329:     */     try
/*  330:     */     {
/*  331: 332 */       con = DConnect.getConnection();
/*  332:     */       
/*  333: 334 */       SimpleDateFormat s_formatter = new SimpleDateFormat("MM-dd-yyyy");
/*  334: 335 */       java.util.Date validDate = null;
/*  335:     */       try
/*  336:     */       {
/*  337: 337 */         validDate = s_formatter.parse(date);
/*  338:     */       }
/*  339:     */       catch (Exception ex)
/*  340:     */       {
/*  341: 339 */         validDate = Calendar.getInstance().getTime();
/*  342:     */       }
/*  343: 343 */       String SQL = "SELECT * FROM livescore_fixtures where game_id=? and DATE(livescore_fixtures.date) =?";
/*  344:     */       
/*  345: 345 */       prepstat = con.prepareStatement(SQL);
/*  346: 346 */       prepstat.setString(1, gameId);
/*  347: 347 */       prepstat.setDate(2, new java.sql.Date(validDate.getTime()));
/*  348:     */       
/*  349: 349 */       rs = prepstat.executeQuery();
/*  350: 351 */       while (rs.next())
/*  351:     */       {
/*  352: 352 */         fixture.setCountryName(rs.getString("country_name"));
/*  353: 353 */         fixture.setDate(rs.getTimestamp("date").toString());
/*  354: 354 */         fixture.setGameId(rs.getString("game_id"));
/*  355: 355 */         fixture.setLeagueId(rs.getString("league_id"));
/*  356: 356 */         fixture.setLeagueName(rs.getString("league_name"));
/*  357: 357 */         fixture.setHomeTeam(rs.getString("home_team"));
/*  358: 358 */         fixture.setHomeScore(rs.getString("home_score"));
/*  359: 359 */         fixture.setAwayTeam(rs.getString("away_team"));
/*  360: 360 */         fixture.setAwayScore(rs.getString("away_score"));
/*  361: 361 */         fixture.setStatus(rs.getInt("status"));
/*  362: 362 */         fixture.setEventNotifSent(rs.getInt("event_notif_sent"));
/*  363:     */       }
/*  364: 365 */       SQL = "SELECT * FROM livescore_game_alias where game_id='" + gameId + "'";
/*  365: 366 */       prepstat = con.prepareStatement(SQL);
/*  366: 367 */       rs = prepstat.executeQuery();
/*  367: 368 */       while (rs.next()) {
/*  368: 369 */         fixture.setAlias(rs.getString("alias"));
/*  369:     */       }
/*  370:     */     }
/*  371:     */     catch (Exception ex)
/*  372:     */     {
/*  373: 372 */       if (con != null) {
/*  374: 373 */         con.close();
/*  375:     */       }
/*  376: 375 */       throw new Exception(ex.getMessage());
/*  377:     */     }
/*  378: 377 */     if (con != null) {
/*  379: 378 */       con.close();
/*  380:     */     }
/*  381: 381 */     return fixture;
/*  382:     */   }
/*  383:     */   
/*  384:     */   public static LiveScoreFixture viewFixtureByAlias(String alias)
/*  385:     */     throws Exception
/*  386:     */   {
/*  387: 386 */     LiveScoreFixture fixture = new LiveScoreFixture();
/*  388:     */     
/*  389:     */ 
/*  390: 389 */     ResultSet rs = null;
/*  391: 390 */     Connection con = null;
/*  392: 391 */     PreparedStatement prepstat = null;
/*  393:     */     try
/*  394:     */     {
/*  395: 393 */       con = DConnect.getConnection();
/*  396:     */       
/*  397: 395 */       String SQL = "SELECT * FROM livescore_fixtures lf inner join livescore_game_alias lg on lf.game_id=lg.game_id where lg.alias='" + alias + "'";
/*  398:     */       
/*  399: 397 */       prepstat = con.prepareStatement(SQL);
/*  400: 398 */       rs = prepstat.executeQuery();
/*  401: 400 */       while (rs.next())
/*  402:     */       {
/*  403: 401 */         fixture.setCountryName(rs.getString("lf.country_name"));
/*  404: 402 */         fixture.setDate(rs.getTimestamp("lf.date").toString());
/*  405: 403 */         fixture.setGameId(rs.getString("lf.game_id"));
/*  406: 404 */         fixture.setLeagueId(rs.getString("lf.league_id"));
/*  407: 405 */         fixture.setLeagueName(rs.getString("lf.league_name"));
/*  408: 406 */         fixture.setHomeTeam(rs.getString("lf.home_team"));
/*  409: 407 */         fixture.setHomeScore(rs.getString("lf.home_score"));
/*  410: 408 */         fixture.setAwayTeam(rs.getString("lf.away_team"));
/*  411: 409 */         fixture.setAwayScore(rs.getString("lf.away_score"));
/*  412: 410 */         fixture.setStatus(rs.getInt("lf.status"));
/*  413: 411 */         fixture.setEventNotifSent(rs.getInt("lf.event_notif_sent"));
/*  414: 412 */         fixture.setAlias(alias);
/*  415:     */       }
/*  416:     */     }
/*  417:     */     catch (Exception ex)
/*  418:     */     {
/*  419: 415 */       if (con != null) {
/*  420: 416 */         con.close();
/*  421:     */       }
/*  422: 418 */       throw new Exception(ex.getMessage());
/*  423:     */     }
/*  424: 420 */     if (con != null) {
/*  425: 421 */       con.close();
/*  426:     */     }
/*  427: 424 */     return fixture;
/*  428:     */   }
/*  429:     */   
/*  430:     */   public static LiveScoreFixture viewFixtureForCPByAlias(String alias, String accountId)
/*  431:     */     throws Exception
/*  432:     */   {
/*  433: 428 */     LiveScoreFixture fixture = new LiveScoreFixture();
/*  434:     */     
/*  435:     */ 
/*  436: 431 */     ResultSet rs = null;
/*  437: 432 */     Connection con = null;
/*  438: 433 */     PreparedStatement prepstat = null;
/*  439:     */     try
/*  440:     */     {
/*  441: 435 */       con = DConnect.getConnection();
/*  442:     */       
/*  443: 437 */       String SQL = "SELECT * FROM livescore_fixture_mgt lfm inner join livescore_fixtures lf on lf.game_id=lfm.game_id inner join livescore_game_alias lg on lf.game_id=lg.game_id where lg.alias='" + alias + "'" + " and lfm.account_id='" + accountId + "'";
/*  444:     */       
/*  445:     */ 
/*  446:     */ 
/*  447: 441 */       prepstat = con.prepareStatement(SQL);
/*  448: 442 */       rs = prepstat.executeQuery();
/*  449: 444 */       while (rs.next())
/*  450:     */       {
/*  451: 445 */         fixture.setCountryName(rs.getString("lf.country_name"));
/*  452: 446 */         fixture.setDate(rs.getTimestamp("lf.date").toString());
/*  453: 447 */         fixture.setGameId(rs.getString("lf.game_id"));
/*  454: 448 */         fixture.setLeagueId(rs.getString("lf.league_id"));
/*  455: 449 */         fixture.setLeagueName(rs.getString("lf.league_name"));
/*  456: 450 */         fixture.setHomeTeam(rs.getString("lf.home_team"));
/*  457: 451 */         fixture.setHomeScore(rs.getString("lf.home_score"));
/*  458: 452 */         fixture.setAwayTeam(rs.getString("lf.away_team"));
/*  459: 453 */         fixture.setAwayScore(rs.getString("lf.away_score"));
/*  460: 454 */         fixture.setStatus(rs.getInt("lf.status"));
/*  461: 455 */         fixture.setEventNotifSent(rs.getInt("lf.event_notif_sent"));
/*  462: 456 */         fixture.setAlias(alias);
/*  463:     */       }
/*  464:     */     }
/*  465:     */     catch (Exception ex)
/*  466:     */     {
/*  467: 459 */       if (con != null) {
/*  468: 460 */         con.close();
/*  469:     */       }
/*  470: 462 */       throw new Exception(ex.getMessage());
/*  471:     */     }
/*  472: 464 */     if (con != null) {
/*  473: 465 */       con.close();
/*  474:     */     }
/*  475: 468 */     return fixture;
/*  476:     */   }
/*  477:     */   
/*  478:     */   public static HashMap viewAllFixturesForDate(String date, int status)
/*  479:     */     throws Exception
/*  480:     */   {
/*  481: 472 */     HashMap groupedFixtures = new HashMap();
/*  482:     */     
/*  483:     */ 
/*  484:     */ 
/*  485: 476 */     ResultSet leagues = null;
/*  486: 477 */     ResultSet fixtures = null;
/*  487: 478 */     ResultSet rs = null;
/*  488: 479 */     Connection con = null;
/*  489: 480 */     PreparedStatement prepstat = null;
/*  490:     */     
/*  491:     */ 
/*  492: 483 */     System.out.println(new java.util.Date() + ":@com.rancard.mobility.infoserver.livescore.LiveScoreFixtureDB: viewAllFixturesForDate(date, status)");
/*  493:     */     try
/*  494:     */     {
/*  495: 486 */       con = DConnect.getConnection();
/*  496:     */       
/*  497:     */ 
/*  498: 489 */       String query = "select league_id from livescore_services";
/*  499: 490 */       prepstat = con.prepareStatement(query);
/*  500: 491 */       leagues = prepstat.executeQuery();
/*  501: 493 */       while (leagues.next())
/*  502:     */       {
/*  503: 494 */         String id = new String();
/*  504: 495 */         id = leagues.getString("league_id");
/*  505:     */         
/*  506: 497 */         String searchDateString = date.substring(0, date.indexOf(":"));
/*  507:     */         
/*  508: 499 */         query = "select * from livescore_fixtures where league_id='" + id + "' and livescore_fixtures.status=" + status + " and  date like '" + searchDateString + "%'";
/*  509:     */         
/*  510:     */ 
/*  511:     */ 
/*  512:     */ 
/*  513: 504 */         prepstat = con.prepareStatement(query);
/*  514: 505 */         fixtures = prepstat.executeQuery();
/*  515:     */         
/*  516: 507 */         ArrayList games = new ArrayList();
/*  517: 509 */         while (fixtures.next())
/*  518:     */         {
/*  519: 510 */           LiveScoreFixture fixture = new LiveScoreFixture();
/*  520:     */           
/*  521: 512 */           fixture.setCountryName(fixtures.getString("country_name"));
/*  522: 513 */           fixture.setDate(fixtures.getTimestamp("date").toString());
/*  523: 514 */           fixture.setGameId(fixtures.getString("game_id"));
/*  524: 515 */           fixture.setLeagueId(fixtures.getString("league_id"));
/*  525: 516 */           fixture.setLeagueName(fixtures.getString("league_name"));
/*  526: 517 */           fixture.setHomeTeam(fixtures.getString("home_team"));
/*  527: 518 */           fixture.setHomeScore(fixtures.getString("home_score"));
/*  528: 519 */           fixture.setAwayTeam(fixtures.getString("away_team"));
/*  529: 520 */           fixture.setAwayScore(fixtures.getString("away_score"));
/*  530: 521 */           fixture.setStatus(fixtures.getInt("status"));
/*  531: 522 */           fixture.setEventNotifSent(fixtures.getInt("event_notif_sent"));
/*  532:     */           
/*  533: 524 */           String SQL = "SELECT * FROM livescore_game_alias where game_id='" + fixture.getGameId() + "'";
/*  534: 525 */           prepstat = con.prepareStatement(SQL);
/*  535: 526 */           rs = prepstat.executeQuery();
/*  536: 527 */           while (rs.next()) {
/*  537: 528 */             fixture.setAlias(rs.getString("alias"));
/*  538:     */           }
/*  539: 531 */           games.add(fixture);
/*  540:     */         }
/*  541: 535 */         groupedFixtures.put(id, games);
/*  542:     */       }
/*  543:     */     }
/*  544:     */     catch (Exception ex)
/*  545:     */     {
/*  546: 538 */       if (con != null) {
/*  547: 539 */         con.close();
/*  548:     */       }
/*  549: 541 */       throw new Exception(ex.getMessage());
/*  550:     */     }
/*  551: 543 */     if (con != null) {
/*  552: 544 */       con.close();
/*  553:     */     }
/*  554: 546 */     return groupedFixtures;
/*  555:     */   }
/*  556:     */   
/*  557:     */   public static HashMap viewAllFixturesForDateGroupByCP(String date, int status, int notifStatus)
/*  558:     */     throws Exception
/*  559:     */   {
/*  560: 551 */     System.out.println(new java.util.Date() + ":@com.rancard.mobility.infoserver.livescore.LiveScoreFixtureDB:viewAllFixturesForDateGroupByCP():");
/*  561:     */     
/*  562: 553 */     HashMap groupedCPFixt = new HashMap();
/*  563:     */     
/*  564:     */ 
/*  565:     */ 
/*  566: 557 */     ResultSet rs = null;
/*  567: 558 */     ResultSet cpRS = null;
/*  568: 559 */     Connection con = null;
/*  569: 560 */     PreparedStatement prepstat = null;
/*  570:     */     
/*  571: 562 */     String searchDateString = date.substring(0, date.indexOf(":"));
/*  572:     */     try
/*  573:     */     {
/*  574: 565 */       con = DConnect.getConnection();
/*  575:     */       
/*  576:     */ 
/*  577:     */ 
/*  578: 569 */       String query = "select count(game_id), account_id from livescore_fixture_mgt where game_id in(select game_id from livescore_fixtures where date like '" + searchDateString + "%') group by account_id";
/*  579:     */       
/*  580:     */ 
/*  581:     */ 
/*  582: 573 */       prepstat = con.prepareStatement(query);
/*  583: 574 */       cpRS = prepstat.executeQuery();
/*  584:     */       
/*  585:     */ 
/*  586: 577 */       String accountId = "";
/*  587: 578 */       while (cpRS.next())
/*  588:     */       {
/*  589: 580 */         accountId = cpRS.getString("account_id");
/*  590:     */         
/*  591: 582 */         System.out.println(new java.util.Date() + ":retrieving fixtures for CP: " + accountId);
/*  592:     */         
/*  593: 584 */         query = "select * from livescore_fixture_mgt where account_id='" + accountId + "' " + "and game_id in(select game_id from livescore_fixtures where status='" + status + "' " + "and  date like '" + searchDateString + "%' and event_notif_sent='" + notifStatus + "')";
/*  594:     */         
/*  595:     */ 
/*  596:     */ 
/*  597: 588 */         prepstat = con.prepareStatement(query);
/*  598: 589 */         rs = prepstat.executeQuery();
/*  599:     */         
/*  600: 591 */         ArrayList<String> games = new ArrayList();
/*  601: 593 */         while (rs.next())
/*  602:     */         {
/*  603: 595 */           String gameId = rs.getString("game_id");
/*  604:     */           
/*  605: 597 */           games.add(gameId);
/*  606:     */         }
/*  607: 600 */         System.out.println(new java.util.Date() + ":No. Fixtures found for CP (" + accountId + "): " + games.size());
/*  608:     */         
/*  609:     */ 
/*  610: 603 */         groupedCPFixt.put(accountId, games);
/*  611:     */         
/*  612:     */ 
/*  613: 606 */         games = null;
/*  614:     */       }
/*  615: 610 */       System.out.println(new java.util.Date() + ":Total No. CPs: " + groupedCPFixt.size());
/*  616:     */     }
/*  617:     */     catch (Exception ex)
/*  618:     */     {
/*  619: 614 */       System.out.println(new java.util.Date() + ":error getting CPFixtureMatrix:");
/*  620: 615 */       ex.printStackTrace();
/*  621: 617 */       if (con != null) {
/*  622: 618 */         con.close();
/*  623:     */       }
/*  624: 620 */       throw new Exception(ex.getMessage());
/*  625:     */     }
/*  626: 622 */     if (con != null) {
/*  627: 623 */       con.close();
/*  628:     */     }
/*  629: 625 */     return groupedCPFixt;
/*  630:     */   }
/*  631:     */   
/*  632:     */   public static HashMap viewAllActiveFixturesForDate(String date)
/*  633:     */     throws Exception
/*  634:     */   {
/*  635: 629 */     HashMap groupedFixtures = new HashMap();
/*  636:     */     
/*  637:     */ 
/*  638:     */ 
/*  639: 633 */     ResultSet leagues = null;
/*  640: 634 */     ResultSet fixtures = null;
/*  641: 635 */     ResultSet rs = null;
/*  642: 636 */     Connection con = null;
/*  643: 637 */     PreparedStatement prepstat = null;
/*  644:     */     try
/*  645:     */     {
/*  646: 640 */       con = DConnect.getConnection();
/*  647:     */       
/*  648: 642 */       SimpleDateFormat s_formatter = new SimpleDateFormat();
/*  649: 643 */       java.util.Date validDate = null;
/*  650:     */       try
/*  651:     */       {
/*  652: 645 */         validDate = s_formatter.parse(date);
/*  653:     */       }
/*  654:     */       catch (Exception ex)
/*  655:     */       {
/*  656: 647 */         validDate = Calendar.getInstance().getTime();
/*  657:     */       }
/*  658: 650 */       Calendar c = Calendar.getInstance();
/*  659: 651 */       c.setTime(validDate);
/*  660: 652 */       c.add(11, 24);
/*  661: 653 */       java.util.Date endDate = c.getTime();
/*  662:     */       
/*  663: 655 */       String query = "select league_id from livescore_services";
/*  664: 656 */       prepstat = con.prepareStatement(query);
/*  665: 657 */       leagues = prepstat.executeQuery();
/*  666: 659 */       while (leagues.next())
/*  667:     */       {
/*  668: 660 */         String id = new String();
/*  669: 661 */         id = leagues.getString("league_id");
/*  670:     */         
/*  671: 663 */         query = "select * from livescore_fixtures lf where lf.league_id='" + id + "' and lf.status='" + 0 + "' and  (lf.date like '" + date + "%' or lf.date >='" + new Timestamp(validDate.getTime()).toString() + "' and lf.date <='" + new Timestamp(endDate.getTime()).toString() + "')";
/*  672:     */         
/*  673: 665 */         prepstat = con.prepareStatement(query);
/*  674:     */         
/*  675:     */ 
/*  676:     */ 
/*  677:     */ 
/*  678:     */ 
/*  679: 671 */         fixtures = prepstat.executeQuery();
/*  680:     */         
/*  681: 673 */         ArrayList games = new ArrayList();
/*  682: 675 */         while (fixtures.next())
/*  683:     */         {
/*  684: 676 */           LiveScoreFixture fixture = new LiveScoreFixture();
/*  685:     */           
/*  686: 678 */           fixture.setCountryName(fixtures.getString("country_name"));
/*  687: 679 */           fixture.setDate(fixtures.getTimestamp("date").toString());
/*  688: 680 */           fixture.setGameId(fixtures.getString("game_id"));
/*  689: 681 */           fixture.setLeagueId(fixtures.getString("league_id"));
/*  690: 682 */           fixture.setLeagueName(fixtures.getString("league_name"));
/*  691: 683 */           fixture.setHomeTeam(fixtures.getString("home_team"));
/*  692: 684 */           fixture.setHomeScore(fixtures.getString("home_score"));
/*  693: 685 */           fixture.setAwayTeam(fixtures.getString("away_team"));
/*  694: 686 */           fixture.setAwayScore(fixtures.getString("away_score"));
/*  695: 687 */           fixture.setStatus(fixtures.getInt("status"));
/*  696: 688 */           fixture.setEventNotifSent(fixtures.getInt("event_notif_sent"));
/*  697:     */           
/*  698: 690 */           String SQL = "SELECT * FROM livescore_game_alias where game_id='" + fixture.getGameId() + "'";
/*  699: 691 */           prepstat = con.prepareStatement(SQL);
/*  700: 692 */           rs = prepstat.executeQuery();
/*  701: 693 */           while (rs.next()) {
/*  702: 694 */             fixture.setAlias(rs.getString("alias"));
/*  703:     */           }
/*  704: 697 */           games.add(fixture);
/*  705:     */         }
/*  706: 701 */         groupedFixtures.put(id, games);
/*  707:     */       }
/*  708:     */     }
/*  709:     */     catch (Exception ex)
/*  710:     */     {
/*  711: 704 */       if (con != null) {
/*  712: 705 */         con.close();
/*  713:     */       }
/*  714: 707 */       throw new Exception(ex.getMessage());
/*  715:     */     }
/*  716: 709 */     if (con != null) {
/*  717: 710 */       con.close();
/*  718:     */     }
/*  719: 712 */     return groupedFixtures;
/*  720:     */   }
/*  721:     */   
/*  722:     */   public static HashMap viewAllActiveFixturesForDate(String accountId, String date)
/*  723:     */     throws Exception
/*  724:     */   {
/*  725: 716 */     HashMap groupedFixtures = new HashMap();
/*  726:     */     
/*  727:     */ 
/*  728:     */ 
/*  729: 720 */     ResultSet leagues = null;
/*  730: 721 */     ResultSet fixtures = null;
/*  731: 722 */     ResultSet rs = null;
/*  732: 723 */     Connection con = null;
/*  733: 724 */     PreparedStatement prepstat = null;
/*  734:     */     try
/*  735:     */     {
/*  736: 727 */       con = DConnect.getConnection();
/*  737:     */       
/*  738: 729 */       SimpleDateFormat s_formatter = new SimpleDateFormat();
/*  739: 730 */       java.util.Date validDate = null;
/*  740:     */       try
/*  741:     */       {
/*  742: 732 */         validDate = s_formatter.parse(date);
/*  743:     */       }
/*  744:     */       catch (Exception ex)
/*  745:     */       {
/*  746: 734 */         validDate = Calendar.getInstance().getTime();
/*  747:     */       }
/*  748: 737 */       Calendar c = Calendar.getInstance();
/*  749: 738 */       c.setTime(validDate);
/*  750: 739 */       c.add(11, 24);
/*  751: 740 */       java.util.Date endDate = c.getTime();
/*  752:     */       
/*  753: 742 */       String query = "select ls.league_id from livescore_services ls inner join keyword_mapping km on km.mapping=ls.league_id where km.account_id='" + accountId + "'";
/*  754: 743 */       prepstat = con.prepareStatement(query);
/*  755: 744 */       leagues = prepstat.executeQuery();
/*  756: 746 */       while (leagues.next())
/*  757:     */       {
/*  758: 747 */         String id = new String();
/*  759: 748 */         id = leagues.getString("league_id");
/*  760:     */         
/*  761: 750 */         query = "select * from livescore_fixtures lf where lf.league_id='" + id + "' and date(lf.date)=?";
/*  762: 751 */         prepstat = con.prepareStatement(query);
/*  763: 752 */         prepstat.setDate(1, new java.sql.Date(validDate.getTime()));
/*  764: 753 */         fixtures = prepstat.executeQuery();
/*  765:     */         
/*  766: 755 */         ArrayList games = new ArrayList();
/*  767: 757 */         while (fixtures.next())
/*  768:     */         {
/*  769: 758 */           LiveScoreFixture fixture = new LiveScoreFixture();
/*  770:     */           
/*  771: 760 */           fixture.setCountryName(fixtures.getString("country_name"));
/*  772: 761 */           fixture.setDate(fixtures.getTimestamp("date").toString());
/*  773: 762 */           fixture.setGameId(fixtures.getString("game_id"));
/*  774: 763 */           fixture.setLeagueId(fixtures.getString("league_id"));
/*  775: 764 */           fixture.setLeagueName(fixtures.getString("league_name"));
/*  776: 765 */           fixture.setHomeTeam(fixtures.getString("home_team"));
/*  777: 766 */           fixture.setHomeScore(fixtures.getString("home_score"));
/*  778: 767 */           fixture.setAwayTeam(fixtures.getString("away_team"));
/*  779: 768 */           fixture.setAwayScore(fixtures.getString("away_score"));
/*  780: 769 */           fixture.setStatus(fixtures.getInt("status"));
/*  781: 770 */           fixture.setEventNotifSent(fixtures.getInt("event_notif_sent"));
/*  782:     */           
/*  783: 772 */           String SQL = "SELECT * FROM livescore_game_alias where game_id='" + fixture.getGameId() + "'";
/*  784: 773 */           prepstat = con.prepareStatement(SQL);
/*  785: 774 */           rs = prepstat.executeQuery();
/*  786: 775 */           while (rs.next()) {
/*  787: 776 */             fixture.setAlias(rs.getString("alias"));
/*  788:     */           }
/*  789: 779 */           games.add(fixture);
/*  790:     */         }
/*  791: 783 */         groupedFixtures.put(id, games);
/*  792:     */       }
/*  793:     */     }
/*  794:     */     catch (Exception ex)
/*  795:     */     {
/*  796: 786 */       if (con != null) {
/*  797: 787 */         con.close();
/*  798:     */       }
/*  799: 789 */       throw new Exception(ex.getMessage());
/*  800:     */     }
/*  801: 791 */     if (con != null) {
/*  802: 792 */       con.close();
/*  803:     */     }
/*  804: 794 */     return groupedFixtures;
/*  805:     */   }
/*  806:     */   
/*  807:     */   public static ArrayList viewAllActiveFixturesInLeague(String date, String keyword, String accountId)
/*  808:     */     throws Exception
/*  809:     */   {
/*  810: 798 */     ArrayList games = new ArrayList();
/*  811:     */     
/*  812:     */ 
/*  813:     */ 
/*  814: 802 */     ResultSet fixtures = null;
/*  815: 803 */     ResultSet rs = null;
/*  816: 804 */     Connection con = null;
/*  817: 805 */     PreparedStatement prepstat = null;
/*  818:     */     try
/*  819:     */     {
/*  820: 808 */       con = DConnect.getConnection();
/*  821:     */       
/*  822: 810 */       SimpleDateFormat s_formatter = new SimpleDateFormat();
/*  823: 811 */       java.util.Date validDate = null;
/*  824:     */       try
/*  825:     */       {
/*  826: 813 */         validDate = s_formatter.parse(date);
/*  827:     */       }
/*  828:     */       catch (Exception ex)
/*  829:     */       {
/*  830: 815 */         validDate = Calendar.getInstance().getTime();
/*  831:     */       }
/*  832: 818 */       Calendar c = Calendar.getInstance();
/*  833: 819 */       c.setTime(validDate);
/*  834: 820 */       c.add(11, 24);
/*  835: 821 */       java.util.Date endDate = c.getTime();
/*  836:     */       
/*  837:     */ 
/*  838: 824 */       String query = "select * from livescore_fixtures lf inner join keyword_mapping km on lf.league_id=km.mapping inner join service_definition sd on km.account_id=sd.account_id and km.keyword=sd.keyword where sd.keyword='" + keyword + "' and sd.account_id='" + accountId + "' and" + " lf.status in ('" + 2 + "', '" + 0 + "') and  (lf.date like '" + date + "%' or lf.date >='" + new Timestamp(validDate.getTime()).toString() + "' and lf.date <='" + new Timestamp(endDate.getTime()).toString() + "')";
/*  839:     */       
/*  840:     */ 
/*  841:     */ 
/*  842:     */ 
/*  843: 829 */       prepstat = con.prepareStatement(query);
/*  844:     */       
/*  845:     */ 
/*  846: 832 */       fixtures = prepstat.executeQuery();
/*  847: 834 */       while (fixtures.next())
/*  848:     */       {
/*  849: 835 */         LiveScoreFixture fixture = new LiveScoreFixture();
/*  850:     */         
/*  851: 837 */         fixture.setCountryName(fixtures.getString("lf.country_name"));
/*  852: 838 */         fixture.setDate(fixtures.getTimestamp("lf.date").toString());
/*  853: 839 */         fixture.setGameId(fixtures.getString("lf.game_id"));
/*  854: 840 */         fixture.setLeagueId(fixtures.getString("lf.league_id"));
/*  855: 841 */         fixture.setLeagueName(fixtures.getString("lf.league_name"));
/*  856: 842 */         fixture.setHomeTeam(fixtures.getString("lf.home_team"));
/*  857: 843 */         fixture.setHomeScore(fixtures.getString("lf.home_score"));
/*  858: 844 */         fixture.setAwayTeam(fixtures.getString("lf.away_team"));
/*  859: 845 */         fixture.setAwayScore(fixtures.getString("lf.away_score"));
/*  860: 846 */         fixture.setStatus(fixtures.getInt("lf.status"));
/*  861: 847 */         fixture.setEventNotifSent(fixtures.getInt("lf.event_notif_sent"));
/*  862:     */         
/*  863: 849 */         String SQL = "SELECT * FROM livescore_game_alias where game_id='" + fixture.getGameId() + "'";
/*  864: 850 */         prepstat = con.prepareStatement(SQL);
/*  865: 851 */         rs = prepstat.executeQuery();
/*  866: 852 */         while (rs.next()) {
/*  867: 853 */           fixture.setAlias(rs.getString("alias"));
/*  868:     */         }
/*  869: 856 */         games.add(fixture);
/*  870:     */       }
/*  871:     */     }
/*  872:     */     catch (Exception ex)
/*  873:     */     {
/*  874: 859 */       if (con != null) {
/*  875: 860 */         con.close();
/*  876:     */       }
/*  877: 862 */       throw new Exception(ex.getMessage());
/*  878:     */     }
/*  879: 864 */     if (con != null) {
/*  880: 865 */       con.close();
/*  881:     */     }
/*  882: 867 */     return games;
/*  883:     */   }
/*  884:     */   
/*  885:     */   public static ArrayList viewAllActiveFixturesForCPInLeague(String date, String keyword, String accountId)
/*  886:     */     throws Exception
/*  887:     */   {
/*  888: 870 */     ArrayList games = new ArrayList();
/*  889:     */     
/*  890:     */ 
/*  891:     */ 
/*  892: 874 */     ResultSet fixtures = null;
/*  893: 875 */     ResultSet rs = null;
/*  894: 876 */     Connection con = null;
/*  895: 877 */     PreparedStatement prepstat = null;
/*  896:     */     try
/*  897:     */     {
/*  898: 880 */       con = DConnect.getConnection();
/*  899:     */       
/*  900: 882 */       SimpleDateFormat s_formatter = new SimpleDateFormat();
/*  901: 883 */       java.util.Date validDate = null;
/*  902:     */       try
/*  903:     */       {
/*  904: 885 */         validDate = s_formatter.parse(date);
/*  905:     */       }
/*  906:     */       catch (Exception ex)
/*  907:     */       {
/*  908: 887 */         validDate = Calendar.getInstance().getTime();
/*  909:     */       }
/*  910: 890 */       Calendar c = Calendar.getInstance();
/*  911: 891 */       c.setTime(validDate);
/*  912: 892 */       c.add(11, 24);
/*  913: 893 */       java.util.Date endDate = c.getTime();
/*  914:     */       
/*  915:     */ 
/*  916: 896 */       String query = "select * from livescore_fixture_mgt lfm inner join livescore_fixtures lf on lf.game_id=lfm.game_id inner join keyword_mapping km on lf.league_id=km.mapping and lfm.account_id=km.account_id inner join service_definition sd on km.account_id=sd.account_id and km.keyword=sd.keyword where sd.keyword='" + keyword + "' and sd.account_id='" + accountId + "' and" + " lf.status in ('" + 2 + "', '" + 0 + "') and  (lf.date like '" + date + "%' or lf.date >='" + new Timestamp(validDate.getTime()).toString() + "' and lf.date <='" + new Timestamp(endDate.getTime()).toString() + "')";
/*  917:     */       
/*  918:     */ 
/*  919:     */ 
/*  920:     */ 
/*  921:     */ 
/*  922: 902 */       prepstat = con.prepareStatement(query);
/*  923:     */       
/*  924:     */ 
/*  925: 905 */       fixtures = prepstat.executeQuery();
/*  926: 907 */       while (fixtures.next())
/*  927:     */       {
/*  928: 908 */         LiveScoreFixture fixture = new LiveScoreFixture();
/*  929:     */         
/*  930: 910 */         fixture.setCountryName(fixtures.getString("lf.country_name"));
/*  931: 911 */         fixture.setDate(fixtures.getTimestamp("lf.date").toString());
/*  932: 912 */         fixture.setGameId(fixtures.getString("lf.game_id"));
/*  933: 913 */         fixture.setLeagueId(fixtures.getString("lf.league_id"));
/*  934: 914 */         fixture.setLeagueName(fixtures.getString("lf.league_name"));
/*  935: 915 */         fixture.setHomeTeam(fixtures.getString("lf.home_team"));
/*  936: 916 */         fixture.setHomeScore(fixtures.getString("lf.home_score"));
/*  937: 917 */         fixture.setAwayTeam(fixtures.getString("lf.away_team"));
/*  938: 918 */         fixture.setAwayScore(fixtures.getString("lf.away_score"));
/*  939: 919 */         fixture.setStatus(fixtures.getInt("lf.status"));
/*  940: 920 */         fixture.setEventNotifSent(fixtures.getInt("lf.event_notif_sent"));
/*  941:     */         
/*  942: 922 */         String SQL = "SELECT * FROM livescore_game_alias where game_id='" + fixture.getGameId() + "'";
/*  943: 923 */         prepstat = con.prepareStatement(SQL);
/*  944: 924 */         rs = prepstat.executeQuery();
/*  945: 925 */         while (rs.next()) {
/*  946: 926 */           fixture.setAlias(rs.getString("alias"));
/*  947:     */         }
/*  948: 929 */         games.add(fixture);
/*  949:     */       }
/*  950:     */     }
/*  951:     */     catch (Exception ex)
/*  952:     */     {
/*  953: 932 */       if (con != null) {
/*  954: 933 */         con.close();
/*  955:     */       }
/*  956: 935 */       throw new Exception(ex.getMessage());
/*  957:     */     }
/*  958: 937 */     if (con != null) {
/*  959: 938 */       con.close();
/*  960:     */     }
/*  961: 940 */     return games;
/*  962:     */   }
/*  963:     */   
/*  964:     */   public static ArrayList viewAllActiveFixturesInAllLeagues(String date)
/*  965:     */     throws Exception
/*  966:     */   {
/*  967: 944 */     ArrayList games = new ArrayList();
/*  968:     */     
/*  969:     */ 
/*  970:     */ 
/*  971: 948 */     ResultSet fixtures = null;
/*  972: 949 */     ResultSet rs = null;
/*  973: 950 */     Connection con = null;
/*  974: 951 */     PreparedStatement prepstat = null;
/*  975:     */     try
/*  976:     */     {
/*  977: 954 */       con = DConnect.getConnection();
/*  978:     */       
/*  979: 956 */       SimpleDateFormat s_formatter = new SimpleDateFormat();
/*  980: 957 */       java.util.Date validDate = null;
/*  981:     */       try
/*  982:     */       {
/*  983: 959 */         validDate = s_formatter.parse(date);
/*  984:     */       }
/*  985:     */       catch (Exception ex)
/*  986:     */       {
/*  987: 961 */         validDate = Calendar.getInstance().getTime();
/*  988:     */       }
/*  989: 964 */       Calendar c = Calendar.getInstance();
/*  990: 965 */       c.setTime(validDate);
/*  991: 966 */       c.add(11, 24);
/*  992: 967 */       java.util.Date endDate = c.getTime();
/*  993:     */       
/*  994:     */ 
/*  995: 970 */       String query = "select * from livescore_fixtures lf where lf.status='0' and  (lf.date like '" + date + "%' or lf.date >='" + new Timestamp(validDate.getTime()).toString() + "' and lf.date <='" + new Timestamp(endDate.getTime()).toString() + "')";
/*  996:     */       
/*  997: 972 */       prepstat = con.prepareStatement(query);
/*  998:     */       
/*  999:     */ 
/* 1000: 975 */       fixtures = prepstat.executeQuery();
/* 1001: 977 */       while (fixtures.next())
/* 1002:     */       {
/* 1003: 978 */         LiveScoreFixture fixture = new LiveScoreFixture();
/* 1004:     */         
/* 1005: 980 */         fixture.setCountryName(fixtures.getString("lf.country_name"));
/* 1006: 981 */         fixture.setDate(fixtures.getTimestamp("lf.date").toString());
/* 1007: 982 */         fixture.setGameId(fixtures.getString("lf.game_id"));
/* 1008: 983 */         fixture.setLeagueId(fixtures.getString("lf.league_id"));
/* 1009: 984 */         fixture.setLeagueName(fixtures.getString("lf.league_name"));
/* 1010: 985 */         fixture.setHomeTeam(fixtures.getString("lf.home_team"));
/* 1011: 986 */         fixture.setHomeScore(fixtures.getString("lf.home_score"));
/* 1012: 987 */         fixture.setAwayTeam(fixtures.getString("lf.away_team"));
/* 1013: 988 */         fixture.setAwayScore(fixtures.getString("lf.away_score"));
/* 1014: 989 */         fixture.setStatus(fixtures.getInt("lf.status"));
/* 1015: 990 */         fixture.setEventNotifSent(fixtures.getInt("lf.event_notif_sent"));
/* 1016:     */         
/* 1017: 992 */         String SQL = "SELECT * FROM livescore_game_alias where game_id='" + fixture.getGameId() + "'";
/* 1018: 993 */         prepstat = con.prepareStatement(SQL);
/* 1019: 994 */         rs = prepstat.executeQuery();
/* 1020: 995 */         while (rs.next()) {
/* 1021: 996 */           fixture.setAlias(rs.getString("alias"));
/* 1022:     */         }
/* 1023: 999 */         games.add(fixture);
/* 1024:     */       }
/* 1025:     */     }
/* 1026:     */     catch (Exception ex)
/* 1027:     */     {
/* 1028:1002 */       if (con != null) {
/* 1029:1003 */         con.close();
/* 1030:     */       }
/* 1031:1005 */       throw new Exception(ex.getMessage());
/* 1032:     */     }
/* 1033:1007 */     if (con != null) {
/* 1034:1008 */       con.close();
/* 1035:     */     }
/* 1036:1010 */     return games;
/* 1037:     */   }
/* 1038:     */   
/* 1039:     */   public static ArrayList viewFixtures(String keyword, String accountId, String date)
/* 1040:     */     throws Exception
/* 1041:     */   {
/* 1042:1015 */     ArrayList games = new ArrayList();
/* 1043:     */     
/* 1044:     */ 
/* 1045:     */ 
/* 1046:1019 */     ResultSet fixtures = null;
/* 1047:1020 */     ResultSet rs = null;
/* 1048:1021 */     Connection con = null;
/* 1049:1022 */     PreparedStatement prepstat = null;
/* 1050:     */     try
/* 1051:     */     {
/* 1052:1025 */       con = DConnect.getConnection();
/* 1053:     */       
/* 1054:1027 */       SimpleDateFormat s_formatter = new SimpleDateFormat("MM-dd-yyyy");
/* 1055:1028 */       java.util.Date validDate = null;
/* 1056:     */       try
/* 1057:     */       {
/* 1058:1030 */         validDate = s_formatter.parse(date);
/* 1059:     */       }
/* 1060:     */       catch (Exception ex)
/* 1061:     */       {
/* 1062:1032 */         validDate = Calendar.getInstance().getTime();
/* 1063:     */       }
/* 1064:1044 */       String query = "select * from livescore_fixtures lf inner join livescore_services ls on lf.league_id=ls.league_id inner join keyword_mapping km on km.mapping=ls.league_id where km.account_id=? and date(lf.date)= ?";
/* 1065:1047 */       if (!"".equals(keyword))
/* 1066:     */       {
/* 1067:1048 */         query = query + " and km.keyword = '" + keyword + "'";
/* 1068:1049 */         query = query + " order by lf.game_id";
/* 1069:     */       }
/* 1070:     */       else
/* 1071:     */       {
/* 1072:1051 */         query = query + " order by lf.league_id";
/* 1073:     */       }
/* 1074:1053 */       prepstat = con.prepareStatement(query);
/* 1075:1054 */       prepstat.setString(1, accountId);
/* 1076:1055 */       prepstat.setDate(2, new java.sql.Date(validDate.getTime()));
/* 1077:     */       
/* 1078:     */ 
/* 1079:1058 */       fixtures = prepstat.executeQuery();
/* 1080:1060 */       while (fixtures.next())
/* 1081:     */       {
/* 1082:1061 */         LiveScoreFixture fixture = new LiveScoreFixture();
/* 1083:     */         
/* 1084:1063 */         fixture.setCountryName(fixtures.getString("lf.country_name"));
/* 1085:1064 */         fixture.setDate(fixtures.getTimestamp("lf.date").toString());
/* 1086:1065 */         fixture.setGameId(fixtures.getString("lf.game_id"));
/* 1087:1066 */         fixture.setLeagueId(fixtures.getString("lf.league_id"));
/* 1088:1067 */         fixture.setLeagueName(fixtures.getString("lf.league_name"));
/* 1089:1068 */         fixture.setHomeTeam(fixtures.getString("lf.home_team"));
/* 1090:1069 */         fixture.setHomeScore(fixtures.getString("lf.home_score"));
/* 1091:1070 */         fixture.setAwayTeam(fixtures.getString("lf.away_team"));
/* 1092:1071 */         fixture.setAwayScore(fixtures.getString("lf.away_score"));
/* 1093:1072 */         fixture.setStatus(fixtures.getInt("lf.status"));
/* 1094:1073 */         fixture.setEventNotifSent(fixtures.getInt("lf.event_notif_sent"));
/* 1095:     */         
/* 1096:1075 */         String SQL = "SELECT * FROM livescore_game_alias where game_id='" + fixture.getGameId() + "'";
/* 1097:1076 */         prepstat = con.prepareStatement(SQL);
/* 1098:1077 */         rs = prepstat.executeQuery();
/* 1099:1078 */         while (rs.next()) {
/* 1100:1079 */           fixture.setAlias(rs.getString("alias"));
/* 1101:     */         }
/* 1102:1082 */         games.add(fixture);
/* 1103:     */       }
/* 1104:     */     }
/* 1105:     */     catch (Exception ex)
/* 1106:     */     {
/* 1107:1085 */       if (con != null) {
/* 1108:1086 */         con.close();
/* 1109:     */       }
/* 1110:1088 */       throw new Exception(ex.getMessage());
/* 1111:     */     }
/* 1112:1090 */     if (con != null) {
/* 1113:1091 */       con.close();
/* 1114:     */     }
/* 1115:1093 */     return games;
/* 1116:     */   }
/* 1117:     */   
/* 1118:     */   public static ArrayList viewAllActiveFixturesInLeague(String date, String keyword, String accountId, String team)
/* 1119:     */     throws Exception
/* 1120:     */   {
/* 1121:1221 */     ArrayList games = new ArrayList();
/* 1122:     */     
/* 1123:     */ 
/* 1124:     */ 
/* 1125:1225 */     ResultSet fixtures = null;
/* 1126:1226 */     ResultSet rs = null;
/* 1127:1227 */     Connection con = null;
/* 1128:1228 */     PreparedStatement prepstat = null;
/* 1129:     */     try
/* 1130:     */     {
/* 1131:1231 */       con = DConnect.getConnection();
/* 1132:     */       
/* 1133:1233 */       SimpleDateFormat s_formatter = new SimpleDateFormat();
/* 1134:1234 */       java.util.Date validDate = null;
/* 1135:     */       try
/* 1136:     */       {
/* 1137:1236 */         validDate = s_formatter.parse(date);
/* 1138:     */       }
/* 1139:     */       catch (Exception ex)
/* 1140:     */       {
/* 1141:1238 */         validDate = Calendar.getInstance().getTime();
/* 1142:     */       }
/* 1143:1241 */       Calendar c = Calendar.getInstance();
/* 1144:1242 */       c.setTime(validDate);
/* 1145:1243 */       c.add(11, 24);
/* 1146:1244 */       java.util.Date endDate = c.getTime();
/* 1147:     */       
/* 1148:     */ 
/* 1149:1247 */       String query = "select * from livescore_fixtures lf inner join keyword_mapping km on lf.league_id=km.mapping inner join service_definition sd on km.account_id=sd.account_id and km.keyword=sd.keyword where sd.keyword='" + keyword + "' and sd.account_id='" + accountId + "' and lf.status in ('" + 2 + "', '" + 0 + "') and (lf.date like '" + date + "%' or lf.date >='" + new Timestamp(validDate.getTime()).toString() + "' and lf.date <='" + new Timestamp(endDate.getTime()).toString() + "') and (lf.home_team like '%" + team + "%' or away_team like '%" + team + "%')";
/* 1150:     */       
/* 1151:     */ 
/* 1152:     */ 
/* 1153:     */ 
/* 1154:1252 */       prepstat = con.prepareStatement(query);
/* 1155:1253 */       fixtures = prepstat.executeQuery();
/* 1156:1255 */       while (fixtures.next())
/* 1157:     */       {
/* 1158:1256 */         LiveScoreFixture fixture = new LiveScoreFixture();
/* 1159:     */         
/* 1160:1258 */         fixture.setCountryName(fixtures.getString("lf.country_name"));
/* 1161:1259 */         fixture.setDate(fixtures.getTimestamp("lf.date").toString());
/* 1162:1260 */         fixture.setGameId(fixtures.getString("lf.game_id"));
/* 1163:1261 */         fixture.setLeagueId(fixtures.getString("lf.league_id"));
/* 1164:1262 */         fixture.setLeagueName(fixtures.getString("lf.league_name"));
/* 1165:1263 */         fixture.setHomeTeam(fixtures.getString("lf.home_team"));
/* 1166:1264 */         fixture.setHomeScore(fixtures.getString("lf.home_score"));
/* 1167:1265 */         fixture.setAwayTeam(fixtures.getString("lf.away_team"));
/* 1168:1266 */         fixture.setAwayScore(fixtures.getString("lf.away_score"));
/* 1169:1267 */         fixture.setStatus(fixtures.getInt("lf.status"));
/* 1170:1268 */         fixture.setEventNotifSent(fixtures.getInt("lf.event_notif_sent"));
/* 1171:     */         
/* 1172:1270 */         String SQL = "SELECT * FROM livescore_game_alias where game_id='" + fixture.getGameId() + "'";
/* 1173:1271 */         prepstat = con.prepareStatement(SQL);
/* 1174:1272 */         rs = prepstat.executeQuery();
/* 1175:1273 */         while (rs.next()) {
/* 1176:1274 */           fixture.setAlias(rs.getString("alias"));
/* 1177:     */         }
/* 1178:1277 */         games.add(fixture);
/* 1179:     */       }
/* 1180:     */     }
/* 1181:     */     catch (Exception ex)
/* 1182:     */     {
/* 1183:1280 */       if (con != null) {
/* 1184:1281 */         con.close();
/* 1185:     */       }
/* 1186:1283 */       throw new Exception(ex.getMessage());
/* 1187:     */     }
/* 1188:1285 */     if (con != null) {
/* 1189:1286 */       con.close();
/* 1190:     */     }
/* 1191:1288 */     return games;
/* 1192:     */   }
/* 1193:     */   
/* 1194:     */   public static ArrayList viewAllActiveFixturesForCPInLeague(String date, String keyword, String accountId, String team)
/* 1195:     */     throws Exception
/* 1196:     */   {
/* 1197:1291 */     ArrayList games = new ArrayList();
/* 1198:     */     
/* 1199:     */ 
/* 1200:     */ 
/* 1201:1295 */     ResultSet fixtures = null;
/* 1202:1296 */     ResultSet rs = null;
/* 1203:1297 */     Connection con = null;
/* 1204:1298 */     PreparedStatement prepstat = null;
/* 1205:     */     try
/* 1206:     */     {
/* 1207:1301 */       con = DConnect.getConnection();
/* 1208:     */       
/* 1209:1303 */       SimpleDateFormat s_formatter = new SimpleDateFormat();
/* 1210:1304 */       java.util.Date validDate = null;
/* 1211:     */       try
/* 1212:     */       {
/* 1213:1306 */         validDate = s_formatter.parse(date);
/* 1214:     */       }
/* 1215:     */       catch (Exception ex)
/* 1216:     */       {
/* 1217:1308 */         validDate = Calendar.getInstance().getTime();
/* 1218:     */       }
/* 1219:1311 */       Calendar c = Calendar.getInstance();
/* 1220:1312 */       c.setTime(validDate);
/* 1221:1313 */       c.add(11, 24);
/* 1222:1314 */       java.util.Date endDate = c.getTime();
/* 1223:     */       
/* 1224:     */ 
/* 1225:1317 */       String query = "select * from livescore_fixture_mgt lfm inner join livescore_fixtures lf on lf.game_id=lfm.game_id inner join keyword_mapping km on lf.league_id=km.mapping and lfm.account_id=km.account_id inner join service_definition sd on km.account_id=sd.account_id and km.keyword=sd.keyword where sd.keyword='" + keyword + "' and sd.account_id='" + accountId + "' and lf.status in ('" + 2 + "', '" + 0 + "') and (lf.date like '" + date + "%' or lf.date >='" + new Timestamp(validDate.getTime()).toString() + "' and lf.date <='" + new Timestamp(endDate.getTime()).toString() + "') and (lf.home_team like '%" + team + "%' or away_team like '%" + team + "%')";
/* 1226:     */       
/* 1227:     */ 
/* 1228:     */ 
/* 1229:     */ 
/* 1230:     */ 
/* 1231:1323 */       prepstat = con.prepareStatement(query);
/* 1232:1324 */       fixtures = prepstat.executeQuery();
/* 1233:1326 */       while (fixtures.next())
/* 1234:     */       {
/* 1235:1327 */         LiveScoreFixture fixture = new LiveScoreFixture();
/* 1236:     */         
/* 1237:1329 */         fixture.setCountryName(fixtures.getString("lf.country_name"));
/* 1238:1330 */         fixture.setDate(fixtures.getTimestamp("lf.date").toString());
/* 1239:1331 */         fixture.setGameId(fixtures.getString("lf.game_id"));
/* 1240:1332 */         fixture.setLeagueId(fixtures.getString("lf.league_id"));
/* 1241:1333 */         fixture.setLeagueName(fixtures.getString("lf.league_name"));
/* 1242:1334 */         fixture.setHomeTeam(fixtures.getString("lf.home_team"));
/* 1243:1335 */         fixture.setHomeScore(fixtures.getString("lf.home_score"));
/* 1244:1336 */         fixture.setAwayTeam(fixtures.getString("lf.away_team"));
/* 1245:1337 */         fixture.setAwayScore(fixtures.getString("lf.away_score"));
/* 1246:1338 */         fixture.setStatus(fixtures.getInt("lf.status"));
/* 1247:1339 */         fixture.setEventNotifSent(fixtures.getInt("lf.event_notif_sent"));
/* 1248:     */         
/* 1249:1341 */         String SQL = "SELECT * FROM livescore_game_alias where game_id='" + fixture.getGameId() + "'";
/* 1250:1342 */         prepstat = con.prepareStatement(SQL);
/* 1251:1343 */         rs = prepstat.executeQuery();
/* 1252:1344 */         while (rs.next()) {
/* 1253:1345 */           fixture.setAlias(rs.getString("alias"));
/* 1254:     */         }
/* 1255:1348 */         games.add(fixture);
/* 1256:     */       }
/* 1257:     */     }
/* 1258:     */     catch (Exception ex)
/* 1259:     */     {
/* 1260:1351 */       if (con != null) {
/* 1261:1352 */         con.close();
/* 1262:     */       }
/* 1263:1354 */       throw new Exception(ex.getMessage());
/* 1264:     */     }
/* 1265:1356 */     if (con != null) {
/* 1266:1357 */       con.close();
/* 1267:     */     }
/* 1268:1359 */     return games;
/* 1269:     */   }
/* 1270:     */   
/* 1271:     */   public static ArrayList viewAllFixtures()
/* 1272:     */     throws Exception
/* 1273:     */   {
/* 1274:1364 */     ArrayList fixtures = new ArrayList();
/* 1275:     */     
/* 1276:     */ 
/* 1277:1367 */     ResultSet rs = null;
/* 1278:1368 */     ResultSet rs2 = null;
/* 1279:1369 */     Connection con = null;
/* 1280:1370 */     PreparedStatement prepstat2 = null;
/* 1281:     */     try
/* 1282:     */     {
/* 1283:1373 */       con = DConnect.getConnection();
/* 1284:     */       
/* 1285:1375 */       String SQL = "SELECT * FROM livescore_fixtures";
/* 1286:     */       
/* 1287:1377 */       PreparedStatement prepstat = con.prepareStatement(SQL);
/* 1288:1378 */       rs = prepstat.executeQuery();
/* 1289:1380 */       while (rs.next())
/* 1290:     */       {
/* 1291:1381 */         LiveScoreFixture fixture = new LiveScoreFixture();
/* 1292:     */         
/* 1293:1383 */         fixture.setCountryName(rs.getString("country_name"));
/* 1294:1384 */         fixture.setDate(rs.getTimestamp("date").toString());
/* 1295:1385 */         fixture.setGameId(rs.getString("game_id"));
/* 1296:1386 */         fixture.setLeagueId(rs.getString("league_id"));
/* 1297:1387 */         fixture.setLeagueName(rs.getString("league_name"));
/* 1298:1388 */         fixture.setHomeTeam(rs.getString("home_team"));
/* 1299:1389 */         fixture.setHomeScore(rs.getString("home_score"));
/* 1300:1390 */         fixture.setAwayTeam(rs.getString("away_team"));
/* 1301:1391 */         fixture.setAwayScore(rs.getString("away_score"));
/* 1302:1392 */         fixture.setStatus(rs.getInt("status"));
/* 1303:1393 */         fixture.setEventNotifSent(rs.getInt("event_notif_sent"));
/* 1304:     */         
/* 1305:1395 */         SQL = "SELECT * FROM livescore_game_alias where game_id='" + fixture.getGameId() + "'";
/* 1306:1396 */         prepstat = con.prepareStatement(SQL);
/* 1307:1397 */         rs2 = prepstat.executeQuery();
/* 1308:1398 */         while (rs2.next()) {
/* 1309:1399 */           fixture.setAlias(rs2.getString("alias"));
/* 1310:     */         }
/* 1311:1402 */         fixtures.add(fixture);
/* 1312:     */       }
/* 1313:     */     }
/* 1314:     */     catch (Exception ex)
/* 1315:     */     {
/* 1316:1406 */       if (con != null) {
/* 1317:1407 */         con.close();
/* 1318:     */       }
/* 1319:1409 */       throw new Exception(ex.getMessage());
/* 1320:     */     }
/* 1321:1411 */     if (con != null) {
/* 1322:1412 */       con.close();
/* 1323:     */     }
/* 1324:1415 */     return fixtures;
/* 1325:     */   }
/* 1326:     */   
/* 1327:     */   public static void deleteFixture(String keyword)
/* 1328:     */     throws Exception
/* 1329:     */   {
/* 1330:1421 */     ResultSet rs = null;
/* 1331:1422 */     Connection con = null;
/* 1332:1423 */     PreparedStatement prepstat = null;
/* 1333:     */     try
/* 1334:     */     {
/* 1335:1426 */       con = DConnect.getConnection();
/* 1336:     */       
/* 1337:     */ 
/* 1338:1429 */       String SQL = "delete from livescore_fixtures where game_id='" + keyword + "'";
/* 1339:1430 */       prepstat = con.prepareStatement(SQL);
/* 1340:1431 */       prepstat.execute();
/* 1341:     */     }
/* 1342:     */     catch (Exception ex)
/* 1343:     */     {
/* 1344:1436 */       throw new Exception(ex.getMessage());
/* 1345:     */     }
/* 1346:     */     finally
/* 1347:     */     {
/* 1348:1439 */       if (con != null) {
/* 1349:1440 */         con.close();
/* 1350:     */       }
/* 1351:     */     }
/* 1352:     */   }
/* 1353:     */   
/* 1354:     */   public static synchronized void subscribeForGame(String accountId, String gameId, String msisdn)
/* 1355:     */     throws Exception
/* 1356:     */   {
/* 1357:1447 */     String regId = "";
/* 1358:1448 */     int error = 0;
/* 1359:1449 */     ResultSet rs = null;
/* 1360:1450 */     Connection con = null;
/* 1361:1451 */     PreparedStatement prepstat = null;
/* 1362:     */     try
/* 1363:     */     {
/* 1364:1454 */       con = DConnect.getConnection();
/* 1365:     */       
/* 1366:     */ 
/* 1367:1457 */       String SQL = "select * from address_book where account_id='" + accountId + "' and msisdn='" + msisdn + "'";
/* 1368:1458 */       prepstat = con.prepareStatement(SQL);
/* 1369:1459 */       rs = prepstat.executeQuery();
/* 1370:1461 */       if (!rs.next())
/* 1371:     */       {
/* 1372:1464 */         SQL = "Insert into address_book (account_id,msisdn,registration_id) values(?,?,?)";
/* 1373:1465 */         prepstat = con.prepareStatement(SQL);
/* 1374:1466 */         regId = uidGen.generateNumberID(6);
/* 1375:     */         
/* 1376:1468 */         prepstat.setString(1, accountId);
/* 1377:1469 */         prepstat.setString(2, msisdn);
/* 1378:1470 */         prepstat.setString(3, regId);
/* 1379:1471 */         prepstat.execute();
/* 1380:     */       }
/* 1381:1474 */       SQL = "select * from service_subscription where keyword='" + gameId + "' and account_id='" + accountId + "' and msisdn='" + msisdn + "'";
/* 1382:1475 */       prepstat = con.prepareStatement(SQL);
/* 1383:1476 */       rs = prepstat.executeQuery();
/* 1384:1478 */       if (!rs.next())
/* 1385:     */       {
/* 1386:1479 */         SQL = "Insert into service_subscription (subscription_date,msisdn,keyword,account_id,status) values(?,?,?,?,?)";
/* 1387:1480 */         prepstat = con.prepareStatement(SQL);
/* 1388:1481 */         prepstat.setTimestamp(1, new Timestamp(Calendar.getInstance().getTime().getTime()));
/* 1389:1482 */         prepstat.setString(2, msisdn);
/* 1390:1483 */         prepstat.setString(3, gameId);
/* 1391:1484 */         prepstat.setString(4, accountId);
/* 1392:1485 */         prepstat.setInt(5, 1);
/* 1393:1486 */         prepstat.execute();
/* 1394:     */         
/* 1395:     */ 
/* 1396:1489 */         SQL = "select no_subscribers from livescore_fixture_mgt where account_id=? and game_id=?";
/* 1397:1490 */         prepstat = con.prepareStatement(SQL);
/* 1398:1491 */         prepstat.setString(1, accountId);
/* 1399:1492 */         prepstat.setString(2, gameId);
/* 1400:1493 */         rs = prepstat.executeQuery();
/* 1401:     */         
/* 1402:1495 */         int no_subs = 1;
/* 1403:1496 */         if (rs.next())
/* 1404:     */         {
/* 1405:1497 */           no_subs += rs.getInt("no_subscribers");
/* 1406:1498 */           SQL = "update livescore_fixture_mgt set no_subscribers = ? where account_id=? and game_id=?";
/* 1407:     */         }
/* 1408:     */         else
/* 1409:     */         {
/* 1410:1500 */           SQL = "insert into livescore_fixture_mgt(no_subscribers,account_id,game_id) values(?,?,?)";
/* 1411:     */         }
/* 1412:1503 */         prepstat = con.prepareStatement(SQL);
/* 1413:1504 */         prepstat.setInt(1, no_subs);
/* 1414:1505 */         prepstat.setString(2, accountId);
/* 1415:1506 */         prepstat.setString(3, gameId);
/* 1416:1507 */         prepstat.executeUpdate();
/* 1417:     */       }
/* 1418:     */     }
/* 1419:     */     catch (Exception ex)
/* 1420:     */     {
/* 1421:1510 */       if (con != null)
/* 1422:     */       {
/* 1423:     */         try
/* 1424:     */         {
/* 1425:1512 */           con.close();
/* 1426:     */         }
/* 1427:     */         catch (SQLException ex1)
/* 1428:     */         {
/* 1429:1514 */           System.out.println(ex1.getMessage());
/* 1430:     */         }
/* 1431:1516 */         con = null;
/* 1432:     */       }
/* 1433:     */     }
/* 1434:     */     finally
/* 1435:     */     {
/* 1436:1519 */       if (rs != null)
/* 1437:     */       {
/* 1438:     */         try
/* 1439:     */         {
/* 1440:1521 */           rs.close();
/* 1441:     */         }
/* 1442:     */         catch (SQLException e) {}
/* 1443:1523 */         rs = null;
/* 1444:     */       }
/* 1445:1525 */       if (prepstat != null)
/* 1446:     */       {
/* 1447:     */         try
/* 1448:     */         {
/* 1449:1527 */           prepstat.close();
/* 1450:     */         }
/* 1451:     */         catch (SQLException e) {}
/* 1452:1529 */         prepstat = null;
/* 1453:     */       }
/* 1454:1531 */       if (con != null)
/* 1455:     */       {
/* 1456:     */         try
/* 1457:     */         {
/* 1458:1533 */           con.close();
/* 1459:     */         }
/* 1460:     */         catch (SQLException e) {}
/* 1461:1535 */         con = null;
/* 1462:     */       }
/* 1463:     */     }
/* 1464:     */   }
/* 1465:     */   
/* 1466:     */   public static synchronized void unsubscribeFromGame(String msisdn, String gameId, String accountId)
/* 1467:     */     throws Exception
/* 1468:     */   {
/* 1469:1542 */     ResultSet rs = null;
/* 1470:1543 */     Connection con = null;
/* 1471:1544 */     PreparedStatement prepstat = null;
/* 1472:     */     try
/* 1473:     */     {
/* 1474:1547 */       con = DConnect.getConnection();
/* 1475:1548 */       String SQL = "delete from service_subscription where msisdn='" + msisdn + "' and keyword='" + gameId + "' and account_id='" + accountId + "'";
/* 1476:1549 */       prepstat = con.prepareStatement(SQL);
/* 1477:1550 */       prepstat.execute();
/* 1478:     */       
/* 1479:     */ 
/* 1480:1553 */       SQL = "select no_subscribers from livescore_fixture_mgt where account_id=? and game_id=?";
/* 1481:1554 */       prepstat = con.prepareStatement(SQL);
/* 1482:1555 */       prepstat.setString(1, accountId);
/* 1483:1556 */       prepstat.setString(2, gameId);
/* 1484:1557 */       rs = prepstat.executeQuery();
/* 1485:     */       
/* 1486:1559 */       int no_subs = 1;
/* 1487:1560 */       if (rs.next())
/* 1488:     */       {
/* 1489:1561 */         int count = rs.getInt("no_subscribers");
/* 1490:1562 */         if (count > 0) {
/* 1491:1563 */           no_subs = count - no_subs;
/* 1492:     */         } else {
/* 1493:1565 */           no_subs = 0;
/* 1494:     */         }
/* 1495:1568 */         SQL = "update livescore_fixture_mgt set no_subscribers = ? where account_id=? and game_id=?";
/* 1496:     */       }
/* 1497:     */       else
/* 1498:     */       {
/* 1499:1570 */         SQL = "insert into livescore_fixture_mgt(no_subscribers,account_id,game_id) values(?,?,?)";
/* 1500:1571 */         no_subs = 0;
/* 1501:     */       }
/* 1502:1574 */       prepstat = con.prepareStatement(SQL);
/* 1503:1575 */       prepstat.setInt(1, no_subs);
/* 1504:1576 */       prepstat.setString(2, accountId);
/* 1505:1577 */       prepstat.setString(3, gameId);
/* 1506:1578 */       prepstat.executeUpdate();
/* 1507:     */     }
/* 1508:     */     catch (Exception ex)
/* 1509:     */     {
/* 1510:1580 */       if (con != null)
/* 1511:     */       {
/* 1512:     */         try
/* 1513:     */         {
/* 1514:1582 */           con.close();
/* 1515:     */         }
/* 1516:     */         catch (SQLException ex1)
/* 1517:     */         {
/* 1518:1584 */           System.out.println(ex1.getMessage());
/* 1519:     */         }
/* 1520:1586 */         con = null;
/* 1521:     */       }
/* 1522:     */     }
/* 1523:     */     finally
/* 1524:     */     {
/* 1525:1589 */       if (rs != null)
/* 1526:     */       {
/* 1527:     */         try
/* 1528:     */         {
/* 1529:1591 */           rs.close();
/* 1530:     */         }
/* 1531:     */         catch (SQLException e) {}
/* 1532:1595 */         rs = null;
/* 1533:     */       }
/* 1534:1597 */       if (prepstat != null)
/* 1535:     */       {
/* 1536:     */         try
/* 1537:     */         {
/* 1538:1599 */           prepstat.close();
/* 1539:     */         }
/* 1540:     */         catch (SQLException e) {}
/* 1541:1603 */         prepstat = null;
/* 1542:     */       }
/* 1543:1605 */       if (con != null)
/* 1544:     */       {
/* 1545:     */         try
/* 1546:     */         {
/* 1547:1607 */           con.close();
/* 1548:     */         }
/* 1549:     */         catch (SQLException e) {}
/* 1550:1611 */         con = null;
/* 1551:     */       }
/* 1552:     */     }
/* 1553:     */   }
/* 1554:     */   
/* 1555:     */   public static void updateSubscriptionStatus(String gameId, int subStatus, int gameStatus)
/* 1556:     */     throws Exception
/* 1557:     */   {
/* 1558:1618 */     ResultSet rs = null;
/* 1559:1619 */     Connection con = null;
/* 1560:1620 */     PreparedStatement prepstat = null;
/* 1561:1621 */     boolean bError = false;
/* 1562:     */     try
/* 1563:     */     {
/* 1564:1624 */       con = DConnect.getConnection();
/* 1565:1625 */       con.setAutoCommit(false);
/* 1566:     */       
/* 1567:     */ 
/* 1568:1628 */       String SQL = "update service_subscription set status=? where keyword=? and account_id in(select account_id from keyword_mapping where mapping in(select league_id from livescore_fixtures where game_id=? and status=?) )";
/* 1569:     */       
/* 1570:     */ 
/* 1571:1631 */       prepstat = con.prepareStatement(SQL);
/* 1572:     */       
/* 1573:     */ 
/* 1574:1634 */       prepstat.setString(1, "" + subStatus);
/* 1575:1635 */       prepstat.setString(2, gameId);
/* 1576:1636 */       prepstat.setString(3, gameId);
/* 1577:1637 */       prepstat.setString(4, "" + gameStatus);
/* 1578:     */       
/* 1579:1639 */       prepstat.execute();
/* 1580:     */     }
/* 1581:     */     catch (Exception ex)
/* 1582:     */     {
/* 1583:1641 */       if (con != null) {
/* 1584:1642 */         con.close();
/* 1585:     */       }
/* 1586:1644 */       bError = true;
/* 1587:1645 */       throw new Exception(ex.getMessage());
/* 1588:     */     }
/* 1589:     */     finally
/* 1590:     */     {
/* 1591:1648 */       if (con != null) {
/* 1592:1649 */         con.close();
/* 1593:     */       }
/* 1594:     */     }
/* 1595:     */   }
/* 1596:     */   
/* 1597:     */   public static ArrayList viewDistinctGameTimesForDate(java.util.Date date, int status)
/* 1598:     */     throws Exception
/* 1599:     */   {
/* 1600:1655 */     int DEFAULT_ALLOWANCE = 0;
/* 1601:1656 */     int OUTLOOK_PERIOD = 0;
/* 1602:     */     try
/* 1603:     */     {
/* 1604:1659 */       String value = PropertyHolder.getPropsValue("LS_TRIGGER_ALLOWANCE");
/* 1605:1660 */       if ((value != null) && (!value.equals(""))) {
/* 1606:1661 */         DEFAULT_ALLOWANCE = Integer.parseInt(value);
/* 1607:     */       } else {
/* 1608:1663 */         DEFAULT_ALLOWANCE = 4;
/* 1609:     */       }
/* 1610:     */     }
/* 1611:     */     catch (Exception e)
/* 1612:     */     {
/* 1613:1666 */       DEFAULT_ALLOWANCE = 4;
/* 1614:     */     }
/* 1615:     */     try
/* 1616:     */     {
/* 1617:1670 */       String value = PropertyHolder.getPropsValue("LS_GAME_OUTLOOK");
/* 1618:1671 */       if ((value != null) && (!value.equals(""))) {
/* 1619:1672 */         OUTLOOK_PERIOD = Integer.parseInt(value);
/* 1620:     */       } else {
/* 1621:1674 */         OUTLOOK_PERIOD = 24;
/* 1622:     */       }
/* 1623:     */     }
/* 1624:     */     catch (Exception e)
/* 1625:     */     {
/* 1626:1677 */       OUTLOOK_PERIOD = 24;
/* 1627:     */     }
/* 1628:1680 */     ArrayList dates = new ArrayList();
/* 1629:     */     
/* 1630:     */ 
/* 1631:1683 */     ResultSet rs_dates = null;
/* 1632:1684 */     Connection con = null;
/* 1633:1685 */     PreparedStatement prepstat = null;
/* 1634:     */     
/* 1635:1687 */     Calendar c = Calendar.getInstance();
/* 1636:1688 */     c.setTime(date);
/* 1637:1689 */     int addition = OUTLOOK_PERIOD + DEFAULT_ALLOWANCE;
/* 1638:1690 */     c.add(11, addition);
/* 1639:1691 */     java.util.Date endDate = c.getTime();
/* 1640:     */     try
/* 1641:     */     {
/* 1642:1694 */       con = DConnect.getConnection();
/* 1643:     */       
/* 1644:1696 */       String query = "select date from livescore_fixtures where livescore_fixtures.status='" + status + "' and  (date >= ? and date <= ?) group by date (date), hour (date)";
/* 1645:1697 */       prepstat = con.prepareStatement(query);
/* 1646:1698 */       prepstat.setTimestamp(1, new Timestamp(date.getTime()));
/* 1647:1699 */       prepstat.setTimestamp(2, new Timestamp(endDate.getTime()));
/* 1648:1700 */       rs_dates = prepstat.executeQuery();
/* 1649:1702 */       while (rs_dates.next())
/* 1650:     */       {
/* 1651:1703 */         LiveScoreFixture fixture = new LiveScoreFixture();
/* 1652:1704 */         String dateStr = rs_dates.getString("date");
/* 1653:1705 */         dates.add(dateStr);
/* 1654:     */       }
/* 1655:     */     }
/* 1656:     */     catch (Exception ex)
/* 1657:     */     {
/* 1658:1708 */       if (con != null) {
/* 1659:1709 */         con.close();
/* 1660:     */       }
/* 1661:1711 */       throw new Exception(ex.getMessage());
/* 1662:     */     }
/* 1663:1713 */     if (con != null) {
/* 1664:1714 */       con.close();
/* 1665:     */     }
/* 1666:1716 */     return dates;
/* 1667:     */   }
/* 1668:     */   
/* 1669:     */   public static String viewGameIdForAlias(String alias)
/* 1670:     */     throws Exception
/* 1671:     */   {
/* 1672:1720 */     String gameId = "";
/* 1673:     */     
/* 1674:     */ 
/* 1675:1723 */     ResultSet rs = null;
/* 1676:1724 */     Connection con = null;
/* 1677:1725 */     PreparedStatement prepstat = null;
/* 1678:     */     try
/* 1679:     */     {
/* 1680:1727 */       con = DConnect.getConnection();
/* 1681:     */       
/* 1682:1729 */       String SQL = "SELECT game_id FROM livescore_game_alias where alias='" + alias + "'";
/* 1683:     */       
/* 1684:1731 */       prepstat = con.prepareStatement(SQL);
/* 1685:1732 */       rs = prepstat.executeQuery();
/* 1686:1734 */       while (rs.next()) {
/* 1687:1735 */         gameId = rs.getString("game_id");
/* 1688:     */       }
/* 1689:     */     }
/* 1690:     */     catch (Exception ex)
/* 1691:     */     {
/* 1692:1738 */       if (con != null) {
/* 1693:1739 */         con.close();
/* 1694:     */       }
/* 1695:1741 */       throw new Exception(ex.getMessage());
/* 1696:     */     }
/* 1697:1743 */     if (con != null) {
/* 1698:1744 */       con.close();
/* 1699:     */     }
/* 1700:1747 */     return gameId;
/* 1701:     */   }
/* 1702:     */   
/* 1703:     */   public static String viewAliasForGameId(String gameId)
/* 1704:     */     throws Exception
/* 1705:     */   {
/* 1706:1751 */     String alias = "";
/* 1707:     */     
/* 1708:     */ 
/* 1709:1754 */     ResultSet rs = null;
/* 1710:1755 */     Connection con = null;
/* 1711:1756 */     PreparedStatement prepstat = null;
/* 1712:     */     try
/* 1713:     */     {
/* 1714:1758 */       con = DConnect.getConnection();
/* 1715:     */       
/* 1716:1760 */       String SQL = "SELECT alias FROM livescore_game_alias where game_id='" + gameId + "'";
/* 1717:     */       
/* 1718:1762 */       prepstat = con.prepareStatement(SQL);
/* 1719:1763 */       rs = prepstat.executeQuery();
/* 1720:1765 */       while (rs.next()) {
/* 1721:1766 */         alias = rs.getString("alias");
/* 1722:     */       }
/* 1723:     */     }
/* 1724:     */     catch (Exception ex)
/* 1725:     */     {
/* 1726:1769 */       if (con != null) {
/* 1727:1770 */         con.close();
/* 1728:     */       }
/* 1729:1772 */       throw new Exception(ex.getMessage());
/* 1730:     */     }
/* 1731:1774 */     if (con != null) {
/* 1732:1775 */       con.close();
/* 1733:     */     }
/* 1734:1778 */     return alias;
/* 1735:     */   }
/* 1736:     */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.livescore.LiveScoreFixtureDB
 * JD-Core Version:    0.7.0.1
 */