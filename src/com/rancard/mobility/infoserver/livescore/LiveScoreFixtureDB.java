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
/*  112: 117 */       if (con != null) {
/*  113: 118 */         con.close();
/*  114:     */       }
/*  115: 120 */       throw new Exception(ex.getMessage());
/*  116:     */     }
/*  117:     */     finally
/*  118:     */     {
/*  119: 123 */       if (con != null) {
/*  120: 124 */         con.close();
/*  121:     */       }
/*  122:     */     }
/*  123:     */   }
/*  124:     */   
/*  125:     */   public static void updateFixture(String match_id, int status, String date)
/*  126:     */     throws Exception
/*  127:     */   {
/*  128: 132 */     ResultSet rs = null;
/*  129: 133 */     Connection con = null;
/*  130: 134 */     PreparedStatement prepstat = null;
/*  131:     */     try
/*  132:     */     {
/*  133: 137 */       con = DConnect.getConnection();
/*  134:     */       
/*  135:     */ 
/*  136: 140 */       String SQL = "update livescore_fixtures set livescore_fixtures.status=?, date=? where livescore_fixtures.game_id=?";
/*  137: 141 */       prepstat = con.prepareStatement(SQL);
/*  138: 142 */       prepstat.setInt(1, status);
/*  139: 143 */       prepstat.setString(2, date);
/*  140: 144 */       prepstat.setString(3, match_id);
/*  141: 145 */       prepstat.execute();
/*  142:     */     }
/*  143:     */     catch (Exception ex)
/*  144:     */     {
/*  145: 147 */       if (con != null) {
/*  146: 148 */         con.close();
/*  147:     */       }
/*  148: 150 */       throw new Exception(ex.getMessage());
/*  149:     */     }
/*  150:     */     finally
/*  151:     */     {
/*  152: 153 */       if (con != null) {
/*  153: 154 */         con.close();
/*  154:     */       }
/*  155:     */     }
/*  156:     */   }
/*  157:     */   
/*  158:     */   public static void updateFixture(String match_id, String homeScore, String awayScore, String date)
/*  159:     */     throws Exception
/*  160:     */   {
/*  161: 162 */     ResultSet rs = null;
/*  162: 163 */     Connection con = null;
/*  163: 164 */     PreparedStatement prepstat = null;
/*  164:     */     try
/*  165:     */     {
/*  166: 167 */       con = DConnect.getConnection();
/*  167:     */       
/*  168:     */ 
/*  169: 170 */       String SQL = "update livescore_fixtures set home_score=?, away_score=?, date=? where livescore_fixtures.game_id=?";
/*  170: 171 */       prepstat = con.prepareStatement(SQL);
/*  171: 172 */       prepstat.setString(1, homeScore);
/*  172: 173 */       prepstat.setString(2, awayScore);
/*  173: 174 */       prepstat.setString(3, date);
/*  174: 175 */       prepstat.setString(4, match_id);
/*  175: 176 */       prepstat.execute();
/*  176:     */     }
/*  177:     */     catch (Exception ex)
/*  178:     */     {
/*  179: 178 */       if (con != null) {
/*  180: 179 */         con.close();
/*  181:     */       }
/*  182: 181 */       throw new Exception(ex.getMessage());
/*  183:     */     }
/*  184:     */     finally
/*  185:     */     {
/*  186: 184 */       if (con != null) {
/*  187: 185 */         con.close();
/*  188:     */       }
/*  189:     */     }
/*  190:     */   }
/*  191:     */   
/*  192:     */   public static void updateFixture(String match_id, String homeScore, String awayScore, int status, String date)
/*  193:     */     throws Exception
/*  194:     */   {
/*  195: 193 */     ResultSet rs = null;
/*  196: 194 */     Connection con = null;
/*  197: 195 */     PreparedStatement prepstat = null;
/*  198:     */     try
/*  199:     */     {
/*  200: 198 */       con = DConnect.getConnection();
/*  201:     */       
/*  202:     */ 
/*  203: 201 */       String SQL = "update livescore_fixtures set home_score=?, away_score=?, livescore_fixtures.status=?, date=? where livescore_fixtures.game_id=?";
/*  204: 202 */       prepstat = con.prepareStatement(SQL);
/*  205: 203 */       prepstat.setString(1, homeScore);
/*  206: 204 */       prepstat.setString(2, awayScore);
/*  207: 205 */       prepstat.setInt(3, status);
/*  208: 206 */       prepstat.setString(4, date);
/*  209: 207 */       prepstat.setString(5, match_id);
/*  210: 208 */       prepstat.execute();
/*  211:     */     }
/*  212:     */     catch (Exception ex)
/*  213:     */     {
/*  214: 210 */       if (con != null) {
/*  215: 211 */         con.close();
/*  216:     */       }
/*  217: 213 */       throw new Exception(ex.getMessage());
/*  218:     */     }
/*  219:     */     finally
/*  220:     */     {
/*  221: 216 */       if (con != null) {
/*  222: 217 */         con.close();
/*  223:     */       }
/*  224:     */     }
/*  225:     */   }
/*  226:     */   
/*  227:     */   public static LiveScoreFixture viewFixture(String gameId)
/*  228:     */     throws Exception
/*  229:     */   {
/*  230: 224 */     LiveScoreFixture fixture = new LiveScoreFixture();
/*  231:     */     
/*  232:     */ 
/*  233: 227 */     ResultSet rs = null;
/*  234: 228 */     Connection con = null;
/*  235: 229 */     PreparedStatement prepstat = null;
/*  236:     */     try
/*  237:     */     {
/*  238: 231 */       con = DConnect.getConnection();
/*  239:     */       
/*  240: 233 */       String SQL = "SELECT * FROM livescore_fixtures where game_id='" + gameId + "'";
/*  241:     */       
/*  242:     */ 
/*  243: 236 */       prepstat = con.prepareStatement(SQL);
/*  244: 237 */       rs = prepstat.executeQuery();
/*  245: 239 */       while (rs.next())
/*  246:     */       {
/*  247: 240 */         fixture.setCountryName(rs.getString("country_name"));
/*  248: 241 */         fixture.setDate(rs.getTimestamp("date").toString());
/*  249: 242 */         fixture.setGameId(rs.getString("game_id"));
/*  250: 243 */         fixture.setLeagueId(rs.getString("league_id"));
/*  251: 244 */         fixture.setLeagueName(rs.getString("league_name"));
/*  252: 245 */         fixture.setHomeTeam(rs.getString("home_team"));
/*  253: 246 */         fixture.setHomeScore(rs.getString("home_score"));
/*  254: 247 */         fixture.setAwayTeam(rs.getString("away_team"));
/*  255: 248 */         fixture.setAwayScore(rs.getString("away_score"));
/*  256: 249 */         fixture.setStatus(rs.getInt("status"));
/*  257: 250 */         fixture.setEventNotifSent(rs.getInt("event_notif_sent"));
/*  258:     */       }
/*  259: 253 */       SQL = "SELECT * FROM livescore_game_alias where game_id='" + gameId + "'";
/*  260: 254 */       prepstat = con.prepareStatement(SQL);
/*  261: 255 */       rs = prepstat.executeQuery();
/*  262: 256 */       while (rs.next()) {
/*  263: 257 */         fixture.setAlias(rs.getString("alias"));
/*  264:     */       }
/*  265:     */     }
/*  266:     */     catch (Exception ex)
/*  267:     */     {
/*  268: 260 */       if (con != null) {
/*  269: 261 */         con.close();
/*  270:     */       }
/*  271: 263 */       throw new Exception(ex.getMessage());
/*  272:     */     }
/*  273: 265 */     if (con != null) {
/*  274: 266 */       con.close();
/*  275:     */     }
/*  276: 269 */     return fixture;
/*  277:     */   }
/*  278:     */   
/*  279:     */   public static LiveScoreFixture viewFixtureForCP(String gameId, String accountId)
/*  280:     */     throws Exception
/*  281:     */   {
/*  282: 273 */     LiveScoreFixture fixture = new LiveScoreFixture();
/*  283:     */     
/*  284:     */ 
/*  285: 276 */     ResultSet rs = null;
/*  286: 277 */     Connection con = null;
/*  287: 278 */     PreparedStatement prepstat = null;
/*  288:     */     try
/*  289:     */     {
/*  290: 280 */       con = DConnect.getConnection();
/*  291:     */       
/*  292: 282 */       String SQL = "SELECT * FROM livescore_fixture_mgt lfm inner join livescore_fixtures lf on lf.game_id=lfm.game_id where lfm.game_id='" + gameId + "' and lfm.account_id='" + accountId + "'";
/*  293:     */       
/*  294:     */ 
/*  295:     */ 
/*  296: 286 */       prepstat = con.prepareStatement(SQL);
/*  297: 287 */       rs = prepstat.executeQuery();
/*  298: 289 */       while (rs.next())
/*  299:     */       {
/*  300: 290 */         fixture.setCountryName(rs.getString("country_name"));
/*  301: 291 */         fixture.setDate(rs.getTimestamp("date").toString());
/*  302: 292 */         fixture.setGameId(rs.getString("game_id"));
/*  303: 293 */         fixture.setLeagueId(rs.getString("league_id"));
/*  304: 294 */         fixture.setLeagueName(rs.getString("league_name"));
/*  305: 295 */         fixture.setHomeTeam(rs.getString("home_team"));
/*  306: 296 */         fixture.setHomeScore(rs.getString("home_score"));
/*  307: 297 */         fixture.setAwayTeam(rs.getString("away_team"));
/*  308: 298 */         fixture.setAwayScore(rs.getString("away_score"));
/*  309: 299 */         fixture.setStatus(rs.getInt("status"));
/*  310: 300 */         fixture.setEventNotifSent(rs.getInt("event_notif_sent"));
/*  311:     */       }
/*  312: 303 */       SQL = "SELECT * FROM livescore_game_alias where game_id='" + gameId + "'";
/*  313: 304 */       prepstat = con.prepareStatement(SQL);
/*  314: 305 */       rs = prepstat.executeQuery();
/*  315: 306 */       while (rs.next()) {
/*  316: 307 */         fixture.setAlias(rs.getString("alias"));
/*  317:     */       }
/*  318:     */     }
/*  319:     */     catch (Exception ex)
/*  320:     */     {
/*  321: 310 */       if (con != null) {
/*  322: 311 */         con.close();
/*  323:     */       }
/*  324: 313 */       throw new Exception(ex.getMessage());
/*  325:     */     }
/*  326: 315 */     if (con != null) {
/*  327: 316 */       con.close();
/*  328:     */     }
/*  329: 319 */     return fixture;
/*  330:     */   }
/*  331:     */   
/*  332:     */   public static LiveScoreFixture viewFixture(String gameId, String date)
/*  333:     */     throws Exception
/*  334:     */   {
/*  335: 323 */     LiveScoreFixture fixture = new LiveScoreFixture();
/*  336:     */     
/*  337:     */ 
/*  338: 326 */     ResultSet rs = null;
/*  339: 327 */     Connection con = null;
/*  340: 328 */     PreparedStatement prepstat = null;
/*  341:     */     try
/*  342:     */     {
/*  343: 332 */       con = DConnect.getConnection();
/*  344:     */       
/*  345: 334 */       SimpleDateFormat s_formatter = new SimpleDateFormat("MM-dd-yyyy");
/*  346: 335 */       java.util.Date validDate = null;
/*  347:     */       try
/*  348:     */       {
/*  349: 337 */         validDate = s_formatter.parse(date);
/*  350:     */       }
/*  351:     */       catch (Exception ex)
/*  352:     */       {
/*  353: 339 */         validDate = Calendar.getInstance().getTime();
/*  354:     */       }
/*  355: 343 */       String SQL = "SELECT * FROM livescore_fixtures where game_id=? and DATE(livescore_fixtures.date) =?";
/*  356:     */       
/*  357: 345 */       prepstat = con.prepareStatement(SQL);
/*  358: 346 */       prepstat.setString(1, gameId);
/*  359: 347 */       prepstat.setDate(2, new java.sql.Date(validDate.getTime()));
/*  360:     */       
/*  361: 349 */       rs = prepstat.executeQuery();
/*  362: 351 */       while (rs.next())
/*  363:     */       {
/*  364: 352 */         fixture.setCountryName(rs.getString("country_name"));
/*  365: 353 */         fixture.setDate(rs.getTimestamp("date").toString());
/*  366: 354 */         fixture.setGameId(rs.getString("game_id"));
/*  367: 355 */         fixture.setLeagueId(rs.getString("league_id"));
/*  368: 356 */         fixture.setLeagueName(rs.getString("league_name"));
/*  369: 357 */         fixture.setHomeTeam(rs.getString("home_team"));
/*  370: 358 */         fixture.setHomeScore(rs.getString("home_score"));
/*  371: 359 */         fixture.setAwayTeam(rs.getString("away_team"));
/*  372: 360 */         fixture.setAwayScore(rs.getString("away_score"));
/*  373: 361 */         fixture.setStatus(rs.getInt("status"));
/*  374: 362 */         fixture.setEventNotifSent(rs.getInt("event_notif_sent"));
/*  375:     */       }
/*  376: 365 */       SQL = "SELECT * FROM livescore_game_alias where game_id='" + gameId + "'";
/*  377: 366 */       prepstat = con.prepareStatement(SQL);
/*  378: 367 */       rs = prepstat.executeQuery();
/*  379: 368 */       while (rs.next()) {
/*  380: 369 */         fixture.setAlias(rs.getString("alias"));
/*  381:     */       }
/*  382:     */     }
/*  383:     */     catch (Exception ex)
/*  384:     */     {
/*  385: 372 */       if (con != null) {
/*  386: 373 */         con.close();
/*  387:     */       }
/*  388: 375 */       throw new Exception(ex.getMessage());
/*  389:     */     }
/*  390: 377 */     if (con != null) {
/*  391: 378 */       con.close();
/*  392:     */     }
/*  393: 381 */     return fixture;
/*  394:     */   }
/*  395:     */   
/*  396:     */   public static LiveScoreFixture viewFixtureByAlias(String alias)
/*  397:     */     throws Exception
/*  398:     */   {
/*  399: 386 */     LiveScoreFixture fixture = new LiveScoreFixture();
/*  400:     */     
/*  401:     */ 
/*  402: 389 */     ResultSet rs = null;
/*  403: 390 */     Connection con = null;
/*  404: 391 */     PreparedStatement prepstat = null;
/*  405:     */     try
/*  406:     */     {
/*  407: 393 */       con = DConnect.getConnection();
/*  408:     */       
/*  409: 395 */       String SQL = "SELECT * FROM livescore_fixtures lf inner join livescore_game_alias lg on lf.game_id=lg.game_id where lg.alias='" + alias + "'";
/*  410:     */       
/*  411: 397 */       prepstat = con.prepareStatement(SQL);
/*  412: 398 */       rs = prepstat.executeQuery();
/*  413: 400 */       while (rs.next())
/*  414:     */       {
/*  415: 401 */         fixture.setCountryName(rs.getString("lf.country_name"));
/*  416: 402 */         fixture.setDate(rs.getTimestamp("lf.date").toString());
/*  417: 403 */         fixture.setGameId(rs.getString("lf.game_id"));
/*  418: 404 */         fixture.setLeagueId(rs.getString("lf.league_id"));
/*  419: 405 */         fixture.setLeagueName(rs.getString("lf.league_name"));
/*  420: 406 */         fixture.setHomeTeam(rs.getString("lf.home_team"));
/*  421: 407 */         fixture.setHomeScore(rs.getString("lf.home_score"));
/*  422: 408 */         fixture.setAwayTeam(rs.getString("lf.away_team"));
/*  423: 409 */         fixture.setAwayScore(rs.getString("lf.away_score"));
/*  424: 410 */         fixture.setStatus(rs.getInt("lf.status"));
/*  425: 411 */         fixture.setEventNotifSent(rs.getInt("lf.event_notif_sent"));
/*  426: 412 */         fixture.setAlias(alias);
/*  427:     */       }
/*  428:     */     }
/*  429:     */     catch (Exception ex)
/*  430:     */     {
/*  431: 415 */       if (con != null) {
/*  432: 416 */         con.close();
/*  433:     */       }
/*  434: 418 */       throw new Exception(ex.getMessage());
/*  435:     */     }
/*  436: 420 */     if (con != null) {
/*  437: 421 */       con.close();
/*  438:     */     }
/*  439: 424 */     return fixture;
/*  440:     */   }
/*  441:     */   
/*  442:     */   public static LiveScoreFixture viewFixtureForCPByAlias(String alias, String accountId)
/*  443:     */     throws Exception
/*  444:     */   {
/*  445: 428 */     LiveScoreFixture fixture = new LiveScoreFixture();
/*  446:     */     
/*  447:     */ 
/*  448: 431 */     ResultSet rs = null;
/*  449: 432 */     Connection con = null;
/*  450: 433 */     PreparedStatement prepstat = null;
/*  451:     */     try
/*  452:     */     {
/*  453: 435 */       con = DConnect.getConnection();
/*  454:     */       
/*  455: 437 */       String SQL = "SELECT * FROM livescore_fixture_mgt lfm inner join livescore_fixtures lf on lf.game_id=lfm.game_id inner join livescore_game_alias lg on lf.game_id=lg.game_id where lg.alias='" + alias + "'" + " and lfm.account_id='" + accountId + "'";
/*  456:     */       
/*  457:     */ 
/*  458:     */ 
/*  459: 441 */       prepstat = con.prepareStatement(SQL);
/*  460: 442 */       rs = prepstat.executeQuery();
/*  461: 444 */       while (rs.next())
/*  462:     */       {
/*  463: 445 */         fixture.setCountryName(rs.getString("lf.country_name"));
/*  464: 446 */         fixture.setDate(rs.getTimestamp("lf.date").toString());
/*  465: 447 */         fixture.setGameId(rs.getString("lf.game_id"));
/*  466: 448 */         fixture.setLeagueId(rs.getString("lf.league_id"));
/*  467: 449 */         fixture.setLeagueName(rs.getString("lf.league_name"));
/*  468: 450 */         fixture.setHomeTeam(rs.getString("lf.home_team"));
/*  469: 451 */         fixture.setHomeScore(rs.getString("lf.home_score"));
/*  470: 452 */         fixture.setAwayTeam(rs.getString("lf.away_team"));
/*  471: 453 */         fixture.setAwayScore(rs.getString("lf.away_score"));
/*  472: 454 */         fixture.setStatus(rs.getInt("lf.status"));
/*  473: 455 */         fixture.setEventNotifSent(rs.getInt("lf.event_notif_sent"));
/*  474: 456 */         fixture.setAlias(alias);
/*  475:     */       }
/*  476:     */     }
/*  477:     */     catch (Exception ex)
/*  478:     */     {
/*  479: 459 */       if (con != null) {
/*  480: 460 */         con.close();
/*  481:     */       }
/*  482: 462 */       throw new Exception(ex.getMessage());
/*  483:     */     }
/*  484: 464 */     if (con != null) {
/*  485: 465 */       con.close();
/*  486:     */     }
/*  487: 468 */     return fixture;
/*  488:     */   }
/*  489:     */   
/*  490:     */   public static HashMap viewAllFixturesForDate(String date, int status)
/*  491:     */     throws Exception
/*  492:     */   {
/*  493: 472 */     HashMap groupedFixtures = new HashMap();
/*  494:     */     
/*  495:     */ 
/*  496:     */ 
/*  497: 476 */     ResultSet leagues = null;
/*  498: 477 */     ResultSet fixtures = null;
/*  499: 478 */     ResultSet rs = null;
/*  500: 479 */     Connection con = null;
/*  501: 480 */     PreparedStatement prepstat = null;
/*  502:     */     
/*  503:     */ 
/*  504: 483 */     System.out.println(new java.util.Date() + ":@com.rancard.mobility.infoserver.livescore.LiveScoreFixtureDB: viewAllFixturesForDate(date, status)");
/*  505:     */     try
/*  506:     */     {
/*  507: 486 */       con = DConnect.getConnection();
/*  508:     */       
/*  509:     */ 
/*  510: 489 */       String query = "select league_id from livescore_services";
/*  511: 490 */       prepstat = con.prepareStatement(query);
/*  512: 491 */       leagues = prepstat.executeQuery();
/*  513: 493 */       while (leagues.next())
/*  514:     */       {
/*  515: 494 */         String id = new String();
/*  516: 495 */         id = leagues.getString("league_id");
/*  517:     */         
/*  518: 497 */         String searchDateString = date.substring(0, date.indexOf(":"));
/*  519:     */         
/*  520: 499 */         query = "select * from livescore_fixtures where league_id='" + id + "' and livescore_fixtures.status=" + status + " and  date like '" + searchDateString + "%'";
/*  521:     */         
/*  522:     */ 
/*  523:     */ 
/*  524:     */ 
/*  525: 504 */         prepstat = con.prepareStatement(query);
/*  526: 505 */         fixtures = prepstat.executeQuery();
/*  527:     */         
/*  528: 507 */         ArrayList games = new ArrayList();
/*  529: 509 */         while (fixtures.next())
/*  530:     */         {
/*  531: 510 */           LiveScoreFixture fixture = new LiveScoreFixture();
/*  532:     */           
/*  533: 512 */           fixture.setCountryName(fixtures.getString("country_name"));
/*  534: 513 */           fixture.setDate(fixtures.getTimestamp("date").toString());
/*  535: 514 */           fixture.setGameId(fixtures.getString("game_id"));
/*  536: 515 */           fixture.setLeagueId(fixtures.getString("league_id"));
/*  537: 516 */           fixture.setLeagueName(fixtures.getString("league_name"));
/*  538: 517 */           fixture.setHomeTeam(fixtures.getString("home_team"));
/*  539: 518 */           fixture.setHomeScore(fixtures.getString("home_score"));
/*  540: 519 */           fixture.setAwayTeam(fixtures.getString("away_team"));
/*  541: 520 */           fixture.setAwayScore(fixtures.getString("away_score"));
/*  542: 521 */           fixture.setStatus(fixtures.getInt("status"));
/*  543: 522 */           fixture.setEventNotifSent(fixtures.getInt("event_notif_sent"));
/*  544:     */           
/*  545: 524 */           String SQL = "SELECT * FROM livescore_game_alias where game_id='" + fixture.getGameId() + "'";
/*  546: 525 */           prepstat = con.prepareStatement(SQL);
/*  547: 526 */           rs = prepstat.executeQuery();
/*  548: 527 */           while (rs.next()) {
/*  549: 528 */             fixture.setAlias(rs.getString("alias"));
/*  550:     */           }
/*  551: 531 */           games.add(fixture);
/*  552:     */         }
/*  553: 535 */         groupedFixtures.put(id, games);
/*  554:     */       }
/*  555:     */     }
/*  556:     */     catch (Exception ex)
/*  557:     */     {
/*  558: 538 */       if (con != null) {
/*  559: 539 */         con.close();
/*  560:     */       }
/*  561: 541 */       throw new Exception(ex.getMessage());
/*  562:     */     }
/*  563: 543 */     if (con != null) {
/*  564: 544 */       con.close();
/*  565:     */     }
/*  566: 546 */     return groupedFixtures;
/*  567:     */   }
/*  568:     */   
/*  569:     */   public static HashMap viewAllFixturesForDateGroupByCP(String date, int status, int notifStatus)
/*  570:     */     throws Exception
/*  571:     */   {
/*  572: 551 */     System.out.println(new java.util.Date() + ":@com.rancard.mobility.infoserver.livescore.LiveScoreFixtureDB:viewAllFixturesForDateGroupByCP():");
/*  573:     */     
/*  574: 553 */     HashMap groupedCPFixt = new HashMap();
/*  575:     */     
/*  576:     */ 
/*  577:     */ 
/*  578: 557 */     ResultSet rs = null;
/*  579: 558 */     ResultSet cpRS = null;
/*  580: 559 */     Connection con = null;
/*  581: 560 */     PreparedStatement prepstat = null;
/*  582:     */     
/*  583: 562 */     String searchDateString = date.substring(0, date.indexOf(":"));
/*  584:     */     try
/*  585:     */     {
/*  586: 565 */       con = DConnect.getConnection();
/*  587:     */       
/*  588:     */ 
/*  589:     */ 
/*  590: 569 */       String query = "select count(game_id), account_id from livescore_fixture_mgt where game_id in(select game_id from livescore_fixtures where date like '" + searchDateString + "%') group by account_id";
/*  591:     */       
/*  592:     */ 
/*  593:     */ 
/*  594: 573 */       prepstat = con.prepareStatement(query);
/*  595: 574 */       cpRS = prepstat.executeQuery();
/*  596:     */       
/*  597:     */ 
/*  598: 577 */       String accountId = "";
/*  599: 578 */       while (cpRS.next())
/*  600:     */       {
/*  601: 580 */         accountId = cpRS.getString("account_id");
/*  602:     */         
/*  603: 582 */         System.out.println(new java.util.Date() + ":retrieving fixtures for CP: " + accountId);
/*  604:     */         
/*  605: 584 */         query = "select * from livescore_fixture_mgt where account_id='" + accountId + "' " + "and game_id in(select game_id from livescore_fixtures where status='" + status + "' " + "and  date like '" + searchDateString + "%' and event_notif_sent='" + notifStatus + "')";
/*  606:     */         
/*  607:     */ 
/*  608:     */ 
/*  609: 588 */         prepstat = con.prepareStatement(query);
/*  610: 589 */         rs = prepstat.executeQuery();
/*  611:     */         
/*  612: 591 */         ArrayList<String> games = new ArrayList();
/*  613: 593 */         while (rs.next())
/*  614:     */         {
/*  615: 595 */           String gameId = rs.getString("game_id");
/*  616:     */           
/*  617: 597 */           games.add(gameId);
/*  618:     */         }
/*  619: 600 */         System.out.println(new java.util.Date() + ":No. Fixtures found for CP (" + accountId + "): " + games.size());
/*  620:     */         
/*  621:     */ 
/*  622: 603 */         groupedCPFixt.put(accountId, games);
/*  623:     */         
/*  624:     */ 
/*  625: 606 */         games = null;
/*  626:     */       }
/*  627: 610 */       System.out.println(new java.util.Date() + ":Total No. CPs: " + groupedCPFixt.size());
/*  628:     */     }
/*  629:     */     catch (Exception ex)
/*  630:     */     {
/*  631: 614 */       System.out.println(new java.util.Date() + ":error getting CPFixtureMatrix:");
/*  632: 615 */       ex.printStackTrace();
/*  633: 617 */       if (con != null) {
/*  634: 618 */         con.close();
/*  635:     */       }
/*  636: 620 */       throw new Exception(ex.getMessage());
/*  637:     */     }
/*  638: 622 */     if (con != null) {
/*  639: 623 */       con.close();
/*  640:     */     }
/*  641: 625 */     return groupedCPFixt;
/*  642:     */   }
/*  643:     */   
/*  644:     */   public static HashMap viewAllActiveFixturesForDate(String date)
/*  645:     */     throws Exception
/*  646:     */   {
/*  647: 629 */     HashMap groupedFixtures = new HashMap();
/*  648:     */     
/*  649:     */ 
/*  650:     */ 
/*  651: 633 */     ResultSet leagues = null;
/*  652: 634 */     ResultSet fixtures = null;
/*  653: 635 */     ResultSet rs = null;
/*  654: 636 */     Connection con = null;
/*  655: 637 */     PreparedStatement prepstat = null;
/*  656:     */     try
/*  657:     */     {
/*  658: 640 */       con = DConnect.getConnection();
/*  659:     */       
/*  660: 642 */       SimpleDateFormat s_formatter = new SimpleDateFormat();
/*  661: 643 */       java.util.Date validDate = null;
/*  662:     */       try
/*  663:     */       {
/*  664: 645 */         validDate = s_formatter.parse(date);
/*  665:     */       }
/*  666:     */       catch (Exception ex)
/*  667:     */       {
/*  668: 647 */         validDate = Calendar.getInstance().getTime();
/*  669:     */       }
/*  670: 650 */       Calendar c = Calendar.getInstance();
/*  671: 651 */       c.setTime(validDate);
/*  672: 652 */       c.add(11, 24);
/*  673: 653 */       java.util.Date endDate = c.getTime();
/*  674:     */       
/*  675: 655 */       String query = "select league_id from livescore_services";
/*  676: 656 */       prepstat = con.prepareStatement(query);
/*  677: 657 */       leagues = prepstat.executeQuery();
/*  678: 659 */       while (leagues.next())
/*  679:     */       {
/*  680: 660 */         String id = new String();
/*  681: 661 */         id = leagues.getString("league_id");
/*  682:     */         
/*  683: 663 */         query = "select * from livescore_fixtures lf where lf.league_id='" + id + "' and lf.status='" + 0 + "' and  (lf.date like '" + date + "%' or lf.date >='" + new Timestamp(validDate.getTime()).toString() + "' and lf.date <='" + new Timestamp(endDate.getTime()).toString() + "')";
/*  684:     */         
/*  685: 665 */         prepstat = con.prepareStatement(query);
/*  686:     */         
/*  687:     */ 
/*  688:     */ 
/*  689:     */ 
/*  690:     */ 
/*  691: 671 */         fixtures = prepstat.executeQuery();
/*  692:     */         
/*  693: 673 */         ArrayList games = new ArrayList();
/*  694: 675 */         while (fixtures.next())
/*  695:     */         {
/*  696: 676 */           LiveScoreFixture fixture = new LiveScoreFixture();
/*  697:     */           
/*  698: 678 */           fixture.setCountryName(fixtures.getString("country_name"));
/*  699: 679 */           fixture.setDate(fixtures.getTimestamp("date").toString());
/*  700: 680 */           fixture.setGameId(fixtures.getString("game_id"));
/*  701: 681 */           fixture.setLeagueId(fixtures.getString("league_id"));
/*  702: 682 */           fixture.setLeagueName(fixtures.getString("league_name"));
/*  703: 683 */           fixture.setHomeTeam(fixtures.getString("home_team"));
/*  704: 684 */           fixture.setHomeScore(fixtures.getString("home_score"));
/*  705: 685 */           fixture.setAwayTeam(fixtures.getString("away_team"));
/*  706: 686 */           fixture.setAwayScore(fixtures.getString("away_score"));
/*  707: 687 */           fixture.setStatus(fixtures.getInt("status"));
/*  708: 688 */           fixture.setEventNotifSent(fixtures.getInt("event_notif_sent"));
/*  709:     */           
/*  710: 690 */           String SQL = "SELECT * FROM livescore_game_alias where game_id='" + fixture.getGameId() + "'";
/*  711: 691 */           prepstat = con.prepareStatement(SQL);
/*  712: 692 */           rs = prepstat.executeQuery();
/*  713: 693 */           while (rs.next()) {
/*  714: 694 */             fixture.setAlias(rs.getString("alias"));
/*  715:     */           }
/*  716: 697 */           games.add(fixture);
/*  717:     */         }
/*  718: 701 */         groupedFixtures.put(id, games);
/*  719:     */       }
/*  720:     */     }
/*  721:     */     catch (Exception ex)
/*  722:     */     {
/*  723: 704 */       if (con != null) {
/*  724: 705 */         con.close();
/*  725:     */       }
/*  726: 707 */       throw new Exception(ex.getMessage());
/*  727:     */     }
/*  728: 709 */     if (con != null) {
/*  729: 710 */       con.close();
/*  730:     */     }
/*  731: 712 */     return groupedFixtures;
/*  732:     */   }
/*  733:     */   
/*  734:     */   public static HashMap viewAllActiveFixturesForDate(String accountId, String date)
/*  735:     */     throws Exception
/*  736:     */   {
/*  737: 716 */     HashMap groupedFixtures = new HashMap();
/*  738:     */     
/*  739:     */ 
/*  740:     */ 
/*  741: 720 */     ResultSet leagues = null;
/*  742: 721 */     ResultSet fixtures = null;
/*  743: 722 */     ResultSet rs = null;
/*  744: 723 */     Connection con = null;
/*  745: 724 */     PreparedStatement prepstat = null;
/*  746:     */     try
/*  747:     */     {
/*  748: 727 */       con = DConnect.getConnection();
/*  749:     */       
/*  750: 729 */       SimpleDateFormat s_formatter = new SimpleDateFormat();
/*  751: 730 */       java.util.Date validDate = null;
/*  752:     */       try
/*  753:     */       {
/*  754: 732 */         validDate = s_formatter.parse(date);
/*  755:     */       }
/*  756:     */       catch (Exception ex)
/*  757:     */       {
/*  758: 734 */         validDate = Calendar.getInstance().getTime();
/*  759:     */       }
/*  760: 737 */       Calendar c = Calendar.getInstance();
/*  761: 738 */       c.setTime(validDate);
/*  762: 739 */       c.add(11, 24);
/*  763: 740 */       java.util.Date endDate = c.getTime();
/*  764:     */       
/*  765: 742 */       String query = "select ls.league_id from livescore_services ls inner join keyword_mapping km on km.mapping=ls.league_id where km.account_id='" + accountId + "'";
/*  766: 743 */       prepstat = con.prepareStatement(query);
/*  767: 744 */       leagues = prepstat.executeQuery();
/*  768: 746 */       while (leagues.next())
/*  769:     */       {
/*  770: 747 */         String id = new String();
/*  771: 748 */         id = leagues.getString("league_id");
/*  772:     */         
/*  773: 750 */         query = "select * from livescore_fixtures lf where lf.league_id='" + id + "' and date(lf.date)=?";
/*  774: 751 */         prepstat = con.prepareStatement(query);
/*  775: 752 */         prepstat.setDate(1, new java.sql.Date(validDate.getTime()));
/*  776: 753 */         fixtures = prepstat.executeQuery();
/*  777:     */         
/*  778: 755 */         ArrayList games = new ArrayList();
/*  779: 757 */         while (fixtures.next())
/*  780:     */         {
/*  781: 758 */           LiveScoreFixture fixture = new LiveScoreFixture();
/*  782:     */           
/*  783: 760 */           fixture.setCountryName(fixtures.getString("country_name"));
/*  784: 761 */           fixture.setDate(fixtures.getTimestamp("date").toString());
/*  785: 762 */           fixture.setGameId(fixtures.getString("game_id"));
/*  786: 763 */           fixture.setLeagueId(fixtures.getString("league_id"));
/*  787: 764 */           fixture.setLeagueName(fixtures.getString("league_name"));
/*  788: 765 */           fixture.setHomeTeam(fixtures.getString("home_team"));
/*  789: 766 */           fixture.setHomeScore(fixtures.getString("home_score"));
/*  790: 767 */           fixture.setAwayTeam(fixtures.getString("away_team"));
/*  791: 768 */           fixture.setAwayScore(fixtures.getString("away_score"));
/*  792: 769 */           fixture.setStatus(fixtures.getInt("status"));
/*  793: 770 */           fixture.setEventNotifSent(fixtures.getInt("event_notif_sent"));
/*  794:     */           
/*  795: 772 */           String SQL = "SELECT * FROM livescore_game_alias where game_id='" + fixture.getGameId() + "'";
/*  796: 773 */           prepstat = con.prepareStatement(SQL);
/*  797: 774 */           rs = prepstat.executeQuery();
/*  798: 775 */           while (rs.next()) {
/*  799: 776 */             fixture.setAlias(rs.getString("alias"));
/*  800:     */           }
/*  801: 779 */           games.add(fixture);
/*  802:     */         }
/*  803: 783 */         groupedFixtures.put(id, games);
/*  804:     */       }
/*  805:     */     }
/*  806:     */     catch (Exception ex)
/*  807:     */     {
/*  808: 786 */       if (con != null) {
/*  809: 787 */         con.close();
/*  810:     */       }
/*  811: 789 */       throw new Exception(ex.getMessage());
/*  812:     */     }
/*  813: 791 */     if (con != null) {
/*  814: 792 */       con.close();
/*  815:     */     }
/*  816: 794 */     return groupedFixtures;
/*  817:     */   }
/*  818:     */   
/*  819:     */   public static ArrayList viewAllActiveFixturesInLeague(String date, String keyword, String accountId)
/*  820:     */     throws Exception
/*  821:     */   {
/*  822: 798 */     ArrayList games = new ArrayList();
/*  823:     */     
/*  824:     */ 
/*  825:     */ 
/*  826: 802 */     ResultSet fixtures = null;
/*  827: 803 */     ResultSet rs = null;
/*  828: 804 */     Connection con = null;
/*  829: 805 */     PreparedStatement prepstat = null;
/*  830:     */     try
/*  831:     */     {
/*  832: 808 */       con = DConnect.getConnection();
/*  833:     */       
/*  834: 810 */       SimpleDateFormat s_formatter = new SimpleDateFormat();
/*  835: 811 */       java.util.Date validDate = null;
/*  836:     */       try
/*  837:     */       {
/*  838: 813 */         validDate = s_formatter.parse(date);
/*  839:     */       }
/*  840:     */       catch (Exception ex)
/*  841:     */       {
/*  842: 815 */         validDate = Calendar.getInstance().getTime();
/*  843:     */       }
/*  844: 818 */       Calendar c = Calendar.getInstance();
/*  845: 819 */       c.setTime(validDate);
/*  846: 820 */       c.add(11, 24);
/*  847: 821 */       java.util.Date endDate = c.getTime();
/*  848:     */       
/*  849:     */ 
/*  850: 824 */       String query = "select * from livescore_fixtures lf inner join keyword_mapping km on lf.league_id=km.mapping inner join service_definition sd on km.account_id=sd.account_id and km.keyword=sd.keyword where sd.keyword='" + keyword + "' and sd.account_id='" + accountId + "' and" + " lf.status in ('" + 2 + "', '" + 0 + "') and  (lf.date like '" + date + "%' or lf.date >='" + new Timestamp(validDate.getTime()).toString() + "' and lf.date <='" + new Timestamp(endDate.getTime()).toString() + "')";
/*  851:     */       
/*  852:     */ 
/*  853:     */ 
/*  854:     */ 
/*  855: 829 */       prepstat = con.prepareStatement(query);
/*  856:     */       
/*  857:     */ 
/*  858: 832 */       fixtures = prepstat.executeQuery();
/*  859: 834 */       while (fixtures.next())
/*  860:     */       {
/*  861: 835 */         LiveScoreFixture fixture = new LiveScoreFixture();
/*  862:     */         
/*  863: 837 */         fixture.setCountryName(fixtures.getString("lf.country_name"));
/*  864: 838 */         fixture.setDate(fixtures.getTimestamp("lf.date").toString());
/*  865: 839 */         fixture.setGameId(fixtures.getString("lf.game_id"));
/*  866: 840 */         fixture.setLeagueId(fixtures.getString("lf.league_id"));
/*  867: 841 */         fixture.setLeagueName(fixtures.getString("lf.league_name"));
/*  868: 842 */         fixture.setHomeTeam(fixtures.getString("lf.home_team"));
/*  869: 843 */         fixture.setHomeScore(fixtures.getString("lf.home_score"));
/*  870: 844 */         fixture.setAwayTeam(fixtures.getString("lf.away_team"));
/*  871: 845 */         fixture.setAwayScore(fixtures.getString("lf.away_score"));
/*  872: 846 */         fixture.setStatus(fixtures.getInt("lf.status"));
/*  873: 847 */         fixture.setEventNotifSent(fixtures.getInt("lf.event_notif_sent"));
/*  874:     */         
/*  875: 849 */         String SQL = "SELECT * FROM livescore_game_alias where game_id='" + fixture.getGameId() + "'";
/*  876: 850 */         prepstat = con.prepareStatement(SQL);
/*  877: 851 */         rs = prepstat.executeQuery();
/*  878: 852 */         while (rs.next()) {
/*  879: 853 */           fixture.setAlias(rs.getString("alias"));
/*  880:     */         }
/*  881: 856 */         games.add(fixture);
/*  882:     */       }
/*  883:     */     }
/*  884:     */     catch (Exception ex)
/*  885:     */     {
/*  886: 859 */       if (con != null) {
/*  887: 860 */         con.close();
/*  888:     */       }
/*  889: 862 */       throw new Exception(ex.getMessage());
/*  890:     */     }
/*  891: 864 */     if (con != null) {
/*  892: 865 */       con.close();
/*  893:     */     }
/*  894: 867 */     return games;
/*  895:     */   }
/*  896:     */   
/*  897:     */   public static ArrayList viewAllActiveFixturesForCPInLeague(String date, String keyword, String accountId)
/*  898:     */     throws Exception
/*  899:     */   {
/*  900: 870 */     ArrayList games = new ArrayList();
/*  901:     */     
/*  902:     */ 
/*  903:     */ 
/*  904: 874 */     ResultSet fixtures = null;
/*  905: 875 */     ResultSet rs = null;
/*  906: 876 */     Connection con = null;
/*  907: 877 */     PreparedStatement prepstat = null;
/*  908:     */     try
/*  909:     */     {
/*  910: 880 */       con = DConnect.getConnection();
/*  911:     */       
/*  912: 882 */       SimpleDateFormat s_formatter = new SimpleDateFormat();
/*  913: 883 */       java.util.Date validDate = null;
/*  914:     */       try
/*  915:     */       {
/*  916: 885 */         validDate = s_formatter.parse(date);
/*  917:     */       }
/*  918:     */       catch (Exception ex)
/*  919:     */       {
/*  920: 887 */         validDate = Calendar.getInstance().getTime();
/*  921:     */       }
/*  922: 890 */       Calendar c = Calendar.getInstance();
/*  923: 891 */       c.setTime(validDate);
/*  924: 892 */       c.add(11, 24);
/*  925: 893 */       java.util.Date endDate = c.getTime();
/*  926:     */       
/*  927:     */ 
/*  928: 896 */       String query = "select * from livescore_fixture_mgt lfm inner join livescore_fixtures lf on lf.game_id=lfm.game_id inner join keyword_mapping km on lf.league_id=km.mapping and lfm.account_id=km.account_id inner join service_definition sd on km.account_id=sd.account_id and km.keyword=sd.keyword where sd.keyword='" + keyword + "' and sd.account_id='" + accountId + "' and" + " lf.status in ('" + 2 + "', '" + 0 + "') and  (lf.date like '" + date + "%' or lf.date >='" + new Timestamp(validDate.getTime()).toString() + "' and lf.date <='" + new Timestamp(endDate.getTime()).toString() + "')";
/*  929:     */       
/*  930:     */ 
/*  931:     */ 
/*  932:     */ 
/*  933:     */ 
/*  934: 902 */       prepstat = con.prepareStatement(query);
/*  935:     */       
/*  936:     */ 
/*  937: 905 */       fixtures = prepstat.executeQuery();
/*  938: 907 */       while (fixtures.next())
/*  939:     */       {
/*  940: 908 */         LiveScoreFixture fixture = new LiveScoreFixture();
/*  941:     */         
/*  942: 910 */         fixture.setCountryName(fixtures.getString("lf.country_name"));
/*  943: 911 */         fixture.setDate(fixtures.getTimestamp("lf.date").toString());
/*  944: 912 */         fixture.setGameId(fixtures.getString("lf.game_id"));
/*  945: 913 */         fixture.setLeagueId(fixtures.getString("lf.league_id"));
/*  946: 914 */         fixture.setLeagueName(fixtures.getString("lf.league_name"));
/*  947: 915 */         fixture.setHomeTeam(fixtures.getString("lf.home_team"));
/*  948: 916 */         fixture.setHomeScore(fixtures.getString("lf.home_score"));
/*  949: 917 */         fixture.setAwayTeam(fixtures.getString("lf.away_team"));
/*  950: 918 */         fixture.setAwayScore(fixtures.getString("lf.away_score"));
/*  951: 919 */         fixture.setStatus(fixtures.getInt("lf.status"));
/*  952: 920 */         fixture.setEventNotifSent(fixtures.getInt("lf.event_notif_sent"));
/*  953:     */         
/*  954: 922 */         String SQL = "SELECT * FROM livescore_game_alias where game_id='" + fixture.getGameId() + "'";
/*  955: 923 */         prepstat = con.prepareStatement(SQL);
/*  956: 924 */         rs = prepstat.executeQuery();
/*  957: 925 */         while (rs.next()) {
/*  958: 926 */           fixture.setAlias(rs.getString("alias"));
/*  959:     */         }
/*  960: 929 */         games.add(fixture);
/*  961:     */       }
/*  962:     */     }
/*  963:     */     catch (Exception ex)
/*  964:     */     {
/*  965: 932 */       if (con != null) {
/*  966: 933 */         con.close();
/*  967:     */       }
/*  968: 935 */       throw new Exception(ex.getMessage());
/*  969:     */     }
/*  970: 937 */     if (con != null) {
/*  971: 938 */       con.close();
/*  972:     */     }
/*  973: 940 */     return games;
/*  974:     */   }
/*  975:     */   
/*  976:     */   public static ArrayList viewAllActiveFixturesInAllLeagues(String date)
/*  977:     */     throws Exception
/*  978:     */   {
/*  979: 944 */     ArrayList games = new ArrayList();
/*  980:     */     
/*  981:     */ 
/*  982:     */ 
/*  983: 948 */     ResultSet fixtures = null;
/*  984: 949 */     ResultSet rs = null;
/*  985: 950 */     Connection con = null;
/*  986: 951 */     PreparedStatement prepstat = null;
/*  987:     */     try
/*  988:     */     {
/*  989: 954 */       con = DConnect.getConnection();
/*  990:     */       
/*  991: 956 */       SimpleDateFormat s_formatter = new SimpleDateFormat();
/*  992: 957 */       java.util.Date validDate = null;
/*  993:     */       try
/*  994:     */       {
/*  995: 959 */         validDate = s_formatter.parse(date);
/*  996:     */       }
/*  997:     */       catch (Exception ex)
/*  998:     */       {
/*  999: 961 */         validDate = Calendar.getInstance().getTime();
/* 1000:     */       }
/* 1001: 964 */       Calendar c = Calendar.getInstance();
/* 1002: 965 */       c.setTime(validDate);
/* 1003: 966 */       c.add(11, 24);
/* 1004: 967 */       java.util.Date endDate = c.getTime();
/* 1005:     */       
/* 1006:     */ 
/* 1007: 970 */       String query = "select * from livescore_fixtures lf where lf.status='0' and  (lf.date like '" + date + "%' or lf.date >='" + new Timestamp(validDate.getTime()).toString() + "' and lf.date <='" + new Timestamp(endDate.getTime()).toString() + "')";
/* 1008:     */       
/* 1009: 972 */       prepstat = con.prepareStatement(query);
/* 1010:     */       
/* 1011:     */ 
/* 1012: 975 */       fixtures = prepstat.executeQuery();
/* 1013: 977 */       while (fixtures.next())
/* 1014:     */       {
/* 1015: 978 */         LiveScoreFixture fixture = new LiveScoreFixture();
/* 1016:     */         
/* 1017: 980 */         fixture.setCountryName(fixtures.getString("lf.country_name"));
/* 1018: 981 */         fixture.setDate(fixtures.getTimestamp("lf.date").toString());
/* 1019: 982 */         fixture.setGameId(fixtures.getString("lf.game_id"));
/* 1020: 983 */         fixture.setLeagueId(fixtures.getString("lf.league_id"));
/* 1021: 984 */         fixture.setLeagueName(fixtures.getString("lf.league_name"));
/* 1022: 985 */         fixture.setHomeTeam(fixtures.getString("lf.home_team"));
/* 1023: 986 */         fixture.setHomeScore(fixtures.getString("lf.home_score"));
/* 1024: 987 */         fixture.setAwayTeam(fixtures.getString("lf.away_team"));
/* 1025: 988 */         fixture.setAwayScore(fixtures.getString("lf.away_score"));
/* 1026: 989 */         fixture.setStatus(fixtures.getInt("lf.status"));
/* 1027: 990 */         fixture.setEventNotifSent(fixtures.getInt("lf.event_notif_sent"));
/* 1028:     */         
/* 1029: 992 */         String SQL = "SELECT * FROM livescore_game_alias where game_id='" + fixture.getGameId() + "'";
/* 1030: 993 */         prepstat = con.prepareStatement(SQL);
/* 1031: 994 */         rs = prepstat.executeQuery();
/* 1032: 995 */         while (rs.next()) {
/* 1033: 996 */           fixture.setAlias(rs.getString("alias"));
/* 1034:     */         }
/* 1035: 999 */         games.add(fixture);
/* 1036:     */       }
/* 1037:     */     }
/* 1038:     */     catch (Exception ex)
/* 1039:     */     {
/* 1040:1002 */       if (con != null) {
/* 1041:1003 */         con.close();
/* 1042:     */       }
/* 1043:1005 */       throw new Exception(ex.getMessage());
/* 1044:     */     }
/* 1045:1007 */     if (con != null) {
/* 1046:1008 */       con.close();
/* 1047:     */     }
/* 1048:1010 */     return games;
/* 1049:     */   }
/* 1050:     */   
/* 1051:     */   public static ArrayList viewFixtures(String keyword, String accountId, String date)
/* 1052:     */     throws Exception
/* 1053:     */   {
/* 1054:1015 */     ArrayList games = new ArrayList();
/* 1055:     */     
/* 1056:     */ 
/* 1057:     */ 
/* 1058:1019 */     ResultSet fixtures = null;
/* 1059:1020 */     ResultSet rs = null;
/* 1060:1021 */     Connection con = null;
/* 1061:1022 */     PreparedStatement prepstat = null;
/* 1062:     */     try
/* 1063:     */     {
/* 1064:1025 */       con = DConnect.getConnection();
/* 1065:     */       
/* 1066:1027 */       SimpleDateFormat s_formatter = new SimpleDateFormat("MM-dd-yyyy");
/* 1067:1028 */       java.util.Date validDate = null;
/* 1068:     */       try
/* 1069:     */       {
/* 1070:1030 */         validDate = s_formatter.parse(date);
/* 1071:     */       }
/* 1072:     */       catch (Exception ex)
/* 1073:     */       {
/* 1074:1032 */         validDate = Calendar.getInstance().getTime();
/* 1075:     */       }
/* 1076:1044 */       String query = "select * from livescore_fixtures lf inner join livescore_services ls on lf.league_id=ls.league_id inner join keyword_mapping km on km.mapping=ls.league_id where km.account_id=? and date(lf.date)= ?";
/* 1077:1047 */       if (!"".equals(keyword))
/* 1078:     */       {
/* 1079:1048 */         query = query + " and km.keyword = '" + keyword + "'";
/* 1080:1049 */         query = query + " order by lf.game_id";
/* 1081:     */       }
/* 1082:     */       else
/* 1083:     */       {
/* 1084:1051 */         query = query + " order by lf.league_id";
/* 1085:     */       }
/* 1086:1053 */       prepstat = con.prepareStatement(query);
/* 1087:1054 */       prepstat.setString(1, accountId);
/* 1088:1055 */       prepstat.setDate(2, new java.sql.Date(validDate.getTime()));
/* 1089:     */       
/* 1090:     */ 
/* 1091:1058 */       fixtures = prepstat.executeQuery();
/* 1092:1060 */       while (fixtures.next())
/* 1093:     */       {
/* 1094:1061 */         LiveScoreFixture fixture = new LiveScoreFixture();
/* 1095:     */         
/* 1096:1063 */         fixture.setCountryName(fixtures.getString("lf.country_name"));
/* 1097:1064 */         fixture.setDate(fixtures.getTimestamp("lf.date").toString());
/* 1098:1065 */         fixture.setGameId(fixtures.getString("lf.game_id"));
/* 1099:1066 */         fixture.setLeagueId(fixtures.getString("lf.league_id"));
/* 1100:1067 */         fixture.setLeagueName(fixtures.getString("lf.league_name"));
/* 1101:1068 */         fixture.setHomeTeam(fixtures.getString("lf.home_team"));
/* 1102:1069 */         fixture.setHomeScore(fixtures.getString("lf.home_score"));
/* 1103:1070 */         fixture.setAwayTeam(fixtures.getString("lf.away_team"));
/* 1104:1071 */         fixture.setAwayScore(fixtures.getString("lf.away_score"));
/* 1105:1072 */         fixture.setStatus(fixtures.getInt("lf.status"));
/* 1106:1073 */         fixture.setEventNotifSent(fixtures.getInt("lf.event_notif_sent"));
/* 1107:     */         
/* 1108:1075 */         String SQL = "SELECT * FROM livescore_game_alias where game_id='" + fixture.getGameId() + "'";
/* 1109:1076 */         prepstat = con.prepareStatement(SQL);
/* 1110:1077 */         rs = prepstat.executeQuery();
/* 1111:1078 */         while (rs.next()) {
/* 1112:1079 */           fixture.setAlias(rs.getString("alias"));
/* 1113:     */         }
/* 1114:1082 */         games.add(fixture);
/* 1115:     */       }
/* 1116:     */     }
/* 1117:     */     catch (Exception ex)
/* 1118:     */     {
/* 1119:1085 */       if (con != null) {
/* 1120:1086 */         con.close();
/* 1121:     */       }
/* 1122:1088 */       throw new Exception(ex.getMessage());
/* 1123:     */     }
/* 1124:1090 */     if (con != null) {
/* 1125:1091 */       con.close();
/* 1126:     */     }
/* 1127:1093 */     return games;
/* 1128:     */   }
/* 1129:     */   
/* 1130:     */   public static ArrayList viewAllActiveFixturesInLeague(String date, String keyword, String accountId, String team)
/* 1131:     */     throws Exception
/* 1132:     */   {
/* 1133:1221 */     ArrayList games = new ArrayList();
/* 1134:     */     
/* 1135:     */ 
/* 1136:     */ 
/* 1137:1225 */     ResultSet fixtures = null;
/* 1138:1226 */     ResultSet rs = null;
/* 1139:1227 */     Connection con = null;
/* 1140:1228 */     PreparedStatement prepstat = null;
/* 1141:     */     try
/* 1142:     */     {
/* 1143:1231 */       con = DConnect.getConnection();
/* 1144:     */       
/* 1145:1233 */       SimpleDateFormat s_formatter = new SimpleDateFormat();
/* 1146:1234 */       java.util.Date validDate = null;
/* 1147:     */       try
/* 1148:     */       {
/* 1149:1236 */         validDate = s_formatter.parse(date);
/* 1150:     */       }
/* 1151:     */       catch (Exception ex)
/* 1152:     */       {
/* 1153:1238 */         validDate = Calendar.getInstance().getTime();
/* 1154:     */       }
/* 1155:1241 */       Calendar c = Calendar.getInstance();
/* 1156:1242 */       c.setTime(validDate);
/* 1157:1243 */       c.add(11, 24);
/* 1158:1244 */       java.util.Date endDate = c.getTime();
/* 1159:     */       
/* 1160:     */ 
/* 1161:1247 */       String query = "select * from livescore_fixtures lf inner join keyword_mapping km on lf.league_id=km.mapping inner join service_definition sd on km.account_id=sd.account_id and km.keyword=sd.keyword where sd.keyword='" + keyword + "' and sd.account_id='" + accountId + "' and lf.status in ('" + 2 + "', '" + 0 + "') and (lf.date like '" + date + "%' or lf.date >='" + new Timestamp(validDate.getTime()).toString() + "' and lf.date <='" + new Timestamp(endDate.getTime()).toString() + "') and (lf.home_team like '%" + team + "%' or away_team like '%" + team + "%')";
/* 1162:     */       
/* 1163:     */ 
/* 1164:     */ 
/* 1165:     */ 
/* 1166:1252 */       prepstat = con.prepareStatement(query);
/* 1167:1253 */       fixtures = prepstat.executeQuery();
/* 1168:1255 */       while (fixtures.next())
/* 1169:     */       {
/* 1170:1256 */         LiveScoreFixture fixture = new LiveScoreFixture();
/* 1171:     */         
/* 1172:1258 */         fixture.setCountryName(fixtures.getString("lf.country_name"));
/* 1173:1259 */         fixture.setDate(fixtures.getTimestamp("lf.date").toString());
/* 1174:1260 */         fixture.setGameId(fixtures.getString("lf.game_id"));
/* 1175:1261 */         fixture.setLeagueId(fixtures.getString("lf.league_id"));
/* 1176:1262 */         fixture.setLeagueName(fixtures.getString("lf.league_name"));
/* 1177:1263 */         fixture.setHomeTeam(fixtures.getString("lf.home_team"));
/* 1178:1264 */         fixture.setHomeScore(fixtures.getString("lf.home_score"));
/* 1179:1265 */         fixture.setAwayTeam(fixtures.getString("lf.away_team"));
/* 1180:1266 */         fixture.setAwayScore(fixtures.getString("lf.away_score"));
/* 1181:1267 */         fixture.setStatus(fixtures.getInt("lf.status"));
/* 1182:1268 */         fixture.setEventNotifSent(fixtures.getInt("lf.event_notif_sent"));
/* 1183:     */         
/* 1184:1270 */         String SQL = "SELECT * FROM livescore_game_alias where game_id='" + fixture.getGameId() + "'";
/* 1185:1271 */         prepstat = con.prepareStatement(SQL);
/* 1186:1272 */         rs = prepstat.executeQuery();
/* 1187:1273 */         while (rs.next()) {
/* 1188:1274 */           fixture.setAlias(rs.getString("alias"));
/* 1189:     */         }
/* 1190:1277 */         games.add(fixture);
/* 1191:     */       }
/* 1192:     */     }
/* 1193:     */     catch (Exception ex)
/* 1194:     */     {
/* 1195:1280 */       if (con != null) {
/* 1196:1281 */         con.close();
/* 1197:     */       }
/* 1198:1283 */       throw new Exception(ex.getMessage());
/* 1199:     */     }
/* 1200:1285 */     if (con != null) {
/* 1201:1286 */       con.close();
/* 1202:     */     }
/* 1203:1288 */     return games;
/* 1204:     */   }
/* 1205:     */   
/* 1206:     */   public static ArrayList viewAllActiveFixturesForCPInLeague(String date, String keyword, String accountId, String team)
/* 1207:     */     throws Exception
/* 1208:     */   {
/* 1209:1291 */     ArrayList games = new ArrayList();
/* 1210:     */     
/* 1211:     */ 
/* 1212:     */ 
/* 1213:1295 */     ResultSet fixtures = null;
/* 1214:1296 */     ResultSet rs = null;
/* 1215:1297 */     Connection con = null;
/* 1216:1298 */     PreparedStatement prepstat = null;
/* 1217:     */     try
/* 1218:     */     {
/* 1219:1301 */       con = DConnect.getConnection();
/* 1220:     */       
/* 1221:1303 */       SimpleDateFormat s_formatter = new SimpleDateFormat();
/* 1222:1304 */       java.util.Date validDate = null;
/* 1223:     */       try
/* 1224:     */       {
/* 1225:1306 */         validDate = s_formatter.parse(date);
/* 1226:     */       }
/* 1227:     */       catch (Exception ex)
/* 1228:     */       {
/* 1229:1308 */         validDate = Calendar.getInstance().getTime();
/* 1230:     */       }
/* 1231:1311 */       Calendar c = Calendar.getInstance();
/* 1232:1312 */       c.setTime(validDate);
/* 1233:1313 */       c.add(11, 24);
/* 1234:1314 */       java.util.Date endDate = c.getTime();
/* 1235:     */       
/* 1236:     */ 
/* 1237:1317 */       String query = "select * from livescore_fixture_mgt lfm inner join livescore_fixtures lf on lf.game_id=lfm.game_id inner join keyword_mapping km on lf.league_id=km.mapping and lfm.account_id=km.account_id inner join service_definition sd on km.account_id=sd.account_id and km.keyword=sd.keyword where sd.keyword='" + keyword + "' and sd.account_id='" + accountId + "' and lf.status in ('" + 2 + "', '" + 0 + "') and (lf.date like '" + date + "%' or lf.date >='" + new Timestamp(validDate.getTime()).toString() + "' and lf.date <='" + new Timestamp(endDate.getTime()).toString() + "') and (lf.home_team like '%" + team + "%' or away_team like '%" + team + "%')";
/* 1238:     */       
/* 1239:     */ 
/* 1240:     */ 
/* 1241:     */ 
/* 1242:     */ 
/* 1243:1323 */       prepstat = con.prepareStatement(query);
/* 1244:1324 */       fixtures = prepstat.executeQuery();
/* 1245:1326 */       while (fixtures.next())
/* 1246:     */       {
/* 1247:1327 */         LiveScoreFixture fixture = new LiveScoreFixture();
/* 1248:     */         
/* 1249:1329 */         fixture.setCountryName(fixtures.getString("lf.country_name"));
/* 1250:1330 */         fixture.setDate(fixtures.getTimestamp("lf.date").toString());
/* 1251:1331 */         fixture.setGameId(fixtures.getString("lf.game_id"));
/* 1252:1332 */         fixture.setLeagueId(fixtures.getString("lf.league_id"));
/* 1253:1333 */         fixture.setLeagueName(fixtures.getString("lf.league_name"));
/* 1254:1334 */         fixture.setHomeTeam(fixtures.getString("lf.home_team"));
/* 1255:1335 */         fixture.setHomeScore(fixtures.getString("lf.home_score"));
/* 1256:1336 */         fixture.setAwayTeam(fixtures.getString("lf.away_team"));
/* 1257:1337 */         fixture.setAwayScore(fixtures.getString("lf.away_score"));
/* 1258:1338 */         fixture.setStatus(fixtures.getInt("lf.status"));
/* 1259:1339 */         fixture.setEventNotifSent(fixtures.getInt("lf.event_notif_sent"));
/* 1260:     */         
/* 1261:1341 */         String SQL = "SELECT * FROM livescore_game_alias where game_id='" + fixture.getGameId() + "'";
/* 1262:1342 */         prepstat = con.prepareStatement(SQL);
/* 1263:1343 */         rs = prepstat.executeQuery();
/* 1264:1344 */         while (rs.next()) {
/* 1265:1345 */           fixture.setAlias(rs.getString("alias"));
/* 1266:     */         }
/* 1267:1348 */         games.add(fixture);
/* 1268:     */       }
/* 1269:     */     }
/* 1270:     */     catch (Exception ex)
/* 1271:     */     {
/* 1272:1351 */       if (con != null) {
/* 1273:1352 */         con.close();
/* 1274:     */       }
/* 1275:1354 */       throw new Exception(ex.getMessage());
/* 1276:     */     }
/* 1277:1356 */     if (con != null) {
/* 1278:1357 */       con.close();
/* 1279:     */     }
/* 1280:1359 */     return games;
/* 1281:     */   }
/* 1282:     */   
/* 1283:     */   public static ArrayList viewAllFixtures()
/* 1284:     */     throws Exception
/* 1285:     */   {
/* 1286:1364 */     ArrayList fixtures = new ArrayList();
/* 1287:     */     
/* 1288:     */ 
/* 1289:1367 */     ResultSet rs = null;
/* 1290:1368 */     ResultSet rs2 = null;
/* 1291:1369 */     Connection con = null;
/* 1292:1370 */     PreparedStatement prepstat2 = null;
/* 1293:     */     try
/* 1294:     */     {
/* 1295:1373 */       con = DConnect.getConnection();
/* 1296:     */       
/* 1297:1375 */       String SQL = "SELECT * FROM livescore_fixtures";
/* 1298:     */       
/* 1299:1377 */       PreparedStatement prepstat = con.prepareStatement(SQL);
/* 1300:1378 */       rs = prepstat.executeQuery();
/* 1301:1380 */       while (rs.next())
/* 1302:     */       {
/* 1303:1381 */         LiveScoreFixture fixture = new LiveScoreFixture();
/* 1304:     */         
/* 1305:1383 */         fixture.setCountryName(rs.getString("country_name"));
/* 1306:1384 */         fixture.setDate(rs.getTimestamp("date").toString());
/* 1307:1385 */         fixture.setGameId(rs.getString("game_id"));
/* 1308:1386 */         fixture.setLeagueId(rs.getString("league_id"));
/* 1309:1387 */         fixture.setLeagueName(rs.getString("league_name"));
/* 1310:1388 */         fixture.setHomeTeam(rs.getString("home_team"));
/* 1311:1389 */         fixture.setHomeScore(rs.getString("home_score"));
/* 1312:1390 */         fixture.setAwayTeam(rs.getString("away_team"));
/* 1313:1391 */         fixture.setAwayScore(rs.getString("away_score"));
/* 1314:1392 */         fixture.setStatus(rs.getInt("status"));
/* 1315:1393 */         fixture.setEventNotifSent(rs.getInt("event_notif_sent"));
/* 1316:     */         
/* 1317:1395 */         SQL = "SELECT * FROM livescore_game_alias where game_id='" + fixture.getGameId() + "'";
/* 1318:1396 */         prepstat = con.prepareStatement(SQL);
/* 1319:1397 */         rs2 = prepstat.executeQuery();
/* 1320:1398 */         while (rs2.next()) {
/* 1321:1399 */           fixture.setAlias(rs2.getString("alias"));
/* 1322:     */         }
/* 1323:1402 */         fixtures.add(fixture);
/* 1324:     */       }
/* 1325:     */     }
/* 1326:     */     catch (Exception ex)
/* 1327:     */     {
/* 1328:1406 */       if (con != null) {
/* 1329:1407 */         con.close();
/* 1330:     */       }
/* 1331:1409 */       throw new Exception(ex.getMessage());
/* 1332:     */     }
/* 1333:1411 */     if (con != null) {
/* 1334:1412 */       con.close();
/* 1335:     */     }
/* 1336:1415 */     return fixtures;
/* 1337:     */   }
/* 1338:     */   
/* 1339:     */   public static void deleteFixture(String keyword)
/* 1340:     */     throws Exception
/* 1341:     */   {
/* 1342:1421 */     ResultSet rs = null;
/* 1343:1422 */     Connection con = null;
/* 1344:1423 */     PreparedStatement prepstat = null;
/* 1345:     */     try
/* 1346:     */     {
/* 1347:1426 */       con = DConnect.getConnection();
/* 1348:     */       
/* 1349:     */ 
/* 1350:1429 */       String SQL = "delete from livescore_fixtures where game_id='" + keyword + "'";
/* 1351:1430 */       prepstat = con.prepareStatement(SQL);
/* 1352:1431 */       prepstat.execute();
/* 1353:     */     }
/* 1354:     */     catch (Exception ex)
/* 1355:     */     {
/* 1356:1433 */       if (con != null) {
/* 1357:1434 */         con.close();
/* 1358:     */       }
/* 1359:1436 */       throw new Exception(ex.getMessage());
/* 1360:     */     }
/* 1361:     */     finally
/* 1362:     */     {
/* 1363:1439 */       if (con != null) {
/* 1364:1440 */         con.close();
/* 1365:     */       }
/* 1366:     */     }
/* 1367:     */   }
/* 1368:     */   
/* 1369:     */   public static synchronized void subscribeForGame(String accountId, String gameId, String msisdn)
/* 1370:     */     throws Exception
/* 1371:     */   {
/* 1372:1447 */     String regId = "";
/* 1373:1448 */     int error = 0;
/* 1374:1449 */     ResultSet rs = null;
/* 1375:1450 */     Connection con = null;
/* 1376:1451 */     PreparedStatement prepstat = null;
/* 1377:     */     try
/* 1378:     */     {
/* 1379:1454 */       con = DConnect.getConnection();
/* 1380:     */       
/* 1381:     */ 
/* 1382:1457 */       String SQL = "select * from address_book where account_id='" + accountId + "' and msisdn='" + msisdn + "'";
/* 1383:1458 */       prepstat = con.prepareStatement(SQL);
/* 1384:1459 */       rs = prepstat.executeQuery();
/* 1385:1461 */       if (!rs.next())
/* 1386:     */       {
/* 1387:1464 */         SQL = "Insert into address_book (account_id,msisdn,registration_id) values(?,?,?)";
/* 1388:1465 */         prepstat = con.prepareStatement(SQL);
/* 1389:1466 */         regId = uidGen.generateNumberID(6);
/* 1390:     */         
/* 1391:1468 */         prepstat.setString(1, accountId);
/* 1392:1469 */         prepstat.setString(2, msisdn);
/* 1393:1470 */         prepstat.setString(3, regId);
/* 1394:1471 */         prepstat.execute();
/* 1395:     */       }
/* 1396:1474 */       SQL = "select * from service_subscription where keyword='" + gameId + "' and account_id='" + accountId + "' and msisdn='" + msisdn + "'";
/* 1397:1475 */       prepstat = con.prepareStatement(SQL);
/* 1398:1476 */       rs = prepstat.executeQuery();
/* 1399:1478 */       if (!rs.next())
/* 1400:     */       {
/* 1401:1479 */         SQL = "Insert into service_subscription (subscription_date,msisdn,keyword,account_id,status) values(?,?,?,?,?)";
/* 1402:1480 */         prepstat = con.prepareStatement(SQL);
/* 1403:1481 */         prepstat.setTimestamp(1, new Timestamp(Calendar.getInstance().getTime().getTime()));
/* 1404:1482 */         prepstat.setString(2, msisdn);
/* 1405:1483 */         prepstat.setString(3, gameId);
/* 1406:1484 */         prepstat.setString(4, accountId);
/* 1407:1485 */         prepstat.setInt(5, 1);
/* 1408:1486 */         prepstat.execute();
/* 1409:     */         
/* 1410:     */ 
/* 1411:1489 */         SQL = "select no_subscribers from livescore_fixture_mgt where account_id=? and game_id=?";
/* 1412:1490 */         prepstat = con.prepareStatement(SQL);
/* 1413:1491 */         prepstat.setString(1, accountId);
/* 1414:1492 */         prepstat.setString(2, gameId);
/* 1415:1493 */         rs = prepstat.executeQuery();
/* 1416:     */         
/* 1417:1495 */         int no_subs = 1;
/* 1418:1496 */         if (rs.next())
/* 1419:     */         {
/* 1420:1497 */           no_subs += rs.getInt("no_subscribers");
/* 1421:1498 */           SQL = "update livescore_fixture_mgt set no_subscribers = ? where account_id=? and game_id=?";
/* 1422:     */         }
/* 1423:     */         else
/* 1424:     */         {
/* 1425:1500 */           SQL = "insert into livescore_fixture_mgt(no_subscribers,account_id,game_id) values(?,?,?)";
/* 1426:     */         }
/* 1427:1503 */         prepstat = con.prepareStatement(SQL);
/* 1428:1504 */         prepstat.setInt(1, no_subs);
/* 1429:1505 */         prepstat.setString(2, accountId);
/* 1430:1506 */         prepstat.setString(3, gameId);
/* 1431:1507 */         prepstat.executeUpdate();
/* 1432:     */       }
/* 1433:     */     }
/* 1434:     */     catch (Exception ex)
/* 1435:     */     {
/* 1436:1510 */       if (con != null)
/* 1437:     */       {
/* 1438:     */         try
/* 1439:     */         {
/* 1440:1512 */           con.close();
/* 1441:     */         }
/* 1442:     */         catch (SQLException ex1)
/* 1443:     */         {
/* 1444:1514 */           System.out.println(ex1.getMessage());
/* 1445:     */         }
/* 1446:1516 */         con = null;
/* 1447:     */       }
/* 1448:     */     }
/* 1449:     */     finally
/* 1450:     */     {
/* 1451:1519 */       if (rs != null)
/* 1452:     */       {
/* 1453:     */         try
/* 1454:     */         {
/* 1455:1521 */           rs.close();
/* 1456:     */         }
/* 1457:     */         catch (SQLException e) {}
/* 1458:1523 */         rs = null;
/* 1459:     */       }
/* 1460:1525 */       if (prepstat != null)
/* 1461:     */       {
/* 1462:     */         try
/* 1463:     */         {
/* 1464:1527 */           prepstat.close();
/* 1465:     */         }
/* 1466:     */         catch (SQLException e) {}
/* 1467:1529 */         prepstat = null;
/* 1468:     */       }
/* 1469:1531 */       if (con != null)
/* 1470:     */       {
/* 1471:     */         try
/* 1472:     */         {
/* 1473:1533 */           con.close();
/* 1474:     */         }
/* 1475:     */         catch (SQLException e) {}
/* 1476:1535 */         con = null;
/* 1477:     */       }
/* 1478:     */     }
/* 1479:     */   }
/* 1480:     */   
/* 1481:     */   public static synchronized void unsubscribeFromGame(String msisdn, String gameId, String accountId)
/* 1482:     */     throws Exception
/* 1483:     */   {
/* 1484:1542 */     ResultSet rs = null;
/* 1485:1543 */     Connection con = null;
/* 1486:1544 */     PreparedStatement prepstat = null;
/* 1487:     */     try
/* 1488:     */     {
/* 1489:1547 */       con = DConnect.getConnection();
/* 1490:1548 */       String SQL = "delete from service_subscription where msisdn='" + msisdn + "' and keyword='" + gameId + "' and account_id='" + accountId + "'";
/* 1491:1549 */       prepstat = con.prepareStatement(SQL);
/* 1492:1550 */       prepstat.execute();
/* 1493:     */       
/* 1494:     */ 
/* 1495:1553 */       SQL = "select no_subscribers from livescore_fixture_mgt where account_id=? and game_id=?";
/* 1496:1554 */       prepstat = con.prepareStatement(SQL);
/* 1497:1555 */       prepstat.setString(1, accountId);
/* 1498:1556 */       prepstat.setString(2, gameId);
/* 1499:1557 */       rs = prepstat.executeQuery();
/* 1500:     */       
/* 1501:1559 */       int no_subs = 1;
/* 1502:1560 */       if (rs.next())
/* 1503:     */       {
/* 1504:1561 */         int count = rs.getInt("no_subscribers");
/* 1505:1562 */         if (count > 0) {
/* 1506:1563 */           no_subs = count - no_subs;
/* 1507:     */         } else {
/* 1508:1565 */           no_subs = 0;
/* 1509:     */         }
/* 1510:1568 */         SQL = "update livescore_fixture_mgt set no_subscribers = ? where account_id=? and game_id=?";
/* 1511:     */       }
/* 1512:     */       else
/* 1513:     */       {
/* 1514:1570 */         SQL = "insert into livescore_fixture_mgt(no_subscribers,account_id,game_id) values(?,?,?)";
/* 1515:1571 */         no_subs = 0;
/* 1516:     */       }
/* 1517:1574 */       prepstat = con.prepareStatement(SQL);
/* 1518:1575 */       prepstat.setInt(1, no_subs);
/* 1519:1576 */       prepstat.setString(2, accountId);
/* 1520:1577 */       prepstat.setString(3, gameId);
/* 1521:1578 */       prepstat.executeUpdate();
/* 1522:     */     }
/* 1523:     */     catch (Exception ex)
/* 1524:     */     {
/* 1525:1580 */       if (con != null)
/* 1526:     */       {
/* 1527:     */         try
/* 1528:     */         {
/* 1529:1582 */           con.close();
/* 1530:     */         }
/* 1531:     */         catch (SQLException ex1)
/* 1532:     */         {
/* 1533:1584 */           System.out.println(ex1.getMessage());
/* 1534:     */         }
/* 1535:1586 */         con = null;
/* 1536:     */       }
/* 1537:     */     }
/* 1538:     */     finally
/* 1539:     */     {
/* 1540:1589 */       if (rs != null)
/* 1541:     */       {
/* 1542:     */         try
/* 1543:     */         {
/* 1544:1591 */           rs.close();
/* 1545:     */         }
/* 1546:     */         catch (SQLException e) {}
/* 1547:1595 */         rs = null;
/* 1548:     */       }
/* 1549:1597 */       if (prepstat != null)
/* 1550:     */       {
/* 1551:     */         try
/* 1552:     */         {
/* 1553:1599 */           prepstat.close();
/* 1554:     */         }
/* 1555:     */         catch (SQLException e) {}
/* 1556:1603 */         prepstat = null;
/* 1557:     */       }
/* 1558:1605 */       if (con != null)
/* 1559:     */       {
/* 1560:     */         try
/* 1561:     */         {
/* 1562:1607 */           con.close();
/* 1563:     */         }
/* 1564:     */         catch (SQLException e) {}
/* 1565:1611 */         con = null;
/* 1566:     */       }
/* 1567:     */     }
/* 1568:     */   }
/* 1569:     */   
/* 1570:     */   public static void updateSubscriptionStatus(String gameId, int subStatus, int gameStatus)
/* 1571:     */     throws Exception
/* 1572:     */   {
/* 1573:1618 */     ResultSet rs = null;
/* 1574:1619 */     Connection con = null;
/* 1575:1620 */     PreparedStatement prepstat = null;
/* 1576:1621 */     boolean bError = false;
/* 1577:     */     try
/* 1578:     */     {
/* 1579:1624 */       con = DConnect.getConnection();
/* 1580:1625 */       con.setAutoCommit(false);
/* 1581:     */       
/* 1582:     */ 
/* 1583:1628 */       String SQL = "update service_subscription set status=? where keyword=? and account_id in(select account_id from keyword_mapping where mapping in(select league_id from livescore_fixtures where game_id=? and status=?) )";
/* 1584:     */       
/* 1585:     */ 
/* 1586:1631 */       prepstat = con.prepareStatement(SQL);
/* 1587:     */       
/* 1588:     */ 
/* 1589:1634 */       prepstat.setString(1, "" + subStatus);
/* 1590:1635 */       prepstat.setString(2, gameId);
/* 1591:1636 */       prepstat.setString(3, gameId);
/* 1592:1637 */       prepstat.setString(4, "" + gameStatus);
/* 1593:     */       
/* 1594:1639 */       prepstat.execute();
/* 1595:     */     }
/* 1596:     */     catch (Exception ex)
/* 1597:     */     {
/* 1598:1641 */       if (con != null) {
/* 1599:1642 */         con.close();
/* 1600:     */       }
/* 1601:1644 */       bError = true;
/* 1602:1645 */       throw new Exception(ex.getMessage());
/* 1603:     */     }
/* 1604:     */     finally
/* 1605:     */     {
/* 1606:1648 */       if (con != null) {
/* 1607:1649 */         con.close();
/* 1608:     */       }
/* 1609:     */     }
/* 1610:     */   }
/* 1611:     */   
/* 1612:     */   public static ArrayList viewDistinctGameTimesForDate(java.util.Date date, int status)
/* 1613:     */     throws Exception
/* 1614:     */   {
/* 1615:1655 */     int DEFAULT_ALLOWANCE = 0;
/* 1616:1656 */     int OUTLOOK_PERIOD = 0;
/* 1617:     */     try
/* 1618:     */     {
/* 1619:1659 */       String value = PropertyHolder.getPropsValue("LS_TRIGGER_ALLOWANCE");
/* 1620:1660 */       if ((value != null) && (!value.equals(""))) {
/* 1621:1661 */         DEFAULT_ALLOWANCE = Integer.parseInt(value);
/* 1622:     */       } else {
/* 1623:1663 */         DEFAULT_ALLOWANCE = 4;
/* 1624:     */       }
/* 1625:     */     }
/* 1626:     */     catch (Exception e)
/* 1627:     */     {
/* 1628:1666 */       DEFAULT_ALLOWANCE = 4;
/* 1629:     */     }
/* 1630:     */     try
/* 1631:     */     {
/* 1632:1670 */       String value = PropertyHolder.getPropsValue("LS_GAME_OUTLOOK");
/* 1633:1671 */       if ((value != null) && (!value.equals(""))) {
/* 1634:1672 */         OUTLOOK_PERIOD = Integer.parseInt(value);
/* 1635:     */       } else {
/* 1636:1674 */         OUTLOOK_PERIOD = 24;
/* 1637:     */       }
/* 1638:     */     }
/* 1639:     */     catch (Exception e)
/* 1640:     */     {
/* 1641:1677 */       OUTLOOK_PERIOD = 24;
/* 1642:     */     }
/* 1643:1680 */     ArrayList dates = new ArrayList();
/* 1644:     */     
/* 1645:     */ 
/* 1646:1683 */     ResultSet rs_dates = null;
/* 1647:1684 */     Connection con = null;
/* 1648:1685 */     PreparedStatement prepstat = null;
/* 1649:     */     
/* 1650:1687 */     Calendar c = Calendar.getInstance();
/* 1651:1688 */     c.setTime(date);
/* 1652:1689 */     int addition = OUTLOOK_PERIOD + DEFAULT_ALLOWANCE;
/* 1653:1690 */     c.add(11, addition);
/* 1654:1691 */     java.util.Date endDate = c.getTime();
/* 1655:     */     try
/* 1656:     */     {
/* 1657:1694 */       con = DConnect.getConnection();
/* 1658:     */       
/* 1659:1696 */       String query = "select date from livescore_fixtures where livescore_fixtures.status='" + status + "' and  (date >= ? and date <= ?) group by date (date), hour (date)";
/* 1660:1697 */       prepstat = con.prepareStatement(query);
/* 1661:1698 */       prepstat.setTimestamp(1, new Timestamp(date.getTime()));
/* 1662:1699 */       prepstat.setTimestamp(2, new Timestamp(endDate.getTime()));
/* 1663:1700 */       rs_dates = prepstat.executeQuery();
/* 1664:1702 */       while (rs_dates.next())
/* 1665:     */       {
/* 1666:1703 */         LiveScoreFixture fixture = new LiveScoreFixture();
/* 1667:1704 */         String dateStr = rs_dates.getString("date");
/* 1668:1705 */         dates.add(dateStr);
/* 1669:     */       }
/* 1670:     */     }
/* 1671:     */     catch (Exception ex)
/* 1672:     */     {
/* 1673:1708 */       if (con != null) {
/* 1674:1709 */         con.close();
/* 1675:     */       }
/* 1676:1711 */       throw new Exception(ex.getMessage());
/* 1677:     */     }
/* 1678:1713 */     if (con != null) {
/* 1679:1714 */       con.close();
/* 1680:     */     }
/* 1681:1716 */     return dates;
/* 1682:     */   }
/* 1683:     */   
/* 1684:     */   public static String viewGameIdForAlias(String alias)
/* 1685:     */     throws Exception
/* 1686:     */   {
/* 1687:1720 */     String gameId = "";
/* 1688:     */     
/* 1689:     */ 
/* 1690:1723 */     ResultSet rs = null;
/* 1691:1724 */     Connection con = null;
/* 1692:1725 */     PreparedStatement prepstat = null;
/* 1693:     */     try
/* 1694:     */     {
/* 1695:1727 */       con = DConnect.getConnection();
/* 1696:     */       
/* 1697:1729 */       String SQL = "SELECT game_id FROM livescore_game_alias where alias='" + alias + "'";
/* 1698:     */       
/* 1699:1731 */       prepstat = con.prepareStatement(SQL);
/* 1700:1732 */       rs = prepstat.executeQuery();
/* 1701:1734 */       while (rs.next()) {
/* 1702:1735 */         gameId = rs.getString("game_id");
/* 1703:     */       }
/* 1704:     */     }
/* 1705:     */     catch (Exception ex)
/* 1706:     */     {
/* 1707:1738 */       if (con != null) {
/* 1708:1739 */         con.close();
/* 1709:     */       }
/* 1710:1741 */       throw new Exception(ex.getMessage());
/* 1711:     */     }
/* 1712:1743 */     if (con != null) {
/* 1713:1744 */       con.close();
/* 1714:     */     }
/* 1715:1747 */     return gameId;
/* 1716:     */   }
/* 1717:     */   
/* 1718:     */   public static String viewAliasForGameId(String gameId)
/* 1719:     */     throws Exception
/* 1720:     */   {
/* 1721:1751 */     String alias = "";
/* 1722:     */     
/* 1723:     */ 
/* 1724:1754 */     ResultSet rs = null;
/* 1725:1755 */     Connection con = null;
/* 1726:1756 */     PreparedStatement prepstat = null;
/* 1727:     */     try
/* 1728:     */     {
/* 1729:1758 */       con = DConnect.getConnection();
/* 1730:     */       
/* 1731:1760 */       String SQL = "SELECT alias FROM livescore_game_alias where game_id='" + gameId + "'";
/* 1732:     */       
/* 1733:1762 */       prepstat = con.prepareStatement(SQL);
/* 1734:1763 */       rs = prepstat.executeQuery();
/* 1735:1765 */       while (rs.next()) {
/* 1736:1766 */         alias = rs.getString("alias");
/* 1737:     */       }
/* 1738:     */     }
/* 1739:     */     catch (Exception ex)
/* 1740:     */     {
/* 1741:1769 */       if (con != null) {
/* 1742:1770 */         con.close();
/* 1743:     */       }
/* 1744:1772 */       throw new Exception(ex.getMessage());
/* 1745:     */     }
/* 1746:1774 */     if (con != null) {
/* 1747:1775 */       con.close();
/* 1748:     */     }
/* 1749:1778 */     return alias;
/* 1750:     */   }
/* 1751:     */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.livescore.LiveScoreFixtureDB
 * JD-Core Version:    0.7.0.1
 */