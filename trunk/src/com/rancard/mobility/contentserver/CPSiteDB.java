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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author Kweku Tandoh
 */
public abstract class CPSiteDB {
    
    public static void createSite (String cpId, String siteName, String siteId, boolean check, String type) throws Exception {
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            query = "INSERT into cp_sites(cp_id,site_name,check_user,site_id, site_type) values(?,?,?,?,?)";
            
            prepstat = con.prepareStatement (query);
            
            prepstat.setString (1, cpId);
            prepstat.setString (2, siteName);
            if(check == true){
                prepstat.setInt (3, 1);
            }else{
                prepstat.setInt (3, 0);
            }
            prepstat.setString (4, siteId);
            prepstat.setString (5, type);
            
            prepstat.execute ();
            
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
    }
    
    public static void updateSiteName (String siteName, String siteId) throws Exception {
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            query = "update cp_sites set site_name=? where site_id=?";
            
            prepstat = con.prepareStatement (query);
            
            prepstat.setString (1, siteName);
            prepstat.setString (2, siteId);
            
            prepstat.execute ();
            
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
    }
    
    public static void updateSiteAuthentication (boolean check, String siteId) throws Exception {
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            query = "update cp_sites set check_user=? where site_id=?";
            
            prepstat = con.prepareStatement (query);
            
            if(check == true){
                prepstat.setInt (1, 1);
            }else{
                prepstat.setInt (1, 0);
            }
            prepstat.setString (2, siteId);
            
            prepstat.execute ();
            
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
    }
    
    public static void deleteSite (String siteId) throws Exception {
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            query = "DELETE from cp_sites WHERE site_id=?";
            prepstat = con.prepareStatement (query);
            
            prepstat.setString (1, siteId);
            
            prepstat.execute ();
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
    }
    
    public static void deleteAllSitesForCP (String cpId) throws Exception {
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            query = "DELETE from cp_sites WHERE cp_id=?";
            prepstat = con.prepareStatement (query);
            
            prepstat.setString (1, cpId);
            
            prepstat.execute ();
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
    }
    
    public static CPSite viewSite (String siteId) throws Exception {
        CPSite site = new CPSite ();
        
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        //------log Statement
        System.out.println(new java.util.Date()+ ":@com.rancard.mobility.contentserver.CPSiteDB...");
        System.out.println(new java.util.Date()+ ": viewing cp_site ("+ siteId +")...");
        
        try {
            con = DConnect.getConnection ();
            query = "select * from cp_sites where site_id=?";
            prepstat = con.prepareStatement (query);
            
            prepstat.setString (1, siteId);
            
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                site.setCpId (rs.getString ("cp_id"));
                site.setCpSiteId (rs.getString ("site_id"));
                site.setCpSiteName (rs.getString ("site_name"));
                if(rs.getInt ("check_user") == 1){
                    site.setCheckUser (true);
                }else{
                    site.setCheckUser (false);
                }
                site.setSiteType (rs.getString ("site_type"));
                
                //log statement: info
                System.out.println(new java.util.Date()+ ": cp_site ("+ siteId +") found!");
                
            }
            if(site.getCpSiteId()==null || "".equals(site.getCpSiteId()) ){
               System.out.println(new java.util.Date()+ ": cp_site ("+ siteId +") not found!"); 
            }
        } catch (Exception ex) {
            
            //error log
            System.out.println(new java.util.Date()+ ": error viewing cp_site ("+ siteId +"): " + ex.getMessage() );
            
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
        
        return site;
    }
    
    public static String getCpIdForSite (String siteId) throws Exception {
        String query;
        String id = "";
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            query = "select cp_id from cp_sites where site_id=?";
            prepstat = con.prepareStatement (query);
            
            prepstat.setString (1, siteId);
            
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                id = rs.getString ("cp_id");
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
        
        return id;
    }
    
    public static ArrayList viewAllSitesForCP (String cpId) throws Exception {
        ArrayList sites = new ArrayList ();
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            query = "select * from cp_sites where cp_id=?";
            prepstat = con.prepareStatement (query);
            
            prepstat.setString (1, cpId);
            
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                CPSite site = new CPSite ();
                
                site.setCpId (rs.getString ("cp_id"));
                site.setCpSiteId (rs.getString ("site_id"));
                site.setCpSiteName (rs.getString ("site_name"));
                if(rs.getInt ("check_user") == 1){
                    site.setCheckUser (true);
                }else{
                    site.setCheckUser (false);
                }
                site.setSiteType (rs.getString ("site_type"));
                
                sites.add (site);
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
        
        return sites;
    }
    
    public static ArrayList viewAffiliatedSitesForCP (String cpId) throws Exception {
        ArrayList sites = new ArrayList ();
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            query = "select * from cp_sites where cp_id='" + cpId + "' or cp_id in (select cs_id from cs_cp_relationship where list_id='" + cpId + "')";
            prepstat = con.prepareStatement (query);
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                CPSite site = new CPSite ();
                site.setCpId (rs.getString ("cp_id"));
                site.setCpSiteId (rs.getString ("site_id"));
                site.setCpSiteName (rs.getString ("site_name"));
                if(rs.getInt ("check_user") == 1){
                    site.setCheckUser (true);
                }else{
                    site.setCheckUser (false);
                }
                site.setSiteType (rs.getString ("site_type"));
                
                sites.add (site);
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
        
        return sites;
    }
    
    public static ArrayList viewCPsWithSites () throws Exception {
        ArrayList cps = new ArrayList ();
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            query = "select distinct cp_id from cp_sites";
            prepstat = con.prepareStatement (query);
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                cps.add (rs.getString ("cp_id"));
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
        
        return cps;
    }
}
