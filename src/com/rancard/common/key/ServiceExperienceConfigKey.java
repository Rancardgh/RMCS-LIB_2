package com.rancard.common.key;

import com.rancard.common.CPSite;
import com.rancard.common.ServiceDefinition;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Mustee on 3/30/2014.
 */
@Embeddable
public class ServiceExperienceConfigKey implements Serializable {

    @Column(length = 150, nullable = false)
    private String keyword;

    @Column(name = "account_id", length = 30, nullable = false)
    private String accountID;

    @Column(name = "site_id", nullable = false, length = 45)
    private String siteID;

    protected ServiceExperienceConfigKey() {
    }

    public ServiceExperienceConfigKey(String accountID, String siteID, String keyword) {
        this.keyword = keyword;
        this.siteID = siteID;
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

    public String getSiteID() {
        return siteID;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
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
