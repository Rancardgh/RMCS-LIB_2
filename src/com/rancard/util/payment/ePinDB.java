/*
 * ePinDB.java
 *
 * Created on April 24, 2006, 7:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.util.payment;

import com.rancard.mobility.contentprovider.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.rancard.common.DConnect;

/**
 *
 * @author USER
 */
public class ePinDB {

    /** Creates a new instance of ePinDB */
    public ePinDB() {
    }

    public ePin viewVoucher(String id) throws Exception {
        ePin voucher = new ePin();
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = new com.rancard.common.DConnect().getConnection();

            SQL = "select * from evoucher where PIN=" +
                    id;
            prepstat = con.prepareStatement(SQL);

            rs = prepstat.executeQuery();
            while (rs.next()) {

                voucher.setProvider(rs.getString("list_id"));
                voucher.setPin(rs.getString("PIN"));
                voucher.setCurrentBalance(rs.getDouble("balance"));
                voucher.setEValue(rs.getDouble("value"));


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

        return voucher;
    }

    public java.util.ArrayList viewVouchers(String listId) throws Exception {
        java.util.ArrayList vouchers = new java.util.ArrayList();
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = new com.rancard.common.DConnect().getConnection();

            SQL = "select * from evoucher where list_id='" +
                    listId + "'";
            prepstat = con.prepareStatement(SQL);

            rs = prepstat.executeQuery();
            while (rs.next()) {
                ePin voucher = new ePin();
                voucher.setProvider(rs.getString("list_id"));
                voucher.setPin(rs.getString("PIN"));
                voucher.setCurrentBalance(rs.getDouble("balance"));
                voucher.setEValue(rs.getDouble("value"));

                vouchers.add(voucher);
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

        return vouchers;
    }


    public void updateVoucher(String id, double balance) throws Exception {
        ePin voucher = new ePin();

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = new com.rancard.common.DConnect().getConnection();

            SQL = "update evoucher SET balance=" + balance + " where PIN=" + id;
            prepstat = con.prepareStatement(SQL);
            prepstat.execute(SQL);
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
    }

    public void insertVoucher(String listId, String pin, double limit, double balance) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = new com.rancard.common.DConnect().getConnection();

            SQL = "INSERT into evoucher (list_id,pin,value,balance) values(?,?,?,?)";
            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, listId);
            prepstat.setString(2, pin);
            prepstat.setDouble(3, limit);
            prepstat.setDouble(4, balance);

            prepstat.execute(SQL);
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
    }

    public static void deleteVoucher(String pin) throws Exception {
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            query = "DELETE from evoucher WHERE pin=?";
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, pin);
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

}
