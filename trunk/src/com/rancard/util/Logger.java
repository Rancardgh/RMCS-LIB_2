/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rancard.util;
import java.sql.*;
import com.rancard.common.DConnect;

/**
 *
 * @author nii
 */
public class Logger {

    public static void logServiceRequest(String keyword, String accountId, String origin, String dest ,String timeSent, String msg, String siteId, String smscId) throws Exception {

        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        //log statements
        System.out.println(new java.util.Date()+ ": updating service_usage_log with request details..");
        System.out.println(new java.util.Date()+ ": service_usage_log details: " +
                "keyword="+keyword+ ", " +
                "account_id="+accountId+", " +
                "origin="+origin+", " +
                "dest="+dest+ ", " +
                "timeSent="+("".equals(timeSent) ? new java.util.Date() : timeSent )+", " +
                "msg="+msg+", " +
                "siteId=" + siteId + ", " +
                "smsc=" + smscId);

        try {
            con = DConnect.getConnection();

            query ="insert into service_usage_log (keyword, account_id, to_number, time_received, from_number, msg, site_id, smscId, time_sent) values (?,?,?,?,?,?,?,?,?)";

            prepstat = con.prepareStatement(query);

            prepstat.setString(1, keyword);
            prepstat.setString(2, accountId);
            prepstat.setString(3, dest);
            prepstat.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
            prepstat.setString(5, origin);
            prepstat.setString(6, msg);
            prepstat.setString(7, siteId);
            prepstat.setString(8, smscId);
            try{
                prepstat.setTimestamp(9, java.sql.Timestamp.valueOf(timeSent));
            }catch (Exception e) {
                prepstat.setTimestamp(9, new java.sql.Timestamp(new java.util.Date().getTime()));
            }


            prepstat.execute();

            //log update success statement
            System.out.println(new java.util.Date()+ ": service_usage_log updated successfully!");


        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception ex1) {
                    System.out.println(ex.getMessage());
                }
                con = null;
            }

            System.out.println(new java.util.Date()+ ": error updating service_usage_log: " + ex.getMessage());

        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (Exception e) {
                    System.out.println(e.getMessage()); ;
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                con = null;
            }
        }
    }


    public static void logServiceRequest(String keyword, String accountId, String origin, String dest ,String timeSent, String msg, String siteId, String smscId,String promoId) throws Exception {

        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        //log statements
        System.out.println(new java.util.Date()+ ": updating service_usage_log with request details..");
        System.out.println(new java.util.Date()+ ": service_usage_log details: " +
                "keyword="+keyword+ ", " +
                "account_id="+accountId+", " +
                "origin="+origin+", " +
                "dest="+dest+ ", " +
                "timeSent="+("".equals(timeSent) ? new java.util.Date() : timeSent )+", " +
                "msg="+msg+", " +
                "promoId="+promoId+", " +
                "siteId=" + siteId + ", " +
                "smsc=" + smscId);

        try {
            con = DConnect.getConnection();

            query ="insert into service_usage_log (keyword, account_id, to_number, time_received, from_number, msg, site_id, smscId, time_sent,promo_id) values (?,?,?,?,?,?,?,?,?,?)";

            prepstat = con.prepareStatement(query);

            prepstat.setString(1, keyword);
            prepstat.setString(2, accountId);
            prepstat.setString(3, dest);
            prepstat.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
            prepstat.setString(5, origin);
            prepstat.setString(6, msg);
            prepstat.setString(7, siteId);
            prepstat.setString(8, smscId);
            try{
                prepstat.setTimestamp(9, java.sql.Timestamp.valueOf(timeSent));
            }catch (Exception e) {
                prepstat.setTimestamp(9, new java.sql.Timestamp(new java.util.Date().getTime()));
            }
            prepstat.setString(10, promoId);

            prepstat.execute();

            //log update success statement
            System.out.println(new java.util.Date()+ ": service_usage_log updated successfully!");


        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception ex1) {
                    System.out.println(ex.getMessage());
                }
                con = null;
            }

            System.out.println(new java.util.Date()+ ": error updating service_usage_log: " + ex.getMessage());

        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (Exception e) {
                    System.out.println(e.getMessage()); ;
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                con = null;
            }
        }
    }



}
