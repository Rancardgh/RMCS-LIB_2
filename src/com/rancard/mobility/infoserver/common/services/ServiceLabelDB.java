package com.rancard.mobility.infoserver.common.services;

import com.rancard.common.DConnect;
import com.rancard.util.SystemLogger;

import java.sql.Connection;
import java.sql.ResultSet;

/**
 * Created with IntelliJ IDEA.
 * User: Mustee
 * Date: 9/17/13
 * Time: 5:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServiceLabelDB {
    private static SystemLogger logger = new SystemLogger(ServiceLabelDB.class);

    public static void createServiceLabel(String accountID, String keyword, String header, String footer) throws Exception {
        Connection conn = null;
        String sql = "INSERT INTO service_labels(account_id, keyword, header, footer) VALUES('" + accountID + "', '" + keyword + "', '" + header + "', '" + footer + "')";


        try {
            conn = DConnect.getConnection();

            logger.log(SystemLogger.Level.DEBUG, "About to create service label: " + sql);
            conn.createStatement().execute(sql);

        } catch (Exception e) {
            logger.log(SystemLogger.Level.ERROR, "Preblem creating service label: "+e.getMessage());
            throw new Exception(e.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static ServiceLabel getServiceLabel(String accountID, String keyword) throws Exception{
        Connection conn = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM service_labels WHERE account_id = '"+accountID+"' AND keyword = '"+keyword+"'";

        try {
            conn = DConnect.getConnection();

            logger.log(SystemLogger.Level.DEBUG, "About to get service label: " + sql);
            rs = conn.createStatement().executeQuery(sql);

            if(rs.next()){
                return new ServiceLabel(rs.getString("account_id"), rs.getString("keyword"), rs.getString("header"), rs.getString("footer"));
            }

            return null;

        } catch (Exception e) {
            logger.log(SystemLogger.Level.ERROR, "Problem getting service label: "+e.getMessage());
            throw new Exception(e.getMessage());
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
