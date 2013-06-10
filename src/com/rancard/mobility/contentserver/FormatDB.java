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
public abstract class FormatDB {

    /*
     insert record
     */
    public static void insertFormat(String fileExt, String push,
                                    String mimeType) throws Exception {

        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            query =
                    "INSERT into format_list(file_ext,push_bearer,mime_type) values(?,?,?)";

            prepstat = con.prepareStatement(query);
            prepstat.setString(1, fileExt);
            prepstat.setString(2, push);
            prepstat.setString(3, mimeType);

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
    public static void updateFormat(int id, String fileExt, String push,
                                    String mimeType) throws Exception {
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            query =
                    "UPDATE format_list SET file_ext=?,push_bearer=?,mime_type=? WHERE" +
                    " format_id=" + id;
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, fileExt);
            prepstat.setString(2, push);
            prepstat.setString(3, mimeType);
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
    public static void deleteFormat(int id) throws Exception {
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            query = "DELETE from format_list WHERE format_id=?";
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
    public static Format viewFormat(int formatId) throws Exception {
        Format newBean = new Format();
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            query = "SELECT * from format_list WHERE format_id=?";
            prepstat = con.prepareStatement(query);
            prepstat.setInt(1, formatId);
            rs = prepstat.executeQuery();

            while (rs.next()) {
                newBean.setId(rs.getInt("format_id"));
                newBean.setFileExt(rs.getString("file_ext"));
                newBean.setPushBearer(rs.getString("push_bearer"));
                newBean.setMimeType(rs.getString("mime_type"));
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

    public static java.util.ArrayList viewFormats() throws Exception {

        java.util.ArrayList formats = new java.util.ArrayList();
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            query = "SELECT * from format_list";
            prepstat = con.prepareStatement(query);
            rs = prepstat.executeQuery();

            while (rs.next()) {
                Format newBean = new Format();
                newBean.setId(rs.getInt("format_id"));
                newBean.setFileExt(rs.getString("file_ext"));
                newBean.setPushBearer(rs.getString("push_bearer"));
                newBean.setMimeType(rs.getString("mime_type"));
                formats.add(newBean);
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
        return formats;
    }


    /*
        test
     */
    public static void main(String args[]) throws Exception {

    }


}
