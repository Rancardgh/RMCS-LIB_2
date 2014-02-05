/*
 * CPUserFeeds.java
 *
 * Created on September 6, 2007, 2:00 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.mobility.infoserver.feeds;

/**
 *
 * @author USER
 */
public class CPUserFeeds {
    private String feedId = "";
    private String URL = "";
    private String keyword ="";
    private String cpUserId = "";
    private int allowedAge = 0;
    private String regexReject = "";

    private int msgDlrPrority; //determines how quickly messages should be pushed out

    /** Creates a new instance of CPUserFeeds */
    public CPUserFeeds() {
    }
    
    public CPUserFeeds(String cpUserID, String keyword, String feedID, int allowedAge, String regexReject, int msgDlrPrority){
        this.cpUserId =cpUserID;
        this.keyword = keyword;
        this.feedId = feedID;
        this.allowedAge = allowedAge;
        this.regexReject = regexReject;
        this.msgDlrPrority = msgDlrPrority;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public void setAllowedAge(int allowedAge) {
        this.allowedAge = allowedAge;
    }

    public int getAllowedAge() {
        return allowedAge;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCpUserId() {
        return cpUserId;
    }

    public void setCpUserId(String cpUserId) {
        this.cpUserId = cpUserId;
    }
    
    public String getRegexReject() {
        return regexReject;
    }

    public void setRegexReject(String regexReject) {
        this.regexReject = regexReject;
    }

    /**
     * @return the msgDlrPrority
     */
    public int getMsgDlrPrority () {
        return msgDlrPrority;
    }

    /**
     * @param msgDlrPrority the msgDlrPrority to set
     */
    public void setMsgDlrPrority (int msgDlrPrority) {
        this.msgDlrPrority = msgDlrPrority;
    }
}
