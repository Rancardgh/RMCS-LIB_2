/*
 * UserServiceTransactionDB.java
 *
 * Created on February 13, 2007, 3:26 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.mobility.infoserver.common.services;

import com.rancard.common.DConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Messenger
 */
public abstract class UserServiceTransactionDB {
    
    public static void createTransaction(UserServiceTransaction trans) throws Exception {
        
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection();
            query = "INSERT into transactions (trans_id,keyword,account_id,msisdn,callback_url,date,msg,is_billed,is_completed,price_point_id) values(?,?,?,?,?,?,?,?,?,?)";
            
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, trans.getTransactionId ());
            prepstat.setString(2, trans.getKeyword ());
            prepstat.setString(3, trans.getAccountId ());
            prepstat.setString(4, trans.getMsisdn ());
            prepstat.setString(5, trans.getCallBackUrl ());
            prepstat.setTimestamp(6, trans.getDate ());
            prepstat.setString(7, trans.getMsg ());
            prepstat.setInt(8, trans.getIsBilled ());
            prepstat.setInt (9, trans.getIsCompleted ());
            prepstat.setString(10, trans.getPricePoint ());
            
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
    
    public static void updateTransaction(String transId, int isCompleted, int isBilled) throws Exception {
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection();
            query = "UPDATE transactions SET is_billed=" + isBilled + ", is_completed=" + isCompleted + " WHERE trans_id='" + transId + "'";
            prepstat = con.prepareStatement(query);
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
    
    public static void deleteTransaction(String transId) throws Exception {
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection();
            query = "DELETE from transactions WHERE trans_id=?";
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, transId);
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
    
    public static UserServiceTransaction viewTransaction(String transId) throws Exception {
        UserServiceTransaction newBean = new UserServiceTransaction();
        
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection();
            query = "select * from transactions WHERE trans_id='" + transId + "'";
            prepstat = con.prepareStatement(query);
            rs = prepstat.executeQuery();
            
            while (rs.next()) {
                newBean.setAccountId (rs.getString ("account_id"));
                newBean.setDate (rs.getTimestamp ("date"));
                newBean.setIsBilled (rs.getInt ("is_billed"));
                newBean.setIsCompleted (rs.getInt ("is_completed"));
                newBean.setKeyword (rs.getString ("keyword"));
                newBean.setCallBackUrl (rs.getString ("callback_url"));
                newBean.setMsg (rs.getString ("msg"));
                newBean.setMsisdn (rs.getString ("msisdn"));
                newBean.setTransactionId (rs.getString ("trans_id"));
                newBean.setPricePoint (rs.getString ("price_point_id"));
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
}
