/*
 * ServiceSubscriber.java
 *
 * Created on August 31, 2007, 1:46 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 * 
 * Edited by Ahmed Mustapha
 * Move towards making class immutable. Removed all setter methods. 
 * No functionality was broken
 */
package com.rancard.mobility.infoserver.common.services;

import java.util.Date;

public class ServiceSubscriber {

    private String msisdn;
    private String accountID;
    private String keyword;
    private Date subscriptionDate;
    private Date nextSubscriptionDate;
    private int status;
    private int billingType;
    public static final int BILLING_TYPE_NON = 0;
    public static final int ON_DEMAND_BILLING = 1;
    public static final int MONTHLY_BILLING = 2;
    public static final int STATUS_ACTIVE = 1;
    public static final int STATUS_INACTIVE = 0;

    /**
     * Creates a new instance of ServiceSubscriber
     */
    public ServiceSubscriber(String msisdn, String accountID, String keyword, Date subscriptionDate, Date nextSubscriptionDate, int status, int billingType) {
        this.msisdn = msisdn;
        this.accountID = accountID;
        this.keyword = keyword;
        this.subscriptionDate = subscriptionDate;
        this.nextSubscriptionDate = nextSubscriptionDate;
        this.status = status;
        this.billingType = billingType;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public Date getSubscriptionDate() {
        return subscriptionDate;
    }

    public Date getNextSubscriptionDate() {
        return nextSubscriptionDate;
    }

    public int getStatus() {
        return status;
    }

    public int getBillingType() {
        return billingType;
    }
}
