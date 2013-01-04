/*
 * LiveScoreGame.java
 *
 * Created on January 14, 2007, 12:12 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.mobility.infoserver.livescore;

/**
 *
 * @author Messenger
 */
public class LiveScoreFixture {
    
    public static final int UNDECIDED = 7;
    public static final int POSTPONED = 6;
    public static final int CANCELLED = 5;
    public static final int INTERRUPTED = 4;
    public static final int OTHER = 3;
    public static final int ACTIVE = 2;
    public static final int PLAYED = 1;
    public static final int NOT_PLAYED = 0;
    
    public static final int NOTIF_NOT_SENT = 0;
    public static final int NOTIF_SENT = 1;
    
    private String gameId;
    private String countryName;
    private String leagueId;
    private String leagueName;
    private String homeTeam;
    private String homeScore;
    private String awayTeam;
    private String awayScore;
    private String date;
    private int status;
    private int eventNotifSent;;
    private String alias;
    
    /** Creates a new instance of LiveScoreGame */
    public LiveScoreFixture () {
        this.gameId = "";
        this.countryName = "";
        this.leagueId = "";
        this.leagueName = "";
        this.homeTeam = "";
        this.homeScore = "na";
        this.awayTeam = "";
        this.awayScore = "na";
        this.date = "";
        this.status = this.NOT_PLAYED;
        this.eventNotifSent = this.NOTIF_NOT_SENT;
        this.alias = "";
    }
    
    public LiveScoreFixture (String gameId, String countryName, String leagueId, String leagueName, String homeTeam, String homeScore, String awayTeam, 
            String awayScore, String date, int status, String alias) {
        this.gameId = gameId;
        this.countryName = countryName;
        this.leagueId = leagueId;
        this.leagueName = leagueName;
        this.homeTeam = homeTeam;
        this.homeScore = homeScore;
        this.awayTeam = awayTeam;
        this.awayScore = awayScore;
        this.date = date;
        this.status = status;
        this.eventNotifSent = this.NOTIF_NOT_SENT;
        this.alias = alias;
    }

    public String getGameId () {
        return gameId;
    }

    public void setGameId (String gameId) {
        this.gameId = gameId;
    }

    public String getCountryName () {
        return countryName;
    }

    public void setCountryName (String countryName) {
        this.countryName = countryName;
    }

    public String getLeagueName () {
        return leagueName;
    }

    public void setLeagueName (String leagueName) {
        this.leagueName = leagueName;
    }

    public String getHomeTeam () {
        return homeTeam;
    }

    public void setHomeTeam (String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getDate () {
        return date;
    }

    public void setDate (String date) {
        this.date = date;
    }

    public int getStatus () {
        return status;
    }

    public void setStatus (int status) {
        this.status = status;
    }

    public String getLeagueId () {
        return leagueId;
    }

    public void setLeagueId (String leagueId) {
        this.leagueId = leagueId;
    }

    public String getAwayTeam () {
        return awayTeam;
    }

    public void setAwayTeam (String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getHomeScore () {
        return homeScore;
    }

    public void setHomeScore (String homeScore) {
        this.homeScore = homeScore;
    }

    public String getAwayScore () {
        return awayScore;
    }

    public void setAwayScore (String awayScore) {
        this.awayScore = awayScore;
    }

    public String getAlias () {
        return alias;
    }

    public void setAlias (String alias) {
        this.alias = alias;
    }

    public int getEventNotifSent () {
        return eventNotifSent;
    }

    public void setEventNotifSent (int eventNotifSent) {
        this.eventNotifSent = eventNotifSent;
    }
    
}
