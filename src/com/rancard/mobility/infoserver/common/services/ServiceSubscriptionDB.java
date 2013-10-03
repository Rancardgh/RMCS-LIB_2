/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.infoserver.common.services;

import com.rancard.common.DConnect;
import com.rancard.mobility.common.ThreadedPostman;
import com.rancard.util.DateUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Mustee
 */
public class ServiceSubscriptionDB {

    public static void addSubscription(Date subscriptionDate, Date nextSubscriptionDate, String msisdn, String accountID,
            String keyword, int status, int billingType) throws Exception {
        Connection conn = null;

        try {
            String nextSubDate = nextSubscriptionDate == null ? "'', " : "'" + DateUtil.convertToMySQLTimeStamp(nextSubscriptionDate) + "'";
            String sql = "INSERT INTO service_subscription (subscription_date, next_subscription_date, msisdn, account_id, keyword, status, billing_type) "
                    + "VALUES ('" + DateUtil.convertToMySQLTimeStamp(subscriptionDate) + "', " + nextSubDate + ", "
                    + "'" + msisdn + "', '" + accountID + "', '" + keyword + "', " + status + ", " + billingType + ")";
            System.out.println(new Date() + "\t[" + ServiceSubscriptionDB.class + "]\tDEBUG\tAdding subscription: " + sql);

            conn = DConnect.getConnection();
            conn.createStatement().execute(sql);

            Map<String, String> params = new HashMap<String, String>();
            params.put("msisdn", msisdn.substring(msisdn.indexOf("+") + 1));
            params.put("keyword", keyword);
            new Thread(new ThreadedPostman(ThreadedPostman.RNDVU_BUY_USER_ACTION_API_TMPLT, params)).start();

        } catch (Exception e) {
            System.out.println(new Date() + "\t[" + ServiceSubscriptionDB.class + "]\tERROR\tAdding subscription: " + e.getMessage());
            throw new Exception(e.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static void addServiceSubscription(ServiceSubscription serviceSubscriber) throws Exception {
        addSubscription(serviceSubscriber.getSubscriptionDate(), serviceSubscriber.getNextSubscriptionDate(),
                serviceSubscriber.getMsisdn(), serviceSubscriber.getAccountID(), serviceSubscriber.getKeyword(),
                serviceSubscriber.getStatus(), serviceSubscriber.getBillingType());
    }

    public static void addSubscription(Date subscriptionDate, int subscriptionInterval, String msisdn, String accountID,
            String keyword, int status, int billingType) throws Exception {
        addSubscription(subscriptionDate, subscriptionInterval == 0 ? null : DateUtil.addDaysToDate(subscriptionDate, subscriptionInterval),
                msisdn, accountID, keyword, status, billingType);
    }

    public static void deleteSubscription(String msisdn, String accountID, String keyword) throws Exception {
        Connection conn = null;

        try {
            String sql = "DELETE FROM service_subscription WHERE msisdn = '" + msisdn + "' AND keyword = '" + keyword + "' AND account_id = '" + accountID + "'";
            System.out.println(new Date() + "\t[" + ServiceSubscriptionDB.class + "]\tINFO\tRemoving subscription: " + sql);

            conn = DConnect.getConnection();
            conn.createStatement().execute(sql);

        } catch (Exception e) {
            System.out.println(new Date() + "\t[" + ServiceSubscriptionDB.class + "]\tERROR\tRemoving subscription: " + e.getMessage());
            throw new Exception(e.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static void deleteSubscription(String msisdn, String accountID, List<String> keywords) throws Exception {
        Connection conn = null;

        try {
            String sql = "DELETE FROM service_subscription WHERE msisdn = '" + msisdn + "' AND keyword IN (" + stitchKeywords(keywords) + ") AND account_id = '" + accountID + "'";
            System.out.println(new Date() + "\t[" + ServiceSubscriptionDB.class + "]\tINFO\tRemoving subscriptions: " + sql);

            conn = DConnect.getConnection();
            conn.createStatement().execute(sql);

        } catch (Exception e) {
            System.out.println(new Date() + "\t[" + ServiceSubscriptionDB.class + "]\tERROR\tRemoving subscriptions: " + e.getMessage());
            throw new Exception(e.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static void deleteSubscription(String msisdn, String accountID) throws Exception {
        Connection conn = null;

        try {
            String sql = "DELETE FROM service_subscription WHERE msisdn = '" + msisdn + "' AND account_id = '" + accountID + "'";
            System.out.println(new Date() + "\t[" + ServiceSubscriptionDB.class + "]\tINFO\tRemoving subscription: " + sql);

            conn = DConnect.getConnection();
            conn.createStatement().execute(sql);

        } catch (Exception e) {
            System.out.println(new Date() + "\t[" + ServiceSubscriptionDB.class + "]\tERROR\tRemoving subscription: " + e.getMessage());
            throw new Exception(e.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static ServiceSubscription viewSubscription(String msisdn, String accountID, String keyword) throws Exception {
        Connection conn = null;
        ResultSet rs = null;
        ServiceSubscription serviceSubscriber = null;

        try {
            String sql = "SELECT * FROM service_subscription WHERE msisdn = '" + msisdn + "' AND keyword = '" + keyword + "' AND account_id = '" + accountID + "'";
            System.out.println(new Date() + "\t[" + ServiceSubscriptionDB.class + "]\tINFO\tViewing subscription: " + sql);

            conn = DConnect.getConnection();
            rs = conn.createStatement().executeQuery(sql);

            if (rs.next()) {
                String nextSubscriptionDate = rs.getString("next_subscription_date") == null || rs.getString("next_subscription_date").trim().equals("")
                        ? null : rs.getString("next_subscription_date");

                serviceSubscriber = new ServiceSubscription(rs.getString("msisdn"), rs.getString("account_id"), rs.getString("keyword"),
                        DateUtil.convertFromMySQLTimeStamp(rs.getString("subscription_date")), DateUtil.convertFromMySQLTimeStamp(nextSubscriptionDate),
                        rs.getInt("status"), rs.getInt("billing_type"));
            }

            return serviceSubscriber;

        } catch (Exception e) {
            System.out.println(new Date() + "\t[" + ServiceSubscriptionDB.class + "]\tERROR\tViewing subscription: " + e.getMessage());
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

    public static List<ServiceSubscription> viewSubscription(String msisdn) throws Exception {
        List<ServiceSubscription> serviceSubscribers = new ArrayList<ServiceSubscription>();
        Connection conn = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT * FROM service_subscription WHERE msisdn = '" + msisdn + "'";
            System.out.println(new Date() + "\t[" + ServiceSubscriptionDB.class + "]\tINFO\tViewing subscription: " + sql);

            conn = DConnect.getConnection();
            rs = conn.createStatement().executeQuery(sql);

            while (rs.next()) {
                String nextSubscriptionDate = rs.getString("next_subscription_date") == null || rs.getString("next_subscription_date").trim().equals("")
                        ? null : rs.getString("next_subscription_date");

                serviceSubscribers.add(new ServiceSubscription(rs.getString("msisdn"), rs.getString("account_id"), rs.getString("keyword"),
                        DateUtil.convertFromMySQLTimeStamp(rs.getString("subscription_date")), DateUtil.convertFromMySQLTimeStamp(nextSubscriptionDate),
                        rs.getInt("status"), rs.getInt("billing_type")));
            }

            return serviceSubscribers;
        } catch (Exception e) {
            System.out.println(new Date() + "\t[" + ServiceSubscriptionDB.class + "]\tERROR\tViewing subscription: " + e.getMessage());
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

    public static void updateNextSubscriptionDate(String msisdn, String accountID, String keyword, Date nextSubscriptionDate) throws Exception {
        Connection conn = null;

        try {
            String sql = "UPDATE service_subscription SET subscription_date = subscription_date, next_subscription_date = " + (nextSubscriptionDate == null
                    ? "''" : "'" + DateUtil.convertToMySQLTimeStamp(nextSubscriptionDate)) + "' WHERE msisdn = '" + msisdn
                    + "' AND keyword = '" + keyword + "' AND account_id = '" + accountID + "'";
            System.out.println(new Date() + "\t[" + ServiceSubscriptionDB.class + "]\tINFO\tUpdate next subscription date: " + sql);

            conn = DConnect.getConnection();
            conn.createStatement().executeUpdate(sql);

        } catch (Exception e) {
            System.out.println(new Date() + "\t[" + ServiceSubscriptionDB.class + "]\tERROR\tUpdate next subscription date: " + e.getMessage());
            throw new Exception(e.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static void updateNextSubscriptionDate(String msisdn, String accountID, String keyword, int subscriptionInterval) throws Exception {
        if (subscriptionInterval == 0) {
            return;
        }

        ServiceSubscription serviceSubscriber = viewSubscription(msisdn, accountID, keyword);

        if (serviceSubscriber == null) {
            return;
        }

        Date today = new Date();
        Date nextSubscriptionDate;
        if(serviceSubscriber.getNextSubscriptionDate() == null){
            nextSubscriptionDate = DateUtil.addDaysToDate(today, subscriptionInterval);
        }else if(serviceSubscriber.getNextSubscriptionDate().before(today)){
            nextSubscriptionDate = DateUtil.addDaysToDate(today, subscriptionInterval);
        } else{
            nextSubscriptionDate = DateUtil.addDaysToDate(serviceSubscriber.getNextSubscriptionDate(), subscriptionInterval);
        }

        updateNextSubscriptionDate(msisdn, accountID, keyword, nextSubscriptionDate);

    }

    private static String stitchKeywords(List<String> keywords) {
        StringBuilder sb = new StringBuilder();

        for (String keyword : keywords) {
            sb.append("'").append(keyword).append("',");
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }
}
