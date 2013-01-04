package com.rancard.mobility.infoserver.common.services;

import com.rancard.common.DConnect;
import com.rancard.common.Feedback;
import com.rancard.common.uidGen;
import com.rancard.util.Page;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public abstract class UserServiceDB {

    public static void createService(UserService service) throws Exception {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            SQL = "Insert into service_definition(service_type, keyword, account_id, service_name, default_message, command, allowed_shortcodes, allowed_site_types,"
                    + " is_basic, pricing, is_subscription, service_response_sender) values(?,?,?,?,?,?,?,?,?,?,?,?)";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, service.getServiceType());
            prepstat.setString(2, service.getKeyword());
            prepstat.setString(3, service.getAccountId());
            prepstat.setString(4, service.getServiceName());
            prepstat.setString(5, service.getDefaultMessage());
            prepstat.setString(6, service.getCommand());
            prepstat.setString(7, service.getAllowedShortcodes());
            prepstat.setString(8, service.getAllowedSiteTypes());
            if (service.isBasic()) {
                prepstat.setInt(9, 1);
            } else {
                prepstat.setInt(9, 0);
            }
            //prepstat.setString (10, service.getAllowedNetworks ());
            prepstat.setString(10, service.getPricing());
            if (service.isSubscription()) {
                prepstat.setInt(11, 1);
            } else {
                prepstat.setInt(11, 0);
            }
            prepstat.setString(12, service.getServiceResponseSender());

            prepstat.execute();

        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (prepstat != null) {
                prepstat.close();
            }
            if (con != null) {
                con.close();
            }
        }

    }

    public static void updateService(String serviceType, String defaultMessage, String serviceName, String keyword, String accountId) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            SQL =
                    "UPDATE service_definition SET default_message=?,service_name=? "
                    + "WHERE keyword=?  and account_id =?";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, defaultMessage);
            prepstat.setString(2, serviceName);
            prepstat.setString(3, serviceType);
            prepstat.setString(4, keyword);
            prepstat.setString(5, accountId);
            prepstat.execute();

        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (prepstat != null) {
                prepstat.close();
            }
            if (con != null) {
                con.close();
            }
        }

    }

    public static void updateService(String defaultMessage, String keyword, String accountId) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            SQL =
                    "UPDATE service_definition SET default_message=? , last_updated = ? "
                    + "WHERE keyword=?  and account_id =?";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, defaultMessage);
            prepstat.setTimestamp(2, new java.sql.Timestamp(new java.util.Date().getTime()));
            prepstat.setString(3, keyword);
            prepstat.setString(4, accountId);
            prepstat.execute();

        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (prepstat != null) {
                prepstat.close();
            }
            if (con != null) {
                con.close();
            }
        }

    }

    public static void deleteService(String keyword, String accountId) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            SQL =
                    "delete from service_definition where keyword = ? and account_id=? ";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, keyword);
            prepstat.setString(2, accountId);

            prepstat.execute();

        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (prepstat != null) {
                prepstat.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public static void deleteService(ArrayList keywords, String accountId) throws Exception {

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
            SQL = "delete from service_definition where keyword in (" + keywordStr + ") and account_id='" + accountId + "'";
            prepstat = con.prepareStatement(SQL);
            prepstat.execute();
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {

                rs.close();

            }
            if (prepstat != null) {

                prepstat.close();

            }
            if (con != null) {

                con.close();

            }
        }
    }

    public static int getLastAccessCount(String msisdn, String accountId, String keyword) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        int accessCount = 0;

        //------log Statement
        System.out.println(new java.util.Date() + ":@com.rancard.mobility.infoserver.common.services.UserServiceDB...");
        System.out.println(new java.util.Date() + ": retrieving access count for last service (" + accountId + ", " + keyword + ") by " + msisdn + "...");

        try {
            con = DConnect.getConnection();

            SQL = "select count(*) as 'access_count' from subscriber_request_history where msisdn = ? and account_id = ? and keyword = ? and date(log_time) = CURRENT_DATE order by log_time desc limit 5";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, msisdn);
            prepstat.setString(2, accountId);
            prepstat.setString(3, keyword);

            rs = prepstat.executeQuery();

            while (rs.next()) {
                accessCount = rs.getInt("access_count");

                //log statement: info
                System.out.println(new java.util.Date() + ": access count for last service (" + accountId + ", " + keyword + ") by " + msisdn + " is " + accessCount);
            }

        } catch (Exception ex) {

            //error log
            System.out.println(new java.util.Date() + ": error retrieving access count for last service (" + accountId + ", " + keyword + ") by " + msisdn + " :: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {

                rs.close();

            }
            if (prepstat != null) {

                prepstat.close();

            }
            if (con != null) {

                con.close();

            }
        }

        return accessCount;

    }

    public static String viewLastRequestedKeyword(String msisdn, String accountId, String siteId) throws
            Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        String lastKeyword = "##BLANK##";

        //------log Statement
        System.out.println(new java.util.Date() + ":@com.rancard.mobility.infoserver.common.services.UserServiceDB...");
        System.out.println(new java.util.Date() + ": retrieving last requested keyword for (" + msisdn + ", " + accountId + ", " + siteId + ")...");

        try {
            con = DConnect.getConnection();

            SQL = "select * from subscriber_request_history where msisdn = ? and account_id = ? and site_id = ? order by log_time desc limit 1";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, msisdn);
            prepstat.setString(2, accountId);
            prepstat.setString(3, siteId);

            rs = prepstat.executeQuery();

            while (rs.next()) {
                lastKeyword = rs.getString("keyword");

                //log statement: info
                System.out.println(new java.util.Date() + ": last requested keyword (" + msisdn + ", " + accountId + ", " + siteId + ") found!");
            }
            //log
            if (lastKeyword.equals("")) {
                System.out.println(new java.util.Date() + ": last requested keyword (" + msisdn + ", " + accountId + ", " + siteId + ") not found!");
            }

        } catch (Exception ex) {

            //error log
            System.out.println(new java.util.Date() + ": error retrieving last requested keyword (" + msisdn + ", " + accountId + ", " + siteId + "): " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (prepstat != null) {

                prepstat.close();

            }
            if (con != null) {
                con.close();
            }
        }

        return lastKeyword;

    }

    //This checks for the most recently subscribed keyword with respect to a giving msisdn
    public static String viewLastSubscribedKeyword(String msisdn, String accountId) throws
            Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        String lastKeyword = "##BLANK##";

        //------log Statement
        System.out.println(new java.util.Date() + ":@com.rancard.mobility.infoserver.common.services.UserServiceDB...");
        System.out.println(new java.util.Date() + ": retrieving last subscribed keyword for (" + msisdn + ", " + accountId + ")...");

        try {
            con = DConnect.getConnection();

            SQL = "select * from service_subscription where msisdn = ? and account_id = ? order by subscription_date desc limit 1";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, msisdn);
            prepstat.setString(2, accountId);

            rs = prepstat.executeQuery();

            while (rs.next()) {
                lastKeyword = rs.getString("keyword");

                //log statement: info
                System.out.println(new java.util.Date() + ": last subscribed keyword (" + msisdn + ", " + accountId + ") found!");
            }
            //log
            if (lastKeyword.equals("")) {
                System.out.println(new java.util.Date() + ": last subscribed keyword (" + msisdn + ", " + accountId + ") not found!");
            }

        } catch (Exception ex) {
            //error log
            System.out.println(new java.util.Date() + ": error retrieving last requested keyword (" + msisdn + ", " + accountId + "): " + ex.getMessage());
            throw new Exception();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (prepstat != null) {
                prepstat.close();
            }
            if (con != null) {
                con.close();
            }
        }

        return lastKeyword;

    }

    // Takes in a keyword alias and an accountId and returns the exact keyword for the 
    // specified alias
    public static UserService viewServiceByAlias(String alias, String accountId) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        UserService service = new UserService();

        //------log Statement
        System.out.println(new java.util.Date() + ":@com.rancard.mobility.infoserver.common.services.UserServiceDB...");
        System.out.println(new java.util.Date() + ": viewing service_definition for alias (" + alias + ", " + accountId + ")...");

        try {
            con = DConnect.getConnection();

            SQL = "select * from keyword_aliases ka inner join service_definition sd on sd.keyword = ka.keyword and sd.account_id = ka.account_id where ka.key_alias = ? and ka.account_id = ?";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, alias);
            prepstat.setString(2, accountId);

            rs = prepstat.executeQuery();

            while (rs.next()) {
                service.setKeyword(rs.getString("keyword"));
                service.setServiceType(rs.getString("service_type"));
                service.setAccountId(rs.getString("account_id"));
                service.setServiceName(rs.getString("service_name"));
                service.setDefaultMessage(rs.getString("default_message"));
                java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd-MMM-yyyy HH.mm.ss");
                String publishTime = df.format(new java.util.Date(rs.getTimestamp("last_updated").getTime()));
                service.setLastUpdated(publishTime);
                service.setCommand(rs.getString("command"));
                service.setAllowedShortcodes(rs.getString("allowed_shortcodes"));
                service.setAllowedSiteTypes(rs.getString("allowed_site_types"));
                //service.setAllowedNetworks (rs.getString ("allowed_networks"));
                service.setPricing(rs.getString("pricing"));
                service.setServiceResponseSender(rs.getString("service_response_sender"));
                if (rs.getInt("is_basic") == 1) {
                    service.setIsBasic(true);
                } else {
                    service.setIsBasic(false);
                }
                if (rs.getInt("is_subscription") == 1 || rs.getString("is_subscription").equals("true")) {
                    service.setIsSubscription(true);
                } else {
                    service.setIsSubscription(false);
                }

                //log statement: info
                System.out.println(new java.util.Date() + ": service for alias (" + alias + ", " + accountId + ") found!");
            }
            //log
            if (service.getKeyword() == null || "".equals(service.getKeyword())) {
                System.out.println(new java.util.Date() + ": service for alias (" + alias + ", " + accountId + ") not found!");
            }

        } catch (Exception ex) {


            //error log
            System.out.println(new java.util.Date() + ": error viewing service for alias (" + alias + ", " + accountId + "): " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (prepstat != null) {
                prepstat.close();
            }
            if (con != null) {
                con.close();
            }
        }

        return service;
    }

    public static UserService viewService(String keyword, String accountId) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        UserService service = new UserService();

        //------log Statement
        System.out.println(new java.util.Date() + ":@com.rancard.mobility.infoserver.common.services.UserServiceDB...");
        System.out.println(new java.util.Date() + ": viewing service_definition for (" + keyword + ", " + accountId + ")...");

        try {
            con = DConnect.getConnection();

            SQL = "select * from service_definition where keyword = ? and account_id = ? ";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, keyword);
            prepstat.setString(2, accountId);

            rs = prepstat.executeQuery();

            while (rs.next()) {
                service.setKeyword(rs.getString("keyword"));
                service.setServiceType(rs.getString("service_type"));
                service.setAccountId(rs.getString("account_id"));
                service.setServiceName(rs.getString("service_name"));
                service.setDefaultMessage(rs.getString("default_message"));
                java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd-MMM-yyyy HH.mm.ss");
                String publishTime = df.format(new java.util.Date(rs.getTimestamp("last_updated").getTime()));
                service.setLastUpdated(publishTime);
                service.setCommand(rs.getString("command"));
                service.setAllowedShortcodes(rs.getString("allowed_shortcodes"));
                service.setAllowedSiteTypes(rs.getString("allowed_site_types"));
                //service.setAllowedNetworks (rs.getString ("allowed_networks"));
                service.setPricing(rs.getString("pricing"));
                service.setServiceResponseSender(rs.getString("service_response_sender"));
                if (rs.getInt("is_basic") == 1) {
                    service.setIsBasic(true);
                } else {
                    service.setIsBasic(false);
                }
                if (rs.getInt("is_subscription") == 1 || rs.getString("is_subscription").equals("true")) {
                    service.setIsSubscription(true);
                } else {
                    service.setIsSubscription(false);
                }

                //log statement: info
                System.out.println(new java.util.Date() + ": service (" + keyword + ", " + accountId + ") found!");
            }
            //log
            if (service.getKeyword() == null || "".equals(service.getKeyword())) {
                System.out.println(new java.util.Date() + ": service (" + keyword + ", " + accountId + ") not found!");
            }

        } catch (Exception ex) {

            //error log
            System.out.println(new java.util.Date() + ": error viewing service (" + keyword + ", " + accountId + "): " + ex.getMessage());
            throw new Exception();

        } finally {
            if (rs != null) {
                rs.close();
            }
            if (prepstat != null) {
                prepstat.close();
            }
            if (con != null) {
                con.close();
            }
        }

        return service;
    }

    public static ArrayList viewAllServices(String accountId) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        java.util.ArrayList serviceList = new java.util.ArrayList();
        try {
            con = DConnect.getConnection();
            SQL = "select * from service_definition where account_id =?";
            prepstat = con.prepareStatement(SQL);
            prepstat.setString(1, accountId);
            rs = prepstat.executeQuery();

            while (rs.next()) {
                UserService service = new UserService();

                service.setKeyword(rs.getString("keyword"));
                service.setServiceType(rs.getString("service_type"));
                service.setAccountId(rs.getString("account_id"));
                service.setServiceName(rs.getString("service_name"));
                service.setDefaultMessage(rs.getString("default_message"));
                java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd-MMM-yyyy HH.mm.ss");
                String publishTime = df.format(new java.util.Date(rs.getTimestamp("last_updated").getTime()));
                service.setLastUpdated(publishTime);
                service.setCommand(rs.getString("command"));
                service.setAllowedShortcodes(rs.getString("allowed_shortcodes"));
                service.setAllowedSiteTypes(rs.getString("allowed_site_types"));
                //service.setAllowedNetworks (rs.getString ("allowed_networks"));
                service.setPricing(rs.getString("pricing"));
                service.setServiceResponseSender(rs.getString("service_response_sender"));
                if (rs.getInt("is_basic") == 1) {
                    service.setIsBasic(true);
                } else {
                    service.setIsBasic(false);
                }
                if (rs.getInt("is_subscription") == 1 || rs.getString("is_subscription").equals("true")) {
                    service.setIsSubscription(true);
                } else {
                    service.setIsSubscription(false);
                }

                serviceList.add(service);
            }

        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (prepstat != null) {
                prepstat.close();
            }
            if (con != null) {
                con.close();
            }
        }

        return serviceList;
    }

    public static ArrayList viewAllServices(String accountId, String serviceType) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        java.util.ArrayList serviceList = new java.util.ArrayList();
        try {
            con = DConnect.getConnection();
            SQL = "select * from service_definition where account_id =? and service_type=?";
            prepstat = con.prepareStatement(SQL);
            prepstat.setString(1, accountId);
            prepstat.setString(2, serviceType);

            rs = prepstat.executeQuery();

            while (rs.next()) {
                UserService service = new UserService();

                service.setKeyword(rs.getString("keyword"));
                service.setServiceType(rs.getString("service_type"));
                service.setAccountId(rs.getString("account_id"));
                service.setServiceName(rs.getString("service_name"));
                service.setDefaultMessage(rs.getString("default_message"));
                java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd-MMM-yyyy HH.mm.ss");
                String publishTime = df.format(new java.util.Date(rs.getTimestamp("last_updated").getTime()));
                service.setLastUpdated(publishTime);
                service.setCommand(rs.getString("command"));
                service.setAllowedShortcodes(rs.getString("allowed_shortcodes"));
                service.setAllowedSiteTypes(rs.getString("allowed_site_types"));
                //service.setAllowedNetworks (rs.getString ("allowed_networks"));
                service.setPricing(rs.getString("pricing"));
                service.setServiceResponseSender(rs.getString("service_response_sender"));
                if (rs.getInt("is_basic") == 1) {
                    service.setIsBasic(true);
                } else {
                    service.setIsBasic(false);
                }
                if (rs.getInt("is_subscription") == 1 || rs.getString("is_subscription").equals("true")) {
                    service.setIsSubscription(true);
                } else {
                    service.setIsSubscription(false);
                }

                serviceList.add(service);
            }

        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (prepstat != null) {
                prepstat.close();
            }
            if (con != null) {
                con.close();
            }
        }

        return serviceList;
    }

    public static ArrayList<UserService> viewAllServices(String accountId, String serviceType, String command) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        java.util.ArrayList serviceList = new java.util.ArrayList();
        try {
            con = DConnect.getConnection();
            SQL = "select * from service_definition where account_id =? and service_type=? and command=?";
            prepstat = con.prepareStatement(SQL);
            prepstat.setString(1, accountId);
            prepstat.setString(2, serviceType);
            prepstat.setString(3, command);


            rs = prepstat.executeQuery();

            while (rs.next()) {
                UserService service = new UserService();

                service.setKeyword(rs.getString("keyword"));
                service.setServiceType(rs.getString("service_type"));
                service.setAccountId(rs.getString("account_id"));
                service.setServiceName(rs.getString("service_name"));
                service.setDefaultMessage(rs.getString("default_message"));
                java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd-MMM-yyyy HH.mm.ss");
                String publishTime = df.format(new java.util.Date(rs.getTimestamp("last_updated").getTime()));
                service.setLastUpdated(publishTime);
                service.setCommand(rs.getString("command"));
                service.setAllowedShortcodes(rs.getString("allowed_shortcodes"));
                service.setAllowedSiteTypes(rs.getString("allowed_site_types"));
                //service.setAllowedNetworks (rs.getString ("allowed_networks"));
                service.setPricing(rs.getString("pricing"));
                service.setServiceResponseSender(rs.getString("service_response_sender"));
                if (rs.getInt("is_basic") == 1) {
                    service.setIsBasic(true);
                } else {
                    service.setIsBasic(false);
                }
                if (rs.getInt("is_subscription") == 1 || rs.getString("is_subscription").equals("true")) {
                    service.setIsSubscription(true);
                } else {
                    service.setIsSubscription(false);
                }

                serviceList.add(service);
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

        return serviceList;
    }

    public static ArrayList viewAllServicesOfParentType(String accountId, String parentType) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        java.util.ArrayList serviceList = new java.util.ArrayList();
        try {
            con = DConnect.getConnection();
            SQL = "select * from service_definition sd, service_route sr where account_id =?"
                    + " and sd.service_type=sr.service_type and sr.parent_service_type=? order by sd.service_name;";
            prepstat = con.prepareStatement(SQL);
            prepstat.setString(1, accountId);
            prepstat.setString(2, parentType);

            rs = prepstat.executeQuery();

            while (rs.next()) {
                UserService service = new UserService();

                service.setKeyword(rs.getString("keyword"));
                service.setServiceType(rs.getString("service_type"));
                service.setAccountId(rs.getString("account_id"));
                service.setServiceName(rs.getString("service_name"));
                service.setDefaultMessage(rs.getString("default_message"));
                java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd-MMM-yyyy HH.mm.ss");
                String publishTime = df.format(new java.util.Date(rs.getTimestamp("last_updated").getTime()));
                service.setLastUpdated(publishTime);
                service.setCommand(rs.getString("command"));
                service.setAllowedShortcodes(rs.getString("allowed_shortcodes"));
                service.setAllowedSiteTypes(rs.getString("allowed_site_types"));
                //service.setAllowedNetworks (rs.getString ("allowed_networks"));
                service.setPricing(rs.getString("pricing"));
                service.setServiceResponseSender(rs.getString("service_response_sender"));
                if (rs.getInt("is_basic") == 1) {
                    service.setIsBasic(true);
                } else {
                    service.setIsBasic(false);
                }
                if (rs.getInt("is_subscription") == 1 || rs.getString("is_subscription").equals("true")) {
                    service.setIsSubscription(true);
                } else {
                    service.setIsSubscription(false);
                }

                serviceList.add(service);
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

        return serviceList;
    }

    public static ArrayList viewAllServicesForType(String serviceType) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        java.util.ArrayList serviceList = new java.util.ArrayList();
        try {
            con = DConnect.getConnection();
            SQL = "select * from service_definition where service_type=? order by account_id";
            prepstat = con.prepareStatement(SQL);
            prepstat.setString(1, serviceType);

            rs = prepstat.executeQuery();

            while (rs.next()) {
                UserService service = new UserService();

                service.setKeyword(rs.getString("keyword"));
                service.setServiceType(rs.getString("service_type"));
                service.setAccountId(rs.getString("account_id"));
                service.setServiceName(rs.getString("service_name"));
                service.setDefaultMessage(rs.getString("default_message"));
                service.setCommand(rs.getString("command"));
                service.setAllowedShortcodes(rs.getString("allowed_shortcodes"));
                service.setAllowedSiteTypes(rs.getString("allowed_site_types"));
                //service.setAllowedNetworks (rs.getString ("allowed_networks"));
                service.setPricing(rs.getString("pricing"));
                service.setServiceResponseSender(rs.getString("service_response_sender"));
                if (rs.getInt("is_basic") == 1) {
                    service.setIsBasic(true);
                } else {
                    service.setIsBasic(false);
                }
                if (rs.getInt("is_subscription") == 1 || rs.getString("is_subscription").equals("true")) {
                    service.setIsSubscription(true);
                } else {
                    service.setIsSubscription(false);
                }

                serviceList.add(service);
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

        return serviceList;
    }

    public static HashMap populateRoutingTable() throws Exception {
        HashMap routingTable = new HashMap();

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        //------log Statement
        System.out.println(new java.util.Date() + ":@com.rancard.mobility.infoserver.common.services.UserServiceDB!");
        System.out.println(new java.util.Date() + ": populating routing table (HashMap)...");

        java.util.ArrayList serviceList = new java.util.ArrayList();
        try {
            con = DConnect.getConnection();
            SQL = "select service_type, service_url from service_route";
            prepstat = con.prepareStatement(SQL);

            rs = prepstat.executeQuery();

            while (rs.next()) {
                routingTable.put(rs.getString("service_type"),
                        rs.getString("service_url"));

                //log details
                System.out.println(new java.util.Date() + ": record added to routing table (HashMap): service_type="
                        + rs.getString("service_type") + ",  service_url=" + rs.getString("service_url"));

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

        return routingTable;
    }

    public static HashMap getServiceTable() throws Exception {
        HashMap serviceTable = new HashMap();

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        java.util.ArrayList serviceList = new java.util.ArrayList();
        try {
            con = DConnect.getConnection();
            SQL = "select service_type, service_name from service_route";
            prepstat = con.prepareStatement(SQL);

            rs = prepstat.executeQuery();

            while (rs.next()) {
                serviceTable.put(rs.getString("service_type"),
                        rs.getString("service_name"));
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

        return serviceTable;
    }

    public static String[][] getCPsForTypes() throws Exception {
        String[][] struct = null;
        String query =
                "SELECT distinct service_definition.service_type, service_definition."
                + "account_id, cp_user.name, cp_user.logo_url FROM service_definition, cp_user where "
                + "cp_user.id=service_definition.account_id order by service_definition.service_type";

        String typeId, account_id, name, logo_url = new String();

        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            prepstat = con.prepareStatement(query);
            rs = prepstat.executeQuery();

            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
            }
            struct = new String[rowCount][4];
            rs.beforeFirst();
            rowCount = 0;

            while (rs.next()) {
                typeId = rs.getString("service_type");
                account_id = rs.getString("account_id");
                name = rs.getString("name");
                logo_url = rs.getString("logo_url");
                struct[rowCount][0] = typeId;
                struct[rowCount][1] = account_id;
                struct[rowCount][2] = name;
                struct[rowCount][3] = logo_url;
                rowCount++;
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }

        return struct;
    }

    public static String[] getCPIDsForServiceType(String serviceType) throws Exception {
        String[] struct = null;
        String query = "SELECT distinct(account_id) from service_definition where service_type='" + serviceType + "'";

        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            prepstat = con.prepareStatement(query);
            rs = prepstat.executeQuery();

            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
            }
            struct = new String[rowCount];
            rs.beforeFirst();
            rowCount = 0;

            while (rs.next()) {
                struct[rowCount] = rs.getString("account_id");
                rowCount++;
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }

        return struct;
    }

    public static HashMap<String, String> getCPIDsForServiceType(String serviceType, String command) throws Exception {

        HashMap struct = new HashMap();
        String accountId = "";
        String keyword = "";
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        String query = "SELECT account_id, keyword from service_definition where service_type='" + serviceType + "' "
                + "and command='" + command + "'";
        //log statement
        System.out.println(new java.util.Date() + ":Looking for CPIds for serviceType-command:" + serviceType + ":" + command);

        try {
            con = DConnect.getConnection();
            prepstat = con.prepareStatement(query);
            rs = prepstat.executeQuery();

            //pupulate hashmap
            int count = 0;
            while (rs.next()) {
                accountId = rs.getString("account_id");
                keyword = rs.getString("keyword");

                struct.put(accountId, keyword);

                count++;
            }
            System.out.println(new java.util.Date() + ":No. of CPs found:" + count);
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            System.out.println(new java.util.Date() + ":error looking for CPIds:" + ex.getMessage());

            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }

        return struct;
    }

    public static void updateSubscriberRequestHistory(String msisdn, String keyword, String accountID, String site_id, Date now, int count) throws Exception {
        Connection conn = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sql = "UPDATE subscriber_request_history SET count = " + count + " where keyword = '" + keyword + "' and account_id = '" + accountID
                + "' and msisdn = '" + msisdn + "' and site_id = '" + site_id + "'";
        try {


            conn = DConnect.getConnection();

            System.out.println("Attempt to update subscriber_request_history: " + sql);
            int update = conn.createStatement().executeUpdate(sql);
            if (update == 0) {
                sql = "INSERT INTO subscriber_request_history values ('" + msisdn + "', '" + keyword + "', '" + accountID + "', '" + site_id + "', '" + df.format(now) + "', 1)";
                System.out.println("Subscription did not exist in subscriber_request_history. Will insert subscription: " + sql);
                conn.createStatement().execute(sql);
            } else {
                System.out.println("updated subscriber_request_history");
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

    }

    public static int getCountOnSubscriberRequestHistory(String msisdn, String keyword, String accountID, String site_id) throws Exception {
        Connection conn = null;
        ResultSet rs = null;
        String sql = "SELECT count FROM subscriber_request_history where keyword = '" + keyword + "' and account_id = '" + accountID
                + "' and msisdn = '" + msisdn + "' and site_id = '" + site_id + "'";
        try {


            conn = DConnect.getConnection();


            System.out.println("Getting subscriber request count: " + sql);
            rs = conn.createStatement().executeQuery(sql);
            if (rs.first()) {
                return rs.getInt(1);
            } else {
                return 0;

            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static boolean stoppedB4SubscriptionEnded(String msisdn, String keyword, String accountID, Date now) throws Exception {
        Connection conn = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ResultSet rs = null;
        String sql = "SELECT * FROM service_subscription_deleted where keyword = '" + keyword + "' and account_id = '" + accountID
                + "' and msisdn = '" + msisdn + "' and subscription_date < '" + df.format(now) + "' and next_subscription_date > '" + df.format(now)
                + "' order by unsubscription_date DESC limit 1";
        System.out.println("Check if user unsubscribed recently: " + sql);
        try {

            conn = DConnect.getConnection();

            rs = conn.createStatement().executeQuery(sql);
            if (rs != null) {
                if (rs.first()) {
                    System.out.println("Yes they have unsubscribed recently");
                    return true;

                } else {
                    System.out.println("No they have not unsubscribed recently");
                    return false;
                }

            } else {
                System.out.println("Yes they have unsubscribed recently");
                return false;
            }
        } catch (Exception e) {
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

    public static void updateSubscriptionStatus(String msisdn, String keyword, String accountId, int status) throws Exception {
        String SQL;
        int error = 0;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        UserService service = new UserService();
        try {
            con = DConnect.getConnection();

            SQL = "select * from service_definition where keyword='" + keyword + "' and account_id='" + accountId + "'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();

            if (rs.next()) {
                SQL = "select registration_id from address_book where account_id='" + accountId + "' and msisdn='" + msisdn + "'";
                prepstat = con.prepareStatement(SQL);
                rs = prepstat.executeQuery();

                if (rs.next()) {
                    SQL = "update service_subscription set status=" + status + " where keyword='" + keyword + "' and account_id='" + accountId + "' and msisdn='" + msisdn + "'";
                    prepstat = con.prepareStatement(SQL);
                    prepstat.execute();
                } else {
                    error = 2; //subscriber has not subscribed;
                }
            } else {
                error = 1; //service does not exist
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
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                }
                con = null;
            }
        }
        if (error == 1) {
            throw new Exception(Feedback.NO_SUCH_SERVICE);
        }
        if (error == 2) {
            throw new Exception(Feedback.NOT_REGISTERED);
        }
    }

    public static String subscribeToService(String msisdn, String keyword, String accountId) throws Exception {
        String SQL;
        String regId = new String();
        ;
        int error = 0;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        UserService service = new UserService();
        try {
            con = DConnect.getConnection();

            SQL = "select * from service_definition where keyword='" + keyword + "' and account_id='" + accountId + "'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();

            if (rs.next()) {
                SQL = "select * from service_subscription where keyword='" + keyword + "' and account_id='" + accountId + "' and msisdn='" + msisdn + "'";
                prepstat = con.prepareStatement(SQL);
                rs = prepstat.executeQuery();

                if (!rs.next()) {
                    SQL = "Insert into service_subscription (registration_id,subscription_date,msisdn,keyword,account_id) values(?,?,?,?,?)";
                    prepstat = con.prepareStatement(SQL);
                    regId = uidGen.generateNumberID(10);
                    prepstat.setString(1, regId);
                    prepstat.setTimestamp(2, new java.sql.Timestamp(java.util.Calendar.getInstance().getTime().getTime()));
                    prepstat.setString(3, msisdn);
                    prepstat.setString(4, keyword);
                    prepstat.setString(5, accountId);
                    prepstat.execute();
                } else {
                    error = 2; //subscriber has already subscribed;
                }
            } else {
                error = 1; //service does not exist
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
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                }
                con = null;
            }
        }
        if (error == 1) {
            throw new Exception(Feedback.NO_SUCH_SERVICE);
        }
        if (error == 2) {
            throw new Exception(Feedback.ALREADY_REGISTERED);
        }

        return regId;
    }

    public static String[] subscribeToService(String msisdn, ArrayList keywords, String accountId) throws Exception {
        String SQL;
        String[] regId = new String[2];

        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        boolean failedCheck = false;

        try {
            con = DConnect.getConnection();
            SQL = "select * from address_book where account_id='" + accountId + "' and msisdn='" + msisdn + "'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();

            if (!rs.next()) {
                //has not registered. Register him and continue
                SQL = "Insert into address_book (account_id,msisdn,registration_id) values(?,?,?)";
                prepstat = con.prepareStatement(SQL);
                regId[0] = uidGen.generateNumberID(6);
                prepstat.setString(1, accountId);
                prepstat.setString(2, msisdn);
                prepstat.setString(3, regId[0]);
                prepstat.execute();
            } else {
                regId[0] = rs.getString("registration_id");
            }


            for (int i = 0; i < keywords.size(); i++) {
                String keyword = keywords.get(i).toString();
                SQL = "select * from service_definition where keyword='" + keyword + "' and account_id='" + accountId + "'";
                prepstat = con.prepareStatement(SQL);
                rs = prepstat.executeQuery();

                if (rs.next()) {
                    SQL = "select * from service_subscription where keyword='" + keyword + "' and account_id='" + accountId + "' and msisdn='" + msisdn + "'";
                    prepstat = con.prepareStatement(SQL);
                    rs = prepstat.executeQuery();

                    if (!rs.next()) {
                        SQL = "Insert into service_subscription (subscription_date,msisdn,keyword,account_id,status) values(?,?,?,?,?)";
                        prepstat = con.prepareStatement(SQL);
                        prepstat.setTimestamp(1, new java.sql.Timestamp(java.util.Calendar.getInstance().getTime().getTime()));
                        prepstat.setString(2, msisdn);
                        prepstat.setString(3, keyword);
                        prepstat.setString(4, accountId);
                        prepstat.setInt(5, 1);
                        prepstat.execute();
                    }
                } else {
                    failedCheck = true;
                }
            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (prepstat != null) {
                prepstat.close();
            }
            if (con != null) {
                con.close();
            }
        }
        if (failedCheck == true) {
            regId[1] = "One or more services were not found.";
        }
        return regId;
    }

    public static String[] subscribeToService(String msisdn, ArrayList keywords, String accountId, int numOfDays) throws Exception {
        String SQL;
        String[] regId = new String[3];
        int error = 0;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        boolean failedCheck = false;

        Calendar c = Calendar.getInstance();
        c.add(c.DAY_OF_MONTH, numOfDays);
        String nextSubDate = new java.sql.Date(c.getTimeInMillis()).toString();

        try {
            con = DConnect.getConnection();
            SQL = "select * from address_book where account_id='" + accountId + "' and msisdn='" + msisdn + "'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();

            if (!rs.next()) {
                //has not registered. Register him and continue
                SQL = "Insert into address_book (account_id,msisdn,registration_id) values(?,?,?)";
                prepstat = con.prepareStatement(SQL);
                regId[0] = uidGen.generateNumberID(6);
                prepstat.setString(1, accountId);
                prepstat.setString(2, msisdn);
                prepstat.setString(3, regId[0]);
                prepstat.execute();
            } else {
                regId[0] = rs.getString("registration_id");
            }

            String keyword = "";
            String keywordStr = "";
            for (int i = 0; i < keywords.size(); i++) {
                keywordStr = keywordStr + "'" + keywords.get(i).toString() + "',";
            }
            keywordStr = keywordStr.substring(0, keywordStr.lastIndexOf(","));

            SQL = "delete from service_subscription where keyword in (" + keywordStr + ") and account_id='" + accountId + "' and msisdn='" + msisdn + "'";
            prepstat = con.prepareStatement(SQL);
            prepstat.execute();

            for (int i = 0; i < keywords.size(); i++) {
                keyword = keywords.get(i).toString();
                SQL = "select * from service_definition where keyword='" + keyword + "' and account_id='" + accountId + "'";
                prepstat = con.prepareStatement(SQL);
                rs = prepstat.executeQuery();

                if (rs.next()) {
                    SQL = "Insert into service_subscription (subscription_date,msisdn,keyword,account_id,status,next_subscription_date) values(?,?,?,?,?,?)";
                    prepstat = con.prepareStatement(SQL);
                    prepstat.setTimestamp(1, new java.sql.Timestamp(java.util.Calendar.getInstance().getTime().getTime()));
                    prepstat.setString(2, msisdn);
                    prepstat.setString(3, keyword);
                    prepstat.setString(4, accountId);
                    prepstat.setInt(5, 1);
                    prepstat.setString(6, nextSubDate);
                    prepstat.execute();
                    regId[2] = nextSubDate;
                } else {
                    failedCheck = true;
                }
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
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                }
                con = null;
            }
        }
        if (failedCheck == true) {
            regId[1] = "One or more services were not found.";
        }
        return regId;
    }

    public static String[] subscribeToService(String msisdn, ArrayList keywords, String accountId, int numOfDays, int status, int billingType) throws Exception {
        String SQL;
        String[] regId = new String[3];
        int error = 0;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        boolean failedCheck = false;

        Calendar c = Calendar.getInstance();
        c.add(c.DATE, numOfDays);
        java.util.Date dt = c.getTime();
        java.text.SimpleDateFormat sm_formatter1 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nextSubDate = sm_formatter1.format(dt);

        try {
            con = DConnect.getConnection();
            SQL = "select * from address_book where account_id='" + accountId + "' and msisdn='" + msisdn + "'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();

            if (!rs.next()) {
                //has not registered. Register him and continue
                SQL = "Insert into address_book (account_id,msisdn,registration_id) values(?,?,?)";
                prepstat = con.prepareStatement(SQL);
                regId[0] = uidGen.generateNumberID(6);
                prepstat.setString(1, accountId);
                prepstat.setString(2, msisdn);
                prepstat.setString(3, regId[0]);
                prepstat.execute();
            } else {
                regId[0] = rs.getString("registration_id");
            }

            String keyword = "";
            String keywordStr = "";
            for (int i = 0; i < keywords.size(); i++) {
                keywordStr = keywordStr + "'" + keywords.get(i).toString() + "',";
            }
            keywordStr = keywordStr.substring(0, keywordStr.lastIndexOf(","));

            SQL = "delete from service_subscription where keyword in (" + keywordStr + ") and account_id='" + accountId + "' and msisdn='" + msisdn + "'";
            prepstat = con.prepareStatement(SQL);
            prepstat.execute();

            for (int i = 0; i < keywords.size(); i++) {
                keyword = keywords.get(i).toString();
                SQL = "select * from service_definition where keyword='" + keyword + "' and account_id='" + accountId + "'";
                prepstat = con.prepareStatement(SQL);
                rs = prepstat.executeQuery();

                if (rs.next()) {
                    SQL = "Insert into service_subscription (subscription_date,msisdn,keyword,account_id,status,next_subscription_date,billing_type) values(?,?,?,?,?,?,?)";
                    prepstat = con.prepareStatement(SQL);
                    prepstat.setTimestamp(1, new java.sql.Timestamp(java.util.Calendar.getInstance().getTime().getTime()));
                    prepstat.setString(2, msisdn);
                    prepstat.setString(3, keyword);
                    prepstat.setString(4, accountId);
                    prepstat.setInt(5, status);
                    prepstat.setString(6, nextSubDate);
                    prepstat.setInt(7, billingType);
                    prepstat.execute();
                    regId[2] = nextSubDate;
                } else {
                    failedCheck = true;
                }
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
            System.out.println("ERROR : " + ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                }
                con = null;
            }
        }
        if (failedCheck == true) {
            regId[1] = "One or more services were not found.";
        }
        return regId;
    }

    public static void resumeSubscription(String msisdn, ArrayList keywords, String accountId, int status, Date subscriptionDate, Date nextSubscriptionDate) throws Exception {
        String SQL;
        String[] regId = new String[3];
        int error = 0;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        boolean failedCheck = false;

        try {
            con = DConnect.getConnection();

            String keyword = "";
            String keywordStr = "";
            for (int i = 0; i < keywords.size(); i++) {
                keywordStr = keywordStr + "'" + keywords.get(i).toString() + "',";
            }
            keywordStr = keywordStr.substring(0, keywordStr.lastIndexOf(","));

            SQL = "delete from service_subscription where keyword in (" + keywordStr + ") and account_id='" + accountId + "' and msisdn='" + msisdn + "'";
            prepstat = con.prepareStatement(SQL);
            prepstat.execute();

            for (int i = 0; i < keywords.size(); i++) {
                keyword = keywords.get(i).toString();
                SQL = "select * from service_definition where keyword='" + keyword + "' and account_id='" + accountId + "'";
                prepstat = con.prepareStatement(SQL);
                rs = prepstat.executeQuery();

                if (rs.next()) {
                    SQL = "Insert into service_subscription (subscription_date,msisdn,keyword,account_id,status,next_subscription_date) values(?,?,?,?,?,?)";
                    prepstat = con.prepareStatement(SQL);
                    prepstat.setTimestamp(1, new java.sql.Timestamp(subscriptionDate.getTime()));
                    prepstat.setString(2, msisdn);
                    prepstat.setString(3, keyword);
                    prepstat.setString(4, accountId);
                    prepstat.setInt(5, status);
                    prepstat.setTimestamp(6, new java.sql.Timestamp(nextSubscriptionDate.getTime()));
                    prepstat.execute();
                } else {
                    failedCheck = true;
                }
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
            System.out.println("ERROR : " + ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                }
                con = null;
            }
        }
        if (failedCheck == true) {
            regId[1] = "One or more services were not found.";
        }
    }

    public static void unsubscribeToService(String msisdn, String keyword, String accountId) throws Exception {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            if (keyword != null && !keyword.equals("")) {
                SQL = "delete from service_subscription where keyword='" + keyword + "' and account_id='" + accountId + "' and msisdn='" + msisdn + "'";
            } else {
                SQL = "delete from service_subscription where account_id='" + accountId + "' and msisdn='" + msisdn + "'";
            }
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

    public static void forceUnsubscribe(String keyword, String accountId) throws Exception {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            SQL = "delete from service_subscription where keyword='" + keyword + "' and account_id='" + accountId + "'";
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

    public static void forceUnsubscribe(String msisdn, ArrayList keywords, String accountId) throws Exception {
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
            SQL = "delete from service_subscription where msisdn='" + msisdn + "' and keyword in (" + keywordStr + ") and account_id='" + accountId + "'";
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

    public static boolean verifyUser(String msisdn, String regId, String acctId, String keyword) throws Exception {
        boolean isRegistered = false;
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        UserService service = new UserService();
        try {
            con = DConnect.getConnection();
            SQL = "select * from address_book ab inner join service_subscription ss on ab.msisdn=ss.msisdn and ab.account_id=ss.account_id where ab.msisdn='"
                    + msisdn + "' and ab.account_id='" + acctId + "' and ss.keyword='" + keyword + "'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();

            if (rs.next() && rs.getString("ab.registration_id").equals(regId)) {
                isRegistered = true;
            } else {
                isRegistered = false;
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
        return isRegistered;
    }

    public static boolean isRegistered(String msisdn, String accountId) throws Exception {
        boolean isRegistered = false;
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            SQL = "select * from address_book where account_id='" + accountId + "' and msisdn='" + msisdn + "'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();

            if (!rs.next()) {
                isRegistered = false;
            } else {
                isRegistered = true;
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
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                }
                con = null;
            }
            return isRegistered;
        }
    }

    public static boolean isRegistered(String msisdn, String accountId, String keyword) throws Exception {
        boolean isRegistered = false;
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            SQL = "select * from address_book ab inner join service_subscription ss on ab.msisdn=ss.msisdn and ab.account_id=ss.account_id where ab.account_id='" + accountId + "' and ab.msisdn='" + msisdn + "'"
                    + " and ss.keyword='" + keyword + "'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();

            if (!rs.next()) {
                isRegistered = false;
            } else {
                isRegistered = true;
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
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                }
                con = null;
            }
            return isRegistered;
        }
    }

    public static boolean isSubscribed(String msisdn, String accountId, String keyword) throws Exception {
        boolean isSubscribed = false;
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            SQL = "select * from service_subscription where account_id='" + accountId + "' and msisdn='" + msisdn + "'"
                    + " and keyword='" + keyword + "'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();

            if (!rs.next()) {
                isSubscribed = false;
            } else {
                isSubscribed = true;
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
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                }
                con = null;
            }
            return isSubscribed;
        }
    }

    public static HashMap getSubscription(String msisdn, String accountId, String keyword, String alternativeKeyword) throws Exception {
        HashMap subscription = new HashMap();
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            if (alternativeKeyword != null && !alternativeKeyword.trim().equals("")) {
                SQL = "select * from service_subscription where account_id='" + accountId + "' and msisdn='" + msisdn + "'"
                        + " and (keyword='" + keyword + "' or keyword='" + alternativeKeyword + "')";
            } else {
                SQL = "select * from service_subscription where account_id='" + accountId + "' and msisdn='" + msisdn + "'"
                        + " and keyword='" + keyword + "'";
            }
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();

            if (rs.next()) {
                subscription.put("msisdn", rs.getString("msisdn"));
                subscription.put("keyword", rs.getString("keyword"));
                subscription.put("account_id", rs.getString("account_id"));
                subscription.put("subscription_date", rs.getTimestamp("subscription_date"));
                subscription.put("next_subscription_date", rs.getTimestamp("next_subscription_date"));
                subscription.put("status", rs.getInt("status"));
                subscription.put("billing_type", rs.getInt("billing_type"));
            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (prepstat != null) {
                prepstat.close();
            }
            if (con != null) {
                con.close();
            }
            return subscription;
        }
    }

    public static HashMap getRecentUnsubscription(String msisdn, String keyword, String accountID, Date now) throws Exception {
        HashMap subscription = new HashMap();
        Connection conn = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ResultSet rs = null;
        String sql = "SELECT * FROM service_subscription_deleted where keyword = '" + keyword + "' and account_id = '" + accountID
                + "' and msisdn = '" + msisdn + "' and subscription_date < '" + df.format(now) + "' and next_subscription_date > '" + df.format(now)
                + "' order by unsubscription_date DESC limit 1";
        System.out.println("Check if user unsubscribed recently: " + sql);
        try {

            conn = DConnect.getConnection();

            rs = conn.createStatement().executeQuery(sql);
            if (rs.next()) {
                subscription.put("msisdn", rs.getString("msisdn"));
                subscription.put("keyword", rs.getString("keyword"));
                subscription.put("account_id", rs.getString("account_id"));
                subscription.put("subscription_date", rs.getTimestamp("subscription_date"));
                subscription.put("next_subscription_date", rs.getTimestamp("next_subscription_date"));
                subscription.put("status", rs.getInt("status"));
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return subscription;
    }

    public static boolean hasRecentUnsubscription(String msisdn, String keyword, String accountID) throws Exception {
        boolean flag = false;
        Connection conn = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ResultSet rs = null;
        String sql = "SELECT * FROM service_subscription_deleted where keyword = '" + keyword + "' and account_id = '" + accountID
                + "' and msisdn = '" + msisdn + "' order by unsubscription_date DESC limit 1";
        System.out.println("Check if user ever unsubscribed : " + sql);
        try {

            conn = DConnect.getConnection();

            rs = conn.createStatement().executeQuery(sql);
            if (rs.next()) {
                flag = true;
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return flag;
    }

    //deprecated as of Friday August 31, 2007
    public static boolean isMonthlySubscriber(String msisdn, String accountId) throws Exception {
        boolean isRegistered = false;
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            SQL = "select * from address_book ab inner join service_subscription ss on ab.msisdn=ss.msisdn where ab.account_id='" + accountId + "' and ab.msisdn='" + msisdn + "'"
                    + " and ss.keyword in(select keyword from service_definition where account_id='" + accountId + "' and command='2' and is_basic='1')";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();

            if (!rs.next()) {
                isRegistered = false;
            } else {
                isRegistered = true;
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
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                }
                con = null;
            }
            return isRegistered;
        }
    }

    public static ArrayList getKeywordsOfBasicServices(String accountId) throws Exception {
        ArrayList keywords = new ArrayList();
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            SQL = "select keyword from service_definition where is_basic=1 and account_id='" + accountId + "'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();

            while (rs.next()) {
                keywords.add(rs.getString("keyword"));
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
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                }
                con = null;
            }
        }
        return keywords;
    }

    public static ArrayList getKeywordsUserSubscribedTo(String msisdn, String accountId) throws Exception {
        ArrayList keywords = new ArrayList();
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            SQL = "select keyword from service_subscription where msisdn='" + msisdn + "' and account_id='" + accountId + "'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();

            while (rs.next()) {
                keywords.add(rs.getString("keyword"));
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
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                }
                con = null;
            }
        }
        return keywords;
    }

    public static ArrayList getKeywordsUserSubscribedTo(String msisdn, String accountId, ArrayList<String> keywordList) throws Exception {
        ArrayList keywords = new ArrayList();
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            SQL = "select keyword from service_subscription where msisdn='" + msisdn + "' and account_id='" + accountId + "' and keyword in (" + expandKeywordList(keywordList) + ")";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();

            while (rs.next()) {
                keywords.add(rs.getString("keyword"));
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
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                }
                con = null;
            }
        }
        return keywords;
    }

    public static ArrayList getKeywordsOfSubscriptionServices(String accountId) throws Exception {
        ArrayList keywords = new ArrayList();
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            SQL = "select keyword from service_definition where is_subscription=1 and account_id='" + accountId + "'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();

            while (rs.next()) {
                keywords.add(rs.getString("keyword"));
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
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                }
                con = null;
            }
        }
        return keywords;
    }

    public static int[] updateServiceRSSFeeds(String accountId, String keyword, java.util.ArrayList<String> userFeedList) throws Exception {
        boolean updateSucessful = false;
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        Statement stmt = null;
        int[] upCounts = null;
        java.util.Iterator<String> it = userFeedList.iterator();
        String feedIdList = "('";

        while (it.hasNext()) {
            feedIdList = feedIdList + it.next();
            if (it.hasNext()) {
                feedIdList = feedIdList + "','";
            }
        }

        feedIdList = feedIdList + "')";

        try {
            con = DConnect.getConnection();
            con.setAutoCommit(false);
            stmt = con.createStatement();
            SQL = "delete from cp_user_feeds where account_id ='" + accountId + "' and keyword ='" + keyword + "'";
            stmt.addBatch(SQL);

            for (int i = 0; i < userFeedList.size(); i++) {
                String feed = (String) userFeedList.get(i);
                SQL = "insert into cp_user_feeds (account_id, keyword, feed_id) values ('" + accountId + "', '" + keyword + "','" + feed + "')";//insert into 3rd_party_permissions (keyword, account_id, can_view, can_add, can_view_reports,3rd_party_id) values ('"+thrdPartyPem.getKeyword()+"','"+accountId+"',"+(thrdPartyPem.getCanView().booleanValue()?1:0)+","+ (thrdPartyPem.getCanSubmit().booleanValue()?1:0)+","+(thrdPartyPem.getCanEdit().booleanValue()?1:0)+",'"+thirdPartyId+"')";

                stmt.addBatch(SQL);
            }


            upCounts = stmt.executeBatch();
            con.commit();


        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
        } finally {
            if (con != null) {
                con.close();
            }
        }


        return upCounts;
    }

    public static Page viewSubscribers(String csid, String[] keywords, java.sql.Timestamp startdate, java.sql.Timestamp enddate, int start, int count) throws Exception {

        java.util.ArrayList subscribers = new java.util.ArrayList();
        Page page = null;

        int y = 0;
        int i = 0;
        int numResults = 0;
        boolean hasNext = false;

        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();

            query = "select msisdn from service_subscription where account_id='" + csid + "' ";

            if (keywords != null && keywords.length != 0) {
                for (int j = 0; j < keywords.length; j++) {
                    query = query + " and keyword='" + keywords[i] + "'";
                }
            }
            if (startdate != null) {
                query = query + " and subscription_date >='" + startdate + "'";
            }
            if (enddate != null) {
                query = query + " and subscription_date <='" + enddate + "'";
            }
            query = query + "oder by subscription_date desc";

            prepstat = con.prepareStatement(query);
            rs = prepstat.executeQuery();

            // get the total number of records
            rs.last();
            numResults = rs.getRow();
            rs.beforeFirst();

            while (i < (start + count) && rs.next()) {
                if (i == 0) {
                    int x = numResults;
                    y = x / count;
                    if ((x % count) > 0) {
                        y += 1;
                    }
                }
                if (i >= start) {
                    subscribers.add(rs.getString("msisdn"));
                }
                i++;
            }

            hasNext = rs.next();
            page = new Page(subscribers, start, hasNext, y, numResults);

            if (page == null) {
                page = com.rancard.util.Page.EMPTY_PAGE;
            }

        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        return page;
    }

    public static ArrayList viewServiceRSSFeeds(String accountId, String keyword) throws Exception {

        java.util.ArrayList feedsList = new java.util.ArrayList();
        Page page = null;

        int y = 0;
        int i = 0;
        int numResults = 0;
        boolean hasNext = false;

        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();

            query = "select * from cp_user_feeds where account_id='" + accountId + "' and  keyword='" + keyword + "'";

            prepstat = con.prepareStatement(query);
            rs = prepstat.executeQuery();

            // get the total number of records

            while (rs.next()) {
                com.rancard.mobility.infoserver.feeds.CPUserFeeds feed = new com.rancard.mobility.infoserver.feeds.CPUserFeeds();
                feed.setFeedId(rs.getString("feed_id"));
                feed.setCpUserId(rs.getString("account_id"));
                feed.setKeyword(rs.getString("keyword"));
                feedsList.add(feed);
            }


        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        return feedsList;
    }

    public static String[] viewAllSubscribers(String csid, String keyword) throws Exception {

        String[] subscribers = null;

        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();

            query = "select msisdn from service_subscription where account_id='" + csid + "' and keyword='" + keyword + "' order by msisdn desc";

            prepstat = con.prepareStatement(query);
            rs = prepstat.executeQuery();

            rs.last();
            int numResults = rs.getRow();
            subscribers = new String[numResults];
            rs.beforeFirst();

            int i = 0;
            while (rs.next()) {
                subscribers[i] = rs.getString("msisdn");
                i++;
            }

        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        return subscribers;
    }

    public static String[] viewAllSubscribersWithStatus(String csid, String keyword, int status) throws Exception {

        String[] subscribers = null;

        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();

            query = "select msisdn from service_subscription where account_id='" + csid + "' and keyword='" + keyword + "' and status=" + status + " order by msisdn desc";

            prepstat = con.prepareStatement(query);
            rs = prepstat.executeQuery();

            rs.last();
            int numResults = rs.getRow();
            subscribers = new String[numResults];
            rs.beforeFirst();

            int i = 0;
            while (rs.next()) {
                subscribers[i] = rs.getString("msisdn");
                i++;
            }

        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        return subscribers;
    }

    public static HashMap viewSubscribersGroupByNetworkPrefix(String csid, String keyword, int status) throws Exception {

        HashMap groupedSubscribers = new HashMap();
        String value = "";
        try {
            value = com.rancard.util.PropertyHolder.getPropsValue("MAX_SUBS_FOR_PRE_GAME_ALERTS");
            if (value == null || value.equals("")) {
                value = "40000";
            }
        } catch (Exception e) {
            value = "40000";
        }

        String query;
        ResultSet networks = null;
        ResultSet subs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();

            //get available networks for content provider
            query = "select allowed_networks from cp_connections where list_id='" + csid + "'";
            prepstat = con.prepareStatement(query);
            networks = prepstat.executeQuery();

            while (networks.next()) {
                String prefix = new String();
                prefix = networks.getString("allowed_networks");

                //comma delimited list of allowed_networks
                java.util.List prefixList = java.util.Arrays.asList(prefix.split(","));
                String OR, prefixQuery = "";
                //build prefixQuery
                for (int i = 0; i < prefixList.size(); i++) {
                    OR = (i + 1 < prefixList.size()) ? " OR " : "";
                    prefixQuery += "ss.msisdn like '+" + prefixList.get(i) + "%' " + OR;
                }
                //logging
                System.out.println(new java.util.Date() + ":network_prefixQuery:" + prefixQuery);

                //get subscribers from a selected network
                query = "select ab.msisdn from address_book ab inner join service_subscription ss on ab.msisdn=ss.msisdn and ab.account_id=ss.account_id where ab.account_id='" + csid
                        + "' and ss.keyword='" + keyword + "' and ss.status=" + status + " and (" + prefixQuery + ") limit " + value;

                prepstat = con.prepareStatement(query);
                subs = prepstat.executeQuery();

                //create an array of subscribers
                subs.last();
                int numResults = subs.getRow();
                String[] subscribers = new String[numResults];
                subs.beforeFirst();

                //populate array
                int count = 0;
                while (subs.next()) {
                    subscribers[count] = subs.getString("ab.msisdn");
                    count++;
                }

                //insert array into hashmap
                if (count > 0) {
                    System.out.println(new java.util.Date() + ":adding subscribers for network:" + prefix + ", No.Subscribers:" + subscribers.length);
                } else {
                    System.out.println(new java.util.Date() + ":No subscriber found for network:" + prefix);
                }

                groupedSubscribers.put(prefix, subscribers);
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return groupedSubscribers;
    }

    public static HashMap viewSubscribersGroupByNetworkPrefix(String csid, String keyword, String alias, int status) throws Exception {

        HashMap groupedSubscribers = new HashMap();

        String query;
        ResultSet networks = null;
        ResultSet subs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();

            //get available networks for content provider
            query = "select allowed_networks from cp_connections where list_id='" + csid + "'";
            prepstat = con.prepareStatement(query);
            networks = prepstat.executeQuery();

            while (networks.next()) {
                String prefix = new String();
                prefix = networks.getString("allowed_networks");

                //get subscribers from a selected network
                /*query = "select ab.msisdn from address_book ab inner join service_subscription ss on ab.msisdn=ss.msisdn and ab.account_id=ss.account_id where ab.account_id='" + csid +
                 "' and (ss.keyword='" + keyword + "' or ss.keyword='" + alias + "') and ss.status=" + status + " and ss.msisdn like '+" + prefix + "%'";*/
                query = "select ab.msisdn from address_book ab inner join service_subscription ss on ab.msisdn=ss.msisdn and ab.account_id=ss.account_id where ab.account_id='" + csid
                        + "' and ss.keyword='" + keyword + "' and ss.status=" + status + " and ss.msisdn like '+" + prefix + "%'";
                prepstat = con.prepareStatement(query);
                subs = prepstat.executeQuery();

                //create an array of subscribers
                subs.last();
                int numResults = subs.getRow();
                String[] subscribers = new String[numResults];
                subs.beforeFirst();

                //populate array
                int count = 0;
                while (subs.next()) {
                    subscribers[count] = subs.getString("ab.msisdn");
                    count++;
                }

                //insert array into hashmap
                groupedSubscribers.put(prefix, subscribers);
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return groupedSubscribers;
    }

    public static HashMap viewSubscribersGroupByNetworkPrefix(String accountid, String keyword, int start, int count) throws Exception {

        HashMap groupedSubscribers = new HashMap();
        boolean hasNext = false;
        java.util.List logList = new java.util.ArrayList();
        Page ret = null;
        int y = 0;
        int i = 0;
        int numResults = 0;

        String query;
        ResultSet networks = null;
        ResultSet subs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();

            //get available networks for content provider
            query = "select allowed_networks from cp_connections where list_id='" + accountid + "'";
            prepstat = con.prepareStatement(query);
            networks = prepstat.executeQuery();

            while (networks.next()) {
                String prefix = new String();
                prefix = networks.getString("allowed_networks");

                //get subscribers from a selected network
                query = "select ab.msisdn from address_book ab inner join service_subscription ss on ab.msisdn=ss.msisdn and ab.account_id=ss.account_id inner join service_definition sd on "
                        + "ss.account_id=sd.account_id and ss.keyword=sd.keyword where ab.account_id='" + accountid + "' and ss.keyword='" + keyword + "' and ss.msisdn like '+" + prefix + "%'";
                prepstat = con.prepareStatement(query);
                subs = prepstat.executeQuery();

                // get the total number of records
                subs.last();
                numResults = subs.getRow();
                subs.beforeFirst();

                while (i < (start + count) && subs.next()) {
                    if (i == 0) {
                        int x = numResults;
                        y = x / count;
                        if ((x % count) > 0) {
                            y += 1;
                        }
                    }
                    if (i >= start) {
                        String number = subs.getString("ab.msisdn");
                        logList.add(number);
                    }
                    i++;
                }
                hasNext = subs.next();
                ret = new Page(logList, start, hasNext, y, numResults);

                if (ret == null) {
                    ret = com.rancard.util.Page.EMPTY_PAGE;
                }

                //insert array into hashmap
                groupedSubscribers.put(prefix, ret);
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return groupedSubscribers;
    }

    public static ArrayList viewTempSubscribersGroupByNetworkPrefix(String accountid, String keyword, String processId) throws Exception {

        ArrayList groupedSubscribers = new ArrayList();

        String query;
        ResultSet subs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();

            //get subscribers from a selected network
            query = "select ab.msisdn,ss.keyword from address_book ab inner join temp_service_subscription ss on ab.msisdn=ss.msisdn and ab.account_id=ss.account_id where ab.account_id='"
                    + accountid + "' and ss.process_id='" + processId + "' and ss.keyword='" + keyword + "'";
            prepstat = con.prepareStatement(query);
            subs = prepstat.executeQuery();

            //create an array of subscribers
            subs.last();
            int numResults = subs.getRow();
            String[][] subscribers = new String[numResults][2];
            subs.beforeFirst();

            //populate array
            int count = 0;
            while (subs.next()) {
                subscribers[count][0] = subs.getString("ab.msisdn");
                subscribers[count][1] = subs.getString("ss.keyword");
                count++;
            }

            //insert array into arraylist
            groupedSubscribers.add(subscribers);
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return groupedSubscribers;
    }

    /*
     @lastUpdated 31st Aug. 2007
     */
    public static ArrayList viewTempSubscribersGroupByNetworkPrefix(String accountid, String processId) throws Exception {

        //log statement
        System.out.println(new java.util.Date() + ":@com.rancard.mobility.infoserver.common.services.UserServiceDB:viewTempSubscribersGroupByNetworkPrefix");
        System.out.println(new java.util.Date() + ":retrieving MONTHLY_BILLING subscriptions for account " + accountid + " from temp_service_subscription table..");

        ArrayList groupedSubscribers = new ArrayList();

        String query;
        ResultSet subs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();

            //get subscribers from a selected network
            query = "select ab.msisdn,ss.keyword,ss.billing_type from address_book ab inner join temp_service_subscription ss on ab.msisdn=ss.msisdn and ab.account_id=ss.account_id where ab.account_id='"
                    + accountid + "' and ss.process_id='" + processId + "'";
            prepstat = con.prepareStatement(query);
            subs = prepstat.executeQuery();

            //create an array of subscribers
            subs.last();
            int numResults = subs.getRow();
            String[][] subscribers = new String[numResults][3];//update from 2 to 3 for billing_type property: updated 31st Aug 07
            subs.beforeFirst();

            //populate array
            int count = 0;
            while (subs.next()) {
                subscribers[count][0] = subs.getString("ab.msisdn");
                subscribers[count][1] = subs.getString("ss.keyword");
                subscribers[count][2] = "" + subs.getString("ss.billing_type");


                //insert array into arraylist
                groupedSubscribers.add(subscribers);

                count++;
            }
            //log statements
            if (count == 0) {
                System.out.println(new java.util.Date() + ": No MONTHLY_BILLING subscribers for account " + accountid + " due for renewal today.");
            } else {
                System.out.println(new java.util.Date() + ": " + count + " subscribers for " + accountid + " due for renewal today.");
            }
            System.out.println(new java.util.Date() + ": process viewTempSubscribersGroupByNetworkPrefix completed successfully!");

        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            //log statement
            System.out.println(new java.util.Date() + ": error in viewTempSubscribersGroupByNetworkPrefix:" + ex.getMessage());

            throw new Exception(ex.getMessage());
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return groupedSubscribers;
    }

    public static HashMap<String, String[]> viewTempSubscribersGroupByNetworkPrefix(String accountId, int status, java.util.Date today) throws Exception {

        HashMap groupedSubscribers = new HashMap();
        String value = "";

        String nextSubscriptionDate = new java.sql.Date(today.getTime()).toString();

        String query;
        ResultSet networks = null;
        ResultSet subs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();

            //get available networks for content provider
            query = "select allowed_networks from cp_connections where list_id='" + accountId + "'";
            prepstat = con.prepareStatement(query);
            networks = prepstat.executeQuery();

            while (networks.next()) {
                String prefix = new String();
                prefix = networks.getString("allowed_networks");

                //comma delimited list of allowed_networks
                java.util.List prefixList = java.util.Arrays.asList(prefix.split(","));
                String OR, prefixQuery = "";
                //build prefixQuery
                for (int i = 0; i < prefixList.size(); i++) {
                    OR = (i + 1 < prefixList.size()) ? " OR " : "";
                    prefixQuery += "msisdn like '+" + prefixList.get(i) + "%' " + OR;
                }
                //logging
                System.out.println(new java.util.Date() + ":network_prefixQuery:" + prefixQuery);

                //get count subscribers from a selected network
                query = "select count(distinct(msisdn)) as num_subs from service_subscription where keyword in(select d.keyword from service_definition d where d.is_basic=1 and d.account_id='" + accountId
                        + "' and d.service_type=15 and d.command=2) and account_id='" + accountId + "' and next_subscription_date='" + nextSubscriptionDate + "'"
                        + " and billing_type='" + ServiceSubscriber.MONTHLY_BILLING + "' and status='" + status + "' and (" + prefixQuery + ")";


                prepstat = con.prepareStatement(query);
                subs = prepstat.executeQuery();

                int numSubs = 0;
                while (subs.next()) {
                    numSubs = subs.getInt("num_subs");
                }
                prepstat = null;
                subs = null;

                //get subscribers from a selected network
                query = "select distinct(msisdn) from service_subscription where keyword in(select d.keyword from service_definition d where d.is_basic=1 and d.account_id='" + accountId
                        + "' and d.service_type=15 and d.command=2) and account_id='" + accountId + "' and next_subscription_date='" + nextSubscriptionDate + "'"
                        + " and billing_type='" + ServiceSubscriber.MONTHLY_BILLING + "' and status='" + status + "' and (" + prefixQuery + ")";


                prepstat = con.prepareStatement(query);
                subs = prepstat.executeQuery();


                //create an arrayList of subscribers
                String[] subscribers = new String[numSubs];


                //populate arrayList
                int count = 0;
                while (subs.next()) {
                    subscribers[count] = subs.getString("msisdn");
                    count++;
                }

                //insert arrayList into hashmap
                if (subscribers.length > 0) {
                    System.out.println(new java.util.Date() + ":adding subscribers for network:" + prefix + ", No.Subscribers:" + subscribers.length);
                } else {
                    System.out.println(new java.util.Date() + ":No subscriber found for network:" + prefix);
                }

                groupedSubscribers.put(prefix, subscribers);

                //free memory
                prefix = null;
                subscribers = null;
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        } finally {
            if (con != null) {
                con.close();
            }

        }
        return groupedSubscribers;
    }

    public static String findKeywordForMapping(String mapping, String accountId) throws Exception {
        String SQL;
        String keyword = new String();
        ;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        UserService service = new UserService();
        try {
            con = DConnect.getConnection();

            SQL = "select * from keyword_mapping where mapping='" + mapping + "' and account_id='" + accountId + "'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();

            if (rs.next()) {
                keyword = rs.getString("keyword");
            } else {
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
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                }
                con = null;
            }
        }

        return keyword;
    }

    public static void deleteTempSubscriptionRecord(String msisdn, String accountId, String processId) throws Exception {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            SQL = "delete from temp_service_subscription where msisdn='" + msisdn + "' and account_id='" + accountId + "' and process_id='" + processId + "'";
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
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                }
                con = null;
            }
        }
    }

    private static String expandKeywordList(ArrayList<String> keywordList) {
        StringBuffer keyListBuffer = new StringBuffer();
        Iterator<String> keywordIterator = keywordList.iterator();
        while (keywordIterator.hasNext()) {
            keyListBuffer.append("'" + keywordIterator.next() + "'");
            if (keywordIterator.hasNext()) {
                keyListBuffer.append(",");
            }
        }
        return keyListBuffer.toString();
    }

    public static String viewNextSubscriptionDate(String msisdn, String accountId, String keyword) throws
            Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        String nextSubsDate = null;

        //------log Statement
        System.out.println(new java.util.Date() + ":@com.rancard.mobility.infoserver.common.services.UserServiceDB...");
        System.out.println(new java.util.Date() + ": retrieving next subscription date for (" + msisdn + ", " + accountId + ", " + keyword + ")...");

        try {
            con = DConnect.getConnection();

            SQL = "select next_subscription_date from service_subscription where msisdn = ? and account_id = ? and keyword = ? ";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, msisdn);
            prepstat.setString(2, accountId);
            prepstat.setString(3, keyword);

            rs = prepstat.executeQuery();

            while (rs.next()) {
                nextSubsDate = rs.getString("next_subscription_date");

                //log statement: info
                System.out.println(new java.util.Date() + ": next subscription date (" + msisdn + ", " + accountId + ", " + keyword + ") found!");
            }
            //log
            if (nextSubsDate == null) {
                System.out.println(new java.util.Date() + ": next subscription date (" + msisdn + ", " + accountId + ", " + keyword + ") not found!");
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
            System.out.println(new java.util.Date() + ": error retrieving last requested keyword (" + msisdn + ", " + accountId + ", " + keyword + "): " + ex.getMessage());

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

        return nextSubsDate;

    }

    public static void createFeed(com.rancard.mobility.infoserver.feeds.Feed feedItem) {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            SQL = "insert into feeds (feed_id, feed_name, feed_url, is_active, username, password) "
                    + "values(?, ?, ?, ?, ?, ?)";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, feedItem.getFeedId());
            prepstat.setString(2, feedItem.getFeedName());
            prepstat.setString(3, feedItem.getFeedURL().toString());
            prepstat.setInt(4, (feedItem.isActive()) ? 1 : 0);
            prepstat.setString(5, feedItem.getUsername());
            prepstat.setString(6, feedItem.getPassword());

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

    public static com.rancard.mobility.infoserver.feeds.Feed viewFeed(String feedId) throws Exception {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        com.rancard.mobility.infoserver.feeds.Feed feed = new com.rancard.mobility.infoserver.feeds.Feed();

        try {
            con = DConnect.getConnection();

            SQL = "select * from feeds where feed_id = ?";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, feedId);

            rs = prepstat.executeQuery();

            while (rs.next()) {
                feed.setFeedId(rs.getString("feed_id"));
                feed.setFeedName(rs.getString("feed_name"));
                feed.setFeedURL(rs.getURL("feed_url"));
                feed.setActive((rs.getInt("is_active") == 1) ? true : false);
                feed.setUsername(rs.getString("username"));
                feed.setPassword(rs.getString("password"));
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

        return feed;

    }

    public static void deleteFeed(String feedId) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();

            SQL = "delete from feeds where feed_id = ?";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, feedId);

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

    public static void deleteFeed(ArrayList feedIds) {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        String keywordStr = "";
        for (int i = 0; i < feedIds.size(); i++) {
            keywordStr = keywordStr + "'" + feedIds.get(i).toString() + "',";
        }
        keywordStr = keywordStr.substring(0, keywordStr.lastIndexOf(","));

        try {
            con = DConnect.getConnection();
            SQL = "delete from feeds where feed_id in (" + keywordStr + ")";
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

    public static void updateFeed(String update_feed_id, com.rancard.mobility.infoserver.feeds.Feed feedItem) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        //Check and set update parameters
        String feed_id = (feedItem.getFeedId() != null && !feedItem.getFeedId().equals("")) ? "'" + feedItem.getFeedId() + "'" : "feed_id";
        String feed_name = (feedItem.getFeedName() != null && !feedItem.getFeedName().equals("")) ? "'" + feedItem.getFeedName() + "'" : "feed_name";
        String feed_url = (feedItem.getFeedURL() != null && !feedItem.getFeedURL().equals("")) ? "'" + feedItem.getFeedURL() + "'" : "feed_url";
        String is_active = (feedItem.isActive()) ? "1" : "0";
        String username = (feedItem.getUsername() != null && !feedItem.getUsername().equals("")) ? "'" + feedItem.getUsername() + "'" : "username";
        String password = (feedItem.getPassword() != null && !feedItem.getPassword().equals("")) ? "'" + feedItem.getPassword() + "'" : "password";

        try {
            con = DConnect.getConnection();
            SQL = "UPDATE feeds SET feed_id = " + feed_id + ", feed_name = " + feed_name + ", feed_url = " + feed_url + ","
                    + "is_active = " + is_active + ", username = " + username + ", password = " + password
                    + " WHERE feed_id = ?";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, update_feed_id);

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

    public static void createCPUserFeedAssociation(com.rancard.mobility.infoserver.feeds.CPUserFeeds cpUserFeed) {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            SQL = "insert into cp_user_feeds (account_id, keyword, feed_id, allowed_age, regex_reject, msg_dlr_priority) "
                    + "values(?, ?, ?, ?, ?, ?)";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, cpUserFeed.getCpUserId());
            prepstat.setString(2, cpUserFeed.getKeyword());
            prepstat.setString(3, cpUserFeed.getFeedId());
            prepstat.setInt(4, cpUserFeed.getAllowedAge());
            prepstat.setString(5, cpUserFeed.getRegexReject());
            prepstat.setInt(6, cpUserFeed.getMsgDlrPrority());

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

    public static com.rancard.mobility.infoserver.feeds.CPUserFeeds viewCPUserFeed(String account_id, String keyword) throws Exception {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        com.rancard.mobility.infoserver.feeds.CPUserFeeds cpUserFeed = new com.rancard.mobility.infoserver.feeds.CPUserFeeds();

        try {
            con = DConnect.getConnection();

            SQL = "select * from cp_user_feeds where account_id = ? and keyword = ? ";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, account_id);
            prepstat.setString(2, keyword);

            rs = prepstat.executeQuery();

            while (rs.next()) {
                cpUserFeed.setCpUserId(rs.getString("account_id"));
                cpUserFeed.setKeyword(rs.getString("keyword"));
                cpUserFeed.setFeedId(rs.getString("feed_id"));
                cpUserFeed.setAllowedAge(rs.getInt("allowed_age"));
                cpUserFeed.setRegexReject(rs.getString("regex_reject"));
                cpUserFeed.setMsgDlrPrority(rs.getInt("msg_dlr_priority"));
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

        return cpUserFeed;

    }

    public static void deleteCPUserFeed(String account_id, String keyword) throws Exception {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();

            SQL = "delete from cp_user_feeds where account_id = ? and keyword = ?";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, account_id);
            prepstat.setString(2, keyword);

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

    public static void deleteCPUserFeed(String accountId, java.util.ArrayList keywords) throws Exception {

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
            SQL = "delete from cp_user_feeds where keyword in (" + keywordStr + ") and account_id='" + accountId + "'";
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

    public static void updateCPUserFeed(String update_account_id, String update_keyword, com.rancard.mobility.infoserver.feeds.CPUserFeeds cpUserFeed) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        //Check and set update parameters
        String account_id = (cpUserFeed.getCpUserId() != null && !cpUserFeed.getCpUserId().equals("")) ? "'" + cpUserFeed.getCpUserId() + "'" : "account_id";
        String keyword = (cpUserFeed.getKeyword() != null && !cpUserFeed.getKeyword().equals("")) ? "'" + cpUserFeed.getKeyword() + "'" : "keyword";
        String feed_id = (cpUserFeed.getFeedId() != null && !cpUserFeed.getFeedId().equals("")) ? "'" + cpUserFeed.getFeedId() + "'" : "feed_id";
        String allowed_age = (cpUserFeed.getAllowedAge() != 0) ? "'" + cpUserFeed.getAllowedAge() + "'" : "allowed_age";
        String regex_reject = (cpUserFeed.getRegexReject() != null && !cpUserFeed.getRegexReject().equals("")) ? "'" + cpUserFeed.getRegexReject() + "'" : "regex_reject";
        String msg_dlr_priority = (cpUserFeed.getMsgDlrPrority() != 0) ? "'" + cpUserFeed.getMsgDlrPrority() + "'" : "msg_dlr_priority";

        try {
            con = DConnect.getConnection();
            SQL = "UPDATE cp_user_feeds SET account_id = " + account_id + ", keyword = " + keyword + ", feed_id = " + feed_id + ","
                    + "allowed_age = " + allowed_age + ", regex_reject = " + regex_reject + ", msg_dlr_priority = " + msg_dlr_priority
                    + " WHERE account_id = ? and keyword = ? ";

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

    public static void createServiceLabel(String account_id, String keyword, String header, String footer) {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            SQL = "insert into service_labels (account_id, keyword, header, footer)"
                    + "values(?, ?, ?, ?)";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, account_id);
            prepstat.setString(2, keyword);
            prepstat.setString(3, header);
            prepstat.setString(4, footer);

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

    public static HashMap viewServiceLabel(String account_id, String keyword) throws Exception {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        HashMap serviceLabel = new HashMap();

        try {
            con = DConnect.getConnection();

            SQL = "select * from service_labels where account_id = ? and keyword = ? ";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, account_id);
            prepstat.setString(2, keyword);

            rs = prepstat.executeQuery();

            while (rs.next()) {
                serviceLabel.put("account_id", rs.getString("account_id"));
                serviceLabel.put("keyword", rs.getString("keyword"));
                serviceLabel.put("header", rs.getString("header"));
                serviceLabel.put("footer", rs.getString("footer"));
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

        return serviceLabel;

    }

    public static void deleteServiceLabel(String account_id, String keyword) throws Exception {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();

            SQL = "delete from service_labels where account_id = ? and keyword = ?";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, account_id);
            prepstat.setString(2, keyword);

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

    public static void deleteServiceLabel(String account_id, ArrayList keywords) throws Exception {
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
            SQL = "delete from service_labels where keyword in (" + keywordStr + ") and account_id='" + account_id + "'";
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

    public static void updateServiceLabel(String update_account_id, String update_keyword, String new_account_id, String new_keyword, String new_header, String new_footer) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        //Check and set update parameters
        String account_id = (new_account_id != null && !new_account_id.equals("")) ? "'" + new_account_id + "'" : "account_id";
        String keyword = (new_keyword != null && !new_keyword.equals("")) ? "'" + new_keyword + "'" : "keyword";
        String header = (new_header != null && !new_header.equals("")) ? "'" + new_header + "'" : "header";
        String footer = (new_footer != null && !new_footer.equals("")) ? "'" + new_footer + "'" : "footer";

        try {
            con = DConnect.getConnection();
            SQL = "UPDATE service_labels SET account_id = " + account_id + ", keyword = " + keyword + ", header = " + header + ","
                    + "footer = " + footer + " WHERE account_id = ? and keyword = ? ";

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

    public static void createServiceForwarding(String account_id, String keyword, String url, long timeout, int listen_status) {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            SQL = "insert into service_forwarding (account_id, keyword, url, timeout, listen_status)"
                    + "values(?, ?, ?, ?, ?)";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, account_id);
            prepstat.setString(2, keyword);
            prepstat.setString(3, url);
            prepstat.setLong(4, timeout);
            prepstat.setInt(5, listen_status);

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

    public static HashMap viewServiceForwarding(String account_id, String keyword) throws Exception {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        HashMap serviceLabel = new HashMap();

        try {
            con = DConnect.getConnection();

            SQL = "select * from service_forwarding where account_id = ? and keyword = ? ";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, account_id);
            prepstat.setString(2, keyword);

            rs = prepstat.executeQuery();

            while (rs.next()) {
                serviceLabel.put("account_id", rs.getString("account_id"));
                serviceLabel.put("keyword", rs.getString("keyword"));
                serviceLabel.put("url", rs.getString("url"));
                serviceLabel.put("timeout", rs.getString("timeout"));
                serviceLabel.put("listen_status", rs.getString("listen_status"));
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

        return serviceLabel;

    }

    public static void deleteServiceForwarding(String account_id, String keyword) throws Exception {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();

            SQL = "delete from service_forwarding where account_id = ? and keyword = ?";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, account_id);
            prepstat.setString(2, keyword);

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

    public static void deleteServiceForwarding(String account_id, ArrayList keywords) throws Exception {
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
            SQL = "delete from service_forwarding where keyword in (" + keywordStr + ") and account_id='" + account_id + "'";
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

    public static void updateServiceForwarding(String update_account_id, String update_keyword, String new_account_id,
            String new_keyword, String new_url, String new_timeout, String new_listen_status) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        //Check and set update parameters
        String account_id = (new_account_id != null && !new_account_id.equals("")) ? "'" + new_account_id + "'" : "account_id";
        String keyword = (new_keyword != null && !new_keyword.equals("")) ? "'" + new_keyword + "'" : "keyword";
        String url = (new_url != null && !new_url.equals("")) ? "'" + new_url + "'" : "url";
        String timeout = (new_timeout != null && !new_timeout.equals("")) ? "'" + new_timeout + "'" : "timeout";
        String listen_status = (new_listen_status != null && !new_listen_status.equals("")) ? "'" + new_listen_status + "'" : "listen_status";

        try {
            con = DConnect.getConnection();
            SQL = "UPDATE service_forwarding SET account_id = " + account_id + ", keyword = " + keyword + ", url = " + url + ","
                    + "timeout = " + timeout + ", listen_status = " + listen_status + " WHERE account_id = ? and keyword = ? ";

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
    /*public static String getServiceReportQuery(String csid, String msisdn, String status, java.sql.Timestamp startdate,
     java.sql.Timestamp enddate, int typeId, String listId, String pin, String siteId, String billed, int start, int count) throws Exception {
    
     java.util.ArrayList transactions = new java.util.ArrayList();
     ContentItem item = new ContentItem();
     Page page = null;
    
     int y = 0;
     int i = 0;
     int numResults = 0;
     boolean hasNext = false;
    
     String query;
     ResultSet rs = null;
     Connection con = null;
     PreparedStatement prepstat = null;
    
     try {
     con = DConnect.getConnection("rmcs");
    
     String siteIDquery = new String();
     String otherAffliliatedSiteIDquery = new String();
    
     //for my sites
     query = "select site_id from cp_sites where cp_id='" + csid +"'";
     prepstat = con.prepareStatement(query);
     rs = prepstat.executeQuery();
     while(rs.next()){
     siteIDquery = siteIDquery + "d.site_id='" + rs.getString("site_id") + "' or ";
     }
     siteIDquery = siteIDquery.substring(0, siteIDquery.lastIndexOf("' or ") + 1);
    
     //for my affiliated sites
     query = "select site_id from cp_sites where cp_id in (select cs_id from cs_cp_relationship where list_id='" + csid + "')";
     prepstat = con.prepareStatement(query);
     rs = prepstat.executeQuery();
     while(rs.next()){
     otherAffliliatedSiteIDquery = otherAffliliatedSiteIDquery + "(d.site_id='" + rs.getString("site_id") + "' and d.list_id='" + csid + "') or ";
     }
     otherAffliliatedSiteIDquery = otherAffliliatedSiteIDquery.substring(0, otherAffliliatedSiteIDquery.lastIndexOf(" or ") + 1);
    
     if((otherAffliliatedSiteIDquery == null || otherAffliliatedSiteIDquery.equals("")) && (siteIDquery == null || siteIDquery.equals(""))){
     listId = csid;
     }
    
     //for report
     query = "select * from download_log d inner join content_list c on d.id=c.id and d.list_id=c.list_id inner join format_list f " +
     "on c.formats=f.format_id inner join service_route t on c.content_type=t.service_type inner join cp_user u on d.list_id=u.id";
    
     if (msisdn != null && !msisdn.equals("")) {
     if (query.indexOf("where") == -1) {
     query = query + " where d.subscriberMSISDN='" + msisdn + "'";
     } else {
     query = query + " and d.subscriberMSISDN='" + msisdn + "'";
     }
     }
     if (status != null && (status.equals("1") || status.equals("0"))) {
     if (query.indexOf("where") == -1) {
     query = query + " where d.status=" + Integer.parseInt(status);
     } else {
     query = query + " and d.status=" + Integer.parseInt(status);
     }
     }
     if (startdate != null) {
     if (query.indexOf("where") == -1) {
     query = query + " where d.date_of_download >='" + startdate + "'";
     } else {
     query = query + " and d.date_of_download >='" + startdate + "'";
     }
     }
     if (enddate != null) {
     if (query.indexOf("where") == -1) {
     query = query + " where d.date_of_download <='" + enddate + "'";
     } else {
     query = query + " and d.date_of_download <='" + enddate + "'";
     }
     }
     if (siteId != null && !siteId.equals("")) {
     //checking owner of given site ID
     String tempquery = "select cp_id from cp_sites where site_id='" + siteId + "'";
     prepstat = con.prepareStatement(tempquery);
     ResultSet temprs = prepstat.executeQuery();
     if(temprs.next()){
     if(!csid.equals(temprs.getString("cp_id"))){ //not the owner of the site
     listId = csid;
     }
     }
     if (query.indexOf("where") == -1) {
     query = query + " where d.site_id='" + siteId + "'";
     } else {
     query = query + " and d.site_id='" + siteId + "'";
     }
     }
     if (listId != null && !listId.equals("")) {
     if (query.indexOf("where") == -1) {
     query = query + " where d.list_id ='" + listId + "'";
     } else {
     query = query + " and d.list_id ='" + listId + "'";
     }
     }
     if (pin != null && !pin.equals("")) {
     if (query.indexOf("where") == -1) {
     query = query + " where d.pin='" + pin + "'";
     } else {
     query = query + " and d.pin='" + pin + "'";
     }
     }
     if (billed != null && (billed.equals("1") || billed.equals("0"))) {
     if (query.indexOf("where") == -1) {
     query = query + " where d.billed=" + Integer.parseInt(billed);
     } else {
     query = query + " and d.billed=" + Integer.parseInt(billed);
     }
     }
     if (typeId != 0) {
     if (query.indexOf("where") == -1) {
     query = query + " where d.id=c.id and d.list_id=c.list_id " + "and c.content_type=" + typeId;
     } else {
     query = query + " and d.id=c.id and d.list_id=c.list_id and " + "c.content_type=" + typeId;
     }
     }
     if (siteId == null || siteId.equals("")) {
     if(siteIDquery != null && !siteIDquery.equals("")){
     if (query.indexOf("where") == -1) {
     query = query + " where ((" + siteIDquery + ")";
     } else {
     query = query + " and ((" + siteIDquery + ")";
     }
     if(otherAffliliatedSiteIDquery != null && !otherAffliliatedSiteIDquery.equals("")){
     query = query + " or " + otherAffliliatedSiteIDquery;
     }
     query = query + ")";
     }else{
     if(otherAffliliatedSiteIDquery != null && !otherAffliliatedSiteIDquery.equals("")){
     query = query + " and (" + otherAffliliatedSiteIDquery + ")";
     }
     }
    
     }
    
     prepstat = con.prepareStatement(query);
     rs = prepstat.executeQuery();
    
     // get the total number of records
     rs.last();
     numResults = rs.getRow();
     rs.beforeFirst();
    
     while (i < (start + count) && rs.next()) {
     if (i == 0) {
     int x = numResults;
     y = x / count;
     if ((x % count) > 0) {
     y += 1;
     }
     }
     if (i >= start) {
    
     Transaction newBean = new Transaction();
     //for transaction record
     newBean.setTicketID(rs.getString("d.ticketid"));
     newBean.setSubscriberMSISDN(rs.getString(
     "d.subscriberMSISDN"));
     newBean.setPhoneId(rs.getString("d.phone_id"));
     newBean.setDate(rs.getTimestamp("d.date_of_download"));
     newBean.setPin(rs.getString("d.pin"));
     if (rs.getInt("d.status") == 1) {
     newBean.setDownloadCompleted(true);
     } else {
     newBean.setDownloadCompleted(false);
     }
     newBean.setSiteId(rs.getString("d.site_id"));
     if (rs.getInt("d.billed") == 1) {
     newBean.setIsBilled(true);
     } else {
     newBean.setIsBilled(false);
     }
     newBean.setKeyword(rs.getString("d.keyword"));
    
     //for format object
     Format format = new Format(rs.getInt("f.format_id"),
     rs.getString("f.file_ext"),
     rs.getString("f.push_bearer"),
     rs.getString("f.mime_type"));
    
     //for type object
     ContentType type = new ContentType(rs.getString("t.service_name"), rs.getInt("t.service_type"),
     rs.getInt("t.parent_service_type"));
    
     //for cp_user
     com.rancard.mobility.contentprovider.User cp =
     new com.rancard.mobility.contentprovider.User();
     cp.setName(rs.getString("u.name"));
     cp.setDefaultSmsc(rs.getString("default_smsc"));
     cp.setId(rs.getString("u.id"));
     cp.setLogoUrl(rs.getString("logo_url"));
     cp.setPassword(rs.getString("password"));
     cp.setUsername(rs.getString("username"));
    
     //for content item
     item = new ContentItem();
     item.setAuthor(rs.getString("c.author"));
     if (rs.getInt("c.show") == 1) {
     item.setCanList(true);
     } else {
     item.setCanList(false);
     }
     item.setCategory(new Integer(rs.getInt(
     "c.category")));
     item.setContentId(rs.getString("c.content_id"));
     item.setContentTypeDetails(type);
     item.setDate_Added(rs.getTimestamp("c.date_added"));
     item.setDownloadUrl(rs.getString("c.download_url"));
     item.setFormat(format);
     item.setid(rs.getString("c.id"));
     item.setListId(rs.getString("c.list_id"));
     item.setOther_Details(rs.getString("c.other_details"));
     item.setPreviewUrl(rs.getString("c.preview_url"));
     item.setPrice(rs.getString("c.price"));
     item.setSize(new Long(rs.getLong("c.size")));
     if (rs.getInt("c.isLocal") == 1) {
     item.setIsLocal(true);
     } else {
     item.setIsLocal(false);
     }
     if (rs.getInt("c.isLocal") == 1) {
     item.setPreviewExists(new Boolean(true));
     } else {
     item.setPreviewExists(new Boolean(false));
     }
     item.settitle(rs.getString("c.title"));
     item.setProviderDetails(cp);
    
     newBean.setContentItem(item);
     newBean.setFormat(format);
    
     transactions.add(newBean);
     }
     i++;
     }
    
     hasNext = rs.next();
     page = new Page(transactions, start, hasNext, y, numResults);
    
     if (page == null) {
     page = com.rancard.util.Page.EMPTY_PAGE;
     }
    
     } catch (Exception ex) {
     if (con != null) {
     con.close();
     }
     throw new Exception(ex.getMessage());
     }
     if (con != null) {
     con.close();
     }
     return page;
     }*/
}
