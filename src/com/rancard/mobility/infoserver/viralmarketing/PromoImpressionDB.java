/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.infoserver.viralmarketing;

import com.rancard.common.DConnect;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author john
 */
public class PromoImpressionDB {

    public static void updatePromoViewDate(PromoImpression impression) {
        String sql = "UPDATE promo_impression_tracker set view_date = ? where account_id = ? and inventory_keyword = ? and msisdn = ?";
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            prepstat = con.prepareStatement(sql);
            prepstat.setTimestamp(1, new java.sql.Timestamp(impression.getViewDate().getTime()));
            prepstat.setString(2, impression.getAccountId());
            prepstat.setString(3, impression.getKeyword());
            prepstat.setString(4, impression.getMsisdn());

            prepstat.executeUpdate();

        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex1) {
                    System.out.println(ex1.getMessage());
                }
                con = null;
            }
            System.out.println(new java.util.Date() + ": error creating promo_campaign_hash entry(" + impression.getHashCode() + "): " + ex.getMessage());
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
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex1) {
                    System.out.println(ex1.getMessage());
                }
                con = null;
            }
            System.out.println(new java.util.Date() + ": error creating promo_campaign_hash entry(" + impression.getHashCode() + "): " + ex.getMessage());
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

    public static PromoImpression viewPromoImpression(String msisdn, String keyword, String accountID) throws Exception {
        System.out.println(new Date()+" :@PromoImpressionDB");
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        PromoImpression impression = new PromoImpression();

        try {
            con = DConnect.getConnection();

            SQL = "select * from promo_impression_tracker where msisdn = '" + msisdn + "' and inventory_keyword = '" + keyword + "' and account_id = '" + accountID + "'";
            System.out.println(new Date()+": Looking for recent promo SQL: "+SQL);
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
            
        System.out.println(new Date()+": Result found: MSISDN="+impression.getMsisdn()+" KEYWORD="+impression.getInventory_keyword()+" ACCOUNTID="+impression.getAccountId());
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

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        PromoImpression impression = new PromoImpression();

        try {
            con = DConnect.getConnection();

            SQL = "select * from promo_impression_tracker where hash_code = ?";

            prepstat = con.prepareStatement(SQL);

            prepstat.setLong(1, hashCode);

            rs = prepstat.executeQuery();

            while (rs.next()) {
                impression.setHashCode(rs.getLong("hash_code"));
                impression.setAccountId(rs.getString("account_id"));
                impression.setMsisdn(rs.getString("msisdn"));
                impression.setKeyword(rs.getString("promo_response_code"));
                impression.setViewDate(new java.util.Date(rs.getTimestamp("view_date").getTime()));
                impression.setInventory_keyword(rs.getString("inventory_keyword"));
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
            System.out.println(new java.util.Date() + ": error viewing promo_campaign_hash (" + hashCode + "): " + ex.getMessage());

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

        return impression;
    }

    public static synchronized void updateAdResponseSummary(PromoImpression impression) throws Exception {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String currDate = sdf.format(dt);

        try {
            con = DConnect.getConnection();
            SQL = "select promo_hits from ad_response_summary where account_id ='" + impression.getAccountId() + "' and keyword='" + impression.getInventory_keyword() + "'"
                    + " and promo_keyword = '" + impression.getKeyword() + "' and log_date = '" + currDate + "'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();

            if (!rs.next()) {
                //has not registered. Log new entry
                SQL = "Insert into ad_response_summary (account_id,keyword,promo_hits,log_date,promo_keyword) values(?,?,?,?,?)";
                prepstat = con.prepareStatement(SQL);
                prepstat.setString(1, impression.getAccountId());
                prepstat.setString(2, impression.getInventory_keyword());
                prepstat.setInt(3, 1);
                prepstat.setString(4, currDate);
                prepstat.setString(5, impression.getKeyword());
                prepstat.execute();
            } else {
                int total_promo_hits = rs.getInt("promo_hits") + 1;;

                SQL = "UPDATE ad_response_summary  SET promo_hits = " + total_promo_hits + " where keyword = ? and account_id = ? and promo_keyword = ? and log_date = ?";
                prepstat = con.prepareStatement(SQL);
                prepstat.setString(1, impression.getInventory_keyword());
                prepstat.setString(2, impression.getAccountId());
                prepstat.setString(3, impression.getKeyword());
                prepstat.setString(4, currDate);
                prepstat.execute();
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
            System.out.println(new java.util.Date() + ": error updating ad_response_summary: " + ex.getMessage());
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
