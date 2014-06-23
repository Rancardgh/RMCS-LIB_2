package com.rancard.common.key;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by Mustee on 4/5/2014.
 */
@Embeddable
public class VMTransactionKey implements Serializable {
    @Column(name = "campaign_id", length = 30, nullable = false)
    private String campaignID;

    @Column(name = "recruiter_msisdn", length = 30, nullable = false)
    private String recruiterMSISDN;

    @Column(name = "recipient_msisdn", length = 30, nullable = false)
    private String recipientMSISDN;

    protected VMTransactionKey() {
    }

    public VMTransactionKey(String campaignID, String recruiterMSISDN, String recipientMSISDN) {
        this.campaignID = campaignID;
        this.recruiterMSISDN = recruiterMSISDN;
        this.recipientMSISDN = recipientMSISDN;
    }

    public String getCampaignID() {
        return campaignID;
    }

    public void setCampaignID(String campaignID) {
        this.campaignID = campaignID;
    }

    public String getRecruiterMSISDN() {
        return recruiterMSISDN;
    }

    public void setRecruiterMSISDN(String recruiterMSISDN) {
        this.recruiterMSISDN = recruiterMSISDN;
    }

    public String getRecipientMSISDN() {
        return recipientMSISDN;
    }

    public void setRecipientMSISDN(String recipientMSISDN) {
        this.recipientMSISDN = recipientMSISDN;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
