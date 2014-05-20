/*   1:    */ package com.rancard.mobility.infoserver.livescore.serviceinterfaces;
/*   2:    */ 
/*   3:    */ import com.rancard.mobility.common.Driver;
/*   4:    */ import com.rancard.mobility.common.PushDriver;
/*   5:    */ import com.rancard.mobility.contentserver.CPConnections;
/*   6:    */ import com.rancard.mobility.infoserver.common.services.ServiceManager;
/*   7:    */ import com.rancard.mobility.infoserver.common.services.UserService;
/*   8:    */ import com.rancard.mobility.infoserver.livescore.LiveScoreFixture;
/*   9:    */ import com.rancard.mobility.infoserver.livescore.LiveScoreServiceManager;
/*  10:    */ import com.rancard.util.PropertyHolder;
/*  11:    */ import com.rancard.util.URLUTF8Encoder;
/*  12:    */ import java.io.IOException;
/*  13:    */ import java.io.PrintStream;
/*  14:    */ import java.sql.Timestamp;
/*  15:    */ import java.util.ArrayList;
/*  16:    */ import java.util.Calendar;
/*  17:    */ import java.util.HashMap;
/*  18:    */ import java.util.Set;
/*  19:    */ import javax.servlet.ServletException;
/*  20:    */ import javax.servlet.http.HttpServlet;
/*  21:    */ import javax.servlet.http.HttpServletRequest;
/*  22:    */ import javax.servlet.http.HttpServletResponse;
/*  23:    */ 
/*  24:    */ public class sendeventnotification
/*  25:    */   extends HttpServlet
/*  26:    */ {
/*  27:    */   private static final String CONTENT_TYPE = "text/html";
/*  28:    */   
/*  29:    */   public void init()
/*  30:    */     throws ServletException
/*  31:    */   {}
/*  32:    */   
/*  33:    */   public void doGet(HttpServletRequest request, HttpServletResponse response)
/*  34:    */     throws ServletException, IOException
/*  35:    */   {
/*  36: 33 */     String allowance = request.getParameter("allowance");
/*  37:    */     
/*  38: 35 */     int DEFAULT_ALLOWANCE = 0;
/*  39:    */     try
/*  40:    */     {
/*  41: 37 */       String value = PropertyHolder.getPropsValue("LS_TRIGGER_ALLOWANCE");
/*  42: 38 */       if ((value != null) && (!value.equals(""))) {
/*  43: 39 */         DEFAULT_ALLOWANCE = Integer.parseInt(value);
/*  44:    */       } else {
/*  45: 41 */         DEFAULT_ALLOWANCE = 6;
/*  46:    */       }
/*  47:    */     }
/*  48:    */     catch (Exception e)
/*  49:    */     {
/*  50: 44 */       DEFAULT_ALLOWANCE = 6;
/*  51:    */     }
/*  52: 47 */     int addition = 0;
/*  53: 48 */     if (allowance == null) {
/*  54: 49 */       addition = DEFAULT_ALLOWANCE;
/*  55:    */     } else {
/*  56: 51 */       addition = Integer.parseInt(allowance);
/*  57:    */     }
/*  58: 54 */     Calendar calendar = Calendar.getInstance();
/*  59: 55 */     Calendar now = Calendar.getInstance();
/*  60: 56 */     System.out.println(new java.util.Date() + ":Event notification for games today. Triggered at " + calendar.getTime().toString());
/*  61: 57 */     System.out.println(new java.util.Date() + ":Trigger allowance: " + addition);
/*  62: 58 */     calendar.add(11, addition);
/*  63: 59 */     System.out.println(new java.util.Date() + ":Looking for games around " + calendar.getTime().toString());
/*  64: 60 */     Timestamp today = new Timestamp(calendar.getTime().getTime());
/*  65: 61 */     String dateString = today.toString();
/*  66: 62 */     CPConnections cnxn = new CPConnections();
/*  67:    */     
/*  68:    */ 
/*  69: 65 */     System.out.println(new java.util.Date() + ":fixture_DateTime_Search_value: " + dateString);
/*  70:    */     try
/*  71:    */     {
/*  72: 70 */       HashMap fixtures = LiveScoreServiceManager.viewAllFixturesForDate(dateString, 0);
/*  73: 71 */       Object[] keys = fixtures.keySet().toArray();
/*  74:    */       
/*  75:    */ 
/*  76: 74 */       HashMap cpFixtureMatrix = LiveScoreServiceManager.viewAllFixturesForDateGroupByCP(dateString, 0, 0);
/*  77: 77 */       if (keys.length < 1) {
/*  78: 78 */         System.out.println(new java.util.Date() + ":No games were found around this time!");
/*  79:    */       }
/*  80: 80 */       for (int i = 0; i < keys.length; i++)
/*  81:    */       {
/*  82: 81 */         String key = (String)keys[i];
/*  83: 82 */         ArrayList fixturesList = (ArrayList)fixtures.get(key);
/*  84: 83 */         if (fixturesList.size() != 0)
/*  85:    */         {
/*  86: 84 */           System.out.println(new java.util.Date() + ":" + fixturesList.size() + " game(s) found for " + key + ".");
/*  87:    */           
/*  88: 86 */           System.out.println(new java.util.Date() + ":getting CP-LS_Keyword Mappings...");
/*  89: 87 */           ArrayList pairing = LiveScoreServiceManager.getAccount_KeywordPairForService(key);
/*  90: 88 */           for (int j = 0; j < pairing.size(); j++)
/*  91:    */           {
/*  92: 89 */             String messageTemplate = "";
/*  93: 90 */             String from = "";
/*  94:    */             
/*  95: 92 */             String pair = (String)pairing.get(j);
/*  96: 93 */             String accountId = pair.split("-")[0];
/*  97: 94 */             String kw = pair.split("-")[1];
/*  98:    */             
/*  99:    */ 
/* 100: 97 */             System.out.println(new java.util.Date() + ":CP-LS_Keyword-Mapping: " + accountId + ":" + kw);
/* 101:    */             
/* 102: 99 */             ArrayList cpFixtureBasket = (ArrayList)cpFixtureMatrix.get(accountId);
/* 103:100 */             ArrayList toSendList = new ArrayList();
/* 104:    */             
/* 105:    */ 
/* 106:103 */             boolean fixtExist = false;
/* 107:104 */             for (int k = 0; k < fixturesList.size(); k++)
/* 108:    */             {
/* 109:105 */               LiveScoreFixture game = (LiveScoreFixture)fixturesList.get(k);
/* 110:108 */               if (game.getEventNotifSent() != 1) {
/* 111:112 */                 if ((cpFixtureBasket != null) && (cpFixtureBasket.contains(game.getGameId())))
/* 112:    */                 {
/* 113:114 */                   toSendList.add(game);
/* 114:115 */                   fixtExist = true;
/* 115:    */                 }
/* 116:    */               }
/* 117:    */             }
/* 118:121 */             if (fixtExist)
/* 119:    */             {
/* 120:125 */               UserService us = new UserService();
/* 121:    */               try
/* 122:    */               {
/* 123:127 */                 us = ServiceManager.viewService(kw, accountId);
/* 124:    */               }
/* 125:    */               catch (Exception e)
/* 126:    */               {
/* 127:129 */                 System.out.println(new java.util.Date() + ":Default message not found for keyword: " + kw + " and account ID: " + accountId + ". Using hard-coded template...");
/* 128:130 */                 messageTemplate = "Send @@gameId@@ to 406 for @@homeTeam@@ vs @@awayTeam@@ on @@date@@";
/* 129:    */               }
/* 130:133 */               UserService lsHeadSrvc = new UserService();
/* 131:    */               try
/* 132:    */               {
/* 133:135 */                 lsHeadSrvc = LiveScoreServiceManager.viewHeadLiveScoreService(accountId);
/* 134:136 */                 from = lsHeadSrvc.getAllowedShortcodes();
/* 135:    */               }
/* 136:    */               catch (Exception e)
/* 137:    */               {
/* 138:138 */                 from = "";
/* 139:    */               }
/* 140:141 */               messageTemplate = us.getDefaultMessage();
/* 141:    */               
/* 142:    */ 
/* 143:    */ 
/* 144:    */ 
/* 145:    */ 
/* 146:    */ 
/* 147:148 */               HashMap groupedSubscribers = ServiceManager.viewSubscribersGroupByNetworkPrefix(accountId, kw, 1);
/* 148:149 */               Object[] subkeys = groupedSubscribers.keySet().toArray();
/* 149:150 */               for (int l = 0; l < subkeys.length; l++)
/* 150:    */               {
/* 151:151 */                 String[] subscribers = (String[])groupedSubscribers.get(subkeys[l].toString());
/* 152:152 */                 if ((subscribers != null) && (subscribers.length > 0))
/* 153:    */                 {
/* 154:    */                   try
/* 155:    */                   {
/* 156:155 */                     cnxn = CPConnections.getConnection(accountId, subscribers[0]);
/* 157:    */                   }
/* 158:    */                   catch (Exception e)
/* 159:    */                   {
/* 160:159 */                     throw new Exception("8002");
/* 161:    */                   }
/* 162:163 */                   String message = "";
/* 163:165 */                   for (int k = 0; k < toSendList.size(); k++)
/* 164:    */                   {
/* 165:166 */                     LiveScoreFixture game = (LiveScoreFixture)toSendList.get(k);
/* 166:    */                     
/* 167:168 */                     String gameIdentifier = game.getAlias();
/* 168:169 */                     if ((gameIdentifier == null) || (gameIdentifier.equals(""))) {
/* 169:170 */                       gameIdentifier = game.getGameId();
/* 170:    */                     }
/* 171:179 */                     String gameTime = com.rancard.util.Date.toLocalTime(new java.util.Date(Timestamp.valueOf(game.getDate()).getTime()), cnxn);
/* 172:    */                     
/* 173:181 */                     System.out.println(new java.util.Date() + ":gameIdentifier: " + gameIdentifier + ", network:" + subkeys[l] + " game_time:" + gameTime);
/* 174:    */                     
/* 175:183 */                     String insertions = "gameId=" + gameIdentifier.toUpperCase() + "&homeTeam=" + game.getHomeTeam() + "&awayTeam=" + game.getAwayTeam() + "&date=" + gameTime;
/* 176:    */                     
/* 177:185 */                     message = message + URLUTF8Encoder.doMessageEscaping(insertions, messageTemplate) + ". ";
/* 178:    */                   }
/* 179:    */                   try
/* 180:    */                   {
/* 181:192 */                     Driver.getDriver(cnxn.getDriverType(), cnxn.getGatewayURL()).sendSMSTextMessage(subscribers, from, message, cnxn.getUsername(), cnxn.getPassword(), cnxn.getConnection(), "", "0");
/* 182:    */                   }
/* 183:    */                   catch (Exception e)
/* 184:    */                   {
/* 185:195 */                     System.out.println(new java.util.Date() + "Error sending notification: Feedback.TRANSPORT_ERROR:" + e.getMessage());
/* 186:196 */                     throw new Exception("5002");
/* 187:    */                   }
/* 188:    */                 }
/* 189:    */                 else
/* 190:    */                 {
/* 191:199 */                   System.out.println(new java.util.Date() + ":No subscribers found for " + subkeys[l]);
/* 192:    */                 }
/* 193:    */               }
/* 194:    */             }
/* 195:    */             else
/* 196:    */             {
/* 197:203 */               System.out.println(new java.util.Date() + ":No fixture for league(" + kw + ") found in send_notif Basket for current CP:" + accountId);
/* 198:    */             }
/* 199:    */           }
/* 200:    */         }
/* 201:211 */         for (int k = 0; k < fixturesList.size(); k++)
/* 202:    */         {
/* 203:212 */           LiveScoreFixture game = (LiveScoreFixture)fixturesList.get(k);
/* 204:213 */           game.setEventNotifSent(1);
/* 205:214 */           LiveScoreServiceManager.updateFixture(game);
/* 206:    */         }
/* 207:    */       }
/* 208:    */     }
/* 209:    */     catch (Exception e)
/* 210:    */     {
/* 211:219 */       e = 
/* 212:    */       
/* 213:    */ 
/* 214:222 */         e;System.out.println(e.getMessage());
/* 215:    */     }
/* 216:    */     finally {}
/* 217:    */   }
/* 218:    */   
/* 219:    */   public void doPost(HttpServletRequest request, HttpServletResponse response)
/* 220:    */     throws ServletException, IOException
/* 221:    */   {
/* 222:227 */     doGet(request, response);
/* 223:    */   }
/* 224:    */   
/* 225:    */   public void destroy() {}
/* 226:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.livescore.serviceinterfaces.sendeventnotification
 * JD-Core Version:    0.7.0.1
 */