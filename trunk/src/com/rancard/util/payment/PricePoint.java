/*
 * PricePoint.java
 *
 * Created on May 24, 2007, 10:48 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.util.payment;

/**
 *
 * @author Messenger
 */
public class PricePoint {
       
    private String pricePointId;
    private String networkPrefix;
    private String value;
    private String currency;
    private String billingMech;
    private String billingUrl;
    
    /** Creates a new instance of PricePoint */
    public PricePoint () {
        pricePointId = "";
        networkPrefix = "";
        value = "";
        currency = "";
        billingMech = PaymentManager.SMSC_BILL;
        billingUrl = "";
    }
    
    /** Creates a new instance of PricePoint */
    public PricePoint (String pricePointId, String networkPrefix, String value, String currenc, String billingMech, String billingUrl) {
        this.pricePointId = pricePointId;
        this.networkPrefix = networkPrefix;
        this.value = value;
        this.currency = currency;
        this.billingMech = billingMech;
        this.billingUrl = billingUrl;
    }

    public String getPricePointId () {
        return pricePointId;
    }

    public void setPricePointId (String pricePointId) {
        this.pricePointId = pricePointId;
    }

    public String getNetworkPrefix () {
        return networkPrefix;
    }

    public void setNetworkPrefix (String networkPrefix) {
        this.networkPrefix = networkPrefix;
    }

    public String getValue () {
        return value;
    }

    public void setValue (String value) {
        this.value = value;
    }

    public String getCurrency () {
        return currency;
    }

    public void setCurrency (String currency) {
        this.currency = currency;
    }

    public String getBillingUrl () {
        return billingUrl;
    }

    public void setBillingUrl (String billingUrl) {
        this.billingUrl = billingUrl;
    }

    public String getBillingMech () {
        return billingMech;
    }

    public void setBillingMech (String billingMech) {
        this.billingMech = billingMech;
    }
    
}
