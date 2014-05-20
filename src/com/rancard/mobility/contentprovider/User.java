/*   1:    */ package com.rancard.mobility.contentprovider;
/*   2:    */ 
/*   3:    */ import com.rancard.common.Message;
/*   4:    */ import com.rancard.security.AccessListItemDB;
/*   5:    */ import com.rancard.security.ProfilesBean;
/*   6:    */ import com.rancard.util.PasswordGenerator;
/*   7:    */ import java.io.PrintStream;
/*   8:    */ import java.io.Serializable;
/*   9:    */ import java.util.ArrayList;
/*  10:    */ 
/*  11:    */ public class User
/*  12:    */   implements Serializable
/*  13:    */ {
/*  14:    */   private String id;
/*  15:    */   private String name;
/*  16:    */   private String password;
/*  17:    */   private String username;
/*  18:    */   private ProfilesBean transactionAuthor;
/*  19:    */   private ArrayList creditBalances;
/*  20:    */   private String defaultSmsc;
/*  21:    */   private String defaultService;
/*  22:    */   private ArrayList connections;
/*  23:    */   private ArrayList services;
/*  24:    */   private String logoUrl;
/*  25:    */   private String defaultLanguage;
/*  26:    */   private double bandwidthBalance;
/*  27:    */   private double inboxBalance;
/*  28:    */   private double outboxBalance;
/*  29:    */   
/*  30:    */   public User()
/*  31:    */   {
/*  32: 31 */     this.id = "";
/*  33: 32 */     this.name = "";
/*  34: 33 */     this.password = "";
/*  35: 34 */     this.username = "";
/*  36:    */     
/*  37: 36 */     this.defaultSmsc = "";
/*  38: 37 */     this.defaultService = "";
/*  39: 38 */     this.connections = new ArrayList();
/*  40: 39 */     this.services = new ArrayList();
/*  41:    */     
/*  42: 41 */     this.logoUrl = "";
/*  43: 42 */     this.defaultLanguage = "";
/*  44:    */     
/*  45: 44 */     this.bandwidthBalance = 0.0D;
/*  46: 45 */     this.inboxBalance = 0.0D;
/*  47: 46 */     this.outboxBalance = 0.0D;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public User(String username)
/*  51:    */   {
/*  52: 50 */     this.id = "";
/*  53: 51 */     this.name = "";
/*  54: 52 */     this.password = "";
/*  55: 53 */     this.username = username;
/*  56:    */     
/*  57: 55 */     this.defaultSmsc = "";
/*  58: 56 */     this.defaultService = "";
/*  59: 57 */     this.connections = new ArrayList();
/*  60: 58 */     this.services = new ArrayList();
/*  61:    */     
/*  62: 60 */     this.logoUrl = "";
/*  63: 61 */     this.defaultLanguage = "";
/*  64:    */     
/*  65: 63 */     this.bandwidthBalance = 0.0D;
/*  66: 64 */     this.inboxBalance = 0.0D;
/*  67: 65 */     this.outboxBalance = 0.0D;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public String resetPassword()
/*  71:    */     throws Exception
/*  72:    */   {
/*  73: 70 */     String newPassword = PasswordGenerator.generatePassword(6);
/*  74: 71 */     UserDB dealerDao = new UserDB();
/*  75:    */     try
/*  76:    */     {
/*  77: 74 */       dealerDao.changePassword(getId(), newPassword);
/*  78:    */     }
/*  79:    */     catch (Exception ex)
/*  80:    */     {
/*  81: 76 */       throw new Exception(ex.getMessage());
/*  82:    */     }
/*  83:    */     finally
/*  84:    */     {
/*  85: 78 */       dealerDao = null;
/*  86:    */     }
/*  87: 81 */     return newPassword;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public Message changePassword(String dealerId, String oldPassword, String newPassword)
/*  91:    */   {
/*  92: 85 */     Message reply = new Message();
/*  93:    */     
/*  94: 87 */     UserDB dealerDao = new UserDB();
/*  95: 88 */     User tmpdealer = null;
/*  96:    */     try
/*  97:    */     {
/*  98: 91 */       tmpdealer = dealerDao.viewDealer(dealerId);
/*  99:    */     }
/* 100:    */     catch (Exception ex1)
/* 101:    */     {
/* 102: 93 */       reply.setStatus(false);
/* 103: 94 */       reply.setMessage(ex1.getMessage());
/* 104: 95 */       return reply;
/* 105:    */     }
/* 106: 98 */     if ((tmpdealer != null) && (oldPassword.equals(tmpdealer.getPassword())))
/* 107:    */     {
/* 108:    */       try
/* 109:    */       {
/* 110:100 */         dealerDao.changePassword(dealerId, newPassword);
/* 111:    */       }
/* 112:    */       catch (Exception ex)
/* 113:    */       {
/* 114:102 */         reply.setStatus(false);
/* 115:103 */         reply.setMessage(ex.getMessage());
/* 116:104 */         return reply;
/* 117:    */       }
/* 118:    */     }
/* 119:    */     else
/* 120:    */     {
/* 121:108 */       reply.setStatus(false);
/* 122:109 */       reply.setMessage("You are not authorized to make this change.please enter a valid password");
/* 123:    */     }
/* 124:113 */     return reply;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public void setId(String id)
/* 128:    */   {
/* 129:117 */     this.id = id;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public String getId()
/* 133:    */   {
/* 134:121 */     return this.id;
/* 135:    */   }
/* 136:    */   
/* 137:    */   public void setName(String name)
/* 138:    */   {
/* 139:125 */     this.name = name;
/* 140:    */   }
/* 141:    */   
/* 142:    */   public String getName()
/* 143:    */   {
/* 144:129 */     return this.name;
/* 145:    */   }
/* 146:    */   
/* 147:    */   public void setPassword(String password)
/* 148:    */   {
/* 149:133 */     this.password = password;
/* 150:    */   }
/* 151:    */   
/* 152:    */   public String getPassword()
/* 153:    */   {
/* 154:137 */     return this.password;
/* 155:    */   }
/* 156:    */   
/* 157:    */   public String getUsername()
/* 158:    */   {
/* 159:141 */     return this.username;
/* 160:    */   }
/* 161:    */   
/* 162:    */   public void setUsername(String username)
/* 163:    */   {
/* 164:145 */     this.username = username;
/* 165:    */   }
/* 166:    */   
/* 167:    */   public void setDefaultSmsc(String defaultSmsc)
/* 168:    */   {
/* 169:149 */     this.defaultSmsc = defaultSmsc;
/* 170:    */   }
/* 171:    */   
/* 172:    */   public String getDefaultSmsc()
/* 173:    */   {
/* 174:153 */     return this.defaultSmsc;
/* 175:    */   }
/* 176:    */   
/* 177:    */   public void setDefaultService(String defaultService)
/* 178:    */   {
/* 179:157 */     this.defaultService = defaultService;
/* 180:    */   }
/* 181:    */   
/* 182:    */   public String getDefaultService()
/* 183:    */   {
/* 184:161 */     return this.defaultService;
/* 185:    */   }
/* 186:    */   
/* 187:    */   public void setConnections(ArrayList connecitons)
/* 188:    */   {
/* 189:165 */     this.connections = connecitons;
/* 190:    */   }
/* 191:    */   
/* 192:    */   public ArrayList getConnections()
/* 193:    */   {
/* 194:169 */     return this.connections;
/* 195:    */   }
/* 196:    */   
/* 197:    */   public void setServices(ArrayList services)
/* 198:    */   {
/* 199:173 */     this.services = services;
/* 200:    */   }
/* 201:    */   
/* 202:    */   public ArrayList getServices()
/* 203:    */   {
/* 204:177 */     return this.services;
/* 205:    */   }
/* 206:    */   
/* 207:    */   public void setLogoUrl(String url)
/* 208:    */   {
/* 209:181 */     this.logoUrl = url;
/* 210:    */   }
/* 211:    */   
/* 212:    */   public String getLogoUrl()
/* 213:    */   {
/* 214:185 */     return this.logoUrl;
/* 215:    */   }
/* 216:    */   
/* 217:    */   public void setDefaultLanguage(String language)
/* 218:    */   {
/* 219:189 */     this.defaultLanguage = language;
/* 220:    */   }
/* 221:    */   
/* 222:    */   public String getDefaultLanguage()
/* 223:    */   {
/* 224:193 */     return this.defaultLanguage;
/* 225:    */   }
/* 226:    */   
/* 227:    */   public double getBandwidthBalance()
/* 228:    */   {
/* 229:197 */     return this.bandwidthBalance;
/* 230:    */   }
/* 231:    */   
/* 232:    */   public void setBandwidthBalance(double bandwidthBalance)
/* 233:    */   {
/* 234:201 */     this.bandwidthBalance = bandwidthBalance;
/* 235:    */   }
/* 236:    */   
/* 237:    */   public double getInboxBalance()
/* 238:    */   {
/* 239:205 */     return this.inboxBalance;
/* 240:    */   }
/* 241:    */   
/* 242:    */   public void setInboxBalance(double inboxBalance)
/* 243:    */   {
/* 244:209 */     this.inboxBalance = inboxBalance;
/* 245:    */   }
/* 246:    */   
/* 247:    */   public double getOutboxBalance()
/* 248:    */   {
/* 249:213 */     return this.outboxBalance;
/* 250:    */   }
/* 251:    */   
/* 252:    */   public void setOutboxBalance(double outboxBalance)
/* 253:    */   {
/* 254:217 */     this.outboxBalance = outboxBalance;
/* 255:    */   }
/* 256:    */   
/* 257:    */   public boolean userExists()
/* 258:    */   {
/* 259:221 */     boolean state = false;
/* 260:    */     
/* 261:223 */     UserDB dealer = new UserDB();
/* 262:224 */     User user = new User();
/* 263:    */     try
/* 264:    */     {
/* 265:227 */       user = dealer.view(getUsername(), getPassword());
/* 266:    */     }
/* 267:    */     catch (Exception ex)
/* 268:    */     {
/* 269:229 */       System.out.println(ex.getMessage());
/* 270:    */     }
/* 271:231 */     if ((user.getId() != null) && (!user.getId().equals("")))
/* 272:    */     {
/* 273:232 */       setId(user.getId());
/* 274:233 */       setName(user.getName());
/* 275:234 */       setPassword(user.getPassword());
/* 276:235 */       setDefaultService(user.getDefaultService());
/* 277:236 */       setUsername(user.getUsername());
/* 278:237 */       setDefaultSmsc(user.getDefaultSmsc());
/* 279:238 */       setLogoUrl(user.getLogoUrl());
/* 280:    */       
/* 281:240 */       state = true;
/* 282:    */     }
/* 283:242 */     return state;
/* 284:    */   }
/* 285:    */   
/* 286:    */   public void createDealer(String name, String id, double accountBalance, String username, String password, double bandwidthBalance, double inboxBalance, double outboxBalance)
/* 287:    */     throws Exception
/* 288:    */   {
/* 289:248 */     new UserDB().createDealer(name, id, accountBalance, username, password, bandwidthBalance, inboxBalance, outboxBalance);
/* 290:    */   }
/* 291:    */   
/* 292:    */   public void updateDealer(String name, String id, double accountBalance, String username, String password)
/* 293:    */     throws Exception
/* 294:    */   {
/* 295:253 */     UserDB dealer = new UserDB();
/* 296:254 */     dealer.updateDealer(name, id, accountBalance, username, password);
/* 297:    */   }
/* 298:    */   
/* 299:    */   public void updateDealer(String name, String username, String password, String defaultSmsc, String defaultService)
/* 300:    */     throws Exception
/* 301:    */   {
/* 302:260 */     UserDB dealer = new UserDB();
/* 303:261 */     dealer.updateDealer(username, name, password, defaultSmsc, defaultService);
/* 304:    */   }
/* 305:    */   
/* 306:    */   public void updateDealer(String name, String username, String defaultSmsc, String defaultService)
/* 307:    */     throws Exception
/* 308:    */   {
/* 309:267 */     UserDB dealer = new UserDB();
/* 310:268 */     dealer.updateDealer(username, name, defaultSmsc, defaultService);
/* 311:    */   }
/* 312:    */   
/* 313:    */   public void updateDealerInfo(String name, String username, String password, String defaultSmsc, String defaultService, String logoUrl)
/* 314:    */     throws Exception
/* 315:    */   {
/* 316:272 */     UserDB dealer = new UserDB();
/* 317:273 */     dealer.updateDealerInfo(username, name, password, defaultSmsc, defaultService, logoUrl);
/* 318:    */   }
/* 319:    */   
/* 320:    */   public void updateDealerInfo(String name, String username, String defaultSmsc, String defaultService, String logoUrl)
/* 321:    */     throws Exception
/* 322:    */   {
/* 323:277 */     UserDB dealer = new UserDB();
/* 324:278 */     dealer.updateDealerInfo(username, name, defaultSmsc, defaultService, logoUrl);
/* 325:    */   }
/* 326:    */   
/* 327:    */   public void updateDealer(String id, double bandwidth, double inbox, double outbox)
/* 328:    */     throws Exception
/* 329:    */   {
/* 330:282 */     UserDB dealer = new UserDB();
/* 331:283 */     dealer.updateDealer(id, bandwidth, inbox, outbox);
/* 332:    */   }
/* 333:    */   
/* 334:    */   public void updateDealer(String username, String language)
/* 335:    */     throws Exception
/* 336:    */   {
/* 337:287 */     UserDB dealer = new UserDB();
/* 338:288 */     dealer.updateDealer(username, language);
/* 339:    */   }
/* 340:    */   
/* 341:    */   public void updateDealer()
/* 342:    */     throws Exception
/* 343:    */   {
/* 344:292 */     UserDB dealer = new UserDB();
/* 345:    */     
/* 346:294 */     dealer.updateDealer(getName(), getId(), 0.0D, getUsername(), getPassword());
/* 347:    */   }
/* 348:    */   
/* 349:    */   public void updateDealerPassWord()
/* 350:    */     throws Exception
/* 351:    */   {
/* 352:300 */     UserDB dealer = new UserDB();
/* 353:301 */     dealer.updateDealer(getName(), getId(), 0.0D, getUsername(), getPassword());
/* 354:    */   }
/* 355:    */   
/* 356:    */   public void deleteDealer(String id)
/* 357:    */     throws Exception
/* 358:    */   {
/* 359:306 */     UserDB dealer = new UserDB();
/* 360:307 */     dealer.deleteDealer(id);
/* 361:    */   }
/* 362:    */   
/* 363:    */   public void deleteDealer(String[] ids)
/* 364:    */     throws Exception
/* 365:    */   {
/* 366:311 */     UserDB dealer = new UserDB();
/* 367:312 */     dealer.deleteDealer(ids);
/* 368:    */   }
/* 369:    */   
/* 370:    */   public User viewDealer(String id)
/* 371:    */     throws Exception
/* 372:    */   {
/* 373:316 */     UserDB dealer = new UserDB();
/* 374:317 */     return dealer.viewDealer(id);
/* 375:    */   }
/* 376:    */   
/* 377:    */   public boolean viewDealerByUserName()
/* 378:    */     throws Exception
/* 379:    */   {
/* 380:321 */     boolean userExists = false;
/* 381:322 */     UserDB dealer = new UserDB();
/* 382:323 */     User tmpDealer = dealer.view(this.username, this.password);
/* 383:324 */     if ((tmpDealer != null) && (tmpDealer.getId() != null))
/* 384:    */     {
/* 385:325 */       userExists = true;
/* 386:    */       
/* 387:    */ 
/* 388:328 */       setId(tmpDealer.getId());
/* 389:329 */       setName(tmpDealer.getName());
/* 390:330 */       setUsername(tmpDealer.getUsername());
/* 391:331 */       setPassword(tmpDealer.getPassword());
/* 392:    */     }
/* 393:334 */     return userExists;
/* 394:    */   }
/* 395:    */   
/* 396:    */   public void viewDealer()
/* 397:    */     throws Exception
/* 398:    */   {
/* 399:338 */     UserDB dealer = new UserDB();
/* 400:339 */     User tmpDealer = dealer.viewDealer(getId());
/* 401:    */     
/* 402:    */ 
/* 403:342 */     setId(tmpDealer.getId());
/* 404:343 */     setName(tmpDealer.getName());
/* 405:344 */     setUsername(tmpDealer.getUsername());
/* 406:345 */     setPassword(tmpDealer.getPassword());
/* 407:346 */     setDefaultService(tmpDealer.getDefaultService());
/* 408:347 */     setDefaultSmsc(tmpDealer.getDefaultSmsc());
/* 409:348 */     setLogoUrl(tmpDealer.logoUrl);
/* 410:349 */     setDefaultLanguage(tmpDealer.defaultLanguage);
/* 411:350 */     setBandwidthBalance(tmpDealer.getBandwidthBalance());
/* 412:351 */     setInboxBalance(tmpDealer.getInboxBalance());
/* 413:352 */     setConnections(tmpDealer.getConnections());
/* 414:353 */     setOutboxBalance(tmpDealer.getOutboxBalance());
/* 415:    */   }
/* 416:    */   
/* 417:    */   public ArrayList getDealers()
/* 418:    */     throws Exception
/* 419:    */   {
/* 420:359 */     UserDB dealer = new UserDB();
/* 421:360 */     return dealer.getDealers();
/* 422:    */   }
/* 423:    */   
/* 424:    */   public ArrayList viewUserWhiteList()
/* 425:    */     throws Exception
/* 426:    */   {
/* 427:364 */     AccessListItemDB accessList = new AccessListItemDB();
/* 428:365 */     return accessList.viewWhiteList(getId());
/* 429:    */   }
/* 430:    */   
/* 431:    */   public void addToUserWhiteList(String ip)
/* 432:    */     throws Exception
/* 433:    */   {
/* 434:370 */     AccessListItemDB accessList = new AccessListItemDB();
/* 435:371 */     accessList.SaveToWhiteList(ip, getId());
/* 436:    */   }
/* 437:    */   
/* 438:    */   public void addToUserBlackList(String ip)
/* 439:    */     throws Exception
/* 440:    */   {
/* 441:375 */     AccessListItemDB accessList = new AccessListItemDB();
/* 442:376 */     accessList.SaveToBlackList(ip, getId());
/* 443:    */   }
/* 444:    */   
/* 445:    */   public void removeFromUserBlackList(String ip)
/* 446:    */     throws Exception
/* 447:    */   {
/* 448:380 */     AccessListItemDB accessList = new AccessListItemDB();
/* 449:381 */     accessList.RemoveFromBlackList(ip);
/* 450:    */   }
/* 451:    */   
/* 452:    */   public void removeFromUserWhiteList(String ip)
/* 453:    */     throws Exception
/* 454:    */   {
/* 455:385 */     AccessListItemDB accessList = new AccessListItemDB();
/* 456:386 */     accessList.RemoveFromWhiteList(ip);
/* 457:    */   }
/* 458:    */   
/* 459:    */   public ArrayList viewUserBlackList()
/* 460:    */     throws Exception
/* 461:    */   {
/* 462:390 */     AccessListItemDB accessList = new AccessListItemDB();
/* 463:391 */     return accessList.viewBlackList(getId());
/* 464:    */   }
/* 465:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.contentprovider.User
 * JD-Core Version:    0.7.0.1
 */