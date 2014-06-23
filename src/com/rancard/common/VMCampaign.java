package com.rancard.common;

import com.rancard.mobility.contentserver.EMF;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Mustee on 4/5/2014.
 */
@Entity
@Table(name = "vm_campaigns_temp", schema = "", catalog = "rmcs")
public class VMCampaign {
    public final static String DEFAULT_FOLLOW_UP_SUCCESS_MSG = "Your invitation has been sent! Thank you.";
    public final static String DEFAULT_FOLLOW_UP_ERROR_MSG = "Sorry your invitation couldn't be sent. Please ensure the numbers you submitted are correctly formatted.";
    public final static String DEFAULT_ALREADY_INVITED_MESSAGE = "Sorry your invitation couldn't be sent, the number has already been invited to this service.";
    public final static String DEFAULT_HOW_TO_MESSAGE = "Send INVITE followed by your friend's number to @@shortCode@@ to share this service with them. Add FROM followed by your name to the message to personalize it.";
    public final static String DEFAULT_INVITATION_ACCEPTED_MESSAGE = "You recently invited @@username@@ to use your favourite service. We're excited to inform you that your invitation was just accepted!";

    @Id
    @Column(name = "campaign_id", length = 30, nullable = false)
    private String campaignID;

    @Column(name = "account_id", length = 30, nullable = false)
    private String accountID;

    @Column(name = "keyword", length = 30, nullable = false)
    private String keyword;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "account_id", referencedColumnName = "account_id", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "keyword", referencedColumnName = "keyword", nullable = false, insertable = false, updatable = false)
    })
    private ServiceDefinition serviceDefinition;

    @Column(name = "message_sender", length = 30, nullable = false)
    private String messageSender;

    @Lob
    @Column(name = "message")
    private String message;

    @Column(name = "how_to_msg")
    private String howToMessage;

    @Column(name = "follow_up_msg_success")
    private String followUpMessageSuccess;

    @Column(name = "follow_up_msg_error")
    private String followUpMessageError;

    @Column(name = "invite_accepted_msg")
    private String inviteAcceptedMessage;

    @Column(name = "already_inv_msg")
    private String alreadyInvitedMessage;

    @Column(name = "last_updated", nullable = false)
    private Date lastUpdated;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "push_wait_time", nullable = false)
    private int pushWaitTime;

    protected VMCampaign() {
    }


    public VMCampaign(String campaignID, String accountID, String keyword, ServiceDefinition serviceDefinition, String messageSender, String message, String howToMessage, String followUpMessageSuccess, String followUpMessageError, String inviteAcceptedMessage, String alreadyInvitedMessage, Date lastUpdated, boolean isActive, int pushWaitTime) {
        this.campaignID = campaignID;
        this.accountID = accountID;
        this.keyword = keyword;
        this.serviceDefinition = serviceDefinition;
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

    public ServiceDefinition getServiceDefinition() {
        return serviceDefinition;
    }

    public void setServiceDefinition(ServiceDefinition serviceDefinition) {
        this.serviceDefinition = serviceDefinition;
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

    public static VMCampaign findByService(EntityManager em, ServiceDefinition serviceDefinition) {
        List<VMCampaign> vmCampaigns = em.createQuery("SELECT vm FROM VMCampaign vm WHERE vm.accountID = :accountID " +
                "AND vm.keyword = :keyword").setParameter("accountID", serviceDefinition.getServiceDefinitionKey().getAccountID())
                .setParameter("keyword", serviceDefinition.getServiceDefinitionKey().getKeyword()).getResultList();

        return (vmCampaigns.size() == 0) ? null : vmCampaigns.get(0);
    }
}
