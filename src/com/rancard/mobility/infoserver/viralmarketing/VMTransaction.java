/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rancard.mobility.infoserver.viralmarketing;

import java.util.Date;

/**
 *
 * @author nii
 * @updated Mustee
 */
public class VMTransaction {

    private Date transactionDate;
    private String campaignID;
    private String recruiterMsisdn;
    private String recipientMsisdn;
    private VMTransactionStatus status;
    private String itemID;
    private String category;
    private String shortURL;

    public VMTransaction(Date transactionDate, String campaignID, String recruiterMsisdn, String recipientMsisdn, VMTransactionStatus status, 
            String itemID, String category, String shortURL) {        
        this.campaignID = campaignID;
        this.recruiterMsisdn = recruiterMsisdn;
        this.recipientMsisdn = recipientMsisdn;
        this.status = status;
        this.transactionDate = transactionDate;
        this.itemID = itemID;
        this.category =category;
        this.shortURL =shortURL;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public String getCampaignID() {
        return campaignID;
    }

    public String getRecruiterMsisdn() {
        return recruiterMsisdn;
    }

    public String getRecipientMsisdn() {
        return recipientMsisdn;
    }

    public VMTransactionStatus getStatus() {
        return status;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setCampaignId(String campaignID) {
        this.campaignID = campaignID;
    }

    public void setRecruiterMsisdn(String recruiterMsisdn) {
        this.recruiterMsisdn = recruiterMsisdn;
    }

    public void setRecipientMsisdn(String recipientMsisdn) {
        this.recipientMsisdn = recipientMsisdn;
    }

    public void setStatus(VMTransactionStatus status) {
        this.status = status;
    }


    public String getItemID() {
        return itemID;
    }

    /**
     * @param itemID the itemID to set
     */
    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return the shortURL
     */
    public String getShortURL() {
        return shortURL;
    }

    /**
     * @param shortURL the shortURL to set
     */
    public void setShortURL(String shortURL) {
        this.shortURL = shortURL;
    }
}