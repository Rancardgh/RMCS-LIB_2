/*    1:     */ package com.rancard.mobility.contentserver;
/*    2:     */ 
/*    3:     */ import com.rancard.common.DConnect;
/*    4:     */ import com.rancard.mobility.contentprovider.User;
/*    5:     */ import java.sql.Blob;
/*    6:     */ import java.sql.Connection;
/*    7:     */ import java.sql.PreparedStatement;
/*    8:     */ import java.sql.ResultSet;
/*    9:     */ import java.sql.Statement;
/*   10:     */ import java.sql.Timestamp;
/*   11:     */ import java.util.ArrayList;
/*   12:     */ import java.util.Calendar;
/*   13:     */ import java.util.StringTokenizer;
/*   14:     */ 
/*   15:     */ public abstract class TransactionDB
/*   16:     */ {
/*   17:     */   public static void createTransaction(String ticketID, String id, String listID, String subscriberMSISDN, String phoneId, boolean downloadCompleted, String pin, String siteId, String keyword)
/*   18:     */     throws Exception
/*   19:     */   {
/*   20:  29 */     ResultSet rs = null;
/*   21:  30 */     Connection con = null;
/*   22:  31 */     PreparedStatement prepstat = null;
/*   23:     */     try
/*   24:     */     {
/*   25:  34 */       con = DConnect.getConnection();
/*   26:  35 */       String query = "INSERT into download_log(ticketId,id,list_id,subscriberMSISDN,phone_id,date_of_download,status,pin,site_id,keyword) values(?,?,?,?,?,?,?,?,?,?)";
/*   27:     */       
/*   28:     */ 
/*   29:  38 */       prepstat = con.prepareStatement(query);
/*   30:  39 */       prepstat.setString(1, ticketID);
/*   31:  40 */       prepstat.setString(2, id);
/*   32:  41 */       prepstat.setString(3, listID);
/*   33:  42 */       prepstat.setString(4, subscriberMSISDN);
/*   34:  43 */       prepstat.setString(5, phoneId);
/*   35:  44 */       prepstat.setTimestamp(6, new Timestamp(Calendar.getInstance().getTime().getTime()));
/*   36:  48 */       if (downloadCompleted) {
/*   37:  49 */         prepstat.setInt(7, 1);
/*   38:     */       } else {
/*   39:  51 */         prepstat.setInt(7, 0);
/*   40:     */       }
/*   41:  53 */       prepstat.setString(8, pin);
/*   42:  54 */       prepstat.setString(9, siteId);
/*   43:  55 */       prepstat.setString(10, keyword);
/*   44:     */       
/*   45:  57 */       prepstat.execute();
/*   46:     */     }
/*   47:     */     catch (Exception ex)
/*   48:     */     {
/*   49:  59 */       if (con != null) {
/*   50:  60 */         con.close();
/*   51:     */       }
/*   52:  62 */       throw new Exception(ex.getMessage());
/*   53:     */     }
/*   54:  64 */     if (con != null) {
/*   55:  65 */       con.close();
/*   56:     */     }
/*   57:     */   }
/*   58:     */   
/*   59:     */   public static void updateDownloadStatus(String ticketID, boolean downloadCompleted)
/*   60:     */     throws Exception
/*   61:     */   {
/*   62:  71 */     ResultSet rs = null;
/*   63:  72 */     Connection con = null;
/*   64:  73 */     PreparedStatement prepstat = null;
/*   65:     */     try
/*   66:     */     {
/*   67:  76 */       int status = downloadCompleted ? 1 : 0;
/*   68:  77 */       con = DConnect.getConnection();
/*   69:  78 */       String query = "UPDATE download_log SET date_of_download=?,status=? WHERE ticketId='" + ticketID + "'";
/*   70:  79 */       prepstat = con.prepareStatement(query);
/*   71:  80 */       prepstat.setTimestamp(1, new Timestamp(Calendar.getInstance().getTime().getTime()));
/*   72:  81 */       prepstat.setInt(2, status);
/*   73:  82 */       prepstat.execute();
/*   74:     */     }
/*   75:     */     catch (Exception ex)
/*   76:     */     {
/*   77:  84 */       if (con != null) {
/*   78:  85 */         con.close();
/*   79:     */       }
/*   80:  87 */       throw new Exception(ex.getMessage());
/*   81:     */     }
/*   82:  89 */     if (con != null) {
/*   83:  90 */       con.close();
/*   84:     */     }
/*   85:     */   }
/*   86:     */   
/*   87:     */   public static void deleteTransaction(String ticketID)
/*   88:     */     throws Exception
/*   89:     */   {
/*   90:  99 */     ResultSet rs = null;
/*   91: 100 */     Connection con = null;
/*   92: 101 */     PreparedStatement prepstat = null;
/*   93:     */     try
/*   94:     */     {
/*   95: 104 */       con = DConnect.getConnection();
/*   96: 105 */       String query = "DELETE from download_log WHERE ticketId=?";
/*   97: 106 */       prepstat = con.prepareStatement(query);
/*   98: 107 */       prepstat.setString(1, ticketID);
/*   99: 108 */       prepstat.execute();
/*  100:     */     }
/*  101:     */     catch (Exception ex)
/*  102:     */     {
/*  103: 110 */       if (con != null) {
/*  104: 111 */         con.close();
/*  105:     */       }
/*  106: 113 */       throw new Exception(ex.getMessage());
/*  107:     */     }
/*  108: 115 */     if (con != null) {
/*  109: 116 */       con.close();
/*  110:     */     }
/*  111:     */   }
/*  112:     */   
/*  113:     */   public static Transaction viewTransaction(String ticketID)
/*  114:     */     throws Exception
/*  115:     */   {
/*  116: 124 */     Transaction newBean = new Transaction();
/*  117: 125 */     ContentItem item = new ContentItem();
/*  118:     */     
/*  119:     */ 
/*  120: 128 */     ResultSet rs = null;
/*  121: 129 */     Connection con = null;
/*  122: 130 */     PreparedStatement prepstat = null;
/*  123:     */     
/*  124: 132 */     int formatId = Integer.parseInt(ticketID.split("-")[1]);
/*  125:     */     try
/*  126:     */     {
/*  127: 135 */       con = DConnect.getConnection();
/*  128:     */       
/*  129: 137 */       String query = "select * from download_log d inner join content_list c on d.id=c.id and d.list_id=c.list_id inner join format_list f on c.formats=f.format_id inner join service_route t on c.content_type=t.service_type inner join cp_user u on d.list_id=u.id  inner join transactions tr on tr.trans_id=d.ticketId inner join cp_sites cps on d.site_id=cps.site_id WHERE d.ticketId=?";
/*  130:     */       
/*  131:     */ 
/*  132:     */ 
/*  133: 141 */       prepstat = con.prepareStatement(query);
/*  134: 142 */       prepstat.setString(1, ticketID);
/*  135: 143 */       rs = prepstat.executeQuery();
/*  136: 145 */       while (rs.next())
/*  137:     */       {
/*  138: 147 */         newBean.setTicketID(rs.getString("d.ticketid"));
/*  139: 148 */         newBean.setSubscriberMSISDN(rs.getString("d.subscriberMSISDN"));
/*  140:     */         
/*  141: 150 */         newBean.setPhoneId(rs.getString("d.phone_id"));
/*  142: 151 */         newBean.setDate(rs.getTimestamp("d.date_of_download"));
/*  143: 152 */         newBean.setPin(rs.getString("d.pin"));
/*  144: 153 */         if (rs.getInt("d.status") == 1) {
/*  145: 154 */           newBean.setDownloadCompleted(true);
/*  146:     */         } else {
/*  147: 156 */           newBean.setDownloadCompleted(false);
/*  148:     */         }
/*  149: 158 */         newBean.setSiteId(rs.getString("d.site_id"));
/*  150: 159 */         if (rs.getInt("tr.is_billed") == 1) {
/*  151: 160 */           newBean.setIsBilled(true);
/*  152:     */         } else {
/*  153: 162 */           newBean.setIsBilled(false);
/*  154:     */         }
/*  155: 164 */         if (rs.getInt("tr.is_completed") == 1) {
/*  156: 165 */           newBean.setNotifSent(true);
/*  157:     */         } else {
/*  158: 167 */           newBean.setNotifSent(false);
/*  159:     */         }
/*  160: 169 */         newBean.setPricePoint(rs.getString("tr.price_point_id"));
/*  161: 170 */         newBean.setKeyword(rs.getString("d.keyword"));
/*  162:     */         
/*  163:     */ 
/*  164: 173 */         CPSite site = new CPSite();
/*  165: 174 */         site.setCpId(rs.getString("cps.cp_id"));
/*  166: 175 */         site.setCpSiteId(rs.getString("cps.site_id"));
/*  167: 176 */         site.setCpSiteName(rs.getString("cps.site_name"));
/*  168:     */         
/*  169:     */ 
/*  170: 179 */         Format format = new Format(rs.getInt("f.format_id"), rs.getString("f.file_ext"), rs.getString("f.push_bearer"), rs.getString("f.mime_type"));
/*  171:     */         
/*  172:     */ 
/*  173:     */ 
/*  174:     */ 
/*  175:     */ 
/*  176: 185 */         ContentType type = new ContentType(rs.getString("t.service_name"), rs.getInt("t.service_type"), rs.getInt("t.parent_service_type"));
/*  177:     */         
/*  178:     */ 
/*  179:     */ 
/*  180: 189 */         User cp = new User();
/*  181:     */         
/*  182: 191 */         cp.setName(rs.getString("u.name"));
/*  183: 192 */         cp.setDefaultSmsc(rs.getString("default_smsc"));
/*  184: 193 */         cp.setId(rs.getString("u.id"));
/*  185: 194 */         cp.setLogoUrl(rs.getString("logo_url"));
/*  186: 195 */         cp.setPassword(rs.getString("password"));
/*  187: 196 */         cp.setUsername(rs.getString("username"));
/*  188:     */         
/*  189:     */ 
/*  190: 199 */         item = new ContentItem();
/*  191: 200 */         item.setAuthor(rs.getString("c.author"));
/*  192: 201 */         if (rs.getInt("c.show") == 1) {
/*  193: 202 */           item.setCanList(true);
/*  194:     */         } else {
/*  195: 204 */           item.setCanList(false);
/*  196:     */         }
/*  197: 206 */         item.setCategory(new Integer(rs.getInt("c.category")));
/*  198:     */         
/*  199: 208 */         item.setContentId(rs.getString("c.content_id"));
/*  200: 209 */         item.setContentTypeDetails(type);
/*  201: 210 */         item.setDate_Added(rs.getTimestamp("c.date_added"));
/*  202: 211 */         item.setDownloadUrl(rs.getString("c.download_url"));
/*  203: 212 */         item.setFormat(format);
/*  204: 213 */         item.setid(rs.getString("c.id"));
/*  205: 214 */         item.setListId(rs.getString("c.list_id"));
/*  206: 215 */         item.setOther_Details(rs.getString("c.other_details"));
/*  207: 216 */         item.setPreviewUrl(rs.getString("c.preview_url"));
/*  208: 217 */         item.setPrice(rs.getString("c.price"));
/*  209: 218 */         item.setSize(new Long(rs.getLong("c.size")));
/*  210: 219 */         if (rs.getInt("c.isLocal") == 1) {
/*  211: 220 */           item.setIsLocal(true);
/*  212:     */         } else {
/*  213: 222 */           item.setIsLocal(false);
/*  214:     */         }
/*  215: 224 */         if (rs.getInt("c.isLocal") == 1) {
/*  216: 225 */           item.setPreviewExists(new Boolean(true));
/*  217:     */         } else {
/*  218: 227 */           item.setPreviewExists(new Boolean(false));
/*  219:     */         }
/*  220: 229 */         item.settitle(rs.getString("c.title"));
/*  221: 230 */         item.setShortItemRef(rs.getString("c.short_item_ref"));
/*  222: 231 */         item.setSupplierId(rs.getString("supplier_id"));
/*  223: 232 */         item.setProviderDetails(cp);
/*  224:     */         
/*  225: 234 */         newBean.setContentItem(item);
/*  226: 235 */         newBean.setFormat(format);
/*  227: 236 */         newBean.setSite(site);
/*  228:     */       }
/*  229:     */     }
/*  230:     */     catch (Exception ex)
/*  231:     */     {
/*  232: 240 */       if (con != null) {
/*  233: 241 */         con.close();
/*  234:     */       }
/*  235: 243 */       throw new Exception(ex.getMessage());
/*  236:     */     }
/*  237: 245 */     if (con != null) {
/*  238: 246 */       con.close();
/*  239:     */     }
/*  240: 248 */     return newBean;
/*  241:     */   }
/*  242:     */   
/*  243:     */   public static ArrayList viewTransactions(String ownerId, String msisdn, String downloadStatus, Timestamp startdate, Timestamp enddate, int typeId, String supplierId, String pin, String siteId, String billedStatus, String notifSent)
/*  244:     */     throws Exception
/*  245:     */   {
/*  246: 254 */     ArrayList transactions = new ArrayList();
/*  247: 255 */     ContentItem item = new ContentItem();
/*  248:     */     
/*  249:     */ 
/*  250: 258 */     ResultSet rs = null;
/*  251: 259 */     Connection con = null;
/*  252: 260 */     PreparedStatement prepstat = null;
/*  253:     */     try
/*  254:     */     {
/*  255: 263 */       con = DConnect.getConnection();
/*  256:     */       
/*  257: 265 */       String siteIDquery = new String();
/*  258:     */       
/*  259:     */ 
/*  260: 268 */       String query = "select site_id from cp_sites where cp_id='" + ownerId + "'";
/*  261: 269 */       prepstat = con.prepareStatement(query);
/*  262: 270 */       rs = prepstat.executeQuery();
/*  263: 271 */       while (rs.next()) {
/*  264: 272 */         siteIDquery = siteIDquery + "d.site_id='" + rs.getString("site_id") + "' or ";
/*  265:     */       }
/*  266: 274 */       siteIDquery = siteIDquery.substring(0, siteIDquery.lastIndexOf("' or ") + 1);
/*  267:     */       
/*  268:     */ 
/*  269: 277 */       query = "select * from download_log d inner join content_list c on d.id=c.id and d.list_id=c.list_id inner join format_list f on c.formats=f.format_id inner join service_route t on c.content_type=t.service_type inner join cp_user u on d.list_id=u.id  inner join transactions tr on tr.trans_id=d.ticketId where d.list_id='" + ownerId + "' ";
/*  270: 281 */       if ((msisdn != null) && (!msisdn.equals(""))) {
/*  271: 282 */         query = query + " and d.subscriberMSISDN like '%" + msisdn + "%'";
/*  272:     */       }
/*  273: 284 */       if ((downloadStatus != null) && ((downloadStatus.equals("1")) || (downloadStatus.equals("0")))) {
/*  274: 285 */         query = query + " and d.status=" + Integer.parseInt(downloadStatus);
/*  275:     */       }
/*  276: 287 */       if (startdate != null) {
/*  277: 288 */         query = query + " and d.date_of_download >='" + startdate + "'";
/*  278:     */       }
/*  279: 290 */       if (enddate != null) {
/*  280: 291 */         query = query + " and d.date_of_download <='" + enddate + "'";
/*  281:     */       }
/*  282: 293 */       if ((siteId != null) && (!siteId.equals(""))) {
/*  283: 294 */         query = query + " and d.site_id='" + siteId + "'";
/*  284:     */       }
/*  285: 296 */       if ((supplierId != null) && (!supplierId.equals("*"))) {
/*  286: 297 */         query = query + " and c.supplier_id ='" + supplierId + "'";
/*  287:     */       }
/*  288: 299 */       if ((pin != null) && (!pin.equals(""))) {
/*  289: 300 */         query = query + " and d.pin='" + pin + "'";
/*  290:     */       }
/*  291: 302 */       if ((billedStatus != null) && ((billedStatus.equals("1")) || (billedStatus.equals("0")))) {
/*  292: 303 */         query = query + " and tr.is_billed=" + Integer.parseInt(billedStatus);
/*  293:     */       }
/*  294: 305 */       if (typeId != 0) {
/*  295: 306 */         query = query + " and d.id=c.id and d.list_id=c.list_id and c.content_type=" + typeId;
/*  296:     */       }
/*  297: 308 */       if ((siteId == null) || (siteId.equals(""))) {
/*  298: 309 */         query = query + " and (" + siteIDquery + ")";
/*  299:     */       }
/*  300: 311 */       if ((notifSent != null) && ((notifSent.equals("1")) || (notifSent.equals("0")))) {
/*  301: 312 */         query = query + " and tr.is_completed=" + Integer.parseInt(notifSent);
/*  302:     */       }
/*  303: 314 */       query = query + " order by d.date_of_download desc";
/*  304:     */       
/*  305: 316 */       prepstat = con.prepareStatement(query);
/*  306: 317 */       rs = prepstat.executeQuery();
/*  307: 319 */       while (rs.next())
/*  308:     */       {
/*  309: 320 */         Transaction newBean = new Transaction();
/*  310:     */         
/*  311: 322 */         newBean.setTicketID(rs.getString("d.ticketid"));
/*  312: 323 */         newBean.setSubscriberMSISDN(rs.getString("d.subscriberMSISDN"));
/*  313: 324 */         newBean.setPhoneId(rs.getString("d.phone_id"));
/*  314: 325 */         newBean.setDate(rs.getTimestamp("d.date_of_download"));
/*  315: 326 */         newBean.setPin(rs.getString("d.pin"));
/*  316: 327 */         if (rs.getInt("d.status") == 1) {
/*  317: 328 */           newBean.setDownloadCompleted(true);
/*  318:     */         } else {
/*  319: 330 */           newBean.setDownloadCompleted(false);
/*  320:     */         }
/*  321: 332 */         newBean.setSiteId(rs.getString("d.site_id"));
/*  322: 333 */         if (rs.getInt("tr.is_billed") == 1) {
/*  323: 334 */           newBean.setIsBilled(true);
/*  324:     */         } else {
/*  325: 336 */           newBean.setIsBilled(false);
/*  326:     */         }
/*  327: 338 */         if (rs.getInt("tr.is_completed") == 1) {
/*  328: 339 */           newBean.setNotifSent(true);
/*  329:     */         } else {
/*  330: 341 */           newBean.setNotifSent(false);
/*  331:     */         }
/*  332: 343 */         newBean.setPricePoint(rs.getString("tr.price_point_id"));
/*  333: 344 */         newBean.setKeyword(rs.getString("d.keyword"));
/*  334:     */         
/*  335:     */ 
/*  336: 347 */         Format format = new Format(rs.getInt("f.format_id"), rs.getString("f.file_ext"), rs.getString("f.push_bearer"), rs.getString("f.mime_type"));
/*  337:     */         
/*  338:     */ 
/*  339: 350 */         ContentType type = new ContentType(rs.getString("t.service_name"), rs.getInt("t.service_type"), rs.getInt("t.parent_service_type"));
/*  340:     */         
/*  341:     */ 
/*  342: 353 */         User cp = new User();
/*  343: 354 */         cp.setName(rs.getString("u.name"));
/*  344: 355 */         cp.setDefaultSmsc(rs.getString("default_smsc"));
/*  345: 356 */         cp.setId(rs.getString("u.id"));
/*  346: 357 */         cp.setLogoUrl(rs.getString("logo_url"));
/*  347: 358 */         cp.setPassword(rs.getString("password"));
/*  348: 359 */         cp.setUsername(rs.getString("username"));
/*  349:     */         
/*  350:     */ 
/*  351: 362 */         item = new ContentItem();
/*  352: 363 */         item.setAuthor(rs.getString("c.author"));
/*  353: 364 */         if (rs.getInt("c.show") == 1) {
/*  354: 365 */           item.setCanList(true);
/*  355:     */         } else {
/*  356: 367 */           item.setCanList(false);
/*  357:     */         }
/*  358: 369 */         item.setCategory(new Integer(rs.getInt("c.category")));
/*  359: 370 */         item.setContentId(rs.getString("c.content_id"));
/*  360: 371 */         item.setContentTypeDetails(type);
/*  361: 372 */         item.setDate_Added(rs.getTimestamp("c.date_added"));
/*  362: 373 */         item.setDownloadUrl(rs.getString("c.download_url"));
/*  363: 374 */         item.setFormat(format);
/*  364: 375 */         item.setid(rs.getString("c.id"));
/*  365: 376 */         item.setListId(rs.getString("c.list_id"));
/*  366: 377 */         item.setOther_Details(rs.getString("c.other_details"));
/*  367: 378 */         item.setPreviewUrl(rs.getString("c.preview_url"));
/*  368: 379 */         item.setPrice(rs.getString("c.price"));
/*  369: 380 */         item.setSize(new Long(rs.getLong("c.size")));
/*  370: 381 */         if (rs.getInt("c.isLocal") == 1) {
/*  371: 382 */           item.setIsLocal(true);
/*  372:     */         } else {
/*  373: 384 */           item.setIsLocal(false);
/*  374:     */         }
/*  375: 386 */         if (rs.getInt("c.isLocal") == 1) {
/*  376: 387 */           item.setPreviewExists(new Boolean(true));
/*  377:     */         } else {
/*  378: 389 */           item.setPreviewExists(new Boolean(false));
/*  379:     */         }
/*  380: 391 */         if (rs.getInt("c.is_free") == 1) {
/*  381: 392 */           item.setFree(true);
/*  382:     */         } else {
/*  383: 394 */           item.setFree(false);
/*  384:     */         }
/*  385: 396 */         item.settitle(rs.getString("c.title"));
/*  386: 397 */         item.setShortItemRef(rs.getString("c.short_item_ref"));
/*  387: 398 */         item.setSupplierId(rs.getString("c.supplier_id"));
/*  388:     */         
/*  389: 400 */         item.setProviderDetails(cp);
/*  390:     */         
/*  391: 402 */         newBean.setContentItem(item);
/*  392: 403 */         newBean.setFormat(format);
/*  393:     */         
/*  394: 405 */         transactions.add(newBean);
/*  395:     */       }
/*  396:     */     }
/*  397:     */     catch (Exception ex)
/*  398:     */     {
/*  399: 408 */       if (con != null) {
/*  400: 409 */         con.close();
/*  401:     */       }
/*  402: 411 */       throw new Exception(ex.getMessage());
/*  403:     */     }
/*  404: 413 */     if (con != null) {
/*  405: 414 */       con.close();
/*  406:     */     }
/*  407: 416 */     return transactions;
/*  408:     */   }
/*  409:     */   
/*  410:     */   public static ArrayList getMostDownloadedThisMonth(int typeId)
/*  411:     */     throws Exception
/*  412:     */   {
/*  413: 425 */     Calendar calendar = Calendar.getInstance();
/*  414: 426 */     java.sql.Date today = new java.sql.Date(calendar.getTime().getTime());
/*  415: 427 */     String dateString = today.toString();
/*  416: 428 */     StringTokenizer st = new StringTokenizer(dateString, "-");
/*  417:     */     
/*  418:     */ 
/*  419: 431 */     int year = Integer.parseInt(st.nextToken());
/*  420: 432 */     int month = Integer.parseInt(st.nextToken());
/*  421:     */     
/*  422: 434 */     calendar.set(year, month - 1, 1, 0, 0, 0);
/*  423: 435 */     Timestamp firstDay = new Timestamp(calendar.getTimeInMillis());
/*  424:     */     
/*  425: 437 */     calendar.set(year, month, 1, 0, 0, 0);
/*  426: 438 */     Timestamp lastDay = new Timestamp(calendar.getTimeInMillis());
/*  427:     */     
/*  428:     */ 
/*  429:     */ 
/*  430: 442 */     ContentItem item = new ContentItem();
/*  431:     */     
/*  432: 444 */     ResultSet rs = null;
/*  433: 445 */     Connection con = null;
/*  434: 446 */     PreparedStatement prepstat = null;
/*  435:     */     ArrayList itemList;
/*  436:     */     try
/*  437:     */     {
/*  438: 449 */       con = DConnect.getConnection();
/*  439: 450 */       String query = "select * from download_log d inner join content_list c on d.id=c.id and d.list_id=c.list_id inner join format_list f on c.formats=f.format_id inner join service_route t on c.content_type=t.service_type inner join cp_user u on d.list_id=u.id where date_of_download>=? and date_of_download<=?";
/*  440:     */       
/*  441:     */ 
/*  442:     */ 
/*  443:     */ 
/*  444:     */ 
/*  445: 456 */       prepstat = con.prepareStatement(query);
/*  446: 457 */       prepstat.setTimestamp(1, firstDay);
/*  447: 458 */       prepstat.setTimestamp(2, lastDay);
/*  448: 459 */       rs = prepstat.executeQuery();
/*  449:     */       
/*  450: 461 */       int fetchSize = 0;
/*  451: 462 */       while (rs.next()) {
/*  452: 463 */         fetchSize++;
/*  453:     */       }
/*  454: 465 */       rs.beforeFirst();
/*  455: 466 */       itemList = new ArrayList(0);
/*  456: 467 */       itemList.add(0, "");
/*  457: 468 */       int[] itemCount = new int[fetchSize];
/*  458: 469 */       for (int i = 0; i < itemCount.length; i++) {
/*  459: 470 */         itemCount[i] = 0;
/*  460:     */       }
/*  461: 473 */       while (rs.next())
/*  462:     */       {
/*  463: 475 */         Format format = new Format(rs.getInt("f.format_id"), rs.getString("f.file_ext"), rs.getString("f.push_bearer"), rs.getString("f.mime_type"));
/*  464:     */         
/*  465:     */ 
/*  466: 478 */         ContentType type = new ContentType(rs.getString("t.service_name"), rs.getInt("t.service_type"), rs.getInt("t.parent_service_type"));
/*  467:     */         
/*  468:     */ 
/*  469: 481 */         User cp = new User();
/*  470: 482 */         cp.setName(rs.getString("u.name"));
/*  471: 483 */         cp.setDefaultSmsc(rs.getString("default_smsc"));
/*  472: 484 */         cp.setId(rs.getString("u.id"));
/*  473: 485 */         cp.setLogoUrl(rs.getString("logo_url"));
/*  474: 486 */         cp.setPassword(rs.getString("password"));
/*  475: 487 */         cp.setUsername(rs.getString("username"));
/*  476:     */         
/*  477:     */ 
/*  478: 490 */         item = new ContentItem();
/*  479: 491 */         item.setAuthor(rs.getString("c.author"));
/*  480: 492 */         if (rs.getInt("c.show") == 1) {
/*  481: 493 */           item.setCanList(true);
/*  482:     */         } else {
/*  483: 495 */           item.setCanList(false);
/*  484:     */         }
/*  485: 497 */         item.setCategory(new Integer(rs.getInt("c.category")));
/*  486: 498 */         item.setContentId(rs.getString("c.content_id"));
/*  487: 499 */         item.setContentTypeDetails(type);
/*  488: 500 */         item.setDate_Added(rs.getTimestamp("c.date_added"));
/*  489: 501 */         item.setDownloadUrl(rs.getString("c.download_url"));
/*  490: 502 */         item.setFormat(format);
/*  491: 503 */         item.setid(rs.getString("c.id"));
/*  492: 504 */         item.setListId(rs.getString("c.list_id"));
/*  493: 505 */         item.setOther_Details(rs.getString("c.other_details"));
/*  494: 506 */         item.setPreviewUrl(rs.getString("c.preview_url"));
/*  495: 507 */         item.setPrice(rs.getString("c.price"));
/*  496: 508 */         item.setSize(new Long(rs.getLong("c.size")));
/*  497: 509 */         if (rs.getInt("c.isLocal") == 1) {
/*  498: 510 */           item.setIsLocal(true);
/*  499:     */         } else {
/*  500: 512 */           item.setIsLocal(false);
/*  501:     */         }
/*  502: 514 */         if (rs.getInt("c.isLocal") == 1) {
/*  503: 515 */           item.setPreviewExists(new Boolean(true));
/*  504:     */         } else {
/*  505: 517 */           item.setPreviewExists(new Boolean(false));
/*  506:     */         }
/*  507: 519 */         item.settitle(rs.getString("c.title"));
/*  508: 520 */         item.setShortItemRef(rs.getString("c.short_item_ref"));
/*  509: 521 */         item.setSupplierId(rs.getString("c.supplier_id"));
/*  510: 522 */         item.setProviderDetails(cp);
/*  511: 524 */         for (int i = 0; i < fetchSize; i++)
/*  512:     */         {
/*  513: 525 */           if ((itemList.get(i).equals("")) || ((!((ContentItem)itemList.get(i)).isEqualTo(item)) && (i == fetchSize - 1)))
/*  514:     */           {
/*  515: 528 */             itemList.add(i, item);
/*  516: 529 */             itemCount[itemList.indexOf(item)] += 1;
/*  517: 530 */             break;
/*  518:     */           }
/*  519: 532 */           if (((ContentItem)itemList.get(i)).isEqualTo(item))
/*  520:     */           {
/*  521: 533 */             itemCount[i] += 1;
/*  522: 534 */             break;
/*  523:     */           }
/*  524:     */         }
/*  525:     */       }
/*  526: 538 */       itemList.remove("");
/*  527: 539 */       ArrayList items = new ArrayList();
/*  528: 540 */       double mean = 0.0D;
/*  529: 541 */       double sum = 0.0D;
/*  530: 542 */       for (int i = 0; i < itemList.size(); i++) {
/*  531: 543 */         if (((ContentItem)itemList.get(i)).gettype().intValue() == typeId)
/*  532:     */         {
/*  533: 545 */           StatStruct struct = new StatStruct((ContentItem)itemList.get(i), itemCount[i]);
/*  534:     */           
/*  535: 547 */           items.add(struct);
/*  536: 548 */           sum += struct.count;
/*  537:     */         }
/*  538:     */       }
/*  539: 551 */       if (sum != 0.0D) {
/*  540: 552 */         mean = sum / items.size();
/*  541:     */       } else {
/*  542: 554 */         mean = 0.0D;
/*  543:     */       }
/*  544: 556 */       itemList = new ArrayList();
/*  545: 557 */       for (int i = 0; i < items.size(); i++)
/*  546:     */       {
/*  547: 558 */         int downloadCounter = ((StatStruct)items.get(i)).count;
/*  548: 559 */         if (downloadCounter >= mean) {
/*  549: 560 */           itemList.add(((StatStruct)items.get(i)).clb);
/*  550:     */         }
/*  551:     */       }
/*  552:     */     }
/*  553:     */     catch (Exception e)
/*  554:     */     {
/*  555: 564 */       throw new Exception();
/*  556:     */     }
/*  557: 566 */     return itemList;
/*  558:     */   }
/*  559:     */   
/*  560:     */   public static ArrayList getMostDownloadedLastMonth(int typeId)
/*  561:     */     throws Exception
/*  562:     */   {
/*  563: 572 */     Calendar calendar = Calendar.getInstance();
/*  564: 573 */     java.sql.Date today = new java.sql.Date(calendar.getTime().getTime());
/*  565: 574 */     String dateString = today.toString();
/*  566: 575 */     StringTokenizer st = new StringTokenizer(dateString, "-");
/*  567:     */     
/*  568:     */ 
/*  569: 578 */     int year = Integer.parseInt(st.nextToken());
/*  570: 579 */     int month = Integer.parseInt(st.nextToken());
/*  571:     */     
/*  572: 581 */     calendar.set(year, month - 2, 1, 0, 0, 0);
/*  573: 582 */     Timestamp firstDay = new Timestamp(calendar.getTimeInMillis());
/*  574:     */     
/*  575: 584 */     calendar.set(year, month - 1, 1, 0, 0, 0);
/*  576: 585 */     Timestamp lastDay = new Timestamp(calendar.getTimeInMillis());
/*  577:     */     
/*  578:     */ 
/*  579:     */ 
/*  580: 589 */     ContentItem item = new ContentItem();
/*  581:     */     
/*  582: 591 */     ResultSet rs = null;
/*  583: 592 */     Connection con = null;
/*  584: 593 */     PreparedStatement prepstat = null;
/*  585:     */     ArrayList itemList;
/*  586:     */     try
/*  587:     */     {
/*  588: 596 */       con = DConnect.getConnection();
/*  589: 597 */       String query = "select * from download_log d inner join content_list c on d.id=c.id and d.list_id=c.list_id inner join format_list f on c.formats=f.format_id inner join service_route t on c.content_type=t.service_type inner join cp_user u on d.list_id=u.id where date_of_download>=? and date_of_download<=?";
/*  590:     */       
/*  591:     */ 
/*  592:     */ 
/*  593:     */ 
/*  594:     */ 
/*  595: 603 */       prepstat = con.prepareStatement(query);
/*  596: 604 */       prepstat.setTimestamp(1, firstDay);
/*  597: 605 */       prepstat.setTimestamp(2, lastDay);
/*  598: 606 */       rs = prepstat.executeQuery();
/*  599:     */       
/*  600: 608 */       int fetchSize = 0;
/*  601: 609 */       while (rs.next()) {
/*  602: 610 */         fetchSize++;
/*  603:     */       }
/*  604: 612 */       rs.beforeFirst();
/*  605: 613 */       itemList = new ArrayList(0);
/*  606: 614 */       itemList.add(0, "");
/*  607: 615 */       int[] itemCount = new int[fetchSize];
/*  608: 616 */       for (int i = 0; i < itemCount.length; i++) {
/*  609: 617 */         itemCount[i] = 0;
/*  610:     */       }
/*  611: 620 */       while (rs.next())
/*  612:     */       {
/*  613: 622 */         Format format = new Format(rs.getInt("f.format_id"), rs.getString("f.file_ext"), rs.getString("f.push_bearer"), rs.getString("f.mime_type"));
/*  614:     */         
/*  615:     */ 
/*  616: 625 */         ContentType type = new ContentType(rs.getString("t.service_name"), rs.getInt("t.service_type"), rs.getInt("t.parent_service_type"));
/*  617:     */         
/*  618:     */ 
/*  619: 628 */         User cp = new User();
/*  620: 629 */         cp.setName(rs.getString("u.name"));
/*  621: 630 */         cp.setDefaultSmsc(rs.getString("default_smsc"));
/*  622: 631 */         cp.setId(rs.getString("u.id"));
/*  623: 632 */         cp.setLogoUrl(rs.getString("logo_url"));
/*  624: 633 */         cp.setPassword(rs.getString("password"));
/*  625: 634 */         cp.setUsername(rs.getString("username"));
/*  626:     */         
/*  627:     */ 
/*  628: 637 */         item = new ContentItem();
/*  629: 638 */         item.setAuthor(rs.getString("c.author"));
/*  630: 639 */         if (rs.getInt("c.show") == 1) {
/*  631: 640 */           item.setCanList(true);
/*  632:     */         } else {
/*  633: 642 */           item.setCanList(false);
/*  634:     */         }
/*  635: 644 */         item.setCategory(new Integer(rs.getInt("c.category")));
/*  636:     */         
/*  637: 646 */         item.setContentId(rs.getString("c.content_id"));
/*  638: 647 */         item.setContentTypeDetails(type);
/*  639: 648 */         item.setDate_Added(rs.getTimestamp("c.date_added"));
/*  640: 649 */         item.setDownloadUrl(rs.getString("c.download_url"));
/*  641: 650 */         item.setFormat(format);
/*  642: 651 */         item.setid(rs.getString("c.id"));
/*  643: 652 */         item.setListId(rs.getString("c.list_id"));
/*  644: 653 */         item.setOther_Details(rs.getString("c.other_details"));
/*  645: 654 */         item.setPreviewUrl(rs.getString("c.preview_url"));
/*  646: 655 */         item.setPrice(rs.getString("c.price"));
/*  647: 656 */         item.setSize(new Long(rs.getLong("c.size")));
/*  648: 657 */         if (rs.getInt("c.isLocal") == 1) {
/*  649: 658 */           item.setIsLocal(true);
/*  650:     */         } else {
/*  651: 660 */           item.setIsLocal(false);
/*  652:     */         }
/*  653: 662 */         if (rs.getInt("c.isLocal") == 1) {
/*  654: 663 */           item.setPreviewExists(new Boolean(true));
/*  655:     */         } else {
/*  656: 665 */           item.setPreviewExists(new Boolean(false));
/*  657:     */         }
/*  658: 667 */         item.settitle(rs.getString("c.title"));
/*  659: 668 */         item.setShortItemRef(rs.getString("c.short_item_ref"));
/*  660: 669 */         item.setSupplierId(rs.getString("c.supplier_id"));
/*  661: 670 */         item.setProviderDetails(cp);
/*  662: 672 */         for (int i = 0; i < fetchSize; i++)
/*  663:     */         {
/*  664: 673 */           if ((itemList.get(i).equals("")) || ((!((ContentItem)itemList.get(i)).isEqualTo(item)) && (i == fetchSize - 1)))
/*  665:     */           {
/*  666: 676 */             itemList.add(i, item);
/*  667: 677 */             itemCount[itemList.indexOf(item)] += 1;
/*  668: 678 */             break;
/*  669:     */           }
/*  670: 680 */           if (((ContentItem)itemList.get(i)).isEqualTo(item))
/*  671:     */           {
/*  672: 681 */             itemCount[i] += 1;
/*  673: 682 */             break;
/*  674:     */           }
/*  675:     */         }
/*  676:     */       }
/*  677: 686 */       itemList.remove("");
/*  678: 687 */       ArrayList items = new ArrayList();
/*  679: 688 */       double mean = 0.0D;
/*  680: 689 */       double sum = 0.0D;
/*  681: 690 */       for (int i = 0; i < itemList.size(); i++) {
/*  682: 691 */         if (((ContentItem)itemList.get(i)).gettype().intValue() == typeId)
/*  683:     */         {
/*  684: 693 */           StatStruct struct = new StatStruct((ContentItem)itemList.get(i), itemCount[i]);
/*  685:     */           
/*  686: 695 */           items.add(struct);
/*  687: 696 */           sum += struct.count;
/*  688:     */         }
/*  689:     */       }
/*  690: 699 */       if (sum != 0.0D) {
/*  691: 700 */         mean = sum / items.size();
/*  692:     */       } else {
/*  693: 702 */         mean = 0.0D;
/*  694:     */       }
/*  695: 704 */       itemList = new ArrayList();
/*  696: 705 */       for (int i = 0; i < items.size(); i++)
/*  697:     */       {
/*  698: 706 */         int downloadCounter = ((StatStruct)items.get(i)).count;
/*  699: 707 */         if (downloadCounter >= mean) {
/*  700: 708 */           itemList.add(((StatStruct)items.get(i)).clb);
/*  701:     */         }
/*  702:     */       }
/*  703:     */     }
/*  704:     */     catch (Exception e)
/*  705:     */     {
/*  706: 712 */       throw new Exception();
/*  707:     */     }
/*  708: 714 */     return itemList;
/*  709:     */   }
/*  710:     */   
/*  711:     */   public static void exportPopularDownloadsToCustomList(String cpid, int limit)
/*  712:     */     throws Exception
/*  713:     */   {
/*  714: 719 */     if (limit > 10) {
/*  715: 719 */       limit = 10;
/*  716:     */     }
/*  717: 720 */     if (limit < 0) {
/*  718: 720 */       limit = 0;
/*  719:     */     }
/*  720: 721 */     java.sql.Date today = new java.sql.Date(Calendar.getInstance().getTime().getTime());
/*  721: 722 */     String dateString = today.toString();
/*  722: 723 */     StringTokenizer st = new StringTokenizer(dateString, "-");
/*  723: 724 */     st.nextToken();
/*  724: 725 */     int month = Integer.parseInt(st.nextToken()) - 1;
/*  725: 726 */     String monthStr = "";
/*  726: 727 */     if (month < 10) {
/*  727: 728 */       monthStr = "0" + month;
/*  728:     */     } else {
/*  729: 730 */       monthStr = "" + month;
/*  730:     */     }
/*  731: 734 */     ResultSet rs = null;
/*  732: 735 */     ResultSet rs2 = null;
/*  733: 736 */     ResultSet rs3 = null;
/*  734: 737 */     Connection con = null;
/*  735: 738 */     PreparedStatement prepstat = null;
/*  736: 739 */     Statement stat = null;
/*  737:     */     try
/*  738:     */     {
/*  739: 742 */       con = DConnect.getConnection();
/*  740:     */       
/*  741: 744 */       String customListId = null;
/*  742:     */       
/*  743: 746 */       String query = "select custom_list_id from custom_list_definition where cp_id=? and custom_list_name='popular downloads'";
/*  744: 747 */       prepstat = con.prepareStatement(query);
/*  745: 748 */       prepstat.setString(1, cpid);
/*  746: 749 */       rs = prepstat.executeQuery();
/*  747: 750 */       while (rs.next()) {
/*  748: 751 */         customListId = rs.getString("custom_list_id");
/*  749:     */       }
/*  750: 754 */       if (customListId != null)
/*  751:     */       {
/*  752: 755 */         query = "select service_type from service_route where parent_service_type=1";
/*  753: 756 */         prepstat = con.prepareStatement(query);
/*  754: 757 */         rs = prepstat.executeQuery();
/*  755: 758 */         while (rs.next())
/*  756:     */         {
/*  757: 759 */           String contentType = rs.getString("service_type");
/*  758: 760 */           query = "select dl.id, dl.list_id, COUNT(*) as hits from download_log dl inner join cp_sites cs on dl.site_id=cs.site_id inner join content_list cl on cl.id=dl.id and cl.list_id=dl.list_id where cs.cp_id='" + cpid + "' and cl.content_type=" + contentType + " and month(dl.date_of_download)='" + monthStr + "' group by dl.id order by hits desc limit " + limit;
/*  759:     */           
/*  760:     */ 
/*  761: 763 */           prepstat = con.prepareStatement(query);
/*  762: 764 */           rs2 = prepstat.executeQuery();
/*  763: 766 */           if (rs2.next())
/*  764:     */           {
/*  765: 768 */             query = "select cu.item_id, cu.prov_id from custom_list cu inner join content_list co on co.id=cu.item_id and co.list_id=cu.prov_id where co.content_type=" + contentType + " and cu.custom_list_id='" + customListId + "'";
/*  766:     */             
/*  767: 770 */             prepstat = con.prepareStatement(query);
/*  768: 771 */             rs3 = prepstat.executeQuery();
/*  769:     */             
/*  770:     */ 
/*  771: 774 */             int hasMore = 0;
/*  772: 775 */             con.setAutoCommit(false);
/*  773: 776 */             stat = con.createStatement();
/*  774: 777 */             while (rs3.next())
/*  775:     */             {
/*  776: 778 */               query = "delete from custom_list where custom_list_id='" + customListId + "' and item_id='" + rs3.getString("item_id") + "' and prov_id='" + rs3.getString("prov_id") + "'";
/*  777:     */               
/*  778: 780 */               stat.addBatch(query);
/*  779: 781 */               hasMore++;
/*  780:     */             }
/*  781: 783 */             if (hasMore > 0)
/*  782:     */             {
/*  783: 784 */               stat.executeBatch();
/*  784: 785 */               stat.clearBatch();
/*  785:     */             }
/*  786: 788 */             rs2.beforeFirst();
/*  787: 789 */             con.setAutoCommit(false);
/*  788: 790 */             con.createStatement();
/*  789: 791 */             stat = con.createStatement();
/*  790: 792 */             while (rs2.next())
/*  791:     */             {
/*  792: 793 */               query = "INSERT into custom_list (custom_list_id, item_id, prov_id) values('" + customListId + "','" + rs2.getString("id") + "','" + rs2.getString("list_id") + "')";
/*  793:     */               
/*  794: 795 */               stat.addBatch(query);
/*  795:     */             }
/*  796: 797 */             stat.executeBatch();
/*  797: 798 */             stat.clearBatch();
/*  798:     */           }
/*  799:     */         }
/*  800:     */       }
/*  801:     */     }
/*  802:     */     catch (Exception e)
/*  803:     */     {
/*  804: 803 */       throw new Exception();
/*  805:     */     }
/*  806:     */   }
/*  807:     */   
/*  808:     */   public static ArrayList fetchPopularDownloads(String cpid, int typeId)
/*  809:     */     throws Exception
/*  810:     */   {
/*  811: 810 */     ResultSet rs = null;
/*  812: 811 */     ResultSet rs2 = null;
/*  813: 812 */     Connection con = null;
/*  814: 813 */     PreparedStatement prepstat = null;
/*  815: 814 */     String custimlistid = null;
/*  816: 815 */     ArrayList items = new ArrayList();
/*  817:     */     try
/*  818:     */     {
/*  819: 818 */       con = DConnect.getConnection();
/*  820: 819 */       String query = "select custom_list_id from custom_list_definition where cp_id=? and custom_list_name='popular downloads'";
/*  821: 820 */       prepstat = con.prepareStatement(query);
/*  822: 821 */       prepstat.setString(1, cpid);
/*  823: 822 */       rs = prepstat.executeQuery();
/*  824: 824 */       if (rs.next())
/*  825:     */       {
/*  826: 825 */         custimlistid = rs.getString("custom_list_id");
/*  827:     */         
/*  828: 827 */         query = "select item_id, prov_id from custom_list where custom_list_id=?";
/*  829: 828 */         prepstat = con.prepareStatement(query);
/*  830: 829 */         prepstat.setString(1, custimlistid);
/*  831: 830 */         rs = prepstat.executeQuery();
/*  832: 832 */         while (rs.next())
/*  833:     */         {
/*  834: 833 */           ContentItem item = new ContentItem();
/*  835: 834 */           Format format = new Format();
/*  836: 835 */           ContentType type = new ContentType();
/*  837: 836 */           User cp = new User();
/*  838:     */           
/*  839: 838 */           query = "SELECT * FROM content_list, service_route, cp_user, format_list where content_list.id=? and content_list.list_id=? and content_list.content_type=service_route.service_type and content_list.list_id=cp_user.id and content_list.formats=format_list.format_id and content_list.content_type=" + typeId;
/*  840:     */           
/*  841:     */ 
/*  842:     */ 
/*  843: 842 */           prepstat = con.prepareStatement(query);
/*  844: 843 */           String id = rs.getString("item_id");
/*  845: 844 */           String listid = rs.getString("prov_id");
/*  846:     */           
/*  847: 846 */           prepstat.setString(1, id);
/*  848: 847 */           prepstat.setString(2, listid);
/*  849:     */           
/*  850: 849 */           rs2 = prepstat.executeQuery();
/*  851: 850 */           while (rs2.next())
/*  852:     */           {
/*  853: 852 */             item.setContentId(rs2.getString("content_id"));
/*  854: 853 */             item.setid(rs2.getString("id"));
/*  855: 854 */             item.settitle(rs2.getString("title"));
/*  856: 855 */             item.settype(new Integer(rs2.getInt("content_type")));
/*  857: 856 */             item.setdownload_url(rs2.getString("download_url"));
/*  858: 857 */             item.setPreviewUrl(rs2.getString("preview_url"));
/*  859: 858 */             item.setPrice(rs2.getString("price"));
/*  860: 859 */             item.setAuthor(rs2.getString("author"));
/*  861: 860 */             item.setCategory(new Integer(rs2.getInt("category")));
/*  862: 861 */             item.setSize(new Long(rs2.getLong("size")));
/*  863: 862 */             item.setListId(rs2.getString("list_id"));
/*  864: 863 */             item.setDate_Added(rs2.getTimestamp("date_added"));
/*  865: 864 */             item.setOther_Details(rs2.getString("other_details"));
/*  866: 865 */             if (rs2.getInt("isLocal") == 1) {
/*  867: 866 */               item.setIsLocal(true);
/*  868:     */             } else {
/*  869: 868 */               item.setIsLocal(false);
/*  870:     */             }
/*  871: 870 */             if (rs2.getInt("show") == 1) {
/*  872: 871 */               item.setCanList(true);
/*  873:     */             } else {
/*  874: 873 */               item.setCanList(false);
/*  875:     */             }
/*  876: 875 */             if (rs2.getInt("is_free") == 1) {
/*  877: 876 */               item.setFree(true);
/*  878:     */             } else {
/*  879: 878 */               item.setFree(false);
/*  880:     */             }
/*  881: 880 */             item.setShortItemRef(rs2.getString("short_item_ref"));
/*  882: 881 */             item.setSupplierId(rs2.getString("supplier_id"));
/*  883:     */             
/*  884:     */ 
/*  885: 884 */             type.setParentServiceType(rs2.getInt("service_route.parent_service_type"));
/*  886: 885 */             type.setServiceName(rs2.getString("service_route.service_name"));
/*  887: 886 */             type.setServiceType(rs2.getInt("service_route.service_type"));
/*  888:     */             
/*  889:     */ 
/*  890: 889 */             cp.setId(rs2.getString("cp_user.id"));
/*  891: 890 */             cp.setName(rs2.getString("cp_user.name"));
/*  892: 891 */             cp.setUsername(rs2.getString("cp_user.username"));
/*  893: 892 */             cp.setPassword(rs2.getString("cp_user.password"));
/*  894: 893 */             cp.setDefaultSmsc(rs2.getString("cp_user.default_smsc"));
/*  895: 894 */             cp.setLogoUrl(rs2.getString("cp_user.logo_url"));
/*  896: 895 */             cp.setDefaultService(rs2.getString("cp_user.default_service"));
/*  897:     */             
/*  898:     */ 
/*  899: 898 */             format.setId(rs2.getInt("format_list.format_id"));
/*  900: 899 */             format.setFileExt(rs2.getString("format_list.file_ext"));
/*  901: 900 */             format.setMimeType(rs2.getString("format_list.mime_type"));
/*  902: 901 */             format.setPushBearer(rs2.getString("format_list.push_bearer"));
/*  903:     */           }
/*  904: 903 */           item.setContentTypeDetails(type);
/*  905: 904 */           item.setProviderDetails(cp);
/*  906: 905 */           item.setFormat(format);
/*  907:     */           
/*  908: 907 */           items.add(item);
/*  909:     */         }
/*  910:     */       }
/*  911:     */     }
/*  912:     */     catch (Exception ex)
/*  913:     */     {
/*  914: 911 */       if (con != null) {
/*  915: 912 */         con.close();
/*  916:     */       }
/*  917: 914 */       throw new Exception(ex.getMessage());
/*  918:     */     }
/*  919: 916 */     if (con != null) {
/*  920: 917 */       con.close();
/*  921:     */     }
/*  922: 920 */     return items;
/*  923:     */   }
/*  924:     */   
/*  925:     */   public static int totalDownloadsToday(String list_id)
/*  926:     */     throws Exception
/*  927:     */   {
/*  928: 924 */     Calendar calendar = Calendar.getInstance();
/*  929: 925 */     java.sql.Date today = new java.sql.Date(calendar.getTime().getTime());
/*  930:     */     
/*  931: 927 */     int number = 0;
/*  932:     */     
/*  933: 929 */     ResultSet rs = null;
/*  934: 930 */     Connection con = null;
/*  935: 931 */     PreparedStatement prepstat = null;
/*  936:     */     try
/*  937:     */     {
/*  938: 934 */       con = DConnect.getConnection();
/*  939: 935 */       String query = "select count(*) as number from download_log where date(date_of_download) =? and list_id=? and status=1";
/*  940: 936 */       prepstat = con.prepareStatement(query);
/*  941: 937 */       prepstat.setDate(1, today);
/*  942: 938 */       prepstat.setString(2, list_id);
/*  943: 939 */       rs = prepstat.executeQuery();
/*  944: 941 */       while (rs.next()) {
/*  945: 942 */         number = rs.getInt("number");
/*  946:     */       }
/*  947:     */     }
/*  948:     */     catch (Exception e)
/*  949:     */     {
/*  950: 945 */       throw new Exception();
/*  951:     */     }
/*  952: 947 */     return number;
/*  953:     */   }
/*  954:     */   
/*  955:     */   public static int totalDownloadsThisMonth(String list_id)
/*  956:     */     throws Exception
/*  957:     */   {
/*  958: 951 */     Calendar calendar = Calendar.getInstance();
/*  959: 952 */     java.sql.Date today = new java.sql.Date(calendar.getTime().getTime());
/*  960: 953 */     String dateString = today.toString();
/*  961:     */     
/*  962: 955 */     int number = 0;
/*  963:     */     
/*  964: 957 */     ResultSet rs = null;
/*  965: 958 */     Connection con = null;
/*  966: 959 */     PreparedStatement prepstat = null;
/*  967:     */     try
/*  968:     */     {
/*  969: 962 */       con = DConnect.getConnection();
/*  970: 963 */       String query = "select count(*) as number from download_log where month(date_of_download)=month(?) and list_id=? and status=1";
/*  971: 964 */       prepstat = con.prepareStatement(query);
/*  972: 965 */       prepstat.setDate(1, today);
/*  973: 966 */       prepstat.setString(2, list_id);
/*  974: 967 */       rs = prepstat.executeQuery();
/*  975: 969 */       while (rs.next()) {
/*  976: 970 */         number = rs.getInt("number");
/*  977:     */       }
/*  978:     */     }
/*  979:     */     catch (Exception e)
/*  980:     */     {
/*  981: 973 */       throw new Exception();
/*  982:     */     }
/*  983: 975 */     return number;
/*  984:     */   }
/*  985:     */   
/*  986:     */   public static int totalDownloadsLastMonth(String list_id)
/*  987:     */     throws Exception
/*  988:     */   {
/*  989: 979 */     Calendar calendar = Calendar.getInstance();
/*  990: 980 */     calendar.add(2, -1);
/*  991: 981 */     java.sql.Date then = new java.sql.Date(calendar.getTime().getTime());
/*  992:     */     
/*  993: 983 */     int number = 0;
/*  994:     */     
/*  995: 985 */     ResultSet rs = null;
/*  996: 986 */     Connection con = null;
/*  997: 987 */     PreparedStatement prepstat = null;
/*  998:     */     try
/*  999:     */     {
/* 1000: 990 */       con = DConnect.getConnection();
/* 1001: 991 */       String query = "select count(*) as number from download_log where month(date_of_download)=month(?) and list_id=? and status=1";
/* 1002: 992 */       prepstat = con.prepareStatement(query);
/* 1003: 993 */       prepstat.setDate(1, then);
/* 1004: 994 */       prepstat.setString(2, list_id);
/* 1005: 995 */       rs = prepstat.executeQuery();
/* 1006: 997 */       while (rs.next()) {
/* 1007: 998 */         number = rs.getInt("number");
/* 1008:     */       }
/* 1009:     */     }
/* 1010:     */     catch (Exception e)
/* 1011:     */     {
/* 1012:1001 */       throw new Exception();
/* 1013:     */     }
/* 1014:1003 */     return number;
/* 1015:     */   }
/* 1016:     */   
/* 1017:     */   public static int totalDownloadsForDate(String list_id, java.util.Date date)
/* 1018:     */     throws Exception
/* 1019:     */   {
/* 1020:1007 */     java.sql.Date today = new java.sql.Date(date.getTime());
/* 1021:     */     
/* 1022:1009 */     int number = 0;
/* 1023:     */     
/* 1024:1011 */     ResultSet rs = null;
/* 1025:1012 */     Connection con = null;
/* 1026:1013 */     PreparedStatement prepstat = null;
/* 1027:     */     try
/* 1028:     */     {
/* 1029:1016 */       con = DConnect.getConnection();
/* 1030:1017 */       String query = "select count(*) as number from download_log where date(date_of_download)=? and list_id=? and status=1";
/* 1031:1018 */       prepstat = con.prepareStatement(query);
/* 1032:1019 */       prepstat.setDate(1, today);
/* 1033:1020 */       prepstat.setString(2, list_id);
/* 1034:1021 */       rs = prepstat.executeQuery();
/* 1035:1023 */       while (rs.next()) {
/* 1036:1024 */         number = rs.getInt("number");
/* 1037:     */       }
/* 1038:     */     }
/* 1039:     */     catch (Exception e)
/* 1040:     */     {
/* 1041:1027 */       throw new Exception();
/* 1042:     */     }
/* 1043:1029 */     return number;
/* 1044:     */   }
/* 1045:     */   
/* 1046:     */   public static int totalIncompleteDownloads(String list_id)
/* 1047:     */     throws Exception
/* 1048:     */   {
/* 1049:1033 */     int number = 0;
/* 1050:     */     
/* 1051:1035 */     ResultSet rs = null;
/* 1052:1036 */     Connection con = null;
/* 1053:1037 */     PreparedStatement prepstat = null;
/* 1054:     */     try
/* 1055:     */     {
/* 1056:1040 */       con = DConnect.getConnection();
/* 1057:1041 */       String query = "select count(*) as number from download_log where list_id=? and status=0";
/* 1058:1042 */       prepstat = con.prepareStatement(query);
/* 1059:1043 */       prepstat.setString(1, list_id);
/* 1060:1044 */       rs = prepstat.executeQuery();
/* 1061:1046 */       while (rs.next()) {
/* 1062:1047 */         number = rs.getInt("number");
/* 1063:     */       }
/* 1064:     */     }
/* 1065:     */     catch (Exception e)
/* 1066:     */     {
/* 1067:1050 */       throw new Exception();
/* 1068:     */     }
/* 1069:1052 */     return number;
/* 1070:     */   }
/* 1071:     */   
/* 1072:     */   public static long totalBandwidthUsed(String list_id)
/* 1073:     */     throws Exception
/* 1074:     */   {
/* 1075:1056 */     long bandwidth = 0L;
/* 1076:     */     
/* 1077:1058 */     ResultSet rs = null;
/* 1078:1059 */     Connection con = null;
/* 1079:1060 */     PreparedStatement prepstat = null;
/* 1080:     */     try
/* 1081:     */     {
/* 1082:1063 */       con = DConnect.getConnection();
/* 1083:1064 */       String query = "select uploads.binaryfile from uploads, download_log where download_log.list_id=? and status=1 and download_log.list_id=uploads.list_id and download_log.id=uploads.id";
/* 1084:     */       
/* 1085:1066 */       prepstat = con.prepareStatement(query);
/* 1086:1067 */       prepstat.setString(1, list_id);
/* 1087:1068 */       rs = prepstat.executeQuery();
/* 1088:1070 */       while (rs.next())
/* 1089:     */       {
/* 1090:1071 */         Blob b = rs.getBlob("binaryfile");
/* 1091:1072 */         if (b != null) {
/* 1092:1073 */           bandwidth += b.length();
/* 1093:     */         }
/* 1094:     */       }
/* 1095:     */     }
/* 1096:     */     catch (Exception e)
/* 1097:     */     {
/* 1098:1077 */       throw new Exception();
/* 1099:     */     }
/* 1100:1079 */     return bandwidth;
/* 1101:     */   }
/* 1102:     */   
/* 1103:     */   public static void main(String[] args)
/* 1104:     */     throws Exception
/* 1105:     */   {}
/* 1106:     */   
/* 1107:     */   static class StatStruct
/* 1108:     */   {
/* 1109:     */     ContentItem clb;
/* 1110:     */     int count;
/* 1111:     */     
/* 1112:     */     public StatStruct(ContentItem clb_new, int count_new)
/* 1113:     */     {
/* 1114:1407 */       this.clb = clb_new;
/* 1115:1408 */       this.count = count_new;
/* 1116:     */     }
/* 1117:     */   }
/* 1118:     */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.contentserver.TransactionDB
 * JD-Core Version:    0.7.0.1
 */