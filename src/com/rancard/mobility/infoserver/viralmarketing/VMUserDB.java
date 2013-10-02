/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.infoserver.viralmarketing;

import com.rancard.common.DConnect;
import com.rancard.util.DateUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;

/**
 *
 * @author nii
 */
public class VMUserDB {

    public static void createUser(VMUser user) throws Exception {
        Connection conn = null;

        try {
            conn = DConnect.getConnection();
            String sql = "insert into vm_users(reg_date, account_id, keyword, msisdn, username, points) "
                    + "values('" + DateUtil.convertToMySQLTimeStamp(new Date()) + "', '" + user.getAccountID() + "', '"
                    + user.getKeyword() + "', '" + user.getMsisdn() + "', '" + user.getUsername() + "', " + user.getPoints() + ")";
            System.out.println(new Date() + "\t[" + VMUserDB.class + "]\tDEBUG\tCreating VMUser: " + sql);
            conn.createStatement().execute(sql);

        } catch (Exception ex) {
            System.out.println(new Date() + "\t[" + VMUserDB.class + "]\tERROR\tCreating VMUser: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

    }

    public static VMUser viewUser(String keyword, String accountID, String msisdn) throws Exception {
        ResultSet rs = null;
        Connection conn = null;
        VMUser user = null;

        try {
            conn = DConnect.getConnection();
            String sql = "select * from vm_users where keyword = '" + keyword + "' and account_id = '" + accountID + "' and msisdn = '" + msisdn + "'";
            System.out.println(new Date() + "\t[" + VMUserDB.class + "]\tDEBUG\tGetting VMUser: " + sql);

            rs = conn.createStatement().executeQuery(sql);

            if (rs.next()) {
                user = new VMUser(DateUtil.convertFromMySQLTimeStamp(rs.getString("reg_date")), rs.getString("msisdn"), rs.getString("account_id"),
                        rs.getString("keyword"), rs.getString("username"), rs.getInt("points"));
            }

            return user;
        } catch (Exception ex) {
            System.out.println(new Date() + "\t[" + VMUserDB.class + "]\tERROR\tGetting VMUser: " + ex.getMessage());
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

    public static void addPoints(String keyword, String accountID, String msisdn, int points) throws Exception {
        Connection conn = null;

        try {
            conn = DConnect.getConnection();
            String sql = "UPDATE vm_users SET points = points + " + points + " WHERE keyword = '" + keyword + "' "
                    + "and account_id ='" + accountID + "' and msisdn = '" + msisdn + "'";
            System.out.println(new Date() + "\t[" + VMUserDB.class + "]\tDEBUG\tUpdating VMUser: " + sql);

            conn.createStatement().executeUpdate(sql);


        } catch (Exception ex) {
            System.out.println(new Date() + "\t[" + VMUserDB.class + "]\tERROR\tUpdating VMUser: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

    }
}