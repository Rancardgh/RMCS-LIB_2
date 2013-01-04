/*
 * LiveScoreFixtureDB.java
 *
 * Created on January 14, 2007, 12:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.mobility.infoserver.livescore;

import com.rancard.common.DConnect;
import com.rancard.common.uidGen;
import com.rancard.mobility.infoserver.livescore.serviceinterfaces.setuptriggers;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author Messenger
 */
public abstract class LiveScoreFixtureDB {
    
    public static void createFixture(LiveScoreFixture game) throws Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection();
            //con.setAutoCommit (false);
            SQL =   "Select * from livescore_fixtures where game_id='" + game.getGameId( ) + "'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            
            if(!rs.next()){//game does not exist
                SQL =   "insert into livescore_fixtures (game_id,country_name,league_id,league_name,home_team,home_score,away_team,away_score,date,status) values(?,?,?,?,?,?,?,?,?,?)";
                prepstat = con.prepareStatement(SQL);
                prepstat.setString(1, game.getGameId());
                prepstat.setString(2, game.getCountryName());
                prepstat.setString(3, game.getLeagueId());
                prepstat.setString(4, game.getLeagueName());
                prepstat.setString(5, game.getHomeTeam());
                prepstat.setString(6, game.getHomeScore());
                prepstat.setString(7, game.getAwayTeam());
                prepstat.setString(8, game.getAwayScore());
                prepstat.setString(9, game.getDate());
                prepstat.setInt(10, game.getStatus());
                prepstat.execute();
                
                //create alias
                SQL =   "select ";
            }else{
                SQL = "update livescore_fixtures set country_name=?, league_id=?, league_name=?, home_team=?, home_score=?, away_team=?, away_score=?, " +
                        "livescore_fixtures.status=?, date=? where livescore_fixtures.game_id=?";
                prepstat = con.prepareStatement(SQL);
                prepstat.setString(1, game.getCountryName());
                prepstat.setString(2, game.getLeagueId());
                prepstat.setString(3, game.getLeagueName());
                prepstat.setString(4, game.getHomeTeam());
                prepstat.setString(5, game.getHomeScore());
                prepstat.setString(6, game.getAwayTeam());
                prepstat.setString(7, game.getAwayScore());
                prepstat.setInt(8, game.getStatus());
                prepstat.setString(9, game.getDate());
                prepstat.setString(10, game.getGameId());
                prepstat.execute();
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            System.out.println("Could not create/update entry for " + game.getGameId() + ": " + game.getHomeTeam() + " vs " + game.getAwayTeam() +
                    ". Error message: " + ex.getMessage());
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }
    
    public static void updateFixture(LiveScoreFixture update) throws Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection();
            
            SQL = "update livescore_fixtures set country_name=?, league_id=?, league_name=?, home_team=?, home_score=?, away_team=?, away_score=?, " +
                    "livescore_fixtures.status=?, date=?, event_notif_sent=? where livescore_fixtures.game_id=?";
            prepstat = con.prepareStatement(SQL);
            prepstat.setString(1, update.getCountryName());
            prepstat.setString(2, update.getLeagueId());
            prepstat.setString(3, update.getLeagueName());
            prepstat.setString(4, update.getHomeTeam());
            prepstat.setString(5, update.getHomeScore());
            prepstat.setString(6, update.getAwayTeam());
            prepstat.setString(7, update.getAwayScore());
            prepstat.setInt(8, update.getStatus());
            prepstat.setString(9, update.getDate());
            prepstat.setInt(10, update.getEventNotifSent());
            prepstat.setString(11, update.getGameId());
            prepstat.execute();
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        } finally {
            
            if (con != null) {
                con.close();
            }
        }
    }
    
    public static void updateFixture(String match_id, int status, String date) throws Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection();
            
            //for competition
            SQL = "update livescore_fixtures set livescore_fixtures.status=?, date=? where livescore_fixtures.game_id=?";
            prepstat = con.prepareStatement(SQL);
            prepstat.setInt(1, status);
            prepstat.setString(2, date);
            prepstat.setString(3, match_id);
            prepstat.execute();
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        } finally {
            
            if (con != null) {
                con.close();
            }
        }
    }
    
    public static void updateFixture(String match_id, String homeScore, String awayScore, String date) throws Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection();
            
            //for competition
            SQL = "update livescore_fixtures set home_score=?, away_score=?, date=? where livescore_fixtures.game_id=?";
            prepstat = con.prepareStatement(SQL);
            prepstat.setString(1, homeScore);
            prepstat.setString(2, awayScore);
            prepstat.setString(3, date);
            prepstat.setString(4, match_id);
            prepstat.execute();
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        } finally {
            
            if (con != null) {
                con.close();
            }
        }
    }
    
    public static void updateFixture(String match_id, String homeScore, String awayScore, int status, String date) throws Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection();
            
            //for competition
            SQL = "update livescore_fixtures set home_score=?, away_score=?, livescore_fixtures.status=?, date=? where livescore_fixtures.game_id=?";
            prepstat = con.prepareStatement(SQL);
            prepstat.setString(1, homeScore);
            prepstat.setString(2, awayScore);
            prepstat.setInt(3, status);
            prepstat.setString(4, date);
            prepstat.setString(5, match_id);
            prepstat.execute();
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        } finally {
            
            if (con != null) {
                con.close();
            }
        }
    }
    
    public static LiveScoreFixture viewFixture(String gameId) throws Exception {
        
        LiveScoreFixture fixture = new LiveScoreFixture();
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            
            SQL = "SELECT * FROM livescore_fixtures where game_id='" + gameId + "'";
            
            
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            
            while (rs.next()) {
                fixture.setCountryName(rs.getString("country_name"));
                fixture.setDate(rs.getTimestamp("date").toString());
                fixture.setGameId(rs.getString("game_id"));
                fixture.setLeagueId(rs.getString("league_id"));
                fixture.setLeagueName(rs.getString("league_name"));
                fixture.setHomeTeam(rs.getString("home_team"));
                fixture.setHomeScore(rs.getString("home_score"));
                fixture.setAwayTeam(rs.getString("away_team"));
                fixture.setAwayScore(rs.getString("away_score"));
                fixture.setStatus(rs.getInt("status"));
                fixture.setEventNotifSent(rs.getInt("event_notif_sent"));
            }
            
            SQL = "SELECT * FROM livescore_game_alias where game_id='" + gameId + "'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            while (rs.next()) {
                fixture.setAlias(rs.getString("alias"));
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        
        return fixture;
    }
    public static LiveScoreFixture viewFixtureForCP(String gameId, String accountId) throws Exception {
        
        LiveScoreFixture fixture = new LiveScoreFixture();
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            
            SQL = "SELECT * FROM livescore_fixture_mgt lfm inner join livescore_fixtures lf on lf.game_id=lfm.game_id " +
                    "where lfm.game_id='" + gameId + "' and lfm.account_id='"+accountId+"'";
            
            
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            
            while (rs.next()) {
                fixture.setCountryName(rs.getString("country_name"));
                fixture.setDate(rs.getTimestamp("date").toString());
                fixture.setGameId(rs.getString("game_id"));
                fixture.setLeagueId(rs.getString("league_id"));
                fixture.setLeagueName(rs.getString("league_name"));
                fixture.setHomeTeam(rs.getString("home_team"));
                fixture.setHomeScore(rs.getString("home_score"));
                fixture.setAwayTeam(rs.getString("away_team"));
                fixture.setAwayScore(rs.getString("away_score"));
                fixture.setStatus(rs.getInt("status"));
                fixture.setEventNotifSent(rs.getInt("event_notif_sent"));
            }
            
            SQL = "SELECT * FROM livescore_game_alias where game_id='" + gameId + "'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            while (rs.next()) {
                fixture.setAlias(rs.getString("alias"));
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        
        return fixture;
    }
    public static LiveScoreFixture viewFixture(String gameId, String date) throws Exception {
        
        LiveScoreFixture fixture = new LiveScoreFixture();
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        
        try {
            con = DConnect.getConnection();
            
            SimpleDateFormat s_formatter = new SimpleDateFormat("MM-dd-yyyy");
            java.util.Date validDate = null;
            try {
                validDate = s_formatter.parse(date);
            } catch (Exception ex) {
                validDate = Calendar.getInstance().getTime();
            }
            
            
            SQL = "SELECT * FROM livescore_fixtures where game_id=? and DATE(livescore_fixtures.date) =?";
            
            prepstat = con.prepareStatement(SQL);
            prepstat.setString(1,gameId);
            prepstat.setDate(2,new java.sql.Date(validDate.getTime()) );
            
            rs = prepstat.executeQuery();
            
            while (rs.next()) {
                fixture.setCountryName(rs.getString("country_name"));
                fixture.setDate(rs.getTimestamp("date").toString());
                fixture.setGameId(rs.getString("game_id"));
                fixture.setLeagueId(rs.getString("league_id"));
                fixture.setLeagueName(rs.getString("league_name"));
                fixture.setHomeTeam(rs.getString("home_team"));
                fixture.setHomeScore(rs.getString("home_score"));
                fixture.setAwayTeam(rs.getString("away_team"));
                fixture.setAwayScore(rs.getString("away_score"));
                fixture.setStatus(rs.getInt("status"));
                fixture.setEventNotifSent(rs.getInt("event_notif_sent"));
            }
            
            SQL = "SELECT * FROM livescore_game_alias where game_id='" + gameId + "'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            while (rs.next()) {
                fixture.setAlias(rs.getString("alias"));
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        
        return fixture;
    }
    
    public static LiveScoreFixture viewFixtureByAlias(String alias) throws Exception {
        
        LiveScoreFixture fixture = new LiveScoreFixture();
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            
            SQL = "SELECT * FROM livescore_fixtures lf inner join livescore_game_alias lg on lf.game_id=lg.game_id where lg.alias='" + alias + "'";
            
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            
            while (rs.next()) {
                fixture.setCountryName(rs.getString("lf.country_name"));
                fixture.setDate(rs.getTimestamp("lf.date").toString());
                fixture.setGameId(rs.getString("lf.game_id"));
                fixture.setLeagueId(rs.getString("lf.league_id"));
                fixture.setLeagueName(rs.getString("lf.league_name"));
                fixture.setHomeTeam(rs.getString("lf.home_team"));
                fixture.setHomeScore(rs.getString("lf.home_score"));
                fixture.setAwayTeam(rs.getString("lf.away_team"));
                fixture.setAwayScore(rs.getString("lf.away_score"));
                fixture.setStatus(rs.getInt("lf.status"));
                fixture.setEventNotifSent(rs.getInt("lf.event_notif_sent"));
                fixture.setAlias(alias);
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        
        return fixture;
    }
    public static LiveScoreFixture viewFixtureForCPByAlias(String alias, String accountId) throws Exception {
        
        LiveScoreFixture fixture = new LiveScoreFixture();
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            
            SQL = "SELECT * FROM livescore_fixture_mgt lfm inner join livescore_fixtures lf on lf.game_id=lfm.game_id " +
                    "inner join livescore_game_alias lg on lf.game_id=lg.game_id where lg.alias='" + alias + "'" +
                    " and lfm.account_id='"+accountId+"'";
            
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            
            while (rs.next()) {
                fixture.setCountryName(rs.getString("lf.country_name"));
                fixture.setDate(rs.getTimestamp("lf.date").toString());
                fixture.setGameId(rs.getString("lf.game_id"));
                fixture.setLeagueId(rs.getString("lf.league_id"));
                fixture.setLeagueName(rs.getString("lf.league_name"));
                fixture.setHomeTeam(rs.getString("lf.home_team"));
                fixture.setHomeScore(rs.getString("lf.home_score"));
                fixture.setAwayTeam(rs.getString("lf.away_team"));
                fixture.setAwayScore(rs.getString("lf.away_score"));
                fixture.setStatus(rs.getInt("lf.status"));
                fixture.setEventNotifSent(rs.getInt("lf.event_notif_sent"));
                fixture.setAlias(alias);
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        
        return fixture;
    }
    
    public static HashMap viewAllFixturesForDate(String date, int status) throws Exception {
        HashMap groupedFixtures = new HashMap();
        
        String query;
        String SQL;
        ResultSet leagues = null;
        ResultSet fixtures = null;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        //logging statement
        System.out.println(new java.util.Date()+":@com.rancard.mobility.infoserver.livescore.LiveScoreFixtureDB: viewAllFixturesForDate(date, status)");
        
        try {
            con = DConnect.getConnection();
            
            //get available networks for content provider
            query = "select league_id from livescore_services";
            prepstat = con.prepareStatement(query);
            leagues = prepstat.executeQuery();
            
            while (leagues.next()) {
                String id = new String();
                id = leagues.getString("league_id");
                
                String searchDateString = date.substring(0, date.indexOf(":"));
                //get subscribers from a selected network
                query = "select * from livescore_fixtures where league_id='" + id +"' and livescore_fixtures.status=" + status + " and  date like '" + searchDateString + "%'";
                
                //logging statement
               // System.out.println(new java.util.Date()+":fixture_searchQuery:" + query);
                
                prepstat = con.prepareStatement(query);
                fixtures = prepstat.executeQuery();
                
                ArrayList games = new ArrayList();
                
                while (fixtures.next()) {
                    LiveScoreFixture fixture = new LiveScoreFixture();
                    
                    fixture.setCountryName(fixtures.getString("country_name"));
                    fixture.setDate(fixtures.getTimestamp("date").toString());
                    fixture.setGameId(fixtures.getString("game_id"));
                    fixture.setLeagueId(fixtures.getString("league_id"));
                    fixture.setLeagueName(fixtures.getString("league_name"));
                    fixture.setHomeTeam(fixtures.getString("home_team"));
                    fixture.setHomeScore(fixtures.getString("home_score"));
                    fixture.setAwayTeam(fixtures.getString("away_team"));
                    fixture.setAwayScore(fixtures.getString("away_score"));
                    fixture.setStatus(fixtures.getInt("status"));
                    fixture.setEventNotifSent(fixtures.getInt("event_notif_sent"));
                    
                    SQL = "SELECT * FROM livescore_game_alias where game_id='" + fixture.getGameId() + "'";
                    prepstat = con.prepareStatement(SQL);
                    rs = prepstat.executeQuery();
                    while (rs.next()) {
                        fixture.setAlias(rs.getString("alias"));
                    }
                    
                    games.add(fixture);
                }
                
                //insert array into hashmap
                groupedFixtures.put(id, games);
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        return groupedFixtures;
    }
    public static HashMap viewAllFixturesForDateGroupByCP(String date, int status, int notifStatus) throws Exception {
       
        //logging statement
         System.out.println(new java.util.Date()+":@com.rancard.mobility.infoserver.livescore.LiveScoreFixtureDB:viewAllFixturesForDateGroupByCP():");
        
        HashMap groupedCPFixt = new HashMap();
        
        
        String query;
        ResultSet rs = null;
        ResultSet cpRS = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        String searchDateString = date.substring(0, date.indexOf(":"));
        
        try {
            con = DConnect.getConnection();
            
            
            //get CPs
            query = "select count(game_id), account_id from livescore_fixture_mgt where " +
                    "game_id in(select game_id from livescore_fixtures where date " +
                    "like '" + searchDateString + "%') group by account_id";
            
            prepstat = con.prepareStatement(query);
            cpRS = prepstat.executeQuery();
            
            //group fixtures by CP
            String accountId = "";
            while (cpRS.next()) {
                
                accountId = cpRS.getString("account_id");
                //logging statement
                System.out.println(new java.util.Date()+":retrieving fixtures for CP: "+accountId);
                //get fixtures
                query = "select * from livescore_fixture_mgt where account_id='"+accountId+"' "+
                        "and game_id in(select game_id from livescore_fixtures where status='" + status + "' " +
                        "and  date like '" + searchDateString + "%' and event_notif_sent='"+notifStatus+"')";
                 
                prepstat = con.prepareStatement(query);
                rs = prepstat.executeQuery();
                
                ArrayList<String> games = new ArrayList();
                
                while (rs.next()){
                    
                    String gameId = rs.getString("game_id");
                    //add to CP's fixtureList
                    games.add(gameId);
                }
                //logging statement
                System.out.println(new java.util.Date()+":No. Fixtures found for CP ("+accountId+"): "+ games.size() );
                
                //insert grouped fixture to current hashmap
                groupedCPFixt.put(accountId, games);
                
                //free memory
                games = null;
            }//end of cpRS while loop
            
            //logging statement 
            System.out.println(new java.util.Date()+":Total No. CPs: "+ groupedCPFixt.size() );
           
            
        } catch (Exception ex) {
            System.out.println(new java.util.Date()+":error getting CPFixtureMatrix:");
            ex.printStackTrace();
            
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        return groupedCPFixt;
    }
    
    public static HashMap viewAllActiveFixturesForDate(String date) throws Exception {
        HashMap groupedFixtures = new HashMap();
        
        String query;
        String SQL;
        ResultSet leagues = null;
        ResultSet fixtures = null;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection();
            
            SimpleDateFormat s_formatter = new SimpleDateFormat();
            java.util.Date validDate = null;
            try {
                validDate = s_formatter.parse(date);
            } catch (Exception ex) {
                validDate = Calendar.getInstance().getTime();
            }
            
            Calendar c = Calendar.getInstance();
            c.setTime(validDate);
            c.add(c.HOUR_OF_DAY, 24);
            Date endDate = c.getTime();
            
            query = "select league_id from livescore_services";
            prepstat = con.prepareStatement(query);
            leagues = prepstat.executeQuery();
            
            while (leagues.next()) {
                String id = new String();
                id = leagues.getString("league_id");
                
                query = "select * from livescore_fixtures lf where lf.league_id='" + id +"' and lf.status='" + LiveScoreFixture.NOT_PLAYED + "' and  (lf.date like '" + date + "%' or lf.date >='" +
                        new java.sql.Timestamp(validDate.getTime()).toString() + "' and lf.date <='" + new java.sql.Timestamp(endDate.getTime()).toString() + "')";
                prepstat = con.prepareStatement(query);
                
                /*/get subscribers from a selected network
                query = "select * from livescore_fixtures where league_id='" + id +"' and livescore_fixtures.status in ('" + LiveScoreFixture.ACTIVE + "', '" + LiveScoreFixture.NOT_PLAYED +
                        "') and  date like '" + date + "%'";
                prepstat = con.prepareStatement (query);*/
                fixtures = prepstat.executeQuery();
                
                ArrayList games = new ArrayList();
                
                while (fixtures.next()) {
                    LiveScoreFixture fixture = new LiveScoreFixture();
                    
                    fixture.setCountryName(fixtures.getString("country_name"));
                    fixture.setDate(fixtures.getTimestamp("date").toString());
                    fixture.setGameId(fixtures.getString("game_id"));
                    fixture.setLeagueId(fixtures.getString("league_id"));
                    fixture.setLeagueName(fixtures.getString("league_name"));
                    fixture.setHomeTeam(fixtures.getString("home_team"));
                    fixture.setHomeScore(fixtures.getString("home_score"));
                    fixture.setAwayTeam(fixtures.getString("away_team"));
                    fixture.setAwayScore(fixtures.getString("away_score"));
                    fixture.setStatus(fixtures.getInt("status"));
                    fixture.setEventNotifSent(fixtures.getInt("event_notif_sent"));
                    
                    SQL = "SELECT * FROM livescore_game_alias where game_id='" + fixture.getGameId() + "'";
                    prepstat = con.prepareStatement(SQL);
                    rs = prepstat.executeQuery();
                    while (rs.next()) {
                        fixture.setAlias(rs.getString("alias"));
                    }
                    
                    games.add(fixture);
                }
                
                //insert array into hashmap
                groupedFixtures.put(id, games);
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        return groupedFixtures;
    }
    
    public static HashMap viewAllActiveFixturesForDate(String accountId, String date) throws Exception {
        HashMap groupedFixtures = new HashMap();
        
        String query;
        String SQL;
        ResultSet leagues = null;
        ResultSet fixtures = null;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection();
            
            SimpleDateFormat s_formatter = new SimpleDateFormat();
            java.util.Date validDate = null;
            try {
                validDate = s_formatter.parse(date);
            } catch (Exception ex) {
                validDate = Calendar.getInstance().getTime();
            }
            
            Calendar c = Calendar.getInstance();
            c.setTime(validDate);
            c.add(c.HOUR_OF_DAY, 24);
            Date endDate = c.getTime();
            
            query = "select ls.league_id from livescore_services ls inner join keyword_mapping km on km.mapping=ls.league_id where km.account_id='" + accountId + "'";
            prepstat = con.prepareStatement(query);
            leagues = prepstat.executeQuery();
            
            while (leagues.next()) {
                String id = new String();
                id = leagues.getString("league_id");
                
                query = "select * from livescore_fixtures lf where lf.league_id='" + id +"' and date(lf.date)=?";
                prepstat = con.prepareStatement(query);
                prepstat.setDate(1, new java.sql.Date(validDate.getTime()));
                fixtures = prepstat.executeQuery();
                
                ArrayList games = new ArrayList();
                
                while (fixtures.next()) {
                    LiveScoreFixture fixture = new LiveScoreFixture();
                    
                    fixture.setCountryName(fixtures.getString("country_name"));
                    fixture.setDate(fixtures.getTimestamp("date").toString());
                    fixture.setGameId(fixtures.getString("game_id"));
                    fixture.setLeagueId(fixtures.getString("league_id"));
                    fixture.setLeagueName(fixtures.getString("league_name"));
                    fixture.setHomeTeam(fixtures.getString("home_team"));
                    fixture.setHomeScore(fixtures.getString("home_score"));
                    fixture.setAwayTeam(fixtures.getString("away_team"));
                    fixture.setAwayScore(fixtures.getString("away_score"));
                    fixture.setStatus(fixtures.getInt("status"));
                    fixture.setEventNotifSent(fixtures.getInt("event_notif_sent"));
                    
                    SQL = "SELECT * FROM livescore_game_alias where game_id='" + fixture.getGameId() + "'";
                    prepstat = con.prepareStatement(SQL);
                    rs = prepstat.executeQuery();
                    while (rs.next()) {
                        fixture.setAlias(rs.getString("alias"));
                    }
                    
                    games.add(fixture);
                }
                
                //insert array into hashmap
                groupedFixtures.put(id, games);
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        return groupedFixtures;
    }
    
    public static ArrayList viewAllActiveFixturesInLeague(String date, String keyword, String accountId) throws Exception {
        ArrayList games = new ArrayList();
        
        String query;;
        String SQL;
        ResultSet fixtures = null;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection();
            
            SimpleDateFormat s_formatter = new SimpleDateFormat();
            java.util.Date validDate = null;
            try {
                validDate = s_formatter.parse(date);
            } catch (Exception ex) {
                validDate = Calendar.getInstance().getTime();
            }
            
            Calendar c = Calendar.getInstance();
            c.setTime(validDate);
            c.add(c.HOUR_OF_DAY, 24);
            Date endDate = c.getTime();
            
            //get available networks for content provider
            query = "select * from livescore_fixtures lf inner join keyword_mapping km on lf.league_id=km.mapping inner join service_definition sd on " +
                    "km.account_id=sd.account_id and km.keyword=sd.keyword where sd.keyword='" + keyword + "' and sd.account_id='" + accountId + "' and" +
                    " lf.status in ('" + LiveScoreFixture.ACTIVE + "', '" + LiveScoreFixture.NOT_PLAYED + "') and  (lf.date like '" + date + "%' or lf.date >='" +
                    new java.sql.Timestamp(validDate.getTime()).toString() + "' and lf.date <='" + new java.sql.Timestamp(endDate.getTime()).toString() + "')";
             
            prepstat = con.prepareStatement(query);
            //prepstat.setTimestamp (1, new java.sql.Timestamp (validDate.getTime ()));
            //prepstat.setTimestamp (2, new java.sql.Timestamp (endDate.getTime ()));
            fixtures = prepstat.executeQuery();
            
            while (fixtures.next()) {
                LiveScoreFixture fixture = new LiveScoreFixture();
                
                fixture.setCountryName(fixtures.getString("lf.country_name"));
                fixture.setDate(fixtures.getTimestamp("lf.date").toString());
                fixture.setGameId(fixtures.getString("lf.game_id"));
                fixture.setLeagueId(fixtures.getString("lf.league_id"));
                fixture.setLeagueName(fixtures.getString("lf.league_name"));
                fixture.setHomeTeam(fixtures.getString("lf.home_team"));
                fixture.setHomeScore(fixtures.getString("lf.home_score"));
                fixture.setAwayTeam(fixtures.getString("lf.away_team"));
                fixture.setAwayScore(fixtures.getString("lf.away_score"));
                fixture.setStatus(fixtures.getInt("lf.status"));
                fixture.setEventNotifSent(fixtures.getInt("lf.event_notif_sent"));
                
                SQL = "SELECT * FROM livescore_game_alias where game_id='" + fixture.getGameId() + "'";
                prepstat = con.prepareStatement(SQL);
                rs = prepstat.executeQuery();
                while (rs.next()) {
                    fixture.setAlias(rs.getString("alias"));
                }
                
                games.add(fixture);
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        return games;
    }
     public static ArrayList viewAllActiveFixturesForCPInLeague(String date, String keyword, String accountId) throws Exception {
        ArrayList games = new ArrayList();
        
        String query;;
        String SQL;
        ResultSet fixtures = null;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection();
            
            SimpleDateFormat s_formatter = new SimpleDateFormat();
            java.util.Date validDate = null;
            try {
                validDate = s_formatter.parse(date);
            } catch (Exception ex) {
                validDate = Calendar.getInstance().getTime();
            }
            
            Calendar c = Calendar.getInstance();
            c.setTime(validDate);
            c.add(c.HOUR_OF_DAY, 24);
            Date endDate = c.getTime();
            
                   //updated for per-provider fixture setting
           query = "select * from livescore_fixture_mgt lfm inner join livescore_fixtures lf on lf.game_id=lfm.game_id "+
                    "inner join keyword_mapping km on lf.league_id=km.mapping and lfm.account_id=km.account_id inner join service_definition sd on " +
                    "km.account_id=sd.account_id and km.keyword=sd.keyword where sd.keyword='" + keyword + "' and sd.account_id='" + accountId + "' and" +
                    " lf.status in ('" + LiveScoreFixture.ACTIVE + "', '" + LiveScoreFixture.NOT_PLAYED + "') and  (lf.date like '" + date + "%' or lf.date >='" +
                    new java.sql.Timestamp(validDate.getTime()).toString() + "' and lf.date <='" + new java.sql.Timestamp(endDate.getTime()).toString() + "')";
            
            prepstat = con.prepareStatement(query);
            //prepstat.setTimestamp (1, new java.sql.Timestamp (validDate.getTime ()));
            //prepstat.setTimestamp (2, new java.sql.Timestamp (endDate.getTime ()));
            fixtures = prepstat.executeQuery();
            
            while (fixtures.next()) {
                LiveScoreFixture fixture = new LiveScoreFixture();
                
                fixture.setCountryName(fixtures.getString("lf.country_name"));
                fixture.setDate(fixtures.getTimestamp("lf.date").toString());
                fixture.setGameId(fixtures.getString("lf.game_id"));
                fixture.setLeagueId(fixtures.getString("lf.league_id"));
                fixture.setLeagueName(fixtures.getString("lf.league_name"));
                fixture.setHomeTeam(fixtures.getString("lf.home_team"));
                fixture.setHomeScore(fixtures.getString("lf.home_score"));
                fixture.setAwayTeam(fixtures.getString("lf.away_team"));
                fixture.setAwayScore(fixtures.getString("lf.away_score"));
                fixture.setStatus(fixtures.getInt("lf.status"));
                fixture.setEventNotifSent(fixtures.getInt("lf.event_notif_sent"));
                
                SQL = "SELECT * FROM livescore_game_alias where game_id='" + fixture.getGameId() + "'";
                prepstat = con.prepareStatement(SQL);
                rs = prepstat.executeQuery();
                while (rs.next()) {
                    fixture.setAlias(rs.getString("alias"));
                }
                
                games.add(fixture);
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        return games;
    }
    
    public static ArrayList viewAllActiveFixturesInAllLeagues(String date) throws Exception {
        ArrayList games = new ArrayList();
        
        String query;;
        String SQL;
        ResultSet fixtures = null;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection();
            
            SimpleDateFormat s_formatter = new SimpleDateFormat();
            java.util.Date validDate = null;
            try {
                validDate = s_formatter.parse(date);
            } catch (Exception ex) {
                validDate = Calendar.getInstance().getTime();
            }
            
            Calendar c = Calendar.getInstance();
            c.setTime(validDate);
            c.add(c.HOUR_OF_DAY, 24);
            Date endDate = c.getTime();
            
            //get available networks for content provider
            query = "select * from livescore_fixtures lf where lf.status='" + LiveScoreFixture.NOT_PLAYED + "' and  (lf.date like '" + date + "%' or lf.date >='" +
                    new java.sql.Timestamp(validDate.getTime()).toString() + "' and lf.date <='" + new java.sql.Timestamp(endDate.getTime()).toString() + "')";
            prepstat = con.prepareStatement(query);
            //prepstat.setTimestamp (1, new java.sql.Timestamp (validDate.getTime ()));
            //prepstat.setTimestamp (2, new java.sql.Timestamp (endDate.getTime ()));
            fixtures = prepstat.executeQuery();
            
            while (fixtures.next()) {
                LiveScoreFixture fixture = new LiveScoreFixture();
                
                fixture.setCountryName(fixtures.getString("lf.country_name"));
                fixture.setDate(fixtures.getTimestamp("lf.date").toString());
                fixture.setGameId(fixtures.getString("lf.game_id"));
                fixture.setLeagueId(fixtures.getString("lf.league_id"));
                fixture.setLeagueName(fixtures.getString("lf.league_name"));
                fixture.setHomeTeam(fixtures.getString("lf.home_team"));
                fixture.setHomeScore(fixtures.getString("lf.home_score"));
                fixture.setAwayTeam(fixtures.getString("lf.away_team"));
                fixture.setAwayScore(fixtures.getString("lf.away_score"));
                fixture.setStatus(fixtures.getInt("lf.status"));
                fixture.setEventNotifSent(fixtures.getInt("lf.event_notif_sent"));
                
                SQL = "SELECT * FROM livescore_game_alias where game_id='" + fixture.getGameId() + "'";
                prepstat = con.prepareStatement(SQL);
                rs = prepstat.executeQuery();
                while (rs.next()) {
                    fixture.setAlias(rs.getString("alias"));
                }
                
                games.add(fixture);
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        return games;
    }
    
    
    public static ArrayList viewFixtures(String keyword, String accountId, String date) throws Exception {
        ArrayList games = new ArrayList();
        
        String query;;
        String SQL;
        ResultSet fixtures = null;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection();
            
            SimpleDateFormat s_formatter = new SimpleDateFormat("MM-dd-yyyy");
            java.util.Date validDate = null;
            try {
                validDate = s_formatter.parse(date);
            } catch (Exception ex) {
                validDate = Calendar.getInstance().getTime();
            }
            
            /*Calendar c = Calendar.getInstance ();
            c.setTime (validDate);
            c.add (c.HOUR_OF_DAY, 24);
            Date endDate = c.getTime ();*/
            
            //get available networks for content provider
            /*query = "select * from livescore_fixtures lf where lf.league_id in (select ls.league_id from livescore_services ls " +
                        "inner join keyword_mapping km on km.mapping=ls.league_id where km.account_id=?) and date(lf.date)=? order by league_id";*/
            
            query = "select * from livescore_fixtures lf inner join livescore_services ls on lf.league_id=ls.league_id "+
                    "inner join keyword_mapping km on km.mapping=ls.league_id where km.account_id=? and date(lf.date)= ?";
            
            if(!"".equals(keyword)){
                query+=" and km.keyword = '"+ keyword+"'";
                query+=" order by lf.game_id";
            } else{
                query+=" order by lf.league_id";
            }
            prepstat = con.prepareStatement(query);
            prepstat.setString(1,  accountId);
            prepstat.setDate(2, new java.sql.Date(validDate.getTime()) );
            //prepstat.setTimestamp (1, new java.sql.Timestamp (validDate.getTime ()));
            //prepstat.setTimestamp (2, new java.sql.Timestamp (endDate.getTime ()));
            fixtures = prepstat.executeQuery();
            
            while (fixtures.next()) {
                LiveScoreFixture fixture = new LiveScoreFixture();
                
                fixture.setCountryName(fixtures.getString("lf.country_name"));
                fixture.setDate(fixtures.getTimestamp("lf.date").toString());
                fixture.setGameId(fixtures.getString("lf.game_id"));
                fixture.setLeagueId(fixtures.getString("lf.league_id"));
                fixture.setLeagueName(fixtures.getString("lf.league_name"));
                fixture.setHomeTeam(fixtures.getString("lf.home_team"));
                fixture.setHomeScore(fixtures.getString("lf.home_score"));
                fixture.setAwayTeam(fixtures.getString("lf.away_team"));
                fixture.setAwayScore(fixtures.getString("lf.away_score"));
                fixture.setStatus(fixtures.getInt("lf.status"));
                fixture.setEventNotifSent(fixtures.getInt("lf.event_notif_sent"));
                
                SQL = "SELECT * FROM livescore_game_alias where game_id='" + fixture.getGameId() + "'";
                prepstat = con.prepareStatement(SQL);
                rs = prepstat.executeQuery();
                while (rs.next()) {
                    fixture.setAlias(rs.getString("alias"));
                }
                
                games.add(fixture);
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        return games;
    }
    
    
    
  /*
    public static com.rancard.util.PaginatedList viewFixtures(String keyword, String accountId, String date, int currPage, int pageSize, String sortCriterion, int sortOrder) throws Exception {
   
        ArrayList games = new ArrayList();
        com.rancard.util.PaginatedList pl;
        String query, query2, keyword_condition =" ";
        String orderBy_condition = " order by lf.league_id ";
        String SQL;
        ResultSet fixtures = null;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
   
        try {
            con = DConnect.getConnection();
   
            SimpleDateFormat s_formatter = new SimpleDateFormat("MM-dd-yyyy");
            java.util.Date validDate = null;
            try {
                validDate = s_formatter.parse(date);
            } catch (Exception ex) {
                validDate = Calendar.getInstance().getTime();
            }
   
   
   
            query = "select * from livescore_fixtures lf inner join livescore_services ls on lf.league_id=ls.league_id "+
                    "inner join keyword_mapping km on km.mapping=ls.league_id where km.account_id=? and date(lf.date)= ?";
   
            query2 = "select count(*) as numResults from livescore_fixtures lf inner join livescore_services ls on lf.league_id=ls.league_id "+
                    "inner join keyword_mapping km on km.mapping=ls.league_id where km.account_id=? and date(lf.date)= ?";
   
            if(!"".equals(keyword)){
                keyword_condition = " and km.keyword = '"+ keyword+"' ";
                query2+= keyword_condition;
            }
   
            if( !"".equals(sortCriterion) )
                orderBy_condition = " order by lf." + sortCriterion +" ";
   
   
   
   
            query+= keyword_condition + orderBy_condition + sortOrder;
   
            //get total Num of records
            prepstat = con.prepareStatement(query2);
            prepstat.setString(1,  accountId);
            prepstat.setDate(2, new java.sql.Date(validDate.getTime()) );
   
            rs = prepstat.executeQuery();
            int totalNumResults =0;
            while(rs.next()){
                totalNumResults = rs.getInt("numResults");
            }
            //----------end getting num of records------
   
            prepstat = con.prepareStatement(query);
            prepstat.setString(1,  accountId);
            prepstat.setDate(2, new java.sql.Date(validDate.getTime()) );
            //prepstat.setTimestamp (1, new java.sql.Timestamp (validDate.getTime ()));
            //prepstat.setTimestamp (2, new java.sql.Timestamp (endDate.getTime ()));
            fixtures = prepstat.executeQuery();
   
   
            int start = com.rancard.util.DisplayTagHelper.getStartPosition(currPage, pageSize);
            int i = 0;
            int totalNumPages = 0;
            //int x = 0;
            while (i < (start+ pageSize) && fixtures.next()) {
   
                if ( i == 0 ) {
                    //int x = totalNumResults
                    totalNumPages = totalNumResults / pageSize;
                    if( (totalNumResults % pageSize) > 0 )
                        totalNumPages += 1;
                }
                if(i>=start) {
                    LiveScoreFixture fixture = new LiveScoreFixture();
   
                    fixture.setCountryName(fixtures.getString("lf.country_name"));
                    fixture.setDate(fixtures.getTimestamp("lf.date").toString());
                    fixture.setGameId(fixtures.getString("lf.game_id"));
                    fixture.setLeagueId(fixtures.getString("lf.league_id"));
                    fixture.setLeagueName(fixtures.getString("lf.league_name"));
                    fixture.setHomeTeam(fixtures.getString("lf.home_team"));
                    fixture.setHomeScore(fixtures.getString("lf.home_score"));
                    fixture.setAwayTeam(fixtures.getString("lf.away_team"));
                    fixture.setAwayScore(fixtures.getString("lf.away_score"));
                    fixture.setStatus(fixtures.getInt("lf.status"));
                    fixture.setEventNotifSent(fixtures.getInt("lf.event_notif_sent"));
   
                    SQL = "SELECT * FROM livescore_game_alias where game_id='" + fixture.getGameId() + "'";
                    prepstat = con.prepareStatement(SQL);
                    rs = prepstat.executeQuery();
                    while (rs.next()) {
                        fixture.setAlias(rs.getString("alias"));
                    }
   
                    games.add(fixture);
                }
                i++;
            }
            //(List partialList,int fullListSize, int pageSize, int pageNumber, int ascending, String sortCriterion)
            pl  = new com.rancard.util.PaginatedList(games,totalNumResults,pageSize,currPage, sortOrder, sortCriterion  );
   
            if(pl == null)
                pl = (com.rancard.util.PaginatedList)com.rancard.util.PaginatedList.EMPTY_PAGE;
   
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        // return games;
        return pl;
    }*/
    
    public static ArrayList viewAllActiveFixturesInLeague(String date, String keyword, String accountId, String team) throws Exception {
        ArrayList games = new ArrayList();
        
        String query;
        String SQL;
        ResultSet fixtures = null;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection();
            
            SimpleDateFormat s_formatter = new SimpleDateFormat();
            java.util.Date validDate = null;
            try {
                validDate = s_formatter.parse(date);
            } catch (Exception ex) {
                validDate = Calendar.getInstance().getTime();
            }
            
            Calendar c = Calendar.getInstance();
            c.setTime(validDate);
            c.add(c.HOUR_OF_DAY, 24);
            Date endDate = c.getTime();
            
            //get available networks for content provider
            query = "select * from livescore_fixtures lf inner join keyword_mapping km on lf.league_id=km.mapping inner join service_definition sd on km.account_id=sd.account_id " +
                    "and km.keyword=sd.keyword where sd.keyword='" + keyword + "' and sd.account_id='" + accountId + "' and lf.status in ('" + LiveScoreFixture.ACTIVE +
                    "', '" + LiveScoreFixture.NOT_PLAYED + "') and (lf.date like '" + date + "%' or lf.date >='" + new java.sql.Timestamp (validDate.getTime ()).toString () + "' and lf.date <='" +
                    new java.sql.Timestamp (endDate.getTime ()).toString () + "') and (lf.home_team like '%" + team + "%' or away_team like '%" + team + "%')";
               
            prepstat = con.prepareStatement(query);
            fixtures = prepstat.executeQuery();
            
            while (fixtures.next()) {
                LiveScoreFixture fixture = new LiveScoreFixture();
                
                fixture.setCountryName(fixtures.getString("lf.country_name"));
                fixture.setDate(fixtures.getTimestamp("lf.date").toString());
                fixture.setGameId(fixtures.getString("lf.game_id"));
                fixture.setLeagueId(fixtures.getString("lf.league_id"));
                fixture.setLeagueName(fixtures.getString("lf.league_name"));
                fixture.setHomeTeam(fixtures.getString("lf.home_team"));
                fixture.setHomeScore(fixtures.getString("lf.home_score"));
                fixture.setAwayTeam(fixtures.getString("lf.away_team"));
                fixture.setAwayScore(fixtures.getString("lf.away_score"));
                fixture.setStatus(fixtures.getInt("lf.status"));
                fixture.setEventNotifSent(fixtures.getInt("lf.event_notif_sent"));
                
                SQL = "SELECT * FROM livescore_game_alias where game_id='" + fixture.getGameId() + "'";
                prepstat = con.prepareStatement(SQL);
                rs = prepstat.executeQuery();
                while (rs.next()) {
                    fixture.setAlias(rs.getString("alias"));
                }
                
                games.add(fixture);
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        return games;
    }
     public static ArrayList viewAllActiveFixturesForCPInLeague(String date, String keyword, String accountId, String team) throws Exception {
        ArrayList games = new ArrayList();
        
        String query;
        String SQL;
        ResultSet fixtures = null;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection();
            
            SimpleDateFormat s_formatter = new SimpleDateFormat();
            java.util.Date validDate = null;
            try {
                validDate = s_formatter.parse(date);
            } catch (Exception ex) {
                validDate = Calendar.getInstance().getTime();
            }
            
            Calendar c = Calendar.getInstance();
            c.setTime(validDate);
            c.add(c.HOUR_OF_DAY, 24);
            Date endDate = c.getTime();
            
               //updated for per-provider fixture setting
            query = "select * from livescore_fixture_mgt lfm inner join livescore_fixtures lf on lf.game_id=lfm.game_id inner join keyword_mapping km on lf.league_id=km.mapping " +
                    "and lfm.account_id=km.account_id inner join service_definition sd on km.account_id=sd.account_id " +
                    "and km.keyword=sd.keyword where sd.keyword='" + keyword + "' and sd.account_id='" + accountId + "' and lf.status in ('" + LiveScoreFixture.ACTIVE +
                    "', '" + LiveScoreFixture.NOT_PLAYED + "') and (lf.date like '" + date + "%' or lf.date >='" + new java.sql.Timestamp(validDate.getTime()).toString() + "' and lf.date <='" +
                    new java.sql.Timestamp(endDate.getTime()).toString() + "') and (lf.home_team like '%" + team + "%' or away_team like '%" + team + "%')";
            
            prepstat = con.prepareStatement(query);
            fixtures = prepstat.executeQuery();
            
            while (fixtures.next()) {
                LiveScoreFixture fixture = new LiveScoreFixture();
                
                fixture.setCountryName(fixtures.getString("lf.country_name"));
                fixture.setDate(fixtures.getTimestamp("lf.date").toString());
                fixture.setGameId(fixtures.getString("lf.game_id"));
                fixture.setLeagueId(fixtures.getString("lf.league_id"));
                fixture.setLeagueName(fixtures.getString("lf.league_name"));
                fixture.setHomeTeam(fixtures.getString("lf.home_team"));
                fixture.setHomeScore(fixtures.getString("lf.home_score"));
                fixture.setAwayTeam(fixtures.getString("lf.away_team"));
                fixture.setAwayScore(fixtures.getString("lf.away_score"));
                fixture.setStatus(fixtures.getInt("lf.status"));
                fixture.setEventNotifSent(fixtures.getInt("lf.event_notif_sent"));
                
                SQL = "SELECT * FROM livescore_game_alias where game_id='" + fixture.getGameId() + "'";
                prepstat = con.prepareStatement(SQL);
                rs = prepstat.executeQuery();
                while (rs.next()) {
                    fixture.setAlias(rs.getString("alias"));
                }
                
                games.add(fixture);
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        return games;
    }
    
    public static ArrayList viewAllFixtures() throws Exception {
        
        ArrayList fixtures = new ArrayList();
        
        String SQL;
        ResultSet rs = null;
        ResultSet rs2 = null;
        Connection con = null;
        PreparedStatement prepstat, prepstat2 = null;
        
        try {
            con = DConnect.getConnection();
            
            SQL = "SELECT * FROM livescore_fixtures";
            
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            
            while (rs.next()) {
                LiveScoreFixture fixture = new LiveScoreFixture();
                
                fixture.setCountryName(rs.getString("country_name"));
                fixture.setDate(rs.getTimestamp("date").toString());
                fixture.setGameId(rs.getString("game_id"));
                fixture.setLeagueId(rs.getString("league_id"));
                fixture.setLeagueName(rs.getString("league_name"));
                fixture.setHomeTeam(rs.getString("home_team"));
                fixture.setHomeScore(rs.getString("home_score"));
                fixture.setAwayTeam(rs.getString("away_team"));
                fixture.setAwayScore(rs.getString("away_score"));
                fixture.setStatus(rs.getInt("status"));
                fixture.setEventNotifSent(rs.getInt("event_notif_sent"));
                
                SQL = "SELECT * FROM livescore_game_alias where game_id='" + fixture.getGameId() + "'";
                prepstat = con.prepareStatement(SQL);
                rs2 = prepstat.executeQuery();
                while (rs2.next()) {
                    fixture.setAlias(rs2.getString("alias"));
                }
                
                fixtures.add(fixture);
            }
            
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        
        return fixtures;
    }
    
    public static void deleteFixture(String keyword) throws Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection();
            
            //from service definition table
            SQL = "delete from livescore_fixtures where game_id='" + keyword + "'";
            prepstat = con.prepareStatement(SQL);
            prepstat.execute();
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        } finally {
            
            if (con != null) {
                con.close();
            }
        }
    }
    
    public synchronized static void subscribeForGame(String accountId, String gameId, String msisdn) throws Exception {
        String SQL;
        String regId = "";
        int error = 0;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection();
            
            //check subscriber is not registered in addressbook. if not add subscriber to addressbook
            SQL = "select * from address_book where account_id='" + accountId + "' and msisdn='" + msisdn + "'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            
            if(!rs.next()){
                //has not registered. Register him and continue
                // String regId = "";
                SQL = "Insert into address_book (account_id,msisdn,registration_id) values(?,?,?)";
                prepstat = con.prepareStatement(SQL);
                regId = uidGen.generateNumberID(6);
                
                prepstat.setString(1, accountId);
                prepstat.setString(2, msisdn);
                prepstat.setString(3, regId);
                prepstat.execute();
            }
            
            SQL = "select * from service_subscription where keyword='" + gameId + "' and account_id='" + accountId + "' and msisdn='" + msisdn + "'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            
            if(!rs.next()){
                SQL = "Insert into service_subscription (subscription_date,msisdn,keyword,account_id,status) values(?,?,?,?,?)";
                prepstat = con.prepareStatement(SQL);
                prepstat.setTimestamp(1, new java.sql.Timestamp(java.util.Calendar.getInstance().getTime().getTime()));
                prepstat.setString(2, msisdn);
                prepstat.setString(3, gameId);
                prepstat.setString(4, accountId);
                prepstat.setInt(5, 1);
                prepstat.execute();
                
                //increment no_subscribers count
                SQL = "select no_subscribers from livescore_fixture_mgt where account_id=? and game_id=?";
                prepstat = con.prepareStatement(SQL);
                prepstat.setString(1,accountId);
                prepstat.setString(2,gameId);
                rs = prepstat.executeQuery();
                
                int no_subs = 1; // pre-increment
                if(rs.next()) {
                    no_subs += rs.getInt("no_subscribers");
                    SQL = "update livescore_fixture_mgt set no_subscribers = ? where account_id=? and game_id=?";
                } else {
                    SQL = "insert into livescore_fixture_mgt(no_subscribers,account_id,game_id) values(?,?,?)";
                }
                //store back
                prepstat = con.prepareStatement(SQL);
                prepstat.setInt(1,no_subs);
                prepstat.setString(2,accountId);
                prepstat.setString(3,gameId);
                prepstat.executeUpdate();
            }
        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex1) {
                    System.out.println(ex1.getMessage());
                }
                con = null;
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {}
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {}
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {}
                con = null;
            }
        }
    }
    
    public synchronized static void unsubscribeFromGame(String msisdn, String gameId, String accountId) throws Exception {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection();
            SQL = "delete from service_subscription where msisdn='" + msisdn + "' and keyword='" + gameId + "' and account_id='" + accountId + "'";
            prepstat = con.prepareStatement(SQL);
            prepstat.execute();
            
            //decrement no_subscribers count
            SQL = "select no_subscribers from livescore_fixture_mgt where account_id=? and game_id=?";
            prepstat = con.prepareStatement(SQL);
            prepstat.setString(1,accountId);
            prepstat.setString(2,gameId);
            rs = prepstat.executeQuery();
            
            int no_subs = 1; // pre-decrement
            if(rs.next()) {
                int count = rs.getInt("no_subscribers");
                if(count > 0){
                    no_subs = count - no_subs;
                }else{
                    no_subs = 0;
                }
                
                SQL = "update livescore_fixture_mgt set no_subscribers = ? where account_id=? and game_id=?";
            } else {
                SQL = "insert into livescore_fixture_mgt(no_subscribers,account_id,game_id) values(?,?,?)";
                no_subs = 0;
            }
            //store back
            prepstat = con.prepareStatement(SQL);
            prepstat.setInt(1,no_subs);
            prepstat.setString(2,accountId);
            prepstat.setString(3,gameId);
            prepstat.executeUpdate();
        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex1) {
                    System.out.println(ex1.getMessage());
                }
                con = null;
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    ;
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                    ;
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    ;
                }
                con = null;
            }
        }
    }
     public static void updateSubscriptionStatus (String gameId, int subStatus, int gameStatus) throws Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        boolean bError = false;
        
        try {
            con = DConnect.getConnection ();
            con.setAutoCommit (false);
            
            //for competition
            SQL = "update service_subscription set status=? where keyword=? and account_id in(select account_id from keyword_mapping " +
                    "where mapping in(select league_id from livescore_fixtures where game_id=? and status=?) )";
            
            prepstat = con.prepareStatement (SQL);
            //prepstat.clearBatch();
            
            prepstat.setString (1, ""+subStatus);
            prepstat.setString (2, gameId);
            prepstat.setString (3, gameId);
            prepstat.setString (4, ""+gameStatus);
            
            prepstat.execute ();
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            bError = true;
            throw new Exception (ex.getMessage ());
        } finally {
            
            if (con != null) {
                con.close ();
            }
        }
    }
    
    public static ArrayList viewDistinctGameTimesForDate(Date date, int status) throws Exception {
        int DEFAULT_ALLOWANCE = 0;
        int OUTLOOK_PERIOD = 0;
        
        try{
            String value = com.rancard.util.PropertyHolder.getPropsValue("LS_TRIGGER_ALLOWANCE");
            if(value != null && !value.equals("")){
                DEFAULT_ALLOWANCE = Integer.parseInt(value);
            }else{
                DEFAULT_ALLOWANCE = 4;
            }
        }catch(Exception e){
            DEFAULT_ALLOWANCE = 4;
        }
        
        try{
            String value = com.rancard.util.PropertyHolder.getPropsValue("LS_GAME_OUTLOOK");
            if(value != null && !value.equals("")){
                OUTLOOK_PERIOD = Integer.parseInt(value);
            }else{
                OUTLOOK_PERIOD = 24;
            }
        }catch(Exception e){
            OUTLOOK_PERIOD = 24;
        }
        
        ArrayList dates = new ArrayList();
        
        String query;
        ResultSet rs_dates = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int addition = OUTLOOK_PERIOD + DEFAULT_ALLOWANCE;
        c.add(c.HOUR_OF_DAY, addition);
        Date endDate = c.getTime();
        
        try {
            con = DConnect.getConnection();
            
            query = "select date from livescore_fixtures where livescore_fixtures.status='" + status + "' and  (date >= ? and date <= ?) group by date (date), hour (date)";
            prepstat = con.prepareStatement(query);
            prepstat.setTimestamp(1, new java.sql.Timestamp(date.getTime()));
            prepstat.setTimestamp(2, new java.sql.Timestamp(endDate.getTime()));
            rs_dates = prepstat.executeQuery();
            
            while (rs_dates.next()) {
                LiveScoreFixture fixture = new LiveScoreFixture();
                String dateStr = rs_dates.getString("date");
                dates.add(dateStr);
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        return dates;
    }
    
    public static String viewGameIdForAlias(String alias) throws Exception {
        String gameId = "";
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            
            SQL = "SELECT game_id FROM livescore_game_alias where alias='" + alias + "'";
            
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            
            while (rs.next()) {
                gameId = rs.getString("game_id");
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        
        return gameId;
    }
    
    public static String viewAliasForGameId(String gameId) throws Exception {
        String alias = "";
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            
            SQL = "SELECT alias FROM livescore_game_alias where game_id='" + gameId + "'";
            
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            
            while (rs.next()) {
                alias = rs.getString("alias");
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        
        return alias;
    }
}
