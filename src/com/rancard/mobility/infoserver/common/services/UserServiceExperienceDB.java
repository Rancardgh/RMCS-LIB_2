package com.rancard.mobility.infoserver.common.services;

import java.sql.*;
import com.rancard.common.DConnect;

public class UserServiceExperienceDB {

    public static void createServiceExperience(UserServiceExperience serviceExperience) throws Exception {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            SQL = "insert into service_experience_config(account_id, site_id, keyword, "
                    + "promo_id, welcome_msg, already_subscribed_msg, unsubscription_conf_msg, "
                    + "promo_msg_sender, welcome_msg_sender, already_subscribed_msg_sender, "
                    + "unsubscription_conf_msg_sender, push_msg_wait_time, subscription_interval, "
                    + "url, url_timeout, meta_data) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, serviceExperience.getAccountId());
            prepstat.setString(2, serviceExperience.getSiteId());
            prepstat.setString(3, serviceExperience.getKeyword());
            prepstat.setString(4, serviceExperience.getPromoId());
            prepstat.setString(5, serviceExperience.getWelcomeMsg());
            prepstat.setString(6, serviceExperience.getAlreadySubscribedMsg());
            prepstat.setString(7, serviceExperience.getUnsubscriptionConfirmationMsg());
            prepstat.setString(8, serviceExperience.getPromoMsgSender());
            prepstat.setString(9, serviceExperience.getWelcomeMsgSender());
            prepstat.setString(10, serviceExperience.getAlreadySubscribedMsgSender());
            prepstat.setString(11, serviceExperience.getUnsubscriptionConfirmationMsgSender());
            prepstat.setInt(12, serviceExperience.getPushMsgWaitTime());
            prepstat.setInt(13, serviceExperience.getSubscriptionInterval());
            prepstat.setString(14, serviceExperience.getUrl());
            prepstat.setInt(15, serviceExperience.getUrlTimeout());
            prepstat.setString(16, serviceExperience.getMetaData());

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

    public static UserServiceExperience viewServiceExperience(String accountId, String siteId, String keyword) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        UserServiceExperience serviceExperience = new UserServiceExperience();

        try {
            con = DConnect.getConnection();

            SQL = "select * from service_experience_config sec LEFT OUTER JOIN promotional_campaign pc "
                    + "ON sec.promo_id = pc.promo_id where sec.account_id = ? and sec.site_id = ? and sec.keyword = ? ";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, accountId);
            prepstat.setString(2, siteId);
            prepstat.setString(3, keyword);

            rs = prepstat.executeQuery();

            while (rs.next()) {
                serviceExperience.setAccountId(rs.getString("sec.account_id"));
                serviceExperience.setSiteId(rs.getString("site_id"));
                serviceExperience.setKeyword(rs.getString("keyword"));
                serviceExperience.setPromoId(rs.getString("sec.promo_id"));
                serviceExperience.setPromoMsg(rs.getString("promo_msg"));
                serviceExperience.setPromoRespCode(rs.getString("promo_response_code"));
                serviceExperience.setWelcomeMsg(rs.getString("welcome_msg"));
                serviceExperience.setAlreadySubscribedMsg(rs.getString("already_subscribed_msg"));
                serviceExperience.setUnsubscriptionConfirmationMsg(rs.getString("unsubscription_conf_msg"));
                serviceExperience.setPromoMsgSender(rs.getString("promo_msg_sender"));
                serviceExperience.setWelcomeMsgSender(rs.getString("welcome_msg_sender"));
                serviceExperience.setAlreadySubscribedMsgSender(rs.getString("already_subscribed_msg_sender"));
                serviceExperience.setUnsubscriptionConfirmationMsgSender(rs.getString("unsubscription_conf_msg_sender"));
                serviceExperience.setPushMsgWaitTime(rs.getInt("push_msg_wait_time"));
                serviceExperience.setSubscriptionInterval(rs.getInt("subscription_interval"));
                serviceExperience.setUrl(rs.getString("url"));
                serviceExperience.setUrlTimeout(rs.getInt("url_timeout"));
                serviceExperience.setMetaData(rs.getString("meta_data"));
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

        return serviceExperience;
    }

    public static void deleteServiceExperience(String accountId, String keyword) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        //UserServiceExperience serviceExperience = new UserServiceExperience();

        try {
            con = DConnect.getConnection();

            SQL = "delete from service_experience_config where keyword = ? and account_id = ?";

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

    public static void deleteServiceExperience(String accountId, java.util.ArrayList keywords) throws Exception {

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
            SQL = "delete from service_experience_config where keyword in (" + keywordStr + ") and account_id='" + accountId + "'";
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
