/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.common;

import com.rancard.mobility.contentserver.CPConnections;
import com.rancard.util.URLUTF8Encoder;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 *
 * @author nii
 */
public class ThreadedPostman implements Runnable {
    
    public static String RNDVU_BUY_USER_ACTION_API_TMPLT = "http://192.168.1.246/rndvu/@@msisdn@@/action/log/@@keyword@@/buy";

    String apiEndpoint;
    HashMap<String, String> params;

    public ThreadedPostman (String urlTemplate) {
        System.out.println (new java.util.Date () + ":\t[ThreadedPostman]\tInitializing Postman. URL: " + urlTemplate);
        this.apiEndpoint = urlTemplate;
    }
    
    public ThreadedPostman (String urlTemplate, HashMap<String, String> params) {
        System.out.println (new java.util.Date () + ":\t[ThreadedPostman]\tInitializing Postman. URL: " + urlTemplate);
        this.apiEndpoint = urlTemplate;
        this.params = params;
    }

    public void run () {
        System.out.println (new java.util.Date () + ":\t[ThreadedPostman]\tAbout to build URL from template: " + this.apiEndpoint);
        String insertions = "";
        if (params != null && !params.isEmpty ()) {
            Iterator<String> itr = params.keySet ().iterator ();
            while (itr.hasNext ()) {
                String key = itr.next ();
                try {
                    insertions = insertions + key + "=" + java.net.URLEncoder.encode (params.get (key), "UTF-8") + "&";
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger (ThreadedPostman.class.getName()).log (Level.SEVERE, null, ex);
                }
            }
            insertions = insertions.substring (0, insertions.lastIndexOf ("&"));
        }
        
        try {
            String temp = URLUTF8Encoder.doMessageEscaping (insertions, apiEndpoint);
            apiEndpoint = temp;
        } catch (Exception e) {
        }
        
        System.out.println (new java.util.Date () + ":\t[ThreadedPostman]\tAbout to post to " + this.apiEndpoint);
        HttpClient client = new HttpClient ();
        GetMethod httpGETFORM = new GetMethod (this.apiEndpoint);

        try {
            client.executeMethod (httpGETFORM);
            System.out.println (new java.util.Date () + ":\t[ThreadedPostman]\tSuccessfully posted to " + this.apiEndpoint);
        } catch (HttpException e) {
            System.out.println (new java.util.Date () + ":\t[ThreadedPostman]\terror exception: " + e.getMessage ());
        } catch (IOException e) {
            System.out.println (new java.util.Date () + ":\t[ThreadedPostman]\terror exception: " + e.getMessage ());
        } catch (Exception e) {
            System.out.println (new java.util.Date () + ":\t[ThreadedPostman]\terror exception: " + e.getMessage ());
        } finally {
            httpGETFORM.releaseConnection ();
            client = null;
            httpGETFORM = null;
        }
    }
}
