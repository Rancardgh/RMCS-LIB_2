 package com.rancard.mobility.infoserver.feeds;
 
 import java.io.PrintStream;
 import java.sql.Timestamp;
 import java.util.Date;
 import org.quartz.Job;
 import org.quartz.JobExecutionContext;
 import org.quartz.JobExecutionException;
 
 public class FeedJob
   implements Job
 {
   public void execute(JobExecutionContext context)
     throws JobExecutionException
   {
     System.out.println("TriggerSetup is executing for FeedReader on " + new Timestamp(new Date().getTime()).toString());
     System.out.println(FeedReader.getFeedUpdates());
   }
 }

