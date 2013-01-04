package com.rancard.mobility.contentserver;

import java.sql.*;
import java.util.*;
import com.rancard.common.DConnect;

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
public abstract class ContentTypeDB {
    
    /*
     insert record
     */
    public static void insertServiceType (String serviceName, String serviceUrl, int serviceType,  int parentServiceType) throws Exception {
        
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            query = "INSERT into service_route(service_type,service_url,service_name,parent_service_type) values(?,?,?,?)";
            
            prepstat = con.prepareStatement (query);
            prepstat.setString (1, serviceName);
            prepstat.setString (2, serviceUrl);
            prepstat.setInt (3, serviceType);
            prepstat.setInt (4, parentServiceType);
            
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
    
    /*
         Update log entry - usually to set time and status of download
     */
    public static void updateServiceType (int oldServiceType, String serviceName, int parentServiceType, String serviceUrl) throws Exception {
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            query = "UPDATE service_route SET service_name=?,service_url=?,parent_service_type=? WHERE service_type=" + oldServiceType;
            prepstat = con.prepareStatement (query);
            prepstat.setString (1, serviceName);
            prepstat.setInt (2, parentServiceType);
            prepstat.setString (3, serviceUrl);
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
    
    /*
         Deletes a lod entry
     */
    public static void deleteServicType (int serviceType) throws Exception {
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            query = "DELETE from service_route WHERE service_type=?";
            prepstat = con.prepareStatement (query);
            prepstat.setInt (1, serviceType);
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
    
    /*
         Retrieves a log item as a bean
     */
    public static ContentType viewServiceType (int serviceType) throws Exception {
        ContentType newBean = new ContentType ();
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            query = "SELECT * from service_route WHERE service_type=?";
            prepstat = con.prepareStatement (query);
            prepstat.setInt (1, serviceType);
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                newBean.setServiceName (rs.getString ("service_name"));
                newBean.setServiceType (rs.getInt ("service_type"));
                newBean.setParentServiceType (rs.getInt ("parent_service_type"));
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
        return newBean;
    }
    
    public static java.util.List getDistinctTypes () throws Exception {
        java.util.List types = new java.util.ArrayList ();
        
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            //query = "SELECT * from type_list";
            query = "SELECT * from service_route";
            prepstat = con.prepareStatement (query);
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                types.add (new ContentType (rs.getString ("service_name"),
                        rs.getInt ("service_type"),
                        rs.getInt ("parent_service_type")));
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
        
        return types;
    }
    
    public static java.util.List getDistinctTypes (int parentServiceType) throws Exception {
        java.util.List types = new java.util.ArrayList ();
        
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            //query = "SELECT * from type_list";
            query = "SELECT * from service_route where parent_service_type=" + parentServiceType;
            prepstat = con.prepareStatement (query);
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                types.add (new ContentType (rs.getString ("service_name"),
                        rs.getInt ("service_type"),
                        rs.getInt ("parent_service_type")));
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
        
        return types;
    }
    
    public static ArrayList getDistinctTypes (int parentServiceType, String accountId) throws Exception {
        ArrayList types = new ArrayList ();
        
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            query = "SELECT * FROM cp_user_profile up inner join service_route sr on up.service_type_id=sr.service_type where up.account_id='" + accountId +
                    "' and sr.parent_service_type='" + parentServiceType + "'";
            prepstat = con.prepareStatement (query);
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                ContentType ct = new ContentType ();
                ct.setParentServiceType (rs.getInt ("sr.parent_service_type"));
                ct.setServiceName (rs.getString ("sr.service_name"));
                ct.setServiceType (rs.getInt ("sr.service_type"));
                types.add (ct);
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }finally{
            if (con != null) {
                con.close ();
            }
            return types;
        }
    }
    
    public static boolean isRegisteredWithProfile (int serviceType, String accountId) throws Exception {
        boolean isRegistered = false;
        
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            query = "SELECT * FROM cp_user_profile where account_id='" + accountId + "' and service_type_id='" + serviceType + "'";
            prepstat = con.prepareStatement (query);
            rs = prepstat.executeQuery ();
            
            if (rs.next ()) {
                isRegistered = true;
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
        } finally {
            if (con != null) {
                con.close ();
            }
            return isRegistered;
        }
    }
    
}
