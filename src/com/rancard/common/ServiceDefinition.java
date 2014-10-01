package com.rancard.common;

import com.rancard.common.db.DConnect;
import com.rancard.util.DateUtil;
import com.rancard.util.Utils;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Mustee on 3/29/2014.
 */

public class ServiceDefinition implements Serializable {
    private static Logger logger = Logger.getLogger(ServiceDefinition.class.getName());

    private String accountID;
    private String keyword;
    private String serviceType;
    private String serviceName;
    private boolean isBasic;
    private String defaultMessage;
    private Date lastUpdated;
    private String allowedSiteTypes;
    private String allowedShortCodes;
    private String pricing;
    private String command;
    private boolean isSubscription;
    private String serviceResponseSender;
    private String tags;


    public ServiceDefinition(String accountID, String keyword, String serviceType, String serviceName, boolean isBasic, String defaultMessage, Date lastUpdated, String allowedSiteTypes, String allowedShortcodes, String pricing, String command, boolean isSubscription, String serviceResponseSender, String tags) {
        this.accountID = accountID;
        this.keyword = keyword;
        this.serviceType = serviceType;
        this.serviceName = serviceName;
        this.isBasic = isBasic;
        this.defaultMessage = defaultMessage;
        this.lastUpdated = lastUpdated;
        this.allowedSiteTypes = allowedSiteTypes;
        this.allowedShortCodes = allowedShortcodes;
        this.pricing = pricing;
        this.command = command;
        this.isSubscription = isSubscription;
        this.serviceResponseSender = serviceResponseSender;
        this.tags = tags;
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

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public boolean isBasic() {
        return isBasic;
    }

    public void setBasic(boolean isBasic) {
        this.isBasic = isBasic;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public void setDefaultMessage(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getAllowedSiteTypes() {
        return allowedSiteTypes;
    }

    public void setAllowedSiteTypes(String allowedSiteTypes) {
        this.allowedSiteTypes = allowedSiteTypes;
    }

    public String getAllowedShortcodes() {
        return allowedShortCodes;
    }

    public void setAllowedShortcodes(String allowedShortcodes) {
        this.allowedShortCodes = allowedShortcodes;
    }

    public String getPricing() {
        return pricing;
    }

    public void setPricing(String pricing) {
        this.pricing = pricing;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public boolean isSubscription() {
        return isSubscription;
    }

    public void setSubscription(boolean isSubscription) {
        this.isSubscription = isSubscription;
    }

    public String getServiceResponseSender() {
        return serviceResponseSender;
    }

    public void setServiceResponseSender(String serviceResponseSender) {
        this.serviceResponseSender = serviceResponseSender;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }


    public static List<ServiceDefinition> findBySMSC(String smsc) throws Exception {
        List<ServiceDefinition> services = new ArrayList<ServiceDefinition>();
        final String query = "SELECT * FROM service_definition sd INNER JOIN cp_connection cp ON sd.account_id = cp.list_id WHERE cp.conn_id LIKE '%" + Utils.getBaseSMSC(smsc) + "%'";

        Connection conn = null;
        ResultSet rs = null;

        try {
            logger.info("Getting services: " + query);
            conn = DConnect.getConnection();
            rs = conn.createStatement().executeQuery(query);

            services.add(new ServiceDefinition(rs.getString("account_id"), rs.getString("keyword"), rs.getString("service_type"), rs.getString("service_name"),
                    rs.getBoolean("is_basic"), rs.getString("default_message"), DateUtil.convertFromTimeStampFormat(rs.getString("last_updated")),
                    rs.getString("allowed_site_types"), rs.getString("allowed_shortcodes"), rs.getString("pricing"), rs.getString("command"), rs.getBoolean("is_subscription"),
                    rs.getString("service_response_sender"), rs.getString("tags")));

            return services;
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

    public static List<ServiceDefinition> findBySMSCAndShortCode(String smsc, String shortCode) throws Exception {
        List<ServiceDefinition> services = new ArrayList<ServiceDefinition>();
        final String query = "SELECT * FROM service_definition sd INNER JOIN cp_connections cp ON sd.account_id = cp.list_id WHERE cp.conn_id LIKE '%"
                + Utils.getBaseSMSC(smsc) + "%' AND service_response_sender = '" + shortCode + "'";

        Connection conn = null;
        ResultSet rs = null;

        try {
            logger.info("Getting services: " + query);
            conn = DConnect.getConnection();
            rs = conn.createStatement().executeQuery(query);

            while (rs.next()) {
                services.add(new ServiceDefinition(rs.getString("account_id"), rs.getString("keyword"), rs.getString("service_type"), rs.getString("service_name"),
                        rs.getBoolean("is_basic"), rs.getString("default_message"), DateUtil.convertFromTimeStampFormat(rs.getString("last_updated")),
                        rs.getString("allowed_site_types"), rs.getString("allowed_shortcodes"), rs.getString("pricing"), rs.getString("command"), rs.getBoolean("is_subscription"),
                        rs.getString("service_response_sender"), rs.getString("tags")));
            }

            return services;
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

    public static ServiceDefinition find(String accountID, String keyword) throws Exception {
        final String query = "SELECT * FROM service_definition WHERE account_id = '" + accountID + "' AND keyword = '" + keyword + "'";

        Connection conn = null;
        ResultSet rs = null;
        try {
            logger.info("Getting services: " + query);
            conn = DConnect.getConnection();
            rs = conn.createStatement().executeQuery(query);

            if(rs.next()) {
                return new ServiceDefinition(rs.getString("account_id"), rs.getString("keyword"), rs.getString("service_type"), rs.getString("service_name"),
                        rs.getBoolean("is_basic"), rs.getString("default_message"), DateUtil.convertFromTimeStampFormat(rs.getString("last_updated")),
                        rs.getString("allowed_site_types"), rs.getString("allowed_shortcodes"), rs.getString("pricing"), rs.getString("command"), rs.getBoolean("is_subscription"),
                        rs.getString("service_response_sender"), rs.getString("tags"));
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
