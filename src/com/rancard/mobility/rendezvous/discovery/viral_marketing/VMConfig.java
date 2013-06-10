/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.rendezvous.discovery.viral_marketing;

/**
 *
 * @author kweku
 */
public class VMConfig {
    
    private String accountId;
    private String defaultService;
    private String recipientUnifiedProfix;
    private String senderUnifiedPrefix;
    private String shareUrl;
    private int numOfSignificanrDigitsForRecipient;
    private int numOfSignificanrDigitsForSender;
    private boolean useMsisdnAsSender;
    
    public VMConfig () {
        accountId = "";
        defaultService = "";
        recipientUnifiedProfix = "";
        senderUnifiedPrefix = "";
        shareUrl = "";
        numOfSignificanrDigitsForRecipient = 0;
        numOfSignificanrDigitsForSender = 0;
        useMsisdnAsSender = true;
    }
    
    public VMConfig (String accountId, String defaultService, String recipientUnifiedProfix, String senderUnifiedPrefix, String shareUrl,
            int numOfSignificanrDigitsForRecipient, int numOfSignificanrDigitsForSender, boolean useMsisdnAsSender) {
        this.accountId = accountId;
        this.defaultService = defaultService;
        this.recipientUnifiedProfix = recipientUnifiedProfix;
        this.senderUnifiedPrefix = senderUnifiedPrefix;
        this.shareUrl = shareUrl;
        this.numOfSignificanrDigitsForRecipient = numOfSignificanrDigitsForRecipient;
        this.numOfSignificanrDigitsForSender = numOfSignificanrDigitsForSender;
        this.useMsisdnAsSender = useMsisdnAsSender;
    }

    /**
     * @return the accountId
     */
    public String getAccountId () {
        return accountId;
    }

    /**
     * @return the defaultService
     */
    public String getDefaultService () {
        return defaultService;
    }

    /**
     * @return the recipientUnifiedProfix
     */
    public String getRecipientUnifiedProfix () {
        return recipientUnifiedProfix;
    }

    /**
     * @return the senderUnifiedPrefix
     */
    public String getSenderUnifiedPrefix () {
        return senderUnifiedPrefix;
    }

    /**
     * @return the numOfSignificanrDigitsForRecipient
     */
    public int getNumOfSignificanrDigitsForRecipient () {
        return numOfSignificanrDigitsForRecipient;
    }

    /**
     * @return the numOfSignificanrDigitsForSender
     */
    public int getNumOfSignificanrDigitsForSender () {
        return numOfSignificanrDigitsForSender;
    }

    /**
     * @return the useMsisdnAsSender
     */
    public boolean isMsisdnSender () {
        return useMsisdnAsSender;
    }

    /**
     * @param accountId the accountId to set
     */
    public void setAccountId (String accountId) {
        this.accountId = accountId;
    }

    /**
     * @param defaultService the defaultService to set
     */
    public void setDefaultService (String defaultService) {
        this.defaultService = defaultService;
    }

    /**
     * @param recipientUnifiedProfix the recipientUnifiedProfix to set
     */
    public void setRecipientUnifiedProfix (String recipientUnifiedProfix) {
        this.recipientUnifiedProfix = recipientUnifiedProfix;
    }

    /**
     * @param senderUnifiedPrefix the senderUnifiedPrefix to set
     */
    public void setSenderUnifiedPrefix (String senderUnifiedPrefix) {
        this.senderUnifiedPrefix = senderUnifiedPrefix;
    }

    /**
     * @param numOfSignificanrDigitsForRecipient the numOfSignificanrDigitsForRecipient to set
     */
    public void setNumOfSignificanrDigitsForRecipient (int numOfSignificanrDigitsForRecipient) {
        this.numOfSignificanrDigitsForRecipient = numOfSignificanrDigitsForRecipient;
    }

    /**
     * @param numOfSignificanrDigitsForSender the numOfSignificanrDigitsForSender to set
     */
    public void setNumOfSignificanrDigitsForSender (int numOfSignificanrDigitsForSender) {
        this.numOfSignificanrDigitsForSender = numOfSignificanrDigitsForSender;
    }

    /**
     * @param useMsisdnAsSender the useMsisdnAsSender to set
     */
    public void setMsisdnAsSender () {
        this.useMsisdnAsSender = true;
    }
    
    public void unsetMsisdnAsSender () {
        this.useMsisdnAsSender = false;
    }

    /**
     * @return the shareUrl
     */
    public String getShareUrl () {
        return shareUrl;
    }

    /**
     * @param shareUrl the shareUrl to set
     */
    public void setShareUrl (String shareUrl) {
        this.shareUrl = shareUrl;
    }
    
}
