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
/*  30:    */   public UserServiceExperience()
/*  31:    */   {
/*  32: 28 */     this.accountId = "";
/*  33: 29 */     this.siteId = "";
/*  34: 30 */     this.keyword = "";
/*  35: 31 */     this.promoMsg = "";
/*  36: 32 */     this.promoId = "";
/*  37: 33 */     this.promoRespCode = "";
/*  38: 34 */     this.welcomeMsg = "";
/*  39: 35 */     this.alreadySubscribedMsg = "";
/*  40: 36 */     this.unsubscriptionConfirmationMsg = "";
/*  41: 37 */     this.promoMsgSender = "";
/*  42: 38 */     this.welcomeMsgSender = "";
/*  43: 39 */     this.alreadySubscribedMsgSender = "";
/*  44: 40 */     this.unsubscriptionConfirmationMsgSender = "";
/*  45: 41 */     this.pushMsgWaitTime = 0;
/*  46: 42 */     this.subscriptionInterval = 0;
/*  47: 43 */     this.url = "";
/*  48: 44 */     this.urlTimeout = 5000;
/*  49: 45 */     this.metaData = "";
/*  50:    */   }
/*  51:    */   
/*  52:    */   public String getAccountId()
/*  53:    */   {
/*  54: 49 */     return this.accountId;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public String getSiteId()
/*  58:    */   {
/*  59: 53 */     return this.siteId;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public String getKeyword()
/*  63:    */   {
/*  64: 57 */     return this.keyword;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public String getPromoMsg()
/*  68:    */   {
/*  69: 61 */     return this.promoMsg;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public String getPromoId()
/*  73:    */   {
/*  74: 65 */     return this.promoId;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public String getPromoRespCode()
/*  78:    */   {
/*  79: 69 */     return this.promoRespCode;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public String getWelcomeMsg()
/*  83:    */   {
/*  84: 73 */     return this.welcomeMsg;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public String getAlreadySubscribedMsg()
/*  88:    */   {
/*  89: 77 */     return this.alreadySubscribedMsg;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public String getUnsubscriptionConfirmationMsg()
/*  93:    */   {
/*  94: 81 */     return this.unsubscriptionConfirmationMsg;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public String getPromoMsgSender()
/*  98:    */   {
/*  99: 85 */     return this.promoMsgSender;
/* 100:    */   }
/* 101:    */   
/* 102:    */   public String getWelcomeMsgSender()
/* 103:    */   {
/* 104: 89 */     return this.welcomeMsgSender;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public String getAlreadySubscribedMsgSender()
/* 108:    */   {
/* 109: 93 */     return this.alreadySubscribedMsgSender;
/* 110:    */   }
/* 111:    */   
/* 112:    */   public String getUnsubscriptionConfirmationMsgSender()
/* 113:    */   {
/* 114: 97 */     return this.unsubscriptionConfirmationMsgSender;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public int getPushMsgWaitTime()
/* 118:    */   {
/* 119:101 */     return this.pushMsgWaitTime;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public int getSubscriptionInterval()
/* 123:    */   {
/* 124:105 */     return this.subscriptionInterval;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public String getUrl()
/* 128:    */   {
/* 129:109 */     return this.url;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public int getUrlTimeout()
/* 133:    */   {
/* 134:113 */     return this.urlTimeout;
/* 135:    */   }
/* 136:    */   
/* 137:    */   public String getMetaData()
/* 138:    */   {
/* 139:117 */     return this.metaData;
/* 140:    */   }
/* 141:    */   
/* 142:    */   public void setAccountId(String accountId)
/* 143:    */   {
/* 144:121 */     this.accountId = accountId;
/* 145:    */   }
/* 146:    */   
/* 147:    */   public void setSiteId(String siteId)
/* 148:    */   {
/* 149:125 */     this.siteId = siteId;
/* 150:    */   }
/* 151:    */   
/* 152:    */   public void setKeyword(String keyword)
/* 153:    */   {
/* 154:129 */     this.keyword = keyword;
/* 155:    */   }
/* 156:    */   
/* 157:    */   public void setPromoMsg(String promoMsg)
/* 158:    */   {
/* 159:133 */     this.promoMsg = promoMsg;
/* 160:    */   }
/* 161:    */   
/* 162:    */   public void setPromoId(String promoId)
/* 163:    */   {
/* 164:137 */     this.promoId = promoId;
/* 165:    */   }
/* 166:    */   
/* 167:    */   public void setPromoRespCode(String promoRespCode)
/* 168:    */   {
/* 169:141 */     this.promoRespCode = promoRespCode;
/* 170:    */   }
/* 171:    */   
/* 172:    */   public void setWelcomeMsg(String welcomeMsg)
/* 173:    */   {
/* 174:145 */     this.welcomeMsg = welcomeMsg;
/* 175:    */   }
/* 176:    */   
/* 177:    */   public void setAlreadySubscribedMsg(String alreadySubscribedMsg)
/* 178:    */   {
/* 179:149 */     this.alreadySubscribedMsg = alreadySubscribedMsg;
/* 180:    */   }
/* 181:    */   
/* 182:    */   public void setUnsubscriptionConfirmationMsg(String unsubscriptionConfirmationMsg)
/* 183:    */   {
/* 184:153 */     this.unsubscriptionConfirmationMsg = unsubscriptionConfirmationMsg;
/* 185:    */   }
/* 186:    */   
/* 187:    */   public void setPromoMsgSender(String promoMsgSender)
/* 188:    */   {
/* 189:157 */     this.promoMsgSender = promoMsgSender;
/* 190:    */   }
/* 191:    */   
/* 192:    */   public void setWelcomeMsgSender(String welcomeMsgSender)
/* 193:    */   {
/* 194:161 */     this.welcomeMsgSender = welcomeMsgSender;
/* 195:    */   }
/* 196:    */   
/* 197:    */   public void setAlreadySubscribedMsgSender(String alreadySubscribedMsgSender)
/* 198:    */   {
/* 199:165 */     this.alreadySubscribedMsgSender = alreadySubscribedMsgSender;
/* 200:    */   }
/* 201:    */   
/* 202:    */   public void setUnsubscriptionConfirmationMsgSender(String unsubscriptionConfirmationMsgSender)
/* 203:    */   {
/* 204:169 */     this.unsubscriptionConfirmationMsgSender = unsubscriptionConfirmationMsgSender;
/* 205:    */   }
/* 206:    */   
/* 207:    */   public void setPushMsgWaitTime(int pushMsgWaitTime)
/* 208:    */   {
/* 209:173 */     this.pushMsgWaitTime = pushMsgWaitTime;
/* 210:    */   }
/* 211:    */   
/* 212:    */   public void setSubscriptionInterval(int subscriptionInterval)
/* 213:    */   {
/* 214:177 */     this.subscriptionInterval = subscriptionInterval;
/* 215:    */   }
/* 216:    */   
/* 217:    */   public void setUrl(String url)
/* 218:    */   {
/* 219:181 */     this.url = url;
/* 220:    */   }
/* 221:    */   
/* 222:    */   public void setUrlTimeout(int urlTimeout)
/* 223:    */   {
/* 224:185 */     this.urlTimeout = urlTimeout;
/* 225:    */   }
/* 226:    */   
/* 227:    */   public void setMetaData(String metaData)
/* 228:    */   {
/* 229:189 */     this.metaData = metaData;
/* 230:    */   }
/* 231:    */   
/* 232:    */   public boolean exists()
/* 233:    */   {
/* 234:193 */     if ((getAccountId().equals("")) || (getSiteId().equals("")) || (getKeyword().equals(""))) {
/* 235:195 */       return false;
/* 236:    */     }
/* 237:197 */     return true;
/* 238:    */   }
/* 239:    */   
/* 240:    */   public Map<String, String> getMetaDataMap()
/* 241:    */   {
/* 242:201 */     HashMap<String, String> mdMap = new HashMap();
/* 243:203 */     if (!this.metaData.equals(""))
/* 244:    */     {
/* 245:204 */       ArrayList<String> mDataPairs = new ArrayList();
/* 246:205 */       mDataPairs.addAll(Arrays.asList(this.metaData.split("&")));
/* 247:207 */       for (String pair : mDataPairs)
/* 248:    */       {
/* 249:208 */         String[] parts = pair.split("=");
/* 250:209 */         mdMap.put(parts[0].toUpperCase(), parts[1]);
/* 251:    */       }
/* 252:    */     }
/* 253:212 */     return mdMap;
/* 254:    */   }
/* 255:    */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.common.services.UserServiceExperience
 * JD-Core Version:    0.7.0.1
 */