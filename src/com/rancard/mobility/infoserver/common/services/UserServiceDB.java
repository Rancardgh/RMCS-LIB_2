package com.rancard.mobility.infoserver.common.services;

import com.rancard.common.DConnect;
import com.rancard.common.uidGen;
import com.rancard.mobility.infoserver.feeds.CPUserFeeds;
import com.rancard.mobility.infoserver.feeds.Feed;
import com.rancard.util.Page;
import com.rancard.util.PropertyHolder;

import java.io.PrintStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public abstract class UserServiceDB {
    public static void createService(UserService service)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            String SQL = "Insert into service_definition(service_type, keyword, account_id, service_name, default_message, command, allowed_shortcodes, allowed_site_types, is_basic, pricing, is_subscription, service_response_sender) values(?,?,?,?,?,?,?,?,?,?,?,?)";


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

    public static void updateService(String serviceType, String defaultMessage, String serviceName, String keyword, String accountId)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            String SQL = "UPDATE service_definition SET default_message=?,service_name=? WHERE keyword=?  and account_id =?";


            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, defaultMessage);
            prepstat.setString(2, serviceName);
            prepstat.setString(3, serviceType);
            prepstat.setString(4, keyword);
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

    public static void updateService(String defaultMessage, String keyword, String accountId)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            String SQL = "UPDATE service_definition SET default_message=? , last_updated = ? WHERE keyword=?  and account_id =?";


            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, defaultMessage);
            prepstat.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));
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

    public static void deleteService(String keyword, String accountId)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            String SQL = "delete from service_definition where keyword = ? and account_id=? ";


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

    public static void deleteService(ArrayList keywords, String accountId)
            throws Exception {
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
            String SQL = "delete from service_definition where keyword in (" + keywordStr + ") and account_id='" + accountId + "'";
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

    public static int getLastAccessCount(String msisdn, String accountId, String keyword)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        int accessCount = 0;


        System.out.println(new java.util.Date() + ":@com.rancard.mobility.infoserver.common.services.UserServiceDB...");
        System.out.println(new java.util.Date() + ": retrieving access count for last service (" + accountId + ", " + keyword + ") by " + msisdn + "...");
        try {
            con = DConnect.getConnection();

            String SQL = "select count(*) as 'access_count' from subscriber_request_history where msisdn = ? and account_id = ? and keyword = ? and date(log_time) = CURRENT_DATE order by log_time desc limit 5";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, msisdn);
            prepstat.setString(2, accountId);
            prepstat.setString(3, keyword);

            rs = prepstat.executeQuery();
            while (rs.next()) {
                accessCount = rs.getInt("access_count");


                System.out.println(new java.util.Date() + ": access count for last service (" + accountId + ", " + keyword + ") by " + msisdn + " is " + accessCount);
            }
        } catch (Exception ex) {
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

    public static String viewLastRequestedKeyword(String msisdn, String accountId, String siteId)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        String lastKeyword = "##BLANK##";


        System.out.println(new java.util.Date() + ":@com.rancard.mobility.infoserver.common.services.UserServiceDB...");
        System.out.println(new java.util.Date() + ": retrieving last requested keyword for (" + msisdn + ", " + accountId + ", " + siteId + ")...");
        try {
            con = DConnect.getConnection();

            String SQL = "select * from subscriber_request_history where msisdn = ? and account_id = ? and site_id = ? order by log_time desc limit 1";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, msisdn);
            prepstat.setString(2, accountId);
            prepstat.setString(3, siteId);

            rs = prepstat.executeQuery();
            while (rs.next()) {
                lastKeyword = rs.getString("keyword");


                System.out.println(new java.util.Date() + ": last requested keyword (" + msisdn + ", " + accountId + ", " + siteId + ") found!");
            }
            if (lastKeyword.equals("")) {
                System.out.println(new java.util.Date() + ": last requested keyword (" + msisdn + ", " + accountId + ", " + siteId + ") not found!");
            }
        } catch (Exception ex) {
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

    public static String viewLastSubscribedKeyword(String msisdn, String accountId)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        String lastKeyword = "##BLANK##";


        System.out.println(new java.util.Date() + ":@com.rancard.mobility.infoserver.common.services.UserServiceDB...");
        System.out.println(new java.util.Date() + ": retrieving last subscribed keyword for (" + msisdn + ", " + accountId + ")...");
        try {
            con = DConnect.getConnection();

            String SQL = "select * from service_subscription where msisdn = ? and account_id = ? order by subscription_date desc limit 1";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, msisdn);
            prepstat.setString(2, accountId);

            rs = prepstat.executeQuery();
            while (rs.next()) {
                lastKeyword = rs.getString("keyword");


                System.out.println(new java.util.Date() + ": last subscribed keyword (" + msisdn + ", " + accountId + ") found!");
            }
            if (lastKeyword.equals("")) {
                System.out.println(new java.util.Date() + ": last subscribed keyword (" + msisdn + ", " + accountId + ") not found!");
            }
        } catch (Exception ex) {
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

    public static UserService viewServiceByAlias(String alias, String accountId)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        UserService service = new UserService();


        System.out.println(new java.util.Date() + ":@com.rancard.mobility.infoserver.common.services.UserServiceDB...");
        System.out.println(new java.util.Date() + ": viewing service_definition for alias (" + alias + ", " + accountId + ")...");
        try {
            con = DConnect.getConnection();

            String SQL = "select * from keyword_aliases ka inner join service_definition sd on sd.keyword = ka.keyword and sd.account_id = ka.account_id where ka.key_alias = ? and ka.account_id = ?";

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
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH.mm.ss");
                String publishTime = df.format(new java.util.Date(rs.getTimestamp("last_updated").getTime()));
                service.setLastUpdated(publishTime);
                service.setCommand(rs.getString("command"));
                service.setAllowedShortcodes(rs.getString("allowed_shortcodes"));
                service.setAllowedSiteTypes(rs.getString("allowed_site_types"));

                service.setPricing(rs.getString("pricing"));
                service.setServiceResponseSender(rs.getString("service_response_sender"));
                if (rs.getInt("is_basic") == 1) {
                    service.setIsBasic(true);
                } else {
                    service.setIsBasic(false);
                }
                if ((rs.getInt("is_subscription") == 1) || (rs.getString("is_subscription").equals("true"))) {
                    service.setIsSubscription(true);
                } else {
                    service.setIsSubscription(false);
                }
                System.out.println(new java.util.Date() + ": service for alias (" + alias + ", " + accountId + ") found!");
            }
            if ((service.getKeyword() == null) || ("".equals(service.getKeyword()))) {
                System.out.println(new java.util.Date() + ": service for alias (" + alias + ", " + accountId + ") not found!");
            }
        } catch (Exception ex) {
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

    public static UserService viewService(String keyword, String accountId)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        UserService service = new UserService();


        System.out.println(new java.util.Date() + ":@com.rancard.mobility.infoserver.common.services.UserServiceDB...");
        System.out.println(new java.util.Date() + ": viewing service_definition for (" + keyword + ", " + accountId + ")...");
        try {
            con = DConnect.getConnection();

            String SQL = "select * from service_definition where keyword = ? and account_id = ? ";

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
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH.mm.ss");
                String publishTime = df.format(new java.util.Date(rs.getTimestamp("last_updated").getTime()));
                service.setLastUpdated(publishTime);
                service.setCommand(rs.getString("command"));
                service.setAllowedShortcodes(rs.getString("allowed_shortcodes"));
                service.setAllowedSiteTypes(rs.getString("allowed_site_types"));

                service.setPricing(rs.getString("pricing"));
                service.setServiceResponseSender(rs.getString("service_response_sender"));
                if (rs.getInt("is_basic") == 1) {
                    service.setIsBasic(true);
                } else {
                    service.setIsBasic(false);
                }
                if ((rs.getInt("is_subscription") == 1) || (rs.getString("is_subscription").equals("true"))) {
                    service.setIsSubscription(true);
                } else {
                    service.setIsSubscription(false);
                }
                System.out.println(new java.util.Date() + ": service (" + keyword + ", " + accountId + ") found!");
            }
            if ((service.getKeyword() == null) || ("".equals(service.getKeyword()))) {
                System.out.println(new java.util.Date() + ": service (" + keyword + ", " + accountId + ") not found!");
            }
        } catch (Exception ex) {
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

    public static ArrayList viewAllServices(String accountId)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        ArrayList serviceList = new ArrayList();
        try {
            con = DConnect.getConnection();
            String SQL = "select * from service_definition where account_id =?";
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
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH.mm.ss");
                String publishTime = df.format(new java.util.Date(rs.getTimestamp("last_updated").getTime()));
                service.setLastUpdated(publishTime);
                service.setCommand(rs.getString("command"));
                service.setAllowedShortcodes(rs.getString("allowed_shortcodes"));
                service.setAllowedSiteTypes(rs.getString("allowed_site_types"));

                service.setPricing(rs.getString("pricing"));
                service.setServiceResponseSender(rs.getString("service_response_sender"));
                if (rs.getInt("is_basic") == 1) {
                    service.setIsBasic(true);
                } else {
                    service.setIsBasic(false);
                }
                if ((rs.getInt("is_subscription") == 1) || (rs.getString("is_subscription").equals("true"))) {
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

    public static ArrayList viewAllServices(String accountId, String serviceType)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        ArrayList serviceList = new ArrayList();
        try {
            con = DConnect.getConnection();
            String SQL = "select * from service_definition where account_id =? and service_type=?";
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
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH.mm.ss");
                String publishTime = df.format(new java.util.Date(rs.getTimestamp("last_updated").getTime()));
                service.setLastUpdated(publishTime);
                service.setCommand(rs.getString("command"));
                service.setAllowedShortcodes(rs.getString("allowed_shortcodes"));
                service.setAllowedSiteTypes(rs.getString("allowed_site_types"));

                service.setPricing(rs.getString("pricing"));
                service.setServiceResponseSender(rs.getString("service_response_sender"));
                if (rs.getInt("is_basic") == 1) {
                    service.setIsBasic(true);
                } else {
                    service.setIsBasic(false);
                }
                if ((rs.getInt("is_subscription") == 1) || (rs.getString("is_subscription").equals("true"))) {
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

    public static ArrayList<UserService> viewAllServices(String accountId, String serviceType, String command)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        ArrayList serviceList = new ArrayList();
        try {
            con = DConnect.getConnection();
            String SQL = "select * from service_definition where account_id =? and service_type=? and command=?";
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
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH.mm.ss");
                String publishTime = df.format(new java.util.Date(rs.getTimestamp("last_updated").getTime()));
                service.setLastUpdated(publishTime);
                service.setCommand(rs.getString("command"));
                service.setAllowedShortcodes(rs.getString("allowed_shortcodes"));
                service.setAllowedSiteTypes(rs.getString("allowed_site_types"));

                service.setPricing(rs.getString("pricing"));
                service.setServiceResponseSender(rs.getString("service_response_sender"));
                if (rs.getInt("is_basic") == 1) {
                    service.setIsBasic(true);
                } else {
                    service.setIsBasic(false);
                }
                if ((rs.getInt("is_subscription") == 1) || (rs.getString("is_subscription").equals("true"))) {
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
        return serviceList;
    }

    public static ArrayList viewAllServicesOfParentType(String accountId, String parentType)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        ArrayList serviceList = new ArrayList();
        try {
            con = DConnect.getConnection();
            String SQL = "select * from service_definition sd, service_route sr where account_id =? and sd.service_type=sr.service_type and sr.parent_service_type=? order by sd.service_name;";

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
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH.mm.ss");
                String publishTime = df.format(new java.util.Date(rs.getTimestamp("last_updated").getTime()));
                service.setLastUpdated(publishTime);
                service.setCommand(rs.getString("command"));
                service.setAllowedShortcodes(rs.getString("allowed_shortcodes"));
                service.setAllowedSiteTypes(rs.getString("allowed_site_types"));

                service.setPricing(rs.getString("pricing"));
                service.setServiceResponseSender(rs.getString("service_response_sender"));
                if (rs.getInt("is_basic") == 1) {
                    service.setIsBasic(true);
                } else {
                    service.setIsBasic(false);
                }
                if ((rs.getInt("is_subscription") == 1) || (rs.getString("is_subscription").equals("true"))) {
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
        return serviceList;
    }

    public static ArrayList viewAllServicesForType(String serviceType)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        ArrayList serviceList = new ArrayList();
        try {
            con = DConnect.getConnection();
            String SQL = "select * from service_definition where service_type=? order by account_id";
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

                service.setPricing(rs.getString("pricing"));
                service.setServiceResponseSender(rs.getString("service_response_sender"));
                if (rs.getInt("is_basic") == 1) {
                    service.setIsBasic(true);
                } else {
                    service.setIsBasic(false);
                }
                if ((rs.getInt("is_subscription") == 1) || (rs.getString("is_subscription").equals("true"))) {
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
        return serviceList;
    }

    public static HashMap populateRoutingTable()
            throws Exception {
        HashMap routingTable = new HashMap();


        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;


        System.out.println(new java.util.Date() + ":@com.rancard.mobility.infoserver.common.services.UserServiceDB!");
        System.out.println(new java.util.Date() + ": populating routing table (HashMap)...");

        ArrayList serviceList = new ArrayList();
        try {
            con = DConnect.getConnection();
            String SQL = "select service_type, service_url from service_route";
            prepstat = con.prepareStatement(SQL);

            rs = prepstat.executeQuery();
            while (rs.next()) {
                routingTable.put(rs.getString("service_type"), rs.getString("service_url"));


                System.out.println(new java.util.Date() + ": record added to routing table (HashMap): service_type=" + rs.getString("service_type") + ",  service_url=" + rs.getString("service_url"));
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
        return routingTable;
    }

    public static HashMap getServiceTable()
            throws Exception {
        HashMap serviceTable = new HashMap();


        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        ArrayList serviceList = new ArrayList();
        try {
            con = DConnect.getConnection();
            String SQL = "select service_type, service_name from service_route";
            prepstat = con.prepareStatement(SQL);

            rs = prepstat.executeQuery();
            while (rs.next()) {
                serviceTable.put(rs.getString("service_type"), rs.getString("service_name"));
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
        return serviceTable;
    }

    public static String[][] getCPsForTypes()
            throws Exception {
        String[][] struct = (String[][]) null;
        String query = "SELECT distinct service_definition.service_type, service_definition.account_id, cp_user.name, cp_user.logo_url FROM service_definition, cp_user where cp_user.id=service_definition.account_id order by service_definition.service_type";


        String logo_url = new String();

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
                String typeId = rs.getString("service_type");
                String account_id = rs.getString("account_id");
                String name = rs.getString("name");
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

    public static String[] getCPIDsForServiceType(String serviceType)
            throws Exception {
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

    public static HashMap<String, String> getCPIDsForServiceType(String serviceType, String command)
            throws Exception {
        HashMap struct = new HashMap();
        String accountId = "";
        String keyword = "";
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        String query = "SELECT account_id, keyword from service_definition where service_type='" + serviceType + "' " + "and command='" + command + "'";


        System.out.println(new java.util.Date() + ":Looking for CPIds for serviceType-command:" + serviceType + ":" + command);
        try {
            con = DConnect.getConnection();
            prepstat = con.prepareStatement(query);
            rs = prepstat.executeQuery();


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

    public static void updateSubscriberRequestHistory(String msisdn, String keyword, String accountID, String site_id, java.util.Date now, int count)
            throws Exception {
        Connection conn = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sql = "UPDATE subscriber_request_history SET count = " + count + " where keyword = '" + keyword + "' and account_id = '" + accountID + "' and msisdn = '" + msisdn + "' and site_id = '" + site_id + "'";
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

    public static int getCountOnSubscriberRequestHistory(String msisdn, String keyword, String accountID, String site_id)
            throws Exception {
        Connection conn = null;
        ResultSet rs = null;
        String sql = "SELECT count FROM subscriber_request_history where keyword = '" + keyword + "' and account_id = '" + accountID + "' and msisdn = '" + msisdn + "' and site_id = '" + site_id + "'";
        try {
            conn = DConnect.getConnection();


            System.out.println("Getting subscriber request count: " + sql);
            rs = conn.createStatement().executeQuery(sql);
            int i;
            if (rs.first()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static boolean stoppedB4SubscriptionEnded(String msisdn, String keyword, String accountID, java.util.Date now)
            throws Exception {
        Connection conn = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ResultSet rs = null;
        String sql = "SELECT * FROM service_subscription_deleted where keyword = '" + keyword + "' and account_id = '" + accountID + "' and msisdn = '" + msisdn + "' and subscription_date < '" + df.format(now) + "' and next_subscription_date > '" + df.format(now) + "' order by unsubscription_date DESC limit 1";


        System.out.println("Check if user unsubscribed recently: " + sql);
        try {
            conn = DConnect.getConnection();

            rs = conn.createStatement().executeQuery(sql);
            boolean bool;
            if (rs != null) {
                if (rs.first()) {
                    System.out.println("Yes they have unsubscribed recently");
                    return true;
                }
                System.out.println("No they have not unsubscribed recently");
                return false;
            }
            System.out.println("Yes they have unsubscribed recently");
            return false;
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

    public static void updateSubscriptionStatus(String msisdn, String keyword, String accountId, int status)
            throws Exception {
        int error = 0;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        UserService service = new UserService();
        try {
            con = DConnect.getConnection();

            String SQL = "select * from service_definition where keyword='" + keyword + "' and account_id='" + accountId + "'";
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
                    error = 2;
                }
            } else {
                error = 1;
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
            throw new Exception("10001");
        }
        if (error == 2) {
            throw new Exception("10000");
        }
    }

    public static String subscribeToService(String msisdn, String keyword, String accountId)
            throws Exception {
        String regId = new String();

        int error = 0;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        UserService service = new UserService();
        try {
            con = DConnect.getConnection();

            String SQL = "select * from service_definition where keyword='" + keyword + "' and account_id='" + accountId + "'";
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
                    prepstat.setTimestamp(2, new Timestamp(Calendar.getInstance().getTime().getTime()));
                    prepstat.setString(3, msisdn);
                    prepstat.setString(4, keyword);
                    prepstat.setString(5, accountId);
                    prepstat.execute();
                } else {
                    error = 2;
                }
            } else {
                error = 1;
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
            throw new Exception("10001");
        }
        if (error == 2) {
            throw new Exception("10007");
        }
        return regId;
    }

    public static String[] subscribeToService(String msisdn, ArrayList keywords, String accountId)
            throws Exception {
        String[] regId = new String[2];

        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        boolean failedCheck = false;
        try {
            con = DConnect.getConnection();
            String SQL = "select * from address_book where account_id='" + accountId + "' and msisdn='" + msisdn + "'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            if (!rs.next()) {
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
                        prepstat.setTimestamp(1, new Timestamp(Calendar.getInstance().getTime().getTime()));
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

    public static String[] subscribeToService(String msisdn, ArrayList keywords, String accountId, int numOfDays)
            throws Exception {
        String[] regId = new String[3];
        int error = 0;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        boolean failedCheck = false;

        Calendar c = Calendar.getInstance();
        c.add(5, numOfDays);
        String nextSubDate = new java.sql.Date(c.getTimeInMillis()).toString();
        try {
            con = DConnect.getConnection();
            String SQL = "select * from address_book where account_id='" + accountId + "' and msisdn='" + msisdn + "'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            if (!rs.next()) {
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
                    prepstat.setTimestamp(1, new Timestamp(Calendar.getInstance().getTime().getTime()));
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

    public static String[] subscribeToService(String msisdn, ArrayList keywords, String accountId, int numOfDays, int status, int billingType)
            throws Exception {
        String[] regId = new String[3];
        int error = 0;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        boolean failedCheck = false;

        Calendar c = Calendar.getInstance();
        c.add(5, numOfDays);
        java.util.Date dt = c.getTime();
        SimpleDateFormat sm_formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nextSubDate = sm_formatter1.format(dt);
        try {
            con = DConnect.getConnection();
            String SQL = "select * from address_book where account_id='" + accountId + "' and msisdn='" + msisdn + "'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            if (!rs.next()) {
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
                    prepstat.setTimestamp(1, new Timestamp(Calendar.getInstance().getTime().getTime()));
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

    public static void resumeSubscription(String msisdn, ArrayList keywords, String accountId, int status, java.util.Date subscriptionDate, java.util.Date nextSubscriptionDate)
            throws Exception {
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

            String SQL = "delete from service_subscription where keyword in (" + keywordStr + ") and account_id='" + accountId + "' and msisdn='" + msisdn + "'";
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
                    prepstat.setTimestamp(1, new Timestamp(subscriptionDate.getTime()));
                    prepstat.setString(2, msisdn);
                    prepstat.setString(3, keyword);
                    prepstat.setString(4, accountId);
                    prepstat.setInt(5, status);
                    prepstat.setTimestamp(6, new Timestamp(nextSubscriptionDate.getTime()));
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

    public static void unsubscribeToService(String msisdn, String keyword, String accountId)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            String SQL;
            if ((keyword != null) && (!keyword.equals(""))) {
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

    public static void forceUnsubscribe(String keyword, String accountId)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            String SQL = "delete from service_subscription where keyword='" + keyword + "' and account_id='" + accountId + "'";
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

    public static void forceUnsubscribe(String msisdn, ArrayList keywords, String accountId)
            throws Exception {
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
            String SQL = "delete from service_subscription where msisdn='" + msisdn + "' and keyword in (" + keywordStr + ") and account_id='" + accountId + "'";
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

    public static boolean verifyUser(String msisdn, String regId, String acctId, String keyword)
            throws Exception {
        boolean isRegistered = false;

        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        UserService service = new UserService();
        try {
            con = DConnect.getConnection();
            String SQL = "select * from address_book ab inner join service_subscription ss on ab.msisdn=ss.msisdn and ab.account_id=ss.account_id where ab.msisdn='" + msisdn + "' and ab.account_id='" + acctId + "' and ss.keyword='" + keyword + "'";

            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            if ((rs.next()) && (rs.getString("ab.registration_id").equals(regId))) {
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
        return isRegistered;
    }

    public static boolean isRegistered(String msisdn, String accountId)
            throws Exception {
        boolean isRegistered = false;

        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            String SQL = "select * from address_book where account_id='" + accountId + "' and msisdn='" + msisdn + "'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            if (!rs.next()) {
                isRegistered = false;
            }
            return true;
        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex1) {
                    System.out.println(ex1.getMessage());
                }
                con = null;
            }
            return isRegistered;
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

    public static boolean isRegistered(String msisdn, String accountId, String keyword)
            throws Exception {
        boolean isRegistered = false;

        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            String SQL = "select * from address_book ab inner join service_subscription ss on ab.msisdn=ss.msisdn and ab.account_id=ss.account_id where ab.account_id='" + accountId + "' and ab.msisdn='" + msisdn + "'" + " and ss.keyword='" + keyword + "'";

            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            if (!rs.next()) {
                isRegistered = false;
            }
            return true;
        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex1) {
                    System.out.println(ex1.getMessage());
                }
                con = null;
            }
            return isRegistered;
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

    public static boolean isSubscribed(String msisdn, String accountId, String keyword)
            throws Exception {
        boolean isSubscribed = false;

        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            String SQL = "select * from service_subscription where account_id='" + accountId + "' and msisdn='" + msisdn + "'" + " and keyword='" + keyword + "'";

            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            if (!rs.next()) {
                isSubscribed = false;
            }
            return true;
        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex1) {
                    System.out.println(ex1.getMessage());
                }
                con = null;
            }
            return isSubscribed;
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

    public static HashMap getSubscription(String msisdn, String accountId, String keyword, String alternativeKeyword)
            throws Exception {
        HashMap subscription = new HashMap();

        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            String SQL;

            if ((alternativeKeyword != null) && (!alternativeKeyword.trim().equals(""))) {
                SQL = "select * from service_subscription where account_id='" + accountId + "' and msisdn='" + msisdn + "'" + " and (keyword='" + keyword + "' or keyword='" + alternativeKeyword + "')";
            } else {
                SQL = "select * from service_subscription where account_id='" + accountId + "' and msisdn='" + msisdn + "'" + " and keyword='" + keyword + "'";
            }
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            if (rs.next()) {
                subscription.put("msisdn", rs.getString("msisdn"));
                subscription.put("keyword", rs.getString("keyword"));
                subscription.put("account_id", rs.getString("account_id"));
                subscription.put("subscription_date", rs.getTimestamp("subscription_date"));
                subscription.put("next_subscription_date", rs.getTimestamp("next_subscription_date"));
                subscription.put("status", Integer.valueOf(rs.getInt("status")));
                subscription.put("billing_type", Integer.valueOf(rs.getInt("billing_type")));
            }
            return subscription;
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

    public static HashMap getRecentUnsubscription(String msisdn, String keyword, String accountID, java.util.Date now)
            throws Exception {
        HashMap subscription = new HashMap();
        Connection conn = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ResultSet rs = null;
        String sql = "SELECT * FROM service_subscription_deleted where keyword = '" + keyword + "' and account_id = '" + accountID + "' and msisdn = '" + msisdn + "' and subscription_date < '" + df.format(now) + "' and next_subscription_date > '" + df.format(now) + "' order by unsubscription_date DESC limit 1";


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
                subscription.put("status", Integer.valueOf(rs.getInt("status")));
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

    public static boolean hasRecentUnsubscription(String msisdn, String keyword, String accountID)
            throws Exception {
        boolean flag = false;
        Connection conn = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ResultSet rs = null;
        String sql = "SELECT * FROM service_subscription_deleted where keyword = '" + keyword + "' and account_id = '" + accountID + "' and msisdn = '" + msisdn + "' order by unsubscription_date DESC limit 1";

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

    public static boolean isMonthlySubscriber(String msisdn, String accountId)
            throws Exception {
        boolean isRegistered = false;

        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            String SQL = "select * from address_book ab inner join service_subscription ss on ab.msisdn=ss.msisdn where ab.account_id='" + accountId + "' and ab.msisdn='" + msisdn + "'" + " and ss.keyword in(select keyword from service_definition where account_id='" + accountId + "' and command='2' and is_basic='1')";

            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            if (!rs.next()) {
                isRegistered = false;
            }
            return true;
        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex1) {
                    System.out.println(ex1.getMessage());
                }
                con = null;
            }
            return isRegistered;
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

    public static ArrayList getKeywordsOfBasicServices(String accountId)
            throws Exception {
        ArrayList keywords = new ArrayList();

        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            String SQL = "select keyword from service_definition where is_basic=1 and account_id='" + accountId + "'";
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

    public static ArrayList getKeywordsUserSubscribedTo(String msisdn, String accountId)
            throws Exception {
        ArrayList keywords = new ArrayList();

        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            String SQL = "select keyword from service_subscription where msisdn='" + msisdn + "' and account_id='" + accountId + "'";
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

    public static ArrayList getKeywordsUserSubscribedTo(String msisdn, String accountId, ArrayList<String> keywordList)
            throws Exception {
        ArrayList keywords = new ArrayList();

        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            String SQL = "select keyword from service_subscription where msisdn='" + msisdn + "' and account_id='" + accountId + "' and keyword in (" + expandKeywordList(keywordList) + ")";
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

    public static ArrayList getKeywordsOfSubscriptionServices(String accountId)
            throws Exception {
        ArrayList keywords = new ArrayList();

        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            String SQL = "select keyword from service_definition where is_subscription=1 and account_id='" + accountId + "'";
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

    public static int[] updateServiceRSSFeeds(String accountId, String keyword, ArrayList<String> userFeedList)
            throws Exception {
        boolean updateSucessful = false;

        ResultSet rs = null;
        Connection con = null;
        Statement stmt = null;
        int[] upCounts = null;
        Iterator<String> it = userFeedList.iterator();
        String feedIdList = "('";
        while (it.hasNext()) {
            feedIdList = feedIdList + (String) it.next();
            if (it.hasNext()) {
                feedIdList = feedIdList + "','";
            }
        }
        feedIdList = feedIdList + "')";
        try {
            con = DConnect.getConnection();
            con.setAutoCommit(false);
            stmt = con.createStatement();
            String SQL = "delete from cp_user_feeds where account_id ='" + accountId + "' and keyword ='" + keyword + "'";
            stmt.addBatch(SQL);
            for (int i = 0; i < userFeedList.size(); i++) {
                String feed = (String) userFeedList.get(i);
                SQL = "insert into cp_user_feeds (account_id, keyword, feed_id) values ('" + accountId + "', '" + keyword + "','" + feed + "')";

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

    public static Page viewSubscribers(String csid, String[] keywords, Timestamp startdate, Timestamp enddate, int start, int count)
            throws Exception {
        ArrayList subscribers = new ArrayList();
        Page page = null;

        int y = 0;
        int i = 0;
        int numResults = 0;
        boolean hasNext = false;


        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();

            String query = "select msisdn from service_subscription where account_id='" + csid + "' ";
            if ((keywords != null) && (keywords.length != 0)) {
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


            rs.last();
            numResults = rs.getRow();
            rs.beforeFirst();
            while ((i < start + count) && (rs.next())) {
                if (i == 0) {
                    int x = numResults;
                    y = x / count;
                    if (x % count > 0) {
                        y++;
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
                page = Page.EMPTY_PAGE;
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

    public static ArrayList viewServiceRSSFeeds(String accountId, String keyword)
            throws Exception {
        ArrayList feedsList = new ArrayList();
        Page page = null;

        int y = 0;
        int i = 0;
        int numResults = 0;
        boolean hasNext = false;


        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();

            String query = "select * from cp_user_feeds where account_id='" + accountId + "' and  keyword='" + keyword + "'";

            prepstat = con.prepareStatement(query);
            rs = prepstat.executeQuery();
            while (rs.next()) {
                CPUserFeeds feed = new CPUserFeeds();
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

    public static String[] viewAllSubscribers(String csid, String keyword)
            throws Exception {
        String[] subscribers = null;


        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();

            String query = "select msisdn from service_subscription where account_id='" + csid + "' and keyword='" + keyword + "' order by msisdn desc";

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

    public static String[] viewAllSubscribersWithStatus(String csid, String keyword, int status)
            throws Exception {
        String[] subscribers = null;


        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();

            String query = "select msisdn from service_subscription where account_id='" + csid + "' and keyword='" + keyword + "' and status=" + status + " order by msisdn desc";

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

    public static HashMap viewSubscribersGroupByNetworkPrefix(String csid, String keyword, int status)
            throws Exception {
        HashMap groupedSubscribers = new HashMap();
        String value = "";
        try {
            value = PropertyHolder.getPropsValue("MAX_SUBS_FOR_PRE_GAME_ALERTS");
            if ((value == null) || (value.equals(""))) {
                value = "40000";
            }
        } catch (Exception e) {
            value = "40000";
        }
        ResultSet networks = null;
        ResultSet subs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();


            String query = "select allowed_networks from cp_connections where list_id='" + csid + "'";
            prepstat = con.prepareStatement(query);
            networks = prepstat.executeQuery();
            while (networks.next()) {
                String prefix = new String();
                prefix = networks.getString("allowed_networks");


                List prefixList = Arrays.asList(prefix.split(","));
                String prefixQuery = "";
                for (int i = 0; i < prefixList.size(); i++) {
                    String OR = i + 1 < prefixList.size() ? " OR " : "";
                    prefixQuery = prefixQuery + "ss.msisdn like '+" + prefixList.get(i) + "%' " + OR;
                }
                System.out.println(new java.util.Date() + ":network_prefixQuery:" + prefixQuery);


                query = "select ab.msisdn from address_book ab inner join service_subscription ss on ab.msisdn=ss.msisdn and ab.account_id=ss.account_id where ab.account_id='" + csid + "' and ss.keyword='" + keyword + "' and ss.status=" + status + " and (" + prefixQuery + ") limit " + value;


                prepstat = con.prepareStatement(query);
                subs = prepstat.executeQuery();


                subs.last();
                int numResults = subs.getRow();
                String[] subscribers = new String[numResults];
                subs.beforeFirst();


                int count = 0;
                while (subs.next()) {
                    subscribers[count] = subs.getString("ab.msisdn");
                    count++;
                }
                if (count > 0) {
                    System.out.println(new java.util.Date() + ":adding subscribers for network:" + prefix + ", No.Subscribers:" + subscribers.length);
                } else {
                    System.out.println(new java.util.Date() + ":No subscriber found for network:" + prefix);
                }
                groupedSubscribers.put(prefix, subscribers);
            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return groupedSubscribers;
    }

    public static HashMap viewSubscribersGroupByNetworkPrefix(String csid, String keyword, String alias, int status)
            throws Exception {
        HashMap groupedSubscribers = new HashMap();


        ResultSet networks = null;
        ResultSet subs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();


            String query = "select allowed_networks from cp_connections where list_id='" + csid + "'";
            prepstat = con.prepareStatement(query);
            networks = prepstat.executeQuery();
            while (networks.next()) {
                String prefix = new String();
                prefix = networks.getString("allowed_networks");


                query = "select ab.msisdn from address_book ab inner join service_subscription ss on ab.msisdn=ss.msisdn and ab.account_id=ss.account_id where ab.account_id='" + csid + "' and ss.keyword='" + keyword + "' and ss.status=" + status + " and ss.msisdn like '+" + prefix + "%'";

                prepstat = con.prepareStatement(query);
                subs = prepstat.executeQuery();


                subs.last();
                int numResults = subs.getRow();
                String[] subscribers = new String[numResults];
                subs.beforeFirst();


                int count = 0;
                while (subs.next()) {
                    subscribers[count] = subs.getString("ab.msisdn");
                    count++;
                }
                groupedSubscribers.put(prefix, subscribers);
            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return groupedSubscribers;
    }

    public static HashMap viewSubscribersGroupByNetworkPrefix(String accountid, String keyword, int start, int count)
            throws Exception {
        HashMap groupedSubscribers = new HashMap();
        boolean hasNext = false;
        List logList = new ArrayList();
        Page ret = null;
        int y = 0;
        int i = 0;
        int numResults = 0;


        ResultSet networks = null;
        ResultSet subs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();


            String query = "select allowed_networks from cp_connections where list_id='" + accountid + "'";
            prepstat = con.prepareStatement(query);
            networks = prepstat.executeQuery();
            while (networks.next()) {
                String prefix = new String();
                prefix = networks.getString("allowed_networks");


                query = "select ab.msisdn from address_book ab inner join service_subscription ss on ab.msisdn=ss.msisdn and ab.account_id=ss.account_id inner join service_definition sd on ss.account_id=sd.account_id and ss.keyword=sd.keyword where ab.account_id='" + accountid + "' and ss.keyword='" + keyword + "' and ss.msisdn like '+" + prefix + "%'";

                prepstat = con.prepareStatement(query);
                subs = prepstat.executeQuery();


                subs.last();
                numResults = subs.getRow();
                subs.beforeFirst();
                while ((i < start + count) && (subs.next())) {
                    if (i == 0) {
                        int x = numResults;
                        y = x / count;
                        if (x % count > 0) {
                            y++;
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
                    ret = Page.EMPTY_PAGE;
                }
                groupedSubscribers.put(prefix, ret);
            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return groupedSubscribers;
    }

    public static ArrayList viewTempSubscribersGroupByNetworkPrefix(String accountid, String keyword, String processId)
            throws Exception {
        ArrayList groupedSubscribers = new ArrayList();


        ResultSet subs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();


            String query = "select ab.msisdn,ss.keyword from address_book ab inner join temp_service_subscription ss on ab.msisdn=ss.msisdn and ab.account_id=ss.account_id where ab.account_id='" + accountid + "' and ss.process_id='" + processId + "' and ss.keyword='" + keyword + "'";

            prepstat = con.prepareStatement(query);
            subs = prepstat.executeQuery();


            subs.last();
            int numResults = subs.getRow();
            String[][] subscribers = new String[numResults][2];
            subs.beforeFirst();


            int count = 0;
            while (subs.next()) {
                subscribers[count][0] = subs.getString("ab.msisdn");
                subscribers[count][1] = subs.getString("ss.keyword");
                count++;
            }
            groupedSubscribers.add(subscribers);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return groupedSubscribers;
    }

    public static ArrayList viewTempSubscribersGroupByNetworkPrefix(String accountid, String processId)
            throws Exception {
        System.out.println(new java.util.Date() + ":@com.rancard.mobility.infoserver.common.services.UserServiceDB:viewTempSubscribersGroupByNetworkPrefix");
        System.out.println(new java.util.Date() + ":retrieving MONTHLY_BILLING subscriptions for account " + accountid + " from temp_service_subscription table..");

        ArrayList groupedSubscribers = new ArrayList();


        ResultSet subs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();


            String query = "select ab.msisdn,ss.keyword,ss.billing_type from address_book ab inner join temp_service_subscription ss on ab.msisdn=ss.msisdn and ab.account_id=ss.account_id where ab.account_id='" + accountid + "' and ss.process_id='" + processId + "'";

            prepstat = con.prepareStatement(query);
            subs = prepstat.executeQuery();


            subs.last();
            int numResults = subs.getRow();
            String[][] subscribers = new String[numResults][3];
            subs.beforeFirst();


            int count = 0;
            while (subs.next()) {
                subscribers[count][0] = subs.getString("ab.msisdn");
                subscribers[count][1] = subs.getString("ss.keyword");
                subscribers[count][2] = ("" + subs.getString("ss.billing_type"));


                groupedSubscribers.add(subscribers);

                count++;
            }
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
            System.out.println(new java.util.Date() + ": error in viewTempSubscribersGroupByNetworkPrefix:" + ex.getMessage());

            throw new Exception(ex.getMessage());
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return groupedSubscribers;
    }

    public static HashMap<String, String[]> viewTempSubscribersGroupByNetworkPrefix(String accountId, int status, java.util.Date today)
            throws Exception {
        HashMap groupedSubscribers = new HashMap();
        String value = "";

        String nextSubscriptionDate = new java.sql.Date(today.getTime()).toString();


        ResultSet networks = null;
        ResultSet subs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();


            String query = "select allowed_networks from cp_connections where list_id='" + accountId + "'";
            prepstat = con.prepareStatement(query);
            networks = prepstat.executeQuery();
            while (networks.next()) {
                String prefix = new String();
                prefix = networks.getString("allowed_networks");


                List prefixList = Arrays.asList(prefix.split(","));
                String prefixQuery = "";
                for (int i = 0; i < prefixList.size(); i++) {
                    String OR = i + 1 < prefixList.size() ? " OR " : "";
                    prefixQuery = prefixQuery + "msisdn like '+" + prefixList.get(i) + "%' " + OR;
                }
                System.out.println(new java.util.Date() + ":network_prefixQuery:" + prefixQuery);


                query = "select count(distinct(msisdn)) as num_subs from service_subscription where keyword in(select d.keyword from service_definition d where d.is_basic=1 and d.account_id='" + accountId + "' and d.service_type=15 and d.command=2) and account_id='" + accountId + "' and next_subscription_date='" + nextSubscriptionDate + "'" + " and billing_type='" + 2 + "' and status='" + status + "' and (" + prefixQuery + ")";


                prepstat = con.prepareStatement(query);
                subs = prepstat.executeQuery();

                int numSubs = 0;
                while (subs.next()) {
                    numSubs = subs.getInt("num_subs");
                }
                prepstat = null;
                subs = null;


                query = "select distinct(msisdn) from service_subscription where keyword in(select d.keyword from service_definition d where d.is_basic=1 and d.account_id='" + accountId + "' and d.service_type=15 and d.command=2) and account_id='" + accountId + "' and next_subscription_date='" + nextSubscriptionDate + "'" + " and billing_type='" + 2 + "' and status='" + status + "' and (" + prefixQuery + ")";


                prepstat = con.prepareStatement(query);
                subs = prepstat.executeQuery();


                String[] subscribers = new String[numSubs];


                int count = 0;
                while (subs.next()) {
                    subscribers[count] = subs.getString("msisdn");
                    count++;
                }
                if (subscribers.length > 0) {
                    System.out.println(new java.util.Date() + ":adding subscribers for network:" + prefix + ", No.Subscribers:" + subscribers.length);
                } else {
                    System.out.println(new java.util.Date() + ":No subscriber found for network:" + prefix);
                }
                groupedSubscribers.put(prefix, subscribers);


                prefix = null;
                subscribers = null;
            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return groupedSubscribers;
    }

    public static String findKeywordForMapping(String mapping, String accountId)
            throws Exception {
        String keyword = new String();

        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        UserService service = new UserService();
        try {
            con = DConnect.getConnection();

            String SQL = "select * from keyword_mapping where mapping='" + mapping + "' and account_id='" + accountId + "'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            if (rs.next()) {
                keyword = rs.getString("keyword");
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

    public static void deleteTempSubscriptionRecord(String msisdn, String accountId, String processId)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            String SQL = "delete from temp_service_subscription where msisdn='" + msisdn + "' and account_id='" + accountId + "' and process_id='" + processId + "'";
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
            keyListBuffer.append("'" + (String) keywordIterator.next() + "'");
            if (keywordIterator.hasNext()) {
                keyListBuffer.append(",");
            }
        }
        return keyListBuffer.toString();
    }

    public static String viewNextSubscriptionDate(String msisdn, String accountId, String keyword)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        String nextSubsDate = null;


        System.out.println(new java.util.Date() + ":@com.rancard.mobility.infoserver.common.services.UserServiceDB...");
        System.out.println(new java.util.Date() + ": retrieving next subscription date for (" + msisdn + ", " + accountId + ", " + keyword + ")...");
        try {
            con = DConnect.getConnection();

            String SQL = "select next_subscription_date from service_subscription where msisdn = ? and account_id = ? and keyword = ? ";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, msisdn);
            prepstat.setString(2, accountId);
            prepstat.setString(3, keyword);

            rs = prepstat.executeQuery();
            while (rs.next()) {
                nextSubsDate = rs.getString("next_subscription_date");


                System.out.println(new java.util.Date() + ": next subscription date (" + msisdn + ", " + accountId + ", " + keyword + ") found!");
            }
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
            System.out.println(new java.util.Date() + ": error retrieving last requested keyword (" + msisdn + ", " + accountId + ", " + keyword + "): " + ex.getMessage());
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
        return nextSubsDate;
    }

    public static void createFeed(Feed feedItem) {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            String SQL = "insert into feeds (feed_id, feed_name, feed_url, is_active, username, password) values(?, ?, ?, ?, ?, ?)";


            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, feedItem.getFeedId());
            prepstat.setString(2, feedItem.getFeedName());
            prepstat.setString(3, feedItem.getFeedURL().toString());
            prepstat.setInt(4, feedItem.isActive() ? 1 : 0);
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

    public static Feed viewFeed(String feedId)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        Feed feed = new Feed();
        try {
            con = DConnect.getConnection();

            String SQL = "select * from feeds where feed_id = ?";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, feedId);

            rs = prepstat.executeQuery();
            while (rs.next()) {
                feed.setFeedId(rs.getString("feed_id"));
                feed.setFeedName(rs.getString("feed_name"));
                feed.setFeedURL(rs.getURL("feed_url"));
                feed.setActive(rs.getInt("is_active") == 1);
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
        return feed;
    }

    public static void deleteFeed(String feedId)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();

            String SQL = "delete from feeds where feed_id = ?";

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

    public static void deleteFeed(ArrayList feedIds) {
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
            String SQL = "delete from feeds where feed_id in (" + keywordStr + ")";
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

    public static void updateFeed(String update_feed_id, Feed feedItem)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;


        String feed_id = (feedItem.getFeedId() != null) && (!feedItem.getFeedId().equals("")) ? "'" + feedItem.getFeedId() + "'" : "feed_id";
        String feed_name = (feedItem.getFeedName() != null) && (!feedItem.getFeedName().equals("")) ? "'" + feedItem.getFeedName() + "'" : "feed_name";
        String feed_url = (feedItem.getFeedURL() != null) && (!feedItem.getFeedURL().equals("")) ? "'" + feedItem.getFeedURL() + "'" : "feed_url";
        String is_active = feedItem.isActive() ? "1" : "0";
        String username = (feedItem.getUsername() != null) && (!feedItem.getUsername().equals("")) ? "'" + feedItem.getUsername() + "'" : "username";
        String password = (feedItem.getPassword() != null) && (!feedItem.getPassword().equals("")) ? "'" + feedItem.getPassword() + "'" : "password";
        try {
            con = DConnect.getConnection();
            String SQL = "UPDATE feeds SET feed_id = " + feed_id + ", feed_name = " + feed_name + ", feed_url = " + feed_url + "," + "is_active = " + is_active + ", username = " + username + ", password = " + password + " WHERE feed_id = ?";


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

    public static void createCPUserFeedAssociation(CPUserFeeds cpUserFeed) {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            String SQL = "insert into cp_user_feeds (account_id, keyword, feed_id, allowed_age, regex_reject, msg_dlr_priority) values(?, ?, ?, ?, ?, ?)";


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

    public static CPUserFeeds viewCPUserFeed(String account_id, String keyword)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        CPUserFeeds cpUserFeed = new CPUserFeeds();
        try {
            con = DConnect.getConnection();

            String SQL = "select * from cp_user_feeds where account_id = ? and keyword = ? ";

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
        return cpUserFeed;
    }

    public static void deleteCPUserFeed(String account_id, String keyword)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();

            String SQL = "delete from cp_user_feeds where account_id = ? and keyword = ?";

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

    public static void deleteCPUserFeed(String accountId, ArrayList keywords)
            throws Exception {
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
            String SQL = "delete from cp_user_feeds where keyword in (" + keywordStr + ") and account_id='" + accountId + "'";
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

    public static void updateCPUserFeed(String update_account_id, String update_keyword, CPUserFeeds cpUserFeed)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;


        String account_id = (cpUserFeed.getCpUserId() != null) && (!cpUserFeed.getCpUserId().equals("")) ? "'" + cpUserFeed.getCpUserId() + "'" : "account_id";
        String keyword = (cpUserFeed.getKeyword() != null) && (!cpUserFeed.getKeyword().equals("")) ? "'" + cpUserFeed.getKeyword() + "'" : "keyword";
        String feed_id = (cpUserFeed.getFeedId() != null) && (!cpUserFeed.getFeedId().equals("")) ? "'" + cpUserFeed.getFeedId() + "'" : "feed_id";
        String allowed_age = cpUserFeed.getAllowedAge() != 0 ? "'" + cpUserFeed.getAllowedAge() + "'" : "allowed_age";
        String regex_reject = (cpUserFeed.getRegexReject() != null) && (!cpUserFeed.getRegexReject().equals("")) ? "'" + cpUserFeed.getRegexReject() + "'" : "regex_reject";
        String msg_dlr_priority = cpUserFeed.getMsgDlrPrority() != 0 ? "'" + cpUserFeed.getMsgDlrPrority() + "'" : "msg_dlr_priority";
        try {
            con = DConnect.getConnection();
            String SQL = "UPDATE cp_user_feeds SET account_id = " + account_id + ", keyword = " + keyword + ", feed_id = " + feed_id + "," + "allowed_age = " + allowed_age + ", regex_reject = " + regex_reject + ", msg_dlr_priority = " + msg_dlr_priority + " WHERE account_id = ? and keyword = ? ";


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

    public static void createServiceLabel(String account_id, String keyword, String header, String footer) {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            String SQL = "insert into service_labels (account_id, keyword, header, footer)values(?, ?, ?, ?)";


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

    public static HashMap viewServiceLabel(String account_id, String keyword)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        HashMap serviceLabel = new HashMap();
        try {
            con = DConnect.getConnection();

            String SQL = "select * from service_labels where account_id = ? and keyword = ? ";

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
        return serviceLabel;
    }

    public static void deleteServiceLabel(String account_id, String keyword)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();

            String SQL = "delete from service_labels where account_id = ? and keyword = ?";

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

    public static void deleteServiceLabel(String account_id, ArrayList keywords)
            throws Exception {
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
            String SQL = "delete from service_labels where keyword in (" + keywordStr + ") and account_id='" + account_id + "'";
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

    public static void updateServiceLabel(String update_account_id, String update_keyword, String new_account_id, String new_keyword, String new_header, String new_footer)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;


        String account_id = (new_account_id != null) && (!new_account_id.equals("")) ? "'" + new_account_id + "'" : "account_id";
        String keyword = (new_keyword != null) && (!new_keyword.equals("")) ? "'" + new_keyword + "'" : "keyword";
        String header = (new_header != null) && (!new_header.equals("")) ? "'" + new_header + "'" : "header";
        String footer = (new_footer != null) && (!new_footer.equals("")) ? "'" + new_footer + "'" : "footer";
        try {
            con = DConnect.getConnection();
            String SQL = "UPDATE service_labels SET account_id = " + account_id + ", keyword = " + keyword + ", header = " + header + "," + "footer = " + footer + " WHERE account_id = ? and keyword = ? ";


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

    public static void createServiceForwarding(String account_id, String keyword, String url, long timeout, int listen_status) {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            String SQL = "insert into service_forwarding (account_id, keyword, url, timeout, listen_status)values(?, ?, ?, ?, ?)";


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

    public static HashMap viewServiceForwarding(String account_id, String keyword)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        HashMap serviceLabel = new HashMap();
        try {
            con = DConnect.getConnection();

            String SQL = "select * from service_forwarding where account_id = ? and keyword = ? ";

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
        return serviceLabel;
    }

    public static void deleteServiceForwarding(String account_id, String keyword)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();

            String SQL = "delete from service_forwarding where account_id = ? and keyword = ?";

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

    public static void deleteServiceForwarding(String account_id, ArrayList keywords)
            throws Exception {
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
            String SQL = "delete from service_forwarding where keyword in (" + keywordStr + ") and account_id='" + account_id + "'";
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

    public static void updateServiceForwarding(String update_account_id, String update_keyword, String new_account_id, String new_keyword, String new_url, String new_timeout, String new_listen_status)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;


        String account_id = (new_account_id != null) && (!new_account_id.equals("")) ? "'" + new_account_id + "'" : "account_id";
        String keyword = (new_keyword != null) && (!new_keyword.equals("")) ? "'" + new_keyword + "'" : "keyword";
        String url = (new_url != null) && (!new_url.equals("")) ? "'" + new_url + "'" : "url";
        String timeout = (new_timeout != null) && (!new_timeout.equals("")) ? "'" + new_timeout + "'" : "timeout";
        String listen_status = (new_listen_status != null) && (!new_listen_status.equals("")) ? "'" + new_listen_status + "'" : "listen_status";
        try {
            con = DConnect.getConnection();
            String SQL = "UPDATE service_forwarding SET account_id = " + account_id + ", keyword = " + keyword + ", url = " + url + "," + "timeout = " + timeout + ", listen_status = " + listen_status + " WHERE account_id = ? and keyword = ? ";


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
}
