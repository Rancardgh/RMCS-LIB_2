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
/*  24: 21 */     this.isBasic = false;
/*  25: 22 */     this.isSubscription = false;
/*  26: 23 */     this.serviceType = "";
/*  27: 24 */     this.keyword = "";
/*  28: 25 */     this.accountId = "";
/*  29: 26 */     this.serviceName = "";
/*  30: 27 */     this.defaultMessage = "";
/*  31: 28 */     this.command = "";
/*  32: 29 */     this.allowedShortcodes = "";
/*  33: 30 */     this.allowedSiteTypes = "";
/*  34:    */     
/*  35: 32 */     this.pricing = "";
/*  36: 33 */     this.serviceResponseSender = "";
/*  37:    */   }
/*  38:    */   
/*  39:    */   public UserService(String serviceType, String keyword, String accountId, String serviceid, String defaultMessage)
/*  40:    */   {
/*  41: 37 */     this.serviceType = serviceType;
/*  42: 38 */     this.keyword = keyword;
/*  43: 39 */     this.accountId = accountId;
/*  44: 40 */     this.serviceName = serviceid;
/*  45: 41 */     this.defaultMessage = defaultMessage;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public UserService(String serviceType, String keyword, String accountId, String serviceid, String defaultMessage, String command, String allowedShortcodes, String allowedSiteTypes, String pricing, boolean isBasic)
/*  49:    */   {
/*  50: 46 */     this.isBasic = isBasic;
/*  51:    */     
/*  52: 48 */     this.serviceType = serviceType;
/*  53: 49 */     this.keyword = keyword;
/*  54: 50 */     this.accountId = accountId;
/*  55: 51 */     this.serviceName = serviceid;
/*  56: 52 */     this.defaultMessage = defaultMessage;
/*  57: 53 */     this.command = command;
/*  58: 54 */     this.allowedShortcodes = allowedShortcodes;
/*  59: 55 */     this.allowedSiteTypes = allowedSiteTypes;
/*  60:    */     
/*  61: 57 */     this.pricing = pricing;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public UserService(String serviceType, String keyword, String accountId, String serviceid, String defaultMessage, String command, String allowedShortcodes, String allowedSiteTypes, String pricing, boolean isBasic, boolean isSubscription, String serviceReponseSender)
/*  65:    */   {
/*  66: 62 */     this.isBasic = isBasic;
/*  67: 63 */     this.isSubscription = isSubscription;
/*  68: 64 */     this.serviceType = serviceType;
/*  69: 65 */     this.keyword = keyword;
/*  70: 66 */     this.accountId = accountId;
/*  71: 67 */     this.serviceName = serviceid;
/*  72: 68 */     this.defaultMessage = defaultMessage;
/*  73: 69 */     this.command = command;
/*  74: 70 */     this.allowedShortcodes = allowedShortcodes;
/*  75: 71 */     this.allowedSiteTypes = allowedSiteTypes;
/*  76:    */     
/*  77: 73 */     this.pricing = pricing;
/*  78: 74 */     this.serviceResponseSender = serviceReponseSender;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public String getAccountId()
/*  82:    */   {
/*  83: 78 */     return this.accountId;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public String getDefaultMessage()
/*  87:    */   {
/*  88: 82 */     return this.defaultMessage;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public String getKeyword()
/*  92:    */   {
/*  93: 86 */     return this.keyword;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public String getServiceType()
/*  97:    */   {
/*  98: 90 */     return this.serviceType;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public String getServiceName()
/* 102:    */   {
/* 103: 94 */     return this.serviceName;
/* 104:    */   }
/* 105:    */   
/* 106:    */   public void setAccountId(String accountId)
/* 107:    */   {
/* 108: 98 */     this.accountId = accountId;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public void setDefaultMessage(String defaultMessage)
/* 112:    */   {
/* 113:102 */     this.defaultMessage = defaultMessage;
/* 114:    */   }
/* 115:    */   
/* 116:    */   public void setKeyword(String keyword)
/* 117:    */   {
/* 118:106 */     this.keyword = keyword;
/* 119:    */   }
/* 120:    */   
/* 121:    */   public void setServiceType(String serviceName)
/* 122:    */   {
/* 123:110 */     this.serviceType = serviceName;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public void setServiceName(String serviceName)
/* 127:    */   {
/* 128:114 */     this.serviceName = serviceName;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public boolean equals(Object elem)
/* 132:    */   {
/* 133:118 */     UserService e = (UserService)elem;
/* 134:120 */     if ((this.accountId.equals(e.getAccountId())) && (this.keyword.equals(e.getKeyword()))) {
/* 135:122 */       return true;
/* 136:    */     }
/* 137:124 */     return false;
/* 138:    */   }
/* 139:    */   
/* 140:    */   public boolean exists()
/* 141:    */   {
/* 142:132 */     boolean userExists = false;
/* 143:133 */     if ((getKeyword() != null) && (getAccountId() != null)) {
/* 144:    */       try
/* 145:    */       {
/* 146:136 */         UserService serv = UserServiceDB.viewService(getKeyword(), getAccountId());
/* 147:138 */         if ((serv != null) && (serv.getAccountId() != null) && (serv.getAccountId().equals(this.accountId)))
/* 148:    */         {
/* 149:140 */           setAccountId(serv.getAccountId());
/* 150:141 */           this.serviceName = serv.getServiceName();
/* 151:142 */           this.serviceType = serv.getServiceType();
/* 152:143 */           this.keyword = serv.getKeyword();
/* 153:144 */           userExists = true;
/* 154:    */         }
/* 155:    */       }
/* 156:    */       catch (Exception ex) {}
/* 157:    */     }
/* 158:152 */     return userExists;
/* 159:    */   }
/* 160:    */   
/* 161:    */   public String getLastUpdated()
/* 162:    */   {
/* 163:156 */     return this.lastUpdated;
/* 164:    */   }
/* 165:    */   
/* 166:    */   public void setLastUpdated(String lastUpdated)
/* 167:    */   {
/* 168:160 */     this.lastUpdated = lastUpdated;
/* 169:    */   }
/* 170:    */   
/* 171:    */   public String getCommand()
/* 172:    */   {
/* 173:164 */     return this.command;
/* 174:    */   }
/* 175:    */   
/* 176:    */   public void setCommand(String command)
/* 177:    */   {
/* 178:168 */     this.command = command;
/* 179:    */   }
/* 180:    */   
/* 181:    */   public String getAllowedShortcodes()
/* 182:    */   {
/* 183:172 */     return this.allowedShortcodes;
/* 184:    */   }
/* 185:    */   
/* 186:    */   public void setAllowedShortcodes(String allowedShortcodes)
/* 187:    */   {
/* 188:176 */     this.allowedShortcodes = allowedShortcodes;
/* 189:    */   }
/* 190:    */   
/* 191:    */   public String getAllowedSiteTypes()
/* 192:    */   {
/* 193:180 */     return this.allowedSiteTypes;
/* 194:    */   }
/* 195:    */   
/* 196:    */   public void setAllowedSiteTypes(String allowedSiteTypes)
/* 197:    */   {
/* 198:184 */     this.allowedSiteTypes = allowedSiteTypes;
/* 199:    */   }
/* 200:    */   
/* 201:    */   public boolean isBasic()
/* 202:    */   {
/* 203:188 */     return this.isBasic;
/* 204:    */   }
/* 205:    */   
/* 206:    */   public void setIsBasic(boolean isBasic)
/* 207:    */   {
/* 208:192 */     this.isBasic = isBasic;
/* 209:    */   }
/* 210:    */   
/* 211:    */   public boolean isSubscription()
/* 212:    */   {
/* 213:196 */     return this.isSubscription;
/* 214:    */   }
/* 215:    */   
/* 216:    */   public void setIsSubscription(boolean isSubscription)
/* 217:    */   {
/* 218:200 */     this.isSubscription = isSubscription;
/* 219:    */   }
/* 220:    */   
/* 221:    */   public String getServiceResponseSender()
/* 222:    */   {
/* 223:204 */     return this.serviceResponseSender;
/* 224:    */   }
/* 225:    */   
/* 226:    */   public void setServiceResponseSender(String serviceResponseSender)
/* 227:    */   {
/* 228:208 */     this.serviceResponseSender = serviceResponseSender;
/* 229:    */   }
/* 230:    */   
/* 231:    */   public String getPricing()
/* 232:    */   {
/* 233:219 */     return this.pricing;
/* 234:    */   }
/* 235:    */   
/* 236:    */   public void setPricing(String pricing)
/* 237:    */   {
/* 238:223 */     this.pricing = pricing;
/* 239:    */   }
/* 240:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.common.services.UserService
 * JD-Core Version:    0.7.0.1
 */