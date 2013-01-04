/*
 * InfoServiceAlertJob.java
 *
 * Created on May 5, 2008, 12:43 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.mobility.infoserver.alert;
import java.io.IOException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.rancard.common.Feedback;
/**
 *
 * @author Administrator
 */
public class InfoServiceAlertJob implements Job{
    
    /** Creates a new instance of InfoServiceAlertJob */
    public InfoServiceAlertJob() {
    }
    
     public void execute(JobExecutionContext context) throws JobExecutionException {
         
        System.out.println(new java.sql.Timestamp(new java.util.Date().getTime()).toString()+":Job is executing for InforServiceAlert...");
        
        String url = context.getJobDetail ().getJobDataMap ().getString ("url");
        System.out.println(new java.util.Date()+":InfoServiceAlertJob URL:"+url);
        HttpClient client = new HttpClient ();
        GetMethod httpGETFORM = new GetMethod (url + "info_alert_service.jsp");
        String resp = "";
        
        try {
            client.executeMethod (httpGETFORM);
        } catch (HttpException e) {
            resp = (Feedback.PROTOCOL_ERROR + ": " + e.getMessage ());
            //logging statements
            System.out.println ("error response: " + resp);
            //end of logging
        } catch (IOException e) {
            resp = (Feedback.TRANSPORT_ERROR + ": " + e.getMessage ());
            //logging statements
            System.out.println ("error response: " + resp);
            //end of logging
        } catch (Exception e) {
            //logging statements
            System.out.println ("error response: " + e.getMessage ());
            //end of logging
        } finally {
            // Release the connection.
            httpGETFORM.releaseConnection ();
            client = null;
            httpGETFORM = null;
        }
    }
    
        
       
    
}
