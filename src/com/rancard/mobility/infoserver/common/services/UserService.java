/*   1:    */ package com.rancard.mobility.infoserver.common.services;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ 
/*   5:    */ public class UserService
/*   6:    */   implements Serializable
/*   7:    */ {
/*   8:    */   private boolean isBasic;
/*   9:    */   private boolean isSubscription;
/*  10:    */   private String serviceType;
/*  11:    */   private String keyword;
/*  12:    */   private String accountId;
/*  13:    */   private String serviceName;
/*  14:    */   private String defaultMessage;
/*  15:    */   private String lastUpdated;
/*  16:    */   private String command;
/*  17:    */   private String allowedShortcodes;
/*  18:    */   private String allowedSiteTypes;
/*  19:    */   private String pricing;
/*  20:    */   private String serviceResponseSender;
/*  21:    */   
/*  22:    */   public UserService()
/*  23:    */   {
/*  24: 22 */     this.isBasic = false;
/*  25: 23 */     this.isSubscription = false;
/*  26: 24 */     this.serviceType = "";
/*  27: 25 */     this.keyword = "";
/*  28: 26 */     this.accountId = "";
/*  29: 27 */     this.serviceName = "";
/*  30: 28 */     this.defaultMessage = "";
/*  31: 29 */     this.command = "";
/*  32: 30 */     this.allowedShortcodes = "";
/*  33: 31 */     this.allowedSiteTypes = "";
/*  34:    */     
/*  35: 33 */     this.pricing = "";
/*  36: 34 */     this.serviceResponseSender = "";
/*  37:    */   }
/*  38:    */   
/*  39:    */   public UserService(String serviceType, String keyword, String accountId, String serviceid, String defaultMessage)
/*  40:    */   {
/*  41: 38 */     this.serviceType = serviceType;
/*  42: 39 */     this.keyword = keyword;
/*  43: 40 */     this.accountId = accountId;
/*  44: 41 */     this.serviceName = serviceid;
/*  45: 42 */     this.defaultMessage = defaultMessage;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public UserService(String serviceType, String keyword, String accountId, String serviceid, String defaultMessage, String command, String allowedShortcodes, String allowedSiteTypes, String pricing, boolean isBasic)
/*  49:    */   {
/*  50: 47 */     this.isBasic = isBasic;
/*  51:    */     
/*  52: 49 */     this.serviceType = serviceType;
/*  53: 50 */     this.keyword = keyword;
/*  54: 51 */     this.accountId = accountId;
/*  55: 52 */     this.serviceName = serviceid;
/*  56: 53 */     this.defaultMessage = defaultMessage;
/*  57: 54 */     this.command = command;
/*  58: 55 */     this.allowedShortcodes = allowedShortcodes;
/*  59: 56 */     this.allowedSiteTypes = allowedSiteTypes;
/*  60: 57 */     this.pricing = pricing;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public UserService(String serviceType, String keyword, String accountId, String serviceid, String defaultMessage, String command, String allowedShortcodes, String allowedSiteTypes, String pricing, boolean isBasic, boolean isSubscription, String serviceReponseSender)
/*  64:    */   {
/*  65: 62 */     this.isBasic = isBasic;
/*  66: 63 */     this.isSubscription = isSubscription;
/*  67: 64 */     this.serviceType = serviceType;
/*  68: 65 */     this.keyword = keyword;
/*  69: 66 */     this.accountId = accountId;
/*  70: 67 */     this.serviceName = serviceid;
/*  71: 68 */     this.defaultMessage = defaultMessage;
/*  72: 69 */     this.command = command;
/*  73: 70 */     this.allowedShortcodes = allowedShortcodes;
/*  74: 71 */     this.allowedSiteTypes = allowedSiteTypes;
/*  75: 72 */     this.pricing = pricing;
/*  76: 73 */     this.serviceResponseSender = serviceReponseSender;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public String getAccountId()
/*  80:    */   {
/*  81: 77 */     return this.accountId;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public String getDefaultMessage()
/*  85:    */   {
/*  86: 81 */     return this.defaultMessage;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public String getKeyword()
/*  90:    */   {
/*  91: 85 */     return this.keyword;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public String getServiceType()
/*  95:    */   {
/*  96: 89 */     return this.serviceType;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public String getServiceName()
/* 100:    */   {
/* 101: 93 */     return this.serviceName;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public void setAccountId(String accountId)
/* 105:    */   {
/* 106: 97 */     this.accountId = accountId;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public void setDefaultMessage(String defaultMessage)
/* 110:    */   {
/* 111:101 */     this.defaultMessage = defaultMessage;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public void setKeyword(String keyword)
/* 115:    */   {
/* 116:105 */     this.keyword = keyword;
/* 117:    */   }
/* 118:    */   
/* 119:    */   public void setServiceType(String serviceName)
/* 120:    */   {
/* 121:109 */     this.serviceType = serviceName;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public void setServiceName(String serviceName)
/* 125:    */   {
/* 126:113 */     this.serviceName = serviceName;
/* 127:    */   }
/* 128:    */   
/* 129:    */   public boolean equals(Object elem)
/* 130:    */   {
/* 131:118 */     if ((elem instanceof UserService)) {
/* 132:119 */       return false;
/* 133:    */     }
/* 134:122 */     UserService e = (UserService)elem;
/* 135:123 */     if ((this.accountId.equals(e.getAccountId())) && (this.keyword.equals(e.getKeyword()))) {
/* 136:124 */       return true;
/* 137:    */     }
/* 138:126 */     return false;
/* 139:    */   }
/* 140:    */   
/* 141:    */   public int hashCode()
/* 142:    */   {
/* 143:132 */     int hash = 3;
/* 144:133 */     hash = 41 * hash + (this.keyword != null ? this.keyword.hashCode() : 0);
/* 145:134 */     hash = 41 * hash + (this.accountId != null ? this.accountId.hashCode() : 0);
/* 146:135 */     return hash;
/* 147:    */   }
/* 148:    */   
/* 149:    */   public boolean exists()
/* 150:    */   {
/* 151:142 */     boolean userExists = false;
/* 152:143 */     if ((getKeyword() != null) && (getAccountId() != null)) {
/* 153:    */       try
/* 154:    */       {
/* 155:146 */         UserService serv = UserServiceDB.viewService(getKeyword(), getAccountId());
/* 156:148 */         if ((serv != null) && (serv.getAccountId() != null) && (serv.getAccountId().equals(this.accountId)))
/* 157:    */         {
/* 158:150 */           setAccountId(serv.getAccountId());
/* 159:151 */           this.serviceName = serv.getServiceName();
/* 160:152 */           this.serviceType = serv.getServiceType();
/* 161:153 */           this.keyword = serv.getKeyword();
/* 162:154 */           userExists = true;
/* 163:    */         }
/* 164:    */       }
/* 165:    */       catch (Exception ex) {}
/* 166:    */     }
/* 167:162 */     return userExists;
/* 168:    */   }
/* 169:    */   
/* 170:    */   public String getLastUpdated()
/* 171:    */   {
/* 172:166 */     return this.lastUpdated;
/* 173:    */   }
/* 174:    */   
/* 175:    */   public void setLastUpdated(String lastUpdated)
/* 176:    */   {
/* 177:170 */     this.lastUpdated = lastUpdated;
/* 178:    */   }
/* 179:    */   
/* 180:    */   public String getCommand()
/* 181:    */   {
/* 182:174 */     return this.command;
/* 183:    */   }
/* 184:    */   
/* 185:    */   public void setCommand(String command)
/* 186:    */   {
/* 187:178 */     this.command = command;
/* 188:    */   }
/* 189:    */   
/* 190:    */   public String getAllowedShortcodes()
/* 191:    */   {
/* 192:182 */     return this.allowedShortcodes;
/* 193:    */   }
/* 194:    */   
/* 195:    */   public void setAllowedShortcodes(String allowedShortcodes)
/* 196:    */   {
/* 197:186 */     this.allowedShortcodes = allowedShortcodes;
/* 198:    */   }
/* 199:    */   
/* 200:    */   public String getAllowedSiteTypes()
/* 201:    */   {
/* 202:190 */     return this.allowedSiteTypes;
/* 203:    */   }
/* 204:    */   
/* 205:    */   public void setAllowedSiteTypes(String allowedSiteTypes)
/* 206:    */   {
/* 207:194 */     this.allowedSiteTypes = allowedSiteTypes;
/* 208:    */   }
/* 209:    */   
/* 210:    */   public boolean isBasic()
/* 211:    */   {
/* 212:198 */     return this.isBasic;
/* 213:    */   }
/* 214:    */   
/* 215:    */   public void setIsBasic(boolean isBasic)
/* 216:    */   {
/* 217:202 */     this.isBasic = isBasic;
/* 218:    */   }
/* 219:    */   
/* 220:    */   public boolean isSubscription()
/* 221:    */   {
/* 222:206 */     return this.isSubscription;
/* 223:    */   }
/* 224:    */   
/* 225:    */   public void setIsSubscription(boolean isSubscription)
/* 226:    */   {
/* 227:210 */     this.isSubscription = isSubscription;
/* 228:    */   }
/* 229:    */   
/* 230:    */   public String getServiceResponseSender()
/* 231:    */   {
/* 232:214 */     return this.serviceResponseSender;
/* 233:    */   }
/* 234:    */   
/* 235:    */   public void setServiceResponseSender(String serviceResponseSender)
/* 236:    */   {
/* 237:218 */     this.serviceResponseSender = serviceResponseSender;
/* 238:    */   }
/* 239:    */   
/* 240:    */   public String getPricing()
/* 241:    */   {
/* 242:222 */     return this.pricing;
/* 243:    */   }
/* 244:    */   
/* 245:    */   public void setPricing(String pricing)
/* 246:    */   {
/* 247:226 */     this.pricing = pricing;
/* 248:    */   }
/* 249:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.infoserver.common.services.UserService
 * JD-Core Version:    0.7.0.1
 */