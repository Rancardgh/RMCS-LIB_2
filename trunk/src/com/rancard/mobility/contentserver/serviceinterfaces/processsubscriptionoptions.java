/*
 * processsubscriberequest.java
 *
 * Created on October 28, 2006, 1:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.mobility.contentserver.serviceinterfaces;

import com.rancard.common.Feedback;
import com.rancard.mobility.contentserver.CPConnections;
import com.rancard.mobility.contentserver.CPSite;
import com.rancard.mobility.infoserver.common.services.ServiceManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.*;

/**
 *
 * @author Messenger
 */
public class processsubscriptionoptions extends HttpServlet implements RequestDispatcher {
    
    //subscription options
    public static final String ACTIVATE_SUBSCRIPTION = "act";
    public static final String DEACTIVATE_SUBSCRIPTION = "deact";
    
    //Initialize global variables
    public void init () throws ServletException {
    }
    
    //Process the HTTP Get request
    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get reeponse writer
        PrintWriter out = response.getWriter ();
        CPConnections cnxn = new CPConnections ();
        
        String ACK = (String) request.getAttribute ("ack");
        String kw = request.getParameter ("keyword");
        String msg = request.getParameter ("msg");
        String msisdn = request.getParameter ("msisdn");
        String provId = (String) request.getAttribute ("acctId");
        
        String siteType = (String) request.getAttribute ("site_type");
        String lang = (String) request.getAttribute ("default_lang");
        if(lang == null || lang.equals ("")){
            lang = "en";
        }
        Feedback feedback = (Feedback) this.getServletContext ().getAttribute ("feedback_" + lang);
        if(feedback == null){
            try{
                feedback = new Feedback ();
            }catch(Exception e){
            }
        }
        
        String provName ="";
        String message = "";
        
        if (provId == null || provId.equals ("")) {
            try {
                if(siteType.equals (CPSite.SMS)){
                    message = feedback.getUserFriendlyDescription (Feedback.MISSING_INVALID_PROV_ID);
                }else{
                    message = feedback.formDefaultMessage (Feedback.MISSING_INVALID_PROV_ID);
                }
            }catch (Exception ex) {
                message = ex.getMessage ();
            }
            out.println (message);
            return;
        }
        //no msisdn found
        if (msisdn == null || msisdn.equals ("")) {
            try {
                if(siteType.equals (CPSite.SMS)){
                    message = feedback.getUserFriendlyDescription (Feedback.MISSING_INVALID_MSISDN);
                }else{
                    message = feedback.formDefaultMessage (Feedback.MISSING_INVALID_MSISDN);
                }
            }catch (Exception ex) {
                message = ex.getMessage ();
            }
            out.println (message);
            return;
        }
        //message body
        if (msg == null || msg.equals ("")) {
            msg = "";
        }
        //default message for acknowledgment
        if (ACK == null || ACK.equals ("")) {
            ACK = "";
        }
        
        //logging statements
        System.out.println ("Received request to update subscription");
        System.out.println ("Date received: " + java.util.Calendar.getInstance ().getTime ().toString ());
        System.out.println ("Keyword: " + kw);
        System.out.println ("Message: " + msg);
        System.out.println ("Subscriber's number: " + msisdn);
        System.out.println ("Service privoder's ID: " + provId);
        //end of logging
        
        //validate number
        String number = msisdn;
        if (number.indexOf ("+") != -1) {
            StringBuffer sb = new StringBuffer (number);
            sb.deleteCharAt (number.indexOf ("+"));
            number = sb.toString ();
            
            sb = null;
        }
        number = number.trim ();
        
        try {
            //checks that number is a string of digits only
            Long.parseLong (number);
            msisdn = "+" + number;
        } catch (NumberFormatException e) {
            try {
                if(siteType.equals (CPSite.SMS)){
                    message = feedback.getUserFriendlyDescription (Feedback.MISSING_INVALID_MSISDN);
                }else{
                    message = feedback.formDefaultMessage (Feedback.MISSING_INVALID_MSISDN);
                }
            }catch (Exception ex) {
                message = ex.getMessage ();
            }
            out.println (message);
            return;
        }
        
        
        //get connection to requesting network for this provider
        try {
            cnxn = CPConnections.getConnection (provId, msisdn);
        } catch (Exception e) {
            try {
                if(siteType.equals (CPSite.SMS)){
                    message = feedback.getUserFriendlyDescription (Feedback.UNSUPPORTED_NETWORK);
                }else{
                    message = feedback.formDefaultMessage (Feedback.UNSUPPORTED_NETWORK);
                }
            }catch (Exception ex) {
                message = ex.getMessage ();
            }
            out.println (message);
            return;
        }
        
        String resp = ACK;
        
        if (msg != null && !msg.equals ("")) {
            String subkw = msg.split (" ")[0];
            String opt = msg.split (" ")[1];
            int action = 2;
            if(opt.equalsIgnoreCase (this.ACTIVATE_SUBSCRIPTION)){
                action = 1;
            }else if(opt.equalsIgnoreCase (this.DEACTIVATE_SUBSCRIPTION)){
                action = 0;
            }
            
            if (action != 2) {
                try{
                    ServiceManager.updateSubscriptionStatus (msisdn, subkw, provId, action);
                    System.out.println ("Update completed");
                }catch (Exception e) {
                    System.out.println ("Update NOT completed");
                    try {
                        if(siteType.equals (CPSite.SMS)){
                            message = feedback.getUserFriendlyDescription (e.getMessage ());
                        }else{
                            message = feedback.formDefaultMessage (e.getMessage ());
                        }
                    }catch (Exception ex) {
                        message = ex.getMessage ();
                    }
                    out.println (message);
                    return;
                }
            } else {
                System.out.println ("Update NOT completed");
                try {
                    if(siteType.equals (CPSite.SMS)){
                        message = feedback.getUserFriendlyDescription (Feedback.GENERIC_ERROR);
                    }else{
                        message = feedback.formDefaultMessage (Feedback.GENERIC_ERROR);
                    }
                }catch (Exception ex) {
                    message = ex.getMessage ();
                }
                resp = message;
            }
        }else{
            System.out.println ("Update NOT completed");
            try {
                if(siteType.equals (CPSite.SMS)){
                    message = feedback.getUserFriendlyDescription (Feedback.GENERIC_ERROR);
                }else{
                    message = feedback.formDefaultMessage (Feedback.GENERIC_ERROR);
                }
            }catch (Exception ex) {
                message = ex.getMessage ();
            }
            resp = message;
        }
        
        out.println (resp);        
        cnxn = null;
    }
    
    //Process the HTTP Post request
    public void doPost (HttpServletRequest request,
            HttpServletResponse response) throws
            ServletException, IOException {
        doGet (request, response);
    }
    
    //Clean up resources
    public void destroy () {
    }
    
    public void forward (ServletRequest request,
            ServletResponse response) throws ServletException, IOException{
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse resp = (HttpServletResponse)response;
        doGet (req, resp);
    }
    
    public void include (ServletRequest request,
            ServletResponse response)  throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse resp = (HttpServletResponse)response;
        doGet (req, resp);
    }
    
}
