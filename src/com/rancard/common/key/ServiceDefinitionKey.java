package com.rancard.common.key;

import com.rancard.common.CPUser;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Mustee on 3/29/2014.
 */
@Embeddable
public class ServiceDefinitionKey implements Serializable {

    @Column(name = "account_id", nullable = false, length = 10)
    private String accountID;

    @Column(nullable = false, length = 100)
    private String keyword;

    protected ServiceDefinitionKey(){}

    public ServiceDefinitionKey(String accountID, String keyword) {
        this.accountID = accountID;
        this.keyword = keyword;
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

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
