/*
 * ForwardedServiceDB.java
 *
 * Created on January 5, 2007, 12:06 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.mobility.extension;

import com.rancard.common.DConnect;
import com.rancard.mobility.infoserver.InfoService;
import com.rancard.mobility.infoserver.livescore.LiveScoreService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *
 * @author Messenger
 */
public abstract class ForwardedServiceDB {
    
    public static void createForwardedService (ForwardedService service) throws Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        boolean bError = false;
        
        try {
            con = DConnect.getConnection ();
            con.setAutoCommit (false);
            
            SQL = "select * from service_definition where keyword='" + service.getKeyword () + "' and account_id='" + service.getAccountId () + "'";
            prepstat = con.prepareStatement (SQL);
            rs = prepstat.executeQuery ();
            
            if (!rs.next ()) {
                //service is not registered - register it
                SQL = "insert into service_definition(service_type,keyword,account_id,service_name,default_message,is_basic,last_updated) values(?,?,?,?,?,?,?)";
                
                prepstat = con.prepareStatement (SQL);
                prepstat.setString (1, service.getServiceType ());
                prepstat.setString (2, service.getKeyword ());
                prepstat.setString (3, service.getAccountId ());
                prepstat.setString (4, service.getServiceName ());
                prepstat.setString (5, service.getDefaultMessage ());
                prepstat.setInt (6, 0);
                prepstat.setTimestamp (7, service.now ());
                prepstat.execute ();
            }
            
            SQL = "select * from service_forwarding where keyword='" + service.getKeyword () + "' and account_id='" + service.getAccountId () + "'";
            prepstat = con.prepareStatement (SQL);
            rs = prepstat.executeQuery ();
            if (!rs.next ()) {
                SQL =   "insert into service_forwarding (keyword,account_id,url,timeout,listen_status) values(?,?,?,?,?)";
                prepstat = con.prepareStatement (SQL);
                prepstat.setString (1, service.getKeyword ());
                prepstat.setString (2, service.getAccountId ());
                prepstat.setString (3, service.getUrl ());
                prepstat.setInt (4, service.getTimeout ());
                prepstat.setInt (5, service.getListenStatus ());
                prepstat.execute ();
            }
        } catch (Exception ex) {
            
            bError = true;
            try{
                deleteForwardedService (service.getKeyword (), service.getAccountId ());
            }catch(Exception ee){}
            throw new Exception (ex.getMessage ());
        } finally {
            
            if (bError) {
                con.rollback ();
            } else {
                con.commit ();
            }
            
            if (con != null) {
                con.close ();
            }
        }
    }
    
    public static void updateForwardedServiceName (String keyword, String account_id, String serviceName) throws Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        boolean bError = false;
        
        try {
            con = DConnect.getConnection ();
            con.setAutoCommit (false);
            
            SQL = "update service_definition set service_name=?,last_updated=? where keyword=? and account_id=?";
            
            prepstat = con.prepareStatement (SQL);
            
            prepstat.setString (1, serviceName);
            prepstat.setTimestamp (2, new Timestamp (java.util.Calendar.getInstance ().getTime ().getTime ()));
            prepstat.setString (3, keyword);
            prepstat.setString (4, account_id);
            
            prepstat.execute ();
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            bError = true;
            throw new Exception (ex.getMessage ());
        } finally {
            
            if (con != null) {
                con.close ();
            }
        }
    }
    
    public static void updateForwardedServiceParameters (String keyword, String account_id, String url, int timeout, int listenStatus) throws Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        boolean bError = false;
        
        try {
            con = DConnect.getConnection ();
            con.setAutoCommit (false);
            
            SQL = "select * from service_definition where keyword='" + keyword + "' and account_id='" + account_id + "'";
            prepstat = con.prepareStatement (SQL);
            rs = prepstat.executeQuery ();
            
            if (rs.next ()) {
                SQL = "update service_forwarding f, service_definition d set f.url=?,f.timeout=?,f.listen_status=?,d.last_updated=? where d.keyword=? and d.account_id=? and " + 
                        "d.keyword=f.keyword and d.account_id=f.account_id";
                
                prepstat = con.prepareStatement (SQL);
                //prepstat.clearBatch();
                
                prepstat.setString (1, url);
                prepstat.setInt (2, timeout);
                prepstat.setInt (3, listenStatus);
                prepstat.setTimestamp (4, new Timestamp (java.util.Calendar.getInstance ().getTime ().getTime ()));
                prepstat.setString (5, keyword);
                prepstat.setString (6, account_id);
                
                prepstat.execute ();
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            bError = true;
            throw new Exception (ex.getMessage ());
        } finally {
            
            if (con != null) {
                con.close ();
            }
        }
    }
    
    public static void updateForwardedService (String keyword, String account_id, String name, String url, int timeout, int listenStatus) throws Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        boolean bError = false;
        
        try {
            con = DConnect.getConnection ();
            con.setAutoCommit (false);
            
            // if service already exists ensure that time is duration is not the same on the competitions table
            //for service
            SQL = "select * from service_definition where keyword='" + keyword + "' and account_id='" + account_id + "'";
            prepstat = con.prepareStatement (SQL);
            rs = prepstat.executeQuery ();
            
            if (rs.next ()) {
                SQL = "update service_forwarding f, service_definition d set f.url=?,f.timeout=?,f.listen_status=?,d.last_updated=?,d.service_name=? where d.keyword=? and d.account_id=? and " + 
                        "d.keyword=f.keyword and d.account_id=f.account_id";
                
                prepstat = con.prepareStatement (SQL);
                //prepstat.clearBatch();
                
                prepstat.setString (1, url);
                prepstat.setInt (2, timeout);
                prepstat.setInt (3, listenStatus);
                prepstat.setTimestamp (4, new Timestamp (java.util.Calendar.getInstance ().getTime ().getTime ()));
                prepstat.setString (5, name);
                prepstat.setString (6, keyword);
                prepstat.setString (7, account_id);
                
                prepstat.execute ();
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            bError = true;
            throw new Exception (ex.getMessage ());
        } finally {
            
            if (con != null) {
                con.close ();
            }
        }
    }
    
    public static ForwardedService viewForwardedService (String keyword, String accountId) throws Exception {
        
        ForwardedService service = new ForwardedService ();
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection ();
            
            SQL = "SELECT * FROM service_definition s inner join service_forwarding k on s.keyword=k.keyword and s.account_id=k.account_id where s.keyword='" + keyword +
                    "' and s.account_id='" + accountId + "'";
            
            prepstat = con.prepareStatement (SQL);
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                service.setAccountId (rs.getString ("s.account_id"));
                service.setDefaultMessage (rs.getString ("s.default_message"));
                service.setKeyword (rs.getString ("s.keyword"));
                service.setLastUpdated (rs.getString ("s.last_updated"));
                service.setServiceName (rs.getString ("s.service_name"));
                service.setServiceType (rs.getString ("s.service_type"));
                service.setListenStatus (rs.getInt ("listen_status"));
                service.setTimeout (rs.getInt ("timeout"));
                service.setUrl (rs.getString ("url"));
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
        
        return service;
    }
    
    public static ArrayList viewAllForwardedServices (String accountId) throws Exception {
        
        ArrayList services = new ArrayList ();
        
        String SQL;
        ResultSet rs = null;
        ResultSet rs2 = null;
        Connection con = null;
        PreparedStatement prepstat, prepstat2 = null;
        
        try {
            con = DConnect.getConnection ();
            
            SQL = "SELECT * FROM service_definition s inner join service_forwarding k on s.keyword=k.keyword and s.account_id=k.account_id where s.account_id='" + accountId + "'";
            
            prepstat = con.prepareStatement (SQL);
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                ForwardedService service = new ForwardedService ();
                
                service.setAccountId (rs.getString ("s.account_id"));
                service.setDefaultMessage (rs.getString ("s.default_message"));
                service.setKeyword (rs.getString ("s.keyword"));
                service.setLastUpdated (rs.getString ("s.last_updated"));
                service.setServiceName (rs.getString ("s.service_name"));
                service.setServiceType (rs.getString ("s.service_type"));
                service.setListenStatus (rs.getInt ("listen_status"));
                service.setTimeout (rs.getInt ("timeout"));
                service.setUrl (rs.getString ("url"));
                
                services.add (service);
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
        
        return services;
    }
    
    public static void deleteForwardedService (String keyword, String account_id) throws Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            
            //from service definition table
            SQL = "delete from service_definition d, service_forwarding f where d.keyword='" + keyword + "' and d.account_id='" + account_id + 
                    "' and d.account_id=f.account_id and d.keyword=f.keyword";
            prepstat = con.prepareStatement (SQL);
            prepstat.execute ();
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        } finally {
            
            if (con != null) {
                con.close ();
            }
        }
    }
}
