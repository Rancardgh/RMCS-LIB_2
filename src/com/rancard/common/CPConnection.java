package com.rancard.common;

import com.rancard.common.db.DConnect;
import com.rancard.util.Utils;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Mustee on 3/29/2014.
 */

public class CPConnection implements Serializable {
    private static Logger logger = Logger.getLogger(CPConnection.class.getName());

    private String connID;
    private String listID;
    private String username;
    private String password;
    private String gatewayURL;
    private String method;
    private String driverType;
    private String allowedNetworks;
    private String billingMech;
    private String timeZone;


    public CPConnection(String connID, String listID, String username, String password, String gatewayURL, String method, String driverType, String allowedNetworks, String billingMech, String timeZone) {
        this.connID = connID;
        this.listID = listID;
        this.username = username;
        this.password = password;
        this.gatewayURL = gatewayURL;
        this.method = method;
        this.driverType = driverType;
        this.allowedNetworks = allowedNetworks;
        this.billingMech = billingMech;
        this.timeZone = timeZone;
    }

    public String getConnID() {
        return connID;
    }

    public void setConnID(String connID) {
        this.connID = connID;
    }

    public String getListID() {
        return listID;
    }

    public void setListID(String listID) {
        this.listID = listID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGatewayURL() {
        return gatewayURL;
    }

    public void setGatewayURL(String gatewayURL) {
        this.gatewayURL = gatewayURL;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getDriverType() {
        return driverType;
    }

    public void setDriverType(String driverType) {
        this.driverType = driverType;
    }

    public String getAllowedNetworks() {
        return allowedNetworks;
    }

    public void setAllowedNetworks(String allowedNetworks) {
        this.allowedNetworks = allowedNetworks;
    }

    public String getBillingMech() {
        return billingMech;
    }

    public void setBillingMech(String billingMech) {
        this.billingMech = billingMech;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public static List<CPConnection> getAll() throws Exception {
        List<CPConnection> conns = new ArrayList<CPConnection>();
        final String query = "SELECT * FROM cp_connections";
        Connection conn = null;
        ResultSet rs = null;

        try {
            logger.info("Getting connections: "+query);
            conn = DConnect.getConnection();
            rs = conn.createStatement().executeQuery(query);

            while (rs.next()){
                conns.add(new CPConnection(rs.getString("conn_id"), rs.getString("list_id"), rs.getString("username"), rs.getString("password"), rs.getString("gateway_url"),
                        rs.getString("method"), rs.getString("type"), rs.getString("allowed_networks"), rs.getString("billing_mech"), rs.getString("time_zone")));
            }

            return conns;
        } catch (Exception e) {
            logger.severe("Problem getting connections: " + e.getMessage());
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

    public static CPConnection getCPConnection(String accountID, String smsc) throws Exception {
        final String query = "SELECT * FROM cp_connections WHERE list_id = '" + accountID + "' AND conn_id LIKE '%" + Utils.getBaseSMSC(smsc) + "%' LIMIT 1";
        Connection conn = null;
        ResultSet rs = null;

        try {
            logger.info("Getting connection: "+query);
            conn = DConnect.getConnection();
            rs = conn.createStatement().executeQuery(query);

            while (rs.next()){
                return new CPConnection(rs.getString("conn_id"), rs.getString("list_id"), rs.getString("username"), rs.getString("password"), rs.getString("gateway_url"),
                        rs.getString("method"), rs.getString("type"), rs.getString("allowed_networks"), rs.getString("billing_mech"), rs.getString("time_zone"));
            }

            return null;
        } catch (Exception e) {
            logger.severe("Problem getting connections: " + e.getMessage());
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
