/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.rendezvous.discovery.viral_marketing;

public class VMCampaign {

    private String campaignId;
    private String accountId;
    private String keyword;
    private String messageSender;
    private String message;
    private String vmHowTo;
    private String followUpMsgSuccess;
    private String followUpMsgError;
    //private String networkId;
    private java.util.Date lastUpdated;
    private String inviteAcceptedMsg;

    public VMCampaign () {
        this.campaignId = "";
        this.accountId = "";
        this.keyword = "";
        this.messageSender = "";
        this.message = "";
        //this.networkId = "";
        this.vmHowTo = "";
        this.followUpMsgSuccess = "";
        this.followUpMsgError = "";
        this.lastUpdated = new java.util.Date ();
    }

    public VMCampaign (String campaignId, String accountId, String keyword, String messageSender,
            String message, String networkId, String vmHowTo, String followUpMsgSuccess, String followUpMsgError) {
        this.campaignId = campaignId;
        this.accountId = accountId;
        this.keyword = keyword;
        this.messageSender = messageSender;
        this.message = message;
        //this.networkId = networkId;
        this.vmHowTo = vmHowTo;
        this.followUpMsgSuccess = followUpMsgSuccess;
        this.followUpMsgError = followUpMsgError;
        this.lastUpdated = new java.util.Date ();
    }

    public java.util.Date getLastUpdated () {
        return lastUpdated;
    }

    public String getCampaignId () {
        return campaignId;
    }

    public String getAccountId () {
        return accountId;
    }

    public String getKeyword () {
        return keyword;
    }

    public String getMessageSender () {
        return messageSender;
    }

    public String getMessage () {
        return message;
    }

    public void setLastUpdated (java.util.Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setCampaignId (String campaignId) {
        this.campaignId = campaignId;
    }

    public void setAccountId (String accountId) {
        this.accountId = accountId;
    }

    public void setKeyword (String keyword) {
        this.keyword = keyword;
    }

    public void setMessageSender (String messageSender) {
        this.messageSender = messageSender;
    }

    public void setMessage (String message) {
        this.message = message;
    }

    /**
     * @return the vmHowTo
     */
    public String getVmHowTo () {
        return vmHowTo;
    }

    /**
     * @param vmHowTo the vmHowTo to set
     */
    public void setVmHowTo (String vmHowTo) {
        this.vmHowTo = vmHowTo;
    }

    /**
     * @return the networkId
     */
    /*public String getNetworkId () {
        return networkId;
    }

    /**
     * @param networkId the networkId to set
     
    public void setNetworkId (String networkId) {
        this.networkId = networkId;
    }*/

    /**
     * @return the followUpMsgSuccess
     */
    public String getFollowUpMsgSuccess () {
        return followUpMsgSuccess;
    }

    /**
     * @return the followUpMsgError
     */
    public String getFollowUpMsgError () {
        return followUpMsgError;
    }

    /**
     * @param followUpMsgSuccess the followUpMsgSuccess to set
     */
    public void setFollowUpMsgSuccess (String followUpMsgSuccess) {
        this.followUpMsgSuccess = followUpMsgSuccess;
    }

    /**
     * @param followUpMsgError the followUpMsgError to set
     */
    public void setFollowUpMsgError (String followUpMsgError) {
        this.followUpMsgError = followUpMsgError;
    }

    public String getInviteAcceptedMsg() {
        return inviteAcceptedMsg;
    }

    public void setInviteAcceptedMsg(String inviteAcceptedMsg) {
        this.inviteAcceptedMsg = inviteAcceptedMsg;
    }
   
    
}
