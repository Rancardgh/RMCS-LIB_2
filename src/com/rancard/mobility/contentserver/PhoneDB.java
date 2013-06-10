package com.rancard.mobility.contentserver;

import java.sql.PreparedStatement;
import com.rancard.common.DConnect;
import java.sql.ResultSet;
import java.sql.Connection;
import com.rancard.util.Page;

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
public abstract class PhoneDB {

    /*
     insert record
     */
    public static void insertPhone(String phoneModel, int phoneMake,
                                   String supportedFormats) throws Exception {

        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            query = "INSERT into phone_list(phone_model,phone_make_id,supported_formats_id) values(?,?,?)";

            prepstat = con.prepareStatement(query);
            prepstat.setString(1, phoneModel);
            prepstat.setInt(2, phoneMake);
            prepstat.setString(3, supportedFormats);

            prepstat.execute();
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            System.out.println(ex.getMessage()+ "\r\n\r\n" + ex.getCause());
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
    }

    /*
         Update list entry
     */
    public static void updatePhone(int id, String phoneModel, int phoneMake,
                                   String supportedFormats) throws Exception {
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            query = "UPDATE phone_list SET phone_model=?,phone_make_id=?,supported_formats_id=? WHERE" +
                    " phone_id=" + id;
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, phoneModel);
            prepstat.setInt(2, phoneMake);
            prepstat.setString(3, supportedFormats);
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
    public static void deletePhone(int id) throws Exception {
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            query = "DELETE from phone_list WHERE phone_id=?";
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
    public static Phone viewPhone(int phoneId) throws Exception {
        Phone newBean = new Phone();
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            query = "SELECT * from phone_list WHERE phone_id=?";
            prepstat = con.prepareStatement(query);
            prepstat.setInt(1, phoneId);
            rs = prepstat.executeQuery();

            while (rs.next()) {
                newBean.setId(rs.getInt("phone_id"));
                newBean.setModel(rs.getString("phone_model"));
                newBean.setMake(rs.getInt("phone_make_id"));
                newBean.setSupportedFormats(rs.getString("supported_formats_id"));
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

    public static java.util.List getDistinctMakes(String criterion) throws
            Exception {
        java.util.List items = new java.util.ArrayList();

        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        String queryEnding = "";

        if (criterion != null || !criterion.equals("")) {
            queryEnding = "where phone_make_id=" + criterion;
        }

        try {
            con = DConnect.getConnection();
            query = "SELECT * from phone_list " + queryEnding;
            prepstat = con.prepareStatement(query);
            rs = prepstat.executeQuery();

            while (rs.next()) {

                items.add(new Phone(rs.getInt("phone_id"),
                                    rs.getString("phone_model"),
                                    rs.getInt("phone_make_id"),
                                    rs.getString("supported_formats_id")));
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

        return items;
    }


    /*
        test
     */
    public static void main(String args[]) throws Exception {

    }


}
