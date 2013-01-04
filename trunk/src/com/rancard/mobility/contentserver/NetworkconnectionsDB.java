package com.rancard.mobility.contentserver;

import java.sql.*;
import java.util.Vector;
import com.rancard.common.DConnect;

public class NetworkconnectionsDB {

//constructor
    public NetworkconnectionsDB() {}

//insert record
    public void createcp_connections(java.lang.String username,
                                     java.lang.String password,
                                     java.lang.String conn_id,
                                     java.lang.String gateway_url,
                                     java.lang.String method,
                                     java.lang.String type,
                                     java.lang.String network_prefix) throws
            Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();

            SQL = "insert into cp_connections(username,password,conn_id,gateway_url,method,type,network_prefix) values(?,?,?,?,?,?,?)";
            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, username);

            prepstat.setString(2, password);

            prepstat.setString(3, conn_id);

            prepstat.setString(4, gateway_url);

            prepstat.setString(5, method);

            prepstat.setString(6, type);

            prepstat.setString(7, network_prefix);
            prepstat.execute();
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
    }

    public void updatecp_connections(java.lang.String username,
                                     java.lang.String password,
                                     java.lang.String conn_id,
                                     java.lang.String gateway_url,
                                     java.lang.String method,
                                     java.lang.String type,
                                     java.lang.String network_prefix) throws
            Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();

            SQL = "update cp_connections set username=?,password=?,gateway_url=?,method=?,type=?,network_prefix=? where conn_id=?";
            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, username);

            prepstat.setString(2, password);

            prepstat.setString(3, gateway_url);

            prepstat.setString(4, method);

            prepstat.setString(5, type);

            prepstat.setString(6, network_prefix);

            prepstat.setString(7, conn_id);
            prepstat.execute();
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
    }

    public void deletecp_connections(java.lang.String conn_id) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            SQL = "delete from cp_connections where conn_id=?";
            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, conn_id);
            prepstat.execute();
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
    }

    public NetworkConnectionsBean viewcp_connections(java.lang.String conn_id) throws
            Exception {
        NetworkConnectionsBean cp_connections = new NetworkConnectionsBean();

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();

            SQL = "select * from  cp_connections where conn_id=?";
            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, conn_id);
            rs = prepstat.executeQuery();
            while (rs.next()) {

                cp_connections.setusername(rs.getString("username"));
                cp_connections.setpassword(rs.getString("password"));
                cp_connections.setconn_id(rs.getString("conn_id"));
                cp_connections.setgateway_url(rs.getString("gateway_url"));
                cp_connections.setmethod(rs.getString("method"));
                cp_connections.settype(rs.getString("type"));
                cp_connections.setnetwork_prefix(rs.getString("network_prefix"));
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
        return cp_connections;
    }
}
