/*   1:    */ package com.rancard.mobility.contentserver;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.StringTokenizer;
/*   5:    */ 
/*   6:    */ public class CPConnections
/*   7:    */ {
/*   8: 12 */   private String password = null;
/*   9: 13 */   private String username = null;
/*  10: 14 */   private String connection = null;
/*  11: 15 */   private String gatewayURL = null;
/*  12: 16 */   private String httpMethod = null;
/*  13: 17 */   private String driverType = null;
/*  14: 18 */   private ArrayList allowedNetworks = null;
/*  15: 19 */   private String providerId = null;
/*  16: 20 */   private String billingMech = null;
/*  17: 21 */   private String countryName = null;
/*  18: 22 */   private String countryAlias = null;
/*  19: 23 */   private int UTCOffset = 0;
/*  20: 24 */   private String language = null;
/*  21:    */   
/*  22:    */   public CPConnections()
/*  23:    */   {
/*  24: 28 */     this.password = new String();
/*  25: 29 */     this.username = new String();
/*  26: 30 */     this.connection = new String();
/*  27: 31 */     this.gatewayURL = new String();
/*  28: 32 */     this.httpMethod = new String();
/*  29: 33 */     this.driverType = new String();
/*  30: 34 */     this.allowedNetworks = new ArrayList();
/*  31: 35 */     this.providerId = new String();
/*  32: 36 */     this.billingMech = new String();
/*  33: 37 */     this.countryName = new String();
/*  34: 38 */     this.countryAlias = new String();
/*  35: 39 */     this.UTCOffset = 0;
/*  36: 40 */     this.language = new String();
/*  37:    */   }
/*  38:    */   
/*  39:    */   public String getPassword()
/*  40:    */   {
/*  41: 45 */     return this.password;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public String getUsername()
/*  45:    */   {
/*  46: 49 */     return this.username;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public String getConnection()
/*  50:    */   {
/*  51: 53 */     return this.connection;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public String getGatewayURL()
/*  55:    */   {
/*  56: 57 */     return this.gatewayURL;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public String getHttpMethod()
/*  60:    */   {
/*  61: 61 */     return this.httpMethod;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public String getDriverType()
/*  65:    */   {
/*  66: 65 */     return this.driverType;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public ArrayList getAllowedNetworks()
/*  70:    */   {
/*  71: 69 */     return this.allowedNetworks;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public String getProviderId()
/*  75:    */   {
/*  76: 73 */     return this.providerId;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public String getCountryName()
/*  80:    */   {
/*  81: 76 */     return this.countryName;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public String getCountryAlias()
/*  85:    */   {
/*  86: 79 */     return this.countryAlias;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public int getUTCOffset()
/*  90:    */   {
/*  91: 82 */     return this.UTCOffset;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public String getLanguage()
/*  95:    */   {
/*  96: 86 */     return this.language;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public void setPassword(String password)
/* 100:    */   {
/* 101: 90 */     this.password = password;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public void setUsername(String username)
/* 105:    */   {
/* 106: 94 */     this.username = username;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public void setConnection(String connection)
/* 110:    */   {
/* 111: 98 */     this.connection = connection;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public void setGatewayURL(String gatewayURL)
/* 115:    */   {
/* 116:102 */     this.gatewayURL = gatewayURL;
/* 117:    */   }
/* 118:    */   
/* 119:    */   public void setHttpMethod(String httpMethod)
/* 120:    */   {
/* 121:106 */     this.httpMethod = httpMethod;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public void setDriverType(String driverType)
/* 125:    */   {
/* 126:110 */     this.driverType = driverType;
/* 127:    */   }
/* 128:    */   
/* 129:    */   public void setAllowedNetworks(ArrayList allowedNetworks)
/* 130:    */   {
/* 131:114 */     this.allowedNetworks = allowedNetworks;
/* 132:    */   }
/* 133:    */   
/* 134:    */   public void setAllowedNetworks(String allowedNetworks)
/* 135:    */   {
/* 136:118 */     StringTokenizer st = new StringTokenizer(allowedNetworks, ",");
/* 137:119 */     while (st.hasMoreTokens()) {
/* 138:120 */       this.allowedNetworks.add(st.nextToken());
/* 139:    */     }
/* 140:    */   }
/* 141:    */   
/* 142:    */   public void setProviderId(String providerId)
/* 143:    */   {
/* 144:125 */     this.providerId = providerId;
/* 145:    */   }
/* 146:    */   
/* 147:    */   public String getBillingMech()
/* 148:    */   {
/* 149:129 */     return this.billingMech;
/* 150:    */   }
/* 151:    */   
/* 152:    */   public void setBillingMech(String billingMech)
/* 153:    */   {
/* 154:132 */     this.billingMech = billingMech;
/* 155:    */   }
/* 156:    */   
/* 157:    */   public void setCountryName(String countryName)
/* 158:    */   {
/* 159:135 */     this.countryName = countryName;
/* 160:    */   }
/* 161:    */   
/* 162:    */   public void setCountryAlias(String countryAlias)
/* 163:    */   {
/* 164:138 */     this.countryAlias = countryAlias;
/* 165:    */   }
/* 166:    */   
/* 167:    */   public void setUTCOffset(int UTCOffset)
/* 168:    */   {
/* 169:141 */     this.UTCOffset = UTCOffset;
/* 170:    */   }
/* 171:    */   
/* 172:    */   public void setLanguage(String language)
/* 173:    */   {
/* 174:145 */     this.language = language;
/* 175:    */   }
/* 176:    */   
/* 177:    */   public static CPConnections getConnection(String providerID, String msisdn)
/* 178:    */     throws Exception
/* 179:    */   {
/* 180:150 */     CPConnections cnxn = null;
/* 181:151 */     ArrayList connections = null;
/* 182:    */     try
/* 183:    */     {
/* 184:155 */       connections = CPConnDB.viewConnections(providerID);
/* 185:156 */       if ((connections == null) || (connections.size() == 0)) {
/* 186:157 */         throw new Exception();
/* 187:    */       }
/* 188:    */     }
/* 189:    */     catch (Exception e)
/* 190:    */     {
/* 191:160 */       throw new Exception("8001");
/* 192:    */     }
/* 193:    */     try
/* 194:    */     {
/* 195:166 */       ArrayList networks = null;
/* 196:167 */       for (int i = 0; i < connections.size(); i++)
/* 197:    */       {
/* 198:168 */         networks = ((CPConnections)connections.get(i)).getAllowedNetworks();
/* 199:170 */         for (int j = 0; j < networks.size(); j++) {
/* 200:171 */           if (msisdn.substring(msisdn.indexOf("+") + 1, networks.get(j).toString().length() + msisdn.indexOf("+") + 1).equals(networks.get(j).toString()))
/* 201:    */           {
/* 202:175 */             cnxn = (CPConnections)connections.get(i);
/* 203:176 */             break;
/* 204:    */           }
/* 205:    */         }
/* 206:179 */         if (cnxn != null) {
/* 207:    */           break;
/* 208:    */         }
/* 209:    */       }
/* 210:183 */       if (cnxn == null) {
/* 211:184 */         throw new Exception();
/* 212:    */       }
/* 213:    */     }
/* 214:    */     catch (Exception e)
/* 215:    */     {
/* 216:188 */       throw new Exception("8002");
/* 217:    */     }
/* 218:190 */     return cnxn;
/* 219:    */   }
/* 220:    */   
/* 221:    */   public static CPConnections getConnection(String providerID, String msisdn, String driverType)
/* 222:    */     throws Exception
/* 223:    */   {
/* 224:196 */     CPConnections cnxn = null;
/* 225:197 */     ArrayList connections = null;
/* 226:    */     try
/* 227:    */     {
/* 228:201 */       connections = CPConnDB.viewConnections(providerID, driverType);
/* 229:202 */       if ((connections == null) || (connections.size() == 0)) {
/* 230:203 */         throw new Exception();
/* 231:    */       }
/* 232:    */     }
/* 233:    */     catch (Exception e)
/* 234:    */     {
/* 235:206 */       throw new Exception("8001");
/* 236:    */     }
/* 237:    */     try
/* 238:    */     {
/* 239:212 */       ArrayList networks = null;
/* 240:213 */       for (int i = 0; i < connections.size(); i++)
/* 241:    */       {
/* 242:214 */         networks = ((CPConnections)connections.get(i)).getAllowedNetworks();
/* 243:216 */         for (int j = 0; j < networks.size(); j++) {
/* 244:217 */           if (msisdn.substring(msisdn.indexOf("+") + 1, networks.get(j).toString().length() + msisdn.indexOf("+") + 1).equals(networks.get(j).toString()))
/* 245:    */           {
/* 246:221 */             cnxn = (CPConnections)connections.get(i);
/* 247:222 */             break;
/* 248:    */           }
/* 249:    */         }
/* 250:225 */         if (cnxn != null) {
/* 251:    */           break;
/* 252:    */         }
/* 253:    */       }
/* 254:229 */       if (cnxn == null) {
/* 255:230 */         throw new Exception();
/* 256:    */       }
/* 257:    */     }
/* 258:    */     catch (Exception e)
/* 259:    */     {
/* 260:234 */       throw new Exception("8002");
/* 261:    */     }
/* 262:236 */     return cnxn;
/* 263:    */   }
/* 264:    */   
/* 265:    */   public void updateConnection()
/* 266:    */     throws Exception
/* 267:    */   {}
/* 268:    */   
/* 269:    */   public void removeConnection()
/* 270:    */     throws Exception
/* 271:    */   {}
/* 272:    */   
/* 273:    */   public void insertConnection()
/* 274:    */     throws Exception
/* 275:    */   {}
/* 276:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.CPConnections
 * JD-Core Version:    0.7.0.1
 */