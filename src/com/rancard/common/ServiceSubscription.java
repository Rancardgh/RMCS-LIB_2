package com.rancard.common;

import com.rancard.common.db.DConnect;
import com.rancard.util.DateUtil;
import com.rancard.util.Utils;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by Mustee on 3/29/2014.
 */
public class ServiceSubscription {
    private static Logger logger = Logger.getLogger(ServiceSubscription.class.getName());

    private String keyword;
    private String accountID;
    private String msisdn;
    private Date subscriptionDate;
    private Date nextSubscriptionDate;
    private int status;
    private int billingType;
    private Channel channel;
    private String metaData;

    public ServiceSubscription(String accountID, String keyword, String msisdn, Date subscriptionDate, Date nextSubscriptionDate, Integer status, Integer billingType, Channel channel, String metaData) {
        this.keyword = keyword;
        this.msisdn = msisdn;
        this.accountID = accountID;
        this.subscriptionDate = subscriptionDate;
        this.nextSubscriptionDate = nextSubscriptionDate;
        this.status = status;
        this.billingType = billingType;
        this.channel = channel;
        this.metaData = metaData;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setBillingType(int billingType) {
        this.billingType = billingType;
    }

    public Date getSubscriptionDate() {
        return subscriptionDate;
    }

    public void setSubscriptionDate(Date subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }

    public Date getNextSubscriptionDate() {
        return nextSubscriptionDate;
    }

    public void setNextSubscriptionDate(Date nextSubscriptionDate) {
        this.nextSubscriptionDate = nextSubscriptionDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getBillingType() {
        return billingType;
    }

    public void setBillingType(Integer billingType) {
        this.billingType = billingType;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getMetaData() {
        return metaData;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }

    public static void createSubscription(ServiceSubscription subscription) throws Exception {
        Connection conn = null;
        Statement statement = null;

        String sql = "INSERT INTO service_subscription (next_subscription_date, msisdn, account_id, keyword, status, billing_type) "
                + "VALUES ('" + DateUtil.formatToTimeStamp(subscription.getNextSubscriptionDate()) + "', " + "'" + subscription.getMsisdn()
                + "', '" + subscription.getAccountID() + "', '" + subscription.getKeyword() + "', " + subscription.getStatus()
                + ", " + subscription.getBillingType() + ")";
        String sqlExt = "INSERT INTO service_subscription_ext (msisdn, account_id, keyword, channel, meta_data) VALUES ('" + subscription.getMsisdn()
                + "', '" + subscription.getAccountID() + "', '" + subscription.getKeyword() + "', '" + subscription.getChannel() + "', "
                + "'" + subscription.getMetaData() + "'" + ")";
        try {
            conn = DConnect.getConnection();
            statement = conn.createStatement();

            logger.info("Adding statement to batch: " + sql);
            statement.addBatch(sql);

            logger.info("Adding statement to batch: " + sqlExt);
            statement.addBatch(sqlExt);

            logger.info("Executing statements.");
            statement.executeBatch();

        } catch (Exception e) {
            logger.severe("Error adding service subscription: " + e.getMessage());
            throw new Exception(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }


    public static ServiceSubscription getLastSubscription(String msisdn) throws Exception {
        final String query = "SELECT * FROM service_subscription ss LEFT JOIN service_subscription_ext sse ON ss.msisdn = sse.msisdn "
                + "AND ss.keyword = sse.keyword AND ss.account_id = sse.account_id WHERE ss.msisdn = '" + msisdn + "' ORDER BY subscription_date desc LIMIT 1";
        Connection conn = null;
        ResultSet rs = null;

        try {
            logger.info("Getting last subscription: " + query);
            conn = DConnect.getConnection();
            rs = conn.createStatement().executeQuery(query);

            if (rs.next()) {
                return new ServiceSubscription(rs.getString("ss.account_id"), rs.getString("ss.keyword"), rs.getString("ss.msisdn"), DateUtil.convertFromTimeStampFormat(rs.getString("ss.subscription_date")),
                        DateUtil.convertFromTimeStampFormat(rs.getString("ss.next_subscription_date")), rs.getInt("ss.status"), rs.getInt("ss.billing_type"), Channel.valueOf(Channel.class, rs.getString("sse.channel")), rs.getString("sse.meta_data"));
            }
            return null;
        } catch (Exception e) {
            logger.severe("Problem getting last subscription: " + e.getMessage());
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

    public static void delete(String msisdn, String accountID, String keyword) throws Exception {
        final String sql = "DELETE FROM service_subscription WHERE msisdn = '" + msisdn + "' AND keyword = '" + keyword + "' AND account_id = '" + accountID + "'";
        final String sqlExt = "DELETE FROM service_subscription_ext WHERE msisdn = '" + msisdn + "' AND keyword = '" + keyword + "' AND account_id = '" + accountID + "'";

        Connection conn = null;
        Statement statement = null;
        try {
            conn = DConnect.getConnection();
            statement = conn.createStatement();

            logger.info("Adding statement to batch: " + sql);
            statement.addBatch(sql);

            logger.info("Adding statement to batch: " + sqlExt);
            statement.addBatch(sqlExt);

            logger.info("Executing statements.");
            statement.executeBatch();

        } catch (Exception e) {
            logger.severe("Error updating service subscription: " + e.getMessage());
            throw new Exception(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (conn != null) {
                conn.close();
            }

        }

    }


    public static ServiceSubscription find(String msisdn, String accountID, String keyword) throws Exception {
        final String sql = "SELECT * FROM service_subscription ss LEFT JOIN service_subscription_ext sse ON ss.msisdn = sse.msisdn "
                + "AND ss.keyword = sse.keyword AND ss.account_id = sse.account_id WHERE ss.msisdn = '" + msisdn + "' "
                + "AND ss.keyword = '" + keyword + "' AND ss.account_id = '" + accountID + "'";


        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = DConnect.getConnection();
            rs = conn.createStatement().executeQuery(sql);

            if (rs.next()) {
                return new ServiceSubscription(rs.getString("ss.account_id"), rs.getString("ss.keyword"), rs.getString("ss.msisdn"), DateUtil.convertFromTimeStampFormat(rs.getString("ss.subscription_date")),
                        DateUtil.convertFromTimeStampFormat(rs.getString("ss.next_subscription_date")), rs.getInt("ss.status"), rs.getInt("ss.billing_type"),
                        rs.getString("sse.channel") == null ? null : Channel.valueOf(rs.getString("sse.channel")), rs.getString("sse.meta_data"));
            }

            return null;

        } catch (Exception e) {
            logger.severe("Error finding service subscription: " + e.getMessage());
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

    public static void update(ServiceSubscription subscription) throws Exception {
        final String sql = "UPDATE service_subscription ss LEFT JOIN service_subscription_ext sse ON ss.msisdn = sse.msisdn AND ss.keyword = sse.keyword AND ss.account_id = sse.account_id SET "
                + "ss.subscription_date = ss.subscription_date, ss.next_subscription_date = '" + DateUtil.formatToTimeStamp(subscription.getNextSubscriptionDate()) + "', "
                + "ss.status = " + subscription.getStatus() + ", ss.billing_type = " + subscription.getBillingType() + ", sse.channel = '" + subscription.getChannel() + "', "
                + "sse.meta_data = '" + subscription.getMetaData() + "' WHERE ss.msisdn = '" + subscription.getMsisdn() + "' AND ss.keyword = '" + subscription.getKeyword()
                + "' AND ss.account_id = '" + subscription.getAccountID() + "'";
        logger.info("About to update service_subscription: " + sql);

        Connection conn = null;
        try {
            conn = DConnect.getConnection();
            conn.createStatement().execute(sql);
        } catch (Exception e) {
            logger.severe("Error updating service subscription: " + e.getMessage());
            throw new Exception(e.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }

        }
    }


    public static List<ServiceSubscription> findBySMSCAndShortCode(String smsc, String shortCode, String msisdn) throws Exception {
        List<ServiceSubscription> services = new ArrayList<ServiceSubscription>();

        Set<String> accountIDs = new HashSet<String>();
        for (ServiceDefinition service : ServiceDefinition.findBySMSCAndShortCode(smsc, shortCode)) {
            accountIDs.add(service.getAccountID());
        }

        String query = "SELECT * FROM service_subscription ss LEFT JOIN service_subscription_ext sse ON ss.msisdn = sse.msisdn AND ss.keyword = sse.keyword AND ss.account_id = sse.account_id WHERE ss.account_id IN (" + StringUtils.join(accountIDs, ",") + ") AND ss.msisdn = '" + msisdn + "'";

        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = DConnect.getConnection();
            rs = conn.createStatement().executeQuery(query);

            if (rs.next()) {
                services.add(new ServiceSubscription(rs.getString("ss.account_id"), rs.getString("ss.keyword"), rs.getString("ss.msisdn"), DateUtil.convertFromTimeStampFormat(rs.getString("ss.subscription_date")),
                        DateUtil.convertFromTimeStampFormat(rs.getString("next_subscription_date")), rs.getInt("ss.status"), rs.getInt("ss.billing_type"), rs.getString("sse.channel") == null? null: Channel.valueOf(rs.getString("sse.channel")), rs.getString("sse.meta_data")));
            }

            return services;

        } catch (Exception e) {
            logger.severe("Error finding services: " + e.getMessage());
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


    public static void deleteBySMSCAndShortCode(String smsc, String shortCode, String msisdn) throws Exception {
        List<ServiceSubscription> subscriptions = findBySMSCAndShortCode(smsc, shortCode, msisdn);

        for (ServiceSubscription subscription : subscriptions) {
            delete(subscription.getMsisdn(), subscription.getAccountID(), subscription.getKeyword());
        }

    }

}
