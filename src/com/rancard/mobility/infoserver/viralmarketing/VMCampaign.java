/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rancard.mobility.infoserver.viralmarketing;

/**
 *
 * @author nii
 */
public class VMCampaign {

    private String campaignId;
    private String accountId;
    private String keyword;
    private String messageSender;
    private String message;
    private String lastUpdated;

    public VMCampaign() {
        this.campaignId = "";
        this.accountId = "";
        this.keyword = "";
        this.messageSender = "";
        this.message = "";
        this.lastUpdated = "";
    }

    public VMCampaign(String campaignId, String accountId, String keyword, String messageSender, String message) {
        this.campaignId = campaignId;
        this.accountId = accountId;
        this.keyword = keyword;
        this.messageSender = messageSender;
        this.message = message;
        this.lastUpdated = "";
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public String getAccountId() {
        return accountId;
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

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
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
}
