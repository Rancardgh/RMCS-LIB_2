/*   1:    */ package com.rancard.mobility.contentserver;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import java.sql.Connection;
/*   5:    */ import java.sql.PreparedStatement;
/*   6:    */ import java.sql.ResultSet;
/*   7:    */ import java.util.ArrayList;
/*   8:    */ import java.util.HashMap;
/*   9:    */ import java.util.List;
/*  10:    */ 
/*  11:    */ public abstract class CategoryDB
/*  12:    */ {
/*  13:    */   public static void insertCategory(String categoryDesc, int contentType)
/*  14:    */     throws Exception
/*  15:    */   {
/*  16: 31 */     ResultSet rs = null;
/*  17: 32 */     Connection con = null;
/*  18: 33 */     PreparedStatement prepstat = null;
/*  19:    */     try
/*  20:    */     {
/*  21: 36 */       con = DConnect.getConnection();
/*  22: 37 */       String query = "INSERT into category_list(content_desc,content_type_id) values(?,?)";
/*  23:    */       
/*  24:    */ 
/*  25: 40 */       prepstat = con.prepareStatement(query);
/*  26: 41 */       prepstat.setString(1, categoryDesc);
/*  27: 42 */       prepstat.setInt(2, contentType);
/*  28:    */       
/*  29: 44 */       prepstat.execute();
/*  30:    */     }
/*  31:    */     catch (Exception ex)
/*  32:    */     {
/*  33: 46 */       if (con != null) {
/*  34: 47 */         con.close();
/*  35:    */       }
/*  36: 49 */       throw new Exception(ex.getMessage());
/*  37:    */     }
/*  38: 51 */     if (con != null) {
/*  39: 52 */       con.close();
/*  40:    */     }
/*  41:    */   }
/*  42:    */   
/*  43:    */   public static void updateCategory(int id, String contentDesc, int contentType)
/*  44:    */     throws Exception
/*  45:    */   {
/*  46: 62 */     ResultSet rs = null;
/*  47: 63 */     Connection con = null;
/*  48: 64 */     PreparedStatement prepstat = null;
/*  49:    */     try
/*  50:    */     {
/*  51: 67 */       con = DConnect.getConnection();
/*  52: 68 */       String query = "UPDATE category_list SET category_desc=?,content_type_id=? WHERE category_id=" + id;
/*  53:    */       
/*  54:    */ 
/*  55: 71 */       prepstat = con.prepareStatement(query);
/*  56: 72 */       prepstat.setString(1, contentDesc);
/*  57: 73 */       prepstat.setInt(2, contentType);
/*  58: 74 */       prepstat.execute();
/*  59:    */     }
/*  60:    */     catch (Exception ex)
/*  61:    */     {
/*  62: 76 */       if (con != null) {
/*  63: 77 */         con.close();
/*  64:    */       }
/*  65: 79 */       throw new Exception(ex.getMessage());
/*  66:    */     }
/*  67: 81 */     if (con != null) {
/*  68: 82 */       con.close();
/*  69:    */     }
/*  70:    */   }
/*  71:    */   
/*  72:    */   public static void deleteCategory(int id)
/*  73:    */     throws Exception
/*  74:    */   {
/*  75: 91 */     ResultSet rs = null;
/*  76: 92 */     Connection con = null;
/*  77: 93 */     PreparedStatement prepstat = null;
/*  78:    */     try
/*  79:    */     {
/*  80: 96 */       con = DConnect.getConnection();
/*  81: 97 */       String query = "DELETE from category_list WHERE category_id=?";
/*  82: 98 */       prepstat = con.prepareStatement(query);
/*  83: 99 */       prepstat.setInt(1, id);
/*  84:100 */       prepstat.execute();
/*  85:    */     }
/*  86:    */     catch (Exception ex)
/*  87:    */     {
/*  88:102 */       if (con != null) {
/*  89:103 */         con.close();
/*  90:    */       }
/*  91:105 */       throw new Exception(ex.getMessage());
/*  92:    */     }
/*  93:107 */     if (con != null) {
/*  94:108 */       con.close();
/*  95:    */     }
/*  96:    */   }
/*  97:    */   
/*  98:    */   public static Category viewCategory(int categoryId)
/*  99:    */     throws Exception
/* 100:    */   {
/* 101:117 */     Category newBean = new Category();
/* 102:    */     
/* 103:119 */     ResultSet rs = null;
/* 104:120 */     Connection con = null;
/* 105:121 */     PreparedStatement prepstat = null;
/* 106:    */     try
/* 107:    */     {
/* 108:124 */       con = DConnect.getConnection();
/* 109:125 */       String query = "SELECT * from category_list WHERE category_id=?";
/* 110:126 */       prepstat = con.prepareStatement(query);
/* 111:127 */       prepstat.setInt(1, categoryId);
/* 112:128 */       rs = prepstat.executeQuery();
/* 113:130 */       while (rs.next())
/* 114:    */       {
/* 115:131 */         newBean.setId(rs.getInt("category_id"));
/* 116:132 */         newBean.setCategoryDesc(rs.getString("category_desc"));
/* 117:133 */         newBean.setContentType(rs.getInt("content_type_id"));
/* 118:    */       }
/* 119:    */     }
/* 120:    */     catch (Exception ex)
/* 121:    */     {
/* 122:136 */       if (con != null) {
/* 123:137 */         con.close();
/* 124:    */       }
/* 125:139 */       throw new Exception(ex.getMessage());
/* 126:    */     }
/* 127:141 */     if (con != null) {
/* 128:142 */       con.close();
/* 129:    */     }
/* 130:144 */     return newBean;
/* 131:    */   }
/* 132:    */   
/* 133:    */   public static List getPopulatedCategories(String contentType, String siteId)
/* 134:    */     throws Exception
/* 135:    */   {
/* 136:151 */     String queryEnding = "";
/* 137:153 */     if ((contentType != null) || (!contentType.equals("")) || (!contentType.equals("0"))) {
/* 138:154 */       queryEnding = "where content_type_id=" + contentType;
/* 139:    */     }
/* 140:157 */     List types = new ArrayList();
/* 141:    */     
/* 142:    */ 
/* 143:160 */     ResultSet rs = null;
/* 144:161 */     Connection con = null;
/* 145:162 */     PreparedStatement prepstat = null;
/* 146:    */     try
/* 147:    */     {
/* 148:165 */       con = DConnect.getConnection();
/* 149:166 */       String query = "select cp_id from cp_sites where site_id='" + siteId + "'";
/* 150:167 */       prepstat = con.prepareStatement(query);
/* 151:168 */       rs = prepstat.executeQuery();
/* 152:169 */       String acctId = new String();
/* 153:170 */       while (rs.next()) {
/* 154:171 */         acctId = rs.getString("cp_id");
/* 155:    */       }
/* 156:174 */       query = "select list_id from cs_cp_relationship where cs_id='" + acctId + "'";
/* 157:175 */       String listIDquery = new String(" (content_list.list_id='" + acctId + "' or ");
/* 158:176 */       prepstat = con.prepareStatement(query);
/* 159:177 */       rs = prepstat.executeQuery();
/* 160:178 */       while (rs.next()) {
/* 161:179 */         listIDquery = listIDquery + " content_list.list_id='" + rs.getString("list_id") + "' or ";
/* 162:    */       }
/* 163:181 */       listIDquery = listIDquery.substring(0, listIDquery.lastIndexOf("' or ") + 1);
/* 164:182 */       listIDquery = listIDquery + ") and ";
/* 165:    */       
/* 166:184 */       query = "SELECT distinct content_list.category, category_list.category_id, category_list.category_desc, category_list.content_type_id FROM content_list, category_list where " + listIDquery + "category_list.content_type_id=" + contentType + " and " + "category_list.category_id=content_list.category order by category_list.category_id";
/* 167:    */       
/* 168:    */ 
/* 169:187 */       prepstat = con.prepareStatement(query);
/* 170:188 */       rs = prepstat.executeQuery();
/* 171:190 */       while (rs.next()) {
/* 172:191 */         types.add(new Category(rs.getInt("category_id"), rs.getString("category_desc"), rs.getInt("content_type_id")));
/* 173:    */       }
/* 174:    */     }
/* 175:    */     catch (Exception ex)
/* 176:    */     {
/* 177:196 */       if (con != null) {
/* 178:197 */         con.close();
/* 179:    */       }
/* 180:199 */       throw new Exception(ex.getMessage());
/* 181:    */     }
/* 182:201 */     if (con != null) {
/* 183:202 */       con.close();
/* 184:    */     }
/* 185:205 */     return types;
/* 186:    */   }
/* 187:    */   
/* 188:    */   public static HashMap getPopulatedCategories(String siteId)
/* 189:    */     throws Exception
/* 190:    */   {
/* 191:209 */     HashMap grpCats = new HashMap();
/* 192:    */     
/* 193:    */ 
/* 194:212 */     ResultSet rs = null;
/* 195:213 */     Connection con = null;
/* 196:214 */     PreparedStatement prepstat = null;
/* 197:    */     try
/* 198:    */     {
/* 199:217 */       con = DConnect.getConnection();
/* 200:218 */       String query = "select cp_id from cp_sites where site_id='" + siteId + "'";
/* 201:219 */       prepstat = con.prepareStatement(query);
/* 202:220 */       rs = prepstat.executeQuery();
/* 203:221 */       String acctId = new String();
/* 204:222 */       while (rs.next()) {
/* 205:223 */         acctId = rs.getString("cp_id");
/* 206:    */       }
/* 207:226 */       query = "select list_id from cs_cp_relationship where cs_id='" + acctId + "'";
/* 208:227 */       String listIDquery = new String(" (content_list.list_id='" + acctId + "' or ");
/* 209:228 */       prepstat = con.prepareStatement(query);
/* 210:229 */       rs = prepstat.executeQuery();
/* 211:230 */       while (rs.next()) {
/* 212:231 */         listIDquery = listIDquery + " content_list.list_id='" + rs.getString("list_id") + "' or ";
/* 213:    */       }
/* 214:233 */       listIDquery = listIDquery.substring(0, listIDquery.lastIndexOf("' or ") + 1);
/* 215:234 */       listIDquery = listIDquery + ") and ";
/* 216:    */       
/* 217:236 */       query = "SELECT distinct content_list.category, category_list.category_id, category_list.category_desc, category_list.content_type_id FROM content_list, category_list where " + listIDquery + "category_list.category_id=content_list.category order by category_list.content_type_id, " + "category_list.category_id";
/* 218:    */       
/* 219:    */ 
/* 220:239 */       prepstat = con.prepareStatement(query);
/* 221:240 */       rs = prepstat.executeQuery();
/* 222:    */       
/* 223:242 */       int conTypeMem = 1;
/* 224:243 */       ArrayList cats = new ArrayList();
/* 225:244 */       while (rs.next())
/* 226:    */       {
/* 227:245 */         Category cat = new Category(rs.getInt("category_id"), rs.getString("category_desc"), rs.getInt("content_type_id"));
/* 228:246 */         if (cat.getContentType() == conTypeMem)
/* 229:    */         {
/* 230:247 */           cats.add(cat);
/* 231:    */         }
/* 232:    */         else
/* 233:    */         {
/* 234:249 */           grpCats.put(new String("" + conTypeMem), cats);
/* 235:250 */           conTypeMem = cat.getContentType();
/* 236:251 */           cats = new ArrayList();
/* 237:252 */           cats.add(cat);
/* 238:    */         }
/* 239:    */       }
/* 240:255 */       grpCats.put(new String("" + conTypeMem), cats);
/* 241:    */     }
/* 242:    */     catch (Exception ex)
/* 243:    */     {
/* 244:257 */       if (con != null) {
/* 245:258 */         con.close();
/* 246:    */       }
/* 247:260 */       throw new Exception(ex.getMessage());
/* 248:    */     }
/* 249:262 */     if (con != null) {
/* 250:263 */       con.close();
/* 251:    */     }
/* 252:266 */     return grpCats;
/* 253:    */   }
/* 254:    */   
/* 255:    */   public static List getDistinctCategories(String contentType)
/* 256:    */     throws Exception
/* 257:    */   {
/* 258:271 */     String queryEnding = "";
/* 259:273 */     if ((contentType != null) || (!contentType.equals("")) || (!contentType.equals("0"))) {
/* 260:274 */       queryEnding = "where content_type_id=" + contentType;
/* 261:    */     }
/* 262:277 */     List types = new ArrayList();
/* 263:    */     
/* 264:    */ 
/* 265:280 */     ResultSet rs = null;
/* 266:281 */     Connection con = null;
/* 267:282 */     PreparedStatement prepstat = null;
/* 268:    */     try
/* 269:    */     {
/* 270:285 */       con = DConnect.getConnection();
/* 271:286 */       String query = "SELECT * from category_list " + queryEnding;
/* 272:287 */       prepstat = con.prepareStatement(query);
/* 273:288 */       rs = prepstat.executeQuery();
/* 274:290 */       while (rs.next()) {
/* 275:291 */         types.add(new Category(rs.getInt("category_id"), rs.getString("category_desc"), rs.getInt("content_type_id")));
/* 276:    */       }
/* 277:    */     }
/* 278:    */     catch (Exception ex)
/* 279:    */     {
/* 280:296 */       if (con != null) {
/* 281:297 */         con.close();
/* 282:    */       }
/* 283:299 */       throw new Exception(ex.getMessage());
/* 284:    */     }
/* 285:301 */     if (con != null) {
/* 286:302 */       con.close();
/* 287:    */     }
/* 288:305 */     return types;
/* 289:    */   }
/* 290:    */   
/* 291:    */   public static void main(String[] args)
/* 292:    */     throws Exception
/* 293:    */   {}
/* 294:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.CategoryDB
 * JD-Core Version:    0.7.0.1
 */