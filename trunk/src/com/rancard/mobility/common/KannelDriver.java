package com.rancard.mobility.common;

import com.rancard.common.Config;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import com.rancard.common.Feedback;
import com.rancard.util.URLUTF8Encoder;

public class KannelDriver implements com.rancard.mobility.common.PushDriver {
    String value = "";
    //Settings
    protected String kannelGatewayUrl = "";
    private Feedback feedback = null;

    public KannelDriver (String gatewayURL) {
        this.kannelGatewayUrl = gatewayURL;
        try {
            feedback = new Feedback ();
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
    }

    public String sendSMSTextMessage(String to, String from, String text, String username, String password, String conn, String meta_data) {
        System.out.println ("Inside Kannel Driver - send SMSTextMessage....");
        String response = "";
        String reply = "";
        String smsc = "";
        String account = "";

        username = URLUTF8Encoder.encode (username);
        password = URLUTF8Encoder.encode (password);
        to = URLUTF8Encoder.encode (to);
        text = URLUTF8Encoder.encode (text);
        from = URLUTF8Encoder.encode (from);

        // conn parameter composed as follows "smsc:account"
        // account component is optional, so populate variable only if present
        String[] cnx = conn.split(":");
        if (cnx.length > 0) smsc = cnx[0];
        if (cnx.length > 1) account = cnx[1];


        String smsurl = this.kannelGatewayUrl + "/sendsms?to=" + to + 
                "&text=" + text + "&smsc=" + smsc + "&username=" + username +
                "&password=" + password + "&account=" + account + "&from=" + from +
                meta_data;

        System.out.println ("SMS URL: " + smsurl);

        HttpClient client = new HttpClient ();
        GetMethod httpGETFORM = new GetMethod (smsurl);
        try {
            client.executeMethod (httpGETFORM);
            String resp = getResponse (httpGETFORM.getResponseBodyAsStream ());
            response = processResponse (resp);
            System.out.println ("response: " + response);
        } catch (HttpException e) {
            //reply = "Fatal protocol violation: " + e.getMessage();
            reply = "ERROR:305";
            //response = (Feedback.PROTOCOL_ERROR + ": " + e.getMessage ());
            response = feedback.formDefaultMessage (Feedback.PROTOCOL_ERROR) + e.getMessage ();
            //logging statements
            System.out.println ("error response: " + response);
            //end of logging
            //e.printStackTrace();
        } catch (IOException e) {
            //reply ="Unable to connect to addess.Please check network interfaces ,Fatal transport error: "+ e.getMessage();
            reply = "ERROR:304";
            //response = (Feedback.TRANSPORT_ERROR + ": " + e.getMessage ());
            response = feedback.formDefaultMessage (Feedback.TRANSPORT_ERROR) + e.getMessage ();
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

    public String sendSMSTextMessage (String to, String from, String text, String username, String password, String cnx, String serviceID, String price) {
        // `service_id` and `price` not available for this driver.
        // Call alternate version of sendSMSTextMessage passing empty
        // string for `meta_data` parameters
        return sendSMSTextMessage(to, from, text, username, password, cnx, "");
    }

    public String sendSMSTextMessage (String[] to, String from, String text, String username, String password, String cnx, String serviceID, String price) {
        // To be implemented
        return "";
    }

    public String sendSMSBinaryMessage (String to, String from, String text, String udh, String codingScheme, String format, String username, String password,
            String cnx, String serviceID, String price) {
        // To be implemented
        return "";
    }

    public String sendWAPPushMessage (String to, String from, String text, String url, String username, String password, String cnx, String serviceID, String price) {
        // To be implemented
        return "";
    }

    private String getResponse (java.io.InputStream in) throws Exception {
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

    public String processResponse (String reply) throws Exception {
        boolean isOk = false;
        if (reply != null || !reply.equals ("")) {
            if (reply.substring (0,2).equalsIgnoreCase ("OK")) {
                isOk = true;
            } else if ((reply.length () > 290) && (reply.indexOf ("1001") != -1)) {
                isOk = true;
            } else if (reply.substring (0,1).equalsIgnoreCase ("3")) {
                isOk = true;
            } else if (reply.contains("Accepted for delivery")) {
                isOk = true;
            } else if (reply.contains("Queued for later delivery")) {
                isOk = true;
            }
        }
        return "Request sent: " + isOk + ". Response from gateway: " +
                reply;
    }
    
}
