/*  1:   */ package com.rancard.mobility.infoserver.alert;
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
/* 16:   */ public class InfoServiceAlertJob
/* 17:   */   implements Job
/* 18:   */ {
/* 19:   */   public void execute(JobExecutionContext context)
/* 20:   */     throws JobExecutionException
/* 21:   */   {
/* 22:31 */     System.out.println(new Timestamp(new Date().getTime()).toString() + ":Job is executing for InforServiceAlert...");
/* 23:   */     
/* 24:33 */     String url = context.getJobDetail().getJobDataMap().getString("url");
/* 25:34 */     System.out.println(new Date() + ":InfoServiceAlertJob URL:" + url);
/* 26:35 */     HttpClient client = new HttpClient();
/* 27:36 */     GetMethod httpGETFORM = new GetMethod(url + "info_alert_service.jsp");
/* 28:37 */     String resp = "";
/* 29:   */     try
/* 30:   */     {
/* 31:40 */       client.executeMethod(httpGETFORM);
/* 32:   */     }
/* 33:   */     catch (HttpException e)
/* 34:   */     {
/* 35:42 */       resp = "5001: " + e.getMessage();
/* 36:   */       
/* 37:44 */       System.out.println("error response: " + resp);
/* 38:   */     }
/* 39:   */     catch (IOException e)
/* 40:   */     {
/* 41:47 */       resp = "5002: " + e.getMessage();
/* 42:   */       
/* 43:49 */       System.out.println("error response: " + resp);
/* 44:   */     }
/* 45:   */     catch (Exception e)
/* 46:   */     {
/* 47:53 */       System.out.println("error response: " + e.getMessage());
/* 48:   */     }
/* 49:   */     finally
/* 50:   */     {
/* 51:57 */       httpGETFORM.releaseConnection();
/* 52:58 */       client = null;
/* 53:59 */       httpGETFORM = null;
/* 54:   */     }
/* 55:   */   }
/* 56:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.alert.InfoServiceAlertJob
 * JD-Core Version:    0.7.0.1
 */