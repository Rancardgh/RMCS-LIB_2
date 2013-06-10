package com.rancard.mobility.contentserver;

import java.sql.PreparedStatement;
import com.rancard.common.DConnect;
import java.sql.ResultSet;
import java.sql.Connection;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public abstract class PhoneMakeDB {

    /*
     insert record
     */
    public static void insertPhoneMake(String phoneMake) throws Exception {

        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            query = "INSERT into phone_make_list(phone_manufacturer) values(?)";

            prepstat = con.prepareStatement(query);
            prepstat.setString(1, phoneMake);

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

    /*
         Update list entry
     */
    public static void updatePhoneMake(int id, String phoneMake) throws
            Exception {
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            query = "UPDATE phone_make_list SET phone_manufacturer=? WHERE" +
                    " id=" + id;
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, phoneMake);
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

    /*
         Deletes an entry
     */
    public static void deletePhoneMake(int id) throws Exception {
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            query = "DELETE from phone_make_list WHERE id=?";
            prepstat = con.prepareStatement(query);
            prepstat.setInt(1, id);
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

    /*
         Retrieves an item as a bean
     */
    public static PhoneMake viewPhoneMake(int id) throws Exception {
        PhoneMake newBean = new PhoneMake();
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            query = "SELECT * from phone_make_list WHERE id=?";
            prepstat = con.prepareStatement(query);
            prepstat.setInt(1, id);
            rs = prepstat.executeQuery();

            while (rs.next()) {
                newBean.setId(rs.getInt("id"));
                newBean.setMake(rs.getString("phone_manufacturer"));
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
        return newBean;
    }

    public static java.util.List getPhoneMakes() throws Exception {
        java.util.List makes = new java.util.ArrayList();

        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            query = "SELECT * from phone_make_list";
            prepstat = con.prepareStatement(query);
            rs = prepstat.executeQuery();

            while (rs.next()) {
                makes.add(new PhoneMake(rs.getInt("id"),
                                        rs.getString("phone_manufacturer")));
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

        return makes;
    }


    /*
        test
     */
    public static void main(String args[]) throws Exception {

    }


}
