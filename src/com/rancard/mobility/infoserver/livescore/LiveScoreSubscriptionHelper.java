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
/* 19:   */ public class LiveScoreSubscriptionHelper
/* 20:   */   implements Job
/* 21:   */ {
/* 22:   */   public void execute(JobExecutionContext context)
/* 23:   */     throws JobExecutionException
/* 24:   */   {
/* 25:30 */     System.out.println("LiveScoreSubscriptionHelper is executing on " + new Timestamp(new Date().getTime()).toString());
/* 26:31 */     HttpClient client = new HttpClient();
/* 27:32 */     String url = context.getJobDetail().getJobDataMap().getString("url");
/* 28:33 */     GetMethod httpGETFORM = new GetMethod(url + "manage_next_subscription");
/* 29:34 */     String resp = "";
/* 30:   */     try
/* 31:   */     {
/* 32:37 */       client.executeMethod(httpGETFORM);
/* 33:   */     }
/* 34:   */     catch (HttpException e)
/* 35:   */     {
/* 36:39 */       resp = "5001: " + e.getMessage();
/* 37:   */       
/* 38:41 */       System.out.println("error response: " + resp);
/* 39:   */     }
/* 40:   */     catch (IOException e)
/* 41:   */     {
/* 42:44 */       resp = "5002: " + e.getMessage();
/* 43:   */       
/* 44:46 */       System.out.println("error response: " + resp);
/* 45:   */     }
/* 46:   */     catch (Exception e)
/* 47:   */     {
/* 48:50 */       System.out.println("error response: " + e.getMessage());
/* 49:   */     }
/* 50:   */     finally
/* 51:   */     {
/* 52:54 */       httpGETFORM.releaseConnection();
/* 53:55 */       client = null;
/* 54:56 */       httpGETFORM = null;
/* 55:   */     }
/* 56:   */   }
/* 57:   */   
/* 58:   */   private String getResponse(InputStream in)
/* 59:   */     throws Exception
/* 60:   */   {
/* 61:61 */     String status = "error";
/* 62:62 */     String reply = "";
/* 63:63 */     String error = "";
/* 64:64 */     String responseString = "";
/* 65:65 */     BufferedReader br = null;
/* 66:   */     try
/* 67:   */     {
/* 68:67 */       InputStream responseBody = in;
/* 69:68 */       br = new BufferedReader(new InputStreamReader(responseBody));
/* 70:   */       
/* 71:70 */       String line = br.readLine();
/* 72:71 */       while (line != null)
/* 73:   */       {
/* 74:72 */         responseString = responseString + line;
/* 75:73 */         line = br.readLine();
/* 76:   */       }
/* 77:   */     }
/* 78:   */     catch (IOException e)
/* 79:   */     {
/* 80:76 */       throw new Exception("5002: " + e.getMessage());
/* 81:   */     }
/* 82:   */     finally
/* 83:   */     {
/* 84:78 */       br.close();
/* 85:79 */       in.close();
/* 86:   */       
/* 87:81 */       br = null;
/* 88:82 */       in = null;
/* 89:   */     }
/* 90:85 */     return responseString;
/* 91:   */   }
/* 92:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.livescore.LiveScoreSubscriptionHelper
 * JD-Core Version:    0.7.0.1
 */