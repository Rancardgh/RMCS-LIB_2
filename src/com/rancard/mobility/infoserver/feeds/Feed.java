/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.infoserver.feeds;

import java.net.URL;

/**
 *
 * @author nii
 */
public class Feed {

    private String feedId;
    private URL feedURL;
    private String feedName;
    private boolean isActive;
    private String username;
    private String password;

    /** Creates a new instance of CPUserFeeds */
    public Feed() {
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public URL getFeedURL() {
        return feedURL;
    }

    public void setFeedURL(URL feedURL) {
        this.feedURL = feedURL;
    }

    public String getFeedName() {
        return feedName;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

}