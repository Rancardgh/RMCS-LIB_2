/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.infoserver.viralmarketing;

import com.rancard.common.DConnect;
import com.rancard.util.DateUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

/**
 *
 * @author john
 */
public class PromoImpressionDB {

    public static void updatePromoViewDate(PromoImpression impression) throws Exception {
        String sql = "UPDATE promo_impression_tracker set view_date = '"
                + DateUtil.convertToMySQLTimeStamp(impression.getViewDate()) + "' "
                + "where hash_code = " + impression.getHashCode();

        Connection conn = null;

        System.out.println(new Date() + " " + PromoImpressionDB.class + ": Will update promo view date");

        try {
            conn = DConnect.getConnection();
            conn.createStatement().executeUpdate(sql);

        } catch (Exception ex) {
            System.out.println(new java.util.Date() + ": error creating promo_campaign_hash entry(" + impression.getHashCode() + "): " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {

            if (conn != null) {
                conn.close();
            }
        }

    }

    public static void createEntry(PromoImpression impression) throws Exception {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            SQL = "insert into promo_impression_tracker (hash_code, msisdn, account_id, promo_response_code, view_date, inventory_keyword) "
                    + "values(?, ?, ?, ?, ?, ?)";

            prepstat = con.prepareStatement(SQL);

            prepstat.setLong(1, impression.getHashCode());
            prepstat.setString(2, impression.getMsisdn());
            prepstat.setString(3, impression.getAccountId());
            prepstat.setString(4, impression.getKeyword());
            prepstat.setTimestamp(5, new java.sql.Timestamp(impression.getViewDate().getTime()));
            prepstat.setString(6, impression.getInventory_keyword());

            prepstat.execute();

        } catch (Exception ex) {
            System.out.println(new java.util.Date() + ": error creating promo_campaign_hash entry(" + impression.getHashCode() + "): " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (prepstat != null) {
                prepstat.close();
            }
            if (con != null) {
                con.close();
            }
        }

    }

    public static PromoImpression viewPromoImpression(String msisdn, String keyword, String accountID) throws Exception {
        System.out.println(new Date() + " :@PromoImpressionDB");
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        PromoImpression impression = new PromoImpression();

        try {
            con = DConnect.getConnection();

            SQL = "select * from promo_impression_tracker where msisdn = '" + msisdn + "' and inventory_keyword = '" + keyword + "' and account_id = '" + accountID + "'";
            System.out.println(new Date() + ": Looking for recent promo SQL: " + SQL);
            prepstat = con.prepareStatement(SQL);

            rs = prepstat.executeQuery();

            while (rs.next()) {
                impression.setHashCode(rs.getLong("hash_code"));
                impression.setAccountId(rs.getString("account_id"));
                impression.setMsisdn(rs.getString("msisdn"));
                impression.setKeyword(rs.getString("promo_response_code"));
                impression.setViewDate(new java.util.Date(rs.getTimestamp("view_date").getTime()));
                impression.setInventory_keyword(rs.getString("inventory_keyword"));
            }

            System.out.println(new Date() + ": Result found: MSISDN=" + impression.getMsisdn() + " KEYWORD=" + impression.getInventory_keyword() + " ACCOUNTID=" + impression.getAccountId());
        } catch (Exception ex) {
            //error log
            System.out.println(new java.util.Date() + ": error viewing promo_campaign_hash (" + impression.getHashCode() + "): " + ex.getMessage());

        } finally {
            if (rs != null) {
                rs.close();
            }
            if (prepstat != null) {
                prepstat.close();
            }
            if (con != null) {
                con.close();
            }
        }

        return impression;
    }

    public static PromoImpression viewPromoImpression(long hashCode) throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PromoImpression impression = new PromoImpression();

        try {
            con = DConnect.getConnection();

            String SQL = "select * from promo_impression_tracker where hash_code = " + hashCode;
            System.out.println(new Date() + " " + PromoImpressionDB.class + ": Will check if promo exists: " + SQL);


            rs = con.createStatement().executeQuery(SQL);

            while (rs.next()) {
                impression.setHashCode(rs.getLong("hash_code"));
                impression.setAccountId(rs.getString("account_id"));
                impression.setMsisdn(rs.getString("msisdn"));
                impression.setKeyword(rs.getString("promo_response_code"));
                impression.setViewDate(new java.util.Date(rs.getTimestamp("view_date").getTime()));
                impression.setInventory_keyword(rs.getString("inventory_keyword"));
                System.out.println(new Date() + " " + PromoImpressionDB.class + ": Promo exists");
                break;
            }

        } catch (Exception ex) {
            //error log
            System.out.println(new java.util.Date() + ": error viewing promo_campaign_hash (" + hashCode + "): " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (con != null) {
                con.close();
            }
        }

        return impression;
    }

    public static synchronized void updateAdResponseSummary(PromoImpression impression) throws Exception {
        ResultSet rs = null;
        Connection con = null;


        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String currDate = sdf.format(dt);

        System.out.println(new java.util.Date() + " " + PromoImpressionDB.class + ": Will update ad_response_summary");
        try {
            con = DConnect.getConnection();

            String selectQuery = "select promo_hits from ad_response_summary where account_id ='" + impression.getAccountId() + "' and keyword='" + impression.getInventory_keyword() + "'"
                    + " and promo_keyword = '" + impression.getKeyword() + "' and log_date = '" + currDate + "'";
            System.out.println(new java.util.Date() + " " + PromoImpressionDB.class + ": Will firsh check if ad_response_summary exists: " + selectQuery);
            rs = con.createStatement().executeQuery(selectQuery);

            if (!rs.next()) {
                //has not registered. Log new entry
                String insertStmt = "Insert into ad_response_summary (account_id,keyword,promo_hits,log_date,promo_keyword) "
                        + "values('" + impression.getAccountId() + "', '" + impression.getInventory_keyword() + "', 1, '"
                        + currDate + "', '" + impression.getKeyword() + "')";
                System.out.println(new java.util.Date() + " " + PromoImpressionDB.class + ": ad_response_summary does not exists. Will insert new one: " + insertStmt);
                con.createStatement().execute(insertStmt);

            } else {
                int total_promo_hits = rs.getInt("promo_hits") + 1;

                String updateStmt = "UPDATE ad_response_summary SET promo_hits = " + total_promo_hits + " where "
                        + "keyword = '" + impression.getInventory_keyword() + "' and account_id = '" + impression.getAccountId() + "' "
                        + "and promo_keyword = '" + impression.getKeyword() + "' and log_date = '" + currDate + "'";
                System.out.println(new java.util.Date() + " " + PromoImpressionDB.class + ": ad_response_summary exists. Will update hits: " + updateStmt);

                con.createStatement().executeUpdate(updateStmt);
            }

        } catch (Exception ex) {
            System.out.println(new java.util.Date() + " " + PromoImpressionDB.class + ": error updating ad_response_summary: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (con != null) {
                con.close();
            }
        }

    }
}
