/*   1:    */ package com.rancard.mobility.contentserver;
/*   2:    */ 
/*   3:    */ import java.io.PrintStream;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.Date;
/*   6:    */ import java.util.List;
/*   7:    */ import java.util.StringTokenizer;
/*   8:    */ 
/*   9:    */ public class CPConnections
/*  10:    */ {
/*  11: 10 */   private String password = null;
/*  12: 11 */   private String username = null;
/*  13: 12 */   private String connection = null;
/*  14: 13 */   private String gatewayURL = null;
/*  15: 14 */   private String httpMethod = null;
/*  16: 15 */   private String driverType = null;
/*  17: 16 */   private List allowedNetworks = null;
/*  18: 17 */   private String providerId = null;
/*  19: 18 */   private String billingMech = null;
/*  20: 19 */   private String countryName = null;
/*  21: 20 */   private String countryAlias = null;
/*  22: 21 */   private int utcOffset = 0;
/*  23: 22 */   private String language = null;
/*  24:    */   
/*  25:    */   public CPConnections(String password, String username, String connection, String gatewayURL, String httpMethod, String driverType, List allowedNetworks, String providerId, String billingMech, String countryName, String countryAlias, int utcOffset, String language)
/*  26:    */   {
/*  27: 27 */     this.password = password;
/*  28: 28 */     this.username = username;
/*  29: 29 */     this.connection = connection;
/*  30: 30 */     this.gatewayURL = gatewayURL;
/*  31: 31 */     this.httpMethod = httpMethod;
/*  32: 32 */     this.driverType = driverType;
/*  33: 33 */     this.allowedNetworks = allowedNetworks;
/*  34: 34 */     this.providerId = providerId;
/*  35: 35 */     this.billingMech = billingMech;
/*  36: 36 */     this.countryName = countryName;
/*  37: 37 */     this.countryAlias = countryAlias;
/*  38: 38 */     this.utcOffset = utcOffset;
/*  39: 39 */     this.language = language;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public CPConnections()
/*  43:    */   {
/*  44: 43 */     this.password = new String();
/*  45: 44 */     this.username = new String();
/*  46: 45 */     this.connection = new String();
/*  47: 46 */     this.gatewayURL = new String();
/*  48: 47 */     this.httpMethod = new String();
/*  49: 48 */     this.driverType = new String();
/*  50: 49 */     this.allowedNetworks = new ArrayList();
/*  51: 50 */     this.providerId = new String();
/*  52: 51 */     this.billingMech = new String();
/*  53: 52 */     this.countryName = new String();
/*  54: 53 */     this.countryAlias = new String();
/*  55: 54 */     this.utcOffset = 0;
/*  56: 55 */     this.language = new String();
/*  57:    */   }
/*  58:    */   
/*  59:    */   public String getPassword()
/*  60:    */   {
/*  61: 60 */     return this.password;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public String getUsername()
/*  65:    */   {
/*  66: 64 */     return this.username;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public String getConnection()
/*  70:    */   {
/*  71: 68 */     return this.connection;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public String getGatewayURL()
/*  75:    */   {
/*  76: 72 */     return this.gatewayURL;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public String getHttpMethod()
/*  80:    */   {
/*  81: 76 */     return this.httpMethod;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public String getDriverType()
/*  85:    */   {
/*  86: 80 */     return this.driverType;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public List getAllowedNetworks()
/*  90:    */   {
/*  91: 84 */     return this.allowedNetworks;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public String getProviderId()
/*  95:    */   {
/*  96: 88 */     return this.providerId;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public String getCountryName()
/* 100:    */   {
/* 101: 92 */     return this.countryName;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public String getCountryAlias()
/* 105:    */   {
/* 106: 96 */     return this.countryAlias;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public int getUTCOffset()
/* 110:    */   {
/* 111:100 */     return this.utcOffset;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public String getLanguage()
/* 115:    */   {
/* 116:104 */     return this.language;
/* 117:    */   }
/* 118:    */   
/* 119:    */   public void setPassword(String password)
/* 120:    */   {
/* 121:109 */     this.password = password;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public void setUsername(String username)
/* 125:    */   {
/* 126:113 */     this.username = username;
/* 127:    */   }
/* 128:    */   
/* 129:    */   public void setConnection(String connection)
/* 130:    */   {
/* 131:117 */     this.connection = connection;
/* 132:    */   }
/* 133:    */   
/* 134:    */   public void setGatewayURL(String gatewayURL)
/* 135:    */   {
/* 136:121 */     this.gatewayURL = gatewayURL;
/* 137:    */   }
/* 138:    */   
/* 139:    */   public void setHttpMethod(String httpMethod)
/* 140:    */   {
/* 141:125 */     this.httpMethod = httpMethod;
/* 142:    */   }
/* 143:    */   
/* 144:    */   public void setDriverType(String driverType)
/* 145:    */   {
/* 146:129 */     this.driverType = driverType;
/* 147:    */   }
/* 148:    */   
/* 149:    */   public void setAllowedNetworks(ArrayList allowedNetworks)
/* 150:    */   {
/* 151:133 */     this.allowedNetworks = allowedNetworks;
/* 152:    */   }
/* 153:    */   
/* 154:    */   public void setAllowedNetworks(String allowedNetworks)
/* 155:    */   {
/* 156:137 */     StringTokenizer st = new StringTokenizer(allowedNetworks, ",");
/* 157:138 */     while (st.hasMoreTokens()) {
/* 158:139 */       this.allowedNetworks.add(st.nextToken());
/* 159:    */     }
/* 160:    */   }
/* 161:    */   
/* 162:    */   public void setProviderId(String providerId)
/* 163:    */   {
/* 164:144 */     this.providerId = providerId;
/* 165:    */   }
/* 166:    */   
/* 167:    */   public String getBillingMech()
/* 168:    */   {
/* 169:148 */     return this.billingMech;
/* 170:    */   }
/* 171:    */   
/* 172:    */   public void setBillingMech(String billingMech)
/* 173:    */   {
/* 174:152 */     this.billingMech = billingMech;
/* 175:    */   }
/* 176:    */   
/* 177:    */   public void setCountryName(String countryName)
/* 178:    */   {
/* 179:156 */     this.countryName = countryName;
/* 180:    */   }
/* 181:    */   
/* 182:    */   public void setCountryAlias(String countryAlias)
/* 183:    */   {
/* 184:160 */     this.countryAlias = countryAlias;
/* 185:    */   }
/* 186:    */   
/* 187:    */   public void setUTCOffset(int utcOffset)
/* 188:    */   {
/* 189:164 */     this.utcOffset = utcOffset;
/* 190:    */   }
/* 191:    */   
/* 192:    */   public void setLanguage(String language)
/* 193:    */   {
/* 194:168 */     this.language = language;
/* 195:    */   }
/* 196:    */   
/* 197:    */   public static CPConnections getConnection(String providerID, String msisdn)
/* 198:    */     throws Exception
/* 199:    */   {
/* 200:173 */     CPConnections cnxn = null;
/* 201:174 */     List connections = null;
/* 202:    */     try
/* 203:    */     {
/* 204:178 */       connections = CPConnDB.viewConnections(providerID);
/* 205:179 */       if ((connections == null) || (connections.isEmpty())) {
/* 206:180 */         throw new Exception();
/* 207:    */       }
/* 208:    */     }
/* 209:    */     catch (Exception e)
/* 210:    */     {
/* 211:183 */       System.out.println(new Date() + ": " + CPConnections.class + ":ERROR " + e.getMessage());
/* 212:184 */       throw new Exception("8001");
/* 213:    */     }
/* 214:    */     try
/* 215:    */     {
/* 216:189 */       for (int i = 0; i < connections.size(); i++)
/* 217:    */       {
/* 218:190 */         List networks = ((CPConnections)connections.get(i)).getAllowedNetworks();
/* 219:192 */         for (int j = 0; j < networks.size(); j++) {
/* 220:193 */           if (msisdn.substring(msisdn.indexOf("+") + 1, networks.get(j).toString().length() + msisdn.indexOf("+") + 1).equals(networks.get(j).toString()))
/* 221:    */           {
/* 222:197 */             cnxn = (CPConnections)connections.get(i);
/* 223:198 */             break;
/* 224:    */           }
/* 225:    */         }
/* 226:201 */         if (cnxn != null) {
/* 227:    */           break;
/* 228:    */         }
/* 229:    */       }
/* 230:205 */       if (cnxn == null) {
/* 231:206 */         throw new Exception();
/* 232:    */       }
/* 233:    */     }
/* 234:    */     catch (Exception e)
/* 235:    */     {
/* 236:210 */       throw new Exception("8002");
/* 237:    */     }
/* 238:212 */     return cnxn;
/* 239:    */   }
/* 240:    */   
/* 241:    */   public static CPConnections getConnection(String providerID, String msisdn, String driverType)
/* 242:    */     throws Exception
/* 243:    */   {
/* 244:217 */     CPConnections cnxn = null;
/* 245:218 */     List connections = null;
/* 246:    */     try
/* 247:    */     {
/* 248:222 */       connections = CPConnDB.viewConnections(providerID, driverType);
/* 249:223 */       if ((connections == null) || (connections.isEmpty())) {
/* 250:224 */         throw new Exception();
/* 251:    */       }
/* 252:    */     }
/* 253:    */     catch (Exception e)
/* 254:    */     {
/* 255:227 */       throw new Exception("8001");
/* 256:    */     }
/* 257:    */     try
/* 258:    */     {
/* 259:233 */       for (int i = 0; i < connections.size(); i++)
/* 260:    */       {
/* 261:234 */         List networks = ((CPConnections)connections.get(i)).getAllowedNetworks();
/* 262:235 */         for (int j = 0; j < networks.size(); j++) {
/* 263:236 */           if (msisdn.substring(msisdn.indexOf("+") + 1, networks.get(j).toString().length() + msisdn.indexOf("+") + 1).equals(networks.get(j).toString()))
/* 264:    */           {
/* 265:239 */             cnxn = (CPConnections)connections.get(i);
/* 266:240 */             break;
/* 267:    */           }
/* 268:    */         }
/* 269:243 */         if (cnxn != null) {
/* 270:    */           break;
/* 271:    */         }
/* 272:    */       }
/* 273:247 */       if (cnxn == null) {
/* 274:248 */         throw new Exception();
/* 275:    */       }
/* 276:    */     }
/* 277:    */     catch (Exception e)
/* 278:    */     {
/* 279:252 */       throw new Exception("8002");
/* 280:    */     }
/* 281:254 */     return cnxn;
/* 282:    */   }
/* 283:    */   
/* 284:    */   public void updateConnection()
/* 285:    */     throws Exception
/* 286:    */   {}
/* 287:    */   
/* 288:    */   public void removeConnection()
/* 289:    */     throws Exception
/* 290:    */   {}
/* 291:    */   
/* 292:    */   public void insertConnection()
/* 293:    */     throws Exception
/* 294:    */   {}
/* 295:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.contentserver.CPConnections
 * JD-Core Version:    0.7.0.1
 */