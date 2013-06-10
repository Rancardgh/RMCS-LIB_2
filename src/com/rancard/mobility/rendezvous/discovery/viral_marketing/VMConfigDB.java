/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.rendezvous.discovery.viral_marketing;

import com.rancard.common.DConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author kweku
 */
public class VMConfigDB {
    
    public static VMConfig getVmConfig (String accountId) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        VMConfig vmConfig = new VMConfig ();

        try {
            con = DConnect.getConnection ();

            SQL = "select * from vm_config where account_id = ?";

            prepstat = con.prepareStatement (SQL);
            prepstat.setString (1, accountId);

            rs = prepstat.executeQuery ();

            while (rs.next ()) {
                vmConfig.setAccountId (rs.getString ("account_id"));
                vmConfig.setDefaultService (rs.getString ("default_service"));
                vmConfig.setNumOfSignificanrDigitsForRecipient (rs.getInt ("recipient_num_significant_digits"));
                vmConfig.setNumOfSignificanrDigitsForSender (rs.getInt ("sender_num_significant_digits"));
                vmConfig.setRecipientUnifiedProfix (rs.getString ("recipient_unified_prefix"));
                vmConfig.setSenderUnifiedPrefix (rs.getString ("sender_unified_prefix"));
                vmConfig.setShareUrl (rs.getString ("share_url"));
                
                if (rs.getString ("use_msisdn_as_sender") != null && rs.getString ("use_msisdn_as_sender").equalsIgnoreCase ("y")) {
                    vmConfig.setMsisdnAsSender ();
                } else {
                    vmConfig.unsetMsisdnAsSender ();
                }
            }

        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close ();
                } catch (SQLException ex1) {
                    System.out.println (ex1.getMessage ());
                }
                con = null;
            }

        } finally {
            if (rs != null) {
                try {
                    rs.close ();
                } catch (SQLException e) {
                    ;
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close ();
                } catch (SQLException e) {
                    ;
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close ();
                } catch (SQLException e) {
                    ;
                }
                con = null;
            }
        }

        return vmConfig;
    }
    
}
