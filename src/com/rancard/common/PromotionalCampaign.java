package com.rancard.common;

import javax.persistence.*;

/**
 * Created by Mustee on 3/31/2014.
 */
@Entity
@Table(name = "promotional_campaign", schema = "", catalog = "rmcs")
public class PromotionalCampaign {

    @Id
    @Column(name = "promo_id", nullable = false, length = 10)
    private String promoID;

    @Lob
    @Column(name = "promo_msg", nullable = false)
    private String promoMessage;

    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CPUser cpUser;

    @Column(name = "promo_response_code", length = 64)
    private String promoResponseCode;

    @Column(name = "campaign_name", length = 20)
    private String campaignName;

    protected PromotionalCampaign() {
    }

    public PromotionalCampaign(String promoID, String promoMessage, CPUser cpUser, String promoResponseCode, String campaignName) {
        this.promoID = promoID;
        this.promoMessage = promoMessage;
        this.cpUser = cpUser;
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

    public CPUser getCpUser() {
        return cpUser;
    }

    public void setCpUser(CPUser cpUser) {
        this.cpUser = cpUser;
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
}
