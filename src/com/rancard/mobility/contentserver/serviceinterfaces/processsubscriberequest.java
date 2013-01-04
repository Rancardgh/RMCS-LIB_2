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
import com.rancard.mobility.common.Driver;
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
public class processsubscriberequest extends HttpServlet implements RequestDispatcher {
    
    //Initialize global variables
    public void init () throws ServletException {
    }
    
    //Process the HTTP Get request
    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get reeponse writer
        PrintWriter out = response.getWriter ();
        CPConnections cnxn = new CPConnections ();
        String[] regId = new String [2];
        
        String ACK = (String) request.getAttribute ("ack");
        String kw = request.getParameter ("keyword");
        String msg = request.getParameter ("msg");
        String msisdn = request.getParameter ("msisdn");
        String provId = (String) request.getAttribute ("acctId");
        String subsPeriodStr = request.getParameter("subs_period");
        
        
        if(provId == null || provId.equals ("")){
            provId = request.getParameter ("acctId");
        }
        String cmd = (String) request.getAttribute ("cmd");
        if(cmd == null || cmd.equals ("")){
            cmd = "0";
        }
        
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
        
        String message = "";
        String provName = "";
        
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
        System.out.println (new java.util.Date()+":Received request to subscribe to a service");
        System.out.println (new java.util.Date()+":Keyword: " + kw);
        System.out.println (new java.util.Date()+":Message: " + msg);
        System.out.println (new java.util.Date()+":Subscriber's number: " + msisdn);
        System.out.println (new java.util.Date()+":Service privoder's ID: " + provId);
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
                    System.out.println (new java.util.Date()+":MISSING or INVALID_MSISDN: " + number);
                    message = feedback.getUserFriendlyDescription (Feedback.MISSING_INVALID_MSISDN);                   
                }else{
                    System.out.println (new java.util.Date()+":MISSING or INVALID_MSISDN: " + number);
                    message = feedback.formDefaultMessage (Feedback.MISSING_INVALID_MSISDN);
                }
            }catch (Exception ex) {
                System.out.println (new java.util.Date()+":ERROR: " + ex.getMessage());
                message = ex.getMessage ();
            }
            out.println (message);
            return;
        }
        
        //continue registration
        try {
            //get connection to requesting network for this provider
            try {
                cnxn = CPConnections.getConnection (provId, msisdn);
            } catch (Exception e) {
                throw new Exception (Feedback.UNSUPPORTED_NETWORK);
            }
            
            ArrayList keywords = new ArrayList ();
            String resp = new String ();            
            
            String serviceNames = "";
            if (msg != null && !msg.equals ("")) {
                //add requested service to service_subscription table
                if(!keywords.contains (msg)){
                    keywords.add (msg);
                }
            }else{
                keywords = ServiceManager.getKeywordsOfBasicServices (provId);
            }
            
            try{
                serviceNames = ServiceManager.viewService (keywords.get (0).toString (), provId).getServiceName ();
                for (int i = 1; i < keywords.size (); i++) {
                    serviceNames = serviceNames + ", " + ServiceManager.viewService (keywords.get (i).toString (), provId).getServiceName ();
                }
                StringBuffer sb = new StringBuffer (serviceNames);
                if(serviceNames.lastIndexOf (",") != -1){
                    sb.replace (serviceNames.lastIndexOf (","), serviceNames.lastIndexOf (",") + 1, " and");
                }
                serviceNames = sb.toString ();
                sb = null;
            }catch(Exception ex) {
                throw new Exception (Feedback.NO_SUCH_SERVICE);
            }
       
            
            //Register Subscriber for service(s)
            
            int numOfDays = 0;
            if(subsPeriodStr!=null && !"".equals(subsPeriodStr)){// subscribe for a period
                numOfDays = Integer.parseInt(subsPeriodStr);
                //register subscriber for specified number of days
                regId = ServiceManager.subscribeToService (msisdn, keywords, provId, numOfDays);
                
            }else{ //Default Subscription: subscribe indefinitely 
                regId = ServiceManager.subscribeToService (msisdn, keywords, provId);
            }
                
          
            
            //logging statements
            System.out.println ("Subscription completed.");
            //end of logging
            
            provName = new com.rancard.mobility.contentprovider.User ().viewDealer (provId).getName ();
            String insertions = "ack=" + ACK + "&keyword=" + kw + "&msg=" + msg + "&msisdn=" + msisdn + "&provName=" + provName + "&pin=" + regId[0] + "&srvcName=" + serviceNames;
            ACK = com.rancard.util.URLUTF8Encoder.doMessageEscaping (insertions, ACK);
            
            if(regId[1] == null){
                message = ACK;
            }else{
                message = regId[1] + " " + ACK + " ";
            }

            /* UGLY UGLY UGLY!! REMOVE THIS GUY AS SOON AS PRACTICAL */
            /*
            if ( provId.equals("000") && ( msg.equalsIgnoreCase("BBC News") || msg.equalsIgnoreCase("BBC Sports") )) {
                message = "Welcome to " + msg + " on Vodafone. You will receive daily " + msg +
                        " updates for only 7.5Gp. To unsubscribe send STOP " + msg.toUpperCase() + " to 1988";
                request.setAttribute("x-kannel-header-from", "1988");
            }
             */
            
            if(cmd.equals ("0")){
                out.println (message);
            }else if(cmd.equals ("1")) {
                try{
                    Driver.getDriver (cnxn.getDriverType (), cnxn.getGatewayURL ()).sendSMSTextMessage (msisdn, provName, message, cnxn.getUsername (),
                            cnxn.getPassword (), cnxn.getConnection (), "", "0");
                }catch(Exception e){
                    System.out.println (Feedback.TRANSPORT_ERROR);
                    throw new Exception (Feedback.TRANSPORT_ERROR);
                }
            }
            
            keywords = null;
            cnxn = null;
            
        } catch (Exception e) {
            try {
                if(siteType.equals (CPSite.SMS)){
                    message = feedback.getUserFriendlyDescription (e.getMessage ());
                }else{
                    message = feedback.formDefaultMessage (e.getMessage ());
                }
                if(message == null){
                    message = e.getMessage ();
                }
            }catch (Exception ex) {
                message = ex.getMessage ();
            }
            
            if(cmd.equals ("0")){
                out.println (message);
            }else if(cmd.equals ("1")) {
                try{
                    Driver.getDriver (cnxn.getDriverType (), cnxn.getGatewayURL ()).sendSMSTextMessage (msisdn, provName, message, cnxn.getUsername (),
                            cnxn.getPassword (), cnxn.getConnection (), "", "0");
                }catch(Exception ex){
                    System.out.println (Feedback.TRANSPORT_ERROR);
                }
            }
            return ;
        }
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
