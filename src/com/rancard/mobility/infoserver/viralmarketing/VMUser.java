/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rancard.mobility.infoserver.viralmarketing;

/**
 *
 * @author nii
 */
public class VMUser {

    private String msisdn;
    private String accountId;
    private String keyword;
    private String username;
    private String regDate;
    private int points;

    public VMUser() {
        this.accountId = "";
        this.msisdn = "";
        this.keyword = "";
        this.username = "";
        this.regDate = "";
        this.points = 0;
    }

    public VMUser(String msisdn, String accountId, String keyword, String username, int points) {
        this.accountId = accountId;
        this.msisdn = msisdn;
        this.keyword = keyword;
        this.username = username;
        this.points = points;
        this.regDate = "";
    }

    public String getAccountId() {
        return accountId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getUsername() {
        return username;
    }

    public String getRegDate() {
        return regDate;
    }

    public int getPoints() {
        return points;
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

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public void setPoints(int points) {
        this.points = points;
    }

}