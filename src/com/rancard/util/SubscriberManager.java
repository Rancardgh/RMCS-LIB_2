/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.util;

import java.util.ArrayList;
import java.util.HashMap;
import redis.clients.jedis.Jedis;
import com.rancard.common.DConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

/**
 *
 * @author kweku
 */
public class SubscriberManager {

    private Jedis jedis;

    public SubscriberManager () throws Exception {

        try {
            Jedis jedis = new Jedis ("localhost");
            this.jedis = jedis;
        } catch (Exception e) {
            e.printStackTrace ();
            throw e;
        }
    }

    public SubscriberManager (String dbServerAddr) throws Exception {

        try {
            Jedis jedis = new Jedis (dbServerAddr);
            this.jedis = jedis;
        } catch (Exception e) {
            e.printStackTrace ();
            throw e;
        }
    }

    public void disconnect () {
        this.jedis.disconnect ();
    }

    public String getNetworkIdFromMsisdn (String msisdn) throws Exception {
        msisdn = msisdn.trim ();
        String networkId = "";

        String smscId = "";
        try {
            smscId = jedis.get (msisdn); //check if MSISDN has ported ... then get its new network (SMSC ID)
        } catch (Exception e) {
            System.out.println (new java.util.Date () + ": [subs-mngr:ERROR]: Error retrieving porting details: " + e.getMessage ());
            smscId = "";
        }

        if (smscId != null && !smscId.equals ("")) {
            networkId = getNetworkIdFromSmscId (smscId);
        } else { //SMSC ID is null, means MSISDN has not ported. deduce network from MSISDN format
            HashMap<String, String> regexMap = getMsisdnRegexAndNetworkIds ();
            Iterator<String> regItr = regexMap.keySet ().iterator ();
            
            if (regItr != null) {
                while (regItr.hasNext ()) {
                    String regEx = regItr.next ();
                    regEx = (regEx == null) ? "" : regEx;
                    
                    boolean matches = java.util.regex.Pattern.matches (regEx, msisdn);
                    if (matches) {
                        //if match made, get corresponding SMSC ID for MSISDN
                        networkId = regexMap.get (regEx);
                        break;
                    }
                }
            }
        }

        return networkId;
    }

    public String getSmscIdFromMsisdn (String msisdn) throws Exception {
        msisdn = msisdn.trim ();

        String smscId = "";
        try {
            smscId = jedis.get (msisdn); //check if MSISDN has ported ... then get its new network (SMSC ID)
        } catch (Exception e) {
            System.out.println (new java.util.Date () + ": [subs-mngr:ERROR]: Error retrieving porting details: " + e.getMessage ());
            smscId = "";
        }

        if (smscId == null || smscId.equals ("")) { //SMSC ID is null, means MSISDN has not ported. deduce network from MSISDN format
            HashMap<String, String> regexMap = getMsisdnRegexAndSmscIds ();
            Iterator<String> regItr = regexMap.keySet ().iterator ();
            
            if (regItr != null) {
                while (regItr.hasNext ()) {
                    String regEx = regItr.next ();
                    regEx = (regEx == null) ? "" : regEx;
                    
                    boolean matches = java.util.regex.Pattern.matches (regEx, msisdn);
                    if (matches) {
                        //if match made, get corresponding SMSC ID for MSISDN
                        smscId = regexMap.get (regEx);
                        break;
                    }
                }
            }
        }

        //if SMSC ID is still null --- well ... we'll have to use the MSISDN to search
        return smscId;
    }

    public String getSmscIdFromNetworkId (String networkId) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        String defaultSmsc = "";

        try {
            con = DConnect.getConnection ();

            SQL = "select default_smsc from mobile_network_operator where mno_id = ?";

            prepstat = con.prepareStatement (SQL);

            prepstat.setString (1, networkId);

            rs = prepstat.executeQuery ();

            if (rs.next ()) {
                defaultSmsc = rs.getString ("default_smsc");
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
        
        return defaultSmsc;
    }
    
    public String getNetworkIdFromSmscId (String smscId) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        String networkId = "";

        try {
            con = DConnect.getConnection ();

            SQL = "select mno_id from mobile_network_operator where default_smsc = ?";

            prepstat = con.prepareStatement (SQL);

            prepstat.setString (1, smscId);

            rs = prepstat.executeQuery ();

            if (rs.next ()) {
                networkId = rs.getString ("mno_id");
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

        return networkId;
    }

    private HashMap<String, String> getMsisdnRegexAndNetworkIds () throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        HashMap<String, String> msisdnRegex = new HashMap<String, String> ();

        try {
            con = DConnect.getConnection ();

            SQL = "select msisdn_regex, mno_id from mobile_network_operator";

            prepstat = con.prepareStatement (SQL);
            rs = prepstat.executeQuery ();

            while (rs.next ()) {
                msisdnRegex.put (rs.getString ("msisdn_regex"), rs.getString ("mno_id"));
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

        return msisdnRegex;
    }
    
    private HashMap<String, String> getMsisdnRegexAndSmscIds () throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        HashMap<String, String> msisdnRegex = new HashMap<String, String> ();

        try {
            con = DConnect.getConnection ();

            SQL = "select msisdn_regex, default_smsc from mobile_network_operator";

            prepstat = con.prepareStatement (SQL);
            rs = prepstat.executeQuery ();

            while (rs.next ()) {
                msisdnRegex.put (rs.getString ("msisdn_regex"), rs.getString ("default_smsc"));
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

        return msisdnRegex;
    }
}
