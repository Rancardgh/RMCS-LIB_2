/*  1:   */ package com.rancard.mobility.infoserver.livescore;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.io.PrintStream;
/*  5:   */ import java.sql.Timestamp;
/*  6:   */ import java.util.Date;
/*  7:   */ import org.apache.commons.httpclient.HttpClient;
/*  8:   */ import org.apache.commons.httpclient.HttpException;
/*  9:   */ import org.apache.commons.httpclient.methods.GetMethod;
/* 10:   */ import org.quartz.Job;
/* 11:   */ import org.quartz.JobDataMap;
/* 12:   */ import org.quartz.JobDetail;
/* 13:   */ import org.quartz.JobExecutionContext;
/* 14:   */ import org.quartz.JobExecutionException;
/* 15:   */ 
/* 16:   */ public class LiveScoreTriggerSetup
/* 17:   */   implements Job
/* 18:   */ {
/* 19:   */   public void execute(JobExecutionContext context)
/* 20:   */     throws JobExecutionException
/* 21:   */   {
/* 22:30 */     System.out.println("TriggerSetup is executing on " + new Timestamp(new Date().getTime()).toString());
/* 23:31 */     String url = context.getJobDetail().getJobDataMap().getString("url");
/* 24:32 */     HttpClient client = new HttpClient();
/* 25:33 */     GetMethod httpGETFORM = new GetMethod(url + "setuptriggers");
/* 26:34 */     String resp = "";
/* 27:   */     try
/* 28:   */     {
/* 29:37 */       client.executeMethod(httpGETFORM);
/* 30:   */     }
/* 31:   */     catch (HttpException e)
/* 32:   */     {
/* 33:39 */       resp = "5001: " + e.getMessage();
/* 34:   */       
/* 35:41 */       System.out.println("error response: " + resp);
/* 36:   */     }
/* 37:   */     catch (IOException e)
/* 38:   */     {
/* 39:44 */       resp = "5002: " + e.getMessage();
/* 40:   */       
/* 41:46 */       System.out.println("error response: " + resp);
/* 42:   */     }
/* 43:   */     catch (Exception e)
/* 44:   */     {
/* 45:50 */       System.out.println("error response: " + e.getMessage());
/* 46:   */     }
/* 47:   */     finally
/* 48:   */     {
/* 49:54 */       httpGETFORM.releaseConnection();
/* 50:55 */       client = null;
/* 51:56 */       httpGETFORM = null;
/* 52:   */     }
/* 53:   */   }
/* 54:   */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.livescore.LiveScoreTriggerSetup
 * JD-Core Version:    0.7.0.1
 */