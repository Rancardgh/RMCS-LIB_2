/*   1:    */ package com.rancard.security;
/*   2:    */ 
/*   3:    */ import com.rancard.util.Page;
/*   4:    */ import java.io.Serializable;
/*   5:    */ import java.text.SimpleDateFormat;
/*   6:    */ import java.util.Date;
/*   7:    */ 
/*   8:    */ public class ProfilesBean
/*   9:    */   extends roles
/*  10:    */   implements Serializable
/*  11:    */ {
/*  12:    */   private String username;
/*  13:    */   private String password;
/*  14:    */   private String role_id;
/*  15:    */   private String emp_id;
/*  16:    */   private String firstname;
/*  17:    */   private String lastname;
/*  18:    */   private String last_login;
/*  19:    */   private String email;
/*  20:    */   private String mobile_phone;
/*  21:    */   private String address;
/*  22:    */   public static final int COUNT = 5;
/*  23:    */   private int start;
/*  24:    */   private String middlename;
/*  25:    */   private String keyword;
/*  26: 21 */   private int count = 5;
/*  27:    */   private String sort_type;
/*  28:    */   
/*  29:    */   public void setusername(String username)
/*  30:    */   {
/*  31: 29 */     this.username = username;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public String getusername()
/*  35:    */   {
/*  36: 33 */     return this.username;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void setpassword(String password)
/*  40:    */   {
/*  41: 36 */     this.password = password;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public String getpassword()
/*  45:    */   {
/*  46: 40 */     return this.password;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void setrole_id(String role_ID)
/*  50:    */   {
/*  51: 43 */     this.role_id = role_ID;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public String getrole_id()
/*  55:    */   {
/*  56: 47 */     return this.role_id;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public void setemp_id(String emp_ID)
/*  60:    */   {
/*  61: 50 */     this.emp_id = emp_ID;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void setEmp_id(String emp_ID)
/*  65:    */   {
/*  66: 54 */     this.emp_id = emp_ID;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public String getemp_id()
/*  70:    */   {
/*  71: 58 */     return this.emp_id;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void setlastname(String lastname)
/*  75:    */   {
/*  76: 62 */     this.lastname = lastname;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void setLastname(String lastname)
/*  80:    */   {
/*  81: 65 */     this.lastname = lastname;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public String getlastname()
/*  85:    */   {
/*  86: 69 */     return this.lastname;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public void setfirstname(String firstname)
/*  90:    */   {
/*  91: 73 */     this.firstname = firstname;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public void setFirstname(String firstname)
/*  95:    */   {
/*  96: 76 */     this.firstname = firstname;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public String getfirstname()
/* 100:    */   {
/* 101: 79 */     return this.firstname;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public void setlast_login(String last_login)
/* 105:    */   {
/* 106: 83 */     this.last_login = last_login;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public void setStart(int start)
/* 110:    */   {
/* 111: 86 */     this.start = start;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public String getlast_login()
/* 115:    */   {
/* 116: 90 */     return this.last_login;
/* 117:    */   }
/* 118:    */   
/* 119:    */   public void createProfiles()
/* 120:    */     throws Exception
/* 121:    */   {
/* 122: 94 */     ProfilesDB Profiles = new ProfilesDB();
/* 123:    */     
/* 124: 96 */     SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
/* 125: 97 */     Date utilDate_last_logindate = null;
/* 126:    */     
/* 127: 99 */     Profiles.createProfiles(this.username, this.password, this.role_id, this.firstname, this.lastname, this.email, this.middlename);
/* 128:    */   }
/* 129:    */   
/* 130:    */   public void createProfiles2()
/* 131:    */     throws Exception
/* 132:    */   {
/* 133:104 */     ProfilesDB Profiles = new ProfilesDB();
/* 134:    */     
/* 135:106 */     Profiles.createProfiles(this.username, this.password, this.role_id, this.emp_id);
/* 136:    */   }
/* 137:    */   
/* 138:    */   public void updateProfiles()
/* 139:    */     throws Exception
/* 140:    */   {
/* 141:110 */     ProfilesDB Profiles = new ProfilesDB();
/* 142:    */     
/* 143:112 */     SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
/* 144:113 */     Date utilDate_last_logindate = null;
/* 145:    */     try
/* 146:    */     {
/* 147:115 */       utilDate_last_logindate = sdf.parse(this.last_login);
/* 148:    */     }
/* 149:    */     catch (Exception e)
/* 150:    */     {
/* 151:117 */       throw new Exception(e.getMessage());
/* 152:    */     }
/* 153:120 */     Profiles.updateProfiles(this.username, this.password, this.role_id, this.emp_id, utilDate_last_logindate);
/* 154:    */   }
/* 155:    */   
/* 156:    */   public void updateProfiles2()
/* 157:    */     throws Exception
/* 158:    */   {
/* 159:125 */     ProfilesDB Profiles = new ProfilesDB();
/* 160:    */     
/* 161:127 */     Profiles.updateProfiles(this.username, this.password, this.role_id, this.emp_id);
/* 162:    */   }
/* 163:    */   
/* 164:    */   public void updateAllProfiles()
/* 165:    */     throws Exception
/* 166:    */   {
/* 167:130 */     ProfilesDB Profiles = new ProfilesDB();
/* 168:    */     
/* 169:132 */     Profiles.updateAllProfiles(this.username, this.password, this.role_id, this.emp_id, this.firstname, this.lastname, this.email, this.middlename, this.mobile_phone);
/* 170:    */   }
/* 171:    */   
/* 172:    */   public void updateUserProfile()
/* 173:    */     throws Exception
/* 174:    */   {
/* 175:135 */     ProfilesDB Profiles = new ProfilesDB();
/* 176:136 */     Profiles.updateUserProfiles(this.username, this.password, this.role_id);
/* 177:    */   }
/* 178:    */   
/* 179:    */   public void updateLastLogin()
/* 180:    */     throws Exception
/* 181:    */   {
/* 182:141 */     ProfilesDB Profiles = new ProfilesDB();
/* 183:    */     
/* 184:143 */     SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
/* 185:144 */     Date utilDate_last_logindate = null;
/* 186:    */     try
/* 187:    */     {
/* 188:146 */       utilDate_last_logindate = sdf.parse(this.last_login);
/* 189:    */     }
/* 190:    */     catch (Exception e)
/* 191:    */     {
/* 192:148 */       throw new Exception(e.getMessage());
/* 193:    */     }
/* 194:151 */     Profiles.updateLastLogin(this.emp_id, utilDate_last_logindate);
/* 195:    */   }
/* 196:    */   
/* 197:    */   public void deleteProfiles()
/* 198:    */     throws Exception
/* 199:    */   {
/* 200:156 */     ProfilesDB Profiles = new ProfilesDB();
/* 201:    */     
/* 202:158 */     Profiles.deleteProfiles();
/* 203:    */   }
/* 204:    */   
/* 205:    */   public void deleteUser()
/* 206:    */     throws Exception
/* 207:    */   {
/* 208:162 */     ProfilesDB Profiles = new ProfilesDB();
/* 209:    */     
/* 210:164 */     Profiles.deleteUser(this.username);
/* 211:    */   }
/* 212:    */   
/* 213:    */   public void viewProfiles()
/* 214:    */     throws Exception
/* 215:    */   {
/* 216:168 */     ProfilesBean bean = new ProfilesBean();
/* 217:169 */     ProfilesDB Profiles = new ProfilesDB();
/* 218:170 */     SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
/* 219:171 */     SimpleDateFormat newDF = new SimpleDateFormat("dd/MM/yyyy");
/* 220:172 */     Date utilDate = null;
/* 221:    */     
/* 222:174 */     bean = Profiles.viewProfiles(this.username);
/* 223:    */     
/* 224:176 */     this.username = bean.getusername();
/* 225:177 */     this.password = bean.getpassword();
/* 226:178 */     this.role_id = bean.getrole_id();
/* 227:179 */     this.emp_id = bean.getemp_id();
/* 228:180 */     this.firstname = bean.getfirstname();
/* 229:181 */     this.lastname = bean.getlastname();
/* 230:182 */     setEmail(bean.getEmail());
/* 231:183 */     setMiddlename(bean.getMiddlename());
/* 232:184 */     setMobile_phone(bean.getMobile_phone());
/* 233:185 */     if (bean.getlast_login() != null)
/* 234:    */     {
/* 235:186 */       utilDate = df.parse(bean.getlast_login());
/* 236:187 */       this.last_login = newDF.format(utilDate);
/* 237:    */     }
/* 238:    */   }
/* 239:    */   
/* 240:    */   public Page viewAllUserProfiles(int st)
/* 241:    */     throws Exception
/* 242:    */   {
/* 243:198 */     return new ProfilesDB().viewAllUserProfiles(st, this.count, this.sort_type);
/* 244:    */   }
/* 245:    */   
/* 246:    */   public Page find()
/* 247:    */     throws Exception
/* 248:    */   {
/* 249:203 */     return new ProfilesDB().viewAllUserProfiles(getKeyword(), getStart(), 5);
/* 250:    */   }
/* 251:    */   
/* 252:    */   public ProfilesBean searchProfiles(String keyword)
/* 253:    */     throws Exception
/* 254:    */   {
/* 255:207 */     ProfilesDB Profiles = new ProfilesDB();
/* 256:208 */     return Profiles.searchProfiles(keyword);
/* 257:    */   }
/* 258:    */   
/* 259:    */   public String WinLogonUser_env()
/* 260:    */   {
/* 261:211 */     String envUser = System.getProperty("user.name");
/* 262:    */     
/* 263:213 */     return envUser;
/* 264:    */   }
/* 265:    */   
/* 266:    */   public void setEmail(String email)
/* 267:    */   {
/* 268:216 */     this.email = email;
/* 269:    */   }
/* 270:    */   
/* 271:    */   public String getEmail()
/* 272:    */   {
/* 273:219 */     return this.email;
/* 274:    */   }
/* 275:    */   
/* 276:    */   public void setMobile_phone(String mobile_phone)
/* 277:    */   {
/* 278:222 */     this.mobile_phone = mobile_phone;
/* 279:    */   }
/* 280:    */   
/* 281:    */   public String getMobile_phone()
/* 282:    */   {
/* 283:225 */     return this.mobile_phone;
/* 284:    */   }
/* 285:    */   
/* 286:    */   public void setAddress(String address)
/* 287:    */   {
/* 288:228 */     this.address = address;
/* 289:    */   }
/* 290:    */   
/* 291:    */   public String getAddress()
/* 292:    */   {
/* 293:231 */     return this.address;
/* 294:    */   }
/* 295:    */   
/* 296:    */   public String getName()
/* 297:    */   {
/* 298:234 */     return getfirstname() + " " + getMiddlename() + " " + getlastname();
/* 299:    */   }
/* 300:    */   
/* 301:    */   public int getStart()
/* 302:    */   {
/* 303:238 */     return this.start;
/* 304:    */   }
/* 305:    */   
/* 306:    */   public void setMiddlename(String middlename)
/* 307:    */   {
/* 308:241 */     this.middlename = middlename;
/* 309:    */   }
/* 310:    */   
/* 311:    */   public String getMiddlename()
/* 312:    */   {
/* 313:244 */     return this.middlename;
/* 314:    */   }
/* 315:    */   
/* 316:    */   public void setKeyword(String keyword)
/* 317:    */   {
/* 318:247 */     this.keyword = keyword;
/* 319:    */   }
/* 320:    */   
/* 321:    */   public String getKeyword()
/* 322:    */   {
/* 323:250 */     return this.keyword;
/* 324:    */   }
/* 325:    */   
/* 326:    */   public void setCount(int count)
/* 327:    */   {
/* 328:254 */     this.count = count;
/* 329:    */   }
/* 330:    */   
/* 331:    */   public int getCount()
/* 332:    */   {
/* 333:257 */     return this.count;
/* 334:    */   }
/* 335:    */   
/* 336:    */   public void setSort_type(String sort_type)
/* 337:    */   {
/* 338:260 */     this.sort_type = sort_type;
/* 339:    */   }
/* 340:    */   
/* 341:    */   public String getSort_type()
/* 342:    */   {
/* 343:263 */     return this.sort_type;
/* 344:    */   }
/* 345:    */   
/* 346:    */   public void verifyUser()
/* 347:    */     throws Exception
/* 348:    */   {
/* 349:267 */     ProfilesDB Profile = new ProfilesDB();
/* 350:268 */     ProfilesBean bean = Profile.verifyUser(this.username);
/* 351:269 */     this.username = bean.getusername();
/* 352:270 */     this.password = bean.getpassword();
/* 353:271 */     this.role_id = bean.getRole_id();
/* 354:    */   }
/* 355:    */   
/* 356:    */   public boolean isValidUser()
/* 357:    */     throws Exception
/* 358:    */   {
/* 359:276 */     boolean isValid = false;
/* 360:277 */     ProfilesDB Profile = new ProfilesDB();
/* 361:    */     try
/* 362:    */     {
/* 363:280 */       ProfilesBean bean = Profile.verifyUser(this.username, this.password);
/* 364:281 */       this.username = bean.getusername();
/* 365:282 */       this.password = bean.getpassword();
/* 366:    */       
/* 367:284 */       this.role_id = bean.getRole_id();
/* 368:    */     }
/* 369:    */     catch (Exception ex)
/* 370:    */     {
/* 371:287 */       setusername(null);
/* 372:288 */       setpassword(null);
/* 373:    */     }
/* 374:291 */     if ((this.username != null) && (this.password != null)) {
/* 375:292 */       isValid = true;
/* 376:    */     }
/* 377:295 */     return isValid;
/* 378:    */   }
/* 379:    */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.security.ProfilesBean
 * JD-Core Version:    0.7.0.1
 */