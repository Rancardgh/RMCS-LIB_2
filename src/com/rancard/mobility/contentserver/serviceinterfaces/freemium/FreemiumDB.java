/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.contentserver.serviceinterfaces.freemium;

import com.rancard.common.DConnect;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mustee
 */
class FreemiumDB {

    private static Freemium createFreemiumFromRS(ResultSet rs) throws SQLException {
        return new Freemium(rs.getString("id"), rs.getString("accountID"), rs.getString("keyword"),
                rs.getInt("length"), rs.getDate("startDate"), rs.getDate("endDate"), rs.getBoolean("isActive"),
                FreemiumType.valueOf(rs.getString("type").trim().toUpperCase()),
                FreemiumRolloverOption.valueOf(rs.getString("rolloverOption").trim()));
    }

    public static Freemium getFreemuim(String id) throws SQLException, Exception {
        Connection conn = null;
        ResultSet rs = null;
        Freemium freemium = null;
        String sql = "SELECT * FROM freemium where id = '" + id + "' LIMIT 1";
        try {
            conn = DConnect.getConnection();
            rs = conn.createStatement().executeQuery(sql);

            if (rs.next()) {
                freemium = createFreemiumFromRS(rs);               
            }
            return freemium;

        } catch (SQLException se) {
            throw new SQLException(se.getMessage(), se.getSQLState());
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

    public static Freemium getFreemuim(String id, Connection conn) throws SQLException {

        ResultSet rs = null;
        Freemium freemium = null;
        String sql = "SELECT * FROM freemium where id = '" + id + "' LIMIT 1";
        try {

            rs = conn.createStatement().executeQuery(sql);

            if (rs.next()) {
                freemium = createFreemiumFromRS(rs);

            }
            return freemium;

        } catch (SQLException se) {
            throw new SQLException(se.getMessage(), se.getSQLState());
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    public static List<Freemium> getFreemuim(String accountID, String keyword) throws SQLException, Exception {
        Connection conn = null;
        ResultSet rs = null;
        List<Freemium> freemiums = new ArrayList<Freemium>();
        String sql = "SELECT * FROM freemium where accountID = '" + accountID + "' and keyword = '" + keyword + "'";
        try {
            conn = DConnect.getConnection();
            rs = conn.createStatement().executeQuery(sql);

            while (rs.next()) {
                freemiums.add(createFreemiumFromRS(rs));
            }
            return freemiums;

        } catch (SQLException se) {
            throw new SQLException(se.getMessage(), se.getSQLState());
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

    public static List<Freemium> getFreemuim(String accountID, String keyword, boolean active) throws SQLException, Exception {
        Connection conn = null;
        ResultSet rs = null;
        List<Freemium> freemiums = new ArrayList<Freemium>();
        String sql = "SELECT * FROM freemium where accountID = '" + accountID + "' and keyword = '" + keyword + "'";
        try {
            conn = DConnect.getConnection();
            rs = conn.createStatement().executeQuery(sql);

            while (rs.next()) {
                if (rs.getBoolean("isActive") == active) {
                    freemiums.add(createFreemiumFromRS(rs));
                }

            }
            return freemiums;

        } catch (SQLException se) {
            throw new SQLException(se.getMessage(), se.getSQLState());
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

    public static List<Freemium> getFreemuim(String accountID, String keyword, Connection conn) throws SQLException {
        ResultSet rs = null;
        List<Freemium> freemiums = new ArrayList<Freemium>();
        String sql = "SELECT * FROM freemium where accountID = '" + accountID + "' and keyword = '" + keyword + "' LIMIT 1";
        try {

            rs = conn.createStatement().executeQuery(sql);

            while (rs.next()) {
                freemiums.add(createFreemiumFromRS(rs));

            }
            return freemiums;

        } catch (SQLException se) {
            throw new SQLException(se.getMessage(), se.getSQLState());
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    public static List<FreemiumDataSource> getFreemuimDataSource(Freemium freemium) throws SQLException, Exception {
        ResultSet rs = null;
        List<FreemiumDataSource> dataSources = new ArrayList<FreemiumDataSource>();
        Connection conn = null;
        String sql = "SELECT * FROM freemium_datasource where freemuimID = '" + freemium.getID() + "'";
        try {
            conn = DConnect.getConnection();
            rs = conn.createStatement().executeQuery(sql);

            while (rs.next()) {
                dataSources.add(new FreemiumDataSource(freemium, FreemiumDataSourceType.valueOf(rs.getString("sourceType").trim().toUpperCase()),
                        rs.getString("source"), rs.getString("column"), rs.getString("delimeter"), rs.getBoolean("notin")));

            }
            return dataSources;

        } catch (SQLException se) {
            throw new SQLException(se.getMessage(), se.getSQLState());
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

    public static List<FreemiumDataSource> getFreemuimDataSource(Freemium freemium, Connection conn) throws SQLException {
        ResultSet rs = null;
        List<FreemiumDataSource> dataSources = new ArrayList<FreemiumDataSource>();
        String sql = "SELECT * FROM freemium_datasource where freemuimID = '" + freemium.getID() + "'";
        try {

            rs = conn.createStatement().executeQuery(sql);

            while (rs.next()) {
                dataSources.add(new FreemiumDataSource(freemium, FreemiumDataSourceType.valueOf(rs.getString("sourceType").trim().toUpperCase()),
                        rs.getString("source"), rs.getString("column"), rs.getString("delimiter"), rs.getBoolean("notin")));

            }
            return dataSources;

        } catch (SQLException se) {
            throw new SQLException(se.getMessage(), se.getSQLState());
        } finally {
            if (rs != null) {
                rs.close();
            }

        }
    }
}
