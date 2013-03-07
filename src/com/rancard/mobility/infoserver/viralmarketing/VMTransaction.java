/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.infoserver.viralmarketing;

import java.util.Date;

/**
 *
 * @author nii
 * @Updated Mustee
 */
public class VMTransaction {

    private Date transactionDate;
    private String campaignId;
    private String recruiterMsisdn;
    private String recipientMsisdn;
    private String status;

    public VMTransaction(String campaignId, String recruiterMsisdn, String recipientMsisdn, String status, Date transactionDate) {
        this.campaignId = campaignId;
        this.recruiterMsisdn = recruiterMsisdn;
        this.recipientMsisdn = recipientMsisdn;
        this.status = status;
        this.transactionDate = transactionDate;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public String getRecruiterMsisdn() {
        return recruiterMsisdn;
    }

    public String getRecipientMsisdn() {
        return recipientMsisdn;
    }

    public String getStatus() {
        return status;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public void setRecruiterMsisdn(String recruiterMsisdn) {
        this.recruiterMsisdn = recruiterMsisdn;
    }

    public void setRecipientMsisdn(String recipientMsisdn) {
        this.recipientMsisdn = recipientMsisdn;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
