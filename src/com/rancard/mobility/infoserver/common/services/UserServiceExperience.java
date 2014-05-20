/*   1:    */ package com.rancard.mobility.infoserver.common.services;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.Map;
/*   7:    */ 
/*   8:    */ public class UserServiceExperience
/*   9:    */ {
/*  10:    */   private String accountId;
/*  11:    */   private String siteId;
/*  12:    */   private String keyword;
/*  13:    */   private String promoMsg;
/*  14:    */   private String promoId;
/*  15:    */   private String promoRespCode;
/*  16:    */   private String welcomeMsg;
/*  17:    */   private String alreadySubscribedMsg;
/*  18:    */   private String unsubscriptionConfirmationMsg;
/*  19:    */   private String promoMsgSender;
/*  20:    */   private String welcomeMsgSender;
/*  21:    */   private String alreadySubscribedMsgSender;
/*  22:    */   private String unsubscriptionConfirmationMsgSender;
/*  23:    */   private int pushMsgWaitTime;
/*  24:    */   private int subscriptionInterval;
/*  25:    */   private String url;
/*  26:    */   private int urlTimeout;
/*  27:    */   private String metaData;
/*  28:    */   private static final int DEFAULT_URL_CALL_TIMEOUT = 5000;
/*  29:    */   
/*  30:    */   public UserServiceExperience(String accountID, String siteID, String keyword, String promoMsg, String promoID, String promoRespCode, String welcomeMsg, String alreadySubscribedMsg, String unsubscriptionConfirmationMsg, String promoMsgSender, String welcomeMsgSender, String alreadySubscribedMsgSender, String unsubscriptionConfirmationMsgSender, int pushMsgWaitTime, int subscriptionInterval, String url, int urlTimeout, String metaData)
/*  31:    */   {
/*  32: 31 */     this.accountId = accountID;
/*  33: 32 */     this.siteId = siteID;
/*  34: 33 */     this.keyword = keyword;
/*  35: 34 */     this.promoMsg = promoMsg;
/*  36: 35 */     this.promoId = promoID;
/*  37: 36 */     this.promoRespCode = promoRespCode;
/*  38: 37 */     this.welcomeMsg = welcomeMsg;
/*  39: 38 */     this.alreadySubscribedMsg = alreadySubscribedMsg;
/*  40: 39 */     this.unsubscriptionConfirmationMsg = unsubscriptionConfirmationMsg;
/*  41: 40 */     this.promoMsgSender = promoMsgSender;
/*  42: 41 */     this.welcomeMsgSender = welcomeMsgSender;
/*  43: 42 */     this.alreadySubscribedMsgSender = alreadySubscribedMsgSender;
/*  44: 43 */     this.unsubscriptionConfirmationMsgSender = unsubscriptionConfirmationMsgSender;
/*  45: 44 */     this.pushMsgWaitTime = pushMsgWaitTime;
/*  46: 45 */     this.subscriptionInterval = subscriptionInterval;
/*  47: 46 */     this.url = url;
/*  48: 47 */     this.urlTimeout = urlTimeout;
/*  49: 48 */     this.metaData = metaData;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public UserServiceExperience()
/*  53:    */   {
/*  54: 53 */     this.accountId = "";
/*  55: 54 */     this.siteId = "";
/*  56: 55 */     this.keyword = "";
/*  57: 56 */     this.promoMsg = "";
/*  58: 57 */     this.promoId = "";
/*  59: 58 */     this.promoRespCode = "";
/*  60: 59 */     this.welcomeMsg = "";
/*  61: 60 */     this.alreadySubscribedMsg = "";
/*  62: 61 */     this.unsubscriptionConfirmationMsg = "";
/*  63: 62 */     this.promoMsgSender = "";
/*  64: 63 */     this.welcomeMsgSender = "";
/*  65: 64 */     this.alreadySubscribedMsgSender = "";
/*  66: 65 */     this.unsubscriptionConfirmationMsgSender = "";
/*  67: 66 */     this.pushMsgWaitTime = 0;
/*  68: 67 */     this.subscriptionInterval = 0;
/*  69: 68 */     this.url = "";
/*  70: 69 */     this.urlTimeout = 5000;
/*  71: 70 */     this.metaData = "";
/*  72:    */   }
/*  73:    */   
/*  74:    */   public String getAccountId()
/*  75:    */   {
/*  76: 74 */     return this.accountId;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public String getSiteId()
/*  80:    */   {
/*  81: 78 */     return this.siteId;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public String getKeyword()
/*  85:    */   {
/*  86: 82 */     return this.keyword;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public String getPromoMsg()
/*  90:    */   {
/*  91: 86 */     return this.promoMsg;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public String getPromoId()
/*  95:    */   {
/*  96: 90 */     return this.promoId;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public String getPromoRespCode()
/* 100:    */   {
/* 101: 94 */     return this.promoRespCode;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public String getWelcomeMsg()
/* 105:    */   {
/* 106: 98 */     return this.welcomeMsg;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public String getAlreadySubscribedMsg()
/* 110:    */   {
/* 111:102 */     return this.alreadySubscribedMsg;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public String getUnsubscriptionConfirmationMsg()
/* 115:    */   {
/* 116:106 */     return this.unsubscriptionConfirmationMsg;
/* 117:    */   }
/* 118:    */   
/* 119:    */   public String getPromoMsgSender()
/* 120:    */   {
/* 121:110 */     return this.promoMsgSender;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public String getWelcomeMsgSender()
/* 125:    */   {
/* 126:114 */     return this.welcomeMsgSender;
/* 127:    */   }
/* 128:    */   
/* 129:    */   public String getAlreadySubscribedMsgSender()
/* 130:    */   {
/* 131:118 */     return this.alreadySubscribedMsgSender;
/* 132:    */   }
/* 133:    */   
/* 134:    */   public String getUnsubscriptionConfirmationMsgSender()
/* 135:    */   {
/* 136:122 */     return this.unsubscriptionConfirmationMsgSender;
/* 137:    */   }
/* 138:    */   
/* 139:    */   public int getPushMsgWaitTime()
/* 140:    */   {
/* 141:126 */     return this.pushMsgWaitTime;
/* 142:    */   }
/* 143:    */   
/* 144:    */   public int getSubscriptionInterval()
/* 145:    */   {
/* 146:130 */     return this.subscriptionInterval;
/* 147:    */   }
/* 148:    */   
/* 149:    */   public String getUrl()
/* 150:    */   {
/* 151:134 */     return this.url;
/* 152:    */   }
/* 153:    */   
/* 154:    */   public int getUrlTimeout()
/* 155:    */   {
/* 156:138 */     return this.urlTimeout;
/* 157:    */   }
/* 158:    */   
/* 159:    */   public String getMetaData()
/* 160:    */   {
/* 161:142 */     return this.metaData;
/* 162:    */   }
/* 163:    */   
/* 164:    */   public void setAccountId(String accountId)
/* 165:    */   {
/* 166:146 */     this.accountId = accountId;
/* 167:    */   }
/* 168:    */   
/* 169:    */   public void setSiteId(String siteId)
/* 170:    */   {
/* 171:150 */     this.siteId = siteId;
/* 172:    */   }
/* 173:    */   
/* 174:    */   public void setKeyword(String keyword)
/* 175:    */   {
/* 176:154 */     this.keyword = keyword;
/* 177:    */   }
/* 178:    */   
/* 179:    */   public void setPromoMsg(String promoMsg)
/* 180:    */   {
/* 181:158 */     this.promoMsg = promoMsg;
/* 182:    */   }
/* 183:    */   
/* 184:    */   public void setPromoId(String promoId)
/* 185:    */   {
/* 186:162 */     this.promoId = promoId;
/* 187:    */   }
/* 188:    */   
/* 189:    */   public void setPromoRespCode(String promoRespCode)
/* 190:    */   {
/* 191:166 */     this.promoRespCode = promoRespCode;
/* 192:    */   }
/* 193:    */   
/* 194:    */   public void setWelcomeMsg(String welcomeMsg)
/* 195:    */   {
/* 196:170 */     this.welcomeMsg = welcomeMsg;
/* 197:    */   }
/* 198:    */   
/* 199:    */   public void setAlreadySubscribedMsg(String alreadySubscribedMsg)
/* 200:    */   {
/* 201:174 */     this.alreadySubscribedMsg = alreadySubscribedMsg;
/* 202:    */   }
/* 203:    */   
/* 204:    */   public void setUnsubscriptionConfirmationMsg(String unsubscriptionConfirmationMsg)
/* 205:    */   {
/* 206:178 */     this.unsubscriptionConfirmationMsg = unsubscriptionConfirmationMsg;
/* 207:    */   }
/* 208:    */   
/* 209:    */   public void setPromoMsgSender(String promoMsgSender)
/* 210:    */   {
/* 211:182 */     this.promoMsgSender = promoMsgSender;
/* 212:    */   }
/* 213:    */   
/* 214:    */   public void setWelcomeMsgSender(String welcomeMsgSender)
/* 215:    */   {
/* 216:186 */     this.welcomeMsgSender = welcomeMsgSender;
/* 217:    */   }
/* 218:    */   
/* 219:    */   public void setAlreadySubscribedMsgSender(String alreadySubscribedMsgSender)
/* 220:    */   {
/* 221:190 */     this.alreadySubscribedMsgSender = alreadySubscribedMsgSender;
/* 222:    */   }
/* 223:    */   
/* 224:    */   public void setUnsubscriptionConfirmationMsgSender(String unsubscriptionConfirmationMsgSender)
/* 225:    */   {
/* 226:194 */     this.unsubscriptionConfirmationMsgSender = unsubscriptionConfirmationMsgSender;
/* 227:    */   }
/* 228:    */   
/* 229:    */   public void setPushMsgWaitTime(int pushMsgWaitTime)
/* 230:    */   {
/* 231:198 */     this.pushMsgWaitTime = pushMsgWaitTime;
/* 232:    */   }
/* 233:    */   
/* 234:    */   public void setSubscriptionInterval(int subscriptionInterval)
/* 235:    */   {
/* 236:202 */     this.subscriptionInterval = subscriptionInterval;
/* 237:    */   }
/* 238:    */   
/* 239:    */   public void setUrl(String url)
/* 240:    */   {
/* 241:206 */     this.url = url;
/* 242:    */   }
/* 243:    */   
/* 244:    */   public void setUrlTimeout(int urlTimeout)
/* 245:    */   {
/* 246:210 */     this.urlTimeout = urlTimeout;
/* 247:    */   }
/* 248:    */   
/* 249:    */   public void setMetaData(String metaData)
/* 250:    */   {
/* 251:214 */     this.metaData = metaData;
/* 252:    */   }
/* 253:    */   
/* 254:    */   public boolean exists()
/* 255:    */   {
/* 256:218 */     if ((getAccountId().equals("")) || (getSiteId().equals("")) || (getKeyword().equals(""))) {
/* 257:220 */       return false;
/* 258:    */     }
/* 259:222 */     return true;
/* 260:    */   }
/* 261:    */   
/* 262:    */   public Map<String, String> getMetaDataMap()
/* 263:    */   {
/* 264:226 */     HashMap<String, String> mdMap = new HashMap();
/* 265:228 */     if (!this.metaData.equals(""))
/* 266:    */     {
/* 267:229 */       ArrayList<String> mDataPairs = new ArrayList();
/* 268:230 */       mDataPairs.addAll(Arrays.asList(this.metaData.split("&")));
/* 269:232 */       for (String pair : mDataPairs)
/* 270:    */       {
/* 271:233 */         String[] parts = pair.split("=");
/* 272:234 */         mdMap.put(parts[0].toUpperCase(), parts[1]);
/* 273:    */       }
/* 274:    */     }
/* 275:237 */     return mdMap;
/* 276:    */   }
/* 277:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.infoserver.common.services.UserServiceExperience
 * JD-Core Version:    0.7.0.1
 */