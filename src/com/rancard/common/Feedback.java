/*   1:    */ package com.rancard.common;
/*   2:    */ 
/*   3:    */ import java.util.Locale;
/*   4:    */ import java.util.ResourceBundle;
/*   5:    */ 
/*   6:    */ public class Feedback
/*   7:    */ {
/*   8:    */   public static final String MISSING_INVALID_ITEM_REF = "1000";
/*   9:    */   public static final String NO_CONTENT_AT_LOCATION = "1001";
/*  10:    */   public static final String MISSING_INVALID_CONTENT_TYPE = "1002";
/*  11:    */   public static final String PREVIEW_CREATION_ERROR = "1003";
/*  12:    */   public static final String MISSING_INVALID_MSISDN = "2000";
/*  13:    */   public static final String MISSING_INVALID_PHONE_ID = "1";
/*  14:    */   public static final String PHONE_FORMAT_INCOMPATIBILITY = "2002";
/*  15:    */   public static final String PHONE_MATRIX_INIT_ERROR = "2003";
/*  16:    */   public static final String PUSH_PROTOCOL_NOT_SUPPORTED = "2004";
/*  17:    */   public static final String MISSING_INVALID_TICKET_ID = "3000";
/*  18:    */   public static final String EXPIRED_TICKET_ID = "3001";
/*  19:    */   public static final String DOWNLOAD_PROCESSED = "3002";
/*  20:    */   public static final String INVALID_PASSWORD = "4000";
/*  21:    */   public static final String INVALID_REQUEST_CREDENTIALS = "4001";
/*  22:    */   public static final String MISSING_INVALID_PIN = "4002";
/*  23:    */   public static final String MISSING_INVALID_REGISTRATION_ID = "4002";
/*  24:    */   public static final String BANDWIDTH_EXCEEDED = "4004";
/*  25:    */   public static final String INBOX_EXCEEDED = "4005";
/*  26:    */   public static final String OUTBOX_EXCEEDED = "4006";
/*  27:    */   public static final String DATA_STREAM_ERROR = "5000";
/*  28:    */   public static final String PROTOCOL_ERROR = "5001";
/*  29:    */   public static final String TRANSPORT_ERROR = "5002";
/*  30:    */   public static final String CONNECTION_ERROR = "5003";
/*  31:    */   public static final String MALFORMED_REQUEST = "6000";
/*  32:    */   public static final String GENERIC_ERROR = "6001";
/*  33:    */   public static final String XML_PARSER_ERROR = "7000";
/*  34:    */   public static final String ROUTE_NOTIFICATION_FAILED = "8000";
/*  35:    */   public static final String NO_CONNECTIONS_FOUND = "8001";
/*  36:    */   public static final String UNSUPPORTED_NETWORK = "8002";
/*  37:    */   public static final String NO_SUCH_COMPETITION = "9000";
/*  38:    */   public static final String COMPETITION_NOT_OVER = "9001";
/*  39:    */   public static final String COMPETITION_ENDED = "9002";
/*  40:    */   public static final String WINNERS_ALREADY_DRAWN = "9003";
/*  41:    */   public static final String WINNERS_NOT_FOUND = "9004";
/*  42:    */   public static final String MISSING_COMPETITION_ID = "9005";
/*  43:    */   public static final String PARTICIPATION_LIMIT_EXCEEDED = "9006";
/*  44:    */   public static final String EMPTY_VOTE = "9007";
/*  45:    */   public static final String ALREADY_VOTED = "9008";
/*  46:    */   public static final String VOTES_RECEIVED = "9009";
/*  47:    */   public static final String NOT_REGISTERED = "10000";
/*  48:    */   public static final String NO_SUCH_SERVICE = "10001";
/*  49:    */   public static final String NO_URL_FOR_SERVICE = "10002";
/*  50:    */   public static final String INVALID_SERVICE_REQUEST = "10003";
/*  51:    */   public static final String MISSING_INVALID_PROV_ID = "10004";
/*  52:    */   public static final String MISSING_INVALID_KEYWORD = "10005";
/*  53:    */   public static final String REGISTRATION_FAILED = "10006";
/*  54:    */   public static final String ALREADY_REGISTERED = "10007";
/*  55:    */   public static final String REGISTRATION_COMPLETED = "10008";
/*  56:    */   public static final String BILLING_MECH_FAILURE = "11000";
/*  57:    */   public static final String NO_BILLING_URL = "11001";
/*  58:    */   public static final String INSUFFICIENT_CREDIT_ON_PIN = "11002";
/*  59:    */   private ResourceBundle rbundle;
/*  60:    */   private String language;
/*  61:    */   
/*  62:    */   public Feedback()
/*  63:    */     throws Exception
/*  64:    */   {
/*  65: 82 */     Locale locale = new Locale("en");
/*  66: 83 */     this.rbundle = ResourceBundle.getBundle("feedback", locale);
/*  67: 84 */     this.language = locale.getLanguage();
/*  68:    */   }
/*  69:    */   
/*  70:    */   public Feedback(String languageCode)
/*  71:    */     throws Exception
/*  72:    */   {
/*  73: 88 */     Locale locale = new Locale(languageCode);
/*  74: 89 */     this.rbundle = ResourceBundle.getBundle("feedback", locale);
/*  75: 90 */     this.language = locale.getLanguage();
/*  76:    */   }
/*  77:    */   
/*  78:    */   public Feedback(String languageCode, String countryCode)
/*  79:    */     throws Exception
/*  80:    */   {
/*  81: 94 */     Locale locale = new Locale(languageCode, countryCode);
/*  82: 95 */     this.rbundle = ResourceBundle.getBundle("feedback", locale);
/*  83:    */   }
/*  84:    */   
/*  85:    */   public String getLanguage()
/*  86:    */   {
/*  87: 99 */     return this.language;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public String getCode(String key)
/*  91:    */   {
/*  92:104 */     String out = null;
/*  93:    */     try
/*  94:    */     {
/*  95:106 */       out = this.rbundle.getString(key + "_CODE");
/*  96:    */     }
/*  97:    */     catch (Exception e)
/*  98:    */     {
/*  99:108 */       out = "";
/* 100:    */     }
/* 101:110 */     return out;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public String getType(String key)
/* 105:    */   {
/* 106:114 */     String out = null;
/* 107:    */     try
/* 108:    */     {
/* 109:116 */       out = this.rbundle.getString(key + "_TYPE");
/* 110:    */     }
/* 111:    */     catch (Exception e)
/* 112:    */     {
/* 113:118 */       out = "";
/* 114:    */     }
/* 115:120 */     return out;
/* 116:    */   }
/* 117:    */   
/* 118:    */   public String getTechnicalDescription(String key)
/* 119:    */   {
/* 120:124 */     String out = null;
/* 121:    */     try
/* 122:    */     {
/* 123:126 */       out = this.rbundle.getString(key + "_TECH_DESC");
/* 124:    */     }
/* 125:    */     catch (Exception e)
/* 126:    */     {
/* 127:128 */       out = "";
/* 128:    */     }
/* 129:130 */     return out;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public String getUserFriendlyDescription(String key)
/* 133:    */   {
/* 134:134 */     String out = null;
/* 135:    */     try
/* 136:    */     {
/* 137:136 */       out = this.rbundle.getString(key + "_USER_DESC");
/* 138:    */     }
/* 139:    */     catch (Exception e)
/* 140:    */     {
/* 141:138 */       out = "";
/* 142:    */     }
/* 143:140 */     return out;
/* 144:    */   }
/* 145:    */   
/* 146:    */   public String getValue(String key)
/* 147:    */   {
/* 148:144 */     String out = null;
/* 149:    */     try
/* 150:    */     {
/* 151:146 */       out = this.rbundle.getString(key);
/* 152:    */     }
/* 153:    */     catch (Exception e)
/* 154:    */     {
/* 155:148 */       out = "";
/* 156:    */     }
/* 157:150 */     return out;
/* 158:    */   }
/* 159:    */   
/* 160:    */   public String formDefaultMessage(String key)
/* 161:    */   {
/* 162:154 */     String out = null;
/* 163:    */     try
/* 164:    */     {
/* 165:156 */       out = getType(key) + " " + getCode(key) + ":" + getTechnicalDescription(key);
/* 166:    */     }
/* 167:    */     catch (Exception e)
/* 168:    */     {
/* 169:158 */       out = "";
/* 170:    */     }
/* 171:160 */     return out;
/* 172:    */   }
/* 173:    */   
/* 174:    */   public String getLiveScoreResultType(String key)
/* 175:    */   {
/* 176:165 */     String out = null;
/* 177:    */     try
/* 178:    */     {
/* 179:167 */       out = this.rbundle.getString("LS_RT_" + key);
/* 180:    */     }
/* 181:    */     catch (Exception e)
/* 182:    */     {
/* 183:169 */       out = "";
/* 184:    */     }
/* 185:171 */     return out;
/* 186:    */   }
/* 187:    */   
/* 188:    */   public String getLiveScoreStatusCode(String key)
/* 189:    */   {
/* 190:175 */     String out = null;
/* 191:    */     try
/* 192:    */     {
/* 193:177 */       out = this.rbundle.getString("LS_SC_" + key);
/* 194:    */     }
/* 195:    */     catch (Exception e)
/* 196:    */     {
/* 197:179 */       out = "";
/* 198:    */     }
/* 199:181 */     return out;
/* 200:    */   }
/* 201:    */   
/* 202:    */   public String getLiveScorePeriodCode(String key)
/* 203:    */   {
/* 204:185 */     String out = null;
/* 205:    */     try
/* 206:    */     {
/* 207:187 */       out = this.rbundle.getString("LS_PC_" + key);
/* 208:    */     }
/* 209:    */     catch (Exception e)
/* 210:    */     {
/* 211:189 */       out = "";
/* 212:    */     }
/* 213:191 */     return out;
/* 214:    */   }
/* 215:    */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.common.Feedback
 * JD-Core Version:    0.7.0.1
 */