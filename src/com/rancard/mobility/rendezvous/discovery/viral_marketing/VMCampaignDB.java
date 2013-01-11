/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.rendezvous.discovery.viral_marketing;

import com.rancard.common.DConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author nii
 */
public class VMCampaignDB {

    public static void createCampaign (VMCampaign campaign) throws Exception {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection ();
            SQL = "insert into vm_campaigns (last_updated, campaign_id, account_id, keyword, message_sender, message, "
                    + "how_to_msg, follow_up_msg_success, follow_up_msg_error) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";

            prepstat = con.prepareStatement (SQL);

            prepstat.setTimestamp (1, new java.sql.Timestamp (new java.util.Date ().getTime ()));
            prepstat.setString (2, campaign.getCampaignId ());
            prepstat.setString (3, campaign.getAccountId ());
            prepstat.setString (4, campaign.getKeyword ());
            prepstat.setString (5, campaign.getMessageSender ());
            prepstat.setString (6, campaign.getMessage ());
            //prepstat.setString (7, campaign.getNetworkId ());
            prepstat.setString (7, campaign.getVmHowTo ());
            prepstat.setString (8, campaign.getFollowUpMsgSuccess ());
            prepstat.setString (9, campaign.getFollowUpMsgError ());

            prepstat.execute ();

        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close ();
                } catch (SQLException ex1) {
                    System.out.println (ex1.getMessage ());
                }
                con = null;
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close ();
                } catch (SQLException e) {
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close ();
                } catch (SQLException e) {
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close ();
                } catch (SQLException e) {
                }
                con = null;
            }
        }

    }

    public static java.util.ArrayList<VMCampaign> viewCampaigns (String campaignId) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        java.util.ArrayList<VMCampaign> campaigns = new java.util.ArrayList<VMCampaign> ();
        VMCampaign campaign = null;

        try {
            con = DConnect.getConnection ();

            SQL = "select * from vm_campaigns where campaign_id = ?";

            prepstat = con.prepareStatement (SQL);

            prepstat.setString (1, campaignId);

            rs = prepstat.executeQuery ();

            while (rs.next ()) {
                campaign = new VMCampaign ();

                campaign.setCampaignId (rs.getString ("campaign_id"));
                campaign.setAccountId (rs.getString ("account_id"));
                campaign.setKeyword (rs.getString ("keyword"));
                campaign.setMessageSender (rs.getString ("message_sender"));
                campaign.setMessage (rs.getString ("message"));
                campaign.setLastUpdated (rs.getTimestamp ("last_updated"));
                //campaign.setNetworkId (rs.getString ("network_id"));
                campaign.setVmHowTo (rs.getString ("how_to_msg"));
                campaign.setFollowUpMsgError (rs.getString ("follow_up_msg_error"));
                campaign.setFollowUpMsgSuccess (rs.getString ("follow_up_msg_success"));

                campaigns.add (campaign);
            }

        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close ();
                } catch (SQLException ex1) {
                    System.out.println (ex1.getMessage ());
                }
                con = null;
            }

            //error log
            System.out.println (new java.util.Date () + ": error viewing vm_campaign (" + campaignId + "): " + ex.getMessage ());

        } finally {
            if (rs != null) {
                try {
                    rs.close ();
                } catch (SQLException e) {
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close ();
                } catch (SQLException e) {
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close ();
                } catch (SQLException e) {
                }
                con = null;
            }
        }

        return campaigns;
    }

    public static VMCampaign viewCampaignByService (String accountId, String keyword) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        VMCampaign campaign = new VMCampaign ();

        try {
            con = DConnect.getConnection ();

            SQL = "select * from vm_campaigns where account_id = ? and keyword = ?";

            prepstat = con.prepareStatement (SQL);

            prepstat.setString (1, accountId);
            prepstat.setString (2, keyword);

            rs = prepstat.executeQuery ();

            while (rs.next ()) {
                campaign.setCampaignId (rs.getString ("campaign_id"));
                campaign.setAccountId (rs.getString ("account_id"));
                campaign.setKeyword (rs.getString ("keyword"));
                campaign.setMessageSender (rs.getString ("message_sender"));
                campaign.setMessage (rs.getString ("message"));
                campaign.setLastUpdated (rs.getTimestamp ("last_updated"));
                //campaign.setNetworkId (rs.getString ("network_id"));
                campaign.setVmHowTo (rs.getString ("how_to_msg"));
                campaign.setFollowUpMsgError (rs.getString ("follow_up_msg_error"));
                campaign.setFollowUpMsgSuccess (rs.getString ("follow_up_msg_success"));
            }

        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close ();
                } catch (SQLException ex1) {
                    System.out.println (ex1.getMessage ());
                }
                con = null;
            }

            //error log
            System.out.println (new java.util.Date () + ": error viewing vm_campaign (" + accountId + " - " + keyword + "): " + ex.getMessage ());

        } finally {
            if (rs != null) {
                try {
                    rs.close ();
                } catch (SQLException e) {
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close ();
                } catch (SQLException e) {
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close ();
                } catch (SQLException e) {
                }
                con = null;
            }
        }

        return campaign;
    }
    
    public static VMCampaign viewCampaignByServiceAndNetwork (String accountId, String keyword, String networkId) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        VMCampaign campaign = new VMCampaign ();

        try {
            con = DConnect.getConnection ();

            SQL = "select * from vm_campaigns where account_id = ? and keyword = ? and network_id=?";

            prepstat = con.prepareStatement (SQL);

            prepstat.setString (1, accountId);
            prepstat.setString (2, keyword);
            prepstat.setString (3, networkId);

            rs = prepstat.executeQuery ();

            while (rs.next ()) {
                campaign.setCampaignId (rs.getString ("campaign_id"));
                campaign.setAccountId (rs.getString ("account_id"));
                campaign.setKeyword (rs.getString ("keyword"));
                campaign.setMessageSender (rs.getString ("message_sender"));
                campaign.setMessage (rs.getString ("message"));
                campaign.setLastUpdated (rs.getTimestamp ("last_updated"));
                //campaign.setNetworkId (rs.getString ("network_id"));
                campaign.setVmHowTo (rs.getString ("how_to_msg"));
                campaign.setFollowUpMsgError (rs.getString ("follow_up_msg_error"));
                campaign.setFollowUpMsgSuccess (rs.getString ("follow_up_msg_success"));
            }

        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close ();
                } catch (SQLException ex1) {
                    System.out.println (ex1.getMessage ());
                }
                con = null;
            }

            //error log
            System.out.println (new java.util.Date () + ": error viewing vm_campaign (" + accountId + " - " + keyword + "): " + ex.getMessage ());

        } finally {
            if (rs != null) {
                try {
                    rs.close ();
                } catch (SQLException e) {
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close ();
                } catch (SQLException e) {
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close ();
                } catch (SQLException e) {
                }
                con = null;
            }
        }

        return campaign;
    }

    public static VMCampaign viewCampaignByAccount (String campaignId, String accountId) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        VMCampaign campaign = new VMCampaign ();

        try {
            con = DConnect.getConnection ();

            SQL = "select * from vm_campaigns where account_id = ? and campaign_id = ?";

            prepstat = con.prepareStatement (SQL);

            prepstat.setString (1, accountId);
            prepstat.setString (2, campaignId);

            rs = prepstat.executeQuery ();

            while (rs.next ()) {
                campaign.setCampaignId (rs.getString ("campaign_id"));
                campaign.setAccountId (rs.getString ("account_id"));
                campaign.setKeyword (rs.getString ("keyword"));
                campaign.setMessageSender (rs.getString ("message_sender"));
                campaign.setMessage (rs.getString ("message"));
                campaign.setLastUpdated (rs.getTimestamp ("last_updated"));
                //campaign.setNetworkId (rs.getString ("network_id"));
                campaign.setVmHowTo (rs.getString ("how_to_msg"));
                campaign.setFollowUpMsgError (rs.getString ("follow_up_msg_error"));
                campaign.setFollowUpMsgSuccess (rs.getString ("follow_up_msg_success"));
            }

        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close ();
                } catch (SQLException ex1) {
                    System.out.println (ex1.getMessage ());
                }
                con = null;
            }

            //error log
            System.out.println (new java.util.Date () + ": error viewing vm_campaign (" + accountId + " - " + campaignId + "): " + ex.getMessage ());

        } finally {
            if (rs != null) {
                try {
                    rs.close ();
                } catch (SQLException e) {
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close ();
                } catch (SQLException e) {
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close ();
                } catch (SQLException e) {
                }
                con = null;
            }
        }

        return campaign;
    }

    public static VMCampaign viewCampaignByNetwork (String campaignId, String networkId) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        VMCampaign campaign = new VMCampaign ();

        try {
            con = DConnect.getConnection ();

            SQL = "select * from vm_campaigns where campaign_id = ? and network_id = ?";

            prepstat = con.prepareStatement (SQL);

            prepstat.setString (1, campaignId);
            prepstat.setString (2, networkId);

            rs = prepstat.executeQuery ();

            while (rs.next ()) {
                campaign.setCampaignId (rs.getString ("campaign_id"));
                campaign.setAccountId (rs.getString ("account_id"));
                campaign.setKeyword (rs.getString ("keyword"));
                campaign.setMessageSender (rs.getString ("message_sender"));
                campaign.setMessage (rs.getString ("message"));
                campaign.setLastUpdated (rs.getTimestamp ("last_updated"));
                //campaign.setNetworkId (rs.getString ("network_id"));
                campaign.setVmHowTo (rs.getString ("how_to_msg"));
                campaign.setFollowUpMsgError (rs.getString ("follow_up_msg_error"));
                campaign.setFollowUpMsgSuccess (rs.getString ("follow_up_msg_success"));
            }

        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close ();
                } catch (SQLException ex1) {
                    System.out.println (ex1.getMessage ());
                }
                con = null;
            }

            //error log
            System.out.println (new java.util.Date () + ": error viewing vm_campaign (" + campaignId + " - " + networkId + "): " + ex.getMessage ());

        } finally {
            if (rs != null) {
                try {
                    rs.close ();
                } catch (SQLException e) {
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close ();
                } catch (SQLException e) {
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close ();
                } catch (SQLException e) {
                }
                con = null;
            }
        }

        return campaign;
    }

    public static void updateCampaignMessage (String campaignId, String message) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection ();
            SQL =
                    "UPDATE vm_campaigns SET message = ? "
                    + "WHERE campaign_id = ?";

            prepstat = con.prepareStatement (SQL);

            prepstat.setString (1, message);
            prepstat.setString (2, campaignId);

            prepstat.execute ();

        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close ();
                } catch (SQLException ex1) {
                    System.out.println (ex1.getMessage ());
                }
                con = null;
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close ();
                } catch (SQLException e) {
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close ();
                } catch (SQLException e) {
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close ();
                } catch (SQLException e) {
                }
                con = null;
            }
        }

    }
}
