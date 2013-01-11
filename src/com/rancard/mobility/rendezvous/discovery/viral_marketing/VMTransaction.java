/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rancard.mobility.rendezvous.discovery.viral_marketing;

/**
 *
 * @author nii
 */
public class VMTransaction {

    private java.util.Date transactionDate;
    private String campaignId;
    private String recruiterMsisdn;
    //private String recruiterNetworkId;
    private String recipientMsisdn;
    //private String recipientNetworkId;
    private String status;
    private String itemId;
    private String category;
    private String shortUrl;

    public VMTransaction() {
        this.transactionDate = new java.util.Date ();
        this.campaignId = "";
        this.recruiterMsisdn = "";
        //this.recruiterNetworkId = "";
        this.recipientMsisdn = "";
        //this.recipientNetworkId = "";
        this.status = "";
        this.itemId = "";
        this.category = "";
        this.shortUrl = "";
    }

    public VMTransaction(String campaignId, String recruiterMsisdn, String recruiterNetworkId, String recipientMsisdn, 
            String recipientNetworkId, String status, String itemId, String category, String shortUrl) {
        this.campaignId = campaignId;
        this.recruiterMsisdn = recruiterMsisdn;
        //this.recruiterNetworkId = recruiterNetworkId;
        this.recipientMsisdn = recipientMsisdn;
        //this.recipientNetworkId = recipientNetworkId;
        this.status = status;
        this.transactionDate = new java.util.Date ();
        this.itemId = itemId;
        this.category = category;
        this.shortUrl = shortUrl;
    }

    public java.util.Date getTransactionDate() {
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

    public void setTransactionDate(java.util.Date transactionDate) {
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
        if (this.campaignId.equals("") &&
                this.recruiterMsisdn.equals("") &&
                this.recipientMsisdn.equals("") &&
                this.status.equals(""))
            return true;
        else
            return false;
    }

    /**
     * @return the itemId
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * @param itemId the itemId to set
     */
    public void setItemId(String itemId) {
        this.itemId = itemId;
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
     * @return the shortUrl
     */
    public String getShortUrl() {
        return shortUrl;
    }

    /**
     * @param shortUrl the shortUrl to set
     */
    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

/*
    public String getRecruiterNetworkId () {
        return recruiterNetworkId;
    }


    public void setRecruiterNetworkId (String recruiterNetworkId) {
        this.recruiterNetworkId = recruiterNetworkId;
    }


    public String getRecipientNetworkId () {
        return recipientNetworkId;
    }


    public void setRecipientNetworkId (String recipientNetworkId) {
        this.recipientNetworkId = recipientNetworkId;
    }
*/
}
