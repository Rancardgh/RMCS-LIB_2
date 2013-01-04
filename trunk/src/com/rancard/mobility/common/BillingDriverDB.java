/*
 * BillingDriverDB.java
 *
 * Created on February 8, 2007, 4:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.mobility.common;

import com.rancard.common.DConnect;
import com.rancard.common.Feedback;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Messenger
 */
public abstract class BillingDriverDB {
    
    //DB Access methods
    public static String viewBillingUrl (String networkPrefix, String command) throws Exception {
        String url = "";
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            query = "SELECT billing_url from network_billing WHERE network_prefix='" + networkPrefix + "' and command='" + command + "'";
            prepstat = con.prepareStatement (query);
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                url = rs.getString ("biling_url");
            }
            if(url == null || url.equals ("")) {
                throw new Exception (Feedback.NO_BILLING_URL);
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
        return url;        
    }
    
}
