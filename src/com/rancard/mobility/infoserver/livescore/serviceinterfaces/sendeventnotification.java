/*
 * sendeventnotification.java
 *
 * Created on January 14, 2007, 11:13 PM
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
import com.rancard.mobility.infoserver.livescore.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

public class sendeventnotification extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html";
    
    //Initialize global variables
    public void init() throws ServletException {
    }
    
    //Process the HTTP Get request
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        String allowance = request.getParameter("allowance");
        
        int DEFAULT_ALLOWANCE = 0;
        try{
            String value = com.rancard.util.PropertyHolder.getPropsValue("LS_TRIGGER_ALLOWANCE");
            if(value != null && !value.equals("")){
                DEFAULT_ALLOWANCE = Integer.parseInt(value);
            }else{
                DEFAULT_ALLOWANCE = 6;
            }
        }catch(Exception e){
            DEFAULT_ALLOWANCE = 6;
        }
        
        int addition = 0;
        if(allowance == null) {
            addition = DEFAULT_ALLOWANCE;
        }else{
            addition = Integer.parseInt(allowance);
        }
        
        Calendar calendar = java.util.Calendar.getInstance();
        Calendar now = java.util.Calendar.getInstance();
        System.out.println(new java.util.Date()+":Event notification for games today. Triggered at " + calendar.getTime().toString());
        System.out.println(new java.util.Date()+":Trigger allowance: " + addition);
        calendar.add(calendar.HOUR_OF_DAY, addition);
        System.out.println(new java.util.Date()+":Looking for games around " + calendar.getTime().toString());
        java.sql.Timestamp today = new java.sql.Timestamp(calendar.getTime().getTime());
        String dateString = today.toString();
        CPConnections cnxn = new CPConnections();
        
        //logging statement
        System.out.println(new java.util.Date()+":fixture_DateTime_Search_value: "+ dateString);
        
        try {
            
            //retrieve all available fixtures for timestamp
            HashMap fixtures = LiveScoreServiceManager.viewAllFixturesForDate(dateString, LiveScoreFixture.NOT_PLAYED);
            Object[] keys = fixtures.keySet().toArray();
            
            //retrieve fixtures grouped by CP
            HashMap cpFixtureMatrix = LiveScoreServiceManager.viewAllFixturesForDateGroupByCP(dateString, LiveScoreFixture.NOT_PLAYED, LiveScoreFixture.NOTIF_NOT_SENT);
            
            //logging statement
            if(keys.length<1)
                System.out.println(new java.util.Date()+":No games were found around this time!");
            
            for (int i = 0; i < keys.length; i++) { //for number of leagues
                String key = (String) keys[i];
                ArrayList fixturesList = (ArrayList) fixtures.get(key);
                if(fixturesList.size() != 0) { //check fixture exists today
                    System.out.println(new java.util.Date()+":"+fixturesList.size() + " game(s) found for " + key + ".");
                    //logging statment
                    System.out.println(new java.util.Date()+":getting CP-LS_Keyword Mappings...");
                    ArrayList pairing = LiveScoreServiceManager.getAccount_KeywordPairForService(key);
                    for (int j = 0; j < pairing.size(); j++) {//for each CP
                        String messageTemplate = "";
                        String from = "";
                        
                        String pair = (String) pairing.get(j);
                        String accountId = pair.split("-")[0];
                        String kw = pair.split("-")[1];
                        
                        //logging statement
                        System.out.println(new java.util.Date()+":CP-LS_Keyword-Mapping: "+ accountId+":"+kw);
                        
                        ArrayList cpFixtureBasket = (ArrayList)cpFixtureMatrix.get(accountId);
                        ArrayList toSendList = new ArrayList(); //remember to destroy this object 
                        
                        //check any fixtures available for this CP
                        boolean fixtExist = false;
                        for (int k = 0; k < fixturesList.size(); k++) {
                            LiveScoreFixture game = (LiveScoreFixture) fixturesList.get(k);
                            
                            //donot include if event_notif_sent
                            if(game.getEventNotifSent()==LiveScoreFixture.NOTIF_SENT)
                                continue;
                            
                           //check if fixture exists in cp's basket
                            if( cpFixtureBasket!=null && cpFixtureBasket.contains(game.getGameId()) ){
                               //add to alertList
                                toSendList.add(game);
                                fixtExist = true;
       
                            }
                        }
                  
                        
                        if(fixtExist) {
                            
                            
                            
                            UserService us = new UserService();
                            try {
                                us = ServiceManager.viewService(kw, accountId);
                            }catch (Exception e) {
                                System.out.println(new java.util.Date()+":Default message not found for keyword: " + kw + " and account ID: " + accountId + ". Using hard-coded template...");
                                messageTemplate = "Send @@gameId@@ to 406 for @@homeTeam@@ vs @@awayTeam@@ on @@date@@";
                            }
                            
                            UserService lsHeadSrvc = new UserService();
                            try {
                                lsHeadSrvc = LiveScoreServiceManager.viewHeadLiveScoreService(accountId);
                                from = lsHeadSrvc.getAllowedShortcodes();
                            }catch (Exception e) {
                                from = "";
                            }
                            
                            messageTemplate = us.getDefaultMessage();
                        /*
                         *end of code insertion
                         */
                            
                            //get subscribers for this league 
                            
                            HashMap groupedSubscribers = ServiceManager.viewSubscribersGroupByNetworkPrefix(accountId, kw, 1);
                            Object[] subkeys = groupedSubscribers.keySet().toArray();
                            for (int l = 0; l < subkeys.length; l++) {
                                String[] subscribers = (String[]) groupedSubscribers.get(subkeys[l].toString());
                                if (subscribers != null && subscribers.length > 0) {
                                    try {
                                        //get connection to requesting network for this provider
                                        cnxn = CPConnections.getConnection(accountId, subscribers[0]);
                                        
                                        
                                    } catch (Exception e) {
                                        throw new Exception(Feedback.UNSUPPORTED_NETWORK);
                                    }
                                    
                                    //compose message
                                    String message = "";
                                    
                                    for (int k = 0; k < toSendList.size(); k++) {
                                        LiveScoreFixture game = (LiveScoreFixture) toSendList.get(k);
                                        
                                            String gameIdentifier = game.getAlias();
                                            if(gameIdentifier == null || gameIdentifier.equals("")){
                                                gameIdentifier = game.getGameId();
                                            }
                                            
                                            //convert game_time to network's local time;
                                         /*   String gameTime = "";
                                            calendar.setTimeInMillis( java.sql.Timestamp.valueOf(game.getDate()).getTime() );
                                            calendar.add(Calendar.HOUR_OF_DAY, cnxn.getUTCOffset());
                                            gameTime = new java.util.Date(calendar.getTimeInMillis()).toString();*/
                                            
                                            String gameTime = com.rancard.util.Date.toLocalTime( new java.util.Date(java.sql.Timestamp.valueOf(game.getDate()).getTime() ), cnxn );
                                            
                                            System.out.println(new java.util.Date()+":gameIdentifier: "+gameIdentifier+ ", network:" +subkeys[l] +" game_time:"+gameTime);
                                            
                                            String insertions = "gameId=" + gameIdentifier.toUpperCase()+ "&homeTeam=" + game.getHomeTeam() + "&awayTeam=" + game.getAwayTeam() +
                                                    "&date=" +gameTime;
                                            message = message + com.rancard.util.URLUTF8Encoder.doMessageEscaping(insertions, messageTemplate) + ". ";
                                        
                                    }
                                    
                                    //end-message composition
                                    
                                    try{
                                        Driver.getDriver(cnxn.getDriverType(), cnxn.getGatewayURL()).sendSMSTextMessage(subscribers, from, message, cnxn.getUsername(),
                                                cnxn.getPassword(), cnxn.getConnection(), "", "0");
                                    }catch(Exception e){
                                        System.out.println(new java.util.Date()+"Error sending notification: Feedback.TRANSPORT_ERROR:"+e.getMessage());
                                        throw new Exception(Feedback.TRANSPORT_ERROR);
                                    }
                                } else {
                                    System.out.println(new java.util.Date()+":No subscribers found for " + subkeys [l]);
                                }
                            }
                        }else{//fixtures (of current league) not available for this CP
                            System.out.println(new java.util.Date()+":No fixture for league("+kw+") found in send_notif Basket for current CP:"+accountId);
                        }
                        
                    }//-end for each cp
                    
                  
                }//--end if check fixture exists today
                
                  for (int k = 0; k < fixturesList.size(); k++) {
                        LiveScoreFixture game = (LiveScoreFixture) fixturesList.get(k);
                        game.setEventNotifSent(LiveScoreFixture.NOTIF_SENT);
                        LiveScoreServiceManager.updateFixture(game);
                  }
            }//--end for each league
            
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
        }
    }
    
    //Process the HTTP Post request
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
    
    //Clean up resources
    public void destroy() {
    }
}
