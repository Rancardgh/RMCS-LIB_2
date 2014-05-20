package com.rancard.mobility.infoserver.viralmarketing;

import java.util.Date;

public class VMCampaign {

    private String campaignId;
    private String accountId;
    private String keyword;
    private String messageSender;
    private String message;
    private Date lastUpdated;

    public VMCampaign(String campaignId, String accountId, String keyword, String messageSender, String message, Date lastUpdated) {
        this.campaignId = campaignId;
        this.accountId = accountId;
        this.keyword = keyword;
        this.messageSender = messageSender;
        this.message = message;
        this.lastUpdated = lastUpdated;
    }

    public Date getLastUpdated() {
        return this.lastUpdated;
    }

    public String getCampaignId() {
        return this.campaignId;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public String getMessageSender() {
        return this.messageSender;
    }

    public String getMessage() {
        return this.message;
    }

    public void setLastUpdated(Date lastUpdated) {
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
