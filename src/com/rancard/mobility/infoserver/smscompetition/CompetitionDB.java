/*    1:     */ package com.rancard.mobility.infoserver.smscompetition;
/*    2:     */ 
/*    3:     */ import com.rancard.common.DConnect;
/*    4:     */ import com.rancard.mobility.infoserver.common.inbox.InboxEntry;
/*    5:     */ import java.sql.Connection;
/*    6:     */ import java.sql.PreparedStatement;
/*    7:     */ import java.sql.ResultSet;
/*    8:     */ import java.sql.Timestamp;
/*    9:     */ import java.util.ArrayList;
/*   10:     */ import java.util.Calendar;
/*   11:     */ import java.util.Date;
/*   12:     */ 
/*   13:     */ public abstract class CompetitionDB
/*   14:     */ {
/*   15:     */   public static boolean alreadyRegistered(String mobileno, String keyword, String accountid)
/*   16:     */     throws Exception
/*   17:     */   {
/*   18:  14 */     boolean registered = false;
/*   19:     */     
/*   20:  16 */     ResultSet rs = null;
/*   21:  17 */     Connection con = null;
/*   22:  18 */     PreparedStatement prepstat = null;
/*   23:     */     try
/*   24:     */     {
/*   25:  20 */       con = DConnect.getConnection();
/*   26:     */       
/*   27:  22 */       String SQL = "select * from inbox where mobileno=? and keyword=? and account_id=?";
/*   28:     */       
/*   29:     */ 
/*   30:     */ 
/*   31:  26 */       prepstat = con.prepareStatement(SQL);
/*   32:  27 */       prepstat.setString(1, mobileno);
/*   33:  28 */       prepstat.setString(2, keyword);
/*   34:  29 */       prepstat.setString(3, accountid);
/*   35:     */       
/*   36:  31 */       rs = prepstat.executeQuery();
/*   37:  32 */       while (rs.next()) {
/*   38:  33 */         registered = true;
/*   39:     */       }
/*   40:     */     }
/*   41:     */     catch (Exception ex)
/*   42:     */     {
/*   43:  36 */       if (con != null) {
/*   44:  37 */         con.close();
/*   45:     */       }
/*   46:  39 */       throw new Exception(ex.getMessage());
/*   47:     */     }
/*   48:  41 */     if (con != null) {
/*   49:  42 */       con.close();
/*   50:     */     }
/*   51:  44 */     return registered;
/*   52:     */   }
/*   53:     */   
/*   54:     */   public static boolean alreadyVoted(String mobileno, String keyword, String accountid)
/*   55:     */     throws Exception
/*   56:     */   {
/*   57:  50 */     boolean voted = false;
/*   58:     */     
/*   59:  52 */     ResultSet rs = null;
/*   60:  53 */     Connection con = null;
/*   61:  54 */     PreparedStatement prepstat = null;
/*   62:     */     try
/*   63:     */     {
/*   64:  56 */       con = DConnect.getConnection();
/*   65:     */       
/*   66:  58 */       String SQL = "select response from inbox where mobileno=? and keyword=? and account_id=?";
/*   67:     */       
/*   68:     */ 
/*   69:  61 */       prepstat = con.prepareStatement(SQL);
/*   70:     */       
/*   71:  63 */       prepstat.setString(1, mobileno);
/*   72:  64 */       prepstat.setString(2, keyword);
/*   73:  65 */       prepstat.setString(3, accountid);
/*   74:     */       
/*   75:  67 */       rs = prepstat.executeQuery();
/*   76:  68 */       while (rs.next()) {
/*   77:  69 */         if ((rs.getString("response") != null) && (!rs.getString("response").equals(""))) {
/*   78:  71 */           voted = true;
/*   79:     */         }
/*   80:     */       }
/*   81:     */     }
/*   82:     */     catch (Exception ex)
/*   83:     */     {
/*   84:  75 */       if (con != null) {
/*   85:  76 */         con.close();
/*   86:     */       }
/*   87:  78 */       throw new Exception(ex.getMessage());
/*   88:     */     }
/*   89:  80 */     if (con != null) {
/*   90:  81 */       con.close();
/*   91:     */     }
/*   92:  83 */     return voted;
/*   93:     */   }
/*   94:     */   
/*   95:     */   public static void createCompetition(Competition comp)
/*   96:     */     throws Exception
/*   97:     */   {
/*   98:  90 */     ResultSet rs = null;
/*   99:  91 */     ResultSet rs2 = null;
/*  100:  92 */     Connection con = null;
/*  101:  93 */     PreparedStatement prepstat = null;
/*  102:  94 */     boolean bError = false;
/*  103:  95 */     Calendar start = Calendar.getInstance();
/*  104:  96 */     Calendar end = Calendar.getInstance();
/*  105:     */     try
/*  106:     */     {
/*  107:  99 */       con = DConnect.getConnection();
/*  108: 100 */       con.setAutoCommit(false);
/*  109:     */       
/*  110: 102 */       start.setTimeInMillis(comp.getStartDate().getTime());
/*  111: 103 */       end.setTimeInMillis(comp.getEndDate().getTime());
/*  112: 105 */       if (end.before(start)) {
/*  113: 106 */         throw new Exception("END date cannot be before START date");
/*  114:     */       }
/*  115: 111 */       String SQL = "select * from service_definition where keyword=? and account_id=? and service_type=8";
/*  116: 112 */       prepstat = con.prepareStatement(SQL);
/*  117: 113 */       prepstat.setString(1, comp.getKeyword());
/*  118: 114 */       prepstat.setString(2, comp.getAccountId());
/*  119:     */       
/*  120: 116 */       rs = prepstat.executeQuery();
/*  121: 117 */       if (!rs.next())
/*  122:     */       {
/*  123: 118 */         SQL = "insert into service_definition(service_type,keyword,account_id,service_name,default_message) values(?,?,?,?,?)";
/*  124:     */         
/*  125: 120 */         prepstat = con.prepareStatement(SQL);
/*  126: 121 */         prepstat.setString(1, "8");
/*  127: 122 */         prepstat.setString(2, comp.getKeyword());
/*  128: 123 */         prepstat.setString(3, comp.getAccountId());
/*  129: 124 */         prepstat.setString(4, comp.getServiceName());
/*  130: 125 */         prepstat.setString(5, comp.getDefaultMessage());
/*  131: 126 */         prepstat.execute();
/*  132:     */       }
/*  133: 137 */       SQL = "select * from competitions where keyword =? and account_id= ? AND ((? < startdate and ? between startdate and enddate) OR (? between startdate and enddate and ? between startdate and enddate) OR (? between startdate and enddate and ? > enddate) OR (startdate between ? and ? and enddate between ? and ?) OR (startdate = ? and enddate = ?))";
/*  134:     */       
/*  135:     */ 
/*  136:     */ 
/*  137:     */ 
/*  138:     */ 
/*  139:     */ 
/*  140:     */ 
/*  141:     */ 
/*  142: 146 */       prepstat = con.prepareStatement(SQL);
/*  143: 147 */       prepstat.setString(1, comp.getKeyword());
/*  144: 148 */       prepstat.setString(2, comp.getAccountId());
/*  145: 149 */       prepstat.setTimestamp(3, comp.getStartDate());
/*  146: 150 */       prepstat.setTimestamp(4, comp.getEndDate());
/*  147: 151 */       prepstat.setTimestamp(5, comp.getStartDate());
/*  148: 152 */       prepstat.setTimestamp(6, comp.getEndDate());
/*  149: 153 */       prepstat.setTimestamp(7, comp.getStartDate());
/*  150: 154 */       prepstat.setTimestamp(8, comp.getEndDate());
/*  151: 155 */       prepstat.setTimestamp(9, comp.getStartDate());
/*  152: 156 */       prepstat.setTimestamp(10, comp.getEndDate());
/*  153: 157 */       prepstat.setTimestamp(11, comp.getStartDate());
/*  154: 158 */       prepstat.setTimestamp(12, comp.getEndDate());
/*  155: 159 */       prepstat.setTimestamp(13, comp.getStartDate());
/*  156: 160 */       prepstat.setTimestamp(14, comp.getEndDate());
/*  157:     */       
/*  158:     */ 
/*  159: 163 */       rs = prepstat.executeQuery();
/*  160: 164 */       if (!rs.next())
/*  161:     */       {
/*  162: 165 */         SQL = "insert into competitions(keyword,description,type,startdate,enddate,thumbnailurl,currentparticipation,participationlimit,account_id,competition_id,question,answer,alternative_answers) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
/*  163:     */         
/*  164:     */ 
/*  165: 168 */         prepstat = con.prepareStatement(SQL);
/*  166: 169 */         prepstat.setString(1, comp.getKeyword());
/*  167: 170 */         prepstat.setString(2, comp.getDescription());
/*  168: 171 */         prepstat.setInt(3, comp.getType());
/*  169: 172 */         prepstat.setTimestamp(4, comp.getStartDate());
/*  170: 173 */         prepstat.setTimestamp(5, comp.getEndDate());
/*  171: 174 */         prepstat.setString(6, comp.getThumbnailURL());
/*  172: 175 */         prepstat.setLong(7, comp.getCurrentParticipation());
/*  173: 176 */         prepstat.setString(8, comp.getParticipationLimit());
/*  174: 177 */         prepstat.setString(9, comp.getAccountId());
/*  175: 178 */         prepstat.setString(10, comp.getCompetitionId());
/*  176: 179 */         prepstat.setString(11, comp.getQuestion());
/*  177: 180 */         prepstat.setString(12, comp.getAnswer());
/*  178: 181 */         prepstat.setString(13, comp.getAlternativeAnswersToString());
/*  179: 182 */         prepstat.execute();
/*  180:     */       }
/*  181:     */       else
/*  182:     */       {
/*  183: 184 */         throw new Exception("A competition  already exists within the same duration with the same keyword");
/*  184:     */       }
/*  185: 188 */       if (!comp.getOptions().isEmpty())
/*  186:     */       {
/*  187: 189 */         SQL = "insert into quickquestions_options(optionid,description,competition_id,keyword,account_id) values(?,?,?,?,?)";
/*  188: 190 */         for (int i = 0; i < comp.getOptions().size(); i++)
/*  189:     */         {
/*  190: 191 */           prepstat = con.prepareStatement(SQL);
/*  191: 192 */           prepstat.setString(1, ((Option)comp.getOptions().get(i)).getOptionId());
/*  192: 193 */           prepstat.setString(2, ((Option)comp.getOptions().get(i)).getDescription());
/*  193: 194 */           prepstat.setString(3, comp.getCompetitionId());
/*  194: 195 */           prepstat.setString(4, ((Option)comp.getOptions().get(i)).getKeyword());
/*  195: 196 */           prepstat.setString(5, ((Option)comp.getOptions().get(i)).getAccountId());
/*  196:     */           
/*  197: 198 */           prepstat.execute();
/*  198:     */         }
/*  199:     */       }
/*  200: 203 */       if (!comp.getPrizes().isEmpty())
/*  201:     */       {
/*  202: 204 */         SQL = "insert into prizes(id,keyword,description,winnersmsisdn,thumbnailurl,account_id,competition_id) values(?,?,?,?,?,?,?)";
/*  203: 206 */         for (int i = 0; i < comp.getPrizes().size(); i++)
/*  204:     */         {
/*  205: 207 */           prepstat = con.prepareStatement(SQL);
/*  206: 208 */           prepstat.setString(1, ((Prize)comp.getPrizes().get(i)).getPrizeId());
/*  207: 209 */           prepstat.setString(2, ((Prize)comp.getPrizes().get(i)).getKeyword());
/*  208: 210 */           prepstat.setString(3, ((Prize)comp.getPrizes().get(i)).getDescription());
/*  209: 211 */           prepstat.setString(4, ((Prize)comp.getPrizes().get(i)).getWinnerMSISDN());
/*  210: 212 */           prepstat.setString(5, ((Prize)comp.getPrizes().get(i)).getThumbnailURL());
/*  211: 213 */           prepstat.setString(6, ((Prize)comp.getPrizes().get(i)).getAccountId());
/*  212: 214 */           prepstat.setString(7, comp.getCompetitionId());
/*  213:     */           
/*  214: 216 */           prepstat.execute();
/*  215:     */         }
/*  216:     */       }
/*  217:     */     }
/*  218:     */     catch (Exception ex)
/*  219:     */     {
/*  220: 225 */       bError = true;
/*  221:     */       try
/*  222:     */       {
/*  223: 227 */         deleteThisCompetition(comp.getCompetitionId());
/*  224:     */       }
/*  225:     */       catch (Exception ee) {}
/*  226: 229 */       throw new Exception(ex.getMessage());
/*  227:     */     }
/*  228:     */     finally
/*  229:     */     {
/*  230: 232 */       if (bError) {
/*  231: 233 */         con.rollback();
/*  232:     */       } else {
/*  233: 235 */         con.commit();
/*  234:     */       }
/*  235: 238 */       if (con != null) {
/*  236: 239 */         con.close();
/*  237:     */       }
/*  238: 241 */       start = null;
/*  239: 242 */       end = null;
/*  240: 243 */       comp = null;
/*  241:     */     }
/*  242:     */   }
/*  243:     */   
/*  244:     */   public static void updateCompetition(Competition oldcomp, Competition newcomp)
/*  245:     */     throws Exception
/*  246:     */   {
/*  247: 251 */     ResultSet rs = null;
/*  248: 252 */     Connection con = null;
/*  249: 253 */     PreparedStatement prepstat = null;
/*  250: 254 */     boolean bError = false;
/*  251:     */     
/*  252: 256 */     Calendar start = Calendar.getInstance();
/*  253: 257 */     Calendar end = Calendar.getInstance();
/*  254:     */     try
/*  255:     */     {
/*  256: 260 */       con = DConnect.getConnection();
/*  257: 261 */       con.setAutoCommit(false);
/*  258:     */       
/*  259: 263 */       start.setTimeInMillis(newcomp.getStartDate().getTime());
/*  260: 264 */       end.setTimeInMillis(newcomp.getEndDate().getTime());
/*  261: 266 */       if (end.before(start)) {
/*  262: 267 */         throw new Exception("END date cannot be before START date");
/*  263:     */       }
/*  264: 278 */       String SQL = "select * from competitions where keyword =? and account_id= ? AND ((? < startdate and ? between startdate and enddate) OR (? between startdate and enddate and ? between startdate and enddate) OR (? between startdate and enddate and ? > enddate) OR (startdate between ? and ? and enddate between ? and ?) OR (startdate = ? and enddate = ?)) AND competition_id!=?";
/*  265:     */       
/*  266:     */ 
/*  267:     */ 
/*  268:     */ 
/*  269:     */ 
/*  270:     */ 
/*  271:     */ 
/*  272:     */ 
/*  273:     */ 
/*  274: 288 */       prepstat = con.prepareStatement(SQL);
/*  275: 289 */       prepstat.setString(1, oldcomp.getKeyword());
/*  276: 290 */       prepstat.setString(2, oldcomp.getAccountId());
/*  277: 291 */       prepstat.setTimestamp(3, newcomp.getStartDate());
/*  278: 292 */       prepstat.setTimestamp(4, newcomp.getEndDate());
/*  279: 293 */       prepstat.setTimestamp(5, newcomp.getStartDate());
/*  280: 294 */       prepstat.setTimestamp(6, newcomp.getEndDate());
/*  281: 295 */       prepstat.setTimestamp(7, newcomp.getStartDate());
/*  282: 296 */       prepstat.setTimestamp(8, newcomp.getEndDate());
/*  283: 297 */       prepstat.setTimestamp(9, newcomp.getStartDate());
/*  284: 298 */       prepstat.setTimestamp(10, newcomp.getEndDate());
/*  285: 299 */       prepstat.setTimestamp(11, newcomp.getStartDate());
/*  286: 300 */       prepstat.setTimestamp(12, newcomp.getEndDate());
/*  287: 301 */       prepstat.setTimestamp(13, newcomp.getStartDate());
/*  288: 302 */       prepstat.setTimestamp(14, newcomp.getEndDate());
/*  289: 303 */       prepstat.setString(15, oldcomp.getCompetitionId());
/*  290:     */       
/*  291:     */ 
/*  292: 306 */       rs = prepstat.executeQuery();
/*  293: 307 */       if (!rs.next())
/*  294:     */       {
/*  295: 310 */         SQL = "update service_definition set default_message='" + newcomp.getDefaultMessage() + "',service_name='" + newcomp.getServiceName() + "' where account_id='" + oldcomp.getAccountId() + "' and keyword='" + oldcomp.getKeyword() + "' and service_type='" + oldcomp.getServiceType() + "'";
/*  296:     */         
/*  297:     */ 
/*  298: 313 */         prepstat = con.prepareStatement(SQL);
/*  299: 314 */         prepstat.execute();
/*  300:     */         
/*  301:     */ 
/*  302: 317 */         SQL = "update competitions set description=?,type=?,startdate=?,enddate=?,thumbnailurl=?,currentparticipation=?,participationlimit=?,question=?, answer=?, alternative_answers=?, keyword=?, account_id=? where competition_id=?";
/*  303:     */         
/*  304:     */ 
/*  305: 320 */         prepstat = con.prepareStatement(SQL);
/*  306:     */         
/*  307:     */ 
/*  308: 323 */         prepstat.setString(1, newcomp.getDescription());
/*  309: 324 */         prepstat.setInt(2, newcomp.getType());
/*  310: 325 */         prepstat.setTimestamp(3, newcomp.getStartDate());
/*  311: 326 */         prepstat.setTimestamp(4, newcomp.getEndDate());
/*  312: 327 */         prepstat.setString(5, newcomp.getThumbnailURL());
/*  313: 328 */         prepstat.setLong(6, newcomp.getCurrentParticipation());
/*  314: 329 */         prepstat.setString(7, newcomp.getParticipationLimit());
/*  315: 330 */         prepstat.setString(8, newcomp.getQuestion());
/*  316: 331 */         prepstat.setString(9, newcomp.getAnswer());
/*  317: 332 */         prepstat.setString(10, newcomp.getAlternativeAnswersToString());
/*  318:     */         
/*  319:     */ 
/*  320: 335 */         prepstat.setString(11, oldcomp.getKeyword());
/*  321: 336 */         prepstat.setString(12, oldcomp.getAccountId());
/*  322: 337 */         prepstat.setString(13, oldcomp.getCompetitionId());
/*  323:     */         
/*  324: 339 */         prepstat.execute();
/*  325:     */         
/*  326:     */ 
/*  327: 342 */         SQL = "update quickquestions_options set description=? where optionid=? and competition_id=?";
/*  328: 344 */         for (int i = 0; i < newcomp.getOptions().size(); i++)
/*  329:     */         {
/*  330: 345 */           prepstat = con.prepareStatement(SQL);
/*  331: 346 */           prepstat.setString(1, ((Option)newcomp.getOptions().get(i)).getDescription());
/*  332:     */           
/*  333: 348 */           prepstat.setString(2, ((Option)oldcomp.getOptions().get(i)).getOptionId());
/*  334: 349 */           prepstat.setString(3, newcomp.getCompetitionId());
/*  335:     */           
/*  336:     */ 
/*  337:     */ 
/*  338: 353 */           prepstat.execute();
/*  339:     */         }
/*  340: 357 */         SQL = "update prizes set description=?,winnersmsisdn=?,thumbnailurl=? where id=? and competition_id=?";
/*  341: 360 */         for (int i = 0; i < newcomp.getPrizes().size(); i++)
/*  342:     */         {
/*  343: 361 */           prepstat = con.prepareStatement(SQL);
/*  344: 362 */           prepstat.setString(1, ((Prize)newcomp.getPrizes().get(i)).getDescription());
/*  345: 363 */           prepstat.setString(2, ((Prize)newcomp.getPrizes().get(i)).getWinnerMSISDN());
/*  346: 364 */           prepstat.setString(3, ((Prize)newcomp.getPrizes().get(i)).getThumbnailURL());
/*  347: 365 */           prepstat.setString(4, ((Prize)oldcomp.getPrizes().get(i)).getPrizeId());
/*  348:     */           
/*  349:     */ 
/*  350: 368 */           prepstat.setString(5, oldcomp.getCompetitionId());
/*  351:     */           
/*  352: 370 */           prepstat.execute();
/*  353:     */         }
/*  354:     */       }
/*  355:     */       else
/*  356:     */       {
/*  357: 375 */         throw new Exception("Cannot edit competition for this date.");
/*  358:     */       }
/*  359:     */     }
/*  360:     */     catch (Exception ex)
/*  361:     */     {
/*  362: 379 */       if (con != null)
/*  363:     */       {
/*  364: 380 */         con.close();
/*  365: 381 */         con = null;
/*  366:     */       }
/*  367: 383 */       bError = true;
/*  368: 384 */       throw new Exception(ex.getMessage());
/*  369:     */     }
/*  370:     */     finally
/*  371:     */     {
/*  372: 387 */       if (con != null) {
/*  373: 388 */         con.close();
/*  374:     */       }
/*  375:     */     }
/*  376:     */   }
/*  377:     */   
/*  378:     */   public static Competition viewCompetition(String keyword, String accountId)
/*  379:     */     throws Exception
/*  380:     */   {
/*  381: 397 */     Competition competition = null;
/*  382: 398 */     ArrayList prizes = new ArrayList();
/*  383: 399 */     ArrayList options = new ArrayList();
/*  384:     */     
/*  385:     */ 
/*  386: 402 */     ResultSet rs = null;
/*  387: 403 */     Connection con = null;
/*  388: 404 */     PreparedStatement prepstat = null;
/*  389:     */     try
/*  390:     */     {
/*  391: 406 */       con = DConnect.getConnection();
/*  392:     */       
/*  393: 408 */       String SQL = "select * from service_definition s ,competitions c where c.account_id=? and c.keyword =? and c.keyword=s.keyword and c.account_id=s.account_id and s.service_type=8";
/*  394:     */       
/*  395:     */ 
/*  396: 411 */       prepstat = con.prepareStatement(SQL);
/*  397:     */       
/*  398: 413 */       prepstat.setString(1, keyword);
/*  399: 414 */       prepstat.setString(2, accountId);
/*  400:     */       
/*  401: 416 */       rs = prepstat.executeQuery();
/*  402: 418 */       while (rs.next())
/*  403:     */       {
/*  404: 419 */         if (rs.getInt("type") == 0) {
/*  405: 420 */           competition = new EndlessOddsCompetition();
/*  406: 421 */         } else if (rs.getInt("type") == 1) {
/*  407: 422 */           competition = new FixedOddsCompetition();
/*  408: 423 */         } else if (rs.getInt("type") == 2) {
/*  409: 424 */           competition = new EndlessOddsWithQuestion();
/*  410:     */         }
/*  411: 427 */         competition.setCompetitionId(rs.getString("competition_id"));
/*  412: 428 */         competition.setCurrentParticipation(rs.getLong("currentparticipation"));
/*  413: 429 */         competition.setDescription(rs.getString("description"));
/*  414: 430 */         competition.setEndDate(rs.getTimestamp("enddate"));
/*  415: 431 */         competition.setKeyword(rs.getString("keyword"));
/*  416: 432 */         competition.setParticipationLimit(rs.getString("participationlimit"));
/*  417: 433 */         competition.setAccountId(rs.getString("account_id"));
/*  418: 434 */         competition.setQuestion(rs.getString("question"));
/*  419: 435 */         competition.setAnswer(rs.getString("answer"));
/*  420: 436 */         competition.setStartDate(rs.getTimestamp("startdate"));
/*  421: 437 */         competition.setThumbnailURL(rs.getString("thumbnailurl"));
/*  422: 438 */         competition.setAlternativeAnswers(rs.getString("alternative_answers"));
/*  423: 439 */         competition.setType(rs.getInt("type"));
/*  424: 440 */         competition.setServiceName(rs.getString("service_name"));
/*  425: 441 */         competition.setServiceType(rs.getString("service_type"));
/*  426:     */       }
/*  427: 444 */       SQL = "select * from  prizes where competition_id=?";
/*  428: 445 */       prepstat = con.prepareStatement(SQL);
/*  429: 446 */       prepstat.setString(1, competition.getCompetitionId());
/*  430: 447 */       rs = prepstat.executeQuery();
/*  431: 448 */       while (rs.next()) {
/*  432: 449 */         prizes.add(new Prize(rs.getString("id"), rs.getString("description"), rs.getString("winnersmsisdn"), rs.getString("thumbnailurl"), rs.getString("keyword"), rs.getString("account_id"), rs.getString("competition_id")));
/*  433:     */       }
/*  434: 452 */       competition.setPrizes(prizes);
/*  435:     */       
/*  436: 454 */       SQL = "select * from quickquestions_options where competition_id=?";
/*  437: 455 */       prepstat = con.prepareStatement(SQL);
/*  438: 456 */       prepstat.setString(1, competition.getCompetitionId());
/*  439: 457 */       rs = prepstat.executeQuery();
/*  440: 458 */       while (rs.next())
/*  441:     */       {
/*  442: 459 */         Option opt = new Option();
/*  443: 460 */         opt.setDescription(rs.getString("description"));
/*  444: 461 */         opt.setOptionId(rs.getString("optionid"));
/*  445: 462 */         opt.setQuestionId(rs.getString("competition_id"));
/*  446: 463 */         opt.setKeyword(rs.getString("keyword"));
/*  447: 464 */         opt.setAccountId(rs.getString("account_id"));
/*  448:     */         
/*  449: 466 */         options.add(opt);
/*  450:     */       }
/*  451: 468 */       competition.setOptions(options);
/*  452:     */     }
/*  453:     */     catch (Exception ex)
/*  454:     */     {
/*  455: 471 */       if (con != null) {
/*  456: 472 */         con.close();
/*  457:     */       }
/*  458: 474 */       throw new Exception(ex.getMessage());
/*  459:     */     }
/*  460: 476 */     if (con != null) {
/*  461: 477 */       con.close();
/*  462:     */     }
/*  463: 480 */     return competition;
/*  464:     */   }
/*  465:     */   
/*  466:     */   public static Competition viewCompetition(String keyword, String accountId, Date date)
/*  467:     */     throws Exception
/*  468:     */   {
/*  469: 486 */     Competition competition = null;
/*  470: 487 */     ArrayList prizes = new ArrayList();
/*  471: 488 */     ArrayList options = new ArrayList();
/*  472: 489 */     String dateStr = new Timestamp(date.getTime()).toString();
/*  473:     */     
/*  474:     */ 
/*  475: 492 */     ResultSet rs = null;
/*  476: 493 */     Connection con = null;
/*  477: 494 */     PreparedStatement prepstat = null;
/*  478: 495 */     boolean compFound = false;
/*  479:     */     try
/*  480:     */     {
/*  481: 498 */       con = DConnect.getConnection();
/*  482:     */       
/*  483: 500 */       String SQL = "select * from service_definition s ,competitions c where c.account_id='" + accountId + "' and c.keyword ='" + keyword + "' and c.startdate<='" + dateStr + "' and c.enddate>='" + dateStr + "' and c.keyword=s.keyword and c.account_id=s.account_id and s.service_type=8";
/*  484:     */       
/*  485:     */ 
/*  486: 503 */       prepstat = con.prepareStatement(SQL);
/*  487:     */       
/*  488: 505 */       rs = prepstat.executeQuery();
/*  489: 507 */       while (rs.next())
/*  490:     */       {
/*  491: 508 */         if (rs.getInt("type") == 0) {
/*  492: 509 */           competition = new EndlessOddsCompetition();
/*  493: 510 */         } else if (rs.getInt("type") == 1) {
/*  494: 511 */           competition = new FixedOddsCompetition();
/*  495: 512 */         } else if (rs.getInt("type") == 2) {
/*  496: 513 */           competition = new EndlessOddsWithQuestion();
/*  497:     */         }
/*  498: 516 */         competition.setCompetitionId(rs.getString("competition_id"));
/*  499: 517 */         competition.setCurrentParticipation(rs.getLong("currentparticipation"));
/*  500: 518 */         competition.setDescription(rs.getString("description"));
/*  501: 519 */         competition.setEndDate(rs.getTimestamp("enddate"));
/*  502: 520 */         competition.setKeyword(rs.getString("keyword"));
/*  503: 521 */         competition.setParticipationLimit(rs.getString("participationlimit"));
/*  504: 522 */         competition.setAccountId(rs.getString("account_id"));
/*  505: 523 */         competition.setQuestion(rs.getString("question"));
/*  506: 524 */         competition.setAnswer(rs.getString("answer"));
/*  507: 525 */         competition.setStartDate(rs.getTimestamp("startdate"));
/*  508: 526 */         competition.setThumbnailURL(rs.getString("thumbnailurl"));
/*  509: 527 */         competition.setAlternativeAnswers(rs.getString("alternative_answers"));
/*  510: 528 */         competition.setType(rs.getInt("type"));
/*  511: 529 */         competition.setServiceName(rs.getString("service_name"));
/*  512: 530 */         competition.setServiceType(rs.getString("service_type"));
/*  513: 531 */         competition.setDefaultMessage(rs.getString("default_message"));
/*  514:     */         
/*  515: 533 */         compFound = true;
/*  516:     */       }
/*  517: 535 */       if (compFound)
/*  518:     */       {
/*  519: 536 */         SQL = "select * from  prizes where competition_id=?";
/*  520: 537 */         prepstat = con.prepareStatement(SQL);
/*  521: 538 */         prepstat.setString(1, competition.getCompetitionId());
/*  522: 539 */         rs = prepstat.executeQuery();
/*  523: 540 */         while (rs.next()) {
/*  524: 541 */           prizes.add(new Prize(rs.getString("id"), rs.getString("description"), rs.getString("winnersmsisdn"), rs.getString("thumbnailurl"), rs.getString("keyword"), rs.getString("account_id"), rs.getString("competition_id")));
/*  525:     */         }
/*  526: 544 */         competition.setPrizes(prizes);
/*  527:     */         
/*  528: 546 */         SQL = "select * from quickquestions_options where competition_id=?";
/*  529: 547 */         prepstat = con.prepareStatement(SQL);
/*  530: 548 */         prepstat.setString(1, competition.getCompetitionId());
/*  531: 549 */         rs = prepstat.executeQuery();
/*  532: 550 */         while (rs.next())
/*  533:     */         {
/*  534: 551 */           Option opt = new Option();
/*  535: 552 */           opt.setDescription(rs.getString("description"));
/*  536: 553 */           opt.setOptionId(rs.getString("optionid"));
/*  537: 554 */           opt.setQuestionId(rs.getString("competition_id"));
/*  538: 555 */           opt.setKeyword(rs.getString("keyword"));
/*  539: 556 */           opt.setAccountId(rs.getString("account_id"));
/*  540:     */           
/*  541: 558 */           options.add(opt);
/*  542:     */         }
/*  543: 560 */         competition.setOptions(options);
/*  544:     */       }
/*  545:     */     }
/*  546:     */     catch (Exception ex)
/*  547:     */     {
/*  548: 564 */       if (con != null) {
/*  549: 565 */         con.close();
/*  550:     */       }
/*  551: 567 */       throw new Exception(ex.getMessage());
/*  552:     */     }
/*  553: 569 */     if (con != null) {
/*  554: 570 */       con.close();
/*  555:     */     }
/*  556: 573 */     return competition;
/*  557:     */   }
/*  558:     */   
/*  559:     */   public static ArrayList viewEntries(String competitionId)
/*  560:     */     throws Exception
/*  561:     */   {
/*  562: 578 */     ArrayList entries = new ArrayList();
/*  563:     */     
/*  564: 580 */     ResultSet rs = null;
/*  565: 581 */     Connection con = null;
/*  566: 582 */     PreparedStatement prepstat = null;
/*  567:     */     try
/*  568:     */     {
/*  569: 584 */       con = DConnect.getConnection();
/*  570: 585 */       String SQL = "select ib.* from inbox ib inner join competitions cmp on ib.keyword =cmp.keyword and ib.account_id = cmp.account_id where cmp.competition_id=?  and ib.date_voted BETWEEN cmp.startdate and cmp.enddate order by date_voted desc";
/*  571:     */       
/*  572: 587 */       prepstat = con.prepareStatement(SQL);
/*  573: 588 */       prepstat.setString(1, competitionId);
/*  574: 589 */       rs = prepstat.executeQuery();
/*  575: 591 */       while (rs.next())
/*  576:     */       {
/*  577: 592 */         InboxEntry entry = new InboxEntry();
/*  578: 593 */         entry.setDateReceived(rs.getTimestamp("date_voted"));
/*  579: 594 */         entry.setMessage(rs.getString("response"));
/*  580: 595 */         entry.setMessageId(rs.getString("voteid"));
/*  581: 596 */         entry.setMsisdn(rs.getString("mobileno"));
/*  582: 597 */         entry.setKeyword(rs.getString("keyword"));
/*  583: 598 */         entry.setShortCode(rs.getString("short_code"));
/*  584: 599 */         entry.setAccountId(rs.getString("account_id"));
/*  585: 600 */         entry.setViewed(rs.getInt("is_read"));
/*  586: 601 */         entries.add(entry);
/*  587:     */       }
/*  588:     */     }
/*  589:     */     catch (Exception ex)
/*  590:     */     {
/*  591: 604 */       if (con != null) {
/*  592: 605 */         con.close();
/*  593:     */       }
/*  594: 607 */       throw new Exception(ex.getMessage());
/*  595:     */     }
/*  596: 609 */     if (con != null) {
/*  597: 610 */       con.close();
/*  598:     */     }
/*  599: 613 */     return entries;
/*  600:     */   }
/*  601:     */   
/*  602:     */   public static ArrayList viewEntries(String competitionId, Date date)
/*  603:     */     throws Exception
/*  604:     */   {
/*  605: 618 */     ArrayList entries = new ArrayList();
/*  606:     */     
/*  607: 620 */     ResultSet rs = null;
/*  608: 621 */     Connection con = null;
/*  609: 622 */     PreparedStatement prepstat = null;
/*  610:     */     
/*  611: 624 */     String dateStr = new Timestamp(date.getTime()).toString().split(" ")[0];
/*  612:     */     try
/*  613:     */     {
/*  614: 627 */       con = DConnect.getConnection();
/*  615: 628 */       String SQL = "select ib.* from inbox ib inner join competitions cmp on ib.keyword =cmp.keyword and ib.account_id = cmp.account_id where cmp.competition_id='" + competitionId + "'  and ib.date_voted BETWEEN cmp.startdate and cmp.enddate and " + "date(date_voted)='" + dateStr + "'  order by date_voted desc";
/*  616:     */       
/*  617:     */ 
/*  618: 631 */       prepstat = con.prepareStatement(SQL);
/*  619: 632 */       rs = prepstat.executeQuery();
/*  620: 634 */       while (rs.next())
/*  621:     */       {
/*  622: 635 */         InboxEntry entry = new InboxEntry();
/*  623: 636 */         entry.setDateReceived(rs.getTimestamp("date_voted"));
/*  624: 637 */         entry.setMessage(rs.getString("response"));
/*  625: 638 */         entry.setMessageId(rs.getString("voteid"));
/*  626: 639 */         entry.setMsisdn(rs.getString("mobileno"));
/*  627: 640 */         entry.setKeyword(rs.getString("keyword"));
/*  628: 641 */         entry.setShortCode(rs.getString("short_code"));
/*  629: 642 */         entry.setAccountId(rs.getString("account_id"));
/*  630: 643 */         entry.setViewed(rs.getInt("is_read"));
/*  631: 644 */         entries.add(entry);
/*  632:     */       }
/*  633:     */     }
/*  634:     */     catch (Exception ex)
/*  635:     */     {
/*  636: 647 */       if (con != null) {
/*  637: 648 */         con.close();
/*  638:     */       }
/*  639: 650 */       throw new Exception(ex.getMessage());
/*  640:     */     }
/*  641: 652 */     if (con != null) {
/*  642: 653 */       con.close();
/*  643:     */     }
/*  644: 656 */     return entries;
/*  645:     */   }
/*  646:     */   
/*  647:     */   public static Competition viewCompetition(String competitionId)
/*  648:     */     throws Exception
/*  649:     */   {
/*  650: 662 */     Competition competition = null;
/*  651: 663 */     ArrayList prizes = new ArrayList();
/*  652: 664 */     ArrayList options = new ArrayList();
/*  653:     */     
/*  654:     */ 
/*  655: 667 */     ResultSet rs = null;
/*  656: 668 */     Connection con = null;
/*  657: 669 */     PreparedStatement prepstat = null;
/*  658:     */     try
/*  659:     */     {
/*  660: 671 */       con = DConnect.getConnection();
/*  661:     */       
/*  662: 673 */       String SQL = "select * from competitions c, service_definition s  where competition_id=? and c.keyword=s.keyword and c.account_id=s.account_id and s.service_type=8";
/*  663:     */       
/*  664:     */ 
/*  665: 676 */       prepstat = con.prepareStatement(SQL);
/*  666:     */       
/*  667: 678 */       prepstat.setString(1, competitionId);
/*  668:     */       
/*  669:     */ 
/*  670: 681 */       rs = prepstat.executeQuery();
/*  671: 683 */       while (rs.next())
/*  672:     */       {
/*  673: 684 */         if (rs.getInt("type") == 0) {
/*  674: 685 */           competition = new EndlessOddsCompetition();
/*  675: 686 */         } else if (rs.getInt("type") == 1) {
/*  676: 687 */           competition = new FixedOddsCompetition();
/*  677: 688 */         } else if (rs.getInt("type") == 2) {
/*  678: 689 */           competition = new EndlessOddsWithQuestion();
/*  679:     */         }
/*  680: 692 */         competition.setCompetitionId(rs.getString("competition_id"));
/*  681: 693 */         competition.setCurrentParticipation(rs.getLong("currentparticipation"));
/*  682: 694 */         competition.setDescription(rs.getString("description"));
/*  683: 695 */         competition.setEndDate(rs.getTimestamp("enddate"));
/*  684: 696 */         competition.setKeyword(rs.getString("keyword"));
/*  685: 697 */         competition.setParticipationLimit(rs.getString("participationlimit"));
/*  686: 698 */         competition.setAccountId(rs.getString("account_id"));
/*  687: 699 */         competition.setQuestion(rs.getString("question"));
/*  688: 700 */         competition.setAnswer(rs.getString("answer"));
/*  689: 701 */         competition.setStartDate(rs.getTimestamp("startdate"));
/*  690: 702 */         competition.setThumbnailURL(rs.getString("thumbnailurl"));
/*  691: 703 */         competition.setAlternativeAnswers(rs.getString("alternative_answers"));
/*  692: 704 */         competition.setType(rs.getInt("type"));
/*  693: 705 */         competition.setServiceName(rs.getString("service_name"));
/*  694: 706 */         competition.setServiceType(rs.getString("service_type"));
/*  695: 707 */         competition.setDefaultMessage(rs.getString("default_message"));
/*  696:     */       }
/*  697: 710 */       SQL = "select * from  prizes where competition_id=?";
/*  698: 711 */       prepstat = con.prepareStatement(SQL);
/*  699: 712 */       prepstat.setString(1, competition.getCompetitionId());
/*  700: 713 */       rs = prepstat.executeQuery();
/*  701: 714 */       while (rs.next()) {
/*  702: 715 */         prizes.add(new Prize(rs.getString("id"), rs.getString("description"), rs.getString("winnersmsisdn"), rs.getString("thumbnailurl"), rs.getString("keyword"), rs.getString("account_id"), rs.getString("competition_id")));
/*  703:     */       }
/*  704: 718 */       competition.setPrizes(prizes);
/*  705:     */       
/*  706: 720 */       SQL = "select * from quickquestions_options where competition_id=?";
/*  707: 721 */       prepstat = con.prepareStatement(SQL);
/*  708: 722 */       prepstat.setString(1, competition.getCompetitionId());
/*  709: 723 */       rs = prepstat.executeQuery();
/*  710: 724 */       while (rs.next())
/*  711:     */       {
/*  712: 725 */         Option opt = new Option();
/*  713: 726 */         opt.setDescription(rs.getString("description"));
/*  714: 727 */         opt.setOptionId(rs.getString("optionid"));
/*  715: 728 */         opt.setQuestionId(rs.getString("competition_id"));
/*  716: 729 */         opt.setKeyword(rs.getString("keyword"));
/*  717: 730 */         opt.setAccountId(rs.getString("account_id"));
/*  718:     */         
/*  719: 732 */         options.add(opt);
/*  720:     */       }
/*  721: 734 */       competition.setOptions(options);
/*  722:     */     }
/*  723:     */     catch (Exception ex)
/*  724:     */     {
/*  725: 737 */       if (con != null) {
/*  726: 738 */         con.close();
/*  727:     */       }
/*  728: 740 */       throw new Exception(ex.getMessage());
/*  729:     */     }
/*  730: 742 */     if (con != null) {
/*  731: 743 */       con.close();
/*  732:     */     }
/*  733: 746 */     return competition;
/*  734:     */   }
/*  735:     */   
/*  736:     */   public static ArrayList viewAllCompetitions(String accountId)
/*  737:     */     throws Exception
/*  738:     */   {
/*  739: 753 */     ArrayList competitions = new ArrayList();
/*  740:     */     
/*  741:     */ 
/*  742: 756 */     ResultSet rs = null;
/*  743: 757 */     ResultSet rs2 = null;
/*  744: 758 */     Connection con = null;
/*  745: 759 */     PreparedStatement prepstat2 = null;
/*  746:     */     try
/*  747:     */     {
/*  748: 761 */       con = DConnect.getConnection();
/*  749:     */       
/*  750: 763 */       String SQL = "select * from service_definition s ,competitions c where c.account_id=? and c.keyword=s.keyword and c.account_id=s.account_id and s.service_type=8";
/*  751:     */       
/*  752:     */ 
/*  753: 766 */       PreparedStatement prepstat = con.prepareStatement(SQL);
/*  754: 767 */       prepstat.setString(1, accountId);
/*  755: 768 */       rs = prepstat.executeQuery();
/*  756: 770 */       while (rs.next())
/*  757:     */       {
/*  758: 771 */         Competition competition = null;
/*  759: 773 */         if (rs.getInt("type") == 0) {
/*  760: 774 */           competition = new EndlessOddsCompetition();
/*  761: 775 */         } else if (rs.getInt("type") == 1) {
/*  762: 776 */           competition = new FixedOddsCompetition();
/*  763: 777 */         } else if (rs.getInt("type") == 2) {
/*  764: 778 */           competition = new EndlessOddsWithQuestion();
/*  765:     */         }
/*  766: 781 */         competition.setCompetitionId(rs.getString("competition_id"));
/*  767: 782 */         competition.setCurrentParticipation(rs.getLong("currentparticipation"));
/*  768: 783 */         competition.setDescription(rs.getString("description"));
/*  769: 784 */         competition.setEndDate(rs.getTimestamp("enddate"));
/*  770: 785 */         competition.setKeyword(rs.getString("keyword"));
/*  771: 786 */         competition.setParticipationLimit(rs.getString("participationlimit"));
/*  772: 787 */         competition.setAccountId(rs.getString("account_id"));
/*  773: 788 */         competition.setQuestion(rs.getString("question"));
/*  774: 789 */         competition.setAnswer(rs.getString("answer"));
/*  775: 790 */         competition.setStartDate(rs.getTimestamp("startdate"));
/*  776: 791 */         competition.setThumbnailURL(rs.getString("thumbnailurl"));
/*  777: 792 */         competition.setAlternativeAnswers(rs.getString("alternative_answers"));
/*  778: 793 */         competition.setType(rs.getInt("type"));
/*  779: 794 */         competition.setServiceName(rs.getString("service_name"));
/*  780: 795 */         competition.setServiceType(rs.getString("service_type"));
/*  781:     */         
/*  782: 797 */         SQL = "select * from  prizes where competition_id=?";
/*  783: 798 */         prepstat = con.prepareStatement(SQL);
/*  784: 799 */         prepstat.setString(1, competition.getCompetitionId());
/*  785: 800 */         rs2 = prepstat.executeQuery();
/*  786:     */         
/*  787: 802 */         ArrayList prizes = new ArrayList();
/*  788: 803 */         while (rs2.next()) {
/*  789: 804 */           prizes.add(new Prize(rs2.getString("id"), rs2.getString("description"), rs2.getString("winnersmsisdn"), rs2.getString("thumbnailurl"), rs2.getString("keyword"), rs2.getString("account_id"), rs2.getString("competition_id")));
/*  790:     */         }
/*  791: 807 */         competition.setPrizes(prizes);
/*  792:     */         
/*  793: 809 */         SQL = "select * from quickquestions_options where competition_id=?";
/*  794: 810 */         prepstat = con.prepareStatement(SQL);
/*  795: 811 */         prepstat.setString(1, competition.getCompetitionId());
/*  796: 812 */         rs2 = prepstat.executeQuery();
/*  797:     */         
/*  798: 814 */         ArrayList options = new ArrayList();
/*  799: 815 */         while (rs2.next())
/*  800:     */         {
/*  801: 816 */           Option opt = new Option();
/*  802: 817 */           opt.setDescription(rs2.getString("description"));
/*  803: 818 */           opt.setOptionId(rs2.getString("optionid"));
/*  804: 819 */           opt.setQuestionId(rs2.getString("competition_id"));
/*  805: 820 */           opt.setKeyword(rs2.getString("keyword"));
/*  806: 821 */           opt.setAccountId(rs2.getString("account_id"));
/*  807:     */           
/*  808: 823 */           options.add(opt);
/*  809:     */         }
/*  810: 826 */         competition.setOptions(options);
/*  811: 827 */         competitions.add(competition);
/*  812:     */       }
/*  813:     */     }
/*  814:     */     catch (Exception ex)
/*  815:     */     {
/*  816: 831 */       if (con != null) {
/*  817: 832 */         con.close();
/*  818:     */       }
/*  819: 834 */       throw new Exception(ex.getMessage());
/*  820:     */     }
/*  821: 836 */     if (con != null) {
/*  822: 837 */       con.close();
/*  823:     */     }
/*  824: 840 */     return competitions;
/*  825:     */   }
/*  826:     */   
/*  827:     */   public static ArrayList viewAllCompetitions(String accountId, String keyword)
/*  828:     */     throws Exception
/*  829:     */   {
/*  830: 848 */     ArrayList competitions = new ArrayList();
/*  831:     */     
/*  832:     */ 
/*  833: 851 */     ResultSet rs = null;
/*  834: 852 */     ResultSet rs2 = null;
/*  835: 853 */     Connection con = null;
/*  836: 854 */     PreparedStatement prepstat2 = null;
/*  837:     */     try
/*  838:     */     {
/*  839: 856 */       con = DConnect.getConnection();
/*  840:     */       
/*  841: 858 */       String SQL = "select * from service_definition s ,competitions c where c.account_id=? and c.keyword=s.keyword and c.account_id=s.account_id and s.service_type=8 and s.keyword= ?";
/*  842:     */       
/*  843:     */ 
/*  844: 861 */       PreparedStatement prepstat = con.prepareStatement(SQL);
/*  845: 862 */       prepstat.setString(1, accountId);
/*  846: 863 */       prepstat.setString(2, keyword);
/*  847: 864 */       rs = prepstat.executeQuery();
/*  848: 866 */       while (rs.next())
/*  849:     */       {
/*  850: 867 */         Competition competition = null;
/*  851: 869 */         if (rs.getInt("type") == 0) {
/*  852: 870 */           competition = new EndlessOddsCompetition();
/*  853: 871 */         } else if (rs.getInt("type") == 1) {
/*  854: 872 */           competition = new FixedOddsCompetition();
/*  855: 873 */         } else if (rs.getInt("type") == 2) {
/*  856: 874 */           competition = new EndlessOddsWithQuestion();
/*  857:     */         }
/*  858: 877 */         competition.setCompetitionId(rs.getString("competition_id"));
/*  859: 878 */         competition.setCurrentParticipation(rs.getLong("currentparticipation"));
/*  860: 879 */         competition.setDescription(rs.getString("description"));
/*  861: 880 */         competition.setEndDate(rs.getTimestamp("enddate"));
/*  862: 881 */         competition.setKeyword(rs.getString("keyword"));
/*  863: 882 */         competition.setParticipationLimit(rs.getString("participationlimit"));
/*  864: 883 */         competition.setAccountId(rs.getString("account_id"));
/*  865: 884 */         competition.setQuestion(rs.getString("question"));
/*  866: 885 */         competition.setAnswer(rs.getString("answer"));
/*  867: 886 */         competition.setStartDate(rs.getTimestamp("startdate"));
/*  868: 887 */         competition.setThumbnailURL(rs.getString("thumbnailurl"));
/*  869: 888 */         competition.setAlternativeAnswers(rs.getString("alternative_answers"));
/*  870: 889 */         competition.setType(rs.getInt("type"));
/*  871: 890 */         competition.setServiceName(rs.getString("service_name"));
/*  872: 891 */         competition.setServiceType(rs.getString("service_type"));
/*  873:     */         
/*  874: 893 */         SQL = "select * from  prizes where competition_id=?";
/*  875: 894 */         prepstat = con.prepareStatement(SQL);
/*  876: 895 */         prepstat.setString(1, competition.getCompetitionId());
/*  877: 896 */         rs2 = prepstat.executeQuery();
/*  878:     */         
/*  879: 898 */         ArrayList prizes = new ArrayList();
/*  880: 899 */         while (rs2.next()) {
/*  881: 900 */           prizes.add(new Prize(rs2.getString("id"), rs2.getString("description"), rs2.getString("winnersmsisdn"), rs2.getString("thumbnailurl"), rs2.getString("keyword"), rs2.getString("account_id"), rs2.getString("competition_id")));
/*  882:     */         }
/*  883: 903 */         competition.setPrizes(prizes);
/*  884:     */         
/*  885: 905 */         SQL = "select * from quickquestions_options where competition_id=?";
/*  886: 906 */         prepstat = con.prepareStatement(SQL);
/*  887: 907 */         prepstat.setString(1, competition.getCompetitionId());
/*  888: 908 */         rs2 = prepstat.executeQuery();
/*  889:     */         
/*  890: 910 */         ArrayList options = new ArrayList();
/*  891: 911 */         while (rs2.next())
/*  892:     */         {
/*  893: 912 */           Option opt = new Option();
/*  894: 913 */           opt.setDescription(rs2.getString("description"));
/*  895: 914 */           opt.setOptionId(rs2.getString("optionid"));
/*  896: 915 */           opt.setQuestionId(rs2.getString("competition_id"));
/*  897: 916 */           opt.setKeyword(rs2.getString("keyword"));
/*  898: 917 */           opt.setAccountId(rs2.getString("account_id"));
/*  899:     */           
/*  900: 919 */           options.add(opt);
/*  901:     */         }
/*  902: 922 */         competition.setOptions(options);
/*  903: 923 */         competitions.add(competition);
/*  904:     */       }
/*  905:     */     }
/*  906:     */     catch (Exception ex)
/*  907:     */     {
/*  908: 927 */       if (con != null) {
/*  909: 928 */         con.close();
/*  910:     */       }
/*  911: 930 */       throw new Exception(ex.getMessage());
/*  912:     */     }
/*  913: 932 */     if (con != null) {
/*  914: 933 */       con.close();
/*  915:     */     }
/*  916: 936 */     return competitions;
/*  917:     */   }
/*  918:     */   
/*  919:     */   public static ArrayList viewAllActiveCompetitions(String accountId, String month)
/*  920:     */     throws Exception
/*  921:     */   {
/*  922: 943 */     ArrayList competitions = new ArrayList();
/*  923:     */     
/*  924:     */ 
/*  925: 946 */     ResultSet rs = null;
/*  926: 947 */     ResultSet rs2 = null;
/*  927: 948 */     Connection con = null;
/*  928: 949 */     PreparedStatement prepstat2 = null;
/*  929:     */     try
/*  930:     */     {
/*  931: 951 */       con = DConnect.getConnection();
/*  932:     */       
/*  933: 953 */       String SQL = "select * from service_definition s ,competitions c where c.account_id=? and c.keyword=s.keyword and c.account_id=s.account_id and s.service_type=8 and month(c.startdate) <= ? and month(c.enddate) >= ?";
/*  934:     */       
/*  935:     */ 
/*  936: 956 */       PreparedStatement prepstat = con.prepareStatement(SQL);
/*  937: 957 */       prepstat.setString(1, accountId);
/*  938: 958 */       prepstat.setString(2, month);
/*  939: 959 */       prepstat.setString(3, month);
/*  940: 960 */       rs = prepstat.executeQuery();
/*  941: 962 */       while (rs.next())
/*  942:     */       {
/*  943: 963 */         Competition competition = null;
/*  944: 965 */         if (rs.getInt("type") == 0) {
/*  945: 966 */           competition = new EndlessOddsCompetition();
/*  946: 967 */         } else if (rs.getInt("type") == 1) {
/*  947: 968 */           competition = new FixedOddsCompetition();
/*  948: 969 */         } else if (rs.getInt("type") == 2) {
/*  949: 970 */           competition = new EndlessOddsWithQuestion();
/*  950:     */         }
/*  951: 973 */         competition.setCompetitionId(rs.getString("competition_id"));
/*  952: 974 */         competition.setCurrentParticipation(rs.getLong("currentparticipation"));
/*  953: 975 */         competition.setDescription(rs.getString("description"));
/*  954: 976 */         competition.setEndDate(rs.getTimestamp("enddate"));
/*  955: 977 */         competition.setKeyword(rs.getString("keyword"));
/*  956: 978 */         competition.setParticipationLimit(rs.getString("participationlimit"));
/*  957: 979 */         competition.setAccountId(rs.getString("account_id"));
/*  958: 980 */         competition.setQuestion(rs.getString("question"));
/*  959: 981 */         competition.setAnswer(rs.getString("answer"));
/*  960: 982 */         competition.setStartDate(rs.getTimestamp("startdate"));
/*  961: 983 */         competition.setThumbnailURL(rs.getString("thumbnailurl"));
/*  962: 984 */         competition.setAlternativeAnswers(rs.getString("alternative_answers"));
/*  963: 985 */         competition.setType(rs.getInt("type"));
/*  964: 986 */         competition.setServiceName(rs.getString("service_name"));
/*  965: 987 */         competition.setServiceType(rs.getString("service_type"));
/*  966:     */         
/*  967: 989 */         SQL = "select * from  prizes where competition_id=?";
/*  968: 990 */         prepstat = con.prepareStatement(SQL);
/*  969: 991 */         prepstat.setString(1, competition.getCompetitionId());
/*  970: 992 */         rs2 = prepstat.executeQuery();
/*  971:     */         
/*  972: 994 */         ArrayList prizes = new ArrayList();
/*  973: 995 */         while (rs2.next()) {
/*  974: 996 */           prizes.add(new Prize(rs2.getString("id"), rs2.getString("description"), rs2.getString("winnersmsisdn"), rs2.getString("thumbnailurl"), rs2.getString("keyword"), rs2.getString("account_id"), rs2.getString("competition_id")));
/*  975:     */         }
/*  976: 999 */         competition.setPrizes(prizes);
/*  977:     */         
/*  978:1001 */         SQL = "select * from quickquestions_options where competition_id=?";
/*  979:1002 */         prepstat = con.prepareStatement(SQL);
/*  980:1003 */         prepstat.setString(1, competition.getCompetitionId());
/*  981:1004 */         rs2 = prepstat.executeQuery();
/*  982:     */         
/*  983:1006 */         ArrayList options = new ArrayList();
/*  984:1007 */         while (rs2.next())
/*  985:     */         {
/*  986:1008 */           Option opt = new Option();
/*  987:1009 */           opt.setDescription(rs2.getString("description"));
/*  988:1010 */           opt.setOptionId(rs2.getString("optionid"));
/*  989:1011 */           opt.setQuestionId(rs2.getString("competition_id"));
/*  990:1012 */           opt.setKeyword(rs2.getString("keyword"));
/*  991:1013 */           opt.setAccountId(rs2.getString("account_id"));
/*  992:     */           
/*  993:1015 */           options.add(opt);
/*  994:     */         }
/*  995:1018 */         competition.setOptions(options);
/*  996:1019 */         competitions.add(competition);
/*  997:     */       }
/*  998:     */     }
/*  999:     */     catch (Exception ex)
/* 1000:     */     {
/* 1001:1023 */       if (con != null) {
/* 1002:1024 */         con.close();
/* 1003:     */       }
/* 1004:1026 */       throw new Exception(ex.getMessage());
/* 1005:     */     }
/* 1006:1028 */     if (con != null) {
/* 1007:1029 */       con.close();
/* 1008:     */     }
/* 1009:1032 */     return competitions;
/* 1010:     */   }
/* 1011:     */   
/* 1012:     */   public static ArrayList viewAllActiveCompetitions(String accountId, String beginMonth, String endMonth)
/* 1013:     */     throws Exception
/* 1014:     */   {
/* 1015:1038 */     ArrayList competitions = new ArrayList();
/* 1016:     */     
/* 1017:     */ 
/* 1018:1041 */     ResultSet rs = null;
/* 1019:1042 */     ResultSet rs2 = null;
/* 1020:1043 */     Connection con = null;
/* 1021:1044 */     PreparedStatement prepstat2 = null;
/* 1022:     */     try
/* 1023:     */     {
/* 1024:1046 */       con = DConnect.getConnection();
/* 1025:     */       
/* 1026:1048 */       String SQL = "select * from competitions c inner join service_definition s on s.keyword=c.keyword where month(c.startdate) <= ? or month(c.enddate) >= ?";
/* 1027:     */       
/* 1028:     */ 
/* 1029:1051 */       PreparedStatement prepstat = con.prepareStatement(SQL);
/* 1030:1052 */       prepstat.setString(1, accountId);
/* 1031:1053 */       prepstat.setString(2, endMonth);
/* 1032:1054 */       prepstat.setString(3, beginMonth);
/* 1033:1055 */       rs = prepstat.executeQuery();
/* 1034:1057 */       while (rs.next())
/* 1035:     */       {
/* 1036:1058 */         Competition competition = null;
/* 1037:1060 */         if (rs.getInt("type") == 0) {
/* 1038:1061 */           competition = new EndlessOddsCompetition();
/* 1039:1062 */         } else if (rs.getInt("type") == 1) {
/* 1040:1063 */           competition = new FixedOddsCompetition();
/* 1041:1064 */         } else if (rs.getInt("type") == 2) {
/* 1042:1065 */           competition = new EndlessOddsWithQuestion();
/* 1043:     */         }
/* 1044:1068 */         competition.setCompetitionId(rs.getString("competition_id"));
/* 1045:1069 */         competition.setCurrentParticipation(rs.getLong("currentparticipation"));
/* 1046:1070 */         competition.setDescription(rs.getString("description"));
/* 1047:1071 */         competition.setEndDate(rs.getTimestamp("enddate"));
/* 1048:1072 */         competition.setKeyword(rs.getString("keyword"));
/* 1049:1073 */         competition.setParticipationLimit(rs.getString("participationlimit"));
/* 1050:1074 */         competition.setAccountId(rs.getString("account_id"));
/* 1051:1075 */         competition.setQuestion(rs.getString("question"));
/* 1052:1076 */         competition.setAnswer(rs.getString("answer"));
/* 1053:1077 */         competition.setStartDate(rs.getTimestamp("startdate"));
/* 1054:1078 */         competition.setThumbnailURL(rs.getString("thumbnailurl"));
/* 1055:1079 */         competition.setAlternativeAnswers(rs.getString("alternative_answers"));
/* 1056:1080 */         competition.setType(rs.getInt("type"));
/* 1057:1081 */         competition.setServiceName(rs.getString("service_name"));
/* 1058:1082 */         competition.setServiceType(rs.getString("service_type"));
/* 1059:     */         
/* 1060:1084 */         SQL = "select * from  prizes where competition_id=?";
/* 1061:1085 */         prepstat = con.prepareStatement(SQL);
/* 1062:1086 */         prepstat.setString(1, competition.getCompetitionId());
/* 1063:1087 */         rs2 = prepstat.executeQuery();
/* 1064:     */         
/* 1065:1089 */         ArrayList prizes = new ArrayList();
/* 1066:1090 */         while (rs2.next()) {
/* 1067:1091 */           prizes.add(new Prize(rs2.getString("id"), rs2.getString("description"), rs2.getString("winnersmsisdn"), rs2.getString("thumbnailurl"), rs2.getString("keyword"), rs2.getString("account_id"), rs2.getString("competition_id")));
/* 1068:     */         }
/* 1069:1094 */         competition.setPrizes(prizes);
/* 1070:     */         
/* 1071:1096 */         SQL = "select * from quickquestions_options where competition_id=?";
/* 1072:1097 */         prepstat = con.prepareStatement(SQL);
/* 1073:1098 */         prepstat.setString(1, competition.getCompetitionId());
/* 1074:1099 */         rs2 = prepstat.executeQuery();
/* 1075:     */         
/* 1076:1101 */         ArrayList options = new ArrayList();
/* 1077:1102 */         while (rs2.next())
/* 1078:     */         {
/* 1079:1103 */           Option opt = new Option();
/* 1080:1104 */           opt.setDescription(rs2.getString("description"));
/* 1081:1105 */           opt.setOptionId(rs2.getString("optionid"));
/* 1082:1106 */           opt.setQuestionId(rs2.getString("competition_id"));
/* 1083:1107 */           opt.setKeyword(rs2.getString("keyword"));
/* 1084:1108 */           opt.setAccountId(rs2.getString("account_id"));
/* 1085:     */           
/* 1086:1110 */           options.add(opt);
/* 1087:     */         }
/* 1088:1113 */         competition.setOptions(options);
/* 1089:1114 */         competitions.add(competition);
/* 1090:     */       }
/* 1091:     */     }
/* 1092:     */     catch (Exception ex)
/* 1093:     */     {
/* 1094:1118 */       if (con != null) {
/* 1095:1119 */         con.close();
/* 1096:     */       }
/* 1097:1121 */       throw new Exception(ex.getMessage());
/* 1098:     */     }
/* 1099:1123 */     if (con != null) {
/* 1100:1124 */       con.close();
/* 1101:     */     }
/* 1102:1127 */     return competitions;
/* 1103:     */   }
/* 1104:     */   
/* 1105:     */   public static void deleteThisCompetition(String competitionId)
/* 1106:     */     throws Exception
/* 1107:     */   {
/* 1108:1134 */     ResultSet rs = null;
/* 1109:1135 */     Connection con = null;
/* 1110:1136 */     PreparedStatement prepstat = null;
/* 1111:1137 */     boolean bError = false;
/* 1112:1138 */     boolean deleted = false;
/* 1113:     */     try
/* 1114:     */     {
/* 1115:1141 */       con = DConnect.getConnection();
/* 1116:     */       
/* 1117:1143 */       String SQL = "select * from competitions where competition_id='" + competitionId + "'";
/* 1118:1144 */       prepstat = con.prepareStatement(SQL);
/* 1119:1145 */       rs = prepstat.executeQuery();
/* 1120:     */       
/* 1121:1147 */       String keyword = "";
/* 1122:1148 */       String accountId = "";
/* 1123:1149 */       String enddate = "";
/* 1124:1150 */       String startdate = "";
/* 1125:1152 */       while (rs.next())
/* 1126:     */       {
/* 1127:1153 */         keyword = rs.getString("keyword");
/* 1128:1154 */         accountId = rs.getString("account_id");
/* 1129:1155 */         startdate = rs.getString("startdate");
/* 1130:1156 */         enddate = rs.getString("enddate");
/* 1131:     */       }
/* 1132:1160 */       SQL = "delete from prizes where competition_id='" + competitionId + "'";
/* 1133:1161 */       prepstat = con.prepareStatement(SQL);
/* 1134:1162 */       prepstat.execute();
/* 1135:     */       
/* 1136:     */ 
/* 1137:1165 */       SQL = "delete from quickquestions_options where competition_id='" + competitionId + "'";
/* 1138:1166 */       prepstat = con.prepareStatement(SQL);
/* 1139:1167 */       prepstat.execute();
/* 1140:     */       
/* 1141:     */ 
/* 1142:1170 */       SQL = "delete from inbox where keyword='" + keyword + "' and account_id='" + accountId + "' and date_voted between '" + startdate + "' and '" + enddate + "'";
/* 1143:1171 */       prepstat = con.prepareStatement(SQL);
/* 1144:1172 */       prepstat.execute();
/* 1145:     */       
/* 1146:     */ 
/* 1147:1175 */       SQL = "delete from competitions where competition_id='" + competitionId + "'";
/* 1148:1176 */       prepstat = con.prepareStatement(SQL);
/* 1149:1177 */       prepstat.execute();
/* 1150:     */       
/* 1151:1179 */       SQL = "select * from competitions where keyword='" + keyword + "' and account_id='" + accountId + "'";
/* 1152:1180 */       prepstat = con.prepareStatement(SQL);
/* 1153:1181 */       rs = prepstat.executeQuery();
/* 1154:1183 */       if (!rs.next())
/* 1155:     */       {
/* 1156:1184 */         SQL = "delete from service_definition where keyword='" + keyword + "' and account_id='" + accountId + "'";
/* 1157:1185 */         prepstat = con.prepareStatement(SQL);
/* 1158:1186 */         prepstat.execute();
/* 1159:     */       }
/* 1160:     */     }
/* 1161:     */     catch (Exception ex)
/* 1162:     */     {
/* 1163:1190 */       if (con != null) {
/* 1164:1191 */         con.close();
/* 1165:     */       }
/* 1166:1193 */       bError = true;
/* 1167:1194 */       throw new Exception(ex.getMessage());
/* 1168:     */     }
/* 1169:     */     finally
/* 1170:     */     {
/* 1171:1197 */       if (con != null) {
/* 1172:1198 */         con.close();
/* 1173:     */       }
/* 1174:     */     }
/* 1175:     */   }
/* 1176:     */   
/* 1177:     */   public static void deleteAllCompetitions(String account_id)
/* 1178:     */     throws Exception
/* 1179:     */   {
/* 1180:1206 */     ResultSet rs = null;
/* 1181:1207 */     Connection con = null;
/* 1182:1208 */     PreparedStatement prepstat = null;
/* 1183:1209 */     boolean bError = false;
/* 1184:     */     try
/* 1185:     */     {
/* 1186:1212 */       con = DConnect.getConnection();
/* 1187:1213 */       con.setAutoCommit(false);
/* 1188:     */       
/* 1189:     */ 
/* 1190:1216 */       String SQL = "delete from competitions where account_id=?";
/* 1191:1217 */       prepstat = con.prepareStatement(SQL);
/* 1192:1218 */       prepstat.setString(1, account_id);
/* 1193:     */       
/* 1194:1220 */       prepstat.execute();
/* 1195:     */       
/* 1196:     */ 
/* 1197:     */ 
/* 1198:     */ 
/* 1199:     */ 
/* 1200:     */ 
/* 1201:     */ 
/* 1202:     */ 
/* 1203:     */ 
/* 1204:1230 */       SQL = "delete from quickquestions_options where account_id=?";
/* 1205:1231 */       prepstat = con.prepareStatement(SQL);
/* 1206:1232 */       prepstat.setString(1, account_id);
/* 1207:     */       
/* 1208:1234 */       prepstat.execute();
/* 1209:     */       
/* 1210:     */ 
/* 1211:1237 */       SQL = "delete from prizes where account_id=?";
/* 1212:1238 */       prepstat = con.prepareStatement(SQL);
/* 1213:1239 */       prepstat.setString(1, account_id);
/* 1214:     */       
/* 1215:1241 */       prepstat.execute();
/* 1216:     */       
/* 1217:     */ 
/* 1218:     */ 
/* 1219:     */ 
/* 1220:     */ 
/* 1221:     */ 
/* 1222:     */ 
/* 1223:     */ 
/* 1224:1250 */       SQL = "select * from competitions where account_id=?";
/* 1225:     */       
/* 1226:1252 */       prepstat = con.prepareStatement(SQL);
/* 1227:     */       
/* 1228:1254 */       prepstat.setString(1, account_id);
/* 1229:     */       
/* 1230:1256 */       rs = prepstat.executeQuery();
/* 1231:1258 */       if (!rs.next())
/* 1232:     */       {
/* 1233:1259 */         SQL = "delete from service_definition where account_id=?";
/* 1234:1260 */         prepstat.setString(1, account_id);
/* 1235:1261 */         prepstat.execute();
/* 1236:     */       }
/* 1237:     */     }
/* 1238:     */     catch (Exception ex)
/* 1239:     */     {
/* 1240:1268 */       if (con != null) {
/* 1241:1269 */         con.close();
/* 1242:     */       }
/* 1243:1271 */       bError = true;
/* 1244:1272 */       throw new Exception(ex.getMessage());
/* 1245:     */     }
/* 1246:     */     finally
/* 1247:     */     {
/* 1248:1275 */       if (con != null) {
/* 1249:1276 */         con.close();
/* 1250:     */       }
/* 1251:     */     }
/* 1252:     */   }
/* 1253:     */   
/* 1254:     */   public static void deleteAllCompetitions(String account_id, String keyword)
/* 1255:     */     throws Exception
/* 1256:     */   {
/* 1257:1284 */     ResultSet rs = null;
/* 1258:1285 */     Connection con = null;
/* 1259:1286 */     PreparedStatement prepstat = null;
/* 1260:1287 */     boolean bError = false;
/* 1261:     */     try
/* 1262:     */     {
/* 1263:1290 */       con = DConnect.getConnection();
/* 1264:1291 */       String SQL = "select * from competitions where account_id='" + account_id + "' and keyword='" + keyword + "'";
/* 1265:1292 */       prepstat = con.prepareStatement(SQL);
/* 1266:1293 */       rs = prepstat.executeQuery();
/* 1267:     */       
/* 1268:1295 */       String enddate = "";
/* 1269:1296 */       String startdate = "";
/* 1270:1297 */       String competitionId = "";
/* 1271:1299 */       while (rs.next())
/* 1272:     */       {
/* 1273:1300 */         competitionId = rs.getString("competition_id");
/* 1274:1301 */         startdate = rs.getString("startdate");
/* 1275:1302 */         enddate = rs.getString("enddate");
/* 1276:     */         
/* 1277:     */ 
/* 1278:1305 */         SQL = "delete from prizes where competition_id='" + competitionId + "'";
/* 1279:1306 */         prepstat = con.prepareStatement(SQL);
/* 1280:1307 */         prepstat.execute();
/* 1281:     */         
/* 1282:     */ 
/* 1283:1310 */         SQL = "delete from quickquestions_options where competition_id='" + competitionId + "'";
/* 1284:1311 */         prepstat = con.prepareStatement(SQL);
/* 1285:1312 */         prepstat.execute();
/* 1286:     */         
/* 1287:     */ 
/* 1288:1315 */         SQL = "delete from inbox where keyword='" + keyword + "' and account_id='" + account_id + "' and date_voted between '" + startdate + "' and '" + enddate + "'";
/* 1289:1316 */         prepstat = con.prepareStatement(SQL);
/* 1290:1317 */         prepstat.execute();
/* 1291:     */         
/* 1292:     */ 
/* 1293:1320 */         SQL = "delete from competitions where competition_id='" + competitionId + "'";
/* 1294:1321 */         prepstat = con.prepareStatement(SQL);
/* 1295:1322 */         prepstat.execute();
/* 1296:     */       }
/* 1297:1325 */       SQL = "select * from competitions where keyword='" + keyword + "' and account_id='" + account_id + "'";
/* 1298:1326 */       prepstat = con.prepareStatement(SQL);
/* 1299:1327 */       rs = prepstat.executeQuery();
/* 1300:1329 */       if (!rs.next())
/* 1301:     */       {
/* 1302:1330 */         SQL = "delete from service_definition where keyword='" + keyword + "' and account_id='" + account_id + "'";
/* 1303:1331 */         prepstat = con.prepareStatement(SQL);
/* 1304:1332 */         prepstat.execute();
/* 1305:     */       }
/* 1306:     */     }
/* 1307:     */     catch (Exception ex)
/* 1308:     */     {
/* 1309:1335 */       if (con != null) {
/* 1310:1336 */         con.close();
/* 1311:     */       }
/* 1312:1338 */       bError = true;
/* 1313:1339 */       throw new Exception(ex.getMessage());
/* 1314:     */     }
/* 1315:     */     finally
/* 1316:     */     {
/* 1317:1342 */       if (con != null) {
/* 1318:1343 */         con.close();
/* 1319:     */       }
/* 1320:     */     }
/* 1321:     */   }
/* 1322:     */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.smscompetition.CompetitionDB
 * JD-Core Version:    0.7.0.1
 */