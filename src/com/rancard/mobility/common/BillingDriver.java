/*
 * BillingDriver.java
 *
 * Created on February 8, 2007, 12:38 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.mobility.common;

import com.rancard.common.Feedback;
import com.rancard.util.URLUTF8Encoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 *
 * @author Messenger
 */
public class BillingDriver {
    
    //Settings
    public static final String CREDIT = "1";
    public static final String DEBIT = "0";
    
    protected String billingUrl = "";
    
    public BillingDriver (String networkPrefix, String command) throws Exception{
        this.billingUrl = BillingDriverDB.viewBillingUrl (networkPrefix, command);
    }
    
    public String fireBillingRequest (String transactionId, String msisdn, String price) {
        //logging statements
        System.out.println ("Inside Billing Driver - doDebit....");
        //end of logging
        String response = null;
        String reply = null;
        
        String billurl = this.billingUrl + "&user_id=" + msisdn + "&transaction_price=" + price + "&cp_transaction_id=" + transactionId;
        
        //logging statements
        System.out.println ("BILL URL: " + billurl);
        //end of logging
        HttpClient client = new HttpClient ();
        GetMethod httpGETFORM = new GetMethod (billurl);
        try {
            client.executeMethod (httpGETFORM);
            String resp = getResponse (httpGETFORM.getResponseBodyAsStream ());
            response = processResponse (resp);
            //logging statements
            System.out.println ("response: " + response);
            //end of logging
        } catch (HttpException e) {
            //reply = "Fatal protocol violation: " + e.getMessage();
            reply = "ERROR:305";
            response = (Feedback.PROTOCOL_ERROR + ": " + e.getMessage ());
            //logging statements
            System.out.println ("error response: " + response);
            //end of logging
            //e.printStackTrace();
        } catch (IOException e) {
            //reply ="Unable to connect to addess.Please check network interfaces ,Fatal transport error: "+ e.getMessage();
            reply = "ERROR:304";
            response = (Feedback.TRANSPORT_ERROR + ": " + e.getMessage ());
            //logging statements
            System.out.println ("error response: " + response);
            //end of logging
            //e.printStackTrace();
        } finally {
            // Release the connection.
            httpGETFORM.releaseConnection ();
            client = null;
            httpGETFORM = null;
            return response;
        }
    }
    
    public String fireBillingRequest (String transactionId, String msisdn, String price, String callBackUrl) {
        //logging statements
        System.out.println ("Inside Billing Driver - doDebit....");
        //end of logging
        String response = null;
        String reply = null;
        
        String billurl = this.billingUrl + "&user_id=" + msisdn + "&transaction_price=" + price + "&cp_transaction_id=" + transactionId + "responsePath=" + callBackUrl;
        
        //logging statements
        System.out.println ("BILL URL: " + billurl);
        //end of logging
        HttpClient client = new HttpClient ();
        GetMethod httpGETFORM = new GetMethod (billurl);
        try {
            client.executeMethod (httpGETFORM);
            String resp = getResponse (httpGETFORM.getResponseBodyAsStream ());
            response = processResponse (resp);
            //logging statements
            System.out.println ("response: " + response);
            //end of logging
        } catch (HttpException e) {
            //reply = "Fatal protocol violation: " + e.getMessage();
            reply = "ERROR:305";
            response = (Feedback.PROTOCOL_ERROR + ": " + e.getMessage ());
            //logging statements
            System.out.println ("error response: " + response);
            //end of logging
            //e.printStackTrace();
        } catch (IOException e) {
            //reply ="Unable to connect to addess.Please check network interfaces ,Fatal transport error: "+ e.getMessage();
            reply = "ERROR:304";
            response = (Feedback.TRANSPORT_ERROR + ": " + e.getMessage ());
            //logging statements
            System.out.println ("error response: " + response);
            //end of logging
            //e.printStackTrace();
        } finally {
            // Release the connection.
            httpGETFORM.releaseConnection ();
            client = null;
            httpGETFORM = null;
            return response;
        }
    }
    
    private static String getResponse (java.io.InputStream in) throws Exception {
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
            //reply ="Unable to connect to addess.Please check network interfaces ,Fatal transport error: "+ e.getMessage();
            reply = "ERROR:304";
            System.err.println (Feedback.TRANSPORT_ERROR + ": " + e.getMessage ());
            //e.printStackTrace();
        } finally {
            br.close ();
            in.close ();
            
            br = null;
            in = null;
        }
        
        return responseString;
    }
    
    private static String processResponse (String reply) throws Exception {
        boolean isOk = false;
        if (reply != null || !reply.equals ("")) {
            if (reply.substring (0,2).equalsIgnoreCase ("OK")) {
                isOk = true;
            } else if ((reply.length () > 290) && (reply.indexOf ("1001") != -1)) {
                isOk = true;
            } else if (reply.substring (0,1).equalsIgnoreCase ("3")) {
                isOk = true;
            }
        }
        return "Request sent: " + isOk + ". Response from gateway: " +
                reply;
    }
    
    public static void test (String url) {
        String response;
        HttpClient client = new HttpClient ();
        GetMethod method = new GetMethod ();
        String[] tokens = url.split ("[?]");
        
        
        // Create a method instance.
        try{
            //method.setPath (URLEncoder.encode (tokens[0],"UTF-8"));
            //method.setQueryString (URLEncoder.encode (tokens[1],"UTF-8"));
            method = new GetMethod(/*URLEncoder.encode(*/url/*,"UTF-8")*/);
            client.executeMethod (method);
        }catch(UnsupportedEncodingException ee){
            System.out.println(ee.getMessage ());
        } catch (HttpException e) {
            //reply = "Fatal protocol violation: " + e.getMessage();
            //end of logging
            e.printStackTrace();
        } catch (IOException e) {
            //reply ="Unable to connect to addess.Please check network interfaces ,Fatal transport error: "+ e.getMessage();
            //end of logging
            e.printStackTrace();
        } catch (Exception e) {
            //reply ="Unable to connect to addess.Please check network interfaces ,Fatal transport error: "+ e.getMessage();
            //end of logging
            e.printStackTrace();
        } finally {
            // Release the connection.
        }
    }
}
