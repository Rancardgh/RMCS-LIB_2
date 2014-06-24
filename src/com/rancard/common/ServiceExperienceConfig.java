package com.rancard.common;


import com.rancard.common.db.DConnect;
import com.rancard.util.DateUtil;
import com.rancard.util.Utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.logging.Logger;

/**
 * Created by Mustee on 3/31/2014.
 */
public class ServiceExperienceConfig {
    private static Logger logger = Logger.getLogger(ServiceExperienceConfig.class.getName());

    private String keyword;
    private String accountId;
    private String siteId;
    private String promoId;
    private String welcomeMessage;
    private String alreadySubscribedMessage;
    private String unsubscriptionMessage;
    private String promoMessageSender;
    private String welcomeMessageSender;
    private String alreadySubscribedMessageSender;
    private String unsubscriptionMessageSender;
    private int pushMessageWaitTime;
    private int subscriptionInterval;
    private String url;
    private int urlTimeout;
    private String metaData;


    public ServiceExperienceConfig(String keyword, String accountId, String siteId, String promoId, String welcomeMessage, String alreadySubscribedMessage, String unsubscriptionMessage, String promoMessageSender, String welcomeMessageSender, String alreadySubscribedMessageSender, String unsubscriptionMessageSender, int pushMessageWaitTime, int subscriptionInterval, String url, int urlTimeout, String metaData) {
        this.keyword = keyword;
        this.accountId = accountId;
        this.siteId = siteId;
        this.promoId = promoId;
        this.welcomeMessage = welcomeMessage;
        this.alreadySubscribedMessage = alreadySubscribedMessage;
        this.unsubscriptionMessage = unsubscriptionMessage;
        this.promoMessageSender = promoMessageSender;
        this.welcomeMessageSender = welcomeMessageSender;
        this.alreadySubscribedMessageSender = alreadySubscribedMessageSender;
        this.unsubscriptionMessageSender = unsubscriptionMessageSender;
        this.pushMessageWaitTime = pushMessageWaitTime;
        this.subscriptionInterval = subscriptionInterval;
        this.url = url;
        this.urlTimeout = urlTimeout;
        this.metaData = metaData;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getPromoId() {
        return promoId;
    }

    public void setPromoId(String promoId) {
        this.promoId = promoId;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public String getAlreadySubscribedMessage() {
        return alreadySubscribedMessage;
    }

    public void setAlreadySubscribedMessage(String alreadySubscribedMessage) {
        this.alreadySubscribedMessage = alreadySubscribedMessage;
    }

    public String getUnsubscriptionMessage() {
        return unsubscriptionMessage;
    }

    public void setUnsubscriptionMessage(String unsubscriptionMessage) {
        this.unsubscriptionMessage = unsubscriptionMessage;
    }

    public String getPromoMessageSender() {
        return promoMessageSender;
    }

    public void setPromoMessageSender(String promoMessageSender) {
        this.promoMessageSender = promoMessageSender;
    }

    public String getWelcomeMessageSender() {
        return welcomeMessageSender;
    }

    public void setWelcomeMessageSender(String welcomeMessageSender) {
        this.welcomeMessageSender = welcomeMessageSender;
    }

    public String getAlreadySubscribedMessageSender() {
        return alreadySubscribedMessageSender;
    }

    public void setAlreadySubscribedMessageSender(String alreadySubscribedMessageSender) {
        this.alreadySubscribedMessageSender = alreadySubscribedMessageSender;
    }

    public String getUnsubscriptionMessageSender() {
        return unsubscriptionMessageSender;
    }

    public void setUnsubscriptionMessageSender(String unsubscriptionMessageSender) {
        this.unsubscriptionMessageSender = unsubscriptionMessageSender;
    }

    public Integer getPushMessageWaitTime() {
        return pushMessageWaitTime;
    }

    public void setPushMessageWaitTime(Integer pushMessageWaitTime) {
        this.pushMessageWaitTime = pushMessageWaitTime;
    }

    public Integer getSubscriptionInterval() {
        return subscriptionInterval;
    }

    public void setSubscriptionInterval(Integer subscriptionInterval) {
        this.subscriptionInterval = subscriptionInterval;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getUrlTimeout() {
        return urlTimeout;
    }

    public void setUrlTimeout(int urlTimeout) {
        this.urlTimeout = urlTimeout;
    }

    public String getMetaData() {
        return metaData;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }

    public static ServiceExperienceConfig find(String keyword, String accountId, String siteId) throws Exception {
        final String query = "SELECT * FROM service_experience_config WHERE keyword = '" + keyword + "' AND account_id = '" + accountId + "' AND site_id = '" + siteId + "'";

        Connection conn = null;
        ResultSet rs = null;
        try {
            logger.info("Getting service experience: " + query);
            conn = DConnect.getConnection();
            rs = conn.createStatement().executeQuery(query);

            if (rs.next()) {
                return new ServiceExperienceConfig(rs.getString("keyword"), rs.getString("account_id"), rs.getString("site_id"), rs.getString("promo_id"),
                        rs.getString("welcome_msg"), rs.getString("already_subscribed_msg"), rs.getString("unsubscription_conf_msg"), rs.getString("promo_msg_sender"),
                        rs.getString("welcome_msg_sender"), rs.getString("already_subscribed_msg_sender"), rs.getString("unsubscription_conf_msg_sender"),
                        rs.getInt("push_msg_wait_time"), rs.getInt("subscription_interval"), rs.getString("url"), rs.getInt("url_timeout"), rs.getString("meta_data"));
            }
            return null;

        } catch (Exception e) {
            logger.severe("Problem getting services: " + e.getMessage());
            throw new Exception(e.getMessage());
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
