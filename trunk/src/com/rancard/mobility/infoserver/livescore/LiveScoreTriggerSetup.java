/*
 * DumbJob.java
 *
 * Created on January 25, 2007, 12:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.mobility.infoserver.livescore;

import com.rancard.common.Feedback;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class LiveScoreTriggerSetup implements Job {
    
    public LiveScoreTriggerSetup () {
    }
    
    public void execute (JobExecutionContext context) throws JobExecutionException {
        System.out.println ("TriggerSetup is executing on " + new java.sql.Timestamp (new java.util.Date ().getTime ()).toString ());
        String url = context.getJobDetail ().getJobDataMap ().getString ("url");
        HttpClient client = new HttpClient ();
        GetMethod httpGETFORM = new GetMethod (url + "setuptriggers");
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
    
    /*private String getResponse (java.io.InputStream in) throws Exception {
        String status = "error";
        String reply = "";
        String error = "";
        String responseString = "";
        BufferedReader br = null;
        try {
            InputStream responseBody = in;
            br = new BufferedReader (new InputStreamReader (
                    responseBody));
            String line = br.readLine ();
            while(line != null){
                responseString = responseString + line;
                line = br.readLine ();
            }
        } catch (IOException e) {
            throw new Exception (Feedback.TRANSPORT_ERROR + ": " + e.getMessage ());
        } finally {
            br.close ();
            in.close ();
            
            br = null;
            in = null;
        }
        
        return responseString;
    }*/
}