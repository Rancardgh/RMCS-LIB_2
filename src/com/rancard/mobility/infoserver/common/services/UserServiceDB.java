package com.rancard.mobility.infoserver.common.services;

import com.rancard.common.AddressBook;
import com.rancard.common.AddressBookDB;
import com.rancard.common.DConnect;
import com.rancard.common.Feedback;
import com.rancard.common.uidGen;
import com.rancard.mobility.common.ThreadedPostman;
import com.rancard.mobility.infoserver.feeds.CPUserFeeds;
import com.rancard.util.DateUtil;
import com.rancard.util.Page;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UserServiceDB {

    private static void log(String level, String message) {
        System.out.println(new Date() + "\t" + UserServiceDB.class.getName() + "\t" + level + "\t" + message);
    }

    public static void createService(UserService service) throws Exception {
        Connection conn = null;

        try {
            conn = DConnect.getConnection();

            String sql = "Insert into service_definition(service_type, keyword, account_id, service_name, default_message, "
                    + "command, allowed_shortcodes, allowed_site_types, is_basic, pricing, is_subscription, service_response_sender) "
                    + "values('" + service.getServiceType() + "', '" + service.getKeyword() + "', '" + service.getAccountId() + "', "
                    + "'" + service.getServiceName() + "', '" + service.getDefaultMessage() + "', '" + service.getCommand() + "', "
                    + "'" + service.getAllowedShortcodes() + "', '" + service.getAllowedSiteTypes() + "', " + ((service.isBasic()) ? 1 : 0) + ", "
                    + "'" + service.getPricing() + "', " + ((service.isSubscription()) ? 1 : 0) + " , '" + service.getServiceResponseSender() + "')";

            log("DEBUG", "About to create service: " + sql);
            conn.createStatement().execute(sql);
        } catch (Exception ex) {
            log("ERROR", "Problem creating service: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

    }

    public static void updateService(String serviceType, String defaultMessage, String serviceName, String keyword, String accountId) throws Exception {
        Connection conn = null;

        try {
            conn = DConnect.getConnection();
            String sql = "UPDATE service_definition SET default_message = '" + defaultMessage + "', service_name = '" + serviceName + "' , "
                    + "last_updated = '" + DateUtil.convertToMySQLTimeStamp(new Date()) + "' "
                    + "WHERE keyword = '" + keyword + "' and account_id = '" + accountId + "' and service_type = '" + serviceType + "'";


            log("DEBUG", "About to update service: " + sql);
            conn.createStatement().executeUpdate(sql);
            log("INFO", "Service updated");
        } catch (Exception ex) {
            log("ERROR", "Problem updating service: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

    }

    public static void updateService(String defaultMessage, String keyword, String accountId) throws Exception {
        Connection conn = null;

        try {
            conn = DConnect.getConnection();
            String sql = "UPDATE service_definition SET default_message = '" + defaultMessage + "', "
                    + "last_updated = '" + DateUtil.convertToMySQLTimeStamp(new Date()) + "' "
                    + "WHERE keyword = '" + keyword + "' and account_id = '" + accountId + "'";

            log("DEBUG", "About to update service: " + sql);
            conn.createStatement().executeUpdate(sql);
            log("INFO", "Service updated");
        } catch (Exception ex) {
            log("ERROR", "Problem updating service: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static void deleteService(String keyword, String accountId) throws Exception {
        Connection conn = null;

        try {
            conn = DConnect.getConnection();
            String sql = "delete from service_definition where keyword = '" + keyword + "' and account_id = '" + accountId + "'";

            log("DEBUG", "About to delete service: " + sql);
            conn.createStatement().execute(sql);
            log("INFO", "Service deleted");
        } catch (Exception ex) {
            log("ERROR", "Problem deleting service: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static void deleteService(List<String> keywords, String accountId) throws Exception {
        if (keywords.isEmpty()) {
            return;
        }

        Connection conn = null;

        StringBuilder keywordStr = new StringBuilder();
        for (String keyword : keywords) {
            keywordStr.append("'").append(keyword).append("',");
        }
        keywordStr.deleteCharAt(keywordStr.toString().lastIndexOf(","));

        try {
            conn = DConnect.getConnection();
            String sql = "delete from service_definition where keyword in (" + keywordStr + ") and account_id = '" + accountId + "'";

            log("DEBUG", "About to delete service: " + sql);
            conn.createStatement().execute(sql);
            log("INFO", "Service deleted");
        } catch (Exception ex) {
            log("ERROR", "Problem deleting service: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static int getLastAccessCount(String msisdn, String accountId, String keyword) throws Exception {
        ResultSet rs = null;
        Connection conn = null;

        int accessCount = 0;

        try {
            conn = DConnect.getConnection();

            String sql = "select count(*) as 'access_count' from subscriber_request_history where msisdn = '" + msisdn + "' "
                    + "and account_id = '" + accountId + "' and keyword = '" + keyword + "' and date(log_time) = CURRENT_DATE order by log_time desc";

            log("DEBUG", "Getting last access count: " + sql);
            rs = conn.createStatement().executeQuery(sql);
            log("INFO", "Got last access count");
            if (rs.next()) {
                log("INFO", "Access count for last service (" + accountId + ", " + keyword + ") by " + msisdn);
                accessCount = rs.getInt("access_count");
            }
            return accessCount;

        } catch (Exception ex) {
            log("ERROR", "retrieving access count for last service (" + accountId + ", " + keyword + ") by " + msisdn + " :: " + ex.getMessage());
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

    public static String viewLastRequestedKeyword(String msisdn, String accountId, String siteId) throws Exception {
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DConnect.getConnection();

            String sql = "select * from subscriber_request_history where msisdn = '" + msisdn + "' and account_id = '" + accountId + "' "
                    + "and site_id = '" + siteId + "' order by log_time desc limit 1";

            log("DEBUG", "Getting last requested keyword: " + sql);
            rs = conn.createStatement().executeQuery(sql);

            if (rs.next()) {
                log("INFO", "Got last requested keyword");
                return rs.getString("keyword");
            }

            log("INFO", "Keyword not found");
            return null;
        } catch (Exception ex) {
            log("ERROR", "Retrieving last requested keyword (" + msisdn + ", " + accountId + ", " + siteId + "): " + ex.getMessage());
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

    //This checks for the most recently subscribed keyword with respect to a giving msisdn
    public static String viewLastSubscribedKeyword(String msisdn, String accountId) throws Exception {
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DConnect.getConnection();

            String sql = "select * from service_subscription where msisdn = '" + msisdn + "' and account_id = '" + accountId + "' "
                    + "order by subscription_date desc limit 1";

            log("DEBUG", "Retrieving last subscribed keyword for (" + msisdn + ", " + accountId + ")...");
            rs = conn.createStatement().executeQuery(sql);

            if (rs.next()) {
                log("INFO", "Got last requested keyword");
                return rs.getString("keyword");
            }

            log("INFO", "Keyword not found");
            return null;
        } catch (Exception ex) {
            log("ERROR", "Retrieving last requested keyword (" + msisdn + ", " + accountId + "): " + ex.getMessage());
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

    public static UserService viewLastServiceSubscribedTo(String msisdn) throws Exception {
        Connection conn = null;
        ResultSet rs = null;

        try {
            conn = DConnect.getConnection();
            String query = "select d.* from service_definition d inner join service_subscription s on d.account_id=s.account_id and d.keyword=s.keyword"
                    + " where msisdn='" + msisdn + "' order by s.subscription_date desc limit 1;";

            log("DEBUG", "SQL Query for last service subscribed to: " + query);
            rs = conn.createStatement().executeQuery(query);

            if (rs.next()) {
                log("INFO", "Service found");
                return new UserService(rs.getString("service_type"), rs.getString("keyword"), rs.getString("account_id"),
                        rs.getString("service_name"), rs.getString("default_message"), rs.getString("command"),
                        Arrays.asList(rs.getString("allowed_shortcodes").split(",")), Arrays.asList(rs.getString("allowed_site_types").split(",")),
                        rs.getString("pricing"), rs.getBoolean("is_basic"), rs.getBoolean("is_subscription"),
                        rs.getString("service_response_sender"), DateUtil.convertFromMySQLTimeStamp(rs.getString("last_updated")));
            }

            log("INFO", "Service not found");
            return null;
        } catch (Exception e) {
            log("ERROR", "Retrieving service: " + e.getMessage());
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

    // Takes in a keyword alias and an accountId and returns the exact keyword for the 
    // specified alias
    public static UserService viewServiceByAlias(String alias, String accountId) throws Exception {
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DConnect.getConnection();

            String sql = "select * from keyword_aliases ka inner join service_definition sd on sd.keyword = ka.keyword and sd.account_id = ka.account_id "
                    + "where ka.key_alias = '" + alias + "' and ka.account_id = '" + accountId + "'";
            log("DEBUG", "SQL Query for last service subscribed to: " + sql);
            rs = conn.createStatement().executeQuery(sql);

            if (rs.next()) {
                log("INFO", "Service found");
                return new UserService(rs.getString("service_type"), rs.getString("keyword"), rs.getString("account_id"),
                        rs.getString("service_name"), rs.getString("default_message"), rs.getString("command"),
                        Arrays.asList(rs.getString("allowed_shortcodes").split(",")), Arrays.asList(rs.getString("allowed_site_types").split(",")),
                        rs.getString("pricing"), rs.getBoolean("is_basic"), rs.getBoolean("is_subscription"),
                        rs.getString("service_response_sender"), DateUtil.convertFromMySQLTimeStamp("s.last_updated"));
            }

            log("INFO", "Service not found");
            return null;
        } catch (Exception ex) {
            log("ERROR", "Retrieving service: " + ex.getMessage());
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

    public static UserService viewService(String keyword, String accountId) throws Exception {
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DConnect.getConnection();

            String sql = "select * from service_definition where keyword = '" + keyword + "' and account_id = '" + accountId + "'";
            System.out.println(new java.util.Date() + ": " + UserServiceDB.class + "DEBUG: SQL Query to get service: " + sql);


            rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                log("INFO", "Service found");
                return new UserService(rs.getString("service_type"), rs.getString("keyword"), rs.getString("account_id"),
                        rs.getString("service_name"), rs.getString("default_message"), rs.getString("command"),
                        Arrays.asList(rs.getString("allowed_shortcodes").split(",")), Arrays.asList(rs.getString("allowed_site_types").split(",")),
                        rs.getString("pricing"), rs.getBoolean("is_basic"), rs.getBoolean("is_subscription"),
                        rs.getString("service_response_sender"), DateUtil.convertFromMySQLTimeStamp(rs.getString("last_updated")));
            }

            System.out.println(new java.util.Date() + ": " + UserServiceDB.class + ":INFO: Service not found");
            return null;

        } catch (Exception ex) {
            System.out.println(new java.util.Date() + ": " + UserServiceDB.class + ": error viewing service (" + keyword + ", " + accountId + "): " + ex.getMessage());
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

    public static List<UserService> viewService(List<String> keywords, String accountId) throws Exception {
        List<UserService> services = new ArrayList<UserService>();
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DConnect.getConnection();

            String sql = "select * from service_definition where keyword IN (" + stitchKeywords(keywords) + ") and account_id = '" + accountId + "'";
            System.out.println(new java.util.Date() + ": " + UserServiceDB.class + "DEBUG: SQL Query to get services: " + sql);


            rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                System.out.println(new java.util.Date() + ": " + UserServiceDB.class + "INFO: Service found");
                services.add(new UserService(rs.getString("service_type"), rs.getString("keyword"), rs.getString("account_id"),
                        rs.getString("service_name"), rs.getString("default_message"), rs.getString("command"),
                        Arrays.asList(rs.getString("allowed_shortcodes").split(",")), Arrays.asList(rs.getString("allowed_site_types").split(",")),
                        rs.getString("pricing"), rs.getBoolean("is_basic"), rs.getBoolean("is_subscription"),
                        rs.getString("service_response_sender"), DateUtil.convertFromMySQLTimeStamp(rs.getString("last_updated"))));
            }

            return services;

        } catch (Exception ex) {
            System.out.println(new java.util.Date() + ": " + UserServiceDB.class + ": error viewing services (" + stitchKeywords(keywords) + ", " + accountId + "): " + ex.getMessage());
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

    public static List<UserService> viewAllServices(String accountId) throws Exception {
        ResultSet rs = null;
        Connection conn = null;

        List<UserService> serviceList = new ArrayList<UserService>();
        try {
            conn = DConnect.getConnection();
            String sql = "select * from service_definition where account_id = '" + accountId + "'";
            System.out.println(new java.util.Date() + ": " + UserServiceDB.class + "DEBUG: SQL Query to get services: " + sql);

            rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                serviceList.add(new UserService(rs.getString("service_type"), rs.getString("keyword"), rs.getString("account_id"),
                        rs.getString("service_name"), rs.getString("default_message"), rs.getString("command"),
                        Arrays.asList(rs.getString("allowed_shortcodes").split(",")), Arrays.asList(rs.getString("allowed_site_types").split(",")),
                        rs.getString("pricing"), rs.getBoolean("is_basic"), rs.getBoolean("is_subscription"),
                        rs.getString("service_response_sender"), DateUtil.convertFromMySQLTimeStamp(rs.getString("last_updated"))));
            }
            return serviceList;
        } catch (Exception ex) {
            System.out.println(new java.util.Date() + ": " + UserServiceDB.class + ":ERROR viewing service " + accountId + ": " + ex.getMessage());
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

    public static List<UserService> viewAllServices(String accountId, String serviceType) throws Exception {
        ResultSet rs = null;
        Connection conn = null;

        List<UserService> serviceList = new ArrayList<UserService>();
        try {
            conn = DConnect.getConnection();
            String sql = "select * from service_definition where account_id ='" + accountId + "' and service_type= '" + serviceType + "'";

            System.out.println(new java.util.Date() + ": " + UserServiceDB.class + "DEBUG: SQL Query to get services: " + sql);
            rs = conn.createStatement().executeQuery(sql);

            while (rs.next()) {
                serviceList.add(new UserService(rs.getString("service_type"), rs.getString("keyword"), rs.getString("account_id"),
                        rs.getString("service_name"), rs.getString("default_message"), rs.getString("command"),
                        Arrays.asList(rs.getString("allowed_shortcodes").split(",")), Arrays.asList(rs.getString("allowed_site_types").split(",")),
                        rs.getString("pricing"), rs.getBoolean("is_basic"), rs.getBoolean("is_subscription"), rs.getString("service_response_sender"),
                        DateUtil.convertFromMySQLTimeStamp(rs.getString("last_updated"))));
            }

            return serviceList;
        } catch (Exception ex) {
            System.out.println(new java.util.Date() + ": " + UserServiceDB.class + ":ERROR viewing service " + accountId + "-" + serviceType + ": " + ex.getMessage());
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

    public static List<UserService> viewAllServices(String accountId, String serviceType, String command) throws Exception {
        ResultSet rs = null;
        Connection conn = null;

        List<UserService> serviceList = new ArrayList<UserService>();
        try {
            conn = DConnect.getConnection();
            String sql = "select * from service_definition where account_id = '" + accountId + "' and service_type= '" + serviceType + "' and command= '" + command + "'";
            System.out.println(new java.util.Date() + ": " + UserServiceDB.class + "DEBUG: SQL Query to get services: " + sql);

            rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                serviceList.add(new UserService(rs.getString("service_type"), rs.getString("keyword"), rs.getString("account_id"),
                        rs.getString("service_name"), rs.getString("default_message"), rs.getString("command"),
                        Arrays.asList(rs.getString("allowed_shortcodes").split(",")), Arrays.asList(rs.getString("allowed_site_types").split(",")),
                        rs.getString("pricing"), rs.getBoolean("is_basic"), rs.getBoolean("is_subscription"), rs.getString("service_response_sender"),
                        DateUtil.convertFromMySQLTimeStamp(rs.getString("last_updated"))));
            }
            return serviceList;
        } catch (Exception ex) {
            System.out.println(new java.util.Date() + ": " + UserServiceDB.class + ":ERROR viewing service " + accountId + "-" + serviceType + "-" + command + ": " + ex.getMessage());
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

    public static List<UserService> viewAllServicesOfParentType(String accountId, String parentType) throws Exception {
        ResultSet rs = null;
        Connection conn = null;

        List<UserService> serviceList = new ArrayList<UserService>();
        try {
            conn = DConnect.getConnection();
            String sql = "select * from service_definition sd, service_route sr where account_id = '" + accountId + "'"
                    + " and sd.service_type=sr.service_type and sr.parent_service_type= '" + parentType + "' order by sd.service_name;";
            System.out.println(new java.util.Date() + ": " + UserServiceDB.class + "DEBUG: SQL Query to get services: " + sql);

            rs = conn.createStatement().executeQuery(sql);

            while (rs.next()) {
                serviceList.add(new UserService(rs.getString("service_type"), rs.getString("keyword"), rs.getString("account_id"),
                        rs.getString("service_name"), rs.getString("default_message"), rs.getString("command"),
                        Arrays.asList(rs.getString("allowed_shortcodes").split(",")), Arrays.asList(rs.getString("allowed_site_types").split(",")),
                        rs.getString("pricing"), rs.getBoolean("is_basic"), rs.getBoolean("is_subscription"),
                        rs.getString("service_response_sender"), DateUtil.convertFromMySQLTimeStamp(rs.getString("last_updated"))));
            }
            return serviceList;
        } catch (Exception ex) {
            System.out.println(new java.util.Date() + ": " + UserServiceDB.class + ":ERROR viewing service " + accountId + "-" + parentType + ": " + ex.getMessage());
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

    public static List<UserService> viewAllServicesForType(String serviceType) throws Exception {
        ResultSet rs = null;
        Connection conn = null;

        List<UserService> serviceList = new ArrayList<UserService>();
        try {
            conn = DConnect.getConnection();
            String sql = "select * from service_definition where service_type = '" + serviceType + "' order by account_id";
            System.out.println(new java.util.Date() + ": " + UserServiceDB.class + "DEBUG: SQL Query to get services: " + sql);

            rs = conn.createStatement().executeQuery(sql);

            while (rs.next()) {
                serviceList.add(new UserService(rs.getString("service_type"), rs.getString("keyword"), rs.getString("account_id"),
                        rs.getString("service_name"), rs.getString("default_message"), rs.getString("command"),
                        Arrays.asList(rs.getString("allowed_shortcodes").split(",")), Arrays.asList(rs.getString("allowed_site_types").split(",")),
                        rs.getString("pricing"), rs.getBoolean("is_basic"), rs.getBoolean("is_subscription"),
                        rs.getString("service_response_sender"), DateUtil.convertFromMySQLTimeStamp(rs.getString("last_updated"))));
            }
            return serviceList;
        } catch (Exception ex) {
            System.out.println(new java.util.Date() + ": " + UserServiceDB.class + ":ERROR viewing service " + serviceType + ": " + ex.getMessage());
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

    public static Map<String, String> populateRoutingTable() throws Exception {
        Map<String, String> routingTable = new HashMap<String, String>();
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DConnect.getConnection();
            String sql = "select service_type, service_url from service_route";
            System.out.println(new Date() + "\t[" + UserServiceDB.class + "]\tDEBUG\tGetting routing table: " + sql);

            rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                routingTable.put(rs.getString("service_type"), rs.getString("service_url"));
            }
            return routingTable;
        } catch (Exception ex) {
            System.out.println(new Date() + "\t[" + UserServiceDB.class + "]\tERROR\tGetting routing table: " + ex.getMessage());
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

    public static Map<String, String> getServiceTable() throws Exception {
        Map<String, String> serviceTable = new HashMap<String, String>();
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DConnect.getConnection();
            String sql = "select service_type, service_name from service_route";
            System.out.println(new java.util.Date() + ": " + UserServiceDB.class + "DEBUG: Getting service table: " + sql);
            rs = conn.createStatement().executeQuery(sql);

            while (rs.next()) {
                serviceTable.put(rs.getString("service_type"), rs.getString("service_name"));
            }
            return serviceTable;
        } catch (Exception ex) {
            System.out.println(new java.util.Date() + ": " + UserServiceDB.class + ":ERROR getting service table: " + ex.getMessage());
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

    public static List<String> getCPIDsForServiceType(String serviceType) throws Exception {
        List<String> struct = new ArrayList<String>();
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DConnect.getConnection();

            String query = "SELECT distinct(account_id) from service_definition where service_type='" + serviceType + "'";
            System.out.println(new java.util.Date() + ": " + UserServiceDB.class + "DEBUG: Getting cp ids for service types: " + query);

            rs = conn.createStatement().executeQuery(query);

            while (rs.next()) {
                struct.add(rs.getString("account_id"));
            }
            return struct;
        } catch (Exception ex) {
            System.out.println(new java.util.Date() + ": " + UserServiceDB.class + ":ERROR getting cp ids for service types: " + ex.getMessage());
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

    public static Map<String, String> getCPIDsForServiceType(String serviceType, String command) throws Exception {
        Map<String, String> struct = new HashMap<String, String>();
        ResultSet rs = null;
        Connection conn = null;


        String query = "SELECT account_id, keyword from service_definition where service_type='" + serviceType + "' and command = '" + command + "'";
        System.out.println(new java.util.Date() + ": " + UserServiceDB.class + "DEBUG: Getting cp ids for service types: " + query);

        try {
            conn = DConnect.getConnection();
            rs = conn.createStatement().executeQuery(query);

            while (rs.next()) {
                struct.put(rs.getString("account_id"), rs.getString("keyword"));
            }
            return struct;
        } catch (Exception ex) {
            System.out.println(new Date() + ": " + UserServiceDB.class + "DEBUG: looking for CPIds: " + ex.getMessage());
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

    public static void updateSubscriberRequestHistory(String msisdn, String keyword, String accountID, String site_id, Date now, int count) throws Exception {
        Connection conn = null;

        try {
            conn = DConnect.getConnection();

            String sql = "UPDATE subscriber_request_history SET count = " + count + " where keyword = '" + keyword + "' and account_id = '" + accountID
                    + "' and msisdn = '" + msisdn + "' and site_id = '" + site_id + "'";
            System.out.println(new Date() + ": " + UserServiceDB.class + "DEBUG: Updating service subscription history: " + sql);

            int update = conn.createStatement().executeUpdate(sql);
            if (update == 0) {
                sql = "INSERT INTO subscriber_request_history values ('" + msisdn + "', '" + keyword + "', '" + accountID + "', '" + site_id + "', '" + DateUtil.convertToMySQLTimeStamp(now) + "', 1)";
                System.out.println(new Date() + ": " + UserServiceDB.class + "DEBUG: Record did not exist in subscriber_request_history. Will insert new record: " + sql);
                conn.createStatement().execute(sql);
            } else {
                System.out.println(new Date() + ": " + UserServiceDB.class + "INFO: updated subscriber_request_history");
            }

        } catch (Exception e) {
            System.out.println(new Date() + ": " + UserServiceDB.class + "ERROR: Updating service subscription history: " + e.getMessage());
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

        try {

            conn = DConnect.getConnection();
            String sql = "SELECT count FROM subscriber_request_history where keyword = '" + keyword + "' and account_id = '" + accountID
                    + "' and msisdn = '" + msisdn + "' and site_id = '" + site_id + "'";
            System.out.println(new Date() + ": " + UserServiceDB.class + "Getting subscriber request count: " + sql);

            rs = conn.createStatement().executeQuery(sql);
            if (rs.first()) {
                return rs.getInt(1);
            } else {
                return 0;
            }

        } catch (Exception e) {
            System.out.println(new Date() + ": " + UserServiceDB.class + "ERROR: Getting subscriber request count: " + e.getMessage());
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

    public static boolean stoppedB4SubscriptionEnded(String msisdn, String keyword, String accountID, Date now) throws Exception {
        Connection conn = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM service_subscription_deleted where keyword = '" + keyword + "' and account_id = '" + accountID
                + "' and msisdn = '" + msisdn + "' and subscription_date < '" + DateUtil.convertToMySQLTimeStamp(now) + "' and next_subscription_date > '" + DateUtil.convertToMySQLTimeStamp(now)
                + "' order by unsubscription_date DESC limit 1";
        System.out.println(new Date() + ": " + UserServiceDB.class + "Check if user unsubscribed recently: " + sql);
        try {

            conn = DConnect.getConnection();

            rs = conn.createStatement().executeQuery(sql);
            if (rs != null) {
                if (rs.first()) {
                    System.out.println(new Date() + ": " + UserServiceDB.class + ":INFO Yes they have unsubscribed recently");
                    return true;

                } else {
                    System.out.println(new Date() + ": " + UserServiceDB.class + ":INFO  No they have not unsubscribed recently");
                    return false;
                }

            } else {
                System.out.println(new Date() + ": " + UserServiceDB.class + ":INFO Yes they have unsubscribed recently");
                return false;
            }
        } catch (Exception e) {
            System.out.println(new Date() + ": " + UserServiceDB.class + "ERROR: Checking if user unsubscribed recently: " + e.getMessage());
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
        Connection conn = null;

        try {
            conn = DConnect.getConnection();

            System.out.println(new Date() + ": " + UserServiceDB.class + ":INFO Will check that service exists");
            UserService user = viewService(keyword, accountId);

            if (user != null) {
                System.out.println(new Date() + ": " + UserServiceDB.class + ":INFO Yes service exists");
                String sql = "update service_subscription set status = " + status + " where keyword = '" + keyword + "' and account_id = '" + accountId + "' and msisdn = '" + msisdn + "'";

                System.out.println(new Date() + ": " + UserServiceDB.class + ":DEBUG Will update subscription: " + sql);

                int update = conn.createStatement().executeUpdate(sql);
                if (update < 1) {
                    System.out.println(new Date() + ": " + UserServiceDB.class + "ERROR: Updating subscription. User not registered");
                    throw new Exception(Feedback.NOT_REGISTERED);
                }
            } else {
                System.out.println(new Date() + ": " + UserServiceDB.class + "ERROR: Updating subscription. Service does not exist " + accountId + "-" + keyword);
                throw new Exception(Feedback.NO_SUCH_SERVICE);
            }
        } catch (Exception ex) {
            System.out.println(new Date() + ": " + UserServiceDB.class + "ERROR: Updating service subscription: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

    }

    public static void subscribeToService(String msisdn, String keyword, String accountId) throws Exception {
        Connection conn = null;
        PreparedStatement prepstat = null;

        try {
            AddressBook addressBook = AddressBookDB.getAddress(accountId, msisdn);

            if (addressBook == null) {
                AddressBookDB.save(new AddressBook(accountId, msisdn, uidGen.generateNumberID(6)));
            }

            conn = DConnect.getConnection();

            String sql = "INSERT INTO service_subscription (subscription_date,msisdn,keyword,account_id,status) VALUES(?,?,?,?,?)";
            prepstat = conn.prepareStatement(sql);


            UserService user = viewService(keyword, accountId);
            if (user != null) {
                Date now = new Date();
                prepstat.setString(1, DateUtil.convertToMySQLTimeStamp(now));
                prepstat.setString(2, msisdn);
                prepstat.setString(3, keyword);
                prepstat.setString(4, accountId);
                prepstat.setInt(5, 1);

                System.out.println(new Date() + ": " + UserServiceDB.class + ":DEBUG Create service: " + prepstat.toString());
                prepstat.execute();

                Map<String, String> params = new HashMap<String, String>();
                params.put("msisdn", msisdn.substring(msisdn.indexOf("+") + 1));
                params.put("keyword", keyword);
                new Thread(new ThreadedPostman(ThreadedPostman.RNDVU_BUY_USER_ACTION_API_TMPLT, params)).start();
            } else {
                System.out.println(new Date() + ": " + UserServiceDB.class + "ERROR: Creating service. Service does not exist: " + accountId + "-" + keyword);
                throw new Exception(Feedback.NO_SUCH_SERVICE);
            }

        } catch (Exception e) {
            System.out.println(new Date() + ": " + UserServiceDB.class + "ERROR: Creating service subscription: " + e.getMessage());
            throw new Exception(e.getMessage());
        } finally {
            if (prepstat != null) {
                prepstat.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static void subscribeToService(String msisdn, List<String> keywords, String accountId) throws Exception {
        Connection conn = null;
        PreparedStatement prepstat = null;

        try {
            AddressBook addressBook = AddressBookDB.getAddress(accountId, msisdn);

            if (addressBook == null) {
                AddressBookDB.save(new AddressBook(accountId, msisdn, uidGen.generateNumberID(6)));
            }

            conn = DConnect.getConnection();

            String sql = "INSERT INTO service_subscription (subscription_date,msisdn,keyword,account_id,status) VALUES(?,?,?,?,?)";
            prepstat = conn.prepareStatement(sql);

            for (String keyword : keywords) {
                UserService user = viewService(keyword, accountId);
                if (user != null) {
                    Date now = new Date();
                    prepstat.setString(1, DateUtil.convertToMySQLTimeStamp(now));
                    prepstat.setString(2, msisdn);
                    prepstat.setString(3, keyword);
                    prepstat.setString(4, accountId);
                    prepstat.setInt(5, 1);

                    System.out.println(new Date() + ": " + UserServiceDB.class + ":DEBUG Create service: " + prepstat.toString());
                    prepstat.execute();

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("msisdn", msisdn.substring(msisdn.indexOf("+") + 1));
                    params.put("keyword", keyword);
                    new Thread(new ThreadedPostman(ThreadedPostman.RNDVU_BUY_USER_ACTION_API_TMPLT, params)).start();
                } else {
                    System.out.println(new Date() + ": " + UserServiceDB.class + "ERROR: Creating service. Service does not exist: " + accountId + "-" + keyword);
                    throw new Exception(Feedback.NO_SUCH_SERVICE);
                }
            }
        } catch (Exception e) {
            System.out.println(new Date() + ": " + UserServiceDB.class + "ERROR: Creating service subscription: " + e.getMessage());
            throw new Exception(e.getMessage());
        } finally {
            if (prepstat != null) {
                prepstat.close();
            }

            if (conn != null) {
                conn.close();
            }
        }
    }

    public static void subscribeToService(String msisdn, List<String> keywords, String accountId, int numOfDays) throws Exception {
        Connection conn = null;
        PreparedStatement prepstat = null;

        try {
            AddressBook addressBook = AddressBookDB.getAddress(accountId, msisdn);

            if (addressBook == null) {
                AddressBookDB.save(new AddressBook(accountId, msisdn, uidGen.generateNumberID(6)));
            }

            conn = DConnect.getConnection();

            String sql = "INSERT INTO service_subscription (subscription_date,msisdn,keyword,account_id,status,next_subscription_date) VALUES(?,?,?,?,?,?)";
            prepstat = conn.prepareStatement(sql);

            for (String keyword : keywords) {
                UserService user = viewService(keyword, accountId);
                if (user != null) {
                    Date now = new Date();
                    prepstat.setString(1, DateUtil.convertToMySQLTimeStamp(now));
                    prepstat.setString(2, msisdn);
                    prepstat.setString(3, keyword);
                    prepstat.setString(4, accountId);
                    prepstat.setInt(5, 1);
                    prepstat.setString(6, DateUtil.convertToMySQLTimeStamp(DateUtil.addDaysToDate(now, numOfDays)));

                    System.out.println(new Date() + ": " + UserServiceDB.class + ":DEBUG Create service: " + prepstat.toString());
                    prepstat.execute();

                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("msisdn", msisdn.substring(msisdn.indexOf("+") + 1));
                    params.put("keyword", keyword);
                    new Thread(new ThreadedPostman(ThreadedPostman.RNDVU_BUY_USER_ACTION_API_TMPLT, params)).start();
                } else {
                    System.out.println(new Date() + ": " + UserServiceDB.class + "ERROR: Creating service. Service does not exist: " + accountId + "-" + keyword);
                    throw new Exception(Feedback.NO_SUCH_SERVICE);
                }
            }
        } catch (Exception e) {
            System.out.println(new Date() + ": " + UserServiceDB.class + "ERROR: Creating service subscription: " + e.getMessage());
            throw new Exception(e.getMessage());
        } finally {
            if (prepstat != null) {
                prepstat.close();
            }

            if (conn != null) {
                conn.close();
            }
        }

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
                SQL = "INSERT INTO address_book (account_id,msisdn,registration_id) VALUES(?,?,?)";
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
                    SQL = "INSERT INTO service_subscription (subscription_date,msisdn,keyword,account_id,status,next_subscription_date,billing_type) VALUES(?,?,?,?,?,?,?)";
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

                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("msisdn", msisdn.substring(msisdn.indexOf("+") + 1));
                    params.put("keyword", keyword);
                    new Thread(new ThreadedPostman(ThreadedPostman.RNDVU_BUY_USER_ACTION_API_TMPLT, params)).start();
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
                    SQL = "INSERT INTO service_subscription (subscription_date,msisdn,keyword,account_id,status,next_subscription_date) VALUES(?,?,?,?,?,?)";
                    prepstat = con.prepareStatement(SQL);
                    prepstat.setTimestamp(1, new java.sql.Timestamp(subscriptionDate.getTime()));
                    prepstat.setString(2, msisdn);
                    prepstat.setString(3, keyword);
                    prepstat.setString(4, accountId);
                    prepstat.setInt(5, status);
                    prepstat.setTimestamp(6, new java.sql.Timestamp(nextSubscriptionDate.getTime()));
                    prepstat.execute();

                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("msisdn", msisdn.substring(msisdn.indexOf("+") + 1));
                    params.put("keyword", keyword);
                    new Thread(new ThreadedPostman(ThreadedPostman.RNDVU_BUY_USER_ACTION_API_TMPLT, params)).start();
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
            SQL = "delete from service_subscription where msisdn='" + msisdn + "' and keyword in (" + keywordStr.toString() + ") and account_id='" + accountId + "'";
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
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DConnect.getConnection();
            String sql = "select ab.registration_id from address_book ab inner join service_subscription ss on ab.msisdn=ss.msisdn and ab.account_id=ss.account_id where ab.msisdn='"
                    + msisdn + "' and ab.account_id='" + acctId + "' and ss.keyword='" + keyword + "'";
            System.out.println(new Date() + "\t[" + UserServiceDB.class + "]\tDEBUG\tVerifying user: " + sql);
            conn.createStatement().execute(sql);

            if (rs.next() && rs.getString("ab.registration_id").equals(regId)) {
                return true;
            }

            return false;
        } catch (Exception ex) {
            System.out.println(new Date() + "\t[" + UserServiceDB.class + "]\tERROR\tVerifying user: " + ex.getMessage());
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

    public static Map getSubscription(String msisdn, String accountID, String keyword, String alternativeKeyword) throws Exception {
        Map<String, Object> subscription = null;
        String SQL;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DConnect.getConnection();
            if (alternativeKeyword != null && !alternativeKeyword.trim().equals("")) {
                SQL = "select * from service_subscription where account_id = '" + accountID + "' and msisdn = '" + msisdn + "'"
                        + " and (keyword = '" + keyword + "' or keyword = '" + alternativeKeyword + "')";
            } else {
                SQL = "select * from service_subscription where account_id = '" + accountID + "' and msisdn = '" + msisdn + "'"
                        + " and keyword = '" + keyword + "'";
            }

            rs = conn.createStatement().executeQuery(SQL);

            if (rs.next()) {
                subscription = new HashMap<String, Object>();

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
            if (conn != null) {
                conn.close();
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
        Connection conn = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM service_subscription_deleted where keyword = '" + keyword + "' and account_id = '" + accountID
                + "' and msisdn = '" + msisdn + "' order by unsubscription_date DESC limit 1";
        System.out.println("Check if user ever unsubscribed : " + sql);
        try {

            conn = DConnect.getConnection();

            rs = conn.createStatement().executeQuery(sql);
            if (rs.next()) {
                return true;
            }

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

    public static List<CPUserFeeds> viewServiceRSSFeeds(String accountId, String keyword) throws Exception {

        List<CPUserFeeds> feedsList = new ArrayList<CPUserFeeds>();
        ResultSet rs = null;
        Connection conn = null;


        try {
            conn = DConnect.getConnection();

            String query = "select * from cp_user_feeds where account_id='" + accountId + "' and  keyword='" + keyword + "'";


            rs = conn.createStatement().executeQuery(query);

            // get the total number of records

            while (rs.next()) {
                CPUserFeeds feed = new CPUserFeeds(rs.getString("account_id"), rs.getString("keyword"), rs.getString("feed_id"),
                        Integer.parseInt(rs.getString("allowed_age")), rs.getString("regex_reject"), rs.getInt("msg_dlr_priority"));

                feedsList.add(feed);
            }

            return feedsList;
        } catch (Exception ex) {

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
                        + " and billing_type='" + ServiceSubscription.MONTHLY_BILLING + "' and status='" + status + "' and (" + prefixQuery + ")";


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
                        + " and billing_type='" + ServiceSubscription.MONTHLY_BILLING + "' and status='" + status + "' and (" + prefixQuery + ")";


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
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DConnect.getConnection();
            String sql = "select keyword from keyword_mapping where mapping='" + mapping + "' and account_id='" + accountId + "'";
            System.out.println(new Date() + "\t[" + UserServiceDB.class + "]\tDEBUG\tFind keyword for mapping: " + sql);

            rs = conn.createStatement().executeQuery(sql);

            if (rs.next()) {
                return rs.getString("keyword");
            }
            return null;
        } catch (Exception ex) {
            System.out.println(new Date() + "\t[" + UserServiceDB.class + "]\tERROR\tFind keyword for mapping: " + ex.getMessage());
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

            SQL = "SELECT next_subscription_date FROM service_subscription WHERE msisdn = ? AND account_id = ? AND keyword = ? ";

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
            SQL = "INSERT INTO feeds (feed_id, feed_name, feed_url, is_active, username, password) "
                    + "VALUES(?, ?, ?, ?, ?, ?)";

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

            SQL = "SELECT * FROM feeds WHERE feed_id = ?";

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

            SQL = "DELETE FROM feeds WHERE feed_id = ?";

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
            SQL = "INSERT INTO cp_user_feeds (account_id, keyword, feed_id, allowed_age, regex_reject, msg_dlr_priority) "
                    + "VALUES(?, ?, ?, ?, ?, ?)";

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

            SQL = "SELECT * FROM cp_user_feeds WHERE account_id = ? AND keyword = ? ";

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

            SQL = "DELETE FROM cp_user_feeds WHERE account_id = ? AND keyword = ?";

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
            SQL = "INSERT INTO service_labels (account_id, keyword, header, footer)"
                    + "VALUES(?, ?, ?, ?)";

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

            SQL = "SELECT * FROM service_labels WHERE account_id = ? AND keyword = ? ";

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

            SQL = "DELETE FROM service_labels WHERE account_id = ? AND keyword = ?";

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
        Connection conn = null;

        try {
            conn = DConnect.getConnection();
            String sql = "UPDATE service_labels SET account_id = '" + new_account_id + "', keyword = '" + new_keyword + "', header = '" + new_header + "',"
                    + "footer = '" + new_footer + "' WHERE account_id = '" + new_account_id + "' and keyword = '" + new_keyword + "'";

            System.out.println(new Date() + ": " + UserServiceDB.class + ":DEBUG Will update service label: " + sql);
            conn.createStatement().executeUpdate(sql);

        } catch (Exception ex) {
            System.out.println(new Date() + ": " + UserServiceDB.class + "ERROR: Updating service label: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {

            if (conn != null) {
                conn.close();
            }
        }
    }

    public static void createServiceForwarding(String account_id, String keyword, String url, long timeout, int listen_status) throws Exception {
        Connection conn = null;

        try {
            conn = DConnect.getConnection();
            String sql = "insert into service_forwarding (account_id, keyword, url, timeout, listen_status) values('" + account_id + "', "
                    + "'" + keyword + "', '" + url + "', " + timeout + ", " + listen_status + ")";

            System.out.println(new Date() + ": " + UserServiceDB.class + ":DEBUG Will insert service forwarding: " + sql);
            conn.createStatement().execute(sql);
        } catch (Exception ex) {
            System.out.println(new Date() + ": " + UserServiceDB.class + "ERROR: Inserting service forwarding: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static Map<String, String> viewServiceForwarding(String account_id, String keyword) throws Exception {
        ResultSet rs = null;
        Connection conn = null;

        Map<String, String> serviceLabel = new HashMap<String, String>();

        try {
            conn = DConnect.getConnection();

            String sql = "select * from service_forwarding where account_id = '" + account_id + "' and keyword = '" + keyword + "'";

            System.out.println(new Date() + ": " + UserServiceDB.class + ":DEBUG Will view service forwarding: " + sql);
            rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                serviceLabel.put("account_id", rs.getString("account_id"));
                serviceLabel.put("keyword", rs.getString("keyword"));
                serviceLabel.put("url", rs.getString("url"));
                serviceLabel.put("timeout", rs.getString("timeout"));
                serviceLabel.put("listen_status", rs.getString("listen_status"));
                break;
            }

            return serviceLabel;
        } catch (Exception ex) {
            System.out.println(new Date() + ": " + UserServiceDB.class + "ERROR: Viewing service forwarding: " + ex.getMessage());
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

    public static void deleteServiceForwarding(String account_id, String keyword) throws Exception {
        Connection conn = null;

        try {
            conn = DConnect.getConnection();

            String sql = "delete from service_forwarding where account_id = '" + account_id + "' and keyword = '" + keyword + "'";

            System.out.println(new Date() + ": " + UserServiceDB.class + ":DEBUG Will delete service forwarding: " + sql);
            conn.createStatement().execute(sql);

        } catch (Exception ex) {
            System.out.println(new Date() + ": " + UserServiceDB.class + "ERROR: Deleting service forwarding: " + ex.getMessage());
            throw new Exception(ex.getMessage());

        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static void deleteServiceForwarding(String account_id, List<String> keywords) throws Exception {
        Connection conn = null;

        StringBuilder keywordStr = new StringBuilder();
        for (int i = 0; i < keywords.size(); i++) {
            keywordStr.append("'").append(keywords.get(i).toString()).append("',");
        }
        keywordStr.deleteCharAt(keywordStr.toString().lastIndexOf(","));

        try {
            conn = DConnect.getConnection();
            String sql = "delete from service_forwarding where keyword in (" + keywordStr + ") and account_id = '" + account_id + "'";

            System.out.println(new Date() + ": " + UserServiceDB.class + ":DEBUG Will delete service forwarding: " + sql);
            conn.createStatement().execute(sql);
        } catch (Exception ex) {
            System.out.println(new Date() + ": " + UserServiceDB.class + "ERROR: Deleting service forwarding: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static void updateServiceForwarding(String update_account_id, String update_keyword, String new_account_id,
                                               String new_keyword, String new_url, String new_timeout, String new_listen_status) throws Exception {
        Connection conn = null;

        try {
            conn = DConnect.getConnection();
            String sql = "UPDATE service_forwarding SET account_id = '" + new_account_id + "', keyword = '" + new_keyword + "', url = '" + new_url + "', "
                    + "timeout = " + new_timeout + ", listen_status = " + new_listen_status + " WHERE account_id = '" + update_account_id + "' and keyword = '" + update_keyword + "'";

            System.out.println(new Date() + ": " + UserServiceDB.class + ":DEBUG Will update service forwarding: " + sql);
            conn.createStatement().executeUpdate(sql);
        } catch (Exception ex) {
            System.out.println(new Date() + ": " + UserServiceDB.class + "ERROR: Updating service forwarding: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
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
