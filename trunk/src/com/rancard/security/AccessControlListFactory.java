package com.rancard.security;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import com.rancard.common.DConnect;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;

public class AccessControlListFactory {
    public AccessControlListFactory() {
    }

    public static java.util.ArrayList viewWhiteList() throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        java.util.ArrayList whiteList = new ArrayList();
        try {
            con = DConnect.getConnection();
            java.sql.Date tempdate = null;

            SQL = "select * from whitelist";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            while (rs.next()) {
                AccessListItem userIP = new AccessListItem();

                whiteList.add(rs.getString("allowed_IP"));
            }

            rs.close();
            rs = null;
            prepstat.close();
            prepstat = null;
            con.close();
            con = null;
        } catch (Exception ex) {
            if (con != null) {
                con.close();
                con = null;
            }
            throw new Exception(ex.getMessage());
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

        return whiteList;

    }

    public static java.util.ArrayList viewBlackList() throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        java.util.ArrayList whiteList = new ArrayList();
        try {
            con = DConnect.getConnection();
            java.sql.Date tempdate = null;

            SQL = "select * from blacklist";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            while (rs.next()) {

                whiteList.add(rs.getString("denied_IP"));
            }

            rs.close();
            rs = null;
            prepstat.close();
            prepstat = null;
            con.close();
            con = null;
        } catch (Exception ex) {
            if (con != null) {
                con.close();
                con = null;
            }
            throw new Exception(ex.getMessage());
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

        return whiteList;

    }



}
