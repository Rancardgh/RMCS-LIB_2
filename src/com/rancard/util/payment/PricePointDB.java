package com.rancard.util.payment;

import com.rancard.common.DConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class PricePointDB {
    public static void createPricePoint(PricePoint pricePoint)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            String SQL = "";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            if (rs.next()) {
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    public static void updatePricePoint(PricePoint pricePoint)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            String SQL = "";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            if (rs.next()) {
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    public static void updatePricePointValue(String pricePointId, String newValue)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            String SQL = "";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            if (rs.next()) {
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    public static void updatePricePointCurrency(String pricePointId, String newCurrency)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            String SQL = "";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            if (rs.next()) {
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    public static void updatePricePointURL(String pricePointId, String newURL)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            String SQL = "";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            if (rs.next()) {
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    public static void deletePricePoint(String pricePointId)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            String SQL = "";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            if (rs.next()) {
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    public static PricePoint viewPricePointFor(String[] pricePointIds, String msisdn)
            throws Exception {
        PricePoint point = new PricePoint();
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        String string = "";
        if ((pricePointIds != null) || (pricePointIds.length > 0)) {
            string = "'" + pricePointIds[0] + "',";
            for (int i = 1; i < pricePointIds.length; i++) {
                string = string + "'" + pricePointIds[i] + "',";
            }
        }
        string = string.substring(0, string.lastIndexOf(","));
        try {
            con = DConnect.getConnection();
            String SQL = "select * from price_points where price_point_id in (" + string + ")";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            while (rs.next()) {
                String network = rs.getString("network_prefix");
                if (msisdn.substring(msisdn.indexOf("+") + 1, network.length() + msisdn.indexOf("+") + 1).equals(network)) {
                    point.setPricePointId(rs.getString("price_point_id"));
                    point.setBillingUrl(rs.getString("billing_url"));
                    point.setCurrency(rs.getString("currency"));
                    point.setNetworkPrefix(network);
                    point.setBillingMech(rs.getString("billing_mech"));
                    point.setValue(rs.getString("value"));
                    break;
                }
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return point;
    }

    public static PricePoint viewPricePoint(String pricePointId)
            throws Exception {
        PricePoint pricePoint = new PricePoint();


        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            String SQL = "select * from price_points where price_point_id='" + pricePointId + "'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            if (rs.next()) {
                pricePoint.setBillingUrl(rs.getString("billing_url"));
                pricePoint.setCurrency(rs.getString("currency"));
                pricePoint.setNetworkPrefix(rs.getString("network_prefix"));
                pricePoint.setPricePointId(rs.getString("price_point_id"));
                pricePoint.setBillingMech(rs.getString("billing_mech"));
                pricePoint.setValue(rs.getString("value"));
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return pricePoint;
    }

    public static ArrayList viewPricePointsSupportedByAccount(String accountId)
            throws Exception {
        ArrayList pricePoints = new ArrayList();


        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            String SQL = "SELECT p.* FROM price_points p inner join cp_connections c on p.network_prefix=c.allowed_networks where c.list_id='" + accountId + "' order by p.network_prefix";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            while (rs.next()) {
                PricePoint pricePoint = new PricePoint();

                pricePoint.setBillingUrl(rs.getString("p.billing_url"));
                pricePoint.setCurrency(rs.getString("p.currency"));
                pricePoint.setNetworkPrefix(rs.getString("p.network_prefix"));
                pricePoint.setPricePointId(rs.getString("p.price_point_id"));
                pricePoint.setValue(rs.getString("p.value"));
                pricePoint.setBillingMech(rs.getString("p.billing_mech"));

                pricePoints.add(pricePoint);
            }
            return pricePoints;
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            return pricePoints;
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    public static ArrayList viewPricePoints(String[] pricePointIds)
            throws Exception {
        ArrayList pricePoints = new ArrayList();


        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        String string = "";
        if ((pricePointIds != null) || (pricePointIds.length > 0)) {
            string = "'" + pricePointIds[0] + "',";
            for (int i = 1; i < pricePointIds.length; i++) {
                string = string + "'" + pricePointIds[i] + "',";
            }
        }
        string = string.substring(0, string.lastIndexOf(","));
        try {
            con = DConnect.getConnection();
            String SQL = "select * from price_points where price_point_id in (" + string + ")";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            while (rs.next()) {
                PricePoint pricePoint = new PricePoint();

                pricePoint.setBillingUrl(rs.getString("billing_url"));
                pricePoint.setCurrency(rs.getString("currency"));
                pricePoint.setNetworkPrefix(rs.getString("network_prefix"));
                pricePoint.setPricePointId(rs.getString("price_point_id"));
                pricePoint.setValue(rs.getString("value"));
                pricePoint.setBillingMech(rs.getString("billing_mech"));

                pricePoints.add(pricePoint);
            }
            return pricePoints;
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            return pricePoints;
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    public static ArrayList viewPricePoints_al()
            throws Exception {
        ArrayList pricePoints = new ArrayList();


        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            String SQL = "select * from price_points";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            while (rs.next()) {
                PricePoint pricePoint = new PricePoint();

                pricePoint.setBillingUrl(rs.getString("billing_url"));
                pricePoint.setCurrency(rs.getString("currency"));
                pricePoint.setNetworkPrefix(rs.getString("network_prefix"));
                pricePoint.setPricePointId(rs.getString("price_point_id"));
                pricePoint.setValue(rs.getString("value"));
                pricePoint.setBillingMech(rs.getString("billing_mech"));

                pricePoints.add(pricePoint);
            }
            return pricePoints;
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            return pricePoints;
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    public static HashMap viewPricePoints_hm()
            throws Exception {
        HashMap pricePoints = new HashMap();


        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            String SQL = "select * from price_points";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            while (rs.next()) {
                String key = rs.getString("price_point_id");
                PricePoint pricePoint = new PricePoint();

                pricePoint.setBillingUrl(rs.getString("billing_url"));
                pricePoint.setCurrency(rs.getString("currency"));
                pricePoint.setNetworkPrefix(rs.getString("network_prefix"));
                pricePoint.setPricePointId(rs.getString("price_point_id"));
                pricePoint.setValue(rs.getString("value"));
                pricePoint.setBillingMech(rs.getString("billing_mech"));

                pricePoints.put(key, pricePoint);
            }
            return pricePoints;
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            return pricePoints;
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    public static boolean isPricingValid(String[] pricePointIds)
            throws Exception {
        boolean isValid = true;


        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        String string = "";
        if ((pricePointIds != null) || (pricePointIds.length > 0)) {
            string = "'" + pricePointIds[0] + "',";
            for (int i = 1; i < pricePointIds.length; i++) {
                string = string + "'" + pricePointIds[i] + "',";
            }
        }
        string = string.substring(0, string.lastIndexOf(","));
        try {
            con = DConnect.getConnection();
            String SQL = "select count(network_prefix) as instances from price_points where price_point_id in (" + string + ") group by network_prefix";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            while (rs.next()) {
                if (rs.getInt("instances") != 1) {
                    isValid = false;
                    break;
                }
                isValid = true;
            }
            return isValid;
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            return isValid;
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }
}



/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar

 * Qualified Name:     com.rancard.util.payment.PricePointDB

 * JD-Core Version:    0.7.0.1

 */