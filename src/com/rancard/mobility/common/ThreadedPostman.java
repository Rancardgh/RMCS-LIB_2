 package com.rancard.mobility.common;
 
 import com.rancard.util.URLUTF8Encoder;
 import java.io.IOException;
 import java.io.UnsupportedEncodingException;
 import java.net.URLEncoder;
 import java.util.Date;
 import java.util.Iterator;
 import java.util.Map;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import org.apache.commons.httpclient.HttpClient;
 import org.apache.commons.httpclient.HttpException;
 import org.apache.commons.httpclient.methods.GetMethod;
 
 public class ThreadedPostman
   implements Runnable
 {
       public static final String RNDVU_BUY_USER_ACTION_API_TMPLT = "http://192.168.1.246/rndvu/@@msisdn@@/action/log/@@keyword@@/buy";
       public static final String RNDVU_CONNECT_USER_API_TMPLT = "http://192.168.1.246/rndvu/@@recruiter@@/knows/@@recipient@@?keyword=@@keyword@@";
       String apiEndpoint;
       Map<String, String> params;
       
   public ThreadedPostman(String urlTemplate, Map<String, String> params)
   {
     System.out.println(new Date() + ":\t[ThreadedPostman]\tInitializing Postman. URL: " + urlTemplate);
     this.apiEndpoint = urlTemplate;
     this.params = params;
   }
       
   public void run()
   {
     System.out.println(new Date() + ":\t[ThreadedPostman]\tAbout to build URL from template: " + this.apiEndpoint);
     String insertions = "";
     if ((this.params != null) && (!this.params.isEmpty()))
     {
       Iterator<String> itr = this.params.keySet().iterator();
       while (itr.hasNext())
       {
         String key = (String)itr.next();
         try
         {
           insertions = insertions + key + "=" + URLEncoder.encode((String)this.params.get(key), "UTF-8") + "&";
         }
         catch (UnsupportedEncodingException ex)
         {
           Logger.getLogger(ThreadedPostman.class.getName()).log(Level.SEVERE, null, ex);
         }
       }
       insertions = insertions.substring(0, insertions.lastIndexOf("&"));
     }
     GetMethod httpGETFORM = null;
     try
     {
       this.apiEndpoint = URLUTF8Encoder.doMessageEscaping(insertions, this.apiEndpoint);
       
 
       System.out.println(new Date() + ":\t[ThreadedPostman]\tAbout to post to " + this.apiEndpoint);
       HttpClient client = new HttpClient();
       httpGETFORM = new GetMethod(this.apiEndpoint);
       
 
       client.executeMethod(httpGETFORM);
       System.out.println(new Date() + ":\t[ThreadedPostman]\tSuccessfully posted to " + this.apiEndpoint);
     }
     catch (HttpException e)
     {
       System.out.println(new Date() + ":\t[ThreadedPostman]\terror exception: " + e.getMessage());
     }
     catch (IOException e)
     {
       System.out.println(new Date() + ":\t[ThreadedPostman]\terror exception: " + e.getMessage());
     }
     catch (Exception e)
     {
       System.out.println(new Date() + ":\t[ThreadedPostman]\terror exception: " + e.getMessage());
     }
     finally
     {
       httpGETFORM.releaseConnection();
     }
   }
 }
