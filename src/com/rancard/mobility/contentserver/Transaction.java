/*   1:    */ package com.rancard.mobility.contentserver;
/*   2:    */ 
/*   3:    */ import java.sql.Timestamp;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.Calendar;
/*   6:    */ import java.util.Date;
/*   7:    */ 
/*   8:    */ public class Transaction
/*   9:    */ {
/*  10:    */   private String ticketID;
/*  11:    */   private String id;
/*  12:    */   private String listID;
/*  13:    */   private String subscriberMSISDN;
/*  14:    */   private String phoneId;
/*  15:    */   private Timestamp date;
/*  16:    */   private boolean downloadCompleted;
/*  17:    */   private String voucher;
/*  18:    */   private String siteId;
/*  19:    */   private boolean isBilled;
/*  20:    */   private boolean notifSent;
/*  21:    */   private String keyword;
/*  22:    */   private String pricePoint;
/*  23:    */   private ContentItem contentItem;
/*  24:    */   private Format format;
/*  25:    */   private CPSite site;
/*  26:    */   
/*  27:    */   public Transaction()
/*  28:    */   {
/*  29: 42 */     this.ticketID = null;
/*  30: 43 */     this.id = null;
/*  31: 44 */     this.listID = null;
/*  32: 45 */     this.subscriberMSISDN = null;
/*  33: 46 */     this.phoneId = "";
/*  34: 47 */     this.date = now();
/*  35: 48 */     this.downloadCompleted = false;
/*  36: 49 */     this.voucher = null;
/*  37: 50 */     this.siteId = null;
/*  38: 51 */     this.pricePoint = null;
/*  39: 52 */     this.isBilled = false;
/*  40: 53 */     this.notifSent = false;
/*  41:    */     
/*  42: 55 */     this.keyword = "";
/*  43:    */     
/*  44: 57 */     this.contentItem = new ContentItem();
/*  45: 58 */     this.format = new Format();
/*  46: 59 */     this.site = new CPSite();
/*  47:    */   }
/*  48:    */   
/*  49:    */   public Transaction(String newTicketID, String newID, String newListID, String newSubscriberMSISDN, String phoneId, boolean flag, String voucherNo, String siteId, boolean isBilled, String kw, boolean notifSent, String pricePoint)
/*  50:    */   {
/*  51: 68 */     this.ticketID = newTicketID;
/*  52: 69 */     this.id = newID;
/*  53: 70 */     this.listID = newListID;
/*  54: 71 */     this.subscriberMSISDN = newSubscriberMSISDN;
/*  55: 72 */     this.phoneId = phoneId;
/*  56: 73 */     this.date = now();
/*  57: 74 */     this.downloadCompleted = flag;
/*  58: 75 */     this.voucher = voucherNo;
/*  59: 76 */     this.siteId = siteId;
/*  60: 77 */     this.isBilled = isBilled;
/*  61: 78 */     this.notifSent = notifSent;
/*  62: 79 */     this.keyword = kw;
/*  63: 80 */     this.pricePoint = pricePoint;
/*  64: 81 */     this.contentItem = new ContentItem();
/*  65: 82 */     this.format = new Format();
/*  66: 83 */     this.site = new CPSite();
/*  67:    */   }
/*  68:    */   
/*  69:    */   public Timestamp now()
/*  70:    */   {
/*  71: 90 */     Timestamp datetime = new Timestamp(Calendar.getInstance().getTimeInMillis());
/*  72:    */     
/*  73: 92 */     return datetime;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public void setTicketID(String newTicketID)
/*  77:    */   {
/*  78: 99 */     this.ticketID = newTicketID;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public void setID(String newID)
/*  82:    */   {
/*  83:107 */     this.id = newID;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void setListID(String newListID)
/*  87:    */   {
/*  88:114 */     this.listID = newListID;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void setSubscriberMSISDN(String newSubscriberMSISDN)
/*  92:    */   {
/*  93:122 */     this.subscriberMSISDN = newSubscriberMSISDN;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public void setPhoneId(String id)
/*  97:    */   {
/*  98:129 */     this.phoneId = id;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public void setDate(Timestamp newDate)
/* 102:    */   {
/* 103:136 */     this.date = newDate;
/* 104:    */   }
/* 105:    */   
/* 106:    */   public void setDownloadCompleted(boolean flag)
/* 107:    */   {
/* 108:143 */     this.downloadCompleted = flag;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public void setPin(String pin)
/* 112:    */     throws Exception
/* 113:    */   {
/* 114:147 */     this.voucher = pin;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public void setSiteId(String siteId)
/* 118:    */   {
/* 119:151 */     this.siteId = siteId;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public void setIsBilled(boolean isBilled)
/* 123:    */   {
/* 124:155 */     this.isBilled = isBilled;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public void setKeyword(String keyword)
/* 128:    */   {
/* 129:159 */     this.keyword = keyword;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public String getTicketID()
/* 133:    */   {
/* 134:166 */     return this.ticketID;
/* 135:    */   }
/* 136:    */   
/* 137:    */   public String getID()
/* 138:    */   {
/* 139:173 */     return this.id;
/* 140:    */   }
/* 141:    */   
/* 142:    */   public String getListID()
/* 143:    */   {
/* 144:180 */     return this.listID;
/* 145:    */   }
/* 146:    */   
/* 147:    */   public ContentItem getContentItem()
/* 148:    */   {
/* 149:184 */     return this.contentItem;
/* 150:    */   }
/* 151:    */   
/* 152:    */   public void setContentItem(ContentItem item)
/* 153:    */   {
/* 154:188 */     this.contentItem = item;
/* 155:    */   }
/* 156:    */   
/* 157:    */   public Format getFormat()
/* 158:    */   {
/* 159:192 */     return this.format;
/* 160:    */   }
/* 161:    */   
/* 162:    */   public void setFormat(Format format)
/* 163:    */   {
/* 164:196 */     this.format = format;
/* 165:    */   }
/* 166:    */   
/* 167:    */   public String getSubscriberMSISDN()
/* 168:    */   {
/* 169:203 */     return this.subscriberMSISDN;
/* 170:    */   }
/* 171:    */   
/* 172:    */   public String getPhoneId()
/* 173:    */   {
/* 174:210 */     return this.phoneId;
/* 175:    */   }
/* 176:    */   
/* 177:    */   public Date getDate()
/* 178:    */   {
/* 179:217 */     return this.date;
/* 180:    */   }
/* 181:    */   
/* 182:    */   public boolean getDownloadCompleted()
/* 183:    */   {
/* 184:224 */     return this.downloadCompleted;
/* 185:    */   }
/* 186:    */   
/* 187:    */   public String getPin()
/* 188:    */   {
/* 189:228 */     return this.voucher;
/* 190:    */   }
/* 191:    */   
/* 192:    */   public String getSiteId()
/* 193:    */   {
/* 194:232 */     return this.siteId;
/* 195:    */   }
/* 196:    */   
/* 197:    */   public boolean isIsBilled()
/* 198:    */   {
/* 199:236 */     return this.isBilled;
/* 200:    */   }
/* 201:    */   
/* 202:    */   public String getKeyword()
/* 203:    */   {
/* 204:240 */     return this.keyword;
/* 205:    */   }
/* 206:    */   
/* 207:    */   public CPSite getSite()
/* 208:    */   {
/* 209:244 */     return this.site;
/* 210:    */   }
/* 211:    */   
/* 212:    */   public void setSite(CPSite site)
/* 213:    */   {
/* 214:248 */     this.site = site;
/* 215:    */   }
/* 216:    */   
/* 217:    */   public static Transaction viewTransaction(String myTicketID)
/* 218:    */     throws Exception
/* 219:    */   {
/* 220:257 */     return TransactionDB.viewTransaction(myTicketID);
/* 221:    */   }
/* 222:    */   
/* 223:    */   public static ArrayList viewTransactions(String ownerId, String msisdn, String downloadStatus, Timestamp startdate, Timestamp enddate, int typeId, String supplierId, String pin, String siteId, String billedStatus, String notifSent)
/* 224:    */     throws Exception
/* 225:    */   {
/* 226:262 */     return TransactionDB.viewTransactions(ownerId, msisdn, downloadStatus, startdate, enddate, typeId, supplierId, pin, siteId, billedStatus, notifSent);
/* 227:    */   }
/* 228:    */   
/* 229:    */   public static void updateDownloadStatus(String ticketID, boolean downloadCompleted)
/* 230:    */     throws Exception
/* 231:    */   {
/* 232:279 */     TransactionDB.updateDownloadStatus(ticketID, downloadCompleted);
/* 233:    */   }
/* 234:    */   
/* 235:    */   public static void removeTransaction(String ticketID)
/* 236:    */     throws Exception
/* 237:    */   {
/* 238:290 */     TransactionDB.deleteTransaction(ticketID);
/* 239:    */   }
/* 240:    */   
/* 241:    */   public static void logTransaction(String ticketID, String id, String listID, String subscriberMSISDN, String phoneId, boolean downloadCompleted, String pin, String siteId, String keyword)
/* 242:    */     throws Exception
/* 243:    */   {
/* 244:303 */     TransactionDB.createTransaction(ticketID, id, listID, subscriberMSISDN, phoneId, downloadCompleted, pin, siteId, keyword);
/* 245:    */   }
/* 246:    */   
/* 247:    */   public static ArrayList getMostDownloadedThisMonth(int contentType)
/* 248:    */     throws Exception
/* 249:    */   {
/* 250:310 */     return TransactionDB.getMostDownloadedThisMonth(contentType);
/* 251:    */   }
/* 252:    */   
/* 253:    */   public static ArrayList getMostDownloadedLastMonth(int contentType)
/* 254:    */     throws Exception
/* 255:    */   {
/* 256:314 */     return TransactionDB.getMostDownloadedLastMonth(contentType);
/* 257:    */   }
/* 258:    */   
/* 259:    */   public static int totalDownloadsToday(String listId)
/* 260:    */     throws Exception
/* 261:    */   {
/* 262:319 */     return TransactionDB.totalDownloadsToday(listId);
/* 263:    */   }
/* 264:    */   
/* 265:    */   public static int totalDownloadsForDate(String listId, Date date)
/* 266:    */     throws Exception
/* 267:    */   {
/* 268:324 */     return TransactionDB.totalDownloadsForDate(listId, date);
/* 269:    */   }
/* 270:    */   
/* 271:    */   public static int totalDownloadsThisMonth(String list_id)
/* 272:    */     throws Exception
/* 273:    */   {
/* 274:329 */     return TransactionDB.totalDownloadsThisMonth(list_id);
/* 275:    */   }
/* 276:    */   
/* 277:    */   public static int totalDownloadsLastMonth(String list_id)
/* 278:    */     throws Exception
/* 279:    */   {
/* 280:334 */     return TransactionDB.totalDownloadsLastMonth(list_id);
/* 281:    */   }
/* 282:    */   
/* 283:    */   public static int totalIncompleteDownloads(String list_id)
/* 284:    */     throws Exception
/* 285:    */   {
/* 286:339 */     return TransactionDB.totalIncompleteDownloads(list_id);
/* 287:    */   }
/* 288:    */   
/* 289:    */   public static long totalBandwidthUsed(String list_id)
/* 290:    */     throws Exception
/* 291:    */   {
/* 292:344 */     return TransactionDB.totalBandwidthUsed(list_id);
/* 293:    */   }
/* 294:    */   
/* 295:    */   public static ArrayList fetchPopularDownloads(String cpid, int typeId)
/* 296:    */     throws Exception
/* 297:    */   {
/* 298:348 */     return TransactionDB.fetchPopularDownloads(cpid, typeId);
/* 299:    */   }
/* 300:    */   
/* 301:    */   public static void exportPopularDownloadsToCustomList(String cpid, int limit)
/* 302:    */     throws Exception
/* 303:    */   {
/* 304:352 */     TransactionDB.exportPopularDownloadsToCustomList(cpid, limit);
/* 305:    */   }
/* 306:    */   
/* 307:    */   public boolean getNotifSent()
/* 308:    */   {
/* 309:368 */     return this.notifSent;
/* 310:    */   }
/* 311:    */   
/* 312:    */   public void setNotifSent(boolean notifSent)
/* 313:    */   {
/* 314:372 */     this.notifSent = notifSent;
/* 315:    */   }
/* 316:    */   
/* 317:    */   public String getPricePoint()
/* 318:    */   {
/* 319:376 */     return this.pricePoint;
/* 320:    */   }
/* 321:    */   
/* 322:    */   public void setPricePoint(String pricePoint)
/* 323:    */   {
/* 324:380 */     this.pricePoint = pricePoint;
/* 325:    */   }
/* 326:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.contentserver.Transaction
 * JD-Core Version:    0.7.0.1
 */