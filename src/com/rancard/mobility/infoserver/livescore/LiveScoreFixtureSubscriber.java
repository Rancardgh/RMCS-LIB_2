/*
 * LiveScoreFixtureSubscriber.java
 *
 * Created on April 18, 2007, 3:47 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.mobility.infoserver.livescore;

/**
 *
 * @author Administrator
 */
public class LiveScoreFixtureSubscriber {
    
    private String gameId;
    private String leagueId;
    private String msisdn;
    private String date;
    private String status;
    /** Creates a new instance of LiveScoreFixtureSubscriber */
    public LiveScoreFixtureSubscriber() {
            gameId = "";
            leagueId = "";
            msisdn ="";
            date = "";
            status ="";
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
    
}
