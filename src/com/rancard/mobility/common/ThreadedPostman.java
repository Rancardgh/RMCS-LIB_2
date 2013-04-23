/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.common;

import com.rancard.mobility.contentserver.CPConnections;
import java.io.IOException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 *
 * @author nii
 */
public class ThreadedPostman implements Runnable {

    String apiEndpoint;
    int delay;

    public ThreadedPostman (String url, int delay) {
        System.out.println (new java.util.Date () + ":\t[ThreadedPostman]\tInitializing Postman. URL: " + url);
        this.apiEndpoint = url;
        this.delay = delay;
    }

    public ThreadedPostman (String url) {
        System.out.println (new java.util.Date () + ":\t[ThreadedPostman]\tInitializing Postman. URL: " + url);
        this.apiEndpoint = url;
        this.delay = 0;
    }

    public void run () {
        System.out.println (new java.util.Date () + ":\t[ThreadedPostman]\tAbout to post to " + this.apiEndpoint);
        HttpClient client = new HttpClient ();
        GetMethod httpGETFORM = new GetMethod (this.apiEndpoint);

        try {
            client.executeMethod (httpGETFORM);
            System.out.println (new java.util.Date () + ":\t[ThreadedPostman]\tSuccessfully posted to " + this.apiEndpoint);
            //resp = httpGETFORM.getResponseBodyAsString ();
            //resp = (resp == null) ? "OK" : resp.trim ();
            //System.out.println ("Response from API call: " + resp);
            //out.println ("Response from post-billing process: " + resp);
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
