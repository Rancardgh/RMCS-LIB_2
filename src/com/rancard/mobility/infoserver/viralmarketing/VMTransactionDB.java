/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.infoserver.viralmarketing;

import com.rancard.common.DConnect;
import com.rancard.util.DateUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;

/**
 *
 * @author nii Updated by Mustee
 */
public final class VMTransactionDB {

    public static void createTransaction(VMTransaction transaction) throws Exception {
        Connection conn = null;

        try {
            conn = DConnect.getConnection();
            String sql = "insert into vm_transactions(trans_date, campaign_id, recruiter_msisdn, recipient_msisdn, status) "
                    + "values('" + DateUtil.convertToMySQLTimeStamp(transaction.getTransactionDate()) + "', '" + transaction.getCampaignId() + "', "
                    + "'" + transaction.getRecruiterMsisdn() + "', '" + transaction.getRecipientMsisdn() + "', "
                    + "'" + transaction.getStatus() + "')";
            System.out.println(new Date() + ": " + VMTransactionDB.class + ":DEBUG Insert Transaction: " + sql);

            conn.createStatement().execute(sql);


        } catch (Exception ex) {
            System.out.println(new Date() + ": " + VMTransactionDB.class + "ERROR: Creating transaction: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

    }

    public static VMTransaction viewTransaction(String campaignId, String recipientMsisdn) throws Exception {
        ResultSet rs = null;
        Connection conn = null;
        VMTransaction trans = null;

        try {
            conn = DConnect.getConnection();

            String sql = "select * from vm_transactions where campaign_id = '" + campaignId + "' and recipient_msisdn = '" + recipientMsisdn + "'";
            System.out.println(new Date() + ": " + VMTransactionDB.class + ":DEBUG Getting Transaction: " + sql);

            rs = conn.createStatement().executeQuery(sql);

            while (rs.next()) {
                trans = new VMTransaction(rs.getString("campaign_id"), rs.getString("recruiter_msisdn"), rs.getString("recipient_msisdn"),
                        rs.getString("status"), new Date(rs.getTimestamp("trans_date").getTime()));
                break;
            }

            return trans;

        } catch (Exception ex) {
            System.out.println(new Date() + ": " + VMTransactionDB.class + "ERROR: Getting transaction: " + ex.getMessage());
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

    public static VMTransaction viewTransaction(String accountId, String keyword, String recipientMsisdn) throws Exception {
        ResultSet rs = null;
        Connection conn = null;
        VMTransaction trans = null;

        try {
            conn = DConnect.getConnection();

            String sql = "select * from vm_campaigns INNER JOIN vm_transactions ON vm_campaigns.campaign_id = vm_transactions.campaign_id  "
                    + "where account_id = '" + accountId + "' and keyword = '" + keyword + "' and recipient_msisdn = '" + recipientMsisdn + "'";

            System.out.println(new Date() + ": " + VMTransactionDB.class + ":DEBUG Getting Transaction: " + sql);

            rs = conn.createStatement().executeQuery(sql);

            while (rs.next()) {
                trans = new VMTransaction(rs.getString("campaign_id"), rs.getString("recruiter_msisdn"), rs.getString("recipient_msisdn"),
                        rs.getString("status"), new Date(rs.getTimestamp("trans_date").getTime()));
                break;
            }

            return trans;
        } catch (Exception ex) {
            System.out.println(new Date() + ": " + VMTransactionDB.class + "ERROR: Getting transaction: " + ex.getMessage());
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

    public static void updateTransactionStatus(String campaignId, String recipientId, String status) throws Exception {
        Connection conn = null;

        try {
            conn = DConnect.getConnection();
            String sql = "UPDATE vm_transactions SET status = '" + status + "' WHERE campaign_id = '" + campaignId + "'  and recipient_msisdn = '" + recipientId + "'";
            System.out.println(new Date() + ": " + VMTransactionDB.class + ":DEBUG:  Updating Transaction: " + sql);

            conn.createStatement().executeUpdate(sql);

        } catch (Exception ex) {
            System.out.println(new Date() + ": " + VMTransactionDB.class + "ERROR:  Updating transaction: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

    }

    public static void updateTransactionStatus(String campaignId, String recipientId, String status, int points) throws Exception {
        Connection conn = null;


        try {
            conn = DConnect.getConnection();
            String sql = "UPDATE vm_transactions SET status = '" + status + "' WHERE campaign_id = '" + campaignId + "'  and recipient_msisdn = '" + recipientId + "'";
            System.out.println(new Date() + ": " + VMTransactionDB.class + ":DEBUG:  Updating Transaction: " + sql);

            conn.createStatement().executeUpdate(sql);


            String sqlPoints = "UPDATE vm_users vmu INNER JOIN vm_campaigns vmc ON vmu.account_id = vmc.account_id AND vmu.keyword = vmc.keyword "
                    + "INNER JOIN vm_transactions vmt ON vmt.campaign_id = vmc.campaign_id SET vmu.points = vmu.points + " + points + " "
                    + "WHERE vmt.recipient_msisdn = '" + recipientId + "' AND vmt.campaign_id = '" + campaignId + "' AND vmt.status = '" + status + "' AND vmu.msisdn = vmt.recruiter_msisdn";
            System.out.println(new Date() + ": " + VMTransactionDB.class + ":DEBUG:  Updating Points: " + sqlPoints);

            conn.createStatement().executeUpdate(sqlPoints);

        } catch (Exception ex) {
            System.out.println(new Date() + ": " + VMTransactionDB.class + "ERROR:  Updating transaction: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

    }

    public static VMTransaction viewTransaction(String campaignId, String recipientMsisdn, String recruiterMsisdn, boolean sendReminderIfExists) throws Exception {
        ResultSet rs = null;
        Connection conn = null;       
        VMTransaction trans = null;

        try {
            conn = DConnect.getConnection();

            String sql = "select * from vm_transactions where campaign_id = '" + campaignId + "' and recipient_msisdn = '" + recipientMsisdn + "' and recruiter_msisdn = '" + recruiterMsisdn + "'";
            System.out.println(new Date() + ": " + VMTransactionDB.class + ":DEBUG:  Getting Transaction: " + sql);


            rs = conn.createStatement().executeQuery(sql);

            while (rs.next()) {
                trans = new VMTransaction(rs.getString("campaign_id"), rs.getString("recruiter_msisdn"), rs.getString("recipient_msisdn"),
                        rs.getString("status"), new Date(rs.getTimestamp("trans_date").getTime()));
                break;
            }
            return trans;
        } catch (Exception ex) {
            System.out.println(new Date() + ": " + VMTransactionDB.class + "ERROR:  Selecting transaction: " + ex.getMessage());
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
