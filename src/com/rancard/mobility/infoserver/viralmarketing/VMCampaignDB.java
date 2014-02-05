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
 * @author nii Updated Mustee
 */
public class VMCampaignDB {

    public static void createCampaign(VMCampaign campaign) throws Exception {
        Connection conn = null;

        try {
            conn = DConnect.getConnection();
            int isActive = campaign.getIsActive() ? 1 : 0;

            String sql = "insert into vm_campaigns_temp(campaign_id, account_id, keyword, message_sender, message, "
                    + "how_to_msg, follow_up_msg_success, follow_up_msg_error, invite_accepted_msg, already_inv_msg, "
                    + "last_updated, is_active, push_wait_time) values('" + campaign.getCampaignID() + "', '"
                    + campaign.getAccountID() + "', '" + campaign.getKeyword() + "', '" + campaign.getMessageSender() + "', '"
                    + campaign.getMessage() + "', '" + campaign.getHowToMessage() + "', '" + campaign.getFollowUpMessageSuccess() + "', '"
                    + campaign.getFollowUpMessageError() + "', '" + campaign.getInviteAcceptedMessage() + "', '" + campaign.getAlreadyInvitedMessage() + "', '"
                    + DateUtil.convertToMySQLTimeStamp(campaign.getLastUpdated()) + "', " + isActive + ", " + campaign.getPushWaitTime() + ")";

            System.out.println(new Date() + "\t[" + VMCampaignDB.class + "]\tDEBUG\tCreate campaign: " + sql);

            conn.createStatement().execute(sql);

        } catch (Exception ex) {
            System.out.println(new Date() + "\t[" + VMCampaignDB.class + "]\tERROR\tCreating campaign: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

    }

    public static VMCampaign viewCampaign(String campaignID) throws Exception {
        ResultSet rs = null;
        Connection conn = null;
        VMCampaign campaign = null;

        try {
            conn = DConnect.getConnection();

            String sql = "select * from vm_campaigns_temp where campaign_id = '" + campaignID + "'";
            System.out.println(new Date() + "\t[" + VMCampaignDB.class + "]\tDEBUG\tView campaign: " + sql);

            rs = conn.createStatement().executeQuery(sql);

            while (rs.next()) {
                campaign = new VMCampaign(rs.getString("campaign_id"), rs.getString("account_id"), rs.getString("keyword"),
                        rs.getString("message_sender"), rs.getString("message"), rs.getString("how_to_msg"), rs.getString("follow_up_msg_success"),
                        rs.getString("follow_up_msg_error"), rs.getString("invite_accepted_msg"),
                        rs.getString("invite_accepted_msg"), DateUtil.convertFromMySQLTimeStamp(rs.getString("last_updated")),
                        rs.getBoolean("is_active"), rs.getInt("push_wait_time"));
                break;
            }
            return campaign;
        } catch (Exception ex) {
            System.out.println(new Date() + "\t[" + VMCampaignDB.class + "]\tERROR\tViewing campaign: " + ex.getMessage());
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

    public static VMCampaign viewCampaign(String accountID, String keyword) throws Exception {
        ResultSet rs = null;
        Connection conn = null;
        VMCampaign campaign = null;

        try {
            conn = DConnect.getConnection();
            String sql = "select * from vm_campaigns_temp where account_id = '" + accountID + "' and keyword = '" + keyword + "'";
            System.out.println(new Date() + "\t[" + VMCampaignDB.class + "]\tDEBUG\tView campaign: " + sql);

            rs = conn.createStatement().executeQuery(sql);

            while (rs.next()) {
                campaign = new VMCampaign(rs.getString("campaign_id"), rs.getString("account_id"),
                        rs.getString("keyword"), rs.getString("message_sender"), rs.getString("message"), rs.getString("how_to_msg"),
                        rs.getString("follow_up_msg_success"), rs.getString("follow_up_msg_error"), rs.getString("invite_accepted_msg"),
                        rs.getString("invite_accepted_msg"), DateUtil.convertFromMySQLTimeStamp(rs.getString("last_updated")),
                        rs.getBoolean("is_active"), rs.getInt("push_wait_time"));
                break;
            }

            return campaign;
        } catch (Exception ex) {
            System.out.println(new Date() + "\t[" + VMCampaignDB.class + "]\tERROR\tViewing campaign: " + ex.getMessage());
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

    public static void updateCampaignMessage(String campaignID, String message) throws Exception {
        Connection conn = null;

        try {
            conn = DConnect.getConnection();
            String sql = "UPDATE vm_campaigns_temp SET message = '" + message + "' WHERE campaign_id = '" + campaignID + "'";
            System.out.println(new Date() + "\t[" + VMCampaignDB.class + "]\tDEBUG\tUpdating campaign: " + sql);

            conn.createStatement().executeUpdate(sql);

        } catch (Exception ex) {
            System.out.println(new Date() + ": " + VMCampaignDB.class + "]\tERROR\tUpdating campaign: " + ex.getMessage());
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

            String sql = "UPDATE vm_campaigns_temp SET campaign_id = '" + campaign.getCampaignID() + "', account_id = '" + campaign.getAccountID() + "', "
                    + "keyword = '" + campaign.getKeyword() + "', message_sender = '" + campaign.getMessageSender() + "', "
                    + "message = '" + campaign.getMessage() + "', last_updated = '" + DateUtil.convertToMySQLTimeStamp(campaign.getLastUpdated()) + "' "
                    + "WHERE acount_id = '" + update_account_id + "' and keyword = '" + update_keyword + "'";
            System.out.println(new Date() + "\t[" + VMCampaignDB.class + "]\tDEBUG\tUpdating campaign: " + sql);

            conn.createStatement().executeUpdate(sql);

        } catch (Exception ex) {
            System.out.println(new Date() + "\t[" + VMCampaignDB.class + "]\tERROR\tUpdating campaign: " + ex.getMessage());
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
            String sql = "delete from vm_campaigns_temp where keyword in (" + keywordStr + ") and account_id='" + accountId + "'";
            System.out.println(new Date() + "\t[" + VMCampaignDB.class + "]\tDEBUG\tDelete campaigns: " + sql);

            conn.createStatement().execute(sql);
        } catch (Exception ex) {
            System.out.println(new Date() + "\t[" + VMCampaignDB.class + "]\tERROR\tDelete campaigns: " + ex.getMessage());
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
            String sql = "delete from vm_campaigns_temp where keyword = '" + keyword + "' and account_id= '" + accountId + "'";
            System.out.println(new Date() + "\t[" + VMCampaignDB.class + "]\tDEBUG\tDelete campaign: " + sql);

            conn.createStatement().execute(sql);

        } catch (Exception ex) {
            System.out.println(new Date() + "\t[" + VMCampaignDB.class + "]\tERROR\tDelete campaign: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }
}
