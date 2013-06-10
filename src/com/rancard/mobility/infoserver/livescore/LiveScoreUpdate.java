/*
 * LiveScoreUpdate.java
 *
 * Created on December 18, 2006, 3:42 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.mobility.infoserver.livescore;

import java.util.ArrayList;

/**
 *
 * @author Messenger
 */
public class LiveScoreUpdate {
    
    private String countryName;
    private String leagueName;
    private String leagueId;
    private String eventStatus;
    private String eventId;
    private String updateId;
    private ArrayList participants;
    private ArrayList scores;
    private String eventName;
    private String eventDate;
    private String trigger;
    private String englishMessage;
    private String frenchMessage;
    private String publishDate;
    private String mode; //property to determin type of update: whether live game update or other
    
    /** Creates a new instance of LiveScoreUpdate */
    public LiveScoreUpdate () {
        countryName = "";
        leagueName = "";
        leagueId = "";
        eventStatus = "";
        eventId = "";
        updateId = "";
        eventDate = "";
        eventName = "";
        trigger = "";
        englishMessage = "";
        frenchMessage = "";
        participants = new ArrayList ();
        scores = new ArrayList ();
        publishDate = new java.sql.Timestamp (new java.util.Date ().getTime ()).toString ();
        mode ="";
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

    public String getLeagueId () {
        return leagueId;
    }

    public void setLeagueId (String leagueId) {
        this.leagueId = leagueId;
    }

    public String getEventStatus () {
        return eventStatus;
    }

    public void setEventStatus (String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getTrigger () {
        return trigger;
    }

    public void setTrigger (String trigger) {
        this.trigger = trigger;
    }

    public String getEnglishMessage () {
        return englishMessage;
    }

    public void setEnglishMessage (String message) {
        this.englishMessage = message;
    }

    public String getEventName () {
        return eventName;
    }

    public void setEventName (String eventName) {
        this.eventName = eventName;
    }

    public String getEventDate () {
        return eventDate;
    }

    public void setEventDate (String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventId () {
        return eventId;
    }

    public void setEventId (String eventId) {
        this.eventId = eventId;
    }

    public ArrayList getParticipants () {
        return participants;
    }

    public void setParticipants (ArrayList participants) {
        this.participants = participants;
    }

    public ArrayList getScores () {
        return scores;
    }

    public void setScores (ArrayList scores) {
        this.scores = scores;
    }

    public String getUpdateId () {
        return updateId;
    }

    public void setUpdateId (String updateId) {
        this.updateId = updateId;
    }

    public String getPublishDate () {
        return publishDate;
    }

    public void setPublishDate (String publishDate) {
        this.publishDate = publishDate;
    }

    public String getFrenchMessage () {
        return frenchMessage;
    }

    public void setFrenchMessage (String message) {
        this.frenchMessage = message;
    }
    
     public String getMode () {
        return this.mode;
    }

    public void setMode (String mode) {
        this.mode = mode;
    }
}
