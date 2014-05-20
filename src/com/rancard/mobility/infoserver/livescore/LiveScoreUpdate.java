/*   1:    */ package com.rancard.mobility.infoserver.livescore;
/*   2:    */ 
/*   3:    */ import java.sql.Timestamp;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.Date;
/*   6:    */ 
/*   7:    */ public class LiveScoreUpdate
/*   8:    */ {
/*   9:    */   private String countryName;
/*  10:    */   private String leagueName;
/*  11:    */   private String leagueId;
/*  12:    */   private String eventStatus;
/*  13:    */   private String eventId;
/*  14:    */   private String updateId;
/*  15:    */   private ArrayList participants;
/*  16:    */   private ArrayList scores;
/*  17:    */   private String eventName;
/*  18:    */   private String eventDate;
/*  19:    */   private String trigger;
/*  20:    */   private String englishMessage;
/*  21:    */   private String frenchMessage;
/*  22:    */   private String publishDate;
/*  23:    */   private String mode;
/*  24:    */   
/*  25:    */   public LiveScoreUpdate()
/*  26:    */   {
/*  27: 38 */     this.countryName = "";
/*  28: 39 */     this.leagueName = "";
/*  29: 40 */     this.leagueId = "";
/*  30: 41 */     this.eventStatus = "";
/*  31: 42 */     this.eventId = "";
/*  32: 43 */     this.updateId = "";
/*  33: 44 */     this.eventDate = "";
/*  34: 45 */     this.eventName = "";
/*  35: 46 */     this.trigger = "";
/*  36: 47 */     this.englishMessage = "";
/*  37: 48 */     this.frenchMessage = "";
/*  38: 49 */     this.participants = new ArrayList();
/*  39: 50 */     this.scores = new ArrayList();
/*  40: 51 */     this.publishDate = new Timestamp(new Date().getTime()).toString();
/*  41: 52 */     this.mode = "";
/*  42:    */   }
/*  43:    */   
/*  44:    */   public String getCountryName()
/*  45:    */   {
/*  46: 56 */     return this.countryName;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void setCountryName(String countryName)
/*  50:    */   {
/*  51: 60 */     this.countryName = countryName;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public String getLeagueName()
/*  55:    */   {
/*  56: 64 */     return this.leagueName;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public void setLeagueName(String leagueName)
/*  60:    */   {
/*  61: 68 */     this.leagueName = leagueName;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public String getLeagueId()
/*  65:    */   {
/*  66: 72 */     return this.leagueId;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void setLeagueId(String leagueId)
/*  70:    */   {
/*  71: 76 */     this.leagueId = leagueId;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public String getEventStatus()
/*  75:    */   {
/*  76: 80 */     return this.eventStatus;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void setEventStatus(String eventStatus)
/*  80:    */   {
/*  81: 84 */     this.eventStatus = eventStatus;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public String getTrigger()
/*  85:    */   {
/*  86: 88 */     return this.trigger;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public void setTrigger(String trigger)
/*  90:    */   {
/*  91: 92 */     this.trigger = trigger;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public String getEnglishMessage()
/*  95:    */   {
/*  96: 96 */     return this.englishMessage;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public void setEnglishMessage(String message)
/* 100:    */   {
/* 101:100 */     this.englishMessage = message;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public String getEventName()
/* 105:    */   {
/* 106:104 */     return this.eventName;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public void setEventName(String eventName)
/* 110:    */   {
/* 111:108 */     this.eventName = eventName;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public String getEventDate()
/* 115:    */   {
/* 116:112 */     return this.eventDate;
/* 117:    */   }
/* 118:    */   
/* 119:    */   public void setEventDate(String eventDate)
/* 120:    */   {
/* 121:116 */     this.eventDate = eventDate;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public String getEventId()
/* 125:    */   {
/* 126:120 */     return this.eventId;
/* 127:    */   }
/* 128:    */   
/* 129:    */   public void setEventId(String eventId)
/* 130:    */   {
/* 131:124 */     this.eventId = eventId;
/* 132:    */   }
/* 133:    */   
/* 134:    */   public ArrayList getParticipants()
/* 135:    */   {
/* 136:128 */     return this.participants;
/* 137:    */   }
/* 138:    */   
/* 139:    */   public void setParticipants(ArrayList participants)
/* 140:    */   {
/* 141:132 */     this.participants = participants;
/* 142:    */   }
/* 143:    */   
/* 144:    */   public ArrayList getScores()
/* 145:    */   {
/* 146:136 */     return this.scores;
/* 147:    */   }
/* 148:    */   
/* 149:    */   public void setScores(ArrayList scores)
/* 150:    */   {
/* 151:140 */     this.scores = scores;
/* 152:    */   }
/* 153:    */   
/* 154:    */   public String getUpdateId()
/* 155:    */   {
/* 156:144 */     return this.updateId;
/* 157:    */   }
/* 158:    */   
/* 159:    */   public void setUpdateId(String updateId)
/* 160:    */   {
/* 161:148 */     this.updateId = updateId;
/* 162:    */   }
/* 163:    */   
/* 164:    */   public String getPublishDate()
/* 165:    */   {
/* 166:152 */     return this.publishDate;
/* 167:    */   }
/* 168:    */   
/* 169:    */   public void setPublishDate(String publishDate)
/* 170:    */   {
/* 171:156 */     this.publishDate = publishDate;
/* 172:    */   }
/* 173:    */   
/* 174:    */   public String getFrenchMessage()
/* 175:    */   {
/* 176:160 */     return this.frenchMessage;
/* 177:    */   }
/* 178:    */   
/* 179:    */   public void setFrenchMessage(String message)
/* 180:    */   {
/* 181:164 */     this.frenchMessage = message;
/* 182:    */   }
/* 183:    */   
/* 184:    */   public String getMode()
/* 185:    */   {
/* 186:168 */     return this.mode;
/* 187:    */   }
/* 188:    */   
/* 189:    */   public void setMode(String mode)
/* 190:    */   {
/* 191:172 */     this.mode = mode;
/* 192:    */   }
/* 193:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.infoserver.livescore.LiveScoreUpdate
 * JD-Core Version:    0.7.0.1
 */