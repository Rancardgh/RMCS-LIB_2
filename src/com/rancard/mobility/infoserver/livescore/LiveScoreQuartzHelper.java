/*  1:   */ package com.rancard.mobility.infoserver.livescore;
/*  2:   */ 
/*  3:   */ import java.io.BufferedReader;
/*  4:   */ import java.io.IOException;
/*  5:   */ import java.io.InputStream;
/*  6:   */ import java.io.InputStreamReader;
/*  7:   */ import java.io.PrintStream;
/*  8:   */ import java.sql.Timestamp;
/*  9:   */ import java.util.Date;
/* 10:   */ import org.apache.commons.httpclient.HttpClient;
/* 11:   */ import org.apache.commons.httpclient.HttpException;
/* 12:   */ import org.apache.commons.httpclient.methods.GetMethod;
/* 13:   */ import org.quartz.Job;
/* 14:   */ import org.quartz.JobDataMap;
/* 15:   */ import org.quartz.JobDetail;
/* 16:   */ import org.quartz.JobExecutionContext;
/* 17:   */ import org.quartz.JobExecutionException;
/* 18:   */ 
/* 19:   */ public class LiveScoreQuartzHelper
/* 20:   */   implements Job
/* 21:   */ {
/* 22:   */   public void execute(JobExecutionContext context)
/* 23:   */     throws JobExecutionException
/* 24:   */   {
/* 25:30 */     System.out.println("QuartzHelper is executing on " + new Timestamp(new Date().getTime()).toString());
/* 26:31 */     HttpClient client = new HttpClient();
/* 27:32 */     String url = context.getJobDetail().getJobDataMap().getString("url");
/* 28:33 */     String allowance = context.getJobDetail().getJobDataMap().getString("allowance");
/* 29:34 */     GetMethod httpGETFORM = new GetMethod(url + "sendeventnotification?allowance=" + allowance);
/* 30:35 */     String resp = "";
/* 31:   */     try
/* 32:   */     {
/* 33:38 */       client.executeMethod(httpGETFORM);
/* 34:   */     }
/* 35:   */     catch (HttpException e)
/* 36:   */     {
/* 37:40 */       resp = "5001: " + e.getMessage();
/* 38:   */       
/* 39:42 */       System.out.println("error response: " + resp);
/* 40:   */     }
/* 41:   */     catch (IOException e)
/* 42:   */     {
/* 43:45 */       resp = "5002: " + e.getMessage();
/* 44:   */       
/* 45:47 */       System.out.println("error response: " + resp);
/* 46:   */     }
/* 47:   */     catch (Exception e)
/* 48:   */     {
/* 49:51 */       System.out.println("error response: " + e.getMessage());
/* 50:   */     }
/* 51:   */     finally
/* 52:   */     {
/* 53:55 */       httpGETFORM.releaseConnection();
/* 54:56 */       client = null;
/* 55:57 */       httpGETFORM = null;
/* 56:   */     }
/* 57:   */   }
/* 58:   */   
/* 59:   */   private String getResponse(InputStream in)
/* 60:   */     throws Exception
/* 61:   */   {
/* 62:62 */     String status = "error";
/* 63:63 */     String reply = "";
/* 64:64 */     String error = "";
/* 65:65 */     String responseString = "";
/* 66:66 */     BufferedReader br = null;
/* 67:   */     try
/* 68:   */     {
/* 69:68 */       InputStream responseBody = in;
/* 70:69 */       br = new BufferedReader(new InputStreamReader(responseBody));
/* 71:   */       
/* 72:71 */       String line = br.readLine();
/* 73:72 */       while (line != null)
/* 74:   */       {
/* 75:73 */         responseString = responseString + line;
/* 76:74 */         line = br.readLine();
/* 77:   */       }
/* 78:   */     }
/* 79:   */     catch (IOException e)
/* 80:   */     {
/* 81:77 */       throw new Exception("5002: " + e.getMessage());
/* 82:   */     }
/* 83:   */     finally
/* 84:   */     {
/* 85:79 */       br.close();
/* 86:80 */       in.close();
/* 87:   */       
/* 88:82 */       br = null;
/* 89:83 */       in = null;
/* 90:   */     }
/* 91:86 */     return responseString;
/* 92:   */   }
/* 93:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.infoserver.livescore.LiveScoreQuartzHelper
 * JD-Core Version:    0.7.0.1
 */