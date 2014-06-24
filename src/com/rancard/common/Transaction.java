package com.rancard.common;

import com.rancard.common.db.DConnect;
import com.rancard.util.DateUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by Mustee on 4/11/2014.
 */
public class Transaction {
    private static Logger logger = Logger.getLogger(Transaction.class.getName());

    private String transactionID;
    private String accountID;
    private String keyword;
    private String msisdn;
    private Date date;
    private String message;
    private String callbackURL;
    private boolean isBilled;
    private boolean isCompleted;
    private String pricePointID;


    public Transaction(String transactionID, String accountID, String keyword, String msisdn, Date date, String message, String callbackURL, Boolean isBilled, Boolean isCompleted, String pricePointID) {
        this.transactionID = transactionID;
        this.keyword = keyword;
        this.accountID = accountID;
        this.msisdn = msisdn;
        this.date = date;
        this.message = message;
        this.callbackURL = callbackURL;
        this.isBilled = isBilled;
        this.isCompleted = isCompleted;
        this.pricePointID = pricePointID;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public boolean isBilled() {
        return isBilled;
    }

    public void setBilled(boolean isBilled) {
        this.isBilled = isBilled;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public String getPricePointID() {
        return pricePointID;
    }

    public void setPricePointID(String pricePointID) {
        this.pricePointID = pricePointID;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCallbackURL() {
        return callbackURL;
    }

    public void setCallbackURL(String callbackURL) {
        this.callbackURL = callbackURL;
    }

    public Boolean getIsBilled() {
        return isBilled;
    }

    public void setIsBilled(Boolean isBilled) {
        this.isBilled = isBilled;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public static void createTransaction(Transaction trans) throws Exception {
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DConnect.getConnection();
            String query = "INSERT into transactions (trans_id,keyword,account_id,msisdn,callback_url,date,msg,is_billed,is_completed,price_point_id) "
                    + "values('" + trans.getTransactionID() + "', '" + trans.getKeyword() + "', '" + trans.getAccountID() + "', '" + trans.getMsisdn() + "', '"
                    + trans.getCallbackURL() + "', '" + DateUtil.formatToTimeStamp(trans.getDate()) + "', '" + trans.getMessage() + "', "
                    + (trans.getIsBilled() ? 1 : 0) + ", " + (trans.getIsCompleted() ? 1 : 0) + ", '" + trans.getPricePointID() + "'";
            logger.info("Creating transaction: " + query);

            conn.createStatement().execute(query);

        } catch (Exception ex) {
            logger.severe("Problem creating transaction: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (conn != null) {
                conn.close();
            }

        }
    }
}
