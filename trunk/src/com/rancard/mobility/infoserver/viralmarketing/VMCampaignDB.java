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
import java.util.ArrayList;

/**
 *
 * @author nii
 */
public class VMCampaignDB {

    public static void createCampaign(VMCampaign campaign) throws Exception {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            SQL = "insert into vm_campaigns(last_updated, campaign_id, account_id, keyword, message_sender, message) "
                    + "values(?, ?, ?, ?, ?, ?)";

            prepstat = con.prepareStatement(SQL);

            prepstat.setTimestamp(1, new java.sql.Timestamp(new java.util.Date().getTime()));
            prepstat.setString(2, campaign.getCampaignId());
            prepstat.setString(3, campaign.getAccountId());
            prepstat.setString(4, campaign.getKeyword());
            prepstat.setString(5, campaign.getMessageSender());
            prepstat.setString(6, campaign.getMessage());

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

    public static VMCampaign viewCampaign(String campaignId) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        VMCampaign campaign = new VMCampaign();

        try {
            con = DConnect.getConnection();

            SQL = "select * from vm_campaigns where campaign_id = ?";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, campaignId);

            rs = prepstat.executeQuery();

            while (rs.next()) {
                campaign.setCampaignId(rs.getString("campaign_id"));
                campaign.setAccountId(rs.getString("account_id"));
                campaign.setKeyword(rs.getString("keyword"));
                campaign.setMessageSender(rs.getString("message_sender"));
                campaign.setMessage(rs.getString("message"));
                java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String lastUpdated = df.format(new java.util.Date(rs.getTimestamp("last_updated").getTime()));
                campaign.setLastUpdated(lastUpdated);
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
            System.out.println(new java.util.Date() + ": error viewing vm_campaign (" + campaignId + "): " + ex.getMessage());

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

        return campaign;
    }

    public static VMCampaign viewCampaign(String accountId, String keyword) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        VMCampaign campaign = new VMCampaign();

        try {
            con = DConnect.getConnection();

            SQL = "select * from vm_campaigns where account_id = ? and keyword = ?";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, accountId);
            prepstat.setString(2, keyword);

            rs = prepstat.executeQuery();

            while (rs.next()) {
                campaign.setCampaignId(rs.getString("campaign_id"));
                campaign.setAccountId(rs.getString("account_id"));
                campaign.setKeyword(rs.getString("keyword"));
                campaign.setMessageSender(rs.getString("message_sender"));
                campaign.setMessage(rs.getString("message"));
                java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String lastUpdated = df.format(new java.util.Date(rs.getTimestamp("last_updated").getTime()));
                campaign.setLastUpdated(lastUpdated);
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
            System.out.println(new java.util.Date() + ": error viewing vm_campaign (" + accountId + " - " + keyword + "): " + ex.getMessage());

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

        return campaign;
    }

    public static void updateCampaignMessage(String campaignId, String message) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            SQL =
                    "UPDATE vm_campaigns SET message = ? "
                    + "WHERE campaign_id = ?";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, message);
            prepstat.setString(2, campaignId);

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

    public static void updateCampaign(VMCampaign campaign, String update_account_id, String update_keyword) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        //Check and set update parameters
        String account_id = (campaign.getAccountId()!= null && !campaign.getAccountId().equals("")) ? "'" + campaign.getAccountId() + "'" : "account_id";
        String campaign_id = (campaign.getCampaignId() != null && !campaign.getCampaignId().equals("")) ? "'" + campaign.getCampaignId() + "'" : "campaign_id";
        String keyword = (campaign.getKeyword() != null && !campaign.getKeyword().equals("")) ? "'" + campaign.getKeyword() + "'" : "keyword";
        String message_sender = (campaign.getMessageSender()!= null && !campaign.getMessageSender().equals("")) ? "'" + campaign.getMessageSender() + "'" : "message_sender";
        String message = (campaign.getMessage() != null && !campaign.getMessage().equals("")) ? "'" + campaign.getMessage() + "'" : "message";
        String last_updated = (campaign.getLastUpdated() != null && !campaign.getLastUpdated().equals("")) ? "'" + campaign.getLastUpdated() + "'" : "last_updated";

        try {
            con = DConnect.getConnection();

            SQL = "UPDATE vm_campaigns SET campaign_id = "+campaign_id+", account_id = "+account_id+", keyword = "+keyword+", message_sender = "+message_sender+", "
                    + "message = "+message+", last_updated = "+last_updated+" WHERE acount_id = ? and keyword = ? ";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, update_account_id);
            prepstat.setString(2, update_keyword);

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

    public static void deleteCampaign(ArrayList keywords, String accountId) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        String keywordStr = "";
        for (int i = 0; i < keywords.size(); i++) {
            keywordStr = keywordStr + "'" + keywords.get(i).toString() + "',";
        }
        keywordStr = keywordStr.substring(0, keywordStr.lastIndexOf(","));

        try {
            con = DConnect.getConnection();
            SQL = "delete from vm_campaigns where keyword in (" + keywordStr + ") and account_id='" + accountId + "'";
            prepstat = con.prepareStatement(SQL);
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

     public static void deleteCampaign(String keyword, String accountId) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            SQL =
                    "delete from vm_campaigns where keyword = ? and account_id=? ";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, keyword);
            prepstat.setString(2, accountId);

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
}
