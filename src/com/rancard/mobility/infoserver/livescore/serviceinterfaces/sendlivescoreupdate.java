/*
 * processsubscriberequest.java
 *
 * Created on October 28, 2006, 1:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.mobility.infoserver.livescore.serviceinterfaces;

import com.rancard.common.Feedback;
import com.rancard.mobility.common.Driver;
import com.rancard.mobility.contentserver.CPConnections;
import com.rancard.mobility.infoserver.common.services.ServiceManager;
import com.rancard.mobility.infoserver.common.services.UserService;
import com.rancard.mobility.infoserver.livescore.LiveScoreServiceManager;
import com.rancard.mobility.infoserver.livescore.LiveScoreUpdate;
import com.rancard.mobility.infoserver.livescore.LiveScoreFixture;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.*;

/**
 *
 * @author Messenger
 */
public class sendlivescoreupdate extends HttpServlet implements RequestDispatcher {
    
    //Initialize global variables
    public void init () throws ServletException {
    }
    
    //Process the HTTP Get request
    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get reeponse writer
        PrintWriter out = response.getWriter ();
        CPConnections cnxn = new CPConnections ();
        
        String ui = request.getParameter ("updateId");
        String accountId = request.getParameter ("acctId");
        
        String from = "";
        String kw = "";
        String alias = "";
        
        LiveScoreUpdate update = null;
        try{
            update = LiveScoreServiceManager.viewUpdate (ui);
            //updated to fix closing update repeats bug (scoreradar migration) 12th Jun 08
            //update = LiveScoreServiceManager.viewUpdate (ui, LiveScoreFixture.ACTIVE);
            System.out.println ("Received Update with ID " + update.getUpdateId ());
        }catch(Exception e) {
            out.println (Feedback.NO_SUCH_SERVICE.substring (Feedback.NO_SUCH_SERVICE.indexOf (":") + 1));
            return;
        }
        
        kw = update.getEventId ();
        try {
            alias = LiveScoreServiceManager.viewAliasForGameId (kw);
        } catch (Exception e) {            
        }
        
        if (kw == null || kw.equals ("")) {
            out.println (Feedback.NO_SUCH_SERVICE.substring (Feedback.NO_SUCH_SERVICE.indexOf (":") + 1));
            return;
        }
        
        com.rancard.mobility.contentprovider.User prov = null;
        String lang = "";
        try{
            prov = new com.rancard.mobility.contentprovider.User ().viewDealer (accountId);
            lang = prov.getDefaultLanguage ();
        }catch (Exception e) {
            lang = "en";
        }
        
        UserService lsHeadSrvc = new UserService ();
        try {
            lsHeadSrvc = LiveScoreServiceManager.viewHeadLiveScoreService (accountId);
            from = lsHeadSrvc.getServiceName ();
        }catch (Exception e) {
            from = "LiveScore";
        }
        
        //logging statements
        System.out.println ("Info to be sent for keyword: " + kw + " on account with ID: " + accountId);
        System.out.println ("Date received: " + java.util.Calendar.getInstance ().getTime ().toString ());
        //end of logging
        
        String gameUpdate = "";
        if(lang.equalsIgnoreCase ("fr")) {
            gameUpdate = update.getFrenchMessage ();
        } else {
            gameUpdate = update.getEnglishMessage ();
        }
        
        try {
            System.out.println ("Info: " + gameUpdate);
            HashMap groupedSubscribers = ServiceManager.viewSubscribersGroupByNetworkPrefix (accountId, kw, alias, 1);
            Object[] keys = groupedSubscribers.keySet ().toArray ();
            //System.out.println ("Number of networks owned by provider " + accountId + " : " + keys.length);
            for (int j = 0; j < keys.length; j++) {
                String network = keys[j].toString ();
                String[] subscribers = (String[]) groupedSubscribers.get (network);
                if (subscribers != null && subscribers.length > 0) {
                    //get connection to requesting network for this provider
                    try {
                        cnxn = CPConnections.getConnection (accountId, subscribers[0]);
                        System.out.println ("Number of subscribers from network " + cnxn.getAllowedNetworks ().get (0).toString () + " : " + subscribers.length);
                    } catch (Exception e) {
                        System.out.println ("Error getting connection: " + e.getMessage ());
                        throw new Exception (Feedback.UNSUPPORTED_NETWORK);
                    }
                    
                    try{
                        System.out.println ("sending...");
                        Driver.getDriver (cnxn.getDriverType (), cnxn.getGatewayURL ()).sendSMSTextMessage (subscribers, from, gameUpdate, cnxn.getUsername (),
                                cnxn.getPassword (), cnxn.getConnection (), "", "0");
                    }catch(Exception e){
                        System.out.println (Feedback.TRANSPORT_ERROR + " Response from gateway: " + e.getMessage ());
                        throw new Exception (Feedback.TRANSPORT_ERROR);
                    }
                } else {
                    System.out.println ("No subscribers found");
                }
            }
        } catch (Exception e) {
            System.out.println ("ERROR!! SENDING UPDATES FAILED: " + e.getMessage ());
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
