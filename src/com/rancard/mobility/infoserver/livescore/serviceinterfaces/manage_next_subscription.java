/*
 * manage_next_subscription.java
 *
 * Created on May 8, 2007, 10:42 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.mobility.infoserver.livescore.serviceinterfaces;


import com.rancard.mobility.infoserver.common.services.ServiceManager;
import com.rancard.mobility.infoserver.common.services.UserService;
import com.rancard.mobility.infoserver.livescore.LiveScoreServiceManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.*;
import java.io.IOException;
import com.rancard.common.Feedback;
import com.rancard.mobility.common.Driver;
import com.rancard.mobility.contentserver.CPConnections;

/**
 *
 * @author Messenger
 */
public class manage_next_subscription extends HttpServlet implements RequestDispatcher {
    
    //Initialize global variables
    public void init() throws ServletException {
    }
    
    //Process the HTTP Get request
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        //log statement
        System.out.println(new java.util.Date()+":@com.rancard.mobility.infoserver.livescore.serviceinterfaces.manage_next_subscription");
        
        //get reeponse writer
        int MILLISECS_BETWEEN_TRANSMITS = 0;
        try{
            String millis = com.rancard.util.PropertyHolder.getPropsValue("MILLIS_BETWEEN_LS_RE-REG_ATMPT");
            if(millis != null && !millis.equals("")){
                MILLISECS_BETWEEN_TRANSMITS= Integer.parseInt(millis);
            }else{
                MILLISECS_BETWEEN_TRANSMITS = 500;
            }
        }catch(Exception e){
            MILLISECS_BETWEEN_TRANSMITS = 500;;
        }
        
        java.util.Date today = new java.util.Date();
        String processId = "";
        //log statement
        System.out.println(new java.util.Date()+":renewing subscription for subscribers on MONTHLY_BILLING...");
        
        try {
            
            CPConnections cnxn = new CPConnections();
            
            //1.--------get cp account Ids and corresponding keyword for monthly subscription
            Map<String, String> accountsReSubKeywordMatrix = ServiceManager.getCPIDsForServiceType("15","9");
            
            Iterator iter = accountsReSubKeywordMatrix.keySet().iterator();
            String accountId ="";
            String kw = "";
            
            //iterate over each CP and process resubscription
            while (iter.hasNext()) {
                
                accountId = (String)iter.next();
                kw = (String)accountsReSubKeywordMatrix.get(accountId);//get all-leageu monthly subscription keyword
                
                
                //2. ------deactivate subscrption-------------
                int deactivateStatus = LiveScoreServiceManager.manageNextLivescoreSubscription(today, accountId);
                
                
                if(deactivateStatus>0){
                    
                    
                    
                    String networkPrefix ="";
                    
                    //3.-------------retrieve subscribers due for resubscription and send notification for each network-------------------
                    HashMap groupedSubscribers = ServiceManager.viewTempSubscribersGroupByNetworkPrefix(accountId, 0, today);
                    Iterator grpSubsItr = groupedSubscribers.keySet().iterator();
                    //send notification to subscribers on each network
                    while (grpSubsItr.hasNext()) {
                        
                        networkPrefix = (String)grpSubsItr.next();
                        String subscribers[] =  (String[])groupedSubscribers.get(networkPrefix);
                        //log statement
                        System.out.println(new java.util.Date()+":getting connection for network:"+networkPrefix+"...");
                        
                        
                        
                        if (subscribers != null && subscribers.length > 0) {
                            try {
                                //get connection to requesting network for this provider
                                cnxn = CPConnections.getConnection(accountId, subscribers[0].toString());
                                
                            } catch (Exception e) {
                                throw new Exception(Feedback.UNSUPPORTED_NETWORK);
                            }
                            
                            
                            //----------------compose message-------------------------------------------------------------------------
                            String message = "";
                        
                            //----------get resubscription notification message template
                            UserService us = new UserService();
                            String messageTemplate = "";
                            try {
                                us = ServiceManager.viewAllServices(accountId, "15", "11").get(0);//get resubsriptoin helper service
                            }catch (Exception e) {
                            }
                            messageTemplate = us.getDefaultMessage();
                            //free memory
                            //us = null;
                            
                            //view head livescore service
                            String from ="";
                            UserService lsHeadSrvc = new UserService();
                            try {
                                lsHeadSrvc = LiveScoreServiceManager.viewHeadLiveScoreService(accountId);
                                from = lsHeadSrvc.getKeyword();
                            }catch (Exception e) {
                                from = "406";
                            }
                            //logging statment
                            System.out.println(new java.util.Date()+":composing resubscription notification message...");
                            
                            String insertions = "lsHeadSrvcName=" + lsHeadSrvc.getServiceName()+ "&subscriptionKw=" + kw + "&shortcode=" + lsHeadSrvc.getKeyword() +
                                    "&date=" +today;
                            //logging statement
                            System.out.println(new java.util.Date()+":Message Template:"+messageTemplate);
                            message = message + com.rancard.util.URLUTF8Encoder.doMessageEscaping(insertions, messageTemplate) + ". ";
                            //logging statement
                            System.out.println(new java.util.Date()+":final message:"+message);
                            
                            
                            //---------------end-message composition-----------------
                            
                            try{
                                //--------send notification----------------------------
                                Driver.getDriver(cnxn.getDriverType(), cnxn.getGatewayURL()).sendSMSTextMessage(subscribers, from, message, cnxn.getUsername(),
                                        cnxn.getPassword(), cnxn.getConnection(), "", "0");
                            }catch(Exception e){
                                System.out.println(new java.util.Date()+":Feedback.TRANSPORT_ERROR:"+e.getMessage());
                                //throw new Exception(Feedback.TRANSPORT_ERROR);
                            }
                            //free memory
                            us =null;
                            lsHeadSrvc =null;
                            
                        } else {
                            System.out.println(new java.util.Date()+":No subscribers found for " + networkPrefix);
                        }
                        
                        
                    }//end-2nd while loop (iterate over network prefixes)
                    
                    
                    /*try{
                       // dispatch = request.getRequestDispatcher ("/livescore/all_league_reg_billing.jsp");
                    }catch(Exception e){
                        System.out.println (Feedback.ROUTE_NOTIFICATION_FAILED.substring (Feedback.ROUTE_NOTIFICATION_FAILED.indexOf (":") + 1));
                    }*/
                    
                    //delete record in temp
                    // ServiceManager.deleteTempSubscriptionRecord (msisdn, accountId, processId);
                    
                    //rest
                 /*   try{
                        Thread.sleep (MILLISECS_BETWEEN_TRANSMITS);
                    }catch (InterruptedException ie) {
                        System.out .println(new java.util.Date()+":InterruptedException:"+ie.getMessage());
                    }catch (Exception e) {
                         System.out .println(new java.util.Date()+":Error pausing LS_autor-resubscription thread:"+e.getMessage());
                    }*/
                    
                    
                }//end of status deactivation check if statement
            }//end 1st while loop
        } catch (Exception e) {
            System.out.println(new java.util.Date()+":error in Livescore auto monthly re-subscription process:"+e.getMessage());
        }
    }//end of-doGet method
    
    //Process the HTTP Post request
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws
            ServletException, IOException {
        doGet(request, response);
    }
    
    //Clean up resources
    public void destroy() {
    }
    
    public void forward(ServletRequest request,
            ServletResponse response) throws ServletException, IOException{
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse resp = (HttpServletResponse)response;
        doGet(req, resp);
    }
    
    public void include(ServletRequest request,
            ServletResponse response)  throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse resp = (HttpServletResponse)response;
        doGet(req, resp);
    }
    
}
