package com.rancard.mobility.infoserver.common.inbox;
import java.sql.*;
import com.rancard.common.*;

import java.sql.*;

public class InboxEntry {

    private String keyword;
    private String msisdn;
    private java.sql.Timestamp dateReceived;
    private String message;
    private String messageId;
    //private String questionId;
    private String shortCode;
    private String accountId;
    private int viewed;

    public InboxEntry() {
        this.keyword = "";
        this.msisdn = "";
        this.dateReceived = new java.sql.Timestamp(
                new java.util.Date().getTime());
        this.message = "";
        this.messageId = new com.rancard.common.uidGen().generateNumberID (10);
        //this.questionId = "";
        this.shortCode = "";
        this.accountId = "";
        this.viewed = 0;
    }
    
    public static InboxEntry  getLastInboxEntryFor(String keyword, String msisdn ) throws Exception{
        InboxEntry ie = new InboxEntry();
           
        String SQL;
        ResultSet rs ;
        Connection con = null;
        PreparedStatement prepstat;
        try {
            con = DConnect.getConnection ();
            SQL =  "select ib.* from inbox ib where ib.keyword = '"+keyword+"' and mobileno  = '"+msisdn +"' order by date_voted desc;";
            prepstat = con.prepareStatement (SQL);
           
            rs = prepstat.executeQuery ();
            
            if (rs.next ()) {
                InboxEntry entry = new InboxEntry ();
                entry.setDateReceived (rs.getTimestamp ("date_voted"));
                entry.setMessage (rs.getString ("response"));
                entry.setMessageId (rs.getString ("voteid"));
                entry.setMsisdn (rs.getString ("mobileno"));
                entry.setKeyword (rs.getString ("keyword"));
                entry.setShortCode (rs.getString ("short_code"));
                entry.setAccountId (rs.getString ("account_id"));
                entry.setViewed (rs.getInt ("is_read"));
                ie = entry;
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
        return ie;
    }

    public InboxEntry(String keyword, String msisdn, java.sql.Timestamp
                      dateReceived, String message, String messageId,
                      /*String questionId, */String shortCode, String provId) {
        this.keyword = keyword;
        this.msisdn = msisdn;
        this.dateReceived = dateReceived;
        this.message = message;
        this.messageId = messageId;
        //this.questionId = questionId;
        this.shortCode = shortCode;
        this.accountId = provId;
        this.viewed = 0;
    }

    public Timestamp getDateReceived() {
        return dateReceived;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    /*public String getQuestionId() {
        return questionId;
    }*/

    public String getKeyword() {
        return keyword;
    }

    public String getShortCode() {
        return shortCode;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setDateReceived(Timestamp dateReceived) {
        this.dateReceived = dateReceived;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    /*public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }*/

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public void setAccountId(String providerId) {
        this.accountId = providerId;
    }

    public int getViewed () {
        return viewed;
    }

    public void setViewed (int viewed) {
        this.viewed = viewed;
    }

}
