package com.rancard.common;

import com.rancard.common.db.DConnect;
import com.rancard.util.DateUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by Mustee on 4/5/2014.
 */
public class VMTransaction {
    private static Logger logger = Logger.getLogger(VMTransaction.class.getName());

    private String campaignID;
    private String recruiterMSISDN;
    private String recipientMSISDN;
    private Date transactionDate;
    private VMTransactionStatus status;
    private String itemID;
    private String category;
    private String shortURL;

    public VMTransaction(String campaignID, String recruiterMSISDN, String recipientMSISDN, Date transactionDate, VMTransactionStatus status, String itemID, String category, String shortURL) {
        this.campaignID = campaignID;
        this.recruiterMSISDN = recruiterMSISDN;
        this.recipientMSISDN = recipientMSISDN;
        this.transactionDate = transactionDate;
        this.status = status;
        this.itemID = itemID;
        this.category = category;
        this.shortURL = shortURL;
    }

    public String getCampaignID() {
        return campaignID;
    }

    public void setCampaignID(String campaignID) {
        this.campaignID = campaignID;
    }

    public String getRecruiterMSISDN() {
        return recruiterMSISDN;
    }

    public void setRecruiterMSISDN(String recruiterMSISDN) {
        this.recruiterMSISDN = recruiterMSISDN;
    }

    public String getRecipientMSISDN() {
        return recipientMSISDN;
    }

    public void setRecipientMSISDN(String recipientMSISDN) {
        this.recipientMSISDN = recipientMSISDN;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public VMTransactionStatus getStatus() {
        return status;
    }

    public void setStatus(VMTransactionStatus status) {
        this.status = status;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getShortURL() {
        return shortURL;
    }

    public void setShortURL(String shortURL) {
        this.shortURL = shortURL;
    }

    public static void createVMTransaction(VMTransaction transaction) throws Exception {
        final String query = "INSERT INTO vm_transaction VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DConnect.getConnection();
            stmt = conn.prepareStatement(query);
            stmt.setTimestamp(1, new Timestamp(transaction.getTransactionDate().getTime()));
            stmt.setString(2, transaction.getCampaignID());
            stmt.setString(3, transaction.getRecruiterMSISDN());
            stmt.setString(4, transaction.getRecipientMSISDN());
            stmt.setString(5, transaction.getStatus().toString());
            stmt.setString(6, transaction.getItemID());
            stmt.setString(7, transaction.getCategory());
            stmt.setString(8, transaction.getShortURL());
        } catch (Exception ex) {
            logger.severe("Problem creating transaction: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static boolean vmTransactionsExists(String campaignID, String recruiterMSISDN, Set<String> recipientMSISDNs) throws Exception {
        for (String recipient : recipientMSISDNs) {
            if (find(campaignID, recruiterMSISDN, recipient) == null) {
                return false;
            }
        }
        return true;
    }

    public static VMTransaction find(String campaignID, String recruiterMSISDN, String recipientMSISDN) throws Exception {
        final String query = "SELECT * FROM vm_transactions WHERE campaign_id = '" + campaignID + "' AND recipient_msisdn = '" + recipientMSISDN + "' AND recruiter_msisdn = '" + recruiterMSISDN + "'";
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = DConnect.getConnection();
            conn.createStatement().executeQuery(query);
            if (rs.next()) {
                return new VMTransaction(rs.getString("campaign_id"), rs.getString("recruiter_msisdn"), rs.getString("recipient_msisdn"),
                        DateUtil.convertFromTimeStampFormat(rs.getString("trans_date")), VMTransactionStatus.valueOf(rs.getString("status")),
                        rs.getString("item_id"), rs.getString("category"), rs.getString("short_url"));
            }

            return null;
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

    public static VMTransaction find(String campaignID, String recipientMSISDN) throws Exception {
        final String query = "SELECT * FROM vm_transactions WHERE campaign_id = '" + campaignID + "' AND recipient_msisdn = '" + recipientMSISDN + "'";
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = DConnect.getConnection();
            rs = conn.createStatement().executeQuery(query);
            if (rs.next()) {
                return new VMTransaction(rs.getString("campaign_id"), rs.getString("recruiter_msisdn"), rs.getString("recipient_msisdn"),
                        DateUtil.convertFromTimeStampFormat(rs.getString("trans_date")), VMTransactionStatus.valueOf(rs.getString("status")),
                        rs.getString("item_id"), rs.getString("category"), rs.getString("short_url"));
            }

            return null;
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

    public static void updateStatus(String campaignID, String recipientMSISDN, VMTransactionStatus status) throws Exception {
        final String query = "UPDATE vm_transactions SET status = '" + status.toString() + "' WHERE campaign_id = '" + campaignID + "' AND recipient_msisdn = '" + recipientMSISDN + "'";
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = DConnect.getConnection();
            conn.createStatement().executeUpdate(query);
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
