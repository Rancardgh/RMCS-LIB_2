package com.rancard.common;

import com.rancard.common.db.DConnect;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by Mustee on 4/5/2014.
 */
public class VMCampaign {
    public final static String DEFAULT_FOLLOW_UP_SUCCESS_MSG = "Your invitation has been sent! Thank you.";
    public final static String DEFAULT_FOLLOW_UP_ERROR_MSG = "Sorry your invitation couldn't be sent. Please ensure the numbers you submitted are correctly formatted.";
    public final static String DEFAULT_ALREADY_INVITED_MESSAGE = "Sorry your invitation couldn't be sent, the number has already been invited to this service.";
    public final static String DEFAULT_HOW_TO_MESSAGE = "Send INVITE followed by your friend's number to @@shortCode@@ to share this service with them. Add FROM followed by your name to the message to personalize it.";
    public final static String DEFAULT_INVITATION_ACCEPTED_MESSAGE = "You recently invited @@username@@ to use your favourite service. We're excited to inform you that your invitation was just accepted!";
    private static Logger logger = Logger.getLogger(VMCampaign.class.getName());

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



    public VMCampaign(String campaignID, String accountID, String keyword, String messageSender, String message, String howToMessage, String followUpMessageSuccess, String followUpMessageError, String inviteAcceptedMessage, String alreadyInvitedMessage, Date lastUpdated, boolean isActive, int pushWaitTime) {
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getMessageSender() {
        return messageSender;
    }

    public void setMessageSender(String messageSender) {
        this.messageSender = messageSender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getPushWaitTime() {
        return pushWaitTime;
    }

    public void setPushWaitTime(int pushWaitTime) {
        this.pushWaitTime = pushWaitTime;
    }

    public static VMCampaign findByService(String accountID, String keyword) throws Exception {
        final String query = "SELECT * FROM vm_campaigns_temp WHERE account_id = '"+accountID + "' AND keyword = '"+keyword+"'";

        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = DConnect.getConnection();
            rs = conn.createStatement().executeQuery(query);
            if(rs.next()){
                return new VMCampaign(rs.getString("campaign_id"), rs.getString("account_id"), rs.getString("keyword"), rs.getString("message_sender"), rs.getString("message"),
                        rs.getString("how_to_msg"), rs.getString("follow_up_msg_success"), rs.getString("follow_up_msg_error"), rs.getString("invite_accepted_msg"),
                        rs.getString("already_inv_msg"), rs.getDate("last_updated"), rs.getBoolean("is_active"), rs.getInt("push_wait_time"));
            }

            return null;
        } catch (Exception ex) {
            logger.severe("Problem creating transaction: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (conn != null) {
                conn.close();
            }

        }

    }
}
