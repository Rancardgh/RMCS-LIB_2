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
 * @author nii
 */
public class VMTransactionDB {

    public static void createTransaction(VMTransaction transaction) throws Exception {
        Connection conn = null;

        try {
            conn = DConnect.getConnection();
            String sql = "insert into vm_transactions(trans_date, campaign_id, recruiter_msisdn, recipient_msisdn, status, item_id, "
                    + "category, short_url) values('" + DateUtil.convertToMySQLTimeStamp(transaction.getTransactionDate()) + "', '"
                    + transaction.getCampaignID() + "', '" + transaction.getRecruiterMsisdn() + "', '" + transaction.getRecipientMsisdn() + "', '"
                    + transaction.getStatus().getValue() + "', '" + transaction.getItemID()+ "', "
                    + (transaction.getCategory() == null ? "NULL " : "'" + transaction.getCategory() + "'")+ ", "
                    + (transaction.getShortURL() == null ? "NULL" : "'" + transaction.getShortURL() + "'") + ")";
            System.out.println(new Date() + "\t[" + VMTransactionDB.class + "]\tDEBUG\tCreate VMTransaction: " + sql);

            conn.createStatement().execute(sql);

        } catch (Exception ex) {
            System.out.println(new Date() + "\t[" + VMTransactionDB.class + "]\tERROR\tCreating VMTransaction: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

    }

    public static VMTransaction viewTransaction(String campaignID, String recipientMsisdn) throws Exception {
        ResultSet rs = null;
        Connection conn = null;

        VMTransaction transaction = null;

        try {
            conn = DConnect.getConnection();
            String sql = "select * from vm_transactions where campaign_id = '" + campaignID + "' and recipient_msisdn = '" + recipientMsisdn + "'";
            System.out.println(new Date() + "\t[" + VMTransactionDB.class + "]\tDEBUG\tViewing VMTransaction: " + sql);

            rs = conn.createStatement().executeQuery(sql);

            if (rs.next()) {
                transaction = new VMTransaction(DateUtil.convertFromMySQLTimeStamp(rs.getString("trans_date")), rs.getString("campaign_id"),
                        rs.getString("recruiter_msisdn"), rs.getString("recipient_msisdn"), VMTransactionStatus.valueOf(rs.getString("status").toUpperCase()),
                        rs.getString("item_id"), rs.getString("category"), rs.getString("short_url"));
            }
            return transaction;
        } catch (Exception ex) {
            System.out.println(new Date() + "\t[" + VMTransactionDB.class + "]\tERROR\tViewing VMTransaction: " + ex.getMessage());
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

    public static VMTransaction viewTransaction(String campaignID, String recipientMsisdn, String recruiterMsisdn) throws Exception {
        ResultSet rs = null;
        Connection conn = null;

        VMTransaction transaction = null;
        try {
            conn = DConnect.getConnection();

            String sql = "select * from vm_transactions where campaign_id = '" + campaignID + "' and recipient_msisdn = '" + recipientMsisdn + "' and recruiter_msisdn = '";
            System.out.println(new Date() + "\t[" + VMTransactionDB.class + "]\tDEBUG\tViewing VMTransaction: " + sql);

            rs = conn.createStatement().executeQuery(sql);

            if (rs.next()) {
                transaction = new VMTransaction(DateUtil.convertFromMySQLTimeStamp(rs.getString("trans_date")), rs.getString("campaign_id"),
                        rs.getString("recruiter_msisdn"), rs.getString("recipient_msisdn"), VMTransactionStatus.valueOf(rs.getString("status")),
                        rs.getString("item_id"), rs.getString("category"), rs.getString("short_url"));
            }
            return transaction;
        } catch (Exception ex) {
            System.out.println(new Date() + "\t[" + VMTransactionDB.class + "]\tERROR\tViewing VMTransaction: " + ex.getMessage());
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

    public static void updateTransactionStatus(String campaignId, String recipientID, VMTransactionStatus status) throws Exception {
        Connection conn = null;       

        try {
            conn = DConnect.getConnection();
            String sql = "UPDATE vm_transactions SET status = '"+status.getValue()+"' "
                    + "WHERE campaign_id = '"+campaignId+"'  and recipient_msisdn = '"+recipientID+"'";
            System.out.println(new Date() + "\t[" + VMTransactionDB.class + "]\tDEBUG\tUpdating VMTransaction: " + sql);

            conn.createStatement().executeUpdate(sql);

        } catch (Exception ex) {
            System.out.println(new Date() + "\t[" + VMTransactionDB.class + "]\tERROR\tUpdating VMTransaction: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

    }

    public static void updateTransactionStatus(String campaignID, String recipientMSISDN, VMTransactionStatus status, 
            int points) throws Exception {       
        Connection conn = null;       

        try {
            updateTransactionStatus(campaignID, recipientMSISDN, status);

            conn = DConnect.getConnection();
            String sql = "UPDATE vm_users vmu INNER JOIN vm_campaigns_temp vmc ON vmu.account_id = vmc.account_id AND "
                    + "vmu.keyword = vmc.keyword INNER JOIN vm_transactions vmt ON vmt.campaign_id = vmc.campaign_id "
                    + "SET vmu.points = vmu.points + "+points+" WHERE vmt.recipient_msisdn = '"+recipientMSISDN+"' "
                    + "AND vmt.campaign_id = '"+campaignID+"' AND vmt.status = ? AND vmu.msisdn = vmt.recruiter_msisdn";

            System.out.println(new Date() + "\t[" + VMTransactionDB.class + "]\tDEBUG\tUpdating VMUser points: " + sql);
            
            conn.createStatement().executeQuery(sql);

        } catch (Exception ex) {
            System.out.println(new Date() + "\t[" + VMTransactionDB.class + "]\tERROR\tUpdating VMUser points: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

    }
}