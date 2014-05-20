/*   1:    */ package com.rancard.mobility.contentserver;
/*   2:    */ 
/*   3:    */ import com.rancard.mobility.contentprovider.User;
/*   4:    */ import com.rancard.util.Page;
/*   5:    */ import java.io.PrintStream;
/*   6:    */ import java.io.Serializable;
/*   7:    */ import java.sql.Timestamp;
/*   8:    */ import java.util.ArrayList;
/*   9:    */ import java.util.HashMap;
/*  10:    */ import java.util.List;
/*  11:    */ 
/*  12:    */ public class ContentItem
/*  13:    */   implements Serializable
/*  14:    */ {
/*  15:    */   private String contentId;
/*  16:    */   private String id;
/*  17:    */   private String listId;
/*  18:    */   private String title;
/*  19:    */   private Integer type;
/*  20:    */   private String downloadUrl;
/*  21:    */   private String previewUrl;
/*  22:    */   private String price;
/*  23:    */   private Integer category;
/*  24:    */   private String author;
/*  25:    */   private String other_details;
/*  26:    */   private Timestamp date_added;
/*  27:    */   private Long size;
/*  28:    */   private boolean canList;
/*  29:    */   private boolean isLocal;
/*  30:    */   private boolean free;
/*  31:    */   private String supplierId;
/*  32:    */   private String shortItemRef;
/*  33:    */   private ContentType contentTypeDetails;
/*  34:    */   private User providerDetails;
/*  35:    */   private Format format;
/*  36:    */   private ArrayList availableFormats;
/*  37:    */   private int start;
/*  38: 40 */   private int count = 5;
/*  39:    */   private Boolean previewExists;
/*  40:    */   
/*  41:    */   public void setContentId(String id)
/*  42:    */   {
/*  43: 50 */     this.contentId = id;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void setid(String id)
/*  47:    */   {
/*  48: 54 */     this.id = id;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void settitle(String title)
/*  52:    */   {
/*  53: 58 */     this.title = title;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void settype(Integer type)
/*  57:    */   {
/*  58: 62 */     this.type = type;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void setdownload_url(String download_url)
/*  62:    */   {
/*  63: 66 */     this.downloadUrl = download_url;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public void setFormat(Format format)
/*  67:    */   {
/*  68: 70 */     this.format = format;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void setCount(int count)
/*  72:    */   {
/*  73: 74 */     this.count = count;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public void setStart(int start)
/*  77:    */   {
/*  78: 78 */     this.start = start;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public void setPreviewExists(Boolean previewExists)
/*  82:    */   {
/*  83: 82 */     this.previewExists = previewExists;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void setPreviewUrl(String previewUrl)
/*  87:    */   {
/*  88: 86 */     this.previewUrl = previewUrl;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void setListId(String listId)
/*  92:    */   {
/*  93: 90 */     this.listId = listId;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public void setDownloadUrl(String downloadUrl)
/*  97:    */   {
/*  98: 94 */     this.downloadUrl = downloadUrl;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public void setPrice(String price)
/* 102:    */   {
/* 103: 98 */     this.price = price;
/* 104:    */   }
/* 105:    */   
/* 106:    */   public void setCategory(Integer category)
/* 107:    */   {
/* 108:102 */     this.category = category;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public void setAuthor(String author)
/* 112:    */   {
/* 113:106 */     this.author = author;
/* 114:    */   }
/* 115:    */   
/* 116:    */   public void setSize(Long size)
/* 117:    */   {
/* 118:110 */     this.size = size;
/* 119:    */   }
/* 120:    */   
/* 121:    */   public void setAvailableFormats(ArrayList availableFormats)
/* 122:    */   {
/* 123:114 */     this.availableFormats = availableFormats;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public void setDate_Added(Timestamp date_added)
/* 127:    */   {
/* 128:118 */     this.date_added = date_added;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public void setOther_Details(String other_details)
/* 132:    */   {
/* 133:122 */     this.other_details = other_details;
/* 134:    */   }
/* 135:    */   
/* 136:    */   public void setIsLocal(boolean flag)
/* 137:    */   {
/* 138:126 */     this.isLocal = flag;
/* 139:    */   }
/* 140:    */   
/* 141:    */   public void setCanList(boolean flag)
/* 142:    */   {
/* 143:130 */     this.canList = flag;
/* 144:    */   }
/* 145:    */   
/* 146:    */   public String getContentId()
/* 147:    */   {
/* 148:135 */     return this.contentId;
/* 149:    */   }
/* 150:    */   
/* 151:    */   public String getid()
/* 152:    */   {
/* 153:139 */     return this.id;
/* 154:    */   }
/* 155:    */   
/* 156:    */   public String gettitle()
/* 157:    */   {
/* 158:143 */     return this.title;
/* 159:    */   }
/* 160:    */   
/* 161:    */   public Integer gettype()
/* 162:    */   {
/* 163:147 */     return this.type;
/* 164:    */   }
/* 165:    */   
/* 166:    */   public String getdownload_url()
/* 167:    */   {
/* 168:151 */     return this.downloadUrl;
/* 169:    */   }
/* 170:    */   
/* 171:    */   public Format getFormat()
/* 172:    */   {
/* 173:155 */     return this.format;
/* 174:    */   }
/* 175:    */   
/* 176:    */   public ArrayList getAvailableFormats()
/* 177:    */   {
/* 178:159 */     return this.availableFormats;
/* 179:    */   }
/* 180:    */   
/* 181:    */   public int getCount()
/* 182:    */   {
/* 183:163 */     return this.count;
/* 184:    */   }
/* 185:    */   
/* 186:    */   public String getDownloadUrl()
/* 187:    */   {
/* 188:167 */     return this.downloadUrl;
/* 189:    */   }
/* 190:    */   
/* 191:    */   public String getListId()
/* 192:    */   {
/* 193:171 */     return this.listId;
/* 194:    */   }
/* 195:    */   
/* 196:    */   public String getPreviewUrl()
/* 197:    */   {
/* 198:175 */     return this.previewUrl;
/* 199:    */   }
/* 200:    */   
/* 201:    */   public int getStart()
/* 202:    */   {
/* 203:179 */     return this.start;
/* 204:    */   }
/* 205:    */   
/* 206:    */   public String getPrice()
/* 207:    */   {
/* 208:183 */     return this.price;
/* 209:    */   }
/* 210:    */   
/* 211:    */   public Integer getCategory()
/* 212:    */   {
/* 213:187 */     return this.category;
/* 214:    */   }
/* 215:    */   
/* 216:    */   public String getAuthor()
/* 217:    */   {
/* 218:191 */     return this.author;
/* 219:    */   }
/* 220:    */   
/* 221:    */   public Long getSize()
/* 222:    */   {
/* 223:195 */     return this.size;
/* 224:    */   }
/* 225:    */   
/* 226:    */   public Boolean getPreviewExists()
/* 227:    */   {
/* 228:199 */     return this.previewExists;
/* 229:    */   }
/* 230:    */   
/* 231:    */   public Timestamp getDate_Added()
/* 232:    */   {
/* 233:203 */     return this.date_added;
/* 234:    */   }
/* 235:    */   
/* 236:    */   public String getOther_Details()
/* 237:    */   {
/* 238:207 */     return this.other_details;
/* 239:    */   }
/* 240:    */   
/* 241:    */   public ContentType getContentTypeDetails()
/* 242:    */   {
/* 243:211 */     return this.contentTypeDetails;
/* 244:    */   }
/* 245:    */   
/* 246:    */   public boolean islocal()
/* 247:    */   {
/* 248:215 */     return this.isLocal;
/* 249:    */   }
/* 250:    */   
/* 251:    */   public boolean canList()
/* 252:    */   {
/* 253:219 */     return this.canList;
/* 254:    */   }
/* 255:    */   
/* 256:    */   public void setContentTypeDetails(ContentType type)
/* 257:    */   {
/* 258:223 */     this.contentTypeDetails = type;
/* 259:    */   }
/* 260:    */   
/* 261:    */   public User getProviderDetails()
/* 262:    */   {
/* 263:227 */     return this.providerDetails;
/* 264:    */   }
/* 265:    */   
/* 266:    */   public void setProviderDetails(User cp)
/* 267:    */   {
/* 268:231 */     this.providerDetails = cp;
/* 269:    */   }
/* 270:    */   
/* 271:    */   public boolean isFree()
/* 272:    */   {
/* 273:235 */     return this.free;
/* 274:    */   }
/* 275:    */   
/* 276:    */   public void setFree(boolean free)
/* 277:    */   {
/* 278:239 */     this.free = free;
/* 279:    */   }
/* 280:    */   
/* 281:    */   public Page ViewContentList()
/* 282:    */     throws Exception
/* 283:    */   {
/* 284:247 */     ContentListDB content_list = new ContentListDB();
/* 285:    */     
/* 286:249 */     Page pg = Page.EMPTY_PAGE;
/* 287:250 */     return content_list.viewContentList(this.listId, this.title, "" + this.format.getId(), this.type, this.start, this.count);
/* 288:    */   }
/* 289:    */   
/* 290:    */   public Page ViewContentList(String listId, String title, String format, Integer type, int start, int count)
/* 291:    */     throws Exception
/* 292:    */   {
/* 293:260 */     ContentListDB content_list = new ContentListDB();
/* 294:    */     
/* 295:262 */     Page pg = Page.EMPTY_PAGE;
/* 296:263 */     return content_list.viewContentList(listId, title, format, type, start, count);
/* 297:    */   }
/* 298:    */   
/* 299:    */   public Page SearchContentList(String listId, String title, String format, Integer type, String author, String price, Integer category, int isFree, int start, int count)
/* 300:    */     throws Exception
/* 301:    */   {
/* 302:273 */     ContentListDB content_list = new ContentListDB();
/* 303:    */     
/* 304:275 */     Page pg = Page.EMPTY_PAGE;
/* 305:276 */     return content_list.searchContentList(listId, title, format, type, author, price, category, isFree, start, count);
/* 306:    */   }
/* 307:    */   
/* 308:    */   public Page searchContentListByPhone(String ownerId, String title, String format, Integer type, String author, String price, Integer category, String phoneId, int isFree, String shortItemRef, String supplierId, int start, int count)
/* 309:    */     throws Exception
/* 310:    */   {
/* 311:285 */     ContentListDB content_list = new ContentListDB();
/* 312:    */     
/* 313:287 */     Page pg = Page.EMPTY_PAGE;
/* 314:288 */     return content_list.searchContentListByPhone(ownerId, title, format, type, author, price, category, phoneId, isFree, shortItemRef, supplierId, start, count);
/* 315:    */   }
/* 316:    */   
/* 317:    */   public List getDistinctListIDs(String queryEnding)
/* 318:    */   {
/* 319:293 */     List listIDs = new ArrayList();
/* 320:    */     try
/* 321:    */     {
/* 322:295 */       listIDs = new ContentListDB().getDistinctListIDs(queryEnding);
/* 323:    */     }
/* 324:    */     catch (Exception e)
/* 325:    */     {
/* 326:298 */       System.out.println("Exception at getDistinctListIDs(): " + e.getMessage());
/* 327:    */     }
/* 328:301 */     return listIDs;
/* 329:    */   }
/* 330:    */   
/* 331:    */   public ContentItem viewContentItem(String id, String listId)
/* 332:    */     throws Exception
/* 333:    */   {
/* 334:305 */     return new ContentListDB().viewcontent_list(id, listId);
/* 335:    */   }
/* 336:    */   
/* 337:    */   public static String resolveShortItemReference(String shortItemRef, String accountId, String keyword)
/* 338:    */     throws Exception
/* 339:    */   {
/* 340:309 */     return ContentListDB.resolveShortItemReference(shortItemRef, accountId, keyword);
/* 341:    */   }
/* 342:    */   
/* 343:    */   public ContentItem viewContentItem(String contentId)
/* 344:    */     throws Exception
/* 345:    */   {
/* 346:313 */     return new ContentListDB().viewcontent_list(contentId);
/* 347:    */   }
/* 348:    */   
/* 349:    */   public boolean isEqualTo(ContentItem clb2)
/* 350:    */   {
/* 351:317 */     boolean flag = false;
/* 352:318 */     if ((getListId().equals(clb2.getListId())) && (getid().equals(clb2.getid()))) {
/* 353:320 */       flag = true;
/* 354:    */     }
/* 355:322 */     return flag;
/* 356:    */   }
/* 357:    */   
/* 358:    */   public String[][] getCPsForTypes()
/* 359:    */     throws Exception
/* 360:    */   {
/* 361:326 */     return ContentListDB.getCPsForTypes();
/* 362:    */   }
/* 363:    */   
/* 364:    */   public ArrayList getRecentlyAdded(int typeId, int count)
/* 365:    */     throws Exception
/* 366:    */   {
/* 367:334 */     return new ContentListDB().getRecentlyAdded(typeId, count);
/* 368:    */   }
/* 369:    */   
/* 370:    */   public ArrayList getRecentlyAdded(String cpid, int typeId)
/* 371:    */     throws Exception
/* 372:    */   {
/* 373:339 */     return new ContentListDB().getRecentlyAdded(cpid, typeId);
/* 374:    */   }
/* 375:    */   
/* 376:    */   public void updateContentItem(String id, String price, Integer category, String author, String listId)
/* 377:    */     throws Exception
/* 378:    */   {
/* 379:348 */     ContentListDB db = new ContentListDB();
/* 380:349 */     db.updateContentItem(id, price, category, author, listId);
/* 381:    */   }
/* 382:    */   
/* 383:    */   public void updateContentItem(String id, String price, String title, Integer category, String author, boolean isFree, String shortItemRef, String listId)
/* 384:    */     throws Exception
/* 385:    */   {
/* 386:359 */     ContentListDB db = new ContentListDB();
/* 387:360 */     db.updateContentItem(id, price, title, category, author, isFree, shortItemRef, listId);
/* 388:    */   }
/* 389:    */   
/* 390:    */   public void updateContentItemsPrice(String[] ids, String newprice, String listId)
/* 391:    */     throws Exception
/* 392:    */   {
/* 393:368 */     ContentListDB db = new ContentListDB();
/* 394:369 */     db.updateContentList(ids, newprice, listId);
/* 395:    */   }
/* 396:    */   
/* 397:    */   public void updateContentItemsPrice(String[] ids, String[] newprice, String listId)
/* 398:    */     throws Exception
/* 399:    */   {
/* 400:373 */     ContentListDB db = new ContentListDB();
/* 401:374 */     db.updateContentList(ids, newprice, listId);
/* 402:    */   }
/* 403:    */   
/* 404:    */   public void updateContentItemsCategory(String[] ids, Integer newcategory, String listId)
/* 405:    */     throws Exception
/* 406:    */   {
/* 407:382 */     ContentListDB db = new ContentListDB();
/* 408:383 */     db.updateContentList(ids, newcategory, listId);
/* 409:    */   }
/* 410:    */   
/* 411:    */   public void updateContentItemsAuthor(String[] ids, String newauthor, String listId)
/* 412:    */     throws Exception
/* 413:    */   {
/* 414:390 */     ContentListDB db = new ContentListDB();
/* 415:391 */     db.updateContentListAuthor(ids, newauthor, listId);
/* 416:    */   }
/* 417:    */   
/* 418:    */   public void updateContentListFreebie(String[] id, boolean isFree, String listId)
/* 419:    */     throws Exception
/* 420:    */   {
/* 421:395 */     ContentListDB db = new ContentListDB();
/* 422:396 */     db.updateContentListFreebie(id, isFree, listId);
/* 423:    */   }
/* 424:    */   
/* 425:    */   public static HashMap itemCountByType(String listId)
/* 426:    */     throws Exception
/* 427:    */   {
/* 428:403 */     return ContentListDB.itemCountByType(listId);
/* 429:    */   }
/* 430:    */   
/* 431:    */   public static int itemCountByType(String listId, int[] typeIDs)
/* 432:    */     throws Exception
/* 433:    */   {
/* 434:409 */     return ContentListDB.itemCountByType(listId, typeIDs);
/* 435:    */   }
/* 436:    */   
/* 437:    */   public String getSupplierId()
/* 438:    */   {
/* 439:413 */     return this.supplierId;
/* 440:    */   }
/* 441:    */   
/* 442:    */   public void setSupplierId(String supplierId)
/* 443:    */   {
/* 444:417 */     this.supplierId = supplierId;
/* 445:    */   }
/* 446:    */   
/* 447:    */   public String getShortItemRef()
/* 448:    */   {
/* 449:421 */     return this.shortItemRef;
/* 450:    */   }
/* 451:    */   
/* 452:    */   public void setShortItemRef(String shortItemRef)
/* 453:    */   {
/* 454:425 */     this.shortItemRef = shortItemRef;
/* 455:    */   }
/* 456:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.ContentItem
 * JD-Core Version:    0.7.0.1
 */