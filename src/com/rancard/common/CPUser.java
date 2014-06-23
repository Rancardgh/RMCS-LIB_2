package com.rancard.common;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Mustee on 3/27/2014.
 */
@Entity
@Table(name = "cp_user", schema = "", catalog = "rmcs")
@NamedQueries({
        @NamedQuery(name = "CPUser.findAll", query = "SELECT c FROM CPUser c")
})
public class CPUser implements Serializable {
    @Id
    @Column(nullable = false, length = 100)
    private String id;

    @Basic
    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 100, name = "default_smsc")
    private String defaultSMSC;

    @Column(nullable = false, length = 255, name = "logo_url")
    private String logoURL;

    @Column(nullable = false, length = 100, name = "default_service")
    private String defaultService;

    @Column(nullable = false, length = 100, name = "default_language")
    private String defaultLanguage;

    @Column(nullable = false, name = "bandwidth_balance")
    private Double bandwidthBalance;

    @Column(nullable = false, name = "inbox_balance")
    private Double inboxBalance;

    @Column(nullable = false, name = "outbox_balance")
    private Double outboxBalance;

    @Column(nullable = false, name = "accountBalance")
    private Double accountBalance;

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

    public static List<CPUser> getAll(EntityManager em){
        return em.createNamedQuery("CPUser.findAll", CPUser.class).getResultList();
    }
}