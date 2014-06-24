package com.rancard.common;


import com.rancard.common.db.DConnect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.logging.Logger;

/**
 * Created by Mustee on 3/31/2014.
 */
public class PromotionalCampaign {
    private static Logger logger = Logger.getLogger(PromotionalCampaign.class.getName());
    private String promoID;
    private String promoMessage;
    private String accountId;
    private String promoResponseCode;
    private String campaignName;


    public PromotionalCampaign(String promoID, String promoMessage, String accountId, String promoResponseCode, String campaignName) {
        this.promoID = promoID;
        this.promoMessage = promoMessage;
        this.accountId = accountId;
        this.promoResponseCode = promoResponseCode;
        this.campaignName = campaignName;
    }

    public String getPromoID() {
        return promoID;
    }

    public void setPromoID(String promoID) {
        this.promoID = promoID;
    }

    public String getPromoMessage() {
        return promoMessage;
    }

    public void setPromoMessage(String promoMessage) {
        this.promoMessage = promoMessage;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPromoResponseCode() {
        return promoResponseCode;
    }

    public void setPromoResponseCode(String promoResponseCode) {
        this.promoResponseCode = promoResponseCode;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public static PromotionalCampaign find(String promoID) throws Exception {
        final String query = "SELECT * FROM promotional_campaign WHERE promo_id = '" + promoID + "'";

        Connection conn= null;
        ResultSet rs = null;
        try{
            logger.info("Getting promotional_campaign: " + query);
            conn = DConnect.getConnection();
            rs = conn.createStatement().executeQuery(query);

            if(rs.next()){
                return new PromotionalCampaign(rs.getString("promo_id"), rs.getString("promo_msg"), rs.getString("account_id"), rs.getString("promo_response_code"), rs.getString("campaign_name"));
            }
            return null;
        }catch (Exception e) {
            logger.severe("Problem getting services: " + e.getMessage());
            throw new Exception(e.getMessage());
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
