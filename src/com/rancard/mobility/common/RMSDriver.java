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
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class RMSDriver implements com.rancard.mobility.common.PushDriver {
    String value = "";
    //Settings
    protected String rmsGatewayUrl = "";
    private Feedback feedback = null;
    
    //message types
    public static final String NOKIARING = "nrt";
    public static final String NOKIAPICT = "npm";
    public static final String NOKIACLI = "ncl";
    public static final String NOKIAOPR = "nol";
    public static final String OTHER = "other";
    
    public RMSDriver (String gatewayURL) {
        this.rmsGatewayUrl = gatewayURL;
        try {
            feedback = new Feedback ();
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
    }

    public String sendSMSTextMessage(String to, String from, String text, String username, String password, String conn, String meta_data) {
        // meta_data not available for this driver.
        // Call alternate version of sendSMSTextMessage passing empty
        // strings for `serviceID` and `price` parameters
        return sendSMSTextMessage(to, from, text, username, password, conn, "", "");
    }
    
    public String sendSMSTextMessage (String to, String from, String text, String username, String password, String cnx, String serviceID, String price) {
        //logging statements
        System.out.println ("Inside RMS Driver - send SMSTextMessage....");
        //end of logging
        String response = null;
        String reply = null;
        
        username = URLUTF8Encoder.encode (username);
        password = URLUTF8Encoder.encode (password);
        to = URLUTF8Encoder.encode (to);
        text = URLUTF8Encoder.encode (text);
        from = URLUTF8Encoder.encode (from);
        
        String smsurl = this.rmsGatewayUrl + "/sendsms?to=" + to + "&text=" +
                text + "&conn=" + cnx + "&username=" + username +
                "&password=" + password + "&serviceId=" + serviceID +
                "&price=" + price + "&from=" + from;
        
        //logging statements
        System.out.println ("SMS URL: " + smsurl);
        //end of logging
        HttpClient client = new HttpClient ();
        GetMethod httpGETFORM = new GetMethod (smsurl);
        try {
            client.executeMethod (httpGETFORM);
            //System.out.println("SMS URL: " + smsurl);
            String resp = getResponse (httpGETFORM.getResponseBodyAsStream ());
            response = processResponse (resp);
            //logging statements
            System.out.println ("response: " + response);
            //end of logging
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
    
    public String sendSMSTextMessage (String[] to, String from, String text, String username, String password, String cnx, String serviceID, String price) {
        //logging statements
        System.out.println ("Inside RMS Driver - send SMSTextMessage....");
        //end of logging
        
        int BULK_MSG_LIMIT = 0;
        int MILLISECS_BETWEEN_TRANSMITS = 0;
        
        try{
            String bulkMsgLimit = com.rancard.util.PropertyHolder.getPropsValue ("BULK_MSG_LIMIT");
            if(bulkMsgLimit != null && !bulkMsgLimit.equals ("")){
                BULK_MSG_LIMIT = Integer.parseInt (bulkMsgLimit);
            }else{
                BULK_MSG_LIMIT = 40;
            }
        }catch(Exception e){
            BULK_MSG_LIMIT = 40;
        }
        
        try{
            String millis = com.rancard.util.PropertyHolder.getPropsValue ("MILLIS_BETWEEN_TRANSMITS");
            if(millis != null && !millis.equals ("")){
                MILLISECS_BETWEEN_TRANSMITS= Integer.parseInt (millis);
            }else{
                MILLISECS_BETWEEN_TRANSMITS = 1000;
            }
        }catch(Exception e){
            MILLISECS_BETWEEN_TRANSMITS = 1000;;
        }
        
        String response = null;
        String reply = null;
        
        int pages = 0;
        
        double temp_pages = to.length / BULK_MSG_LIMIT;
        if ((to.length % BULK_MSG_LIMIT) > 0) {
            temp_pages += 1;
        }
        
        //double temp_pages = new Double (to.length) / BULK_MSG_LIMIT;
        pages = new Double (temp_pages).intValue ();
        
        String[] to_paginated = new String[pages];
        
        //String toStr = to[0];
        String toStr = "";
        int to_count = 0;
        int page_count = 0;
        for (int i = 0; i < to.length; i++) {
            if(toStr.length () == 0){
                toStr = to[i];
            }else{
                toStr = toStr + ":" + to[i];
            }
            to_count++;
            if(to_count == BULK_MSG_LIMIT || i == (to.length - 1)){
                to_paginated[page_count] = toStr;
                to_count = 0;
                page_count++;
                toStr = "";
            }
        }
        
        username = URLUTF8Encoder.encode (username);
        password = URLUTF8Encoder.encode (password);
        text = URLUTF8Encoder.encode (text);
        from = URLUTF8Encoder.encode (from);
        
        for(int p = 0; p < to_paginated.length; p++) {
            
            toStr = URLUTF8Encoder.encode (to_paginated[p]);
            
            String smsurl = this.rmsGatewayUrl + "/sendsms?to=" + toStr + "&text=" +
                    text + "&conn=" + cnx + "&username=" + username +
                    "&password=" + password + "&serviceId=" + serviceID +
                    "&price=" + price + "&from=" + from;
            
            //logging statements
            System.out.println ("SMS URL: " + smsurl);
            //end of logging
            HttpClient client = new HttpClient ();
            GetMethod httpGETFORM = new GetMethod (smsurl);
            try {
                client.executeMethod (httpGETFORM);
                //System.out.println("SMS URL: " + smsurl);
                String resp = getResponse (httpGETFORM.getResponseBodyAsStream ());
                response = response + " " + processResponse (resp);
                //logging statements
                //end of logging
            } catch (HttpException e) {
                //reply = "Fatal protocol violation: " + e.getMessage();
                reply = "ERROR:305";
                try {
                    //response = (Feedback.PROTOCOL_ERROR + ": " + e.getMessage ());
                    response = feedback.formDefaultMessage (Feedback.PROTOCOL_ERROR) + e.getMessage ();
                } catch (Exception ex) {
                    ex.printStackTrace ();
                }
                //logging statements
                System.out.println ("error response: " + response);
                //end of logging
                //e.printStackTrace();
            } catch (IOException e) {
                //reply ="Unable to connect to addess.Please check network interfaces ,Fatal transport error: "+ e.getMessage();
                reply = "ERROR:304";
                try {
                    //response = (Feedback.TRANSPORT_ERROR + ": " + e.getMessage ());
                    response = feedback.formDefaultMessage (Feedback.TRANSPORT_ERROR) + e.getMessage ();
                } catch (Exception ex) {
                    //ex.printStackTrace();
                }
                //logging statements
                System.out.println ("error response: " + response);
                //end of logging
                //e.printStackTrace();
            }catch(Exception e) {
                try {
                    //response = (Feedback.ROUTE_NOTIFICATION_FAILED + ": " + e.getMessage ());
                    response = feedback.formDefaultMessage (Feedback.ROUTE_NOTIFICATION_FAILED) + e.getMessage ();
                } catch (Exception ex) {
                    //ex.printStackTrace();
                }
            } finally {
                // Release the connection.
                httpGETFORM.releaseConnection ();
                client = null;
                httpGETFORM = null;
            }
            
            //break for a second
            try{
                Thread.sleep (MILLISECS_BETWEEN_TRANSMITS);
            }catch (InterruptedException ie) {
                
            }catch (Exception e) {
                
            }
        }
        System.out.println ("response: " + response);
        return response;
    }
    
    public String sendSMSBinaryMessage (String to, String from, String text, String udh, String codingScheme, String format, String username, String password,
            String cnx, String serviceID, String price) {
        //logging statements
        System.out.println ("Inside RMS Driver - send SMSBinaryMessage....");
        //end of logging
        
        String response = null;
        String reply = null;
        
        if (format.equalsIgnoreCase ("noktxt") || format.equalsIgnoreCase ("ott")) {
            format = this.NOKIARING;
        } else if (format.equalsIgnoreCase ("ems")) {
            udh = text;
            text = new String ("");
            format = this.OTHER;
        } else if (format.equalsIgnoreCase ("imy") || format.equalsIgnoreCase ("10.imy")) {
            int ieilength = (text.length ()/2) + 1;
            int totalLength = ieilength + 2;
            String iei = "0C";
            String xieilength = Integer.toHexString (ieilength);
            if(xieilength.length () % 2 != 0) {xieilength = "0" + xieilength;} //zero extend
            String offset = "00";
            String totalUDHlength = Integer.toHexString (totalLength);
            if(totalUDHlength.length () % 2 != 0) {totalUDHlength = "0" + totalUDHlength;} //zero extend
            udh = totalUDHlength + iei + xieilength + offset + text;
            text = new String ("");
            format = this.OTHER;
        } else if (format.equalsIgnoreCase ("bmp") || format.equalsIgnoreCase ("wbmp")) {
            String width = text.substring (2, 4);
            int basetenofwidth = Integer.parseInt (width, 16);
            width = Integer.toHexString (basetenofwidth/8); //EMS standards require that the width(DEC) be divided by 8 and converted back to hex
            if(width.length () % 2 != 0) {width = "0" + width;} //zero extend
            String height = text.substring (4, 6);
            text = text.substring (8);
            int ieilength = (text.length ()/2) + 3;
            int totalLength = ieilength + 2;
            String iei = "12";
            String xieilength = Integer.toHexString (ieilength);
            if(xieilength.length () % 2 != 0) {xieilength = "0" + xieilength;} //zero extend
            String offset = "00";
            String totalUDHlength = Integer.toHexString (totalLength);
            if(totalUDHlength.length () % 2 != 0) {totalUDHlength = "0" + totalUDHlength;} //zero extend
            udh = totalUDHlength + iei + xieilength + offset + width + height + text;
            text = new String ("");
            format = this.OTHER;
        }
        
        username = URLUTF8Encoder.encode (username);
        password = URLUTF8Encoder.encode (password);
        to = URLUTF8Encoder.encode (to);
        text = URLUTF8Encoder.encode (text);
        from = URLUTF8Encoder.encode (from);
        
        String smsurl = this.rmsGatewayUrl + "/sendsms?to=" + to + "&text=" +
                text + "&conn=" + cnx + "&username=" + username +
                "&password=" + password + "&serviceId=" + serviceID +
                "&price=" + price + "&udh=" + udh + "&from=" + from + "&msgType=" +
                format;
        
        //logging statements
        System.out.println ("SMS URL: " + smsurl);
        //end of logging
        
        HttpClient client = new HttpClient ();
        GetMethod httpGETFORM = new GetMethod (smsurl);
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
            //response = (Feedback.PROTOCOL_ERROR + ": " + e.getMessage ());
            try {
                //response = (Feedback.PROTOCOL_ERROR + ": " + e.getMessage ());
                response = feedback.formDefaultMessage (Feedback.PROTOCOL_ERROR) + e.getMessage ();
            } catch (Exception ex) {
                ex.printStackTrace ();
            }
            System.out.println ("error response: " + response);
            //end of logging
            //e.printStackTrace();
        } catch (IOException e) {
            //reply ="Unable to connect to addess.Please check network interfaces ,Fatal transport error: "+ e.getMessage();
            reply = "ERROR:304";
            //response = (Feedback.TRANSPORT_ERROR + ": " + e.getMessage ());
            try {
                //response = (Feedback.TRANSPORT_ERROR + ": " + e.getMessage ());
                response = feedback.formDefaultMessage (Feedback.TRANSPORT_ERROR) + e.getMessage ();
            } catch (Exception ex) {
                //ex.printStackTrace();
            }
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
    
    public String sendWAPPushMessage (String to, String from, String text, String url, String username, String password, String cnx, String serviceID, String price) {
        //logging statements
        System.out.println ("Inside RMS Driver - send WAP push....");
        //end of logging
        String response = "";
        String reply = null;
        
        username = URLUTF8Encoder.encode (username);
        password = URLUTF8Encoder.encode (password);
        to = to.substring (to.indexOf ("+") + 1);
        to = URLUTF8Encoder.encode (to);
        text = URLUTF8Encoder.encode (text);
        from = URLUTF8Encoder.encode (from);
        
        String wapurl = this.rmsGatewayUrl + "/sendwappush?to=" + to + "&text=" +
                text + "&conn=" + cnx + "&username=" + username +
                "&password=" + password + "&serviceId=" + serviceID +
                "&price=" + price + "&url=" + url + "&from=" + from;
        
        //logging statements
        System.out.println ("WAP push URL: " + wapurl);
        //end of logging
        
        HttpClient client = new HttpClient ();
        GetMethod httpGETFORM = new GetMethod (wapurl);
        try {
            client.executeMethod (httpGETFORM);
            //System.out.println("WAP URL: " + wapurl);
            String resp = getResponse (httpGETFORM.getResponseBodyAsStream ());
            response = processResponse (resp);
            //logging statements
            System.out.println ("response: " + response);
            //end of logging
        } catch (HttpException e) {
            //reply = "Fatal protocol violation: " + e.getMessage();
            reply = "ERROR:305";
            //response = (Feedback.PROTOCOL_ERROR + ": " + e.getMessage ());
            try {
                //response = (Feedback.TRANSPORT_ERROR + ": " + e.getMessage ());
                response = feedback.formDefaultMessage (Feedback.PROTOCOL_ERROR) + e.getMessage ();
            } catch (Exception ex) {
                //ex.printStackTrace();
            }
            //logging statements
            System.out.println ("error response: " + response);
            //end of logging
            //e.printStackTrace();
        } catch (IOException e) {
            //reply ="Unable to connect to addess.Please check network interfaces ,Fatal transport error: "+ e.getMessage();
            reply = "ERROR:304";
            //response = (Feedback.TRANSPORT_ERROR + ": " + e.getMessage ());
            try {
                //response = (Feedback.TRANSPORT_ERROR + ": " + e.getMessage ());
                response = feedback.formDefaultMessage (Feedback.TRANSPORT_ERROR) + e.getMessage ();
            } catch (Exception ex) {
                //ex.printStackTrace();
            }
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
            }
        }
        return "Request sent: " + isOk + ". Response from gateway: " +
                reply;
    }
    
    public static String getShortCode (String pricePoint) {
        String sc = new String ();
        if(pricePoint.equals ("3000"))
            sc = "700";
        else if(pricePoint.equals ("5000"))
            sc = "701";
        else if(pricePoint.equals ("9000"))
            sc = "702";
        else if(pricePoint.equals ("10000"))
            sc = "703";
        else if(pricePoint.equals ("15000"))
            sc = "704";
        else if(pricePoint.equals ("20000"))
            sc = "705";
        
        return sc;
    }
}
