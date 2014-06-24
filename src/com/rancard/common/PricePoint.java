package com.rancard.common;


import com.rancard.common.db.DConnect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.logging.Logger;

/**
 * Created by Mustee on 3/29/2014.
 */
public class PricePoint {
    private static Logger logger = Logger.getLogger(PricePoint.class.getName());

    public static final String SMSC_BILL = "1";
    public static final String PIN_REDEMPTION = "2";
    public static final String OT_BILL = "3";
    public static final String AIRTEL_BILL = "4";
    public static final String GLO_BILL = "5";
    public static final String UNIFIED_BILL = "6";

    private String pricePointID;
    private String network_prefix;
    private String value;
    private String currency;
    private String billingMech;
    private String billingURL;


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

    public static PricePoint find(String pricePointID) throws Exception {
        final String query = "SELECT * FROM price_points WHERE price_point_id = '" + pricePointID + "'";

        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = DConnect.getConnection();
            rs = conn.createStatement().executeQuery(query);

            if(rs.next()){
                return new PricePoint(rs.getString("price_point_id"), rs.getString("network_prefix"), rs.getString("value"),
                        rs.getString("currency"), rs.getString("billing_mech"), rs.getString("billing_url"));
            }
            return null;
        } catch (Exception ex) {
            logger.severe("Problem creating transaction: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (conn != null) {
                conn.close();
            }

        }
    }
}
