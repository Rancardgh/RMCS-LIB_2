/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.infoserver.viralmarketing;

/**
 *
 * @author john
 */
public class PromoImpression {

    private long hashCode;
    private String msisdn;
    private String promoResponseCode;
    private String accountId;
    private java.util.Date viewDate;
    private String inventory_keyword;

    public PromoImpression() {
        this.hashCode = 0;
        this.msisdn = "";
        this.promoResponseCode = "";
        this.accountId = "";
        this.inventory_keyword = "";
        this.viewDate = new java.util.Date ();

    }

    public boolean exists() {
        if (this.getHashCode() == 0) {
            return false;
        }
        return true;
    }

    /**
     * @return the hashCode
     */
    public long getHashCode () {
        return hashCode;
    }

    /**
     * @param hashCode the hashCode to set
     */
    public void setHashCode (long hashCode) {
        this.hashCode = hashCode;
    }

    /**
     * @return the msisdn
     */
    public String getMsisdn () {
        return msisdn;
    }

    /**
     * @param msisdn the msisdn to set
     */
    public void setMsisdn (String msisdn) {
        this.msisdn = msisdn;
    }

    /**
     * @return the promoResponseCode
     */
    public String getKeyword () {
        return promoResponseCode;
    }

    /**
     * @param promoResponseCode the promoResponseCode to set
     */
    public void setKeyword (String promoResponseCode) {
        this.promoResponseCode = promoResponseCode;
    }

    /**
     * @return the accountId
     */
    public String getAccountId () {
        return accountId;
    }

    /**
     * @param accountId the accountId to set
     */
    public void setAccountId (String accountId) {
        this.accountId = accountId;
    }

    /**
     * @return the viewDate
     */
    public java.util.Date getViewDate () {
        return viewDate;
    }

    /**
     * @param viewDate the viewDate to set
     */
    public void setViewDate (java.util.Date viewDate) {
        this.viewDate = viewDate;
    }

    /**
     * @return the inventory_keyword
     */
    public String getInventory_keyword() {
        return inventory_keyword;
    }

    /**
     * @param inventory_keyword the inventory_keyword to set
     */
    public void setInventory_keyword(String inventory_keyword) {
        this.inventory_keyword = inventory_keyword;
    }
}

