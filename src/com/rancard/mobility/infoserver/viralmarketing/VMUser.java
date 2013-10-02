/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rancard.mobility.infoserver.viralmarketing;

import java.util.Date;

/**
 *
 * @author nii
 */
public class VMUser {

    private String msisdn;
    private String accountID;
    private String keyword;
    private String username;
    private Date regDate;
    private int points;

    public VMUser(Date regDate, String msisdn, String accountID, String keyword, String username, int points) {
        this.accountID = accountID;
        this.msisdn = msisdn;
        this.keyword = keyword;
        this.username = username;
        this.points = points;
        this.regDate = regDate;
    }

    public String getAccountID() {
        return accountID;
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

    public Date getRegDate() {
        return regDate;
    }

    public int getPoints() {
        return points;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
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