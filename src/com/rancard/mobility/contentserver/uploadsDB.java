/*   1:    */ package com.rancard.mobility.contentserver;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import com.rancard.util.DataImportListener;
/*   5:    */ import java.io.File;
/*   6:    */ import java.io.InputStream;
/*   7:    */ import java.io.PrintStream;
/*   8:    */ import java.sql.Blob;
/*   9:    */ import java.sql.Connection;
/*  10:    */ import java.sql.PreparedStatement;
/*  11:    */ import java.sql.ResultSet;
/*  12:    */ import java.sql.SQLException;
/*  13:    */ import java.util.Iterator;
/*  14:    */ import java.util.List;
/*  15:    */ import org.apache.commons.fileupload.FileItem;
/*  16:    */ 
/*  17:    */ public class uploadsDB
/*  18:    */ {
/*  19:    */   public void createuploads(String id, String filename, File binaryfile, String list_id)
/*  20:    */     throws Exception
/*  21:    */   {
/*  22: 22 */     ResultSet rs = null;
/*  23: 23 */     Connection con = null;
/*  24: 24 */     PreparedStatement prepstat = null;
/*  25:    */     try
/*  26:    */     {
/*  27: 26 */       con = DConnect.getConnection();
/*  28:    */       
/*  29: 28 */       String SQL = "insert into uploads(id,filename,binaryfile,list_id) values(?,?,?,?)";
/*  30:    */       
/*  31: 30 */       prepstat = con.prepareStatement(SQL);
/*  32:    */       
/*  33: 32 */       prepstat.setString(1, id);
/*  34:    */       
/*  35: 34 */       prepstat.setString(2, filename);
/*  36:    */       
/*  37: 36 */       prepstat.setString(4, list_id);
/*  38: 37 */       prepstat.execute();
/*  39:    */     }
/*  40:    */     catch (Exception ex)
/*  41:    */     {
/*  42: 39 */       if (con != null) {
/*  43: 40 */         con.close();
/*  44:    */       }
/*  45: 42 */       throw new Exception(ex.getMessage());
/*  46:    */     }
/*  47: 44 */     if (con != null) {
/*  48: 45 */       con.close();
/*  49:    */     }
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void updateuploads(String id, String filename, File binaryfile, String list_id)
/*  53:    */     throws Exception
/*  54:    */   {
/*  55: 54 */     ResultSet rs = null;
/*  56: 55 */     Connection con = null;
/*  57: 56 */     PreparedStatement prepstat = null;
/*  58:    */     try
/*  59:    */     {
/*  60: 58 */       con = DConnect.getConnection();
/*  61:    */       
/*  62: 60 */       String SQL = "update uploads set filename=?,binaryfile=?,list_id=? and id=?";
/*  63:    */       
/*  64: 62 */       prepstat = con.prepareStatement(SQL);
/*  65:    */       
/*  66: 64 */       prepstat.setString(1, filename);
/*  67:    */       
/*  68: 66 */       prepstat.setString(3, list_id);
/*  69:    */       
/*  70: 68 */       prepstat.setString(4, id);
/*  71: 69 */       prepstat.execute();
/*  72:    */     }
/*  73:    */     catch (Exception ex)
/*  74:    */     {
/*  75: 71 */       if (con != null) {
/*  76: 72 */         con.close();
/*  77:    */       }
/*  78: 74 */       throw new Exception(ex.getMessage());
/*  79:    */     }
/*  80: 76 */     if (con != null) {
/*  81: 77 */       con.close();
/*  82:    */     }
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void updateuploadsWithPreview(String id, String filename, File previewfile, String list_id)
/*  86:    */     throws Exception
/*  87:    */   {
/*  88: 88 */     ResultSet rs = null;
/*  89: 89 */     Connection con = null;
/*  90: 90 */     PreparedStatement prepstat = null;
/*  91:    */     try
/*  92:    */     {
/*  93: 92 */       con = DConnect.getConnection();
/*  94:    */       
/*  95: 94 */       String SQL = "update uploads set filename=?,previewfile=?,list_id=? and id=?";
/*  96:    */       
/*  97: 96 */       prepstat = con.prepareStatement(SQL);
/*  98:    */       
/*  99: 98 */       prepstat.setString(1, filename);
/* 100:    */       
/* 101:100 */       prepstat.setString(3, list_id);
/* 102:    */       
/* 103:102 */       prepstat.setString(4, id);
/* 104:103 */       prepstat.execute();
/* 105:    */     }
/* 106:    */     catch (Exception ex)
/* 107:    */     {
/* 108:105 */       if (con != null) {
/* 109:106 */         con.close();
/* 110:    */       }
/* 111:108 */       throw new Exception(ex.getMessage());
/* 112:    */     }
/* 113:110 */     if (con != null) {
/* 114:111 */       con.close();
/* 115:    */     }
/* 116:    */   }
/* 117:    */   
/* 118:    */   public void deleteuploads(String list_id, String id)
/* 119:    */     throws Exception
/* 120:    */   {
/* 121:119 */     ResultSet rs = null;
/* 122:120 */     Connection con = null;
/* 123:121 */     PreparedStatement prepstat = null;
/* 124:    */     try
/* 125:    */     {
/* 126:123 */       con = DConnect.getConnection();
/* 127:124 */       String SQL = "delete from uploads where list_id=? and id=?";
/* 128:125 */       prepstat = con.prepareStatement(SQL);
/* 129:    */       
/* 130:127 */       prepstat.setString(1, list_id);
/* 131:    */       
/* 132:129 */       prepstat.setString(2, id);
/* 133:130 */       prepstat.execute();
/* 134:    */     }
/* 135:    */     catch (Exception ex)
/* 136:    */     {
/* 137:132 */       if (con != null) {
/* 138:133 */         con.close();
/* 139:    */       }
/* 140:135 */       throw new Exception(ex.getMessage());
/* 141:    */     }
/* 142:137 */     if (con != null) {
/* 143:138 */       con.close();
/* 144:    */     }
/* 145:    */   }
/* 146:    */   
/* 147:    */   public uploadsBean viewuploads(String list_id, String id)
/* 148:    */     throws Exception
/* 149:    */   {
/* 150:144 */     uploadsBean uploads = new uploadsBean();
/* 151:    */     
/* 152:    */ 
/* 153:147 */     ResultSet rs = null;
/* 154:148 */     Connection con = null;
/* 155:149 */     PreparedStatement prepstat = null;
/* 156:    */     try
/* 157:    */     {
/* 158:151 */       con = DConnect.getConnection();
/* 159:    */       
/* 160:153 */       String SQL = "select * from uploads,content_list,format_list where uploads.list_id=? and uploads.id=? and content_list.id=uploads.id and  content_list.list_id=uploads.list_id and format_list.format_id=content_list.formats";
/* 161:    */       
/* 162:    */ 
/* 163:    */ 
/* 164:157 */       prepstat = con.prepareStatement(SQL);
/* 165:    */       
/* 166:159 */       prepstat.setString(1, list_id);
/* 167:160 */       prepstat.setString(2, id);
/* 168:161 */       rs = prepstat.executeQuery();
/* 169:162 */       if (!rs.next()) {
/* 170:163 */         throw new Exception("File not found");
/* 171:    */       }
/* 172:165 */       uploads.setid(id);
/* 173:166 */       uploads.setlist_id(list_id);
/* 174:167 */       uploads.setfilename(rs.getString("uploads.filename"));
/* 175:168 */       Blob b = rs.getBlob("uploads.binaryfile");
/* 176:169 */       uploads.setDataStream(b.getBytes(1L, new Long(b.length()).intValue()));
/* 177:170 */       Blob p = rs.getBlob("uploads.previewfile");
/* 178:171 */       if (p != null) {
/* 179:172 */         uploads.setPreviewStream(p.getBytes(1L, new Long(p.length()).intValue()));
/* 180:    */       }
/* 181:175 */       uploads.setFormat(rs.getString("format_list.file_ext"));
/* 182:176 */       uploads.setMimeType(rs.getString("format_list.mime_type"));
/* 183:    */     }
/* 184:    */     catch (Exception ex)
/* 185:    */     {
/* 186:179 */       if (con != null) {
/* 187:180 */         con.close();
/* 188:    */       }
/* 189:182 */       throw new Exception(ex.getMessage());
/* 190:    */     }
/* 191:    */     finally
/* 192:    */     {
/* 193:    */       try
/* 194:    */       {
/* 195:185 */         if (rs != null) {
/* 196:186 */           rs.close();
/* 197:    */         }
/* 198:188 */         if (prepstat != null) {
/* 199:189 */           prepstat.close();
/* 200:    */         }
/* 201:191 */         if (con != null) {
/* 202:192 */           con.close();
/* 203:    */         }
/* 204:    */       }
/* 205:    */       catch (SQLException sqlee) {}
/* 206:    */     }
/* 207:197 */     return uploads;
/* 208:    */   }
/* 209:    */   
/* 210:    */   public static long totalDiskSpaceUsed(String list_id)
/* 211:    */     throws Exception
/* 212:    */   {
/* 213:202 */     long space = 0L;
/* 214:    */     
/* 215:204 */     String query = null;
/* 216:205 */     ResultSet rs = null;
/* 217:206 */     Connection con = null;
/* 218:207 */     PreparedStatement prepstat = null;
/* 219:    */     try
/* 220:    */     {
/* 221:209 */       con = DConnect.getConnection();
/* 222:    */       
/* 223:211 */       query = "select previewfile, binaryfile from uploads where list_id=?";
/* 224:    */       
/* 225:213 */       prepstat = con.prepareStatement(query);
/* 226:214 */       prepstat.setString(1, list_id);
/* 227:215 */       rs = prepstat.executeQuery();
/* 228:217 */       while (rs.next())
/* 229:    */       {
/* 230:218 */         Blob b = rs.getBlob("binaryfile");
/* 231:219 */         if (b != null) {
/* 232:220 */           space += b.length();
/* 233:    */         }
/* 234:222 */         Blob p = rs.getBlob("previewfile");
/* 235:223 */         if (p != null) {
/* 236:224 */           space += p.length();
/* 237:    */         }
/* 238:    */       }
/* 239:    */     }
/* 240:    */     catch (Exception ex)
/* 241:    */     {
/* 242:228 */       if (con != null) {
/* 243:229 */         con.close();
/* 244:    */       }
/* 245:231 */       throw new Exception(ex.getMessage());
/* 246:    */     }
/* 247:    */     finally
/* 248:    */     {
/* 249:    */       try
/* 250:    */       {
/* 251:234 */         if (rs != null) {
/* 252:235 */           rs.close();
/* 253:    */         }
/* 254:237 */         if (prepstat != null) {
/* 255:238 */           prepstat.close();
/* 256:    */         }
/* 257:240 */         if (con != null) {
/* 258:241 */           con.close();
/* 259:    */         }
/* 260:    */       }
/* 261:    */       catch (SQLException sqlee) {}
/* 262:    */     }
/* 263:246 */     return space;
/* 264:    */   }
/* 265:    */   
/* 266:    */   public static void uploadPreview(DataImportListener listener, List fileItems, String listId)
/* 267:    */     throws Exception
/* 268:    */   {
/* 269:251 */     String query = null;
/* 270:252 */     ResultSet rs = null;
/* 271:253 */     Connection con = null;
/* 272:254 */     PreparedStatement prepstat = null;
/* 273:    */     
/* 274:256 */     Iterator itr = fileItems.iterator();
/* 275:257 */     int i = 0;
/* 276:    */     try
/* 277:    */     {
/* 278:260 */       con = DConnect.getConnection();
/* 279:262 */       while (itr.hasNext())
/* 280:    */       {
/* 281:263 */         FileItem fi = (FileItem)itr.next();
/* 282:266 */         if (!fi.isFormField())
/* 283:    */         {
/* 284:268 */           System.out.println("\nNAME: " + fi.getName());
/* 285:269 */           System.out.println("SIZE: " + fi.getSize());
/* 286:270 */           String filename = fi.getName();
/* 287:271 */           String fileId = fi.getFieldName();
/* 288:272 */           if (filename != null)
/* 289:    */           {
/* 290:273 */             PreparedStatement pstmt = con.prepareStatement("update uploads set previewfile =? where id= ? and list_id =?");
/* 291:    */             
/* 292:275 */             InputStream is = fi.getInputStream();
/* 293:    */             
/* 294:277 */             pstmt.setBinaryStream(1, is, is.available());
/* 295:278 */             pstmt.setString(2, fileId);
/* 296:279 */             pstmt.setString(3, listId);
/* 297:280 */             pstmt.executeUpdate();
/* 298:281 */             listener.processRead(1);
/* 299:    */             
/* 300:    */ 
/* 301:284 */             pstmt = con.prepareStatement("update content_list set preview_exists=1 where id =? and list_id =? ");
/* 302:    */             
/* 303:286 */             pstmt.setString(1, fileId);
/* 304:287 */             pstmt.setString(2, listId);
/* 305:288 */             pstmt.executeUpdate();
/* 306:    */           }
/* 307:    */         }
/* 308:    */         else
/* 309:    */         {
/* 310:292 */           System.out.println("Field =" + fi.getFieldName());
/* 311:    */         }
/* 312:294 */         listener.done();
/* 313:    */       }
/* 314:    */     }
/* 315:    */     catch (Exception ex)
/* 316:    */     {
/* 317:297 */       if (con != null) {
/* 318:298 */         con.close();
/* 319:    */       }
/* 320:300 */       throw new Exception(ex.getMessage());
/* 321:    */     }
/* 322:    */     finally
/* 323:    */     {
/* 324:    */       try
/* 325:    */       {
/* 326:303 */         if (rs != null) {
/* 327:304 */           rs.close();
/* 328:    */         }
/* 329:306 */         if (prepstat != null) {
/* 330:307 */           prepstat.close();
/* 331:    */         }
/* 332:309 */         if (con != null) {
/* 333:310 */           con.close();
/* 334:    */         }
/* 335:    */       }
/* 336:    */       catch (SQLException sqlee) {}
/* 337:    */     }
/* 338:    */   }
/* 339:    */   
/* 340:    */   public static void main(String[] args)
/* 341:    */   {
/* 342:    */     try
/* 343:    */     {
/* 344:318 */       System.out.println(totalDiskSpaceUsed("kf007tg"));
/* 345:    */     }
/* 346:    */     catch (Exception e)
/* 347:    */     {
/* 348:320 */       System.out.println(e.getMessage());
/* 349:    */     }
/* 350:    */   }
/* 351:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.uploadsDB
 * JD-Core Version:    0.7.0.1
 */