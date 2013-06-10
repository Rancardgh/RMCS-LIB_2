/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.infoserver.viralmarketing;

import com.rancard.common.DConnect;
import com.rancard.util.DateUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author nii
 * Updated Mustee
 */
public class VMCampaignDB {

    public static void createCampaign(VMCampaign campaign) throws Exception {
        Connection conn = null;

        try {
            conn = DConnect.getConnection();
            String sql = "insert into vm_campaigns(last_updated, campaign_id, account_id, keyword, message_sender, message) "
                    + "values('" + DateUtil.convertToMySQLTimeStamp(campaign.getLastUpdated()) + "' , '" + campaign.getCampaignId() + "', "
                    + "'" + campaign.getAccountId() + "', '" + campaign.getKeyword() + "', '" + campaign.getMessageSender() + "', "
                    + "'" + campaign.getMessage() + "')";
            System.out.println(new Date() + ": " + VMCampaignDB.class + ":DEBUG   Create campaign: " + sql);

            conn.createStatement().execute(sql);

        } catch (Exception ex) {
            System.out.println(new Date() + ": " + VMCampaignDB.class + "ERROR: Creating campaign: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

    }

    public static VMCampaign viewCampaign(String campaignId) throws Exception {
        ResultSet rs = null;
        Connection conn = null;
        VMCampaign campaign = null;

        try {
            conn = DConnect.getConnection();

            String sql = "select * from vm_campaigns where campaign_id = '" + campaignId + "'";
            System.out.println(new Date() + ": " + VMCampaignDB.class + ":DEBUG   View campaign: " + sql);

            rs = conn.createStatement().executeQuery(sql);

            while (rs.next()) {
                campaign = new VMCampaign(rs.getString("campaign_id"), rs.getString("account_id"), rs.getString("keyword"),
                        rs.getString("message_sender"), rs.getString("message"), DateUtil.convertFromMySQLTimeStamp(rs.getString("last_updated")));
                break;
            }
            return campaign;
        } catch (Exception ex) {
            System.out.println(new Date() + ": " + VMCampaignDB.class + "ERROR: Viewing campaign: " + ex.getMessage());
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

    public static VMCampaign viewCampaign(String accountId, String keyword) throws Exception {
        ResultSet rs = null;
        Connection conn = null;
        VMCampaign campaign = null;

        try {
            conn = DConnect.getConnection();
            String sql = "select * from vm_campaigns where account_id = '" + accountId + "' and keyword = '" + keyword + "'";
            System.out.println(new Date() + ": " + VMCampaignDB.class + ":DEBUG   View campaign: " + sql);

            rs = conn.createStatement().executeQuery(sql);

            while (rs.next()) {
                campaign = new VMCampaign(rs.getString("campaign_id"), rs.getString("account_id"), rs.getString("keyword"),
                        rs.getString("message_sender"), rs.getString("message"),
                        DateUtil.convertFromMySQLTimeStamp(rs.getString("last_updated")));
                break;
            }

            return campaign;
        } catch (Exception ex) {
            System.out.println(new Date() + ": " + VMCampaignDB.class + ":ERROR Viewing campaign: " + ex.getMessage());
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

    public static void updateCampaignMessage(String campaignId, String message) throws Exception {
        Connection conn = null;

        try {
            conn = DConnect.getConnection();
            String sql = "UPDATE vm_campaigns SET message = '" + message + "' WHERE campaign_id = '" + campaignId + "'";
            System.out.println(new Date() + ": " + VMCampaignDB.class + ":DEBUG   Updating campaign: " + sql);

            conn.createStatement().executeUpdate(sql);

        } catch (Exception ex) {
            System.out.println(new Date() + ": " + VMCampaignDB.class + ":ERROR   Updating campaign: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static void updateCampaign(VMCampaign campaign, String update_account_id, String update_keyword) throws Exception {
        Connection conn = null;

        try {
            conn = DConnect.getConnection();

            String sql = "UPDATE vm_campaigns SET campaign_id = '" + campaign.getCampaignId() + "', account_id = '" + campaign.getAccountId() + "', "
                    + "keyword = '" + campaign.getKeyword() + "', message_sender = '" + campaign.getMessageSender() + "', "
                    + "message = '" + campaign.getMessage() + "', last_updated = '" + DateUtil.convertToMySQLTimeStamp(campaign.getLastUpdated()) + "' "
                    + "WHERE acount_id = '" + update_account_id + "' and keyword = '" + update_keyword + "'";
            System.out.println(new Date() + ": " + VMCampaignDB.class + ":DEBUG   Updating campaign: " + sql);

            conn.createStatement().executeUpdate(sql);

        } catch (Exception ex) {
            System.out.println(new Date() + ": " + VMCampaignDB.class + ":ERROR   Updating campaign: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

    }

    public static void deleteCampaign(ArrayList keywords, String accountId) throws Exception {
        Connection conn = null;


        String keywordStr = "";
        for (int i = 0; i < keywords.size(); i++) {
            keywordStr = keywordStr + "'" + keywords.get(i).toString() + "',";
        }
        keywordStr = keywordStr.substring(0, keywordStr.lastIndexOf(","));

        try {
            conn = DConnect.getConnection();
            String sql = "delete from vm_campaigns where keyword in (" + keywordStr + ") and account_id='" + accountId + "'";
            System.out.println(new Date() + ": " + VMCampaignDB.class + ":DEBUG   Delete campaigns: " + sql);

            conn.createStatement().execute(sql);
        } catch (Exception ex) {
            System.out.println(new Date() + ": " + VMCampaignDB.class + ":ERROR   Delete campaigns: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static void deleteCampaign(String keyword, String accountId) throws Exception {
        Connection conn = null;

        try {
            conn = DConnect.getConnection();
            String sql = "delete from vm_campaigns where keyword = '" + keyword + "' and account_id= '" + accountId + "'";
            System.out.println(new Date() + ": " + VMCampaignDB.class + ":DEBUG   Delete campaign: " + sql);

            conn.createStatement().execute(sql);

        } catch (Exception ex) {
            System.out.println(new Date() + ": " + VMCampaignDB.class + ":ERROR   Delete campaign: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
           if (conn != null) {
                conn.close();
            }
        }
    }
}
