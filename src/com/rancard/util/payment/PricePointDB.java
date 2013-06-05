/*
 * PricePointDB.java
 *
 * Created on May 24, 2007, 10:53 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.util.payment;

import com.rancard.common.DConnect;
import com.rancard.mobility.infoserver.common.services.UserServiceDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author Messenger
 */
public abstract class PricePointDB {
    
    public static void createPricePoint (PricePoint pricePoint) throws Exception {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            SQL =   "";
            prepstat = con.prepareStatement (SQL);
            rs = prepstat.executeQuery ();
            
            if(!rs.next ()){
                
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
        } finally {
            if (con != null) {
                con.close ();
            }
        }
    }
    
    public static void updatePricePoint (PricePoint pricePoint) throws Exception {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            SQL =   "";
            prepstat = con.prepareStatement (SQL);
            rs = prepstat.executeQuery ();
            
            if(!rs.next ()){
                
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
        } finally {
            if (con != null) {
                con.close ();
            }
        }
    }
    
    public static void updatePricePointValue (String pricePointId, String newValue) throws Exception {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            SQL =   "";
            prepstat = con.prepareStatement (SQL);
            rs = prepstat.executeQuery ();
            
            if(!rs.next ()){
                
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
        } finally {
            if (con != null) {
                con.close ();
            }
        }
    }
    
    public static void updatePricePointCurrency (String pricePointId, String newCurrency) throws Exception {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            SQL =   "";
            prepstat = con.prepareStatement (SQL);
            rs = prepstat.executeQuery ();
            
            if(!rs.next ()){
                
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
        } finally {
            if (con != null) {
                con.close ();
            }
        }
    }
    
    public static void updatePricePointURL (String pricePointId, String newURL) throws Exception {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            SQL =   "";
            prepstat = con.prepareStatement (SQL);
            rs = prepstat.executeQuery ();
            
            if(!rs.next ()){
                
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
        } finally {
            if (con != null) {
                con.close ();
            }
        }
    }
    
    public static void deletePricePoint (String pricePointId) throws Exception {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            SQL =   "";
            prepstat = con.prepareStatement (SQL);
            rs = prepstat.executeQuery ();
            
            if(!rs.next ()){
                
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
        } finally {
            if (con != null) {
                con.close ();
            }
        }
    }
    
    public static PricePoint viewPricePointFor (String[] pricePointIds, String msisdn) throws Exception {
        String SQL;
        PricePoint point = new PricePoint ();
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        String string = "";
        if(pricePointIds != null || pricePointIds.length > 0){
            string = "'" + pricePointIds[0] + "',";
            for(int i = 1; i < pricePointIds.length; i++){
                string = string + "'" + pricePointIds[i] + "',";
            }
        }
        string = string.substring (0, string.lastIndexOf (","));
        
        try {
            con = DConnect.getConnection ();
            SQL =   "select * from price_points where price_point_id in (" + string + ")";
            prepstat = con.prepareStatement (SQL);
            rs = prepstat.executeQuery ();
            
            while(rs.next ()){
                String network = rs.getString ("network_prefix");
                if (msisdn.substring (msisdn.indexOf ("+") + 1, network.length () + msisdn.indexOf ("+") + 1).equals (network)) {
                    point.setPricePointId (rs.getString ("price_point_id"));
                    point.setBillingUrl (rs.getString ("billing_url"));
                    point.setCurrency (rs.getString ("currency"));
                    point.setNetworkPrefix (network);
                    point.setBillingMech (rs.getString ("billing_mech"));
                    point.setValue (rs.getString ("value"));
                    break;
                }
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
        } finally {
            if (con != null) {
                con.close ();
            }
            return point;
        }
    }
    
    public static PricePoint viewPricePoint (String pricePointId) throws Exception {        
        ResultSet rs = null;
        Connection conn = null;       
        
        try {
            conn = DConnect.getConnection ();
            
            String sql =   "select * from price_points where price_point_id = '" + pricePointId + "'";
            System.out.println(new Date() + ": " + UserServiceDB.class + ":DEBUG SQL Query to get price point: " + sql);            
            
            rs = conn.createStatement().executeQuery(sql);
            
            if(rs.next ()){
                return new PricePoint(rs.getString ("price_point_id"), rs.getString ("network_prefix"), rs.getString ("value"),
                        rs.getString ("currency"), rs.getString ("billing_mech"), rs.getString ("billing_url"));                
            }
            
            return null;
        } catch (Exception ex) {
            System.out.println(new java.util.Date() + ": " + UserServiceDB.class + ":ERROR Getting price point " + pricePointId + ": " + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if(rs != null){
                rs.close();
            }
            if (conn != null) {
                conn.close ();
            }
            
        }
    }
    
    public static ArrayList viewPricePointsSupportedByAccount (String accountId) throws Exception {
        ArrayList pricePoints = new ArrayList ();
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            SQL =   "SELECT p.* FROM price_points p inner join cp_connections c on p.network_prefix=c.allowed_networks where c.list_id='" + accountId + "' order by p.network_prefix";
            prepstat = con.prepareStatement (SQL);
            rs = prepstat.executeQuery ();
            
            while (rs.next ()){
                PricePoint pricePoint = new PricePoint ();
                
                pricePoint.setBillingUrl (rs.getString ("p.billing_url"));
                pricePoint.setCurrency (rs.getString ("p.currency"));
                pricePoint.setNetworkPrefix (rs.getString ("p.network_prefix"));
                pricePoint.setPricePointId (rs.getString ("p.price_point_id"));
                pricePoint.setValue (rs.getString ("p.value"));
                pricePoint.setBillingMech (rs.getString ("p.billing_mech"));
                
                pricePoints.add (pricePoint);
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
        } finally {
            if (con != null) {
                con.close ();
            }
            return pricePoints;
        }
    }
    
    public static ArrayList viewPricePoints (String[] pricePointIds) throws Exception {
        ArrayList pricePoints = new ArrayList ();
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        String string = "";
        if(pricePointIds != null || pricePointIds.length > 0){
            string = "'" + pricePointIds[0] + "',";
            for(int i = 1; i < pricePointIds.length; i++){
                string = string + "'" + pricePointIds[i] + "',";
            }
        }
        string = string.substring (0, string.lastIndexOf (","));
        
        try {
            con = DConnect.getConnection ();
            SQL =   "select * from price_points where price_point_id in (" + string + ")";
            prepstat = con.prepareStatement (SQL);
            rs = prepstat.executeQuery ();
            
            while(rs.next ()){
                PricePoint pricePoint = new PricePoint ();
                
                pricePoint.setBillingUrl (rs.getString ("billing_url"));
                pricePoint.setCurrency (rs.getString ("currency"));
                pricePoint.setNetworkPrefix (rs.getString ("network_prefix"));
                pricePoint.setPricePointId (rs.getString ("price_point_id"));
                pricePoint.setValue (rs.getString ("value"));
                pricePoint.setBillingMech (rs.getString ("billing_mech"));
                
                pricePoints.add (pricePoint);
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
        } finally {
            if (con != null) {
                con.close ();
            }
            return pricePoints;
        }
    }
    
    public static ArrayList viewPricePoints_al () throws Exception {
        ArrayList pricePoints = new ArrayList ();
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            SQL =   "select * from price_points";
            prepstat = con.prepareStatement (SQL);
            rs = prepstat.executeQuery ();
            
            while(rs.next ()){
                PricePoint pricePoint = new PricePoint ();
                
                pricePoint.setBillingUrl (rs.getString ("billing_url"));
                pricePoint.setCurrency (rs.getString ("currency"));
                pricePoint.setNetworkPrefix (rs.getString ("network_prefix"));
                pricePoint.setPricePointId (rs.getString ("price_point_id"));
                pricePoint.setValue (rs.getString ("value"));
                pricePoint.setBillingMech (rs.getString ("billing_mech"));
                
                pricePoints.add (pricePoint);
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
        } finally {
            if (con != null) {
                con.close ();
            }
            return pricePoints;
        }
    }
    
    public static HashMap viewPricePoints_hm () throws Exception {
        HashMap pricePoints = new HashMap ();
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            SQL =   "select * from price_points";
            prepstat = con.prepareStatement (SQL);
            rs = prepstat.executeQuery ();
            
            while(rs.next ()){
                String key = rs.getString ("price_point_id");
                PricePoint pricePoint = new PricePoint ();
                
                pricePoint.setBillingUrl (rs.getString ("billing_url"));
                pricePoint.setCurrency (rs.getString ("currency"));
                pricePoint.setNetworkPrefix (rs.getString ("network_prefix"));
                pricePoint.setPricePointId (rs.getString ("price_point_id"));
                pricePoint.setValue (rs.getString ("value"));
                pricePoint.setBillingMech (rs.getString ("billing_mech"));
                
                pricePoints.put ( key, pricePoint);
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
        } finally {
            if (con != null) {
                con.close ();
            }
            return pricePoints;
        }
    }
    
    public static boolean isPricingValid (String[] pricePointIds) throws Exception {
        boolean isValid = true;
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        String string = "";
        if(pricePointIds != null || pricePointIds.length > 0){
            string = "'" + pricePointIds[0] + "',";
            for(int i = 1; i < pricePointIds.length; i++){
                string = string + "'" + pricePointIds[i] + "',";
            }
        }
        string = string.substring (0, string.lastIndexOf (","));
        
        try {
            con = DConnect.getConnection ();
            SQL =   "select count(network_prefix) as instances from price_points where price_point_id in (" + string +") group by network_prefix";
            prepstat = con.prepareStatement (SQL);
            rs = prepstat.executeQuery ();
            
            while(rs.next ()){
                if (rs.getInt ("instances") != 1) {
                    isValid = false;
                    break;
                }else{
                    isValid = true;
                }
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
        } finally {
            if (con != null) {
                con.close ();
            }
            return isValid;
        }        
    }
}
