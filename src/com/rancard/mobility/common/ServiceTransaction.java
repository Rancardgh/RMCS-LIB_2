/*
 * ServiceTransaction.java
 *
 * Created on February 13, 2007, 3:00 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.mobility.common;

/**
 *
 * @author Messenger
 */
public abstract class ServiceTransaction {
    
    private String transactionId;
    private String keyword;
    private String accountId;
    private String msg;
    private String msisdn;
    private String callBackUrl;
    private java.sql.Timestamp date;
    private String pricePoint;
    private int isBilled;
    private int isCompleted;
    
    /** Creates a new instance of ServiceTransaction */
    public ServiceTransaction () {
        transactionId = "";
        keyword = "";
        accountId = "";
        msg = "";
        msisdn = "";
        date = new java.sql.Timestamp (new java.util.Date ().getTime ());
        pricePoint = "";
        isBilled = 0;
        isCompleted = 0;
    }
    
    /** Creates a new instance of ServiceTransaction */
    public ServiceTransaction (String transactionId, String keyword, String accountId, String msg, String msisdn, String callBackUrl, java.sql.Timestamp date, String pricePoint, 
            int isBilled, int isCompleted) {
        this.transactionId = transactionId;
        this.keyword = keyword;
        this.accountId = accountId;
        this.msg = msg;
        this.msisdn = msisdn;
        this.callBackUrl = callBackUrl;
        this.date = date;
        this.pricePoint = pricePoint;
        this.isBilled = isBilled;
        this.isCompleted = isCompleted;
    }

    public String getTransactionId () {
        return transactionId;
    }

    public void setTransactionId (String transactionId) {
        this.transactionId = transactionId;
    }

    public String getKeyword () {
        return keyword;
    }

    public void setKeyword (String keyword) {
        this.keyword = keyword;
    }

    public String getAccountId () {
        return accountId;
    }

    public void setAccountId (String accountId) {
        this.accountId = accountId;
    }

    public String getMsg () {
        return msg;
    }

    public void setMsg (String msg) {
        this.msg = msg;
    }

    public String getMsisdn () {
        return msisdn;
    }

    public void setMsisdn (String msisdn) {
        this.msisdn = msisdn;
    }

    public java.sql.Timestamp getDate () {
        return date;
    }

    public void setDate (java.sql.Timestamp date) {
        this.date = date;
    }

    public int getIsBilled () {
        return isBilled;
    }

    public void setIsBilled (int isBilled) {
        this.isBilled = isBilled;
    }

    public int getIsCompleted () {
        return isCompleted;
    }

    public void setIsCompleted (int isCompleted) {
        this.isCompleted = isCompleted;
    }

    public String getCallBackUrl () {
        return callBackUrl;
    }

    public void setCallBackUrl (String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }

    public String getPricePoint () {
        return pricePoint;
    }

    public void setPricePoint (String pricePoint) {
        this.pricePoint = pricePoint;
    }
    
}
