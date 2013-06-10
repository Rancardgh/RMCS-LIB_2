/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.infoserver.common.services;

import com.rancard.common.DConnect;
import com.rancard.util.DateUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author Mustee
 */
public class ServiceSubscriberDB {

    public static void addServiceSubscription(ServiceSubscriber serviceSubscriber) throws SQLException, Exception {
        Connection conn = null;
        String sql = "INSERT INTO service_subscription (subscription_date, next_subscription_date, msisdn, account_id, keyword, status, billing_type) "
                + "VALUES ('" + DateUtil.convertToMySQLTimeStamp(serviceSubscriber.getSubscriptionDate()) + "', '" + DateUtil.convertToMySQLTimeStamp(serviceSubscriber.getNextSubscriptionDate()) + "', "
                + "'" + serviceSubscriber.getMsisdn() + "', '" + serviceSubscriber.getAccountID() + "', '" + serviceSubscriber.getKeyword() + "', " + serviceSubscriber.getStatus() + ", " + serviceSubscriber.getBillingType() + ")";
        try {
            conn = DConnect.getConnection();
            conn.createStatement().execute(sql);

        } catch (SQLException se) {
            throw new SQLException(se.getMessage(), se.getSQLState());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static void addServiceSubscription(ServiceSubscriber serviceSubscriber, Connection conn) throws SQLException {
        String sql = "INSERT INTO service_subscription (subscription_date, next_subscription_date, msisdn, account_id, keyword, status, billing_type) "
                + "VALUES ('" + DateUtil.convertToMySQLTimeStamp(serviceSubscriber.getSubscriptionDate()) + "', '" + DateUtil.convertToMySQLTimeStamp(serviceSubscriber.getNextSubscriptionDate()) + "', "
                + "'" + serviceSubscriber.getMsisdn() + "', '" + serviceSubscriber.getAccountID() + "', '" + serviceSubscriber.getKeyword() + "', " + serviceSubscriber.getStatus() + ", " + serviceSubscriber.getBillingType() + ")";
        try {

            conn.createStatement().execute(sql);

        } catch (SQLException se) {
            throw new SQLException(se.getMessage(), se.getSQLState());
        }
    }

    public static void addSubscription(Date subscriptionDate, Date nextSubscriptionDate, String msisdn, String accountID,
            String keyword, int status, int billingType) throws SQLException, Exception {
        Connection conn = null;
        String sql = "INSERT INTO service_subscription (subscription_date, next_subscription_date, msisdn, account_id, keyword, status, billing_type) "
                + "VALUES ('" + DateUtil.convertToMySQLTimeStamp(subscriptionDate) + "', '" + DateUtil.convertToMySQLTimeStamp(nextSubscriptionDate) + "', "
                + "'" + msisdn + "', '" + accountID + "', '" + keyword + "', " + status + ", " + billingType + ")";
        try {
            conn = DConnect.getConnection();
            conn.createStatement().execute(sql);

        } catch (SQLException se) {
            throw new SQLException(se.getMessage(), se.getSQLState());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static void addSubscription(Date subscriptionDate, Date nextSubscriptionDate, String msisdn, String accountID,
            String keyword, int status, int billingType, Connection conn) throws SQLException {

        String sql = "INSERT INTO service_subscription (subscription_date, next_subscription_date, msisdn, account_id, keyword, status, billing_type) "
                + "VALUES ('" + DateUtil.convertToMySQLTimeStamp(subscriptionDate) + "', '" + DateUtil.convertToMySQLTimeStamp(nextSubscriptionDate) + "', "
                + "'" + msisdn + "', '" + accountID + "', '" + keyword + "', " + status + ", " + billingType + ")";
        try {
            conn.createStatement().execute(sql);
        } catch (SQLException se) {
            throw new SQLException(se.getMessage(), se.getSQLState());
        }
    }

    public static void addSubscription(Date subscriptionDate, int subscriptionInterval, String msisdn, String accountID,
            String keyword, int status, int billingType) throws SQLException, Exception {
        Connection conn = null;
        String sql = "INSERT INTO service_subscription (subscription_date, next_subscription_date, msisdn, account_id, keyword, status, billing_type) "
                + "VALUES ('" + DateUtil.convertToMySQLTimeStamp(subscriptionDate) + "', '" + DateUtil.convertToMySQLTimeStamp(DateUtil.addDaysToDate(subscriptionDate, subscriptionInterval)) + "', "
                + "'" + msisdn + "', '" + accountID + "', '" + keyword + "', " + status + ", " + billingType + ")";
        try {
            conn = DConnect.getConnection();
            conn.createStatement().execute(sql);

        } catch (SQLException se) {
            throw new SQLException(se.getMessage(), se.getSQLState());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static void addSubscription(Date subscriptionDate, int subscriptionInterval, String msisdn, String accountID,
            String keyword, int status, int billingType, Connection conn) throws SQLException, Exception {

        String sql = "INSERT INTO service_subscription (subscription_date, next_subscription_date, msisdn, account_id, keyword, status, billing_type) "
                + "VALUES ('" + DateUtil.convertToMySQLTimeStamp(subscriptionDate) + "', '" + DateUtil.convertToMySQLTimeStamp(DateUtil.addDaysToDate(subscriptionDate, subscriptionInterval)) + "', "
                + "'" + msisdn + "', '" + accountID + "', '" + keyword + "', " + status + ", " + billingType + ")";
        try {
            conn.createStatement().execute(sql);
        } catch (SQLException se) {
            throw new SQLException(se.getMessage(), se.getSQLState());
        }
    }
}
