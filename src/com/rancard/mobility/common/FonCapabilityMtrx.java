/*   1:    */ package com.rancard.mobility.common;
/*   2:    */ 
/*   3:    */ import com.rancard.common.Config;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.Set;
/*   7:    */ import java.util.TreeMap;
/*   8:    */ import javax.naming.Context;
/*   9:    */ import javax.naming.InitialContext;
/*  10:    */ import javax.naming.NamingException;
/*  11:    */ import net.sourceforge.wurfl.wurflapi.CapabilityMatrix;
/*  12:    */ import net.sourceforge.wurfl.wurflapi.ListManager;
/*  13:    */ import net.sourceforge.wurfl.wurflapi.ObjectsManager;
/*  14:    */ import net.sourceforge.wurfl.wurflapi.UAManager;
/*  15:    */ import net.sourceforge.wurfl.wurflapi.WurflDevice;
/*  16:    */ 
/*  17:    */ public class FonCapabilityMtrx
/*  18:    */ {
/*  19: 12 */   private UAManager uam = null;
/*  20: 13 */   private CapabilityMatrix cm = null;
/*  21: 14 */   private ListManager lm = null;
/*  22: 15 */   private TreeMap allDevices = null;
/*  23: 16 */   private TreeMap allActualDevices = null;
/*  24: 17 */   private HashMap capabilities = null;
/*  25:    */   private String wurflPath;
/*  26:    */   private String wurflPatchPath;
/*  27:    */   
/*  28:    */   public FonCapabilityMtrx()
/*  29:    */     throws Exception
/*  30:    */   {
/*  31: 23 */     this.wurflPath = getWurflPath();
/*  32: 24 */     this.wurflPatchPath = getWurflPatchPath();
/*  33:    */     
/*  34: 26 */     ObjectsManager.getWurflInstance(this.wurflPath);
/*  35:    */     
/*  36: 28 */     this.uam = ObjectsManager.getUAManagerInstance();
/*  37: 29 */     this.cm = ObjectsManager.getCapabilityMatrixInstance();
/*  38: 30 */     this.lm = ObjectsManager.getListManagerInstance();
/*  39:    */     
/*  40: 32 */     this.allDevices = this.lm.getDeviceGroupedByBrand();
/*  41: 33 */     this.allActualDevices = this.lm.getActualDeviceElementsList();
/*  42:    */     
/*  43: 35 */     initCapabilities();
/*  44:    */   }
/*  45:    */   
/*  46:    */   private String getWurflPath()
/*  47:    */     throws Exception
/*  48:    */   {
/*  49: 41 */     String path = "WEB-INF/work";
/*  50:    */     try
/*  51:    */     {
/*  52: 43 */       Context ctx = new InitialContext();
/*  53: 44 */       Config appConfig = (Config)ctx.lookup("java:comp/env/bean/rmcsConfig");
/*  54: 46 */       if ((appConfig != null) && (appConfig.valueOf("WURFL") != null)) {
/*  55: 47 */         path = appConfig.valueOf("WURFL");
/*  56:    */       }
/*  57:    */     }
/*  58:    */     catch (NamingException e)
/*  59:    */     {
/*  60: 53 */       throw new Exception(e.getMessage());
/*  61:    */     }
/*  62: 56 */     return path;
/*  63:    */   }
/*  64:    */   
/*  65:    */   private String getWurflPatchPath()
/*  66:    */     throws Exception
/*  67:    */   {
/*  68: 63 */     String path = "WEB-INF/work";
/*  69:    */     try
/*  70:    */     {
/*  71: 65 */       Context ctx = new InitialContext();
/*  72: 66 */       Config appConfig = (Config)ctx.lookup("java:comp/env/bean/rmcsConfig");
/*  73: 68 */       if ((appConfig != null) && (appConfig.valueOf("WURFL_PATCH") != null)) {
/*  74: 69 */         path = appConfig.valueOf("WURFL_PATCH");
/*  75:    */       }
/*  76:    */     }
/*  77:    */     catch (NamingException e)
/*  78:    */     {
/*  79: 75 */       throw new Exception(e.getMessage());
/*  80:    */     }
/*  81: 78 */     return path;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public ArrayList getBrandNames()
/*  85:    */     throws Exception
/*  86:    */   {
/*  87: 84 */     return this.lm.getDeviceBrandList();
/*  88:    */   }
/*  89:    */   
/*  90:    */   public Object[] getModelNamesFor(String brand)
/*  91:    */     throws Exception
/*  92:    */   {
/*  93: 89 */     TreeMap models = (TreeMap)this.allDevices.get(brand);
/*  94: 90 */     return models.keySet().toArray();
/*  95:    */   }
/*  96:    */   
/*  97:    */   public WurflDevice getActualDevice(String brand, String model)
/*  98:    */     throws Exception
/*  99:    */   {
/* 100: 96 */     TreeMap models = (TreeMap)this.allDevices.get(brand);
/* 101: 97 */     return (WurflDevice)models.get(model);
/* 102:    */   }
/* 103:    */   
/* 104:    */   public WurflDevice getActualDevice(String phoneId)
/* 105:    */     throws Exception
/* 106:    */   {
/* 107:102 */     if (this.allActualDevices.containsKey(phoneId)) {
/* 108:103 */       return (WurflDevice)this.allActualDevices.get(phoneId);
/* 109:    */     }
/* 110:105 */     return null;
/* 111:    */   }
/* 112:    */   
/* 113:    */   private void initCapabilities()
/* 114:    */   {
/* 115:111 */     this.capabilities = new HashMap();
/* 116:    */     
/* 117:    */ 
/* 118:    */ 
/* 119:115 */     ArrayList panasonic = null;
/* 120:    */     ArrayList midi_poly;
/* 121:116 */     (midi_poly = new ArrayList()).add("mid");
/* 122:117 */     midi_poly.add("midi");
/* 123:118 */     midi_poly.add("0.mid");
/* 124:119 */     midi_poly.add("1.mid");
/* 125:    */     ArrayList mmf;
/* 126:120 */     (mmf = new ArrayList()).add("mmf");
/* 127:121 */     mmf.add("ma1.mmf");
/* 128:122 */     mmf.add("ma2.mmf");
/* 129:123 */     mmf.add("ma3.mmf");
/* 130:    */     ArrayList imy;
/* 131:124 */     (imy = new ArrayList()).add("imy");
/* 132:125 */     imy.add("10.imy");
/* 133:126 */     imy.add("12.imy");
/* 134:    */     ArrayList amr;
/* 135:127 */     (amr = new ArrayList()).add("amr");
/* 136:    */     ArrayList awb;
/* 137:128 */     (awb = new ArrayList()).add("awb");
/* 138:    */     ArrayList aac;
/* 139:129 */     (aac = new ArrayList()).add("aac");
/* 140:    */     ArrayList wav;
/* 141:130 */     (wav = new ArrayList()).add("wav");
/* 142:    */     ArrayList mp3;
/* 143:131 */     (mp3 = new ArrayList()).add("mp3");
/* 144:    */     ArrayList spmidi;
/* 145:132 */     (spmidi = new ArrayList()).add("sp.mid");
/* 146:    */     ArrayList qcelp;
/* 147:133 */     (qcelp = new ArrayList()).add("qcp");
/* 148:    */     ArrayList wbmp;
/* 149:134 */     (wbmp = new ArrayList()).add("wbmp");
/* 150:    */     ArrayList bmp;
/* 151:135 */     (bmp = new ArrayList()).add("bmp");
/* 152:    */     ArrayList jpg;
/* 153:136 */     (jpg = new ArrayList()).add("jpg");
/* 154:137 */     jpg.add("jpeg");
/* 155:    */     ArrayList gif;
/* 156:138 */     (gif = new ArrayList()).add("gif");
/* 157:    */     ArrayList tiff;
/* 158:139 */     (tiff = new ArrayList()).add("tiff");
/* 159:140 */     tiff.add("tif");
/* 160:    */     ArrayList png;
/* 161:141 */     (png = new ArrayList()).add("png");
/* 162:    */     ArrayList vid3gpp;
/* 163:142 */     (vid3gpp = new ArrayList()).add("3gpp");
/* 164:143 */     vid3gpp.add("3gp");
/* 165:    */     ArrayList vid3gpp2;
/* 166:144 */     (vid3gpp2 = new ArrayList()).add("3gpp2");
/* 167:    */     ArrayList mp4;
/* 168:145 */     (mp4 = new ArrayList()).add("mp4");
/* 169:146 */     mp4.add("m4v");
/* 170:    */     ArrayList mov;
/* 171:147 */     (mov = new ArrayList()).add("mov");
/* 172:    */     ArrayList wmv;
/* 173:148 */     (wmv = new ArrayList()).add("wmv");
/* 174:    */     ArrayList ems;
/* 175:149 */     (ems = new ArrayList()).add("ems");
/* 176:150 */     ems.add("ems.extende");
/* 177:151 */     ems.add("emy");
/* 178:    */     ArrayList emy;
/* 179:152 */     (emy = new ArrayList()).add("emy");
/* 180:    */     ArrayList jar;
/* 181:153 */     (jar = new ArrayList()).add("jar");
/* 182:    */     ArrayList jad;
/* 183:154 */     (jad = new ArrayList()).add("jad");
/* 184:    */     ArrayList sis;
/* 185:155 */     (sis = new ArrayList()).add("sis");
/* 186:    */     ArrayList nokia;
/* 187:156 */     (nokia = new ArrayList()).add("noktxt");
/* 188:157 */     nokia.add("nokia");
/* 189:158 */     nokia.add("7bit.nokia");
/* 190:159 */     nokia.add("8bit.nokia");
/* 191:    */     ArrayList motorola;
/* 192:160 */     (motorola = new ArrayList()).add("motbin");
/* 193:161 */     motorola.add("mot");
/* 194:    */     ArrayList sagem1;
/* 195:162 */     (sagem1 = new ArrayList()).add("10.sagem");
/* 196:    */     ArrayList sagem2;
/* 197:163 */     (sagem2 = new ArrayList()).add("21.sagem");
/* 198:164 */     panasonic = new ArrayList();
/* 199:    */     
/* 200:    */ 
/* 201:    */ 
/* 202:    */ 
/* 203:    */ 
/* 204:    */ 
/* 205:    */ 
/* 206:    */ 
/* 207:    */ 
/* 208:    */ 
/* 209:    */ 
/* 210:    */ 
/* 211:    */ 
/* 212:    */ 
/* 213:    */ 
/* 214:    */ 
/* 215:    */ 
/* 216:    */ 
/* 217:    */ 
/* 218:    */ 
/* 219:    */ 
/* 220:    */ 
/* 221:    */ 
/* 222:    */ 
/* 223:    */ 
/* 224:    */ 
/* 225:    */ 
/* 226:    */ 
/* 227:    */ 
/* 228:    */ 
/* 229:    */ 
/* 230:196 */     this.capabilities.put("midi_polyphonic", midi_poly);
/* 231:197 */     this.capabilities.put("imelody", imy);
/* 232:198 */     this.capabilities.put("mmf", mmf);
/* 233:199 */     this.capabilities.put("amr", amr);
/* 234:200 */     this.capabilities.put("awb", awb);
/* 235:201 */     this.capabilities.put("aac", aac);
/* 236:202 */     this.capabilities.put("wav", wav);
/* 237:203 */     this.capabilities.put("mp3", mp3);
/* 238:204 */     this.capabilities.put("sp_midi", spmidi);
/* 239:205 */     this.capabilities.put("qcelp", qcelp);
/* 240:206 */     this.capabilities.put("wbmp", wbmp);
/* 241:207 */     this.capabilities.put("bmp", bmp);
/* 242:208 */     this.capabilities.put("gif", gif);
/* 243:209 */     this.capabilities.put("jpg", jpg);
/* 244:210 */     this.capabilities.put("png", png);
/* 245:211 */     this.capabilities.put("tiff", tiff);
/* 246:212 */     this.capabilities.put("video_3gpp", vid3gpp);
/* 247:213 */     this.capabilities.put("video_3gpp2", vid3gpp2);
/* 248:214 */     this.capabilities.put("video_mp4", mp4);
/* 249:215 */     this.capabilities.put("video_wmv", wmv);
/* 250:216 */     this.capabilities.put("ems", ems);
/* 251:217 */     this.capabilities.put("nokiaring", nokia);
/* 252:218 */     this.capabilities.put("mms_symbian_install", sis);
/* 253:219 */     this.capabilities.put("mms_jar", jar);
/* 254:220 */     this.capabilities.put("mms_jad", jad);
/* 255:221 */     this.capabilities.put("gprtf", motorola);
/* 256:222 */     this.capabilities.put("sagem_v1", sagem1);
/* 257:223 */     this.capabilities.put("sagem_v2", sagem2);
/* 258:224 */     this.capabilities.put("panasonic", panasonic);
/* 259:    */   }
/* 260:    */   
/* 261:    */   public ListManager getListManager()
/* 262:    */   {
/* 263:229 */     return this.lm;
/* 264:    */   }
/* 265:    */   
/* 266:    */   public CapabilityMatrix getCapabilitiesManager()
/* 267:    */   {
/* 268:234 */     return this.cm;
/* 269:    */   }
/* 270:    */   
/* 271:    */   public UAManager getUAManager()
/* 272:    */   {
/* 273:239 */     return this.uam;
/* 274:    */   }
/* 275:    */   
/* 276:    */   public String findSupportedCapability(String format)
/* 277:    */   {
/* 278:244 */     String capability = null;
/* 279:245 */     Object[] capa = this.capabilities.keySet().toArray();
/* 280:246 */     for (int i = 0; i < capa.length; i++)
/* 281:    */     {
/* 282:247 */       ArrayList fmts = (ArrayList)this.capabilities.get(capa[i].toString());
/* 283:248 */       if (fmts.contains(format))
/* 284:    */       {
/* 285:249 */         capability = capa[i].toString();
/* 286:250 */         break;
/* 287:    */       }
/* 288:    */     }
/* 289:253 */     return capability;
/* 290:    */   }
/* 291:    */   
/* 292:    */   public static void main(String[] args)
/* 293:    */   {
/* 294:281 */     FonCapabilityMtrx fcm = null;
/* 295:282 */     ArrayList al = null;
/* 296:283 */     Object[] models = null;
/* 297:284 */     Object[] capa = null;
/* 298:285 */     String formats = null;
/* 299:286 */     HashMap hm = null;
/* 300:287 */     WurflDevice dev = null;
/* 301:    */   }
/* 302:    */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.common.FonCapabilityMtrx
 * JD-Core Version:    0.7.0.1
 */