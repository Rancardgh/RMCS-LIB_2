/*   1:    */ package com.rancard.security;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ 
/*   5:    */ public class roles
/*   6:    */ {
/*   7:    */   private String role_id;
/*   8:    */   private String role_name;
/*   9:    */   private boolean result;
/*  10: 20 */   rolesDB rolDB = new rolesDB();
/*  11:    */   private String createUser;
/*  12:    */   private String deleteUser;
/*  13:    */   private String manageUser;
/*  14:    */   private String manageArticles;
/*  15:    */   private String createFolder;
/*  16:    */   private String deleteFolder;
/*  17:    */   private String manageTemplate;
/*  18:    */   private String publishTemplate;
/*  19:    */   private String menu1;
/*  20:    */   private String menu2;
/*  21:    */   private String menu3;
/*  22:    */   private String menu4;
/*  23:    */   private String menu5;
/*  24:    */   private String menu6;
/*  25:    */   
/*  26:    */   public String getRole_id()
/*  27:    */   {
/*  28: 37 */     return this.role_id;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void setRole_id(String role_id)
/*  32:    */   {
/*  33: 41 */     this.role_id = role_id;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setRole_name(String role_name)
/*  37:    */   {
/*  38: 45 */     this.role_name = role_name;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public String getRole_name()
/*  42:    */   {
/*  43: 49 */     return this.role_name;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public boolean canCreateUser()
/*  47:    */   {
/*  48: 53 */     if ("1".equals(this.createUser)) {
/*  49: 54 */       return true;
/*  50:    */     }
/*  51: 57 */     return false;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public boolean canManageUsers()
/*  55:    */   {
/*  56: 68 */     if ("1".equals(this.manageUser)) {
/*  57: 69 */       return true;
/*  58:    */     }
/*  59: 72 */     return false;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public boolean canDeleteFolder()
/*  63:    */   {
/*  64: 83 */     if ("1".equals(this.deleteFolder)) {
/*  65: 84 */       return true;
/*  66:    */     }
/*  67: 87 */     return false;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public boolean canCreateFolder()
/*  71:    */   {
/*  72: 93 */     if ("1".equals(this.createFolder)) {
/*  73: 94 */       return true;
/*  74:    */     }
/*  75: 97 */     return false;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public boolean canManageTemplate()
/*  79:    */   {
/*  80:105 */     if ("1".equals(this.manageTemplate)) {
/*  81:106 */       return true;
/*  82:    */     }
/*  83:109 */     return false;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public boolean canViewMenu1()
/*  87:    */   {
/*  88:120 */     if ("1".equals(this.menu1)) {
/*  89:121 */       return true;
/*  90:    */     }
/*  91:124 */     return false;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public boolean canViewMenu2()
/*  95:    */   {
/*  96:130 */     if ("1".equals(this.menu2)) {
/*  97:131 */       return true;
/*  98:    */     }
/*  99:134 */     return false;
/* 100:    */   }
/* 101:    */   
/* 102:    */   public boolean canViewMenu3()
/* 103:    */   {
/* 104:140 */     if ("1".equals(this.menu3)) {
/* 105:141 */       return true;
/* 106:    */     }
/* 107:144 */     return false;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public boolean canViewMenu4()
/* 111:    */   {
/* 112:150 */     if ("1".equals(this.menu4)) {
/* 113:151 */       return true;
/* 114:    */     }
/* 115:154 */     return false;
/* 116:    */   }
/* 117:    */   
/* 118:    */   public boolean canViewMenu5()
/* 119:    */   {
/* 120:161 */     if ("1".equals(this.menu5)) {
/* 121:162 */       return true;
/* 122:    */     }
/* 123:165 */     return false;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public boolean canViewMenu6()
/* 127:    */   {
/* 128:173 */     if ("1".equals(this.menu6)) {
/* 129:174 */       return true;
/* 130:    */     }
/* 131:177 */     return false;
/* 132:    */   }
/* 133:    */   
/* 134:    */   public void viewRoles()
/* 135:    */   {
/* 136:183 */     roles roleType = new roles();
/* 137:    */     try
/* 138:    */     {
/* 139:186 */       roleType = this.rolDB.viewRoles(this.role_id);
/* 140:187 */       this.role_id = roleType.getRole_id();
/* 141:188 */       this.role_name = roleType.getRole_name();
/* 142:189 */       this.createFolder = roleType.getCreateFolder();
/* 143:190 */       this.deleteFolder = roleType.getDeleteFolder();
/* 144:191 */       this.manageUser = roleType.getManageUser();
/* 145:192 */       this.manageTemplate = roleType.getManageTemplate();
/* 146:193 */       this.menu1 = roleType.getMenu1();
/* 147:194 */       this.menu2 = roleType.getMenu2();
/* 148:195 */       this.menu3 = roleType.getMenu3();
/* 149:196 */       this.menu4 = roleType.getMenu4();
/* 150:197 */       this.menu5 = roleType.getMenu5();
/* 151:198 */       this.menu6 = roleType.getMenu6();
/* 152:    */     }
/* 153:    */     catch (Exception e) {}
/* 154:    */   }
/* 155:    */   
/* 156:    */   public HashMap viewAllRoles()
/* 157:    */     throws Exception
/* 158:    */   {
/* 159:210 */     rolesDB roleInfo = new rolesDB();
/* 160:    */     
/* 161:212 */     return roleInfo.getAllRoles();
/* 162:    */   }
/* 163:    */   
/* 164:    */   public String getroleName(String id)
/* 165:    */   {
/* 166:216 */     rolesDB roleInfo2 = new rolesDB();
/* 167:217 */     String rolename = "";
/* 168:    */     try
/* 169:    */     {
/* 170:219 */       rolename = roleInfo2.getroleName(id);
/* 171:    */     }
/* 172:    */     catch (Exception e) {}
/* 173:224 */     return rolename;
/* 174:    */   }
/* 175:    */   
/* 176:    */   public void setCreateUser(String createUser)
/* 177:    */   {
/* 178:228 */     this.createUser = createUser;
/* 179:    */   }
/* 180:    */   
/* 181:    */   public String getCreateUser()
/* 182:    */   {
/* 183:232 */     return this.createUser;
/* 184:    */   }
/* 185:    */   
/* 186:    */   public void setDeleteUser(String deleteUser)
/* 187:    */   {
/* 188:236 */     this.deleteUser = deleteUser;
/* 189:    */   }
/* 190:    */   
/* 191:    */   public String getDeleteUser()
/* 192:    */   {
/* 193:240 */     return this.deleteUser;
/* 194:    */   }
/* 195:    */   
/* 196:    */   public void setManageUser(String manageUser)
/* 197:    */   {
/* 198:244 */     this.manageUser = manageUser;
/* 199:    */   }
/* 200:    */   
/* 201:    */   public String getManageUser()
/* 202:    */   {
/* 203:248 */     return this.manageUser;
/* 204:    */   }
/* 205:    */   
/* 206:    */   public void setManageArticles(String manageArticles)
/* 207:    */   {
/* 208:252 */     this.manageArticles = manageArticles;
/* 209:    */   }
/* 210:    */   
/* 211:    */   public String getManageArticles()
/* 212:    */   {
/* 213:256 */     return this.manageArticles;
/* 214:    */   }
/* 215:    */   
/* 216:    */   public void setCreateFolder(String createFolder)
/* 217:    */   {
/* 218:260 */     this.createFolder = createFolder;
/* 219:    */   }
/* 220:    */   
/* 221:    */   public String getCreateFolder()
/* 222:    */   {
/* 223:264 */     return this.createFolder;
/* 224:    */   }
/* 225:    */   
/* 226:    */   public void setDeleteFolder(String deleteFolder)
/* 227:    */   {
/* 228:268 */     this.deleteFolder = deleteFolder;
/* 229:    */   }
/* 230:    */   
/* 231:    */   public String getDeleteFolder()
/* 232:    */   {
/* 233:272 */     return this.deleteFolder;
/* 234:    */   }
/* 235:    */   
/* 236:    */   public void setManageTemplate(String manageTemplate)
/* 237:    */   {
/* 238:276 */     this.manageTemplate = manageTemplate;
/* 239:    */   }
/* 240:    */   
/* 241:    */   public String getManageTemplate()
/* 242:    */   {
/* 243:280 */     return this.manageTemplate;
/* 244:    */   }
/* 245:    */   
/* 246:    */   public void setPublishTemplate(String publishTemplate)
/* 247:    */   {
/* 248:284 */     this.publishTemplate = publishTemplate;
/* 249:    */   }
/* 250:    */   
/* 251:    */   public String getPublishTemplate()
/* 252:    */   {
/* 253:288 */     return this.publishTemplate;
/* 254:    */   }
/* 255:    */   
/* 256:    */   public void setMenu1(String menu1)
/* 257:    */   {
/* 258:292 */     this.menu1 = menu1;
/* 259:    */   }
/* 260:    */   
/* 261:    */   public String getMenu1()
/* 262:    */   {
/* 263:296 */     return this.menu1;
/* 264:    */   }
/* 265:    */   
/* 266:    */   public void setMenu2(String menu2)
/* 267:    */   {
/* 268:300 */     this.menu2 = menu2;
/* 269:    */   }
/* 270:    */   
/* 271:    */   public String getMenu2()
/* 272:    */   {
/* 273:304 */     return this.menu2;
/* 274:    */   }
/* 275:    */   
/* 276:    */   public void setMenu3(String menu3)
/* 277:    */   {
/* 278:308 */     this.menu3 = menu3;
/* 279:    */   }
/* 280:    */   
/* 281:    */   public String getMenu3()
/* 282:    */   {
/* 283:312 */     return this.menu3;
/* 284:    */   }
/* 285:    */   
/* 286:    */   public void setMenu4(String menu4)
/* 287:    */   {
/* 288:316 */     this.menu4 = menu4;
/* 289:    */   }
/* 290:    */   
/* 291:    */   public String getMenu4()
/* 292:    */   {
/* 293:320 */     return this.menu4;
/* 294:    */   }
/* 295:    */   
/* 296:    */   public void setMenu5(String menu5)
/* 297:    */   {
/* 298:324 */     this.menu5 = menu5;
/* 299:    */   }
/* 300:    */   
/* 301:    */   public String getMenu5()
/* 302:    */   {
/* 303:328 */     return this.menu5;
/* 304:    */   }
/* 305:    */   
/* 306:    */   public void setMenu6(String menu6)
/* 307:    */   {
/* 308:332 */     this.menu6 = menu6;
/* 309:    */   }
/* 310:    */   
/* 311:    */   public String getMenu6()
/* 312:    */   {
/* 313:336 */     return this.menu6;
/* 314:    */   }
/* 315:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.security.roles
 * JD-Core Version:    0.7.0.1
 */