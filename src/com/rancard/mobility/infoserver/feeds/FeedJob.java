/*  1:   */ package com.rancard.mobility.infoserver.feeds;
/*  2:   */ 
/*  3:   */ import java.io.PrintStream;
/*  4:   */ import java.sql.Timestamp;
/*  5:   */ import java.util.Date;
/*  6:   */ import org.quartz.Job;
/*  7:   */ import org.quartz.JobExecutionContext;
/*  8:   */ import org.quartz.JobExecutionException;
/*  9:   */ 
/* 10:   */ public class FeedJob
/* 11:   */   implements Job
/* 12:   */ {
/* 13:   */   public void execute(JobExecutionContext context)
/* 14:   */     throws JobExecutionException
/* 15:   */   {
/* 16:25 */     System.out.println("TriggerSetup is executing for FeedReader on " + new Timestamp(new Date().getTime()).toString());
/* 17:26 */     System.out.println(FeedReader.getFeedUpdates());
/* 18:   */   }
/* 19:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.feeds.FeedJob
 * JD-Core Version:    0.7.0.1
 */