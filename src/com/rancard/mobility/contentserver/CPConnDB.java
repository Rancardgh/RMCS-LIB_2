package com.rancard.mobility.contentserver;

import com.rancard.common.DConnect;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public final class CPConnDB {

    //DB Access methods
    public static CPConnections viewConnection(String providerId, String networkPrefix) throws Exception {
        CPConnections cnxn = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DConnect.getConnection();
            String query = "SELECT * from cp_connections cpc left outer join country_timezone "
                    + "ctz on ctz.country_alias = cpc.time_zone WHERE cpc.list_id = '" + providerId + "'";
            System.out.println(new Date() + ": " + CPConnDB.class + ":DEBUG About to get connections: " + query);
            rs = conn.createStatement().executeQuery(query);

            while (rs.next()) {
                if (rs.getString("allowed_networks").contains(networkPrefix)) {
                    cnxn = new CPConnections(rs.getString("password"), rs.getString("username"), rs.getString("conn_id"),
                            rs.getString("gateway_url"), rs.getString("method"), rs.getString("type"),
                            Arrays.asList(rs.getString("allowed_networks").split(",")), rs.getString("list_id"), rs.getString("billing_mech"),
                            rs.getString("country_name"), rs.getString("time_zone"), rs.getInt("utc_offset"), rs.getString("language"));
                }
            }
            return cnxn;
        } catch (Exception ex) {
            System.out.println(new Date() + ": " + CPConnDB.class + ":ERROR " + ex.getMessage());
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

    public static List viewConnections(String providerId) throws Exception {
        List connections = new ArrayList();
        ResultSet rs = null;
        Connection conn = null;


        try {
            conn = DConnect.getConnection();
            String query = "SELECT * from cp_connections cpc left outer join country_timezone "
                    + "ctz on ctz.country_alias=cpc.time_zone WHERE cpc.list_id= '" + providerId + "'";
            System.out.println(new Date() + ": " + CPConnDB.class + ":DEBUG About to get connections: " + query);

            rs = conn.createStatement().executeQuery(query);

            while (rs.next()) {
                connections.add(new CPConnections(rs.getString("password"), rs.getString("username"), rs.getString("conn_id"),
                        rs.getString("gateway_url"), rs.getString("method"), rs.getString("type"),
                        Arrays.asList(rs.getString("allowed_networks").split(",")), rs.getString("list_id"), rs.getString("billing_mech"),
                        rs.getString("country_name"), rs.getString("time_zone"), rs.getInt("utc_offset"), rs.getString("language")));

            }
        } catch (Exception ex) {
            System.out.println(new Date() + ": " + CPConnDB.class + ":ERROR " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return connections;

    }

    public static List viewConnections(String providerId, String driverType) throws Exception {
        List connections = new ArrayList();        
        ResultSet rs = null;
        Connection conn = null;        

        try {
            conn = DConnect.getConnection();
            String query = "SELECT * from cp_connections cpc left outer join country_timezone "
                    + "ctz on ctz.country_alias=cpc.time_zone WHERE list_id= '" + providerId + "' and type= '" + driverType + "'";
            System.out.println(new Date() + ": " + CPConnDB.class + ":DEBUG About to get connections: " + query);
            
            rs = conn.createStatement().executeQuery(query);

            while (rs.next()) {
               connections.add(new CPConnections(rs.getString("password"), rs.getString("username"), rs.getString("conn_id"),
                        rs.getString("gateway_url"), rs.getString("method"), rs.getString("type"),
                        Arrays.asList(rs.getString("allowed_networks").split(",")), rs.getString("list_id"), rs.getString("billing_mech"),
                        rs.getString("country_name"), rs.getString("time_zone"), rs.getInt("utc_offset"), rs.getString("language")));
            }
        } catch (Exception ex) {
            System.out.println(new Date() + ": " + CPConnDB.class + ":ERROR " + ex.getMessage());
            throw new Exception(ex.getMessage());
        }
        finally {
            if (rs != null) {
                rs.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return connections;

    }
}
