/*   1:    */ package com.rancard.mobility.contentserver;
/*   2:    */ 
/*   3:    */ import com.rancard.util.URLUTF8Encoder;
/*   4:    */ import com.rancard.util.payment.ePin;
/*   5:    */ import java.io.BufferedReader;
/*   6:    */ import java.io.IOException;
/*   7:    */ import java.io.InputStream;
/*   8:    */ import java.io.InputStreamReader;
/*   9:    */ import java.io.PrintStream;
/*  10:    */ import org.apache.commons.httpclient.HttpClient;
/*  11:    */ import org.apache.commons.httpclient.HttpException;
/*  12:    */ import org.apache.commons.httpclient.methods.GetMethod;
/*  13:    */ 
/*  14:    */ public abstract class BillingManager
/*  15:    */ {
/*  16:    */   public static final String PIN_OPTION = "pinredemption";
/*  17:    */   public static final String SHORTCODE_OPTION = "shortcode";
/*  18:    */   public static final String OT_BILLING = "ot_bill";
/*  19:    */   
/*  20:    */   public static boolean doShortCodeBilling(String serviceType, CPConnections cnxn, String networkPrefix, String msisdn, String messageString, String keyword, String provId)
/*  21:    */     throws Exception
/*  22:    */   {
/*  23: 35 */     boolean isBilled = false;
/*  24:    */     
/*  25: 37 */     ServicePricingBean bean = ServicePricingBean.viewPriceWithUrl(provId, serviceType, networkPrefix);
/*  26: 38 */     if (bean == null) {
/*  27: 39 */       throw new Exception("10003");
/*  28:    */     }
/*  29: 52 */     String rawurl = bean.getUrl();
/*  30:    */     
/*  31: 54 */     String username = URLUTF8Encoder.encode(cnxn.getUsername());
/*  32: 55 */     String password = URLUTF8Encoder.encode(cnxn.getPassword());
/*  33: 56 */     String transId = URLUTF8Encoder.encode(messageString);
/*  34: 57 */     String number = URLUTF8Encoder.encode(msisdn);
/*  35: 58 */     String shortcode = URLUTF8Encoder.encode(bean.getShortCode());
/*  36: 59 */     String conn = URLUTF8Encoder.encode(cnxn.getConnection());
/*  37:    */     
/*  38: 61 */     String insertions = "username=" + username + "&password=" + password + "&msg=" + transId + "&msisdn=" + number + "&shortcode=" + shortcode + "&conn=" + conn;
/*  39:    */     
/*  40: 63 */     String formattedurl = URLUTF8Encoder.doMessageEscaping(insertions, rawurl);
/*  41:    */     
/*  42: 65 */     HttpClient client = new HttpClient();
/*  43: 66 */     GetMethod httpGETFORM = new GetMethod(formattedurl);
/*  44: 67 */     String resp = "";
/*  45:    */     try
/*  46:    */     {
/*  47: 70 */       client.executeMethod(httpGETFORM);
/*  48: 71 */       resp = getResponse(httpGETFORM.getResponseBodyAsStream());
/*  49: 72 */       System.out.println("Billing response: " + resp);
/*  50: 73 */       if (resp == null) {
/*  51: 74 */         throw new Exception("11000");
/*  52:    */       }
/*  53:101 */       return processResponse(resp);
/*  54:    */     }
/*  55:    */     catch (HttpException e)
/*  56:    */     {
/*  57: 79 */       resp = "5001: " + e.getMessage();
/*  58:    */       
/*  59: 81 */       System.out.println("error response: " + resp);
/*  60:    */       
/*  61: 83 */       isBilled = false;
/*  62:    */       
/*  63:    */ 
/*  64:    */ 
/*  65:    */ 
/*  66:    */ 
/*  67:    */ 
/*  68:    */ 
/*  69:    */ 
/*  70:    */ 
/*  71:    */ 
/*  72:    */ 
/*  73:    */ 
/*  74:    */ 
/*  75:    */ 
/*  76:    */ 
/*  77:    */ 
/*  78:    */ 
/*  79:101 */       return processResponse(resp);
/*  80:    */     }
/*  81:    */     catch (IOException e)
/*  82:    */     {
/*  83: 85 */       resp = "5002: " + e.getMessage();
/*  84:    */       
/*  85: 87 */       System.out.println("error response: " + resp);
/*  86:    */       
/*  87: 89 */       isBilled = false;
/*  88:    */       
/*  89:    */ 
/*  90:    */ 
/*  91:    */ 
/*  92:    */ 
/*  93:    */ 
/*  94:    */ 
/*  95:    */ 
/*  96:    */ 
/*  97:    */ 
/*  98:    */ 
/*  99:101 */       return processResponse(resp);
/* 100:    */     }
/* 101:    */     catch (Exception e)
/* 102:    */     {
/* 103: 92 */       System.out.println("error response: " + e.getMessage());
/* 104:    */       
/* 105: 94 */       isBilled = false;
/* 113:    */     }
/* 114:    */     finally
/* 115:    */     {
/* 116: 97 */       httpGETFORM.releaseConnection();
/* 117: 98 */       client = null;
/* 118: 99 */       httpGETFORM = null;
/* 119:100 */       bean = null;
/* 120:    */     }
/* 121:101 */     return processResponse(resp);
/* 122:    */   }
/* 123:    */   
/* 124:    */   public static boolean doPinRedemption(String serviceType, String provId, String networkPrefix, String pin, String keyword)
/* 125:    */     throws Exception
/* 126:    */   {
/* 127:106 */     boolean isBilled = false;
/* 128:    */     
/* 129:108 */     ServicePricingBean bean = ServicePricingBean.viewPrice(provId, serviceType, networkPrefix);
/* 130:109 */     if (!bean.getKeyword().equalsIgnoreCase(keyword)) {
/* 131:110 */       throw new Exception("10003");
/* 132:    */     }
/* 133:112 */     ePin voucher = new ePin();
/* 134:113 */     voucher.setPin(pin);
/* 135:114 */     if (!voucher.isValid()) {
/* 136:115 */       throw new Exception("4002");
/* 137:    */     }
/* 138:117 */     if (voucher.getCurrentBalance() - Double.parseDouble(bean.getPrice()) < 0.0D) {
/* 139:118 */       throw new Exception("11002");
/* 140:    */     }
/* 141:120 */     voucher.setCurrentBalance(voucher.getCurrentBalance() - Double.parseDouble(bean.getPrice()));
/* 142:121 */     voucher.updateMyLog();
/* 143:122 */     isBilled = true;
/* 144:    */     
/* 145:    */ 
/* 146:125 */     bean = null;
/* 147:126 */     voucher = null;
/* 148:127 */     return isBilled;
/* 149:    */   }
/* 150:    */   
/* 151:    */   public static boolean doOTBilling(String serviceType, CPConnections cnxn, String networkPrefix, String msisdn, String keyword, String provId)
/* 152:    */     throws Exception
/* 153:    */   {
/* 154:131 */     boolean isBilled = false;
/* 155:    */     
/* 156:133 */     ServicePricingBean bean = ServicePricingBean.viewPriceWithUrl(provId, serviceType, networkPrefix);
/* 157:134 */     if (bean == null) {
/* 158:135 */       throw new Exception("10003");
/* 159:    */     }
/* 160:137 */     String number = msisdn.substring(msisdn.indexOf("+233") + 4);
/* 161:    */     
/* 162:139 */     String username = URLUTF8Encoder.encode(cnxn.getUsername());
/* 163:140 */     String password = URLUTF8Encoder.encode(cnxn.getPassword());
/* 164:141 */     String price = URLUTF8Encoder.encode(bean.getPrice());
/* 165:142 */     number = URLUTF8Encoder.encode(number);
/* 166:    */     
/* 167:144 */     String rawurl = bean.getUrl();
/* 168:145 */     String action = "&action=2";
/* 169:146 */     String insertions = "username=" + username + "&password=" + password + "&msisdn=" + number + "&price=" + price;
/* 170:147 */     rawurl = URLUTF8Encoder.doMessageEscaping(insertions, rawurl);
/* 171:148 */     String formattedurl = rawurl + action;
/* 172:    */     
/* 173:150 */     HttpClient client = new HttpClient();
/* 174:151 */     GetMethod httpGETFORM = new GetMethod(formattedurl);
/* 175:152 */     String resp = "";
/* 176:    */     try
/* 177:    */     {
/* 178:155 */       client.executeMethod(httpGETFORM);
/* 179:156 */       resp = getResponse(httpGETFORM.getResponseBodyAsStream());
/* 180:157 */       System.out.println("OT Billing response: " + resp);
/* 181:158 */       if (resp == null) {
/* 182:159 */         throw new Exception("11000");
/* 183:    */       }
/* 184:161 */       httpGETFORM.releaseConnection();
/* 185:    */       
/* 186:163 */       String code = resp.split(";")[4];
/* 187:164 */       if (code.equals("0"))
/* 188:    */       {
/* 189:166 */         String opTransactionId = resp.split(";")[3];
/* 190:167 */         action = "&action=4";
/* 191:168 */         formattedurl = rawurl + action + "&op_transaction_id=" + opTransactionId;
/* 192:    */         
/* 193:170 */         httpGETFORM = new GetMethod(formattedurl);
/* 194:171 */         resp = "";
/* 195:172 */         client.executeMethod(httpGETFORM);
/* 196:173 */         resp = getResponse(httpGETFORM.getResponseBodyAsStream());
/* 197:174 */         System.out.println("OT Billing response: " + resp);
/* 198:175 */         if (resp == null) {
/* 199:176 */           throw new Exception("11000");
/* 200:    */         }
/* 201:179 */         code = resp.split(";")[4];
/* 202:180 */         if (!code.equals("0")) {}
/* 203:    */       }
/* 204:181 */       return true;
/* 205:    */     }
/* 206:    */     catch (HttpException e)
/* 207:    */     {
/* 208:188 */       resp = "5001: " + e.getMessage();
/* 209:    */       
/* 210:190 */       System.out.println("error response: " + resp);
/* 211:    */       
/* 212:192 */       return false;
/* 213:    */     }
/* 214:    */     catch (IOException e)
/* 215:    */     {
/* 216:194 */       resp = "5002: " + e.getMessage();
/* 217:    */       
/* 218:196 */       System.out.println("error response: " + resp);
/* 219:    */       
/* 220:198 */       return false;
/* 221:    */     }
/* 222:    */     catch (Exception e)
/* 223:    */     {
/* 224:201 */       System.out.println("error response: " + e.getMessage());
/* 225:    */       
/* 226:203 */       return false;
/* 227:    */     }
/* 228:    */     finally
/* 229:    */     {
/* 230:206 */       httpGETFORM.releaseConnection();
/* 231:207 */       client = null;
/* 232:208 */       httpGETFORM = null;
/* 233:    */     }
/* 235:    */   }
/* 236:    */   
/* 237:    */   private static String getResponse(InputStream in)
/* 238:    */     throws Exception
/* 239:    */   {
/* 240:214 */     String status = "error";
/* 241:215 */     String reply = "";
/* 242:216 */     String error = "";
/* 243:217 */     String responseString = "";
/* 244:218 */     BufferedReader br = null;
/* 245:    */     try
/* 246:    */     {
/* 247:220 */       InputStream responseBody = in;
/* 248:221 */       br = new BufferedReader(new InputStreamReader(responseBody));
/* 249:    */       
/* 250:223 */       String line = br.readLine();
/* 251:224 */       while (line != null)
/* 252:    */       {
/* 253:225 */         responseString = responseString + line;
/* 254:226 */         line = br.readLine();
/* 255:    */       }
/* 256:    */     }
/* 257:    */     catch (IOException e)
/* 258:    */     {
/* 259:229 */       throw new Exception("5002: " + e.getMessage());
/* 260:    */     }
/* 261:    */     finally
/* 262:    */     {
/* 263:231 */       br.close();
/* 264:232 */       in.close();
/* 265:    */       
/* 266:234 */       br = null;
/* 267:235 */       in = null;
/* 268:    */     }
/* 269:238 */     return responseString;
/* 270:    */   }
/* 271:    */   
/* 272:    */   public static boolean processResponse(String reply)
/* 273:    */     throws Exception
/* 274:    */   {
/* 275:242 */     boolean isOk = false;
/* 276:243 */     if ((reply != null) && (!reply.equals(""))) {
/* 277:244 */       if (reply.substring(0, 2).equalsIgnoreCase("OK")) {
/* 278:245 */         isOk = true;
/* 279:246 */       } else if ((reply.length() > 290) && (reply.indexOf("1001") != -1)) {
/* 280:247 */         isOk = true;
/* 281:248 */       } else if (reply.substring(0, 1).equalsIgnoreCase("3")) {
/* 282:249 */         isOk = true;
/* 283:    */       }
/* 284:    */     }
/* 285:252 */     return isOk;
/* 286:    */   }
/* 287:    */ }



/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar

 * Qualified Name:     com.rancard.mobility.contentserver.BillingManager

 * JD-Core Version:    0.7.0.1

 */