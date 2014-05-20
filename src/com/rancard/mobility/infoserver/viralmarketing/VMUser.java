package com.rancard.mobility.infoserver.viralmarketing;

import java.util.Date;

public class VMUser {

    private String msisdn;
    private String accountId;
    private String keyword;
    private String username;
    private Date regDate;
    private int points;

    public VMUser(String msisdn, String accountId, String keyword, String username, Date regDate, int points) {
        this.accountId = accountId;
        this.msisdn = msisdn;
        this.keyword = keyword;
        this.username = username;
        this.points = points;
        this.regDate = regDate;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public String getMsisdn() {
        return this.msisdn;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public String getUsername() {
        return this.username;
    }

    public Date getRegDate() {
        return this.regDate;
    }

    public int getPoints() {
        return this.points;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
