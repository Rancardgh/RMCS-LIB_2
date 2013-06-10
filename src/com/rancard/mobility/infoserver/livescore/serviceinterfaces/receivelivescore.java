package com.rancard.mobility.infoserver.livescore.serviceinterfaces;

import com.mullassery.imaging.java2d.Java2DImagingTest;
import com.rancard.common.Feedback;
import com.rancard.mobility.infoserver.common.services.ServiceManager;
import com.rancard.mobility.infoserver.livescore.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.dom4j.*;

//import com.google.api.translate.Language;
//import com.google.api.translate.Translate;

public class receivelivescore extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html";
    private String mode = "";
    private Feedback feedback_en = null;
    private Feedback feedback_fr = null;
    private String feedType ="";
    /*data structure for monitoring all livescore (delta) updates to ensure
     *updates are not repeated
     *key - gameId
     *value - Arraylist of lives updates for the game/fixture/match
     */
    private HashMap <String, ArrayList > updateManager = null;
    private HashMap <String, java.util.Date > expiredGamesManager = null;
    
    
    //Initialize global variables
    public void init() throws ServletException {
        /*
        resultTypes = (HashMap) this.getServletContext ().getAttribute ("resultTypes");
        periodCodes = (HashMap) this.getServletContext ().getAttribute ("periodCodes");
        statusCodes = (HashMap) this.getServletContext ().getAttribute ("statusCodes");
         */
        
        feedback_en = (Feedback) this.getServletContext().getAttribute("feedback_en");
        if(feedback_en == null){
            try{
                feedback_en = new Feedback();
            }catch(Exception e){
            }
        }
        feedback_fr = (Feedback) this.getServletContext().getAttribute("feedback_fr");
        if(feedback_fr == null){
            try{
                feedback_fr = new Feedback();
            }catch(Exception e){
            }
        }
        
        //initialize update manager
        updateManager = new HashMap();
        expiredGamesManager = new HashMap();
        
    }
    
    //Process the HTTP Get request
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        
        
        //for processing http post
        PrintWriter pw = null;
        BufferedReader br = null;
        //execute method
        HttpClient client = null;
        GetMethod httpGETFORM =  null;
        
        String svrAddr = (String) this.getServletContext().getInitParameter("contentServerUrl");
        boolean dopost = true;  //default: get feed via HTTP post
        String resp = "";
        ArrayList<LiveScoreUpdate> updateList = null;
        
        String feedURL = "";
        String param_feedType="0";
        String type = request.getParameter("type");
        String isLocalTest = request.getParameter("localTest");
        //scorerader Livescore URL (for HTTP GET)
        feedURL =  "http://194.19.15.164/livescore/getXml2.php?username=Rancard&key=264dbf25ba132dbf183e3c30e5eee822&type=";
        
        if(type!=null && !"".equals(type)){
            param_feedType = type;
            dopost = false;
            
            //check if testing locally
            if(isLocalTest!=null && "true".equals(isLocalTest) ){
                param_feedType ="";
                if("1".equals(type))
                    feedURL = svrAddr+"livescoretest/livescore_lang_delta.xml";//raw delta xml (for local testing)
                else{
                    feedURL = svrAddr+"livescoretest/livescore_lang.xml";//raw full xml (for local testing)
                }
            }
            
        }
        
        
        
        
        
        try {
            
            String status = "error";
            String reply = "";
            String error = "";
            String responseString = "";
            pw = response.getWriter();
            
            InputStream responseBody = null;
            
            if (dopost){ //process feed from HTTP post request
                //logging statement
                System.out.println(new Date()+":Livescore Feed: HTTP Post request...");
                //responseBody = request.getInputStream();
                //get post data
                resp = request.getParameter("data");
                //check null
                resp = (resp==null)? "" : resp;
                
            }else{
                try {
                    //process feed from HTTP get request
                    feedURL +=param_feedType;
                    //logging statement
                    System.out.println(new Date()+":Livescore Feed: HTTP Get request...\nScoreradarFeedURL:"+feedURL);
                    
                    client = new HttpClient();
                    httpGETFORM = new GetMethod(feedURL);
                    
                    //get response body
                    client.executeMethod(httpGETFORM);
                    responseBody = httpGETFORM.getResponseBodyAsStream();
                    
                    br = new BufferedReader(new InputStreamReader(
                            responseBody));
                    String line = ""; //br.readLine();
               /* while(line != null){
                    responseString = responseString + line+"\n";
                    line = br.readLine();
                }*/
                    
                    while ((line = br.readLine()) != null) {
                        resp = resp + line + "\n";
                        
                    }
                    
                    
                } catch (IOException e) {
                    throw new Exception(e.getMessage());
                }catch (Exception e){
                    throw new Exception(e.getMessage());
                } finally {
                    br.close();
//in.close ();
                    br = null;
//in = null;
                }
            }
//set response
            String rawXML = resp;
            if(resp.indexOf("%") !=-1)
                rawXML = resp.substring(resp.indexOf("%"));
            // System.out.println(new java.util.Date()+":raw ScoreRadar XML: " + rawXML);
            String processedXML = java.net.URLDecoder.decode(rawXML, "UTF-8");
            //System.out.println(new java.util.Date()+":processed ScoreRadar XML:" + processedXML);
            
            //send response OK
            if(!"".equals(resp)){
                pw.println("OK");
                if(dopost)
                    response.setStatus(response.SC_OK);
                //process notification
                updateList =  processResponse(processedXML);
            }else{
                pw.println("NO FEED RECEIVED");
                if(dopost)
                    response.setStatus(response.SC_BAD_REQUEST);
                //logging statement
                System.out.println(new Date()+":NO FEED RECEIVED from Scoreradar");
            }
            
            
        } catch (HttpException e) {
            
//logging statements
            System.out.println(new Date()+":HTTPException: " +  e.getMessage());
//end of logging
        } catch (IOException e) {
            
//logging statements
            System.out.println(new Date()+":IOException : " + e.getMessage());
//end of logging
        } catch (Exception e) {
//logging statements
            System.out.println(new Date()+":ERROR: " + e.getMessage());
//end of logging
        } finally {
            if(!dopost){
                // Release the connection for get request.
                //System.out.println(new java.util.Date()+":responseXML:\n"+resp);
                httpGETFORM.releaseConnection();
                client = null;
                httpGETFORM = null;
                
            }
        }
        
        
        
        //------------process updates & send alert-----------------------------------
        LiveScoreUpdate update = null;
        
        if(updateList!=null){
            for(int i=0; i < updateList.size(); i++){
                
                update = updateList.get(i);
                
                //logging statements
                System.out.println(new java.util.Date()+":Received XML from Scoreradar for " + update.getLeagueName());
                System.out.println(new java.util.Date()+":Event ID " + update.getEventId());
                System.out.println(new java.util.Date()+":Event Update ID " + update.getUpdateId());
                System.out.println(new java.util.Date()+":Event Name " + update.getEventName());
                System.out.println(new java.util.Date()+":Event Status " + update.getEventStatus());
                System.out.println(new java.util.Date()+":Feed Type: " + update.getMode() );
                //  System.out.println("------------------------------------------------------------------------");
                
                try{
                    if (update != null) {
                        
                        
                        LiveScoreFixture game = new LiveScoreFixture();
                        //get fixture for this update
                        LiveScoreFixture temp_game = LiveScoreServiceManager.viewFixture(update.getEventId());
                        
                        //---update fixture details with new update-----------------------
                        game.setCountryName(update.getCountryName());
                        //format date
                        String eventdate = update.getEventDate().replaceAll("T"," ");
                        //eventdate = eventdate.substring(0, eventdate.indexOf("+"));
                        game.setDate(eventdate);
                        game.setGameId(update.getEventId());
                        game.setLeagueName(update.getLeagueName());
                        game.setLeagueId(update.getLeagueId());
                        
                        game.setHomeTeam((String) update.getParticipants().get(0));
                        
                        String homescore = (String) update.getScores().get(0);
                        if(homescore == null || homescore.equals("")){homescore = "NA";}
                        game.setHomeScore(homescore);
                        
                        game.setAwayTeam((String) update.getParticipants().get(1));
                        
                        String awayscore = (String) update.getScores().get(1);
                        if(awayscore == null || awayscore.equals("")){awayscore = "NA";}
                        game.setAwayScore(awayscore);
                        
                        if (update.getEventStatus().equals("0")){
                            game.setStatus(LiveScoreFixture.NOT_PLAYED);
                        }else if (update.getEventStatus().equals("100") || update.getEventStatus().equals("110") || update.getEventStatus().equals("120")){
                            game.setStatus(LiveScoreFixture.PLAYED);
                        }else if ( isActive(update.getEventStatus()) ){
                            game.setStatus(LiveScoreFixture.ACTIVE);
                        }else if (update.getEventStatus().equals("80")){
                            game.setStatus(LiveScoreFixture.INTERRUPTED);
                        }else if (update.getEventStatus().equals("70")){
                            game.setStatus(LiveScoreFixture.CANCELLED);
                        }else if (update.getEventStatus().equals("60") ){
                            game.setStatus(LiveScoreFixture.POSTPONED);
                        }else{
                            game.setStatus(LiveScoreFixture.OTHER);
                        }
                        
                        //set event notif status
                        game.setEventNotifSent(temp_game.getEventNotifSent());
                        temp_game = null;
                        
                        
                        java.util.Calendar now = java.util.Calendar.getInstance();
                        java.util.Calendar gameTime = java.util.Calendar.getInstance();
                        gameTime.setTime(new java.util.Date(java.sql.Timestamp.valueOf(game.getDate()).getTime()));
                        gameTime.add(Calendar.MINUTE, -10);
                        
                        if(now.before(gameTime) || "full".equalsIgnoreCase(update.getMode().trim()) || "future".equalsIgnoreCase(update.getMode().trim())){
                            //store game
                            System.out.println(new java.util.Date()+":Creating fixture for " + game.getHomeTeam() + " vs " + game.getAwayTeam());
                            LiveScoreServiceManager.createFixture(game);
                            game = null;
                        }else{//is delta feed
                            
                            ArrayList pairing = LiveScoreServiceManager.getAccount_KeywordPairForService(game.getLeagueId());
                            if(update.getEventStatus().equals("0") && update.getTrigger().equals("")) {
                                //don't transmit alert
                            }else if( !isAlertStatus(update.getEventStatus()) ){
                                //update fixture
                                LiveScoreServiceManager.updateFixture(game);
                            }else{
                                
                                //game hasn't ended - create update message
                                //create update
                                String englishMessage = createMessage(update, feedback_en);
                                String frenchMessage = createMessage(update, feedback_fr);
                                
                                //-------eliminate update repeatition--------------------------
                                boolean sendupdate = true;
                                //check if game in expired-games list
                                if ( expiredGamesManager.containsKey(game.getGameId()) ){
                                    System.out.println(new java.util.Date()+":Closing update for "+game.getGameId()+" sent already. donnot send!");
                                    sendupdate = false;
                                    
                                }else{//game not expired. process
                                    //retrieve updatList for this game
                                    ArrayList<Integer> updateHashList = null;
                                    updateHashList =  updateManager.get(game.getGameId());
                                    int updateMsgKey = englishMessage.hashCode();
                                    
                                    //if game not in updateManager, add
                                    if(updateHashList==null){
                                        //logging statements
                                        System.out.println(new java.util.Date()+":fixture ("+game.getGameId() +") not in update updateManager. creating entry...");
                                        
                                        //create new entry for this game
                                        updateHashList = new ArrayList();
                                        updateHashList.add(updateMsgKey);
                                        updateManager.put(game.getGameId(),updateHashList); //add entry to HashMap
                                        //send update
                                    }else{//entry exist for game
                                        //chech if update already exists
                                        if (updateHashList.contains(updateMsgKey)){
                                            sendupdate = false;
                                            //logging statements
                                            System.out.println(new java.util.Date()+":update with hashKey "+updateMsgKey+"already exists for gameId:"+game.getGameId() +":  Donot transmit alert!");
                                            System.out.println(new java.util.Date()+":current size of updateHashList for gameId:"+game.getGameId() +": "+ updateHashList.size());
                                            
                                        }else{ //add message to list
                                            //logging statements
                                            System.out.println(new java.util.Date()+":adding new update: hashKey="+updateMsgKey+": gameId="+game.getGameId() +": updateHashList size:"+ updateHashList.size()+". Transmit alert!");
                                            updateHashList.add(updateMsgKey);
                                            updateManager.put(game.getGameId(), updateHashList); //update updateManager
                                            //logging statement
                                            System.out.println(new java.util.Date()+":size of updateHashList for gameId:"+game.getGameId() +" after adding new update: "+ updateHashList.size());
                                            
                                        }
                                        
                                    }
                                    
                                    
                                }
                                
                                if(sendupdate){
                                    //message = filterMessage (message);
                                    update.setEnglishMessage(englishMessage);
                                    update.setFrenchMessage(frenchMessage);
                                    
                                    //update fixture
                                    LiveScoreServiceManager.updateFixture(game);
                                    
                                    //create update
                                    if(LiveScoreServiceManager.createUpdate(update) == true) {
                                        System.out.println(new java.util.Date()+":created update record for " + update.getUpdateId());
                                        request.setAttribute("updateId", update.getUpdateId());
                                        
                                        for (int j = 0; j < pairing.size(); j++) {
                                            String pair = (String) pairing.get(j);
                                            String accountId = pair.split("-")[0];
                                            
                                            LiveScoreServiceManager.sendUpdate(svrAddr, accountId, update.getUpdateId());
                                            System.out.println(new java.util.Date()+":>>>>>>>>>>>>>>>>>>>accountId: " + accountId + " >>>>>>>>>>>>>>>>>>>updateId: " + update.getUpdateId());
                                        }
                                    }else{
                                        System.out.println(new java.util.Date()+":could NOT create update record for " + update.getUpdateId());
                                    }
                                    
                                }
                                //remove game record from updateManager if game has ended
                                if(game.getStatus()==LiveScoreFixture.PLAYED){
                                    
                                    //logging statement
                                    System.out.println(new java.util.Date()+":Event "+update.getEventName()+" with gameId:"+game.getGameId() +" has ended. removing entry from updateManager...");
                                    System.out.println(new java.util.Date()+":No. of entries in updateManager before:"+ updateManager.size());
                                    
                                    updateManager.remove(game.getGameId());
                                    //logging statement
                                    System.out.println(new java.util.Date()+":No. of entries in updateManager after:"+ updateManager.size());
                                    
                                    //add to expired game manager
                                    expiredGamesManager.put(game.getGameId(), new java.util.Date());
                                    System.out.println(new java.util.Date()+":No. of entries in expiredGamesManager:"+ expiredGamesManager.size());
                                    
                                    //deactivate subscribers for ended game
                                    LiveScoreServiceManager.updateSubscriptionStatus(game.getGameId(), 0, LiveScoreFixture.PLAYED);
                                    
                                }
                                
                                
                            }
                            
                            
                        }
                        
                        //free memory
                        game = null;
                        
                        
                    }else{
                        System.out.println(new java.util.Date()+":XML_PARSER_ERROR:");
                        throw new Exception(Feedback.XML_PARSER_ERROR);
                    }
                    
                    System.out.println("------------------------------------------------------------------------");
                }   catch (Exception e) {
                    System.out.println(new java.util.Date()+":ERROR:"+e.getMessage());
                    pw.println(e.getMessage());
                } finally {
                    if (pw != null) {
                        pw.close();
                    } // end of if (out != null)
                    try {
                        br.close();
                    } catch (Exception e) {
                    } // end of try-catch
              /*  try {
                   // isr.close();
                } catch (Exception e) {
                } // end of try-catch
               */
                    update = null;
                    
                }
                
                
            }//end for loop
        }
        //free memory
        updateList = null;
        
        //------------manage epiredGames-------------
        manageExpiredGames();
        
        
        
    }//end doGet
//Process the HTTP Post request
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
    
//Clean up resources
    public void destroy() {
    }
    
    
    
    
//============================Custom Helper Methods==============================
    
/*
    public void treeWalk(Element element) {
        for ( int i = 0, size = element.nodeCount(); i < size; i++ ) {
            Node node = element.node(i);
            if ( node instanceof Element ) {
                treeWalk( (Element) node );
            } else {
                // do something....
 
                System.out.println(new java.util.Date()+":Node Name:"+   node.getName());
                System.out.println(new java.util.Date()+":Node Type:"+   node.getNodeTypeName());
                System.out.println(new java.util.Date()+":Node XPath:"+   node.getPath() );
                System.out.println(new java.util.Date()+":Node StringValue:"+   node.getStringValue());
                System.out.println(new java.util.Date()+":Node Text:"+ node.getText());
            }
        }
    }*/
    private void manageExpiredGames(){
        //log statement
        System.out.println(new java.util.Date()+":managing expired games...");
        System.out.println(new java.util.Date()+":size of expiredGamesManager before:"+ expiredGamesManager.size());
        
        Object[] keys = expiredGamesManager.keySet().toArray();
        
        for (int i=0; i < keys.length; i++){
            String gameId = (String) keys[i];
            
            //get time ended
            java.util.Date gameEndtime = expiredGamesManager.get(gameId);
            //get current time
            java.util.Date currentTime = new java.util.Date();
            
            final long MILL_SEC_IN_1HOUR = 3600000;
            final long EXPIRE_HOURS = 24;
            long numOfHours = 0;
            long timeDiffMillisc = 0;
            
            
            //remove game from hashmap if expired 12 hours old
            timeDiffMillisc = currentTime.getTime() - gameEndtime.getTime();
            numOfHours = timeDiffMillisc/MILL_SEC_IN_1HOUR;
            
            //log statement
            System.out.println(new java.util.Date()+":expired game management: gameId: "+ gameId);
            System.out.println(new java.util.Date()+":expired game management: timeDiff in millisc: "+ timeDiffMillisc);
            System.out.println(new java.util.Date()+":expired game management: timeDiff in hours: "+ numOfHours);
            
            if (numOfHours >= EXPIRE_HOURS){
                //log statement
                System.out.println(new java.util.Date()+":game "+gameId+" expired " + numOfHours + " hrs ago. flush...");
                expiredGamesManager.remove(gameId);
            }
            //free memory
            currentTime = null;
        }
        System.out.println(new java.util.Date()+":size of expiredGamesManager after:"+ expiredGamesManager.size());
        System.out.println(new java.util.Date()+":expired game management done!");
        
        
    }
    private  ArrayList<LiveScoreUpdate>  processResponse(String xmlDoc) {
        
        
        String description = "";
        String score = "";
        
        //define custom non-scoreradar statuses
        final String RMCS_CUSTOM_STATUS_SUBSTITUTION = "150";
        final String RMCS_CUSTOM_STATUS_BOOKING = "151";
        final String RMCS_CUSTOM_STATUS_GOAL = "152";
        
        final String DEFAULT_RESULT_CODE = "200";
        
        //fixture properties
        String sportName = "";
        String countryName = "";
        String leagueName = "";
        String leagueId ="";
        String gameId = "";
        String homeTeam ="";
        String awayTeam ="";
        String gameTime ="";
        String status ="";
        ArrayList<String> participants = null;
        ArrayList<String> scores = null;
        String lsTrigger = "";
        ArrayList<LiveScoreUpdate> updateList = new ArrayList();
        LiveScoreUpdate update = null;
        String mode = "";
        
        try {
            
            //get root - scoreradar
            Document xml = org.dom4j.DocumentHelper.parseText(xmlDoc);
            Element rootNode = xml.getRootElement(); //Root= BetradarLivescoreData
            Element element = null;
            //  Element tempElement = null;
            Attribute attribute = null;
            int count = 0;
            Iterator tempItr = null;
            Element tempEl = null;
            String language = "";
            
            
            
            
            //Logging statement
            System.out.println(new java.util.Date()+":About to consume feed...");
            
            // treeWalk(rootNode);
            
            //get feed type (full/delta)
            if(rootNode!=null && rootNode.getName().equals("BetradarLivescoreData")){
                mode = rootNode.attribute("type").getValue();
            }
            // iterate through child elements of root
            for ( Iterator i = rootNode.elementIterator(); i.hasNext(); ) {
                System.out.println(new java.util.Date()+":Processing starts...");
                
                element = (Element) i.next();
                
                sportName = element.element("Name").getText();
                
                
                //------Iterate over Categories---------------------------
                Iterator j = element.elementIterator("Category");
                
                while (j.hasNext()){
                    Element subelement = (Element)j.next(); //category
                    
                    //get Category/Country name per language
                    tempItr = subelement.elementIterator("Name");
                    while(tempItr.hasNext()){
                        tempEl = (Element)tempItr.next();
                        language = tempEl.attribute("language").getValue();
                        if("en".equals(language) ){
                            countryName = tempEl.getText(); //get CountryName in english
                            break;
                        }
                    }
                    
                    // countryName = subelement.element("Name").getText();
                    
                    //---------------------iterate over Tournaments--------------------------
                    Iterator k = subelement.elementIterator("Tournament");
                    while(k.hasNext()){
                        
                        Element subelement2 = (Element)k.next();
                        
                        leagueId = subelement2.attribute("BetradarTournamentId").getValue();
                        
                        //get tournament/league name per language------------------
                        // leagueName = subelement2.element("Name").getText();
                        tempItr = subelement2.elementIterator("Name");
                        while(tempItr.hasNext()){
                            tempEl = (Element)tempItr.next();
                            language = tempEl.attribute("language").getValue();
                            if("en".equals(language) ){
                                leagueName = tempEl.getText(); break; }
                        }
                        
                        
                        Iterator L = subelement2.elementIterator("Match");
                        //-----------------iterate over Matches----------------------------
                        while(L.hasNext()){
                            count ++;
                            Element subelement3 = (Element)L.next();
                            
                            //declare variables
                            int latestResultTime = 0;
                            String period_en = "";//english.getLiveScorePeriodCode (attribute.getValue ());
                            String period_fr = "";//french.getLiveScorePeriodCode (attribute.getValue ());
                            String resultType_en ="";
                            String resultType_fr = "";
                            String resultDescription_en = "";
                            String resultDescription_fr = "";
                            String resultCode = DEFAULT_RESULT_CODE;//default result: Goal score by home team
                            String score24_status ="";
                            
                            //gameId = subelement3.attribute("BetradarMatchId").getValue(); //get gameId. BetradarMatchId can have duplicates for the same fixture
                            gameId = subelement3.attribute("Id").getValue(); //Id attribute is always unique (updated 27th June 08)
                            gameTime = subelement3.element("MatchDate").getText(); //get gameTime
                            
                            //-------get teams per language-------------------------------
                            
                            tempItr = subelement3.element("Team1").elementIterator();
                            //get hometeam
                            while(tempItr.hasNext()){
                                tempEl = (Element)tempItr.next();
                                language = tempEl.attribute("language").getValue();
                                if("en".equals(language) ){
                                    homeTeam = tempEl.getText(); break;}
                            }
                            //get awayTeam
                            tempItr = subelement3.element("Team2").elementIterator();
                            while(tempItr.hasNext()){
                                tempEl = (Element)tempItr.next();
                                language = tempEl.attribute("language").getValue();
                                if("en".equals(language) ){
                                    awayTeam = tempEl.getText(); break;} //get hometeam
                            }
                            
                            //set participants
                            participants = new ArrayList();
                            participants.add(homeTeam);
                            participants.add(awayTeam);
                            
                            //get status
                            status = subelement3.element("Status").attribute("Code").getValue(); //get status
                            
                            
                            //iterate over scores
                            Iterator m = subelement3.element("Scores").elementIterator();
                            scores = new ArrayList();
                            scores.add("NA");//no score yet, game not started
                            scores.add("NA");//no score yet, game not started
                            String homeScore ="";
                            String awayScore ="";
                            //  System.out.println("doing scores");
                            Element innerElement = null;
                            while(m!=null && m.hasNext()){
                                innerElement =(Element)m.next();
                                //System.out.println("inside score loop..");
                                if( "Current".equals(innerElement.attribute("type").getValue()) ){
                                    homeScore = innerElement.element("Team1").getText();
                                    awayScore = innerElement.element("Team2").getText();
                                    
                                    //set scores
                                    scores.set(0, homeScore);
                                    scores.set(1, awayScore);
                                    //set default result description
                                    //resultDescription_en = homeScore;
                                    //resultDescription_fr = homeScore;
                                    break;
                                }
                            }
                            
                            
                            //---------------get latest goal-------------------------------
                            m = subelement3.element("Goals").elementIterator();
                            //int prevTime = 0;
                            int currTime =0;
                            while(m!=null && m.hasNext()){
                                innerElement =(Element)m.next();
                                
                                currTime = Integer.parseInt( innerElement.element("Time").getText() );
                                if( currTime > latestResultTime){
                                    latestResultTime = currTime;
                                    
                                    //compose message for new goal
                                    String player = innerElement.element("Player").getText();
                                    resultDescription_en = player+", "+ currTime +" min. "+ homeScore +"-" + awayScore;
                                    resultDescription_fr = resultDescription_en;
                                    
                                    //set score24 result type = Goal score, player
                                    resultCode = "400";
                                    
                                     //set update status
                                     if(!isHighPriorityStatus(status)){
                                          status = RMCS_CUSTOM_STATUS_GOAL;
                                        
                                    }
                                }
                                
                            }
                            
                            //-----get latest booking (cards)-------------------------------
                            m = subelement3.element("Cards").elementIterator();
                            while(m!=null && m.hasNext()){
                                innerElement =(Element)m.next();
                                
                                currTime = Integer.parseInt( innerElement.element("Time").getText() );
                                if( currTime > latestResultTime){
                                    latestResultTime = currTime;
                                    
                                    //compose message for new Booking
                                    String player = innerElement.element("Player").getText();
                                    resultDescription_en = player+", "+ currTime +" min.";
                                    resultDescription_fr = resultDescription_en;
                                    
                                    //set score24 result type = Red card / Warning (Yellow card)
                                    String cardType = innerElement.attribute("type").getValue();
                                    resultCode = cardType.equalsIgnoreCase("Red") ? "420" : "410";
                                    
                                    //set update status
                                     if(!isHighPriorityStatus(status)){
                                          status = RMCS_CUSTOM_STATUS_BOOKING;
                                        
                                    }
                                 
                                }
                                
                            }
                            //get latest substitutions if any
                            m = subelement3.element("Substitutions").elementIterator();
                            while(m!=null && m.hasNext()){
                                innerElement =(Element)m.next();
                                
                                currTime = Integer.parseInt( innerElement.element("Time").getText() );
                                if( currTime > latestResultTime){
                                    latestResultTime = currTime;
                                    
                                    //compose message for new substitution
                                    String playerOut = innerElement.element("PlayerOut").getText();
                                    String playerIn =  innerElement.element("PlayerIn").getText();
                                    int teamNo =  Integer.parseInt( innerElement.element("PlayerTeam").getText() );
                                    String playerTeam = (teamNo==1) ? homeTeam : awayTeam;
                                    
                                    resultDescription_en = "by "+ playerTeam+ ": " +playerIn+" in for " + playerOut + "," + currTime +" min.";
                                    resultDescription_fr = resultDescription_en;
                                    
                                    //set result type = Substitution
                                    resultCode = "800";//added new result type to properties file
                                    
                                    //set update status
                                    if(!isHighPriorityStatus(status)){
                                        status = RMCS_CUSTOM_STATUS_SUBSTITUTION;
                                        
                                    }
                                }
                                
                            }
                            
                            
                            //get lineups if any
                            
                            //------set normal result------------------------
                            
                            //case match started
                            if("20".equals(status) ){
                                resultCode = "500";//
                                score24_status = "1100";
                                resultDescription_en =  feedback_en.getLiveScoreStatusCode(score24_status);
                                resultDescription_fr =  feedback_fr.getLiveScoreStatusCode(score24_status);
                            }
                            //case half time
                            if("31".equals(status) ){
                                resultCode = "500";//
                                score24_status = "1910";
                                resultDescription_en =  feedback_en.getLiveScoreStatusCode(score24_status);
                                resultDescription_fr =  feedback_fr.getLiveScoreStatusCode(score24_status);
                            }
                            
                            
                            //case match ends
                            if("100".equals(status) || "110".equals(status) ||"120".equals(status) ){
                                resultCode = "500";//
                                score24_status = "1500";
                                resultDescription_en =  feedback_en.getLiveScoreStatusCode(score24_status);
                                resultDescription_fr =  feedback_fr.getLiveScoreStatusCode(score24_status);
                            }
                            //-------set result Type Description--------------
                            resultType_en = feedback_en.getLiveScoreResultType(resultCode);
                            resultType_fr = feedback_fr.getLiveScoreResultType(resultCode);
                            
                            lsTrigger = "LS_RT_"+resultCode; //set trigger
                            
                            String msg_en ="";
                            String msg_fr ="";
                            
                            //-------set update message--------------
                            if(!DEFAULT_RESULT_CODE.equals(resultCode)){//normal score update
                                
                                msg_en = resultType_en + ": " + resultDescription_en;
                                msg_fr = resultType_fr + ": " + resultDescription_fr;
                            }
                            //--------set update properties-------------
                            update = new LiveScoreUpdate();
                            
                            update.setCountryName(countryName);
                            update.setEventId(gameId);
                            update.setEventDate(gameTime);
                            update.setLeagueName(leagueName);
                            update.setLeagueId(leagueId);
                            update.setEventName(homeTeam+" - "+awayTeam);
                            update.setEventStatus(status);
                            
                            //generate updateID
                            String uid = com.rancard.common.uidGen.getUId();
                            uid+="-"+count;
                            update.setUpdateId(uid);
                            update.setTrigger(lsTrigger);
                            update.setScores(scores);
                            update.setParticipants(participants);
                            update.setEnglishMessage(msg_en);
                            update.setFrenchMessage(msg_fr);
                            update.setMode(mode);
                            
                            
                            //print out properties
                        /*
                            System.out.println("SportName: "+sportName +"\nCountry: "+update.getCountryName() +"\nTournament: " +
                                    update.getLeagueName()+ "\nLeagueId:"+update.getLeagueId() +"\nGameId: "+update.getEventId()+"\nGameTime: "+update.getEventDate() +"" +
                                    "\nHomeTeam: "+update.getParticipants().get(0)+"\nAwayTeam: "+update.getParticipants().get(1) +"\nStatus: "+update.getEventStatus() +"" +
                                    "\nUpdateId: "+update.getUpdateId() +"\nHomeScore:"+update.getScores().get(0)+"\nAwayScore:"+update.getScores().get(1)+"" +
                                    "\nTrigger:"+update.getTrigger()+"\nMessage_en:"+update.getEnglishMessage()+"" +
                                    "\nMessage_fr:"+update.getFrenchMessage());
                         
                            System.out.println("\n============================================");*/
                            //populate update Array
                            updateList.add(update);
                            
                            //free memory
                            update = null;
                        }
                        
                        
                    }
                    
                }
                
                
            }
            
            
            
            System.out.println(new java.util.Date()+":Finished Extracting Data from feed!");
            
            
            
        } catch (Exception e) {
            
            System.out.println(new java.util.Date()+":Error Consuming Feed:" + e.getMessage());
            update = null;
        }
        
        return updateList;
    }
    
    private String getPeriod(String statusCode){
        String periodCode = "";
        
        return periodCode;
    }
    
    private void setFeedType(String feedType){
        this.feedType = feedType;
    }
    private String getFeedType(){
        return this.feedType;
    }
    
    
    private String createMessage(LiveScoreUpdate obj, Feedback language) {
        String msg = "";
        /*java.text.SimpleDateFormat df=new java.text.SimpleDateFormat ("dd-MMM-yyyy HH.mm.ss");
        String dateString = "";
         
        try{
            java.util.Date pubDate = df.parse (obj.getEventDate ());
            dateString = (new java.sql.Timestamp (pubDate.getTime ())).toString ();
         
            pubDate = null;
        }catch (Exception e) {
            //use current system time
            java.util.Calendar calendar = java.util.Calendar.getInstance ();
            dateString = calendar.getTime ().toString ();
            calendar = null;
        }
        df = null;*/
        String country = obj.getCountryName();
        String leagueName = obj.getLeagueName();
        String eventName = obj.getEventName();
        String currentScore = language.getValue("CURRENT_SCORE") + ": " + (String) obj.getParticipants().get(0) + ": " + (String) obj.getScores().get(0) + " " + (String) obj.getParticipants().get(1) +
                ": " + (String) obj.getScores().get(1);
        
        String message = "";
        if(language.getLanguage().equals("fr")){
            message = obj.getFrenchMessage();
        }else {
            message = obj.getEnglishMessage();
        }
        //important info
        if(eventName != null && !eventName.equals("")) {msg = msg + eventName + ":\r\n";}
        if(message != null && !message.equals("")) {msg = msg + message + ":\r\n";}
        if(currentScore != null && !currentScore.equals("")) {msg = msg + currentScore;}
        
        //checking message length
        if(leagueName != null && !leagueName.equals("") && (new String(msg + leagueName).length() <= 160)) {msg = leagueName + ":\r\n" + msg;}
        if(country != null && !country.equals("") && (new String(msg + country).length() <= 160)) {msg = country + ":\r\n" + msg;}
        
        return msg;
    }
    
    private String filterMessage(String message) {
        message = message.replaceAll("'","\'");
        message = message.replaceAll("`","\'");
        message = message.replaceAll("'","\'");
        return message;
    }
    
    private boolean isActive(String statusCode){
        boolean status = false;
        //get
        String POSTPONED = "60";
        String START_DELAYED = "61";
        String CANCELlED = "70";
        String INTERUPTED ="80";
        String ABANDONED ="90";
        String WALKOVER ="91";
        String RETIRED ="92";
        String ENDED = "100";
        String AET = "110";
        String AP = "120";
        String NOT_STARTED = "0";
        
        String xl[] ={POSTPONED, START_DELAYED, CANCELlED, INTERUPTED,
        ABANDONED, WALKOVER,RETIRED, RETIRED,ENDED,AET, AP, NOT_STARTED};
        
        //convert array to list
        List  inactiveList =  Arrays.asList(xl);
        
        //check status code
        if(!inactiveList.contains(statusCode))
            status = true;
        //log statement
        //System.out.println(new Date()+":Game active:"+ status);
        
        return status;
        
    }
    
    private boolean isAlertStatus(String statusCode){
        
        boolean status = true;
        
        //statuses for which alerts should not be transmited
        String POSTPONED = "60";
        String START_DELAYED = "61";
        String CANCELlED = "70";
        String INTERUPTED ="80";
        String ABANDONED ="90";
        String WALKOVER ="91";
        String RETIRED ="92";
        // String ENDED = "100";
        // String AET = "110";
        // String AP = "120";
        String NOT_STARTED = "0";
        String RMCS_CUSTOM_STATUS_SUBSTITUTION = "150";
        String RMCS_CUSTOM_STATUS_BOOKING = "151";
        
        String xl[] ={POSTPONED, START_DELAYED, CANCELlED, INTERUPTED,
        ABANDONED, WALKOVER,RETIRED, RETIRED, NOT_STARTED, RMCS_CUSTOM_STATUS_BOOKING, RMCS_CUSTOM_STATUS_SUBSTITUTION};
        
        //convert array to list
        List  nonAlertList =  Arrays.asList(xl);
        
        //check status code
        if(nonAlertList.contains(statusCode))
            status = false;
        //log statement
        //System.out.println(new Date()+":Game active:"+ status);
        
        return status;
        
    }
    
    private boolean isHighPriorityStatus(String statusCode){
        
        boolean status = false;
        
        //High priority statuses
        String STARTED = "20";
        String HALFTIME = "31";
        String GAME_END1 = "100";
        String GAME_END2 = "110";
        String GAME_END3 = "120";
        
        String xl[] ={STARTED, HALFTIME, GAME_END1, GAME_END2, GAME_END3};
        
        //convert array to list
        List  allowList =  Arrays.asList(xl);
        
        //check status code
        if(allowList.contains(statusCode)){
            status = true;
        }
        //log statement
        //System.out.println(new Date()+":Game active:"+ status);
        
        return status;
        
    }
    
}


