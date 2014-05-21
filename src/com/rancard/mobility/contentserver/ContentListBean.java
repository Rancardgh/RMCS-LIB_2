/*   1:    */ package com.rancard.mobility.contentserver;
/*   2:    */ 
/*   3:    */ import com.rancard.util.Page;
/*   4:    */ import java.io.File;
/*   5:    */ import java.io.PrintStream;
/*   6:    */ import java.sql.Date;
/*   7:    */ import java.util.ArrayList;
/*   8:    */ import java.util.List;
/*   9:    */ import java.util.StringTokenizer;
/*  10:    */ 
/*  11:    */ public class ContentListBean
/*  12:    */ {
/*  13:    */   private String contentId;
/*  14:    */   private String id;
/*  15:    */   private String listId;
/*  16:    */   private String title;
/*  17:    */   private Integer type;
/*  18:    */   private String downloadUrl;
/*  19:    */   private String previewUrl;
/*  20:    */   private String formats;
/*  21:    */   private String price;
/*  22:    */   private Integer category;
/*  23:    */   private String author;
/*  24:    */   private String other_details;
/*  25:    */   private Date date_added;
/*  26:    */   private Long size;
/*  27:    */   private File contentFile;
/*  28:    */   private File contentPreviewFile;
/*  29:    */   private ArrayList availableFormats;
/*  30:    */   private int start;
/*  31: 31 */   private int count = 5;
/*  32:    */   
/*  33:    */   public void setContentId(String id)
/*  34:    */   {
/*  35: 40 */     this.contentId = id;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setid(String id)
/*  39:    */   {
/*  40: 44 */     this.id = id;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void settitle(String title)
/*  44:    */   {
/*  45: 48 */     this.title = title;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void settype(Integer type)
/*  49:    */   {
/*  50: 52 */     this.type = type;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void setdownload_url(String download_url)
/*  54:    */   {
/*  55: 56 */     this.downloadUrl = download_url;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void setformats(String formats)
/*  59:    */   {
/*  60: 60 */     ArrayList tempFmtLst = new ArrayList();
/*  61: 61 */     this.formats = formats;
/*  62: 62 */     StringTokenizer st = new StringTokenizer(this.formats, ",");
/*  63: 63 */     while (st.hasMoreTokens()) {
/*  64: 64 */       tempFmtLst.add(st.nextToken());
/*  65:    */     }
/*  66: 66 */     this.availableFormats = tempFmtLst;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void setCount(int count)
/*  70:    */   {
/*  71: 70 */     this.count = count;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void setStart(int start)
/*  75:    */   {
/*  76: 74 */     this.start = start;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void setPreviewUrl(String previewUrl)
/*  80:    */   {
/*  81: 78 */     this.previewUrl = previewUrl;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void setListId(String listId)
/*  85:    */   {
/*  86: 82 */     this.listId = listId;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public void setDownloadUrl(String downloadUrl)
/*  90:    */   {
/*  91: 86 */     this.downloadUrl = downloadUrl;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public void setPrice(String price)
/*  95:    */   {
/*  96: 90 */     this.price = price;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public void setCategory(Integer category)
/* 100:    */   {
/* 101: 94 */     this.category = category;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public void setAuthor(String author)
/* 105:    */   {
/* 106: 98 */     this.author = author;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public void setSize(Long size)
/* 110:    */   {
/* 111:102 */     this.size = size;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public void setAvailableFormats(ArrayList availableFormats)
/* 115:    */   {
/* 116:106 */     this.availableFormats = availableFormats;
/* 117:    */   }
/* 118:    */   
/* 119:    */   public void setDate_Added(Date date_added)
/* 120:    */   {
/* 121:110 */     this.date_added = date_added;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public void setOther_Details(String other_details)
/* 125:    */   {
/* 126:114 */     this.other_details = other_details;
/* 127:    */   }
/* 128:    */   
/* 129:    */   public String getContentId()
/* 130:    */   {
/* 131:119 */     return this.contentId;
/* 132:    */   }
/* 133:    */   
/* 134:    */   public String getid()
/* 135:    */   {
/* 136:123 */     return this.id;
/* 137:    */   }
/* 138:    */   
/* 139:    */   public String gettitle()
/* 140:    */   {
/* 141:127 */     return this.title;
/* 142:    */   }
/* 143:    */   
/* 144:    */   public Integer gettype()
/* 145:    */   {
/* 146:131 */     return this.type;
/* 147:    */   }
/* 148:    */   
/* 149:    */   public String getdownload_url()
/* 150:    */   {
/* 151:135 */     return this.downloadUrl;
/* 152:    */   }
/* 153:    */   
/* 154:    */   public String getformats()
/* 155:    */   {
/* 156:139 */     return this.formats;
/* 157:    */   }
/* 158:    */   
/* 159:    */   public ArrayList getAvailableFormats()
/* 160:    */   {
/* 161:143 */     return this.availableFormats;
/* 162:    */   }
/* 163:    */   
/* 164:    */   public int getCount()
/* 165:    */   {
/* 166:147 */     return this.count;
/* 167:    */   }
/* 168:    */   
/* 169:    */   public String getDownloadUrl()
/* 170:    */   {
/* 171:151 */     return this.downloadUrl;
/* 172:    */   }
/* 173:    */   
/* 174:    */   public String getListId()
/* 175:    */   {
/* 176:155 */     return this.listId;
/* 177:    */   }
/* 178:    */   
/* 179:    */   public String getPreviewUrl()
/* 180:    */   {
/* 181:159 */     return this.previewUrl;
/* 182:    */   }
/* 183:    */   
/* 184:    */   public int getStart()
/* 185:    */   {
/* 186:163 */     return this.start;
/* 187:    */   }
/* 188:    */   
/* 189:    */   public String getPrice()
/* 190:    */   {
/* 191:167 */     return this.price;
/* 192:    */   }
/* 193:    */   
/* 194:    */   public Integer getCategory()
/* 195:    */   {
/* 196:171 */     return this.category;
/* 197:    */   }
/* 198:    */   
/* 199:    */   public String getAuthor()
/* 200:    */   {
/* 201:175 */     return this.author;
/* 202:    */   }
/* 203:    */   
/* 204:    */   public Long getSize()
/* 205:    */   {
/* 206:179 */     return this.size;
/* 207:    */   }
/* 208:    */   
/* 209:    */   public Date getDate_Added()
/* 210:    */   {
/* 211:183 */     return this.date_added;
/* 212:    */   }
/* 213:    */   
/* 214:    */   public String getOther_Details()
/* 215:    */   {
/* 216:187 */     return this.other_details;
/* 217:    */   }
/* 218:    */   
/* 219:    */   public Page ViewContentList()
/* 220:    */     throws Exception
/* 221:    */   {
/* 222:195 */     ContentListDB content_list = new ContentListDB();
/* 223:    */     
/* 224:197 */     Page pg = Page.EMPTY_PAGE;
/* 225:198 */     return content_list.viewContentList(this.listId, this.title, this.formats, this.type, this.start, this.count);
/* 226:    */   }
/* 227:    */   
/* 228:    */   public Page ViewContentList(String listId, String title, Integer format, Integer type, int start, int count)
/* 229:    */     throws Exception
/* 230:    */   {
/* 231:210 */     ContentListDB content_list = new ContentListDB();
/* 232:    */     
/* 233:212 */     Page pg = Page.EMPTY_PAGE;
/* 234:213 */     return content_list.viewContentList(listId, title, this.formats, type, start, count);
/* 235:    */   }
/* 236:    */   
/* 237:    */   public Page SearchContentList(String listId, String title, String format, Integer type, String author, String price, Integer category, int isFree, int start, int count)
/* 238:    */     throws Exception
/* 239:    */   {
/* 240:225 */     ContentListDB content_list = new ContentListDB();
/* 241:    */     
/* 242:227 */     Page pg = Page.EMPTY_PAGE;
/* 243:228 */     return content_list.searchContentList(listId, title, format, type, author, price, category, isFree, start, count);
/* 244:    */   }
/* 245:    */   
/* 246:    */   public List getDistinctListIDs(String queryEnding)
/* 247:    */   {
/* 248:232 */     List listIDs = new ArrayList();
/* 249:    */     try
/* 250:    */     {
/* 251:234 */       listIDs = new ContentListDB().getDistinctListIDs(queryEnding);
/* 252:    */     }
/* 253:    */     catch (Exception e)
/* 254:    */     {
/* 255:237 */       System.out.println("Exception at getDistinctListIDs(): " + e.getMessage());
/* 256:    */     }
/* 257:240 */     return listIDs;
/* 258:    */   }
/* 259:    */   
/* 260:    */   public ContentItem getBean(String id, String listId)
/* 261:    */     throws Exception
/* 262:    */   {
/* 263:245 */     return new ContentListDB().viewcontent_list(id, listId);
/* 264:    */   }
/* 265:    */   
/* 266:    */   public ContentItem getBean(String contentId)
/* 267:    */     throws Exception
/* 268:    */   {
/* 269:249 */     return new ContentListDB().viewcontent_list(contentId);
/* 270:    */   }
/* 271:    */   
/* 272:    */   public boolean isEqualTo(ContentListBean clb2)
/* 273:    */   {
/* 274:254 */     boolean flag = false;
/* 275:255 */     if ((getListId().equals(clb2.getListId())) && (getid().equals(clb2.getid()))) {
/* 276:257 */       flag = true;
/* 277:    */     }
/* 278:259 */     return flag;
/* 279:    */   }
/* 280:    */   
/* 281:    */   public ArrayList getRecentlyAdded(int typeId, int count)
/* 282:    */     throws Exception
/* 283:    */   {
/* 284:267 */     return new ContentListDB().getRecentlyAdded(typeId, count);
/* 285:    */   }
/* 286:    */   
/* 287:    */   public static void main(String[] args)
/* 288:    */   {
/* 289:272 */     ContentListBean cb = new ContentListBean();
/* 290:    */   }
/* 291:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.ContentListBean
 * JD-Core Version:    0.7.0.1
 */