/*   1:    */ package com.rancard.mobility.infoserver.livescore.serviceinterfaces;
/*   2:    */ 
/*   3:    */ import com.rancard.mobility.infoserver.livescore.LiveScoreQuartzHelper;
/*   4:    */ import com.rancard.mobility.infoserver.livescore.LiveScoreServiceManager;
/*   5:    */ import com.rancard.mobility.infoserver.livescore.LiveScoreSubscriptionHelper;
/*   6:    */ import com.rancard.util.PropertyHolder;
/*   7:    */ import java.io.IOException;
/*   8:    */ import java.io.PrintStream;
/*   9:    */ import java.io.PrintWriter;
/*  10:    */ import java.sql.Timestamp;
/*  11:    */ import java.util.ArrayList;
/*  12:    */ import java.util.Calendar;
/*  13:    */ import javax.servlet.RequestDispatcher;
/*  14:    */ import javax.servlet.ServletConfig;
/*  15:    */ import javax.servlet.ServletContext;
/*  16:    */ import javax.servlet.ServletException;
/*  17:    */ import javax.servlet.ServletRequest;
/*  18:    */ import javax.servlet.ServletResponse;
/*  19:    */ import javax.servlet.http.HttpServlet;
/*  20:    */ import javax.servlet.http.HttpServletRequest;
/*  21:    */ import javax.servlet.http.HttpServletResponse;
/*  22:    */ import org.quartz.JobDataMap;
/*  23:    */ import org.quartz.JobDetail;
/*  24:    */ import org.quartz.Scheduler;
/*  25:    */ import org.quartz.SimpleTrigger;
/*  26:    */ 
/*  27:    */ public class setuptriggers
/*  28:    */   extends HttpServlet
/*  29:    */   implements RequestDispatcher
/*  30:    */ {
/*  31:    */   public void init()
/*  32:    */     throws ServletException
/*  33:    */   {}
/*  34:    */   
/*  35:    */   public void doGet(HttpServletRequest request, HttpServletResponse response)
/*  36:    */     throws ServletException, IOException
/*  37:    */   {
/*  38: 46 */     int DEFAULT_ALLOWANCE = 0;
/*  39: 47 */     int OUTLOOK_PERIOD = 0;
/*  40: 48 */     int REREG_START_HOUR_OF_DAY = 0;
/*  41:    */     
/*  42:    */ 
/*  43: 51 */     System.out.println(new java.util.Date() + "@com.rancard.mobility.infoserver.livescore.serviceinterfaces");
/*  44:    */     try
/*  45:    */     {
/*  46: 55 */       String value = PropertyHolder.getPropsValue("LS_TRIGGER_ALLOWANCE");
/*  47: 56 */       if ((value != null) && (!value.equals(""))) {
/*  48: 57 */         DEFAULT_ALLOWANCE = Integer.parseInt(value);
/*  49:    */       } else {
/*  50: 59 */         DEFAULT_ALLOWANCE = 6;
/*  51:    */       }
/*  52:    */     }
/*  53:    */     catch (Exception e)
/*  54:    */     {
/*  55: 62 */       DEFAULT_ALLOWANCE = 6;
/*  56:    */     }
/*  57:    */     try
/*  58:    */     {
/*  59: 66 */       String value = PropertyHolder.getPropsValue("LS_GAME_OUTLOOK");
/*  60: 67 */       if ((value != null) && (!value.equals(""))) {
/*  61: 68 */         OUTLOOK_PERIOD = Integer.parseInt(value);
/*  62:    */       } else {
/*  63: 70 */         OUTLOOK_PERIOD = 24;
/*  64:    */       }
/*  65:    */     }
/*  66:    */     catch (Exception e)
/*  67:    */     {
/*  68: 73 */       OUTLOOK_PERIOD = 24;
/*  69:    */     }
/*  70:    */     try
/*  71:    */     {
/*  72: 78 */       String value = PropertyHolder.getPropsValue("REREG_TRIGGER_START_HOUR_OF_DAY");
/*  73: 79 */       if ((value != null) && (!value.equals(""))) {
/*  74: 80 */         REREG_START_HOUR_OF_DAY = Integer.parseInt(value);
/*  75:    */       } else {
/*  76: 82 */         REREG_START_HOUR_OF_DAY = 7;
/*  77:    */       }
/*  78:    */     }
/*  79:    */     catch (Exception e)
/*  80:    */     {
/*  81: 85 */       REREG_START_HOUR_OF_DAY = 7;
/*  82:    */     }
/*  83: 89 */     PrintWriter out = response.getWriter();
/*  84: 90 */     Calendar calendar = Calendar.getInstance();
/*  85: 91 */     java.sql.Date today = new java.sql.Date(calendar.getTime().getTime());
/*  86:    */     
/*  87: 93 */     Scheduler scheduler = null;
/*  88: 94 */     String url = getServletContext().getInitParameter("contentServerUrl");
/*  89: 95 */     String todayString = new Timestamp(new java.util.Date().getTime()).toString();
/*  90:    */     try
/*  91:    */     {
/*  92: 98 */       scheduler = (Scheduler)getServletContext().getAttribute("scheduler");
/*  93: 99 */       if (scheduler != null)
/*  94:    */       {
/*  95:100 */         System.out.println(new java.util.Date() + ":Setting up triggers on " + todayString);
/*  96:101 */         System.out.println(new java.util.Date() + ":Looking for game times...");
/*  97:102 */         ArrayList dates = LiveScoreServiceManager.viewDistinctGameTimesForDate(today, 0);
/*  98:103 */         String message = "";
/*  99:105 */         if (dates.size() > 0)
/* 100:    */         {
/* 101:106 */           System.out.println(new java.util.Date() + ":Found " + dates.size() + " game time(s) for.");
/* 102:107 */           for (int i = 0; i < dates.size(); i++)
/* 103:    */           {
/* 104:109 */             String dateString = (String)dates.get(i);
/* 105:110 */             System.out.println(new java.util.Date() + ":Game time " + (i + 1) + ": " + dateString);
/* 106:111 */             Timestamp gameTime = Timestamp.valueOf(dateString);
/* 107:112 */             Calendar cal = Calendar.getInstance();
/* 108:    */             
/* 109:114 */             cal.setTime(new java.util.Date(gameTime.getTime()));
/* 110:    */             
/* 111:116 */             cal.add(11, -DEFAULT_ALLOWANCE);
/* 112:    */             
/* 113:118 */             Calendar now = Calendar.getInstance();
/* 114:    */             
/* 115:120 */             String allowance = "";
/* 116:121 */             boolean createTrigger = false;
/* 117:122 */             if (cal.before(now))
/* 118:    */             {
/* 119:123 */               int halfOfDefaultAllowance = DEFAULT_ALLOWANCE / 2;
/* 120:124 */               cal.setTime(new java.util.Date(gameTime.getTime()));
/* 121:125 */               cal.add(11, -halfOfDefaultAllowance);
/* 122:    */               
/* 123:127 */               allowance = "";
/* 124:128 */               if (cal.before(now))
/* 125:    */               {
/* 126:129 */                 createTrigger = false;
/* 127:    */               }
/* 128:    */               else
/* 129:    */               {
/* 130:131 */                 allowance = "" + halfOfDefaultAllowance;
/* 131:132 */                 createTrigger = true;
/* 132:    */               }
/* 133:    */             }
/* 134:    */             else
/* 135:    */             {
/* 136:135 */               allowance = "" + DEFAULT_ALLOWANCE;
/* 137:136 */               createTrigger = true;
/* 138:    */             }
/* 139:139 */             if (createTrigger)
/* 140:    */             {
/* 141:140 */               System.out.println(new java.util.Date() + ":Trigger time " + (i + 1) + ": " + new Timestamp(cal.getTimeInMillis()).toString());
/* 142:    */               
/* 143:    */ 
/* 144:143 */               String jobName = new String("livescore_fixture_today_" + (i + 1));
/* 145:144 */               String groupName = new String("todays_games_" + (i + 1));
/* 146:145 */               SimpleTrigger trigger = new SimpleTrigger(jobName, groupName, cal.getTime());
/* 147:146 */               JobDetail jobDetail = new JobDetail(jobName, groupName, LiveScoreQuartzHelper.class);
/* 148:    */               
/* 149:148 */               JobDataMap dataMap = new JobDataMap();
/* 150:149 */               dataMap.put("allowance", allowance);
/* 151:150 */               dataMap.put("url", url);
/* 152:151 */               jobDetail.setJobDataMap(dataMap);
/* 153:    */               
/* 154:153 */               JobDetail temp = scheduler.getJobDetail(jobName, groupName);
/* 155:154 */               if ((temp != null) && ((temp.getName() != null) || (!temp.getName().equals("")))) {
/* 156:155 */                 scheduler.deleteJob(jobName, groupName);
/* 157:    */               }
/* 158:159 */               scheduler.scheduleJob(jobDetail, trigger);
/* 159:160 */               getServletConfig().getServletContext().setAttribute("scheduler", scheduler);
/* 160:    */             }
/* 161:    */           }
/* 162:    */         }
/* 163:    */         else
/* 164:    */         {
/* 165:164 */           System.out.println(new java.util.Date() + ":No game times found");
/* 166:    */         }
/* 167:168 */         Calendar now = Calendar.getInstance();
/* 168:169 */         Calendar preferredTriggerTime = Calendar.getInstance();
/* 169:170 */         Calendar triggerTime = Calendar.getInstance();
/* 170:    */         
/* 171:172 */         preferredTriggerTime.set(11, REREG_START_HOUR_OF_DAY);
/* 172:173 */         preferredTriggerTime.set(12, 0);
/* 173:174 */         preferredTriggerTime.set(13, 0);
/* 174:175 */         preferredTriggerTime.set(14, 0);
/* 175:177 */         if (now.before(preferredTriggerTime))
/* 176:    */         {
/* 177:178 */           triggerTime = preferredTriggerTime;
/* 178:    */         }
/* 179:    */         else
/* 180:    */         {
/* 181:180 */           now.add(12, 5);
/* 182:181 */           triggerTime = now;
/* 183:    */         }
/* 184:187 */         System.out.println(new java.util.Date() + ":Trigger time for re-subscription process: " + new Timestamp(triggerTime.getTimeInMillis()).toString());
/* 185:    */         
/* 186:    */ 
/* 187:190 */         String jobName = new String("livescore_re-subscription_today");
/* 188:191 */         String groupName = new String("livescore_re-subscription");
/* 189:192 */         SimpleTrigger trigger = new SimpleTrigger(jobName, groupName, triggerTime.getTime());
/* 190:193 */         JobDetail jobDetail = new JobDetail(jobName, groupName, LiveScoreSubscriptionHelper.class);
/* 191:    */         
/* 192:195 */         JobDataMap dataMap = new JobDataMap();
/* 193:196 */         dataMap.put("url", url);
/* 194:197 */         jobDetail.setJobDataMap(dataMap);
/* 195:    */         
/* 196:199 */         JobDetail temp = scheduler.getJobDetail(jobName, groupName);
/* 197:200 */         if ((temp != null) && ((temp.getName() != null) || (!temp.getName().equals("")))) {
/* 198:201 */           scheduler.deleteJob(jobName, groupName);
/* 199:    */         }
/* 200:205 */         scheduler.scheduleJob(jobDetail, trigger);
/* 201:206 */         getServletConfig().getServletContext().setAttribute("scheduler", scheduler);
/* 202:    */       }
/* 203:    */       else
/* 204:    */       {
/* 205:208 */         System.out.println("scheduler is null");
/* 206:    */       }
/* 207:    */     }
/* 208:    */     catch (Exception e)
/* 209:    */     {
/* 210:210 */       e = 
/* 211:    */       
/* 212:    */ 
/* 213:213 */         e;System.out.println(new java.util.Date() + "error setting up LiveScore triggers:" + e.getMessage());
/* 214:    */     }
/* 215:    */     finally {}
/* 216:    */   }
/* 217:    */   
/* 218:    */   public void doPost(HttpServletRequest request, HttpServletResponse response)
/* 219:    */     throws ServletException, IOException
/* 220:    */   {
/* 221:220 */     doGet(request, response);
/* 222:    */   }
/* 223:    */   
/* 224:    */   public void destroy() {}
/* 225:    */   
/* 226:    */   public void forward(ServletRequest request, ServletResponse response)
/* 227:    */     throws ServletException, IOException
/* 228:    */   {
/* 229:229 */     HttpServletRequest req = (HttpServletRequest)request;
/* 230:230 */     HttpServletResponse resp = (HttpServletResponse)response;
/* 231:231 */     doGet(req, resp);
/* 232:    */   }
/* 233:    */   
/* 234:    */   public void include(ServletRequest request, ServletResponse response)
/* 235:    */     throws ServletException, IOException
/* 236:    */   {
/* 237:236 */     HttpServletRequest req = (HttpServletRequest)request;
/* 238:237 */     HttpServletResponse resp = (HttpServletResponse)response;
/* 239:238 */     doGet(req, resp);
/* 240:    */   }
/* 241:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.livescore.serviceinterfaces.setuptriggers
 * JD-Core Version:    0.7.0.1
 */