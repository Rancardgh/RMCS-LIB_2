package com.rancard.mobility.infoserver.livescore.serviceinterfaces;

import com.rancard.common.Feedback;
import com.rancard.mobility.contentserver.BillingManager;
import com.rancard.mobility.contentserver.CPConnections;
import com.rancard.mobility.infoserver.common.services.ServiceManager;
import com.rancard.mobility.infoserver.common.services.UserService;
import com.rancard.mobility.infoserver.livescore.*;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.*;

/**
 *
 * @author Messenger
 */
public class gamesubscription extends HttpServlet implements RequestDispatcher {
    
    //Initialize global variables
    @Override
    public void init () throws ServletException {
    }
    
    //Process the HTTP Get request
    @Override
    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get reeponse writer
        PrintWriter out = response.getWriter ();
        //UserService service = new UserService ();
        CPConnections cnxn = new CPConnections ();
        
        //String ACK = request.getParameter("ack");
        String ACK = (String) request.getAttribute ("ack");
        String kw = request.getParameter ("keyword");
        String msg = request.getParameter ("msg");
        String msisdn = request.getParameter ("msisdn");
        //String provId = request.getParameter("acctId");
        String provId = (String) request.getAttribute ("acctId");
        
        java.util.Date realDate = new java.util.Date ();
        
        //competition cannot be found because provider ID is missing
        if (provId == null || provId.equals ("")) {
            out.println (Feedback.MISSING_INVALID_PROV_ID.substring (Feedback.MISSING_INVALID_PROV_ID.indexOf (":") + 1));
            return;
        }
        //no msisdn found
        if (msisdn == null || msisdn.equals ("")) {
            out.println (Feedback.MISSING_INVALID_MSISDN.substring (Feedback.MISSING_INVALID_MSISDN.indexOf (":") + 1));
            return;
        }
        //default message for acknowledgment
        if (ACK == null || ACK.equals ("")) {
            ACK = "";
        }
        
        //logging statements
        System.out.println ("Received request to subscribe to a service");
        System.out.println ("Date received: " + java.util.Calendar.getInstance ().getTime ().toString ());
        System.out.println ("Keyword: " + kw);
        System.out.println ("Message: " + msg);
        System.out.println ("Subscriber's number: " + msisdn);
        System.out.println ("Service privoder's ID: " + provId);
        //end of logging
        
        //validate number
        try {
            if (msisdn.indexOf ("+") != -1) {
                StringBuffer sb = new StringBuffer (msisdn);
                sb.deleteCharAt (msisdn.indexOf ("+"));
                msisdn = sb.toString ();
                sb = null;
            }
            msisdn = msisdn.trim ();
            try {
                Long.parseLong (msisdn); //checks that number is a string of digits only
                msisdn = "+" + msisdn;
            } catch (NumberFormatException e) {
                throw new Exception (Feedback.MISSING_INVALID_MSISDN.substring (Feedback.MISSING_INVALID_MSISDN.indexOf (":") + 1));
            }
        } catch (Exception e) {
            out.println (e.getMessage ());
            return;
        }
        
        //continue registration
        try {
            String serviceName = "";
            //get connection to requesting network for this provider
            try {
                cnxn = CPConnections.getConnection (provId, msisdn);
            } catch (Exception e) {
                throw new Exception (Feedback.UNSUPPORTED_NETWORK);
            }
            
            String keyword = "";
            LiveScoreFixture lsf = null;
            try {
                lsf = LiveScoreServiceManager.viewFixture (msg);
                keyword = LiveScoreServiceManager.getKeywordForService (lsf.getLeagueId (), provId);
                if(keyword == null || keyword.equals ("")){
                    throw new Exception (Feedback.NO_SUCH_SERVICE);
                }
            } catch (Exception e) {
                throw new Exception (Feedback.NO_SUCH_SERVICE);
            }
            
            UserService us = null;
            if (msg != null && !msg.equals ("")) {
                try{
                    us = ServiceManager.viewService (keyword, provId);
                    serviceName = us.getServiceName ();
                }catch(Exception ex) {
                    throw new Exception (Feedback.NO_SUCH_SERVICE);
                }
            }
            
            boolean billed = false;
            if(cnxn.getBillingMech ().equals (BillingManager.SHORTCODE_OPTION)){
                //do billing using SMSC
                //logging statements
                System.out.println ("do billing using SMSC...");
                //end of logging
                String messageString = us.getAccountId () + " " + msg;
                billed = BillingManager.doShortCodeBilling (us.getServiceType (), cnxn, cnxn.getAllowedNetworks ().get (0).toString (), msisdn, messageString,
                        keyword, us.getAccountId ());
            }else if(cnxn.getBillingMech ().equals (BillingManager.PIN_OPTION)) {
                /*//deduct from voucher value
                //logging statements
                System.out.println ("deduct from voucher value...");
                //end of logging
                billed = BillingManager.doPinRedemption (us.getServiceType (), us.getAccountId (),  cnxn.getAllowedNetworks ().get (0).toString (), pin, us.getKeyword ());
                if(billed){
                    //logging statements
                    System.out.println ("billing successful. Initiating download....");
                    //end of logging
                    initiateDownload (baseUrl, ticket);
                }else{
                    //logging statements
                    System.out.println ("billing ws NOT successful");
                    //end of logging
                }*/
            }
            
            //check if billed
            if(billed == false){
                //logging statements
                ACK = "Billing process ws not successful. Subscription could not be completed.";
                System.out.println ("billing ws NOT successful");
                //end of logging
                throw new Exception (Feedback.BILLING_MECH_FAILURE);
            }
            
            String provName = new com.rancard.mobility.contentprovider.User ().viewDealer (provId).getName ();
            String insertions = "ack=" + ACK + "&keyword=" + kw + "&msg=" + msg + "&msisdn=" + msisdn + "&provName=" + provName + "&srvcName=" + serviceName;;
            ACK = com.rancard.util.URLUTF8Encoder.doMessageEscaping (insertions, ACK);
            
            out.println (ACK);
            
            cnxn = null;
            realDate =  null;
            
        } catch (Exception e) {
            out.println (e.getMessage ().substring (e.getMessage ().indexOf (":") + 1));
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
