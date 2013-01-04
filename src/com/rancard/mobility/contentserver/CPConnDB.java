package com.rancard.mobility.contentserver;

import java.sql.PreparedStatement;
import com.rancard.common.DConnect;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.ArrayList;

public abstract class CPConnDB {
    
    //DB Access methods
    public static CPConnections viewConnection (String providerId, String networkPrefix) throws Exception {
        CPConnections cnxn = new CPConnections ();
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            query = "SELECT * from cp_connections WHERE list_id=?";
            prepstat = con.prepareStatement (query);
            prepstat.setString (1, providerId);
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                cnxn.setAllowedNetworks (rs.getString ("allowed_networks"));
                if (cnxn.getAllowedNetworks ().contains (networkPrefix)) {
                    cnxn.setConnection (rs.getString ("conn_id"));
                    cnxn.setDriverType (rs.getString ("type"));
                    cnxn.setGatewayURL (rs.getString ("gateway_url"));
                    cnxn.setHttpMethod (rs.getString ("method"));
                    cnxn.setPassword (rs.getString ("password"));
                    cnxn.setProviderId (rs.getString ("list_id"));
                    cnxn.setUsername (rs.getString ("username"));
                    cnxn.setBillingMech (rs.getString ("billing_mech"));
                }
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
        return cnxn;
        
    }
    
    public static ArrayList viewConnections (String providerId) throws Exception {
        ArrayList connections = new ArrayList ();
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            query = "SELECT * from cp_connections cpc left outer join country_timezone " +
                    "ctz on ctz.country_alias=cpc.time_zone WHERE list_id=?";
            
            prepstat = con.prepareStatement (query);
            prepstat.setString (1, providerId);
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                CPConnections cnxn = new CPConnections ();
                cnxn.setAllowedNetworks (rs.getString ("allowed_networks"));
                cnxn.setConnection (rs.getString ("conn_id"));
                cnxn.setDriverType (rs.getString ("type"));
                cnxn.setGatewayURL (rs.getString ("gateway_url"));
                cnxn.setHttpMethod (rs.getString ("method"));
                cnxn.setPassword (rs.getString ("password"));
                cnxn.setProviderId (rs.getString ("list_id"));
                cnxn.setUsername (rs.getString ("username"));
                cnxn.setBillingMech (rs.getString ("billing_mech"));
                cnxn.setCountryAlias(rs.getString ("time_zone"));
                cnxn.setCountryName(rs.getString ("country_name"));
                cnxn.setUTCOffset(rs.getInt("utc_offset"));
                cnxn.setLanguage(rs.getString("language"));
                
                connections.add (cnxn);
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
        return connections;
        
    }

    
    public static ArrayList viewConnections (String providerId, String driverType) throws Exception {
        ArrayList connections = new ArrayList ();
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection ();
            query = "SELECT * from cp_connections cpc left outer join country_timezone " +
                    "ctz on ctz.country_alias=cpc.time_zone WHERE list_id=? and type=?";

            prepstat = con.prepareStatement (query);
            prepstat.setString (1, providerId);
            prepstat.setString(2, driverType);
            rs = prepstat.executeQuery ();

            while (rs.next ()) {
                CPConnections cnxn = new CPConnections ();
                cnxn.setAllowedNetworks (rs.getString ("allowed_networks"));
                cnxn.setConnection (rs.getString ("conn_id"));
                cnxn.setDriverType (rs.getString ("type"));
                cnxn.setGatewayURL (rs.getString ("gateway_url"));
                cnxn.setHttpMethod (rs.getString ("method"));
                cnxn.setPassword (rs.getString ("password"));
                cnxn.setProviderId (rs.getString ("list_id"));
                cnxn.setUsername (rs.getString ("username"));
                cnxn.setBillingMech (rs.getString ("billing_mech"));
                cnxn.setCountryAlias(rs.getString ("time_zone"));
                cnxn.setCountryName(rs.getString ("country_name"));
                cnxn.setUTCOffset(rs.getInt("utc_offset"));
                cnxn.setLanguage(rs.getString("language"));

                connections.add (cnxn);
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
        return connections;

    }
    
}
