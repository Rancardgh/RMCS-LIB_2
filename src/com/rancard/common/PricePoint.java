package com.rancard.common;

import javax.persistence.*;

/**
 * Created by Mustee on 3/29/2014.
 */
@Entity
@Table(name = "price_points", schema = "", catalog = "rmcs")
public class PricePoint {
    public static final String SMSC_BILL = "1";
    public static final String PIN_REDEMPTION = "2";
    public static final String OT_BILL = "3";
    public static final String AIRTEL_BILL = "4";
    public static final String GLO_BILL = "5";
    public static final String UNIFIED_BILL = "6";

    @Id
    @Column(name="price_point_id", length = 100, nullable = false)
    private String pricePointID;

    @Column(name = "network_prefix", length = 100)
    private String network_prefix;

    @Column(name = "value", length = 100)
    private String value;

    @Column(name = "currency", length = 100)
    private String currency;

    @Column(name = "billing_mech", length = 100)
    private String billingMech;

    @Lob
    @Column(name = "billing_url")
    private String billingURL;

    protected PricePoint() {
    }

    public PricePoint(String pricePointID, String network_prefix, String value, String currency, String billingMech, String billingURL) {
        this.pricePointID = pricePointID;
        this.network_prefix = network_prefix;
        this.value = value;
        this.currency = currency;
        this.billingMech = billingMech;
        this.billingURL = billingURL;
    }

    public String getPricePointID() {
        return pricePointID;
    }

    public void setPricePointID(String pricePointID) {
        this.pricePointID = pricePointID;
    }

    public String getNetwork_prefix() {
        return network_prefix;
    }

    public void setNetwork_prefix(String network_prefix) {
        this.network_prefix = network_prefix;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBillingMech() {
        return billingMech;
    }

    public void setBillingMech(String billingMech) {
        this.billingMech = billingMech;
    }

    public String getBillingURL() {
        return billingURL;
    }

    public void setBillingURL(String billingURL) {
        this.billingURL = billingURL;
    }
}
