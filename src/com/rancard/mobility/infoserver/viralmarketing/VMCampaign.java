/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.infoserver.viralmarketing;

import java.util.Date;

/**
 *
 * @author nii
 */
public class VMCampaign {

    public final static String DEFAULT_FOLLOW_UP_SUCCESS_MSG = "Your invitation has been sent! Thank you.";
    public final static String DEFAULT_FOLLOW_UP_ERROR_MSG = "Sorry your invitation couldn't be sent. Please ensure the numbers you submitted are correctly formatted.";
    public final static String DEFAULT_ALREADY_INVITED_MESSAGE = "Sorry your invitation couldn't be sent, the number has already been invited to this service.";
    
    private String campaignID;
    private String accountID;
    private String keyword;
    private String messageSender;
    private String message;
    private String howToMessage;
    private String followUpMessageSuccess;
    private String followUpMessageError;
    private String inviteAcceptedMessage;
    private String alreadyInvitedMessage;
    private Date lastUpdated;
    private boolean isActive;
    private int pushWaitTime;

    public VMCampaign(String campaignID, String accountID, String keyword, String messageSender, String message,
            String howToMessage, String followUpMessageSuccess, String followUpMessageError, String inviteAcceptedMessage,
            String alreadyInvitedMessage, Date lastUpdated, boolean isActive, int pushWaitTime) {
        this.campaignID = campaignID;
        this.accountID = accountID;
        this.keyword = keyword;
        this.messageSender = messageSender;
        this.message = message;
        this.howToMessage = howToMessage;
        this.followUpMessageSuccess = followUpMessageSuccess;
        this.followUpMessageError = followUpMessageError;
        this.inviteAcceptedMessage = inviteAcceptedMessage;
        this.alreadyInvitedMessage = alreadyInvitedMessage;
        this.lastUpdated = lastUpdated;
        this.isActive = isActive;
        this.pushWaitTime = pushWaitTime;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getMessageSender() {
        return messageSender;
    }

    public String getMessage() {
        return message;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setMessageSender(String messageSender) {
        this.messageSender = messageSender;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCampaignID() {
        return campaignID;
    }

    public void setCampaignID(String campaignID) {
        this.campaignID = campaignID;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getHowToMessage() {
        return howToMessage;
    }

    public void setHowToMessage(String howToMessage) {
        this.howToMessage = howToMessage;
    }

    public String getFollowUpMessageSuccess() {
        return followUpMessageSuccess;
    }

    public void setFollowUpMessageSuccess(String followUpMessageSuccess) {
        this.followUpMessageSuccess = followUpMessageSuccess;
    }

    public String getFollowUpMessageError() {
        return followUpMessageError;
    }

    public void setFollowUpMessageError(String followUpMessageError) {
        this.followUpMessageError = followUpMessageError;
    }

    public String getInviteAcceptedMessage() {
        return inviteAcceptedMessage;
    }

    public void setInviteAcceptedMessage(String inviteAcceptedMessage) {
        this.inviteAcceptedMessage = inviteAcceptedMessage;
    }

    public String getAlreadyInvitedMessage() {
        return alreadyInvitedMessage;
    }

    public void setAlreadyInvitedMessage(String alreadyInvitedMessage) {
        this.alreadyInvitedMessage = alreadyInvitedMessage;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * @return the pushWaitTime
     */
    public int getPushWaitTime() {
        return pushWaitTime;
    }

    /**
     * @param pushWaitTime the pushWaitTime to set
     */
    public void setPushWaitTime(int pushWaitTime) {
        this.pushWaitTime = pushWaitTime;
    }

    public static String getDefaultHowToMessage(String shortCode) {
        return "Send INVITE followed by your friend's number to " + shortCode + " to share this service with them. Add FROM followed by your name to the message to personalize it.";
    }

    public static String getDefaultInvitationAcceptedMessage(String username) {
        return "You recently invited " + username + " to use your favourite service. We're excited to inform you that your invitation was just accepted!";
    }
}
