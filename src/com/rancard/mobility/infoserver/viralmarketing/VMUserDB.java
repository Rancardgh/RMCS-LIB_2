/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.infoserver.viralmarketing;

import com.rancard.common.DConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author nii
 */
public class VMUserDB {

    public static void createUser(VMUser user) throws Exception {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            SQL = "insert into vm_users(reg_date, account_id, keyword, msisdn, username, points) " +
                    "values(?, ?, ?, ?, ?, ?)";

            prepstat = con.prepareStatement(SQL);

            prepstat.setTimestamp(1, new java.sql.Timestamp(new java.util.Date().getTime()));
            prepstat.setString(2, user.getAccountId());
            prepstat.setString(3, user.getKeyword());
            prepstat.setString(4, user.getMsisdn());
            prepstat.setString(5, user.getUsername());
            prepstat.setInt(6, user.getPoints());

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

    public static VMUser viewUser(String keyword, String accountId, String msisdn) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        VMUser user = new VMUser();

        try {
            con = DConnect.getConnection();

            SQL = "select * from vm_users where keyword = ? and account_id = ? and msisdn = ?";

            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, keyword);
            prepstat.setString(2, accountId);
            prepstat.setString(3, msisdn);

            rs = prepstat.executeQuery();

            while (rs.next()) {
                user.setKeyword(rs.getString("keyword"));
                user.setAccountId(rs.getString("account_id"));
                user.setMsisdn(rs.getString("msisdn"));
                user.setUsername(rs.getString("username"));
                user.setPoints(rs.getInt("points"));
                java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String regDate = df.format(new java.util.Date(rs.getTimestamp("reg_date").getTime()));
                user.setRegDate(regDate);
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
            System.out.println(new java.util.Date()+ ": error viewing vm_user ("+ keyword +", "+accountId+", " + msisdn + "): " + ex.getMessage() );

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

        return user;
    }

    public static void addPoints(String keyword, String accountId, String msisdn, int points) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            SQL =
                    "UPDATE vm_users SET points = points + ? " +
                    "WHERE keyword = ?  and account_id =? and msisdn = ?";

            prepstat = con.prepareStatement(SQL);

            prepstat.setInt(1, points);
            prepstat.setString(2, keyword);
            prepstat.setString(3, accountId);
            prepstat.setString(4, msisdn);
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
}