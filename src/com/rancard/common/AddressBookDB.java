/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author Mustee
 */
public class AddressBookDB {

    public static AddressBook getAddress(String accountID, String msisdn) throws SQLException, Exception {
        Connection conn = null;
        ResultSet rs = null;
        AddressBook address = null;
        
        try {
            conn = DConnect.getConnection();

            String sql = "SELECT * FROM address_book where account_id = '" + accountID + "' and msisdn = '" + msisdn + "'";
            System.out.println(new Date() + ": " + AddressBookDB.class + ":DEBUG Get address: " + sql);

            rs = conn.createStatement().executeQuery(sql);

            if(rs.next()){
                address = new AddressBook(rs.getString("account_id"), rs.getString("msisdn"), rs.getString("registration_id"));               
            }
            return address;
        } catch (SQLException se) {
            System.out.println(new Date() + ": " + AddressBookDB.class + "ERROR: Checking if address exists: " + se.getMessage());
            throw new SQLException(se.getMessage(), se.getSQLState());
        } catch (Exception e) {
            System.out.println(new Date() + ": " + AddressBookDB.class + "ERROR: Checking if address exists: " + e.getMessage());
            throw new Exception(e.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    public static void save(AddressBook addressBook) throws SQLException, Exception {
        Connection conn = null;

        try {
            conn = DConnect.getConnection();

            String sql = "INSERT INTO address_book (account_id, msisdn, registration_id) "
                    + "VALUES ('" + addressBook.getAccountID() + "', '" + addressBook.getMsisdn() + "', '" + addressBook.getRegistrationID() + "')";

            System.out.println(new Date() + ": " + AddressBookDB.class + ":DEBUG Inserting address : " + sql);
            conn.createStatement().execute(sql);

        } catch (SQLException se) {
            System.out.println(new Date() + ": " + AddressBookDB.class + "ERROR: Inserting address: " + se.getMessage());
            throw new SQLException(se.getMessage(), se.getSQLState());
        } catch (Exception e) {
            System.out.println(new Date() + ": " + AddressBookDB.class + "ERROR: Inserting address: " + e.getMessage());
            throw new Exception(e.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }

        }
    }

    public static void delete(AddressBook addressBook) throws SQLException, Exception {
        Connection conn = null;

        try {
            String sql = "DELETE FROM address_book where account_id = '" + addressBook.getAccountID() + "' and msisdn = '" + addressBook.getMsisdn() + "'";
            System.out.println(new Date() + ": " + AddressBookDB.class + ":DEBUG Deleting address : " + sql);

            conn = DConnect.getConnection();
            conn.createStatement().execute(sql);

        } catch (SQLException se) {
            System.out.println(new Date() + ": " + AddressBookDB.class + "ERROR: Deleting address: " + se.getMessage());
            throw new SQLException(se.getMessage(), se.getSQLState());
        } catch (Exception e) {
            System.out.println(new Date() + ": " + AddressBookDB.class + "ERROR: Deleting address: " + e.getMessage());
            throw new Exception(e.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }

        }
    }

    public static void update(AddressBook original, AddressBook update) throws SQLException, Exception {
        Connection conn = null;
        String sql = "UPDATE address_book SET account_id = '" + update.getAccountID() + "', msisdn = '" + update.getMsisdn() + "', registration_id = '" + update.getRegistrationID() + "' "
                + "where account_id = '" + original.getAccountID() + "' and msisdn = '" + original.getMsisdn() + "'";
        System.out.println(new Date() + ": " + AddressBookDB.class + ":DEBUG Updating address : " + sql);
        try {
            conn = DConnect.getConnection();
            conn.createStatement().executeUpdate(sql);
        } catch (SQLException se) {
            System.out.println(new Date() + ": " + AddressBookDB.class + "ERROR: Updating address: " + se.getMessage());
            throw new SQLException(se.getMessage(), se.getSQLState());
        } catch (Exception e) {
            System.out.println(new Date() + ": " + AddressBookDB.class + "ERROR: Updating address: " + e.getMessage());
            throw new Exception(e.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }
    
    
}
