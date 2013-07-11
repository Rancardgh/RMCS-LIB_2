package com.rancard.mobility.infoserver.common.services;

import com.rancard.common.DConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class UserServiceExperienceDB {

    public static void createServiceExperience(UserServiceExperience serviceExperience) throws Exception {
        Connection conn = null;

        try {
            conn = DConnect.getConnection();
            String sql = "insert into service_experience_config(account_id, site_id, keyword, promo_id, welcome_msg, "
                    + "already_subscribed_msg, unsubscription_conf_msg, promo_msg_sender, welcome_msg_sender, "
                    + "already_subscribed_msg_sender, unsubscription_conf_msg_sender, push_msg_wait_time, subscription_interval, "
                    + "url, url_timeout, meta_data) values('" + serviceExperience.getAccountId() + "', '" + serviceExperience.getSiteId() + "', "
                    + "'" + serviceExperience.getKeyword() + "', '" + serviceExperience.getPromoId() + "', '" + serviceExperience.getWelcomeMsg() + "', "
                    + "'" + serviceExperience.getAlreadySubscribedMsg() + "', '" + serviceExperience.getUnsubscriptionConfirmationMsg() + "', "
                    + "'" + serviceExperience.getPromoMsgSender() + "', '" + serviceExperience.getWelcomeMsgSender() + "', "
                    + "'" + serviceExperience.getAlreadySubscribedMsgSender() + "', '" + serviceExperience.getUnsubscriptionConfirmationMsgSender() + "', "
                    + serviceExperience.getPushMsgWaitTime() + ", " + serviceExperience.getSubscriptionInterval() + ", '" + serviceExperience.getUrl() + "', "
                    + serviceExperience.getUrlTimeout() + ", '" + serviceExperience.getMetaData() + "')";

            System.out.println(new Date() + ": " + UserServiceExperienceDB.class + " : Inserting new service_experience_config: " + sql);

            conn.createStatement().executeQuery(sql);

            System.out.println(new Date() + ": " + UserServiceExperienceDB.class + " : Inserted service_experience_config");


        } catch (SQLException ex) {
            throw new SQLException(ex.getMessage(), ex.getSQLState());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

    }

    public static UserServiceExperience viewServiceExperience(String accountId, String siteId, String keyword) throws Exception {
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DConnect.getConnection();
            String sql = "select * from service_experience_config sec LEFT OUTER JOIN promotional_campaign pc "
                    + "ON sec.promo_id = pc.promo_id where sec.account_id = '" + accountId + "' and "
                    + "sec.site_id = '" + siteId + "' and sec.keyword = '" + keyword + "'";

            System.out.println(new Date() + ": " + UserServiceExperienceDB.class + " : Selecting service_experience_config: " + sql);

            rs = conn.createStatement().executeQuery(sql);

            while (rs.next()) {
                System.out.println(new Date() + ": " + UserServiceExperienceDB.class + " : Found service_experience_config: " + sql);
                return new UserServiceExperience(rs.getString("sec.account_id"), rs.getString("site_id"), rs.getString("keyword"),
                        rs.getString("promo_msg"), rs.getString("sec.promo_id"), rs.getString("promo_response_code"), rs.getString("welcome_msg"),
                        rs.getString("already_subscribed_msg"), rs.getString("unsubscription_conf_msg"), rs.getString("promo_msg_sender"),
                        rs.getString("welcome_msg_sender"), rs.getString("already_subscribed_msg_sender"), rs.getString("unsubscription_conf_msg_sender"),
                        rs.getInt("push_msg_wait_time"), rs.getInt("subscription_interval"), rs.getString("url"), rs.getInt("url_timeout"),
                        rs.getString("meta_data"));

            }

        } catch (SQLException ex) {
            throw new SQLException(ex.getMessage(), ex.getSQLState());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return null;
    }

    public static void deleteServiceExperience(String accountId, String keyword) throws Exception {
        Connection conn = null;

        try {
            conn = DConnect.getConnection();

            String sql = "delete from service_experience_config where keyword = '" + keyword + "' and account_id = '" + accountId + "'";
            System.out.println(new Date() + ": " + UserServiceExperienceDB.class + " : Delete service_experience_config: " + sql);

            conn.createStatement().execute(sql);

            System.out.println(new Date() + ": " + UserServiceExperienceDB.class + " : Deleted service_experience_config: " + accountId + "-" + keyword);

        } catch (Exception ex) {
            throw new Exception(ex.getMessage());

        } finally {
            if (conn != null) {
                conn.close();
            }
        }

    }

    public static void deleteServiceExperience(String accountId, java.util.ArrayList keywords) throws Exception {
        Connection conn = null;
        StringBuilder keywordStr = new StringBuilder();
        
        for (int i = 0; i < keywords.size(); i++) {
            keywordStr.append("'").append(keywords.get(i).toString()).append("',");
        }

        keywordStr.deleteCharAt(keywordStr.toString().lastIndexOf(","));

        try {
            conn = DConnect.getConnection();
            String sql = "delete from service_experience_config where keyword in (" + keywordStr.toString() + ") and account_id='" + accountId + "'";
            System.out.println(new Date() + ": " + UserServiceExperienceDB.class + " : Delete service_experience_config: " + sql);

            conn.createStatement().executeQuery(sql);
            System.out.println(new Date() + ": " + UserServiceExperienceDB.class + " : Deleted service_experience_config: " + accountId + "-" + keywordStr.toString());
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {

            if (conn != null) {
                conn.close();
            }
        }
    }

    public static void updateServiceExperience(String update_account_id, String update_keyword, UserServiceExperience serviceExperience) throws Exception {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        //Check and set update parameters
        String account_id = (serviceExperience.getAccountId() != null && !serviceExperience.getAccountId().equals("")) ? "'" + serviceExperience.getAccountId() + "'" : "account_id";
        String site_id = (serviceExperience.getSiteId() != null && !serviceExperience.getSiteId().equals("")) ? "'" + serviceExperience.getSiteId() + "'" : "site_id";
        String keyword = (serviceExperience.getKeyword() != null && !serviceExperience.getKeyword().equals("")) ? "'" + serviceExperience.getKeyword() + "'" : "keyword";
        String promo_id = (serviceExperience.getPromoId() != null && !serviceExperience.getPromoId().equals("")) ? "'" + serviceExperience.getPromoId() + "'" : "promo_id";
        String welcome_msg = (serviceExperience.getWelcomeMsg() != null && !serviceExperience.getWelcomeMsg().equals("")) ? "'" + serviceExperience.getWelcomeMsg() + "'" : "welcome_msg";
        String already_subscribed_msg = (serviceExperience.getAlreadySubscribedMsg() != null && !serviceExperience.getAlreadySubscribedMsg().equals("")) ? "'" + serviceExperience.getAlreadySubscribedMsg() + "'" : "already_subscribed_msg";
        String unsubscription_conf_msg = (serviceExperience.getUnsubscriptionConfirmationMsg() != null && !serviceExperience.getUnsubscriptionConfirmationMsg().equals("")) ? "'" + serviceExperience.getUnsubscriptionConfirmationMsg() + "'" : "unsubscription_conf_msg";
        String promo_msg_sender = (serviceExperience.getPromoMsgSender() != null && !serviceExperience.getPromoMsgSender().equals("")) ? "'" + serviceExperience.getPromoMsgSender() + "'" : "promo_msg_sender";
        String welcome_msg_sender = (serviceExperience.getWelcomeMsgSender() != null && !serviceExperience.getWelcomeMsgSender().equals("")) ? "'" + serviceExperience.getWelcomeMsgSender() + "'" : "welcome_msg_sender";
        String already_subscribed_msg_sender = (serviceExperience.getAlreadySubscribedMsgSender() != null && !serviceExperience.getAlreadySubscribedMsgSender().equals("")) ? "'" + serviceExperience.getAlreadySubscribedMsgSender() + "'" : "already_subscribed_msg_sender";
        String unsubscription_conf_msg_sender = (serviceExperience.getUnsubscriptionConfirmationMsgSender() != null && !serviceExperience.getUnsubscriptionConfirmationMsgSender().equals("")) ? "'" + serviceExperience.getUnsubscriptionConfirmationMsgSender() + "'" : "unsubscription_conf_msg_sender";
        int push_msg_wait_time = serviceExperience.getPushMsgWaitTime();
        int subscription_interval = serviceExperience.getSubscriptionInterval();
        String url = (serviceExperience.getUrl() != null && !serviceExperience.getUrl().equals("")) ? "'" + serviceExperience.getUrl() + "'" : "url";
        int url_timeout = serviceExperience.getUrlTimeout();
        String meta_data = (serviceExperience.getMetaData() != null && !serviceExperience.getMetaData().equals("")) ? "'" + serviceExperience.getMetaData() + "'" : "meta_data";


        try {
            con = DConnect.getConnection();
            SQL = "UPDATE service_experience_config SET account_id = " + account_id + ", site_id = " + site_id + ", keyword = " + keyword + ","
                    + "promo_id = " + promo_id + ", welcome_msg = " + welcome_msg + ", already_subscribed_msg = " + already_subscribed_msg + ", unsubscription_conf_msg = " + unsubscription_conf_msg + ", "
                    + "promo_msg_sender = " + promo_msg_sender + ", welcome_msg_sender = " + welcome_msg_sender + ", already_subscribed_msg_sender = " + already_subscribed_msg_sender + ", "
                    + "unsubscription_conf_msg_sender = " + unsubscription_conf_msg_sender + ", push_msg_wait_time = " + push_msg_wait_time + ", subscription_interval = " + subscription_interval + ", "
                    + "url = " + url + ", url_timeout = " + url_timeout + ", meta_data = " + meta_data + " WHERE account_id = ? and keyword = ? ";

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
}
