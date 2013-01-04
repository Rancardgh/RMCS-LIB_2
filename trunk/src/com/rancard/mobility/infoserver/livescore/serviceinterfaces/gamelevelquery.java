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
public class gamelevelquery extends HttpServlet implements RequestDispatcher {
    
    //Initialize global variables
    public void init () throws ServletException {
    }
    
    //Process the HTTP Get request
    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get reeponse writer
        PrintWriter out = response.getWriter ();
        
        //get required parameters
        String subscriber = request.getParameter ("msisdn");
        String gameId = request.getParameter ("msg");
        String accountId = (String) request.getAttribute ("acctId");
        String keyword = request.getParameter ("keyword");
        String ack = (String) request.getAttribute ("ack");
        if(ack == null) {ack = "Your request is being processed";}
        
        CPConnections cnxn = new CPConnections ();
        
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
            
            LiveScoreFixture game = null;
            try{
                game = LiveScoreServiceManager.viewFixture (gameId);
                if(game.getGameId () == null || game.getGameId ().equals ("")){
                    throw new Exception ("Specified game does not exist.");
                }
            }catch(Exception e){
                throw new Exception (ack);
            }
            //out.println (ack);
            String message = "Current score:\r\n " + game.getHomeTeam () + ": " + game.getHomeScore () + "\r\n" + game.getAwayTeam () + ": " + game.getAwayScore ();
            /*try{
                Driver.getDriver (cnxn.getDriverType (), cnxn.getGatewayURL ()).sendSMSTextMessage (subscriber, "LiveScore", message, cnxn.getUsername (),
                        cnxn.getPassword (), cnxn.getConnection (), "", "0");
            }catch(Exception e){
                System.out.println (Feedback.TRANSPORT_ERROR);
                throw new Exception (Feedback.TRANSPORT_ERROR);
            }*/
            out.println (message);
            game = null;
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
