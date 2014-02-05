/*
 * CPSiteDB.java
 *
 * Created on August 23, 2006, 1:30 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.rancard.mobility.contentserver;

import com.rancard.common.DConnect;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Kweku Tandoh
 */
public class CPSiteDB {

    public static void createSite(String cpID, String siteName, String siteID, boolean check, String type) throws Exception {
        Connection conn = null;

        try {
            conn = DConnect.getConnection();
            int checkUser = (check) ? 1 : 0;
            String query = "INSERT into cp_sites(cp_id, site_name, check_user, site_id, site_type) values('" + cpID + "', '" + siteName + "', "
                    + checkUser + ",'" + siteID + "', '" + type + "')";
            System.out.println(new Date() + "\t[" + CPSiteDB.class + "]\tINFO\tCreating site: " + query);

            conn.createStatement().execute(query);

        } catch (Exception ex) {
            System.out.println(new Date() + "\t[" + CPSiteDB.class + "]\tERROR\tCreating site: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static void updateSiteName(String siteName, String siteID) throws Exception {
        Connection conn = null;

        try {
            conn = DConnect.getConnection();
            String query = "update cp_sites set site_name = '" + siteName + "' where site_id = '" + siteID + "'";
            System.out.println(new Date() + "\t[" + CPSiteDB.class + "]\tINFO\tUpdating site name: " + query);

            conn.createStatement().executeUpdate(query);
        } catch (Exception ex) {
            System.out.println(new Date() + "\t[" + CPSiteDB.class + "]\tERROR\tUpdating site name: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static void updateSiteAuthentication(boolean check, String siteID) throws Exception {
        Connection conn = null;

        try {
            conn = DConnect.getConnection();
            int checkUser = (check) ? 1 : 0;
            String query = "update cp_sites set check_user = " + checkUser + " where site_id = '" + siteID + "'";
            System.out.println(new Date() + "\t[" + CPSiteDB.class + "]\tINFO\tUpdating site authentication: " + query);

            conn.createStatement().executeUpdate(query);

        } catch (Exception ex) {
            System.out.println(new Date() + "\t[" + CPSiteDB.class + "]\tERROR\tUpdating site authentication: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static void deleteSite(String siteID) throws Exception {
        Connection conn = null;

        try {
            conn = DConnect.getConnection();
            String query = "DELETE from cp_sites WHERE site_id = '" + siteID + "'";
            System.out.println(new Date() + "\t[" + CPSiteDB.class + "]\tINFO\tDeleting site: " + query);

            conn.createStatement().execute(query);

        } catch (Exception ex) {
            System.out.println(new Date() + "\t[" + CPSiteDB.class + "]\tERROR\tDeleting site: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static void deleteAllSitesForCP(String cpID) throws Exception {
        Connection conn = null;

        try {
            conn = DConnect.getConnection();
            String query = "DELETE from cp_sites WHERE cp_id = '" + cpID + "'";
            System.out.println(new Date() + "\t[" + CPSiteDB.class + "]\tINFO\tDeleting sites: " + query);

            conn.createStatement().execute(query);

        } catch (Exception ex) {
            System.out.println(new Date() + "\t[" + CPSiteDB.class + "]\tERROR\tDeleting sites: " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static CPSite viewSite(String siteID) throws Exception {
        CPSite site = null;
        ResultSet rs = null;
        Connection conn = null;


        try {
            conn = DConnect.getConnection();
            String query = "select * from cp_sites where site_id = '" + siteID + "'";
            System.out.println(new Date() + "\t[" + CPSiteDB.class + "]\tINFO\tGetting site: " + query);

            rs = conn.createStatement().executeQuery(query);

            if (rs.next()) {
                site = new CPSite(rs.getString("cp_id"), rs.getString("site_name"), rs.getString("site_id"),
                        rs.getString("site_type"), rs.getBoolean("check_user"));

            }
            return site;
        } catch (Exception ex) {
            System.out.println(new Date() + "\t[" + CPSiteDB.class + "]\tERROR\tGetting site: " + ex.getMessage());
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

    public static String getCpIdForSite(String siteID) throws Exception {
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DConnect.getConnection();
            String query = "select cp_id from cp_sites where site_id = '" + siteID + "'";
            System.out.println(new Date() + "\t[" + CPSiteDB.class + "]\tINFO\tGetting siteID: " + query);

            rs = conn.createStatement().executeQuery(query);

            if (rs.next()) {
                return rs.getString("cp_id");
            }
            return null;
        } catch (Exception ex) {
            System.out.println(new Date() + "\t[" + CPSiteDB.class + "]\tERROR\tGetting siteID: " + ex.getMessage());
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

    public static List<CPSite> viewAllSitesForCP(String cpID) throws Exception {
        List<CPSite> sites = new ArrayList<CPSite>();
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DConnect.getConnection();
            String query = "select * from cp_sites where cp_id= '" + cpID + "'";
            System.out.println(new Date() + "\t[" + CPSiteDB.class + "]\tINFO\tGetting sites: " + query);

            rs = conn.createStatement().executeQuery(query);

            while (rs.next()) {
                sites.add(new CPSite(rs.getString("cp_id"), rs.getString("site_name"), rs.getString("site_id"),
                        rs.getString("site_type"), rs.getBoolean("check_user")));
            }
            return sites;
        } catch (Exception ex) {
            System.out.println(new Date() + "\t[" + CPSiteDB.class + "]\tERROR\tGetting sites: " + ex.getMessage());
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

    public static List<CPSite> viewAffiliatedSitesForCP(String cpID) throws Exception {
        List<CPSite> sites = new ArrayList<CPSite>();
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DConnect.getConnection();
            String query = "select * from cp_sites where cp_id = '" + cpID + "' or cp_id in (select cs_id from cs_cp_relationship where list_id='" + cpID + "')";
            System.out.println(new Date() + "\t[" + CPSiteDB.class + "]\tINFO\tGetting affiliated sites: " + query);
            
            rs = conn.createStatement().executeQuery(query);

            while (rs.next()) {
                sites.add(new CPSite(rs.getString("cp_id"), rs.getString("site_name"), rs.getString("site_id"),
                        rs.getString("site_type"), rs.getBoolean("check_user")));
            }
            return sites;

        } catch (Exception ex) {
            System.out.println(new Date() + "\t[" + CPSiteDB.class + "]\tERROR\tGetting affiliated sites: " + ex.getMessage());
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

    public static List<String> viewCPsWithSites() throws Exception {
        List<String> cps = new ArrayList<String>();
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DConnect.getConnection();
            String query = "select distinct cp_id from cp_sites";
            System.out.println(new Date() + "\t[" + CPSiteDB.class + "]\tINFO\tGetting distinct cps: " + query);
            
            rs = conn.createStatement().executeQuery(query);

            while (rs.next()) {
                cps.add(rs.getString("cp_id"));
            }
            return cps;
        } catch (Exception ex) {
            System.out.println(new Date() + "\t[" + CPSiteDB.class + "]\tERROR\tGetting distinct cps: " + ex.getMessage());
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
}
