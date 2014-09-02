/*   1:    */ package com.rancard.util;
/*   2:    */ 
/*   3:    */ import java.io.PrintStream;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.HashMap;
/*   6:    */ 
/*   7:    */ public abstract class ExtManager
/*   8:    */ {
/*   9:  7 */   private static HashMap formatLookUp = new HashMap();
/*  10:  8 */   private static HashMap typeLookUp = new HashMap();
/*  11:  9 */   private static HashMap typeMatch = new HashMap();
/*  12:    */   
/*  13:    */   public static int getTypeForFormat(String fileExt)
/*  14:    */     throws Exception
/*  15:    */   {
/*  16:    */     
/*  17: 13 */     if (fileExt != null)
/*  18:    */     {
/*  19: 14 */       fileExt = fileExt.toLowerCase();
/*  20:    */       try
/*  21:    */       {
/*  22: 16 */         if (typeLookUp.containsKey(fileExt)) {
/*  23: 17 */           return new Integer(typeLookUp.get(fileExt).toString()).intValue();
/*  24:    */         }
/*  25: 20 */         return 0;
/*  26:    */       }
/*  27:    */       catch (Exception e)
/*  28:    */       {
/*  29: 23 */         throw new Exception("Key not found.");
/*  30:    */       }
/*  31:    */     }
/*  32: 26 */     throw new Exception("extension not specified.");
/*  33:    */   }
/*  34:    */   
/*  35:    */   public static boolean isSupportedFormat(String fileExt)
/*  36:    */     throws Exception
/*  37:    */   {
/*  38:    */     
/*  39: 32 */     if (fileExt != null)
/*  40:    */     {
/*  41: 33 */       fileExt = fileExt.toLowerCase();
/*  42:    */       try
/*  43:    */       {
/*  44: 35 */         if (typeLookUp.containsKey(fileExt)) {
/*  45: 36 */           return true;
/*  46:    */         }
/*  47: 38 */         return false;
/*  48:    */       }
/*  49:    */       catch (Exception e)
/*  50:    */       {
/*  51: 41 */         throw new Exception("Key not found.");
/*  52:    */       }
/*  53:    */     }
/*  54: 44 */     throw new Exception("extension not specified.");
/*  55:    */   }
/*  56:    */   
/*  57:    */   public static String getFormatFor(String fileExt)
/*  58:    */     throws Exception
/*  59:    */   {
/*  60:    */     
/*  61: 50 */     if (fileExt != null)
/*  62:    */     {
/*  63: 51 */       fileExt = fileExt.toLowerCase();
/*  64:    */       try
/*  65:    */       {
/*  66: 53 */         return formatLookUp.get(fileExt).toString();
/*  67:    */       }
/*  68:    */       catch (Exception e)
/*  69:    */       {
/*  70: 55 */         throw new Exception("Key not found.");
/*  71:    */       }
/*  72:    */     }
/*  73: 58 */     throw new Exception("extension not specified.");
/*  74:    */   }
/*  75:    */   
/*  76:    */   private static void initTypeLookUp()
/*  77:    */   {
/*  78: 63 */     typeLookUp.put("jpg", "5");
/*  79: 64 */     typeLookUp.put("gif", "5");
/*  80: 65 */     typeLookUp.put("png", "5");
/*  81: 66 */     typeLookUp.put("wbmp", "4");
/*  82: 67 */     typeLookUp.put("tiff", "5");
/*  83: 68 */     typeLookUp.put("jpeg", "5");
/*  84: 69 */     typeLookUp.put("bmp", "4");
/*  85: 70 */     typeLookUp.put("noktxt", "1");
/*  86: 71 */     typeLookUp.put("emy", "1");
/*  87: 72 */     typeLookUp.put("imy", "1");
/*  88: 73 */     typeLookUp.put("ems", "1");
/*  89: 74 */     typeLookUp.put("amr", "3");
/*  90: 75 */     typeLookUp.put("midi", "2");
/*  91: 76 */     typeLookUp.put("mid", "2");
/*  92: 77 */     typeLookUp.put("m4a", "3");
/*  93: 78 */     typeLookUp.put("aac", "3");
/*  94: 79 */     typeLookUp.put("mp3", "3");
/*  95: 80 */     typeLookUp.put("qcp", "3");
/*  96: 81 */     typeLookUp.put("awb", "3");
/*  97: 82 */     typeLookUp.put("wav", "3");
/*  98: 83 */     typeLookUp.put("wma", "3");
/*  99: 84 */     typeLookUp.put("pmd", "3");
/* 100: 85 */     typeLookUp.put("jar", "7");
/* 101: 86 */     typeLookUp.put("jad", "7");
/* 102: 87 */     typeLookUp.put("m4v", "6");
/* 103: 88 */     typeLookUp.put("3gpp", "6");
/* 104: 89 */     typeLookUp.put("3gp", "6");
/* 105: 90 */     typeLookUp.put("avi", "6");
/* 106: 91 */     typeLookUp.put("mpg", "6");
/* 107: 92 */     typeLookUp.put("mpeg", "6");
/* 108: 93 */     typeLookUp.put("txt", "8");
/* 109: 94 */     typeLookUp.put("cab", "7");
/* 110: 95 */     typeLookUp.put("gcd", "8");
/* 111: 96 */     typeLookUp.put("ott", "8");
/* 112: 97 */     typeLookUp.put("sis", "7");
/* 113: 98 */     typeLookUp.put("mp4", "6");
/* 114: 99 */     typeLookUp.put("mmf", "2");
/* 115:100 */     typeLookUp.put("m4v", "6");
/* 116:101 */     typeLookUp.put("0.mid", "2");
/* 117:102 */     typeLookUp.put("1.mid", "2");
/* 118:103 */     typeLookUp.put("ma1.mmf", "2");
/* 119:104 */     typeLookUp.put("ma2.mmf", "2");
/* 120:105 */     typeLookUp.put("ma3.mmf", "2");
/* 121:106 */     typeLookUp.put("10.imy", "1");
/* 122:107 */     typeLookUp.put("12.imy", "1");
/* 123:108 */     typeLookUp.put("rtttl", "1");
/* 124:109 */     typeLookUp.put("rtx", "1");
/* 125:110 */     typeLookUp.put("nokia", "1");
/* 126:111 */     typeLookUp.put("bin", "1");
/* 127:112 */     typeLookUp.put("ott", "1");
/* 128:113 */     typeLookUp.put("7bit.nokia", "1");
/* 129:114 */     typeLookUp.put("8bit.nokia", "1");
/* 130:115 */     typeLookUp.put("10.sagem", "1");
/* 131:116 */     typeLookUp.put("emy.extended", "1");
/* 132:117 */     typeLookUp.put("motbin", "1");
/* 133:118 */     typeLookUp.put("mot", "1");
/* 134:119 */     typeLookUp.put("sagem", "1");
/* 135:120 */     typeLookUp.put("au", "3");
/* 136:121 */     typeLookUp.put("aiff", "3");
/* 137:122 */     typeLookUp.put("swf", "3");
/* 138:123 */     typeLookUp.put("mfi", "3");
/* 139:124 */     typeLookUp.put("apk", "7");
/* 140:125 */     typeLookUp.put("flv", "6");
/* 141:    */   }
/* 142:    */   
/* 143:    */   private static void initFormatLookUp()
/* 144:    */   {
/* 145:129 */     formatLookUp.put("jpg", "1");
/* 146:130 */     formatLookUp.put("gif", "2");
/* 147:131 */     formatLookUp.put("png", "3");
/* 148:132 */     formatLookUp.put("wbmp", "4");
/* 149:133 */     formatLookUp.put("tiff", "35");
/* 150:134 */     formatLookUp.put("jpeg", "22");
/* 151:135 */     formatLookUp.put("bmp", "26");
/* 152:136 */     formatLookUp.put("noktxt", "27");
/* 153:137 */     formatLookUp.put("emy", "38");
/* 154:138 */     formatLookUp.put("imy", "36");
/* 155:139 */     formatLookUp.put("ems", "37");
/* 156:140 */     formatLookUp.put("amr", "13");
/* 157:141 */     formatLookUp.put("midi", "21");
/* 158:142 */     formatLookUp.put("mid", "7");
/* 159:143 */     formatLookUp.put("m4a", "10");
/* 160:144 */     formatLookUp.put("aac", "14");
/* 161:145 */     formatLookUp.put("mp3", "9");
/* 162:146 */     formatLookUp.put("qcp", "8");
/* 163:    */     
/* 164:148 */     formatLookUp.put("wav", "17");
/* 165:149 */     formatLookUp.put("wma", "18");
/* 166:150 */     formatLookUp.put("pmd", "5");
/* 167:151 */     formatLookUp.put("jar", "15");
/* 168:152 */     formatLookUp.put("jad", "16");
/* 169:153 */     formatLookUp.put("m4v", "19");
/* 170:154 */     formatLookUp.put("3gpp", "20");
/* 171:155 */     formatLookUp.put("3gp", "50");
/* 172:156 */     formatLookUp.put("avi", "23");
/* 173:157 */     formatLookUp.put("mpg", "24");
/* 174:158 */     formatLookUp.put("mpeg", "25");
/* 175:159 */     formatLookUp.put("txt", "30");
/* 176:160 */     formatLookUp.put("cab", "31");
/* 177:161 */     formatLookUp.put("gcd", "32");
/* 178:162 */     formatLookUp.put("ott", "33");
/* 179:163 */     formatLookUp.put("sis", "34");
/* 180:164 */     formatLookUp.put("mp4", "6");
/* 181:165 */     formatLookUp.put("mmf", "11");
/* 182:166 */     formatLookUp.put("0.mid", "7");
/* 183:167 */     formatLookUp.put("1.mid", "7");
/* 184:168 */     formatLookUp.put("ma1.mmf", "11");
/* 185:169 */     formatLookUp.put("ma2.mmf", "11");
/* 186:170 */     formatLookUp.put("ma3.mmf", "11");
/* 187:171 */     formatLookUp.put("10.imy", "45");
/* 188:172 */     formatLookUp.put("12.imy", "46");
/* 189:173 */     formatLookUp.put("rtttl", "39");
/* 190:174 */     formatLookUp.put("rtx", "40");
/* 191:175 */     formatLookUp.put("nokia", "41");
/* 192:    */     
/* 193:177 */     formatLookUp.put("7bit.nokia", "42");
/* 194:178 */     formatLookUp.put("8bit.nokia", "43");
/* 195:179 */     formatLookUp.put("10.sagem", "44");
/* 196:180 */     formatLookUp.put("emy.extended", "47");
/* 197:181 */     formatLookUp.put("motbin", "48");
/* 198:182 */     formatLookUp.put("apk", "52");
/* 199:183 */     formatLookUp.put("flv", "51");
/* 200:    */   }
/* 201:    */   
/* 202:    */   private static void initTypeMatch()
/* 203:    */   {
/* 204:191 */     ArrayList wallpaperFmts = new ArrayList();
/* 205:192 */     ArrayList logoFmts = new ArrayList();
/* 206:193 */     ArrayList videoFmts = new ArrayList();
/* 207:194 */     ArrayList monophoneFmts = new ArrayList();
/* 208:195 */     ArrayList polyphoneFmts = new ArrayList();
/* 209:196 */     ArrayList realtoneFmts = new ArrayList();
/* 210:197 */     ArrayList gameFmts = new ArrayList();
/* 211:198 */     ArrayList themeFmts = new ArrayList();
/* 212:199 */     ArrayList aniWallpaperFmts = new ArrayList();
/* 213:    */     
/* 214:201 */     int[] wallarr = { 1, 2, 3, 4, 35, 22 };
/* 215:202 */     for (int i = 0; i < wallarr.length; i++) {
/* 216:203 */       wallpaperFmts.add(Integer.valueOf(wallarr[i]));
/* 217:    */     }
/* 218:206 */     int[] logoarr = { 4, 26 };
/* 219:207 */     for (int i = 0; i < logoarr.length; i++) {
/* 220:208 */       logoFmts.add(Integer.valueOf(logoarr[i]));
/* 221:    */     }
/* 222:211 */     int[] vidarr = { 19, 20, 50, 23, 24, 25, 6, 51 };
/* 223:212 */     for (int i = 0; i < vidarr.length; i++) {
/* 224:213 */       videoFmts.add(Integer.valueOf(vidarr[i]));
/* 225:    */     }
/* 226:216 */     int[] monoarr = { 27, 38, 36, 37, 45, 46, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48 };
/* 227:217 */     for (int i = 0; i < monoarr.length; i++) {
/* 228:218 */       monophoneFmts.add(Integer.valueOf(monoarr[i]));
/* 229:    */     }
/* 230:221 */     int[] polyarr = { 21, 7, 11 };
/* 231:222 */     for (int i = 0; i < polyarr.length; i++) {
/* 232:223 */       polyphoneFmts.add(Integer.valueOf(polyarr[i]));
/* 233:    */     }
/* 234:226 */     int[] realarr = { 13, 10, 14, 9, 8, 17, 18, 6 };
/* 235:227 */     for (int i = 0; i < realarr.length; i++) {
/* 236:228 */       realtoneFmts.add(Integer.valueOf(realarr[i]));
/* 237:    */     }
/* 238:231 */     int[] thmarr = { 15, 16, 31, 34, 52 };
/* 239:232 */     for (int i = 0; i < thmarr.length; i++)
/* 240:    */     {
/* 241:233 */       themeFmts.add(Integer.valueOf(thmarr[i]));
/* 242:234 */       gameFmts.add(Integer.valueOf(thmarr[i]));
/* 243:    */     }
/* 244:237 */     int[] aniarr = { 2 };
/* 245:238 */     for (int i = 0; i < aniarr.length; i++) {
/* 246:239 */       aniWallpaperFmts.add(Integer.valueOf(aniarr[i]));
/* 247:    */     }
/* 248:242 */     typeMatch.put("1", monophoneFmts);
/* 249:243 */     typeMatch.put("2", polyphoneFmts);
/* 250:244 */     typeMatch.put("3", realtoneFmts);
/* 251:245 */     typeMatch.put("4", logoFmts);
/* 252:246 */     typeMatch.put("5", wallpaperFmts);
/* 253:247 */     typeMatch.put("6", videoFmts);
/* 254:248 */     typeMatch.put("7", gameFmts);
/* 255:249 */     typeMatch.put("9", aniWallpaperFmts);
/* 256:250 */     typeMatch.put("10", themeFmts);
/* 257:    */   }
/* 258:    */   
/* 259:    */   public static boolean match(String typeId, String formatId)
/* 260:    */     throws Exception
/* 261:    */   {
/* 262:254 */     initTypeMatch();
/* 263:255 */     ArrayList formats = (ArrayList)typeMatch.get(typeId);
/* 264:    */     try
/* 265:    */     {
/* 266:257 */       if (formats.contains(new Integer(formatId))) {
/* 267:258 */         return true;
/* 268:    */       }
/* 269:260 */       return false;
/* 270:    */     }
/* 271:    */     catch (NumberFormatException e) {}
/* 272:263 */     return false;
/* 273:    */   }
/* 274:    */   
/* 275:    */   public static String stripAwayFileExtension(String fileName)
/* 276:    */   {
/* 277:269 */     int x = fileName.lastIndexOf(".");
/* 278:270 */     if (x != 0) {
/* 279:271 */       fileName = fileName.substring(0, x);
/* 280:    */     }
/* 281:274 */     return fileName;
/* 282:    */   }
/* 283:    */   
/* 284:    */   public static String changeFileExtension(String fileName, String newExt)
/* 285:    */   {
/* 286:279 */     int x = fileName.lastIndexOf(".");
/* 287:280 */     if (x > 0) {
/* 288:281 */       fileName = fileName.substring(0, x);
/* 289:    */     }
/* 290:284 */     return fileName + "." + newExt;
/* 291:    */   }
/* 292:    */   
/* 293:    */   public static String getFileExtension(String fileName)
/* 294:    */   {
/* 295:288 */     String ext = "";
/* 296:289 */     int x = fileName.lastIndexOf(".");
/* 297:290 */     if (x != 0) {
/* 298:291 */       ext = fileName.substring(x + 1, fileName.length());
/* 299:    */     }
/* 300:294 */     return ext;
/* 301:    */   }
/* 302:    */   
/* 303:    */   public static int getType(String fileName)
/* 304:    */     throws Exception
/* 305:    */   {
/* 306:300 */     String ext = getFileExtension(fileName);
/* 307:301 */     int type = getTypeForFormat(ext);
/* 308:302 */     return type;
/* 309:    */   }
/* 310:    */   
/* 311:    */   public static void main(String[] args)
/* 312:    */   {
/* 313:    */     try
/* 314:    */     {
/* 315:307 */       System.out.println(getTypeForFormat("motbin"));
/* 316:    */     }
/* 317:    */     catch (Exception ex) {}
/* 318:    */     try
/* 319:    */     {
/* 320:311 */       System.out.println(getTypeForFormat("aiff"));
/* 321:    */     }
/* 322:    */     catch (Exception ex1) {}
/* 323:    */   }
/* 324:    */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.util.ExtManager
 * JD-Core Version:    0.7.0.1
 */