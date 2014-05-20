/*  1:   */ package com.rancard.mobility.common;
/*  2:   */ 
/*  3:   */ import com.rancard.util.URLUTF8Encoder;
/*  4:   */ import java.io.IOException;
/*  5:   */ import java.io.PrintStream;
/*  6:   */ import java.io.UnsupportedEncodingException;
/*  7:   */ import java.net.URLEncoder;
/*  8:   */ import java.util.Date;
/*  9:   */ import java.util.Iterator;
/* 10:   */ import java.util.Map;
/* 11:   */ import java.util.Set;
/* 12:   */ import java.util.logging.Level;
/* 13:   */ import java.util.logging.Logger;
/* 14:   */ import org.apache.commons.httpclient.HttpClient;
/* 15:   */ import org.apache.commons.httpclient.HttpException;
/* 16:   */ import org.apache.commons.httpclient.methods.GetMethod;
/* 17:   */ 
/* 18:   */ public class ThreadedPostman
/* 19:   */   implements Runnable
/* 20:   */ {
/* 21:   */   public static final String RNDVU_BUY_USER_ACTION_API_TMPLT = "http://192.168.1.246/rndvu/@@msisdn@@/action/log/@@keyword@@/buy";
/* 22:   */   public static final String RNDVU_CONNECT_USER_API_TMPLT = "http://192.168.1.246/rndvu/@@recruiter@@/knows/@@recipient@@?keyword=@@keyword@@";
/* 23:   */   String apiEndpoint;
/* 24:   */   Map<String, String> params;
/* 25:   */   
/* 26:   */   public ThreadedPostman(String urlTemplate, Map<String, String> params)
/* 27:   */   {
/* 28:31 */     System.out.println(new Date() + ":\t[ThreadedPostman]\tInitializing Postman. URL: " + urlTemplate);
/* 29:32 */     this.apiEndpoint = urlTemplate;
/* 30:33 */     this.params = params;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public void run()
/* 34:   */   {
/* 35:38 */     System.out.println(new Date() + ":\t[ThreadedPostman]\tAbout to build URL from template: " + this.apiEndpoint);
/* 36:39 */     String insertions = "";
/* 37:40 */     if ((this.params != null) && (!this.params.isEmpty()))
/* 38:   */     {
/* 39:41 */       Iterator<String> itr = this.params.keySet().iterator();
/* 40:42 */       while (itr.hasNext())
/* 41:   */       {
/* 42:43 */         String key = (String)itr.next();
/* 43:   */         try
/* 44:   */         {
/* 45:45 */           insertions = insertions + key + "=" + URLEncoder.encode((String)this.params.get(key), "UTF-8") + "&";
/* 46:   */         }
/* 47:   */         catch (UnsupportedEncodingException ex)
/* 48:   */         {
/* 49:47 */           Logger.getLogger(ThreadedPostman.class.getName()).log(Level.SEVERE, null, ex);
/* 50:   */         }
/* 51:   */       }
/* 52:50 */       insertions = insertions.substring(0, insertions.lastIndexOf("&"));
/* 53:   */     }
/* 54:53 */     GetMethod httpGETFORM = null;
/* 55:   */     try
/* 56:   */     {
/* 57:55 */       this.apiEndpoint = URLUTF8Encoder.doMessageEscaping(insertions, this.apiEndpoint);
/* 58:   */       
/* 59:   */ 
/* 60:58 */       System.out.println(new Date() + ":\t[ThreadedPostman]\tAbout to post to " + this.apiEndpoint);
/* 61:59 */       HttpClient client = new HttpClient();
/* 62:60 */       httpGETFORM = new GetMethod(this.apiEndpoint);
/* 63:   */       
/* 64:   */ 
/* 65:63 */       client.executeMethod(httpGETFORM);
/* 66:64 */       System.out.println(new Date() + ":\t[ThreadedPostman]\tSuccessfully posted to " + this.apiEndpoint);
/* 67:   */     }
/* 68:   */     catch (HttpException e)
/* 69:   */     {
/* 70:66 */       System.out.println(new Date() + ":\t[ThreadedPostman]\terror exception: " + e.getMessage());
/* 71:   */     }
/* 72:   */     catch (IOException e)
/* 73:   */     {
/* 74:68 */       System.out.println(new Date() + ":\t[ThreadedPostman]\terror exception: " + e.getMessage());
/* 75:   */     }
/* 76:   */     catch (Exception e)
/* 77:   */     {
/* 78:70 */       System.out.println(new Date() + ":\t[ThreadedPostman]\terror exception: " + e.getMessage());
/* 79:   */     }
/* 80:   */     finally
/* 81:   */     {
/* 82:72 */       httpGETFORM.releaseConnection();
/* 83:   */     }
/* 84:   */   }
/* 85:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.common.ThreadedPostman
 * JD-Core Version:    0.7.0.1
 */