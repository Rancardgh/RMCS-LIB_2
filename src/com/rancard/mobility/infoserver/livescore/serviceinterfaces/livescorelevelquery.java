package com.rancard.mobility.infoserver.livescore.serviceinterfaces;

/*
 * livescore.java
 *
 * Created on January 28, 2007, 12:45 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */



import com.rancard.common.Feedback;
import com.rancard.mobility.common.Driver;
import com.rancard.mobility.contentserver.CPConnections;
import com.rancard.mobility.infoserver.livescore.LiveScoreFixture;
import com.rancard.mobility.infoserver.livescore.LiveScoreServiceManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
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
public class livescorelevelquery extends HttpServlet implements RequestDispatcher {
    
    //Initialize global variables
    public void init () throws ServletException {
    }
    
    //Process the HTTP Get request
    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get reeponse writer
        PrintWriter out = response.getWriter ();
        
        //get required parameters
        String subscriber = request.getParameter ("msisdn");
        String command = request.getParameter ("msg");
        String accountId = (String) request.getAttribute ("acctId");
        String keyword = request.getParameter ("keyword");
        String ack = (String) request.getAttribute ("ack");
        if(ack == null) {ack = "Your request is being processed";}
        
        CPConnections cnxn = new CPConnections ();
        String from = "406";
        System.out.println ("Livescore-level query");
        System.out.println ("command: " + command);
        
        try {
            //validate parameters
            if(subscriber == null || subscriber.equals ("")) {
                throw new Exception (Feedback.MISSING_INVALID_MSISDN);
            }
            if(accountId == null || accountId.equals ("")) {
                throw new Exception (Feedback.MISSING_INVALID_PROV_ID);
            }
            if(keyword == null || keyword.equals ("")) {
                throw new Exception (Feedback.NO_SUCH_SERVICE);
            }
            
            try {
                cnxn = CPConnections.getConnection (accountId, subscriber);
            } catch (Exception e) {
                throw new Exception (Feedback.UNSUPPORTED_NETWORK);
            }
            
            String message = "";
            
            //get fixtures based on parameters
            if (command != null && command.equalsIgnoreCase ("l")){
                out.println (ack);
                //get all leagues
                String[][] leagues = LiveScoreServiceManager.getAvailableLiveScoreServices (accountId); 
                for (int i = 0; i < leagues.length; i++) {
                    message = "Send START " + leagues[i][0].toUpperCase () + " to subscribe to updates for " + leagues[i][1] + ".";
                    try{
                        Driver.getDriver (cnxn.getDriverType (), cnxn.getGatewayURL ()).sendSMSTextMessage (subscriber, from, message, cnxn.getUsername (),
                                cnxn.getPassword (), cnxn.getConnection (), "", "0");
                    }catch(Exception e){
                        System.out.println (Feedback.TRANSPORT_ERROR);
                        throw new Exception (Feedback.TRANSPORT_ERROR);
                    }
                }
            } else if (command != null && command.equalsIgnoreCase ("g")) {
                out.println (ack);
                //get all games
                Calendar calendar = java.util.Calendar.getInstance ();
                java.sql.Date today = new java.sql.Date (calendar.getTime ().getTime ());
                String dateString = today.toString ();
                
                HashMap fixtures = LiveScoreServiceManager.viewAllActiveFixturesForDate (dateString);
                Object[] keys = fixtures.keySet ().toArray ();
                for (int i = 0; i < keys.length; i++) { //for number of leagues
                    String key = (String) keys[i];
                    ArrayList fixturesList = (ArrayList) fixtures.get (key);
                    if(fixturesList != null && fixturesList.size () != 0) {
                        for (int k = 0; k < fixturesList.size (); k++) {
                            LiveScoreFixture game = (LiveScoreFixture) fixturesList.get (k);
                            message = "Send OK " + game.getGameId () +" to 406 for live updates for " + game.getHomeTeam () + " vs " + game.getAwayTeam () + " on " +
                                    new java.util.Date (java.sql.Timestamp.valueOf (game.getDate ()).getTime ()).toString ();
                            
                            try{
                                Driver.getDriver (cnxn.getDriverType (), cnxn.getGatewayURL ()).sendSMSTextMessage (subscriber, from, message, cnxn.getUsername (),
                                        cnxn.getPassword (), cnxn.getConnection (), "", "0");
                            }catch(Exception e){
                                System.out.println (Feedback.TRANSPORT_ERROR);
                                throw new Exception (Feedback.TRANSPORT_ERROR);
                            }
                        }
                    }
                }
                today = null;
                fixtures = null;
            } else {
                //help
                out.println ("Send LS L for available leagues.\r\nSend LS G for all games playing today.");
            }
        } catch (Exception e) {
            out.println (e.getMessage ().substring (e.getMessage ().indexOf (":") + 1));
        } finally {
            cnxn = null;
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
