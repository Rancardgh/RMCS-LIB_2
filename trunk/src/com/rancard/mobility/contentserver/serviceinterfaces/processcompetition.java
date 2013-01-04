package com.rancard.mobility.contentserver.serviceinterfaces;

import com.rancard.common.Feedback;
import com.rancard.mobility.contentserver.CPConnections;
import com.rancard.mobility.contentserver.CPSite;
import com.rancard.mobility.infoserver.common.inbox.InboxEntry;
import com.rancard.mobility.infoserver.common.inbox.InboxManager;
import com.rancard.mobility.infoserver.smscompetition.CompetitionManager;
import com.rancard.mobility.infoserver.smscompetition.CompetitionManager;
import com.rancard.mobility.infoserver.smscompetition.EndlessOddsWithQuestion;
import com.rancard.mobility.infoserver.smscompetition.EndlessOddsCompetition;
import com.rancard.mobility.infoserver.smscompetition.FixedOddsCompetition;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class processcompetition extends HttpServlet implements RequestDispatcher {
    
    //Initialize global variables
    public void init () throws ServletException {
        
    }
    
    //Process the HTTP Get request
    public void doGet (HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        String message = "";
        //get reeponse writer
        PrintWriter out = response.getWriter ();
        
        //String ACK = request.getParameter("ack");
        String ACK = (String) request.getAttribute ("ack");
        String kw = request.getParameter ("keyword");
        if(request.getParameter ("routeBy") != null &&
                (request.getParameter ("routeBy").equals (servicelocator.BY_SHORTCODE) || request.getParameter ("routeBy").equals (servicelocator.BY_PROVIDER))){
            kw = (String) request.getAttribute ("attr_keyword");
        }
        String msg = request.getParameter ("msg");
        String dest = request.getParameter ("dest");
        String msisdn = request.getParameter ("msisdn");
        String provId = (String) request.getAttribute ("acctId");
        String date = request.getParameter ("date");
        
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
        
        com.rancard.mobility.contentserver.CPConnections cnxn =
                new com.rancard.mobility.contentserver.CPConnections ();
        com.rancard.mobility.infoserver.smscompetition.Competition comp =
                new com.rancard.mobility.infoserver.smscompetition.Competition ();
        
        String REG_REPLY = "Vote ID: ";
        String FROM = "RMCS";
        String QSTN_REPLY = "Question: ";
        String provName = "";
        
        String svrresp = new String ();
        String number = new String ();
        java.util.Date realDate = new java.util.Date ();
        
        //competition cannot be found because provider ID is missing
        if (provId == null || provId.equals ("")) {
            try {
                if(siteType.equals (CPSite.SMS)){
                    message = feedback.getUserFriendlyDescription (Feedback.MISSING_INVALID_PROV_ID);
                }else{
                    message = feedback.formDefaultMessage (Feedback.MISSING_INVALID_PROV_ID);
                }
            }catch (Exception ex) {
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
            }
            out.println (message);
            return;
        }
        //msg is empty
        if (kw == null || kw.equals ("")) {
            try {
                if(siteType.equals (CPSite.SMS)){
                    message = feedback.getUserFriendlyDescription (Feedback.NO_SUCH_COMPETITION);
                }else{
                    message = feedback.formDefaultMessage (Feedback.NO_SUCH_COMPETITION);
                }
            }catch (Exception ex) {
            }
            out.println (message);
            return;
        }
        //to parameter is empty - not a required parameter
        if (dest == null || dest.equals ("")) {
            dest = "";
        }
        //message body
        if (msg == null || msg.equals ("")) {
            msg = "";
        }
        //default message for acknowledgment
        if (ACK == null || ACK.equals ("")) {
            ACK = "";
        }
        //date received
        if (date == null || date.equals ("")) {
            realDate = java.util.Calendar.getInstance ().getTime ();
        } else {
            try{
                java.sql.Timestamp t = java.sql.Timestamp.valueOf (date);
                realDate = new java.util.Date (t.getTime ());
                t = null;
            }catch(Exception e) {
                realDate = java.util.Calendar.getInstance ().getTime ();
            }
        }
        
        //validate number
        number = msisdn;
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
            }
            out.println (message);
            return;
        }
        
        //logging statements
        System.out.println ("Received request to participate in competition");
        System.out.println ("Date received: " + java.util.Calendar.getInstance ().getTime ().toString ());
        System.out.println ("Keyword: " + kw);
        System.out.println ("Message: " + msg);
        System.out.println ("Destination number: " + dest);
        System.out.println ("Participant's number: " + msisdn);
        System.out.println ("Service privoder's ID: " + provId);
        System.out.println ("Date (from RMS): " + date);
        //end of logging
        
        com.rancard.mobility.contentprovider.User user = new com.rancard.mobility.contentprovider.User ();
        try{
            user = user.viewDealer (provId);
            
            if(user != null){
                double inboxLimit = user.getInboxBalance ();
                if(inboxLimit - 1 < 0){
                    System.out.println ("User does NOT have enough space in inbox");
                    throw new Exception (Feedback.INBOX_EXCEEDED);
                }else{
                    System.out.println ("User has enough space in inbox");
                    double result = inboxLimit - 1;
                    user.updateDealer (user.getId (), user.getBandwidthBalance (), result, user.getOutboxBalance ());
                }
            }else{
                throw new Exception (Feedback.MISSING_INVALID_PROV_ID);
            }
        }catch(Exception e){
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
            }
            out.println (message);
            return;
        }
        
        //continue registration
        try {
            try {
                //logging statements
                System.out.println ("Looking up competition details ......");
                //end of logging
                comp = CompetitionManager.viewCompetition (kw, provId, realDate);
                provName = user.getName ();
                //logging statements
                System.out.println ("Found details for competition with ID: " + comp.getCompetitionId ());
                System.out.println ("competition name: " + comp.getServiceName ());
                System.out.println ("competition question: " + comp.getQuestion ());
                System.out.println ("Keyword: " + comp.getKeyword ());
                //end of logging
            }catch(Exception e) {
                //logging statements
                System.out.println ("Could not find details for competition");
                //end of logging
                throw new Exception (Feedback.NO_SUCH_COMPETITION);
            }
            
            //check if competition is not over
            if (!comp.isEnded ()) {
                //logging statements
                System.out.println ("Competition not ended");
                //end of logging
                //check type of competition
                if (comp.getType () == CompetitionManager.FIXED_ODDS) {
                    //logging statements
                    System.out.println ("Competition is a fixed odds competition");
                    //end of logging
                    FixedOddsCompetition fo = (com.rancard.mobility.infoserver.smscompetition.FixedOddsCompetition) comp;
                    //check if person has already registered
                    if (!fo.alreadyRegistered (msisdn)) {
                        //logging statements
                        System.out.println ("participant hzs not already registered");
                        //end of logging
                        if (fo.canAddParticipant ()) {
                            //logging statements
                            System.out.println ("participant can be registered");
                            //end of logging
                            //register participant
                            InboxEntry entry = new InboxEntry ();
                            entry.setMsisdn (msisdn);
                            entry.setKeyword (fo.getKeyword ());
                            entry.setShortCode (dest);
                            entry.setAccountId (provId);
                            String registrationId = fo.register (entry);
                            //logging statements
                            System.out.println ("participant has been registered");
                            //end of logging
                            //fo.setCurrentParticipation (fo.getCurrentParticipation () + 1);
                            //send response via sms
                            
                            //send question
                            String options = "";
                            java.util.ArrayList opt = fo.getOptions ();
                            for (int i = 0; i < opt.size (); i++) {
                                options = options + ((com.rancard.mobility.infoserver.smscompetition.Option) opt.get (i)).getOptionId () +
                                        ". " + ((com.rancard.mobility.infoserver.smscompetition.Option) opt.get (i)).getDescription () + ", ";
                            }
                            String question = fo.getQuestion () + " " + options;
                            //logging statements
                            System.out.println ("sending question");
                            //end of logging
                            
                            //clean up
                            fo = null;
                            entry = null;
                            opt = null;
                            //end of clean up
                            
                            out.println (new String (question));
                            return;
                        } else {
                            //logging statements
                            System.out.println ("participant can NOT be registered. participation limit exceeded");
                            //end of logging
                            throw new Exception (Feedback.PARTICIPATION_LIMIT_EXCEEDED);
                        }
                    } else if (fo.alreadyRegistered (msisdn) && !fo.alreadyVoted (msisdn) &&
                            (msg == null || msg.equals (""))) {
                        //logging statements
                        System.out.println ("participant has already been registered. cannot register");
                        //end of logging
                        throw new Exception (Feedback.ALREADY_REGISTERED);
                    } else if (fo.alreadyVoted (msisdn)) {
                        //logging statements
                        System.out.println ("participant has already voted. cannot vote");
                        //end of logging
                        throw new Exception (com.rancard.common.Feedback.ALREADY_VOTED);
                    } else if (!fo.alreadyVoted (msisdn) && msg != null && !msg.equals ("")) {
                        //vote
                        //logging statements
                        System.out.println ("participant can vote");
                        //end of logging
                        InboxEntry entry = new InboxEntry ();
                        entry.setMsisdn (msisdn);
                        entry.setKeyword (fo.getKeyword ());
                        entry.setMessage (msg);
                        entry.setShortCode (dest);
                        entry.setAccountId (provId);
                        fo.vote (entry);
                        //logging statements
                        System.out.println ("participant voted successfully");
                        //end of logging
                        
                        //clean up
                        fo = null;
                        //end of clean up
                        
                        String insertions = "ack=" + ACK + "&keyword=" + kw + "&msg=" + msg + "&msisdn=" + msisdn + "&acctId=" + provId + "&srvcName=" + fo.getServiceName () +
                                "&provName=" + provName + "&pin=" + entry.getMessageId ();
                        ACK = com.rancard.util.URLUTF8Encoder.doMessageEscaping (insertions, ACK);
                        out.println (ACK);
                        
                        //clean up
                        entry = null;
                        //end of clean up
                        
                        return;
                    } else {
                    }
                } else if (comp.getType () == CompetitionManager.ENDLESS_ODDS) {
                    //logging statements
                    System.out.println ("Competition is a endless odds competition");
                    //end of logging
                    EndlessOddsCompetition eo = (com.rancard.mobility.infoserver.smscompetition.EndlessOddsCompetition) comp;
                    //vote
                    //logging statements
                    System.out.println ("participant can vote");
                    //end of logging
                    InboxEntry entry = new InboxEntry ();
                    entry.setMsisdn (msisdn);
                    entry.setKeyword (eo.getKeyword ());
                    entry.setMessage (msg);
                    entry.setShortCode (dest);
                    entry.setAccountId (provId);
                    String voteid = eo.vote (entry);
                    //logging statements
                    System.out.println ("participant voted successfully");
                    //end of logging
                    //eo.setCurrentParticipation (InboxManager.getParticipationLevel (eo.getKeyword (), eo.getAccountId ()));
                    
                    String insertions = "ack=" + ACK + "&keyword=" + kw + "&msg=" + msg + "&msisdn=" + msisdn + "&acctId=" + provId + "&srvcName=" + eo.getServiceName () +
                            "&provName=" + provName + "&pin=" + entry.getMessageId ();
                    ACK = com.rancard.util.URLUTF8Encoder.doMessageEscaping (insertions, ACK);
                    out.println (ACK);
                    
                    //clean up
                    eo = null;
                    entry = null;
                    //end of clean up
                    
                    return;
                } else if (comp.getType () == CompetitionManager.ENDLESS_ODDS_QUESTION_BACK) {
                    //logging statements
                    System.out.println ("Competition is a endless odds competition with question");
                    //end of logging
                    EndlessOddsWithQuestion eo = (com.rancard.mobility.infoserver.smscompetition.EndlessOddsWithQuestion) comp;
                    if(msg == null || msg.equals ("")){ //keyword only - requesting question
                        //logging statements
                        System.out.println ("keyword only - requesting question");
                        //end of logging
                        String options = "";
                        java.util.ArrayList opt = eo.getOptions ();
                        for (int i = 0; i < opt.size (); i++) {
                            options = options + ((com.rancard.mobility.infoserver.smscompetition.Option) opt.get (i)).getOptionId () +
                                    ". " + ((com.rancard.mobility.infoserver.smscompetition.Option) opt.get (i)).getDescription () + ", ";
                        }
                        String question = eo.getQuestion () + " " + options;
                        //logging statements
                        System.out.println ("sending question");
                        //end of logging
                        
                        eo = null;
                        opt = null;
                        
                        out.println (new String (question));
                        return;
                    } else {
                        //vote
                        //logging statements
                        System.out.println ("participant can vote");
                        //end of logging
                        InboxEntry entry = new InboxEntry ();
                        entry.setMsisdn (msisdn);
                        entry.setKeyword (eo.getKeyword ());
                        entry.setMessage (msg);
                        entry.setShortCode (dest);
                        entry.setAccountId (provId);
                        String voteid = eo.vote (entry);
                        //logging statements
                        System.out.println ("participant voted successfully");
                        //end of logging
                        //eo.setCurrentParticipation (InboxManager.getParticipationLevel (eo.getKeyword (), eo.getAccountId ()));
                        
                        String insertions = "ack=" + ACK + "&keyword=" + kw + "&msg=" + msg + "&msisdn=" + msisdn + "&acctId=" + provId + "&srvcName=" + eo.getServiceName () +
                                "&provName=" + provName + "&pin=" + entry.getMessageId ();
                        ACK = com.rancard.util.URLUTF8Encoder.doMessageEscaping (insertions, ACK);
                        out.println (ACK);
                        
                        //clean up
                        entry = null;
                        //end of clean up
                        
                        return;
                    }
                }
            } else {
                //logging statements
                System.out.println ("Competition ended");
                //end of logging
                throw new Exception (Feedback.COMPETITION_ENDED);
            }
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
                message = e.getMessage ();
            }
            out.println (message);
            return;
        } finally {
            realDate = null;
            cnxn = null;
            comp = null;
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
    
    private String doURLEscaping (String insertions,String stored_ack) throws Exception{
        String url_tmp = stored_ack;
        String result = "";
        String tmp_1_val = "";
        //if( stored_ack.indexOf ("?") != -1 ){
        String[] query = insertions.split ("&");
        
        for (int i = 0; i < query.length; i++) {
            String[] tmp = query[i].split ("=");
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile ("@@" + tmp[0] + "@@");
            java.util.regex.Matcher matcher = pattern.matcher (url_tmp);
            try{
                tmp_1_val = tmp[1];
            }catch(ArrayIndexOutOfBoundsException a){
                tmp_1_val = "";
            }
            url_tmp = matcher.replaceAll (tmp_1_val);
        }
        result = url_tmp;
        //}else
        //result = stored_ack;
        return result;
    }
}
