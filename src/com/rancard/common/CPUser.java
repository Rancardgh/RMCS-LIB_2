package com.rancard.common;

import java.io.Serializable;

/**
 * Created by Mustee on 3/27/2014.
 */

public class CPUser implements Serializable {
    private String id;
    private String name;
    private String username;
    private String password;
    private String defaultSMSC;
    private String logoURL;
    private String defaultService;
    private String defaultLanguage;
    private double bandwidthBalance;
    private double inboxBalance;
    private double outboxBalance;
    private double accountBalance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDefaultSMSC() {
        return defaultSMSC;
    }

    public void setDefaultSMSC(String defaultSMSC) {
        this.defaultSMSC = defaultSMSC;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public String getDefaultService() {
        return defaultService;
    }

    public void setDefaultService(String defaultService) {
        this.defaultService = defaultService;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public Double getBandwidthBalance() {
        return bandwidthBalance;
    }

    public void setBandwidthBalance(Double bandwidthBalance) {
        this.bandwidthBalance = bandwidthBalance;
    }

    public Double getInboxBalance() {
        return inboxBalance;
    }

    public void setInboxBalance(Double inboxBalance) {
        this.inboxBalance = inboxBalance;
    }

    public Double getOutboxBalance() {
        return outboxBalance;
    }

    public void setOutboxBalance(Double outboxBalance) {
        this.outboxBalance = outboxBalance;
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        this.accountBalance = accountBalance;
    }

}