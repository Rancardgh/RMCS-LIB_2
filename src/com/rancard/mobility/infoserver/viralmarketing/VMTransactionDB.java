/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rancard.mobility.infoserver.viralmarketing;

import com.rancard.common.DConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author nii
 */
public class VMTransactionDB {

    public static void createTransaction(VMTransaction transaction) throws Exception {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            SQL = "insert into vm_transactions(trans_date, campaign_id, recruiter_msisdn, recipient_msisdn, status) " +
                    "values(?, ?, ?, ?, ?)";

            prepstat = con.prepareStatement(SQL);

            prepstat.setTimestamp(1, new java.sql.Timestamp(new java.util.Date().getTime()));
            prepstat.setString(2, transaction.getCampaignId());
            prepstat.setString(3, transaction.getRecruiterMsisdn());
            prepstat.setString(4, transaction.getRecipientMsisdn());
            prepstat.setString(5, transaction.getStatus());

            prepstat.execute();

        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex1) {
                    System.out.println(ex1.getMessage());
                }
                con = null;
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    ;
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                    ;
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    ;
                }
                con = null;
            }
        }

    }

    public static VMTransaction viewTransaction(String campaignId, String recipientMsisdn) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        VMTransaction transaction = new VMTransaction();

        try {
            con = DConnect.getConnection();

            SQL = "select * from vm_transactions where campaign_id = ? and recipient_msisdn = ?";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, campaignId);
            prepstat.setString(2, recipientMsisdn);

            rs = prepstat.executeQuery();

            while (rs.next()) {
                transaction.setCampaignId(rs.getString("campaign_id"));
                transaction.setRecruiterMsisdn(rs.getString("recruiter_msisdn"));
                transaction.setRecipientMsisdn(rs.getString("recipient_msisdn"));
                transaction.setStatus(rs.getString("status"));
                java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String transactionDate = df.format(new java.util.Date(rs.getTimestamp("trans_date").getTime()));
                transaction.setTransactionDate(transactionDate);
            }

        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex1) {
                    System.out.println(ex1.getMessage());
                }
                con = null;
            }

            //error log
            System.out.println(new java.util.Date()+ ": error viewing vm_transaction ("+ campaignId +", "+recipientMsisdn + "): " + ex.getMessage() );

        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    ;
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                    ;
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    ;
                }
                con = null;
            }
        }

        return transaction;
    }
    
    public static VMTransaction viewTransaction(String campaignId, String recipientMsisdn, String recruiterMsisdn, boolean sendReminderIfExists) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        VMTransaction transaction = new VMTransaction();

        try {
            con = DConnect.getConnection();

            SQL = "select * from vm_transactions where campaign_id = ? and recipient_msisdn = ? and recruiter_msisdn = ?";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, campaignId);
            prepstat.setString(2, recipientMsisdn);
            prepstat.setString(3, recruiterMsisdn);

            rs = prepstat.executeQuery();

            while (rs.next()) {
                transaction.setCampaignId(rs.getString("campaign_id"));
                transaction.setRecruiterMsisdn(rs.getString("recruiter_msisdn"));
                transaction.setRecipientMsisdn(rs.getString("recipient_msisdn"));
                transaction.setStatus(rs.getString("status"));
                java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String transactionDate = df.format(new java.util.Date(rs.getTimestamp("trans_date").getTime()));
                transaction.setTransactionDate(transactionDate);
            }

        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex1) {
                    System.out.println(ex1.getMessage());
                }
                con = null;
            }

            //error log
            System.out.println(new java.util.Date()+ ": error viewing vm_transaction ("+ campaignId +", "+recipientMsisdn + "): " + ex.getMessage() );

        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    ;
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                    ;
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    ;
                }
                con = null;
            }
        }

        return transaction;
    }

    public static VMTransaction viewTransaction(String accountId, String keyword, String recipientMsisdn) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        VMTransaction transaction = new VMTransaction();

        try {
            con = DConnect.getConnection();

            SQL = "select * from vm_campaigns INNER JOIN vm_transactions ON vm_campaigns.campaign_id = vm_transactions.campaign_id  " +
                    "where account_id = ? and keyword = ? and recipient_msisdn = ?";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, accountId);
            prepstat.setString(2, keyword);
            prepstat.setString(3, recipientMsisdn);

            rs = prepstat.executeQuery();

            while (rs.next()) {
                transaction.setCampaignId(rs.getString("campaign_id"));
                transaction.setRecruiterMsisdn(rs.getString("recruiter_msisdn"));
                transaction.setRecipientMsisdn(rs.getString("recipient_msisdn"));
                transaction.setStatus(rs.getString("status"));
                java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String transactionDate = df.format(new java.util.Date(rs.getTimestamp("trans_date").getTime()));
                transaction.setTransactionDate(transactionDate);
            }

        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex1) {
                    System.out.println(ex1.getMessage());
                }
                con = null;
            }

            //error log
            System.out.println(new java.util.Date()+ ": error viewing vm_transaction ("+ accountId +","+keyword+","+recipientMsisdn + "): " + ex.getMessage() );

        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    ;
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                    ;
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    ;
                }
                con = null;
            }
        }

        return transaction;
    } 
    
    public static void updateTransactionStatus(String campaignId, String recipientId, String status) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            SQL =
                    "UPDATE vm_transactions SET status = ? " +
                    "WHERE campaign_id = ?  and recipient_msisdn = ?";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, status);
            prepstat.setString(2, campaignId);
            prepstat.setString(3, recipientId);
            
            prepstat.execute();

        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex1) {
                    System.out.println(ex1.getMessage());
                }
                con = null;
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    ;
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                    ;
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    ;
                }
                con = null;
            }
        }

    }

    public static void updateTransactionStatus(String campaignId, String recipientId, String status, int points) throws Exception {

        String SQL;
        String SQL_points;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        PreparedStatement prepstat_points = null;

        try {
            con = DConnect.getConnection();
            SQL =
                    "UPDATE vm_transactions SET status = ? " +
                    "WHERE campaign_id = ?  and recipient_msisdn = ?";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, status);
            prepstat.setString(2, campaignId);
            prepstat.setString(3, recipientId);

            prepstat.execute();

            /*SQL_points =
                    "UPDATE vm_users vmu " +
                    "INNER JOIN vm_transactions vmt " +
                    "ON vmu.msisdn = vmt.recruiter_msisdn " +
                    "INNER join vm_campaigns vmc " +
                    "ON vmu.keyword = vmc.keyword AND vmu.account_id = vmc.account_id " +
                    "SET vmu.points = vmu.points + ? " +
                    "WHERE vmt.recipient_msisdn = ? AND vmt.campaign_id = ? AND vmt.status = ?";*/
            SQL_points =
                    "UPDATE vm_users vmu " +
                    "INNER JOIN vm_campaigns vmc ON vmu.account_id = vmc.account_id AND vmu.keyword = vmc.keyword " +
                    "INNER JOIN vm_transactions vmt ON vmt.campaign_id = vmc.campaign_id " +
                    "SET vmu.points = vmu.points + ? " +
                    "WHERE vmt.recipient_msisdn = ? AND vmt.campaign_id = ? " +
                    "AND vmt.status = ? AND vmu.msisdn = vmt.recruiter_msisdn";

            prepstat_points = con.prepareStatement(SQL_points);

            prepstat_points.setInt(1, points);
            prepstat_points.setString(2, recipientId);
            prepstat_points.setString(3, campaignId);
            prepstat_points.setString(4, status);

            prepstat_points.execute();

        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex1) {
                    System.out.println(ex1.getMessage());
                }
                con = null;
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    ;
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                    prepstat_points.close();
                } catch (SQLException e) {
                    ;
                }
                prepstat = null;
                prepstat_points = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    ;
                }
                con = null;
            }
        }

    }

}