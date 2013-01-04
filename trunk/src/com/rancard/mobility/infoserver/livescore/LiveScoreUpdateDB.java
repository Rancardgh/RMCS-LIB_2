/*
 * LiveScoreUpdateDB.java
 *
 * Created on January 28, 2007, 9:57 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.mobility.infoserver.livescore;

import com.rancard.common.DConnect;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Messenger
 */
public abstract class LiveScoreUpdateDB {
    
    public static boolean createUpdate (LiveScoreUpdate update) throws Exception {
        
        String SQL;
        ResultSet rs=null;
        Connection con=null;
        PreparedStatement prepstat=null;
        boolean created = false;
        String updateMessage = update.getEnglishMessage ()/*.replaceAll ("'", "\'")*/;
        //updateMessage = updateMessage.replaceAll ("`", "\'");
        
        try {
            con=DConnect.getConnection ();
            
            // first test if game exists
            SQL = "select * from livescore_fixtures where game_id='" + update.getEventId () + "'";
            prepstat=con.prepareStatement (SQL);
            rs=prepstat.executeQuery ();
            if (rs.next ()) {
                //check if update exists
                SQL = "select * from livescore_updates where update_id='" + update.getUpdateId () + "'";
                prepstat=con.prepareStatement (SQL);
                rs=prepstat.executeQuery ();
                if (!rs.next ()) {
                    //insert update
                    //SQL="insert into livescore_updates (update_id,game_id,event_trigger,message,publish_date) values ('"+ update.getUpdateId () + "','" + update.getEventId () + "','" + update.getTrigger () +
                    //        "','" + updateMessage + "','" + update.getPublishDate () + "')";
                    SQL="insert into livescore_updates (update_id,game_id,event_trigger,message_en,publish_date,message_fr) values ('"+ update.getUpdateId () + "','" + update.getEventId () + "','" 
                            + update.getTrigger () + "',?,'" + update.getPublishDate () + "',?)";
                    prepstat=con.prepareStatement (SQL);
                    prepstat.setBytes (1, update.getEnglishMessage ().getBytes ());
                    prepstat.setBytes (2, update.getFrenchMessage ().getBytes ());
                    prepstat.execute ();
                    created = true;
                }else{
                    created = false;
                }
            }
        }catch (Exception ex){
            if(con !=null)
                con.close ();
            throw new Exception (ex.getMessage ());
        }finally{
            if(con !=null) {
                con.close ();
            }
        }
        return created;
    }
    
    public static String viewUpdateMessage (String updateId, String language) throws Exception {
        String message = "No updates yet";
        String SQL;
        ResultSet rs=null;
        Connection con=null;
        PreparedStatement prepstat=null;
        boolean created = false;
        
        try {
            con=DConnect.getConnection ();
            
            //check if update exists
            SQL = "select * from livescore_updates lu inner join livescore_fixtures lf on lu.game_id=lf.game_id where lu.update_id='" + updateId + "'";
            prepstat=con.prepareStatement (SQL);
            rs=prepstat.executeQuery ();
            if(rs.next ()) {
                String columnName = "lu.message_" + language;
                byte[] msgBytes = rs.getBytes (columnName);
                //message = rs.getString ("lu.message");
                message = new String (msgBytes);
            }
        }catch (Exception ex){
            if(con !=null)
                con.close ();
            throw new Exception (ex.getMessage ());
        }finally{
            if(con !=null) {
                con.close ();
            }
        }
        return message;
    }
    
    public static LiveScoreUpdate viewUpdate (String updateId) throws Exception {
        LiveScoreUpdate update = new LiveScoreUpdate ();
        String SQL;
        ResultSet rs=null;
        Connection con=null;
        PreparedStatement prepstat=null;
        boolean created = false;
        
        try {
            con=DConnect.getConnection ();
            
            //check if update exists
            SQL = "select * from livescore_updates lu inner join livescore_fixtures lf on lu.game_id=lf.game_id where lu.update_id='" + updateId + "'";
            prepstat=con.prepareStatement (SQL);
            rs=prepstat.executeQuery ();
            if(rs.next ()) {
                update.setCountryName (rs.getString ("lf.country_name"));
                update.setEventDate (rs.getString ("lf.date"));
                update.setEventId (rs.getString ("lf.game_id"));
                update.setEventName (rs.getString ("lf.home_team") + " vs " + rs.getString ("lf.away_team"));
                update.setEventStatus (rs.getString ("lf.status"));
                update.setLeagueId (rs.getString ("lf.league_id"));
                update.setLeagueName (rs.getString ("lf.league_name"));
                
                byte[] msgBytes = rs.getBytes ("lu.message_en");
                String message = new String (msgBytes);
                update.setEnglishMessage (message);
                
                msgBytes = rs.getBytes ("lu.message_fr");
                message = new String (msgBytes);
                update.setFrenchMessage (message);
                
                update.setTrigger (rs.getString ("lu.event_trigger"));
                update.setUpdateId (rs.getString ("lu.update_id"));
                ArrayList participants = new ArrayList ();
                participants.add (rs.getString ("lf.home_team"));
                participants.add (rs.getString ("lf.away_team"));
                ArrayList scores = new ArrayList ();
                participants.add (rs.getString ("lf.home_score"));
                participants.add (rs.getString ("lf.away_score"));
                update.setParticipants (participants);
                update.setScores (scores);
                update.setPublishDate (rs.getString ("lu.publish_date"));
            }
        }catch (Exception ex){
            if(con !=null)
                con.close ();
            throw new Exception (ex.getMessage ());
        }finally{
            if(con !=null) {
                con.close ();
            }
        }
        return update;
    }
    
     public static LiveScoreUpdate viewUpdate(String updateId, int gameStatus) throws Exception {
        LiveScoreUpdate update = new LiveScoreUpdate ();
        String SQL;
        ResultSet rs=null;
        Connection con=null;
        PreparedStatement prepstat=null;
        boolean created = false;
        
        try {
            con=DConnect.getConnection ();
            
            //check if update exists
            SQL = "select * from livescore_updates lu inner join livescore_fixtures lf on lu.game_id=lf.game_id where lu.update_id='" + updateId + "' and lf.status ='"+gameStatus + "'";
            prepstat=con.prepareStatement (SQL);
            rs=prepstat.executeQuery ();
            if(rs.next ()) {
                update.setCountryName (rs.getString ("lf.country_name"));
                update.setEventDate (rs.getString ("lf.date"));
                update.setEventId (rs.getString ("lf.game_id"));
                update.setEventName (rs.getString ("lf.home_team") + " vs " + rs.getString ("lf.away_team"));
                update.setEventStatus (rs.getString ("lf.status"));
                update.setLeagueId (rs.getString ("lf.league_id"));
                update.setLeagueName (rs.getString ("lf.league_name"));
                
                byte[] msgBytes = rs.getBytes ("lu.message_en");
                String message = new String (msgBytes);
                update.setEnglishMessage (message);
                
                msgBytes = rs.getBytes ("lu.message_fr");
                message = new String (msgBytes);
                update.setFrenchMessage (message);
                
                update.setTrigger (rs.getString ("lu.event_trigger"));
                update.setUpdateId (rs.getString ("lu.update_id"));
                ArrayList participants = new ArrayList ();
                participants.add (rs.getString ("lf.home_team"));
                participants.add (rs.getString ("lf.away_team"));
                ArrayList scores = new ArrayList ();
                participants.add (rs.getString ("lf.home_score"));
                participants.add (rs.getString ("lf.away_score"));
                update.setParticipants (participants);
                update.setScores (scores);
                update.setPublishDate (rs.getString ("lu.publish_date"));
            }
        }catch (Exception ex){
            if(con !=null)
                con.close ();
            throw new Exception (ex.getMessage ());
        }finally{
            if(con !=null) {
                con.close ();
            }
        }
        return update;
    }
    
    
    public static ArrayList viewAllUpdates (String gameId) throws Exception {
        ArrayList updates = new ArrayList ();
        String SQL;
        ResultSet rs=null;
        Connection con=null;
        PreparedStatement prepstat=null;
        boolean created = false;
        
        try {
            con=DConnect.getConnection ();
            
            //check if update exists
            SQL = "select * from livescore_updates lu inner join livescore_fixtures lf on lu.game_id=lf.game_id where lf.game_id='" + gameId + "' order by publish_date desc";
            prepstat=con.prepareStatement (SQL);
            rs=prepstat.executeQuery ();
            while(rs.next ()) {
                LiveScoreUpdate update = new LiveScoreUpdate ();
                update.setCountryName (rs.getString ("lf.country_name"));
                update.setEventDate (rs.getString ("lf.date"));
                update.setEventId (rs.getString ("lf.game_id"));
                update.setEventName (rs.getString ("lf.home_team") + " vs " + rs.getString ("lf.away_team"));
                update.setEventStatus (rs.getString ("lf.status"));
                update.setLeagueId (rs.getString ("lf.league_id"));
                update.setLeagueName (rs.getString ("lf.league_name"));
                
                byte[] msgBytes = rs.getBytes ("lu.message_en");
                String message = new String (msgBytes);
                update.setEnglishMessage (message);
                
                msgBytes = rs.getBytes ("lu.message_fr");
                message = new String (msgBytes);
                update.setFrenchMessage (message);
                
                update.setTrigger (rs.getString ("lu.event_trigger"));
                update.setUpdateId (rs.getString ("lu.update_id"));
                ArrayList participants = new ArrayList ();
                participants.add (rs.getString ("lf.home_team"));
                participants.add (rs.getString ("lf.away_team"));
                ArrayList scores = new ArrayList ();
                participants.add (rs.getString ("lf.home_score"));
                participants.add (rs.getString ("lf.away_score"));
                update.setParticipants (participants);
                update.setScores (scores);
                update.setPublishDate (rs.getString ("lu.publish_date"));
                
                updates.add (update);
            }
        }catch (Exception ex){
            if(con !=null)
                con.close ();
            throw new Exception (ex.getMessage ());
        }finally{
            if(con !=null) {
                con.close ();
            }
        }
        return updates;
    }
    
    public static ArrayList viewAllUpdates (String gameId, String date) throws Exception {
        ArrayList updates = new ArrayList ();
        String SQL;
        ResultSet rs=null;
        Connection con=null;
        PreparedStatement prepstat=null;
        boolean created = false;
        
        try {
            con=DConnect.getConnection ();
            
            SimpleDateFormat s_formatter = new SimpleDateFormat ("MM-dd-yyyy");
            java.util.Date validDate = null;
            try {
                validDate = s_formatter.parse (date);
            } catch (Exception ex) {
                validDate = Calendar.getInstance ().getTime ();
            }
            
            
            //check if update exists
            SQL = "select * from livescore_updates lu inner join livescore_fixtures lf on lu.game_id=lf.game_id where lf.game_id=? and date(lf.date)=? order by publish_date desc";
            prepstat=con.prepareStatement (SQL);
             prepstat.setString(1,gameId);
            prepstat.setDate(2,new java.sql.Date(validDate.getTime()) );
            
            rs=prepstat.executeQuery ();
            while(rs.next ()) {
                LiveScoreUpdate update = new LiveScoreUpdate ();
                update.setCountryName (rs.getString ("lf.country_name"));
                update.setEventDate (rs.getString ("lf.date"));
                update.setEventId (rs.getString ("lf.game_id"));
                update.setEventName (rs.getString ("lf.home_team") + " vs " + rs.getString ("lf.away_team"));
                update.setEventStatus (rs.getString ("lf.status"));
                update.setLeagueId (rs.getString ("lf.league_id"));
                update.setLeagueName (rs.getString ("lf.league_name"));
                
                byte[] msgBytes = rs.getBytes ("lu.message_en");
                String message = new String (msgBytes);
                update.setEnglishMessage (message);
                
                msgBytes = rs.getBytes ("lu.message_fr");
                message = new String (msgBytes);
                update.setFrenchMessage (message);
                
                update.setTrigger (rs.getString ("lu.event_trigger"));
                update.setUpdateId (rs.getString ("lu.update_id"));
                ArrayList participants = new ArrayList ();
                participants.add (rs.getString ("lf.home_team"));
                participants.add (rs.getString ("lf.away_team"));
                ArrayList scores = new ArrayList ();
                participants.add (rs.getString ("lf.home_score"));
                participants.add (rs.getString ("lf.away_score"));
                update.setParticipants (participants);
                update.setScores (scores);
                update.setPublishDate (rs.getString ("lu.publish_date"));
                
                updates.add (update);
            }
        }catch (Exception ex){
            if(con !=null)
                con.close ();
            throw new Exception (ex.getMessage ());
        }finally{
            if(con !=null) {
                con.close ();
            }
        }
        return updates;
    }
}
