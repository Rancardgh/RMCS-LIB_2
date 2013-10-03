package com.rancard.mobility.infoserver.common.services;

/**
 * Created with IntelliJ IDEA.
 * User: Mustee
 * Date: 9/17/13
 * Time: 5:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServiceLabel {
    private String accountID;
    private String keyword;
    private String header;
    private String footer;

    public ServiceLabel(String accountID, String keyword, String header, String footer){
        this.accountID = accountID;
        this.keyword = keyword;
        this.header = header;
        this.footer = footer;
    }


    public String getAccountID() {
        return accountID;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getHeader() {
        return header;
    }

    public String getFooter() {
        return footer;
    }
}
