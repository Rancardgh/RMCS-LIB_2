/*   1:    */ package com.rancard.mobility.infoserver;
/*   2:    */ 
/*   3:    */ import com.rancard.mobility.infoserver.common.services.UserService;
/*   4:    */ import java.io.PrintStream;
/*   5:    */ import java.text.DateFormat;
/*   6:    */ import java.text.ParseException;
/*   7:    */ import java.text.SimpleDateFormat;
/*   8:    */ import java.util.ArrayList;
/*   9:    */ import java.util.Date;
/*  10:    */ import java.util.HashMap;
/*  11:    */ 
/*  12:    */ public class InfoService
/*  13:    */   extends UserService
/*  14:    */ {
/*  15:    */   private String publishDate;
/*  16:    */   private int msgId;
/*  17:    */   private String publishTime;
/*  18:    */   private String message;
/*  19:    */   private ArrayList<InfoService> messages;
/*  20:    */   private String ownerId;
/*  21:    */   private String imageURL;
/*  22:    */   private String articleTitle;
/*  23:    */   private String header;
/*  24:    */   private String footer;
/*  25:    */   private String messageRef;
/*  26:    */   private String tags;
/*  27:    */   public static final String DATEFORMATDISPLAY = "dd-MMM-yyyy";
/*  28:    */   public static final String DATEFORMAT = "dd-MM-yyyy";
/*  29:    */   public static final String TIMESTAMPFORMAT = "dd-MMM-yyyy HH.mm.ss";
/*  30:    */   
/*  31:    */   public InfoService() {}
/*  32:    */   
/*  33:    */   public InfoService(String serviceType, String serviceName, String defaultMessage, String keyword, String message, String accountId, String pubDate)
/*  34:    */   {
/*  35: 38 */     super(serviceType, keyword, accountId, serviceName, defaultMessage);
/*  36:    */     
/*  37: 40 */     this.message = message;
/*  38: 41 */     this.publishDate = pubDate;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public String getPublishDate()
/*  42:    */   {
/*  43: 47 */     return this.publishDate;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public String getPublishTime()
/*  47:    */   {
/*  48: 51 */     return this.publishTime;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public String getMessage()
/*  52:    */   {
/*  53: 55 */     return this.message;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public String getHeader()
/*  57:    */   {
/*  58: 59 */     return this.header;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public String getFooter()
/*  62:    */   {
/*  63: 63 */     return this.footer;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public ArrayList<InfoService> getMessages()
/*  67:    */   {
/*  68: 67 */     return this.messages;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public int getMsgId()
/*  72:    */   {
/*  73: 71 */     return this.msgId;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public String getOwnerId()
/*  77:    */   {
/*  78: 74 */     return this.ownerId;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public String getImageURL()
/*  82:    */   {
/*  83: 77 */     return this.imageURL;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public String getArticleTitle()
/*  87:    */   {
/*  88: 80 */     return this.articleTitle;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void setPublishDate(String date)
/*  92:    */   {
/*  93: 84 */     this.publishDate = date;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public void setPublishTime(String time)
/*  97:    */   {
/*  98: 88 */     this.publishTime = time;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public void setMessage(String message)
/* 102:    */   {
/* 103: 92 */     this.message = message;
/* 104:    */   }
/* 105:    */   
/* 106:    */   public void setHeader(String header)
/* 107:    */   {
/* 108: 96 */     this.header = header;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public void setFooter(String footer)
/* 112:    */   {
/* 113:100 */     this.footer = footer;
/* 114:    */   }
/* 115:    */   
/* 116:    */   public void setMessages(ArrayList<InfoService> messages)
/* 117:    */   {
/* 118:104 */     this.messages = messages;
/* 119:    */   }
/* 120:    */   
/* 121:    */   public void setMsgId(int msgId)
/* 122:    */   {
/* 123:108 */     this.msgId = msgId;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public void setOwnerId(String ownerId)
/* 127:    */   {
/* 128:111 */     this.ownerId = ownerId;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public void setImageURL(String imageURL)
/* 132:    */   {
/* 133:115 */     this.imageURL = imageURL;
/* 134:    */   }
/* 135:    */   
/* 136:    */   public void setArticleTitle(String articleTitle)
/* 137:    */   {
/* 138:119 */     this.articleTitle = articleTitle;
/* 139:    */   }
/* 140:    */   
/* 141:    */   public String getMessageRef()
/* 142:    */   {
/* 143:126 */     return this.messageRef;
/* 144:    */   }
/* 145:    */   
/* 146:    */   public void setMessageRef(String messageRef)
/* 147:    */   {
/* 148:133 */     this.messageRef = messageRef;
/* 149:    */   }
/* 150:    */   
/* 151:    */   public String getTags()
/* 152:    */   {
/* 153:140 */     return this.tags;
/* 154:    */   }
/* 155:    */   
/* 156:    */   public void setTags(String tags)
/* 157:    */   {
/* 158:147 */     this.tags = tags;
/* 159:    */   }
/* 160:    */   
/* 161:    */   public void createInfoServiceEntry()
/* 162:    */     throws Exception
/* 163:    */   {
/* 164:151 */     InfoServiceDB system_sms_queue = new InfoServiceDB();
/* 165:152 */     DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
/* 166:    */     try
/* 167:    */     {
/* 168:155 */       Date date = formatter.parse(getPublishDate());
/* 169:156 */       system_sms_queue.createInfoServiceEntry(date, getKeyword(), getMessage(), getAccountId(), getOwnerId(), this.imageURL, this.articleTitle);
/* 170:    */     }
/* 171:    */     catch (ParseException ex)
/* 172:    */     {
/* 173:159 */       System.out.println(new Date() + ":Error creating InfoServiceEntry:");
/* 174:160 */       ex.printStackTrace();
/* 175:    */     }
/* 176:    */   }
/* 177:    */   
/* 178:    */   public void updateInfoServiceEntry()
/* 179:    */     throws Exception
/* 180:    */   {
/* 181:166 */     InfoServiceDB system_sms_queue = new InfoServiceDB();
/* 182:167 */     DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
/* 183:    */     try
/* 184:    */     {
/* 185:170 */       Date date = formatter.parse(getPublishDate());
/* 186:171 */       system_sms_queue.updateInfoService(date, getKeyword(), getMessage(), getAccountId(), this.msgId, this.imageURL, this.articleTitle);
/* 187:    */     }
/* 188:    */     catch (ParseException ex)
/* 189:    */     {
/* 190:174 */       System.out.println(new Date() + ":Error updating InfoServiceEntry:");
/* 191:175 */       ex.printStackTrace();
/* 192:    */     }
/* 193:    */   }
/* 194:    */   
/* 195:    */   public void deleteInfoServiceEntry()
/* 196:    */     throws Exception
/* 197:    */   {
/* 198:180 */     InfoServiceDB system_sms_queue = new InfoServiceDB();
/* 199:181 */     DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
/* 200:    */     try
/* 201:    */     {
/* 202:184 */       Date date = formatter.parse(getPublishDate());
/* 203:185 */       system_sms_queue.deleteInfoService(date, getKeyword(), getAccountId(), this.msgId);
/* 204:    */     }
/* 205:    */     catch (ParseException ex)
/* 206:    */     {
/* 207:188 */       System.out.println(new Date() + ":Error deleting InfoServiceEntry:");
/* 208:189 */       ex.printStackTrace();
/* 209:    */     }
/* 210:    */   }
/* 211:    */   
/* 212:    */   public void viewInfoService()
/* 213:    */     throws Exception
/* 214:    */   {
/* 215:194 */     InfoService bean = new InfoService();
/* 216:195 */     InfoServiceDB system_sms_queue = new InfoServiceDB();
/* 217:196 */     SimpleDateFormat newDF = new SimpleDateFormat("dd-MM-yyyy");
/* 218:197 */     Date pubDate = newDF.parse(getPublishDate());
/* 219:198 */     bean = system_sms_queue.viewInfoService(getKeyword(), getAccountId(), pubDate);
/* 220:199 */     SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
/* 221:    */     
/* 222:201 */     setAccountId(bean.getAccountId());
/* 223:202 */     setDefaultMessage(bean.getDefaultMessage());
/* 224:203 */     setKeyword(bean.getKeyword());
/* 225:204 */     setPublishDate(bean.getPublishDate());
/* 226:205 */     setPublishTime(bean.getPublishTime());
/* 227:206 */     setServiceType(bean.getServiceType());
/* 228:207 */     setMessage(bean.getMessage());
/* 229:208 */     setMsgId(bean.getMsgId());
/* 230:209 */     setServiceName(bean.getServiceName());
/* 231:210 */     setPricing(bean.getPricing());
/* 232:211 */     setAllowedShortcodes(bean.getAllowedShortcodes());
/* 233:212 */     setAllowedSiteTypes(bean.getAllowedSiteTypes());
/* 234:213 */     setOwnerId(bean.getOwnerId());
/* 235:214 */     setImageURL(bean.getImageURL());
/* 236:215 */     setArticleTitle(bean.getArticleTitle());
/* 237:216 */     setHeader(bean.getHeader());
/* 238:217 */     setFooter(bean.getFooter());
/* 239:    */   }
/* 240:    */   
/* 241:    */   public static InfoService viewInfoService(String keyword, String accountId, String date, int msg_id)
/* 242:    */     throws Exception
/* 243:    */   {
/* 244:221 */     SimpleDateFormat newDF = new SimpleDateFormat("dd-MM-yyyy");
/* 245:222 */     Date pubDate = newDF.parse(date);
/* 246:223 */     return InfoServiceDB.viewInfoService(keyword, accountId, pubDate, msg_id);
/* 247:    */   }
/* 248:    */   
/* 249:    */   public void viewMessages()
/* 250:    */     throws Exception
/* 251:    */   {
/* 252:229 */     InfoServiceDB system_sms_queue = new InfoServiceDB();
/* 253:230 */     SimpleDateFormat newDF = new SimpleDateFormat("dd-MM-yyyy");
/* 254:231 */     Date pubDate = newDF.parse(getPublishDate());
/* 255:232 */     this.messages = system_sms_queue.viewInfoServices(getKeyword(), getAccountId(), pubDate);
/* 256:    */   }
/* 257:    */   
/* 258:    */   public void viewMessagesFor3rdParty()
/* 259:    */     throws Exception
/* 260:    */   {
/* 261:235 */     ArrayList<InfoService> list = new ArrayList();
/* 262:236 */     InfoServiceDB system_sms_queue = new InfoServiceDB();
/* 263:237 */     SimpleDateFormat newDF = new SimpleDateFormat("dd-MM-yyyy");
/* 264:238 */     Date pubDate = newDF.parse(getPublishDate());
/* 265:239 */     this.messages = system_sms_queue.viewInfoServices(getKeyword(), getAccountId(), pubDate, getOwnerId());
/* 266:    */   }
/* 267:    */   
/* 268:    */   public void updateInfoService()
/* 269:    */     throws Exception
/* 270:    */   {
/* 271:243 */     InfoServiceDB system_sms_queue = new InfoServiceDB();
/* 272:    */     
/* 273:245 */     SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
/* 274:246 */     Date utilDate_date = new Date();
/* 275:247 */     String s = DateFormat.getTimeInstance(3).format(utilDate_date);
/* 276:    */     try
/* 277:    */     {
/* 278:250 */       utilDate_date = sdf.parse(this.publishDate);
/* 279:    */     }
/* 280:    */     catch (Exception e)
/* 281:    */     {
/* 282:252 */       throw new Exception(e.getMessage());
/* 283:    */     }
/* 284:    */   }
/* 285:    */   
/* 286:    */   public void viewHeaderFooter()
/* 287:    */     throws Exception
/* 288:    */   {
/* 289:258 */     InfoServiceDB system_sms_queue = new InfoServiceDB();
/* 290:259 */     HashMap<String, String> headerFooter = new HashMap();
/* 291:260 */     headerFooter = system_sms_queue.viewHeaderFooter(getKeyword(), getAccountId());
/* 292:261 */     this.header = ((String)headerFooter.get("header"));
/* 293:262 */     this.footer = ((String)headerFooter.get("footer"));
/* 294:    */   }
/* 295:    */   
/* 296:    */   public void deleteInfoService()
/* 297:    */     throws Exception
/* 298:    */   {
/* 299:266 */     InfoServiceDB system_sms_queue = new InfoServiceDB();
/* 300:267 */     system_sms_queue.deleteInfoService(getKeyword(), getAccountId());
/* 301:    */   }
/* 302:    */   
/* 303:    */   public static void logInfoRequest(String ownerId, String msg, String accountId, String keyword, String msisdn, String shortcode)
/* 304:    */     throws Exception
/* 305:    */   {
/* 306:272 */     InfoServiceDB.logInfoRequest(ownerId, msg, accountId, keyword, msisdn, shortcode);
/* 307:    */   }
/* 308:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.infoserver.InfoService
 * JD-Core Version:    0.7.0.1
 */