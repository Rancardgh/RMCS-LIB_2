/*   1:    */ package com.rancard.mobility.infoserver.viralmarketing;
/*   2:    */ 
/*   3:    */ import com.rancard.mobility.common.ThreadedMessageSender;
/*   4:    */ import com.rancard.mobility.contentserver.CPConnections;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.Iterator;
/*   7:    */ import java.util.regex.Matcher;
/*   8:    */ import java.util.regex.Pattern;
/*   9:    */ 
/*  10:    */ public class VMServiceManager
/*  11:    */ {
/*  12:    */   public static void createUser(String msisdn, String accountId, String keyword, String username, int points)
/*  13:    */     throws Exception
/*  14:    */   {
/*  15: 19 */     VMUser user = new VMUser(msisdn, accountId, keyword, username, points);
/*  16: 20 */     VMUserDB.createUser(user);
/*  17:    */   }
/*  18:    */   
/*  19:    */   public static VMUser viewUser(String keyword, String accountId, String msisdn)
/*  20:    */     throws Exception
/*  21:    */   {
/*  22: 25 */     return VMUserDB.viewUser(keyword, accountId, msisdn);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public static void addPoints(String keyword, String accountId, String msisdn, int point)
/*  26:    */     throws Exception
/*  27:    */   {
/*  28: 30 */     VMUserDB.addPoints(keyword, accountId, msisdn, point);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public static void createTransaction(String campaignId, String recruiterMsisdn, String recipientMsisdn, String status)
/*  32:    */     throws Exception
/*  33:    */   {
/*  34: 35 */     VMTransaction transaction = new VMTransaction(campaignId, recruiterMsisdn, recipientMsisdn, status);
/*  35: 36 */     VMTransactionDB.createTransaction(transaction);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public static VMTransaction viewTransaction(String campaignId, String recipientMsisdn)
/*  39:    */     throws Exception
/*  40:    */   {
/*  41: 41 */     return VMTransactionDB.viewTransaction(campaignId, recipientMsisdn);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public static VMTransaction viewTransaction(String accountId, String keyword, String recipientMsisdn)
/*  45:    */     throws Exception
/*  46:    */   {
/*  47: 46 */     return VMTransactionDB.viewTransaction(accountId, keyword, recipientMsisdn);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public static void updateTransactionStatus(String campaignId, String recipientMsisdn, String status)
/*  51:    */     throws Exception
/*  52:    */   {
/*  53: 51 */     VMTransactionDB.updateTransactionStatus(campaignId, recipientMsisdn, status);
/*  54:    */   }
/*  55:    */   
/*  56:    */   public static void updateTransactionStatus(String campaignId, String recipientMsisdn, String status, int points)
/*  57:    */     throws Exception
/*  58:    */   {
/*  59: 56 */     VMTransactionDB.updateTransactionStatus(campaignId, recipientMsisdn, status, points);
/*  60:    */   }
/*  61:    */   
/*  62:    */   public static void createCampaign(String campaignId, String accountId, String keyword, String messageSender, String message)
/*  63:    */     throws Exception
/*  64:    */   {
/*  65: 61 */     VMCampaign campaign = new VMCampaign(campaignId, accountId, keyword, messageSender, message);
/*  66: 62 */     VMCampaignDB.createCampaign(campaign);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public static VMCampaign viewCampaign(String campaignId)
/*  70:    */     throws Exception
/*  71:    */   {
/*  72: 67 */     return VMCampaignDB.viewCampaign(campaignId);
/*  73:    */   }
/*  74:    */   
/*  75:    */   public static VMCampaign viewCampaign(String accountId, String keyword)
/*  76:    */     throws Exception
/*  77:    */   {
/*  78: 72 */     return VMCampaignDB.viewCampaign(accountId, keyword);
/*  79:    */   }
/*  80:    */   
/*  81:    */   public static void updateCampaignMessage(String campaignId, String message)
/*  82:    */     throws Exception
/*  83:    */   {
/*  84: 77 */     VMCampaignDB.updateCampaignMessage(campaignId, message);
/*  85:    */   }
/*  86:    */   
/*  87:    */   public static void updateCampaign(VMCampaign campaign, String update_accountId, String update_keyword)
/*  88:    */     throws Exception
/*  89:    */   {
/*  90: 81 */     VMCampaignDB.updateCampaign(campaign, update_accountId, update_keyword);
/*  91:    */   }
/*  92:    */   
/*  93:    */   public static String sendInvitation(String campaignId, String recipList, String recipRegex, String recipUnifiedPrefix, String senderUnifiedPrefix, int numSignificantDigits, String msisdn, boolean useMsisdnAsSender)
/*  94:    */     throws Exception
/*  95:    */   {
/*  96: 88 */     String message = "";
/*  97:    */     
/*  98: 90 */     VMCampaign campaign = viewCampaign(campaignId);
/*  99: 91 */     String campaign_message = campaign.getMessage();
/* 100: 92 */     String campaign_keyword = campaign.getKeyword();
/* 101: 93 */     String campaign_account_id = campaign.getAccountId();
/* 102: 94 */     String campaign_message_sender = "";
/* 103: 95 */     if (useMsisdnAsSender) {
/* 104: 96 */       campaign_message_sender = normalizeNumber(msisdn, senderUnifiedPrefix, numSignificantDigits);
/* 105:    */     } else {
/* 106: 98 */       campaign_message_sender = campaign.getMessageSender();
/* 107:    */     }
/* 108:101 */     Pattern pattern = Pattern.compile(recipRegex);
/* 109:102 */     ArrayList<String> formattedNumbers = new ArrayList();
/* 110:    */     
/* 111:    */ 
/* 112:105 */     createUser(msisdn, campaign_account_id, campaign_keyword, "", 0);
/* 113:    */     
/* 114:107 */     String[] rawNumbers = recipList.split(" ");
/* 115:108 */     for (int i = 0; i < rawNumbers.length; i++)
/* 116:    */     {
/* 117:109 */       String recipient = rawNumbers[i];
/* 118:110 */       Matcher matcher = pattern.matcher(recipient);
/* 119:111 */       if (matcher.matches())
/* 120:    */       {
/* 121:112 */         String formattedRecipient = normalizeNumber(recipient, recipUnifiedPrefix, numSignificantDigits);
/* 122:115 */         if (!msisdn.equalsIgnoreCase(formattedRecipient)) {
/* 123:117 */           if (viewTransaction(campaignId, formattedRecipient).isEmptyTransaction())
/* 124:    */           {
/* 125:119 */             formattedNumbers.add(formattedRecipient);
/* 126:120 */             createTransaction(campaignId, msisdn, formattedRecipient, "inv_sent");
/* 127:    */           }
/* 128:    */         }
/* 129:    */       }
/* 130:    */     }
/* 131:131 */     CPConnections cnxn = new CPConnections();
/* 132:132 */     cnxn = CPConnections.getConnection(campaign_account_id, msisdn);
/* 133:    */     
/* 134:134 */     Iterator<String> subList = formattedNumbers.iterator();
/* 135:137 */     while (subList.hasNext()) {
/* 136:138 */       new Thread(new ThreadedMessageSender(cnxn, (String)subList.next(), campaign_message_sender, campaign_message, 0)).start();
/* 137:    */     }
/* 138:142 */     if (formattedNumbers.size() > 0) {
/* 139:143 */       message = "Your invitation has been sent! Invite another friend and increase your chances of winning 100 cedis in airtime";
/* 140:    */     } else {
/* 141:145 */       message = "Sorry, your invitation couldn't be sent. Your recipients may have already been invited. Also please ensure the numbers you submitted are correctly formatted";
/* 142:    */     }
/* 143:148 */     return message;
/* 144:    */   }
/* 145:    */   
/* 146:    */   public static String sendInvitation(String accountId, String keyword, String recipList, String recipRegex, String recipUnifiedPrefix, String senderUnifiedPrefix, int numSignificantDigits, String msisdn, boolean useMsisdnAsSender)
/* 147:    */     throws Exception
/* 148:    */   {
/* 149:155 */     String message = "";
/* 150:    */     
/* 151:157 */     VMCampaign campaign = viewCampaign(accountId, keyword);
/* 152:158 */     String campaign_message = campaign.getMessage();
/* 153:159 */     String campaign_keyword = campaign.getKeyword();
/* 154:160 */     String campaign_account_id = campaign.getAccountId();
/* 155:161 */     String campaign_message_sender = "";
/* 156:162 */     String campaignId = campaign.getCampaignId();
/* 157:163 */     if (useMsisdnAsSender) {
/* 158:164 */       campaign_message_sender = normalizeNumber(msisdn, senderUnifiedPrefix, numSignificantDigits);
/* 159:    */     } else {
/* 160:166 */       campaign_message_sender = campaign.getMessageSender();
/* 161:    */     }
/* 162:169 */     String formattedRecipient = "";
/* 163:170 */     ArrayList<String> formattedNumbers = new ArrayList();
/* 164:172 */     if ((campaignId != null) && (!campaignId.equals("")))
/* 165:    */     {
/* 166:173 */       Pattern pattern = Pattern.compile(recipRegex);
/* 167:    */       
/* 168:    */ 
/* 169:176 */       createUser(msisdn, campaign_account_id, campaign_keyword, "", 0);
/* 170:    */       
/* 171:178 */       String[] rawNumbers = recipList.split(" ");
/* 172:179 */       for (int i = 0; i < rawNumbers.length; i++)
/* 173:    */       {
/* 174:180 */         String recipient = rawNumbers[i];
/* 175:181 */         Matcher matcher = pattern.matcher(recipient);
/* 176:182 */         if (matcher.matches())
/* 177:    */         {
/* 178:183 */           formattedRecipient = normalizeNumber(recipient, recipUnifiedPrefix, numSignificantDigits);
/* 179:186 */           if (!msisdn.equalsIgnoreCase(formattedRecipient)) {
/* 180:188 */             if (viewTransaction(campaignId, formattedRecipient).isEmptyTransaction())
/* 181:    */             {
/* 182:190 */               formattedNumbers.add(formattedRecipient);
/* 183:191 */               createTransaction(campaignId, msisdn, formattedRecipient, "inv_sent");
/* 184:    */             }
/* 185:    */           }
/* 186:    */         }
/* 187:    */       }
/* 188:    */     }
/* 189:    */     else
/* 190:    */     {
/* 191:201 */       message = "Sorry, your invitation couldn't be sent at this time.";
/* 192:    */     }
/* 193:205 */     CPConnections cnxn = new CPConnections();
/* 194:206 */     cnxn = CPConnections.getConnection(campaign_account_id, msisdn);
/* 195:    */     
/* 196:208 */     Iterator<String> subList = formattedNumbers.iterator();
/* 197:211 */     while (subList.hasNext()) {
/* 198:212 */       new Thread(new ThreadedMessageSender(cnxn, (String)subList.next(), campaign_message_sender, campaign_message, 0)).start();
/* 199:    */     }
/* 200:216 */     if (formattedNumbers.size() > 0) {
/* 201:217 */       message = "Your invitation has been sent! Invite another friend and increase your chances of winning 100 cedis in airtime";
/* 202:    */     } else {
/* 203:219 */       message = "Sorry, your invitation couldn't be sent. Your recipients may have already been invited. Also please ensure the numbers you submitted are correctly formatted";
/* 204:    */     }
/* 205:222 */     return message;
/* 206:    */   }
/* 207:    */   
/* 208:    */   public static String sendInvitation(String accountId, String keyword, String recipList, String recipRegex, String recipUnifiedPrefix, String senderUnifiedPrefix, int numSignificantDigits, String msisdn, boolean useMsisdnAsSender, String usersName)
/* 209:    */     throws Exception
/* 210:    */   {
/* 211:229 */     String message = "";
/* 212:    */     
/* 213:231 */     VMCampaign campaign = viewCampaign(accountId, keyword);
/* 214:232 */     String campaign_message = campaign.getMessage();
/* 215:234 */     if ((campaign.getMessage() + " -" + usersName).length() > 160)
/* 216:    */     {
/* 217:235 */       String temp_name = "";
/* 218:236 */       String[] name = usersName.split(" ");
/* 219:237 */       if (name != null) {
/* 220:239 */         for (int i = name.length - 1; i > 0; i--)
/* 221:    */         {
/* 222:240 */           temp_name = usersName.substring(0, usersName.indexOf(name[i]));
/* 223:241 */           if (((campaign_message + " -" + temp_name).length() < 160) && (temp_name.length() > 0))
/* 224:    */           {
/* 225:242 */             campaign_message = (campaign_message + " -" + temp_name).trim();
/* 226:243 */             break;
/* 227:    */           }
/* 228:    */         }
/* 229:    */       }
/* 230:    */     }
/* 231:    */     else
/* 232:    */     {
/* 233:248 */       campaign_message = campaign_message + " -" + usersName;
/* 234:    */     }
/* 235:250 */     String campaign_keyword = campaign.getKeyword();
/* 236:251 */     String campaign_account_id = campaign.getAccountId();
/* 237:252 */     String campaign_message_sender = "";
/* 238:253 */     String campaignId = campaign.getCampaignId();
/* 239:254 */     if (useMsisdnAsSender) {
/* 240:255 */       campaign_message_sender = normalizeNumber(msisdn, senderUnifiedPrefix, numSignificantDigits);
/* 241:    */     } else {
/* 242:257 */       campaign_message_sender = campaign.getMessageSender();
/* 243:    */     }
/* 244:260 */     String formattedRecipient = "";
/* 245:261 */     ArrayList<String> formattedNumbers = new ArrayList();
/* 246:263 */     if ((campaignId != null) && (!campaignId.equals("")))
/* 247:    */     {
/* 248:264 */       Pattern pattern = Pattern.compile(recipRegex);
/* 249:    */       
/* 250:    */ 
/* 251:267 */       createUser(msisdn, campaign_account_id, campaign_keyword, "", 0);
/* 252:    */       
/* 253:269 */       String[] rawNumbers = recipList.split(" ");
/* 254:270 */       for (int i = 0; i < rawNumbers.length; i++)
/* 255:    */       {
/* 256:271 */         String recipient = rawNumbers[i];
/* 257:272 */         Matcher matcher = pattern.matcher(recipient);
/* 258:273 */         if (matcher.matches())
/* 259:    */         {
/* 260:274 */           formattedRecipient = normalizeNumber(recipient, recipUnifiedPrefix, numSignificantDigits);
/* 261:277 */           if (!msisdn.equalsIgnoreCase(formattedRecipient)) {
/* 262:279 */             if (viewTransaction(campaignId, formattedRecipient).isEmptyTransaction())
/* 263:    */             {
/* 264:281 */               formattedNumbers.add(formattedRecipient);
/* 265:282 */               createTransaction(campaignId, msisdn, formattedRecipient, "inv_sent");
/* 266:    */             }
/* 267:    */           }
/* 268:    */         }
/* 269:    */       }
/* 270:    */     }
/* 271:    */     else
/* 272:    */     {
/* 273:292 */       message = "Sorry, your invitation couldn't be sent at this time.";
/* 274:    */     }
/* 275:296 */     CPConnections cnxn = new CPConnections();
/* 276:297 */     cnxn = CPConnections.getConnection(campaign_account_id, msisdn);
/* 277:    */     
/* 278:299 */     Iterator<String> subList = formattedNumbers.iterator();
/* 279:302 */     while (subList.hasNext()) {
/* 280:303 */       new Thread(new ThreadedMessageSender(cnxn, (String)subList.next(), campaign_message_sender, campaign_message, 0)).start();
/* 281:    */     }
/* 282:307 */     if (formattedNumbers.size() > 0) {
/* 283:308 */       message = "Your invitation has been sent! Invite another friend and increase your chances of winning 100 cedis in airtime";
/* 284:    */     } else {
/* 285:310 */       message = "Sorry, your invitation couldn't be sent. Your recipients may have already been invited. Also please ensure the numbers you submitted are correctly formatted";
/* 286:    */     }
/* 287:313 */     return message;
/* 288:    */   }
/* 289:    */   
/* 290:    */   private static String normalizeNumber(String msisdn, String unifiedPrefix, int numDigits)
/* 291:    */     throws Exception
/* 292:    */   {
/* 293:318 */     if (numDigits > msisdn.length()) {
/* 294:319 */       throw new Exception("Cannot normalize number. Invalid num_digits parameter passed");
/* 295:    */     }
/* 296:322 */     return unifiedPrefix + msisdn.substring(msisdn.length() - numDigits, msisdn.length());
/* 297:    */   }
/* 298:    */   
/* 299:    */   public static PromoImpression viewPromoImpression(long hashCode)
/* 300:    */     throws Exception
/* 301:    */   {
/* 302:328 */     return PromoImpressionDB.viewPromoImpression(hashCode);
/* 303:    */   }
/* 304:    */   
/* 305:    */   public static void createPromoImpression(PromoImpression promoImpression)
/* 306:    */     throws Exception
/* 307:    */   {
/* 308:333 */     PromoImpressionDB.createEntry(promoImpression);
/* 309:    */   }
/* 310:    */   
/* 311:    */   public static void updateAdRespSummary(PromoImpression promoImpression)
/* 312:    */     throws Exception
/* 313:    */   {
/* 314:338 */     PromoImpressionDB.updateAdResponseSummary(promoImpression);
/* 315:    */   }
/* 316:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.viralmarketing.VMServiceManager
 * JD-Core Version:    0.7.0.1
 */