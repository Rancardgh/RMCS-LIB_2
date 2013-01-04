/*
 * BillingManager.java
 *
 * Created on August 30, 2006, 3:27 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.mobility.contentserver;
import com.rancard.common.Feedback;
import com.rancard.mobility.common.Driver;
import com.rancard.util.URLUTF8Encoder;
import com.rancard.util.payment.ePin;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 *
 * @author Kweku Tandoh
 */
public abstract class BillingManager {
    //static variables
    public static final String PIN_OPTION = "pinredemption";
    public static final String SHORTCODE_OPTION = "shortcode";
    public static final String OT_BILLING = "ot_bill";
    
    //does billing by sending SMS from the subscriber's number to a priced short code on the SMSC'
    public static boolean doShortCodeBilling (String serviceType, CPConnections cnxn, String networkPrefix, String msisdn, String messageString, String keyword, String provId) throws Exception {
        boolean isBilled = false;
        
        ServicePricingBean bean = ServicePricingBean.viewPriceWithUrl (provId, serviceType, networkPrefix);
        if(bean == null/* || bean.getKeyword () == null || !bean.getKeyword ().equalsIgnoreCase (keyword)*/){
            throw new Exception (Feedback.INVALID_SERVICE_REQUEST);
        }
        //get short code
        /*String shortcode = bean.getShortCode ();
         
        //initiate billing
        String resp = Driver.getDriver (cnxn.getDriverType (), cnxn.getGatewayURL ()).
                sendSMSTextMessage (shortcode, msisdn, new String("BILL " + messageString), cnxn.getUsername (), cnxn.getPassword (), cnxn.getConnection (), "", "");
         
        if (resp.length () > 18 && resp.substring (14, 18).equals ("true")) {
            isBilled = true;
        }*/
        
        String rawurl = bean.getUrl ();
        
        String username = URLUTF8Encoder.encode (cnxn.getUsername ());
        String password = URLUTF8Encoder.encode (cnxn.getPassword ());
        String transId = URLUTF8Encoder.encode (messageString);
        String number = URLUTF8Encoder.encode (msisdn);
        String shortcode = URLUTF8Encoder.encode (bean.getShortCode ());
        String conn = URLUTF8Encoder.encode (cnxn.getConnection ());
        
        String insertions = "username=" + username+ "&password=" + password + "&msg=" + transId + "&msisdn=" + number + "&shortcode=" +
                shortcode + "&conn=" + conn;
        String formattedurl = com.rancard.util.URLUTF8Encoder.doMessageEscaping (insertions, rawurl);
        
        HttpClient client = new HttpClient ();
        GetMethod httpGETFORM = new GetMethod (formattedurl);
        String resp = "";
        
        try {
            client.executeMethod (httpGETFORM);
            resp = getResponse (httpGETFORM.getResponseBodyAsStream ());
            System.out.println ("Billing response: " + resp);
            if(resp == null) {
                throw new Exception (Feedback.BILLING_MECH_FAILURE);
            }else{
            }
            //end of logging
        } catch (HttpException e) {
            resp = (Feedback.PROTOCOL_ERROR + ": " + e.getMessage ());
            //logging statements
            System.out.println ("error response: " + resp);
            //end of logging
            isBilled = false;
        } catch (IOException e) {
            resp = (Feedback.TRANSPORT_ERROR + ": " + e.getMessage ());
            //logging statements
            System.out.println ("error response: " + resp);
            //end of logging
            isBilled = false;
        } catch (Exception e) {
            //logging statements
            System.out.println ("error response: " + e.getMessage ());
            //end of logging
            isBilled = false;
        } finally {
            // Release the connection.
            httpGETFORM.releaseConnection ();
            client = null;
            httpGETFORM = null;
            bean = null;
            return processResponse (resp);
        }
    }
    
    public static boolean doPinRedemption (String serviceType, String provId, String networkPrefix, String pin, String keyword) throws Exception {
        boolean isBilled = false;
        //ServicePricingBean bean = ServicePricingBean.view (csid, keyword, networkPrefix);
        ServicePricingBean bean = ServicePricingBean.viewPrice (provId, serviceType, networkPrefix);
        if(!bean.getKeyword ().equalsIgnoreCase (keyword)){
            throw new Exception (Feedback.INVALID_SERVICE_REQUEST);
        }
        ePin voucher = new ePin ();
        voucher.setPin (pin);
        if(voucher.isValid () == false){
            throw new Exception (Feedback.MISSING_INVALID_PIN);
        }else{
            if ((voucher.getCurrentBalance () - Double.parseDouble (bean.getPrice ())) < 0) {
                throw new Exception (Feedback.INSUFFICIENT_CREDIT_ON_PIN);
            } else {
                voucher.setCurrentBalance (voucher.getCurrentBalance () - Double.parseDouble (bean.getPrice ()));
                voucher.updateMyLog ();
                isBilled = true;
            }
        }
        bean = null;
        voucher = null;
        return isBilled;
    }
    
    public static boolean doOTBilling (String serviceType, CPConnections cnxn, String networkPrefix, String msisdn, String keyword, String provId) throws Exception {
        boolean isBilled = false;
        
        ServicePricingBean bean = ServicePricingBean.viewPriceWithUrl (provId, serviceType, networkPrefix);
        if(bean == null/* || bean.getKeyword () == null || !bean.getKeyword ().equalsIgnoreCase (keyword*/){
            throw new Exception (Feedback.INVALID_SERVICE_REQUEST);
        }
        String number = msisdn.substring (msisdn.indexOf ("+233") + 4);
        
        String username = URLUTF8Encoder.encode (cnxn.getUsername ());
        String password = URLUTF8Encoder.encode (cnxn.getPassword ());
        String price = URLUTF8Encoder.encode (bean.getPrice ());
        number = URLUTF8Encoder.encode (number);
        
        String rawurl = bean.getUrl ();
        String action = "&action=2";
        String insertions = "username=" + username + "&password=" + password + "&msisdn=" + number + "&price=" + price;
        rawurl = com.rancard.util.URLUTF8Encoder.doMessageEscaping (insertions, rawurl);
        String formattedurl = rawurl + action;
        
        HttpClient client = new HttpClient ();
        GetMethod httpGETFORM = new GetMethod (formattedurl);
        String resp = "";
        
        try {
            client.executeMethod (httpGETFORM);
            resp = getResponse (httpGETFORM.getResponseBodyAsStream ());
            System.out.println ("OT Billing response: " + resp);
            if(resp == null) {
                throw new Exception (Feedback.BILLING_MECH_FAILURE);
            }else{
                httpGETFORM.releaseConnection ();
                //String code = resp.substring ((resp.lastIndexOf (";") - 1), resp.lastIndexOf (";"));
                String code = resp.split (";")[4];
                if(code.equals ("0")){
                    //isBilled = true;
                    String opTransactionId = resp.split (";")[3];
                    action = "&action=4";
                    formattedurl = rawurl + action + "&op_transaction_id=" + opTransactionId;
                    
                    httpGETFORM = new GetMethod (formattedurl);
                    resp = "";
                    client.executeMethod (httpGETFORM);
                    resp = getResponse (httpGETFORM.getResponseBodyAsStream ());
                    System.out.println ("OT Billing response: " + resp);
                    if(resp == null) {
                        throw new Exception (Feedback.BILLING_MECH_FAILURE);
                    }else{
                        //code = resp.substring ((resp.lastIndexOf (";") - 1), resp.lastIndexOf (";"));
                        code = resp.split (";")[4];
                        if(code.equals ("0")){
                            isBilled = true;
                        }
                    }
                }
            }
            //end of logging
        } catch (HttpException e) {
            resp = (Feedback.PROTOCOL_ERROR + ": " + e.getMessage ());
            //logging statements
            System.out.println ("error response: " + resp);
            //end of logging
            isBilled = false;
        } catch (IOException e) {
            resp = (Feedback.TRANSPORT_ERROR + ": " + e.getMessage ());
            //logging statements
            System.out.println ("error response: " + resp);
            //end of logging
            isBilled = false;
        } catch (Exception e) {
            //logging statements
            System.out.println ("error response: " + e.getMessage ());
            //end of logging
            isBilled = false;
        } finally {
            // Release the connection.
            httpGETFORM.releaseConnection ();
            client = null;
            httpGETFORM = null;
            return isBilled;
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
            throw new Exception (Feedback.TRANSPORT_ERROR + ": " + e.getMessage ());
        } finally {
            br.close ();
            in.close ();
            
            br = null;
            in = null;
        }
        
        return responseString;
    }
    
    public static boolean processResponse (String reply) throws Exception {
        boolean isOk = false;
        if (reply != null && !reply.equals ("")) {
            if (reply.substring (0,2).equalsIgnoreCase ("OK")) {
                isOk = true;
            } else if ((reply.length () > 290) && (reply.indexOf ("1001") != -1)) {
                isOk = true;
            } else if (reply.substring (0,1).equalsIgnoreCase ("3")) {
                isOk = true;
            }
        }
        return isOk;
    }
}
