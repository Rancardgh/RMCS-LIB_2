package com.rancard.mobility.infoserver.livescore.serviceinterfaces;

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

public class receivefeed extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html";
    private String mode = "";
    
    //Initialize global variables
    public void init () throws ServletException {
        /*
        resultTypes = (HashMap) this.getServletContext ().getAttribute ("resultTypes");
        periodCodes = (HashMap) this.getServletContext ().getAttribute ("periodCodes");
        statusCodes = (HashMap) this.getServletContext ().getAttribute ("statusCodes");
         */
    }
    
    //Process the HTTP Get request
    public void doGet (HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        
        PrintWriter pw = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        
        Feedback feedback_en = (Feedback) this.getServletContext ().getAttribute ("feedback_en");
        if(feedback_en == null){
            try{
                feedback_en = new Feedback ();
            }catch(Exception e){
            }
        }
        Feedback feedback_fr = (Feedback) this.getServletContext ().getAttribute ("feedback_fr");
        if(feedback_fr == null){
            try{
                feedback_fr = new Feedback ();
            }catch(Exception e){
            }
        }
        
        try {
            String svrAddr = (String) this.getServletContext ().getInitParameter ("contentServerUrl");
            pw = response.getWriter ();
            //receive notification
            isr = new InputStreamReader (request.getInputStream ());
            
            br = new BufferedReader (isr);
            String line = new String ();
            String resp = new String ();
            while ((line = br.readLine ()) != null) {
                resp = resp + line + "\n";
            }
            
            String rawXML = resp.substring (resp.indexOf ("%"));
            System.out.println (new java.util.Date()+":raw Score24 XML: " + rawXML);
            String processedXML = java.net.URLDecoder.decode (rawXML, "UTF-8");
            //System.out.println("processed Score24 XML: " + processedXML);
            
            //process notification
            LiveScoreUpdate update = processResponse (processedXML, feedback_fr, feedback_en);
            if (!resp.equals ("")) {
                System.out.println (new java.util.Date()+":Received XML from Score24 for " + update.getLeagueName ());
                System.out.println (new java.util.Date()+":Event ID " + update.getEventId ());
                System.out.println (new java.util.Date()+":Event Update ID " + update.getUpdateId ());
                System.out.println (new java.util.Date()+":Event Name " + update.getEventName ());
                System.out.println (new java.util.Date()+":Event Status " + update.getEventStatus ());
                System.out.println (new java.util.Date()+":Request Mode " + mode);
                
                response.setStatus (response.SC_OK);
                pw.println ("OK");
            } else {
                System.out.println (new java.util.Date()+":DID NOT receive XML from Score24");
            }
            
            if (update != null) {
                LiveScoreFixture game = new LiveScoreFixture ();
                LiveScoreFixture temp_game = LiveScoreServiceManager.viewFixture (update.getEventId ());
                
                game.setCountryName (update.getCountryName ());
                //format date
                String eventdate = update.getEventDate ().replaceAll ("T"," ");
                eventdate = eventdate.substring (0, eventdate.indexOf ("+"));
                game.setDate (eventdate);
                game.setGameId (update.getEventId ());
                game.setLeagueName (update.getLeagueName ());
                game.setLeagueId (update.getLeagueId ());
                
                game.setHomeTeam ((String) update.getParticipants ().get (0));
                
                String homescore = (String) update.getScores ().get (0);
                if(homescore == null || homescore.equals ("")){homescore = "NA";}
                game.setHomeScore (homescore);
                
                game.setAwayTeam ((String) update.getParticipants ().get (1));
                
                String awayscore = (String) update.getScores ().get (1);
                if(awayscore == null || awayscore.equals ("")){awayscore = "NA";}
                game.setAwayScore (awayscore);
                
                if (update.getEventStatus ().equals ("1000")){
                    game.setStatus (LiveScoreFixture.NOT_PLAYED);
                }else if (update.getEventStatus ().equals ("1500") || update.getEventStatus ().equals ("1420") || update.getEventStatus ().equals ("1400")){
                    game.setStatus (LiveScoreFixture.PLAYED);
                }else if (update.getEventStatus ().equals ("1100")){
                    game.setStatus (LiveScoreFixture.ACTIVE);
                }else if (update.getEventStatus ().equals ("1200")){
                    game.setStatus (LiveScoreFixture.INTERRUPTED);
                }else if (update.getEventStatus ().equals ("1300")){
                    game.setStatus (LiveScoreFixture.CANCELLED);
                }else if (update.getEventStatus ().equals ("1600") || update.getEventStatus ().equals ("1700")){
                    game.setStatus (LiveScoreFixture.POSTPONED);
                }else if (update.getEventStatus ().equals ("1800")){
                    game.setStatus (LiveScoreFixture.UNDECIDED);
                }else{
                    game.setStatus (LiveScoreFixture.OTHER);
                }
                
                game.setEventNotifSent (temp_game.getEventNotifSent ());
                temp_game = null;
                
                java.util.Calendar now = java.util.Calendar.getInstance ();
                java.util.Calendar gameTime = java.util.Calendar.getInstance ();
                gameTime.setTime (new java.util.Date (java.sql.Timestamp.valueOf (game.getDate ()).getTime ()));
                gameTime.add (Calendar.MINUTE, -10);
                
                if(mode.equalsIgnoreCase ("Manual request") || (now.before (gameTime))){
                    //store game
                    System.out.println (new java.util.Date()+":Creating fixture for " + game.getHomeTeam () + " vs " + game.getAwayTeam ());
                    LiveScoreServiceManager.createFixture (game);
                    game = null;
                } else {
                    ArrayList pairing = LiveScoreServiceManager.getAccount_KeywordPairForService (game.getLeagueId ());
                    if(update.getEventStatus ().equals ("1000") && update.getTrigger ().equals ("")) {
                        //don't transmit alert
                    } else if(update.getEventStatus ().equals ("1500")) {
                        //event is closed
                        LiveScoreServiceManager.updateFixture (game);
                    }else if (update.getEventStatus ().equals ("1420") || update.getEventStatus ().equals ("1400")){
                        LiveScoreServiceManager.updateFixture (game);
                    }else{
                        //game hasn't ended - create update message
                        //create update
                        String englishMessage = createMessage (update, feedback_en);
                        String frenchMessage = createMessage (update, feedback_fr);
                        //message = filterMessage (message);
                        update.setEnglishMessage (englishMessage);
                        update.setFrenchMessage (frenchMessage);
                        
                        //update fixture
                        LiveScoreServiceManager.updateFixture (game);
                        
                        //create update
                        if(LiveScoreServiceManager.createUpdate (update) == true) {
                            System.out.println (new java.util.Date()+":created update record for " + update.getUpdateId ());
                            request.setAttribute ("updateId", update.getUpdateId ());
                            
                            for (int j = 0; j < pairing.size (); j++) {
                                String pair = (String) pairing.get (j);
                                String accountId = pair.split ("-")[0];
                                
                                LiveScoreServiceManager.sendUpdate (svrAddr, accountId, update.getUpdateId ());
                                System.out.println (new java.util.Date()+":>>>>>>>>>>>>>>>>>>>accountId: " + accountId + " >>>>>>>>>>>>>>>>>>>updateId: " + update.getUpdateId ());
                            }
                        }else{
                            System.out.println (new java.util.Date()+":could NOT create update record for " + update.getUpdateId ());
                        }
                    }
                }
            }else{
                System.out.println (new java.util.Date()+":XML_PARSER_ERROR:");
                throw new Exception (Feedback.XML_PARSER_ERROR);
            }
        } catch (Exception e) {
            System.out.println (new java.util.Date()+":ERROR:"+e.getMessage());
            pw.println (e.getMessage ());
        } finally {
            if (pw != null) {
                pw.close ();
            } // end of if (out != null)
            try {
                br.close ();
            } catch (Exception e) {
            } // end of try-catch
            try {
                isr.close ();
            } catch (Exception e) {
            } // end of try-catch
        }
    }
    
    //Process the HTTP Post request
    public void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet (request, response);
    }
    
    //Clean up resources
    public void destroy () {
    }
    
    private LiveScoreUpdate processResponse (String xmlDoc, Feedback french, Feedback english) {
        LiveScoreUpdate update = new LiveScoreUpdate ();
        String description = "";
        String score = "";
        
        try {
            Document xml = org.dom4j.DocumentHelper.parseText (xmlDoc);
            Element rootNode = xml.getRootElement ();
            Element element = null;
            Attribute attribute = null;
            
            //get root - score24
            Iterator i = rootNode.elementIterator ();
            
            //get next element - message
            element = rootNode.element ("message");
            attribute = element.attribute ("id");;
            update.setUpdateId (attribute.getValue ());
            
            //search for relevant elements - event
            description = element.element ("description").getText ();
            this.mode = description;
            element = element.element ("event");
            
            //get desired attributes
            
            attribute = element.attribute ("id");
            update.setEventId (attribute.getValue ());
            attribute = element.attribute ("primarycountry");;
            update.setCountryName (attribute.getValue ());
            attribute = element.attribute ("primarydesc");;
            update.setLeagueName (attribute.getValue ());
            attribute = element.attribute ("primaryid");;
            update.setLeagueId (attribute.getValue ());
            attribute = element.attribute ("status");;
            update.setEventStatus (attribute.getValue ());
            
            //get name of event
            Element innerelement = null;
            innerelement = element.element ("name");
            update.setEventName (innerelement.getText ());
            
            //get event date
            innerelement = element.element ("date");
            update.setEventDate (innerelement.getText ());
            
            //get participants
            i = element.elementIterator ();
            ArrayList participants = new ArrayList ();
            ArrayList scores = new ArrayList ();
            scores.add ("NA");
            scores.add ("NA");
            int participantiterations = 0;
            while(i.hasNext ()){
                innerelement = (Element) i.next ();
                if(innerelement.getName ().equals ("participant")){
                    participants.add (innerelement.element ("name").getText ());
                    Iterator e = innerelement.elementIterator ();
                    //look thru results
                    while(e.hasNext ()){
                        Element subelement = (Element) e.next ();
                        if(subelement.getName ().equals ("result")){
                            //look for score
                            Attribute type = subelement.attribute ("type");
                            Attribute prd = subelement.attribute ("period");
                            if(type != null && type.getValue ().equals ("200") &&
                                    prd != null && prd.getValue ().equals ("90")) {
                                scores.set (participantiterations, subelement.getText ());
                            }
                            //look for trigger
                            attribute = subelement.attribute ("trigger");
                            if(attribute != null && attribute.getName ().equals ("trigger") && attribute.getValue ().equals ("yes")){
                                
                                attribute = subelement.attribute ("period");
                                //period = period + periodCodes.get (attribute.getValue ()) + ", ";
                                String period_en = english.getLiveScorePeriodCode (attribute.getValue ());
                                String period_fr = french.getLiveScorePeriodCode (attribute.getValue ());
                                
                                attribute = subelement.attribute ("type");
                                //update.setTrigger (resultTypes.get (attribute.getValue ()).toString ());
                                String resultType_en = english.getLiveScoreResultType (attribute.getValue ());
                                String resultType_fr = french.getLiveScoreResultType (attribute.getValue ());
                                update.setTrigger ("LS_RT_" + attribute.getValue ());
                                
                                Element text = subelement.element ("text");
                                String resultDescription_en = "";
                                String resultDescription_fr = "";
                                if(text != null){
                                    //update.setMessage (period + update.getTrigger () + ": " + text.getText ());
                                    if(attribute.getValue ().equals ("410") || attribute.getValue ().equals ("420")) {
                                        String value = text.getText ();
                                        resultDescription_en = value.substring (0, value.lastIndexOf (".") + 1);
                                        resultDescription_fr = value.substring (0, value.lastIndexOf (".") + 1);
                                    }else{
                                        resultDescription_en = text.getText ();
                                        resultDescription_fr = text.getText ();
                                    }
                                    text = null;
                                }else{
                                    if(description.equalsIgnoreCase ("Changed status")){
                                        //update.setMessage (period + update.getTrigger () + ": " + statusCodes.get (subelement.getText ()));
                                        resultDescription_en = english.getLiveScoreStatusCode (subelement.getText ());
                                        resultDescription_fr = french.getLiveScoreStatusCode (subelement.getText ());
                                    }else{
                                        //update.setMessage (period + update.getTrigger () + ": " + subelement.getText ());
                                        resultDescription_en = subelement.getText ();
                                        resultDescription_fr = subelement.getText ();
                                    }
                                }
                                
                                update.setEnglishMessage (period_en + ", " + resultType_en + ": " + resultDescription_en);
                                update.setFrenchMessage (period_fr + ", " + resultType_fr + ": " + resultDescription_fr);
                            }
                            
                            type = null;
                            prd = null;
                        }else if(subelement.getName ().equals ("participant")) {
                            //look for embedded result
                            Iterator sub_e = subelement.elementIterator ();
                            //look thru results
                            while(sub_e.hasNext ()){
                                //subelement = subelement.element ("result");
                                subelement = (Element) sub_e.next ();
                                if(subelement != null){
                                    //look for trigger
                                    attribute = subelement.attribute ("trigger");
                                    if(attribute != null && attribute.getName ().equals ("trigger") && attribute.getValue ().equals ("yes")){
                                        
                                        attribute = subelement.attribute ("period");
                                        //period = period + periodCodes.get (attribute.getValue ()) + ", ";
                                        String period_en = english.getLiveScorePeriodCode (attribute.getValue ());
                                        String period_fr = french.getLiveScorePeriodCode (attribute.getValue ());
                                        
                                        attribute = subelement.attribute ("type");
                                        //update.setTrigger (resultTypes.get (attribute.getValue ()).toString ());
                                        String resultType_en = english.getLiveScoreResultType (attribute.getValue ());
                                        String resultType_fr = french.getLiveScoreResultType (attribute.getValue ());
                                        update.setTrigger ("LS_RT_" + attribute.getValue ());
                                        
                                        Element text = subelement.element ("text");
                                        String resultDescription_en = "";
                                        String resultDescription_fr = "";
                                        if(text != null){
                                            //update.setMessage (period + update.getTrigger () + ": " + text.getText ());
                                            if(attribute.getValue ().equals ("410") || attribute.getValue ().equals ("420")) {
                                                String value = text.getText ();
                                                resultDescription_en = value.substring (0, value.lastIndexOf (".") + 1);
                                                resultDescription_fr = value.substring (0, value.lastIndexOf (".") + 1);
                                            }else{
                                                resultDescription_en = text.getText ();
                                                resultDescription_fr = text.getText ();
                                            }
                                            text = null;
                                        }else{
                                            if(description.equalsIgnoreCase ("Changed status")){
                                                //update.setMessage (period + update.getTrigger () + ": " + statusCodes.get (subelement.getText ()));
                                                resultDescription_en = english.getLiveScoreStatusCode (subelement.getText ());
                                                resultDescription_fr = french.getLiveScoreStatusCode (subelement.getText ());
                                            }else{
                                                //update.setMessage (period + update.getTrigger () + ": " + subelement.getText ());
                                                resultDescription_en = subelement.getText ();
                                                resultDescription_fr = subelement.getText ();
                                            }
                                        }
                                        
                                        update.setEnglishMessage (period_en + ", " + resultType_en + ": " + resultDescription_en);
                                        update.setFrenchMessage (period_fr + ", " + resultType_fr + ": " + resultDescription_fr);
                                    }
                                    subelement = null;
                                }
                            }
                        }
                    }
                    e = null;
                    participantiterations++;
                }
                innerelement = null;
                attribute = null;
            }
            update.setScores (scores);
            update.setParticipants (participants);
        } catch (Exception e) {
            update = null;
        }
        return update;
    }
    
    private String createMessage (LiveScoreUpdate obj, Feedback language) {
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
        String country = obj.getCountryName ();
        String leagueName = obj.getLeagueName ();
        String eventName = obj.getEventName ();
        String currentScore = language.getValue ("CURRENT_SCORE") + ": " + (String) obj.getParticipants ().get (0) + ": " + (String) obj.getScores ().get (0) + " " + (String) obj.getParticipants ().get (1) +
                ": " + (String) obj.getScores ().get (1);
        
        String message = "";
        if(language.getLanguage ().equals ("fr")){
            message = obj.getFrenchMessage ();
        }else {
            message = obj.getEnglishMessage ();
        }
        //important info
        if(eventName != null && !eventName.equals ("")) {msg = msg + eventName + ":\r\n";}
        if(message != null && !message.equals ("")) {msg = msg + message + ":\r\n";}
        if(currentScore != null && !currentScore.equals ("")) {msg = msg + currentScore;}
        
        //checking message length
        if(leagueName != null && !leagueName.equals ("") && (new String (msg + leagueName).length () <= 160)) {msg = leagueName + ":\r\n" + msg;}
        if(country != null && !country.equals ("") && (new String (msg + country).length () <= 160)) {msg = country + ":\r\n" + msg;}
        
        return msg;
    }
    
    private String filterMessage (String message) {
        message = message.replaceAll ("'","\'");
        message = message.replaceAll ("`","\'");
        message = message.replaceAll ("'","\'");
        return message;
    }
    
    private void sendUpdate (String serverUrl, String accountId, String updateId) throws HttpException, IOException, Exception {
        
        String url = serverUrl + "sendlivescoreupdate?acctId=" + accountId + "&updateId=" + updateId;
        
        HttpClient client = new HttpClient ();
        GetMethod httpGETFORM = new GetMethod (url);
        String resp = "";
        
        try {
            client.executeMethod (httpGETFORM);
        } catch (HttpException e) {
            resp = (Feedback.PROTOCOL_ERROR + ": " + e.getMessage ());
            //logging statements
            System.out.println ("error response: " + resp);
            //end of logging
        } catch (IOException e) {
            resp = (Feedback.TRANSPORT_ERROR + ": " + e.getMessage ());
            //logging statements
            System.out.println ("error response: " + resp);
            //end of logging
        } catch (Exception e) {
            //logging statements
            System.out.println ("error response: " + e.getMessage ());
            //end of logging
        } finally {
            // Release the connection.
            httpGETFORM.releaseConnection ();
            client = null;
            httpGETFORM = null;
        }
    }
}
