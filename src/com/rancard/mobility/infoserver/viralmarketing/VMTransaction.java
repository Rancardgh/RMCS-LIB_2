/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rancard.mobility.infoserver.viralmarketing;

/**
 *
 * @author nii
 */
public class VMTransaction {

    private String transactionDate;
    private String campaignId;
    private String recruiterMsisdn;
    private String recipientMsisdn;
    private String status;

    public VMTransaction() {
        this.transactionDate = "";
        this.campaignId = "";
        this.recruiterMsisdn = "";
        this.recipientMsisdn = "";
        this.status = "";
    }

    public VMTransaction(String campaignId, String recruiterMsisdn, String recipientMsisdn, String status) {        
        this.campaignId = campaignId;
        this.recruiterMsisdn = recruiterMsisdn;
        this.recipientMsisdn = recipientMsisdn;
        this.status = status;
        this.transactionDate = "";
    }

    public String getTransactionDate() {
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

    public void setTransactionDate(String transactionDate) {
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

    public boolean isEmptyTransaction() {
        if (this.transactionDate.equals("") &&
                this.campaignId.equals("") &&
                this.recruiterMsisdn.equals("") &&
                this.recipientMsisdn.equals("") &&
                this.status.equals(""))
            return true;
        else
            return false;
    }

}