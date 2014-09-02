/*   1:    */ package com.rancard.mobility.infoserver.viralmarketing;
/*   2:    */ 
/*   3:    */ import com.rancard.common.ThreadedMessageSender;
/*   4:    */ import com.rancard.mobility.contentserver.CPConnections;
/*   5:    */
/*   6:    */ import java.util.ArrayList;
/*   7:    */ import java.util.Iterator;
/*   8:    */ import java.util.regex.Matcher;
/*   9:    */ import java.util.regex.Pattern;
/*  10:    */ 
/*  11:    */ public class VMServiceManager
/*  12:    */ {
/*  13:    */   public static void createUser(String msisdn, String accountId, String keyword, String username, int points)
/*  14:    */     throws Exception
/*  15:    */   {
/*  16: 19 */     VMUser user = new VMUser(msisdn, accountId, keyword, username, points);
/*  17: 20 */     VMUserDB.createUser(user);
/*  18:    */   }
/*  19:    */   
/*  20:    */   public static VMUser viewUser(String keyword, String accountId, String msisdn)
/*  21:    */     throws Exception
/*  22:    */   {
/*  23: 25 */     return VMUserDB.viewUser(keyword, accountId, msisdn);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public static void addPoints(String keyword, String accountId, String msisdn, int point)
/*  27:    */     throws Exception
/*  28:    */   {
/*  29: 30 */     VMUserDB.addPoints(keyword, accountId, msisdn, point);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public static void createTransaction(String campaignId, String recruiterMsisdn, String recipientMsisdn, String status)
/*  33:    */     throws Exception
/*  34:    */   {
/*  35: 35 */     VMTransaction transaction = new VMTransaction(campaignId, recruiterMsisdn, recipientMsisdn, status);
/*  36: 36 */     VMTransactionDB.createTransaction(transaction);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public static VMTransaction viewTransaction(String campaignId, String recipientMsisdn)
/*  40:    */     throws Exception
/*  41:    */   {
/*  42: 41 */     return VMTransactionDB.viewTransaction(campaignId, recipientMsisdn);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public static VMTransaction viewTransaction(String accountId, String keyword, String recipientMsisdn)
/*  46:    */     throws Exception
/*  47:    */   {
/*  48: 46 */     return VMTransactionDB.viewTransaction(accountId, keyword, recipientMsisdn);
/*  49:    */   }
/*  50:    */   
/*  51:    */   public static void updateTransactionStatus(String campaignId, String recipientMsisdn, String status)
/*  52:    */     throws Exception
/*  53:    */   {
/*  54: 51 */     VMTransactionDB.updateTransactionStatus(campaignId, recipientMsisdn, status);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public static void updateTransactionStatus(String campaignId, String recipientMsisdn, String status, int points)
/*  58:    */     throws Exception
/*  59:    */   {
/*  60: 56 */     VMTransactionDB.updateTransactionStatus(campaignId, recipientMsisdn, status, points);
/*  61:    */   }
/*  62:    */   
/*  63:    */   public static void createCampaign(String campaignId, String accountId, String keyword, String messageSender, String message)
/*  64:    */     throws Exception
/*  65:    */   {
/*  66: 61 */     VMCampaign campaign = new VMCampaign(campaignId, accountId, keyword, messageSender, message);
/*  67: 62 */     VMCampaignDB.createCampaign(campaign);
/*  68:    */   }
/*  69:    */   
/*  70:    */   public static VMCampaign viewCampaign(String campaignId)
/*  71:    */     throws Exception
/*  72:    */   {
/*  73: 67 */     return VMCampaignDB.viewCampaign(campaignId);
/*  74:    */   }
/*  75:    */   
/*  76:    */   public static VMCampaign viewCampaign(String accountId, String keyword)
/*  77:    */     throws Exception
/*  78:    */   {
/*  79: 72 */     return VMCampaignDB.viewCampaign(accountId, keyword);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public static void updateCampaignMessage(String campaignId, String message)
/*  83:    */     throws Exception
/*  84:    */   {
/*  85: 77 */     VMCampaignDB.updateCampaignMessage(campaignId, message);
/*  86:    */   }
/*  87:    */   
/*  88:    */   public static void updateCampaign(VMCampaign campaign, String update_accountId, String update_keyword)
/*  89:    */     throws Exception
/*  90:    */   {
/*  91: 81 */     VMCampaignDB.updateCampaign(campaign, update_accountId, update_keyword);
/*  92:    */   }
/*  93:    */   
/*  94:    */   public static String sendInvitation(String campaignId, String recipList, String recipRegex, String recipUnifiedPrefix, String senderUnifiedPrefix, int numSignificantDigits, String msisdn, boolean useMsisdnAsSender)
/*  95:    */     throws Exception
/*  96:    */   {
/*  97: 88 */     String message = "";
/*  98:    */     
/*  99: 90 */     VMCampaign campaign = viewCampaign(campaignId);
/* 100: 91 */     String campaign_message = campaign.getMessage();
/* 101: 92 */     String campaign_keyword = campaign.getKeyword();
/* 102: 93 */     String campaign_account_id = campaign.getAccountId();
/* 103: 94 */     String campaign_message_sender = "";
/* 104: 95 */     if (useMsisdnAsSender) {
/* 105: 96 */       campaign_message_sender = normalizeNumber(msisdn, senderUnifiedPrefix, numSignificantDigits);
/* 106:    */     } else {
/* 107: 98 */       campaign_message_sender = campaign.getMessageSender();
/* 108:    */     }
/* 109:101 */     Pattern pattern = Pattern.compile(recipRegex);
/* 110:102 */     ArrayList<String> formattedNumbers = new ArrayList();
/* 111:    */     
/* 112:    */ 
/* 113:105 */     createUser(msisdn, campaign_account_id, campaign_keyword, "", 0);
/* 114:    */     
/* 115:107 */     String[] rawNumbers = recipList.split(" ");
/* 116:108 */     for (int i = 0; i < rawNumbers.length; i++)
/* 117:    */     {
/* 118:109 */       String recipient = rawNumbers[i];
/* 119:110 */       Matcher matcher = pattern.matcher(recipient);
/* 120:111 */       if (matcher.matches())
/* 121:    */       {
/* 122:112 */         String formattedRecipient = normalizeNumber(recipient, recipUnifiedPrefix, numSignificantDigits);
/* 123:115 */         if (!msisdn.equalsIgnoreCase(formattedRecipient)) {
/* 124:117 */           if (viewTransaction(campaignId, formattedRecipient).isEmptyTransaction())
/* 125:    */           {
/* 126:119 */             formattedNumbers.add(formattedRecipient);
/* 127:120 */             createTransaction(campaignId, msisdn, formattedRecipient, "inv_sent");
/* 128:    */           }
/* 129:    */         }
/* 130:    */       }
/* 131:    */     }
/* 132:131 */     CPConnections cnxn = new CPConnections();
/* 133:132 */     cnxn = CPConnections.getConnection(campaign_account_id, msisdn);
/* 134:    */     
/* 135:134 */     Iterator<String> subList = formattedNumbers.iterator();
/* 136:137 */     while (subList.hasNext()) {
/* 137:138 */       new Thread(new ThreadedMessageSender(cnxn, (String)subList.next(), campaign_message_sender, campaign_message, 0)).start();
/* 138:    */     }
/* 139:142 */     if (formattedNumbers.size() > 0) {
/* 140:143 */       message = "Your invitation has been sent! Invite another friend and increase your chances of winning 100 cedis in airtime";
/* 141:    */     } else {
/* 142:145 */       message = "Sorry, your invitation couldn't be sent. Your recipients may have already been invited. Also please ensure the numbers you submitted are correctly formatted";
/* 143:    */     }
/* 144:148 */     return message;
/* 145:    */   }
/* 146:    */   
/* 147:    */   public static String sendInvitation(String accountId, String keyword, String recipList, String recipRegex, String recipUnifiedPrefix, String senderUnifiedPrefix, int numSignificantDigits, String msisdn, boolean useMsisdnAsSender)
/* 148:    */     throws Exception
/* 149:    */   {
/* 150:155 */     String message = "";
/* 151:    */     
/* 152:157 */     VMCampaign campaign = viewCampaign(accountId, keyword);
/* 153:158 */     String campaign_message = campaign.getMessage();
/* 154:159 */     String campaign_keyword = campaign.getKeyword();
/* 155:160 */     String campaign_account_id = campaign.getAccountId();
/* 156:161 */     String campaign_message_sender = "";
/* 157:162 */     String campaignId = campaign.getCampaignId();
/* 158:163 */     if (useMsisdnAsSender) {
/* 159:164 */       campaign_message_sender = normalizeNumber(msisdn, senderUnifiedPrefix, numSignificantDigits);
/* 160:    */     } else {
/* 161:166 */       campaign_message_sender = campaign.getMessageSender();
/* 162:    */     }
/* 163:169 */     String formattedRecipient = "";
/* 164:170 */     ArrayList<String> formattedNumbers = new ArrayList();
/* 165:172 */     if ((campaignId != null) && (!campaignId.equals("")))
/* 166:    */     {
/* 167:173 */       Pattern pattern = Pattern.compile(recipRegex);
/* 168:    */       
/* 169:    */ 
/* 170:176 */       createUser(msisdn, campaign_account_id, campaign_keyword, "", 0);
/* 171:    */       
/* 172:178 */       String[] rawNumbers = recipList.split(" ");
/* 173:179 */       for (int i = 0; i < rawNumbers.length; i++)
/* 174:    */       {
/* 175:180 */         String recipient = rawNumbers[i];
/* 176:181 */         Matcher matcher = pattern.matcher(recipient);
/* 177:182 */         if (matcher.matches())
/* 178:    */         {
/* 179:183 */           formattedRecipient = normalizeNumber(recipient, recipUnifiedPrefix, numSignificantDigits);
/* 180:186 */           if (!msisdn.equalsIgnoreCase(formattedRecipient)) {
/* 181:188 */             if (viewTransaction(campaignId, formattedRecipient).isEmptyTransaction())
/* 182:    */             {
/* 183:190 */               formattedNumbers.add(formattedRecipient);
/* 184:191 */               createTransaction(campaignId, msisdn, formattedRecipient, "inv_sent");
/* 185:    */             }
/* 186:    */           }
/* 187:    */         }
/* 188:    */       }
/* 189:    */     }
/* 190:    */     else
/* 191:    */     {
/* 192:201 */       message = "Sorry, your invitation couldn't be sent at this time.";
/* 193:    */     }
/* 194:205 */     CPConnections cnxn = new CPConnections();
/* 195:206 */     cnxn = CPConnections.getConnection(campaign_account_id, msisdn);
/* 196:    */     
/* 197:208 */     Iterator<String> subList = formattedNumbers.iterator();
/* 198:211 */     while (subList.hasNext()) {
/* 199:212 */       new Thread(new ThreadedMessageSender(cnxn, (String)subList.next(), campaign_message_sender, campaign_message, 0)).start();
/* 200:    */     }
/* 201:216 */     if (formattedNumbers.size() > 0) {
/* 202:217 */       message = "Your invitation has been sent! Invite another friend and increase your chances of winning 100 cedis in airtime";
/* 203:    */     } else {
/* 204:219 */       message = "Sorry, your invitation couldn't be sent. Your recipients may have already been invited. Also please ensure the numbers you submitted are correctly formatted";
/* 205:    */     }
/* 206:222 */     return message;
/* 207:    */   }
/* 208:    */   
/* 209:    */   public static String sendInvitation(String accountId, String keyword, String recipList, String recipRegex, String recipUnifiedPrefix, String senderUnifiedPrefix, int numSignificantDigits, String msisdn, boolean useMsisdnAsSender, String usersName)
/* 210:    */     throws Exception
/* 211:    */   {
/* 212:229 */     String message = "";
/* 213:    */     
/* 214:231 */     VMCampaign campaign = null;
/* 215:232 */     String campaign_message = "";
/* 216:    */     try
/* 217:    */     {
/* 218:234 */       campaign = viewCampaign(accountId, keyword);
/* 219:235 */       campaign_message = campaign.getMessage();
/* 220:    */     }
/* 221:    */     catch (Exception e)
/* 222:    */     {
/* 223:237 */       message = "Sorry, your invitation couldn't be sent at this time.";
/* 224:238 */       System.out.println("Problem getting campaign. Probably related to the database");
/* 225:239 */       throw new Exception();
/* 226:    */     }
/* 227:242 */     if ((campaign.getMessage() + " -" + usersName).length() > 160)
/* 228:    */     {
/* 229:243 */       String temp_name = "";
/* 230:244 */       String[] name = usersName.split(" ");
/* 231:245 */       if (name != null) {
/* 232:247 */         for (int i = name.length - 1; i > 0; i--)
/* 233:    */         {
/* 234:248 */           temp_name = usersName.substring(0, usersName.indexOf(name[i]));
/* 235:249 */           if (((campaign_message + " -" + temp_name).length() < 160) && (temp_name.length() > 0))
/* 236:    */           {
/* 237:250 */             campaign_message = (campaign_message + " -" + temp_name).trim();
/* 238:251 */             break;
/* 239:    */           }
/* 240:    */         }
/* 241:    */       }
/* 242:    */     }
/* 243:    */     else
/* 244:    */     {
/* 245:256 */       campaign_message = campaign_message + " -" + usersName;
/* 246:    */     }
/* 247:258 */     String campaign_keyword = campaign.getKeyword();
/* 248:259 */     String campaign_account_id = campaign.getAccountId();
/* 249:260 */     String campaign_message_sender = "";
/* 250:261 */     String campaignId = campaign.getCampaignId();
/* 251:262 */     if (useMsisdnAsSender) {
/* 252:263 */       campaign_message_sender = normalizeNumber(msisdn, senderUnifiedPrefix, numSignificantDigits);
/* 253:    */     } else {
/* 254:265 */       campaign_message_sender = campaign.getMessageSender();
/* 255:    */     }
/* 256:268 */     String formattedRecipient = "";
/* 257:269 */     ArrayList<String> formattedNumbers = new ArrayList();
/* 258:271 */     if ((campaignId != null) && (!campaignId.equals("")))
/* 259:    */     {
/* 260:272 */       Pattern pattern = Pattern.compile(recipRegex);
/* 261:    */       
/* 262:    */ 
/* 263:275 */       createUser(msisdn, campaign_account_id, campaign_keyword, "", 0);
/* 264:    */       
/* 265:277 */       String[] rawNumbers = recipList.split(" ");
/* 266:278 */       for (int i = 0; i < rawNumbers.length; i++)
/* 267:    */       {
/* 268:279 */         String recipient = rawNumbers[i];
/* 269:280 */         Matcher matcher = pattern.matcher(recipient);
/* 270:281 */         if (matcher.matches())
/* 271:    */         {
/* 272:282 */           formattedRecipient = normalizeNumber(recipient, recipUnifiedPrefix, numSignificantDigits);
/* 273:285 */           if (!msisdn.equalsIgnoreCase(formattedRecipient)) {
/* 274:287 */             if (viewTransaction(campaignId, formattedRecipient).isEmptyTransaction())
/* 275:    */             {
/* 276:289 */               formattedNumbers.add(formattedRecipient);
/* 277:290 */               createTransaction(campaignId, msisdn, formattedRecipient, "inv_sent");
/* 278:    */             }
/* 279:    */           }
/* 280:    */         }
/* 281:    */       }
/* 282:    */     }
/* 283:    */     else
/* 284:    */     {
/* 285:300 */       System.out.println("Campaign ID is null or empty");
/* 286:301 */       message = "Sorry, your invitation couldn't be sent at this time.";
/* 287:    */     }
/* 288:306 */     CPConnections cnxn = CPConnections.getConnection(campaign_account_id, msisdn);
/* 289:    */     
/* 290:308 */     Iterator<String> subList = formattedNumbers.iterator();
/* 291:311 */     while (subList.hasNext()) {
/* 292:312 */       new Thread(new ThreadedMessageSender(cnxn, (String)subList.next(), campaign_message_sender, campaign_message, 0)).start();
/* 293:    */     }
/* 294:316 */     if (formattedNumbers.size() > 0) {
/* 295:317 */       message = "Your invitation has been sent! Invite another friend and increase your chances of winning 100 cedis in airtime";
/* 296:    */     } else {
/* 297:319 */       message = "Sorry, your invitation couldn't be sent. Your recipients may have already been invited. Also please ensure the numbers you submitted are correctly formatted";
/* 298:    */     }
/* 299:322 */     return message;
/* 300:    */   }
/* 301:    */   
/* 302:    */   private static String normalizeNumber(String msisdn, String unifiedPrefix, int numDigits)
/* 303:    */     throws Exception
/* 304:    */   {
/* 305:327 */     if (numDigits > msisdn.length()) {
/* 306:328 */       throw new Exception("Cannot normalize number. Invalid num_digits parameter passed");
/* 307:    */     }
/* 308:331 */     return unifiedPrefix + msisdn.substring(msisdn.length() - numDigits, msisdn.length());
/* 309:    */   }
/* 310:    */   
/* 311:    */   public static PromoImpression viewPromoImpression(long hashCode)
/* 312:    */     throws Exception
/* 313:    */   {
/* 314:337 */     return PromoImpressionDB.viewPromoImpression(hashCode);
/* 315:    */   }
/* 316:    */   
/* 317:    */   public static PromoImpression viewPromoImpression(String msisdn, String keyword, String accountID)
/* 318:    */     throws Exception
/* 319:    */   {
/* 320:342 */     return PromoImpressionDB.viewPromoImpression(msisdn, keyword, accountID);
/* 321:    */   }
/* 322:    */   
/* 323:    */   public static void updatePromoViewDate(PromoImpression impression)
/* 324:    */     throws Exception
/* 325:    */   {
/* 326:346 */     PromoImpressionDB.updatePromoViewDate(impression);
/* 327:    */   }
/* 328:    */   
/* 329:    */   public static void createPromoImpression(PromoImpression promoImpression)
/* 330:    */     throws Exception
/* 331:    */   {
/* 332:351 */     PromoImpressionDB.createEntry(promoImpression);
/* 333:    */   }
/* 334:    */   
/* 335:    */   public static void updateAdRespSummary(PromoImpression promoImpression)
/* 336:    */     throws Exception
/* 337:    */   {
/* 338:356 */     PromoImpressionDB.updateAdResponseSummary(promoImpression);
/* 339:    */   }
/* 340:    */ }



/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar

 * Qualified Name:     com.rancard.mobility.infoserver.viralmarketing.VMServiceManager

 * JD-Core Version:    0.7.0.1

 */