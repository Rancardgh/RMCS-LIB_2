/*   1:    */ package com.rancard.mobility.infoserver.livescore;
/*   2:    */ 
/*   3:    */ public class LiveScoreFixture
/*   4:    */ {
/*   5:    */   public static final int UNDECIDED = 7;
/*   6:    */   public static final int POSTPONED = 6;
/*   7:    */   public static final int CANCELLED = 5;
/*   8:    */   public static final int INTERRUPTED = 4;
/*   9:    */   public static final int OTHER = 3;
/*  10:    */   public static final int ACTIVE = 2;
/*  11:    */   public static final int PLAYED = 1;
/*  12:    */   public static final int NOT_PLAYED = 0;
/*  13:    */   public static final int NOTIF_NOT_SENT = 0;
/*  14:    */   public static final int NOTIF_SENT = 1;
/*  15:    */   private String gameId;
/*  16:    */   private String countryName;
/*  17:    */   private String leagueId;
/*  18:    */   private String leagueName;
/*  19:    */   private String homeTeam;
/*  20:    */   private String homeScore;
/*  21:    */   private String awayTeam;
/*  22:    */   private String awayScore;
/*  23:    */   private String date;
/*  24:    */   private int status;
/*  25:    */   private int eventNotifSent;
/*  26:    */   private String alias;
/*  27:    */   
/*  28:    */   public LiveScoreFixture()
/*  29:    */   {
/*  30: 45 */     this.gameId = "";
/*  31: 46 */     this.countryName = "";
/*  32: 47 */     this.leagueId = "";
/*  33: 48 */     this.leagueName = "";
/*  34: 49 */     this.homeTeam = "";
/*  35: 50 */     this.homeScore = "na";
/*  36: 51 */     this.awayTeam = "";
/*  37: 52 */     this.awayScore = "na";
/*  38: 53 */     this.date = "";
/*  39: 54 */     this.status = 0;
/*  40: 55 */     this.eventNotifSent = 0;
/*  41: 56 */     this.alias = "";
/*  42:    */   }
/*  43:    */   
/*  44:    */   public LiveScoreFixture(String gameId, String countryName, String leagueId, String leagueName, String homeTeam, String homeScore, String awayTeam, String awayScore, String date, int status, String alias)
/*  45:    */   {
/*  46: 61 */     this.gameId = gameId;
/*  47: 62 */     this.countryName = countryName;
/*  48: 63 */     this.leagueId = leagueId;
/*  49: 64 */     this.leagueName = leagueName;
/*  50: 65 */     this.homeTeam = homeTeam;
/*  51: 66 */     this.homeScore = homeScore;
/*  52: 67 */     this.awayTeam = awayTeam;
/*  53: 68 */     this.awayScore = awayScore;
/*  54: 69 */     this.date = date;
/*  55: 70 */     this.status = status;
/*  56: 71 */     this.eventNotifSent = 0;
/*  57: 72 */     this.alias = alias;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public String getGameId()
/*  61:    */   {
/*  62: 76 */     return this.gameId;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void setGameId(String gameId)
/*  66:    */   {
/*  67: 80 */     this.gameId = gameId;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public String getCountryName()
/*  71:    */   {
/*  72: 84 */     return this.countryName;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void setCountryName(String countryName)
/*  76:    */   {
/*  77: 88 */     this.countryName = countryName;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public String getLeagueName()
/*  81:    */   {
/*  82: 92 */     return this.leagueName;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void setLeagueName(String leagueName)
/*  86:    */   {
/*  87: 96 */     this.leagueName = leagueName;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public String getHomeTeam()
/*  91:    */   {
/*  92:100 */     return this.homeTeam;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void setHomeTeam(String homeTeam)
/*  96:    */   {
/*  97:104 */     this.homeTeam = homeTeam;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public String getDate()
/* 101:    */   {
/* 102:108 */     return this.date;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public void setDate(String date)
/* 106:    */   {
/* 107:112 */     this.date = date;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public int getStatus()
/* 111:    */   {
/* 112:116 */     return this.status;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public void setStatus(int status)
/* 116:    */   {
/* 117:120 */     this.status = status;
/* 118:    */   }
/* 119:    */   
/* 120:    */   public String getLeagueId()
/* 121:    */   {
/* 122:124 */     return this.leagueId;
/* 123:    */   }
/* 124:    */   
/* 125:    */   public void setLeagueId(String leagueId)
/* 126:    */   {
/* 127:128 */     this.leagueId = leagueId;
/* 128:    */   }
/* 129:    */   
/* 130:    */   public String getAwayTeam()
/* 131:    */   {
/* 132:132 */     return this.awayTeam;
/* 133:    */   }
/* 134:    */   
/* 135:    */   public void setAwayTeam(String awayTeam)
/* 136:    */   {
/* 137:136 */     this.awayTeam = awayTeam;
/* 138:    */   }
/* 139:    */   
/* 140:    */   public String getHomeScore()
/* 141:    */   {
/* 142:140 */     return this.homeScore;
/* 143:    */   }
/* 144:    */   
/* 145:    */   public void setHomeScore(String homeScore)
/* 146:    */   {
/* 147:144 */     this.homeScore = homeScore;
/* 148:    */   }
/* 149:    */   
/* 150:    */   public String getAwayScore()
/* 151:    */   {
/* 152:148 */     return this.awayScore;
/* 153:    */   }
/* 154:    */   
/* 155:    */   public void setAwayScore(String awayScore)
/* 156:    */   {
/* 157:152 */     this.awayScore = awayScore;
/* 158:    */   }
/* 159:    */   
/* 160:    */   public String getAlias()
/* 161:    */   {
/* 162:156 */     return this.alias;
/* 163:    */   }
/* 164:    */   
/* 165:    */   public void setAlias(String alias)
/* 166:    */   {
/* 167:160 */     this.alias = alias;
/* 168:    */   }
/* 169:    */   
/* 170:    */   public int getEventNotifSent()
/* 171:    */   {
/* 172:164 */     return this.eventNotifSent;
/* 173:    */   }
/* 174:    */   
/* 175:    */   public void setEventNotifSent(int eventNotifSent)
/* 176:    */   {
/* 177:168 */     this.eventNotifSent = eventNotifSent;
/* 178:    */   }
/* 179:    */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.livescore.LiveScoreFixture
 * JD-Core Version:    0.7.0.1
 */