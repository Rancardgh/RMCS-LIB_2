/*  1:   */ package com.rancard.util;
/*  2:   */ 
/*  3:   */ import com.rancard.mobility.infoserver.livescore.LiveScoreTriggerSetup;
/*  4:   */ import java.util.Date;
/*  5:   */ import org.quartz.JobDetail;
/*  6:   */ import org.quartz.Scheduler;
/*  7:   */ import org.quartz.SchedulerException;
/*  8:   */ import org.quartz.Trigger;
/*  9:   */ import org.quartz.helpers.TriggerUtils;
/* 10:   */ import org.quartz.impl.StdSchedulerFactory;
/* 11:   */ 
/* 12:   */ public class QuartzTest
/* 13:   */ {
/* 14:   */   public static void main(String[] args)
/* 15:   */   {
/* 16:   */     try
/* 17:   */     {
/* 18:26 */       Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
/* 19:   */       
/* 20:   */ 
/* 21:29 */       scheduler.start();
/* 22:   */       
/* 23:31 */       JobDetail jobDetail = new JobDetail("myJob", "group1", LiveScoreTriggerSetup.class);
/* 24:   */       
/* 25:33 */       Trigger trigger = TriggerUtils.makeSecondlyTrigger();
/* 26:34 */       trigger.setStartTime(TriggerUtils.getEvenSecondDate(new Date()));
/* 27:35 */       trigger.setName("livescore_fixtures");
/* 28:36 */       trigger.setGroup("group1");
/* 29:   */       
/* 30:38 */       scheduler.scheduleJob(jobDetail, trigger);
/* 31:   */       try
/* 32:   */       {
/* 33:42 */         Thread.sleep(30000L);
/* 34:   */       }
/* 35:   */       catch (Exception e) {}
/* 36:47 */       scheduler.shutdown();
/* 37:   */     }
/* 38:   */     catch (SchedulerException se)
/* 39:   */     {
/* 40:50 */       se.printStackTrace();
/* 41:   */     }
/* 42:   */   }
/* 43:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.util.QuartzTest
 * JD-Core Version:    0.7.0.1
 */