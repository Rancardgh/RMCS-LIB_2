/*   1:    */ package com.rancard.util;
/*   2:    */ 
/*   3:    */ import com.rancard.common.Config;
/*   4:    */ import com.rancard.common.Feedback;
/*   5:    */ import com.rancard.mobility.common.FonCapabilityMtrx;
/*   6:    */ import com.rancard.mobility.contentserver.ContentType;
/*   7:    */ import com.rancard.mobility.infoserver.feeds.FeedJob;
/*   8:    */ import com.rancard.mobility.infoserver.livescore.LiveScoreTriggerSetup;
/*   9:    */ import com.rancard.util.payment.PaymentManager;
/*  10:    */ import java.io.IOException;
/*  11:    */ import java.io.PrintStream;
/*  12:    */ import java.io.PrintWriter;
/*  13:    */ import java.util.Calendar;
/*  14:    */ import java.util.Date;
/*  15:    */ import java.util.HashMap;
/*  16:    */ import java.util.List;
/*  17:    */ import java.util.Properties;
/*  18:    */ import javax.naming.Context;
/*  19:    */ import javax.naming.InitialContext;
/*  20:    */ import javax.naming.NamingException;
/*  21:    */ import javax.servlet.ServletConfig;
/*  22:    */ import javax.servlet.ServletContext;
/*  23:    */ import javax.servlet.ServletException;
/*  24:    */ import javax.servlet.http.HttpServlet;
/*  25:    */ import javax.servlet.http.HttpServletRequest;
/*  26:    */ import javax.servlet.http.HttpServletResponse;
/*  27:    */ import org.quartz.CronTrigger;
/*  28:    */ import org.quartz.JobDataMap;
/*  29:    */ import org.quartz.JobDetail;
/*  30:    */ import org.quartz.Scheduler;
/*  31:    */ import org.quartz.SchedulerException;
/*  32:    */ import org.quartz.impl.StdSchedulerFactory;
/*  33:    */ 
/*  34:    */ public class serverinit
/*  35:    */   extends HttpServlet
/*  36:    */ {
/*  37: 34 */   PrintWriter out = null;
/*  38: 35 */   FonCapabilityMtrx fcm = null;
/*  39: 36 */   HashMap routingTable = null;
/*  40: 37 */   List types = null;
/*  41: 38 */   HashMap prices = null;
/*  42: 39 */   Scheduler scheduler = null;
/*  43: 40 */   Scheduler feedScheduler = null;
/*  44: 42 */   Feedback feedback_en = null;
/*  45: 43 */   Feedback feedback_fr = null;
/*  46:    */   
/*  47:    */   public void init(ServletConfig config)
/*  48:    */     throws ServletException
/*  49:    */   {
/*  50: 47 */     super.init(config);
/*  51:    */     
/*  52:    */ 
/*  53: 50 */     System.out.println(new Date() + ":@com.rancard.util.serverint");
/*  54:    */     
/*  55: 52 */     String url = config.getServletContext().getInitParameter("contentServerUrl");
/*  56: 53 */     String feedUrl = config.getServletContext().getInitParameter("feedServerUrl");
/*  57:    */     try
/*  58:    */     {
/*  59: 55 */       this.fcm = new FonCapabilityMtrx();
/*  60: 56 */       System.out.println(new Date() + ":Capabilities Matrix initialized");
/*  61:    */     }
/*  62:    */     catch (Exception e)
/*  63:    */     {
/*  64: 58 */       System.out.println(new Date() + ":Could not initialize capabilities matrix.");
/*  65:    */     }
/*  66:    */     try
/*  67:    */     {
/*  68: 61 */       this.types = ContentType.getDistinctTypes(1);
/*  69: 62 */       System.out.println(new Date() + ":Service Types initialized");
/*  70:    */     }
/*  71:    */     catch (Exception e)
/*  72:    */     {
/*  73: 64 */       System.out.println(new Date() + ":Could not initialize service types.");
/*  74:    */     }
/*  75:    */     try
/*  76:    */     {
/*  77: 68 */       this.prices = PaymentManager.viewPricePoints_hm();
/*  78: 69 */       System.out.println(new Date() + ":Pricing Matrix initialized");
/*  79:    */     }
/*  80:    */     catch (Exception e)
/*  81:    */     {
/*  82: 71 */       System.out.println(new Date() + ":Could not initialize pricing matrix.");
/*  83:    */     }
/*  84:    */     try
/*  85:    */     {
/*  86: 88 */       this.feedback_en = new Feedback("en");
/*  87: 89 */       System.out.println(new Date() + ":English feedback initialized");
/*  88:    */     }
/*  89:    */     catch (Exception e)
/*  90:    */     {
/*  91: 91 */       System.out.println(new Date() + ":Could not initialize English feedback." + e.getMessage());
/*  92:    */     }
/*  93:    */     try
/*  94:    */     {
/*  95: 94 */       this.feedback_fr = new Feedback("fr");
/*  96: 95 */       System.out.println(new Date() + ":French feedback initialized");
/*  97:    */     }
/*  98:    */     catch (Exception e)
/*  99:    */     {
/* 100: 97 */       System.out.println(new Date() + ":Could not initialize French feedback." + e.getMessage());
/* 101:    */     }
/* 102:157 */     config.getServletContext().setAttribute("capabilitiesMtrx", this.fcm);
/* 103:158 */     config.getServletContext().setAttribute("serviceTypes", this.types);
/* 104:159 */     config.getServletContext().setAttribute("pricingMatrix", this.prices);
/* 105:    */     
/* 106:    */ 
/* 107:    */ 
/* 108:    */ 
/* 109:    */ 
/* 110:165 */     config.getServletContext().setAttribute("scheduler", this.scheduler);
/* 111:166 */     config.getServletContext().setAttribute("feedback_en", this.feedback_en);
/* 112:167 */     config.getServletContext().setAttribute("feedback_fr", this.feedback_fr);
/* 113:    */   }
/* 114:    */   
/* 115:    */   public void doGet(HttpServletRequest request, HttpServletResponse response)
/* 116:    */     throws ServletException, IOException
/* 117:    */   {}
/* 118:    */   
/* 119:    */   public void doPost(HttpServletRequest request, HttpServletResponse response)
/* 120:    */     throws ServletException, IOException
/* 121:    */   {
/* 122:177 */     doGet(request, response);
/* 123:    */   }
/* 124:    */   
/* 125:    */   public void destroy() {}
/* 126:    */   
/* 127:    */   private Scheduler initScheduler(String url)
/* 128:    */     throws Exception
/* 129:    */   {
/* 130:    */     try
/* 131:    */     {
/* 132:248 */       StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();
/* 133:    */       
/* 134:    */ 
/* 135:251 */       Properties quartzProps = getQuartzPropertiesPath();
/* 136:    */       
/* 137:    */ 
/* 138:254 */       schedulerFactory.initialize(quartzProps);
/* 139:    */       
/* 140:    */ 
/* 141:257 */       Scheduler scheduler = null;
/* 142:    */       try
/* 143:    */       {
/* 144:259 */         scheduler = schedulerFactory.getScheduler();
/* 145:    */       }
/* 146:    */       catch (Exception e)
/* 147:    */       {
/* 148:261 */         System.out.println(e.getMessage());
/* 149:    */       }
/* 150:265 */       Calendar c = Calendar.getInstance();
/* 151:266 */       int mins = 0;
/* 152:    */       try
/* 153:    */       {
/* 154:268 */         String minite = PropertyHolder.getPropsValue("LS_SCHED_START_AFTER");
/* 155:269 */         if ((minite != null) && (!minite.equals(""))) {
/* 156:270 */           mins = Integer.parseInt(minite);
/* 157:    */         } else {
/* 158:272 */           mins = 5;
/* 159:    */         }
/* 160:    */       }
/* 161:    */       catch (Exception e)
/* 162:    */       {
/* 163:275 */         mins = 5;
/* 164:    */       }
/* 165:278 */       c.add(12, mins);
/* 166:    */       
/* 167:280 */       String jobName = "livescore_job";
/* 168:281 */       String jobGroup = "livescore_job_group";
/* 169:282 */       String triggerName = "livescore_trigger";
/* 170:283 */       String triggerGroup = "livescore_trigger_group";
/* 171:    */       
/* 172:    */ 
/* 173:286 */       JobDetail jobDetail = new JobDetail(jobName, jobGroup, LiveScoreTriggerSetup.class);
/* 174:287 */       JobDataMap dataMap = new JobDataMap();
/* 175:288 */       dataMap.put("url", url);
/* 176:289 */       jobDetail.setJobDataMap(dataMap);
/* 177:    */       
/* 178:291 */       CronTrigger cronTrigger = new CronTrigger(triggerName, triggerGroup);
/* 179:    */       try
/* 180:    */       {
/* 181:294 */         String cexp = new String("0 " + c.get(12) + " " + c.get(11) + " * * ? *");
/* 182:    */         
/* 183:296 */         cronTrigger.setCronExpression(cexp);
/* 184:    */       }
/* 185:    */       catch (Exception e) {}
/* 186:302 */       JobDetail tempJobDetail = scheduler.getJobDetail(jobName, jobGroup);
/* 187:303 */       CronTrigger tempCronTrigger = (CronTrigger)scheduler.getTrigger(triggerName, triggerGroup);
/* 188:305 */       if ((tempJobDetail != null) && ((tempJobDetail.getName() != null) || (!tempJobDetail.getName().equals(""))))
/* 189:    */       {
/* 190:308 */         System.out.println("Scheduler with the following description is already initialized:\r\nJobName: " + tempJobDetail.getName() + " | JobGroup: " + tempJobDetail.getGroup() + "  | TriggerName: " + tempCronTrigger.getName() + " | TriggerGroup: " + tempCronTrigger.getGroup());
/* 191:    */       }
/* 192:    */       else
/* 193:    */       {
/* 194:311 */         scheduler.deleteJob(jobName, jobGroup);
/* 195:312 */         scheduler.scheduleJob(jobDetail, cronTrigger);
/* 196:    */       }
/* 197:316 */       scheduler.start();
/* 198:    */       
/* 199:318 */       return scheduler;
/* 200:    */     }
/* 201:    */     catch (SchedulerException se)
/* 202:    */     {
/* 203:321 */       System.out.println("Scheduler Initialization Exception: " + se.getMessage());
/* 204:322 */       throw new Exception();
/* 205:    */     }
/* 206:    */   }
/* 207:    */   
/* 208:    */   private Scheduler initFeedScheduler(String url)
/* 209:    */     throws Exception
/* 210:    */   {
/* 211:    */     try
/* 212:    */     {
/* 213:332 */       System.out.println(new Date() + ":Initiating a Schedule Factory for FeedScheduler...");
/* 214:333 */       StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();
/* 215:    */       
/* 216:335 */       System.out.println(new Date() + ":Schedule Factory for FeedScheduler initiated!");
/* 217:    */       
/* 218:    */ 
/* 219:338 */       System.out.println(new Date() + ":retrieving PropertiesPath for FeedScheduler...");
/* 220:    */       
/* 221:340 */       Properties quartzFeedsProps = getQuartzFeedsPropertiesPath();
/* 222:    */       
/* 223:    */ 
/* 224:    */ 
/* 225:344 */       System.out.println(new Date() + ":initializing schedulerFactory with properties file:" + quartzFeedsProps + "...");
/* 226:345 */       schedulerFactory.initialize(quartzFeedsProps);
/* 227:346 */       System.out.println(new Date() + ":schedulerFactory successfully inititialized with properties file!");
/* 228:    */       
/* 229:    */ 
/* 230:349 */       Scheduler scheduler = null;
/* 231:    */       try
/* 232:    */       {
/* 233:351 */         System.out.println(new Date() + ":Retrieving a Scheduler from schedule factory...");
/* 234:352 */         scheduler = schedulerFactory.getScheduler();
/* 235:353 */         System.out.println(new Date() + ":Scheduler successfully retrieved from schedule factory!");
/* 236:    */       }
/* 237:    */       catch (Exception e)
/* 238:    */       {
/* 239:356 */         System.out.println(new Date() + ":error retrieving Sheduler from shedule factory:" + e.getMessage());
/* 240:    */       }
/* 241:359 */       String jobName = "infoFeeds_job";
/* 242:360 */       String jobGroup = "infoFeeds_job_group";
/* 243:361 */       String triggerName = "infoFeeds_trigger";
/* 244:362 */       String triggerGroup = "infoFeeds_trigger_group";
/* 245:    */       
/* 246:    */ 
/* 247:365 */       System.out.println(new Date() + ":Initiate JobDetail with job name (" + jobName + "), job group (" + jobGroup + ")" + ", and executable job class (com.rancard.mobility.infoserver.feeds.FeedJob)");
/* 248:    */       
/* 249:    */ 
/* 250:368 */       JobDetail jobDetail = new JobDetail(jobName, jobGroup, FeedJob.class);
/* 251:    */       
/* 252:370 */       System.out.println(new Date() + ":Populating JobDataMap...");
/* 253:371 */       System.out.println(new Date() + ":JobData1: URL=" + url);
/* 254:    */       
/* 255:373 */       JobDataMap dataMap = new JobDataMap();
/* 256:374 */       dataMap.put("url", url);
/* 257:375 */       jobDetail.setJobDataMap(dataMap);
/* 258:    */       
/* 259:377 */       System.out.println(new Date() + ":JobDataMap pupulated!");
/* 260:    */       
/* 261:    */ 
/* 262:380 */       System.out.println(new Date() + ":Initiating CronTrigger with its name (" + triggerName + ")" + "and group name (" + triggerGroup + ")...");
/* 263:    */       
/* 264:    */ 
/* 265:383 */       CronTrigger cronTrigger = new CronTrigger(triggerName, triggerGroup);
/* 266:    */       try
/* 267:    */       {
/* 268:386 */         System.out.println(new Date() + ":seting up CronExpression...");
/* 269:387 */         String cexp = new String("0 0/2 * * * ?");
/* 270:    */         
/* 271:    */ 
/* 272:390 */         System.out.println(new Date() + ":Assigning the CronExpression (" + cexp + ")to CronTrigger...");
/* 273:391 */         cronTrigger.setCronExpression(cexp);
/* 274:    */         
/* 275:393 */         System.out.println(new Date() + ":CronExpression (" + cexp + ") Assigned!");
/* 276:    */       }
/* 277:    */       catch (Exception e)
/* 278:    */       {
/* 279:397 */         System.out.println(new Date() + ": error setting up CronExpression:" + e.getMessage());
/* 280:    */       }
/* 281:401 */       System.out.println(new Date() + ": scheduling job with JobDetail (" + jobName + "|" + jobGroup + ") and Trigger (" + triggerName + "|" + triggerGroup);
/* 282:    */       
/* 283:403 */       JobDetail tempJobDetail = scheduler.getJobDetail(jobName, jobGroup);
/* 284:404 */       CronTrigger tempCronTrigger = (CronTrigger)scheduler.getTrigger(triggerName, triggerGroup);
/* 285:406 */       if ((tempJobDetail != null) && ((tempJobDetail.getName() != null) || (!tempJobDetail.getName().equals(""))))
/* 286:    */       {
/* 287:409 */         System.out.println(new Date() + ":Scheduler with the following description is already initialized:\r\nJobName: " + tempJobDetail.getName() + " | JobGroup: " + tempJobDetail.getGroup() + "  | TriggerName: " + tempCronTrigger.getName() + " | TriggerGroup: " + tempCronTrigger.getGroup());
/* 288:    */       }
/* 289:    */       else
/* 290:    */       {
/* 291:413 */         scheduler.deleteJob(jobName, jobGroup);
/* 292:414 */         scheduler.scheduleJob(jobDetail, cronTrigger);
/* 293:    */       }
/* 294:418 */       System.out.println(new Date() + ": Starting FeedScheduler...");
/* 295:    */       
/* 296:420 */       scheduler.start();
/* 297:    */       
/* 298:422 */       return scheduler;
/* 299:    */     }
/* 300:    */     catch (SchedulerException se)
/* 301:    */     {
/* 302:425 */       System.out.println(new Date() + ":Feeds Scheduler Initialization Exception: " + se.getMessage());
/* 303:426 */       throw new Exception();
/* 304:    */     }
/* 305:    */   }
/* 306:    */   
/* 307:    */   public Properties getQuartzPropertiesPath()
/* 308:    */     throws Exception
/* 309:    */   {
/* 310:432 */     Properties path = new Properties();
/* 311:433 */     String value = "";
/* 312:    */     try
/* 313:    */     {
/* 314:435 */       Context ctx = new InitialContext();
/* 315:436 */       Config appConfig = (Config)ctx.lookup("java:comp/env/bean/quartzConfig");
/* 316:438 */       if (appConfig != null) {
/* 317:439 */         path = appConfig.load();
/* 318:    */       }
/* 319:    */     }
/* 320:    */     catch (NamingException e)
/* 321:    */     {
/* 322:444 */       throw new Exception(e.getMessage());
/* 323:    */     }
/* 324:447 */     return path;
/* 325:    */   }
/* 326:    */   
/* 327:    */   public Properties getQuartzFeedsPropertiesPath()
/* 328:    */     throws Exception
/* 329:    */   {
/* 330:453 */     Properties path = new Properties();
/* 331:454 */     String value = "";
/* 332:    */     try
/* 333:    */     {
/* 334:456 */       Context ctx = new InitialContext();
/* 335:457 */       Config appConfig = (Config)ctx.lookup("java:comp/env/bean/quartzFeedsConfig");
/* 336:459 */       if (appConfig != null) {
/* 337:460 */         path = appConfig.load();
/* 338:    */       }
/* 339:    */     }
/* 340:    */     catch (NamingException e)
/* 341:    */     {
/* 342:465 */       throw new Exception(e.getMessage());
/* 343:    */     }
/* 344:468 */     return path;
/* 345:    */   }
/* 346:    */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.util.serverinit
 * JD-Core Version:    0.7.0.1
 */