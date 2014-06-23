package com.rancard.common.key;

import com.rancard.common.ServiceDefinition;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Mustee on 3/30/2014.
 */
@Embeddable
public class ServiceSubscriptionKey implements Serializable {

    @Column(length = 100, nullable = false)
    private String keyword;

    @Column(name = "account_id", length = 100, nullable = false)
    private String accountID;

    @Column(name = "msisdn", nullable = false, length = 100)
    private String msisdn;

    protected ServiceSubscriptionKey() {
    }

    public ServiceSubscriptionKey(String accountID, String keyword, String msisdn) {
        this.keyword = keyword;
        this.msisdn = msisdn;
        this.accountID = accountID;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
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
